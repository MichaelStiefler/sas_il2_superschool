package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombABKP3 extends BombABKP {

    protected void doFireContaineds() {
        this.doFireContaineds(10, BombAO10sub.class, BombABKPNose.class, BombABKPTop.class, BombABKPBottom.class);
    }

    static {
        Class class1 = BombABKP3.class;
        Property.set(class1, "mesh", "3DO/Arms/ABKP/mono.sim");
        Property.set(class1, "mesh_tail", "3DO/Arms/ABKP/mono4.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.28F);
        Property.set(class1, "massa", 103.6F);
        Property.set(class1, "sound", "weapon.bomb_std");
//        Property.set(class1, "fuze", ((Object) (new Object[] {
//            Fuze_AD_A.class
//        })));
    }
}
