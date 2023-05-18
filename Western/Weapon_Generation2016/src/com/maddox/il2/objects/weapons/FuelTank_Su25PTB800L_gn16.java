// Last Modified by: western0221 2019-01-01

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTank

public class FuelTank_Su25PTB800L_gn16 extends FuelTank
{

    public FuelTank_Su25PTB800L_gn16()
    {
    }


    static 
    {
        Class var_class = com.maddox.il2.objects.weapons.FuelTank_Su25PTB800L_gn16.class;
        Property.set(var_class, "mesh", "3DO/Arms/PTB800L_Su25_gn16/mono.sim");
        Property.set(var_class, "kalibr", 0.5F);
        Property.set(var_class, "massa", 800F);
		Property.set(var_class, "dragCoefficient", 0.30F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}
