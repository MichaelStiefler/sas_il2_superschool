// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 20.11.2018 8:56:04
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileR60M.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileR60M extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileR60M(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileR60M(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public MissileR60M()
    {
    }

    static 
    {
        Class class1 = MissileR60M.class;
        Property.set(class1, "mesh", "3do/arms/R-60M/mono.sim");
        Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 23F);
        Property.set(class1, "timeFire", 1.8F);
        Property.set(class1, "force", 20000F);
        Property.set(class1, "forceT1", 0.5F);
        Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "forceT2", 0.2F);
        Property.set(class1, "forceP2", 50F);
        Property.set(class1, "dragCoefficient", 0.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 3.5F);
        Property.set(class1, "radius", 5F);
        Property.set(class1, "kalibr", 0.12F);
        Property.set(class1, "massa", 44F);
        Property.set(class1, "massaEnd", 30F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 1);
        Property.set(class1, "detectorType", 1);
        Property.set(class1, "sunRayAngle", 8F);
        Property.set(class1, "multiTrackingCapable", 1);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", 0);
        Property.set(class1, "attackDecisionByAI", 1);
        Property.set(class1, "targetType", 1);
        Property.set(class1, "shotFreq", 1.01F);
        Property.set(class1, "groundTrackFactor", 5F);
        Property.set(class1, "flareLockTime", 200L);
        Property.set(class1, "trackDelay", 1000L);
        Property.set(class1, "failureRate", 30F);
        Property.set(class1, "maxLockGForce", 7F);
        Property.set(class1, "maxFOVfrom", 2.5F);
        Property.set(class1, "maxFOVfrom_real", 20F);
        Property.set(class1, "maxFOVto", 360F);
        Property.set(class1, "PkMaxFOVfrom", 25F);
        Property.set(class1, "PkMaxFOVto", 180F);
        Property.set(class1, "PkDistMin", 200F);
        Property.set(class1, "PkDistOpt", 1500F);
        Property.set(class1, "PkDistMax", 5000F);
        Property.set(class1, "leadPercent", 100F);
        Property.set(class1, "maxGForce", 20F);
        Property.set(class1, "stepsForFullTurn", 7);
        Property.set(class1, "fxLock", "weapon.R60.lock");
        Property.set(class1, "fxNoLock", "weapon.R60.nolock");
        Property.set(class1, "smplLock", "R60_lock.wav");
        Property.set(class1, "smplNoLock", "R60_no_lock.wav");
        Property.set(class1, "friendlyName", "R-60M");
        Spawn.add(class1, new SPAWN());
    }
}