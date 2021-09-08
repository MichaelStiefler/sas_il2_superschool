package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class AT_32 extends AT_32xyz {
    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 92F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 92F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -143F * f);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, -143F * f);
    }

    protected void moveGear(float f) {
        AT_32.moveGear(this.hierMesh(), f);
    }

    static {
        Class class1 = AT_32.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Condor");
        Property.set(class1, "meshName", "3DO/Plane/AT-32/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/AT32.fmd");
        Property.set(class1, "LOSElevation", 1.1058F);
        Property.set(class1, "cockpitClass", new Class[] { Cockpit_AT32.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalBomb01", "_ExternalBomb02" });
    }
}
