// Source File Name: MissileAIM4D_gn16.java
// Author:           western0221
// Last Modified by: western0221 2018-02-07
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class MissileAIM4D_gn16 extends Missile {

	static class SPAWN extends Missile.SPAWN {

		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
			new MissileAIM4D_gn16(actor, netchannel, i, point3d, orient, f);
		}
	}

	static {
		Class class1 = MissileAIM4D_gn16.class;
		Property.set(class1, "mesh", "3do/arms/AIM4D_gn16/mono.sim");
		Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
		Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
		Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
		Property.set(class1, "smokeTrail", "3do/Effects/RocketSidewinder/RocketShutdownTrail.eff");
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 50F); // Emitted Smoke length
		Property.set(class1, "emitMax", 0.4F); // Amount of emitted Smoke
		Property.set(class1, "sound", "weapon.rocket_132"); // Sound of Rocket Motor
		Property.set(class1, "timeLife", 30F); // Rocket life time in Seconds
		Property.set(class1, "timeFire", 3.3F); // Rocket Engine Burn time in Seconds
		Property.set(class1, "force", 13000F); // Rocket Engine Power (in Newton)
		Property.set(class1, "timeSustain", 0F); // Rocket Sustained Motor Burn time in Seconds (0 for single stage rockets)
		Property.set(class1, "forceSustain", 0F); // Rocket Sustained Power (in Newton)
		Property.set(class1, "forceT1", 0.5F); // Time1, i.e. time until Rocket Engine force output maximum reached (in Seconds), 0 disables this feature
		Property.set(class1, "forceP1", 0.0F); // Power1, i.e. Rocket Engine force output at beginning (in Percent)
		Property.set(class1, "dragCoefficient", 0.4F); // Aerodynamic Drag Coefficient
		Property.set(class1, "powerType", 0); // Type of Explosion. 0="Splash", 1="Splinters", 2="Napalm"
		Property.set(class1, "power", 0.70F); // RL Data: 3.5kg HE warhead, for realism reduced to 1/5th of it's RL weight
		Property.set(class1, "radius", 5.0F); // Radius of Explosion Effect
        Property.set(class1, "proximityFuzeRadius", 0F); // Exploding decition Radius of Proximity Fuze
                                                         // AIM-4s don't have Proximity Fuze. They need Direct Hit.
		Property.set(class1, "kalibr", 0.163F); // Diameter of Missile Body
		Property.set(class1, "massa", 61.0F); // Missile Weight at Launch time
		Property.set(class1, "massaEnd", 44.2F); // Missile Weight when Booster burned out
		Property.set(class1, "stepMode", Missile.STEP_MODE_HOMING); // target tracking mode
		Property.set(class1, "launchType", Missile.LAUNCH_TYPE_QUICK); // launch pattern
		Property.set(class1, "detectorType", Missile.DETECTOR_TYPE_INFRARED); // detector type
		Property.set(class1, "sunRayAngle", 20.0F); // max. Angle at which the missile will track Sun Ray, zero disables Sun Ray tracking (only valid for IR detector missiles)
		Property.set(class1, "multiTrackingCapable", 1); // set whether or not this type of missile can fight multiple different targets simultaneously. Usually "1" for IR missiles and "0" for others.
		Property.set(class1, "canTrackSubs", 0); // set whether or not submerged objects can be tracked
		Property.set(class1, "minPkForAI", 25.0F); // min. Kill Probability for AI to launch a missile
		Property.set(class1, "timeForNextLaunchAI", 10000L); // time between two Missile launches for AI
		Property.set(class1, "engineDelayTime", -100L); // Rocket Engine Start Delay Time (in milliseconds), negative Value means that Engine is started before Missile leaves it's rail
		Property.set(class1, "attackDecisionByAI", 1); // let AI decide whether or not to attack. Usually "1" for short range missiles and "0" for all others.
		Property.set(class1, "targetType", Missile.TARGET_AIR); // Type of valid targets, can be any combination of TARGET_AIR, TARGET_GROUND and TARGET_SHIP.
		Property.set(class1, "shotFreq", 0.5F); // Minimum time (in seconds) between two missile launches

		// Factor for missile tracking random ground targets issue.
		// Calculation: Angle to ground (in degrees) divided by:
		// (altitude in meters divided by 1000)^2
		Property.set(class1, "groundTrackFactor", 10.0F); // lower value means higher probability of ground target tracking
		Property.set(class1, "flareLockTime", 1000L); // time (in milliseconds) for missile locking on to different target, i.e. flare (sun, ground clutter etc.)
		Property.set(class1, "trackDelay", 1200L); // time (in milliseconds) for missile tracking target after launch (i.e. the time for the missile to fly straight first to be clear of the launching A/C)
		Property.set(class1, "failureRate", 33.0F); // Failure rate in percent
		Property.set(class1, "maxLockGForce", 3.5F); // max. G-Force for lockon
		Property.set(class1, "maxFOVfrom", 24.0F); // max Angle offset from launch A/C POV
		Property.set(class1, "maxFOVto", 55.0F); // max Angle offset from target aft POV
		Property.set(class1, "PkMaxFOVfrom", 30.0F); // max Angle offset from launch A/C POV for Pk calculation
		Property.set(class1, "PkMaxFOVto", 66.0F); // max Angle offset from target aft POV for Pk calculation
		Property.set(class1, "PkDistMin", 450.0F); // min Distance for Pk calculation
		Property.set(class1, "PkDistOpt", 2200.0F); // optimum Distance for Pk calculation
		Property.set(class1, "PkDistMax", 6000.0F); // max Distance for Pk calculation
		Property.set(class1, "leadPercent", 100.0F); // Track calculation lead value
		Property.set(class1, "maxGForce", 12.0F); // max turning rate G-Force
		Property.set(class1, "stepsForFullTurn", 15); // No. of ticks (1 tick = 30ms) for full control surface turn, higher value means slower reaction and smoother flight, lower value means higher agility
		Property.set(class1, "fxLock", "weapon.AIM9.lock"); // prs file for Lock Tone
		Property.set(class1, "fxLockVolume", 1.0F); // Volume for Lock Tone, "1" is default
		Property.set(class1, "fxNoLock", "weapon.AIM9.nolock"); // prs file for No Lock Tone
		Property.set(class1, "fxNoLockVolume", 1.0F); // Volume for No Lock Tone, "1" is default
		Property.set(class1, "smplLock", "AIM9_lock.wav"); // wav file for Lock Tone
		Property.set(class1, "smplNoLock", "AIM9_no_lock.wav"); // wav file for No Lock Tone
		Property.set(class1, "friendlyName", "AIM-4D"); // Display Name of this missile
		Spawn.add(class1, new SPAWN());
	}

	public MissileAIM4D_gn16() {
	}

	public MissileAIM4D_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		this.MissileInit(actor, netchannel, i, point3d, orient, f);
	}
}
