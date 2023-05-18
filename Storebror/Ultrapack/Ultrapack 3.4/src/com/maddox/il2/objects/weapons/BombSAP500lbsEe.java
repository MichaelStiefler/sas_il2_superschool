package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombSAP500lbsEe extends Bomb {
    static {
        Class class1 = BombSAP500lbsEe.class;
        Property.set(class1, "mesh", "3DO/Arms/SAP500LbsEe/mono.sim");
        Property.set(class1, "radius", 60F);
        Property.set(class1, "power", 90F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.35F);
        Property.set(class1, "massa", 226F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
