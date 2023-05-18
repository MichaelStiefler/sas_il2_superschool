package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class ME_163B1A extends Scheme1 implements TypeFighter, TypeBNZFighter {

	public ME_163B1A() {
		this.bCockpitNVentilated = false;
		this.bCartAttached = true;
		this.flame = null;
		this.dust = null;
		this.trail = null;
		this.sprite = null;
		this.turboexhaust = null;
		this.dynamoOrient = 0.0F;
		this.bDynamoRotary = false;
		this.pk = 0;
	}

	public void destroy() {
		if (Actor.isValid(this.flame)) { this.flame.destroy(); }
		if (Actor.isValid(this.dust)) { this.dust.destroy(); }
		if (Actor.isValid(this.trail)) { this.trail.destroy(); }
		if (Actor.isValid(this.sprite)) { this.sprite.destroy(); }
		if (Actor.isValid(this.turboexhaust)) { this.turboexhaust.destroy(); }
		super.destroy();
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (Config.isUSE_RENDER()) {
			this.flame = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109F.eff", -1F);
			this.dust = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109D.eff", -1F);
			this.trail = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", -1F);
			this.sprite = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", -1F);
			this.turboexhaust = Eff3DActor.New(this, this.findHook("_Engine1ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallGND.eff", -1F);
			Eff3DActor.setIntesity(this.flame, 0.0F);
			Eff3DActor.setIntesity(this.dust, 0.0F);
			Eff3DActor.setIntesity(this.trail, 0.0F);
			Eff3DActor.setIntesity(this.sprite, 0.0F);
			Eff3DActor.setIntesity(this.turboexhaust, 1.0F);
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("1")) {
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
					if (this.getEnergyPastArmor(World.Rnd().nextFloat(30F, 90F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) < 0.0F) { this.doRicochet(shot); }
				} else if (s.endsWith("2")) { this.getEnergyPastArmor(13.13D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot); }
				return;
			}
			if (s.startsWith("xxcontrols")) {
				int i = s.charAt(10) - 48;
				switch (i) {
					default:
						break;

					case 1:
						if (this.getEnergyPastArmor(2.2F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) { this.FM.AS.setControlsDamage(shot.initiator, 2); }
						break;

					case 2:
						if (this.getEnergyPastArmor(2.2F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) <= 0.0F) { break; }
						if (World.Rnd().nextFloat() < 0.25F) { this.FM.AS.setControlsDamage(shot.initiator, 2); }
						if (World.Rnd().nextFloat() < 0.25F) {
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;

					case 3:
					case 4:
						if (this.getEnergyPastArmor(2.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.1F) {
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						break;
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(v1.y)), shot) > 0.0F) {
					this.debuggunnery("*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if (s.startsWith("xxspark") && this.chunkDamageVisible("Keel1") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** Keel Spars Damaged..");
					this.nextDMGLevels(1, 2, "Keel1_D3", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxlock")) {
				if (s.startsWith("xxlockal") && this.getEnergyPastArmor(4.35F, shot) > 0.0F) {
					this.debuggunnery("*** AroneL Lock Damaged..");
					this.nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
				}
				if (s.startsWith("xxlockar") && this.getEnergyPastArmor(4.35F, shot) > 0.0F) {
					this.debuggunnery("*** AroneR Lock Damaged..");
					this.nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
				}
				if (s.startsWith("xxlockfl") && this.getEnergyPastArmor(4.35F, shot) > 0.0F) {
					this.debuggunnery("*** VatorL Lock Damaged..");
					this.nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
				}
				if (s.startsWith("xxlockfr") && this.getEnergyPastArmor(4.35F, shot) > 0.0F) {
					this.debuggunnery("*** VatorR Lock Damaged..");
					this.nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
				}
				if (s.startsWith("xxlockr") && this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
					this.debuggunnery("*** Rudder1 Lock Damaged..");
					this.nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxeng")) {
				int j = s.charAt(8) - 48;
				switch (j) {
					default:
						break;

					case 1:
						if (World.Rnd().nextFloat() < 0.01F) { this.FM.AS.hitEngine(shot.initiator, 0, 100); }
						if (Pd.x < -2.7D) { this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.01F, 0.35F)); }
						break;

					case 2:
						if (this.getEnergyPastArmor(4.96F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) { this.FM.AS.hitEngine(shot.initiator, 0, 100); }
						break;

					case 3:
						this.getEnergyPastArmor(5.808F, shot);
						break;
				}
				return;
			}
			if (s.startsWith("xxtank")) {
				int k = s.charAt(6) - 48;
				switch (k) {
					default:
						break;

					case 1:
						if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 7.9F), shot) > 0.0F) { this.FM.AS.hitTank(shot.initiator, 3, 1); }
						break;

					case 2:
					case 3:
						if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 7.9F), shot) > 0.0F) {
							this.FM.AS.hitTank(shot.initiator, 2, 1);
							this.bCockpitNVentilated = true;
						}
						// fall through

					case 4:
						if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 7.9F), shot) > 0.0F) { this.FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, 4)); }
						break;

					case 5:
						if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 7.9F), shot) > 0.0F) { this.FM.AS.hitTank(shot.initiator, 1, World.Rnd().nextInt(1, 4)); }
						break;
				}
				return;
			}
			if (s.startsWith("xxammo")) {
				int l = s.charAt(6) - 48;
				if (World.Rnd().nextFloat() < 0.1F) {
					if (l == 0) {
						this.FM.AS.setJamBullets(1, 1);
					} else {
						this.FM.AS.setJamBullets(1, 0);
					}
				}
				return;
			}
			if (s.startsWith("xxgunl") && this.getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 35.6F), shot) > 0.0F) { this.FM.AS.setJamBullets(1, 0); }
			if (s.startsWith("xxgunr") && this.getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 35.6F), shot) > 0.0F) { this.FM.AS.setJamBullets(1, 1); }
			if (!s.startsWith("xxeqpt")) { ; }
			return;
		}
		if (s.startsWith("xcf")) {
			if (Pd.x > 2.01D && this.getEnergyPastArmor(11.11F / ((float) Math.sqrt(v1.y * v1.y + v1.z * v1.z) + 0.0001F), shot) <= 0.0F) {
				this.doRicochet(shot);
				return;
			}
			if (Pd.x > 0.8D && Pd.x < 2D) {
				if (Pd.z > 0.425D) {
					if (World.Rnd().nextFloat() < 0.5F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1); }
				} else if (Pd.y > 0.0D) {
					if (World.Rnd().nextFloat() < 0.5F) {
						this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
					} else {
						this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
					}
				} else if (World.Rnd().nextFloat() < 0.5F) {
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
				} else {
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
				}
			}
			if (this.chunkDamageVisible("CF") < 3) { this.hitChunk("CF", shot); }
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		} else if (s.startsWith("xkeel")) {
			if (this.chunkDamageVisible("Keel1") < 2) { this.hitChunk("Keel1", shot); }
		} else if (s.startsWith("xrudder")) {
			if (this.chunkDamageVisible("Rudder1") < 1) { this.hitChunk("Rudder1", shot); }
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
			if (this.chunkDamageVisible("AroneL") < 2) { this.hitChunk("AroneL", shot); }
		} else if (s.startsWith("xaroner")) {
			if (this.chunkDamageVisible("AroneR") < 2) { this.hitChunk("AroneR", shot); }
		} else if (s.startsWith("xflapl")) {
			if (this.chunkDamageVisible("VatorL") < 1) { this.hitChunk("VatorL", shot); }
		} else if (s.startsWith("xflapr")) {
			if (this.chunkDamageVisible("VatorR") < 1) { this.hitChunk("VatorR", shot); }
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int i1;
			if (s.endsWith("a") || s.endsWith("a2")) {
				byte0 = 1;
				i1 = s.charAt(6) - 49;
			} else if (s.endsWith("b") || s.endsWith("b2")) {
				byte0 = 2;
				i1 = s.charAt(6) - 49;
			} else {
				i1 = s.charAt(5) - 49;
			}
			this.hitFlesh(i1, shot, byte0);
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag && this.bCockpitNVentilated) { this.FM.AS.hitPilot(this, 0, 1); }
		if (Config.isUSE_RENDER()) {
			if (this.oldVwld < 20F && this.FM.getSpeed() > 20F) {
				Eff3DActor.finish(this.turboexhaust);
				this.turboexhaust = Eff3DActor.New(this, this.findHook("_Engine1ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallTSPD.eff", -1F);
			}
			if (this.oldVwld > 20F && this.FM.getSpeed() < 20F) {
				Eff3DActor.finish(this.turboexhaust);
				this.turboexhaust = Eff3DActor.New(this, this.findHook("_Engine1ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallGND.eff", -1F);
			}
			this.oldVwld = this.FM.getSpeed();
		}
	}

	public void doMurderPilot(int i) {
		if (i != 0) {
			return;
		} else {
			this.hierMesh().chunkVisible("Pilot1_D0", false);
			this.hierMesh().chunkVisible("Head1_D0", false);
			this.hierMesh().chunkVisible("Pilot1_D1", true);
			this.hierMesh().chunkVisible("HMask1_D0", false);
			return;
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -15F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
	}

	protected void moveGear(float f) {
		HierMesh hiermesh = this.hierMesh();
		if (this.bCartAttached) {
			if (f < 1.0F) {
				this.hierMesh().chunkVisible("GearL1_D0", false);
				this.hierMesh().chunkVisible("GearR1_D0", false);
				if (this.hierMesh().isChunkVisible("Cart_D0")) {
					this.hierMesh().chunkVisible("CartDrop_D0", true);
					this.cut("CartDrop");
				}
				this.hierMesh().chunkVisible("Cart_D0", false);
				this.bCartAttached = false;
				this.FM.setCapableOfTaxiing(false);
				this.FM.CT.bHasBrakeControl = false;
			}
		} else {
			this.resetYPRmodifier();
			xyz[1] = -0.3F + 0.1125F * f;
			ypr[1] = 88F;
			hiermesh.chunkSetLocate("Cart_D0", xyz, ypr);
		}
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -15F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
	}

	public void moveWheelSink() {
		if (!this.bCartAttached && this.FM.CT.getGear() > 0.99F) {
			float f = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.066F, -45F, 0.0F);
			this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, f, 0.0F);
			this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, f, 0.0F);
			this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, f, 0.0F);
			this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, f, 0.0F);
			this.hierMesh().chunkSetAngles("GearL6_D0", 0.0F, f, 0.0F);
		}
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
	}

	protected void moveElevator(float f) {
		this.reflectControls();
	}

	protected void moveAileron(float f) {
		this.reflectControls();
	}

	private void reflectControls() {
		HierMesh hiermesh = this.hierMesh();
		float f = -20F * this.FM.CT.getAileron();
		float f1 = 20F * this.FM.CT.getElevator();
		hiermesh.chunkSetAngles("AroneL_D0", 0.0F, f + f1, 0.0F);
		hiermesh.chunkSetAngles("AroneR_D0", 0.0F, f - f1, 0.0F);
		hiermesh.chunkSetAngles("VatorL_D0", 0.0F, 0.5F * f1, 0.0F);
		hiermesh.chunkSetAngles("VatorR_D0", 0.0F, 0.5F * f1, 0.0F);
	}

	protected void moveFlap(float f) {
		float f1 = -50F * f;
		this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, f1, 0.0F);
	}

	protected void moveFan(float f) {
		this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
		if (this.pk >= 1) { this.pk = 1; }
		if (this.bDynamoRotary != (this.pk == 1)) {
			this.bDynamoRotary = this.pk == 1;
			this.hierMesh().chunkVisible("Prop1_D0", !this.bDynamoRotary);
			this.hierMesh().chunkVisible("PropRot1_D0", this.bDynamoRotary);
		}
		this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - this.FM.Vwld.length() * 1.5444015264511108D) % 360F;
		this.hierMesh().chunkSetAngles("Prop1_D0", 0.0F, this.dynamoOrient, 0.0F);
	}

	public void update(float f) {
		super.update(f);
		if (this.bCartAttached) {
			this.moveGear(this.FM.CT.getGear());
			if (this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) {
				this.FM.CT.bHasBrakeControl = false;
			} else {
				this.FM.CT.bHasBrakeControl = true;
			}
		}
		if (this.FM.AS.isMaster()) {
			if (Config.isUSE_RENDER()) {
				if (this.FM.EI.engines[0].getw() > 0.0F && this.FM.EI.engines[0].getStage() == 6) {
					this.FM.AS.setSootState(this, 0, 1);
				} else {
					this.FM.AS.setSootState(this, 0, 0);
				}
			}
			if (this.oldThtl < 0.35F) {
				this.FM.EI.setThrottle(0.0F);
			} else if (this.oldThtl < 0.65F) {
				this.FM.EI.setThrottle(0.35F);
			} else if (this.oldThtl < 1.0F) {
				this.FM.EI.setThrottle(0.65F);
			} else {
				this.FM.EI.setThrottle(1.0F);
			}
			if (this.oldThtl != this.FM.CT.PowerControl) {
				this.oldThtl = this.FM.CT.PowerControl;
				if (this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) { HUD.log(AircraftHotKeys.hudLogPowerId, "Power", new Object[] { new Integer(Math.round(this.oldThtl * 100F)) }); }
			}
			if (this.oldThtl == 0.0F) {
				if (!this.FM.Gears.onGround()) {
					if (this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode() && this.FM.EI.engines[0].getStage() == 6) { HUD.log("EngineI0"); }
					this.FM.EI.engines[0].setEngineStops(this);
				}
			} else {
				if (this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode() && this.FM.EI.engines[0].getStage() == 0 && this.FM.M.fuel > 0.0F) { HUD.log("EngineI1"); }
				this.FM.EI.engines[0].setStage(this, 6);
			}
		}
	}

	public void doSetSootState(int i, int j) {
		switch (j) {
			case 0:
				Eff3DActor.setIntesity(this.flame, 0.0F);
				Eff3DActor.setIntesity(this.dust, 0.0F);
				Eff3DActor.setIntesity(this.trail, 0.0F);
				Eff3DActor.setIntesity(this.sprite, 0.0F);
				break;

			case 1:
				Eff3DActor.setIntesity(this.flame, 1.0F);
				Eff3DActor.setIntesity(this.dust, 1.0F);
				Eff3DActor.setIntesity(this.trail, 1.0F);
				Eff3DActor.setIntesity(this.sprite, 1.0F);
				break;
		}
	}

	private boolean    bCockpitNVentilated;
	public boolean     bCartAttached;
	private Eff3DActor flame;
	private Eff3DActor dust;
	private Eff3DActor trail;
	private Eff3DActor sprite;
	private Eff3DActor turboexhaust;
	private float      oldThtl;
	private float      oldVwld;
	private float      dynamoOrient;
	private boolean    bDynamoRotary;
	private int        pk;

	static {
		Class class1 = ME_163B1A.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Me-163");
		Property.set(class1, "meshName", "3DO/Plane/Me-163B-1a/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1946F);
		Property.set(class1, "FlightModel", "FlightModels/Me-163B-1a.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitME_163.class });
		Property.set(class1, "LOSElevation", 0.87325F);
		weaponTriggersRegister(class1, new int[] { 1, 1 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
	}
}
