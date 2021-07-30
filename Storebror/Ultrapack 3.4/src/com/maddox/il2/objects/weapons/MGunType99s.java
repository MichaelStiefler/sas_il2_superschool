package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunType99s extends MGunMGFFs {
    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.shotFreq = 8.666667F;
        if (gunproperties.bullet.length > 0) {
            gunproperties.bullet[0].massa = 0.129F;
            gunproperties.bullet[0].speed = 600F;
        }
        if (gunproperties.bullet.length > 1) {
            gunproperties.bullet[1].massa = 0.129F;
            gunproperties.bullet[1].speed = 600F;
        }
        if (gunproperties.bullet.length > 2) {
            gunproperties.bullet[2].massa = 0.129F;
            gunproperties.bullet[2].speed = 600F;
        }
        return gunproperties;
    }
}
