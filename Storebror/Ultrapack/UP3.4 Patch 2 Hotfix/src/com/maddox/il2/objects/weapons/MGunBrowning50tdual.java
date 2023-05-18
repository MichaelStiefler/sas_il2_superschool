package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunBrowning50tdual extends MGunBrowning50s
{
    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bUseHookAsRel = false;
        gunproperties.shells = null;
        gunproperties.shotFreq = 25F;
        gunproperties.bulletsCluster *= 2;
        gunproperties.weaponType = 1;
        gunproperties.sound = "weapon.MGunBrowning50s";
        gunproperties.maxDeltaAngle = 0.229F;
        return gunproperties;
    }
}
