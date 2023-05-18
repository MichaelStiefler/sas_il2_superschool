package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class JU_88 extends Scheme2 {

	public JU_88() {
		this.suspR = 0.0F;
		this.suspL = 0.0F;
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		float f1 = Math.max(-f * 1600F, -80F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, -90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
		if (f1 > -2.5F) { f1 = 0.0F; }
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -f1, 0.0F);
		f1 = f >= 0.5F ? Math.abs(Math.min(1.0F - f, 0.1F)) : Math.abs(Math.min(f, 0.1F));
		if (f1 < 0.002F) { f1 = 0.0F; }
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -450F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 450F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 1200F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -1200F * f1, 0.0F);
		f1 = cvt(f, 0.0F, 0.5F, 0.0F, 0.1F);
		if (f1 < 0.002F) { f1 = 0.0F; }
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 900F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -900F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -900F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 900F * f1, 0.0F);
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
		if (this.suspL > 0.035F) { this.suspL = 0.035F; }
		if (this.suspR > 0.035F) { this.suspR = 0.035F; }
		if (this.suspL < 0.0F) { this.suspL = 0.0F; }
		if (this.suspR < 0.0F) { this.suspR = 0.0F; }
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
		float f1 = -70F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public void update(float f) {
		for (int i = 1; i < 11; i++) {
			this.hierMesh().chunkSetAngles("Radl" + i + "_D0", -30F * this.FM.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
			this.hierMesh().chunkSetAngles("Radr" + i + "_D0", -30F * this.FM.EI.engines[1].getControlRadiator(), 0.0F, 0.0F);
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
				f = 0.0F;
				f1 = 0.0F;
				flag = false;
				break;

			case 2:
				if (f < -45F) {
					f = -45F;
					flag = false;
				}
				if (f > 25F) {
					f = 25F;
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
				if (f < -2F) {
					if (f1 < cvt(f, -6.8F, -2F, -10F, -2.99F)) { f1 = cvt(f, -6.8F, -2F, -10F, -2.99F); }
					break;
				}
				if (f < 0.5F) {
					if (f1 < cvt(f, -2F, 0.5F, -2.99F, -2.3F)) { f1 = cvt(f, -2F, 0.5F, -2.99F, -2.3F); }
					break;
				}
				if (f < 5.3F) {
					if (f1 < cvt(f, 0.5F, 5.3F, -2.3F, -2.3F)) { f1 = cvt(f, 0.5F, 5.3F, -2.3F, -2.3F); }
					break;
				}
				if (f1 < cvt(f, 5.3F, 25F, -2.3F, -7.2F)) { f1 = cvt(f, 5.3F, 25F, -2.3F, -7.2F); }
				break;

			case 3:
				if (f < -35F) {
					f = -35F;
					flag = false;
				}
				if (f > 35F) {
					f = 35F;
					flag = false;
				}
				if (f1 < -35F) {
					f1 = -35F;
					flag = false;
				}
				if (f1 > -0.48F) {
					f1 = -0.48F;
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

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag && World.Rnd().nextFloat() < 0.2F) {
			if (this.FM.AS.astateEngineStates[0] > 3) {
				if (World.Rnd().nextFloat() < 0.25F) { this.FM.AS.hitTank(this, 0, 3); }
				if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.hitTank(this, 1, 3); }
			}
			if (this.FM.AS.astateEngineStates[1] > 3) {
				if (World.Rnd().nextFloat() < 0.12F) { this.FM.AS.hitTank(this, 2, 3); }
				if (World.Rnd().nextFloat() < 0.25F) { this.FM.AS.hitTank(this, 3, 3); }
			}
			if (this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.11F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this); }
			if (this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.11F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this); }
			if (this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.11F) { this.FM.AS.hitTank(this, 2, 3); }
			if (this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.11F) { this.FM.AS.hitTank(this, 1, 3); }
			if (this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.11F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this); }
			if (this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.11F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this); }
		}
		if (!(this instanceof JU_88MSTL)) {
			for (int i = 1; i < 4; i++) {
				if (this.FM.getAltitude() < 3000F) {
					this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
				} else {
					this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
				}
			}

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

	public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
	}

	public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
	}

	float suspR;
	float suspL;

	static {
		Class class1 = JU_88.class;
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
	}
}
