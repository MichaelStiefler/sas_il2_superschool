// US Navy / Marine Corps AGM-123A "Skipper II" Laser Guided short-range Missle , Based on GBU-16 --- Mk83 bomb + PavewayII kit , plus Rocket motor
// A-6E TRAM and later (or early E + Laser targeting pod) , A-7 and early F/A-18 with Laser targeting pod.

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class MissileAGM123A_gn16 extends Missile  implements MissileInterceptable {

	static class SPAWN extends Missile.SPAWN {
		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
			new MissileAGM123A_gn16(actor, netchannel, i, point3d, orient, f);
		}
	}

	static {
		Class class1 = MissileAGM123A_gn16.class;
		Property.set(class1, "mesh", "3do/arms/AGM123A_gn16/mono.sim");
		Property.set(class1, "meshFly", "3do/arms/AGM123A_gn16/mono_open.sim");
		Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketMaverickFlame.sim");
		Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSparrowSmoke.eff");
		Property.set(class1, "smokeTrail", "3do/Effects/RocketSidewinder/RocketShutdownTrail.eff");
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 50F);
		Property.set(class1, "emitMax", 0.4F);
		Property.set(class1, "sound", "weapon.rocket_132");
		Property.set(class1, "timeLife", 400F);
		Property.set(class1, "timeFire", 20.8F);
		Property.set(class1, "force", 16000F);
		Property.set(class1, "forceT1", 0.45F);
		Property.set(class1, "forceP1", 0.0F);
		Property.set(class1, "forceT2", 0.18F);
		Property.set(class1, "forceP2", 50F);
		Property.set(class1, "dragCoefficient", 0.20F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "power", 275F);
		Property.set(class1, "radius", 150F);
		Property.set(class1, "kalibr", 0.202F);
		Property.set(class1, "massa", 582F);
		Property.set(class1, "massaEnd", 512F);
		Property.set(class1, "stepMode", Missile.STEP_MODE_HOMING);
		Property.set(class1, "launchType", Missile.LAUNCH_TYPE_DROP);
		Property.set(class1, "detectorType", Missile.DETECTOR_TYPE_LASER);
		Property.set(class1, "sunRayAngle", 0.0F);
		Property.set(class1, "multiTrackingCapable", 1);
		Property.set(class1, "canTrackSubs", 0);
		Property.set(class1, "minPkForAI", 25F);
		Property.set(class1, "timeForNextLaunchAI", 5000L);
		Property.set(class1, "engineDelayTime", -100L);
		Property.set(class1, "attackDecisionByAI", Missile.ATTACK_DECISION_BY_AI_WAYPOINT);
		Property.set(class1, "targetType", Missile.TARGET_GROUND + Missile.TARGET_SHIP);
		Property.set(class1, "shotFreq", 1.01F);
		Property.set(class1, "groundTrackFactor", 256F);
		Property.set(class1, "flareLockTime", 1000L);
		Property.set(class1, "trackDelay", 200L);
		Property.set(class1, "failureRate", 16F);
		Property.set(class1, "maxLockGForce", 15F);
		Property.set(class1, "maxFOVfrom", 30F);
		Property.set(class1, "maxFOVto", 360F);
		Property.set(class1, "PkMaxFOVfrom", 25F);
		Property.set(class1, "PkMaxFOVto", 360F);
		Property.set(class1, "PkDistMin", 1000F);
		Property.set(class1, "PkDistOpt", 4000F);
		Property.set(class1, "PkDistMax", 9000F);
		Property.set(class1, "leadPercent", 30F);
		Property.set(class1, "maxGForce", 12F);
		Property.set(class1, "stepsForFullTurn", 12);
		Property.set(class1, "fxLock", "weapon.F4.lock");
		Property.set(class1, "fxNoLock", "weapon.F4.nolock");
		Property.set(class1, "smplLock", "F4_lock.wav");
		Property.set(class1, "smplNoLock", "F4_no_lock.wav");
		Property.set(class1, "friendlyName", "AGM-123");
		Spawn.add(class1, new SPAWN());
	}

	public MissileAGM123A_gn16() {
	}

	public MissileAGM123A_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		this.MissileInit(actor, netchannel, i, point3d, orient, f);
	}
}