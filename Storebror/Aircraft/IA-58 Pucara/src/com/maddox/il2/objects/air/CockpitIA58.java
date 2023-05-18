package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitIA58 extends CockpitPilot
{
    private class Variables
    {

        float throttle1;
        float throttle2;
        float stage;
        float prop;
        float mix;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;

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
                setNew.throttle1 = 0.85F * setOld.throttle1 + 0.15F * fm.EI.engines[0].getControlThrottle();
                setNew.throttle2 = 0.85F * setOld.throttle2 + 0.15F * fm.EI.engines[1].getControlThrottle();
                setNew.stage = 0.85F * setOld.stage + (float)fm.EI.engines[0].getControlCompressor() * 0.15F;
                setNew.prop = 0.85F * setOld.prop + fm.CT.getStepControl() * 0.15F;
                setNew.mix = 0.85F * setOld.mix + fm.EI.engines[0].getControlMix() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + waypointAzimuth() + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected float waypointAzimuth()
    {
        WayPoint waypoint = this.fm.AP.way.curr();
        if(waypoint == null)
        {
            return 0.0F;
        } else
        {
            waypoint.getP(tmpP);
            tmpV.sub(tmpP, this.fm.Loc);
            return (float)(Math.toDegrees(Math.atan2(-tmpV.y, tmpV.x)));
        }
    }

    public CockpitIA58()
    {
        super("3DO/Cockpit/IA58/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] {
            "GagePanel1", "GagePanel2", "GagePanel3", "GagePanel4", "GagePanel5", "GagePanel6", "GagePanel7", "Glass", "needles", "radio1"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        this.mesh.chunkSetAngles("Z_RightPedal", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", interp(setNew.throttle1, setOld.throttle1, f) * 40F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", interp(setNew.throttle2, setOld.throttle2, f) * 40F, 0.0F, 0.0F);
        resetYPRmodifier();
        float f1 = this.fm.EI.engines[0].getStage();
        if(f1 > 0.0F && f1 < 7F)
            f1 = 0.0345F;
        else
            f1 = -0.05475F;
        Cockpit.xyz[2] = f1;
        this.mesh.chunkSetAngles("Z_Column", (pictElev = 0.85F * pictElev + 0.15F * this.fm.CT.ElevatorControl) * 16F, 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl) * 20F);
        if(this.fm.CT.GearControl == 0.0F && this.fm.CT.getGear() != 0.0F)
            f1 = 40F;
        else
        if(this.fm.CT.GearControl == 1.0F && this.fm.CT.getGear() != 1.0F)
            f1 = 20F;
        else
            f1 = 0.0F;
        this.mesh.chunkSetAngles("Z_Gear1", f1, 0.0F, 0.0F);
        resetYPRmodifier();
        if(this.fm.CT.saveWeaponControl[0])
            Cockpit.xyz[2] = -0.0029F;
        this.mesh.chunkSetLocate("Z_DropTank", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        f1 = this.fm.EI.engines[0].getPowerOutput();
        this.mesh.chunkSetAngles("Z_Fuel1", cvt((float)Math.sqrt(f1), 0.0F, 1.0F, -59.5F, 223F), 0.0F, 0.0F);
        f1 = cvt(this.fm.M.fuel, 0.0F, 1000F, 0.0F, 270F);
        if(f1 < 45F)
            f1 = cvt(f1, 0.0F, 45F, -58F, 45F);
        f1 += 58F;
        f1 = this.fm.EI.engines[1].getPowerOutput();
        this.mesh.chunkSetAngles("Z_Fuel2", cvt((float)Math.sqrt(f1), 0.0F, 1.0F, -59.5F, 223F), 0.0F, 0.0F);
        f1 = cvt(this.fm.M.fuel, 0.0F, 1000F, 0.0F, 270F);
        if(f1 < 45F)
            f1 = cvt(f1, 0.0F, 45F, -58F, 45F);
        f1 += 58F;
        this.mesh.chunkSetAngles("Z_Fuel3", cvt(this.fm.M.fuel, 0.0F, this.fm.M.maxFuel, 0.0F, 325F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness(), 0.0F, 28F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 289F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 5000F, 0.0F, 289F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", cvt(this.fm.EI.engines[0].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", cvt(this.fm.EI.engines[1].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oxypres1", 142.5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 90F + setNew.azimuth, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 90F + interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", setNew.azimuth, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", cvt(w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        w.set(this.fm.getW());
        this.fm.Or.transform(w);
        this.mesh.chunkSetAngles("Z_TurnBank2", cvt(getBall(7D), -7F, 7F, -15F, 15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3a", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, cvt(this.fm.Or.getTangage(), -45F, 45F, 1.5F, -1.5F));
        this.mesh.chunkSetAngles("Z_Pres1", cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ny1", cvt(this.fm.getOverload(), -4F, 12F, -80.5F, 241.5F), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_GearGreen1", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_GearRed1", this.fm.CT.getGear() < 0.05F || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareFlaps", this.fm.CT.getFlap() > 0.1F);
        this.mesh.chunkVisible("FlareAirBrake", this.fm.CT.AirBrakeControl > 0.01F);
        this.mesh.chunkVisible("Z_LampFuelCf", this.fm.M.fuel < 125F);
        this.mesh.chunkVisible("Z_Trim1", Math.abs(this.fm.CT.getTrimElevatorControl()) < 0.05F);
        this.mesh.chunkVisible("Z_FireLamp", this.fm.AS.astateEngineStates[0] > 2);

        this.mesh.chunkSetAngles("Z_Horizont_1", 0.0F, 0.0F, cvt(this.fm.Or.getKren(), -45F, 45F, -45F, 45F));
        this.mesh.chunkSetAngles("Z_Horizont", 0.0F, -cvt(this.fm.Or.getTangage(), -30F, 30F, -30F, 30F), 0.0F);
        this.mesh.chunkSetAngles("Z_Horizont_Emergency_1", 0.0F, 0.0F, cvt(this.fm.Or.getKren(), -45F, 45F, -45F, 45F));
        this.mesh.chunkSetAngles("Z_Horizont_Emergency", 0.0F, -cvt(this.fm.Or.getTangage(), -30F, 30F, -30F, 30F), 0.0F);
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 2) != 0)
        {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
        }
        if((this.fm.AS.astateCockpitState & 1) != 0)
        {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if((this.fm.AS.astateCockpitState & 4) != 0)
            this.mesh.chunkVisible("XGlassDamage3", true);
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
            this.mesh.chunkVisible("XGlassDamage4", true);
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
    private static final float speedometerScale[] = {
        0.0F, 42F, 65.5F, 88.5F, 111.3F, 134F, 156.5F, 181F, 205F, 227F, 
        249.4F, 271.7F, 294F, 316.5F, 339.5F
    };
    private static final float variometerScale[] = {
        -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 
        124F, 147F, 170F
    };
    private Point3d tmpP;
    private Vector3d tmpV;
}
