package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombNuke_Mark17 extends BombNukeChute {

    static {
        Class class1 = BombNuke_Mark17.class;
        Property.set(class1, "mesh", "3do/arms/Nuke_Mark17/mono.sim");
        Property.set(class1, "meshOpen", "3DO/Arms/Nuke_Mark17/Open/mono.sim");
        Property.set(class1, "radius", 5500F);
        Property.set(class1, "power", 1500000F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.46F);
        Property.set(class1, "massa", 21000F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "newEffect", 1);
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", new Object[] { Fuze_Mk17Mod0.class, Fuze_TX14_Baro.class, Fuze_T91.class });
    }
}
