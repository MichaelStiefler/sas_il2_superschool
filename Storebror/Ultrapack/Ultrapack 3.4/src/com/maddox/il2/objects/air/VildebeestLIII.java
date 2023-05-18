package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class VildebeestLIII extends Biplanexyz {
    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, -40F * f, 0.0F);
    }

    static {
        Class class1 = VildebeestLIII.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "VildebeestL");
        Property.set(class1, "meshName", "3DO/Plane/Vildebeest/hierLIII.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/VildebeestLIII.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitVildebeestLIII.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 10, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalDev11", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb30", "_ExternalBomb31", "_ExternalBomb32", "_ExternalBomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalBomb37", "_ExternalBomb38",
                "_ExternalBomb39", "_ExternalBomb40", "_ExternalBomb41", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalBomb42", "_ExternalBomb43", "_ExternalBomb44", "_ExternalBomb45", "_ExternalBomb46", "_ExternalBomb47", "_ExternalBomb48", "_ExternalBomb49", "_ExternalBomb50", "_ExternalBomb51", "_ExternalBomb52", "_ExternalBomb53", "_ExternalBomb54", "_ExternalBomb55", "_ExternalBomb56", "_ExternalBomb57", "_ExternalBomb58", "_ExternalBomb59", "_ExternalBomb60", "_ExternalBomb61", "_ExternalBomb62", "_ExternalBomb63", "_ExternalBomb64", "_ExternalBomb65", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalDev19" });
    }
}
