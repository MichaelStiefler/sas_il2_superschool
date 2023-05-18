package com.maddox.il2.engine;

public class GuidedMissileInterop {
	private static Class guidedMissileUtilsClass;
	private static Class missileGunClass;
	private static Class missileInterceptableClass;
	private static Class typeGuidedMissileCarrierAbstract;
	private static boolean guidedMissileModExists = false;

	public static Class getGuidedMissileUtilsClass() {
		return guidedMissileUtilsClass;
	}

	public static void setGuidedMissileUtilsClass(Class guidedMissileUtilsClass) {
		GuidedMissileInterop.guidedMissileUtilsClass = guidedMissileUtilsClass;
	}

	public static Class getMissileGunClass() {
		return missileGunClass;
	}

	public static void setMissileGunClass(Class missileGunClass) {
		GuidedMissileInterop.missileGunClass = missileGunClass;
	}

	public static boolean getGuidedMissileModExists() {
		return guidedMissileModExists;
	}

	public static void setGuidedMissileModExists(boolean guidedMissileModExists) {
		GuidedMissileInterop.guidedMissileModExists = guidedMissileModExists;
	}

	public static Class getMissileInterceptableClass() {
		return missileInterceptableClass;
	}

	public static void setMissileInterceptableClass(Class missileInterceptableClass) {
		GuidedMissileInterop.missileInterceptableClass = missileInterceptableClass;
	}

	public static Class getTypeGuidedMissileCarrierAbstract() {
		return typeGuidedMissileCarrierAbstract;
	}

	public static void setTypeGuidedMissileCarrierAbstract(Class typeGuidedMissileCarrierAbstract) {
		GuidedMissileInterop.typeGuidedMissileCarrierAbstract = typeGuidedMissileCarrierAbstract;
	}

	static {
		try {
			setGuidedMissileUtilsClass(Class.forName("com.maddox.il2.objects.weapons.GuidedMissileUtils"));
			setMissileGunClass(Class.forName("com.maddox.il2.objects.weapons.MissileGun"));
			setMissileInterceptableClass(Class.forName("com.maddox.il2.objects.weapons.MissileInterceptable"));
			setTypeGuidedMissileCarrierAbstract(Class.forName("com.maddox.il2.objects.air.TypeGuidedMissileCarrier"));
			setGuidedMissileModExists(true);
		} catch (ClassNotFoundException cnfe) {
		}

	}
}
