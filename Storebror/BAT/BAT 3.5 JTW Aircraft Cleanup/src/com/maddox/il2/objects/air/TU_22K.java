package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class TU_22K extends TU_22fuelReceiver implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeFuelDump {

    public TU_22K() {
        this.guidedMissileUtils = null;
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
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.removeChuteTimer = -1L;
        this.dynamoOrient = 0.0F;
        this.g1 = null;
    }

    public float getFlowRate() {
        return TU_22K.FlowRate;
    }

    public float getFuelReserve() {
        return TU_22K.FuelReserve;
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 70F * f);
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 85F), 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -85F), 0.0F);
    }

    public void moveWingFold(float f) {
        if (f < 0.001F) {
            this.setGunPodsOn(true);
            this.hideWingWeapons(false);
        } else {
            this.setGunPodsOn(false);
            this.FM.CT.WeaponControl[0] = false;
            this.hideWingWeapons(false);
        }
        this.moveWingFold(this.hierMesh(), f);
    }

    public long getChaffDeployed() {
        if (this.hasChaff) {
            return this.lastChaffDeployed;
        } else {
            return 0L;
        }
    }

    public long getFlareDeployed() {
        if (this.hasFlare) {
            return this.lastFlareDeployed;
        } else {
            return 0L;
        }
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

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
        if (this.FM.CT.Weapons[0] != null) {
            this.g1 = this.FM.CT.Weapons[0][0];
        }
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-33F * this.FM.Gears.arrestorVSink) / 57F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.2F) {
                    f2 = 0.2F;
                }
                if (f2 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f2));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
                }
                if (this.arrestor < 0.0F) {
                    this.arrestor = 0.0F;
                } else if (this.arrestor > 1.0F) {
                    this.arrestor = 1.0F;
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
        if (this.FM.CT.FlapsControl > 0.2F) {
            this.FM.CT.BlownFlapsControl = 1.0F;
        } else {
            this.FM.CT.BlownFlapsControl = 0.0F;
        }
        super.update(f);
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute1 = new Chute(this);
            this.chute2 = new Chute(this);
            this.chute1.setMesh("3do/plane/ChuteSu_25/mono.sim");
            this.chute2.setMesh("3do/plane/ChuteSu_25/mono.sim");
            this.chute1.mesh().setScale(0.5F);
            this.chute2.mesh().setScale(0.5F);
            this.chute1.pos.setRel(new Point3d(-18D, 0.0D, 0.6D), new Orient(20F, 90F, 0.0F));
            this.chute2.pos.setRel(new Point3d(-18D, 0.0D, 0.6D), new Orient(-20F, 90F, 0.0F));
            this.bHasDeployedDragChute = true;
        } else if (this.bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl) {
            if (((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() > 600F)) || (this.FM.CT.DragChuteControl < 1.0F)) {
                if ((this.chute1 != null) && (this.chute2 != null)) {
                    this.chute1.tangleChute(this);
                    this.chute2.tangleChute(this);
                    this.chute1.pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(20F, 90F, 0.0F));
                    this.chute2.pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(-20F, 90F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 250L;
            } else if ((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() < 20F)) {
                if ((this.chute1 != null) && (this.chute2 != null)) {
                    this.chute1.tangleChute(this);
                    this.chute2.tangleChute(this);
                }
                this.chute1.pos.setRel(new Orient(10F, 100F, 0.0F));
                this.chute2.pos.setRel(new Orient(-10F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 10000L;
            }
        }
        if ((this.removeChuteTimer > 0L) && !this.FM.CT.bHasDragChuteControl && (Time.current() > this.removeChuteTimer)) {
            this.chute1.destroy();
            this.chute2.destroy();
        }
    }

    public void moveFan(float f) {
        if ((this.g1 != null) && this.g1.isShots() && (this.oldbullets != this.g1.countBullets())) {
            this.oldbullets = this.g1.countBullets();
            if (this.dynamoOrient == 360F) {
                this.dynamoOrient = 0.0F;
            }
            this.dynamoOrient = this.dynamoOrient + 30F;
            this.hierMesh().chunkSetAngles("6x20mm_C", 0.0F, this.dynamoOrient, 0.0F);
            this.hierMesh().chunkSetAngles("6x20mm_L", 0.0F, this.dynamoOrient, 0.0F);
            this.hierMesh().chunkSetAngles("6x20mm_R", 0.0F, this.dynamoOrient, 0.0F);
        }
        super.moveFan(f);
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "22K_";
    }

    private GuidedMissileUtils guidedMissileUtils;
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
    public static float        FlowRate    = 10F;
    public static float        FuelReserve = 1500F;
    public boolean             bToFire;
    private boolean            bHasDeployedDragChute;
    private long               removeChuteTimer;
    private float              arrestor;
    private BulletEmitter      g1;
    private int                oldbullets;
    private float              dynamoOrient;
    private Chute              chute1;
    private Chute              chute2;

    static {
        Class class1 = TU_22K.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Tu-22");
        Property.set(class1, "meshName", "3DO/Plane/Tu-22/hierTu22K.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/Tu-22K.fmd:TU22FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTU_22.class, CockpitTU_22_Bombardier.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_InternalBomb01", "_InternalBomb02", "_InternalBomb03", "_InternalBomb04", "_InternalBomb05", "_InternalBomb06", "_InternalBomb07", "_InternalBomb08", "_InternalBomb09", "_InternalBomb10", "_InternalBomb11", "_InternalBomb12", "_InternalBomb13", "_InternalBomb14", "_InternalBomb15", "_InternalBomb16", "_InternalBomb17", "_InternalBomb18", "_InternalBomb19", "_InternalBomb20", "_InternalBomb21", "_InternalBomb22", "_InternalBomb23", "_InternalBomb24", "_ExternalRock01", "_ExternalRock02" });
    }
}
