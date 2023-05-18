package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;

public class G4M2E extends G4M implements TypeBomber, TypeTransport, TypeDockable {

	public G4M2E() {
	}

	public boolean typeDockableIsDocked() {
		return true;
	}

	public Actor typeDockableGetDrone() {
		return drones[0];
	}

	public void typeDockableAttemptAttach() {
	}

	public void typeDockableAttemptDetach() {
		if (FM.AS.isMaster()) {
			for (int i = 0; i < drones.length; i++)
				if (Actor.isValid(drones[i]))
					typeDockableRequestDetach(drones[i], i, true);

		}
	}

	public void typeDockableRequestAttach(Actor actor) {
		if (actor instanceof Aircraft) {
			Aircraft aircraft = (Aircraft) actor;
			if (aircraft.FM.AS.isMaster() && aircraft.FM.Gears.onGround() && aircraft.FM.getSpeedKMH() < 10F
					&& FM.getSpeedKMH() < 10F) {
				for (int i = 0; i < drones.length; i++) {
					if (Actor.isValid(drones[i]))
						continue;
					HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
					Loc loc = new Loc();
					Loc loc1 = new Loc();
					pos.getAbs(loc1);
					hooknamed.computePos(this, loc1, loc);
					actor.pos.getAbs(loc1);

					// TODO: Storebror: Enhance docking radius to 50m
					// because it's impossible to roll above the MXY-7
					// with a G4M-2 without exploding
//                    if (loc.getPoint().distance(loc1.getPoint()) >= 5D)
					if (loc.getPoint().distance(loc1.getPoint()) >= 50D)
						continue;
					if (FM.AS.isMaster())
						typeDockableRequestAttach(actor, i, true);
					else
						FM.AS.netToMaster(32, i, 0, actor);
					break;
				}

			}
		}
	}

	public void typeDockableRequestDetach(Actor actor) {
		for (int i = 0; i < drones.length; i++)
			if (actor == drones[i]) {
				Aircraft aircraft = (Aircraft) actor;
				if (aircraft.FM.AS.isMaster())
					if (FM.AS.isMaster())
						typeDockableRequestDetach(actor, i, true);
					else
						FM.AS.netToMaster(33, i, 1, actor);
			}

	}

	public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
		if (i >= 0 && i <= 1)
			if (flag) {
				if (FM.AS.isMaster()) {
					FM.AS.netToMirrors(34, i, 1, actor);
					typeDockableDoAttachToDrone(actor, i);
				} else {
					FM.AS.netToMaster(34, i, 1, actor);
				}
			} else if (FM.AS.isMaster()) {
				if (!Actor.isValid(drones[i])) {
					FM.AS.netToMirrors(34, i, 1, actor);
					typeDockableDoAttachToDrone(actor, i);
				}
			} else {
				FM.AS.netToMaster(34, i, 0, actor);
			}
	}

	public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
		if (flag)
			if (FM.AS.isMaster()) {
				FM.AS.netToMirrors(35, i, 1, actor);
				typeDockableDoDetachFromDrone(i);
			} else {
				FM.AS.netToMaster(35, i, 1, actor);
			}
	}

	public void typeDockableDoAttachToDrone(Actor actor, int i) {
		if (!Actor.isValid(drones[i])) {
			HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
			Loc loc = new Loc();
			Loc loc1 = new Loc();
			pos.getAbs(loc1);
			hooknamed.computePos(this, loc1, loc);
			actor.pos.setAbs(loc);
			actor.pos.setBase(this, null, true);
			actor.pos.resetAsBase();
			drones[i] = actor;
			((TypeDockable) drones[i]).typeDockableDoAttachToQueen(this, i);
		}
	}

	public void typeDockableDoDetachFromDrone(int i) {
		if (Actor.isValid(drones[i])) {
			drones[i].pos.setBase(null, null, true);
			((TypeDockable) drones[i]).typeDockableDoDetachFromQueen(i);
			drones[i] = null;
		}
	}

	public void typeDockableDoAttachToQueen(Actor actor, int i) {
	}

	public void typeDockableDoDetachFromQueen(int i) {
	}

	public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		for (int i = 0; i < drones.length; i++)
			if (Actor.isValid(drones[i])) {
				netmsgguaranted.writeByte(1);
				ActorNet actornet = drones[i].net;
				if (actornet.countNoMirrors() == 0)
					netmsgguaranted.writeNetObj(actornet);
				else
					netmsgguaranted.writeNetObj(null);
			} else {
				netmsgguaranted.writeByte(0);
			}

	}

	public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		for (int i = 0; i < drones.length; i++)
			if (netmsginput.readByte() == 1) {
				NetObj netobj = netmsginput.readNetObj();
				if (netobj != null)
					typeDockableDoAttachToDrone((Actor) netobj.superObj(), i);
			}

	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -20F) {
				f = -20F;
				flag = false;
			}
			if (f > 20F) {
				f = 20F;
				flag = false;
			}
			if (f1 < -20F) {
				f1 = -20F;
				flag = false;
			}
			if (f1 > 20F) {
				f1 = 20F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < -30F) {
				f = -30F;
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
			if (f1 > 30F) {
				f1 = 30F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f1 < -3F) {
				f1 = -3F;
				flag = false;
			}
			if (f1 > 70F) {
				f1 = 70F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f < -10F) {
				f = -10F;
				flag = false;
			}
			if (f > 70F) {
				f = 70F;
				flag = false;
			}
			if (f1 < -40F) {
				f1 = -40F;
				flag = false;
			}
			if (f1 > 25F) {
				f1 = 25F;
				flag = false;
			}
			break;

		case 4: // '\004'
			if (f < -70F) {
				f = -70F;
				flag = false;
			}
			if (f > 45F) {
				f = 45F;
				flag = false;
			}
			if (f1 < -40F) {
				f1 = -40F;
				flag = false;
			}
			if (f1 > 25F) {
				f1 = 25F;
				flag = false;
			}
			break;

		case 5: // '\005'
			if (f < -30F) {
				f = -30F;
				flag = false;
			}
			if (f > 30F) {
				f = 30F;
				flag = false;
			}
			if (f1 < -70F) {
				f1 = -70F;
				flag = false;
			}
			if (f1 > 70F) {
				f1 = 70F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	private Actor drones[] = { null };

	static {
		Class class1 = G4M2E.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "G4M");
		Property.set(class1, "meshName", "3DO/Plane/G4M2E(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
		Property.set(class1, "meshName_ja", "3DO/Plane/G4M2E(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
		Property.set(class1, "yearService", 1945F);
		Property.set(class1, "yearExpired", 1946F);
		Property.set(class1, "FlightModel", "FlightModels/G4M1-11.fmd");
		Property.set(class1, "cockpitClass",
				new Class[] { CockpitG4M2E.class, CockpitG4M2E_TGunner.class, CockpitG4M2E_AGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 15 });
		weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04" });
	}
}
