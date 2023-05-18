package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class P_47BDT extends P_47ModPack {

	protected void moveFan(float f) {
		if (!Config.isUSE_RENDER()) return;

		int i = this.FM.EI.engines[0].getStage();
		if (i > 0 && i < 6) f = 0.005F * i;

		this.hierMesh().chunkFind(Aircraft.Props[1][0]);
		for (int j = 0; j < 2; j++) {
			if (this.oldProp[j] < 2) {
				i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
				if (i >= 1) i = 1;
				if (i != this.oldProp[j] && this.hierMesh().isChunkVisible(Aircraft.Props[j][this.oldProp[j]])) {
					this.hierMesh().chunkVisible(Aircraft.Props[j][this.oldProp[j]], false);
					this.oldProp[j] = i;
					this.hierMesh().chunkVisible(Aircraft.Props[j][i], true);
				}
			}
			if (i == 0) this.propPos[j] = (this.propPos[j] + 57.3F * this.FM.EI.engines[0].getw() * f) % 360F;
			else {
				float f1 = 57.3F * this.FM.EI.engines[0].getw();
				f1 %= 2880F;
				f1 /= 2880F;
				if (f1 <= 0.5F) f1 *= 2.0F;
				else f1 = f1 * 2.0F - 2.0F;
				f1 *= 1200F;
				this.propPos[j] = (this.propPos[j] + f1 * f) % 360F;
			}
			if (j == 0) this.hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, this.propPos[j], 0.0F);
			else this.hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, -this.propPos[j], 0.0F);
		}
	}

	public void hitProp(int i, int j, Actor actor) {
		if (i > this.FM.EI.getNum() - 1 || this.oldProp[i] == 2) return;
		if (this.isChunkAnyDamageVisible("Prop" + (i + 1)) || this.isChunkAnyDamageVisible("PropRot" + (i + 1))) {
			this.hierMesh().chunkVisible(Aircraft.Props[i + 1][0], false);
			this.hierMesh().chunkVisible(Aircraft.Props[i + 1][1], false);
			this.hierMesh().chunkVisible(Aircraft.Props[i + 1][2], true);
		}
		super.hitProp(i, j, actor);
	}

	public void update(float f) {
		if (this == World.getPlayerAircraft()) World.cur().diffCur.Torque_N_Gyro_Effects = false;
		super.update(f);
	}

	static {
		Class class1 = P_47BDT.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P-47");
		Property.set(class1, "meshName", "3DO/Plane/P-47B-DT(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
		Property.set(class1, "meshName_us", "3DO/Plane/P-47B-DT(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1947.5F);
		Property.set(class1, "FlightModel", "FlightModels/P-47B15.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D10.class });
		Property.set(class1, "LOSElevation", 0.9879F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08" });
	}
}
