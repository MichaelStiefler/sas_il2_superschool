package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.*;

public class CockpitB29SP_Bombardier extends CockpitPilot
{
    private class Variables
    {

        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            float f = ((B_29X)aircraft()).fSightCurForwardAngle;
            float f1 = ((B_29X)aircraft()).fSightCurSideslip;
            CockpitB29SP_Bombardier.calibrAngle = 360F - fm.Or.getPitch();
            mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, CockpitB29SP_Bombardier.calibrAngle + f);
            if(bEntered)
            {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setSimpleAimOrient(aAim + 10F * f1, tAim + CockpitB29SP_Bombardier.calibrAngle + f, 0.0F);
            }
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
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.altimeter = fm.getAltitude();
                float f2 = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f2 - 90F);
                    setOld.waypointAzimuth.setDeg(f2 - 90F);
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f2);
                }
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.15000000596046448D, 0.0D, -0.10000000149011612D);
            aircraft().hierMesh().chunkVisible("Pilot3_D" + aircraft().chunkDamageVisible("Pilot3"), false);
            aircraft().hierMesh().chunkVisible("Head3_D0", false);
            aircraft().hierMesh().chunkVisible("HMask3_D0", false);
            aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            aircraft().hierMesh().chunkVisible("Blister2_D0", false);
            aircraft().hierMesh().chunkVisible("Blister3_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        } else
        {
            leave();
            aircraft().hierMesh().chunkVisible("Pilot3_D" + aircraft().chunkDamageVisible("Pilot3"), true);
            aircraft().hierMesh().chunkVisible("Head3_D0", true);
            aircraft().hierMesh().chunkVisible("HMask3_D0", true);
            aircraft().hierMesh().chunkVisible("Cockpit_D0", true);
            aircraft().hierMesh().chunkVisible("Blister1_D0", true);
            aircraft().hierMesh().chunkVisible("Blister2_D0", true);
            aircraft().hierMesh().chunkVisible("Blister3_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void leave()
    {
        if(!bEntered)
        {
            return;
        } else
        {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
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
            enter();
        else
            leave();
    }

    public CockpitB29SP_Bombardier()
    {
        super("3DO/Cockpit/B-29/hierBombardier.him", "he111");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bEntered = false;
        bNeedSetUp = true;
        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(mesh, "CAMERAAIM");
            hooknamed.computePos(this, pos.getAbs(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        cockpitNightMats = (new String[] {
            "Gauges1", "textrbm4"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        mesh.chunkSetAngles("Zspeedbomb", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("Zalt2bomb", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        mesh.chunkSetAngles("Zalt01bomb", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        mesh.chunkSetAngles("Zclock1bomb", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("Zclock2bomb", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Zcompassbomb", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Zcompass02bomb", 0.0F, setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
        if(bEntered)
        {
            mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((B_29X)aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((B_29X)aircraft()).fSightCurReadyness > 0.93F;
            mesh.chunkVisible("BlackBox", true);
            mesh.chunkVisible("zReticle", flag);
            mesh.chunkVisible("zAngleMark", flag);
        } else
        {
            mesh.chunkVisible("BlackBox", false);
            mesh.chunkVisible("zReticle", false);
            mesh.chunkVisible("zAngleMark", false);
        }
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("XGlassDamage8", true);
        if((fm.AS.astateCockpitState & 8) != 0)
            mesh.chunkVisible("XGlassDamage7", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("XGlassDamage2", true);
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("XGlassDamage4", true);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
            mesh.chunkVisible("XGlassDamage5", true);
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("XGlassDamage6", true);
        if((fm.AS.astateCockpitState & 0x40) != 0);
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("XGlassDamage1", true);
    }

    protected void reflectPlaneMats()
    {
    }

    public Vector3f w;
    private static final float angleScale[] = {
        -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 
        75F, 76.5F, 77F, 78F, 79F, 80F
    };
    private static final float speedometerScale[] = {
        0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 
        296.5F, 308.5F, 324F, 338.5F
    };
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private static float calibrAngle = 0.0F;
    private float saveFov;
    private float aAim;
    private float tAim;
    private boolean bNeedSetUp;
    private boolean bEntered;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitB29SP_Bombardier.class, "astatePilotIndx", 1);
    }
}