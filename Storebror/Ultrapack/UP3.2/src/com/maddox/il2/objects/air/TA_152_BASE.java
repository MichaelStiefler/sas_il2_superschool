package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;

public class TA_152_BASE extends FW_190D_BASE {

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -77F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -77F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -102F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -102F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 20F * f, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
		float f1 = Math.max(-f * 1500F, -94F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		if (this.FM.CT.getGear() < 0.98f) { return; }
		this.hierMesh().chunkSetAngles("GearC3_D0", 0.0f, -f, 0.0f);
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.44F, 0.0F, 0.44F);
		this.hierMesh().chunkSetLocate("GearL2a_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.44F, 0.0F, 0.44F);
		this.hierMesh().chunkSetLocate("GearR2a_D0", Aircraft.xyz, Aircraft.ypr);
	}

	public void update(float f) {
		for (int i = 13; i < 15; i++) {
			this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, Aircraft.cvt(this.FM.getSpeedKMH(), 200F, 400F, -20F, 0F) * this.kangle, 0.0F);
//			System.out.println("TA_152_BASE Water" + i + "_D0 kangle=" + this.kangle);
		}
		super.update(f);
	}

}
