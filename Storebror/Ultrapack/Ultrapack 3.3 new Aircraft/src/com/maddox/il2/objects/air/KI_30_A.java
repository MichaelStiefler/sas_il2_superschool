package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class KI_30_A extends KI_30 implements TypeBomber {

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			case 0:
				if (f < -31F) {
					f = -31F;
					flag = false;
				}
				if (f > 31F) {
					f = 31F;
					flag = false;
				}
				if (f1 < -10F) {
					f1 = -10F;
					flag = false;
				}
				if (f1 > 52F) {
					f1 = 52F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void moveCockpitDoor(float f) {
		this.resetYPRmodifier();
		Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.64F);
		this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
			this.setDoorSnd(f);
		}
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
		Class class1 = KI_30_A.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ki30A");
		Property.set(class1, "meshName", "3DO/Plane/Ki-30-A(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "yearService", 1935F);
		Property.set(class1, "yearExpired", 1946.5F);
		Property.set(class1, "FlightModel", "FlightModels/Ki-30-A.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitKI_30_A.class, CockpitKI_30_A_TGun.class });
		Property.set(class1, "LOSElevation", 0.742F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 9, 3, 3, 3, 3, 3, 3, 9, 3, 3, 3, 9, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev04", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalDev03",
				"_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev02", "_ExternalBomb02" });
	}
}
