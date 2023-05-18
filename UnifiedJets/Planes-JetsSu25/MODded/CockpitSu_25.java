// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 09/10/2015 07:33:45 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitSu_25.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.util.HashMapExt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, Su_25X, AircraftLH, 
//            TypeGuidedMissileCarrier, Su_25, TypeLaserSpotter

public class CockpitSu_25 extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                if(bNeedSetUp)
                    bNeedSetUp = false;
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = 0.9F * setOld.throttle + ((FlightModelMain) (fm)).CT.PowerControl * 0.1F;
                setNew.starter = 0.94F * setOld.starter + 0.06F * (((FlightModelMain) (fm)).EI.engines[0].getStage() > 0 && ((FlightModelMain) (fm)).EI.engines[0].getStage() < 6 ? 1.0F : 0.0F);
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f - 90F);
                    setOld.waypointAzimuth.setDeg(f - 90F);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                    if(((FlightModelMain) (fm)).AS.listenLorenzBlindLanding)
                    {
                        setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                        setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                        setNew.navDiviation0 = setNew.ilsLoc;
                    } else
                    {
                        setNew.ilsLoc = 0.0F;
                        setNew.ilsGS = 0.0F;
                        setNew.navDiviation0 = normalizeDegree(normalizeDegree(waypointAzimuth(0.02F) + 90F) - normalizeDegree(getNDBDirection()));
                        if(Math.abs(setNew.navDiviation0) < 90F)
                            setNew.navTo0 = true;
                        else
                        if(setNew.navDiviation0 > 270F)
                        {
                            setNew.navTo0 = true;
                            setNew.navDiviation0 = setNew.navDiviation0 - 360F;
                        } else
                        {
                            setNew.navTo0 = false;
                            setNew.navDiviation0 = normalizeDegree180(setNew.navDiviation0);
                            if(setNew.navDiviation0 >= 90F)
                                setNew.navDiviation0 = 180F - setNew.navDiviation0;
                            else
                                setNew.navDiviation0 = -180F - setNew.navDiviation0;
                        }
                    }
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
                }
                Variables variables = setNew;
                float f1 = 0.9F * setOld.radioalt;
                float f2 = 0.1F;
                float f3 = fm.getAltitude();
                World.cur();
                World.land();
                variables.radioalt = f1 + f2 * (f3 - Landscape.HQ_Air((float)((Tuple3d) (((FlightModelMain) (fm)).Loc)).x, (float)((Tuple3d) (((FlightModelMain) (fm)).Loc)).y));
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                setNew.pitch = ((FlightModelMain) (fm)).Or.getPitch();
                setNew.bank = ((FlightModelMain) (fm)).Or.getRoll();
                float f4 = ((Su_25X)aircraft()).k14Distance;
                setNew.k14w = (5F * CockpitSu_25.k14TargetWingspanScale[((Su_25X)aircraft()).k14WingspanType]) / f4;
                setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
                setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitSu_25.k14TargetMarkScale[((Su_25X)aircraft()).k14WingspanType];
                setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((Su_25X)aircraft()).k14Mode;
                Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
                double d = 0.00125D * (double)f4;
                float f5 = (float)Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
                float f6 = -(float)Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
                float f7 = floatindex((f4 - 200F) * 0.04F, CockpitSu_25.k14BulletDrop) - CockpitSu_25.k14BulletDrop[0];
                f6 += (float)Math.toDegrees(Math.atan(f7 / f4));
                setNew.k14x = 0.92F * setOld.k14x + 0.08F * f5;
                setNew.k14y = 0.92F * setOld.k14y + 0.08F * f6;
                if(setNew.k14x > 7F)
                    setNew.k14x = 7F;
                if(setNew.k14x < -7F)
                    setNew.k14x = -7F;
                if(setNew.k14y > 7F)
                    setNew.k14y = 7F;
                if(setNew.k14y < -7F)
                    setNew.k14y = -7F;
                if(Mission.curCloudsType() > 4)
                {
                    iRain = 1;
                    if(fm.getSpeedKMH() < 20F)
                        iRain = 2;
                    if(fm.getAltitude() > Mission.curCloudsHeight() + 300F)
                        iRain = 3;
                } else
                {
                    iRain = 0;
                }
                f2 = fm.getAltitude();
                f3 = (float)(-(Math.abs(((FlightModelMain) (fm)).Vwld.length()) * Math.sin(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getTangage())))) * 0.10189999639987946D);
                f3 += (float)Math.sqrt(f3 * f3 + 2.0F * f2 * 0.1019F);
                f4 = Math.abs((float)((FlightModelMain) (fm)).Vwld.length()) * (float)Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getTangage())));
                f5 = (f4 * f3 + 10F) - 10F;
                alpha = 90F - Math.abs(((FlightModelMain) (fm)).Or.getTangage()) - (float)Math.toDegrees(Math.atan(f5 / f2));
                alpha2 = Math.abs(((FlightModelMain) (fm)).Or.getTangage()) - (float)Math.toDegrees(Math.atan(f5 / f2));
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttle;
        float vspeed;
        float starter;
        float altimeter;
        float radioalt;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float ilsLoc;
        float ilsGS;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;
        float dimPosition;
        float pitch;
        float bank;
        float navDiviation0;
        float navDiviation1;
        boolean navTo0;
        boolean navTo1;

        private Variables()
        {
            throttle = 0.0F;
            starter = 0.0F;
            altimeter = 0.0F;
            vspeed = 0.0F;
            radioalt = 0.0F;
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            aircraft().hierMesh().chunkVisible("NoseF_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        } else
        {
            aircraft().hierMesh().chunkVisible("Blister1_D0", true);
            aircraft().hierMesh().chunkVisible("NoseF_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    private float machNumber()
    {
        return ((Su_25X)super.aircraft()).calculateMach();
    }

    public CockpitSu_25()
    {
        super("3DO/Cockpit/Su-25/hier.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        HookNamed hooknamed = new HookNamed(super.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(300F, 0.0F, 0.0F);
        light1.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        hooknamed = new HookNamed(super.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(300F, 0.0F, 0.0F);
        light2.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        hooknamed = new HookNamed(super.mesh, "LAMPHOOK3");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light3 = new LightPointActor(new LightPoint(), loc.getPoint());
        light3.light.setColor(300F, 0.0F, 0.0F);
        light3.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK3", light3);
        super.cockpitNightMats = (new String[] {
            "Gause1", "Gause2", "Gause3", "Gause4", "Sidepanel", "instrument1"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
        FOV = 1.0D;
        ScX = 0.00099999997764825825D;
        ScY = 3.3000000000000002E-006D;
        ScZ = 0.0010000000474974513D;
        FOrigX = 0.0F;
        FOrigY = 0.0F;
        nTgts = 10;
        RRange = 30000F;
        RClose = 5F;
        BRange = 0.1F;
        BRefresh = 1300;
        BSteps = 12;
        BDiv = BRefresh / BSteps;
        tBOld = 0L;
        radarPlane = new ArrayList();
        radarPlanefriendly = new ArrayList();
        radarTracking = new ArrayList();
        trackzone = false;
    }

    private int iLockState()
    {
        if(!(super.aircraft() instanceof TypeGuidedMissileCarrier))
            return 0;
        else
            return ((TypeGuidedMissileCarrier)super.aircraft()).getGuidedMissileUtils().getMissileLockState();
    }

    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public void reflectWorldToInstruments(float f)
    {
        reflectGlassMats();
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
        Su_25X Su_25X = (Su_25X)aircraft();
        if(Su_25X.bChangedPit)
        {
            reflectPlaneToModel();
            Su_25X _tmp1 = (Su_25X)aircraft();
            Su_25X.bChangedPit = false;
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) == 0)
        {
            int i = ((Su_25X)aircraft()).k14Mode;
            if(i == 0)
            {
                super.mesh.chunkVisible("Z_Z_Bombmark1", true);
                super.mesh.chunkSetAngles("Z_Z_Bombmark", 0.0F, -setNew.k14y, 0.0F);
                super.mesh.chunkSetAngles("Z_Z_Bombmark1", 0.0F, -1.8F * alpha, 0.0F);
            } else
            if(i == 1)
            {
                super.mesh.chunkVisible("Z_Z_Bombmark1", false);
                super.mesh.chunkVisible("Z_Z_Rocketmark1", true);
                super.mesh.chunkSetAngles("Z_Z_Rocketmark", 0.0F, -setNew.k14y, 0.0F);
                super.mesh.chunkSetAngles("Z_Z_Rocketmark1", -setNew.k14x, 2.0F, 0.0F);
            } else
            if(i == 2)
            {
                super.mesh.chunkVisible("Z_Z_Bombmark1", false);
                super.mesh.chunkVisible("Z_Z_Rocketmark1", false);
            }
        }
        resetYPRmodifier();
        super.mesh.chunkSetAngles("Z_Z_HUD_AH1", 0.0F, 0.0F, -((FlightModelMain) (super.fm)).Or.getKren());
        super.mesh.chunkSetAngles("Canopy", -90F * ((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("stick", 0.0F, (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 10F);
        super.mesh.chunkSetAngles("leftrudder", 10F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("rightrudder", 10F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("throttle", 0.0F, -40.909F * interp(setNew.throttle, setOld.throttle, f), 0.0F);
        super.mesh.chunkSetAngles("Z_N2", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 20F), rpmScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_N1", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 20F), rpmScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_fuel1", cvt(((FlightModelMain) (super.fm)).M.fuel / 2.0F, 0.0F, 2214F, 0.0F, 234F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_temp1", -cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 20F, 175F, 0.0F, 265F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_temp2", cvt(((FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 20F, 175F, 0.0F, 265F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_inlettemp1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 300F, 900F, 0.0F, 225F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_12", cvt(((FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 2.0F, 0.0F, 145F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_GCount", 0.0F, 0.0F, -cvt(super.fm.getOverload(), -4.5F, 10F, -110F, 220F));
        super.mesh.chunkSetAngles("Z_AOA", 0.0F, 0.0F, -cvt(super.fm.getAOA(), -10F, 35F, -50F, 180F));
        super.mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        w.set(super.fm.getW());
        ((FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_Z_Compassa", 90F + setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass3", -90F + setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass2", setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_need_blind_V", cvt(setNew.ilsLoc, -63F, 63F, -45F, 45F), 0.0F, 0.0F);
        if(setNew.ilsGS >= 0.0F)
            super.mesh.chunkSetAngles("Z_need_blind_H", cvt(setNew.ilsGS, 0.0F, 0.5F, 0.0F, 40F), 0.0F, 0.0F);
        else
            super.mesh.chunkSetAngles("Z_need_blind_H", cvt(setNew.ilsGS, -0.3F, 0.0F, -40F, 0.0F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(super.fm.getSpeedKMH(), 200F, 2500F, 0.0F, 24F), speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer3", cvt(machNumber(), 0.5F, 3F, 0.0F, 349F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer2", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 200F, 2500F, 0.0F, 24F), speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 340F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 10800F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter3", cvt(interp(setNew.radioalt, setOld.radioalt, f), 0.0F, 600F, 0.0F, 280F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Climb1", cvt(setNew.vspeed, -20F, 20F, -72F, 72F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Turn1", cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_horizontal2", ((FlightModelMain) (super.fm)).Or.getKren(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_horizontal1b", 0.0F, -1.2F * ((FlightModelMain) (super.fm)).Or.getTangage(), 0.0F);
        super.mesh.chunkSetAngles("Z_Gear1", 0.0F, 0.0F, 50F * (pictGear = 0.82F * pictGear + 0.18F * ((FlightModelMain) (super.fm)).CT.GearControl));
        super.mesh.chunkVisible("L_DownC", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_DownL", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_DownR", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_UPC", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("L_UPL", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("L_UPR", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        if(((FlightModelMain) (super.fm)).CT.getFlap() > 0.3F)
            super.mesh.chunkVisible("L_Flapland", true);
        else
            super.mesh.chunkVisible("L_Flapland", false);
        if(((FlightModelMain) (super.fm)).CT.getAirBrake() > 0.1F)
            super.mesh.chunkVisible("L_Airbrake", true);
        else
            super.mesh.chunkVisible("L_Airbrake", false);
        if(((FlightModelMain) (super.fm)).CT.getTrimElevatorControl() > 0.19F)
            super.mesh.chunkVisible("L_Trimland", true);
        else
            super.mesh.chunkVisible("L_Trimland", false);
        if(((FlightModelMain) (super.fm)).CT.getTrimElevatorControl() == 0.0F)
            super.mesh.chunkVisible("L_Trimneutral", true);
        else
            super.mesh.chunkVisible("L_Trimneutral", false);
        if(((FlightModelMain) (super.fm)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.fm)).EI.engines[0].getStage() > 5)
            super.mesh.chunkVisible("L_AB1", true);
        else
            super.mesh.chunkVisible("L_AB1", false);
        if(((FlightModelMain) (super.fm)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.fm)).EI.engines[0].getStage() > 5)
            super.mesh.chunkVisible("L_AB2", true);
        else
            super.mesh.chunkVisible("L_AB2", false);
        super.mesh.chunkVisible("L_Fire1", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
        super.mesh.chunkVisible("L_Fire2", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
        super.mesh.chunkVisible("L_Fuel", ((FlightModelMain) (super.fm)).M.fuel < 450F);
        float f1 = 0.9F * setOld.radioalt;
        float f2 = 0.1F;
        float f3 = super.fm.getAltitude();
        if(f1 + f2 * (f3 - Landscape.HQ_Air((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).x, (float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).y)) < 20F)
            super.mesh.chunkVisible("L_Altitude1", true);
        else
            super.mesh.chunkVisible("L_Altitude1", false);
        if(f1 + f2 * (f3 - Landscape.HQ_Air((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).x, (float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).y)) < 300F)
            super.mesh.chunkVisible("L_Altitude2", true);
        else
            super.mesh.chunkVisible("L_Altitude2", false);
        if(super.fm.getAOA() > 20F)
        {
            super.mesh.chunkVisible("L_AOA1", true);
            super.mesh.chunkVisible("L_AOA2", true);
        } else
        {
            super.mesh.chunkVisible("L_AOA1", false);
            super.mesh.chunkVisible("L_AOA2", false);
        }
        if(((Interpolate) (super.fm)).actor instanceof TypeGuidedMissileCarrier)
            if(iLockState() == 2)
            {
                super.mesh.chunkVisible("L_Missiles", true);
                super.mesh.chunkVisible("Z_Z_Radarmissile", true);
            } else
            {
                super.mesh.chunkVisible("L_Missiles", false);
                super.mesh.chunkVisible("Z_Z_Radarmissile", false);
            }
        if(((Su_25)aircraft()).laserOn)
            laserTrack();
    }

    private void laserTrack()
    {
        Su_25 su_25 = (Su_25)aircraft();
        laserP = TypeLaserSpotter.spot;
        double x = ((Tuple3d) (laserP)).x;
        tangage = ((Tuple3d) (laserP)).y;
        azimult = ((Tuple3d) (laserP)).z;
        FOV = 2680D / x;
        double d3 = -((Tuple3d) (laserP)).y * FOV;
        double d4 = ((Tuple3d) (laserP)).z * FOV;
        double d5 = ((Tuple3d) (laserP)).x;
        float f = (float)(d3 * 0.00099999997764825825D);
        if(f > 0.5F)
            f = 0.5F;
        if(f < -0.5F)
            f = -0.5F;
        float f1 = (float)(d4 * 0.00099999997764825825D);
        if(f1 > 0.5F)
            f1 = 0.5F;
        if(f1 < -0.5F)
            f1 = -0.5F;
        float f2 = (float)(d5 * 1.9000000000000001E-005D);
        if(f2 > 0.5F)
            f2 = 0.5F;
        if(f2 < -0.5F)
            f2 = -0.5F;
        String s1 = "Z_Z_HUD_LOCK";
        super.mesh.setCurChunk(s1);
        resetYPRmodifier();
        Cockpit.xyz[1] = -f;
        Cockpit.xyz[2] = f1;
        super.mesh.chunkSetLocate(s1, Cockpit.xyz, Cockpit.ypr);
        super.mesh.render();
        if(!super.mesh.isChunkVisible(s1))
            super.mesh.chunkVisible(s1, true);
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

    protected float getNDBDirection()
    {
        int i = ((FlightModelMain) (super.fm)).AS.getBeacon();
        if(i == 0)
        {
            return 0.0F;
        } else
        {
            ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (super.fm)).actor.getArmy());
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

    public void reflectCockpitState()
    {
        super.mesh.chunkVisible("Z_Z_RETICLE1", false);
        for(int i = 1; i < 11; i++)
            super.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);

    }

    private void reflectGlassMats()
    {
        if(iRain == iRainSet)
            return;
        int i = super.mesh.materialFind("glass");
        if(i > 0)
            switch(iRain)
            {
            case 1: // '\001'
                super.mesh.materialReplace(i, "glass_rain");
                break;

            case 2: // '\002'
                super.mesh.materialReplace(i, "glass_stationary");
                break;

            case 3: // '\003'
                super.mesh.materialReplace(i, "glass_wet");
                break;

            default:
                super.mesh.materialReplace(i, "glass");
                break;
            }
        iRainSet = iRain;
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
        {
            light1.light.setEmit(0.0065F, 0.5F);
            light2.light.setEmit(0.0065F, 0.5F);
            light3.light.setEmit(0.0065F, 0.5F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            light3.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }

    public void toggleDim()
    {
        super.cockpitDimControl = !super.cockpitDimControl;
        if(super.cockpitDimControl)
        {
            super.mesh.chunkVisible("Z_Z_MASKL", true);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "ZSh-3 Helmet: Sun Glass Down!");
        } else
        {
            super.mesh.chunkVisible("Z_Z_MASKL", false);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "ZSh-3 Helmet: Sun Glass Up!");
        }
    }

    public float angleBetween(Actor actorFrom, Vector3f targetVector)
    {
        return angleBetween(actorFrom, new Vector3d(targetVector));
    }

    void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        super.mesh.chunkVisible("Nose_D0", hiermesh.isChunkVisible("Nose_D0"));
    }

    public float angleBetween(Actor actorFrom, Vector3d targetVector)
    {
        Vector3d theTargetVector = new Vector3d();
        theTargetVector.set(targetVector);
        double angleDoubleTemp = 0.0D;
        Loc angleActorLoc = new Loc();
        Point3d angleActorPos = new Point3d();
        Vector3d angleNoseDir = new Vector3d();
        actorFrom.pos.getAbs(angleActorLoc);
        angleActorLoc.get(angleActorPos);
        angleDoubleTemp = theTargetVector.length();
        theTargetVector.scale(1.0D / angleDoubleTemp);
        angleNoseDir.set(1.0D, 0.0D, 0.0D);
        angleActorLoc.transform(angleNoseDir);
        angleDoubleTemp = angleNoseDir.dot(theTargetVector);
        return Geom.RAD2DEG((float)Math.acos(angleDoubleTemp));
    }

    public float angleActorBetween(Actor actorFrom, Actor actorTo)
    {
        float angleRetVal = 180.1F;
        double angleDoubleTemp = 0.0D;
        Loc angleActorLoc = new Loc();
        Point3d angleActorPos = new Point3d();
        Point3d angleTargetPos = new Point3d();
        Vector3d angleTargRayDir = new Vector3d();
        Vector3d angleNoseDir = new Vector3d();
        actorFrom.pos.getAbs(angleActorLoc);
        angleActorLoc.get(angleActorPos);
        actorTo.pos.getAbs(angleTargetPos);
        angleTargRayDir.sub(angleTargetPos, angleActorPos);
        angleDoubleTemp = angleTargRayDir.length();
        angleTargRayDir.scale(1.0D / angleDoubleTemp);
        angleNoseDir.set(1.0D, 0.0D, 0.0D);
        angleActorLoc.transform(angleNoseDir);
        angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
        angleRetVal = Geom.RAD2DEG((float)Math.acos(angleDoubleTemp));
        return angleRetVal;
    }

    private Point3d laserP;
    private int iRain;
    private int iRainSet;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private float pictAiler;
    private float pictElev;
    private LightPointActor light1;
    private LightPointActor light2;
    private LightPointActor light3;
    private boolean bNeedSetUp;
    private static final float speedometerScale[] = {
        19F, 55F, 90F, 105F, 118.8F, 131F, 144.2F, 157.8F, 171.4F, 185.2F, 
        198.5F, 212.1F, 226.3F, 239.8F, 252.1F, 265.7F, 277F, 291.1F, 302.2F, 314.4F, 
        324F, 335.8F, 346.8F, 359.5F
    };
    private static final float variometerScale[] = {
        -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 
        124F, 147F, 170F
    };
    private static final float rpmScale[] = {
        -5F, 29F, 58F, 87F, 116F, 155F, 188F, 196.71F, 205.42F, 214.13F, 
        222.84F, 231.55F, 240.26F, 248.97F, 257.68F, 266.39F, 275.1F, 283.81F, 292.52F, 301.23F, 
        310F
    };
    private static final float rpmScale2[] = {
        -5F, 29F, 58F, 87F, 100F, 110F, 120F, 132.07F, 144.14F, 156.21F, 
        168.28F, 180.35F, 192.42F, 204.49F, 216.56F, 228.63F, 240.07F, 252.77F, 264.84F, 276.91F, 
        289F
    };
    private static final float k14TargetMarkScale[] = {
        -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F
    };
    private static final float k14TargetWingspanScale[] = {
        11F, 11.3F, 11.8F, 15F, 20F, 25F, 30F, 35F, 40F, 43.05F
    };
    private static final float k14BulletDrop[] = {
        5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 
        8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 
        10.603F, 10.704F, 10.739F, 10.782F, 10.789F
    };
    public Vector3f w;
    private float pictGear;
    private long to;
    double FOV;
    double ScX;
    double ScY;
    double ScZ;
    float FOrigX;
    float FOrigY;
    int nTgts;
    float RRange;
    float RClose;
    float BRange;
    int BRefresh;
    int BSteps;
    float BDiv;
    long tBOld;
    private ArrayList radarPlane;
    private ArrayList radarPlanefriendly;
    private ArrayList radarTracking;
    protected float offset;
    private long t1;
    private long t2;
    private boolean clutter;
    private boolean tracking;
    private long trefresh;
    private double azimult;
    private double tangage;
    private boolean trackzone;
    private Point3d tmpP;
    private Vector3d tmpV;
    private float alpha;
    private float alpha2;















}
