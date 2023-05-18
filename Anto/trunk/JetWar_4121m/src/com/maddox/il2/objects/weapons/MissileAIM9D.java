// Source File Name: MissileAIM9D.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;
import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.Actor;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Orient;

public class MissileAIM9D extends Missile {

  static class SPAWN extends Missile.SPAWN {

    public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
      new MissileAIM9D(actor, netchannel, i, point3d, orient, f);
    }
  }

  public MissileAIM9D(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
    this.MissileInit(actor, netchannel, i, point3d, orient, f);
  }

  public MissileAIM9D() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.MissileAIM9D.class;
    Property.set(class1, "mesh", "3do/arms/AIM9D/mono.sim");
    Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
    Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
    Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
    Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
    Property.set(class1, "emitLen", 50F);
    Property.set(class1, "emitMax", 0.4F);
    Property.set(class1, "sound", "weapon.rocket_132");
    Property.set(class1, "timeLife", 60F); // Rocket life time in Seconds
    Property.set(class1, "timeFire", 2.8F); // Rocket Engine Burn time in Seconds
    Property.set(class1, "force", 20000F); // Rocket Engine Power (in Newton)
    Property.set(class1, "forceT1", 0.5F); // Time1, i.e. time until Rocket Engine force output maximum reached (in Seconds), 0 disables this feature
    Property.set(class1, "forceP1", 0.0F); // Power1, i.e. Rocket Engine force output at beginning (in Percent)
    Property.set(class1, "forceT2", 0.2F); // Time2, i.e. time before Rocket Engine burn time ends (in Seconds), from this time on Rocket Engine power output decreases, 0 disables this feature
    Property.set(class1, "forceP2", 50.0F); // Power2, i.e. Rocket Engine force output at the end of burn time (in Percent)
    Property.set(class1, "dragCoefficient", 0.3F); // Aerodynamic Drag Coefficient
    Property.set(class1, "powerType", 0);
    Property.set(class1, "power", 1.02F); // RL Data: 10.2kg HE warhead, for realism reduced to 1/10th of it's RL weight
    Property.set(class1, "radius", 12.93F);
    Property.set(class1, "kalibr", 0.2F);
    Property.set(class1, "massa", 88.5F);
    Property.set(class1, "massaEnd", 60F);
    Property.set(class1, "stepMode", Missile.STEP_MODE_HOMING); // target tracking mode
    Property.set(class1, "launchType", Missile.LAUNCH_TYPE_QUICK); // launch pattern
    Property.set(class1, "detectorType", Missile.DETECTOR_TYPE_INFRARED); // detector type
    Property.set(class1, "sunRayAngle", 12.5F); // max. Angle at which the missile will track Sun Ray, zero disables Sun Ray tracking (only valid for IR detector missiles)
    Property.set(class1, "multiTrackingCapable", 1); // set whether or not this type of missile can fight multiple different targets simultaneously. Usually "1" for IR missiles and "0" for others.
    Property.set(class1, "canTrackSubs", 0); // set whether or not submerged objects can be tracked
    Property.set(class1, "minPkForAI", 25.0F); // min. Kill Probability for AI to launch a missile
    Property.set(class1, "timeForNextLaunchAI", 10000L); // time between two Missile launches for AI
    Property.set(class1, "engineDelayTime", -200L); // Rocket Engine Start Delay Time (in milliseconds), negative Value means that Engine is started before Missile leaves it's rail
    Property.set(class1, "attackDecisionByAI", 1); // let AI decide whether or not to attack. Usually "1" for short range missiles and "0" for all others.
    Property.set(class1, "targetType", Missile.TARGET_AIR); // Type of valid targets, can be any combination of TARGET_AIR, TARGET_GROUND and TARGET_SHIP.
    Property.set(class1, "shotFreq", 0.01F); // Minimum time (in seconds) between two missile launches

    // Factor for missile tracking random ground targets issue.
    // Calculation: Angle to ground (in degrees) divided by:
    //              (altitude in meters divided by 1000)^2
    Property.set(class1, "groundTrackFactor", 16.0F); // lower value means higher probability of ground target tracking
    Property.set(class1, "flareLockTime", 1000L); // time (in milliseconds) for missile locking on to different target, i.e. flare (sun, ground clutter etc.)
    Property.set(class1, "trackDelay", 1000L); // time (in milliseconds) for missile tracking target after launch (i.e. the time for the missile to fly straight first to be clear of the launching A/C)
    Property.set(class1, "failureRate", 30.0F); // Failure rate in percent
    Property.set(class1, "maxLockGForce", 6.0F); // max. G-Force for lockon
    Property.set(class1, "maxFOVfrom", 30.0F); // max Angle offset from launch A/C POV
    Property.set(class1, "maxFOVto", 60.0F); // max Angle offset from target aft POV
    Property.set(class1, "PkMaxFOVfrom", 35.0F); // max Angle offset from launch A/C POV for Pk calculation
    Property.set(class1, "PkMaxFOVto", 70.0F); // max Angle offset from target aft POV for Pk calculation
    Property.set(class1, "PkDistMin", 400.0F); // min Distance for Pk calculation
    Property.set(class1, "PkDistOpt", 4500.0F); // optimum Distance for Pk calculation
    Property.set(class1, "PkDistMax", 8000.0F); // max Distance for Pk calculation
    Property.set(class1, "leadPercent", 100.0F); // Track calculation lead value
    Property.set(class1, "maxGForce", 15.0F); // max turning rate G-Force
    Property.set(class1, "stepsForFullTurn", 8); // No. of ticks (1 tick = 30ms) for full control surface turn, higher value means slower reaction and smoother flight, lower value means higher agility
    Property.set(class1, "fxLock", "weapon.AIM9.lock"); // prs file for Lock Tone
    Property.set(class1, "fxNoLock", "weapon.AIM9.nolock"); // prs file for No Lock Tone
    Property.set(class1, "smplLock", "AIM9_lock.wav"); // wav file for Lock Tone
    Property.set(class1, "smplNoLock", "AIM9_no_lock.wav"); // wav file for No Lock Tone
    Property.set(class1, "friendlyName", "AIM-9D"); // Display Name of this missile
    Spawn.add(class1, new SPAWN());
  }
}
