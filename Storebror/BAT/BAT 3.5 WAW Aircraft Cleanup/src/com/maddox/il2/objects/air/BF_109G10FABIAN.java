package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class BF_109G10FABIAN extends BF_109 implements TypeBNZFighter, TypeAcePlane {

    public BF_109G10FABIAN() {
        this.kangle = 0.0F;
        this.flapps = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.Skill = 3;
    }

    public void update(float f) {
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
        }
        if (Math.abs(this.flapps - this.kangle) > 0.01F) {
            this.flapps = this.kangle;
            this.hierMesh().chunkSetAngles("Flap01L_D0", 0.0F, -20F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap01U_D0", 0.0F, 20F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap02L_D0", 0.0F, -20F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap02U_D0", 0.0F, 20F * this.kangle, 0.0F);
        }
        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        if (this.kangle > 1.0F) {
            this.kangle = 1.0F;
        }
        super.update(f);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        float f3 = 0.8F;
        float f4 = (-0.5F * (float) Math.cos((f / f3) * Math.PI)) + 0.5F;
        if ((f <= f3) || (f == 1.0F)) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -33.5F * f4, 0.0F, 0.0F);
        }
        f4 = (-0.5F * (float) Math.cos(((f1 - (1.0F - f3)) / f3) * Math.PI)) + 0.5F;
        if (f1 >= (1.0F - f3)) {
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 33.5F * f4, 0.0F, 0.0F);
        }
        if (f > 0.99F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -33.5F, 0.0F, 0.0F);
        }
        if (f < 0.01F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 0.0F);
        }
        if (f1 > 0.99F) {
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 33.5F, 0.0F, 0.0F);
        }
        if (f1 < 0.01F) {
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
        }
    }

    protected void moveGear(float f, float f1, float f2) {
        BF_109G10FABIAN.moveGear(this.hierMesh(), f, f1, f2);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    private float kangle;
    private float flapps;

    static {
        Class class1 = BF_109G10FABIAN.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109G-10(ofFabian)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109G-10.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109G10.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 1, 1, 1, 1, 1, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_ExternalDev02", "_ExternalDev03" });
    }
}
