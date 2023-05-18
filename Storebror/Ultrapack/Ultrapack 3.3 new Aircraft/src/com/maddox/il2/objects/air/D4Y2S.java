package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.objects.weapons.BombGun500kgJ;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class D4Y2S extends D4YS implements TypeStormovik, TypeDiveBomber {

	public D4Y2S() {
		this.flapps = 0.0F;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0] instanceof BombGun500kgJ) {
			this.hierMesh().chunkVisible("Bay01_D0", false);
			this.hierMesh().chunkVisible("Bay02_D0", false);
		}
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			case 0:
				if (f < -33F) {
					f = -33F;
					flag = false;
				}
				if (f > 33F) {
					f = 33F;
					flag = false;
				}
				if (f1 < -3F) {
					f1 = -3F;
					flag = false;
				}
				if (f1 > 62F) {
					f1 = 62F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public boolean typeDiveBomberToggleAutomation() {
		return false;
	}

	public void typeDiveBomberAdjAltitudeReset() {
	}

	public void typeDiveBomberAdjAltitudePlus() {
	}

	public void typeDiveBomberAdjAltitudeMinus() {
	}

	public void typeDiveBomberAdjVelocityReset() {
	}

	public void typeDiveBomberAdjVelocityPlus() {
	}

	public void typeDiveBomberAdjVelocityMinus() {
	}

	public void typeDiveBomberAdjDiveAngleReset() {
	}

	public void typeDiveBomberAdjDiveAnglePlus() {
	}

	public void typeDiveBomberAdjDiveAngleMinus() {
	}

	public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
	}

	public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
	}

	public void update(float f) {
		super.update(f);
		float f1 = this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(this.flapps - f1) > 0.01F) {
			this.flapps = f1;
			this.hierMesh().chunkSetAngles("Oil_D0", 0.0F, -22F * f1, 0.0F);
			for (int i = 1; i < 9; i++)
				this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f1, 0.0F);

		}
	}

	private float flapps;

	static {
		Class class1 = D4Y2S.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "D4Y");
		Property.set(class1, "meshName", "3DO/Plane/D4Y2S(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "meshName_ja", "3DO/Plane/D4Y2S(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1946.5F);
		Property.set(class1, "FlightModel", "FlightModels/D4Y2S.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitD4Y2S.class, CockpitD4Y2S_TGunner.class });
		Property.set(class1, "LOSElevation", 1.1058F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 9, 9, 3, 9, 9, 1 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn02", "_BombSpawn03", "_BombSpawn01", "_ExternalDev02", "_ExternalDev03", "_BombSpawn04", "_ExternalDev04", "_ExternalDev05", "_CANNON01" });
	}
}
