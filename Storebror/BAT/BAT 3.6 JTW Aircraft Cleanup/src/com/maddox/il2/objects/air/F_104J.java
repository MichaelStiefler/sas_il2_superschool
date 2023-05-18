package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class F_104J extends F_104 implements TypeGuidedMissileCarrier, TypeThreatDetector, TypeGSuit, TypeAcePlane {

    public F_104J() {
        this.guidedMissileUtils = null;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.engineSFX = null;
        this.engineSTimer = 0x98967f;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "104J_";
    }

    public float getFlowRate() {
        return F_104J.FlowRate;
    }

    public float getFuelReserve() {
        return F_104J.FuelReserve;
    }

    public void setCommonThreatActive() {
        long l = Time.current();
        if ((l - this.lastCommonThreatActive) > this.intervalCommonThreat) {
            this.lastCommonThreatActive = l;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long l = Time.current();
        if ((l - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = l;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        long l = Time.current();
        if ((l - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
            this.lastMissileLaunchThreatActive = l;
            this.doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat() {
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
    }

    public static final double Rnd(double d, double d1) {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1) {
        return World.Rnd().nextFloat(f, f1);
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        this.FM.CT.bHasDragChuteControl = true;
        if (this.thisWeaponsName.startsWith("GAttack:")) {
            this.hierMesh().chunkVisible("PylonL2", false);
            this.hierMesh().chunkVisible("PylonR2", false);
            this.FM.Sq.dragParasiteCx += 0.0001F;
        }
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        this.computeJ79GE11_AB();
        super.update(f);
    }

    public void computeJ79GE11_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 20000D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 21F) {
                f1 = 10F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                float f5 = f4 * f;
                f1 = ((((-0.000273872F * f5) + (0.0131599F * f4)) - (0.199571F * f3)) + (1.01749F * f2)) - (1.49134F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    private GuidedMissileUtils guidedMissileUtils;
    private long               lastCommonThreatActive;
    private long               intervalCommonThreat;
    private long               lastRadarLockThreatActive;
    private long               intervalRadarLockThreat;
    private long               lastMissileLaunchThreatActive;
    private long               intervalMissileLaunchThreat;
    public static float        FlowRate    = 10F;
    public static float        FuelReserve = 1500F;
    public boolean             bToFire;
    protected SoundFX          engineSFX;
    protected int              engineSTimer;

    static {
        Class class1 = F_104J.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-104J");
        Property.set(class1, "meshName", "3DO/Plane/F-104/hierF104G.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1966F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/F-104G.fmd:F104FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_104G.class, CockpitF_104Bombardier.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 3, 9, 9, 2, 2, 2, 2, 9, 9, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_ExternalDev01", "_ExternalDev02", "_Rock05", "_Rock06", "_ExternalRock07", "_Rock08", "_ExternalDev03", "_ExternalDev04", "_Rock09", "_Rock10", "_ExternalRock11", "_Rock12", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_Rock13", "_Rock14", "_ExternalRock15", "_Rock16", "_ExternalBomb01", "_ExternalDev11", "_ExternalDev12", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_Dev13", "_ExternalDev14", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
