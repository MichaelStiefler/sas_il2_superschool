// Source File Name: MissileK5M.java
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

public class MissileKh66 extends Missile {

  static class SPAWN extends Missile.SPAWN {

    public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
      new MissileKh66(actor, netchannel, i, point3d, orient, f);
    }
  }

  public MissileKh66(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
    this.MissileInit(actor, netchannel, i, point3d, orient, f);
  }

  public MissileKh66() {
  }

  static {
    Class class1 = com.maddox.il2.objects.weapons.MissileKh66.class;
    Property.set(class1, "mesh", "3do/arms/Kh-66/mono.sim");
    Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
    Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
    Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
    Property.set(class1, "exhausts", 2);
    Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
    Property.set(class1, "emitLen", 50F);
    Property.set(class1, "emitMax", 0.4F);
    Property.set(class1, "sound", "weapon.rocket_132");
    Property.set(class1, "timeLife", 60F); // Rocket life time in Seconds
    Property.set(class1, "timeFire", 7F); // Rocket Engine Burn time in Seconds
    Property.set(class1, "force", 20000F); // Rocket Engine Power (in Newton)
    Property.set(class1, "forceT1", 1.0F); // Time1, i.e. time until Rocket Engine force output maximum reached (in Seconds), 0 disables this feature
    Property.set(class1, "forceP1", 0.0F); // Power1, i.e. Rocket Engine force output at beginning (in Percent)
    Property.set(class1, "forceT2", 0.2F); // Time2, i.e. time before Rocket Engine burn time ends (in Seconds), from this time on Rocket Engine power output decreases, 0 disables this feature
    Property.set(class1, "forceP2", 50.0F); // Power2, i.e. Rocket Engine force output at the end of burn time (in Percent)
    Property.set(class1, "dragCoefficient", 0.5F); // Aerodynamic Drag Coefficient
    Property.set(class1, "power", 103.0F); // RL Data: 9.2kg HE warhead, for realism reduced to 1/4th of it's RL weight
    Property.set(class1, "powerType", 0);
    Property.set(class1, "radius", 60F);
    Property.set(class1, "kalibr", 0.275F);
    Property.set(class1, "massa", 278F);
    Property.set(class1, "massaEnd", 255F);
    Property.set(class1, "stepMode", Missile.STEP_MODE_BEAMRIDER); // target tracking mode
    Property.set(class1, "launchType", Missile.LAUNCH_TYPE_DROP); // launch pattern
    Property.set(class1, "detectorType", Missile.DETECTOR_TYPE_RADAR_BEAMRIDING); // detector type
    Property.set(class1, "sunRayAngle", 0.0F); // max. Angle at which the missile will track Sun Ray, zero disables Sun Ray tracking (only valid for IR detector missiles)
    Property.set(class1, "multiTrackingCapable", 0); // set whether or not this type of missile can fight multiple different targets simultaneously. Usually "1" for IR missiles and "0" for others.
    Property.set(class1, "canTrackSubs", 0); // set whether or not submerged objects can be tracked
    Property.set(class1, "minPkForAI", 25.0F); // min. Kill Probability for AI to launch a missile
    Property.set(class1, "timeForNextLaunchAI", 10000L); // time between two Missile launches for AI
    Property.set(class1, "engineDelayTime", 1000L); // Rocket Engine Start Delay Time (in milliseconds), negative Value means that Engine is started before Missile leaves it's rail
    Property.set(class1, "attackDecisionByAI", 0); // let AI decide whether or not to attack. Usually "1" for short range missiles and "0" for all others.
    Property.set(class1, "targetType", Missile.TARGET_GROUND); // Type of valid targets, can be any combination of TARGET_AIR, TARGET_GROUND and TARGET_SHIP.
    Property.set(class1, "shotFreq", 5.0F); // Minimum time (in seconds) between two missile launches

    // Factor for missile tracking random ground targets issue.
    // Calculation: Angle to ground (in degrees) divided by:
    //              (altitude in meters divided by 1000)^2
    Property.set(class1, "groundTrackFactor", 1000.0F); // lower value means higher probability of ground target tracking
    Property.set(class1, "flareLockTime", 1000L); // time (in milliseconds) for missile locking on to different target, i.e. flare (sun, ground clutter etc.)
    Property.set(class1, "trackDelay", 1000L); // time (in milliseconds) for missile tracking target after launch (i.e. the time for the missile to fly straight first to be clear of the launching A/C)
    Property.set(class1, "failureRate", 20.0F); // Failure rate in percent

    Property.set(class1, "maxLockGForce", 99.9F); // max. G-Force for lockon
    Property.set(class1, "maxFOVfrom", 25.0F); // max Angle offset from launch A/C POV
    Property.set(class1, "maxFOVto", 180.0F); // max Angle offset from target aft POV
    Property.set(class1, "PkMaxFOVfrom", 30.0F); // max Angle offset from launch A/C POV for Pk calculation
    Property.set(class1, "PkMaxFOVto", 70.0F); // max Angle offset from target aft POV for Pk calculation
    Property.set(class1, "PkDistMin", 800.0F); // min Distance for Pk calculation
    Property.set(class1, "PkDistOpt", 3500.0F); // optimum Distance for Pk calculation
    Property.set(class1, "PkDistMax", 5000.0F); // max Distance for Pk calculation
    Property.set(class1, "leadPercent", 0.0F); // Track calculation lead value
    Property.set(class1, "maxGForce", 12.0F); // max turning rate G-Force
    Property.set(class1, "stepsForFullTurn", 5.0F); // No. of 30ms ticks for full control surface turn
    Property.set(class1, "fxLock", "weapon.K5.lock"); // prs file for Lock Tone
    Property.set(class1, "fxNoLock", "weapon.K5.nolock"); // prs file for No Lock Tone
    Property.set(class1, "smplLock", "K5_lock.wav"); // wav file for Lock Tone
    Property.set(class1, "smplNoLock", "K5_no_lock.wav"); // wav file for No Lock Tone
    Property.set(class1, "friendlyName", "Kh-66"); // Display Name of this missile
    Spawn.add(class1, new SPAWN());
  }
}
