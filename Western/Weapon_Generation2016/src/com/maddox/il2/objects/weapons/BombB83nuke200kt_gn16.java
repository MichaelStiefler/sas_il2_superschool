
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;


public class BombB83nuke200kt_gn16 extends Bomb
{

    public BombB83nuke200kt_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombB83nuke200kt_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/USnukeB83_gn16/mono.sim");
        Property.set(class1, "radius", 18000F);
        Property.set(class1, "power", 200000000F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.46F);
        Property.set(class1, "massa", 1100.0F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_USnukeDelay.class, com.maddox.il2.objects.weapons.Fuze_USnukeLowAlt.class, com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_USnukeAir.class
        })));
 		Property.set(class1, "dragCoefficient", 0.32F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
   }
}