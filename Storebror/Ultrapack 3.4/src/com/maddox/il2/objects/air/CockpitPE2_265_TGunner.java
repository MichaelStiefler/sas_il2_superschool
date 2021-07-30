package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitPE2_265_TGunner extends CockpitPE2_402_TGunner {

    public CockpitPE2_265_TGunner() {
        super("3DO/Cockpit/Pe-2series402-TGun/hier-265.him");
        this.mesh.materialReplace("Trans_VIII", "Trans_VIII_110-274");
        this.setNightMats(false);
    }

    static {
        Property.set(CockpitPE2_265_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitPE2_265_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitPE2_265_TGunner.class, "astatePilotIndx", 1);
    }
}
