package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.rts.Property;

public class E13A1 extends E13A {

	public E13A1() {
		this.needsToOpenBombays = true;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (this.thisWeaponsName.startsWith("2x1")) this.needsToOpenBombays = false;
	}

	protected void moveBayDoor(float f) {
		if (!this.needsToOpenBombays) return;
		else {
			this.hierMesh().chunkSetAngles("BDoor1_D0", 0.0F, -64F * f, 0.0F);
			this.hierMesh().chunkSetAngles("BDoor2_D0", 0.0F, -64F * f, 0.0F);
			return;
		}
	}

	public void update(float f) {
		super.update(f);
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 2; j++)
				if (this.FM.Gears.clpGearEff[i][j] != null) {
					tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
					tmpp.z = 0.01D;
					this.FM.Gears.clpGearEff[i][j].pos.setAbs(tmpp);
					this.FM.Gears.clpGearEff[i][j].pos.reset();
				}

	}

	private static Point3d tmpp = new Point3d();
	private boolean        needsToOpenBombays;

	static {
		Class class1 = E13A1.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "E13A");
		Property.set(class1, "meshName", "3DO/Plane/E13A(multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "meshName_ja", "3DO/Plane/E13A(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
		Property.set(class1, "yearService", 1941.5F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/E13A1.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB6N2.class, Cockpit_BombsightOPB.class, CockpitE13_Gunner.class });
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02" });
	}
}
