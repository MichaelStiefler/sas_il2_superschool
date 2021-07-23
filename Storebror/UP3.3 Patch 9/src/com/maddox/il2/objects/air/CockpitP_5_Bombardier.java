package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitP_5_Bombardier extends CockpitPilot // CockpitBombardier
{
    private class Variables
    {

        float throttle1;
        float mix1;
        float pictElev;
        float pictAiler;
        float pictRudd;
        float compasTangage;
        float compasKren;
        float azimuth;
        float altimeter;
        float turn;
        float dimPos;
        float Airstartr;

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
                setNew.pictElev = 0.8F * setOld.pictElev + 0.2F * fm.CT.ElevatorControl;
                setNew.pictAiler = 0.8F * setOld.pictAiler + 2.0F * fm.CT.AileronControl;
                setNew.pictRudd = 0.8F * setOld.pictRudd + 0.2F * fm.CT.getRudder();
                setNew.throttle1 = 0.9F * setOld.throttle1 + 0.1F * fm.EI.engines[0].getControlThrottle();
                setNew.mix1 = 0.8F * setOld.mix1 + 0.2F * fm.EI.engines[0].getControlMix();
                setNew.altimeter = fm.getAltitude();
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.turn = (12F * setOld.turn + w.z) / 13F;
                if(Math.abs(fm.Or.getKren()) < 20F && Math.abs(fm.Or.getTangage()) < 20F)
                    setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.compasTangage = 0.95F * setOld.compasTangage + 0.05F * cvt(fm.Or.getTangage(), -20F, 20F, -20F, 20F);
                setNew.compasKren = 0.95F * setOld.compasKren + 0.05F * cvt(fm.Or.getKren(), -20F, 20F, -20F, 20F);
                if(cockpitDimControl)
                {
                    if(setNew.dimPos < 1.0F)
                        setNew.dimPos = setOld.dimPos + 0.05F;
                } else
                if(setNew.dimPos > 0.0F)
                    setNew.dimPos = setOld.dimPos - 0.05F;
                if(fm.EI.engines[0].getStage() == 1 || fm.EI.engines[0].getStage() == 2)
                {
                    if(setNew.Airstartr < 1.0F)
                        setNew.Airstartr = setOld.Airstartr + 0.1F;
                    if(setNew.Airstartr > 1.0F)
                        setNew.Airstartr = 1.0F;
                } else
                {
                    if(setNew.Airstartr > 0.0F)
                        setNew.Airstartr = setOld.Airstartr - 0.1F;
                    if(setNew.Airstartr < 0.0F)
                        setNew.Airstartr = 0.0F;
                }
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected void setCameraOffset()
    {
        cameraCenter.add(0.0D, 0.0D, 0.0D);
    }

    public void toggleDim()
    {
        cockpitDimControl = !cockpitDimControl;
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            lights[0].light.setEmit(0.5F, 0.7F);
            lights[1].light.setEmit(0.5F, 0.7F);
            mesh.materialReplace("AL2_add", "AL2_add_light");
            mesh.materialReplace("AL2_na", "AL2_na_light");
            mesh.materialReplace("equip_AN4b", "equip_AN4b_light");
            mesh.materialReplace("equip_AN4c", "equip_AN4c_light");
            mesh.materialReplace("equip_AN4_sh", "equip_AN4_sh_light");
        } else
        {
            lights[0].light.setEmit(0.0F, 0.0F);
            lights[1].light.setEmit(0.0F, 0.0F);
            mesh.materialReplace("AL2_add", "AL2_add");
            mesh.materialReplace("AL2_na", "AL2_na");
            mesh.materialReplace("equip_AN4b", "equip_AN4b");
            mesh.materialReplace("equip_AN4c", "equip_AN4c");
            mesh.materialReplace("equip_AN4_sh", "equip_AN4_sh");
        }
        setNightMats(false);
    }

//    private void retoggleLight()
//    {
//        if(cockpitLightControl)
//            setNightMats(false);
//        else
//            setNightMats(false);
//    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        mesh.chunkVisible("CF_D0_00", hiermesh.isChunkVisible("CF_D0"));
        mesh.chunkVisible("CF_D1_00", hiermesh.isChunkVisible("CF_D1"));
        mesh.chunkVisible("CF_D2_00", hiermesh.isChunkVisible("CF_D2"));
        mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
        mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
        mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
        if(R_5xyz.bChangedPit)
        {
            reflectPlaneToModel();
            R_5xyz.bChangedPit = false;
        }
        resetYPRmodifier();
        float f1 = setNew.pictRudd * 20F;
        mesh.chunkSetAngles("Pedals", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Pedal_tros_L1", -f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Pedal_tros_R1", -f1, 0.0F, 0.0F);
        float f2 = setNew.pictRudd;
        if(f2 > 0.0F)
        {
            mesh.chunkSetAngles("Pedal_tros_L2", -f2 * 22.2F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Pedal_tros_R2", -f2 * 19.1F, 0.0F, 0.0F);
        } else
        {
            mesh.chunkSetAngles("Pedal_tros_L2", -f2 * 19.1F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Pedal_tros_R2", -f2 * 22.2F, 0.0F, 0.0F);
        }
        mesh.chunkSetAngles("zRollerL", 200F * f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("zRollerR", 200F * f2, 0.0F, 0.0F);
        if(tros2Mat != null)
        {
            tros2 = 0.25F * f2;
            tros2Mat.setLayer(0);
            tros2Mat.set((byte)11, tros2);
        }
        float f3 = 0.0F;
        if(setNew.pictElev > 0.0F)
            f3 = 21F * setNew.pictElev;
        else
            f3 = 13F * setNew.pictElev;
        mesh.chunkSetAngles("StickElev", 0.0F, 0.0F, f3);
        mesh.chunkSetAngles("Stick", 0.0F, setNew.pictAiler, 0.0F);
        mesh.chunkSetAngles("StickKardan", 0.0F, 0.0F, -f3 * (float)Math.cos(setNew.pictAiler * 0.01745329F));
        mesh.chunkSetAngles("StickConnctr", -f3 * (float)Math.sin(setNew.pictAiler * 0.01745329F), 0.0F, 0.0F);
        mesh.chunkSetAngles("StickEl_trosL1", 0.0F, 0.0F, -f3);
        mesh.chunkSetAngles("StickEl_trosL2", 0.0F, 0.0F, -f3);
        mesh.chunkSetAngles("StickEl_trosR1", 0.0F, 0.0F, -f3);
        mesh.chunkSetAngles("StickEl_trosR2", 0.0F, 0.0F, -f3);
        float f4 = -50F * setNew.throttle1;
        mesh.chunkSetAngles("zThrotle", 0.0F, 0.0F, f4);
        mesh.chunkSetAngles("Cable_throtle01", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("Cable_throtle02", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("zThrotleNav", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("Cable_throtle03", 0.0F, 0.0F, f4);
        float f5 = -37.5F * setNew.mix1;
        mesh.chunkSetAngles("zMixture", 0.0F, 0.0F, f5);
        mesh.chunkSetAngles("Cable_mixture01", 0.0F, 0.0F, -f5);
        mesh.chunkSetAngles("Cable_mixture02", 0.0F, 0.0F, -f5);
        mesh.chunkSetAngles("zMixtureNav", 0.0F, 0.0F, -f5);
        mesh.chunkSetAngles("Cable_mixture03", 0.0F, 0.0F, f5);
        float f6 = cvt(fm.EI.engines[0].getRPM(), 900F, 1200F, 0.0F, -50F);
        mesh.chunkSetAngles("zIgnition", 0.0F, 0.0F, f6);
        mesh.chunkSetAngles("Cable_Ignition", 0.0F, 0.0F, -f6);
        R_5xyz r_5xyz2 = (R_5xyz)aircraft();
        mesh.chunkSetAngles("Z_AL2c", r_5xyz2.CompassDelta, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_AN4a", 0.0F, -setNew.compasKren, -setNew.compasTangage);
        mesh.chunkSetAngles("Z_AN4b", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Speed2", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 60F, 400F, 0.0F, 17F), IAS_Scale), 0.0F);
        float f7 = getBall(8D);
        mesh.chunkSetAngles("zArr_PioneerBall2", 0.0F, cvt(f7, -4F, 4F, -11F, 11F), 0.0F);
        f7 = cvt(setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        mesh.chunkSetAngles("zArr_Pioneer2", 0.0F, f7, 0.0F);
        mesh.chunkSetAngles("zArr_ClockS2", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zArr_ClockM2", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zArr_ClockH2", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("zArr_Alt2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 12000F, 0.0F, 720F), 0.0F);
        float f8 = 0.0F;
        if(fm.AS.bLandingLightOn)
            f8 = -2.5F;
        if(fm.AS.bNavLightsOn)
            f8--;
        mesh.chunkSetAngles("zArr_Volt", 0.0F, f8, 0.0F);
        mesh.chunkSetAngles("zArr_AirPress250", 0.0F, 0.0F, 153F);
        mesh.chunkSetAngles("zAirstartr", 0.0F, 0.0F, -90F * setNew.Airstartr);
        if(fm.EI.engines[0].getStage() == 1 || fm.EI.engines[0].getStage() == 2)
            mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, -245F * setNew.Airstartr + 5F * World.Rnd().nextFloat());
        else
            mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, 0.0F);
        HierMesh hiermesh = aircraft().hierMesh();
        mesh.chunkVisible("Pilot1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        mesh.chunkVisible("Head1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        mesh.chunkVisible("HMask1_D0", hiermesh.isChunkVisible("HMask1_D0"));
        mesh.chunkVisible("Pilot1_D1", hiermesh.isChunkVisible("Pilot1_D1"));
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Interior_D0", false);
            bNeedSetUp = true;
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Interior_D0", true);
        super.doFocusLeave();
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("xHullDm1", true);
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("xHullDm4", true);
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_ClockH2", false);
                mesh.chunkVisible("zArr_ClockM2", false);
                mesh.chunkVisible("zArr_ClockS2", false);
                mesh.chunkVisible("zArr_ClockDop2", false);
                mesh.materialReplace("Prib_ClockACHO", "Prib_ClockACHO_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("xHullDm5", true);
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_Alt2", false);
                mesh.materialReplace("Prib_Alt6km", "Prib_Alt6km_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("xGlassDm3", true);
            mesh.chunkVisible("xHullDm6", true);
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_Pioneer2", false);
                mesh.chunkVisible("zArr_PioneerBall2", false);
                mesh.materialReplace("Prib_Peoneer2", "Prib_Peoneer2_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("xGlassDm2", true);
            mesh.chunkVisible("xHullDm7", true);
            mesh.materialReplace("Prib_Kpa3Alt", "Prib_Kpa3Alt_dmg");
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_Speed2", false);
                mesh.materialReplace("Prib_Prib40", "Prib_Prib40_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("xGlassDm1", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("xHullDm2", true);
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_Volt", false);
                mesh.materialReplace("Prib_Volt", "Prib_Volt_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("xHullDm3", true);
            mesh.chunkVisible("zArr_AirPress25", false);
            mesh.materialReplace("Prib_Oxy25", "Prib_Oxy25_dmg");
            mesh.chunkVisible("zArr_AirPress250", false);
            mesh.materialReplace("Prib_Oxy250", "Prib_Oxy250_dmg");
        }
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("xOilSplats_D1", true);
    }

    protected void interpTick()
    {
    }

    public CockpitP_5_Bombardier()
    {
        super("3DO/Cockpit/R-5-Bombardier/hier-P5-Co.him", "u2");
        bNeedSetUp = true;
//        bEntered = false;
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        tros2 = 0.5F;
        tros2Mat = null;
        int i = -1;
        i = mesh.materialFind("tros2");
        if(i != -1)
        {
            tros2Mat = mesh.material(i);
            tros2Mat.setLayer(0);
        }
        cockpitNightMats = new String[0];
        for(int j = 0; j < 2; j++)
        {
            HookNamed hooknamed = new HookNamed(mesh, "LAMP0" + (j + 3));
            Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
            lights[j] = new LightPointActor(new LightPoint(), loc.getPoint());
            lights[j].light.setColor(0.8980392F, 0.8117647F, 0.6235294F);
            lights[j].light.setEmit(0.0F, 0.0F);
            pos.base().draw.lightMap().put("LAMP0" + (j + 3), lights[j]);
        }

        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        hidePilot = true;
        limits6DoF = (new float[] {
            0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.03F, -0.03F
        });
    }

    protected float waypointAzimuth()
    {
        return this.waypointAzimuthInvertMinus(10F);
    }

    private boolean bNeedSetUp;
//    private boolean bEntered;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float tros2;
    private Mat tros2Mat;
    private LightPointActor lights[] = {
        null, null
    };
    private static final float IAS_Scale[] = {
        0.0F, 13.7F, 29.5F, 49.5F, 72.2F, 98.8F, 127.09F, 161.5F, 192.5F, 229F, 
        262.6F, 299F, 335.29F, 370.29F, 405.29F, 439.59F, 471.29F, 504.69F
    };

    static 
    {
        Property.set(CockpitP_5_Bombardier.class, "aiTuretNum", -1);
        Property.set(CockpitP_5_Bombardier.class, "weaponControlNum", 3);
        Property.set(CockpitP_5_Bombardier.class, "astatePilotIndx", 1);
        Property.set(CockpitP_5_Bombardier.class, "normZNs", new float[] {
            0.3F, 0.7F, 1.1F, 0.7F
        });
        Property.set(CockpitP_5_Bombardier.class, "gsZN", 0.6F);
    }
}
