
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTank

public class FuelTank_TankF105_450gal_gn16 extends FuelTank
{

    public FuelTank_TankF105_450gal_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankF105_450gal_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankF105_450gal_gn16/mono.sim");
        Property.set(class1, "kalibr", 1.5F);
        Property.set(class1, "massa", 1550F);
		Property.set(class1, "dragCoefficient", 0.35F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}
