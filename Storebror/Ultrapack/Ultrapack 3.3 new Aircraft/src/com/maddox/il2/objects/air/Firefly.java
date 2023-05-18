package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class Firefly extends Scheme1 implements TypeFighter {

	public Firefly() {
		this.kangle = 0.0F;
		this.obsLookTime = 0;
		this.obsLookAzimuth = 0.0F;
		this.obsLookElevation = 0.0F;
		this.obsAzimuth = 0.0F;
		this.obsElevation = 0.0F;
		this.obsAzimuthOld = 0.0F;
		this.obsElevationOld = 0.0F;
		this.obsMove = 0.0F;
		this.obsMoveTot = 0.0F;
		this.bObserverKilled = false;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (!this.bObserverKilled) if (this.obsLookTime == 0) {
			this.obsLookTime = 2 + World.Rnd().nextInt(1, 3);
			this.obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
			this.obsMove = 0.0F;
			this.obsAzimuthOld = this.obsAzimuth;
			this.obsElevationOld = this.obsElevation;
			if (World.Rnd().nextFloat() > 0.8D) {
				this.obsAzimuth = 0.0F;
				this.obsElevation = 0.0F;
			} else {
				this.obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
				this.obsElevation = World.Rnd().nextFloat() * 50F - 20F;
			}
		} else this.obsLookTime--;
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
			this.hierMesh().chunkVisible("HMask2_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
			this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
		}
	}

	protected void moveFlap(float f) {
		float f1 = -85F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.6F, 0.0F, -95F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -95F), 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.247F, 0.0F, 0.247F);
		this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.247F, 0.0F, 0.247F);
		this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
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
				this.hierMesh().chunkVisible("Head2_D0", false);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				this.bObserverKilled = true;
				break;
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1")) this.getEnergyPastArmor(6.78F, shot);
				else if (s.endsWith("g1")) this.getEnergyPastArmor(9.96F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
				else if (s.endsWith("g2")) this.getEnergyPastArmor(World.Rnd().nextFloat(30F, 50F) / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
				return;
			}
			if (s.startsWith("xxcontrols")) {
				if (s.endsWith("1")) {
					if (World.Rnd().nextFloat() < 0.12F) {
						this.FM.AS.setControlsDamage(shot.initiator, 1);
						Aircraft.debugprintln(this, "*** Evelator Controls Out..");
					}
					if (World.Rnd().nextFloat() < 0.12F) {
						this.FM.AS.setControlsDamage(shot.initiator, 2);
						Aircraft.debugprintln(this, "*** Rudder Controls Out..");
					}
					if (World.Rnd().nextFloat() < 0.12F) {
						this.FM.AS.setControlsDamage(shot.initiator, 0);
						Aircraft.debugprintln(this, "*** Arone Controls Out..");
					}
				} else if (s.endsWith("2")) {
					if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
						this.FM.AS.setControlsDamage(shot.initiator, 1);
						Aircraft.debugprintln(this, "*** Evelator Controls Out..");
					}
				} else if (s.endsWith("3") && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
					this.FM.AS.setControlsDamage(shot.initiator, 2);
					Aircraft.debugprintln(this, "*** Rudder Controls Out..");
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				if ((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && this.chunkDamageVisible("Tail1") > 2
						&& this.getEnergyPastArmor(3.5F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
					this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				}
				if ((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if ((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if ((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if ((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if ((s.endsWith("lo1") || s.endsWith("lo2")) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if ((s.endsWith("ro1") || s.endsWith("ro2")) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if ((s.endsWith("sl1") || s.endsWith("sl2") || s.endsWith("sl3") || s.endsWith("sl4") || s.endsWith("sl5")) && this.chunkDamageVisible("StabL") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
					this.nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
				}
				if ((s.endsWith("sr1") || s.endsWith("sr2") || s.endsWith("sr3") || s.endsWith("sr4") || s.endsWith("sr5")) && this.chunkDamageVisible("StabR") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
					this.nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
				}
				if (s.endsWith("e1")) this.getEnergyPastArmor(6F, shot);
				return;
			}
			if (s.startsWith("xxeng1")) {
				if (s.endsWith("prp") && this.getEnergyPastArmor(0.1F, shot) > 0.0F) this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
				if (s.endsWith("cas") && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
					if (World.Rnd().nextFloat() < shot.power / 200000F) {
						this.FM.AS.setEngineStuck(shot.initiator, 0);
						Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
					}
					if (World.Rnd().nextFloat() < shot.power / 50000F) {
						this.FM.AS.hitEngine(shot.initiator, 0, 2);
						Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
					}
					if (World.Rnd().nextFloat() < shot.power / 28000F) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
						Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
					}
					this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
					Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
				}
				if (s.endsWith("cyl") && this.getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.75F) {
					this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
					Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
					if (this.FM.AS.astateEngineStates[0] < 1) this.FM.AS.hitEngine(shot.initiator, 0, 1);
					if (World.Rnd().nextFloat() < shot.power / 24000F) {
						this.FM.AS.hitEngine(shot.initiator, 0, 3);
						Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("sup") && this.getEnergyPastArmor(0.05F, shot) > 0.0F) this.FM.EI.engines[0].setKillCompressor(shot.initiator);
				return;
			}
			if (s.startsWith("xxtank")) {
				int i = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.FM.AS.hitTank(shot.initiator, i, 1);
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F) this.FM.AS.hitTank(shot.initiator, i, 2);
				}
				return;
			}
			if (s.startsWith("xxmgunl1") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(0, 0);
			if (s.startsWith("xxmgunr1") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(0, 1);
			if (s.startsWith("xxmgunl2") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(0, 2);
			if (s.startsWith("xxmgunr2") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(0, 3);
			if (s.startsWith("xxhispa1") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 0);
			if (s.startsWith("xxhispa2") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 1);
			if (s.startsWith("xxhispa3") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 2);
			if (s.startsWith("xxhispa4") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) this.FM.AS.setJamBullets(1, 3);
			return;
		}
		if (s.startsWith("xcf") || s.startsWith("xcockpit")) this.hitChunk("CF", shot);
		else if (s.startsWith("xeng")) {
			if (this.chunkDamageVisible("Engine1") < 3) this.hitChunk("Engine1", shot);
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) this.hitChunk("Keel1", shot);
		else if (s.startsWith("xrudder")) this.hitChunk("Rudder1", shot);
		else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 3) this.hitChunk("StabL", shot);
			if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 3) this.hitChunk("StabR", shot);
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl")) this.hitChunk("VatorL", shot);
			if (s.startsWith("xvatorr")) this.hitChunk("VatorR", shot);
		} else if (s.startsWith("xwing")) {
			if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
			if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
			if (s.startsWith("xwinglmid")) {
				if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
				if (World.Rnd().nextFloat() < shot.mass + 0.02F) this.FM.AS.hitOil(shot.initiator, 0);
			}
			if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
			if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
			if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
		} else if (s.startsWith("xarone")) {
			if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
			if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
		} else {
			if (s.startsWith("xoil")) {
				if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
					this.debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
					this.FM.AS.hitOil(shot.initiator, 0);
				}
				return;
			}
			if (s.startsWith("xwater")) {
				if (this.FM.AS.astateEngineStates[0] == 0) {
					this.debuggunnery("Engine Module: Water Radiator Pierced..");
					this.FM.AS.hitEngine(shot.initiator, 0, 1);
					this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
				} else if (this.FM.AS.astateEngineStates[0] == 1) {
					this.debuggunnery("Engine Module: Water Radiator Pierced..");
					this.FM.AS.hitEngine(shot.initiator, 0, 1);
					this.FM.AS.doSetEngineState(shot.initiator, 0, 2);
				}
				return;
			}
			if (s.startsWith("xpilot") || s.startsWith("xhead")) {
				byte byte0 = 0;
				int j;
				if (s.endsWith("a")) {
					byte0 = 1;
					j = s.charAt(6) - 49;
				} else if (s.endsWith("b")) {
					byte0 = 2;
					j = s.charAt(6) - 49;
				} else j = s.charAt(5) - 49;
				this.hitFlesh(j, shot, byte0);
			}
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 11:
			case 17:
			case 18:
			case 19:
				this.hierMesh().chunkVisible("Wire_D0", false);
				// fall through

			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			default:
				return super.cutFM(i, j, actor);
		}
	}

	public void update(float f) {
		if (this.obsMove < this.obsMoveTot && !this.bObserverKilled && !this.FM.AS.isPilotParatrooper(1)) {
			if (this.obsMove < 0.2F || this.obsMove > this.obsMoveTot - 0.2F) this.obsMove += 0.3D * f;
			else if (this.obsMove < 0.1F || this.obsMove > this.obsMoveTot - 0.1F) this.obsMove += 0.15F;
			else this.obsMove += 1.2D * f;
			this.obsLookAzimuth = Aircraft.cvt(this.obsMove, 0.0F, this.obsMoveTot, this.obsAzimuthOld, this.obsAzimuth);
			this.obsLookElevation = Aircraft.cvt(this.obsMove, 0.0F, this.obsMoveTot, this.obsElevationOld, this.obsElevation);
			this.hierMesh().chunkSetAngles("Head2_D0", 0.0F, this.obsLookAzimuth, this.obsLookElevation);
		}
		this.hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * this.kangle, 0.0F);
		this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
		if (this.kangle > 1.0F) this.kangle = 1.0F;
		super.update(f);
	}

	private float         kangle;
	private int           obsLookTime;
	private float         obsLookAzimuth;
	private float         obsLookElevation;
	private float         obsAzimuth;
	private float         obsElevation;
	private float         obsAzimuthOld;
	private float         obsElevationOld;
	private float         obsMove;
	private float         obsMoveTot;
	boolean               bTAGKilled;
	boolean               bObserverKilled;
	public static boolean bChangedPit = false;

	static {
		Class class1 = Firefly.class;
		Property.set(class1, "originCountry", PaintScheme.countryBritain);
	}
}
