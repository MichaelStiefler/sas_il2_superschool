package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.weapons.BombFAB2000;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class MXY_7 extends Scheme2a implements MsgCollisionRequestListener, TypeDockable {

	public MXY_7() {
		bNeedSetup = true;
		dtime = -1L;
		queen_last = null;
		queen_time = 0L;
		target_ = null;
		queen_ = null;
		charge_armed = false;
	}

	public void msgCollisionRequest(Actor actor, boolean aflag[]) {
		super.msgCollisionRequest(actor, aflag);
		if (queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
			aflag[0] = false;
		else
			aflag[0] = true;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (Config.isUSE_RENDER()) {
			for (int i = 0; i < 3; i++) {
				flame[i] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F,
						"3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
				dust[i] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F,
						"3DO/Effects/Aircraft/TurboJRD1100D.eff", -1F);
				trail[i] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F,
						"3DO/Effects/Aircraft/TurboJRD1100T.eff", -1F);
				sprite[i] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F,
						"3DO/Effects/Aircraft/TurboJRD1100S.eff", -1F);
				Eff3DActor.setIntesity(flame[i], 0.0F);
				Eff3DActor.setIntesity(dust[i], 0.0F);
				Eff3DActor.setIntesity(trail[i], 0.0F);
				Eff3DActor.setIntesity(sprite[i], 0.0F);
			}

		}
		charge_armed = false;
	}

	public void doMurderPilot(int i) {
		if (i != 0) {
			return;
		} else {
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			hierMesh().chunkVisible("HMask1_D0", false);
			return;
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xcf"))
			hitChunk("CF", shot);
		else if (s.startsWith("xtail")) {
			if (chunkDamageVisible("Tail1") < 1)
				hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel1"))
			hitChunk("Keel1", shot);
		else if (s.startsWith("xkeel2"))
			hitChunk("Keel2", shot);
		else if (s.startsWith("xrudder1"))
			hitChunk("Rudder1", shot);
		else if (s.startsWith("xrudder2"))
			hitChunk("Rudder2", shot);
		else if (s.startsWith("xstabl"))
			hitChunk("StabL", shot);
		else if (s.startsWith("xvator"))
			hitChunk("VatorL", shot);
		else if (s.startsWith("xwing")) {
			if (s.startsWith("xwinglin"))
				hitChunk("WingLIn", shot);
			if (s.startsWith("xwingrin"))
				hitChunk("WingRIn", shot);
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int i;
			if (s.endsWith("a")) {
				byte0 = 1;
				i = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				i = s.charAt(6) - 49;
			} else {
				i = s.charAt(5) - 49;
			}
			hitFlesh(i, shot, byte0);
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 3:
		case 19:
			FM.AS.setEngineDies(this, 0);
			return false;
		}
		return super.cutFM(i, j, actor);
	}

	protected void moveElevator(float f) {
		hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
	}

	public void msgEndAction(Object obj, int i) {
		super.msgEndAction(obj, i);
		switch (i) {
		case 2:
			Actor actor = null;
			if (Actor.isValid(queen_last))
				actor = queen_last;
			else
				actor = Engine.cur.actorLand;
			MsgExplosion.send(this, null, FM.Loc, actor, 0.0F, charge_armed ? 1200F : 120F, 0,
					charge_armed ? 800F : 80F);
			if (!charge_armed)
				break;
			// System.out.println("Ohka Bomb Spawning!");
			BombFAB2000 bomb = new BombFAB2000();
			bomb.pos.setUpdateEnable(true);
			bomb.pos.setAbs(this.pos.getAbs());
			bomb.pos.reset();
			bomb.start();
			bomb.setOwner(Actor.isValid(queen_last) ? queen_last : this);
			bomb.setSpeed(new Vector3d());
			bomb.armingTime = 0L;
			Reflection.setBoolean(bomb, "isArmed", true);
			break;
		}
	}

	protected void doExplosion() {
		super.doExplosion();
		if (FM.Loc.z - 300D < Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y))
			if (Engine.land().isWater(FM.Loc.x, FM.Loc.y)) {
				if (charge_armed)
					Explosions.BOMB1000a_Water(FM.Loc, 1.0F, 1.0F);
				else
					Explosions.BOMB250_Water(FM.Loc, 1.0F, 1.0F);
			} else {
				if (charge_armed)
					Explosions.BOMB1000a_Land(FM.Loc, 1.0F, 1.0F, true);
				else
					Explosions.BOMB250_Land(FM.Loc, 1.0F, 1.0F, true);
			}
	}

	public void update(float f) {
		if (bNeedSetup)
			checkAsDrone();
		if (FM instanceof Maneuver)
			if (typeDockableIsDocked()) {
				if (!(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) {
					((Maneuver) FM).unblock();
					((Maneuver) FM).set_maneuver(48);
					((Maneuver) FM).AP.way.setCur(((Aircraft) queen_).FM.AP.way.Cur());
					((Pilot) FM).setDumbTime(3000L);
				}
			} else if (!(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) {
				if (FM.EI.engines[0].getStage() == 0)
					FM.EI.setEngineRunning();
				if (dtime > 0L) {
					((Maneuver) FM).setBusy(false);
					((Maneuver) FM).Group.leaderGroup = null;
					((Maneuver) FM).set_maneuver(22);
					((Pilot) FM).setDumbTime(3000L);
					if (Time.current() > dtime + 3000L) {
						dtime = -1L;
						((Maneuver) FM).clear_stack();
						((Maneuver) FM).pop();
						((Pilot) FM).setDumbTime(0L);
					}
				}
			}
		super.update(f);
		if (FM.AS.isMaster()) {
			for (int i = 0; i < 3; i++) {
				if (FM.CT.PowerControl > 0.77F && FM.EI.engines[i].getStage() == 0 && FM.M.fuel > 0.0F
						&& !typeDockableIsDocked())
					FM.EI.engines[i].setStage(this, 6);
				if (FM.CT.PowerControl < 0.77F && FM.EI.engines[i].getStage() > 0 || FM.M.fuel == 0.0F)
					FM.EI.engines[i].setEngineStops(this);
			}

			if (Config.isUSE_RENDER()) {
				for (int j = 0; j < 3; j++)
					if (FM.EI.engines[j].getw() > 50F && FM.EI.engines[j].getStage() == 6)
						FM.AS.setSootState(this, j, 1);
					else
						FM.AS.setSootState(this, j, 0);

			}
		}
		if (FM.getSpeedKMH() > 200F)
			charge_armed = true;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag && FM.AP.way.curr().Action == 3 && typeDockableIsDocked()
				&& Math.abs(((Aircraft) queen_).FM.Or.getKren()) < 3F)
			if (FM.isPlayers()) {
				if ((FM instanceof RealFlightModel) && !((RealFlightModel) FM).isRealMode()) {
					typeDockableAttemptDetach();
					((Maneuver) FM).set_maneuver(22);
					((Maneuver) FM).setCheckStrike(false);
					FM.Vwld.z -= 5D;
					dtime = Time.current();
				}
			} else {
				typeDockableAttemptDetach();
				((Maneuver) FM).set_maneuver(22);
				((Maneuver) FM).setCheckStrike(false);
				FM.Vwld.z -= 5D;
				dtime = Time.current();
			}
	}

	public void missionStarting() {
		checkAsDrone();
	}

	private void checkAsDrone() {
		if (target_ == null) {
			if (FM.AP.way.curr().getTarget() == null)
				FM.AP.way.next();
			target_ = FM.AP.way.curr().getTarget();
			if (Actor.isValid(target_) && (target_ instanceof Wing)) {
				Wing wing = (Wing) target_;
				int i = aircIndex();
				if (Actor.isValid(wing.airc[i]))
					target_ = wing.airc[i];
				else
					target_ = null;
			}
		}
		if (Actor.isValid(target_) && (target_ instanceof G4M2E)) {
			queen_last = target_;
			queen_time = Time.current();
			if (isNetMaster())
				((TypeDockable) target_).typeDockableRequestAttach(this, 0, true);
		}
		bNeedSetup = false;
		target_ = null;
	}

	public int typeDockableGetDockport() {
		if (typeDockableIsDocked())
			return dockport_;
		else
			return -1;
	}

	public Actor typeDockableGetQueen() {
		return queen_;
	}

	public boolean typeDockableIsDocked() {
		return Actor.isValid(queen_);
	}

	public void typeDockableAttemptAttach() {
		if (!FM.AS.isMaster())
			return;
		if (!typeDockableIsDocked()) {
			Aircraft aircraft = War.getNearestFriend(this);
			if (aircraft instanceof G4M2E)
				((TypeDockable) aircraft).typeDockableRequestAttach(this);
		}
	}

	public void typeDockableAttemptDetach() {
		if (FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
			((TypeDockable) queen_).typeDockableRequestDetach(this);
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
		queen_ = actor;
		dockport_ = i;
		queen_last = queen_;
		queen_time = 0L;
	}

	public void typeDockableDoDetachFromQueen(int i) {
		if (dockport_ != i) {
			return;
		} else {
			queen_last = queen_;
			queen_time = Time.current();
			queen_ = null;
			dockport_ = 0;
			return;
		}
	}

	public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		if (typeDockableIsDocked()) {
			netmsgguaranted.writeByte(1);
			ActorNet actornet = null;
			if (Actor.isValid(queen_)) {
				actornet = queen_.net;
				if (actornet.countNoMirrors() > 0)
					actornet = null;
			}
			netmsgguaranted.writeByte(dockport_);
			netmsgguaranted.writeNetObj(actornet);
		} else {
			netmsgguaranted.writeByte(0);
		}
	}

	public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		if (netmsginput.readByte() == 1) {
			dockport_ = netmsginput.readByte();
			NetObj netobj = netmsginput.readNetObj();
			if (netobj != null) {
				Actor actor = (Actor) netobj.superObj();
				((TypeDockable) actor).typeDockableDoAttachToDrone(this, dockport_);
			}
		}
	}

	public void doSetSootState(int i, int j) {
		switch (j) {
		case 0:
			Eff3DActor.setIntesity(flame[i], 0.0F);
			Eff3DActor.setIntesity(dust[i], 0.0F);
			Eff3DActor.setIntesity(trail[i], 0.0F);
			Eff3DActor.setIntesity(sprite[i], 0.0F);
			break;

		case 1:
			Eff3DActor.setIntesity(flame[i], 1.0F);
			Eff3DActor.setIntesity(dust[i], 0.5F);
			Eff3DActor.setIntesity(trail[i], 1.0F);
			Eff3DActor.setIntesity(sprite[i], 1.0F);
			break;
		}
	}

	private Eff3DActor flame[] = { null, null, null };
	private Eff3DActor dust[] = { null, null, null };
	private Eff3DActor trail[] = { null, null, null };
	private Eff3DActor sprite[] = { null, null, null };
	private boolean bNeedSetup;
	private long dtime;
	private Actor queen_last;
	private long queen_time;
	private Actor target_;
	private Actor queen_;
	private int dockport_;
	private boolean charge_armed;

	static {
		Class class1 = MXY_7.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "MXY");
		Property.set(class1, "meshName", "3DO/Plane/MXY-7-11(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
		Property.set(class1, "originCountry", PaintScheme.countryJapan);
		Property.set(class1, "yearService", 1945F);
		Property.set(class1, "yearExpired", 1945F);
		Property.set(class1, "FlightModel", "FlightModels/MXY-7-11.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitMXY_7.class });
		weaponTriggersRegister(class1, new int[] { 0 });
		weaponHooksRegister(class1, new String[] { "_Clip00" });
	}
}
