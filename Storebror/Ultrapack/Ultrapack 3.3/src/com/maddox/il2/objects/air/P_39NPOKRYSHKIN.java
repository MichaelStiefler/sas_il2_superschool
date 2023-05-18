package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_39NPOKRYSHKIN extends P_39 implements TypeAcePlane {

    public P_39NPOKRYSHKIN() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.Skill = 3;
    }

    static {
        Class class1 = P_39NPOKRYSHKIN.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P39");
        Property.set(class1, "meshName", "3do/plane/P-39N(ofPokryshkin)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
        Property.set(class1, "FlightModel", "FlightModels/P-39N(ofPokryshkin).fmd");
        weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 1, 1, 1, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_CANNON01", "_ExternalBomb01" });
    }
}
