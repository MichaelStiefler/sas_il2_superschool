// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   Paratrooper.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.ScoreItem;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.Chat;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.*;

import java.io.IOException;
import java.io.PrintStream;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;

import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            Aircraft, Chute, NetGunner, NetAircraft

public class ParatrooperSoldierAirborn extends ActorMesh implements
		MsgCollisionRequestListener, MsgCollisionListener,
		MsgExplosionListener, MsgShotListener {
	public static class SPAWN implements NetSpawn {

		public void netSpawn(int i, NetMsgInput netmsginput) {
			try {
				Loc loc = new Loc(netmsginput.readFloat(),
						netmsginput.readFloat(), netmsginput.readFloat(),
						netmsginput.readFloat(), netmsginput.readFloat(),
						netmsginput.readFloat());
				Vector3d vector3d = new Vector3d(netmsginput.readFloat(),
						netmsginput.readFloat(), netmsginput.readFloat());
				Actor actor = null;
				NetObj netobj = netmsginput.readNetObj();
				if (netobj != null)
					actor = (Actor) netobj.superObj();
				Paratrooper paratrooper = new Paratrooper(actor,
						netmsginput.readUnsignedByte(),
						netmsginput.readUnsignedByte(), loc, vector3d,
						netmsginput, i);
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}

		public SPAWN() {
		}
	}

	class Mirror extends ParaNet {

		public Mirror(Actor actor, NetMsgInput netmsginput, int i) {
			super(actor, netmsginput, i);
			try {
				turn_para_on_height = netmsginput.readFloat();
				nRunCycles = netmsginput.readByte();
				driver = (NetUser) netmsginput.readNetObj();
			} catch (Exception exception) {
			}
		}
	}

	class Master extends ParaNet {

		public Master(Actor actor) {
			super(actor);
			actor.pos.getAbs(ParatrooperSoldierAirborn.p);
			float f = (float) ParatrooperSoldierAirborn.p.z
					- Engine.land().HQ((float) ParatrooperSoldierAirborn.p.x,
							(float) ParatrooperSoldierAirborn.p.y);
			if (f <= 500F)
				turn_para_on_height = 500F;
			else if (f >= 4000F)
				turn_para_on_height = 2000F;
			else
				turn_para_on_height = 500F + 1500F * ((f - 500F) / 3500F);
			turn_para_on_height *= World.Rnd().nextFloat(1.0F, 1.2F);
			nRunCycles = World.Rnd().nextInt(6, 18);
			Class class1 = actor.getOwner().getClass();
			Object obj = Property.value(class1, "cockpitClass");
			if (obj != null) {
				Class aclass[] = null;
				if (obj instanceof Class) {
					aclass = new Class[1];
					aclass[0] = (Class) obj;
				} else {
					aclass = (Class[]) (Class[]) obj;
				}
				for (int i = 0; i < aclass.length; i++) {
					int j = Property.intValue(aclass[i], "astatePilotIndx", 0);
					if (j != idxOfPilotPlace)
						continue;
					Actor actor1 = ((Aircraft) actor.getOwner())
							.netCockpitGetDriver(i);
					if (actor1 == null)
						continue;
					if (Mission.isSingle()) {
						driver = (NetUser) NetEnv.host();
						break;
					}
					if (actor1 instanceof NetGunner)
						driver = ((NetGunner) actor1).getUser();
					else
						driver = ((NetAircraft) actor1).netUser();
					break;
				}

			}
		}
	}

	class ParaNet extends ActorNet {

		public boolean netInput(NetMsgInput netmsginput) throws IOException {
			if (!netmsginput.isGuaranted())
				return false;
			byte byte0 = netmsginput.readByte();
			byte byte1 = -1;
			switch (byte0) {
			case 68: // 'D'
				byte1 = 1;
				NetObj netobj = netmsginput.readNetObj();
				Actor actor = null;
				if (netobj != null)
					actor = (Actor) netobj.superObj();
				Die(actor, false);
				break;

			case 83: // 'S'
				byte1 = 1;
				NetObj netobj1 = netmsginput.readNetObj();
				Actor actor1 = null;
				if (netobj1 != null)
					actor1 = (Actor) netobj1.superObj();
				Object aobj[] = getOwnerAttached();
				for (int i = 0; i < aobj.length; i++) {
					Chute chute = (Chute) aobj[i];
					if (Actor.isValid(chute))
						chute.tangleChute(actor1);
				}

				break;
			}
			if (byte1 >= 0) {
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(
						netmsginput, byte1);
				postExclude(netmsginput.channel(), netmsgguaranted);
				return true;
			} else {
				return false;
			}
		}

		public ParaNet(Actor actor) {
			super(actor);
		}

		public ParaNet(Actor actor, NetMsgInput netmsginput, int i) {
			super(actor, netmsginput.channel(), i);
		}
	}

	private Actor insert(ActorSpawn actorspawn, Loc loc, int i) {
		spawnArg.clear();
		spawnArg.point = loc.getPoint();
		spawnArg.orient = loc.getOrient();
		spawnArg.army = i;
		spawnArg.armyExist = true;
		Actor actor;
		actor = actorspawn.actorSpawn(spawnArg);
		((com.maddox.il2.objects.ActorAlign) (com.maddox.il2.objects.ActorAlign) actor)
				.align();
		return actor;
	}

	class Move extends Interpolate {

		public boolean tick() {
			// HUD.log(AircraftHotKeys.hudLogWeaponId, "222");

			// com.maddox.JGP.Point3d point3d = new Point3d();
			//
			// com.maddox.il2.engine.Actor actor3 = getOwner();
			//
			// Random randomGenerator = new Random();
			// int ii = randomGenerator.nextInt(360);
			// pos.getAbs(ParatrooperVDV.p);
			// orient.setYPR(orient.getYaw(), orient.getPitch(),
			// orient.getRoll());
			//
			// HUD.log(AircraftHotKeys.hudLogWeaponId, "333");
			//
			// class1 =
			// com.maddox.il2.objects.vehicles.artillery.ArtilleryWasted.VDVInfantry.class;
			//
			// spawn = (ActorSpawn) Spawn.get(class1.getName());
			//
			//
			// insert(spawn, new Loc (pos.getAbsPoint().x, pos.getAbsPoint().y,
			// 0.0F, ii, 0.0F, 0.0F), actor3.getArmy());
			//
			// World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102,
			// 15D, false));
			// postDestroy();
			// pos.getAbs(point3d);
			if (st == 9) {
				if (dying == 0)
					postDestroy();
				EventLog.type("245");
				return false;
			}
			if ((st == 6 || st == 7 || st == 8)
					&& Time.current() >= disappearTime) {
				postDestroy();
				EventLog.type("251");
				return false;
			}
			if (dying != 0)
				switch (st) {
				case 4: // '\004'
					st = 5;
					animStartTime = Time.current();
					EventLog.type("260");
					break;

				case 6: // '\006'
					st = 7;
					idxOfDeadPose = World.Rnd().nextInt(0, 3);
					break;
				}
			long l = Time.tickNext() - animStartTime;
			switch (st) {
			default:
				break;

			case 0: // '\0'
			case 1: // '\001'
			case 2: // '\002'
			case 3: // '\003'
				pos.getAbs(ParatrooperSoldierAirborn.p);
				Engine.land();
				float f = Landscape.HQ((float) ParatrooperSoldierAirborn.p.x,
						(float) ParatrooperSoldierAirborn.p.y);
				if (st == 0)
					if (l >= 2500L) {
						pos.setAbs(faceOrient);
						if (dying == 0
								&& (float) ParatrooperSoldierAirborn.p.z - f <= turn_para_on_height
								&& speed.z < -5D) {
							st = 1;
							animStartTime = Time.current();
							l = Time.tickNext() - animStartTime;
							new Chute(actor);
							EventLog.type("291");
						}
					} else {
						pos.getAbs(ParatrooperSoldierAirborn.o);
						float f1 = (float) l / 2500F;
						if (f1 <= 0.0F)
							f1 = 0.0F;
						if (f1 >= 1.0F)
							f1 = 1.0F;
						ParatrooperSoldierAirborn.o.interpolate(startOrient, faceOrient,
								f1);
						pos.setAbs(ParatrooperSoldierAirborn.o);
						EventLog.type("303");
					}
				if (st == 1 && l >= 500L) {
					st = 2;
					animStartTime = Time.current();
					l = Time.tickNext() - animStartTime;
					EventLog.type("310");
				}
				ParatrooperSoldierAirborn.p.scaleAdd(Time.tickLenFs(), speed,
						ParatrooperSoldierAirborn.p);
				speed.z -= Time.tickLenFs() * World.g();
				if (st == 2) {
					if (speed.x != 0.0D)
						speed.x -= (Math.abs(speed.x) / speed.x)
								* 0.0099999997764825821D * (speed.x * speed.x)
								* (double) Time.tickLenFs();
					if (speed.y != 0.0D)
						speed.y -= (Math.abs(speed.y) / speed.y)
								* 0.0099999997764825821D * (speed.y * speed.y)
								* (double) Time.tickLenFs();
					EventLog.type("320");
				} else {
					if (speed.x != 0.0D)
						speed.x -= (Math.abs(speed.x) / speed.x)
								* 0.0010000000474974513D * (speed.x * speed.x)
								* (double) Time.tickLenFs();
					if (speed.y != 0.0D)
						speed.y -= (Math.abs(speed.y) / speed.y)
								* 0.0010000000474974513D * (speed.y * speed.y)
								* (double) Time.tickLenFs();
					EventLog.type("327");
				}
				double d = st != 2 ? 50F : 5F;
				if (-speed.z > d) {
					double d1 = -speed.z - d;
					if (d1 > (double) (Time.tickLenFs() * 20F))
						d1 = Time.tickLenFs() * 20F;
					speed.z += d1;
					EventLog.type("336");
				}
				if (ParatrooperSoldierAirborn.p.z <= (double) f) {
					boolean flag = speed.length() > 10.5D;
					Vector3d vector3d = new Vector3d();
					vector3d.set(1.0D, 0.0D, 0.0D);
					faceOrient.transform(vector3d);
					speed.set(vector3d);
					speed.z = 0.0D;
					speed.normalize();
					speed.scale(6.5454545021057129D);
					ParatrooperSoldierAirborn.p.z = f;
					if (flag || dying != 0) {
						EventLog.type("351");
						st = 7;
						animStartTime = Time.current();
						disappearTime = Time.tickNext()
								+ (long) (1000 * World.Rnd().nextInt(25, 35));
						idxOfDeadPose = World.Rnd().nextInt(0, 3);
						new MsgAction(0.0D, actor) {

							public void doAction(Object obj) {
								ParatrooperSoldierAirborn paratrooper = (ParatrooperSoldierAirborn) obj;
								paratrooper.Die(Engine.actorLand());
							}

						};
					} else {
						st = 4;
						animStartTime = Time.current();
						EventLog.type("369");
						if (name().equals("_paraplayer_") && Mission.isNet()
								&& World.getPlayerFM() != null
								&& Actor.isValid(World.getPlayerAircraft())
								&& World.getPlayerAircraft().isNetPlayer()) {
							FlightModel flightmodel = World.getPlayerFM();
							if (flightmodel.isWasAirborne()
									&& flightmodel.isStationedOnGround()
									&& !flightmodel.isNearAirdrome())
								Chat.sendLogRnd(2, "gore_walkaway",
										World.getPlayerAircraft(), null);
							EventLog.type("245");
						}
					}
					pos.setAbs(faceOrient);
					Object aobj[] = getOwnerAttached();
					for (int i = 0; i < aobj.length; i++) {
						Chute chute = (Chute) aobj[i];
						if (Actor.isValid(chute))
							chute.landing();
					}

				}
				pos.setAbs(ParatrooperSoldierAirborn.p);
				break;

			case 4: // '\004'
				pos.getAbs(ParatrooperSoldierAirborn.p);
				ParatrooperSoldierAirborn.p.scaleAdd(Time.tickLenFs(), speed,
						ParatrooperSoldierAirborn.p);
				ParatrooperSoldierAirborn.p.z = Engine.land().HQ(ParatrooperSoldierAirborn.p.x,
						ParatrooperSoldierAirborn.p.y);
				pos.setAbs(ParatrooperSoldierAirborn.p);

				if (World.land()
						.isWater(ParatrooperSoldierAirborn.p.x, ParatrooperSoldierAirborn.p.y)) {
					if (swimMeshCode < 0) {
						st = 5;
						animStartTime = Time.current();
					} else {
						setMesh(ParatrooperSoldierAirborn.GetMeshName_Water(swimMeshCode));
						pos.getAbs(ParatrooperSoldierAirborn.p);
						ParatrooperSoldierAirborn.p.z = Engine.land().HQ(
								ParatrooperSoldierAirborn.p.x, ParatrooperSoldierAirborn.p.y);
						pos.setAbs(ParatrooperSoldierAirborn.p);
						st = 8;
						animStartTime = Time.current();
						disappearTime = Time.tickNext()
								+ (long) (1000 * World.Rnd().nextInt(25, 35));
					}
					break;
				}
				if (l / 733L >= (long) nRunCycles) {

					// TODO: Insert
					// st = 5;
					animStartTime = Time.current();
					EventLog.type("419");

					class1 = com.maddox.il2.objects.vehicles.artillery.ArtilleryWasted.SoldierAirborn.class;
					spawn = (ActorSpawn) Spawn.get(class1.getName());

					insert(spawn, new Loc(pos.getAbsPoint().x, pos.getAbsPoint().y, ((Tuple3d) (pos.getAbsPoint())).z, faceOrient.getYaw() + World.cur().rnd.nextFloat(-20F, 20F), 0.0F, 0.0F), army);
					if (getOwner() == World.getPlayerAircraft())
					{
						World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 15D, false));
					}
					postDestroy();

				}
				break;

			case 5: // '\005'
				pos.getAbs(ParatrooperSoldierAirborn.p);
				ParatrooperSoldierAirborn.p.scaleAdd(Time.tickLenFs(), speed,
						ParatrooperSoldierAirborn.p);
				ParatrooperSoldierAirborn.p.z = Engine.land().HQ(ParatrooperSoldierAirborn.p.x,
						ParatrooperSoldierAirborn.p.y);
				if (World.land()
						.isWater(ParatrooperSoldierAirborn.p.x, ParatrooperSoldierAirborn.p.y))
					if (swimMeshCode < 0) {
						ParatrooperSoldierAirborn.p.z -= 0.5D;
					} else {
						setMesh(ParatrooperSoldierAirborn.GetMeshName_Water(swimMeshCode));
						pos.getAbs(ParatrooperSoldierAirborn.p);
						ParatrooperSoldierAirborn.p.z = Engine.land().HQ(
								ParatrooperSoldierAirborn.p.x, ParatrooperSoldierAirborn.p.y);
						pos.setAbs(ParatrooperSoldierAirborn.p);
						st = 8;
						animStartTime = Time.current();
						disappearTime = Time.tickNext()
								+ (long) (1000 * World.Rnd().nextInt(25, 35));
						break;
					}
				pos.setAbs(ParatrooperSoldierAirborn.p);
				if (l >= 1066L) {
					st = 6;
					animStartTime = Time.current();
					disappearTime = Time.tickNext()
							+ (long) (1000 * World.Rnd().nextInt(25, 35));
					EventLog.type("449");
				}
				break;

			case 6: // '\006'
			case 7: // '\007'
				pos.getAbs(ParatrooperSoldierAirborn.p);
				ParatrooperSoldierAirborn.p.z = Engine.land().HQ(ParatrooperSoldierAirborn.p.x,
						ParatrooperSoldierAirborn.p.y);
				if (World.land()
						.isWater(ParatrooperSoldierAirborn.p.x, ParatrooperSoldierAirborn.p.y))
					ParatrooperSoldierAirborn.p.z -= 3D;
				pos.setAbs(ParatrooperSoldierAirborn.p);
				break;
			}
			setAnimFrame(Time.tickNext());
			return true;
		}

		Move() {
		}
	}

	private class SoldDraw extends ActorMeshDraw {

		public int preRender(Actor actor) {
			setAnimFrame(Time.current());
			return super.preRender(actor);
		}

		private SoldDraw() {
		}

	}

	public boolean isChuteSafelyOpened() {
		return st == 2 || st == 6 || st == 8 || st == 9;
	}

	public static void resetGame() {
		_counter = 0;
		preload1 = preload2 = preload3 = null;
	}

	public static void PRELOAD() {
		preload1 = new Mesh(GetMeshName(1));
		preload2 = new Mesh(GetMeshName(2));
		preload3 = new Mesh(Chute.GetMeshName());
		preload4 = new Mesh(GetMeshName_Water(0));
		preload5 = new Mesh(GetMeshName_Water(1));
		preload6 = new Mesh(GetMeshName_Water(2));
	}

	public void msgCollisionRequest(Actor actor, boolean aflag[]) {
		if ((actor instanceof Aircraft) && actor.isNet() && actor.isNetMirror())
			aflag[0] = false;
		if ((actor == getOwner() || getOwner() == null)
				&& Time.current() - animStartTime < 2800L)
			aflag[0] = false;
		if (dying != 0
				&& (actor == null || !(actor instanceof ShipGeneric)
						&& !(actor instanceof BigshipGeneric)))
			aflag[0] = false;
	}

	public void msgCollision(Actor actor, String s, String s1) {
		if (st == 9)
			return;
		if (dying != 0) {
			if (actor != null
					&& ((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric)))
				st = 9;
			return;
		}
		if (actor != null
				&& ((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric))) {
			boolean flag = Math.abs(speed.z) > 10D;
			if (flag)
				Die(actor);
			st = 9;
			return;
		}
		Point3d point3d = p;
		pos.getAbs(p);
		Point3d point3d1 = actor.pos.getAbsPoint();
		Vector3d vector3d = new Vector3d();
		vector3d.set(point3d.x - point3d1.x, point3d.y - point3d1.y, 0.0D);
		if (vector3d.length() < 0.001D) {
			float f = World.Rnd().nextFloat(0.0F, 359.99F);
			vector3d.set(Geom.sinDeg(f), Geom.cosDeg(f), 0.0D);
		}
		vector3d.normalize();
		float f1 = 0.2F;
		vector3d.add(World.Rnd().nextFloat(-f1, f1),
				World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1));
		vector3d.normalize();
		float f2 = 13.09091F * Time.tickLenFs();
		vector3d.scale(f2);
		speed.z *= 0.5D;
		point3d.add(vector3d);
		pos.setAbs(point3d);
		if (st == 4) {
			st = 5;
			animStartTime = Time.current();
		}
		if (st == 6 && dying == 0 && (actor instanceof UnitInterface)
				&& actor.getSpeed(null) > 0.5D)
			Die(actor);
	}

	public void msgShot(Shot shot) {
		if (st == 9)
			return;
		shot.bodyMaterial = 3;
		if (dying != 0)
			return;
		if (shot.power <= 0.0F)
			return;
		if (shot.powerType == 1) {
			Die(shot.initiator);
			return;
		}
		if (shot.v.length() < 20D) {
			return;
		} else {
			Die(shot.initiator);
			return;
		}
	}

	public void msgExplosion(Explosion explosion) {
		if (st == 9)
			return;
		if (dying != 0)
			return;
		float f = 0.005F;
		float f1 = 0.1F;
		Explosion _tmp = explosion;
		if (Explosion.killable(this, explosion.receivedTNT_1meter(this), f, f1,
				0.0F))
			Die(explosion.initiator);
	}

	private void Die(Actor actor) {
		Die(actor, true);
	}

	private void Die(Actor actor, boolean flag) {
		if (dying != 0)
			return;
		World.onActorDied(this, actor);
		if (actor != this) {
			if (name().equals("_paraplayer_")) {
				World.setPlayerDead();
				if (Config.isUSE_RENDER())
					HUD.log("Player_Killed");
				if (Mission.isNet()) {
					if ((actor instanceof Aircraft)
							&& ((Aircraft) actor).isNetPlayer()
							&& Actor.isAlive(World.getPlayerAircraft()))
						Chat.sendLogRnd(1, "gore_pkonchute", (Aircraft) actor,
								World.getPlayerAircraft());
					Chat.sendLog(0, "gore_killed", (NetUser) NetEnv.host(),
							(NetUser) NetEnv.host());
				}
			}
			if (logAircraftName != null
					&& (driver == null && isNetMaster() || driver != null
							&& driver.isMaster()))
				if (Actor.isValid(actor) && actor != Engine.actorLand())
					EventLog.onParaKilled(this, logAircraftName,
							idxOfPilotPlace, actor);
				else
					EventLog.onPilotKilled(this, logAircraftName,
							idxOfPilotPlace);
		}
		dying = 1;
		if (isNet() && flag)
			try {
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(68);
				if (actor != null)
					netmsgguaranted.writeNetObj(actor.net);
				else
					netmsgguaranted.writeNetObj(null);
				net.postExclude(null, netmsgguaranted);
			} catch (Exception exception) {
			}
	}

	public void destroy() {
		Object aobj[] = getOwnerAttached();
		for (int i = 0; i < aobj.length; i++) {
			Chute chute = (Chute) aobj[i];
			if (Actor.isValid(chute))
				chute.destroy();
		}

		if (Mission.isPlaying() && World.cur() != null && driver != null
				&& (driver.isMaster() || driver.isTrackWriter()))
			World.cur().checkViewOnPlayerDied(this);
		super.destroy();
	}

	public Object getSwitchListener(Message message) {
		return this;
	}

	void chuteTangled(Actor actor, boolean flag) {
		if (st == 1 || st == 2) {
			st = 3;
			animStartTime = Time.current();
			pos.setAbs(faceOrient);
			if (logAircraftName != null
					&& (driver == null && isNetMaster() || driver != null
							&& driver.isMaster()))
				EventLog.onChuteKilled(this, logAircraftName, idxOfPilotPlace,
						actor);
			if (isNet() && flag)
				try {
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(83);
					if (actor != null)
						netmsgguaranted.writeNetObj(actor.net);
					else
						netmsgguaranted.writeNetObj(null);
					net.postExclude(null, netmsgguaranted);
				} catch (Exception exception) {
				}
		}
	}

	void setAnimFrame(double d) {
		int i;
		int j;
		float f;
		switch (st) {
		case 0: // '\0'
			i = 0;
			j = 19;
			int k = 633;
			double d1 = d - (double) animStartTime;
			if (d1 <= 0.0D)
				f = 0.0F;
			else if (d1 >= (double) k)
				f = 1.0F;
			else
				f = (float) (d1 / (double) k);
			if (f >= 1.0F && dying != 0) {
				i = j = 134;
				f = 0.0F;
			}
			break;

		case 1: // '\001'
			i = 19;
			j = 34;
			int l = 500;
			double d2 = d - (double) animStartTime;
			if (d2 <= 0.0D) {
				f = 0.0F;
				break;
			}
			if (d2 >= (double) l)
				f = 1.0F;
			else
				f = (float) (d2 / (double) l);
			break;

		case 2: // '\002'
		case 3: // '\003'
			i = 34;
			j = 54;
			int i1 = 666;
			double d3 = d - (double) animStartTime;
			if (d3 <= 0.0D)
				f = 0.0F;
			else if (d3 >= (double) i1)
				f = 1.0F;
			else
				f = (float) (d3 / (double) i1);
			if (f >= 1.0F && dying != 0) {
				i = j = 133;
				f = 0.0F;
			}
			break;

		case 4: // '\004'
			i = 55;
			j = 77;
			int j1 = 733;
			double d4 = d - (double) animStartTime;
			d4 %= j1;
			if (d4 < 0.0D)
				d4 += j1;
			f = (float) (d4 / (double) j1);
			break;

		case 5: // '\005'
			i = 77;
			j = 109;
			int k1 = 1066;
			double d5 = d - (double) animStartTime;
			if (d5 <= 0.0D) {
				f = 0.0F;
				break;
			}
			if (d5 >= (double) k1)
				f = 1.0F;
			else
				f = (float) (d5 / (double) k1);
			break;

		case 6: // '\006'
			i = 109;
			j = 128;
			int l1 = 633;
			double d6 = d - (double) animStartTime;
			if (d6 <= 0.0D) {
				f = 0.0F;
				break;
			}
			if (d6 >= (double) l1)
				f = 1.0F;
			else
				f = (float) (d6 / (double) l1);
			break;

		case 8: // '\b'
			return;

		case 9: // '\t'
			return;

		case 7: // '\007'
		default:
			i = j = 129 + idxOfDeadPose;
			f = 0.0F;
			break;
		}
		mesh().setFrameFromRange(i, j, f);
	}

	public int HitbyMask() {
		return -25;
	}

	public int chooseBulletType(BulletProperties abulletproperties[]) {
		if (dying != 0)
			return -1;
		if (abulletproperties.length == 1)
			return 0;
		if (abulletproperties.length <= 0)
			return -1;
		if (abulletproperties[0].power <= 0.0F)
			return 1;
		if (abulletproperties[0].powerType == 1)
			return 0;
		if (abulletproperties[1].powerType == 1)
			return 1;
		if (abulletproperties[0].cumulativePower > 0.0F)
			return 1;
		return abulletproperties[0].powerType != 2 ? 0 : 1;
	}

	public int chooseShotpoint(BulletProperties bulletproperties) {
		return dying == 0 ? 0 : -1;
	}

	public boolean getShotpointOffset(int i, Point3d point3d) {
		if (dying != 0)
			return false;
		if (i != 0)
			return false;
		if (point3d != null)
			point3d.set(0.0D, 0.0D, 0.0D);
		return true;
	}

	private static String GetMeshName(int i) {
		return "3do/humans/Paratroopers/" + (i != 2 ? "Russia" : "Russia")
				+ "/mono.sim";
	}

	private static String GetMeshName_Water(int i) {
		return "3do/humans/Paratroopers/Water/"
				+ (i != 0 ? i != 1 ? "US_Dinghy" : "US_Jacket" : "JN_Jacket")
				+ "/live.sim";
	}

	public void prepareSkin(String s, String s1, Mat amat[]) {
		if (!Config.isUSE_RENDER())
			return;
		String s2 = "Pilot";
		int i = mesh().materialFind(s2);
		if (i < 0)
			return;
		Mat mat;
		if (FObj.Exist(s)) {
			mat = (Mat) FObj.Get(s);
		} else {
			Mat mat1 = mesh().material(i);
			mat = (Mat) mat1.Clone();
			mat.Rename(s);
			mat.setLayer(0);
			mat.set('\0', s1);
		}
		if (amat != null)
			amat[0] = mat;
		mesh().materialReplace(s2, mat);
	}

	public ParatrooperSoldierAirborn(Actor actor, int i, int j, Loc loc,
			Vector3d vector3d, NetMsgInput netmsginput, int k) {
		super(GetMeshName(i));
		logAircraftName = null;
		swimMeshCode = -1;
		st = 0;
		dying = 0;
		bCheksCaptured = false;
		if (Config.isUSE_RENDER() && (actor instanceof Aircraft)) {
			Aircraft aircraft = (Aircraft) actor;
			String s = "Pilot1";
			if (j > 0)
				s = "Pilot2";
			int l = aircraft.hierMesh().materialFind(s);
			if (l >= 0) {
				Mat mat = aircraft.hierMesh().material(l);
				mesh().materialReplace("pilot", mat);
			} else if (j > 0) {
				int i1 = aircraft.hierMesh().materialFind("Pilot1");
				if (i1 >= 0) {
					Mat mat1 = aircraft.hierMesh().material(i1);
					mesh().materialReplace("pilot", mat1);
				}
			}
		}
		startOrient = new Orient();
		loc.get(startOrient);
		faceOrient = new Orient();
		faceOrient.set(startOrient);
		faceOrient.setYPR(faceOrient.getYaw() + World.cur().rnd.nextFloat(0F, 40F), 0.0F, 0.0F);
		Vector3d vector3d1 = new Vector3d();
		vector3d1.set(1.0D, 0.0D, 0.0D);
		faceOrient.transform(vector3d1);
		speed = new Vector3d();
		speed.set(vector3d);
		setOwner(actor);
		idxOfPilotPlace = j;
		setArmy(i);
		army = i;
		swimMeshCode = -1;
		if (Actor.isValid(actor) && (actor instanceof Aircraft)) {
			String s1 = ((Aircraft) actor).getRegiment().country();
			if ("us".equals(s1) || "gb".equals(s1))
				swimMeshCode = j != 0 ? 1 : 2;
			else if ("ja".equals(s1))
				swimMeshCode = 0;
		}
		o.setAT0(speed);
		o.set(o.azimut(), 0.0F, 0.0F);
		pos.setAbs(loc);
		pos.reset();
		st = 0;
		animStartTime = Time.tick();
		dying = 0;
		setName("_para_" + _counter++);
		collide(true);
		draw = new SoldDraw();
		dreamFire(true);
		drawing(true);
		if (!interpEnd("move"))
			interpPut(new Move(), "move", Time.current(), null);
		if (Actor.isValid(actor))
			logAircraftName = EventLog.name(actor);
		if (netmsginput == null)
			net = new Master(this);
		else
			net = new Mirror(this, netmsginput, k);

		spawnArg = new ActorSpawnArg();

	}

	public ParatrooperSoldierAirborn(Actor actor, int i, int j, Loc loc, Vector3d vector3d) {
		this(actor, i, j, loc, vector3d, null, 0);
	}

	public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException {
		NetMsgSpawn netmsgspawn = new NetMsgSpawn(net);
		Point3d point3d = pos.getAbsPoint();
		netmsgspawn.writeFloat((float) point3d.x);
		netmsgspawn.writeFloat((float) point3d.y);
		netmsgspawn.writeFloat((float) point3d.z);
		Orient orient = pos.getAbsOrient();
		netmsgspawn.writeFloat(orient.getAzimut());
		netmsgspawn.writeFloat(orient.getTangage());
		netmsgspawn.writeFloat(orient.getKren());
		netmsgspawn.writeFloat((float) speed.x);
		netmsgspawn.writeFloat((float) speed.y);
		netmsgspawn.writeFloat((float) speed.z);
		netmsgspawn.writeByte(getArmy());
		if (getOwner() != null && netchannel != null
				&& netchannel.isMirrored(getOwner().net))
			netmsgspawn.writeNetObj(getOwner().net);
		else
			netmsgspawn.writeNetObj(null);
		netmsgspawn.writeByte(idxOfPilotPlace);
		netmsgspawn.writeFloat(turn_para_on_height);
		netmsgspawn.writeByte(nRunCycles);
		netmsgspawn.writeNetObj(driver);
		return netmsgspawn;
	}

	public int army;
	public Class class1;
	public ActorSpawn spawn;
	private ActorSpawnArg spawnArg;

	private static final int FPS = 30;
	private static final int FREEFLY_START_FRAME = 0;
	private static final int FREEFLY_LAST_FRAME = 19;
	private static final int FREEFLY_N_FRAMES = 20;
	private static final int FREEFLY_CYCLE_TIME = 633;
	private static final int FREEFLY_ROT_TIME = 2500;
	private static final int PARAUP1_START_FRAME = 19;
	private static final int PARAUP1_LAST_FRAME = 34;
	private static final int PARAUP1_N_FRAMES = 16;
	private static final int PARAUP1_CYCLE_TIME = 500;
	private static final int PARAUP2_START_FRAME = 34;
	private static final int PARAUP2_LAST_FRAME = 54;
	private static final int PARAUP2_N_FRAMES = 21;
	private static final int PARAUP2_CYCLE_TIME = 666;
	private static final int RUN_START_FRAME = 55;
	private static final int RUN_LAST_FRAME = 77;
	private static final int RUN_N_FRAMES = 23;
	private static final int RUN_CYCLE_TIME = 733;
	private static final int FALL_START_FRAME = 77;
	private static final int FALL_LAST_FRAME = 109;
	private static final int FALL_N_FRAMES = 33;
	private static final int FALL_CYCLE_TIME = 1066;
	private static final int LIE_START_FRAME = 109;
	private static final int LIE_LAST_FRAME = 128;
	private static final int LIE_N_FRAMES = 20;
	private static final int LIE_CYCLE_TIME = 633;
	private static final int LIEDEAD_START_FRAME = 129;
	private static final int LIEDEAD_N_FRAMES = 4;
	private static final int PARADEAD_FRAME = 133;
	private static final int FREEFLYDEAD_FRAME = 134;
	private static final float FREE_SPEED = 50F;
	private static final float PARA_SPEED = 5F;
	private static final float RUN_SPEED = 6.545455F;
	public static final String playerParaName = "_paraplayer_";
	private String logAircraftName;
	private int idxOfPilotPlace;
	private NetUser driver;
	private int swimMeshCode;
	private Vector3d speed;
	private Orient startOrient;
	private Orient faceOrient;
	private static final int ST_FREEFLY = 0;
	private static final int ST_PARAUP1 = 1;
	private static final int ST_PARAUP2 = 2;
	private static final int ST_PARATANGLED = 3;
	private static final int ST_RUN = 4;
	private static final int ST_FALL = 5;
	private static final int ST_LIE = 6;
	private static final int ST_LIEDEAD = 7;
	private static final int ST_SWIM = 8;
	private static final int ST_DISAPPEAR = 9;
	private int st;
	private int dying;
	static final int DYING_NONE = 0;
	static final int DYING_DEAD = 1;
	private int idxOfDeadPose;
	private long animStartTime;
	private long disappearTime;
	private int nRunCycles;
	private float turn_para_on_height;
	private static int _counter = 0;
	private static Mesh preload1 = null;
	private static Mesh preload2 = null;
	private static Mesh preload3 = null;
	private static Mesh preload4 = null;
	private static Mesh preload5 = null;
	private static Mesh preload6 = null;
	private static Point3d p = new Point3d();
	private static Orient o = new Orient();
	private static Vector3f n = new Vector3f();
	private boolean bCheksCaptured;

	static {
		Spawn.add(com.maddox.il2.objects.air.Paratrooper.class, new SPAWN());
	}

}
