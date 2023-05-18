package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.il2.objects.weapons.MGunHo203si;
import com.maddox.rts.Property;

public abstract class KI_45 extends Scheme2 implements TypeFighter, TypeBNZFighter, TypeStormovik {

	public KI_45() {
		this.suspR = 0.0F;
		this.suspL = 0.0F;
		this.bChangedPit = true;
		this.hasJazzMusic = false;
		this.canopyF = 0.0F;
		this.fullCanopyOpened = false;
		this.sideWindowOpened = false;
		this.sideWindow = false;
		this.slideRWindow = false;
		this.pit = null;
		this.gunner = null;
		this.turretUp = false;
		this.turretMove = 0.0F;
		this.gunnerDead = false;
		this.gunnerEjected = false;
	}

	public float getEyeLevelCorrection() {
		return -0.05F;
	}

	public void registerPit(CockpitKI_45 cockpitki_45) {
		this.pit = cockpitki_45;
	}

	public void registerGunner(CockpitKI_45_Gunner cockpitki_45_gunner) {
		this.gunner = cockpitki_45_gunner;
	}

	public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
		super.setOnGround(point3d, orient, vector3d);
		if (this.FM.isPlayers()) {
			this.FM.CT.bHasCockpitDoorControl = true;
			this.FM.CT.dvCockpitDoor = 0.5F;
			this.FM.CT.cockpitDoorControl = 1.0F;
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
			this.hierMesh().chunkVisible("HMask2_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
			this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
		}
		Actor actor = War.GetNearestEnemy(this, 16, 7000F);
		Aircraft aircraft = War.getNearestEnemy(this, 6000F);
		boolean flag1 = this.FM.CT.Weapons[10] != null && this.FM.CT.Weapons[10][0].haveBullets();
		if (flag1 && (actor != null && !(actor instanceof BridgeSegment) || aircraft != null)) {
			if (!this.turretUp) this.turretUp = true;
		} else if (this.turretUp) this.turretUp = false;
	}

	protected void moveFlap(float f) {
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 50F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 50F * f, 0.0F);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		if (af[0] < -25F) {
			af[0] = -25F;
			flag = false;
		} else if (af[0] > 25F) {
			af[0] = 25F;
			flag = false;
		}
		float f = Math.abs(af[0]);
		if (af[1] < -10F) {
			af[1] = -10F;
			flag = false;
		}
		if (af[1] > 45F) {
			af[1] = 45F;
			flag = false;
		}
		if (!flag) return false;
		float f1 = af[1];
		if (f < 1.2F && f1 < 13.3F) return false;
		return f1 >= -3.1F || f1 <= -4.6F;
	}

	public void update(float f) {
		super.update(f);
		if (!this.FM.isPlayers() && this.FM.Gears.onGround()) if (this.FM.EI.engines[1].getRPM() < 100F) this.FM.CT.cockpitDoorControl = 1.0F;
		else this.FM.CT.cockpitDoorControl = 0.0F;
		float f1 = 25F * this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(this.flapps[0] - f1) > 0.01F) {
			this.flapps[0] = f1;
			for (int i = 1; i <= 10; i++) {
				String s = "Radiator1_" + i + "_D0";
				if (i <= 5) this.hierMesh().chunkSetAngles(s, 0.0F, -f1, 0.0F);
				else this.hierMesh().chunkSetAngles(s, 0.0F, f1, 0.0F);
			}

		}
		f1 = 25F * this.FM.EI.engines[1].getControlRadiator();
		if (Math.abs(this.flapps[1] - f1) > 0.01F) {
			this.flapps[1] = f1;
			for (int j = 1; j <= 10; j++) {
				String s1 = "Radiator2_" + j + "_D0";
				if (j > 5) this.hierMesh().chunkSetAngles(s1, 0.0F, -f1, 0.0F);
				else this.hierMesh().chunkSetAngles(s1, 0.0F, f1, 0.0F);
			}

		}
		if (this.FM.turret[0].bIsAIControlled) {
			this.hierMesh().chunkSetAngles("TurretTilt_D0", 0.0F, 0.0F, 0.0F);
			if (!this.gunnerDead && !this.gunnerEjected) {
				if (this.turretUp) {
					if (this.turretMove < 1.0D) this.turretMove += 0.025F;
				} else if (this.turretMove > 0.0D) this.turretMove -= 0.025F;
				this.hierMesh().chunkSetAngles("TurretMain_D0", 0.0F, this.turretMove * 10F, 0.0F);
				this.hierMesh().chunkSetAngles("TurretTilt2_D0", this.turretMove * 10F, 0.0F, 0.0F);
			}
		}
		super.update(f);
	}

	public void setHumanControlledTurretAngels(Turret turret, float af[], HierMesh hiermesh, ActorHMesh actorhmesh) {
		af[0] = af[1] = af[2] = 0.0F;
		hiermesh.setCurChunk(turret.indexA);
		actorhmesh.hierMesh().chunkSetAngles(af);
		float f = turret.tu[1];
		float f1 = turret.tu[0];
		af[1] = Math.min(f, 50F);
		hiermesh.setCurChunk(turret.indexB);
		actorhmesh.hierMesh().chunkSetAngles(af);
		this.hierMesh().chunkSetAngles("TurretTilt_D0", 0.0F, -f1, 0.0F);
		if (f > 50F) {
			this.hierMesh().chunkSetAngles("TurretMain_D0", 0.0F, Aircraft.cvt(f, 50F, 75F, 10F, 25F), 0.0F);
			this.hierMesh().chunkSetAngles("TurretTilt2_D0", 10F, 0.0F, 0.0F);
		} else if (f < 10F) {
			float f2 = Math.min(10F, (float) Math.abs(f1 * 0.5D));
			float f3 = Aircraft.cvt(f, 0.0F, 10F, f2, 10F);
			this.hierMesh().chunkSetAngles("TurretMain_D0", 0.0F, f3, 0.0F);
			this.hierMesh().chunkSetAngles("TurretTilt2_D0", f3, 0.0F, 0.0F);
		} else {
			this.hierMesh().chunkSetAngles("TurretMain_D0", 0.0F, 10F, 0.0F);
			this.hierMesh().chunkSetAngles("TurretTilt2_D0", 10F, 0.0F, 0.0F);
		}
	}

	public void doRemoveBodyFromPlane(int i) {
		super.doRemoveBodyFromPlane(i);
		if (i == 2) {
			super.doRemoveBodyFromPlane(3);
			this.gunnerEjected = true;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D1", this.hierMesh().isChunkVisible("Pilot2_D0"));
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				this.gunnerDead = true;
				break;
		}
	}

	public void moveCockpitDoor(float f) {
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.xyz[1] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.xyz[2] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		if (f > this.canopyF) {
			if (this.FM.Gears.onGround() && this.FM.getSpeed() < 5F || this.fullCanopyOpened) {
				this.sideWindow = false;
				this.fullCanopyOpened = true;
				float f1 = 80F * this.FM.CT.getCockpitDoor();
				this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, f1, 0.0F);
				this.hierMesh().chunkSetAngles("LBlister1_D0", 0.0F, -floatindex(Aircraft.cvt(f1, 0.0F, 80F, 0.0F, 16F), canopyScale), 0.0F);
			} else {
				this.sideWindow = true;
				if (this.pit != null && this.canopyF == 0.0F) this.slideRWindow = this.pit.isViewRight();
				this.sideWindowOpened = true;
				Aircraft.xyz[1] = f * 0.25F;
				if (this.slideRWindow) this.hierMesh().chunkSetLocate("RGlassBlister1_d0", Aircraft.xyz, Aircraft.ypr);
				else this.hierMesh().chunkSetLocate("LGlassBlister1_d0", Aircraft.xyz, Aircraft.ypr);
			}
		} else if (this.FM.Gears.onGround() && this.FM.getSpeed() < 5F && !this.sideWindowOpened || this.fullCanopyOpened) {
			this.sideWindow = false;
			float f2 = 80F * this.FM.CT.getCockpitDoor();
			this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, f2, 0.0F);
			this.hierMesh().chunkSetAngles("LBlister1_D0", 0.0F, -floatindex(Aircraft.cvt(f2, 0.0F, 80F, 0.0F, 16F), canopyScale), 0.0F);
			if (f == 0.0F) this.fullCanopyOpened = false;
		} else {
			this.sideWindow = true;
			Aircraft.xyz[1] = f * 0.25F;
			if (this.slideRWindow) this.hierMesh().chunkSetLocate("RGlassBlister1_d0", Aircraft.xyz, Aircraft.ypr);
			else this.hierMesh().chunkSetLocate("LGlassBlister1_d0", Aircraft.xyz, Aircraft.ypr);
			if (f == 0.0F) this.sideWindowOpened = false;
		}
		this.canopyF = f;
		if (this.canopyF < 0.01D) this.canopyF = 0.0F;
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
			this.setDoorSnd(f);
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -45.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -45.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, -54F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -54F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL9_D0", 0.0F, -60F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR9_D0", 0.0F, -60F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 105F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 105F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 40F * f, 0.0F);
		float f7 = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 60F);
		float f8 = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 60F);
		if (f7 < 0.05F) f7 = 0.0F;
		if (f8 < 0.05F) f8 = 0.0F;
		hiermesh.chunkSetAngles("GearL10_d0", 0.0F, f7, 0.0F);
		hiermesh.chunkSetAngles("GearL11_d0", 0.0F, -f7, 0.0F);
		hiermesh.chunkSetAngles("GearR10_d0", 0.0F, -f8, 0.0F);
		hiermesh.chunkSetAngles("GearR11_d0", 0.0F, f8, 0.0F);
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.xyz[1] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.xyz[2] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		Aircraft.xyz[1] = f * -0.2F;
		hiermesh.chunkSetLocate("GearR8_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[1] = f * -0.2F;
		hiermesh.chunkSetLocate("GearL8_D0", Aircraft.xyz, Aircraft.ypr);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	protected void moveAileron(float f) {
		if (f > 0.0F) {
			this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 24F * f, 0.0F);
			this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -16F * f, 0.0F);
		} else {
			this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 16F * f, 0.0F);
			this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -24F * f, 0.0F);
		}
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -27F * f, 0.0F);
	}

	protected void moveElevator(float f) {
		if (f > 0.0F) {
			this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -25F * f, 0.0F);
			this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -25F * f, 0.0F);
		} else {
			this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -18F * f, 0.0F);
			this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -18F * f, 0.0F);
		}
	}

	public void moveWheelSink() {
		this.suspL = 0.9F * this.suspL + 0.1F * this.FM.Gears.gWheelSinking[0];
		this.suspR = 0.9F * this.suspR + 0.1F * this.FM.Gears.gWheelSinking[1];
		if (this.suspL > 0.035F) this.suspL = 0.035F;
		if (this.suspR > 0.035F) this.suspR = 0.035F;
		if (this.suspL < 0.0F) this.suspL = 0.0F;
		if (this.suspR < 0.0F) this.suspR = 0.0F;
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.xyz[1] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.xyz[2] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		float f = -793.5F;
		Aircraft.xyz[1] = this.suspL * 3.5F;
		this.hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, -this.suspL * f, 0.0F);
		this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, this.suspL * f, 0.0F);
		Aircraft.xyz[1] = this.suspR * 3.5F;
		this.hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, -this.suspR * f, 0.0F);
		this.hierMesh().chunkSetAngles("GearR5_D0", 0.0F, this.suspR * f, 0.0F);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, -65F, 65F, 65F, -65F), 0.0F);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				this.debuggunnery("Armor: Hit..");
				if (s.endsWith("2")) this.getEnergyPastArmor(World.Rnd().nextFloat(1.96F, 3.4839F), shot);
				else if (s.endsWith("3")) {
					if (point3d.z < 0.08D) this.getEnergyPastArmor(8.585D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
					else if (point3d.z < 0.09D) this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
					else if (point3d.y > 0.175D && point3d.y < 0.287D && point3d.z < 0.177D) this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
					else if (point3d.y > -0.334D && point3d.y < -0.177D && point3d.z < 0.204D) this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
					else if (point3d.z > 0.288D && Math.abs(point3d.y) < 0.077D) this.getEnergyPastArmor(World.Rnd().nextFloat(8.5F, 12.46F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
					else this.getEnergyPastArmor(10.51D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
				} else if (s.endsWith("1")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F), shot);
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
					this.debuggunnery("Armor: Armor Glass: Hit..");
					if (shot.power <= 0.0F) {
						this.debuggunnery("Armor: Armor Glass: Bullet Stopped..");
						if (World.Rnd().nextFloat() < 0.96F) this.doRicochetBack(shot);
					}
				}
			} else if (s.startsWith("xxcontrol")) {
				this.debuggunnery("Controls: Hit..");
				if (World.Rnd().nextFloat() >= 0.99F) {
					int i = new Integer(s.substring(9)).intValue();
					switch (i) {
						case 2:
						case 3:
							if (this.getEnergyPastArmor(3.5F, shot) > 0.0F) {
								this.FM.AS.setControlsDamage(shot.initiator, 2);
								this.debuggunnery("Controls: Rudder Controls: Fuselage Line Destroyed..");
							}
							break;

						case 0:
						case 1:
							if (this.getEnergyPastArmor(0.002F, shot) > 0.0F) {
								this.FM.AS.setControlsDamage(shot.initiator, 1);
								this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
							}
							break;

						case 5:
						case 6:
						case 7:
						case 8:
							if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
								this.FM.AS.setControlsDamage(shot.initiator, 0);
								this.debuggunnery("Controls: Aileron Controls: Disabled..");
							}
							break;
					}
				}
			} else if (s.startsWith("xxspar")) {
				this.debuggunnery("Spar Construction: Hit..");
				if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.5F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.debuggunnery("Spar Construction: Tail1 Spars Broken in Half..");
					this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				}
				if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(17.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(17.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(13.2F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(13.2F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
			} else if (s.startsWith("xxlock")) {
				this.debuggunnery("Lock Construction: Hit..");
				if (s.startsWith("xxlockr")) {
					int j = s.charAt(6) - 48;
					if (this.getEnergyPastArmor(6.56F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) if (j < 3) {
						this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
						this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
					} else {
						this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
						this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
					}
				}
				if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
				}
				if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
				}
			} else if (s.startsWith("xxeng")) {
				int k = s.charAt(5) - 49;
				this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Hit..");
				if (s.endsWith("prop")) {
					if (this.getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) if (World.Rnd().nextFloat() < 0.5F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 3);
						this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Prop Governor Hit, Disabled..");
					} else {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 4);
						this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Prop Governor Hit, Damaged..");
					}
				} else if (s.endsWith("eqpt")) {
					if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) if (World.Rnd().nextFloat() < 0.5F) {
						this.FM.EI.engines[k].setEngineStuck(shot.initiator);
						this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Bullet Jams Reductor Gear..");
					} else {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 3);
						this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 4);
						this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Reductor Gear Damaged, Prop Governor Failed..");
					}
				} else if (s.endsWith("supc")) {
					if (this.getEnergyPastArmor(2.0F, shot) > 0.0F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 0);
						this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Supercharger Disabled..");
					}
				} else if (s.endsWith("feed")) {
					if (this.getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F && this.FM.EI.engines[k].getPowerOutput() > 0.7F) {
						this.FM.AS.hitEngine(shot.initiator, k, 100);
						this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Pressurized Fuel Line Pierced, Fuel Flamed..");
					}
				} else if (s.endsWith("fuel")) {
					if (this.getEnergyPastArmor(1.1F, shot) > 0.0F) {
						this.FM.EI.engines[k].setEngineStops(shot.initiator);
						this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Fuel Line Stalled, Engine Stalled..");
					}
				} else if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(3.1F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 175000F) {
							this.FM.AS.setEngineStuck(shot.initiator, k);
							this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Bullet Jams Crank Ball Bearing..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, k, 2);
							this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[k].getReadyness() + "..");
						}
						this.FM.EI.engines[k].setReadyness(shot.initiator, this.FM.EI.engines[k].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
						this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[k].getReadyness() + "..");
					}
					this.getEnergyPastArmor(World.Rnd().nextFloat(22.5F, 33.6F), shot);
				} else if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[k].getCylindersRatio() * 1.0F) {
						this.FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
						this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Cylinders Hit, " + this.FM.EI.engines[k].getCylindersOperable() + "/" + this.FM.EI.engines[k].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 24000F) {
							this.FM.AS.hitEngine(shot.initiator, k, 3);
							this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Cylinders Hit, Engine Fires..");
						}
						if (World.Rnd().nextFloat() < 0.01F) {
							this.FM.AS.setEngineStuck(shot.initiator, k);
							this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Bullet Jams Piston Head..");
						}
						this.getEnergyPastArmor(22.5F, shot);
					}
				} else if (s.endsWith("mag1") || s.endsWith("mag2")) {
					if (World.Rnd().nextFloat() < 0.5F) {
						int j2 = s.charAt(9) - 49;
						this.FM.EI.engines[k].setMagnetoKnockOut(shot.initiator, j2);
						this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Magneto " + j2 + " Destroyed..");
					}
				} else if (s.endsWith("oil")) {
					this.FM.AS.hitOil(shot.initiator, k);
					this.debuggunnery("Engine Module (" + (k == 0 ? "Left" : "Right") + "): Oil Radiator Hit..");
				}
			} else if (s.startsWith("xxoil")) {
				if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 2.345F), shot) > 0.0F) {
					int l = s.charAt(5) - 49;
					this.FM.AS.hitOil(shot.initiator, l);
					this.getEnergyPastArmor(0.22F, shot);
					this.debuggunnery("Engine Module (" + (l == 0 ? "Left" : "Right") + "): Oil Tank Pierced..");
				}
			} else if (s.startsWith("xxw")) {
				if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 0.75F), shot) > 0.0F) {
					int i1 = s.charAt(3) - 49;
					if (this.FM.AS.astateEngineStates[i1] == 0) {
						this.debuggunnery("Engine Module (" + (i1 == 0 ? "Left" : "Right") + "): Water Radiator Pierced..");
						this.FM.AS.hitEngine(shot.initiator, i1, 2);
						this.FM.AS.doSetEngineState(shot.initiator, i1, 2);
					}
					this.getEnergyPastArmor(2.22F, shot);
				}
			} else if (s.startsWith("xxtank")) {
				int j1 = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 2.23F), shot) > 0.0F && World.Rnd().nextFloat() < 0.1F && j1 < this.FM.AS.astateTankStates.length) {
					if (this.FM.AS.astateTankStates[j1] == 0) {
						this.debuggunnery("Fuel Tank " + (j1 + 1) + ": Pierced..");
						this.FM.AS.hitTank(shot.initiator, j1, 1);
						this.FM.AS.doSetTankState(shot.initiator, j1, 1);
					} else if (this.FM.AS.astateTankStates[j1] == 1) {
						this.debuggunnery("Fuel Tank " + (j1 + 1) + ": Pierced..");
						this.FM.AS.hitTank(shot.initiator, j1, 1);
						this.FM.AS.doSetTankState(shot.initiator, j1, 2);
					}
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.2F) {
						this.FM.AS.hitTank(shot.initiator, 2, 2);
						this.debuggunnery("Fuel Tank " + (j1 + 1) + ": Hit..");
					}
				}
			} else if (s.startsWith("xxhyd")) {
				if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F) {
					this.debuggunnery("Hydro System: Disabled..");
					this.FM.AS.setInternalDamage(shot.initiator, 0);
				}
			} else if (s.startsWith("xxpanel")) {
				if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
				if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
				if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
			} else {
				if (s.startsWith("xxbgun")) {
					this.debuggunnery("Belly gun disabled");
					this.FM.AS.setJamBullets(0, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 12.96F), shot);
				}
				if (s.startsWith("xxmaingun")) {
					this.debuggunnery("main nose gun disabled..");
					this.FM.AS.setJamBullets(1, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
				}
				if (s.startsWith("xxcannon1")) {
					this.debuggunnery("jazz gun disabled..");
					this.FM.AS.setJamBullets(1, 1);
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
				}
				if (s.startsWith("xxcannon2")) {
					this.debuggunnery("jazz gun disabled..");
					this.FM.AS.setJamBullets(1, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
				}
			}
			if (s.equals("xxglassright")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
			else if (s.equals("xxglassleft")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
			else if (s.equals("xxglasstop")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
			else if (s.equals("xxglassfront")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
			else if (s.equals("xxpanel")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
			else if (s.equals("xxrevi")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
		} else if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
			if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
		} else if (s.startsWith("xengine")) {
			int k1 = s.charAt(7) - 48;
			if (this.chunkDamageVisible("Engine" + k1) < 2) this.hitChunk("Engine" + k1, shot);
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) {
			int l1 = s.charAt(5) - 48;
			if (this.chunkDamageVisible("Keel" + l1) < 3) this.hitChunk("Keel" + l1, shot);
			if (this.hierMesh().isChunkVisible("keel1_d2")) this.hierMesh().chunkVisible("Wire_D0", false);
		} else if (s.startsWith("xrudder")) {
			int i2 = s.charAt(7) - 48;
			if (this.chunkDamageVisible("Rudder" + i2) < 1) this.hitChunk("Rudder" + i2, shot);
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 3) this.hitChunk("StabL", shot);
			if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 3) this.hitChunk("StabR", shot);
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
			if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
		} else if (s.startsWith("xwing")) {
			if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
			if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
			if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
			if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
			if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
			if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
		} else if (s.startsWith("xarone")) {
			if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
			if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
		} else if (s.startsWith("xgear")) {
			if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
				this.debuggunnery("Hydro System: Disabled..");
				this.FM.AS.setInternalDamage(shot.initiator, 0);
			}
			if (s.endsWith("2")) {
				if (World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(6.8F, 29.35F), shot) > 0.0F) {
					this.debuggunnery("Undercarriage: Stuck..");
					this.FM.AS.setInternalDamage(shot.initiator, 3);
				}
				char s1 = s.charAt(5);
				this.hitChunk("Gear" + Character.toUpperCase(s1) + "2", shot);
			}
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int k2;
			if (s.endsWith("a")) {
				byte0 = 1;
				k2 = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				k2 = s.charAt(6) - 49;
			} else k2 = s.charAt(5) - 49;
			this.hitFlesh(k2, shot, byte0);
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 19:
				this.FM.Gears.hitCentreGear();
				this.hierMesh().chunkVisible("WireAnt_D0", false);
				break;

			case 11:
				this.hierMesh().chunkVisible("WireAnt_D0", false);
				break;

			case 33:
				this.hierMesh().chunkVisible("TankHolderL_D0", false);
				break;

			case 36:
				this.hierMesh().chunkVisible("TankHolderR_D0", false);
				break;

			case 13:
				this.killNoseCannon();
				break;

			case 10:
				this.doWreck("GearR6_D0");
				this.FM.Gears.hitRightGear();
				break;

			case 9:
				this.doWreck("GearL6_D0");
				this.FM.Gears.hitLeftGear();
				break;
		}
		return super.cutFM(i, j, actor);
	}

	private void doWreck(String s) {
	}

	private void killNoseCannon() {
		for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
			BulletEmitter abulletemitter[] = this.FM.CT.Weapons[i];
			if (abulletemitter != null) for (int j = 0; j < abulletemitter.length; j++) {
				BulletEmitter bulletemitter = abulletemitter[j];
				if (bulletemitter != null && bulletemitter instanceof MGunHo203si) this.FM.CT.Weapons[i][j] = GunEmpty.get();
			}
		}

	}

	public void blisterRemoved(int i) {
		if (i == 1) this.doRemoveBlisters();
	}

	private final void doRemoveBlisters() {
		this.FM.CT.bHasCockpitDoorControl = false;
		this.bChangedPit = true;
		if (this.hierMesh().chunkFindCheck("LBlister1_D0") != -1) {
			this.hierMesh().hideSubTrees("LBlister1_D0");
			Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("LBlister1_D0"));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(this.FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
		if (this.hierMesh().chunkFindCheck("Blister1_D0") != -1) {
			this.hierMesh().hideSubTrees("Blister1_D0");
			Wreckage wreckage1 = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
			wreckage1.collide(true);
			Vector3d vector3d1 = new Vector3d();
			vector3d1.set(this.FM.Vwld);
			wreckage1.setSpeed(vector3d1);
		}
	}

	float                      suspR;
	float                      suspL;
	public boolean             bChangedPit;
	public boolean             hasJazzMusic;
	public float               canopyF;
	private boolean            fullCanopyOpened;
	private boolean            sideWindowOpened;
	public boolean             sideWindow;
	private boolean            slideRWindow;
	CockpitKI_45               pit;
	CockpitKI_45_Gunner        gunner;
	private boolean            turretUp;
	private float              turretMove;
	private boolean            gunnerDead;
	private boolean            gunnerEjected;
	private static final float canopyScale[] = { 0.0F, 2.2F, 4.3F, 5.5F, 5.7F, 6.43F, 6.85F, 5.84F, 7.11F, 9F, 10.5F, 13.5F, 16.9F, 19.9F, 22.6F, 27.63F, 31.9F };
	private float              flapps[]      = { 0.0F, 0.0F };

	static {
		Class class1 = KI_45.class;
		Property.set(class1, "originCountry", PaintScheme.countryJapan);
	}
}
