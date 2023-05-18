package com.maddox.il2.game.cmd;

import java.util.Map;

import com.maddox.il2.game.Main;
import com.maddox.il2.net.NetUser;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.NetEnv;

public class CmdSlapN extends Cmd {

    public Object exec(CmdEnv cmdenv, Map map) {
        if (Main.cur().netServerParams == null) return null;

        if (!map.containsKey("_$$")) {
            this.ERR_HARD("bad format of slap# command (no socket number found)");
            return null;
        }

        int numArgs = nargs(map, "_$$");
        if (numArgs < 1) {
            this.ERR_HARD("bad format of slap# command (argument number < 1)");
            return null;
        }

        int socketNumber = 0;
        try {
            socketNumber = Integer.parseInt(arg(map, "_$$", 0));
        } catch (Exception exception) {
            this.ERR_HARD("bad format of slap# command (first argument is not numeric)");
            return null;
        }

        if (socketNumber <= 0) {
            this.ERR_HARD("slap# error: socket number is smaller than 1.");
            return null;
        }

        if (socketNumber > NetEnv.hosts().size()) {
            this.ERR_HARD("slap# error: socket number exceeeds number of hosts.");
            return null;
        }

        NetUser netuser = (NetUser) NetEnv.hosts().get(socketNumber - 1);

        if (netuser == null) {
            this.ERR_HARD("slap# error (user for socket " + socketNumber + " not found).");
            return null;
        }

        int numSlaps = 1;
        if (numArgs >= 2) try {
            numSlaps = Integer.parseInt(arg(map, "_$$", 1));
        } catch (Exception exception) {
            this.ERR_HARD("bad format of slap# command (second argument is not numeric)");
            return null;
        }

        NetUser.slap(netuser, numSlaps);

        return CmdEnv.RETURN_OK;
    }

    public CmdSlapN() {
        this._properties.put("NAME", "slap#");
        this._levelAccess = 1;
    }
}
