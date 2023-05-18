package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public class LAGG_3RD extends LAGG_3 {

	public LAGG_3RD() {
		this.oldctl = -1F;
		this.curctl = -1F;
	}

	public void update(float f) {
		if (this.FM.AS.isMaster()) {
			if (this.curctl == -1F) {
				this.curctl = this.oldctl = this.FM.EI.engines[0].getControlThrottle();
			} else {
				this.curctl = this.FM.EI.engines[0].getControlThrottle();
				if ((this.curctl - this.oldctl) / f > 3F && this.FM.EI.engines[0].getRPM() < 2100F && this.FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.25F) { this.FM.AS.hitEngine(this, 0, 100); }
				if ((this.curctl - this.oldctl) / f < -3F && this.FM.EI.engines[0].getRPM() < 2100F && this.FM.EI.engines[0].getStage() == 6) {
					if (World.Rnd().nextFloat() < 0.25F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) { this.FM.EI.engines[0].setEngineStops(this); }
					if (World.Rnd().nextFloat() < 0.75F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) { this.FM.EI.engines[0].setKillCompressor(this); }
				}
				this.oldctl = this.curctl;
			}
		}
		if (Config.isUSE_RENDER() && this.FM.AS.isMaster()) {
			if (this.FM.EI.engines[0].getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6) {
				if (this.FM.EI.engines[0].getPowerOutput() > 0.95F) {
					this.FM.AS.setSootState(this, 0, 3);
				} else {
					this.FM.AS.setSootState(this, 0, 2);
				}
			} else {
				this.FM.AS.setSootState(this, 0, 0);
			}
		}
		if (this.FM.getSpeed() > 5F) {
			this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.2F), 0.0F);
			this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.2F), 0.0F);
		}
		super.update(f);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 80F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -80F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 100F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 100F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC99_D0", 0.0F, -75F * f, 0.0F);
		float f1 = Math.max(-f * 1200F, -80F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -f1, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, -f1, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, f, 0.0F);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxammo")) {
				if (this.getEnergyPastArmor(4.81F, shot) > 0.0F) {
					if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.setJamBullets(1, 0); }
					if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.setJamBullets(1, 1); }
					if (World.Rnd().nextFloat() < 0.012F) { this.FM.AS.explodeTank(shot.initiator, 0); }
					this.getEnergyPastArmor(8.98F, shot);
				}
				return;
			}
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1")) {
					this.getEnergyPastArmor(12.7F / (1E-005F + (float) Math.abs(v1.x)), shot);
				} else if (s.endsWith("g1")) {
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
					this.getEnergyPastArmor(18.15F / (1E-005F + (float) Math.abs(v1.x)), shot);
				}
				return;
			}
			if (s.startsWith("xxcontrols")) {
				if (Pd.x > -0.35499998927116394D) {
					if (World.Rnd().nextFloat() < 0.1F || this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
						if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1); }
					}
				} else if (Pd.x > -1.2849999666213989D) {
					if (World.Rnd().nextFloat() < 0.2F) { this.FM.AS.setControlsDamage(shot.initiator, 0); }
					if (World.Rnd().nextFloat() < 0.2F) { this.FM.AS.setControlsDamage(shot.initiator, 1); }
					if (World.Rnd().nextFloat() < 0.2F) { this.FM.AS.setControlsDamage(shot.initiator, 2); }
				} else if (this.getEnergyPastArmor(0.31F, shot) > 0.0F) { this.FM.AS.setControlsDamage(shot.initiator, 1); }
				return;
			}
			if (s.startsWith("xxspar")) {
				if ((s.endsWith("li1") || s.endsWith("li2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if ((s.endsWith("ri1") || s.endsWith("ri2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if ((s.endsWith("lm1") || s.endsWith("lm2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if ((s.endsWith("rm1") || s.endsWith("rm2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if ((s.endsWith("lo1") || s.endsWith("lo2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if ((s.endsWith("ro1") || s.endsWith("ro2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if ((s.endsWith("sl1") || s.endsWith("sl2")) && this.chunkDamageVisible("StabL") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** StabL Spars Damaged..");
					this.nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
				}
				if ((s.endsWith("sr1") || s.endsWith("sr2")) && this.chunkDamageVisible("StabR") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					debugprintln(this, "*** StabR Spars Damaged..");
					this.nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxlock")) {
				if (s.endsWith("al")) {
					if (this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
						debugprintln(this, "*** AroneL Lock Damaged..");
						this.nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
					}
				} else if (s.endsWith("ar")) {
					if (this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
						debugprintln(this, "*** AroneR Lock Damaged..");
						this.nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
					}
				} else if (s.endsWith("vl1") || s.endsWith("vl2")) {
					if (this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
						debugprintln(this, "*** VatorL Lock Damaged..");
						this.nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
					}
				} else if (s.endsWith("vr1") || s.endsWith("vr2")) {
					if (this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
						debugprintln(this, "*** VatorR Lock Damaged..");
						this.nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
					}
				} else if ((s.endsWith("r1") || s.endsWith("r2")) && this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
					debugprintln(this, "*** Rudder1 Lock Damaged..");
					this.nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxeng1")) {
				if (point3d.x > 1.054D && point3d.x < 1.417D) { this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6)); }
				if (World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass) { this.FM.AS.hitEngine(shot.initiator, 0, 2); }
				this.getEnergyPastArmor(14.296F, shot);
				return;
			}
			if (s.startsWith("xxoil")) {
				this.FM.AS.hitOil(shot.initiator, 0);
				this.getEnergyPastArmor(0.1F, shot);
				return;
			}
			if (s.startsWith("xxtank")) {
				int i = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F && shot.powerType == 3) { this.FM.AS.hitTank(shot.initiator, i, 2); }
				return;
			}
			if (s.startsWith("xxshvak")) {
				int j = s.charAt(7) - 49;
				this.FM.AS.setJamBullets(1, j);
				this.getEnergyPastArmor(12F, shot);
				return;
			}
			if (s.startsWith("xxpneu")) {
				this.FM.Gears.setHydroOperable(false);
				return;
			} else {
				return;
			}
		}
		if ((s.startsWith("xcf") || s.startsWith("xcockpit") && Pd.z > 0.73299998044967651D) && this.chunkDamageVisible("CF") < 3) { this.hitChunk("CF", shot); }
		if (s.startsWith("xxcockpit")) {
			if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40); }
			if (Pd.y < 0.0D) {
				if (World.Rnd().nextFloat() < 0.25F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4); }
				if (World.Rnd().nextFloat() < 0.25F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8); }
			} else {
				if (World.Rnd().nextFloat() < 0.25F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10); }
				if (World.Rnd().nextFloat() < 0.25F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20); }
			}
			if (Pd.z > 0.63899999856948853D && Pd.x < -1.0410000085830688D) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1); }
		} else if (s.startsWith("xeng")) {
			this.hitChunk("Engine1", shot);
		} else if (s.startsWith("xtail")) {
			this.hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) {
			this.hitChunk("Keel1", shot);
		} else if (s.startsWith("xrudder")) {
			if (this.chunkDamageVisible("Rudder1") < 1) { this.hitChunk("Rudder1", shot); }
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 2) { this.hitChunk("StabL", shot); }
			if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 2) { this.hitChunk("StabR", shot); }
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
			if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneL") < 1) { this.hitChunk("AroneR", shot); }
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

	private float oldctl;
	private float curctl;

	static {
		Class class1 = LAGG_3RD.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "LaGG");
		Property.set(class1, "meshName", "3DO/Plane/LaGG-3RD(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/LaGG-3RD.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitLAGG_3RD.class });
		Property.set(class1, "LOSElevation", 0.90695F);
		weaponTriggersRegister(class1, new int[] { 1, 1 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
	}
}
