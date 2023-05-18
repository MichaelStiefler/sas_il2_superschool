package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbDC_MkVIII extends Bomb {

    static {
        Class class1 = Bomb250lbDC_MkVIII.class;
        Property.set(class1, "mesh", "3do/arms/250lbDC_MkVIII/mono.sim");
        Property.set(class1, "radius", 56F);
        Property.set(class1, "power", 72.6F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 113.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_MkX.class, Fuze_Pistol_MkXIIIA.class })));
    }
}
