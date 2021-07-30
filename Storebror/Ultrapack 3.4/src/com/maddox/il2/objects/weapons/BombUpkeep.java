package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombUpkeep extends Bomb {

    static {
        Class class1 = BombUpkeep.class;
        Property.set(class1, "mesh", "3DO/Arms/Upkeep/upkeep.sim");
        Property.set(class1, "radius", 400F);
        Property.set(class1, "power", 1380F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.76F);
        Property.set(class1, "massa", 1780F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
