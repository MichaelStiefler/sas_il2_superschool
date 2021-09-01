package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitFireflyUpdate extends CockpitPilot
{
    private class Variables
    {

        float throttle;
        float prop;
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
                setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
                setNew.prop = (10F * setOld.prop + fm.EI.engines[0].getControlProp()) / 11F;
                setNew.altimeter = fm.getAltitude();
                if(Math.abs(fm.Or.getKren()) < 30F)
                    setNew.azimuth = (35F * setOld.azimuth + fm.Or.azimut()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    public CockpitFireflyUpdate()
    {
        super("3DO/Cockpit/Firefly-P/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        bNeedSetUp = true;
        this.cockpitNightMats = (new String[] {
            "Dials", "Compa", "BORT4", "BORT5"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(this.acoustics != null)
            this.acoustics.globFX = new ReverbFXRoom(0.2F);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.5F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(fm.CT.arrestorControl, 0.01F, 0.99F, 0.0F, 0.03F);
        this.mesh.chunkVisible("XLampArrest", fm.CT.getArrestor() > 0.9F);
        this.mesh.chunkVisible("XLampGearUpL", fm.CT.getGear() == 0.0F && fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearUpR", fm.CT.getGear() == 0.0F && fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearUpC", fm.CT.getGear() == 0.0F && fm.Gears.cgear);
        this.mesh.chunkVisible("XLampGearDownL", fm.CT.getGear() == 1.0F && fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownR", fm.CT.getGear() == 1.0F && fm.Gears.rgear);
        resetYPRmodifier();
        this.mesh.chunkSetAngles("Z_BasePedal", 20F * fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 0.0F, -20F * fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 0.0F, -20F * fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Columnbase", 16F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 45F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(pictAiler, -1F, 1.0F, -0.027F, 0.027F);
        this.mesh.chunkSetLocate("Z_Shlang01", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -Cockpit.xyz[2];
        this.mesh.chunkSetLocate("Z_Shlang02", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("RUD", 0.0F, -75F * interp(setNew.throttle, setOld.throttle, f), 0.0F);
        this.mesh.chunkSetAngles("PROP", 0.0F, -70F * interp(setNew.prop, setOld.prop, f), 0.0F);
        this.mesh.chunkSetAngles("DIRECTOR", 0.0F, interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("COMPASS", 0.0F, interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("STRELKA_ALT1", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
        this.mesh.chunkSetAngles("STRELKA_ALT2", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
        this.mesh.chunkSetAngles("STRELKA_BOOST", 0.0F, 0.0F, -cvt(fm.EI.engines[0].getManifoldPressure(), 0.7242097F, 1.620528F, -111.5F, 221F));
        this.mesh.chunkSetAngles("STRELKA_FUEL", cvt(fm.M.fuel / 0.725F, 0.0F, 600F, 0.0F, 200F), 0.0F, 0.0F);
//        this.mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -floatindex(cvt(fm.EI.engines[0].getRPM(), 1000F, 5000F, 2.0F, 10F), rpmScale));
        this.mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -cvt(fm.EI.engines[0].getRPM() % 1000F, 0F, 1000F, 0F, 360F));
        this.mesh.chunkSetAngles("STRELKA_RPM2", 0.0F, 0.0F, -cvt(fm.EI.engines[0].getRPM() / 1000F, 0F, 10F, 0F, 360F));
        this.mesh.chunkSetAngles("STRELKA_OIL", 0.0F, 0.0F, -cvt(fm.EI.engines[0].tOilOut, 40F, 100F, 0.0F, 270F));
        resetYPRmodifier();
        w.set(this.fm.getW());
        fm.Or.transform(w);
        this.mesh.chunkSetAngles("STRELKA_DOWN", 0.0F, 0.0F, -cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, -48F, 48F));
        this.mesh.chunkSetAngles("STRELKA_UP", 0.0F, 0.0F, -cvt(getBall(8D), -8F, 8F, 35F, -35F));
        this.mesh.chunkSetAngles("STRELKA_VL", 0.0F, 0.0F, -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, this.fm.getSpeed()), 0.0F, 180.0555F, 0.0F, 35F), speedometerScale));
        this.mesh.chunkSetAngles("STRELKA_VY", 0.0F, 0.0F, -floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale));
        resetYPRmodifier();
    }

    protected void reflectPlaneMats()
    {
        this.mesh.materialReplace("Gloss1D0o", aircraft().hierMesh().material(aircraft().hierMesh().materialFind("Gloss1D0o")));
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 1) != 0)
            this.mesh.chunkVisible("XGlassDamage1", true);
        if((fm.AS.astateCockpitState & 2) != 0)
            this.mesh.chunkVisible("XGlassDamage2", true);
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
    private boolean bNeedSetUp;
    private static final float speedometerScale[] = {
        0.0F, 0.0F, 1.0F, 3F, 7.5F, 34.5F, 46F, 63F, 76F, 94F, 
        112.5F, 131F, 150F, 168.5F, 187F, 203F, 222F, 242.5F, 258.5F, 277F, 
        297F, 315.5F, 340F, 360F, 376.5F, 392F, 407F, 423.5F, 442F, 459F, 
        476F, 492.5F, 513F, 534.5F, 552F, 569.5F
    };
//    private static final float rpmScale[] = {
//        0.0F, 0.0F, 0.0F, 22F, 58F, 103.5F, 152.5F, 193.5F, 245F, 281.5F, 
//        311.5F
//    };
    private static final float variometerScale[] = {
        -158F, -110F, -63.5F, -29F, 0.0F, 29F, 63.5F, 110F, 158F
    };
}
