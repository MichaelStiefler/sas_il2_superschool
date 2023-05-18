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

public class A_6XYZ_tanker extends A_6 implements TypeDockable, TypeTankerDrogue {

    public A_6XYZ_tanker() {
        this.bDrogueExtended = true;
        this.bInRefueling = false;
        this.drones = new Actor[1];
        this.ratdeg = 0.0F;
        this.bEmpty = false;
    }

    public boolean isDrogueExtended() {
        return this.bDrogueExtended;
    }

    void checkChangeWeaponColors() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.M.maxFuel += 880F;
        this.FM.M.fuel += 880F;
        this.FM.M.massEmpty += 370F;
        this.FM.M.mass += 370F;
        this.FM.M.maxWeight += 1250F;
        if (this.thisWeaponsName.startsWith("none")) {
            this.bEmpty = true;
        }
    }

    public void update(float f) {
        this.drogueRefuel(f);
        if (this.FM.getSpeedKMH() > 185F) {
            this.RATrot();
        }
        super.update(f);
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
            if (aircraft.FM.AS.isMaster() && (aircraft.FM.getSpeedKMH() > 10F) && (this.FM.getSpeedKMH() > 10F) && this.isDrogueExtended()) {
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
                    HookNamed hooknamed1 = new HookNamed((ActorMesh) actor, "_Probe");
                    hooknamed1.computePos(actor, loc1, loc3);
                    if (loc2.getPoint().distance(loc3.getPoint()) >= 8D) {
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
            HookNamed hooknamed1 = new HookNamed((ActorMesh) actor, "_Probe");
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
            if (netmsginput.readByte() == 1) {
                NetObj netobj = netmsginput.readNetObj();
                if (netobj != null) {
                    this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), i);
                }
            }
        }

    }

    public void missionStarting() {
        super.missionStarting();
        this.checkChangeWeaponColors();
    }

    void RATrot() {
        if (this.FM.getSpeedKMH() < 250F) {
            this.ratdeg -= 10F;
        } else if (this.FM.getSpeedKMH() < 400F) {
            this.ratdeg -= 20F;
        } else if (this.FM.getSpeedKMH() < 550F) {
            this.ratdeg -= 25F;
        } else {
            this.ratdeg -= 31F;
        }
        if (this.ratdeg < 720F) {
            this.ratdeg += 1440F;
        }
    }

    void drogueRefuel(float f) {
    }

    void drogueRefuel(float f, String sFuelLine, String sDrogue, String sDrogueFold) {
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        if (this.bEmpty || (this.FM.getAltitude() < 1000F) || (this.FM.CT.getGear() > 0.0F) || (this.FM.CT.getArrestor() > 0.0F) || (f1 > 580F) || (f1 < 325F) || (this.FM.M.fuel < (this.FM.M.maxFuel * 0.2F))) {
            this.hierMesh().chunkVisible(sFuelLine, false);
            this.hierMesh().chunkVisible(sDrogue, false);
            this.hierMesh().chunkVisible(sDrogueFold, true);
            if (this.bDrogueExtended) {
                this.hierMesh().materialReplace("CYellowOff", "CYellowOff");
            }
            this.bDrogueExtended = false;
            this.typeDockableAttemptDetach();
            if (this.bInRefueling) {
                this.hierMesh().materialReplace("CGreenOff", "CGreenOff");
                this.bInRefueling = false;
            }
        } else {
            this.hierMesh().chunkVisible(sFuelLine, true);
            this.hierMesh().chunkVisible(sDrogue, true);
            this.hierMesh().chunkVisible(sDrogueFold, false);
            if (!this.bDrogueExtended && !this.bInRefueling) {
                this.hierMesh().materialReplace("CYellowOff", "CYellowOn");
            }
            this.bDrogueExtended = true;
        }
        if (this.bDrogueExtended && this.FM.AS.isMaster()) {
            for (int i = 0; i < this.drones.length; i++) {
                if (Actor.isValid(this.drones[i])) {
                    if (!this.bInRefueling) {
                        this.hierMesh().materialReplace("CGreenOff", "CGreenOn");
                        this.hierMesh().materialReplace("CYellowOff", "CYellowOff");
                        this.bInRefueling = true;
                    }
                } else if (this.bInRefueling) {
                    this.hierMesh().materialReplace("CGreenOff", "CGreenOff");
                    this.hierMesh().materialReplace("CYellowOff", "CYellowOn");
                    this.bInRefueling = false;
                }
            }

        }
    }

    public final float requestRefuel(Aircraft aircraft, float f, float f1) {
        if (this.bDrogueExtended && this.FM.AS.isMaster()) {
            for (int i = 0; i < this.drones.length; i++) {
                if (Actor.isValid(this.drones[i]) && (this.drones[i] == aircraft)) {
                    if (f > this.maxSendRefuel) {
                        f = this.maxSendRefuel;
                    }
                    if (this.FM.M.requestFuel(f * f1)) {
                        return f * f1;
                    }
                }
            }

        }
        return 0.0F;
    }

    private boolean bDrogueExtended;
    private boolean bInRefueling;
    private Actor   drones[];
    float           maxSendRefuel;
    private float   ratdeg;
    private boolean bEmpty;

    static int[]    triggers = { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 7, 8 };

    static String[] hooks    = { "_ExternalDev06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb01", "_Bomb06", "_ExternalBomb07", "_Bomb08", "_ExternalBomb09", "_Bomb10", "_ExternalBomb11", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_Bomb12", "_Bomb13", "_ExternalBomb14", "_ExternalBomb15", "_Bomb16", "_ExternalBomb17", "_ExternalBomb18", "_Bomb19NOUSE", "_ExternalBomb20", "_Bomb21", "_Bomb22NOUSE", "_ExternalBomb23", "_Bomb24", "_Bomb25", "_ExternalBomb26", "_ExternalBomb27", "_Bomb28", "_Bomb29", "_ExternalBomb30", "_ExternalBomb31", "_Bomb32", "_Bomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalBomb37", "_ExternalBomb38", "_ExternalBomb39",
            "_ExternalBomb40", "_ExternalBomb41", "_ExternalBomb42", "_ExternalBomb43", "_ExternalBomb44", "_ExternalBomb45", "_ExternalBomb46", "_ExternalBomb47", "_ExternalBomb48", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Rock29", "_Rock30", "_ExternalRock31", "_ExternalRock31", "_ExternalRock32", "_ExternalRock32", "_Rock33", "_Rock34", "_ExternalRock35", "_ExternalRock35", "_ExternalRock36", "_ExternalRock36", "_ExternalRock37",
            "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_Flare01", "_Chaff01" };

}
