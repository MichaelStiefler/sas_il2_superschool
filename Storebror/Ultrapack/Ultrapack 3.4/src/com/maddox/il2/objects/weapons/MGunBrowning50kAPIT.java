package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunBrowning50kAPIT extends MGunBrowning50APIT {

    public MGunBrowning50kAPIT() {
    }

    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bUseHookAsRel = true;
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.shotFreq = 12.5F;
        gunproperties.maxDeltaAngle = 0.229F;
        gunproperties.shotFreqDeviation = 0.08F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmPink/mono.sim";
        gunproperties.bullet[0].traceColor = 0xd29e6bed;
        return gunproperties;
    }
}
