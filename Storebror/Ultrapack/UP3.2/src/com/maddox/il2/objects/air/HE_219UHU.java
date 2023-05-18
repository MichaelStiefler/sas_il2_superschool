package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class HE_219UHU extends HE_219 {

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xxcannon01")) {
			this.debuggunnery("Armament System: Left Wing Cannon: Disabled..");
			this.FM.AS.setJamBullets(1, 1);
			this.getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
			return;
		}
		if (s.startsWith("xxcannon02")) {
			this.debuggunnery("Armament System: Right Wing Cannon: Disabled..");
			this.FM.AS.setJamBullets(1, 2);
			this.getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
			return;
		} else {
			super.hitBone(s, shot, point3d);
			return;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("Head1_D0", false);
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				break;
		}
	}

	static {
		Class class1 = HE_219UHU.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "He219UHU");
		Property.set(class1, "meshName", "3DO/Plane/He-219UHU/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/He-219.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitHE_219.class });
		Property.set(class1, "LOSElevation", 1.00705F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 2, 2, 1, 1, 1, 1, 9, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev01", "_ExternalDev02" });
	}
}
