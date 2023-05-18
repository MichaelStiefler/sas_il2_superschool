// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 27.11.2018 16:15:59
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileAIM9E_gn16.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileAIM9E_gn16 extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileAIM9E_gn16(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileAIM9E_gn16()
    {
    }

    public MissileAIM9E_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }


    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.MissileAIM9E_gn16.class;
        Property.set(class1, "mesh", "3do/arms/AIM9E_gn16/mono.sim");
        Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 20F);
        Property.set(class1, "timeFire", 2.7F);
        Property.set(class1, "force", 14000F);
        Property.set(class1, "forceSustain", 0.0F);
        Property.set(class1, "spriteSustain", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
//      Property.set(class1, "flameSustain", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smokeSustain", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        Property.set(class1, "timeSustain", 17F);
        Property.set(class1, "forceT1", 0.5F);
        Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "dragCoefficient", 0.6F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 0.54F);
        Property.set(class1, "radius", 7.45F);
        Property.set(class1, "proximityFuzeRadius", 18F);
        Property.set(class1, "kalibr", 0.16F);
        Property.set(class1, "massa", 74.5F);
        Property.set(class1, "massaEnd", 50.5F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 1);
        Property.set(class1, "detectorType", 1);
        Property.set(class1, "sunRayAngle", 5F);
        Property.set(class1, "multiTrackingCapable", 1);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", 0L);
        Property.set(class1, "attackDecisionByAI", 1);
        Property.set(class1, "targetType", 1);
        Property.set(class1, "shotFreq", 0.01F);
        Property.set(class1, "groundTrackFactor", 16F);
        Property.set(class1, "flareLockTime", 1000L);
        Property.set(class1, "trackDelay", 1000L);
        Property.set(class1, "failureRate", 30F);
        Property.set(class1, "maxLockGForce", 4F);
        Property.set(class1, "maxFOVfrom", 1.25F);
        Property.set(class1, "maxFOVfrom_real", 16.5F);
        Property.set(class1, "maxFOVto", 60F);
        Property.set(class1, "PkMaxFOVfrom", 2F);
        Property.set(class1, "PkMaxFOVto", 70F);
        Property.set(class1, "PkDistMin", 400F);
        Property.set(class1, "PkDistOpt", 4500F);
        Property.set(class1, "PkDistMax", 8000F);
        Property.set(class1, "leadPercent", 60F);
        Property.set(class1, "maxGForce", 15F);
        Property.set(class1, "stepsForFullTurn", 5);
        Property.set(class1, "fxLock", "weapon.AIM9E.lock");
        Property.set(class1, "fxLockVolume", 1.0F);
        Property.set(class1, "fxNoLock", "weapon.AIM9E.nolock");
        Property.set(class1, "fxNoLockVolume", 0.5F);
        Property.set(class1, "smplLock", "AIM9E_lock.wav");
        Property.set(class1, "smplNoLock", "AIM9E_no_lock.wav");
        Property.set(class1, "friendlyName", "AIM-9E");
        Spawn.add(class1, new SPAWN());
    }
}