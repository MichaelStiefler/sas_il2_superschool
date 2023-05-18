package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class MissileBGM71A_gn16 extends Missile {

	static class SPAWN extends Missile.SPAWN {
		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
			new MissileBGM71A_gn16(actor, netchannel, i, point3d, orient, f);
		}
	}

	static {
		Class class1 = MissileBGM71A_gn16.class;
		Property.set(class1, "mesh", "3do/arms/BGM71A_TOW_gn16/mono.sim");
		Property.set(class1, "meshFly", "3do/arms/BGM71A_TOW_gn16/monoFly.sim");
		Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
		Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
		Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
		Property.set(class1, "smokeTrail", "3do/Effects/RocketSidewinder/RocketShutdownTrail.eff");
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 50F); // Emitted Smoke length
		Property.set(class1, "emitMax", 0.4F); // Amount of emitted Smoke
		Property.set(class1, "sound", "weapon.rocket_132"); // Sound of Rocket Motor
		Property.set(class1, "timeLife", 20F); // Rocket life time in Seconds
		Property.set(class1, "timeFire", 3.0F); // Rocket Engine Burn time in Seconds
		Property.set(class1, "force", 7000F); // Rocket Engine Power (in Newton)
		Property.set(class1, "timeSustain", 6.0F); // Rocket Sustained Motor Burn time in Seconds (0 for single stage rockets)
		Property.set(class1, "forceSustain", 800F); // Rocket Sustained Power (in Newton)
		Property.set(class1, "forceT1", 1.0F); // Time1, i.e. time until Rocket Engine force output maximum reached (in Seconds), 0 disables this feature
		Property.set(class1, "forceP1", 0.0F); // Power1, i.e. Rocket Engine force output at beginning (in Percent)
		Property.set(class1, "dragCoefficient", 0.25F); // Aerodynamic Drag Coefficient
		Property.set(class1, "powerType", 0); // Type of Explosion. 0="Splash", 1="Splinters", 2="Napalm"
		Property.set(class1, "power", 8.0F); // RL Data: 4.0kg HEAT warhead, and make twice converting in HE
		Property.set(class1, "radius", 0.6F); // Radius of Explosion Effect , HEAT is very narrow rafius
		Property.set(class1, "kalibr", 0.152F); // Diameter of Missile Body
		Property.set(class1, "massa", 18.9F); // Missile Weight at Launch time
		Property.set(class1, "massaEnd", 11.0F); // Missile Weight when Booster burned out
		Property.set(class1, "stepMode", Missile.STEP_MODE_HOMING); // target tracking mode
		Property.set(class1, "launchType", Missile.LAUNCH_TYPE_STRAIGHT); // launch pattern
		Property.set(class1, "detectorType", Missile.DETECTOR_TYPE_SACLOS); // detector type
		Property.set(class1, "sunRayAngle", 0.0F); // max. Angle at which the missile will track Sun Ray, zero disables Sun Ray tracking (only valid for IR detector missiles)
		Property.set(class1, "multiTrackingCapable", 1); // set whether or not this type of missile can fight multiple different targets simultaneously. Usually "1" for IR missiles and "0" for others.
		Property.set(class1, "canTrackSubs", 0); // set whether or not submerged objects can be tracked
		Property.set(class1, "minPkForAI", 15.0F); // min. Kill Probability for AI to launch a missile
		Property.set(class1, "timeForNextLaunchAI", 10000L); // time between two Missile launches for AI
		Property.set(class1, "engineDelayTime", -200L); // Rocket Engine Start Delay Time (in milliseconds), negative Value means that Engine is started before Missile leaves it's rail
		Property.set(class1, "attackDecisionByAI", Missile.ATTACK_DECISION_BY_AI_WAYPOINT); // let AI decide whether or not to attack. Usually "1" for short range missiles and "0" for all others.
		Property.set(class1, "targetType", Missile.TARGET_GROUND + Missile.TARGET_SHIP); // Type of valid targets, can be any combination of TARGET_AIR, TARGET_GROUND and TARGET_SHIP.
		Property.set(class1, "shotFreq", 1.0F); // Minimum time (in seconds) between two missile launches

		// Factor for missile tracking random ground targets issue.
		// Calculation: Angle to ground (in degrees) divided by:
		// (altitude in meters divided by 1000)^2
		Property.set(class1, "groundTrackFactor", 1024F); // lower value means higher probability of ground target tracking
		Property.set(class1, "flareLockTime", 1000L); // time (in milliseconds) for missile locking on to different target, i.e. flare (sun, ground clutter etc.)
		Property.set(class1, "trackDelay", 200L); // time (in milliseconds) for missile tracking target after launch (i.e. the time for the missile to fly straight first to be clear of the launching A/C)
		Property.set(class1, "failureRate", 18.0F); // Failure rate in percent
		Property.set(class1, "maxLockGForce", 9.0F); // max. G-Force for lockon
		Property.set(class1, "maxFOVfrom", 20.0F); // max Angle offset from launch A/C POV
		Property.set(class1, "maxFOVto", 360.0F); // max Angle offset from target aft POV
		Property.set(class1, "PkMaxFOVfrom", 15.0F); // max Angle offset from launch A/C POV for Pk calculation
		Property.set(class1, "PkMaxFOVto", 360.0F); // max Angle offset from target aft POV for Pk calculation
		Property.set(class1, "PkDistMin", 100.0F); // min Distance for Pk calculation
		Property.set(class1, "PkDistOpt", 1900.0F); // optimum Distance for Pk calculation
		Property.set(class1, "PkDistMax", 3000.0F); // max Distance for Pk calculation
		Property.set(class1, "leadPercent", 0.0F); // Track calculation lead value
		Property.set(class1, "maxGForce", 15.0F); // max turning rate G-Force
		Property.set(class1, "stepsForFullTurn", 8); // No. of ticks (1 tick = 30ms) for full control surface turn, higher value means slower reaction and smoother flight, lower value means higher agility
		Property.set(class1, "fxLock", "weapon.F4.lock"); // prs file for Lock Tone
		Property.set(class1, "fxLockVolume", 1.0F); // Volume for Lock Tone, "1" is default
		Property.set(class1, "fxNoLock", "weapon.F4.nolock"); // prs file for No Lock Tone
		Property.set(class1, "fxNoLockVolume", 1.0F); // Volume for No Lock Tone, "1" is default
		Property.set(class1, "smplLock", "F4_lock.wav"); // wav file for Lock Tone
		Property.set(class1, "smplNoLock", "F4_no_lock.wav"); // wav file for No Lock Tone
		Property.set(class1, "friendlyName", "TOW"); // Display Name of this missile
		Spawn.add(class1, new SPAWN());
	}

	public MissileBGM71A_gn16() {
	}

	public MissileBGM71A_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		this.MissileInit(actor, netchannel, i, point3d, orient, f);
	}
}