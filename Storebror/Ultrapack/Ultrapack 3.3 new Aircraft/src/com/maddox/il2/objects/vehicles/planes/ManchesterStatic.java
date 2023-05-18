package com.maddox.il2.objects.vehicles.planes;

public abstract class ManchesterStatic extends Plane {
	public static class Manchester_I extends PlaneGeneric {}

	public static class Manchester_Ia extends PlaneGeneric {}

	public static class Manchester_early extends PlaneGeneric {}

	static {
		new PlaneGeneric.SPAWN(Manchester_early.class);
		new PlaneGeneric.SPAWN(Manchester_I.class);
		new PlaneGeneric.SPAWN(Manchester_Ia.class);
	}
}
