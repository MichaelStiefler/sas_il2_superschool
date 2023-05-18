// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:07:01
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Supersonic.java

package com.maddox.il2.fm;

import com.maddox.rts.SectFile;

public class Supersonic
{

    public Supersonic()
    {
        fDragFactorX[0] = VminMach = 0.95F;
        fDragFactorY[0] = 1.0F;
        fDragFactorX[1] = VsoundMach1 = 0.99F;
        fDragFactorY[1] = CfsoundMach1 = 5F;
        fDragFactorX[2] = VsoundMach2 = 1.1F;
        fDragFactorY[2] = CfsoundMach2 = 5F;
        fDragFactorX[3] = VmaxMach = 2.5F;
        fDragFactorY[3] = CfmaxMach = 1.5F;
        allParamsSet = false;
    }

    public Supersonic(SectFile theSectFile)
    {
        load(theSectFile);
    }

    public final void load(SectFile theSectFile)
    {
        String strSection = "Supersonic";
        fDragFactorX[0] = VminMach = theSectFile.get(strSection, "VminMach", 0.95F);
        fDragFactorX[1] = VsoundMach1 = theSectFile.get(strSection, "VsoundMach1", 0.99F);
        fDragFactorY[1] = CfsoundMach1 = theSectFile.get(strSection, "CfsoundMach1", 5F);
        fDragFactorX[2] = VsoundMach2 = theSectFile.get(strSection, "VsoundMach2", 1.1F);
        fDragFactorY[2] = CfsoundMach2 = theSectFile.get(strSection, "CfsoundMach2", 5F);
        fDragFactorX[3] = VmaxMach = theSectFile.get(strSection, "VmaxMach", 2.5F);
        fDragFactorY[3] = CfmaxMach = theSectFile.get(strSection, "CfmaxMach", 1.5F);
        allParamsSet = theSectFile.exist(strSection, "VminMach") && theSectFile.exist(strSection, "VsoundMach1") && theSectFile.exist(strSection, "CfsoundMach1") && theSectFile.exist(strSection, "VsoundMach2") && theSectFile.exist(strSection, "CfsoundMach2") && theSectFile.exist(strSection, "VmaxMach") && theSectFile.exist(strSection, "CfmaxMach");
    }

    public float getDragFactorForMach(float theMachValue)
    {
        if(theMachValue <= VminMach)
            return 1.0F;
        if(theMachValue >= VmaxMach)
            return CfmaxMach;
        int i = 0;
        for(i = 0; i < fDragFactorX.length; i++)
            if(fDragFactorX[i] > theMachValue)
                break;

        if(i == 0)
        {
            return fDragFactorY[0];
        } else
        {
            float baseFactor = fDragFactorY[i - 1];
            float spanFactor = fDragFactorY[i] - baseFactor;
            float baseMach = fDragFactorX[i - 1];
            float spanMach = fDragFactorX[i] - baseMach;
            float spanMult = (theMachValue - baseMach) / spanMach;
            return baseFactor + spanFactor * spanMult;
        }
    }

    public float VminMach;
    public float VsoundMach1;
    public float CfsoundMach1;
    public float VsoundMach2;
    public float CfsoundMach2;
    public float VmaxMach;
    public float CfmaxMach;
    public boolean allParamsSet;
    private float fDragFactorX[] = {
        0.95F, 0.99F, 1.1F, 2.5F
    };
    private float fDragFactorY[] = {
        1.0F, 5F, 5F, 1.5F
    };
}