package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class MissileAS34_gn16 extends Missile implements MissileInterceptable {
	static class SPAWN extends Missile.SPAWN {

		SPAWN() {
		}

		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
			new MissileAS34_gn16(actor, netchannel, i, point3d, orient, f);
		}
	}

	static {
		Class class1 = MissileAS34_gn16.class;
		Property.set(class1, "mesh", "3do/arms/AS34_gn16/mono.sim");
        Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "EFFECTS/Smokes/SmokeMissilessmall.eff");
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.05F));
		Property.set(class1, "emitLen", 50F);
		Property.set(class1, "emitMax", 0.4F);
		Property.set(class1, "sound", "weapon.rocket_132");
		Property.set(class1, "timeLife", 600F);
		Property.set(class1, "timeFire", 0.7F);
		Property.set(class1, "force", 80000F);
		Property.set(class1, "timeSustain", 550F);
		Property.set(class1, "forceSustain", 19230F);
		Property.set(class1, "forceT1", 0.5F);
		Property.set(class1, "forceP1", 0.0F);
		Property.set(class1, "forceT2", 1.0F);
		Property.set(class1, "forceP2", 100F);
		Property.set(class1, "dragCoefficient", 0.3F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "power", 165F);
		Property.set(class1, "radius", 100F);
		Property.set(class1, "kalibr", 0.4F);
		Property.set(class1, "massa", 600F);
		Property.set(class1, "massaEnd", 400F);
		Property.set(class1, "stepMode", 0);
		Property.set(class1, "launchType", 2);
		Property.set(class1, "detectorType", 4);
		Property.set(class1, "sunRayAngle", 0.0F);
		Property.set(class1, "multiTrackingCapable", 1);
		Property.set(class1, "canTrackSubs", 0);
		Property.set(class1, "minPkForAI", 25F);
		Property.set(class1, "timeForNextLaunchAI", 5000L);
		Property.set(class1, "engineDelayTime", 150L);
		Property.set(class1, "attackDecisionByAI", 2);
		Property.set(class1, "targetType", Missile.TARGET_SHIP);
		Property.set(class1, "shotFreq", 0.01F);
		Property.set(class1, "groundTrackFactor", 3.402823E+038F);
		Property.set(class1, "flareLockTime", 1300L);
		Property.set(class1, "trackDelay", 1500L);
		Property.set(class1, "failureRate", 10F);
		Property.set(class1, "maxLockGForce", 6F);
		Property.set(class1, "maxFOVfrom", 20F);
		Property.set(class1, "maxFOVto", 360F);
		Property.set(class1, "PkMaxFOVfrom", 25F);
		Property.set(class1, "PkMaxFOVto", 80F);
		Property.set(class1, "PkDistMin", 2500F);
		Property.set(class1, "PkDistOpt", 15000F);
		Property.set(class1, "PkDistMax", 25000F);
		Property.set(class1, "leadPercent", 10F);
		Property.set(class1, "maxGForce", 6F);
		Property.set(class1, "stepsForFullTurn", 10);
        Property.set(class1, "fxLock", "weapon.AIM7.lock");
        Property.set(class1, "fxLockVolume", 1.0F);
        Property.set(class1, "fxNoLock", (String)null);
        Property.set(class1, "fxNoLockVolume", 2.0F);
        Property.set(class1, "smplLock", "AIM7_lock.wav");
        Property.set(class1, "smplNoLock", (String)null);
		Property.set(class1, "friendlyName", "AS.34");
		Property.set(class1, "iconFar_shortClassName", "AS.34 Missile");
		Spawn.add(class1, new SPAWN());
	}

	public MissileAS34_gn16() {
	}

	public MissileAS34_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		this.MissileInit(actor, netchannel, i, point3d, orient, f);
	}
}
