package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class JU_88Axx extends Scheme2 {

	public JU_88Axx() {
		this.suspR = 0.0F;
		this.suspL = 0.0F;
		this.mainRearGunActive = true;
		this.secondaryRearGunActive = false;
		bNavigatorUseBombsight = false;
	}

	public float getEyeLevelCorrection() {
		return 0.1F;
	}

	private void doWreck(String s) {
		if (this.hierMesh().chunkFindCheck(s) != -1) {
			this.hierMesh().hideSubTrees(s);
			Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind(s));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(this.FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		this.hierMesh().chunkVisible("fakeNose_D3", this.hierMesh().isChunkVisible("Nose_D3"));
		this.hierMesh().chunkVisible("fakeNose_D2", this.hierMesh().isChunkVisible("Nose_D2"));
		this.hierMesh().chunkVisible("fakeNose_D1", this.hierMesh().isChunkVisible("Nose_D1"));
		this.hierMesh().chunkVisible("fakeNose_D0", this.hierMesh().isChunkVisible("Nose_D0"));
		if (this.FM.isPlayers()) bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		this.hierMesh().chunkVisible("fakeNose_D3", this.hierMesh().isChunkVisible("Nose_D3"));
		this.hierMesh().chunkVisible("fakeNose_D2", this.hierMesh().isChunkVisible("Nose_D2"));
		this.hierMesh().chunkVisible("fakeNose_D1", this.hierMesh().isChunkVisible("Nose_D1"));
		this.hierMesh().chunkVisible("fakeNose_D0", this.hierMesh().isChunkVisible("Nose_D0"));
		if (this.FM.isPlayers()) bChangedPit = true;
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		float f3 = Math.max(-f * 1600F, -80F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, -90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
		if (f3 > -2.5F) f3 = 0.0F;
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f3, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -f3, 0.0F);
		float f4 = f >= 0.5F ? Math.abs(Math.min(1.0F - f, 0.1F)) : Math.abs(Math.min(f, 0.1F));
		if (f4 < 0.002F) f4 = 0.0F;
		float f5 = f >= 0.5F ? Math.abs(Math.min(1.0F - f, 0.1F)) : Math.abs(Math.min(f, 0.1F));
		if (f5 < 0.002F) f5 = 0.0F;
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -450F * f4, 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 450F * f5, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 1200F * f4, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -1200F * f5, 0.0F);
		f4 = Aircraft.cvt(f, 0.0F, 0.5F, 0.0F, 0.1F);
		if (f4 < 0.002F) f4 = 0.0F;
		f5 = Aircraft.cvt(f, 0.0F, 0.5F, 0.0F, 0.1F);
		if (f5 < 0.002F) f5 = 0.0F;
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 900F * f4, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -900F * f5, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -900F * f4, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 900F * f5, 0.0F);
		hiermesh.chunkSetAngles("GearR8_D0", 0.0F, 0.0F, 93F * f);
		hiermesh.chunkSetAngles("GearL8_D0", 0.0F, 0.0F, 93F * f);
		hiermesh.chunkSetAngles("GearR3_D0", 85F * f, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", -85F * f, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearR9_D0", 0.0F, 0.0F, -116F * f);
		hiermesh.chunkSetAngles("GearL9_D0", 0.0F, 0.0F, -116F * f);
		hiermesh.chunkSetAngles("GearR10_D0", 0.0F, 0.0F, 126F * f);
		hiermesh.chunkSetAngles("GearL10_D0", 0.0F, 0.0F, 126F * f);
	}

	public void moveWheelSink() {
		this.suspL = 0.9F * this.suspL + 0.1F * this.FM.Gears.gWheelSinking[0];
		this.suspR = 0.9F * this.suspR + 0.1F * this.FM.Gears.gWheelSinking[1];
		if (this.suspL > 0.035F) this.suspL = 0.035F;
		if (this.suspR > 0.035F) this.suspR = 0.035F;
		if (this.suspL < 0.0F) this.suspL = 0.0F;
		if (this.suspR < 0.0F) this.suspR = 0.0F;
		Aircraft.xyz[0] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.xyz[1] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.xyz[2] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		float f = 588F;
		Aircraft.xyz[2] = this.suspL * 6F;
		this.hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearL11_D0", 0.0F, 0.0F, this.suspL * f);
		this.hierMesh().chunkSetAngles("GearL12_D0", 0.0F, 0.0F, -this.suspL * f);
		Aircraft.xyz[2] = this.suspR * 6F;
		this.hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
		this.hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, this.suspR * f);
		this.hierMesh().chunkSetAngles("GearR12_D0", 0.0F, 0.0F, -this.suspR * f);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	protected void moveFlap(float f) {
		float f1 = -60F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public void update(float f) {
		float f1 = this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(this.flapps[0] - f1) > 0.01F) {
			this.flapps[0] = f1;
			for (int i = 1; i < 11; i++) {
				String s = "Radl" + i + "_D0";
				this.hierMesh().chunkSetAngles(s, -30F * f1, 0.0F, 0.0F);
			}

		}
		f1 = this.FM.EI.engines[1].getControlRadiator();
		if (Math.abs(this.flapps[1] - f1) > 0.01F) {
			this.flapps[1] = f1;
			for (int j = 1; j < 11; j++) {
				String s1 = "Radr" + j + "_D0";
				this.hierMesh().chunkSetAngles(s1, -30F * f1, 0.0F, 0.0F);
			}

		}
		super.update(f);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (bNavigatorUseBombsight) {
					flag = false;
					f = 0.0F;
					f1 = 0.0F;
					break;
				}
				if (f < -30F) {
					f = -30F;
					flag = false;
				}
				if (f > 35F) {
					f = 35F;
					flag = false;
				}
				if (f1 < -10F) {
					f1 = -10F;
					flag = false;
				}
				if (f1 > 35F) {
					f1 = 35F;
					flag = false;
				}
				break;

			case 1:
				if (!this.FM.turret[2].bIsAIControlled || this.secondaryRearGunActive) {
					flag = false;
					f = 0.0F;
					f1 = 0.0F;
					break;
				}
				this.mainRearGunActive = true;
				if (f < -25F) {
					f = -25F;
					flag = false;
					this.mainRearGunActive = false;
				}
				if (f > 45F) {
					f = 45F;
					flag = false;
				}
				if (f1 < -10F) {
					f1 = -10F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				if (f > 3F) {
					if (f1 < Aircraft.cvt(f, 3F, 8.4F, -1.72F, -10F)) f1 = Aircraft.cvt(f, 3F, 8.4F, -1.72F, -10F);
					break;
				}
				if (f > -5.3F) {
					if (f1 < Aircraft.cvt(f, -5.3F, 3F, -1.72F, -1.72F)) f1 = Aircraft.cvt(f, -5.3F, 3F, -1.72F, -1.72F);
					break;
				}
				if (f1 < Aircraft.cvt(f, -25F, -5.3F, -3F, -1.72F)) f1 = Aircraft.cvt(f, -25F, -5.3F, -3F, -1.72F);
				break;

			case 2:
				if (!this.FM.turret[1].bIsAIControlled || this.mainRearGunActive) {
					this.secondaryRearGunActive = false;
					flag = false;
					f = 0.0F;
					f1 = 0.0F;
					break;
				}
				this.secondaryRearGunActive = true;
				if (f < -45F) {
					f = -45F;
					flag = false;
				}
				if (f > 25F) {
					f = 25F;
					flag = false;
					this.secondaryRearGunActive = false;
				}
				if (f1 < -10F) {
					f1 = -10F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				if (f < -3F) {
					if (f1 < Aircraft.cvt(f, -8.9F, -3F, -10F, -1.72F)) f1 = Aircraft.cvt(f, -8.9F, -3F, -10F, -1.72F);
					break;
				}
				if (f < 5.3F) {
					if (f1 < Aircraft.cvt(f, -3F, 5.3F, -1.72F, -1.72F)) f1 = Aircraft.cvt(f, -3F, 5.3F, -1.72F, -1.72F);
					break;
				}
				if (f1 < Aircraft.cvt(f, 5.3F, 25F, -1.72F, -3F)) f1 = Aircraft.cvt(f, 5.3F, 25F, -1.72F, -3F);
				break;

			case 3:
				if (f < -43F) {
					f = -43F;
					flag = false;
				}
				if (f > 43F) {
					f = 43F;
					flag = false;
				}
				if (f1 < -40F) {
					f1 = -40F;
					flag = false;
				}
				if (f1 > 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 13:
				return false;

			case 33:
				return super.cutFM(34, j, actor);

			case 36:
				return super.cutFM(37, j, actor);

			case 3:
				this.FM.AS.hitEngine(this, 0, 99);
				break;

			case 4:
				this.FM.AS.hitEngine(this, 1, 99);
				break;

			case 19:
				this.FM.Gears.hitCentreGear();
				this.hierMesh().chunkVisible("Wire_D0", false);
				break;

			case 37:
				this.FM.Gears.hitRightGear();
				break;

			case 34:
				this.FM.Gears.hitLeftGear();
				break;

			case 10:
				this.doWreck("GearR8_D0");
				this.FM.Gears.hitRightGear();
				break;

			case 9:
				this.doWreck("GearL8_D0");
				this.FM.Gears.hitLeftGear();
				break;
		}
		return super.cutFM(i, j, actor);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag) {
			if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.39F) this.FM.AS.hitTank(this, 0, 1);
			if (this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.39F) this.FM.AS.hitTank(this, 1, 1);
			if (this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.1F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
			if (this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.1F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
			if (this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.1F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
			if (this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.1F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) if (s.endsWith("p1")) {
				if (Aircraft.v1.z > 0.5D) this.getEnergyPastArmor(5D / Aircraft.v1.z, shot);
				else if (Aircraft.v1.x > 0.93969261646270752D) this.getEnergyPastArmor(10D / Aircraft.v1.x * World.Rnd().nextFloat(1.0F, 1.2F), shot);
			} else if (s.endsWith("p2")) this.getEnergyPastArmor(5D / Math.abs(Aircraft.v1.z), shot);
			else if (s.endsWith("p5")) this.getEnergyPastArmor(5D / Math.abs(Aircraft.v1.z), shot);
			else if (s.endsWith("p3")) this.getEnergyPastArmor(8D / Math.abs(Aircraft.v1.x) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
			else if (s.endsWith("p4")) {
				if (Aircraft.v1.x > 0.70710676908493042D) this.getEnergyPastArmor(8D / Aircraft.v1.x * World.Rnd().nextFloat(1.0F, 1.2F), shot);
				else if (Aircraft.v1.x > -0.70710676908493042D) this.getEnergyPastArmor(6F, shot);
			} else if (s.endsWith("o1") || s.endsWith("o2")) if (Aircraft.v1.x > 0.70710676908493042D) this.getEnergyPastArmor(8D / Aircraft.v1.x * World.Rnd().nextFloat(1.0F, 1.2F), shot);
			else this.getEnergyPastArmor(5F, shot);
			if (s.startsWith("xxcontrols")) {
				int i = s.charAt(10) - 48;
				switch (i) {
					default:
						break;

					case 1:
					case 2:
						if (this.getEnergyPastArmor(1.0F, shot) <= 0.0F) break;
						if (World.Rnd().nextFloat() < 0.08F) this.FM.AS.setControlsDamage(shot.initiator, 1);
						if (World.Rnd().nextFloat() < 0.08F) this.FM.AS.setControlsDamage(shot.initiator, 2);
						break;

					case 3:
					case 4:
						if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F) this.FM.AS.setControlsDamage(shot.initiator, 0);
						break;

					case 5:
						if (this.getEnergyPastArmor(0.1F, shot) <= 0.0F) break;
						if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
						if (World.Rnd().nextFloat() < 0.15F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
						break;

					case 6:
						if (this.getEnergyPastArmor(0.1F, shot) <= 0.0F) break;
						if (World.Rnd().nextFloat() < 0.15F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
						if (World.Rnd().nextFloat() < 0.15F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
						break;
				}
			}
			if (s.startsWith("xxcannon1")) {
				this.debuggunnery("MGFF: Disabled..");
				this.FM.AS.setJamBullets(1, 0);
				this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
			}
			if (s.startsWith("xxspar")) {
				this.getEnergyPastArmor(1.0F, shot);
				if ((s.endsWith("cf1") || s.endsWith("cf2")) && World.Rnd().nextFloat() < 0.1F && this.chunkDamageVisible("CF") > 2
						&& this.getEnergyPastArmor(15.9F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					this.msgCollision(this, "Tail1_D0", "Tail1_D0");
					this.msgCollision(this, "WingLIn_D0", "WingLIn_D0");
					this.msgCollision(this, "WingRIn_D0", "WingRIn_D0");
				}
				if ((s.endsWith("ta1") || s.endsWith("ta2")) && World.Rnd().nextFloat() < 0.1F && this.chunkDamageVisible("Tail1") > 2
						&& this.getEnergyPastArmor(15.9F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
					this.msgCollision(this, "Tail1_D0", "Tail1_D0");
				if ((s.endsWith("li1") || s.endsWith("li2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
					this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				if ((s.endsWith("ri1") || s.endsWith("ri2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
					this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				if ((s.endsWith("lm1") || s.endsWith("lm2")) && World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
					this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				if ((s.endsWith("rm1") || s.endsWith("rm2")) && World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
					this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				if ((s.endsWith("lo1") || s.endsWith("lo2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
					this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				if ((s.endsWith("ro1") || s.endsWith("ro2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
					this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				if (s.endsWith("e1") && (point3d.y > 2.79D || point3d.y < 2.32D) && this.getEnergyPastArmor(18F, shot) > 0.0F) this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
				if (s.endsWith("e2") && (point3d.y < -2.79D || point3d.y > -2.32D) && this.getEnergyPastArmor(18F, shot) > 0.0F) this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
			}
			if (s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
				this.FM.AS.hitTank(shot.initiator, 0, 100);
				this.FM.AS.hitTank(shot.initiator, 1, 100);
				this.FM.AS.hitTank(shot.initiator, 2, 100);
				this.FM.AS.hitTank(shot.initiator, 3, 100);
				this.msgCollision(this, "CF_D0", "CF_D0");
			}
			if (s.startsWith("xxprop")) {
				int j = 0;
				if (s.endsWith("2")) j = 1;
				if (this.getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 3);
			}
			if (s.startsWith("xxeng")) {
				int k = 0;
				if (s.startsWith("xxeng2")) k = 1;
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 120000F) this.FM.AS.setEngineStuck(shot.initiator, k);
						if (World.Rnd().nextFloat() < shot.power / 30000F) this.FM.AS.hitEngine(shot.initiator, k, 2);
					}
				} else if (s.endsWith("feed")) {
					if (this.getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F && this.FM.EI.engines[k].getPowerOutput() > 0.7F) {
						this.FM.AS.hitEngine(shot.initiator, k, 100);
						this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Pressurized Fuel Line Pierced, Fuel Flamed..");
					}
				} else if (s.endsWith("mag1") || s.endsWith("mag2")) {
					if (World.Rnd().nextFloat() < 0.5F) {
						int j1 = s.charAt(9) - 49;
						this.FM.EI.engines[k].setMagnetoKnockOut(shot.initiator, j1);
						this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Magneto " + j1 + " Destroyed..");
					}
				} else if (s.endsWith("cyl1") || s.endsWith("cyl2")) {
					if (this.getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[k].getCylindersRatio() * 1.8F) {
						this.FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
						if (this.FM.AS.astateEngineStates[k] < 1) {
							this.FM.AS.hitEngine(shot.initiator, k, 1);
							this.FM.AS.doSetEngineState(shot.initiator, k, 1);
						}
						if (World.Rnd().nextFloat() < shot.power / 960000F) this.FM.AS.hitEngine(shot.initiator, k, 3);
					}
				} else if (s.endsWith("rad") && this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
					this.debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
					this.FM.AS.hitOil(shot.initiator, 0);
				}
			}
			if (s.startsWith("xxoil")) {
				int l = 0;
				if (s.endsWith("2")) l = 1;
				if (this.getEnergyPastArmor(0.18F, shot) > 0.0F) {
					this.FM.AS.hitOil(shot.initiator, l);
					this.getEnergyPastArmor(0.42F, shot);
				}
			}
			if (s.startsWith("xxtank")) {
				int i1 = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) if (shot.power < 14100F) {
					if (this.FM.AS.astateTankStates[i1] < 1) this.FM.AS.hitTank(shot.initiator, i1, 1);
					if (this.FM.AS.astateTankStates[i1] < 4 && World.Rnd().nextFloat() < 0.1F) this.FM.AS.hitTank(shot.initiator, i1, 1);
					if (shot.powerType == 3 && this.FM.AS.astateTankStates[i1] > 2 && World.Rnd().nextFloat() < 0.12F) this.FM.AS.hitTank(shot.initiator, i1, 10);
				} else this.FM.AS.hitTank(shot.initiator, i1, World.Rnd().nextInt(0, (int) (shot.power / 20000F)));
			}
		}
		if (s.startsWith("xoil")) {
			if (s.equals("xoil1")) {
				this.FM.AS.hitOil(shot.initiator, 0);
				s = "xengine1";
			}
			if (s.equals("xoil2")) {
				this.FM.AS.hitOil(shot.initiator, 1);
				s = "xengine2";
			}
		}
		if (s.startsWith("xcf")) {
			if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
		} else if (s.startsWith("xnose")) {
			if (this.chunkDamageVisible("Nose") < 3) this.hitChunk("Nose", shot);
			if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
			if (point3d.x > 4.505000114440918D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
			else if (point3d.y > 0.0D) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
				if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
			} else {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
				if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
			}
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) {
			if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
		} else if (s.startsWith("xrudder")) this.hitChunk("Rudder1", shot);
		else if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
		else if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
		else if (s.startsWith("xvatorl")) this.hitChunk("VatorL", shot);
		else if (s.startsWith("xvatorr")) this.hitChunk("VatorR", shot);
		else if (s.startsWith("xwinglin")) {
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
		} else if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
		else if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
		else if (s.startsWith("xengine1")) {
			if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
		} else if (s.startsWith("xengine2")) {
			if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
		} else if (s.startsWith("xgear")) {
			if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
				this.debuggunnery("Hydro System: Disabled..");
				this.FM.AS.setInternalDamage(shot.initiator, 0);
			}
			if (s.endsWith("2")) {
				if (World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(6.8F, 29.35F), shot) > 0.0F) {
					this.debuggunnery("Undercarriage: Stuck..");
					this.FM.AS.setInternalDamage(shot.initiator, 3);
				}
				char s1 = s.charAt(5);
				this.hitChunk("Gear" + Character.toUpperCase(s1) + "2", shot);
			}
		} else if (s.startsWith("xturret")) {
			if (s.startsWith("xturret1")) this.FM.AS.setJamBullets(10, 0);
			if (s.startsWith("xturret2")) this.FM.AS.setJamBullets(11, 0);
			if (s.startsWith("xturret3")) this.FM.AS.setJamBullets(12, 0);
			if (s.startsWith("xturret4")) this.FM.AS.setJamBullets(13, 0);
			if (s.startsWith("xturret5")) this.FM.AS.setJamBullets(14, 0);
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int k1;
			if (s.endsWith("a")) {
				byte0 = 1;
				k1 = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				k1 = s.charAt(6) - 49;
			} else k1 = s.charAt(5) - 49;
			this.hitFlesh(k1, shot, byte0);
		}
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

	public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
	}

	public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
	}

	public static boolean bChangedPit = false;
	public static boolean bNavigatorUseBombsight;
	float                 suspR;
	float                 suspL;
	boolean               mainRearGunActive;
	boolean               secondaryRearGunActive;
	private float         flapps[]    = { 0.0F, 0.0F };

	static {
		Class class1 = JU_88Axx.class;
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
	}
}
