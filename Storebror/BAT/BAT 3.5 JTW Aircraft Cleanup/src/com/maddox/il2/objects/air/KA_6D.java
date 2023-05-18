package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.BombGunCBU24_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawkNF_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawkU1_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawkV2_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU118_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU7_gn16;
import com.maddox.il2.objects.weapons.Pylon_USMERfw_gn16;
import com.maddox.il2.objects.weapons.Pylon_USTER_gn16;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;

public class KA_6D extends A_6 implements TypeDockable, TypeTankerDrogue {

    public KA_6D() {
        this.bDrogueExtended = true;
        this.bInRefueling = false;
        this.maxSendRefuel = 10.093F;
        this.drones = new Actor[1];
        this.bD704 = false;
        this.bUseD704 = false;
        this.ratdeg = 0.0F;
        this.bEmpty = false;
    }

    public boolean isDrogueExtended() {
        return this.bDrogueExtended;
    }

    private void checkChangeWeaponColors() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] != null) {
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j] instanceof Pylon_USTER_gn16) {
                        ((Pylon_USTER_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_USMERfw_gn16) {
                        ((Pylon_USMERfw_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU10_gn16) {
                        ((Pylon_LAU10_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16) {
                        ((Pylon_LAU10_Cap_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16) {
                        ((Pylon_LAU7_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16) {
                        ((Pylon_LAU118_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof BombGunCBU24_gn16) {
                        ((BombGunCBU24_gn16) this.FM.CT.Weapons[i][j]).matGray();
                    } else if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkU1_gn16) {
                        ((FuelTankGun_TankSkyhawkU1_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkNF_gn16) {
                        ((FuelTankGun_TankSkyhawkNF_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkV2_gn16) {
                        ((FuelTankGun_TankSkyhawkV2_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    }
                }

            }
        }

    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("D704")) {
            this.bD704 = true;
        }
        if (this.thisWeaponsName.startsWith("D704use")) {
            this.bUseD704 = true;
        }
        if (this.bD704) {
            this.FM.M.maxFuel += 880F;
            this.FM.M.fuel += 880F;
            this.FM.M.massEmpty += 370F;
            this.FM.M.mass += 370F;
            this.FM.M.maxWeight += 1250F;
            this.hierMesh().chunkVisible("D704_RefuelStore", true);
            this.hierMesh().chunkVisible("D704_Drogue1_Fold", true);
            this.hierMesh().chunkVisible("D704_rat", true);
            this.hierMesh().chunkVisible("D704_iGreen", true);
            this.hierMesh().chunkVisible("D704_iYellow", true);
        }
        if (this.thisWeaponsName.startsWith("none")) {
            this.bEmpty = true;
        }
    }

    public void update(float f) {
        this.drogueRefuel(f);
        if (this.bD704 && (this.FM.getSpeedKMH() > 185F)) {
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
            if (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster() && (aircraft.FM.getSpeedKMH() > 10F) && (this.FM.getSpeedKMH() > 10F) && this.isDrogueExtended()) {
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

    public void missionStarting() {
        super.missionStarting();
        this.checkChangeWeaponColors();
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
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
        if (this.bEmpty || (this.FM.getAltitude() < 1000F) || (this.FM.CT.getGear() > 0.0F) || (this.FM.CT.getArrestor() > 0.0F) || (f1 > 580F) || (f1 < 325F) || (this.FM.M.fuel < (this.FM.M.maxFuel * 0.2F))) {
            this.hierMesh().chunkVisible("KA_Drogue1", false);
            if (this.bUseD704) {
                this.hierMesh().chunkVisible("D704_FuelLine1", false);
                this.hierMesh().chunkVisible("D704_Drogue1_Fold", true);
                if (this.bDrogueExtended) {
                    this.hierMesh().materialReplace("CDYellowOff", "CDYellowOff");
                }
            } else {
                this.hierMesh().chunkVisible("KA_FuelLine1", false);
                this.hierMesh().chunkVisible("KA_Drogue1_Fold", true);
                if (this.bDrogueExtended) {
                    this.hierMesh().materialReplace("CYellowOff", "CYellowOff");
                }
            }
            this.bDrogueExtended = false;
            this.typeDockableAttemptDetach();
            if (this.bInRefueling) {
                if (this.bUseD704) {
                    this.hierMesh().materialReplace("CDGreenOff", "CDGreenOff");
                } else {
                    this.hierMesh().materialReplace("CGreenOff", "CGreenOff");
                }
                this.bInRefueling = false;
            }
        } else {
            this.hierMesh().chunkVisible("KA_Drogue1", true);
            if (this.bUseD704) {
                this.hierMesh().chunkVisible("D704_FuelLine1", true);
                this.hierMesh().chunkVisible("D704_Drogue1_Fold", false);
                if (!this.bDrogueExtended && !this.bInRefueling) {
                    this.hierMesh().materialReplace("CDYellowOff", "CDYellowOn");
                }
            } else {
                this.hierMesh().chunkVisible("KA_FuelLine1", true);
                this.hierMesh().chunkVisible("KA_Drogue1_Fold", false);
                if (!this.bDrogueExtended && !this.bInRefueling) {
                    this.hierMesh().materialReplace("CYellowOff", "CYellowOn");
                }
            }
            this.bDrogueExtended = true;
        }
        if (this.bDrogueExtended && this.FM.AS.isMaster()) {
            for (int i = 0; i < this.drones.length; i++) {
                if (Actor.isValid(this.drones[i])) {
                    if (!this.bInRefueling) {
                        if (this.bUseD704) {
                            this.hierMesh().materialReplace("CDGreenOff", "CDGreenOn");
                            this.hierMesh().materialReplace("CDYellowOff", "CDYellowOff");
                        } else {
                            this.hierMesh().materialReplace("CGreenOff", "CGreenOn");
                            this.hierMesh().materialReplace("CYellowOff", "CYellowOff");
                        }
                        this.bInRefueling = true;
                    }
                } else if (this.bInRefueling) {
                    if (this.bUseD704) {
                        this.hierMesh().materialReplace("CDGreenOff", "CDGreenOff");
                        this.hierMesh().materialReplace("CDYellowOff", "CDYellowOn");
                    } else {
                        this.hierMesh().materialReplace("CGreenOff", "CGreenOff");
                        this.hierMesh().materialReplace("CYellowOff", "CYellowOn");
                    }
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
    private float   maxSendRefuel;
    private float   ratdeg;
    private boolean bD704;
    private boolean bUseD704;
    private boolean bEmpty;

    static {
        Class class1 = KA_6D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "KA-6D");
        Property.set(class1, "meshName", "3DO/Plane/KA-6D/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1970F);
        Property.set(class1, "yearExpired", 1980F);
        Property.set(class1, "FlightModel", "FlightModels/A6A.fmd:A6");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA_6.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 7, 8 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_ExternalDev06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb01", "_Bomb06", "_ExternalBomb07", "_Bomb08", "_ExternalBomb09", "_Bomb10", "_ExternalBomb11", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_Bomb12", "_Bomb13", "_ExternalBomb14", "_ExternalBomb15", "_Bomb16", "_ExternalBomb17", "_ExternalBomb18", "_Bomb19NOUSE", "_ExternalBomb20", "_Bomb21", "_Bomb22NOUSE", "_ExternalBomb23", "_Bomb24", "_Bomb25", "_ExternalBomb26", "_ExternalBomb27", "_Bomb28", "_Bomb29", "_ExternalBomb30", "_ExternalBomb31", "_Bomb32", "_Bomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalBomb37", "_ExternalBomb38", "_ExternalBomb39",
                        "_ExternalBomb40", "_ExternalBomb41", "_ExternalBomb42", "_ExternalBomb43", "_ExternalBomb44", "_ExternalBomb45", "_ExternalBomb46", "_ExternalBomb47", "_ExternalBomb48", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Rock29", "_Rock30", "_ExternalRock31", "_ExternalRock31", "_ExternalRock32", "_ExternalRock32", "_Rock33", "_Rock34", "_ExternalRock35", "_ExternalRock35", "_ExternalRock36", "_ExternalRock36",
                        "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_Flare01", "_Chaff01" });
    }
}
