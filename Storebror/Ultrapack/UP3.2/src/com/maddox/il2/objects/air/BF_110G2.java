package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class BF_110G2 extends BF_110 {

	public BF_110G2() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.AS.wantBeaconsNet(true);
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			case 0:
				if (f1 < -19F) {
					f1 = -19F;
					flag = false;
				}
				if (f1 > 30F) {
					f1 = 30F;
					flag = false;
				}
				float f2;
				if (f1 < 0.0F) {
					f2 = cvt(f1, -19F, 0.0F, 20F, 30F);
				} else if (f1 < 12F) {
					f2 = cvt(f1, 0.0F, 12F, 30F, 35F);
				} else {
					f2 = cvt(f1, 12F, 30F, 35F, 40F);
				}
				if (f < 0.0F) {
					if (f < -f2) {
						f = -f2;
						flag = false;
					}
				} else if (f > f2) {
					f = f2;
					flag = false;
				}
				if (Math.abs(f) > 17.8F && Math.abs(f) < 25F && f1 < -12F) { flag = false; }
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public static boolean bChangedPit = false;

	static {
		Class class1 = BF_110G2.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Bf-110");
		Property.set(class1, "meshName", "3DO/Plane/Bf-110G-2/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Bf-110G-2.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBF_110G.class, CockpitBF_110G_Gunner.class });
		Property.set(class1, "LOSElevation", 0.66895F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 10, 10, 9, 9, 9, 3, 3, 3, 3, 3, 3, 0, 0, 1, 9, 9, 9, 9, 2, 2, 2, 2, 9, 9, 9, 9 });
		weaponHooksRegister(class1,
				new String[] { "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_CANNON01", "_CANNON02", "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb01",
						"_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_CANNON03", "_CANNON04", "_CANNON05", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock01",
						"_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
	}
}
