package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;

public class CockpitMi8 extends CockpitPilot
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
                setNew.pitch = 0.9F * setOld.pitch + (((FlightModelMain) (fm)).EI.engines[0].getControlProp() + ((FlightModelMain) (fm)).EI.engines[0].getControlProp() / 2) * 0.1F;
                setNew.starter = 0.94F * setOld.starter + 0.06F * (((FlightModelMain) (fm)).EI.engines[0].getStage() <= 0 || ((FlightModelMain) (fm)).EI.engines[0].getStage() >= 6 ? 0.0F : 1.0F);
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
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
                    } else
                    {
                        //setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), getBeaconDirection());
                        setNew.ilsLoc = 0.0F;
                        setNew.ilsGS = 0.0F;
                    }
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), waypointAzimuth() - ((FlightModelMain) (fm)).Or.azimut());
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
                }
                //setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
                //setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
               
                Variables variables = setNew;
                float f1 = 0.9F * setOld.radioalt;
                float f2 = 0.1F;
                float f3 = fm.getAltitude();
                World.cur();
                World.land();
                variables.radioalt = f1 + f2 * (f3 - Landscape.HQ_Air((float)fm.Loc.x, (float)fm.Loc.y));
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
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
        float pitch;
        float vspeed;
        float starter;
        float altimeter;
        float radioalt;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float beaconDirection;
        float beaconRange;
        float ilsLoc;
        float ilsGS;

        private Variables()
        {
            throttle = 0.0F;
            pitch = 0.0F;
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
            aircraft().hierMesh().chunkVisible("CF_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(Actor.isAlive(aircraft()))
            aircraft().hierMesh().chunkVisible("CF_D0", true);
        super.doFocusLeave();
    }

//    private float machNumber()
    {
//        return ((MIG_21)super.aircraft()).calculateMach();
    }

    public CockpitMi8()
    {
        super("3DO/Cockpit/MI8/hier.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(300F, 0F, 0F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        hooknamed = new HookNamed(mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(300F, 0F, 0F);
        light2.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        hooknamed = new HookNamed(mesh, "LAMPHOOK3");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light3 = new LightPointActor(new LightPoint(), loc.getPoint());
        light3.light.setColor(300F, 0F, 0F);
        light3.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK3", light3);
        super.cockpitNightMats = (new String[] {
            "Gause1", "Gause2", "Gause3", "Gause4", "Sidepanel", "instrument1"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
//        AircraftLH.printCompassHeading = true;
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
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
        resetYPRmodifier();
//        super.mesh.chunkSetAngles("Canopy", -90F * ((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_STICK1", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 10F, -(pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F);
        super.mesh.chunkSetAngles("Z_STICK2", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 10F, -(pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F);
        super.mesh.chunkSetAngles("BARRAD", 0.0F, 30F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("BARRAI", 0.0F, 30F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(((FlightModelMain) (super.fm)).CT.getRudder(), -1.0F, 1.0F, -0.09F, 0.06F);
        super.mesh.chunkSetLocate("PEDALL1", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(((FlightModelMain) (super.fm)).CT.getRudder(), -1.0F, 1.0F, 0.06F, -0.09F);
        super.mesh.chunkSetLocate("PEDALL2", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Colectivo", 0.0F, 0.0F, -20.909F * interp(setNew.pitch, setOld.pitch, f));
        super.mesh.chunkSetAngles("Z_Gases", 0.0F, -45.0F * interp(setNew.throttle, setOld.throttle, f), 0.0F);
        super.mesh.chunkSetAngles("Z_ColectivoL", 0.0F, 0.0F, -20.909F * interp(setNew.pitch, setOld.pitch, f));
        super.mesh.chunkSetAngles("Z_GasesL", 0.0F, -45.0F * interp(setNew.throttle, setOld.throttle, f), 0.0F);
        super.mesh.chunkSetAngles("Z_RPM1", 0.0F, -cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 1665F, 0.0F, 315F), 0.0F);
        super.mesh.chunkSetAngles("Z_RPM2", 0.0F, -cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 1665F, 0.0F, 315F), 0.0F);
        super.mesh.chunkSetAngles("Z_FUEL", 0.0F, -cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 1470F, 0.0F, 330F), 0.0F);
        super.mesh.chunkSetAngles("Z_TEMP1", 0.0F, cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 20F, 175F, 0.0F, 130F), 0.0F);
        super.mesh.chunkSetAngles("Z_TEMP2", 0.0F, -cvt(((FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 20F, 175F, 0.0F, 130F), 0.0F);
        super.mesh.chunkSetAngles("Z_TEMP3", 0.0F, -cvt(((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 300F, 900F, 0.0F, 220F), 0.0F);
        super.mesh.chunkSetAngles("Z_PRES", 0.0F, -cvt(((FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : 0.55F, 0.0F, 1.0F, 0.0F, 160F), 0.0F);
        
        super.mesh.chunkSetAngles("Z_AOA", 0.0F, cvt(super.fm.getOverload(), -4.5F, 10F, -110F, 220F), 0.0F);
        
//        super.mesh.chunkSetAngles("Z_AOA", cvt(fm.getAOA(), -10F, 35F, -50F, 180F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_HOUR", 0.0F, -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("Z_MIMUTE", 0.0F, -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_SECOND", 0.0F, -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F),0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        super.mesh.chunkSetAngles("Z_COMPASSA", 0.0F, 90F + setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("Z_COMPASSB", -90F + setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_COMPASSC", setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_SPD", 0.0F, -cvt(super.fm.getSpeedKMH(), 0F, 300F, 0.0F, 330F), 0.0F);
//        super.mesh.chunkSetAngles("Z_Speedometer3", cvt(machNumber(), 0.5F, 3F, 0.0F, 349F), 0.0F, 0.0F);
//        super.mesh.chunkSetAngles("Z_Speedometer2", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 200F, 2500F, 0.0F, 24F), speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_ALTKM", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_ALTM", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 10800F), 0.0F);
//        super.mesh.chunkSetAngles("Z_Altimeter3", cvt(interp(setNew.radioalt, setOld.radioalt, f), 0.0F, 600F, 0.0F, 280F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_VERTSPD", 0.0F, -cvt(setNew.vspeed, -20F, 20F, -65F, 65F), 0.0F);
        super.mesh.chunkSetAngles("Z_BANK", 0.0F, -cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F);
        super.mesh.chunkSetAngles("ADIBANK", ((FlightModelMain) (super.fm)).Or.getKren(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("ADIMOVE", 1.2F * ((FlightModelMain) (super.fm)).Or.getTangage(), 0.0F, 0.0F);
//        super.mesh.chunkSetAngles("Z_Gear1", 0.0F, 0.0F, 50F * (pictGear = 0.82F * pictGear + 0.18F * ((FlightModelMain) (super.fm)).CT.GearControl));
    }
/*        super.mesh.chunkVisible("L_DownC", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_DownL", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_DownR", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_UPC", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("L_UPL", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("L_UPR", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        
        if(((FlightModelMain) (super.fm)).CT.getFlap() > 0.9F)
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
                
        if(((FlightModelMain) (super.fm)).EI.engines[0].getThrustOutput() > 1.05F && ((FlightModelMain) (super.fm)).EI.engines[0].getStage() > 5)
                super.mesh.chunkVisible("L_AB2", true);
            else
                super.mesh.chunkVisible("L_AB2", false);
          
        super.mesh.chunkVisible("L_Fire1", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
        super.mesh.chunkVisible("L_Fire2", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);

        super.mesh.chunkVisible("L_Fuel", ((FlightModelMain) (super.fm)).M.fuel < 750F);

        if(super.fm.getAltitude() < 150F)
                super.mesh.chunkVisible("L_Altitude1", true);
            else
                super.mesh.chunkVisible("L_Altitude1", false);
                
        if(super.fm.getAltitude() < 300F)
                super.mesh.chunkVisible("L_Altitude2", true);
            else
                super.mesh.chunkVisible("L_Altitude2", false);

//        if(super.fm.getAOA() > 20F)
//                super.mesh.chunkVisible("L_AOA1", true);
//            else
//                super.mesh.chunkVisible("L_AOA1", false);
//                
//        if(super.fm.getAOA() > 20F)
//                super.mesh.chunkVisible("L_AOA2", true);
//            else
//                super.mesh.chunkVisible("L_AOA2", false);
                
                
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
            if(iLockState() == 2)
                super.mesh.chunkVisible("L_Missiles", true);
            else
                super.mesh.chunkVisible("L_Missiles", false);
        if(((MIG_21)aircraft()).k14Mode == 2)
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
        else
            super.mesh.chunkVisible("Z_Z_RETICLE", true);
        }
    }

    public void reflectCockpitState()
    {
    } */

/*    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
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
    } */
    
    protected void reflectPlaneMats()
    {
        mesh.chunkVisible("Cockpit_D0", false);
        mesh.chunkVisible("CF_D0", false);
    }


    protected void reflectPlaneToModel()
    {
    }

    public void doToggleDim()
    {
    }

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
    public Vector3f w;
    private float pictGear;
}