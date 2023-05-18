package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunMauser98t extends MGunMauser98s {

    public MGunMauser98t() {
    }

    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bUseHookAsRel = false;
        gunproperties.shells = null;
        gunproperties.maxDeltaAngle = 0.25F;
        return gunproperties;
    }
}
