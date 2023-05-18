package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;

public class TB_3_4M_34R_SPB extends TB_3 implements TypeDockable {

    public TB_3_4M_34R_SPB() {
        this.fSightCurAltitude = 300F;
        this.fSightCurSpeed = 50F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TB_3_4M_34R_SPB.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TB_3_4M_34R_SPB.bChangedPit = true;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        float f2 = Math.abs(f);
        switch (i) {
            default:
                break;

            case 0:
                if (f1 < -47F) {
                    f1 = -47F;
                    flag = false;
                }
                if (f1 > 47F) {
                    f1 = 47F;
                    flag = false;
                }
                if (f2 < 147F) {
                    if (f1 < ((0.5964912F * f2) - 117.6842F)) {
                        f1 = (0.5964912F * f2) - 117.6842F;
                        flag = false;
                    }
                } else if (f2 < 157F) {
                    if (f1 < ((0.3F * f2) - 74.1F)) {
                        f1 = (0.3F * f2) - 74.1F;
                        flag = false;
                    }
                } else if (f1 < ((0.2173913F * f2) - 61.13044F)) {
                    f1 = (0.2173913F * f2) - 61.13044F;
                    flag = false;
                }
                if (f2 >= 110F) {
                    if (f2 < 115F) {
                        if ((f1 < -5F) && (f1 > -20F)) {
                            flag = false;
                        }
                    } else if (f2 < 160F) {
                        if (f1 < -5F) {
                            flag = false;
                        }
                    } else if (f1 < 15F) {
                        flag = false;
                    }
                }
                break;

            case 1:
                if (f1 < -47F) {
                    f1 = -47F;
                    flag = false;
                }
                if (f1 > 47F) {
                    f1 = 47F;
                    flag = false;
                }
                if (f < -38F) {
                    if (f1 < -32F) {
                        f1 = -32F;
                        flag = false;
                    }
                } else if (f < -16F) {
                    if (f1 < ((0.5909091F * f) - 9.545455F)) {
                        f1 = (0.5909091F * f) - 9.545455F;
                        flag = false;
                    }
                } else if (f < 35F) {
                    if (f1 < -19F) {
                        f1 = -19F;
                        flag = false;
                    }
                } else if (f < 44F) {
                    if (f1 < ((-3.111111F * f) + 89.88889F)) {
                        f1 = (-3.111111F * f) + 89.88889F;
                        flag = false;
                    }
                } else if (f < 139F) {
                    if (f1 < -47F) {
                        f1 = -47F;
                        flag = false;
                    }
                } else if (f < 150F) {
                    if (f1 < ((1.363636F * f) - 236.5455F)) {
                        f1 = (1.363636F * f) - 236.5455F;
                        flag = false;
                    }
                } else if (f1 < -32F) {
                    f1 = -32F;
                    flag = false;
                }
                if (f < -175.7F) {
                    if (f1 < 80.8F) {
                        flag = false;
                    }
                    break;
                }
                if (f < -82F) {
                    if (f1 < -16F) {
                        flag = false;
                    }
                    break;
                }
                if (f < 24F) {
                    if (f1 < 0.0F) {
                        flag = false;
                    }
                    break;
                }
                if (f < 32F) {
                    if (f1 < -8.3F) {
                        flag = false;
                    }
                    break;
                }
                if (f < 80F) {
                    if (f1 < 0.0F) {
                        flag = false;
                    }
                    break;
                }
                if (f < 174F) {
                    if (f1 < ((0.5F * f) - 87F)) {
                        flag = false;
                    }
                    break;
                }
                if (f < 178.7F) {
                    if (f1 < 0.0F) {
                        flag = false;
                    }
                    break;
                }
                if (f1 < 80.8F) {
                    flag = false;
                }
                break;

            case 2:
                if (f1 < -47F) {
                    f1 = -47F;
                    flag = false;
                }
                if (f1 > 47F) {
                    f1 = 47F;
                    flag = false;
                }
                if (f < -90F) {
                    flag = false;
                }
                if (f > 90F) {
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            for (int i = 0; i < 4; i++) {
                if ((this.FM.AS.astateEngineStates[i] > 3) && (this.FM.EI.engines[i].getReadyness() < 0.1F)) {
                    this.FM.AS.repairEngine(i);
                }
            }

            for (int j = 0; j < 4; j++) {
                if ((this.FM.AS.astateTankStates[j] > 3) && (this.FM.AS.astatePilotStates[4] < 50F) && (this.FM.AS.astatePilotStates[7] < 50F) && (World.Rnd().nextFloat() < 0.1F)) {
                    this.FM.AS.repairTank(j);
                }
            }

        }
    }

    public void update(float f) {
        super.update(f);
        this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -this.FM.Gears.gWheelAngles[0], 0.0F);
        this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, -this.FM.Gears.gWheelAngles[1], 0.0F);
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if ((explosion.chunkName != null) && (explosion.chunkName.startsWith("Wing") || explosion.chunkName.startsWith("Tail")) && explosion.chunkName.endsWith("D3") && (explosion.power < 0.014F)) {
            return;
        } else {
            super.msgExplosion(explosion);
            return;
        }
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 50F;
        if (this.fSightCurAltitude > 5000F) {
            this.fSightCurAltitude = 5000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 50F;
        if (this.fSightCurAltitude < 300F) {
            this.fSightCurAltitude = 300F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 5F;
        if (this.fSightCurSpeed > 350F) {
            this.fSightCurSpeed = 350F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 5F;
        if (this.fSightCurSpeed < 50F) {
            this.fSightCurSpeed = 50F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
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
        if (!(actor instanceof Aircraft)) {
            return;
        }
        Aircraft aircraft = (Aircraft) actor;
        if (aircraft.FM.AS.isMaster() && aircraft.FM.Gears.onGround() && (aircraft.FM.getSpeedKMH() < 10F) && (this.FM.getSpeedKMH() < 10F)) {
            for (int i = 0; i < this.drones.length; i++) {
                if (Actor.isValid(this.drones[i])) {
                    continue;
                }
                HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
                Loc loc = new Loc();
                Loc loc1 = new Loc();
                this.pos.getAbs(loc1);
                hooknamed.computePos(this, loc1, loc);
                actor.pos.getAbs(loc1);
                if (loc.getPoint().distance(loc1.getPoint()) >= 5D) {
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
        if ((i < 0) || (i > 1)) {
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
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    private Actor         drones[]    = { null, null };

    static {
        Class class1 = TB_3_4M_34R_SPB.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "TB-3");
        Property.set(class1, "meshName", "3DO/Plane/TB-3-4M-34R_SPB/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/TB-3-4M-34R.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTB_3.class, CockpitTB_3_Bombardier2.class, CockpitTB_3_NGunner.class, CockpitTB_3_TGunner3.class, CockpitTB_3_RGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_BombSpawn01", "_BombSpawn02" });
    }
}
