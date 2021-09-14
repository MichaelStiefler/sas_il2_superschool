package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunMadsen20i extends MGunMadsen20 {

    public MGunMadsen20i() {
    }

    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.shells = null;
        return gunproperties;
    }
}
