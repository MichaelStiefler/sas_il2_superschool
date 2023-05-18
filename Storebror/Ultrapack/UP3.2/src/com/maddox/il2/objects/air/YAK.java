package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class YAK extends Scheme1 implements TypeFighter {

	public YAK() {
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				break;
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		float f1 = Math.max(-f * 1500F, -60F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 82.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 82.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -85F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -85F * f, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
	}

	protected void moveFlap(float f) {
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				this.debuggunnery("Armor: Hit..");
				if (s.endsWith("p1")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(5F, 12.7F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
				} else if (s.endsWith("p2")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.7F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
				} else if (s.endsWith("g1")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
					if (World.Rnd().nextFloat() < 0.2F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2); }
					if (shot.power <= 0.0F) { this.doRicochetBack(shot); }
				} else if (s.endsWith("g2")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
					if (shot.power <= 0.0F) { this.doRicochetBack(shot); }
				}
				return;
			}
			if (s.startsWith("xxcontrols")) {
				this.debuggunnery("Controls: Hit..");
				int i = s.charAt(10) - 48;
				switch (i) {
					default:
						break;

					case 1:
						if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
							if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1); }
							if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6); }
							if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7); }
						}
						break;

					case 2:
						if (this.getEnergyPastArmor(4.8F, shot) > 0.0F) {
							debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
							this.FM.AS.setControlsDamage(shot.initiator, 2);
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 3:
					case 4:
						if (this.getEnergyPastArmor(0.99F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
							this.debuggunnery("Controls: Ailerones Controls: Out..");
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 5:
					case 6:
						if (this.getEnergyPastArmor(0.002F, shot) > 0.0F) {
							this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
							this.FM.AS.setControlsDamage(shot.initiator, 2);
						}
						break;

					case 7:
						if (this.getEnergyPastArmor(4.2F, shot) > 0.0F) {
							this.debuggunnery("Controls: Elevator Controls: Disabled..");
							this.FM.AS.setControlsDamage(shot.initiator, 1);
						}
						break;

					case 8:
						if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
							debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
						}
						break;
				}
				return;
			}
			if (s.startsWith("xxeng1")) {
				if (s.endsWith("prop") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F) {
					this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
					debugprintln(this, "*** Engine Prop Governor Failed..");
				}
				if (s.endsWith("gear") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F) {
					this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
					debugprintln(this, "*** Engine Prop Governor Damaged..");
				}
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 200000F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
						}
						if (World.Rnd().nextFloat() < shot.power / 28000F) {
							this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
							debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
						}
						if (World.Rnd().nextFloat() < 0.08F) {
							this.FM.EI.engines[0].setEngineStuck(shot.initiator);
							debugprintln(this, "*** Engine Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
						}
						this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
						debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
					}
					if (World.Rnd().nextFloat() < 0.01F) {
						this.FM.EI.engines[0].setEngineStops(shot.initiator);
						debugprintln(this, "*** Engine Crank Case Hit - Engine Stalled..");
					}
					if (World.Rnd().nextFloat() < 0.01F) {
						this.FM.AS.hitEngine(shot.initiator, 0, 10);
						debugprintln(this, "*** Engine Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
					}
					this.getEnergyPastArmor(6F, shot);
				}
				if ((s.endsWith("cyl1") || s.endsWith("cyl2")) && this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.72F) {
					this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
					debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
					if (World.Rnd().nextFloat() < 0.01F) {
						this.FM.EI.engines[0].setEngineStuck(shot.initiator);
						debugprintln(this, "*** Engine Cylinder Case Broken - Engine Stuck..");
					}
					if (World.Rnd().nextFloat() < shot.power / 24000F) {
						this.FM.AS.hitEngine(shot.initiator, 0, 3);
						debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
					}
					this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
				}
				if (s.endsWith("supc") && this.getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
					this.FM.EI.engines[0].setKillCompressor(shot.initiator);
					debugprintln(this, "*** Engine Supercharger Out..");
				}
				if (s.endsWith("eqpt") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
					if (World.Rnd().nextFloat() < 0.11F) {
						this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
						debugprintln(this, "*** Engine Magneto Out..");
					}
					if (World.Rnd().nextFloat() < 0.11F) {
						this.FM.EI.engines[0].setKillCompressor(shot.initiator);
						debugprintln(this, "*** Engine Compressor Feed Out..");
					}
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
				if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
				}
				if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxmgun0")) {
				int j = s.charAt(7) - 49;
				if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
					this.debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
					this.FM.AS.setJamBullets(0, j);
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
				}
				return;
			}
			if (s.startsWith("xxcannon")) {
				if (this.getEnergyPastArmor(9.8F, shot) > 0.0F) {
					this.debuggunnery("Armament: Cannon (0) Disabled..");
					this.FM.AS.setJamBullets(1, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
				}
				return;
			}
			if (s.startsWith("xxoil")) {
				if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.FM.AS.hitOil(shot.initiator, 0);
					this.getEnergyPastArmor(0.22F, shot);
					this.debuggunnery("Engine Module: Oil Radiator Pierced..");
				}
				return;
			}
			if (s.startsWith("xxtank")) {
				int k = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					if (this.FM.AS.astateTankStates[k] == 0) {
						this.debuggunnery("Fuel Tank (" + k + "): Pierced..");
						this.FM.AS.hitTank(shot.initiator, k, 1);
						this.FM.AS.doSetTankState(shot.initiator, k, 1);
					}
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F) {
						this.FM.AS.hitTank(shot.initiator, k, 2);
						this.debuggunnery("Fuel Tank (" + k + "): Hit..");
					}
				}
				return;
			}
			if (s.startsWith("xxpnm")) {
				this.FM.AS.setInternalDamage(shot.initiator, 1);
				return;
			}
			if (s.startsWith("xxspar")) {
				debugprintln(this, "*** Spar Construction: Hit..");
				if (s.startsWith("xxsparlm") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debugprintln(this, "*** WingLMid Spar Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
				}
				if (s.startsWith("xxsparrm") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debugprintln(this, "*** WingRMid Spar Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
				}
				if (s.startsWith("xxsparlo") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debugprintln(this, "*** WingLOut Spar Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
				}
				if (s.startsWith("xxsparro") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debugprintln(this, "*** WingROut Spar Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
				}
				if (s.startsWith("xxspark") && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					debugprintln(this, "*** Keel Spars Damaged..");
					this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
				}
				if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.86F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				}
				return;
			} else {
				if (!s.startsWith("xxwater")) { ; }
				return;
			}
		}
		if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
			this.hitChunk("CF", shot);
			if (s.startsWith("xcockpit")) {
				if (point3d.z > 0.716D && World.Rnd().nextFloat() < 0.25F && World.Rnd().nextFloat() < 0.2F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1); }
				if (point3d.z > 0.716D && World.Rnd().nextFloat() < 0.25F && World.Rnd().nextFloat() < 0.2F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8); }
				if (point3d.x > -0.5D && World.Rnd().nextFloat() < 0.2F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40); }
				if (point3d.x > -0.5D && World.Rnd().nextFloat() < 0.2F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20); }
			}
			return;
		}
		if (s.startsWith("xeng")) {
			if (this.chunkDamageVisible("Engine1") < 2) { this.hitChunk("Engine1", shot); }
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		} else if (s.startsWith("xkeel")) {
			if (this.chunkDamageVisible("Keel1") < 2) { this.hitChunk("Keel1", shot); }
		} else if (s.startsWith("xrudder")) {
			if (this.chunkDamageVisible("Rudder1") < 1) { this.hitChunk("Rudder1", shot); }
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl")) { this.hitChunk("StabL", shot); }
			if (s.startsWith("xstabr")) { this.hitChunk("StabR", shot); }
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 1) { this.hitChunk("VatorL", shot); }
			if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 1) { this.hitChunk("VatorR", shot); }
		} else if (s.startsWith("xwing")) {
			if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) { this.hitChunk("WingLIn", shot); }
			if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) { this.hitChunk("WingRIn", shot); }
			if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) { this.hitChunk("WingLMid", shot); }
			if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) { this.hitChunk("WingRMid", shot); }
			if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) { this.hitChunk("WingLOut", shot); }
			if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) { this.hitChunk("WingROut", shot); }
		} else if (s.startsWith("xarone")) {
			if (s.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 1) { this.hitChunk("AroneL", shot); }
			if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneR") < 1) { this.hitChunk("AroneR", shot); }
		} else if (s.startsWith("xgear")) {
			if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
				this.debuggunnery("Hydro System: Disabled..");
				this.FM.AS.setInternalDamage(shot.initiator, 0);
			}
			if (s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F) {
				this.debuggunnery("Undercarriage: Stuck..");
				this.FM.AS.setInternalDamage(shot.initiator, 3);
			}
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
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

	static {
		Class class1 = YAK.class;
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
	}
}
