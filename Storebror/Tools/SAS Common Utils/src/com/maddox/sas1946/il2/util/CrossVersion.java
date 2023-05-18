package com.maddox.sas1946.il2.util;

import com.maddox.il2.objects.air.AircraftLH;
import com.maddox.il2.objects.air.CockpitPilot;

/**
 * "CrossVersion" Class of the "SAS Common Utils"
 * <p>
 * This Class provides helper functions for Cross Version Compatible coding of IL-2 1946 mods.
 * <p>
 *
 * @version 1.1.0
 * @since 1.0.2
 * @author SAS~Storebror
 */
public class CrossVersion {

	/**
	 * Override default Constructor to avoid instanciation
	 * @throws Exception
     * @since 1.0.4
	 */
	private CrossVersion() throws Exception{
        throw new Exception("Class com.maddox.sas1946.il2.util.CrossVersion cannot be instanciated!");
	}
	
	// *****************************************************************************************************************************************************************************************************
	// Public interface section.
	// Methods and Arguments are supposed to be final here.
	// Take care of encapsulation and don't modify methods or arguments declared on this interface
	// to ensure future backward compatibility
	// If you need a new method with the same name but different parameters or return types,
	// simply overload the given methods in this interface.
	// *****************************************************************************************************************************************************************************************************

	/**
	 * This helper function lets you set the "printCompassHeading" field regardless the game version your mod is running on.<BR>
	 * In IL-2 4.09m and earlier there was no such Field "printCompassHeading" at all.<BR>
	 * In IL-2 4.10m and 4.10.1m, "printCompassHeading" was a static field of the "AircraftLH" class.<BR>
	 * In IL-2 4.11m and later, "printCompassHeading" is a member field of the "Cockpit" class.<BR>
	 * This helper function deals with these version differences automatically.
	 *
	 * @param obj
	 *            The Object (Aircraft or Cockpit) instance where you want to set the "printCompassHeading" field for.
	 * @param flag
	 *            defines the value which the field "printCompassHeading" will be set to.
     * @since 1.0.2
	 */
	public static final void setPrintCompassHeading(Object obj, boolean flag) {
		doSetPrintCompassHeading(obj, flag);
	}

	/**
	 * This helper function lets you set the "limits6DoF" field regardless the game version your mod is running on.<BR>
	 * This field was introduced with IL-2 4.11m
	 *
	 * @param obj
	 *            The Cockpit instance where you want to set the "limits6DoF" field for.
	 * @param limits
	 *            The limits to apply for 6DoF
     * @since 1.0.2
	 */
	public static final void setLimits6DoF(Object obj, float[] limits) {
		doSetLimits6DoF(obj, limits);
	}

	/**
	 * This helper function compares a given float value to an array of float comparatives and returns the interpolated index position of the given float value in that array.<BR>
	 * A similar version exists in some but not all game versions within different classes, sometimes as static, sometimes as member function.<BR>
	 * Here you have it reliably in one spot.
	 *
	 * @param f
	 *            The float value "f" to find the array index of.
	 * @param af
	 *            The array of float values to compare "f" to.
	 * @return The interpolated array index position of "f".
     * @since 1.0.2
	 */
	public static final float floatindex(float f, float af[]) {
		return thefloatindex(f, af);
	}

	// *****************************************************************************************************************************************************************************************************
	// Private implementation section.
	// Do whatever you like here but keep it private to this class.
	// *****************************************************************************************************************************************************************************************************

	private static void doSetPrintCompassHeading(Object obj, boolean flag) {
		if (BaseGameVersion.is411orLater()) {
			Reflection.setBoolean(obj, "printCompassHeading", flag);
		} else if (BaseGameVersion.is410orLater()) {
			Reflection.setBoolean(AircraftLH.class, "printCompassHeading", flag);
		}
	}

	private static final void doSetLimits6DoF(Object obj, float[] limits) {
		if (!BaseGameVersion.is411orLater()) return;
		((CockpitPilot) obj).limits6DoF = limits;
	}

	private static float thefloatindex(float f, float af[]) {
		int i = (int) f;
		if (i >= af.length - 1) return af[af.length - 1];
		if (i < 0) return af[0];
		if (i == 0) {
			if (f > 0.0F) return af[0] + f * (af[1] - af[0]);
			else return af[0];
		} else return af[i] + (f % i) * (af[i + 1] - af[i]);
	}

}
