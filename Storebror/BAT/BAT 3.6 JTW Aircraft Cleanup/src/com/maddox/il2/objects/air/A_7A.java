package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class A_7A extends A_7fuelReceiver implements TypeCountermeasure, TypeThreatDetector, TypeFastJet {

    public A_7A() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
    }

    public void update(float f) {
        super.update(f);
    }

    public boolean bToFire;

    static {
        Class class1 = A_7A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-7A");
        Property.set(class1, "meshName", "3DO/Plane/A7A/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1966.9F);
        Property.set(class1, "yearExpired", 1994.3F);
        Property.set(class1, "FlightModel", "FlightModels/A7A.fmd:Vought_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA_7.class, CockpitA_7Bombardier.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev19", "_ExternalDev20", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16",
                        "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalDev21", "_ExternalDev22", "_ExternalPylon1", "_ExternalPylon2", "_Pylon3", "_Pylon4", "_ExternalPylon5", "_ExternalPylon6", "_Pylon7", "_Pylon8", "_ExternalBomb25", "_ExternalBomb26", "_Bomb27", "_Bomb28", "_ExternalBomb29", "_ExternalBomb30", "_Bomb31", "_Bomb32", "_ExternalBomb33", "_ExternalBomb34", "_Bomb35", "_Bomb36", "_ExternalBomb37", "_ExternalBomb38", "_Bomb39",
                        "_Bomb40", "_ExternalBomb41", "_ExternalBomb42", "_Bomb43", "_Bomb44", "_ExternalBomb45", "_ExternalBomb46", "_Bomb47", "_Bomb48" });
    }
}
