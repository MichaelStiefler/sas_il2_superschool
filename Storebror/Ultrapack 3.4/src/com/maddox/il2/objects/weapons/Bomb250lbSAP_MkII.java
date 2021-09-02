package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbSAP_MkII extends Bomb {

    static {
        Class class1 = Bomb250lbSAP_MkII.class;
        Property.set(class1, "mesh", "3do/arms/250lbSAP_MkII/mono.sim");
        Property.set(class1, "radius", 27F);
        Property.set(class1, "power", 20F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.23F);
        Property.set(class1, "massa", 110F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No30.class })));
    }
}
