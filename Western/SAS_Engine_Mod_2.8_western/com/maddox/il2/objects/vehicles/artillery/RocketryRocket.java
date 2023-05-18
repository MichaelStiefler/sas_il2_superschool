// Source File Name:   RocketryRocket.java
// Author:	1C:Maddox , Team Daidalos
// Edit:	western0221 on 20th/Aug./2018

package com.maddox.il2.objects.vehicles.artillery;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.*;


public class RocketryRocket extends ActorHMesh
	implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Prey
{
	class Move extends Interpolate
	{

		private void disappear()
		{
			ramp.forgetRocket((RocketryRocket)actor);
			if(wagon != null)
			{
				wagon.forgetRocket();
				wagon = null;
			}
			collide(false);
			drawing(false);
			postDestroy();
		}

		public boolean tick()
		{
			if(Corpse())
				if(--countdownTicks > 0L)
				{
					return true;
				} else
				{
					disappear();
					return false;
				}
			long l = Time.current();
			float f = (float)(l - timeOfStartMS) * 0.001F;
			int i = chooseTrajectorySegment(f);
			if(sta != i)
			{
				if(i == -2)
				{
					disappear();
					return false;
				}
				advanceState(sta, i);
			}
			computeCurLoc(sta, f, RocketryRocket.tmpL);
			if(fallMode > 0)
			{
				float f1 = RocketryRocket.tmpL.getOrient().getYaw();
				float f2 = RocketryRocket.tmpL.getOrient().getPitch();
				float f3 = RocketryRocket.tmpL.getOrient().getRoll();
				float f4;
				if(fallMode == 0)
					f4 = 0.0F;
				else
				if(fallMode == 1)
				{
					f4 = f * fallVal;
					if(f4 >= 360F)
						f4 %= 360F;
					else
					if(f4 < -360F)
					{
						f4 = -f4;
						f4 %= 360F;
						f4 = -f4;
					}
				} else
				if(f <= 0.0F)
					f4 = 0.0F;
				else
				if(f >= fallVal)
				{
					f4 = 180F;
				} else
				{
					float f5 = Math.abs(fallVal);
					f4 = Geom.sinDeg((f / f5) * 180F);
					f4 = (float)((double)f4 * (fallVal >= 0.0F ? -180D : 180D));
				}
				RocketryRocket.tmpL.getOrient().setYPR(f1, f2, f3 + f4);
			}
			boolean flag = false;
			if((dmg & 0x20) != 0 && --countdownTicks <= 0L)
				flag = true;
			double d = Engine.land().HQ_Air(RocketryRocket.tmpL.getPoint().x, RocketryRocket.tmpL.getPoint().y);
			if(RocketryRocket.tmpL.getPoint().z <= d)
			{
				RocketryRocket.tmpL.getPoint().z = d;
				pos.setAbs(RocketryRocket.tmpL);
				flag = true;
			} else
			{
				pos.setAbs(RocketryRocket.tmpL);
			}
			if(flag)
				handleCommand('X', ramp.RndI(0, 65535), Engine.cur.actorLand());
			return true;
		}

		Move()
		{
		}
	}

	public class MyMsgAction extends MsgAction
	{

		public void doAction(Object obj)
		{
		}

		Point3d posi;

		public MyMsgAction(double d, Object obj, Point3d point3d)
		{
			super(d, obj);
			posi = new Point3d(point3d);
		}
	}


	public final boolean Corpse()
	{
		return (dmg & 0x40) != 0;
	}

	public boolean isDamaged()
	{
		return dmg != 0;
	}

	// By western, change method range into "public" for AAM interceptable
	public boolean isOnRamp()
	{
		return !ramp.prop.air && sta <= 0;
	}

	void silentDeath()
	{
		if(wagon != null)
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
		long l = (long)(0.5D + (double)(f / Time.tickLenFs()));
		return l >= 1L ? l : 1L;
	}

	public void msgCollisionRequest(Actor actor, boolean aflag[])
	{
		if(Corpse())
		{
			aflag[0] = false;
			return;
		}
		if(actor == ramp)
		{
			aflag[0] = false;
			return;
		} else
		{
			return;
		}
	}

	public void msgCollision(Actor actor, String s, String s1)
	{
		if(Corpse())
			return;
		if(!Actor.isValid(actor))
			return;
		if(s == null)
			return;
		if(actor.net != null && actor.net.isMirror())
			return;
		if((actor instanceof Aircraft) && s.startsWith("Wing"))
			return;
		if(s.startsWith("WingLIn"))
			sendRocketStateChange('l', actor);
		else
		if(s.startsWith("WingRIn"))
			sendRocketStateChange('r', actor);
		else
		if(s.startsWith("Engine1"))
			sendRocketStateChange('e', actor);
		else
			sendRocketStateChange('x', actor);
	}

	public void msgShot(Shot shot)
	{
		shot.bodyMaterial = 2;
		if(Corpse())
			return;
		if(shot.power <= 0.0F)
			return;
		if(shot.chunkName == null || shot.chunkName.equals("") || shot.chunkName.equals("Body"))
			return;
		float f = 0.0F;
		float f1 = 0.0F;
		char c = ' ';
		if(shot.chunkName.equals("CF_D0"))
		{
			c = 'x';
			f = ramp.prop.DMG_WARHEAD_MM;
			f1 = ramp.prop.DMG_WARHEAD_PROB;
		} else
		if(shot.chunkName.equals("Tail1_D0"))
		{
			c = 'f';
			f = ramp.prop.DMG_FUEL_MM;
			f1 = ramp.prop.DMG_FUEL_PROB;
		} else
		if(shot.chunkName.equals("Engine1_D0"))
		{
			c = 'e';
			f = ramp.prop.DMG_ENGINE_MM;
			f1 = ramp.prop.DMG_ENGINE_PROB;
		} else
		if(shot.chunkName.equals("WingLIn_D0"))
		{
			c = 'l';
			f = ramp.prop.DMG_WING_MM;
			f1 = ramp.prop.DMG_WING_PROB;
		} else
		if(shot.chunkName.equals("WingRIn_D0"))
		{
			c = 'r';
			f = ramp.prop.DMG_WING_MM;
			f1 = ramp.prop.DMG_WING_PROB;
		}
		if(f1 <= 0.0F)
			return;
		if(!Aircraft.isArmorPenetrated(f, shot))
			return;
		RocketryGeneric _tmp = ramp;
		if(RocketryGeneric.RndF(0.0F, 1.0F) > f1)
		{
			return;
		} else
		{
			sendRocketStateChange(c, shot.initiator);
			return;
		}
	}

	public void msgExplosion(Explosion explosion)
	{
		if(Corpse())
			return;
		if(ramp.prop.air && sta == -1)
			return;
		float f = explosion.receivedTNT_1meter(this);
		if(f <= 0.0F)
			return;
		if(f >= ramp.prop.DMG_WARHEAD_TNT)
			sendRocketStateChange('x', explosion.initiator);
		else
		if(f >= ramp.prop.DMG_WING_TNT)
		{
			RocketryGeneric _tmp = ramp;
			char c = RocketryGeneric.RndF(0.0F, 1.0F) <= 0.5F ? 'r' : 'l';
			if(isCommandApplicable(c))
				sendRocketStateChange(c, explosion.initiator);
		}
	}

	public int HitbyMask()
	{
		return -1;
	}

	public int chooseBulletType(BulletProperties abulletproperties[])
	{
		if(Corpse())
			return -1;
		if(abulletproperties.length == 1)
			return 0;
		if(abulletproperties.length <= 0)
			return -1;
		if(abulletproperties[0].power <= 0.0F)
			return 1;
		if(abulletproperties[0].powerType == 1)
			return 0;
		if(abulletproperties[1].powerType == 1)
			return 1;
		if(abulletproperties[0].cumulativePower > 0.0F)
			return 1;
		return abulletproperties[0].powerType != 2 ? 0 : 1;
	}

	public int chooseShotpoint(BulletProperties bulletproperties)
	{
		return !Corpse() ? 0 : -1;
	}

	public boolean getShotpointOffset(int i, Point3d point3d)
	{
		if(Corpse())
			return false;
		if(i != 0)
			return false;
		if(point3d != null)
			point3d.set(0.0D, 0.0D, 0.0D);
		return true;
	}

	private boolean isCommandApplicable(char c)
	{
		if(Corpse())
			return false;
		switch(c)
		{
		case 88: // 'X'
		case 120: // 'x'
			return (dmg & 0x40) == 0;

		case 69: // 'E'
		case 101: // 'e'
			return (dmg & 0x10) == 0;

		case 70: // 'F'
		case 102: // 'f'
			return (dmg & 0x20) == 0;

		case 76: // 'L'
		case 108: // 'l'
			return (dmg & 1) == 0;

		case 82: // 'R'
		case 114: // 'r'
			return (dmg & 2) == 0;

		case 65: // 'A'
		case 97: // 'a'
			return (dmg & 4) == 0;

		case 66: // 'B'
		case 98: // 'b'
			return (dmg & 8) == 0;

		case 67: // 'C'
		case 68: // 'D'
		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
		case 74: // 'J'
		case 75: // 'K'
		case 77: // 'M'
		case 78: // 'N'
		case 79: // 'O'
		case 80: // 'P'
		case 81: // 'Q'
		case 83: // 'S'
		case 84: // 'T'
		case 85: // 'U'
		case 86: // 'V'
		case 87: // 'W'
		case 89: // 'Y'
		case 90: // 'Z'
		case 91: // '['
		case 92: // '\\'
		case 93: // ']'
		case 94: // '^'
		case 95: // '_'
		case 96: // '`'
		case 99: // 'c'
		case 100: // 'd'
		case 103: // 'g'
		case 104: // 'h'
		case 105: // 'i'
		case 106: // 'j'
		case 107: // 'k'
		case 109: // 'm'
		case 110: // 'n'
		case 111: // 'o'
		case 112: // 'p'
		case 113: // 'q'
		case 115: // 's'
		case 116: // 't'
		case 117: // 'u'
		case 118: // 'v'
		case 119: // 'w'
		default:
			return false;
		}
	}

	char handleCommand(char c, int i, Actor actor)
	{
		if(!isCommandApplicable(c))
			return '\0';
		if(isOnRamp())
		{
			c = 'X';
			if(wagon != null)
			{
				wagon.silentDeath();
				wagon = null;
			}
		}
		switch(c)
		{
		case 88: // 'X'
		case 120: // 'x'
			dmg |= 0x40;
			hierMesh().chunkVisible("CF_D0", false);
			hierMesh().chunkVisible("CF_D1", true);
			tmpL.set(pos.getAbs());
			Explosions.HydrogenBalloonExplosion(tmpL, null);
			new MyMsgAction(0.0D, actor, pos.getAbsPoint()) {

				public void doAction(Object obj)
				{
					MsgExplosion.send(null, "Body", posi, (Actor)obj, 0.0F, ramp.prop.MASS_TNT, 0, ramp.prop.EXPLOSION_RADIUS);
				}

			}
;
			ramp.forgetRocket(this);
			collide(false);
			drawing(false);
			countdownTicks = SecsToTicks(0.5F);
			breakSounds();
			return c;

		case 69: // 'E'
		case 101: // 'e'
			dmg |= 0x10;
			Eff3DActor.finish(eng_trail);
			eng_trail = null;
			hierMesh().chunkVisible("Engine1_D0", false);
			hierMesh().chunkVisible("Engine1_D1", true);
			Eff3DActor.New(this, findHook("_Engine1Burn"), null, 1.0F, "3DO/Effects/Aircraft/FireSPD.eff", -1F);
			Eff3DActor.New(this, findHook("_Engine1Burn"), null, 1.0F, "3DO/Effects/Aircraft/BlackHeavySPD.eff", -1F);
			Eff3DActor.New(this, findHook("_Engine1Burn"), null, 1.0F, "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", -1F);
			breakSounds();
			if(fallMode < 0)
				startFalling(i, 0, 0.0F);
			return c;

		case 70: // 'F'
		case 102: // 'f'
			dmg |= 0x20;
			hierMesh().chunkVisible("Tail1_D0", false);
			hierMesh().chunkVisible("Tail1_D1", true);
			Eff3DActor.New(this, findHook("_Tank1Burn"), null, 1.0F, "3DO/Effects/Aircraft/FireSPD.eff", -1F);
			Eff3DActor.New(this, findHook("_Tank1Burn"), null, 1.0F, "3DO/Effects/Aircraft/BlackHeavySPD.eff", -1F);
			Eff3DActor.New(this, findHook("_Tank1Burn"), null, 1.0F, "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", -1F);
			countdownTicks = SecsToTicks(rndSeed.nextFloat(5F, 40F));
			return c;

		case 76: // 'L'
		case 108: // 'l'
			dmg |= 1;
			hierMesh().chunkVisible("WingLIn_D0", false);
			hierMesh().chunkVisible("WingLIn_D1", true);
			if(fallMode < 0)
				startFalling(i, 1, 90F);
			return c;

		case 82: // 'R'
		case 114: // 'r'
			dmg |= 2;
			hierMesh().chunkVisible("WingRIn_D0", false);
			hierMesh().chunkVisible("WingRIn_D1", true);
			if(fallMode < 0)
				startFalling(i, 1, -90F);
			return c;

		case 65: // 'A'
		case 97: // 'a'
			dmg |= 4;
			if(fallMode < 0)
				startFalling(i, 2, 5F);
			return c;

		case 66: // 'B'
		case 98: // 'b'
			dmg |= 8;
			if(fallMode < 0)
				startFalling(i, 2, -5F);
			return c;

		case 67: // 'C'
		case 68: // 'D'
		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
		case 74: // 'J'
		case 75: // 'K'
		case 77: // 'M'
		case 78: // 'N'
		case 79: // 'O'
		case 80: // 'P'
		case 81: // 'Q'
		case 83: // 'S'
		case 84: // 'T'
		case 85: // 'U'
		case 86: // 'V'
		case 87: // 'W'
		case 89: // 'Y'
		case 90: // 'Z'
		case 91: // '['
		case 92: // '\\'
		case 93: // ']'
		case 94: // '^'
		case 95: // '_'
		case 96: // '`'
		case 99: // 'c'
		case 100: // 'd'
		case 103: // 'g'
		case 104: // 'h'
		case 105: // 'i'
		case 106: // 'j'
		case 107: // 'k'
		case 109: // 'm'
		case 110: // 'n'
		case 111: // 'o'
		case 112: // 'p'
		case 113: // 'q'
		case 115: // 's'
		case 116: // 't'
		case 117: // 'u'
		case 118: // 'v'
		case 119: // 'w'
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
		for(i = 0; i < traj.length; i++)
			if((double)f < traj[i].t0)
				return i - 1;

		if((double)f < traj[i - 1].t0 + traj[i - 1].t)
			return i - 1;
		else
			return -2;
	}

	private void computeCurLoc(int i, float f, Loc loc)
	{
		if(i < 0)
		{
			loc.getPoint().set(traj[0].pos0);
			if(traj[0].v0.lengthSquared() > 0.0D)
				loc.getOrient().setAT0(traj[0].v0);
			else
				loc.getOrient().setAT0(traj[0].a);
			return;
		}
		f = (float)((double)f - traj[i].t0);
		tmpV3d0.scale(traj[i].v0, f);
		tmpV3d1.scale(traj[i].a, (double)(f * f) * 0.5D);
		tmpV3d0.add(tmpV3d1);
		tmpV3d0.add(traj[i].pos0);
		loc.getPoint().set(tmpV3d0);
		tmpV3d0.scale(traj[i].a, f);
		tmpV3d0.add(traj[i].v0);
		if(tmpV3d0.lengthSquared() <= 0.0D)
			tmpV3d0.set(traj[i].a);
		loc.getOrient().setAT0(tmpV3d0);
	}

	private void computeCurPhys(int i, float f, Point3d point3d, Vector3d vector3d)
	{
		if(i < 0)
		{
			point3d.set(traj[0].pos0);
			if(traj[0].v0.lengthSquared() > 0.0D)
			{
				vector3d.set(traj[0].v0);
			} else
			{
				vector3d.set(traj[0].a);
				vector3d.normalize();
				vector3d.scale(0.001D);
			}
			return;
		}
		f = (float)((double)f - traj[i].t0);
		tmpV3d0.scale(traj[i].v0, f);
		tmpV3d1.scale(traj[i].a, (double)(f * f) * 0.5D);
		tmpV3d0.add(tmpV3d1);
		tmpV3d0.add(traj[i].pos0);
		point3d.set(tmpV3d0);
		tmpV3d0.scale(traj[i].a, f);
		tmpV3d0.add(traj[i].v0);
		if(tmpV3d0.lengthSquared() <= 0.0D)
		{
			vector3d.set(traj[i].a);
			vector3d.normalize();
			vector3d.scale(0.001D);
		} else
		{
			vector3d.set(tmpV3d0);
		}
	}

	private void startFalling(int i, int j, float f)
	{
		long l = Time.current();
		float f1 = (float)(l - timeOfStartMS) * 0.001F;
		int k = chooseTrajectorySegment(f1);
		if(k == -2)
		{
			ramp.forgetRocket(this);
			silentDeath();
			return;
		} else
		{
			computeCurPhys(k, f1, tmpP, tmpV);
			traj = ramp._computeFallTrajectory_(i, tmpP, tmpV);
			fallMode = j;
			fallVal = f;
			timeOfStartMS = l;
			sta = chooseTrajectorySegment(0.0F);
			return;
		}
	}

	private void advanceState(int i, int j)
	{
		sta = i;
		do
			if(sta < j)
			{
				sta++;
				if(ramp.prop.air)
					switch(sta)
					{
					case 0: // '\0'
						collide(true);
						drawing(true);
						eng_trail = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Tracers/ImpulseRocket/rocket.eff", -1F);
						newSound(ramp.prop.soundName, true);
						break;

					case 1: // '\001'
						Eff3DActor.finish(eng_trail);
						eng_trail = null;
						breakSounds();
						break;
					}
				else
					switch(sta)
					{
					case 0: // '\0'
						eng_trail = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Tracers/ImpulseRocket/rocket.eff", -1F);
						newSound(ramp.prop.soundName, true);
						if(wagon != null)
						{
							wagon.forgetRocket();
							wagon = null;
						}
						break;

					case 4: // '\004'
						Eff3DActor.finish(eng_trail);
						eng_trail = null;
						breakSounds();
						break;
					}
			} else
			{
				sta = j;
				return;
			}
		while(true);
	}

	void changeLaunchTimeIfCan(long l)
	{
		if(Corpse())
			return;
		if(sta == -1)
		{
			if(Time.current() < l)
				timeOfStartMS = l;
			else
				timeOfStartMS = Time.current();
			if(wagon != null)
				wagon.timeOfStartMS = timeOfStartMS;
		}
	}

	public RocketryRocket(RocketryGeneric rocketrygeneric, String s, int i, int j, long l, long l1, RocketryGeneric.TrajSeg atrajseg[])
	{
		super(s);
		dmg = 0;
		fallMode = -1;
		fallVal = 0.0F;
		sta = -1;
		traj = null;
		ramp = null;
		wagon = null;
		countdownTicks = 0L;
		eng_trail = null;
		ramp = rocketrygeneric;
		idR = i;
		randseed = j;
		traj = atrajseg;
		timeOfStartMS = l;
		dmg = 0;
		setArmy(ramp.getArmy());
		sta = -1;
		float f = (float)(l1 - timeOfStartMS) * 0.001F;
		int k = chooseTrajectorySegment(f);
		if(k == -2 || ramp.prop.air && k >= 2 || !ramp.prop.air && k >= 5)
		{
			dmg = 64;
			collide(false);
			drawing(false);
			return;
		}
		wagon = null;
		if(!ramp.prop.air)
		{
			RocketryGeneric.TrajSeg atrajseg1[] = ramp._computeWagonTrajectory_(randseed);
			if(atrajseg1 == null)
			{
				dmg = 64;
				collide(false);
				drawing(false);
				return;
			}
			wagon = new RocketryWagon(this, ramp.meshNames.wagon, timeOfStartMS, l1, atrajseg1);
			if(wagon == null || !wagon.isDrawing())
			{
				dmg = 64;
				collide(false);
				drawing(false);
				return;
			}
		}
		if(ramp.prop.air)
		{
			collide(false);
			drawing(false);
		} else
		{
			collide(true);
			drawing(true);
		}
		setName(ramp.name() + "_" + i);
		if(sta != k)
			advanceState(sta, k);
		computeCurLoc(sta, f, tmpL);
		pos.setAbs(tmpL);
		pos.reset();
		dreamFire(true);
		if(!interpEnd("move"))
			interpPut(new Move(), "move", l1, null);
	}

	private static final int DMG_LWING = 1;
	private static final int DMG_RWING = 2;
	private static final int DMG_LHYRO = 4;
	private static final int DMG_RHYRO = 8;
	private static final int DMG_ENGINE = 16;
	private static final int DMG_FUEL = 32;
	private static final int DMG_DEAD = 64;
	private static final int DMG_ANY_ = 127;
	private static final int STA_HELL = -2;
	private static final int STA_WAIT = -1;
	private static final int STARMP_RAMP = 0;
	private static final int STARMP_GOUP = 1;
	private static final int STARMP_SPEEDUP = 2;
	private static final int STARMP_TRAVEL = 3;
	private static final int STARMP_GODOWN = 4;
	private static final int STARMP_GROUND = 5;
	private static final int STAAIR_TRAVEL = 0;
	private static final int STAAIR_GODOWN = 1;
	private static final int STAAIR_GROUND = 2;
	private int dmg;
	private int fallMode;
	private float fallVal;
	private int sta;
	private RocketryGeneric.TrajSeg traj[];
	private RocketryGeneric ramp;
	private RocketryWagon wagon;
	int idR;
	int randseed;
	long timeOfStartMS;
	private long countdownTicks;
	private Eff3DActor eng_trail;
	private static Point3d tmpP = new Point3d();
	private static Vector3d tmpV = new Vector3d();
	private static Vector3d tmpV3d0 = new Vector3d();
	private static Vector3d tmpV3d1 = new Vector3d();
	private static Loc tmpL = new Loc();
	private static RangeRandom rndSeed = new RangeRandom();
}
