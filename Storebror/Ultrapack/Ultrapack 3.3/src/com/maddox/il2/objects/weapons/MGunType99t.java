package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunType99t extends MGunType99s {
    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bUseHookAsRel = false;
        gunproperties.shells = null;
        return gunproperties;
    }
}
