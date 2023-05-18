// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 02/11/2015 03:26:52 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketAIM9B.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            AIM9BMissile

public class RocketAIM9B extends AIM9BMissile
{
    static class SPAWN extends AIM9BMissile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new RocketAIM9B(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public RocketAIM9B(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public RocketAIM9B()
    {
        MissileInit();
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketAIM9B.class;
        Property.set(class1, "mesh", "3do/arms/AIM9B/mono.sim");
        Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 30F);
        Property.set(class1, "timeFire", 2.2F);
        Property.set(class1, "force", 12000F);
        Property.set(class1, "forceT1", 0.5F);
        Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "forceT2", 0.2F);
        Property.set(class1, "forceP2", 50F);
        Property.set(class1, "dragCoefficient", 0.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 0.45F);
        Property.set(class1, "radius", 9.14F);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 70F);
        Property.set(class1, "massaEnd", 45F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 1);
        Property.set(class1, "detectorType", 1);
        Property.set(class1, "sunRayAngle", 20F);
        Property.set(class1, "groundTrackFactor", 10F);
        Property.set(class1, "flareLockTime", 1000L);
        Property.set(class1, "trackDelay", 1000L);
        Property.set(class1, "failureRate", 50F);
        Property.set(class1, "maxLockGForce", 2.0F);
        Property.set(class1, "maxFOVfrom", 25F);
        Property.set(class1, "maxFOVto", 60F);
        Property.set(class1, "PkMaxFOVfrom", 30F);
        Property.set(class1, "PkMaxFOVto", 70F);
        Property.set(class1, "PkDistMin", 400F);
        Property.set(class1, "PkDistOpt", 1500F);
        Property.set(class1, "PkDistMax", 4500F);
        Property.set(class1, "leadPercent", 100F);
        Property.set(class1, "maxGForce", 12F);
        Property.set(class1, "stepsForFullTurn", 12);
        Property.set(class1, "fxLock", "weapon.AIM9.lock");
        Property.set(class1, "fxNoLock", "weapon.AIM9.nolock");
        Property.set(class1, "smplLock", "AIM9_lock.wav");
        Property.set(class1, "smplNoLock", "AIM9_no_lock.wav");
        Property.set(class1, "friendlyName", "AIM-9B");
        Spawn.add(class1, new SPAWN());
    }
}
