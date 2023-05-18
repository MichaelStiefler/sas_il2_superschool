// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 14/10/2015 10:04:25 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitF84G3.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, F84G3, Aircraft, Cockpit

public class CockpitF84G3 extends CockpitPilot
{
    private class Variables
    {

        float throttle;
        float prop;
        float mix;
        float stage;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;

        private Variables()
        {
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
                setNew.throttle = 0.85F * setOld.throttle + fm.CT.PowerControl * 0.15F;
                setNew.prop = 0.85F * setOld.prop + fm.CT.getStepControl() * 0.15F;
                setNew.stage = 0.85F * setOld.stage + (float)fm.EI.engines[0].getControlCompressor() * 0.15F;
                setNew.mix = 0.85F * setOld.mix + fm.EI.engines[0].getControlMix() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + waypointAzimuth() + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                float f = ((F84G3)aircraft()).k14Distance;
                setNew.k14w = (5F * CockpitF84G3.k14TargetWingspanScale[((F84G3)aircraft()).k14WingspanType]) / f;
                setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
                setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitF84G3.k14TargetMarkScale[((F84G3)aircraft()).k14WingspanType];
                setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((F84G3)aircraft()).k14Mode;
                Vector3d vector3d = aircraft().FM.getW();
                double d = 0.00125D * (double)f;
                float f1 = (float)Math.toDegrees(d * vector3d.z);
                float f2 = -(float)Math.toDegrees(d * vector3d.y);
                float f3 = floatindex((f - 200F) * 0.04F, CockpitF84G3.k14BulletDrop) - CockpitF84G3.k14BulletDrop[0];
                f2 += (float)Math.toDegrees(Math.atan(f3 / f));
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
        WayPoint waypoint = fm.AP.way.curr();
        if(waypoint == null)
        {
            return 0.0F;
        } else
        {
            waypoint.getP(tmpP);
            tmpV.sub(tmpP, fm.Loc);
            return (float)(57.295779513082323D * Math.atan2(-tmpV.y, tmpV.x));
        }
    }

    public CockpitF84G3()
    {
        super("3DO/Cockpit/F84G/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        bHasBoosters = true;
        boosterFireOutTime = -1L;
        pictGear = 0.0F;
        pictFlap = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        cockpitNightMats = (new String[] {
            "Instrumentos1", "Instrumentos2", "Instrumentos3", "Instrumentos4", "Instrumentos5", "Instrumentos6", "Instrumentos7", "GagePanel3", "GagePanel5", "needles"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(Math.abs(fm.CT.getTrimAileronControl() - fm.CT.trimAileron) > 1E-006F)
        {
            if(fm.CT.getTrimAileronControl() - fm.CT.trimAileron > 0.0F)
                mesh.chunkSetAngles("A_Trim1", 0.0F, 10F, 0.0F);
            else
                mesh.chunkSetAngles("A_Trim1", 0.0F, 10F, 0.0F);
        } else
        {
            mesh.chunkSetAngles("A_Trim1", 0.0F, 0.0F, 0.0F);
        }
        if(Math.abs(fm.CT.getTrimRudderControl() - fm.CT.trimRudder) > 1E-006F)
        {
            if(fm.CT.getTrimRudderControl() - fm.CT.trimRudder > 0.0F)
                mesh.chunkSetAngles("A_Trim2", 0.0F, 10F, 0.0F);
            else
                mesh.chunkSetAngles("A_Trim2", 0.0F, 10F, 0.0F);
        } else
        {
            mesh.chunkSetAngles("A_Trim2", 0.0F, 0.0F, 0.0F);
        }
        if(Math.abs(fm.CT.getTrimElevatorControl() - fm.CT.trimElevator) > 1E-006F)
        {
            if(fm.CT.getTrimElevatorControl() - fm.CT.trimElevator > 0.0F)
                mesh.chunkSetAngles("A_Trim3", 0.0F, 10F, 0.0F);
            else
                mesh.chunkSetAngles("A_Trim3", 0.0F, 10F, 0.0F);
        } else
        {
            mesh.chunkSetAngles("A_Trim3", 0.0F, 0.0F, 0.0F);
        }
        resetYPRmodifier();
        mesh.chunkSetAngles("Trim_Rud", -60F * fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Trim_Ele", 0.0F, 0.0F, 722F * fm.CT.getTrimElevatorControl());
        mesh.chunkSetAngles("Trim_Ail", 0.0F, 722F * fm.CT.getTrimAileronControl(), 0.0F);
        if((fm.AS.astateCockpitState & 2) == 0)
        {
            int i = ((F84G3)aircraft()).k14Mode;
            boolean flag = i < 2;
            mesh.chunkVisible("Z_Z_RETICLE", flag);
            flag = i > 0;
            mesh.chunkVisible("Z_Z_RETICLE1", flag);
            mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, setNew.k14y);
            resetYPRmodifier();
            Cockpit.xyz[0] = setNew.k14w;
            for(int j = 1; j < 7; j++)
            {
                mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
                mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
            }

        }
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.0F, 0.2F, 0.0F, 0.625F);
        mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_RightPedal", 15F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_LeftPedal", -15F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Throtle1", 0.0F, 0.0F, -70F * setNew.throttle);
        resetYPRmodifier();
        float f1 = fm.EI.engines[0].getStage();
        if(f1 > 0.0F && f1 < 7F)
            f1 = 0.0345F;
        else
            f1 = -0.05475F;
        Cockpit.xyz[2] = f1;
        mesh.chunkSetLocate("Z_EngShutOff", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_Column", (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F, 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 20F);
        mesh.chunkSetAngles("z_Flap", 0.0F, -38F * (pictFlap = 0.75F * pictFlap + 0.95F * fm.CT.FlapsControl), 0.0F);
        mesh.chunkSetAngles("Palanca_Tren", 0.0F, 0.0F, 46F * (pictGear = 0.8F * pictGear + 0.2F * fm.CT.GearControl));
        mesh.chunkSetAngles("Z_Target1", 0.0F, 0.0F, setNew.k14wingspan);
        mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Speedometer2", floatindex(cvt(fm.getSpeedKMH(), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        f1 = fm.EI.engines[0].getPowerOutput();
        mesh.chunkSetAngles("Z_Fuel1", cvt((float)Math.sqrt(f1), 0.0F, 1.0F, -59.5F, 123F), 0.0F, 0.0F);
        f1 = cvt(fm.M.fuel, 0.0F, 2760F, 0.0F, 270F);
        if(f1 < 45F)
            f1 = cvt(f1, 0.0F, 45F, -58F, 45F);
        f1 += 58F;
        mesh.chunkSetAngles("Z_Fuel2", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Oilpres1", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 28F, 0.0F, 180F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPM1", cvt(fm.EI.engines[0].getRPM(), 0.0F, 1100F, 0.0F, 322F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPM2", cvt(fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 289F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Temp1", cvt(fm.EI.engines[0].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Temp2", cvt(fm.EI.engines[0].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Climb1", floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Oxypres1", 142.5F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Compass2", 90F + setNew.azimuth, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Compass3", 90F + interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Compass1", setNew.azimuth, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TurnBank1", cvt(w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("Z_TurnBank2", cvt(getBall(7D), -7F, 7F, -15F, 15F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TurnBank3a", fm.Or.getKren(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, cvt(fm.Or.getTangage(), -45F, 45F, 1.5F, -1.5F));
        mesh.chunkSetAngles("Z_Pres1", cvt(fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Ny1", cvt(fm.getOverload(), -4F, 12F, -80.5F, 241.5F), 0.0F, 0.0F);
        mesh.chunkVisible("A_Tren", fm.CT.getGear() > 0.95F);
        mesh.chunkVisible("Z_GearRed1", fm.CT.getGear() < 0.05F || !fm.Gears.lgear || !fm.Gears.rgear);
        mesh.chunkVisible("Z_Alt_Alarm", fm.getAltitude() < 50F && fm.CT.getGear() < 0.05F);
        mesh.chunkVisible("Z_Hook", fm.CT.getArrestor() > 0.9F);
        mesh.chunkVisible("A_Brake", fm.CT.AirBrakeControl > 0.5F);
        mesh.chunkVisible("A_Eng_Fire", fm.AS.astateEngineStates[0] > 2);
        mesh.chunkVisible("A_Eng_OH", (double)fm.EI.engines[0].tOilOut > 125D);
        mesh.chunkVisible("A_ATO", fm.EI.getPowerOutput() > 0.8F && fm.Gears.onGround());
        mesh.chunkVisible("A_Fuel1", fm.M.fuel < 455F);
        mesh.chunkVisible("A_Fuel2", fm.M.fuel < 226F);
        if(tgun[0] == null)
            tgun[0] = ((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalDev07");
        if(tgun[0] != null)
            mesh.chunkVisible("A_Fuel3", tgun[0].countBullets() > 0);
        mesh.chunkVisible("TankMain", tgun[0].countBullets() == 0);
        mesh.chunkVisible("TankDrop", tgun[0].countBullets() > 0);
        if(gun[0] == null)
            gun[0] = ((Aircraft)fm.actor).getGunByHookName("_MGUN06");
        if(bgun[0] == null)
        {
            bgun[0] = ((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb01");
            bgun[1] = ((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb03");
        }
        if(rgun[0] == null)
        {
            rgun[0] = ((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalRock02");
            rgun[1] = ((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalRock22");
            rgun[2] = ((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalRock09");
        }
        if(gun[0] != null)
            mesh.chunkVisible("A_Guns_A", gun[0].countBullets() > 0);
        mesh.chunkVisible("A_Guns_E", gun[0].countBullets() < 50);
        if(bgun[0] != null)
            mesh.chunkVisible("A_Bomb_I", bgun[0].countBullets() > 0);
        if(bgun[1] != null)
            mesh.chunkVisible("A_Bomb_O", bgun[1].countBullets() > 0);
        if(rgun[0] != null)
            mesh.chunkVisible("A_Rock_I2", rgun[0].countBullets() > 0);
        if(rgun[1] != null)
            mesh.chunkVisible("A_Rock_I", rgun[1].countBullets() > 0);
        if(rgun[2] != null)
            mesh.chunkVisible("A_Rock_O", rgun[2].countBullets() > 0);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("XGlassDamage2", true);
            mesh.chunkVisible("Mira", false);
            mesh.chunkVisible("Mira_D", true);
            mesh.chunkVisible("Z_Z_RETICLE", false);
            mesh.chunkVisible("Z_Z_RETICLE1", false);
            for(int i = 1; i < 7; i++)
                mesh.chunkVisible("Z_Z_AIMMARK" + i, false);

            mesh.chunkVisible("Z_Z_MASK", false);
        }
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("XGlassDamage1", true);
            mesh.chunkVisible("XGlassDamage4", true);
        }
        if((fm.AS.astateCockpitState & 0x40) == 0);
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("XGlassDamage3", true);
        if((fm.AS.astateCockpitState & 8) != 0)
            if((fm.AS.astateCockpitState & 0x80) != 0);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("XGlassDamage4", true);
        if((fm.AS.astateCockpitState & 0x20) == 0);
        retoggleLight();
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(cockpitLightControl)
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
    protected boolean bHasBoosters;
    protected long boosterFireOutTime;
    private float pictGear;
    private float pictFlap;
    private Gun gun[] = {
        null, null, null, null
    };
    private BulletEmitter bgun[] = {
        null, null, null, null
    };
    private BulletEmitter rgun[] = {
        null, null, null, null
    };
    private BulletEmitter tgun[] = {
        null, null
    };
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










}
