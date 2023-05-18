// Last Modified by: western0221 2019-10-20

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTank

public class FuelTank_Mi24PTB450L_gn16 extends FuelTank
{

    public FuelTank_Mi24PTB450L_gn16()
    {
    }


    static 
    {
        Class var_class = com.maddox.il2.objects.weapons.FuelTank_Mi24PTB450L_gn16.class;
        Property.set(var_class, "mesh", "3DO/Arms/PTB450L_Mi24_gn16/mono.sim");
        Property.set(var_class, "kalibr", 0.548F);
        Property.set(var_class, "massa", 430F);
		Property.set(var_class, "dragCoefficient", 0.90F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}
