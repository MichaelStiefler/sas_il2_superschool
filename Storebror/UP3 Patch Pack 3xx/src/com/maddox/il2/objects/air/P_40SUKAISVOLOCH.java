package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class P_40SUKAISVOLOCH extends Scheme1 implements TypeFighter {

	public P_40SUKAISVOLOCH() {
		this.flapps = 0.0F;
	}

	public float getEyeLevelCorrection() {
		return -0.05F;
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f2, 0.04F, 0.5F, 0.0F, -70F), 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f2, 0.02F, 0.09F, 0.0F, -60F), 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f2, 0.02F, 0.09F, 0.0F, -60F), 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.01F, 0.79F, 0.0F, -90F), 0.0F);
		hiermesh.chunkSetAngles("GearL21_D0", 0.0F, cvt(f, 0.01F, 0.79F, 0.0F, 94F), 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, cvt(f, 0.01F, 0.39F, 0.0F, -53F), 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.01F, 0.11F, 0.0F, -100F), 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.01F, 0.79F, 0.0F, 100F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f1, 0.21F, 0.99F, 0.0F, -90F), 0.0F);
		hiermesh.chunkSetAngles("GearR21_D0", 0.0F, cvt(f1, 0.21F, 0.99F, 0.0F, -94F), 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, cvt(f1, 0.21F, 0.59F, 0.0F, -53F), 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f1, 0.21F, 0.31F, 0.0F, -100F), 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f1, 0.21F, 0.99F, 0.0F, 100F), 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(this.hierMesh(), f, f1, f2);
	}

	// ************************************************************************************************
	// Gear code for backward compatibility, older base game versions don't
	// indepently move their gears
	public static void moveGear(HierMesh hiermesh, float gearPos) {
		moveGear(hiermesh, gearPos, gearPos, gearPos);
	}

	protected void moveGear(float gearPos) {
		moveGear(this.hierMesh(), gearPos);
	}
	// ************************************************************************************************

	public void moveWheelSink() {
		this.resetYPRmodifier();
		xyz[1] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.26F, 0.0F, 0.26F);
		ypr[1] = cvt(this.FM.CT.getGear(), 0.01F, 0.79F, 0.0F, 94F);
		this.hierMesh().chunkSetLocate("GearL21_D0", xyz, ypr);
		this.hierMesh().chunkSetAngles("GearL6_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.26F, 0.0F, -25F),
				0.0F);
		this.hierMesh().chunkSetAngles("GearL7_D0", 0.0F,
				cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.26F, 0.0F, -68.5F), 0.0F);
		this.resetYPRmodifier();
		xyz[1] = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.26F, 0.0F, 0.26F);
		ypr[1] = cvt(this.FM.CT.getGear(), 0.01F, 0.79F, 0.0F, -94F);
		this.hierMesh().chunkSetLocate("GearR21_D0", xyz, ypr);
		this.hierMesh().chunkSetAngles("GearR6_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.26F, 0.0F, -25F),
				0.0F);
		this.hierMesh().chunkSetAngles("GearR7_D0", 0.0F,
				cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.26F, 0.0F, -68.5F), 0.0F);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, f, 0.0F);
	}

	public void moveCockpitDoor(float f) {
		this.resetYPRmodifier();
		xyz[1] = cvt(f, 0.01F, 0.99F, 0.0F, 0.63F);
		this.hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
		if (Config.isUSE_RENDER()) {
			if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			}
			this.setDoorSnd(f);
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
		default:
			break;

		case 0:
			if (!this.FM.AS.bIsAboutToBailout) {
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
			}
			break;
		}
	}

	protected void moveFlap(float f) {
		float f1 = -40F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
		}
	}

	public void msgExplosion(Explosion explosion) {
		this.setExplosion(explosion);
		if (explosion.chunkName != null) {
			if (explosion.chunkName.startsWith("WingL")) {
				if (World.Rnd().nextFloat() < 0.02F) {
					this.FM.AS.setJamBullets(0, 0);
				}
				if (World.Rnd().nextFloat() < 0.02F) {
					this.FM.AS.setJamBullets(0, 1);
				}
				if (World.Rnd().nextFloat() < 0.02F) {
					this.FM.AS.setJamBullets(0, 2);
				}
			}
			if (explosion.chunkName.startsWith("WingR")) {
				if (World.Rnd().nextFloat() < 0.02F) {
					this.FM.AS.setJamBullets(0, 3);
				}
				if (World.Rnd().nextFloat() < 0.02F) {
					this.FM.AS.setJamBullets(0, 4);
				}
				if (World.Rnd().nextFloat() < 0.02F) {
					this.FM.AS.setJamBullets(0, 5);
				}
			}
		}
		super.msgExplosion(explosion);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1")) {
					this.getEnergyPastArmor(15F / (1E-005F + (float) Math.abs(v1.x)), shot);
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
				} else if (s.endsWith("p2")) {
					this.getEnergyPastArmor(4F / (1E-005F + (float) Math.abs(v1.x)), shot);
				} else if (s.endsWith("p3")) {
					this.getEnergyPastArmor(2.0F / (1E-005F + (float) Math.abs(v1.x)), shot);
				}
			}
			if (s.startsWith("xxcontrols")) {
				if (s.endsWith("1")) {
					if (World.Rnd().nextFloat() < 0.3F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
						debugprintln(this, "*** Engine Controls Out..");
					}
					if (World.Rnd().nextFloat() < 0.3F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
						debugprintln(this, "*** Engine Controls Out..");
					}
				} else if (s.endsWith("2")) {
					if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
						this.FM.AS.setControlsDamage(shot.initiator, 1);
						debugprintln(this, "*** Evelator Controls Out..");
					}
					if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
						this.FM.AS.setControlsDamage(shot.initiator, 0);
						debugprintln(this, "*** Ailerones Controls Out..");
					}
					if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
						this.FM.AS.setControlsDamage(shot.initiator, 2);
						debugprintln(this, "*** Rudder Controls Out..");
					}
					if (World.Rnd().nextFloat() < 0.3F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
						debugprintln(this, "*** Engine Controls Out..");
					}
					if (World.Rnd().nextFloat() < 0.3F) {
						this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
						debugprintln(this, "*** Engine Controls Out..");
					}
				} else if (s.endsWith("3")) {
					if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
						this.FM.AS.setControlsDamage(shot.initiator, 2);
						debugprintln(this, "*** Rudder Controls Out..");
					}
				} else if ((s.endsWith("4") || s.endsWith("5")) && (World.Rnd().nextFloat() < 0.3F)) {
					this.FM.AS.setControlsDamage(shot.initiator, 0);
					debugprintln(this, "*** Ailerones Controls Out..");
				}
			}
			if (s.startsWith("xxeng1")) {
				if (s.endsWith("prp") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
					this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
				}
				if (s.endsWith("cas") && (this.getEnergyPastArmor(0.7F, shot) > 0.0F)) {
					if (World.Rnd().nextFloat(20000F, 200000F) < shot.power) {
						this.FM.AS.setEngineStuck(shot.initiator, 0);
						debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
					}
					if (World.Rnd().nextFloat(10000F, 50000F) < shot.power) {
						this.FM.AS.hitEngine(shot.initiator, 0, 2);
						debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
					}
					if (World.Rnd().nextFloat(8000F, 28000F) < shot.power) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
						debugprintln(this,
								"*** Engine Crank Case Hit - Cylinder Feed Out, "
										+ this.FM.EI.engines[0].getCylindersOperable() + "/"
										+ this.FM.EI.engines[0].getCylinders() + " Left..");
					}
					this.FM.EI.engines[0].setReadyness(shot.initiator,
							this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
					debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to "
							+ this.FM.EI.engines[0].getReadyness() + "..");
				}
				if (s.endsWith("cyl") && (this.getEnergyPastArmor(0.45F, shot) > 0.0F)
						&& (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.75F))) {
					this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator,
							World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
					debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/"
							+ this.FM.EI.engines[0].getCylinders() + " Left..");
					if (this.FM.AS.astateEngineStates[0] < 1) {
						this.FM.AS.hitEngine(shot.initiator, 0, 1);
					}
					if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
						this.FM.AS.hitEngine(shot.initiator, 0, 3);
						debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("sup") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
					this.FM.EI.engines[0].setKillCompressor(shot.initiator);
				}
				if (s.endsWith("sup")) {
					if ((World.Rnd().nextFloat() < 0.3F) && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
						this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
					}
					if ((World.Rnd().nextFloat() < 0.3F) && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
						this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
					}
					if ((World.Rnd().nextFloat() < 0.3F) && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
						this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
					}
					if ((World.Rnd().nextFloat() < 0.3F) && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
						this.FM.EI.engines[0].setEngineStuck(shot.initiator);
					}
					if ((World.Rnd().nextFloat() < 0.3F) && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
						this.FM.EI.engines[0].setEngineStops(shot.initiator);
					}
				}
				if (s.endsWith("oil")) {
					this.FM.AS.hitOil(shot.initiator, 0);
				}
			}
			if (s.startsWith("xxtank")) {
				int i = s.charAt(6) - 49;
				if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
					this.FM.AS.hitTank(shot.initiator, i, 1);
					if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.11F)) {
						this.FM.AS.hitTank(shot.initiator, i, 2);
					}
				}
			}
			if (s.startsWith("xxmgun")) {
				int j = s.charAt(6) - 49;
				this.FM.AS.setJamBullets(0, j);
			}
			if (s.startsWith("xxammo")) {
				int k = s.charAt(6) - 48;
				if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
					switch (k) {
					case 1:
						this.FM.AS.setJamBullets(0, 2);
						break;

					case 2:
						this.FM.AS.setJamBullets(0, 3);
						break;

					case 3:
						this.FM.AS.setJamBullets(0, 4);
						break;

					case 4:
						this.FM.AS.setJamBullets(0, 5);
						break;
					}
				}
			}
			return;
		}
		if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
			this.hitChunk("CF", shot);
		}
		if (s.startsWith("xcockpit")) {
			if (point3d.z > 0.75D) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
			}
			if ((point3d.x > -1.1000000238418579D) && (World.Rnd().nextFloat() < 0.1F)) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
			}
			if (World.Rnd().nextFloat() < 0.25F) {
				if (point3d.y > 0.0D) {
					if (point3d.x > -1.1000000238418579D) {
						this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
					} else {
						this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
					}
				} else if (point3d.x > -1.1000000238418579D) {
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
				} else {
					this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
				}
			}
		} else if (s.startsWith("xeng")) {
			this.hitChunk("Engine1", shot);
		} else if (s.startsWith("xtail")) {
			this.hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) {
			this.hitChunk("Keel1", shot);
		} else if (s.startsWith("xrudder")) {
			this.hitChunk("Rudder1", shot);
		} else if (s.startsWith("xstabl")) {
			this.hitChunk("StabL", shot);
		} else if (s.startsWith("xstabr")) {
			this.hitChunk("StabR", shot);
		} else if (s.startsWith("xvatorl")) {
			this.hitChunk("VatorL", shot);
		} else if (s.startsWith("xvatorr")) {
			this.hitChunk("VatorR", shot);
		} else if (s.startsWith("xwing")) {
			if (s.startsWith("xwinglin")) {
				this.hitChunk("WingLIn", shot);
			}
			if (s.startsWith("xwingrin")) {
				this.hitChunk("WingRIn", shot);
			}
			if (s.startsWith("xwinglmid")) {
				this.hitChunk("WingLMid", shot);
			}
			if (s.startsWith("xwingrmid")) {
				this.hitChunk("WingRMid", shot);
			}
			if (s.startsWith("xwinglout")) {
				this.hitChunk("WingLOut", shot);
			}
			if (s.startsWith("xwingrout")) {
				this.hitChunk("WingROut", shot);
			}
		} else if (s.startsWith("xarone")) {
			if (s.startsWith("xaronel")) {
				this.hitChunk("AroneL", shot);
			}
			if (s.startsWith("xaroner")) {
				this.hitChunk("AroneR", shot);
			}
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
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

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 35:
			this.FM.AS.setJamBullets(0, 2);
			this.FM.AS.setJamBullets(0, 3);
			break;

		case 38:
			this.FM.AS.setJamBullets(0, 4);
			this.FM.AS.setJamBullets(0, 5);
			break;
		}
		switch (i) {
		case 11:
		case 19:
		case 33:
		case 34:
		case 35:
		case 36:
		case 37:
		case 38:
			this.hierMesh().chunkVisible("Wire_D0", false);
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		float f1 = cvt(this.FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 27F, -11F);
		if (Math.abs(this.flapps - f1) > 0.2F) {
			this.flapps = f1;
			this.hierMesh().chunkSetAngles("Water2_D0", 0.0F, f1, 0.0F);
			this.hierMesh().chunkSetAngles("Water3_D0", 0.0F, f1, 0.0F);
			f1 = Math.min(f1, 10.5F);
			this.hierMesh().chunkSetAngles("Water1_D0", 0.0F, f1, 0.0F);
			this.hierMesh().chunkSetAngles("Water4_D0", 0.0F, f1, 0.0F);
		}
		super.update(f);
	}

	private float flapps;

	static {
		Class class1 = P_40SUKAISVOLOCH.class;
		Property.set(class1, "originCountry", PaintScheme.countryBritain);
	}
}
