package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;

public abstract class YAK_9TX extends YAK implements TypeStormovik {

	public YAK_9TX() {
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		float f1 = Math.max(-f * 1500F, -80F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 80F * f, 0.0F);
		f1 = Math.max(-f * 1500F, -60F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 82.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 82.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -85F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -85F * f, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
	}

	public void update(float f) {
		this.hierMesh().chunkSetAngles("Wind_luk", 0.0F, 12F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		this.hierMesh().chunkSetAngles("Water_luk", 0.0F, 12F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		super.update(f);
	}

}
