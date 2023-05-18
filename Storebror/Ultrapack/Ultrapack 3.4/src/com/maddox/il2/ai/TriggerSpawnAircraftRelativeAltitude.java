// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CollideEnv;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.Mission;
import com.maddox.sas1946.il2.util.TrueRandom;

public class TriggerSpawnAircraftRelativeAltitude extends Trigger {

    public TriggerSpawnAircraftRelativeAltitude(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, String targetActorNames, int altitudeMin, int altitudeMax, int triggeredBy, boolean triggerOnExit, int noObjectsMin, int probability,
            int deltaAltitude, String linkActorName, String displayMessage, float displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        this.setTargetActorNames(targetActorNames);
        this.setTriggerClass(TYPE_SPAWN_AIRCRAFT_RELATIVE_ALTITUDE);
        if (this.getTargetActorNames() == null || this.getTargetActorNames().size() < 1) this.destroy();
        for (int targetActorNameIndex = 0; targetActorNameIndex < this.getTargetActorNames().size(); targetActorNameIndex++) {
            String targetActorName = (String)this.getTargetActorNames().get(targetActorNameIndex);
            if (targetActorName.indexOf("Trigger") >= 0) World.cur().triggersGuard.getListTriggerTriggerActivate().add(targetActorName);
            else {
                World.cur().triggersGuard.getListTriggerAircraftSpawn().add(targetActorName);
                World.cur().triggersGuard.getListTriggerAircraftAirSpawnRelativeAltitude().add(targetActorName);
            }
        }
        
//        this.setTriggerClass(TYPE_SPAWN_AIRCRAFT_RELATIVE_ALTITUDE);
//        this.setTargetActorName(targetActorName);
//        this.setDeltaAltitude(deltaAltitude);
//        if (this.getTargetActorName() == "" || this.getTargetActorName() == null) this.destroy();
//        World.cur().triggersGuard.getListTriggerAircraftSpawn().add(this.getTargetActorName());
//        World.cur().triggersGuard.getListTriggerAircraftAirSpawnRelativeAltitude().add(this.getTargetActorName());
    }

    protected boolean checkPeriodic() {
        boolean allTargetActorsSpawned = true;
        if (this.getTargetActorNames() != null && this.getTargetActorNames().size() > 0) {
            for (int targetActorNameIndex = 0; targetActorNameIndex < this.getTargetActorNames().size(); targetActorNameIndex++) {
                String targetActorName = (String)this.getTargetActorNames().get(targetActorNameIndex);
                if (Actor.getByName(targetActorName) == null) {
                    allTargetActorsSpawned = false;
                    break;
                }
            }
        }
        
//        if (Actor.getByName(this.getTargetActorName()) == null) {
        if (!allTargetActorsSpawned) {
            this.checkMove();
            if (this.getLinkActorName() != "" && this.getLinkActorName() != null && Actor.getByName(this.getLinkActorName()) == null) return false;
            CollideEnv.ResultTrigger result = Engine.collideEnv().getEnemiesInCyl(this.getPosTrigger(), this.getRadius(), this.getAltitudeMin(), this.getAltitudeMax(), this.getTriggeredByArmy(), this.getTriggeredBy(),
                    this.getNoObjectsMin());
            if (this.isTriggerOnExit()) {
                if (this.isEntered()) {
                    this.setMessageAltitude((int) result.altiSea);
                    return !result.result;
                }
                if (result.result) {
                    this.setEntered(true);
                    this.setAltitude(result.altiSea + this.getDeltaAltitude());
                }
            } else if (result.result) {
                this.setAltitude(result.altiSea + this.getDeltaAltitude());
                this.setMessageAltitude((int) result.altiSea);
                return true;
            }
            return false;
        } else {
            this.destroy();
            return false;
        }
    }

    protected void execute() {
        if (TrueRandom.nextFloat(100F) < this.getProbability()) this.doExecute();
        super.execute();
    }
    
    protected void doExecute() {
        if (this.isTriggered()) return;
        this.setTriggered(true);

        for (int targetActorNameIndex = 0; targetActorNameIndex < this.getTargetActorNames().size(); targetActorNameIndex++) {
            String targetActorName = (String)this.getTargetActorNames().get(targetActorNameIndex);
            if (targetActorName != null && Actor.getByName(targetActorName) == null) {
                if (targetActorName.indexOf("Trigger") > 0) {
                    Actor triggerTargetActor = World.cur().triggersGuard.getTrigger(targetActorName);
                    if (triggerTargetActor instanceof Trigger) {
                        ((Trigger) triggerTargetActor).setActivated(true);
                        // System.out.println("Trigger " + ((Trigger)triggerTargetActor).name() + " activated!");
                    }
                }
                else
                    Mission.cur().loadWingOnTrigger(targetActorName);
            }
            if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
                EventLog.onTriggerActivate(Actor.getByName(targetActorName), this);
                this.doSendMsg(false);
            } else {
                EventLog.onTriggerActivateLink(Actor.getByName(targetActorName), this);
                this.doSendMsg(true);
            }
        }
//        if (this.getTargetActorName() != null && Actor.getByName(this.getTargetActorName()) == null) Mission.cur().loadWingOnTrigger(this.getTargetActorName());
//        if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
//            EventLog.onTriggerActivate(Actor.getByName(this.getTargetActorName()), this);
//            this.doSendMsg(false);
//        } else {
//            EventLog.onTriggerActivateLink(Actor.getByName(this.getTargetActorName()), this);
//            this.doSendMsg(true);
//        }
        super.doExecute();
    }

    public double getAltitude() {
        return this.altitude;
    }

//    public String getTargetActorName() {
//        return this.targetActorName;
//    }

    public void destroy() {
        super.destroy();
        for (int targetActorNameIndex = 0; targetActorNameIndex < this.getTargetActorNames().size(); targetActorNameIndex++) {
            String targetActorName = (String)this.getTargetActorNames().get(targetActorNameIndex);
            if (targetActorName.indexOf("Trigger") >= 0)
                World.cur().triggersGuard.getListTriggerTriggerActivate().remove(World.cur().triggersGuard.getListTriggerTriggerActivate().indexOf(targetActorName));
            else {
                World.cur().triggersGuard.getListTriggerAircraftAirSpawnRelativeAltitude().remove(World.cur().triggersGuard.getListTriggerAircraftAirSpawnRelativeAltitude().indexOf(targetActorName));
                World.cur().triggersGuard.getListTriggerAircraftSpawn().remove(World.cur().triggersGuard.getListTriggerAircraftSpawn().indexOf(targetActorName));
            }
        }
//        World.cur().triggersGuard.getListTriggerAircraftAirSpawnRelativeAltitude().remove(World.cur().triggersGuard.getListTriggerAircraftAirSpawnRelativeAltitude().indexOf(this.getTargetActorName()));
//        World.cur().triggersGuard.getListTriggerAircraftSpawn().remove(World.cur().triggersGuard.getListTriggerAircraftSpawn().indexOf(this.getTargetActorName()));
    }

//    public void setTargetActorName(String targetActorName) {
//        this.targetActorName = targetActorName;
//    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getDeltaAltitude() {
        return this.deltaAltitude;
    }

    public void setDeltaAltitude(int deltaAltitude) {
        this.deltaAltitude = deltaAltitude;
    }
    
    public ArrayList getTargetActorNames() {
        return this.targetActorNames;
    }

    private void setTargetActorNames(String targetActorNames) {
        this.targetActorNames = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(targetActorNames, "|");
        while (tokenizer.hasMoreTokens())
            this.targetActorNames.add(tokenizer.nextToken());
    }

    private ArrayList targetActorNames;// = new ArrayList();
//    private String targetActorName;
    private double altitude;
    private int    deltaAltitude;
}
