package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;


public class CockpitTU4_Bombardier extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            float f = ((TU_4)aircraft()).fSightCurForwardAngle;
            float f1 = ((TU_4)aircraft()).fSightCurSideslip;
            mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            if(bEntered)
            {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setInstantOrient(aAim + 10F * f1, tAim + f, 0.0F);
            }
            mesh.chunkSetAngles("TurretA", 0.0F, ((SndAircraft) (aircraft())).FM.turret[0].tu[0], 0.0F);
            mesh.chunkSetAngles("TurretB", 0.0F, ((SndAircraft) (aircraft())).FM.turret[0].tu[1], 0.0F);
            float f2 = waypointAzimuth();
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f2 - 90F);
                setOld.waypointAzimuth.setDeg(f2 - 90F);
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f2);
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        AnglesFork azimuth;
        AnglesFork waypointAzimuth;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.15000000596046448D, 0.0D, -0.10000000149011612D);
            hookpilot.setTubeSight(point3d);
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
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter()
    {
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, -33F, 0.0F);
        enteringAim = true;
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 33.413");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setInstantOrient(aAim, tAim, 0.0F);
        hookpilot.setSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void leave()
    {
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(0.0F, -33F, 0.0F);
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
            hookpilot1.setInstantOrient(0.0F, -33F, 0.0F);
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
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
            prepareToEnter();
        else
            leave();
    }

    public CockpitTU4_Bombardier()
    {
        super("3DO/Cockpit/B-25J-Bombardier/BombardierTU4.him", "bf109");
        enteringAim = false;
        bEntered = false;
        setOld = new Variables(null);
        setNew = new Variables(null);
        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(super.mesh, "CAMERAAIM");
            hooknamed.computePos(this, super.pos.getAbs(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        super.cockpitNightMats = (new String[] {
            "textrbm9", "texture25"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    public void reflectWorldToInstruments(float f)
    {
        super.mesh.chunkSetAngles("zSpeed", 0.0F, floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
        super.mesh.chunkSetAngles("zSpeed1", 0.0F, floatindex(cvt(super.fm.getSpeedKMH(), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
        super.mesh.chunkSetAngles("zAlt1", 0.0F, cvt((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        super.mesh.chunkSetAngles("zAlt2", 0.0F, cvt((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        super.mesh.chunkSetAngles("zHour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("zMinute", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("zSecond", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("zCompass1", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("zCompass2", 0.0F, setNew.waypointAzimuth.getDeg(0.1F), 0.0F);
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
        if(bEntered)
        {
            super.mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((TU_4)aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((TU_4)aircraft()).fSightCurReadyness > 0.93F;
            super.mesh.chunkVisible("BlackBox", true);
            super.mesh.chunkVisible("zReticle", flag);
            super.mesh.chunkVisible("zAngleMark", flag);
        } else
        {
            super.mesh.chunkVisible("BlackBox", false);
            super.mesh.chunkVisible("zReticle", false);
            super.mesh.chunkVisible("zAngleMark", false);
        }
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
            super.mesh.chunkVisible("XGlassDamage1", true);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
            super.mesh.chunkVisible("XGlassDamage2", true);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
            super.mesh.chunkVisible("XGlassDamage2", true);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0)
            super.mesh.chunkVisible("XGlassDamage3", true);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
            super.mesh.chunkVisible("XHullDamage1", true);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
            super.mesh.chunkVisible("XHullDamage2", true);
    }

    private boolean enteringAim;
    private static final float angleScale[] = {
        -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 
        75F, 76.5F, 77F, 78F, 79F, 80F
    };
    private static final float speedometerScale[] = {
        0.0F, 2.5F, 54F, 104F, 154.5F, 205.5F, 224F, 242F, 259.5F, 277.5F, 
        296.25F, 314F, 334F, 344.5F
    };
    private float saveFov;
    private float aAim;
    private float tAim;
    private boolean bEntered;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitTU4_Bombardier.class, "astatePilotIndx", 0);
    }
}