// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 27/06/2013 22:00:59
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FlightModelMain.java

package com.maddox.il2.fm;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.*;
import java.io.*;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.fm:
//            FMMath, Atmosphere, AircraftState, Controls, 
//            Mass, Squares, Arm, FlightModel, 
//            EnginesInterface, Gear, Supersonic, Polares, 
//            Turret, RealFlightModel, Motor, Autopilotage

public class FlightModelMain extends FMMath
{

    public float getSpeedKMH()
    {
        return (float)(((Tuple3d) (Vflow)).x * 3.6000000000000001D);
    }

    public float getSpeed()
    {
        return (float)((Tuple3d) (Vflow)).x;
    }

    public void getSpeed(Vector3d vector3d)
    {
        vector3d.set(Vair);
    }

    public float getVertSpeed()
    {
        return (float)((Tuple3d) (Vair)).z;
    }

    public float getAltitude()
    {
        return (float)((Tuple3d) (Loc)).z;
    }

    public float getAOA()
    {
        return AOA;
    }

    public float getAOS()
    {
        return AOS;
    }

    public void getLoc(Point3f point3f)
    {
        point3f.set(Loc);
    }

    public void getLoc(Point3d point3d)
    {
        point3d.set(Loc);
    }

    public void getOrient(Orientation orientation)
    {
        orientation.set(Or);
    }

    public Vector3d getW()
    {
        return W;
    }

    public Vector3d getAW()
    {
        return AW;
    }

    public Vector3d getAccel()
    {
        return Accel;
    }

    public float getSideAccel()
    {
        return (float)((Tuple3d) (ACmeter)).y;
    }

    public Vector3d getLocalAccel()
    {
        return LocalAccel;
    }

    public Vector3d getBallAccel()
    {
        TmpV.set(LocalAccel);
        if(Vwld.lengthSquared() < 10D)
        {
            double d = Vwld.lengthSquared() - 5D;
            if(d < 0.0D)
                d = 0.0D;
            TmpV.scale(0.20000000000000001D * d);
        }
        TmpA.set(0.0D, 0.0D, -Atmosphere.g());
        Or.transformInv(TmpA);
        TmpA.sub(TmpV);
        return TmpA;
    }

    public Vector3d getAM()
    {
        return AM;
    }

    public Vector3d getVflow()
    {
        return Vflow;
    }

    public void load(String s)
    {
        String s1 = "Error loading params from " + s;
        SectFile sectfile = sectFile(s);
        String s2 = "Aircraft";
        Wingspan = sectfile.get(s2, "Wingspan", 0.0F);
        if(Wingspan == 0.0F)
            throw new RuntimeException(s1);
        Length = sectfile.get(s2, "Length", 0.0F);
        if(Length == 0.0F)
            throw new RuntimeException(s1);
        Scheme = sectfile.get(s2, "Type", -1);
        if(Scheme == -1)
            throw new RuntimeException(s1);
        crew = sectfile.get(s2, "Crew", 0, 0, 9);
        if(crew == 0)
            throw new RuntimeException(s1);
        for(int k = 0; k < AS.astatePilotFunctions.length; k++)
        {
            int i = sectfile.get(s2, "CrewFunction" + k, -1);
            if(i != -1)
                AS.astatePilotFunctions[k] = (byte)i;
        }

        int j = sectfile.get("Controls", "CDiveBrake", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        AIRBRAKE = j == 1;
        s2 = "Controls";
        j = sectfile.get(s2, "CAileron", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasAileronControl = j == 1;
        j = sectfile.get(s2, "CAileronTrim", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasAileronTrim = j == 1;
        j = sectfile.get(s2, "CElevator", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasElevatorControl = j == 1;
        j = sectfile.get(s2, "CElevatorTrim", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasElevatorTrim = j == 1;
        j = sectfile.get(s2, "CRudder", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasRudderControl = j == 1;
        j = sectfile.get(s2, "CRudderTrim", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasRudderTrim = j == 1;
        j = sectfile.get(s2, "CFlap", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasFlapsControl = j == 1;
        j = sectfile.get(s2, "CFlapPos", -1);
        if(j < 0 || j > 3)
            throw new RuntimeException(s1);
        CT.bHasFlapsControlRed = j < 3;
        j = sectfile.get(s2, "CDiveBrake", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasAirBrakeControl = j == 1;
        j = sectfile.get(s2, "CUndercarriage", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasGearControl = j == 1;
        j = sectfile.get(s2, "CArrestorHook", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasArrestorControl = j == 1;
        j = sectfile.get(s2, "CWingFold", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasWingControl = j == 1;
        j = sectfile.get(s2, "CCockpitDoor", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasCockpitDoorControl = j == 1;
        j = sectfile.get(s2, "CWheelBrakes", 1);
        CT.bHasBrakeControl = j == 1;
        j = sectfile.get(s2, "CLockTailwheel", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasLockGearControl = j == 1;
        CT.AilThr = 0.27778F * sectfile.get(s2, "CAileronThreshold", 360F);
        CT.RudThr = 0.27778F * sectfile.get(s2, "CRudderThreshold", 360F);
        CT.ElevThr = 0.27778F * sectfile.get(s2, "CElevatorThreshold", 403.2F);
        CT.CalcTresholds();
        j = sectfile.get(s2, "CBombBay", 0);
        if(j != 0 && j != 1)
            throw new RuntimeException(s1);
        CT.bHasBayDoorControl = j == 1;
        CT.setTrimAileronControl(sectfile.get(s2, "DefaultAileronTrim", -999F));
        if(CT.getTrimAileronControl() == -999F)
            throw new RuntimeException(s1);
        if(!CT.bHasElevatorTrim)
        {
            CT.setTrimElevatorControl(sectfile.get(s2, "DefaultElevatorTrim", -999F));
            if(CT.getTrimElevatorControl() == -999F)
                throw new RuntimeException(s1);
        }
        CT.setTrimRudderControl(sectfile.get(s2, "DefaultRudderTrim", -999F));
        if(CT.getTrimRudderControl() == -999F)
            throw new RuntimeException(s1);
        if(!CT.bHasGearControl)
        {
            GearCX = 0.0F;
            CT.GearControl = 1.0F;
            CT.setFixedGear(true);
        }
        if(!CT.bHasFlapsControl)
            CT.FlapsControl = 0.0F;
        j = sectfile.get(s2, "cElectricProp", 0);
        CT.bUseElectricProp = j == 1;
        float f = sectfile.get(s2, "GearPeriod", -999F);
        if(f != -999F)
            CT.dvGear = 1.0F / f;
        f = sectfile.get(s2, "WingPeriod", -999F);
        if(f != -999F)
            CT.dvWing = 1.0F / f;
        f = sectfile.get(s2, "CockpitDoorPeriod", -999F);
        if(f != -999F)
            CT.dvCockpitDoor = 1.0F / f;
        switch(Scheme)
        {
        default:
            throw new RuntimeException("Invalid Plane Scheme (Can't Get There!)..");

        case 0: // '\0'
        case 1: // '\001'
            float f1 = Length * 0.35F;
            f1 *= f1;
            float f11 = Length * 0.125F;
            f11 *= f11;
            float f6 = Wingspan * 0.2F;
            f6 *= f6;
            float f16 = Length * 0.07F;
            f16 *= f16;
            J0.z = f1 * 0.2F + f11 * 0.4F + f6 * 0.4F;
            J0.y = f1 * 0.2F + f11 * 0.4F + f16 * 0.4F;
            J0.x = f16 * 0.6F + f6 * 0.4F;
            break;

        case 2: // '\002'
            float f2 = Length * 0.35F;
            f2 *= f2;
            float f12 = Length * 0.125F;
            f12 *= f12;
            float f7 = Wingspan * 0.2F;
            f7 *= f7;
            float f17 = Length * 0.07F;
            f17 *= f17;
            J0.z = f2 * 0.2F + f12 * 0.1F + f7 * 0.7F;
            J0.y = f2 * 0.2F + f12 * 0.1F + f17 * 0.7F;
            J0.x = f17 * 0.3F + f7 * 0.7F;
            break;

        case 3: // '\003'
            float f3 = Length * 0.35F;
            f3 *= f3;
            float f13 = Length * 0.125F;
            f13 *= f13;
            float f8 = Wingspan * 0.2F;
            f8 *= f8;
            float f18 = Length * 0.07F;
            f18 *= f18;
            J0.z = f3 * 0.2F + f13 * 0.2F + f8 * 0.6F;
            J0.y = f3 * 0.2F + f13 * 0.2F + f18 * 0.6F;
            J0.x = f18 * 0.2F + f8 * 0.8F;
            break;

        case 4: // '\004'
        case 5: // '\005'
        case 7: // '\007'
            float f4 = Length * 0.35F;
            f4 *= f4;
            float f14 = Length * 0.125F;
            f14 *= f14;
            float f9 = Wingspan * 0.2F;
            f9 *= f9;
            float f19 = Length * 0.07F;
            f19 *= f19;
            J0.z = f4 * 0.25F + f14 * 0.15F + f9 * 0.6F;
            J0.y = f4 * 0.25F + f14 * 0.15F + f19 * 0.6F;
            J0.x = f19 * 0.4F + f9 * 0.6F;
            break;

        case 6: // '\006'
            float f5 = Length * 0.35F;
            f5 *= f5;
            float f15 = Length * 0.125F;
            f15 *= f15;
            float f10 = Wingspan * 0.2F;
            f10 *= f10;
            float f20 = Length * 0.07F;
            f20 *= f20;
            J0.z = f5 * 0.25F + f15 * 0.15F + f10 * 0.6F;
            J0.y = f5 * 0.25F + f15 * 0.15F + f20 * 0.6F;
            J0.x = f20 * 0.4F + f10 * 0.6F;
            break;
        }
        s2 = "Params";
        if(sectfile.exist(s2, "ReferenceWeight"))
            refM = sectfile.get(s2, "ReferenceWeight", 0.0F, -2000F, 2000F);
        else
            refM = 0.0F;
        M.load(sectfile, this);
        Sq.load(sectfile);
        Arms.load(sectfile);
        Aircraft.debugprintln(super.actor, "Calling engines interface to resolve file '" + sectfile.toString() + "'....");
        EI.load((FlightModel)this, sectfile);
        Gears.load(sectfile);
        Ss.load(sectfile);
        if(sectfile.exist(s2, "G_CLASS"))
        {
            LimitLoad = sectfile.get(s2, "G_CLASS", 12F, 0.0F, 15F);
            LimitLoad /= 1.5F;
        } else
        {
            LimitLoad = 12F;
        }
        if(sectfile.exist(s2, "G_CLASS_COEFF"))
            G_ClassCoeff = sectfile.get(s2, "G_CLASS_COEFF", 20F, -30F, 50F);
        else
            G_ClassCoeff = 20F;
        float f21 = M.maxWeight * Atmosphere.g();
        float f22 = Sq.squareWing;
        Vmax = sectfile.get(s2, "Vmax", 1.0F);
        VmaxH = sectfile.get(s2, "VmaxH", 1.0F);
        Vmin = sectfile.get(s2, "Vmin", 1.0F);
        HofVmax = sectfile.get(s2, "HofVmax", 1.0F);
        VmaxFLAPS = sectfile.get(s2, "VmaxFLAPS", 1.0F);
        VminFLAPS = sectfile.get(s2, "VminFLAPS", 1.0F);
        SensYaw = sectfile.get(s2, "SensYaw", 1.0F);
        SensPitch = sectfile.get(s2, "SensPitch", 1.0F);
        SensRoll = sectfile.get(s2, "SensRoll", 1.0F);
        VmaxAllowed = sectfile.get(s2, "VmaxAllowed", VmaxH * 1.3F);
        Range = sectfile.get(s2, "Range", 800F);
        CruiseSpeed = sectfile.get(s2, "CruiseSpeed", 0.7F * Vmax);
        FuelConsumption = M.maxFuel / (0.64F * ((Range / CruiseSpeed) * 3600F) * (float)EI.getNum());
        Vmax *= 0.2777778F;
        VmaxH *= 0.2777778F;
        Vmin *= 0.2777778F;
        VmaxFLAPS *= 0.2777778F;
        VminFLAPS *= 0.2416667F;
        VmaxAllowed *= 0.2777778F;
        Fusel.lineCyCoeff = 0.02F;
        Fusel.AOAMinCx_Shift = 0.0F;
        Fusel.Cy0_0 = 0.0F;
        Fusel.AOACritH_0 = 17F;
        Fusel.AOACritL_0 = -17F;
        Fusel.CyCritH_0 = 0.2F;
        Fusel.CyCritL_0 = -0.2F;
        Fusel.parabCxCoeff_0 = 0.0006F;
        Fusel.CxMin_0 = 0.0F;
        Fusel.Cy0_1 = 0.0F;
        Fusel.AOACritH_1 = 17F;
        Fusel.AOACritL_1 = -17F;
        Fusel.CyCritH_1 = 0.2F;
        Fusel.CyCritL_1 = -0.2F;
        Fusel.CxMin_1 = 0.0F;
        Fusel.parabCxCoeff_1 = 0.0006F;
        Fusel.declineCoeff = 0.007F;
        Fusel.maxDistAng = 30F;
        Fusel.parabAngle = 5F;
        Fusel.setFlaps(0.0F);
        Tail.lineCyCoeff = 0.085F;
        Tail.AOAMinCx_Shift = 0.0F;
        Tail.Cy0_0 = 0.0F;
        Tail.AOACritH_0 = 17F;
        Tail.AOACritL_0 = -17F;
        Tail.CyCritH_0 = 1.1F;
        Tail.CyCritL_0 = -1.1F;
        Tail.parabCxCoeff_0 = 0.0006F;
        Tail.CxMin_0 = 0.02F;
        Tail.Cy0_1 = 0.0F;
        Tail.AOACritH_1 = 17F;
        Tail.AOACritL_1 = -17F;
        Tail.CyCritH_1 = 1.1F;
        Tail.CyCritL_1 = -1.1F;
        Tail.CxMin_1 = 0.02F;
        Tail.parabCxCoeff_1 = 0.0006F;
        Tail.declineCoeff = 0.007F;
        Tail.maxDistAng = 30F;
        Tail.parabAngle = 5F;
        Tail.setFlaps(0.0F);
        s2 = "Params";
        Wing.AOA_crit = sectfile.get(s2, "CriticalAOA", 16F);
        Wing.V_max = 0.27778F * sectfile.get(s2, "Vmax", 500F);
        Wing.V_min = 0.27778F * sectfile.get(s2, "Vmin", 160F);
        Wing.V_maxFlaps = 0.27778F * sectfile.get(s2, "VmaxFLAPS", 270F);
        Wing.V_land = 0.27778F * sectfile.get(s2, "VminFLAPS", 140F);
        Wing.T_turn = sectfile.get(s2, "T_turn", 20F);
        Wing.V_turn = 0.27778F * sectfile.get(s2, "V_turn", 300F);
        Wing.Vz_climb = sectfile.get(s2, "Vz_climb", 18F);
        Wing.V_climb = 0.27778F * sectfile.get(s2, "V_climb", 270F);
        Wing.K_max = sectfile.get(s2, "K_max", 14F);
        Wing.Cy0_max = sectfile.get(s2, "Cy0_max", 0.15F);
        Wing.FlapsMult = sectfile.get(s2, "FlapsMult", 0.16F);
        Wing.FlapsAngSh = sectfile.get(s2, "FlapsAngSh", 4F);
        Wing.P_Vmax = EI.forcePropAOA(Wing.V_max, 0.0F, 1.1F, true);
        Wing.S = f22;
        Wing.G = f21;
        s2 = "Polares";
        f = sectfile.get(s2, "lineCyCoeff", -999F);
        if(f != -999F)
        {
            Wing.lineCyCoeff = f;
            Wing.AOAMinCx_Shift = sectfile.get(s2, "AOAMinCx_Shift", 0.0F);
            Wing.Cy0_0 = sectfile.get(s2, "Cy0_0", 0.15F);
            Wing.AOACritH_0 = sectfile.get(s2, "AOACritH_0", 16F);
            Wing.AOACritL_0 = sectfile.get(s2, "AOACritL_0", -16F);
            Wing.CyCritH_0 = sectfile.get(s2, "CyCritH_0", 1.1F);
            Wing.CyCritL_0 = sectfile.get(s2, "CyCritL_0", -0.8F);
            Wing.parabCxCoeff_0 = sectfile.get(s2, "parabCxCoeff_0", 0.0008F);
            Wing.CxMin_0 = sectfile.get(s2, "CxMin_0", 0.026F);
            Wing.Cy0_1 = sectfile.get(s2, "Cy0_1", 0.65F);
            Wing.AOACritH_1 = sectfile.get(s2, "AOACritH_1", 15F);
            Wing.AOACritL_1 = sectfile.get(s2, "AOACritL_1", -18F);
            Wing.CyCritH_1 = sectfile.get(s2, "CyCritH_1", 1.6F);
            Wing.CyCritL_1 = sectfile.get(s2, "CyCritL_1", -0.75F);
            Wing.CxMin_1 = sectfile.get(s2, "CxMin_1", 0.09F);
            Wing.parabCxCoeff_1 = sectfile.get(s2, "parabCxCoeff_1", 0.0025F);
            Wing.parabAngle = sectfile.get(s2, "parabAngle", 5F);
            Wing.declineCoeff = sectfile.get(s2, "Decline", 0.007F);
            Wing.maxDistAng = sectfile.get(s2, "maxDistAng", 30F);
            Wing.setFlaps(0.0F);
            Wing.loadMachParams(sectfile);
        } else
        {
            throw new RuntimeException(s1);
        }
        AOA_Crit = sectfile.get(s2, "AOACritH_0", -999F);
        int l = s.indexOf(':');
        String s3;
        if(-1 != l)
            s3 = s.substring(0, l);
        else
            s3 = s;
        turnFile = s3.substring(0, s3.length() - 4);
        speedFile = s3.substring(0, s3.length() - 4);
    }

    public void drawData()
    {
        Wing.G = M.getFullMass() * Atmosphere.g();
        Wing.S = Sq.squareWing;
        for(int i = 0; i <= 75; i += 25)
        {
            Wing.G = (M.getFullMass() - (M.maxFuel * (float)i) / 100F) * Atmosphere.g();
            for(int k = 0; k < 250; k++)
            {
                Wing.normP[k] = EI.forcePropAOA(k, 0.0F, 1.0F, false);
                Wing.maxP[k] = EI.forcePropAOA(k, 1000F, 1.1F, true);
            }

            String s1 = turnFile;
            Wing.setFlaps(0.0F);
            Wing.drawGraphs(s1, i);
            Wing.setFlaps(0.0F);
            s1 = speedFile + "_MA520_" + (100 - i) + "fuel" + "_speed.txt";
            drawSpeed(s1, 1.0F, 1.1F, 1.0F);
            Wing.setFlaps(0.0F);
            String s2 = speedFile + "_MA520_avail_thrust.txt";
            String s3 = speedFile + "_MA520_thrust_summary" + (100 - i) + ".txt";
            String s4 = speedFile + "_MA520_req_thrust" + (100 - i) + ".txt";
            drawZhukovski(s2, s3, s4);
            Wing.setFlaps(0.0F);
        }

        String s = speedFile + "_MA520_data.txt";
        PrintWriter printwriter = null;
        try
        {
            printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
            printwriter.println("***MASS DATA***");
            printwriter.println("Empty: " + M.massEmpty);
            printwriter.println("Takeoff (reference only, not used directly in game): " + M.maxWeight);
            printwriter.println("Max fuel: " + M.maxFuel);
            if(M.maxNitro > 0.0F)
                printwriter.println("Max nitro: " + M.maxNitro);
            printwriter.println("");
            printwriter.println("***POWER PLANT DATA***");
            printwriter.println("Number of engines: " + EI.engines.length);
            for(int j = 0; j < EI.engines.length; j++)
                if(EI.engines[j] != null)
                {
                    printwriter.println("*Engine " + (j + 1) + " data*");
                    EI.engines[j].DataToFile(printwriter);
                }

            printwriter.println("");
            printwriter.println("***CONTROLS DATA***");
            printwriter.println("Aileron trim: " + (CT.bHasAileronTrim ? "+" : "-"));
            printwriter.println("Elevator trim: " + (CT.bHasElevatorTrim ? "+" : "-"));
            printwriter.println("Rudder trim: " + (CT.bHasRudderTrim ? "+" : "-"));
            printwriter.println("Flap: " + (CT.bHasFlapsControl ? "+" : "-"));
            printwriter.println("Flap 3-positions: " + (CT.bHasFlapsControlRed ? "-" : "+"));
            printwriter.println("Airbrake: " + (CT.bHasAirBrakeControl ? "+" : "-"));
            printwriter.println("Retractable gear: " + (CT.bHasGearControl ? "+" : "-"));
            printwriter.println("Arrestor hook: " + (CT.bHasArrestorControl ? "+" : "-"));
            printwriter.println("Wing fold: " + (CT.bHasWingControl ? "+" : "-"));
            printwriter.println("Cockpit door: " + (CT.bHasCockpitDoorControl ? "+" : "-"));
            printwriter.println("Wheel brakes: " + (CT.bHasBrakeControl ? "+" : "-"));
            printwriter.println("Tail gear lock: " + (CT.bHasLockGearControl ? "+" : "-"));
            printwriter.println("");
            printwriter.println("***COMMON DATA***");
            printwriter.println("Crew: " + crew);
            printwriter.println("HofVmax: " + HofVmax);
            printwriter.println("Vmax0: " + (int)((double)Vmax * 3.6000000000000001D));
            printwriter.println("VmaxH: " + (int)((double)VmaxH * 3.6000000000000001D));
            printwriter.println("Vmin: " + (int)((double)Vmin * 3.6000000000000001D));
            printwriter.println("VminFLAPS_MAXRPM: " + (int)((double)VminFLAPS * 3.6000000000000001D));
            printwriter.println("VminFLAPS_MINRPM: " + (int)((double)Wing.V_land * 3.6000000000000001D));
            printwriter.println("VmaxFLAPS: " + (int)((double)VmaxFLAPS * 3.6000000000000001D));
            printwriter.println("VmaxAllowed: " + (int)((double)VmaxAllowed * 3.6000000000000001D));
            printwriter.println("V_turn: " + (int)((double)Wing.V_turn * 3.6000000000000001D));
            printwriter.println("V_climb: " + (int)((double)Wing.V_climb * 3.6000000000000001D));
            printwriter.println("AOA_crit: " + Wing.AOA_crit);
            printwriter.println("T_turn: " + Wing.T_turn);
            printwriter.println("Vz_climb: " + Wing.Vz_climb);
            printwriter.println("K_max: " + Wing.K_max);
            printwriter.println("Cy0_max: " + Wing.Cy0_max);
            printwriter.println("FlapsMult: " + Wing.FlapsMult);
            printwriter.println("FlapsAngSh: " + Wing.FlapsAngSh);
            printwriter.println("");
            printwriter.println("***AERODYNAMICS DATA***");
            printwriter.println("Flaps-independent data:");
            printwriter.println("Wing square: " + Wing.S);
            printwriter.println("lineCyCoeff: " + Wing.lineCyCoeff);
            printwriter.println("parabAngle: " + Wing.parabAngle);
            printwriter.println("declineCoeff: " + Wing.declineCoeff);
            printwriter.println("maxDistAng: " + Wing.maxDistAng);
            printwriter.println("AOAMinCx_Shift: " + Wing.AOAMinCx_Shift);
            printwriter.println("No Flaps data:");
            printwriter.println("Cy0_0: " + Wing.Cy0_0);
            printwriter.println("AOACritH_0: " + Wing.AOACritH_0);
            printwriter.println("AOACritL_0: " + Wing.AOACritL_0);
            printwriter.println("CyCritH_0: " + Wing.CyCritH_0);
            printwriter.println("CyCritL_0: " + Wing.CyCritL_0);
            printwriter.println("parabCxCoeff_0: " + Wing.parabCxCoeff_0);
            printwriter.println("CxMin_0: " + Wing.CxMin_0);
            printwriter.println("Maximum Flaps data:");
            printwriter.println("Cy0_1: " + Wing.Cy0_1);
            printwriter.println("AOACritH_1: " + Wing.AOACritH_1);
            printwriter.println("AOACritL_1: " + Wing.AOACritL_1);
            printwriter.println("CyCritH_1: " + Wing.CyCritH_1);
            printwriter.println("CyCritL_1: " + Wing.CyCritL_1);
            printwriter.println("parabCxCoeff_1: " + Wing.parabCxCoeff_1);
            printwriter.println("CxMin_1: " + Wing.CxMin_1);
        }
        catch(IOException ioexception)
        {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
        finally
        {
            if(printwriter != null)
                printwriter.close();
        }
        Wing.G = M.maxWeight * Atmosphere.g();
        return;
    }

    private void drawZhukovski(String s, String s1, String s2)
    {
        PrintWriter printwriter = null;
        PrintWriter printwriter1 = null;
        PrintWriter printwriter2 = null;
        try
        {
            printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
            printwriter1 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s1, 0))));
            printwriter2 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s2, 0))));
            int i = 0;
            int j = 0;
            int k = 0;
            int l = 0;
            int i1 = 0;
            int j1 = 0;
            float f = 0.0F;
            int k1 = 0;
            int l1 = 0;
            for(int i2 = 0; i2 <= 1500; i2++)
            {
                printwriter.print("\t" + i2);
                printwriter2.print("\t" + i2);
            }

            printwriter1.println("Height\tMaxThrust\tSpeedMaxThrust\tMaxClimb\tSpeedMaxClimb\tMaxSpeed");
            for(int j2 = 0; j2 <= 10000; j2 += 100)
            {
                int k2 = 0;
                int l2 = 0;
                float f1 = 0.0F;
                int i3 = 0;
                int j3 = 0;
                boolean flag = false;
                printwriter.println();
                printwriter.print(j2);
                printwriter2.println();
                printwriter2.print(j2);
                for(int k3 = 0; k3 <= 1500; k3++)
                {
                    float f2 = (float)k3 * 0.27778F;
                    int l3 = (int)EI.forcePropAOA(f2, j2, 1.1F, true);
                    if(l3 <= 0)
                        break;
                    printwriter.print("\t" + l3);
                    if(l3 > k2)
                    {
                        k2 = l3;
                        l2 = k3;
                    }
                    int i4 = 0x186a0;
                    if(k3 >= 50)
                    {
                        float f3 = Wing.getClimb(f2, j2, l3);
                        if(f3 > f1)
                        {
                            f1 = f3;
                            i3 = k3;
                        }
                        if(f3 > 0.0F && !flag)
                            flag = true;
                        if(f3 < 0.0F && flag && j3 == 0)
                            j3 = k3;
                        i4 = l3 - (int)((f3 * Wing.G) / f2);
                    }
                    printwriter2.print("\t" + i4);
                }

                if(k2 > i)
                {
                    i = k2;
                    j = j2;
                    k = l2;
                }
                if(f1 > f)
                {
                    f = f1;
                    k1 = i3;
                    l1 = j2;
                }
                if(j3 > i1)
                {
                    i1 = j3;
                    j1 = j2;
                }
                if(j2 == 0)
                    l = j3;
                printwriter1.println(j2 + "\t" + k2 + "\t" + l2 + "\t" + f1 + "\t" + i3 + "\t" + j3);
            }

            printwriter1.println("\tSummary");
            printwriter1.println("Maximum thrust is " + i + " newtons at " + j + " meters height and " + k + " kph speed.");
            printwriter1.println("Maximum climb is " + f + " mps at " + l1 + " meters height and " + k1 + " kph speed.");
            printwriter1.println("Maximum speed is " + i1 + " kph at " + j1 + " meters height.");
            printwriter1.println("SL speed is " + l + " kph.");
        }
        catch(IOException ioexception)
        {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
        finally
        {
            if(printwriter != null)
                printwriter.close();
            if(printwriter1 != null)
                printwriter1.close();
            if(printwriter2 != null)
                printwriter2.close();
        }
        return;
    }

    public void drawSpeed(String s, float f, float f1, float f2)
    {
        try
        {
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
            for(int i = 0; i <= 10000; i += 100)
            {
                printwriter.print(i + "\t");
                float f3 = -1000F;
                float f4 = -1000F;
                int j = 50;
                do
                {
                    if(j >= 1500)
                        break;
                    float f5 = EI.forcePropAOA((float)j * 0.27778F, i, f1, true, f2);
                    float f7 = Wing.getClimb((float)j * 0.27778F, i, f5, (Sq.dragParasiteCx * 0.5F) / Wing.S);
                    if(f7 > f3)
                        f3 = f7;
                    if(f7 < 0.0F && f7 < f3)
                    {
                        f4 = j;
                        break;
                    }
                    j++;
                } while(true);
                if(f4 < 0.0F)
                    printwriter.print("\t");
                else
                    printwriter.print(f4 + "\t");
                printwriter.print(f3 * Wing.Vyfac + "\t");
                f3 = -1000F;
                f4 = -1000F;
                j = 50;
                do
                {
                    if(j >= 1500)
                        break;
                    float f6 = EI.forcePropAOA((float)j * 0.27778F, i, f, false, f2);
                    float f8 = Wing.getClimb((float)j * 0.27778F, i, f6, (Sq.dragParasiteCx * 0.5F) / Wing.S);
                    if(f8 > f3)
                        f3 = f8;
                    if(f8 < 0.0F && f8 < f3)
                    {
                        f4 = j;
                        break;
                    }
                    j++;
                } while(true);
                if(f4 < 0.0F)
                    printwriter.print("\t");
                else
                    printwriter.print(f4 + "\t");
                printwriter.print(f3 * Wing.Vyfac + "\t");
                printwriter.println();
            }

            printwriter.close();
        }
        catch(IOException ioexception)
        {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
    }

    public FlightModelMain(String s)
    {
        prButtons = Config.cur.ini.get("Mods", "PALButtonsPrefix", "").trim();
        flags0 = 0L;
        bDamagedGround = false;
        bDamaged = false;
        damagedInitiator = null;
        Skill = 0;
        crew = 0;
        turretSkill = 0;
        AP = null;
        CT = null;
        SensYaw = 0.0F;
        SensPitch = 0.0F;
        SensRoll = 0.0F;
        GearCX = 0.0F;
        radiatorCX = 0.0F;
        Loc = null;
        Or = null;
        Leader = null;
        Wingman = null;
        Offset = null;
        formationType = 0;
        formationScale = 0.0F;
        minElevCoeff = 0.0F;
        AOA = 0.0F;
        AOS = 0.0F;
        V = 0.0F;
        V2 = 0.0F;
        q_ = 0.0F;
        Gravity = 0.0F;
        Mach = 0.0F;
        Energy = 0.0F;
        BarometerZ = 0.0F;
        WingDiff = 0.0F;
        WingLoss = 0.0F;
        turbCoeff = 0.0F;
        FuelConsumption = 0.0F;
        producedAM = null;
        producedAMM = null;
        producedAF = null;
        fmsfxCurrentType = 0;
        fmsfxPrevValue = 0.0F;
        fmsfxTimeDisable = 0L;
        AF = null;
        AM = null;
        GF = null;
        GM = null;
        SummF = null;
        SummM = null;
        ACmeter = null;
        Accel = null;
        LocalAccel = null;
        BallAccel = null;
        Vwld = null;
        Vrel = null;
        Vair = null;
        Vflow = null;
        Vwind = null;
        J0 = null;
        J = null;
        W = null;
        AW = null;
        EI = null;
        M = null;
        AS = null;
        Sq = null;
        Ss = null;
        Arms = null;
        Gears = null;
        AIRBRAKE = false;
        Wing = null;
        Tail = null;
        Fusel = null;
        Scheme = 0;
        Wingspan = 0.0F;
        Length = 0.0F;
        Vmax = 0.0F;
        VmaxH = 0.0F;
        Vmin = 0.0F;
        HofVmax = 0.0F;
        VmaxFLAPS = 0.0F;
        VminFLAPS = 0.0F;
        VmaxAllowed = 0.0F;
        indSpeed = 0.0F;
        AOA_Crit = 0.0F;
        Range = 0.0F;
        CruiseSpeed = 0.0F;
        damagedParts = null;
        maxDamage = 0;
        cutPart = 0;
        bReal = false;
        UltimateLoad = 0.0F;
        Negative_G_Limit = 0.0F;
        Negative_G_Ultimate = 0.0F;
        LimitLoad = 0.0F;
        G_ClassCoeff = 0.0F;
        refM = 0.0F;
        SafetyFactor = 0.0F;
        ReferenceForce = 0.0F;
        turnFile = null;
        speedFile = null;
        craftFile = null;
        Operate = 0L;
        flags0 = 240L;
        bDamagedGround = false;
        bDamaged = false;
        damagedInitiator = null;
        CT = new Controls(this);
        SensYaw = 0.3F;
        SensPitch = 0.5F;
        SensRoll = 0.4F;
        GearCX = 0.035F;
        radiatorCX = 0.003F;
        Loc = new Point3d();
        Or = new Orientation();
        Offset = new Vector3d(25D, 25D, 0.0D);
        formationType = 0;
        formationScale = 1.0F;
        minElevCoeff = 4F;
        BarometerZ = 0.0F;
        WingDiff = 0.0F;
        WingLoss = 0.0F;
        turbCoeff = 1.0F;
        FuelConsumption = 0.06F;
        producedAM = new Vector3d(0.0D, 0.0D, 0.0D);
        producedAMM = new Vector3d(0.0D, 0.0D, 0.0D);
        producedAF = new Vector3d(0.0D, 0.0D, 0.0D);
        fmsfxCurrentType = -1;
        fmsfxPrevValue = 0.0F;
        fmsfxTimeDisable = -1L;
        AF = new Vector3d();
        AM = new Vector3d();
        GF = new Vector3d();
        GM = new Vector3d();
        SummF = new Vector3d();
        SummM = new Vector3d();
        ACmeter = new Vector3d();
        Accel = new Vector3d();
        LocalAccel = new Vector3d();
        BallAccel = new Vector3d();
        Vwld = new Vector3d();
        Vrel = new Vector3d();
        Vair = new Vector3d();
        Vflow = new Vector3d();
        Vwind = new Vector3d();
        J0 = new Vector3d();
        J = new Vector3d();
        W = new Vector3d();
        AW = new Vector3d();
        EI = new EnginesInterface();
        M = new Mass();
        AS = new AircraftState();
        Sq = new Squares();
        Ss = new Supersonic();
        Arms = new Arm();
        Gears = new Gear();
        Wing = new Polares();
        Tail = new Polares();
        Fusel = new Polares();
        Vmax = 116.6667F;
        VmaxH = 122.2222F;
        Vmin = 43.33334F;
        HofVmax = 277.7778F;
        VmaxFLAPS = 72.22223F;
        VminFLAPS = 39.72223F;
        VmaxAllowed = 208.3333F;
        indSpeed = 0.0F;
        AOA_Crit = 15.5F;
        Range = 800F;
        CruiseSpeed = 370F;
        damagedParts = new int[7];
        maxDamage = 0;
        cutPart = -1;
        UltimateLoad = 12F;
        Negative_G_Limit = -4F;
        Negative_G_Ultimate = -6F;
        LimitLoad = 8F;
        SafetyFactor = 1.5F;
        turnFile = new String();
        speedFile = new String();
        craftFile = new String();
        Operate = 0xfffffffffffL;
        load(s);
        init_G_Limits();
    }

    public void set(Loc loc, Vector3f vector3f)
    {
        super.actor.pos.setAbs(loc);
        Vwld.set(vector3f);
        loc.get(Loc, Or);
    }

    public void set(Point3d point3d, Orient orient, Vector3f vector3f)
    {
        super.actor.pos.setAbs(point3d, orient);
        Vwld.set(vector3f);
        Loc.set(point3d);
        Or.set(orient);
    }

    public void update(float f)
    {
        ((Aircraft)super.actor).update(f);
    }

    public boolean tick()
    {
        float f = Time.tickLenFs();
        super.actor.pos.getAbs(Loc, Or);
        int i = (int)(f / 0.05F) + 1;
        float f2 = f / (float)i;
        for(int j = 0; j < i; j++)
            update(f2);

        Gears.bFlatTopGearCheck = false;
        if(super.actor.pos.base() == null)
        {
            super.actor.pos.setAbs(Loc, Or);
        } else
        {
            if(super.actor.pos.base() instanceof Aircraft)
            {
                Vwld.set(((FlightModelMain) (((SndAircraft) ((Aircraft)super.actor.pos.base())).FM)).Vwld);
            } else
            {
                super.actor.pos.speed(actVwld);
                Vwld.x = (float)((Tuple3d) (actVwld)).x;
                Vwld.y = (float)((Tuple3d) (actVwld)).y;
                Vwld.z = (float)((Tuple3d) (actVwld)).z;
            }
            super.actor.pos.getAbs(Or);
            producedAF.z += 9.81F * M.mass;
        }
        Energy = Atmosphere.g() * (float)((Tuple3d) (Loc)).z + V2 * 0.5F;
        return true;
    }

    public float getOverload()
    {
        return (float)((Tuple3d) (ACmeter)).z;
    }

    public float getForwAccel()
    {
        return (float)((Tuple3d) (ACmeter)).x;
    }

    public float getRollAcceleration()
    {
        return (float)((Tuple3d) (AM)).x / Gravity;
    }

    public void gunPulse(Vector3d vector3d)
    {
        GPulse.set(vector3d);
        GPulse.scale(1.0F / M.mass);
        Vwld.sub(GPulse);
    }

    private void cutOp(int i)
    {
        Operate &= ~(1L << i);
    }

    protected boolean getOp(int i)
    {
        return (Operate & 1L << i) != 0L;
    }

    private float Op(int i)
    {
        return getOp(i) ? 1.0F : 0.0F;
    }

    public final boolean isPlayers()
    {
        return super.actor != null && super.actor == World.getPlayerAircraft();
    }

    public final boolean isCapableOfACM()
    {
        return (flags0 & 32L) != 0L;
    }

    public final boolean isCapableOfBMP()
    {
        return (flags0 & 16L) != 0L;
    }

    public final boolean isCapableOfTaxiing()
    {
        return (flags0 & 64L) != 0L;
    }

    public final boolean isReadyToDie()
    {
        return (flags0 & 4L) != 0L;
    }

    public final boolean isReadyToReturn()
    {
        return (flags0 & 2L) != 0L;
    }

    public final boolean isTakenMortalDamage()
    {
        return (flags0 & 8L) != 0L;
    }

    public final boolean isStationedOnGround()
    {
        return (flags0 & 128L) != 0L;
    }

    public final boolean isCrashedOnGround()
    {
        return (flags0 & 256L) != 0L;
    }

    public final boolean isNearAirdrome()
    {
        return (flags0 & 512L) != 0L;
    }

    public final boolean isCrossCountry()
    {
        return (flags0 & 1024L) != 0L;
    }

    public final boolean isWasAirborne()
    {
        return (flags0 & 2048L) != 0L;
    }

    public final boolean isSentWingNote()
    {
        return (flags0 & 4096L) != 0L;
    }

    public final boolean isSentBuryNote()
    {
        return (flags0 & 16384L) != 0L;
    }

    public final boolean isSentControlsOutNote()
    {
        return (flags0 & 32768L) != 0L;
    }

    public boolean isOk()
    {
        return isCapableOfBMP() && !isReadyToDie() && !isTakenMortalDamage();
    }

    private void checkDamaged()
    {
        if(!bDamaged && Actor.isValid(damagedInitiator) && (!isCapableOfBMP() || isTakenMortalDamage()))
        {
            bDamaged = true;
            if(super.actor != damagedInitiator)
                EventLog.onDamaged(super.actor, damagedInitiator);
            damagedInitiator = null;
        }
        if(!bDamagedGround && isStationedOnGround() && (!isCapableOfBMP() || !isCapableOfTaxiing() || isReadyToDie() || isTakenMortalDamage() || isSentControlsOutNote()))
        {
            bDamagedGround = true;
            EventLog.onDamagedGround(super.actor);
        }
    }

    public final void setCapableOfACM(boolean flag)
    {
        if(isCapableOfACM() == flag)
            return;
        if(flag)
            flags0 |= 32L;
        else
            flags0 &= -33L;
    }

    public final void setCapableOfBMP(boolean flag, Actor actor)
    {
        if(isCapableOfBMP() == flag)
            return;
        if(isCapableOfBMP() && World.Rnd().nextInt(0, 99) < 25)
            Voice.speakMayday((Aircraft)super.actor);
        if(flag)
        {
            flags0 |= 16L;
        } else
        {
            flags0 &= -17L;
            if(!bDamaged)
                damagedInitiator = actor;
            checkDamaged();
        }
    }

    public final void setCapableOfTaxiing(boolean flag)
    {
        if(isCapableOfTaxiing() == flag)
            return;
        if(flag)
        {
            flags0 |= 64L;
        } else
        {
            flags0 &= -65L;
            checkDamaged();
        }
    }

    public final void setReadyToDie(boolean flag)
    {
        if(isReadyToDie() == flag)
            return;
        if(!isReadyToDie())
        {
            if(World.Rnd().nextInt(0, 99) < 75)
                Voice.speakMayday((Aircraft)super.actor);
            Explosions.generateComicBulb(super.actor, "OnFire", 9F);
        }
        if(flag)
        {
            flags0 |= 4L;
            checkDamaged();
        } else
        {
            flags0 &= -5L;
        }
    }

    public final void setReadyToDieSoftly(boolean flag)
    {
        if(isReadyToDie() == flag)
            return;
        if(flag)
        {
            flags0 |= 4L;
            checkDamaged();
        } else
        {
            flags0 &= -5L;
        }
    }

    public final void setReadyToReturn(boolean flag)
    {
        if(isReadyToReturn() == flag)
            return;
        if(!isReadyToReturn())
            Explosions.generateComicBulb(super.actor, "RTB", 9F);
        if(flag)
            flags0 |= 2L;
        else
            flags0 &= -3L;
        Voice.speakToReturn((Aircraft)super.actor);
    }

    public final void setReadyToReturnSoftly(boolean flag)
    {
        if(isReadyToReturn() == flag)
            return;
        if(flag)
            flags0 |= 2L;
        else
            flags0 &= -3L;
    }

    public final void setTakenMortalDamage(boolean flag, Actor actor)
    {
        if(isTakenMortalDamage() == flag)
            return;
        if(flag)
        {
            flags0 |= 8L;
            if(!bDamaged && !Actor.isValid(damagedInitiator))
                damagedInitiator = actor;
            checkDamaged();
        } else
        {
            flags0 &= -9L;
        }
        if(flag && super.actor != World.getPlayerAircraft() && ((SndAircraft) ((Aircraft)super.actor)).FM.turret.length > 0)
        {
            for(int i = 0; i < ((SndAircraft) ((Aircraft)super.actor)).FM.turret.length; i++)
                ((SndAircraft) ((Aircraft)super.actor)).FM.turret[i].bIsOperable = false;

        }
    }

    public final void setStationedOnGround(boolean flag)
    {
        if(isStationedOnGround() == flag)
            return;
        if(flag)
        {
            flags0 |= 128L;
            EventLog.onAirLanded((Aircraft)super.actor);
            checkDamaged();
        } else
        {
            flags0 &= -129L;
        }
    }

    public final void setCrashedOnGround(boolean flag)
    {
        if(isCrashedOnGround() == flag)
            return;
        if(flag)
        {
            flags0 |= 256L;
            checkDamaged();
        } else
        {
            flags0 &= -257L;
        }
    }

    public final void setNearAirdrome(boolean flag)
    {
        if(isNearAirdrome() == flag)
            return;
        if(flag)
            flags0 |= 512L;
        else
            flags0 &= -513L;
    }

    public final void setCrossCountry(boolean flag)
    {
        if(isCrossCountry() == flag)
            return;
        if(flag)
            flags0 |= 1024L;
        else
            flags0 &= -1025L;
    }

    public final void setWasAirborne(boolean flag)
    {
        if(isWasAirborne() == flag)
            return;
        if(flag)
            flags0 |= 2048L;
        else
            flags0 &= -2049L;
    }

    public final void setSentWingNote(boolean flag)
    {
        if(isSentWingNote() == flag)
            return;
        if(flag)
            flags0 |= 4096L;
        else
            flags0 &= -4097L;
    }

    public final void setSentBuryNote(boolean flag)
    {
        if(isSentBuryNote() == flag)
            return;
        if(flag)
            flags0 |= 16384L;
        else
            flags0 &= -16385L;
    }

    public final void setSentControlsOutNote(boolean flag)
    {
        if(isSentControlsOutNote() == flag)
            return;
        if(flag)
        {
            flags0 |= 32768L;
            checkDamaged();
        } else
        {
            flags0 &= -32769L;
        }
    }

    public void hit(int i)
    {
        Aircraft.debugprintln(super.actor, "Detected NDL in " + Aircraft.partNames()[i] + "..");
        if(i < 0 || i >= 44)
            return;
        if(this instanceof RealFlightModel)
            bReal = true;
        else
            bReal = false;
        switch(i)
        {
        case 2: // '\002'
        case 7: // '\007'
        case 8: // '\b'
        case 9: // '\t'
        case 10: // '\n'
        case 14: // '\016'
        case 21: // '\025'
        case 22: // '\026'
        case 23: // '\027'
        case 24: // '\030'
        case 25: // '\031'
        case 26: // '\032'
        case 27: // '\033'
        case 28: // '\034'
        case 29: // '\035'
        case 30: // '\036'
        default:
            break;

        case 13: // '\r'
            Sq.dragFuselageCx = Sq.dragFuselageCx > 0.08F ? Sq.dragFuselageCx * 2.0F : 0.08F;
            break;

        case 0: // '\0'
            Sq.liftWingLOut *= 0.95F;
            SensRoll *= 0.68F;
            break;

        case 1: // '\001'
            Sq.liftWingROut *= 0.95F;
            SensRoll *= 0.68F;
            break;

        case 17: // '\021'
            Sq.liftStab *= 0.68F;
            Sq.getClass();
            Sq.dragProducedCx += 0.12F;
            break;

        case 18: // '\022'
            Sq.liftStab *= 0.68F;
            Sq.getClass();
            Sq.dragProducedCx += 0.12F;
            break;

        case 31: // '\037'
            Sq.squareElevators *= 0.68F;
            SensPitch *= 0.68F;
            break;

        case 32: // ' '
            Sq.squareElevators *= 0.68F;
            SensPitch *= 0.68F;
            break;

        case 11: // '\013'
            Sq.liftKeel *= 0.68F;
            Sq.getClass();
            Sq.dragProducedCx += 0.12F;
            break;

        case 12: // '\f'
            Sq.liftKeel *= 0.68F;
            Sq.getClass();
            Sq.dragProducedCx += 0.12F;
            break;

        case 15: // '\017'
            Sq.squareRudders *= 0.5F;
            SensYaw *= 0.68F;
            break;

        case 16: // '\020'
            Sq.squareRudders *= 0.5F;
            SensYaw *= 0.68F;
            break;

        case 33: // '!'
            if(bReal)
                setDmgLoadLimit(0.6F, 2);
            Sq.getClass();
            Sq.liftWingLIn -= 0.8F;
            Sq.getClass();
            Sq.dragProducedCx += 0.12F;
            if(World.Rnd().nextFloat() < 0.09F)
                setReadyToReturn(true);
            if(World.Rnd().nextFloat() < 0.09F)
                setCapableOfACM(false);
            break;

        case 36: // '$'
            if(bReal)
                setDmgLoadLimit(0.6F, 3);
            Sq.getClass();
            Sq.liftWingRIn -= 0.8F;
            Sq.getClass();
            Sq.dragProducedCx += 0.12F;
            if(World.Rnd().nextFloat() < 0.09F)
                setReadyToReturn(true);
            if(World.Rnd().nextFloat() < 0.09F)
                setCapableOfACM(false);
            break;

        case 34: // '"'
            if(bReal)
                setDmgLoadLimit(0.7F, 1);
            Sq.getClass();
            Sq.liftWingLMid -= 0.8F;
            Sq.getClass();
            Sq.dragProducedCx += 0.12F;
            if(World.Rnd().nextFloat() < 0.09F)
                setReadyToReturn(true);
            if(World.Rnd().nextFloat() < 0.09F)
                setCapableOfACM(false);
            break;

        case 37: // '%'
            if(bReal)
                setDmgLoadLimit(0.7F, 4);
            Sq.getClass();
            Sq.liftWingRMid -= 0.8F;
            Sq.getClass();
            Sq.dragProducedCx += 0.12F;
            if(World.Rnd().nextFloat() < 0.09F)
                setReadyToReturn(true);
            if(World.Rnd().nextFloat() < 0.09F)
                setCapableOfACM(false);
            break;

        case 35: // '#'
            if(bReal)
                setDmgLoadLimit(0.8F, 0);
            Sq.getClass();
            Sq.liftWingLOut -= 0.8F;
            Sq.getClass();
            Sq.dragProducedCx += 0.12F;
            if(World.Rnd().nextFloat() < 0.12F)
                setReadyToReturn(true);
            if(World.Rnd().nextFloat() < 0.12F)
                setCapableOfACM(false);
            break;

        case 38: // '&'
            if(bReal)
                setDmgLoadLimit(0.8F, 5);
            Sq.getClass();
            Sq.liftWingROut -= 0.8F;
            Sq.getClass();
            Sq.dragProducedCx += 0.12F;
            if(World.Rnd().nextFloat() < 0.12F)
                setReadyToReturn(true);
            if(World.Rnd().nextFloat() < 0.12F)
                setCapableOfACM(false);
            break;

        case 3: // '\003'
            if(Sq.dragEngineCx[0] < 0.15F)
                Sq.dragEngineCx[0] += 0.050000000000000003D;
            if(World.Rnd().nextFloat() < 0.12F)
                setReadyToReturn(true);
            break;

        case 4: // '\004'
            if(Sq.dragEngineCx[1] < 0.15F)
                Sq.dragEngineCx[1] += 0.05F;
            if(World.Rnd().nextFloat() < 0.12F)
                setReadyToReturn(true);
            break;

        case 5: // '\005'
            if(Sq.dragEngineCx[2] < 0.15F)
                Sq.dragEngineCx[2] += 0.05F;
            if(World.Rnd().nextFloat() < 0.12F)
                setReadyToReturn(true);
            break;

        case 6: // '\006'
            if(Sq.dragEngineCx[3] < 0.15F)
                Sq.dragEngineCx[3] += 0.05F;
            if(World.Rnd().nextFloat() < 0.12F)
                setReadyToReturn(true);
            break;

        case 19: // '\023'
        case 20: // '\024'
            if(bReal)
                setDmgLoadLimit(0.5F, 6);
            break;
        }
    }

    public void cut(int i, int j, Actor actor)
    {
        if(i < 0 || i >= 44)
            return;
        Aircraft.debugprintln(super.actor, "cutting part #" + i + " (" + Aircraft.partNames()[i] + ")");
        if(!getOp(i))
        {
            Aircraft.debugprintln(super.actor, "part #" + i + " (" + Aircraft.partNames()[i] + ") already cut off");
            return;
        }
        cutOp(i);
        switch(i)
        {
        case 8: // '\b'
        case 14: // '\016'
        case 21: // '\025'
        case 22: // '\026'
        case 23: // '\027'
        case 24: // '\030'
        case 25: // '\031'
        case 26: // '\032'
        case 27: // '\033'
        case 28: // '\034'
        case 29: // '\035'
        case 30: // '\036'
        default:
            break;

        case 13: // '\r'
            setCapableOfBMP(false, actor);
            setCapableOfACM(false);
            Sq.dragFuselageCx = Sq.dragFuselageCx > 0.24F ? Sq.dragFuselageCx * 2.0F : 0.24F;
            break;

        case 9: // '\t'
            setCapableOfTaxiing(false);
            Gears.hitLeftGear();
            break;

        case 10: // '\n'
            setCapableOfTaxiing(false);
            Gears.hitRightGear();
            break;

        case 7: // '\007'
            setCapableOfTaxiing(false);
            Gears.hitCentreGear();
            break;

        case 2: // '\002'
            setCapableOfACM(false);
            setCapableOfBMP(false, actor);
            setCapableOfTaxiing(false);
            setTakenMortalDamage(true, actor);
            for(int k = 0; k < EI.getNum(); k++)
                EI.engines[k].setReadyness(super.actor, 0.0F);

            cutOp(19);
            cutOp(20);
            // fall through

        case 19: // '\023'
        case 20: // '\024'
            setCapableOfACM(false);
            setCapableOfBMP(false, actor);
            setReadyToDie(true);
            setTakenMortalDamage(true, actor);
            Arms.GCENTER = 0.0F;
            W.y += World.Rnd().nextFloat(-0.1F, 0.1F);
            W.z += World.Rnd().nextFloat(-0.1F, 0.1F);
            J0.y *= 0.5D;
            cut(17, j, actor);
            cut(18, j, actor);
            cut(11, j, actor);
            cut(12, j, actor);
            break;

        case 17: // '\021'
            if(World.Rnd().nextInt(-1, 8) < Skill)
                setReadyToReturn(true);
            if(World.Rnd().nextInt(-1, 16) < Skill)
                setReadyToDie(true);
            Sq.liftStab *= 0.5F * Op(18) + 0.1F;
            if(isPlayers())
            {
                Sq.getClass();
                Sq.dragProducedCx += 0.12F;
            } else
            {
                Sq.getClass();
                Sq.dragProducedCx += 0.06F;
            }
            Sq.liftWingRMid *= 0.9F;
            Sq.liftWingLMid *= 1.1F;
            Sq.liftWingROut *= 0.9F;
            Sq.liftWingLOut *= 1.1F;
            if(Op(18) == 0.0F)
            {
                CT.setTrimAileronControl(CT.getTrimAileronControl() - 0.25F);
                if(World.Rnd().nextBoolean())
                {
                    Sq.liftWingLOut *= 0.95F;
                    Sq.liftWingLMid *= 0.95F;
                    Sq.liftWingLIn *= 0.95F;
                    Sq.liftWingRIn *= 0.75F;
                    Sq.liftWingRMid *= 0.75F;
                    Sq.liftWingROut *= 0.75F;
                } else
                {
                    Sq.liftWingROut *= 0.75F;
                    Sq.liftWingRMid *= 0.75F;
                    Sq.liftWingRIn *= 0.75F;
                    Sq.liftWingLIn *= 0.95F;
                    Sq.liftWingLMid *= 0.95F;
                    Sq.liftWingLOut *= 0.95F;
                }
            }
            cutOp(31);
            // fall through

        case 31: // '\037'
            setCapableOfACM(false);
            if(Op(32) == 0.0F)
                setReadyToDie(true);
            Sq.squareElevators *= 0.5F * Op(32);
            SensPitch *= 0.5F * Op(32);
            break;

        case 18: // '\022'
            if(World.Rnd().nextInt(-1, 8) < Skill)
                setReadyToReturn(true);
            if(World.Rnd().nextInt(-1, 16) < Skill)
                setReadyToDie(true);
            Sq.liftStab *= 0.5F * Op(17) + 0.1F;
            if(isPlayers())
            {
                Sq.getClass();
                Sq.dragProducedCx += 0.12F;
            } else
            {
                Sq.getClass();
                Sq.dragProducedCx += 0.06F;
            }
            Sq.liftWingLMid *= 0.9F;
            Sq.liftWingRMid *= 1.1F;
            Sq.liftWingLOut *= 0.9F;
            Sq.liftWingROut *= 1.1F;
            if(Op(17) == 0.0F)
            {
                CT.setTrimAileronControl(CT.getTrimAileronControl() + 0.25F);
                if(World.Rnd().nextBoolean())
                {
                    Sq.liftWingLOut *= 0.95F;
                    Sq.liftWingLMid *= 0.95F;
                    Sq.liftWingLIn *= 0.95F;
                    Sq.liftWingRIn *= 0.75F;
                    Sq.liftWingRMid *= 0.75F;
                    Sq.liftWingROut *= 0.75F;
                } else
                {
                    Sq.liftWingROut *= 0.75F;
                    Sq.liftWingRMid *= 0.75F;
                    Sq.liftWingRIn *= 0.75F;
                    Sq.liftWingLIn *= 0.95F;
                    Sq.liftWingLMid *= 0.95F;
                    Sq.liftWingLOut *= 0.95F;
                }
            }
            cutOp(32);
            // fall through

        case 32: // ' '
            setCapableOfACM(false);
            if(Op(31) == 0.0F)
                setReadyToDie(true);
            Sq.squareElevators *= 0.5F * Op(31);
            SensPitch *= 0.5F * Op(31);
            break;

        case 11: // '\013'
            if(World.Rnd().nextInt(-1, 8) < Skill)
                setReadyToReturn(true);
            if(World.Rnd().nextInt(-1, 16) < Skill)
                setReadyToDie(true);
            if((super.actor instanceof Scheme2a) || (super.actor instanceof Scheme5) || (super.actor instanceof Scheme7))
                Sq.liftKeel *= 0.25F * Op(12);
            else
                Sq.liftKeel *= 0.0F;
            cutOp(15);
            // fall through

        case 15: // '\017'
            setCapableOfACM(false);
            if(World.Rnd().nextInt(-1, 8) < Skill)
                setReadyToReturn(true);
            if((super.actor instanceof Scheme2a) || (super.actor instanceof Scheme5) || (super.actor instanceof Scheme7))
            {
                Sq.squareRudders *= 0.5F;
                SensYaw *= 0.25F;
            } else
            {
                Sq.squareRudders *= 0.0F;
                SensYaw *= 0.0F;
            }
            break;

        case 12: // '\f'
            if(World.Rnd().nextInt(-1, 8) < Skill)
                setReadyToReturn(true);
            if(World.Rnd().nextInt(-1, 16) < Skill)
                setReadyToDie(true);
            if((super.actor instanceof Scheme2a) || (super.actor instanceof Scheme5) || (super.actor instanceof Scheme7))
                Sq.liftKeel *= 0.25F * Op(12);
            else
                Sq.liftKeel *= 0.0F;
            cutOp(16);
            // fall through

        case 16: // '\020'
            setCapableOfACM(false);
            if(World.Rnd().nextInt(-1, 8) < Skill)
                setReadyToReturn(true);
            if((super.actor instanceof Scheme2a) || (super.actor instanceof Scheme5) || (super.actor instanceof Scheme7))
            {
                Sq.squareRudders *= 0.5F;
                SensYaw *= 0.25F;
            } else
            {
                Sq.squareRudders *= 0.0F;
                SensYaw *= 0.0F;
            }
            break;

        case 33: // '!'
            Sq.liftWingLIn *= 0.25F;
            ((ActorHMesh)super.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.BombGun.class);
            ((ActorHMesh)super.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.RocketBombGun.class);
            cut(9, j, actor);
            cutOp(34);
            // fall through

        case 34: // '"'
            setTakenMortalDamage(true, actor);
            setReadyToDie(true);
            Sq.liftWingLMid *= 0.0F;
            Sq.liftWingLIn *= 0.9F;
            ((ActorHMesh)super.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.RocketGun.class);
            cutOp(35);
            // fall through

        case 35: // '#'
            setCapableOfBMP(false, actor);
            setCapableOfACM(false);
            AS.bWingTipLExists = false;
            AS.setStallState(false);
            AS.setAirShowState(false);
            Sq.liftWingLOut *= 0.0F;
            Sq.liftWingLMid *= 0.5F;
            Sq.liftWingLOut = 0.0F;
            Sq.liftWingLMid = 0.0F;
            Sq.liftWingLIn = 0.0F;
            CT.bHasAileronControl = false;
            cutOp(0);
            // fall through

        case 0: // '\0'
            if(Op(1) == 0.0F)
                setCapableOfACM(false);
            Sq.squareAilerons *= 0.5F;
            SensRoll *= 0.5F * Op(1);
            break;

        case 36: // '$'
            Sq.liftWingRIn *= 0.25F;
            ((ActorHMesh)super.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.BombGun.class);
            ((ActorHMesh)super.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.RocketBombGun.class);
            cut(10, j, actor);
            cutOp(37);
            // fall through

        case 37: // '%'
            setTakenMortalDamage(true, actor);
            setReadyToDie(true);
            Sq.liftWingRMid *= 0.0F;
            Sq.liftWingRIn *= 0.9F;
            ((ActorHMesh)super.actor).destroyChildFiltered(com.maddox.il2.objects.weapons.RocketGun.class);
            cutOp(38);
            // fall through

        case 38: // '&'
            setCapableOfBMP(false, actor);
            setCapableOfACM(false);
            AS.bWingTipRExists = false;
            AS.setStallState(false);
            AS.setAirShowState(false);
            Sq.liftWingROut *= 0.0F;
            Sq.liftWingRMid *= 0.5F;
            Sq.liftWingROut = 0.0F;
            Sq.liftWingRMid = 0.0F;
            Sq.liftWingRIn = 0.0F;
            CT.bHasAileronControl = false;
            cutOp(1);
            // fall through

        case 1: // '\001'
            if(Op(0) == 0.0F)
                setCapableOfACM(false);
            Sq.squareAilerons *= 0.5F;
            SensRoll *= 0.5F * Op(0);
            break;

        case 3: // '\003'
            if(EI.engines.length <= 0)
                break;
            setCapableOfTaxiing(false);
            if(super.actor instanceof Scheme1)
                setReadyToDie(true);
            setCapableOfACM(false);
            EI.engines[0].setEngineDies(super.actor);
            Sq.dragEngineCx[0] = 0.15F;
            break;

        case 4: // '\004'
            if(EI.engines.length > 1)
            {
                setCapableOfTaxiing(false);
                setCapableOfACM(false);
                EI.engines[1].setEngineDies(super.actor);
                Sq.dragEngineCx[1] = 0.15F;
            }
            break;

        case 5: // '\005'
            if(EI.engines.length > 2)
            {
                setCapableOfTaxiing(false);
                setCapableOfACM(false);
                EI.engines[2].setEngineDies(super.actor);
                Sq.dragEngineCx[2] = 0.15F;
            }
            break;

        case 6: // '\006'
            if(EI.engines.length > 3)
            {
                setCapableOfTaxiing(false);
                setCapableOfACM(false);
                EI.engines[3].setEngineDies(super.actor);
                Sq.dragEngineCx[3] = 0.15F;
            }
            break;
        }
    }

    private void init_G_Limits()
    {
        UltimateLoad = LimitLoad * 1.5F;
        ReferenceForce = LimitLoad * (M.referenceWeight + refM);
        Negative_G_Limit = -0.5F * LimitLoad;
        Negative_G_Ultimate = Negative_G_Limit * 1.5F;
    }

    public void setDmgLoadLimit(float f, int i)
    {
        setLimitLoad(LimitLoad -= f);
        damagedParts[i]++;
        for(i = 0; i < damagedParts.length; i++)
            if(damagedParts[i] > maxDamage)
                cutPart = i;

    }

    public void setUltimateLoad(float f)
    {
        UltimateLoad = f * SafetyFactor;
        Negative_G_Ultimate = UltimateLoad * -0.5F;
    }

    public float getUltimateLoad()
    {
        return UltimateLoad;
    }

    public void setLimitLoad(float f)
    {
        LimitLoad = f;
        Negative_G_Limit = -f * 0.5F;
        setUltimateLoad(f);
    }

    public float getLimitLoad()
    {
        return LimitLoad;
    }

    public void setSafetyFactor(float f)
    {
        SafetyFactor -= f;
    }

    public float getSafetyFactor()
    {
        return SafetyFactor;
    }

    public float getLoadDiff()
    {
        return getLimitLoad() - getOverload();
    }

    public void doRequestFMSFX(int i, int j)
    {
        if(fmsfxCurrentType == 1 && i != 1)
            return;
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            fmsfxCurrentType = i;
            break;

        case 1: // '\001'
            fmsfxCurrentType = i;
            fmsfxPrevValue = (float)j * 0.01F;
            if(fmsfxPrevValue < 0.05F)
                fmsfxCurrentType = 0;
            break;

        case 2: // '\002'
        case 3: // '\003'
            fmsfxCurrentType = i;
            fmsfxTimeDisable = Time.current() + (long)((float)(100 * j) / (Wingspan * Wingspan));
            break;
        }
    }

    public void setGCenter(float f)
    {
        Arms.GCENTER = f;
    }

    public void setGC_Gear_Shift(float f)
    {
        Arms.GC_GEAR_SHIFT = f;
    }

    public void setFlapsShift(float f)
    {
        Wing.setFlaps(f);
    }

    public static long finger(long l, String s)
    {
        SectFile sectfile = sectFile(s);
        l = sectfile.finger(l);
        int i = 0;
        do
        {
            if(i >= 10)
                break;
            String s1 = "Engine" + i + "Family";
            String s2 = sectfile.get("Engine", s1);
            if(s2 == null)
                break;
            SectFile sectfile1 = sectFile("FlightModels/" + s2 + ".emd");
            l = sectfile1.finger(l);
            i++;
        } while(true);
        return l;
    }

    private static boolean exisstFile(String s)
    {
        try
        {
            SFSInputStream sfsinputstream = new SFSInputStream(s);
            sfsinputstream.close();
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }

    public static SectFile sectFile(String s)
    {
        SectFile sectfile = null;
        String s1 = s.toLowerCase();
        String s2 = "gui/game/buttons";
        int i = s1.indexOf(":");
        if(i > -1)
        {
            s2 = s1.substring(i + 1, s1.length());
            s1 = s1.substring(0, i);
            if(s.endsWith(".emd"))
            {
                s2 = s2.substring(0, s2.length() - 4);
                s1 = s1 + ".emd";
            }
            System.out.println("FM called '" + s + "' is being loaded from File: '" + s2 + "'");
        }
        if(prButtons != null && prButtons.length() > 0)
        {
            String s3 = prButtons + s2;
            int ra = s2.lastIndexOf('/');
            if(ra > -1)
                s3 = s2.substring(0, ra + 1) + prButtons + s2.substring(ra + 1, s2.length());
            if(exisstFile(s3))
            {
                s2 = s3;
                System.out.println("FM called '" + s + "' is being loaded from Alternative File: '" + s2 + "'");
            }
        }
        try
        {
            Object obj = Property.value(s, "stream", null);
            InputStream inputstream;
            if(obj != null)
            {
                inputstream = (InputStream)obj;
            } else
            {
                if(fmDir == null)
                {
                    fmDir = new InOutStreams();
                    fmDir.open(Finger.LongFN(0L, s2));
                    fmDirs.add(fmDir);
                    fmDirNames.add(s2);
                } else
                if(!s2.equalsIgnoreCase(lastFMFile))
                {
                    fmDir = null;
                    int j = 0;
                    do
                    {
                        if(j >= fmDirNames.size())
                            break;
                        String s3 = (String)fmDirNames.get(j);
                        if(s2.equalsIgnoreCase(s3))
                        {
                            fmDir = (InOutStreams)fmDirs.get(j);
                            break;
                        }
                        j++;
                    } while(true);
                    if(fmDir == null)
                    {
                        fmDir = new InOutStreams();
                        fmDir.open(Finger.LongFN(0L, s2));
                        fmDirs.add(fmDir);
                        fmDirNames.add(s2);
                    }
                }
                lastFMFile = s2;
                inputstream = fmDir.openStream("" + Finger.Int(s1 + "d2wO"));
                if(inputstream == null)
                    inputstream = fmDir.openStream("" + Finger.Int(s1 + "d2w0"));
                if(inputstream == null)
                    inputstream = fmDir.openStream("" + Finger.Int(s1 + "d2w5"));
                if(inputstream == null)
                {
                    System.out.println("Error loading FM called '" + s + "'!");
                    System.out.println("This FM is not present in '" + s2 + "' File");
                    if(s.endsWith(".emd"))
                        System.out.println("*** This should have been the reason of the 'Explosion in the Air' when mission started ***");
                    else
                        System.out.println("*** This was the Reason of your 60% or 70% CTD ***");
                }
            }
            inputstream.mark(0);
            sectfile = new SectFile(new InputStreamReader(new KryptoInputFilter(inputstream, getSwTbl(Finger.Int(s1 + "ogh9"), inputstream.available())), "Cp1252"));
            inputstream.reset();
            if(obj == null)
                Property.set(s, "stream", inputstream);
        }
        catch(Exception exception) { }
        return sectfile;
    }

    private static int[] getSwTbl(int i, int j)
    {
        if(i < 0)
            i = -i;
        if(j < 0)
            j = -j;
        int k = (j + i / 5) % 16 + 14;
        int l = (j + i / 19) % Finger.kTable.length;
        if(k < 0)
            k = -k % 16;
        if(k < 10)
            k = 10;
        if(l < 0)
            l = -l % Finger.kTable.length;
        int ai[] = new int[k];
        for(int i1 = 0; i1 < k; i1++)
            ai[i1] = Finger.kTable[(l + i1) % Finger.kTable.length];

        return ai;
    }

    public static final int __DEBUG__IL2C_DUMP_LEVEL__ = 0;
    public static final int ROOKIE = 0;
    public static final int NORMAL = 1;
    public static final int VETERAN = 2;
    public static final int ACE = 3;
    public static final int FMSFX_NOOP = 0;
    public static final int FMSFX_DROP_WINGFOLDED = 1;
    public static final int FMSFX_DROP_LEFTWING = 2;
    public static final int FMSFX_DROP_RIGHTWING = 3;
    public static float outEngineAOA = 0.0F;
    private long flags0;
    private boolean bDamagedGround;
    private boolean bDamaged;
    private Actor damagedInitiator;
    public int Skill;
    public int crew;
    public int turretSkill;
    public Autopilotage AP;
    public Controls CT;
    public float SensYaw;
    public float SensPitch;
    public float SensRoll;
    public float GearCX;
    public float radiatorCX;
    public Point3d Loc;
    public Orientation Or;
    public FlightModel Leader;
    public FlightModel Wingman;
    public Vector3d Offset;
    public byte formationType;
    public float formationScale;
    public float minElevCoeff;
    protected float AOA;
    protected float AOS;
    protected float V;
    protected float V2;
    protected float q_;
    protected float Gravity;
    protected float Mach;
    public float Energy;
    public float BarometerZ;
    public float WingDiff;
    public float WingLoss;
    public float turbCoeff;
    public float FuelConsumption;
    public Vector3d producedAM;
    public Vector3d producedAMM;
    public Vector3d producedAF;
    protected int fmsfxCurrentType;
    protected float fmsfxPrevValue;
    protected long fmsfxTimeDisable;
    protected Vector3d AF;
    protected Vector3d AM;
    protected Vector3d GF;
    protected Vector3d GM;
    protected Vector3d SummF;
    protected Vector3d SummM;
    private static Vector3d TmpA = new Vector3d();
    private static Vector3d TmpV = new Vector3d();
    protected Vector3d ACmeter;
    protected Vector3d Accel;
    protected Vector3d LocalAccel;
    protected Vector3d BallAccel;
    public Vector3d Vwld;
    public Vector3d Vrel;
    protected Vector3d Vair;
    protected Vector3d Vflow;
    protected Vector3d Vwind;
    protected Vector3d J0;
    protected Vector3d J;
    protected Vector3d W;
    protected Vector3d AW;
    public EnginesInterface EI;
    public Mass M;
    public AircraftState AS;
    public Squares Sq;
    protected Arm Arms;
    public Gear Gears;
    public boolean AIRBRAKE;
    protected Polares Wing;
    protected Polares Tail;
    protected Polares Fusel;
    public int Scheme;
    public float Wingspan;
    public float Length;
    public float Vmax;
    public float VmaxH;
    public float Vmin;
    public float HofVmax;
    public float VmaxFLAPS;
    public float VminFLAPS;
    public float VmaxAllowed;
    public float indSpeed;
    public float AOA_Crit;
    public float Range;
    public float CruiseSpeed;
    public int damagedParts[];
    public int maxDamage;
    public int cutPart;
    private boolean bReal;
    public float UltimateLoad;
    public float Negative_G_Limit;
    public float Negative_G_Ultimate;
    public float LimitLoad;
    public float G_ClassCoeff;
    public float refM;
    public float SafetyFactor;
    public float ReferenceForce;
    String turnFile;
    String speedFile;
    String craftFile;
    private static Vector3d actVwld = new Vector3d();
    public long Operate;
    private static Vector3d GPulse = new Vector3d();
    public static boolean bCY_CRIT04 = true;
    private static InOutStreams fmDir = null;
    private static String lastFMFile;
    private static ArrayList fmDirs = new ArrayList();
    private static ArrayList fmDirNames = new ArrayList();
    private static String prButtons;
    public Supersonic Ss;
}
