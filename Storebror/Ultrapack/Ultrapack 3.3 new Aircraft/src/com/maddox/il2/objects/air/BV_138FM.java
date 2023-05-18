package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class BV_138FM extends BV_138xyz implements TypeScout, TypeBomber, TypeSeaPlane {

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 5; i++)
			if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				break;

			case 2:
				this.hierMesh().chunkVisible("Pilot3_D0", false);
				this.hierMesh().chunkVisible("Pilot3_D1", true);
				this.hierMesh().chunkVisible("HMask3_D0", false);
				break;

			case 3:
				this.hierMesh().chunkVisible("Pilot4_D0", false);
				this.hierMesh().chunkVisible("Pilot4_D1", true);
				this.hierMesh().chunkVisible("HMask4_D0", false);
				break;

			case 4:
				this.hierMesh().chunkVisible("Pilot5_D0", false);
				this.hierMesh().chunkVisible("Pilot5_D1", true);
				this.hierMesh().chunkVisible("HMask5_D0", false);
				break;
		}
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 2:
				this.FM.turret[0].bIsOperable = false;
				break;

			case 3:
				this.FM.turret[1].bIsOperable = false;
				break;
		}
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (f < -110F) {
					f = -110F;
					flag = false;
				}
				if (f > 110F) {
					f = 110F;
					flag = false;
				}
				if (f1 < 3F) {
					f1 = 3F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				break;

			case 1:
				if (f < -90F) {
					f = -90F;
					flag = false;
				}
				if (f > 90F) {
					f = 90F;
					flag = false;
				}
				if (f1 < -3F) {
					f1 = -3F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public boolean typeBomberToggleAutomation() {
		return false;
	}

	public void typeBomberAdjDistanceReset() {
	}

	public void typeBomberAdjDistancePlus() {
	}

	public void typeBomberAdjDistanceMinus() {
	}

	public void typeBomberAdjSideslipReset() {
	}

	public void typeBomberAdjSideslipPlus() {
	}

	public void typeBomberAdjSideslipMinus() {
	}

	public void typeBomberAdjAltitudeReset() {
	}

	public void typeBomberAdjAltitudePlus() {
	}

	public void typeBomberAdjAltitudeMinus() {
	}

	public void typeBomberAdjSpeedReset() {
	}

	public void typeBomberAdjSpeedPlus() {
	}

	public void typeBomberAdjSpeedMinus() {
	}

	public void typeBomberUpdate(float f) {
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
	}

	static {
		Class class1 = BV_138FM.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "BV138");
		Property.set(class1, "meshName", "3DO/Plane/BV138B/BV138FM.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1945F);
		Property.set(class1, "FlightModel", "FlightModels/BV138.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBV_138B.class, CockpitBV_138_FGunner.class, CockpitBV_138_TGunner.class });
		Property.set(class1, "LOSElevation", 0.5265F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 9, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_MGUN02", "_ExternalDev01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
	}
}
