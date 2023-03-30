package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BF_109T extends BF_109Ex {

    public void update(float f) {
        super.update(f);
        if (this.FM.isPlayers()) {
            if (this.FM.Gears.arrestorVAngle == 0.0F || this.FM.CT.arrestorControl < 0.5F) this.FM.setGC_Gear_Shift(-0.45F);
            else this.FM.setGC_Gear_Shift(0.0F);
        }
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -37F * f, 0.0F);
        this.arrestor = f;
    }

    protected float arrestor;

    static {
        Class class1 = BF_109T.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109T/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109T-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109Ex.class });
        Property.set(class1, "LOSElevation", 0.74985F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02" });
    }
}
