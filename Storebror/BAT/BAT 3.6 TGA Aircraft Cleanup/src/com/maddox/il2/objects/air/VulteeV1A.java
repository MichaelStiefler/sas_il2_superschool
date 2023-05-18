package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class VulteeV1A extends VulteeAxyz implements TypeStormovikArmored {

    public VulteeV1A() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        Math.max(-f * 800F, -70F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 120F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -120F * f, 0.0F);
    }

    protected void moveGear(float f) {
        VulteeV1A.moveGear(this.hierMesh(), f);
    }

    static {
        Class class1 = VulteeV1A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Vultee");
        Property.set(class1, "meshName", "3DO/Plane/Vultee/hierA.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/VulteeV1A.fmd:VulteeV1A_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitVultee.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 3, 3, 9, 9, 9, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb07", "_ExternalDev04", "_ExternalDev05" });
    }
}
