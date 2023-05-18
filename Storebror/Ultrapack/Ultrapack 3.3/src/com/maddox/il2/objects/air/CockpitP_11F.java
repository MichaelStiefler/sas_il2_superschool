package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitP_11F extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(bNeedSetUp)
            {
                reflectPlaneMats();
                reflectCockpitMats();
                bNeedSetUp = false;
            }
            if(P_11.bChangedPit)
            {
                reflectPlaneToModel();
                P_11.bChangedPit = false;
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
            setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
            w.set(fm.getW());
            fm.Or.transform(w);
            setNew.turn = (33F * setOld.turn + w.z) / 34F;
            setNew.power = 0.85F * setOld.power + fm.EI.engines[0].getPowerOutput() * 0.15F;
            setNew.fuelpressure = 0.9F * setOld.fuelpressure + (fm.M.fuel > 1.0F && fm.EI.engines[0].getStage() == 6 ? 0.026F * (10F + (float)Math.sqrt(setNew.power)) : 0.0F) * 0.1F;
            if(fm.getAltitude() > 5000F)
                CockpitP_11F.oxyf = 12F;
            else
            if(fm.getAltitude() > 3000F)
                CockpitP_11F.oxyf = (fm.getAltitude() - 2600F) / 200F;
            if(fm.getAltitude() > 3000F)
                CockpitP_11F.oxyp = CockpitP_11F.oxyp <= 0.0F ? 0.0F : CockpitP_11F.oxyp - 1E-006F * CockpitP_11F.oxyf;
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
        float throttle;
        float turn;
        float power;
        float fuelpressure;

        private Variables()
        {
        }

    }


    public CockpitP_11F()
    {
        super("3DO/Cockpit/P-11f/hier.him", "u2");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        light1 = new LightPointActor(new LightPoint(), new Point3d(-0.92D, -0.003D, 0.625D));
        light1.light.setColor(0.96F, 0.87F, 0.74F);
        light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectCockpitMats();
            bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 420F, 0.0F, 21F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.72421F, 1.27579F, -160F, 160F), 0.0F);
        this.mesh.chunkSetAngles("zSecond", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, 90F - interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 24F * (pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl), 24F * (pictElev = 0.85F * pictElev + 0.15F * this.fm.CT.ElevatorControl));
        this.mesh.chunkSetAngles("Column_Cam", 0.0F, 24F * pictAiler, 0.0F);
        this.mesh.chunkSetAngles("Column_Rod", 0.0F, -24F * pictAiler, 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, cvt(setNew.fuelpressure, 0.0F, 0.5F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zFuelQty", 0.0F, cvt(this.fm.M.fuel * 1.25F, 50F, 310F, 0.0F, 275F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zOilIn", 0.0F, floatindex(cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 140F, 0.0F, 7F), oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, floatindex(cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 7F), oilTempScale), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(this.fm.Or.getTangage(), -20F, 20F, 0.0385F, -0.0385F);
        this.mesh.chunkSetLocate("zPitch", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zRPM", 0.0F, floatindex(cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 15F), rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Rudder", 26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableL", -26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableR", -26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, cvt(setNew.turn, -0.6F, 0.6F, 1.8F, -1.8F), 0.0F);
        this.mesh.chunkSetAngles("zSlide", 0.0F, cvt(getBall(7D), -7F, 7F, -10F, 10F), 0.0F);
        this.mesh.chunkSetAngles("Boost", 0.0F, 0.0F, -90F + 90F * interp(setNew.throttle, setOld.throttle, f));
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -90F + 90F * interp(setNew.throttle, setOld.throttle, f));
        this.mesh.chunkSetAngles("zU1", 0.0F, 0.0F, cvt(oxyp, 0.0F, 1.0F, 0.0F, 260F));
        this.mesh.chunkSetAngles("zU2", 0.0F, 0.0F, cvt(oxyf, 0.0F, 12F, 0.0F, 260F));
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Cart_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(Actor.isAlive(aircraft()))
            aircraft().hierMesh().chunkVisible("Cart_D0", true);
        super.doFocusLeave();
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_CAP", hiermesh.isChunkVisible("WingLIn_CAP"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_CAP", hiermesh.isChunkVisible("WingRIn_CAP"));
        this.mesh.chunkVisible("WingLOut_CAP", hiermesh.isChunkVisible("WingLOut_CAP"));
        this.mesh.chunkVisible("WingROut_CAP", hiermesh.isChunkVisible("WingROut_CAP"));
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
    }

    private void reflectCockpitMats()
    {
        int i = this.mesh.materialFind("dials3_lit");
        mat1_lit = this.mesh.material(i);
        int j = this.mesh.materialFind("ldials_lit");
        mat2_lit = this.mesh.material(j);
        int k = this.mesh.materialFind("sdials_lit");
        mat3_lit = this.mesh.material(k);
        int l = this.mesh.materialFind("dials4_lit");
        mat4_lit = this.mesh.material(l);
        setLitMats(false);
    }

    public void reflectCockpitState()
    {
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
        {
            setLitMats(true);
            light1.light.setEmit(0.3F, 0.05F);
        } else
        {
            setLitMats(false);
            light1.light.setEmit(0.0F, 0.0F);
        }
    }

    private void setLitMats(boolean flag)
    {
        shine = flag ? 0.5F : 0.1F;
        if(mat1_lit.isValidLayer(0))
        {
            mat1_lit.setLayer(0);
            mat1_lit.set((byte)24, shine);
            mat1_lit.set((short)0, true);
        }
        if(mat2_lit.isValidLayer(0))
        {
            mat2_lit.setLayer(0);
            mat2_lit.set((byte)24, shine);
            mat2_lit.set((short)0, true);
        }
        if(mat3_lit.isValidLayer(0))
        {
            mat3_lit.setLayer(0);
            mat3_lit.set((byte)24, shine);
            mat3_lit.set((short)0, true);
        }
        if(mat4_lit.isValidLayer(0))
        {
            mat4_lit.setLayer(0);
            mat4_lit.set((byte)24, shine);
            mat4_lit.set((short)0, true);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private Vector3f w;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private LightPointActor light1;
    private static float shine;
    private static Mat mat1_lit;
    private static Mat mat2_lit;
    private static Mat mat3_lit;
    private static Mat mat4_lit;
    private static float oxyf = 0.0F;
    private static float oxyp = 1.0F;
    private static final float speedometerScale[] = {
        0.0F, 3F, 6F, 9F, 13F, 28F, 41.5F, 54F, 71.5F, 90F, 
        112.5F, 135F, 156F, 180F, 203.5F, 224.5F, 247F, 270F, 289.5F, 313.5F, 
        336F, 358.5F
    };
    private static final float rpmScale[] = {
        0.0F, 10F, 21F, 36F, 51F, 66F, 87F, 109F, 133F, 156F, 
        184F, 211F, 241F, 270F, 300F, 330F
    };
    private static final float oilTempScale[] = {
        0.0F, 18F, 36F, 63F, 115F, 180F, 245F, 351F
    };
}
