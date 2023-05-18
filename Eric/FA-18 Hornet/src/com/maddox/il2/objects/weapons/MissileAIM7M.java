// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11/7/2012 9:35:11 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileAIM7M.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileAIM7M extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileAIM7M(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileAIM7M(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public MissileAIM7M()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.MissileAIM7M.class;
        Property.set(class1, "mesh", "3do/arms/AIM7M/mono.sim");
        Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "EFFECTS/Smokes/SmokeBlack_missiles.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 60F);
        Property.set(class1, "timeFire", 40.8F);
        Property.set(class1, "force", 45000F);
        Property.set(class1, "forceT1", 0.5F);
        Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "forceT2", 0.2F);
        Property.set(class1, "forceP2", 50F);
        Property.set(class1, "dragCoefficient", 0.23F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 1.52F);
        Property.set(class1, "radius", 24.93F);
        Property.set(class1, "kalibr", 0.8F);
        Property.set(class1, "massa", 204F);
        Property.set(class1, "massaEnd", 85F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 2);
        Property.set(class1, "detectorType", 2);
        Property.set(class1, "sunRayAngle", 0.0F);
        Property.set(class1, "multiTrackingCapable", 1);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 5000L);
        Property.set(class1, "engineDelayTime", 0L);
        Property.set(class1, "attackDecisionByAI", 0);
        Property.set(class1, "targetType", 1);
        Property.set(class1, "shotFreq", 1.01F);
        Property.set(class1, "groundTrackFactor", 16F);
        Property.set(class1, "flareLockTime", 1200L);
        Property.set(class1, "trackDelay", 1500L);
        Property.set(class1, "failureRate", 10F);
        Property.set(class1, "maxLockGForce", 40F);
        Property.set(class1, "maxFOVfrom", 20F);
        Property.set(class1, "maxFOVto", 180F);
        Property.set(class1, "PkMaxFOVfrom", 25F);
        Property.set(class1, "PkMaxFOVto", 80F);
        Property.set(class1, "PkDistMin", 1000F);
        Property.set(class1, "PkDistOpt", 10000F);
        Property.set(class1, "PkDistMax", 40000F);
        Property.set(class1, "leadPercent", 100F);
        Property.set(class1, "maxGForce", 10F);
        Property.set(class1, "stepsForFullTurn", 15);
        Property.set(class1, "fxLock", "weapon.F4.lock");
        Property.set(class1, "fxNoLock", "weapon.F4.nolock");
        Property.set(class1, "smplLock", "F4_lock.wav");
        Property.set(class1, "smplNoLock", "F4_no_lock.wav");
        Property.set(class1, "friendlyName", "AIM-7M");
        Spawn.add(class1, new SPAWN());
    }
}