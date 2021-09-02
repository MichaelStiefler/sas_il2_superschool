package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbGP_MkIII extends Bomb {

    static {
        Class class1 = Bomb250lbGP_MkIII.class;
        Property.set(class1, "mesh", "3do/arms/250lbGP_MkIII/mono.sim");
        Property.set(class1, "radius", 70F);
        Property.set(class1, "power", 30.9F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.26F);
        Property.set(class1, "massa", 112F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No19.class, Fuze_Pistol_No20.class, Fuze_Pistol_No17.class, Fuze_Pistol_No22.class })));
    }
}
