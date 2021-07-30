package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;

public class MissileAIM9B extends Missile {

    static class SPAWN extends Missile.SPAWN {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
            new MissileAIM9B(actor, netchannel, i, point3d, orient, f);
        }
    }

    public MissileAIM9B() {
    }

    public MissileAIM9B(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    static {
        GuidedMissileUtils.parseMissilePropertiesFile(MissileAIM9B.class);
        Spawn.add(MissileAIM9B.class, new SPAWN());
    }
}
