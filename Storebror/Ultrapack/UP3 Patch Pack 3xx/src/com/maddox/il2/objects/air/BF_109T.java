package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class BF_109T extends BF_109 {

	public BF_109T() {
		this.kangle = 0.0F;
	}

	public void update(float f) {
		if (this.FM.getSpeed() > 5F) {
			this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 0.0F,
					Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
			this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 0.0F,
					Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
		}
		this.hierMesh().chunkSetAngles("WaterL_D0", 0.0F, -38F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("WaterR_D0", 0.0F, -38F * this.kangle, 0.0F);
		this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
		if (this.kangle > 1.0F)
			this.kangle = 1.0F;
		super.update(f);
		if (this.FM.isPlayers()) {
			if (!Main3D.cur3D().isViewOutside())
				this.hierMesh().chunkVisible("CF_D0", false);
			else
				this.hierMesh().chunkVisible("CF_D0", true);
			if (!Main3D.cur3D().isViewOutside())
				this.hierMesh().chunkVisible("CF_D1", false);
			this.hierMesh().chunkVisible("CF_D2", false);
			this.hierMesh().chunkVisible("CF_D3", false);
			if (!Main3D.cur3D().isViewOutside())
				this.hierMesh().chunkVisible("Blister1_D0", false);
			else
				this.hierMesh().chunkVisible("Blister1_D0", true);
			Point3d point3d = World.getPlayerAircraft().pos.getAbsPoint();
			if (point3d.z - World.land().HQ(point3d.x, point3d.y) < 0.01D)
				this.hierMesh().chunkVisible("CF_D0", true);
			if (this.FM.AS.bIsAboutToBailout)
				this.hierMesh().chunkVisible("Blister1_D0", false);
			if (this.FM.Gears.arrestorVAngle == 0.0F || this.FM.CT.arrestorControl < 0.5F) {
				this.FM.setGC_Gear_Shift(-0.45F);
//                HUD.training("109T GC_Gear_Shift=-0.45");
			} else {
				this.FM.setGC_Gear_Shift(0.0F);
//                HUD.training("109T GC_Gear_Shift=0.0");
			}
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		float f1 = 0.8F;
		float f2 = -0.5F * (float) Math.cos(f / f1 * Math.PI) + 0.5F;
		if (f <= f1) {
			hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -78F * f2, 0.0F);
			hiermesh.chunkSetAngles("GearL2_D0", -24F * f2, 0.0F, 0.0F);
		}
		f2 = -0.5F * (float) Math.cos((f - (1.0F - f1)) / f1 * Math.PI) + 0.5F;
		if (f >= 1.0F - f1) {
			hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 78F * f2, 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 24F * f2, 0.0F, 0.0F);
		}
		if (f > 0.99F) {
			hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -78F, 0.0F);
			hiermesh.chunkSetAngles("GearL2_D0", -24F, 0.0F, 0.0F);
			hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 78F, 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 24F, 0.0F, 0.0F);
		}
		if (f < 0.01F) {
			hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 0.0F);
			hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 0.0F);
			hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
		}
	}

	protected void moveGear(float f) {
		float f1 = 0.9F - ((Wing) this.getOwner()).aircIndex(this) * 0.1F;
		float f2 = -0.5F * (float) Math.cos(f / f1 * Math.PI) + 0.5F;
		if (f <= f1) {
			this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -78F * f2, 0.0F);
			this.hierMesh().chunkSetAngles("GearL2_D0", -24F * f2, 0.0F, 0.0F);
		}
		f2 = -0.5F * (float) Math.cos((f - (1.0F - f1)) / f1 * Math.PI) + 0.5F;
		if (f >= 1.0F - f1) {
			this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 78F * f2, 0.0F);
			this.hierMesh().chunkSetAngles("GearR2_D0", 24F * f2, 0.0F, 0.0F);
		}
		if (f > 0.99F) {
			this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -78F, 0.0F);
			this.hierMesh().chunkSetAngles("GearL2_D0", -24F, 0.0F, 0.0F);
			this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 78F, 0.0F);
			this.hierMesh().chunkSetAngles("GearR2_D0", 24F, 0.0F, 0.0F);
		}
	}

	public void moveArrestorHook(float f) {
		this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -37F * f, 0.0F);
		this.arrestor = f;
	}

	public void moveSteering(float f) {
		if (f > 77.5F) {
			f = 77.5F;
			this.FM.Gears.steerAngle = f;
		}
		if (f < -77.5F) {
			f = -77.5F;
			this.FM.Gears.steerAngle = f;
		}
		this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
	}

	private float kangle;
	protected float arrestor;

	static {
		Class class1 = BF_109T.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Bf109");
		Property.set(class1, "meshName", "3DO/Plane/Bf-109T/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1944F);
		Property.set(class1, "FlightModel", "FlightModels/Bf-109T-1.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109Ex.class });
		Property.set(class1, "LOSElevation", 0.74985F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02" });
	}
}
