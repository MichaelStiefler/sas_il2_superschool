package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class MissileKh15 extends Missile implements MissileInterceptable {
	static class SPAWN extends Missile.SPAWN {

		SPAWN() {
		}

		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
			new MissileKh15(actor, netchannel, i, point3d, orient, f);
		}
	}

        static Class class$com$maddox$il2$objects$weapons$MissileKh15;

    static Class class$(String s)
    {
        Class c;
        try{
            c = Class.forName(s);
        } catch ( ClassNotFoundException e ){
            throw new NoClassDefFoundError(e.getMessage());
        }
        return c;
    }


  static {
		Class class1 = (class$com$maddox$il2$objects$weapons$MissileKh15 == null
	       ? (class$com$maddox$il2$objects$weapons$MissileKh15
		  = class$("com.maddox.il2.objects.weapons.MissileKh15"))
	       : class$com$maddox$il2$objects$weapons$MissileKh15);

		Property.set(class1, "mesh", "3do/arms/Kh_15/mono.sim");
		Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
		Property.set(class1, "flame", "3do/Effects/Kh15/Kh15Flame.sim");
		Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 50F);
		Property.set(class1, "emitMax", 0.4F);
		Property.set(class1, "sound", "weapon.rocket_132");
		Property.set(class1, "timeLife", 320F);
		Property.set(class1, "timeFire", 250F);
		Property.set(class1, "force", 42000F);
		Property.set(class1, "forceT1", 2.5F);
		Property.set(class1, "forceP1", 0.0F);
		Property.set(class1, "forceT2", 0.6F);
		Property.set(class1, "forceP2", 100F);
		Property.set(class1, "dragCoefficient", 0.3F);
		Property.set(class1, "dragCoefficientTurn", 0.8F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "power", 130F);
		Property.set(class1, "radius", 150F);
		Property.set(class1, "kalibr", 0.455F);
		Property.set(class1, "massa", 1200F);
		Property.set(class1, "massaEnd", 500F);
		Property.set(class1, "stepMode", 0);
		Property.set(class1, "launchType", 2);
		Property.set(class1, "detectorType", 2);
		Property.set(class1, "sunRayAngle", 0.0F);
		Property.set(class1, "multiTrackingCapable", 0);
		Property.set(class1, "canTrackSubs", 0);
		Property.set(class1, "minPkForAI", 25F);
		Property.set(class1, "timeForNextLaunchAI", 10000L);
		Property.set(class1, "engineDelayTime", 0L);
		Property.set(class1, "attackDecisionByAI", 2);
		Property.set(class1, "targetType", TARGET_LOCATE);
		Property.set(class1, "shotFreq", 5F);
		Property.set(class1, "groundTrackFactor", 3.402823E+038F);
		Property.set(class1, "flareLockTime", 1000L);
		Property.set(class1, "trackDelay", 5000L);
		Property.set(class1, "failureRate", 5F);
		Property.set(class1, "maxLockGForce", 6F);
		Property.set(class1, "maxFOVfrom", 30F);
		Property.set(class1, "maxFOVto", 360F);
		Property.set(class1, "PkMaxFOVfrom", 35F);
		Property.set(class1, "PkMaxFOVto", 3.402823E+038F);
		Property.set(class1, "PkDistMin", 1000F);
		Property.set(class1, "PkDistOpt", 60000F);
		Property.set(class1, "PkDistMax", 400000F);
		Property.set(class1, "leadPercent", 50F);
		Property.set(class1, "maxGForce", 10F);
		Property.set(class1, "stepsForFullTurn", 10);
		Property.set(class1, "fxLock", "weapon.K5.lock");
		Property.set(class1, "fxNoLock", "weapon.K5.nolock");
		Property.set(class1, "smplLock", "K5_lock.wav");
		Property.set(class1, "smplNoLock", "K5_no_lock.wav");
		Property.set(class1, "friendlyName", "Kh-15");
		Property.set(class1, "iconFar_shortClassName", "Kh-15 Missile");
		Spawn.add(class1, new SPAWN());
	}

	public MissileKh15() {
	}

	public MissileKh15(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		this.MissileInit(actor, netchannel, i, point3d, orient, f);
	}
}
