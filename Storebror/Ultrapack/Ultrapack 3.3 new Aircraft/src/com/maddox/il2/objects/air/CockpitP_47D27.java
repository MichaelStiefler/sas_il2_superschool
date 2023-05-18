package com.maddox.il2.objects.air;

public class CockpitP_47D27 extends CockpitP_47DModPack {
	public CockpitP_47D27() {
		super("3DO/Cockpit/P-47D-25/hier_D27.him");
	}

	public void reflectWorldToInstruments(float f) {
		super.reflectWorldToInstruments(f);
		this.mesh.chunkSetAngles("supercharge", 0.0F, this.cvt(this.setNew.supercharge, 0.3F, 0.75F, -2F, 44F), 0.0F);
		this.mesh.chunkSetAngles("throtle", 0.0F, 0.0F, this.cvt(this.setNew.throttle, 0.0F, 1.1F, -6F, -68F));
		this.mesh.chunkSetAngles("zManifold1a", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 240F), 0.0F);
	}
}
