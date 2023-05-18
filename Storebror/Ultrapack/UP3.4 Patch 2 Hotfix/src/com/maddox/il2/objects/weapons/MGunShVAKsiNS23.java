package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunShVAKsiNS23 extends MGunShVAKs
{
    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bUseHookAsRel = false;
        gunproperties.shells = null;
        gunproperties.shotFreq = 13.33333F;
        gunproperties.maxDeltaAngle = 0.12F;
        gunproperties.sound = "weapon.MGunNS23s";
        return gunproperties;
    }
}
