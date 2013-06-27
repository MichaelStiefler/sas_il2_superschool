// This file is part of the SAS IL-2 Sturmovik 1946 4.12
// Flyable AI Aircraft Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
//              www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited at: 2013/02/05

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class G_11 extends Scheme0 implements TypeGlider, TypeTransport, TypeBomber {

	public G_11() {
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			hierMesh().chunkVisible("Head1_D0", false);
			break;
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
		Class class1 = G_11.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "G-11");
		Property.set(class1, "meshName", "3DO/Plane/G-11/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
		Property.set(class1, "yearService", 1936F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/G-11.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitG_11.class });
		Property.set(class1, "gliderString", "3DO/Arms/TowString/mono.sim");
		Property.set(class1, "gliderStringLength", 160F);
		Property.set(class1, "gliderStringKx", 30F);
		Property.set(class1, "gliderStringFactor", 1.8F);
		weaponTriggersRegister(class1, new int[] { 3 });
		weaponHooksRegister(class1, new String[] { "_BombSpawn01" });
		weaponsRegister(class1, "default", new String[] { null });
		weaponsRegister(class1, "none", new String[] { null });
	}
}
