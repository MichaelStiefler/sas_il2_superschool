package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombNuke_Mark4 extends BombNuke
{

    static 
    {
        Class class1 = BombNuke_Mark4.class;
        Property.set(class1, "mesh", "3do/arms/Nuke_Mark4/mono.sim");
        Property.set(class1, "radius", 3500F);
        Property.set(class1, "power", 310000F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.52F);
        Property.set(class1, "massa", 4944F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "newEffect", 1);
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", new Object[] {
            Fuze_Mk17Mod0.class, Fuze_TX14_Baro.class, Fuze_T91.class
        });
    }
}
