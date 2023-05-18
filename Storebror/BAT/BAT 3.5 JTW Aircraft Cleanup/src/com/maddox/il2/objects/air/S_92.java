package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class S_92 extends ME_262 implements TypeStormovik {

    public S_92() {
    }

    static {
        Class class1 = S_92.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "S-92");
        Property.set(class1, "meshName", "3DO/Plane/S-92/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1944.1F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Me-262A-1a.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_262.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev03", "_ExternalDev04", "_CANNON05", "_CANNON06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalDev01", "_ExternalDev02", "_ExternalRock25", "_ExternalRock25", "_ExternalRock26", "_ExternalRock26", "_ExternalDev07", "_ExternalDev08" });
    }
}
