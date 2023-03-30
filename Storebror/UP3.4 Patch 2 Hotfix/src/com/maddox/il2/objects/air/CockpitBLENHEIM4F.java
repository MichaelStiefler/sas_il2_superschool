package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitBLENHEIM4F extends CockpitPilot
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
                setNew.throttle1 = 0.85F * setOld.throttle1 + fm.EI.engines[0].getControlThrottle() * 0.15F;
                setNew.throttle2 = 0.85F * setOld.throttle2 + fm.EI.engines[1].getControlThrottle() * 0.15F;
                setNew.prop1 = 0.85F * setOld.prop1 + fm.EI.engines[0].getControlProp() * 0.15F;
                setNew.prop2 = 0.85F * setOld.prop2 + fm.EI.engines[1].getControlProp() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), f);
                setNew.waypointDeviation.setDeg(setOld.waypointDeviation.getDeg(0.1F), (f - setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if((fm.AS.astateCockpitState & 0x40) == 0)
                    setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                else
                    setNew.vspeed = (1990F * setOld.vspeed + fm.getVertSpeed()) / 2000F;
                World.land();
                setNew.radioalt = 0.9F * setOld.radioalt + 0.1F * ((fm.getAltitude() - Landscape.HQ((float)fm.Loc.x, (float)fm.Loc.y)) + World.Rnd().nextFloat(-10F, 10F));
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttle1;
        float throttle2;
        float prop1;
        float prop2;
        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float vspeed;
        AnglesFork waypointDeviation;
        float radioalt;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            waypointDeviation = new AnglesFork();
        }

    }

    protected float waypointAzimuth()
    {
        WayPoint waypoint = this.fm.AP.way.curr();
        if(waypoint == null)
            return 0.0F;
        waypoint.getP(tmpP);
        tmpV.sub(tmpP, this.fm.Loc);
        float f = (float)(Math.toDegrees(Math.atan2(-tmpV.y, tmpV.x)));
        while (f <= -180F) f += 360F;
        while (f > 180F) f -= 360F;
        return f;
    }

    public CockpitBLENHEIM4F()
    {
        super("3DO/Cockpit/BlenheimMk4F/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        pictManf1 = 1.0F;
        pictManf2 = 1.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] {
            "prib_one_fin", "prib_two", "prib_three", "prib_four", "gauges2", "prib_one_fin_damage", "prib_two_damage", "prib_three_damage", "prib_four_damage", "gauges2_damage", 
            "PEICES1", "PEICES2"
        });
        mesh.chunkSetAngles("PRICEL_ST_MOS", 0.0F, 0.0F, 0.0F);
        mesh.chunkSetAngles("PRICEL_MOS", 0.0F, 0.0F, 0.0F);
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(acoustics != null)
            acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Nose_D0", false);
            aircraft().hierMesh().chunkVisible("Nose_D1", false);
            aircraft().hierMesh().chunkVisible("Nose_D2", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Nose_D0", true);
        aircraft().hierMesh().chunkVisible("Nose_D1", true);
        aircraft().hierMesh().chunkVisible("Nose_D2", true);
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float f)
    {
        resetYPRmodifier();
        this.mesh.chunkSetAngles("Canopy", 0.0F, cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, -120F), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim1", 0.0F, 161F * this.fm.CT.getTrimAileronControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 0.0F, 332F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", 0.0F, 722F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 0.0F, -75.5F * (pictFlap = 0.85F * pictFlap + 0.15F * this.fm.CT.FlapsControl), 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 0.0F, -75.5F * (pictGear = 0.85F * pictGear + 0.15F * this.fm.CT.GearControl), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 90F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, 100F * interp(setNew.prop1, setOld.prop1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 0.0F, 100F * interp(setNew.prop2, setOld.prop2, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, 90F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger1", 0.0F, cvt(this.fm.EI.engines[0].getControlCompressor(), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger1", 0.0F, cvt(this.fm.EI.engines[1].getControlCompressor(), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (pictElev = 0.85F * pictElev + 0.15F * this.fm.CT.ElevatorControl) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl) * 57F, 0.0F);
        this.mesh.chunkSetAngles("Z_Brake", 0.0F, -25F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter3", 0.0F, cvt(interp(setNew.radioalt, setOld.radioalt, f), 0.0F, 609.6F, 0.0F, 720F), 0.0F);
        if((this.fm.AS.astateCockpitState & 0x40) == 0)
            this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 143.0528F, 0.0F, 32F), speedometerScaleFAF), 0.0F);
        w.set(this.fm.getW());
        this.fm.Or.transform(w);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, cvt(w.z, -0.23562F, 0.23562F, -48F, 48F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, cvt(getBall(8D), -8F, 8F, 35F, -35F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, -this.fm.Or.getKren(), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0275F, -0.0275F);
        this.mesh.chunkSetLocate("Z_TurnBank4", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, floatindex(cvt(this.fm.EI.engines[0].getRPM(), 1000F, 5000F, 0.0F, 8F), rpmScale), 0.0F);
        if((this.fm.AS.astateCockpitState & 0x40) == 0)
            this.mesh.chunkSetAngles("Z_RPM2", 0.0F, floatindex(cvt(this.fm.EI.engines[1].getRPM(), 1000F, 5000F, 0.0F, 8F), rpmScale), 0.0F);
        else
            this.mesh.chunkSetAngles("Z_RPM2", 0.0F, floatindex(cvt(this.fm.EI.engines[1].getRPM(), 500F, 9800F, 0.0F, 8F), rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, 0.0F, cvt(this.fm.M.fuel, 0.0F, 2332F, 0.0F, 77F));
        this.mesh.chunkSetAngles("Z_Fuel2", 0.0F, 0.0F, cvt(this.fm.M.fuel, 0.0F, 2332F, 0.0F, 77F));
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, cvt(this.fm.EI.engines[0].getRPM() / 910F, 0.0F, 10F, 0.0F, 277F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres2", 0.0F, cvt(this.fm.EI.engines[1].getRPM() / 880F, 0.0F, 10F, 0.0F, 277F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, floatindex(cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 140F, 0.0F, 14F), radScale), 0.0F);
        if((this.fm.AS.astateCockpitState & 0x40) == 0)
            this.mesh.chunkSetAngles("Z_Temp2", 0.0F, floatindex(cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 140F, 0.0F, 14F), radScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", 0.0F, pictManf1 = 0.9F * pictManf1 + 0.1F * cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Pres2", 0.0F, pictManf2 = 0.9F * pictManf2 + 0.1F * cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, cvt(this.fm.EI.engines[0].tOilOut, 40F, 100F, 0.0F, 274F), 0.0F);
        if((this.fm.AS.astateCockpitState & 0x40) == 0)
            this.mesh.chunkSetAngles("Z_Oil2", 0.0F, cvt(this.fm.EI.engines[1].tOilOut, 40F, 100F, 0.0F, 274F), 0.0F);
        if(this.fm.getOverload() < 0.0F)
        {
            this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, cvt(1.0F + 0.05F * (this.fm.EI.engines[0].tOilOut / 10F) + this.fm.EI.engines[0].getRPM() / 800F + this.fm.getOverload() / 1.8F, 0.0F, 12.59F, 0.0F, 277F), 0.0F);
            this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, cvt(1.0F + 0.05F * (this.fm.EI.engines[1].tOilOut / 10F) + this.fm.EI.engines[1].getRPM() / 820F + this.fm.getOverload() / 1.8F, 0.0F, 12.59F, 0.0F, 277F), 0.0F);
        } else
        {
            this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, cvt(1.0F + 0.05F * (this.fm.EI.engines[0].tOilOut / 10F) + this.fm.EI.engines[0].getRPM() / 800F + this.fm.getOverload() / 3.8F, 0.0F, 12.59F, 0.0F, 277F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, cvt(1.0F + 0.05F * (this.fm.EI.engines[1].tOilOut / 10F) + this.fm.EI.engines[1].getRPM() / 820F + this.fm.getOverload() / 3.8F, 0.0F, 12.59F, 0.0F, 277F), 0.0F);
        float f1 = 0.5F * this.fm.EI.engines[0].getRPM() + 0.5F * this.fm.EI.engines[1].getRPM();
        f1 = 2.5F * (float)Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("Z_Suction", 0.0F, cvt(f1, 0.0F, 10F, 0.0F, 302F), 0.0F);
        this.mesh.chunkSetAngles("Z_Approach", 0.0F, cvt(setNew.waypointDeviation.getDeg(f), -90F, 90F, -46.5F, 46.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_AirTemp", 0.0F, cvt(Atmosphere.temperature((float)this.fm.Loc.z) - 273.15F, -17.8F, 60F, 0.0F, -109.5F), 0.0F);
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 0x80) != 0)
        {
            this.mesh.chunkVisible("Z_OilSplats1_D1", true);
            this.mesh.chunkVisible("Z_OilSplats2_D1", true);
        }
        if((this.fm.AS.astateCockpitState & 2) != 0)
        {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if((this.fm.AS.astateCockpitState & 1) != 0)
        {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if((this.fm.AS.astateCockpitState & 0x40) != 0)
        {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_Fuel2", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Altimeter3", false);
            this.mesh.chunkVisible("Z_Hour1", false);
            this.mesh.chunkVisible("Z_Minute1", false);
            this.mesh.chunkVisible("Z_Oilpres1", false);
            this.mesh.chunkVisible("Z_FuelPres2", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
        }
        if((this.fm.AS.astateCockpitState & 4) != 0)
            this.mesh.chunkVisible("XGlassDamage3", true);
        if((this.fm.AS.astateCockpitState & 8) != 0)
            this.mesh.chunkVisible("XHullDamage2", true);
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
            this.mesh.chunkVisible("XGlassDamage2", true);
        if((this.fm.AS.astateCockpitState & 0x20) != 0)
            this.mesh.chunkVisible("XHullDamage3", true);
        retoggleLight();
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(this.cockpitLightControl)
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
    private float pictFlap;
    private float pictGear;
    private float pictManf1;
    private float pictManf2;
    private static final float speedometerScaleFAF[] = {
        0.0F, 0.0F, 2.0F, 6F, 21F, 40F, 56F, 72.5F, 89.5F, 114F, 
        135.5F, 157F, 182.5F, 205F, 227.5F, 246.5F, 265.5F, 286F, 306F, 326F, 
        345.5F, 363F, 385F, 401F, 414.5F, 436.5F, 457F, 479F, 496.5F, 515F, 
        539F, 559.5F, 576.5F
    };
    private static final float variometerScale[] = {
        -158F, -110F, -63.5F, -29F, 0.0F, 29F, 63.5F, 110F, 158F
    };
    private static final float rpmScale[] = {
        0.0F, 20F, 54F, 99F, 151.5F, 195.5F, 249.25F, 284.5F, 313.75F
    };
    private static final float radScale[] = {
        0.0F, 3F, 7F, 13.5F, 21.5F, 27F, 34.5F, 50.5F, 71F, 94F, 
        125F, 161F, 202.5F, 253F, 315.5F
    };
    private Point3d tmpP;
    private Vector3d tmpV;

}
