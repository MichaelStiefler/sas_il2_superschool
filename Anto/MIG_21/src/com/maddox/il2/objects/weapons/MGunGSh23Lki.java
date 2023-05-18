package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunGSh23Lki extends MGunGSh23Ls{
    public GunProperties createProperties() {
    	GunProperties gunproperties = super.createProperties();
    	gunproperties.shells = null;
    	return gunproperties;
        }
}
