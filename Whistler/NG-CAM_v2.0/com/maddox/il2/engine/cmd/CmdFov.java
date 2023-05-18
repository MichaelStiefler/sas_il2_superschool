// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 04/03/2019 18:26:42
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   CmdFov.java

package com.maddox.il2.engine.cmd;

import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookView;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class CmdFov extends Cmd
{
    class CmdFovAnimated
        implements MsgTimeOutListener
    {

        public float getTargetFOV()
        {
            return targetFOV;
        }

        public void stopAnimation()
        {
            RTSConf.cur.queueRealTime.remove(ticker);
            RTSConf.cur.queueRealTimeNextTick.remove(ticker);
            ticker = null;
        }

        public void msgTimeOut(Object arg0)
        {
            String cokpitName = "";
            if(!Main3D.cur3D().isViewOutside())
            {
                cokpitName = Main3D.cur3D().cockpitCur.getClass().getName();
                cokpitName = cokpitName.toLowerCase().substring(cokpitName.lastIndexOf('.') + 1);
            }
            curStep++;
            float newFOV = (!HookView.isCamEnabled() || (int)Config.cur.ini.get("Mods", "ngCAMfovspeed", 2, 0, 3) == 0 || (!Main3D.cur3D().isViewOutside() && (cokpitName.indexOf("bombardier") != -1 || cokpitName.indexOf("observer") != -1))) ? targetFOV : CmdFov.smoothCvt(curStep, 0.0F, steps, startFOV, targetFOV);

            if(Math.abs(newFOV - targetFOV) < 0.1F)
            {
                CmdFov.DoChangeFOV(targetFOV);
                CmdFov.curCmdFovAnimated = null;
                stopAnimation();
            } else
            {
                CmdFov.DoChangeFOV(newFOV);
                ticker.post();
            }
        }

        private final int steps;
        private final float targetFOV;
        private final float startFOV;
        private int curStep;
        private MsgTimeOut ticker;

        CmdFovAnimated(float startFOV, float targetFOV)
        {
            this.startFOV = startFOV;
            this.targetFOV = targetFOV;
            curStep = 0;
            steps = (int)Math.max(Math.min((Math.abs(targetFOV - startFOV) * 2.0F) / CmdFov.speedFactor, 40F / CmdFov.speedFactor), 1.0F);
            ticker = new MsgTimeOut();
            ticker.setTickPos(-1);
            ticker.setNotCleanAfterSend();
            ticker.setFlags(88);
            ticker.setListener(this);
            ticker.post();
        }
    }


    static float smoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax)
    {
        inputValue = Math.min(Math.max(inputValue, inMin), inMax);
        return outMin + (outMax - outMin) * (-0.5F * (float)Math.cos((double)((inputValue - inMin) / (inMax - inMin)) * 3.1415926535897931D) + 0.5F);
    }

    private static void DoChangeFOV(float fovValue)
    {
        Camera3D camera3d1 = (Camera3D)Actor.getByName("camera");
        camera3d1.set(fovValue);
        Render render = (Render)Actor.getByName("renderCockpit");
        if(render != null)
        {
            Camera3D camera3d2 = (Camera3D)render.getCamera();
            camera3d2.set(fovValue);
        }
        Main3D.FOVX = fovValue;
    }

    private int getFoVIntRev(float rawFOV, boolean increase)
    {
        int renderWidth = RendersMain.width();
        int renderHeight = RendersMain.height();
        if(!RendersMain.isSaveAspect() || !Config.cur.windowsWideScreenFoV || (renderWidth * 3) / 4 == renderHeight || renderWidth < renderHeight)
            return (int)rawFOV;
        float rawFOVWide = 0.75F - (float)renderHeight / (float)renderWidth;
        int aspect = Math.round(rawFOV / (1.0F + rawFOVWide));
        int aspect10s = (aspect / 10) * 10;
        int aspect1s = aspect % 10;
        if(aspect1s == 0 || aspect1s == 5)
            return aspect10s + aspect1s;
        if(increase)
        {
            if(aspect1s >= 7)
                return aspect10s + 10;
            if(aspect1s >= 2)
                return aspect10s + 5;
            else
                return aspect10s;
        }
        if(aspect1s > 7)
            return aspect10s + 10;
        if(aspect1s > 3)
            return aspect10s + 5;
        else
            return aspect10s;
    }

    public Object exec(CmdEnv cmdenv, Map map)
    {
        if(map.containsKey("_$$"))
        {
            float targetFOV = Cmd.arg(map, "_$$", 0, 70F, minFOV, maxFOV);
            speedFactor = (float)(int)Config.cur.ini.get("Mods", "ngCAMfovspeed", 2, 0, 3);
            if(speedFactor == 0.0F || speedFactor == 3.0F)
                speedFactor = 4.0F;
            float curTargetFOV = curCmdFovAnimated != null ? curCmdFovAnimated.getTargetFOV() : Main3D.FOVX;
            int iMaxFovOverride = 90;
            int iMinFovOverride = 30;
            int iStockFovDefault = 70;
            int renderWidth = RendersMain.width();
            int renderHeight = RendersMain.height();
            if(RendersMain.isSaveAspect() && Config.cur.windowsWideScreenFoV && (renderWidth * 3) / 4 != renderHeight && renderWidth >= renderHeight)
            {
                float fOverrideFactor = 1.75F - (float)renderHeight / (float)renderWidth;
                iMaxFovOverride = (int)((float)iMaxFovOverride * fOverrideFactor);
                iMinFovOverride = (int)((float)iMinFovOverride * fOverrideFactor);
                iStockFovDefault = (int)(((0.75F - (float)renderHeight / (float)renderWidth) + 1.0F) * 70F);
            }
            if(targetFOV == 70F)
            {
                StringWriter sw = new StringWriter();
                (new Exception()).printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                if(exceptionAsString.indexOf("com.maddox.il2.game.Mission.doBegin") != -1)
                {
                    targetFOV = (float)(int)Config.cur.ini.get("Mods", "ngCAMfovdefault", 90, 60, 120);
                    lastFOVX = targetFOV;
                    DoChangeFOV(targetFOV);
                    return "OK";
                }
            }
            if(targetFOV == (float)iStockFovDefault && (float)Math.abs(getFoVIntRev(lastFOVX, targetFOV > lastFOVX) - getFoVIntRev(targetFOV, targetFOV > lastFOVX)) > 5F)
                targetFOV = (float)(int)Config.cur.ini.get("Mods", "ngCAMfovdefault", 90, 60, 120);
            if(curTargetFOV > (float)iMaxFovOverride - 2.5F)
            {
                if((int)targetFOV == iMaxFovOverride - 5)
                    targetFOV = curTargetFOV - 5F;
                else
                if((int)targetFOV == iMaxFovOverride)
                    targetFOV = curTargetFOV + 5F;
            } else
            if(curTargetFOV < (float)iMinFovOverride + 2.5F)
                if((int)targetFOV == iMinFovOverride)
                    targetFOV = curTargetFOV - 5F;
                else
                if((int)targetFOV == iMinFovOverride + 5)
                    targetFOV = curTargetFOV + 5F;
            if(targetFOV < minFOV)
                targetFOV = minFOV;
            if(targetFOV > maxFOV)
                targetFOV = maxFOV;
            lastFOVX = targetFOV;
            if(curCmdFovAnimated != null)
                curCmdFovAnimated.stopAnimation();
            curCmdFovAnimated = new CmdFovAnimated(Main3D.FOVX, targetFOV);
            if((int)Config.cur.ini.get("Mods", "ngCAMfovlog", 0, 0, 1) == 1)
            {
                if(hudLogFOVId == -1)
                    hudLogFOVId = HUD.makeIdLog();
                HUD.log(hudLogFOVId, "FOV {0}\260", new Object[] {
                    new Integer(Math.round(targetFOV / 5F) * 5)
                });
            }
        } else
        {
            Camera3D camera3d = (Camera3D)Actor.getByName("camera");
            INFO_HARD("Camera fov: ".concat(String.valueOf(camera3d.FOV())));
        }
        return "OK";
    }

    public CmdFov()
    {
        super._properties.put("NAME", "fov");
        super._levelAccess = 1;
    }

    public static int hudLogFOVId = -1;
    private static CmdFovAnimated curCmdFovAnimated = null;
    private static float speedFactor = 2.0F;
    private static float minFOV = 5F;
    private static float maxFOV = 135F;
    private static float lastFOVX = 70F;
}
