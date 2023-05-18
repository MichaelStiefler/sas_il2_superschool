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

public abstract class HS_129 extends Scheme2 implements TypeStormovikArmored {

	public HS_129() {
		this.canopyF = 0.0F;
		this.fullCanopyOpened = false;
		this.sideWindowOpened = false;
		this.sideWindow = false;
		this.suspR = 0.0F;
		this.suspL = 0.0F;
		this.bChangedPit = true;
		this.pit = null;
		this.slideRWindow = false;
	}

	public void registerPit(CockpitHS_129 cockpiths_129) {
		this.pit = cockpiths_129;
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("pilotarm2_d0", false);
				this.hierMesh().chunkVisible("pilotarm1_d0", false);
				break;
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
		}
	}

	protected void moveFlap(float f) {
		float f1 = -45F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public void update(float f) {
		this.hierMesh().chunkSetAngles("radiator1_d0", 0.0F, -63F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		this.hierMesh().chunkSetAngles("radiator2_d0", 0.0F, -63F * this.FM.EI.engines[1].getControlRadiator(), 0.0F);
		super.update(f);
	}

	public void moveCockpitDoor(float f) {
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.xyz[1] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.xyz[2] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		if (f > this.canopyF) {
			if ((this.FM.Gears.onGround() && this.FM.getSpeed() < 5F || this.fullCanopyOpened) && (this.FM.isPlayers() || this.isNetPlayer())) {
				this.sideWindow = false;
				this.fullCanopyOpened = true;
				Aircraft.xyz[1] = f * 1.0F;
				this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
			} else {
				this.sideWindow = true;
				if (this.pit != null && this.canopyF == 0.0F) { this.slideRWindow = this.pit.isViewRight(); }
				this.sideWindowOpened = true;
				Aircraft.xyz[1] = f * 0.33F;
				if (this.slideRWindow) {
					this.hierMesh().chunkSetLocate("Blister2R_D0", Aircraft.xyz, Aircraft.ypr);
				} else {
					this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
				}
			}
		} else if (this.FM.Gears.onGround() && this.FM.getSpeed() < 5F && !this.sideWindowOpened || this.fullCanopyOpened) {
			this.sideWindow = false;
			Aircraft.xyz[1] = f * 1.0F;
			this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
			if (f == 0.0F) { this.fullCanopyOpened = false; }
		} else {
			this.sideWindow = true;
			Aircraft.xyz[1] = f * 0.33F;
			if (this.slideRWindow) {
				this.hierMesh().chunkSetLocate("Blister2R_D0", Aircraft.xyz, Aircraft.ypr);
			} else {
				this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
			}
			if (f == 0.0F) { this.sideWindowOpened = false; }
		}
		this.canopyF = f;
		if (this.canopyF < 0.01D) { this.canopyF = 0.0F; }
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) { Main3D.cur3D().cockpits[0].onDoorMoved(f); }
			this.setDoorSnd(f);
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
	}

	public void prepareCamouflage() {
		super.prepareCamouflage();
		this.hierMesh().chunkVisible("pilotarm2_d0", true);
		this.hierMesh().chunkVisible("pilotarm1_d0", true);
	}

	protected void moveFan(float f) {
		if (Config.isUSE_RENDER()) {
			super.moveFan(f);
			float f1 = this.FM.CT.getAileron();
			float f2 = this.FM.CT.getElevator();
			this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 25F * f1, f2 * 15F);
			this.hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f1, -1F, 1.0F, 22F, -7F), 0.0F, cvt(f1, -1F, 1.0F, 7F, 2.0F) - cvt(f2, -1F, 1.0F, -18F, 18F));
			this.hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, 3F, 9F) + cvt(f2, -1F, 0.0F, -43F, -12F) + cvt(f2, 0.0F, 1.0F, -12F, 17F));
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 95.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 95.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -35F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -35F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -120F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -120F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -127F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -127F * f, 0.0F);
		float f1 = Math.max(-f * 1500F, -90F);
		if (f1 > -3F) { f1 = 0.0F; }
		hiermesh.chunkSetAngles("GearL9_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR9_D0", 0.0F, -f1, 0.0F);
	}

	protected void moveAileron(float f) {
		super.moveAileron(-f);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveWheelSink() {
		this.suspL = 0.9F * this.suspL + 0.1F * this.FM.Gears.gWheelSinking[0];
		this.suspR = 0.9F * this.suspR + 0.1F * this.FM.Gears.gWheelSinking[1];
		if (this.suspL > 0.035F) { this.suspL = 0.035F; }
		if (this.suspR > 0.035F) { this.suspR = 0.035F; }
		if (this.suspL < 0.0F) { this.suspL = 0.0F; }
		if (this.suspR < 0.0F) { this.suspR = 0.0F; }
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.xyz[1] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.xyz[2] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		float f = 1150F;
		Aircraft.xyz[1] = -this.suspL * 7F;
		this.hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearL7_D0", 0.0F, -this.suspL * f, 0.0F);
		this.hierMesh().chunkSetAngles("GearL8_D0", 0.0F, this.suspL * f, 0.0F);
		Aircraft.xyz[1] = -this.suspR * 7F;
		this.hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearR7_D0", 0.0F, -this.suspR * f, 0.0F);
		this.hierMesh().chunkSetAngles("GearR8_D0", 0.0F, this.suspR * f, 0.0F);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", Aircraft.cvt(f, -65F, 65F, -65F, 65F), 0.0F, 0.0F);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				this.debuggunnery("Armor: Hit..");
				if (s.endsWith("2")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(1.96F, 3.4839F), shot);
				} else if (s.endsWith("3")) {
					if (point3d.z < 0.080000000000000002D) {
						this.getEnergyPastArmor(8.585D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
					} else if (point3d.z < 0.09D) {
						this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
					} else if (point3d.y > 0.175D && point3d.y < 0.287D && point3d.z < 0.177D) {
						this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
					} else if (point3d.y > -0.334D && point3d.y < -0.177D && point3d.z < 0.204D) {
						this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
					} else if (point3d.z > 0.288D && Math.abs(point3d.y) < 0.077D) {
						this.getEnergyPastArmor(World.Rnd().nextFloat(8.5F, 12.46F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
					} else {
						this.getEnergyPastArmor(10.51D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
					}
				} else if (s.endsWith("1")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F), shot);
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
					this.debuggunnery("Armor: Armor Glass: Hit..");
					if (shot.power <= 0.0F) {
						this.debuggunnery("Armor: Armor Glass: Bullet Stopped..");
						if (World.Rnd().nextFloat() < 0.96F) { this.doRicochetBack(shot); }
					}
				} else if (s.endsWith("4")) {
					this.getEnergyPastArmor(5.51D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
					if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40); }
					if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20); }
				} else if (s.endsWith("6")) {
					if (point3d.z > 0.448D) {
						if (point3d.z > 0.609D && Math.abs(point3d.y) > 0.251D) {
							this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
						} else {
							this.getEnergyPastArmor(10.604999542236328D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
						}
					} else if (Math.abs(point3d.y) > 0.264D) {
						if (point3d.z > 0.021000000000000001D) {
							this.getEnergyPastArmor(8.51D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
						} else {
							this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
						}
					} else if (point3d.z < -0.352D && Math.abs(point3d.y) < 0.040000000000000001D) {
						this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
					} else {
						this.getEnergyPastArmor(8.0600004196166992D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
					}
				} else if (s.endsWith("7")) {
					this.getEnergyPastArmor(6.059999942779541D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
				} else if (s.endsWith("8")) {
					if (point3d.y > 0.112D && point3d.z < -0.319D || point3d.y < -0.065000000000000002D && point3d.z > 0.038D && point3d.z < 0.204D) {
						this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
					} else {
						this.getEnergyPastArmor(8.0600004196166992D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
					}
				} else if (s.endsWith("9")) {
					if (point3d.z > 0.611D && point3d.z < 0.674D && Math.abs(point3d.y) < 0.041500000000000002D) {
						this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
					} else {
						this.getEnergyPastArmor(8.0600004196166992D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
					}
				}
			} else if (s.startsWith("xxcontrol")) {
				this.debuggunnery("Controls: Hit..");
				if (World.Rnd().nextFloat() >= 0.99F) {
					int i = new Integer(s.substring(9)).intValue();
					switch (i) {
						case 3:
						case 4:
							if (this.getEnergyPastArmor(3.5F, shot) > 0.0F) {
								this.FM.AS.setControlsDamage(shot.initiator, 2);
								this.debuggunnery("Controls: Rudder Controls: Fuselage Line Destroyed..");
							}
							break;

						case 1:
						case 2:
							if (this.getEnergyPastArmor(0.002F, shot) > 0.0F) {
								this.FM.AS.setControlsDamage(shot.initiator, 1);
								this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
							}
							break;

						case 5:
						case 6:
						case 7:
						case 8:
							if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
								this.FM.AS.setControlsDamage(shot.initiator, 0);
								this.debuggunnery("Controls: Aileron Controls: Disabled..");
							}
							break;
					}
				}
			} else if (s.startsWith("xxspar")) {
				this.debuggunnery("Spar Construction: Hit..");
				if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.5F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.debuggunnery("Spar Construction: Tail1 Spars Broken in Half..");
					this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				}
				if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(17.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(17.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(13.2F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(13.2F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Spar Construction: WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
			} else if (s.startsWith("xxwj")) {
				if (this.getEnergyPastArmor(World.Rnd().nextFloat(12.5F, 55.96F), shot) > 0.0F) {
					if (s.endsWith("l")) {
						this.debuggunnery("Spar Construction: WingL Console Lock Destroyed..");
						this.nextDMGLevels(4, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
					} else {
						this.debuggunnery("Spar Construction: WingR Console Lock Destroyed..");
						this.nextDMGLevels(4, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
					}
				}
			} else if (s.startsWith("xxlock")) {
				this.debuggunnery("Lock Construction: Hit..");
				if (s.startsWith("xxlockr")) {
					int j = s.charAt(6) - 48;
					if (this.getEnergyPastArmor(6.56F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
						if (j < 3) {
							this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
							this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
						} else {
							this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
							this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
						}
					}
				}
				if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
				}
				if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
				}
			} else if (s.startsWith("xxeng")) {
				int k = s.charAt(5) - 49;
				this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Hit..");
				if (s.endsWith("prop")) {
					if (this.getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) {
						if (World.Rnd().nextFloat() < 0.5F) {
							this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 3);
							this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Prop Governor Hit, Disabled..");
						} else {
							this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 4);
							this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Prop Governor Hit, Damaged..");
						}
					}
				} else if (s.endsWith("gear")) {
					if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < 0.5F) {
							this.FM.EI.engines[k].setEngineStuck(shot.initiator);
							this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Bullet Jams Reductor Gear..");
						} else {
							this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 3);
							this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 4);
							this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Reductor Gear Damaged, Prop Governor Failed..");
						}
					}
				} else if (s.endsWith("supc")) {
					if (this.getEnergyPastArmor(2.0F, shot) > 0.0F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 0);
						this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Supercharger Disabled..");
					}
				} else if (s.endsWith("feed")) {
					if (this.getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F && this.FM.EI.engines[k].getPowerOutput() > 0.7F) {
						this.FM.AS.hitEngine(shot.initiator, k, 100);
						this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Pressurized Fuel Line Pierced, Fuel Flamed..");
					}
				} else if (s.endsWith("fuel")) {
					if (this.getEnergyPastArmor(1.1F, shot) > 0.0F) {
						this.FM.EI.engines[k].setEngineStops(shot.initiator);
						this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Fuel Line Stalled, Engine Stalled..");
					}
				} else if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(3.1F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 175000F) {
							this.FM.AS.setEngineStuck(shot.initiator, k);
							this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Bullet Jams Crank Ball Bearing..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, k, 2);
							this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[k].getReadyness() + "..");
						}
						this.FM.EI.engines[k].setReadyness(shot.initiator, this.FM.EI.engines[k].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
						this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[k].getReadyness() + "..");
					}
					this.getEnergyPastArmor(World.Rnd().nextFloat(22.5F, 33.6F), shot);
				} else if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[k].getCylindersRatio() * 1.0F) {
						this.FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
						this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Cylinders Hit, " + this.FM.EI.engines[k].getCylindersOperable() + "/" + this.FM.EI.engines[k].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 24000F) {
							this.FM.AS.hitEngine(shot.initiator, k, 3);
							this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Cylinders Hit, Engine Fires..");
						}
						if (World.Rnd().nextFloat() < 0.01F) {
							this.FM.AS.setEngineStuck(shot.initiator, k);
							this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Bullet Jams Piston Head..");
						}
						this.getEnergyPastArmor(22.5F, shot);
					}
				} else if (s.endsWith("mag1") || s.endsWith("mag2")) {
					if (World.Rnd().nextFloat() < 0.5F) {
						int j2 = s.charAt(9) - 49;
						this.FM.EI.engines[k].setMagnetoKnockOut(shot.initiator, j2);
						this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Magneto " + j2 + " Destroyed..");
					}
				} else if (s.endsWith("oil1")) {
					this.FM.AS.hitOil(shot.initiator, k);
					this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Oil Radiator Hit..");
				}
			} else if (s.startsWith("xxoil")) {
				if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 2.345F), shot) > 0.0F) {
					int l = s.charAt(5) - 49;
					this.FM.AS.hitOil(shot.initiator, l);
					this.getEnergyPastArmor(0.22F, shot);
					this.debuggunnery("Engine Module (" + (l != 0 ? "Right" : "Left") + "): Oil Tank Pierced..");
				}
			} else if (s.startsWith("xxw")) {
				if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 0.75F), shot) > 0.0F) {
					int i1 = s.charAt(3) - 49;
					if (this.FM.AS.astateEngineStates[i1] == 0) {
						this.debuggunnery("Engine Module (" + (i1 != 0 ? "Right" : "Left") + "): Water Radiator Pierced..");
						this.FM.AS.hitEngine(shot.initiator, i1, 2);
						this.FM.AS.doSetEngineState(shot.initiator, i1, 2);
					}
					this.getEnergyPastArmor(2.22F, shot);
				}
			} else if (s.startsWith("xxtank")) {
				int j1 = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 2.23F), shot) > 0.0F && World.Rnd().nextFloat() < 0.1F) {
					if (this.FM.AS.astateTankStates[j1] == 0) {
						this.debuggunnery("Fuel Tank " + (j1 + 1) + ": Pierced..");
						this.FM.AS.hitTank(shot.initiator, j1, 1);
						this.FM.AS.doSetTankState(shot.initiator, j1, 1);
					} else if (this.FM.AS.astateTankStates[j1] == 1) {
						this.debuggunnery("Fuel Tank " + (j1 + 1) + ": Pierced..");
						this.FM.AS.hitTank(shot.initiator, j1, 1);
						this.FM.AS.doSetTankState(shot.initiator, j1, 2);
					}
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.2F) {
						this.FM.AS.hitTank(shot.initiator, 2, 2);
						this.debuggunnery("Fuel Tank " + (j1 + 1) + ": Hit..");
					}
				}
			} else if (s.startsWith("xxhyd")) {
				if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F) {
					this.debuggunnery("Hydro System: Disabled..");
					this.FM.AS.setInternalDamage(shot.initiator, 0);
				}
			} else if (s.startsWith("xxrevi")) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
			} else {
				if (s.startsWith("xxmgun")) {
					if (s.endsWith("01")) {
						this.debuggunnery("left side MG17: Disabled..");
						this.FM.AS.setJamBullets(0, 0);
					}
					if (s.endsWith("02")) {
						this.debuggunnery("right side MG17: Disabled..");
						this.FM.AS.setJamBullets(0, 1);
					}
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 12.96F), shot);
				}
				if (s.startsWith("xxcannon")) {
					if (s.endsWith("01")) {
						this.debuggunnery("left side cannon: Disabled..");
						this.FM.AS.setJamBullets(0, 2);
					}
					if (s.endsWith("02")) {
						this.debuggunnery("right side cannon: Disabled..");
						this.FM.AS.setJamBullets(0, 3);
					}
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
				}
				if (s.startsWith("xmk101")) {
					this.debuggunnery("MK101: Disabled..");
					this.FM.AS.setJamBullets(1, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
				}
				if (s.startsWith("xmk103")) {
					this.debuggunnery("MK103: Disabled..");
					this.FM.AS.setJamBullets(1, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
				}
				if (s.startsWith("xbk75gun")) {
					this.debuggunnery("BK75: Disabled..");
					this.FM.AS.setJamBullets(1, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
				}
				if (s.startsWith("xbk37gun")) {
					this.debuggunnery("BK37: Disabled..");
					this.FM.AS.setJamBullets(1, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
				}
				if (s.startsWith("xmg17")) {
					float f = World.Rnd().nextFloat(0.0F, 4F);
					if (f < 1.5F) {
						this.FM.AS.setJamBullets(1, 0);
					} else if (f < 2.0F) {
						this.FM.AS.setJamBullets(1, 1);
					} else if (f < 2.5F) {
						this.FM.AS.setJamBullets(1, 2);
					} else {
						this.FM.AS.setJamBullets(1, 3);
					}
					this.debuggunnery("one of 4xMG17: Disabled..");
					this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
				}
			}
		} else if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
			if (this.chunkDamageVisible("CF") < 3) { this.hitChunk("CF", shot); }
		} else if (s.startsWith("xblister")) {
			if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1); }
			if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4); }
			if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8); }
		} else if (s.startsWith("xengine")) {
			int k1 = s.charAt(7) - 48;
			if (this.chunkDamageVisible("Engine" + k1) < 2) { this.hitChunk("Engine" + k1, shot); }
			this.bChangedPit = true;
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		} else if (s.startsWith("xkeel")) {
			int l1 = s.charAt(5) - 48;
			if (this.chunkDamageVisible("Keel" + l1) < 3) { this.hitChunk("Keel" + l1, shot); }
			if (this.hierMesh().isChunkVisible("keel1_d2")) { this.hierMesh().chunkVisible("Wire_D0", false); }
		} else if (s.startsWith("xrudder")) {
			int i2 = s.charAt(7) - 48;
			if (this.chunkDamageVisible("Rudder" + i2) < 1) { this.hitChunk("Rudder" + i2, shot); }
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 3) { this.hitChunk("StabL", shot); }
			if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 3) { this.hitChunk("StabR", shot); }
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 1) { this.hitChunk("VatorL", shot); }
			if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 1) { this.hitChunk("VatorR", shot); }
		} else if (s.startsWith("xwing")) {
			if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) {
				this.hitChunk("WingLIn", shot);
				this.bChangedPit = true;
			}
			if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) {
				this.hitChunk("WingRIn", shot);
				this.bChangedPit = true;
			}
			if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) { this.hitChunk("WingLMid", shot); }
			if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) { this.hitChunk("WingRMid", shot); }
			if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) { this.hitChunk("WingLOut", shot); }
			if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) { this.hitChunk("WingROut", shot); }
		} else if (s.startsWith("xarone")) {
			if (s.startsWith("xaronel")) { this.hitChunk("AroneL", shot); }
			if (s.startsWith("xaroner")) { this.hitChunk("AroneR", shot); }
		} else if (s.startsWith("xgear")) {
			if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
				this.debuggunnery("Hydro System: Disabled..");
				this.FM.AS.setInternalDamage(shot.initiator, 0);
			}
			if (s.endsWith("2")) {
				if (World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(6.8F, 29.35F), shot) > 0.0F) {
					this.debuggunnery("Undercarriage: Stuck..");
					this.FM.AS.setInternalDamage(shot.initiator, 3);
				}
				String s1 = "" + s.charAt(5);
				this.hitChunk("Gear" + s1.toUpperCase() + "2", shot);
			}
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int k2;
			if (s.endsWith("a")) {
				byte0 = 1;
				k2 = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				k2 = s.charAt(6) - 49;
			} else {
				k2 = s.charAt(5) - 49;
			}
			this.hitFlesh(k2, shot, byte0);
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 36:
				this.bChangedPit = true;
				break;

			case 33:
				this.bChangedPit = true;
				break;

			case 3:
				this.bChangedPit = true;
				break;

			case 4:
				this.bChangedPit = true;
				break;

			case 19:
				this.FM.Gears.hitCentreGear();
				this.hierMesh().chunkVisible("Wire_D0", false);
				break;

			case 11:
				this.hierMesh().chunkVisible("Wire_D0", false);
				break;

			case 10:
				this.doWreck("GearR3_D0");
				this.FM.Gears.hitRightGear();
				break;

			case 9:
				this.doWreck("GearL3_D0");
				this.FM.Gears.hitLeftGear();
				break;
		}
		return super.cutFM(i, j, actor);
	}

	private void doWreck(String s) {
		if (this.hierMesh().chunkFindCheck(s) != -1) {
			this.hierMesh().hideSubTrees(s);
			Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind(s));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(this.FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
	}

	public float    canopyF;
	private boolean fullCanopyOpened;
	private boolean sideWindowOpened;
	public boolean  sideWindow;
	float           suspR;
	float           suspL;
	public boolean  bChangedPit;
	CockpitHS_129   pit;
	private boolean slideRWindow;

	static {
		Class class1 = HS_129.class;
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
	}
}
