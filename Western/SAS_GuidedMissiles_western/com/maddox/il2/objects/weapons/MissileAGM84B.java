package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class MissileAGM84B extends Missile implements MissileInterceptable {
	static class SPAWN extends Missile.SPAWN {

		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
			new MissileAGM84B(actor, netchannel, i, point3d, orient, f);
		}
	}

	static {
		Class class1 = MissileAGM84B.class;
		Property.set(class1, "mesh", "3do/arms/AGM-84A/mono.sim");
		Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
		Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
		Property.set(class1, "smoke", "EFFECTS/Smokes/SmokeBlack_missiles.eff");
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 50F);
		Property.set(class1, "emitMax", 0.4F);
		Property.set(class1, "sound", "weapon.rocket_132");
		Property.set(class1, "timeLife", 400F);
		Property.set(class1, "timeFire", 300F);
		Property.set(class1, "force", 35000F);
		Property.set(class1, "forceT1", 10F);
		Property.set(class1, "forceP1", 0.0F);
		Property.set(class1, "forceT2", 1.0F);
		Property.set(class1, "forceP2", 100F);
		Property.set(class1, "dragCoefficient", 0.3F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "power", 222F);
		Property.set(class1, "radius", 250F);
		Property.set(class1, "kalibr", 0.343F);
		Property.set(class1, "massa", 511F);
		Property.set(class1, "massaEnd", 356F);
		Property.set(class1, "stepMode", 0);
		Property.set(class1, "launchType", 2);
		Property.set(class1, "detectorType", 4);
		Property.set(class1, "sunRayAngle", 0.0F);
		Property.set(class1, "multiTrackingCapable", 1);
		Property.set(class1, "canTrackSubs", 0);
		Property.set(class1, "minPkForAI", 25F);
		Property.set(class1, "timeForNextLaunchAI", 10000L);
		Property.set(class1, "engineDelayTime", 600L);
		Property.set(class1, "attackDecisionByAI", 2);
		Property.set(class1, "targetType", Missile.TARGET_SHIP);
		Property.set(class1, "shotFreq", 5F);
		Property.set(class1, "groundTrackFactor", 3.402823E+038F);
		Property.set(class1, "flareLockTime", 1000L);
		Property.set(class1, "trackDelay", 5000L);
		Property.set(class1, "failureRate", 10F);
		Property.set(class1, "maxLockGForce", 6F);
		Property.set(class1, "maxFOVfrom", 30F);
		Property.set(class1, "maxFOVto", 360F);
		Property.set(class1, "PkMaxFOVfrom", 35F);
		Property.set(class1, "PkMaxFOVto", 3.402823E+038F);
		Property.set(class1, "PkDistMin", 1000F);
		Property.set(class1, "PkDistOpt", 120000F);
		Property.set(class1, "PkDistMax", 200000F);
		Property.set(class1, "leadPercent", 50F);
		Property.set(class1, "maxGForce", 6F);
		Property.set(class1, "stepsForFullTurn", 25);
		Property.set(class1, "fxLock", "weapon.F4.lock");
		Property.set(class1, "fxNoLock", "weapon.F4.nolock");
		Property.set(class1, "smplLock", "F4_lock.wav");
		Property.set(class1, "smplNoLock", "F4_no_lock.wav");
		Property.set(class1, "friendlyName", "AGM-84B");
		Spawn.add(class1, new SPAWN());
	}

	public MissileAGM84B() {
	}

	public MissileAGM84B(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		this.MissileInit(actor, netchannel, i, point3d, orient, f);
	}
}