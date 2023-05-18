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
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitU_2VSLNB_Bombardier extends CockpitPilot // CockpitBombardier
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
            float f = ((U_2xyz)aircraft()).fSightCurForwardAngle;
            if(bEntered)
            {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setInstantOrient(aAim, tAim + f, 0.0F);
                Point3d point3d = new Point3d();
                if(f > 33F)
                    f = 33F;
                point3d.x = -1.04010495D - 0.20595115D * Math.tan(Math.toRadians(f));
                point3d.y = -0.43969995D;
                point3d.z = 0.45649825D;
                hookpilot.setAim(point3d);
                hookpilot.setInstantAim(true);
            }
            mesh.chunkSetAngles("zTurret1A", -55F, 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -30F);
            if(bbombR1 && bombR1 > 0.0F)
                bombR1 = bombR1 - 0.05F;
            if(bbombR2 && bombR2 > 0.0F)
                bombR2 = bombR2 - 0.05F;
            if(bbombR3 && bombR3 > 0.0F)
                bombR3 = bombR3 - 0.05F;
            if(bbombL1 && bombL1 > 0.0F)
                bombL1 = bombL1 - 0.05F;
            if(bbombL2 && bombL2 > 0.0F)
                bombL2 = bombL2 - 0.05F;
            if(bbombL3 && bombL3 > 0.0F)
                bombL3 = bombL3 - 0.05F;
            if(amountOfBombs == 2 && !abpk && !cargo)
            {
                if(!bombs[0].haveBullets() && !bbombR1)
                {
                    bombR1 = 1.0F;
                    bbombR1 = true;
                }
                if(!bombs[1].haveBullets() && !bbombL1)
                {
                    bombL1 = 1.0F;
                    bbombL1 = true;
                }
            } else
            if(amountOfBombs == 2 && abpk && !cargo)
            {
                if(!bombs[6].haveBullets() && !bbombR2)
                {
                    bombR2 = 1.0F;
                    bbombR2 = true;
                }
                if(!bombs[7].haveBullets() && !bbombL2)
                {
                    bombL2 = 1.0F;
                    bbombL2 = true;
                }
            } else
            if(amountOfBombs == 2 && !abpk && cargo)
            {
                if(!bombs[8].haveBullets() && !bbombR2)
                {
                    bombR2 = 1.0F;
                    bbombR2 = true;
                }
                if(!bombs[9].haveBullets() && !bbombL2)
                {
                    bombL2 = 1.0F;
                    bbombL2 = true;
                }
            } else
            if(amountOfBombs == 4)
            {
                if(!bombs[0].haveBullets() && !bbombR1)
                {
                    bombR1 = 1.0F;
                    bbombR1 = true;
                }
                if(!bombs[1].haveBullets() && !bbombL1)
                {
                    bombL1 = 1.0F;
                    bbombL1 = true;
                }
                if(!bombs[2].haveBullets() && !bbombR2)
                {
                    bombR2 = 1.0F;
                    bbombR2 = true;
                }
                if(!bombs[3].haveBullets() && !bbombL2)
                {
                    bombL2 = 1.0F;
                    bbombL2 = true;
                }
            } else
            if(amountOfBombs == 6)
            {
                if(!bombs[0].haveBullets() && !bbombR1)
                {
                    bombR1 = 1.0F;
                    bbombR1 = true;
                }
                if(!bombs[1].haveBullets() && !bbombL1)
                {
                    bombL1 = 1.0F;
                    bbombL1 = true;
                }
                if(!bombs[2].haveBullets() && !bbombR2)
                {
                    bombR2 = 1.0F;
                    bbombR2 = true;
                }
                if(!bombs[3].haveBullets() && !bbombL2)
                {
                    bombL2 = 1.0F;
                    bbombL2 = true;
                }
                if(!bombs[4].haveBullets() && !bbombR3)
                {
                    bombR3 = 1.0F;
                    bbombR3 = true;
                }
                if(!bombs[5].haveBullets() && !bbombL3)
                {
                    bombL3 = 1.0F;
                    bbombL3 = true;
                }
            }
            mesh.chunkVisible("ShKASSpare1", U_2xyz.Spare1);
            mesh.chunkVisible("ShKASSpare2", U_2xyz.Spare2);
            mesh.chunkVisible("ShKASSpare3", U_2xyz.Spare3);
            mesh.chunkVisible("ShKASSpare3", U_2xyz.Spare4);
            if((double)u_2xyz.getGunnerAnimation() < 1.0D)
                if(!Main3D.cur3D().isViewOutside())
                {
                    hiermesh.chunkVisible("Turret1A_FAKE", false);
                    hiermesh.chunkVisible("Turret1B_FAKE", false);
                } else
                {
                    hiermesh.chunkVisible("Turret1A_FAKE", true);
                    hiermesh.chunkVisible("Turret1B_FAKE", true);
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
        float mix1;
        float pictElev;
        float pictAiler;
        float pictRudd;
        float compasTangage;
        float compasKren;
        float azimuth;
        float altimeter;
        float turn;

        private Variables()
        {
        }

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
        mesh.chunkVisible("WingRMid_CAP_00", hiermesh.isChunkVisible("WingRMid_CAP"));
        mesh.chunkVisible("WingLMid_CAP_00", hiermesh.isChunkVisible("WingLMid_CAP"));
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
        if(U_2xyz.bChangedPit)
        {
            reflectPlaneToModel();
            U_2xyz.bChangedPit = false;
        }
        if(bEntered)
        {
            mesh.chunkSetAngles("zBomb_sight", 0.0F, 0.0F, floatindex(cvt(((U_2xyz)aircraft()).fSightCurForwardAngle, 9F, 33F, 0.0F, 24F), Bombsight));
        } else
        {
            float f1 = 15F * setNew.pictAiler;
            float f2 = 0.0F;
            if(setNew.pictElev > 0.0F)
                f2 = 15F * setNew.pictElev;
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
            float f4 = getBall(8D);
            mesh.chunkSetAngles("zArr_PioneerBal", cvt(-f4, -4F, 4F, -7.5F, 7.5F), 0.0F, 0.0F);
            f4 = cvt(setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
            mesh.chunkSetAngles("zArr_Pioneer", -f4, 0.0F, 0.0F);
            float f5 = -50F * setNew.throttle1;
            mesh.chunkSetAngles("Z_Throtle", f5, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_Throtle1", 0.0F, 0.0F, -f5);
            float f6 = 30F * setNew.mix1;
            mesh.chunkSetAngles("Z_Mixture", f6, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_Mixture1", 0.0F, 0.0F, -f6);
            mesh.chunkSetAngles("zArr_RPMB", cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
            mesh.chunkSetAngles("zArr_RPMS", cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
            mesh.chunkSetAngles("zArr_Speed", -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 15F), IAS_Scale), 0.0F, 0.0F);
            float f7 = cvt(fm.EI.engines[0].getRPM(), 0.0F, 1100F, 0.0F, 4.5F);
            mesh.chunkSetAngles("zArr_OilPres", cvt(f7, 0.0F, 15F, 0.0F, -268F), 0.0F, 0.0F);
            mesh.chunkSetAngles("zArr_Alt_m", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
            mesh.chunkSetAngles("zArr_Alt_km", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
            mesh.chunkSetAngles("zArr_ACHO_m", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
            mesh.chunkSetAngles("zArr_ACHO_h", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
            mesh.chunkSetAngles("zArr_ACHO_s", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 21600F), 0.0F, 0.0F);
            mesh.chunkSetAngles("z_SW_Start", fm.EI.engines[0].getStage() > 2 || fm.EI.engines[0].getStage() <= 0 ? 0.0F : -25F, 0.0F, 0.0F);
            float f8 = World.getTimeofDay();
            if(f8 > 18.5F || f8 < 6.5F)
            {
                mesh.chunkVisible("nBomb_equip", true);
                mesh.chunkVisible("znBomb_sight", true);
            } else
            {
                mesh.chunkVisible("nBomb_equip", false);
                mesh.chunkVisible("znBomb_sight", false);
            }
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = 0.025F * bombR1;
            mesh.chunkSetLocate("Z_BdropR1", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[2] = 0.025F * bombR2;
            mesh.chunkSetLocate("Z_BdropR2", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[2] = 0.025F * bombR3;
            mesh.chunkSetLocate("Z_BdropR3", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[2] = 0.025F * bombL1;
            mesh.chunkSetLocate("Z_BdropL1", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[2] = 0.025F * bombL2;
            mesh.chunkSetLocate("Z_BdropL2", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[2] = 0.025F * bombL3;
            mesh.chunkSetLocate("Z_BdropL3", Cockpit.xyz, Cockpit.ypr);
            HierMesh hiermesh = aircraft().hierMesh();
            mesh.chunkVisible("Pilot1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
            mesh.chunkVisible("Head1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
            mesh.chunkVisible("Pilot1_D1", hiermesh.isChunkVisible("Pilot1_D1"));
        }
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HierMesh hiermesh = aircraft().hierMesh();
            hiermesh.chunkVisible("Cockpit_D0", false);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.0D, -0.15D, -0D);
            hookpilot.setTubeSight(point3d);
            bNeedSetUp = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TurretDown");
            aircraft().FM.turret[0].bIsAIControlled = false;
            ((U_2xyz)aircraft()).bMultiFunction = false;
            doHidePilot();
            return true;
        } else
        {
            return false;
        }
    }

    private void prepareToEnter()
    {
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, -45F, 0.0F);
        enteringAim = true;
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 70.0");
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
        hookpilot.setInstantOrient(aAim, tAim, 0.0F);
        hookpilot.setSimpleUse(true);
        doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    protected void doFocusLeave()
    {
        if(isFocused())
        {
            HierMesh hiermesh = aircraft().hierMesh();
            hiermesh.chunkVisible("Cockpit_D0", true);
            leave();
            aircraft().FM.turret[0].bIsAIControlled = true;
            ((U_2xyz)aircraft()).bMultiFunction = false;
            if((double)u_2xyz.getGunnerAnimation() < 1.0D)
            {
                hiermesh.chunkVisible("Turret1A_FAKE", true);
                hiermesh.chunkVisible("Turret1B_FAKE", true);
                hiermesh.chunkVisible("Turret1A_D0", false);
                hiermesh.chunkVisible("Turret1B_D0", false);
            } else
            {
                hiermesh.chunkVisible("Turret1A_FAKE", false);
                hiermesh.chunkVisible("Turret1B_FAKE", false);
                hiermesh.chunkVisible("Turret1A_D0", true);
                hiermesh.chunkVisible("Turret1B_D0", true);
            }
            super.doFocusLeave();
            doShowPilot();
            return;
        } else
        {
            return;
        }
    }

    private void leave()
    {
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(0.0F, 0.0F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if(!bEntered)
        {
            return;
        } else
        {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.setInstantOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setInstantOrient(0.0F, tAim, aAim);
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

    public void destroy()
    {
        super.destroy();
        leave();
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

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("xHullDm1", true);
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("zArr_Alt_m", false);
            mesh.chunkVisible("zArr_Alt_km", false);
            mesh.materialReplace("Alt", "Alt_dmg");
            mesh.chunkVisible("zArr_Pioneer", false);
            mesh.chunkVisible("zArr_PioneerBal", false);
            mesh.materialReplace("Pioner", "Pioner_dmg");
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("zArr_RPMB", false);
            mesh.chunkVisible("zArr_RPMS", false);
            mesh.materialReplace("RPM10", "RPM10_dmg");
            mesh.chunkVisible("zArr_ACHO_s", false);
            mesh.chunkVisible("zArr_ACHO_m", false);
            mesh.chunkVisible("zArr_ACHO_h", false);
            mesh.materialReplace("ACHO", "ACHO_dmg");
            mesh.chunkVisible("zAzimuth", false);
            mesh.chunkVisible("zAzimuth_DM", true);
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("zArr_Speed", false);
            mesh.materialReplace("Speed300", "Speed300_dmg");
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("xGlassDm1", true);
            mesh.chunkVisible("zArr_OilPres", false);
            mesh.materialReplace("OilPres", "OilPres_dmg");
        }
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("xGlassDm1", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
            mesh.chunkVisible("xHullDm2", true);
        if((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0)
            mesh.chunkVisible("xHullDm3", true);
    }

    public CockpitU_2VSLNB_Bombardier()
    {
        super("3DO/Cockpit/U_2VSLNB-Bombardier/hier.him", "i16");
        u_2xyz = (U_2xyz)aircraft();
        bNeedSetUp = true;
        enteringAim = false;
        bEntered = false;
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        abpk = false;
        cargo = false;
        amountOfBombs = 0;
        bombR1 = 0.0F;
        bombL1 = 0.0F;
        bombR2 = 0.0F;
        bombL2 = 0.0F;
        bombR3 = 0.0F;
        bombL3 = 0.0F;
        bbombR1 = false;
        bbombL1 = false;
        bbombR2 = false;
        bbombL2 = false;
        bbombR3 = false;
        bbombL3 = false;
        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(mesh, "CAMERAAIM");
            hooknamed.computePos(this, new Loc(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        cockpitNightMats = new String[0];
        for(int i = 0; i < 2; i++)
        {
            HookNamed hooknamed1 = new HookNamed(mesh, "LAMP0" + (i + 3));
            Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed1.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
            lights[i] = new LightPointActor(new LightPoint(), loc1.getPoint());
            lights[i].light.setColor(0.8980392F, 0.8117647F, 0.6235294F);
            lights[i].light.setEmit(0.0F, 0.0F);
            pos.base().draw.lightMap().put("LAMP0" + (i + 3), lights[i]);
        }

        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        limits6DoF = (new float[] {
            0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.03F, -0.03F
        });
        try
        {
            bombs[0] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb01");
            amountOfBombs++;
        }
        catch(Exception exception1) { }
        try
        {
            bombs[1] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb02");
            amountOfBombs++;
        }
        catch(Exception exception2) { }
        try
        {
            bombs[2] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb03");
            amountOfBombs++;
        }
        catch(Exception exception3) { }
        try
        {
            bombs[3] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb04");
            amountOfBombs++;
        }
        catch(Exception exception4) { }
        try
        {
            bombs[4] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
            amountOfBombs++;
        }
        catch(Exception exception5) { }
        try
        {
            bombs[5] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb06");
            amountOfBombs++;
        }
        catch(Exception exception6) { }
        try
        {
            bombs[6] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb07");
            amountOfBombs++;
            abpk = true;
        }
        catch(Exception exception7) { }
        try
        {
            bombs[7] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb08");
            amountOfBombs++;
            abpk = true;
        }
        catch(Exception exception8) { }
        try
        {
            bombs[8] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalDev01");
            amountOfBombs++;
            cargo = true;
        }
        catch(Exception exception9) { }
        try
        {
            bombs[9] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalDev02");
            amountOfBombs++;
            cargo = true;
        }
        catch(Exception exception10) { }
    }

    protected float waypointAzimuth()
    {
        return waypointAzimuthInvertMinus(30F);
    }

    private boolean bNeedSetUp;
    private boolean enteringAim;
    private float saveFov;
    private float aAim;
    private float tAim;
    private boolean bEntered;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float bombR1;
    private float bombL1;
    private float bombR2;
    private float bombL2;
    private float bombR3;
    private float bombL3;
    private boolean bbombR1;
    private boolean bbombL1;
    private boolean bbombR2;
    private boolean bbombL2;
    private boolean bbombR3;
    private boolean bbombL3;
    private int amountOfBombs;
    private BombGun bombs[] = {
        null, null, null, null, null, null, null, null, null, null
    };
    private boolean abpk;
    private boolean cargo;
    U_2xyz u_2xyz;
    private LightPointActor lights[] = {
        null, null
    };
    private static final float IAS_Scale[] = {
        0.0F, 0.0F, 6F, 27.75F, 54.37F, 79F, 101.67F, 126.4F, 149.41F, 174.81F, 
        199.71F, 224.86F, 250.35F, 272.52F, 298.7F, 327.43F
    };
    private static final float Bombsight[] = {
        0.0F, 4.3F, 8.3F, 12.18F, 15.88F, 19.46F, 22.93F, 26.32F, 29.64F, 32.9F, 
        36.1F, 39.27F, 42.39F, 45.47F, 48.53F, 51.56F, 54.56F, 57.54F, 60.51F, 63.44F, 
        66.36F, 69.28F, 72.19F, 75.089F, 77.989F
    };

    static 
    {
        Property.set(CockpitU_2VSLNB_Bombardier.class, "astatePilotIndx", 1);
        Property.set(CockpitU_2VSLNB_Bombardier.class, "weaponControlNum", 3);
        Property.set(CockpitU_2VSLNB_Bombardier.class, "normZNs", new float[] {
            0.65F, 0.55F, 0.65F, 0.55F
        });
        Property.set(CockpitU_2VSLNB_Bombardier.class, "gsZN", 0.6F);
    }
}
