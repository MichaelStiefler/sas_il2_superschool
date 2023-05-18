package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class LA_7KOJEDUB extends LA_X implements TypeAcePlane {

    public LA_7KOJEDUB() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.Skill = 3;
    }

    static {
        Class class1 = LA_7KOJEDUB.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "La");
        Property.set(class1, "meshName", "3DO/Plane/La-7(ofKojedub)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/La-7.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLA_7.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
