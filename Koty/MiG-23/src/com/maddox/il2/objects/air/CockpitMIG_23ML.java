// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 09.08.2019 20:16:44
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitMIG_23ML.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.artillery.RocketryRocket;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.MissileInterceptable;
import com.maddox.rts.SectFile;
import com.maddox.il2.engine.Mat;
import com.maddox.rts.CLASS;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.CrossVersion;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.util.HashMapExt;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, MIG_23, TypeGuidedMissileCarrier, Aircraft, 
//            Cockpit

public class CockpitMIG_23ML extends CockpitPilot
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
                float wpalti = waypointAzimuth(); //TODO:
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f - 90F);
                    setOld.waypointAzimuth.setDeg(f - 90F);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                    if(((FlightModelMain) (fm)).AS.listenLorenzBlindLanding && ((FlightModelMain) (fm)).AS.isAAFIAS)
                    {
                        setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                        setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                        blindLanding = true;
                        
                    } else
                    {
                        setNew.ilsLoc = 0.0F;
                        setNew.ilsGS = 0.0F;
                        blindLanding = false;
                    }
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), waypointAzimuth() - ((FlightModelMain) (fm)).Or.azimut());
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
                }
                Variables variables = setNew;
                float f1 = 0.9F * setOld.radioalt;
                float f2 = 0.1F;
                float f3 = fm.getAltitude();
                World.cur();
                World.land();
                variables.radioalt = f1 + f2 * (f3 - Landscape.HQ_Air((float)((Tuple3d) (((FlightModelMain) (fm)).Loc)).x, (float)((Tuple3d) (((FlightModelMain) (fm)).Loc)).y));
                variables.rangef = ((MIG_23)aircraft()).directionalDistanceToGround(2000D);
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                double radarrange = (double) track_range;
                float f4 = (float)radarrange;
                if(f4>1500f)f4=1500f;
                if(f4<300f) f4=300f;
                //((MIG_23)aircraft()).k14Distance;
                //f4 = track_range;
                
                if(((MIG_23)aircraft()).radarmode==100 || ((MIG_23)aircraft()).radarmode==101 )
                {
                	angular_timer = Time.current()-angular_timer;
      			  angular_dx=track_elevation-angular_dx;
      			  angular_x = (float) Math.toRadians((1000*angular_dx)/angular_timer);
      			  angular_dx=track_elevation;

      			  angular_dy=track_azimuth-angular_dy;
      			  angular_y = (float) Math.toRadians((1000*52*angular_dy)/angular_timer);
      			  angular_dy=track_azimuth;
      			  angular_timer = Time.current();
                }
                setNew.k14w = (5F * CockpitMIG_23ML.k14TargetWingspanScale[((MIG_23)aircraft()).k14WingspanType]) / f4;
                setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
                setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitMIG_23ML.k14TargetMarkScale[((MIG_23)aircraft()).k14WingspanType];
                setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((MIG_23)aircraft()).k14Mode;
                Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
                double flightTime = 0.00166D * (double)f4;//0.00125D * (double)f4;
                float f5 = 0.0F;
                float f6 = (float)flightTime * (float)Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getTangage())));
                if((((MIG_23)aircraft()).radarmode == 100 && ((MIG_23)aircraft()).RP_23_range == 0)|| (((MIG_23)aircraft()).radarmode == 101 && ((MIG_23)aircraft()).RP_23_range == 0)|| (((MIG_23)aircraft()).radarmode == 201 && ((MIG_23)aircraft()).RP_23_range == 0))
                {
                    f5 = -(float)Math.toDegrees(flightTime *( ((Tuple3d) (vector3d)).z+angular_y));
                    f6 = -((float)Math.toDegrees(flightTime *( ((Tuple3d) (vector3d)).y-angular_x)));
                }
                float f7 = floatindex((f4 - 200F) * 0.04F, CockpitMIG_23ML.k14BulletDrop) - CockpitMIG_23ML.k14BulletDrop[0];
                f6 += (float)Math.toDegrees(Math.atan(f7 / f4));
                setNew.k14x = 0.05F * setOld.k14x + 0.95F * f5;
                setNew.k14y = 0.05F * setOld.k14y + 0.95F * f6;
               
                if(setNew.k14x > 6F)
                    setNew.k14x = 6F;
                if(setNew.k14x < -6F)
                    setNew.k14x = -6F;
                if(setNew.k14y > 6F)
                    setNew.k14y = 6F;
                if(setNew.k14y < -6F)
                    setNew.k14y = -6F;
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
                if(((MIG_23)aircraft()).k14Mode == 2 && variables.rangef < 2000D && variables.rangef > 300D)
                    inRangeLight = true;
                else
                if((((MIG_23)aircraft()).k14Mode == 3 || ((MIG_23)aircraft()).k14Mode == 4) && variables.rangef < 2000D && variables.rangef > 300D)
                    inRangeLight = true;
                else
                if(((MIG_23)aircraft()).k14Mode == 5 && variables.radioalt < 8000F && variables.rangef > 200D)
                    inRangeLight = true;
                else
                    inRangeLight = false;
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
        double rangef;
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

        private Variables()
        {
            throttle = 0.0F;
            starter = 0.0F;
            altimeter = 0.0F;
            vspeed = 0.0F;
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
            ownAC = aircraft();
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
            super.doFocusLeave();
            return;
        }
    }

    private float machNumber()
    {
        return ((MIG_23)super.aircraft()).calculateMach();
    }

    public CockpitMIG_23ML()
    {
        super("3DO/Cockpit/MiG-23/hier.him", "bf109");
        inRangeLight = false;
        inRangeWarn = false;
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        bChanged=true;
        imgW = 512;
        imgH = 512;
        imgCRT = new BufferedImage(imgW, imgH, 2);
        grCRT = imgCRT.createGraphics();
        bufInt = new int[imgW * imgH * 4];
        buf = new byte[imgW * imgH * 4];
        prepareBuf(255,0);
        lookdown = false;

        
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
        CrossVersion.setPrintCompassHeading(this, true);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
        FOV = 1.0D;
        ScX = 0.0012999999776482582D;
        ScY = 3.3000000000000002E-006D;
        ScZ = 0.0010000000474974513D;
        FOrigX = 0.0F;
        FOrigY = 0.0F;
        nTgts = 10;
        RRange = 25000F;
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
        tp_23_direction_right = true;
        tp_23_azimuth = -28;
        rp_23_azimuth = -30;
        rp_23_bar = 1;
		rp_23_down = true;
		rp_23_lockphase = 0;
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
        if(bNeedSetUp)
        {
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
        reflectGlassMats();
        if(super.fm != null)
        {
        	if(Actor.isValid(ownAC))
        	{
        	}
        }
        
        MIG_23 mig_23 = (MIG_23)aircraft();
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) == 0)
        {
            int i = ((MIG_23)aircraft()).k14Mode;
            boolean flag = i < 6;
            /*
            super.mesh.chunkVisible("Z_Z_RETICLE", flag);
            flag = i > 0 && i < 7;
            super.mesh.chunkVisible("Z_Z_RETICLE1", flag);
            switch(i)
            {
            case 1: // '\001'
                super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, 0F + setNew.k14y);
                break;

            case 2: // '\002'
                super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, -0.55F + setNew.k14y);
                break;

            case 3: // '\003'
                super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, -1.32F + 0.39F * setNew.k14y);
                break;

            case 4: // '\004'
                super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, 1.25F * setNew.k14y);
                break;

            case 5: // '\005'
                super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, -0.55F + setNew.k14y);
                break;

            case 6: // '\006'
                super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, 0.0F * setNew.k14x, -0.55F + 0.0F * setNew.k14y);
                break;
            }
            resetYPRmodifier();
            Cockpit.xyz[0] = setNew.k14w;
            for(int j = 1; j < 11; j++)
            {
                super.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
                super.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
            }
*/
        }
        resetYPRmodifier();
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
        super.mesh.chunkSetAngles("Z_AOA", 0.0F, 0.0F, -cvt((2*super.fm.getAOA())-5.5f, -10F, 35F, -50F, 180F));
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
        super.mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 3600F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter3", cvt(interp(setNew.radioalt, setOld.radioalt, f), 0.0F, 600F, 0.0F, 280F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Climb1", cvt(setNew.vspeed, -20F, 20F, -72F, 72F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Turn1", cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_horizontal2", ((FlightModelMain) (super.fm)).Or.getKren(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Horizont", 0.0F, cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -30F, 30F, -3.125F, 3.125F), -((FlightModelMain) (super.fm)).Or.getKren());
        
        submode15();
	    super.mesh.chunkVisible("Z_Z_Bar_1", false);
	    super.mesh.chunkVisible("Z_Z_Bar_2", false);
	    super.mesh.chunkVisible("Z_Z_Bar_3", false);
	    super.mesh.chunkVisible("Z_Z_Bar_4", false);
	    super.mesh.chunkVisible("Z_Z_Bar_down", false);
	    super.mesh.chunkVisible("Z_Z_Bar_up", false);
	    super.mesh.chunkVisible("Z_Z_break", false);
	    super.mesh.chunkVisible("Z_Z_Jam_A", false);
	    super.mesh.chunkVisible("Z_Z_Jam_P", false);
	    super.mesh.chunkVisible("Z_Z_SteeringDot", false);
	    super.mesh.chunkVisible("Z_Z_range_3", false);
	    super.mesh.chunkVisible("Z_Z_range_30", false);
	    super.mesh.chunkVisible("Z_Z_range_60", false);
	    super.mesh.chunkVisible("Z_Z_range_120", false);
	    super.mesh.chunkVisible("Z_Z_Crosshair", false);
	    long time = Time.current();
	    if (time > t60 + 150)
	    {
	    fadeBuf();
	    t60 = time;
	    }
        if(((MIG_23)aircraft()).radarmode == 0)
        {

        	super.mesh.chunkVisible("Z_Z_Crosshair", false);
    	    super.mesh.chunkVisible("Z_Z_HUD_radar", false);
    	    super.mesh.chunkVisible("Z_Z_mode_A", false);
    	    super.mesh.chunkVisible("Z_Z_mode_RL", false);
    	    super.mesh.chunkVisible("Z_Z_mode_TP", false);


    	    super.mesh.chunkVisible("Z_Z_Range_Dynamic", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope1", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope2", false);
    	    super.mesh.chunkVisible("Z_Z_RangeGate", false);
    	    super.mesh.chunkVisible("Z_Z_LAUNCH_AUTHORIZED", false);
    	    
        	if(blindLanding)
        	{
        		super.mesh.chunkVisible("Z_Z_TargetCircle_big", true);
        	    super.mesh.chunkVisible("Z_Z_TargetCircle_small", true);
        	    super.mesh.chunkVisible("Z_Z_ILS_K", true);
        	    super.mesh.chunkVisible("Z_Z_ILS_G", true);
        	super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(setNew.radioCompassAzimuth.getDeg(f * 0.02F)+90F, -45F, 45F, 3F, -3F), 0.0F, 0.0F);
        	
        	if (setNew.ilsGS >= 0.0F)
        	{
        		super.mesh.chunkSetAngles("Z_Z_TargetCircle_small", cvt(setNew.ilsLoc, -63F, 63F, 3F, -3F), cvt(setNew.ilsGS, 0.0F, 1F, 0F, 3.125F), 0.0F);
        	}
        	else
        	{
        		super.mesh.chunkSetAngles("Z_Z_TargetCircle_small", cvt(setNew.ilsLoc, -63F, 63F, 3F, -3F), cvt(setNew.ilsGS, -1F, 0F, -3.125F, 0F), 0.0F);
        	}
        	
        	} 
        	else
        		{
        		super.mesh.chunkVisible("Z_Z_TargetCircle_big", false);
        	    super.mesh.chunkVisible("Z_Z_TargetCircle_small", false);
        	    super.mesh.chunkVisible("Z_Z_ILS_K", false);
        	    super.mesh.chunkVisible("Z_Z_ILS_G", false);
        		}
        	
        }
        
        if(((MIG_23)aircraft()).radarmode == 1)
        {
        	super.mesh.chunkVisible("Z_Z_Crosshair", false);
    	    super.mesh.chunkVisible("Z_Z_HUD_radar", true);
    	    super.mesh.chunkVisible("Z_Z_mode_RL", true);
    	    super.mesh.chunkVisible("Z_Z_mode_TP", false);
    	    super.mesh.chunkVisible("Z_Z_RangeGate", true);
    	    super.mesh.chunkVisible("Z_Z_LAUNCH_AUTHORIZED", false);
    	    super.mesh.chunkSetAngles("Z_Z_RangeGate",
    	    		cvt(((MIG_23)aircraft()).rangeGate_azimuth, -1F, 1F, 3.125F, -3.125F),
    	    		cvt(((MIG_23)aircraft()).rangeGate_range, -1F, 1F, 0F, 7.25F),
    	    		0.0F);
    	    

    	    super.mesh.chunkVisible("Z_Z_Bar_1", (rp_23_bar==1));
    	    super.mesh.chunkVisible("Z_Z_Bar_2", (rp_23_bar==2));
    	    super.mesh.chunkVisible("Z_Z_Bar_3", (rp_23_bar==3));
    	    super.mesh.chunkVisible("Z_Z_Bar_4", (rp_23_bar==4));
    	    super.mesh.chunkVisible("Z_Z_Bar_down", lookdown);
    	    super.mesh.chunkVisible("Z_Z_Bar_up", !lookdown);
    	    
    	    switch(((MIG_23)aircraft()).RP_23_range)
    	    {
    	    case 0:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", true);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    break;
    	    }
    	    case 1:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", true);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    break;
    	    }
    	    case 2:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", true);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    break;
    	    }
    	    case 3:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", true);
        	    break;
    	    }
    	    }
    	    RP_23();
    	    if(((MIG_23)aircraft()).RP_lock)RP_23_lock(((MIG_23)aircraft()).rangeGate_range, ((MIG_23)aircraft()).rangeGate_azimuth);
    	    
    	    /*TODO: RL*/
    	    
        }
        
        if(((MIG_23)aircraft()).radarmode == 11)
        {
        	super.mesh.chunkVisible("Z_Z_Crosshair", false);
    	    super.mesh.chunkVisible("Z_Z_HUD_radar", true);
    	    //super.mesh.chunkVisible("Z_Z_mode_RL", true);
    	    super.mesh.chunkVisible("Z_Z_mode_TP", false);
    	    super.mesh.chunkVisible("Z_Z_RangeGate", true);
    	    super.mesh.chunkVisible("Z_Z_LAUNCH_AUTHORIZED", false);
    	    super.mesh.chunkVisible("Z_Z_RangeGate", false);
    	    

    	    super.mesh.chunkVisible("Z_Z_SteeringDot", false);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", false);
    	    super.mesh.chunkVisible("Z_Z_mode_A", false);

    	    super.mesh.chunkVisible("Z_Z_range_3", false);
    	    super.mesh.chunkVisible("Z_Z_range_30", false);
    	    super.mesh.chunkVisible("Z_Z_range_60", false);
    	    super.mesh.chunkVisible("Z_Z_range_120", false);

    	    super.mesh.chunkVisible("Z_Z_Range_Dynamic", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope1", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope2", false);

    	    super.mesh.chunkVisible("Z_Z_Bar_1", (rp_23_bar==1));
    	    super.mesh.chunkVisible("Z_Z_Bar_2", (rp_23_bar==2));
    	    super.mesh.chunkVisible("Z_Z_Bar_3", (rp_23_bar==3));
    	    super.mesh.chunkVisible("Z_Z_Bar_4", (rp_23_bar==4));
    	    super.mesh.chunkVisible("Z_Z_Bar_down", lookdown);
    	    super.mesh.chunkVisible("Z_Z_Bar_up", !lookdown);
    	    
    	    switch(((MIG_23)aircraft()).RP_23_range)
    	    {
    	    case 0:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", true);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    break;
    	    }
    	    case 1:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", true);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    break;
    	    }
    	    case 2:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", true);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    break;
    	    }
    	    case 3:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", true);
        	    break;
    	    }
    	    }
    	    RP_23();
    	    if(((MIG_23)aircraft()).RP_lock)RP_23_lock(((MIG_23)aircraft()).rangeGate_range, ((MIG_23)aircraft()).rangeGate_azimuth);
    	    
    	    /*TODO: RL*/
    	    
        }

        if(((MIG_23)aircraft()).radarmode == 12)
        {

        	super.mesh.chunkVisible("Z_Z_Crosshair", false);
    	    super.mesh.chunkVisible("Z_Z_SteeringDot", false);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", false);
    	    super.mesh.chunkVisible("Z_Z_mode_A", false);

    	    super.mesh.chunkVisible("Z_Z_range_3", false);
    	    super.mesh.chunkVisible("Z_Z_range_30", false);
    	    super.mesh.chunkVisible("Z_Z_range_60", false);
    	    super.mesh.chunkVisible("Z_Z_range_120", false);

    	    super.mesh.chunkVisible("Z_Z_Range_Dynamic", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope1", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope2", false);
    	    
    	    super.mesh.chunkVisible("Z_Z_Bar_1", false);
    	    super.mesh.chunkVisible("Z_Z_Bar_2", false);
    	    super.mesh.chunkVisible("Z_Z_Bar_3", false);
    	    super.mesh.chunkVisible("Z_Z_Bar_4", false);
    	    super.mesh.chunkVisible("Z_Z_Bar_down", false);
    	    super.mesh.chunkVisible("Z_Z_Bar_up", false);
    	    super.mesh.chunkVisible("Z_Z_HUD_radar", true);
    	    super.mesh.chunkVisible("Z_Z_mode_RL", false);
    	    super.mesh.chunkVisible("Z_Z_mode_TP", true);
    	    super.mesh.chunkVisible("Z_Z_RangeGate", true);
    	    super.mesh.chunkVisible("Z_Z_range_3", false);
    	    super.mesh.chunkVisible("Z_Z_range_30", false);
    	    super.mesh.chunkVisible("Z_Z_range_60", false);
    	    super.mesh.chunkVisible("Z_Z_range_120", false);
    	    super.mesh.chunkVisible("Z_Z_LAUNCH_AUTHORIZED", false);
    	    super.mesh.chunkSetAngles("Z_Z_RangeGate",
    	    		cvt(((MIG_23)aircraft()).rangeGate_azimuth, -1F, 1F, 3.125F, -3.125F),
    	    		cvt(((MIG_23)aircraft()).rangeGate_range, -1F, 1F, 0F, 7.25F), 0.0F);
    	    
    	    TP_23();
    	    if(((MIG_23)aircraft()).TP_lock)
    	    {
    	    	TP_23_lock(((MIG_23)aircraft()).rangeGate_azimuth, ((MIG_23)aircraft()).rangeGate_range, 4f);
    	    }
        }
        //Z_Z_HUD_radar

        if(((MIG_23)aircraft()).radarmode == 13)
        {

        	super.mesh.chunkVisible("Z_Z_Crosshair", true);
    	    super.mesh.chunkVisible("Z_Z_Bar_1", false);
    	    super.mesh.chunkVisible("Z_Z_Bar_2", false);
    	    super.mesh.chunkVisible("Z_Z_Bar_3", false);
    	    super.mesh.chunkVisible("Z_Z_Bar_4", false);
    	    super.mesh.chunkVisible("Z_Z_Bar_down", false);
    	    super.mesh.chunkVisible("Z_Z_Bar_up", false);
    	    super.mesh.chunkVisible("Z_Z_HUD_radar", false);
    	    super.mesh.chunkVisible("Z_Z_mode_A", true);
    	    super.mesh.chunkVisible("Z_Z_mode_RL", false);
    	    super.mesh.chunkVisible("Z_Z_mode_TP", false);
    	    super.mesh.chunkVisible("Z_Z_RangeGate", false);
    	    super.mesh.chunkVisible("Z_Z_range_3", false);
    	    super.mesh.chunkVisible("Z_Z_range_30", false);
    	    super.mesh.chunkVisible("Z_Z_range_60", false);
    	    super.mesh.chunkVisible("Z_Z_range_120", false);
    	    super.mesh.chunkVisible("Z_Z_LAUNCH_AUTHORIZED", (iLockState() == 2));
    	    /*super.mesh.chunkSetAngles("Z_Z_SteeringDot",
    	    		cvt(((MIG_23)aircraft()).Myotka_x, -3.125F, 3.125F, -3.125F, 3.125F),
    	    		cvt(((MIG_23)aircraft()).Myotka_y, -3.125F, 3.125F, -3.125F, 3.125F), 0.0F);
    	    		*/
    	    
        }
        
        if (((MIG_23)aircraft()).radarmode == 100)
        {
        	super.mesh.chunkVisible("Z_Z_HUD_radar", false);
    	    super.mesh.chunkVisible("Z_Z_mode_A", true);
    	    //super.mesh.chunkVisible("Z_Z_mode_RL", true); //mode_RL_flash()
    	    super.mesh.chunkVisible("Z_Z_mode_TP", false);
    	    super.mesh.chunkVisible("Z_Z_RangeGate", false);
    	    super.mesh.chunkVisible("Z_Z_SteeringDot", true);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", true);
    		
    		RP_23_track();
    		super.mesh.chunkVisible("Z_Z_Crosshair", true);
    	    super.mesh.chunkSetAngles("Z_Z_Crosshair", 0, 0, 0);
    	    if(track_elevation>0)
    		super.mesh.chunkSetAngles("Z_Z_SteeringDot", cvt(track_azimuth, -1F, 1F, -3.125F, 3.125F), cvt(track_elevation, 0, +45f, 0F, 3.125F), 0.0F);
    	    else
    		super.mesh.chunkSetAngles("Z_Z_SteeringDot", cvt(track_azimuth, -1F, 1F, -3.125F, 3.125F), cvt(track_elevation, -30f, 0f, -3.125F, 0F), 0.0F);
    		super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.0767F, 0.0767F, -4F, 4F), cvt(track_elevation, -4F, 4F, -4F, 4F), 0.0F);
    		
    		
    	    super.mesh.chunkVisible("Z_Z_LAUNCH_AUTHORIZED", (iLockState() == 2));
    	    super.mesh.chunkVisible("Z_Z_Range_Dynamic", true);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope1", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope2", false);
    		
    		switch(((MIG_23)aircraft()).RP_23_range)
    	    {
    	    case 0:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", true);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 3000F, -0.725F, 6.525F), 0.0F);
        	    if(track_elevation>0)
            		super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.12F, 0.12F, -3.125F, 3.125F), cvt(track_elevation, 0, +6.25f, 0F, 3.125F), 0.0F);
            	    else
            		super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.12F, 0.12F, -3.125F, 3.125F), cvt(track_elevation, -6.25f, 0f, -3.125F, 0F), 0.0F);
        	    super.mesh.chunkSetAngles("Z_Z_SteeringDot", cvt(track_azimuth, -1F, 1F, -52F, 52F)+setNew.k14x, cvt(track_elevation, -15F, 15F, -15F, 15F)+setNew.k14y, 0.0F);
            		
        	    
        	    break;
    	    }
    	    case 1:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", true);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 30000F, -0.725F, 6.525F), 0.0F);
        	    break;
    	    }
    	    case 2:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", true);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 60000F, -0.725F, 6.525F), 0.0F);
        	    break;
    	    }
    	    case 3:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", true);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 120000F, -0.725F, 6.525F), 0.0F);
        	    break;
    	    }
    	    }
    		
        	
        }
        
        if (((MIG_23)aircraft()).radarmode == 101)
        {
        	super.mesh.chunkVisible("Z_Z_Crosshair", true);
        	super.mesh.chunkVisible("Z_Z_HUD_radar", false);
    	    super.mesh.chunkVisible("Z_Z_mode_A", true);
    	    //super.mesh.chunkVisible("Z_Z_mode_RL", true); //mode_RL_flash()
    	    if(track_elevation > 3 || track_elevation < -12)
    	    	super.mesh.chunkVisible("Z_Z_mode_TP", mode_RL_flash()); //TP disengaged, out of gimbals
    	    else
        	    super.mesh.chunkVisible("Z_Z_mode_TP", true);
    	    	//TODO: TP gimbals 
    	    super.mesh.chunkVisible("Z_Z_RangeGate", false);
    	    super.mesh.chunkVisible("Z_Z_SteeringDot", true);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", true);
    		RP_23_track();
    		super.mesh.chunkVisible("Z_Z_LAUNCH_AUTHORIZED", (iLockState() == 2));
    	    super.mesh.chunkSetAngles("Z_Z_Crosshair", 0, 0, 0);
    	    if(track_elevation>0)
    		super.mesh.chunkSetAngles("Z_Z_SteeringDot", cvt(track_azimuth, -1F, 1F, -3.125F, 3.125F), cvt(track_elevation, 0, +45f, 0F, 3.125F), 0.0F);
    	    else
    		super.mesh.chunkSetAngles("Z_Z_SteeringDot", cvt(track_azimuth, -1F, 1F, -3.125F, 3.125F), cvt(track_elevation, -30f, 0f, -3.125F, 0F), 0.0F);
    		super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.0769F, 0.0769F, -4F, 4F), cvt(track_elevation, -4F, 4F, -4F, 4F), 0.0F);
    		

    	    super.mesh.chunkVisible("Z_Z_Range_Dynamic", true);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope1", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope2", false);
    		
    		switch(((MIG_23)aircraft()).RP_23_range)
    	    {
    	    case 0:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", true);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 3000F, -0.725F, 6.525F), 0.0F);
        	    if(track_elevation>0)
            		super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.12F, 0.12F, -3.125F, 3.125F), cvt(track_elevation, 0, +6.25f, 0F, 3.125F), 0.0F);
            	    else
            		super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.12F, 0.12F, -3.125F, 3.125F), cvt(track_elevation, -6.25f, 0f, -3.125F, 0F), 0.0F);
            		super.mesh.chunkSetAngles("Z_Z_SteeringDot", cvt((track_azimuth*52F+setNew.k14x), -4F, 4F, -4F, 4F), cvt(track_elevation+setNew.k14y, -5F, 5F, -5F, 5F), 0.0F);
            		
        	    
        	    break;
    	    }
    	    case 1:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", true);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 30000F, -0.725F, 6.525F), 0.0F);
        	    break;
    	    }
    	    case 2:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", true);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 60000F, -0.725F, 6.525F), 0.0F);
        	    break;
    	    }
    	    case 3:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", true);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 120000F, -0.725F, 6.525F), 0.0F);
        	    break;
    	    }
    	    }
    		
        	
        }
        
        
        if (((MIG_23)aircraft()).radarmode == 200)
        {
        	super.mesh.chunkVisible("Z_Z_Crosshair", true);
        	super.mesh.chunkVisible("Z_Z_LAUNCH_AUTHORIZED", (iLockState() == 2));
        	super.mesh.chunkVisible("Z_Z_HUD_radar", false);
    	    super.mesh.chunkVisible("Z_Z_mode_A", true);
    	    super.mesh.chunkVisible("Z_Z_mode_RL", false);
    	    super.mesh.chunkVisible("Z_Z_mode_TP", true);
    	    super.mesh.chunkVisible("Z_Z_RangeGate", false);
    	    super.mesh.chunkVisible("Z_Z_SteeringDot", true);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", true);
    		TP_23_track();
    		super.mesh.chunkSetAngles("Z_Z_SteeringDot", cvt(track_azimuth, -1F, 1F, -3.125F, 3.125F), cvt(track_elevation, -1F, 1F, -3.125F, 3.125F), 0.0F);
    		
    		if(track_elevation > 0)
    			super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.133F, 0.133F, -4F, 4F), cvt(track_elevation, 0F, 1F, 0F, 3F), 0.0F);
    		else
    			super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.133F, 0.133F, -4F, 4F), cvt(track_elevation, -0.333F, 0F, -4F, 0F), 0.0F);
    		
        	
        }
        if (((MIG_23)aircraft()).radarmode == 201)
        {
        	super.mesh.chunkVisible("Z_Z_Crosshair", true);
        	super.mesh.chunkVisible("Z_Z_HUD_radar", false);
    	    super.mesh.chunkVisible("Z_Z_mode_A", true);
    	    super.mesh.chunkVisible("Z_Z_mode_RL", false);
    	    super.mesh.chunkVisible("Z_Z_mode_TP", true);
    	    super.mesh.chunkVisible("Z_Z_RangeGate", false);
    	    super.mesh.chunkVisible("Z_Z_SteeringDot", true);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", true);
    		TP_23_track();
    		super.mesh.chunkSetAngles("Z_Z_SteeringDot", cvt(track_azimuth, -1F, 1F, -3.125F, 3.125F), cvt(track_elevation, -1F, 1F, -3.125F, 3.125F), 0.0F);
    		
    		if(track_elevation > 0)
    			super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.133F, 0.133F, -4F, 4F), cvt(track_elevation, 0F, 1F, 0F, 3F), 0.0F);
    		else
    			super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.133F, 0.133F, -4F, 4F), cvt(track_elevation, -0.333F, 0F, -4F, 0F), 0.0F);
    		
    		super.mesh.chunkVisible("Z_Z_LAUNCH_AUTHORIZED", (iLockState() == 2));
    	    super.mesh.chunkVisible("Z_Z_Range_Dynamic", true);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope1", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope2", false);
    		
    		switch(((MIG_23)aircraft()).RP_23_range)
    	    {
    	    case 0:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", true);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 3000F, -0.725F, 6.525F), 0.0F);
        	    
        	    if(track_elevation > 0)
        			super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.266F, 0.266F, -4F, 4F), cvt(track_elevation, 0F, 2F, 0F, 3F), 0.0F);
        		else
        			super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -0.266F, 0.266F, -4F, 4F), cvt(track_elevation, -0.666F, 0F, -4F, 0F), 0.0F);
        		
        		if(track_elevation > 0)
        			super.mesh.chunkSetAngles("Z_Z_SteeringDot", cvt(track_azimuth, -0.133F, 0.133F, -4F, 4F)+setNew.k14x, cvt(track_elevation, 0F, 1F, 0F, 3F)+setNew.k14y, 0.0F);
        		else
        			super.mesh.chunkSetAngles("Z_Z_SteeringDot", cvt(track_azimuth, -0.133F, 0.133F, -4F, 4F)+setNew.k14x, cvt(track_elevation, -0.333F, 0F, -4F, 0F)+setNew.k14y, 0.0F);
        		
        	    break;
    	    }
    	    case 1:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", true);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 30000F, -0.725F, 6.525F), 0.0F);
        	    break;
    	    }
    	    case 2:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", true);
        	    super.mesh.chunkVisible("Z_Z_range_120", false);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 60000F, -0.725F, 6.525F), 0.0F);
        	    break;
    	    }
    	    case 3:
    	    {

        	    super.mesh.chunkVisible("Z_Z_range_3", false);
        	    super.mesh.chunkVisible("Z_Z_range_30", false);
        	    super.mesh.chunkVisible("Z_Z_range_60", false);
        	    super.mesh.chunkVisible("Z_Z_range_120", true);
        	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 120000F, -0.725F, 6.525F), 0.0F);
        	    break;
    	    }
    	    }
    		
        	
        }
        if (((MIG_23)aircraft()).radarmode == 1001)
        {
        	super.mesh.chunkVisible("Z_Z_Crosshair", true);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", true);
        	super.mesh.chunkVisible("Z_Z_HUD_radar", false);
        	super.mesh.chunkVisible("Z_Z_Range_Dynamic", true);
        	super.mesh.chunkVisible("Z_Z_range_3", true);
    	    super.mesh.chunkVisible("Z_Z_range_30", false);
    	    super.mesh.chunkVisible("Z_Z_range_60", false);
    	    super.mesh.chunkVisible("Z_Z_range_120", false);
    	    AG_Bomb();
    	    super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -4F, 4F, -4F, 4F), cvt(track_elevation, -8F, 4F, -8F, 4F), 0.0F);
    	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 3000F, -0.725F, 6.525F), 0.0F);
    	    
        }
        if (((MIG_23)aircraft()).radarmode == 1002)
        {
        	super.mesh.chunkVisible("Z_Z_Crosshair", true);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", true);
        	super.mesh.chunkVisible("Z_Z_HUD_radar", false);
        	super.mesh.chunkVisible("Z_Z_Range_Dynamic", true);
        	super.mesh.chunkVisible("Z_Z_range_3", true);
    	    super.mesh.chunkVisible("Z_Z_range_30", false);
    	    super.mesh.chunkVisible("Z_Z_range_60", false);
    	    super.mesh.chunkVisible("Z_Z_range_120", false);
    	    AG_S5();
    	    super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -4F, 4F, -4F, 4F), cvt(track_elevation, -4F, 4F, -4F, 4F), 0.0F);
    	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 3000F, -0.725F, 6.525F), 0.0F);
        }
        if (((MIG_23)aircraft()).radarmode == 1003)
        {
        	super.mesh.chunkVisible("Z_Z_Crosshair", true);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", true);
        	super.mesh.chunkVisible("Z_Z_HUD_radar", false);
        	super.mesh.chunkVisible("Z_Z_Range_Dynamic", true);
        	super.mesh.chunkVisible("Z_Z_range_3", true);
    	    super.mesh.chunkVisible("Z_Z_range_30", false);
    	    super.mesh.chunkVisible("Z_Z_range_60", false);
    	    super.mesh.chunkVisible("Z_Z_range_120", false);
    	    AG_gun();
    	    super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -4F, 4F, -4F, 4F), cvt(track_elevation, -4F, 4F, -4F, 4F), 0.0F);
    	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 3000F, -0.725F, 6.525F), 0.0F);
        }
        if (((MIG_23)aircraft()).radarmode == 1004)
        {
        	super.mesh.chunkVisible("Z_Z_Crosshair", true);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", true);
        	super.mesh.chunkVisible("Z_Z_HUD_radar", false);
        	super.mesh.chunkVisible("Z_Z_Range_Dynamic", true);
        	super.mesh.chunkVisible("Z_Z_range_3", true);
    	    super.mesh.chunkVisible("Z_Z_range_30", false);
    	    super.mesh.chunkVisible("Z_Z_range_60", false);
    	    super.mesh.chunkVisible("Z_Z_range_120", false);
    	    AG_S24();
    	    super.mesh.chunkSetAngles("Z_Z_TargetCircle_big", cvt(track_azimuth, -4F, 4F, -4F, 4F), cvt(track_elevation, -4F, 4F, -4F, 4F), 0.0F);
    	    super.mesh.chunkSetAngles("Z_Z_Range_Dynamic", 0.0f, cvt(track_range, 0F, 3000F, -0.725F, 6.525F), 0.0F);
        }
        
        
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
        {

            super.mesh.chunkVisible("Z_Z_MainGrid", true);
            super.mesh.chunkVisible("Z_Z_Horizont", true);
            if(iLockState() == 2)
            {
                if(((MIG_23)aircraft()).k14Mode == 6)
                    super.mesh.chunkVisible("L_Missiles", true);
                super.mesh.chunkVisible("Z_Z_Radarmissile", true);
            } else
            {
                if(((MIG_23)aircraft()).k14Mode == 6)
                    super.mesh.chunkVisible("L_Missiles", false);
                super.mesh.chunkVisible("Z_Z_Radarmissile", false);
            }
            /*
            if(((MIG_23)aircraft()).k14Mode == 7)
                super.mesh.chunkVisible("Z_Z_RETICLE", false);
            else
                super.mesh.chunkVisible("Z_Z_RETICLE", true);
            */
            if(((MIG_23)aircraft()).k14Mode >= 2 && ((MIG_23)aircraft()).k14Mode <= 5)
                if(inRangeLight)
                    super.mesh.chunkVisible("L_Missiles", true);
                else
                    super.mesh.chunkVisible("L_Missiles", false);
        }
        long imageTimer = Time.current();
        if(imageTimer > image_refresh + 100)
        {
        updateImage();
        image_refresh = imageTimer;
        }
    }
    

    public void submode15()
    {
    	boolean Submode15 = (fm.getAltitude() < 1500f);

	    super.mesh.chunkVisible("Z_Z_Submode15", Submode15);
	    super.mesh.chunkVisible("Z_Z_Altitude_15", !Submode15);
	    super.mesh.chunkVisible("Z_Z_Altitude_1_5", Submode15);
	    super.mesh.chunkVisible("Z_Z_AltitudeMark", true);
	    
	    if(fm.getAltitude()>1500f)
	    {
	    	super.mesh.chunkSetAngles("Z_Z_AltitudeMark", 0.0F, cvt(fm.getAltitude(), 1500F, 15000F, 0F, 6.525F), 0.0F);
	    	return;
	    }
	    else
	    if(fm.getAltitude()<=1500f)
	    {
	    	float RadioAltitude = Landscape.HQ_Air((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).x, (float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).y);
	    	RadioAltitude = fm.getAltitude() - RadioAltitude;
	    	
	    	if(RadioAltitude<100f)
	    	{
	    		super.mesh.chunkSetAngles("Z_Z_AltitudeMark", 0.0F, cvt(RadioAltitude, 0.0F, 100F, -0.725F, 2.175F), 0.0F);
	    	}
	    	if(RadioAltitude>=100f && RadioAltitude<200f)
	    	{
	    		super.mesh.chunkSetAngles("Z_Z_AltitudeMark", 0.0F, cvt(RadioAltitude, 100F, 200F, 2.175F, 3.625F), 0.0F);
	    	}
	    	if(RadioAltitude>=200f && RadioAltitude<500f)
	    	{
	    		super.mesh.chunkSetAngles("Z_Z_AltitudeMark", 0.0F, cvt(RadioAltitude, 200F, 500F, 3.625F, 5.075F), 0.0F);
	    	}
	    	if(RadioAltitude>500f)
	    	{
	    		super.mesh.chunkSetAngles("Z_Z_AltitudeMark", 0.0F, cvt(RadioAltitude, 500F, 1500F, 5.075F, 6.525F), 0.0F);
	    	}
	    }
    }
    public void TP_23()
    {
    	try
    	{
    	long time = Time.current();
	    if (time > t_tp23 + 120)
	    {
	    	Aircraft aircraft = World.getPlayerAircraft();
		    	if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
		    	{
		    		Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
	                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
	                List list = Engine.targets();
	                int j = list.size();
	                for(int k = 0; k < j; k++)
	                {
	                	Actor actor = (Actor)list.get(k);
	                    if(((actor instanceof Aircraft) && actor != World.getPlayerAircraft())|| (((actor instanceof RocketryRocket)) && (!((RocketryRocket)actor).isOnRamp())) || (((actor instanceof MissileInterceptable)) && (((MissileInterceptable)actor).isReleased())))
	                    {
	                        Vector3d vector3d = new Vector3d();
	                        vector3d.set(point3d);
	                        Point3d point3d1 = new Point3d();
	                        point3d1.set(actor.pos.getAbsPoint());
	                        point3d1.sub(point3d);
	                        orient.transformInv(point3d1);
	                          float irst_range = ((MIG_23)aircraft()).TP_23_model;
	                          float aspect = GuidedMissileUtils.angleBetween(actor, aircraft);
	                          aspect = aspect / 180;
	                        if( ((Tuple3d) (point3d1)).x > 300 && ((Tuple3d) (point3d1)).x < (((irst_range*0.25) + (irst_range*0.75*aspect))*1000))
	                        {
	                        	float alpha = (float) - Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).y/((Tuple3d) (point3d1)).x));
	                        	if (tp_23_azimuth -2f <= alpha && alpha <= tp_23_azimuth + 2f)
	                        	{
	                        		float beta = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).z/((Tuple3d) (point3d1)).x));
	                        		if(beta <= 3 && beta >= -12)
	                        		{
	                        			float size = (float) (((Tuple3d) (point3d1)).x / (((irst_range*0.25) + (irst_range*0.75*aspect))*1000));
	                        			size = 1-size;
	                        			if(((actor instanceof RocketryRocket))||(actor instanceof MissileInterceptable)) size *=0.75;
	                        			drawContact_TP(alpha,beta,size);
	                        		}
	                        	}
	                        }
	                    }
	                }
		    		
		    	if(tp_23_direction_right)
		    	{
		    		tp_23_azimuth += 2;
		    		if (tp_23_azimuth > 28)
		    		{
		    			tp_23_azimuth = 28;
		    			tp_23_direction_right = false;
		    		}
		    	}
		    	else
		    	{
		    		tp_23_azimuth -=2;
		    		if (tp_23_azimuth < -28)
		    		{
		    			tp_23_azimuth = -28;
		    			tp_23_direction_right = true;
		    		}
		    	}
	    	}
	    t_tp23 = time;
	    }
    	}
    	catch(Exception exception)
        {
            exception.printStackTrace();
        }
    	
    }
    public void TP_23_lock(float box_x, float box_y, float fov)
    {
    	
    	box_x *= 28;
    	if(box_y > 0)
    		box_y *=3;
    	else
    		box_y *=12;
    	fov *= 0.5;
    	
    	Aircraft aircraft = World.getPlayerAircraft();
    	if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
    	{
    		Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
            Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
            List list = Engine.targets();
            int j = list.size();
            for(int k = 0; k < j; k++)
            {
            	Actor actor = (Actor)list.get(k);
            	if(((actor instanceof Aircraft) && actor != World.getPlayerAircraft())|| (((actor instanceof RocketryRocket)) && (!((RocketryRocket)actor).isOnRamp())) || (((actor instanceof MissileInterceptable)) && (((MissileInterceptable)actor).isReleased())))
                {
                    Vector3d vector3d = new Vector3d();
                    vector3d.set(point3d);
                    Point3d point3d1 = new Point3d();
                    point3d1.set(actor.pos.getAbsPoint());
                    point3d1.sub(point3d);
                    orient.transformInv(point3d1);
                      float irst_range = ((MIG_23)aircraft()).TP_23_model;
                      float aspect = GuidedMissileUtils.angleBetween(actor, aircraft);
                      aspect = aspect / 180;
                    if( ((Tuple3d) (point3d1)).x > 300 && ((Tuple3d) (point3d1)).x < (((irst_range*0.25) + (irst_range*0.75*aspect))*1000))
                    {
                    	float alpha = (float) - Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).y/((Tuple3d) (point3d1)).x));
                    	if (box_x - (fov) <= alpha && alpha <= box_x + (fov))
                    	{
                    		float beta = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).z/((Tuple3d) (point3d1)).x));
                    		if(beta <= box_y + fov && beta >= box_y - fov)
                    		{
                    			float size = (float) (((Tuple3d) (point3d1)).x / (((irst_range*0.25) + (irst_range*0.75*aspect))*1000));
                    			size = 1-size;
                    			if(((actor instanceof RocketryRocket))||(actor instanceof MissileInterceptable)) size *=0.75;
                    			if (size > 0.02f)
                    			{
                    				((MIG_23)aircraft()).victim_TP = actor;
                    				((MIG_23)aircraft()).radarmode = 201;
                    				((MIG_23)aircraft()).RP_23_range = 1;
                    			}
                    		}
                    	}
                    }
                }
            }
    	}

		((MIG_23)aircraft()).TP_lock = false;
    }
    public boolean mode_RL_flash()
    {
    	long timer = Time.current();
    	boolean returner = true;
    			if(timer > tp_flash_timer + 500)
    	        returner = false;
    			if(timer > tp_flash_timer + 1000)
    				tp_flash_timer = timer;
    				return returner;
    }
    public void TP_23_track()
    {

    	Aircraft aircraft = World.getPlayerAircraft();
    	if(Actor.isValid(aircraft))
    	{
    	Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
        Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
    	Actor victim = ((MIG_23)aircraft()).victim_TP;
    	Vector3d vector3d = new Vector3d();
        vector3d.set(point3d);
        Point3d point3d1 = new Point3d();
        point3d1.set(victim.pos.getAbsPoint());
        point3d1.sub(point3d);
        orient.transformInv(point3d1);
          float irst_range = ((MIG_23)aircraft()).TP_23_model;
          float aspect = GuidedMissileUtils.angleBetween(victim, aircraft);
          aspect = aspect / 180;
          if(Actor.isValid(victim) && ((Tuple3d) (point3d1)).x > 200 && ((Tuple3d) (point3d1)).x < (((irst_range*0.25) + (irst_range*0.75*aspect))*1000))
          {
        	  track_elevation = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).z/((Tuple3d) (point3d1)).x));
        	  track_azimuth = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).y/((Tuple3d) (point3d1)).x));
        	  if(track_azimuth > -30f && track_azimuth < 30f && track_elevation > -12f && track_elevation < 3)
        	  {
        		  
        		  ((MIG_23)aircraft()).Myotka_x = track_elevation;
        		  ((MIG_23)aircraft()).Myotka_y = -track_azimuth;
        		  track_azimuth = track_azimuth/30;
        		  if(track_elevation>0) track_elevation = track_elevation /3; else track_elevation = track_elevation/12;
        		  track_range = (float)Math.sqrt((float)((Tuple3d) (point3d1)).x*(float)((Tuple3d) (point3d1)).x+(float)((Tuple3d) (point3d1)).z*(float)((Tuple3d) (point3d1)).z+(float)((Tuple3d) (point3d1)).y*(float)((Tuple3d) (point3d1)).y);
        		  if(track_range < 3000) 
      		    	((MIG_23)aircraft()).RP_23_range = 0;
      		  else

      		    	((MIG_23)aircraft()).RP_23_range = 1;
        		  
        		  return;
        	  }
          }
    	}
    	
    		((MIG_23)aircraft()).victim_TP = null;
    		((MIG_23)aircraft()).radarmode = 12;
    	    super.mesh.chunkVisible("Z_Z_SteeringDot", false);
    		super.mesh.chunkVisible("Z_Z_TargetCircle_big", false);
    	    super.mesh.chunkVisible("Z_Z_mode_A", false);

    	    ((MIG_23)aircraft()).Myotka_x = 0;
    	    ((MIG_23)aircraft()).Myotka_y = 0;
    	    
    	    super.mesh.chunkVisible("Z_Z_range_3", false);
    	    super.mesh.chunkVisible("Z_Z_range_30", false);
    	    super.mesh.chunkVisible("Z_Z_range_60", false);
    	    super.mesh.chunkVisible("Z_Z_range_120", false);

    	    super.mesh.chunkVisible("Z_Z_Range_Dynamic", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope1", false);
    	    super.mesh.chunkVisible("Z_Z_MissileEnvelope2", false);
    	    
    		return;
    	
    	
    }

    public void RP_23()  //XXX:RP-23
    {

    	float antennapitch = 0;
    	switch(rp_23_bar)
    	{
    	case 1:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation + 2f;
    	break;}
    	case 2:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation;
    	break;}
    	case 3:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation - 2f;
    	break;}
    	case 4:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation - 4f;
    	break;}
    	}
    	
    	
    	if(antennapitch >=0)lookdown = false; else lookdown = true;
    	
    	if(!((MIG_23)aircraft()).BSV_SC)
    	{
    	if(fm.getAltitude() > 1500f)
    		{
    		if(antennapitch >=0)
    		{
    			((MIG_23)aircraft()).BSV_mode = 0;
    			((MIG_23)aircraft()).RP_23_range = 2;
    			
    		}
    			else
    			{

        			((MIG_23)aircraft()).BSV_mode = 1;
        			((MIG_23)aircraft()).RP_23_range = 2;
        			
    			}
    		}
    	if(fm.getAltitude() <= 1500f)
    		{
    		if(antennapitch >=0)
    		{
    			((MIG_23)aircraft()).BSV_mode = 2;
    			((MIG_23)aircraft()).RP_23_range = 1;
    			
    		}
    			else
    			{

        			((MIG_23)aircraft()).BSV_mode = 3;
        			((MIG_23)aircraft()).RP_23_range = 1;
        			
    			}
    		}
    	}
    	else
    	{
    		((MIG_23)aircraft()).BSV_mode = 4;
    	}
    	
    	
    	
    	try
    	{
    	long time = Time.current();
	    if (time > t_rp23 + 10)
	    {
	    	float rp_23_pitch = 0;
	    	switch(rp_23_bar)
	    	{
	    	case 1:
	    	{rp_23_pitch = ((MIG_23)aircraft()).RP_23_elevation + 3f;
	    	break;}
	    	case 2:
	    	{rp_23_pitch = ((MIG_23)aircraft()).RP_23_elevation + 0.875f;
	    	break;}
	    	case 3:
	    	{rp_23_pitch = ((MIG_23)aircraft()).RP_23_elevation - 0.875f;
	    	break;}
	    	case 4:
	    	{rp_23_pitch = ((MIG_23)aircraft()).RP_23_elevation - 3f;
	    	break;}
	    	}
	    	
	    	float K = ((FlightModelMain) (super.fm)).Or.getKren();
	    	float T = ((FlightModelMain) (super.fm)).Or.getTangage();
	    	if (T > 35)T=35;
	    	if (T < -30)T=-30;
	    	if (K>72) K=72;
	    	if (K<-72) K=-72;
	    	float alpha1 = (float) (Math.cos(Math.toRadians(K))*rp_23_pitch);
	    	float alpha2 = (float) (Math.sin(Math.toRadians(K))*rp_23_pitch);
	    	float beta1 = (float) (Math.sin(Math.toRadians(K))*rp_23_azimuth);
	    	float beta2 = (float) (Math.cos(Math.toRadians(K))*rp_23_azimuth);
	    	
	    	float x = -T+alpha1+beta1;
	    	float y = +alpha2-beta2;
	    	
	    	double noise = RP_23_noiseCheck(x,y);
	    	noise += RP_23_noiseCheckBroad(x,y);
	    	RP_23_noise = (float)noise;
	    	double ECCM_power = ((MIG_23)aircraft()).ECCM_power;
	      	double ECCM_RCS = 16;
	      	int PPS = ((MIG_23)aircraft()).RP_23_range;
	      	if(PPS == 0) ECCM_power /=2;
	      	double tresholdCheck =  16*ECCM_power/((Math.pow((RP_23_range()*1000)*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
	    	Aircraft aircraft = World.getPlayerAircraft();
		    	if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
		    	{
		    		Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
	                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
	                List list = Engine.targets();
	                int j = list.size();
		    		
	                for(int k = 0; k < j; k++)
	                {
	                	Actor actor = (Actor)list.get(k);
	                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft()|| (((actor instanceof RocketryRocket)) && (!((RocketryRocket)actor).isOnRamp())) || (((actor instanceof MissileInterceptable)) && (((MissileInterceptable)actor).isReleased())))
	                    {
	                    	if(actor instanceof TypeJammer)ECCM_RCS = ((TypeJammer)actor).getJammerRCS(aircraft);
	                        Vector3d vector3d = new Vector3d();
	                        vector3d.set(point3d);
	                        Point3d point3d1 = new Point3d();
	                        point3d1.set(actor.pos.getAbsPoint());
	                        point3d1.sub(point3d);
	                        orient.transformInv(point3d1);
	                          float radar_range = RP_23_range();
	                          float aspect = GuidedMissileUtils.angleBetween(actor, aircraft);
	                          
	                          
	                          
	                          if(actor instanceof Aircraft && !(actor instanceof TypeHelicopter))
	                          {
	                          float velocity = ((Aircraft)actor).FM.getSpeed();
	                          velocity *= (float)Math.cos(Math.toRadians(aspect));
	                          if(PPS == 0)velocity = Math.abs(velocity);
	                          if(PPS == -1 || ((MIG_23)aircraft()).BSV_mode == 3)velocity *= -1;
	                          if(velocity < 60 || velocity > 700) continue;
	                          }
	                          else
	                        	  if(actor instanceof TypeHelicopter)
	                        	  {
	                        		  float velocity = ((Aircraft)actor).FM.getSpeed();
	    	                          velocity *= (float)Math.cos(Math.toRadians(aspect));
	    	                          velocity = Math.abs(velocity);
	    	                          if(velocity < 60 || velocity > 700) ECCM_RCS /=2 ;
	                        	  }
	                        	  else
	                        	  {
	                          
	              	    		switch (((MIG_23)aircraft()).BSV_mode)
	              	    		{
	              	    		case 0:
	              	    			break;
	              	    		case 1:
	              	    			if(aspect > 70 && aspect < 110) continue;
	              	    			break;
	              	    		case 2:
	              	    			if(aspect > 70 && aspect < 110) continue;
	              	    			break;
	              	    		case 3:
	              	    			if(aspect < 110) continue;
	              	    			break;
	              	    		case 4:
	              	    			if(aspect > 70 && aspect < 110) continue;
	              	    			break;
	              	    			default:
	              	    				break;
	              	    		}
	              	    		}
	                          
	                        if( ((Tuple3d) (point3d1)).x > 300 && ((Tuple3d) (point3d1)).x < (radar_range)*1000)
	                        {
	                        	float alpha = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).z/((Tuple3d) (point3d1)).x));
	                        	float beta = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).y/((Tuple3d) (point3d1)).x));
	                        	
	                	    	
	                	    	
	                        	float z = (float) Math.sqrt(
	                        		     ((Tuple3d) (point3d1)).x*((Tuple3d) (point3d1)).x
	                        			+((Tuple3d) (point3d1)).y*((Tuple3d) (point3d1)).y
	                        			+((Tuple3d) (point3d1)).z*((Tuple3d) (point3d1)).z);
	                        		
	                        	if(alpha > x - 1.25f && alpha < x + 1.25f && beta > y - 1.25f && beta < y + 1.25)
	                        	{
	                        		
	                    	    	float alpha22 = (float) (Math.sin(Math.toRadians(K))*alpha);
	                    	    	
	                    	    	float beta22 = (float) (Math.cos(Math.toRadians(K))*beta);
	                        		
		                	    	float y3 = +alpha22-beta22;
	                        		boolean iff = false;
	                        		iff = actor.getArmy() == World.getPlayerArmy();
	                        		
	                        		double radar_return = ECCM_RCS*ECCM_power/((Math.pow(z*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
	                        		if (noise < tresholdCheck) noise =  tresholdCheck;
	                        		
	                        		if(actor instanceof TypeJammer)
	                        		{
	                        			((TypeJammer)actor).setJammerRWR('I', null, (float) (ECCM_power/((Math.pow(z*2D*3.1415926D*2.5D/360 , 2)*3.1415926D))));
	                        			float[] ghosts;
	                        			if (((TypeJammer)actor).getJammerGhost('I', null) != null)
	                        			{
	                        			ghosts = ((TypeJammer)actor).getJammerGhost('I', null);
	                        			if(ghosts[0] != 0)
	                        			{
	                        				int iG = ghosts.length;
	                        				for(int jG = 1; jG < iG; jG++)
	                        				{
	                        					float size = cvt ((float)(ghosts[0]/noise), 0f, 10f, 0f, 1f);
	                        					if(ghosts[0] > noise) drawContact_RP(y3, ghosts[jG], size, false);
	                        				}
	                        			}
	                        			}
	                        		}
	                        		
	                        		if (radar_return > noise)
	                        			{float size = cvt ((float)(radar_return/noise), 0f, 10f, 0f, 1f);
	                        			if((actor instanceof RocketryRocket)|| (actor instanceof MissileInterceptable)) size *=0.7;
	                        		drawContact_RP(y3,z,size,iff);}
	                        		
	                        		
	                        		
	                        		
	                        	}
	                        		
	                        	
	                        }
	                    }
	                }
		    			


	                //if(noise > 0 && tresholdCheck > 0)
	                //{
	                	
	            	//float intensity = cvt((float)noise, (float)tresholdCheck, (float) tresholdCheck *100, 0.1f, 1f);
	                //drawNoise_RP(rp_23_azimuth, intensity);}
	                
	                //if((((MIG_23)aircraft()).rangeGate_azimuth*30f - 4f < rp_23_azimuth) && (((MIG_23)aircraft()).rangeGate_azimuth*30f + 4f > rp_23_azimuth))
	                
            		float strobe = 0.5f*(((MIG_23)aircraft()).rangeGate_range +1 );
            		switch(((MIG_23)aircraft()).RP_23_range)
                	{
                	case 1:
                		strobe*=30000;
                		break;
                	case 2:
                		strobe*=60000;
                		break;
                	
                	}
            		if(rp_23_azimuth < (((MIG_23)aircraft()).rangeGate_azimuth*30) + 4 && rp_23_azimuth > (((MIG_23)aircraft()).rangeGate_azimuth*30) - 4)
            		drawContact_RP(rp_23_azimuth, strobe, 1f, false);
            		drawContact_RP(rp_23_azimuth, strobe + 9000f, 1f, false);
	                
		    		
		    	switch(rp_23_bar)
		    	{
		    	case 1: 
		    		rp_23_azimuth++;
		    		if (rp_23_azimuth == 31)
		    		{
		    			rp_23_azimuth = 30;
		    			rp_23_bar++;
		    			rp_23_down = true; 
		    		}
		    		break;
		    	case 2:
		    		rp_23_azimuth--;
		    		if (rp_23_azimuth == -31)
		    		{
		    			rp_23_azimuth = -30;
		    			rp_23_bar++; 
		    		}
		    		break;
		    	case 3:
		    		rp_23_azimuth++;
		    		if (rp_23_azimuth == 31)
		    		{
		    			rp_23_azimuth = 30;
		    			rp_23_bar++;
		    		}
		    		break;
		    	case 4:
		    		rp_23_azimuth--;
		    		if (rp_23_azimuth == -31)
		    		{
		    			rp_23_azimuth = -30;
		    			rp_23_bar=1;
		    			rp_23_down = false; 
		    		}
		    		break;
		    	}
	    	}
	    t_rp23 = time;
	    }
    	}
    	catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
    
    
    public double RP_23_noiseCheck(float x2, float y2)
    {
    	double backgroundNoise = 0;
    	Aircraft aircraft = World.getPlayerAircraft();

    	double ECCM_power = ((MIG_23)aircraft()).ECCM_power;

      	int PPS = ((MIG_23)aircraft()).RP_23_range;
      	if(PPS == 0) ECCM_power /=2;
        double ECCM_gate = 16*ECCM_power/((Math.pow((RP_23_range()*1000)*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
    	if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
    	{
    		Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
            Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
            List list = Engine.targets();
            int j = list.size();

            for(int k = 0; k < j; k++)
            {
            	Actor actor = (Actor)list.get(k);
                if((actor instanceof TypeJammer) && actor != World.getPlayerAircraft()|| (((actor instanceof RocketryRocket)) && (!((RocketryRocket)actor).isOnRamp())) || (((actor instanceof MissileInterceptable)) && (((MissileInterceptable)actor).isReleased())))
                {
                	float Jam = ((TypeJammer) actor).getJammerFlood('I', aircraft);
                	if (Jam == 0)continue;
                    Vector3d vector3d = new Vector3d();
                    vector3d.set(point3d);
                    Point3d point3d1 = new Point3d();
                    point3d1.set(actor.pos.getAbsPoint());
                    point3d1.sub(point3d);
                    orient.transformInv(point3d1);
    	if(((Tuple3d) (point3d1)).x > 0)
        {
      	  
      	  
      		  
      		  
    		
      		  double ECM_power = Jam/(4*3.1415926D*Math.pow(((Tuple3d) (point3d1)).x, 2));
      		
      		  if(ECM_power > ECCM_gate)
      		  {
      			  
      			  
                    {
                    	float alpha = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).z/((Tuple3d) (point3d1)).x));
                    	float beta = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).y/((Tuple3d) (point3d1)).x));

                    	float sidelobePower = 1f;
                    	if(alpha > x2 - 1.25f*sidelobePower && alpha < x2 + 1.25f*sidelobePower && beta > y2 - 1.25f*sidelobePower && beta < y2 + 1.25f*sidelobePower)
                    	{
                    		
                    		backgroundNoise += ECM_power;
                    	}
      			  
      			  
      		  };
      		  
      		  
      	  }
        }
                }
            }
    	}
    	if(backgroundNoise > 0 && ECCM_gate > 0)
    	{
        float intensity = cvt((float)Math.log(backgroundNoise), (float) Math.log(ECCM_gate), 2f+(float) Math.log(ECCM_gate), 0.2f, 1f);
		
		drawNoise_RP(rp_23_azimuth, intensity);}
		return backgroundNoise;
    }

    public double RP_23_noiseCheckBroad(float x2, float y2)
    {
    	double backgroundNoise = 0;
    	double ECCM_power = ((MIG_23)aircraft()).ECCM_power;

      	int PPS = ((MIG_23)aircraft()).RP_23_range;
      	if(PPS == 0) ECCM_power /=2;
        double ECCM_gate = 16*ECCM_power/((Math.pow((RP_23_range()*1000)*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
    	Aircraft aircraft = World.getPlayerAircraft();
    	if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
    	{
    		Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
            Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
            List list = Engine.targets();
            int j = list.size();

            for(int k = 0; k < j; k++)
            {
            	Actor actor = (Actor)list.get(k);
                if(actor != World.getPlayerAircraft()|| (((actor instanceof RocketryRocket)) && (!((RocketryRocket)actor).isOnRamp())) || (((actor instanceof MissileInterceptable)) && (((MissileInterceptable)actor).isReleased())))
                {
                	if(!(actor instanceof TypeJammer))continue;
                	float Jam = ((TypeJammer) actor).getJammerSpecial(3,'I', aircraft);
                	
        		    if(Jam == 0)continue;
                    Vector3d vector3d = new Vector3d();
                    vector3d.set(point3d);
                    Point3d point3d1 = new Point3d();
                    point3d1.set(actor.pos.getAbsPoint());
                    point3d1.sub(point3d);
                    orient.transformInv(point3d1);
    	if(((Tuple3d) (point3d1)).x > 0)
        {
      		  
      		  double ECM_power = Jam/(4*3.1415926D*Math.pow(((Tuple3d) (point3d1)).x, 2));
      		
      		  
      		
      		  int sidelobePower;
      		  for (sidelobePower = 0; sidelobePower <= 16 ; sidelobePower++)
      		  {
      			        float alpha = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).z/((Tuple3d) (point3d1)).x));
                    	float beta = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).y/((Tuple3d) (point3d1)).x));
                    	if(alpha > x2 - 1.25f*sidelobePower && alpha < x2 + 1.25f*sidelobePower && beta > y2 - 1.25f*sidelobePower && beta < y2 + 1.25f*sidelobePower)
                    	{
                    		ECM_power/=(double)Math.pow(4, sidelobePower);
                    		if(sidelobePower < 8) backgroundNoise += ECM_power;
                    		break;
                    	}
                    	
      		  
      	      }
      		
        }
                }
            }
    	}
    	
    	if(backgroundNoise > 0 && ECCM_gate > 0)
    	{
        float intensity = cvt((float)Math.log(backgroundNoise), (float) Math.log(ECCM_gate), 2f+(float) Math.log(ECCM_gate), 0.2f, 1f);
        
		drawNoise_RP(rp_23_azimuth, intensity);}
		return backgroundNoise;
    }
    
    
    public float RP_23_range()
    {
    	float radar_range = 90;
    	
    	float RadioAltitude = Landscape.HQ_Air((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).x, (float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).y);
    	  RadioAltitude = fm.getAltitude() - RadioAltitude;
    	  if(RadioAltitude < ((MIG_23)aircraft()).RP_23_model[2])
    		  radar_range = ((MIG_23)aircraft()).RP_23_model[1];
    	  if(RadioAltitude >= ((MIG_23)aircraft()).RP_23_model[2]
    			  &&
    			  RadioAltitude <= ((MIG_23)aircraft()).RP_23_model[3])
    		  
    		  radar_range = ((MIG_23)aircraft()).RP_23_model[1]
    				  +
    				  ( 
    					((MIG_23)aircraft()).RP_23_model[0]-((MIG_23)aircraft()).RP_23_model[1]
    							*(RadioAltitude - ((MIG_23)aircraft()).RP_23_model[2])
    				  )/(((MIG_23)aircraft()).RP_23_model[3]-((MIG_23)aircraft()).RP_23_model[2])
    				  ;
    	  if(RadioAltitude>((MIG_23)aircraft()).RP_23_model[3])
    		  radar_range = ((MIG_23)aircraft()).RP_23_model[0];
    	
    	return radar_range;
    }
    
    
    
    
    public void RP_23_lock(float box_x, float box_y)
    {
    	float antennapitch = 0;
    	switch(rp_23_bar)
    	{
    	case 1:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation + 2f;
    	break;}
    	case 2:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation;
    	break;}
    	case 3:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation - 2f;
    	break;}
    	case 4:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation - 4f;
    	break;}
    	}
    	
    	
    	if(antennapitch >=0)lookdown = false; else lookdown = true;
    	
    	if(!((MIG_23)aircraft()).BSV_SC)
    	{
    	if(fm.getAltitude() > 1500f)
    		{
    		if(antennapitch >=0)
    		{
    			((MIG_23)aircraft()).BSV_mode = 0;
    			((MIG_23)aircraft()).RP_23_range = 2;
    			
    		}
    			else
    			{

        			((MIG_23)aircraft()).BSV_mode = 1;
        			((MIG_23)aircraft()).RP_23_range = 2;
        			
    			}
    		}
    	if(fm.getAltitude() <= 1500f)
    		{
    		if(antennapitch >=0)
    		{
    			((MIG_23)aircraft()).BSV_mode = 2;
    			((MIG_23)aircraft()).RP_23_range = 1;
    			
    		}
    			else
    			{

        			((MIG_23)aircraft()).BSV_mode = 3;
        			((MIG_23)aircraft()).RP_23_range = 1;
        			
    			}
    		}
    	}
    	else
    	{
    		((MIG_23)aircraft()).BSV_mode = 4;
    	}
    	
    	
    	
    	try
    	{
    	long time = Time.current();
	    if (time > t_rp23 + 10)
	    {
	    	float rp_23_pitch = 0;
	    	switch(rp_23_bar)
	    	{
	    	case 1:
	    	{rp_23_pitch = ((MIG_23)aircraft()).RP_23_elevation + 3f;
	    	break;}
	    	case 2:
	    	{rp_23_pitch = ((MIG_23)aircraft()).RP_23_elevation + 0.875f;
	    	break;}
	    	case 3:
	    	{rp_23_pitch = ((MIG_23)aircraft()).RP_23_elevation - 0.875f;
	    	break;}
	    	case 4:
	    	{rp_23_pitch = ((MIG_23)aircraft()).RP_23_elevation - 3f;
	    	break;}
	    	}
	    	
	    	float K = ((FlightModelMain) (super.fm)).Or.getKren();
	    	float T = ((FlightModelMain) (super.fm)).Or.getTangage();
	    	if (T > 35)T=35;
	    	if (T < -30)T=-30;
	    	if (K>72) K=72;
	    	if (K<-72) K=-72;
	    	float alpha1 = (float) (Math.cos(Math.toRadians(K))*rp_23_pitch);
	    	float alpha2 = (float) (Math.sin(Math.toRadians(K))*rp_23_pitch);
	    	float beta1 = (float) (Math.sin(Math.toRadians(K))*rp_23_azimuth);
	    	float beta2 = (float) (Math.cos(Math.toRadians(K))*rp_23_azimuth);
	    	
	    	float x = -T+alpha1+beta1;
	    	float y = +alpha2-beta2;
	    	
	    	float strobe = 0.5f*(((MIG_23)aircraft()).rangeGate_range +1 );
    		switch(((MIG_23)aircraft()).RP_23_range)
        	{
        	case 1:
        		strobe*=30000;
        		break;
        	case 2:
        		strobe*=60000;
        		break;
        	
        	}
    		
    		float lockdistance_Max = strobe+9000;
    		float lockdistance_Min = strobe;
    		Actor preliminarytarget = null;

	    	
	    	double noise = RP_23_noiseCheck(x,y);
	    	noise += RP_23_noiseCheckBroad(x,y);
	    	RP_23_noise = (float)noise;
	    	double ECCM_power = ((MIG_23)aircraft()).ECCM_power;
	      	double ECCM_RCS = 16;
	      	int PPS = ((MIG_23)aircraft()).RP_23_range;
	      	if(PPS == 0) ECCM_power /=2;
	      	double tresholdCheck =  16*ECCM_power/((Math.pow((RP_23_range()*1000)*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
	    	Aircraft aircraft = World.getPlayerAircraft();
		    	if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
		    	{
		    		Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
	                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
	                List list = Engine.targets();
	                int j = list.size();
		    		
	                for(int k = 0; k < j; k++)
	                {
	                	Actor actor = (Actor)list.get(k);
	                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft()|| (((actor instanceof RocketryRocket)) && (!((RocketryRocket)actor).isOnRamp())) || (((actor instanceof MissileInterceptable)) && (((MissileInterceptable)actor).isReleased())))
	                    {
	                    	if(actor instanceof TypeJammer)ECCM_RCS = ((TypeJammer)actor).getJammerRCS(aircraft);
	                        Vector3d vector3d = new Vector3d();
	                        vector3d.set(point3d);
	                        Point3d point3d1 = new Point3d();
	                        point3d1.set(actor.pos.getAbsPoint());
	                        point3d1.sub(point3d);
	                        orient.transformInv(point3d1);
	                          float radar_range = RP_23_range();
	                          float aspect = GuidedMissileUtils.angleBetween(actor, aircraft);
	                          
	                          
	                          
	                          if(actor instanceof Aircraft && !(actor instanceof TypeHelicopter))
	                          {
	                          float velocity = ((Aircraft)actor).FM.getSpeed();
	                          velocity *= (float)Math.cos(Math.toRadians(aspect));
	                          if(PPS == 0)velocity = Math.abs(velocity);
	                          if(PPS == -1 || ((MIG_23)aircraft()).BSV_mode == 3)velocity *= -1;
	                          if(velocity < 60 || velocity > 700) continue;
	                          }
	                          else
	                        	  if(actor instanceof TypeHelicopter)
	                        	  {
	                        		  float velocity = ((Aircraft)actor).FM.getSpeed();
	    	                          velocity *= (float)Math.cos(Math.toRadians(aspect));
	    	                          velocity = Math.abs(velocity);
	    	                          if(velocity < 60 || velocity > 700) ECCM_RCS /=2 ;
	                        	  }
	                        	  else
	                        	  {
	                          
	              	    		switch (((MIG_23)aircraft()).BSV_mode)
	              	    		{
	              	    		case 0:
	              	    			break;
	              	    		case 1:
	              	    			if(aspect > 70 && aspect < 110) continue;
	              	    			break;
	              	    		case 2:
	              	    			if(aspect > 70 && aspect < 110) continue;
	              	    			break;
	              	    		case 3:
	              	    			if(aspect < 110) continue;
	              	    			break;
	              	    		case 4:
	              	    			if(aspect > 70 && aspect < 110) continue;
	              	    			break;
	              	    			default:
	              	    				break;
	              	    		}
	              	    		}
	                          
	                        if( ((Tuple3d) (point3d1)).x > 300 && ((Tuple3d) (point3d1)).x < (radar_range)*1000)
	                        {
	                        	float alpha = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).z/((Tuple3d) (point3d1)).x));
	                        	float beta = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).y/((Tuple3d) (point3d1)).x));
	                        	
	                	    	
	                	    	
	                        	float z = (float) Math.sqrt(
	                        		     ((Tuple3d) (point3d1)).x*((Tuple3d) (point3d1)).x
	                        			+((Tuple3d) (point3d1)).y*((Tuple3d) (point3d1)).y
	                        			+((Tuple3d) (point3d1)).z*((Tuple3d) (point3d1)).z);
	                        		
	                        	if(alpha > x - 1.25f && alpha < x + 1.25f && beta > y - 1.25f && beta < y + 1.25)
	                        	{
	                        		
	                    	    	float alpha22 = (float) (Math.sin(Math.toRadians(K))*alpha);
	                    	    	
	                    	    	float beta22 = (float) (Math.cos(Math.toRadians(K))*beta);
	                        		
		                	    	float y3 = +alpha22-beta22;
	                        		boolean iff = false;
	                        		iff = actor.getArmy() == World.getPlayerArmy();
	                        		
	                        		double radar_return = ECCM_RCS*ECCM_power/((Math.pow(z*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
	                        		if (noise < tresholdCheck) noise =  tresholdCheck;
	                        		
	                        		if(actor instanceof TypeJammer)
	                        		{
	                        			((TypeJammer)actor).setJammerRWR('I', null, (float) (ECCM_power/((Math.pow(z*2D*3.1415926D*2.5D/360 , 2)*3.1415926D))));
	                        			float[] ghosts;
	                        			if (((TypeJammer)actor).getJammerGhost('I', null) != null)
	                        			{
	                        			ghosts = ((TypeJammer)actor).getJammerGhost('I', null);
	                        			if(ghosts[0] != 0)
	                        			{
	                        				int iG = ghosts.length;
	                        				for(int jG = 1; jG < iG; jG++)
	                        				{
	                        					float size = cvt ((float)(ghosts[0]/noise), 0f, 10f, 0f, 1f);
	                        					if(ghosts[0] > noise)
	                        					{
	                        						drawContact_RP(y3, ghosts[jG], size, false);
	                        					
	                        					
	                        						if(ghosts[jG] < lockdistance_Max && ghosts[jG] > lockdistance_Min) { lockdistance_Max = ghosts[jG]; ((MIG_23)aircraft()).RP_lock = false;}
	                        					}
	                        					
	                        					}
	                        			}
	                        			}
	                        		}
	                        		
	                        		if (radar_return > noise)
	                        			{float size = cvt ((float)(radar_return/noise), 0f, 10f, 0f, 1f);
	                        			if((actor instanceof RocketryRocket)|| (actor instanceof MissileInterceptable)) size *=0.7;
	                        			
	                        			drawContact_RP(y3,z,size,iff);
	                        			if(z<lockdistance_Max && z > lockdistance_Min){lockdistance_Max = z; preliminarytarget = actor; ((MIG_23)aircraft()).RP_lock = false;}
	                        			
	                        			}
	                        		
	                        		
	                        		
	                        		
	                        	}
	                        		
	                        	
	                        }
	                    }
	                }
		    			


	                //if(noise > 0 && tresholdCheck > 0)
	                //{
	                	
	            	//float intensity = cvt((float)noise, (float)tresholdCheck, (float) tresholdCheck *100, 0.1f, 1f);
	                //drawNoise_RP(rp_23_azimuth, intensity);}
	                
	                //if((((MIG_23)aircraft()).rangeGate_azimuth*30f - 4f < rp_23_azimuth) && (((MIG_23)aircraft()).rangeGate_azimuth*30f + 4f > rp_23_azimuth))
	                
	                if(preliminarytarget != null)
	                {
	                ((MIG_23)aircraft()).victim_RP = preliminarytarget;
            		((MIG_23)aircraft()).radarmode = 100;
            		((MIG_23)aircraft()).RP_23_range = 1;
            		return;
	                }
            		if(rp_23_azimuth < (((MIG_23)aircraft()).rangeGate_azimuth*30) + 4 && rp_23_azimuth > (((MIG_23)aircraft()).rangeGate_azimuth*30) - 4)
            		drawContact_RP(rp_23_azimuth, strobe, 1f, false);
            		drawContact_RP(rp_23_azimuth, strobe + 9000f, 1f, false);
	                
		    		
		    	switch(rp_23_bar)
		    	{
		    	case 1: 
		    		rp_23_azimuth++;
		    		if (rp_23_azimuth >= (((MIG_23)aircraft()).rangeGate_azimuth*30) + 4)
		    		{
		    			rp_23_azimuth = (int) ((((MIG_23)aircraft()).rangeGate_azimuth*30) + 4);
		    			rp_23_bar++;
		    			rp_23_down = true; 
		    		}
		    		break;
		    	case 2:
		    		rp_23_azimuth--;
		    		if (rp_23_azimuth <= (((MIG_23)aircraft()).rangeGate_azimuth*30) - 4)
		    		{
		    			rp_23_azimuth = (int) ((((MIG_23)aircraft()).rangeGate_azimuth*30) - 4);
		    			rp_23_bar++; 
		    		}
		    		break;
		    	case 3:
		    		rp_23_azimuth++;
		    		if (rp_23_azimuth >= (((MIG_23)aircraft()).rangeGate_azimuth*30) + 4)
		    		{
		    			rp_23_azimuth = (int) ((((MIG_23)aircraft()).rangeGate_azimuth*30) + 4);
		    			rp_23_bar++;
		    		}
		    		break;
		    	case 4:
		    		rp_23_azimuth--;
		    		if (rp_23_azimuth <= (((MIG_23)aircraft()).rangeGate_azimuth*30) - 4)
		    		{
		    			rp_23_azimuth = (int) ((((MIG_23)aircraft()).rangeGate_azimuth*30) - 4);
		    			rp_23_bar=1;
		    			rp_23_down = false; 
		    		}
		    		break;
		    	}
	    	}
	    t_rp23 = time;
	    }
    	}
    	catch(Exception exception)
        {
            exception.printStackTrace();
        }
    	/*
    	box_x ++;
    	box_x *=0.5;
    	switch(((MIG_23)aircraft()).RP_23_range)
    	{
    	case 1:
    		box_x*=30000;
    		break;
    	case 2:
    		box_x*=60000;
    		break;
    		default:
    			return;
    	}
    	
    	box_y *=30f;

    	float antennapitch = 0;
    	switch(rp_23_bar)
    	{
    	case 1:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation + 2f;
    	break;}
    	case 2:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation;
    	break;}
    	case 3:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation - 2f;
    	break;}
    	case 4:
    	{antennapitch = ((MIG_23)aircraft()).RP_23_elevation - 4f;
    	break;}
    	}
    	
    	if(!((MIG_23)aircraft()).BSV_SC)
    	{
    	if(fm.getAltitude() > 1500f)
    		{
    		if(antennapitch >=0)
    		{
    			((MIG_23)aircraft()).BSV_mode = 0;
    			((MIG_23)aircraft()).RP_23_range = 2;
    		}
    			else
    			{

        			((MIG_23)aircraft()).BSV_mode = 1;
        			((MIG_23)aircraft()).RP_23_range = 2;
    			}
    		}
    	if(fm.getAltitude() <= 1500f)
    		{
    		if(antennapitch >=0)
    		{
    			((MIG_23)aircraft()).BSV_mode = 2;
    			((MIG_23)aircraft()).RP_23_range = 1;
    		}
    			else
    			{

        			((MIG_23)aircraft()).BSV_mode = 3;
        			((MIG_23)aircraft()).RP_23_range = 1;
    			}
    		}
    	}
    	else
    	{
    		((MIG_23)aircraft()).BSV_mode = 4;
    	}
    	
    	
    	
    	try
    	
	    {

    		double noise = (double)RP_23_noise;
	    	double ECCM_power = ((MIG_23)aircraft()).ECCM_power;
	      	double ECCM_RCS = 16;
	      	int PPS = ((MIG_23)aircraft()).RP_23_range;
	      	if(PPS == 0) ECCM_power /=2;
	      	double tresholdCheck =  16*ECCM_power/((Math.pow((RP_23_range()*1000)*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
	    	
	    	Aircraft aircraft = World.getPlayerAircraft();
		    	if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
		    	{
		    		Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
	                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
	                List list = Engine.targets();
	                int j = list.size();
		    		float z_min = -1;
	                for(int k = 0; k < j; k++)
	                {
	                	Actor actor = (Actor)list.get(k);
	                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft()|| (((actor instanceof RocketryRocket)) && (!((RocketryRocket)actor).isOnRamp())) || (((actor instanceof MissileInterceptable)) && (((MissileInterceptable)actor).isReleased())))
	                    {
	                        Vector3d vector3d = new Vector3d();
	                        vector3d.set(point3d);
	                        Point3d point3d1 = new Point3d();
	                        point3d1.set(actor.pos.getAbsPoint());
	                        point3d1.sub(point3d);
	                        orient.transformInv(point3d1);
	                          float radar_range = RP_23_range();
	                          float aspect = GuidedMissileUtils.angleBetween(actor, aircraft);
	                          
	                          
	                          if(actor instanceof Aircraft && !(actor instanceof TypeHelicopter))
	                          {
	                          float velocity = ((Aircraft)actor).FM.getSpeed();
	                          velocity *= (float)Math.cos(Math.toRadians(aspect));
	                          if(PPS == 0)velocity = Math.abs(velocity);
	                          if(PPS == -1 || ((MIG_23)aircraft()).BSV_mode == 3)velocity *= -1;
	                          if(velocity < 60 || velocity > 700) continue;
	                          }
	                          else
	                        	  if(actor instanceof TypeHelicopter)
	                        	  {
	                        		  float velocity = ((Aircraft)actor).FM.getSpeed();
	    	                          velocity *= (float)Math.cos(Math.toRadians(aspect));
	    	                          velocity = Math.abs(velocity);
	    	                          if(velocity < 60 || velocity > 700) ECCM_RCS /=2 ;
	                        	  }
	                        	  else
	                        	  {
	                          
	              	    		switch (((MIG_23)aircraft()).BSV_mode)
	              	    		{
	              	    		case 0:
	              	    			break;
	              	    		case 1:
	              	    			if(aspect > 70 && aspect < 110) continue;
	              	    			break;
	              	    		case 2:
	              	    			if(aspect > 70 && aspect < 110) continue;
	              	    			break;
	              	    		case 3:
	              	    			if(aspect < 110) continue;
	              	    			break;
	              	    		case 4:
	              	    			if(aspect > 70 && aspect < 110) continue;
	              	    			break;
	              	    			default:
	              	    				break;
	              	    		}
	              	    		}
	                          
	                        if( ((Tuple3d) (point3d1)).x > 300 && ((Tuple3d) (point3d1)).x < (radar_range)*1000)
	                        {
	                        	float alpha = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).z/((Tuple3d) (point3d1)).x));
	                        	float beta = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).y/((Tuple3d) (point3d1)).x));
	                        	float K = ((FlightModelMain) (super.fm)).Or.getKren();
	                        	float T = ((FlightModelMain) (super.fm)).Or.getTangage();
	                        	if (T > 35)T=35;
	                        	if (T < -30)T=-30;
	                        	if (K>72) K=72;
	                        	if (K<-72) K=-72;
	                        	float alpha1 = (float) (Math.cos(Math.toRadians(K))*alpha);
	                        	float alpha2 = (float) (Math.sin(Math.toRadians(K))*alpha);
	                        	float beta1 = (float) (Math.sin(Math.toRadians(K))*beta);
	                        	float beta2 = (float) (Math.cos(Math.toRadians(K))*beta);
	                        	
	                        	float x = T+alpha1+beta1;
	                        	float y = +alpha2-beta2;
	                        	float z = (float) Math.sqrt(
	                        		     ((Tuple3d) (point3d1)).x*((Tuple3d) (point3d1)).x
	                        			+((Tuple3d) (point3d1)).y*((Tuple3d) (point3d1)).y
	                        			+((Tuple3d) (point3d1)).z*((Tuple3d) (point3d1)).z);
	                        		
	                        	
	                        	if(y > box_y -4f && y < box_y +4f)
	                        	{
	                        		float radar_elevation = ((MIG_23)aircraft()).RP_23_elevation; 
	                        		
	                        			if(x < 4.25f + radar_elevation && x > -4.25f + radar_elevation)
	                        			{
	                        			}
	                        			else
	                        				
	                        				continue;
	                        		
	                        		
	                        		if(z > box_x && z < box_x+9000)
	                        		{
	                        		if(actor.getArmy() != World.getPlayerArmy())
	                        		{
	                        			double radar_return = ECCM_RCS*ECCM_power/((Math.pow(z*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
		                        		if (noise < tresholdCheck) noise =  tresholdCheck;
	                        			if((z_min == -1 || z < z_min)&&radar_return > noise)
	                        			{
	                        		((MIG_23)aircraft()).victim_RP = actor;
	                        		((MIG_23)aircraft()).radarmode = 100;
	                        		((MIG_23)aircraft()).RP_23_range = 1;
	                        		z_min = z;
	                        			}
	                        		
	                        		}
	                        		}
	                        		
	                        		
	                        		
	                        	}
	                        		
	                        	
	                        }
	                    }
	                }
		    			
		    	}
	    	
    	}
    	catch(Exception exception)
        {
            exception.printStackTrace();
        }
    	((MIG_23)aircraft()).RP_lock = false;
    	
    	
    	*/
    	}
    		
    	
    public void RP_23_track()
    {

    	boolean TP_support = (track_elevation <= 3 && track_elevation >= -12)&&((MIG_23)aircraft()).radarmode == 101 ;
    	double noise = 0;
    	double ECCM_power = ((MIG_23)aircraft()).ECCM_power;
      	double ECCM_RCS = 16;
      	int PPS = ((MIG_23)aircraft()).RP_23_range;
      	if(PPS == 0) ECCM_power /=2;
      	double tresholdCheck =  16*ECCM_power/((Math.pow((RP_23_range()*1000)*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
      	
    	Aircraft aircraft = World.getPlayerAircraft();
    	if(Actor.isValid(aircraft))
    	{
    	Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
        Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
    	Actor victim = ((MIG_23)aircraft()).victim_RP;
    	Vector3d vector3d = new Vector3d();
        vector3d.set(point3d);
        Point3d point3d1 = new Point3d();
        point3d1.set(victim.pos.getAbsPoint());
        point3d1.sub(point3d);
        orient.transformInv(point3d1);
          float radar_range = RP_23_range()*1000f;
          float aspect = GuidedMissileUtils.angleBetween(victim, aircraft);
          if(victim instanceof Aircraft && !(victim instanceof TypeHelicopter))
          {
          float velocity = ((Aircraft)victim).FM.getSpeed();
          velocity *= (float)Math.cos(Math.toRadians(aspect));
          velocity = Math.abs(velocity);
          if((velocity < 60 || velocity > 700)&&!(TP_support)) breakLock();
          }
          else
        	  if(victim instanceof TypeHelicopter)
        	  {
        		  float velocity = ((Aircraft)victim).FM.getSpeed();
                  velocity *= (float)Math.cos(Math.toRadians(aspect));
                  velocity = Math.abs(velocity);
                  if(velocity < 60 || velocity > 700) ECCM_RCS /=2 ;
        	  }
        	  else
        	  {
	    			if((aspect > 70 && aspect < 110)&&!(TP_support)) breakLock();
	    		}
          if(Actor.isValid(victim) &&  ((Tuple3d) (point3d1)).x > 200 && ((Tuple3d) (point3d1)).x < radar_range)
          {
        	  track_elevation = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).z/((Tuple3d) (point3d1)).x));
        	  track_azimuth = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).y/((Tuple3d) (point3d1)).x));
        	  if(track_azimuth > -52f && track_azimuth < 52f && track_elevation > -30f && track_elevation < 45)
        	  {
        		  
        		  
          		
          		
        		  ((MIG_23)aircraft()).Myotka_x = track_elevation;
        		  ((MIG_23)aircraft()).Myotka_y = -track_azimuth;
        		  track_azimuth = track_azimuth/52;
        		  track_range = (float)Math.sqrt((float)((Tuple3d) (point3d1)).x*(float)((Tuple3d) (point3d1)).x+(float)((Tuple3d) (point3d1)).z*(float)((Tuple3d) (point3d1)).z+(float)((Tuple3d) (point3d1)).y*(float)((Tuple3d) (point3d1)).y);
        		  double radar_return = ECCM_RCS*ECCM_power/((Math.pow(track_range*2D*3.1415926D*2.5D/360 , 4)*3.1415926D));
            		if (noise < tresholdCheck) noise =  tresholdCheck;
        		  if(track_range < 3000) 
        		    	((MIG_23)aircraft()).RP_23_range = 0;
        		  else

        		    	((MIG_23)aircraft()).RP_23_range = 1;
        		  if(radar_return > noise)
        		  return;
        	  }
          }
    	}
    	breakLock();
    		return;
    	
    	
    }
public void breakLock()
{

	((MIG_23)aircraft()).victim_RP = null;
    super.mesh.chunkVisible("Z_Z_SteeringDot", false);
	super.mesh.chunkVisible("Z_Z_TargetCircle_big", false);
    super.mesh.chunkVisible("Z_Z_mode_A", false);
    ((MIG_23)aircraft()).Myotka_x = 0;
    ((MIG_23)aircraft()).Myotka_y = 0;
    super.mesh.chunkVisible("Z_Z_mode_TP", false);
    super.mesh.chunkVisible("Z_Z_range_3", false);
    if(((MIG_23)aircraft()).RP_23_range == 0)  ((MIG_23)aircraft()).RP_23_range = 1;
    super.mesh.chunkVisible("Z_Z_range_30", false);
    super.mesh.chunkVisible("Z_Z_range_60", false);
    super.mesh.chunkVisible("Z_Z_range_120", false);

    super.mesh.chunkVisible("Z_Z_Range_Dynamic", false);
    super.mesh.chunkVisible("Z_Z_MissileEnvelope1", false);
    super.mesh.chunkVisible("Z_Z_MissileEnvelope2", false);
	((MIG_23)aircraft()).radarmode = 11;  /**Radar has to reset to mode accessible by all variants.*/
    
}
    public void AG_Bomb()
    {
    	double radioalt = ((MIG_23)aircraft()).directionalDistanceToGround(8000);
    	radioalt *= Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	radioalt = Math.abs(radioalt);
    	double v0z = ( ((FlightModelMain) (fm)).getSpeed() - 25 )* Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	double v0x = ( ((FlightModelMain) (fm)).getSpeed() - 25 )* Math.cos(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	double t_H = (-v0z - (Math.sqrt(v0z*v0z+2*9.81*radioalt)))/ -9.81;
    	double d_Projectile = v0x * t_H;
    	double Beta = 0;
    	if(radioalt > 0)
    		Beta = -90+Math.toDegrees(Math.atan(d_Projectile/radioalt));
    	
    	float f6 = (float) Beta  - (((FlightModelMain) (fm)).Or.getTangage());
    	float f5 = f6* (float)Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getKren()));
    	f6 = f6* (float)Math.cos(Math.toRadians(((FlightModelMain) (fm)).Or.getKren()));
    	
    	track_elevation = f6;
    	track_azimuth = f5;
    	((MIG_23)aircraft()).RP_23_range = 0;
    	track_range = (float)radioalt;
    	
    }
    public void AG_S5()
    {
    	double radioalt = ((MIG_23)aircraft()).directionalDistanceToGround(8000);
    	radioalt *= Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	radioalt = Math.abs(radioalt);
    	double v0z = ( ((FlightModelMain) (fm)).getSpeed() + 350 )* Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	double v0x = ( ((FlightModelMain) (fm)).getSpeed() + 350 )* Math.cos(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	double t_H = (-v0z - (Math.sqrt(v0z*v0z+2*9.81*radioalt)))/ -9.81;
    	double d_Projectile = v0x * t_H;
    	double Beta = 0;
    	if(radioalt > 0)
    		Beta = -90+Math.toDegrees(Math.atan(d_Projectile/radioalt));
    	
    	float f6 = (float) Beta - (((FlightModelMain) (fm)).Or.getTangage());
    	float f5 = f6* (float)Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getKren()));
    	f6 = f6* (float)Math.cos(Math.toRadians(((FlightModelMain) (fm)).Or.getKren()));
    	f6 = f6 -(float)Math.toDegrees(Math.abs(Math.atan(1.9/((MIG_23)aircraft()).directionalDistanceToGround(3000))));
    	track_elevation = f6;
    	track_azimuth = f5;
    	((MIG_23)aircraft()).RP_23_range = 0;
    	track_range = (float)((MIG_23)aircraft()).directionalDistanceToGround(8000);
    }
    public void AG_S24()
    {
    	double radioalt = ((MIG_23)aircraft()).directionalDistanceToGround(8000);
    	radioalt *= Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	radioalt = Math.abs(radioalt);
    	double v0z = ( ((FlightModelMain) (fm)).getSpeed() + 350 )* Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	double v0x = ( ((FlightModelMain) (fm)).getSpeed() + 350 )* Math.cos(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	double t_H = (-v0z - (Math.sqrt(v0z*v0z+2*9.81*radioalt)))/ -9.81;
    	double d_Projectile = v0x * t_H;
    	double Beta = 0;
    	if(radioalt > 0)
    		Beta = -90+Math.toDegrees(Math.atan(d_Projectile/radioalt));
    	
    	float f6 = (float) Beta - (((FlightModelMain) (fm)).Or.getTangage());
    	float f5 = f6* (float)Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getKren()));
    	f6 = f6* (float)Math.cos(Math.toRadians(((FlightModelMain) (fm)).Or.getKren()));
    	f6 = f6 -(float)Math.toDegrees(Math.abs(Math.atan(1.9/((MIG_23)aircraft()).directionalDistanceToGround(3000))));
    	track_elevation = f6;
    	track_azimuth = f5;
    	((MIG_23)aircraft()).RP_23_range = 0;
    	track_range = (float)((MIG_23)aircraft()).directionalDistanceToGround(3000);
    }
    public void AG_gun()
    {
    	double radioalt = ((MIG_23)aircraft()).directionalDistanceToGround(8000);
    	radioalt *= Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	radioalt = Math.abs(radioalt);
    	double v0z = ( ((FlightModelMain) (fm)).getSpeed() + 700 )* Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	double v0x = ( ((FlightModelMain) (fm)).getSpeed() + 700 )* Math.cos(Math.toRadians(((FlightModelMain) (fm)).Or.getTangage()));
    	double t_H = (-v0z - (Math.sqrt(v0z*v0z+2*9.81*radioalt)))/ -9.81;
    	double d_Projectile = v0x * t_H;
    	double Beta = 0;
    	if(radioalt > 0)
    		Beta = -90+Math.toDegrees(Math.atan(d_Projectile/radioalt));
    	
    	float f6 = (float) Beta - (((FlightModelMain) (fm)).Or.getTangage());
    	float f5 = f6* (float)Math.sin(Math.toRadians(((FlightModelMain) (fm)).Or.getKren()));
    	f6 = f6* (float)Math.cos(Math.toRadians(((FlightModelMain) (fm)).Or.getKren()));
    	f6 = f6 -(float)Math.toDegrees(Math.abs(Math.atan(1.9/((MIG_23)aircraft()).directionalDistanceToGround(3000))));
    	track_elevation = f6;
    	track_azimuth = f5;
    	((MIG_23)aircraft()).RP_23_range = 0;
    	track_range = (float)((MIG_23)aircraft()).directionalDistanceToGround(3000);
    }

    public void reflectCockpitState()
    {
    	/*
        super.mesh.chunkVisible("Z_Z_RETICLE1", false);
        for(int i = 1; i < 11; i++)
            super.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);
            */

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

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        super.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        super.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        super.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        super.mesh.chunkVisible("WingLIn_D3", hiermesh.isChunkVisible("WingLIn_D3"));
        super.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        super.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        super.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        super.mesh.chunkVisible("WingRIn_D3", hiermesh.isChunkVisible("WingRIn_D3"));
        super.mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        super.mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        super.mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        super.mesh.chunkVisible("WingLMid_D3", hiermesh.isChunkVisible("WingLMid_D3"));
        super.mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        super.mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        super.mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        super.mesh.chunkVisible("WingRMid_D3", hiermesh.isChunkVisible("WingRMid_D3"));
        super.mesh.chunkVisible("Flap01_D0", hiermesh.isChunkVisible("Flap01_D0"));
        super.mesh.chunkVisible("Flap02_D0", hiermesh.isChunkVisible("Flap02_D0"));
        super.mesh.chunkVisible("Flap03_D0", hiermesh.isChunkVisible("Flap03_D0"));
        super.mesh.chunkVisible("Flap04_D0", hiermesh.isChunkVisible("Flap04_D0"));
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

    public float angleBetween(Actor actor, Vector3f vector3f)
    {
        return angleBetween(actor, new Vector3d(vector3f));
    }

    public float angleBetween(Actor actor, Vector3d vector3d)
    {
        Vector3d vector3d1 = new Vector3d();
        vector3d1.set(vector3d);
        double d = 0.0D;
        Loc loc = new Loc();
        Point3d point3d = new Point3d();
        Vector3d vector3d2 = new Vector3d();
        actor.pos.getAbs(loc);
        loc.get(point3d);
        d = vector3d1.length();
        vector3d1.scale(1.0D / d);
        vector3d2.set(1.0D, 0.0D, 0.0D);
        loc.transform(vector3d2);
        d = vector3d2.dot(vector3d1);
        return Geom.RAD2DEG((float)Math.acos(d));
    }

    public float angleActorBetween(Actor actor, Actor actor1)
    {
        float f = 180.1F;
        double d = 0.0D;
        Loc loc = new Loc();
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        Vector3d vector3d = new Vector3d();
        Vector3d vector3d1 = new Vector3d();
        actor.pos.getAbs(loc);
        loc.get(point3d);
        actor1.pos.getAbs(point3d1);
        vector3d.sub(point3d1, point3d);
        d = vector3d.length();
        vector3d.scale(1.0D / d);
        vector3d1.set(1.0D, 0.0D, 0.0D);
        loc.transform(vector3d1);
        d = vector3d1.dot(vector3d);
        f = Geom.RAD2DEG((float)Math.acos(d));
        return f;
    }

    private boolean inRangeLight;
    private boolean inRangeWarn;
    private boolean blindLanding;
    private boolean beacon;
    private boolean waypointNAV;
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
        0.0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F
    };
    private static final float k14TargetWingspanScale[] = {
        11F, 11.3F, 11.8F, 15F, 20F, 25F, 30F, 35F, 40F, 43.05F
    };
    private static final float k14BulletDrop[] = {
    	0.453F, 0.587F, 0.743F, 0.921F, 1.122F, 1.350F, 1.604F, 1.887F, 2.201F, 2.547F,
    	2.927F, 3.344F, 3.799F, 4.295F, 4.834F, 5.419F, 6.052F, 6.736F, 7.474F, 8.269F,
    	9.124F, 10.044F, 11.030F, 12.088F, 13.221F, 14.432F, 15.728F, 17.111F, 18.588F,
    	20.162F, 21.840F, 23.627F, 25.528F, 27.550F, 29.700F, 31.984F, 34.410F, 36.984F,
    	39.715F, 42.611F, 45.680F, 48.932F, 52.377F, 56.024F, 59.884F, 63.968F, 68.288F,
    	72.855F, 77.683F, 82.784F, 88.174F, 93.867F, 99.878F, 106.224F
        /*5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 
        8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 
        10.603F, 10.704F, 10.739F, 10.782F, 10.789F, 10.789F, 10.789F, 10.789F, 10.789F, 10.789F,
        10.789F, 10.789F, 10.789F, 10.789F, 10.789F, 10.789F, 10.789F, 10.789F, 10.789F, 10.789F*/
    };
    public void setRGBA(int x, int y, int R, int G, int B, int A)
    {
        int l = (y * imgW + x) * 4;
        buf[l + 0] = (byte)(R & 0xff);
        buf[l + 1] = (byte)(G & 0xff);
        buf[l + 1] = (byte)(B & 0xff);
        buf[l + 3] = (byte)(A & 0xff);
        bufEmpty = false;
        bChanged = true;
    }

    public void setA (int x, int y, int A)
	{
		int I = (y*imgW+x)*4;
		buf[I+3] = (byte)(A&0xff);
		bufEmpty = false;
		bChanged = true;
	}
	

	
	public void prepareBuf (int colour, int transparency)
	{
		for(int y=0; y<imgH; y++)
		{
			for(int x=0; x<imgW; x++)
			{
				int I = (y*imgW + x)*4;
				buf[I+0] = (byte)(colour);
				buf[I+1] = (byte)(colour);
				buf[I+2] = (byte)(colour);
				buf[I+3] = (byte)(transparency);
				
			}
		}
	}
	public void fadeBuf ()
	/**
	 * Fades current the CRT picture;
	 * method runs through the entire array decreasing each alpha field by 10 to get to 0.
	 */
	{
		for(int y=0; y<imgH; y++)
		{
			for(int x=0; x<imgW; x++) //set of cycles to cycle through all pixels
			{
				int I = (y*imgW + x)*4; //colour+alpha values of single pixel are treated as 4 successive numbers in an array
				int B = (buf[I+3] & 0xFF); //load original alpha value of the pixel
				if(B>0) //when alpha is not 0
				{
					B -=15; //decrease alpha by 10
					if (B<0) B=0;

					
				buf[I+3] = (byte)(B & 0xff); //the new alpha value is applied here 
				bufEmpty = false; //meaningless condition, but i kept it for consistency with rest of the code
				bChanged = true; //condition for texture being updated in game
				}				
			}
		}
	}
	
	public void drawContact_TP(float alpha, float beta, float size)
	{
		
		if(alpha > -30 && alpha < 30 && beta < 3 && beta > -12)
		{

			
			alpha = alpha/30;
			if(beta > 0) beta = beta /3; else beta = beta /12;
			alpha *= ((imgW/2)-5);
			beta *= -((imgH/2)-5);
			alpha += ((imgW/2)-1);
			beta += ((imgH/2)-1);


			if (size > 1) size = 1;
			if (size < 0) size = 0;
			
			size *=20;
			alpha = Math.round(alpha);
			beta = Math.round(beta);
			size = Math.round(size);
			

			beta -= size;
			
			for(int length = 0; length <= size*2; length++)
			{
				try
				{
				int I = (int)((beta+length)*imgW+ alpha )*4;
				if(I+3 > buf.length) break;
				
				if((alpha+length >= 0 && alpha + length < imgW)&&(beta > 0 && beta < imgH))
				{
					
				int A = 100;
				buf[I+3] = (byte)(A & 0xff);
					
				}
				else
				{
					continue;}
				
				
				I = (int)((beta+length)*imgW+ (alpha-1) )*4;
				if((alpha+length >= 0 && alpha + length < imgW)&&(beta-1 > 0 && beta-1 < imgH))
				{
				int A = 75 + buf[I+3];
				if (A>=100)A = 100;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				I = (int)((beta+length)*imgW+ (alpha+1) )*4;
				if((alpha+length >= 0 && alpha + length < imgW)&&(beta+1 > 0 && beta+1 < imgH))
				{
				int A = 75 + buf[I+3];
				if (A>=100)A = 100;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				
				I = (int)((beta+length)*imgW+ (alpha-2) )*4;
				if((alpha+length >= 0 && alpha + length < imgW)&&(beta-1 > 0 && beta-1 < imgH))
				{
				int A = 25 + buf[I+3];
				if (A>=100)A = 100;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				I = (int)((beta+length)*imgW+ (alpha+2) )*4;
				if((alpha+length >= 0 && alpha + length < imgW)&&(beta+1 > 0 && beta+1 < imgH))
				{
				int A = 25 + buf[I+3];
				if (A>=100)A = 100;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				
				
				}
				catch(Exception exception)
		        {
		            exception.printStackTrace();
		        }
			}
			/*
			alpha -= size;
			
			for(int length = 0; length <= size*2; length++)
			{
				try
				{
				int I = (int)(beta*imgW+ (alpha+length) )*4;
				if(I+3 > buf.length) break;
				
				if((alpha+length >= 0 && alpha + length < imgW)&&(beta > 0 && beta < imgH))
				{
					
				int A = 100;
				buf[I+3] = (byte)(A & 0xff);
					
				}
				else
				{
					continue;}
				
				
				I = (int)((beta-1)*imgW+ (alpha+length) )*4;
				if((alpha+length >= 0 && alpha + length < imgW)&&(beta-1 > 0 && beta-1 < imgH))
				{
				int A = 75 + buf[I+3];
				if (A>=100)A = 100;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				I = (int)((beta+1)*imgW+ (alpha+length) )*4;
				if((alpha+length >= 0 && alpha + length < imgW)&&(beta+1 > 0 && beta+1 < imgH))
				{
				int A = 75 + buf[I+3];
				if (A>=100)A = 100;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				
				I = (int)((beta-2)*imgW+ (alpha+length) )*4;
				if((alpha+length >= 0 && alpha + length < imgW)&&(beta-1 > 0 && beta-1 < imgH))
				{
				int A = 25 + buf[I+3];
				if (A>=100)A = 100;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				I = (int)((beta+2)*imgW+ (alpha+length) )*4;
				if((alpha+length >= 0 && alpha + length < imgW)&&(beta+1 > 0 && beta+1 < imgH))
				{
				int A = 25 + buf[I+3];
				if (A>=100)A = 100;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				
				
				}
				catch(Exception exception)
		        {
		            exception.printStackTrace();
		        }
			}
			*/

			bufEmpty = false;
			bChanged = true;
			
		}
		
		
	}
	
	public void drawNoise_RP(float azimuth, float intensity)
	{
		azimuth = azimuth/30;
		intensity *=100;
		azimuth = (azimuth+1f)*(imgW*0.5f);
		azimuth = Math.round(azimuth);
		
		int wide = (int) cvt(intensity, 0.1f, 1f, 4, 10);
		for(int length = -wide; length <= wide; length++)
		{
			try
			{
				for(int z = 1; z < imgH; z++)
				{
			int I = (int)(z*imgW+ (azimuth+length) )*4;
			if(I+3 > buf.length) break;
			
			if((azimuth+length >= 0 && azimuth + length < imgW))
			{
				
			int A = (buf[I+3] & 0xFF)+ (int)intensity;
			if(A > 200) A = 200;
			buf[I+3] = (byte)(A & 0xff);
			
			}
			else
			{
				continue;}
			}}
			catch(Exception exception)
	        {
	            exception.printStackTrace();
	        }
		}
		
	}
	public void drawContact_RP(float x,float z,float size, boolean IFF)
	{
		x=x/30;
		if(x>1)  x =  1;
		if(x<-1) x = -1;
		if(size < 0) return;
		float scale=1;
		if(size <= 1) scale = size * 20f; else scale = 20;
		int Range = ((MIG_23)aircraft()).RP_23_range;
		switch(Range)
		{
		case 0:
			Range = 3000;
			break;
		case 1:
			Range = 30000;
			break;
		case 2:
			Range = 60000;
			break;
		case 3:
			Range = 120000;
			break;
			default:
				break;
		}
		z = (z/Range);
		if(z>1)
			{
			//System.out.println("Target - failed to draw, z too big");
			return;
			}
		x++;
		x*= (imgW*0.5);
		z*= imgH;
		z=imgH-z;
		
		x = x-scale;
		

		x = Math.round(x);
		z = Math.round(z);
		for(int length = 0; length <= (scale*2); length++)
		{
			try
			{
			int I = (int)(z*imgW+ (x+length) )*4;
			if(I+3 > buf.length) break;
			
			if((x+length >= 0 && x + length < imgW)&&(z > 0 && z < imgH))
			{
				
				int A = (int)(100 + (buf[I+3]& 0xFF));
				if (A>=200)A = 200;
			buf[I+3] = (byte)(A & 0xff);
				
			}
			else
			{
				continue;}
			
			
			I = (int)((z-1)*imgW+ (x+length) )*4;
			if((x+length >= 0 && x + length < imgW)&&(z-1 > 0 && z-1 < imgH))
			{
				int A = (int)(75*size + (buf[I+3]& 0xFF));
			if (A>=200)A = 200;
			buf[I+3] = (byte)(A & 0xff);
			}
			
			I = (int)((z+1)*imgW+ (x+length) )*4;
			if((x+length >= 0 && x + length < imgW)&&(z+1 > 0 && z+1 < imgH))
			{
				int A = (int)(75*size + (buf[I+3]& 0xFF));
			if (A>=200)A = 200;
			buf[I+3] = (byte)(A & 0xff);
			}
			
			
			I = (int)((z-2)*imgW+ (x+length) )*4;
			if((x+length >= 0 && x + length < imgW)&&(z-1 > 0 && z-1 < imgH))
			{
				int A = (int)(25*size + (buf[I+3]& 0xFF));
			if (A>=200)A = 200;
			buf[I+3] = (byte)(A & 0xff);
			}
			
			I = (int)((z+2)*imgW+ (x+length) )*4;
			if((x+length >= 0 && x + length < imgW)&&(z+1 > 0 && z+1 < imgH))
			{
				int A = (int)(25*size + (buf[I+3]& 0xFF));
			if (A>=200)A = 200;
			buf[I+3] = (byte)(A & 0xff);
			}
			
			
			
			}
			catch(Exception exception)
	        {
	            exception.printStackTrace();
	        }
		}
		
		if(IFF)
		{
			z -= 7;
			for(int length = 0; length <= (scale*2); length++)
			{
				try
				{
				int I = (int)(z*imgW+ (x+length) )*4;
				if(I+3 > buf.length) break;
				
				if((x+length >= 0 && x + length < imgW)&&(z > 0 && z < imgH))
				{
					
					int A = (int)(100*size + (buf[I+3]& 0xFF));
					if (A>=200)A = 200;
				buf[I+3] = (byte)(A & 0xff);
					
				}
				else
				{
					continue;}
				
				
				I = (int)((z-1)*imgW+ (x+length) )*4;
				if((x+length >= 0 && x + length < imgW)&&(z-1 > 0 && z-1 < imgH))
				{
					int A = (int)(75*size + (buf[I+3]& 0xFF));
				if (A>=200)A = 200;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				I = (int)((z+1)*imgW+ (x+length) )*4;
				if((x+length >= 0 && x + length < imgW)&&(z+1 > 0 && z+1 < imgH))
				{
					int A = (int)(75*size + (buf[I+3]& 0xFF));
				if (A>=200)A = 200;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				
				I = (int)((z-2)*imgW+ (x+length) )*4;
				if((x+length >= 0 && x + length < imgW)&&(z-1 > 0 && z-1 < imgH))
				{
					int A = (int)(25*size + (buf[I+3]& 0xFF));
				if (A>=200)A = 200;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				I = (int)((z+2)*imgW+ (x+length) )*4;
				if((x+length >= 0 && x + length < imgW)&&(z+1 > 0 && z+1 < imgH))
				{
					int A = (int)(25*size + (buf[I+3]& 0xFF));
				if (A>=200)A = 200;
				buf[I+3] = (byte)(A & 0xff);
				}
				
				
				
				}
				catch(Exception exception)
		        {
		            exception.printStackTrace();
		        }
			}
		}

		bufEmpty = false;
		bChanged = true;
		
		
		
		
		
	}
	

	
	public void updateImage()
	{
		if(mat==null)
		{
			try
		{
				ch = super.mesh.chunkFind("Z_Z_HUD_radar");
				mt = super.mesh.materialFindInChunk("z_z_HUD_RADAR",ch);
				/*mat = super.mesh.material(mt);
				mat.setLayer(0);*/
				mat = (Mat)super.mesh.material(mt);
				mat.Rename(null);
				mat.setLayer(0);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}}
		else
		{
			try
			{
				if(bChanged)
				{
					ch = super.mesh.chunkFind("Z_Z_HUD_radar");
					mt = super.mesh.materialFindInChunk("z_z_HUD_RADAR",ch);
					mat = (Mat)super.mesh.material(mt);
					mat.setLayer(0);
					mat.updateImage(imgW,imgH,0x380004,buf);
					bChanged = false;
				}
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}
	
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
    private long t60;
    private long tp_flash_timer;
    private long t_tp23;
    private long image_refresh;
    private boolean tp_23_direction_right;
    private float tp_23_azimuth;
    private int rp_23_bar;
    private boolean rp_23_down;
    private long t_rp23;
    private int rp_23_azimuth;
    private int rp_23_lockphase;
    private float track_elevation;
    private float track_azimuth;
    private float track_range;
    private float LAZUR_elevation;
    private float LAZUR_azimuth;
    private boolean clutter;
    private boolean tracking;
    private long trefresh;
    private double azimult;
    private double tangage;
    private boolean trackzone;
    private float angular_x;
    private float angular_y;
    private float angular_dx;
    private float angular_dy;
    private float angular_timer;
    Aircraft ownAC;
    int imgW;
    int imgH;
    int bufInt[];
    byte buf[];
    int x0;
    int y0;
    BufferedImage imgCRT;
    Graphics grCRT;
    public boolean bChanged;
    public Mat mat;
    public int ch;
    public int mt;
    boolean bufEmpty;
    public float RP_23_noise;
    private boolean lookdown;













}