package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.Reflection;

public class TB_3_4M_34R_SPB extends TB_3 implements TypeDockable {

	public TB_3_4M_34R_SPB() {
		this.fSightCurAltitude = 300F;
		this.fSightCurSpeed = 50F;
		this.drones = new Actor[2];
		this.drones[0] = this.drones[1] = null;
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		float f2 = Math.abs(f);
		switch (i) {
			default:
				break;

			case 0:
				if (f1 < -47F) {
					f1 = -47F;
					flag = false;
				}
				if (f1 > 47F) {
					f1 = 47F;
					flag = false;
				}
				if (f2 < 147F) {
					if (f1 < 0.5964912F * f2 - 117.6842F) {
						f1 = 0.5964912F * f2 - 117.6842F;
						flag = false;
					}
				} else if (f2 < 157F) {
					if (f1 < 0.3F * f2 - 74.1F) {
						f1 = 0.3F * f2 - 74.1F;
						flag = false;
					}
				} else if (f1 < 0.2173913F * f2 - 61.13044F) {
					f1 = 0.2173913F * f2 - 61.13044F;
					flag = false;
				}
				if (f2 >= 110F) {
					if (f2 < 115F) {
						if (f1 < -5F && f1 > -20F) { flag = false; }
					} else if (f2 < 160F) {
						if (f1 < -5F) { flag = false; }
					} else if (f1 < 15F) { flag = false; }
				}
				break;

			case 1:
				if (f1 < -47F) {
					f1 = -47F;
					flag = false;
				}
				if (f1 > 47F) {
					f1 = 47F;
					flag = false;
				}
				if (f < -38F) {
					if (f1 < -32F) {
						f1 = -32F;
						flag = false;
					}
				} else if (f < -16F) {
					if (f1 < 0.5909091F * f - 9.545455F) {
						f1 = 0.5909091F * f - 9.545455F;
						flag = false;
					}
				} else if (f < 35F) {
					if (f1 < -19F) {
						f1 = -19F;
						flag = false;
					}
				} else if (f < 44F) {
					if (f1 < -3.111111F * f + 89.88889F) {
						f1 = -3.111111F * f + 89.88889F;
						flag = false;
					}
				} else if (f < 139F) {
					if (f1 < -47F) {
						f1 = -47F;
						flag = false;
					}
				} else if (f < 150F) {
					if (f1 < 1.363636F * f - 236.5455F) {
						f1 = 1.363636F * f - 236.5455F;
						flag = false;
					}
				} else if (f1 < -32F) {
					f1 = -32F;
					flag = false;
				}
				if (f < -175.7F) {
					if (f1 < 80.8F) { flag = false; }
					break;
				}
				if (f < -82F) {
					if (f1 < -16F) { flag = false; }
					break;
				}
				if (f < 24F) {
					if (f1 < 0.0F) { flag = false; }
					break;
				}
				if (f < 32F) {
					if (f1 < -8.3F) { flag = false; }
					break;
				}
				if (f < 80F) {
					if (f1 < 0.0F) { flag = false; }
					break;
				}
				if (f < 174F) {
					if (f1 < 0.5F * f - 87F) { flag = false; }
					break;
				}
				if (f < 178.7F) {
					if (f1 < 0.0F) { flag = false; }
					break;
				}
				if (f1 < 80.8F) { flag = false; }
				break;

			case 2:
				if (f1 < -47F) {
					f1 = -47F;
					flag = false;
				}
				if (f1 > 47F) {
					f1 = 47F;
					flag = false;
				}
				if (f < -90F) { flag = false; }
				if (f > 90F) { flag = false; }
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag) {
			for (int i = 0; i < 4; i++) {
				if (this.FM.AS.astateEngineStates[i] > 3 && this.FM.EI.engines[i].getReadyness() < 0.1F) { this.FM.AS.repairEngine(i); }
			}

			for (int j = 0; j < 4; j++) {
				if (this.FM.AS.astateTankStates[j] > 3 && this.FM.AS.astatePilotStates[4] < 50F && this.FM.AS.astatePilotStates[7] < 50F && World.Rnd().nextFloat() < 0.1F) { this.FM.AS.repairTank(j); }
			}

		}
	}

	public void update(float f) {
		super.update(f);
		this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -this.FM.Gears.gWheelAngles[0], 0.0F);
		this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, -this.FM.Gears.gWheelAngles[1], 0.0F);
	}

	public void msgExplosion(Explosion explosion) {
		this.setExplosion(explosion);
		if (explosion.chunkName != null && (explosion.chunkName.startsWith("Wing") || explosion.chunkName.startsWith("Tail")) && explosion.chunkName.endsWith("D3") && explosion.power < 0.014F) {
			return;
		} else {
			super.msgExplosion(explosion);
			return;
		}
	}

	public boolean typeBomberToggleAutomation() {
		return false;
	}

	public void typeBomberAdjDistanceReset() {
	}

	public void typeBomberAdjDistancePlus() {
	}

	public void typeBomberAdjDistanceMinus() {
	}

	public void typeBomberAdjSideslipReset() {
	}

	public void typeBomberAdjSideslipPlus() {
	}

	public void typeBomberAdjSideslipMinus() {
	}

	public void typeBomberAdjAltitudeReset() {
		this.fSightCurAltitude = 300F;
	}

	public void typeBomberAdjAltitudePlus() {
		this.fSightCurAltitude += 50F;
		if (this.fSightCurAltitude > 5000F) { this.fSightCurAltitude = 5000F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
	}

	public void typeBomberAdjAltitudeMinus() {
		this.fSightCurAltitude -= 50F;
		if (this.fSightCurAltitude < 300F) { this.fSightCurAltitude = 300F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
	}

	public void typeBomberAdjSpeedReset() {
		this.fSightCurSpeed = 50F;
	}

	public void typeBomberAdjSpeedPlus() {
		this.fSightCurSpeed += 5F;
		if (this.fSightCurSpeed > 350F) { this.fSightCurSpeed = 350F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		this.fSightCurSpeed -= 5F;
		if (this.fSightCurSpeed < 50F) { this.fSightCurSpeed = 50F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeFloat(this.fSightCurAltitude);
		netmsgguaranted.writeFloat(this.fSightCurSpeed);
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		this.fSightCurAltitude = netmsginput.readFloat();
		this.fSightCurSpeed = netmsginput.readFloat();
	}

	public boolean typeDockableIsDocked() {
//        DebugLog("typeDockableIsDocked()", 1);
		return true;
	}

	public void typeDockableAttemptAttach() {
		DebugLog("typeDockableAttemptAttach()", 1);
	}

	public void typeDockableAttemptDetach() {
		DebugLog("typeDockableAttemptDetach()", 1);
		DebugLog("FM.AS.isMaster() = " + this.FM.AS.isMaster(), 1);
		if (this.FM.AS.isMaster()) {
			for (int i = 0; i < DRONES_LENGTH; i++) {
				if (Actor.isValid(this.drones[i])) {
					DebugLog("drones[" + i + "] is a valid Actor, requesting Detach!", 1);
					this.typeDockableRequestDetach(this.drones[i], i, true);
				}
			}
		}
	}

	public void typeDockableRequestAttach(Actor actor) {
		DebugLog("typeDockableRequestAttach(" + (actor == null ? "null" : actor.getClass().getName()) + ")", 1);
		if (!(actor instanceof Aircraft)) { return; }
		Aircraft parasiteAircraft = (Aircraft) actor;

		// TODO: Storebror: Enable inflight attachment of I-16 drones
		boolean attachEnabled = false;
		double attachMaxDistance = 5D;
		DebugLog("parasiteAircraft.FM.AS.isMaster()=" + parasiteAircraft.FM.AS.isMaster() + ", parasiteAircraft.FM.Gears.onGround()=" + parasiteAircraft.FM.Gears.onGround() + ", parasiteAircraft.FM.getSpeedKMH()=" + parasiteAircraft.FM.getSpeedKMH()
				+ ", this.FM.Gears.onGround()=" + this.FM.Gears.onGround() + ", this.FM.getSpeedKMH()=" + this.FM.getSpeedKMH(), 1);
		if (parasiteAircraft.FM.AS.isMaster() && parasiteAircraft.FM.Gears.onGround() && parasiteAircraft.FM.getSpeedKMH() < 10F && this.FM.getSpeedKMH() < 10F) {
			attachMaxDistance = 5D;
			attachEnabled = true;
		} else if (parasiteAircraft.FM.AS.isMaster() && !parasiteAircraft.FM.Gears.onGround() && !this.FM.Gears.onGround() && parasiteAircraft.FM.getSpeedKMH() > 50F && this.FM.getSpeedKMH() > 50F) {

			Vector3d parasiteVector = new Vector3d();
			Vector3d motherVector = new Vector3d();
			parasiteAircraft.FM.getSpeed(parasiteVector);
			this.FM.getSpeed(motherVector);
			float angleBetween = Geom.RAD2DEG(Math.abs((float) parasiteVector.angle(motherVector)));

			DebugLog("angleBetween = " + angleBetween, 1);
			if (Math.abs(angleBetween) < 25F && Math.abs(this.FM.getSpeedKMH() - parasiteAircraft.FM.getSpeedKMH()) < 30F) {
				attachMaxDistance = 10D;
				attachEnabled = true;
			}
		}

		if (!attachEnabled) {
			// TODO: +++ Auto Docking Mode
			if (parasiteAircraft == World.getPlayerAircraft() && parasiteAircraft instanceof TypeDockableAutoDock) {
				if (((TypeDockableAutoDock) parasiteAircraft).TypeDockableAutoDockIsDockModeActive()) { ((TypeDockableAutoDock) parasiteAircraft).showDockDistance(-1); }
			}
			// ---
			return;
		}

		// Get closest Hook before attempting to attach!
		int hookIndex = -1;
		double hookDistance = Double.MAX_VALUE;
		for (int i = 0; i < DRONES_LENGTH; i++) {
			HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
			Loc loc = new Loc();
			Loc loc1 = new Loc();
			this.pos.getAbs(loc1);
			hooknamed.computePos(this, loc1, loc);
			actor.pos.getAbs(loc1);
			double curDistance = loc.getPoint().distance(loc1.getPoint());
			if (curDistance >= hookDistance) { continue; }
			if (Actor.isValid(this.drones[i])) { continue; }
			hookIndex = i;
			hookDistance = curDistance;
		}

		if (hookIndex == -1) {
			DebugLog("no free hook available!", 1);
			// TODO: +++ Auto Docking Mode
			if (parasiteAircraft == World.getPlayerAircraft() && parasiteAircraft instanceof TypeDockableAutoDock) {
				if (((TypeDockableAutoDock) parasiteAircraft).TypeDockableAutoDockIsDockModeActive()) { ((TypeDockableAutoDock) parasiteAircraft).showDockDistance(-1); }
			}
			// ---
			return;
		}

		if (Actor.isValid(this.drones[hookIndex])) {
			DebugLog("drones[" + hookIndex + "] = " + this.drones[hookIndex].getClass().getName(), 1);
			// TODO: +++ Auto Docking Mode
			if (parasiteAircraft == World.getPlayerAircraft() && parasiteAircraft instanceof TypeDockableAutoDock) {
				if (((TypeDockableAutoDock) parasiteAircraft).TypeDockableAutoDockIsDockModeActive()) { ((TypeDockableAutoDock) parasiteAircraft).showDockDistance(-1); }
			}
			// ---
			return;
		}

		DebugLog("Hook Distance = " + hookDistance + "(attachMaxDistance = " + attachMaxDistance + ")", 1);

		// TODO: +++ Auto Docking Mode
		if (parasiteAircraft == World.getPlayerAircraft() && parasiteAircraft instanceof TypeDockableAutoDock) {
			if (((TypeDockableAutoDock) parasiteAircraft).TypeDockableAutoDockIsDockModeActive()) {
				if (hookDistance >= attachMaxDistance * 20D) {
					((TypeDockableAutoDock) parasiteAircraft).showDockDistance(-1);
				} else {
					((TypeDockableAutoDock) parasiteAircraft).showDockDistance((int) Aircraft.cvt((float) hookDistance, (float) attachMaxDistance, (float) attachMaxDistance * 20F, 0F, 20F));
				}
			}
		}
		// ---

		if (hookDistance >= attachMaxDistance) { return; }
		DebugLog("FM.AS.isMaster() = " + this.FM.AS.isMaster(), 1);
		if (this.FM.AS.isMaster()) {
			this.typeDockableRequestAttach(actor, hookIndex, true);
			return;
		} else {
			AircraftStateClearSendedDockableItemsToMaster(this.FM.AS, 32);
			this.FM.AS.netToMaster(32, hookIndex, 0, actor);
			return;
		}

	}

	public void typeDockableRequestDetach(Actor actor) {
		DebugLog("typeDockableRequestDetach(" + (actor == null ? "null" : actor.getClass().getName()) + ")", 1);
		for (int i = 0; i < DRONES_LENGTH; i++) {
			if (actor != this.drones[i]) { continue; }
			DebugLog("actor == drones[" + i + "] (" + this.drones[i].getClass().getName() + ")", 1);
			Aircraft aircraft = (Aircraft) actor;
			DebugLog("aircraft.FM.AS.isMaster() = " + aircraft.FM.AS.isMaster(), 1);
			DebugLog("FM.AS.isMaster() = " + this.FM.AS.isMaster(), 1);
			if (!aircraft.FM.AS.isMaster()) { continue; }
			if (this.FM.AS.isMaster()) {
				this.typeDockableRequestDetach(actor, i, true);
			} else {
				AircraftStateClearSendedDockableItemsToMaster(this.FM.AS, 33);
			}
			this.FM.AS.netToMaster(33, i, 1, actor);
		}

	}

	public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
		DebugLog("typeDockableRequestAttach(" + (actor == null ? "null" : actor.getClass().getName()) + ", " + i + ", " + flag + ")", 1);
		if (i < 0 || i > 1) { return; }
		if (flag) {
			if (this.FM.AS.isMaster()) {
				DebugLog("FM.AS.isMaster() = true", 1);
				AircraftStateClearSendedDockableItemsToMirrors(this.FM.AS, 34);
				this.FM.AS.netToMirrors(34, i, 1, actor);
				this.typeDockableDoAttachToDrone(actor, i);
			} else {
				DebugLog("FM.AS.isMaster() = false", 1);
				AircraftStateClearSendedDockableItemsToMaster(this.FM.AS, 34);
				this.FM.AS.netToMaster(34, i, 1, actor);
			}
		} else if (this.FM.AS.isMaster()) {
			DebugLog("FM.AS.isMaster() = true", 1);
			if (!Actor.isValid(this.drones[i])) {
				DebugLog("drones[" + i + "] is not a valid Actor!", 1);
				AircraftStateClearSendedDockableItemsToMirrors(this.FM.AS, 34);
				this.FM.AS.netToMirrors(34, i, 1, actor);
				this.typeDockableDoAttachToDrone(actor, i);
			}
		} else {
			DebugLog("FM.AS.isMaster() = false", 1);
			AircraftStateClearSendedDockableItemsToMaster(this.FM.AS, 34);
			this.FM.AS.netToMaster(34, i, 0, actor);
		}
	}

	public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
		DebugLog("typeDockableRequestDetach(" + (actor == null ? "null" : actor.getClass().getName()) + ", " + i + ", " + flag + ")", 1);
		if (flag) {
			if (this.FM.AS.isMaster()) {
				DebugLog("FM.AS.isMaster() = true", 1);
				AircraftStateClearSendedDockableItemsToMirrors(this.FM.AS, 35);
				this.FM.AS.netToMirrors(35, i, 1, actor);
				this.typeDockableDoDetachFromDrone(i);
			} else {
				DebugLog("FM.AS.isMaster() = false", 1);
				AircraftStateClearSendedDockableItemsToMaster(this.FM.AS, 35);
				this.FM.AS.netToMaster(35, i, 1, actor);
			}
		}
	}

	public void typeDockableDoAttachToDrone(Actor actor, int i) {
		DebugLog("typeDockableDoAttachToDrone(" + (actor == null ? "null" : actor.getClass().getName()) + ", " + i + ")", 1);
		if (!Actor.isValid(this.drones[i])) {
			DebugLog("drones[" + i + "] is not a valid Actor!", 1);
			HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
			Loc loc = new Loc();
			Loc loc1 = new Loc();
			this.pos.getAbs(loc1);
			hooknamed.computePos(this, loc1, loc);
			actor.pos.setAbs(loc);
			actor.pos.setBase(this, null, true);
			actor.pos.resetAsBase();
			this.drones[i] = actor;
			DebugLog("Attaching drones[" + i + "] (" + this.drones[i].getClass().getName() + ")", 1);
			((TypeDockable) this.drones[i]).typeDockableDoAttachToQueen(this, i);
		}
	}

	public void typeDockableDoDetachFromDrone(int i) {
		DebugLog("typeDockableDoDetachFromDrone(" + i + ")", 1);
		if (!Actor.isValid(this.drones[i])) {
			DebugLog("drones[" + i + "] is not a valid Actor!", 1);
			return;
		} else {
			DebugLog("Detaching drones[" + i + "] (" + this.drones[i].getClass().getName() + ")", 1);
			this.drones[i].pos.setBase(null, null, true);
			((TypeDockable) this.drones[i]).typeDockableDoDetachFromQueen(i);
			this.drones[i] = null;
			return;
		}
	}

	public void typeDockableDoAttachToQueen(Actor actor, int i) {
		DebugLog("typeDockableDoAttachToQueen(" + (actor == null ? "null" : actor.getClass().getName()) + ", " + i + ")", 1);
	}

	public void typeDockableDoDetachFromQueen(int i) {
		DebugLog("typeDockableDoDetachFromQueen(" + i + ")", 1);
	}

	public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		DebugLog("typeDockableReplicateToNet(" + netmsgguaranted.dataLength() + ")", 1);
		for (int i = 0; i < DRONES_LENGTH; i++) {
			if (Actor.isValid(this.drones[i])) {
				netmsgguaranted.writeByte(1);
				com.maddox.il2.engine.ActorNet actornet = this.drones[i].net;
				if (actornet.countNoMirrors() == 0) {
					netmsgguaranted.writeNetObj(actornet);
				} else {
					netmsgguaranted.writeNetObj(null);
				}
			} else {
				netmsgguaranted.writeByte(0);
			}
		}

	}

	public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		DebugLog("typeDockableReplicateFromNet(" + netmsginput.available() + ")", 1);
		for (int i = 0; i < DRONES_LENGTH; i++) {
			if (netmsginput.readByte() != 1) { continue; }
			NetObj netobj = netmsginput.readNetObj();
			if (netobj != null) { this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), i); }
		}

	}

	protected boolean cutFM(int i, int j, Actor actor) {
		if (this.FM.AS.isMaster()) {
			switch (i) {
				case 33:
				case 34:
				case 35:
					this.typeDockableRequestDetach(this.drones[0], 0, true);
					break;

				case 36:
				case 37:
				case 38:
					this.typeDockableRequestDetach(this.drones[1], 1, true);
					break;
			}
		}
		return super.cutFM(i, j, actor);
	}

	private static void DebugLog(String logLine, int minLogLevel) {
		if (DEBUG >= minLogLevel) { System.out.println("[TB_3_4M_34R_SPB] " + logLine); }
	}

	protected static Aircraft GetNearestFriendlyInstance(Aircraft referenceAircraft, float maxDistance) {
		Point3d referencePos = referenceAircraft.pos.getAbsPoint();
		double maxDistanceSquared = maxDistance * maxDistance;
		int referenceArmy = referenceAircraft.getArmy();
		Aircraft nearestFriendlyInstance = null;
		List targetList = Engine.targets();
		int targetListSize = targetList.size();
		for (int targetListIndex = 0; targetListIndex < targetListSize; targetListIndex++) {
			Actor curTarget = (Actor) targetList.get(targetListIndex);
			if (curTarget instanceof TB_3_4M_34R_SPB && curTarget != referenceAircraft && curTarget.getArmy() == referenceArmy) {
				Point3d curTargetPos = curTarget.pos.getAbsPoint();
				double curTargetDistanceSquared = (referencePos.x - curTargetPos.x) * (referencePos.x - curTargetPos.x) + (referencePos.y - curTargetPos.y) * (referencePos.y - curTargetPos.y)
						+ (referencePos.z - curTargetPos.z) * (referencePos.z - curTargetPos.z);
				if (curTargetDistanceSquared < maxDistanceSquared) {
					nearestFriendlyInstance = (Aircraft) curTarget;
					maxDistanceSquared = curTargetDistanceSquared;
				}
			}
		}
		return nearestFriendlyInstance;
	}

	protected static void AircraftStateClearSendedDockableItemsToMaster(AircraftState as, int asMessageIndex) {
		AircraftStateClearSendedDockableItems(as, asMessageIndex, "itemsToMaster");
	}

	protected static void AircraftStateClearSendedDockableItemsToMirrors(AircraftState as, int asMessageIndex) {
		AircraftStateClearSendedDockableItems(as, asMessageIndex, "itemsToMirrors");
	}

	protected static void AircraftStateClearSendedDockableItems(AircraftState as, int asMessageIndex, String fieldName) {
		Object[] itemsToMaster = (Object[]) Reflection.getValue(as, fieldName);
		if (itemsToMaster == null) { return; }
		if (itemsToMaster.length <= asMessageIndex) {
			System.out.println("[TB_3_4M_34R_SPB] cannot clear " + fieldName + "[" + asMessageIndex + "], " + fieldName + " length is " + itemsToMaster.length);
			return;
		}
		if (itemsToMaster[asMessageIndex] == null) { return; }
		itemsToMaster[asMessageIndex] = null;
	}

	public static int     DEBUG         = 0;
	public static int     DRONES_LENGTH = 2;
	public static boolean bChangedPit   = false;
	public float          fSightCurAltitude;
	public float          fSightCurSpeed;
	private Actor[]       drones;

	static {
		Class class1 = TB_3_4M_34R_SPB.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "TB-3");
		Property.set(class1, "meshName", "3DO/Plane/TB-3-4M-34R_SPB/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "yearService", 1932F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/TB-3-4M-34R.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitTB_3.class, CockpitTB_3_Bombardier2.class, CockpitTB_3_NGunner.class, CockpitTB_3_TGunner3.class, CockpitTB_3_RGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04",
						"_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12",
						"_BombSpawn01", "_BombSpawn02" });
	}
}
