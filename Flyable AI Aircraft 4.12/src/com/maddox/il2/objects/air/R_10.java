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
// Last Edited at: 2013/01/23

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class R_10 extends Scheme1 implements TypeScout, TypeBomber, TypeStormovik {

	public R_10() {
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 1: // '\001'
			FM.turret[0].setHealth(f);
			break;
		}
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			hierMesh().chunkVisible("Head1_D0", false);
			break;

		case 1: // '\001'
			hierMesh().chunkVisible("Pilot2_D0", false);
			hierMesh().chunkVisible("HMask2_D0", false);
			hierMesh().chunkVisible("Pilot2_D1", true);
			break;
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (FM.getAltitude() < 3000F) {
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("HMask2_D0", false);
		} else {
			hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
			hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
		}
	}

	protected void moveBayDoor(float f) {
		hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -90F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -90F * f, 0.0F);
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -90F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f1, 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int i;
			if (s.endsWith("a")) {
				byte0 = 1;
				i = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				i = s.charAt(6) - 49;
			} else {
				i = s.charAt(5) - 49;
			}
			hitFlesh(i, shot, byte0);
		}
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
		if (!flag)
			return false;
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
		} else {
			return true;
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

	public static boolean bChangedPit = false;

	static {
		Class class1 = R_10.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "R-10");
		Property.set(class1, "meshName", "3DO/Plane/R-10/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/R-10.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitR_10.class, CockpitR10_TGunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02" });
		weaponsRegister(class1, "default", new String[] { "MGunShKASk 750", "MGunShKASk 750", "MGunShKASt 250", null, null });
		weaponsRegister(class1, "6xFAB50", new String[] { "MGunShKASk 750", "MGunShKASk 750", "MGunShKASt 250", "BombGunFAB50 3", "BombGunFAB50 3" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null });
	}
}
