// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FuelTank_F16Wtank.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTank

public class FuelTank_TankF16W_gn16 extends FuelTank
{

    public FuelTank_TankF16W_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankF16W_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankF16CW_gn16/monoW.sim");
        Property.set(class1, "kalibr", 0.675F);
        Property.set(class1, "massa", 1232F);
		Property.set(class1, "dragCoefficient", 0.35F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}
