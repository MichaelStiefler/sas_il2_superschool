package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public abstract class G_55xyz extends Scheme1 implements TypeFighter {

	public G_55xyz() {
		this.canopyF = 0.0F;
		this.tiltCanopyOpened = false;
		this.slideCanopyOpened = false;
		this.blisterRemoved = false;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.Gears.computePlaneLandPose(this.FM);
		this.mydebuggunnery("H = " + this.FM.Gears.H);
		this.mydebuggunnery("Pitch = " + this.FM.Gears.Pitch);
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

	protected boolean cutFM(int i, int j, Actor actor) {
		if (i == 19) { this.FM.Gears.hitCentreGear(); }
		return super.cutFM(i, j, actor);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("hmask1_D0", false);
		} else {
			this.hierMesh().chunkVisible("hmask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
		}
		if (this.tiltCanopyOpened && !this.blisterRemoved && this.FM.getSpeed() > 75F) { this.doRemoveBlister1(); }
		if (flag) {
			if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.39F) { this.FM.AS.hitTank(this, 0, 1); }
			if (this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.1F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this); }
			if (this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.1F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this); }
			if (this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.1F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this); }
			if (this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.1F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this); }
		}
	}

	private final void doRemoveBlister1() {
		this.blisterRemoved = true;
		if (this.hierMesh().chunkFindCheck("Blister1_D0") != -1) {
			this.hierMesh().hideSubTrees("Blister1_D0");
			Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(this.FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
	}

	public void moveCockpitDoor(float f) {
		if (f > this.canopyF) {
			if ((this.FM.Gears.onGround() && this.FM.getSpeed() < 5F || this.tiltCanopyOpened) && (this.FM.isPlayers() || this.isNetPlayer())) {
				this.tiltCanopyOpened = true;
				this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
			} else {
				this.slideCanopyOpened = true;
				this.resetYPRmodifier();
				Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.28F);
				this.hierMesh().chunkSetLocate("Blister4L_D0", Aircraft.xyz, Aircraft.ypr);
			}
		} else if (this.FM.Gears.onGround() && this.FM.getSpeed() < 5F && !this.slideCanopyOpened || this.tiltCanopyOpened) {
			this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
			if (this.FM.getSpeed() > 70F && f < 0.6F && !this.blisterRemoved) { this.doRemoveBlister1(); }
			if (f == 0.0F) { this.tiltCanopyOpened = false; }
		} else {
			this.resetYPRmodifier();
			Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.3F);
			this.hierMesh().chunkSetLocate("Blister4L_D0", Aircraft.xyz, Aircraft.ypr);
			if (f == 0.0F) { this.slideCanopyOpened = false; }
		}
		this.canopyF = f;
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) { Main3D.cur3D().cockpits[0].onDoorMoved(f); }
			this.setDoorSnd(f);
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.18F, 0.99F, 0.0F, 90F), 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.2F, 0.0F, 90F), 0.0F);
		hiermesh.chunkSetAngles("Gearl6_D0", 0.0F, Aircraft.cvt(f, 0.18F, 0.99F, 0.0F, -30F), 0.0F);
		Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.3F, 0.99F, 0.0F, 0.45F);
		hiermesh.chunkSetLocate("Gearl9_D0", Aircraft.xyz, Aircraft.ypr);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.18F, 0.99F, 0.0F, 90F), 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.2F, 0.0F, 90F), 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.18F, 0.99F, 0.0F, 30F), 0.0F);
		Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.3F, 0.99F, 0.0F, 0.45F);
		hiermesh.chunkSetLocate("GearR9_D0", Aircraft.xyz, Aircraft.ypr);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.67F, 0.0F, -70F), 0.0F);
		hiermesh.chunkSetAngles("GearC3L_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.15F, 0.0F, 80F), 0.0F);
		hiermesh.chunkSetAngles("GearC3R_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.15F, 0.0F, -80F), 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, f, 0.0F);
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.2085F, 0.0F, 0.2085F);
		this.hierMesh().chunkSetLocate("GearL8_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.2085F, 0.0F, 0.2085F);
		this.hierMesh().chunkSetLocate("GearR8_D0", Aircraft.xyz, Aircraft.ypr);
	}

	protected void moveFlap(float f) {
		float f1 = -35F * f;
		this.hierMesh().chunkSetAngles("FlapInL_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("FlapInR_D0", 0.0F, f1, 0.0F);
		f1 = -35F * f;
		this.hierMesh().chunkSetAngles("FlapOutL_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("FlapOutR_D0", 0.0F, f1, 0.0F);
	}

	public void update(float f) {
		this.hierMesh().chunkSetAngles("Filter_D0", 0.0F, -20F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		super.update(f);
	}

	protected void setControlDamage(Shot shot, int i) {
		if (World.Rnd().nextFloat() < 0.002F && this.getEnergyPastArmor(4F, shot) > 0.0F) {
			this.FM.AS.setControlsDamage(shot.initiator, i);
			this.mydebuggunnery(i + " Controls Out... //0 = AILERON, 1 = ELEVATOR, 2 = RUDDER");
		}
	}

	protected void moveAileron(float f) {
		float f1 = -(f * 30F);
		this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, f1, 0.0F);
		f1 = -(f * 30F);
		this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, f1, 0.0F);
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -31F * f, 0.0F);
	}

	protected void moveElevator(float f) {
		if (f < 0.0F) {
			this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -20F * f, 0.0F);
			this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -20F * f, 0.0F);
		} else {
			this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
			this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				this.mydebuggunnery("Armor: Hit..");
				if (s.startsWith("xxarmorp")) {
					int i = s.charAt(8) - 48;
					switch (i) {
						case 1:
							this.getEnergyPastArmor(22.76D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
							if (shot.power <= 0.0F) { this.doRicochetBack(shot); }
							break;

						case 3:
							this.getEnergyPastArmor(9.366F, shot);
							break;

						case 5:
							this.getEnergyPastArmor(12.699999809265137D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
							break;
					}
				}
			} else if (s.startsWith("xxcontrols")) {
				this.mydebuggunnery("Controls: Hit..");
				int j = s.charAt(10) - 48;
				switch (j) {
					case 1:
					case 2:
						if (this.getEnergyPastArmor(0.25F / ((float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z) + 0.0001F), shot) > 0.0F) {
							if (World.Rnd().nextFloat() < 0.05F) {
								this.mydebuggunnery("Controls: Elevator Wiring Hit, Elevator Controls Disabled..");
								this.FM.AS.setControlsDamage(shot.initiator, 1);
							}
							if (World.Rnd().nextFloat() < 0.75F) {
								this.mydebuggunnery("Controls: Rudder Wiring Hit, Rudder Controls Disabled..");
								this.FM.AS.setControlsDamage(shot.initiator, 2);
							}
						}
						break;

					case 3:
						if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
							this.mydebuggunnery("*** Control Column: Hit, Controls Destroyed..");
							this.FM.AS.setControlsDamage(shot.initiator, 2);
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 4:
						if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
							this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
							this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
							this.mydebuggunnery("Quadrant: Hit, Engine Controls Disabled..");
						}
						break;

					case 5:
					case 7:
						if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
							this.FM.AS.setControlsDamage(shot.initiator, 0);
							this.mydebuggunnery("*** Aileron Controls: Control Crank Destroyed..");
						}
						break;

					case 6:
					case 8:
						if (this.getEnergyPastArmor(4D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
							this.mydebuggunnery("Controls: Aileron Wiring Hit, Aileron Controls Disabled..");
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;
				}
			} else if (s.startsWith("xxspar")) {
				this.mydebuggunnery("Spar Construction: Hit..");
				if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					this.mydebuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					this.mydebuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					this.mydebuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					this.mydebuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.86F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.mydebuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				}
			} else {
				if (s.startsWith("xxlock")) {
					this.mydebuggunnery("Lock Construction: Hit..");
					if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
						this.mydebuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
						this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
					}
					if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
						this.mydebuggunnery("Lock Construction: VatorL Lock Shot Off..");
						this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
					}
					if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
						this.mydebuggunnery("Lock Construction: VatorR Lock Shot Off..");
						this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
					}
					if (s.startsWith("xxlockalL") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
						this.mydebuggunnery("Lock Construction: AroneL Lock Shot Off..");
						this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
					}
					if (s.startsWith("xxlockalR") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
						this.mydebuggunnery("Lock Construction: AroneR Lock Shot Off..");
						this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
					}
				}
				if (s.startsWith("xxeng")) {
					if ((s.endsWith("prop") || s.endsWith("pipe")) && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) { this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator); }
					if (s.endsWith("case") || s.endsWith("gear")) {
						if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
							if (World.Rnd().nextFloat() < shot.power / 140000F) {
								this.FM.AS.setEngineStuck(shot.initiator, 0);
								this.mydebuggunnery("*** Engine Crank Case Hit - Engine Stucks..");
							}
							if (World.Rnd().nextFloat() < shot.power / 85000F) {
								this.FM.AS.hitEngine(shot.initiator, 0, 2);
								this.mydebuggunnery("*** Engine Crank Case Hit - Engine Damaged..");
							}
						} else if (World.Rnd().nextFloat() < 0.02F) {
							this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
						} else {
							this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
							this.mydebuggunnery("*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
						}
						this.getEnergyPastArmor(12F, shot);
					}
					if (s.endsWith("cyl1") || s.endsWith("cyl2")) {
						if (this.getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 4F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.75F) {
							this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
							this.mydebuggunnery("*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
							if (World.Rnd().nextFloat() < shot.power / 48000F) {
								this.FM.AS.hitEngine(shot.initiator, 0, 2);
								this.mydebuggunnery("*** Engine Cylinders Hit - Engine Fires..");
							}
						}
						this.getEnergyPastArmor(25F, shot);
					}
					if (s.endsWith("supc")) {
						if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) { this.FM.EI.engines[0].setKillCompressor(shot.initiator); }
						this.getEnergyPastArmor(2.0F, shot);
					}
					if (s.startsWith("xxeng1oil")) {
						this.FM.AS.hitOil(shot.initiator, 0);
						this.mydebuggunnery("*** Engine Module: Oil Radiator Hit..");
					}
					this.mydebuggunnery("*** Engine state = " + this.FM.AS.astateEngineStates[0]);
				} else if (s.startsWith("xxtank")) {
					int k = s.charAt(6) - 49;
					if (this.getEnergyPastArmor(0.19F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
						if (this.FM.AS.astateTankStates[k] == 0) {
							this.mydebuggunnery("Fuel System: Fuel Tank Pierced..");
							this.FM.AS.hitTank(shot.initiator, k, 1);
							this.FM.AS.doSetTankState(shot.initiator, k, 1);
						} else if (this.FM.AS.astateTankStates[k] == 1) {
							this.mydebuggunnery("Fuel System: Fuel Tank Pierced (2)..");
							this.FM.AS.hitTank(shot.initiator, k, 1);
							this.FM.AS.doSetTankState(shot.initiator, k, 2);
						}
						if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) {
							this.FM.AS.hitTank(shot.initiator, k, 2);
							this.mydebuggunnery("Fuel System: Fuel Tank Pierced, State Shifted..");
						}
					}
					this.mydebuggunnery("Tank State: " + this.FM.AS.astateTankStates[k]);
				} else if (s.startsWith("xxmgun")) {
					if (s.endsWith("01")) {
						this.mydebuggunnery("Armament System: Left Machine Gun: Disabled..");
						this.FM.AS.setJamBullets(0, 0);
					}
					if (s.endsWith("02")) {
						this.mydebuggunnery("Armament System: Right Machine Gun: Disabled..");
						this.FM.AS.setJamBullets(1, 0);
					}
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
				} else if (s.startsWith("xxcannon")) {
					if (s.endsWith("02")) {
						this.mydebuggunnery("Armament System: Left Cannon: Disabled..");
						this.FM.AS.setJamBullets(3, 0);
					}
					if (s.endsWith("03")) {
						this.mydebuggunnery("Armament System: Right Cannon: Disabled..");
						this.FM.AS.setJamBullets(4, 0);
					}
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
				}
			}
		} else if (s.startsWith("xcf")) {
			this.setControlDamage(shot, 0);
			this.setControlDamage(shot, 1);
			this.setControlDamage(shot, 2);
			if (this.chunkDamageVisible("CF") < 3) { this.hitChunk("CF", shot); }
		} else if (s.startsWith("xeng")) {
			if (this.chunkDamageVisible("Engine1") < 2) { this.hitChunk("Engine1", shot); }
		} else if (s.startsWith("xtail")) {
			this.setControlDamage(shot, 1);
			this.setControlDamage(shot, 2);
			if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		} else if (s.startsWith("xkeel")) {
			this.hitChunk("Keel1", shot);
		} else if (s.startsWith("xrudder")) {
			this.setControlDamage(shot, 2);
			if (this.chunkDamageVisible("Rudder1") < 1) { this.hitChunk("Rudder1", shot); }
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl")) { this.hitChunk("StabL", shot); }
			if (s.startsWith("xstabr")) { this.hitChunk("StabR", shot); }
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 1) { this.hitChunk("VatorL", shot); }
			if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 1) { this.hitChunk("VatorR", shot); }
		} else if (s.startsWith("xwing")) {
			if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) {
				this.setControlDamage(shot, 0);
				this.hitChunk("WingLIn", shot);
			}
			if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) {
				this.setControlDamage(shot, 0);
				this.hitChunk("WingRIn", shot);
			}
			if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) {
				this.setControlDamage(shot, 0);
				this.hitChunk("WingLMid", shot);
			}
			if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) {
				this.setControlDamage(shot, 0);
				this.hitChunk("WingRMid", shot);
			}
			if (s.startsWith("xwinglout1") && this.chunkDamageVisible("WingLOut") < 3) { this.hitChunk("WingLOut", shot); }
			if (s.startsWith("xwingrout1") && this.chunkDamageVisible("WingROut") < 3) { this.hitChunk("WingROut", shot); }
		} else if (s.startsWith("xarone")) {
			if (s.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 1) { this.hitChunk("AroneL", shot); }
			if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneR") < 1) { this.hitChunk("AroneR", shot); }
		} else if (s.startsWith("xgear")) {
			if (World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F) {
				this.mydebuggunnery("Undercarriage: Stuck..");
				this.FM.AS.setInternalDamage(shot.initiator, 3);
			}
		} else if (s.startsWith("xoil")) {
			if (World.Rnd().nextFloat() < 0.12F) {
				this.FM.AS.hitOil(shot.initiator, 0);
				this.mydebuggunnery("*** Engine Module: Oil Radiator Hit..");
			}
		} else if (!s.startsWith("xblister") && (s.startsWith("xpilot") || s.startsWith("xhead"))) {
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

	protected void mydebuggunnery(String s) {
	}

	public float    canopyF;
	private boolean tiltCanopyOpened;
	private boolean slideCanopyOpened;
	private boolean blisterRemoved;

	static {
		Class class1 = G_55xyz.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "originCountry", PaintScheme.countryItaly);
	}
}
