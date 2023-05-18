
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTank_TankF16C_gn16 extends FuelTank
{

    public FuelTank_TankF16C_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankF16C_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankF16CW_gn16/monoC.sim");
        Property.set(class1, "kalibr", 0.7F);   // in detailed, 0.6F height and 0.8F width
        Property.set(class1, "massa", 1007F);
		Property.set(class1, "dragCoefficient", 0.35F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}
