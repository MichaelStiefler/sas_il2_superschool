package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitPE2_244_Bombardier extends CockpitPE2_402_Bombardier {

    public CockpitPE2_244_Bombardier() {
        super("3DO/Cockpit/Pe-2series402-Bombardier/hier-244.him");
        this.mesh.materialReplace("Trans_VIII", "Trans_VIII_110-274");
        this.setNightMats(false);
    }

    public void PrivateDevices() {
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 300F, 0.0F, -73F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 300F, 0.0F, -73F), 0.0F, 0.0F);
    }

    static {
        Property.set(CockpitPE2_244_Bombardier.class, "astatePilotIndx", 1);
    }
}
