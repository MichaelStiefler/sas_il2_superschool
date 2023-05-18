package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class YAK_9TALBERT extends YAK_9TX implements TypeAcePlane {

    public YAK_9TALBERT() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.Skill = 3;
    }

    static {
        Class class1 = YAK_9TALBERT.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-9T(ofAlbert)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
        Property.set(class1, "FlightModel", "FlightModels/Yak-9T.fmd");
        weaponTriggersRegister(class1, new int[] { 0, 1 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_CANNON01" });
    }
}
