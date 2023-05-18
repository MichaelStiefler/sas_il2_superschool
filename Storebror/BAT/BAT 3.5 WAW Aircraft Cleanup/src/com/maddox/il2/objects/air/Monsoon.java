package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Monsoon extends Monsoonx implements TypeSailPlane {

    public Monsoon() {
    }

    public void onAircraftLoaded() {
        this.FM.EI.engines[0].doSetKillControlAfterburner();
        super.onAircraftLoaded();
    }

    static {
        Class class1 = Monsoon.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Monsoon");
        Property.set(class1, "meshName", "3DO/Plane/Monsoon/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_gb", "3DO/Plane/Monsoon/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Monsoon.fmd:Monsoon_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTYPHOON1B.class });
        Property.set(class1, "LOSElevation", 0.93655F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
