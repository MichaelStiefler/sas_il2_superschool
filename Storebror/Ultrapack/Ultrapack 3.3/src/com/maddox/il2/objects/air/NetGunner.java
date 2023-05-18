/* 4.10.1 class */
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Message;
import com.maddox.rts.MsgInvokeMethod_Object;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.NetUpdate;
import com.maddox.rts.ObjState;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.util.IntHashtable;

public class NetGunner extends com.maddox.il2.engine.Actor {
    private com.maddox.il2.net.NetUser   user;
    private java.lang.String             aircraftName;
    private Aircraft                     aircraft;
    private com.maddox.util.IntHashtable filterTable;
    protected int                        netCockpitIndxPilot;
    protected int                        netCockpitWeaponControlNum;
    protected int                        netCockpitTuretNum;
    protected boolean                    netCockpitValid;
    protected NetMsgGuaranted            netCockpitMsg;
    private boolean                      bFirstAirCheck;

    static class SPAWN implements NetSpawn {
        public void netSpawn(int i, NetMsgInput netmsginput) {
            try {
                java.lang.String aircraftName = netmsginput.read255();
                com.maddox.il2.net.NetUser netuser = (com.maddox.il2.net.NetUser) netmsginput.readNetObj();
                int cockpitNum = netmsginput.readUnsignedByte();
                if (netuser != null) new NetGunner(aircraftName, netuser, i, cockpitNum);

                // TODO: Added by |ZUTI|
                // ----------------------------------------------------------------
                /*
                 * System.out.println("Current gunner army: " + ZutiSupportMethods.getNetUser(netuser.uniqueName()).getArmy()); System.out.println("  Resolved object army: " + netuser.getArmy());
                 */
                Actor actor = Actor.getByName(aircraftName);
                if (actor != null) {
                    if (actor.equals(World.getPlayerAircraft())) {
                        HUD.log("mds.netCommand.crewJoin", new java.lang.Object[] { netuser.uniqueName(), new Integer(Math.round(cockpitNum)) });
                        System.out.println("Gunner AC=" + aircraftName + ", player AC=" + World.getPlayerAircraft().name());
                    }

                    netuser.setArmy(actor.getArmy());
                    System.out.println("NetGunner - synced >" + netuser.uniqueName() + "< army designation to >" + netuser.getArmy() + "<.");
                }
                /*
                 * System.out.println("Aircraft >" + aircraftName + "< army: " + actor.getArmy()); System.out.println("--------------------------------------------------");
                 */
                // ----------------------------------------------------------------
            } catch (java.lang.Exception exception) {
                java.lang.System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        SPAWN() {
        }
    }

    class Mirror extends com.maddox.il2.engine.ActorNet implements NetUpdate {

        public void netUpdate() {
            if (com.maddox.il2.engine.Actor.isValid(NetGunner.this.aircraft) && NetGunner.this.netCockpitTuretNum >= 0 && Time.current() - this.lastUpdateTime > 2000L)
                NetGunner.this.aircraft.FM.CT.WeaponControl[NetGunner.this.netCockpitWeaponControlNum] = false;
        }

        public boolean netInput(NetMsgInput netmsginput) throws java.io.IOException {
            if (netmsginput.isGuaranted()) return false;
            if (this.isMirrored()) {
                this.out.unLockAndSet(netmsginput, 0);
                this.postReal(Message.currentTime(true), this.out);
            }
            if (NetGunner.this.checkAircraft() && NetGunner.this.netCockpitTuretNum >= 0) {
                int i = netmsginput.readUnsignedShort();
                int j = netmsginput.readUnsignedShort();
                float f = NetGunner.this.unpackSY(i);
                float f1 = NetGunner.this.unpackSP(j & 0x7fff);
                NetGunner.this.aircraft.FM.CT.WeaponControl[NetGunner.this.netCockpitWeaponControlNum] = (j & 0x8000) != 0;
                if (com.maddox.il2.net.NetMissionTrack.isPlaying() && NetGunner.this.aircraft == com.maddox.il2.ai.World.getPlayerAircraft()) {
                    com.maddox.il2.engine.Actor._tmpOrient.set(f, f1, 0.0F);
                    ((CockpitGunner) com.maddox.il2.game.Main3D.cur3D().cockpits[NetGunner.this.getCockpitNum()]).moveGun(com.maddox.il2.engine.Actor._tmpOrient);
                } else {
                    com.maddox.il2.fm.Turret turret = NetGunner.this.aircraft.FM.turret[NetGunner.this.netCockpitTuretNum];
                    turret.tu[0] = f;
                    turret.tu[1] = f1;
                }
                this.lastUpdateTime = Time.current();
            }
            return true;
        }

        NetMsgFiltered out;
        long           lastUpdateTime;

        public Mirror(com.maddox.il2.engine.Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
            this.out = new NetMsgFiltered();
            this.lastUpdateTime = Time.current();
            try {
                this.out.setFilterArg(actor);
            } catch (java.lang.Exception exception) {}
        }
    }

    class Master extends com.maddox.il2.engine.ActorNet implements NetUpdate {

        public boolean netInput(NetMsgInput netmsginput) throws java.io.IOException {
            return false;
        }

        public void netUpdate() {
            if (!com.maddox.il2.engine.Actor.isValid(NetGunner.this.aircraft)) {
                NetGunner.this.checkAircraft();
                return;
            }
            if (NetGunner.this.netCockpitValid && NetGunner.this.netCockpitTuretNum >= 0) try {
                // System.out.println("Gunner master sending data...");

                com.maddox.il2.fm.Turret turret = NetGunner.this.aircraft.FM.turret[NetGunner.this.netCockpitTuretNum];
                boolean flag = NetGunner.this.aircraft.FM.CT.WeaponControl[NetGunner.this.netCockpitWeaponControlNum];
                this.out.unLockAndClear();
                this.out.writeShort(NetGunner.this.packSY(turret.tu[0]));
                this.out.writeShort(NetGunner.this.packSP(turret.tu[1]) | (flag ? 0x8000 : 0));
                this.post(Time.current(), this.out);
            } catch (java.lang.Exception exception) {
                NetObj.printDebug(exception);
            }
        }

        NetMsgFiltered out;

        public Master(com.maddox.il2.engine.Actor actor) {
            super(actor);
            this.out = new NetMsgFiltered();
            try {
                this.out.setFilterArg(actor);
            } catch (java.lang.Exception exception) {}
        }
    }

    public com.maddox.util.IntHashtable getFilterTable() {
        if (this.filterTable == null) this.filterTable = new IntHashtable();
        return this.filterTable;
    }

    public java.lang.String getAircraftName() {
        return this.aircraftName;
    }

    public com.maddox.il2.net.NetUser getUser() {
        return this.user;
    }

    public Aircraft getAircraft() {
        this.checkAircraft();
        return this.aircraft;
    }

    public int getCockpitNum() {
        return this.netCockpitIndxPilot;
    }

    private boolean isMirroredAsAir() {
        if (this.aircraft.net == null) return false;
        int i = this.aircraft.net.countMirrors();
        if (this.aircraft.net.isMirror()) i++;
        int j = this.net.countMirrors();
        if (this.net.isMirror()) j++;
        return i == j;
    }

    private boolean checkAircraft() {
        if (com.maddox.il2.engine.Actor.isValid(this.aircraft)) return this.isMirroredAsAir();
        this.aircraft = (Aircraft) com.maddox.il2.engine.Actor.getByName(this.aircraftName);
        if (!com.maddox.il2.engine.Actor.isValid(this.aircraft)) return false;
        if (!this.isMirroredAsAir()) {
            this.aircraft = null;
            return false;
        }
        this.pos.setBase(this.aircraft, null, false);
        this.pos.resetAsBase();
        // TODO: Storebror: Debugging Army switch on gunner pos switch bug
//        System.out.println("### GUNNER POS SWITCH BUG DEBUG: NetGunner.checkAircraft aircraft.getArmy()=" + aircraft.getArmy() + ", getArmy()=" + getArmy());
        // ...
        this.setArmy(this.aircraft.getArmy());
        // TODO: Storebror: Debugging Army switch on gunner pos switch bug
        this.user.setArmy(this.getArmy()); // Original
//        user.setArmy(aircraft.getArmy()); // Test Modified
        this.setOwner(this.aircraft);
        if (this.isNetMaster() || this.user.isTrackWriter()) {
            com.maddox.il2.ai.World.cur().resetUser();
            com.maddox.il2.ai.World.setPlayerAircraft(this.aircraft);
            com.maddox.il2.ai.World.setPlayerFM();
            com.maddox.il2.ai.World.setPlayerRegiment();
            this.aircraft.createCockpits();
            try {
                CockpitGunner cockpitgunner = (CockpitGunner) com.maddox.il2.game.Main3D.cur3D().cockpits[this.getCockpitNum()];
                com.maddox.il2.game.Main3D.cur3D().cockpitCur = cockpitgunner;
                this.aircraft.FM.AS.astatePlayerIndex = cockpitgunner.astatePilotIndx();
                if (!this.user.isTrackWriter()) this.aircraft.netCockpitEnter(this, this.getCockpitNum(), this.bFirstAirCheck);
                this.bFirstAirCheck = false;
                com.maddox.il2.game.Main3D.cur3D().cockpitCur.focusEnter();
                if (!this.user.isTrackWriter()) {
                    // Original
                    cockpitgunner.setRealMode(true);
                    this.aircraft.netCockpitAuto(this, this.getCockpitNum(), false);

                    // TODO: Added by |ZUTI|
                    // ----------------------------------------------------------------------
                    this.aircraft.netCockpitAuto(World.getPlayerGunner(), this.getCockpitNum(), true);
                    // ----------------------------------------------------------------------
                }
            } catch (Exception ex) {
                // TODO: Added by |ZUTI|: bombardier requires net gunner!
                CockpitPilot cockpitgunner = (CockpitPilot) com.maddox.il2.game.Main3D.cur3D().cockpits[this.getCockpitNum()];
                com.maddox.il2.game.Main3D.cur3D().cockpitCur = cockpitgunner;
                this.aircraft.FM.AS.astatePlayerIndex = cockpitgunner.astatePilotIndx();
                if (!this.user.isTrackWriter()) this.aircraft.netCockpitEnter(this, this.getCockpitNum(), this.bFirstAirCheck);
                this.bFirstAirCheck = false;
                com.maddox.il2.game.Main3D.cur3D().cockpitCur.focusEnter();
                if (!this.user.isTrackWriter()) {
                    // Original
                    this.aircraft.netCockpitAuto(this, this.getCockpitNum(), false);

                    // TODO: Added by |ZUTI|
                    // ----------------------------------------------------------------------
                    this.aircraft.netCockpitAuto(World.getPlayerGunner(), this.getCockpitNum(), true);
                    // ----------------------------------------------------------------------
                }
            }
        }
        this.user.tryPreparePilot(this.aircraft, this.aircraft.netCockpitAstatePilotIndx(this.getCockpitNum()));
        return true;
    }

    public void netFirstUpdate(NetChannel netchannel) throws java.io.IOException {
        this.doNetFirstUpdate(netchannel);
    }

    public void doNetFirstUpdate(java.lang.Object obj) {
        if (this.isDestroyed()) return;
        NetChannel netchannel = (NetChannel) obj;
        if (netchannel.isDestroyed()) return;
        if (!this.checkAircraft() || !netchannel.isMirrored(this.aircraft.net)) if (com.maddox.il2.engine.Actor.isValid(this.aircraft) && this.aircraft.net.masterChannel() == netchannel) return;
        else {
            new MsgInvokeMethod_Object("doNetFirstUpdate", netchannel).post(72, this, 0.0D);
            return;
        }
        try {
            this.aircraft.netCockpitFirstUpdate(this, netchannel);
        } catch (java.lang.Exception exception) {
            java.lang.System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    int packSY(float f) {
        return 0xffff & (int) ((f % 360D + 180D) * 65000D / 360D);
    }

    int packSP(float f) {
        return 0x7fff & (int) ((f % 360D + 180D) * 32000D / 360D);
    }

    float unpackSY(int i) {
        return (float) (i * 360D / 65000D - 180D);
    }

    float unpackSP(int i) {
        return (float) (i * 360D / 32000D - 180D);
    }

    public NetGunner(java.lang.String s, com.maddox.il2.net.NetUser netuser, int i, int j) {
        this.netCockpitIndxPilot = 0;
        this.netCockpitWeaponControlNum = 0;
        this.netCockpitTuretNum = -1;
        this.netCockpitValid = false;
        this.netCockpitMsg = null;
        this.bFirstAirCheck = true;
        this.aircraftName = s;
        this.user = netuser;
        this.netCockpitIndxPilot = j;
        java.lang.String s1 = " " + s + "(" + j + ")";
        ObjState.destroy(com.maddox.il2.engine.Actor.getByName(s1));
        this.setName(s1);
        this.pos = new ActorPosMove(this);
        if (netuser.isMaster()) this.net = new Master(this);
        else this.net = new Mirror(this, netuser.masterChannel(), i);
        if (netuser.isMaster() || netuser.isTrackWriter()) com.maddox.il2.ai.World.setPlayerGunner(this);
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel) throws java.io.IOException {
        NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
        netmsgspawn.write255(this.aircraftName);
        netmsgspawn.writeNetObj(this.user);
        netmsgspawn.writeByte(this.getCockpitNum());
        return netmsgspawn;
    }

    static {
        Spawn.add(NetGunner.class, new SPAWN());
    }

    // TODO: |ZUTI| methods and variables
    // -----------------------------------------------
    /**
     * Get current cockpit number that gunner is occupying.
     *
     * @return
     */
    public int zutiGetCockpitNum() {
        return this.netCockpitIndxPilot;
    }

    /**
     * Call this method when you receive information about player ejecting or changing cockpit position.
     *
     * @param ac
     * @param cockpitNum
     */
    public void zutiSetAircraftAndCockpitNum(String acName, int cockpitNum) {
        this.aircraft = (Aircraft) Actor.getByName(acName);

        if (this.aircraft == null) {
            this.netCockpitWeaponControlNum = -1;
            this.netCockpitTuretNum = -1;
            this.aircraftName = "";
            this.netCockpitIndxPilot = -1;
        } else {
            this.aircraftName = this.aircraft.name();
            this.netCockpitIndxPilot = cockpitNum;
        }

    }
    // -----------------------------------------------
}
