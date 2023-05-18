// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   Polares.java

package com.maddox.il2.fm;

import com.maddox.rts.HomePath;
import com.maddox.rts.SectFile;
import java.io.*;
import java.util.StringTokenizer;

// Referenced classes of package com.maddox.il2.fm:
//            FMMath, Atmosphere

public class Polares extends FMMath
{

    public Polares()
    {
        lastAOA = -1E+012F;
        normP = new float[250];
        maxP = new float[250];
        AOA_crit = 17F;
        V_max = 153F;
        V_min = 35F;
        P_Vmax = 18000F;
        G = 26000F;
        S = 18F;
        K_max = 14F;
        Cy0_max = 0.18F;
        Tfac = 1.0F;
        Vyfac = 1.0F;
        FlapsMult = 0.16F;
        FlapsAngSh = 4F;
        Vz_climb = 21F;
        V_climb = 72F;
        T_turn = 19F;
        V_turn = 310F;
        V_maxFlaps = 180F;
        V_land = 144F;
        AOA_land = 12F;
        Ro = Atmosphere.ro0();
        R1000 = Atmosphere.density(1000F);
        sign = 1.0F;
        AOALineH = 13F;
        AOALineL = -13F;
        AOACritH = 16F;
        AOACritL = -16F;
        parabCyCoeffH = 0.01F;
        parabCyCoeffL = 0.01F;
        Cy0 = 0.0F;
        lineCyCoeff = 0.85F;
        declineCoeff = 0.007F;
        maxDistAng = 30F;
        parabAngle = 5F;
        CyCritH = 1.0F;
        CyCritL = -1F;
        AOAMinCx = -1F;
        AOAParabH = 6F;
        AOAParabL = -6F;
        CxMin = 0.02F;
        parabCxCoeff = 0.0008F;
        AOACritH_0 = 16F;
        AOACritL_0 = -16F;
        Cy0_0 = 0.0F;
        CyCritH_0 = 1.0F;
        CyCritL_0 = -1F;
        AOAMinCx_Shift = 0.0F;
        CxMin_0 = 0.02F;
        parabCxCoeff_0 = 0.0008F;
        AOACritH_1 = 16F;
        AOACritL_1 = -16F;
        Cy0_1 = 0.0F;
        CyCritH_1 = 1.0F;
        CyCritL_1 = -1F;
        CxMin_1 = 0.02F;
        parabCxCoeff_1 = 0.0008F;
        mc2 = new float[8];
        mc3 = new float[8];
        mc4 = new float[8];
        mm = new float[8];
        md = new float[8];
        mcMin = 999F;
    }

    public final float new_Cy(float f)
    {
        if(f == lastAOA)
        {
            return lastCy;
        } else
        {
            polares2(f);
            return lastCy;
        }
    }

    public final float new_Cx(float f)
    {
        if(f == lastAOA)
        {
            return lastCx;
        } else
        {
            polares2(f);
            return lastCx;
        }
    }

    protected final void polares2(float f)
    {
        float f1 = new_Cxa(f);
        float f2 = new_Cya(f);
        float f3 = (float)Math.cos(DEG2RAD(f));
        float f4 = (float)Math.sin(DEG2RAD(f));
        lastAOA = f;
        lastCx = f1 * f3 - f2 * f4;
        lastCy = f2 * f3 + f1 * f4;
    }

    public final float new_CyM(float f, float f1)
    {
        if(f != lastAOA || f1 != lastMach)
            polares2M(f, f1);
        return lastCy;
    }

    public final float new_CxM(float f, float f1)
    {
        if(f != lastAOA || f1 != lastMach)
            polares2M(f, f1);
        return lastCx;
    }

    protected final void polares2M(float f, float f1)
    {
        float f3 = new_Cya(f);
        float f2 = new_CxaM(f, f1, f3);
        float f4 = (float)Math.cos(DEG2RAD(f));
        float f5 = (float)Math.sin(DEG2RAD(f));
        lastAOA = f;
        lastCx = f2 * f4 - f3 * f5;
        lastCy = f3 * f4 + f2 * f5;
    }

    public float get_AOA_CRYT()
    {
        return AOACritH;
    }

    public final void setFlaps(float f)
    {
        float f1 = 0.25F * f + 0.75F * (float)Math.sqrt(f);
        Cy0 = Cy0_0 + (Cy0_1 - Cy0_0) * f1;
        CyCritH = CyCritH_0 + (CyCritH_1 - CyCritH_0) * f1;
        CyCritL = CyCritL_0 + (CyCritL_1 - CyCritL_0) * f1;
        AOACritH = AOACritH_0 + (AOACritH_1 - AOACritH_0) * f1;
        AOACritL = AOACritL_0 + (AOACritL_1 - AOACritL_0) * f1;
        AOALineH = (2.0F * (CyCritH - Cy0)) / lineCyCoeff - AOACritH;
        parabCyCoeffH = (0.5F * lineCyCoeff) / (AOACritH - AOALineH);
        AOALineL = (2.0F * (CyCritL - Cy0)) / lineCyCoeff - AOACritL;
        parabCyCoeffL = (0.5F * lineCyCoeff) / (AOALineL - AOACritL);
        CxMin = CxMin_0 + (CxMin_1 - CxMin_0) * f;
        parabCxCoeff = parabCxCoeff_0 + (parabCxCoeff_1 - parabCxCoeff_0) * f;
        AOAMinCx = AOAMinCx_Shift - Cy0 / lineCyCoeff;
    }

    public final void setCxMin_0()
    {
        AOAMinCx = AOAMinCx_Shift - Cy0_0 / lineCyCoeff;
        float f = S * Ro * V_max * V_max;
        float f1 = (2.0F * G) / f;
        float f2 = (2.0F * P_Vmax) / f;
        float f3 = (f1 - Cy0_0) / lineCyCoeff;
        float f4 = f3 - AOAMinCx;
        CxMin_0 = f2 - parabCxCoeff * f4 * f4;
    }

    public final void setCoeffs(float f, float f1)
    {
        lineCyCoeff = (CyCritH * f - Cy0) / AOACritH;
        CyCritL = (Cy0 + lineCyCoeff * AOACritL) / f;
        AOAMinCx = (-f1 * Cy0) / lineCyCoeff;
        float f2 = S * Ro * V_max * V_max;
        float f3 = (2.0F * G) / f2;
        float f4 = (2.0F * P_Vmax) / f2;
        float f5 = (f3 - Cy0) / lineCyCoeff;
        if(AOAMinCx > f5)
            AOAMinCx = f5;
        float f6 = f5 - AOAMinCx;
        AOAMinCx_Shift = AOAMinCx - -Cy0 / lineCyCoeff;
        CxMin = f4 - parabCxCoeff * f6 * f6;
        AOALineH = (2.0F * (CyCritH - Cy0)) / lineCyCoeff - AOACritH;
        parabCyCoeffH = (0.5F * lineCyCoeff) / (AOACritH - AOALineH);
        AOALineL = (2.0F * (CyCritL - Cy0)) / lineCyCoeff - AOACritL;
        parabCyCoeffL = (0.5F * lineCyCoeff) / (AOALineL - AOACritL);
    }

    public final void calcPolares()
    {
        CyCritH = (2.0F * G) / (S * Ro * V_min * V_min);
        AOACritH = AOA_crit;
        float f = 10000F;
        float f8 = 0.0F;
        float f9 = 0.0F;
        float f10 = 0.0F;
        float f11 = 0.0F;
label0:
        for(int i = 0; i <= 25; i++)
        {
            Cy0 = 0.05F + (float)i * 0.01F;
            if(Cy0 > Cy0_max)
                continue;
            int j = 0;
            do
            {
                if(j >= 200)
                    continue label0;
                float f6 = 1.0F + (float)j * 0.006F;
                for(int k = 0; k < 100; k++)
                {
                    parabCxCoeff = 0.0003F + (float)k * 2E-005F;
                    for(int l = 0; l <= 20; l++)
                    {
                        float f7 = 1.0F - (float)l * 0.05F;
                        setCoeffs(f6, f7);
                        if(lineCyCoeff > 0.12F || CxMin < 0.005F || AOAMinCx > 0.0F)
                            continue;
                        float f1 = -10000F;
                        float f12 = 0;
                        do
                        {
                            if(f12 >= 20)
                                break;
                            float f13 = 0.5F * (float)f12;
                            float f17 = new_Cya(f13) / new_Cxa(f13);
                            if(f1 >= f17)
                                break;
                            f1 = f17;
                            f12++;
                        } while(true);
                        if(f1 > 1.3F * K_max || f1 < 0.6F * K_max)
                            continue;
                        float f2 = -10000F;
                        float f3 = 300F;
                        f12 = 25;
                        do
                        {
                            if(f12 >= 200)
                                break;
                            float f14 = S * Ro * (float)f12 * (float)f12;
                            float f18 = (2.0F * G) / f14;
                            float f21 = (f18 - Cy0) / lineCyCoeff;
                            float f24 = f21 - AOAMinCx;
                            float f27 = 0.5F * (CxMin + parabCxCoeff * f24 * f24) * f14;
                            float f30 = ((float)f12 * (normP[(int) f12] - f27)) / G;
                            if(f2 >= f30)
                                break;
                            f2 = f30;
                            f3 = f12;
                            f12++;
                        } while(true);
                        if(f2 > f2 + 3F || f2 < f2 - 3F)
                            continue;
                        if(f3 > 1.3F * V_climb || f3 < 0.7F * V_climb)
                            continue;
                        float f4 = 10000F;
                        float f5 = 300F;
                        for(f12 = 125; f12 > 40; f12--)
                        {
                            float f15 = S * R1000 * (float)f12 * (float)f12;
                            float f19 = (2.0F * maxP[(int) f12]) / f15;
                            if(f19 < CxMin)
                                continue;
                            float f22 = (float)Math.sqrt((f19 - CxMin) / parabCxCoeff);
                            float f25 = AOAMinCx + f22;
                            if(f25 > 12.5F)
                                f25 = 12.5F;
                            float f28 = (0.5F * new_Cya(f25) * f15) / G;
                            float f31 = (float)Math.sqrt(f28 * f28 - 1.0F);
                            float f32 = (6.283185F * (float)f12) / (9.8F * f31);
                            if(f32 > 60F)
                                continue;
                            if(f4 <= f32)
                                break;
                            f4 = f32;
                            f5 = f12;
                        }

                        f12 = K_max - f1;
                        f12 = Math.abs(f12);
                        float f16 = Vz_climb - f2;
                        f16 = Math.abs(f16);
                        float f20 = V_climb - f3;
                        f20 = Math.abs(f20);
                        float f23 = T_turn - f4;
                        f23 = Math.abs(f23);
                        float f26 = V_turn - f5;
                        f26 = Math.abs(f26);
                        float f29 = 2.0F * f12 + 12F * f16 + 5F * f16 + 15F * f23 + 2.0F * f26;
                        if(f > f29)
                        {
                            f = f29;
                            f8 = f6;
                            f9 = f7;
                            f10 = parabCxCoeff;
                            f11 = Cy0;
                        }
                    }

                }

                j++;
            } while(true);
        }

        Cy0 = f11;
        parabCxCoeff = f10;
        setCoeffs(f8, f9);
        Cy0_0 = Cy0;
        AOACritH_0 = AOACritH;
        AOACritL_0 = AOACritL;
        CyCritH_0 = CyCritH;
        CyCritL_0 = CyCritL;
        CxMin_0 = CxMin;
        parabCxCoeff_0 = parabCxCoeff;
        calcFlaps();
        Cy0_1 = Cy0;
        AOACritH_1 = AOACritH;
        AOACritL_1 = AOACritL;
        CyCritH_1 = CyCritH;
        CyCritL_1 = CyCritL;
        CxMin_1 = CxMin;
        parabCxCoeff_1 = parabCxCoeff;
    }

    public final void calcFlaps()
    {
        float f = S * Ro * V_land * V_land;
        float f1 = (2.0F * G) / f;
        Cy0 = f1 - AOA_land * lineCyCoeff;
        AOAMinCx = -Cy0 / lineCyCoeff + AOAMinCx_Shift;
        parabCxCoeff = parabCxCoeff * FlapsMult;
        f = S * Ro * V_maxFlaps * V_maxFlaps;
        f1 = (2.0F * G) / f;
        float f2 = (2.0F * P_Vmax) / f;
        float f3 = (f1 - Cy0) / lineCyCoeff;
        float f4 = f3 - AOAMinCx;
        CxMin = f2 - parabCxCoeff * f4;
        AOACritH = AOACritH_0 - 4F;
        CyCritH = 0.85F * (Cy0 + AOACritH * lineCyCoeff);
        AOACritL = AOACritL_0 - FlapsAngSh;
        float f5 = -0.9F * (float)Math.sin(-0.03926991F * AOACritL);
        CyCritL = Math.min(-0.7F, f5);
        AOALineH = (2.0F * (CyCritH - Cy0)) / lineCyCoeff - AOACritH;
        parabCyCoeffH = (0.5F * lineCyCoeff) / (AOACritH - AOALineH);
        AOALineL = (2.0F * (CyCritL - Cy0)) / lineCyCoeff - AOACritL;
        parabCyCoeffL = (0.5F * lineCyCoeff) / (AOALineL - AOACritL);
    }

    public float getClimb(float f, float f1, float f2)
    {
        return getClimb(f, f1, f2, 0.0F);
    }

    public float getClimb(float f, float f1, float f2, float f3)
    {
        float f4 = Atmosphere.density(f1);
        float f5 = S * f4 * f * f;
        float f6 = (2.0F * G) / f5;
        float f7 = f / Atmosphere.sonicSpeed(f1);
        float f8 = (f6 - Cy0) / lineCyCoeff;
        float f9 = f8 - AOAMinCx;
        float f10 = 0.5F * (f3 + CxMin + parabCxCoeff * f9 * f9 + cdw(f8, f7, f6)) * f5;
        float f11 = (f * (f2 - f10)) / G;
        return f11;
    }

    public final void saveCoeffs()
    {
        try
        {
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("Coeffs.txt", 0))));
            printwriter.println("; ==============================================================================");
            printwriter.println("[Polares]");
            printwriter.println("; ==============================================================================");
            printwriter.println("lineCyCoeff        " + lineCyCoeff);
            printwriter.println("AOAMinCx_Shift     " + AOAMinCx_Shift);
            printwriter.println("Cy0_0              " + Cy0_0);
            printwriter.println("AOACritH_0         " + AOACritH_0);
            printwriter.println("AOACritL_0         " + AOACritL_0);
            printwriter.println("CyCritH_0          " + CyCritH_0);
            printwriter.println("CyCritL_0          " + CyCritL_0);
            printwriter.println("CxMin_0            " + CxMin_0);
            printwriter.println("parabCxCoeff_0     " + parabCxCoeff_0);
            printwriter.println("Cy0_1              " + Cy0_1);
            printwriter.println("AOACritH_1         " + AOACritH_1);
            printwriter.println("AOACritL_1         " + AOACritL_1);
            printwriter.println("CyCritH_1          " + CyCritH_1);
            printwriter.println("CyCritL_1          " + CyCritL_1);
            printwriter.println("CxMin_1            " + CxMin_1);
            printwriter.println("parabCxCoeff_1     " + parabCxCoeff_1);
            printwriter.close();
        }
        catch(IOException ioexception)
        {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
    }

    public final void drawGraphs(String s, int fuel)
    {
        float f = -10000F;
        try
        {
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s + "_MA521_Polares.txt", 0))));
            for(int i = -90; i < 90; i++)
                printwriter.print(i + "\t");

            printwriter.println();
            for(int i2 = 0; i2 <= 5; i2++)
            {
                setFlaps((float)i2 * 0.2F);
                for(int j = -90; j < 90; j++)
                    printwriter.print(new_Cya(j) + "\t");

                printwriter.println();
                for(int k = -90; k < 90; k++)
                    printwriter.print(new_Cxa(k) + "\t");

                printwriter.println();
                if(i2 == 0)
                {
                    for(int l = -90; l < 90; l++)
                    {
                        float f9 = new_Cya(l) / new_Cxa(l);
                        printwriter.print(f9 * 0.1F + "\t");
                        if(f < f9)
                            f = f9;
                    }

                    printwriter.println();
                }
                for(int i1 = -90; i1 < 90; i1++)
                {
                    float f10 = Cy0 + lineCyCoeff * (float)i1;
                    if((double)f10 < 2D && (double)f10 > -2D)
                        printwriter.print(f10 + "\t");
                    else
                        printwriter.print("\t");
                }

                printwriter.println();
            }

            printwriter.close();
        }
        catch(IOException ioexception)
        {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
        try
        {
            PrintWriter printwriter1 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s + "_MA521_" + (100 - fuel) + "fuel" + "_turn.txt", 0))));
            for(int j1 = 120; j1 < 620; j1 += 2)
                printwriter1.print(j1 + "\t");

            printwriter1.println();
            float f2 = -10000F;
            float f4 = 300F;
            float f6 = 10000F;
            float f8 = 300F;
            for(int j2 = 0; j2 <= 3; j2++)
            {
                switch(j2)
                {
                case 0: // '\0'
                    setFlaps(0.0F);
                    break;

                case 1: // '\001'
                    setFlaps(0.2F);
                    break;

                case 2: // '\002'
                    setFlaps(0.33F);
                    break;

                case 3: // '\003'
                    setFlaps(1.0F);
                    break;
                }
                if(j2 == 0)
                {
                    for(int k1 = 120; k1 < 620; k1 += 2)
                    {
                        float f11 = (float)k1 * 0.27778F;
                        float f13 = S * Ro * f11 * f11;
                        float f15 = (2.0F * G) / f13;
                        float f17 = getAoAbyCy(f15);
                        float f19 = f17 - AOAMinCx;
                        float f21 = 0.5F * (CxMin + parabCxCoeff * f19 * f19) * f13;
                        float f23 = (f11 * (normP[(int)f11] - f21)) / G;
                        if(j2 == 0 && f2 < f23)
                        {
                            f2 = f23;
                            f4 = f11;
                        }
                        if(f23 < -10F)
                            printwriter1.print("\t");
                        else
                            printwriter1.print(f23 * Vyfac + "\t");
                    }

                    printwriter1.println();
                }
                for(int l1 = 120; l1 < 620; l1 += 2)
                {
                    float f12 = (float)l1 * 0.27778F;
                    float f14 = S * R1000 * f12 * f12;
                    float f16 = (2.0F * maxP[(int)f12]) / f14;
                    float f18 = (float)Math.sqrt((f16 - CxMin) / parabCxCoeff);
                    float f20 = AOAMinCx + f18;
                    if(f20 > AOACritH)
                        f20 = AOACritH;
                    float f22 = (0.5F * new_Cya(f20) * f14) / G;
                    float f24 = (float)Math.sqrt(f22 * f22 - 1.0F);
                    float f25 = 0.0F;
                    if(f24 > 0.2F)
                        f25 = (6.283185F * f12) / (9.8F * f24);
                    if(f25 > 40F)
                        f25 = 0.0F;
                    if(f25 == 0.0F)
                        printwriter1.print("\t");
                    else
                        printwriter1.print(f25 * Tfac + "\t");
                    if(j2 == 0 && f25 > 0.0F && f6 > f25)
                    {
                        f6 = f25;
                        f8 = f12;
                    }
                }

                printwriter1.println();
            }

            printwriter1.println("M_takeoff:\t" + G / 9.8F);
            printwriter1.println("K_max:\t" + f);
            printwriter1.println("T_turn:\t" + f6 * Tfac);
            printwriter1.println("V_turn:\t" + f8 * 3.6F);
            printwriter1.println("Vz_climb:\t" + f2 * Vyfac);
            printwriter1.println("V_climb:\t" + f4 * 3.6F);
            printwriter1.println("CxMin_LandFlaps:\t" + CxMin);
            setFlaps(0.33F);
            printwriter1.println("CxMin_TOFlaps:\t" + CxMin);
            setFlaps(0.2F);
            printwriter1.println("CxMin_CombatFlaps:\t" + CxMin);
            setFlaps(0.0F);
            printwriter1.println("CxMin_NoFlaps:\t" + CxMin);
            printwriter1.close();
        }
        catch(IOException ioexception1)
        {
            System.out.println("File save failed: " + ioexception1.getMessage());
            ioexception1.printStackTrace();
        }
    }

    public final float getAoAbyCy(float f)
    {
        if(f > CyCritH)
            return 90F;
        float f1 = (f - Cy0) / lineCyCoeff;
        if(f1 <= AOALineH)
            return f1;
        if(f1 >= AOACritH)
            return 90F;
        float f2 = AOALineH;
        float f3 = AOACritH;
        for(int i = 0; i < 1000; i++)
        {
            float f4 = new_Cya(f1);
            if((double)Math.abs(f4 - f) < 0.0001D)
                return f1;
            if(f4 < f)
                f2 = f1;
            else
                f3 = f1;
            f1 = 0.5F * (f2 + f3);
        }

        return f1;
    }

    public final float new_Cya(float f)
    {
        if(f <= AOALineH && f >= AOALineL)
            return Cy0 + lineCyCoeff * f;
        if(f > 0.0F)
        {
            if(f <= AOACritH)
            {
                float f1 = AOACritH - f;
                f1 *= f1;
                return CyCritH - parabCyCoeffH * f1;
            }
            if(f <= 40F)
                if(f <= maxDistAng)
                {
                    float f2 = f - AOACritH;
                    if(f2 < parabAngle)
                    {
                        return CyCritH - declineCoeff * f2 * f2;
                    } else
                    {
                        float f10 = 0.9F * (float)Math.sin(0.03926991F * maxDistAng);
                        float f12 = maxDistAng - parabAngle - AOACritH;
                        float f14 = CyCritH - declineCoeff * parabAngle * parabAngle - f10;
                        float f15 = f14 / (f12 * f12);
                        float f3 = maxDistAng - f;
                        return f10 + f15 * f3 * f3;
                    }
                } else
                {
                    return 0.9F * (float)Math.sin(0.03926991F * f);
                }
            if(f <= 140F)
            {
                sign = 1.0F;
                if(f > 90F)
                {
                    sign = -1F;
                    f = 40F + (140F - f);
                }
                float f4 = 0.9F * (float)Math.sin(1.570796F + 0.03141593F * (f - 40F));
                return f4 * sign;
            } else
            {
                sign = -1F;
                f = 180F - f;
                float f5 = 0.9F * (float)Math.sin(0.03926991F * f);
                return f5 * sign;
            }
        }
        if(f >= AOACritL)
        {
            float f6 = f - AOACritL;
            f6 *= f6;
            return CyCritL + parabCyCoeffL * f6;
        }
        if(f >= -40F)
        {
            float f7 = AOACritL - f;
            float f11 = CyCritL + 0.007F * f7 * f7;
            float f13 = -0.9F * (float)Math.sin(-0.03926991F * f);
            return Math.min(f11, f13);
        }
        f = -f;
        if(f <= 140F)
        {
            sign = -1F;
            if(f > 90F)
            {
                sign = 1.0F;
                f = 40F + (140F - f);
            }
            float f8 = 0.9F * (float)Math.sin(1.570796F + 0.03141593F * (f - 40F));
            return f8 * sign;
        } else
        {
            sign = 1.0F;
            f = 180F - f;
            float f9 = 0.9F * (float)Math.sin(0.03926991F * f);
            return f9 * sign;
        }
    }

    public final float new_Cxa(float f)
    {
        float f1 = f - AOAMinCx;
        float f2 = CxMin + parabCxCoeff * f1 * f1;
        if(f <= AOAParabH && f >= AOAParabL)
            return f2;
        if(f >= AOACritH)
            f2 += 0.03F * (f - AOACritH);
        else
        if(f <= AOACritL)
            f2 += 0.03F * (AOACritL - f);
        float f3 = 0.2F + 1.2F * (float)Math.abs(Math.sin(DEG2RAD(f)));
        return Math.min(f2, f3);
    }

    public final float new_Cz(float f)
    {
        return 0.7F * (float)Math.sin(DEG2RAD(f));
    }

    public final float getFlaps()
    {
        return Flaps;
    }

    protected final float new_CxaM(float f, float f1, float f2)
    {
        return new_Cxa(f) + cdw(f, f1, f2);
    }

    protected final float cdw(float f, float f1, float f2)
    {
        lastMach = f1;
        if(f1 < mcMin)
            return 0.0F;
        float f3 = f2 + 0.9F;
        float f4 = f3 * 3.333333F;
        int i = (int)f4;
        f4 -= i;
        if(f3 < 0.0F || i >= 7)
        {
            if(f3 < 0.0F)
                i = 0;
            else
            if(i > 7)
                i = 7;
            float f5 = f1 - mc[i];
            if(f5 < 0.0F)
                return 0.0F;
            if(f1 < mm[i])
            {
                f5 /= mm[i] - mc[i];
                return ((f5 * mc4[i] + mc3[i]) * f5 + mc2[i]) * f5 * f5;
            } else
            {
                return 0.5F * f5 + md[i];
            }
        }
        float f6 = mc[i] + (mc[i + 1] - mc[i]) * f4;
        float f7 = mm[i] + (mm[i + 1] - mm[i]) * f4;
        if(f1 < f6)
            return 0.0F;
        if(f1 < f7)
        {
            float f8 = (f1 - f6) / (f7 - f6);
            float f9 = (f8 * mc4[i] + mc3[i]) * f8 + mc2[i];
            float f10 = (f8 * mc4[i + 1] + mc3[i + 1]) * f8 + mc2[i + 1];
            return (f9 + (f10 - f9) * f4) * f8 * f8;
        } else
        {
            return md[i] + (md[i + 1] - md[i]) * f4 + 0.5F * (f1 - f6);
        }
    }

    public void loadMachParams(SectFile sectfile)
        throws RuntimeException
    {
        try
        {
            float af[] = new float[8];
            StringTokenizer stringtokenizer = new StringTokenizer(sectfile.get("Polares", "mc3", ""), ",");
            for(int i = 0; i < 8; i++)
                mc3[i] = Float.parseFloat(stringtokenizer.nextToken());

            stringtokenizer = new StringTokenizer(sectfile.get("Polares", "mc4", ""), ",");
            for(int j = 0; j < 8; j++)
                mc4[j] = Float.parseFloat(stringtokenizer.nextToken());

            stringtokenizer = new StringTokenizer(sectfile.get("Polares", "mm", ""), ",");
            for(int k = 0; k < 8; k++)
                mm[k] = Float.parseFloat(stringtokenizer.nextToken());

            stringtokenizer = new StringTokenizer(sectfile.get("Polares", "mz", ""), ",");
            for(int l = 0; l < 8; l++)
                af[l] = Float.parseFloat(stringtokenizer.nextToken());

            for(int i1 = 0; i1 < 8; i1++)
            {
                mc4[i1] *= af[i1] * af[i1] * af[i1] * af[i1];
                mc3[i1] *= af[i1] * af[i1] * af[i1];
                mc2[i1] = 0.25F * (af[i1] - 6F * mc3[i1] - 8F * mc4[i1]);
                mc[i1] = mm[i1] - af[i1];
                if(mcMin > mc[i1])
                    mcMin = mc[i1];
                md[i1] = (mc4[i1] + mc3[i1] + mc2[i1]) - af[i1] * 0.5F;
            }

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            mcMin = 999F;
        }
    }

    protected float lastAOA;
    protected float lastCx;
    protected float lastCy;
    protected float Flaps;
    float normP[];
    float maxP[];
    public float AOA_crit;
    public float V_max;
    public float V_min;
    public float P_Vmax;
    public float G;
    public float S;
    public float K_max;
    public float Cy0_max;
    public float Tfac;
    public float Vyfac;
    public float FlapsMult;
    public float FlapsAngSh;
    public float Vz_climb;
    public float V_climb;
    public float T_turn;
    public float V_turn;
    public float V_maxFlaps;
    public float V_land;
    public float AOA_land;
    private float Ro;
    private float R1000;
    private float sign;
    public float AOALineH;
    public float AOALineL;
    public float AOACritH;
    public float AOACritL;
    public float parabCyCoeffH;
    public float parabCyCoeffL;
    public float Cy0;
    public float lineCyCoeff;
    public float declineCoeff;
    public float maxDistAng;
    public float parabAngle;
    public float CyCritH;
    public float CyCritL;
    public float AOAMinCx;
    public float AOAParabH;
    public float AOAParabL;
    public float CxMin;
    public float parabCxCoeff;
    public float AOACritH_0;
    public float AOACritL_0;
    public float Cy0_0;
    public float CyCritH_0;
    public float CyCritL_0;
    public float AOAMinCx_Shift;
    public float CxMin_0;
    public float parabCxCoeff_0;
    public float AOACritH_1;
    public float AOACritL_1;
    public float Cy0_1;
    public float CyCritH_1;
    public float CyCritL_1;
    public float CxMin_1;
    public float parabCxCoeff_1;
    float mc[] = {
        999F, 999F, 999F, 999F, 999F, 999F, 999F, 999F
    };
    float mc2[];
    float mc3[];
    float mc4[];
    float mm[];
    float md[];
    public float mcMin;
    private float lastMach;
    public float cdw;
}
