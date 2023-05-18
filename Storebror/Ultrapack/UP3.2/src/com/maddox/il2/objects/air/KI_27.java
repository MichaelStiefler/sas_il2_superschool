package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.rts.Property;

public abstract class KI_27 extends Scheme1 implements TypeFighter, TypeTNBFighter {

	public KI_27() {
		this.flapps = 0.0F;
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		Object aobj[] = this.pos.getBaseAttached();
		if (aobj == null) { return; }
		for (int i = 0; i < aobj.length; i++) {
			if (aobj[i] instanceof BombGun) {
				for (int j = 1; j < 5; j++) {
					this.hierMesh().chunkVisible("Pilon0" + j + "_D0", true);
				}

				return;
			}
		}

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

	public void update(float f) {
		super.update(f);
		float f1 = this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(this.flapps - f1) > 0.01F) {
			this.flapps = f1;
			for (int i = 1; i < 9; i++) {
				this.hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, -30F * f1, 0.0F);
			}

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

	public void moveCockpitDoor(float f) {
		this.resetYPRmodifier();
		xyz[1] = cvt(f, 0.05F, 0.95F, 0.0F, -0.55F);
		this.hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) { Main3D.cur3D().cockpits[0].onDoorMoved(f); }
			this.setDoorSnd(f);
		}
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		float f = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.15F, 0.0F, 0.15F);
		xyz[1] = f;
		this.hierMesh().chunkSetLocate("GearL2_D0", xyz, ypr);
		f = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.15F, 0.0F, 0.15F);
		xyz[1] = f;
		this.hierMesh().chunkSetLocate("GearR2_D0", xyz, ypr);
	}

	protected void moveFlap(float f) {
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxcontrols")) {
				this.debuggunnery("Controls: Hit..");
				int i = s.charAt(10) - 48;
				switch (i) {
					default:
						break;

					case 1:
					case 2:
					case 3:
					case 4:
						if (this.getEnergyPastArmor(4.2F, shot) > 0.0F) {
							this.debuggunnery("Controls: Ailerones Controls: Out..");
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 5:
						if (this.getEnergyPastArmor(4.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F) {
							this.debuggunnery("Controls: Elevator Controls: Disabled..");
							this.FM.AS.setControlsDamage(shot.initiator, 1);
						}
						break;

					case 6:
						if (this.getEnergyPastArmor(0.22F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F) {
							this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
							this.FM.AS.setControlsDamage(shot.initiator, 2);
						}
						break;

					case 7:
						if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
							debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
							this.FM.AS.setControlsDamage(shot.initiator, 2);
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;
				}
				return;
			}
			if (s.startsWith("xxeng")) {
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 140000F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 85000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
						}
					} else if (World.Rnd().nextFloat() < 0.01F) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
					} else {
						this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.002F);
						debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
					}
					this.getEnergyPastArmor(12F, shot);
				}
				if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.75F) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
						debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 48000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
						}
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("gear")) { this.getEnergyPastArmor(44.2F, shot); }
				if (s.startsWith("xxeng1oil")) {
					if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) { this.debuggunnery("Engine Module: Oil Radiator Hit.."); }
					this.FM.AS.hitOil(shot.initiator, 0);
				}
				return;
			}
			if (s.startsWith("xxlock")) {
				this.debuggunnery("Lock Construction: Hit..");
				if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
				}
				if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
				}
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
				return;
			}
			if (s.startsWith("xxmgun01")) {
				if (this.getEnergyPastArmor(0.75F, shot) > 0.0F) {
					this.debuggunnery("Armament: MGUN (0) Disabled..");
					this.FM.AS.setJamBullets(0, 0);
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
				}
				return;
			}
			if (s.startsWith("xxmgun02")) {
				if (this.getEnergyPastArmor(0.75F, shot) > 0.0F) {
					this.debuggunnery("Armament: MGUN (1) Disabled..");
					this.FM.AS.setJamBullets(0, 1);
					this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
				}
				return;
			}
			if (s.startsWith("xxoil")) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
				if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					this.FM.AS.hitOil(shot.initiator, 0);
					this.getEnergyPastArmor(0.22F, shot);
					this.debuggunnery("Engine Module: Oil Tank Pierced..");
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				debugprintln(this, "*** Spar Construction: Hit..");
				if (s.endsWith("li1") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 4.4F), shot) > 0.0F) {
					debugprintln(this, "*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.endsWith("ri1") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 4.4F), shot) > 0.0F) {
					debugprintln(this, "*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.endsWith("lm1") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 3.3F), shot) > 0.0F) {
					debugprintln(this, "*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.endsWith("rm1") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 3.3F), shot) > 0.0F) {
					debugprintln(this, "*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.endsWith("lo1") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 2.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.endsWith("ro1") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 2.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.86F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.15F) {
					this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
					this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxtank")) {
				int j = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.4F) {
					if (this.FM.AS.astateTankStates[j] == 0) {
						this.debuggunnery("Fuel Tank (" + j + "): Pierced..");
						this.FM.AS.hitTank(shot.initiator, j, 1);
						this.FM.AS.doSetTankState(shot.initiator, j, 1);
					}
					if (World.Rnd().nextFloat() < 0.003F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.4F) {
						this.FM.AS.hitTank(shot.initiator, j, 2);
						this.debuggunnery("Fuel Tank (" + j + "): Hit..");
					}
				}
				return;
			} else {
				return;
			}
		}
		if (s.startsWith("xcf") || s.startsWith("xcock")) {
			this.hitChunk("CF", shot);
			if (point3d.z > 0.568D) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
				if (point3d.x > -0.093D) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2); }
			} else {
				if (point3d.y > 0.0D) {
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
				} else {
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
				}
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8); }
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20); }
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40); }
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
			if (World.Rnd().nextFloat() < 0.05F) {
				this.debuggunnery("Hydro System: Disabled..");
				this.FM.AS.setInternalDamage(shot.initiator, 0);
			}
			if (World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F) {
				this.debuggunnery("Undercarriage: Stuck..");
				this.FM.AS.setInternalDamage(shot.initiator, 3);
			}
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int k;
			if (s.endsWith("a")) {
				byte0 = 1;
				k = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				k = s.charAt(6) - 49;
			} else {
				k = s.charAt(5) - 49;
			}
			this.hitFlesh(k, shot, byte0);
		}
	}

	protected boolean haveFlaps() {
		return false;
	}

	public static boolean bChangedPit = false;
	private float         flapps;

	static {
		Class class1 = KI_27.class;
		Property.set(class1, "originCountry", PaintScheme.countryJapan);
	}
}
