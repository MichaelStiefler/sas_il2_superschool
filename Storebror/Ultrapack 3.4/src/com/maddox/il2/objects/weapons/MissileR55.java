// Source File Name: MissileR55.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;

public class MissileR55 extends Missile {

    static class SPAWN extends Missile.SPAWN {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
            new MissileR55(actor, netchannel, i, point3d, orient, f);
        }
    }

    public MissileR55() {
    }

    public MissileR55(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    static {
        GuidedMissileUtils.parseMissilePropertiesFile(MissileR55.class);
        Spawn.add(MissileR55.class, new SPAWN());
    }
}
