package com.maddox.il2.fm;

public class KuusenFlapControl extends Polares {
    public class InclusiveLine {

        public float Aoa;
        public float Cy2;
        public float Cy;
        public float Cx;
        public float Flap;

        public InclusiveLine() {
            this.Aoa = 0.0F;
            this.Cy2 = 0.0F;
            this.Cy = 0.0F;
            this.Cx = 0.0F;
            this.Flap = 0.0F;
        }
    }

    public KuusenFlapControl(FlightModelMain fm, int airSpeedOffset) {
        this.lastAOA = fm.Wing.lastAOA;
        this.lastCx = fm.Wing.lastCx;
        this.lastCy = fm.Wing.lastCy;
        this.Flaps = fm.Wing.Flaps;
        this.AOA_crit = fm.Wing.AOA_crit;
        this.V_max = fm.Wing.V_max;
        this.V_min = fm.Wing.V_min;
        this.P_Vmax = fm.Wing.P_Vmax;
        this.G = fm.Wing.G;
        this.S = fm.Wing.S;
        this.K_max = fm.Wing.K_max;
        this.Cy0_max = fm.Wing.Cy0_max;
        this.Tfac = fm.Wing.Tfac;
        this.Vyfac = fm.Wing.Vyfac;
        this.FlapsMult = fm.Wing.FlapsMult;
        this.FlapsAngSh = fm.Wing.FlapsAngSh;
        this.Vz_climb = fm.Wing.Vz_climb;
        this.V_climb = fm.Wing.V_climb;
        this.T_turn = fm.Wing.T_turn;
        this.V_turn = fm.Wing.V_turn;
        this.V_maxFlaps = fm.Wing.V_maxFlaps;
        this.V_land = fm.Wing.V_land;
        this.AOA_land = fm.Wing.AOA_land;
        this.AOACritH = fm.Wing.AOACritH;
        this.AOACritL = fm.Wing.AOACritL;
        this.lineCyCoeff = fm.Wing.lineCyCoeff;
        this.declineCoeff = fm.Wing.declineCoeff;
        this.maxDistAng = fm.Wing.maxDistAng;
        this.parabAngle = fm.Wing.parabAngle;
        this.AOAMinCx = fm.Wing.AOAMinCx;
        this.AOACritH_0 = fm.Wing.AOACritH_0;
        this.AOACritL_0 = fm.Wing.AOACritL_0;
        this.Cy0_0 = fm.Wing.Cy0_0;
        this.CyCritH_0 = fm.Wing.CyCritH_0;
        this.CyCritL_0 = fm.Wing.CyCritL_0;
        this.AOAMinCx_Shift = fm.Wing.AOAMinCx_Shift;
        this.CxMin_0 = fm.Wing.CxMin_0;
        this.parabCxCoeff_0 = fm.Wing.parabCxCoeff_0;
        this.AOACritH_1 = fm.Wing.AOACritH_1;
        this.AOACritL_1 = fm.Wing.AOACritL_1;
        this.Cy0_1 = fm.Wing.Cy0_1;
        this.CyCritH_1 = fm.Wing.CyCritH_1;
        this.CyCritL_1 = fm.Wing.CyCritL_1;
        this.CxMin_1 = fm.Wing.CxMin_1;
        this.parabCxCoeff_1 = fm.Wing.parabCxCoeff_1;
        this.setFlaps(0.0F);
        for (int i = 0; i < 250; i++) {
            this.normP[i] = fm.Wing.normP[i];
            this.maxP[i] = fm.Wing.maxP[i];
        }

        this.maxWeight = fm.M.maxWeight * 9.8F;
        this.AirSpeedOffset = airSpeedOffset * -1;
        this.CreateInclusiveLine();
    }

    public void CreateInclusiveLine() {
        float Flap = 0.0F;
        float Cy = 0.0F;
        float Cx = 0.0F;
        this.InclusiveLineArray = new InclusiveLine[180];
        try {
/*            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("InclusiveLine.txt", 0))));
            for (int i = -90; i < 90; i++)
                printwriter.print(i + "\t");

            printwriter.println();
            for (int iFlap = 0; iFlap <= 10; iFlap++) {
                Flap = iFlap * 0.1F;
                this.setFlaps(Flap);
                printwriter.print("Flap" + Flap + "\t");
                printwriter.println();
                printwriter.flush();
                for (int i = -90; i < 90; i++)
                    printwriter.print(this.new_Cya(i) + "\t");

                printwriter.println();
                printwriter.flush();
                for (int i = -90; i < 90; i++)
                    printwriter.print(this.new_Cxa(i) + "\t");

                printwriter.println();
                printwriter.flush();
            } */

            for (int iFlap = 0; iFlap <= 100; iFlap++) {
                Flap = iFlap * 0.01F;
                this.setFlaps(Flap);
                if (iFlap == 0) for (int i = -90; i < 90; i++) {
                    Cy = this.new_Cya(i);
                    Cx = this.new_Cxa(i);
                    this.InclusiveLineArray[i + 90] = new InclusiveLine();
                    this.InclusiveLineArray[i + 90].Aoa = i;
                    this.InclusiveLineArray[i + 90].Cy2 = Cy * (float) Math.cos(FMMath.DEG2RAD(i));
                    this.InclusiveLineArray[i + 90].Cy = Cy;
                    this.InclusiveLineArray[i + 90].Cx = Cx;
                    this.InclusiveLineArray[i + 90].Flap = Flap;
                }
                else {
                    float Aoa = 0.0F;
                    float MaxCya = 0.0F;
                    boolean IsStall = false;
                    for (int i = 0; i < 90; i++) {
                        if (MaxCya < this.InclusiveLineArray[i + 90].Cy) {
                            MaxCya = this.InclusiveLineArray[i + 90].Cy;
                            IsStall = false;
                        } else {
                            IsStall = true;
                            for (; Aoa < 90F; Aoa++) {
                                MaxCya = this.new_Cya(Aoa);
                                if (MaxCya > this.InclusiveLineArray[i + 90].Cy) break;
                            }

                        }
                        Aoa = this.getAoAbyCy(MaxCya);
                        Cy = this.new_Cya(Aoa);
                        Cx = this.new_Cxa(Aoa);
                        if (IsStall || Cy / Cx > this.InclusiveLineArray[i + 90].Cy / this.InclusiveLineArray[i + 90].Cx) {
                            this.InclusiveLineArray[i + 90].Aoa = Aoa;
                            this.InclusiveLineArray[i + 90].Cy2 = Cy * (float) Math.cos(FMMath.DEG2RAD(Aoa));
                            this.InclusiveLineArray[i + 90].Cy = Cy;
                            this.InclusiveLineArray[i + 90].Cx = Cx;
                            this.InclusiveLineArray[i + 90].Flap = Flap;
                        }
                    }

                }
            }

//        } catch (IOException ioexception) {
//            System.out.println("File save failed: " + ioexception.getMessage());
//            ioexception.printStackTrace();
        } catch (Exception exception) {
            System.out.println("Exception: " + exception.getMessage());
            exception.printStackTrace();
        }
        this.setFlaps(0.0F);
    }

    public float GetKuusenFlapValue(float AirSpeed, float Gvalue, float Weight) {
        float FlapValue = 1.0F;
        if (Weight == 0.0F) Weight = this.maxWeight;
        float NeedCy = Gvalue * Weight;
        if (Gvalue <= 0.0F) NeedCy = 0.0F;
        AirSpeed -= this.AirSpeedOffset;
        float NeedCya = 2.0F * NeedCy / (this.S * 1.225F * AirSpeed * AirSpeed);
        for (int i = -10; i < 90; i++) {
            if (this.InclusiveLineArray[i + 90].Cy2 < NeedCya) continue;
            FlapValue = this.InclusiveLineArray[i + 90].Flap;
            break;
        }

        return FlapValue;
    }

/*    public void drawPolareData(String s) {
        float f = -10000F;
        try {
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
            for (int i = -90; i < 90; i++)
                printwriter.print(i + "\t");

            printwriter.println();
            for (int i2 = 0; i2 <= 5; i2++) {
                this.setFlaps(i2 * 0.2F);
                float KCy0 = this.Cy0_0 + (this.Cy0_1 - this.Cy0_0) * i2 * 0.2F;
                for (int j = -90; j < 90; j++)
                    printwriter.print(this.new_Cya(j) + "\t");

                printwriter.println();
                for (int k = -90; k < 90; k++)
                    printwriter.print(this.new_Cxa(k) + "\t");

                printwriter.println();
                if (i2 == 0) {
                    for (int l = -90; l < 90; l++) {
                        float f9 = this.new_Cya(l) / this.new_Cxa(l);
                        printwriter.print(f9 * 0.1F + "\t");
                        if (f < f9) f = f9;
                    }

                    printwriter.println();
                }
                for (int i1 = -90; i1 < 90; i1++) {
                    float f10 = KCy0 + this.lineCyCoeff * i1;
                    if (f10 < 2D && f10 > -2D) printwriter.print(f10 + "\t");
                    else printwriter.print("\t");
                }

                printwriter.println();
            }

            printwriter.close();
        } catch (IOException ioexception) {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
    }*/

    public InclusiveLine InclusiveLineArray[];
    public int           AirSpeedOffset;
    float                maxWeight;
}
