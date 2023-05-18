package com.maddox.il2.objects.vehicles.planes;

public abstract class StirlingStatic extends Plane {
	public static class SHORT_Stirling extends PlaneGeneric {}

	public static class SHORT_StirlingIII extends PlaneGeneric {}

	static {
		new PlaneGeneric.SPAWN(SHORT_Stirling.class);
		new PlaneGeneric.SPAWN(SHORT_StirlingIII.class);
	}
}
