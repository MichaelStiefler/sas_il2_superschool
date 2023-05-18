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
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitU_2VSLNB extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            HierMesh hiermesh = aircraft().hierMesh();
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.pictAiler = 0.85F * setOld.pictAiler + 0.15F * cvt(fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F);
            setNew.pictElev = 0.85F * setOld.pictElev + 0.15F * cvt(fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F);
            setNew.pictRudd = 0.85F * setOld.pictRudd + 0.15F * cvt(fm.CT.getRudder(), -1F, 1.0F, -1F, 1.0F);
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
            setNew.throttle1 = 0.9F * setOld.throttle1 + 0.1F * fm.EI.engines[0].getControlThrottle();
            setNew.mix1 = 0.8F * setOld.mix1 + 0.2F * fm.EI.engines[0].getControlMix();
            setNew.altimeter = fm.getAltitude();
            setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            float f = 57.3F * fm.EI.engines[0].getw();
            f %= 2880F;
            f /= 2880F;
            if(f <= 0.5F)
                f *= 2.0F;
            else
                f = f * 2.0F - 2.0F;
            f *= 1200F;
            propPos = (propPos + f * U_2xyz.updatef) % 360F;
            if(fm.EI.engines[0].getRPM() > 0.0F)
            {
                if(fm.EI.engines[0].getRPM() < 60F)
                {
                    if(propPos > 20F && propPos < 50F)
                        setNew.EngK2 = -15F;
                    setNew.EngK1 = 0.0F;
                    if(propPos > 50F && propPos < 80F)
                    {
                        setNew.EngK2 = 0.0F;
                        setNew.EngK1 = -15F;
                    }
                    if(propPos > 80F)
                    {
                        setNew.EngK2 = 0.0F;
                        setNew.EngK1 = 0.0F;
                    }
                } else
                {
                    setNew.EngK2 = -15F * World.Rnd().nextFloat();
                    setNew.EngK1 = -15F * World.Rnd().nextFloat();
                }
            } else
            {
                setNew.EngK2 = 0.0F;
                setNew.EngK1 = 0.0F;
            }
            if((double)u_2xyz.getGunnerAnimation() < 1.0D)
            {
                if(!Main3D.cur3D().isViewOutside())
                {
                    hiermesh.chunkVisible("Turret1A_FAKE", false);
                    hiermesh.chunkVisible("Turret1B_FAKE", false);
                } else
                {
                    hiermesh.chunkVisible("Turret1A_FAKE", true);
                    hiermesh.chunkVisible("Turret1B_FAKE", true);
                }
                hiermesh.chunkVisible("Turret1A_D0", false);
                hiermesh.chunkVisible("Turret1B_D0", false);
                moveGunner();
            } else
            {
                hiermesh.chunkVisible("Turret1A_FAKE", false);
                hiermesh.chunkVisible("Turret1B_FAKE", false);
                hiermesh.chunkVisible("Turret1A_D0", true);
                hiermesh.chunkVisible("Turret1B_D0", !u_2xyz.bMultiFunction || !u_2xyz.isPlayerMannedTurret());
                mesh.chunkSetAngles("zTurret1A", -aircraft().FM.turret[0].tu[0], 0.0F, 0.0F);
                mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -aircraft().FM.turret[0].tu[1]);
            }
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
        float throttle1;
        float mix1;
        float turn;
        float vspeed;
        float pictElev;
        float pictAiler;
        float pictRudd;
        float compasTangage;
        float compasKren;
        float EngK1;
        float EngK2;

        private Variables()
        {
        }

    }


    protected void setCameraOffset()
    {
        cameraCenter.add(0.0D, 0.0D, 0.0D);
    }

    public CockpitU_2VSLNB()
    {
        super("3DO/Cockpit/U_2VSLNB/hier.him", "i16");
        u_2xyz = (U_2xyz)aircraft();
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        cockpitNightMats = new String[0];
        for(int i = 0; i < 2; i++)
        {
            HookNamed hooknamed = new HookNamed(mesh, "LAMP0" + (i + 1));
            Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
            lights[i] = new LightPointActor(new LightPoint(), loc.getPoint());
            lights[i].light.setColor(0.8980392F, 0.8117647F, 0.6235294F);
            lights[i].light.setEmit(0.0F, 0.0F);
            pos.base().draw.lightMap().put("LAMP0" + (i + 1), lights[i]);
        }

        setNightMats(false);
        interpPut(new Interpolater(), (Object)null, Time.current(), (Message)null);
        limits6DoF = (new float[] {
            0.7F, 0.055F, -0.07F, 0.11F, 0.0F, -0.12F, 0.03F, -0.03F
        });
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
        if(U_2xyz.bChangedPit)
        {
            reflectPlaneToModel();
            U_2xyz.bChangedPit = false;
        }
        float f1 = 15F * setNew.pictAiler;
        float f2 = 0.0F;
        if(setNew.pictElev > 0.0F)
            f2 = 15F * setNew.pictElev;
        else
            f2 = 13F * setNew.pictElev;
        mesh.chunkSetAngles("Z_Stick", f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Stick_e", 0.0F, f1, 0.0F);
        mesh.chunkSetAngles("Z_Stick_d", 0.0F, 0.0F, -f2);
        float f3 = 15F * setNew.pictRudd;
        mesh.chunkSetAngles("Z_Pedals", f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Pedals1", f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Pedals2", -f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_PedalR", -f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_PedalL", -f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_PedalsN", f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("zKI6a", 0.0F, setNew.compasTangage, -setNew.compasKren);
        mesh.chunkSetAngles("zAzimuth", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        float f4 = getBall(8D);
        mesh.chunkSetAngles("zArr_PioneerBal", cvt(-f4, -4F, 4F, -7.5F, 7.5F), 0.0F, 0.0F);
        f4 = cvt(setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        mesh.chunkSetAngles("zArr_Pioneer", -f4, 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = -cvt(fm.Or.getTangage(), -45F, 45F, 0.017F, -0.017F);
        Cockpit.ypr[0] = fm.Or.getKren();
        mesh.chunkSetLocate("zArr_AG", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("zArr_AG1", fm.Or.getKren(), 0.0F, 0.0F);
        float f5 = -50F * setNew.throttle1;
        mesh.chunkSetAngles("Z_Throtle", f5, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Throtle1", 0.0F, 0.0F, f5 / 10F);
        mesh.chunkSetAngles("Z_Throtle2", 0.0F, 0.0F, -f5);
        float f6 = 30F * setNew.mix1;
        mesh.chunkSetAngles("Z_Mixture", f6, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Mixture1", f6 / 15F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Mixture2", 0.0F, 0.0F, -f6);
        mesh.chunkSetAngles("zArr_RPMB", cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_RPMS", cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Speed", -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 15F), IAS_Scale), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_TempL", cvt(fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, -285F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_TempR", cvt(fm.EI.engines[0].tOilIn, 0.0F, 125F, 0.0F, -285F), 0.0F, 0.0F);
        float f7 = cvt(fm.EI.engines[0].getRPM(), 0.0F, 1100F, 0.0F, 4.5F);
        mesh.chunkSetAngles("zArr_OilPres", cvt(f7, 0.0F, 15F, 0.0F, -268F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Alt_m", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Alt_km", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Fuel", cvt(fm.M.fuel, 0.0F, 100F, 0.0F, -305F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Variom", cvt(setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F, 0.0F);
        f1 = cvt(fm.EI.engines[0].getRPM(), 0.0F, 1700F, 0.0F, 10F);
        if(fm.AS.bLandingLightOn)
            f1--;
        if(fm.AS.bNavLightsOn)
            f1 -= 2.5F;
        mesh.chunkSetAngles("zArr_Volt", -f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("z_SW_Magneto", cvt(fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 87F), 0.0F, 0.0F);
        mesh.chunkSetAngles("z_SW_ANO", fm.AS.bNavLightsOn ? -30F : 30F, 0.0F, 0.0F);
        mesh.chunkSetAngles("z_SW_Landing", fm.AS.bLandingLightOn ? -30F : 30F, 0.0F, 0.0F);
        mesh.chunkSetAngles("z_EngineK2", 0.0F, -setNew.EngK2, 0.0F);
        mesh.chunkSetAngles("z_EngineK1", 0.0F, setNew.EngK1, 0.0F);
        HierMesh hiermesh = aircraft().hierMesh();
        mesh.chunkVisible("Pilot2_D0", hiermesh.isChunkVisible("Pilot2_D0"));
        mesh.chunkVisible("Pilot3_D0", hiermesh.isChunkVisible("Pilot3_D0"));
        mesh.chunkVisible("Pilot2_D1", hiermesh.isChunkVisible("Pilot2_D1"));
        mesh.chunkVisible("Pilot3_D1", hiermesh.isChunkVisible("Pilot3_D1"));
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("xHullDm1", true);
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_Alt_m", false);
                mesh.chunkVisible("zArr_Alt_km", false);
                mesh.materialReplace("Alt", "Alt_dmg");
            }
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_Pioneer", false);
                mesh.chunkVisible("zArr_PioneerBal", false);
                mesh.materialReplace("Pioner", "Pioner_dmg");
            }
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_TempL", false);
                mesh.chunkVisible("zArr_TempR", false);
                mesh.materialReplace("Temp125", "Temp125_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("zArr_RPMB", false);
            mesh.chunkVisible("zArr_RPMS", false);
            mesh.materialReplace("RPM10", "RPM10_dmg");
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_Variom", false);
                mesh.materialReplace("Variom", "Variom_dmg");
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zAzimuth", false);
                mesh.chunkVisible("zAzimuth_DM", true);
            }
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_Volt", false);
                mesh.materialReplace("Volt", "Volt_dmg");
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_Fuel", false);
                mesh.materialReplace("Fuel10", "Fuel10_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("xGlassDm2", true);
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("zArr_OilPres", false);
                mesh.materialReplace("OilPres", "OilPres_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("xGlassDm1", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("xHullDm2", true);
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_Speed", false);
                mesh.materialReplace("Speed300", "Speed300_dmg");
            }
            if(World.Rnd().nextFloat() < 0.75F)
            {
                mesh.chunkVisible("zArr_AG", false);
                mesh.materialReplace("AG", "AG_dmg");
            }
        }
        if((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0)
            mesh.chunkVisible("xHullDm3", true);
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("xOilSplats_D1", true);
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        mesh.chunkVisible("CF_D0_00", hiermesh.isChunkVisible("CF_D0"));
        mesh.chunkVisible("CF_D1_00", hiermesh.isChunkVisible("CF_D1"));
        mesh.chunkVisible("CF_D2_00", hiermesh.isChunkVisible("CF_D2"));
        mesh.chunkVisible("WingLMid_D0_00", hiermesh.isChunkVisible("WingLMid_D0"));
        mesh.chunkVisible("WingLMid_D1_00", hiermesh.isChunkVisible("WingLMid_D1"));
        mesh.chunkVisible("WingLMid_D2_00", hiermesh.isChunkVisible("WingLMid_D2"));
        mesh.chunkVisible("WingRMid_D0_00", hiermesh.isChunkVisible("WingRMid_D0"));
        mesh.chunkVisible("WingRMid_D1_00", hiermesh.isChunkVisible("WingRMid_D1"));
        mesh.chunkVisible("WingRMid_D2_00", hiermesh.isChunkVisible("WingRMid_D2"));
        mesh.chunkVisible("WingRMid_CAP_00", hiermesh.isChunkVisible("WingRMid_CAP"));
        mesh.chunkVisible("WingLMid_CAP_00", hiermesh.isChunkVisible("WingLMid_CAP"));
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        mesh.materialReplace("Matt2D0o", mat);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            for(int i = 0; i < 2; i++)
                lights[i].light.setEmit(0.45F, 0.45F);

            mesh.chunkSetAngles("zCabin_Lights", -25F, 0.0F, 0.0F);
        } else
        {
            for(int j = 0; j < 2; j++)
                lights[j].light.setEmit(0.0F, 0.0F);

            mesh.chunkSetAngles("zCabin_Lights", 25F, 0.0F, 0.0F);
        }
        setNightMats(false);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HierMesh hiermesh = aircraft().hierMesh();
            hiermesh.chunkVisible("Cockpit_D0", false);
            hiermesh.chunkVisible("Engine1p_D0", false);
            hiermesh.chunkVisible("Engine1p_D1", false);
            hiermesh.chunkVisible("Engine1p_D2", false);
            moveGunner();
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        hiermesh.chunkVisible("Cockpit_D0", true);
        hiermesh.chunkVisible("Engine1p_D0", true);
        hiermesh.chunkVisible("Engine1p_D1", true);
        hiermesh.chunkVisible("Engine1p_D2", true);
        super.doFocusLeave();
    }

    private void moveGunner()
    {
        if(u_2xyz.gunnerDead || u_2xyz.gunnerEjected)
            return;
        if((double)u_2xyz.getGunnerAnimation() > 0.5D)
        {
            mesh.chunkVisible("Pilot3_D0", true);
            mesh.chunkVisible("Pilot2_D0", false);
            mesh.chunkSetAngles("Pilot3_D0", 0.0F, 0.0F, 0.0F);
        } else
        if((double)u_2xyz.getGunnerAnimation() > 0.25D)
        {
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = (u_2xyz.getGunnerAnimation() - 0.5F) * 0.5F;
            Cockpit.ypr[0] = -120F + 480F * (u_2xyz.getGunnerAnimation() - 0.25F);
            Cockpit.ypr[1] = 0.0F;
            Cockpit.ypr[2] = 0.0F;
            mesh.chunkSetLocate("Pilot3_D0", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkVisible("Pilot3_D0", true);
            mesh.chunkVisible("Pilot2_D0", false);
            mesh.chunkSetAngles("zTurret1A", -55F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -30F);
        } else
        {
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = u_2xyz.getGunnerAnimation() * 0.5F;
            Cockpit.ypr[0] = 0.0F;
            Cockpit.ypr[1] = 0.0F;
            Cockpit.ypr[2] = 0.0F;
            mesh.chunkSetLocate("Pilot2_D0", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkVisible("Pilot3_D0", false);
            mesh.chunkVisible("Pilot2_D0", true);
            mesh.chunkSetAngles("zTurret1A", -55F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -30F);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private Vector3f w;
    private boolean bNeedSetUp;
    private float propPos;
    U_2xyz u_2xyz;
    private LightPointActor lights[] = {
        null, null
    };
    private static final float IAS_Scale[] = {
        0.0F, 0.0F, 6F, 27.75F, 54.37F, 79F, 101.67F, 126.4F, 149.41F, 174.81F, 
        199.71F, 224.86F, 250.35F, 272.52F, 298.7F, 327.43F
    };

    static 
    {
        Property.set(CockpitU_2VSLNB.class, "normZNs", new float[] {
            0.7F, 0.6F, 0.6F, 0.6F
        });
        Property.set(CockpitU_2VSLNB.class, "gsZN", 0.2F);
    }
}
