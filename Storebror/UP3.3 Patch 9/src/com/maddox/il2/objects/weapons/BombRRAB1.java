package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombRRAB1 extends BombRRAB {

    protected void doFireContaineds() {
        this.doFireContaineds(100, 4, BombAO10sub.class, BombRRAB1Nose.class);
        this.M = 25F;
    }

    static {
        Class class1 = BombRRAB1.class;
        Property.set(class1, "mesh", "3DO/Arms/RRAB1/mono.sim");
        Property.set(class1, "mesh_released", "3DO/Arms/RRAB1/mono1.sim");
        Property.set(class1, "mesh_tail", "3DO/Arms/RRAB1/mono3.sim");
        Property.set(class1, "rotate", 1);
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.72F);
        Property.set(class1, "massa", 1150F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
