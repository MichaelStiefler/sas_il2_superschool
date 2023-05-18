// Source File Name: MissilePhysics.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

public class MissilePhysics {

	private static float[]     fMachAltX = { 0.0F, 11.0F, 20.1F, 47.4F, 51.4F, 86.0F, 90.0F, 500.0F, Float.MAX_VALUE }; // Altitude in km
	private static float[]     fMachAltY = { 1225.0F, 1062.3F, 1062.3F, 1187.3F, 1187.3F, 986.5F, 986.5F, 1108.8F, 1108.8F }; // Mach 1.0 Speed in km/h
	// Mach Drag Coefficient tables, typical base drag value for a bullet is ~ 0.3
	private static float[]     fMachCwX  = { 0.0F, 0.8F, 0.9F, 1.0F, 1.1F, 1.2F, 1.3F, 1.6F, 2.0F, 3.0F, 5.0F, Float.MAX_VALUE }; // Mach Values
	// private static float[] fMachCwY = {1.0F, 1.1F, 2.0F, 2.3F, 2.4F, 2.4F, 2.2F, 1.8F, 1.5F, 1.2F, 1.1F, 1.0F}; // Cw Values
	private static float[]     fMachCwY  = { 1.0F, 1.17F, 1.67F, 2.23F, 2.67F, 2.73F, 2.7F, 2.4F, 2.0F, 1.7F, 1.5F, 1.4F }; // Cw Values
	public static final float  G_CONST   = 9.80665F;
	private static final float L         = 0.0065F;
	private static final float M         = 0.0289644F;
	private static final float p0        = 101325F;
	private static final float R         = 8.31447F;
	private static final float Rho0      = 1.225F;
	private static final float T0        = 288.15F;

	public static float getAirDensity(float theAltitude) {
		return getAirPressure(theAltitude) * M / (R * (T0 - L * theAltitude));
	}

	public static float getAirDensityFactor(float theAltitude) {
		return getAirDensity(theAltitude) / Rho0;
	}

	public static float getAirPressure(float theAltitude) {
		float fBase = 1F - L * theAltitude / T0;
		float fExponent = G_CONST * M / (R * L);
		return p0 * (float) Math.pow(fBase, fExponent);
	}

	public static float getAirPressureFactor(float theAltitude) {
		return getAirPressure(theAltitude) / p0;
	}

	public static float getCwFactorForMach(float theMachValue) {
		int i = 0;
		for (i = 0; i < fMachCwX.length - 1; i++) { // Fix 2013/06/09: Check n-1 elements only, so when loop falls through we're on last element and don't exceed array boundaries
			if (fMachCwX[i] > theMachValue) { break; }
		}
		if (i == 0) { return fMachCwY[0]; }
		float baseFactor = fMachCwY[i - 1];
		float spanFactor = fMachCwY[i] - baseFactor;
		float baseMach = fMachCwX[i - 1];
		float spanMach = fMachCwX[i] - baseMach;
		float spanMult = (theMachValue - baseMach) / spanMach;
		return baseFactor + spanFactor * spanMult;
	}

	public static float getDegPerSec(float fSpeed, float fMaxG) {
		return fMaxG * G_CONST * 180.0F / (fSpeed * (float) Math.PI);
	}

	// public static float getAirDensity(float theAltitude) {
	// theAltitude /= 1000F; // get altitude in km
	// float factor1 = 44.3308F;
	// if (theAltitude > 44.3308F) return 0F;
	// float factor2 = 42.2665F;
	// float factor3 = 4.25588F;
	// return (float)Math.pow((factor1 - theAltitude) / factor2, factor3);
	// }
	public static float getDragForce(float theFrontSqM, float theDragCoefficient, float theAltitudeM, float theSpeedMps) {
		float altSpeedMach = theSpeedMps / getMpsFromKmh(getMachForAlt(theAltitudeM));
		float altSpeedCw = theDragCoefficient * getCwFactorForMach(altSpeedMach);
		float altDensity = getAirDensity(theAltitudeM);

		float dragForce = theFrontSqM * altSpeedCw * altDensity * theSpeedMps * theSpeedMps / 2F;

		return dragForce;
	}

	public static float getDragInGravity(float theFrontSqM, float theDragCoefficient, float theAltitudeM, float theSpeedMps, float theTangageDeg, float theMass) {
		float fDragForce = getDragForce(theFrontSqM, theDragCoefficient, theAltitudeM, theSpeedMps);
		double alpha = Math.toRadians(theTangageDeg);
		fDragForce += Math.sin(alpha) * G_CONST * theMass;
		return fDragForce;
	}

	public static float getForceInGravity(float theTangageDeg, float theForceN, float theMass) {
		float g = G_CONST * theMass;
		double alpha = Math.toRadians(90.0F + Math.abs(theTangageDeg));
		float resForce = 0F;
		if (theTangageDeg < 0F) {
			if (theForceN == 0F) { return g; }
			if (theForceN < g) { return 0.0F; }
			double beta = Math.asin(Math.sin(alpha) * g / theForceN);
			double gamma = Math.PI - alpha - beta;
			resForce = g * (float) (Math.sin(gamma) / Math.sin(beta));
		} else {
			resForce = (float) Math.sqrt(g * g + theForceN * theForceN + 2 * g * theForceN * Math.cos(alpha));
		}
		return resForce;
	}

	public static float getGForce(float fSpeed, float fDegPerSec) {
		return fDegPerSec * fSpeed * (float) Math.PI / (G_CONST * 180.0F);
	}

	public static float getMachForAlt(float theAltValue) {
		theAltValue /= 1000F; // get altitude in km
		int i = 0;
		for (i = 0; i < fMachAltX.length - 1; i++) { // Fix 2013/06/09: Check n-1 elements only, so when loop falls through we're on last element and don't exceed array boundaries
			if (fMachAltX[i] > theAltValue) { break; }
		}
		if (i == 0) { return fMachAltY[0]; }
		float baseMach = fMachAltY[i - 1];
		float spanMach = fMachAltY[i] - baseMach;
		float baseAlt = fMachAltX[i - 1];
		float spanAlt = fMachAltX[i] - baseAlt;
		float spanMult = (theAltValue - baseAlt) / spanAlt;
		return baseMach + spanMach * spanMult;
	}

	public static float getMpsFromKmh(float kilometersPerHour) {
		return kilometersPerHour / 3.6F;
	}

	private MissilePhysics() {
	}
}
