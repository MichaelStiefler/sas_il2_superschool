package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunM24 extends MGunHispanoMKVk {

    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bUseHookAsRel = false;
        gunproperties.shells = null;
        gunproperties.shotFreq = 12.75F;
        gunproperties.maxDeltaAngle = 0.28F;
        gunproperties.shotFreqDeviation = 0.03F;
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMaxDist = 3000.0F;
        gunproperties.bullet[0].timeLife = 3.0F;
        gunproperties.bullet[1].timeLife = 3.0F;
        return gunproperties;
    }
}
