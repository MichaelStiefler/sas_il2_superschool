
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;


public class BombBLU32Napalm_gn16 extends BombNapalmBGeneric_gn16
{

    public BombBLU32Napalm_gn16()
    {
        napalmbFilledKg = 240F;
    }


    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombBLU32Napalm_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/BLU32_US_napalm_gn16/mono.sim");
        Property.set(class1, "radius", 70.0F);
        Property.set(class1, "power", 110F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.40F);
        Property.set(class1, "massa", 200F);
        Property.set(class1, "sound", "weapon.bomb_std");
 		Property.set(class1, "dragCoefficient", 0.30F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}
