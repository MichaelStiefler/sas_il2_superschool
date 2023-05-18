/*4.10.1 class*/
package com.maddox.il2.ai.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FMMath;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.sounds.Voice;

public class AutopilotAI extends Autopilotage
{
	public boolean				bWayPoint;
	public boolean				bStabAltitude;
	public boolean				bStabSpeed;
	public boolean				bStabDirection;
	protected double			StabAltitude;
	protected double			StabSpeed;
	protected double			StabDirection;
	protected Pilot				FM;
	protected WayPoint			WWPoint;
	protected Point3d			WPoint	= new Point3d();
	private static Point3d		P		= new Point3d();
	private static Point3d		PlLoc	= new Point3d();
	private static Orientation	O		= new Orientation();
	protected Vector3d			courseV	= new Vector3d();
	protected Vector3d			windV	= new Vector3d();
	private float				Ail;
	private float				Pw;
	private float				Ev;
	private float				SA;
	private Vector3d			Ve		= new Vector3d();

	// TODO: Guided Missiles Update
	private boolean overrideMissileControl = false;
	private Controls theMissileControls = null;

	public AutopilotAI(FlightModel flightmodel)
	{
		FM = (Pilot) flightmodel;
	}

	public boolean getWayPoint()
	{
		return bWayPoint;
	}

	public boolean getStabAltitude()
	{
		return bStabAltitude;
	}

	public boolean getStabSpeed()
	{
		return bStabSpeed;
	}

	public boolean getStabDirection()
	{
		return bStabDirection;
	}

	public void setWayPoint(boolean bool)
	{
		bWayPoint = bool;
		if (bool)
		{
			bStabSpeed = false;
			bStabAltitude = false;
			bStabDirection = false;
			if (WWPoint != null)
			{
				WWPoint.getP(WPoint);
				StabSpeed = (double) WWPoint.Speed;
				StabAltitude = WPoint.z;
			}
			else
			{
				StabAltitude = 1000.0;
				StabSpeed = 80.0;
			}
			StabDirection = (double) O.getAzimut();
		}
	}

	public void setStabAltitude(boolean bool)
	{
		bStabAltitude = bool;
		if (bool)
		{
			bWayPoint = false;
			FM.getLoc(P);
			StabAltitude = P.z;
			if (!bStabSpeed)
				StabSpeed = (double) FM.getSpeed();
			Pw = FM.CT.PowerControl;
		}
	}

	public void setStabAltitude(float f)
	{
		bStabAltitude = true;
		bWayPoint = false;
		FM.getLoc(P);
		StabAltitude = (double) f;
		if (!bStabSpeed)
			StabSpeed = (double) FM.getSpeed();
		Pw = FM.CT.PowerControl;
	}

	public void setStabSpeed(boolean bool)
	{
		bStabSpeed = bool;
		if (bool)
		{
			bWayPoint = false;
			StabSpeed = (double) FM.getSpeed();
		}
	}

	public void setStabSpeed(float f)
	{
		bStabSpeed = true;
		bWayPoint = false;
		StabSpeed = (double) (f / 3.6F);
	}

	public void setStabDirection(boolean bool)
	{
		bStabDirection = bool;
		if (bool)
		{
			bWayPoint = false;
			O.set(FM.Or);
			StabDirection = (double) O.getAzimut();
			Ail = FM.CT.AileronControl;
		}
	}

	public void setStabDirection(float f)
	{
		bStabDirection = true;
		bWayPoint = false;
		StabDirection = (double) ((f + 3600.0F) % 360.0F);
		Ail = FM.CT.AileronControl;
	}

	public void setStabAll(boolean bool)
	{
		bWayPoint = false;
		setStabDirection(bool);
		setStabAltitude(bool);
		setStabSpeed(bool);
		setStabDirection(bool);
	}

	public float getWayPointDistance()
	{
		if (WPoint == null)
			return 1000000.0F;
		way.curr().getP(P);
		P.sub(FM.Loc);
		return (float) Math.sqrt(P.x * P.x + P.y * P.y);
	}

	private void voiceCommand(Point3d point3d, Point3d point3d_0_)
	{
		Ve.sub(point3d_0_, point3d);
		float f = 57.32484F * (float) Math.atan2(Ve.x, Ve.y);
		int i = (int) f;
		i = (i + 180) % 360;
		Voice.speakHeading((Aircraft) FM.actor, i);
		Voice.speakAltitude((Aircraft) FM.actor, (int) point3d.z);
	}

	public void update(float f)
	{
		// TODO: Guided Missiles Update
		if (this.overrideMissileControl) {
			this.theMissileControls.WeaponControl[this.theMissileControls.rocketHookSelected] = true;
		}
		FM.getLoc(PlLoc);
		SA = (float) Math.max(StabAltitude, Engine.land().HQ_Air(PlLoc.x, PlLoc.y) + 5.0);
		do
		{
			if (bWayPoint)
			{
				if (WWPoint != way.auto(PlLoc) || way.isReached(PlLoc))
				{
					WWPoint = way.auto(PlLoc);
					WWPoint.getP(WPoint);
					if (((Aircraft) FM.actor).aircIndex() == 0 && !way.isLanding())
						voiceCommand(WPoint, PlLoc);
					StabSpeed = (double) (WWPoint.Speed - 2.0F * (float) ((Aircraft) FM.actor).aircIndex());
					StabAltitude = WPoint.z;
					if (WWPoint.Action == 3)
					{
						Actor actor = WWPoint.getTarget();
						if (actor != null)
							FM.target_ground = null;
						else if ((Aircraft) FM.actor instanceof TypeBomber && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
						{
							FM.CT.BayDoorControl = 1.0F;
							Pilot pilot = FM;
							while (pilot.Wingman != null)
							{
								pilot = (Pilot) pilot.Wingman;
								pilot.CT.BayDoorControl = 1.0F;
							}
						}
					}
					else
					{
						if ((Aircraft) FM.actor instanceof TypeBomber)
						{
							FM.CT.BayDoorControl = 0.0F;
							Pilot pilot = FM;
							while (pilot.Wingman != null)
							{
								pilot = (Pilot) pilot.Wingman;
								pilot.CT.BayDoorControl = 0.0F;
							}
						}
						Actor actor = WWPoint.getTarget();
						if (actor instanceof Aircraft)
						{
							if (actor.getArmy() == FM.actor.getArmy())
								FM.airClient = (Maneuver) ((Aircraft) actor).FM;
							else
								FM.target = ((Aircraft) actor).FM;
						}
					}
					if (way.isLanding())
					{
						FM.getLoc(P);
						if (way.Cur() > 3 && P.z > WPoint.z + 500.0)
							way.setCur(1);
						if (way.Cur() == 5)
						{
							if (Main.cur().mission != null)
							{
								/* empty */
							}
							//TODO: Altered by |ZUTI|: fixed variables references
							if (!Mission.isDogfight() || !Mission.MDS_VARIABLES().zutiMisc_DisableAIRadioChatter)
								Voice.speakLanding((Aircraft) FM.actor);
						}
						if (way.Cur() == 6 || way.Cur() == 7)
						{
							int i = 0;
							if (Actor.isAlive(way.landingAirport))
								i = (way.landingAirport.landingFeedback(WPoint, (Aircraft) FM.actor));
							if (i == 0)
							{
								if (Main.cur().mission != null)
								{
									/* empty */
								}
								//TODO: Altered by |ZUTI|: fixed variables references
								if (!Mission.isDogfight() || !Mission.MDS_VARIABLES().zutiMisc_DisableAIRadioChatter)
									Voice.speakLandingPermited((Aircraft) FM.actor);
							}
							if (i == 1)
							{
								if (Main.cur().mission != null)
								{
									/* empty */
								}
								//TODO: Altered by |ZUTI|: fixed variables references
								if (!Mission.isDogfight() || !Mission.MDS_VARIABLES().zutiMisc_DisableAIRadioChatter)
									Voice.speakLandingDenied((Aircraft) FM.actor);
								way.first();
								FM.push(2);
								FM.push(2);
								FM.push(2);
								FM.push(2);
								FM.pop();
								if (Main.cur().mission != null)
								{
									/* empty */
								}
								//TODO: Altered by |ZUTI|: fixed variables references
								if (!Mission.isDogfight() || !Mission.MDS_VARIABLES().zutiMisc_DisableAIRadioChatter)
									Voice.speakGoAround((Aircraft) FM.actor);
								FM.CT.FlapsControl = 0.4F;
								FM.CT.GearControl = 0.0F;
								return;
							}
							if (i == 2)
							{
								if (Main.cur().mission != null)
								{
									/* empty */
								}
								//TODO: Altered by |ZUTI|: fixed variables references
								if (!Mission.isDogfight() || !Mission.MDS_VARIABLES().zutiMisc_DisableAIRadioChatter)
									Voice.speakWaveOff((Aircraft) FM.actor);
								if (FM.isReadyToReturn())
								{
									if (Main.cur().mission != null)
									{
										/* empty */
									}
									//TODO: Altered by |ZUTI|: fixed variables references
									if (!Mission.isDogfight() || !Mission.MDS_VARIABLES().zutiMisc_DisableAIRadioChatter)
										Voice.speakGoingIn((Aircraft) FM.actor);
									FM.AS.setCockpitDoor(FM.actor, 1);
									FM.CT.GearControl = 1.0F;
								}
								else
								{
									way.first();
									FM.push(2);
									FM.push(2);
									FM.push(2);
									FM.push(2);
									FM.pop();
									FM.CT.FlapsControl = 0.4F;
									FM.CT.GearControl = 0.0F;
									Aircraft.debugprintln(FM.actor, "Going around!.");
									return;
								}
								return;
							}
							FM.CT.GearControl = 1.0F;
						}
					}
				}
				if (way.isLanding() && way.Cur() < 6 && way.getCurDist() < 800.0)
					way.next();
				if (((way.Cur() == way.size() - 1 && getWayPointDistance() < 2000.0F && way.curr().getTarget() == null && FM.M.fuel < 0.2F * FM.M.maxFuel) || way.curr().Action == 2)
						&& !way.isLanding())
				{
					Airport airport = Airport.makeLandWay(FM);
					if (airport != null)
					{
						WWPoint = null;
						way.first();
						update(f);
						return;
					}
					FM.set_task(3);
					FM.set_maneuver(49);
					FM.setBusy(true);
				}
				if (World.cur().diffCur.Wind_N_Turbulence)
				{
					World.cur();
					if (!World.wind().noWind && FM.Skill > 0)
					{
						World.cur();
						World.wind().getVectorAI(WPoint, windV);
						windV.scale(-1.0);
						if (FM.Skill == 1)
							windV.scale(0.75);
						courseV.set(WPoint.x - PlLoc.x, WPoint.y - PlLoc.y, 0.0);
						courseV.normalize();
						courseV.scale((double) FM.getSpeed());
						courseV.add(windV);
						StabDirection = (double) -(FMMath.RAD2DEG((float) Math.atan2(courseV.y, courseV.x)));
						break;
					}
				}
				StabDirection = (double) -FMMath.RAD2DEG((float) Math.atan2((WPoint.y - PlLoc.y), (WPoint.x - (PlLoc.x))));
			}
		}
		while (false);
		if (bStabSpeed || bWayPoint)
		{
			Pw = 0.3F - 0.04F * (FM.getSpeed() - (float) StabSpeed);
			if (Pw > 1.0F)
				Pw = 1.0F;
			else if (Pw < 0.0F)
				Pw = 0.0F;
		}
		if (bStabAltitude || bWayPoint)
		{
			Ev = FM.CT.ElevatorControl;
			double d = (double) (SA - FM.getAltitude());
			double d_1_ = 0.0;
			double d_2_ = 0.0;
			if (d > -50.0)
			{
				float f_3_ = 5.0F + 2.5E-4F * FM.getAltitude();
				f_3_ += 0.02 * (250.0 - (double) FM.Vmax);
				if (f_3_ > 14.0F)
					f_3_ = 14.0F;
				d_1_ = (double) (Math.min(FM.getAOA() - f_3_, FM.Or.getTangage() - 1.0F) * 1.0F * f + 0.5F * FM.getForwAccel());
			}
			if (d < 50.0)
			{
				float f_4_ = -15.0F + FM.M.mass * 3.3E-4F;
				if (f_4_ < -4.0F)
					f_4_ = -4.0F;
				d_2_ = (double) ((FM.Or.getTangage() - f_4_) * 0.8F * f);
			}
			double d_5_ = 0.01 * (d + 50.0);
			if (d_5_ > 1.0)
				d_5_ = 1.0;
			if (d_5_ < 0.0)
				d_5_ = 0.0;
			Ev -= d_5_ * d_1_ + (1.0 - d_5_) * d_2_;
			Ev += 1.0 * FM.getW().y + 0.5 * FM.getAW().y;
			if (FM.getSpeed() < 1.3F * FM.VminFLAPS)
				Ev -= 0.0040F * f;
			float f_6_ = 9.0F * FM.getSpeed() / FM.VminFLAPS;
			if (FM.VminFLAPS < 28.0F)
				f_6_ = 10.0F;
			if (f_6_ > 25.0F)
				f_6_ = 25.0F;
			float f_7_ = (f_6_ - FM.Or.getTangage()) * 0.1F;
			float f_8_ = -15.0F + FM.M.mass * 3.3E-4F;
			if (f_8_ < -4.0F)
				f_8_ = -4.0F;
			float f_9_ = (f_8_ - FM.Or.getTangage()) * 0.2F;
			if (Ev > f_7_)
				Ev = f_7_;
			if (Ev < f_9_)
				Ev = f_9_;
			FM.CT.ElevatorControl = 0.8F * FM.CT.ElevatorControl + 0.2F * Ev;
		}
		float f_10_ = 0.0F;
		if (bStabDirection || bWayPoint)
		{
			f_10_ = FM.Or.getAzimut();
			float f_11_ = FM.Or.getKren();
			f_10_ -= StabDirection;
			f_10_ = (f_10_ + 3600.0F) % 360.0F;
			f_11_ = (f_11_ + 3600.0F) % 360.0F;
			if (f_10_ > 180.0F)
				f_10_ -= 360.0F;
			if (f_11_ > 180.0F)
				f_11_ -= 360.0F;
			float f_12_ = (((FM.getSpeed() - FM.VminFLAPS) * 3.6F + FM.getVertSpeed() * 40.0F) * 0.25F);
			if (way.isLanding())
				f_12_ = 65.0F;
			if (f_12_ < 15.0F)
				f_12_ = 15.0F;
			else if (f_12_ > 65.0F)
				f_12_ = 65.0F;
			if (f_10_ < -f_12_)
				f_10_ = -f_12_;
			else if (f_10_ > f_12_)
				f_10_ = f_12_;
			Ail = -0.01F * (f_10_ + f_11_ + 3.0F * (float) FM.getW().x + 0.5F * (float) FM.getAW().x);
			if (Ail > 1.0F)
				Ail = 1.0F;
			else if (Ail < -1.0F)
				Ail = -1.0F;
			WPoint.get(Ve);
			Ve.sub(FM.Loc);
			FM.Or.transformInv(Ve);
			if (Math.abs(Ve.y) < 25.0 && Math.abs(Ve.x) < 150.0)
				FM.CT.AileronControl = -0.01F * FM.Or.getKren();
			else
				FM.CT.AileronControl = Ail;
			FM.CT.ElevatorControl += Math.abs(f_11_) * 0.0040F * f;
			FM.CT.RudderControl -= FM.getAOS() * 0.04F * f;
		}
		if (bWayPoint && way.isLanding())
		{
			if (World.Rnd().nextFloat() < 0.01F)
				FM.doDumpBombsPassively();
			if (way.Cur() > 5)
				FM.set_maneuver(25);
			FM.CT.RudderControl -= f_10_ * 0.04F * f;
			landUpdate(f);
		}
	}

	private void landUpdate(float f)
	{
		if (FM.getAltitude() - 10.0F + FM.getVertSpeed() * 5.0F - SA > 0.0F)
		{
			if (FM.Vwld.z > -10.0)
				FM.Vwld.z -= (double) (1.0F * f);
		}
		else if (FM.Vwld.z < 10.0)
			FM.Vwld.z += (double) (1.0F * f);
		if (FM.getAOA() > 11.0F && FM.CT.ElevatorControl > -0.3F)
			FM.CT.ElevatorControl -= 0.3F * f;
	}

	// TODO: Guided Missiles Update
	public void setOverrideMissileControl(Controls theControls, boolean overrideMissile) {
		this.theMissileControls = theControls;
		this.overrideMissileControl = overrideMissile;
	}

}