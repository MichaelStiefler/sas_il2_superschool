// Source File Name: MissileK13A.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;

public class MissileK13A extends Missile {

    static class SPAWN extends Missile.SPAWN {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
            new MissileK13A(actor, netchannel, i, point3d, orient, f);
        }
    }

    public MissileK13A() {
    }

    public MissileK13A(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    static {
        GuidedMissileUtils.parseMissilePropertiesFile(MissileK13A.class);
        Spawn.add(MissileK13A.class, new SPAWN());
    }
}
