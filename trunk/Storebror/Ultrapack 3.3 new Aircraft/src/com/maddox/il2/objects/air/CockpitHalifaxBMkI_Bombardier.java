package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHalifaxBMkI_Bombardier extends CockpitPilot
{
    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(bEntered)
            {
                float f = ((Halifax)aircraft()).fSightCurForwardAngle;
                float f1 = -((Halifax)aircraft()).fSightCurSideslip;
                mesh.chunkSetAngles("BlackBox", f1, 0.0F, -f);
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setSimpleAimOrient(-f1, tAim + f, 0.0F);
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
            enter();
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
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 50.0");
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
        if(bEntered)
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

    public CockpitHalifaxBMkI_Bombardier()
    {
        super("3DO/Cockpit/Lanc-Bombardier/hier.him", "he111");
        bEntered = false;
        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(this.mesh, "CAMERA");
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
        }
        catch(java.lang.Exception exception)
        {
            java.lang.System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        this.mesh.chunkSetAngles("zMark1", ((Halifax)aircraft()).fSightCurForwardAngle * 3.675F, 0.0F, 0.0F);
        float f1 = cvt(((Halifax)aircraft()).fSightSetForwardAngle, -15F, 75F, -15F, 75F);
        this.mesh.chunkSetAngles("zMark2", f1 * 3.675F, 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(this.fm.Or.getKren() * java.lang.Math.abs(this.fm.Or.getKren()), -1225F, 1225F, -0.23F, 0.23F);
        Cockpit.xyz[1] = cvt((this.fm.Or.getTangage() - 1.0F) * java.lang.Math.abs(this.fm.Or.getTangage() - 1.0F), -1225F, 1225F, 0.23F, -0.23F);
        Cockpit.ypr[0] = cvt(this.fm.Or.getKren(), -45F, 45F, -180F, 180F);
        this.mesh.chunkSetLocate("zBulb", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(Cockpit.xyz[0], -0.23F, 0.23F, 0.0095F, -0.0095F);
        Cockpit.xyz[1] = cvt(Cockpit.xyz[1], -0.23F, 0.23F, 0.0095F, -0.0095F);
        this.mesh.chunkSetLocate("zRefraction", Cockpit.xyz, Cockpit.ypr);
    }

    private float saveFov;
    private float aAim;
    private float tAim;
    private boolean bEntered;

    static 
    {
        Property.set(CockpitLanc_Bombardier.class, "astatePilotIndx", 0);
    }
}
