// Source File Name: CAC_Sabre_Mk31.java
// Author:           
// Last Modified by: Storebror 2011-06-03
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Time;

public class CAC_Sabre_Mk31 extends F_86F
        implements TypeCountermeasure,
        TypeThreatDetector,
        TypeGSuit {

  private float oldthrl = -1F;
  private float curthrl = -1F;
  private float engineSurgeDamage;


  public CAC_Sabre_Mk31() {
    engineSurgeDamage = 0.0F;
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

  public void onAircraftLoaded() {
    super.onAircraftLoaded();
    if (super.FM.isPlayers()) {
      ((FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = true;
      ((FlightModelMain) (super.FM)).CT.dvCockpitDoor = 0.5F;
    }
  }

  public void engineSurge(float f) {
    if (((FlightModelMain) (super.FM)).AS.isMaster()) {
      if (curthrl == -1F) {
        curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
      } else {
        curthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
        if (curthrl < 1.05F) {
          if ((curthrl - oldthrl) / f > 10.0F && FM.EI.engines[0].getRPM() < 3200.0F && FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.40F) {
            if (FM.actor == World.getPlayerAircraft()) {
              HUD.log("Compressor Stall!");
            }
            super.playSound("weapon.MGunMk108s", true);
            engineSurgeDamage += 1.0000000000000001E-002D * (double) (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
            ((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
            if (World.Rnd().nextFloat() < 0.05F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
              FM.AS.hitEngine(this, 0, 100);
            }
            if (World.Rnd().nextFloat() < 0.05F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
              FM.EI.engines[0].setEngineDies(this);
            }
          }
          if ((curthrl - oldthrl) / f < -10.0F && (curthrl - oldthrl) / f > -100.0F && FM.EI.engines[0].getRPM() < 3200.0F && FM.EI.engines[0].getStage() == 6) {
            super.playSound("weapon.MGunMk108s", true);
            engineSurgeDamage += 1.0000000000000001E-003D * (double) (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
            ((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
            if (World.Rnd().nextFloat() < 0.40F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
              if (FM.actor == World.getPlayerAircraft()) {
                HUD.log("Engine Flameout!");
              }
              FM.EI.engines[0].setEngineStops(this);
            } else {
              if (FM.actor == World.getPlayerAircraft()) {
                HUD.log("Compressor Stall!");
              }
            }
          }
        }
        oldthrl = curthrl;
      }
    }
    if (FM.CT.WeaponControl[1] && FM.EI.engines[0].getStage() == 6 && FM.CT.Weapons[0][0].countBullets() != 0) {
      if (World.Rnd().nextFloat() < 0.005F) {
        super.playSound("weapon.MGunMk108s", true);
        engineSurgeDamage += 1.0000000000000001E-003D * (double) (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
        ((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
        if (World.Rnd().nextFloat() < 0.20F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
          if (FM.actor == World.getPlayerAircraft()) {
            HUD.log("Engine Flameout!");
          }
          FM.EI.engines[0].setEngineStops(this);
        } else {
          if (FM.actor == World.getPlayerAircraft()) {
            HUD.log("Compressor Stall!");
          }
        }
      }
    }
  }

  public void update(float f) {
    super.update(f);
    engineSurge(f);
  }
  
  static {
    java.lang.Class localClass = com.maddox.il2.objects.air.CAC_Sabre_Mk31.class;
    new NetAircraft.SPAWN(localClass);
    com.maddox.rts.Property.set(localClass, "iconFar_shortClassName", "CAC_Sabre");
    com.maddox.rts.Property.set(localClass, "meshName_gb", "3DO/Plane/CAC_Sabre_Mk32(Multi1)/hier.him");
    com.maddox.rts.Property.set(localClass, "PaintScheme_gb", new PaintSchemeFMPar1956());
    com.maddox.rts.Property.set(localClass, "meshName", "3DO/Plane/CAC_Sabre_Mk32(Multi1)/hier.him");
    com.maddox.rts.Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
    com.maddox.rts.Property.set(localClass, "yearService", 1949.9F);
    com.maddox.rts.Property.set(localClass, "yearExpired", 1960.3F);
    com.maddox.rts.Property.set(localClass, "FlightModel", "FlightModels/CAC_Sabre_Mk31.fmd");
    try {
      com.maddox.rts.Property.set(localClass, "cockpitClass", new java.lang.Class[]{
                java.lang.Class.forName("com.maddox.il2.objects.air.CockpitF_86Flate")
              });
    } catch (java.lang.Exception exception) {
      com.maddox.il2.ai.EventLog.type("Exception in CAC Mk31 Cockpit init, " + exception.getMessage());
    }
    com.maddox.rts.Property.set(localClass, "LOSElevation", 0.725F);
    com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(localClass, new int[]{
              0, 0, 9, 9, 9, 9, 9, 3, 3, 9,
              3, 3, 9, 2, 2, 9, 2, 2, 9, 9,
              9, 9, 9, 3, 3, 9, 3, 3, 2, 2,
              2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
              2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
              2, 2, 9, 9, 9, 9, 9, 9, 9, 9,
              2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
              2, 2, 2, 2, 2, 2, 9, 9, 9, 9,
              9, 9, 9, 9
            });
    com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(localClass, new java.lang.String[]{
              "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev06",
              "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08", "_ExternalRock02", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10",
              "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04", "_ExternalBomb04", "_ExternalRock03", "_ExternalRock04",
              "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14",
              "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24",
              "_ExternalRock25", "_ExternalRock26", "_ExternalDev17", "_ExternalDev18", "_ExternalDev19", "_ExternalDev20", "_ExternalDev21", "_ExternalDev22", "_ExternalDev23", "_ExternalDev24",
              "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36",
              "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_ExternalDev25", "_ExternalDev26", "_ExternalDev27", "_ExternalDev28",
              "_ExternalDev29", "_ExternalDev30", "_ExternalDev31", "_ExternalDev32"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "default", new java.lang.String[]{
              "MGunADEN30ki 150", "MGunADEN30ki 150", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x120dt", new java.lang.String[]{
              "MGunADEN30ki 150", "MGunADEN30ki 150", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x207dt", new java.lang.String[]{
            "MGunADEN30ki 150", "MGunADEN30ki 150", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_207galL 1", "FuelTankGun_207galR 1", null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null
          });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x75nap", new java.lang.String[]{
              "MGunADEN30ki 150", "MGunADEN30ki 150", null, null, null, null, "PylonF86_Bombs 1", "BombGun75Napalm 1", "BombGunNull 1", "PylonF86_Bombs 1",
              "BombGun75Napalm 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500", new java.lang.String[]{
              "MGunADEN30ki 150", "MGunADEN30ki 150", null, null, null, null, "PylonF86_Bombs 1", "BombGun500lbsMC 1", "BombGunNull 1", "PylonF86_Bombs 1",
              "BombGun500lbsMC 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x1000", new java.lang.String[]{
              "MGunADEN30ki 150", "MGunADEN30ki 150", null, null, null, null, "PylonF86_Bombs 1", "BombGun1000lbsMC 1", "BombGunNull 1", "PylonF86_Bombs 1",
              "BombGun1000lbsMC 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "16xRP3Mk5", new java.lang.String[]{
    		"MGunADEN30ki 150", "MGunADEN30ki 150", null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		"RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1",
    		"RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1",
    		"PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1"
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "16xRP3Mk5-25", new java.lang.String[]{
    		"MGunADEN30ki 150", "MGunADEN30ki 150", null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		"RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1",
    		"RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1",
    		"PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1"
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "8xRP3Mk5+2x120dt", new java.lang.String[]{
    		"MGunADEN30ki 150", "MGunADEN30ki 150", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1",
    		"RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", "RocketGunRP3_Mk5_60 1", null, null, null, null,
    		"PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1"
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "8xRP3Mk5-25+2x120dt", new java.lang.String[]{
    		"MGunADEN30ki 150", "MGunADEN30ki 150", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1",
    		"RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", "RocketGunRP3_Mk5_25 1", null, null, null, null,
    		"PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1"
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "24xSURA_D_HE", new java.lang.String[]{
              "MGunADEN30ki 150", "MGunADEN30ki 150", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, "RocketGunSURA_HE 1", "RocketGunSURA_HE 1",
              "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1",
              "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "RocketGunSURA_HE 1",
              "RocketGunSURA_HE 1", "RocketGunSURA_HE 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "24xSURA_D_AP", new java.lang.String[]{
              "MGunADEN30ki 150", "MGunADEN30ki 150", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, "RocketGunSURA_AP 1", "RocketGunSURA_AP 1",
              "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1",
              "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "RocketGunSURA_AP 1",
              "RocketGunSURA_AP 1", "RocketGunSURA_AP 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1", "PylonSURA_Launcher 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "none", new java.lang.String[]{
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null
            });
  }
}