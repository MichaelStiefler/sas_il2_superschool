// Source File Name: Mig_17F.java
// Author:           
// Last Modified by: Storebror 2011-11-19
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class Mig_17F extends Mig_17
  implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeRadarGunsight
{
  private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);
  private SoundFX fxSirena = newSound("aircraft.Sirena2", false);
  private Sample smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
  private boolean sirenaSoundPlaying = false;
  private boolean bRadarWarning;
  private boolean hasChaff = false;
  private boolean hasFlare = false;
  private long lastChaffDeployed = 0L;
  private long lastFlareDeployed = 0L;

  private long lastCommonThreatActive = 0L;
  private long intervalCommonThreat = 1000L;
  private long lastRadarLockThreatActive = 0L;
  private long intervalRadarLockThreat = 1000L;
  private long lastMissileLaunchThreatActive = 0L;
  private long intervalMissileLaunchThreat = 1000L;
  
  public Mig_17F()
  {
	this.guidedMissileUtils = new GuidedMissileUtils(this);
    this.smplSirena.setInfinite(true);
  }

//<editor-fold defaultstate="collapsed" desc="G Forces">
  /**
   * G-Force Resistance, Tolerance and Recovery parmeters.
   * See TypeGSuit.GFactors Private fields implementation
   * for further details.
   */
  private static final float NEG_G_TOLERANCE_FACTOR = 1.0F;
  private static final float NEG_G_TIME_FACTOR = 1.0F;
  private static final float NEG_G_RECOVERY_FACTOR = 1.0F;
  private static final float POS_G_TOLERANCE_FACTOR = 1.8F;
  private static final float POS_G_TIME_FACTOR = 1.5F;
  private static final float POS_G_RECOVERY_FACTOR = 1.0F;

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
  
  public long getChaffDeployed()
  {
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
  }

  public void setCommonThreatActive()
  {
    long curTime = Time.current();
    if (curTime - this.lastCommonThreatActive > this.intervalCommonThreat) {
      this.lastCommonThreatActive = curTime;
      doDealCommonThreat();
    }
  }

  public void setRadarLockThreatActive() {
    long curTime = Time.current();
    if (curTime - this.lastRadarLockThreatActive > this.intervalRadarLockThreat) {
      this.lastRadarLockThreatActive = curTime;
      doDealRadarLockThreat();
    }
  }

  public void setMissileLaunchThreatActive() {
    long curTime = Time.current();
    if (curTime - this.lastMissileLaunchThreatActive > this.intervalMissileLaunchThreat) {
      this.lastMissileLaunchThreatActive = curTime;
      doDealMissileLaunchThreat();
    }
  }

  private void doDealCommonThreat()
  {
  }

  private void doDealRadarLockThreat() {
  }

  private void doDealMissileLaunchThreat() {
  }

  private boolean sirenaWarning() {
    Point3d point3d = new Point3d();
    this.pos.getAbs(point3d);
    Vector3d vector3d = new Vector3d();
    Aircraft aircraft = World.getPlayerAircraft();
    double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
    double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
    double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
    int i = (int)(-(aircraft.pos.getAbsOrient().getYaw() - 90.0D));
    if (i < 0)
      i += 360;
    int j = (int)(-(aircraft.pos.getAbsOrient().getPitch() - 90.0D));
    if (j < 0)
      j += 360;
    Actor actor = War.getNearestEnemy(this, 4000.0F);
    if (((actor instanceof Aircraft)) && (actor.getArmy() != World.getPlayerArmy()) && ((actor instanceof TypeFighterAceMaker) && (actor instanceof TypeRadarGunsight)) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(vector3d) > 20.0D))
    {
      this.pos.getAbs(point3d);
      double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
      double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
      double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
      new String();
      new String();
      double d6 = (int)(Math.ceil((d2 - d5) / 10.0D) * 10.0D);
      new String();
      double d7 = d3 - d;
      double d8 = d4 - d1;
      float f = 57.324841F * (float)Math.atan2(d8, -d7);
      int i1 = (int)(Math.floor((int)f) - 90.0D);
      if (i1 < 0)
        i1 += 360;
      int j1 = i1 - i;
      double d9 = d - d3;
      double d10 = d1 - d4;
      double d11 = Math.sqrt(d6 * d6);
      int k1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10.0D) * 10.0D);
      float f1 = 57.324841F * (float)Math.atan2(k1, d11);
      int l1 = (int)(Math.floor((int)f1) - 90.0D);
      if (l1 < 0)
        l1 += 360;
      int i2 = l1 - j;
      int j2 = (int)(Math.ceil(k1 * 3.2808399D / 100.0D) * 100.0D);
      if (j2 >= 5280)
      {
        j2 = (int)Math.floor(j2 / 5280);
      }
      this.bRadarWarning = ((k1 <= 3000.0D) && (k1 >= 50.0D) && (i2 >= 195) && (i2 <= 345) && (Math.sqrt(j1 * j1) >= 120.0D));
      playSirenaWarning(this.bRadarWarning);
    }
    else
    {
      this.bRadarWarning = false;
      playSirenaWarning(this.bRadarWarning);
    }
    return true;
  }

  public void playSirenaWarning(boolean isThreatened)
  {
    if ((isThreatened) && (!this.sirenaSoundPlaying)) {
      this.fxSirena.play(this.smplSirena);
      this.sirenaSoundPlaying = true;
      HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
    }
    else if ((!isThreatened) && (this.sirenaSoundPlaying)) {
      this.fxSirena.cancel();
      this.sirenaSoundPlaying = false;
    }
  }

// <editor-fold defaultstate="collapsed" desc="TypeGuidedMissileCarrier Implementation">

  public GuidedMissileUtils getGuidedMissileUtils() {
    return this.guidedMissileUtils;
  }

// </editor-fold>

  public void onAircraftLoaded()
  {
    super.onAircraftLoaded();
    if (this.FM.isPlayers()) {
      this.FM.CT.bHasCockpitDoorControl = true;
      this.FM.CT.dvCockpitDoor = 0.5F;
    }
    this.guidedMissileUtils.onAircraftLoaded();
  }

  public void doEjectCatapult() {
	    new MsgAction(false, this) {

	      public void doAction(Object paramObject) {
	        Aircraft localAircraft = (Aircraft) paramObject;
	        if (Actor.isValid(localAircraft)) {
	          Loc localLoc1 = new Loc();
	          Loc localLoc2 = new Loc();
	          Vector3d localVector3d = new Vector3d(0.0, 0.0, 10.0);
	          HookNamed localHookNamed = new HookNamed(localAircraft, "_ExternalSeat01");
	          localAircraft.pos.getAbs(localLoc2);
	          localHookNamed.computePos(localAircraft, localLoc2,
	                  localLoc1);
	          localLoc1.transform(localVector3d);
	          localVector3d.x += localAircraft.FM.Vwld.x;
	          localVector3d.y += localAircraft.FM.Vwld.y;
	          localVector3d.z += localAircraft.FM.Vwld.z;
	          new EjectionSeat(8, localLoc1, localVector3d,
	                  localAircraft);
	        }
	      }
	    };
	    this.hierMesh().chunkVisible("Seat_D0", false);
	    FM.setTakenMortalDamage(true, null);
	    FM.CT.WeaponControl[0] = false;
	    FM.CT.WeaponControl[1] = false;
	    FM.CT.bHasAileronControl = false;
	    FM.CT.bHasRudderControl = false;
	    FM.CT.bHasElevatorControl = false;
	  }
  
  public void update(float f1) {
    this.guidedMissileUtils.update();
    super.update(f1);
    typeFighterAceMakerRangeFinder();
    if ((this.FM.AS.isMaster()) && (Config.isUSE_RENDER())) {
      if ((this.FM.EI.engines[0].getThrustOutput() > 0.5F) && (this.FM.EI.engines[0].getStage() == 6)) {
          if (this.FM.EI.engines[0].getThrustOutput()> 1.001F)
            this.FM.AS.setSootState(this, 0, 5);
          else
            this.FM.AS.setSootState(this, 0, 3);
      }
      else {
        this.FM.AS.setSootState(this, 0, 0);
      }
    }
    if (((World.getPlayerRegiment().country().equalsIgnoreCase("ru")) || (World.getPlayerRegiment().country().equalsIgnoreCase("nv"))) && (Mission.curYear() >= 1952))
      sirenaWarning();
  }
  
  static
  {
    Class var_class = Mig_17F.class;
    new NetAircraft.SPAWN(var_class);
    Property.set(var_class, "iconFar_shortClassName", "MiG-17AS");
    Property.set(var_class, "meshName_ru", "3DO/Plane/MiG-17F(Multi1)/hier.him");
    Property.set(var_class, "PaintScheme_ru", new PaintSchemeFCSPar1956());
    Property.set(var_class, "meshName_sk", "3DO/Plane/MiG-17F(Multi1)/hier.him");
    Property.set(var_class, "PaintScheme_sk", new PaintSchemeFMPar1956());
    Property.set(var_class, "meshName_ro", "3DO/Plane/MiG-17F(Multi1)/hier.him");
    Property.set(var_class, "PaintScheme_ro", new PaintSchemeFMPar1956());
    Property.set(var_class, "meshName_hu", "3DO/Plane/MiG-17F(Multi1)/hier.him");
    Property.set(var_class, "PaintScheme_hu", new PaintSchemeFMPar1956());
    Property.set(var_class, "meshName", "3DO/Plane/MiG-17F(Multi1)/hier.him");
    Property.set(var_class, "PaintScheme", new PaintSchemeFMPar06());
    Property.set(var_class, "yearService", 1952.11F);
    Property.set(var_class, "yearExpired", 1960.3F);
    Property.set(var_class, "FlightModel", "FlightModels/MiG-17F.fmd");
    Property.set(var_class, "cockpitClass", new Class[] { 
      CockpitMig_17.class });

    Property.set(var_class, "LOSElevation", 0.725F);
    Aircraft.weaponTriggersRegister(var_class, new int[] { 
      1, 0, 0, 9, 9, 9, 9, 9, 9, 2, 
      2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 
      2, 2, 2, 9, 9, 9, 9, 2, 2, 2, 
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
      2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 
      9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
      2, 2, 2, 9, 9, 3, 3, 3, 3, 3, 
      3, 3, 3 });

    Aircraft.weaponHooksRegister(var_class, new String[] { 
      "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev03", "_ExternalDev04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", 
      "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev07", "_ExternalDev08", "_ExternalRock05", 
      "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", 
      "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", 
      "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", 
      "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalDev13", 
      "_ExternalDev14", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46", "_ExternalRock47", 
      "_ExternalRock48", "_ExternalRock49", "_ExternalRock50", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", "_ExternalRock57", 
      "_ExternalRock58", "_ExternalRock59", "_ExternalRock60", "_ExternalRock61", "_ExternalRock62", "_ExternalRock63", "_ExternalRock64", "_ExternalRock65", "_ExternalRock66", "_ExternalRock67", 
      "_ExternalRock68", "_ExternalRock69", "_ExternalRock70", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", 
      "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04" });

    Aircraft.weaponsRegister(var_class, "default", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80" });

    Aircraft.weaponsRegister(var_class, "2xdrops", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1" });

    Aircraft.weaponsRegister(var_class, "2x100", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG17Inner 1", 
      "PylonMiG17Inner 1", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGunFAB100 1", 
      null, "BombGunFAB100 1" });

    Aircraft.weaponsRegister(var_class, "2x100+2xdrops", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG17Inner 1", 
      "PylonMiG17Inner 1", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGunFAB100 1", 
      null, "BombGunFAB100 1" });

    Aircraft.weaponsRegister(var_class, "2x100+2x250m46", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG17Inner 1", 
      "PylonMiG17Inner 1", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG15 1", "PylonMiG15 1", "BombGunFAB250m46 1", null, "BombGunFAB250m46 1", null, "BombGunFAB100 1", 
      null, "BombGunFAB100 1" });

    Aircraft.weaponsRegister(var_class, "4x100", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG17Inner 1", 
      "PylonMiG17Inner 1", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG15 1", "PylonMiG15 1", "BombGunFAB100 1", null, "BombGunFAB100 1", null, "BombGunFAB100 1", 
      null, "BombGunFAB100 1" });

    Aircraft.weaponsRegister(var_class, "2x250m46", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMiG15 1", "PylonMiG15 1", "BombGunFAB250m46 1", null, "BombGunFAB250m46 1" });

    Aircraft.weaponsRegister(var_class, "2xMARS2", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMARS2 1", 
      "PylonMARS2 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1" });

    Aircraft.weaponsRegister(var_class, "2xMARS2+2xdrops", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonMARS2 1", 
      "PylonMARS2 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1" });

    Aircraft.weaponsRegister(var_class, "2xORO57", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonORO57 1", "PylonORO57 1", null, null, "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1" });

    Aircraft.weaponsRegister(var_class, "2xORO57+2xdrops", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonORO57 1", "PylonORO57 1", null, null, "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1" });

    Aircraft.weaponsRegister(var_class, "4xORO57", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonORO57 1", "PylonORO57 1", "PylonORO57 1", "PylonORO57 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1" });

    Aircraft.weaponsRegister(var_class, "4xORO57+2xdrops", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonORO57 1", "PylonORO57 1", "PylonORO57 1", "PylonORO57 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
      "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1" });

    Aircraft.weaponsRegister(var_class, "2xK13A", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", 
      null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonK13A 1", "PylonK13A 1", "RocketGunK13A 1", 
      "RocketGunNull 1", "RocketGunK13A 1", "RocketGunNull 1" });

    Aircraft.weaponsRegister(var_class, "2xK13A+2xdrops", new String[] { 
      "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", 
      null, null, null, null, null, null, null, null, null, null, null, null, "PylonK13A 1", "PylonK13A 1", "RocketGunK13A 1", 
      "RocketGunNull 1", "RocketGunK13A 1", "RocketGunNull 1" });

    Aircraft.weaponsRegister(var_class, "none", new String[103]);
  }
}