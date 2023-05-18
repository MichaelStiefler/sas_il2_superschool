// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   BlindLandingData.java

package com.maddox.il2.objects.vehicles.radios;


public class BlindLandingData
{

    public BlindLandingData()
    {
        isOnOuterMarker = false;
        isOnInnerMarker = false;
        blindLandingAzimuthPB = 0.0F;
        blindLandingAzimuthBP = 0.0F;
        blindLandingRange = 0.0F;
        signalStrength = 0.0F;
        runwayLength = 1700F;
    }

    public void reset()
    {
        isOnOuterMarker = false;
        isOnInnerMarker = false;
        blindLandingAzimuthPB = 0.0F;
        blindLandingAzimuthBP = 0.0F;
        blindLandingRange = 50000F;
        signalStrength = 0.0F;
    }

    public void addSignal(float f, float f1, float f2, boolean flag, float f3, float f4, float f5,
    		float f6, boolean flag1)
    {
        blindLandingAzimuthPB = f1;
        blindLandingAzimuthBP = f;
        blindLandingRange = f2;
        signalStrength = f3;
        bSingleDirection = flag1;
        if(flag)
        {
            runwayLength = 1700F;
            if(f5 > 500F)
                runwayLength = 2300F;
        } else
        {
            isOnInnerMarker = false;
            isOnOuterMarker = false;
            isOnInner2Marker = false;
            runwayLength = 0.0F;
            return;
        }
        if(Math.abs(blindLandingAzimuthBP) < 10F && f2 > (f6 - 85F) + runwayLength && f2 < f6 + 85F + runwayLength)
            isOnInner2Marker = true;
        else
            isOnInner2Marker = false;
        if(Math.abs(blindLandingAzimuthBP) < 10F && f2 > (f5 - 85F) + runwayLength && f2 < f5 + 85F + runwayLength)
            isOnInnerMarker = true;
        else
            isOnInnerMarker = false;
        if(Math.abs(blindLandingAzimuthBP) < 10F && f2 > (f4 - 85F) + runwayLength && f2 < f4 + 85F + runwayLength)
            isOnOuterMarker = true;
        else
            isOnOuterMarker = false;
    }

    public boolean isOnOuterMarker;
    public boolean isOnInnerMarker;
    public boolean isOnInner2Marker;
    public float blindLandingAzimuthPB;
    public float blindLandingAzimuthBP;
    public float blindLandingRange;
    public float signalStrength;
    private static final float markerFanLength = 85F;
    public float runwayLength;
    private boolean bSingleDirection;
}
