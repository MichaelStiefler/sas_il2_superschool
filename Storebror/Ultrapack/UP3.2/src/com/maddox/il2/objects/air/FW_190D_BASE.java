package com.maddox.il2.objects.air;

public class FW_190D_BASE extends FW_190A_BASE {

	public FW_190D_BASE() {
		this.applyLoadoutVisibility = false;
		this.kangle = 0.0F;
	}

	public void update(float f) {
		for (int i = 1; i < 13; i++) {
			this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, Aircraft.cvt(this.FM.getSpeedKMH(), 200F, 400F, -20F, 0F) * this.kangle, 0.0F);
//			System.out.println("FW_190D_BASE Water" + i + "_D0 kangle=" + this.kangle);
		}

		this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
		super.update(f);
	}

	float kangle;
}
