// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   CmdFPS.java

package com.maddox.il2.engine.cmd;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.Mission;
import com.maddox.rts.*;
import com.maddox.sound.AudioDevice;
import java.util.*;

public class CmdFPS extends Cmd
    implements MsgTimeOutListener
{

    public void msgTimeOut(Object obj)
    {
        msg.post();
        if(!bGo)
            return;
        long l = Time.real();
        int i = RendersMain.frame();
        if(l >= timePrev + 250L)
        {
            fpsCur = (1000D * (double)(i - framePrev)) / (double)(l - timePrev);
            if(fpsMin > fpsCur)
                fpsMin = fpsCur;
            if(fpsMax < fpsCur)
                fpsMax = fpsCur;
            framePrev = i;
            timePrev = l;
        }
        if(framePrev == frameStart)
            return;
        if(bShow)
        {
            Render render = (Render)Actor.getByName("renderTextScr");
            if(render == null)
                return;
            TTFont ttfont = TextScr.font();
            int j = render.getViewPortWidth();
            int k = render.getViewPortHeight();
            String s = "fps:" + fpsInfo();
            int i1 = (int)ttfont.width(s);
            int j1 = k - ttfont.height() - 5;
            int k1 = (j - i1) / 2;
            TextScr.output(k1, j1, s);
        }
        if(logPeriod > 0L && l >= logPrintTime + logPeriod)
        {
            System.out.println("fps:" + fpsInfo());
            logPrintTime = l;
        }
    }

    public Object exec(CmdEnv cmdenv, Map map)
    {
        boolean flag = false;
        checkMsg();
        if(map.containsKey("SHOW"))
        {
            bShow = true;
            flag = true;
        } else
        if(map.containsKey("HIDE"))
        {
            bShow = false;
            flag = true;
        }
        if(map.containsKey("LOG"))
        {
            int i = arg(map, "LOG", 0, 5);
            if(i < 0)
                i = 0;
            logPeriod = (long)i * 1000L;
            flag = true;
        }
        if(map.containsKey("PERF"))
        {
            AudioDevice.setPerformInfo(true);
            flag = true;
        }
        if(map.containsKey("START"))
        {
            if(bGo && timeStart != timePrev && logPeriod == 0L)
                INFO_HARD("fps:" + fpsInfo());
            timeStart = Time.real();
            frameStart = RendersMain.frame();
            fpsMin = 1000000D;
            fpsMax = 0.0D;
            fpsCur = 0.0D;
            timePrev = timeStart;
            framePrev = frameStart;
            logPrintTime = timeStart;
            bGo = true;
            flag = true;
        } else
        if(map.containsKey("STOP"))
        {
            if(bGo && timeStart != timePrev && logPeriod == 0L)
                INFO_HARD("fps:" + fpsInfo());
            bGo = false;
            flag = true;
        }
        if(map.containsKey("FMINFO"))
        {
            flag = true;
            if(Mission.isNet() && NetEnv.hosts().size() > 1)
            {
                System.out.println("Network mission detected, disabling fm debug.");
            } else
            {
                if(map.containsKey("SWITCH"))
                    if(!Maneuver.showFM)
                        Maneuver.showFM = true;
                    else
                        Maneuver.showFM = false;
                if(map.containsKey("DUMP") && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead())
                {
                    com.maddox.il2.fm.FlightModel flightmodel = World.getPlayerFM();
                    if(flightmodel != null)
                        flightmodel.drawData();
                }
            }
        }
        if(!flag)
        {
            INFO_HARD("  LOG  " + logPeriod / 1000L);
            if(bShow)
                INFO_HARD("  SHOW");
            else
                INFO_HARD("  HIDE");
            if(bGo)
            {
                if(timeStart != timePrev)
                    INFO_HARD("  " + fpsInfo());
            } else
            {
                INFO_HARD("  STOPPED");
            }
        }
        return CmdEnv.RETURN_OK;
    }

    private String fpsInfo()
    {
        return "" + (int)Math.floor(fpsCur) + " avg:" + (int)Math.floor((1000D * (double)(framePrev - frameStart)) / (double)(timePrev - timeStart)) + " max:" + (int)Math.floor(fpsMax) + " min:" + (int)Math.floor(fpsMin) + " #fr:" + (framePrev - frameStart);
    }

    private void checkMsg()
    {
        if(msg == null)
        {
            msg = new MsgTimeOut();
            msg.setListener(this);
            msg.setNotCleanAfterSend();
            msg.setFlags(72);
        }
        if(!msg.busy())
            msg.post();
    }

    public CmdFPS()
    {
        bGo = false;
        bShow = false;
        logPeriod = 5000L;
        logPrintTime = -1L;
        param.put("LOG", null);
        param.put("START", null);
        param.put("STOP", null);
        param.put("SHOW", null);
        param.put("HIDE", null);
        param.put("PERF", null);
        param.put("FMINFO", null);
        param.put("SWITCH", null);
        param.put("DUMP", null);
        _properties.put("NAME", "fps");
        _levelAccess = 1;
    }

    private boolean bGo;
    private boolean bShow;
    private long timeStart;
    private int frameStart;
    private double fpsMin;
    private double fpsMax;
    private double fpsCur;
    private long timePrev;
    private int framePrev;
    private long logPeriod;
    private long logPrintTime;
    public static final String LOG = "LOG";
    public static final String START = "START";
    public static final String STOP = "STOP";
    public static final String SHOW = "SHOW";
    public static final String HIDE = "HIDE";
    public static final String PERF = "PERF";
    private MsgTimeOut msg;
}
