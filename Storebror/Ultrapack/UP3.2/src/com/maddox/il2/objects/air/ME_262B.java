package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public class ME_262B extends Scheme2 implements TypeFighter, TypeBNZFighter {

	public ME_262B() {
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				if (!this.FM.AS.bIsAboutToBailout) {
					if (this.hierMesh().isChunkVisible("Blister1_D0")) { this.hierMesh().chunkVisible("Gore1_D0", true); }
					this.hierMesh().chunkVisible("Gore2_D0", true);
				}
				// fall through

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Head2_D0", false);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				if (!this.FM.AS.bIsAboutToBailout) {
					if (this.hierMesh().isChunkVisible("Blister2_D0")) { this.hierMesh().chunkVisible("Gore1_D0", true); }
					this.hierMesh().chunkVisible("Gore2_D0", true);
				}
				// fall through

			default:
				return;
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 111F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC21_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 73F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 73F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 88F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 88F * f, 0.0F);
		float f1 = Math.max(-f * 1500F, -90F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f1, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		float f = this.FM.Gears.gWheelSinking[2];
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.19F, 0.0F, 0.19F);
		this.hierMesh().chunkSetLocate("GearC22_D0", Aircraft.xyz, Aircraft.ypr);
		f = Aircraft.cvt(f, 0.0F, 19F, 0.0F, 30F);
		this.hierMesh().chunkSetAngles("GearC7_D0", 0.0F, f, 0.0F);
		this.hierMesh().chunkSetAngles("GearC8_D0", 0.0F, 2.0F * f, 0.0F);
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
		if (this.FM.CT.getGear() > 0.75F) { this.hierMesh().chunkSetAngles("GearC21_D0", 0.0F, 40F * f, 0.0F); }
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xcf")) {
			if (this.chunkDamageVisible("CF") < 3) { this.hitChunk("CF", shot); }
			if (point3d.x > 1.7D) {
				if (World.Rnd().nextFloat() < 0.07F) { this.FM.AS.setJamBullets(0, 0); }
				if (World.Rnd().nextFloat() < 0.07F) { this.FM.AS.setJamBullets(0, 1); }
				if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.setJamBullets(1, 0); }
				if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.setJamBullets(1, 1); }
			}
			if (point3d.x > -0.999D && point3d.x < 0.535D && point3d.z > -0.224D) {
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10); }
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20); }
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4); }
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8); }
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40); }
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1); }
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2); }
			}
			if (point3d.x > 0.8D && point3d.x < 1.58D && World.Rnd().nextFloat() < 0.25F && (shot.powerType == 3 && this.getEnergyPastArmor(0.4F, shot) > 0.0F || shot.powerType == 0)) {
				this.FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, (int) (shot.power / 4000F)));
			}
			if (point3d.x > -2.485D && point3d.x < -1.6D && World.Rnd().nextFloat() < 0.25F && (shot.powerType == 3 && this.getEnergyPastArmor(0.4F, shot) > 0.0F || shot.powerType == 0)) {
				this.FM.AS.hitTank(shot.initiator, 1, World.Rnd().nextInt(1, (int) (shot.power / 4000F)));
			}
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		} else if (s.startsWith("xkeel")) {
			this.hitChunk("Keel1", shot);
		} else if (s.startsWith("xstabl")) {
			this.hitChunk("StabL", shot);
		} else if (s.startsWith("xstabr")) {
			this.hitChunk("StabR", shot);
		} else if (s.startsWith("xwing")) {
			if (s.endsWith("lin") && this.chunkDamageVisible("WingLIn") < 3) { this.hitChunk("WingLIn", shot); }
			if (s.endsWith("rin") && this.chunkDamageVisible("WingRIn") < 3) { this.hitChunk("WingRIn", shot); }
			if (s.endsWith("lmid") && this.chunkDamageVisible("WingLMid") < 3) { this.hitChunk("WingLMid", shot); }
			if (s.endsWith("rmid") && this.chunkDamageVisible("WingRMid") < 3) { this.hitChunk("WingRMid", shot); }
			if (s.endsWith("lout") && this.chunkDamageVisible("WingLOut") < 3) { this.hitChunk("WingLOut", shot); }
			if (s.endsWith("rout") && this.chunkDamageVisible("WingROut") < 3) { this.hitChunk("WingROut", shot); }
		} else if (s.startsWith("xengine")) {
			int i = s.charAt(7) - 49;
			if (point3d.x > 0.0D && point3d.x < 0.697D) { this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6)); }
			if (World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass) { this.FM.AS.hitEngine(shot.initiator, i, 5); }
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int j;
			if (s.endsWith("a")) {
				byte0 = 1;
				j = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				j = s.charAt(6) - 49;
			} else {
				j = s.charAt(5) - 49;
			}
			this.hitFlesh(j, shot, byte0);
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 33:
				return super.cutFM(34, j, actor);

			case 36:
				return super.cutFM(37, j, actor);

			case 11:
				this.cutFM(17, j, actor);
				this.FM.cut(17, j, actor);
				this.cutFM(18, j, actor);
				this.FM.cut(18, j, actor);
				return super.cutFM(i, j, actor);
		}
		return super.cutFM(i, j, actor);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag && World.Rnd().nextFloat() < 0.2F) {
			if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.12F) {
				this.FM.AS.explodeEngine(this, 0);
				this.msgCollision(this, "WingLIn_D0", "WingLIn_D0");
				if (World.Rnd().nextBoolean()) {
					this.FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
				} else {
					this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
				}
			}
			if (this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.12F) {
				this.FM.AS.explodeEngine(this, 1);
				this.msgCollision(this, "WingRIn_D0", "WingRIn_D0");
				if (World.Rnd().nextBoolean()) {
					this.FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
				} else {
					this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
				}
			}
		}
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
		}
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask2_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Head2_D0"));
		}
	}

	public void update(float f) {
		if (this.FM.getSpeed() > 5F) {
			this.hierMesh().chunkSetAngles("SlatL0_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
			this.hierMesh().chunkSetAngles("SlatL1_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
			this.hierMesh().chunkSetAngles("SlatR0_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
			this.hierMesh().chunkSetAngles("SlatR1_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
			this.hierMesh().chunkSetAngles("SlatL2_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
			this.hierMesh().chunkSetAngles("SlatR2_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
		}
		if (this.FM.AS.isMaster()) {
			for (int i = 0; i < 2; i++) {
				if (this.curctl[i] == -1F) {
					this.curctl[i] = this.oldctl[i] = this.FM.EI.engines[i].getControlThrottle();
				} else {
					this.curctl[i] = this.FM.EI.engines[i].getControlThrottle();
					if ((this.curctl[i] - this.oldctl[i]) / f > 3F && this.FM.EI.engines[i].getRPM() < 2400F && this.FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.25F) { this.FM.AS.hitEngine(this, i, 100); }
					if ((this.curctl[i] - this.oldctl[i]) / f < -3F && this.FM.EI.engines[i].getRPM() < 2400F && this.FM.EI.engines[i].getStage() == 6) {
						if (World.Rnd().nextFloat() < 0.25F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) { this.FM.EI.engines[i].setEngineStops(this); }
						if (World.Rnd().nextFloat() < 0.75F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) { this.FM.EI.engines[i].setKillCompressor(this); }
					}
					this.oldctl[i] = this.curctl[i];
				}
			}

			if (Config.isUSE_RENDER()) {
				if (this.FM.EI.engines[0].getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6) {
					if (this.FM.EI.engines[0].getPowerOutput() > 0.95F) {
						this.FM.AS.setSootState(this, 0, 3);
					} else {
						this.FM.AS.setSootState(this, 0, 2);
					}
				} else {
					this.FM.AS.setSootState(this, 0, 0);
				}
				if (this.FM.EI.engines[1].getPowerOutput() > 0.8F && this.FM.EI.engines[1].getStage() == 6) {
					if (this.FM.EI.engines[1].getPowerOutput() > 0.95F) {
						this.FM.AS.setSootState(this, 1, 3);
					} else {
						this.FM.AS.setSootState(this, 1, 2);
					}
				} else {
					this.FM.AS.setSootState(this, 1, 0);
				}
			}
		}
		super.update(f);
	}

	private float oldctl[] = { -1F, -1F };
	private float curctl[] = { -1F, -1F };

	static {
		Class class1 = ME_262B.class;
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
	}
}
