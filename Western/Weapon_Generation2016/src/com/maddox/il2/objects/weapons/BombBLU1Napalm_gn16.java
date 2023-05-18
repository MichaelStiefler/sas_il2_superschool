
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;


public class BombBLU1Napalm_gn16 extends BombNapalmGeneric_gn16
{

    public BombBLU1Napalm_gn16()
    {
        napalmFilledKg = 340F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombBLU1Napalm_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/BLU1_US_napalm_gn16/mono.sim");
        Property.set(class1, "radius", 90.0F);
        Property.set(class1, "power", 204F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.47F);
        Property.set(class1, "massa", 400F);
        Property.set(class1, "sound", "weapon.bomb_std");
 		Property.set(class1, "dragCoefficient", 0.32F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}
