package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitPE2_244 extends CockpitPE2_402 {

    public CockpitPE2_244() {
        super("3DO/Cockpit/Pe-2series402/hier-244.him");
    }

    public void PrivateDevices() {
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 300F, 0.0F, -73F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 300F, 0.0F, -73F), 0.0F, 0.0F);
    }

    static {
        Property.set(CockpitPE2_244.class, "normZNs", new float[] { 1.45F, 0.89F, 1.1F, 0.89F });
    }
}
