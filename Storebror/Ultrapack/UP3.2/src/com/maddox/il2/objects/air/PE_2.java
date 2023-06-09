package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class PE_2 extends Scheme2a {

	public PE_2() {
		this.fSightCurAltitude = 300F;
		this.fSightCurSpeed = 50F;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightSetForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.AS.wantBeaconsNet(true);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearC99_D0", 0.0F, 75F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 112.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -27F * (float) Math.sin(f * 3.1415926535897931D), 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -170.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 112.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -27F * (float) Math.sin(f * 3.1415926535897931D), 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 170.5F * f, 0.0F);
		float f1 = Math.max(-f * 1500F, -90F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.833333F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, -0.833333F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -f1, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		if (this.FM.CT.getGear() < 0.98F) {
			return;
		} else {
			this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 0.0F, -f);
			return;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0: // '\0'
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				break;

			case 1: // '\001'
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Head2_D0", false);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				break;
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxArmor")) {
				this.debuggunnery("Armor: Hit..");
				if (s.endsWith("p1")) {
					this.getEnergyPastArmor(12.699999809265137D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
				} else if (s.endsWith("p2")) {
					this.getEnergyPastArmor(12.699999809265137D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
				} else if (s.endsWith("p3")) {
					this.getEnergyPastArmor(12.699999809265137D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
				} else if (s.endsWith("p4")) { this.getEnergyPastArmor(9D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot); }
				return;
			}
			if (s.startsWith("xxcontrols")) {
				this.debuggunnery("Controls: Hit..");
				int i = s.charAt(10) - 48;
				switch (i) {
					default:
						break;

					case 3: // '\003'
						if (this.getEnergyPastArmor(4.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
							this.debuggunnery("Controls: Elevator Controls: Disabled..");
							this.FM.AS.setControlsDamage(shot.initiator, 1);
						}
						if (this.getEnergyPastArmor(0.002F, shot) > 0.0F && World.Rnd().nextFloat() < 0.11F) {
							this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
							this.FM.AS.setControlsDamage(shot.initiator, 2);
						}
						break;

					case 4: // '\004'
					case 5: // '\005'
						if (this.getEnergyPastArmor(0.99F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
							this.debuggunnery("Controls: Ailerones Controls: Out..");
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;
				}
				return;
			}
			if (s.startsWith("xxbomb")) {
				if (World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
					this.debuggunnery("*** Bomb Payload Detonates..");
					this.FM.AS.hitTank(shot.initiator, 0, 100);
					this.FM.AS.hitTank(shot.initiator, 1, 100);
					this.FM.AS.hitTank(shot.initiator, 2, 100);
					this.FM.AS.hitTank(shot.initiator, 3, 100);
					this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxeng1")) {
				if (s.endsWith("prop") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F) {
					this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
					debugprintln(this, "*** Engine1 Prop Governor Failed..");
				}
				if (s.endsWith("gear") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F) {
					this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
					debugprintln(this, "*** Engine1 Prop Governor Damaged..");
				}
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 200000F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							debugprintln(this, "*** Engine1 Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							debugprintln(this, "*** Engine1 Crank Case Hit - Engine Damaged..");
						}
						if (World.Rnd().nextFloat() < shot.power / 28000F) {
							this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
							debugprintln(this, "*** Engine1 Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
						}
						if (World.Rnd().nextFloat() < 0.08F) {
							this.FM.EI.engines[0].setEngineStuck(shot.initiator);
							debugprintln(this, "*** Engine1 Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
						}
						this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
						debugprintln(this, "*** Engine1 Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
					}
					if (World.Rnd().nextFloat() < 0.01F) {
						this.FM.EI.engines[0].setEngineStops(shot.initiator);
						debugprintln(this, "*** Engine1 Crank Case Hit - Engine Stalled..");
					}
					if (World.Rnd().nextFloat() < 0.01F) {
						this.FM.AS.hitEngine(shot.initiator, 0, 10);
						debugprintln(this, "*** Engine1 Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
					}
					this.getEnergyPastArmor(6F, shot);
				}
				if ((s.endsWith("cyl1") || s.endsWith("cyl2")) && this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.72F) {
					this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
					debugprintln(this, "*** Engine1 Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
					if (World.Rnd().nextFloat() < 0.01F) {
						this.FM.EI.engines[0].setEngineStuck(shot.initiator);
						debugprintln(this, "*** Engine1 Cylinder Case Broken - Engine Stuck..");
					}
					if (World.Rnd().nextFloat() < shot.power / 24000F) {
						this.FM.AS.hitEngine(shot.initiator, 0, 3);
						debugprintln(this, "*** Engine1 Cylinders Hit - Engine Fires..");
					}
					this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
				}
				if (s.endsWith("supc") && this.getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
					this.FM.EI.engines[0].setKillCompressor(shot.initiator);
					debugprintln(this, "*** Engine1 Supercharger Out..");
				}
				if (s.endsWith("eqpt") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
					if (World.Rnd().nextFloat() < 0.11F) {
						this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
						debugprintln(this, "*** Engine1 Magneto Out..");
					}
					if (World.Rnd().nextFloat() < 0.11F) {
						this.FM.EI.engines[0].setKillCompressor(shot.initiator);
						debugprintln(this, "*** Engine1 Compressor Feed Out..");
					}
				}
				return;
			}
			if (s.startsWith("xxeng2")) {
				if (s.endsWith("prop") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F) {
					this.FM.EI.engines[1].setKillPropAngleDevice(shot.initiator);
					debugprintln(this, "*** Engine2 Prop Governor Failed..");
				}
				if (s.endsWith("gear") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F) {
					this.FM.EI.engines[1].setKillPropAngleDeviceSpeeds(shot.initiator);
					debugprintln(this, "*** Engine2 Prop Governor Damaged..");
				}
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 200000F) {
							this.FM.AS.setEngineStuck(shot.initiator, 1);
							debugprintln(this, "*** Engine2 Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, 1, 2);
							debugprintln(this, "*** Engine2 Crank Case Hit - Engine Damaged..");
						}
						if (World.Rnd().nextFloat() < shot.power / 28000F) {
							this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, 1);
							debugprintln(this, "*** Engine2 Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[1].getCylindersOperable() + "/" + this.FM.EI.engines[1].getCylinders() + " Left..");
						}
						if (World.Rnd().nextFloat() < 0.08F) {
							this.FM.EI.engines[1].setEngineStuck(shot.initiator);
							debugprintln(this, "*** Engine2 Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
						}
						this.FM.EI.engines[1].setReadyness(shot.initiator, this.FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
						debugprintln(this, "*** Engine2 Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[1].getReadyness() + "..");
					}
					if (World.Rnd().nextFloat() < 0.01F) {
						this.FM.EI.engines[1].setEngineStops(shot.initiator);
						debugprintln(this, "*** Engine2 Crank Case Hit - Engine Stalled..");
					}
					if (World.Rnd().nextFloat() < 0.01F) {
						this.FM.AS.hitEngine(shot.initiator, 1, 10);
						debugprintln(this, "*** Engine2 Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
					}
					this.getEnergyPastArmor(6F, shot);
				}
				if ((s.endsWith("cyl1") || s.endsWith("cyl2")) && this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[1].getCylindersRatio() * 1.72F) {
					this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
					debugprintln(this, "*** Engine2 Cylinders Hit, " + this.FM.EI.engines[1].getCylindersOperable() + "/" + this.FM.EI.engines[1].getCylinders() + " Left..");
					if (World.Rnd().nextFloat() < 0.01F) {
						this.FM.EI.engines[1].setEngineStuck(shot.initiator);
						debugprintln(this, "*** Engine2 Cylinder Case Broken - Engine Stuck..");
					}
					if (World.Rnd().nextFloat() < shot.power / 24000F) {
						this.FM.AS.hitEngine(shot.initiator, 1, 3);
						debugprintln(this, "*** Engine2 Cylinders Hit - Engine Fires..");
					}
					this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
				}
				if (s.endsWith("supc") && this.getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
					this.FM.EI.engines[1].setKillCompressor(shot.initiator);
					debugprintln(this, "*** Engine2 Supercharger Out..");
				}
				if (s.endsWith("eqpt") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
					if (World.Rnd().nextFloat() < 0.11F) {
						this.FM.EI.engines[1].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
						debugprintln(this, "*** Engine2 Magneto Out..");
					}
					if (World.Rnd().nextFloat() < 0.11F) {
						this.FM.EI.engines[1].setKillCompressor(shot.initiator);
						debugprintln(this, "*** Engine2 Compressor Feed Out..");
					}
				}
				return;
			}
			if (s.startsWith("xxlock")) {
				this.debuggunnery("Lock Construction: Hit..");
				if (s.startsWith("xxlockr")) {
					if ((s.startsWith("xxlockr1") || s.startsWith("xxlockr2")) && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
						this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
						this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
					}
					if ((s.startsWith("xxlockr3") || s.startsWith("xxlockr4")) && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
						this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
						this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
					}
				}
				if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
				}
				if (s.startsWith("xxlockvR") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
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
			if (s.startsWith("xxMgun0")) {
				int j = s.charAt(7) - 49;
				if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
					this.debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
					this.FM.AS.setJamBullets(0, j);
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
				}
				return;
			}
			if (s.startsWith("xxoil1")) {
				if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.FM.AS.hitOil(shot.initiator, 0);
					this.getEnergyPastArmor(0.22F, shot);
					this.debuggunnery("Engine Module: Oil Radiator 1 Pierced..");
				}
				return;
			}
			if (s.startsWith("xxoil2")) {
				if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.FM.AS.hitOil(shot.initiator, 1);
					this.getEnergyPastArmor(0.22F, shot);
					this.debuggunnery("Engine Module: Oil Radiator 2 Pierced..");
				}
				return;
			}
			if (s.startsWith("xxprib")) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
				this.getEnergyPastArmor(4.88F, shot);
				return;
			}
			if (s.startsWith("xxtank")) {
				int k = s.charAt(6) - 48;
				switch (k) {
					case 1: // '\001'
						this.doHitMeATank(shot, 1);
						this.doHitMeATank(shot, 2);
						break;

					case 2: // '\002'
						this.doHitMeATank(shot, 1);
						break;

					case 3: // '\003'
						this.doHitMeATank(shot, 2);
						break;

					case 4: // '\004'
						this.doHitMeATank(shot, 0);
						break;

					case 5: // '\005'
						this.doHitMeATank(shot, 3);
						break;
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(7.5F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.2F) {
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
				if (point3d.x > 2.2000000000000002D) {
					if (World.Rnd().nextFloat() < 0.73F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1); }
				} else if (point3d.y > 0.0D) {
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
				} else {
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
				}
				if (World.Rnd().nextFloat() < 0.125F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40); }
				if (World.Rnd().nextFloat() < 0.125F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2); }
			}
			return;
		}
		if (s.startsWith("xnose")) {
			if (this.chunkDamageVisible("Nose1") < 2) {
				this.hitChunk("Nose1", shot);
				if (World.Rnd().nextFloat() < 0.125F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20); }
				if (World.Rnd().nextFloat() < 0.125F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8); }
			}
		} else if (s.startsWith("xengine1")) {
			if (this.chunkDamageVisible("Engine1") < 2) { this.hitChunk("Engine1", shot); }
		} else if (s.startsWith("xengine2")) {
			if (this.chunkDamageVisible("Engine2") < 2) { this.hitChunk("Engine2", shot); }
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		} else if (s.startsWith("xkeel1")) {
			if (this.chunkDamageVisible("Keel1") < 2) { this.hitChunk("Keel1", shot); }
		} else if (s.startsWith("xkeel2")) {
			if (this.chunkDamageVisible("Keel2") < 2) { this.hitChunk("Keel2", shot); }
		} else if (s.startsWith("xrudder1")) {
			if (this.chunkDamageVisible("Rudder1") < 1) { this.hitChunk("Rudder1", shot); }
		} else if (s.startsWith("xrudder2")) {
			if (this.chunkDamageVisible("Rudder2") < 1) { this.hitChunk("Rudder2", shot); }
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
			if (s.startsWith("xhead")) {
				l = s.charAt(5) - 49;
			} else if (s.endsWith("a")) {
				byte0 = 1;
				l = s.charAt(6) - 49;
			} else {
				byte0 = 2;
				l = s.charAt(6) - 49;
			}
			this.hitFlesh(l, shot, byte0);
		}
	}

	private final void doHitMeATank(Shot shot, int i) {
		if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
			if (shot.power < 14100F) {
				if (this.FM.AS.astateTankStates[i] == 0) {
					this.FM.AS.hitTank(shot.initiator, i, 1);
					this.FM.AS.doSetTankState(shot.initiator, i, 1);
				}
				if (this.FM.AS.astateTankStates[i] > 0 && (World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)) { this.FM.AS.hitTank(shot.initiator, i, 2); }
			} else {
				this.FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int) (shot.power / 56000F)));
			}
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag) {
			if (this.FM.AS.astateEngineStates[0] > 3) {
				if (this.FM.AS.astateTankStates[1] < 4 && World.Rnd().nextFloat() < 0.025F) { this.FM.AS.hitTank(this, 1, 1); }
				if (this.FM.getSpeedKMH() > 200F && World.Rnd().nextFloat() < 0.025F) { this.nextDMGLevel("Keel1_D0", 0, this); }
				if (this.FM.getSpeedKMH() > 200F && World.Rnd().nextFloat() < 0.025F) { this.nextDMGLevel("StabL_D0", 0, this); }
				if (World.Rnd().nextFloat() < 0.25F) { this.nextDMGLevel("WingLIn_D0", 0, this); }
			}
			if (this.FM.AS.astateEngineStates[1] > 3) {
				if (this.FM.AS.astateTankStates[2] < 4 && World.Rnd().nextFloat() < 0.025F) { this.FM.AS.hitTank(this, 2, 1); }
				if (this.FM.getSpeedKMH() > 200F && World.Rnd().nextFloat() < 0.025F) { this.nextDMGLevel("Keel2_D0", 0, this); }
				if (this.FM.getSpeedKMH() > 200F && World.Rnd().nextFloat() < 0.025F) { this.nextDMGLevel("StabR_D0", 0, this); }
				if (World.Rnd().nextFloat() < 0.25F) { this.nextDMGLevel("WingRIn_D0", 0, this); }
			}
		}
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
		}
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask2_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
		}
		if (this.hierMesh().chunkFindCheck("HMask3_D0") > 0) {
			if (this.FM.getAltitude() < 3000F) {
				this.hierMesh().chunkVisible("HMask3_D0", false);
			} else {
				this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
			}
		}
		if (this.hierMesh().chunkFindCheck("HMask3a_D0") > 0) {
			if (this.FM.getAltitude() < 3000F) {
				this.hierMesh().chunkVisible("HMask3a_D0", false);
			} else {
				this.hierMesh().chunkVisible("HMask3a_D0", this.hierMesh().isChunkVisible("Pilot3a_D0"));
			}
		}
		if (this.hierMesh().chunkFindCheck("HMask3b_D0") > 0) {
			if (this.FM.getAltitude() < 3000F) {
				this.hierMesh().chunkVisible("HMask3b_D0", false);
			} else {
				this.hierMesh().chunkVisible("HMask3b_D0", this.hierMesh().isChunkVisible("Pilot3b_D0"));
			}
		}
		if (this.hierMesh().chunkFindCheck("HMask3c_D0") > 0) {
			if (this.FM.getAltitude() < 3000F) {
				this.hierMesh().chunkVisible("HMask3c_D0", false);
			} else {
				this.hierMesh().chunkVisible("HMask3c_D0", this.hierMesh().isChunkVisible("Pilot3c_D0"));
			}
		}
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
			case 1: // '\001'
				this.FM.turret[0].setHealth(f);
				break;

			case 2: // '\002'
				this.FM.turret[1].setHealth(f);
				// TODO: Storebror: Pe-2 gunner bugfix
				// ------------------------------------
				if (this.FM.turret.length < 3) { break; }
				if (this.FM.turret[2] == null) { break; }
				this.FM.turret[2].setHealth(f);
				if (this.FM.turret.length < 4) { break; }
				if (this.FM.turret[3] == null) { break; }
				this.FM.turret[3].setHealth(f);
				// ------------------------------------
				break;
		}
	}

	public void doRemoveBodyFromPlane(int i) {
		super.doRemoveBodyFromPlane(i);
		if (i >= 3) {
			this.doRemoveBodyChunkFromPlane("Pilot3a");
			this.doRemoveBodyChunkFromPlane("Head3a");
			this.doRemoveBodyChunkFromPlane("Pilot3b");
			this.doRemoveBodyChunkFromPlane("Head3b");
			this.doRemoveBodyChunkFromPlane("Pilot3c");
			this.doRemoveBodyChunkFromPlane("Head3c");
		}
	}

	protected void moveAirBrake(float f) {
		if (this instanceof TypeDiveBomber) {
			this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 90F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 90F * f, 0.0F);
		}
	}

	protected void moveBayDoor(float f) {
		if (this instanceof TypeBomber) {
			this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -85F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 85F * f, 0.0F);
		}
		this.hierMesh().chunkSetAngles("BayL1_D0", 0.0F, -65F * f, 0.0F);
		this.hierMesh().chunkSetAngles("BayL2_D0", 0.0F, 65F * f, 0.0F);
		this.hierMesh().chunkSetAngles("BayR1_D0", 0.0F, 65F * f, 0.0F);
		this.hierMesh().chunkSetAngles("BayR2_D0", 0.0F, -65F * f, 0.0F);
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 33: // '!'
				return super.cutFM(34, j, actor);

			case 36: // '$'
				return super.cutFM(37, j, actor);

			case 11: // '\013'
				this.hierMesh().chunkVisible("WireL_D0", false);
				break;

			case 12: // '\f'
				this.hierMesh().chunkVisible("WireR_D0", false);
				break;

			case 3: // '\003'
				this.FM.AS.setEngineState(this, 0, 0);
				break;

			case 4: // '\004'
				this.FM.AS.setEngineState(this, 1, 0);
				break;

			case 13: // '\r'
				return false;
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		if (this.FM.AS.bIsAboutToBailout) {
			this.hierMesh().chunkVisible("WireL_D0", false);
			this.hierMesh().chunkVisible("WireR_D0", false);
		}
		super.update(f);
	}

	public boolean typeDiveBomberToggleAutomation() {
		return false;
	}

	public void typeDiveBomberAdjAltitudeReset() {
	}

	public void typeDiveBomberAdjAltitudePlus() {
	}

	public void typeDiveBomberAdjAltitudeMinus() {
	}

	public void typeDiveBomberAdjVelocityReset() {
	}

	public void typeDiveBomberAdjVelocityPlus() {
	}

	public void typeDiveBomberAdjVelocityMinus() {
	}

	public void typeDiveBomberAdjDiveAngleReset() {
	}

	public void typeDiveBomberAdjDiveAnglePlus() {
	}

	public void typeDiveBomberAdjDiveAngleMinus() {
	}

	public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
	}

	public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
	}

	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurForwardAngle;
	public float fSightSetForwardAngle;
	public float fSightCurSideslip;

	static {
		Class class1 = PE_2.class;
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
	}
}
