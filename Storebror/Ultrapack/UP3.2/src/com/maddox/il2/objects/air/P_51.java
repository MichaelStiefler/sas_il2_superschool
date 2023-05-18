package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class P_51 extends Scheme1 implements TypeFighter, TypeBNZFighter, TypeStormovik {

	public P_51() {
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		float f1 = f <= 0.5F ? cvt(f, 0.0F, 0.2F, 0.0F, 95F) : cvt(f, 0.8F, 1.0F, 95F, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -105F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -85F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 85F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
		this.resetYPRmodifier();
		xyz[1] = cvt(f, 0.0F, 0.4F, 0.0F, -0.1F);
		this.hierMesh().chunkSetLocate("GearL9_D0", xyz, ypr);
		if (this instanceof P_51B || this instanceof P_51C) { this.hierMesh().chunkSetLocate("GearR9_D0", xyz, ypr); }
	}

	public void moveSteering(float f) {
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		xyz[1] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.2F, 0.0F, 0.2F);
		this.hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
		xyz[1] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.2F, 0.0F, 0.2F);
		this.hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
			case 0:
			default:
				return;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				break;
		}
	}

	protected void moveFlap(float f) {
		float f1 = -52F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				this.debuggunnery("Armor: Hit..");
				if (s.endsWith("p1")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(5F, 28.096F), shot);
				} else if (s.endsWith("p2")) {
					this.getEnergyPastArmor(13.35D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
				} else if (s.endsWith("p3")) {
					this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 40F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
					if (shot.power <= 0.0F) { this.doRicochetBack(shot); }
				}
				return;
			}
			if (s.startsWith("xxcontrols")) {
				this.debuggunnery("Controls: Hit..");
				int i = s.charAt(10) - 48;
				if (s.endsWith("10")) { i = 10; }
				switch (i) {
					default:
						break;

					case 1:
						if (this.getEnergyPastArmor(2.2F, shot) <= 0.0F) { break; }
						this.debuggunnery("Controls: Control Column: Hit, Controls Destroyed..");
						this.FM.AS.setControlsDamage(shot.initiator, 2);
						this.FM.AS.setControlsDamage(shot.initiator, 1);
						this.FM.AS.setControlsDamage(shot.initiator, 0);
						if (World.Rnd().nextFloat() < 0.25F) { this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1); }
						if (World.Rnd().nextFloat() < 0.25F) { this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6); }
						break;

					case 2:
						this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
						break;

					case 3:
					case 4:
						if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
							this.debuggunnery("Controls: Ailerones Controls: Out..");
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 5:
					case 6:
					case 8:
					case 9:
						if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F) {
							this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
							this.FM.AS.setControlsDamage(shot.initiator, 1);
						}
						break;

					case 7:
					case 10:
						if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F) {
							this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
							this.FM.AS.setControlsDamage(shot.initiator, 2);
						}
						break;
				}
				return;
			}
			if (s.startsWith("xxeng1")) {
				this.debuggunnery("Engine Module: Hit..");
				if (s.endsWith("prop") && this.getEnergyPastArmor(0.1F, shot) > 0.0F) { this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator); }
				if (s.endsWith("case") && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
					if (World.Rnd().nextFloat() < shot.power / 200000F) {
						this.FM.AS.setEngineStuck(shot.initiator, 0);
						this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
					}
					if (World.Rnd().nextFloat() < shot.power / 50000F) {
						this.FM.AS.hitEngine(shot.initiator, 0, 2);
						this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
					}
					if (World.Rnd().nextFloat() < shot.power / 28000F) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
						this.debuggunnery("Engine Module: Engine Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
					}
					this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
					this.debuggunnery("Engine Module: Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
				}
				if (s.endsWith("cyls") && this.getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.75F) {
					this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
					this.debuggunnery("Engine Module: Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
					if (this.FM.AS.astateEngineStates[0] < 1) { this.FM.AS.hitEngine(shot.initiator, 0, 1); }
					if (World.Rnd().nextFloat() < shot.power / 24000F) {
						this.FM.AS.hitEngine(shot.initiator, 0, 3);
						this.debuggunnery("Engine Module: Engine Cylinders Hit - Engine Fires..");
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("supc") && this.getEnergyPastArmor(0.05F, shot) > 0.0F) { this.FM.EI.engines[0].setKillCompressor(shot.initiator); }
				if (s.endsWith("eqpt")) {
					if (World.Rnd().nextFloat() < 0.3F && this.getEnergyPastArmor(0.05F, shot) > 0.0F) { this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator); }
					if (World.Rnd().nextFloat() < 0.3F && this.getEnergyPastArmor(0.05F, shot) > 0.0F) { this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0); }
					if (World.Rnd().nextFloat() < 0.3F && this.getEnergyPastArmor(0.05F, shot) > 0.0F) { this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1); }
					if (World.Rnd().nextFloat() < 0.3F && this.getEnergyPastArmor(0.05F, shot) > 0.0F) { this.FM.EI.engines[0].setEngineStuck(shot.initiator); }
					if (World.Rnd().nextFloat() < 0.3F && this.getEnergyPastArmor(0.05F, shot) > 0.0F) { this.FM.EI.engines[0].setEngineStops(shot.initiator); }
				}
				if (s.endsWith("oil1")) { this.FM.AS.hitOil(shot.initiator, 0); }
				if (s.endsWith("wat1") && World.Rnd().nextFloat() < 0.05F) { this.FM.AS.hitOil(shot.initiator, 0); }
				if (s.startsWith("xxeng1mag")) {
					int j = s.charAt(9) - 49;
					this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
					this.debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
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
				int k = s.charAt(7) - 49;
				if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
					this.debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
					this.FM.AS.setJamBullets(0, k);
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
				}
				return;
			}
			if (s.startsWith("xxoil")) {
				if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.FM.AS.hitOil(shot.initiator, 0);
					this.getEnergyPastArmor(0.22F, shot);
					this.debuggunnery("Engine Module: Oil Tank Pierced..");
				}
				return;
			}
			if (s.startsWith("xxtank")) {
				int l = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					if (this.FM.AS.astateTankStates[l] == 0) {
						this.debuggunnery("Fuel Tank (" + l + "): Pierced..");
						this.FM.AS.hitTank(shot.initiator, l, 1);
						this.FM.AS.doSetTankState(shot.initiator, l, 1);
					}
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F) {
						this.FM.AS.hitTank(shot.initiator, l, 2);
						this.debuggunnery("Fuel Tank (" + l + "): Hit..");
					}
				}
			}
			return;
		}
		if (s.startsWith("xblister")) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1); }
		if (s.startsWith("xcf")) {
			this.hitChunk("CF", shot);
		} else if (s.startsWith("xeng")) {
			if (this.chunkDamageVisible("Engine1") < 2) { this.hitChunk("Engine1", shot); }
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		} else if (s.startsWith("xkeel")) {
			if (this.chunkDamageVisible("Keel1") < 2) { this.hitChunk("Keel1", shot); }
		} else if (s.startsWith("xrudder")) {
			if (this.chunkDamageVisible("Rudder1") < 1) { this.hitChunk("Rudder1", shot); }
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 2) { this.hitChunk("StabL", shot); }
			if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 1) { this.hitChunk("StabR", shot); }
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
			int i1;
			if (s.endsWith("a")) {
				byte0 = 1;
				i1 = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				i1 = s.charAt(6) - 49;
			} else {
				i1 = s.charAt(5) - 49;
			}
			this.hitFlesh(i1, shot, byte0);
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 35:
				this.FM.AS.setJamBullets(0, 0);
				this.FM.AS.setJamBullets(1, 0);
				this.FM.AS.setJamBullets(1, 2);
				break;

			case 38:
				this.FM.AS.setJamBullets(0, 1);
				this.FM.AS.setJamBullets(1, 1);
				this.FM.AS.setJamBullets(1, 3);
				break;
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		float f1 = this.FM.EI.engines[0].getControlRadiator();
		this.hierMesh().chunkSetAngles("Oil_D0", 0.0F, 15F * f1, 0.0F);
		this.hierMesh().chunkSetAngles("Water_D0", 0.0F, -12F + 15F * f1, 0.0F);
		super.update(f);
	}

	static {
		Class class1 = P_51.class;
		Property.set(class1, "originCountry", PaintScheme.countryUSA);
	}
}
