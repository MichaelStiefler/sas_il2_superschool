package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class JU_88P1 extends JU_88Axx implements TypeBNZFighter, TypeStormovik {

	public JU_88P1() {
		this.topBlisterRemoved = false;
		this.phase = 0;
		this.disp = 0.0F;
		this.oldbullets = -1;
		this.g1 = null;
		this.BK75stabilizingMultiplier = 2.0F;
	}

	public void onAircraftLoaded() {
		if (this.FM.CT.Weapons[1] != null) this.g1 = this.FM.CT.Weapons[1][0];
		this.FM.Sq.liftKeel *= this.BK75stabilizingMultiplier;
	}

	public void doWreck(String s) {
		if (this.hierMesh().chunkFindCheck(s) != -1) {
			this.hierMesh().hideSubTrees(s);
			Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind(s));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(this.FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
	}

	public void blisterRemoved(int i) {
		if (!this.topBlisterRemoved) {
			this.doWreck("BlisterTop_D0");
			this.hierMesh().chunkVisible("Turret1B_D0", false);
			this.hierMesh().chunkVisible("Turret2B_D0", false);
			this.topBlisterRemoved = true;
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xbk75gun")) {
			this.debuggunnery("BK75: Disabled..");
			this.FM.AS.setJamBullets(1, 0);
			this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
		}
		if (s.equals("xxarmorp6")) this.getEnergyPastArmor(0.5F, shot);
		if (s.equals("xxarmorp7")) this.getEnergyPastArmor(0.5F, shot);
		super.hitBone(s, shot, point3d);
	}

	protected void moveBayDoor(float f) {
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
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
			this.hierMesh().chunkVisible("HMask2_D0", false);
			this.hierMesh().chunkVisible("HMask3_D0", false);
			this.hierMesh().chunkVisible("HMask3a_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
			this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
			this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
			this.hierMesh().chunkVisible("HMask3a_D0", this.hierMesh().isChunkVisible("Pilot3a_D0"));
		}
	}

	public void update(float f) {
		if (this.g1 != null) switch (this.phase) {
			default:
				break;

			case 0:
				if (this.g1.isShots() && this.oldbullets != this.g1.countBullets()) {
					this.oldbullets = this.g1.countBullets();
					this.phase++;
					this.hierMesh().chunkVisible("Shell_D0", true);
					this.disp = 0.0F;
				}
				break;

			case 1:
				this.disp += 8F * f;
				this.resetYPRmodifier();
				Aircraft.xyz[1] = this.disp;
				this.hierMesh().chunkSetLocate("BK75Dulo_D0", Aircraft.xyz, Aircraft.ypr);
				if (this.disp >= 0.75F) this.phase++;
				break;

			case 2:
				this.disp += 6F * f;
				this.resetYPRmodifier();
				Aircraft.xyz[0] = this.disp;
				this.hierMesh().chunkSetLocate("Shell_D0", Aircraft.xyz, Aircraft.ypr);
				if (this.disp >= 1.0F) this.phase++;
				break;

			case 3:
				Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Shell_D0"));
				Eff3DActor.New(wreckage, null, null, 1.0F, Wreckage.SMOKE, 3F);
				Vector3d vector3d = new Vector3d();
				vector3d.set(this.FM.Vwld);
				vector3d.y = 0.95D * vector3d.y - 1.0D;
				vector3d.x = 0.95D * vector3d.x + 1.0D;
				vector3d.z -= 5D;
				wreckage.setSpeed(vector3d);
				this.hierMesh().chunkVisible("Shell_D0", false);
				this.disp = 0.75F;
				this.phase++;
				break;

			case 4:
				this.disp -= 1.0F * f;
				this.resetYPRmodifier();
				Aircraft.xyz[1] = this.disp;
				this.hierMesh().chunkSetLocate("BK75Dulo_D0", Aircraft.xyz, Aircraft.ypr);
				if (this.disp <= 0.0F) {
					this.disp = 0.0F;
					Aircraft.xyz[0] = this.disp;
					Aircraft.xyz[1] = this.disp;
					this.hierMesh().chunkSetLocate("BK75Dulo_D0", Aircraft.xyz, Aircraft.ypr);
					this.hierMesh().chunkSetLocate("Shell_D0", Aircraft.xyz, Aircraft.ypr);
					this.phase++;
				}
				break;

			case 5:
				this.phase = 0;
				break;
		}
		super.update(f);
		if (Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) > 70F && this.FM.CT.getFlap() > 0.01D && this.FM.CT.FlapsControl != 0.0F) {
			this.FM.CT.FlapsControl = 0.0F;
			World.cur();
			if (((Interpolate) this.FM).actor == World.getPlayerAircraft()) HUD.log("FlapsRaised");
		}
	}

	public boolean turretAngles(int i, float af[]) {
		for (int j = 0; j < 2; j++) {
			af[j] = (af[j] + 3600F) % 360F;
			if (af[j] > 180F) af[j] -= 360F;
		}

		af[2] = 0.0F;
		boolean flag = true;
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (!this.FM.turret[1].bIsAIControlled || this.secondaryRearGunActive) {
					flag = false;
					f = 0.0F;
					f1 = 0.0F;
					break;
				}
				this.mainRearGunActive = true;
				if (f < -25F) {
					f = -25F;
					flag = false;
					this.mainRearGunActive = false;
				}
				if (f > 45F) {
					f = 45F;
					flag = false;
				}
				if (f1 < -10F) {
					f1 = -10F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				if (f > 3F) {
					if (f1 < Aircraft.cvt(f, 3F, 8.4F, -1.72F, -10F)) f1 = Aircraft.cvt(f, 3F, 8.4F, -1.72F, -10F);
					break;
				}
				if (f > -5.3F) {
					if (f1 < Aircraft.cvt(f, -5.3F, 3F, -1.72F, -1.72F)) f1 = Aircraft.cvt(f, -5.3F, 3F, -1.72F, -1.72F);
					break;
				}
				if (f1 < Aircraft.cvt(f, -25F, -5.3F, -3F, -1.72F)) f1 = Aircraft.cvt(f, -25F, -5.3F, -3F, -1.72F);
				break;

			case 1:
				if (!this.FM.turret[0].bIsAIControlled || this.mainRearGunActive) {
					this.secondaryRearGunActive = false;
					flag = false;
					f = 0.0F;
					f1 = 0.0F;
					break;
				}
				this.secondaryRearGunActive = true;
				if (f < -45F) {
					f = -45F;
					flag = false;
				}
				if (f > 25F) {
					f = 25F;
					flag = false;
					this.secondaryRearGunActive = false;
				}
				if (f1 < -10F) {
					f1 = -10F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				if (f < -3F) {
					if (f1 < Aircraft.cvt(f, -8.9F, -3F, -10F, -1.72F)) f1 = Aircraft.cvt(f, -8.9F, -3F, -10F, -1.72F);
					break;
				}
				if (f < 5.3F) {
					if (f1 < Aircraft.cvt(f, -3F, 5.3F, -1.72F, -1.72F)) f1 = Aircraft.cvt(f, -3F, 5.3F, -1.72F, -1.72F);
					break;
				}
				if (f1 < Aircraft.cvt(f, 5.3F, 25F, -1.72F, -3F)) f1 = Aircraft.cvt(f, 5.3F, 25F, -1.72F, -3F);
				break;

			case 2:
				if (f < -15F) {
					f = -15F;
					flag = false;
				}
				if (f > 25F) {
					f = 25F;
					flag = false;
				}
				if (f1 < -45F) {
					f1 = -45F;
					flag = false;
				}
				if (f1 > 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	protected void moveAirBrake(float f) {
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
				this.hierMesh().chunkVisible("HMask3_D0", false);
				this.hierMesh().chunkVisible("HMask3a_D0", false);
				this.hierMesh().chunkVisible("Pilot3_D1", this.hierMesh().isChunkVisible("Pilot3_D0"));
				this.hierMesh().chunkVisible("Pilot3a_D1", this.hierMesh().isChunkVisible("Pilot3a_D0"));
				this.hierMesh().chunkVisible("Pilot3_D0", false);
				this.hierMesh().chunkVisible("Pilot3a_D0", false);
				break;
		}
	}

	public void doRemoveBodyFromPlane(int i) {
		super.doRemoveBodyFromPlane(i);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, -65F, 65F, 65F, -65F), 0.0F);
	}

	public static boolean bChangedPit = false;
	public boolean        topBlisterRemoved;
	private int           phase;
	private float         disp;
	private int           oldbullets;
	private BulletEmitter g1;
	private float         BK75stabilizingMultiplier;

	static {
		Class class1 = JU_88P1.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ju-88");
		Property.set(class1, "meshName", "3DO/Plane/Ju-88P-1/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Ju-88P-1.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitJU_88P1.class, CockpitJU_88P1_LGunner.class, CockpitJU_88P1_RGunner.class });
		Property.set(class1, "LOSElevation", 0.5F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 12, 1, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_HEAVYCANNON01", "_ExternalDev01" });
	}
}
