package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class WellesleyMKI extends Scheme1 implements TypeScout, TypeBomber, TypeStormovik {

	public void doKillPilot(int i) {
		switch (i) {
			case 1:
				this.FM.turret[0].bIsOperable = false;
				break;
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

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
			this.hierMesh().chunkVisible("HMask2_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
			this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
		}
	}

	protected void moveBayDoor(float f) {
		this.hierMesh().chunkSetAngles("Bay1_D0", 5F * f, -95F * f, 0F);
		this.hierMesh().chunkSetAngles("Bay2_D0", -5F * f, -95F * f, 0F);
		this.hierMesh().chunkSetAngles("Bay1b_D0", -5F * f, -95F * f, 0F);
		this.hierMesh().chunkSetAngles("Bay2b_D0", 5F * f, -95F * f, 0F);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		if (af[0] < -142F) {
			af[0] = -142F;
			flag = false;
		} else if (af[0] > 142F) {
			af[0] = 142F;
			flag = false;
		}
		if (af[1] > 45F) {
			af[1] = 45F;
			flag = false;
		}
		if (!flag) return false;
		float f = Math.abs(af[0]);
		if (f < 2.5F && af[1] < 20.8F) {
			af[1] = 20.8F;
			return false;
		}
		if (f < 21F && af[1] < 16.1F) {
			af[1] = 16.1F;
			return false;
		}
		if (f < 41F && af[1] < -8.5F) {
			af[1] = -8.5F;
			return false;
		}
		if (f < 103F && af[1] < -45F) {
			af[1] = -45F;
			return false;
		}
		if (f < 180F && af[1] < -7.8F) {
			af[1] = -7.8F;
			return false;
		} else return true;
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
		Class class1 = WellesleyMKI.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Wellesley");
		Property.set(class1, "meshName", "3DO/Plane/WellesleyMKI/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/Wellesley.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitWellesley.class, CockpitWellesley_TGunner.class });
		Property.set(class1, "LOSElevation", 0.73425F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02" });
	}
}
