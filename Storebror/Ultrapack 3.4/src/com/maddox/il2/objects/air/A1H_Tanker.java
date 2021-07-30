package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;

public class A1H_Tanker extends AD_Tanker implements TypeDockable {

    public A1H_Tanker() {
    }

    public boolean typeDockableIsDocked() {
        return true;
    }

    public void typeDockableAttemptAttach() {
    }

    public void typeDockableAttemptDetach() {
        if (this.FM.AS.isMaster()) for (int i = 0; i < this.drones.length; i++)
            if (Actor.isValid(this.drones[i])) this.typeDockableRequestDetach(this.drones[i], i, true);
    }

    public void typeDockableRequestAttach(Actor actor) {
        if (actor instanceof Aircraft) {
            Aircraft aircraft = (Aircraft) actor;
            if (aircraft.FM.AS.isMaster() && aircraft.FM.getSpeedKMH() > 10F && this.FM.getSpeedKMH() > 10F) for (int i = 0; i < this.drones.length; i++) {
                if (Actor.isValid(this.drones[i])) continue;
                HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
                Loc loc = new Loc();
                Loc loc1 = new Loc();
                this.pos.getAbs(loc1);
                hooknamed.computePos(this, loc1, loc);
                actor.pos.getAbs(loc1);
                if (loc.getPoint().distance(loc1.getPoint()) >= 7.5D) continue;
                if (this.FM.AS.isMaster()) this.typeDockableRequestAttach(actor, i, true);
                else this.FM.AS.netToMaster(32, i, 0, actor);
                break;
            }
        }
    }

    public void typeDockableRequestDetach(Actor actor) {
        for (int i = 0; i < this.drones.length; i++) {
            if (actor != this.drones[i]) continue;
            Aircraft aircraft = (Aircraft) actor;
            if (!aircraft.FM.AS.isMaster()) continue;
            if (this.FM.AS.isMaster()) this.typeDockableRequestDetach(actor, i, true);
            else this.FM.AS.netToMaster(33, i, 1, actor);
        }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
        if (i >= 0 && i <= 1) if (flag) {
            if (this.FM.AS.isMaster()) {
                this.FM.AS.netToMirrors(34, i, 1, actor);
                this.typeDockableDoAttachToDrone(actor, i);
            } else this.FM.AS.netToMaster(34, i, 1, actor);
        } else if (this.FM.AS.isMaster()) {
            if (!Actor.isValid(this.drones[i])) {
                this.FM.AS.netToMirrors(34, i, 1, actor);
                this.typeDockableDoAttachToDrone(actor, i);
            }
        } else this.FM.AS.netToMaster(34, i, 0, actor);
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
        if (flag) if (this.FM.AS.isMaster()) {
            this.FM.AS.netToMirrors(35, i, 1, actor);
            this.typeDockableDoDetachFromDrone(i);
        } else this.FM.AS.netToMaster(35, i, 1, actor);
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i) {
        if (!Actor.isValid(this.drones[i])) {
            HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            this.pos.getAbs(loc1);
            hooknamed.computePos(this, loc1, loc);
            HookNamed probe = new HookNamed((ActorMesh) actor, "_Probe");
            Loc loc2 = new Loc();
            probe.computePos(this, loc, loc2);
            actor.pos.setAbs(loc2);
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

    public void typeDockableDoAttachToQueen(Actor actor1, int j) {
    }

    public void typeDockableDoDetachFromQueen(int j) {
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        for (int i = 0; i < this.drones.length; i++)
            if (Actor.isValid(this.drones[i])) {
                netmsgguaranted.writeByte(1);
                com.maddox.il2.engine.ActorNet actornet = this.drones[i].net;
                if (actornet.countNoMirrors() == 0) netmsgguaranted.writeNetObj(actornet);
                else netmsgguaranted.writeNetObj(null);
            } else netmsgguaranted.writeByte(0);

    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        for (int i = 0; i < this.drones.length; i++) {
            if (netmsginput.readByte() != 1) continue;
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null) this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), i);
        }

    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (this.FM.AS.isMaster()) switch (i) {
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
        return super.cutFM(i, j, actor);
    }

    private Actor drones[] = { null, null };

    static {
        Class class1 = A1H_Tanker.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A1H");
        Property.set(class1, "meshName", "3DO/Plane/A1H_Tanker(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/A1H.fmd");
        Property.set(class1, "LOSElevation", 1.0585F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev17", "_ExternalDev18" });
    }
}
