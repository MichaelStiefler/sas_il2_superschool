package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbDC_Mk9 extends Bomb {
    static {
        Class class1 = Bomb250lbDC_Mk9.class;
        Property.set(class1, "mesh", "3DO/Arms/250lbDC_Mk9/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 50F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.27F);
        Property.set(class1, "massa", 128F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
