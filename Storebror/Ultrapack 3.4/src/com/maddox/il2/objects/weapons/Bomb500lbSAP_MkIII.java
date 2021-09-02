package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbSAP_MkIII extends Bomb {

    static {
        Class class1 = Bomb500lbSAP_MkIII.class;
        Property.set(class1, "mesh", "3do/arms/500lbSAP_MkIII/mono.sim");
        Property.set(class1, "radius", 40F);
        Property.set(class1, "power", 40F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.34F);
        Property.set(class1, "massa", 220F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No30.class })));
    }
}
