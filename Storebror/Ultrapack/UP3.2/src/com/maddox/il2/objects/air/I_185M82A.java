package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.BombFAB100;
import com.maddox.il2.objects.weapons.BombFAB250;
import com.maddox.rts.Property;

public class I_185M82A extends I_185 {

	public I_185M82A() {
	}

	public void update(float f) {
		this.hierMesh().chunkSetAngles("Water1_D0", 0.0F, -20F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		for (int i = 1; i < 5; i++) {
			this.hierMesh().chunkSetAngles("Oil" + i + "_D0", 0.0F, -15F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		}

		super.update(f);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -65F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, cvt(f, 0.02F, 0.1F, 0.0F, -65F), 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f, 0.02F, 0.1F, 0.0F, -65F), 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 87F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.02F, 0.1F, 0.0F, -85F), 0.0F);
		hiermesh.chunkSetAngles("GearL8_D0", 0.0F, -90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 87F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f, 0.02F, 0.1F, 0.0F, -85F), 0.0F);
		hiermesh.chunkSetAngles("GearR8_D0", 0.0F, -90F * f, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		Object aobj[] = this.pos.getBaseAttached();
		if (aobj == null) { return; }
		for (int i = 0; i < aobj.length; i++) {
			if (aobj[i] instanceof BombFAB100) {
				this.hierMesh().chunkVisible("RackL1_D0", true);
				this.hierMesh().chunkVisible("RackR1_D0", true);
				return;
			}
			if (aobj[i] instanceof BombFAB250) {
				this.hierMesh().chunkVisible("RackL2_D0", true);
				this.hierMesh().chunkVisible("RackR2_D0", true);
				return;
			}
		}

	}

	static {
		Class class1 = I_185M82A.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "I-185");
		Property.set(class1, "meshName", "3DO/Plane/I-185(M-82A)(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1945F);
		Property.set(class1, "FlightModel", "FlightModels/I-185M-82A.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitI_185M82.class });
		Property.set(class1, "LOSElevation", 0.89135F);
		weaponTriggersRegister(class1, new int[] { 1, 1, 1, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02",
				"_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
	}
}
