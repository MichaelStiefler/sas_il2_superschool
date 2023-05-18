package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunM61A2 extends MGunM61A1 {

    public GunProperties createProperties()
    {
    	GunProperties gunproperties = super.createProperties();
        gunproperties.bullet[0].massa = 0.1024F;
        gunproperties.bullet[0].speed = 1050F;
        gunproperties.bullet[0].power = 0.0105F;
        gunproperties.bullet[0].powerType = 1;
        gunproperties.bullet[0].powerRadius = 0.3F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        return gunproperties;
    }
}
