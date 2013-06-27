// This file is part of the SAS IL-2 Sturmovik 1946 4.12
// Flyable AI Aircraft Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
//              www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class N1K extends Scheme1 implements TypeFighter {

	public N1K() {
		flapps = 0.0F;
		curFlaps = 0.0F;
		mBar = 0.0F;
		desiredPosition = 0.0F;
		autoEng = false;
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			break;
		}
	}

	public void update(float f) {
		super.update(f);
		if (FM.CT.FlapsControl <= 0.32F && FM.CT.FlapsControl >= 0.05F) {
			if (this == World.getPlayerAircraft() && !autoEng) {
				HUD.log("FlapsAuto");
				autoEng = true;
				curFlaps = FM.CT.getFlap();
			}
			float f1 = Pitot.Indicator((float) FM.Loc.z, FM.getSpeed());
			mBar = (f1 * f1) / (FM.getOverload() * 10F);
			if (mBar < 315F && FM.getOverload() > 0.0F && f1 < 128F) {
				desiredPosition = (315F - mBar) / 140F;
				if (desiredPosition > 1.0F)
					desiredPosition = 1.0F;
				if (curFlaps < desiredPosition)
					curFlaps = flapsMovement(f, desiredPosition, curFlaps, 999F, cvt(FM.getSpeedKMH(), 150F, 280F, 0.15F, 0.08F));
				else
					curFlaps = flapsMovement(f, desiredPosition, curFlaps, 999F, cvt(FM.getSpeedKMH(), 150F, 280F, 0.15F, 0.25F));
				FM.CT.forceFlaps(curFlaps);
			} else {
				desiredPosition = 0.0F;
				curFlaps = flapsMovement(f, desiredPosition, curFlaps, 999F, cvt(FM.getSpeedKMH(), 150F, 280F, 0.15F, 0.25F));
				FM.CT.forceFlaps(curFlaps);
			}
			if (Math.abs(desiredPosition - curFlaps) >= 0.02F)
				sfxFlaps(true);
			else
				sfxFlaps(false);
		} else {
			autoEng = false;
		}
		if (FM.CT.BrakeControl > 0.4F)
			FM.CT.BrakeControl = 0.4F;
	}

	private float flapsMovement(float f, float f1, float f2, float f3, float f4) {
		float f5 = (float) Math.exp(-f / f3);
		float f6 = f1 + (f2 - f1) * f5;
		if (f6 < f1) {
			f6 += f4 * f;
			if (f6 > f1)
				f6 = f1;
		} else if (f6 > f1) {
			f6 -= f4 * f;
			if (f6 < f1)
				f6 = f1;
		}
		return f6;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (FM.getAltitude() < 3000F)
			hierMesh().chunkVisible("HMask1_D0", false);
		else
			hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		xyz[0] = xyz[1] = xyz[2] = ypr[0] = ypr[1] = ypr[2] = 0.0F;
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f2, 0.75F, 0.95F, 0.0F, -60F), 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.05F, 0.95F, 0.0F, -85F), 0.0F);
		xyz[1] = cvt(f, 0.4F, 0.75F, 0.0F, -0.205F);
		hiermesh.chunkSetLocate("GearL3_D0", xyz, ypr);
		xyz[1] = cvt(f, 0.75F, 0.94F, 0.0F, -0.05F);
		hiermesh.chunkSetLocate("GearL4_D0", xyz, ypr);
		if (f > 0.75F)
			hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.75F, 0.95F, -45F, -65F), 0.0F);
		else
			hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.4F, 0.75F, 0.0F, -45F), 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, cvt(f, 0.05F, 0.95F, 0.0F, -130F), 0.0F);
		if (f > 0.75F)
			hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.725F, 0.95F, -86F, -133F), 0.0F);
		else
			hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.05F, 0.725F, 0.0F, -86F), 0.0F);
		xyz[1] = cvt(f, 0.05F, 0.95F, 0.0F, -0.1F);
		hiermesh.chunkSetLocate("GearL8_D0", xyz, ypr);
		hiermesh.chunkSetAngles("GearL9_D0", 0.0F, cvt(f, 0.05F, 0.115F, 0.0F, -85F), 0.0F);
		hiermesh.chunkSetAngles("GearL10_D0", 0.0F, cvt(f, 0.05F, 0.85F, 0.0F, -85F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f1, 0.05F, 0.95F, 0.0F, -85F), 0.0F);
		xyz[1] = cvt(f1, 0.4F, 0.75F, 0.0F, -0.205F);
		hiermesh.chunkSetLocate("GearR3_D0", xyz, ypr);
		xyz[1] = cvt(f1, 0.75F, 0.94F, 0.0F, -0.05F);
		hiermesh.chunkSetLocate("GearR4_D0", xyz, ypr);
		if (f1 > 0.75F)
			hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f1, 0.75F, 0.95F, -45F, -65F), 0.0F);
		else
			hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f1, 0.4F, 0.75F, 0.0F, -45F), 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, cvt(f1, 0.05F, 0.95F, 0.0F, -130F), 0.0F);
		if (f1 > 0.75F)
			hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f1, 0.725F, 0.95F, -86F, -133F), 0.0F);
		else
			hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f1, 0.05F, 0.725F, 0.0F, -86F), 0.0F);
		xyz[1] = cvt(f1, 0.05F, 0.95F, 0.0F, -0.1F);
		hiermesh.chunkSetLocate("GearR8_D0", xyz, ypr);
		hiermesh.chunkSetAngles("GearR9_D0", 0.0F, cvt(f1, 0.05F, 0.115F, 0.0F, -85F), 0.0F);
		hiermesh.chunkSetAngles("GearR10_D0", 0.0F, cvt(f1, 0.05F, 0.85F, 0.0F, -85F), 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

	public void moveWheelSink() {
	}

	public void moveSteering(float f) {
		hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
	}

	protected void moveAileron(float f) {
		if (f > 0.0F) {
			hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -16F * f, 0.0F);
			hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -28.5F * f, 0.0F);
		} else {
			hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -16F * f, 0.0F);
			hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -28.5F * f, 0.0F);
		}
	}

	protected void moveElevator(float f) {
		if (f > 0.0F) {
			hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * f, 0.0F);
			hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
		} else {
			hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -24.5F * f, 0.0F);
			hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -24.5F * f, 0.0F);
		}
	}

	protected void moveFlap(float f) {
		hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -30F * f, 0.0F);
		hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -30F * f, 0.0F);
	}

	protected void moveRudder(float f) {
		hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -33F * f, 0.0F);
	}

	public void moveCockpitDoor(float f) {
		resetYPRmodifier();
		xyz[1] = cvt(f, 0.1F, 0.99F, 0.0F, -0.61F);
		hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1")) {
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
					if (getEnergyPastArmor((double) World.Rnd().nextFloat(32.5F, 65F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) < 0.0F)
						doRicochetBack(shot);
				} else if (s.endsWith("p2"))
					getEnergyPastArmor(13.130000114440918D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
				else if (s.endsWith("p3"))
					getEnergyPastArmor((double) World.Rnd().nextFloat(8.7F, 9.81F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
				return;
			}
			if (s.startsWith("xxcanon0")) {
				int i = s.charAt(8) - 49;
				if (getEnergyPastArmor(6.29F, shot) > 0.0F) {
					debuggunnery("Armament: Cannon (" + i + ") Disabled..");
					FM.AS.setJamBullets(1, i);
					getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
				}
				return;
			}
			if (s.startsWith("xxcontrols")) {
				debuggunnery("Controls: Hit..");
				int j = s.charAt(10) - 48;
				switch (j) {
				default:
					break;

				case 1: // '\001'
				case 2: // '\002'
				case 3: // '\003'
				case 4: // '\004'
					if (getEnergyPastArmor(0.99F, shot) > 0.0F && World.Rnd().nextFloat() < 0.175F) {
						debuggunnery("Controls: Ailerones Controls: Out..");
						FM.AS.setControlsDamage(shot.initiator, 0);
					}
					break;

				case 5: // '\005'
				case 6: // '\006'
					if (getEnergyPastArmor(0.22F, shot) > 0.0F && World.Rnd().nextFloat() < 0.275F) {
						debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
						FM.AS.setControlsDamage(shot.initiator, 2);
					}
					break;

				case 7: // '\007'
					if (getEnergyPastArmor(4.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.175F) {
						debuggunnery("Controls: Elevator Controls: Disabled..");
						FM.AS.setControlsDamage(shot.initiator, 1);
					}
					break;

				case 8: // '\b'
					if (getEnergyPastArmor(3.2F, shot) > 0.0F) {
						debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
						FM.AS.setControlsDamage(shot.initiator, 2);
						FM.AS.setControlsDamage(shot.initiator, 1);
						FM.AS.setControlsDamage(shot.initiator, 0);
					}
					break;

				case 9: // '\t'
					if (getEnergyPastArmor(0.1F, shot) > 0.0F) {
						FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
						FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
						FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
						debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
					}
					break;
				}
				return;
			}
			if (s.startsWith("xxeng1")) {
				if (s.endsWith("case")) {
					if (getEnergyPastArmor(0.2F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 140000F) {
							FM.AS.setEngineStuck(shot.initiator, 0);
							debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 85000F) {
							FM.AS.hitEngine(shot.initiator, 0, 2);
							debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
						}
					} else if (World.Rnd().nextFloat() < 0.01F) {
						FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
					} else {
						FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.002F);
						debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
					}
					getEnergyPastArmor(12F, shot);
				}
				if (s.endsWith("cyls")) {
					if (getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.75F) {
						FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
						debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 48000F) {
							FM.AS.hitEngine(shot.initiator, 0, 2);
							debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
						}
					}
					getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("gear")) {
					if (getEnergyPastArmor(0.1F, shot) > 0.0F) {
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
							FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
							FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
							FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
						if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
							FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
					}
					getEnergyPastArmor(2.0F, shot);
				}
				if (s.endsWith("prop") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
					FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
				return;
			}
			if (s.startsWith("xxlock")) {
				debuggunnery("Lock Construction: Hit..");
				if (s.startsWith("xxlockr") && getEnergyPastArmor(2.5F, shot) > 0.0F) {
					debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
					nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
				}
				if (s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: VatorL Lock Shot Off..");
					nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
				}
				if (s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: VatorR Lock Shot Off..");
					nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
				}
				if (s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: AroneL Lock Shot Off..");
					nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
				}
				if (s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: AroneR Lock Shot Off..");
					nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxmgun0")) {
				int k = s.charAt(7) - 49;
				if (getEnergyPastArmor(0.75F, shot) > 0.0F) {
					debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
					FM.AS.setJamBullets(0, k);
					getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
				}
				return;
			}
			if (s.startsWith("xxoil")) {
				if (getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					FM.AS.hitOil(shot.initiator, 0);
					getEnergyPastArmor(0.22F, shot);
					debuggunnery("Engine Module: Oil Tank Pierced..");
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				debuggunnery("Spar Construction: Hit..");
				if (s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					debuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					debuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
					debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if (s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
					nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxtank")) {
				int l = s.charAt(6) - 49;
				if (l > 3)
					return;
				if (getEnergyPastArmor(0.8F, shot) > 0.0F && World.Rnd().nextFloat() < 0.45F) {
					if (FM.AS.astateTankStates[l] == 0) {
						debuggunnery("Fuel Tank (" + l + "): Pierced..");
						FM.AS.hitTank(shot.initiator, l, 1);
						FM.AS.doSetTankState(shot.initiator, l, 1);
					}
					if (World.Rnd().nextFloat() < 0.008F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F) {
						FM.AS.hitTank(shot.initiator, l, 1);
						debuggunnery("Fuel Tank (" + l + "): Hit..");
					}
				}
				return;
			} else {
				return;
			}
		}
		if (s.startsWith("xcf") || s.startsWith("xcock")) {
			if (chunkDamageVisible("CF") < 3)
				hitChunk("CF", shot);
			if (s.startsWith("xcock")) {
				if (((Tuple3d) (point3d)).z > 0.40000000000000002D) {
					if (World.Rnd().nextFloat() < 0.2F)
						((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
					if (World.Rnd().nextFloat() < 0.2F)
						((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x20);
				} else if (((Tuple3d) (point3d)).y > 0.0D) {
					if (World.Rnd().nextFloat() < 0.2F)
						((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 4);
				} else if (World.Rnd().nextFloat() < 0.2F)
					((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x10);
				if (((Tuple3d) (point3d)).x > 0.20000000000000001D && World.Rnd().nextFloat() < 0.2F)
					((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x40);
			}
		} else if (s.startsWith("xeng")) {
			if (chunkDamageVisible("Engine1") < 2 && getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
				hitChunk("Engine1", shot);
		} else if (s.startsWith("xtail")) {
			if (getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 5000F) < shot.power)
				hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) {
			if (getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
				hitChunk("Keel1", shot);
		} else if (s.startsWith("xrudder")) {
			if (chunkDamageVisible("Rudder1") < 1 && getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 3000F) < shot.power && getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
				hitChunk("Rudder1", shot);
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl") && getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
				hitChunk("StabL", shot);
			if (s.startsWith("xstabr") && getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
				hitChunk("StabR", shot);
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1 && getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
				hitChunk("VatorL", shot);
			if (s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1 && getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
				hitChunk("VatorR", shot);
		} else if (s.startsWith("xwing")) {
			if (s.startsWith("xwinglin") && getEnergyPastArmor(2.5F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 5000F) < shot.power)
				hitChunk("WingLIn", shot);
			if (s.startsWith("xwingrin") && getEnergyPastArmor(2.5F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 5000F) < shot.power)
				hitChunk("WingRIn", shot);
			if (s.startsWith("xwinglmid") && getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 4000F) < shot.power)
				hitChunk("WingLMid", shot);
			if (s.startsWith("xwingrmid") && getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 4000F) < shot.power)
				hitChunk("WingRMid", shot);
			if (s.startsWith("xwinglout") && getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
				hitChunk("WingLOut", shot);
			if (s.startsWith("xwingrout") && getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
				hitChunk("WingROut", shot);
		} else if (s.startsWith("xarone")) {
			if (s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
				hitChunk("AroneL", shot);
			if (s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
				hitChunk("AroneR", shot);
		} else if (s.startsWith("xgear")) {
			if (World.Rnd().nextFloat() < 0.05F) {
				debuggunnery("Hydro System: Disabled..");
				FM.AS.setInternalDamage(shot.initiator, 0);
			}
			if (World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F) {
				debuggunnery("Undercarriage: Stuck..");
				FM.AS.setInternalDamage(shot.initiator, 3);
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
			hitFlesh(i1, shot, byte0);
		}
	}

	protected float flapps;
	protected float curFlaps;
	protected float mBar;
	protected float desiredPosition;
	private boolean autoEng;

	static {
		Class class1 = N1K.class;
		Property.set(class1, "originCountry", PaintScheme.countryJapan);
	}
}
