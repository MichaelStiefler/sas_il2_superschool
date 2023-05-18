package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;

public class KB_29P extends KB_29 implements TypeTankerBoom, TypeDockable {

    public KB_29P() {
        this.bBoomExtended = true;
        this.maxSendRefuel = 32.026F;
        this.drones = new Actor[1];
        this.bEmpty = false;
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

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("none")) {
            this.bEmpty = true;
        }
    }

    public boolean isBoomExtended() {
        return this.bBoomExtended;
    }

    public void update(float f) {
        this.boomRefuel(f);
        super.update(f);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            KB_29P.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            KB_29P.bChangedPit = true;
        }
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
        if (actor instanceof Aircraft) {
            Aircraft aircraft = (Aircraft) actor;
            if (aircraft.FM.AS.isMaster() && (aircraft.FM.getSpeedKMH() > 10F) && (this.FM.getSpeedKMH() > 10F) && this.isBoomExtended()) {
                for (int i = 0; i < this.drones.length; i++) {
                    if (Actor.isValid(this.drones[i])) {
                        continue;
                    }
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    this.pos.getAbs(loc);
                    actor.pos.getAbs(loc1);
                    Loc loc2 = new Loc();
                    HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
                    hooknamed.computePos(this, loc, loc2);
                    Loc loc3 = new Loc();
                    HookNamed hooknamed1 = new HookNamed((ActorMesh) actor, "_Receptacle");
                    hooknamed1.computePos(actor, loc1, loc3);
                    if (loc2.getPoint().distance(loc3.getPoint()) >= 20D) {
                        continue;
                    }
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
        if ((i >= 0) && (i <= 1)) {
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
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            this.pos.getAbs(loc);
            actor.pos.getAbs(loc1);
            Loc loc2 = new Loc();
            HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
            hooknamed.computePos(this, loc, loc2);
            Loc loc3 = new Loc();
            HookNamed hooknamed1 = new HookNamed((ActorMesh) actor, "_Receptacle");
            hooknamed1.computePos(actor, loc1, loc3);
            Loc loc4 = new Loc();
            Loc loc5 = new Loc();
            loc4 = loc1;
            loc4.sub(loc3);
            loc5 = loc2;
            loc5.sub(loc);
            loc4.add(loc5);
            loc4.add(loc);
            actor.pos.setAbs(loc4);
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
            if (netmsginput.readByte() != 1) {
                continue;
            }
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null) {
                this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), i);
            }
        }

    }

    private void boomRefuel(float f) {
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        if (this.bEmpty || (this.FM.getAltitude() < 1000F) || (this.FM.CT.getGear() > 0.0F) || (f1 > 760F) || (f1 < 325F) || (this.FM.M.fuel < (this.FM.M.maxFuel * 0.05000000000000001D))) {
            this.hierMesh().chunkSetAngles("GearC9_D0", 0.0F, 0.0F, -61.5F);
            this.hierMesh().chunkSetAngles("GearC11_D0", 0.0F, -30F, 0.0F);
            this.hierMesh().chunkSetAngles("GearC12_D0", 0.0F, 30F, 0.0F);
            this.resetYPRmodifier();
            Aircraft.xyz[1] = -3.5F;
            this.hierMesh().chunkSetLocate("GearC10_D0", Aircraft.xyz, Aircraft.ypr);
            this.bBoomExtended = false;
            this.typeDockableAttemptDetach();
        } else {
            this.hierMesh().chunkSetAngles("GearC9_D0", 0.0F, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("GearC11_D0", 0.0F, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("GearC12_D0", 0.0F, 0.0F, 0.0F);
            this.resetYPRmodifier();
            this.hierMesh().chunkSetLocate("GearC10_D0", Aircraft.xyz, Aircraft.ypr);
            this.bBoomExtended = true;
        }
    }

    public final float requestRefuel(Aircraft aircraft, float f, float f1) {
        if (this.bBoomExtended && this.FM.AS.isMaster()) {
            for (int i = 0; i < this.drones.length; i++) {
                if (!Actor.isValid(this.drones[i]) || (this.drones[i] != aircraft)) {
                    continue;
                }
                if (f > this.maxSendRefuel) {
                    f = this.maxSendRefuel;
                }
                if (this.FM.M.requestFuel(f * f1)) {
                    return f * f1;
                }
            }

        }
        return 0.0F;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (this.FM.AS.isMaster()) {
            switch (i) {
                case 33:
                case 34:
                case 35:
                    this.typeDockableRequestDetach(this.drones[0], 0, true);
                    break;
            }
        }
        return super.cutFM(i, j, actor);
    }

    public static boolean bChangedPit = false;
    private boolean       bBoomExtended;
    private Actor         drones[];
    private float         maxSendRefuel;
    private boolean       bEmpty;

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
