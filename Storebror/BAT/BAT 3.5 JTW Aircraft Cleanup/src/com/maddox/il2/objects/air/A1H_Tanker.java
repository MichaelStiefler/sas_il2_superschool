package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;

public class A1H_Tanker extends AD_Tanker implements TypeDockable, TypeTankerDrogue {

    public A1H_Tanker() {
        this.bDrogueExtended = true;
        this.bInRefueling = false;
        this.maxSendRefuel = 9.588F;
        this.drones = new Actor[1];
        this.ratdeg = 0.0F;
        this.bEmpty = false;
    }

    public boolean isDrogueExtended() {
        return this.bDrogueExtended;
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
        if (this.FM.CT.getArrestor() > 0.2F) {
            this.calculateArrestor();
        }
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
                    if (loc2.getPoint().distance(loc3.getPoint()) >= 7.5D) {
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

    private void RATrot() {
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
        this.hierMesh().chunkSetAngles("D704_rat", 0.0F, 0.0F, this.ratdeg);
        if (this.FM.getSpeedKMH() > 300F) {
            this.hierMesh().chunkVisible("D704_rat_rot", true);
            this.hierMesh().chunkVisible("D704_rat", false);
        } else {
            this.hierMesh().chunkVisible("D704_rat_rot", false);
            this.hierMesh().chunkVisible("D704_rat", true);
        }
    }

    private void drogueRefuel(float f) {
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        if (this.bEmpty || (this.FM.getAltitude() < 1000F) || (this.FM.CT.getGear() > 0.0F) || (this.FM.CT.getArrestor() > 0.0F) || (f1 > 580F) || (f1 < 325F) || (this.FM.M.fuel < (this.FM.M.maxFuel * 0.2D))) {
            this.hierMesh().chunkVisible("D704_FuelLine1", false);
            this.hierMesh().chunkVisible("D704_Drogue1", false);
            this.hierMesh().chunkVisible("D704_Drogue1_Fold", true);
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
            this.hierMesh().chunkVisible("D704_FuelLine1", true);
            this.hierMesh().chunkVisible("D704_Drogue1", true);
            this.hierMesh().chunkVisible("D704_Drogue1_Fold", false);
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
                    continue;
                }
                if (this.bInRefueling) {
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

    private void calculateArrestor() {
        if (this.FM.Gears.arrestorVAngle != 0.0F) {
            float f = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            this.arrestor = (0.8F * this.arrestor) + (0.2F * f);
            this.moveArrestorHook(this.arrestor);
        } else {
            float f1 = (-33F * this.FM.Gears.arrestorVSink) / 57F;
            if ((f1 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            }
            if ((f1 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                f1 = 0.0F;
            }
            if (f1 > 0.2F) {
                f1 = 0.2F;
            }
            if (f1 > 0.0F) {
                this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f1));
            } else {
                this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f1));
            }
            if (this.arrestor < 0.0F) {
                this.arrestor = 0.0F;
            } else if (this.arrestor > 1.0F) {
                this.arrestor = 1.0F;
            }
            this.moveArrestorHook(this.arrestor);
        }
    }

    private boolean bDrogueExtended;
    private boolean bInRefueling;
    private Actor   drones[];
    private float   maxSendRefuel;
    private float   ratdeg;
    private boolean bEmpty;

    static {
        Class class1 = A1H_Tanker.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-1H");
        Property.set(class1, "meshName", "3DO/Plane/A1H_Tanker(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1968F);
        Property.set(class1, "FlightModel", "FlightModels/A1H.fmd");
        Property.set(class1, "LOSElevation", 1.0585F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev17", "_ExternalDev18" });
    }
}
