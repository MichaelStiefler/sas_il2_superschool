// Last Modified by: western0221 2019-10-28

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTank

public class FuelTank_MilInternal450L_gn16 extends FuelTank
{

    public FuelTank_MilInternal450L_gn16()
    {
    }


    static 
    {
        Class var_class = com.maddox.il2.objects.weapons.FuelTank_MilInternal450L_gn16.class;
        Property.set(var_class, "mesh", "3DO/Arms/InternalFuelTank_MilHeli_gn16/mono.sim");
        Property.set(var_class, "kalibr", 0.80F);
        Property.set(var_class, "massa", 430F);
		Property.set(var_class, "dragCoefficient", 0.00001F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}
