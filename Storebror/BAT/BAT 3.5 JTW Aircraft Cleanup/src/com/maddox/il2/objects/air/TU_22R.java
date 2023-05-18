package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class TU_22R extends TU_22 implements TypeCountermeasure, TypeFuelDump {

    public TU_22R() {
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.removeChuteTimer = -1L;
    }

    public float getFlowRate() {
        return TU_22R.FlowRate;
    }

    public float getFuelReserve() {
        return TU_22R.FuelReserve;
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 70F * f);
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

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.Skill = 3;
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
        if (this.FM.CT.Weapons[0] != null) {
        }
    }

    public void update(float f) {
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

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "22R_";
    }

    private boolean     hasChaff;
    private boolean     hasFlare;
    private long        lastChaffDeployed;
    private long        lastFlareDeployed;
    public static float FlowRate    = 10F;
    public static float FuelReserve = 1500F;
    public boolean      bToFire;
    private boolean     bHasDeployedDragChute;
    private long        removeChuteTimer;
    private float       arrestor;
    private Chute       chute1;
    private Chute       chute2;

    static {
        Class class1 = TU_22R.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Tu-22");
        Property.set(class1, "meshName", "3DO/Plane/Tu-22/hierTu22R.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/Tu-22.fmd:TU22FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTU_22.class, CockpitTU_22_Bombardier.class, CockpitTU_22_Gunner.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24", "_ExternalRock01", "_ExternalRock02" });
    }
}
