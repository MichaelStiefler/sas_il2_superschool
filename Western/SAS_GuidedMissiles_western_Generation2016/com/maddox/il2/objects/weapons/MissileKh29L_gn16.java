// Kh-29L : Russian Laser guided Air-to-Ground missile 600kg
// Last Modified by: western0221 2018-12-30

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class MissileKh29L_gn16 extends Missile  implements MissileInterceptable {

	static class SPAWN extends Missile.SPAWN {
		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
			new MissileKh29L_gn16(actor, netchannel, i, point3d, orient, f);
		}
	}

	static {
		Class class1 = MissileKh29L_gn16.class;
		Property.set(class1, "mesh", "3do/arms/Kh29_gn16/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketMaverickFlame.sim");
        Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
		Property.set(class1, "smokeTrail", "3do/Effects/RocketSidewinder/RocketShutdownTrail.eff");
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 50F);
		Property.set(class1, "emitMax", 0.4F);
		Property.set(class1, "sound", "weapon.rocket_132");
		Property.set(class1, "timeLife", 20F);
		Property.set(class1, "timeFire", 9.0F);
		Property.set(class1, "force", 24000F);
		Property.set(class1, "forceT1", 2.0F);
		Property.set(class1, "forceP1", 0.0F);
		Property.set(class1, "forceT2", 3.0F);
		Property.set(class1, "forceP2", 50F);
		Property.set(class1, "dragCoefficient", 0.30F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "power", 600F);
		Property.set(class1, "radius", 70F);
		Property.set(class1, "kalibr", 0.380F);
		Property.set(class1, "massa", 630F);
		Property.set(class1, "massaEnd", 450F);
		Property.set(class1, "stepMode", Missile.STEP_MODE_HOMING);
		Property.set(class1, "launchType", Missile.LAUNCH_TYPE_DROP);
		Property.set(class1, "detectorType", Missile.DETECTOR_TYPE_LASER);
		Property.set(class1, "sunRayAngle", 0.0F);
		Property.set(class1, "multiTrackingCapable", 1);
		Property.set(class1, "canTrackSubs", 0);
		Property.set(class1, "minPkForAI", 25F);
		Property.set(class1, "timeForNextLaunchAI", 10000L);
		Property.set(class1, "engineDelayTime", -200L);
		Property.set(class1, "attackDecisionByAI", Missile.ATTACK_DECISION_BY_AI_WAYPOINT);
		Property.set(class1, "targetType", Missile.TARGET_GROUND + Missile.TARGET_SHIP);
		Property.set(class1, "shotFreq", 6.01F);
		Property.set(class1, "groundTrackFactor", 250F);
		Property.set(class1, "flareLockTime", 1000L);
		Property.set(class1, "trackDelay", 200L);
		Property.set(class1, "failureRate", 15F);
		Property.set(class1, "maxLockGForce", 14F);
		Property.set(class1, "maxFOVfrom", 30F);
		Property.set(class1, "maxFOVto", 360F);
		Property.set(class1, "PkMaxFOVfrom", 25F);
		Property.set(class1, "PkMaxFOVto", 360F);
		Property.set(class1, "PkDistMin", 1000F);
		Property.set(class1, "PkDistOpt", 5000F);
		Property.set(class1, "PkDistMax", 11000F);
		Property.set(class1, "leadPercent", 30F);
		Property.set(class1, "maxGForce", 12F);
		Property.set(class1, "stepsForFullTurn", 17);
        Property.set(class1, "fxLock", (String)null);
        Property.set(class1, "fxNoLock", (String)null);
        Property.set(class1, "smplLock", (String)null);
        Property.set(class1, "smplNoLock", (String)null);
		Property.set(class1, "friendlyName", "Kh-29L");
		Spawn.add(class1, new SPAWN());
	}

	public MissileKh29L_gn16() {
	}

	public MissileKh29L_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		this.MissileInit(actor, netchannel, i, point3d, orient, f);
	}
}