package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.GunProperties;

public class MGunHispanoMkISkyFlame extends MGunHispanoMkISkys {

    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.fireMesh = "3DO/Effects/GunFire/20mmFlameHidden/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/20mmFlameHidden/GunFlare.eff";
        gunproperties.smoke = null;
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 5E-013F;
        gunproperties.emitR = 5E-013F;
        gunproperties.emitTime = 0.01F;
        return gunproperties;
    }
}
