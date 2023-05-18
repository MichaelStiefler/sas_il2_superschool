package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombRRAB3_1 extends BombRRAB {

    protected void doFireContaineds() {
        this.doFireContaineds(25, 3, BombAO10sub.class, BombRRAB3Nose.class);
        this.M = 20F;
    }

    static {
        Class class1 = BombRRAB3_1.class;
        Property.set(class1, "mesh", "3DO/Arms/RRAB3_39/mono.sim");
        Property.set(class1, "mesh_released", "3DO/Arms/RRAB3_39/mono1.sim");
        Property.set(class1, "mesh_tail", "3DO/Arms/RRAB3_39/mono3.sim");
        Property.set(class1, "rotate", 1);
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.52F);
        Property.set(class1, "massa", 310F);
        Property.set(class1, "sound", "weapon.bomb_std");
        Property.set(class1, "dateOfUse", 19400101);
    }
}
