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
    private static boolean  DEBUG                      = false;

    public static final int MIN_SYNC_DELTA             = 4000;
    public static final int MAX_SYNC_DELTA             = 32000;
    public static final int MODE_DOGFIGHT              = 0;
    public static final int MODE_COOP                  = 1;
    public static final int MODE_SINGLE                = 2;
    public static final int MODE_MASK                  = 7;
    public static final int TYPE_LOCAL                 = 0;
    public static final int TYPE_BBGC                  = 16;
    public static final int TYPE_BBGC_DEMO             = 32;
    public static final int TYPE_GAMESPY               = 32;
    public static final int TYPE_USGS                  = 48;
    public static final int TYPE_MASK                  = 48;
    public static final int TYPE_SHIFT                 = 4;
    public static final int PROTECTED                  = 128;
    public static final int DEDICATED                  = 8;
    public static final int SHOW_SPEED_BAR             = 4096;
    public static final int EXTRA_OCCLUSION            = 8192;
    public static final int MSG_UPDATE                 = 0;
    public static final int MSG_COOP_ENTER             = 1;
    public static final int MSG_COOP_ENTER_ASK         = 2;
    public static final int MSG_SYNC                   = 3;
    public static final int MSG_SYNC_ASK               = 4;
    public static final int MSG_SYNC_START             = 5;
    public static final int MSG_TIME                   = 6;
    public static final int MSG_CHECK_BEGIN            = 8;
    public static final int MSG_CHECK_FIRST            = 8;
    public static final int MSG_CHECK_SECOND           = 9;
    public static final int MSG_CHECK_STEP             = 10;
    public static final int MSG_CHECK_END              = 10;
    private String          serverName;
    public String           serverDescription;
    private String          serverPassword;
    private NetHost         host;
    private int             flags                      = 4096;
    private int             difficulty;
    private int             maxUsers;
    private int             autoLogDetail              = 3;
    private boolean         eventlogHouse              = false;
    private int             eventlogClient             = -1;
    public boolean          bNGEN                      = false;
    public long             timeoutNGEN                = 0L;
    public boolean          bLandedNGEN                = false;
    private float           farMaxLagTime              = 10.0F;
    private float           nearMaxLagTime             = 2.0F;
    private float           cheaterWarningDelay        = 10.0F;
    private int             cheaterWarningNum          = 3;
    private NetMsgFiltered  outMsgF;
    private int             syncStamp                  = 0;
    private long            syncTime;
    private long            syncDelta;
    private boolean         bCheckStartSync            = false;
    private boolean         bDoSync                    = false;

    public boolean          filterUserNames;
    public boolean          allowMorseAsText;

    // TODO: Modified by |ZUTI|: changed from private to protected
    // -----------------------------------------------------------
    protected long          serverDeltaTime            = 0L;
    protected long          serverDeltaTime_lastUpdate = 0L;
    protected static long   timeofday                  = -1L;
    // -----------------------------------------------------------

    private long            serverClockOffset0         = 0L;
    private long            lastServerTime             = 0L;
    long                    _lastCheckMaxLag           = -1L;
    private int             checkRuntime               = 0;
    private long            checkTimeUpdate            = 0L;
    private HashMapExt      checkUsers;
    private int             checkPublicKey;
    private int             checkKey;
    private int             checkSecond2;

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
                    if (checkKey == 0)
                        checkKey = checkFirst(checkPublicKey);

                    int userKey = netmsginput.readInt();
                    flag = checkKey == userKey;

                    if (NetServerParams.DEBUG) {
                        System.out.println("CRT=1 Server key: " + checkKey + " vs CRT=1 user key " + userKey);
                        System.out.println("  Flag = " + flag + ", state = " + state);
                        System.out.println("-----------------------------------------");
                    }

                    if (flag) {
                        state++;
                    }
                    break;
                case 9: // '\t'
                    int j = 0;
                    timeoutMessage = "( CRT=1 Failed )";
                    // TODO: Edited by |ZUTI|
                    if (checkRuntime == 2) {
                        j = publicKey;
                        timeoutMessage = "( CRT=2 Failed )";
                    }

                    int userCRT2key = netmsginput.readInt();
                    int serverCRT2key = checkSecond(publicKey, j);
                    // Original Check - Changed to display value on Server
                    // flag = netmsginput.readInt() == checkSecond(publicKey, j);
                    flag = userCRT2key == serverCRT2key;

                    if (NetServerParams.DEBUG)
                        System.out.println("CRT=2 Classes: Server key: " + serverCRT2key + " vs CRT=2 user key " + userCRT2key);

                    if (flag) {
                        // After class check client also sends methods check for those classes
                        userCRT2key = netmsginput.readInt();
                        if (NetServerParams.DEBUG)
                            System.out.println("CRT=2 Methods: Server key: " + checkSecond2 + " vs CRT=2 user key " + userCRT2key);
                        // Original Check - Changed to display value on Server
                        // flag = netmsginput.readInt() == checkSecond2;
                        flag = userCRT2key == checkSecond2;
                        if (flag)
                            System.out.println("CRT=" + checkRuntime + " passed for connecting user!");
                    }

                    if (flag)
                        state++;
                    break;
                case 10: // '\n'

                    // TODO: Added by |ZUTI|: some planes report this check failed of you select specific weapon loadouts... CRT1 and CRT2 are affected
                    // -----------------------------------------------------------------
                    /*
                     * flag = true;
                     * timeoutMessage = "Valid Plane Selected";
                     * Aircraft aircraft = user.findAircraft();
                     * classAircraft = aircraft.getClass();
                     * break;
                     */
                    // -----------------------------------------------------------------
                    timeoutMessage = "( Plane Check Failed )";
                    Aircraft aircraft = user.findAircraft();
                    if (Actor.isValid(aircraft)) {
                        int k = Finger.incInt(publicKey, diff);
                        // 4.08
                        int readInt = netmsginput.readInt();
                        int fingerInt = (int) aircraft.finger(k);
                        flag = readInt == fingerInt;

                        if (DEBUG)
                            System.out.println("AC Check for: " + aircraft.name() + ", readInt=" + readInt + ", fingerInt=" + fingerInt);

                        // 4.09
                        // --------------------------------------------------------------------------------------------
                        /*
                         * try
                         * {
                         * flag = (netmsginput.readInt() == ((int)aircraft.finger((long)k) + SFSInputStream.oo));
                         * }
                         * catch(Exception ex)
                         * {
                         * flag = (netmsginput.readInt() == ((int)aircraft.finger((long)k)));
                         * }
                         */
                        // --------------------------------------------------------------------------------------------

                        if (flag)
                            classAircraft = aircraft.getClass();
                        else {
                            classAircraft = null;
                            System.out.println("User (" + user.fullName() + ") Failed Plane Check (" + user.findAircraft().toString() + ")");
                        }
                    } else {
                        classAircraft = null;
                        flag = true;
                    }
                    break;
                default:
                    return false;
            }
            timeSended = 0L;
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
            if (state <= 10) {
                if (timeSended != 0L) {
                    if (l >= timeSended + 150000L) {
                        NetChannel netchannel = user.masterChannel();
                        if (!netchannel.isDestroying()) {
                            String string = "Timeout. ";
                            // TODO: Modified by |ZUTI|
                            // string += state - 8;
                            string += "CRT" + (state - 7) + " failed?";
                            netchannel.destroy(string);
                        }
                    }
                } else {
                    try {
                        int i = 0;
                        switch (state) {
                            case 8:
                                i = checkPublicKey;
                                break;
                            case 9:
                                i = publicKey = (int) (Math.random() * 4.294967295E9);
                                break;
                            case 10: {
                                Aircraft aircraft = user.findAircraft();
                                if (Actor.isValid(aircraft) && !aircraft.getClass().equals(classAircraft)) {
                                    i = publicKey = (int) (Math.random() * 4.294967295E9);
                                    diff = World.cur().diffCur.get();
                                }
                                break;
                            }
                        }
                        if (i != 0) {
                            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                            netmsgguaranted.writeByte(state);
                            netmsgguaranted.writeNetObj(user);
                            netmsgguaranted.writeInt(i);
                            if (state == 9) {
                                if (checkRuntime == 2) {
                                    netmsgguaranted.writeInt(i);
                                } else {
                                    netmsgguaranted.writeInt(0);
                                }
                            }
                            NetServerParams.this.postTo(user.masterChannel(), netmsgguaranted);
                            timeSended = l;
                        }
                    } catch (Exception exception) {
                        /* empty */
                    }
                }
            }
        }

        public CheckUser(NetUser netuser) {
            user = netuser;
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
                    if (netmsginput.available() >= 8)
                        netserverparams.serverDeltaTime = netmsginput.readLong();
                    else
                        netserverparams.serverDeltaTime = 0L;
                } else
                    World.cur().diffCur.set(i_3_);
                netserverparams.synkExtraOcclusion();
                if (netmsginput.available() >= 4)
                    netserverparams.eventlogClient = netmsginput.readInt();
                else
                    netserverparams.eventlogClient = -1;
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }
    }

    public static long getServerTime() {
        // TODO: Added by |ZUTI|
        // -----------------------------------------------------------------------------
        if (Main.cur() != null && Main.cur().netServerParams != null && Main.cur().netServerParams.isDogfight()) {
            if (NetMissionTrack.isPlaying() || (Main.cur().netServerParams.isMirror() && !Time.isPaused())) {
                long tempServerTime = Time.current() + Main.cur().netServerParams.serverDeltaTime;
                // Never return time that is smaller than last reported time!!
                if (tempServerTime < ZUTI_LAST_SERVER_TIME) {
                    tempServerTime = ZUTI_LAST_SERVER_TIME;
                }

                ZUTI_LAST_SERVER_TIME = tempServerTime;

                return ZUTI_LAST_SERVER_TIME;
            }
        }
        // -----------------------------------------------------------------------------

        if (NetMissionTrack.isPlaying()) {
            if (Main.cur() != null && Main.cur().netServerParams != null && Main.cur().netServerParams.isCoop()) {
                long l = (Time.current() - Main.cur().netServerParams.serverDeltaTime);
                if (l < 0L)
                    l = 0L;
                if (l > Main.cur().netServerParams.lastServerTime)
                    Main.cur().netServerParams.lastServerTime = l;
                return Main.cur().netServerParams.lastServerTime;
            }
            return Time.current();
        }
        if (Main.cur() != null && Main.cur().netServerParams != null && Main.cur().netServerParams.isCoop() && Main.cur().netServerParams.isMirror() && !Time.isPaused() && Main.cur().netServerParams.serverClockOffset0 != 0L) {
            long l = Main.cur().netServerParams.masterChannel().remoteClockOffset();
            long l_5_ = (Time.current() - (l - Main.cur().netServerParams.serverClockOffset0));
            if (l_5_ < 0L)
                l_5_ = 0L;
            if (l_5_ > Main.cur().netServerParams.lastServerTime)
                Main.cur().netServerParams.lastServerTime = l_5_;
            return Main.cur().netServerParams.lastServerTime;
        }
        return Time.current();
    }

    public NetHost host() {
        return host;
    }

    public boolean isDedicated() {
        return (flags & 0x8) != 0;
    }

    public boolean isBBGC() {
        return (flags & 0x30) == 16;
    }

    public boolean isGAMESPY() {
        return (flags & 0x30) == 32;
    }

    public boolean isUSGS() {
        return (flags & 0x30) == 48;
    }

    public int getType() {
        return flags & 0x30;
    }

    public void setType(int i) {
        flags = flags & ~0x30 | i & 0x30;
    }

    public boolean isDogfight() {
        return (flags & 0x7) == 0;
    }

    public boolean isCoop() {
        return (flags & 0x7) == 1;
    }

    public boolean isSingle() {
        return (flags & 0x7) == 2;
    }

    public void setMode(int i) {
        if (isMaster()) {
            flags = flags & ~0x7 | i & 0x7;
            mirrorsUpdate();
        }
    }

    public boolean isShowSpeedBar() {
        return (flags & 0x1000) != 0;
    }

    public void setShowSpeedBar(boolean bool) {
        if (isMaster() && bool != isShowSpeedBar()) {
            if (bool)
                flags |= 0x1000;
            else
                flags &= ~0x1000;
            mirrorsUpdate();
        }
    }

    public boolean isExtraOcclusion() {
        return (flags & 0x2000) != 0;
    }

    public void setExtraOcclusion(boolean bool) {
        if (isMaster() && bool != isExtraOcclusion()) {
            if (bool)
                flags |= 0x2000;
            else
                flags &= ~0x2000;
            synkExtraOcclusion();
            mirrorsUpdate();
        }
    }

    public void synkExtraOcclusion() {
        if (!isDedicated())
            AudioDevice.setExtraOcclusion(isExtraOcclusion());
    }

    public int autoLogDetail() {
        return autoLogDetail;
    }

    public boolean eventlogHouse() {
        return eventlogHouse && isMaster();
    }

    public int eventlogClient() {
        return eventlogClient;
    }

    public float farMaxLagTime() {
        return farMaxLagTime;
    }

    public float nearMaxLagTime() {
        return nearMaxLagTime;
    }

    public float cheaterWarningDelay() {
        return cheaterWarningDelay;
    }

    public int cheaterWarningNum() {
        return cheaterWarningNum;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int i) {
        if (isMaster()) {
            difficulty = i;
            World.cur().diffCur.set(difficulty);
            setClouds();
            mirrorsUpdate();
        }
    }

    public String serverName() {
        return serverName;
    }

    public void setServerName(String string) {
        if (USGS.isUsed() && isMaster()) {
            serverName = USGS.room;
            if (serverName == null)
                serverName = "Server";
        } else if (Main.cur().netGameSpy != null)
            serverName = Main.cur().netGameSpy.roomName;
        else {
            serverName = string;
            mirrorsUpdate();
        }
    }

    public boolean isProtected() {
        return (flags & 0x80) != 0;
    }

    public String getPassword() {
        return serverPassword;
    }

    public void setPassword(String string) {
        serverPassword = string;
        if (serverPassword != null)
            flags |= 0x80;
        else
            flags &= ~0x80;
        mirrorsUpdate();
    }

    private void setClouds() {
        if (Config.isUSE_RENDER()) {
            if (World.cur().diffCur.Clouds) {
                Main3D.cur3D().bDrawClouds = true;
                if (RenderContext.cfgSky.get() == 0) {
                    RenderContext.cfgSky.set(1);
                    RenderContext.cfgSky.apply();
                    RenderContext.cfgSky.reset();
                }
            } else
                Main3D.cur3D().bDrawClouds = false;
        }
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int i) {
        if (isMaster()) {
            maxUsers = i;
            mirrorsUpdate();
        }
    }

    private void mirrorsUpdate() {
        USGSupdate();
        if (Main.cur().netGameSpy != null)
            Main.cur().netGameSpy.sendStatechanged();
        if (isMirrored()) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(0);
                netmsgguaranted.writeInt(flags);
                netmsgguaranted.writeInt(difficulty);
                netmsgguaranted.writeByte(maxUsers);
                netmsgguaranted.write255(serverName);
                post(netmsgguaranted);
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }
    }

    public void USGSupdate() {
        if (isMaster() && USGS.isUsed())
            USGS.update();
    }

    public void doMissionCoopEnter() {
        if (isMaster()) {
            List list = NetEnv.hosts();
            if (list.size() == 0) {
                prepareHidenAircraft();
                startCoopGame();
                return;
            }
            for (int i = 0; i < list.size(); i++)
                ((NetUser) list.get(i)).syncCoopStart = -1;
            bCheckStartSync = true;
            syncTime = Time.currentReal() + 32000L;
        } else if (Main.cur().netMissionListener != null)
            Main.cur().netMissionListener.netMissionCoopEnter();
        if (isMirrored()) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(1);
                netmsgguaranted.writeByte(syncStamp);
                post(netmsgguaranted);
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }
        if (!isMaster()) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(2);
                netmsgguaranted.writeNetObj(NetEnv.host());
                postTo(masterChannel(), netmsgguaranted);
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
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
                serverName = netmsginput.read255();
                flags = i_6_;
                difficulty = i_7_;
                maxUsers = i_8_;
                World.cur().diffCur.set(difficulty);
                setClouds();
                synkExtraOcclusion();
                if (isMirrored())
                    post(new NetMsgGuaranted(netmsginput, 0));
                break;
            }
            case 1:
                syncStamp = netmsginput.readUnsignedByte();
                doMissionCoopEnter();
                break;
            case 2:
                if (isMaster()) {
                    NetUser netuser = (NetUser) netmsginput.readNetObj();
                    if (netuser != null)
                        netuser.syncCoopStart = syncStamp;
                } else
                    postTo(masterChannel(), new NetMsgGuaranted(netmsginput, 1));
                break;
            case 3: {
                int i_9_ = netmsginput.readUnsignedByte();
                int i_10_ = netmsginput.readInt();
                if (syncStamp != i_9_) {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(4);
                    netmsgguaranted.writeByte(i_9_);
                    netmsgguaranted.writeNetObj(NetEnv.host());
                    postTo(masterChannel(), netmsgguaranted);
                    syncStamp = i_9_;
                    syncTime = (long) i_10_ + Message.currentRealTime();
                } else {
                    long l = (long) i_10_ + Message.currentRealTime();
                    if (syncTime > l)
                        syncTime = l;
                }
                if (isMirrored()) {
                    outMsgF.unLockAndClear();
                    outMsgF.writeByte(3);
                    outMsgF.writeByte(syncStamp);
                    outMsgF.writeInt((int) (syncTime - Time.currentReal()));
                    postReal(Time.currentReal(), outMsgF);
                }
                break;
            }
            case 4:
                if (isMaster()) {
                    int i_11_ = netmsginput.readUnsignedByte();
                    NetUser netuser = (NetUser) netmsginput.readNetObj();
                    if (netuser != null && i_11_ == syncStamp) {
                        netuser.syncCoopStart = syncStamp;
                        List list = NetEnv.hosts();
                        for (int i_12_ = 0; i_12_ < list.size(); i_12_++) {
                            if (((NetUser) list.get(i_12_)).syncCoopStart != syncStamp)
                                return true;
                        }
                        bDoSync = false;
                        doStartCoopGame();
                    }
                } else
                    postTo(masterChannel(), new NetMsgGuaranted(netmsginput, 1));
                break;
            case 5:
                doStartCoopGame();
                break;
            case 6:
                serverDeltaTime = netmsginput.readLong();
                if (NetMissionTrack.isRecording()) {
                    try {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(6);
                        netmsgguaranted.writeLong(serverDeltaTime);
                        postTo(NetMissionTrack.netChannelOut(), netmsgguaranted);
                    } catch (Exception exception) {
                        NetObj.printDebug(exception);
                    }
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
                    zutiNewServerDeltaTime = netmsginput.readLong();
                    // System.out.println(" Client received Server time1: " + zutiNewServerDeltaTime);
                    zutiNewServerDeltaTime = (((NetUser) NetEnv.host()).ping / 2) + zutiNewServerDeltaTime - Time.current();
                    // if( zutiNewServerDeltaTime > serverDeltaTime )
                    serverDeltaTime = zutiNewServerDeltaTime;
                    // System.out.println(" Client received Server time2: " + (Time.current() + serverDeltaTime));
                }

                if (NetMissionTrack.isRecording()) {
                    try {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(7);
                        netmsgguaranted.writeLong(serverDeltaTime);
                        postTo(NetMissionTrack.netChannelOut(), netmsgguaranted);
                    } catch (Exception exception) {
                        NetObj.printDebug(exception);
                    }
                }
                break;
            // ----------------------------------------------------------------------------------

            default:
                return checkInput(i, netmsginput);
        }
        return true;
    }

    public void netUpdate() {
        if (!NetMissionTrack.isPlaying()) {
            // TODO: ZUTI: Server side code
            // ----------------------------------------------------------------------------------
            if (isMaster()) {
                long l = Time.current();
                if (!zutiInitialTimeSyncDone || l > serverDeltaTime_lastUpdate + NetServerParams.ZUTI_RESYNC_INTERVAL) {
                    zutiInitialTimeSyncDone = true;
                    serverDeltaTime_lastUpdate = l;

                    try {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(7);
                        netmsgguaranted.writeLong(serverDeltaTime_lastUpdate);
                        post(netmsgguaranted);

                        // System.out.println(" Server reported time: " + serverDeltaTime_lastUpdate);
                    } catch (IOException ex) {
                        NetObj.printDebug(ex);
                    }
                }
            }
            // ----------------------------------------------------------------------------------

            doCheckMaxLag();

            if (isMaster())
                checkUpdate();
        }
        if (isMirror() && isCoop() && !Time.isPaused() && NetMissionTrack.isRecording() && !NetMissionTrack.isPlaying()) {
            long l = Time.current();
            if (l > serverDeltaTime_lastUpdate + 3000L) {
                serverDeltaTime_lastUpdate = l;
                try {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(6);
                    long l_13_ = Main.cur().netServerParams.masterChannel().remoteClockOffset();
                    long l_14_ = (l_13_ - Main.cur().netServerParams.serverClockOffset0);
                    netmsgguaranted.writeLong(l_14_);
                    postTo(NetMissionTrack.netChannelOut(), netmsgguaranted);
                } catch (Exception exception) {
                    NetObj.printDebug(exception);
                }
            }
        }
        // TODO: ZUTI: client side code
        // -----------------------------------------------------------------------------------
        if (isMirror() && isDogfight() && !Time.isPaused() && NetMissionTrack.isRecording() && !NetMissionTrack.isPlaying()) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(7);
                netmsgguaranted.writeLong(Time.current() + Main.cur().netServerParams.serverDeltaTime);
                postTo(NetMissionTrack.netChannelOut(), netmsgguaranted);
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }
        // -----------------------------------------------------------------------------------

        if (bDoSync || bCheckStartSync) {
            if (isMaster()) {
                if (bCheckStartSync) {
                    List list = NetEnv.hosts();
                    if (list.size() == 0) {
                        prepareHidenAircraft();
                        startCoopGame();
                        bCheckStartSync = false;
                        return;
                    }
                    if (Time.currentReal() > syncTime) {
                        for (int i = 0; i < list.size(); i++) {
                            NetUser netuser = (NetUser) list.get(i);
                            if (netuser.syncCoopStart != syncStamp)
                                ((NetUser) NetEnv.host()).kick(netuser);
                        }
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            NetUser netuser = (NetUser) list.get(i);
                            if (netuser.syncCoopStart != syncStamp)
                                return;
                        }
                    }
                    syncStamp = syncStamp + 1 & 0xff;
                    syncDelta = 4000L;
                    syncTime = Time.currentReal() + syncDelta;
                    bCheckStartSync = false;
                    bDoSync = true;
                }
                if (NetEnv.hosts().size() == 0) {
                    prepareHidenAircraft();
                    startCoopGame();
                    bDoSync = false;
                } else {
                    if (Time.currentReal() > syncTime - syncDelta / 2L) {
                        if (syncDelta < 32000L) {
                            syncStamp = syncStamp + 1 & 0xff;
                            syncDelta *= 2L;
                            syncTime = Time.currentReal() + syncDelta;
                        } else {
                            List list = NetEnv.hosts();
                            for (int i = 0; i < list.size(); i++) {
                                NetUser netuser = (NetUser) list.get(i);
                                if (netuser.syncCoopStart != syncStamp)
                                    ((NetUser) NetEnv.host()).kick(netuser);
                            }
                            bDoSync = false;
                            doStartCoopGame();
                            return;
                        }
                    }
                    try {
                        outMsgF.unLockAndClear();
                        outMsgF.writeByte(3);
                        outMsgF.writeByte(syncStamp);
                        outMsgF.writeInt((int) (syncTime - Time.currentReal()));
                        postReal(Time.currentReal(), outMsgF);
                    } catch (Exception exception) {
                        NetObj.printDebug(exception);
                    }
                }
            }
        }
    }

    public void msgNetDelChannel(NetChannel netchannel) {
        netUpdate();
    }

    private void doStartCoopGame() {
        if (isMirrored()) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(5);
                post(netmsgguaranted);
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }
        HUD.logCoopTimeStart(syncTime);
        new MsgAction(64, syncTime, this) {
            public void doAction(Object object) {
                if (object == Main.cur().netServerParams) {
                    prepareHidenAircraft();
                    NetServerParams.this.startCoopGame();
                }
            }
        };
    }

    private void startCoopGame() {
        prepareOrdersTree();
        Mission.doMissionStarting();
        Time.setPause(false);
        AudioDevice.soundsOn();
        if (isMaster() && bNGEN) {
            if (timeoutNGEN > 0L)
                startTimeoutNGEN(timeoutNGEN);
            if (bLandedNGEN)
                startLandedNGEN(2000L);
        } else {
            if (masterChannel() != null)
                serverClockOffset0 = masterChannel().remoteClockOffset();
            else
                serverClockOffset0 = 0L;
            lastServerTime = 0L;
            serverDeltaTime_lastUpdate = -100000L;
        }
    }

    private void startTimeoutNGEN(long l) {
        new MsgAction(0, Time.current() + l, Mission.cur()) {
            public void doAction(Object object) {
                if (Mission.cur() == object && Mission.isPlaying()) {
                    if (Main.state().id() != 49)
                        NetServerParams.this.startTimeoutNGEN(500L);
                    else
                        ((GUINetServerCMission) Main.state()).tryExit();
                }
            }
        };
    }

    private void startLandedNGEN(long l) {
        new MsgAction(0, Time.current() + l, Mission.cur()) {
            public void doAction(Object object) {
                if (Mission.cur() == object && Mission.isPlaying()) {
                    if (Main.state().id() != 49)
                        NetServerParams.this.startLandedNGEN(2000L);
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
                        if (bool)
                            ((GUINetServerCMission) Main.state()).tryExit();
                        else
                            NetServerParams.this.startLandedNGEN(2000L);
                    }
                }
            }
        };
    }

    public void prepareHidenAircraft() {
        ArrayList arraylist = new ArrayList();
        for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (actor instanceof Aircraft && actor.name().charAt(0) == ' ')
                arraylist.add(actor);
        }
        for (int i = 0; i < arraylist.size(); i++) {
            Aircraft aircraft = (Aircraft) arraylist.get(i);
            String string = aircraft.name().substring(1);
            if (Actor.getByName(string) != null)
                aircraft.destroy();
            else {
                aircraft.setName(string);
                aircraft.collide(true);
                aircraft.restoreLinksInCoopWing();
            }
        }
        if (World.isPlayerGunner())
            World.getPlayerGunner().getAircraft();
        if (!isMaster()) {
            for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
                Actor actor = (Actor) entry.getValue();
                if (actor instanceof Aircraft) {
                    Aircraft aircraft = (Aircraft) actor;
                    if (!aircraft.isNetPlayer() && !aircraft.isNet())
                        arraylist.add(actor);
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
        if (World.isPlayerGunner())
            World.getPlayerGunner().getAircraft();
        else
            ((Main3D) Main.cur()).ordersTree.netMissionLoaded(World.getPlayerAircraft());
        if (!isMirror()) {
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
        if (_lastCheckMaxLag <= 0L || l - _lastCheckMaxLag >= 1000L) {
            _lastCheckMaxLag = l;
            if (Mission.isPlaying()) {
                if (isMaster()) {
                    List list = Engine.targets();
                    int i = list.size();
                    for (int i_20_ = 0; i_20_ < i; i_20_++) {
                        Actor actor = (Actor) list.get(i_20_);
                        
                        // TODO: Add null check for "actor.net" to avoid exceptions here
                        if (actor instanceof Aircraft && Actor.isAlive(actor) && actor.net != null && !actor.net.isMaster()) {
                            NetUser netuser = ((Aircraft) actor).netUser();
                            if (netuser != null) {
                                if (netuser.netMaxLag == null)
                                    netuser.netMaxLag = new NetMaxLag();
                                netuser.netMaxLag.doServerCheck((Aircraft) actor);
                            }
                        }
                    }
                } else {
                    NetUser netuser = (NetUser) NetEnv.host();
                    if (netuser.netMaxLag == null)
                        netuser.netMaxLag = new NetMaxLag();
                    netuser.netMaxLag.doClientCheck();
                }
            }
        }
    }

    public void destroy() {
        super.destroy();
        bCheckStartSync = false;
        bDoSync = false;
        Main.cur().netServerParams = null;
    }

    // ZUTI: Server side constructor
    public NetServerParams() {
        super(null);
        checkUsers = new HashMapExt();
        checkPublicKey = 0;
        checkKey = 0;
        checkSecond2 = 0;
        host = NetEnv.host();
        serverName = host.shortName();
        Main.cur().netServerParams = this;
        outMsgF = new NetMsgFiltered();
        try {
            outMsgF.setIncludeTime(true);
        } catch (Exception exception) {}
        if (!Config.isUSE_RENDER())
            flags |= 0x8;
        synkExtraOcclusion();
        autoLogDetail = Config.cur.ini.get("chat", "autoLogDetail", autoLogDetail, 0, 3);
        nearMaxLagTime = Config.cur.ini.get("MaxLag", "nearMaxLagTime", nearMaxLagTime, 0.1F, 30.0F);
        farMaxLagTime = Config.cur.ini.get("MaxLag", "farMaxLagTime", farMaxLagTime, nearMaxLagTime, 30.0F);
        cheaterWarningDelay = Config.cur.ini.get("MaxLag", "cheaterWarningDelay", cheaterWarningDelay, 1.0F, 30.0F);

        // TODO: Added by |ZTUI|
        // ------------------------------------------
        ZutiSupportMethods_Net.checkTimeOfDay();
        // ------------------------------------------

        cheaterWarningNum = Config.cur.ini.get("MaxLag", "cheaterWarningNum", cheaterWarningNum);
        checkRuntime = Config.cur.ini.get("NET", "checkRuntime", 0, 0, 2);
        eventlogHouse = Config.cur.ini.get("game", "eventlogHouse", false);
        eventlogClient = Config.cur.ini.get("game", "eventlogClient", -1);

        filterUserNames = false;

        filterUserNames = Config.cur.ini.get("NET", "filterUserNames", false);

        allowMorseAsText = true;
        allowMorseAsText = Config.cur.ini.get("NET", "allowMorseAsText", true);
    }

    // ZUTI: Client side constructor
    public NetServerParams(NetChannel netchannel, int i, NetHost nethost) {
        super(null, netchannel, i);

        // ZUTI: reset old time
        zutiNewServerDeltaTime = 0;
        zutiInitialTimeSyncDone = false;

        checkUsers = new HashMapExt();
        checkPublicKey = 0;
        checkKey = 0;
        checkSecond2 = 0;
        host = nethost;
        Main.cur().netServerParams = this;

        // TODO: Added by |ZTUI|
        // ------------------------------------------
        ZutiSupportMethods_Net.checkTimeOfDay();
        // ------------------------------------------

        filterUserNames = false;
        allowMorseAsText = true;

        outMsgF = new NetMsgFiltered();
        try {
            outMsgF.setIncludeTime(true);
        } catch (Exception exception) {}
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException {
        NetMsgSpawn netmsgspawn = new NetMsgSpawn(this);
        netmsgspawn.writeNetObj(host);
        netmsgspawn.writeInt(flags);
        netmsgspawn.writeInt(difficulty);
        netmsgspawn.writeByte(maxUsers);
        netmsgspawn.write255(serverName);
        netmsgspawn.writeByte(autoLogDetail);
        netmsgspawn.writeFloat(farMaxLagTime);
        netmsgspawn.writeFloat(nearMaxLagTime);
        netmsgspawn.writeFloat(cheaterWarningDelay);
        netmsgspawn.writeInt(cheaterWarningNum);

        netmsgspawn.writeBoolean(filterUserNames);
        netmsgspawn.writeBoolean(allowMorseAsText);

        if (netchannel instanceof NetChannelOutStream && isCoop()) {
            if (NetMissionTrack.isPlaying())
                netmsgspawn.writeLong(serverDeltaTime);
            else {
                long l = 0L;
                if (isMirror()) {
                    long l_21_ = Main.cur().netServerParams.masterChannel().remoteClockOffset();
                    l = l_21_ - Main.cur().netServerParams.serverClockOffset0;
                }
                netmsgspawn.writeLong(l);
            }
        }
        netmsgspawn.writeInt(eventlogClient);
        return netmsgspawn;
    }

    private int checkFirst(int i) {
        if (i != 0) {
            long l = Finger.file((long) i, MainWin32.GetCDDrive("jvm.dll"), -1);
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
                if (propertyName != null)
                    l = FlightModelMain.finger(l, propertyName);
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
            if (NetServerParams.DEBUG)
                System.out.println("Check sum before MODS check: " + l);
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File("MODS"), 0, 0);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG)
                System.out.println("Check sum after MODS check: " + l);
            String zutiLocalTime = "FILES";// "files" + PATH_SEPARATOR + "3do" + PATH_SEPARATOR + "plane";
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File(zutiLocalTime), 0, 0);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG)
                System.out.println("Check sum after " + zutiLocalTime + " check: " + l);
            zutiLocalTime = "#SAS";// "files" + PATH_SEPARATOR + "3do" + PATH_SEPARATOR + "cockpit";
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File(zutiLocalTime), 0, 0);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG)
                System.out.println("Check sum after " + zutiLocalTime + " check: " + l);
            zutiLocalTime = "#UP#";// "files" + PATH_SEPARATOR + "samples";
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File(zutiLocalTime), 0, 0);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG)
                System.out.println("Check sum after " + zutiLocalTime + " check: " + l);
            // zutiLocalTime = "files" + PATH_SEPARATOR + "presets";
            // try{l += Statics.getSize(new java.io.File(zutiLocalTime), 0, 0);}catch(Exception ex){}
            // System.out.println("Check sum after " + zutiLocalTime + " check: " + l);
            zutiLocalTime = "files" + PATH_SEPARATOR + "effects";
            try {
                l += ZutiSupportMethods_Objects.getLocationGeneratedNumber(new java.io.File(zutiLocalTime), 0, 0);
            } catch (Exception ex) {}
            if (NetServerParams.DEBUG)
                System.out.println("Check sum after " + zutiLocalTime + " check: " + l);

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

        checkSecond2 = j;
        try {
            ClassLoader classloader = this.getClass().getClassLoader();
            Field[] fields = ClassLoader.class.getDeclaredFields();
            Field field = null;
            for (int i_23_ = 0; i_23_ < fields.length; i_23_++) {
                if ("classes".equals(fields[i_23_].getName())) {
                    field = fields[i_23_];
                    break;
                }
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

                if (fields != null) {
                    for (int i_25_ = 0; i_25_ < fields.length; i_25_++)
                        i = Finger.incInt(i, fields[i_25_].getName());
                }
                Method[] methods = var_class.getDeclaredMethods();
                if (methods != null) {
                    for (int i_26_ = 0; i_26_ < methods.length; i_26_++)
                        i = Finger.incInt(i, methods[i_26_].getName());
                }
                if (checkSecond2 != 0)
                    j = CLASS.method(var_class, j);

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
        } catch (Exception exception) {
            /* empty */
        }
        checkSecond2 = j;

        if (NetServerParams.DEBUG) {
            System.out.println("publicKey: " + new Integer(i).toString());
            System.out.println("checkSecond2: " + new Integer(checkSecond2).toString());
            System.out.println("=====================================================================");
        }

        return i;
    }

    // TODO: CRT Checks - Client Side
    private boolean CheckUserInput(int i, NetMsgInput netmsginput) throws IOException {
        int result;
        switch (i) {
            case 8:
                result = checkFirst(netmsginput.readInt());

                if (NetServerParams.DEBUG)
                    System.out.println("CRT=1 sum = " + result);
                break;
            case 9:
                result = checkSecond(netmsginput.readInt(), netmsginput.readInt());

                if (NetServerParams.DEBUG)
                    System.out.println("CRT=2 sum = " + result);
                break;
            case 10: {
                result = netmsginput.readInt();
                NetUser netuser = (NetUser) NetEnv.host();
                Aircraft aircraft = netuser.findAircraft();
                if (Actor.isValid(aircraft)) {
                    result = Finger.incInt(result, World.cur().diffCur.get());
                    // 4.08
                    result = (int) aircraft.finger((long) result);
                    // 4.09
                    // --------------------------------------------------------------------------------------------
                    /*
                     * try
                     * {
                     * i_27_ = (int) aircraft.finger((long) i_27_) + SFSInputStream.oo;
                     * }
                     * catch(Exception ex)
                     * {
                     * i_27_ = (int) aircraft.finger((long) i_27_);
                     * }
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
        if (i == 9)
            netmsgguaranted.writeInt(checkSecond2);
        postTo(netmsginput.channel(), netmsgguaranted);
        return true;
    }

    private boolean checkInput(int i, NetMsgInput netmsginput) throws IOException {
        NetUser netuser = (NetUser) netmsginput.readNetObj();
        if (isMaster()) {
            CheckUser checkuser = (CheckUser) checkUsers.get(netuser);
            if (checkuser != null)
                return checkuser.checkInput(i, netmsginput);
        } else {
            if (NetEnv.host() == netuser)
                return CheckUserInput(i, netmsginput);
            netmsginput.reset();
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeMsg(netmsginput, 1);
            if (netmsginput.channel() == masterChannel())
                postTo(netuser.masterChannel(), netmsgguaranted);
            else
                postTo(masterChannel(), netmsgguaranted);
            return true;
        }
        return false;
    }

    private void checkUpdate() {
        if (!isSingle()) {
            long l = Time.currentReal();
            if (l >= checkTimeUpdate) {
                checkTimeUpdate = l + 1000L;
                List list = NetEnv.hosts();
                int i = list.size();
                for (int i_29_ = 0; i_29_ < i; i_29_++) {
                    NetUser netuser = (NetUser) list.get(i_29_);
                    if (!checkUsers.containsKey(netuser))
                        checkUsers.put(netuser, new CheckUser(netuser));
                }
                if (i != checkUsers.size()) {
                    boolean bool;
                    do {
                        bool = false;
                        for (Map.Entry entry = checkUsers.nextEntry(null); entry != null; entry = checkUsers.nextEntry(entry)) {
                            NetUser netuser = (NetUser) entry.getKey();
                            if (netuser.isDestroyed()) {
                                checkUsers.remove(netuser);
                                bool = true;
                                break;
                            }
                        }
                    } while (bool);
                }
                for (Map.Entry entry = checkUsers.nextEntry(null); entry != null; entry = checkUsers.nextEntry(entry)) {
                    if (checkPublicKey == 0 && checkRuntime >= 1)
                        checkPublicKey = (int) (Math.random() * 4.294967295E9);
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