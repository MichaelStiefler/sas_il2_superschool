package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class BarracudaMkII extends Barracuda implements TypeBomber {

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
		Class class1 = BarracudaMkII.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Barracuda");
		Property.set(class1, "meshName", "3DO/Plane/BarracudaMkII(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1946.5F);
		Property.set(class1, "FlightModel", "FlightModels/BarracudaMkII.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBarracuda.class, CockpitASVII_standin.class, CockpitBarracuda_TGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 10, 9, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 3, 3, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3 });
		weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev03", "_ExternalBomb03", "_ExternalBomb03", "_ExternalDev04", "_ExternalBomb04",
						"_ExternalBomb04", "_ExternalDev05", "_ExternalBomb05", "_ExternalBomb05", "_ExternalDev06", "_ExternalBomb06", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb08", "_ExternalDev07",
						"_ExternalBomb09", "_ExternalBomb09", "_ExternalDev08", "_ExternalBomb10", "_ExternalBomb10", "_ExternalDev09", "_ExternalBomb11", "_ExternalBomb11", "_ExternalDev10", "_ExternalBomb12", "_ExternalBomb12", "_ExternalDev11",
						"_ExternalBomb13", "_ExternalBomb13", "_ExternalDev12", "_ExternalBomb14", "_ExternalBomb14" });
	}
}
