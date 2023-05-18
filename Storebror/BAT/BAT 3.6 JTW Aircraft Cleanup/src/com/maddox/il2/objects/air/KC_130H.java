package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Regiment;
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

public class KC_130H extends C_130X implements TypeDockable, TypeTankerDrogue {

    public KC_130H() {
        this.bFirstTime = true;
        this.bDrogueExtended = true;
        this.maxSendRefuel = 10.093F;
        this.drones = new Actor[2];
        this.bEmpty = false;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "KC_";
    }

    public boolean isDrogueExtended() {
        return this.bDrogueExtended;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("none")) {
            this.bEmpty = true;
        }
    }

    public void missionStarting() {
        super.missionStarting();
        this.bWingBroken[0] = this.bWingBroken[1] = false;
        this.bFirstTime = true;
    }

    public void update(float f) {
        this.drogueRefuel();
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
            if (aircraft.FM.AS.isMaster() && (aircraft.FM.getSpeedKMH() > 100F) && (this.FM.getSpeedKMH() > 100F) && this.isDrogueExtended()) {
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
                        return;
                    } else {
                        this.FM.AS.netToMaster(32, i, 0, actor);
                        return;
                    }
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
        if (this.bFirstTime) {
            this.drogueRefuel();
        }
        if ((i >= 0) && (i <= 1) && this.bDrogueExtended) {
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
        switch (i) {
            default:
                break;

            case 19:
                this.killPilot(this, 4);
                break;

            case 33:
            case 34:
                if (this.FM.AS.isMaster()) {
                    this.typeDockableRequestDetach(this.drones[0], 0, true);
                    this.bWingBroken[0] = true;
                }
                break;

            case 36:
            case 37:
                if (this.FM.AS.isMaster()) {
                    this.typeDockableRequestDetach(this.drones[1], 0, true);
                    this.bWingBroken[1] = true;
                }
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

        for (int j = 0; j < 2; j++) {
            if (this.bWingBroken[j]) {
                this.hierMesh().chunkVisible("Rfp_Drogue" + (j + 1) + "_Fold", false);
                this.hierMesh().chunkVisible("Rfp_iGreen" + (j + 1), false);
                this.hierMesh().chunkVisible("Rfp_iRed" + (j + 1), false);
                this.hierMesh().chunkVisible("Rfp_iYellow" + (j + 1), false);
                this.hierMesh().chunkVisible("Rfp_FuelLine" + (j + 1), false);
                this.hierMesh().chunkVisible("Rfp_Drogue" + (j + 1), false);
            }
        }

    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            KC_130H.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            KC_130H.bChangedPit = true;
        }
    }

    private void drogueRefuel() {
        float f = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        if (this.bEmpty || ((this.FM.getAltitude() < 1000F) && (this.FM.getAltitude() != 0.0F) && !this.bFirstTime) || (this.FM.CT.getGear() > 0.0F) || (f > 580F) || ((f < 250F) && (f != 0.0F) && !this.bFirstTime) || (this.FM.M.fuel < (this.FM.M.maxFuel * 0.12F))) {
            this.hierMesh().chunkVisible("Rfp_Drogue1", false);
            this.hierMesh().chunkVisible("Rfp_Drogue2", false);
            this.hierMesh().chunkVisible("Rfp_FuelLine1", false);
            this.hierMesh().chunkVisible("Rfp_FuelLine2", false);
            if (!this.bWingBroken[0]) {
                this.hierMesh().chunkVisible("Rfp_Drogue1_Fold", true);
            }
            if (!this.bWingBroken[1]) {
                this.hierMesh().chunkVisible("Rfp_Drogue2_Fold", true);
            }
            if (this.bDrogueExtended) {
                this.hierMesh().materialReplace("CYellowOff1", "CYellowOff1");
                this.hierMesh().materialReplace("CYellowOff2", "CYellowOff2");
            }
            this.bDrogueExtended = false;
            this.typeDockableAttemptDetach();
            boolean flag = false;
            for (int j = 0; j < 2; j++) {
                if (this.bInRefueling[j]) {
                    this.bInRefueling[j] = false;
                    flag = true;
                }
            }

            if (flag) {
                this.hierMesh().materialReplace("CGreenOff1", "CGreenOff1");
            }
            this.hierMesh().materialReplace("CGreenOff2", "CGreenOff2");
        } else {
            if (!this.bWingBroken[0]) {
                this.hierMesh().chunkVisible("Rfp_Drogue1", true);
                this.hierMesh().chunkVisible("Rfp_FuelLine1", true);
            }
            if (!this.bWingBroken[1]) {
                this.hierMesh().chunkVisible("Rfp_Drogue2", true);
                this.hierMesh().chunkVisible("Rfp_FuelLine2", true);
            }
            this.hierMesh().chunkVisible("Rfp_Drogue1_Fold", false);
            this.hierMesh().chunkVisible("Rfp_Drogue2_Fold", false);
            if (!this.bDrogueExtended && !this.bInRefueling[0]) {
                this.hierMesh().materialReplace("CYellowOff1", "CYellowOn1");
                this.hierMesh().materialReplace("CYellowOff2", "CYellowOn2");
            }
            this.bDrogueExtended = true;
        }
        if (this.bDrogueExtended && this.FM.AS.isMaster()) {
            for (int i = 0; i < this.drones.length; i++) {
                if (Actor.isValid(this.drones[i])) {
                    if (!this.bInRefueling[i]) {
                        this.hierMesh().materialReplace("CGreenOff" + String.valueOf(i + 1), "CGreenOn" + String.valueOf(i + 1));
                        this.hierMesh().materialReplace("CYellowOff" + String.valueOf(i + 1), "CYellowOff" + String.valueOf(i + 1));
                        this.bInRefueling[i] = true;
                    }
                    continue;
                }
                if (this.bInRefueling[i]) {
                    this.hierMesh().materialReplace("CGreenOff" + String.valueOf(i + 1), "CGreenOff" + String.valueOf(i + 1));
                    this.hierMesh().materialReplace("CYellowOff" + String.valueOf(i + 1), "CYellowOn" + String.valueOf(i + 1));
                    this.bInRefueling[i] = false;
                }
            }

        }
        if (this.bFirstTime && ((this.FM.getAltitude() != 0.0F) || (f != 0.0F))) {
            this.bFirstTime = false;
        }
    }

    public final float requestRefuel(Aircraft aircraft, float f, float f1) {
        if (this.bDrogueExtended && this.FM.AS.isMaster()) {
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

    public static boolean bChangedPit    = false;
    private boolean       bFirstTime;
    private boolean       bDrogueExtended;
    private boolean       bInRefueling[] = { false, false };
    private Actor         drones[];
    private float         maxSendRefuel;
    private boolean       bEmpty;
    private boolean       bWingBroken[]  = { false, false };

    static {
        Class class1 = KC_130H.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "KC-130H");
        Property.set(class1, "meshName", "3DO/Plane/C-130/hierKC.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 2080F);
        Property.set(class1, "FlightModel", "FlightModels/LockheedC-130.fmd:C130_FM");
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 14, 3, 3, 3, 2, 2, 2, 2, 2, 2, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN11", "_MGUN11", "_BombSpawn01", "_BombSpawn02", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_Rock05", "_Rock06", "_InternalBomb01" });
    }
}
