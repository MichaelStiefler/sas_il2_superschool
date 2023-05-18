// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:12:36
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TypeSupersonic.java

package com.maddox.il2.objects.air;


public interface TypeSupersonic
{

    public abstract float getAirPressure(float f);

    public abstract float getAirPressureFactor(float f);

    public abstract float getAirDensity(float f);

    public abstract float getAirDensityFactor(float f);

    public abstract float getMachForAlt(float f);

    public abstract float calculateMach();

    public abstract void soundbarier();

    public static final float G_CONST = 9.80665F;
    public static final float p0 = 101325F;
    public static final float T0 = 288.15F;
    public static final float L = 0.0065F;
    public static final float R = 8.31447F;
    public static final float M = 0.0289644F;
    public static final float Rho0 = 1.225F;
    public static final float fMachCwX[] = {
        0.0F, 0.8F, 0.9F, 1.0F, 1.1F, 1.2F, 1.3F, 1.6F, 2.0F, 3F, 
        5F, 3.402823E+038F
    };
    public static final float fMachCwY[] = {
        1.0F, 1.1F, 2.0F, 2.3F, 2.4F, 2.4F, 2.2F, 1.8F, 1.5F, 1.2F, 
        1.1F, 1.0F
    };
    public static final float fMachAltX[] = {
        0.0F, 11F, 20.1F, 47.4F, 51.4F, 86F, 90F, 500F, 3.402823E+038F
    };
    public static final float fMachAltY[] = {
        1225F, 1062.3F, 1062.3F, 1187.3F, 1187.3F, 986.5F, 986.5F, 1108.8F, 1108.8F
    };

}