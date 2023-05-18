// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 14/10/2015 10:17:05 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitF9F_Student.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, F9F_Cougar

public class CockpitF9F_Student extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = 0.9F * setOld.throttle + ((FlightModelMain) (fm)).CT.PowerControl * 0.1F;
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth = ((FlightModelMain) (fm)).Or.getYaw();
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                if(((FlightModelMain) (fm)).CT.arrestorControl <= 0.5F)
                {
                    if(setNew.stbyPosition > 0.0F)
                        setNew.stbyPosition = setOld.stbyPosition - 0.05F;
                } else
                if(setNew.stbyPosition < 1.0F)
                    setNew.stbyPosition = setOld.stbyPosition + 0.05F;
                if(((FlightModelMain) (fm)).EI.engines[0].getStage() < 6 && !((FlightModelMain) (fm)).CT.bHasAileronControl && !((FlightModelMain) (fm)).CT.bHasElevatorControl && !((FlightModelMain) (fm)).CT.bHasGearControl && !((FlightModelMain) (fm)).CT.bHasAirBrakeControl && !((FlightModelMain) (fm)).CT.bHasFlapsControl)
                {
                    if(setNew.stbyPosition3 > 0.0F)
                        setNew.stbyPosition3 = setOld.stbyPosition3 - 0.005F;
                } else
                if(setNew.stbyPosition3 < 1.0F)
                    setNew.stbyPosition3 = setOld.stbyPosition3 + 0.005F;
                float f = ((F9F_Cougar)aircraft()).k14Distance;
                setNew.k14w = (5F * CockpitF9F_Student.k14TargetWingspanScale[((F9F_Cougar)aircraft()).k14WingspanType]) / f;
                setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
                setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitF9F_Student.k14TargetMarkScale[((F9F_Cougar)aircraft()).k14WingspanType];
                setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((F9F_Cougar)aircraft()).k14Mode;
                Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
                double d = 0.00125D * (double)f;
                float f1 = (float)Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
                float f2 = -(float)Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
                float f3 = floatindex((f - 200F) * 0.04F, CockpitF9F_Student.k14BulletDrop) - CockpitF9F_Student.k14BulletDrop[0];
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

    private class Variables
    {

        float throttle;
        float vspeed;
        float altimeter;
        float azimuth;
        float waypointAzimuth;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;
        float stbyPosition;
        float stbyPosition3;

        private Variables()
        {
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    public CockpitF9F_Student()
    {
        super("3DO/Cockpit/F9/F9F8T.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictGear = 0.0F;
        pictMet1 = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        super.cockpitNightMats = (new String[] {
            "Gauges_01", "Gauges_02", "Gauges_03", "Gauges_04", "Gauges_05", "Gauges_06", "Gauges_08", "Compass1", "Needles"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
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

    protected float waypointAzimuth()
    {
        WayPoint waypoint = ((FlightModelMain) (super.fm)).AP.way.curr();
        if(waypoint == null)
        {
            return 0.0F;
        } else
        {
            waypoint.getP(tmpP);
            tmpV.sub(tmpP, ((FlightModelMain) (super.fm)).Loc);
            return (float)(57.295779513082323D * Math.atan2(((Tuple3d) (tmpV)).y, ((Tuple3d) (tmpV)).x));
        }
    }

    public void reflectWorldToInstruments(float f)
    {
        if(F9F_Cougar.bChangedPit)
            F9F_Cougar.bChangedPit = false;
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) == 0)
        {
            int i = ((F9F_Cougar)aircraft()).k14Mode;
            boolean flag = i < 2;
            super.mesh.chunkVisible("Z_Z_RETICLE", flag);
            flag = i > 0;
            super.mesh.chunkVisible("Z_Z_RETICLE1", flag);
            super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, setNew.k14y);
            resetYPRmodifier();
            Cockpit.xyz[0] = setNew.k14w;
            for(int k = 1; k < 7; k++)
            {
                super.mesh.chunkVisible("Z_Z_AIMMARK" + k, flag);
                super.mesh.chunkSetLocate("Z_Z_AIMMARK" + k, Cockpit.xyz, Cockpit.ypr);
            }

        }
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.01F, 0.99F, -0.52F, 0.0F);
        super.mesh.chunkSetLocate("CanopyOpen01", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("CanopyOpen02", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("CanopyOpen03", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("XGlassDamage4", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr == 0)
        {
            Cockpit.xyz[1] = 0.0F;
            super.mesh.chunkSetLocate("Body", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Boites", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("CanLever01", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("CanLever02", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("CanLever03", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("CanLever04", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Collimateur", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("EjectSeat", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("EjectSeat2", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("EjectSeat3", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("FlareGearDn_C", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("FlareGearDn_L", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("FlareGearDn_R", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("FlareGearUp_C", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("FlareGearUp_L", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("FlareGearUp_R", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Instruments", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("InstrumentsD", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Interior", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Manette", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("NewVis", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Panel", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("PareBrise2", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("PareBrise", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Pedal_L", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Pedal_R", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Placards", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Stick01", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Stick02", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Throttle", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Vis2", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Vis", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("VisFace", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("XGlassDamage1", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("XGlassDamage2", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("XGlassDamage3", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("XGlassDamage4", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Canopy1", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Canopy2", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_FlapsLever", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_FlareFire1", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_FlareFire2", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_FlareFuel", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_GearHandle", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Hook2a", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Hook2b", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Hook", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Ignition1", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Ignition2", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Master1", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Master2", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Slide1a", Cockpit.xyz, Cockpit.ypr);
            super.mesh.chunkSetLocate("Z_Target1", Cockpit.xyz, Cockpit.ypr);
            if(((FlightModelMain) (super.fm)).CT.getCockpitDoor() == 1.0F)
            {
                super.mesh.chunkSetLocate("CanopyOpen01", Cockpit.xyz, Cockpit.ypr);
                super.mesh.chunkSetLocate("CanopyOpen02", Cockpit.xyz, Cockpit.ypr);
                super.mesh.chunkSetLocate("CanopyOpen03", Cockpit.xyz, Cockpit.ypr);
            }
        }
        super.mesh.chunkSetAngles("Z_GearHandle", 0.0F, 0.0F, 50F * (pictGear = 0.82F * pictGear + 0.18F * ((FlightModelMain) (super.fm)).CT.GearControl));
        super.mesh.chunkSetAngles("Z_FlapsLever", -35F * ((FlightModelMain) (super.fm)).CT.FlapsControl, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Hook", 0.0F, cvt(interp(setNew.stbyPosition, setOld.stbyPosition, f), 0.0F, 1.0F, 0.0F, 35F), 0.0F);
        super.mesh.chunkSetAngles("Z_Target1", setNew.k14wingspan, 0.0F, 0.0F);
        w.set(super.fm.getW());
        ((FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_G-Factor", cvt(super.fm.getOverload(), -4F, 12F, -80.5F, 241.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel", cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 1358F, -150F, 150F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Turn1a", 0.0F, cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
        super.mesh.chunkSetAngles("Z_Slide1a", 0.0F, cvt(getBall(8D), -8F, 8F, -24F, 24F), 0.0F);
        super.mesh.chunkSetAngles("Z_horizont1a", 0.0F, -((FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
        super.mesh.chunkSetAngles("Z_Suction", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3500F, 100F, 395F), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(((FlightModelMain) (super.fm)).CT.getRudder(), -1F, 1.0F, -0.035F, 0.035F);
        super.mesh.chunkSetLocate("Pedal_L", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -Cockpit.xyz[1];
        super.mesh.chunkSetLocate("Pedal_R", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 0.0328F, -0.0328F);
        super.mesh.chunkSetLocate("Z_horizont1b", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Stick01", (pictElev = 0.7F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 16F, 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 15F);
        super.mesh.chunkSetAngles("Stick02", (pictElev = 0.7F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 16F, 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 15F);
        pictMet1 = 0.96F * pictMet1 + 0.04F * (0.6F * ((FlightModelMain) (super.fm)).EI.engines[0].getThrustOutput() * ((FlightModelMain) (super.fm)).EI.engines[0].getControlThrottle() * (((FlightModelMain) (super.fm)).EI.engines[0].getStage() != 6 ? 0.02F : 1.0F));
        super.mesh.chunkSetAngles("Z_Compass-Emerg1", cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -30F, 30F, 30F, -30F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass-Emerg3", cvt(((FlightModelMain) (super.fm)).Or.getKren(), -45F, 45F, -45F, 45F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass-Emerg2", -interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM-1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 2550F, -180F, 50F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM-2", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 2550F, 3500F, -10F, 335F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Exhaust_Temp", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 20F, 95F, -160F, -25F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass1", 0.0F, interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        super.mesh.chunkSetAngles("Z_Compass2", 0.0F, interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        super.mesh.chunkSetAngles("Z_Compass3", 0.0F, interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        super.mesh.chunkSetAngles("Z_Azimuth1", -interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter_1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter_2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter_3", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2_1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2_2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2_3", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Alt2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 1219.2F, -150F, 160F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speed", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Climb", 0.0F, cvt(setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        super.mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Hour2", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Minute2", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Second2", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkVisible("FlareGearUp_R", ((FlightModelMain) (super.fm)).CT.getGear() < 0.01F || !((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("FlareGearUp_L", ((FlightModelMain) (super.fm)).CT.getGear() < 0.01F || !((FlightModelMain) (super.fm)).Gears.lgear);
        super.mesh.chunkVisible("FlareGearUp_C", ((FlightModelMain) (super.fm)).CT.getGear() < 0.01F);
        super.mesh.chunkVisible("FlareGearDn_R", ((FlightModelMain) (super.fm)).CT.getGear() > 0.99F && ((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("FlareGearDn_L", ((FlightModelMain) (super.fm)).CT.getGear() > 0.99F && ((FlightModelMain) (super.fm)).Gears.lgear);
        super.mesh.chunkVisible("FlareGearDn_C", ((FlightModelMain) (super.fm)).CT.getGear() > 0.99F);
        super.mesh.chunkVisible("Z_FlareFuel", ((FlightModelMain) (super.fm)).M.fuel < 150F);
        super.mesh.chunkVisible("Z_FlareFire1", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
        super.mesh.chunkVisible("Z_FlareFire2", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
        resetYPRmodifier();
        Cockpit.ypr[0] = interp(setNew.throttle, setOld.throttle, f) * 45F;
        Cockpit.xyz[2] = cvt(Cockpit.ypr[0], 7.5F, 9.5F, 0.0F, 0.0F);
        super.mesh.chunkSetLocate("Throttle", Cockpit.xyz, Cockpit.ypr);
        float f1 = World.Rnd().nextFloat(0.87F, 1.04F);
        if(((FlightModelMain) (super.fm)).CT.getCockpitDoor() > 0.49F && super.fm.getSpeed() > 164F * f1)
        {
            ((FlightModelMain) (super.fm)).CT.bHasCockpitDoorControl = false;
            super.mesh.chunkVisible("CanopyOpen01", false);
            super.mesh.chunkVisible("CanopyOpen02", false);
            super.mesh.chunkVisible("CanopyOpen03", false);
            super.mesh.chunkVisible("XGlassDamage4", false);
        }
        if(((FlightModelMain) (super.fm)).AS.bIsAboutToBailout && !super.fm.brakeShoe && super.fm.getSpeedKMH() >= 100F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr == 0)
        {
            super.mesh.chunkVisible("Z_Canopy1", false);
            super.mesh.chunkVisible("Z_Canopy2", true);
        } else
        {
            super.mesh.chunkVisible("Z_Canopy1", true);
            super.mesh.chunkVisible("Z_Canopy2", false);
        }
        if(((FlightModelMain) (super.fm)).AS.bIsAboutToBailout)
        {
            super.mesh.chunkVisible("CanopyOpen01", false);
            super.mesh.chunkVisible("CanopyOpen02", false);
            super.mesh.chunkVisible("CanopyOpen03", false);
            super.mesh.chunkVisible("XGlassDamage4", false);
        }
        if(((FlightModelMain) (super.fm)).CT.cockpitDoorControl == 1.0F && !((FlightModelMain) (super.fm)).AS.bIsAboutToBailout)
        {
            if(((FlightModelMain) (super.fm)).CT.getCockpitDoor() > 0.0F && ((FlightModelMain) (super.fm)).CT.getCockpitDoor() < 0.2F)
            {
                super.mesh.chunkVisible("CanLever01", false);
                super.mesh.chunkVisible("CanLever03", true);
                super.mesh.chunkVisible("CanLever04", false);
            } else
            {
                super.mesh.chunkVisible("CanLever01", true);
                super.mesh.chunkVisible("CanLever03", false);
                super.mesh.chunkVisible("CanLever04", false);
            }
        } else
        if(((FlightModelMain) (super.fm)).CT.cockpitDoorControl == 0.0F && !((FlightModelMain) (super.fm)).AS.bIsAboutToBailout)
        {
            if(((FlightModelMain) (super.fm)).CT.getCockpitDoor() < 1.0F && ((FlightModelMain) (super.fm)).CT.getCockpitDoor() > 0.8F)
            {
                super.mesh.chunkVisible("CanLever01", false);
                super.mesh.chunkVisible("CanLever02", true);
                super.mesh.chunkVisible("CanLever04", false);
            } else
            {
                super.mesh.chunkVisible("CanLever01", true);
                super.mesh.chunkVisible("CanLever02", false);
                super.mesh.chunkVisible("CanLever04", false);
            }
        } else
        if(((FlightModelMain) (super.fm)).AS.bIsAboutToBailout && super.fm.getSpeedKMH() < 100F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr > 0)
        {
            super.mesh.chunkVisible("CanLever01", false);
            super.mesh.chunkVisible("CanLever04", true);
        }
        if(((FlightModelMain) (super.fm)).CT.BayDoorControl == 1.0F)
        {
            super.mesh.chunkVisible("Z_Master1", false);
            super.mesh.chunkVisible("Z_Master2", true);
        }
        if(((FlightModelMain) (super.fm)).CT.BayDoorControl == 0.0F)
        {
            super.mesh.chunkVisible("Z_Master1", true);
            super.mesh.chunkVisible("Z_Master2", false);
        }
        if(((FlightModelMain) (super.fm)).CT.arrestorControl <= 0.5F)
        {
            super.mesh.chunkVisible("Z_Hook2b", false);
            super.mesh.chunkVisible("Z_Hook2a", true);
        }
        if(((FlightModelMain) (super.fm)).CT.arrestorControl > 0.5F)
        {
            super.mesh.chunkVisible("Z_Hook2a", false);
            super.mesh.chunkVisible("Z_Hook2b", true);
        }
        if(((FlightModelMain) (super.fm)).EI.engines[0].getStage() < 6 && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr == 0)
        {
            super.mesh.chunkVisible("Z_Ignition1", false);
            super.mesh.chunkVisible("Z_Ignition2", true);
        } else
        {
            super.mesh.chunkVisible("Z_Ignition1", true);
            super.mesh.chunkVisible("Z_Ignition2", false);
        }
        if(((F9F_Cougar)aircraft()).k14Mode == 2)
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
        else
            super.mesh.chunkVisible("Z_Z_RETICLE", true);
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0)
        {
            super.mesh.chunkVisible("Instruments", false);
            super.mesh.chunkVisible("InstrumentsD", true);
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
            super.mesh.chunkVisible("Z_Z_MASK", false);
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage2", true);
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
            super.mesh.chunkVisible("Z_Speed", false);
            super.mesh.chunkVisible("Z_Compass1", false);
            super.mesh.chunkVisible("Z_Azimuth1", false);
            super.mesh.chunkVisible("Z_Alt_Km", false);
            super.mesh.chunkVisible("Z_Alt_M", false);
            super.mesh.chunkVisible("Z_Turn", false);
            super.mesh.chunkVisible("Z_Turn1a", false);
            super.mesh.chunkVisible("Z_Slide1a", false);
            super.mesh.chunkVisible("Z_RPM1", false);
            super.mesh.chunkVisible("Z_Z_RETICLE1", false);
            for(int i = 1; i < 7; i++)
                super.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);

        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage2", true);
        }
        if(((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) != 0 || (((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0) && (((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage2", true);
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
        }
        if(((((FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0 || (((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0) && (((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
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

    public void doToggleDim()
    {
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictGear;
    private float pictMet1;
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
    private static final float speedometerScale[] = {
        0.0F, 42F, 65.5F, 88.5F, 111.3F, 134F, 156.5F, 181F, 205F, 227F, 
        249.4F, 271.7F, 294F, 316.5F, 339.5F
    };
    private Point3d tmpP;
    private Vector3d tmpV;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitF9F_Student.class, "normZN", 0.98F);
        Property.set(com.maddox.il2.objects.air.CockpitF9F_Student.class, "gsZN", 0.98F);
    }









}
