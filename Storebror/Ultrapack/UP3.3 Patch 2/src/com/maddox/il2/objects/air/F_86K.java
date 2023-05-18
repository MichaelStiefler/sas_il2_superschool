package com.maddox.il2.objects.air;

import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class F_86K extends F_86D implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit {

    public F_86K() {
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
        this.bToFire = false;
        this.turbo = null;
        this.turbosmoke = null;
        this.afterburner = null;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public long getChaffDeployed() {
        if (this.hasChaff) return this.lastChaffDeployed;
        else return 0L;
    }

    public long getFlareDeployed() {
        if (this.hasFlare) return this.lastFlareDeployed;
        else return 0L;
    }

    public void setCommonThreatActive() {
        long curTime = Time.current();
        if (curTime - this.lastCommonThreatActive > this.intervalCommonThreat) {
            this.lastCommonThreatActive = curTime;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long curTime = Time.current();
        if (curTime - this.lastRadarLockThreatActive > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = curTime;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        long curTime = Time.current();
        if (curTime - this.lastMissileLaunchThreatActive > this.intervalMissileLaunchThreat) {
            this.lastMissileLaunchThreatActive = curTime;
            this.doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat() {
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
    }

    public void getGFactors(TypeGSuit.GFactors theGFactors) {
        theGFactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public void destroy() {
        if (this.turbo != null && Actor.isValid(this.turbo)) this.turbo.destroy();
        if (this.turbosmoke != null && Actor.isValid(this.turbosmoke)) this.turbosmoke.destroy();
        if (this.afterburner != null && Actor.isValid(this.afterburner)) this.afterburner.destroy();
        super.destroy();
    }

    public void rareAction(float paramFloat, boolean paramBoolean) {
        super.rareAction(paramFloat, paramBoolean);
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && this.FM instanceof Maneuver && this.FM.Gears.nOfGearsOnGr == 0) {
            this.FM.CT.cockpitDoorControl = 0.0F;
            this.FM.CT.bHasCockpitDoorControl = false;
        }
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        super.update(f);
        if (this.FM.getSpeed() > 5F) {
            this.moveSlats(f);
            this.bSlatsOff = false;
        } else this.slatsOff();
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            if (this.FM.EI.engines[0].getPowerOutput() > 0.45F && this.FM.EI.engines[0].getStage() == 6) {
                if (this.FM.EI.engines[0].getPowerOutput() > 1.001F) this.FM.AS.setSootState(this, 0, 5);
                else if (this.FM.EI.engines[0].getPowerOutput() > 0.65F && this.FM.EI.engines[0].getPowerOutput() < 1.001F) this.FM.AS.setSootState(this, 0, 3);
                else this.FM.AS.setSootState(this, 0, 2);
            } else this.FM.AS.setSootState(this, 0, 0);
            this.setExhaustFlame(Math.round(Aircraft.cvt(this.FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
        }
    }

    protected void moveSlats(float paramFloat) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.15F);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 0.1F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.065F);
        this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        this.hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.1F);
        this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        this.hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void slatsOff() {
        if (!this.bSlatsOff) {
            this.resetYPRmodifier();
            Aircraft.xyz[0] = -0.15F;
            Aircraft.xyz[1] = 0.1F;
            Aircraft.xyz[2] = -0.065F;
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 8.5F, 0.0F);
            this.hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
            Aircraft.xyz[1] = -0.1F;
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 8.5F, 0.0F);
            this.hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
            this.bSlatsOff = true;
        }
    }

    private GuidedMissileUtils guidedMissileUtils;
    private Eff3DActor         turbo;
    private Eff3DActor         turbosmoke;
    private Eff3DActor         afterburner;
    private boolean            hasChaff;
    private boolean            hasFlare;
    private long               lastChaffDeployed;
    private long               lastFlareDeployed;
    private long               lastCommonThreatActive;
    private long               intervalCommonThreat;
    private long               lastRadarLockThreatActive;
    private long               intervalRadarLockThreat;
    private long               lastMissileLaunchThreatActive;
    private long               intervalMissileLaunchThreat;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR      = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR  = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR      = 2F;
    private static final float POS_G_RECOVERY_FACTOR  = 2F;
    public boolean             bToFire;

    static {
        Class localClass = F_86K.class;
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
        Property.set(localClass, "cockpitClass", new Class[] { CockpitF_86K.class });
        Property.set(localClass, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(localClass, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 9, 2, 2, 9, 2, 2 });
        Aircraft.weaponHooksRegister(localClass, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08",
                "_ExternalRock02", "_ExternalRock02" });
    }
}
