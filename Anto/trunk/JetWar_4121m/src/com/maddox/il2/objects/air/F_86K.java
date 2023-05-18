// Source File Name: F_86K.java
// Author:           
// Last Modified by: Storebror 2011-06-03
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Time;
import com.maddox.rts.Property;

public class F_86K extends F_86D
        implements TypeGuidedMissileCarrier,
        TypeCountermeasure,
        TypeThreatDetector,
        TypeGSuit {

  private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);
  private Eff3DActor turbo;
  private Eff3DActor turbosmoke;
  private Eff3DActor afterburner;

  public F_86K() {
    this.turbo = null;
    this.turbosmoke = null;
    this.afterburner = null;
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

  public void destroy() {
    if (Actor.isValid(this.turbo)) {
      this.turbo.destroy();
    }
    if (Actor.isValid(this.turbosmoke)) {
      this.turbosmoke.destroy();
    }
    if (Actor.isValid(this.afterburner)) {
      this.afterburner.destroy();
    }
    super.destroy();
  }

  public void rareAction(float paramFloat, boolean paramBoolean) {
    super.rareAction(paramFloat, paramBoolean);
    if ((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver)) {
      if (FM.Gears.nOfGearsOnGr == 0) {
        FM.CT.cockpitDoorControl = 0.0F;
        FM.CT.bHasCockpitDoorControl = false;
      }
    }
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
    if (FM.AS.isMaster() && Config.isUSE_RENDER()) {
      if (FM.EI.engines[0].getPowerOutput() > 0.45F
              && FM.EI.engines[0].getStage() == 6) {
        if (FM.EI.engines[0].getPowerOutput() > 1.001F) {
          FM.AS.setSootState(this, 0, 5);
        } else if (FM.EI.engines[0].getPowerOutput() > 0.65F && FM.EI.engines[0].getPowerOutput() < 1.001F) {
          FM.AS.setSootState(this, 0, 3);
        } else {
          FM.AS.setSootState(this, 0, 2);
        }
      } else {
        FM.AS.setSootState(this, 0, 0);
      }
      setExhaustFlame(Math.round(Aircraft.cvt(FM.EI.engines[0].getThrustOutput(),
              0.7F, 0.87F, 0.0F, 12.0F)),
              0);
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
    Class localClass = com.maddox.il2.objects.air.F_86K.class;
    new NetAircraft.SPAWN(localClass);
    Property.set(localClass, "iconFar_shortClassName", "F_86K");
    Property.set(localClass, "meshName", "3DO/Plane/F_86K(Multi1)/hier.him");
    Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
    Property.set(localClass, "meshName_de", "3DO/Plane/F_86K(Multi1)/hier.him");
    Property.set(localClass, "PaintScheme_de", new PaintSchemeFMPar1956());
    Property.set(localClass, "meshName_it", "3DO/Plane/F_86K(Multi1)/hier.him");
    Property.set(localClass, "PaintScheme_it", new PaintSchemeFMPar1956());
    Property.set(localClass, "meshName_du", "3DO/Plane/F_86K(Multi1)/hier.him");
    Property.set(localClass, "PaintScheme_du", new PaintSchemeFMPar1956());
    Property.set(localClass, "yearService", 1949.9F);
    Property.set(localClass, "yearExpired", 1960.3F);
    Property.set(localClass, "FlightModel", "FlightModels/F-86K.fmd");
    Property.set(localClass, "cockpitClass", new Class[]{
              com.maddox.il2.objects.air.CockpitF_86K.class
            /*,com.maddox.il2.objects.air.CockpitF86_Radar.class*/
            });
    Property.set(localClass, "LOSElevation", 0.725F);
    com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(localClass, new int[]{
              0, 0, 0, 0, 9, 9, 9, 9, 9, 2,
              2, 9, 2, 2
            });
    com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(localClass, new java.lang.String[]{
              "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev07", "_ExternalRock01",
              "_ExternalRock01", "_ExternalDev08", "_ExternalRock02", "_ExternalRock02"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "default", new java.lang.String[]{
              "MGunColtMk12ki 132", "MGunColtMk12ki 132", "MGunColtMk12ki 132", "MGunColtMk12ki 132", null, null, null, null, null, null,
              null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 132", "MGunColtMk12ki 132", "MGunColtMk12ki 132", "MGunColtMk12ki 132", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", null, null,
              null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xAIM9B", new java.lang.String[]{
              "MGunColtMk12ki 132", "MGunColtMk12ki 132", "MGunColtMk12ki 132", "MGunColtMk12ki 132", null, null, null, null, "PylonF86_Sidewinder 1", "RocketGunAIM9B 1",
              "RocketGunNull 1", "PylonF86_Sidewinder 1", "RocketGunAIM9B 1", "RocketGunNull 1"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xAIM9B+2x120dt", new java.lang.String[]{
              "MGunColtMk12ki 132", "MGunColtMk12ki 132", "MGunColtMk12ki 132", "MGunColtMk12ki 132", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_120galL 1", "FuelTankGun_120galR 1", "PylonF86_Sidewinder 1", "RocketGunAIM9B 1",
              "RocketGunNull 1", "PylonF86_Sidewinder 1", "RocketGunAIM9B 1", "RocketGunNull 1"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "none", new java.lang.String[]{
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null
            });
  }
}