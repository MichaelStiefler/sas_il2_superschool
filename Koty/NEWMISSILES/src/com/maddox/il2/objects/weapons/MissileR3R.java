// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 19.11.2018 22:42:38
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileR3R.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileR3R extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileR3R(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileR3R(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public MissileR3R()
    {
    }

    static 
    {
        Class class1 = MissileR3R.class;
        Property.set(class1, "mesh", "3do/arms/R-3R/mono.sim");
        Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 35F);
        Property.set(class1, "timeFire", 2.4F);
        Property.set(class1, "force", 12000F);
        Property.set(class1, "forceT1", 0.7F);
        Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "forceT2", 0.2F);
        Property.set(class1, "forceP2", 50F);
        Property.set(class1, "dragCoefficient", 0.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 0.42F);
        Property.set(class1, "radius", 8.96F);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 90F);
        Property.set(class1, "massaEnd", 63F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 2);
        Property.set(class1, "detectorType", 2);
        
        Property.set(class1, "spriteSustain", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
//      Property.set(class1, "flameSustain", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
      Property.set(class1, "smokeSustain", "3DO/Effects/Tracers/GuidedRocket/White.eff");
      Property.set(class1, "timeSustain", 12.2F);
        
        
        Property.set(class1, "sunRayAngle", 0.0F);
        Property.set(class1, "multiTrackingCapable", 1);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", 0L);
        Property.set(class1, "attackDecisionByAI", 1);
        Property.set(class1, "targetType", 1);
        Property.set(class1, "shotFreq", 1.01F);
        Property.set(class1, "groundTrackFactor", 10F);
        Property.set(class1, "flareLockTime", 1000L);
        Property.set(class1, "trackDelay", 1000L);
        Property.set(class1, "failureRate", 30F);
        Property.set(class1, "maxLockGForce", 2F);
        Property.set(class1, "maxFOVfrom", 1.75F);
        Property.set(class1, "maxFOVfrom_real", 25F);
        Property.set(class1, "maxFOVto", 180F);
        Property.set(class1, "PkMaxFOVfrom", 3F);
        Property.set(class1, "PkMaxFOVto", 70F);
        Property.set(class1, "PkDistMin", 1000F);
        Property.set(class1, "PkDistOpt", 3000F);
        Property.set(class1, "PkDistMax", 8000F);
        Property.set(class1, "maxSpeed", 2012.214F);
        Property.set(class1, "leadPercent", 100F);
        Property.set(class1, "maxGForce", 11F);
        Property.set(class1, "stepsForFullTurn", 10);
        Property.set(class1, "fxLock", "weapon.K5.lock");
        Property.set(class1, "fxNoLock", "weapon.K5.nolock");
        Property.set(class1, "smplLock", "K5_lock.wav");
        Property.set(class1, "smplNoLock", "K5_no_lock.wav");
        Property.set(class1, "friendlyName", "R-3R");
        Spawn.add(class1, new SPAWN());
    }
}