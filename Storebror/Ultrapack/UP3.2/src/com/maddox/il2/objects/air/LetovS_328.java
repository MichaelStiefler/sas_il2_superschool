package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class LetovS_328 extends Letov {

	public LetovS_328() {
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
		Class class1 = LetovS_328.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "S-328");
		Property.set(class1, "meshName", "3do/Plane/LetovS-328/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00s());
		Property.set(class1, "meshName_de", "3do/Plane/LetovS-328_DE/hier.him");
		Property.set(class1, "PaintScheme_de", new PaintSchemeBMPar00s());
		Property.set(class1, "meshName_sk", "3do/Plane/LetovS-328_SK/hier.him");
		Property.set(class1, "PaintScheme_sk", new PaintSchemeBMPar00s());
		Property.set(class1, "yearService", 1935F);
		Property.set(class1, "yearExpired", 1950F);
		Property.set(class1, "FlightModel", "FlightModels/LetovS-328.fmd");
		Property.set(class1, "cockpitClass", new Class[] { Cockpit_RanwersLetov.class });
		Property.set(class1, "originCountry", PaintScheme.countrySlovakia);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb18",
						"_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08",
						"_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17" });
	}
}
