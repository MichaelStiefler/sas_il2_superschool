package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbsDC extends Bomb {

    static {
        Class class1 = Bomb500lbsDC.class;
        Property.set(class1, "mesh", "3DO/Arms/500lbs_Charge/mono.sim");
        Property.set(class1, "radius", 58F);
        Property.set(class1, "power", 60F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3683F);
        Property.set(class1, "massa", 230F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
