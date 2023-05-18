package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.MsgShot;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.Chat;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Message;
import com.maddox.rts.ObjState;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Rocket extends ActorMesh implements MsgCollisionRequestListener, MsgCollisionListener {
	class Interpolater extends Interpolate {

		public boolean tick() {
			if (timeBegin + timeLife < Time.current()) {
				doExplosionAir();
				postDestroy();
				collide(false);
				drawing(false);
				return false;
			}
			if (timeBegin + timeFire < Time.current()) {
				endSmoke();
				P = 0.0F;
			} else {
				M -= DM;
			}
			if(spinFactor > 0.0F)
			{
				if (interpolateStep()) Ballistics.updateSpinningRocket(actor, M, S, P, timeBegin + noGDelay < Time.current(), spinFactor);
			}
			else if (interpolateStep()) Ballistics.update(actor, M, S, P, timeBegin + noGDelay < Time.current());
			return true;
		}

		Interpolater() {
		}
	}

	public void msgCollisionRequest(Actor actor, boolean aflag[]) {
		if (actor == getOwner()) aflag[0] = false;
	}

	public void msgCollision(Actor actor, String s, String s1) {
		if (getOwner() == World.getPlayerAircraft() && !(actor instanceof ActorLand)) {
			World.cur().scoreCounter.rocketsHit++;
			if (Mission.isNet() && (actor instanceof Aircraft) && ((Aircraft) actor).isNetPlayer()) Chat.sendLogRnd(3, "gore_rocketed", (Aircraft) getOwner(), (Aircraft) actor);
		}
		doExplosion(actor, s1);
	}

	protected void doExplosion(Actor actor, String s) {
		pos.getTime(Time.current(), p);
		Class class1 = getClass();
		float f = Property.floatValue(class1, "power", 1000F);
		int i = Property.intValue(class1, "powerType", 0);
		float f1 = Property.floatValue(class1, "radius", 0.0F);
		int j = Property.intValue(class1, "newEffect", 0);
		int k = Property.intValue(class1, "nuke", 0);
		getSpeed(speed);
		Vector3f vector3f = new Vector3f(speed);
		if (f1 <= 0.0F) {
			MsgShot.send(actor, s, p, vector3f, M, getOwner(), f, 1, 0.0D);
		} else {
			MsgShot.send(actor, s, p, vector3f, M, getOwner(), (float) ((double) (0.5F * M) * speed.lengthSquared()), 0, 0.0D);
			MsgExplosion.send(actor, s, p, getOwner(), M, f, i, f1, k);
		}
		Explosions.generateRocket(actor, p, f, i, f1, j);
		destroy();
	}

	protected void doExplosionAir() {
		pos.getTime(Time.current(), p);
		Class class1 = getClass();
		float f = Property.floatValue(class1, "power", 1000F);
		int i = Property.intValue(class1, "powerType", 0);
		float f1 = Property.floatValue(class1, "radius", 150F);
		MsgExplosion.send(null, null, p, getOwner(), M, f, i, f1);
		Explosions.AirFlak(p, 0);
	}

	public boolean interpolateStep() {
		return true;
	}

	protected void endSmoke() {
		if (endedSmoke) return;
		endedSmoke = true;
		if (light != null) light.light.setEmit(0.0F, 1.0F);
		Eff3DActor.finish(smoke);
		Eff3DActor.finish(sprite);
		ObjState.destroy(flame);
		stopSounds();
	}

	public void destroy() {
		endSmoke();
		super.destroy();
		smoke = null;
		sprite = null;
		flame = null;
		light = null;
		soundName = null;
	}

	protected void setThrust(float f) {
		P = f;
	}

	public double getSpeed(Vector3d vector3d) {
		if (vector3d != null) vector3d.set(speed);
		return speed.length();
	}

	public void setSpeed(Vector3d vector3d) {
		speed.set(vector3d);
	}

	protected void init(float f, float f1, float f2, float f3, float f4, float f5) {
		if (Actor.isValid(getOwner()) && World.getPlayerAircraft() == getOwner()) setName("_rocket_");
		super.getSpeed(speed);
		if (World.cur().diffCur.Wind_N_Turbulence) {
			Point3d point3d = new Point3d();
			Vector3d vector3d = new Vector3d();
			pos.getAbs(point3d);
			World.wind().getVectorWeapon(point3d, vector3d);
			speed.add(-vector3d.x, -vector3d.y, 0.0D);
		}
		S = (float) ((3.1415926535897931D * (double) f * (double) f) / 4D);
		M = f1;
		if (f3 > 0.0F) DM = (f1 - f2) / (f3 / Time.tickConstLenFs());
		else DM = 0.0F;
		P = f4;
		timeFire = (long) ((double) (f3 * 1000F) + 0.5D);
		timeLife = (long) ((double) (f5 * 1000F) + 0.5D);
	}

	public void start(float f) {
		start(f, 0);
	}

	public void start(float f, int i) {
		Class class1 = getClass();
		float f1 = Property.floatValue(class1, "kalibr", 0.082F);
		if (f <= 0.0F) f = Property.floatValue(class1, "timeLife", 45F);
		RangeRandom rangerandom = new RangeRandom(i);
		float f2 = -1F + 2.0F * rangerandom.nextFloat();
		f2 *= f2 * f2;
		float f3 = -1F + 2.0F * rangerandom.nextFloat();
		f3 *= f3 * f3;
		init(f1, Property.floatValue(class1, "massa", 6.8F), Property.floatValue(class1, "massaEnd", 2.52F), Property.floatValue(class1, "timeFire", 4F) / (1.0F + 0.1F * f2), Property.floatValue(class1, "force", 500F) * (1.0F + 0.1F * f2), f + f3 * 0.1F);
		spinFactor = Property.floatValue(class1, "spinningStraightFactor", 0.0F);
		setOwner(pos.base(), false, false, false);
		pos.setBase(null, null, true);
		pos.setAbs(pos.getCurrent());
		pos.getAbs(Aircraft.tmpOr);
		float f4 = 0.68F * Property.floatValue(class1, "maxDeltaAngle", 3F);
		f2 = -1F + 2.0F * rangerandom.nextFloat();
		f3 = -1F + 2.0F * rangerandom.nextFloat();
		f2 *= f2 * f2 * f4;
		f3 *= f3 * f3 * f4;
		Aircraft.tmpOr.increment(f2, f3, 0.0F);
		pos.setAbs(Aircraft.tmpOr);
		pos.getRelOrient().transformInv(speed);
		speed.z /= 3D;
		speed.x += 200D;
		pos.getRelOrient().transform(speed);
		collide(true);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (getOwner() == World.getPlayerAircraft()) World.cur().scoreCounter.rocketsFire++;
		if (!Config.isUSE_RENDER()) return;
		com.maddox.il2.engine.Hook hook = null;
		String s = Property.stringValue(class1, "sprite", null);
		if (s != null) {
			if (hook == null) hook = findHook("_SMOKE");
			sprite = Eff3DActor.New(this, hook, null, f1, s, -1F);
			if (sprite != null) sprite.pos.changeHookToRel();
		}
		s = Property.stringValue(class1, "flame", null);
		if (s != null) {
			if (hook == null) hook = findHook("_SMOKE");
			flame = new ActorSimpleMesh(s);
			if (flame != null) {
				((ActorSimpleMesh) flame).mesh().setScale(f1);
				flame.pos.setBase(this, hook, false);
				flame.pos.changeHookToRel();
				flame.pos.resetAsBase();
			}
		}
		s = Property.stringValue(class1, "smoke", null);
		if (s != null) {
			if (hook == null) hook = findHook("_SMOKE");
			smoke = Eff3DActor.New(this, hook, null, 1.0F, s, -1F);
			if (smoke != null) smoke.pos.changeHookToRel();
		}
		light = new LightPointActor(new LightPointWorld(), new Point3d());
		light.light.setColor((Color3f) Property.value(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
		light.light.setEmit(Property.floatValue(class1, "emitMax", 1.0F), Property.floatValue(class1, "emitLen", 50F));
		draw.lightMap().put("light", light);
		soundName = Property.stringValue(class1, "sound", null);
		if (soundName != null) newSound(soundName, true);
		setMesh(MeshShared.get(Property.stringValue(getClass(), "meshFly", Property.stringValue(getClass(), "mesh", null))));
	}

	public Object getSwitchListener(Message message) {
		return this;
	}

	public String getRocketName()
	{
		Class class1 = getClass();
		String rocketName = Property.stringValue(class1, "friendlyName", "Rocket");
		return rocketName;
	}

	public Rocket() {
		noGDelay = 1000L;
		endedSmoke = false;
		speed = new Vector3d();
		setMesh(MeshShared.get(Property.stringValue(getClass(), "mesh", null)));
		flags |= 0xe0;
		collide(false);
		drawing(true);
	}

	protected long noGDelay;
	private static Point3d p = new Point3d();
	private boolean endedSmoke;
	protected Eff3DActor smoke;
	protected Eff3DActor sprite;
	protected Actor flame;
	protected LightPointActor light;
	protected String soundName;
	protected long timeFire;
	protected long timeLife;
	protected Vector3d speed;
	private float S;
	protected float M;
	private float DM;
	private float P;
	private float spinFactor;
}