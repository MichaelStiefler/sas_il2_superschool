/* 4.10.1 class */
package com.maddox.il2.net;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.RenderContext;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.order.OrdersTree;
import com.maddox.il2.gui.GUIBriefing;
import com.maddox.il2.gui.GUINetServerCMission;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.ZutiSupportMethods_Objects;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.ZutiSupportMethods_Ships;
import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.MainWin32;
import com.maddox.rts.Message;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetChannelOutStream;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetHost;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.NetUpdate;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.sound.AudioDevice;
import com.maddox.util.HashMapExt;

public class NetServerParams extends NetObj implements NetUpdate {
    private static boolean  DEBUG               = false;

    public static final int MIN_SYNC_DELTA      = 4000;
    public static final int MAX_SYNC_DELTA      = 32000;
    public static final int MODE_DOGFIGHT       = 0;
    public static final int MODE_COOP           = 1;
    public static final int MODE_SINGLE         = 2;
    public static final int MODE_MASK           = 7;
    public static final int TYPE_LOCAL          = 0;
    public static final int TYPE_BBGC           = 16;
    public static final int TYPE_BBGC_DEMO      = 32;
    public static final int TYPE_GAMESPY        = 32;
    public static final int TYPE_USGS           = 48;
    public static final int TYPE_MASK           = 48;
    public static final int TYPE_SHIFT          = 4;
    public static final int PROTECTED           = 128;
    public static final int DEDICATED           = 8;
    public static final int SHOW_SPEED_BAR      = 4096;
    public static final int EXTRA_OCCLUSION     = 8192;
    public static final int MSG_UPDATE          = 0;
    public static final int MSG_COOP_ENTER      = 1;
    public static final int MSG_COOP_ENTER_ASK  = 2;
    public static final int MSG_SYNC            = 3;
    public static final int MSG_SYNC_ASK        = 4;
    public static final int MSG_SYNC_START      = 5;
    public static final int MSG_TIME            = 6;
    public static final int MSG_CHECK_BEGIN     = 8;
    public static final int MSG_CHECK_FIRST     = 8;
    public static final int MSG_CHECK_SECOND    = 9;
    public static final int MSG_CHECK_STEP      = 10;
    public static final int MSG_CHECK_END       = 10;
    private String          serverName;
    public String           serverDescription;
    private String          serverPassword;
    private NetHost         host;
    private int             flags               = 4096;
    private int             difficulty;
    private int             maxUsers;
    private int             autoLogDetail       = 3;
    private boolean         eventlogHouse       = false;
    private int             eventlogClient      = -1;
    public boolean          bNGEN               = false;
    public long             timeoutNGEN         = 0L;
    public boolean          bLandedNGEN         = false;
    private float           farMaxLagTime       = 10.0F;
    private float           nearMaxLagTime      = 2.0F;
    private float           cheaterWarningDelay = 10.0F;
    private int             cheaterWarningNum   = 3;
    private NetMsgFiltered  outMsgF;
    private int             syncStamp           = 0;
    private long            syncTime;
    private long            syncDelta;
    private boolean         bCheckStartSync     = false;
    private boolean         bDoSync             = false;

    public boolean          filterUserNames;
    public boolean          allowMorseAsText;

    // TODO: Modified by |ZUTI|: changed from private to protected
    // -----------------------------------------------------------
    protected long        serverDeltaTime            = 0L;
    protected long        serverDeltaTime_lastUpdate = 0L;
    protected static long timeofday                  = -1L;
    // -----------------------------------------------------------

    private long       serverClockOffset0 = 0L;
    private long       lastServerTime     = 0L;
    long               _lastCheckMaxLag   = -1L;
    private int        checkRuntime       = 0;
    private long       checkTimeUpdate    = 0L;
    private HashMapExt checkUsers;
    private int        checkPublicKey;
    private int        checkKey;
    private int        checkSecond2;

    private class CheckUser {
        public NetUser user;
        public int     state         = 8;
        public long    timeSended    = 0L;
        public Class   classAircraft = null;
        public int     publicKey     = 0;
        public int     diff;

        /**
         * This method gets executed on server side. Client side is executed in checkUserInput method
         *
         * @param i
         * @param netmsginput
         * @return
         * @throws IOException
         */
        public boolean checkInput(int i, NetMsgInput netmsginput) throws IOException {
            boolean flag = false;
            String timeoutMessage = "";
            switch (i) {
                // TODO: CRT Checks - Server Side
                case 8: // '\b'
                    if (NetServerParams.this.checkKey == 0) NetServerParams.this.checkKey = NetServerParams.this.checkFirst(NetServerParams.this.checkPublicKey);

                    int userKey = netmsginput.readInt();
                    flag = NetServerParams.this.checkKey == userKey;

                    if (NetServerParams.DEBUG) {
                        System.out.println("CRT=1 Server key: " + NetServerParams.this.checkKey + " vs CRT=1 user key " + userKey);
                        System.out.println("  Flag = " + flag + ", state = " + this.state);
                        System.out.println("-----------------------------------------");
                    }

                    if (flag) this.state++;
                    break;
                case 9: // '\t'
                    int j = 0;
                    timeoutMessage = "( CRT=1 Failed )";
                    // TODO: Edited by |ZUTI|
                    if (NetServerParams.this.checkRuntime == 2) {
                        j = this.publicKey;
                        timeoutMessage = "( CRT=2 Failed )";
                    }

                    int userCRT2key = netmsginput.readInt();
                    int serverCRT2key = NetServerParams.this.checkSecond(this.publicKey, j);
                    // Original Check - Changed to display value on Server
                    // flag = netmsginput.readInt() == checkSecond(publicKey, j);
                    flag = userCRT2key == serverCRT2key;

                    if (NetServerParams.DEBUG) System.out.println("CRT=2 Classes: Server key: " + serverCRT2key + " vs CRT=2 user key " + userCRT2key);

                    if (flag) {
                        // After class check client also sends methods check for those classes
                        userCRT2key = netmsginput.readInt();
                        if (NetServerParams.DEBUG) System.out.println("CRT=2 Methods: Server key: " + NetServerParams.this.checkSecond2 + " vs CRT=2 user key " + userCRT2key);
                        // Original Check - Changed to display value on Server
                        // flag = netmsginput.readInt() == checkSecond2;
                        flag = userCRT2key == NetServerParams.this.checkSecond2;
                        if (flag) System.out.println("CRT=" + NetServerParams.this.checkRuntime + " passed for connecting user!");
                    }

                    if (flag) this.state++;
                    break;
                case 10: // '\n'

                    // TODO: Added by |ZUTI|: some planes report this check failed of you select specific weapon loadouts... CRT1 and CRT2 are affected
                    // -----------------------------------------------------------------
                    /*
                     * flag = true; timeoutMessage = "Valid Plane Selected"; Aircraft aircraft = user.findAircraft(); classAircraft = aircraft.getClass(); break;
                     */
                    // -----------------------------------------------------------------
                    timeoutMessage = "( Plane Check Failed )";
                    Aircraft aircraft = this.user.findAircraft();
                    if (Actor.isValid(aircraft)) {
                        int k = Finger.incInt(this.publicKey, this.diff);
                        // 4.08
                        int readInt = netmsginput.readInt();
                        int fingerInt = (int) aircraft.finger(k);
                        flag = readInt == fingerInt;

                        if (DEBUG) System.out.println("AC Check for: " + aircraft.name() + ", readInt=" + readInt + ", fingerInt=" + fingerInt);

                        // 4.09
                        // --------------------------------------------------------------------------------------------
                        /*
                         * try { flag = (netmsginput.readInt() == ((int)aircraft.finger((long)k) + SFSInputStream.oo)); } catch(Exception ex) { flag = (netmsginput.readInt() == ((int)aircraft.finger((long)k))); }
                         */
                        // --------------------------------------------------------------------------------------------

                        if (flag) this.classAircraft = aircraft.getClass();
                        else {
                            this.classAircraft = null;
                            System.out.println("User (" + this.user.fullName() + ") Failed Plane Check (" + this.user.findAircraft().toString() + ")");
                        }
                    } else {
                        this.classAircraft = null;
                        flag = true;
                    }
                    break;
                default:
                    return false;
            }
            this.timeSended = 0L;
            if (!flag) {
                NetChannel netchannel = netmsginput.channel();
                if (!netchannel.isDestroying()) {
                    String s = timeoutMessage + "Timeout ";
                    s = s + (i - 8);
                    netchannel.destroy(s);
                }
            }
            return true;
        }

        public void checkUpdate(long l) {
            if (this.state <= 10) if (this.timeSended != 0L) {
                if (l >= this.timeSended + 150000L) {
                    NetChannel netchannel = this.user.masterChannel();
                    if (!netchannel.isDestroying()) {
                        String string = "Timeout. ";
                        // TODO: Modified by |ZUTI|
                        // string += state - 8;
                        string += "CRT" + (this.state - 7) + " failed?";
                        netchannel.destroy(string);
                    }
                }
            } else try {
                int i = 0;
                switch (this.state) {
                    case 8:
                        i = NetServerParams.this.checkPublicKey;
                        break;
                    case 9:
                        i = this.publicKey = (int) (Math.random() * 4.294967295E9);
                        break;
                    case 10: {
                        Aircraft aircraft = this.user.findAircraft();
                        if (Actor.isValid(aircraft) && !aircraft.getClass().equals(this.classAircraft)) {
                            i = this.publicKey = (int) (Math.random() * 4.294967295E9);
                            this.diff = World.cur().diffCur.get();
                        }
                        break;
                    }
                }
                if (i != 0) {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(this.state);
                    netmsgguaranted.writeNetObj(this.user);
                    netmsgguaranted.writeInt(i);
                    if (this.state == 9) if (NetServerParams.this.checkRuntime == 2) netmsgguaranted.writeInt(i);
                    else netmsgguaranted.writeInt(0);
                    NetServerParams.this.postTo(this.user.masterChannel(), netmsgguaranted);
                    this.timeSended = l;
                }
            } catch (Exception exception) {
                /* empty */
            }
        }

        public CheckUser(NetUser netuser) {
            this.user = netuser;
        }
    }

    static class SPAWN implements NetSpawn {
        public void netSpawn(int i, NetMsgInput netmsginput) {
            try {
                NetObj netobj = netmsginput.readNetObj();
                int i_2_ = netmsginput.readInt();
                int i_3_ = netmsginput.readInt();
                int i_4_ = netmsginput.readByte();
                String string = netmsginput.read255();
                NetServerParams netserverparams = new NetServerParams(netmsginput.channel(), i, (NetHost) netobj);
                netserverparams.flags = i_2_;
                netserverparams.difficulty = i_3_;
                netserverparams.maxUsers = i_4_;
                netserverparams.serverName = string;
                netserverparams.autoLogDetail = netmsginput.readByte();
                netserverparams.farMaxLagTime = netmsginput.readFloat();
                netserverparams.nearMaxLagTime = netmsginput.readFloat();
                netserverparams.cheaterWarningDelay = netmsginput.readFloat();
                netserverparams.cheaterWarningNum = netmsginput.readInt();

                if (!com.maddox.il2.net.NetMissionTrack.isPlaying() || com.maddox.il2.net.NetMissionTrack.playingOriginalVersion() > 102) {
                    netserverparams.filterUserNames = netmsginput.readBoolean();
                    netserverparams.allowMorseAsText = netmsginput.readBoolean();
                }

                if (netmsginput.channel() instanceof NetChannelInStream) {
                    netserverparams.difficulty = World.cur().diffCur.get();
                    if (netmsginput.available() >= 8) netserverparams.serverDeltaTime = netmsginput.readLong();
                    else netserverparams.serverDeltaTime = 0L;
                } else World.cur().diffCur.set(i_3_);
                netserverparams.synkExtraOcclusion();
                if (netmsginput.available() >= 4) netserverparams.eventlogClient = netmsginput.readInt();
                else netserverparams.eventlogClient = -1;
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }
    }

    public static long getServerTime() {
        // TODO: Added by |ZUTI|
        // -----------------------------------------------------------------------------
        if (Main.cur() != null && Main.cur().netServerParams != null && Main.cur().netServerParams.isDogfight()) if (NetMissionTrack.isPlaying() || Main.cur().netServerParams.isMirror() && !Time.isPaused()) {
            long tempServerTime = Time.current() + Main.cur().netServerParams.serverDeltaTime;
            // Never return time that is smaller than last reported time!!
            if (tempServerTime < ZUTI_LAST_SERVER_TIME) tempServerTime = ZUTI_LAST_SERVER_TIME;

            ZUTI_LAST_SERVER_TIME = tempServerTime;

            return ZUTI_LAST_SERVER_TIME;
        }

        if (NetMissionTrack.isPlaying()) {
            if (Main.cur() != null && Main.cur().netServerParams != null && Main.cur().netServerParams.isCoop()) {
                long l = Time.current() - Main.cur().netServerParams.serverDeltaTime;
                if (l < 0L) l = 0L;
                if (l > Main.cur().netServerParams.lastServerTime) Main.cur().netServerParams.lastServerTime = l;
                return Main.cur().netServerParams.lastServerTime;
            }
            return Time.current();
        }
        if (Main.cur() != null && Main.cur().netServerParams != null && Main.cur().netServerParams.isCoop() && Main.cur().netServerParams.isMirror() && !Time.isPaused() && Main.cur().netServerParams.serverClockOffset0 != 0L) {
            long l = Main.cur().netServerParams.masterChannel().remoteClockOffset();
            long l_5_ = Time.current() - (l - Main.cur().netServerParams.serverClockOffset0);
            if (l_5_ < 0L) l_5_ = 0L;
            if (l_5_ > Main.cur().netServerParams.lastServerTime) Main.cur().netServerParams.lastServerTime = l_5_;
            return Main.cur().netServerParams.lastServerTime;
        }
        return Time.current();
    }

    public NetHost host() {
        return this.host;
    }

    public boolean isDedicated() {
        return (this.flags & 0x8) != 0;
    }

    public boolean isBBGC() {
        return (this.flags & 0x30) == 16;
    }

    public boolean isGAMESPY() {
        return (this.flags & 0x30) == 32;
    }

    public boolean isUSGS() {
        return (this.flags & 0x30) == 48;
    }

    public int getType() {
        return this.flags & 0x30;
    }

    public void setType(int i) {
        this.flags = this.flags & ~0x30 | i & 0x30;
    }

    public boolean isDogfight() {
        return (this.flags & 0x7) == 0;
    }

    public boolean isCoop() {
        return (this.flags & 0x7) == 1;
    }

    public boolean isSingle() {
        return (this.flags & 0x7) == 2;
    }

    public void setMode(int i) {
        if (this.isMaster()) {
            this.flags = this.flags & ~0x7 | i & 0x7;
            this.mirrorsUpdate();
        }
    }

    public boolean isShowSpeedBar() {
        return (this.flags & 0x1000) != 0;
    }

    public void setShowSpeedBar(boolean bool) {
        if (this.isMaster() && bool != this.isShowSpeedBar()) {
            if (bool) this.flags |= 0x1000;
            else this.flags &= ~0x1000;
            this.mirrorsUpdate();
        }
    }

    public boolean isExtraOcclusion() {
        return (this.flags & 0x2000) != 0;
    }

    public void setExtraOcclusion(boolean bool) {
        if (this.isMaster() && bool != this.isExtraOcclusion()) {
            if (bool) this.flags |= 0x2000;
            else this.flags &= ~0x2000;
            this.synkExtraOcclusion();
            this.mirrorsUpdate();
        }
    }

    public void synkExtraOcclusion() {
        if (!this.isDedicated()) AudioDevice.setExtraOcclusion(this.isExtraOcclusion());
    }

    public int autoLogDetail() {
        return this.autoLogDetail;
    }

    public boolean eventlogHouse() {
        return this.eventlogHouse && this.isMaster();
    }

    public int eventlogClient() {
        return this.eventlogClient;
    }

    public float farMaxLagTime() {
        return this.farMaxLagTime;
    }

    public float nearMaxLagTime() {
        return this.nearMaxLagTime;
    }

    public float cheaterWarningDelay() {
        return this.cheaterWarningDelay;
    }

    public int cheaterWarningNum() {
        return this.cheaterWarningNum;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int i) {
        if (this.isMaster()) {
            this.difficulty = i;
            World.cur().diffCur.set(this.difficulty);
            this.setClouds();
            this.mirrorsUpdate();
        }
    }

    public String serverName() {
        return this.serverName;
    }

    public void setServerName(String string) {
        if (USGS.isUsed() && this.isMaster()) {
            this.serverName = USGS.room;
            if (this.serverName == null) this.serverName = "Server";
        } else if (Main.cur().netGameSpy != null) this.serverName = Main.cur().netGameSpy.roomName;
        else {
            this.serverName = string;
            this.mirrorsUpdate();
        }
    }

    public boolean isProtected() {
        return (this.flags & 0x80) != 0;
    }

    public String getPassword() {
        return this.serverPassword;
    }

    public void setPassword(String string) {
        this.serverPassword = string;
        if (this.serverPassword != null) this.flags |= 0x80;
        else this.flags &= ~0x80;
        this.mirrorsUpdate();
    }

    private void setClouds() {
        if (Config.isUSE_RENDER()) if (World.cur().diffCur.Clouds) {
            Main3D.cur3D().bDrawClouds = true;
            if (RenderContext.cfgSky.get() == 0) {
                RenderContext.cfgSky.set(1);
                RenderContext.cfgSky.apply();
                RenderContext.cfgSky.reset();
            }
        } else Main3D.cur3D().bDrawClouds = false;
    }

    public int getMaxUsers() {
        return this.maxUsers;
    }

    public void setMaxUsers(int i) {
        if (this.isMaster()) {
            this.maxUsers = i;
            this.mirrorsUpdate();
        }
    }

    private void mirrorsUpdate() {
        this.USGSupdate();
        if (Main.cur().netGameSpy != null) Main.cur().netGameSpy.sendStatechanged();
        if (this.isMirrored()) try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(0);
            netmsgguaranted.writeInt(this.flags);
            netmsgguaranted.writeInt(this.difficulty);
            netmsgguaranted.writeByte(this.maxUsers);
            netmsgguaranted.write255(this.serverName);
            this.post(netmsgguaranted);
        } catch (Exception exception) {
            NetObj.printDebug(exception);
        }
    }

    public void USGSupdate() {
        if (this.isMaster() && USGS.isUsed()) USGS.update();
    }

    public void doMissionCoopEnter() {
        if (this.isMaster()) {
            List list = NetEnv.hosts();
            if (list.size() == 0) {
                this.prepareHidenAircraft();
                this.startCoopGame();
                return;
            }
            for (int i = 0; i < list.size(); i++)
                ((NetUser) list.get(i)).syncCoopStart = -1;
            this.bCheckStartSync = true;
            this.syncTime = Time.currentReal() + 32000L;
        } else if (Main.cur().netMissionListener != null) Main.cur().netMissionListener.netMissionCoopEnter();
        if (this.isMirrored()) try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(1);
            netmsgguaranted.writeByte(this.syncStamp);
            this.post(netmsgguaranted);
        } catch (Exception exception) {
            NetObj.printDebug(exception);
        }
        if (!this.isMaster()) try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(2);
            netmsgguaranted.writeNetObj(NetEnv.host());
            this.postTo(this.masterChannel(), netmsgguaranted);
        } catch (Exception exception) {
            NetObj.printDebug(exception);
        }
    }

    public boolean netInput(NetMsgInput netmsginput) throws IOException {
        netmsginput.reset();
        byte i = netmsginput.readByte();
        switch (i) {
            case 0: {
                int i_6_ = netmsginput.readInt();
                int i_7_ = netmsginput.readInt();
                int i_8_ = netmsginput.readByte();
                this.serverName = netmsginput.read255();
                this.flags = i_6_;
                this.difficulty = i_7_;
                this.maxUsers = i_8_;
                World.cur().diffCur.set(this.difficulty);
                this.setClouds();
                this.synkExtraOcclusion();
                if (this.isMirrored()) this.post(new NetMsgGuaranted(netmsginput, 0));
                break;
            }
            case 1:
                this.syncStamp = netmsginput.readUnsignedByte();
                this.doMissionCoopEnter();
                break;
            case 2:
                if (this.isMaster()) {
                    NetUser netuser = (NetUser) netmsginput.readNetObj();
                    if (netuser != null) netuser.syncCoopStart = this.syncStamp;
                } else this.postTo(this.masterChannel(), new NetMsgGuaranted(netmsginput, 1));
                break;
            case 3: {
                int i_9_ = netmsginput.readUnsignedByte();
                int i_10_ = netmsginput.readInt();
                if (this.syncStamp != i_9_) {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(4);
                    netmsgguaranted.writeByte(i_9_);
                    netmsgguaranted.writeNetObj(NetEnv.host());
                    this.postTo(this.masterChannel(), netmsgguaranted);
                    this.syncStamp = i_9_;
                    this.syncTime = i_10_ + Message.currentRealTime();
                } else {
                    long l = i_10_ + Message.currentRealTime();
                    if (this.syncTime > l) this.syncTime = l;
                }
                if (this.isMirrored()) {
                    this.outMsgF.unLockAndClear();
                    this.outMsgF.writeByte(3);
                    this.outMsgF.writeByte(this.syncStamp);
                    this.outMsgF.writeInt((int) (this.syncTime - Time.currentReal()));
                    this.postReal(Time.currentReal(), this.outMsgF);
                }
                break;
            }
            case 4:
                if (this.isMaster()) {
                    int i_11_ = netmsginput.readUnsignedByte();
                    NetUser netuser = (NetUser) netmsginput.readNetObj();
                    if (netuser != null && i_11_ == this.syncStamp) {
                        netuser.syncCoopStart = this.syncStamp;
                        List list = NetEnv.hosts();
                        for (int i_12_ = 0; i_12_ < list.size(); i_12_++)
                            if (((NetUser) list.get(i_12_)).syncCoopStart != this.syncStamp) return true;
                        this.bDoSync = false;
                        this.doStartCoopGame();
                    }
                } else this.postTo(this.masterChannel(), new NetMsgGuaranted(netmsginput, 1));
                break;
            case 5:
                this.doStartCoopGame();
                break;
            case 6:
                this.serverDeltaTime = netmsginput.readLong();
                if (NetMissionTrack.isRecording()) try {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(6);
                    netmsgguaranted.writeLong(this.serverDeltaTime);
                    this.postTo(NetMissionTrack.netChannelOut(), netmsgguaranted);
                } catch (Exception exception) {
                    NetObj.printDebug(exception);
                }
                break;

            // TODO: ZUTI: Client side code
            // ----------------------------------------------------------------------------------
            case 7:
                // this message is received from server on DF game OR
                // when we are playing a recorded track!
                // Resync only if player plane is NOT on the deck. If it is... it might go kaboom because carrier might resync and its gear would get torn off
                if (GUIBriefing.ZUTI_IS_BRIEFING_ACTIVE || !ZutiSupportMethods_Ships.isAircraftOnDeck(World.getPlayerAircraft(), 15D)) {
                    // never have smaller delta or you'll get white stripes! - disabled because in case we only accept
                    // greater server deltas, our difference always grows.
                    this.zutiNewServerDeltaTime = netmsginput.readLong();
                    // System.out.println(" Client received Server time1: " + zutiNewServerDeltaTime);
                    this.zutiNewServerDeltaTime = ((NetUser) NetEnv.host()).ping / 2 + this.zutiNewServerDeltaTime - Time.current();
                    // if( zutiNewServerDeltaTime > serverDeltaTime )
                    this.serverDeltaTime = this.zutiNewServerDeltaTime;
                    // System.out.println(" Client received Server time2: " + (Time.current() + serverDeltaTime));
                }

                if (NetMissionTrack.isRecording()) try {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(7);
                    netmsgguaranted.writeLong(this.serverDeltaTime);
                    this.postTo(NetMissionTrack.netChannelOut(), netmsgguaranted);
                } catch (Exception exception) {
                    NetObj.printDebug(exception);
                }
                break;
            // ----------------------------------------------------------------------------------

            default:
                return this.checkInput(i, netmsginput);
        }
        return true;
    }

    public void netUpdate() {
        if (!NetMissionTrack.isPlaying()) {
            // TODO: ZUTI: Server side code
            // ----------------------------------------------------------------------------------
            if (this.isMaster()) {
                long l = Time.current();
                if (!this.zutiInitialTimeSyncDone || l > this.serverDeltaTime_lastUpdate + NetServerParams.ZUTI_RESYNC_INTERVAL) {
                    this.zutiInitialTimeSyncDone = true;
                    this.serverDeltaTime_lastUpdate = l;

                    try {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(7);
                        netmsgguaranted.writeLong(this.serverDeltaTime_lastUpdate);
                        this.post(netmsgguaranted);

                        // System.out.println(" Server reported time: " + serverDeltaTime_lastUpdate);
                    } catch (IOException ex) {
                        NetObj.printDebug(ex);
                    }
                }
            }
            // ----------------------------------------------------------------------------------

            this.doCheckMaxLag();

            if (this.isMaster()) this.checkUpdate();
        }
        if (this.isMirror() && this.isCoop() && !Time.isPaused() && NetMissionTrack.isRecording() && !NetMissionTrack.isPlaying()) {
            long l = Time.current();
            if (l > this.serverDeltaTime_lastUpdate + 3000L) {
                this.serverDeltaTime_lastUpdate = l;
                try {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(6);
                    long l_13_ = Main.cur().netServerParams.masterChannel().remoteClockOffset();
                    long l_14_ = l_13_ - Main.cur().netServerParams.serverClockOffset0;
                    netmsgguaranted.writeLong(l_14_);
                    this.postTo(NetMissionTrack.netChannelOut(), netmsgguaranted);
                } catch (Exception exception) {
                    NetObj.printDebug(exception);
                }
            }
        }
        // TODO: ZUTI: client side code
        // -----------------------------------------------------------------------------------
        if (this.isMirror() && this.isDogfight() && !Time.isPaused() && NetMissionTrack.isRecording() && !NetMissionTrack.isPlaying()) try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(7);
            netmsgguaranted.writeLong(Time.current() + Main.cur().netServerParams.serverDeltaTime);
            this.postTo(NetMissionTrack.netChannelOut(), netmsgguaranted);
        } catch (Exception exception) {
            NetObj.printDebug(exception);
        }

        if (this.bDoSync || this.bCheckStartSync) if (this.isMaster()) {
            if (this.bCheckStartSync) {
                List list = NetEnv.hosts();
                if (list.size() == 0) {
                    this.prepareHidenAircraft();
                    this.startCoopGame();
                    this.bCheckStartSync = false;
                    return;
                }
                if (Time.currentReal() > this.syncTime) for (int i = 0; i < list.size(); i++) {
                    NetUser netuser = (NetUser) list.get(i);
                    if (netuser.syncCoopStart != this.syncStamp) ((NetUser) NetEnv.host()).kick(netuser);
                }
                else for (int i = 0; i < list.size(); i++) {
                    NetUser netuser = (NetUser) list.get(i);
                    if (netuser.syncCoopStart != this.syncStamp) return;
                }
                this.syncStamp = this.syncStamp + 1 & 0xff;
                this.syncDelta = 4000L;
                this.syncTime = Time.currentReal() + this.syncDelta;
                this.bCheckStartSync = false;
                this.bDoSync = true;
            }
            if (NetEnv.hosts().size() == 0) {
                this.prepareHidenAircraft();
                this.startCoopGame();
                this.bDoSync = false;
            } else {
                if (Time.currentReal() > this.syncTime - this.syncDelta / 2L) if (this.syncDelta < 32000L) {
                    this.syncStamp = this.syncStamp + 1 & 0xff;
                    this.syncDelta *= 2L;
                    this.syncTime = Time.currentReal() + this.syncDelta;
                } else {
                    List list = NetEnv.hosts();
                    for (int i = 0; i < list.size(); i++) {
                        NetUser netuser = (NetUser) list.get(i);
                        if (netuser.syncCoopStart != this.syncStamp) ((NetUser) NetEnv.host()).kick(netuser);
                    }
                    this.bDoSync = false;
                    this.doStartCoopGame();
                    return;
                }
                try {
                    this.outMsgF.unLockAndClear();
                    this.outMsgF.writeByte(3);
                    this.outMsgF.writeByte(this.syncStamp);
                    this.outMsgF.writeInt((int) (this.syncTime - Time.currentReal()));
                    this.postReal(Time.currentReal(), this.outMsgF);
                } catch (Exception exception) {
                    NetObj.printDebug(exception);
                }
            }
        }
    }

    public void msgNetDelChannel(NetChannel netchannel) {
        this.netUpdate();
    }

    private void doStartCoopGame() {
        if (this.isMirrored()) try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(5);
            this.post(netmsgguaranted);
        } catch (Exception exception) {
            NetObj.printDebug(exception);
        }
        HUD.logCoopTimeStart(this.syncTime);
        new MsgAction(64, this.syncTime, this) {
            public void doAction(Object object) {
                if (object == Main.cur().netServerParams) {
                    NetServerParams.this.prepareHidenAircraft();
                    NetServerParams.this.startCoopGame();
                }
            }
        };
    }

    private void startCoopGame() {
        this.prepareOrdersTree();
        Mission.doMissionStarting();
        Time.setPause(false);
        AudioDevice.soundsOn();
        if (this.isMaster() && this.bNGEN) {
            if (this.timeoutNGEN > 0L) this.startTimeoutNGEN(this.timeoutNGEN);
            if (this.bLandedNGEN) this.startLandedNGEN(2000L);
        } else {
            if (this.masterChannel() != null) this.serverClockOffset0 = this.masterChannel().remoteClockOffset();
            else this.serverClockOffset0 = 0L;
            this.lastServerTime = 0L;
            this.serverDeltaTime_lastUpdate = -100000L;
        }
    }

    private void startTimeoutNGEN(long l) {
        new MsgAction(0, Time.current() + l, Mission.cur()) {
            public void doAction(Object object) {
                if (Mission.cur() == object && Mission.isPlaying()) if (Main.state().id() != 49) NetServerParams.this.startTimeoutNGEN(500L);
                else((GUINetServerCMission) Main.state()).tryExit();
            }
        };
    }

    private void startLandedNGEN(long l) {
        new MsgAction(0, Time.current() + l, Mission.cur()) {
            public void doAction(Object object) {
                if (Mission.cur() == object && Mission.isPlaying()) if (Main.state().id() != 49) NetServerParams.this.startLandedNGEN(2000L);
                else {
                    boolean bool = true;
                    for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                        Actor actor = (Actor) entry.getValue();
                        if (actor instanceof Aircraft && Actor.isAlive(actor)) {
                            Aircraft aircraft = (Aircraft) actor;
                            if (aircraft.isNetPlayer()) {
                                if (!aircraft.FM.isWasAirborne()) {
                                    bool = false;
                                    break;
                                }
                                if (!aircraft.FM.isStationedOnGround()) {
                                    bool = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (bool) ((GUINetServerCMission) Main.state()).tryExit();
                    else NetServerParams.this.startLandedNGEN(2000L);
                }
            }
        };
    }

    public void prepareHidenAircraft() {
        ArrayList arraylist = new ArrayList();
        for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (actor instanceof Aircraft && actor.name().charAt(0) == ' ') arraylist.add(actor);
        }
        for (int i = 0; i < arraylist.size(); i++) {
            Aircraft aircraft = (Aircraft) arraylist.get(i);
            String string = aircraft.name().substring(1);
            if (Actor.getByName(string) != null) aircraft.destroy();
            else {
                aircraft.setName(string);
                aircraft.collide(true);
                aircraft.restoreLinksInCoopWing();
            }
        }
        if (World.isPlayerGunner()) World.getPlayerGunner().getAircraft();
        if (!this.isMaster()) {
            for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (actor instanceof Aircraft) {
                    Aircraft aircraft = (Aircraft) actor;
                    if (!aircraft.isNetPlayer() && !aircraft.isNet()) arraylist.add(actor);
                }
            }
            for (int i = 0; i < arraylist.size(); i++) {
                Aircraft aircraft = (Aircraft) arraylist.get(i);
                aircraft.destroy();
            }
        }
    }

    // TODO: Modified by |ZUTI|: from private to protected
    protected void prepareOrdersTree() {
        if (World.isPlayerGunner()) World.getPlayerGunner().getAircraft();
        else((Main3D) Main.cur()).ordersTree.netMissionLoaded(World.getPlayerAircraft());
        if (!this.isMirror()) {
            List list = NetEnv.hosts();
            for (int i = 0; i < list.size(); i++) {
                NetUser netuser = (NetUser) list.get(i);
                netuser.ordersTree = new OrdersTree(false);
                netuser.ordersTree.netMissionLoaded(netuser.findAircraft());
            }
        }
    }

    private void doCheckMaxLag() {
        long l = Time.real();
        if (this._lastCheckMaxLag <= 0L || l - this._lastCheckMaxLag >= 1000L) {
            this._lastCheckMaxLag = l;
            if (Mission.isPlaying()) if (this.isMaster()) {
                List list = Engine.targets();
                int i = list.size();
                for (int i_20_ = 0; i_20_ < i; i_20_++) {
                    Actor actor = (Actor) list.get(i_20_);

                    // TODO: Add null check for "actor.net" to avoid exceptions here
                    if (actor instanceof Aircraft && Actor.isAlive(actor) && actor.net != null && !actor.net.isMaster()) {
                        NetUser netuser = ((Aircraft) actor).netUser();
                        if (netuser != null) {
                            if (netuser.netMaxLag == null) netuser.netMaxLag = new NetMaxLag();
                            netuser.netMaxLag.doServerCheck((Aircraft) actor);
                        }
                    }
                }
            } else {
                NetUser netuser = (NetUser) NetEnv.host();
                if (netuser.netMaxLag == null) netuser.netMaxLag = new NetMaxLag();
                netuser.netMaxLag.doClientCheck();
            }
        }
    }

    public void destroy() {
        super.destroy();
        this.bCheckStartSync = false;
        this.bDoSync = false;
        Main.cur().netServerParams = null;
    }

    // ZUTI: Server side constructor
    public NetServerParams() {
        super(null);
        this.checkUsers = new HashMapExt();
        this.checkPublicKey = 0;
        this.checkKey = 0;
        this.checkSecond2 = 0;
        this.host = NetEnv.host();
        this.serverName = this.host.shortName();
        Main.cur().netServerParams = this;
        this.outMsgF = new NetMsgFiltered();
        try {
            this.outMsgF.setIncludeTime(true);
        } catch (Exception exception) {}
        if (!Config.isUSE_RENDER()) this.flags |= 0x8;
        this.synkExtraOcclusion();
        this.autoLogDetail = Config.cur.ini.get("chat", "autoLogDetail", this.autoLogDetail, 0, 3);
        this.nearMaxLagTime = Config.cur.ini.get("MaxLag", "nearMaxLagTime", this.nearMaxLagTime, 0.1F, 30.0F);
        this.farMaxLagTime = Config.cur.ini.get("MaxLag", "farMaxLagTime", this.farMaxLagTime, this.nearMaxLagTime, 30.0F);
        this.cheaterWarningDelay = Config.cur.ini.get("MaxLag", "cheaterWarningDelay", this.cheaterWarningDelay, 1.0F, 30.0F);

        // TODO: Added by |ZTUI|
        // ------------------------------------------
        ZutiSupportMethods_Net.checkTimeOfDay();
        // ------------------------------------------

        this.cheaterWarningNum = Config.cur.ini.get("MaxLag", "cheaterWarningNum", this.cheaterWarningNum);
        this.checkRuntime = Config.cur.ini.get("NET", "checkRuntime", 0, 0, 2);
        this.eventlogHouse = Config.cur.ini.get("game", "eventlogHouse", false);
        this.eventlogClient = Config.cur.ini.get("game", "eventlogClient", -1);

        this.filterUserNames = false;

        this.filterUserNames = Config.cur.ini.get("NET", "filterUserNames", false);

        this.allowMorseAsText = true;
        this.allowMorseAsText = Config.cur.ini.get("NET", "allowMorseAsText", true);
    }

    // ZUTI: Client side constructor
    public NetServerParams(NetChannel netchannel, int i, NetHost nethost) {
        super(null, netchannel, i);

        // ZUTI: reset old time
        this.zutiNewServerDeltaTime = 0;
        this.zutiInitialTimeSyncDone = false;

        this.checkUsers = new HashMapExt();
        this.checkPublicKey = 0;
        this.checkKey = 0;
        this.checkSecond2 = 0;
        this.host = nethost;
        Main.cur().netServerParams = this;

        // TODO: Added by |ZTUI|
        // ------------------------------------------
        ZutiSupportMethods_Net.checkTimeOfDay();
        // ------------------------------------------

        this.filterUserNames = false;
        this.allowMorseAsText = true;

        this.outMsgF = new NetMsgFiltered();
        try {
            this.outMsgF.setIncludeTime(true);
        } catch (Exception exception) {}
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException {
        NetMsgSpawn netmsgspawn = new NetMsgSpawn(this);
        netmsgspawn.writeNetObj(this.host);
        netmsgspawn.writeInt(this.flags);
        netmsgspawn.writeInt(this.difficulty);
        netmsgspawn.writeByte(this.maxUsers);
        netmsgspawn.write255(this.serverName);
        netmsgspawn.writeByte(this.autoLogDetail);
        netmsgspawn.writeFloat(this.farMaxLagTime);
        netmsgspawn.writeFloat(this.nearMaxLagTime);
        netmsgspawn.writeFloat(this.cheaterWarningDelay);
        netmsgspawn.writeInt(this.cheaterWarningNum);

        netmsgspawn.writeBoolean(this.filterUserNames);
        netmsgspawn.writeBoolean(this.allowMorseAsText);

        if (netchannel instanceof NetChannelOutStream && this.isCoop()) if (NetMissionTrack.isPlaying()) netmsgspawn.writeLong(this.serverDeltaTime);
        else {
            long l = 0L;
            if (this.isMirror()) {
                long l_21_ = Main.cur().netServerParams.masterChannel().remoteClockOffset();
                l = l_21_ - Main.cur().netServerParams.serverClockOffset0;
            }
            netmsgspawn.writeLong(l);
        }
        netmsgspawn.writeInt(this.eventlogClient);
        return netmsgspawn;
    }

    private int checkFirst(int i) {
        if (i != 0) {
            long l = Finger.file(i, MainWin32.GetCDDrive("jvm.dll"), -1);
            l = Finger.file(l, MainWin32.GetCDDrive("java.dll"), -1);
            l = Finger.file(l, MainWin32.GetCDDrive("net.dll"), -1);
            l = Finger.file(l, MainWin32.GetCDDrive("verify.dll"), -1);
            l = Finger.file(l, MainWin32.GetCDDrive("zip.dll"), -1);
            l = Finger.file(l, "lib/rt.jar", -1);

            // Added with 4.09 official
            // -----------------------------------------------------------------------------------------------------------------------------------
            ArrayList arraylist = Main.cur().airClasses;
            for (int i_22_ = 0; i_22_ < arraylist.size(); i_22_++) {
                Class var_class = (Class) arraylist.get(i_22_);

                // TODO: Added by |ZUTI|: check that property string is not NULL!
                String propertyName = Property.stringValue(var_class, "FlightModel", null);
                if (propertyName != null) l = FlightModelMain.finger(l, propertyName);
            }

            l += timeofday;
            try {
                l = Statics.getShipsFile().finger(l);
            } catch (Exception ex) {}
            try {
                l = Statics.getTechnicsFile().finger(l);
            } catch (Exception ex) {}
            try {
                l = Statics.getBuildingsFile().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getChiefsFile().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getAirFile().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getStationaryFile().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getRocketsFile().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getVehiclesFile().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getShips1File().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getTechnics1File().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getBuildings1File().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getChiefs1File().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getAir1File().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getStationary1File().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getRockets1File().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getVehicles1File().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getShips2File().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getTechnics2File().finger(l);
            } catch (Exception ex) {}
            try {
                l = ZutiSupportMethods_Objects.getBuildings2File().finger(l);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG) System.out.println("Check sum before MODS check: " + l);
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File("MODS"), 0, 0);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG) System.out.println("Check sum after MODS check: " + l);
            String zutiLocalTime = "FILES";// "files" + PATH_SEPARATOR + "3do" + PATH_SEPARATOR + "plane";
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File(zutiLocalTime), 0, 0);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG) System.out.println("Check sum after " + zutiLocalTime + " check: " + l);
            zutiLocalTime = "#SAS";// "files" + PATH_SEPARATOR + "3do" + PATH_SEPARATOR + "cockpit";
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File(zutiLocalTime), 0, 0);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG) System.out.println("Check sum after " + zutiLocalTime + " check: " + l);
            zutiLocalTime = "#UP#";// "files" + PATH_SEPARATOR + "samples";
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File(zutiLocalTime), 0, 0);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG) System.out.println("Check sum after " + zutiLocalTime + " check: " + l);
            // zutiLocalTime = "files" + PATH_SEPARATOR + "presets";
            // try{l += Statics.getSize(new java.io.File(zutiLocalTime), 0, 0);}catch(Exception ex){}
            // System.out.println("Check sum after " + zutiLocalTime + " check: " + l);
            zutiLocalTime = "files" + PATH_SEPARATOR + "effects";
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File(zutiLocalTime), 0, 0);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG) System.out.println("Check sum after " + zutiLocalTime + " check: " + l);

            zutiLocalTime = "files" + PATH_SEPARATOR + "presets" + PATH_SEPARATOR + "sounds";
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File(zutiLocalTime), 0, 0);
            } catch (Exception ex) {}
            zutiLocalTime = "files" + PATH_SEPARATOR + "3do" + PATH_SEPARATOR + "effects";
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File(zutiLocalTime), 0, 0);
            } catch (Exception ex) {}
            // -----------------------------------------------------------------------------------------------------------------------------------

            i = (int) l;
        }
        return i;
    }

    private int checkSecond(int i, int j) {
        // TODO: Added |ZUTI| outputs
        if (NetServerParams.DEBUG) {
            System.out.println("=====================================================================");
            System.out.println("publicKey: " + new Integer(i).toString());
            System.out.println("checkSecond2: " + new Integer(j).toString());
        }

        this.checkSecond2 = j;
        try {
            ClassLoader classloader = this.getClass().getClassLoader();
            Field[] fields = ClassLoader.class.getDeclaredFields();
            Field field = null;
            for (int i_23_ = 0; i_23_ < fields.length; i_23_++)
                if ("classes".equals(fields[i_23_].getName())) {
                    field = fields[i_23_];
                    break;
                }

            Vector vector = (Vector) CLASS.field(classloader, field);
            int ignoredClasses = 0;
            int checkedClasses = 0;
            Class var_class = null;
            String varClass = null;

            for (int x = 0; x < vector.size(); x++) {
                var_class = (Class) vector.get(x);
                varClass = var_class.toString();
                // TODO: Altered by |ZUTI|: Do not check these classes
                if (varClass.indexOf(".builder.Pl") > -1 || varClass.indexOf(".builder.Builder") > -1 || varClass.indexOf(".builder.PathFind") > -1 || varClass.indexOf(".builder.PNodes") > -1 || varClass.indexOf(".builder.PathChief") > -1
                        || varClass.endsWith(".engine.Loc") || varClass.endsWith(".engine.Config") || varClass.indexOf(".game.AircraftHotKeys") > -1 || varClass.indexOf(".ScrShot") > -1 || varClass.indexOf(".gui.GUIQuick") > -1
                        || varClass.indexOf(".gui.GUINetNewClient") > -1 || varClass.indexOf(".gui.GUINetClient") > -1 || varClass.indexOf(".rts.TrackIRWin") > -1 || varClass.indexOf(".rts.TrackIR") > -1 || varClass.indexOf(".hotkey.HookPilot") > -1
                        || varClass.indexOf(".engine.Camera3D") > -1 || varClass.indexOf(".engine.GUIRenders") > -1 || varClass.indexOf(".engine.Renders") > -1 || varClass.indexOf("$") > -1 || ZutiSupportMethods_Net.ignoreClass(varClass)) {
                    // System.out.println(var_class.toString() + " ignored!");
                    ignoredClasses++;
                    continue;
                }

                checkedClasses++;
                fields = var_class.getDeclaredFields();

                if (fields != null) for (int i_25_ = 0; i_25_ < fields.length; i_25_++)
                    i = Finger.incInt(i, fields[i_25_].getName());
                Method[] methods = var_class.getDeclaredMethods();
                if (methods != null) for (int i_26_ = 0; i_26_ < methods.length; i_26_++)
                    i = Finger.incInt(i, methods[i_26_].getName());
                if (this.checkSecond2 != 0) j = CLASS.method(var_class, j);

                if (NetServerParams.DEBUG) {
                    System.out.println("var_class: " + var_class.toString());
                    System.out.println("i: " + new Integer(i).toString());
                    System.out.println("j: " + new Integer(j).toString());
                    System.out.println("---------------------------------------------------------------------");
                }
            }

            if (NetServerParams.DEBUG) {
                System.out.println("Checked classes: " + checkedClasses);
                System.out.println("Ignored classes: " + ignoredClasses);
                System.out.println("Sum classes: " + new Integer(vector.size()).toString());
                System.out.println("=====================================================================");
            }
        } catch (RuntimeException exception) {
            /* empty */
        } catch (Exception exception) {
            /* empty */
        }
        this.checkSecond2 = j;

        if (NetServerParams.DEBUG) {
            System.out.println("publicKey: " + new Integer(i).toString());
            System.out.println("checkSecond2: " + new Integer(this.checkSecond2).toString());
            System.out.println("=====================================================================");
        }

        return i;
    }

    // TODO: CRT Checks - Client Side
    private boolean CheckUserInput(int i, NetMsgInput netmsginput) throws IOException {
        int result;
        switch (i) {
            case 8:
                result = this.checkFirst(netmsginput.readInt());

                if (NetServerParams.DEBUG) System.out.println("CRT=1 sum = " + result);
                break;
            case 9:
                result = this.checkSecond(netmsginput.readInt(), netmsginput.readInt());

                if (NetServerParams.DEBUG) System.out.println("CRT=2 sum = " + result);
                break;
            case 10: {
                result = netmsginput.readInt();
                NetUser netuser = (NetUser) NetEnv.host();
                Aircraft aircraft = netuser.findAircraft();
                if (Actor.isValid(aircraft)) {
                    result = Finger.incInt(result, World.cur().diffCur.get());
                    // 4.08
                    result = (int) aircraft.finger(result);
                    // 4.09
                    // --------------------------------------------------------------------------------------------
                    /*
                     * try { i_27_ = (int) aircraft.finger((long) i_27_) + SFSInputStream.oo; } catch(Exception ex) { i_27_ = (int) aircraft.finger((long) i_27_); }
                     */
                    // --------------------------------------------------------------------------------------------
                }
                break;
            }
            default:
                return false;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(i);
        netmsgguaranted.writeNetObj(NetEnv.host());
        netmsgguaranted.writeInt(result);
        if (i == 9) netmsgguaranted.writeInt(this.checkSecond2);
        this.postTo(netmsginput.channel(), netmsgguaranted);
        return true;
    }

    private boolean checkInput(int i, NetMsgInput netmsginput) throws IOException {
        NetUser netuser = (NetUser) netmsginput.readNetObj();
        if (this.isMaster()) {
            CheckUser checkuser = (CheckUser) this.checkUsers.get(netuser);
            if (checkuser != null) return checkuser.checkInput(i, netmsginput);
        } else {
            if (NetEnv.host() == netuser) return this.CheckUserInput(i, netmsginput);
            netmsginput.reset();
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeMsg(netmsginput, 1);
            if (netmsginput.channel() == this.masterChannel()) this.postTo(netuser.masterChannel(), netmsgguaranted);
            else this.postTo(this.masterChannel(), netmsgguaranted);
            return true;
        }
        return false;
    }

    private void checkUpdate() {
        if (!this.isSingle()) {
            long l = Time.currentReal();
            if (l >= this.checkTimeUpdate) {
                this.checkTimeUpdate = l + 1000L;
                List list = NetEnv.hosts();
                int i = list.size();
                for (int i_29_ = 0; i_29_ < i; i_29_++) {
                    NetUser netuser = (NetUser) list.get(i_29_);
                    if (!this.checkUsers.containsKey(netuser)) this.checkUsers.put(netuser, new CheckUser(netuser));
                }
                if (i != this.checkUsers.size()) {
                    boolean bool;
                    do {
                        bool = false;
                        for (Map.Entry entry = this.checkUsers.nextEntry(null); entry != null; entry = this.checkUsers.nextEntry(entry)) {
                            NetUser netuser = (NetUser) entry.getKey();
                            if (netuser.isDestroyed()) {
                                this.checkUsers.remove(netuser);
                                bool = true;
                                break;
                            }
                        }
                    } while (bool);
                }
                for (Map.Entry entry = this.checkUsers.nextEntry(null); entry != null; entry = this.checkUsers.nextEntry(entry)) {
                    if (this.checkPublicKey == 0 && this.checkRuntime >= 1) this.checkPublicKey = (int) (Math.random() * 4.294967295E9);
                    CheckUser checkuser = (CheckUser) entry.getValue();
                    checkuser.checkUpdate(l);
                }
            }
        }
    }

    static {
        Spawn.add(NetServerParams.class, new SPAWN());
    }

    // TODO: |ZUTI| methods and variables
    // -------------------------------------------------------------
    private static String    PATH_SEPARATOR          = System.getProperty("file.separator");
    private long             zutiNewServerDeltaTime  = 0;
    protected boolean        zutiInitialTimeSyncDone = false;
    private final static int ZUTI_RESYNC_INTERVAL    = 2000;
    public static long       ZUTI_LAST_SERVER_TIME   = 0;
    // -----------------------------------------------------------------
}