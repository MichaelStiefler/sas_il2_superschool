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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class FW_189A2 extends Scheme3 implements TypeScout, TypeBomber {

	public FW_189A2() {
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 115F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 115F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 100F * f2, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 90F * f2, 0.0F);
		float f3 = Math.max(-f * 1500F, -110F);
		float f4 = Math.max(-f1 * 1500F, -110F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -f3, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -f3, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -f4, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f4, 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

	protected void moveFlap(float f) {
		float f1 = -40F * f;
		hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
	}

	public void msgShot(Shot shot) {
		setShot(shot);
		if (shot.chunkName.startsWith("WingLIn")) {
			if (World.Rnd().nextFloat() < 0.048F)
				FM.AS.setJamBullets(0, 0);
			if (v1.x < 0.25D && World.Rnd().nextFloat() < 0.25F && World.Rnd().nextFloat(0.01F, 0.121F) < shot.mass)
				FM.AS.hitTank(shot.initiator, 0, (int) (1.0F + shot.mass * 26.08F));
		}
		if (shot.chunkName.startsWith("WingRIn")) {
			if (World.Rnd().nextFloat() < 0.048F)
				FM.AS.setJamBullets(0, 1);
			if (v1.x < 0.25D && World.Rnd().nextFloat() < 0.25F && World.Rnd().nextFloat(0.01F, 0.121F) < shot.mass)
				FM.AS.hitTank(shot.initiator, 1, (int) (1.0F + shot.mass * 26.08F));
		}
		if (shot.chunkName.startsWith("Engine1")) {
			if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F)
				FM.AS.hitEngine(shot.initiator, 0, (int) (1.0F + shot.mass * 20.7F));
			if (World.Rnd().nextFloat() < 0.01F)
				FM.AS.hitEngine(shot.initiator, 0, 5);
		}
		if (shot.chunkName.startsWith("Engine2")) {
			if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F)
				FM.AS.hitEngine(shot.initiator, 1, (int) (1.0F + shot.mass * 20.7F));
			if (World.Rnd().nextFloat() < 0.01F)
				FM.AS.hitEngine(shot.initiator, 1, 5);
		}
		if (shot.chunkName.startsWith("Pilot1")) {
			if (v1.x > -0.5D || (double) shot.power * -v1.x > 12800D) {
				killPilot(shot.initiator, 0);
				FM.setCapableOfBMP(false, shot.initiator);
				if (Pd.z > 0.5D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
					HUD.logCenter("H E A D S H O T");
			}
			shot.chunkName = "CF_D0";
		}
		if (shot.chunkName.startsWith("Pilot2")) {
			killPilot(shot.initiator, 1);
			if (Pd.z > 0.5D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
				HUD.logCenter("H E A D S H O T");
			shot.chunkName = "CF_D0";
		}
		if (shot.chunkName.startsWith("Pilot3")) {
			killPilot(shot.initiator, 2);
			if (Pd.z > 0.5D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
				HUD.logCenter("H E A D S H O T");
			shot.chunkName = "CF_D0";
		}
		if (shot.chunkName.startsWith("Tail1") && World.Rnd().nextFloat() < 0.05F)
			FM.AS.hitTank(shot.initiator, 0, (int) (1.0F + shot.mass * 26.08F));
		if (shot.chunkName.startsWith("Tail2") && World.Rnd().nextFloat() < 0.05F)
			FM.AS.hitTank(shot.initiator, 1, (int) (1.0F + shot.mass * 26.08F));
		if ((FM.AS.astateEngineStates[0] == 4 || FM.AS.astateEngineStates[1] == 4) && World.Rnd().nextInt(0, 99) < 33)
			FM.setCapableOfBMP(false, shot.initiator);
		super.msgShot(shot);
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 33: // '!'
			if (World.Rnd().nextFloat() < 0.66F)
				FM.AS.hitTank(this, 0, 6);
			return super.cutFM(34, j, actor);

		case 36: // '$'
			if (World.Rnd().nextFloat() < 0.66F)
				FM.AS.hitTank(this, 1, 6);
			return super.cutFM(37, j, actor);
		}
		return super.cutFM(i, j, actor);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 4; i++)
			if (FM.getAltitude() < 3000F)
				hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else
				hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	public void doWoundPilot(int i, float f) {
		if (i == 1) {
			FM.turret[1].setHealth(f);
			hierMesh().chunkVisible("Turret2A_D0", false);
			hierMesh().chunkVisible("Turret2B_D0", false);
			hierMesh().chunkVisible("Turret2B_D1", true);
		}
		if (i == 2) {
			FM.turret[0].setHealth(f);
			hierMesh().chunkVisible("Turret1A_D0", false);
			hierMesh().chunkVisible("Turret1B_D0", false);
			hierMesh().chunkVisible("Turret1B_D1", true);
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			break;

		case 1: // '\001'
			hierMesh().chunkVisible("Pilot2_D0", false);
			hierMesh().chunkVisible("HMask2_D0", false);
			hierMesh().chunkVisible("Pilot2_D1", true);
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

		case 0: // '\0'
			if (f1 < 40F) {
				f1 = 40F;
				flag = false;
			}
			if (f1 > 96F) {
				f1 = 96F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < -75F) {
				f = -75F;
				flag = false;
			}
			if (f > 85F) {
				f = 85F;
				flag = false;
			}
			if (f1 < 4F) {
				f1 = 4F;
				flag = false;
			}
			if (f1 > 80F) {
				f1 = 80F;
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

	public static String getSkinPrefix(String s, Regiment regiment) {
		if (regiment == null || regiment.country() == null)
			return "";
		if (regiment.country().equals(PaintScheme.countryHungary))
			return PaintScheme.countryHungary + "_";
		else
			return "";
	}

	static {
		Class class1 = FW_189A2.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Fw-189");
		Property.set(class1, "meshName", "3do/plane/Fw-189A-2/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
		Property.set(class1, "yearService", 1941.6F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/Fw-189A-2.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitFw189.class, CockpitFw189_RGunner.class, CockpitFW189_BGunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 3, 10, 10, 11, 11 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06" });
		weaponsRegister(class1, "default", new String[] { "MGunMG17k 660", "MGunMG17k 660", null, null, null, null, "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG81t 1500", "MGunMG81t 1500" });
		weaponsRegister(class1, "4xSC50", new String[] { "MGunMG17k 660", "MGunMG17k 660", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG81t 1500", "MGunMG81t 1500" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null });
	}
}