package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public abstract class TB_3 extends Scheme4 implements TypeTransport, TypeBomber {

	public TB_3() {
		this.bDynamoOperational = true;
		this.dynamoOrient = 0.0F;
		this.bDynamoRotary = false;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.AS.wantBeaconsNet(true);
	}

	protected void moveFan(float f) {
		if (this.bDynamoOperational) {
			this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
			if (this.pk >= 1) { this.pk = 1; }
		}
		if (this.bDynamoRotary != (this.pk == 1)) {
			this.bDynamoRotary = this.pk == 1;
			this.hierMesh().chunkVisible("Gener_D0", !this.bDynamoRotary);
			this.hierMesh().chunkVisible("Generrot_D0", this.bDynamoRotary);
		}
		this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - this.FM.Vwld.length() * 1.5444015264511108D) % 360F;
		this.hierMesh().chunkSetAngles("Gener_D0", 0.0F, this.dynamoOrient, 0.0F);
		super.moveFan(f);
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		float f = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 0.5F);
		this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, 25F * f, 0.0F);
		xyz[2] = -0.21F * f;
		this.hierMesh().chunkSetLocate("GearL6_D0", xyz, ypr);
		xyz[2] = -0.69F * f;
		this.hierMesh().chunkSetLocate("GearL7_D0", xyz, ypr);
		xyz[2] = -0.87F * f;
		this.hierMesh().chunkSetLocate("GearL8_D0", xyz, ypr);
		f = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, 0.5F);
		this.hierMesh().chunkSetAngles("GearR2_D0", 0.0F, -25F * f, 0.0F);
		xyz[2] = -0.21F * f;
		this.hierMesh().chunkSetLocate("GearR6_D0", xyz, ypr);
		xyz[2] = -0.69F * f;
		this.hierMesh().chunkSetLocate("GearR7_D0", xyz, ypr);
		xyz[2] = -0.87F * f;
		this.hierMesh().chunkSetLocate("GearR8_D0", xyz, ypr);
		f = -this.FM.Or.getTangage() + 10.42765F;
		this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, f, 0.0F);
		this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, f, 0.0F);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1")) {
					this.getEnergyPastArmor(0.78F, shot);
				} else if (s.endsWith("p2")) {
					this.getEnergyPastArmor(0.78F, shot);
				} else if (s.endsWith("g1") || s.endsWith("g2")) { this.getEnergyPastArmor(2.0F * World.Rnd().nextFloat(0.9F, 1.21F), shot); }
			}
			if (s.startsWith("xxcontrols")) {
				if (World.Rnd().nextFloat() < 0.12F) {
					this.FM.AS.setControlsDamage(shot.initiator, 1);
					debugprintln(this, "*** Evelator Controls Out..");
				}
				if (World.Rnd().nextFloat() < 0.12F) {
					this.FM.AS.setControlsDamage(shot.initiator, 2);
					debugprintln(this, "*** Rudder Controls Out..");
				}
			}
			if (s.startsWith("xxspar")) {
				if ((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && World.Rnd().nextFloat() < 0.1F && this.chunkDamageVisible("Tail1") > 2
						&& this.getEnergyPastArmor(12.9F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F) {
					debugprintln(this, "*** Tail1 Spars Broken in Half..");
					this.msgCollision(this, "Tail1_D0", "Tail1_D0");
				}
				if ((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && World.Rnd().nextFloat() < 0.25F && this.chunkDamageVisible("WingLIn") > 2
						&& this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if ((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && World.Rnd().nextFloat() < 0.25F && this.chunkDamageVisible("WingRIn") > 2
						&& this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if ((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && World.Rnd().nextFloat() < 0.25F && this.chunkDamageVisible("WingLMid") > 2
						&& this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if ((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && World.Rnd().nextFloat() < 0.25F && this.chunkDamageVisible("WingRMid") > 2
						&& this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if ((s.endsWith("lo1") || s.endsWith("lo2") || s.endsWith("lo3") || s.endsWith("lo4")) && World.Rnd().nextFloat() < 0.25F && this.chunkDamageVisible("WingLOut") > 2
						&& this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if ((s.endsWith("ro1") || s.endsWith("ro2") || s.endsWith("ro3") || s.endsWith("ro4")) && World.Rnd().nextFloat() < 0.25F && this.chunkDamageVisible("WingROut") > 2
						&& this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if (s.endsWith("e1") && this.getEnergyPastArmor(28F, shot) > 0.0F && World.Rnd().nextFloat() < 0.45F) {
					debugprintln(this, "*** Engine1 Suspension Broken in Half..");
					this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
				}
				if (s.endsWith("e2") && this.getEnergyPastArmor(28F, shot) > 0.0F && World.Rnd().nextFloat() < 0.45F) {
					debugprintln(this, "*** Engine2 Suspension Broken in Half..");
					this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
				}
				if (s.endsWith("e3") && this.getEnergyPastArmor(28F, shot) > 0.0F && World.Rnd().nextFloat() < 0.45F) {
					debugprintln(this, "*** Engine3 Suspension Broken in Half..");
					this.nextDMGLevels(3, 2, "Engine3_D0", shot.initiator);
				}
				if (s.endsWith("e4") && this.getEnergyPastArmor(28F, shot) > 0.0F && World.Rnd().nextFloat() < 0.45F) {
					debugprintln(this, "*** Engine4 Suspension Broken in Half..");
					this.nextDMGLevels(3, 2, "Engine4_D0", shot.initiator);
				}
			}
			if (s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
				debugprintln(this, "*** Bomb Payload Detonates..");
				this.FM.AS.hitTank(shot.initiator, 0, 10);
				this.FM.AS.hitTank(shot.initiator, 1, 10);
				this.FM.AS.hitTank(shot.initiator, 2, 10);
				this.FM.AS.hitTank(shot.initiator, 3, 10);
				this.msgCollision(this, "CF_D0", "CF_D0");
			}
			if (s.startsWith("xxeng")) {
				int i = s.charAt(5) - 49;
				if (s.endsWith("base")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 200000F) {
							this.FM.AS.setEngineStuck(shot.initiator, i);
							debugprintln(this, "*** Engine" + (i + 1) + " Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, i, 2);
							debugprintln(this, "*** Engine" + (i + 1) + " Crank Case Hit - Engine Damaged..");
						}
						if (World.Rnd().nextFloat() < shot.power / 14000F) {
							this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
							debugprintln(this, "*** Engine" + (i + 1) + " Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
						}
						this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
						debugprintln(this, "*** Engine" + (i + 1) + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[i].getReadyness() + "..");
					}
				} else if (s.endsWith("cyl")) {
					if (this.getEnergyPastArmor(0.5F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio()) {
						this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
						debugprintln(this, "*** Engine" + (i + 1) + " Cylinders Hit, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
						if (this.FM.AS.astateEngineStates[i] < 1) { this.FM.AS.hitEngine(shot.initiator, i, 1); }
						if (World.Rnd().nextFloat() < shot.power / 24000F) {
							this.FM.AS.hitEngine(shot.initiator, i, 3);
							debugprintln(this, "*** Engine" + (i + 1) + " Cylinders Hit - Engine Fires..");
						}
						this.getEnergyPastArmor(25F, shot);
					}
				} else if (s.endsWith("wat")) {
					if (World.Rnd().nextFloat() < 0.03F) {
						this.FM.EI.engines[i].setMagnetoKnockOut(shot.initiator, 0);
						this.FM.EI.engines[i].setMagnetoKnockOut(shot.initiator, 1);
					}
					this.FM.AS.hitOil(shot.initiator, i);
				}
			}
			if (s.startsWith("xxoil")) {
				int j = s.charAt(5) - 49;
				if (this.getEnergyPastArmor(0.023F, shot) > 0.0F) {
					this.FM.AS.hitOil(shot.initiator, j);
					this.getEnergyPastArmor(0.12F, shot);
				}
			}
			if (s.startsWith("xxtank")) {
				int k = s.charAt(6) - 49;
				k /= 2;
				if (this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.FM.AS.hitTank(shot.initiator, k, 1);
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F) { this.FM.AS.hitTank(shot.initiator, k, 2); }
				}
			}
			return;
		}
		if (s.startsWith("xcf")) {
			this.hitChunk("CF", shot);
			if (point3d.x > 1.0D) {
				if (World.Rnd().nextFloat() < 0.05F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40); }
				if (v1.x < -0.8D && World.Rnd().nextFloat() < 0.2F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2); }
				if (v1.x < -0.89999997615814209D && World.Rnd().nextFloat() < 0.2F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1); }
				if (Math.abs(v1.x) < 0.8D) {
					if (point3d.y > 0.0D) {
						if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4); }
						if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8); }
					} else {
						if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10); }
						if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20); }
					}
				}
			}
		}
		if (s.startsWith("xtail") && this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		if (s.startsWith("xkeel") && this.chunkDamageVisible("Keel1") < 3) { this.hitChunk("Keel1", shot); }
		if (s.startsWith("xrudder") && this.chunkDamageVisible("Rudder1") < 1) { this.hitChunk("Rudder1", shot); }
		if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 3) { this.hitChunk("StabL", shot); }
		if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 3) { this.hitChunk("StabR", shot); }
		if (s.startsWith("xvatorl")) { this.hitChunk("VatorL", shot); }
		if (s.startsWith("xvatorr")) { this.hitChunk("VatorR", shot); }
		if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) { this.hitChunk("WingLIn", shot); }
		if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) { this.hitChunk("WingRIn", shot); }
		if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) { this.hitChunk("WingLMid", shot); }
		if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) { this.hitChunk("WingRMid", shot); }
		if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) { this.hitChunk("WingLOut", shot); }
		if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) { this.hitChunk("WingROut", shot); }
		if (s.startsWith("xaronel")) { this.hitChunk("AroneL", shot); }
		if (s.startsWith("xaroner")) { this.hitChunk("AroneR", shot); }
		if (s.startsWith("xgearl")) { this.hitChunk("GearL2", shot); }
		if (s.startsWith("xgearr")) { this.hitChunk("GearR2", shot); }
		if (s.startsWith("xengine1") && this.chunkDamageVisible("Engine1") < 3) { this.hitChunk("Engine1", shot); }
		if (s.startsWith("xengine2") && this.chunkDamageVisible("Engine2") < 3) { this.hitChunk("Engine2", shot); }
		if (s.startsWith("xengine3") && this.chunkDamageVisible("Engine3") < 3) { this.hitChunk("Engine3", shot); }
		if (s.startsWith("xengine4") && this.chunkDamageVisible("Engine4") < 3) { this.hitChunk("Engine4", shot); }
		if (s.startsWith("xturret")) {
			if (s.startsWith("xturret1")) {
				this.FM.AS.setJamBullets(10, 0);
				this.FM.AS.setJamBullets(10, 1);
			}
			if (s.startsWith("xturret2")) {
				this.FM.AS.setJamBullets(11, 0);
				this.FM.AS.setJamBullets(11, 1);
			}
			if (s.startsWith("xturret3")) {
				this.FM.AS.setJamBullets(11, 0);
				this.FM.AS.setJamBullets(11, 1);
			}
		}
		if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int l;
			if (s.endsWith("a")) {
				byte0 = 1;
				l = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				l = s.charAt(6) - 49;
			} else {
				l = s.charAt(5) - 49;
			}
			this.hitFlesh(l, shot, byte0);
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 33:
				this.hitProp(0, j, actor);
				this.hitProp(1, j, actor);
				break;

			case 36:
				this.hitProp(2, j, actor);
				this.hitProp(3, j, actor);
				break;

			case 19:
				this.killPilot(this, 5);
				this.killPilot(this, 6);
				return false;
		}
		return super.cutFM(i, j, actor);
	}

	protected void moveFlap(float f) {
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
			case 2:
				this.FM.turret[0].setHealth(f);
				break;

			case 5:
				this.FM.turret[1].setHealth(f);
				break;

			case 6:
				this.FM.turret[2].setHealth(f);
				break;
		}
	}

	public void doMurderPilot(int i) {
		if (i != 3 && i != 4 && i != 7) { this.hierMesh().chunkSetAngles("Pilot" + (i + 1) + "_D0", 0.0F, 10F, -25F); }
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag && (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode())) {
			for (int i = 0; i < this.FM.EI.getNum(); i++) {
				if (this.FM.AS.astateEngineStates[i] > 3 && World.Rnd().nextFloat() < 0.2F) { this.FM.EI.engines[i].setExtinguisherFire(); }
			}

		}
	}

	protected void moveBayDoor(float f) {
		this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -90F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -90F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, -90F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -90F * f, 0.0F);
	}

	private boolean bDynamoOperational;
	private float   dynamoOrient;
	private boolean bDynamoRotary;
	private int     pk;

	static {
		Class class1 = TB_3.class;
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
	}
}
