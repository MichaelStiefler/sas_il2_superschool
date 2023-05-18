package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class B25A extends B_25 implements TypeTransport {

	public B25A() {
		this.fSightCurForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurAltitude = 3000F;
		this.fSightCurSpeed = 200F;
		this.fSightCurReadyness = 0.0F;
	}

	public static boolean bChangedPit = false;
	public float          fSightCurForwardAngle;
	public float          fSightCurSideslip;
	public float          fSightCurAltitude;
	public float          fSightCurSpeed;
	public float          fSightCurReadyness;

	static {
		Class class1 = B25A.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-25");
		Property.set(class1, "meshName", "3DO/Plane/B-25A(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "meshName_us", "3DO/Plane/B-25A(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1956.6F);
		Property.set(class1, "FlightModel", "FlightModels/B-25C.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB25C25.class });
		Property.set(class1, "LOSElevation", 0.73425F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01", "_BombSpawn02", "_BombSpawn03" });
	}
}
