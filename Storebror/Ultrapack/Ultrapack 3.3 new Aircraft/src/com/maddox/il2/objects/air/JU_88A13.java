package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombAB23;
import com.maddox.il2.objects.weapons.BombSC50;
import com.maddox.il2.objects.weapons.BombSC50C;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.il2.objects.weapons.FuelTank_Ju88;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class JU_88A13 extends JU_88A13xx implements TypeStormovik, TypeScout {

	public JU_88A13() {
		this.booster = new Bomb[2];
		this.JU88A13 = true;
		this.MassBoost = 12000F;
		this.massFuelTank = 0.0F;
		this.fullMass = 0.0F;
		this.enableBombFirstBombBox = false;
		this.enableBombSecondBombBox = false;
		this.bomb50 = 0;
		this.boostersEnable = false;
		this.bHasBoosters = true;
		this.boosterFireOutTime = -1L;
		this.diveMechStage = 0;
		this.bNDives = false;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurAltitude = 850F;
		this.fSightCurSpeed = 150F;
		this.fSightCurReadyness = 0.0F;
		this.fDiveRecoveryAlt = 850F;
		this.fDiveVelocity = 150F;
		this.fDiveAngle = 70F;
	}

	protected void moveBayDoor(float f) {
		this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -85F * f, 0.0F);
		if (this.enableBombFirstBombBox) {
			this.hierMesh().chunkSetAngles("Bay5_D0", 0.0F, 85F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Bay6_D0", 0.0F, -85F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Bay7_D0", 0.0F, 85F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Bay8_D0", 0.0F, -85F * f, 0.0F);
		}
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.M.massEmpty -= 160F;
		this.FM.CT.bHasAirBrakeControl = false;
		this.FM.M.fuel = 640F;
		if (this.getGunByHookName("_MGUN09") instanceof GunEmpty || this.getGunByHookName("_MGUN21") instanceof GunEmpty) this.FM.M.massEmpty += 90F;
		if (this.getGunByHookName("_CANNON03") instanceof GunEmpty) this.FM.M.massEmpty += 108F;
		if (this.getGunByHookName("_CANNON07") instanceof GunEmpty) this.FM.M.massEmpty += 240F;
		Object aobj[] = this.pos.getBaseAttached();
		if (aobj != null) for (int i = 0; i < aobj.length; i++) {
			if (aobj[i] instanceof FuelTank_Ju88) this.massFuelTank += 700F;
			if (aobj[i] instanceof BombSC50C || aobj[i] instanceof BombAB23 || aobj[i] instanceof BombSC50) this.bomb50++;
		}
		if (this.bomb50 <= 10) {
			this.hierMesh().chunkVisible("Baytank_D0", true);
			if (this.bomb50 != 0) this.enableBombSecondBombBox = true;
		} else {
			this.enableBombFirstBombBox = true;
			this.hierMesh().chunkVisible("Bay5_D0", true);
			this.hierMesh().chunkVisible("Bay6_D0", true);
			this.hierMesh().chunkVisible("Bay7_D0", true);
			this.hierMesh().chunkVisible("Bay8_D0", true);
		}
		this.fullMass = this.FM.M.getFullMass() + this.FM.M.fuel + this.FM.CT.getWeaponMass() + this.massFuelTank;
		if (this.fullMass >= this.MassBoost) this.boostersEnable = true;
		if (this.boostersEnable) for (int j = 0; j < 2; j++)
			try {
				this.booster[j] = new BombStarthilfe109500();
				this.booster[j].pos.setBase(this, this.findHook("_BoosterH" + (j + 1)), false);
				this.booster[j].pos.resetAsBase();
				this.booster[j].drawing(true);
			} catch (Exception exception) {
				this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
			}
	}

	public void destroy() {
		this.doCutBoosters();
		super.destroy();
	}

	public void doFireBoosters() {
		if (this.boostersEnable) {
			Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
			Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
		}
	}

	public void doCutBoosters() {
		if (this.boostersEnable) for (int i = 0; i < 2; i++)
			if (this.booster[i] != null) {
				this.booster[i].start();
				this.booster[i] = null;
			}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 33:
			case 34:
			case 35:
			case 36:
			case 37:
			case 38:
				this.doCutBoosters();
				this.FM.AS.setGliderBoostOff();
				this.bHasBoosters = false;
				break;
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		super.update(f);
		if (this.FM instanceof Pilot && this.bHasBoosters) {
			if (this.FM.getAltitude() > 300F && this.boosterFireOutTime == -1L && this.FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F) {
				this.doCutBoosters();
				this.FM.AS.setGliderBoostOff();
				this.bHasBoosters = false;
			}
			if (this.bHasBoosters && this.boosterFireOutTime == -1L && this.FM.Gears.onGround() && this.FM.EI.getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6 && this.FM.EI.engines[1].getStage() == 6 && this.FM.getSpeedKMH() > 20F)
				if (this.boostersEnable) {
					this.boosterFireOutTime = Time.current() + 30000L;
					this.doFireBoosters();
					this.FM.AS.setGliderBoostOn();
				} else {
					this.doCutBoosters();
					this.FM.AS.setGliderBoostOff();
					this.bHasBoosters = false;
				}
			if (this.bHasBoosters && this.boosterFireOutTime > 0L) {
				if (Time.current() < this.boosterFireOutTime) this.FM.producedAF.x += 20000D;
				if (Time.current() > this.boosterFireOutTime + 10000L) {
					this.doCutBoosters();
					this.FM.AS.setGliderBoostOff();
					this.bHasBoosters = false;
				}
			}
		}
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (this.FM.isPlayers()) bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers()) bChangedPit = true;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 4; i++)
			if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				break;

			case 2:
				this.hierMesh().chunkVisible("Pilot3_D0", false);
				this.hierMesh().chunkVisible("Pilot3_D1", true);
				this.hierMesh().chunkVisible("HMask3_D0", false);
				break;

			case 3:
				this.hierMesh().chunkVisible("Pilot4_D0", false);
				this.hierMesh().chunkVisible("Pilot4_D1", true);
				this.hierMesh().chunkVisible("HMask4_D0", false);
				break;
		}
	}

	public boolean        JU88A13;
	private float         MassBoost;
	private float         massFuelTank;
	private float         fullMass;
	protected boolean     enableBombFirstBombBox;
	protected boolean     enableBombSecondBombBox;
	private int           bomb50;
	protected boolean     boostersEnable;
	protected Bomb        booster[];
	protected boolean     bHasBoosters;
	protected long        boosterFireOutTime;
	public static boolean bChangedPit = false;
	public int            diveMechStage;
	public boolean        bNDives;
	public float          fSightCurForwardAngle;
	public float          fSightCurSideslip;
	public float          fSightCurAltitude;
	public float          fSightCurSpeed;
	public float          fSightCurReadyness;
	public float          fDiveRecoveryAlt;
	public float          fDiveVelocity;
	public float          fDiveAngle;

	static {
		Class class1 = JU_88A13.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ju-88");
		Property.set(class1, "meshName", "3DO/Plane/Ju-88A-13/hier_A13.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Ju-88A-13.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitJU_88A13.class, CockpitJU_88A13_NGunner.class, CockpitJU_88A13_RGunner.class, CockpitJU_88A13_BGunner.class });
		Property.set(class1, "LOSElevation", 1.0976F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 1, 14, 15, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
				3, 3, 3, 3, 3, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_CANNON01", "_MGUN06", "_MGUN07", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04",
						"_ExternalBomb04", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_BombSpawn04", "_BombSpawn04", "_BombSpawn04", "_BombSpawn04", "_BombSpawn04", "_BombSpawn04", "_BombSpawn04", "_BombSpawn04",
						"_BombSpawn04", "_BombSpawn04", "_BombSpawn04", "_BombSpawn04", "_BombSpawn04", "_BombSpawn04", "_BombSpawn04", "_BombSpawn03", "_BombSpawn04", "_BombSpawn03", "_BombSpawn03", "_BombSpawn03", "_BombSpawn03", "_BombSpawn03",
						"_BombSpawn03", "_BombSpawn03", "_BombSpawn03", "_BombSpawn03", "_BombSpawn03", "_BombSpawn03", "_BombSpawn03", "_BombSpawn02", "_BombSpawn03", "_BombSpawn02", "_BombSpawn02", "_BombSpawn02", "_BombSpawn02", "_BombSpawn02",
						"_BombSpawn02", "_BombSpawn02", "_BombSpawn01", "_BombSpawn01", "_BombSpawn01", "_BombSpawn01", "_BombSpawn01", "_BombSpawn01", "_BombSpawn01", "_BombSpawn01", "_BombSpawn01", "_BombSpawn01", "_ExternalDev03", "_ExternalDev04",
						"_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_MGUN17", "_MGUN18", "_MGUN19", "_MGUN20", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_MGUN21", "_MGUN22", "_MGUN23", "_MGUN24",
						"_MGUN25", "_MGUN26", "_MGUN27", "_MGUN28", "_MGUN29", "_MGUN30", "_MGUN31", "_MGUN32", "_CANNON07", "_CANNON08", "_CANNON09", "_CANNON10" });
	}
}
