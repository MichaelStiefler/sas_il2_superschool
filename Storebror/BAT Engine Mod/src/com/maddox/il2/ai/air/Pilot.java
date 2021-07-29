/*Modified Pilot class for the SAS Engine Mod*/
/*By western, change 5minutes call calcuration for FastJet on 24th/Jul./2018*/
package com.maddox.il2.ai.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.ME_163B1A;
import com.maddox.il2.objects.air.TypeBNZFighter;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.il2.objects.air.TypeGlider;
import com.maddox.il2.objects.air.TypeHasToKG;
import com.maddox.il2.objects.air.TypeJazzPlayer;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.BombGunPara;
import com.maddox.il2.objects.weapons.ParaTorpedoGun;
import com.maddox.il2.objects.weapons.TorpedoGun;
import com.maddox.rts.Time;

public class Pilot extends Maneuver {
	// TODO: Default Parameters
	// -------------------------------------------
	public static final int GTARGET_ALL = 0;
	public static final int GTARGET_TANKS = 1;
	public static final int GTARGET_FLAK = 2;
	public static final int GTARGET_VEHICLES = 3;
	public static final int GTARGET_TRAIN = 4;
	public static final int GTARGET_BRIDGE = 5;
	public static final int GTARGET_SHIPS = 6;
	public static final int ATARGET_FIGHTERS = 7;
	public static final int ATARGET_BOMBERS = 8;
	public static final int ATARGET_ALL = 9;
	public static final int TARGET_FIGHTERS = 7;
	public static final int TARGET_BOMBERS = 8;
	public static final int TARGET_ALL = 9;
	private int airTargetType;
	private int groundTargetType;
	private long dumbOffTime;
	private int oldTask;
	private FlightModel oldTaskObject;
	private Actor oldGTarget;
	private boolean continueManeuver;
	private static final Vector3d MAIN_LOOK = new Vector3d(0.34202013999999997D, 0.0D, 0.93969259999999999D);
	private static Vector3d vecDanger = new Vector3d();
	private static Vector3d onMe = new Vector3d();
	private static Vector3d diffV = new Vector3d();
	private double diffVLength;
	private static Vector3f tmpV = new Vector3f();
	private static Point3d p1 = new Point3d();
	private static Point3d p2 = new Point3d();
	private static Point3f p1f = new Point3f();
	private static Point3f p2f = new Point3f();
	private boolean visible;
	private boolean near;
	private boolean onBack;
	private boolean looks;
	private boolean higher;
	private boolean faster;
	private boolean energed;
	private float dist;
	private float dE;
	public boolean bFromPlayer;
	private Actor act;
	private Actor actg;

	// -------------------------------------------

	public native int[] doFighterDefense(float f, float f1, float f2, float f3, float f4, float f5, int i, int j, int k, boolean flag, boolean flag1, boolean flag2, boolean flag3, boolean flag4, boolean flag5, boolean flag6, boolean flag7);

	public native int doStormovikDefense(float f, float f1, float f2, float f3, int i, int j, int k, boolean flag, int l, int i1);

	public native int doTransportDefense(float f, float f1, float f2, int i, int j, int k, int l, int i1);

	public void targetAll() {
		airTargetType = 9;
	}

	public void targetFighters() {
		airTargetType = 7;
	}

	public void targetBombers() {
		airTargetType = 8;
	}

	public void attackGround(int i) {
		groundTargetType = i;
	}

	public Pilot(String s) {
		super(s);
		airTargetType = 9;
		groundTargetType = 0;
		dumbOffTime = 0L;
		oldTask = 3;
		oldTaskObject = null;
		oldGTarget = null;
		continueManeuver = false;
		diffVLength = 0.0D;
		energed = false;
		dist = 0.0F;
		dE = 0.0F;
		bFromPlayer = false;
	}

	private boolean killed(Actor actor) {
		if (actor == null) return true;
		if (actor instanceof BridgeSegment) actor = actor.getOwner();
		if (Actor.isValid(actor)) return !actor.isAlive();
		else return true;
	}

	public boolean killed(FlightModel flightmodel) {
		if (flightmodel == null) return true;
		if (flightmodel.AS.isAllPilotsDead()) return true;
		if (Actor.isValid(flightmodel.actor)) return killed(flightmodel.actor);
		else return flightmodel.isTakenMortalDamage();
	}

	private boolean detectable(Actor actor) {
		if (!(actor instanceof Aircraft)) return false;
		if (Skill >= 2) {
			return true;
		} else {
			vecDanger.set(((Aircraft) actor).FM.Loc);
			vecDanger.sub(Loc);
			onMe.scale(-1D, vecDanger);
			Or.transformInv(vecDanger);
			return vecDanger.x >= 0.0D;
		}
	}

	public boolean isDumb() {
		return Time.current() < dumbOffTime;
	}

	public void setDumbTime(long l) {
		dumbOffTime = Time.current() + l;
	}

	public void addDumbTime(long l) {
		if (isDumb()) dumbOffTime += l;
		else setDumbTime(l);
	}

	public void super_update(float f) {
		super.update(f);
	}

	public void update(float f) {
		if (actor.net != null && actor.net.isMirror()) {
			((com.maddox.il2.objects.air.NetAircraft.Mirror) actor.net).fmUpdate(f);
			return;
		}
		moveCarrier();
		if (TaxiMode) {
			World.cur().airdrome.update(this, f);
			return;
		}
		if (isTick(8, 0) || get_maneuver() == 0) {
			setPriorities();
			setTaskAndManeuver();
		}
		super.update(f);
	}

	protected void setPriorities() {
		if (killed(danger)) danger = null;
		if (killed(target)) target = null;
		if (killed(airClient)) airClient = null;
		if (killed(target_ground)) target_ground = null;
		setBusy(false);
		if (AS.isAllPilotsDead()) {
			setBusy(true);
			set_maneuver(44);
			if (crew > 1) ((Aircraft) actor).hitDaSilk();
			set_task(0);
			return;
		}
		if (get_maneuver() == 46) {
			setBusy(true);
			dontSwitch = true;
			return;
		}
		float f = 0.0F;
		int i = EI.getNum();
		if (i != 0) {
			for (int j = 0; j < i; j++)
				f += EI.engines[j].getReadyness() / (float) i;

			if (f < 0.7F + World.Rnd().nextFloat() * 0.15F) setReadyToReturn(true);
			if (f < 0.3F + World.Rnd().nextFloat() * 0.2F) setReadyToDie(true);
		}
		if (M.fuel < 0.3F * M.maxFuel) {
			int k = AP.way.Cur();
			AP.way.last();
			float f1 = AP.getWayPointDistance();
			AP.way.setCur(k);
			if (M.maxFuel < 0.001F) M.maxFuel = 0.001F;
			float f2 = (1000F * Range * M.fuel) / M.maxFuel;
			if (f2 < 2.0F * f1 && !(actor instanceof TypeGlider)) setReadyToReturn(true);
		}
		if (M.fuel < 0.01F && !(actor instanceof TypeGlider)) setReadyToDie(true);
		if (isTakenMortalDamage() || !isCapableOfBMP()) {
			setBusy(true);
			((Aircraft) actor).hitDaSilk();
			set_task(0);
			if (Group != null) Group.delAircraft((Aircraft) actor);
		}
		if (isReadyToDie()) {
			AP.way.setCur(1);
			bombsOut = true;
			CT.dropFuelTanks();
			set_task(0);
			if (get_maneuver() != 49 && get_maneuver() != 12 && get_maneuver() != 54) {
				clear_stack();
				set_maneuver(49);
			}
			setBusy(true);
			return;
		}
		if (get_maneuver() == 44 || get_maneuver() == 25 && AP.way.Cur() > 6 || get_maneuver() == 49 || get_maneuver() == 26 || get_maneuver() == 64 || get_maneuver() == 102 || get_maneuver() == 2 || get_maneuver() == 84 || get_maneuver() == 57
				|| get_maneuver() == 60 || get_maneuver() == 61) {
			setBusy(true);
			dontSwitch = true;
			return;
		}
		if (getDangerAggressiveness() > (1.0F - 0.12F * (float) Skill) * World.Rnd().nextFloat(0.95F, 1.01F) && danger != null && ((danger.actor instanceof TypeFighter) || (danger.actor instanceof TypeStormovik)) && ((Maneuver) danger).isOk()) {
			if (courage <= 1 && World.Rnd().nextInt(0, 30000) < 2 - courage) {
				setBusy(true);
				((Aircraft) actor).hitDaSilk();
				set_task(0);
				if (Group != null) Group.delAircraft((Aircraft) actor);
			}
			if (get_task() != 4) {
				set_task(4);
				clear_stack();
				set_maneuver(0);
				if ((actor instanceof TypeStormovik) && Group != null) {
					int l = Group.numInGroup((Aircraft) actor);
					if (((Aircraft) danger.actor).aircNumber() < Group.nOfAirc && !hasBombs() && Group.nOfAirc >= l + 2) {
						Maneuver maneuver = (Maneuver) Group.airc[l + 1].FM;
						Voice.speakCheckYour6((Aircraft) actor, (Aircraft) danger.actor);
						Voice.speakHelpFromAir((Aircraft) maneuver.actor, 1);
						maneuver.airClient = this;
						set_task(6);
						clear_stack();
						maneuver.target = danger;
						set_maneuver(27);
						setBusy(true);
						return;
					}
				}
			}
			Voice.speakClearTail((Aircraft) actor);
			setBusy(true);
			return;
		}
		if (isReadyToReturn() && !AP.way.isLanding()) {
			if (Group != null && Group.grTask != 1) {
				AirGroup airgroup = new AirGroup(Group);
				airgroup.rejoinGroup = null;
				Group.delAircraft((Aircraft) actor);
				airgroup.addAircraft((Aircraft) actor);
				airgroup.w.last();
				airgroup.w.prev();
				airgroup.w.curr().setTimeout(3);
				airgroup.timeOutForTaskSwitch = 10000;
				AP.way.last();
				AP.way.prev();
				AP.way.curr().getP(p1f);
				p1f.z = -10F + (float) Loc.z;
			}
			bombsOut = true;
			CT.dropFuelTanks();
			return;
		}
		if (get_task() == 6) {
			CT.GearControl = 0.0F;
			CT.arrestorControl = 0.0F;
			if (target != null && airClient != null && target == ((Maneuver) airClient).danger) {
				if (actor instanceof TypeStormovik) {
					if (((Maneuver) airClient).getDangerAggressiveness() > 0.0F) {
						setBusy(true);
						return;
					}
					airClient = null;
				}
				setBusy(true);
				return;
			}
			if ((((Aircraft) actor).aircIndex() & 1) == 0 && Group != null) {
				int i1 = Group.numInGroup((Aircraft) actor);
				if (Group.nOfAirc >= i1 + 2 && (Group.airc[i1 + 1].aircIndex() & 1) != 0) {
					Maneuver maneuver1 = (Maneuver) Group.airc[i1 + 1].FM;
					if (maneuver1.airClient == this && maneuver1.getDangerAggressiveness() > 0.5F && maneuver1.danger != null && ((Maneuver) maneuver1.danger).isOk()) {
						Voice.speakCheckYour6((Aircraft) maneuver1.actor, (Aircraft) maneuver1.danger.actor);
						Voice.speakHelpFromAir((Aircraft) actor, 1);
						set_task(6);
						clear_stack();
						target = maneuver1.danger;
						set_maneuver(27);
						setBusy(true);
						return;
					}
				}
			}
			if (target != null && ((Maneuver) target).getDangerAggressiveness() > 0.5F && ((Maneuver) target).danger == this && ((Maneuver) target).isOk()) {
				setBusy(true);
				return;
			}
		}
		if (isDumb()) {
			setBusy(true);
			return;
		}
		if (get_task() == 7 && target_ground != null && get_maneuver() != 0) {
			setBusy(true);
			return;
		}
		if (get_task() == 3 && AP.way.isLanding()) {
			setBusy(true);
			return;
		} else {
			return;
		}
	}

	private void setTaskAndManeuver() {
		if (dontSwitch) {
			dontSwitch = false;
			return;
		}
		if (!isBusy()) {
			if ((wasBusy || get_maneuver() == 0) && Group != null) {
				clear_stack();
				Group.setTaskAndManeuver(Group.numInGroup((Aircraft) actor));
			}
		} else if (get_maneuver() == 0) {
			clear_stack();
			setManeuverByTask();
		}
	}

	public void setManeuverByTask() {
		clear_stack();
		switch (get_task()) {
		default:
			break;

		case 2: // '\002'
			if (Leader != null && Actor.isValid(Leader.actor) && !Leader.isReadyToReturn() && !Leader.isReadyToDie() && Leader.getSpeed() > 35F) set_maneuver(24);
			else set_task(3);
			break;

		case 3: // '\003'
			set_maneuver(21);
			if (AP.way.isLanding()) break;
			wingman(true);
			if (Leader != null) break;
			int i;
			float f;
			if (AP.way.Cur() < AP.way.size() - 1) {
				AP.way.next();
				i = AP.way.curr().Action;
				f = AP.getWayPointDistance();
				AP.way.prev();
			} else {
				i = AP.way.curr().Action;
				f = AP.getWayPointDistance();
			}
			Pilot pilot = (Pilot) ((Aircraft) actor).FM;
			do {
				if (pilot.Wingman == null) break;
				pilot = (Pilot) pilot.Wingman;
				pilot.wingman(true);
				pilot.AP.way.setCur(AP.way.Cur());
				if (!pilot.AP.way.isLanding() && pilot.get_task() == 3) pilot.set_task(2);
			} while (true);
			if (actor instanceof TypeFastJet && getSpeed() > 100F) {
				if (((Aircraft) actor).aircIndex() == 0 && Speak5minutes == 0 && i == 3 && f < getSpeed() * 350F) {
					Voice.speak5minutes((Aircraft) actor);
					Speak5minutes = 1;
				}
			} else {
				if (((Aircraft) actor).aircIndex() == 0 && Speak5minutes == 0 && i == 3 && f < 30000F) {
					Voice.speak5minutes((Aircraft) actor);
					Speak5minutes = 1;
				}
			}
			break;

		case 4: // '\004'
			if (get_maneuver() == 0) set_maneuver(21);
			if (danger == null) {
				set_task(3);
				break;
			}
			if (actor instanceof TypeFighter) {
				bombsOut = true;
				CT.dropFuelTanks();
			}
			dist = (float) Loc.distance(danger.Loc);
			vecDanger.sub(danger.Loc, Loc);
			onMe.scale(-1D, vecDanger);
			tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
			tmpOr.transformInv(vecDanger);
			diffV.sub(danger.Vwld, Vwld);
			tmpOr.transformInv(diffV);
			diffVLength = diffV.length();
			tmpOr.setYPR(danger.Or.getYaw(), 0.0F, 0.0F);
			danger.Or.transformInv(onMe);
			vecDanger.normalize();
			onMe.normalize();
			dE = (Energy - danger.Energy) * 0.1019F;
			energed = danger.Energy > Energy;
			faster = danger.getSpeed() > getSpeed();
			higher = danger.Loc.z > Loc.z;
			near = dist < 300F;
			onBack = vecDanger.x < 0.0D && dist < 2000F;
			visible = vecDanger.dot(MAIN_LOOK) > 0.0D;
			looks = onMe.x > 0.0D;
			vecDanger.normalize();
			if (onBack && near && (danger instanceof TypeFighter) && ((actor instanceof TypeTransport) || Wingman == null || killed(Wingman) || ((Pilot) Wingman).target != danger))
				if (isLeader()) {
					if (((actor instanceof TypeFighter) || (actor instanceof TypeStormovik) && Skill > 1 && AP.way.curr().Action == 0) && Wingman != null && !killed(Wingman) && !((Pilot) Wingman).requestCoverFor(this))
						if (Wingman.Wingman != null && !killed(Wingman.Wingman)) ((Pilot) Wingman.Wingman).requestCoverFor(this);
						else if (Skill >= 2) {
							Aircraft aircraft = War.getNearestFriendlyFighter((Aircraft) actor, 8000F);
							if (aircraft != null && (aircraft.FM instanceof Pilot)) ((Pilot) aircraft.FM).requestCoverFor(this);
						}
				} else if (Skill >= 2) {
					Aircraft aircraft1 = War.getNearestFriendlyFighter((Aircraft) actor, 8000F);
					if ((aircraft1 instanceof TypeFighter) && (aircraft1.FM instanceof Pilot)) ((Pilot) aircraft1.FM).requestCoverFor(this);
				}
			if (actor instanceof TypeFighter) {
				fighterDefence();
				break;
			}
			if (actor instanceof TypeStormovik) stormovikDefence();
			else transportDefence();
			break;

		case 5: // '\005'
			if (airClient != null && !killed(airClient)) {
				followOffset.set(100D, 0.0D, 20D);
				set_maneuver(65);
				break;
			}
			airClient = null;
			if (target != null && !killed(target)) {
				set_task(6);
				set_maneuver(0);
			} else {
				set_task(3);
				set_maneuver(21);
			}
			break;

		case 6: // '\006'
			WeWereInAttack = true;
			if ((actor instanceof TypeFighter) && !bKeepOrdnance) {
				bombsOut = true;
				CT.dropFuelTanks();
			}
			if (target == null || !hasCourseWeaponBullets()) {
				if (AP.way.curr().Action == 3) {
					set_task(7);
					set_maneuver(0);
					break;
				}
				set_task(3);
				if (Leader == null) set_maneuver(21);
				else set_maneuver(24);
				break;
			}
			int j = ((Aircraft) actor).aircIndex();
			if (target.actor instanceof TypeBomber) {
				attackBombers();
				break;
			}
			if (target.actor instanceof TypeStormovik) {
				attackStormoviks();
				break;
			}
			if (j == 0 || j == 2) set_maneuver(27);
			if (j != 1 && j != 3) break;
			if (Leader != null && !killed(Leader)) {
				airClient = Leader;
				set_task(5);
				set_maneuver(0);
			} else {
				set_maneuver(27);
			}
			break;

		case 7: // '\007'
			if (!WeWereInGAttack) WeWereInGAttack = true;
			if (!Actor.isAlive(target_ground)) {
				set_task(2);
				set_maneuver(0);
				break;
			}
			boolean flag = true;
			if (CT.Weapons[0] != null && CT.Weapons[0][0] != null && CT.Weapons[0][0].bulletMassa() > 0.05F && CT.Weapons[0][0].countBullets() > 0) flag = false;
			if (flag && CT.getWeaponMass() < 15F || CT.getWeaponMass() < 1.0F) {
				Voice.speakEndOfAmmo((Aircraft) actor);
				set_task(2);
				set_maneuver(0);
				if (AP.way.curr().Action != 3) {
					AP.way.next();
				}
				target_ground = null;
			}
			if ((target_ground instanceof Prey) && (((Prey) target_ground).HitbyMask() & 1) == 0) {
				float f1 = 0.0F;
				for (int k = 0; k < 4; k++)
					if (CT.Weapons[k] != null && CT.Weapons[k][0] != null && CT.Weapons[k][0].countBullets() != 0 && CT.Weapons[k][0].bulletMassa() > f1) f1 = CT.Weapons[k][0].bulletMassa();

				if (f1 < 0.08F || (target_ground instanceof TgtShip) && f1 < 0.55F) {
					set_task(2);
					set_maneuver(0);
					if (AP.way.curr().Action != 3) {
						AP.way.next();
					}
					target_ground = null;
				}
			}
			if (CT.Weapons[3] != null && CT.Weapons[3][0] != null && CT.Weapons[3][0].countBullets() != 0) {
				if (CT.Weapons[3][0] instanceof ParaTorpedoGun) {
					set_maneuver(43);
					break;
				}
				if (CT.Weapons[3][0] instanceof TorpedoGun) {
					if (target_ground instanceof TgtShip) {
						if ((this instanceof TypeHasToKG) && Skill >= 2) set_maneuver(73);
						else set_maneuver(51);
					} else {
						set_maneuver(43);
					}
					break;
				}
				if (CT.Weapons[3][0] instanceof BombGunPara) {
					AP.way.curr().setTarget(null);
					target_ground = null;
					set_maneuver(21);
					break;
				}
				if (CT.Weapons[3][0].bulletMassa() < 10F) {
					set_maneuver(52);
					break;
				}
				if ((actor instanceof TypeDiveBomber) && Alt > 1200F) set_maneuver(50);
				else set_maneuver(43);
				break;
			}
			if (target_ground instanceof BridgeSegment) {
				set_task(2);
				set_maneuver(0);
				if (AP.way.curr().Action != 3) {
					AP.way.next();
				}
				target_ground = null;
			}
			if ((actor instanceof TypeFighter) || (actor instanceof TypeStormovik)) {
				set_maneuver(43);
				break;
			}
			set_task(2);
			set_maneuver(0);
			if (AP.way.curr().Action != 3) {
				AP.way.next();
			}
			target_ground = null;
			break;

		case 0: // '\0'
			if (isReadyToDie()) set_maneuver(49);
			break;

		case 1: // '\001'
			set_maneuver(45);
			break;
		}
	}

	public boolean requestCoverFor(FlightModel flightmodel) {
		if (actor instanceof TypeTransport) {
			Voice.speakHelpFromAir((Aircraft) actor, 0);
			return false;
		}
		if (danger == null || ((Pilot) danger).target != this || danger.Loc.distance(Loc) > 600D + 200D * (double) Skill || (danger.actor instanceof TypeStormovik) || (danger.actor instanceof TypeBomber)) {
			if (((Pilot) flightmodel).danger == null || killed(((Pilot) flightmodel).danger) || (((Pilot) flightmodel).danger.actor instanceof TypeTransport) || ((Pilot) flightmodel).danger.Loc.distance(flightmodel.Loc) > 3000D) {
				Voice.speakHelpFromAir((Aircraft) actor, 2);
				return true;
			}
			set_task(6);
			set_maneuver(27);
			target = ((Pilot) flightmodel).danger;
			if (World.Rnd().nextBoolean()) Voice.speakCoverProvided((Aircraft) actor, (Aircraft) flightmodel.actor);
			else Voice.speakHelpFromAir((Aircraft) actor, 1);
			return true;
		} else {
			Voice.speakHelpFromAir((Aircraft) actor, 0);
			return false;
		}
	}

	public void setAsDanger(Actor actor) {
		if (get_maneuver() == 44) return;
		if (get_maneuver() == 26) return;
		if (isDumb() && !isReadyToReturn()) return;
		if (actor.getArmy() == this.actor.getArmy()) {
			set_maneuver(8);
			setDumbTime(5000L);
			if (actor instanceof Aircraft) Voice.speakCheckFire((Aircraft) this.actor, (Aircraft) actor);
			return;
		}
		if (!Actor.isValid(this.actor)) {
			if (World.cur().isArcade()) {
				Aircraft.debugprintln(actor, "Jeopardizing invalid actor (one being destroyed)..");
				Explosions.generateComicBulb(actor, "Sucker", 5F);
				if ((actor instanceof TypeFighter) && (((Aircraft) actor).FM instanceof Pilot)) ((Pilot) ((Aircraft) actor).FM).set_maneuver(35);
			}
			Voice.speakNiceKill((Aircraft) actor);
			return;
		}
		switch (Skill) {
		case 0: // '\0'
			if (World.Rnd().nextInt(0, 99) < 98) return;
			if (actor instanceof Aircraft) {
				Vector3d vector3d = new Vector3d();
				vector3d.sub(Loc, ((Aircraft) actor).FM.Loc);
				((Aircraft) actor).FM.Or.transformInv(vector3d);
				if (vector3d.z > 0.0D) return;
			}
			break;

		case 1: // '\001'
			if (!detectable(actor)) return;
			if (World.Rnd().nextInt(0, 99) < 97) return;
			break;

		case 2: // '\002'
			if (getMnTime() < 1.0F) return;
			break;

		case 3: // '\003'
			if (getMnTime() < 1.0F) return;
			break;
		}
		if (this.actor instanceof TypeTransport) {
			if (AP.way.curr().Action != 3 && (get_maneuver() == 24 || get_maneuver() == 21)) {
				set_task(4);
				set_maneuver(0);
			}
			return;
		}
		if (get_task() == 2) {
			set_task(3);
			set_maneuver(0);
		}
		if (actor instanceof Aircraft) {
			if (actor instanceof TypeFighter) {
				if (turret.length > 0 && AS.astatePilotStates[turret.length] < 90) Voice.speakDanger((Aircraft) this.actor, 4);
				Voice.speakDanger((Aircraft) this.actor, 0);
			}
			Aircraft aircraft = (Aircraft) actor;
			Pilot pilot = this;
			pilot.danger = aircraft.FM;
			if ((this.actor instanceof TypeFighter) && get_task() != 4) {
				target = aircraft.FM;
				set_task(4);
				set_maneuver(0);
				clear_stack();
				return;
			}
		}
	}

	private void transportDefence() {
		int i = 0;
		for (int j = 0; j < turret.length; j++)
			if (turret[j].bIsOperable) i++;

		int k = ((Aircraft) danger.actor).aircNumber();
		int l = doTransportDefense((float) vecDanger.x, dist, Alt, Skill, courage, flying, i, k);
		set_maneuver(l);
	}

	private void stormovikDefence() {
		int i = ((Aircraft) danger.actor).aircNumber();
		int j = 0;
		for (int k = 0; k < turret.length; k++)
			if (turret[k].bIsOperable) j++;

		int l = doStormovikDefense((float) vecDanger.x, (float) onMe.x, dist, Alt, Skill, courage, flying, visible, j, i);
		if (l == 27) target = danger;
		set_maneuver(l);
	}

	private void fighterDefence() {
		if (bKeepOrdnance) bKeepOrdnance = false;
		switch (Skill) {
		case 0: // '\0'
		case 1: // '\001'
		case 2: // '\002'
		case 3: // '\003'
			int ai[] = doFighterDefense(dE, (float) vecDanger.x, (float) onMe.x, dist, (float) diffVLength, Alt, Skill, courage, flying, visible, near, onBack, looks, higher, faster, isTurnBetterThanEnemy(), isDiveBetterThanEnemy());
			set_maneuver(ai[0]);
			for (int i = 1; i < ai.length; i++)
				push(ai[i]);

			break;
		}
	}

	private boolean isDiveBetterThanEnemy() {
		return Alt > 2000F && (actor instanceof TypeBNZFighter) && VmaxAllowed > danger.VmaxAllowed + 10F;
	}

	private boolean isTurnBetterThanEnemy() {
		return Wing.T_turn < danger.getWing().T_turn && Math.abs(getSpeedKMH() - Wing.V_turn) < 100F;
	}

	public void attackBombers() {
		if (bFromPlayer) {
			bFromPlayer = false;
			return;
		}
		float f = 0.0F;
		if (CT.Weapons[1] != null && ((GunGeneric) CT.Weapons[1][0]).countBullets() > 0) f = ((GunGeneric) CT.Weapons[1][0]).bulletMassa();
		if (actor instanceof ME_163B1A) switch (World.Rnd().nextInt(0, 2)) {
		case 0: // '\0'
			setBomberAttackType(7);
			break;

		default:
			setBomberAttackType(2);
			break;
		}
		else if ((actor instanceof TypeJazzPlayer) && ((TypeJazzPlayer) actor).hasSlantedWeaponBullets())
		// TODO: Randomizes schrage musik aircraft behaviour, so they will choose which attack option is most suitable
		switch (World.Rnd().nextInt(0, 3)) {
		case 0: // '\0'
			setBomberAttackType(7);
			break;

		case 1: // '\0'
			setBomberAttackType(2);
			break;

		default:
			setBomberAttackType(10);
			break;
		}
		else switch (Skill) {
		default:
			break;

		case 0: // '\0'
			switch (World.Rnd().nextInt(0, 5)) {
			case 0: // '\0'
				setBomberAttackType(7);
				break;

			case 1: // '\001'
				setBomberAttackType(2);
				break;

			default:
				setBomberAttackType(5);
				break;
			}
			break;

		case 1: // '\001'
			if (f > 0.12F) {
				switch (World.Rnd().nextInt(0, 3)) {
				case 0: // '\0'
					setBomberAttackType(2);
					break;

				case 1: // '\001'
					setBomberAttackType(1);
					break;

				default:
					setBomberAttackType(0);
					break;
				}
				break;
			}
			switch (World.Rnd().nextInt(0, 6)) {
			case 0: // '\0'
				setBomberAttackType(1);
				break;

			case 1: // '\001'
				setBomberAttackType(7);
				break;

			default:
				setBomberAttackType(2);
				break;
			}
			break;

		case 2: // '\002'
			if (f > 0.12F) {
				switch (World.Rnd().nextInt(0, 6)) {
				case 0: // '\0'
					setBomberAttackType(2);
					break;

				case 1: // '\001'
					setBomberAttackType(1);
					break;

				default:
					setBomberAttackType(0);
					break;
				}
				break;
			}
			if (f > 0.05F) {
				switch (World.Rnd().nextInt(0, 10)) {
				case 0: // '\0'
				case 1: // '\001'
				case 2: // '\002'
					setBomberAttackType(1);
					break;

				case 3: // '\003'
					setBomberAttackType(7);
					break;

				case 4: // '\004'
					setBomberAttackType(6);
					break;

				default:
					setBomberAttackType(2);
					break;
				}
				break;
			}
			switch (World.Rnd().nextInt(0, 6)) {
			case 0: // '\0'
				setBomberAttackType(1);
				break;

			case 1: // '\001'
				setBomberAttackType(7);
				break;

			case 2: // '\002'
				setBomberAttackType(3);
				break;

			default:
				setBomberAttackType(2);
				break;
			}
			break;

		case 3: // '\003'
			if (f > 0.12F) {
				switch (World.Rnd().nextInt(0, 7)) {
				case 0: // '\0'
					setBomberAttackType(2);
					break;

				case 1: // '\001'
					setBomberAttackType(1);
					break;

				case 2: // '\002'
					setBomberAttackType(6);
					break;

				default:
					setBomberAttackType(0);
					break;
				}
				break;
			}
			if (f > 0.05F) {
				switch (World.Rnd().nextInt(0, 10)) {
				case 0: // '\0'
				case 1: // '\001'
				case 2: // '\002'
					setBomberAttackType(1);
					break;

				case 3: // '\003'
					setBomberAttackType(7);
					break;

				case 4: // '\004'
					setBomberAttackType(3);
					break;

				case 5: // '\005'
					setBomberAttackType(6);
					break;

				default:
					setBomberAttackType(2);
					break;
				}
				break;
			}
			switch (World.Rnd().nextInt(0, 4)) {
			case 0: // '\0'
				setBomberAttackType(1);
				break;

			case 1: // '\001'
				setBomberAttackType(7);
				break;

			case 2: // '\002'
				setBomberAttackType(3);
				break;

			default:
				setBomberAttackType(2);
				break;
			}
			break;
		}
		set_maneuver(63);
	}

	public void attackStormoviks() {
		switch (Skill) {
		default:
			break;

		case 0: // '\0'
			switch (World.Rnd().nextInt(0, 5)) {
			case 0: // '\0'
				setBomberAttackType(8);
				break;

			case 1: // '\001'
				setBomberAttackType(9);
				break;

			default:
				setBomberAttackType(5);
				break;
			}
			break;

		case 1: // '\001'
		case 2: // '\002'
		case 3: // '\003'
			if (target.crew > 1) {
				switch (World.Rnd().nextInt(0, 6)) {
				case 0: // '\0'
					setBomberAttackType(9);
					break;

				case 1: // '\001'
				case 2: // '\002'
					setBomberAttackType(0);
					break;

				default:
					setBomberAttackType(4);
					break;
				}
				break;
			}
			switch (World.Rnd().nextInt(0, 3)) {
			case 0: // '\0'
				setBomberAttackType(9);
				break;

			default:
				setBomberAttackType(8);
				break;
			}
			break;
		}
		set_maneuver(63);
	}

	public void wingmanAttacks(int i) {
		switch (Skill) {
		case 0: // '\0'
			switch (i) {
			case 0: // '\0'
			case 1: // '\001'
			case 3: // '\003'
			case 4: // '\004'
				switch (flying) {
				case 0: // '\0'
					followOffset.set(World.Rnd().nextFloat(150F, 200F), (float) (100 - World.Rnd().nextInt(0, 1) * 200) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 50F));
					set_maneuver(65);
					break;

				case 1: // '\001'
					followOffset.set(World.Rnd().nextFloat(100F, 150F), (float) (200 - World.Rnd().nextInt(0, 1) * 400) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 20F));
					set_maneuver(65);
					break;

				case 2: // '\002'
					followOffset.set(World.Rnd().nextFloat(50F, 100F), (float) (300 - World.Rnd().nextInt(0, 1) * 600) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-20F, 10F));
					set_maneuver(65);
					break;
				}
				break;

			case 2: // '\002'
				switch (flying) {
				case 0: // '\0'
					followOffset.set(World.Rnd().nextFloat(100F, 200F), (float) (100 - World.Rnd().nextInt(0, 1) * 300) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-100F, 150F));
					set_maneuver(65);
					break;

				case 1: // '\001'
					followOffset.set(World.Rnd().nextFloat(100F, 250F), (float) (200 - World.Rnd().nextInt(0, 1) * 500) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-100F, 120F));
					set_maneuver(65);
					break;

				case 2: // '\002'
					followOffset.set(World.Rnd().nextFloat(50F, 200F), (float) (300 - World.Rnd().nextInt(0, 1) * 600) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-200F, 100F));
					set_maneuver(65);
					break;
				}
				break;
			}
			break;

		case 1: // '\001'
			switch (i) {
			default:
				break;

			case 0: // '\0'
				if (courage == 3) {
					switch (World.Rnd().nextInt(0, 2)) {
					case 0: // '\0'
						set_maneuver(27);
						break;

					case 1: // '\001'
						set_maneuver(81);
						break;

					case 2: // '\002'
						spreadV3d.set(World.Rnd().nextFloat(-20F, 20F), (float) (350 - World.Rnd().nextInt(0, 1) * 700) + World.Rnd().nextFloat(-50F, 100F), 0.0D);
						set_maneuver(74);
						break;
					}
				} else switch (flying) {
				case 1: // '\001'
					spreadV3d.set(World.Rnd().nextFloat(50F, 100F), (float) (300 - World.Rnd().nextInt(0, 1) * 600) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-20F, 10F));
					set_maneuver(76);
					break;

				case 2: // '\002'
					spreadV3d.set(World.Rnd().nextFloat(-50F, 50F), (float) (300 - World.Rnd().nextInt(0, 1) * 600) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 10F));
					set_maneuver(76);
					break;

				case 3: // '\003'
					spreadV3d.set(World.Rnd().nextFloat(-20F, 20F), (float) (350 - World.Rnd().nextInt(0, 1) * 700) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 10F));
					set_maneuver(76);
					break;
				}
				break;

			case 1: // '\001'
				switch (flying) {
				case 1: // '\001'
					spreadV3d.set(World.Rnd().nextFloat(50F, 100F), (float) (300 - World.Rnd().nextInt(0, 1) * 600) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-20F, 10F));
					set_maneuver(76);
					break;

				case 2: // '\002'
					spreadV3d.set(World.Rnd().nextFloat(-50F, 50F), (float) (300 - World.Rnd().nextInt(0, 1) * 600) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 10F));
					set_maneuver(76);
					break;

				case 3: // '\003'
					spreadV3d.set(World.Rnd().nextFloat(-20F, 20F), (float) (350 - World.Rnd().nextInt(0, 1) * 700) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 10F));
					set_maneuver(76);
					break;
				}
				break;

			case 2: // '\002'
				switch (flying) {
				case 1: // '\001'
					followOffset.set(World.Rnd().nextFloat(100F, 200F), (float) (100 - World.Rnd().nextInt(0, 1) * 300) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-100F, 150F));
					set_maneuver(65);
					break;

				case 2: // '\002'
					followOffset.set(World.Rnd().nextFloat(100F, 250F), (float) (200 - World.Rnd().nextInt(0, 1) * 500) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-100F, 120F));
					set_maneuver(65);
					break;

				case 3: // '\003'
					followOffset.set(World.Rnd().nextFloat(50F, 200F), (float) (300 - World.Rnd().nextInt(0, 1) * 600) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-200F, 100F));
					set_maneuver(65);
					break;
				}
				break;

			case 3: // '\003'
				spreadV3d.set(World.Rnd().nextFloat(-10F, 50F), (float) (-1400 + ((Maneuver) airClient).bracketSide * 2800) + World.Rnd().nextFloat(-200F, 200F), World.Rnd().nextFloat(0.0F, 250F));
				set_maneuver(74);
				break;

			case 4: // '\004'
				set_maneuver(65);
				break;
			}
			break;

		case 2: // '\002'
			switch (i) {
			default:
				break;

			case 0: // '\0'
				switch (flying) {
				case 2: // '\002'
					setWingmanAttackType(4);
					set_maneuver(81);
					break;

				case 3: // '\003'
					setWingmanAttackType(6);
					set_maneuver(81);
					break;

				case 4: // '\004'
					setWingmanAttackType(7);
					set_maneuver(81);
					break;
				}
				break;

			case 1: // '\001'
				switch (flying) {
				case 2: // '\002'
					spreadV3d.set(World.Rnd().nextFloat(-20F, 20F), (float) (350 - World.Rnd().nextInt(0, 1) * 700) + World.Rnd().nextFloat(-50F, 100F), 0.0D);
					set_maneuver(74);
					break;

				case 3: // '\003'
					spreadV3d.set(World.Rnd().nextFloat(-100F, -300F), (float) (350 - World.Rnd().nextInt(0, 1) * 700) + World.Rnd().nextFloat(-50F, 100F), 0.0D);
					set_maneuver(74);
					break;

				case 4: // '\004'
					spreadV3d.set(World.Rnd().nextFloat(-10F, -100F), (float) (350 - World.Rnd().nextInt(0, 1) * 700) + World.Rnd().nextFloat(-50F, 100F), -20D);
					set_maneuver(74);
					break;
				}
				break;

			case 2: // '\002'
				spreadV3d.set(World.Rnd().nextFloat(-10F, 50F), (float) (100 - World.Rnd().nextInt(0, 1) * 250) + World.Rnd().nextFloat(-50F, 50F), 0.0D);
				set_maneuver(65);
				break;

			case 3: // '\003'
				spreadV3d.set(World.Rnd().nextFloat(-10F, 50F), (float) (-1400 + ((Maneuver) airClient).bracketSide * 2800) + World.Rnd().nextFloat(-150F, 150F), World.Rnd().nextFloat(0.0F, 550F));
				set_maneuver(74);
				break;

			case 4: // '\004'
				set_maneuver(65);
				break;
			}
			break;

		case 3: // '\003'
			switch (i) {
			default:
				break;

			case 0: // '\0'
				switch (flying) {
				default:
					break;

				case 3: // '\003'
					setWingmanAttackType(4);
					set_maneuver(81);
					break;

				case 4: // '\004'
					setWingmanAttackType(6);
					set_maneuver(81);
					break;

				case 5: // '\005'
					if (World.Rnd().nextBoolean()) setWingmanAttackType(8);
					else setWingmanAttackType(9);
					set_maneuver(81);
					break;
				}
				break;

			case 1: // '\001'
				switch (flying) {
				case 3: // '\003'
					spreadV3d.set(World.Rnd().nextFloat(-20F, 20F), (float) (350 - World.Rnd().nextInt(0, 1) * 700) + World.Rnd().nextFloat(-50F, 100F), 0.0D);
					set_maneuver(74);
					break;

				case 4: // '\004'
					spreadV3d.set(World.Rnd().nextFloat(-100F, -300F), (float) (350 - World.Rnd().nextInt(0, 1) * 700) + World.Rnd().nextFloat(-50F, 100F), 0.0D);
					set_maneuver(74);
					break;

				case 5: // '\005'
					spreadV3d.set(World.Rnd().nextFloat(-10F, -100F), (float) (350 - World.Rnd().nextInt(0, 1) * 700) + World.Rnd().nextFloat(-50F, 100F), -20D);
					set_maneuver(74);
					break;
				}
				break;

			case 2: // '\002'
				spreadV3d.set(World.Rnd().nextFloat(-10F, 20F), (float) (100 - World.Rnd().nextInt(0, 1) * 200) + World.Rnd().nextFloat(-50F, 50F), 0.0D);
				set_maneuver(65);
				break;

			case 3: // '\003'
				spreadV3d.set(World.Rnd().nextFloat(-10F, 50F), (float) (-1400 + ((Maneuver) airClient).bracketSide * 2800) + World.Rnd().nextFloat(-200F, 200F), World.Rnd().nextFloat(0.0F, 550F));
				set_maneuver(74);
				break;

			case 4: // '\004'
				spreadV3d.set(World.Rnd().nextFloat(-10F, 20F), (float) (100 - World.Rnd().nextInt(0, 1) * 200) + World.Rnd().nextFloat(-50F, 50F), 0.0D);
				set_maneuver(65);
				break;
			}
			break;

		default:
			set_maneuver(65);
			break;
		}
	}

	private void assignManeuverToWingmen(int i) {
		for (Pilot pilot = this; pilot.Wingman != null;) {
			pilot = (Pilot) pilot.Wingman;
			pilot.set_maneuver(i);
		}

	}

	private void assignTaskToWingmen(int i) {
		for (Pilot pilot = this; pilot.Wingman != null;) {
			pilot = (Pilot) pilot.Wingman;
			pilot.set_task(i);
		}

	}

	public boolean isLeader() {
		if (actor instanceof TypeFighter) return ((Aircraft) actor).aircIndex() % 2 == 0;
		else return Leader == null;
	}

	public boolean isLonely(float f, boolean flag) {
		if (flag) {
			if (Leader == null && Wingman == null) return true;
			double d = 0.0D;
			if (Leader != null) d = Leader.Loc.distance(Loc);
			if (Wingman != null) d = Math.min(Wingman.Loc.distance(Loc), d);
			return d > (double) f;
		}
		Actor actor = NearestTargets.getEnemy(9, -1, Loc, f, 0);
		if (Actor.isValid(actor)) return actor.pos.getAbsPoint().distance(Loc) > (double) f;
		else return true;
	}

}