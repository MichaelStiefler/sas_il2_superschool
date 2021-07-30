package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombSC2500Ju288 extends Bomb {

    static {
        Class class1 = BombSC2500Ju288.class;
        Property.set(class1, "mesh", "3DO/Arms/SC-2500-Ju288/mono.sim");
        Property.set(class1, "power", 1200F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.8128F);
        Property.set(class1, "massa", 2400F);
        Property.set(class1, "sound", "weapon.bomb_big");
//        Property.set(class1, "fuze", ((new Object[] { Fuze_EL_AZ25.class })));
        try {
            Object o = Class.forName("com.maddox.il2.objects.weapons.Fuze_EL_AZ25");
            Property.set(class1, "fuze", ((Object) (new Object[] { o })));
        } catch (Exception e) {

        }
    }
}
