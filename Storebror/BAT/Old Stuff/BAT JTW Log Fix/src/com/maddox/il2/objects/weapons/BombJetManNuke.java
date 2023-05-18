package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombJetManNuke extends Bomb {

    static {
        Class class1 = com.maddox.il2.objects.weapons.BombJetManNuke.class;
        Property.set(class1, "mesh", "3DO/Arms/JetManNuke/mono.sim");
        Property.set(class1, "power", 1.3E+007F);
        Property.set(class1, "radius", 2500F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "newEffect", 1);
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", ((new Object[] { Fuze_generic_LongDelay.class })));
    }
}
