package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class E13A extends Scheme1 implements TypeSailPlane, TypeScout, TypeStormovik {

	public E13A() {
		this.bGunUp = false;
		this.btme = -1L;
		this.fGunPos = 0.0F;
		this.flapps = 0.0F;
	}

	public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
		super.setOnGround(point3d, orient, vector3d);
		if (this.FM.isPlayers()) {
			this.FM.CT.bHasCockpitDoorControl = true;
			this.FM.CT.dvCockpitDoor = 0.5F;
			this.FM.CT.cockpitDoorControl = 1.0F;
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 9:
			case 33:
				this.FM.Gears.bIsSail = false;
				break;

			case 10:
			case 36:
				this.FM.Gears.bIsSail = false;
				break;
		}
		return super.cutFM(i, j, actor);
	}

	public void moveCockpitDoor(float f) {
		this.resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.65F);
		this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
	}

	protected void moveFlap(float f) {
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
	}

	protected void moveElevator(float f) {
		this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
	}

	public static void moveGear(HierMesh hiermesh1, float f3, float f4, float f5) {
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(this.hierMesh(), f, f1, f2);
	}

	public void moveSteering(float f1) {
	}

	public void moveWheelSink() {
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("GearR12_D0", 0.0F, -30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("GearL12_D0", 0.0F, -30F * f, 0.0F);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 5; i++)
			if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		float f2 = -30F;
		if (f < 0.0F) {
			if (f < -20F) f2 = Aircraft.cvt(f, -41F, -20F, -30F, -15F);
			else f2 = Aircraft.cvt(f, -20F, -10F, -15F, -8F);
		} else if (f > 20F) f2 = Aircraft.cvt(f, 20F, 41F, -15F, -30F);
		else f2 = Aircraft.cvt(f, 10F, 20F, -8F, -15F);
		switch (i) {
			case 0:
				if (f < -54F) {
					f = -54F;
					flag = false;
				}
				if (f > 54F) {
					f = 54F;
					flag = false;
				}
				if (f1 < f2) {
					f1 = f2;
					flag = false;
				}
				if (f1 > 55F) {
					f1 = 55F;
					flag = false;
				}
				if (f > -0.9F && f < 0.9F && f1 < 15.5F) flag = false;
				if (f > -32F && f < 32F && f1 < -8F && f1 > -15F) flag = false;
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
			case 2:
				this.FM.turret[0].setHealth(f);
				break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
			default:
				break;

			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				break;

			case 2:
			case 3:
				if (this.hierMesh().isChunkVisible("Pilot3_D0")) {
					this.hierMesh().chunkVisible("Pilot3_D0", false);
					this.hierMesh().chunkVisible("Pilot3_D1", true);
					this.hierMesh().chunkVisible("HMask3_D0", false);
				} else {
					this.hierMesh().chunkVisible("Pilot4_D0", false);
					this.hierMesh().chunkVisible("Pilot4_D1", true);
					this.hierMesh().chunkVisible("HMask4_D0", false);
				}
				break;
		}
	}

	protected void moveWingFold(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("WingLFold_D0", 0.0F, -156F * f, 0.0F);
		hiermesh.chunkSetAngles("WingRFold_D0", 0.0F, -156F * f, 0.0F);
	}

	public void moveWingFold(float f) {
		this.moveWingFold(this.hierMesh(), f);
	}

	public void update(float f) {
		super.update(f);
		if (!this.FM.isPlayers() && this.FM.Gears.onGround()) if (this.FM.EI.engines[0].getRPM() < 100F) this.FM.CT.cockpitDoorControl = 1.0F;
		else this.FM.CT.cockpitDoorControl = 0.0F;
		float f1 = this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(this.flapps - f1) > 0.01F) {
			this.flapps = f1;
			for (int i = 1; i < 14; i++) {
				String s = "Cowflap" + i + "_D0";
				this.hierMesh().chunkSetAngles(s, 0.0F, -30F * f1, 0.0F);
			}

		}
		if (!this.bGunUp) {
			if (this.fGunPos > 0.0F) {
				this.fGunPos -= 0.2F * f;
				this.FM.turret[0].bIsOperable = false;
				this.hierMesh().chunkVisible("Turret1A_D0", false);
				this.hierMesh().chunkVisible("Turret1B_D0", false);
				this.hierMesh().chunkVisible("Pilot3_D0", true);
				this.hierMesh().chunkVisible("Pilot4_D0", false);
			}
		} else if (this.fGunPos < 1.0F) {
			this.fGunPos += 0.2F * f;
			if (this.fGunPos > 0.8F && this.fGunPos < 0.9F) {
				this.FM.turret[0].bIsOperable = true;
				this.hierMesh().chunkVisible("Turret1A_D0", true);
				this.hierMesh().chunkVisible("Turret1B_D0", true);
				this.hierMesh().chunkVisible("Pilot3_D0", false);
				this.hierMesh().chunkVisible("Pilot4_D0", true);
			}
		}
		if (this.fGunPos < 0.333F) this.hierMesh().chunkSetAngles("Blister4_D0", 0.0F, -Aircraft.cvt(this.fGunPos, 0.0F, 0.333F, 0.0F, 41F), 0.0F);
		else if (this.fGunPos < 0.666F) {
			this.resetYPRmodifier();
			Aircraft.xyz[1] = Aircraft.cvt(this.fGunPos, 0.333F, 0.666F, 0.0F, -0.4F);
			this.hierMesh().chunkSetLocate("Blister3_D0", Aircraft.xyz, Aircraft.ypr);
		} else this.hierMesh().chunkSetAngles("Blister4_D0", 0.0F, -Aircraft.cvt(this.fGunPos, 0.666F, 1.0F, 41F, 71F), 0.0F);
		if (this.FM.turret[0].bIsAIControlled) {
			if (this.FM.turret[0].target != null && this.FM.AS.astatePilotStates[2] < 90) this.bGunUp = true;
			if (Time.current() > this.btme) {
				this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
				if (this.FM.turret[0].target == null && this.FM.AS.astatePilotStates[2] < 90) this.bGunUp = false;
			}
		} else this.bGunUp = true;
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxcontrols")) {
				int i = s.charAt(10) - 48;
				switch (i) {
					case 2:
					default:
						break;

					case 1:
						if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) this.FM.AS.setControlsDamage(shot.initiator, 0);
						break;

					case 3:
						if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) this.FM.AS.setControlsDamage(shot.initiator, 2);
						break;
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				if ((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && World.Rnd().nextFloat() < 0.1F && this.chunkDamageVisible("Tail1") > 2
						&& this.getEnergyPastArmor(23F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					this.debuggunnery("*** Tail1 Spars Broken in Half..");
					this.msgCollision(this, "Tail1_D0", "Tail1_D0");
				}
				if ((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if ((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if ((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if ((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if ((s.endsWith("lo1") || s.endsWith("lo2") || s.endsWith("lo3") || s.endsWith("lo4")) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if ((s.endsWith("ro1") || s.endsWith("ro2") || s.endsWith("ro3") || s.endsWith("ro4")) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxeng1")) {
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 140000F) this.FM.AS.setEngineStuck(shot.initiator, 0);
						if (World.Rnd().nextFloat() < shot.power / 50000F) this.FM.AS.hitEngine(shot.initiator, 0, 2);
					} else if (World.Rnd().nextFloat() < 0.04F) this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
					else this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.02F);
					this.getEnergyPastArmor(12F, shot);
				}
				if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.75F) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
						if (World.Rnd().nextFloat() < shot.power / 48000F) this.FM.AS.hitEngine(shot.initiator, 0, 2);
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.startsWith("xxeng1mag")) {
					int j = s.charAt(9) - 49;
					this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
				}
				if (s.endsWith("oil1")) this.FM.AS.hitOil(shot.initiator, 0);
				return;
			}
			if (s.startsWith("xxoil")) {
				this.FM.AS.hitOil(shot.initiator, 0);
				this.getEnergyPastArmor(0.22F, shot);
			}
			if (s.startsWith("xxtank")) {
				int k = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					if (this.FM.AS.astateTankStates[k] == 0) {
						this.FM.AS.hitTank(shot.initiator, k, 1);
						this.FM.AS.doSetTankState(shot.initiator, k, 1);
					}
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) this.FM.AS.hitTank(shot.initiator, k, 2);
				}
			}
			return;
		}
		if (s.startsWith("xcf")) {
			if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) {
			if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
		} else if (s.startsWith("xrudder")) this.hitChunk("Rudder1", shot);
		else if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
		else if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
		else if (s.startsWith("xvatorl")) this.hitChunk("VatorL", shot);
		else if (s.startsWith("xwinglin")) {
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
		else if (s.startsWith("xengine")) {
			if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
		} else if (s.startsWith("xturret1a")) {
			this.FM.AS.setJamBullets(10, 0);
			this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
		} else if (s.startsWith("xgearl")) this.hitChunk("GearL2", shot);
		else if (s.startsWith("xgearr")) this.hitChunk("GearR2", shot);
		else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int l;
			if (s.endsWith("a")) {
				byte0 = 1;
				l = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				l = s.charAt(6) - 49;
			} else l = s.charAt(5) - 49;
			this.hitFlesh(l, shot, byte0);
		}
	}

	boolean         bGunUp;
	public long     btme;
	public float    fGunPos;
	protected float flapps;

	static {
		Class class1 = E13A.class;
		Property.set(class1, "originCountry", PaintScheme.countryJapan);
	}
}
