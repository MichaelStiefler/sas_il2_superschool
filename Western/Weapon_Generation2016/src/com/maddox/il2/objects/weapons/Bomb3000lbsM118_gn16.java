package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb3000lbsM118_gn16 extends Bomb
{

    public Bomb3000lbsM118_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Bomb3000lbsM118_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/3000lbsBombM118_gn16/mono.sim");
        Property.set(class1, "radius", 250F);
        Property.set(class1, "power", 895F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 1383F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
        })));
		Property.set(class1, "dragCoefficient", 0.32F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}