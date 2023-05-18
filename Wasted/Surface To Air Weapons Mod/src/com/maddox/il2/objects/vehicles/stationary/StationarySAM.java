package com.maddox.il2.objects.vehicles.stationary;

import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.vehicles.artillery.*;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.MissileV755;
import com.maddox.il2.objects.weapons.RocketChaff;
import com.maddox.rts.NetChannel;
import com.maddox.sas1946.il2.util.TrueRandom;

public abstract class StationarySAM
{

	public static class SNR_75M extends StationaryGenericSAM
	implements TgtSAM, TgtFlak, AAA
	{

		public void Target() {
			rndRange = TrueRandom.nextDouble(0D, 8000D);
			rndAlt = TrueRandom.nextDouble(0D, 400D);
			List list = Engine.targets();
			int i = list.size();
			if (lock == null) {
				for(int j = 0; j < i; j++)
				{
					Actor contact = (Actor)list.get(j);
					if((contact instanceof Aircraft) && (contact.pos.getAbsPoint().distance(pos.getAbsPoint()) > 3000D) && (contact.pos.getAbsPoint().distance(pos.getAbsPoint()) < (26000D + rndRange)) && ((contact.pos.getAbsPoint().z - World.land().HQ(pos.getAbsPoint().x, pos.getAbsPoint().y)) > (900D - rndAlt)) && (contact.pos.getAbsPoint().z < 27000D) && (contact.getArmy() != getArmy()))
					{
						if (!Landscape.rayHitHQ(this.pos.getAbsPoint(), contact.pos.getAbsPoint(), tmpp1)) {
							if (((Aircraft)contact).FM.getOverload() < 4){
								lock = contact;
							}
						}
					} 
				}
			}
			if (lock != null) {
				if(((Aircraft)lock).FM.getOverload() > 4 || Landscape.rayHitHQ(this.pos.getAbsPoint(), lock.pos.getAbsPoint(), tmpp1) || !Actor.isValid(lock) || !(lock instanceof Aircraft) || (lock.pos.getAbsPoint().distance(pos.getAbsPoint()) < 3000D) || (lock.pos.getAbsPoint().distance(pos.getAbsPoint()) > 26000D) || ((lock.pos.getAbsPoint().z - World.land().HQ(pos.getAbsPoint().x, pos.getAbsPoint().y)) < (900D - rndAlt)) || (lock.pos.getAbsPoint().z > 27000D))
				{
					lock = null;
				} 
			}
			checkCountermeasure();
		}

		private void checkCountermeasure() {
			if (lock != null) {
				List theCountermeasures = Engine.countermeasures();
				int lockTime = TrueRandom.nextInt(0 + 1000);
				int counterMeasureSize = theCountermeasures.size();
				for (int counterMeasureIndex = 0; counterMeasureIndex < counterMeasureSize; counterMeasureIndex++) {
					Actor flarechaff = (Actor) theCountermeasures.get(counterMeasureIndex);
					double flareDistance = GuidedMissileUtils.distanceBetween(lock, flarechaff);
					if(flarechaff instanceof RocketChaff &&  500 < lockTime && flareDistance < 500D)
					{
						lock = null;
					}
				}
			}
		}

		public Actor getRadarContact()
		{
			return lock;
		}

		public SNR_75M()
		{
			lock = null;
		}

		private static Point3d tmpp1 = new Point3d();
		public double rndRange;
		public double rndAlt;
		public Actor lock;	
	}

	public static class S_75M extends StationaryGenericSAM
	implements TgtSAM, TgtFlak, AAA
	{

		public void Target() {
			List list = Engine.targets();
			int i = list.size();
			for(int j = 0; j < i; j++)
			{
				Actor radar = (Actor)list.get(j);
				if((radar instanceof SNR_75M) && radar.pos.getAbsPoint().distance(pos.getAbsPoint()) < 2000D && radar.getArmy() == getArmy())
				{
					target = ((SNR_75M) radar).getRadarContact();
					if (!launched && target != null && Actor.isValid(target)) {
						//						HUD.log("victim=" + ((SNR_75M) radar).getRadarContact().hashCode() + " " + ((SNR_75M) radar).getRadarContact().getClass().getName());
						Point3d localPoint3d = new Point3d();
						Orient localOrient = new Orient();
						Vector3d localVector3d = new Vector3d();
						this.pos.getAbs(localPoint3d, localOrient);
						localVector3d.sub(target.pos.getAbsPoint(), this.pos.getAbsPoint());
						this.pos.getAbsOrient().transformInv(localVector3d);
						float f = 57.32484F * (float)Math.atan2(localVector3d.y, -localVector3d.x);
						int k = (int)f + 180;
						if (k > 360)
							k -= 360;
						if (((k > 300) && (k < 360)) || (k < 60))
						{
							int rnd = TrueRandom.nextInt(0, 100);
							if (rnd < 50) {
								localOrient.setYPR(localOrient.getYaw(), localOrient.getPitch() + 45.0F, localOrient.getRoll());
								localPoint3d.z += 5.0D;
								netchannel = null;
								super.setMesh("3do/SAM/S-75/hier2.him");
								this.launched = true;
								MissileV755 missile = new MissileV755(radar, netchannel, 1, localPoint3d, localOrient, 100, target);
							}
						}

					}
				} 
			}
		}

		public Actor getMissileTarget()
		{
			return target;
		}

		public S_75M()
		{
			this.target = null;	
			this.launched = false;
		}

		private boolean launched;
		public double rndRange;
		public double rndAlt;
		public Actor target;
		public NetChannel netchannel;
	}


	public StationarySAM()
	{
	}

	static 
	{
		new StationaryGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.StationarySAM.SNR_75M.class);
		new StationaryGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.StationarySAM.S_75M.class);
	}
}