package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombMk84 extends com.maddox.il2.objects.weapons.Bomb
{

    public BombMk84()
    {
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.weapons.BombMk84.class;
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "mesh", "3DO/Arms/Mk84/mono.sim");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "radius", 400.65F);  //
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "power",  428.644F);  //945 lb, lbs = 428.644 789 65 kilogram
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "powerType", 0);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "kalibr", 0.457F);   // 18 inch = 0.457 2 meter
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "massa", 893.57F);  //1 970 lb, lbs = 893.576 968 9 kilogram
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "sound", "weapon.bomb_big");
    }
}

//ready