// US GBU-38 "JDAM" 500lbs GPS+INS Guided Bomb, based on Mk82 LDGP bomb

package com.maddox.il2.objects.weapons;

import com.maddox.rts.*;
import java.io.IOException;


public class BombGBU38_Mk82JDAM_gn16 extends BombUSGPS_JDAM_Generic_gn16
{

    public BombGBU38_Mk82JDAM_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGBU38_Mk82JDAM_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/GBU38_Mk82JDAM_gn16/mono.sim");
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 125F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 262F);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
        })));
 		Property.set(class1, "dragCoefficient", 0.30F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }

}
