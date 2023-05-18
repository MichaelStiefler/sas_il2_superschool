/*4.10.1 class*/
package com.maddox.il2.objects.vehicles.artillery;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Message;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Time;

public class RocketryRocket extends ActorHMesh implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Prey
{
	static final int         DMG_LWING = 1;
	static final int         DMG_RWING = 2;
	static final int         DMG_LHYRO = 4;
	static final int         DMG_RHYRO = 8;
	static final int         DMG_ENGINE = 16;
	static final int         DMG_FUEL = 32;
	static final int         DMG_DEAD = 64;
	static final int         DMG_ANY_ = 127;
	static final int         STA_HELL = -2;
	static final int         STA_WAIT = -1;
	static final int         STARMP_RAMP = 0;
	static final int         STARMP_GOUP = 1;
	static final int         STARMP_SPEEDUP = 2;
	static final int         STARMP_TRAVEL = 3;
	static final int         STARMP_GODOWN = 4;
	static final int         STARMP_GROUND = 5;
	static final int         STAAIR_TRAVEL = 0;
	static final int         STAAIR_GODOWN = 1;
	static final int         STAAIR_GROUND = 2;
	private int dmg = 0;
	private int fallMode = -1;
	private float fallVal = 0.0F;
	private int sta = -1;
	private RocketryGeneric.TrajSeg[] traj = null;
	private RocketryGeneric ramp = null;
	private RocketryWagon wagon = null;
	int idR;
	int randseed;
	long timeOfStartMS;
	private long countdownTicks = 0L;
	private Eff3DActor eng_trail = null;
	private static Point3d tmpP = new Point3d();
	private static Vector3d tmpV = new Vector3d();
	private static Vector3d tmpV3d0 = new Vector3d();
	private static Vector3d tmpV3d1 = new Vector3d();
	private static Loc tmpL = new Loc();
	private static RangeRandom rndSeed = new RangeRandom();

	class Move extends Interpolate
	{
		private void disappear()
		{
			ramp.forgetRocket((RocketryRocket) actor);
			if (wagon != null)
			{
				wagon.forgetRocket();
				wagon = null;
			}
			RocketryRocket.this.collide(false);
			RocketryRocket.this.drawing(false);
			RocketryRocket.this.postDestroy();
		}

		public boolean tick()
		{
			if (RocketryRocket.this.Corpse())
			{
				if (access$306(RocketryRocket.this) > 0L) return true;
				disappear();
				return false;
			}
			long l = Time.current();
			float f = (float) (l - timeOfStartMS) * 0.0010F;
			int i = RocketryRocket.this.chooseTrajectorySegment(f);
			if (sta != i)
			{
				if (i == -2)
				{
					disappear();
					return false;
				}
				RocketryRocket.this.advanceState(sta, i);
			}
			RocketryRocket.this.computeCurLoc(sta, f, RocketryRocket.tmpL);
			if (fallMode > 0)
			{
				float f_0_ = RocketryRocket.tmpL.getOrient().getYaw();
				float f_1_ = RocketryRocket.tmpL.getOrient().getPitch();
				float f_2_ = RocketryRocket.tmpL.getOrient().getRoll();
				float f_3_;
				if (fallMode == 0)
					f_3_ = 0.0F;
				else if (fallMode == 1)
				{
					f_3_ = f * fallVal;
					if (f_3_ >= 360.0F)
						f_3_ %= 360.0F;
					else if (f_3_ < -360.0F)
					{
						f_3_ = -f_3_;
						f_3_ %= 360.0F;
						f_3_ = -f_3_;
					}
				}
				else if (f <= 0.0F)
					f_3_ = 0.0F;
				else if (f >= fallVal)
					f_3_ = 180.0F;
				else
				{
					float f_4_ = Math.abs(fallVal);
					f_3_ = Geom.sinDeg(f / f_4_ * 180.0F);
					f_3_ = (float) ((double) f_3_ * (fallVal < 0.0F ? 180.0 : -180.0));
				}
				RocketryRocket.tmpL.getOrient().setYPR(f_0_, f_1_, f_2_ + f_3_);
			}
			boolean bool = false;
			if ((dmg & 0x20) != 0 && access$306(RocketryRocket.this) <= 0L) bool = true;
			double d = Engine.land().HQ_Air(RocketryRocket.tmpL.getPoint().x, RocketryRocket.tmpL.getPoint().y);
			if (RocketryRocket.tmpL.getPoint().z <= d)
			{
				RocketryRocket.tmpL.getPoint().z = d;
				pos.setAbs(RocketryRocket.tmpL);
				bool = true;
			}
			else
				pos.setAbs(RocketryRocket.tmpL);
			if (bool)
			{
				RocketryRocket rocketryrocket = RocketryRocket.this;
				char c = 'X';
				if (ramp != null)
				{
					/* empty */
				}
				int i_5_ = RocketryGeneric.RndI(0, 65535);
				if (Engine.cur != null)
				{
					/* empty */
				}
				rocketryrocket.handleCommand(c, i_5_, Engine.actorLand());
			}
			return true;
		}
	}

	public class MyMsgAction extends MsgAction
	{
		Point3d posi;

		public void doAction(Object object)
		{
		/* empty */
		}

		public MyMsgAction(double d, Object object, Point3d point3d)
		{
			super(d, object);
			posi = new Point3d(point3d);
		}
	}

	//TODO: Changed by |ZUTI|: made public
	public final boolean Corpse()
	{
		return (dmg & 0x40) != 0;
	}
	//TODO: Changed by |ZUTI|: made public
	public boolean isDamaged()
	{
		return dmg != 0;
	}

	boolean isOnRamp()
	{
		return !ramp.prop.air && sta <= 0;
	}

	void silentDeath()
	{
		if (wagon != null)
		{
			wagon.forgetRocket();
			wagon = null;
		}
		destroy();
	}

	void forgetWagon()
	{
		wagon = null;
	}

	public void sendRocketStateChange(char c, Actor actor)
	{
		ramp.sendRocketStateChange(this, c, actor);
	}

	private static final long SecsToTicks(float f)
	{
		long l = (long) (0.5 + (double) (f / Time.tickLenFs()));
		return l < 1L ? 1L : l;
	}

	public void msgCollisionRequest(Actor actor, boolean[] bools)
	{
		if (Corpse())
			bools[0] = false;
		else if (actor == ramp) bools[0] = false;
	}

	public void msgCollision(Actor actor, String string, String string_6_)
	{
		if (!Corpse() && Actor.isValid(actor) && string != null && (actor.net == null || !actor.net.isMirror()) && (!(actor instanceof Aircraft) || !string.startsWith("Wing")))
		{
			if (string.startsWith("WingLIn"))
				sendRocketStateChange('l', actor);
			else if (string.startsWith("WingRIn"))
				sendRocketStateChange('r', actor);
			else if (string.startsWith("Engine1"))
				sendRocketStateChange('e', actor);
			else
				sendRocketStateChange('x', actor);
		}
	}

	public void msgShot(Shot shot)
	{
		shot.bodyMaterial = 2;
		if (!Corpse() && !(shot.power <= 0.0F) && (shot.chunkName != null && !shot.chunkName.equals("") && !shot.chunkName.equals("Body")))
		{
			float f = 0.0F;
			float f_7_ = 0.0F;
			char c = ' ';
			if (shot.chunkName.equals("CF_D0"))
			{
				c = 'x';
				f = ramp.prop.DMG_WARHEAD_MM;
				f_7_ = ramp.prop.DMG_WARHEAD_PROB;
			}
			else if (shot.chunkName.equals("Tail1_D0"))
			{
				c = 'f';
				f = ramp.prop.DMG_FUEL_MM;
				f_7_ = ramp.prop.DMG_FUEL_PROB;
			}
			else if (shot.chunkName.equals("Engine1_D0"))
			{
				c = 'e';
				f = ramp.prop.DMG_ENGINE_MM;
				f_7_ = ramp.prop.DMG_ENGINE_PROB;
			}
			else if (shot.chunkName.equals("WingLIn_D0"))
			{
				c = 'l';
				f = ramp.prop.DMG_WING_MM;
				f_7_ = ramp.prop.DMG_WING_PROB;
			}
			else if (shot.chunkName.equals("WingRIn_D0"))
			{
				c = 'r';
				f = ramp.prop.DMG_WING_MM;
				f_7_ = ramp.prop.DMG_WING_PROB;
			}
			if (!(f_7_ <= 0.0F) && Aircraft.isArmorPenetrated(f, shot))
			{
				if (ramp != null)
				{
					/* empty */
				}
				if (!(RocketryGeneric.RndF(0.0F, 1.0F) > f_7_)) sendRocketStateChange(c, shot.initiator);
			}
		}
	}

	public void msgExplosion(Explosion explosion)
	{
		if (!Corpse() && (!ramp.prop.air || sta != -1))
		{
			float f = explosion.receivedTNT_1meter(this);
			if (!(f <= 0.0F))
			{
				if (f >= ramp.prop.DMG_WARHEAD_TNT)
					sendRocketStateChange('x', explosion.initiator);
				else if (f >= ramp.prop.DMG_WING_TNT)
				{
					if (ramp != null)
					{
						/* empty */
					}
					char c = RocketryGeneric.RndF(0.0F, 1.0F) > 0.5F ? 'l' : 'r';
					if (isCommandApplicable(c)) sendRocketStateChange(c, explosion.initiator);
				}
			}
		}
	}

	public int HitbyMask()
	{
		return -1;
	}

	public int chooseBulletType(BulletProperties[] bulletpropertieses)
	{
		if (Corpse()) return -1;
		if (bulletpropertieses.length == 1) return 0;
		if (bulletpropertieses.length <= 0) return -1;
		if (bulletpropertieses[0].power <= 0.0F) return 1;
		if (bulletpropertieses[0].powerType == 1) return 0;
		if (bulletpropertieses[1].powerType == 1) return 1;
		if (bulletpropertieses[0].cumulativePower > 0.0F) return 1;
		if (bulletpropertieses[0].powerType == 2) return 1;
		return 0;
	}

	public int chooseShotpoint(BulletProperties bulletproperties)
	{
		if (Corpse()) return -1;
		return 0;
	}

	public boolean getShotpointOffset(int i, Point3d point3d)
	{
		if (Corpse()) return false;
		if (i != 0) return false;
		if (point3d != null) point3d.set(0.0, 0.0, 0.0);
		return true;
	}

	private boolean isCommandApplicable(char c)
	{
		if (Corpse()) return false;
		switch (c)
		{
		case 'X':
		case 'x':
			return (dmg & 0x40) == 0;
		case 'E':
		case 'e':
			return (dmg & 0x10) == 0;
		case 'F':
		case 'f':
			return (dmg & 0x20) == 0;
		case 'L':
		case 'l':
			return (dmg & 0x1) == 0;
		case 'R':
		case 'r':
			return (dmg & 0x2) == 0;
		case 'A':
		case 'a':
			return (dmg & 0x4) == 0;
		case 'B':
		case 'b':
			return (dmg & 0x8) == 0;
		default:
			return false;
		}
	}

	char handleCommand(char c, int i, Actor actor) {
	if (!isCommandApplicable(c))
	    return '\0';
	if (isOnRamp()) {
	    c = 'X';
	    if (wagon != null) {
		wagon.silentDeath();
		wagon = null;
	    }
	}
	switch (c) {
	case 'X':
	case 120: // 'x'
			dmg |= 0x40;
			hierMesh().chunkVisible("CF_D0", false);
			hierMesh().chunkVisible("CF_D1", true);
			tmpL.set(pos.getAbs());
			com.maddox.il2.objects.effects.Explosions.HydrogenBalloonExplosion(tmpL, null);
			new com.maddox.il2.objects.vehicles.artillery.RocketryRocket.MyMsgAction(0.0D, actor, pos.getAbsPoint())
			{

				public void doAction(java.lang.Object obj)
				{
					com.maddox.il2.ai.MsgExplosion.send(null, "Body", posi, (com.maddox.il2.engine.Actor) obj, 0.0F, ramp.prop.MASS_TNT, 0, ramp.prop.EXPLOSION_RADIUS);
				}

			};
			ramp.forgetRocket(this);
			collide(false);
			drawing(false);
			countdownTicks = com.maddox.il2.objects.vehicles.artillery.RocketryRocket.SecsToTicks(0.5F);
			breakSounds();
			return c;
	case 'E':
	case 'e':
	    dmg |= 0x10;
	    Eff3DActor.finish(eng_trail);
	    eng_trail = null;
	    hierMesh().chunkVisible("Engine1_D0", false);
	    hierMesh().chunkVisible("Engine1_D1", true);
	    Eff3DActor.New(this, findHook("_Engine1Burn"), null, 1.0F,
			   "3DO/Effects/Aircraft/FireSPD.eff", -1.0F);
	    Eff3DActor.New(this, findHook("_Engine1Burn"), null, 1.0F,
			   "3DO/Effects/Aircraft/BlackHeavySPD.eff", -1.0F);
	    Eff3DActor.New(this, findHook("_Engine1Burn"), null, 1.0F,
			   "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", -1.0F);
	    breakSounds();
	    if (fallMode < 0)
		startFalling(i, 0, 0.0F);
	    return c;
	case 'F':
	case 'f':
	    dmg |= 0x20;
	    hierMesh().chunkVisible("Tail1_D0", false);
	    hierMesh().chunkVisible("Tail1_D1", true);
	    Eff3DActor.New(this, findHook("_Tank1Burn"), null, 1.0F,
			   "3DO/Effects/Aircraft/FireSPD.eff", -1.0F);
	    Eff3DActor.New(this, findHook("_Tank1Burn"), null, 1.0F,
			   "3DO/Effects/Aircraft/BlackHeavySPD.eff", -1.0F);
	    Eff3DActor.New(this, findHook("_Tank1Burn"), null, 1.0F,
			   "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", -1.0F);
	    countdownTicks = SecsToTicks(rndSeed.nextFloat(5.0F, 40.0F));
	    return c;
	case 'L':
	case 'l':
	    dmg |= 0x1;
	    hierMesh().chunkVisible("WingLIn_D0", false);
	    hierMesh().chunkVisible("WingLIn_D1", true);
	    if (fallMode < 0)
		startFalling(i, 1, 90.0F);
	    return c;
	case 'R':
	case 'r':
	    dmg |= 0x2;
	    hierMesh().chunkVisible("WingRIn_D0", false);
	    hierMesh().chunkVisible("WingRIn_D1", true);
	    if (fallMode < 0)
		startFalling(i, 1, -90.0F);
	    return c;
	case 'A':
	case 'a':
	    dmg |= 0x4;
	    if (fallMode < 0)
		startFalling(i, 2, 5.0F);
	    return c;
	case 'B':
	case 'b':
	    dmg |= 0x8;
	    if (fallMode < 0)
		startFalling(i, 2, -5.0F);
	    return c;
	default:
	    return '\0';
	}
    }

	public Object getSwitchListener(Message message)
	{
		return this;
	}

	private int chooseTrajectorySegment(float f)
	{
		int i;
		for (i = 0; i < traj.length; i++)
		{
			if ((double) f < traj[i].t0) return i - 1;
		}
		if ((double) f < traj[i - 1].t0 + traj[i - 1].t) return i - 1;
		return -2;
	}

	private void computeCurLoc(int i, float f, Loc loc)
	{
		if (i < 0)
		{
			loc.getPoint().set(traj[0].pos0);
			if (traj[0].v0.lengthSquared() > 0.0)
				loc.getOrient().setAT0(traj[0].v0);
			else
				loc.getOrient().setAT0(traj[0].a);
		}
		else
		{
			f -= traj[i].t0;
			tmpV3d0.scale(traj[i].v0, (double) f);
			tmpV3d1.scale(traj[i].a, (double) (f * f) * 0.5);
			tmpV3d0.add(tmpV3d1);
			tmpV3d0.add(traj[i].pos0);
			loc.getPoint().set(tmpV3d0);
			tmpV3d0.scale(traj[i].a, (double) f);
			tmpV3d0.add(traj[i].v0);
			if (tmpV3d0.lengthSquared() <= 0.0) tmpV3d0.set(traj[i].a);
			loc.getOrient().setAT0(tmpV3d0);
		}
	}

	private void computeCurPhys(int i, float f, Point3d point3d, Vector3d vector3d)
	{
		if (i < 0)
		{
			point3d.set(traj[0].pos0);
			if (traj[0].v0.lengthSquared() > 0.0)
				vector3d.set(traj[0].v0);
			else
			{
				vector3d.set(traj[0].a);
				vector3d.normalize();
				vector3d.scale(0.0010);
			}
		}
		else
		{
			f -= traj[i].t0;
			tmpV3d0.scale(traj[i].v0, (double) f);
			tmpV3d1.scale(traj[i].a, (double) (f * f) * 0.5);
			tmpV3d0.add(tmpV3d1);
			tmpV3d0.add(traj[i].pos0);
			point3d.set(tmpV3d0);
			tmpV3d0.scale(traj[i].a, (double) f);
			tmpV3d0.add(traj[i].v0);
			if (tmpV3d0.lengthSquared() <= 0.0)
			{
				vector3d.set(traj[i].a);
				vector3d.normalize();
				vector3d.scale(0.0010);
			}
			else
				vector3d.set(tmpV3d0);
		}
	}

	private void startFalling(int i, int i_9_, float f)
	{
		long l = Time.current();
		float f_10_ = (float) (l - timeOfStartMS) * 0.0010F;
		int i_11_ = chooseTrajectorySegment(f_10_);
		if (i_11_ == -2)
		{
			ramp.forgetRocket(this);
			silentDeath();
		}
		else
		{
			computeCurPhys(i_11_, f_10_, tmpP, tmpV);
			traj = ramp._computeFallTrajectory_(i, tmpP, tmpV);
			fallMode = i_9_;
			fallVal = f;
			timeOfStartMS = l;
			sta = chooseTrajectorySegment(0.0F);
		}
	}

	private void advanceState(int i, int i_12_)
	{
		sta = i;
		while (sta < i_12_)
		{
			sta++;
			if (ramp.prop.air)
			{
				switch (sta)
				{
				case 0:
					collide(true);
					drawing(true);
					eng_trail = (Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Tracers/ImpulseRocket/rocket.eff", -1.0F));
					newSound(ramp.prop.soundName, true);
					break;
				case 1:
					Eff3DActor.finish(eng_trail);
					eng_trail = null;
					breakSounds();
					break;
				}
			}
			else
			{
				switch (sta)
				{
				case 0:
					eng_trail = (Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Tracers/ImpulseRocket/rocket.eff", -1.0F));
					newSound(ramp.prop.soundName, true);
					if (wagon != null)
					{
						wagon.forgetRocket();
						wagon = null;
					}
					break;
				case 1:
					break;
				case 4:
					Eff3DActor.finish(eng_trail);
					eng_trail = null;
					breakSounds();
					break;
				}
			}
		}
		sta = i_12_;
	}

	void changeLaunchTimeIfCan(long l)
	{
		if (!Corpse())
		{
			if (sta == -1)
			{
				if (Time.current() < l)
					timeOfStartMS = l;
				else
					timeOfStartMS = Time.current();
				if (wagon != null) wagon.timeOfStartMS = timeOfStartMS;
			}
		}
	}

	public RocketryRocket(RocketryGeneric rocketrygeneric, String string, int i, int i_13_, long l, long l_14_, RocketryGeneric.TrajSeg[] trajsegs)
	{
		super(string);
		ramp = rocketrygeneric;
		idR = i;
		randseed = i_13_;
		traj = trajsegs;
		timeOfStartMS = l;
		dmg = 0;
		setArmy(ramp.getArmy());
		sta = -1;
		float f = (float) (l_14_ - timeOfStartMS) * 0.0010F;
		int i_15_ = chooseTrajectorySegment(f);
		if (i_15_ == -2 || ramp.prop.air && i_15_ >= 2 || !ramp.prop.air && i_15_ >= 5)
		{
			dmg = 64;
			collide(false);
			drawing(false);
		}
		else
		{
			wagon = null;
			if (!ramp.prop.air)
			{
				RocketryGeneric.TrajSeg[] trajsegs_16_ = ramp._computeWagonTrajectory_(randseed);
				if (trajsegs_16_ == null)
				{
					dmg = 64;
					collide(false);
					drawing(false);
					return;
				}
				wagon = new RocketryWagon(this, ramp.meshNames.wagon, timeOfStartMS, l_14_, trajsegs_16_);
				if (wagon == null || !wagon.isDrawing())
				{
					dmg = 64;
					collide(false);
					drawing(false);
					return;
				}
			}
			if (ramp.prop.air)
			{
				collide(false);
				drawing(false);
			}
			else
			{
				collide(true);
				drawing(true);
			}
			setName(ramp.name() + "_" + i);
			if (sta != i_15_) advanceState(sta, i_15_);
			computeCurLoc(sta, f, tmpL);
			pos.setAbs(tmpL);
			pos.reset();
			dreamFire(true);
			if (!interpEnd("move")) interpPut(new Move(), "move", l_14_, null);
		}
	}

	static long access$306(RocketryRocket rocketryrocket)
	{
		return --rocketryrocket.countdownTicks;
	}
}