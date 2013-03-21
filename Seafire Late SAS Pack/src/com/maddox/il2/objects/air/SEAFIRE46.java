// This file is part of the SAS IL-2 Sturmovik 1946
// Late Seafire Mod package.
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
// Last Edited at: 2013/03/17

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class SEAFIRE46 extends SeafireLateBubbleTopCanopy {

	public SEAFIRE46() {
	}

	protected void moveFan(float f) {
		hierMesh().chunkFind(Aircraft.Props[1][0]);
		int i = 0;
		for (int j = 0; j < 2; j++) {
			if (oldProp[j] < 2) {
				i = Math.abs((int) (FM.EI.engines[0].getw() * 0.06F));
				if (i >= 1)
					i = 1;
				if (i != oldProp[j] && hierMesh().isChunkVisible(Aircraft.Props[j][oldProp[j]])) {
					hierMesh().chunkVisible(Aircraft.Props[j][oldProp[j]], false);
					oldProp[j] = i;
					hierMesh().chunkVisible(Aircraft.Props[j][i], true);
				}
			}
			if (i == 0) {
				propPos[j] = (propPos[j] + 57.3F * FM.EI.engines[0].getw() * f) % 360F;
			} else {
				float f1 = 57.3F * FM.EI.engines[0].getw();
				f1 %= 2880F;
				f1 /= 2880F;
				if (f1 <= 0.5F)
					f1 *= 2.0F;
				else
					f1 = f1 * 2.0F - 2.0F;
				f1 *= 1200F;
				propPos[j] = (propPos[j] + f1 * f) % 360F;
			}
			if (j == 0)
				hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, propPos[j], 0.0F);
			else
				hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, -propPos[j], 0.0F);
		}
	}

	public void hitProp(int i, int j, Actor actor) {
		if (i > FM.EI.getNum() - 1 || oldProp[i] == 2)
			return;
		if (isChunkAnyDamageVisible("Prop" + (i + 1)) || isChunkAnyDamageVisible("PropRot" + (i + 1))) {
			hierMesh().chunkVisible(Aircraft.Props[i + 1][0], false);
			hierMesh().chunkVisible(Aircraft.Props[i + 1][1], false);
			hierMesh().chunkVisible(Aircraft.Props[i + 1][2], true);
		}
		super.hitProp(i, j, actor);
	}

	public void update(float f) {
		World.cur().diffCur.Torque_N_Gyro_Effects = false;
		super.update(f);
	}
	
	static {
		Class class1 = SEAFIRE46.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Spit");
		Property.set(class1, "meshName", "3DO/Plane/SeafireMk46_SAS(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1946.5F);
		Property.set(class1, "FlightModel", "FlightModels/Seafire46.fmd:SeafireLate_FM");
		Property.set(class1, "cockpitClass", new Class[] { CockpitSeafireLate.class });
		Property.set(class1, "LOSElevation", 0.5926F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 3, 9, 9, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03",
				"_ExternalBomb03", "_ExternalBomb01", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11",
				"_ExternalDev12", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08",
				"_ExternalDev13" });
		Aircraft.weaponsRegister(class1, "default", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "90gal_dt", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "PylonSpitC 1", null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "FuelTankGun_TankSF17 1" });
		Aircraft.weaponsRegister(class1, "1x250lb", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "PylonSpitC 1", null, null,
				null, null, "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "1x500lb", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "PylonSpitC 1", null, null,
				null, null, "BombGun500lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "2x250lb", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", null, "3 BombGun250lbsE 1",
				"PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "2x500lb", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", null, "3 BombGun500lbsE 1",
				"PylonSpitL 1", "PylonSpitR 1", "BombGun500lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "90gal_dt+2x250lb", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "PylonSpitC 1",
				"3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"FuelTankGun_TankSF17 1" });
		Aircraft.weaponsRegister(class1, "90gal_dt+2x500lb", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "PylonSpitC 1",
				"3 BombGun500lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun500lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"FuelTankGun_TankSF17 1" });
		Aircraft.weaponsRegister(class1, "3x250lb", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "PylonSpitC 1",
				"3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null });
		Aircraft.weaponsRegister(class1, "1x500lb+2x250lb", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "PylonSpitC 1",
				"3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", "BombGun500lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null });
		Aircraft.weaponsRegister(class1, "1x250lb+2x500lb", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "PylonSpitC 1",
				"3 BombGun500lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun500lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null });
		Aircraft.weaponsRegister(class1, "3x500lb", new String[] { "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "PylonSpitC 1",
				"3 BombGun500lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun500lbsE 1", "BombGun500lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null });
		Aircraft.weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null });
	}
}
