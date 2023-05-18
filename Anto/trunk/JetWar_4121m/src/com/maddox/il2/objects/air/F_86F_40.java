// Source File Name: F_86F_40.java
// Author:           
// Last Modified by: Storebror 2011-06-03
package com.maddox.il2.objects.air;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Time;
import com.maddox.rts.Property;

public class F_86F_40 extends F_86F
        implements TypeGuidedMissileCarrier,
        TypeCountermeasure,
        TypeThreatDetector,
        TypeGSuit {

  private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);

  public F_86F_40() {
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
    if (super.FM.isPlayers()) {
      ((FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = true;
      ((FlightModelMain) (super.FM)).CT.dvCockpitDoor = 0.5F;
    }
    this.guidedMissileUtils.onAircraftLoaded();
  }

  public void update(float f) {
    this.guidedMissileUtils.update();
    super.update(f);
    if (FM.getSpeed() > 5.0F) {
      moveSlats(f);
      bSlatsOff = false;
    } else {
      slatsOff();
    }
  }

  protected void moveSlats(float paramFloat) {
    this.resetYPRmodifier();
    Aircraft.xyz[0] = Aircraft.cvt(FM.getAOA(), 6.8F, 15.0F, 0.0F, -0.15F);
    Aircraft.xyz[1] = Aircraft.cvt(FM.getAOA(), 6.8F, 15.0F, 0.0F, 0.1F);
    Aircraft.xyz[2] = Aircraft.cvt(FM.getAOA(), 6.8F, 15.0F, 0.0F, -0.065F);
    this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F,
            Aircraft.cvt(FM.getAOA(), 6.8F, 15.0F, 0.0F, 8.5F), 0.0F);
    this.hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
    Aircraft.xyz[1] = Aircraft.cvt(FM.getAOA(), 6.8F, 15.0F, 0.0F, -0.1F);
    this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F,
            Aircraft.cvt(FM.getAOA(), 6.8F, 15.0F, 0.0F, 8.5F), 0.0F);
    this.hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
  }

  protected void slatsOff() {
    if (!bSlatsOff) {
      this.resetYPRmodifier();
      Aircraft.xyz[0] = -0.15F;
      Aircraft.xyz[1] = 0.1F;
      Aircraft.xyz[2] = -0.065F;
      this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 8.5F, 0.0F);
      this.hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz,
              Aircraft.ypr);
      Aircraft.xyz[1] = -0.1F;
      this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 8.5F, 0.0F);
      this.hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz,
              Aircraft.ypr);
      bSlatsOff = true;
    }
  }

  public boolean bToFire;

  static {
    Class localClass = com.maddox.il2.objects.air.F_86F_40.class;
    new NetAircraft.SPAWN(localClass);
    Property.set(localClass, "iconFar_shortClassName", "F-86");
    Property.set(localClass, "meshName", "3DO/Plane/F-86F(Multi1)/hierF40.him");
    Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
    Property.set(localClass, "meshName_us", "3DO/Plane/F-86F(USA)/hierF40.him");
    Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());
    Property.set(localClass, "yearService", 1949.9F);
    Property.set(localClass, "yearExpired", 1960.3F);
    Property.set(localClass, "FlightModel", "FlightModels/F-86F-40.fmd");
    Property.set(localClass, "cockpitClass", new Class[]{
              com.maddox.il2.objects.air.CockpitF_86Flate.class
            });
    Property.set(localClass, "LOSElevation", 0.725F);
    com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(localClass, new int[]{
              0, 0, 0, 0, 0, 0, 9, 9, 9, 9,
              9, 3, 3, 9, 3, 3, 9, 2, 2, 9,
              2, 2, 9, 9, 9, 9, 9, 3, 3, 9,
              3, 3, 2, 2, 2, 2, 2, 2, 2, 2,
              2, 2, 2, 2, 2, 2, 2, 2, 3
            });
    com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(localClass, new java.lang.String[]{
              "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
              "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08",
              "_ExternalRock02", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalBomb03", "_ExternalDev14",
              "_ExternalBomb04", "_ExternalBomb04", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10",
              "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalBomb07"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "default", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x120dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "4x120dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x120dt+2x207dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x207dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "4x207dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x75nap", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun75Napalm 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGun75Napalm 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x75nap+2x120dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun75Napalm 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGun75Napalm 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x75nap+2x207dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun75Napalm 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGun75Napalm 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xMk82", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGunMk82 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGunMk82 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xMk82+2x120dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGunMk82 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGunMk82 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xMk82+2x207dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGunMk82 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGunMk82 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xMk82+2xM117", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", null, null,
              null, "BombGunMk82 1", "BombGunNull 1", null, "BombGunMk82 1", "BombGunNull 1", null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGun750lbsM117 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "4xMk82", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", null, null,
              null, "BombGunMk82 1", "BombGunNull 1", null, "BombGunMk82 1", "BombGunNull 1", null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGunMk82 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGunMk82 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM117", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGun750lbsM117 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM117+2x120dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGun750lbsM117 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM117+2x207dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGun750lbsM117 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xMk83", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGunMk83 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGunMk83 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xMk83+2x120dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGunMk83 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGunMk83 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xMk83+2x207dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGunMk83 1", "BombGunNull 1", "PylonF86_Outboard 1",
              "BombGunMk83 1", "BombGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "16xHVAR", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1",
              "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "8xHVAR+2x120dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "8xHVAR+2x207dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xAIM9B", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              null, null, null, null, null, null, "PylonF86_Sidewinder 1", "RocketGunAIM9B 1", "RocketGunNull 1", "PylonF86_Sidewinder 1",
              "RocketGunAIM9B 1", "RocketGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xAIM9B+2x120dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
              null, null, null, null, null, null, "PylonF86_Sidewinder 1", "RocketGunAIM9B 1", "RocketGunNull 1", "PylonF86_Sidewinder 1",
              "RocketGunAIM9B 1", "RocketGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xAIM9B+2x207dt", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
              null, null, null, null, null, null, "PylonF86_Sidewinder 1", "RocketGunAIM9B 1", "RocketGunNull 1", "PylonF86_Sidewinder 1",
              "RocketGunAIM9B 1", "RocketGunNull 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "none", new java.lang.String[]{
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null
            });
  }
}