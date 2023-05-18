package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SBD_DAUNTLESS_1 extends SBD implements TypeStormovik, TypeDiveBomber {

	public SBD_DAUNTLESS_1() {
		this.flapps = 0.0F;
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			case 0:
				if (f < -135F) f = -135F;
				if (f > 135F) f = 135F;
				if (f1 < -69F) {
					f1 = -69F;
					flag = false;
				}
				if (f1 > 45F) {
					f1 = 45F;
					flag = false;
				}
				float f2;
				for (f2 = Math.abs(f); f2 > 180F; f2 -= 180F)
					;
				if (f1 < -Aircraft.floatindex(Aircraft.cvt(f2, 0.0F, 180F, 0.0F, 36F), af)) f1 = -Aircraft.floatindex(Aircraft.cvt(f2, 0.0F, 180F, 0.0F, 36F), af);
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
			for (int i = 1; i < 3; i++)
				this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f1, 0.0F);

		}
	}

	private float flapps;

	static {
		Class class1 = SBD_DAUNTLESS_1.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Dauntless");
		Property.set(class1, "originCountry", PaintScheme.countryBritain);
		Property.set(class1, "meshName", "3DO/Plane/SBD-5(GB)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "meshName_rz", "3DO/Plane/SBD-5(RZ)/hier.him");
		Property.set(class1, "PaintScheme_rz", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1946.5F);
		Property.set(class1, "FlightModel", "FlightModels/SBD-5.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitSBD5.class, CockpitSBD3_TGunner.class });
		Property.set(class1, "LOSElevation", 1.1058F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01" });
	}
}
