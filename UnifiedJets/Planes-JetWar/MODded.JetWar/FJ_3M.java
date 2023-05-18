// Source File Name: FJ_3M.java
// Author:           
// Last Modified by: Storebror 2011-06-03
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Time;
import com.maddox.rts.Property;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;

import java.io.IOException;

public class FJ_3M extends F_86F
        implements TypeDockable,
        TypeGuidedMissileCarrier,
        TypeCountermeasure,
        TypeThreatDetector,
        TypeGSuit {

//  public static boolean bChangedPit = false;
  private Actor queen_last;
  private long queen_time;
  private boolean bNeedSetup;
  private long dtime;
  private Actor target_;
  private Actor queen_;
  private int dockport_;
  private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);

  public FJ_3M() {
    arrestor = 0.0F;
  }
// <editor-fold defaultstate="collapsed" desc="Countermeasures">
  private boolean hasChaff = false;     // Aircraft is equipped with Chaffs yes/no
  private boolean hasFlare = false;     // Aircraft is equipped with Flares yes/no
  private long lastChaffDeployed = 0L;  // Last Time when Chaffs have been deployed
  private long lastFlareDeployed = 0L;  // Last Time when Flares have been deployed

  public long getChaffDeployed() {
    if (this.hasChaff) {
      return this.lastChaffDeployed;
    }
    return 0L;
  }

  public long getFlareDeployed() {
    if (this.hasFlare) {
      return this.lastFlareDeployed;
    }
    return 0L;
  }// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Threat Detection">
  private long lastCommonThreatActive = 0L;         // Last Time when a common threat was reported
  private long intervalCommonThreat = 1000L;        // Interval (milliseconds) at which common threats should be dealt with (i.e. duration of warning sound / light)
  private long lastRadarLockThreatActive = 0L;      // Last Time when a radar lock threat was reported
  private long intervalRadarLockThreat = 1000L;     // Interval (milliseconds) at which radar lock threats should be dealt with (i.e. duration of warning sound / light)
  private long lastMissileLaunchThreatActive = 0L;  // Last Time when a missile launch threat was reported
  private long intervalMissileLaunchThreat = 1000L; // Interval (milliseconds) at which missile launch threats should be dealt with (i.e. duration of warning sound / light)

  public void setCommonThreatActive() {
    long curTime = Time.current();
    if ((curTime - this.lastCommonThreatActive) > this.intervalCommonThreat) {
      this.lastCommonThreatActive = curTime;
      this.doDealCommonThreat();
    }
  }

  public void setRadarLockThreatActive() {
    long curTime = Time.current();
    if ((curTime - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
      this.lastRadarLockThreatActive = curTime;
      this.doDealRadarLockThreat();
    }
  }

  public void setMissileLaunchThreatActive() {
    long curTime = Time.current();
    if ((curTime - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
      this.lastMissileLaunchThreatActive = curTime;
      this.doDealMissileLaunchThreat();
    }
  }

  private void doDealCommonThreat() {       // Must be filled with life for A/Cs capable of dealing with common Threats
  }

  private void doDealRadarLockThreat() {    // Must be filled with life for A/Cs capable of dealing with radar lock Threats
  }

  private void doDealMissileLaunchThreat() {// Must be filled with life for A/Cs capable of dealing with missile launch Threats
  }// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="G Forces">
  /**
   * G-Force Resistance, Tolerance and Recovery parmeters.
   * See TypeGSuit.GFactors Private fields implementation
   * for further details.
   */
  private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
  private static final float NEG_G_TIME_FACTOR = 1.5F;
  private static final float NEG_G_RECOVERY_FACTOR = 1.0F;
  private static final float POS_G_TOLERANCE_FACTOR = 2.0F;
  private static final float POS_G_TIME_FACTOR = 2.0F;
  private static final float POS_G_RECOVERY_FACTOR = 2.0F;

  public void getGFactors(GFactors theGFactors) {
    theGFactors.setGFactors(
            NEG_G_TOLERANCE_FACTOR,
            NEG_G_TIME_FACTOR,
            NEG_G_RECOVERY_FACTOR,
            POS_G_TOLERANCE_FACTOR,
            POS_G_TIME_FACTOR,
            POS_G_RECOVERY_FACTOR);
  }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="TypeGuidedMissileCarrier Implementation">

  public GuidedMissileUtils getGuidedMissileUtils() {
    return this.guidedMissileUtils;
  }

// </editor-fold>

  public void onAircraftLoaded() {
    super.onAircraftLoaded();
    this.guidedMissileUtils.onAircraftLoaded();
  }

  public void update(float f) {
    if (bNeedSetup) {
      checkAsDrone();
    }
    this.guidedMissileUtils.update();
    int i = aircIndex();
    if (super.FM instanceof Maneuver) {
      if (typeDockableIsDocked()) {
        if (!(super.FM instanceof RealFlightModel) || !((RealFlightModel) super.FM).isRealMode()) {
          ((Maneuver) super.FM).unblock();
          ((Maneuver) super.FM).set_maneuver(48);
          for (int j = 0; j < i; j++) {
            ((Maneuver) super.FM).push(48);
          }

          if (((FlightModelMain) (super.FM)).AP.way.curr().Action != 3) {
            ((FlightModelMain) ((Maneuver) super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft) queen_)).FM)).AP.way.Cur());
          }
          ((Pilot) super.FM).setDumbTime(3000L);
        }
        if (((FlightModelMain) (super.FM)).M.fuel < ((FlightModelMain) (super.FM)).M.maxFuel) {
          ((FlightModelMain) (super.FM)).M.fuel += 20F * f;
        }
      } else if (!(super.FM instanceof RealFlightModel) || !((RealFlightModel) super.FM).isRealMode()) {
        if (FM.CT.GearControl == 0.0F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 0) {
          ((FlightModelMain) (super.FM)).EI.setEngineRunning();
        }
        if (dtime > 0L && ((Maneuver) super.FM).Group != null) {
          ((Maneuver) super.FM).Group.leaderGroup = null;
          ((Maneuver) super.FM).set_maneuver(22);
          ((Pilot) super.FM).setDumbTime(3000L);
          if (Time.current() > dtime + 3000L) {
            dtime = -1L;
            ((Maneuver) super.FM).clear_stack();
            ((Maneuver) super.FM).set_maneuver(0);
            ((Pilot) super.FM).setDumbTime(0L);
          }
        } else if (((FlightModelMain) (super.FM)).AP.way.curr().Action == 0) {
          Maneuver maneuver = (Maneuver) super.FM;
          if (maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null) {
            maneuver.Group.setGroupTask(2);
          }
        }
      }
    }
    if (FM.CT.getArrestor() > 0.2F) {
      if (FM.Gears.arrestorVAngle != 0.0F) {
        float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
        arrestor = 0.8F * arrestor + 0.2F * f1;
        moveArrestorHook(arrestor);
      } else {
        float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
        if (f2 < 0.0F && FM.getSpeedKMH() > 60F) {
          Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
        }
        if (f2 > 0.0F && FM.CT.getArrestor() < 0.95F) {
          f2 = 0.0F;
        }
        if (f2 > 0.2F) {
          f2 = 0.2F;
        }
        if (f2 > 0.0F) {
          arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
        } else {
          arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
        }
        if (arrestor < 0.0F) {
          arrestor = 0.0F;
        } else if (arrestor > 1.0F) {
          arrestor = 1.0F;
        }
        moveArrestorHook(arrestor);
      }
    }
    super.update(f);
  }

  public void msgCollisionRequest(Actor actor, boolean aflag[]) {
    super.msgCollisionRequest(actor, aflag);
    if (queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L)) {
      aflag[0] = false;
    } else {
      aflag[0] = true;
    }
  }

  public void missionStarting() {
    checkAsDrone();
  }

  private void checkAsDrone() {
    if (target_ == null) {
      if (((FlightModelMain) (super.FM)).AP.way.curr().getTarget() == null) {
        ((FlightModelMain) (super.FM)).AP.way.next();
      }
      target_ = ((FlightModelMain) (super.FM)).AP.way.curr().getTarget();
      if (Actor.isValid(target_) && (target_ instanceof Wing)) {
        Wing wing = (Wing) target_;
        int i = aircIndex();
        if (Actor.isValid(wing.airc[i / 2])) {
          target_ = wing.airc[i / 2];
        } else {
          target_ = null;
        }
      }
    }
    if (Actor.isValid(target_) && (target_ instanceof TypeTankerDrogue)) {
      queen_last = target_;
      queen_time = Time.current();
      if (isNetMaster()) {
        ((TypeDockable) target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
      }
    }
    bNeedSetup = false;
    target_ = null;
  }

  public int typeDockableGetDockport() {
    if (typeDockableIsDocked()) {
      return dockport_;
    } else {
      return -1;
    }
  }

  public Actor typeDockableGetQueen() {
    return queen_;
  }

  public boolean typeDockableIsDocked() {
    return Actor.isValid(queen_);
  }

  public void typeDockableAttemptAttach() {
    if (((FlightModelMain) (super.FM)).AS.isMaster() && !typeDockableIsDocked()) {
      Aircraft aircraft = War.getNearestFriend(this);
      if (aircraft instanceof TypeTankerDrogue) {
        ((TypeDockable) aircraft).typeDockableRequestAttach(this);
      }
    }
  }

  public void typeDockableAttemptDetach() {
    if (((FlightModelMain) (super.FM)).AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_)) {
      ((TypeDockable) queen_).typeDockableRequestDetach(this);
    }
  }

  public void typeDockableRequestAttach(Actor actor1) {
  }

  public void typeDockableRequestDetach(Actor actor1) {
  }

  public void typeDockableRequestAttach(Actor actor1, int j, boolean flag1) {
  }

  public void typeDockableRequestDetach(Actor actor1, int j, boolean flag1) {
  }

  public void typeDockableDoAttachToDrone(Actor actor1, int j) {
  }

  public void typeDockableDoDetachFromDrone(int j) {
  }

  public void typeDockableDoAttachToQueen(Actor actor, int i) {

    queen_ = actor;
    dockport_ = i;
    queen_last = queen_;
    queen_time = 0L;
    ((FlightModelMain) (super.FM)).EI.setEngineRunning();
    ((FlightModelMain) (super.FM)).CT.setGearAirborne();
    moveGear(0.0F);
    com.maddox.il2.fm.FlightModel flightmodel = ((SndAircraft) ((Aircraft) queen_)).FM;
    if (aircIndex() == 0 && (super.FM instanceof Maneuver) && (flightmodel instanceof Maneuver)) {
      Maneuver maneuver = (Maneuver) flightmodel;
      Maneuver maneuver1 = (Maneuver) super.FM;
      if (maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1) {
        AirGroup airgroup = new AirGroup(maneuver1.Group);
        maneuver1.Group.delAircraft(this);
        airgroup.addAircraft(this);
        airgroup.attachGroup(maneuver.Group);
        airgroup.rejoinGroup = null;
        airgroup.leaderGroup = null;
        airgroup.clientGroup = maneuver.Group;
      }
    }
  }

  public void typeDockableDoDetachFromQueen(int i) {
    if (dockport_ == i) {
      queen_last = queen_;
      queen_time = Time.current();
      queen_ = null;
      dockport_ = 0;
    }
  }

  public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
          throws IOException {
    if (typeDockableIsDocked()) {
      netmsgguaranted.writeByte(1);
      com.maddox.il2.engine.ActorNet actornet = null;
      if (Actor.isValid(queen_)) {
        actornet = queen_.net;
        if (actornet.countNoMirrors() > 0) {
          actornet = null;
        }
      }
      netmsgguaranted.writeByte(dockport_);
      netmsgguaranted.writeNetObj(actornet);
    } else {
      netmsgguaranted.writeByte(0);
    }
  }

  public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
          throws IOException {
    if (netmsginput.readByte() == 1) {
      dockport_ = netmsginput.readByte();
      NetObj netobj = netmsginput.readNetObj();
      if (netobj != null) {
        Actor actor = (Actor) netobj.superObj();
        ((TypeDockable) actor).typeDockableDoAttachToDrone(this, dockport_);
      }
    }
  }

  public void rareAction(float f, boolean flag) {
    super.rareAction(f, flag);
    if ((super.FM instanceof RealFlightModel) && ((RealFlightModel) super.FM).isRealMode() || !flag || !(super.FM instanceof Pilot)) {
      return;
    }
    if (flag && ((FlightModelMain) (super.FM)).AP.way.curr().Action == 3 && typeDockableIsDocked() && Math.abs(((FlightModelMain) (((SndAircraft) ((Aircraft) queen_)).FM)).Or.getKren()) < 3F) {
      if (super.FM.isPlayers()) {
        if ((super.FM instanceof RealFlightModel) && !((RealFlightModel) super.FM).isRealMode()) {
          typeDockableAttemptDetach();
          ((Maneuver) super.FM).set_maneuver(22);
          ((Maneuver) super.FM).setCheckStrike(false);
          ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
          dtime = Time.current();
        }
      } else {
        typeDockableAttemptDetach();
        ((Maneuver) super.FM).set_maneuver(22);
        ((Maneuver) super.FM).setCheckStrike(false);
        ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
        dtime = Time.current();
      }
    }
  }

  protected void moveWingFold(HierMesh hiermesh, float f) {
    hiermesh.chunkSetAngles("WingLFold", 0F * f, 0F * f, -22F * f);
    hiermesh.chunkSetAngles("WingRFold", 0F * f, 0F * f, -22F * f);
    hiermesh.chunkSetAngles("WingLOut_D0", 0F * f, 90F * f, -22F * f);
    hiermesh.chunkSetAngles("WingROut_D0", 0F * f, -90F * f, -22F * f);
  }

  public void moveWingFold(float f) {
    if (f < 0.001F) {
      setGunPodsOn(true);
      hideWingWeapons(false);
    } else {
      setGunPodsOn(false);
      FM.CT.WeaponControl[0] = false;
      hideWingWeapons(true);
    }
    moveWingFold(hierMesh(), f);
  }

  public void moveArrestorHook(float f) {
    hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 45F * f, 0.0F);
    resetYPRmodifier();
    Aircraft.xyz[2] = 0.1385F * f;
    arrestor = f;
  }

  protected boolean cutFM(int i, int j, Actor actor) {
    switch (i) {
      case 19: // '\023'
        FM.CT.bHasArrestorControl = false;
        break;
    }
    return super.cutFM(i, j, actor);
  }

  public void checkHydraulicStatus() {
    if (FM.EI.engines[0].getStage() < 6
            && FM.Gears.nOfGearsOnGr > 0) {
      hasHydraulicPressure = false;
      FM.CT.bHasAileronControl = false;
      FM.CT.bHasElevatorControl = false;
      FM.CT.AirBrakeControl = 1.0F;
      FM.CT.bHasArrestorControl = false;
    } else if (!hasHydraulicPressure) {
      hasHydraulicPressure = true;
      FM.CT.bHasAileronControl = true;
      FM.CT.bHasElevatorControl = true;
      FM.CT.bHasAirBrakeControl = true;
      FM.CT.bHasArrestorControl = true;
    }
  }
  
  public boolean bToFire;
  private float arrestor;

  static {
    Class localClass = com.maddox.il2.objects.air.FJ_3M.class;
    new NetAircraft.SPAWN(localClass);
    Property.set(localClass, "iconFar_shortClassName", "FJ-3M");
    Property.set(localClass, "meshName", "3DO/Plane/FJ_3M(Multi1)/hier.him");
    Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
    Property.set(localClass, "yearService", 1949.9F);
    Property.set(localClass, "yearExpired", 1960.3F);
    Property.set(localClass, "FlightModel", "FlightModels/FJ-3M.fmd");
    Property.set(localClass, "cockpitClass", new Class[]{
              com.maddox.il2.objects.air.CockpitF_86Flate.class
            });
    Property.set(localClass, "LOSElevation", 0.725F);
    com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(localClass, new int[]{
              0, 0, 0, 0, 9, 9, 9, 9, 9, 3,
              3, 9, 3, 3, 9, 2, 2, 9, 2, 2,
              9, 9, 9, 9, 9, 3, 3, 9, 3, 3,
              9, 9, 2, 2, 2, 2, 2, 2, 2, 2
            });
    com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(localClass, new java.lang.String[]{
              "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01",
              "_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08", "_ExternalRock02", "_ExternalRock02",
              "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04", "_ExternalBomb04",
              "_ExternalDev17", "_ExternalDev18", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "default", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x75nap", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun75Napalm 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun75Napalm 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x75nap+2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun75Napalm 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun75Napalm 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun500lbs 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun500lbs 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500+2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun500lbs 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun500lbs 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500+2x750", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", null, null, null, "BombGun500lbs 1",
              "BombGunNull 1", null, "BombGun500lbs 1", "BombGunNull 1", null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbs 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun750lbs 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500+2xM117", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", null, null, null, "BombGun500lbs 1",
              "BombGunNull 1", null, "BombGun500lbs 1", "BombGunNull 1", null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun750lbsM117 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "4x500", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", null, null, null, "BombGun500lbs 1",
              "BombGunNull 1", null, "BombGun500lbs 1", "BombGunNull 1", null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun500lbs 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun500lbs 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x750", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbs 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun750lbs 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x750+2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbs 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun750lbs 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM117", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun750lbsM117 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM117+2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun750lbsM117 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x1000", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun1000lbs 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun1000lbs 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x1000+2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun1000lbs 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun1000lbs 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM114", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun1000lbs_M114 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun1000lbs_M114 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM114+2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", "BombGun1000lbs_M114 1", "BombGunNull 1", "PylonF86_Outboard 1", "BombGun1000lbs_M114 1", "BombGunNull 1",
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xLAU10", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", null, null, "PylonF86_Outboard 1", null, null,
              "Pylon_Zuni 1", "Pylon_Zuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xLAU10+2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Outboard 1", null, null, "PylonF86_Outboard 1", null, null,
              "Pylon_Zuni 1", "Pylon_Zuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xAIM9B", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Sidewinder 1", "RocketGunAIM9B 1", "RocketGunNull 1", "PylonF86_Sidewinder 1", "RocketGunAIM9B 1", "RocketGunNull 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xAIM9B+2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null, "PylonF86_Sidewinder 1", "RocketGunAIM9B 1", "RocketGunNull 1", "PylonF86_Sidewinder 1", "RocketGunAIM9B 1", "RocketGunNull 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xAIM9D", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", null, null, null, null, null, null,
              null, null, null, null, "PylonF86_Sidewinder 1", "RocketGunAIM9D 1", "RocketGunNull 1", "PylonF86_Sidewinder 1", "RocketGunAIM9D 1", "RocketGunNull 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xAIM9D+2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "MGunColtMk12ki 162", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null, "PylonF86_Sidewinder 1", "RocketGunAIM9D 1", "RocketGunNull 1", "PylonF86_Sidewinder 1", "RocketGunAIM9D 1", "RocketGunNull 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "none", new java.lang.String[]{
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null
            });
  }
}