package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class MissileAGM88A_gn16 extends Missile {
	static class SPAWN extends Missile.SPAWN {

		SPAWN() {
		}

		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
			new MissileAGM88A_gn16(actor, netchannel, i, point3d, orient, f);
		}
	}

	static {
		Class class1 = MissileAGM88A_gn16.class;
		Property.set(class1, "mesh", "3do/arms/AGM88_HARM_gn16/mono.sim");
//		Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");  // AGM-88 is no smoke
		Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSparrowFlame.sim");
		Property.set(class1, "smoke", "EFFECTS/Smokes/SmokeMissilessmall.eff");
//		Property.set(class1, "smokeTrail", "3do/Effects/RocketSidewinder/RocketShutdownTrail2.eff");  // AGM-88 is no smoke
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 50F);
		Property.set(class1, "emitMax", 0.4F);
		Property.set(class1, "sound", "weapon.rocket_132");
		Property.set(class1, "timeLife", 180F);
		Property.set(class1, "timeFire", 15F);
		Property.set(class1, "force", 17000F);
		Property.set(class1, "timeSustain", 130F);
		Property.set(class1, "forceSustain", 10500F);
		Property.set(class1, "forceT1", 2F);
		Property.set(class1, "forceP1", 0.0F);
		Property.set(class1, "forceT2", 0.2F);
		Property.set(class1, "forceP2", 50F);
		Property.set(class1, "dragCoefficient", 0.3F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "power", 45.4F);
		Property.set(class1, "radius", 10.0F);
		Property.set(class1, "kalibr", 0.254F);
		Property.set(class1, "massa", 355F);
		Property.set(class1, "massaEnd", 90F);
		Property.set(class1, "stepMode", Missile.STEP_MODE_HOMING);
		Property.set(class1, "launchType", Missile.LAUNCH_TYPE_STRAIGHT);
		Property.set(class1, "detectorType", Missile.DETECTOR_TYPE_ANTI_RADIATION_LOCREC);
		Property.set(class1, "sunBrightThreshold", 0.0F);
		Property.set(class1, "sunRayAngle", 0.0F);
		Property.set(class1, "multiTrackingCapable", 1);
		Property.set(class1, "canTrackSubs", 0);
		Property.set(class1, "minPkForAI", 20F);
		Property.set(class1, "timeForNextLaunchAI", 10000L);
		Property.set(class1, "engineDelayTime", -100L);
		Property.set(class1, "attackDecisionByAI", Missile.ATTACK_DECISION_BY_AI_WAYPOINT);
		Property.set(class1, "targetType", Missile.TARGET_GROUND);
		Property.set(class1, "shotFreq", 0.01D);
		Property.set(class1, "groundTrackFactor", 24F);
		Property.set(class1, "flareLockTime", 1000L);
		Property.set(class1, "trackDelay", 1000L);
		Property.set(class1, "failureRate", 12F);
		Property.set(class1, "maxLockGForce", 6F);
		Property.set(class1, "maxFOVfrom", 40F);
		Property.set(class1, "maxFOVto", 360F);
		Property.set(class1, "PkMaxFOVfrom", 30F);
		Property.set(class1, "PkMaxFOVto", 360F);
		Property.set(class1, "PkDistMin", 8000F);
		Property.set(class1, "PkDistOpt", 60000F);
		Property.set(class1, "PkDistMax", 140000F);
		Property.set(class1, "leadPercent", 0.0F);
		Property.set(class1, "maxGForce", 4F);
		Property.set(class1, "stepsForFullTurn", 12);
		Property.set(class1, "fxLock", "weapon.F4.lock");
		Property.set(class1, "fxNoLock", "weapon.F4.nolock");
		Property.set(class1, "smplLock", "F4_lock.wav");
		Property.set(class1, "smplNoLock", "F4_no_lock.wav");
		Property.set(class1, "friendlyName", "AGM-88A");
		Spawn.add(class1, new SPAWN());
	}

	public MissileAGM88A_gn16() {
	}

    public MissileAGM88A_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		this.MissileInit(actor, netchannel, i, point3d, orient, f);
	}
}
