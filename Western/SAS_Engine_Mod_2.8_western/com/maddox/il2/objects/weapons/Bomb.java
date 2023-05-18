/*Modified Bomb class for the SAS Engine Mod*/

package com.maddox.il2.objects.weapons;

import java.util.AbstractCollection;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.Chat;
import com.maddox.il2.objects.ActorCrater;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.buildings.House;
import com.maddox.il2.objects.buildings.Plate;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.Message;
import com.maddox.rts.MsgInvokeMethod_Object;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class Bomb extends ActorMesh implements MsgCollisionRequestListener, MsgCollisionListener {
	class Interpolater extends Interpolate {

		public boolean tick() {
			interpolateTick();
			return true;
		}

		Interpolater() {
		}
	}

	static class DelayParam {

		Actor other;
		String otherChunk;
		Point3d p;
		Loc loc;

		DelayParam(Actor actor, String s, Loc loc1) {
			p = new Point3d();
			other = actor;
			otherChunk = s;
			loc1.get(p);
			if (Actor.isValid(actor)) {
				loc = new Loc();
				other.pos.getTime(Time.current(), Bomb.__loc);
				loc.sub(loc1, Bomb.__loc);
			}
		}
	}

	private static class PlateFilter implements ActorFilter {

		public boolean isUse(Actor actor, double d) {
			if (!(actor instanceof Plate)) return true;
			Mesh mesh = ((ActorMesh) actor).mesh();
			mesh.getBoundBox(Bomb.plateBox);
			Bomb.corn1.set(Bomb.corn);
			Loc loc1 = actor.pos.getAbs();
			loc1.transformInv(Bomb.corn1);
			if ((double) (Bomb.plateBox[0] - 2.5F) < Bomb.corn1.x && Bomb.corn1.x < (double) (Bomb.plateBox[3] + 2.5F) && (double) (Bomb.plateBox[1] - 2.5F) < Bomb.corn1.y && Bomb.corn1.y < (double) (Bomb.plateBox[4] + 2.5F)) {
				Bomb.bPlateExist = true;
				Bomb.bPlateGround = ((Plate) actor).isGround();
			}
			return true;
		}

		private PlateFilter() {
		}

	}

	public void destroy() {
		fuze = null;
		super.destroy();
	}

	public void msgCollisionRequest(Actor actor, boolean aflag[]) {
		if (actor == getOwner()) aflag[0] = false;
	}

	public void msgCollision(Actor actor, String s, String s1) {
		pos.getTime(Time.current(), p);
		other = actor;
		otherChunk = s1;
		if (fuze != null && !isJettisoned) fuze.isFuzeArmed(pos, speed, actor);
		float f = -dir.z;
		f *= 3.225806F;
		f = 0.85F - 0.35F * f;
		f *= getRnd().nextFloat(0.85F, 1.0F);
		if (actor != null && (actor instanceof ActorLand) && isPointApplicableForJump()) {
			if (speed.z >= 0.0D) return;
			float f1 = (float) speed.length();
			if (f1 >= 30F) {
				dir.set(speed);
				dir.scale(1.0F / f1);
				if (-dir.z < 0.31F) {
					pos.getAbs(or);
					dirN.set(1.0F, 0.0F, 0.0F);
					or.transform(dirN);
					if (dir.dot(dirN) >= 0.91F) {
						speed.scale(f);
						speed.z *= f;
						if (speed.z < 0.0D) speed.z = -speed.z;
						p.z = Engine.land().HQ(p.x, p.y);
						pos.setAbs(p);
						if (M >= 200F) f = 1.0F;
						else if (M <= 5F) f = 0.0F;
						else f = (M - 5F) / 195F;
						float f2 = 3.5F + f * 15F;
						if (Engine.land().isWater(p.x, p.y)) {
							Explosions.SomethingDrop_Water(p, f2);
							Explosions.dudBomb_Water(p, 2.0F, 1.0F);
						} else {
							Explosions.dudBomb_Land(p, 2.0F, 2.0F);
						}
						if (this instanceof FuelTank) {
							destroy();
							return;
						}
						if (fuze != null) {
							int i = Property.intValue(fuze.getClass(), "type", 0);
							if (i != 0) {
								pos.getTime(Time.current(), loc);
								if (delayExplosion > 0.0F) {
									if (isArmed() && !delayedDetonationStarted) startDelayedExplosion(null, null);
								} else {
									doExplosion(actor, s1);
								}
							}
						}
						return;
					}
				}
			}
		}
		if (getScoreOwner() == World.getPlayerAircraft() && !(actor instanceof ActorLand)) {
			World.cur().scoreCounter.bombHit++;
			if (Mission.isNet() && (actor instanceof Aircraft) && ((Aircraft) actor).isNetPlayer()) Chat.sendLogRnd(3, "gore_bombed", (Aircraft) getScoreOwner(), (Aircraft) actor);
		}
		if (actor instanceof ActorLand) {
			if (Engine.land().isWater(p.x, p.y)) {
				collide(false);
				drawing(false);
				speed.set(0.0D, 0.0D, 0.0D);
			} else {
				stop();
			}
		} else {
			stop();
			actor.pos.getTime(Time.current(), __loc);
			loc.sub(loc, __loc);
			drawing(false);
		}
		if (delayExplosion > 0.0F) {
			if (!delayedDetonationStarted) if (isArmed()) {
				startDelayedExplosion(actor, s1);
			} else {
				if (p.z < Engine.land().HQ(p.x, p.y) + 5D) if (Engine.land().isWater(p.x, p.y)) Explosions.dudBomb_Water(p, 2.0F, 2.0F);
				else if (speed.length() > 10D) Explosions.dudBomb_Land(p, 2.0F, 2.0F);
				destroy();
			}
		} else {
			doExplosion(actor, s1);
		}
	}

	private void stop() {
		pos.getTime(Time.current(), loc);
		interpEndAll();
		stopTick = true;
		collide(false);
	}

	public void setStartDelayedExplosion(boolean flag) {
		delayedDetonationStarted = flag;
	}

	private void startDelayedExplosion(Actor actor, String s) {
		delayedDetonationStarted = true;
		pos.getTime(Time.current(), loc);
		DelayParam delayparam = new DelayParam(actor, s, loc);
		if (p.z < Engine.land().HQ(p.x, p.y) + 5D) if (Engine.land().isWater(p.x, p.y)) Explosions.dudBomb_Water(p, 2.0F, 2.0F);
		else if (speed.z < -10D) Explosions.dudBomb_Land(p, 2.0F, 2.0F);
		(new MsgInvokeMethod_Object("doDelayExplosion", delayparam)).post(this, delayExplosion);
		if (sound != null) sound.cancel();
	}

	private boolean isPointApplicableForJump() {
		if (Engine.land().isWater(p.x, p.y)) return true;
		float f = 200F;
		bPlateExist = false;
		bPlateGround = false;
		p.get(corn);
		Engine.drawEnv().getFiltered((AbstractCollection) null, corn.x - (double) f, corn.y - (double) f, corn.x + (double) f, corn.y + (double) f, 1, plateFilter);
		if (bPlateExist) return true;
		int i = Engine.cur.land.HQ_RoadTypeHere(p.x, p.y);
		switch (i) {
		case 1: // '\001'
			return true;

		case 2: // '\002'
			return true;

		case 3: // '\003'
			return false;
		}
		return false;
	}

	public void doDelayExplosion(Object obj) {
		DelayParam delayparam = (DelayParam) obj;
		if (Actor.isValid(delayparam.other)) {
			delayparam.other.pos.getTime(Time.current(), __loc);
			delayparam.loc.add(__loc);
			if (other == Engine.actorLand()) {
				pos.getTime(Time.current(), p);
				Point3d point3d = p;
				doExplosion(delayparam.other, delayparam.otherChunk, point3d);
			} else {
				doExplosion(delayparam.other, delayparam.otherChunk, delayparam.loc.getPoint());
			}
		} else if (delayparam.other == null && delayparam.otherChunk == null) {
			Point3d point3d1;
			if (Actor.isValid(other) && other != Engine.actorLand()) {
				other.pos.getTime(Time.current(), __loc);
				loc.add(__loc);
				point3d1 = loc.getPoint();
			} else {
				pos.getTime(Time.current(), p);
				point3d1 = p;
			}
			if (point3d1.z > Engine.land().HQ(point3d1.x, point3d1.y) + 2D && other == Engine.actorLand()) doMidAirExplosion(point3d1);
			else doExplosion(other, otherChunk, point3d1);
		} else {
			doExplosion(Engine.actorLand(), "Body", delayparam.p);
		}
	}

	protected void doMidAirExplosion() {
		pos.getTime(Time.current(), p);
		doMidAirExplosion(p);
	}

	private void doMidAirExplosion(Point3d point3d) {
		Class class1 = getClass();
		float f = Property.floatValue(class1, "power", 1000F);
		int i = Property.intValue(class1, "powerType", 0);
		float f1 = Property.floatValue(class1, "radius", 150F);
		MsgExplosion.send(other, otherChunk, point3d, getScoreOwner(), M, f, i, f1);
		ActorCrater.initOwner = null;
		Explosions.generateMidAirBombExp(point3d, f, i, f1, false);
		destroy();
		stop();
		drawing(false);
	}

	protected void doExplosion(Actor actor, String s) {
		pos.getTime(Time.current(), p);
		if (isArmed()) {
			doExplosion(actor, s, p);
		} else {
			if (p.z < Engine.land().HQ(p.x, p.y) + 5D) if (Engine.land().isWater(p.x, p.y)) Explosions.dudBomb_Water(p, 2.0F, 2.0F);
			else if (speed.length() > 10D) Explosions.dudBomb_Land(p, 2.0F, 2.0F);
			destroy();
		}
	}

	// TODO: Edited to allow for Nukes
	protected void doExplosion(Actor actor, String s, Point3d point3d) {
		Class class1 = getClass();
		float f = Property.floatValue(class1, "power", 1000F);
		int i = Property.intValue(class1, "powerType", 0);
		// TODO: Bomb radius code here
		float f1 = Property.floatValue(class1, "radius", 7.07F * (float) Math.sqrt(Property.floatValue(class1, "massa", 150F)));
		int j = Property.intValue(class1, "newEffect", 0);
		int k = Property.intValue(class1, "nuke", 0);
		if (isArmed()) {
			float f2 = 0.0F;
			boolean flag = false;
			if (delayExplosion > 0.0F) if (actor instanceof ActorLand) {
				if (Engine.land().isWater(point3d.x, point3d.y)) {
					if (point3d.z < 0.0D) f2 = (float) point3d.z * 0.0168F;
				} else if (speed.z < -50D) {
					f1 = f1 * 0.80F;
					f = f * 0.80F;
					flag = true;
				}
			} else if (actor instanceof House) {
				f1 = f1 * 0.7F;
				f = f * 1.4F;
			} else if (((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && speed.length() > 100D) {
				f1 = f1 * 0.6F;
				f = f * 1.4F;
			}
			if (point3d.z < 0.0D) point3d.z = f2;
			MsgExplosion.send(actor, s, point3d, getScoreOwner(), M, f, i, f1, k);
			ActorCrater.initOwner = getScoreOwner();
			if (point3d.z < 0.0D) point3d.z = 0.0D;
			Explosions.generate(actor, point3d, f, i, f1, !Mission.isNet(), flag, j);
			ActorCrater.initOwner = null;
			destroy();
		} else {
			destroy();
		}
	}

	public boolean isArmed() {
		if (isJettisoned) return false;
		if (World.cur().diffCur.BombFuzes) {
			if (fuze != null) return fuze.isArmed();
			else return true;
		} else {
			return true;
		}
	}

	public void interpolateTick() {
		if (stopTick) return;
		curTm += Time.tickLenFs();
		Ballistics.updateBomb(this, M, S, J, DistFromCMtoStab);
		updateSound();
		if (fuze != null && (int) (curTm * 10F) % 10 == 0) {
			pos.getTime(Time.current(), p);
			fuze.rareAction(pos, speed);
		}
	}

	float RndFloatSign(float f, float f1) {
		f = rnd.nextFloat(f, f1);
		return rnd.nextFloat(0.0F, 1.0F) <= 0.5F ? -f : f;
	}

	private void randomizeStart(Orient orient, Vector3d vector3d, float f, int i) {
		if (i != 0) {
			dir.set(RndFloatSign(0.1F, 1.0F), RndFloatSign(0.1F, 1.0F), RndFloatSign(0.1F, 1.0F));
			dir.normalize();
		} else {
			dir.set(1.0F, 0.0F, 0.0F);
			orient.transform(dir);
			float f1 = 0.04F;
			dir.add(rnd.nextFloat(-f1, f1), rnd.nextFloat(-f1, f1), rnd.nextFloat(-f1, f1));
			dir.normalize();
		}
		orient.setAT0(dir);
		vector3d.set(RndFloatSign(0.1F, 1.0F), RndFloatSign(0.1F, 1.0F), RndFloatSign(0.1F, 1.0F));
		vector3d.normalize();
		float f2 = Geom.DEG2RAD(RndFloatSign(2.0F, 35F));
		if (f > 60F) {
			float f3 = 0.05F;
			if (f < 350F) {
				f3 = 1.0F - (f - 60F) / 290F;
				f3 = f3 * 0.95F + 0.05F;
			}
			f2 *= f3;
		}
		if (i != 0) f2 *= 0.2F;
		vector3d.scale(f2);
	}

	public double getSpeed(Vector3d vector3d) {
		if (vector3d != null) vector3d.set(speed);
		return speed.length();
	}

	public void setSpeed(Vector3d vector3d) {
		speed.set(vector3d);
	}

	protected void init(float f, float f1) {
		if (Actor.isValid(getOwner()) && World.getPlayerAircraft() == getOwner()) setName("_bomb_");
		super.getSpeed(speed);
		S = (float) ((3.1415926535897931D * (double) f * (double) f) / 4D);
		M = f1;
		M *= World.Rnd().nextFloat(1.0F, 1.06F);
		float f2 = f * 0.5F;
		float f3 = f * 4F;
		float f4 = f2;
		float f5 = f3 * 0.5F;
		J = M * 0.1F * (f4 * f4 * f5 * f5);
		DistFromCMtoStab = f3 * 0.05F;
	}

	protected void updateSound() {
		if (sound != null) {
			sound.setControl(200, (float) getSpeed(null));
			if (curTm < 5F) sound.setControl(201, curTm);
			else if (curTm < 5F + (float) (2 * Time.tickConstLen())) sound.setControl(201, 5F);
		}
	}

	protected boolean haveSound() {
		return true;
	}

	public void start() {
		Class class1 = getClass();
		init(Property.floatValue(class1, "kalibr", 0.082F), Property.floatValue(class1, "massa", 6.8F));
		curTm = 0.0F;
		setOwner(pos.base(), false, false, false);
		pos.setBase(null, null, true);
		pos.setAbs(pos.getCurrent());
		if (fuze != null) {
			fuze.setStartingTime(Time.current());
			fuze.setStartingPoint(pos.getCurrent().getPoint());
		}
		pos.getAbs(or);
		randomizeStart(or, rotAxis, M, Property.intValue(class1, "randomOrient", 0));
		pos.setAbs(or);
		getSpeed(spd);
		pos.getAbs(P, Or);
		Vector3d vector3d = new Vector3d(0.0D, 0.0D, 0.0D);
		vector3d.x += rnd.nextFloat_Dome(-2F, 2.0F);
		vector3d.y += rnd.nextFloat_Dome(-1.2F, 1.2F);
		Or.transform(vector3d);
		spd.add(vector3d);
		setSpeed(spd);
		getSpeed(spd);
		collide(true);
		interpPut(new Interpolater(), this, Time.current(), null);
		drawing(true);
		if (Actor.isAlive(World.getPlayerAircraft()) && getScoreOwner() == World.getPlayerAircraft()) {
			World.cur().scoreCounter.bombFire++;
			World.cur();
			FlightModel flightmodel = World.getPlayerFM();
			flightmodel.M.computeParasiteMass(flightmodel.CT.Weapons);
			flightmodel.getW().y -= 0.0004F * Math.min(M, 50F);
		}
		if (Property.containsValue(class1, "emitColor")) {
			LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
			lightpointactor.light.setColor((Color3f) Property.value(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
			lightpointactor.light.setEmit(Property.floatValue(class1, "emitMax", 1.0F), Property.floatValue(class1, "emitLen", 50F));
			draw.lightMap().put("light", lightpointactor);
		}
		if (haveSound()) {
			String s = Property.stringValue(class1, "sound", null);
			if (s != null) sound = newSound(s, true);
		}
	}

	public Object getSwitchListener(Message message) {
		return this;
	}

	public Bomb() {
		fuze = null;
		isJettisoned = false;
		stopTick = false;
		delayExplosion = 0.0F;
		delayedDetonationStarted = false;
		speed = new Vector3d();
		rotAxis = new Vector3d(0.0D, 0.0D, 0.0D);
		loc = new Loc();
		sound = null;
		String s = Property.stringValue(getClass(), "mesh", null);
		setMesh(MeshShared.get(s));
		flags |= 0xe0;
		collide(false);
		drawing(true);
		int i = World.Rnd().nextInt();
		rnd = new RangeRandom(i);
	}

	public void setSeed(int i) {
		seed = i;
		rnd = new RangeRandom(seed);
	}

	public void setJettisoned() {
		isJettisoned = true;
	}

	public RangeRandom getRnd() {
		return rnd;
	}

	public void setFuse(Fuze fuze1) {
		if (fuze1 == null) return;
		fuze = fuze1;
		fuze.setOwnerBomb(this);
		if (fuze.fuzeMode != 0) fuze.setDetonationDelay(fuze.getDetonationDelay());
	}

	public Actor getScoreOwner() {
		Actor actor = super.getOwner();
		if (actor instanceof Aircraft) {
			Aircraft aircraft = (Aircraft) actor;
			if (!aircraft.isNetPlayer()) {
				Aircraft aircraft1 = aircraft.getBombScoreOwner();
				if (aircraft1 != null) return aircraft1;
			}
		}
		return actor;
	}

	RangeRandom rnd;
	public Fuze fuze;
	private boolean isJettisoned;
	private boolean stopTick;
	static Vector3d spd = new Vector3d();
	static Orient Or = new Orient();
	static Point3d P = new Point3d();
	public int seed;
	protected float delayExplosion;
	private boolean delayedDetonationStarted;
	private Actor other;
	private String otherChunk;
	float curTm;
	protected Vector3d speed;
	protected float S;
	protected float M;
	protected float J;
	protected float DistFromCMtoStab;
	Vector3d rotAxis;
	protected int index;
	private static Point3d p = new Point3d();
	private static Vector3f dir = new Vector3f();
	private static Vector3f dirN = new Vector3f();
	private static Orient or = new Orient();
	private Loc loc;
	private static PlateFilter plateFilter = new PlateFilter();
	private static Point3d corn = new Point3d();
	private static Point3d corn1 = new Point3d();
	private static float plateBox[] = new float[6];
	private static boolean bPlateExist = false;
	protected static boolean bPlateGround = false;
	private static Loc __loc = new Loc();
	protected SoundFX sound;
	protected static final float SND_TIME_BOUND = 5F;
}
