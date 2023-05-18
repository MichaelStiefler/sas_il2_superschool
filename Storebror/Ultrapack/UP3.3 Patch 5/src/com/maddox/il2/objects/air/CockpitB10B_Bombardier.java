package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB10B_Bombardier extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(bEntered)
            {
                float f = ((Martin_B10_xyz)aircraft()).fSightCurForwardAngle;
                float f1 = -((Martin_B10_xyz)aircraft()).fSightCurSideslip;
                mesh.chunkSetAngles("BlackBox", f1, 0.0F, -f);
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setInstantOrient(-f1, tAim + f, 0.0F);
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.0D, 0.0D, 0.0D);
            hookpilot.setTubeSight(point3d);
            enter();
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

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 50.0");
        Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
        hookpilot.setInstantOrient(aAim, tAim, 0.0F);
        doSetSimpleUse(true);
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
            doSetSimpleUse(false);
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
    }

    public CockpitB10B_Bombardier()
    {
        super("3DO/Cockpit/B-10B-Bombardier/B10BBombardier.him", "he111");
        bEntered = false;
        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(mesh, "CAMERA");
            hooknamed.computePos(this, pos.getAbs(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        mesh.chunkSetAngles("zMark1", ((Martin_B10_xyz)aircraft()).fSightCurForwardAngle * 3.675F, 0.0F, 0.0F);
        float f1 = cvt(((Martin_B10_xyz)aircraft()).fSightSetForwardAngle, -15F, 75F, -15F, 75F);
        mesh.chunkSetAngles("zMark2", f1 * 3.675F, 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(fm.Or.getKren() * Math.abs(fm.Or.getKren()), -1225F, 1225F, -0.23F, 0.23F);
        Cockpit.xyz[1] = cvt((fm.Or.getTangage() - 1.0F) * Math.abs(fm.Or.getTangage() - 1.0F), -1225F, 1225F, 0.23F, -0.23F);
        Cockpit.ypr[0] = cvt(fm.Or.getKren(), -45F, 45F, -180F, 180F);
        mesh.chunkSetLocate("zBulb", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(Cockpit.xyz[0], -0.23F, 0.23F, 0.0095F, -0.0095F);
        Cockpit.xyz[1] = cvt(Cockpit.xyz[1], -0.23F, 0.23F, 0.0095F, -0.0095F);
        mesh.chunkSetLocate("zRefraction", Cockpit.xyz, Cockpit.ypr);
    }

    private float saveFov;
    private float aAim;
    private float tAim;
    private boolean bEntered;

    static 
    {
        Property.set(CockpitB10B_Bombardier.class, "astatePilotIndx", 0);
    }


}
