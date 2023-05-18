/*Modified AutopilotAI class for the SAS Engine Mod*/
/*By western, add AI landing process flexible gear / hook / flap controls on 05th-15th/Jul./2018*/
/*By western, rework elevator control for very heavy planes on 18th-23rd/Jul./2018*/
/*By western, rework reach waypoint decision on 07th/Sep./2018*/
/*By SAS~Storebror, Avoid Heavy Planes pulling up above reference altitude on 19th/Dec./2019*/
package com.maddox.il2.ai.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
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
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Property;

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
//	private float oldEvdecrease = 0F;
	private float newEv = 0F;
	private int iLandingFailCountAlt = -1;
	private int iLandingFailCountSpd = -1;

//	private boolean bLogDetail = true;

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
		if (!flag) return;
		bStabSpeed = false;
		bStabAltitude = false;
		bStabDirection = false;
		if (WWPoint != null) {
			WWPoint.getP(WPoint);
			StabSpeed = WWPoint.Speed;
			StabAltitude = WPoint.z;
		} else {
			StabAltitude = 1000D;
			StabSpeed = (((Aircraft) FM.actor) instanceof TypeFastJet) ? 120D : 80D;
		}
		StabDirection = O.getAzimut();
	}

	public void setStabAltitude(boolean flag) {
		bStabAltitude = flag;
		if (!flag) return;
		bWayPoint = false;
		FM.getLoc(P);
		StabAltitude = P.z;
		if (!bStabSpeed) StabSpeed = FM.getSpeed();
		Pw = FM.CT.PowerControl;
	}

	public void setStabAltitude(float f) {
		bStabAltitude = true;
		bWayPoint = false;
		FM.getLoc(P);
		StabAltitude = f;
		if (!bStabSpeed) StabSpeed = FM.getSpeed();
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
			this.theMissileControls.WeaponControl[this.theMissileControls.rocketHookSelected] = true;
		}
		FM.getLoc(PlLoc);
		boolean bEV = false;
		boolean bFJ = ((Aircraft) FM.actor) instanceof TypeFastJet;
		float tempWPdistance = Math.min(getWayPointDistance(), 18000F) * 1.1F;
		if (way.isLanding()) tempWPdistance = getWayPointDistance() * 1.1F;
		if (iLandingFailCountAlt > 0 && !way.isLanding()) iLandingFailCountAlt = -1;
		if (iLandingFailCountSpd > 0 && !way.isLanding()) iLandingFailCountSpd = -1;
		SA = (float) Math.max(StabAltitude, Engine.land().HQ_Air(PlLoc.x, PlLoc.y) + 5D);
		if (((Maneuver) (FM)).Group != null && ((Maneuver) (FM)).Group.getAaaNum() > 3F && ((Aircraft) FM.actor).aircIndex() == 0 && FM.isTick(165, 0)) avoidance = (float) (1 - World.Rnd().nextInt(0, 1) * 2) * World.Rnd().nextFloat(15F, 30F);
		if (this.bWayPoint) {
			boolean bReached = false;
			if (bFJ) {
				bReached = (WWPoint != way.autoFastJet(PlLoc, FM.getSpeed()));
				if (!bReached) bReached = way.isReachedFastJet(PlLoc, FM.getSpeed());
			} else {
				bReached = (WWPoint != way.auto(PlLoc));
				if (!bReached) bReached = way.isReached(PlLoc);
			}
			if (bReached) {
				WWPoint = bFJ ? way.autoFastJet(PlLoc, FM.getSpeed()) : way.auto(PlLoc);
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
					if (actor != null) FM.target_ground = null;
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
					if (actor1 instanceof Aircraft) if (actor1.getArmy() == FM.actor.getArmy()) FM.airClient = ((Aircraft) actor1).FM;
					else FM.target = ((Aircraft) actor1).FM;
				}
				if (way.isLanding()) {
					FM.getLoc(P);
					if (bFJ)
						turnOnAINavLights(true);
					if (way.Cur() > 3 && P.z > (WPoint.z + (bFJ ? 600D : 500D))) {
						if (iLandingFailCountAlt < 0) iLandingFailCountAlt = 0;
						iLandingFailCountAlt++;
						if (iLandingFailCountAlt > 5) iLandingFailCountAlt = 5;
						flapsGoaround();
						if (FM.bOnGoingOverHeadApproach && bFJ)
							way.setCur(0);
						else
							way.setCur(1);
					}
					if (way.Cur() > 3 && FM.getSpeed() > way.curr().getV() * 1.8F && bFJ) {
						if (iLandingFailCountSpd < 0) iLandingFailCountSpd = 0;
						iLandingFailCountSpd++;
						if (iLandingFailCountSpd > 5) iLandingFailCountSpd = 5;
						flapsGoaround();
						if (FM.bOnGoingOverHeadApproach)
							way.setCur(0);
						else
							way.setCur(1);
					}
					if (bFJ && FM.bOnGoingOverHeadApproach && FM.AILandingWPGearDown > 0 && FM.CT.bHasGearControl) {
						if (way.Cur() > 8 - FM.AILandingWPGearDown && FM.CT.GearControl != 1.0F)
							FM.CT.GearControl = 1.0F;
					}
					if (bFJ && way.isLandingOnShip() && FM.AILandingWPHookDown > 0 && FM.CT.bHasArrestorControl) {
						if (way.Cur() > 8 - FM.AILandingWPHookDown && FM.CT.arrestorControl != 1.0F)
							FM.AS.setArrestor(FM.actor, 1);
					}
					if (bFJ && !FM.CT.bHasFlapsControlSwitch && FM.bOnGoingOverHeadApproach
						&& FM.AILandingWPFlapFullDown > 0 && FM.CT.bHasFlapsControl) {
						if (way.Cur() > 8 - FM.AILandingWPFlapFullDown) {
							FM.CT.FlapsControl = 1.0F;
							if (FM.CT.bHasBlownFlaps) FM.CT.BlownFlapsControl = 1.0F;
						}
					}
					if (bFJ && !FM.CT.bHasFlapsControlSwitch && FM.bOnGoingOverHeadApproach
						&& FM.AILandingWPFlapHalfDown > 0 && FM.AILandingWPFlapHalfDown > FM.AILandingWPFlapFullDown && FM.CT.bHasFlapsControl) {
						if (way.Cur() > 8 - FM.AILandingWPFlapHalfDown && way.Cur() <= 8 - FM.AILandingWPFlapFullDown) {
							if (FM.CT.FlapStage != null && FM.CT.FlapStageMax != -1.0F) {
								if (FM.CT.FlapStage[FM.CT.nFlapStages - 1] < 0.33F) FM.CT.FlapsControl = 1.0F;
								else FM.CT.FlapsControl = FM.CT.FlapStage[FM.CT.nFlapStages - 1];
							}
							else
								FM.CT.FlapsControl = 0.33F;
							if (FM.CT.bHasBlownFlaps) FM.CT.BlownFlapsControl = 1.0F;
						}
					}
					if (bFJ && !FM.CT.bHasFlapsControlSwitch && (!FM.bOnGoingOverHeadApproach || (FM.AILandingWPFlapHalfDown < 0 && FM.AILandingWPFlapFullDown < 0)) && FM.CT.bHasFlapsControl) {
						if (way.Cur() == 4) {
							if (FM.CT.FlapStage != null && FM.CT.FlapStageMax != -1.0F) {
								if (FM.CT.FlapStage[FM.CT.nFlapStages - 1] < 0.33F) FM.CT.FlapsControl = 1.0F;
								else FM.CT.FlapsControl = FM.CT.FlapStage[FM.CT.nFlapStages - 1];
							}
							else
								FM.CT.FlapsControl = 0.33F;
							if (FM.CT.bHasBlownFlaps) FM.CT.BlownFlapsControl = 1.0F;
						}
						else if (way.Cur() == 5) {
							FM.CT.FlapsControl = 1.0F;
							if (FM.CT.bHasBlownFlaps) FM.CT.BlownFlapsControl = 1.0F;
						}
					}
					if (way.Cur() == 5) {
						if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) Voice.speakLanding((Aircraft) FM.actor);
					}
					if (way.Cur() == 6 || way.Cur() == 7) {
						int i = 0;
						if (Actor.isAlive(way.landingAirport)) i = way.landingAirport.landingFeedback(WPoint, (Aircraft) FM.actor);
						if (i == 0) {
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) Voice.speakLandingPermited((Aircraft) FM.actor);
						}
						if (i == 1) {
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) Voice.speakLandingDenied((Aircraft) FM.actor);
							way.first();
							FM.push(2);
							FM.push(2);
							FM.push(2);
							FM.push(2);
							FM.pop();
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) Voice.speakGoAround((Aircraft) FM.actor);
							flapsGoaround();
							FM.CT.GearControl = 0.0F;
							return;
						}
						if (i == 2) {
							if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) Voice.speakWaveOff((Aircraft) FM.actor);
							if (FM.isReadyToReturn()) {
								if (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter) Voice.speakGoingIn((Aircraft) FM.actor);
								// By western, not to open canopy about modern jets
								if (FM.CT.bHasCockpitDoorControl && !FM.CT.bNoCarrierCanopyOpen) FM.AS.setCockpitDoor(FM.actor, 1);
								FM.CT.GearControl = 1.0F;
								return;
							} else {
								way.first();
								FM.push(2);
								FM.push(2);
								FM.push(2);
								FM.push(2);
								FM.pop();
								flapsGoaround();
								FM.CT.GearControl = 0.0F;
								Aircraft.debugprintln(FM.actor, "Going around!.");
								return;
							}
						}
						FM.CT.GearControl = 1.0F;
					}
				}
			}
			if (way.isLanding() && way.Cur() < 6 && way.getCurDist() < 800D) way.next();
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
					if (FM.Skill == 1) windV.scale(0.75D);
					courseV.set(WPoint.x - PlLoc.x, WPoint.y - PlLoc.y, 0.0D);
					courseV.normalize();
					courseV.scale(FM.getSpeed());
					courseV.add(windV);
					StabDirection = -FMMath.RAD2DEG((float) Math.atan2(courseV.y, courseV.x));
					bStabDirectionSet = true;
				}
			}
			if (!bStabDirectionSet) StabDirection = -FMMath.RAD2DEG((float) Math.atan2(WPoint.y - PlLoc.y, WPoint.x - PlLoc.x));
		}
		if (bStabSpeed || bWayPoint) {
			Pw = 0.3F - 0.04F * (FM.getSpeed() - (float) StabSpeed);
			if (way.isLanding() && iLandingFailCountSpd > 0) Pw -= (float) iLandingFailCountSpd * 0.05F;
			if (way.isLanding() && iLandingFailCountAlt > 0) Pw -= (float) iLandingFailCountAlt * 0.01F;
			if (Pw > 1.0F) Pw = 1.0F;
			else if (Pw < 0.0F) Pw = 0.0F;
		}
		if (bStabAltitude || bWayPoint) {
			Ev = FM.CT.ElevatorControl;
			double d = SA - FM.getAltitude();
			double d1 = 0.0D;
			double d2 = 0.0D;
			float minuspitchlimit = (bFJ ? -5F : -4F);

			// TODO: +++ Added by SAS~Storebror: Avoid Heavy Planes pulling up above reference altitude +++
			// This Modification is based on a new (optional) property for aircraft classes, e.g. from G10N1:
			// Property.set(class1, "AutopilotElevatorAboveReferenceAltitudeFactor", 1.2E-4F);
			// If this property isn't set, the old default value of 0.00033F will be used so there's no side effect for existing aircraft.
			float fAutopilotElevatorAboveReferenceAltitudeFactor = Property.floatValue(this.FM.actor.getClass(), "AutopilotElevatorAboveReferenceAltitudeFactor", 0.00033F);
			// TODO: --- Added by SAS~Storebror: Avoid Heavy Planes pulling up above reference altitude ---

			if (way.isLanding() && iLandingFailCountAlt > 0) minuspitchlimit -= (float) iLandingFailCountAlt;
			if (d > (bFJ ? -250D : -50D)) {
				float f4 = 5F + 0.00025F * FM.getAltitude();
				f4 = f4 + 0.02F * (250F - ((FM.Vmax > 300F) ? 300F : FM.Vmax));
				if (f4 > 14F) f4 = 14F;
				d1 = Math.min(FM.getAOA() - f4, FM.Or.getTangage() - 1.0F) * 1.0F * f + 0.5F * FM.getForwAccel();
			}
			if (d < (bFJ ? 250D : 50D)) {

				// TODO: +++ Added by SAS~Storebror: Avoid Heavy Planes pulling up above reference altitude +++
				// float f5 = -15F + FM.M.mass * Aircraft.cvt(FM.M.mass, 50000F, 300000F, 0.00033F, 0.00005F) * Aircraft.cvt(FM.CT.getFlap(), 0.0F, 1.0F, 1.0F, 0.2F);
				float f5 = -15F + FM.M.mass * Aircraft.cvt(FM.M.mass, 50000F, 300000F, fAutopilotElevatorAboveReferenceAltitudeFactor, 0.00005F) * Aircraft.cvt(FM.CT.getFlap(), 0.0F, 1.0F, 1.0F, 0.2F);
				// TODO: --- Added by SAS~Storebror: Avoid Heavy Planes pulling up above reference altitude ---

				if (bFJ && d < -20D && FM.getVertSpeed() > -10.0F) {
					if (Math.abs(FM.Or.getRoll() - 360F) < 10F) {
						float fmulti = Aircraft.cvt((float)d, -50F, -600F, 0.10F, 1.85F) * Aircraft.cvt(FM.getVertSpeed(), 0.005F, 15.0F, 0.05F, 0.40F) * Aircraft.cvt(FM.getVertSpeed(), -10.0F, 0.005F, 0.0F, 1.0F);
						if (fmulti > 0.75F) fmulti = 0.75F;
						if (f5 > -8.4F) f5 = -8.4F * fmulti + f5 * (1.0F - fmulti);  // considered as mass = 20 ton
					}
					else if (Math.abs(FM.Or.getRoll() - 360F) < 60F) {
						float fmulti = Aircraft.cvt((float)d, -50F, -600F, 0.08F, 1.50F) * Aircraft.cvt(FM.getVertSpeed(), 0.005F, 15.0F, 0.049F, 0.39F) * Aircraft.cvt(FM.getVertSpeed(), -10.0F, 0.005F, 0.0F, 1.0F);
						if (fmulti > 0.75F) fmulti = 0.75F;
						if (f5 > -8.4F) f5 = -8.4F * fmulti + f5 * (1.0F - fmulti);  // considered as mass = 20 ton
					}
					f5 += Aircraft.cvt((float) d, -1600F, -200F, -16F, 0F) * Aircraft.cvt(FM.getVertSpeed(), -10.0F, -5.0F, 0.0F, 1.0F);
					if (d < -200F && FM.getVertSpeed() > -0.5F && f5 > 0.0F) {
						f5 = Aircraft.cvt((float) d, -1800F, -200F, -6.0F, 0.5F) + Aircraft.cvt(FM.getVertSpeed(), -10.0F, -0.5F, 0.5F, -4.0F);
					}
					else if (f5 > 0.0F) {
						f5 *= Aircraft.cvt((float) d, -200F, -20F, 0.0F, 1.0F);
					}
				}
				if (f5 < minuspitchlimit) f5 = minuspitchlimit;
				d2 = (double) ((FM.Or.getTangage() - f5) * 0.8F * f);
			}
			/* if (d <= -50D || d >= 50D || !bFJ) */ bEV = true;
			double d3 = 0.01D * (d + 50D);
			if (d3 > 1.0D) d3 = 1.0D;
			if (d3 < 0.0D) d3 = 0.0D;
			if (bEV) {
				Ev -= d3 * d1 + (1.0D - d3) * d2;
				Ev += 1.0D * FM.getW().y + 0.5D * FM.getAW().y;
				if (FM.getSpeed() < 1.3F * FM.VminFLAPS) Ev -= 0.004F * f;
			}
			if (d > -200D && FM.getSpeed() > 30F && bFJ) {
				if (FM.getVertSpeed() < -0.0F) {
					if (tempWPdistance / FM.getSpeed() * FM.getVertSpeed() < (float) d - Math.min((SA * 0.05F), 50F)) {
						newEv = (((float) d / (tempWPdistance / FM.getSpeed())) - FM.getVertSpeed()) * 0.05F;
						if (way.isLanding() || FM.Alt < 40F) {
							if (newEv > 0.9F) newEv = 0.9F;
						} else {
							if (newEv > 0.6F) newEv = 0.6F;
						}
						if (way.isLanding()) {
							float nelimit = -0.16F;
							if (iLandingFailCountAlt > 0) nelimit -= (float) iLandingFailCountAlt * 0.02F;
							if (newEv < nelimit) newEv = nelimit;
						} else {
							if (newEv < -0.02F) newEv = -0.02F;
						}
					} else {
						if (newEv < 0F) newEv += 0.002F;
						if (newEv > 0F) newEv = 0F;
					}
				} else if (FM.getVertSpeed() > 4.0F) newEv *= 0.99F;
				else if (FM.getVertSpeed() > 2.0F) newEv *= 0.998F;
				if (way.isLanding() && way.Cur() > 1 && FM.bOnGoingOverHeadApproach)
					newEv = newEv + 0.08F + FM.AILandingWP360PitchPlus;
				Ev = newEv;
			}
			float f6 = (9F * FM.getSpeed()) / FM.VminFLAPS;
			if (FM.VminFLAPS < 28F) f6 = 10F;
			if (f6 > 25F) f6 = 25F;
			float f7 = (f6 - FM.Or.getTangage()) * 0.1F;

			// TODO: +++ Added by SAS~Storebror: Avoid Heavy Planes pulling up above reference altitude +++
			// float f8 = -15F + FM.M.mass * Aircraft.cvt(FM.M.mass, 50000F, 300000F, 0.00033F, 0.00005F) * Aircraft.cvt(FM.CT.getFlap(), 0.0F, 1.0F, 1.0F, 0.2F);
			float f8 = -15F + FM.M.mass * Aircraft.cvt(FM.M.mass, 50000F, 300000F, fAutopilotElevatorAboveReferenceAltitudeFactor, 0.00005F) * Aircraft.cvt(FM.CT.getFlap(), 0.0F, 1.0F, 1.0F, 0.2F);
			// TODO: --- Added by SAS~Storebror: Avoid Heavy Planes pulling up above reference altitude ---

			float fpt = 0.0F;
			if (FM.getVertSpeed() > 2.0F && d < -150D) {
				fpt = Aircraft.cvt(FM.getVertSpeed(), 2.0F, 10.0F, 0.5F, -2.0F) + Aircraft.cvt((float) d, -400F, -150F, -3.0F, -0.5F);
				f8 = Math.min(f8, fpt);
			}
			else if (FM.getVertSpeed() > -2.0F && d < -50F) {
				fpt = Aircraft.cvt(FM.getVertSpeed(), -2.0F, 10.0F, 1.0F, -2.5F) + Aircraft.cvt((float) d, -400F, -50F, -3.5F, -0.2F);
				f8 = Math.min(f8, fpt);
			}
			else if (FM.getVertSpeed() > -2.0F) {
				fpt = Aircraft.cvt(FM.getVertSpeed(), -2.0F, 10.0F, 1.0F, -2.5F) + Aircraft.cvt((float) d, -50F, 500F, -0.2F, 7.5F);
				f8 = Math.min(f8, fpt);
			}
			else {
				fpt = Aircraft.cvt(FM.getVertSpeed(), -20.0F, -2.0F, 7.0F, 1.0F) + Aircraft.cvt((float) d, -500F, 500F, -4.0F, 7.5F);
				f8 = Math.min(f8, fpt);
			}
			if (f8 < minuspitchlimit) f8 = minuspitchlimit;
			float f9 = (f8 - FM.Or.getTangage()) * 0.2F;
			if (FM.getVertSpeed() < -3.0F && FM.Alt / -FM.getVertSpeed() < 20F && FM.getAOA() < 15F && !(way.isLanding() && way.Cur() > 2)) f9 = 0.80F;
			else if (FM.getVertSpeed() < 0.0F && FM.Alt / -FM.getVertSpeed() < 40F && FM.getAOA() < 15F && !(way.isLanding() && way.Cur() > 2)) f9 = 0.50F;
			if (Ev > f7) Ev = f7;
			if (Ev < f9) Ev = f9;
			FM.CT.ElevatorControl = 0.8F * FM.CT.ElevatorControl + 0.2F * Ev;
		}
		float f1 = 0.0F;
		if (bStabDirection || bWayPoint) {
			double d = SA - FM.getAltitude();
			f1 = FM.Or.getAzimut();
			float f2 = FM.Or.getKren();
			if (((Maneuver) (FM)).Group.getAaaNum() > 3F && ((Aircraft) FM.actor).aircIndex() == 0) f1 = (float) ((double) f1 - (StabDirection + (double) avoidance));
			else f1 = (float) ((double) f1 - StabDirection);
			f1 = (f1 + 3600F) % 360F;
			f2 = (f2 + 3600F) % 360F;
			if (f1 > 180F) f1 -= 360F;
			if (f2 > 180F) f2 -= 360F;
			float f3 = ((FM.getSpeed() - FM.VminFLAPS) * 3.6F + FM.getVertSpeed() * 40F) * 0.25F;
			if (way.isLanding()) f3 = 65F;
			if (f3 < 15F) f3 = 15F;
			else if (f3 > 65F) f3 = 65F;
			if (f1 < -f3) f1 = -f3;
			else if (f1 > f3) f1 = f3;
			Ail = -0.01F * (f1 + f2 + 3F * (float) FM.getW().x + 0.5F * (float) FM.getAW().x);
			if (Ail > 1.0F) Ail = 1.0F;
			else if (Ail < -1F) Ail = -1F;
			WPoint.get(Ve);
			Ve.sub(FM.Loc);
			FM.Or.transformInv(Ve);
			float rolllimitbyspeedhight = 24F + (FM.getSpeed() / FM.Vmin - 1.2F) + (FM.Alt - 100F) / 10F + (float) FM.Vwld.z * 1.6F;
			boolean bflagOK = false;
			if (rolllimitbyspeedhight > 60F) {
				rolllimitbyspeedhight = 60F;
				bflagOK = true;
			}
			boolean bflagFIXED = false;
			if (Math.abs(FM.Or.getRoll() - 360F) > rolllimitbyspeedhight * 1.3 && !bflagOK) {
				FM.CT.AileronControl = ((FM.Or.getRoll() - 360F) < 0) ? -0.5F : 0.5F;
				bflagFIXED = true;
			} else if (Math.abs(FM.Or.getRoll() - 360F) > rolllimitbyspeedhight && !bflagOK) {
		/*		if (FM.CT.AileronControl * Ail > 0) FM.CT.AileronControl = 0.0F;
				else */ FM.CT.AileronControl = Ail;
				bflagFIXED = true;
			} else if (Math.abs(FM.Or.getRoll() - 360F) > rolllimitbyspeedhight * 0.90 && !bflagOK) {
				float newail;
				if (Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D) newail = -0.01F * FM.Or.getKren();
				else newail = Ail;
				if (FM.CT.AileronControl > 0 && newail > 0) if (newail > FM.CT.AileronControl * 0.33F) {
					FM.CT.AileronControl = newail * 0.33F;
					bflagFIXED = true;
				} else FM.CT.AileronControl = newail;
				else if (FM.CT.AileronControl > 0 && newail < 0) FM.CT.AileronControl = newail;
				else if (FM.CT.AileronControl < 0 && newail < 0) if (newail < FM.CT.AileronControl * 0.33F) {
					FM.CT.AileronControl = newail * 0.33F;
					bflagFIXED = true;
				} else FM.CT.AileronControl = newail;
				else FM.CT.AileronControl = newail;
				if (bflagFIXED == false && FM.Vwld.z < 0) bflagFIXED = true;
			} else if (Math.abs(FM.Or.getRoll() - 360F) > rolllimitbyspeedhight * 0.80 && !bflagOK) {
				float newail;
				if (Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D) newail = -0.01F * FM.Or.getKren();
				else newail = Ail;
				if (FM.CT.AileronControl > 0 && newail > 0) if (newail > FM.CT.AileronControl * 0.5F) {
					FM.CT.AileronControl = newail * 0.5F;
					bflagFIXED = true;
				} else FM.CT.AileronControl = newail;
				else if (FM.CT.AileronControl > 0 && newail < 0) FM.CT.AileronControl = newail;
				else if (FM.CT.AileronControl < 0 && newail < 0) if (newail < FM.CT.AileronControl * 0.5F) {
					FM.CT.AileronControl = newail * 0.5F;
					bflagFIXED = true;
				} else FM.CT.AileronControl = newail;
				else FM.CT.AileronControl = newail;
				if (bflagFIXED == false && FM.Vwld.z < 0) bflagFIXED = true;
			} else if (Math.abs(FM.Or.getRoll() - 360F) > rolllimitbyspeedhight * 0.70 && !bflagOK) {
				float newail;
				if (Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D) newail = -0.01F * FM.Or.getKren();
				else newail = Ail;
				if (FM.CT.AileronControl > 0 && newail > 0) if (newail > FM.CT.AileronControl * 0.75F) {
					FM.CT.AileronControl = newail * 0.75F;
					bflagFIXED = true;
				} else FM.CT.AileronControl = newail;
				else if (FM.CT.AileronControl > 0 && newail < 0) FM.CT.AileronControl = newail;
				if (FM.CT.AileronControl < 0 && newail < 0) if (newail < FM.CT.AileronControl * 0.75F) {
					FM.CT.AileronControl = newail * 0.75F;
					bflagFIXED = true;
				} else FM.CT.AileronControl = newail;
				else FM.CT.AileronControl = newail;
				if (bflagFIXED == false && FM.Vwld.z < 0) bflagFIXED = true;
			} else if (Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D) FM.CT.AileronControl = -0.01F * FM.Or.getKren();
			else FM.CT.AileronControl = Ail;
			float newelev = FM.CT.ElevatorControl + (Math.abs(f2) * 0.004F * f + ((bflagFIXED && d > 10D) ? 0.08F : 0F));
			if (way.isLanding() && way.Cur() > 1 && FM.bOnGoingOverHeadApproach)
				newelev = newelev + 0.08F + FM.AILandingWP360PitchPlus;
			/*
			 * if (FM.getAltitude() < way.curr().z() - 50F)
			 * newelev += 0.20F;
			 * if (FM.getSpeed() > 0 && FM.getVertSpeed() < -0.9F && (((Aircraft)FM.actor) instanceof TypeFastJet))
			 * if (getWayPointDistance() / FM.getSpeed() * FM.getVertSpeed() < (float)d - Math.min((SA * 0.05F), 50F))
			 * newelev -= (getWayPointDistance() / FM.getSpeed() * FM.getVertSpeed() - (float)d) / 200F;
			 */
			if (newelev > 1.0F) newelev = 1.0F;
			if (newelev < -1.0F) newelev = -1.0F;
			if (way.isLanding() && d > (bFJ ? -200D : -50D) && tempWPdistance / FM.getSpeed() * FM.getVertSpeed() < (float) d + 50F) {
				FM.CT.ElevatorControl = newelev;
			}
			float newRd = -FM.getAOS() * 0.04F * f * Aircraft.cvt(FM.Alt, 20F, 50F, 0.0F, 1.0F);
			FM.CT.RudderControl = clamp11(newRd);
		}
		if (bWayPoint && way.isLanding()) {
			if (World.Rnd().nextFloat() < 0.01F) FM.doDumpBombsPassively();
			if (way.Cur() > 5) FM.set_maneuver(25);
			float newRd = -f1 * 0.04F * f * Aircraft.cvt(FM.Alt, 10F, 40F, 0.0F, 1.0F);
			FM.CT.RudderControl = clamp11(newRd);
			landUpdate(f);
		}
	}

	private void landUpdate(float f) {
		if (((FM.getAltitude() - 10F) + FM.getVertSpeed() * 5F) - SA > 0.0F) {
			if (FM.Vwld.z > -10D) FM.Vwld.z -= 1.0F * f;
		} else if (FM.Vwld.z < 10D) FM.Vwld.z += 1.0F * f;
		if (FM.getAOA() > 11F && FM.CT.ElevatorControl > -0.3F) FM.CT.ElevatorControl -= 0.3F * f;

	}

	// TODO:
	public void setOverrideMissileControl(Controls theControls, boolean overrideMissile) {
		this.theMissileControls = theControls;
		this.overrideMissileControl = overrideMissile;
	}

	protected void turnOnAINavLights(boolean flag) {
		boolean bNoNavLightsAI = false;
		if (Config.cur.ini.get("Mods", "NoNavLightsAI", 0) == 1) bNoNavLightsAI = true;
		if (Mission.cur().sectFile().get("Mods", "NoNavLightsAI", 0) == 1) bNoNavLightsAI = true;
		if (flag) {
			if (World.Sun().ToSun.z < -0.22F || bNoNavLightsAI) FM.AS.setNavLightsState(true);
		} else {
			FM.AS.setNavLightsState(false);
		}
	}

	private void flapsGoaround() {
		if (!FM.CT.bHasFlapsControlSwitch) {
			if (FM.getSpeed() > Math.max(FM.Vmin * 1.02F, FM.VminAI * 0.96F))
				FM.CT.FlapsControl = 0.0F;
			else if (FM.getSpeed() < FM.Vmin * 0.9F)
				FM.CT.FlapsControl = 1.0F;
			else if (FM.CT.FlapTakeoffGround > 0F)
				FM.CT.FlapsControl = FM.CT.FlapTakeoffGround;
			else if (FM.CT.FlapStage != null && FM.CT.FlapStageMax != -1.0F) {
				if (FM.CT.FlapStage[FM.CT.nFlapStages - 1] < 0.22F) FM.CT.FlapsControl = 1.0F;
				else FM.CT.FlapsControl = FM.CT.FlapStage[FM.CT.nFlapStages - 1];
			} else {
				FM.CT.FlapsControl = 0.33F;
			}
		}
	}

	private float clamp11(float f) {
		if (f < -1.0F) f = -1.0F;
		else if (f > 1.0F) f = 1.0F;
		return f;
	}
}