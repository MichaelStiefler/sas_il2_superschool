package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;

public class CockpitR_10 extends CockpitPilot
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            com.maddox.il2.engine.hotkey.HookPilot hookpilot = com.maddox.il2.engine.hotkey.HookPilot.current;
            hookpilot.doAim(false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(isFocused())
        {
            leave();
            super.doFocusLeave();
        }
    }

    private void enter()
    {
        saveFov = com.maddox.il2.game.Main3D.FOVX;
        com.maddox.rts.CmdEnv.top().exec("fov 31");
        com.maddox.il2.game.Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        com.maddox.il2.engine.hotkey.HookPilot hookpilot = com.maddox.il2.engine.hotkey.HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        com.maddox.rts.HotKeyEnv.enable("PanView", false);
        com.maddox.rts.HotKeyEnv.enable("SnapView", false);
        bEntered = true;
        mesh.chunkVisible("SuperReticle", true);
        mesh.chunkVisible("truba", false);
    }

    private void leave()
    {
        if(bEntered)
        {
            com.maddox.il2.game.Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            com.maddox.rts.CmdEnv.top().exec("fov " + saveFov);
            com.maddox.il2.engine.hotkey.HookPilot hookpilot = com.maddox.il2.engine.hotkey.HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = com.maddox.rts.HotKeyEnv.isEnabled("aircraftView");
            com.maddox.rts.HotKeyEnv.enable("PanView", flag);
            com.maddox.rts.HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
            mesh.chunkVisible("SuperReticle", false);
            mesh.chunkVisible("truba", true);
        }
    }

    public void destroy()
    {
        leave();
        super.destroy();
    }

    public void doToggleAim(boolean flag)
    {
        if(isFocused() && isToggleAim() != flag)
            if(flag)
                enter();
            else
                leave();
    }

    protected float waypointAzimuth()
    {
        com.maddox.il2.ai.WayPoint waypoint = fm.AP.way.curr();
        if(waypoint == null)
        {
            return 0.0F;
        } else
        {
            waypoint.getP(tmpP);
            tmpV.sub(tmpP, fm.Loc);
            return (float)(57.295779513082323D * java.lang.Math.atan2(-tmpV.y, tmpV.x));
        }
    }

    public CockpitR_10()
    {
        super("3DO/Cockpit/R-10/hier.him", "i16");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        bEntered = false;
        cockpitNightMats = (new java.lang.String[] {
            "prib_one", "prib_one_dd", "prib_two", "prib_two_dd", "prib_three", "prib_three_dd", "prib_four", "prib_four_dd", "shkala", "oxigen"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        mesh.chunkSetAngles("Stick", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 15F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F);
        mesh.chunkSetAngles("Ped_Base", 0.0F, -fm.CT.getRudder() * 15F, 0.0F);
        mesh.chunkSetAngles("PedalL", 0.0F, fm.CT.getRudder() * 15F, 0.0F);
        mesh.chunkSetAngles("PedalR", 0.0F, fm.CT.getRudder() * 15F, 0.0F);
        mesh.chunkSetAngles("Fire1", 0.0F, -20F * (fm.CT.WeaponControl[0] ? 1.0F : 0.0F), 0.0F);
        mesh.chunkSetAngles("Fire2", 0.0F, -20F * (fm.CT.WeaponControl[1] ? 1.0F : 0.0F), 0.0F);
        mesh.chunkSetAngles("Thtl", 30F - 57F * interp(setNew.throttle, setOld.throttle, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Prop", interp(setNew.prop, setOld.prop, f) * -57F + 30F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Thtl_Rod", -30F + 57F * interp(setNew.throttle, setOld.throttle, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Prop_Rod", interp(setNew.prop, setOld.prop, f) * 57F - 30F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Gear_Crank", (15840F * interp(setNew.gCrankAngle, setOld.gCrankAngle, f)) % 360F, 0.0F, 0.0F);
        mesh.chunkSetAngles("zAlt1a", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        mesh.chunkSetAngles("zAlt1b", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zAzimuth1a", 0.0F, cvt(fm.Or.getTangage(), -40F, 40F, -40F, 40F), 0.0F);
        mesh.chunkSetAngles("zAzimuth1b", 0.0F, -90F - interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        mesh.chunkSetAngles("zGas1a", 0.0F, floatindex(cvt(fm.M.fuel, 0.0F, 190F, 0.0F, 14F), fuelQuantityScale), 0.0F);
        mesh.chunkSetAngles("zSpeed1a", 0.0F, floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 550F, 0.0F, 11F), speedometerScale), 0.0F);
        if((fm.AS.astateCockpitState & 4) == 0 && (fm.AS.astateCockpitState & 0x10) == 0)
        {
            w.set(fm.getW());
            fm.Or.transform(w);
            mesh.chunkSetAngles("zTurn1a", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
            mesh.chunkSetAngles("zSlide1a", 0.0F, cvt(getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
            mesh.chunkSetAngles("zTOilIn1a", 0.0F, cvt(fm.EI.engines[0].tOilIn, 0.0F, 125F, 0.0F, 275F), 0.0F);
            mesh.chunkSetAngles("zTOilOut1a", 0.0F, cvt(fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 275F), 0.0F);
            mesh.chunkSetAngles("zPressAir1a", 0.0F, cvt(fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 10F, 0.0F, 275F), 0.0F);
        }
        mesh.chunkSetAngles("zVariometer1a", 0.0F, cvt(setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        if((fm.AS.astateCockpitState & 8) == 0 && (fm.AS.astateCockpitState & 0x20) == 0)
        {
            mesh.chunkSetAngles("zRPS1a", 0.0F, floatindex(cvt(fm.EI.engines[0].getRPM(), 0.0F, 2400F, 0.0F, 13F), engineRPMScale), 0.0F);
            mesh.chunkSetAngles("zManifold1a", 0.0F, cvt(fm.EI.engines[0].getManifoldPressure(), 0.4F, 2.133F, 0.0F, 334.286F), 0.0F);
            mesh.chunkSetAngles("zTCil1a", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -88F), 0.0F);
        }
        mesh.chunkSetAngles("zClock1a", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zClock1b", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("zPressOil1a", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 270F), 0.0F);
        mesh.chunkVisible("Z_Red1", fm.CT.getGear() == 0.0F);
        mesh.chunkVisible("Z_Red2", fm.CT.getGear() == 0.0F);
        mesh.chunkVisible("Z_Green1", fm.CT.getGear() == 1.0F);
        mesh.chunkVisible("Z_Green2", fm.CT.getGear() == 1.0F);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0 || (fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("pribors1", false);
            mesh.chunkVisible("pribors1_dd", true);
            mesh.chunkVisible("zSpeed1a", false);
            mesh.chunkVisible("zAlt1a", false);
            mesh.chunkVisible("zAlt1b", false);
            mesh.chunkVisible("zPressOil1a", false);
            mesh.chunkVisible("zVariometer1a", false);
        }
        if((fm.AS.astateCockpitState & 8) != 0 || (fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("pribors2", false);
            mesh.chunkVisible("pribors2_dd", true);
            mesh.chunkVisible("zAzimuth1a", false);
            mesh.chunkVisible("zAzimuth1b", false);
            mesh.chunkVisible("zManifold1a", false);
            mesh.chunkVisible("zGas1a", false);
        }
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("Z_Holes1_D1", true);
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("Z_Holes2_D1", true);
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("Z_OilSplats_D1", true);
    }

    protected void reflectPlaneMats()
    {
        mesh.chunkVisible("ritedoor", false);
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
    public com.maddox.JGP.Vector3f w;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private static final float speedometerScale[] = {
        0.0F, 0.0F, 18F, 44F, 74.5F, 106F, 136.3F, 169.5F, 207.5F, 245F, 
        287.5F, 330F
    };
    private static final float fuelQuantityScale[] = {
        0.0F, 38.5F, 74.5F, 98.5F, 122F, 143F, 163F, 182.5F, 203F, 221F, 
        239.5F, 256F, 274F, 295F, 295F, 295F
    };
    private static final float engineRPMScale[] = {
        0.0F, 16F, 18F, 59.5F, 100.5F, 135.5F, 166.5F, 198.5F, 227F, 255F, 
        281.5F, 307F, 317F, 327F
    };
    private com.maddox.JGP.Point3d tmpP;
    private com.maddox.JGP.Vector3d tmpV;
    private float saveFov;
    private boolean bEntered;

    private class Variables
    {

        float throttle;
        float prop;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;
        float gCrankAngle;

        private Variables()
        {
            gCrankAngle = 0.0F;
        }

    }

    private class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                if(bNeedSetUp)
                {
                    reflectPlaneMats();
                    bNeedSetUp = false;
                }
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
                setNew.prop = (10F * setOld.prop + fm.EI.engines[0].getControlProp()) / 11F;
                setNew.altimeter = fm.getAltitude();
                if(java.lang.Math.abs(fm.Or.getKren()) < 30F)
                    setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + com.maddox.il2.ai.World.Rnd().nextFloat(-10F, 10F)) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                boolean flag = false;
                if(setNew.gCrankAngle < fm.CT.getGear() - 0.005F)
                    if(java.lang.Math.abs(setNew.gCrankAngle - fm.CT.getGear()) < 0.33F)
                    {
                        setNew.gCrankAngle += 0.0025F;
                        flag = true;
                    } else
                    {
                        setNew.gCrankAngle = fm.CT.getGear();
                        setOld.gCrankAngle = fm.CT.getGear();
                    }
                if(setNew.gCrankAngle > fm.CT.getGear() + 0.005F)
                    if(java.lang.Math.abs(setNew.gCrankAngle - fm.CT.getGear()) < 0.33F)
                    {
                        setNew.gCrankAngle -= 0.0025F;
                        flag = true;
                    } else
                    {
                        setNew.gCrankAngle = fm.CT.getGear();
                        setOld.gCrankAngle = fm.CT.getGear();
                    }
                if(flag != sfxPlaying)
                {
                    if(flag)
                        sfxStart(16);
                    else
                        sfxStop(16);
                    sfxPlaying = flag;
                }
            }
            return true;
        }

        boolean sfxPlaying;

        Interpolater()
        {
            sfxPlaying = false;
        }
    }

}
