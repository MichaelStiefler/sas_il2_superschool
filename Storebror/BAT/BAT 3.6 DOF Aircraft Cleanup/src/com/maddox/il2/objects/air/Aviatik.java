package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Aviatik extends Aviatikx {

    public Aviatik() {
    }

    static {
        Class class1 = Aviatik.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Aviatik");
        Property.set(class1, "meshName", "3DO/Plane/Aviatik(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_il", "3DO/Plane/Aviatik(il)/hier.him");
        Property.set(class1, "PaintScheme_il", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1914F);
        Property.set(class1, "yearExpired", 1918F);
        Property.set(class1, "FlightModel", "FlightModels/U-2VS.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAviatik.class, CockpitAviatik_TGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
