
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombB28EXnuke70kt_gn16 extends Bomb
{

    public BombB28EXnuke70kt_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombB28EXnuke70kt_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/USnukeB28_gn16/monoEX.sim");
        Property.set(class1, "radius", 11000F);
        Property.set(class1, "power", 70000000F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.58F);
        Property.set(class1, "massa", 860.0F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_USnukeDelay.class, com.maddox.il2.objects.weapons.Fuze_USnukeLowAlt.class, com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_USnukeAir.class
        })));
		Property.set(class1, "dragCoefficient", 0.29F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}