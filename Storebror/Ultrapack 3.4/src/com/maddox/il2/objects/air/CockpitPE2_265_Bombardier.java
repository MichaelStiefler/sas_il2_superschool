package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitPE2_265_Bombardier extends CockpitPE2_402_Bombardier {

    public CockpitPE2_265_Bombardier() {
        super("3DO/Cockpit/Pe-2series402-Bombardier/hier-265.him");
        this.mesh.materialReplace("Trans_VIII", "Trans_VIII_110-274");
        this.setNightMats(false);
    }

    static {
        Property.set(CockpitPE2_265_Bombardier.class, "astatePilotIndx", 1);
    }
}
