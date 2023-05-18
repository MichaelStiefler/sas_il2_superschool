package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class R_5_SKIS extends R_5xyz implements TypeBomber {

	public R_5_SKIS() {
		this.skiAngleL = 0.0F;
		this.skiAngleR = 0.0F;
		this.spring = 0.15F;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.CT.bHasBrakeControl = false;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 9:
				this.hierMesh().chunkVisible("SkiL2_D0", false);
				break;

			case 10:
				this.hierMesh().chunkVisible("SkiR2_D0", false);
				break;
		}
		return super.cutFM(i, j, actor);
	}

	protected void moveFan(float f) {
		if (Config.isUSE_RENDER()) {
			boolean flag = false;
			float f1 = Aircraft.cvt(this.FM.getSpeed(), 20F, 50F, 1.0F, 0.0F);
			float f2 = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 20F, 0.0F, 0.5F);
			if (this.FM.Gears.gWheelSinking[0] > 0.0F) {
				flag = true;
				this.skiAngleL = 0.5F * this.skiAngleL + 0.5F * this.FM.Or.getTangage();
				if (this.skiAngleL > 20F) { this.skiAngleL = this.skiAngleL - this.spring; }
				this.hierMesh().chunkSetAngles("SkiL0_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleL, World.Rnd().nextFloat(f2, f2));
			} else {
				if (this.skiAngleL > f1 * -10F + 0.01D) {
					this.skiAngleL = this.skiAngleL - this.spring;
					flag = true;
				} else if (this.skiAngleL < f1 * -10F - 0.01D) {
					this.skiAngleL = this.skiAngleL + this.spring;
					flag = true;
				}
				this.hierMesh().chunkSetAngles("SkiL0_D0", 0.0F, -this.skiAngleL, 0.0F);
			}
			if (this.FM.Gears.gWheelSinking[1] > 0.0F) {
				flag = true;
				this.skiAngleR = 0.5F * this.skiAngleR + 0.5F * this.FM.Or.getTangage();
				if (this.skiAngleR > 20F) { this.skiAngleR = this.skiAngleR - this.spring; }
				this.hierMesh().chunkSetAngles("SkiR0_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleR, World.Rnd().nextFloat(f2, f2));
				if (this.FM.Gears.gWheelSinking[0] == 0.0F && this.FM.Or.getRoll() < 365F && this.FM.Or.getRoll() > 355F) {
					this.skiAngleL = this.skiAngleR;
					this.hierMesh().chunkSetAngles("SkiL0_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleL, World.Rnd().nextFloat(f2, f2));
				}
			} else {
				if (this.skiAngleR > f1 * -10F + 0.01D) {
					this.skiAngleR = this.skiAngleR - this.spring;
					flag = true;
				} else if (this.skiAngleR < f1 * -10F - 0.01D) {
					this.skiAngleR = this.skiAngleR + this.spring;
					flag = true;
				}
				this.hierMesh().chunkSetAngles("SkiR0_D0", 0.0F, -this.skiAngleR, 0.0F);
			}
			if (!flag && f1 == 0.0F) {
				super.moveFan(f);
				return;
			}
			this.hierMesh().chunkSetAngles("SkiC_D0", 0.0F, (this.skiAngleL + this.skiAngleR) / 2.0F, 0.0F);
			float f3 = this.skiAngleL / 20F;
			this.hierMesh().chunkSetAngles("SkiL1_D0", 0.0F, f3 * -2F + 0.25F * this.suspL, f3 * 8.25F + 3F * this.suspL);
			this.hierMesh().chunkSetAngles("SkiL2_D0", 0.0F, f3 * -7F + 1.25F * this.suspL, f3 * -6.25F + 2.75F * this.suspL);
			if (this.skiAngleL < 0.0F) {
				this.hierMesh().chunkSetAngles("SkiL3_D0", 0.0F, 0.0F, f3 * 15F);
				this.hierMesh().chunkSetAngles("SkiL4_D0", 0.0F, 0.0F, 0.0F);
				this.hierMesh().chunkSetAngles("SkiL5_D0", 0.0F, 0.0F, 0.0F);
				this.hierMesh().chunkSetAngles("SkiL6_D0", 0.0F, 0.0F, 0.0F);
				this.hierMesh().chunkSetAngles("SkiL7_D0", 0.0F, 0.0F, 0.0F);
				this.hierMesh().chunkSetAngles("SkiL8_D0", 0.0F, 0.0F, 0.0F);
			} else {
				this.hierMesh().chunkSetAngles("SkiL3_D0", 0.0F, f3 * 40F, f3 * 70F);
				float f4 = f3 * -5F;
				float f6 = f3 * 10F;
				float f8 = f3 * 15F;
				float f10 = f3 * 20F;
				this.hierMesh().chunkSetAngles("SkiL4_D0", 0.0F, -f4, -f10);
				this.hierMesh().chunkSetAngles("SkiL5_D0", 0.0F, -f6, -f10);
				this.hierMesh().chunkSetAngles("SkiL6_D0", 0.0F, -f8, -f10);
				this.hierMesh().chunkSetAngles("SkiL7_D0", 0.0F, -f6, -f10);
				this.hierMesh().chunkSetAngles("SkiL8_D0", 0.0F, -f6, -f10);
			}
			f3 = this.skiAngleR / 20F;
			this.hierMesh().chunkSetAngles("SkiR1_D0", 0.0F, f3 * 2.0F - 0.25F * this.suspR, f3 * 8.25F + 3F * this.suspR);
			this.hierMesh().chunkSetAngles("SkiR2_D0", 0.0F, f3 * -7F + 1.25F * this.suspR, f3 * -6.25F + 2.75F * this.suspR);
			if (this.skiAngleR < 0.0F) {
				this.hierMesh().chunkSetAngles("SkiR3_D0", 0.0F, 0.0F, f3 * 15F);
				this.hierMesh().chunkSetAngles("SkiR4_D0", 0.0F, 0.0F, 0.0F);
				this.hierMesh().chunkSetAngles("SkiR5_D0", 0.0F, 0.0F, 0.0F);
				this.hierMesh().chunkSetAngles("SkiR6_D0", 0.0F, 0.0F, 0.0F);
				this.hierMesh().chunkSetAngles("SkiR7_D0", 0.0F, 0.0F, 0.0F);
				this.hierMesh().chunkSetAngles("SkiR8_D0", 0.0F, 0.0F, 0.0F);
			} else {
				this.hierMesh().chunkSetAngles("SkiR3_D0", 0.0F, f3 * 40F, f3 * 70F);
				float f5 = f3 * -5F;
				float f7 = f3 * 10F;
				float f9 = f3 * 15F;
				float f11 = f3 * 20F;
				this.hierMesh().chunkSetAngles("SkiR4_D0", 0.0F, -f7, -f11);
				this.hierMesh().chunkSetAngles("SkiR5_D0", 0.0F, -f7, -f11);
				this.hierMesh().chunkSetAngles("SkiR6_D0", 0.0F, -f5, -f11);
				this.hierMesh().chunkSetAngles("SkiR7_D0", 0.0F, -f9, -f11);
				this.hierMesh().chunkSetAngles("SkiR8_D0", 0.0F, -f7, -f11);
			}
		}
		super.moveFan(f);
	}

	private float skiAngleL;
	private float skiAngleR;
	private float spring;

	static {
		Class class1 = R_5_SKIS.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "R-5");
		Property.set(class1, "meshName", "3do/plane/R-5/hier_skis.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFCSPar08());
		Property.set(class1, "yearService", 1931F);
		Property.set(class1, "yearExpired", 1944F);
		Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE6.class });
		Property.set(class1, "FlightModel", "FlightModels/R-5.fmd");
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 10, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07",
						"_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19",
						"_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05" });
	}
}
