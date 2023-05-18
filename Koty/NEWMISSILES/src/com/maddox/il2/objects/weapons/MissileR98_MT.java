// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 05.01.2019 21:39:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileR98_MT.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileR98_MT extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileR98_MT(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileR98_MT(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public MissileR98_MT()
    {
    }


    static 
    {
        Class class1 = MissileR98_MT.class;
        Property.set(class1, "mesh", "3do/arms/R-98MT/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Aircraft/TurboHWK109D.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50.0F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 10F);
        Property.set(class1, "timeLife", 25F);
        Property.set(class1, "timeFire", 3F);
        Property.set(class1, "force", 110324F);
        Property.set(class1, "forceSustain", 0.0F);
        Property.set(class1, "forceT1", 1.0F);
        Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "forceT2", 0.2F);
        Property.set(class1, "forceP2", 50F);
        
        Property.set(class1, "spriteSustain", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
//      Property.set(class1, "flameSustain", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smokeSustain", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        Property.set(class1, "timeSustain", 22F);
  
        
        Property.set(class1, "dragCoefficient", 0.2F);
        Property.set(class1, "power", 15F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.5F);
        Property.set(class1, "massa", 260F);
        Property.set(class1, "massaEnd", 235F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 1);
        Property.set(class1, "detectorType", 3);
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
        Property.set(class1, "trackDelay", 1000L);
        Property.set(class1, "failureRate", 50F);
        Property.set(class1, "maxLockGForce", 2F);
        Property.set(class1, "maxFOVfrom", 1.75F);
        Property.set(class1, "maxFOVfrom_real", 12F);
        Property.set(class1, "maxFOVto", 60F);
        Property.set(class1, "PkMaxFOVfrom", 32F);
        Property.set(class1, "PkMaxFOVto", 70F);
        Property.set(class1, "PkDistMin", 2000F);
        Property.set(class1, "PkDistOpt", 6000F);
        Property.set(class1, "PkDistMax", 12000F);
        Property.set(class1, "leadPercent", 70F);
        Property.set(class1, "maxGForce", 6F);
        Property.set(class1, "stepsForFullTurn", 10F);
        Property.set(class1, "fxLock", "weapon.GAR8.lock");
        Property.set(class1, "fxLockVolume", 1.3F);
        Property.set(class1, "fxNoLock", "weapon.GAR8.nolock");
        Property.set(class1, "fxNoLockVolume", 1.0F);
        Property.set(class1, "smplLock", "GAR8_lock.wav");
        Property.set(class1, "smplNoLock", "GAR8_no_lock.wav");
        Property.set(class1, "friendlyName", "R-98MT");
        Spawn.add(class1, new SPAWN());
    }
}