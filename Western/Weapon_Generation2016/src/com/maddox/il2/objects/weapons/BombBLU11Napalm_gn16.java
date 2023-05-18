
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;


public class BombBLU11Napalm_gn16 extends BombNapalmGeneric_gn16
{

    public BombBLU11Napalm_gn16()
    {
        napalmFilledKg = 200F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombBLU11Napalm_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/BLU11_US_napalm_gn16/mono.sim");
        Property.set(class1, "radius", 20.0F);
        Property.set(class1, "power", 120F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.470F);
        Property.set(class1, "massa", 230F);
        Property.set(class1, "sound", "weapon.bomb_std");
 		Property.set(class1, "dragCoefficient", 0.40F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}
