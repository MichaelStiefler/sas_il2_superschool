// Source File Name: MissileK5M.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;

public class MissileK5M extends Missile {

    static class SPAWN extends Missile.SPAWN {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
            new MissileK5M(actor, netchannel, i, point3d, orient, f);
        }
    }

    public MissileK5M() {
    }

    public MissileK5M(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    static {
        GuidedMissileUtils.parseMissilePropertiesFile(MissileK5M.class);
        Spawn.add(MissileK5M.class, new SPAWN());
    }
}
