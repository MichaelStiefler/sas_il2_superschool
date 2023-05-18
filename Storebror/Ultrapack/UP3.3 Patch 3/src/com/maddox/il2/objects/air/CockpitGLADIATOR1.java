package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitGLADIATOR1 extends CockpitPilot
{
    private class Variables
    {
        float altimeter;
        float azimuth;
        float throttle;
        float mix;
        float prop;
        float turn;
        float vspeed;
        float stbyPosition;
        private Variables()
        {
        }
    }

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(bNeedSetUp)
            {
                reflectPlaneMats();
                bNeedSetUp = false;
            }
            if(((GLADIATOR1)aircraft()).bChangedPit)
            {
                reflectPlaneToModel();
                ((GLADIATOR1)aircraft()).bChangedPit = false;
            }
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            if((fm.AS.astateCockpitState & 2) != 0 && setNew.stbyPosition < 1.0F)
            {
                setNew.stbyPosition = setOld.stbyPosition + 0.0125F;
                setOld.stbyPosition = setNew.stbyPosition;
            }
            setNew.altimeter = fm.getAltitude();
            if(Math.abs(fm.Or.getKren()) < 30F)
                setNew.azimuth = (21F * setOld.azimuth + fm.Or.azimut()) / 22F;
            if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                setOld.azimuth -= 360F;
            if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                setOld.azimuth += 360F;
            setNew.throttle = (10F * setOld.throttle + fm.EI.engines[0].getControlThrottle()) / 11F;
            setNew.mix = (10F * setOld.mix + fm.EI.engines[0].getControlMix()) / 11F;
            setNew.prop = setOld.prop;
            if(setNew.prop < fm.EI.engines[0].getControlProp() - 0.01F)
                setNew.prop += 0.0025F;
            if(setNew.prop > fm.EI.engines[0].getControlProp() + 0.01F)
                setNew.prop -= 0.0025F;
            w.set(fm.getW());
            fm.Or.transform(w);
            setNew.turn = (12F * setOld.turn + w.z) / 13F;
            setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            pictSupc = 0.8F * pictSupc + 0.2F * (float)fm.EI.engines[0].getControlCompressor();
            return true;
        }

        Interpolater()
        {
        }
    }


    public CockpitGLADIATOR1()
    {
        super("3DO/Cockpit/Gladiator/hier.him", "u2");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictSupc = 0.0F;
        cockpitNightMats = (new String[] {
            "PRIB_01", "PRIB_02", "PRIB_03", "PRIB_04", "PRIB_05", "PRIB_06", "PRIB_07"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        if(((GLADIATOR1)aircraft()).bChangedPit)
        {
            reflectPlaneToModel();
            ((GLADIATOR1)aircraft()).bChangedPit = false;
        }
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.65F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("FONAR_GLASS2", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_ManPrs1", 0.0F, 0.0F, 4F - cvt(pictManifold = 0.85F * pictManifold + 0.15F * fm.EI.engines[0].getManifoldPressure(), 0.600034F, 1.66661F, -124.8F, 208F));
        mesh.chunkSetAngles("Flap01_D0", 0.0F, -50F * fm.CT.getFlap(), 0.0F);
        mesh.chunkSetAngles("Flap04_D0", 0.0F, -50F * fm.CT.getFlap(), 0.0F);
        mesh.chunkSetAngles("Z_Column", 0.0F, 14F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 14F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl));
        mesh.chunkSetAngles("Z_PedalStrut", 15F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_LeftPedal", -15F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RightPedal", -15F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Throttle", 0.0F, 0.0F, -51.8F * interp(setNew.throttle, setOld.throttle, f));
        mesh.chunkSetAngles("Z_Mix", 0.0F, 0.0F, -52.3F * interp(setNew.mix, setOld.mix, f));
        mesh.chunkSetAngles("Z_Altimeter1", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
        mesh.chunkSetAngles("Z_Altimeter2", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
        mesh.chunkSetAngles("Z_Speedometer1", 0.0F, 0.0F, -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeed()), 0.0F, 180.0555F, 0.0F, 35F), speedometerScale));
        mesh.chunkSetAngles("Z_Compass1", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPM1", 0.0F, 0.0F, cvt(fm.EI.engines[0].getRPM(), 1200F, 3400F, 0.0F, -328F));
        mesh.chunkSetAngles("Z_OilPress1", 0.0F, 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 12F, 0.0F, -309F));
        mesh.chunkSetAngles("Z_Hour1", 0.0F, 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F));
        mesh.chunkSetAngles("Z_Minute1", 0.0F, 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F));
        mesh.chunkSetAngles("Z_Second1", 0.0F, 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, -360F));
        mesh.chunkSetAngles("Z_FuelPress1", 0.0F, 0.0F, cvt(fm.M.fuel, 0.0F, 282F, 0.0F, 71F));
        mesh.chunkSetAngles("Z_OilTemp1", 0.0F, 0.0F, cvt(fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, -315.5F));
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(fm.getAOA(), -20F, 20F, 0.042F, -0.042F);
        mesh.chunkSetLocate("Z_AoA1", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Turn1", 0.0F, 0.0F, cvt(setNew.turn, -0.2F, 0.2F, -22.5F, 22.5F));
        mesh.chunkSetAngles("Turn2", 0.0F, 0.0F, cvt(getBall(8D), -8F, 8F, 16.9F, -16.9F));
        mesh.chunkSetAngles("Z_Climb1", 0.0F, 0.0F, cvt(setNew.vspeed, -15F, 15F, 180F, -180F));
        mesh.chunkSetAngles("Z_Oxypres1", 0.0F, 0.0F, -260F);
        mesh.chunkSetAngles("Z_Oxypres2", 0.0F, 0.0F, -320F);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("Z_Z_RETICLE", false);
            mesh.chunkVisible("Z_Z_MASK", false);
            mesh.chunkVisible("Revi_D0", false);
            mesh.chunkVisible("Revi_D1", true);
        }
        if((fm.AS.astateCockpitState & 1) != 0);
        if((fm.AS.astateCockpitState & 0x40) != 0);
        if((fm.AS.astateCockpitState & 4) != 0);
        if((fm.AS.astateCockpitState & 8) != 0);
        if((fm.AS.astateCockpitState & 0x80) != 0);
        if((fm.AS.astateCockpitState & 0x10) != 0);
        if((fm.AS.astateCockpitState & 0x20) != 0);
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        mesh.chunkVisible("WingLMid_D3", hiermesh.isChunkVisible("WingLMid_D3"));
        mesh.chunkVisible("WingLMid_CAP", hiermesh.isChunkVisible("WingLMid_CAP"));
        mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        mesh.chunkVisible("WingRMid_D3", hiermesh.isChunkVisible("WingRMid_D3"));
        mesh.chunkVisible("WingRMid_CAP", hiermesh.isChunkVisible("WingRMid_CAP"));
        mesh.chunkVisible("WingLOut_D0", hiermesh.isChunkVisible("WingLOut_D0"));
        mesh.chunkVisible("WingLOut_D1", hiermesh.isChunkVisible("WingLOut_D1"));
        mesh.chunkVisible("WingLOut_D2", hiermesh.isChunkVisible("WingLOut_D2"));
        mesh.chunkVisible("WingLOut_D3", hiermesh.isChunkVisible("WingLOut_D3"));
        mesh.chunkVisible("WingROut_D0", hiermesh.isChunkVisible("WingROut_D0"));
        mesh.chunkVisible("WingROut_D1", hiermesh.isChunkVisible("WingROut_D1"));
        mesh.chunkVisible("WingROut_D2", hiermesh.isChunkVisible("WingROut_D2"));
        mesh.chunkVisible("WingROut_D3", hiermesh.isChunkVisible("WingROut_D3"));
        mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        mesh.chunkVisible("CF_D3", hiermesh.isChunkVisible("CF_D3"));
        mesh.chunkVisible("WireL_D0", hiermesh.isChunkVisible("WireL_D0"));
        mesh.chunkVisible("WireR_D0", hiermesh.isChunkVisible("WireR_D0"));
        mesh.chunkVisible("Flap01_D0", hiermesh.isChunkVisible("Flap01_D0"));
        mesh.chunkVisible("Flap04_D0", hiermesh.isChunkVisible("Flap04_D0"));
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        mesh.materialReplace("Matt2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
        mesh.materialReplace("Matt2D2o", mat);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private float pictSupc;
    private float pictManifold;
    private static final float speedometerScale[] = {
        0.0F, 0.0F, 1.0F, 3F, 7.5F, 34.5F, 46F, 63F, 76F, 94F, 
        112.5F, 131F, 150F, 168.5F, 187F, 203F, 222F, 242.5F, 258.5F, 277F, 
        297F, 315.5F, 340F, 360F, 376.5F, 392F, 407F, 423.5F, 442F, 459F, 
        476F, 492.5F, 513F, 534.5F, 552F, 569.5F
    };
    private Vector3f w;
}
