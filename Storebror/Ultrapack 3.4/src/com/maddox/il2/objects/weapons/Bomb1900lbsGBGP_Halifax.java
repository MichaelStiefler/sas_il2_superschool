package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1900lbsGBGP_Halifax extends Bomb {

    static {
        Class class1 = Bomb1900lbsGBGP_Halifax.class;
        Property.set(class1, "mesh", "3DO/Arms/Bomb1900lbsGBGP_Halifax/mono.sim");
        Property.set(class1, "power", 490F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 870F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
