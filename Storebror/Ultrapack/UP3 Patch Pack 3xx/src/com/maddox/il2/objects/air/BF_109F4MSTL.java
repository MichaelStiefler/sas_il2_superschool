package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.BombFAB5000;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class BF_109F4MSTL extends BF_109 implements TypeDockable, Mistel {

	public BF_109F4MSTL() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL ()");
		this.cockpitDoor_ = 0.0F;
		this.fMaxKMHSpeedForOpenCanopy = 250F;
		this.kangle = 0.0F;
		this.bHasBlister = true;
		this.bNeedSetup = true;
		this.dtime = -1L;
		this.target_ = null;
		this.queen_ = null;
		this.mistelQueen = null;
	}

	public Aircraft getDrone() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL getDrone()");
		return this;
	}

	public Aircraft getQueen() {
		if (!(this.mistelQueen instanceof Aircraft))
			return null;
		return (Aircraft) this.mistelQueen;
	}

	public void mistelExplosion() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL mistelExplosion()");
		if (this.mistelQueen == null) {
			if (DEBUG >= 1)
				System.out.println("BF_109F4MSTL mistelExplosion() mistelQueen=null");
			return;
		}
		if (this.mistelQueen.pos.getAbsPoint().z - Engine.cur.land.HQ(this.mistelQueen.pos.getAbsPoint().x,
				this.mistelQueen.pos.getAbsPoint().y) > 50D) {
			if (DEBUG >= 1)
				System.out.println("BF_109F4MSTL mistelExplosion() Altitude above land > 50m");
			MsgExplosion.send(this.mistelQueen, null, this.mistelQueen.pos.getAbsPoint(), this, 0.0F, 6550.0F, 0,
					890.0F);
			return;
		}
		if (DEBUG >= 1)
			System.out.println("BF_109F4MSTL mistelExplosion() spawning BombFAB5000");
		BombFAB5000 bomb = new BombFAB5000();
		bomb.pos.setUpdateEnable(true);
		bomb.pos.setAbs(this.mistelQueen.pos.getAbs());
		bomb.pos.reset();
		bomb.start();
		bomb.setOwner(this);
		bomb.setSpeed(new Vector3d());
		bomb.armingTime = 0L;
		Reflection.setBoolean(bomb, "isArmed", true);
	}

	protected void finalize() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL finalize()");
		super.finalize();
		NetMistel.removeNetMistelFromList(this);
	}

	protected void moveFan(float f) {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL moveFan(" + f + ")");
		int i = 0;
		for (int j = 0; j < 1; j++) {
			if (super.oldProp[j] < 2) {
				i = Math.abs((int) (this.FM.EI.engines[j].getw() * 0.12F * 1.5F));
				if (i >= 1)
					i = 1;
				if (i != super.oldProp[j]) {
					this.hierMesh().chunkVisible(Aircraft.Props[j][super.oldProp[j]], false);
					super.oldProp[j] = i;
					this.hierMesh().chunkVisible(Aircraft.Props[j][i], true);
				}
			}
			if (i == 0) {
				super.propPos[j] = (super.propPos[j] + 57.3F * this.FM.EI.engines[j].getw() * f) % 360F;
			} else {
				float f1 = 57.3F * this.FM.EI.engines[j].getw();
				f1 %= 2880F;
				f1 /= 2880F;
				if (f1 <= 0.5F)
					f1 *= 2.0F;
				else
					f1 = f1 * 2.0F - 2.0F;
				f1 *= 1200F;
				super.propPos[j] = (super.propPos[j] + f1 * f) % 360F;
			}
			this.hierMesh().chunkSetAngles(Aircraft.Props[j][0], 0.0F, -super.propPos[j], 0.0F);
		}

	}

	public static void moveGear(HierMesh hiermesh, float f) {
		if (DEBUG_METHODS)
			System.out.println("BF_109F4MSTL moveGear(" + hiermesh.hashCode() + ", " + f + ")");
		float f1 = 0.8F;
		float f2 = -0.5F * (float) Math.cos(f / f1 * 3.1415926535897931D) + 0.5F;
		if (f <= f1 || f == 1.0F) {
			hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f2, 0.0F);
			hiermesh.chunkSetAngles("GearL2_D0", -33.5F * f2, 0.0F, 0.0F);
		}
		f2 = -0.5F * (float) Math.cos((f - (1.0F - f1)) / f1 * 3.1415926535897931D) + 0.5F;
		if (f >= 1.0F - f1) {
			hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f2, 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 33.5F * f2, 0.0F, 0.0F);
		}
		hiermesh.chunkSetAngles("GearC3_D0", 70F * f, 0.0F, 0.0F);
		if (f > 0.99F) {
			hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F, 0.0F);
			hiermesh.chunkSetAngles("GearL2_D0", -33.5F, 0.0F, 0.0F);
			hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F, 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 33.5F, 0.0F, 0.0F);
		}
		if (f < 0.01F) {
			hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 0.0F);
			hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 0.0F);
			hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
		}
	}

	protected void moveGear(float f) {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL moveGear(" + f + ")");
		if (this.typeDockableIsDocked())
			moveGear(this.hierMesh(), 0.0F);
		else
			moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL moveSteering(" + f + ")");
		if (this.FM.CT.getGear() >= 0.98F)
			this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
	}

	public void update(float f) {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL update(" + f + ")");
		if (this.FM.getSpeed() > 5F) {
			this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F),
					0.0F);
			this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F),
					0.0F);
		}
		this.hierMesh().chunkSetAngles("Flap01L_D0", 0.0F, -16F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Flap01U_D0", 0.0F, 16F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02L_D0", 0.0F, -16F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02U_D0", 0.0F, 16F * this.kangle, 0.0F);
		this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
		if (this.kangle > 1.0F)
			this.kangle = 1.0F;
		super.update(f);
		if (this.FM.CT.getCockpitDoor() > 0.2D && this.bHasBlister
				&& this.FM.getSpeedKMH() > this.fMaxKMHSpeedForOpenCanopy
				&& this.hierMesh().chunkFindCheck("Blister1_D0") != -1) {
			try {
				if (this == World.getPlayerAircraft())
					((CockpitBF_109F2) Main3D.cur3D().cockpitCur).removeCanopy();
			} catch (Exception exception) {
			}
			this.hierMesh().hideSubTrees("Blister1_D0");
			Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(this.FM.Vwld);
			wreckage.setSpeed(vector3d);
			this.bHasBlister = false;
			this.FM.CT.bHasCockpitDoorControl = false;
			this.FM.setGCenter(-0.5F);
		}
		if (this.FM.isPlayers())
			if (!Main3D.cur3D().isViewOutside())
				this.hierMesh().chunkVisible("CF_D0", false);
			else
				this.hierMesh().chunkVisible("CF_D0", true);
		if (this.FM.isPlayers()) {
			if (!Main3D.cur3D().isViewOutside())
				this.hierMesh().chunkVisible("CF_D1", false);
			this.hierMesh().chunkVisible("CF_D2", false);
			this.hierMesh().chunkVisible("CF_D3", false);
		}
		if (this.FM.isPlayers()) {
			if (!Main3D.cur3D().isViewOutside())
				this.hierMesh().chunkVisible("Blister1_D0", false);
			else if (this.bHasBlister)
				this.hierMesh().chunkVisible("Blister1_D0", true);
			com.maddox.JGP.Point3d point3d = World.getPlayerAircraft().pos.getAbsPoint();
			if (point3d.z - World.land().HQ(point3d.x, point3d.y) < 0.01D)
				this.hierMesh().chunkVisible("CF_D0", true);
			if (this.FM.AS.bIsAboutToBailout)
				this.hierMesh().chunkVisible("Blister1_D0", false);
		}
		if (this.bNeedSetup)
			this.checkAsDrone();
		if (this.FM instanceof Maneuver)
			if (this.typeDockableIsDocked()) {
				if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
					((Maneuver) this.FM).set_maneuver(48);
					((FlightModelMain) (this.FM)).AP.way.setCur(((Aircraft) this.queen_).FM.AP.way.Cur());
					((Pilot) this.FM).setDumbTime(3000L);
				}
			} else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode())
				if (this.dtime > 0L) {
					((Maneuver) this.FM).set_maneuver(22);
					((Pilot) this.FM).setDumbTime(3000L);
					if (Time.current() > this.dtime + 3000L) {
						this.dtime = -1L;
						((Maneuver) this.FM).clear_stack();
						((Maneuver) this.FM).pop();
						((Pilot) this.FM).setDumbTime(0L);
					}
				} else if (this.FM.AP.way.curr().Action == 3 && ((Maneuver) this.FM).get_maneuver() == 24) {
					((Maneuver) this.FM).set_maneuver(21);
					((Pilot) this.FM).setDumbTime(30000L);
				}

		if (this.typeDockableIsDocked()) {
			Aircraft aircraft = (Aircraft) this.typeDockableGetQueen();
			NetMistel.mistelToMirrors(this);
			if (!aircraft.isNetMirror()) {
				aircraft.FM.CT.AileronControl = this.FM.CT.AileronControl;
				aircraft.FM.CT.ElevatorControl = this.FM.CT.ElevatorControl;
				aircraft.FM.CT.RudderControl = this.FM.CT.RudderControl;
				aircraft.FM.CT.setTrimAileronControl(this.FM.CT.getTrimAileronControl());
				aircraft.FM.CT.setTrimElevatorControl(this.FM.CT.getTrimElevatorControl());
				aircraft.FM.CT.setTrimRudderControl(this.FM.CT.getTrimRudderControl());
			}
			aircraft.FM.CT.GearControl = this.FM.CT.GearControl;
			aircraft.FM.CT.FlapsControl = this.FM.CT.FlapsControl;
			aircraft.FM.CT.forceFlaps(this.FM.CT.getFlap());
			this.moveSteering(0.0F);
			this.FM.canChangeBrakeShoe = (aircraft.FM.Gears.onGround() && this.FM.getSpeedKMH() < 10F);
			if (aircraft.FM.Gears.onGround()) {
				this.FM.Loc.z = Engine.cur.land.HQ(this.FM.Loc.x, this.FM.Loc.y) + 51D;
			}
		}
		super.update(f);
		if (this.FM.CT.saveWeaponControl[3] || this.FM.CT.WeaponControl[3])
			this.typeDockableAttemptDetach();
	}

	public void onAircraftLoaded() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL onAircraftLoaded()");
		super.onAircraftLoaded();
		if (Mission.isCoop() && !Mission.isServer() && !this.isSpawnFromMission() && super.net.isMaster())
			new MsgAction(64, 0.0D, this) {
				public void doAction() {
					BF_109F4MSTL.this.onCoopMasterSpawned();
				}
			};
	}

	private void onCoopMasterSpawned() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL onCoopMasterSpawned()");
		Actor actor = null;
		if (this.FM.AP.way.curr().getTargetName() == null)
			this.FM.AP.way.next();
		String s = this.FM.AP.way.curr().getTargetName();
		if (s != null)
			actor = Actor.getByName(s);
		if (Actor.isValid(actor) && (actor instanceof Wing) && actor.getOwnerAttachedCount() > 0)
			actor = (Actor) actor.getOwnerAttached(0);
		this.FM.AP.way.setCur(0);
		if (Actor.isValid(actor) && (actor instanceof JU_88MSTL))
			try {
				Aircraft aircraft = (Aircraft) actor;
				float f = 100F;
				if (aircraft.FM.M.maxFuel > 0.0F)
					f = (aircraft.FM.M.fuel / aircraft.FM.M.maxFuel) * 100F;
				String s1 = "spawn " + actor.getClass().getName() + " NAME net" + actor.name() + " FUEL " + f
						+ " WEAPONS " + aircraft.thisWeaponsName + (aircraft.bPaintShemeNumberOn ? "" : " NUMBEROFF")
						+ " OVR";
				CmdEnv.top().exec(s1);
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
	}

	public void missionStarting() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL missionStarting()");
		this.checkAsDrone();
	}

	private void checkAsDrone() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL checkAsDrone()");
		if (this.target_ == null) {
			if (this.FM.AP.way.curr().getTargetActorRandom() == null)
				this.FM.AP.way.curr().getTargetActorRandom();
			this.target_ = this.FM.AP.way.curr().getTargetActorRandom();
			if (Actor.isValid(this.target_))
				this.target_ = this.FM.AP.way.curr().getTargetActorRandom();
		}
		if (Actor.isValid(this.target_) && (this.target_ instanceof JU_88MSTL) && this.isNetMaster())
			((TypeDockable) this.target_).typeDockableRequestAttach(this, 0, true);
		this.bNeedSetup = false;
		this.target_ = null;
	}

	public int typeDockableGetDockport() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL typeDockableGetDockport()");
		if (this.typeDockableIsDocked())
			return this.dockport_;
		else
			return -1;
	}

	public Actor typeDockableGetQueen() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL typeDockableGetQueen()");
		return this.queen_;
	}

	public boolean typeDockableIsDocked() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL typeDockableIsDocked()");
		return Actor.isValid(this.queen_);
	}

	public void typeDockableAttemptAttach() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL typeDockableAttemptAttach()");
		if (this.FM.AS.isMaster() && !this.typeDockableIsDocked()) {
			Aircraft aircraft = War.getNearestFriend(this);
			if (aircraft instanceof JU_88MSTL)
				((TypeDockable) aircraft).typeDockableRequestAttach(this);
		}
	}

	public void typeDockableAttemptDetach() {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL typeDockableAttemptDetach()");
		if (this.FM.AS.isMaster() && this.typeDockableIsDocked() && Actor.isValid(this.queen_))
			((TypeDockable) this.queen_).typeDockableRequestDetach(this);
	}

	public void typeDockableRequestAttach(Actor actor) {
	}

	public void typeDockableRequestDetach(Actor actor) {
	}

	public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
	}

	public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
	}

	public void typeDockableDoAttachToDrone(Actor actor, int i) {
	}

	public void typeDockableDoDetachFromDrone(int i) {
	}

	public void typeDockableDoAttachToQueen(Actor actor, int i) {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL typeDockableDoAttachToQueen(" + actor.hashCode() + ", " + i + ")");
		this.queen_ = actor;
		this.mistelQueen = actor;
		this.dockport_ = i;
		if (this.FM.EI.getNum() == 1) {
			if (DEBUG >= 2) {
				System.out.println("queen isNet=" + this.queen_.isNet());
				System.out.println("queen isNetMaster=" + this.queen_.isNetMaster());
				System.out.println("queen isNetMirror=" + this.queen_.isNetMirror());
				System.out.println("this isNet=" + this.isNet());
				System.out.println("this isNetMaster=" + this.isNetMaster());
				System.out.println("this isNetMirror=" + this.isNetMirror());
			}
			this.FM.Scheme = 6;
			Aircraft aircraft = (Aircraft) actor;
			this.FM.EI.setNum(3);
			Motor motor = this.FM.EI.engines[0];
			this.FM.EI.engines = new Motor[3];
			this.FM.EI.engines[0] = motor;
			this.FM.EI.engines[1] = aircraft.FM.EI.engines[0];
			this.FM.EI.engines[2] = aircraft.FM.EI.engines[1];
			this.FM.EI.bCurControl = (new boolean[] { true, true, true });
		}
		this.FM.EI.setEngineRunning();
		this.FM.CT.setGearAirborne();
		this.moveGear(0.0F);
		this.moveSteering(0.0F);
		this.FM.CT.GearControl = ((Aircraft) actor).FM.CT.GearControl;
		FlightModel flightmodel = ((Aircraft) this.queen_).FM;
		if ((this.FM instanceof Maneuver) && (flightmodel instanceof Maneuver)) {
			Maneuver maneuver = (Maneuver) flightmodel;
			Maneuver maneuver1 = (Maneuver) this.FM;
			if (maneuver.Group != null && maneuver1.Group != null
					&& maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1) {
				AirGroup airgroup = new AirGroup(maneuver1.Group);
				maneuver1.Group.delAircraft(this);
				airgroup.addAircraft(this);
				airgroup.attachGroup(maneuver.Group);
				airgroup.rejoinGroup = null;
			}
		}
		if (this.FM.CT.Weapons[3] == null) {
			this.FM.CT.Weapons[3] = new BulletEmitter[1];
		}
		this.FM.CT.Weapons[3][0] = new BombGunNull();
		this.FM.CT.Weapons[3][0].set(this, "_Clip00");
		this.FM.CT.Weapons[3][0].loadBullets(2);
	}

	public void typeDockableDoDetachFromQueen(int i) {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL typeDockableDoDetachFromQueen(" + i + ")");
		if (this.dockport_ == i) {
			this.queen_ = null;
			this.dockport_ = 0;
			this.FM.CT.setTrimElevatorControl(0.51F);
			this.FM.CT.trimElevator = 0.51F;
			this.FM.CT.setGearAirborne();
			if (this.FM.EI.getNum() == 3) {
				this.FM.Scheme = 1;
				this.FM.EI.setNum(1);
				Motor motor = this.FM.EI.engines[0];
				this.FM.EI.engines = new Motor[1];
				this.FM.EI.engines[0] = motor;
				this.FM.EI.bCurControl = (new boolean[] { true });
				for (int j = 1; j < 3; j++) {
					if (this.FM.Gears.clpEngineEff[j][0] != null) {
						Eff3DActor.finish(this.FM.Gears.clpEngineEff[j][0]);
						this.FM.Gears.clpEngineEff[j][0] = null;
					}
					if (this.FM.Gears.clpEngineEff[j][1] != null) {
						Eff3DActor.finish(this.FM.Gears.clpEngineEff[j][1]);
						this.FM.Gears.clpEngineEff[j][1] = null;
					}
				}

			}
		}
	}

	public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL typeDockableReplicateToNet(" + netmsgguaranted.hashCode() + ")");
		if (this.typeDockableIsDocked()) {
			netmsgguaranted.writeByte(1);
			ActorNet actornet = null;
			if (Actor.isValid(this.queen_)) {
				actornet = this.queen_.net;
				if (actornet.countNoMirrors() > 0)
					actornet = null;
			}
			netmsgguaranted.writeByte(this.dockport_);
			netmsgguaranted.writeNetObj(actornet);
		} else {
			netmsgguaranted.writeByte(0);
		}
	}

	public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL typeDockableReplicateFromNet(" + netmsginput.hashCode() + ")");
		if (netmsginput.readByte() == 1) {
			this.dockport_ = netmsginput.readByte();
			NetObj netobj = netmsginput.readNetObj();
			if (netobj != null) {
				Actor actor = (Actor) netobj.superObj();
				((TypeDockable) actor).typeDockableDoAttachToDrone(this, this.dockport_);
			}
		}
	}

	public void moveCockpitDoor(float f) {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL moveCockpitDoor(" + f + ")");
		if (this.bHasBlister) {
			this.resetYPRmodifier();
			this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
			if (Config.isUSE_RENDER()) {
				if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
					Main3D.cur3D().cockpits[0].onDoorMoved(f);
				this.setDoorSnd(f);
			}
		}
	}

	public boolean netGetGMsg(NetMsgInput netmsginput, boolean bool) throws IOException {
		if (Debug_Methods())
			System.out.println("BF_109F4MSTL netGetGMsg(" + netmsginput.hashCode() + ", " + bool + ")");
		if (!NetMistel.netGetGMsg(this, netmsginput, bool))
			return super.netGetGMsg(netmsginput, bool);
		return true;
	}

	private boolean Debug_Methods() {
		if (!DEBUG_METHODS)
			return false;
		if (this.getQueen() == null)
			return false;
		if (!(this.getQueen() instanceof JU_88MSTL))
			return false;
		if (!((JU_88MSTL) this.getQueen()).wasInCutFM)
			return false;
		return true;
	}

	public float cockpitDoor_;
	private float fMaxKMHSpeedForOpenCanopy;
	private float kangle;
	public boolean bHasBlister;
	private boolean bNeedSetup;
	private long dtime;
	private Actor target_;
	private Actor queen_;
	private int dockport_;
	private Actor mistelQueen;
	private static int DEBUG = 0;
	private static boolean DEBUG_METHODS = false;

	static {
		Class class1 = BF_109F4MSTL.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Bf109");
		Property.set(class1, "meshName", "3DO/Plane/Bf-109F-4/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
		Property.set(class1, "meshName_sk", "3DO/Plane/Bf-109F-4(sk)/hier.him");
		Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar03());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1944.5F);
		Property.set(class1, "FlightModel", "FlightModels/Bf-109F-4_Mod.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109F2.class });
		Property.set(class1, "LOSElevation", 0.74205F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01" });
	}

}
