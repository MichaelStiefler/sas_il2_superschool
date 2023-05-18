// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 2016/07/05 15:21:43
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitSkyhawkA4M.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, TypeGuidedMissileCarrier, Skyhawk, SkyhawkA4M, 
//            Cockpit

public class CockpitSkyhawkA4M extends CockpitPilot
{
    private class Variables
    {

        float throttle;
        float prop;
        float mix;
        float stage;
        float altimeter;
        float vspeed;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = 0.85F * setOld.throttle + ((FlightModelMain) (fm)).CT.PowerControl * 0.15F;
                setNew.prop = 0.85F * setOld.prop + ((FlightModelMain) (fm)).CT.getStepControl() * 0.15F;
                setNew.stage = 0.85F * setOld.stage + (float)((FlightModelMain) (fm)).EI.engines[0].getControlCompressor() * 0.15F;
                setNew.mix = 0.85F * setOld.mix + ((FlightModelMain) (fm)).EI.engines[0].getControlMix() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth(10F);
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                float f4 = ((SkyhawkA4M)aircraft()).k14Distance;
                setNew.k14w = (5F * CockpitSkyhawkA4M.k14TargetWingspanScale[((SkyhawkA4M)aircraft()).k14WingspanType]) / f4;
                setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
                setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitSkyhawkA4M.k14TargetMarkScale[((SkyhawkA4M)aircraft()).k14WingspanType];
                setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((SkyhawkA4M)aircraft()).k14Mode;
                Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
                double d = 0.00125D * (double)f4;
                float f1 = (float)Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
                float f2 = -(float)Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
                float f3 = floatindex((f4 - 200F) * 0.04F, CockpitSkyhawkA4M.k14BulletDrop) - CockpitSkyhawkA4M.k14BulletDrop[0];
                f2 += (float)Math.toDegrees(Math.atan(f3 / f4));
                setNew.k14x = 0.92F * setOld.k14x + 0.08F * f1;
                setNew.k14y = 0.92F * setOld.k14y + 0.08F * f2;
                if(setNew.k14x > 7F)
                    setNew.k14x = 7F;
                if(setNew.k14x < -7F)
                    setNew.k14x = -7F;
                if(setNew.k14y > 7F)
                    setNew.k14y = 7F;
                if(setNew.k14y < -7F)
                    setNew.k14y = -7F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(30F);
    }

    public CockpitSkyhawkA4M()
    {
        super("3DO/Cockpit/CockpitSkyhawkA4/hier.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        super.cockpitNightMats = (new String[] {
            "GagePanel1", "GagePanel2", "GagePanel3", "GagePanel4", "GagePanel5", "GagePanel6", "GagePanel7", "Glass", "needles", "radio1"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        super.printCompassHeading = true;
        super.limits6DoF = (new float[] {
            0.7F, 0.055F, -0.07F, 0.09F, 0.15F, -0.11F, 0.03F, -0.03F
        });
        loadBuzzerFX();
    }

    private int iLockState()
    {
        if(!(super.aircraft() instanceof TypeGuidedMissileCarrier))
            return 0;
        else
            return ((TypeGuidedMissileCarrier)super.aircraft()).getGuidedMissileUtils().getMissileLockState();
    }

    private float machNumber()
    {
        return ((Skyhawk)super.aircraft()).calculateMach();
    }

    public void reflectWorldToInstruments(float f)
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) == 0)
        {
            int i = ((SkyhawkA4M)aircraft()).k14Mode;
            boolean flag = i < 2;
            super.mesh.chunkVisible("Z_Z_RETICLE", flag);
            flag = i > 0;
            super.mesh.chunkVisible("Z_Z_RETICLE1", flag);
            super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, setNew.k14y);
            resetYPRmodifier();
            Cockpit.xyz[0] = setNew.k14w;
            for(int j = 1; j < 7; j++)
            {
                super.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
                super.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
            }

        }
        super.mesh.chunkSetAngles("Z_RightPedal", 15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_LeftPedal", -15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Throtle1", 40F * setNew.throttle, 0.0F, 0.0F);
        resetYPRmodifier();
        float f1 = ((FlightModelMain) (super.fm)).EI.engines[0].getStage();
        if(f1 > 0.0F && f1 < 7F)
            f1 = 0.0345F;
        else
            f1 = -0.05475F;
        Cockpit.xyz[2] = f1;
        super.mesh.chunkSetLocate("Z_EngShutOff", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Column", (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 16F, 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 20F);
        if(((FlightModelMain) (super.fm)).CT.GearControl == 0.0F && ((FlightModelMain) (super.fm)).CT.getGear() != 0.0F)
            f1 = 40F;
        else
        if(((FlightModelMain) (super.fm)).CT.GearControl == 1.0F && ((FlightModelMain) (super.fm)).CT.getGear() != 1.0F)
            f1 = 20F;
        else
            f1 = 0.0F;
        super.mesh.chunkSetAngles("Z_Gear1", f1, 0.0F, 0.0F);
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).CT.saveWeaponControl[0])
            Cockpit.xyz[2] = -0.0029F;
        super.mesh.chunkSetLocate("Z_DropTank", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Target1", setNew.k14wingspan, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        f1 = ((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput();
        super.mesh.chunkSetAngles("Z_Fuel1", cvt((float)Math.sqrt(f1), 0.0F, 1.0F, -59.5F, 223F), 0.0F, 0.0F);
        f1 = cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 1000F, 0.0F, 270F);
        if(f1 < 45F)
            f1 = cvt(f1, 0.0F, 45F, -58F, 45F);
        f1 += 58F;
        super.mesh.chunkSetAngles("Z_Fuel2", f1, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres1", cvt(1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness(), 0.0F, 28F, 0.0F, 180F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 1100F, 0.0F, 322F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM2", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 289F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Temp1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 400F, 1000F, 0.0F, 116.75F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Temp2", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Climb1", floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Oxypres1", 142.5F, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass3", 90F + setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass2", setNew.waypointAzimuth.getDeg(0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass1", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank1", cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        w.set(super.fm.getW());
        ((FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_TurnBank2", cvt(getBall(7D), -7F, 7F, -15F, 15F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank3a", ((FlightModelMain) (super.fm)).Or.getKren(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 1.5F, -1.5F));
        super.mesh.chunkSetAngles("Z_Pres1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Ny1", cvt(super.fm.getOverload(), -4F, 12F, -80.5F, 241.5F), 0.0F, 0.0F);
        super.mesh.chunkVisible("Z_GearGreen1", ((FlightModelMain) (super.fm)).CT.getGearL() > 0.99F && ((FlightModelMain) (super.fm)).CT.getGearR() > 0.99F);
        super.mesh.chunkVisible("Z_GearRed1", ((FlightModelMain) (super.fm)).CT.getGearL() < 0.01F && ((FlightModelMain) (super.fm)).CT.getGearR() < 0.01F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("Z_LampFuelL", ((FlightModelMain) (super.fm)).M.fuel < 500F);
        super.mesh.chunkVisible("Z_LampFuelR", ((FlightModelMain) (super.fm)).M.fuel < 500F);
        super.mesh.chunkVisible("Z_LampFuelCf", ((FlightModelMain) (super.fm)).M.fuel < 125F);
        super.mesh.chunkVisible("Z_Trim1", Math.abs(((FlightModelMain) (super.fm)).CT.getTrimElevatorControl()) < 0.05F);
        super.mesh.chunkVisible("Z_FireLamp", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage2", true);
            super.mesh.chunkVisible("Pricel_D0", false);
            super.mesh.chunkVisible("Pricel_D1", true);
            super.mesh.chunkVisible("Pricel1_D0", false);
            super.mesh.chunkVisible("Pricel1_D1", true);
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
            super.mesh.chunkVisible("Z_Z_RETICLE1", false);
            for(int i = 1; i < 7; i++)
                super.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);

            super.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.chunkVisible("Panel_D0", false);
            super.mesh.chunkVisible("Panel_D1", true);
            super.mesh.chunkVisible("XHullDamage2", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.chunkVisible("XHullDamage3", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
            super.mesh.chunkVisible("XHullDamage4", true);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) == 0);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage4", true);
            super.mesh.chunkVisible("XHullDamage1", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
        {
            super.mesh.chunkVisible("XHullDamage2", true);
            super.mesh.chunkVisible("XHullDamage3", true);
        }
        retoggleLight();
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(super.cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private static final float fuelGallonsScale[] = {
        0.0F, 8.25F, 17.5F, 36.5F, 54F, 90F, 108F
    };
    private static final float fuelGallonsAuxScale[] = {
        0.0F, 38F, 62.5F, 87F, 104F
    };
    private static final float speedometerScale[] = {
        0.0F, 42F, 65.5F, 88.5F, 111.3F, 134F, 156.5F, 181F, 205F, 227F, 
        249.4F, 271.7F, 294F, 316.5F, 339.5F
    };
    private static final float variometerScale[] = {
        -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 
        124F, 147F, 170F
    };
    private static final float k14TargetMarkScale[] = {
        -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F
    };
    private static final float k14TargetWingspanScale[] = {
        9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F
    };
    private static final float k14BulletDrop[] = {
        5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 
        8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 
        10.603F, 10.704F, 10.739F, 10.782F, 10.789F
    };
    private Point3d tmpP;
    private Vector3d tmpV;

    static 
    {
        Property.set(CLASS.THIS(), "normZNs", new float[] {
            0.89F, 0.89F, 0.86F, 0.89F
        });
    }



}