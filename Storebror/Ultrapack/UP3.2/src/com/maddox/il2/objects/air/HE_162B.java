package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class HE_162B extends HE_162 {

	public HE_162B() {
	}

	protected void moveRudder(float f) {
		this.resetYPRmodifier();
		xyz[1] = cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, 0.0632F);
		if (this.FM.CT.getGear() > 0.99F) { ypr[1] = 40F * this.FM.CT.getRudder(); }
		this.hierMesh().chunkSetLocate("GearC25_D0", xyz, ypr);
		this.hierMesh().chunkSetAngles("GearC27_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, -15F), 0.0F);
		this.hierMesh().chunkSetAngles("GearC28_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, 30F), 0.0F);
		this.updateControlsVisuals();
	}

	protected void moveElevator(float f) {
		this.updateControlsVisuals();
	}

	private final void updateControlsVisuals() {
		this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -21.25F * this.FM.CT.getElevator() - 21.25F * this.FM.CT.getRudder(), 0.0F);
		this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -21.25F * this.FM.CT.getElevator() + 21.25F * this.FM.CT.getRudder(), 0.0F);
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 33:
				return super.cutFM(34, j, actor);

			case 36:
				return super.cutFM(37, j, actor);

			case 17:
				return super.cutFM(11, j, actor);

			case 18:
				return super.cutFM(12, j, actor);
		}
		return super.cutFM(i, j, actor);
	}

	static {
		Class class1 = HE_162B.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "He-162");
		Property.set(class1, "meshName", "3DO/Plane/He-162B/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1946F);
		Property.set(class1, "yearExpired", 1956F);
		Property.set(class1, "FlightModel", "FlightModels/He-162B.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitHE_162A2.class });
		Property.set(class1, "LOSElevation", 0.5099F);
		weaponTriggersRegister(class1, new int[] { 0, 0 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
	}
}
