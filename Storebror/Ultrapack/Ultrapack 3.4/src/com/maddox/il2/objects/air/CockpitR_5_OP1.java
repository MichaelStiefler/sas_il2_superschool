package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitR_5_OP1 extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            if(cockpitDimControl)
            {
                if(setNew.dimPos < 1.0F)
                    setNew.dimPos = setOld.dimPos + 0.05F;
            } else
            if(setNew.dimPos > 0.0F)
                setNew.dimPos = setOld.dimPos - 0.05F;
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float dimPos;

        private Variables()
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
        CmdEnv.top().exec("fov 31.0");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
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

    public void toggleDim()
    {
        cockpitDimControl = !cockpitDimControl;
    }

    public CockpitR_5_OP1()
    {
        super("3DO/Cockpit/R-5_OP1/hier.him", "u2");
        setOld = new Variables();
        setNew = new Variables();
        bEntered = false;
        interpPut(new Interpolater(), (Object)null, Time.current(), (Message)null);
    }

    public void reflectWorldToInstruments(float f)
    {
        float f1 = cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -130F);
        mesh.chunkSetAngles("Z_BoxTinter", 0.0F, f1, 0.0F);
    }

    private float saveFov;
    private boolean bEntered;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;

    static 
    {
        Property.set(CockpitR_5_OP1.class, "astatePilotIndx", 0);
        Property.set(CockpitR_5_OP1.class, "gsZN", 0.2F);
    }
}
