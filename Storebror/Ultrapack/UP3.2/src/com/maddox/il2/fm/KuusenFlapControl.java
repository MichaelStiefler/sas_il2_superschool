package com.maddox.il2.fm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class KuusenFlapControl extends com.maddox.il2.fm.Polares
{
    public class InclusiveLine
    {

        public float Aoa;
        public float Cy2;
        public float Cy;
        public float Cx;
        public float Flap;

        public InclusiveLine()
        {
            Aoa = 0.0F;
            Cy2 = 0.0F;
            Cy = 0.0F;
            Cx = 0.0F;
            Flap = 0.0F;
        }
    }


    public KuusenFlapControl(FlightModelMain fm, int airSpeedOffset)
    {
        lastAOA = fm.Wing.lastAOA;
        lastCx = fm.Wing.lastCx;
        lastCy = fm.Wing.lastCy;
        Flaps = fm.Wing.Flaps;
        AOA_crit = fm.Wing.AOA_crit;
        V_max = fm.Wing.V_max;
        V_min = fm.Wing.V_min;
        P_Vmax = fm.Wing.P_Vmax;
        G = fm.Wing.G;
        S = fm.Wing.S;
        K_max = fm.Wing.K_max;
        Cy0_max = fm.Wing.Cy0_max;
        Tfac = fm.Wing.Tfac;
        Vyfac = fm.Wing.Vyfac;
        FlapsMult = fm.Wing.FlapsMult;
        FlapsAngSh = fm.Wing.FlapsAngSh;
        Vz_climb = fm.Wing.Vz_climb;
        V_climb = fm.Wing.V_climb;
        T_turn = fm.Wing.T_turn;
        V_turn = fm.Wing.V_turn;
        V_maxFlaps = fm.Wing.V_maxFlaps;
        V_land = fm.Wing.V_land;
        AOA_land = fm.Wing.AOA_land;
        AOACritH = fm.Wing.AOACritH;
        AOACritL = fm.Wing.AOACritL;
        lineCyCoeff = fm.Wing.lineCyCoeff;
        declineCoeff = fm.Wing.declineCoeff;
        maxDistAng = fm.Wing.maxDistAng;
        parabAngle = fm.Wing.parabAngle;
        AOAMinCx = fm.Wing.AOAMinCx;
        AOACritH_0 = fm.Wing.AOACritH_0;
        AOACritL_0 = fm.Wing.AOACritL_0;
        Cy0_0 = fm.Wing.Cy0_0;
        CyCritH_0 = fm.Wing.CyCritH_0;
        CyCritL_0 = fm.Wing.CyCritL_0;
        AOAMinCx_Shift = fm.Wing.AOAMinCx_Shift;
        CxMin_0 = fm.Wing.CxMin_0;
        parabCxCoeff_0 = fm.Wing.parabCxCoeff_0;
        AOACritH_1 = fm.Wing.AOACritH_1;
        AOACritL_1 = fm.Wing.AOACritL_1;
        Cy0_1 = fm.Wing.Cy0_1;
        CyCritH_1 = fm.Wing.CyCritH_1;
        CyCritL_1 = fm.Wing.CyCritL_1;
        CxMin_1 = fm.Wing.CxMin_1;
        parabCxCoeff_1 = fm.Wing.parabCxCoeff_1;
        setFlaps(0.0F);
        for(int i = 0; i < 250; i++)
        {
            normP[i] = fm.Wing.normP[i];
            maxP[i] = fm.Wing.maxP[i];
        }

        maxWeight = fm.M.maxWeight * 9.8F;
        AirSpeedOffset = airSpeedOffset * -1;
        CreateInclusiveLine();
    }

    public void CreateInclusiveLine()
    {
        float Flap = 0.0F;
        float Cy = 0.0F;
        float Cx = 0.0F;
        InclusiveLineArray = new InclusiveLine[180];
        try
        {
            java.io.PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(com.maddox.rts.HomePath.toFileSystemName("InclusiveLine.txt", 0))));
            for(int i = -90; i < 90; i++)
                printwriter.print(i + "\t");

            printwriter.println();
            for(int iFlap = 0; iFlap <= 10; iFlap++)
            {
                Flap = (float)iFlap * 0.1F;
                setFlaps(Flap);
                printwriter.print("Flap" + Flap + "\t");
                printwriter.println();
                printwriter.flush();
                for(int i = -90; i < 90; i++)
                    printwriter.print(new_Cya(i) + "\t");

                printwriter.println();
                printwriter.flush();
                for(int i = -90; i < 90; i++)
                    printwriter.print(new_Cxa(i) + "\t");

                printwriter.println();
                printwriter.flush();
            }

            for(int iFlap = 0; iFlap <= 100; iFlap++)
            {
                Flap = (float)iFlap * 0.01F;
                setFlaps(Flap);
                if(iFlap == 0)
                {
                    for(int i = -90; i < 90; i++)
                    {
                        Cy = new_Cya(i);
                        Cx = new_Cxa(i);
                        InclusiveLineArray[i + 90] = new InclusiveLine();
                        InclusiveLineArray[i + 90].Aoa = i;
                        InclusiveLineArray[i + 90].Cy2 = Cy * (float)java.lang.Math.cos(com.maddox.il2.fm.FMMath.DEG2RAD(i));
                        InclusiveLineArray[i + 90].Cy = Cy;
                        InclusiveLineArray[i + 90].Cx = Cx;
                        InclusiveLineArray[i + 90].Flap = Flap;
                    }

                } else
                {
                    float Aoa = 0.0F;
                    float MaxCya = 0.0F;
                    boolean IsStall = false;
                    for(int i = 0; i < 90; i++)
                    {
                        if(MaxCya < InclusiveLineArray[i + 90].Cy)
                        {
                            MaxCya = InclusiveLineArray[i + 90].Cy;
                            IsStall = false;
                        } else
                        {
                            IsStall = true;
                            for(; Aoa < 90F; Aoa++)
                            {
                                MaxCya = new_Cya(Aoa);
                                if(MaxCya > InclusiveLineArray[i + 90].Cy)
                                    break;
                            }

                        }
                        Aoa = getAoAbyCy(MaxCya);
                        Cy = new_Cya(Aoa);
                        Cx = new_Cxa(Aoa);
                        if(IsStall || Cy / Cx > InclusiveLineArray[i + 90].Cy / InclusiveLineArray[i + 90].Cx)
                        {
                            InclusiveLineArray[i + 90].Aoa = Aoa;
                            InclusiveLineArray[i + 90].Cy2 = Cy * (float)java.lang.Math.cos(com.maddox.il2.fm.FMMath.DEG2RAD(Aoa));
                            InclusiveLineArray[i + 90].Cy = Cy;
                            InclusiveLineArray[i + 90].Cx = Cx;
                            InclusiveLineArray[i + 90].Flap = Flap;
                        }
                    }

                }
            }

        }
        catch(java.io.IOException ioexception)
        {
            java.lang.System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
        catch(java.lang.Exception exception)
        {
            java.lang.System.out.println("Exception: " + exception.getMessage());
            exception.printStackTrace();
        }
        setFlaps(0.0F);
    }

    public float GetKuusenFlapValue(float AirSpeed, float Gvalue, float Weight)
    {
        float FlapValue = 1.0F;
        if(Weight == 0.0F)
            Weight = maxWeight;
        float NeedCy = Gvalue * Weight;
        if(Gvalue <= 0.0F)
            NeedCy = 0.0F;
        AirSpeed -= AirSpeedOffset;
        float NeedCya = (2.0F * NeedCy) / (S * 1.225F * AirSpeed * AirSpeed);
        for(int i = -10; i < 90; i++)
        {
            if(InclusiveLineArray[i + 90].Cy2 < NeedCya)
                continue;
            FlapValue = InclusiveLineArray[i + 90].Flap;
            break;
        }

        return FlapValue;
    }

    public void drawPolareData(java.lang.String s)
    {
        float f = -10000F;
        try
        {
            java.io.PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(com.maddox.rts.HomePath.toFileSystemName(s, 0))));
            for(int i = -90; i < 90; i++)
                printwriter.print(i + "\t");

            printwriter.println();
            for(int i2 = 0; i2 <= 5; i2++)
            {
                setFlaps((float)i2 * 0.2F);
                float KCy0 = Cy0_0 + (Cy0_1 - Cy0_0) * (float)i2 * 0.2F;
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
                    float f10 = KCy0 + lineCyCoeff * (float)i1;
                    if((double)f10 < 2D && (double)f10 > -2D)
                        printwriter.print(f10 + "\t");
                    else
                        printwriter.print("\t");
                }

                printwriter.println();
            }

            printwriter.close();
        }
        catch(java.io.IOException ioexception)
        {
            java.lang.System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
    }

    public InclusiveLine InclusiveLineArray[];
    public int AirSpeedOffset;
    float maxWeight;
}
