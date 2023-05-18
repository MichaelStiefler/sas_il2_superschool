package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class BF_109G6KOVACS extends BF_109 implements TypeBNZFighter, TypeAcePlane {

    public BF_109G6KOVACS() {
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
        BF_109G6KOVACS.moveGear(this.hierMesh(), f, f1, f2);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGearC() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    private float kangle;
    private float flapps;

    static {
        Class class1 = BF_109G6KOVACS.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3do/plane/Bf-109G-6Early(ofKovacs)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
        Property.set(class1, "FlightModel", "FlightModels/Bf-109G-6Early.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109G6.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 1, 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_ExternalDev01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
