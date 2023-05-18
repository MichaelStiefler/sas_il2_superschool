package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public abstract class PZL42xyz extends Scheme1 implements TypeStormovik {

	public void moveWheelSink() {
		this.resetYPRmodifier();
		Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.35F, 0.0F, 0.3F);
		this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.35F, 0.0F, 0.3F);
		this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			case 0:
				if (f < -130F) {
					f = -130F;
					flag = false;
				}
				if (f > 130F) {
					f = 130F;
					flag = false;
				}
				if (f1 < -15F) {
					f1 = -15F;
					flag = false;
				}
				if (f1 > 50F) {
					f1 = 50F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -20F * f, 0.0F);
	}

	protected void moveFlap(float f) {
		float f1 = -38F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
			this.hierMesh().chunkVisible("HMask2_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
			this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
		}
	}

	public void doKillPilot(int i, float f) {
		switch (i) {
			case 1:
				this.FM.turret[0].setHealth(f);
				break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
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
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxcontrols")) {
				if (s.endsWith("s1") && this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
					this.FM.AS.setControlsDamage(shot.initiator, 2);
					this.FM.AS.setControlsDamage(shot.initiator, 1);
					this.FM.AS.setControlsDamage(shot.initiator, 0);
				}
				if (s.endsWith("s2") && this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
					this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
					this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
					Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
				}
				if (s.endsWith("s3") && this.getEnergyPastArmor(6.8F, shot) > 0.0F) {
					this.FM.AS.setControlsDamage(shot.initiator, 1);
					Aircraft.debugprintln(this, "*** Elevator Crank: Hit, Controls Destroyed..");
				}
				if ((s.endsWith("s4") || s.endsWith("s5")) && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
					this.FM.AS.setControlsDamage(shot.initiator, 1);
					Aircraft.debugprintln(this, "*** Evelator Controls Out..");
				}
				if ((s.endsWith("s6") || s.endsWith("s7") || s.endsWith("s10") || s.endsWith("s11")) && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
					this.FM.AS.setControlsDamage(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Aileron Controls Out..");
				}
				if ((s.endsWith("s8") || s.endsWith("s9")) && this.getEnergyPastArmor(6.75F, shot) > 0.0F) {
					this.FM.AS.setControlsDamage(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Aileron Cranks Destroyed..");
				}
				return;
			}
			if (s.startsWith("xxeng1")) {
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 140000F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 85000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
						}
					} else if (World.Rnd().nextFloat() < 0.01F) this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
					else {
						this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.002F);
						Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
					}
					this.getEnergyPastArmor(12F, shot);
				}
				if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.75F) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
						Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 48000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
						}
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.startsWith("xxeng1mag")) {
					int i = s.charAt(9) - 49;
					this.debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
					this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
				}
				if (s.endsWith("oil1")) {
					if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
					this.FM.AS.hitOil(shot.initiator, 0);
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				if ((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && this.chunkDamageVisible("Tail1") > 2
						&& this.getEnergyPastArmor(3.5F / (float) Math.sqrt(((Tuple3d) Aircraft.v1).y * ((Tuple3d) Aircraft.v1).y + ((Tuple3d) Aircraft.v1).z * ((Tuple3d) Aircraft.v1).z), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
					this.nextDMGLevels(1, 2, "Tail1_D2", shot.initiator);
				}
				if ((s.endsWith("li1") || s.endsWith("li2")) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
				}
				if ((s.endsWith("ri1") || s.endsWith("ri2")) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
				}
				if ((s.endsWith("lm1") || s.endsWith("lm2")) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D2", shot.initiator);
				}
				if ((s.endsWith("rm1") || s.endsWith("rm2")) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D2", shot.initiator);
				}
				if ((s.endsWith("lo1") || s.endsWith("lo2")) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
				}
				if ((s.endsWith("ro1") || s.endsWith("ro2")) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxtank")) {
				int j = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.FM.AS.hitTank(shot.initiator, j, 1);
					if (World.Rnd().nextFloat() < 0.05F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F) this.FM.AS.hitTank(shot.initiator, j, 2);
				}
				return;
			}
			if (s.startsWith("xxlock")) {
				this.debuggunnery("Lock Construction: Hit..");
				if (s.startsWith("xxlockr1") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
					this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
				}
				if (s.startsWith("xxlockr2") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
					this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
				}
				if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
				}
				if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxmgun")) {
				if (s.endsWith("01")) {
					Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
					this.FM.AS.setJamBullets(0, 0);
				}
				if (s.endsWith("02")) {
					Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
					this.FM.AS.setJamBullets(0, 1);
				}
				this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
			}
			return;
		}
		if (s.startsWith("xcf")) {
			this.hitChunk("CF", shot);
			if (point3d.x > 0.305D && point3d.x < 1.597D) {
				if (point3d.x > 1.202D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
				if (((Tuple3d) Aircraft.v1).x < -0.8D && World.Rnd().nextFloat() < 0.2F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
				if (point3d.z > 0.577D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
				else if (point3d.y > 0.0D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
				else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
				if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
				if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
			}
		} else if (!s.startsWith("xblister")) if (s.startsWith("xeng")) this.hitChunk("Engine1", shot);
		else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 2) this.hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) {
			if (s.startsWith("xkeel1") && this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
			if (s.startsWith("xkeel2") && this.chunkDamageVisible("Keel2") < 2) this.hitChunk("Keel2", shot);
		} else if (s.startsWith("xrudder")) {
			if (s.startsWith("xrudder1") && this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
			if (s.startsWith("xrudder2") && this.chunkDamageVisible("Rudder2") < 2) this.hitChunk("Rudder2", shot);
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
			if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
			if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
		} else if (s.startsWith("xwing")) {
			if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 2) this.hitChunk("WingLIn", shot);
			if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 2) this.hitChunk("WingRIn", shot);
			if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 2) this.hitChunk("WingLMid", shot);
			if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 2) this.hitChunk("WingRMid", shot);
			if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 2) this.hitChunk("WingLOut", shot);
			if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 2) this.hitChunk("WingROut", shot);
		} else if (s.startsWith("xarone")) {
			if (s.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 2) this.hitChunk("AroneL", shot);
			if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneR") < 2) this.hitChunk("AroneR", shot);
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int k;
			if (s.endsWith("a")) {
				byte0 = 1;
				k = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				k = s.charAt(6) - 49;
			} else k = s.charAt(5) - 49;
			this.hitFlesh(k, shot, byte0);
		}
	}

	static {
		Class class1 = PZL42xyz.class;
		Property.set(class1, "originCountry", PaintScheme.countryPoland);
	}
}
