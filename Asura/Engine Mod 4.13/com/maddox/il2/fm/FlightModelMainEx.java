// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:06:07
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FlightModelMainEx.java

package com.maddox.il2.fm;


// Referenced classes of package com.maddox.il2.fm:
//            FlightModelMain, Arm

public class FlightModelMainEx
{

    private FlightModelMainEx()
    {
    }

    public static Arm getFmArm(FlightModelMain theFM)
    {
        return theFM.Arms;
    }

    public static float getFmGCenter(FlightModelMain theFM)
    {
        return theFM.Arms.GCENTER;
    }
}