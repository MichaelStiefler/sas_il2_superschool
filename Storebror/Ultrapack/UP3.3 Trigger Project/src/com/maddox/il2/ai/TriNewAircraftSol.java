// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.Random;

import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.ground.ChiefGround;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Main;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.trains.Train;
import com.maddox.il2.objects.vehicles.artillery.RocketryGeneric;
import com.maddox.rts.NetEnv;

public class TriNewAircraftSol extends Trigger {

    public TriNewAircraftSol(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, String targetActorName, int altitudeMin, int altitudeMax, int triggeredBy, boolean hasTriggerActor, int noObjectsMin, int probability,
            String linkActorName, String displayMessage, int displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, hasTriggerActor, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        this.setTargetActorName(targetActorName);
        if (this.getTargetActorName() == "" || this.getTargetActorName() == null) this.destroy();
        if (this.getTargetActorName().indexOf("Chief") >= 0 || this.getTargetActorName().indexOf("Rocket") >= 0) World.cur().triggersGuard.getListTriggerChiefActivate().add(this.getTargetActorName());
        else World.cur().triggersGuard.getListTriggerAircraftActivate().add(this.getTargetActorName());
    }

    protected void execute() {
        if (new Random().nextFloat() * 100F + 1.0F <= this.getProbability()) {
            this.setTriggered(true);
            if (this.getTargetActorName() != null) {
                if (this.getTargetActorName().indexOf("Chief") >= 0 || this.getTargetActorName().indexOf("Rocket") >= 0) {
                    if (Main.cur().netServerParams.isMaster()) ((NetUser) NetEnv.host()).replicateTriggerStartGround(this.getTargetActorName());
                    startGround(this.getTargetActorName());
                } else {
                    Wing wing = (Wing) Actor.getByName(this.getTargetActorName());
                    for (int i = 0; i < 4; i++)
                        if (wing.airc[i] != null) {
                            Aircraft aircraft = wing.airc[i];
                            if (aircraft.FM instanceof Maneuver) ((Maneuver) aircraft.FM).triggerTakeOff = true;
                        }

                }
                if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
                    EventLog.onTriggerActivate(Actor.getByName(this.getTargetActorName()), this);
                    this.doSendMsg(false);
                } else {
                    EventLog.onTriggerActivateLink(Actor.getByName(this.getTargetActorName()), this);
                    this.doSendMsg(true);
                }
            }
        }
        super.execute();
    }

    public void destroy() {
        super.destroy();
        if (this.getTargetActorName().indexOf("Chief") >= 0 || this.getTargetActorName().indexOf("Rocket") >= 0)
            World.cur().triggersGuard.getListTriggerChiefActivate().remove(World.cur().triggersGuard.getListTriggerChiefActivate().indexOf(this.getTargetActorName()));
        else World.cur().triggersGuard.getListTriggerAircraftActivate().remove(World.cur().triggersGuard.getListTriggerAircraftActivate().indexOf(this.getTargetActorName()));
    }

    public static void startGround(String nameUnit) {
        Actor actor = Actor.getByName(nameUnit);
        if (actor instanceof RocketryGeneric) ((RocketryGeneric) actor).startMove();
        else if (actor instanceof Train) ((Train) actor).startMove();
        else if (actor instanceof BigshipGeneric) ((BigshipGeneric) actor).startMove();
        else if (actor instanceof ShipGeneric) ((ShipGeneric) actor).startMove();
        else if (actor instanceof ChiefGround) ((ChiefGround) actor).startMove();
    }

    public String getTargetActorName() {
        return this.targetActorName;
    }

    public void setTargetActorName(String targetActorName) {
        this.targetActorName = targetActorName;
    }

    private String targetActorName;
}
