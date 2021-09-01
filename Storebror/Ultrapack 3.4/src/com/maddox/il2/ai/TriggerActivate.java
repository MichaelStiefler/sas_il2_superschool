// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.ground.ChiefGround;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.trains.Train;
import com.maddox.il2.objects.vehicles.artillery.RocketryGeneric;
import com.maddox.sas1946.il2.util.TrueRandom;

public class TriggerActivate extends Trigger {

    public TriggerActivate(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, String targetActorNames, int altitudeMin, int altitudeMax, int triggeredBy, boolean triggerOnExit, int noObjectsMin, int probability,
            String linkActorName, String displayMessage, float displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        this.setTargetActorNames(targetActorNames);
        this.setTriggerClass(TYPE_ACTIVATE);
        if (this.getTargetActorNames() == null || this.getTargetActorNames().size() < 1) this.destroy();
        for (int targetActorNameIndex = 0; targetActorNameIndex < this.getTargetActorNames().size(); targetActorNameIndex++) {
            String targetActorName = (String)this.getTargetActorNames().get(targetActorNameIndex);
            if (targetActorName.indexOf("Chief") >= 0 || targetActorName.indexOf("Rocket") >= 0) World.cur().triggersGuard.getListTriggerChiefActivate().add(targetActorName);
            else if (targetActorName.indexOf("Trigger") >= 0) World.cur().triggersGuard.getListTriggerTriggerActivate().add(targetActorName);
            else World.cur().triggersGuard.getListTriggerAircraftActivate().add(targetActorName);
        }
        
        
//        this.setTargetActorName(targetActorName);
//        this.setTriggerClass(TYPE_ACTIVATE);
//        if (this.getTargetActorName() == "" || this.getTargetActorName() == null) this.destroy();
//        if (this.getTargetActorName().indexOf("Chief") >= 0 || this.getTargetActorName().indexOf("Rocket") >= 0) World.cur().triggersGuard.getListTriggerChiefActivate().add(this.getTargetActorName());
//        else if (this.getTargetActorName().indexOf("Trigger") >= 0) World.cur().triggersGuard.getListTriggerTriggerActivate().add(this.getTargetActorName());
//        else World.cur().triggersGuard.getListTriggerAircraftActivate().add(this.getTargetActorName());
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
            if (targetActorName.indexOf("Chief") >= 0 || targetActorName.indexOf("Rocket") >= 0) {
              startGround(targetActorName);
          } else {
              Actor targetActor = Actor.getByName(targetActorName);
              if (targetActor == null && targetActorName.indexOf("Trigger") > 0) {
                  targetActor = World.cur().triggersGuard.getTrigger(targetActorName);
              }
              if (targetActor instanceof Wing) {
                  Wing wing = (Wing)targetActor;
                  for (int i = 0; i < 4; i++) {
                      if (wing.airc[i] != null) {
                          Aircraft aircraft = wing.airc[i];
                          if (aircraft.FM instanceof Maneuver) ((Maneuver) aircraft.FM).triggerTakeOff = true;
                      }
                  }
              }
              else if (targetActor instanceof Trigger) {
                  ((Trigger) targetActor).setActivated(true);
                  // System.out.println("Trigger " + ((Trigger)targetActor).name() + " activated!");

              }
              else {
//                   throw new RuntimeException("TriggerActivate Target Actor Type mismatch, " + (targetActorName == null?"null":targetActorName) + " type is:" + (targetActor==null?"null":targetActor.getClass().getName()));
                   System.out.println("TriggerActivate Target Actor Type mismatch, " + (targetActorName == null?"null":targetActorName) + " type is:" + (targetActor==null?"null":targetActor.getClass().getName()));
              }
          }
          if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
              EventLog.onTriggerActivate(Actor.getByName(targetActorName), this);
              this.doSendMsg(false);
          } else {
              EventLog.onTriggerActivateLink(Actor.getByName(targetActorName), this);
              this.doSendMsg(true);
          }
        }
        
//        if (this.getTargetActorName() != null) {
//            if (this.getTargetActorName().indexOf("Chief") >= 0 || this.getTargetActorName().indexOf("Rocket") >= 0) {
////                if (Main.cur().netServerParams.isMaster()) ((NetUser) NetEnv.host()).replicateTriggerStartGround(this.getTargetActorName());
//                startGround(this.getTargetActorName());
//            } else {
//                Actor targetActor = Actor.getByName(this.getTargetActorName());
//                if (targetActor == null && this.getTargetActorName().indexOf("Trigger") > 0) {
//                    targetActor = World.cur().triggersGuard.getTrigger(this.getTargetActorName());
//                }
//                if (targetActor instanceof Wing) {
//                    Wing wing = (Wing)targetActor;
//                    for (int i = 0; i < 4; i++) {
//                        if (wing.airc[i] != null) {
//                            Aircraft aircraft = wing.airc[i];
//                            if (aircraft.FM instanceof Maneuver) ((Maneuver) aircraft.FM).triggerTakeOff = true;
//                        }
//                    }
//                }
//                else if (targetActor instanceof Trigger) {
//                    ((Trigger) targetActor).setActivated(true);
//                    System.out.println("Trigger " + ((Trigger)targetActor).name() + " activated!");
//
//                }
//                else throw new RuntimeException("TriggerActivate Target Actor Type mismatch, " + this.getTargetActorName() + " type is:" + targetActor==null?"null":targetActor.getClass().getName());
//            }
//            if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
//                EventLog.onTriggerActivate(Actor.getByName(this.getTargetActorName()), this);
//                this.doSendMsg(false);
//            } else {
//                EventLog.onTriggerActivateLink(Actor.getByName(this.getTargetActorName()), this);
//                this.doSendMsg(true);
//            }
//        }
        super.doExecute();
    }

    public void destroy() {
        super.destroy();
        for (int targetActorNameIndex = 0; targetActorNameIndex < this.getTargetActorNames().size(); targetActorNameIndex++) {
            String targetActorName = (String)this.getTargetActorNames().get(targetActorNameIndex);
            if (targetActorName.indexOf("Chief") >= 0 || targetActorName.indexOf("Rocket") >= 0)
                World.cur().triggersGuard.getListTriggerChiefActivate().remove(World.cur().triggersGuard.getListTriggerChiefActivate().indexOf(targetActorName));
            else if (targetActorName.indexOf("Trigger") >= 0)
                World.cur().triggersGuard.getListTriggerTriggerActivate().remove(World.cur().triggersGuard.getListTriggerTriggerActivate().indexOf(targetActorName));
            else World.cur().triggersGuard.getListTriggerAircraftActivate().remove(World.cur().triggersGuard.getListTriggerAircraftActivate().indexOf(targetActorName));
        }
//        if (this.getTargetActorName().indexOf("Chief") >= 0 || this.getTargetActorName().indexOf("Rocket") >= 0)
//            World.cur().triggersGuard.getListTriggerChiefActivate().remove(World.cur().triggersGuard.getListTriggerChiefActivate().indexOf(this.getTargetActorName()));
//        else if (this.getTargetActorName().indexOf("Trigger") >= 0)
//            World.cur().triggersGuard.getListTriggerTriggerActivate().remove(World.cur().triggersGuard.getListTriggerTriggerActivate().indexOf(this.getTargetActorName()));
//        else World.cur().triggersGuard.getListTriggerAircraftActivate().remove(World.cur().triggersGuard.getListTriggerAircraftActivate().indexOf(this.getTargetActorName()));
    }

    public static void startGround(String nameUnit) {
        Actor actor = Actor.getByName(nameUnit);
        if (actor instanceof RocketryGeneric) ((RocketryGeneric) actor).startMove();
        else if (actor instanceof Train) ((Train) actor).startMove();
        else if (actor instanceof BigshipGeneric) ((BigshipGeneric) actor).startMove();
        else if (actor instanceof ShipGeneric) ((ShipGeneric) actor).startMove();
        else if (actor instanceof ChiefGround) ((ChiefGround) actor).startMove();
    }

//    public String getTargetActorName() {
//        return this.targetActorName;
//    }
//
//    public void setTargetActorName(String targetActorName) {
//        this.targetActorName = targetActorName;
//    }
//
//    private String targetActorName;
    
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
}
