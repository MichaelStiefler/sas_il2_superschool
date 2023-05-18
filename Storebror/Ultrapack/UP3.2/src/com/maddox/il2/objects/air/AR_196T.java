package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class AR_196T extends Scheme1 implements TypeScout, TypeStormovik, TypeFighter, TypeTNBFighter {

	public AR_196T() {
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 33:
				this.FM.AS.hitTank(this, 0, 6);
				return super.cutFM(34, j, actor);

			case 36:
				this.FM.AS.hitTank(this, 1, 6);
				return super.cutFM(37, j, actor);

			case 34:
				this.FM.cut(9, j, actor);
				break;

			case 37:
				this.FM.cut(10, j, actor);
				break;
		}
		return super.cutFM(i, j, actor);
	}

	public void msgExplosion(Explosion explosion) {
		this.setExplosion(explosion);
		if (explosion.chunkName != null) {
			if (explosion.chunkName.startsWith("CF")) {
				if (World.Rnd().nextFloat() < 0.005F) { this.FM.AS.setControlsDamage(explosion.initiator, 0); }
				if (World.Rnd().nextFloat() < 0.005F) { this.FM.AS.setControlsDamage(explosion.initiator, 1); }
				if (World.Rnd().nextFloat() < 0.005F) { this.FM.AS.setControlsDamage(explosion.initiator, 2); }
			}
			if (explosion.chunkName.startsWith("Engine") && explosion.power > 0.011F) { this.FM.AS.hitOil(explosion.initiator, 0); }
			if (explosion.chunkName.startsWith("Tail")) {
				if (World.Rnd().nextFloat() < 0.01F) { this.FM.AS.setControlsDamage(explosion.initiator, 1); }
				if (World.Rnd().nextFloat() < 0.11F) { this.FM.AS.setControlsDamage(explosion.initiator, 2); }
			}
		}
		super.msgExplosion(explosion);
	}

	protected void moveFlap(float f) {
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
	}

	protected void moveElevator(float f) {
		this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
	}

	public void moveWheelSink() {
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
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

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			case 0:
				if (f < -35F) {
					f = -35F;
					flag = false;
				}
				if (f > 35F) {
					f = 35F;
					flag = false;
				}
				if (f1 < -30F) {
					f1 = -30F;
					flag = false;
				}
				if (f1 > 30F) {
					f1 = 30F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 1:
				this.FM.turret[0].bIsOperable = false;
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
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(5F, 10F), shot);
				} else if (s.endsWith("p2")) {
					this.getEnergyPastArmor(5.25D / (Math.abs(Aircraft.v1.y) + 0.0001D), shot);
				} else if (s.endsWith("p3")) { this.getEnergyPastArmor(World.Rnd().nextFloat(5F, 10F), shot); }
				return;
			}
			if (s.startsWith("xxcontrols")) {
				int i = s.charAt(10) - 48;
				switch (i) {
					default:
						break;

					case 1:
						if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
							this.FM.AS.setControlsDamage(shot.initiator, 2);
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 2:
						this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
						break;

					case 3:
						if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
						}
						break;

					case 4:
					case 5:
						if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.85F) { this.FM.AS.setControlsDamage(shot.initiator, 0); }
						break;

					case 6:
					case 9:
						if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) { this.FM.AS.setControlsDamage(shot.initiator, 1); }
						break;

					case 7:
					case 8:
						if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) { this.FM.AS.setControlsDamage(shot.initiator, 2); }
						break;
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				if ((s.endsWith("t1") || s.endsWith("t2")) && World.Rnd().nextFloat() < 0.1F && this.chunkDamageVisible("Tail1") > 2
						&& this.getEnergyPastArmor(19.9F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					this.debuggunnery("*** Tail1 Spars Broken in Half..");
					this.msgCollision(this, "Tail1_D0", "Tail1_D0");
				}
				if ((s.endsWith("li1") || s.endsWith("li2")) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if ((s.endsWith("ri1") || s.endsWith("ri2")) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if ((s.endsWith("lm1") || s.endsWith("lm2")) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if ((s.endsWith("rm1") || s.endsWith("rm2")) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if ((s.endsWith("lo1") || s.endsWith("lo2")) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if ((s.endsWith("ro1") || s.endsWith("ro2")) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxeng")) {
				Aircraft.debugprintln(this, "*** Engine Module: Hit..");
				if (s.endsWith("pipe")) {
					if (World.Rnd().nextFloat() < 0.1F && this.FM.EI.engines[0].getType() == 0 && this.FM.CT.Weapons[1] != null && this.FM.CT.Weapons[1].length != 2) {
						this.FM.AS.setJamBullets(1, 0);
						Aircraft.debugprintln(this, "*** Engine Module: Nose Nozzle Pipe Bent..");
					}
					this.getEnergyPastArmor(0.3F, shot);
				} else if (s.endsWith("prop")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) {
						if (World.Rnd().nextFloat() < 0.5F) {
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
							Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
						} else {
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
							Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Damaged..");
						}
					}
				} else if (s.endsWith("gear")) {
					if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < 0.5F) {
							this.FM.EI.engines[0].setEngineStuck(shot.initiator);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Reductor Gear..");
						} else {
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
							Aircraft.debugprintln(this, "*** Engine Module: Reductor Gear Damaged, Prop Governor Failed..");
						}
					}
				} else if (s.endsWith("supc")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
						Aircraft.debugprintln(this, "*** Engine Module: Supercharger Disabled..");
					}
					this.getEnergyPastArmor(0.5F, shot);
				} else if (s.endsWith("feed")) {
					if (this.getEnergyPastArmor(8.9F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F && this.FM.EI.engines[0].getPowerOutput() > 0.7F && this.FM.EI.engines[0].getType() == 0) {
						this.FM.AS.hitEngine(shot.initiator, 0, 100);
						Aircraft.debugprintln(this, "*** Engine Module: Pressurized Fuel Line Pierced, Fuel Flamed..");
					}
					this.getEnergyPastArmor(1.0F, shot);
				} else if (s.endsWith("fuel")) {
					if (this.getEnergyPastArmor(1.1F, shot) > 0.0F && this.FM.EI.engines[0].getType() == 0) {
						this.FM.EI.engines[0].setEngineStops(shot.initiator);
						Aircraft.debugprintln(this, "*** Engine Module: Fuel Line Stalled, Engine Stalled..");
					}
					this.getEnergyPastArmor(1.0F, shot);
				} else if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(4.2F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 175000F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F && this.FM.EI.engines[0].getType() == 0) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
						}
						if (this.FM.EI.engines[0].getType() == 0) { this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F)); }
						Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
					}
					this.getEnergyPastArmor(27.5F, shot);
				} else if (s.startsWith("xxeng1cyl")) {
					if (this.getEnergyPastArmor(2.4F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * (this.FM.EI.engines[0].getType() == 0 ? 1.75F : 0.5F)) {
						if (this.FM.EI.engines[0].getType() == 0) {
							this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
						} else {
							this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19200F)));
						}
						Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 96000F && this.FM.EI.engines[0].getType() == 0) {
							this.FM.AS.hitEngine(shot.initiator, 0, 3);
							Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
						}
						if (World.Rnd().nextFloat() < shot.power / 96000F && this.FM.EI.engines[0].getType() == 1) {
							this.FM.AS.hitEngine(shot.initiator, 0, 1);
							Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
						}
						if (World.Rnd().nextFloat() < 0.01F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
						}
						this.getEnergyPastArmor(43.6F, shot);
					}
				} else if (s.startsWith("xxeng1mag")) {
					int j = s.charAt(9) - 49;
					this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
					Aircraft.debugprintln(this, "*** Engine Module: Magneto " + j + " Destroyed..");
				} else if (s.endsWith("sync")) {
					if (this.getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) { Aircraft.debugprintln(this, "*** Engine Module: Gun Synchronized Hit, Nose Guns Lose Authority.."); }
				} else if (s.endsWith("oil1") && this.getEnergyPastArmor(2.4F, shot) > 0.0F) {
					this.FM.AS.hitOil(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
				}
				return;
			}
			if (s.startsWith("xxmgun")) { this.FM.AS.setJamBullets(0, 0); }
			if (s.startsWith("xxcannon01")) {
				this.FM.AS.setJamBullets(1, 0);
				this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
			}
			if (s.startsWith("xxcannon02")) {
				this.FM.AS.setJamBullets(1, 1);
				this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
			}
			if (s.startsWith("xxammo")) {
				if (World.Rnd().nextFloat(3800F, 30000F) < shot.power) {
					if (s.endsWith("01")) { this.FM.AS.setJamBullets(1, 0); }
					if (s.endsWith("02")) { this.FM.AS.setJamBullets(1, 1); }
				}
				this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
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
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) { this.FM.AS.hitTank(shot.initiator, k, 2); }
				}
			}
			return;
		}
		if (s.startsWith("xcf")) {
			if (this.chunkDamageVisible("CF") < 3) { this.hitChunk("CF", shot); }
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		} else if (s.startsWith("xkeel")) {
			if (this.chunkDamageVisible("Keel1") < 2) { this.hitChunk("Keel1", shot); }
		} else if (s.startsWith("xrudder")) {
			this.hitChunk("Rudder1", shot);
		} else if (s.startsWith("xstabl")) {
			this.hitChunk("StabL", shot);
		} else if (s.startsWith("xstabr")) {
			this.hitChunk("StabR", shot);
		} else if (s.startsWith("xvatorl")) {
			this.hitChunk("VatorL", shot);
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
			this.hitChunk("AroneL", shot);
		} else if (s.startsWith("xaroner")) {
			this.hitChunk("AroneR", shot);
		} else if (s.startsWith("xengine")) {
			if (this.chunkDamageVisible("Engine1") < 2) { this.hitChunk("Engine1", shot); }
		} else if (s.startsWith("xturret")) {
			this.FM.AS.setJamBullets(10, 0);
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
		Class class1 = AR_196T.class;
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
	}
}
