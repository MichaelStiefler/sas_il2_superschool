package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class KI_46 extends Scheme2 {

	public KI_46() {
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		xyz[0] = xyz[1] = xyz[2] = ypr[0] = ypr[1] = ypr[2] = 0.0F;
		xyz[1] = 0.415F * f;
		hiermesh.chunkSetLocate("GearC2_D0", xyz, ypr);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, cvt(f, 0.01F, 0.05F, 0.0F, -50F), 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f, 0.01F, 0.05F, 0.0F, -50F), 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.05F, 0.88F, 0.0F, -120F), 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.05F, 0.11F, 0.0F, -70F), 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.05F, 0.11F, 0.0F, -70F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f, 0.18F, 0.98F, 0.0F, -120F), 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f, 0.18F, 0.24F, 0.0F, -70F), 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f, 0.18F, 0.24F, 0.0F, -70F), 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.resetYPRmodifier();
		xyz[1] = 0.415F * this.FM.CT.getGear();
		ypr[1] = f;
		this.hierMesh().chunkSetLocate("GearC2_D0", xyz, ypr);
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		xyz[1] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.16F, 0.0F, 0.16F);
		this.hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
		this.hierMesh().chunkSetAngles("GearL6_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.16F, 0.0F, -32F), 0.0F);
		this.hierMesh().chunkSetAngles("GearL7_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.16F, 0.0F, -64F), 0.0F);
		this.resetYPRmodifier();
		xyz[1] = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.16F, 0.0F, 0.16F);
		this.hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
		this.hierMesh().chunkSetAngles("GearR6_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.16F, 0.0F, -32F), 0.0F);
		this.hierMesh().chunkSetAngles("GearR7_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.16F, 0.0F, -64F), 0.0F);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 3; i++) {
			if (this.FM.getAltitude() < 3000F) {
				this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
			} else {
				this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
			}
		}

	}

	protected void moveFlap(float f) {
		float f1 = -40F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 35:
			default:
				break;

			case 33:
				this.hitProp(0, j, actor);
				break;

			case 36:
				this.hitProp(1, j, actor);
				break;

			case 34:
				this.FM.AS.hitEngine(this, 0, 3);
				if (World.Rnd().nextInt(0, 99) < 66) { this.FM.AS.hitEngine(this, 0, 1); }
				break;

			case 37:
				this.FM.AS.hitEngine(this, 1, 3);
				if (World.Rnd().nextInt(0, 99) < 66) { this.FM.AS.hitEngine(this, 1, 1); }
				break;
		}
		return super.cutFM(i, j, actor);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxcontrol")) {
				int i = s.charAt(9) - 48;
				switch (i) {
					default:
						break;

					case 1:
						if (this.getEnergyPastArmor(1.0F, shot) > 0.0F) {
							if (World.Rnd().nextFloat() < 0.12F) {
								this.FM.AS.setControlsDamage(shot.initiator, 1);
								this.debuggunnery("Evelator Controls Out..");
							}
							if (World.Rnd().nextFloat() < 0.12F) {
								this.FM.AS.setControlsDamage(shot.initiator, 2);
								this.debuggunnery("Rudder Controls Out..");
							}
						}
						break;

					case 2:
					case 3:
						if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
							this.FM.AS.setControlsDamage(shot.initiator, 0);
							this.debuggunnery("Ailerons Controls Out..");
						}
						break;
				}
			}
			if (s.startsWith("xxeng")) {
				int j = s.charAt(5) - 49;
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(1.7F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 140000F) {
							this.FM.AS.setEngineStuck(shot.initiator, j);
							this.debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, j, 2);
							this.debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Damaged..");
						}
					} else if (World.Rnd().nextFloat() < 0.04F) {
						this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
					} else {
						this.FM.EI.engines[j].setReadyness(shot.initiator, this.FM.EI.engines[j].getReadyness() - 0.02F);
						this.debuggunnery("*** Engine" + j + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
					}
					this.getEnergyPastArmor(12F, shot);
				}
				if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 0.9878F) {
						this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
						this.debuggunnery("*** Engine" + j + " Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 48000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							this.debuggunnery("*** Engine Cylinders Hit - Engine Fires..");
						}
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("supc")) {
					if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) { this.FM.EI.engines[j].setKillCompressor(shot.initiator); }
					this.getEnergyPastArmor(2.0F, shot);
				}
				if (s.endsWith("gear")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
						if (Pd.y > 0.0D && Pd.z < 0.18899999558925629D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power) { this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0); }
						if (Pd.y < 0.0D && Pd.z < 0.18899999558925629D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power) { this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1); }
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) { this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4); }
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) { this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0); }
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) { this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 6); }
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) { this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 1); }
					}
					this.getEnergyPastArmor(2.0F, shot);
				}
				if (s.endsWith("oil1") && this.getEnergyPastArmor(4.21F, shot) > 0.0F) {
					this.FM.AS.hitOil(shot.initiator, j);
					this.getEnergyPastArmor(0.42F, shot);
				}
				return;
			}
			if (s.startsWith("xxfuel")) {
				int i1 = s.charAt(6) - 48;
				int k;
				switch (i1) {
					case 1:
					default:
						if (World.Rnd().nextFloat() < 0.33F) { this.hitBone("xxfuel2", shot, point3d); }
						if (World.Rnd().nextFloat() < 0.33F) { this.hitBone("xxfuel3", shot, point3d); }
						if (World.Rnd().nextFloat() < 0.33F) { this.hitBone("xxfuel5", shot, point3d); }
						if (World.Rnd().nextFloat() < 0.33F) { this.hitBone("xxfuel6", shot, point3d); }
						return;

					case 2:
						k = 1;
						break;

					case 3:
						k = World.Rnd().nextInt(0, 1);
						break;

					case 4:
						k = 0;
						break;

					case 5:
						k = 2;
						break;

					case 6:
						k = World.Rnd().nextInt(2, 3);
						break;

					case 7:
						k = 3;
						break;
				}
				if (this.getEnergyPastArmor(0.6F, shot) > 0.0F) {
					if (this.FM.AS.astateTankStates[k] == 0) {
						this.FM.AS.hitTank(shot.initiator, k, 2);
						this.FM.AS.doSetTankState(shot.initiator, k, 2);
					}
					if (World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F) { this.FM.AS.hitTank(shot.initiator, k, 2); }
				}
				return;
			}
			if (s.startsWith("xxgun")) {
				int l = s.charAt(5) - 49;
				if (this.getEnergyPastArmor(6.8F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) { this.FM.AS.setJamBullets(1, l); }
				this.getEnergyPastArmor(22.7F, shot);
				return;
			}
			if (s.startsWith("xxlock")) {
				if (s.startsWith("xxlockal")) {
					if (this.getEnergyPastArmor(4.35F, shot) > 0.0F) {
						this.debuggunnery("*** AroneL Lock Damaged..");
						this.nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
					}
				} else if (s.startsWith("xxlockar")) {
					if (this.getEnergyPastArmor(4.35F, shot) > 0.0F) {
						this.debuggunnery("*** AroneR Lock Damaged..");
						this.nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
					}
				} else if (s.startsWith("xxlocksl")) {
					if (this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
						this.debuggunnery("*** VatorL Lock Damaged..");
						this.nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
					}
				} else if (s.startsWith("xxlocksr")) {
					if (this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
						this.debuggunnery("*** VatorR Lock Damaged..");
						this.nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
					}
				} else if (s.startsWith("xxlockk") && this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
					this.debuggunnery("*** Rudder1 Lock Damaged..");
					this.nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxpar")) {
				if (s.startsWith("xxpark") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(12.699999809265137D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** Keel1 Spars Damaged..");
					this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
				}
				if (s.startsWith("xxparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if (s.startsWith("xxparrs") && this.chunkDamageVisible("StabL") > 1 && this.getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** StabL Spars Damaged..");
					this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
				}
				if (s.startsWith("xxparls") && this.chunkDamageVisible("StabR") > 1 && this.getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** StabR Spars Damaged..");
					this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
				}
				return;
			} else {
				return;
			}
		}
		if (s.startsWith("xcf")) {
			if (this.chunkDamageVisible("CF") < 3) { this.hitChunk("CF", shot); }
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		} else if (s.startsWith("xkeel")) {
			if (this.chunkDamageVisible("Keel1") < 2) { this.hitChunk("Keel1", shot); }
		} else if (s.startsWith("xrudder")) {
			if (this.chunkDamageVisible("Rudder1") < 1) { this.hitChunk("Rudder1", shot); }
		} else if (s.startsWith("xstabl")) {
			this.hitChunk("StabL", shot);
		} else if (s.startsWith("xstabr")) {
			this.hitChunk("StabR", shot);
		} else if (s.startsWith("xvatorl")) {
			if (this.chunkDamageVisible("VatorL") < 1) { this.hitChunk("VatorL", shot); }
		} else if (s.startsWith("xvatorr")) {
			if (this.chunkDamageVisible("VatorR") < 1) { this.hitChunk("VatorR", shot); }
		} else if (s.startsWith("xwinglin")) {
			if (this.chunkDamageVisible("WingLIn") < 3) { this.hitChunk("WingLIn", shot); }
		} else if (s.startsWith("xwingrin")) {
			if (this.chunkDamageVisible("WingRIn") < 3) { this.hitChunk("WingRIn", shot); }
		} else if (s.startsWith("xwinglmid")) {
			if (this.chunkDamageVisible("WingLMid") < 3) { this.hitChunk("WingLMid", shot); }
		} else if (s.startsWith("xwingrmid")) {
			if (this.chunkDamageVisible("WingRMid") < 3) { this.hitChunk("WingRMid", shot); }
		} else if (s.startsWith("xwinglout")) {
			if (this.chunkDamageVisible("WingLOut") < 3) { this.hitChunk("WingLOut", shot); }
		} else if (s.startsWith("xwingrout")) {
			if (this.chunkDamageVisible("WingROut") < 3) { this.hitChunk("WingROut", shot); }
		} else if (s.startsWith("xaronel")) {
			if (this.chunkDamageVisible("AroneL") < 1) { this.hitChunk("AroneL", shot); }
		} else if (s.startsWith("xaroner")) {
			if (this.chunkDamageVisible("AroneR") < 1) { this.hitChunk("AroneR", shot); }
		} else if (s.startsWith("xengine1")) {
			if (this.chunkDamageVisible("Engine1") < 2) { this.hitChunk("Engine1", shot); }
		} else if (s.startsWith("xengine2")) {
			if (this.chunkDamageVisible("Engine2") < 2) { this.hitChunk("Engine2", shot); }
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int j1;
			if (s.endsWith("a")) {
				byte0 = 1;
				j1 = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				j1 = s.charAt(6) - 49;
			} else {
				j1 = s.charAt(5) - 49;
			}
			this.hitFlesh(j1, shot, byte0);
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
				break;
		}
	}

	static {
		Class class1 = KI_46.class;
		Property.set(class1, "originCountry", PaintScheme.countryJapan);
	}
}
