// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 2013/11/19 15:37:03
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitYak36MF.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.util.HashMapExt;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, Yak_36MF, AircraftLH, 
//            TypeGuidedMissileCarrier, Aircraft

public class CockpitYak36MF extends CockpitPilot
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
                setNew.starter = 0.94F * setOld.starter + 0.06F * (((FlightModelMain) (fm)).EI.engines[0].getStage() <= 0 || ((FlightModelMain) (fm)).EI.engines[0].getStage() >= 6 ? 0.0F : 1.0F);
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
                float f4 = ((Yak_36MF)aircraft()).k14Distance;
                setNew.k14w = (5F * CockpitYak36MF.k14TargetWingspanScale[((Yak_36MF)aircraft()).k14WingspanType]) / f4;
                setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
                setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitYak36MF.k14TargetMarkScale[((Yak_36MF)aircraft()).k14WingspanType];
                setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((Yak_36MF)aircraft()).k14Mode;
                Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
                double d = 0.00125D * (double)f4;
                float f5 = (float)Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
                float f6 = -(float)Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
                float f7 = floatindex((f4 - 200F) * 0.04F, CockpitYak36MF.k14BulletDrop) - CockpitYak36MF.k14BulletDrop[0];
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
                f = ((Yak_36MF)aircraft()).fSightCurForwardAngle;
                f1 = ((Yak_36MF)aircraft()).fSightCurSideslip;
                f2 = fm.getAltitude();
                f3 = (float)(-(Math.abs(((FlightModelMain) (fm)).Vwld.length()) * Math.sin(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getTangage())))) * 0.10189999639987946D);
                f3 += (float)Math.sqrt(f3 * f3 + 2.0F * f2 * 0.1019F);
                f4 = Math.abs((float)((FlightModelMain) (fm)).Vwld.length()) * (float)Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getTangage())));
                f5 = (f4 * f3 + 10F) - 10F;
                alpha = 90F - Math.abs(((FlightModelMain) (fm)).Or.getTangage()) - (float)Math.toDegrees(Math.atan(f5 / f2));
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
        return ((Yak_36MF)super.aircraft()).calculateMach();
    }

    public CockpitYak36MF()
    {
        super("3DO/Cockpit/Yak-36MF/hier.him", "bf109");
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
        Yak_36MF Yak_36MF = (Yak_36MF)aircraft();
        if(Yak_36MF.bChangedPit)
        {
            reflectPlaneToModel();
            Yak_36MF _tmp1 = (Yak_36MF)aircraft();
            Yak_36MF.bChangedPit = false;
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) == 0)
        {
            int i = ((Yak_36MF)aircraft()).k14Mode;
            boolean flag = i == 0;
            if(i == 0)
            {
                super.mesh.chunkVisible("Z_Z_RETICLE", true);
                super.mesh.chunkVisible("Z_Z_Bombmark1", true);
                super.mesh.chunkSetAngles("Z_Z_Bombmark", 0.0F, -setNew.k14y, 0.0F);
                super.mesh.chunkSetAngles("Z_Z_Bombmark1", 0.0F, -1.8F * alpha, 0.0F);
            } else
            {
                super.mesh.chunkVisible("Z_Z_RETICLE", false);
                super.mesh.chunkVisible("Z_Z_Bombmark1", false);
            }
            flag = i == 1;
            super.mesh.chunkVisible("Z_Z_RETICLE1", flag);
            super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, setNew.k14y);
            super.mesh.chunkVisible("Z_Z_HUDMARK", flag);
            super.mesh.chunkVisible("Z_Z_ALT_MARK", flag);
            super.mesh.chunkVisible("Z_Z_HUD_LOCK", flag);
            super.mesh.chunkVisible("Z_Z_HUD_Radarrange", flag);
            if(i == 1 && ((Yak_36MF)aircraft()).radarmode != 2)
                radartracking();
            resetYPRmodifier();
            float altfactor = 0.0F;
            float comp = 0.0F;
            if(super.fm.getAltitude() >= 5000F)
            {
                altfactor = 1.2E-005F;
                comp = 0.17F;
            }
            if(super.fm.getAltitude() >= 2000F && super.fm.getAltitude() < 5000F)
            {
                altfactor = 1.6E-005F;
                comp = 0.14794F;
            }
            if(super.fm.getAltitude() >= 1000F && super.fm.getAltitude() < 2000F)
            {
                altfactor = 6E-005F;
                comp = 0.06F;
            }
            if(super.fm.getAltitude() < 1000F)
            {
                altfactor = 0.00012F;
                comp = 0.0F;
            }
            float alt = super.fm.getAltitude() * altfactor + comp;
            Cockpit.xyz[2] = alt;
            super.mesh.chunkSetLocate("Z_Z_ALT_MARK", Cockpit.xyz, Cockpit.ypr);
            resetYPRmodifier();
            Cockpit.xyz[0] = setNew.k14w;
            for(int j = 1; j < 11; j++)
            {
                super.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
                super.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
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
        super.mesh.chunkSetAngles("Z_N1", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 20F), rpmScale2), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_fuel1", cvt(((FlightModelMain) (super.fm)).M.fuel / 2.0F, 0.0F, 2214F, 0.0F, 234F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_temp1", -cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 20F, 175F, 0.0F, 265F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_temp2", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 20F, 175F, 0.0F, 265F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_inlettemp1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 300F, 900F, 0.0F, 225F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_12", cvt(((FlightModelMain) (super.fm)).M.fuel > 1.0F ? 0.77F : 0.0F, 0.0F, 2.0F, 0.0F, 145F), 0.0F, 0.0F);
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
        radarclutter();
        moveHUD(f);
    }

    public void moveHUD(float f)
    {
        float f2 = super.fm.getAltitude() / 1000F;
        float f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_ALT_2", false);
        else
            super.mesh.chunkVisible("Z_Z_ALT_2", true);
        super.mesh.chunkSetAngles("Z_Z_ALT_2", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_ALT_3", false);
        else
            super.mesh.chunkVisible("Z_Z_ALT_3", true);
        super.mesh.chunkSetAngles("Z_Z_ALT_3", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0.0F && f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_ALT_4", false);
        else
            super.mesh.chunkVisible("Z_Z_ALT_4", true);
        super.mesh.chunkSetAngles("Z_Z_ALT_4", 0.0F, 0.0F, f5);
        float f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_5", 0.0F, 0.0F, f6);
        if(f2 > 10F)
        {
            super.mesh.chunkSetAngles("Z_Z_ALT_1", 0.0F, 0.0F, 36F);
        } else
        {
            super.mesh.chunkVisible("Z_Z_ALT_1", false);
            f2 = super.fm.getSpeedKMH() / 1000F;
            f3 = (float)(int)f2 * 36F;
            if(f3 == 0.0F)
                super.mesh.chunkVisible("Z_Z_SPD_1", false);
            else
                super.mesh.chunkVisible("Z_Z_SPD_1", true);
            super.mesh.chunkSetAngles("Z_Z_SPD_1", 0.0F, 0.0F, f3);
            f4 = (float)((int)(f2 * 10F) % 10) * 36F;
            if(f4 == 0.0F && f3 == 0.0F)
                super.mesh.chunkVisible("Z_Z_SPD_2", false);
            else
                super.mesh.chunkVisible("Z_Z_SPD_2", true);
            super.mesh.chunkSetAngles("Z_Z_SPD_2", 0.0F, 0.0F, f4);
            f5 = (float)((int)(f2 * 100F) % 10) * 36F;
            if(f5 == 0.0F && f4 == 0.0F && f3 == 0.0F)
                super.mesh.chunkVisible("Z_Z_SPD_3", false);
            else
                super.mesh.chunkVisible("Z_Z_SPD_3", true);
            super.mesh.chunkSetAngles("Z_Z_SPD_3", 0.0F, 0.0F, f5);
            f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
            super.mesh.chunkSetAngles("Z_Z_SPD_4", 0.0F, 0.0F, f6);
            float f9 = normalizeDegree(setNew.azimuth.getDeg(f) + 90F);
            super.mesh.chunkSetAngles("Z_Z_HUD_HDG", 0.0F, f9, 0.0F);
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

    public void radarclutter()
    {
        long t = Time.current() + World.Rnd().nextLong(-250L, 250L);
        float alt = super.fm.getAltitude();
        if(alt < 1.0F)
            alt = 1.0F;
        if(super.fm.getAltitude() > 300F)
        {
            if(((FlightModelMain) (super.fm)).Or.getTangage() < -25F + 8000F / alt && super.fm.getAltitude() < 2500F)
            {
                clutter = true;
                if(t > t1 + 200L && t < t1 + 400L)
                {
                    super.mesh.chunkVisible("Radarclutter1", true);
                    super.mesh.chunkVisible("Radarclutter2", false);
                    super.mesh.chunkVisible("Radarclutter3", false);
                    super.mesh.chunkVisible("Radarclutter4", false);
                } else
                if(t > t1 + 400L && t < t1 + 800L)
                {
                    super.mesh.chunkVisible("Radarclutter1", false);
                    super.mesh.chunkVisible("Radarclutter2", true);
                    super.mesh.chunkVisible("Radarclutter3", false);
                    super.mesh.chunkVisible("Radarclutter4", false);
                } else
                if(t > t1 + 800L && t < t1 + 1200L)
                {
                    super.mesh.chunkVisible("Radarclutter1", false);
                    super.mesh.chunkVisible("Radarclutter2", false);
                    super.mesh.chunkVisible("Radarclutter3", true);
                    super.mesh.chunkVisible("Radarclutter4", false);
                }
                if(t > t1 + 1200L)
                {
                    t1 = t;
                    super.mesh.chunkVisible("Radarclutter1", false);
                    super.mesh.chunkVisible("Radarclutter2", false);
                    super.mesh.chunkVisible("Radarclutter3", false);
                    super.mesh.chunkVisible("Radarclutter4", true);
                }
                for(int j = 0; j <= nTgts; j++)
                {
                    String m = "Radarmark0" + j;
                    String n = "RadarA0" + j;
                    String o = "RadarB0" + j;
                    String p = "RadarIFF" + j;
                    if(super.mesh.isChunkVisible(p))
                        super.mesh.chunkVisible(p, false);
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                    if(super.mesh.isChunkVisible(n))
                        super.mesh.chunkVisible(n, false);
                    if(super.mesh.isChunkVisible(o))
                        super.mesh.chunkVisible(o, false);
                }

                String m = "Z_Z_HUD_LOCK";
                String n = "Z_Z_HUD_Radarrange";
                if(super.mesh.isChunkVisible(m))
                    super.mesh.chunkVisible(m, false);
                if(super.mesh.isChunkVisible(n))
                    super.mesh.chunkVisible(n, false);
                super.mesh.chunkVisible("Z_Z_Radarmode", false);
                super.mesh.chunkVisible("Z_Z_Radarfault", true);
            } else
            if(((Yak_36MF)aircraft()).radarmode == 0)
            {
                super.mesh.chunkVisible("Z_Z_Radarfault", false);
                clutter = false;
                tracking = true;
                azimult = 0.0D;
                tangage = 0.0D;
                radarscan();
                radarscanIFF();
                super.mesh.chunkVisible("Radarlockrange", true);
                resetYPRmodifier();
                Cockpit.xyz[0] = ((Yak_36MF)aircraft()).lockrange;
                super.mesh.chunkSetLocate("Radarlockrange", Cockpit.xyz, Cockpit.ypr);
                super.mesh.chunkVisible("Radarclutter1", false);
                super.mesh.chunkVisible("Radarclutter2", false);
                super.mesh.chunkVisible("Radarclutter3", false);
                super.mesh.chunkVisible("Radarclutter4", false);
                String m = "Z_Z_HUD_LOCK";
                String n = "Z_Z_HUD_Radarrange";
                if(super.mesh.isChunkVisible(m))
                    super.mesh.chunkVisible(m, false);
                if(super.mesh.isChunkVisible(n))
                    super.mesh.chunkVisible(n, false);
                super.mesh.chunkVisible("Z_Z_Radarmode", false);
                super.mesh.chunkVisible("Z_Z_Radarfault", true);
            }
            if(((Yak_36MF)aircraft()).radarmode == 1 && !clutter)
            {
                for(int j = 0; j <= nTgts + 1; j++)
                {
                    String m = "Radarmark0" + j;
                    String n = "RadarA0" + j;
                    String o = "RadarB0" + j;
                    String p = "RadarIFF" + j;
                    if(super.mesh.isChunkVisible(p))
                        super.mesh.chunkVisible(p, false);
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                    if(super.mesh.isChunkVisible(n))
                        super.mesh.chunkVisible(n, false);
                    if(super.mesh.isChunkVisible(o))
                        super.mesh.chunkVisible(o, false);
                }

                super.mesh.chunkVisible("Radarclutter1", false);
                super.mesh.chunkVisible("Radarclutter2", false);
                super.mesh.chunkVisible("Radarclutter3", false);
                super.mesh.chunkVisible("Radarclutter4", false);
                super.mesh.chunkVisible("Radarlockrange", false);
            }
        }
        if(((Yak_36MF)aircraft()).radarmode == 2)
        {
            super.mesh.chunkVisible("Z_Z_Radarfault", false);
            radarground();
            super.mesh.chunkVisible("Radarlockrange", false);
            super.mesh.chunkVisible("Radarclutter1", false);
            super.mesh.chunkVisible("Radarclutter2", false);
            super.mesh.chunkVisible("Radarclutter3", false);
            super.mesh.chunkVisible("Radarclutter4", false);
            String m = "Z_Z_HUD_LOCK";
            String n = "Z_Z_HUD_Radarrange";
            if(super.mesh.isChunkVisible(m))
                super.mesh.chunkVisible(m, false);
            if(super.mesh.isChunkVisible(n))
                super.mesh.chunkVisible(n, false);
            super.mesh.chunkVisible("Z_Z_Radarmode", false);
        }
    }

    public void radarscan()
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            long ti = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if(ti > t2 + 1250L)
                radarPlane.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft) && ti > t2 + 2000L)
            {
                t2 = ti;
                Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(pointAC);
                        Point3d pointOrtho = new Point3d();
                        pointOrtho.set(actor.pos.getAbsPoint());
                        pointOrtho.sub(pointAC);
                        orientAC.transformInv(pointOrtho);
                        float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                        if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < ((Tuple3d) (pointOrtho)).x * 0.57735026918999999D && ((Tuple3d) (pointOrtho)).y > -((Tuple3d) (pointOrtho)).x * 0.57735026918999999D && ((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.36397023426000003D && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.36397023426000003D)
                            radarPlane.add(pointOrtho);
                    }
                }

                int i1 = radarPlane.size();
                int nt = 0;
                for(int j = 0; j < i1; j++)
                {
                    double x = ((Tuple3d) ((Point3d)radarPlane.get(j))).x;
                    if(x > (double)RClose && nt <= nTgts)
                    {
                        FOV = 60D / x;
                        double NewX = -((Tuple3d) ((Point3d)radarPlane.get(j))).y * FOV;
                        double NewY = ((Tuple3d) ((Point3d)radarPlane.get(j))).x;
                        float f = FOrigX + (float)(NewX * ScX) + (float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getKren())) * 0.011F;
                        if(f > 0.07F)
                            f = 0.07F;
                        if(f < -0.07F)
                            f = -0.07F;
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 > 0.07F)
                            f1 = 0.07F;
                        if(f1 < -0.07F)
                            f1 = -0.07F;
                        nt++;
                        String m = "Radarmark0" + nt;
                        super.mesh.setCurChunk(m);
                        super.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[0] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        String n = "RadarA0" + nt;
                        String o = "RadarB0" + nt;
                        if(((Tuple3d) ((Point3d)radarPlane.get(j))).z > ((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.0192686779D)
                        {
                            if(!super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, true);
                            if(super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, false);
                        }
                        if(((Tuple3d) ((Point3d)radarPlane.get(j))).z < -((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.0192686779D)
                        {
                            if(!super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, true);
                            if(super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, false);
                        }
                        if(((Tuple3d) ((Point3d)radarPlane.get(j))).z >= -((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.0192686779D && ((Tuple3d) ((Point3d)radarPlane.get(j))).z <= ((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.0192686779D)
                        {
                            if(!super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, true);
                            if(!super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, true);
                        }
                    }
                }

                for(int j = nt + 1; j <= nTgts; j++)
                {
                    String m = "Radarmark0" + j;
                    String n = "RadarA0" + j;
                    String o = "RadarB0" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                    if(super.mesh.isChunkVisible(n))
                        super.mesh.chunkVisible(n, false);
                    if(super.mesh.isChunkVisible(o))
                        super.mesh.chunkVisible(o, false);
                }

            }
            if(radarPlane.size() == 0)
            {
                if(super.mesh.isChunkVisible("Radarmark00"))
                    super.mesh.chunkVisible("Radarmark00", false);
                if(super.mesh.isChunkVisible("RadarA00"))
                    super.mesh.chunkVisible("RadarA00", false);
                if(super.mesh.isChunkVisible("RadarB00"))
                    super.mesh.chunkVisible("RadarB00", false);
                for(int j = 0; j <= nTgts + 1; j++)
                {
                    String m = "Radarmark0" + j;
                    String n = "RadarA0" + j;
                    String o = "RadarB0" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                    if(super.mesh.isChunkVisible(n))
                        super.mesh.chunkVisible(n, false);
                    if(super.mesh.isChunkVisible(o))
                        super.mesh.chunkVisible(o, false);
                }

            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radarscanIFF()
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            long ti = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if(ti > to + 1450L)
                radarPlanefriendly.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {
                if(ti > to + 2000L)
                {
                    to = ti;
                    Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                    Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                    List list = Engine.targets();
                    int i = list.size();
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() == World.getPlayerArmy())
                        {
                            Vector3d vector3d = new Vector3d();
                            vector3d.set(pointAC);
                            Point3d pointOrtho = new Point3d();
                            pointOrtho.set(actor.pos.getAbsPoint());
                            pointOrtho.sub(pointAC);
                            orientAC.transformInv(pointOrtho);
                            float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < ((Tuple3d) (pointOrtho)).x * 0.57735026918999999D && ((Tuple3d) (pointOrtho)).y > -((Tuple3d) (pointOrtho)).x * 0.57735026918999999D && ((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.36397023426000003D && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.36397023426000003D)
                                radarPlanefriendly.add(pointOrtho);
                        }
                    }

                }
                int i = radarPlanefriendly.size();
                int nt = 0;
                for(int j = 0; j < i; j++)
                {
                    double x = ((Tuple3d) ((Point3d)radarPlanefriendly.get(j))).x;
                    if(x > (double)RClose && nt <= nTgts)
                    {
                        FOV = 60D / x;
                        double NewX = -((Tuple3d) ((Point3d)radarPlanefriendly.get(j))).y * FOV;
                        double NewY = ((Tuple3d) ((Point3d)radarPlanefriendly.get(j))).x;
                        float f = FOrigX + (float)(NewX * ScX) + (float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getKren())) * 0.011F;
                        if(f > 0.07F)
                            f = 0.07F;
                        if(f < -0.07F)
                            f = -0.07F;
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 > 0.07F)
                            f1 = 0.07F;
                        if(f1 < -0.07F)
                            f1 = -0.07F;
                        nt++;
                        String m = "RadarIFF" + nt;
                        super.mesh.setCurChunk(m);
                        super.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[0] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                    }
                }

                for(int j = nt + 1; j <= nTgts; j++)
                {
                    String m = "RadarIFF" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                }

            }
            if(radarPlanefriendly.size() == 0)
            {
                if(super.mesh.isChunkVisible("RadarIFF0"))
                    super.mesh.chunkVisible("RadarIFF0", false);
                for(int j = 0; j <= nTgts + 1; j++)
                {
                    String m = "RadarIFF" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                }

            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radartracking()
    {
        try
        {
            Aircraft aircraft = World.getPlayerAircraft();
            long ti = Time.current() + World.Rnd().nextLong(-15L, 15L);
            if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
            {
                trefresh = ti;
                Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
                radarTracking.clear();
                List list = Engine.targets();
                int j1 = list.size();
                for(int k1 = 0; k1 < j1; k1++)
                {
                    Actor actor = (Actor)list.get(k1);
                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                    {
                        double D1 = 0.0D;
                        if(azimult == 0.0D && tangage == 0.0D)
                            D1 = 0.16397023425999999D;
                        else
                            D1 = 0.0074886635200000001D;
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(point3d);
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                        double d2 = 300000D;
                        if(!trackzone)
                        {
                            if(((Tuple3d) (point3d1)).x < (double)(((Yak_36MF)aircraft()).lockrange + 0.01F) * d2 - (double)(200F * f) && ((Tuple3d) (point3d1)).x > (double)(((Yak_36MF)aircraft()).lockrange - 0.01F) * d2 - (double)(200F * f) && ((Tuple3d) (point3d1)).y < tangage + ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).y > tangage - ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).z < azimult + ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).z > azimult - ((Tuple3d) (point3d1)).x * D1)
                            {
                                radarTracking.add(point3d1);
                                trackzone = true;
                                super.mesh.chunkVisible("Z_Z_Radarmode", true);
                                ((Yak_36MF)aircraft()).radarmode = 1;
                            }
                        } else
                        if(((Tuple3d) (point3d1)).x < (double)(((Yak_36MF)aircraft()).lockrange + 0.01F) * d2 - (double)(200F * f) && ((Tuple3d) (point3d1)).y < tangage + ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).y > tangage - ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).z < azimult + ((Tuple3d) (point3d1)).x * D1 && ((Tuple3d) (point3d1)).z > azimult - ((Tuple3d) (point3d1)).x * D1)
                            radarTracking.add(point3d1);
                    }
                }

                int i = radarTracking.size();
                int j = 0;
                for(int k = 0; k < i && k < 1; k++)
                {
                    double x = ((Tuple3d) ((Point3d)radarTracking.get(k))).x;
                    tangage = ((Tuple3d) ((Point3d)radarTracking.get(k))).y;
                    azimult = ((Tuple3d) ((Point3d)radarTracking.get(k))).z;
                    if(x < 18000D && j <= nTgts)
                    {
                        FOV = 2680D / x;
                        double d3 = -((Tuple3d) ((Point3d)radarTracking.get(k))).y * FOV;
                        double d4 = ((Tuple3d) ((Point3d)radarTracking.get(k))).z * FOV;
                        double d5 = ((Tuple3d) ((Point3d)radarTracking.get(k))).x;
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
                        j++;
                        String s1 = "Z_Z_HUD_LOCK";
                        super.mesh.setCurChunk(s1);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[2] = f1;
                        super.mesh.chunkSetLocate(s1, Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        String s2 = "Z_Z_HUD_Radarrange";
                        resetYPRmodifier();
                        Cockpit.xyz[2] = f2;
                        super.mesh.chunkSetLocate(s2, Cockpit.xyz, Cockpit.ypr);
                        if(!super.mesh.isChunkVisible(s1))
                            super.mesh.chunkVisible(s1, true);
                        if(!super.mesh.isChunkVisible(s2))
                            super.mesh.chunkVisible(s2, true);
                    } else
                    {
                        String s1 = "Z_Z_HUD_LOCK";
                        String s2 = "Z_Z_HUD_Radarrange";
                        if(super.mesh.isChunkVisible(s1))
                            super.mesh.chunkVisible(s1, false);
                        if(super.mesh.isChunkVisible(s2))
                            super.mesh.chunkVisible(s2, false);
                    }
                }

                if(i == 0)
                {
                    String s1 = "Z_Z_HUD_LOCK";
                    String s2 = "Z_Z_HUD_Radarrange";
                    if(super.mesh.isChunkVisible(s1))
                        super.mesh.chunkVisible(s1, false);
                    if(super.mesh.isChunkVisible(s2))
                        super.mesh.chunkVisible(s2, false);
                    trackzone = false;
                    super.mesh.chunkVisible("Z_Z_Radarmode", false);
                    ((Yak_36MF)aircraft()).radarmode = 0;
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radarground()
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();
            long ti = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if(ti > t2 + 1250L)
                radarPlane.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft) && ti > t2 + 2000L)
            {
                t2 = ti;
                Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    if(((actor instanceof TgtFlak) || (actor instanceof TgtTank) || (actor instanceof TgtTrain) || (actor instanceof TgtVehicle) || (actor instanceof TgtTank)) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(pointAC);
                        Point3d pointOrtho = new Point3d();
                        pointOrtho.set(actor.pos.getAbsPoint());
                        pointOrtho.sub(pointAC);
                        orientAC.transformInv(pointOrtho);
                        float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                        if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < ((Tuple3d) (pointOrtho)).x * 0.57735026918999999D && ((Tuple3d) (pointOrtho)).y > -((Tuple3d) (pointOrtho)).x * 0.57735026918999999D && ((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.36397023426000003D && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.36397023426000003D)
                            radarPlane.add(pointOrtho);
                    }
                }

                int i1 = radarPlane.size();
                int nt = 0;
                for(int j = 0; j < i1; j++)
                {
                    double x = ((Tuple3d) ((Point3d)radarPlane.get(j))).x;
                    if(x > (double)RClose && nt <= nTgts)
                    {
                        FOV = 60D / x;
                        double NewX = -((Tuple3d) ((Point3d)radarPlane.get(j))).y * FOV;
                        double NewY = ((Tuple3d) ((Point3d)radarPlane.get(j))).x;
                        float f = FOrigX + (float)(NewX * ScX) + (float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getKren())) * 0.011F;
                        if(f > 0.07F)
                            f = 0.07F;
                        if(f < -0.07F)
                            f = -0.07F;
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 > 0.07F)
                            f1 = 0.07F;
                        if(f1 < -0.07F)
                            f1 = -0.07F;
                        nt++;
                        String m = "Radarmark0" + nt;
                        super.mesh.setCurChunk(m);
                        super.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[0] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        String n = "RadarA0" + nt;
                        String o = "RadarB0" + nt;
                        if(((Tuple3d) ((Point3d)radarPlane.get(j))).z > ((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.0192686779D)
                        {
                            if(!super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, true);
                            if(super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, false);
                        }
                        if(((Tuple3d) ((Point3d)radarPlane.get(j))).z < -((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.0192686779D)
                        {
                            if(!super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, true);
                            if(super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, false);
                        }
                        if(((Tuple3d) ((Point3d)radarPlane.get(j))).z >= -((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.0192686779D && ((Tuple3d) ((Point3d)radarPlane.get(j))).z <= ((Tuple3d) ((Point3d)radarPlane.get(j))).x * 0.0192686779D)
                        {
                            if(!super.mesh.isChunkVisible(o))
                                super.mesh.chunkVisible(o, true);
                            if(!super.mesh.isChunkVisible(n))
                                super.mesh.chunkVisible(n, true);
                        }
                    }
                }

                for(int j = nt + 1; j <= nTgts; j++)
                {
                    String m = "Radarmark0" + j;
                    String n = "RadarA0" + j;
                    String o = "RadarB0" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                    if(super.mesh.isChunkVisible(n))
                        super.mesh.chunkVisible(n, false);
                    if(super.mesh.isChunkVisible(o))
                        super.mesh.chunkVisible(o, false);
                }

            }
            if(radarPlane.size() == 0)
            {
                if(super.mesh.isChunkVisible("Radarmark00"))
                    super.mesh.chunkVisible("Radarmark00", false);
                if(super.mesh.isChunkVisible("RadarA00"))
                    super.mesh.chunkVisible("RadarA00", false);
                if(super.mesh.isChunkVisible("RadarB00"))
                    super.mesh.chunkVisible("RadarB00", false);
                for(int j = 0; j <= nTgts + 1; j++)
                {
                    String m = "Radarmark0" + j;
                    String n = "RadarA0" + j;
                    String o = "RadarB0" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                    if(super.mesh.isChunkVisible(n))
                        super.mesh.chunkVisible(n, false);
                    if(super.mesh.isChunkVisible(o))
                        super.mesh.chunkVisible(o, false);
                }

            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
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














}