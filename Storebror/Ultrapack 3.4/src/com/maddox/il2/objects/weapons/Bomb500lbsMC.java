package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbsMC extends Bomb {
    static {
        Class class1 = com.maddox.il2.objects.weapons.Bomb500lbsMC.class;
        Property.set(class1, "mesh", "3DO/Arms/500lbsMC/mono.sim");
        Property.set(class1, "radius", 170F);
        Property.set(class1, "power", 180F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 312F);
        Property.set(class1, "sound", "weapon.bomb_big");
        try {
            Class fuze1 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo44");
            Class fuze2 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo54");
            Class fuze3 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo28");
            Property.set(class1, "fuze", new Object[] { fuze1, fuze2, fuze3 });
        } catch (Exception e) {
        }
    }
}
