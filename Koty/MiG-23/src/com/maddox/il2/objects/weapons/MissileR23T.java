// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 08.12.2019 15:00:09
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileR23T.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileR23T extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileR23T(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileR23T(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public MissileR23T()
    {
    }


    static 
    {
        Class class1 = MissileR23T.class;
        Property.set(class1, "mesh", "3do/arms/R-23T/mono.sim");
        /*Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSparrowFlame.sim");
        */
        Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSparrowFlame.sim");
        Property.set(class1, "smoke", "3DO/Effects/Aircraft/TurboHWK109D.eff");
        
        //Property.set(class1, "smokeTrail", "3do/Effects/RocketSidewinder/RocketShutdownTrail2.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 10F);
        Property.set(class1, "timeLife", 45F);
        Property.set(class1, "timeFire", 3.5F);
        Property.set(class1, "force", 31000F);
        Property.set(class1, "dragCoefficient", 0.5F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 215F);
        Property.set(class1, "massaEnd", 175F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 1);
        Property.set(class1, "detectorType", 1);
        Property.set(class1, "sunRayAngle", 22F);
        Property.set(class1, "multiTrackingCapable", 1);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 30F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", 0L);
        Property.set(class1, "attackDecisionByAI", 1);
        Property.set(class1, "targetType", 1);
        Property.set(class1, "shotFreq", 0.5F);
        Property.set(class1, "groundTrackFactor", 16F);
        Property.set(class1, "flareLockTime", 1000L);
        Property.set(class1, "trackDelay", 2000L);
        Property.set(class1, "failureRate", 50F);
        Property.set(class1, "maxLockGForce", 5F);
        Property.set(class1, "maxFOVfrom", 24F);
        Property.set(class1, "maxFOVto", 80F);
        Property.set(class1, "PkMaxFOVfrom", 32F);
        Property.set(class1, "PkMaxFOVto", 70F);
        Property.set(class1, "PkDistMin", 1000F);
        Property.set(class1, "PkDistOpt", 7000F);
        Property.set(class1, "PkDistMax", 20000F);
        Property.set(class1, "leadPercent", 100F);
        Property.set(class1, "maxGForce", 15F);
        Property.set(class1, "stepsForFullTurn", 12F);
        Property.set(class1, "fxLock", "weapon.R60.lock");
        Property.set(class1, "fxNoLock", "weapon.R60.nolock");
        Property.set(class1, "smplLock", "R60_lock.wav");
        Property.set(class1, "smplNoLock", "R60_no_lock.wav");
        Property.set(class1, "friendlyName", "R-23T");
        Property.set(class1, "typeMyotka", 1.25f);
        Spawn.add(class1, new SPAWN());
    }
}