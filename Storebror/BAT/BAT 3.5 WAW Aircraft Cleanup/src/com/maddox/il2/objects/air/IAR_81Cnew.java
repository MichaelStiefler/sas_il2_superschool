package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class IAR_81Cnew extends IAR_8X implements TypeFighter, TypeBNZFighter {

    public IAR_81Cnew() {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            IAR_81Cnew.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            IAR_81Cnew.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = IAR_81Cnew.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "IAR 81C");
        Property.set(class1, "meshName", "3DO/Plane/IAR81C/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitIAR81.class });
        Property.set(class1, "FlightModel", "FlightModels/IAR-80M.fmd");
        Property.set(class1, "LOSElevation", 0.8323F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN05", "_MGUN06" });
    }
}
