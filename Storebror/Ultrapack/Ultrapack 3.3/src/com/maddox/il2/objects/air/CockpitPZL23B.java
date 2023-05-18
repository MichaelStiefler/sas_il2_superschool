package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitPZL23B extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(bNeedSetUp)
            {
                reflectPlaneMats();
                bNeedSetUp = false;
            }
            if(PZL23B.bChangedPit)
            {
                reflectPlaneToModel();
                PZL23B.bChangedPit = false;
            }
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            if(Math.abs(fm.Or.getKren()) < 30F)
                setNew.azimuth = (35F * setOld.azimuth + fm.Or.azimut()) / 36F;
            if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                setOld.azimuth -= 360F;
            if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                setOld.azimuth += 360F;
            setNew.mix = (10F * setOld.mix + fm.EI.engines[0].getControlMix()) / 11F;
            setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
            w.set(fm.getW());
            fm.Or.transform(w);
            setNew.turn = (33F * setOld.turn + w.z) / 34F;
            setNew.power = 0.85F * setOld.power + fm.EI.engines[0].getPowerOutput() * 0.15F;
            setNew.fuelpressure = 0.9F * setOld.fuelpressure + (fm.M.fuel <= 1.0F || fm.EI.engines[0].getStage() != 6 ? 0.0F : 0.026F * (10F + (float)Math.sqrt(setNew.power))) * 0.1F;
            setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            mesh.chunkSetAngles("Turret1A", 0.0F, aircraft().FM.turret[0].tu[0], 0.0F);
            mesh.chunkSetAngles("Turret1B", 0.0F, aircraft().FM.turret[0].tu[1], 0.0F);
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float altimeter;
        float azimuth;
        float mix;
        float throttle;
        float turn;
        float power;
        float fuelpressure;
        float vspeed;

        private Variables()
        {
        }

    }


    public CockpitPZL23B()
    {
        super("3DO/Cockpit/PZL23B/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        interpPut(new Interpolater(), null, Time.current(), null);
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(0.8980392F, 0.8117647F, 0.9235294F);
        light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(0.8980392F, 0.8117647F, 0.9235294F);
        light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        this.cockpitNightMats = (new String[] {
            "dials1", "dials2", "dials3", "dials4", "dials5", "gauges"
        });
        setNightMats(false);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 1000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 500F, 0.0F, 25F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.72421F, 1.27579F, -160F, 160F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zCompass1", 0.0F, 90F + interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zCompass2", 0.0F, 90F + interp(-setNew.azimuth, -setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 16F * (pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl), 12F * (pictElev = 0.85F * pictElev + 0.15F * this.fm.CT.ElevatorControl));
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, cvt(setNew.fuelpressure, 0.0F, 0.6F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 268F), 0.0F);
        this.mesh.chunkSetAngles("zOilIn", 0.0F, floatindex(cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 140F, 0.0F, 7F), oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, floatindex(cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 7F), oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zMagnetoSwitch", cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(this.fm.Or.getTangage(), -20F, 20F, 0.0485F, -0.0485F);
        this.mesh.chunkSetLocate("zPitch1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zRPM", 0.0F, floatindex(cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3200F, 0.0F, 16F), rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Rudder", 26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, cvt(setNew.turn, -0.3F, 0.3F, -1.8F, 1.8F), 0.0F);
        this.mesh.chunkSetAngles("zSlide1", 0.0F, cvt(getBall(3D), -3F, 3F, -9F, 9F), 0.0F);
        this.mesh.chunkSetAngles("zSlide2", 0.0F, cvt(getBall(3D), -3F, 3F, -6F, 6F), 0.0F);
        this.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, -33F + 30F * interp(setNew.mix, setOld.mix, f));
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -55F + 50F * interp(setNew.throttle, setOld.throttle, f));
        this.mesh.chunkSetAngles("zAH", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("zVspeed", 0.0F, cvt(setNew.vspeed, -18F, 18F, -180F, 180F), 0.0F);
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        this.mesh.chunkVisible("Engine1_D0", hiermesh.isChunkVisible("Engine1_D0"));
        this.mesh.chunkVisible("Engine1_D1", hiermesh.isChunkVisible("Engine1_D1"));
        this.mesh.chunkVisible("Engine1_D2", hiermesh.isChunkVisible("Engine1_D2"));
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("Pilot2_D0", hiermesh.isChunkVisible("Pilot2_D0"));
        this.mesh.chunkVisible("Pilot2_D1", hiermesh.isChunkVisible("Pilot2_D1"));
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
        {
            light1.light.setEmit(0.3F, 0.3F);
            light2.light.setEmit(0.3F, 0.3F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private LightPointActor light1;
    private LightPointActor light2;
    private Vector3f w;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private static final float speedometerScale[] = {
        0.0F, 1.0F, 3F, 6.2F, 12F, 26.5F, 39F, 51F, 67.5F, 85.5F, 
        108F, 131.5F, 154F, 180F, 205.7F, 228.2F, 251F, 272.9F, 291.9F, 314.5F, 
        336.5F, 354F, 360F, 363F, 364F, 365F
    };
    private static final float rpmScale[] = {
        0.0F, 12F, 25F, 47F, 68F, 90F, 112F, 134F, 157F, 180F, 
        203F, 226F, 248F, 270F, 292F, 314F, 335F
    };
    private static final float oilTempScale[] = {
        0.0F, 20F, 40F, 80F, 120F, 160F, 240F, 320F
    };
}
