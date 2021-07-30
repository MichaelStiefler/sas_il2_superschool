package com.maddox.il2.net;

import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.Main;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class NetMaxLag {

    public NetMaxLag() {
        this.warningCounter = 0;
        this.lastWarningTime = -1D;
    }

    private void checkLag(Aircraft aircraftToCheck, boolean isServerCheck) {
        double timeInSeconds = Time.real() / 1000D;
        NetServerParams netserverparams = Main.cur().netServerParams;
        double cheaterWarningDelay = netserverparams.cheaterWarningDelay();
        if (!isServerCheck) cheaterWarningDelay += 3D;
        if (this.lastWarningTime > 0.0D && timeInSeconds - this.lastWarningTime < cheaterWarningDelay) return;
        int armyIndex = aircraftToCheck.getArmy();
        Point3d p3dAircraft = aircraftToCheck.pos.getAbsPoint();
        long maxLagAllowed = -1L;
        List targets = Engine.targets();
        int targetListSize = targets.size();
        for (int targetListIterator = 0; targetListIterator < targetListSize; targetListIterator++) {
            Actor targetFromList = (Actor) targets.get(targetListIterator);
            // Search for human player enemy aircraft only!
            if (targetFromList == aircraftToCheck) continue; // target is aircraft to check, ignore!
            if (!(targetFromList instanceof Aircraft)) continue; // target is no aircraft, ignore!
            if (!Actor.isAlive(targetFromList)) continue; // target is already dead, ignore!
            if (targetFromList.getArmy() == armyIndex) continue; // target is from own army, ignore!
            if (!((Aircraft) targetFromList).isNetPlayer()) continue; // target is AI, ignore!
            double distanceToTargetSquared = p3dAircraft.distanceSquared(targetFromList.pos.getAbsPoint());
            long maxLagAllowedFromThisTarget = 0L;
            if (distanceToTargetSquared > far2) {
                if (isServerCheck) maxLagAllowedFromThisTarget = aircraftToCheck.net.masterChannel().getMaxTimeout() / 2;
                else maxLagAllowedFromThisTarget = netserverparams.masterChannel().getMaxTimeout() / 2;
            } else {
                double nearMaxLagTime = netserverparams.nearMaxLagTime();
                if (distanceToTargetSquared > near2) {
                    double nearDistanceToTargetSquared = Math.sqrt(distanceToTargetSquared);
                    nearMaxLagTime += (nearDistanceToTargetSquared - near) / (far - near) * (netserverparams.farMaxLagTime() - netserverparams.nearMaxLagTime());
                }
                maxLagAllowedFromThisTarget = (long) (nearMaxLagTime * 1000D);
            }
            if (maxLagAllowed < 0L || maxLagAllowed > maxLagAllowedFromThisTarget) maxLagAllowed = maxLagAllowedFromThisTarget;
        }

        while (true) {
            if (maxLagAllowed < 0L) break;
            NetChannel masterNetChannel;
            if (isServerCheck) masterNetChannel = aircraftToCheck.net.masterChannel();
            else masterNetChannel = netserverparams.masterChannel();
            if (masterNetChannel == null) break;
            if (maxLagAllowed > masterNetChannel.getCurTimeoutOk()) break;
            this.lastWarningTime = timeInSeconds;
            this.warningCounter++;
            boolean warningCounterElapsed = false;
            if (netserverparams.cheaterWarningNum() >= 0) warningCounterElapsed = this.warningCounter > netserverparams.cheaterWarningNum();
            if (warningCounterElapsed) {
                if (isServerCheck) {
                    Chat.sendLog(0, "user_cheatkick1", aircraftToCheck, null);
                    ((NetUser) NetEnv.host()).kick(aircraftToCheck.netUser());
                } else {
                    Chat.sendLog(0, "user_cheatkick2", (NetUser) NetEnv.host(), null);
                    masterNetChannel.destroy("You have been kicked from the server .");
                }
//            } else if (isServerCheck) {
//                String s = "user_cheating" + (warningCounter % 3 + 1);
//                Chat.sendLog(0, s, aircraftToCheck, null);
//            }
            } else if (isServerCheck) // TODO: Changed by Storebror, only show lag warnings when there's a limit set for lagging.
                if (netserverparams.cheaterWarningNum() > 0) Chat.sendLog(0, "User " + aircraftToCheck.netUser().shortName() + " lags (Warning " + this.warningCounter + "/" + netserverparams.cheaterWarningNum() + ")!", (Aircraft) null, null);
            // ---
            return;
        }
        this.warningCounter = 0; // reset warning counter if cheating doesn't repeat!
        // We get here only when there was no lag violation.
    }

    public void doServerCheck(Aircraft aircraft) {
        this.checkLag(aircraft, true);
    }

    public void doClientCheck() {
        if (!Actor.isAlive(World.getPlayerAircraft())) return;
        else {
            this.checkLag(World.getPlayerAircraft(), false);
            return;
        }
    }

    private static final double far   = 2000D;
    private static final double near  = 100D;
    private static final double far2  = far * far;
    private static final double near2 = near * near;
    private int                 warningCounter;
    private double              lastWarningTime;
}
