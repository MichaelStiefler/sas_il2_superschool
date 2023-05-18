package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.VisibilityChecker;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;

public abstract class D_520 extends Scheme1 implements TypeFighter, TypeBNZFighter, TypeStormovik {

	public D_520() {
		this.gun = new Gun[5];
		this.bCanopyInitState = false;
		this.CanopyEjectState = false;
		this.CockpitDamaged1 = false;
		this.CockpitDamaged2 = false;
		this.CockpitDamaged3 = false;
		this.CockpitDamaged4 = false;
		this.CockpitDamaged5 = false;
		this.CockpitShoot1 = false;
		this.CockpitShoot2 = false;
		this.CockpitShoot3 = false;
		this.CockpitShoot4 = false;
		this.CockpitShoot5 = false;
		this.CockpitShoot6 = false;
		this.CockpitShoot7 = false;
		this.CockpitShoot8 = false;
		this.CockpitShoot9 = false;
		this.CockpitShoot10 = false;
		this.CockpitShoot11 = false;
		this.CockpitShoot12 = false;
		this.timeCounterCockpitShoot1 = 0.0F;
		this.timeCockpitShoot1 = 9F;
		this.timeCounterCockpitShoot2 = 0.0F;
		this.timeCockpitShoot2 = 9F;
		this.timeCounterCockpitShoot3 = 0.0F;
		this.timeCockpitShoot3 = 9F;
		this.timeCounterCockpitShoot4 = 0.0F;
		this.timeCockpitShoot4 = 9F;
		this.timeCounterCockpitShoot5 = 0.0F;
		this.timeCockpitShoot5 = 9F;
		this.timeCounterCockpitShoot6 = 0.0F;
		this.timeCockpitShoot6 = 9F;
		this.timeCounterCockpitShoot7 = 0.0F;
		this.timeCockpitShoot7 = 9F;
		this.timeCounterCockpitShoot8 = 0.0F;
		this.timeCockpitShoot8 = 9F;
		this.timeCounterCockpitShoot9 = 0.0F;
		this.timeCockpitShoot9 = 9F;
		this.timeCounterCockpitShoot10 = 0.0F;
		this.timeCockpitShoot10 = 9F;
		this.HitArmor1 = false;
		this.OilHit = false;
		this.JamBulletsC = false;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
		else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
	}

	protected void moveFlap(float f) {
		float f1 = -45F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public void moveWheelSink() {
		float f = 0.0F;
		this.resetYPRmodifier();
		if (!this.FM.brakeShoe) f = this.FM.CT.getBrake() * Math.max(Aircraft.cvt(this.FM.EI.engines[0].getRPM(), 500F, 700F, 0.0F, 1.0F), Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 100F, 0.1F, 1.0F)) * 0.25F;
		float f1 = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.25F, 0.0F, 1.0F);
		float f2 = f1 + f;
		Aircraft.xyz[0] = -0.25F * f2;
		this.hierMesh().chunkSetLocate("GearL2b_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetLocate("GearR2b_D0", Aircraft.xyz, Aircraft.ypr);
	}

	public void doKillPilot(int j) {
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("Head1_D1", true);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				if (this.FM.getAltitude() > 3000F) this.hierMesh().chunkVisible("HMask1_D1", true);
				if (!this.FM.AS.bIsAboutToBailout && (this.hierMesh().isChunkVisible("Blister1_D0") || this.hierMesh().isChunkVisible("Blister1_D1"))) this.hierMesh().chunkVisible("Bloody1", true);
				break;
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xarmor")) {
				Aircraft.debugprintln(this, "*** Armor: Hit..");
				if (s.endsWith("1")) {
					if (point3d.z < -0.27D) this.getEnergyPastArmor(4.1D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-006D), shot);
					else this.getEnergyPastArmor(8.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-006D), shot);
					this.CockpitShoot1 = true;
				}
			}
			if (s.startsWith("xxcontrols")) {
				int i = s.charAt(10) - 48;
				switch (i) {
					default:
						break;

					case 1:
					case 4:
						if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
							this.FM.AS.setControlsDamage(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
						}
						break;

					case 2:
					case 3:
						if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.1F) {
							this.FM.AS.setControlsDamage(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Aileron Controls: Disabled..");
						}
						break;

					case 5:
					case 6:
						if (this.getEnergyPastArmor(0.002F, shot) > 0.0F && World.Rnd().nextFloat() < 0.1F) {
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							Aircraft.debugprintln(this, "*** Elevator Controls: Disabled / Strings Broken..");
						}
						break;

					case 7:
						if (this.getEnergyPastArmor(2.3F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2F) {
							this.FM.AS.setControlsDamage(shot.initiator, 2);
							Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
						}
						break;

					case 8:
						if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
							Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
							this.FM.AS.setControlsDamage(shot.initiator, 2);
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 9:
						if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
							this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
							Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
						}
						break;
				}
			}
			if (s.startsWith("xxspar")) {
				Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
				if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.5F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
					this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				}
				if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(4.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(4.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(4.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(4.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(4.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(4.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
			}
			if (s.startsWith("xxwj") && this.getEnergyPastArmor(12.5F, shot) > 0.0F) if (s.endsWith("l")) {
				Aircraft.debugprintln(this, "*** WingL Console Lock Destroyed..");
				this.nextDMGLevels(4, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
			} else {
				Aircraft.debugprintln(this, "*** WingR Console Lock Destroyed..");
				this.nextDMGLevels(4, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
			}
			if (s.startsWith("xxlock")) {
				Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
				if (s.startsWith("xxlockr") && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
					this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
				}
				if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
				}
				if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
				}
			}
			if (s.startsWith("xxeng")) {
				Aircraft.debugprintln(this, "*** Engine Module: Hit..");
				if (s.endsWith("pipe")) {
					if (World.Rnd().nextFloat() < 0.1F && this.FM.CT.Weapons[1] != null && this.FM.CT.Weapons[1].length != 1) {
						this.FM.AS.setJamBullets(1, 0);
						this.JamBulletsC = true;
						Aircraft.debugprintln(this, "*** Engine Module: Nose Nozzle Pipe Bent..");
					}
					this.getEnergyPastArmor(0.3F, shot);
				} else if (s.endsWith("prop")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) if (World.Rnd().nextFloat() < 0.5F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
						Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
					} else {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
						Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Damaged..");
					}
				} else if (s.endsWith("gear")) {
					if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) if (World.Rnd().nextFloat() < 0.5F) {
						this.FM.EI.engines[0].setEngineStuck(shot.initiator);
						Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Reductor Gear..");
					} else {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
						Aircraft.debugprintln(this, "*** Engine Module: Reductor Gear Damaged, Prop Governor Failed..");
					}
				} else if (s.endsWith("supc")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
						Aircraft.debugprintln(this, "*** Engine Module: Supercharger Disabled..");
					}
				} else if (s.endsWith("feed")) {
					if (this.getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F && this.FM.EI.engines[0].getPowerOutput() > 0.7F) {
						this.FM.AS.hitEngine(shot.initiator, 0, 100);
						Aircraft.debugprintln(this, "*** Engine Module: Pressurized Fuel Line Pierced, Fuel Flamed..");
						this.CockpitShoot11 = true;
					}
				} else if (s.endsWith("fuel")) {
					if (this.getEnergyPastArmor(1.1F, shot) > 0.0F) {
						this.FM.EI.engines[0].setEngineStops(shot.initiator);
						Aircraft.debugprintln(this, "*** Engine Module: Fuel Line Stalled, Engine Stalled..");
					}
				} else if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 175000F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
						}
						this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
						Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
					}
					this.getEnergyPastArmor(22.5F, shot);
				} else if (s.startsWith("xxeng1cyl")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.75F) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
						Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 24000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 3);
							Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
							this.CockpitShoot10 = true;
						}
						if (World.Rnd().nextFloat() < 0.01F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
						}
						this.getEnergyPastArmor(22.5F, shot);
					}
				} else if (s.startsWith("xxeng1mag")) {
					int j = s.charAt(9) - 49;
					this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
					Aircraft.debugprintln(this, "*** Engine Module: Magneto " + j + " Destroyed..");
				} else if (s.endsWith("sync")) {
					if (this.getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
						Aircraft.debugprintln(this, "*** Engine Module: Gun Synchronized Hit, Nose Guns Lose Authority..");
						this.FM.AS.setJamBullets(1, 0);
						this.JamBulletsC = true;
					}
				} else if (s.endsWith("oil1")) {
					this.FM.AS.hitOil(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
					this.CockpitShoot8 = true;
				}
			}
			if (s.startsWith("xxoil")) {
				this.FM.AS.hitOil(shot.initiator, 0);
				this.getEnergyPastArmor(0.22F, shot);
				Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
				this.CockpitShoot7 = true;
				this.OilHit = true;
			}
			if (s.startsWith("xxtank")) {
				int k = s.charAt(6) - 48;
				switch (k) {
					default:
						break;

					case 1:
					case 2:
						if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
							if (this.FM.AS.astateTankStates[3] == 0) {
								Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
								this.FM.AS.hitTank(shot.initiator, 2, 1);
								this.FM.AS.doSetTankState(shot.initiator, 2, 1);
							}
							if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) {
								this.FM.AS.hitTank(shot.initiator, 2, 2);
								Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
							}
						}
						break;

					case 3:
						if (this.getEnergyPastArmor(1.7F, shot) > 0.0F && (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F || World.Rnd().nextFloat() < 0.25F)) {
							Aircraft.debugprintln(this, "*** Nitrogen Oxyde Tank: Pierced, Nitros Flamed..");
							this.FM.AS.hitTank(shot.initiator, 0, 100);
							this.FM.AS.hitTank(shot.initiator, 1, 100);
							this.FM.AS.hitTank(shot.initiator, 2, 100);
						}
						break;
				}
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
				if (s.endsWith("03")) {
					Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
					this.FM.AS.setJamBullets(0, 2);
				}
				if (s.endsWith("04")) {
					Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
					this.FM.AS.setJamBullets(0, 3);
				}
			}
			if (s.startsWith("xxcannon")) {
				Aircraft.debugprintln(this, "*** Nose Cannon: Disabled..");
				this.FM.AS.setJamBullets(1, 0);
				this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
				this.JamBulletsC = true;
			}
			if (s.startsWith("xxammo")) {
				if (World.Rnd().nextFloat(3800F, 30000F) < shot.power) {
					if (s.endsWith("01")) {
						Aircraft.debugprintln(this, "*** Cowling Gun: Ammo Feed Chain Broken..");
						this.FM.AS.setJamBullets(0, 0);
					}
					if (s.endsWith("02")) {
						Aircraft.debugprintln(this, "*** Cowling Gun: Ammo Feed Chain Broken..");
						this.FM.AS.setJamBullets(0, 1);
					}
					if (s.endsWith("03")) {
						Aircraft.debugprintln(this, "*** Cowling Gun: Ammo Feed Chain Broken..");
						this.FM.AS.setJamBullets(0, 2);
					}
					if (s.endsWith("04")) {
						Aircraft.debugprintln(this, "*** Cowling Gun: Ammo Feed Chain Broken..");
						this.FM.AS.setJamBullets(0, 3);
					}
					if (s.endsWith("05")) {
						Aircraft.debugprintln(this, "*** Cowling Gun: Ammo Feed Chain Broken..");
						this.FM.AS.setJamBullets(1, 0);
						this.JamBulletsC = true;
					}
				}
				this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
			}
			return;
		}
		if (s.startsWith("xcockpit")) {
			if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
			if (s.startsWith("xcockpit1")) {
				this.CockpitDamaged1 = true;
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
				this.CockpitShoot3 = true;
			}
			if (s.startsWith("xcockpit2")) {
				this.CockpitDamaged2 = true;
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
				this.CockpitShoot4 = true;
			}
			if (s.startsWith("xcockpit3")) {
				this.CockpitDamaged3 = true;
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
				this.CockpitShoot5 = true;
			}
			if (s.startsWith("xcockpit4")) {
				this.CockpitDamaged4 = true;
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
				this.CockpitShoot6 = true;
			}
			if (s.startsWith("xcockpit5")) this.CockpitDamaged5 = true;
		} else if (s.startsWith("xeng")) {
			if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) {
			if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
		} else if (s.startsWith("xrudder")) {
			if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
			if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 1) this.hitChunk("StabR", shot);
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
		} else if (s.startsWith("xarmor1")) this.HitArmor1 = true;
		else if (!this.HitArmor1 && (s.startsWith("xpilot") || s.startsWith("xhead"))) {
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

	public void update(float f) {
		if (this.FM.isPlayers() && !this.bCanopyInitState && this.FM.isStationedOnGround()) {
			this.FM.AS.setCockpitDoor(this.FM.actor, 1);
			this.FM.CT.cockpitDoorControl = 1.0F;
			this.bCanopyInitState = true;
			System.out.println("*** Initial canopy state: " + (this.FM.CT.getCockpitDoor() != 1.0F ? "closed" : "open"));
		}
		if (this.FM.isPlayers()) {
			if (!Main3D.cur3D().isViewOutside()) this.hierMesh().chunkVisible("Blister1_D0", false);
			else if (!this.CanopyEjectState) this.hierMesh().chunkVisible("Blister1_D0", true);
			Point3d point3d = World.getPlayerAircraft().pos.getAbsPoint();
			if (point3d.z - World.land().HQ(point3d.x, point3d.y) < 0.01D) this.hierMesh().chunkVisible("CF_D0", true);
		}
		if (this.FM.isPlayers() && this.FM.CT.cockpitDoorControl > 0.01F && this.FM.getSpeedKMH() > 400F && this.FM.AS.aircraft.hierMesh().chunkFindCheck("Blister1_D0") != -1 && this.FM.AS.getPilotHealth(0) > 0.0F) {
			this.CanopyEjectState = true;
			this.playSound("aircraft.canopy-wreck", true);
			this.FM.CT.cockpitDoorControl = 0.0F;
			this.FM.CT.bHasCockpitDoorControl = false;
			this.FM.VmaxAllowed = 380F;
			this.FM.Sq.dragEngineCx[0] *= 6.2F;
		}
		if (this.FM.isPlayers()) {
			if (this.CockpitDamaged1) {
				this.hierMesh().chunkVisible("FGlassHoles", true);
				this.hierMesh().chunkVisible("FGlass", false);
			}
			if (this.CockpitDamaged3) {
				this.hierMesh().chunkVisible("RGlass", false);
				this.hierMesh().chunkVisible("RGlassHoles", true);
			}
			if (this.FM.brakeShoe && this.FM.Gears.nOfGearsOnGr == 3) {
				this.hierMesh().chunkVisible("ShockL", true);
				this.hierMesh().chunkVisible("ShockR", true);
			} else {
				this.hierMesh().chunkVisible("ShockL", false);
				this.hierMesh().chunkVisible("ShockR", false);
			}
		}
		if (this.FM.isPlayers()) {
			if (this.CockpitShoot1) {
				this.timeCounterCockpitShoot1 += f;
				if (this.timeCounterCockpitShoot1 > this.timeCockpitShoot1) this.CockpitShoot1 = false;
			}
			if (this.CockpitShoot2) {
				this.timeCounterCockpitShoot2 += f;
				if (this.timeCounterCockpitShoot2 > this.timeCockpitShoot2) this.CockpitShoot2 = false;
			}
			if (this.CockpitShoot3) {
				this.timeCounterCockpitShoot3 += f;
				if (this.timeCounterCockpitShoot3 > this.timeCockpitShoot3) this.CockpitShoot3 = false;
			}
			if (this.CockpitShoot4) {
				this.timeCounterCockpitShoot4 += f;
				if (this.timeCounterCockpitShoot4 > this.timeCockpitShoot4) this.CockpitShoot4 = false;
			}
			if (this.CockpitShoot5) {
				this.timeCounterCockpitShoot5 += f;
				if (this.timeCounterCockpitShoot5 > this.timeCockpitShoot5) this.CockpitShoot5 = false;
			}
			if (this.CockpitShoot6) {
				this.timeCounterCockpitShoot6 += f;
				if (this.timeCounterCockpitShoot6 > this.timeCockpitShoot6) this.CockpitShoot6 = false;
			}
			if (this.CockpitShoot7) {
				this.timeCounterCockpitShoot7 += f;
				if (this.timeCounterCockpitShoot7 > this.timeCockpitShoot7) this.CockpitShoot7 = false;
			}
			if (this.CockpitShoot8) {
				this.timeCounterCockpitShoot8 += f;
				if (this.timeCounterCockpitShoot8 > this.timeCockpitShoot8) this.CockpitShoot8 = false;
			}
			if (this.CockpitShoot9) {
				this.timeCounterCockpitShoot9 += f;
				if (this.timeCounterCockpitShoot9 > this.timeCockpitShoot9) this.CockpitShoot9 = false;
			}
			if (this.CockpitShoot10) {
				this.timeCounterCockpitShoot10 += f;
				if (this.timeCounterCockpitShoot10 > this.timeCockpitShoot10) this.CockpitShoot9 = false;
			}
		}
		this.gun[0] = ((Aircraft) this.FM.actor).getGunByHookName("_MGUN01");
		this.gun[1] = ((Aircraft) this.FM.actor).getGunByHookName("_MGUN02");
		this.gun[2] = ((Aircraft) this.FM.actor).getGunByHookName("_MGUN03");
		this.gun[3] = ((Aircraft) this.FM.actor).getGunByHookName("_MGUN04");
		this.gun[4] = ((Aircraft) this.FM.actor).getGunByHookName("_CANNON01");
		if (this.FM.isPlayers() && this.FM.CT.WeaponControl[0] && (this.gun[0].countBullets() > 0.0F || this.gun[1].countBullets() > 0.0F || this.gun[2].countBullets() > 0.0F || this.gun[3].countBullets() > 0.0F)) {
			VisibilityChecker.checkCabinObstacle = true;
			((RealFlightModel) this.FM).producedShakeLevel = 0.3F;
		}
		if (this.FM.isPlayers() && this.FM.CT.WeaponControl[1]) {
			if (this.JamBulletsC) return;
			if (this.gun[4].countBullets() > 0.0F) {
				VisibilityChecker.checkCabinObstacle = true;
				((RealFlightModel) this.FM).producedShakeLevel = 0.3F;
			}
		}
		if (this.FM.isPlayers() && this.FM.getSpeedKMH() > 780F) ((RealFlightModel) this.FM).producedShakeLevel = 0.4F;
		if (this.FM.M.fuel > 516F) this.FM.setGCenter(-0.25F);
		if (this.FM.getSpeedKMH() > 250F && this.FM.getVertSpeed() > 0.0F && this.FM.getAltitude() < 5000F) this.FM.producedAF.x += 20F * (250F - this.FM.getSpeedKMH());
		if (this.FM.isPlayers() && this.FM.Sq.squareElevators > 0.0F) {
			RealFlightModel realflightmodel = (RealFlightModel) this.FM;
			if (realflightmodel.RealMode && realflightmodel.indSpeed > 120F) {
				float f3 = 1.0F + 0.005F * (140F - realflightmodel.indSpeed);
				if (f3 < 0.0F) f3 = 0.0F;
				this.FM.SensPitch = 0.55F * f3;
				if (realflightmodel.indSpeed > 160F) this.FM.producedAM.y -= 1700F * (140F - realflightmodel.indSpeed);
			} else this.FM.SensPitch = 0.65F;
		}
		super.update(f);
	}

	public void msgExplosion(Explosion explosion) {
		this.setExplosion(explosion);
		if (explosion.chunkName != null && explosion.power > 0.0F && explosion.chunkName.startsWith("Tail1")) {
			if (World.Rnd().nextFloat(0.0F, 0.038F) < explosion.power) this.FM.AS.setControlsDamage(explosion.initiator, 1);
			if (World.Rnd().nextFloat(0.0F, 0.042F) < explosion.power) this.FM.AS.setControlsDamage(explosion.initiator, 2);
		}
		super.msgExplosion(explosion);
	}

	private Gun    gun[];
	public boolean bCanopyInitState;
	public boolean CanopyEjectState;
	public boolean CockpitDamaged1;
	public boolean CockpitDamaged2;
	public boolean CockpitDamaged3;
	public boolean CockpitDamaged4;
	public boolean CockpitDamaged5;
	private float  timeCounterCockpitShoot1;
	private float  timeCockpitShoot1;
	private float  timeCounterCockpitShoot2;
	private float  timeCockpitShoot2;
	private float  timeCounterCockpitShoot3;
	private float  timeCockpitShoot3;
	private float  timeCounterCockpitShoot4;
	private float  timeCockpitShoot4;
	private float  timeCounterCockpitShoot5;
	private float  timeCockpitShoot5;
	private float  timeCounterCockpitShoot6;
	private float  timeCockpitShoot6;
	private float  timeCounterCockpitShoot7;
	private float  timeCockpitShoot7;
	private float  timeCounterCockpitShoot8;
	private float  timeCockpitShoot8;
	private float  timeCounterCockpitShoot9;
	private float  timeCockpitShoot9;
	private float  timeCounterCockpitShoot10;
	private float  timeCockpitShoot10;
	public boolean CockpitShoot1;
	public boolean CockpitShoot2;
	public boolean CockpitShoot3;
	public boolean CockpitShoot4;
	public boolean CockpitShoot5;
	public boolean CockpitShoot6;
	public boolean CockpitShoot7;
	public boolean CockpitShoot8;
	public boolean CockpitShoot9;
	public boolean CockpitShoot10;
	public boolean CockpitShoot11;
	public boolean CockpitShoot12;
	public boolean HitArmor1;
	public boolean OilHit;
	public boolean JamBulletsC;

	static {
		Class class1 = D_520.class;
		Property.set(class1, "originCountry", PaintScheme.countryFrance);
	}
}
