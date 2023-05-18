package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class SPITFIRE8 extends SPITFIRE {

	public SPITFIRE8() {
		this.flapps = 0.0F;
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.0F, 0.6F, 0.0F, -95F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f, 0.2F, 1.0F, 0.0F, -95F), 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f, 0.01F, 0.99F, 0.0F, -75F), 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		xyz[2] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.247F, 0.0F, -0.247F);
		this.hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
		xyz[2] = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.247F, 0.0F, 0.247F);
		this.hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
	}

	public void update(float f) {
		super.update(f);
		float f1 = this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(this.flapps - f1) > 0.01F) {
			this.flapps = f1;
			this.hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * f1, 0.0F);
			this.hierMesh().chunkSetAngles("Oil2_D0", 0.0F, -20F * f1, 0.0F);
		}
	}

	private float flapps;

	static {
		Class class1 = SPITFIRE8.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Spit");
		Property.set(class1, "meshName", "3DO/Plane/SpitfireMkVIII(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1946.5F);
		Property.set(class1, "FlightModel", "FlightModels/Spitfire-LF-VIII-M66-18.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitSpit8.class });
		Property.set(class1, "LOSElevation", 0.5926F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev08" });
	}
}
