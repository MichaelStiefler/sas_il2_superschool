// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 08.12.2019 14:59:38
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileR23R.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile, MissileR55

public class MissileR23R extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileR23R(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileR23R(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public MissileR23R()
    {
    }


    static 
    {
        Class class1 = MissileR23R.class;
        Property.set(class1, "mesh", "3do/arms/R-23R/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSparrowFlame.sim");
        Property.set(class1, "smoke", "3DO/Effects/Aircraft/TurboHWK109D.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 45F);
        Property.set(class1, "timeFire", 3.5F);
        Property.set(class1, "force", 31000F);
        Property.set(class1, "dragCoefficient", 0.5f);//0.3F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "power", 25F);
        Property.set(class1, "radius", 25F);
        Property.set(class1, "proximityFuzeRadius", 40F);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 222F);
        Property.set(class1, "massaEnd", 180F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 1);
        Property.set(class1, "detectorType", 2);
        Property.set(class1, "sunRayAngle", 0.0F);
        Property.set(class1, "multiTrackingCapable", 0);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", 0L);
        Property.set(class1, "attackDecisionByAI", 1);
        Property.set(class1, "targetType", 1);
        Property.set(class1, "shotFreq", 0.01F);
        Property.set(class1, "groundTrackFactor", 16F);
        Property.set(class1, "flareLockTime", 1000L);
        Property.set(class1, "trackDelay", 2000L);
        Property.set(class1, "failureRate", 65F);
        Property.set(class1, "maxLockGForce", 5F);
        Property.set(class1, "maxFOVfrom", 20F);
        Property.set(class1, "maxFOVto", 360F);
        Property.set(class1, "PkMaxFOVfrom", 25F);
        Property.set(class1, "PkMaxFOVto", 80F);
        Property.set(class1, "PkDistMin", 1000F);
        Property.set(class1, "PkDistOpt", 10000F);
        Property.set(class1, "PkDistMax", 25000F);
        Property.set(class1, "leadPercent", 100F);
        Property.set(class1, "maxGForce", 15F);
        Property.set(class1, "stepsForFullTurn", 12);


        /*
        Property.set(class1, "fxLock", "weapon.K5.lock");
        Property.set(class1, "fxNoLock", "weapon.K5.nolock");
        Property.set(class1, "smplLock", "K5_lock.wav");
        Property.set(class1, "smplNoLock", "K5_no_lock.wav");*/
        Property.set(class1, "friendlyName", "R-23R");
        Spawn.add(class1, new SPAWN());
    }
}