// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 14.01.2020 2:17:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileAIM7M_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileAIM7M_gn16 extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileAIM7M_gn16(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileAIM7M_gn16()
    {
    }

    public MissileAIM7M_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }



    static 
    {
        Class class1 = MissileAIM7M_gn16.class;
        Property.set(class1, "mesh", "3do/arms/AIM7M_gn16/mono.sim");
        //Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSparrowFlame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSparrowSmokeBlack.eff");
        Property.set(class1, "smokeTrail", "3do/Effects/RocketSidewinder/RocketShutdownTrail2.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 70F);
        Property.set(class1, "timeFire", 4.5F);
        Property.set(class1, "force", 25577);//49500F);
        Property.set(class1, "timeSustain", 11F);
        Property.set(class1, "forceSustain", 4500);//12700F);
        //Property.set(class1, "forceT1", 0.5F);
        //Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "dragCoefficient", 0.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 4F);
        Property.set(class1, "radius", 25F);
        Property.set(class1, "proximityFuzeRadius", 40F);
        Property.set(class1, "kalibr", 0.203F);
        Property.set(class1, "massa", 231F);
        Property.set(class1, "massaEnd", 200F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 2);
        Property.set(class1, "detectorType", 2);
        Property.set(class1, "sunRayAngle", 0.0F);
        Property.set(class1, "multiTrackingCapable", 0);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", 100L);
        Property.set(class1, "attackDecisionByAI", 1);
        Property.set(class1, "targetType", 1);
        Property.set(class1, "shotFreq", 0.01F);
        Property.set(class1, "groundTrackFactor", 16F);
        Property.set(class1, "flareLockTime", 1200L);
        Property.set(class1, "trackDelay", 2500L);
        Property.set(class1, "failureRate", 15F);
        Property.set(class1, "maxLockGForce", 40F);
        Property.set(class1, "maxFOVfrom", 20F);
        Property.set(class1, "maxFOVto", 360F);
        Property.set(class1, "PkMaxFOVfrom", 25F);
        Property.set(class1, "PkMaxFOVto", 80F);
        Property.set(class1, "PkDistMin", 1000F);
        Property.set(class1, "PkDistOpt", 10000F);
        Property.set(class1, "PkDistMax", 40000F);
        Property.set(class1, "leadPercent", 100F);
        Property.set(class1, "maxGForce", 25F);
        Property.set(class1, "stepsForFullTurn", 7);
        Property.set(class1, "fxLock", "weapon.AIM7.lock");
        Property.set(class1, "fxLockVolume", 1.0F);
        Property.set(class1, "fxNoLock", (String)null);
        Property.set(class1, "fxNoLockVolume", 1.0F);
        Property.set(class1, "smplLock", "AIM7_lock.wav");
        Property.set(class1, "smplNoLock", (String)null);
        Property.set(class1, "friendlyName", "AIM-7M");
        Spawn.add(class1, new SPAWN());
    }
}