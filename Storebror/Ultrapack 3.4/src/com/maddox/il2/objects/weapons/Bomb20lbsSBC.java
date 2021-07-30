package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb20lbsSBC extends Bomb {

    protected boolean haveSound() {
        return false;
    }

    static {
        Class class1 = Bomb20lbsSBC.class;
        Property.set(class1, "mesh", "3DO/Arms/Bomb20lbsSBC/mono.sim");
        Property.set(class1, "power", 4.55F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.12827F);
        Property.set(class1, "massa", 9.07F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
