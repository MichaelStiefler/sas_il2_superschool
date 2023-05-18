package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class IAR_80A extends IAR_8X implements TypeFighter, TypeBNZFighter {

    public IAR_80A() {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            IAR_80A.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            IAR_80A.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = IAR_80A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "IAR 80");
        Property.set(class1, "meshName", "3DO/Plane/IAR80early/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitIAR81.class });
        Property.set(class1, "FlightModel", "FlightModels/IAR-80A.fmd");
        Property.set(class1, "LOSElevation", 0.8323F);
        Aircraft.weaponTriggersRegister(class1, new int[4]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
