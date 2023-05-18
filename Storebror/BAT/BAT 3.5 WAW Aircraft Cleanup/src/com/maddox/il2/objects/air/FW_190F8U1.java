package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class FW_190F8U1 extends FW_190 {

    public FW_190F8U1() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        this.hierMesh().chunkVisible("Flap01_D0", true);
        this.hierMesh().chunkVisible("Flap01Holed_D0", false);
        this.hierMesh().chunkVisible("Flap04_D0", true);
        this.hierMesh().chunkVisible("Flap04Holed_D0", false);
    }

    protected void moveGear(float f) {
        FW_190.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    static {
        Class class1 = FW_190F8U1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190F-8U1(Beta)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190F-8.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190F8U1.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb05" });
    }
}
