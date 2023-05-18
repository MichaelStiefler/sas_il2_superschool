package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class TA_152C extends TA_152NEW implements TypeX4Carrier {

    public TA_152C() {
        this.bToFire = false;
        this.tX4Prev = 0L;
        this.kangle = 0.0F;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -102F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -102F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 20F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        float f1 = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.1F);
        hiermesh.chunkSetLocate("poleL_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetLocate("poleR_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveGear(float f) {
        TA_152C.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.44F, 0.0F, 0.44F);
        this.hierMesh().chunkSetLocate("GearL2a_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.44F, 0.0F, 0.44F);
        this.hierMesh().chunkSetLocate("GearR2a_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFlap(float f) {
        float f1 = -50F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -f1, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) || !flag || !(this.FM instanceof Pilot)) {
            return;
        }
        Pilot pilot = (Pilot) this.FM;
        if ((pilot.get_maneuver() == 63) && (((Maneuver) (pilot)).target != null)) {
            Point3d point3d = new Point3d(((FlightModelMain) (((Maneuver) (pilot)).target)).Loc);
            point3d.sub(this.FM.Loc);
            this.FM.Or.transformInv(point3d);
            if ((((point3d.x > 4000D) && (point3d.x < 5500D)) || ((point3d.x > 100D) && (point3d.x < 5000D) && (World.Rnd().nextFloat() < 0.33F))) && (Time.current() > (this.tX4Prev + 10000L))) {
                this.bToFire = true;
                this.tX4Prev = Time.current();
            }
        }
    }

    public void update(float f) {
        for (int i = 1; i < 15; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * this.kangle, 0.0F);
        }

        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        super.update(f);
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 1.0F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -1F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 1.0F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -1F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        if (this.getGunByHookName("_CANNON03") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmL1_D0", false);
        }
        if (this.getGunByHookName("_CANNON04") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmR1_D0", false);
        }
    }

    public boolean bToFire;
    private long   tX4Prev;
    private float  kangle;
    private float  deltaAzimuth;
    private float  deltaTangage;

    static {
        Class class1 = TA_152C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ta.152");
        Property.set(class1, "meshName", "3DO/Plane/Ta-152C/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Ta-152C.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTA_152C.class });
        Property.set(class1, "LOSElevation", 0.755F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 1, 1, 1, 1, 9, 9, 2, 2, 2, 2, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalBomb02", "_ExternalBomb03" });
    }
}
