package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.il2.ai.War;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.BombGunCBU24_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawk400gal_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawkNF_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawk_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU118_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU7_gn16;
import com.maddox.il2.objects.weapons.Pylon_Mk4HIPEGpod_gn16;
import com.maddox.il2.objects.weapons.Pylon_USMERfw_gn16;
import com.maddox.il2.objects.weapons.Pylon_USTER_gn16;
import com.maddox.il2.objects.weapons.RocketGunChaff_gn16;
import com.maddox.il2.objects.weapons.RocketGunFlare_gn16;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class SkyhawkA4E_tanker extends Skyhawk implements TypeCountermeasure, TypeDockable, TypeTankerDrogue {

    public SkyhawkA4E_tanker() {
        this.bFirstTime = true;
        this.bChangedPit = false;
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.counterFlareList = new ArrayList();
        this.counterChaffList = new ArrayList();
        this.bDrogueExtended = true;
        this.bInRefueling = false;
        this.maxSendRefuel = 10.093F;
        this.drones = new Actor[1];
        this.ratdeg = 0.0F;
        this.bEmpty = false;
    }

    public boolean isDrogueExtended() {
        return this.bDrogueExtended;
    }

    private void checkChangeWeaponColors() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] == null) {
                continue;
            }
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
                } else if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk_gn16) {
                    ((FuelTankGun_TankSkyhawk_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkNF_gn16) {
                    ((FuelTankGun_TankSkyhawkNF_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk400gal_gn16) {
                    ((FuelTankGun_TankSkyhawk400gal_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_Mk4HIPEGpod_gn16) {
                    ((Pylon_Mk4HIPEGpod_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                }
                if (this.FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16) {
                    this.counterFlareList.add(this.FM.CT.Weapons[i][j]);
                    continue;
                }
                if (this.FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16) {
                    this.counterChaffList.add(this.FM.CT.Weapons[i][j]);
                }
            }

        }

    }

    public void backFire() {
        if (this.counterFlareList.isEmpty()) {
            this.hasFlare = false;
        } else if (Time.current() > (this.lastFlareDeployed + 700L)) {
            ((RocketGunFlare_gn16) this.counterFlareList.get(0)).shots(1);
            this.hasFlare = true;
            this.lastFlareDeployed = Time.current();
            if (!((RocketGunFlare_gn16) this.counterFlareList.get(0)).haveBullets()) {
                this.counterFlareList.remove(0);
            }
        }
        if (this.counterChaffList.isEmpty()) {
            this.hasChaff = false;
        } else if (Time.current() > (this.lastChaffDeployed + 1300L)) {
            ((RocketGunChaff_gn16) this.counterChaffList.get(0)).shots(1);
            this.hasChaff = true;
            this.lastChaffDeployed = Time.current();
            if (!((RocketGunChaff_gn16) this.counterChaffList.get(0)).haveBullets()) {
                this.counterChaffList.remove(0);
            }
        }
    }

    public long getChaffDeployed() {
        if (this.hasChaff) {
            return this.lastChaffDeployed;
        } else {
            return 0L;
        }
    }

    public long getFlareDeployed() {
        if (this.hasFlare) {
            return this.lastFlareDeployed;
        } else {
            return 0L;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.bNoSpoiler = true;
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
        this.drogueRefuel();
        if (this.FM.getSpeedKMH() > 185F) {
            this.RATrot();
        }
        super.update(f);
        if (this.backfire) {
            this.backFire();
        }
    }

    public void missionStarting() {
        super.missionStarting();
        this.checkChangeWeaponColors();
        this.bFirstTime = true;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
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
        if ((i >= 0) && (i < 1) && this.bDrogueExtended) {
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

    private void drogueRefuel() {
        float f = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        Aircraft aircraft = War.GetNearestEnemyAircraft(this, 5000F, 9);
        Aircraft aircraft1 = War.GetNearestEnemyAircraft(this, 6000F, 9);
        if (this.bEmpty || ((this.FM.getAltitude() < 1000F) && (this.FM.getAltitude() != 0.0F) && !this.bFirstTime) || (this.FM.CT.getGear() > 0.0F) || (this.FM.CT.getArrestor() > 0.0F) || (f > 580F) || ((f < 325F) && (f != 0.0F) && !this.bFirstTime) || (this.FM.M.fuel < (this.FM.M.maxFuel * 0.2F)) || (aircraft != null)) {
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
        } else if (aircraft1 == null) {
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

    public boolean    bChangedPit;
    private boolean   hasChaff;
    private boolean   hasFlare;
    private long      lastChaffDeployed;
    private long      lastFlareDeployed;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;
    private boolean   bFirstTime;
    private boolean   bDrogueExtended;
    private boolean   bInRefueling;
    private Actor     drones[];
    private float     maxSendRefuel;
    private float     ratdeg;
    private boolean   bEmpty;

    static {
        Class class1 = SkyhawkA4E_tanker.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Skyhawk");
        Property.set(class1, "meshName", "3DO/Plane/SkyhawkA4E(Multi1)/hier_A4Etanker.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1962F);
        Property.set(class1, "yearExpired", 1982F);
        Property.set(class1, "FlightModel", "FlightModels/a4e.fmd:SKYHAWKS");
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 7 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb01", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_Bomb13", "_ExternalBomb14", "_Bomb15", "_ExternalBomb16", "_Bomb17", "_ExternalBomb18", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_Rock05",
                "_Rock06", "_Rock07", "_Rock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_ExternalRock43", "_ExternalRock43", "_ExternalRock29", "_ExternalRock29", "_ExternalRock30", "_ExternalRock30", "_ExternalRock31", "_ExternalRock31", "_ExternalRock32", "_ExternalRock32", "_ExternalRock33", "_ExternalRock33", "_ExternalRock34", "_ExternalRock34", "_ExternalRock35", "_ExternalRock35", "_ExternalRock36", "_ExternalRock36", "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_ExternalRock39", "_ExternalRock39", "_ExternalRock40", "_ExternalRock40", "_Rock41", "_Rock42", "_Bomb19", "_Bomb20", "_ExternalBomb21", "_ExternalBomb22", "_Bomb23", "_ExternalBomb24", "_ExternalBomb25", "_Bomb26", "_ExternalBomb27", "_ExternalBomb28", "_Flare01" });
    }
}
