package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class V_156_F extends SB2Uxyz {
    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake1_D0", 0.0F, 0.0F, 85F * f);
        this.hierMesh().chunkSetAngles("Brake2_D0", 0.0F, 0.0F, 85F * f);
        this.hierMesh().chunkSetAngles("Brake3_D0", 0.0F, 0.0F, 88F * f);
        this.hierMesh().chunkSetAngles("Brake4_D0", 0.0F, 0.0F, 88F * f);
    }

    static {
        Class class1 = V_156_F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "V-156F");
        Property.set(class1, "meshName", "3DO/Plane/V156F/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryFrance);
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/SB2U.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSB2U.class });
        Property.set(class1, "LOSElevation", 0.84305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
