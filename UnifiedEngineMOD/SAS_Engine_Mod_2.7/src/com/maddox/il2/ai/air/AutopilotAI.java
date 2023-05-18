/*Modified AutopilotAI class for the SAS Engine Mod*/
package com.maddox.il2.ai.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.il2.objects.sounds.Voice;

public class AutopilotAI extends Autopilotage {
	public boolean bWayPoint;
	public boolean bStabAltitude;
	public boolean bStabSpeed;
	public boolean bStabDirection;
	protected double StabAltitude;
	protected double StabSpeed;
	protected double StabDirection;
	protected Pilot FM;
	protected WayPoint WWPoint;
	protected Point3d WPoint;
	private static Point3d P = new Point3d();
	private static Point3d PlLoc = new Point3d();
	private static Orientation O = new Orientation();
	protected Vector3d courseV;
	protected Vector3d windV;
	private float Ail;
	private float Pw;
	private float Ev;
	private float SA;
	private float avoidance;
	private Vector3d Ve;
	// TODO:
	private boolean overrideMissileControl = false;
	private Controls theMissileControls = null;
	private float oldEvdecrease = 0F;
	private float newEv = 0F;

	private boolean bLogDetail = false;

	public AutopilotAI(FlightModel flightmodel) {
		WPoint = new Point3d();
		courseV = new Vector3d();
		windV = new Vector3d();
		Ve = new Vector3d();
		FM = (Pilot) flightmodel;
	}

	public boolean getWayPoint() {
		return bWayPoint;
	}

	public boolean getStabAltitude() {
		return bStabAltitude;
	}

	public boolean getStabSpeed() {
		return bStabSpeed;
	}

	public boolean getStabDirection() {
		return bStabDirection;
	}

	public void setWayPoint(boolean flag) {
		bWayPoint = flag;
		if (!flag)
			return;
		bStabSpeed = false;
		bStabAltitude = false;
		bStabDirection = false;
		if (WWPoint != null) {
			WWPoint.getP(WPoint);
			StabSpeed = WWPoint.Speed;
			StabAltitude = WPoint.z;
		} else {
			StabAltitude = 1000D;
			StabSpeed = 80D;
		}
		StabDirection = O.getAzimut();
	}

	public void setStabAltitude(boolean flag) {
		bStabAltitude = flag;
		if (!flag)
			return;
		bWayPoint = false;
		FM.getLoc(P);
		StabAltitude = P.z;
		if (!bStabSpeed)
			StabSpeed = FM.getSpeed();
		Pw = FM.CT.PowerControl;
	}

	public void setStabAltitude(float f) {
		bStabAltitude = true;
		bWayPoint = false;
		FM.getLoc(P);
		StabAltitude = f;
		if (!bStabSpeed)
			StabSpeed = FM.getSpeed();
		Pw = FM.CT.PowerControl;
	}

	public void setStabSpeed(boolean flag) {
		bStabSpeed = flag;
		if (!flag) {
			return;
		} else {
			bWayPoint = false;
			StabSpeed = FM.getSpeed();
			return;
		}
	}

	public void setStabSpeed(float f) {
		bStabSpeed = true;
		bWayPoint = false;
		StabSpeed = f / 3.6F;
	}

	public void setStabDirection(boolean flag) {
		bStabDirection = flag;
		if (!flag) {
			return;
		} else {
			bWayPoint = false;
			O.set(FM.Or);
			StabDirection = O.getAzimut();
			Ail = FM.CT.AileronControl;
			return;
		}
	}

	public void setStabDirection(float f) {
		bStabDirection = true;
		bWayPoint = false;
		StabDirection = (f + 3600F) % 360F;
		Ail = FM.CT.AileronControl;
	}

	public void setStabAll(boolean flag) {
		bWayPoint = false;
		setStabDirection(flag);
		setStabAltitude(flag);
		setStabSpeed(flag);
		setStabDirection(flag);
	}

	public float getWayPointDistance() {
		if (WPoint == null) {
			return 1000000F;
		} else {
			way.curr().getP(P);
			P.sub(FM.Loc);
			return (float) Math.sqrt(P.x * P.x + P.y * P.y);
		}
	}

	private void voiceCommand(Point3d point3d, Point3d point3d1) {
		Ve.sub(point3d1, point3d);
		float f = 57.32484F * (float) Math.atan2(Ve.x, Ve.y);
		int i = (int) f;
		i = (i + 180) % 360;
		Voice.speakHeading((Aircraft) FM.actor, i);
		Voice.speakAltitude((Aircraft) FM.actor, (int) point3d.z);
	}

	public void update(float f) {
		// TODO: Code for missile override
		if (this.overrideMissileControl) {
			this.theMissileControls.WeaponControl[2] = true;
		}
		FM.getLoc(PlLoc);
		boolean bEV = false;
		SA = (float) Math.max(StabAltitude, Engine.land().HQ_Air(PlLoc.x, PlLoc.y) + 5D);
		if (((Maneuver) (FM)).Group != null && ((Maneuver) (FM)).Group.getAaaNum() > 3F && ((Aircraft) FM.actor).aircIndex() == 0 && FM.isTick(165, 0))
			avoidance = (float) (1 - World.Rnd().nextInt(0, 1) * 2) * World.Rnd().nextFloat(15F, 30F);
		if (this.bWayPoint) {
            boolean bReached = false;
            if((((Aircraft)FM.actor) instanceof TypeFastJet) && FM.getSpeed() > 155F)
                bReached = way.isReachedFastJet(PlLoc);
            else
                bReached = way.isReached(PlLoc);
			if (WWPoint != way.auto(PlLoc) || bReached) {
				WWPoint = ((((Aircraft)FM.actor) instanceof TypeFastJet) && FM.getSpeed() > 155F)? way.autoFastJet(PlLoc) : way.auto(PlLoc);
				WWPoint.getP(WPoint);
				if (((Aircraft) FM.actor).aircIndex() == 0 && !way.isLanding()) {
					voiceCommand(WPoint, PlLoc);
					FM.formationType = (byte) way.curr().formation;
					if (((Maneuver) (FM)).Group != null) {
						((Maneuver) (FM)).Group.setFormationAndScale(FM.formationType, 1.0F, true);
						((Maneuver) (FM)).Group.formationType = FM.formationType;
					}
				}
				StabSpeed = WWPoint.Speed - 2.0F * (float) ((Aircraft) FM.actor).aircIndex();
				StabAltitude = WPoint.z;
				if (WWPoint.Action == 3) {
					Actor actor = WWPoint.getTarget();
					if (actor != null)
						FM.target_ground = null;
					else if (((Aircraft) FM.actor instanceof TypeBomber) 
							&& ((FM.CT.Weapons[2] != null && FM.CT.Weapons[2][0] != null && FM.CT.Weapons[2][FM.CT.Weapons[2].length - 1].haveBullets())
							|| (FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets()))) {
						FM.CT.BayDoorControl = 1.0F;
						for (Pilot pilot1 = FM; pilot1.Wingman != null;) {
							pilot1 = (Pilot) pilot1.Wingman;
							pilot1.CT.BayDoorControl = 1.0F;
						}

					}
				} else {
					if (((Aircraft) FM.actor instanceof TypeBomber) || FM.CT.bHasBayDoorControl) {
						FM.CT.BayDoorControl = 0.0F;
						for (Pilot pilot = FM; pilot.Wingman != null;) {
							pilot = (Pilot) pilot.Wingman;
							pilot.CT.BayDoorControl = 0.0F;
						}

					}
					Actor actor1 = WWPoint.getTarget();
					if (actor1 instanceof Aircraft)
						if (actor1.getArmy() == FM.actor.getArmy())
							FM.airClient = ((Aircraft) actor1).FM;
						else
							FM.target = ((Aircraft) actor1).FM;
				}
				if (way.isLanding()) {
					FM.getLoc(P);
					if(way.Cur() > 3 && P.z > (WPoint.z +
					   ((((Aircraft)FM.actor) instanceof TypeFastJet)? 600D : 500D)))
                                        {
						if(!FM.CT.bHasFlapsControlSwitch) FM.CT.FlapsControl = 0.0F;
						way.setCur(1);
                                        }
					if (way.Cur() > 3 && FM.getSpeed() > way.curr().getV() * 1.8F && ((Aircraft) FM.actor) instanceof TypeFastJet)
                                        {
						if(!FM.CT.bHasFlapsControlSwitch) FM.CT.FlapsControl = 0.0F;
						way.setCur(1);
                                        }
					if (way.Cur() == 4 && ((Aircraft)FM.actor) instanceof TypeFastJet) {
						if(!FM.CT.bHasFlapsControlSwitch){
							if(FM.CT.FlapStage != null && FM.CT.FlapStageMax != -1.0F)
								FM.CT.FlapsControl = FM.CT.FlapStage[FM.CT.nFlapStages - 1];
							else
								FM.CT.FlapsControl = 0.33F;
						}
					}
					if (way.Cur() == 5) {
						if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter)
							Voice.speakLanding((Aircraft) FM.actor);
					}
					if (way.Cur() == 6 || way.Cur() == 7) {
						int i = 0;
						if (Actor.isAlive(way.landingAirport))
							i = way.landingAirport.landingFeedback(WPoint, (Aircraft) FM.actor);
						if (i == 0) {
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter)
								Voice.speakLandingPermited((Aircraft) FM.actor);
						}
						if (i == 1) {
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter)
								Voice.speakLandingDenied((Aircraft) FM.actor);
							way.first();
							FM.push(2);
							FM.push(2);
							FM.push(2);
							FM.push(2);
							FM.pop();
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter)
								Voice.speakGoAround((Aircraft) FM.actor);
							if(!FM.CT.bHasFlapsControlSwitch){
								if(FM.CT.FlapStage != null && FM.CT.FlapStageMax != -1.0F)
									FM.CT.FlapsControl = FM.CT.FlapStage[FM.CT.nFlapStages - 1];
								else
									FM.CT.FlapsControl = 0.33F;
							}
							FM.CT.GearControl = 0.0F;
							return;
						}
						if (i == 2) {
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter)
								Voice.speakWaveOff((Aircraft) FM.actor);
							if (FM.isReadyToReturn()) {
								if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter)
									Voice.speakGoingIn((Aircraft) FM.actor);
								FM.AS.setCockpitDoor(FM.actor, 1);
								FM.CT.GearControl = 1.0F;
								return;
							} else {
								way.first();
								FM.push(2);
								FM.push(2);
								FM.push(2);
								FM.push(2);
								FM.pop();
								if(!FM.CT.bHasFlapsControlSwitch){
									if(FM.CT.FlapStage != null && FM.CT.FlapStageMax != -1.0F)
										FM.CT.FlapsControl = FM.CT.FlapStage[FM.CT.nFlapStages - 1];
									else
										FM.CT.FlapsControl = 0.33F;
								}
								FM.CT.GearControl = 0.0F;
								Aircraft.debugprintln(FM.actor, "Going around!.");
								return;
							}
						}
						FM.CT.GearControl = 1.0F;
					}
				}
			}
			if (way.isLanding() && way.Cur() < 6 && way.getCurDist() < 800D)
				way.next();
			if ((way.Cur() == way.size() - 1 && getWayPointDistance() < 2000F && way.curr().getTarget() == null && FM.M.fuel < 0.2F * FM.M.maxFuel || way.curr().Action == 2) && !way.isLanding()) {
				Airport airport = Airport.makeLandWay(FM);
				if (airport != null) {
					WWPoint = null;
					way.first();
					update(f);
					return;
				}
				FM.set_task(3);
				FM.set_maneuver(49);
				FM.setBusy(true);
			}
			boolean bStabDirectionSet = false;
			if (World.cur().diffCur.Wind_N_Turbulence) {
				World.cur();
				if (!World.wind().noWind && FM.Skill > 0) {
					World.cur();
					World.wind().getVectorAI(WPoint, windV);
					windV.scale(-1D);
					if (FM.Skill == 1)
						windV.scale(0.75D);
					courseV.set(WPoint.x - PlLoc.x, WPoint.y - PlLoc.y, 0.0D);
					courseV.normalize();
					courseV.scale(FM.getSpeed());
					courseV.add(windV);
					StabDirection = -FMMath.RAD2DEG((float) Math.atan2(courseV.y, courseV.x));
					bStabDirectionSet = true;
				}
			}
			if (!bStabDirectionSet)
				StabDirection = -FMMath.RAD2DEG((float) Math.atan2(WPoint.y - PlLoc.y, WPoint.x - PlLoc.x));
		}
		if (bStabSpeed || bWayPoint) {
			Pw = 0.3F - 0.04F * (FM.getSpeed() - (float) StabSpeed);
			if (Pw > 1.0F)
				Pw = 1.0F;
			else if (Pw < 0.0F)
				Pw = 0.0F;
		}
		if (bStabAltitude || bWayPoint) {
			Ev = FM.CT.ElevatorControl;
			double d = SA - FM.getAltitude();
			double d1 = 0.0D;
			double d2 = 0.0D;
			if (d > -50D) {
				float f4 = 5F + 0.00025F * FM.getAltitude();
				f4 = (float) ((double) f4 + 0.02D * (250D - ((FM.Vmax > 300F)? 300D : ((double) FM.Vmax))));
				if (f4 > 14F)
					f4 = 14F;
				d1 = Math.min(FM.getAOA() - f4, FM.Or.getTangage() - 1.0F) * 1.0F * f + 0.5F * FM.getForwAccel();
			}
			if (d < 50D) {
				float f5 = -15F + FM.M.mass * 0.00033F;
				if (f5 < -4F)
					f5 = -4F;
				d2 = (FM.Or.getTangage() - f5) * 0.8F * f;
			}
            if(d <= -50D || d >= 50D || !(((Aircraft)FM.actor) instanceof TypeFastJet))
        		bEV = true;
			double d3 = 0.01D * (d + 50D);
			if (d3 > 1.0D)
				d3 = 1.0D;
			if (d3 < 0.0D)
				d3 = 0.0D;
			if(bEV)
            {
				Ev -= d3 * d1 + (1.0D - d3) * d2;
				Ev += 1.0D * FM.getW().y + 0.5D * FM.getAW().y;
				if (FM.getSpeed() < 1.3F * FM.VminFLAPS)
					Ev -= 0.004F * f;
            }
			if(d > -50D && FM.getSpeed() > 30F && (((Aircraft)FM.actor) instanceof TypeFastJet))
			{
				if(FM.getVertSpeed() < -0.0F)
				{
					if(getWayPointDistance() / FM.getSpeed() * FM.getVertSpeed() < (float)d - Math.min((SA * 0.05F), 50F))
					{
						newEv = (((float)d / (getWayPointDistance() / FM.getSpeed())) - FM.getVertSpeed()) * 0.05F;
						if(newEv > 0.6F)
							newEv = 0.6F;
						if(newEv < 0.0F)
							newEv = 0.0F;
					}
					else
					{
						if(newEv < 0F)
							newEv += 0.002F;
						if(newEv > 0F)
							newEv = 0F;
					}
				}
				else if(FM.getVertSpeed() > 4.0F)
					newEv *= 0.98F;
				else if(FM.getVertSpeed() > 2.0F)
					newEv *= 0.996F;
				Ev = newEv;
			}
			float f6 = (9F * FM.getSpeed()) / FM.VminFLAPS;
			if (FM.VminFLAPS < 28F)
				f6 = 10F;
			if (f6 > 25F)
				f6 = 25F;
			float f7 = (f6 - FM.Or.getTangage()) * 0.1F;
			float f8 = -15F + FM.M.mass * 0.00033F;
			if (f8 < -4F)
				f8 = -4F;
			float f9 = (f8 - FM.Or.getTangage()) * 0.2F;
			if(FM.getVertSpeed() < 0 && FM.Alt / -FM.getVertSpeed() < 40F && FM.getAOA() < 15F && !(way.isLanding() && way.Cur() > 2))
				f9 = 0.50F;
			if (Ev > f7)
				Ev = f7;
			if (Ev < f9)
				Ev = f9;
			FM.CT.ElevatorControl = 0.8F * FM.CT.ElevatorControl + 0.2F * Ev;
		}
		float f1 = 0.0F;
		if (bStabDirection || bWayPoint) {
			double d = SA - FM.getAltitude();
			f1 = FM.Or.getAzimut();
			float f2 = FM.Or.getKren();
			if (((Maneuver) (FM)).Group.getAaaNum() > 3F && ((Aircraft) FM.actor).aircIndex() == 0)
				f1 = (float) ((double) f1 - (StabDirection + (double) avoidance));
			else
				f1 = (float) ((double) f1 - StabDirection);
			f1 = (f1 + 3600F) % 360F;
			f2 = (f2 + 3600F) % 360F;
			if (f1 > 180F)
				f1 -= 360F;
			if (f2 > 180F)
				f2 -= 360F;
			float f3 = ((FM.getSpeed() - FM.VminFLAPS) * 3.6F + FM.getVertSpeed() * 40F) * 0.25F;
			if (way.isLanding())
				f3 = 65F;
			if (f3 < 15F)
				f3 = 15F;
			else if (f3 > 65F)
				f3 = 65F;
			if (f1 < -f3)
				f1 = -f3;
			else if (f1 > f3)
				f1 = f3;
			Ail = -0.01F * (f1 + f2 + 3F * (float) FM.getW().x + 0.5F * (float) FM.getAW().x);
			if (Ail > 1.0F)
				Ail = 1.0F;
			else if (Ail < -1F)
				Ail = -1F;
			WPoint.get(Ve);
			Ve.sub(FM.Loc);
			FM.Or.transformInv(Ve);
			float rolllimitbyspeedhight = 24F + (FM.getSpeed() / FM.Vmin - 1.2F) + (((float)FM.Loc.z - (float)Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y) - 100F) / 10F) + (float)FM.Vwld.z * 1.6F;
			boolean bflagOK = false;
			if(rolllimitbyspeedhight > 60F)
			{
				rolllimitbyspeedhight = 60F;
				bflagOK = true;
			}
			boolean bflagFIXED = false;
			if(Math.abs(FM.Or.getRoll() - 360F) > rolllimitbyspeedhight * 1.3 && !bflagOK)
			{
				FM.CT.AileronControl = ((FM.Or.getRoll() - 360F) < 0)? -0.5F : 0.5F;
				bflagFIXED = true;
			}
			else if(Math.abs(FM.Or.getRoll() - 360F) > rolllimitbyspeedhight && !bflagOK)
			{
				if(FM.CT.AileronControl * Ail > 0)
					FM.CT.AileronControl = 0.0F;
				else
					FM.CT.AileronControl = Ail;
				bflagFIXED = true;
			}
			else if(Math.abs(FM.Or.getRoll() - 360F) > rolllimitbyspeedhight * 0.90 && !bflagOK)
			{
				float newail;
				if(Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D)
					newail = -0.01F * FM.Or.getKren();
				else
					newail = Ail;
				if(FM.CT.AileronControl > 0 &&  newail > 0)
					if(newail > FM.CT.AileronControl * 0.33F)
					{
						FM.CT.AileronControl = newail * 0.33F;
						bflagFIXED = true;
					}
					else
						FM.CT.AileronControl = newail;
				else if(FM.CT.AileronControl > 0 &&  newail < 0)
					FM.CT.AileronControl = newail;
				else if(FM.CT.AileronControl < 0 &&  newail < 0)
					if(newail < FM.CT.AileronControl * 0.33F)
					{
						FM.CT.AileronControl = newail * 0.33F;
						bflagFIXED = true;
					}
					else
						FM.CT.AileronControl = newail;
				else
					FM.CT.AileronControl = newail;
				if(bflagFIXED = false && FM.Vwld.z < 0)
					bflagFIXED = true;
			}
			else if(Math.abs(FM.Or.getRoll() - 360F) > rolllimitbyspeedhight * 0.80 && !bflagOK)
			{
				float newail;
				if(Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D)
					newail = -0.01F * FM.Or.getKren();
				else
					newail = Ail;
				if(FM.CT.AileronControl > 0 &&  newail > 0)
					if(newail > FM.CT.AileronControl * 0.5F)
					{
						FM.CT.AileronControl = newail * 0.5F;
						bflagFIXED = true;
					}
					else
						FM.CT.AileronControl = newail;
				else if(FM.CT.AileronControl > 0 &&  newail < 0)
					FM.CT.AileronControl = newail;
				else if(FM.CT.AileronControl < 0 &&  newail < 0)
					if(newail < FM.CT.AileronControl * 0.5F)
					{
						FM.CT.AileronControl = newail * 0.5F;
						bflagFIXED = true;
					}
					else
						FM.CT.AileronControl = newail;
				else
					FM.CT.AileronControl = newail;
				if(bflagFIXED = false && FM.Vwld.z < 0)
					bflagFIXED = true;
			}
			else if(Math.abs(FM.Or.getRoll() - 360F) > rolllimitbyspeedhight * 0.70 && !bflagOK)
			{
				float newail;
				if(Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D)
					newail = -0.01F * FM.Or.getKren();
				else
					newail = Ail;
				if(FM.CT.AileronControl > 0 &&  newail > 0)
					if(newail > FM.CT.AileronControl * 0.75F)
					{
						FM.CT.AileronControl = newail * 0.75F;
						bflagFIXED = true;
					}
					else
						FM.CT.AileronControl = newail;
				else if(FM.CT.AileronControl > 0 &&  newail < 0)
					FM.CT.AileronControl = newail;
				if(FM.CT.AileronControl < 0 &&  newail < 0)
					if(newail < FM.CT.AileronControl * 0.75F)
					{
						FM.CT.AileronControl = newail * 0.75F;
						bflagFIXED = true;
					}
					else
						FM.CT.AileronControl = newail;
				else
					FM.CT.AileronControl = newail;
				if(bflagFIXED = false && FM.Vwld.z < 0)
					bflagFIXED = true;
			}
			else if(Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D)
				FM.CT.AileronControl = -0.01F * FM.Or.getKren();
			else
				FM.CT.AileronControl = Ail;
			float newelev = FM.CT.ElevatorControl + (Math.abs(f2) * 0.004F * f + ((bflagFIXED)? 0.08F : 0F));
/*			if(FM.getAltitude() < way.curr().z() - 50F)
				newelev += 0.20F;
			if(FM.getSpeed() > 0 && FM.getVertSpeed() < -0.9F && (((Aircraft)FM.actor) instanceof TypeFastJet))
				if(getWayPointDistance() / FM.getSpeed() * FM.getVertSpeed() < (float)d - Math.min((SA * 0.05F), 50F))
					newelev -= (getWayPointDistance() / FM.getSpeed() * FM.getVertSpeed() - (float)d) / 200F;*/
			if(newelev > 1.0F)
				newelev = 1.0F;
			if(newelev < -1.0F)
				newelev = -1.0F;
			FM.CT.ElevatorControl = newelev;
			FM.CT.RudderControl -= FM.getAOS() * 0.04F * f;
		}
		if (bWayPoint && way.isLanding()) {
			if (World.Rnd().nextFloat() < 0.01F)
				FM.doDumpBombsPassively();
			if (way.Cur() > 5)
				FM.set_maneuver(25);
			FM.CT.RudderControl -= f1 * 0.04F * f;
			landUpdate(f);
		}
	}

	private void landUpdate(float f) {
		if (((FM.getAltitude() - 10F) + FM.getVertSpeed() * 5F) - SA > 0.0F) {
			if (FM.Vwld.z > -10D)
				FM.Vwld.z -= 1.0F * f;
		} else if (FM.Vwld.z < 10D)
			FM.Vwld.z += 1.0F * f;
		if (FM.getAOA() > 11F && FM.CT.ElevatorControl > -0.3F)
			FM.CT.ElevatorControl -= 0.3F * f;
	}

	// TODO:
	public void setOverrideMissileControl(Controls theControls, boolean overrideMissile) {
		this.theMissileControls = theControls;
		this.overrideMissileControl = overrideMissile;
	}
}