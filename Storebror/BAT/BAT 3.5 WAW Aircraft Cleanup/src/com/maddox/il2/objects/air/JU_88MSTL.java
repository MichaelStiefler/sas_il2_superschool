package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;

public class JU_88MSTL extends JU_88 implements TypeDockable {

    public JU_88MSTL() {
        this.droneInitiator = null;
    }

    public void msgEndAction(Object obj, int i) {
        super.msgEndAction(obj, i);
        switch (i) {
            case 2:
                MsgExplosion.send(this, null, this.FM.Loc, this.droneInitiator, 0.0F, 4550F, 0, 890F);
                break;
        }
    }

    protected void doExplosion() {
        super.doExplosion();
        World.cur();
        if ((this.FM.Loc.z - 300D) < World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y)) {
            if (Engine.land().isWater(this.FM.Loc.x, this.FM.Loc.y)) {
                Explosions.bomb1000_water(this.FM.Loc, 1.0F, 1.0F);
            } else {
                Explosions.bomb1000_land(this.FM.Loc, 1.0F, 1.0F, true, false);
            }
        }
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLMid") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitTank(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("WingRMid") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitTank(shot.initiator, 3, 1);
        }
        if (shot.chunkName.startsWith("WingLIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitTank(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("WingRIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitTank(shot.initiator, 2, 1);
        }
        if (shot.chunkName.startsWith("Engine1") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("Engine2") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 1, 1);
        }
        super.msgShot(shot);
    }

    public boolean typeDockableIsDocked() {
        return true;
    }

    public void typeDockableAttemptAttach() {
    }

    public void typeDockableAttemptDetach() {
        if (this.FM.AS.isMaster()) {
            for (int i = 0; i < this.drones.length; i++) {
                if (Actor.isValid(this.drones[i])) {
                    this.typeDockableRequestDetach(this.drones[i], i, true);
                }
            }

        }
    }

    public void typeDockableRequestAttach(Actor actor) {
    }

    public void typeDockableRequestDetach(Actor actor) {
        for (int i = 0; i < this.drones.length; i++) {
            if (actor != this.drones[i]) {
                continue;
            }
            Aircraft aircraft = (Aircraft) actor;
            if (!aircraft.FM.AS.isMaster()) {
                continue;
            }
            if (this.FM.AS.isMaster()) {
                this.typeDockableRequestDetach(actor, i, true);
            } else {
                this.FM.AS.netToMaster(33, i, 1, actor);
            }
        }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
        if (i != 0) {
            return;
        }
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
            this.droneInitiator = actor;
            ((TypeDockable) this.drones[i]).typeDockableDoAttachToQueen(this, i);
        }
    }

    public void typeDockableDoDetachFromDrone(int i) {
        if (!Actor.isValid(this.drones[i])) {
            return;
        } else {
            this.drones[i].pos.setBase(null, null, true);
            ((TypeDockable) this.drones[i]).typeDockableDoDetachFromQueen(i);
            this.drones[i] = null;
            return;
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
        for (int i = 0; i < this.drones.length; i++) {
            if (netmsginput.readByte() != 1) {
                continue;
            }
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null) {
                this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), i);
            }
        }

    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (this.FM.AS.isMaster()) {
            if (i == 2) {
                this.typeDockableRequestDetach(this.drones[0], 0, true);
            }
            if ((i == 13) && (j == 0)) {
                this.nextDMGLevels(4, 1, "CF_D0", this);
                return true;
            }
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        if (this.FM instanceof Pilot) {
            ((Pilot) this.FM).setDumbTime(9999L);
        }
        if ((this.FM instanceof Maneuver) && (this.FM.EI.engines[0].getStage() == 6) && (this.FM.EI.engines[1].getStage() == 6)) {
            ((Maneuver) this.FM).set_maneuver(44);
            ((Maneuver) this.FM).setSpeedMode(-1);
        }
        this.FM.CT.bHasGearControl = !this.FM.Gears.onGround();
        super.update(f);
    }

    private Actor drones[] = { null };
    private Actor droneInitiator;

    static {
        Class class1 = JU_88MSTL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88MSTL/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88A-4Mistel.fmd");
        Aircraft.weaponTriggersRegister(class1, new int[] { 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Dockport0" });
    }
}
