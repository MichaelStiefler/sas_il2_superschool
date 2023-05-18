package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;

public class KB_29P extends KB_29 implements TypeBomber, TypeDockable {

	public KB_29P() {
		this.bSightAutomation = false;
		this.bSightBombDump = false;
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurAltitude = 3000F;
		this.fSightCurSpeed = 200F;
		this.fSightCurReadyness = 0.0F;
		this.fSightSetForwardAngle = 0.0F;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 7; i++) {
			if (this.FM.getAltitude() < 3000F) {
				this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
			} else {
				this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
			}
		}

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
		switch (i) {
			default:
				break;

			case 0:
				if (f < -23F) {
					f = -23F;
					flag = false;
				}
				if (f > 23F) {
					f = 23F;
					flag = false;
				}
				if (f1 < -25F) {
					f1 = -25F;
					flag = false;
				}
				if (f1 > 15F) {
					f1 = 15F;
					flag = false;
				}
				break;

			case 1:
				if (f1 < 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				if (f1 > 73F) {
					f1 = 73F;
					flag = false;
				}
				break;

			case 2:
				if (f < -38F) {
					f = -38F;
					flag = false;
				}
				if (f > 38F) {
					f = 38F;
					flag = false;
				}
				if (f1 < -41F) {
					f1 = -41F;
					flag = false;
				}
				if (f1 > 43F) {
					f1 = 43F;
					flag = false;
				}
				break;

			case 3:
				if (f < -85F) {
					f = -85F;
					flag = false;
				}
				if (f > 22F) {
					f = 22F;
					flag = false;
				}
				if (f1 < -40F) {
					f1 = -40F;
					flag = false;
				}
				if (f1 > 32F) {
					f1 = 32F;
					flag = false;
				}
				break;

			case 4:
				if (f < -34F) {
					f = -34F;
					flag = false;
				}
				if (f > 30F) {
					f = 30F;
					flag = false;
				}
				if (f1 < -30F) {
					f1 = -30F;
					flag = false;
				}
				if (f1 > 32F) {
					f1 = 32F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 2:
				this.FM.turret[0].bIsOperable = false;
				break;

			case 3:
				this.FM.turret[1].bIsOperable = false;
				break;

			case 4:
				this.FM.turret[2].bIsOperable = false;
				break;

			case 5:
				this.FM.turret[3].bIsOperable = false;
				this.FM.turret[4].bIsOperable = false;
				break;
		}
	}

	private static final float toMeters(float f) {
		return 0.3048F * f;
	}

	private static final float toMetersPerSecond(float f) {
		return 0.4470401F * f;
	}

	public boolean typeBomberToggleAutomation() {
		this.bSightAutomation = !this.bSightAutomation;
		this.bSightBombDump = false;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
		return this.bSightAutomation;
	}

	public void typeBomberAdjDistanceReset() {
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		this.fSightCurForwardAngle++;
		if (this.fSightCurForwardAngle > 85F) { this.fSightCurForwardAngle = 85F; }
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
		if (this.bSightAutomation) { this.typeBomberToggleAutomation(); }
	}

	public void typeBomberAdjDistanceMinus() {
		this.fSightCurForwardAngle--;
		if (this.fSightCurForwardAngle < 0.0F) { this.fSightCurForwardAngle = 0.0F; }
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
		if (this.bSightAutomation) { this.typeBomberToggleAutomation(); }
	}

	public void typeBomberAdjSideslipReset() {
		this.fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		this.fSightCurSideslip += 0.1F;
		if (this.fSightCurSideslip > 3F) { this.fSightCurSideslip = 3F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 10F)) });
	}

	public void typeBomberAdjSideslipMinus() {
		this.fSightCurSideslip -= 0.1F;
		if (this.fSightCurSideslip < -3F) { this.fSightCurSideslip = -3F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 10F)) });
	}

	public void typeBomberAdjAltitudeReset() {
		this.fSightCurAltitude = 3000F;
	}

	public void typeBomberAdjAltitudePlus() {
		this.fSightCurAltitude += 50F;
		if (this.fSightCurAltitude > 50000F) { this.fSightCurAltitude = 50000F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus() {
		this.fSightCurAltitude -= 50F;
		if (this.fSightCurAltitude < 1000F) { this.fSightCurAltitude = 1000F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
	}

	public void typeBomberAdjSpeedReset() {
		this.fSightCurSpeed = 200F;
	}

	public void typeBomberAdjSpeedPlus() {
		this.fSightCurSpeed += 10F;
		if (this.fSightCurSpeed > 450F) { this.fSightCurSpeed = 450F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		this.fSightCurSpeed -= 10F;
		if (this.fSightCurSpeed < 100F) { this.fSightCurSpeed = 100F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
			this.fSightCurReadyness -= 0.0666666F * f;
			if (this.fSightCurReadyness < 0.0F) { this.fSightCurReadyness = 0.0F; }
		}
		if (this.fSightCurReadyness < 1.0F) {
			this.fSightCurReadyness += 0.0333333F * f;
		} else if (this.bSightAutomation) {
			this.fSightCurDistance -= toMetersPerSecond(this.fSightCurSpeed) * f;
			if (this.fSightCurDistance < 0.0F) {
				this.fSightCurDistance = 0.0F;
				this.typeBomberToggleAutomation();
			}
			this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / toMeters(this.fSightCurAltitude)));
			if (this.fSightCurDistance < toMetersPerSecond(this.fSightCurSpeed) * Math.sqrt(toMeters(this.fSightCurAltitude) * (2F / 9.81F))) { this.bSightBombDump = true; }
			if (this.bSightBombDump) {
				if (this.FM.isTick(3, 0)) {
					if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
						this.FM.CT.WeaponControl[3] = true;
						HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
					}
				} else {
					this.FM.CT.WeaponControl[3] = false;
				}
			}
		}
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
		netmsgguaranted.writeFloat(this.fSightCurDistance);
		netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
		netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
		netmsgguaranted.writeFloat(this.fSightCurAltitude);
		netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
		netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		int i = netmsginput.readUnsignedByte();
		this.bSightAutomation = (i & 1) != 0;
		this.bSightBombDump = (i & 2) != 0;
		this.fSightCurDistance = netmsginput.readFloat();
		this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
		this.fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
		this.fSightCurAltitude = netmsginput.readFloat();
		this.fSightCurSpeed = netmsginput.readUnsignedByte() * 2.5F;
		this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
	}

	public boolean typeDockableIsDocked() {
		return true;
	}

	public void typeDockableAttemptAttach() {
	}

	public void typeDockableAttemptDetach() {
		if (this.FM.AS.isMaster()) {
			for (int i = 0; i < this.drones.length; i++) {
				if (Actor.isValid(this.drones[i])) { this.typeDockableRequestDetach(this.drones[i], i, true); }
			}

		}
	}

	public void typeDockableRequestAttach(Actor actor) {
		if (actor instanceof Aircraft) {
			Aircraft aircraft = (Aircraft) actor;
			if (aircraft.FM.AS.isMaster() && aircraft.FM.getSpeedKMH() > 10F && this.FM.getSpeedKMH() > 10F) {
				for (int i = 0; i < this.drones.length; i++) {
					if (Actor.isValid(this.drones[i])) { continue; }
					HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
					Loc loc = new Loc();
					Loc loc1 = new Loc();
					this.pos.getAbs(loc1);
					hooknamed.computePos(this, loc1, loc);
					actor.pos.getAbs(loc1);
					if (loc.getPoint().distance(loc1.getPoint()) >= 7.5D) { continue; }
					if (this.FM.AS.isMaster()) {
						this.typeDockableRequestAttach(actor, i, true);
					} else {
						this.FM.AS.netToMaster(32, i, 0, actor);
					}
					break;
				}

			}
		}
	}

	public void typeDockableRequestDetach(Actor actor) {
		for (int i = 0; i < this.drones.length; i++) {
			if (actor == this.drones[i]) {
				Aircraft aircraft = (Aircraft) actor;
				if (aircraft.FM.AS.isMaster()) {
					if (this.FM.AS.isMaster()) {
						this.typeDockableRequestDetach(actor, i, true);
					} else {
						this.FM.AS.netToMaster(33, i, 1, actor);
					}
				}
			}
		}

	}

	public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
		if (i >= 0 && i <= 1) {
			if (flag) {
				if (this.FM.AS.isMaster()) {
					this.FM.AS.netToMirrors(34, i, 1, actor);
					this.typeDockableDoAttachToDrone(actor, i);
				} else {
					this.FM.AS.netToMaster(34, i, 1, actor);
				}
			} else if (this.FM.AS.isMaster()) {
				if (!Actor.isValid(this.drones[i])) {
					this.FM.AS.netToMirrors(34, i, 1, actor);
					this.typeDockableDoAttachToDrone(actor, i);
				}
			} else {
				this.FM.AS.netToMaster(34, i, 0, actor);
			}
		}
	}

	public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
		if (flag) {
			if (this.FM.AS.isMaster()) {
				this.FM.AS.netToMirrors(35, i, 1, actor);
				this.typeDockableDoDetachFromDrone(i);
			} else {
				this.FM.AS.netToMaster(35, i, 1, actor);
			}
		}
	}

	public void typeDockableDoAttachToDrone(Actor actor, int i) {
		if (!Actor.isValid(this.drones[i])) {
			HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
			Loc loc = new Loc();
			Loc loc1 = new Loc();
			this.pos.getAbs(loc1);
			hooknamed.computePos(this, loc1, loc);
			actor.pos.setAbs(loc);
			actor.pos.setBase(this, null, true);
			actor.pos.resetAsBase();
			this.drones[i] = actor;
			((TypeDockable) this.drones[i]).typeDockableDoAttachToQueen(this, i);
		}
	}

	public void typeDockableDoDetachFromDrone(int i) {
		if (Actor.isValid(this.drones[i])) {
			this.drones[i].pos.setBase(null, null, true);
			((TypeDockable) this.drones[i]).typeDockableDoDetachFromQueen(i);
			this.drones[i] = null;
		}
	}

	public void typeDockableDoAttachToQueen(Actor actor, int i) {
	}

	public void typeDockableDoDetachFromQueen(int i) {
	}

	public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		for (int i = 0; i < this.drones.length; i++) {
			if (Actor.isValid(this.drones[i])) {
				netmsgguaranted.writeByte(1);
				ActorNet actornet = this.drones[i].net;
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
		for (int i = 0; i < this.drones.length; i++) {
			if (netmsginput.readByte() == 1) {
				NetObj netobj = netmsginput.readNetObj();
				if (netobj != null) { this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), i); }
			}
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

	public static boolean bChangedPit = false;
	private Actor         drones[]    = { null, null };
	private boolean       bSightAutomation;
	private boolean       bSightBombDump;
	private float         fSightCurDistance;
	public float          fSightCurForwardAngle;
	public float          fSightCurSideslip;
	public float          fSightCurAltitude;
	public float          fSightCurSpeed;
	public float          fSightCurReadyness;
	public float          fSightSetForwardAngle;

	static {
		Class class1 = KB_29P.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "KB-29");
		Property.set(class1, "meshName", "3DO/Plane/KB-29/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
		Property.set(class1, "yearService", 1948.5F);
		Property.set(class1, "yearExpired", 1960.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-29.fmd");
		Aircraft.weaponTriggersRegister(class1, new int[] { 14, 14, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02" });
	}
}
