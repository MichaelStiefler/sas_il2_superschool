package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitPE2_244_TGunner extends CockpitPE2_402_TGunner {

	public CockpitPE2_244_TGunner() {
		super("3DO/Cockpit/Pe-2series402-TGun/hier-244.him");
		this.mesh.materialReplace("Trans_VIII", "Trans_VIII_110-274");
		this.setNightMats(false);
	}

	public void PrivateDevices() {
		this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 300F, 0.0F, -73F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 300F, 0.0F, -73F), 0.0F, 0.0F);
	}

	static {
		Property.set(CockpitPE2_244_TGunner.class, "aiTuretNum", 0);
		Property.set(CockpitPE2_244_TGunner.class, "weaponControlNum", 10);
		Property.set(CockpitPE2_244_TGunner.class, "astatePilotIndx", 1);
	}
}
