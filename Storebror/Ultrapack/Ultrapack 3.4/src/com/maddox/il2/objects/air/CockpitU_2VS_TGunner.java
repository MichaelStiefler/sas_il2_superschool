package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitU_2VS_TGunner extends CockpitGunner
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            U_2VS_TD.Spare1 = mesh.isChunkVisible("DASpare1");
            U_2VS_TD.Spare2 = mesh.isChunkVisible("DASpare2");
            U_2VS_TD.Spare3 = mesh.isChunkVisible("DASpare3");
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.pictAiler = 0.85F * setOld.pictAiler + 0.15F * cvt(fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F);
            setNew.pictElev = 0.85F * setOld.pictElev + 0.15F * cvt(fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F);
            setNew.pictRudd = 0.85F * setOld.pictRudd + 0.15F * cvt(fm.CT.getRudder(), -1F, 1.0F, -1F, 1.0F);
            if(Math.abs(fm.Or.getKren()) < 20F && Math.abs(fm.Or.getTangage()) < 20F)
                setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
            if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                setOld.azimuth -= 360F;
            if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                setOld.azimuth += 360F;
            setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            setNew.compasTangage = 0.95F * setOld.compasTangage + 0.05F * cvt(fm.Or.getTangage(), -20F, 20F, -20F, 20F);
            setNew.compasKren = 0.95F * setOld.compasKren + 0.05F * cvt(fm.Or.getKren(), -20F, 20F, -20F, 20F);
            setNew.throttle1 = 0.9F * setOld.throttle1 + 0.1F * fm.EI.engines[0].getControlThrottle();
            setNew.mix1 = 0.8F * setOld.mix1 + 0.2F * fm.EI.engines[0].getControlMix();
            setNew.altimeter = fm.getAltitude();
            return true;
        }

        Interpolater()
        {
        }
    }

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
        float vspeed;
        float turn;

        private Variables()
        {
        }

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
        mesh.chunkVisible("wingrin_D0_00", hiermesh.isChunkVisible("wingrin_D0"));
        mesh.chunkVisible("wingrin_D1_00", hiermesh.isChunkVisible("wingrin_D1"));
        mesh.chunkVisible("wingrin_D2_00", hiermesh.isChunkVisible("wingrin_D2"));
        mesh.chunkVisible("winglin_D0_00", hiermesh.isChunkVisible("winglin_D0"));
        mesh.chunkVisible("winglin_D1_00", hiermesh.isChunkVisible("winglin_D1"));
        mesh.chunkVisible("winglin_D2_00", hiermesh.isChunkVisible("winglin_D2"));
        mesh.chunkVisible("WingRMid_CAP_00", hiermesh.isChunkVisible("WingRMid_CAP"));
        mesh.chunkVisible("WingLMid_CAP_00", hiermesh.isChunkVisible("WingLMid_CAP"));
        mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        mesh.chunkVisible("RodCAPL_00", hiermesh.isChunkVisible("VatorLrodV_D0"));
        mesh.chunkVisible("RodCAPR_00", hiermesh.isChunkVisible("VatorRrodV_D0"));
    }

    public void reflectWorldToInstruments(float f)
    {
        super.reflectWorldToInstruments(f);
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
            f2 = 21F * setNew.pictElev;
        else
            f2 = 13F * setNew.pictElev;
        mesh.chunkSetAngles("Z_Stick", f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Stick_e", -f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Stick_d1", 0.0F, 0.0F, -f2);
        mesh.chunkSetAngles("Z_Stick_d2", 0.0F, 0.0F, -f2);
        float f3 = 15F * setNew.pictRudd;
        mesh.chunkSetAngles("Z_PedalsC", f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_PedalsN", f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("zKI6a", 0.0F, setNew.compasTangage, -setNew.compasKren);
        mesh.chunkSetAngles("zAzimuth", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        float f4 = 0.0F;
        mesh.chunkSetAngles("zArr_PioneerBal", cvt(-f4, -4F, 4F, -7.5F, 7.5F), 0.0F, 0.0F);
        f4 = cvt(setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        mesh.chunkSetAngles("zArr_Pioneer", -f4, 0.0F, 0.0F);
        float f5 = -50F * setNew.throttle1;
        mesh.chunkSetAngles("Z_Throtle", f5, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Throtle1", 0.0F, 0.0F, -f5);
        float f6 = 30F * setNew.mix1;
        mesh.chunkSetAngles("Z_Mixture", f6, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Mixture1", 0.0F, 0.0F, -f6);
        mesh.chunkSetAngles("zArr_RPM", -floatindex(cvt(fm.EI.engines[0].getRPM(), 400F, 2200F, 0.0F, 9F), RPM_Scale), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Speed", -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 60F, 400F, 0.0F, 17F), IAS_Scale), 0.0F, 0.0F);
        float f7 = cvt(fm.EI.engines[0].getRPM(), 0.0F, 1100F, 0.0F, 4.5F);
        mesh.chunkSetAngles("zArr_OilPres", cvt(f7, 0.0F, 10F, 0.0F, -265F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Temp", -floatindex(cvt(fm.EI.engines[0].tOilOut, 40F, 125F, 0.0F, 17F), Oil_Scale), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Alt_m", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 6000F, 0.0F, -360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Variom", cvt(setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Clock_m", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Clock_h", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Clock_s", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 21600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("z_SW_Start", fm.EI.engines[0].getStage() > 2 || fm.EI.engines[0].getStage() <= 0 ? 0.0F : 25F, 0.0F, 0.0F);
        HierMesh hiermesh = aircraft().hierMesh();
        mesh.chunkVisible("Pilot1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        mesh.chunkVisible("Head1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        mesh.chunkVisible("Pilot1_D1", hiermesh.isChunkVisible("Pilot1_D1"));
    }

    public void moveGun(Orient orient)
    {
        if((double)u_2vsclass.getGunnerAnimation() < 1.0D)
        {
            return;
        } else
        {
            super.moveGun(orient);
            mesh.chunkSetAngles("zTurret1A", -orient.getYaw(), 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -orient.getTangage());
            mesh.chunkSetAngles("zTurret1A_FAKE", -orient.getYaw(), 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B_FAKE", 0.0F, 0.0F, -orient.getTangage());
            mesh.chunkSetAngles("Z_SightBead", 0.0F, orient.getYaw(), 0.0F);
            return;
        }
    }

    public void clipAnglesGun(Orient orient)
    {
        if(isRealMode())
        {
            float f = orient.getYaw();
            float f1 = orient.getTangage();
            if(f < -35F)
                f = -35F;
            if(f > 35F)
                f = 35F;
            if(f1 > 30F)
                f1 = 30F;
            if(f1 < -15F)
                f1 = -15F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
        }
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
            if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
                bGunFire = false;
            fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
            if(bGunFire)
            {
                if(hook1 == null)
                    hook1 = new HookNamed(aircraft(), "_MGUN01");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
            }
        }
        if((double)u_2vsclass.getGunnerAnimation() < 0.25D)
        {
            mesh.chunkSetAngles("zTurret1A", -55F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -30F);
            resetYPRmodifier();
            Cockpit.ypr[0] = -180F;
            Cockpit.xyz[2] = -0.3F + u_2vsclass.getGunnerAnimation();
            mesh.chunkSetLocate("CameraRod", Cockpit.xyz, Cockpit.ypr);
            resetYPRmodifier();
        } else
        if((double)u_2vsclass.getGunnerAnimation() < 0.5D)
        {
            mesh.chunkSetAngles("zTurret1A", -55F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -30F);
            float f = -125F + 475F * (u_2vsclass.getGunnerAnimation() - 0.25F);
            mesh.chunkSetAngles("CameraRod", f, 0.0F, 0.0F);
        } else
        if((double)u_2vsclass.getGunnerAnimation() < 1.0D)
        {
            mesh.chunkSetAngles("CameraRod", 0.0F, 0.0F, 0.0F);
            float f1 = -55F + 55F * u_2vsclass.getGunnerAnimation();
            float f2 = -30F + 30F * u_2vsclass.getGunnerAnimation();
            mesh.chunkSetAngles("zTurret1A", f1, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, f2);
            mesh.chunkSetAngles("zTurret1A_FAKE", 0.0F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B_FAKE", 0.0F, 0.0F, 0.0F);
        }
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        else
            bGunFire = flag;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("xHullDm1", true);
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("zArr_Alt_m", false);
            mesh.materialReplace("Prib_Alt6km", "Prib_Alt6km_dmg");
            mesh.chunkVisible("zArr_Pioneer", false);
            mesh.chunkVisible("zArr_PioneerBal", false);
            mesh.materialReplace("Prib_Pioner", "Prib_Pioner_dmg");
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("zArr_RPM", false);
            mesh.materialReplace("Prib_Prib22", "Prib_Prib22_dmg");
            mesh.chunkVisible("zArr_Clock_h", false);
            mesh.chunkVisible("zArr_Clock_m", false);
            mesh.chunkVisible("zArr_Clock_s", false);
            mesh.materialReplace("Prib_Clock1", "Prib_Clock1_dmg");
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("zArr_Speed", false);
            mesh.materialReplace("Prib_Prib40", "Prib_Prib40_dmg");
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("xGlassDm1", true);
            mesh.chunkVisible("zArr_OilPres", false);
            mesh.materialReplace("Prib_M10", "Prib_M10_dmg");
        }
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("xGlassDm1", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
            mesh.chunkVisible("xHullDm2", true);
        if((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0)
            mesh.chunkVisible("xHullDm3", true);
    }

    public boolean useMultiFunction()
    {
        return true;
    }

    public void doMultiFunction(boolean flag)
    {
    }

    public CockpitU_2VS_TGunner()
    {
        super("3DO/Cockpit/U_2VS-TGun/hier.him", "i16");
        u_2vsclass = (U_2VS_TD)aircraft();
        bNeedSetUp = true;
//        spareMagName = "DASpare";
//        gunLeverMoveAxis = -1;
        interpPut(new Interpolater(), null, Time.current(), null);
//        limits6DoF = (new float[] {
//            0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.0F, -0.03F
//        });
        setOld = new Variables();
        setNew = new Variables();
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TurretUp");
            ((U_2xyz)aircraft()).bMultiFunction = true;
            bNeedSetUp = true;
            doHidePilot();
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Cockpit_D0", true);
        aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        ((U_2xyz)aircraft()).bMultiFunction = false;
        super.doFocusLeave();
        doShowPilot();
    }

    private boolean bNeedSetUp;
    private Hook hook1;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    U_2VS_TD u_2vsclass;
    private static final float IAS_Scale[] = {
        0.0F, 13.7F, 29.5F, 49.5F, 72.2F, 98.8F, 127.09F, 161.5F, 192.5F, 229F, 
        262.6F, 299F, 335.29F, 370.29F, 405.29F, 439.59F, 471.29F, 504.69F
    };
    private static final float Oil_Scale[] = {
        0.0F, 5.4F, 11.3F, 20.9F, 31.1F, 42.1F, 53.3F, 66.7F, 81.7F, 96.7F, 
        115.3F, 130.6F, 147.89F, 173.69F, 197.4F, 223.6F, 253.1F, 286F
    };
    private static final float RPM_Scale[] = {
        0.0F, 30F, 68.8F, 114.8F, 160.9F, 200.4F, 236F, 263.9F, 288.1F, 307F
    };

    static 
    {
        Property.set(CockpitU_2VS_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitU_2VS_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitU_2VS_TGunner.class, "astatePilotIndx", 1);
        Property.set(CockpitU_2VS_TGunner.class, "normZNs", new float[] {
            0.1F, 0.1F, 0.1F, 0.1F
        });
    }
}
