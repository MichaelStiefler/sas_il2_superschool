// US GBU-31 "JDAM" 2000lbs GPS+INS Guided Bomb, based on Mk84 LDGP bomb

package com.maddox.il2.objects.weapons;

import com.maddox.rts.*;
import java.io.IOException;


public class BombGBU31_Mk84JDAM_gn16 extends BombUSGPS_JDAM_Generic_gn16
{

    public BombGBU31_Mk84JDAM_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGBU31_Mk84JDAM_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/GBU31_Mk84JDAM_gn16/mono.sim");
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "radius", 400.65F);
        Property.set(class1, "power", 428.644F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.457F);
        Property.set(class1, "massa", 944F);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
        })));
 		Property.set(class1, "dragCoefficient", 0.30F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }

}
