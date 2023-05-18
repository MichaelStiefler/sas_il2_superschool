
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTank_TankF14L_gn16 extends FuelTank
{

    public FuelTank_TankF14L_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankF14L_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankF14LR_gn16/monoL.sim");
        Property.set(class1, "kalibr", 1.5F);
        Property.set(class1, "massa", 800F);
		Property.set(class1, "dragCoefficient", 0.30F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}