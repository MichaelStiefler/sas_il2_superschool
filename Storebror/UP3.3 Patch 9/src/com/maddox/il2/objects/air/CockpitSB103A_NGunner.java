package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitSB103A_NGunner extends CockpitSB103_NGunner
{

    public CockpitSB103A_NGunner()
    {
        super("3DO/Cockpit/SB_103A-NGunner/hier.him");
    }

    static 
    {
        Property.set(CockpitSB103A_NGunner.class, "aiTuretNum", 0);
        Property.set(CockpitSB103A_NGunner.class, "weaponControlNum", 10);
        Property.set(CockpitSB103A_NGunner.class, "astatePilotIndx", 1);
        Property.set(CockpitSB103A_NGunner.class, "normZN", 1.1F);
        Property.set(CockpitSB103A_NGunner.class, "gsZN", 1.1F);
    }
}
