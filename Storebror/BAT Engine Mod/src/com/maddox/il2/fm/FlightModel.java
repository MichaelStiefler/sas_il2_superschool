/*Modified FlightModel class for the SAS Engine Mod*/
//By western, on 19th/Nov./2017, Turrets can shoot not only Enemy Aircraft, but also ALL enemy actors.
//By western, on 29th/Sep./2019, bugfix cannot remove chocks for IK-3 etc. after spawned with Chocks set.
package com.maddox.il2.fm;

import java.util.StringTokenizer;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.VisCheck;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.ScareEnemies;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.TestRunway;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Time;

public class FlightModel extends FlightModelMain {
	// TODO: Default Parameters
	// --------------------------------------------------------------------------
	public Turret turret[];
	protected HierMesh HM;
	ActorHMesh am;
	private boolean shoot;
	public boolean turnOffCollisions;
	public boolean brakeShoe;
	public Loc brakeShoeLoc;
	public Actor brakeShoeLastCarrier;
	public boolean canChangeBrakeShoe;
	public static final int _FIRST_TURRET = 10;
	private final float tAcc1 = 0.06F;
	private final float tAcc2 = 10F;
	private final float tAcc3 = 2.5F;
	public int gunnery;
	public int sight;
	public int courage;
	public int flying;
	public int subSkill;
	public float convAI;
	public float wanderRate;
	public float shootingPoint;
	public float bankAngle;
	private static Point3d Pt = new Point3d();
	private static Vector3d Ve = new Vector3d();
	private static Vector3d Vg = new Vector3d();
	private static Vector3d Vt = new Vector3d();
	private static Orient Oo = new Orient();
	private static float tu[] = new float[3];
	public float dryFriction;
	private float rotSpeed;
	private float prev0;
	private float prev1;
	static ClipFilter clipFilter = new ClipFilter();
	static Point3d p = new Point3d();
	static Vector3d v1 = new Vector3d();
	static Point3d p2 = new Point3d();
	// --------------------------------------------------------------------------

	// TODO: New Parameters
	// --------------------------------------------------------------------------
	private static com.maddox.JGP.Vector3d v2 = new Vector3d();
	private static com.maddox.JGP.Vector3d v3 = new Vector3d();

	public boolean spawnedWithChocks;
	// --------------------------------------------------------------------------
	static class ClipFilter implements ActorFilter {

		public boolean isUse(Actor actor, double d) {
			return actor != Engine.actorLand() && actor != target && actor != owner && !(actor instanceof TestRunway);
		}

		public void setTarget(Actor actor) {
			target = actor;
		}

		public void setOwner(Actor actor) {
			owner = actor;
		}

		Actor target;
		Actor owner;

		ClipFilter() {
			target = null;
			owner = null;
		}
	}

	private native void nCreateSubSkills(int i);

	public native float nShootingPoint(int i);

	public native void nSmackMe(float f, float f1, float f2, int i, int j);

	public native void nShakeMe(int i, int j);

	public native void nLandAvoidance(float f, float f1, float f2, float f3, float f4, int i);

	public native void nFullPants(float f, int i);

	public void shakeMe(float f, float f1, float f2, float f3, float f4, float f5) {
		producedAMM.x += f;
		producedAMM.y += f1;
		producedAMM.z += f2;
		producedAF.x += f3;
		producedAF.y += f4;
		producedAF.z += f5;
	}

	public void control(float f, float f1) {
		bankAngle = f;
		CT.AileronControl = f1;
	}

	public native float nDanCoeff(double d, float f, float f1);

	public Polares getWing() {
		return Wing;
	}

	public FlightModel(String s) {
		super(s);
		turnOffCollisions = false;
		brakeShoe = false;
		brakeShoeLoc = new Loc();
		brakeShoeLastCarrier = null;
		canChangeBrakeShoe = false;
		spawnedWithChocks = false;
		dryFriction = 1.0F;
		rotSpeed = 0.0F;
		prev1 = 0.0F;
	}

	public void setSkill(int i) {
		if (i < 0) i = 0;
		if (i > 3) i = 3;
		Skill = i;
		nCreateSubSkills(Skill);
		turretSkill = i;
		// TODO: Custom gunner skills
		for (int j = 0; j < turret.length; j++) {
			turret[j].gunnerSkill = setGunner();
			turret[j].gunnerSkill *= turretSkill;
			turret[j].igunnerSkill = Math.round(turret[j].gunnerSkill);
		}
		World.cur();
		if (actor != World.getPlayerAircraft() && !((Aircraft) actor).isNetPlayer()) switch (i) {
		case 0: // '\0'
			SensPitch *= 0.75F;
			SensRoll *= 0.5F;
			SensYaw *= 0.5F;
			break;

		case 1: // '\001'
			SensRoll *= 0.7F;
			SensPitch *= 0.75F;
			SensYaw *= 0.7F;
			break;

		case 2: // '\002'
			SensRoll *= 0.88F;
			SensPitch *= 0.92F;
			SensYaw *= 0.9F;
			break;

		case 3: // '\003'
			SensPitch *= 1.1F;
			SensRoll *= 1.1F;
			SensYaw *= 1.2F;
			break;
		}
		else Aircraft.debugprintln(actor, "Skill adjustment rejected on the Player AI parameters..");
	}

	public void set(HierMesh hiermesh) {
		HM = hiermesh;
		am = (ActorHMesh) actor;
		int j;
		for (j = 1; j <= 9 && HM.chunkFindCheck("Turret" + j + "A_D0") >= 0 && HM.chunkFindCheck("Turret" + j + "B_D0") >= 0; j++)
			;
		j--;
		turret = new Turret[j];
		for (int i = 0; i < j; i++) {
			turret[i] = new Turret();
			turret[i].indexA = HM.chunkFind("Turret" + (i + 1) + "A_D0");
			turret[i].indexB = HM.chunkFind("Turret" + (i + 1) + "B_D0");
			tu[0] = tu[1] = tu[2] = 0.0F;
			HM.setCurChunk(turret[i].indexA);
			am.hierMesh().chunkSetAngles(tu);
			HM.setCurChunk(turret[i].indexB);
			am.hierMesh().chunkSetAngles(tu);
			am.getChunkLoc(turret[i].Lstart);
			turret[i].setObservingDirection();
		}

		Gears.set(hiermesh);
	}

	private void updateRotation(Turret turret1, float f) {
		tu[0] = turret1.tuLim[0];
		tu[1] = turret1.tuLim[1];
		rotSpeed = (Math.abs(prev0 - tu[0]) + Math.abs(prev1 - tu[1])) * f;
		prev0 = tu[0];
		prev1 = tu[1];
		float f1 = 20F * f;
		float f2 = tu[0] - turret1.tu[0];
		if (f2 < -f1) f2 = -f1;
		else if (f2 > f1) f2 = f1;
		tu[0] = turret1.tu[0] + f2;
		turret1.tu[0] = tu[0];
		float f3 = tu[1] - turret1.tu[1];
		if (f3 < -f1) f3 = -f1;
		else if (f3 > f1) f3 = f1;
		tu[1] = turret1.tu[1] + f3;
		turret1.tu[1] = tu[1];
		if (f2 == 0.0F && f3 == 0.0F) {
			return;
		} else {
			float f4 = tu[0];
			tu[0] = 0.0F;
			HM.setCurChunk(turret1.indexB);
			am.hierMesh().chunkSetAngles(tu);
			tu[1] = f4;
			HM.setCurChunk(turret1.indexA);
			am.hierMesh().chunkSetAngles(tu);
			return;
		}
	}

	private void updateTurret(Turret turret1, int i, float f) {
		if (!turret1.bIsOperable) {
			CT.WeaponControl[i + 10] = false;
			return;
		}
		if (!turret1.bIsAIControlled) {
			((Aircraft) this.actor).setHumanControlledTurretAngels(turret1, tu, HM, am);
			return;
		}
		if (turret1.indexA == -1 || turret1.indexB == -1) return;
		am = (ActorHMesh) this.actor;
		float f1 = 0.0F;
		float f3 = (float) (turretSkill + 1) * turret1.health;
		if (W.lengthSquared() > 0.25D) f3 *= 1.0F - (float) Math.sqrt(W.length() - 0.5D);
		if (getOverload() > 1.0F) f3 *= Aircraft.cvt(getOverload(), 1.0F, 5F, 1.0F, 0.0F);
		else if (getOverload() < 0.0F) f3 *= Aircraft.cvt(getOverload(), -1.5F, 0.0F, 0.0F, 1.0F);
		if (turret1.target != null && (turret1.target instanceof Aircraft) && ((Aircraft) turret1.target).FM.isTakenMortalDamage()) turret1.target = null;
		if (turret1.target == null) {
			if (turret1.tMode != 0) {
				turret1.tMode = 0;
				turret1.timeNext = Time.current();
			}
		} else {
			turret1.target.pos.getAbs(Pt);
			turret1.target.getSpeed(Vt);
			this.actor.getSpeed(Ve);
			Vt.sub(Ve);
			HM.setCurChunk(turret1.indexA);
			am.getChunkLocAbs(Actor._tmpLoc);
			Ve.sub(Pt, Actor._tmpLoc.getPoint());
			f1 = (float) Ve.length();
			float f2 = (14F - 2.25F * f3) * (float) Math.sin((float) (Time.current() & 65535L) * 0.003F);
			Vt.scale((f1 + f2) * 0.001492537F);
			Ve.add(Vt);
			Vg.set(Ve);
			Or.transformInv(Ve);
			turret1.Lstart.transformInv(Ve);
			Ve.y = -Ve.y;
			HM.setCurChunk(turret1.indexB);
			turret1.Lstart.get(Oo);
			Oo.setAT0(Ve);
			Oo.get(tu);
			Or.transformInv(Vt);
			turret1.Lstart.transformInv(Vt);
			Vt.normalize();
			shoot = ((Aircraft) this.actor).turretAngles(i, tu);
		}
		switch (turret1.tMode) {
		default:
			break;

		case 0: // '\0'
			turret1.bIsShooting = false;
			turret1.tuLim[0] = turret1.tuLim[1] = 0.0F;
			if (Time.current() > turret1.timeNext) {
				turret1.target = War.GetNearestEnemyAircraft(this.actor, 3619F, 9);
				if (turret1.target == null) {
					turret1.target = War.GetNearestEnemyAircraft(this.actor, 6822F, 9);
					if (turret1.target == null) turret1.timeNext = Time.current() + World.Rnd().nextLong(1000L, 10000L);
					else if (VisCheck.visCheckTurret(turret1, (Aircraft) this.actor, turret1.target, true)) {
						turret1.tMode = 1;
						turret1.timeNext = 0L;
						if ((Aircraft) this.actor == World.getPlayerAircraft() && turret1.health > 0.4F && (turret1.target instanceof Aircraft)) Voice.speakRearGunEA((Aircraft) this.actor, (Aircraft) turret1.target);
					} else {
						turret1.timeNext = Time.current() + World.Rnd().nextLong(100L, 3000L);
					}
				} else if (VisCheck.visCheckTurret(turret1, (Aircraft) this.actor, turret1.target, true)) {
					turret1.tMode = 1;
					turret1.timeNext = 0L;
					if ((Aircraft) this.actor == World.getPlayerAircraft() && turret1.health > 0.4F && (turret1.target instanceof Aircraft)) Voice.speakRearGunEA((Aircraft) this.actor, (Aircraft) turret1.target);
				} else {
					turret1.timeNext = Time.current() + World.Rnd().nextLong(100L, 3000L);
				}
			}
			break;

		case 1: // '\001'
			turret1.bIsShooting = false;
			turret1.tuLim[0] = tu[0];
			turret1.tuLim[1] = tu[1];
			if (!isTick(39, 16)) break;
			if (!shoot && f1 > 550F || World.Rnd().nextFloat() < 0.1F) {
				turret1.tMode = 0;
				turret1.timeNext = Time.current();
			}
			if ((float) (World.Rnd().nextInt() & 0xff) >= 32F * f3 && f1 >= 148F + 27F * (f3 - 1.0F)) break;
			if (f1 < 450F + 66.6F * (f3 - 1.0F)) {
				if (f3 - 1.0F <= 0.0F) {
					if (Vt.x < -0.95999997854232788D) {
						switch (World.Rnd().nextInt(1, 3)) {
						case 1: // '\001'
							turret1.tMode = 5;
							turret1.timeNext = Time.current() + World.Rnd().nextLong(500L, 1200L);
							break;

						case 2: // '\002'
							turret1.tuLim[0] += World.Rnd().nextFloat(-15F, 15F);
							turret1.tuLim[1] += World.Rnd().nextFloat(-10F, 10F);
							// fall through

						case 3: // '\003'
							turret1.tMode = 3;
							turret1.timeNext = Time.current() + World.Rnd().nextLong(500L, 10000L);
							break;
						}
						break;
					}
					if (Vt.x < -0.33000001311302185D) {
						turret1.tMode = 3;
						turret1.timeNext = Time.current() + World.Rnd().nextLong(1000L, 5000L);
					}
					break;
				}
				if (f3 - 1.0F <= 2.0F) {
					if (Vt.x < -0.9100000262260437D) {
						if (World.Rnd().nextBoolean()) turret1.tMode = 3;
						else turret1.tMode = 2;
						turret1.timeNext = Time.current() + World.Rnd().nextLong(500L, 2200L);
						break;
					}
					if (World.Rnd().nextFloat() < 0.5F) turret1.tMode = 2;
					else turret1.tMode = 3;
					turret1.timeNext = Time.current() + World.Rnd().nextLong(1500L, 7500L);
				} else {
					turret1.tMode = 2;
					turret1.timeNext = Time.current() + World.Rnd().nextLong(500L, 7500L);
				}
				break;
			}
			if (f1 < 902F + 88F * (f3 - 1.0F)) {
				turret1.tMode = 3;
				turret1.timeNext = Time.current() + World.Rnd().nextLong(100L, 1000L);
			}
			break;

		case 5: // '\005'
			turret1.bIsShooting = false;
			if (Time.current() > turret1.timeNext) {
				turret1.tMode = 0;
				turret1.timeNext = 0L;
			}
			break;

		case 3: // '\003'
			turret1.bIsShooting = true;
			turret1.tuLim[0] = tu[0] * World.Rnd().nextFloat(0.76F + 0.06F * f3, 1.24F - 0.06F * f3) + World.Rnd().nextFloat((-10F + 2.5F * f3) - rotSpeed * 5F, (10F - 2.5F * f3) + rotSpeed * 5F);
			turret1.tuLim[1] = tu[1] * World.Rnd().nextFloat(0.76F + 0.06F * f3, 1.24F - 0.06F * f3) + World.Rnd().nextFloat((-10F + 2.5F * f3) - rotSpeed * 5F, (10F - 2.5F * f3) + rotSpeed * 5F);
			if (Time.current() > turret1.timeNext) turret1.tMode = 1;
			break;

		case 2: // '\002'
			turret1.bIsShooting = true;
			turret1.tuLim[0] = tu[0] * World.Rnd().nextFloat(0.76F + 0.06F * f3, 1.24F - 0.06F * f3) + World.Rnd().nextFloat((-10F + 2.5F * f3) - rotSpeed * 4F, (10F - 2.5F * f3) + rotSpeed * 4F);
			turret1.tuLim[1] = tu[1] * World.Rnd().nextFloat(0.76F + 0.06F * f3, 1.24F - 0.06F * f3) + World.Rnd().nextFloat((-10F + 2.5F * f3) - rotSpeed * 4F, (10F - 2.5F * f3) + rotSpeed * 4F);
			if (Time.current() <= turret1.timeNext) break;
			turret1.tMode = 1;
			if (f3 - 1.0F <= 1.0F) {
				turret1.tMode = 0;
				turret1.timeNext = Time.current() + World.Rnd().nextLong(100L, (long) (f3 * 700F));
			}
			break;

		case 4: // '\004'
			turret1.bIsShooting = true;
			shoot = true;
			((Aircraft) this.actor).turretAngles(i, turret1.tuLim);
			if (isTick(20, 0)) {
				turret1.tuLim[0] += World.Rnd().nextFloat(-50F, 50F);
				turret1.tuLim[1] += World.Rnd().nextFloat(-50F, 50F);
			}
			if (Time.current() > turret1.timeNext) {
				turret1.tMode = 5;
				turret1.timeNext = Time.current() + World.Rnd().nextLong(100L, 1500L);
			}
			break;
		}
		shoot &= turret1.bIsShooting;
		if (shoot && isTick(32, 0)) {
			shoot = VisCheck.visCheckTurret(turret1, (Aircraft) this.actor, turret1.target, false);
			if (!shoot) turret1.tMode = 0;
		}
		if (shoot) {
			HM.setCurChunk(turret1.indexB);
			am.getChunkLocAbs(Actor._tmpLoc);
			Vg.normalize();
			Vg.scale(2D);
			p.set(Actor._tmpLoc.getPoint());
			p.add(Vg);
			Vg.normalize();
			Vg.scale(f1 + 300F);
			p2.set(Actor._tmpLoc.getPoint());
			p2.add(Vg);
			clipFilter.setTarget(turret1.target);
			clipFilter.setOwner(this.actor);
			Actor actor = Engine.collideEnv().getLine(p, p2, true, clipFilter, null);
			if (actor != null && actor.getArmy() == this.actor.getArmy() && actor != this.actor && (actor instanceof Aircraft)) {
				shoot = false;
			} else {
				Vt.set(1.0D, 0.0D, 0.0D);
				Actor._tmpLoc.getOrient().transform(Vt);
				double d = Vg.angle(Vt);
				if (d > 0.10000000000000001D) shoot = false;
			}
		}
		if (f3 <= 0.0F) shoot = false;
		CT.WeaponControl[i + 10] = shoot;
		updateRotation(turret1, f);
	}

	public void hit(int i) {
		if (!Actor.isValid(actor)) return;
		if (actor.isNetMirror()) {
			return;
		} else {
			super.hit(i);
			return;
		}
	}

	public float getSpeed() {
		if (!Actor.isValid(actor)) return 0.0F;
		if (actor.isNetMirror()) return (float) Vwld.length();
		else return super.getSpeed();
	}

	public void update(float f) {
		if (actor.isNetMirror()) ((NetAircraft.Mirror) actor.net).fmUpdate(f);
		else FMupdate(f);
	}

	public final void FMupdate(float f) {
		if (turret != null) {
			int j = turret.length;
			for (int i = 0; i < j; i++)
				updateTurret(turret[i], i, f);

		}
		super.update(f);
	}

	protected void putScareShpere() {
		v1.set(1.0D, 0.0D, 0.0D);
		Or.transform(v1);
		v1.scale(2000D);
		p2.set(Loc);
		p2.add(v1);
		Engine.land();
		if (Landscape.rayHitHQ(Loc, p2, p)) {
			float f = (float) ((double) getAltitude() - Engine.land().HQ_Air(Loc.x, Loc.y));
			if ((actor instanceof TypeDiveBomber) && f > 780F && Or.getTangage() < -70F) {
				ScareEnemies.set(16);
				Engine.collideEnv().getNearestEnemies(p, 75D, actor.getArmy(), ScareEnemies.enemies());
			}
			if (actor instanceof TypeStormovik) {
				if (f < 600F && Or.getTangage() < -15F) {
					ScareEnemies.set(2);
					Engine.collideEnv().getNearestEnemies(p, 45D, actor.getArmy(), ScareEnemies.enemies());
				}
			} else if ((actor instanceof TypeFighter) && f < 500F && Or.getTangage() < -15F) {
				ScareEnemies.set(2);
				Engine.collideEnv().getNearestEnemies(p, 45D, actor.getArmy(), ScareEnemies.enemies());
			}
		}
	}

	public void moveCarrier() {
		if (AP.way.isLandingOnShip()) {
			if (AP.way.landingAirport == null) {
				int i = AP.way.Cur();
				AP.way.last();
				if (AP.way.curr().Action == 2) {
					Actor actor = AP.way.curr().getTarget();
					if (actor != null && (actor instanceof BigshipGeneric)) AP.way.landingAirport = ((BigshipGeneric) actor).getAirport();
				}
				AP.way.setCur(i);
			}
			if (Actor.isAlive(AP.way.landingAirport) && !AP.way.isLanding()) AP.way.landingAirport.rebuildLastPoint(this);
		}
	}

	protected static float cvt(float f, float f1, float f2, float f3, float f4) {
		f = Math.min(Math.max(f, f1), f2);
		return f3 + ((f4 - f3) * (f - f1)) / (f2 - f1);
	}

	// TODO: New code for gunners
	private float setGunner() {
		com.maddox.rts.SectFile sectfile = Mission.cur().sectFile();
		java.lang.String s = "AOC_Gunner";
		java.lang.String s1 = super.actor.name();
		int i = s1.length();
		s1 = s1.substring(0, i - 1);
		java.lang.String s2 = sectfile.get(s, s1);
		if (s2 == null) {
			s2 = sectfile.get(s, super.actor.name());
			if (s2 == null) return 1.0F;
		}
		java.util.StringTokenizer stringtokenizer = new StringTokenizer(s2, " ");
		float af[] = new float[2];
		int j;
		for (j = 0; stringtokenizer.hasMoreElements(); j++)
			af[j] = java.lang.Float.parseFloat(stringtokenizer.nextToken());

		if (j != 1) {
			float f = af[1] * (2.0F * (float) java.lang.Math.random() - 1.0F);
			af[0] += f;
		}
		if (af[0] < 0.0F) af[0] = 0.0F;
		if (af[0] > 1.0F) af[0] = 1.0F;
		return af[0];
	}

	private boolean isNightTargetVisible(Actor actor, float f) {
		float f1 = World.Sun().sunMultiplier;
		int i = Mission.curCloudsType();
		float f2 = Mission.curCloudsHeight();
		float f3 = 500F;
		if (am.pos.getAbs().getZ() < actor.pos.getAbs().getZ()) {
			if (actor.pos.getAbs().getZ() < (double) (f2 + f3)) if (i > 2) f1 *= 1.2F;
			else if (i > 3) f1 *= 1.3F;
			f1 *= 1.1F;
		} else if (actor.pos.getAbs().getZ() > (double) (f2 + f3)) if (i > 2) f1 *= 1.2F;
		else if (i > 3) f1 *= 1.3F;
		v3.sub(am.pos.getAbsPoint(), actor.pos.getAbsPoint());
		float f4 = FlightModel.cvt(f, 0.0F, 3F, 0.75F, 1.2F);
		float f5 = FlightModel.cvt((float) v3.length(), 0.0F, 800F, 1.0F, 0.1F);
		float f6 = FlightModel.cvt(f1, 0.095F, 1.0F, 1E-005F, tAcc1) * f5 * f4;
		float f7 = World.Rnd().nextFloat();
		return f7 < f6;
	}

	private boolean isComingFromTheSun(Actor actor) {
		if ((actor instanceof Aircraft) && World.Sun().ToSun.z > 0.0F) {
			if (Mission.curCloudsType() > 3 && actor.pos.getAbs().getZ() < (double) (Mission.curCloudsHeight() + 200F)) return false;
			v3.set(World.Sun().ToSun.x, World.Sun().ToSun.y, World.Sun().ToSun.z);
			v2.sub(am.pos.getAbsPoint(), actor.pos.getAbsPoint());
			float f = (float) v2.length();
			v2.normalize();
			double d = v3.angle(v2);
			float f1 = FlightModel.cvt(f, 100F, 3000F, 2.9F, 3F);
			if (d > (double) f1) return true;
		}
		return false;
	}

	private boolean isTargetExposed(Actor actor) {
		if (actor instanceof Aircraft) {
			Aircraft aircraft = (Aircraft) actor;
			if (aircraft.FM.AS.bLandingLightOn || aircraft.FM.AS.bNavLightsOn || aircraft.FM.CT.WeaponControl[0] || aircraft.FM.CT.WeaponControl[1]) return true;
			if (World.Sun().ToMoon.z > 0.0F) {
				if (Mission.curCloudsType() > 3 && actor.pos.getAbs().getZ() < (double) (Mission.curCloudsHeight() + 200F)) return false;
				v3.set(World.Sun().ToMoon.x, World.Sun().ToMoon.y, World.Sun().ToMoon.z);
				v2.sub(am.pos.getAbsPoint(), actor.pos.getAbsPoint());
				float f = (float) v2.length();
				v2.normalize();
				double d = v3.angle(v2);
				float f1 = FlightModel.cvt(f, 100F, 3000F, 2.9F, 3F);
				if (d > (double) f1) return true;
			}
			return false;
		} else {
			return false;
		}
	}

	static {
		FMMath.loadNative();
	}
}