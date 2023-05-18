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
		canopyF = 0.0F;
		tiltCanopyOpened = false;
		slideCanopyOpened = false;
		blisterRemoved = false;
		suspension = 0.0F;
		canopyMaxAngle = 0.8F;
		hasSelfSealingTank = false;
		hasSkis = false;
		bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				debuggunnery("Armor: Hit..");
				if (s.endsWith("t1")) {
					getEnergyPastArmor(World.Rnd().nextFloat(21F, 42F) / (Math.abs(Aircraft.v1.x) + 1E-004D), shot);
					doRicochetBack(shot);
				} else if (s.endsWith("t3"))
					getEnergyPastArmor(8.9F, shot);
				else if (s.endsWith("t4"))
					getEnergyPastArmor(8.9F, shot);
			} else if (s.startsWith("xxpanel"))
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
			else if (s.startsWith("xxrevi"))
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
			else if (s.startsWith("xxfrontglass"))
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
			else if (s.startsWith("xxrightglass"))
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
			else if (s.startsWith("xxleftglass"))
				FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
			else if (s.startsWith("xxcontrol")) {
				if ((s.endsWith("s1") || s.endsWith("7")) && getEnergyPastArmor(4.8F, shot) > 0.0F
						&& World.Rnd().nextFloat() < 0.25F) {
					FM.AS.setControlsDamage(shot.initiator, 1);
					Aircraft.debugprintln(this, "*** Elevator Cables: Hit, Controls Destroyed..");
				}
				if (s.endsWith("0") && getEnergyPastArmor(3.2F, shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Rudder Cabels: Hit, Controls Destroyed..");
					FM.AS.setControlsDamage(shot.initiator, 2);
				}
				if (s.endsWith("s4") && getEnergyPastArmor(0.2F, shot) > 0.0F) {
					FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
					FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
					Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
				}
				if ((s.endsWith("3") || s.endsWith("6")) && World.Rnd().nextFloat() < 0.5F
						&& getEnergyPastArmor(0.35F, shot) > 0.0F) {
					FM.AS.setControlsDamage(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Aileron Controls Out..");
				}
			} else if (s.startsWith("xxeng1")) {
				debuggunnery("Engine Module: Hit..");
				if (s.endsWith("case")) {
					if (getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 280000F) {
							debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
							FM.AS.setEngineStuck(shot.initiator, 0);
						}
						if (World.Rnd().nextFloat() < shot.power / 100000F) {
							debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
							FM.AS.hitEngine(shot.initiator, 0, 2);
						}
					}
					getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
				}
				if (s.endsWith("cyls")) {
					if (getEnergyPastArmor(0.85F, shot) > 0.0F
							&& World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.66F) {
						FM.EI.engines[0].setCyliderKnockOut(shot.initiator,
								World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
						debuggunnery("Engine Module: Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/"
								+ FM.EI.engines[0].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 1000000F) {
							FM.AS.hitEngine(shot.initiator, 0, 2);
							debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
						}
					}
					getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("mag1")) {
					debuggunnery("Engine Module: Magneto 1 Destroyed..");
					FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
				}
				if (s.endsWith("mag2")) {
					debuggunnery("Engine Module: Magneto 2 Destroyed..");
					FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
				}
				if (s.endsWith("oil")) {
					if (World.Rnd().nextFloat() < 0.6F && getEnergyPastArmor(0.25F, shot) > 0.0F)
						debuggunnery("Engine Module: Oil Radiator Hit..");
					FM.AS.hitOil(shot.initiator, 0);
				}
				if (s.endsWith("supc")) {
					if (getEnergyPastArmor(0.1F, shot) > 0.0F) {
						FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
						Aircraft.debugprintln(this, "*** Engine Module: Supercharger Disabled..");
					}
					getEnergyPastArmor(0.5F, shot);
				}
				if (s.endsWith("eqpt") && getEnergyPastArmor(0.2721F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
					if (World.Rnd().nextFloat() < 0.2F) {
						FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
						Aircraft.debugprintln(this, "*** Engine Module: Throttle Controls Cut..");
					}
					if (World.Rnd().nextFloat() < 0.2F) {
						FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
						Aircraft.debugprintln(this, "*** Engine Module: Mix Controls Cut..");
					}
				}
			} else if (s.startsWith("xxlock")) {
				debuggunnery("Lock Construction: Hit..");
				if (s.startsWith("xxlockr")
						&& getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
					nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
				}
				if (s.startsWith("xxlockvl")
						&& getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: VatorL Lock Shot Off..");
					nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
				}
				if (s.startsWith("xxlockvr")
						&& getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: VatorR Lock Shot Off..");
					nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
				}
			} else if (s.startsWith("xxammo00")) {
				if (getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
					if (World.Rnd().nextFloat() < 0.5F)
						FM.AS.setJamBullets(0, 0);
					else
						FM.AS.setJamBullets(0, 1);
			} else if (s.startsWith("xxmgun01")) {
				if (getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					FM.AS.setJamBullets(0, 0);
					getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.startsWith("xxmgun02")) {
				if (getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					FM.AS.setJamBullets(0, 1);
					getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.startsWith("xxmgun03")) {
				if (getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					FM.AS.setJamBullets(0, 2);
					getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.startsWith("xxmgun04")) {
				if (getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					FM.AS.setJamBullets(0, 3);
					getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.startsWith("xxoil1")) {
				if (getEnergyPastArmor(2.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					FM.AS.hitOil(shot.initiator, 0);
					getEnergyPastArmor(6.75F, shot);
					debuggunnery("Engine Module: Oil Tank Pierced..");
				}
			} else if (s.startsWith("xxspar")) {
				Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
				if (s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2
						&& getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
					nextDMGLevels(1, 2, "WingLIn_D" + chunkDamageVisible("WingLIn"), shot.initiator);
				}
				if (s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2
						&& getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
					nextDMGLevels(1, 2, "WingRIn_D" + chunkDamageVisible("WingRIn"), shot.initiator);
				}
				if (s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2
						&& getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
					nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
				}
				if (s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2
						&& getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
					nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
				}
				if (s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2
						&& getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
					nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
				}
				if (s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2
						&& getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
					nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
				}
				if (s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2
						&& getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					debuggunnery("*** Tail1 Spars Damaged..");
					nextDMGLevels(1, 2, "Tail1_D" + chunkDamageVisible("Tail1"), shot.initiator);
				}
			} else if (s.startsWith("xxtank1")) {
				int i = 0;
				if (getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.45F) {
					if (FM.AS.astateTankStates[i] == 0) {
						debuggunnery("Fuel Tank (" + i + "): Pierced..");
						if (hasSelfSealingTank)
							FM.AS.hitTank(shot.initiator, i, 1);
						else
							FM.AS.hitTank(shot.initiator, i, 2);
					}
					if (World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.22F) {
						FM.AS.hitTank(shot.initiator, i, 2);
						debuggunnery("Fuel Tank (" + i + "): Hit..");
					}
				}
			}
		} else if (s.startsWith("xcf")) {
			if (chunkDamageVisible("CF") < 3)
				hitChunk("CF", shot);
		} else if (!s.startsWith("xblister"))
			if (s.startsWith("xengine")) {
				if (chunkDamageVisible("Engine1") < 2)
					hitChunk("Engine1", shot);
			} else if (s.startsWith("xtail")) {
				if (chunkDamageVisible("Tail1") < 3)
					hitChunk("Tail1", shot);
			} else if (s.startsWith("xkeel"))
				hitChunk("Keel1", shot);
			else if (s.startsWith("xrudder")) {
				if (chunkDamageVisible("Rudder1") < 2)
					hitChunk("Rudder1", shot);
			} else if (s.startsWith("xstab")) {
				if (s.startsWith("xstabl"))
					hitChunk("StabL", shot);
				if (s.startsWith("xstabr"))
					hitChunk("StabR", shot);
			} else if (s.startsWith("xvator")) {
				if (s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 2)
					hitChunk("VatorL", shot);
				if (s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 2)
					hitChunk("VatorR", shot);
			} else if (s.startsWith("xwing")) {
				if (s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
					hitChunk("WingLIn", shot);
				if (s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
					hitChunk("WingRIn", shot);
				if (s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
					hitChunk("WingLMid", shot);
				if (s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
					hitChunk("WingRMid", shot);
				if (s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
					hitChunk("WingLOut", shot);
				if (s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
					hitChunk("WingROut", shot);
			} else if (s.startsWith("xarone")) {
				if (s.startsWith("xaronel"))
					hitChunk("AroneL", shot);
				if (s.startsWith("xaroner"))
					hitChunk("AroneR", shot);
			} else if (s.startsWith("xgearl")) {
				if (s.startsWith("xgearl2"))
					hitChunk("GearL22", shot);
			} else if (s.startsWith("xgearr")) {
				if (s.startsWith("xgearr2"))
					hitChunk("GearR22", shot);
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
				hitFlesh(j, shot, byte0);
			}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (FM.getAltitude() < 3000F)
			hierMesh().chunkVisible("HMask1_D0", false);
		else
			hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
		if (!FM.isPlayers() && !isNetPlayer() && FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.2F)
			FM.EI.engines[0].setExtinguisherFire();
		if (tiltCanopyOpened && !blisterRemoved && FM.getSpeed() > 75F)
			doRemoveBlister3();

		// TODO: +++ Fix DXXI Danish/Dutch/Sarja4 Compass
		if ((this.FM.Or.getKren() < -10.0F) || (this.FM.Or.getKren() > 10.0F)) {
			this.gyroDelta = ((float) (this.gyroDelta - 0.01D));
		}
		// ---
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			hierMesh().chunkVisible("Head1_D1", true);
			hierMesh().chunkVisible("pilotarm2_d0", false);
			hierMesh().chunkVisible("pilotarm1_d0", false);
			break;
		}
	}

	public void doRemoveBodyFromPlane(int i) {
		super.doRemoveBodyFromPlane(i);
		hierMesh().chunkVisible("pilotarm2_d0", false);
		hierMesh().chunkVisible("pilotarm1_d0", false);
	}

	public void missionStarting() {
		super.missionStarting();
		hierMesh().chunkVisible("pilotarm2_d0", true);
		hierMesh().chunkVisible("pilotarm1_d0", true);
		// TODO: +++ Fix DXXI Danish/Dutch/Sarja4 Compass
		if (this.FM.isStationedOnGround())
			this.gyroDelta += (float) Math.random() * 360.0F;
		// ---
	}

	public void prepareCamouflage() {
		super.prepareCamouflage();
		hierMesh().chunkVisible("pilotarm2_d0", true);
		hierMesh().chunkVisible("pilotarm1_d0", true);
	}

	public void blisterRemoved(int i) {
		if (i == 1)
			doRemoveBlister2();
	}

	private final void doRemoveBlister2() {
		blisterRemoved = true;
		if (hierMesh().chunkFindCheck("Blister2_D0") != -1) {
			hierMesh().hideSubTrees("Blister2_D0");
			Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister2_D0"));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
	}

	private final void doRemoveBlister3() {
		blisterRemoved = true;
		FM.CT.bHasCockpitDoorControl = false;
		bChangedPit = true;
		if (hierMesh().chunkFindCheck("Blister3_D0") != -1) {
			hierMesh().hideSubTrees("Blister3_D0");
			Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister3_D0"));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
		if (hierMesh().chunkFindCheck("Blister2_D0") != -1) {
			hierMesh().hideSubTrees("Blister2_D0");
			Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("Blister2_D0"));
			wreckage1.collide(true);
			Vector3d vector3d1 = new Vector3d();
			vector3d1.set(FM.Vwld);
			wreckage1.setSpeed(vector3d1);
		}
	}

	protected void moveRudder(float f) {
		hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
		if (f < 0.0F) {
			hierMesh().chunkSetAngles("RudderWireR_d0", -28F * f, 0.0F, 0.0F);
			hierMesh().chunkSetAngles("RudderWireL_d0", -30.41F * f, 0.0F, 0.0F);
		} else {
			hierMesh().chunkSetAngles("RudderWireR_d0", -30F * f, 0.0F, 0.0F);
			hierMesh().chunkSetAngles("RudderWireL_d0", -28F * f, 0.0F, 0.0F);
		}
	}

	protected void moveAileron(float f) {
		float f1 = -30F * f;
		hierMesh().chunkSetAngles("AroneL_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("AroneR_D0", 0.0F, f1, 0.0F);
	}

	protected void moveFlap(float f) {
		float f1 = 50F * f;
		hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public void sfxFlaps(boolean flag) {
	}

	public void moveCockpitDoor(float f) {
		if (f > canopyF) {
			if ((FM.Gears.onGround() && FM.getSpeed() < 5F || tiltCanopyOpened) && (FM.isPlayers() || isNetPlayer())) {
				tiltCanopyOpened = true;
				hierMesh().chunkSetAngles("Blister2_D0", 0.0F, -f * 80F, 0.0F);
				hierMesh().chunkSetAngles("Blister3_D0", 0.0F, -f * 125F, 0.0F);
			} else {
				slideCanopyOpened = true;
				hierMesh().chunkSetAngles("Blister4L_D0", 0.0F, f * canopyMaxAngle, 0.0F);
			}
		} else if (FM.Gears.onGround() && FM.getSpeed() < 5F && !slideCanopyOpened || tiltCanopyOpened) {
			hierMesh().chunkSetAngles("Blister2_D0", 0.0F, -f * 80F, 0.0F);
			hierMesh().chunkSetAngles("Blister3_D0", 0.0F, -f * 125F, 0.0F);
			if (FM.getSpeed() > 50F && f < 0.6F && !blisterRemoved)
				doRemoveBlister3();
			if (f == 0.0F)
				tiltCanopyOpened = false;
		} else {
			hierMesh().chunkSetAngles("Blister4L_D0", 0.0F, f * canopyMaxAngle, 0.0F);
			if (f == 0.0F)
				slideCanopyOpened = false;
		}
		canopyF = f;
		if (canopyF < 0.01D)
			canopyF = 0.0F;
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	public void moveSteering(float f) {
		hierMesh().chunkSetAngles("GearC2_D0", 0.0F, f, 0.0F);
	}

	public void moveWheelSink() {
		if (FM.Gears.onGround())
			suspension = suspension + 0.008F;
		else
			suspension = suspension - 0.008F;
		if (suspension < 0.0F) {
			suspension = 0.0F;
			if (!FM.isPlayers())
				FM.Gears.bTailwheelLocked = true;
		}
		if (suspension > 0.1F)
			suspension = 0.1F;
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		Aircraft.xyz[1] = suspension / 10F;
		float f = Aircraft.cvt(FM.getSpeed(), 0.0F, 25F, 0.0F, 1.0F);
		float f1 = FM.Gears.gWheelSinking[0] * f + suspension;
		Aircraft.xyz[2] = Aircraft.cvt(f1, 0.0F, 0.24F, 0.0F, 0.24F);
		hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
		hierMesh().chunkSetAngles("GearL31_D0", f1 * 500F, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("GearL32_D0", -f1 * 500F, 0.0F, 0.0F);
		f1 = FM.Gears.gWheelSinking[1] * f + suspension;
		Aircraft.xyz[2] = Aircraft.cvt(f1, 0.0F, 0.24F, 0.0F, 0.24F);
		hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
		hierMesh().chunkSetAngles("GearR31_D0", f1 * 500F, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("GearR32_D0", -f1 * 500F, 0.0F, 0.0F);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		default:
			break;

		case 11: // '\013'
			hierMesh().chunkVisible("WireL_D0", false);
			hierMesh().chunkVisible("WireR_D0", false);
			break;

		case 17: // '\021'
			hierMesh().chunkVisible("WireL_D0", false);
			break;

		case 18: // '\022'
			hierMesh().chunkVisible("WireL_D0", false);
			break;

		case 19: // '\023'
			FM.Gears.hitCentreGear();
			hierMesh().chunkVisible("Antenna_D0", false);
			break;

		case 3: // '\003'
			if (World.Rnd().nextInt(0, 99) < 1) {
				FM.AS.hitEngine(this, 0, 4);
				hitProp(0, j, actor);
				FM.EI.engines[0].setEngineStuck(actor);
				return cut("engine1");
			} else {
				FM.AS.setEngineDies(this, 0);
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

	public float canopyF;
	public boolean tiltCanopyOpened;
	private boolean slideCanopyOpened;
	public boolean blisterRemoved;
	private float suspension;
	public float canopyMaxAngle;
	public boolean hasSelfSealingTank;
	public boolean hasSkis;
	public boolean bChangedPit;

	// TODO: +++ Fix DXXI Danish/Dutch/Sarja4 Compass
	public float gyroDelta = 0.0F;
	// ---

	static {
		Class class1 = DXXI.class;
		Property.set(class1, "originCountry", PaintScheme.countryFinland);
	}
}
