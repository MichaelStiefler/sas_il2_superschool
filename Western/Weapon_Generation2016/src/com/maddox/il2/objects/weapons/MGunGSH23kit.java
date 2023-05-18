// "Turret" version of MGunGSH23ki
// By western0221 on 13th/Oct./2019

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;


public class MGunGSH23kit extends MGunGSH23ki
{

    public MGunGSH23kit()
    {
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bUseHookAsRel = false;
        gunproperties.maxDeltaAngle = 0.30F;
        return gunproperties;
    }
}
