package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombHeisenberg extends Bomb
{
    static 
    {
        Class class1 = BombHeisenberg.class;
        Property.set(class1, "mesh", "3DO/Arms/Heisenberg/mono.sim");
        Property.set(class1, "radius", 3200F);
        Property.set(class1, "power", 11000000F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.0F);
        Property.set(class1, "massa", 4630F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "newEffect", 1);
        Property.set(class1, "nuke", 1);
    }
}
