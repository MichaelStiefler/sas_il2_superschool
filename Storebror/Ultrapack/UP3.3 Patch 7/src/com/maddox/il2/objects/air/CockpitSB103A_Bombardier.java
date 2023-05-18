package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitSB103A_Bombardier extends CockpitSB103_Bombardier
{

    public CockpitSB103A_Bombardier()
    {
        super("3DO/Cockpit/SB_103A-Bombardier/hier.him");
    }

    static 
    {
        Property.set(CockpitSB103A_Bombardier.class, "normZN", 2.0F);
        Property.set(CockpitSB103A_Bombardier.class, "astatePilotIndx", 1);
        Property.set(CockpitSB103A_Bombardier.class, "aiTuretNum", -2);
        Property.set(CockpitSB103A_Bombardier.class, "weaponControlNum", 3);
    }
}
