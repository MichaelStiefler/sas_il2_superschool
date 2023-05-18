package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class Halifax extends Scheme4 implements TypeBomber, TypeTransport {

	public Halifax() {
		this.bSightAutomation = false;
		this.bSightBombDump = false;
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurAltitude = 3000F;
		this.fSightCurSpeed = 200F;
		this.fSightCurReadyness = 0.0F;
		this.fSightSetForwardAngle = 0.0F;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.AS.wantBeaconsNet(true);
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30F * f, 0.0F);
	}

	protected void moveFlap(float f) {
		float f1 = -35.5F * f;
		for (int i = 1; i < 5; i++)
			this.hierMesh().chunkSetAngles("Flap0" + i + "_D0", 0.0F, f1, 0.0F);

	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		byte byte0 = 0;
		if (s.startsWith("xx")) {
			if (s.startsWith("xxammo")) {
				int i = s.charAt(6) - 48;
				if (s.length() > 7) i = 10;
				if (this.getEnergyPastArmor(6.87F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F) {
					switch (i) {
						case 1:
							i = 10;
							byte0 = 0;
							break;

						case 2:
							i = 10;
							byte0 = 1;
							break;

						case 3:
							i = 11;
							byte0 = 0;
							break;

						case 4:
							i = 11;
							byte0 = 1;
							break;

						case 5:
							i = 11;
							byte0 = 2;
							break;

						case 6:
							i = 11;
							byte0 = 3;
							break;

						case 7:
							i = 12;
							byte0 = 0;
							break;

						case 8:
							i = 12;
							byte0 = 1;
							break;

						case 9:
							i = 12;
							byte0 = 2;
							break;

						case 10:
							i = 12;
							byte0 = 3;
							break;
					}
					this.FM.AS.setJamBullets(i, byte0);
					return;
				}
			}
			if (s.startsWith("xxcontrols")) {
				int j = s.charAt(10) - 48;
				switch (j) {
					case 1:
					case 2:
						if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
							this.FM.AS.setControlsDamage(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Aileron Controls Out..");
						}
						break;

					case 3:
						if (World.Rnd().nextFloat() < 0.125F && this.getEnergyPastArmor(5.2F, shot) > 0.0F) {
							Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
							this.FM.AS.setControlsDamage(shot.initiator, 2);
							this.FM.AS.setControlsDamage(shot.initiator, 1);
							this.FM.AS.setControlsDamage(shot.initiator, 0);
						}
						this.getEnergyPastArmor(2.0F, shot);
						break;

					case 4:
					case 5:
					case 6:
						if (World.Rnd().nextFloat() < 0.252F && this.getEnergyPastArmor(5.2F, shot) > 0.0F) {
							if (World.Rnd().nextFloat() < 0.125F) this.FM.AS.setControlsDamage(shot.initiator, 2);
							if (World.Rnd().nextFloat() < 0.125F) this.FM.AS.setControlsDamage(shot.initiator, 1);
						}
						this.getEnergyPastArmor(2.0F, shot);
						break;
				}
			} else if (s.startsWith("xxeng")) {
				int k = s.charAt(5) - 49;
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 140000F) {
							this.FM.AS.setEngineStuck(shot.initiator, k);
							Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 85000F) {
							this.FM.AS.hitEngine(shot.initiator, k, 2);
							Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Engine Damaged..");
						}
					} else if (World.Rnd().nextFloat() < 0.005F) this.FM.EI.engines[k].setCyliderKnockOut(shot.initiator, 1);
					else {
						this.FM.EI.engines[k].setReadyness(shot.initiator, this.FM.EI.engines[k].getReadyness() - 0.00082F);
						Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[k].getReadyness() + "..");
					}
					this.getEnergyPastArmor(12F, shot);
				}
				if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[k].getCylindersRatio() * 0.75F) {
						this.FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
						Aircraft.debugprintln(this, "*** Engine (" + k + ") Cylinders Hit, " + this.FM.EI.engines[k].getCylindersOperable() + "/" + this.FM.EI.engines[k].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 18000F) {
							this.FM.AS.hitEngine(shot.initiator, k, 2);
							Aircraft.debugprintln(this, "*** Engine (" + k + ") Cylinders Hit - Engine Fires..");
						}
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("mag1")) {
					this.FM.EI.engines[k].setMagnetoKnockOut(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Magneto #0 Destroyed..");
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("mag2")) {
					this.FM.EI.engines[k].setMagnetoKnockOut(shot.initiator, 1);
					Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Magneto #1 Destroyed..");
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("oil1") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
					this.FM.AS.setOilState(shot.initiator, k, 1);
					Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Oil Filter Pierced..");
				}
			} else if (s.startsWith("xxlock")) {
				this.debuggunnery("Lock Construction: Hit..");
				if (s.startsWith("xxlockr1") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
					this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
				}
				if (s.startsWith("xxlockr2") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
					this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
				}
				if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
				}
				if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
				}
				if (s.startsWith("xxlockal") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
				}
				if (s.startsWith("xxlockar") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
				}
			} else if (s.startsWith("xxoil")) {
				int l = s.charAt(5) - 49;
				if (this.getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2435F) this.FM.AS.hitOil(shot.initiator, l);
				Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Oil Tank Pierced..");
			} else if (s.startsWith("xxpnm")) {
				if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F) {
					this.debuggunnery("Pneumo System: Disabled..");
					this.FM.AS.setInternalDamage(shot.initiator, 1);
				}
			} else if (s.startsWith("xxradio")) this.getEnergyPastArmor(World.Rnd().nextFloat(5F, 25F), shot);
			else if (s.startsWith("xxspar")) {
				if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if (s.startsWith("xxspark1") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
					this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
				}
				if (s.startsWith("xxspark2") && this.chunkDamageVisible("Keel2") > 1 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Keel2 Spars Damaged..");
					this.nextDMGLevels(1, 2, "Keel2_D" + this.chunkDamageVisible("Keel2"), shot.initiator);
				}
				if (s.startsWith("xxsparsl") && this.chunkDamageVisible("StabL") > 1 && this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** StabL Spars Damaged..");
					this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
				}
				if (s.startsWith("xxsparsr") && this.chunkDamageVisible("StabR") > 1 && this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.debuggunnery("*** StabR Spars Damaged..");
					this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
				}
			} else if (s.startsWith("xxtank")) {
				int i1 = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.06F, shot) > 0.0F) {
					if (this.FM.AS.astateTankStates[i1] == 0) {
						this.FM.AS.hitTank(shot.initiator, i1, 1);
						this.FM.AS.doSetTankState(shot.initiator, i1, 1);
					}
					if (shot.powerType == 3) {
						if (shot.power < 16100F) {
							if (this.FM.AS.astateTankStates[i1] < 4 && World.Rnd().nextFloat() < 0.21F) this.FM.AS.hitTank(shot.initiator, i1, 1);
						} else this.FM.AS.hitTank(shot.initiator, i1, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
					} else if (shot.power > 16100F) this.FM.AS.hitTank(shot.initiator, i1, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
				}
			}
		} else if (s.startsWith("xcf")) {
			if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel1")) {
			if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
		} else if (s.startsWith("xkeel2")) {
			if (this.chunkDamageVisible("Keel2") < 2) this.hitChunk("Keel2", shot);
		} else if (s.startsWith("xrudder1")) {
			if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
		} else if (s.startsWith("xrudder2")) {
			if (this.chunkDamageVisible("Rudder2") < 1) this.hitChunk("Rudder2", shot);
		} else if (s.startsWith("xstabl")) {
			if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
		} else if (s.startsWith("xstabr")) {
			if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
		} else if (s.startsWith("xwinglin")) {
			if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
		} else if (s.startsWith("xwingrin")) {
			if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
		} else if (s.startsWith("xwinglmid")) {
			if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
		} else if (s.startsWith("xwingrmid")) {
			if (this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
		} else if (s.startsWith("xwinglout")) {
			if (this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
		} else if (s.startsWith("xwingrout")) {
			if (this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
		} else if (s.startsWith("xaronel")) {
			if (this.chunkDamageVisible("AroneL") < 1) this.hitChunk("AroneL", shot);
		} else if (s.startsWith("xaroner")) {
			if (this.chunkDamageVisible("AroneR") < 1) this.hitChunk("AroneR", shot);
		} else if (s.startsWith("xengine1")) {
			if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
		} else if (s.startsWith("xengine2")) {
			if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
		} else if (s.startsWith("xengine3")) {
			if (this.chunkDamageVisible("Engine3") < 2) this.hitChunk("Engine3", shot);
		} else if (s.startsWith("xengine4")) {
			if (this.chunkDamageVisible("Engine4") < 2) this.hitChunk("Engine4", shot);
		} else if (s.startsWith("xgear")) {
			if (World.Rnd().nextFloat() < 0.05F) {
				Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
				this.FM.Gears.setHydroOperable(false);
			}
		} else if (!s.startsWith("xturret")) if (s.startsWith("xmgun")) {
			int j1 = 10 * (s.charAt(5) - 48) + s.charAt(6) - 48;
			if (this.getEnergyPastArmor(6.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F) {
				switch (j1) {
					case 1:
						j1 = 10;
						byte0 = 0;
						break;

					case 2:
						j1 = 10;
						byte0 = 1;
						break;

					case 3:
						j1 = 11;
						byte0 = 0;
						break;

					case 4:
						j1 = 11;
						byte0 = 1;
						break;

					case 5:
						j1 = 11;
						byte0 = 2;
						break;

					case 6:
						j1 = 11;
						byte0 = 3;
						break;

					case 7:
						j1 = 12;
						byte0 = 0;
						break;

					case 8:
						j1 = 12;
						byte0 = 1;
						break;

					case 9:
						j1 = 12;
						byte0 = 2;
						break;

					case 10:
						j1 = 12;
						byte0 = 3;
						break;
				}
				this.FM.AS.setJamBullets(j1, byte0);
			}
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte1 = 0;
			int k1;
			if (s.endsWith("a")) {
				byte1 = 1;
				k1 = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte1 = 2;
				k1 = s.charAt(6) - 49;
			} else k1 = s.charAt(5) - 49;
			this.hitFlesh(k1, shot, byte1);
		}
	}

	public void msgExplosion(Explosion explosion) {
		this.setExplosion(explosion);
		if (explosion.chunkName == null || explosion.power <= 0.0F || !explosion.chunkName.equals("Tail1_D3") && !explosion.chunkName.equals("WingLIn_D3") && !explosion.chunkName.equals("WingRIn_D3") && !explosion.chunkName.equals("WingLMid_D3")
				&& !explosion.chunkName.equals("WingRMid_D3") && !explosion.chunkName.equals("WingLOut_D3") && !explosion.chunkName.equals("WingROut_D3"))
			super.msgExplosion(explosion);
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2, boolean flag) {
		if (flag) {
			if (f < 0.5F) {
				hiermesh.chunkSetAngles("GearL6_D0", 0.0F, smoothCvt(f, 0.05F, 0.33F, 0.0F, -70F), 0.0F);
				hiermesh.chunkSetAngles("GearL7_D0", 0.0F, smoothCvt(f, 0.0F, 0.28F, 0.0F, -70F), 0.0F);
			} else {
				hiermesh.chunkSetAngles("GearL6_D0", 0.0F, smoothCvt(f, 0.8F, 0.89F, -70F, -56F), 0.0F);
				hiermesh.chunkSetAngles("GearL7_D0", 0.0F, smoothCvt(f, 0.8F, 0.89F, -70F, -56F), 0.0F);
			}
			if (f1 < 0.5F) {
				hiermesh.chunkSetAngles("GearR6_D0", 0.0F, smoothCvt(f1, 0.15F, 0.43F, 0.0F, 70F), 0.0F);
				hiermesh.chunkSetAngles("GearR7_D0", 0.0F, smoothCvt(f1, 0.1F, 0.38F, 0.0F, 70F), 0.0F);
			} else {
				hiermesh.chunkSetAngles("GearR6_D0", 0.0F, smoothCvt(f1, 0.85F, 0.94F, 70F, 56F), 0.0F);
				hiermesh.chunkSetAngles("GearR7_D0", 0.0F, smoothCvt(f1, 0.85F, 0.94F, 70F, 56F), 0.0F);
			}
			hiermesh.chunkSetAngles("GearL2_D0", 0.0F, smoothCvt(f, 0.25F, 0.7F, 0.0F, -80F), 0.0F);
			hiermesh.chunkSetAngles("GearL4_D0", 0.0F, smoothCvt(f, 0.25F, 0.7F, -170F, -55F), 0.0F);
			hiermesh.chunkSetAngles("GearL5_D0", 0.0F, smoothCvt(f, 0.25F, 0.7F, -30F, -190F), 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 0.0F, smoothCvt(f1, 0.3F, 0.75F, 0.0F, -80F), 0.0F);
			hiermesh.chunkSetAngles("GearR4_D0", 0.0F, smoothCvt(f1, 0.3F, 0.75F, -170F, -55F), 0.0F);
			hiermesh.chunkSetAngles("GearR5_D0", 0.0F, smoothCvt(f1, 0.3F, 0.75F, -30F, -190F), 0.0F);
			hiermesh.chunkSetAngles("GearC2_D0", 0.0F, smoothCvt(f2, 0.55F, 0.99F, 0.0F, -95F), 0.0F);
		} else {
			if (f < 0.7F) {
				hiermesh.chunkSetAngles("GearL6_D0", 0.0F, smoothCvt(f, 0.25F, 0.53F, 0.0F, -80F), 0.0F);
				hiermesh.chunkSetAngles("GearL7_D0", 0.0F, smoothCvt(f, 0.2F, 0.48F, 0.0F, -80F), 0.0F);
			} else {
				hiermesh.chunkSetAngles("GearL6_D0", 0.0F, smoothCvt(f, 0.9F, 0.99F, -80F, -60F), 0.0F);
				hiermesh.chunkSetAngles("GearL7_D0", 0.0F, smoothCvt(f, 0.9F, 0.99F, -80F, -60F), 0.0F);
			}
			if (f1 < 0.7F) {
				hiermesh.chunkSetAngles("GearR6_D0", 0.0F, smoothCvt(f1, 0.35F, 0.63F, 0.0F, 80F), 0.0F);
				hiermesh.chunkSetAngles("GearR7_D0", 0.0F, smoothCvt(f1, 0.3F, 0.58F, 0.0F, 80F), 0.0F);
			} else {
				hiermesh.chunkSetAngles("GearR6_D0", 0.0F, smoothCvt(f1, 0.85F, 0.94F, 80F, 60F), 0.0F);
				hiermesh.chunkSetAngles("GearR7_D0", 0.0F, smoothCvt(f1, 0.85F, 0.94F, 80F, 60F), 0.0F);
			}
			hiermesh.chunkSetAngles("GearL2_D0", 0.0F, smoothCvt(f, 0.5F, 0.95F, 0.0F, -80F), 0.0F);
			hiermesh.chunkSetAngles("GearL4_D0", 0.0F, smoothCvt(f, 0.5F, 0.95F, -170F, -55F), 0.0F);
			hiermesh.chunkSetAngles("GearL5_D0", 0.0F, smoothCvt(f, 0.5F, 0.95F, -30F, -190F), 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 0.0F, smoothCvt(f1, 0.55F, 0.99F, 0.0F, -80F), 0.0F);
			hiermesh.chunkSetAngles("GearR4_D0", 0.0F, smoothCvt(f1, 0.55F, 0.99F, -170F, -55F), 0.0F);
			hiermesh.chunkSetAngles("GearR5_D0", 0.0F, smoothCvt(f1, 0.55F, 0.99F, -30F, -190F), 0.0F);
			hiermesh.chunkSetAngles("GearC2_D0", 0.0F, smoothCvt(f2, 0.01F, 0.45F, 0.0F, -95F), 0.0F);
		}
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		moveGear(hiermesh, f, f1, f2, true);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(this.hierMesh(), f, f1, f2, this.FM.CT.GearControl > 0.5F);
	}

	public static void moveGear(HierMesh hiermesh, float f, boolean flag) {
		moveGear(hiermesh, f, f, f, flag);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		moveGear(hiermesh, f, f, f, true);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f, this.FM.CT.GearControl > 0.5F);
	}

	private static float smoothCvt(float f, float f1, float f2, float f3, float f4) {
		f = Math.min(Math.max(f, f1), f2);
		return f3 + (f4 - f3) * (-0.5F * (float) Math.cos((f - f1) / (f2 - f1) * Math.PI) + 0.5F);
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.456F, 0.0F, 0.2821F);
		this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
		this.resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.456F, 0.0F, 0.2821F);
		this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 33:
				this.hitProp(1, j, actor);
				this.FM.EI.engines[1].setEngineStuck(actor);
				this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 9));
				// fall through

			case 34:
				this.hitProp(0, j, actor);
				this.FM.EI.engines[0].setEngineStuck(actor);
				this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(2, 8));
				this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 5));
				// fall through

			case 35:
				this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(0, 4));
				break;

			case 36:
				this.hitProp(2, j, actor);
				this.FM.EI.engines[2].setEngineStuck(actor);
				this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 9));
				// fall through

			case 37:
				this.hitProp(3, j, actor);
				this.FM.EI.engines[3].setEngineStuck(actor);
				this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 5));
				this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(2, 8));
				// fall through

			case 38:
				this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(0, 4));
				break;

			case 25:
				this.FM.turret[0].bIsOperable = false;
				return false;

			case 26:
				this.FM.turret[1].bIsOperable = false;
				return false;

			case 27:
				this.FM.turret[2].bIsOperable = false;
				return false;

			case 28:
				this.FM.turret[3].bIsOperable = false;
				return false;

			case 29:
				this.FM.turret[4].bIsOperable = false;
				return false;

			case 30:
				this.FM.turret[5].bIsOperable = false;
				return false;

			case 19:
				this.killPilot(this, 5);
				this.killPilot(this, 6);
				this.cut("StabL");
				this.cut("StabR");
				break;

			case 13:
				this.killPilot(this, 0);
				this.killPilot(this, 1);
				this.killPilot(this, 2);
				this.killPilot(this, 3);
				break;

			case 17:
				this.cut("Keel1");
				this.hierMesh().chunkVisible("Keel1_CAP", false);
				break;

			case 18:
				this.cut("Keel2");
				this.hierMesh().chunkVisible("Keel2_CAP", false);
				break;
		}
		return super.cutFM(i, j, actor);
	}

	protected void moveElevator(float f) {
		this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -20F * f, 0.0F);
		this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -20F * f, 0.0F);
	}

	protected void moveBayDoor(float f) {
		this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -20F * f, 0.0F);
		this.hierMesh().chunkSetLocate("Bay02_D0", new float[] { -0.1F * f, 0.0F, 0.0F }, new float[] { 0.0F, -70F * f, 0.0F });
		this.hierMesh().chunkSetAngles("Bay04_D0", 0.0F, -20F * f, 0.0F);
		this.hierMesh().chunkSetLocate("Bay05_D0", new float[] { 0.1F * f, 0.0F, 0.0F }, new float[] { 0.0F, -70F * f, 0.0F });
		this.hierMesh().chunkSetAngles("Bay06_D0", 0.0F, 85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay03_D0", 0.0F, 85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay13_D0", 0.0F, 85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay14_D0", 0.0F, 85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay15_D0", 0.0F, 85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay16_D0", 0.0F, 85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay07_D0", 0.0F, -85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay10_D0", 0.0F, -85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay11_D0", 0.0F, -85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay08_D0", 0.0F, -85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay12_D0", 0.0F, -85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay09_D0", 0.0F, -85F * f, 0.0F);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag) {
			if (this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
			if (this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
			if (this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
			if (this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
			if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) for (int i = 0; i < this.FM.EI.getNum(); i++)
				if (this.FM.AS.astateEngineStates[i] > 3 && World.Rnd().nextFloat() < 0.2F) this.FM.EI.engines[i].setExtinguisherFire();
		}
		for (int j = 1; j < 5; j++)
			if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + j + "_D0", false);
			else this.hierMesh().chunkVisible("HMask" + j + "_D0", this.hierMesh().isChunkVisible("Pilot" + j + "_D0"));

	}

	public void doMurderPilot(int i) {
		this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
		this.hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
		this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
		this.hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
	}

	private static final float toMeters(float f) {
		return 0.3048F * f;
	}

	private static final float toMetersPerSecond(float f) {
		return 0.4470401F * f;
	}

	public boolean typeBomberToggleAutomation() {
		this.bSightAutomation = !this.bSightAutomation;
		this.bSightBombDump = false;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
		return this.bSightAutomation;
	}

	public void typeBomberAdjDistanceReset() {
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		this.fSightCurForwardAngle++;
		if (this.fSightCurForwardAngle > 85F) this.fSightCurForwardAngle = 85F;
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
		if (this.bSightAutomation) this.typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus() {
		this.fSightCurForwardAngle--;
		if (this.fSightCurForwardAngle < 0.0F) this.fSightCurForwardAngle = 0.0F;
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
		if (this.bSightAutomation) this.typeBomberToggleAutomation();
	}

	public void typeBomberAdjSideslipReset() {
		this.fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		this.fSightCurSideslip += 0.05F;
		if (this.fSightCurSideslip > 3F) this.fSightCurSideslip = 3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjSideslipMinus() {
		this.fSightCurSideslip -= 0.05F;
		if (this.fSightCurSideslip < -3F) this.fSightCurSideslip = -3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjAltitudeReset() {
		this.fSightCurAltitude = 3000F;
	}

	public void typeBomberAdjAltitudePlus() {
		this.fSightCurAltitude += 50F;
		if (this.fSightCurAltitude > 50000F) this.fSightCurAltitude = 50000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus() {
		this.fSightCurAltitude -= 50F;
		if (this.fSightCurAltitude < 1000F) this.fSightCurAltitude = 1000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
	}

	public void typeBomberAdjSpeedReset() {
		this.fSightCurSpeed = 200F;
	}

	public void typeBomberAdjSpeedPlus() {
		this.fSightCurSpeed += 10F;
		if (this.fSightCurSpeed > 450F) this.fSightCurSpeed = 450F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		this.fSightCurSpeed -= 10F;
		if (this.fSightCurSpeed < 100F) this.fSightCurSpeed = 100F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
			this.fSightCurReadyness -= 0.0666666F * f;
			if (this.fSightCurReadyness < 0.0F) this.fSightCurReadyness = 0.0F;
		}
		if (this.fSightCurReadyness < 1.0F) this.fSightCurReadyness += 0.0333333F * f;
		else if (this.bSightAutomation) {
			this.fSightCurDistance -= toMetersPerSecond(this.fSightCurSpeed) * f;
			if (this.fSightCurDistance < 0.0F) {
				this.fSightCurDistance = 0.0F;
				this.typeBomberToggleAutomation();
			}
			this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / toMeters(this.fSightCurAltitude)));
			if (this.fSightCurDistance < toMetersPerSecond(this.fSightCurSpeed) * Math.sqrt(toMeters(this.fSightCurAltitude) * (2F / 9.81F))) this.bSightBombDump = true;
			if (this.bSightBombDump) if (this.FM.isTick(3, 0)) {
				if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
					this.FM.CT.WeaponControl[3] = true;
					HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
				}
			} else this.FM.CT.WeaponControl[3] = false;
		}
		double d = this.fSightCurSpeed / 3.6D * Math.sqrt(this.fSightCurAltitude * 0.203873598D);
		d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
		this.fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / this.fSightCurAltitude));
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
		netmsgguaranted.writeFloat(this.fSightCurDistance);
		netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
		netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
		netmsgguaranted.writeFloat(this.fSightCurAltitude);
		netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
		netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		int i = netmsginput.readUnsignedByte();
		this.bSightAutomation = (i & 1) != 0;
		this.bSightBombDump = (i & 2) != 0;
		this.fSightCurDistance = netmsginput.readFloat();
		this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
		this.fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
		this.fSightCurAltitude = netmsginput.readFloat();
		this.fSightCurSpeed = netmsginput.readUnsignedByte() * 2.5F;
		this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
	}

	public void msgShot(Shot shot) {
		this.setShot(shot);
		if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitEngine(shot.initiator, 0, 1);
		if (shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitEngine(shot.initiator, 1, 1);
		if (shot.chunkName.startsWith("Engine3") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitEngine(shot.initiator, 2, 1);
		if (shot.chunkName.startsWith("Engine4") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitEngine(shot.initiator, 3, 1);
		if (shot.chunkName.startsWith("CF")) {
			if (Aircraft.Pd.x > 4.55D && Aircraft.Pd.x < 7.15D && Aircraft.Pd.z > 0.58D) {
				if (World.Rnd().nextFloat() < 0.233F) if (Aircraft.Pd.z > 1.21D) {
					this.killPilot(shot.initiator, 0);
					if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
				} else this.FM.AS.hitPilot(shot.initiator, 0, (int) (shot.power * 0.004F));
				if (World.Rnd().nextFloat() < 0.233F) if (Aircraft.Pd.z > 1.21D) {
					this.killPilot(shot.initiator, 1);
					if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
				} else this.FM.AS.hitPilot(shot.initiator, 1, (int) (shot.power * 0.004F));
			}
			if (Aircraft.Pd.x > 9.53D && Aircraft.Pd.z < 0.14D && Aircraft.Pd.z > -0.63D) this.FM.AS.hitPilot(shot.initiator, 2, (int) (shot.power * 0.002F));
			if (Aircraft.Pd.x > 2.475D && Aircraft.Pd.x < 4.49D && Aircraft.Pd.z > 0.61D && shot.power * Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z) > 11900D && World.Rnd().nextFloat() < 0.45F) for (int i = 0; i < 4; i++)
				this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 0);
		}
		if (shot.chunkName.startsWith("Turret1")) {
			if (Aircraft.Pd.z > 0.03345D) {
				this.killPilot(shot.initiator, 2);
				if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
			} else this.FM.AS.hitPilot(shot.initiator, 2, (int) (shot.power * 0.004F));
			shot.chunkName = "CF_D" + this.chunkDamageVisible("CF");
		}
		if (shot.chunkName.startsWith("Turret2")) if (World.Rnd().nextBoolean()) this.FM.AS.hitPilot(shot.initiator, 4, (int) (shot.power * 0.004F));
		else this.FM.turret[1].bIsOperable = false;
		if (shot.chunkName.startsWith("Turret3")) {
			if (Aircraft.Pd.z > 0.30445000529289246D) {
				this.killPilot(shot.initiator, 7);
				if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
			} else this.FM.AS.hitPilot(shot.initiator, 7, (int) (shot.power * 0.002F));
			shot.chunkName = "Tail1_D" + this.chunkDamageVisible("Tail1");
		}
		if (shot.chunkName.startsWith("Turret4")) {
			if (Aircraft.Pd.z > -0.99540001153945923D) {
				this.killPilot(shot.initiator, 5);
				if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
			} else this.FM.AS.hitPilot(shot.initiator, 5, (int) (shot.power * 0.002F));
		} else if (shot.chunkName.startsWith("Turret5")) {
			if (Aircraft.Pd.z > -0.99540001153945923D) {
				this.killPilot(shot.initiator, 6);
				if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
			} else this.FM.AS.hitPilot(shot.initiator, 6, (int) (shot.power * 0.002F));
		} else super.msgShot(shot);
	}

	private boolean bSightAutomation;
	private boolean bSightBombDump;
	private float   fSightCurDistance;
	public float    fSightCurForwardAngle;
	public float    fSightCurSideslip;
	public float    fSightCurAltitude;
	public float    fSightCurSpeed;
	public float    fSightCurReadyness;
	public float    fSightSetForwardAngle;

	static {
		Property.set(Halifax.class, "originCountry", PaintScheme.countryBritain);
	}
}
