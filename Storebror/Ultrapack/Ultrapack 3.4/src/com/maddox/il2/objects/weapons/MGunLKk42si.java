package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunLKk42si extends MGunLKk42s {
    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bUseHookAsRel = true;
        gunproperties.shells = null;
        gunproperties.shotFreq = 9F;
        gunproperties.maxDeltaAngle = 0.17F;
        return gunproperties;
    }
}
