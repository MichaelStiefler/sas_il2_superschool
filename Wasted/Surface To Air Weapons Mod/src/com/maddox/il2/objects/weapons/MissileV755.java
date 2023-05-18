package com.maddox.il2.objects.weapons;

import java.util.List;

import com.maddox.JGP.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.vehicles.stationary.StationarySAM.SNR_75M;


public class MissileV755 extends MissileSAM
{
	static class SPAWN extends MissileSAM.SPAWN
	{
		SPAWN() {}

		public void doSpawn(Actor paramActor, NetChannel paramNetChannel, int paramInt, Point3d paramPoint3d, Orient paramOrient, float paramFloat, Actor radartarget)
		{
			new MissileV755(paramActor, paramNetChannel, paramInt, paramPoint3d, paramOrient, paramFloat, radartarget);
		}
	}

	public MissileV755(Actor paramActor, NetChannel paramNetChannel, int paramInt, Point3d paramPoint3d, Orient paramOrient, float paramFloat, Actor radartarget)
	{
		MissileSAMInit(paramActor, paramNetChannel, paramInt, paramPoint3d, paramOrient, paramFloat, radartarget);
		radar = paramActor;
	}

	public boolean interpolateStep() {
		if (radar == null || !Actor.isValid(radar) || ((SNR_75M)radar).getRadarContact() == null || !Actor.isValid(((SNR_75M)radar).getRadarContact())) {
			victim = null;
		}
		if (radar != null && Actor.isValid(radar)) {
			double rnd = TrueRandom.nextFloat(0, 100);
			double rndDist = TrueRandom.nextFloat(0, 200);
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if(actor instanceof Aircraft && rnd < 20 && this.pos.getAbsPoint().distance(actor.pos.getAbsPoint()) <= (244D - rndDist) && actor.getArmy() != getArmy())
                {
					this.doExplosionAir();
					this.postDestroy();
					this.collide(false);
					this.drawing(false);
                }
            }

		}
		super.interpolateStep();
		return true;
	}

	public Actor radar;


	public MissileV755() {
		radar = null;
	}

	static 
	{
		Class localClass = com.maddox.il2.objects.weapons.MissileV755.class;
		Property.set(localClass, "mesh", "3do/arms/V-755/mono.sim");
		Property.set(localClass, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
		Property.set(localClass, "flame", "3do/effects/misil/mono.sim");
		Property.set(localClass, "smoke", "3DO/Effects/Tracers/Piropatron/Smokeflare.eff");
		Property.set(localClass, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(localClass, "emitLen", 50F);
		Property.set(localClass, "emitMax", 1.0F);
		Property.set(localClass, "sound", "weapon.rocketSAM");
		Property.set(localClass, "radius", 244F);
		Property.set(localClass, "timeLife", 86F);
		Property.set(localClass, "timeFire", 3F);
		Property.set(localClass, "force", 40000F);
		Property.set(localClass, "timeSustain", 34F);
		Property.set(localClass, "forceSustain", 26500F);
		Property.set(localClass, "forceT1", 0.5F);
		Property.set(localClass, "forceP1", 0.0F);
		Property.set(localClass, "dragCoefficient", 0.3F);
		Property.set(localClass, "power", 134F);
		Property.set(localClass, "powerType", 2);
		Property.set(localClass, "kalibr", 0.7F);
		Property.set(localClass, "massa", 2160F);
		Property.set(localClass, "massaEnd", 616F);

		Property.set(localClass, "stepMode", 0);
		Property.set(localClass, "launchType", 1);
		Property.set(localClass, "detectorType", 3);
		Property.set(localClass, "sunRayAngle", 0.0F);
		Property.set(localClass, "multiTrackingCapable", 1);
		Property.set(localClass, "canTrackSubs", 0);
		Property.set(localClass, "engineDelayTime", -200L);
		Property.set(localClass, "targetType", 1);
		Property.set(localClass, "shotFreq", 0.01F);
		Property.set(localClass, "groundTrackFactor", 0.0F);
		Property.set(localClass, "flareLockTime", 500L);
		Property.set(localClass, "trackDelay", 1000L);
		Property.set(localClass, "failureRate", 40.0F);
		Property.set(localClass, "maxLockGForce", 6.0F);
		Property.set(localClass, "maxFOVfrom", 60.0F);
		Property.set(localClass, "maxFOVto", 360.0F);
		Property.set(localClass, "PkDistMax", 34000.0F);
		Property.set(localClass, "leadPercent", 100.0F);
		Property.set(localClass, "maxGForce", 12F);
		Property.set(localClass, "stepsForFullTurn", 20);
		Property.set(localClass, "friendlyName", "SA-2");
		Spawn.add(localClass, new SPAWN());
	}


}