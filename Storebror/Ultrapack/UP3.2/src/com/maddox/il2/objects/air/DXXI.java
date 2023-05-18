package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public abstract class DXXI extends Scheme1 implements TypeFighter {

	public DXXI() {
		this.canopyF = 0.0F;
		this.tiltCanopyOpened = false;
		this.slideCanopyOpened = false;
		this.blisterRemoved = false;
		this.suspension = 0.0F;
		this.canopyMaxAngle = 0.8F;
		this.hasSelfSealingTank = false;
		this.hasSkis = false;
		this.bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers()) { this.bChangedPit = true; }
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				this.debuggunnery("Armor: Hit..");
				if (s.endsWith("t1")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(21F, 42F) / (Math.abs(Aircraft.v1.x) + 1E-004D), shot);
					this.doRicochetBack(shot);
				} else if (s.endsWith("t3")) {
					this.getEnergyPastArmor(8.9F, shot);
				} else if (s.endsWith("t4")) { this.getEnergyPastArmor(8.9F, shot); }
			} else if (s.startsWith("xxpanel")) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
			} else if (s.startsWith("xxrevi")) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
			} else if (s.startsWith("xxfrontglass")) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
			} else if (s.startsWith("xxrightglass")) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
			} else if (s.startsWith("xxleftglass")) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
			} else if (s.startsWith("xxcontrol")) {
				if ((s.endsWith("s1") || s.endsWith("7")) && this.getEnergyPastArmor(4.8F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.FM.AS.setControlsDamage(shot.initiator, 1);
					Aircraft.debugprintln(this, "*** Elevator Cables: Hit, Controls Destroyed..");
				}
				if (s.endsWith("0") && this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Rudder Cabels: Hit, Controls Destroyed..");
					this.FM.AS.setControlsDamage(shot.initiator, 2);
				}
				if (s.endsWith("s4") && this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
					this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
					this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
					Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
				}
				if ((s.endsWith("3") || s.endsWith("6")) && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
					this.FM.AS.setControlsDamage(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Aileron Controls Out..");
				}
			} else if (s.startsWith("xxeng1")) {
				this.debuggunnery("Engine Module: Hit..");
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 280000F) {
							this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
							this.FM.AS.setEngineStuck(shot.initiator, 0);
						}
						if (World.Rnd().nextFloat() < shot.power / 100000F) {
							this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
						}
					}
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
				}
				if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.66F) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
						this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 1000000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
						}
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("mag1")) {
					this.debuggunnery("Engine Module: Magneto 1 Destroyed..");
					this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
				}
				if (s.endsWith("mag2")) {
					this.debuggunnery("Engine Module: Magneto 2 Destroyed..");
					this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
				}
				if (s.endsWith("oil")) {
					if (World.Rnd().nextFloat() < 0.6F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) { this.debuggunnery("Engine Module: Oil Radiator Hit.."); }
					this.FM.AS.hitOil(shot.initiator, 0);
				}
				if (s.endsWith("supc")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
						Aircraft.debugprintln(this, "*** Engine Module: Supercharger Disabled..");
					}
					this.getEnergyPastArmor(0.5F, shot);
				}
				if (s.endsWith("eqpt") && this.getEnergyPastArmor(0.2721F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
					if (World.Rnd().nextFloat() < 0.2F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
						Aircraft.debugprintln(this, "*** Engine Module: Throttle Controls Cut..");
					}
					if (World.Rnd().nextFloat() < 0.2F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
						Aircraft.debugprintln(this, "*** Engine Module: Mix Controls Cut..");
					}
				}
			} else if (s.startsWith("xxlock")) {
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
			} else if (s.startsWith("xxammo00")) {
				if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					if (World.Rnd().nextFloat() < 0.5F) {
						this.FM.AS.setJamBullets(0, 0);
					} else {
						this.FM.AS.setJamBullets(0, 1);
					}
				}
			} else if (s.startsWith("xxmgun01")) {
				if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.FM.AS.setJamBullets(0, 0);
					this.getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.startsWith("xxmgun02")) {
				if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.FM.AS.setJamBullets(0, 1);
					this.getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.startsWith("xxmgun03")) {
				if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.FM.AS.setJamBullets(0, 2);
					this.getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.startsWith("xxmgun04")) {
				if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.FM.AS.setJamBullets(0, 3);
					this.getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.startsWith("xxoil1")) {
				if (this.getEnergyPastArmor(2.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.FM.AS.hitOil(shot.initiator, 0);
					this.getEnergyPastArmor(6.75F, shot);
					this.debuggunnery("Engine Module: Oil Tank Pierced..");
				}
			} else if (s.startsWith("xxspar")) {
				Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
				if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
				}
				if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
				}
				if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
				}
				if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
				}
				if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
				}
				if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
				}
				if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** Tail1 Spars Damaged..");
					this.nextDMGLevels(1, 2, "Tail1_D" + this.chunkDamageVisible("Tail1"), shot.initiator);
				}
			} else if (s.startsWith("xxtank1")) {
				int i = 0;
				if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.45F) {
					if (this.FM.AS.astateTankStates[i] == 0) {
						this.debuggunnery("Fuel Tank (" + i + "): Pierced..");
						if (this.hasSelfSealingTank) {
							this.FM.AS.hitTank(shot.initiator, i, 1);
						} else {
							this.FM.AS.hitTank(shot.initiator, i, 2);
						}
					}
					if (World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.22F) {
						this.FM.AS.hitTank(shot.initiator, i, 2);
						this.debuggunnery("Fuel Tank (" + i + "): Hit..");
					}
				}
			}
		} else if (s.startsWith("xcf")) {
			if (this.chunkDamageVisible("CF") < 3) { this.hitChunk("CF", shot); }
		} else if (!s.startsWith("xblister")) {
			if (s.startsWith("xengine")) {
				if (this.chunkDamageVisible("Engine1") < 2) { this.hitChunk("Engine1", shot); }
			} else if (s.startsWith("xtail")) {
				if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
			} else if (s.startsWith("xkeel")) {
				this.hitChunk("Keel1", shot);
			} else if (s.startsWith("xrudder")) {
				if (this.chunkDamageVisible("Rudder1") < 2) { this.hitChunk("Rudder1", shot); }
			} else if (s.startsWith("xstab")) {
				if (s.startsWith("xstabl")) { this.hitChunk("StabL", shot); }
				if (s.startsWith("xstabr")) { this.hitChunk("StabR", shot); }
			} else if (s.startsWith("xvator")) {
				if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 2) { this.hitChunk("VatorL", shot); }
				if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 2) { this.hitChunk("VatorR", shot); }
			} else if (s.startsWith("xwing")) {
				if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) { this.hitChunk("WingLIn", shot); }
				if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) { this.hitChunk("WingRIn", shot); }
				if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) { this.hitChunk("WingLMid", shot); }
				if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) { this.hitChunk("WingRMid", shot); }
				if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) { this.hitChunk("WingLOut", shot); }
				if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) { this.hitChunk("WingROut", shot); }
			} else if (s.startsWith("xarone")) {
				if (s.startsWith("xaronel")) { this.hitChunk("AroneL", shot); }
				if (s.startsWith("xaroner")) { this.hitChunk("AroneR", shot); }
			} else if (s.startsWith("xgearl")) {
				if (s.startsWith("xgearl2")) { this.hitChunk("GearL22", shot); }
			} else if (s.startsWith("xgearr")) {
				if (s.startsWith("xgearr2")) { this.hitChunk("GearR22", shot); }
			} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
				byte byte0 = 0;
				int j;
				if (s.endsWith("a")) {
					byte0 = 1;
					j = s.charAt(6) - 49;
				} else if (s.endsWith("b")) {
					byte0 = 2;
					j = s.charAt(6) - 49;
				} else {
					j = s.charAt(5) - 49;
				}
				this.hitFlesh(j, shot, byte0);
			}
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
		}
		if (!this.FM.isPlayers() && !this.isNetPlayer() && this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.2F) { this.FM.EI.engines[0].setExtinguisherFire(); }
		if (this.tiltCanopyOpened && !this.blisterRemoved && this.FM.getSpeed() > 75F) { this.doRemoveBlister3(); }

		// TODO: +++ Fix DXXI Danish/Dutch/Sarja4 Compass
		if (this.FM.Or.getKren() < -10.0F || this.FM.Or.getKren() > 10.0F) { this.gyroDelta = (float) (this.gyroDelta - 0.01D); }
		// ---
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0: // '\0'
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("Head1_D1", true);
				this.hierMesh().chunkVisible("pilotarm2_d0", false);
				this.hierMesh().chunkVisible("pilotarm1_d0", false);
				break;
		}
	}

	public void doRemoveBodyFromPlane(int i) {
		super.doRemoveBodyFromPlane(i);
		this.hierMesh().chunkVisible("pilotarm2_d0", false);
		this.hierMesh().chunkVisible("pilotarm1_d0", false);
	}

	public void missionStarting() {
		super.missionStarting();
		this.hierMesh().chunkVisible("pilotarm2_d0", true);
		this.hierMesh().chunkVisible("pilotarm1_d0", true);
		// TODO: +++ Fix DXXI Danish/Dutch/Sarja4 Compass
		if (this.FM.isStationedOnGround()) {
			this.gyroDelta += (float) Math.random() * 360.0F;
			// ---
		}
	}

	public void prepareCamouflage() {
		super.prepareCamouflage();
		this.hierMesh().chunkVisible("pilotarm2_d0", true);
		this.hierMesh().chunkVisible("pilotarm1_d0", true);
	}

	public void blisterRemoved(int i) {
		if (i == 1) { this.doRemoveBlister2(); }
	}

	private final void doRemoveBlister2() {
		this.blisterRemoved = true;
		if (this.hierMesh().chunkFindCheck("Blister2_D0") != -1) {
			this.hierMesh().hideSubTrees("Blister2_D0");
			Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister2_D0"));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(this.FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
	}

	private final void doRemoveBlister3() {
		this.blisterRemoved = true;
		this.FM.CT.bHasCockpitDoorControl = false;
		this.bChangedPit = true;
		if (this.hierMesh().chunkFindCheck("Blister3_D0") != -1) {
			this.hierMesh().hideSubTrees("Blister3_D0");
			Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister3_D0"));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(this.FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
		if (this.hierMesh().chunkFindCheck("Blister2_D0") != -1) {
			this.hierMesh().hideSubTrees("Blister2_D0");
			Wreckage wreckage1 = new Wreckage(this, this.hierMesh().chunkFind("Blister2_D0"));
			wreckage1.collide(true);
			Vector3d vector3d1 = new Vector3d();
			vector3d1.set(this.FM.Vwld);
			wreckage1.setSpeed(vector3d1);
		}
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
		if (f < 0.0F) {
			this.hierMesh().chunkSetAngles("RudderWireR_d0", -28F * f, 0.0F, 0.0F);
			this.hierMesh().chunkSetAngles("RudderWireL_d0", -30.41F * f, 0.0F, 0.0F);
		} else {
			this.hierMesh().chunkSetAngles("RudderWireR_d0", -30F * f, 0.0F, 0.0F);
			this.hierMesh().chunkSetAngles("RudderWireL_d0", -28F * f, 0.0F, 0.0F);
		}
	}

	protected void moveAileron(float f) {
		float f1 = -30F * f;
		this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, f1, 0.0F);
	}

	protected void moveFlap(float f) {
		float f1 = 50F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public void sfxFlaps(boolean flag) {
	}

	public void moveCockpitDoor(float f) {
		if (f > this.canopyF) {
			if ((this.FM.Gears.onGround() && this.FM.getSpeed() < 5F || this.tiltCanopyOpened) && (this.FM.isPlayers() || this.isNetPlayer())) {
				this.tiltCanopyOpened = true;
				this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, -f * 80F, 0.0F);
				this.hierMesh().chunkSetAngles("Blister3_D0", 0.0F, -f * 125F, 0.0F);
			} else {
				this.slideCanopyOpened = true;
				this.hierMesh().chunkSetAngles("Blister4L_D0", 0.0F, f * this.canopyMaxAngle, 0.0F);
			}
		} else if (this.FM.Gears.onGround() && this.FM.getSpeed() < 5F && !this.slideCanopyOpened || this.tiltCanopyOpened) {
			this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, -f * 80F, 0.0F);
			this.hierMesh().chunkSetAngles("Blister3_D0", 0.0F, -f * 125F, 0.0F);
			if (this.FM.getSpeed() > 50F && f < 0.6F && !this.blisterRemoved) { this.doRemoveBlister3(); }
			if (f == 0.0F) { this.tiltCanopyOpened = false; }
		} else {
			this.hierMesh().chunkSetAngles("Blister4L_D0", 0.0F, f * this.canopyMaxAngle, 0.0F);
			if (f == 0.0F) { this.slideCanopyOpened = false; }
		}
		this.canopyF = f;
		if (this.canopyF < 0.01D) { this.canopyF = 0.0F; }
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) { Main3D.cur3D().cockpits[0].onDoorMoved(f); }
			this.setDoorSnd(f);
		}
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, f, 0.0F);
	}

	public void moveWheelSink() {
		if (this.FM.Gears.onGround()) {
			this.suspension = this.suspension + 0.008F;
		} else {
			this.suspension = this.suspension - 0.008F;
		}
		if (this.suspension < 0.0F) {
			this.suspension = 0.0F;
			if (!this.FM.isPlayers()) { this.FM.Gears.bTailwheelLocked = true; }
		}
		if (this.suspension > 0.1F) { this.suspension = 0.1F; }
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		Aircraft.xyz[1] = this.suspension / 10F;
		float f = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 25F, 0.0F, 1.0F);
		float f1 = this.FM.Gears.gWheelSinking[0] * f + this.suspension;
		Aircraft.xyz[2] = Aircraft.cvt(f1, 0.0F, 0.24F, 0.0F, 0.24F);
		this.hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearL31_D0", f1 * 500F, 0.0F, 0.0F);
		this.hierMesh().chunkSetAngles("GearL32_D0", -f1 * 500F, 0.0F, 0.0F);
		f1 = this.FM.Gears.gWheelSinking[1] * f + this.suspension;
		Aircraft.xyz[2] = Aircraft.cvt(f1, 0.0F, 0.24F, 0.0F, 0.24F);
		this.hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearR31_D0", f1 * 500F, 0.0F, 0.0F);
		this.hierMesh().chunkSetAngles("GearR32_D0", -f1 * 500F, 0.0F, 0.0F);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			default:
				break;

			case 11: // '\013'
				this.hierMesh().chunkVisible("WireL_D0", false);
				this.hierMesh().chunkVisible("WireR_D0", false);
				break;

			case 17: // '\021'
				this.hierMesh().chunkVisible("WireL_D0", false);
				break;

			case 18: // '\022'
				this.hierMesh().chunkVisible("WireL_D0", false);
				break;

			case 19: // '\023'
				this.FM.Gears.hitCentreGear();
				this.hierMesh().chunkVisible("Antenna_D0", false);
				break;

			case 3: // '\003'
				if (World.Rnd().nextInt(0, 99) < 1) {
					this.FM.AS.hitEngine(this, 0, 4);
					this.hitProp(0, j, actor);
					this.FM.EI.engines[0].setEngineStuck(actor);
					return this.cut("engine1");
				} else {
					this.FM.AS.setEngineDies(this, 0);
					return false;
				}
		}
		return super.cutFM(i, j, actor);
	}

	// TODO: +++ Fix DXXI Danish/Dutch/Sarja4 Compass
	public void auxPlus(int paramInt) {
		switch (paramInt) {
			case 1:
				this.gyroDelta += 1.0F;
		}
	}

	public void auxMinus(int paramInt) {
		switch (paramInt) {
			case 1:
				this.gyroDelta -= 1.0F;
		}
	}
	// ---

	public float    canopyF;
	public boolean  tiltCanopyOpened;
	private boolean slideCanopyOpened;
	public boolean  blisterRemoved;
	private float   suspension;
	public float    canopyMaxAngle;
	public boolean  hasSelfSealingTank;
	public boolean  hasSkis;
	public boolean  bChangedPit;

	// TODO: +++ Fix DXXI Danish/Dutch/Sarja4 Compass
	public float gyroDelta = 0.0F;
	// ---

	static {
		Class class1 = DXXI.class;
		Property.set(class1, "originCountry", PaintScheme.countryFinland);
	}
}
