
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.RadarWarningReceiverUtils;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.weapons.FuelTankGun_Tank18C;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.il2.objects.weapons.Missile;
import com.maddox.rts.Time;
import com.maddox.sound.*;
import com.maddox.sas1946.il2.util.CrossVersion;

import java.util.ArrayList;
import java.util.List;


// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, AircraftLH, F_18, Aircraft,
//            Cockpit, TypeSupersonic

public class CockpitF_18C extends CockpitPilot
{
    private class Variables
    {

        float altimeter;
        float throttlel;
        float throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float PDI;
        float ilsLoc;
        float ilsGS;
        float navDiviation0;
        float navDiviation1;
        boolean navTo0;
        boolean navTo1;
        float navDist0;
        float navDist1;
        float vspeed;
        float vspeed2;
        float dimPosition;
        float beaconDirection;
        float beaconRange;
        float pitch;
        float bank;
        float fpmPitch;
        float fpmYaw;
        boolean isGeneratorAllive;
        boolean isBatteryOn;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;
        float hsiLoc;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
            isGeneratorAllive = false;
            isBatteryOn = false;
        }

        Variables(Variables variables)
        {
            this();
        }
    }

    private class Radardata
    {

        Point3d p3d;
        Actor act;

        private Radardata()
        {
            p3d = new Point3d();
        }

        Radardata(Radardata radardata)
        {
            this();
        }
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            //aircraft().hierMesh().chunkVisible("Canopy_D0", false);
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            aircraft().hierMesh().chunkVisible("Head1_D0", false);
            aircraft().hierMesh().chunkVisible("HMask1_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            aircraft().hierMesh().chunkVisible("Seat1_D0", false);
            ((F_18)aircraft()).startCockpitSounds();
            return true;
        }
        else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        //aircraft().hierMesh().chunkVisible("Canopy_D0", true);
        aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        aircraft().hierMesh().chunkVisible("Head1_D0", true);
        aircraft().hierMesh().chunkVisible("HMask1_D0", true);
        aircraft().hierMesh().chunkVisible("Pilot1_D0", true);
        aircraft().hierMesh().chunkVisible("Seat1_D0", true);

        stopSoundEffects();
        ((F_18)aircraft()).stopCockpitSounds();
        super.doFocusLeave();
    }


    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            setNew.throttlel = (10F * setOld.throttlel + fm.EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + fm.EI.engines[1].getControlThrottle()) / 11F;
            float f = 200F;
            if(((F_18) aircraft()).radargunsight == 0 && ((F_18) aircraft()).k14Mode == 1)
                f = 200F;
            if(((F_18) aircraft()).radargunsight == 1 && ((F_18) aircraft()).k14Mode == 1)
                f = ((F_18) aircraft()).k14Distance;
            Point3d pointAC = ((Actor) (World.getPlayerAircraft())).pos.getAbsPoint();
            float z = (float)((Tuple3d) (pointAC)).z;
            if((((F_18) aircraft()).radargunsight == 2 || ((F_18) aircraft()).radargunsight == 3) && ((F_18) aircraft()).k14Mode == 1){
                f = z / (float)Math.cos(Math.toRadians(fm.Or.getPitch()-270F)) + 200F;
            }
            setNew.k14w = (5F * CockpitF_18C.k14TargetWingspanScale[((F_18) aircraft()).k14WingspanType]) / f;
            setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
            setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitF_18C.k14TargetMarkScale[((F_18) aircraft()).k14WingspanType];
            setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((F_18) aircraft()).k14Mode;
            Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
            double d = 0.00125D * (double)f;
            float f1 = (float)Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
            float f2 = -(float)Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
            float f3 = floatindex((f - 200F) * 0.04F, CockpitF_18C.k14BulletDrop) - CockpitF_18C.k14BulletDrop[0];
            if(((F_18) aircraft()).radargunsight == 2)
                f3 = floatindex((f - 200F) * 0.04F, CockpitF_18C.k14RocketDrop) - CockpitF_18C.k14RocketDrop[0];
            f2 += (float)Math.toDegrees(Math.atan(f3 / f));
            setNew.k14x = 0.92F * setOld.k14x + 0.08F * f1;
            setNew.k14y = 0.92F * setOld.k14y + 0.08F * f2;
            if(setNew.k14x > 9F)
                setNew.k14x = 9F;
            if(setNew.k14x < -9F)
                setNew.k14x = -9F;
            if(setNew.k14y > 9F)
                setNew.k14y = 9F;
            if(setNew.k14y < -9F)
                setNew.k14y = -9F;
            f = waypointAzimuth();
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f);
                setOld.waypointAzimuth.setDeg(f);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                if(fm.AS.listenLorenzBlindLanding && fm.AS.isILSBL)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                    bHSIILS = true;
                    bHSIDL = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
                }
                else if(fm.AS.listenTACAN)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = 0.0F;
                    bHSITAC = true;
                    bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITGT = bHSIUHF = false;
                }
                else if(fm.AS.listenNDBeacon || fm.AS.listenYGBeacon)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = 0.0F;
                    bHSIUHF = true;
                    bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = false;
                }
                else
                {
                    setNew.hsiLoc = 0.0F;
                    setNew.ilsLoc = 0.0F;
                    setNew.ilsGS = 0.0F;
                    bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
                }
            }
            else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
                bHSINAV = true;
                bHSIDL = bHSIILS = bHSIMAN = bHSITAC = bHSITGT = bHSIUHF = false;
            }
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
            setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
            setNew.vspeed2 = (49F * setOld.vspeed2 + fm.getVertSpeed()) / 50F;
            setNew.pitch = fm.Or.getPitch();
            setNew.bank = fm.Or.getRoll();
            Vector3d vector3d1 = new Vector3d();
            fm.Or.transformInv(fm.Vwld, vector3d1);
            setNew.fpmPitch = FMMath.RAD2DEG(-(float)Math.atan2(((Tuple3d) (vector3d1)).z, ((Tuple3d) (vector3d1)).x));
            setNew.fpmYaw = FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (vector3d1)).y, ((Tuple3d) (vector3d1)).x));
            if(cockpitDimControl)
            {
                if(setNew.dimPosition > 0.0F)
                    setNew.dimPosition = setOld.dimPosition - 0.05F;
            }
            else if(setNew.dimPosition < 1.0F)
                setNew.dimPosition = setOld.dimPosition + 0.05F;
            if(fm.EI.engines[0].getRPM() > 200F || fm.EI.engines[1].getRPM() > 200F)
                setNew.isGeneratorAllive = true;
            else
                setNew.isGeneratorAllive = false;
            if(fm.getSpeedKMH() > 200F || fm.Gears.nOfGearsOnGr > 2)
                setNew.isBatteryOn = true;
            else
                setNew.isBatteryOn = false;
            f = ((F_18) aircraft()).fSightCurForwardAngle;
            f1 = ((F_18) aircraft()).fSightCurSideslip;
            f2 = fm.getAltitude();
            f3 = (float)(-(Math.abs(fm.Vwld.length()) * Math.sin(Math.toRadians(Math.abs(fm.Or.getTangage())))) * 0.10189999639987946D);
            f3 += (float)Math.sqrt(f3 * f3 + 2.0F * f2 * 0.1019F);
            float f4 = Math.abs((float)fm.Vwld.length()) * (float)Math.cos(Math.toRadians(Math.abs(fm.Or.getTangage())));
            float f5 = (f4 * f3 + 10F) - 10F;
            alpha = 90F - Math.abs(fm.Or.getTangage()) - (float)Math.toDegrees(Math.atan(f5 / f2));
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

    public CockpitF_18C()
    {
        super("3DO/Cockpit/F-18/hier.him", "bf109");
        gun = new Gun[4];
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        tmpP = new Point3d();
        tmpV = new Vector3d();
        tmpMat = new Matrix3d();
        bNeedSetUp = true;
        isDimmer = false;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        blinkCounter = 0;
        bU4 = false;
        bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
        CrossVersion.setPrintCompassHeading(this, true);
        super.cockpitNightMats = (new String[] {
            "Instrument", "Gauges", "AH"
        });
        setNightMats(false);
        setNew.dimPosition = 1.0F;
        interpPut(new Interpolater(), null, Time.current(), null);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.05F);
        aoaWarnFX = aircraft().newSound("aircraft.usAOA_Slow", false);
        aoaWarnFX2 = aircraft().newSound("aircraft.usAOA_Mid", false);
        aoaWarnFX3 = aircraft().newSound("aircraft.usAOA_Fast", false);
        aoaWarnFX4 = aircraft().newSound("aircraft.usAOA_Stall", false);
        PullupWarn = aircraft().newSound("aircraft.vcenPullup", false);
        AltitudeWarn = aircraft().newSound("aircraft.vcenAltitude", false);
        OverGWarn = aircraft().newSound("aircraft.vcenOverG", false);
        MinimumSpeedWarn = aircraft().newSound("aircraft.vcenMinimumSpeed", false);
        MaximumSpeedWarn = aircraft().newSound("aircraft.vcenMaximumSpeed", false);
        EngineFireLeftWarn = aircraft().newSound("aircraft.vcenEngineFireLeft", false);
        EngineFireRightWarn = aircraft().newSound("aircraft.vcenEngineFireRight", false);
        lLastEngineFireLeftWarned = -1L;
        lLastEngineFireRightWarned = -1L;
        BingoFuelWarn = aircraft().newSound("aircraft.vcenBingoFuel", false);
        iBingoFuelWarned = 0;
        hudPitchRudderStr = new String[37];
        for(int i = 18; i >= 0; i--)
            hudPitchRudderStr[i] = "Z_Z_HUD_PITCH" + (90 - i * 5);

        for(int j = 1; j <= 18; j++)
            hudPitchRudderStr[j + 18] = "Z_Z_HUD_PITCHN" + j * 5;

        ((F_18) aircraft()).bWantBeaconKeys = true;  //TODO:Radar parameter
        t2 = 0L;
        t3 = 0L;
        FOV = 1.0D;
        ScX = 0.0059999997764825821D;
        ScY = 0.0000045;
        ScZ = 0.0010000000474974513D;
        FOrigX = 0.0F;
        FOrigY = 0.0F;
        nTgts = 10;
        RRange = 74000F;
        RClose = 50F;
        BRange = 0.1F;
        BRefresh = 1300;
        BSteps = 12;
        BDiv = BRefresh / BSteps;
        tBOld = 0L;
        radarPlane = new ArrayList();
        radarLock = new ArrayList();
        radarGround = new ArrayList();
        victim = new ArrayList();
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(168F, 83F, 0F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        hooknamed = new HookNamed(mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(168F, 83F, 0F);
        light2.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        tw = 0L;
        x = 0F;
        start = false;
        left = false;
        right = false;
        becondistance = 0F;
        oldleftscreen = 0;
        oldrightscreen = 0;
        oldhddnav = 0;
        LFlapStat = 0;
        LGearStat = 0;
        if(!useRealisticNavigationInstruments())
            super.mesh.materialReplace("Nav_Tacan", "Nav_WPT");
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bF18D = (aircraft() instanceof F_18D);
            bNeedSetUp = false;
            rwrutils = ((F_18) aircraft()).getRadarWarningReceiverUtils();
        }
        HUDgunsight();
        radarclutter(f);
        if(((F_18) aircraft()).Nvision)
            super.mesh.chunkVisible("Z_Z_NVision", true);
        else
            super.mesh.chunkVisible("Z_Z_NVision", false);
        int j = ((F_18) aircraft()).leftscreen;
        if(j != oldleftscreen)
        {
            if(j == 0)
                super.mesh.materialReplace("HDD_Fuel", "HDD_Fuel");
            if(j == 1)
                super.mesh.materialReplace("HDD_Fuel", "HDD_FuelFlow");
            if(j == 2)
                super.mesh.materialReplace("HDD_Fuel", "HDD_Engine");
            oldleftscreen = j;
        }
        if(j == 0)
            movescreenfuel();
        if(j == 1)
            movescreenfuelflow();
        if(j == 2)
            movescreenengines();
        if(bHSIILS)
        {
            if(oldhddnav != 1)
            {
                super.mesh.materialReplace("HDD_Nav", "HDD_NavILS");
                oldhddnav = 1;
            }
        }
        else if(bHSITAC)
        {
            if(oldhddnav != 2)
            {
                super.mesh.materialReplace("HDD_Nav", "HDD_NavTCN");
                oldhddnav = 2;
            }
        }
        else
        {
            if(oldhddnav != 0)
            {
                super.mesh.materialReplace("HDD_Nav", "HDD_Nav");
                oldhddnav = 0;
            }
        }
        if(bNeedSetUp)
        {
            super.mesh.chunkVisible("Int_Marker", false);
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        moveControls(f);
        HUD(f);
        drawSound(f);
        Navscreen(f);
        backupGauges(f);
        Warninglight();
        ILS();
        RWR();
    }

    private void RWR()
    {
        int totalrwr = 0;
        boolean warnflag = false;
        boolean warnAIR = false;
        boolean warnSAM = false;
        long newestTime = -1L;
        float newestX = 0F;
        float newestZ = 0F;
        RadarWarningReceiverUtils.RWRdata tempRrwrdata = rwrutils.new RWRdata();
        super.mesh.chunkVisible("Z_Z_RWR_threat", false);

        if(rwrutils.getMissileWarning() && setNew.isGeneratorAllive)
        {
            warnflag = true;
            resetYPRmodifier();
            ArrayList rhmissiles = new ArrayList();
            rhmissiles = rwrutils.getRHmissileList();
            for(int i = 0; i < rhmissiles.size() && totalrwr < RWR_MAX_DETECT; i++)
            {
                tempRrwrdata = (RadarWarningReceiverUtils.RWRdata)rhmissiles.get(i);
                float dir = tempRrwrdata.lastDetectDirection;
                float dst = cvt((float)tempRrwrdata.lastDetectDistance, 500F, 30000F, 0.003F, 0.022F);
                Cockpit.xyz[0] = -(float)Math.sin(Math.toRadians(dir)) * dst;
                Cockpit.xyz[2] = (float)Math.cos(Math.toRadians(dir)) * dst;
                String sni = (totalrwr < 10) ? "0" + Integer.toString(totalrwr) : Integer.toString(totalrwr);
                super.mesh.chunkVisible("Z_Z_RWR_" + sni, true);
                super.mesh.chunkSetLocate("Z_Z_RWR_" + sni, Cockpit.xyz, Cockpit.ypr);
                if(i == 0)
                {
                    super.mesh.materialReplace("RWR" + sni, "RWRmissile1");
                }
                else
                    super.mesh.materialReplace("RWR" + sni, "RWRmissile2");

                if(newestTime < tempRrwrdata.firstDetectTime)
                {
                    newestX = Cockpit.xyz[0];
                    newestZ = Cockpit.xyz[2];
                    newestTime = tempRrwrdata.firstDetectTime;
                }

                totalrwr++;
            }
        }
        if(rwrutils.getRadarLockedWarning() && setNew.isGeneratorAllive)
        {
            warnflag = true;
            resetYPRmodifier();
            ArrayList radarsLock = new ArrayList();
            radarsLock = rwrutils.getRadarLockList();
            for(int i = 0; i < radarsLock.size() && totalrwr < RWR_MAX_DETECT; i++)
            {
                tempRrwrdata = (RadarWarningReceiverUtils.RWRdata)radarsLock.get(i);
                float dir = tempRrwrdata.lastDetectDirection;
                float dst = cvt((float)tempRrwrdata.lastDetectDistance, 15000F, 30000F, 0.011F, 0.022F);
                Cockpit.xyz[0] = -(float)Math.sin(Math.toRadians(dir)) * dst;
                Cockpit.xyz[2] = (float)Math.cos(Math.toRadians(dir)) * dst;
                String sni = (totalrwr < 10) ? "0" + Integer.toString(totalrwr) : Integer.toString(totalrwr);
                super.mesh.chunkVisible("Z_Z_RWR_" + sni, true);
                super.mesh.chunkSetLocate("Z_Z_RWR_" + sni, Cockpit.xyz, Cockpit.ypr);
                if(totalrwr == 0)
                {
                    super.mesh.chunkVisible("Z_Z_RWR_threat", true);
                    super.mesh.chunkSetLocate("Z_Z_RWR_threat", Cockpit.xyz, Cockpit.ypr);
                }
                if(tempRrwrdata.actor instanceof Aircraft)
                {
                    warnAIR = true;
                    int idx = getRwrPlaneIdx(tempRrwrdata.actor);
                    if(idx != -1)
                    {
                        String sm = rwrPlaneMatString[idx];
                        if(rwrPlaneLock[idx])
                            super.mesh.materialReplace("RWR" + sni, sm + "_lock");
                        else
                            super.mesh.materialReplace("RWR" + sni, sm);
                    }
                    else
                        super.mesh.materialReplace("RWR" + sni, "RWR" + sni);
                }
                else
                {
                    warnSAM = true;
                    super.mesh.materialReplace("RWR" + sni, "RWRZsu234");
                }

                if(newestTime < tempRrwrdata.firstDetectTime)
                {
                    newestX = Cockpit.xyz[0];
                    newestZ = Cockpit.xyz[2];
                    newestTime = tempRrwrdata.firstDetectTime;
                }

                totalrwr++;
            }
        }
        if(rwrutils.getRadarSearchedWarning() && setNew.isGeneratorAllive)
        {
            warnflag = true;
            resetYPRmodifier();
            ArrayList radars = new ArrayList();
            radars = rwrutils.getRadarList();
            for(int i = 0; i < radars.size() && totalrwr < RWR_MAX_DETECT; i++)
            {
                tempRrwrdata = (RadarWarningReceiverUtils.RWRdata)radars.get(i);
                float dir = tempRrwrdata.lastDetectDirection;
                float dst = cvt((float)tempRrwrdata.lastDetectDistance, 24000F, 30000F, 0.0177F, 0.022F);
                Cockpit.xyz[0] = -(float)Math.sin(Math.toRadians(dir)) * dst;
                Cockpit.xyz[2] = (float)Math.cos(Math.toRadians(dir)) * dst;
                String sni = (totalrwr < 10) ? "0" + Integer.toString(totalrwr) : Integer.toString(totalrwr);
                super.mesh.chunkVisible("Z_Z_RWR_" + sni, true);
                super.mesh.chunkSetLocate("Z_Z_RWR_" + sni, Cockpit.xyz, Cockpit.ypr);
                if(tempRrwrdata.actor instanceof Aircraft)
                {
                    int idx = getRwrPlaneIdx(tempRrwrdata.actor);
                    if(idx != -1)
                    {
                        String sm = rwrPlaneMatString[idx];
                        super.mesh.materialReplace("RWR" + sni, sm);
                    }
                    else
                        super.mesh.materialReplace("RWR" + sni, "RWR" + sni);
                }
                else
                {
                    super.mesh.materialReplace("RWR" + sni, "RWRZsu234");
                }

                if(newestTime < tempRrwrdata.firstDetectTime)
                {
                    newestX = Cockpit.xyz[0];
                    newestZ = Cockpit.xyz[2];
                    newestTime = tempRrwrdata.firstDetectTime;
                }

                totalrwr++;
            }
        }

        if(newestTime > 0L)
        {
            super.mesh.chunkVisible("Z_Z_RWR_new", true);
            Cockpit.xyz[0] = newestX;
            Cockpit.xyz[2] = newestZ;
            super.mesh.chunkSetLocate("Z_Z_RWR_new", Cockpit.xyz, Cockpit.ypr);
        }
        else
            super.mesh.chunkVisible("Z_Z_RWR_new", false);

        for(int i = totalrwr; i < RWR_MAX_DETECT; i++)
        {
            String sni = (i < 10) ? "0" + Integer.toString(i) : Integer.toString(i);
            super.mesh.chunkVisible("Z_Z_RWR_" + sni, false);
        }
        super.mesh.chunkVisible("L_AI_SAM", warnflag && setNew.isGeneratorAllive);
        if(warnAIR && warnSAM)
            super.mesh.materialReplace("Wlight_AISAM", "Wlight_AI_SAM");
        else if(warnAIR)
            super.mesh.materialReplace("Wlight_AISAM", "Wlight_AI");
        else if(warnSAM)
            super.mesh.materialReplace("Wlight_AISAM", "Wlight_SAM");
    }

    private void ILS()  //TODO ILS
    {
        if(!((F_18) aircraft()).ILS || (!setNew.isBatteryOn && !setNew.isGeneratorAllive))
        {
            super.mesh.chunkVisible("Z_Z_ILS_Hor", false);
            super.mesh.chunkVisible("Z_Z_ILS_Ver", false);
            super.mesh.chunkVisible("Z_Z_ILS_Pitch", false);
            super.mesh.chunkVisible("Z_Z_ILS_AOA", false);
            return;
        }

        super.mesh.chunkVisible("Z_Z_ILS_Hor", true);
        super.mesh.chunkVisible("Z_Z_ILS_Ver", true);
        super.mesh.chunkVisible("Z_Z_ILS_Pitch", true);
        super.mesh.chunkVisible("Z_Z_ILS_AOA", true);

        resetYPRmodifier();
        float ilsloctmp = setNew.ilsLoc * setNew.ilsLoc * ((setNew.ilsLoc < 0)? -1F : 1F);
        Cockpit.xyz[0] = -cvt(ilsloctmp, -10000F, 10000F, -0.590F, 0.590F);
        mesh.chunkSetLocate("Z_Z_ILS_Hor", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        float ilsgstmp = setNew.ilsGS * setNew.ilsGS * ((setNew.ilsGS < 0)? -1F : 1F);
        Cockpit.xyz[2] = cvt(ilsgstmp, -0.25F, 0.25F, -0.590F, 0.590F);
        mesh.chunkSetLocate("Z_Z_ILS_Ver", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkVisible("Z_Z_ILS_Hor", bHSIDL || bHSIILS || bHSIMAN || bHSINAV || bHSITAC || bHSITGT || bHSIUHF);
        //mesh.chunkVisible("Z_Z_ILS_Ver", bHSIILS);
        resetYPRmodifier();
        float speed = cvt(fm.getSpeedKMH(), 200F, 450F, -0.09F, 0.09F);
        Cockpit.xyz[2] = speed;
        mesh.chunkSetLocate("Z_Z_ILS_Pitch", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[2] = -cvt(fm.getAOA(), 6.4F, 8.9F, -0.08F, 0.08F);
        mesh.chunkSetLocate("Z_Z_ILS_AOA", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
    }



    private void HUDgunsight()  //TODO Gunsight
    {
        if ((fm.AS.astateCockpitState & 2) == 0)
        {
            int i = ((F_18) aircraft()).k14Mode;
            resetYPRmodifier();
            Cockpit.xyz[0] = setNew.k14w;
            if (i == 0)
            {
                resetYPRmodifier();
                super.mesh.chunkSetAngles("Z_Z_Bomb", 0.0F, 0.0F, -fm.Or.getRoll());
                super.mesh.chunkSetAngles("Z_Z_Bombsteer", -setNew.k14x, 0.0F, 0.0F);
                super.mesh.chunkVisible("Z_Z_Bombsteer", true);
                super.mesh.chunkVisible("Z_Z_Bombmark3", true);
                super.mesh.chunkSetAngles("Z_Z_Bombmark", 0.0F, -1.4F * alpha - setNew.k14y, 0.0F);
                super.mesh.chunkVisible("Z_Z_RETICLE", false);
                super.mesh.chunkVisible("Z_Z_BulletdropL", false);
                super.mesh.chunkVisible("Z_Z_BulletdropR", false);
            }
            else if (i == 1)
            {
                super.mesh.chunkVisible("Z_Z_Bombsteer", false);
                super.mesh.chunkVisible("Z_Z_Bombmark3", false);
                if(((F_18) aircraft()).radargunsight == 1)
                {
                    super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x, -setNew.k14y, 0.0F);
                    super.mesh.chunkVisible("Z_Z_RETICLE", true);
                    super.mesh.chunkVisible("Z_Z_BulletdropL", false);
                    super.mesh.chunkVisible("Z_Z_BulletdropR", false);
                }
                else if(((F_18) aircraft()).radargunsight == 0)
                {
                    super.mesh.chunkVisible("Z_Z_RETICLE", false);
                    super.mesh.chunkVisible("Z_Z_BulletdropL", true);
                    super.mesh.chunkVisible("Z_Z_BulletdropR", true);
                    //super.mesh.chunkSetAngles("Z_Z_Bulletdrop", -setNew.k14x - ((FlightModelMain) (fm)).Or.getRoll(), 0.0F, -setNew.k14y);
                    super.mesh.chunkSetAngles("Z_Z_Bulletdrop", setNew.k14x * 27F, 0.0F, setNew.k14y * 10F);
                    resetYPRmodifier();
                    Cockpit.xyz[1] = -setNew.k14y / 100F;
                    super.mesh.chunkSetLocate("Z_Z_BulletdropL", Cockpit.xyz, Cockpit.ypr);
                    Cockpit.xyz[1] = setNew.k14y / 100F;
                    super.mesh.chunkSetLocate("Z_Z_BulletdropR", Cockpit.xyz, Cockpit.ypr);
                    super.mesh.chunkVisible("Z_Z_radarlock", false);
                    super.mesh.chunkVisible("Z_Z_missilelock", false);
                }
                else if(((F_18) aircraft()).radargunsight == 2)
                {
                    super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x * 0.75F, -setNew.k14y * 0.75F, 0.0F);
                    super.mesh.chunkVisible("Z_Z_RETICLE", true);
                    super.mesh.chunkVisible("Z_Z_BulletdropL", false);
                    super.mesh.chunkVisible("Z_Z_BulletdropR", false);
                    super.mesh.chunkVisible("Z_Z_radarlock", false);
                    super.mesh.chunkVisible("Z_Z_missilelock", false);
                }
                else if(((F_18) aircraft()).radargunsight == 3)
                {
                    super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x * 0.22F, -setNew.k14y * 0.22F, 0.0F);
                    super.mesh.chunkVisible("Z_Z_RETICLE", true);
                    super.mesh.chunkVisible("Z_Z_BulletdropL", false);
                    super.mesh.chunkVisible("Z_Z_BulletdropR", false);
                    super.mesh.chunkVisible("Z_Z_radarlock", false);
                    super.mesh.chunkVisible("Z_Z_missilelock", false);
                }
            }
            else
            {
                super.mesh.chunkVisible("Z_Z_RETICLE", false);
                super.mesh.chunkVisible("Z_Z_Bombsteer", false);
                super.mesh.chunkVisible("Z_Z_Bombmark3", false);
                super.mesh.chunkVisible("Z_Z_BulletdropL", false);
                super.mesh.chunkVisible("Z_Z_BulletdropR", false);
            }
            if(i != 1)
            {
                super.mesh.chunkVisible("Z_Z_radarlock", false);
                super.mesh.chunkVisible("Z_Z_missilelock", false);
            }
        }
    }

    public void radarclutter(float r)  //TODO: Radar
    {
        ScY = 0.0000045F * ((F_18) aircraft()).radarrange;
        boolean radar = false;
        if(!setNew.isGeneratorAllive || !((F_18) aircraft()).radartoggle)
        {
            radar = false;
            start = false;
            ground = false;
            if(oldrightscreen != 0)
                super.mesh.materialReplace("HDDR", "HDDR");
            oldrightscreen = 0;
            super.mesh.chunkVisible("Z_Z_RADAR_AH", false);
            super.mesh.chunkVisible("Z_Z_lockgate", false);
            super.mesh.chunkVisible("Z_Z_Scan_1", false);
            for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + j, false);
            for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + j, false);
            for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + j, false);
            for(int j = 1; j < 4; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + j, false);
            super.mesh.chunkVisible("Z_Z_dif+", false);
            super.mesh.chunkVisible("Z_Z_dif-", false);
            super.mesh.chunkVisible("Z_Z_Radarbrg", false);
            super.mesh.chunkVisible("Z_Z_RADAR_MBRG", false);
            super.mesh.chunkVisible("Z_Z_RADAR_TBRG", false);
            ((F_18) aircraft()).radarmode = 0;
            ((F_18) aircraft()).lockmode = 0;
        }
        else
        {
            radar = true;
            if(((F_18) aircraft()).k14Mode != 1)
            {
                super.mesh.chunkVisible("Z_Z_radarlock", false);
                super.mesh.chunkVisible("Z_Z_missilelock", false);
            }
        }
        for(int ra = 1; ra < 3; ra++)
            super.mesh.chunkVisible("Z_Z_RADAR_Mach_" + ra, radar);
        for(int ra = 1; ra < 4; ra++)
            super.mesh.chunkVisible("Z_Z_RADAR_Speed_" + ra, radar);
        for(int ra = 1; ra < 3; ra++)
            super.mesh.chunkVisible("Z_Z_RADAR_ALT_" + ra, radar);
        super.mesh.chunkVisible("Z_Z_RADAR_AH", radar);
        if(!radar)
        {
            for(int j = 0; j <= nTgts; j++)
            {
                String m = "Z_Z_RadarMark" + j;
                if(super.mesh.isChunkVisible(m))
                     super.mesh.chunkVisible(m, false);
            }
            return;
        }
        float limit = 0F;
        long ts = Time.current();
        if(((F_18) aircraft()).radarvrt == 0 && ((F_18) aircraft()).radarhol == 0)
            super.mesh.chunkVisible("Z_Z_lockgate", false);
        else
            super.mesh.chunkVisible("Z_Z_lockgate", true);
        resetYPRmodifier();
        Cockpit.xyz[1] = -((F_18) aircraft()).radarvrt * 1.3333F;
        Cockpit.xyz[2] = ((F_18) aircraft()).radarhol * ((F_18) aircraft()).radarrange * 1.3333F;
        super.mesh.chunkSetLocate("Z_Z_lockgate", Cockpit.xyz, Cockpit.ypr);
        boolean flag = false;
        if(((F_18) aircraft()).radarmode == 0 && ((F_18) aircraft()).lockmode == 0)
        {
            if(oldrightscreen != 1)
                super.mesh.materialReplace("HDDR", "radarframe");
            oldrightscreen = 1;
            radarselection();
            limit = 0.03625F;
            super.mesh.chunkVisible("Z_Z_RADAR_MBRG", false);
            super.mesh.chunkVisible("Z_Z_RADAR_TBRG", false);
            if((((F_18) aircraft()).radarvrt != 0F || ((F_18) aircraft()).radarhol != 0F) && t3  + 60000L < ts)
            {
                ((F_18) aircraft()).radarvrt = 0F;
                ((F_18) aircraft()).radarhol = 0F;
                t3 = ts;
            }
        }
        if(((F_18) aircraft()).radarmode == 0 && ((F_18) aircraft()).lockmode == 1)
        {
            if(oldrightscreen != 1)
                super.mesh.materialReplace("HDDR", "radarframe");
            oldrightscreen = 1;
            radarselection();
            limit = 0.00875F;
            radaracquire(r);
            radarlock();
            flag = true;
        }
        else if(((F_18) aircraft()).radarmode == 1)
        {
            if(oldrightscreen != 1)
                super.mesh.materialReplace("HDDR", "radarframe");
            oldrightscreen = 1;
            radarselection();
            limit = 0.01375F;
            radarboresight(r);
            radarlock();
            if(((F_18) aircraft()).lockmode == 1)
                flag = true;
            else
                flag = false;
        }
        else
        {
            flag = false;
        }
        for(int j = 1; j < 3; j++)
            super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + j, flag);
        for(int j = 1; j < 3; j++)
            super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + j, flag);
        for(int j = 1; j < 3; j++)
            super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + j, flag);
        for(int j = 1; j < 4; j++)
            super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + j, flag);
        super.mesh.chunkVisible("Z_Z_dif+", flag);
        super.mesh.chunkVisible("Z_Z_dif-", flag);
        super.mesh.chunkVisible("Z_Z_Radarbrg", flag);
        super.mesh.chunkVisible("Z_Z_RADAR_MBRG", flag);
        super.mesh.chunkSetAngles("Z_Z_RADAR_AH", 0.0F, setNew.bank, 0.0F);
        float f = calculateMach();
        float f1 = (float)(int)f * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_Mach_1", 0.0F, 0.0F, f1);
        float f2 = (float)((int)(f * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_Mach_2", 0.0F, 0.0F, f2 - f1);
        f = (fm.getSpeedKMH() * 0.539956803455F) / 100F;
        f1 = (float)(int)f * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_Speed_1", 0.0F, 0.0F, f1);
        f2 = (float)((int)(f * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_Speed_2", 0.0F, 0.0F, f2 - f1);
        float f3 = (float)((int)(f * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_Speed_3", 0.0F, 0.0F, f3 - f1);
        f = (fm.getAltitude() * 3.28084F) / 10000F;
        f1 = (float)(int)f * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_ALT_1", 0.0F, 0.0F, f1);
        f2 = (float)((int)(f * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_ALT_2", 0.0F, 0.0F, f2 - f1);
        if(((F_18) aircraft()).radarmode < 2)
        {
            super.mesh.chunkVisible("Z_Z_Scan_1", true);
            ground = false;
            if(!start)
            {
                right = true;
                left = false;
            }
            if(x < -limit)
            {
                right = true;
                left = false;
            }
            if(x > limit)
            {
                right = false;
                left = true;
                start = true;
            }
            if(left && !right)
                if(ts > t2 + 5L)
                {
                    x -= 0.0025F;
                    t2 = ts;
                }
            if(right && !left)
                if(ts > t2 + 5L)
                {
                    x+=0.0025F;
                    t2 = ts;
                }
            resetYPRmodifier();
            if(((F_18) aircraft()).lockmode == 0 || ((F_18) aircraft()).radarmode == 1)
                Cockpit.xyz[0] = x * 1.3333F;
            if(((F_18) aircraft()).lockmode == 1 && ((F_18) aircraft()).radarmode == 0)
                Cockpit.xyz[0] = (x - ((F_18) aircraft()).radarvrt / 4F) * 1.3333F;
            super.mesh.chunkSetLocate("Z_Z_Scan_1", Cockpit.xyz, Cockpit.ypr);
        }
        else if(((F_18) aircraft()).radarmode == 2)
        {
            if(((F_18) aircraft()).lockmode == 0)
            {
                radarground();
                super.mesh.chunkVisible("Z_Z_Scan_1", true);
            }
            if(((F_18) aircraft()).lockmode == 1)
            {
                radarlock();
                radaracquireground(r);
                super.mesh.chunkVisible("Z_Z_Scan_1", false);
            }
            start = false;
            if(!ground)
            {
                right = true;
                left = false;
            }
            if(y < -57F)
            {
                right = true;
                left = false;
            }
            if(y > 57F)
            {
                right = false;
                left = true;
                ground = true;
            }
            if(left && !right)
                if(ts > t2 + 5L)
                {
                    y -= 2F;
                    t2 = ts;
                }
            if(right && !left)
                if(ts > t2 + 5L)
                {
                    y += 2F;
                    t2 = ts;
                }
            x = cvt(y, -50F, 50F, 0.145F, -0.145F);
            super.mesh.chunkSetAngles("Z_Z_Scan_1", 0.0F, y, 0.0F);
            if(oldrightscreen != 2)
                super.mesh.materialReplace("HDDR", "Groundradar");
            oldrightscreen = 2;
            super.mesh.chunkVisible("Z_Z_RADAR_TBRG", false);
        }
    }

    private int iLockState()
    {
        if(!(aircraft() instanceof TypeGuidedMissileCarrier))
            return 0;
        else
            return ((TypeGuidedMissileCarrier)aircraft()).getGuidedMissileUtils().getMissileLockState();
    }

    public void radarlock()  //TODO radarlock
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {
                int i1 = radarLock.size();
                if(i1 > 0)
                {
                    int nt = 0;
                    int k = 0;
                    if(i1 > 0)
                    {
                        double x1 = ((Tuple3d) ((Point3d)radarLock.get(k))).x;
                        if(x1 > (double)RClose && nt <= nTgts)
                        {
                            FOV = 300D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                            double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                            double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).z * FOV; //distance
                            float f = FOrigX + (float)(NewX * 0.0099999997764825821D) + ((float)Math.sin(Math.toRadians(fm.Or.getKren())) * 0.011F); //FOrigX currently do nothing
                            float f1 = FOrigY + (float)(NewY * 0.0099999997764825821D);
                            if(f1 > 0.3F)
                                f1 = 0.3F;
                            if(f1 < -0.3F)
                                f1 = -0.3F;
                            if(f > 0.3F)
                                f = 0.3F;
                            if(f < -0.3F)
                                f = -0.3F;
                            String m = "Z_Z_radarlock";
                            super.mesh.setCurChunk(m);
                            resetYPRmodifier();
                            Cockpit.xyz[1] = -f;
                            Cockpit.xyz[2] = f1;
                            super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                            super.mesh.render();
                            //if(f>=0.18F || f<=-0.18F || f1>0.36F)
                                //super.mesh.chunkVisible(m, false);
                            //else
                            if(!super.mesh.isChunkVisible(m))
                                super.mesh.chunkVisible(m, true);
                            if(iLockState() == 2)
                                super.mesh.chunkVisible("Z_Z_missilelock", true);
                            else
                                super.mesh.chunkVisible("Z_Z_missilelock", false);
                        }
                        if(x1 > (double)RClose && nt <= nTgts)
                        {
                            FOV = 300D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                            double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                            double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).z * FOV; //distance
                            float f = FOrigX + (float)(NewX * 0.000099999997764825821D) + ((float)Math.sin(Math.toRadians(fm.Or.getKren())) * 0.011F); //FOrigX currently do nothing
                            float f1 = FOrigY + (float)(NewY * 0.000099999997764825821D);
                            String m = "Z_Z_RADAR_TBRG";
                            super.mesh.setCurChunk(m);
                            resetYPRmodifier();
                            Cockpit.xyz[0] = -f * 1.3333F;
                            Cockpit.xyz[2] = f1 * 1.3333F;
                            super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                            super.mesh.render();
                            //if(f>=0.18F || f<=-0.18F || f1>0.36F)
                                //super.mesh.chunkVisible(m, false);
                            //else
                            if(!super.mesh.isChunkVisible(m))
                                super.mesh.chunkVisible(m, true);
                        }
                    }
                }
                else
                {    // hide everything when there's no enemy
                    if(super.mesh.isChunkVisible("Z_Z_radarlock"))
                        super.mesh.chunkVisible("Z_Z_radarlock", false);
                    if(super.mesh.isChunkVisible("Z_Z_missilelock"))
                        super.mesh.chunkVisible("Z_Z_missilelock", false);
                    if(super.mesh.isChunkVisible("Z_Z_RADAR_TBRG"))
                        super.mesh.chunkVisible("Z_Z_RADAR_TBRG", false);
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radarselection()  //TODO scan selection
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            radarPlane.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {
                Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                Radardata rdd = new Radardata();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy()) //basically tell that target is not your own aircraft and not friendly aircraft
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(pointAC);
                        Point3d pointOrtho = new Point3d();
                        pointOrtho.set(actor.pos.getAbsPoint());
                        pointOrtho.sub(pointAC);
                        orientAC.transformInv(pointOrtho);
                        float f = Mission.cur().curCloudsType();
                        double v = ((x + ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F)) / ScX) / (30D / ((Tuple3d) (pointOrtho)).x) * 4F;
                        //HUD.log(AircraftHotKeys.hudLogWeaponId, "scan " + Math.round(v) + "target " + Math.round(((Tuple3d) (pointOrtho)).y));
                        if(right)
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < v + 1000D && ((Tuple3d) (pointOrtho)).y > v - 8000D && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.46397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.46397023426))
                            {
                                rdd.p3d = pointOrtho;
                                rdd.act = actor;
                                radarPlane.add(rdd);
                                tw = Time.current();
                            }
                        if(left)
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < v + 8000D && ((Tuple3d) (pointOrtho)).y > v - 1000D && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.46397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.46397023426))
                            {
                                rdd.p3d = pointOrtho;
                                rdd.act = actor;
                                radarPlane.add(rdd);
                                tw = Time.current();
                            }
                    }
                }
                int i1 = radarPlane.size();
                if(i1 > 0)
                {
                    int nt = 0;
                    int k = 0;
                    //HUD.log(AircraftHotKeys.hudLogWeaponId, "target num" + k);
                    for(int j = 0; j < i1; j++)
                    {
                        Tuple3d tuppl = (Tuple3d) ((Radardata) (radarPlane.get(j))).p3d;
                        double x = tuppl.x;
                        if(x > (double)RClose && nt <= nTgts)
                        {
                            FOV = 30D / x; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                            double NewX = -tuppl.y * FOV; // spanning
                            double NewY = tuppl.x; //distance
                            float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F); //FOrigX currently do nothing
                            float f1 = FOrigY + (float)(NewY * ScY);
                            if(f1 < 0)
                                f1 = 0;
                            nt++; // number of marks, from 0 -> 10
                            String m = "Z_Z_RadarMark" + nt;
                            super.mesh.setCurChunk(m);
                            resetYPRmodifier();
                            Cockpit.xyz[1] = -f * 1.3333F;
                            Cockpit.xyz[2] = f1 * 1.3333F;
                            super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                            super.mesh.render();
                            if(f >= 0.18F || f <= -0.18F || f1 > 0.36F)
                                super.mesh.chunkVisible(m, false);
                            else if(!super.mesh.isChunkVisible(m))
                                super.mesh.chunkVisible(m, true);

                        }
                    }
                    for(int j = nt + 1; j <= nTgts; j++)
                    {
                        String m = "Z_Z_RadarMark" + j;
                        if(super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, false);
                    }
                }
                else
                {    // hide everything when there's no enemy
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    for(int j = 0; j <= nTgts + 1; j++)
                    {
                        String m = "Z_Z_RadarMark" + j;
                        if(super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, false);
                    }

                }
            }

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radaracquire(float r)  //TODO scan acquire
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            radarLock.clear();
            victim.clear();
            double v = -((((F_18) aircraft()).radarvrt + ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F)) / ScX);
            double h = ((((F_18) aircraft()).radarhol * ((F_18) aircraft()).radarrange) / (ScY));
            float mach = 0F;
            float alt = 0F;
            float dif = 0F;
            float HDG = 0F;
            float brg = 0F;
            float test = 0F;
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {
                Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy()) //basically tell that target is not your own aircraft and not friendly aircraft
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(pointAC);
                        Point3d pointOrtho = new Point3d();
                        pointOrtho.set(actor.pos.getAbsPoint());
                        pointOrtho.sub(pointAC);
                        orientAC.transformInv(pointOrtho);
                        //HUD.log(AircraftHotKeys.hudLogWeaponId, "target heading" + HDG);
                        float f = Mission.cur().curCloudsType();
                        if(((Tuple3d) (pointOrtho)).x > h - 500D && ((Tuple3d) (pointOrtho)).x < h + 500D && ((Tuple3d) (pointOrtho)).x < 48000D && ((Tuple3d) (pointOrtho)).y < v / (30D / ((Tuple3d) (pointOrtho)).x) + 500D && ((Tuple3d) (pointOrtho)).y > v / (30D / ((Tuple3d) (pointOrtho)).x) - 500D && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.56397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.56397023426))
                        {
                            radarLock.add(pointOrtho);
                            victim.add(actor);
                            ((F_18) aircraft()).setSemiActiveRadarLockedActor(actor);
                        }
                    }
                }
                i = victim.size();
                if(i > 0)
                {
                    for(int j = 0; j < i; j++)
                    {
                        Actor avictim = (Actor)victim.get(j);
                        Vector3d tarvec = new Vector3d();
                        tarvec.set(avictim.pos.getAbsPoint());
                        Point3d target = new Point3d();
                        target.set(avictim.pos.getAbsPoint());
                        mach = ((float)avictim.getSpeed(tarvec) * 3.6F / getMachForAlt((float)((Tuple3d) (target)).z)) * 1.621371F;
                        alt = ((float)((Tuple3d) (target)).z * 3.28084F) / 10000F;
                        dif = ((fm.getAltitude() - (float)((Tuple3d) (target)).z) * 3.28084F) / 1000F;
                        Orient tgt = ((Actor) (avictim)).pos.getAbsOrient();
                        Orient orient = ((Actor) (ownaircraft)).pos.getAbsOrient();
                        HDG = (normalizeDegree(tgt.getAzimut() - 270F)) / 100F;
                        brg = normalizeDegree(-normalizeDegree(setNew.azimuth.getDeg(r) + 90F) + (normalizeDegree(tgt.getAzimut() - 270F)));
                    }
                }
                int i1 = radarLock.size();
                if(i1 > 0)
                {
                    int nt = 0;
                    int k = 0;
                    float fma = (float)(int)mach * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Mach_1", 0.0F, 0.0F, fma);
                    float fma2 = (float)((int)(mach * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Mach_2", 0.0F, 0.0F, fma2 - fma);
                    fma = (float)(int)alt * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_ALT_1", 0.0F, 0.0F, fma);
                    fma2 = (float)((int)(alt * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_ALT_2", 0.0F, 0.0F, fma2 - fma);
                    fma = (float)(int)Math.abs(dif) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_1", 0.0F, 0.0F, fma);
                    fma2 = (float)((int)(Math.abs(dif) * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_2", 0.0F, 0.0F, fma2 - fma);
                    fma = (float)(int)HDG * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_1", 0.0F, 0.0F, fma);
                    fma2 = (float)((int)(HDG * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_2", 0.0F, 0.0F, fma2 - fma);
                    float fma3 = (float)((int)(HDG * 100F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_3", 0.0F, 0.0F, fma3 - fma);
                    super.mesh.chunkSetAngles("Z_Z_Radarbrg", 0.0F, brg, 0.0F);
                    if(dif > 0)
                    {
                        super.mesh.chunkVisible("Z_Z_dif+", false);
                        super.mesh.chunkVisible("Z_Z_dif-", true);
                    }
                    else
                    {
                        super.mesh.chunkVisible("Z_Z_dif+", true);
                        super.mesh.chunkVisible("Z_Z_dif-", false);
                    }
                    double x1 = ((Tuple3d) ((Point3d)radarLock.get(k))).x;
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                        FOV = 30D / x1;
                        double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV;  // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).x;  //distance
                        float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F);
                        float f1 = FOrigY + (float)(NewY * ScY);
                        ((F_18) aircraft()).radarvrt = f;
                        ((F_18) aircraft()).radarhol = f1 / (((F_18) aircraft()).radarrange);
                        if(((F_18) aircraft()).radarvrt > 0.03625F || ((F_18) aircraft()).radarvrt < -0.03625F)
                            super.mesh.chunkVisible("Z_Z_Scan_1", false);
                        if(f1 < 0)
                            f1 = 0;
                        String m = "Z_Z_lockgate";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f * 1.3333F;
                        Cockpit.xyz[2] = f1 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f >= 0.18F || f <= -0.18F || f1 > 0.36F)
                            super.mesh.chunkVisible(m, false);
                        else if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);

                    }
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                        FOV = 30D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                        double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F); //FOrigX currently do nothing
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 < 0)
                            f1 = 0;
                        String m = "Z_Z_RadarMark0";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f * 1.3333F;
                        Cockpit.xyz[2] = f1 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f >= 0.18F || f <= -0.18F || f1 > 0.36F)
                            super.mesh.chunkVisible(m, false);
                        else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);

                    }
                }
                else
                {    // hide everything when there's no enemy
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    if(super.mesh.isChunkVisible("Z_Z_lockgate"))
                        super.mesh.chunkVisible("Z_Z_lockgate", false);
                    for(int j = 1; j < 3; j++)
                        super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + j, false);
                    for(int j = 1; j < 3; j++)
                        super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + j, false);
                    for(int j = 1; j < 3; j++)
                        super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + j, false);
                    for(int j = 1; j < 4; j++)
                        super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + j, false);
                    super.mesh.chunkVisible("Z_Z_dif+", false);
                    super.mesh.chunkVisible("Z_Z_dif-", false);
                    super.mesh.chunkVisible("Z_Z_Radarbrg", false);
                    ((F_18) aircraft()).lockmode = 0;
                    ((F_18) aircraft()).setSemiActiveRadarLockedActor(null);
                }
            }

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radarboresight(float r)  //TODO scan boresight
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            radarLock.clear();
            victim.clear();
            float mach = 0F;
            float alt = 0F;
            float dif = 0F;
            float HDG = 0F;
            float brg = 0F;
            long ts = Time.current();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {
                Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy()) //basically tell that target is not your own aircraft and not friendly aircraft
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(pointAC);
                        Point3d pointOrtho = new Point3d();
                        pointOrtho.set(actor.pos.getAbsPoint());
                        pointOrtho.sub(pointAC);
                        orientAC.transformInv(pointOrtho);
                        if(ts > t4 + 5L)
                        {
                            range += 1000F;
                            t4 = ts;
                        }

                        //HUD.log(AircraftHotKeys.hudLogWeaponId, "target heading" + HDG);
                        float f = Mission.cur().curCloudsType();
                        if(range + 700F >= 16000F - (double)(350F * f))
                            range = 0F;
                        double v = ((x + ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F)) / ScX) / (30D / ((Tuple3d) (pointOrtho)).x) * 4F;
                        if(((Tuple3d) (pointOrtho)).x < range + 700F && ((Tuple3d) (pointOrtho)).y < v + 8000D && ((Tuple3d) (pointOrtho)).y > v - 8000D && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.56397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.56397023426))
                        {
                            radarLock.add(pointOrtho);
                            victim.add(actor);
                            range = (float) ((Tuple3d) (pointOrtho)).x;
                            ((F_18) aircraft()).lockmode = 1;
                            ((F_18) aircraft()).setSemiActiveRadarLockedActor(actor);
                        }
                    }
                }
                i = victim.size();
                if(i > 0)
                {
                    for(int j = 0; j < i; j++)
                    {
                        Actor avictim = (Actor)victim.get(j);
                        Vector3d tarvec = new Vector3d();
                        tarvec.set(avictim.pos.getAbsPoint());
                        Point3d target = new Point3d();
                        target.set(avictim.pos.getAbsPoint());
                        mach = ((float)avictim.getSpeed(tarvec) * 3.6F / getMachForAlt((float)((Tuple3d) (target)).z)) * 1.621371F;
                        alt = ((float)((Tuple3d) (target)).z * 3.28084F) / 10000F;
                        dif = ((fm.getAltitude() - (float)((Tuple3d) (target)).z) * 3.28084F) / 1000F;
                        Orient tgt = ((Actor) (avictim)).pos.getAbsOrient();
                        Orient orient = ((Actor) (ownaircraft)).pos.getAbsOrient();
                        HDG = (normalizeDegree(tgt.getAzimut() - 270F)) / 100F;
                        brg = normalizeDegree(-normalizeDegree(setNew.azimuth.getDeg(r) + 90F) + (normalizeDegree(tgt.getAzimut() - 270F)));
                    }
                }
                int i1 = radarLock.size();
                if(i1 > 0)
                {
                    int nt = 0;
                    int k = 0;
                    float fma = (float)(int)mach * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Mach_1", 0.0F, 0.0F, fma);
                    float fma2 = (float)((int)(mach * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Mach_2", 0.0F, 0.0F, fma2 - fma);
                    fma = (float)(int)alt * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_ALT_1", 0.0F, 0.0F, fma);
                    fma2 = (float)((int)(alt * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_ALT_2", 0.0F, 0.0F, fma2 - fma);
                    fma = (float)(int)Math.abs(dif) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_1", 0.0F, 0.0F, fma);
                    fma2 = (float)((int)(Math.abs(dif) * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_2", 0.0F, 0.0F, fma2 - fma);
                    fma = (float)(int)HDG * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_1", 0.0F, 0.0F, fma);
                    fma2 = (float)((int)(HDG * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_2", 0.0F, 0.0F, fma2 - fma);
                    float fma3 = (float)((int)(HDG * 100F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_3", 0.0F, 0.0F, fma3 - fma);
                    super.mesh.chunkSetAngles("Z_Z_Radarbrg", 0.0F, brg, 0.0F);
                    if(dif > 0)
                    {
                        super.mesh.chunkVisible("Z_Z_dif+", false);
                        super.mesh.chunkVisible("Z_Z_dif-", true);
                    }
                    else
                    {
                        super.mesh.chunkVisible("Z_Z_dif+", true);
                        super.mesh.chunkVisible("Z_Z_dif-", false);
                    }
                    double x1 = ((Tuple3d) ((Point3d)radarLock.get(k))).x;
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                        FOV = 30D / x1;
                        double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F);
                        float f1 = FOrigY + (float)(NewY * ScY);
                        ((F_18) aircraft()).radarvrt = f;
                        ((F_18) aircraft()).radarhol = f1;
                        if(f1 < 0)
                            f1 = 0;
                        String m = "Z_Z_lockgate";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f * 1.3333F;
                        Cockpit.xyz[2] = f1 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f >= 0.18F || f <= -0.18F || f1 > 0.36F)
                            super.mesh.chunkVisible(m, false);
                        else if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);

                    }
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                        FOV = 30D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                        double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F); //FOrigX currently do nothing
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 < 0)
                            f1 = 0;
                        String m = "Z_Z_RadarMark0";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f * 1.3333F;
                        Cockpit.xyz[2] = f1 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f >= 0.18F || f <= -0.18F || f1 > 0.36F)
                            super.mesh.chunkVisible(m, false);
                        else if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);

                    }
                }
                else
                {    // hide everything when there's no enemy
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    if(super.mesh.isChunkVisible("Z_Z_lockgate"))
                        super.mesh.chunkVisible("Z_Z_lockgate", false);
                    for(int j = 1; j < 3; j++)
                        super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + j, false);
                    for(int j = 1; j < 3; j++)
                        super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + j, false);
                    for(int j = 1; j < 3; j++)
                        super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + j, false);
                    for(int j = 1; j < 4; j++)
                        super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + j, false);
                    super.mesh.chunkVisible("Z_Z_dif+", false);
                    super.mesh.chunkVisible("Z_Z_dif-", false);
                    super.mesh.chunkVisible("Z_Z_Radarbrg", false);
                    ((F_18) aircraft()).lockmode = 0;
                    ((F_18) aircraft()).setSemiActiveRadarLockedActor(null);
                }
            }

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radarground()  //TODO scan selection
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            radarGround.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {
                Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                Radardata rdd = new Radardata();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    Vector3d victim = new Vector3d();
                    if(((actor instanceof CarGeneric) || (actor instanceof TankGeneric) || (actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric) || (actor instanceof BridgeSegment)) && !(actor instanceof StationaryGeneric) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy() && actor.getSpeed(victim) > 5D) //basically tell that target is not your own aircraft and not friendly aircraft
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(pointAC);
                        Point3d pointOrtho = new Point3d();
                        pointOrtho.set(actor.pos.getAbsPoint());
                        pointOrtho.sub(pointAC);
                        orientAC.transformInv(pointOrtho);
                        float f = Mission.cur().curCloudsType();
                        double v = ((x + ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F)) / ScX) / (30D / ((Tuple3d) (pointOrtho)).x);
                        if(right)
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < v + 6000D && ((Tuple3d) (pointOrtho)).y > v - 1000D && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.46397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.46397023426))
                            {
                                rdd.p3d = pointOrtho;
                                rdd.act = actor;
                                radarGround.add(rdd);
                                tw = Time.current();
                            }
                        if(left)
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < v + 1000D && ((Tuple3d) (pointOrtho)).y > v - 6000D && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.46397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.46397023426))
                            {
                                rdd.p3d = pointOrtho;
                                rdd.act = actor;
                                radarGround.add(rdd);
                                tw = Time.current();
                            }
                    }
                }
                int i1 = radarGround.size();
                if(i1 > 0)
                {
                    int nt = 0;
                    int k = 0;

                    for(int j = 0; j < i1; j++)
                    {
                        Tuple3d tupgr = (Tuple3d) ((Radardata) (radarGround.get(j))).p3d;
                        double x = tupgr.x;
                        if(x > (double)RClose && nt <= nTgts)
                        {
                            FOV = 30D / x; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                            double NewX = -tupgr.y * FOV; // spanning
                            double NewY = tupgr.x; //distance
                            float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F); //FOrigX currently do nothing
                            float f1 = FOrigY + (float)(NewY * ScY);
                            if(f1 < 0)
                                f1 = 0;
                            nt++; // number of marks, from 0 -> 10
                            String m = "Z_Z_RadarMark" + nt;
                            super.mesh.setCurChunk(m);
                            resetYPRmodifier();
                            Cockpit.xyz[1] = -f * 1.3333F;
                            Cockpit.xyz[2] = f1 * 1.3333F;
                            super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                            super.mesh.render();
                            if(f >= 0.18F || f <= -0.18F || f1 > 0.36F)
                                super.mesh.chunkVisible(m, false);
                            else if(!super.mesh.isChunkVisible(m))
                                super.mesh.chunkVisible(m, true);

                        }
                    }
                    for(int j = nt + 1; j <= nTgts; j++)
                    {
                        String m = "Z_Z_RadarMark" + j;
                        if(super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, false);
                    }
                }
                else
                {    // hide everything when there's no enemy
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    for(int j = 0; j <= nTgts + 1; j++)
                    {
                        String m = "Z_Z_RadarMark" + j;
                        if(super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, false);
                    }

                }
            }

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radaracquireground(float r)  //TODO scan acquire
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            radarLock.clear();
            victim.clear();
            double v = -((((F_18) aircraft()).radarvrt + ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F)) / ScX);
            double h = ((((F_18) aircraft()).radarhol * ((F_18) aircraft()).radarrange) / (ScY));
            float mach = 0F;
            float alt = 0F;
            float dif = 0F;
            float HDG = 0F;
            float brg = 0F;
            float test = 0F;
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {
                Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    if(((actor instanceof CarGeneric) || (actor instanceof TankGeneric) || (actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric) || (actor instanceof BridgeSegment)) && !(actor instanceof StationaryGeneric) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(pointAC);
                        Point3d pointOrtho = new Point3d();
                        pointOrtho.set(actor.pos.getAbsPoint());
                        Point3d victim = new Point3d();
                        victim.set(actor.pos.getAbsPoint());
                        pointOrtho.sub(pointAC);
                        orientAC.transformInv(pointOrtho);
                        //HUD.log(AircraftHotKeys.hudLogWeaponId, "target heading" + HDG);
                        float f = Mission.cur().curCloudsType();
                        if(((Tuple3d) (pointOrtho)).x > h - 500D && ((Tuple3d) (pointOrtho)).x < h + 500D && ((Tuple3d) (pointOrtho)).y < v / (30D / ((Tuple3d) (pointOrtho)).x) + 500D && ((Tuple3d) (pointOrtho)).y > v / (30D / ((Tuple3d) (pointOrtho)).x) - 500D)
                        {
                            radarLock.add(pointOrtho);
                            ((F_18) aircraft()).setGroundRadarLockedActor(actor);
                        }
                    }
                }
                int i1 = radarLock.size();
                if(i1 > 0)
                {
                    int nt = 0;
                    int k = 0;
                    double x1 = ((Tuple3d) ((Point3d)radarLock.get(k))).x;
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                        FOV = 30D / x1;
                        double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F);
                        float f1 = FOrigY + (float)(NewY * ScY);
                        ((F_18) aircraft()).radarvrt = f;
                        ((F_18) aircraft()).radarhol = f1 / (((F_18) aircraft()).radarrange);
                        if(f1 < 0)
                            f1 = 0;
                        String m = "Z_Z_lockgate";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f * 1.3333F;
                        Cockpit.xyz[2] = f1 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f >= 0.18F || f <= -0.18F || f1 > 0.36F)
                            super.mesh.chunkVisible(m, false);
                        else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);

                    }
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                        FOV = 30D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                        double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F); //FOrigX currently do nothing
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 < 0)
                            f1 = 0;
                        String m = "Z_Z_RadarMark0";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f * 1.3333F;
                        Cockpit.xyz[2] = f1 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f >= 0.18F || f <= -0.18F || f1 > 0.36F)
                            super.mesh.chunkVisible(m, false);
                        else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                            super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                        for(int j = 1; j <= nTgts + 1; j++)
                        {
                            String n = "Z_Z_RadarMark" + j;
                            if(super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, false);
                        }
                    }
                }
                else
                {    // hide everything when there's no enemy
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    if(super.mesh.isChunkVisible("Z_Z_lockgate"))
                        super.mesh.chunkVisible("Z_Z_lockgate", false);
                    ((F_18) aircraft()).lockmode = 0;
                    ((F_18) aircraft()).setGroundRadarLockedActor(null);
                }
            }

        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    protected void moveControls(float f)
    {
        super.mesh.chunkSetAngles("Canopy", 0.0F, 0.0F, 20F * fm.CT.getCockpitDoor());
        super.mesh.chunkSetAngles("Z_Z_Stick", 0.0F, fm.CT.AileronControl * 10F, fm.CT.ElevatorControl * 10F);
        super.mesh.chunkSetAngles("Z_Z_Throttle1", 0.0F, 0.0F, -42F * interp(setNew.throttler, setOld.throttler, f));
        super.mesh.chunkSetAngles("Z_Z_Throttle2", 0.0F, 0.0F, -42F * interp(setNew.throttlel, setOld.throttlel, f));
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = 0.0F;
        Cockpit.xyz[2] = fm.CT.getRudder() * -0.07F;
        super.mesh.chunkSetLocate("PedalL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = fm.CT.getRudder() * 0.07F;
        super.mesh.chunkSetLocate("PedalR", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        super.mesh.chunkSetAngles("Z_Z_Gear", 0.0F, 0.0F, cvt(fm.CT.GearControl,0.0F,1.0F,0.0F,20F));
        float f1 = 0F;
        if(fm.CT.FlapsControlSwitch == 0)
        {
            f1 = -15F;
            super.mesh.chunkSetAngles("Z_Z_Flap", 0.0F, 0.0F, f1);
        }
        if(fm.CT.FlapsControlSwitch == 1)
        {
            f1 = 0F;
            super.mesh.chunkSetAngles("Z_Z_Flap", f1, 0.0F, 0.0F);
        }
        if(fm.CT.FlapsControlSwitch == 2)
        {
            f1 = 15F;
            super.mesh.chunkSetAngles("Z_Z_Flap", 0.0F, 0.0F, f1);
        }
        super.mesh.chunkSetAngles("Z_Z_Hook", 0.0F, 0.0F, (int)fm.CT.arrestorControl * 20F);
        super.mesh.chunkSetAngles("Z_Z_WingFold",(int)fm.CT.wingControl * -80F, 0.0F, 0.0F);
    }

    protected void backupGauges(float f)  //TODO Gauges
    {
        float f1 = fm.getAltitude() * 3.28084F;
        float f2 = cvt(f1, 0.0F, 50000F, 0.0F, 1800F);
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt", 0.0F, f2, 0.0F);
        f1 = (fm.getAltitude() * 3.28084F) / 10000F;
        f2 = (float)(int)f1 * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt_1", 0.0F, 0.0F, f2);
        f2 = (float)((int)(f1 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt_2", 0.0F, 0.0F, f2);
        f2 = (float)((int)(f1 * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt_3", 0.0F, 0.0F, f2);
        float f4 = 0.0F;
        float f6 = Pitot.Indicator((float)((Tuple3d) (fm.Loc)).z, fm.getSpeedKMH());
        f6 *= 0.53996F;
        if(f6 <= 50F)
            f4 = (f6 / 50F) * 15F;
        else if(f6 <= 100F)
            f4 = (f6 - 50F) + 15F;
        else if(f6 <= 150F)
            f4 = (f6 - 100F) * 1.2F + 65F;
        else if(f6 <= 200F)
            f4 = (f6 - 150F) * 0.9F + 125F;
        else if(f6 <= 300F)
            f4 = (f6 - 200F) * 0.6F + 170F;
        else if(f6 <= 400F)
            f4 = (f6 - 300F) * 0.45F + 230F;
        else if(f6 <= 700F)
            f4 = (f6 - 400F) * 0.3F + 275F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Speed", 0.0F, f4, 0.0F);
        float f7 = setNew.vspeed2 * 3.48F;
        f7 *= 60F;
        float f8 = Math.abs(f7);
        boolean flag = f7 < 0.0F;
        if(f8 <= 1000F)
            f2 = f8 * 0.07F;
        else if(f8 <= 2000F)
            f2 = (f8 - 1000F) * 0.035F + 70F;
        else if(f8 <= 4000F)
            f2 = (f8 - 2000F) * 0.0175F + 105F;
        else if(f8 <= 6000F)
            f2 = (f8 - 4000F) * 0.00875F + 140F;
        else
            f2 = 157.5F;
        if(flag)
            f2 = -f2;
        super.mesh.chunkSetAngles("Z_Z_Ins_VSpeed", 0.0F, f2, 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_AH", 0.0F, setNew.bank, setNew.pitch);
        float ilsloctmp = setNew.ilsLoc * setNew.ilsLoc * ((setNew.ilsLoc < 0)? -1F : 1F);
        Cockpit.xyz[0] = -cvt(ilsloctmp, -10000F, 10000F, -0.020F, 0.020F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(fm.Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("Z_Z_Ins_Bank", Cockpit.xyz, Cockpit.ypr);
        float ilsgstmp = setNew.ilsGS * setNew.ilsGS * ((setNew.ilsGS < 0)? -1F : 1F);
        Cockpit.xyz[1] = -cvt(ilsgstmp, -0.25F, 0.25F, -0.020F, 0.020F);
        Cockpit.xyz[0] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(fm.Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("Z_Z_Ins_Climb", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkVisible("Z_Z_Ins_Bank", bHSIDL || bHSIILS || bHSIMAN || bHSINAV || bHSITAC || bHSITGT || bHSIUHF);
        mesh.chunkVisible("Z_Z_Ins_Climb", bHSIILS);
        f2 = (fm.M.fuel / 10000F) * 2.20462262F;
        float f3 = (float)(int)f2 * 36F;
        super.mesh.chunkVisible("Z_Z_Ins_Fuel_1", !(f3 == 0F));
        super.mesh.chunkSetAngles("Z_Z_Ins_Fuel_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_Ins_Fuel_2", !(f4 == 0F && f3 == 0F));
        super.mesh.chunkSetAngles("Z_Z_Ins_Fuel_2", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_Ins_Fuel_3", !(f5 == 0F && f4 == 0F && f3 == 0F));
        super.mesh.chunkSetAngles("Z_Z_Ins_Fuel_3", 0.0F, 0.0F, f5);
        float bingo = ((F_18) aircraft()).Bingofuel / 1000F;
        f3 = (float)(int)bingo * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Bingo_1", 0.0F, 0.0F, f3);
        float fuelinteltempL = fm.EI.engines[0].tWaterOut;
        float fuelinteltempR = fm.EI.engines[1].tWaterOut;
        if(fuelinteltempL < fuelinteltempR)  fuelinteltempL = fuelinteltempR;
        f3 = (float)((int)(fuelinteltempL / 1000F * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Temp_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(fuelinteltempL / 1000F * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Temp_2", 0.0F, 0.0F, f4);
        float N1L = cvt(fm.EI.engines[0].getRPM(), 0.0F, 4080F, 0F, 100F) / 10F;
        float N1R = cvt(fm.EI.engines[1].getRPM(), 0.0F, 4080F, 0F, 100F) / 10F;
        if(N1L < N1R)  N1L = N1R;
        if(N1L > 9.9F)  N1L = 9.9F;
        f3 = (float)((int)(N1L) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_RPM_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(N1L * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_RPM_2", 0.0F, 0.0F, f4);
        float FFL = fm.EI.engines[0].tmpF;
        float FFR = fm.EI.engines[1].tmpF;
        if(FFL < FFR)  FFL = FFR;
        f3 = (float)((int)(FFL * 10F * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_FF_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(FFL * 10F * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_FF_2", 0.0F, 0.0F, f4);
        float extempL = cvt(fm.EI.engines[0].tOilOut, 0F, 120F, 0F, 80F);
        float extempR = cvt(fm.EI.engines[1].tOilOut, 0F, 120F, 0F, 80F);
        super.mesh.chunkSetAngles("Z_Z_Ins_EGT_1", -extempL, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_EGT_2", extempR, 0.0F, 0.0F);
        float oilpressL = 1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness();
        float oilpressR = 1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness();
        if(oilpressL < oilpressR)  oilpressL = oilpressR;
        f3 = (float)((int)(oilpressL / 10F)) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Oil_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(oilpressL / 10F * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Oil_2", 0.0F, 0.0F, f4);
        super.mesh.chunkSetAngles("Z_Z_Ins_H", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_M", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_S", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        f2 = normalizeDegree(setNew.azimuth.getDeg(f) + 90F);
        super.mesh.chunkSetAngles("Z_Z_Ins_Compass", f2, 0.0F, 0.0F);
    }

    protected void movescreenfuel()  //TODO fuel
    {
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.06133F;
        Cockpit.xyz[1] = -0.00827F;
        Cockpit.xyz[2] = 0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM21_1", Cockpit.xyz, Cockpit.ypr); //TOTAL 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04933F;
        Cockpit.xyz[1] = -0.00827F;
        Cockpit.xyz[2] = 0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM31_1", Cockpit.xyz, Cockpit.ypr); //TOTAL 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.05600F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.00933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM32_1", Cockpit.xyz, Cockpit.ypr); //L WING
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04133F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.00933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM33_1", Cockpit.xyz, Cockpit.ypr); //R WING
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.00933F;
        Cockpit.xyz[1] = -0.00866F;
        Cockpit.xyz[2] = -0.01466F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM22_1", Cockpit.xyz, Cockpit.ypr); //TANK 4 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.00133F;
        Cockpit.xyz[1] = -0.00866F;
        Cockpit.xyz[2] = -0.01466F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM23_1", Cockpit.xyz, Cockpit.ypr); //TANK 4 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.00666F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM34_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL CENTER 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.009333F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM212_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL CENTER 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.0280F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM35_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL RIGHT 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.0440F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM37_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL RIGHT 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.042665F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.0520F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM36_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL LEFT 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.026666F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.0520F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM38_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL LEFT 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.009333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.009333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM24_1", Cockpit.xyz, Cockpit.ypr); //FEED R 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.0026666F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.009333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM25_1", Cockpit.xyz, Cockpit.ypr); //FEED R 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.009333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0280F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM26_1", Cockpit.xyz, Cockpit.ypr); //FEED L 1
        Cockpit.xyz[0] = -0.00266F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0280F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM27_1", Cockpit.xyz, Cockpit.ypr); //FEED L 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.049332F;
        Cockpit.xyz[1] = -0.008266F;
        Cockpit.xyz[2] = 0.0520F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM28_1", Cockpit.xyz, Cockpit.ypr); //BINGO 1
        Cockpit.xyz[0] = -0.05866F;
        Cockpit.xyz[1] = -0.008266F;
        Cockpit.xyz[2] = 0.0520F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM29_1", Cockpit.xyz, Cockpit.ypr); //BINGO 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.009333F;
        Cockpit.xyz[1] = -0.008266F;
        Cockpit.xyz[2] = 0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM210_1", Cockpit.xyz, Cockpit.ypr); //TANK 1 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.002666F;
        Cockpit.xyz[1] = -0.008266F;
        Cockpit.xyz[2] = 0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM211_1", Cockpit.xyz, Cockpit.ypr); //TANK 1 2
        float f3 = 0F;
        float totalfuelInt = (fm.M.fuel / 1000F) * 2.20462262F;  // 1000 lbs unit
        float tankW = 0F;  // Tank Wing (one side)
        if(bF18D)
        {
            if(totalfuelInt > 8.960F)
                tankW = (totalfuelInt - 8.960F) / 2F;
        }
        else
        {
            if(totalfuelInt > 9.650F)
                tankW = (totalfuelInt - 9.650F) / 2F;
        }
        float f4 = (float)((int)(tankW * 10F) % 10) * 36F;
        if(f4 == 0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", false);
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", false);
        }
        else
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", true);
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_2", 0.0F, 0.0F, f4);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_2", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(tankW * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", false);
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", false);
        }
        else
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", true);
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_3", 0.0F, 0.0F, f5);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_3", 0.0F, 0.0F, f5);
        float f6 = (float)((int)(tankW * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM32_4", true);
        super.mesh.chunkVisible("Z_Z_HDD_NUM33_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_4", 0.0F, 0.0F, f6);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_4", 0.0F, 0.0F, f6);
        float tank1 = 0F;  // tank1, forward fuselage
        float tank4 = 0F;  // tank4, aft fuselage
        if(bF18D)
        {
            if (totalfuelInt > 8.960F)
            {
                tank1 = 2.150F;
                tank4 = 3.620F;
            }
            else if(totalfuelInt > 7.490F)
            {
                tank1 = 2.150F;
                tank4 = 3.620F - (totalfuelInt - 7.490F);
            }
            else if(totalfuelInt > 3.190F)
            {
                tank1 = tank4 = (totalfuelInt - 3.190F) / 2F;
            }
        }
        else
        {
            if (totalfuelInt > 9.650F)
            {
                tank1 = 2.840F;
                tank4 = 3.620F;
            }
            else if(totalfuelInt > 8.180F)
            {
                tank1 = 2.840F;
                tank4 = 3.620F - (totalfuelInt - 8.180F);
            }
            else if(totalfuelInt > 3.190F)
            {
                tank1 = tank4 = (totalfuelInt - 3.190F) / 2F;
            }
        }
        f3 = (float)(int)tank4 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tank4 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tank4 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tank4 * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_3", 0.0F, 0.0F, f6);
        f3 = (float)(int)tank1 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tank1 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tank1 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tank1 * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_3", 0.0F, 0.0F, f6);
        float tankExC = 0F;    // Center external Droptank
        if(((F_18) aircraft()).bHasCenterTank && !fm.M.bFuelTanksDropped)
            tankExC = (((F_18) aircraft()).checkfuel(0) / 1000F) * 2.20462262F;
        f3 = (float)(int)tankExC * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tankExC * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tankExC * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_4", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tankExC * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_2", 0.0F, 0.0F, f6);
        super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", false);
        float tankExL = 0F;  // Wing external Droptank (one side)
        if(((F_18) aircraft()).bHasWingTank && !fm.M.bFuelTanksDropped)
        {
            if(((F_18) aircraft()).bHasCenterTank)
                tankExL = (((F_18) aircraft()).checkfuel(1) / 1000F) * 2.20462262F;
            else
                tankExL = (((F_18) aircraft()).checkfuel(0) / 1000F) * 2.20462262F;
        }
        f3 = (float)(int)tankExL * 36F;
        if(f3 == 0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", false);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", false);
        }    else
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", true);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_2", 0.0F, 0.0F, f3);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tankExL * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", false);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", false);
        }    else
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", true);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_3", 0.0F, 0.0F, f4);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tankExL* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_4", false);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_4", false);
        }    else
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_4", true);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_4", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_4", 0.0F, 0.0F, f5);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_4", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tankExL * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", true);
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_2", 0.0F, 0.0F, f6);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_2", 0.0F, 0.0F, f6);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", false);
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", false);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_4", false);
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", false);
        float feedL = 1.790F;  // Feed tank Left (Tank2)
        if(totalfuelInt < 3.190F)
        {
            if(totalfuelInt > 2.800F)
                feedL = totalfuelInt - 1.400F;
            else
                feedL = totalfuelInt / 2F;
        }
        if(feedL < 0F)
            feedL = 0F;
        f3 = (float)(int)feedL * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(feedL * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(feedL * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(feedL * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_3", 0.0F, 0.0F, f6);
        float feedR = 1.400F;
        if(totalfuelInt < 2.800F)
            feedR = totalfuelInt / 2F;
        if(feedR < 0F)
            feedR = 0F;
        f3 = (float)(int)feedR * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(feedR * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(feedR * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(feedR * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_3", 0.0F, 0.0F, f6);
        totalfuel = ((fm.M.fuel / 1000F) * 2.20462262F + tankExL * 2F + tankExC) / 10F;
        timefuel = Time.current();
        f3 = (float)(int)totalfuel * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(totalfuel * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(totalfuel * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(totalfuel * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_3", 0.0F, 0.0F, f6);
        float f7 = (float)((int)(totalfuel * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_4", 0.0F, 0.0F, f7);

        float bingo = ((F_18) aircraft()).Bingofuel / 1000F;
        f3 = (float)(int)bingo * 36F;
        if(f3 == 0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", false);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(bingo * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM28_3", false);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(bingo* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", false);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(bingo * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_3", 0.0F, 0.0F, f6);
    }

    protected void movescreenfuelflow()  //TODO fuel flow
    {
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.009333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0400F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM21_1", Cockpit.xyz, Cockpit.ypr); //BINGO RANGE 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.001333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0400F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM31_1", Cockpit.xyz, Cockpit.ypr); //BINGO RANGE 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.03333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0400F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM22_1", Cockpit.xyz, Cockpit.ypr); //BINGO DURATION 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.0440F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0400F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM23_1", Cockpit.xyz, Cockpit.ypr); //BINGO DURATION 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.00933F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM24_1", Cockpit.xyz, Cockpit.ypr); //BEST M RANGE 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.001333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM32_1", Cockpit.xyz, Cockpit.ypr); //BEST M RANGE 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.03333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM25_1", Cockpit.xyz, Cockpit.ypr); //BEST M ENDURANCE 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04400F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM26_1", Cockpit.xyz, Cockpit.ypr); //BEST M ENDURANCE 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.03333F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.005333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM27_1", Cockpit.xyz, Cockpit.ypr); //TIME 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.022666F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.005333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM28_1", Cockpit.xyz, Cockpit.ypr); //TIME 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.001333F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.005333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM29_1", Cockpit.xyz, Cockpit.ypr); //FUEL REMAIN 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.009333F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.005333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM33_1", Cockpit.xyz, Cockpit.ypr); //FUEL REMAIN 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.017333F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.0200F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM210_1", Cockpit.xyz, Cockpit.ypr); //BASIC 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.006666F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.0200F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM34_1", Cockpit.xyz, Cockpit.ypr); //BASIC 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.017333F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.02933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM211_1", Cockpit.xyz, Cockpit.ypr); //FUEL 1
        Cockpit.xyz[0] = 0.006666F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.02933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM35_1", Cockpit.xyz, Cockpit.ypr); //FUEL 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.01733F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.03600F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM212_1", Cockpit.xyz, Cockpit.ypr); //STORE 1
        Cockpit.xyz[0] = 0.00666F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.03600F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM36_1", Cockpit.xyz, Cockpit.ypr); //STORE 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.01733F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.04933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM37_1", Cockpit.xyz, Cockpit.ypr); //TOTAL 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.00666F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.04933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM38_1", Cockpit.xyz, Cockpit.ypr); //TOTAL 2
        float tankExL = 0F;
        if(((F_18) aircraft()).bHasWingTank && !fm.M.bFuelTanksDropped)
        {
            if(((F_18) aircraft()).bHasCenterTank)
                tankExL = (((F_18) aircraft()).checkfuel(1) / 1000F) * 2.20462262F;
            else
                tankExL = (((F_18) aircraft()).checkfuel(0) / 1000F) * 2.20462262F;
        }
        float tankExC = 0F;
        if(((F_18) aircraft()).bHasCenterTank && !fm.M.bFuelTanksDropped)
            tankExC = (((F_18) aircraft()).checkfuel(0) / 1000F) * 2.20462262F;
        float Sumfuel = (fm.M.fuel / 1000F * 2.20462262F + tankExL * 2F + tankExC) / 10F; //FUEL REMAIN
        float Sfuel = Sumfuel * 10000F / 2.20462262F;
        float flowrate = (totalfuel * 10000F / 2.20462262F - Sfuel) / ((Time.current() - timefuel) / 1000L);
        float distance = becondistance / 1000F;
        float Duration = (Sfuel / flowrate) / 36000F;
        float Currange = Duration * 10F * fm.getSpeedKMH();
        float rangeremain = Currange - distance;
        if(rangeremain < 0)
            rangeremain = 0;
        Duration = rangeremain / (fm.getSpeedKMH() * 10F);
        Sumfuel = Duration * flowrate * 36000F;
        float f2 = Sumfuel / 10000F * 2.20462262F;
        float f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_2", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_3", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_2", 0.0F, 0.0F, f5);
        float f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_3", 0.0F, 0.0F, f6);
        float f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_4", 0.0F, 0.0F, f7);
        float clock = (distance / fm.getSpeedKMH()); //Clock
        //HUD.log(AircraftHotKeys.hudLogWeaponId, "ETA " + clock + "range " + Currange + "distance " + distance);
        if(clock < 0F)
            clock = 0F;
        f3 = (float)((int)clock % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(clock * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_3", 0.0F, 0.0F, f4);
        f4 = (float)((int)(clock * 100F) % 10) * 0.6F;
        f5 = (float)((int)f4 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_2", 0.0F, 0.0F, f5);
        f5 = (float)((int)(clock * 1000F) % 10) * 0.6F;
        f6 = (float)((int)f5 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM28_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_3", 0.0F, 0.0F, f6);
        float bingo = ((F_18) aircraft()).Bingofuel / 2.20462262F;
        Duration = ((Sfuel - bingo) / flowrate) / 36000F;
        if(Duration < 0F)
            Duration = 0F;
        f3 = (float)((int)Duration % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(Duration * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_3", 0.0F, 0.0F, f4);
        f4 = (float)((int)(Duration * 100F) % 10) * 0.6F;
        f5 = (float)((int)f4 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_2", 0.0F, 0.0F, f5);
        f5 = (float)((int)(Duration * 1000F) % 10) * 0.6F;
        f6 = (float)(int)f5 * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_3", 0.0F, 0.0F, f6);
        Currange = Duration * 10F * fm.getSpeedKMH() * 0.539956803455F; //Bingo Range
        f2 = Currange / 10000F;
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_4", 0.0F, 0.0F, f7);
        float duspeed = 700F * cvt(fm.getAltitude(), 0F, 10000F, 1.0F, 1.3F);
        float thrustCo = cvt(fm.getAltitude(), 0F, 10000F, 1.0F, 0.7F);
        float duthrust = cvt(duspeed, 600F, 800F, 0.35F, 0.50F) * thrustCo;
        float fuelflowvalue = cvt(duthrust, 0.0F , 1.00F, 0.09F, 1.20F);
        //HUD.log(AircraftHotKeys.hudLogWeaponId," " + fuelflowvalue);
        Duration = (Sfuel - bingo) / fuelflowvalue / 36000;  //best mach duration
        f3 = (float)((int)Duration % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(Duration * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_3", 0.0F, 0.0F, f4);
        f4 = (float)((int)(Duration * 100F) % 10) * 0.6F;
        f5 = (float)((int)f4 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_2", 0.0F, 0.0F, f5);
        f5 = (float)((int)(Duration * 1000F) % 10) * 0.6F;
        f6 = (float)((int)f5 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_3", 0.0F, 0.0F, f6);
        Currange = Duration * 10F * duspeed * 0.539956803455F;  //best mach range
        f2 = Currange / 10000F;
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_4", 0.0F, 0.0F, f7);

        float basic = fm.M.massEmpty;
        f2 = basic * 2.20462262F / 10000F;  //basic weight
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_4", 0.0F, 0.0F, f7);
        float fuel = fm.M.fuel;  //fuel
        f2 = fuel * 2.20462262F / 10000F;
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_4", 0.0F, 0.0F, f7);
        float store = fm.M.mass - basic - fuel;  //store weight
        f2 = store * 2.20462262F / 10000F;
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_4", 0.0F, 0.0F, f7);
        float total = fm.M.mass;  //total weight
        f2 = total * 2.20462262F / 10000F;
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_3", 0.0F, 0.0F, f4);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_4", false);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_4", 0.0F, 0.0F, f7);
    }

    protected void movescreenengines()  //TODO Engine
    {
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03999F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM21_1", Cockpit.xyz, Cockpit.ypr); //Inlet Temp 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03999F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM22_1", Cockpit.xyz, Cockpit.ypr); //Inlet Temp 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM23_1", Cockpit.xyz, Cockpit.ypr); //N1 RPM 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM24_1", Cockpit.xyz, Cockpit.ypr); //N1 RPM 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.026666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM25_1", Cockpit.xyz, Cockpit.ypr); //N2 RPM 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.026666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM26_1", Cockpit.xyz, Cockpit.ypr); //N2 RPM 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.020F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM31_1", Cockpit.xyz, Cockpit.ypr); //EGT 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.020F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM32_1", Cockpit.xyz, Cockpit.ypr); //EGT 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.013333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM33_1", Cockpit.xyz, Cockpit.ypr); //FF 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.013333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM34_1", Cockpit.xyz, Cockpit.ypr); //FF 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM27_1", Cockpit.xyz, Cockpit.ypr); //NOZ POS 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM28_1", Cockpit.xyz, Cockpit.ypr); //NOZ POS 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM29_1", Cockpit.xyz, Cockpit.ypr); //OIL PRES 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM210_1", Cockpit.xyz, Cockpit.ypr); //OIL PRES 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.05066F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM211_1", Cockpit.xyz, Cockpit.ypr); //THRUST 1
        Cockpit.xyz[0] = 0.03999F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM35_1", Cockpit.xyz, Cockpit.ypr); //THRUST 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.03999F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM212_1", Cockpit.xyz, Cockpit.ypr); //THRUST2 1
        Cockpit.xyz[0] = -0.05066F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM36_1", Cockpit.xyz, Cockpit.ypr); //THRUST2 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.00880F;
        Cockpit.xyz[2] = -0.010666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM37_1", Cockpit.xyz, Cockpit.ypr); //FAN VIB 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.00880F;
        Cockpit.xyz[2] = -0.010666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM38_1", Cockpit.xyz, Cockpit.ypr); //FAN VIB 2
        float inlettempL = Atmosphere.temperature(fm.getAltitude());
        float inlettempR = inlettempL;
        float f2 = inlettempL / 10F;
        float f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_2", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_3", 0.0F, 0.0F, f4);
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_3", 0.0F, 0.0F, f4);
        float fuelinteltempL = fm.EI.engines[0].tWaterOut;
        float fuelinteltempR = fm.EI.engines[1].tWaterOut;
        float N1L = cvt(fm.EI.engines[0].getRPM(), 0.0F, 4080F, 0F, 100F);
        f2 = N1L / 10F;
        if(f2 > 9.9F)
            f2 = 9.9F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_3", 0.0F, 0.0F, f4);
        float N1R = cvt(fm.EI.engines[1].getRPM(), 0.0F, 4080F, 0F, 100F);
        f2 = N1R / 10F;
        if(f2 > 9.9F)
            f2 = 9.9F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_3", 0.0F, 0.0F, f4);
        float N2L = cvt(fm.EI.engines[0].getRPM() * 0.85294117647058823529411764705882F, 0.0F, 3480F, 0F, 100F);
        f2 = N2L / 10F;
        if(f2 > 9.9F)
            f2 = 9.9F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_3", 0.0F, 0.0F, f4);
        float N2R = cvt(fm.EI.engines[1].getRPM() * 0.85294117647058823529411764705882F, 0.0F, 3480F, 0F, 100F);
        f2 = N2R / 10F;
        if(f2 > 9.9F)
            f2 = 9.9F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_3", 0.0F, 0.0F, f4);
        float extempL = cvt(fm.EI.engines[0].tOilOut, 0F, 120F, 10F, 838F);
        f2 = extempL / 100F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_3", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F && f5 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_4", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_4", 0.0F, 0.0F, f5);
        float extempR = cvt(fm.EI.engines[1].tOilOut, 0F, 120F, 10F, 838F);
        f2 = extempR / 100F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F && f5 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_4", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_4", 0.0F, 0.0F, f5);
        float FFL = fm.EI.engines[0].tmpF;
        f2 = FFL * 100F;
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F && f5 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_4", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_4", 0.0F, 0.0F, f5);
        float FFR = fm.EI.engines[1].tmpF;
        f2 = FFR * 100F;
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F && f5 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_4", 0.0F, 0.0F, f5);
        f2 = -((F_18) aircraft()).fNozzleOpenL * 1.1F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_3", 0.0F, 0.0F, f4);
        f2 = -((F_18) aircraft()).fNozzleOpenR * 1.1F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM28_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_3", 0.0F, 0.0F, f4);
        float oilpressL = 1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness();
        f2 = oilpressL;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_3", 0.0F, 0.0F, f4);
        float oilpressR = 1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness();
        f2 = oilpressR;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_3", 0.0F, 0.0F, f4);
        float thrustL = (fm.EI.engines[0].getPowerOutput() < 1.0F ? cvt(fm.EI.engines[0].getPowerOutput(),0F,1.0F,0F,4800F) : cvt(fm.EI.engines[0].getPowerOutput(),1.00F,1.10F,4800F,7950F));
        f2 = thrustL / 10000F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_2", 0.0F, 0.0F, f5);
        float f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_3", 0.0F, 0.0F, f6);
        float f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM35_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_4", 0.0F, 0.0F, f7);
        float thrustR = (fm.EI.engines[1].getPowerOutput() < 1.00F ? cvt(fm.EI.engines[1].getPowerOutput(),0F,1.0F,0F,4800F) : cvt(fm.EI.engines[1].getPowerOutput(),1.00F,1.10F,4800F,7950F));
        f2 = thrustR / 10000F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM36_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_4", 0.0F, 0.0F, f7);
        float shake = (fm.EI.engines[0].w / fm.EI.engines[0].wMax) * fm.EI.engines[0].thrustMax * (float)Math.sqrt(fm.getSpeed() / 94F);
        float enginevibL = cvt(shake, 0.0F, fm.EI.engines[0].thrustMax, 0.05F, 0.21F);
        f2 = enginevibL * 10F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_3", 0.0F, 0.0F, f4);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_4", false);
        shake = (fm.EI.engines[1].w / fm.EI.engines[1].wMax) * fm.EI.engines[1].thrustMax * (float)Math.sqrt(fm.getSpeed() / 94F);
        float enginevibR = cvt(shake, 0.0F, fm.EI.engines[1].thrustMax, 0.05F, 0.21F);
        f2 = enginevibR * 10F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_3", 0.0F, 0.0F, f4);
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", false);
        float CPRL = fm.EI.engines[0].getManifoldPressure();
        float CPRR = fm.EI.engines[1].getManifoldPressure();
        float TDPL = fm.EI.engines[0].getControlCompressor();
        float TDPR = fm.EI.engines[1].getControlCompressor();
    }

    protected void Navscreen(float A)
    {
        float f = -normalizeDegree(setNew.azimuth.getDeg(A) + 90F);
        super.mesh.chunkSetAngles("HDD_Nav_Comp", 0.0F, f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_N", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_W", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_E", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_S", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_3", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_6", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_12", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_15", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_21", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_24", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_30", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_33", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_Pointer", 0.0F, setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F);
        resetYPRmodifier();
        float hsiloctmp = setNew.hsiLoc * setNew.hsiLoc * ((setNew.hsiLoc < 0)? -1F : 1F);
        if(bHSIILS)
            Cockpit.xyz[0] = cvt(hsiloctmp, -20000F, 20000F, 0.026666F, -0.026666F);
        else
            Cockpit.xyz[0] = 0.0F;
        super.mesh.chunkSetLocate("HDD_Nav_DIV", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkVisible("HDD_Nav_DIV", bHSIILS || bHSIMAN || bHSINAV);

        float beaconDistanceInMeter = getBeaconDistance();
        if(!useRealisticNavigationInstruments())
        {
            WayPoint waypoint = fm.AP.way.curr();
            if(waypoint != null)
            {
                Point3d P1 = new Point3d();
                Vector3d V = new Vector3d();
                waypoint.getP(P1);
                V.sub(P1, fm.Loc);
                beaconDistanceInMeter = (float)Math.sqrt(V.x * V.x + V.y * V.y);
            }
        }
        becondistance = beaconDistanceInMeter;
        float f2 = (beaconDistanceInMeter / 1852F) / 1000F;
        float f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
            super.mesh.chunkVisible("Z_Z_Nav_SDT_1", false);
        else
            super.mesh.chunkVisible("Z_Z_Nav_SDT_1", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_1", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_Nav_SDT_2", false);
        else
            super.mesh.chunkVisible("Z_Z_Nav_SDT_2", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_2", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
            super.mesh.chunkVisible("Z_Z_Nav_SDT_3", false);
        else
            super.mesh.chunkVisible("Z_Z_Nav_SDT_3", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_3", 0.0F, 0.0F, f5);
        float f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
            super.mesh.chunkVisible("Z_Z_Nav_SDT_4", false);
        else
            super.mesh.chunkVisible("Z_Z_Nav_SDT_4", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_4", 0.0F, 0.0F, f6);
        if(useRealisticNavigationInstruments())
        {
            super.mesh.chunkVisible("HDD_Nav_Tacan", bHSIILS || bHSITAC);
            resetYPRmodifier();
            Cockpit.xyz[2] = cvt(beaconDistanceInMeter, 1852F, 92600F, -0.0586652F, -0.013333F);
            super.mesh.chunkSetLocate("HDD_Nav_Tacan", Cockpit.xyz, Cockpit.ypr);
        }
        else
        {
            super.mesh.chunkVisible("HDD_Nav_Tacan", bHSINAV);
            resetYPRmodifier();
            Cockpit.xyz[2] = cvt(beaconDistanceInMeter, 1852F, 92600F, -0.052222F, 0.0F);
            super.mesh.chunkSetLocate("HDD_Nav_Tacan", Cockpit.xyz, Cockpit.ypr);
        }
    }

    protected void HUD(float A)  //TODO HUD
    {
        boolean flag = false;
        boolean flag1 = false;
        if(!setNew.isGeneratorAllive)
            flag1 = false;
        else
            flag1 = true;
        super.mesh.chunkVisible("Z_Z_HUD_Screen", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_PITCH", flag1);
        for(int j = 1; j < 5; j++)
            super.mesh.chunkVisible("Z_Z_HUD_Speed_" + j, flag1);
        super.mesh.chunkVisible("Z_Z_HUD_AOA_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_AOA_2", flag1);
        for(int j = 1; j < 5; j++)
            super.mesh.chunkVisible("Z_Z_HUD_VP_" + j, flag1);
        for(int j = 1; j < 6; j++)
            super.mesh.chunkVisible("Z_Z_HUD_Alt_" + j, flag1);
        super.mesh.chunkVisible("Z_Z_HUD_G_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_G_2", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_HDG", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_2", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_FPM", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Bank", flag1);
        super.mesh.chunkVisible("Z_Z_RETICLECROSS", flag1);
        if(!flag1)
        {
            return;
        }
        float f10 = calculateMach();
        float f3 = (float)(int)f10 * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_1", 0.0F, 0.0F, f3);
        f3 = (float)((int)(f10 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_2", 0.0F, 0.0F, f3);
        float f = (fm.getSpeedKMH() * 0.539956803455F) / 1000F;
        float f1 = (float)(int)f * 36F;
        if(f1 == 0)
            super.mesh.chunkVisible("Z_Z_HUD_Speed_1", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Speed_1", 0.0F, 0.0F, f1);
        float f2 = (float)((int)(f * 10F) % 10) * 36F;
        if(f2 == 0 && f1 == 0)
            super.mesh.chunkVisible("Z_Z_HUD_Speed_2", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Speed_2", 0.0F, 0.0F, f2);
        f3 = (float)((int)(f * 100F) % 10) * 36F;
        if(f3 == 0 && f2 == 0 && f1 == 0)
            super.mesh.chunkVisible("Z_Z_HUD_Speed_3", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Speed_3", 0.0F, 0.0F, f3);
        f3 = (float)((int)(f * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Speed_4", 0.0F, 0.0F, f3);
        f = (fm.getAltitude() * 3.28084F) / 10000F;
        f1 = (float)(int)f * 36F;
        if(f1 == 0)
            super.mesh.chunkVisible("Z_Z_HUD_Alt_1", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Alt_1", 0.0F, 0.0F, f1);
        f2 = (float)((int)(f * 10F) % 10) * 36F;
        if(f2 == 0 && f1 == 0)
            super.mesh.chunkVisible("Z_Z_HUD_Alt_2", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Alt_2", 0.0F, 0.0F, f2);
        f3 = (float)((int)(f * 100F) % 10) * 36F;
        if(f3 == 0 && f2 == 0 && f1 == 0)
            super.mesh.chunkVisible("Z_Z_HUD_Alt_3", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Alt_3", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(f * 1000F) % 10) * 36F;
        if(f4 == 0 && f3 == 0 && f2 == 0 && f1 == 0)
            super.mesh.chunkVisible("Z_Z_HUD_Alt_4", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Alt_4", 0.0F, 0.0F, f2);
        float f5 = (float)((int)(f * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Alt_5", 0.0F, 0.0F, f2);
        f = Math.abs(setNew.vspeed2 * 3.48F / 1000F);
        f1 = (float)(int)f * 36F;
        if(f1 == 0)
            super.mesh.chunkVisible("Z_Z_HUD_VP_1", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_VP_1", 0.0F, 0.0F, f1);
        f2 = (float)((int)(f * 10F) % 10) * 36F;
        if(f2 == 0 && f1 == 0)
            super.mesh.chunkVisible("Z_Z_HUD_VP_2", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_VP_2", 0.0F, 0.0F, f2);
        f3 = (float)((int)(f * 100F) % 10) * 36F;
        if(f3 == 0 && f2 == 0)
            super.mesh.chunkVisible("Z_Z_HUD_VP_3", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_VP_3", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_VP_4", 0.0F, 0.0F, f4);
        float f8 = fm.getOverload();
        f3 = (float)(int)Math.abs(f8) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_G_1", 0.0F, 0.0F, f3);
        f3 = (float)((int)(Math.abs(f8) * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_G_2", 0.0F, 0.0F, f3);
        float f9 = normalizeDegree(setNew.azimuth.getDeg(A) + 90F);
        super.mesh.chunkSetAngles("Z_Z_HUD_HDG", 0.0F, f9, 0.0F);
        f2 = setNew.pitch;
        if(f2 > 90F)
            for(; f2 > 90F; f2 -= 360F);
        else if(f2 < -90F)
            for(; f2 < -90F; f2 += 360F);
        f2 -= 90F;
        f2 = -f2;
        int j = (int)f2 / 5;
        for(int k = j - 3; k <= j + 2; k++)
            if(k >= 0 && k < 37)
            {
                super.mesh.chunkVisible("Z_Z_HUD_PITCH", true);
                super.mesh.chunkSetAngles("Z_Z_HUD_PITCH", 0.0F, setNew.bank, -setNew.pitch);
            }
        Cockpit.xyz[0] = 0.0F;
        boolean flag2 = false;
        if(fm.Gears.nOfGearsOnGr < 1 || f1 > 10F)
        {
            Cockpit.xyz[1] = -setNew.fpmYaw;
            if(Cockpit.xyz[1] < -6.5F)
            {
                Cockpit.xyz[1] = -6.5F;
                flag2 = true;
            }
            else if(Cockpit.xyz[1] > 6.5F)
            {
                Cockpit.xyz[1] = 6.5F;
                flag2 = true;
            }
            Cockpit.xyz[2] = -setNew.fpmPitch;
            if(Cockpit.xyz[2] < -11.5F)
            {
                Cockpit.xyz[2] = -11.5F;
                flag2 = true;
            }
            else if(Cockpit.xyz[2] > 7F)
            {
                Cockpit.xyz[2] = 7F;
                flag2 = true;
            }
        }
        else
        {
            Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        }
        super.mesh.chunkSetAngles("Z_Z_HUD_FPM", 0.0F, Cockpit.xyz[1], Cockpit.xyz[2]);
        super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", flag2);
        f3 = normalizeDegree(setNew.bank);
        if(f3 > 50F && f3 < 310F)
            super.mesh.chunkVisible("Z_Z_HUD_Bank", false);
        else
            super.mesh.chunkVisible("Z_Z_HUD_Bank", true);
        super.mesh.chunkSetAngles("Z_Z_HUD_BANK", 0.0F, f3, 0.0F);
        f10 = setNew.fpmPitch / 10F;
        f3 = (float)(int)f10 * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_AOA_1", 0.0F, 0.0F, f3);
        f3 = (float)((int)(f10 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_AOA_2", 0.0F, 0.0F, f3);
        float beaconDistanceInNM = getBeaconDistance() / 1852F;
        if(beaconDistanceInNM > 99.9F) beaconDistanceInNM = 99.9F;
        if((bHSIILS || bHSITAC) && flag1)
        {
            super.mesh.chunkVisible("Z_Z_HUD_TCN_1", beaconDistanceInNM >= 10.0F);
            super.mesh.chunkVisible("Z_Z_HUD_TCN_2", true);
            super.mesh.chunkVisible("Z_Z_HUD_TCN_3", true);
            f3 = (float)(Math.floor((double)(beaconDistanceInNM / 10F))) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_TCN_1", 0.0F, 0.0F, f3);
            f3 = (float)(Math.floor((double)(beaconDistanceInNM)) * 36F);
            super.mesh.chunkSetAngles("Z_Z_HUD_TCN_2", 0.0F, 0.0F, f3);
            f3 = (float)(Math.floor((double)(beaconDistanceInNM * 10F) % 10) * 36F);
            super.mesh.chunkSetAngles("Z_Z_HUD_TCN_3", 0.0F, 0.0F, f3);
        }
        else
        {
            super.mesh.chunkVisible("Z_Z_HUD_TCN_1", false);
            super.mesh.chunkVisible("Z_Z_HUD_TCN_2", false);
            super.mesh.chunkVisible("Z_Z_HUD_TCN_3", false);
        }
    }

    protected void drawSound(float f)  //TODO SOUND
    {
        if(Time.current() < 100L) return; // soon from mission start, some flight values are not set correctly by game system

        boolean flag = false;
        boolean flagAoA = false;
        if(aoaWarnFX4 != null)
            if((setNew.fpmPitch >= 28F || fm.getAOA() >= 25F) && fm.Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX4.isPlaying())
                    aoaWarnFX4.play();
                flag = true;
                flagAoA = true;
            }
            else
            {
                aoaWarnFX4.cancel();
            }
        if(aoaWarnFX3 != null && !flagAoA)
            if(((setNew.fpmPitch >= 20F && setNew.fpmPitch < 28F) || (fm.getAOA() >= 19F && fm.getAOA() < 25F)) && fm.Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX3.isPlaying())
                    aoaWarnFX3.play();
                flagAoA = true;
            }
            else
            {
                aoaWarnFX3.cancel();
            }
        if(aoaWarnFX2 != null && !flagAoA)
            if(((setNew.fpmPitch >= 15F && setNew.fpmPitch < 20F) || (fm.getAOA() >= 16.5F && fm.getAOA() < 19F)) && fm.Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX2.isPlaying())
                    aoaWarnFX2.play();
                flagAoA = true;
            }
            else
            {
                aoaWarnFX2.cancel();
            }
        if(aoaWarnFX != null && !flagAoA)
            if(((setNew.fpmPitch >= 11F && setNew.fpmPitch < 15F) || (fm.getAOA() >= 13F && fm.getAOA() < 16.5F)) && fm.Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX.isPlaying())
                    aoaWarnFX.play();
                flagAoA = true;
            }
            else
            {
                aoaWarnFX.cancel();
            }
        if(AltitudeWarn != null && PullupWarn != null)
        {
            float radioaltnow = fm.getAltitude() - (float)Engine.land().HQ_Air(fm.Loc.x, fm.Loc.y);
            //radioaltnow = radioaltnow / (float)Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getKren()))) / (float)Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getTangage())));
            if((radioaltnow < 550F && fm.getVertSpeed() < -40F) || (radioaltnow < 100F && fm.CT.getGear() < 0.999999F))
            {
                if((fm.Or.getPitch() - 360F) < -22F || ((fm.Or.getPitch() - 360F) < -10F && radioaltnow < 100F))
                {
                    PullupWarn.start();
                    AltitudeWarn.stop();
                    flag = true;
                }
                else
                {
                    AltitudeWarn.start();
                    PullupWarn.stop();
                    flag = true;
                }
            }
            else
            {
                AltitudeWarn.stop();
                PullupWarn.stop();
            }
        }
        if(OverGWarn != null)
        {
            if(fm.getOverload() > 11F)
            {
                OverGWarn.start();
                flag = true;
            }
            else
            {
                OverGWarn.stop();
            }
        }
        if(MinimumSpeedWarn != null)
        {
            if(fm.getSpeed() < 72F && fm.CT.getGear() < 0.999999F)
            {
                MinimumSpeedWarn.start();
                flag = true;
            }
            else
            {
                MinimumSpeedWarn.stop();
            }
        }
        if(MaximumSpeedWarn != null)
        {
            if(fm.getSpeed() > 725F)
            {
                MaximumSpeedWarn.start();
                flag = true;
            }
            else
            {
                MaximumSpeedWarn.stop();
            }
        }
        super.mesh.chunkVisible("L_MC", flag);

        if(fm.AS.astateEngineStates[0] > 2 && (lLastEngineFireLeftWarned == -1L || Time.current() - lLastEngineFireLeftWarned > 7200L) && EngineFireLeftWarn != null)
        {
            EngineFireLeftWarn.play();
            lLastEngineFireLeftWarned = Time.current();
        }
        if(fm.AS.astateEngineStates[1] > 2 && (lLastEngineFireRightWarned == -1L || Time.current() - lLastEngineFireRightWarned > 7200L) && EngineFireRightWarn != null)
        {
            EngineFireRightWarn.play();
            lLastEngineFireRightWarned = Time.current();
        }
        float ffuel = fm.M.fuel * 2.20462262F;
        if((((F_18) aircraft()).Bingofuel > ffuel && iBingoFuelWarned == 0) || (((F_18) aircraft()).Bingofuel - 100F > ffuel && iBingoFuelWarned == 1) && BingoFuelWarn != null)
        {
            BingoFuelWarn.play();
            iBingoFuelWarned++;
        }
    }

    private void stopSoundEffects()
    {
        if(aoaWarnFX4 != null && aoaWarnFX4.isPlaying())
            aoaWarnFX4.cancel();
        if(aoaWarnFX3 != null && aoaWarnFX3.isPlaying())
            aoaWarnFX3.cancel();
        if(aoaWarnFX2 != null && aoaWarnFX2.isPlaying())
            aoaWarnFX2.cancel();
        if(aoaWarnFX != null && aoaWarnFX.isPlaying())
            aoaWarnFX.cancel();
    }

    protected void Warninglight()
    {
        super.mesh.chunkVisible("L_SpBr", fm.CT.getAirBrake() > 0.1F && (setNew.isBatteryOn || setNew.isGeneratorAllive));
        super.mesh.chunkVisible("L_LFire", fm.AS.astateEngineStates[0] > 2);
        super.mesh.chunkVisible("L_RFire", fm.AS.astateEngineStates[1] > 2);

        super.mesh.chunkVisible("L_Gear", (setNew.isBatteryOn || setNew.isGeneratorAllive));
        super.mesh.chunkVisible("L_Flap", (setNew.isBatteryOn || setNew.isGeneratorAllive));

        if(fm.CT.getGearL() > 0.95F && fm.CT.getGearR() > 0.95F && fm.CT.getGearC() > 0.95F && LGearStat != 7)
        {
            super.mesh.materialReplace("WlightGear", "WlightGearA");
            LGearStat = 7;
        }
        else if(fm.CT.getGearL() < 0.95F && fm.CT.getGearR() > 0.95F && fm.CT.getGearC() > 0.95F && LGearStat != 6)
        {
            super.mesh.materialReplace("WlightGear", "WlightGearNR");
            LGearStat = 6;
        }
        else if(fm.CT.getGearL() > 0.95F && fm.CT.getGearR() < 0.95F && fm.CT.getGearC() > 0.95F && LGearStat != 5)
        {
            super.mesh.materialReplace("WlightGear", "WlightGearNL");
            LGearStat = 5;
        }
        else if(fm.CT.getGearL() < 0.95F && fm.CT.getGearR() < 0.95F && fm.CT.getGearC() > 0.95F && LGearStat != 4)
        {
            super.mesh.materialReplace("WlightGear", "WlightGearN");
            LGearStat = 4;
        }
        else if(fm.CT.getGearL() > 0.95F && fm.CT.getGearR() > 0.95F && fm.CT.getGearC() < 0.95F && LGearStat != 3)
        {
            super.mesh.materialReplace("WlightGear", "WlightGearLR");
            LGearStat = 3;
        }
        else if(fm.CT.getGearL() < 0.95F && fm.CT.getGearR() > 0.95F && fm.CT.getGearC() < 0.95F && LGearStat != 2)
        {
            super.mesh.materialReplace("WlightGear", "WlightGearR");
            LGearStat = 2;
        }
        else if(fm.CT.getGearL() > 0.95F && fm.CT.getGearR() < 0.95F && fm.CT.getGearC() < 0.95F && LGearStat != 1)
        {
            super.mesh.materialReplace("WlightGear", "WlightGearL");
            LGearStat = 1;
        }
        else if(fm.CT.getGearL() < 0.95F && fm.CT.getGearR() < 0.95F && fm.CT.getGearC() < 0.95F && LGearStat != 0)
        {
            super.mesh.materialReplace("WlightGear", "WlightGear");
            LGearStat = 0;
        }

        if(fm.CT.FlapsControlSwitch == 0 && LFlapStat != 0)
        {
            super.mesh.materialReplace("WlightFlap", "WlightFlap");
            LFlapStat = 0;
        }
        else if(fm.CT.FlapsControlSwitch == 1)
        {
            if(LFlapStat != 1 && !((F_18) aircraft()).bForceFlapmodeAuto)
            {
                super.mesh.materialReplace("WlightFlap", "WlightFlapH");
                LFlapStat = 1;
            }
            else if(LFlapStat != 5 && ((F_18) aircraft()).bForceFlapmodeAuto)
            {
                super.mesh.materialReplace("WlightFlap", "WlightFlapHY");
                LFlapStat = 5;
            }
        }
        else if(fm.CT.FlapsControlSwitch == 2)
        {
            if(LFlapStat != 2 && !((F_18) aircraft()).bForceFlapmodeAuto)
            {
                super.mesh.materialReplace("WlightFlap", "WlightFlapF");
                LFlapStat = 2;
            }
            else if(LFlapStat != 10 && ((F_18) aircraft()).bForceFlapmodeAuto)
            {
                super.mesh.materialReplace("WlightFlap", "WlightFlapFY");
                LFlapStat = 10;
            }
        }

        if(fm.CT.getGear() < 0.1F || (!setNew.isBatteryOn && !setNew.isGeneratorAllive))
        {
            super.mesh.chunkVisible("L_AOA", false);
        }
        else
        {
            super.mesh.chunkVisible("L_AOA", true);
            if(((FlightModelMain) (fm)).getAOA() > 8.9F)   // SLOW, Green
                super.mesh.materialReplace("WlightAOA", "WlightAOA_H");
            else if(((FlightModelMain) (fm)).getAOA() > 8.1F)
                super.mesh.materialReplace("WlightAOA", "WlightAOA_HM");
            else if(((FlightModelMain) (fm)).getAOA() > 6.9F)   // ON SPEED, Yellow
                super.mesh.materialReplace("WlightAOA", "WlightAOA_M");
            else if(((FlightModelMain) (fm)).getAOA() > 6.4F)
                super.mesh.materialReplace("WlightAOA", "WlightAOA_ML");
            else   // FAST, Red
                super.mesh.materialReplace("WlightAOA", "WlightAOA_L");
        }
    }


    public float normalizeDegree(float f)
    {
        if(f < 0.0F)
            do
                f += 360F;
            while(f < 0.0F);
        else
        if(f > 360F)
            do
                f -= 360F;
            while(f >= 360F);
        return f;
    }

    public float normalizeDegree180(float f)
    {
        if(f < -180F)
            do
                f += 360F;
            while(f < -180F);
        else
        if(f > 180F)
            do
                f -= 360F;
            while(f > 180F);
        return f;
    }

    public float getMachForAlt(float f)
    {
        f /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length - 1; i++)
            if(TypeSupersonic.fMachAltX[i] > f)
                break;

        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        }
        else
        {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + f2 * f5;
        }
    }

    public float calculateMach()
    {
        return fm.getSpeedKMH() / getMachForAlt(fm.getAltitude());
    }

    public void reflectCockpitState()
    {
    }

    public void toggleDim()
    {
        isDimmer = !isDimmer;
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
        {
            //super.mesh.chunkVisible("Z_Z_NVision", true);
            light1.light.setEmit(0.0075F, 0.7F);
            light2.light.setEmit(0.0075F, 0.7F);
            // ((F_18) aircraft()).FLIR = true;
            setNightMats(true);
        }
        else
        {
            //super.mesh.chunkVisible("Z_Z_NVision", false);
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            // ((F_18) aircraft()).FLIR = false;
            setNightMats(false);
        }
    }

    private void retoggleLight()
    {
    }

    protected float getNDBDist()
    {
        int i = fm.AS.getBeacon();
        if(i == 0)
        {
            return 0.0F;
        }
        else
        {
            ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (fm)).actor.getArmy());
            Actor actor = (Actor)arraylist.get(i - 1);
            tmpV.sub(actor.pos.getAbsPoint(), fm.Loc);
            return (float)(tmpV.length() * 0.0010000000474974513D) / 1.853F;
        }
    }

    protected float getNDBDirection()
    {
        int i = fm.AS.getBeacon();
        if(i == 0)
        {
            return 0.0F;
        }
        else
        {
            ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (fm)).actor.getArmy());
            Actor actor = (Actor)arraylist.get(i - 1);
            tmpV.x = ((Tuple3d) (actor.pos.getAbsPoint())).x;
            tmpV.y = ((Tuple3d) (actor.pos.getAbsPoint())).y;
            tmpV.z = ((Tuple3d) (actor.pos.getAbsPoint())).z + 20D;
            ((Actor) (aircraft())).pos.getAbs(tmpP);
            tmpP.sub(tmpV);
            float f = 57.32484F * (float)Math.atan2(-((Tuple3d) (tmpP)).y, -((Tuple3d) (tmpP)).x);
            return 360F - f;
        }
    }

    public boolean useRealisticNavigationInstruments()
    {
        return World.cur().diffCur.RealisticNavigationInstruments;
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    protected void reflectPlaneToModel()
    {
//        HierMesh hiermesh = aircraft().hierMesh();
//        mesh.chunkVisible("Nose_D0", hiermesh.isChunkVisible("Nose_D0"));
//        mesh.chunkVisible("Refillrod1", hiermesh.isChunkVisible("Refillrod1"));
//        mesh.chunkVisible("Refillrod2", hiermesh.isChunkVisible("Refillrod2"));
    }

    int getRwrPlaneIdx(Actor actor)
    {
        String classnameFull = actor.getClass().getName();
        int idot = classnameFull.lastIndexOf('.');
        int idol = classnameFull.lastIndexOf('$');
        if (idot < idol) idot = idol;
        String classnameSection = classnameFull.substring(idot + 1);
        for(int num = 0; num < rwrPlaneClassString.length; num++)
        {
            if((classnameSection.indexOf(rwrPlaneClassString[num])) != -1)
            {
                return num;
            }
        }
        return -1;
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private Point3d tmpP;
    private Vector3d tmpV;
    private Matrix3d tmpMat;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private boolean bU4;
    private boolean isDimmer;
    private int blinkCounter;
    private SoundFX aoaWarnFX;
    private SoundFX aoaWarnFX2;
    private SoundFX aoaWarnFX3;
    private SoundFX aoaWarnFX4;
    private SoundFX PullupWarn;
    private SoundFX AltitudeWarn;
    private SoundFX OverGWarn;
    private SoundFX MinimumSpeedWarn;
    private SoundFX MaximumSpeedWarn;
    private SoundFX EngineFireLeftWarn;
    private SoundFX EngineFireRightWarn;
    private long lLastEngineFireLeftWarned;
    private long lLastEngineFireRightWarned;
    private SoundFX BingoFuelWarn;
    private int iBingoFuelWarned;
    private String hudPitchRudderStr[];
    private Gun gun[];
    private float alpha;
    protected Polares dragfs;
    private long to;
    private float ElevationMaxPositive;
    private float ElevationMinNegative;
    public boolean radaron;
    private boolean bHSIDL;
    private boolean bHSIILS;
    private boolean bHSIMAN;
    private boolean bHSINAV;
    private boolean bHSITAC;
    private boolean bHSITGT;
    private boolean bHSIUHF;
    private int oldleftscreen;
    private int oldrightscreen;
    private int oldhddnav;
    private char LFlapStat;
    private char LGearStat;
    protected String[] HUD1 = null;
    private float testfuel;
    private LightPointActor light1;
    private LightPointActor light2;

    private ArrayList radarPlane;
    private ArrayList radarLock;
    private ArrayList radarGround;
    private ArrayList victim;
    private long t2;
    private float x;
    private float y = 0F;
    private double FOV;
    private double ScX;
    private double ScY;
    private double ScZ;
    private float FOrigX;
    private float FOrigY;
    private int nTgts;
    private float RRange;
    private float RClose;
    private float BRange;
    private int BRefresh;
    private int BSteps;
    private float BDiv;
    private long tBOld;
    private boolean start;
    private boolean ground = false;
    private boolean left;
    private boolean right;
    private long t3;
    private long t4 = 0L;
    private float range = 0F;
    private long tw;
    protected float offset;
    protected int targetnum;
    private long ts = 0L;
    private float totalfuel;
    private long timefuel;
    private float becondistance;
    private boolean bF18D;
    private RadarWarningReceiverUtils rwrutils;
    private static final int RWR_MAX_DETECT = 11;   // 16;  // (250 nodes limit in cockpit hier.him)

    private static final float rpmScale[] = {
        0.0F, 190F, 220F, 300F
    };
    private static final float k14TargetMarkScale[] = {
        0.0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F
    };
    private static final float k14TargetWingspanScale[] = {
        9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F
    };
    private static final float k14BulletDrop[] = {
        5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F,
        8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F,
        10.603F, 10.704F, 10.739F, 10.782F, 10.789F
    };
    private static final float k14RocketDrop[] = {
        7.812F, 8.168F, 8.508F, 8.978F, 9.24F, 9.576F, 9.849F, 10.108F, 10.473F, 10.699F,
        10.911F, 11.111F, 11.384F, 11.554F, 11.787F, 11.928F, 11.992F, 12.282F, 12.381F, 12.513F,
        12.603F, 12.704F, 12.739F, 12.782F, 12.789F
    };
    private static final String rwrPlaneClassString[] = {
        "A_50",       "E_2",        "E_3",           "F_4",      "F_5",      "F_8",      "F_14",     "F_15",     "F_16",      "F_18",
        "F_100",      "F_101",      "F_102",         "F_104",    "F_105",    "F_106",    "F_111",    "AV_8",     "MiG_17",    "MiG_19",
        "SM_12",      "F_6",        "FT_6",          "MiG_21",   "MiG21",    "F_7",      "MiG_23",   "MiG_27",   "MiG_25",    "MiG_29",
        "Su_27",      "Su_33",      "MiG_31",        "Su_9",     "Su_11",    "Su_15",    "Su_30",    "Su_34",    "MIRAGEIIIC", "IAI_Nesher",
        "IAI_Dagger", "IAI_Kfir",   "Mirage_2000",   "Tu_126"
    };
    private static final String rwrPlaneMatString[] = {
        "RWRA50",     "RWRE2",      "RWRE3",         "RWRF4",    "RWRF5",    "RWRF8",    "RWRF14",   "RWRF15",   "RWRF16",     "RWRF18",
        "RWRF100",    "RWRF101",    "RWRF102",       "RWRF104",  "RWRF105",  "RWRF106",  "RWRF111",  "RWRAV8",   "RWRMiG17",   "RWRMiG19",
        "RWRMiG19",   "RWRMiG19",   "RWRMiG19",      "RWRMiG21", "RWRMiG21", "RWRMiG21", "RWRMiG23", "RWRMiG23", "RWWRMiG25",  "RWRMiG29",
        "RWRMiG29",   "RWRMiG29",   "RWRMiG31",      "RWRSu9",   "RWRSu11",  "RWRSu15",  "RWRSu30",  "RWRSu34",  "RWRMirage3", "RWRMirage3",
        "RWRMirage3", "RWRMirage3", "RWRMirage2000", "RWRTu126"
    };
    private static final boolean rwrPlaneLock[] = {
        false,        false,        false,           true,       false,      true,       true,       true,       true,         true,
        false,        true,         true,            true,       false,      true,       false,      true,       true,         true,
        true,         true,         true,            true,       true,       true,       true,       true,       true,         true,
        true,         true,         true,            true,       true,       true,       true,       true,       true,         true,
        true,         true,         true,            true
    };
    private static final String rwrGroundClassString[] = {
        "ZSU23",     "M163"
    };
    private static final String rwrGroundMatString[] = {
        "RWRZsu234", "RWRM163"
    };
    private static final boolean rwrGroundLock[] = {
        false,       false
    };
    private static final String rwrShipClassString[] = {
    };
    private static final String rwrShipMatString[] = {
    };
    private static final boolean rwrShipLock[] = {
    };

}