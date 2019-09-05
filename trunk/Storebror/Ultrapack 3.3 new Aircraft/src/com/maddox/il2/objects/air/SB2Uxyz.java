package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class SB2Uxyz extends Scheme1 {

	public SB2Uxyz() {
		this.arrestor = 0.0F;
		this.flapps = 0.0F;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
			this.hierMesh().chunkVisible("HMask2_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
			this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
		}
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 1:
				this.FM.turret[0].bIsOperable = false;
				break;
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

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				break;
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxcontrols")) {
				if ((s.endsWith("1") || s.endsWith("2")) && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
					this.FM.AS.setControlsDamage(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Aileron Controls Out..");
				}
				if (s.endsWith("3") && this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
					this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
					this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
					Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
				}
				if (s.endsWith("4") && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
					this.FM.AS.setControlsDamage(shot.initiator, 1);
					Aircraft.debugprintln(this, "*** Evelator Controls Out..");
				}
				if (s.endsWith("5") && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
					this.FM.AS.setControlsDamage(shot.initiator, 2);
					Aircraft.debugprintln(this, "*** Rudder Controls Out..");
				}
				return;
			}
			if (s.startsWith("xxeng1")) {
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 140000F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 85000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
						}
					} else if (World.Rnd().nextFloat() < 0.01F) this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
					else {
						this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.002F);
						Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
					}
					this.getEnergyPastArmor(12F, shot);
				}
				if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.75F) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
						Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 48000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
						}
					}
					this.getEnergyPastArmor(25F, shot);
				}
				if (s.startsWith("xxeng1mag")) {
					int i = s.charAt(9) - 49;
					this.debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
					this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
				}
				if (s.endsWith("oil1")) {
					if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
					this.FM.AS.hitOil(shot.initiator, 0);
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
			if (s.startsWith("xxoil")) {
				if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.FM.AS.hitOil(shot.initiator, 0);
					this.getEnergyPastArmor(0.22F, shot);
					this.debuggunnery("Engine Module: Oil Tank Pierced..");
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				if (s.endsWith("li1") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.endsWith("ri1") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.endsWith("lm1") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.endsWith("rm1") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.endsWith("lo1") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.endsWith("ro1") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if ((s.endsWith("sl1") || s.endsWith("sl2")) && this.chunkDamageVisible("StabL") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
					this.nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
				}
				if ((s.endsWith("sr1") || s.endsWith("sr2")) && this.chunkDamageVisible("StabR") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
					this.nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
				}
				if (s.startsWith("xxspark") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
					this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
				}
				return;
			}
			if (s.startsWith("xxtank")) {
				int j = s.charAt(6) - 48;
				int k = 0;
				switch (j) {
					case 1:
						k = World.Rnd().nextInt(0, 1);
						break;

					case 2:
						k = World.Rnd().nextInt(2, 3);
						break;

					case 3:
						k = World.Rnd().nextInt(1, 2);
						break;
				}
				if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
					this.FM.AS.hitTank(shot.initiator, k, 1);
					if (World.Rnd().nextFloat() < 0.05F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F) this.FM.AS.hitTank(shot.initiator, k, 2);
				}
				return;
			} else return;
		}
		if (s.startsWith("xcf")) this.hitChunk("CF", shot);
		else if (!s.startsWith("xblister")) if (s.startsWith("xeng")) this.hitChunk("Engine1", shot);
		else if (s.startsWith("xtail")) this.hitChunk("Tail1", shot);
		else if (s.startsWith("xkeel")) {
			if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
		} else if (s.startsWith("xrudder")) {
			if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
			if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
			if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
		} else if (s.startsWith("xwing")) {
			if (s.startsWith("xWingLIn") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
			if (s.startsWith("xWingRIn") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
			if (s.startsWith("xWingLMid")) {
				if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
				if (World.Rnd().nextFloat() < shot.mass + 0.02F) this.FM.AS.hitOil(shot.initiator, 0);
			}
			if (s.startsWith("xWingRMid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
			if (s.startsWith("xWingLOut") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
			if (s.startsWith("xWingROut") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int l;
			if (s.endsWith("a")) {
				byte0 = 1;
				l = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				l = s.charAt(6) - 49;
			} else l = s.charAt(5) - 49;
			this.hitFlesh(l, shot, byte0);
		}
	}

	protected void moveWingFold(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, 150F * f, 0.0F);
		hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, -150F * f, 0.0F);
	}

	public void moveWingFold(float f) {
		this.moveWingFold(this.hierMesh(), f);
	}

	protected void moveAirBrake(float f) {
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * Math.max(f, this.FM.CT.getFlap()), 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * Math.max(f, this.FM.CT.getFlap()), 0.0F);
		this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -45F * Math.max(f, this.FM.CT.getFlap()), 0.0F);
		this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, -45F * Math.max(f, this.FM.CT.getFlap()), 0.0F);
	}

	protected void moveFlap(float f) {
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * Math.max(f, this.FM.CT.getAirBrake()), 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * Math.max(f, this.FM.CT.getAirBrake()), 0.0F);
		this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -45F * Math.max(f, this.FM.CT.getAirBrake()), 0.0F);
		this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, -45F * Math.max(f, this.FM.CT.getAirBrake()), 0.0F);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL21_D0", 0.0F, 94F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -55F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR21_D0", 0.0F, -94F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -55F * f, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
	}

	public void moveArrestorHook(float f) {
		this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 53F * f, 0.0F);
	}

	public void moveCockpitDoor(float f) {
		this.resetYPRmodifier();
		Aircraft.xyz[2] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
		this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
			this.setDoorSnd(f);
		}
	}

	public void update(float f) {
		super.update(f);
		this.onAircraftLoaded();
		if (this.FM.isPlayers()) if (!Main3D.cur3D().isViewOutside()) this.hierMesh().chunkVisible("Mast", false);
		else this.hierMesh().chunkVisible("Mast", true);
		if (this.FM.isPlayers() && !Main3D.cur3D().isViewOutside()) this.hierMesh().chunkVisible("Mast", false);
		super.update(f);
		if (this.FM.Gears.arrestorVAngle != 0.0F) {
			float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -44F, 9F, 1.0F, 0.0F);
			this.arrestor = 0.8F * this.arrestor + 0.2F * f1;
		} else {
			float f2 = -28F * this.FM.Gears.arrestorVSink / 53F;
			if (f2 < 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
			if (f2 > 0.15F) f2 = 0.15F;
			this.arrestor = 0.3F * this.arrestor + 0.7F * (this.arrestor + f2);
			if (this.arrestor < 0.0F) this.arrestor = 0.0F;
		}
		if (this.arrestor > this.FM.CT.getArrestor()) this.arrestor = this.FM.CT.getArrestor();
		this.moveArrestorHook(this.arrestor);
		float f3 = this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(this.flapps - f3) > 0.02F) {
			this.flapps = f3;
			for (int i = 1; i < 11; i++)
				this.hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, 22F * f3, 0.0F);

		}
	}

	private float arrestor;
	private float flapps;

	static {
		Class class1 = SB2Uxyz.class;
		Property.set(class1, "originCountry", PaintScheme.countryUSA);
	}
}
