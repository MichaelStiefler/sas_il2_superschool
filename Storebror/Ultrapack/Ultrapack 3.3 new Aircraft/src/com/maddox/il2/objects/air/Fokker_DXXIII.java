package com.maddox.il2.objects.air;

import java.security.SecureRandom;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class Fokker_DXXIII extends Scheme2 implements TypeFighter {

	private float[]        rndgear     = { 0.0F, 0.0F, 0.0F };
	private static float[] rndgearnull = { 0.0F, 0.0F, 0.0F };

	public Fokker_DXXIII() {
		this.canopyF = 0.0F;
		this.tiltCanopyOpened = false;
		this.slideCanopyOpened = false;
		this.blisterRemoved = new boolean[2];
		this.canopyMaxAngle = 0.8F;
		this.bChangedPit = true;
		this.gyroDelta = 0.0F;

		// Random Gear Move .. made exclusively for those two who recon it ..
		SecureRandom secRandom = new SecureRandom();
		secRandom.setSeed(System.currentTimeMillis());
		RangeRandom rr = new RangeRandom(secRandom.nextLong());
		for (int i = 0; i < this.rndgear.length; i++)
			this.rndgear[i] = rr.nextFloat(0.0F, 0.15F);
	}

	public void missionStarting() {
		super.missionStarting();
		if (this.FM.isStationedOnGround()) this.gyroDelta += (float) Math.random() * 360F;
	}

	public float getEyeLevelCorrection() {
		return -0.06F;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 3:
				this.FM.setGCenter(-1.5F);
				break;
		}
		return super.cutFM(i, j, actor);
	}

	public void moveCockpitDoor(float f) {
		if (f > this.canopyF) {
			if (this.FM.Gears.onGround() && this.FM.getSpeed() < 5.0F || this.tiltCanopyOpened && (this.FM.isPlayers() || this.isNetPlayer())) {
				this.tiltCanopyOpened = true;
				this.hierMesh().chunkSetAngles("Blister1L", 0.0F, -f * 155.0F, 0.0F);
			} else {
				this.resetYPRmodifier();
				this.slideCanopyOpened = true;
				xyz[1] = f * 0.265F;
				this.hierMesh().chunkSetLocate("Blister2L", xyz, ypr);
			}
		} else if (this.FM.Gears.onGround() && this.FM.getSpeed() < 5.0F && !this.slideCanopyOpened || this.tiltCanopyOpened) {
			this.hierMesh().chunkSetAngles("Blister1L", 0.0F, -f * 155.0F, 0.0F);
			if (this.FM.getSpeed() > 50.0F && f < 0.6F && !this.blisterRemoved[BLISTER_LEFT]) this.doRemoveBlisterLeft();
			if (f == 0.0F) this.tiltCanopyOpened = false;
		} else {
			this.resetYPRmodifier();
			xyz[1] = f * 0.265F;
			this.hierMesh().chunkSetLocate("Blister2L", xyz, ypr);
			if (f == 0.0F) this.slideCanopyOpened = false;
		}
		this.canopyF = f;
		if (this.canopyF < 0.01D) this.canopyF = 0.0F;
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
			this.setDoorSnd(f);
		}
		if (!this.blisterRemoved[BLISTER_LEFT]) this.hierMesh().chunkVisible("Blister2L", this.canopyF < 0.99F);
	}

	public void hitDaSilk() {
		super.hitDaSilk();
		if (!(this.blisterRemoved[BLISTER_LEFT] && this.blisterRemoved[BLISTER_RIGHT]) && this.FM.AS.bIsAboutToBailout && !this.FM.AS.isPilotDead(0)) this.doRemoveBlisterFull();
	}

	public void blisterRemoved(int i) {
		if (i == 1) this.doRemoveBlisterFull();
	}

	private final void doRemoveBlisterFull() {
		if (this.hierMesh().chunkFindCheck("Blister1_D0") != -1) {
			this.hierMesh().hideSubTrees("Blister1_D0");
			Wreckage localWreckage;
			Vector3d localVector3d;
			if (!this.blisterRemoved[BLISTER_LEFT]) {
				localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1L"));
				localWreckage.collide(true);
				localVector3d = new Vector3d();
				localVector3d.set(this.FM.Vwld);
				localWreckage.setSpeed(localVector3d);
				localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister2L"));
				localWreckage.collide(true);
				localVector3d = new Vector3d();
				localVector3d.set(this.FM.Vwld);
				localWreckage.setSpeed(localVector3d);
				this.blisterRemoved[BLISTER_LEFT] = true;
			}
			if (!this.blisterRemoved[BLISTER_RIGHT]) {
				localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1R"));
				localWreckage.collide(true);
				localVector3d = new Vector3d();
				localVector3d.set(this.FM.Vwld);
				localWreckage.setSpeed(localVector3d);
				this.blisterRemoved[BLISTER_RIGHT] = true;
			}
		}
	}

	private final void doRemoveBlisterLeft() {
		this.FM.CT.bHasCockpitDoorControl = false;
		this.bChangedPit = true;
		Wreckage localWreckage;
		Vector3d localVector3d;
		if (this.hierMesh().chunkFindCheck("Blister1L") != -1) {
			this.hierMesh().hideSubTrees("Blister1L");
			if (!this.blisterRemoved[BLISTER_LEFT]) {
				localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1L"));
				localWreckage.collide(true);
				localVector3d = new Vector3d();
				localVector3d.set(this.FM.Vwld);
				localWreckage.setSpeed(localVector3d);
				localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister2L"));
				localWreckage.collide(true);
				localVector3d = new Vector3d();
				localVector3d.set(this.FM.Vwld);
				localWreckage.setSpeed(localVector3d);
				this.blisterRemoved[BLISTER_LEFT] = true;
			}
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2, float[] rnd) {
		myResetYPRmodifier();
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.1F + rnd[2], 0.85F + rnd[2], 3.0F, -115.5F), 0.0F);
		hiermesh.chunkSetAngles("GearC7_D0", 0.0F, Aircraft.cvt(f2, 0.1F + rnd[2], 0.85F + rnd[2], 0.0F, -137F), 0.0F);
		hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f2, 0.1F + rnd[2], 0.85F + rnd[2], 0.0F, -148.5F), 0.0F);
		hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f2, 0.1F + rnd[2], 0.85F + rnd[2], 0.0F, 1.0F), 0.0F);
		Aircraft.xyz[1] = Aircraft.cvt(f2, 0.1F + rnd[2], 0.85F + rnd[2], 0.0F, 0.09F);
		hiermesh.chunkSetLocate("GearC10_D0", Aircraft.xyz, Aircraft.ypr);
		hiermesh.chunkSetAngles("GearC11_D0", 0.0F, Aircraft.cvt(f2, 0.1F + rnd[2], 0.2F + rnd[2], 0.0F, 100F), 0.0F);
		hiermesh.chunkSetAngles("GearC12_D0", 0.0F, Aircraft.cvt(f2, 0.1F + rnd[2], 0.2F + rnd[2], 0.0F, -100F), 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.1F + rnd[0], 0.85F + rnd[0], 0.0F, -86F), 0.0F);
		hiermesh.chunkSetAngles("GearL10_D0", 0.0F, Aircraft.cvt(f, 0.1F + rnd[0], 0.2071F + rnd[0], 0.0F, -110F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.1F + rnd[1], 0.85F + rnd[1], 0.0F, -86F), 0.0F);
		hiermesh.chunkSetAngles("GearR10_D0", 0.0F, Aircraft.cvt(f1, 0.1F + rnd[1], 0.2071F + rnd[1], 0.0F, -110F), 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(this.hierMesh(), f, f1, f2, this.rndgear);
	}

	private static void myResetYPRmodifier() {
		Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
	}

	// compatibility stuff:

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		moveGear(hiermesh, f, f1, f2, rndgearnull);
	}

	public static void moveGear(HierMesh hiermesh, float gearPos) {
		moveGear(hiermesh, gearPos, gearPos, gearPos, rndgearnull);
	}

	protected void moveGear(float gearPos) {
		moveGear(this.hierMesh(), gearPos, gearPos, gearPos, this.rndgear);
	}

	// end of gear move area ---------------------------------------------------------------------------------------------------------

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC4_D0", 0.0F, -f, 0.0F);
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.4719F, 0.0F, 0.52255F);
		this.hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.4719F, 0.0F, -65F), 0.0F);
		this.hierMesh().chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.4719F, 0.0F, -130F), 0.0F);
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.27625F, 0.0F, 0.27625F);
		this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.27625F, 0.0F, 0.27625F);
		this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
		else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
		if (this.tiltCanopyOpened && !this.blisterRemoved[BLISTER_LEFT] && this.FM.getSpeed() > 75.0F) this.doRemoveBlisterLeft();
		if (this.FM.Or.getKren() < -10F || this.FM.Or.getKren() > 10F) this.gyroDelta -= 0.01D;
	}

	public void auxPlus(int i) {
		switch (i) {
			case 1:
				this.gyroDelta++;
				break;
		}
	}

	public void auxMinus(int i) {
		switch (i) {
			case 1:
				this.gyroDelta--;
				break;
		}
	}

	protected void moveFlap(float f) {
		float f1 = -45F * f;
		this.hierMesh().chunkSetAngles("Flap1_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap2_D0", 0.0F, f1, 0.0F);
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30F * f, 0.0F);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xxcannon04")) {
			this.debuggunnery("Armament System: Left Wing Cannon: Disabled..");
			this.FM.AS.setJamBullets(1, 1);
			this.getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
			return;
		}
		if (s.startsWith("xxcannon05")) {
			this.debuggunnery("Armament System: Right Wing Cannon: Disabled..");
			this.FM.AS.setJamBullets(1, 2);
			this.getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
			return;
		} else if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1")) {
					this.getEnergyPastArmor(35.89D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
					if (shot.power <= 0.0F) this.doRicochetBack(shot);
				} else if (s.endsWith("p2")) this.getEnergyPastArmor(12.71D, shot);
				else if (s.endsWith("p3")) this.getEnergyPastArmor(12.71D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
				return;
			}
			if (s.startsWith("xxcannon")) {
				if (s.endsWith("01")) {
					this.debuggunnery("Armament System: Left Cannon: Disabled..");
					this.FM.AS.setJamBullets(0, 0);
				}
				if (s.endsWith("02")) {
					this.debuggunnery("Armament System: Right Cannon: Disabled..");
					this.FM.AS.setJamBullets(0, 1);
				}
				this.getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
				return;
			}
			if (s.startsWith("xxcontrols")) {
				int i = s.charAt(10) - 48;
				switch (i) {
					default:
						break;

					case 1:
					case 4:
						if (this.getEnergyPastArmor(1.2F, shot) > 0.0F) {
							this.FM.AS.setControlsDamage(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
						}
						break;

					case 2:
					case 3:
						if (this.getEnergyPastArmor(4D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
							this.debuggunnery("Controls: Aileron Wiring Hit, Aileron Controls Disabled..");
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 5:
					case 6:
						if (this.getEnergyPastArmor(1.0F, shot) <= 0.0F) break;
						if (World.Rnd().nextFloat() < 0.12F) {
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.debuggunnery("Evelator Controls Out..");
						}
						if (World.Rnd().nextFloat() < 0.12F) {
							this.FM.AS.setControlsDamage(shot.initiator, 2);
							this.debuggunnery("Rudder Controls Out..");
						}
						break;
				}
				return;
			}
			if (s.startsWith("xxeng")) {
				int j = s.charAt(5) - 49;
				Aircraft.debugprintln(this, "*** Engine Module (" + j + "): Hit..");
				if (s.endsWith("prop") || s.endsWith("prop3")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) if (World.Rnd().nextFloat() < 0.5F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 3);
						Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
					} else {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
						Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Damaged..");
					}
				} else if (s.endsWith("prop1") || s.endsWith("prop2")) {
					if (this.getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) {
						this.FM.AS.setInternalDamage(shot.initiator, 5);
						Aircraft.debugprintln(this, "*** Engine Module: Drive Shaft Damaged..");
					}
				} else if (s.endsWith("gear")) {
					if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) if (World.Rnd().nextFloat() < 0.5F) {
						this.FM.EI.engines[j].setEngineStuck(shot.initiator);
						Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Reductor Gear..");
					} else {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 3);
						this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
						Aircraft.debugprintln(this, "*** Engine Module: Reductor Gear Damaged, Prop Governor Failed..");
					}
				} else if (s.endsWith("supc")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
						Aircraft.debugprintln(this, "*** Engine Module: Supercharger Disabled..");
					}
				} else if (s.endsWith("feed")) {
					if (this.getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F && this.FM.EI.engines[j].getPowerOutput() > 0.7F) {
						this.FM.AS.hitEngine(shot.initiator, j, 100);
						Aircraft.debugprintln(this, "*** Engine Module: Pressurized Fuel Line Pierced, Fuel Flamed..");
					}
				} else if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 175000F) {
							this.FM.AS.setEngineStuck(shot.initiator, j);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, j, 2);
							Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
						}
						this.FM.EI.engines[j].setReadyness(shot.initiator, this.FM.EI.engines[j].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
						Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
					}
					this.getEnergyPastArmor(22.5F, shot);
				} else if (s.startsWith("xxeng1cyl") || s.startsWith("xxeng2cyl")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 1.75F) {
						this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
						Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 24000F) {
							this.FM.AS.hitEngine(shot.initiator, j, 3);
							Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
						}
						if (World.Rnd().nextFloat() < 0.01F) {
							this.FM.AS.setEngineStuck(shot.initiator, j);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
						}
						this.getEnergyPastArmor(22.5F, shot);
					}
				} else if (s.startsWith("xxeng1mag") || s.startsWith("xxeng2mag")) {
					int l = s.charAt(9) - 49;
					this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, l);
					Aircraft.debugprintln(this, "*** Engine Module: Magneto " + l + " Destroyed..");
				} else if (s.endsWith("sync")) {
					if (this.getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
						Aircraft.debugprintln(this, "*** Engine Module: Gun Synchronized Hit, Nose Guns Lose Authority..");
						this.FM.AS.setJamBullets(0, 0);
						this.FM.AS.setJamBullets(0, 1);
					}
				} else if (s.endsWith("oil1")) {
					this.FM.AS.hitOil(shot.initiator, j);
					Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
				}
				return;
			}
			if (s.startsWith("xxlock")) {
				this.debuggunnery("Lock Construction: Hit..");
				if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
					this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
				}
				if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
				}
				if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				this.debuggunnery("Spar Construction: Hit..");
				if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.86F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxtank")) {
				int k = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) if (shot.power < 14100F) {
					if (this.FM.AS.astateTankStates[k] == 0) {
						this.FM.AS.hitTank(shot.initiator, k, 1);
						this.FM.AS.doSetTankState(shot.initiator, k, 1);
					}
					if (World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(shot.initiator, k, 1);
					if (shot.powerType == 3 && this.FM.AS.astateTankStates[k] > 2 && World.Rnd().nextFloat() < 0.4F) this.FM.AS.hitTank(shot.initiator, k, 10);
				} else this.FM.AS.hitTank(shot.initiator, k, World.Rnd().nextInt(0, (int) (shot.power / 56000F)));
				return;
			} else return;
		}
		if (s.startsWith("xcf")) {
			if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
		} else if (s.startsWith("xcockpit")) {
			if (point3d.z > 0.4D) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
				if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
			} else if (point3d.y > 0.0D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
			else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
			if (point3d.x > 0.2D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel1")) {
			if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
		} else if (s.startsWith("xkeel2")) {
			if (this.chunkDamageVisible("Keel2") < 2) this.hitChunk("Keel2", shot);
		} else if (s.startsWith("xrudder1")) {
			if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
		} else if (s.startsWith("xrudder2")) {
			if (this.chunkDamageVisible("Rudder2") < 1) this.hitChunk("Rudder2", shot);
		} else if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
		else if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
		else if (s.startsWith("xvatorl")) {
			if (this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
		} else if (s.startsWith("xvatorr")) {
			if (this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
		} else if (s.startsWith("xwinglin")) {
			if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
		} else if (s.startsWith("xwingrin")) {
			if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
		} else if (s.startsWith("xwinglmid")) {
			if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
		} else if (s.startsWith("xwingrmid")) {
			if (this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
		} else if (s.startsWith("xwinglout")) {
			if (this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
		} else if (s.startsWith("xwingrout")) {
			if (this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
		} else if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
		else if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
		else if (s.startsWith("xengine1")) {
			if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
		} else if (s.startsWith("xgear")) {
			if (World.Rnd().nextFloat() < 0.1F) {
				this.debuggunnery("*** Gear Hydro Failed..");
				this.FM.Gears.setHydroOperable(false);
			}
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int i1;
			if (s.endsWith("a")) {
				byte0 = 1;
				i1 = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				i1 = s.charAt(6) - 49;
			} else i1 = s.charAt(5) - 49;
			this.hitFlesh(i1, shot, byte0);
		}
	}

	protected static final int BLISTER_LEFT  = 0;
	private static final int   BLISTER_RIGHT = 1;

	public float               canopyF;
	public boolean             tiltCanopyOpened;
	private boolean            slideCanopyOpened;
	public boolean             blisterRemoved[];
	public float               canopyMaxAngle;
	public boolean             bChangedPit;
	public float               gyroDelta;

	static {
		Class class1 = Fokker_DXXIII.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
		Property.set(class1, "iconFar_shortClassName", "D.XXIII");
		Property.set(class1, "meshName", "3DO/Plane/Fokker-DXXIII/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1935F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/Fokker_DXXIII.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitFokker_DXXIII.class });
		Property.set(class1, "LOSElevation", 1.00705F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
	}
}
