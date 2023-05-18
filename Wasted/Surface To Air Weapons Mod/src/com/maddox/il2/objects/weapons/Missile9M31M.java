package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.*;


public class Missile9M31M extends MissileSAM
{
	static class SPAWN extends MissileSAM.SPAWN
	{
		SPAWN() {}

		public void doSpawn(Actor paramActor, NetChannel paramNetChannel, int paramInt, Point3d paramPoint3d, Orient paramOrient, float paramFloat, Actor radartarget)
		{
			new Missile9M31M(paramActor, paramNetChannel, paramInt, paramPoint3d, paramOrient, paramFloat, radartarget);
		}
	}

   public Missile9M31M(Actor paramActor, NetChannel paramNetChannel, int paramInt, Point3d paramPoint3d, Orient paramOrient, float paramFloat, Actor radartarget)
   {
     MissileSAMInit(paramActor, paramNetChannel, paramInt, paramPoint3d, paramOrient, paramFloat, radartarget);
   }
   
   public Missile9M31M() {}

    static 
    {
        Class localClass = com.maddox.il2.objects.weapons.Missile9M31M.class;
        Property.set(localClass, "mesh", "3DO/Arms/Hydra70_275inch_gn16/Hydra70fly.sim");
        Property.set(localClass, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        Property.set(localClass, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(localClass, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(localClass, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(localClass, "emitLen", 50F);
        Property.set(localClass, "emitMax", 0.4F);
        Property.set(localClass, "sound", "weapon.rocket_132");
        Property.set(localClass, "radius", 3.6F);
        Property.set(localClass, "timeLife", 16F);
        Property.set(localClass, "timeFire", 14F);
        Property.set(localClass, "force", 10000.0F);
        Property.set(localClass, "timeSustain", 0.0F);
        Property.set(localClass, "forceSustain", 0.0F);
        Property.set(localClass, "forceT1", 0.5F);
        Property.set(localClass, "forceP1", 0.0F);
        Property.set(localClass, "dragCoefficient", 0.4F);
        Property.set(localClass, "power", 3.0F);
        Property.set(localClass, "powerType", 0);
        Property.set(localClass, "kalibr", 0.120F);
        Property.set(localClass, "massa", 30.5F);
        Property.set(localClass, "massaEnd", 17.1F);
        Property.set(localClass, "stepMode", 0);
        Property.set(localClass, "launchType", 1);
        Property.set(localClass, "detectorType", 1);
        Property.set(localClass, "sunRayAngle", 5.0F);
        Property.set(localClass, "multiTrackingCapable", 1);
        Property.set(localClass, "canTrackSubs", 0);
        Property.set(localClass, "engineDelayTime", -200L);
        Property.set(localClass, "targetType", 1);
        Property.set(localClass, "shotFreq", 0.01F);
        Property.set(localClass, "groundTrackFactor", 5.0F);
        Property.set(localClass, "flareLockTime", 1000L);
        Property.set(localClass, "trackDelay", 500L);
        Property.set(localClass, "failureRate", 30.0F);
        Property.set(localClass, "maxLockGForce", 6.0F);
        Property.set(localClass, "maxFOVfrom", 40.0F);
        Property.set(localClass, "maxFOVto", 360.0F);
        Property.set(localClass, "PkDistMax", 4200.0F);
        Property.set(localClass, "leadPercent", 100.0F);
        Property.set(localClass, "maxGForce", 20.0F);
        Property.set(localClass, "stepsForFullTurn", 7);
        Property.set(localClass, "friendlyName", "Strela 1");
        Spawn.add(localClass, new SPAWN());
    }


}