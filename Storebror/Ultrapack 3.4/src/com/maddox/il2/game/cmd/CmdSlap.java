/*
 * New "slap" command by SAS~Storebror
 */

package com.maddox.il2.game.cmd;

import java.util.Map;

import com.maddox.il2.game.Main;
import com.maddox.il2.net.NetUser;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.NetEnv;

public class CmdSlap extends Cmd {

    public Object exec(CmdEnv cmdenv, Map map) {
        if (Main.cur().netServerParams == null) return null;

        if (!map.containsKey("_$$")) {
            this.ERR_HARD("bad format of slap command (no key found)");
            return null;
        }

        int numArgs = nargs(map, "_$$");
        if (numArgs < 1) {
            this.ERR_HARD("bad format of slap command (argument number < 1)");
            return null;
        }

        NetUser netuser = null;
        String userName = arg(map, "_$$", 0);
        for (int k = 0; k < NetEnv.hosts().size() && netuser == null; k++)
            if (userName.equals(((NetUser) NetEnv.hosts().get(k)).uniqueName())) netuser = (NetUser) NetEnv.hosts().get(k);

        if (netuser == null) for (int k = 0; k < NetEnv.hosts().size() && netuser == null; k++)
            if (userName.equalsIgnoreCase(((NetUser) NetEnv.hosts().get(k)).uniqueName())) netuser = (NetUser) NetEnv.hosts().get(k);

        if (netuser == null) {
            this.ERR_HARD("slap error (user " + userName + " not found).");
            return null;
        }
        int numSlaps = 1;
        if (numArgs >= 2) try {
            numSlaps = Integer.parseInt(arg(map, "_$$", 1));
        } catch (Exception exception) {
            this.ERR_HARD("bad format of slap command (second argument is not numeric)");
            return null;
        }

        NetUser.slap(netuser, numSlaps);

        return CmdEnv.RETURN_OK;
    }

    public CmdSlap() {
        this._properties.put("NAME", "slap");
        this._levelAccess = 1;
    }
}
