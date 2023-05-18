
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombB57nuke5kt_gn16 extends Bomb
{

    public BombB57nuke5kt_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombB57nuke5kt_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/USnukeB57_gn16/mono.sim");
        Property.set(class1, "radius", 5000F);
        Property.set(class1, "power", 5000000F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.375F);
        Property.set(class1, "massa", 227.0F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_USnukeDelay.class, com.maddox.il2.objects.weapons.Fuze_USnukeLowAlt.class, com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_USnukeAir.class
        })));
 		Property.set(class1, "dragCoefficient", 0.24F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
   }
}