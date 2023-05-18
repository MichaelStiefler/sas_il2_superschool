// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   CmdSlapN.java

package com.maddox.il2.game.cmd;

import com.maddox.il2.game.Main;
import com.maddox.il2.net.NetUser;
import com.maddox.rts.*;
import java.util.*;

public class CmdSlapN extends Cmd
{

    public Object exec(CmdEnv cmdenv, Map map)
    {
        if (Main.cur().netServerParams == null)
            return null;

        if (!map.containsKey("_$$")) {
            ERR_HARD("bad format of slap# command (no socket number found)");
            return null;
        }

        int numArgs = nargs(map, "_$$");
        if (numArgs < 1) {
            ERR_HARD("bad format of slap# command (argument number < 1)");
            return null;
        }
        
        int socketNumber = 0;
        try {
            socketNumber = Integer.parseInt(arg(map, "_$$", 0));
        } catch (Exception exception) {
            ERR_HARD("bad format of slap# command (first argument is not numeric)");
            return null;
        }
        
        if(socketNumber <= 0) {
            ERR_HARD("slap# error: socket number is smaller than 1.");
            return null;
        }

        if(socketNumber > NetEnv.hosts().size()) {
            ERR_HARD("slap# error: socket number exceeeds number of hosts.");
            return null;
        }
        
        NetUser netuser = (NetUser)NetEnv.hosts().get(socketNumber - 1);
        
        if (netuser == null) {
            ERR_HARD("slap# error (user for socket " + socketNumber + " not found).");
            return null;
        }
        
        int numSlaps = 1;
        if (numArgs >= 2) {
            try {
                numSlaps = Integer.parseInt(arg(map, "_$$", 1));
            } catch (Exception exception) {
                ERR_HARD("bad format of slap# command (second argument is not numeric)");
                return null;
            }
        }

        if (!netuser.isMaster())
            ((NetUser) NetEnv.host()).slap(netuser, numSlaps);

        return CmdEnv.RETURN_OK;
    }

    public CmdSlapN()
    {
        _properties.put("NAME", "slap#");
        _levelAccess = 1;
    }
}
