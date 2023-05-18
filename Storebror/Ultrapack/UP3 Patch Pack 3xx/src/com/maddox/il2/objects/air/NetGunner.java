/* 4.10.1 class */
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Message;
import com.maddox.rts.MsgInvokeMethod_Object;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.NetUpdate;
import com.maddox.rts.ObjState;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.util.IntHashtable;

public class NetGunner extends com.maddox.il2.engine.Actor {
	private com.maddox.il2.net.NetUser user;
	private java.lang.String aircraftName;
	private com.maddox.il2.objects.air.Aircraft aircraft;
	private com.maddox.util.IntHashtable filterTable;
	protected int netCockpitIndxPilot;
	protected int netCockpitWeaponControlNum;
	protected int netCockpitTuretNum;
	protected boolean netCockpitValid;
	protected NetMsgGuaranted netCockpitMsg;
	private boolean bFirstAirCheck;

	static class SPAWN implements NetSpawn {
		public void netSpawn(int i, NetMsgInput netmsginput) {
			try {
				java.lang.String aircraftName = netmsginput.read255();
				com.maddox.il2.net.NetUser netuser = (com.maddox.il2.net.NetUser) netmsginput.readNetObj();
				int cockpitNum = netmsginput.readUnsignedByte();
				if (netuser != null)
					new NetGunner(aircraftName, netuser, i, cockpitNum);

				// TODO: Added by |ZUTI|
				// ----------------------------------------------------------------
				/*
				 * System.out.println("Current gunner army: " +
				 * ZutiSupportMethods.getNetUser(netuser.uniqueName()).getArmy());
				 * System.out.println("  Resolved object army: " + netuser.getArmy());
				 */
				Actor actor = Actor.getByName(aircraftName);
				if (actor != null) {
					if (actor.equals(World.getPlayerAircraft())) {
						HUD.log("mds.netCommand.crewJoin",
								new java.lang.Object[] { netuser.uniqueName(), new Integer(Math.round(cockpitNum)) });
						System.out.println(
								"Gunner AC=" + aircraftName + ", player AC=" + World.getPlayerAircraft().name());
					}

					netuser.setArmy(actor.getArmy());
					System.out.println("NetGunner - synced >" + netuser.uniqueName() + "< army designation to >"
							+ netuser.getArmy() + "<.");
				}
				/*
				 * System.out.println("Aircraft >" + aircraftName + "< army: " +
				 * actor.getArmy());
				 * System.out.println("--------------------------------------------------");
				 */
				// ----------------------------------------------------------------
			} catch (java.lang.Exception exception) {
				java.lang.System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}

		SPAWN() {
		}
	}

	class Mirror extends com.maddox.il2.engine.ActorNet implements NetUpdate {

		public void netUpdate() {
			if (com.maddox.il2.engine.Actor.isValid(aircraft) && netCockpitTuretNum >= 0
					&& Time.current() - lastUpdateTime > 2000L)
				aircraft.FM.CT.WeaponControl[netCockpitWeaponControlNum] = false;
		}

		public boolean netInput(NetMsgInput netmsginput) throws java.io.IOException {
			if (netmsginput.isGuaranted())
				return false;
			if (isMirrored()) {
				out.unLockAndSet(netmsginput, 0);
				postReal(Message.currentTime(true), out);
			}
			if (checkAircraft() && netCockpitTuretNum >= 0) {
				int i = netmsginput.readUnsignedShort();
				int j = netmsginput.readUnsignedShort();
				float f = unpackSY(i);
				float f1 = unpackSP(j & 0x7fff);
				aircraft.FM.CT.WeaponControl[netCockpitWeaponControlNum] = (j & 0x8000) != 0;
				if (com.maddox.il2.net.NetMissionTrack.isPlaying()
						&& aircraft == com.maddox.il2.ai.World.getPlayerAircraft()) {
					com.maddox.il2.engine.Actor._tmpOrient.set(f, f1, 0.0F);
					((com.maddox.il2.objects.air.CockpitGunner) com.maddox.il2.game.Main3D
							.cur3D().cockpits[getCockpitNum()]).moveGun(com.maddox.il2.engine.Actor._tmpOrient);
				} else {
					com.maddox.il2.fm.Turret turret = aircraft.FM.turret[netCockpitTuretNum];
					turret.tu[0] = f;
					turret.tu[1] = f1;
				}
				lastUpdateTime = Time.current();
			}
			return true;
		}

		NetMsgFiltered out;
		long lastUpdateTime;

		public Mirror(com.maddox.il2.engine.Actor actor, NetChannel netchannel, int i) {
			super(actor, netchannel, i);
			out = new NetMsgFiltered();
			lastUpdateTime = Time.current();
			try {
				out.setFilterArg(actor);
			} catch (java.lang.Exception exception) {
			}
		}
	}

	class Master extends com.maddox.il2.engine.ActorNet implements NetUpdate {

		public boolean netInput(NetMsgInput netmsginput) throws java.io.IOException {
			return false;
		}

		public void netUpdate() {
			if (!com.maddox.il2.engine.Actor.isValid(aircraft)) {
				checkAircraft();
				return;
			}
			if (netCockpitValid && netCockpitTuretNum >= 0)
				try {
					// System.out.println("Gunner master sending data...");

					com.maddox.il2.fm.Turret turret = aircraft.FM.turret[netCockpitTuretNum];
					boolean flag = aircraft.FM.CT.WeaponControl[netCockpitWeaponControlNum];
					out.unLockAndClear();
					out.writeShort(packSY(turret.tu[0]));
					out.writeShort(packSP(turret.tu[1]) | (flag ? 0x8000 : 0));
					post(Time.current(), out);
				} catch (java.lang.Exception exception) {
					NetObj.printDebug(exception);
				}
		}

		NetMsgFiltered out;

		public Master(com.maddox.il2.engine.Actor actor) {
			super(actor);
			out = new NetMsgFiltered();
			try {
				out.setFilterArg(actor);
			} catch (java.lang.Exception exception) {
			}
		}
	}

	public com.maddox.util.IntHashtable getFilterTable() {
		if (filterTable == null)
			filterTable = new IntHashtable();
		return filterTable;
	}

	public java.lang.String getAircraftName() {
		return aircraftName;
	}

	public com.maddox.il2.net.NetUser getUser() {
		return user;
	}

	public com.maddox.il2.objects.air.Aircraft getAircraft() {
		checkAircraft();
		return aircraft;
	}

	public int getCockpitNum() {
		return netCockpitIndxPilot;
	}

	private boolean isMirroredAsAir() {
		if (aircraft.net == null)
			return false;
		int i = aircraft.net.countMirrors();
		if (aircraft.net.isMirror())
			i++;
		int j = net.countMirrors();
		if (net.isMirror())
			j++;
		return i == j;
	}

	private boolean checkAircraft() {
		if (com.maddox.il2.engine.Actor.isValid(aircraft))
			return isMirroredAsAir();
		aircraft = (com.maddox.il2.objects.air.Aircraft) com.maddox.il2.engine.Actor.getByName(aircraftName);
		if (!com.maddox.il2.engine.Actor.isValid(aircraft))
			return false;
		if (!isMirroredAsAir()) {
			aircraft = null;
			return false;
		}
		pos.setBase(aircraft, null, false);
		pos.resetAsBase();
		// TODO: Storebror: Debugging Army switch on gunner pos switch bug
//        System.out.println("### GUNNER POS SWITCH BUG DEBUG: NetGunner.checkAircraft aircraft.getArmy()=" + aircraft.getArmy() + ", getArmy()=" + getArmy());
		// ...
		setArmy(aircraft.getArmy());
		// TODO: Storebror: Debugging Army switch on gunner pos switch bug
		user.setArmy(getArmy()); // Original
//        user.setArmy(aircraft.getArmy()); // Test Modified
		setOwner(aircraft);
		if (isNetMaster() || user.isTrackWriter()) {
			com.maddox.il2.ai.World.cur().resetUser();
			com.maddox.il2.ai.World.setPlayerAircraft(aircraft);
			com.maddox.il2.ai.World.setPlayerFM();
			com.maddox.il2.ai.World.setPlayerRegiment();
			aircraft.createCockpits();
			try {
				com.maddox.il2.objects.air.CockpitGunner cockpitgunner = (com.maddox.il2.objects.air.CockpitGunner) com.maddox.il2.game.Main3D
						.cur3D().cockpits[getCockpitNum()];
				com.maddox.il2.game.Main3D.cur3D().cockpitCur = cockpitgunner;
				aircraft.FM.AS.astatePlayerIndex = cockpitgunner.astatePilotIndx();
				if (!user.isTrackWriter())
					aircraft.netCockpitEnter(this, getCockpitNum(), bFirstAirCheck);
				bFirstAirCheck = false;
				com.maddox.il2.game.Main3D.cur3D().cockpitCur.focusEnter();
				if (!user.isTrackWriter()) {
					// Original
					cockpitgunner.setRealMode(true);
					aircraft.netCockpitAuto(this, getCockpitNum(), false);

					// TODO: Added by |ZUTI|
					// ----------------------------------------------------------------------
					aircraft.netCockpitAuto(World.getPlayerGunner(), getCockpitNum(), true);
					// ----------------------------------------------------------------------
				}
			} catch (Exception ex) {
				// TODO: Added by |ZUTI|: bombardier requires net gunner!
				CockpitPilot cockpitgunner = (CockpitPilot) com.maddox.il2.game.Main3D
						.cur3D().cockpits[getCockpitNum()];
				com.maddox.il2.game.Main3D.cur3D().cockpitCur = cockpitgunner;
				aircraft.FM.AS.astatePlayerIndex = cockpitgunner.astatePilotIndx();
				if (!user.isTrackWriter())
					aircraft.netCockpitEnter(this, getCockpitNum(), bFirstAirCheck);
				bFirstAirCheck = false;
				com.maddox.il2.game.Main3D.cur3D().cockpitCur.focusEnter();
				if (!user.isTrackWriter()) {
					// Original
					aircraft.netCockpitAuto(this, getCockpitNum(), false);

					// TODO: Added by |ZUTI|
					// ----------------------------------------------------------------------
					aircraft.netCockpitAuto(World.getPlayerGunner(), getCockpitNum(), true);
					// ----------------------------------------------------------------------
				}
			}
		}
		user.tryPreparePilot(aircraft, aircraft.netCockpitAstatePilotIndx(getCockpitNum()));
		return true;
	}

	public void netFirstUpdate(NetChannel netchannel) throws java.io.IOException {
		doNetFirstUpdate(netchannel);
	}

	public void doNetFirstUpdate(java.lang.Object obj) {
		if (isDestroyed())
			return;
		NetChannel netchannel = (NetChannel) obj;
		if (netchannel.isDestroyed())
			return;
		if (!checkAircraft() || !netchannel.isMirrored(aircraft.net))
			if (com.maddox.il2.engine.Actor.isValid(aircraft) && aircraft.net.masterChannel() == netchannel) {
				return;
			} else {
				(new MsgInvokeMethod_Object("doNetFirstUpdate", netchannel)).post(72, this, 0.0D);
				return;
			}
		try {
			aircraft.netCockpitFirstUpdate(this, netchannel);
		} catch (java.lang.Exception exception) {
			java.lang.System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	int packSY(float f) {
		return 0xffff & (int) (((f % 360D + 180D) * 65000D) / 360D);
	}

	int packSP(float f) {
		return 0x7fff & (int) (((f % 360D + 180D) * 32000D) / 360D);
	}

	float unpackSY(int i) {
		return (float) ((i * 360D) / 65000D - 180D);
	}

	float unpackSP(int i) {
		return (float) ((i * 360D) / 32000D - 180D);
	}

	public NetGunner(java.lang.String s, com.maddox.il2.net.NetUser netuser, int i, int j) {
		netCockpitIndxPilot = 0;
		netCockpitWeaponControlNum = 0;
		netCockpitTuretNum = -1;
		netCockpitValid = false;
		netCockpitMsg = null;
		bFirstAirCheck = true;
		aircraftName = s;
		user = netuser;
		netCockpitIndxPilot = j;
		java.lang.String s1 = " " + s + "(" + j + ")";
		ObjState.destroy(com.maddox.il2.engine.Actor.getByName(s1));
		setName(s1);
		pos = new ActorPosMove(this);
		if (netuser.isMaster())
			net = new Master(this);
		else
			net = new Mirror(this, netuser.masterChannel(), i);
		if (netuser.isMaster() || netuser.isTrackWriter())
			com.maddox.il2.ai.World.setPlayerGunner(this);
	}

	public NetMsgSpawn netReplicate(NetChannel netchannel) throws java.io.IOException {
		NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
		netmsgspawn.write255(aircraftName);
		netmsgspawn.writeNetObj(user);
		netmsgspawn.writeByte(getCockpitNum());
		return netmsgspawn;
	}

	static {
		Spawn.add(com.maddox.il2.objects.air.NetGunner.class, new SPAWN());
	}

	// TODO: |ZUTI| methods and variables
	// -----------------------------------------------
	/**
	 * Get current cockpit number that gunner is occupying.
	 * 
	 * @return
	 */
	public int zutiGetCockpitNum() {
		return netCockpitIndxPilot;
	}

	/**
	 * Call this method when you receive information about player ejecting or
	 * changing cockpit position.
	 * 
	 * @param ac
	 * @param cockpitNum
	 */
	public void zutiSetAircraftAndCockpitNum(String acName, int cockpitNum) {
		this.aircraft = (Aircraft) Actor.getByName(acName);

		if (aircraft == null) {
			this.netCockpitWeaponControlNum = -1;
			this.netCockpitTuretNum = -1;
			this.aircraftName = "";
			this.netCockpitIndxPilot = -1;
		} else {
			this.aircraftName = aircraft.name();
			this.netCockpitIndxPilot = cockpitNum;
		}

	}
	// -----------------------------------------------
}