package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class YAK_7B_late extends YAK_7BPF {

    public YAK_7B_late() {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            YAK_7B_late.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            YAK_7B_late.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = YAK_7B_late.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-7B_late/hier.him");
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_7B_late.class });
        Property.set(class1, "FlightModel", "FlightModels/Yak-9.fmd");
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
    }
}
