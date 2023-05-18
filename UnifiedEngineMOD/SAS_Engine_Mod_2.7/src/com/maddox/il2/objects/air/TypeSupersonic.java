// Source File Name: TypeSupersonic.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

public interface TypeSupersonic {

  public static final float G_CONST = 9.80665F;
  public static final float p0 = 101325F;
  public static final float T0 = 288.15F;
  public static final float L = 0.0065F;
  public static final float R = 8.31447F;
  public static final float M = 0.0289644F;
  public static final float Rho0 = 1.225F;
  // Mach Drag Coefficient tables, typical base drag value for a bullet is ~ 0.3
  public static float[] fMachCwX = {0.0F, 0.8F, 0.9F, 1.0F, 1.1F, 1.2F, 1.3F, 1.6F, 2.0F, 3.0F, 5.0F, Float.MAX_VALUE}; // Mach Values
  public static float[] fMachCwY = {1.0F, 1.1F, 2.0F, 2.3F, 2.4F, 2.4F, 2.2F, 1.8F, 1.5F, 1.2F, 1.1F, 1.0F}; // Cw Values
  public static float[] fMachAltX = {0.0F, 11.0F, 20.1F, 47.4F, 51.4F, 86.0F, 90.0F, 500.0F, Float.MAX_VALUE}; // Altitude in km
  public static float[] fMachAltY = {1225.0F, 1062.3F, 1062.3F, 1187.3F, 1187.3F, 986.5F, 986.5F, 1108.8F, 1108.8F}; // Mach 1.0 Speed in km/h

  public abstract float getAirPressure(float theAltitude);

  public abstract float getAirPressureFactor(float theAltitude);

  public abstract float getAirDensity(float theAltitude);

  public abstract float getAirDensityFactor(float theAltitude);

  public abstract float getMachForAlt(float theAltValue);

  public float calculateMach();

  public void soundbarier();
}