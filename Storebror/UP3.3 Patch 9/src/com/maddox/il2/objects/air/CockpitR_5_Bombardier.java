package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitR_5_Bombardier extends CockpitPilot//CockpitBombardier
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
                if(bEntered)
                {
                    R_5Bomber r_5bomber = (R_5Bomber)aircraft();
                    float f = r_5bomber.fSightCurForwardAngle + r_5bomber.tSAim;
                    float f1 = -r_5bomber.fSightCurSideslip;
                    mesh.chunkSetAngles("BlackBox", f1, -r_5bomber.kSAim, -f);
                    HookPilot hookpilot = HookPilot.current;
                    hookpilot.setInstantOrient(-f1, tAim + f, 0.0F);
                } else
                {
                    R_5Bomber r_5bomber1 = (R_5Bomber)aircraft();
//                    mesh.chunkSetAngles("zArr_Stopwatch", -6F * r_5bomber1.getOPBTimer(), 0.0F, 0.0F);
                    mesh.chunkSetAngles("OPB1_on", -r_5bomber1.fSightCurSideslip, r_5bomber1.kSAim, r_5bomber1.tSAim);
                }
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


    public float[] getBombSightFovs()
    {
        return defaultBSightFoVsOPB;
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
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            if(hookpilot.isAimReached())
            {
                enteringAim = false;
                enter();
            } else
            if(!hookpilot.isAim())
                enteringAim = false;
        }
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
//        R_5xyz r_5xyz = (R_5xyz)aircraft();
        if(R_5xyz.bChangedPit)
        {
            reflectPlaneToModel();
//            R_5xyz r_5xyz1 = (R_5xyz)aircraft();
            R_5xyz.bChangedPit = false;
        }
        mesh.chunkVisible("BlackBox", bEntered);
        mesh.chunkVisible("zReticle", bEntered);
        mesh.chunkVisible("zMark1", bEntered);
        mesh.chunkVisible("zMark2", bEntered);
        mesh.chunkVisible("zBulb", bEntered);
        mesh.chunkVisible("zRefraction", bEntered);
        if(bEntered)
        {
            R_5Bomber r_5bomber = (R_5Bomber)aircraft();
            mesh.chunkSetAngles("zMark1", r_5bomber.fSightCurForwardAngle * 3.675F, 0.0F, 0.0F);
            float f2 = cvt(r_5bomber.fSightSetForwardAngle, -15F, 75F, -15F, 75F);
            mesh.chunkSetAngles("zMark2", f2 * 3.675F, 0.0F, 0.0F);
            resetYPRmodifier();
            Cockpit.xyz[0] = cvt(fm.Or.getKren() + r_5bomber.kSAim, -13F, 13F, 0.23F, -0.23F);
            Cockpit.xyz[1] = cvt(fm.Or.getTangage() + r_5bomber.tSAim, -13F, 13F, -0.23F, 0.23F);
            Cockpit.ypr[0] = cvt(fm.Or.getKren(), -45F, 45F, -180F, 180F);
            mesh.chunkSetLocate("zBulb", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[0] = cvt(Cockpit.xyz[0], -0.23F, 0.23F, 0.0095F, -0.0095F);
            Cockpit.xyz[1] = cvt(Cockpit.xyz[1], -0.23F, 0.23F, 0.0095F, -0.0095F);
            mesh.chunkSetLocate("zRefraction", Cockpit.xyz, Cockpit.ypr);
        }
        resetYPRmodifier();
        float f1 = setNew.pictRudd * 20F;
        mesh.chunkSetAngles("Pedals", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Pedal_tros_L1", -f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Pedal_tros_R1", -f1, 0.0F, 0.0F);
        float f3 = setNew.pictRudd;
        if(f3 > 0.0F)
        {
            mesh.chunkSetAngles("Pedal_tros_L2", -f3 * 22.2F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Pedal_tros_R2", -f3 * 19.1F, 0.0F, 0.0F);
        } else
        {
            mesh.chunkSetAngles("Pedal_tros_L2", -f3 * 19.1F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Pedal_tros_R2", -f3 * 22.2F, 0.0F, 0.0F);
        }
        mesh.chunkSetAngles("zRollerL", 200F * f3, 0.0F, 0.0F);
        mesh.chunkSetAngles("zRollerR", 200F * f3, 0.0F, 0.0F);
        if(tros2Mat != null)
        {
            tros2 = 0.25F * f3;
            tros2Mat.setLayer(0);
            tros2Mat.set((byte)11, tros2);
        }
        float f4 = 0.0F;
        if(setNew.pictElev > 0.0F)
            f4 = 21F * setNew.pictElev;
        else
            f4 = 13F * setNew.pictElev;
        mesh.chunkSetAngles("StickElev", 0.0F, 0.0F, f4);
        mesh.chunkSetAngles("Stick", 0.0F, setNew.pictAiler, 0.0F);
        mesh.chunkSetAngles("StickKardan", 0.0F, 0.0F, -f4 * (float)Math.cos(setNew.pictAiler * 0.01745329F));
        mesh.chunkSetAngles("StickConnctr", -f4 * (float)Math.sin(setNew.pictAiler * 0.01745329F), 0.0F, 0.0F);
        mesh.chunkSetAngles("StickEl_trosL1", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("StickEl_trosL2", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("StickEl_trosR1", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("StickEl_trosR2", 0.0F, 0.0F, -f4);
        R_5xyz r_5xyz2 = (R_5xyz)aircraft();
        if((double)r_5xyz2.getGunnerAnimation() > 0.5D)
        {
            float f5 = cvt(r_5xyz2.getGunnerAnimation(), 0.5F, 1.0F, -60F, -20F);
            mesh.chunkSetAngles("zTurret1A", f5, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", -f5, 0.0F, 0.0F);
        } else
        {
            mesh.chunkSetAngles("zTurret1A", -60F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 60F, 0.0F, 0.0F);
        }
        HierMesh hiermesh = aircraft().hierMesh();
        hiermesh.chunkVisible("Turret1A_D0", false);
        hiermesh.chunkVisible("Turret1C_D0", false);
        hiermesh.chunkVisible("Turret1A_FAKE", false);
        hiermesh.chunkVisible("Turret1B_FAKE", false);
        float f6 = -50F * setNew.throttle1;
        mesh.chunkSetAngles("zThrotle", 0.0F, 0.0F, f6);
        mesh.chunkSetAngles("Cable_throtle01", 0.0F, 0.0F, -f6);
        mesh.chunkSetAngles("Cable_throtle02", 0.0F, 0.0F, -f6);
        mesh.chunkSetAngles("zThrotleNav", 0.0F, 0.0F, -f6);
        mesh.chunkSetAngles("Cable_throtle03", 0.0F, 0.0F, f6);
        float f7 = -37.5F * setNew.mix1;
        mesh.chunkSetAngles("zMixture", 0.0F, 0.0F, f7);
        mesh.chunkSetAngles("Cable_mixture01", 0.0F, 0.0F, -f7);
        mesh.chunkSetAngles("Cable_mixture02", 0.0F, 0.0F, -f7);
        mesh.chunkSetAngles("zMixtureNav", 0.0F, 0.0F, -f7);
        mesh.chunkSetAngles("Cable_mixture03", 0.0F, 0.0F, f7);
        float f8 = cvt(fm.EI.engines[0].getRPM(), 900F, 1200F, 0.0F, -50F);
        mesh.chunkSetAngles("zIgnition", 0.0F, 0.0F, f8);
        mesh.chunkSetAngles("Cable_Ignition", 0.0F, 0.0F, -f8);
        mesh.chunkSetAngles("Z_AL2c", r_5xyz2.CompassDelta, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_AN4a", 0.0F, -setNew.compasKren, -setNew.compasTangage);
        mesh.chunkSetAngles("Z_AN4b", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Speed2", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 60F, 400F, 0.0F, 17F), IAS_Scale), 0.0F);
        float f9 = getBall(8D);
        mesh.chunkSetAngles("zArr_PioneerBall2", 0.0F, cvt(f9, -4F, 4F, -11F, 11F), 0.0F);
        f9 = cvt(setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        mesh.chunkSetAngles("zArr_Pioneer2", 0.0F, f9, 0.0F);
        mesh.chunkSetAngles("zArr_ClockS2", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zArr_ClockM2", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zArr_ClockH2", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("zArr_Alt2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 12000F, 0.0F, 720F), 0.0F);
        float f10 = 0.0F;
        if(fm.AS.bLandingLightOn)
            f10 = -2.5F;
        if(fm.AS.bNavLightsOn)
            f10--;
        mesh.chunkSetAngles("zArr_Volt", 0.0F, f10, 0.0F);
        mesh.chunkSetAngles("zArr_AirPress250", 0.0F, 0.0F, 153F);
        mesh.chunkSetAngles("zAirstartr", 0.0F, 0.0F, -90F * setNew.Airstartr);
        if(fm.EI.engines[0].getStage() == 1 || fm.EI.engines[0].getStage() == 2)
            mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, -245F * setNew.Airstartr + 5F * World.Rnd().nextFloat());
        else
            mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, 0.0F);
        if(!hiermesh.isChunkVisible("Pilot1_D0") && !hiermesh.isChunkVisible("Pilot1_D1"))
        {
            mesh.chunkVisible("GS", true);
            mesh.chunkVisible("GS_Cap", true);
            mesh.chunkVisible("GS_Tinter", true);
            mesh.chunkVisible("GS_Spring", true);
            mesh.chunkSetAngles("GS_Cap", 0.0F, 0.0F, cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -130F));
            mesh.chunkSetAngles("GS_Tinter", 0.0F, 0.0F, cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 0.69F, 0.0F, -90F));
            mesh.chunkSetAngles("GS_Spring", cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
        } else
        {
            mesh.chunkVisible("GS", false);
            mesh.chunkVisible("GS_Cap", false);
            mesh.chunkVisible("GS_Tinter", false);
            mesh.chunkVisible("GS_Spring", false);
        }
        mesh.chunkVisible("Pilot1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        mesh.chunkVisible("Head1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        mesh.chunkVisible("HMask1_D0", hiermesh.isChunkVisible("HMask1_D0"));
        mesh.chunkVisible("Pilot1_D1", hiermesh.isChunkVisible("Pilot1_D1"));
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.25D, 0.0D, -0.09D);
            hookpilot.setTubeSight(point3d);
            aircraft().hierMesh().chunkVisible("Interior_D0", false);
            bNeedSetUp = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TurretDown");
            aircraft().FM.turret[0].bIsAIControlled = false;
//            aircraft().FM.turret[0].bMultiFunction = false;
//            ((R_5xyz)aircraft()).bMultiFunction = false;
            doHidePilot();
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
            return;
        leave();
        aircraft().hierMesh().chunkVisible("Interior_D0", true);
        aircraft().FM.turret[0].bIsAIControlled = true;
//        aircraft().FM.turret[0].bMultiFunction = false;
//        ((R_5xyz)aircraft()).bMultiFunction = false;
        
        HierMesh hiermesh = aircraft().hierMesh();
        R_5xyz r_5xyz = (R_5xyz)aircraft();
        if(r_5xyz.getGunnerAnimation() < 1.0F)
        {
            hiermesh.chunkVisible("Turret1A_FAKE", true);
            hiermesh.chunkVisible("Turret1B_FAKE", true);
            hiermesh.chunkVisible("Turret1A_D0", false);
            hiermesh.chunkVisible("Turret1C_D0", false);
        } else
        {
            hiermesh.chunkVisible("Turret1A_FAKE", false);
            hiermesh.chunkVisible("Turret1B_FAKE", false);
            hiermesh.chunkVisible("Turret1A_D0", true);
            hiermesh.chunkVisible("Turret1C_D0", true);
        }
        
        super.doFocusLeave();
        doShowPilot();
    }

    private void prepareToEnter()
    {
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, -90F, 0.0F);
        enteringAim = true;
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov " + saveBSFov);
        Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
        HookPilot hookpilot = HookPilot.current;
        R_5Bomber r_5bomber = (R_5Bomber)aircraft();
        hookpilot.setSimpleAimOrient(aAim, tAim + r_5bomber.tSAim, r_5bomber.kSAim);
        hookpilot.setInstantOrient(aAim, tAim + r_5bomber.tSAim, r_5bomber.kSAim);
        hookpilot.setSimpleUse(true);
        doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void leave()
    {
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(0.0F, -90F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if(!bEntered)
        {
            return;
        } else
        {
            saveBSFov = Main3D.FOVX;
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.setInstantOrient(0.0F, -90F, 0.0F);
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
            bNeedSetUp = true;
            return;
        }
    }

    public void doToggleAim(boolean flag)
    {
        if(!isFocused())
            return;
        if(isToggleAim() == flag)
            return;
        if(flag)
            prepareToEnter();
        else
            leave();
    }

    public void destroy()
    {
        super.destroy();
        leave();
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("xHullDm1", true);
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("xHullDm4", true);
            mesh.chunkVisible("zArr_ClockH2", false);
            mesh.chunkVisible("zArr_ClockM2", false);
            mesh.chunkVisible("zArr_ClockS2", false);
            mesh.chunkVisible("zArr_ClockDop2", false);
            mesh.materialReplace("Prib_ClockACHO", "Prib_ClockACHO_dmg");
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("xHullDm5", true);
            mesh.chunkVisible("zArr_Alt2", false);
            mesh.materialReplace("Prib_Alt6km", "Prib_Alt6km_dmg");
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("xGlassDm3", true);
            mesh.chunkVisible("xHullDm6", true);
            mesh.chunkVisible("zArr_Pioneer2", false);
            mesh.chunkVisible("zArr_PioneerBall2", false);
            mesh.materialReplace("Prib_Peoneer2", "Prib_Peoneer2_dmg");
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("xGlassDm2", true);
            mesh.chunkVisible("xHullDm7", true);
            mesh.materialReplace("Prib_Kpa3Alt", "Prib_Kpa3Alt_dmg");
            mesh.chunkVisible("zArr_Speed2", false);
            mesh.materialReplace("Prib_Prib40", "Prib_Prib40_dmg");
        }
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("xGlassDm1", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("xHullDm2", true);
            mesh.chunkVisible("zArr_Volt", false);
            mesh.materialReplace("Prib_Volt", "Prib_Volt_dmg");
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

    public CockpitR_5_Bombardier()
    {
        super("3DO/Cockpit/R-5-Bombardier/hier.him", "u2");
        saveBSFov = 50F;
        bNeedSetUp = true;
        enteringAim = false;
        bEntered = false;
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
        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(mesh, "CAMERAAIM");
            hooknamed.computePos(this, new Loc(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
//            kAim = loc.getOrient().getKren();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        cockpitNightMats = new String[0];
        for(int j = 0; j < 2; j++)
        {
            HookNamed hooknamed1 = new HookNamed(mesh, "LAMP0" + (j + 3));
            Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed1.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
            lights[j] = new LightPointActor(new LightPoint(), loc1.getPoint());
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
    private boolean enteringAim;
    private float saveFov;
    private float saveBSFov;
    private final float defaultBSightFoVsOPB[] = {
        15F, 50F, 65F
    };
    private float aAim;
    private float tAim;
//    private float kAim;
    private boolean bEntered;
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
        Property.set(CockpitR_5_Bombardier.class, "weaponControlNum", 3);
        Property.set(CockpitR_5_Bombardier.class, "astatePilotIndx", 1);
        Property.set(CockpitR_5_Bombardier.class, "normZNs", new float[] {
            0.3F, 0.7F, 1.1F, 0.7F
        });
        Property.set(CockpitR_5_Bombardier.class, "gsZN", 0.6F);
    }
}
