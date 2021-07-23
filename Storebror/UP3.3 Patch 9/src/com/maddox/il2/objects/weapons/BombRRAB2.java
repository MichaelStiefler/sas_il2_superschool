package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombRRAB2 extends BombRRAB {

    protected void doFireContaineds() {
        this.doFireContaineds(66, 3, BombAO10sub.class, BombRRAB2Nose.class);
        this.M = 22F;
    }

    static {
        Class class1 = BombRRAB2.class;
        Property.set(class1, "mesh", "3DO/Arms/RRAB2/mono.sim");
        Property.set(class1, "mesh_released", "3DO/Arms/RRAB2/mono1.sim");
        Property.set(class1, "mesh_tail", "3DO/Arms/RRAB2/mono3.sim");
        Property.set(class1, "rotate", 1);
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 630F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
