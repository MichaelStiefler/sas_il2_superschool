//////////////////////////////////////////////////////////////////////////////////
//	By PAL, Land Auxiliar, to be invoked from Gear class
//  By western, updated not to conflict with side line-up wingmans, 10th/Apr./2018
//////////////////////////////////////////////////////////////////////////////////

package com.maddox.il2.objects.humans;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.UnitInterface;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Message;
import com.maddox.rts.Time;

import com.maddox.il2.objects.air.Aircraft; //By PAL

public class LandAux extends ActorMesh
	implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Prey
{
	class Move extends Interpolate {

		public boolean tick() {
			if((st == ST_LIE || st == ST_LIEDEAD) && Time.current() >= disappearTime) {
				postDestroy();
				return false;
			}
			if(dying != DYING_NONE) {
				switch(st) {
				case ST_RUN: // '\001'
					st = ST_FALL;
					animStartTime = Time.current();
					break;

				case ST_LIE: // '\003'
					st = ST_LIEDEAD;
					idxOfDeadPose = World.Rnd().nextInt(0, 3);
					break;
				}
				setAnimFrame(Time.tickNext());
			}
			long l = Time.tickNext() - animStartTime;
			switch(st) {
			default:
				break;

			case ST_FLY: // '\0'
				pos.getAbs(LandAux.p);
				LandAux.p.scaleAdd(Time.tickLenFs(), speed, LandAux.p);
				speed.z -= Time.tickLenFs() * World.g();
				Engine.land();
				float f = Landscape.HQ((float)LandAux.p.x, (float)LandAux.p.y);
				if(LandAux.p.z <= (double)f)
				{
					speed.z = 0.0D;
					speed.normalize();
					speed.scale(RUN_SPEED / 2F); //By PAL, slower
					LandAux.p.z = f;
					st = ST_RUN;
					//By PAL
					nRunCycles = 8; //World.Rnd().nextInt(9, 17);
				}
				pos.setAbs(LandAux.p);
				break;

			case ST_RUN: // '\001'
				pos.getAbs(LandAux.p);
				LandAux.p.scaleAdd(Time.tickLenFs(), speed, LandAux.p);
				LandAux.p.z = Engine.land().HQ(LandAux.p.x, LandAux.p.y);
				pos.setAbs(LandAux.p);
				//By western, add quit decision non owner aircraft becomes nearer than owner to avoid conflict.
				if(l / RUN_CYCLE_TIME >= (long)nRunCycles || World.land().isWater(LandAux.p.x, LandAux.p.y)
				   || War.getNearestFriendAtPoint(LandAux.p, (Aircraft)getOwner(), 30F) != getOwner())
				{
					//By western, avoid disturbing AI's taxi to Take-off Go/Stop decision.
					if(getOwner() instanceof Aircraft)
					{
						Autopilotage AP = ((Aircraft)getOwner()).FM.AP;
						if(AP.way.first().waypointType == 4 || AP.way.first().waypointType == 5)
						{
							postDestroy();
							return false;
						}
					}
				//By PAL, Stop soldier
					st = ST_RUN_PARALLEL;
					o.set(oPar); //By PAL, he has to run parallel to the runway //o.set(pos.getAbsOrient());
					pos.setAbs(o);
					animStartTime = Time.current();
				}
				break;

			case ST_RUN_PARALLEL: // '\001'
				pos.getAbs(LandAux.p);
				LandAux.p.scaleAdd(Time.tickLenFs(), speedPar, LandAux.p);
				LandAux.p.z = Engine.land().HQ(LandAux.p.x, LandAux.p.y);
				pos.setAbs(LandAux.p);
				if(l / (RUN_CYCLE_TIME / 2)>= (long)nRunCycles || World.land().isWater(LandAux.p.x, LandAux.p.y))
				{
				//By PAL, Stop soldier
					st = ST_WAITING;
					o.set(pos.getAbsOrient());
					animStartTime = Time.current();
				}
				break;

	   //By PAL, added
			case ST_WAITING: // '\005'
				speed.scale(0F); //By PAL, don't move anymore
				Actor plane = getOwner();
				if(plane != null)
					if(plane instanceof Aircraft)
						//if((((Aircraft)plane).FM).Gears.getLandingState() > 0F)
						//By PAL, while the airplane still didn't acquire enough speed
						//if(((Aircraft)plane).FM.getSpeed() < 80F)
						tmpV3d.sub(plane.pos.getAbsPoint(), pos.getAbsPoint());
						if(tmpV3d.length() < 200F) { //By PAL, while I'm closer than 200m
							tmpV3d.normalize();
							//tmpV3d.scale(1D);
							oInt.setAT0(tmpV3d);
							pos.getAbs(o);
							o.interpolate(oInt, 0.1F); //By PAL, make it fluent
							pos.setAbs(o);
							break;
						}
					//By PAL, Elminiate LandAux
						postDestroy();
						return false;
				//break;

			case ST_FALL: // '\002'
				pos.getAbs(LandAux.p);
				LandAux.p.scaleAdd(Time.tickLenFs(), speed, LandAux.p);
				LandAux.p.z = Engine.land().HQ(LandAux.p.x, LandAux.p.y);
				if(World.land().isWater(LandAux.p.x, LandAux.p.y))
					LandAux.p.z -= 0.5D;
				pos.setAbs(LandAux.p);
				if(l >= FALL_CYCLE_TIME) {
					st = ST_LIE;
					animStartTime = Time.current();
					disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
				}
				break;

			case ST_LIE: // '\003'
			case ST_LIEDEAD: // '\004'
				pos.getAbs(LandAux.p);
				LandAux.p.z = Engine.land().HQ(LandAux.p.x, LandAux.p.y);
				if(World.land().isWater(LandAux.p.x, LandAux.p.y))
					LandAux.p.z -= 3D;
				pos.setAbs(LandAux.p);
				break;
			}
			setAnimFrame(Time.tickNext());
			return true;
		}

		Move() {
		}
	}

	private class AuxDraw extends ActorMeshDraw {

		public int preRender(Actor actor) {
			setAnimFrame(Time.current());
			return super.preRender(actor);
		}

		private AuxDraw() { }

	}


	public static void resetGame() {
//		preload1 = preload2 = null;
	}

	public static void PRELOAD() {
//		preload1 = new Mesh(GetMeshName(1));
//		preload2 = new Mesh(GetMeshName(2));
	}

	public void msgCollisionRequest(Actor actor, boolean aflag[]) {
		if(actor == getOwner())
			aflag[0] = false;
		if(actor instanceof Soldier)
			aflag[0] = false;
		if(dying != DYING_NONE)
			aflag[0] = false;
	}

	public void msgCollision(Actor actor, String s, String s1) {
		if(dying != DYING_NONE)
			return;
		Point3d point3d = p;
		pos.getAbs(p);
		Point3d point3d1 = actor.pos.getAbsPoint();
		Vector3d vector3d = new Vector3d();
		vector3d.set(point3d.x - point3d1.x, point3d.y - point3d1.y, 0.0D);
		if(vector3d.length() < 0.001D) {
			float f = World.Rnd().nextFloat(0.0F, 359.99F);
			vector3d.set(Geom.sinDeg(f), Geom.cosDeg(f), 0.0D);
		}
		vector3d.normalize();
		float f1 = 0.2F;
		vector3d.add(World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1));
		vector3d.normalize();
		float f2 = 13.09091F * Time.tickLenFs();
		vector3d.scale(f2);
		point3d.add(vector3d);
		pos.setAbs(point3d);
		if(st == ST_RUN) {
			st = ST_FALL;
			animStartTime = Time.current();
		}
		if(st == ST_LIE && dying == DYING_NONE && (actor instanceof UnitInterface) && actor.getSpeed(null) > 0.5D)
			Die(actor);
	}

	public void msgShot(Shot shot) {
		shot.bodyMaterial = 3;
		if(dying != DYING_NONE)
			return;
		if(shot.power <= 0.0F)
			return;
		if(shot.powerType == 1) {
			Die(shot.initiator);
			return;
		}
		if(shot.v.length() < 20D) {
			return;
		} else {
			Die(shot.initiator);
			return;
		}
	}

	public void msgExplosion(Explosion explosion) {
		if(dying != DYING_NONE)
			return;
		float f = 0.005F;
		float f1 = 0.1F;
//		Explosion _tmp = explosion;
		if(Explosion.killable(this, explosion.receivedTNT_1meter(this), f, f1, 0.0F))
			Die(explosion.initiator);
	}

	private void Die(Actor actor) {
		if(dying != DYING_NONE) {
			return;
		} else {
			World.onActorDied(this, actor);
			dying = DYING_DEAD;
			return;
		}
	}

	public void destroy() {
		super.destroy();
	}

	public Object getSwitchListener(Message message) {
		return this;
	}

	void setAnimFrame(double d) {
		int i;
		int j;
		float f;
		switch(st) {
		case ST_FLY: // '\0'
		case ST_RUN: // '\001'
		case ST_RUN_PARALLEL: //By PAL, new added
			i = RUN_START_FRAME;
			j = RUN_LAST_FRAME;
			int k = RUN_CYCLE_TIME;
			double d1 = d - (double)animStartTime;
			d1 %= k;
			if(d1 < 0.0D)
				d1 += k;
			f = (float)(d1 / (double)k);
			break;

		case ST_FALL: // '\002'
			i = FALL_START_FRAME;
			j = FALL_LAST_FRAME;
			int l = FALL_CYCLE_TIME;
			double d2 = d - (double)animStartTime;
			if(d2 <= 0.0D) {
				f = 0.0F;
				break;
			}
			if(d2 >= (double)l)
				f = 1.0F;
			else
				f = (float)(d2 / (double)l);
			break;

		case ST_LIE: // '\003'
			i = LIE_START_FRAME;
			j = LIE_LAST_FRAME;
			int i1 = LIE_CYCLE_TIME;
			double d3 = d - (double)animStartTime;
			if(d3 <= 0.0D) {
				f = 0.0F;
				break;
			}
			if(d3 >= (double)i1)
				f = 1.0F;
			else
				f = (float)(d3 / (double)i1);
			break;

		default:
			i = j = 75 + idxOfDeadPose;
			f = 0.0F;
			break;
		}
		mesh().setFrameFromRange(i, j, f);
	}

	public int HitbyMask() {
		return -25;
	}

	public int chooseBulletType(BulletProperties abulletproperties[]) {
		if(dying != DYING_NONE)
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

	public int chooseShotpoint(BulletProperties bulletproperties) {
		return dying == DYING_NONE ? 0 : -1;
	}

	public boolean getShotpointOffset(int i, Point3d point3d) {
		if(dying != DYING_NONE)
			return false;
		if(i != 0)
			return false;
		if(point3d != null)
			point3d.set(0.0D, 0.0D, 0.0D);
		return true;
	}

	private static String GetMeshName(int i) {
		boolean flag = i == 2;
//		boolean flag1 = World.cur().camouflage == 1;
		//return "3do/humans/soldiers/" + (flag ? "Germany" : "Russia") + (flag1 ? "Winter" : "Summer") + "/mono.sim";
		return "3do/humans/LandAux/" + (flag ? "Germany" : "Russia") + "/mono.sim";
	}

	public LandAux(Actor actor, int i, Loc loc, Orient opar) {
		super(GetMeshName(i));
		st = ST_FLY;
		dying = DYING_NONE;
		setOwner(actor);
		setArmy(i);
		Point3d point3d = new Point3d();
		Orient orient = new Orient();
		loc.get(point3d, orient);
		Vector3d vector3d = new Vector3d();
		vector3d.set(1.0D, 0.0D, 0.0D);
		orient.transform(vector3d);
		speed = new Vector3d();
		speed.set(vector3d);
		if(speed.length() < 0.01D)
			speed.set(1.0D, 0.0D, 0.0D);
		speed.normalize();
		if(Math.abs(speed.z) > 0.9D) {
			speed.set(1.0D, 0.0D, 0.0D);
			speed.normalize();
		}
		orient.setAT0(speed);
		orient.set(orient.azimut(), 0.0F, 0.0F);
		pos.setAbs(point3d, orient);
		pos.reset();
		speed.scale(RUN_SPEED); //By PAL, Normal Speed?

		//By PAL, to make assitant walk parallel after getting to the border of the runway.
		oPar.set(opar);
		vector3d.set(1.0D, 0.0D, 0.0D);
		oPar.transform(vector3d);
		speedPar = new Vector3d();
		speedPar.set(vector3d);
		if(speedPar.length() < 0.01D)
			speedPar.set(1.0D, 0.0D, 0.0D);
		speedPar.normalize();
		if(Math.abs(speedPar.z) > 0.9D) {
			speedPar.set(1.0D, 0.0D, 0.0D);
			speedPar.normalize();
		}
		speedPar.scale(RUN_SPEED); //By PAL, Normal Speed?

		st = ST_FLY;
		animStartTime = Time.tick() + (long)World.Rnd().nextInt(0, 2300);
		dying = DYING_NONE;
		
        // +++ TODO: Updated by SAS~Storebror: Fix Member -> Static access, fix HW shadows issues with scaled meshes!

//		//By PAL, scale it, to make different size persons. By western, skip it by conf.ini setting to avoid shadow error lines in log.lst .
//		if (!((Aircraft)getOwner()).FM.Gears.bShowChocksLandAuxiliarFIXsize)
//			this.mesh().setScaleXYZ(1F, World.Rnd().nextFloat(0.85F, 1.15F), World.Rnd().nextFloat(0.85F, 1.15F));

        if (!Gear.bShowChocksLandAuxiliarFIXsize) {
            this.mesh().setScaleXYZ(1F, World.Rnd().nextFloat(0.85F, 1.15F), World.Rnd().nextFloat(0.85F, 1.15F));
            this.mesh().setFastShadowVisibility(2); // Makes the game ignore shadow mesh data, which is necessary to avoid log flooding from scaled meshes!
        }
        
        // ---

        collide(true);
		draw = new AuxDraw();
		drawing(true);
		if(!interpEnd("move"))
			interpPut(new Move(), "move", Time.current(), null);
	}

//	private static final int FREEFLY_START_FRAME = 0;
//	private static final int FREEFLY_LAST_FRAME = 19;
//	private static final int FREEFLY_N_FRAMES = 20;
//	private static final int FREEFLY_CYCLE_TIME = 633;
//	private static final int FREEFLY_ROT_TIME = 2500;
//	private static final int PARAUP1_START_FRAME = 19;
//	private static final int PARAUP1_LAST_FRAME = 34;
//	private static final int PARAUP1_N_FRAMES = 16;
//	private static final int PARAUP1_CYCLE_TIME = 500;
//	private static final int PARAUP2_START_FRAME = 34;
//	private static final int PARAUP2_LAST_FRAME = 54;
//	private static final int PARAUP2_N_FRAMES = 21;
//	private static final int PARAUP2_CYCLE_TIME = 666;
	private static final int RUN_START_FRAME = 55;
	private static final int RUN_LAST_FRAME = 77;
//	private static final int RUN_N_FRAMES = 23;
	private static final int RUN_CYCLE_TIME = 733;
	private static final int FALL_START_FRAME = 77;
	private static final int FALL_LAST_FRAME = 109;
//	private static final int FALL_N_FRAMES = 33;
	private static final int FALL_CYCLE_TIME = 1066;
	private static final int LIE_START_FRAME = 109;
	private static final int LIE_LAST_FRAME = 128;
//	private static final int LIE_N_FRAMES = 20;
	private static final int LIE_CYCLE_TIME = 633;
//	private static final int LIEDEAD_START_FRAME = 129;
//	private static final int LIEDEAD_N_FRAMES = 4;
//	private static final int PARADEAD_FRAME = 133;
//	private static final int FREEFLYDEAD_FRAME = 134;
//	private static final int FPS = 30;
//	private static final int RUN_START_FRAME = 0;
//	private static final int RUN_LAST_FRAME = 22;
//	private static final int RUN_N_FRAMES = 23;
//	private static final int RUN_CYCLE_TIME = 733;
//	private static final int FALL_START_FRAME = 22;
//	private static final int FALL_LAST_FRAME = 54;
//	private static final int FALL_N_FRAMES = 33;
//	private static final int FALL_CYCLE_TIME = 1066;
//	private static final int LIE_START_FRAME = 54;
//	private static final int LIE_LAST_FRAME = 74;
//	private static final int LIE_N_FRAMES = 21;
//	private static final int LIE_CYCLE_TIME = 666;
//	private static final int LIEDEAD_START_FRAME = 75;
//	private static final int LIEDEAD_N_FRAMES = 4;
	private static final float RUN_SPEED = 6.545455F;
	private Vector3d speed;
	private static final int ST_FLY = 0;
	private static final int ST_RUN = 1;
	private static final int ST_FALL = 2;
	private static final int ST_LIE = 3;
	private static final int ST_LIEDEAD = 4;
	private static final int ST_RUN_PARALLEL = 5;
	private static final int ST_WAITING = 6;  //By PAL, added
	private int st;
	private int dying;
	static final int DYING_NONE = 0;
	static final int DYING_DEAD = 1;
	private int idxOfDeadPose;
	private long animStartTime;
	private long disappearTime;
	private int nRunCycles;
//	private static Mesh preload1 = null;
//	private static Mesh preload2 = null;
	private static Point3d p = new Point3d();
	private static Orient o = new Orient();
//	private static Vector3f n = new Vector3f();
	//By PAL
	private Vector3d speedPar; //By PAL, speed and Orient to run parallel to the runway.
	private static Orient oPar = new Orient();

	private Vector3d tmpV3d = new Vector3d();
	private static Orient oInt = new Orient();
}
