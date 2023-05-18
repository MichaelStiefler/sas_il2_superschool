/*4.10.1 class*/
package com.maddox.il2.objects.trains;

import java.util.ArrayList;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.RoadPart;
import com.maddox.il2.ai.ground.RoadPath;
import com.maddox.il2.ai.ground.RoadSegment;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.game.ZutiSupportMethods_ResourcesManagement;
import com.maddox.rts.Message;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class Train extends Chief
{
	public static final float TRAIN_SPEED = 11.111112F;
	public static final double TRAIN_SPEEDUPDIST = 350.0;
	private ArrayList wagons;
	private float trainLength;
	private RoadPath road;
	private long startDelay;
	private int headSeg;
	private double headAlong;
	private int tailSeg;
	private float curSpeed;
	private double milestoneDist;
	private float requiredSpeed;
	private float maxAcceler;

	class Move extends Interpolate
	{
		public boolean tick()
		{
			if (Time.current() >= startDelay)
				Train.this.moveTrain(Time.tickLenFs());
			return true;
		}
	}

	public static class TrainState
	{
		public int _headSeg;
		public double _headAlong;
		public float _curSpeed;
		public double _milestoneDist;
		public float _requiredSpeed;
		public float _maxAcceler;
	}

	void getStateData(TrainState trainstate)
	{
		trainstate._headSeg = headSeg;
		trainstate._headAlong = headAlong;
		trainstate._curSpeed = curSpeed;
		trainstate._milestoneDist = milestoneDist;
		trainstate._requiredSpeed = requiredSpeed;
		trainstate._maxAcceler = maxAcceler;
	}

	protected final float getEngineSmokeKoef()
	{
		if (requiredSpeed < 11.111112F)
			return curSpeed / 11.111112F;
		return 1.0F;
	}

	protected final boolean stoppedForever()
	{
		return requiredSpeed < 0.0F;
	}

	private static final void ERR(String string)
	{
		String string_4_ = "INTERNAL ERROR IN Train: " + string;
		System.out.println(string_4_);
		throw new ActorException(string_4_);
	}

	private static final void ConstructorFailure()
	{
		System.out.println("Train: Creation error");
		throw new ActorException();
	}

	public Object getSwitchListener(Message message)
	{
		return this;
	}

	public Train(String string, int i, SectFile sectfile, String string_5_, SectFile sectfile_6_, String string_7_)
	{
		try
		{
			//TODO: Added by |ZUTI|
			//----------------------------------------------------------
			zutiTrainName = string_5_.substring(string_5_.indexOf(".")+1, string_5_.length());
			//----------------------------------------------------------
			
			road = new RoadPath(sectfile_6_, string_7_);
			startDelay = road.get(0).waitTime;
			if (startDelay < 0L)
				startDelay = 0L;
			road.RegisterTravellerToBridges(this);
			setName(string);
			setArmy(i);
			headSeg = 0;
			headAlong = 0.0;
			pos = new ActorPosMove(this);
			pos.setAbs(road.get(0).start);
			pos.reset();
			int i_8_ = sectfile.sectionIndex(string_5_);
			int i_9_ = sectfile.vars(i_8_);
			if (i_9_ <= 0)
				throw new ActorException("Train: Missing wagons");
			wagons = new ArrayList();
			for (int i_10_ = 0; i_10_ < i_9_; i_10_++)
			{
				String string_11_ = sectfile.var(i_8_, i_10_);
				Object object = Spawn.get(string_11_);
				if (object == null)
					throw new ActorException("Train: Unknown class of wagon (" + string_11_ + ")");
				Wagon wagon = ((WagonSpawn) object).wagonSpawn(this);
				wagon.setName(string + i_10_);
				wagons.add(wagon);
			}
			recomputeTrainLength();
			curSpeed = 0.0F;
			requiredSpeed = 0.0F;
			placeTrain(true, false);
			recomputeSpeedRequirements(road.get(headSeg).length2Dallprev + headAlong - (double) trainLength);
			if (!interpEnd("move"))
				interpPut(new Move(), "move", Time.current(), null);
		}
		catch (Exception exception)
		{
			System.out.println("Train creation failure:");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
			ConstructorFailure();
		}
	}

	public void BridgeSegmentDestroyed(int i, int i_12_, Actor actor)
	{
		boolean bool = road.MarkDestroyedSegments(i, i_12_);
		if (bool)
		{
			int i_13_;
			for (i_13_ = tailSeg; i_13_ <= headSeg; i_13_++)
			{
				if (road.segIsWrongOrDamaged(i_13_))
					break;
			}
			if (i_13_ <= headSeg)
			{
				for (int i_14_ = 0; i_14_ < wagons.size(); i_14_++)
				{
					Wagon wagon = (Wagon) wagons.get(i_14_);
					wagon.absoluteDeath(actor);
				}
				road.UnregisterTravellerFromBridges(this);
				destroy();
			}
		}
	}

	public Actor GetNearestEnemy(Point3d point3d, double d, int i)
	{
		NearestEnemies.set(i);
		return NearestEnemies.getAFoundEnemy(point3d, d, getArmy());
	}

	private static float solveSquareEq(float f, float f_15_, float f_16_)
	{
		float f_17_ = f_15_ * f_15_ - 4.0F * f * f_16_;
		if (f_17_ < 0.0F)
			return -1.0F;
		f_17_ = (float) Math.sqrt((double) f_17_);
		float f_18_ = (-f_15_ + f_17_) / (2.0F * f);
		float f_19_ = (-f_15_ - f_17_) / (2.0F * f);
		if (f_19_ > f_18_)
			f_18_ = f_19_;
		return f_18_ >= 0.0F ? f_18_ : -1.0F;
	}

	private static float findSideBOfTriangle(float f, float f_20_, float f_21_)
	{
		return solveSquareEq(1.0F, 2.0F * f * f_21_, f * f - f_20_ * f_20_);
	}

	private void recomputeTrainLength()
	{
		trainLength = 0.0F;
		for (int i = 0; i < wagons.size(); i++)
		{
			Wagon wagon = (Wagon) wagons.get(i);
			trainLength += wagon.getLength();
		}
	}

	private void placeTrain(boolean bool, boolean bool_22_)
	{
		if (bool_22_)
		{
			for (int i = 0; i < wagons.size(); i++)
			{
				Wagon wagon = (Wagon) wagons.get(i);
				wagon.place(null, null, false, true);
			}
		}
		else
		{
			if (bool)
			{
				float f = trainLength * 1.02F;
				RoadPart roadpart = new RoadPart();
				if (!road.FindFreeSpace((double) f, headSeg, headAlong, roadpart))
				{
					System.out.println("Train: Not enough room for wagons");
					throw new ActorException();
				}
				headSeg = roadpart.begseg;
				headAlong = roadpart.begt;
			}
			double d = 0.0;
			int i = headSeg;
			double d_24_ = headAlong;
			Point3d point3d = road.get(i).computePos_Fit(d_24_, 0.0, 0.0F);
			for (int i_25_ = 0; i_25_ < wagons.size(); i_25_++)
			{
				Wagon wagon = (Wagon) wagons.get(i_25_);
				float f = wagon.getLength();
				int i_26_;
				if (d_24_ >= (double) f)
				{
					i_26_ = i;
					d = d_24_ - (double) f;
				}
				else
				{
					if (i <= 0)
					{
						System.out.println("Train: No room for wagons (curly station road?)");
						throw new ActorException();
					}
					i_26_ = i - 1;
					d = road.get(i_26_).length2D;
					float f_27_ = road.computeCosToNextSegment(i_26_);
					float f_28_ = findSideBOfTriangle((float) d_24_, f, f_27_);
					if (f_28_ < 0.0F || (double) f_28_ > d)
					{
						System.out.println("Train: internal error in computings (len=" + d + " B=" + f_28_ + ")");
						throw new ActorException();
					}
					d -= (double) f_28_;
					if (d <= 0.0)
						d = 0.0;
				}
				Point3d point3d_29_ = road.get(i_26_).computePos_Fit(d, 0.0, 0.0F);
				wagon.place(point3d, point3d_29_, bool, false);
				i = i_26_;
				d_24_ = d;
				point3d.set(point3d_29_);
				tailSeg = i_26_;
			}
		}
	}

	private void computeSpeedsWhenCrush(float f)
	{
		curSpeed = f * 0.9F;
		milestoneDist = 9.9999E7;
		requiredSpeed = 0.0F;
		maxAcceler = 3.5F;
	}

	private void LocoSound(int i)
	{
		if (wagons != null && wagons.size() > 0)
		{
			Wagon wagon = (Wagon) wagons.get(0);
			if (wagon != null && wagon instanceof LocomotiveVerm)
			{
				switch (i)
				{
					case 0:
						wagon.newSound("models.train", true);
					break;
					case 1:
						wagon.newSound("objects.train_signal", true);
					break;
					default:
						wagon.breakSounds();
				}
			}
		}
	}

	private void recomputeSpeedRequirements(double d)
	{
		double d_35_ = road.get(road.nsegments() - 1).length2Dallprev;
		d_35_ -= (double) trainLength;
		maxAcceler = 1.5F;
		double d_36_;
		if (d_35_ <= 350.0)
		{
			if (d < d_35_ * 0.5)
			{
				LocoSound(0);
				milestoneDist = d_35_ * 0.5;
				d_36_ = milestoneDist - d;
				requiredSpeed = 11.111112F;
			}
			else
			{
				LocoSound(1);
				milestoneDist = 9.99999E7;
				d_36_ = d_35_ - d;
				requiredSpeed = 0.7222222F;
			}
		}
		else if (d < 350.0)
		{
			LocoSound(0);
			milestoneDist = 350.0;
			d_36_ = milestoneDist - d;
			requiredSpeed = 11.111112F;
		}
		else if (d < d_35_ - 350.0)
		{
			milestoneDist = d_35_ - 350.0;
			d_36_ = 175.0;
			requiredSpeed = 11.111112F;
		}
		else
		{
			LocoSound(1);
			milestoneDist = 9.99999E7;
			d_36_ = d_35_ - d;
			requiredSpeed = 0.7222222F;
		}
		if (d_36_ > 0.05)
		{
			float f = (float) ((double) (requiredSpeed * requiredSpeed - curSpeed * curSpeed) / (2.0 * d_36_));
			f = Math.abs(f);
			if (f <= maxAcceler)
				maxAcceler = f;
		}
		if (maxAcceler < 0.01F)
			maxAcceler = 0.01F;
	}

	private boolean cantEnterIntoSegment(int i)
	{
		return i >= road.nsegments() - 1 || !road.segIsPassableBy(i, this);
	}

	private void moveTrain(float f)
	{
		if (requiredSpeed < 0.0F)
		{
			placeTrain(false, true);
			
			//TODO: Added by |ZUTI|: train has stopped
			//----------------------------------------
			if( zutiReportFinalDestination )
			{
				System.out.println(zutiTrainName + " has reached final destination!");
				zutiReportFinalDestination = false;
				
				ZutiSupportMethods_ResourcesManagement.addResourcesFromMovingRRRObjects(zutiTrainName, pos.getAbsPoint(), this.getArmy(), 1.0F, true);
			}
			//----------------------------------------
		}
		else
		{
			RoadSegment roadsegment = road.get(headSeg);
			double d = (roadsegment.length2Dallprev + headAlong - (double) trainLength);
			if (d >= milestoneDist)
				recomputeSpeedRequirements(d);
			float f_37_ = requiredSpeed - curSpeed;
			double d_38_;
			if (f_37_ != 0.0F)
			{
				f_37_ /= f;
				float f_39_;
				if (Math.abs(f_37_) > maxAcceler)
				{
					f_37_ = f_37_ >= 0.0F ? maxAcceler : -maxAcceler;
					f_39_ = curSpeed + f * f_37_;
					if (f_39_ < 0.0F)
						f_39_ = 0.0F;
				}
				else
					f_39_ = requiredSpeed;
				d_38_ = (double) (curSpeed * f + f_37_ * f * f * 0.5F);
				curSpeed = f_39_;
				if (d_38_ <= 0.0)
					d_38_ = 0.0;
			}
			else
			{
				d_38_ = (double) (curSpeed * f);
				if (requiredSpeed == 0.0F)
					requiredSpeed = -1.0F;
			}
			for (/**/; headAlong + d_38_ >= roadsegment.length2D; roadsegment = road.get(headSeg))
			{
				if (cantEnterIntoSegment(headSeg + 1))
				{
					headAlong = roadsegment.length2D;
					d_38_ = 0.0;
					curSpeed = 0.0F;
					requiredSpeed = -1.0F;
					if (headSeg + 1 >= road.nsegments() - 1)
						World.onTaskComplete(this);
					break;
				}
				headAlong = headAlong + d_38_ - roadsegment.length2D;
				headSeg++;
			}
			headAlong += d_38_;
			pos.setAbs(roadsegment.computePos_Fit(headAlong, 0.0, 0.0F));
			placeTrain(false, false);
		}
	}

	void wagonDied(Wagon wagon, Actor actor)
	{
		int i;
		for (i = 0; i < wagons.size(); i++)
		{
			if (wagon == (Wagon) wagons.get(i))
				break;
		}
		if (i >= wagons.size())
			ERR("Unknown wagon");
		if (requiredSpeed >= 0.0F)
		{
			if (i == 0)
			{
				computeSpeedsWhenCrush(curSpeed);
				if (wagon instanceof LocomotiveVerm)
					World.onActorDied(this, actor);
			}
			else
			{
				computeSpeedsWhenCrush(curSpeed);
				if (wagon instanceof LocomotiveVerm)
					World.onActorDied(this, actor);
			}
		}
	}

	boolean isAnybodyDead()
	{
		for (int i = 0; i < wagons.size(); i++)
		{
			Wagon wagon = (Wagon) wagons.get(i);
			if (wagon.IsDeadOrDying())
				return true;
		}
		return false;
	}

	void setStateDataMirror(TrainState trainstate, boolean bool)
	{
		headSeg = trainstate._headSeg;
		headAlong = trainstate._headAlong;
		curSpeed = trainstate._curSpeed;
		milestoneDist = trainstate._milestoneDist;
		requiredSpeed = trainstate._requiredSpeed;
		maxAcceler = trainstate._maxAcceler;
		placeTrain(false, false);
		if (requiredSpeed >= 0.0F && bool)
			computeSpeedsWhenCrush(curSpeed);
	}
	
	//TODO: |ZUTI| variables
	//--------------------------------------------------------------
	private boolean zutiReportFinalDestination = true;
	private String zutiTrainName = null;
	//--------------------------------------------------------------
}
