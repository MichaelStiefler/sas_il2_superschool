// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 8/14/2013 6:01:10 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileR55S.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileR55S extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileR55S(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileR55S(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public MissileR55S()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.MissileR55S.class;
        Property.set(class1, "mesh", "3do/arms/R-55/mono.sim");
        Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 10F);
        Property.set(class1, "timeLife", 22F);
        Property.set(class1, "timeFire", 12);
        Property.set(class1, "force", 8500F);
        Property.set(class1, "forceT1", 1.0F);
        Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "forceT2", 0.2F);
        Property.set(class1, "forceP2", 50F);
        Property.set(class1, "dragCoefficient", 0.5F);
        Property.set(class1, "power", 1.135F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 74.25F);
        Property.set(class1, "massaEnd", 68F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 2);
        Property.set(class1, "detectorType", 1);
        Property.set(class1, "sunRayAngle", 22F);
        Property.set(class1, "multiTrackingCapable", 1);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", 800L);
        Property.set(class1, "attackDecisionByAI", 1);
        Property.set(class1, "targetType", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "groundTrackFactor", 9F);
        Property.set(class1, "flareLockTime", 1000L);
        Property.set(class1, "trackDelay", 1000L);
        Property.set(class1, "failureRate", 20F);
        Property.set(class1, "maxLockGForce", 99.9F);
        Property.set(class1, "maxFOVfrom", 24F);
        Property.set(class1, "maxFOVto", 60F);
        Property.set(class1, "PkMaxFOVfrom", 28F);
        Property.set(class1, "PkMaxFOVto", 70F);
        Property.set(class1, "PkDistMin", 800F);
        Property.set(class1, "PkDistOpt", 1500F);
        Property.set(class1, "PkDistMax", 6500F);
        Property.set(class1, "leadPercent", 50.0F);
        Property.set(class1, "maxGForce", 12F);
        Property.set(class1, "stepsForFullTurn", 10F);
        Property.set(class1, "fxLock", "weapon.K5.lock");
        Property.set(class1, "fxNoLock", "weapon.K5.nolock");
        Property.set(class1, "smplLock", "K5_lock.wav");
        Property.set(class1, "smplNoLock", "K5_no_lock.wav");
        Property.set(class1, "friendlyName", "R-55");
        Spawn.add(class1, new SPAWN());
    }
}