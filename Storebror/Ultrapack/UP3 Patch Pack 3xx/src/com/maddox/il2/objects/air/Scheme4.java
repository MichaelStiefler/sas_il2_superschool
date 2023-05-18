/*Here only because of obfuscation reasons.*/
package com.maddox.il2.objects.air;

public abstract class Scheme4 extends AircraftLH {
	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30.0F * f, 0.0F);
	}

	protected void moveElevator(float f) {
		this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30.0F * f, 0.0F);
		this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30.0F * f, 0.0F);
	}

	protected void moveAileron(float f) {
		this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30.0F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30.0F * f, 0.0F);
	}

	protected void moveFlap(float f) {
		float f_0_ = -70.0F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f_0_, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f_0_, 0.0F);
		this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f_0_, 0.0F);
		this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f_0_, 0.0F);
	}
}