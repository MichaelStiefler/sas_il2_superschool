package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class N1K1JA extends N1K {

	public N1K1JA() {
	}

	public void update(float f) {
		super.update(f);
		float f1 = this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(this.flapps - f1) > 0.01F) {
			this.flapps = f1;
			for (int i = 1; i < 11; i++) {
				this.hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, -20F * f1, 0.0F);
			}

		}
	}

	public void moveCockpitDoor(float f) {
		this.resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, 0.61F);
		this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) { Main3D.cur3D().cockpits[0].onDoorMoved(f); }
			this.setDoorSnd(f);
		}
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (this.FM.CT.Weapons[3] != null) {
			this.hierMesh().chunkVisible("RackL_D0", true);
			this.hierMesh().chunkVisible("RackR_D0", true);
		}
	}

	static {
		Class class1 = N1K1JA.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "N1K");
		Property.set(class1, "meshName", "3DO/Plane/N1K1-Ja(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "meshName_ja", "3DO/Plane/N1K1-Ja(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar01());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/N1K1-J.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitN1K2JA.class });
		Property.set(class1, "LOSElevation", 1.01885F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 3, 3, 9, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
	}
}
