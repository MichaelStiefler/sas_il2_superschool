// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:09:36
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   NetSafeLog.java

package com.maddox.il2.game;

import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;

// Referenced classes of package com.maddox.il2.game:
//            HUD

public class NetSafeLog
{

    private NetSafeLog()
    {
    }

    private static boolean isLocalActor(Actor theActor)
    {
        return theActor == World.getPlayerAircraft() && !theActor.isNetMirror();
    }

    public static void log(Actor logActor, String logLine)
    {
        if(isLocalActor(logActor))
            HUD.log(logLine);
    }

    public static void log(Actor logActor, int i, String logLine)
    {
        if(isLocalActor(logActor))
            HUD.log(i, logLine);
    }

    public static void training(Actor logActor, String logLine)
    {
        if(isLocalActor(logActor))
            HUD.training(logLine);
    }

    public static void type(Actor logActor, String logLine)
    {
        if(isLocalActor(logActor))
            EventLog.type(logLine);
    }
}