package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbsE_MC extends Bomb {

    static {
        Class class1 = Bomb1000lbsE_MC.class;
        Property.set(class1, "mesh", "3DO/Arms/1000LbsBombE_MC/mono.sim");
        Property.set(class1, "radius", 365.76F);
        Property.set(class1, "power", 226.79F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 463F);
        Property.set(class1, "sound", "weapon.bomb_big");
        try
        {
          Class fuze1 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo44");
          Class fuze2 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo54");
          Class fuze3 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo28");
          Property.set(class1, "fuze", new Object[] { fuze1, fuze2, fuze3 });
        }
        catch (Exception localException) {}
    }
}
