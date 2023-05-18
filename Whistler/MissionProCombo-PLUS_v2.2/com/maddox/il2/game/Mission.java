package com.maddox.il2.game;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.ai.ground.ChiefGround;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.order.OrdersTree;
import com.maddox.il2.gui.*;
import com.maddox.il2.net.*;
import com.maddox.il2.objects.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.bridges.LongBridge;
import com.maddox.il2.objects.buildings.HouseManager;
import com.maddox.il2.objects.effects.*;
import com.maddox.il2.objects.humans.Soldier;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.artillery.RocketryGeneric;
import com.maddox.il2.objects.vehicles.radios.*;
import com.maddox.il2.objects.vehicles.stationary.SmokeGeneric;
import com.maddox.il2.objects.vehicles.stationary.TypeRunwayLight;
import com.maddox.rts.*;
import com.maddox.rts.net.NetFileServerDef;
import com.maddox.sound.AudioDevice;
import com.maddox.sound.CmdMusic;
import com.maddox.util.*;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.util.*;

public class Mission
    implements Destroy
{
    static class SPAWN
        implements NetSpawn
    {

        public void netSpawn(int i, NetMsgInput netmsginput)
        {
            try
            {
                if(Main.cur().mission != null)
                    Main.cur().mission.destroy();
                Mission mission = new Mission();
                if(Mission.cur() != null)
                    Mission.cur().destroy();
                mission.clear();
                Main.cur().mission = mission;
                mission.createNetObject(netmsginput.channel(), i);
                Main.cur().missionLoading = mission;
            }
            catch(Exception exception)
            {
                Mission.printDebug(exception);
            }
        }

        SPAWN()
        {
        }
    }

    class Mirror extends NetMissionObj
    {

        public Mirror(Mission mission1, NetChannel netchannel, int i)
        {
            super(mission1, netchannel, i);
            try
            {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(0);
                postTo(netchannel, netmsgguaranted);
            }
            catch(Exception exception)
            {
                Mission.printDebug(exception);
            }
        }
    }

    class Master extends NetMissionObj
    {

        public Master(Mission mission1)
        {
            super(mission1);
            mission1.sectFinger = mission1.sectFile.fingerExcludeSectPrefix("$$$");
        }
    }

    class NetMissionObj extends NetObj
        implements NetChannelCallbackStream
    {

        private void msgCallback(NetChannel netchannel, int i)
        {
            try
            {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(1);
                netmsgguaranted.writeByte(i);
                NetMsgInput netmsginput = new NetMsgInput();
                netmsginput.setData(netchannel, true, netmsgguaranted.data(), 0, netmsgguaranted.size());
                MsgNet.postReal(Time.currentReal(), this, netmsginput);
            }
            catch(Exception exception)
            {
                Mission.printDebug(exception);
            }
        }

        public boolean netChannelCallback(NetChannelOutStream netchanneloutstream, NetMsgGuaranted netmsgguaranted)
        {
            if(netmsgguaranted instanceof NetMsgSpawn)
                msgCallback(netchanneloutstream, 0);
            else
            if(!(netmsgguaranted instanceof NetMsgDestroy))
                try
                {
                    NetMsgInput netmsginput = new NetMsgInput();
                    netmsginput.setData(netchanneloutstream, true, netmsgguaranted.data(), 0, netmsgguaranted.size());
                    int i = netmsginput.readUnsignedByte();
                    switch(i)
                    {
                    case 0: // '\0'
                        msgCallback(netchanneloutstream, 1);
                        break;

                    case 2: // '\002'
                        msgCallback(netchanneloutstream, 3);
                        break;

                    case 4: // '\004'
                        msgCallback(netchanneloutstream, 4);
                        break;

                    case 12: // '\f'
                        Main3D.cur3D().gameTrackRecord().startKeyRecord(netmsgguaranted);
                        return false;
                    }
                }
                catch(Exception exception)
                {
                    Mission.printDebug(exception);
                }
            return true;
        }

        public boolean netChannelCallback(NetChannelInStream netchannelinstream, NetMsgInput netmsginput)
        {
            try
            {
                int i = netmsginput.readUnsignedByte();
                if(i == 4)
                    netchannelinstream.setPause(true);
                else
                if(i == 12)
                {
                    netchannelinstream.setGameTime();
                    if(Mission.isCoop() || Mission.isDogfight())
                    {
                        Main.cur().netServerParams.prepareHidenAircraft();
                        Mission.doMissionStarting();
                        Time.setPause(false);
                    }
                    Main3D.cur3D().gameTrackPlay().startKeyPlay();
                }
                netmsginput.reset();
            }
            catch(Exception exception)
            {
                Mission.printDebug(exception);
            }
            return true;
        }

        public void netChannelCallback(NetChannelInStream netchannelinstream, NetMsgGuaranted netmsgguaranted)
        {
            if(!(netmsgguaranted instanceof NetMsgSpawn) && !(netmsgguaranted instanceof NetMsgDestroy))
                try
                {
                    NetMsgInput netmsginput = new NetMsgInput();
                    netmsginput.setData(netchannelinstream, true, netmsgguaranted.data(), 0, netmsgguaranted.size());
                    int i = netmsginput.readUnsignedByte();
                    if(i == 4)
                        netchannelinstream.setPause(false);
                }
                catch(Exception exception)
                {
                    Mission.printDebug(exception);
                }
        }

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            Mission mission = (Mission)superObj();
            mission.netInput(netmsginput);
            return true;
        }

        public void msgNetNewChannel(NetChannel netchannel)
        {
            if(Main.cur().missionLoading != null)
            {
                return;
            } else
            {
                tryReplicate(netchannel);
                return;
            }
        }

        private void tryReplicate(NetChannel netchannel)
        {
            if(netchannel.isReady() && netchannel.isPublic() && netchannel != masterChannel && !netchannel.isMirrored(this) && netchannel.userState == 1)
                try
                {
                    postTo(netchannel, new NetMsgSpawn(this));
                }
                catch(Exception exception)
                {
                    Mission.printDebug(exception);
                }
        }

        public NetMissionObj(Object obj)
        {
            super(obj);
        }

        public NetMissionObj(Object obj, NetChannel netchannel, int i)
        {
            super(obj, netchannel, i);
        }
    }

    static class WingTakeoffPos
    {

        public boolean equals(Object obj)
        {
            if(obj == null)
                return false;
            if(!(obj instanceof WingTakeoffPos))
            {
                return false;
            } else
            {
                WingTakeoffPos wingtakeoffpos = (WingTakeoffPos)obj;
                return x == wingtakeoffpos.x && y == wingtakeoffpos.y;
            }
        }

        public int hashCode()
        {
            return x + y;
        }

        public int x;
        public int y;

        public WingTakeoffPos(double d, double d1)
        {
            x = (int)(d / 100D) * 100;
            y = (int)(d1 / 100D) * 100;
        }
    }

    class TimeOutWing
    {

        public void start()
        {
            if(isDestroyed())
                return;
            try
            {
                NetAircraft.loadingCoopPlane = false;
                Wing wing = new Wing();
                wing.load(sectFile, wingName, null);
                prepareSkinInWing(sectFile, wing);
                wing.setOnAirport();
            }
            catch(Exception exception)
            {
                Mission.printDebug(exception);
            }
        }

        String wingName;

        public TimeOutWing(String s)
        {
            wingName = s;
        }
    }

    public class BackgroundLoader extends BackgroundTask
    {

        public void run()
            throws Exception
        {
            _load(_name, _in, true);
        }

        private String _name;
        private SectFile _in;

        public BackgroundLoader(String s, SectFile sectfile)
        {
            _name = s;
            _in = sectfile;
        }
    }


    public Mission()
    {
        name = null;
        sectFinger = 0L;
        actors = new ArrayList();
        curActor = 0;
        bPlaying = false;
        curCloudsType = 0;
        curCloudsHeight = 1000F;
        curWindDirection = 0.0F;
        curWindVelocity = 0.0F;
        curGust = 0.0F;
        curTurbulence = 0.0F;
        bigShipHpDiv = 1.0F;
        _loadPlayer = false;
        playerNum = 0;
    }

    public static float respawnTime(String s)
    {
        Object obj = respawnMap.get(s);
        if(obj == null)
            return 1800F;
        else
            return ((Float)obj).floatValue();
    }

    public static boolean isPlaying()
    {
        if(Main.cur() == null)
            return false;
        if(Main.cur().mission == null)
            return false;
        if(Main.cur().mission.isDestroyed())
            return false;
        else
            return Main.cur().mission.bPlaying;
    }

    public static boolean isSingle()
    {
        if(Main.cur().mission == null)
            return false;
        if(Main.cur().mission.isDestroyed())
            return false;
        if(Main.cur().mission.net == null)
            return true;
        if(Main.cur().netServerParams == null)
            return true;
        else
            return Main.cur().netServerParams.isSingle();
    }

    public static boolean isNet()
    {
        if(Main.cur().mission == null)
            return false;
        if(Main.cur().mission.isDestroyed())
            return false;
        if(Main.cur().mission.net == null)
            return false;
        if(Main.cur().netServerParams == null)
            return false;
        else
            return !Main.cur().netServerParams.isSingle();
    }

    public NetChannel getNetMasterChannel()
    {
        if(net == null)
            return null;
        else
            return net.masterChannel();
    }

    public static boolean isServer()
    {
        return NetEnv.isServer();
    }

    public static boolean isDeathmatch()
    {
        return isDogfight();
    }

    public static boolean isDogfight()
    {
        if(Main.cur().mission == null)
            return false;
        if(Main.cur().mission.isDestroyed())
            return false;
        if(Main.cur().mission.net == null)
            return false;
        if(Main.cur().netServerParams == null)
            return false;
        else
            return Main.cur().netServerParams.isDogfight();
    }

    public static boolean isCoop()
    {
        if(Main.cur().mission == null)
            return false;
        if(Main.cur().mission.isDestroyed())
            return false;
        if(Main.cur().mission.net == null)
            return false;
        if(Main.cur().netServerParams == null)
            return false;
        else
            return Main.cur().netServerParams.isCoop();
    }

    public static int curCloudsType()
    {
        if(Main.cur().mission == null)
            return 0;
        else
            return Main.cur().mission.curCloudsType;
    }

    public static float curCloudsHeight()
    {
        if(Main.cur().mission == null)
            return 1000F;
        else
            return Main.cur().mission.curCloudsHeight;
    }

    public static int curYear()
    {
        return curYear;
    }

    public static int curMonth()
    {
        return curMonth;
    }

    public static int curDay()
    {
        return curDay;
    }

    public static void resetDate()
    {
        curYear = 0;
        curMonth = 0;
        curDay = 0;
    }

    public static float curWindDirection()
    {
        if(Main.cur().mission == null)
            return 0.0F;
        else
            return Main.cur().mission.curWindDirection;
    }

    public static float curWindVelocity()
    {
        if(Main.cur().mission == null)
            return 0.0F;
        else
            return Main.cur().mission.curWindVelocity;
    }

    public static float curGust()
    {
        if(Main.cur().mission == null)
            return 0.0F;
        else
            return Main.cur().mission.curGust;
    }

    public static float curTurbulence()
    {
        if(Main.cur().mission == null)
            return 0.0F;
        else
            return Main.cur().mission.curTurbulence;
    }

    public static Mission cur()
    {
        return Main.cur().mission;
    }

    public static void BreakP()
    {
        System.out.print("");
    }

    public static void load(String s)
        throws Exception
    {
        load(s, false);
    }

    public static void load(String s, boolean flag)
        throws Exception
    {
        load("missions/", s, flag);
    }

    public static void load(String s, String s1)
        throws Exception
    {
        load(s, s1, false);
    }

    public static void load(String s, String s1, boolean flag)
        throws Exception
    {
        Mission mission = new Mission();
        if(cur() != null)
            cur().destroy();
        else
            mission.clear();
        mission.sectFile = new SectFile(s + s1);
        mission.load(s1, mission.sectFile, flag);
    }

    public static void loadFromSect(SectFile sectfile)
        throws Exception
    {
        loadFromSect(sectfile, false);
    }

    public static void loadFromSect(SectFile sectfile, boolean flag)
        throws Exception
    {
        Mission mission = new Mission();
        String s = sectfile.fileName();
        if(s != null && s.toLowerCase().startsWith("missions/"))
            s = s.substring("missions/".length());
        if(cur() != null)
            cur().destroy();
        else
            mission.clear();
        mission.sectFile = sectfile;
        mission.load(s, mission.sectFile, flag);
    }

    public String name()
    {
        return name;
    }

    public SectFile sectFile()
    {
        return sectFile;
    }

    public long finger()
    {
        return sectFinger;
    }

    public boolean isDestroyed()
    {
        return name == null;
    }

    public void destroy()
    {
        if(isDestroyed())
            return;
        if(bPlaying)
            doEnd();
        bPlaying = false;
        clear();
        name = null;
        Main.cur().mission = null;
        if(Main.cur().netMissionListener != null)
            Main.cur().netMissionListener.netMissionState(8, 0.0F, null);
        if(net != null && !net.isDestroyed())
            net.destroy();
        net = null;
        if(Config.isUSE_RENDER())
            CmdMusic.setCurrentVolume(1.0F);
    }

    private void clear()
    {
        if(net != null)
        {
            doReplicateNotMissionActors(false);
            if(net.masterChannel() != null)
                doReplicateNotMissionActors(net.masterChannel(), false);
        }
        actors.clear();
        beaconsRed.clear();
        beaconsBlue.clear();
        hasRadioStations = false;
        radioStationsLoaded = false;
        meaconsRed.clear();
        meaconsBlue.clear();
        hayrakeMap.clear();
        curActor = 0;
        NetUser netuser = (NetUser)NetEnv.host();
        if(netuser != null)
            netuser.resetTeamScores();
        ZutiSupportMethods.clear();
        ZutiRadarRefresh.reset();
        Maneuver.clear();
        ActorCrater.clear();
        if(GUI.pad != null)
            GUI.pad.zutiPadObjects.clear();
        DeviceLink.clearBuffers();
        resetDate();
        Main.cur().resetGame();
    }

    private void load(String s, SectFile sectfile, boolean flag)
        throws Exception
    {
        if(flag)
            BackgroundTask.execute(new BackgroundLoader(s, sectfile));
        else
            _load(s, sectfile, flag);
    }

    private void LOADING_STEP(float f, String s)
    {
        if(net != null && Main.cur().netMissionListener != null)
            Main.cur().netMissionListener.netMissionState(3, f, s);
        if(!BackgroundTask.step(f, s))
            throw new RuntimeException(BackgroundTask.executed().messageCancel());
        else
            return;
    }

    private void _load(String s, SectFile sectfile, boolean flag)
        throws Exception
    {
        if(!sectfile.sectionExist("$$$record"))
            FMMath.initSeed();
        if(GUI.pad != null)
            GUI.pad.zutiPadObjects.clear();
        zutiResetMissionVariables();
        AudioDevice.soundsOff();
        if(s != null)
            System.out.println("Loading mission " + s + "...");
        else
            System.out.println("Loading mission ...");
        EventLog.checkState();
        Main.cur().missionLoading = this;
        RTSConf.cur.time.setEnableChangePause1(false);
        Actor.setSpawnFromMission(true);
        try
        {
            Main.cur().mission = this;
            name = s;
            if(net == null)
                createNetObject(null, 0);
            loadMain(sectfile);
            loadRespawnTime(sectfile);
            Front.loadMission(sectfile);
            List list = null;
            if(Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isDogfight() || Main.cur().netServerParams.isSingle())
            {
                try
                {
                    list = loadWings(sectfile);
                }
                catch(Exception exception2)
                {
                    System.out.println("Mission error, ID_04: " + exception2.toString());
                    exception2.printStackTrace();
                }
                try
                {
                    loadChiefs(sectfile);
                }
                catch(Exception exception3)
                {
                    System.out.println("Mission error, ID_05: " + exception3.toString());
                }
            }
            try
            {
                loadHouses(sectfile);
            }
            catch(Exception exception4)
            {
                System.out.println("Mission error, ID_06.1: " + exception4.toString());
            }
            try
            {
                loadNStationary(sectfile);
            }
            catch(Exception exception5)
            {
                System.out.println("Mission error, ID_06.2: " + exception5.toString());
            }
            try
            {
                loadStationary(sectfile);
            }
            catch(Exception exception6)
            {
                System.out.println("Mission error, ID_06.3: " + exception6.toString());
            }
            try
            {
                loadRocketry(sectfile);
            }
            catch(Exception exception7)
            {
                System.out.println("Mission error, ID_06.4: " + exception7.toString());
            }
            try
            {
                loadViewPoint(sectfile);
            }
            catch(Exception exception8)
            {
                System.out.println("Mission error, ID_06.5: " + exception8.toString());
            }
            try
            {
                if(Main.cur().netServerParams.isDogfight())
                    loadBornPlaces(sectfile);
            }
            catch(Exception exception9)
            {
                System.out.println("Mission error, ID_07: " + exception9.toString());
            }
            if(Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isDogfight())
                try
                {
                    loadTargets(sectfile);
                }
                catch(Exception exception10)
                {
                    System.out.println("Mission error, ID_08: " + exception10.toString());
                }
            try
            {
                populateBeacons();
            }
            catch(Exception exception11)
            {
                System.out.println("Mission error, ID_09: " + exception11.toString());
            }
            try
            {
                populateRunwayLights();
            }
            catch(Exception exception12)
            {
                System.out.println("Mission error, ID_10: " + exception12.toString());
            }
            if(list != null)
            {
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Wing wing = (Wing)list.get(j);
                    try
                    {
                        if(Actor.isValid(wing))
                            wing.setOnAirport();
                    }
                    catch(Exception exception13)
                    {
                        exception13.printStackTrace();
                    }
                }

            }
        }
        catch(Exception exception)
        {
            if(net != null && Main.cur().netMissionListener != null)
                Main.cur().netMissionListener.netMissionState(4, 0.0F, exception.getMessage());
            printDebug(exception);
            clear();
            if(net != null && !net.isDestroyed())
                net.destroy();
            net = null;
            Main.cur().mission = null;
            name = null;
            Actor.setSpawnFromMission(false);
            Main.cur().missionLoading = null;
            setTime(false);
            throw exception;
        }
        if(Config.isUSE_RENDER())
        {
            if(Actor.isValid(World.getPlayerAircraft()))
                World.land().cubeFullUpdate((float)World.getPlayerAircraft().pos.getAbsPoint().z);
            else
                World.land().cubeFullUpdate(1000F);
            GUI.pad.fillAirports();
        }
        Actor.setSpawnFromMission(false);
        Main.cur().missionLoading = null;
        Main.cur().missionCounter++;
        setTime(!Main.cur().netServerParams.isSingle());
        LOADING_STEP(90F, "task.Load_humans");
        Paratrooper.PRELOAD();
        LOADING_STEP(95F, "task.Load_humans");
        if(Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isDogfight())
            Soldier.PRELOAD();
        LOADING_STEP(100F, "");
        if(Main.cur().netMissionListener != null)
            Main.cur().netMissionListener.netMissionState(5, 0.0F, null);
        if(net.isMirror())
        {
            try
            {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(4);
                net.masterChannel().userState = 4;
                net.postTo(net.masterChannel(), netmsgguaranted);
            }
            catch(Exception exception1)
            {
                printDebug(exception1);
            }
            ((NetUser)NetEnv.host()).missionLoaded();
        } else
        if(Main.cur().netServerParams.isSingle())
        {
            if(Main.cur() instanceof Main3D)
                ((Main3D)Main.cur()).ordersTree.missionLoaded();
            Main.cur().dotRangeFriendly.setDefault();
            Main.cur().dotRangeFoe.setDefault();
            Main.cur().dotRangeFoe.set(-1D, -1D, -1D, 5D, -1D, -1D);
            String s1 = "users/" + World.cur().userCfg.sId + "/Icons";
            RTSConf.cur.console.getEnv().exec("file " + s1);
        } else
        {
            ((NetUser)NetEnv.host()).replicateDotRange();
        }
        NetObj.tryReplicate(net, false);
        War.cur().missionLoaded();
        if(flag)
            Main.cur().mission = this;
    }

    private void setTime(boolean flag)
    {
        RTSConf _tmp = RTSConf.cur;
        Time.setSpeed(1.0F);
        RTSConf _tmp1 = RTSConf.cur;
        Time.setSpeedReal(1.0F);
        if(flag)
        {
            RTSConf.cur.time.setEnableChangePause1(false);
            RTSConf.cur.time.setEnableChangeSpeed(false);
            RTSConf.cur.time.setEnableChangeTickLen(true);
        } else
        {
            RTSConf.cur.time.setEnableChangePause1(true);
            RTSConf.cur.time.setEnableChangeSpeed(true);
            RTSConf.cur.time.setEnableChangeTickLen(false);
        }
    }

    private void loadZutis(SectFile sectfile)
    {
        try
        {
            Main.cur().mission.zutiRadar_ShipsAsRadar = false;
            if(sectfile.get("MDS", "MDS_Radar_ShipsAsRadar", 0, 0, 1) == 1)
                Main.cur().mission.zutiRadar_ShipsAsRadar = true;
            Main.cur().mission.zutiRadar_ShipRadar_MaxRange = sectfile.get("MDS", "MDS_Radar_ShipRadar_MaxRange", 100, 1, 0x1869f);
            Main.cur().mission.zutiRadar_ShipRadar_MinHeight = sectfile.get("MDS", "MDS_Radar_ShipRadar_MinHeight", 100, 0, 0x1869f);
            Main.cur().mission.zutiRadar_ShipRadar_MaxHeight = sectfile.get("MDS", "MDS_Radar_ShipRadar_MaxHeight", 5000, 1000, 0x1869f);
            Main.cur().mission.zutiRadar_ShipSmallRadar_MaxRange = sectfile.get("MDS", "MDS_Radar_ShipSmallRadar_MaxRange", 25, 1, 0x1869f);
            Main.cur().mission.zutiRadar_ShipSmallRadar_MinHeight = sectfile.get("MDS", "MDS_Radar_ShipSmallRadar_MinHeight", 0, 0, 0x1869f);
            Main.cur().mission.zutiRadar_ShipSmallRadar_MaxHeight = sectfile.get("MDS", "MDS_Radar_ShipSmallRadar_MaxHeight", 2000, 1000, 0x1869f);
            Main.cur().mission.zutiRadar_ScoutsAsRadar = false;
            if(sectfile.get("MDS", "MDS_Radar_ScoutsAsRadar", 0, 0, 1) == 1)
                Main.cur().mission.zutiRadar_ScoutsAsRadar = true;
            Main.cur().mission.zutiRadar_ScoutRadar_MaxRange = sectfile.get("MDS", "MDS_Radar_ScoutRadar_MaxRange", 2, 1, 0x1869f);
            Main.cur().mission.zutiRadar_ScoutRadar_DeltaHeight = sectfile.get("MDS", "MDS_Radar_ScoutRadar_DeltaHeight", 1500, 100, 0x1869f);
            Main.cur().mission.zutiRadar_ScoutCompleteRecon = false;
            if(sectfile.get("MDS", "MDS_Radar_ScoutCompleteRecon", 0, 0, 1) == 1)
                Main.cur().mission.zutiRadar_ScoutCompleteRecon = true;
            zutiLoadScouts_Red(sectfile);
            zutiLoadScouts_Blue(sectfile);
            Main.cur().mission.zutiRadar_RefreshInterval = sectfile.get("MDS", "MDS_Radar_RefreshInterval", 0, 0, 0x1869f) * 1000;
            Main.cur().mission.zutiRadar_DisableVectoring = false;
            if(sectfile.get("MDS", "MDS_Radar_DisableVectoring", 0, 0, 1) == 1)
                Main.cur().mission.zutiRadar_DisableVectoring = true;
            Main.cur().mission.zutiRadar_EnableTowerCommunications = true;
            if(sectfile.get("MDS", "MDS_Radar_EnableTowerCommunications", 1, 0, 1) == 0)
                Main.cur().mission.zutiRadar_EnableTowerCommunications = false;
            ZUTI_RADAR_IN_ADV_MODE = false;
            if(sectfile.get("MDS", "MDS_Radar_SetRadarToAdvanceMode", 0, 0, 1) == 1)
                ZUTI_RADAR_IN_ADV_MODE = true;
            Main.cur().mission.zutiRadar_HideUnpopulatedAirstripsFromMinimap = false;
            if(sectfile.get("MDS", "MDS_Radar_HideUnpopulatedAirstripsFromMinimap", 0, 0, 1) == 1)
                Main.cur().mission.zutiRadar_HideUnpopulatedAirstripsFromMinimap = true;
            Main.cur().mission.zutiRadar_ScoutGroundObjects_Alpha = sectfile.get("MDS", "MDS_Radar_ScoutGroundObjects_Alpha", 5, 1, 11);
            Main.cur().mission.zutiMisc_DisableAIRadioChatter = false;
            if(sectfile.get("MDS", "MDS_Misc_DisableAIRadioChatter", 0, 0, 1) == 1)
                Main.cur().mission.zutiMisc_DisableAIRadioChatter = true;
            Main.cur().mission.zutiMisc_DespawnAIPlanesAfterLanding = true;
            if(sectfile.get("MDS", "MDS_Misc_DespawnAIPlanesAfterLanding", 1, 0, 1) == 0)
                Main.cur().mission.zutiMisc_DespawnAIPlanesAfterLanding = false;
            Main.cur().mission.zutiMisc_HidePlayersCountOnHomeBase = false;
            if(sectfile.get("MDS", "MDS_Misc_HidePlayersCountOnHomeBase", 0, 0, 1) == 1)
                Main.cur().mission.zutiMisc_HidePlayersCountOnHomeBase = true;
            Main.cur().mission.zutiMisc_BombsCat1_CratersVisibilityMultiplier = sectfile.get("MDS", "MDS_Misc_BombsCat1_CratersVisibilityMultiplier", 1.0F, 1.0F, 99999F);
            Main.cur().mission.zutiMisc_BombsCat2_CratersVisibilityMultiplier = sectfile.get("MDS", "MDS_Misc_BombsCat2_CratersVisibilityMultiplier", 1.0F, 1.0F, 99999F);
            Main.cur().mission.zutiMisc_BombsCat3_CratersVisibilityMultiplier = sectfile.get("MDS", "MDS_Misc_BombsCat3_CratersVisibilityMultiplier", 1.0F, 1.0F, 99999F);
            zutiSetShipRadars();
        }
        catch(Exception exception)
        {
            System.out.println("Mission error, ID_11: " + exception.toString());
        }
    }

    private void loadMain(SectFile sectfile)
        throws Exception
    {
        int i = sectfile.get("MAIN", "TIMECONSTANT", 0, 0, 1);
        World.cur().setTimeOfDayConstant(i == 1);
        World.setTimeofDay(sectfile.get("MAIN", "TIME", 12F, 0.0F, 23.99F));
        loadZutis(sectfile);
        int j = sectfile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
        World.cur().setWeaponsConstant(j == 1);
        bigShipHpDiv = 1.0F / sectfile.get("MAIN", "ShipHP", 1.0F, 0.001F, 100F);
        String s = sectfile.get("MAIN", "MAP");
        if(s == null)
            throw new Exception("No MAP in mission file ");
        String s1 = null;
        int ai[] = null;
        SectFile sectfile1 = new SectFile("maps/" + s);
        int k = sectfile1.sectionIndex("static");
        if(k >= 0 && sectfile1.vars(k) > 0)
        {
            s1 = sectfile1.var(k, 0);
            if(s1 == null || s1.length() <= 0)
            {
                s1 = null;
            } else
            {
                s1 = HomePath.concatNames("maps/" + s, s1);
                ai = Statics.readBridgesEndPoints(s1);
            }
        }
        LOADING_STEP(0.0F, "task.Load_landscape");
        int l = sectfile.get("SEASON", "Year", 1940, 1900, Calendar.getInstance().get(Calendar.YEAR));
        int i1 = sectfile.get("SEASON", "Month", World.land().config.getDefaultMonth("maps/" + s), 1, 12);
        int j1 = sectfile.get("SEASON", "Day", 15, 1, 31);
        setDate(l, i1, j1);
        World.land().LoadMap(s, ai, i1, j1);
        World.cur().setCamouflage(World.land().config.camouflage);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().land2D != null)
            {
                if(!Main3D.cur3D().land2D.isDestroyed())
                    Main3D.cur3D().land2D.destroy();
                Main3D.cur3D().land2D = null;
            }
            Object obj = null;
            int k1 = sectfile1.sectionIndex("MAP2D");
            if(k1 >= 0)
            {
                int i2 = sectfile1.vars(k1);
                if(i2 > 0)
                {
                    LOADING_STEP(20F, "task.Load_map");
                    Main3D.cur3D().land2D = new Land2Dn(s, World.land().getSizeX(), World.land().getSizeY());
                }
            }
            if(Main3D.cur3D().land2DText == null)
                Main3D.cur3D().land2DText = new Land2DText();
            else
                Main3D.cur3D().land2DText.clear();
            int j2 = sectfile1.sectionIndex("text");
            if(j2 >= 0 && sectfile1.vars(j2) > 0)
            {
                LOADING_STEP(22F, "task.Load_landscape_texts");
                String s2 = sectfile1.var(j2, 0);
                Main3D.cur3D().land2DText.load(HomePath.concatNames("maps/" + s, s2));
            }
        }
        if(s1 != null)
        {
            LOADING_STEP(23F, "task.Load_static_objects");
            Statics.load(s1, World.cur().statics.bridges);
            Engine.drawEnv().staticTrimToSize();
        }
        Statics.trim();
        if(Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isDogfight())
            try
            {
                World.cur().statics.loadStateBridges(sectfile, false);
                World.cur().statics.loadStateHouses(sectfile, false);
                World.cur().statics.loadStateBridges(sectfile, true);
                World.cur().statics.loadStateHouses(sectfile, true);
                checkBridgesAndHouses(sectfile);
            }
            catch(Exception exception)
            {
                System.out.println("Mission error, ID_12: " + exception.toString());
            }
        if(Main.cur().netServerParams.isSingle())
        {
            player = sectfile.get("MAIN", "player");
            playerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);
        } else
        {
            player = null;
        }
        World.setMissionArmy(sectfile.get("MAIN", "army", 1, 1, 2));
        if(Config.isUSE_RENDER())
            Main3D.cur3D().ordersTree.setFrequency(OrdersTree.FREQ_FRIENDLY);
        if(Config.isUSE_RENDER())
        {
            LOADING_STEP(29F, "task.Load_landscape_effects");
            Main3D main3d = Main3D.cur3D();
            int l1 = sectfile.get("MAIN", "CloudType", 0, 0, 6);
            float f = sectfile.get("MAIN", "CloudHeight", 1000F, 300F, 5000F);
            createClouds(l1, f);
            if(!Config.cur.ini.get("game", "NoLensFlare", false))
            {
                main3d.sunFlareCreate();
                main3d.sunFlareShow(true);
            } else
            {
                main3d.sunFlareShow(false);
            }
            float f1 = (float)(s.charAt(0) - 64) * 3.141593F;
            f1 = sectfile.get("WEATHER", "WindDirection", f1, 0.0F, 359.99F);
            float f2 = 0.25F + (float)(l1 * l1) * 0.12F;
            f2 = sectfile.get("WEATHER", "WindSpeed", f2, 0.0F, 15F);
            float f3 = l1 > 3 ? (float)l1 * 2.0F : 0.0F;
            f3 = sectfile.get("WEATHER", "Gust", f3, 0.0F, 12F);
            float f4 = l1 > 2 ? l1 : 0.0F;
            f4 = sectfile.get("WEATHER", "Turbulence", f4, 0.0F, 6F);
            World.wind().set(f, f1, f2, f3, f4);
            for(int k2 = 0; k2 < 3; k2++)
            {
                Main3D.cur3D()._lightsGlare[k2].setShow(true);
                Main3D.cur3D()._sunGlare[k2].setShow(true);
            }

        }
    }

    public static void setDate(int i, int j, int k)
    {
        setYear(i);
        setMonth(j);
        setDay(k);
    }

    public static void setYear(int i)
    {
        if(i < 1900)
            i = 1900;
        if(i > Calendar.getInstance().get(Calendar.YEAR))
            i = Calendar.getInstance().get(Calendar.YEAR);
        curYear = i;
    }

    public static void setMonth(int i)
    {
        if(i < 1)
            i = 1;
        if(i > 12)
            i = 12;
        curMonth = i;
    }

    public static void setDay(int i)
    {
        if(i < 1)
            i = 1;
        if(i > 31)
            i = 31;
        curDay = i;
    }

    public static void setWindDirection(float f)
    {
        if(f < 0.0F)
            f = 0.0F;
        if(f > 359.99F)
            f = 0.0F;
        if(cur() != null)
            cur().curWindDirection = f;
    }

    public static void setWindVelocity(float f)
    {
        if(f < 0.0F)
            f = 0.0F;
        if(f > 15F)
            f = 15F;
        if(cur() != null)
            cur().curWindVelocity = f;
    }

    public static void setGust(float f)
    {
        if(f < 0.0F)
            f = 0.0F;
        if(f > 12F)
            f = 12F;
        if(cur() != null)
            cur().curGust = f;
    }

    public static void setTurbulence(float f)
    {
        if(f < 0.0F)
            f = 0.0F;
        if(f > 6F)
            f = 6F;
        if(cur() != null)
            cur().curTurbulence = f;
    }

    public static void createClouds(int i, float f)
    {
        if(i < 0)
            i = 0;
        if(i > 6)
            i = 6;
        if(cur() != null)
        {
            cur().curCloudsType = i;
            cur().curCloudsHeight = f;
        }
        if(!Config.isUSE_RENDER())
        {
            Main main = Main.cur();
            if(main.clouds != null)
                main.clouds.destroy();
            main.clouds = new EffClouds(World.cur().diffCur.NewCloudsRender, i, f);
            return;
        }
        Main3D main3d = Main3D.cur3D();
        Camera3D camera3d = (Camera3D)Actor.getByName("camera");
        if(main3d.clouds != null)
            main3d.clouds.destroy();
        main3d.clouds = new EffClouds(World.cur().diffCur.NewCloudsRender, i, f);
        if(i > 5)
            try
            {
                if(main3d.zip != null)
                    main3d.zip.destroy();
                main3d.zip = new Zip(f);
            }
            catch(Exception exception)
            {
                System.out.println("Zip load error: " + exception);
            }
        int j = 5 - i;
        if(i == 6)
            j = 1;
        if(j > 4)
            j = 4;
        RenderContext.cfgLandFogHaze.set(j);
        RenderContext.cfgLandFogHaze.apply();
        RenderContext.cfgLandFogHaze.reset();
        RenderContext.cfgLandFogLow.set(0);
        RenderContext.cfgLandFogLow.apply();
        RenderContext.cfgLandFogLow.reset();
        if(Actor.isValid(main3d.spritesFog))
            main3d.spritesFog.destroy();
        main3d.spritesFog = new SpritesFog(camera3d, 0.7F, 7000F, 7500F);
    }

    public static void setCloudsType(int i)
    {
        if(i < 0)
            i = 0;
        if(i > 6)
            i = 6;
        if(cur() != null)
            cur().curCloudsType = i;
        if(Main.cur().clouds != null)
            Main.cur().clouds.setType(i);
        int j = 5 - i;
        if(i == 6)
            j = 1;
        if(j > 4)
            j = 4;
        RenderContext.cfgLandFogHaze.set(j);
        RenderContext.cfgLandFogHaze.apply();
        RenderContext.cfgLandFogHaze.reset();
        RenderContext.cfgLandFogLow.set(0);
        RenderContext.cfgLandFogLow.apply();
        RenderContext.cfgLandFogLow.reset();
    }

    public static void setCloudsHeight(float f)
    {
        if(cur() != null)
            cur().curCloudsHeight = f;
        if(Main.cur().clouds != null)
            Main.cur().clouds.setHeight(f);
    }

    private void loadRespawnTime(SectFile sectfile)
    {
        respawnMap.clear();
        int i = sectfile.sectionIndex("RespawnTime");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        for(int k = 0; k < j; k++)
        {
            String s = sectfile.var(i, k);
            float f = sectfile.get("RespawnTime", s, 1800F, 20F, 1000000F);
            respawnMap.put(s, new Float(f));
        }

    }

    private List loadWings(SectFile sectfile)
        throws Exception
    {
        int i = sectfile.sectionIndex("Wing");
        if(i < 0)
            return null;
        if(!World.cur().diffCur.Takeoff_N_Landing)
            prepareTakeoff(sectfile, !Main.cur().netServerParams.isSingle());
        NetChannel netchannel = null;
        if(!isServer())
            netchannel = net.masterChannel();
        int j = sectfile.vars(i);
        ArrayList arraylist = null;
        if(j > 0)
            arraylist = new ArrayList(j);
        for(int k = 0; k < j; k++)
        {
            LOADING_STEP(30 + Math.round(((float)k / (float)j) * 30F), "task.Load_aircraft");
            String s = sectfile.var(i, k);
            _loadPlayer = s.equals(player);
            int l = sectfile.get(s, "StartTime", 0);
            if(l > 0 && !_loadPlayer)
            {
                if(netchannel == null)
                {
                    double d = (long)l * 60L;
                    new MsgAction(0, d, new TimeOutWing(s)) {

                        public void doAction(Object obj)
                        {
                            TimeOutWing timeoutwing = (TimeOutWing)obj;
                            timeoutwing.start();
                        }

                    }
;
                }
            } else
            {
                NetAircraft.loadingCoopPlane = Main.cur().netServerParams != null && Main.cur().netServerParams.isCoop();
                Wing wing = new Wing();
                wing.load(sectfile, s, netchannel);
                if(netchannel != null && !Main.cur().netServerParams.isCoop())
                {
                    Aircraft aaircraft[] = wing.airc;
                    for(int i1 = 0; i1 < aaircraft.length; i1++)
                        if(Actor.isValid(aaircraft[i1]) && aaircraft[i1].net == null)
                        {
                            aaircraft[i1].destroy();
                            aaircraft[i1] = null;
                        }

                }
                arraylist.add(wing);
                prepareSkinInWing(sectfile, wing);
            }
        }

        LOADING_STEP(60F, "task.Load_aircraft");
        return arraylist;
    }

    private void prepareSkinInWing(SectFile sectfile, Wing wing)
    {
        if(!Config.isUSE_RENDER())
            return;
        Aircraft aaircraft[] = wing.airc;
        for(int i = 0; i < aaircraft.length; i++)
            if(Actor.isValid(aaircraft[i]))
            {
                Aircraft aircraft = aaircraft[i];
                prepareSkinInWing(sectfile, aircraft, wing.name(), i);
            }

    }

    private void prepareSkinInWing(SectFile sectfile, Aircraft aircraft, String s, int i)
    {
        if(!Config.isUSE_RENDER())
            return;
        if(World.getPlayerAircraft() == aircraft)
        {
            if(isSingle())
                if(NetMissionTrack.isPlaying())
                {
                    ((NetUser)Main.cur().netServerParams.host()).tryPrepareSkin(aircraft);
                    ((NetUser)Main.cur().netServerParams.host()).tryPrepareNoseart(aircraft);
                    ((NetUser)Main.cur().netServerParams.host()).tryPreparePilot(aircraft);
                } else
                {
                    String s1 = Property.stringValue(aircraft.getClass(), "keyName", null);
                    String s3 = World.cur().userCfg.getSkin(s1);
                    if(s3 != null)
                    {
                        String s6 = GUIAirArming.validateFileName(s1);
                        ((NetUser)NetEnv.host()).setSkin(s6 + "/" + s3);
                        ((NetUser)NetEnv.host()).tryPrepareSkin(aircraft);
                    } else
                    {
                        ((NetUser)NetEnv.host()).setSkin(null);
                    }
                    String s7 = World.cur().userCfg.getNoseart(s1);
                    if(s7 != null)
                    {
                        ((NetUser)NetEnv.host()).setNoseart(s7);
                        ((NetUser)NetEnv.host()).tryPrepareNoseart(aircraft);
                    } else
                    {
                        ((NetUser)NetEnv.host()).setNoseart(null);
                    }
                    String s11 = World.cur().userCfg.netPilot;
                    ((NetUser)NetEnv.host()).setPilot(s11);
                    if(s11 != null)
                        ((NetUser)NetEnv.host()).tryPreparePilot(aircraft);
                }
        } else
        {
            String s2 = sectfile.get(s, "skin" + i, (String)null);
            if(s2 != null)
            {
                String s4 = Aircraft.getPropertyMesh(aircraft.getClass(), aircraft.getRegiment().country());
                s2 = GUIAirArming.validateFileName(Property.stringValue(aircraft.getClass(), "keyName", null)) + "/" + s2;
                if(NetMissionTrack.isPlaying())
                {
                    s2 = NetFilesTrack.getLocalFileName(Main.cur().netFileServerSkin, Main.cur().netServerParams.host(), s2);
                    if(s2 != null)
                        s2 = Main.cur().netFileServerSkin.alternativePath() + "/" + s2;
                } else
                {
                    s2 = Main.cur().netFileServerSkin.primaryPath() + "/" + s2;
                }
                if(s2 != null)
                {
                    String s8 = "PaintSchemes/Cache/" + Finger.file(0L, s2, -1);
                    Aircraft.prepareMeshSkin(s4, aircraft.hierMesh(), s2, s8, aircraft.getRegiment());
                }
            }
            String s5 = sectfile.get(s, "noseart" + i, (String)null);
            if(s5 != null)
            {
                String s9 = Main.cur().netFileServerNoseart.primaryPath() + "/" + s5;
                String s12 = s5.substring(0, s5.length() - 4);
                if(NetMissionTrack.isPlaying())
                {
                    s9 = NetFilesTrack.getLocalFileName(Main.cur().netFileServerNoseart, Main.cur().netServerParams.host(), s5);
                    if(s9 != null)
                    {
                        s12 = s9.substring(0, s9.length() - 4);
                        s9 = Main.cur().netFileServerNoseart.alternativePath() + "/" + s9;
                    }
                }
                if(s9 != null)
                {
                    String s14 = "PaintSchemes/Cache/Noseart0" + s12 + ".tga";
                    String s16 = "PaintSchemes/Cache/Noseart0" + s12 + ".mat";
                    String s18 = "PaintSchemes/Cache/Noseart1" + s12 + ".tga";
                    String s20 = "PaintSchemes/Cache/Noseart1" + s12 + ".mat";
                    if(BmpUtils.bmp8PalTo2TGA4(s9, s14, s18))
                        Aircraft.prepareMeshNoseart(aircraft.hierMesh(), s16, s20, s14, s18, null);
                }
            }
            String s10 = sectfile.get(s, "pilot" + i, (String)null);
            if(s10 != null)
            {
                String s13 = Main.cur().netFileServerPilot.primaryPath() + "/" + s10;
                String s15 = s10.substring(0, s10.length() - 4);
                if(NetMissionTrack.isPlaying())
                {
                    s13 = NetFilesTrack.getLocalFileName(Main.cur().netFileServerPilot, Main.cur().netServerParams.host(), s10);
                    if(s13 != null)
                    {
                        s15 = s13.substring(0, s13.length() - 4);
                        s13 = Main.cur().netFileServerPilot.alternativePath() + "/" + s13;
                    }
                }
                if(s13 != null)
                {
                    String s17 = "PaintSchemes/Cache/Pilot" + s15 + ".tga";
                    String s19 = "PaintSchemes/Cache/Pilot" + s15 + ".mat";
                    if(BmpUtils.bmp8PalToTGA3(s13, s17))
                        Aircraft.prepareMeshPilot(aircraft.hierMesh(), 0, s19, s17, null);
                }
            }
        }
    }

    public void prepareSkinAI(Aircraft aircraft)
    {
        String s = aircraft.name();
        if(s.length() < 4)
            return;
        String s1 = s.substring(0, s.length() - 1);
        int i = 0;
        try
        {
            i = Integer.parseInt(s.substring(s.length() - 1, s.length()));
        }
        catch(Exception exception)
        {
            return;
        }
        prepareSkinInWing(sectFile, aircraft, s1, i);
    }

    public void recordNetFiles()
    {
        int i = sectFile.sectionIndex("Wing");
        if(i >= 0)
        {
            int j = sectFile.vars(i);
            for(int l = 0; l < j; l++)
                try
                {
                    String s = sectFile.var(i, l);
                    String s1 = sectFile.get(s, "Class", (String)null);
                    Class class1 = ObjIO.classForName(s1);
                    String s4 = GUIAirArming.validateFileName(Property.stringValue(class1, "keyName", null));
                    for(int j1 = 0; j1 < 4; j1++)
                    {
                        String s6 = sectFile.get(s, "skin" + j1, (String)null);
                        if(s6 != null)
                            recordNetFile(Main.cur().netFileServerSkin, s4 + "/" + s6);
                        recordNetFile(Main.cur().netFileServerNoseart, sectFile.get(s, "noseart" + j1, (String)null));
                        recordNetFile(Main.cur().netFileServerPilot, sectFile.get(s, "pilot" + j1, (String)null));
                    }

                }
                catch(Exception exception)
                {
                    printDebug(exception);
                }

        }
        i = sectFile.sectionIndex("NStationary");
        if(i < 0)
            return;
        int k = sectFile.vars(i);
        for(int i1 = 0; i1 < k; i1++)
            try
            {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectFile.line(i, i1));
                numbertokenizer.next("");
                String s2 = numbertokenizer.next("");
                numbertokenizer.next(0);
                numbertokenizer.next(0.0D);
                numbertokenizer.next(0.0D);
                numbertokenizer.next(0.0F);
                numbertokenizer.next(0.0F);
                numbertokenizer.next((String)null);
                numbertokenizer.next((String)null);
                numbertokenizer.next((String)null);
                String s3 = numbertokenizer.next((String)null);
                if(s3 != null && !s3.equalsIgnoreCase("null"))
                {
                    s2 = "air." + s2.substring(s2.indexOf("$") + 1);
                    Class class2 = ObjIO.classForName(s2);
                    String s5 = GUIAirArming.validateFileName(Property.stringValue(class2, "keyName", null));
                    if(s3.indexOf("\\") != -1)
                    {
                        char ac[] = s3.toCharArray();
                        for(int k1 = 0; k1 < ac.length; k1++)
                            if(ac[k1] == '\\')
                                ac[k1] = ' ';

                        s3 = new String(ac);
                    }
                    recordNetFile(Main.cur().netFileServerSkin, s5 + "/" + s3);
                }
            }
            catch(Exception exception1)
            {
                printDebug(exception1);
            }

    }

    private void recordNetFile(NetFileServerDef netfileserverdef, String s)
    {
        if(s == null)
            return;
        String s1 = s;
        if(NetMissionTrack.isPlaying())
        {
            s1 = NetFilesTrack.getLocalFileName(netfileserverdef, Main.cur().netServerParams.host(), s);
            if(s1 == null)
                return;
        }
        NetFilesTrack.recordFile(netfileserverdef, (NetUser)Main.cur().netServerParams.host(), s, s1);
    }

    public Aircraft loadAir(SectFile sectfile, String s, String s1, String s2, int i)
        throws Exception
    {
        boolean flag = !isServer();
        Class class1 = ObjIO.classForName(s);
        Aircraft aircraft = (Aircraft)class1.newInstance();
        if(Main.cur().netServerParams.isSingle() && _loadPlayer)
        {
            if(Property.value(class1, "cockpitClass", null) == null)
                throw new Exception("One of selected aircraft has no cockpit.");
            if(playerNum == 0)
            {
                World.setPlayerAircraft(aircraft);
                _loadPlayer = false;
            } else
            {
                playerNum--;
            }
        }
        aircraft.setName(s2);
        int j = 0;
        if(flag)
        {
            j = ((Integer)actors.get(curActor)).intValue();
            if(j == 0)
                aircraft.load(sectfile, s1, i, null, 0);
            else
                aircraft.load(sectfile, s1, i, net.masterChannel(), j);
        } else
        {
            aircraft.load(sectfile, s1, i, null, 0);
        }
        if(aircraft.isSpawnFromMission())
            if(net.isMirror())
            {
                if(j == 0)
                    actors.set(curActor++, null);
                else
                    actors.set(curActor++, aircraft);
            } else
            {
                actors.add(aircraft);
            }
        aircraft.pos.reset();
        return aircraft;
    }

    public static void preparePlayerNumberOn(SectFile sectfile)
    {
        UserCfg usercfg = World.cur().userCfg;
        String s = sectfile.get("MAIN", "player", "");
        if("".equals(s))
        {
            return;
        } else
        {
            String s1 = sectfile.get("MAIN", "playerNum", "");
            sectfile.set(s, "numberOn" + s1, usercfg.netNumberOn ? "1" : "0");
            return;
        }
    }

    private void prepareTakeoff(SectFile sectfile, boolean flag)
    {
        if(flag)
        {
            int i = sectfile.sectionIndex("Wing");
            if(i < 0)
                return;
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
                prepareWingTakeoff(sectfile, sectfile.var(i, k));

        } else
        {
            String s = sectfile.get("MAIN", "player", (String)null);
            if(s == null)
                return;
            prepareWingTakeoff(sectfile, s);
        }
        sectFinger = sectfile.fingerExcludeSectPrefix("$$$");
    }

    private void prepareWingTakeoff(SectFile sectfile, String s)
    {
        int i;
        ArrayList arraylist;
label0:
        {
            String s1 = s + "_Way";
            i = sectfile.sectionIndex(s1);
            if(i < 0)
                return;
            int j = sectfile.vars(i);
            if(j == 0)
                return;
            arraylist = new ArrayList(j);
            boolean flag = true;
            for(int k = 0; k < j; k++)
            {
                String s2 = sectfile.line(i, k);
                if(k == 0)
                    arraylist.add(s2);
                if(!s2.startsWith("TRIGGERS") || !flag)
                {
                    if(!s2.startsWith("TAKEOFF"))
                        arraylist.add(s2);
                    if(s2.startsWith("NORMFLY"))
                        flag = false;
                }
            }

            String s3 = (String)arraylist.get(0);
            if(!s3.startsWith("TAKEOFF"))
                return;
            StringBuffer stringbuffer = new StringBuffer("NORMFLY");
            NumberTokenizer numbertokenizer = new NumberTokenizer(s3);
            numbertokenizer.next((String)null);
            double d = numbertokenizer.next(1000D);
            double d1 = numbertokenizer.next(1000D);
            WingTakeoffPos wingtakeoffpos = new WingTakeoffPos(d, d1);
            if(mapWingTakeoff == null)
            {
                mapWingTakeoff = new HashMap();
                mapWingTakeoff.put(wingtakeoffpos, wingtakeoffpos);
            } else
            {
                do
                {
                    WingTakeoffPos wingtakeoffpos1 = (WingTakeoffPos)mapWingTakeoff.get(wingtakeoffpos);
                    if(wingtakeoffpos1 == null)
                    {
                        mapWingTakeoff.put(wingtakeoffpos, wingtakeoffpos);
                        break;
                    }
                    wingtakeoffpos.x += 200;
                } while(true);
            }
            d = wingtakeoffpos.x;
            d1 = wingtakeoffpos.y;
            stringbuffer.append(" ");
            stringbuffer.append(d);
            stringbuffer.append(" ");
            stringbuffer.append(d1);
            if(j > 1)
            {
                String s4 = (String)arraylist.get(1);
                if(!s4.startsWith("TAKEOFF") && !s4.startsWith("TRIGGERS") && !s4.startsWith("LANDING"))
                {
                    NumberTokenizer numbertokenizer1 = new NumberTokenizer(s4);
                    numbertokenizer1.next((String)null);
                    numbertokenizer1.next((String)null);
                    numbertokenizer1.next((String)null);
                    stringbuffer.append(" ");
                    stringbuffer.append(numbertokenizer1.next("1000.0"));
                    stringbuffer.append(" ");
                    stringbuffer.append(numbertokenizer1.next("300.0"));
                    arraylist.set(0, stringbuffer.toString());
                    break label0;
                }
            }
            stringbuffer.append(" 1000 300");
            arraylist.set(0, stringbuffer.toString());
        }
        sectfile.sectionClear(i);
        for(int l = 0; l < arraylist.size(); l++)
            sectfile.lineAdd(i, (String)arraylist.get(l));

    }

    private void loadChiefs(SectFile sectfile)
        throws Exception
    {
        int i = sectfile.sectionIndex("Chiefs");
        if(i < 0)
            return;
        if(chiefsIni == null)
            chiefsIni = new SectFile("com/maddox/il2/objects/chief.ini");
        int j = sectfile.vars(i);
        for(int k = 0; k < j; k++)
        {
            LOADING_STEP(60 + Math.round(((float)k / (float)j) * 20F), "task.Load_tanks");
            NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
            String s = numbertokenizer.next();
            String s1 = numbertokenizer.next();
            int l = numbertokenizer.next(-1);
            if(l < 0)
            {
                System.out.println("Mission: Wrong chief's army [" + l + "]");
                continue;
            }
            Chief.new_DELAY_WAKEUP = numbertokenizer.next(0.0F);
            Chief.new_SKILL_IDX = numbertokenizer.next(2);
            if(Chief.new_SKILL_IDX < 0 || Chief.new_SKILL_IDX > 3)
            {
                System.out.println("Mission: Wrong chief's skill [" + Chief.new_SKILL_IDX + "]");
                continue;
            }
            Chief.new_SLOWFIRE_K = numbertokenizer.next(1.0F);
            if(Chief.new_SLOWFIRE_K < 0.5F || Chief.new_SLOWFIRE_K > 100F)
            {
                System.out.println("Mission: Wrong chief's slowfire [" + Chief.new_SLOWFIRE_K + "]");
                continue;
            }
            if(chiefsIni.sectionIndex(s1) < 0)
            {
                System.out.println("Mission: Wrong chief's type [" + s1 + "]");
                continue;
            }
            int i1 = s1.indexOf('.');
            if(i1 <= 0)
            {
                System.out.println("Mission: Wrong chief's type [" + s1 + "]");
                continue;
            }
            String s2 = s1.substring(0, i1);
            String s3 = s1.substring(i1 + 1);
            String s4 = chiefsIni.get(s2, s3);
            if(s4 == null)
            {
                System.out.println("Mission: Wrong chief's type [" + s1 + "]");
                continue;
            }
            numbertokenizer = new NumberTokenizer(s4);
            s4 = numbertokenizer.nextToken();
            numbertokenizer.nextToken();
            String s5 = null;
            if(numbertokenizer.hasMoreTokens())
                s5 = numbertokenizer.nextToken();
            Class class1 = ObjIO.classForName(s4);
            if(class1 == null)
            {
                System.out.println("Mission: Unknown chief's class [" + s4 + "]");
                continue;
            }
            Constructor constructor;
            try
            {
                Class aclass[] = new Class[6];
                aclass[0] = java.lang.String.class;
                aclass[1] = Integer.TYPE;
                aclass[2] = com.maddox.rts.SectFile.class;
                aclass[3] = java.lang.String.class;
                aclass[4] = com.maddox.rts.SectFile.class;
                aclass[5] = java.lang.String.class;
                constructor = class1.getConstructor(aclass);
            }
            catch(Exception exception)
            {
                System.out.println("Mission: No required constructor in chief's class [" + s4 + "]");
                continue;
            }
            int j1 = curActor;
            Object obj;
            try
            {
                Object aobj[] = new Object[6];
                aobj[0] = s;
                aobj[1] = new Integer(l);
                aobj[2] = chiefsIni;
                aobj[3] = s1;
                aobj[4] = sectfile;
                aobj[5] = s + "_Road";
                obj = constructor.newInstance(aobj);
            }
            catch(Exception exception1)
            {
                System.out.println("Mission: Can't create chief '" + s + "' [class:" + s4 + "]");
                continue;
            }
            if(s5 != null)
                ((Actor)obj).icon = IconDraw.get(s5);
            if(j1 != curActor && net != null && net.isMirror())
            {
                for(int k1 = j1; k1 < curActor; k1++)
                {
                    Actor actor = (Actor)actors.get(k1);
                    if(actor.net == null || actor.net.isMaster())
                    {
                        if(Actor.isValid(actor))
                        {
                            if(obj instanceof ChiefGround)
                                ((ChiefGround)obj).Detach(actor, actor);
                            actor.destroy();
                        }
                        actors.set(k1, null);
                    }
                }

            }
            if(obj instanceof ChiefGround)
                ((ChiefGround)obj).dreamFire(true);
        }

    }

    public int getUnitNetIdRemote(Actor actor)
    {
        if(net.isMaster())
        {
            actors.add(actor);
            return 0;
        } else
        {
            Integer integer = (Integer)actors.get(curActor);
            actors.set(curActor, actor);
            curActor++;
            return integer.intValue();
        }
    }

    private Actor loadStationaryActor(String s, String s1, int i, double d, double d1, 
            float f, float f1, String s2, String s3, String s4, String s5, int j)
    {
        Class class1 = null;
        try
        {
            class1 = ObjIO.classForName(s1);
        }
        catch(Exception exception)
        {
            System.out.println("Mission: class '" + s1 + "' not found");
            return null;
        }
        ActorSpawn actorspawn = (ActorSpawn)Spawn.get(class1.getName(), false);
        if(actorspawn == null)
        {
            System.out.println("Mission: ActorSpawn for '" + s1 + "' not found");
            return null;
        }
        spawnArg.clear();
        if(s != null)
        {
            if("NONAME".equals(s))
            {
                System.out.println("Mission: 'NONAME' - not valid actor name");
                return null;
            }
            if(Actor.getByName(s) != null)
            {
                System.out.println("Mission: actor '" + s + "' alredy exist");
                return null;
            }
            spawnArg.name = s;
        }
        spawnArg.army = i;
        spawnArg.armyExist = true;
        spawnArg.country = s2;
        Chief.new_DELAY_WAKEUP = 0.0F;
        ArtilleryGeneric.new_RADIUS_HIDE = 0.0F;
        if(s2 != null)
            try
            {
                Chief.new_DELAY_WAKEUP = Integer.parseInt(s2);
                ArtilleryGeneric.new_RADIUS_HIDE = Chief.new_DELAY_WAKEUP;
            }
            catch(Exception exception1) { }
        Chief.new_SKILL_IDX = 2;
        ArtilleryGeneric.new_SKILL = 1;
        if(s3 != null)
        {
            try
            {
                int k = Integer.parseInt(s3);
                Chief.new_SKILL_IDX = k;
                ArtilleryGeneric.new_SKILL = k;
                spawnArg.skill = k;
            }
            catch(Exception exception2) { }
            if(Chief.new_SKILL_IDX < 0 || Chief.new_SKILL_IDX > 3)
            {
                System.out.println("Mission: Wrong actor skill '" + Chief.new_SKILL_IDX + "'");
                return null;
            }
        }
        Chief.new_SLOWFIRE_K = 1.0F;
        if(s4 != null)
        {
            try
            {
                Chief.new_SLOWFIRE_K = Float.parseFloat(s4);
                if(Chief.new_SLOWFIRE_K > 1.0F)
                    ArtilleryGeneric.new_SPOTTER = true;
                else
                    ArtilleryGeneric.new_SPOTTER = false;
            }
            catch(Exception exception3) { }
            if(Chief.new_SLOWFIRE_K < 0.5F || Chief.new_SLOWFIRE_K > 100F)
            {
                System.out.println("Mission: Wrong actor slowfire '" + Chief.new_SLOWFIRE_K + "'");
                return null;
            }
        } else
        {
            ArtilleryGeneric.new_SPOTTER = false;
        }
        p.set(d, d1, 0.0D);
        spawnArg.point = p;
        o.set(f, 0.0F, 0.0F);
        spawnArg.orient = o;
        if(f1 > 0.0F)
        {
            spawnArg.timeLenExist = true;
            spawnArg.timeLen = f1;
        }
        if(j == 1 || j == -1)
            spawnArg.bNumberOn = true;
        else
            spawnArg.bNumberOn = false;
        spawnArg.paramFileName = s5;
        spawnArg.netChannel = null;
        spawnArg.netIdRemote = 0;
        if(net.isMirror())
        {
            spawnArg.netChannel = net.masterChannel();
            spawnArg.netIdRemote = ((Integer)actors.get(curActor)).intValue();
            if(spawnArg.netIdRemote == 0)
            {
                actors.set(curActor++, null);
                return null;
            }
        }
        Actor actor = null;
        try
        {
            actor = actorspawn.actorSpawn(spawnArg);
        }
        catch(Exception exception4)
        {
            System.out.println(exception4.getMessage());
            exception4.printStackTrace();
        }
        if(net.isMirror())
            actors.set(curActor++, actor);
        else
            actors.add(actor);
        return actor;
    }

    private void loadStationary(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("Stationary");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        for(int k = 0; k < j; k++)
        {
            LOADING_STEP(80 + Math.round(((float)k / (float)j) * 5F), "task.Load_stationary_objects");
            NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
            loadStationaryActor(null, numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0D), numbertokenizer.next(0.0D), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next(-1));
        }

    }

    private void loadNStationary(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("NStationary");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        for(int k = 0; k < j; k++)
        {
            LOADING_STEP(85 + Math.round(((float)k / (float)j) * 5F), "task.Load_stationary_objects");
            NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
            loadStationaryActor(numbertokenizer.next(""), numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0D), numbertokenizer.next(0.0D), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next(-1));
        }

    }

    private void loadRocketry(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("Rocket");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        for(int k = 0; k < j; k++)
        {
            NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
            if(!numbertokenizer.hasMoreTokens())
                continue;
            String s = numbertokenizer.next("");
            if(!numbertokenizer.hasMoreTokens())
                continue;
            String s1 = numbertokenizer.next("");
            if(!numbertokenizer.hasMoreTokens())
                continue;
            int l = numbertokenizer.next(1, 1, 2);
            double d = numbertokenizer.next(0.0D);
            if(!numbertokenizer.hasMoreTokens())
                continue;
            double d1 = numbertokenizer.next(0.0D);
            if(!numbertokenizer.hasMoreTokens())
                continue;
            float f = numbertokenizer.next(0.0F);
            if(!numbertokenizer.hasMoreTokens())
                continue;
            float f1 = numbertokenizer.next(0.0F);
            int i1 = numbertokenizer.next(1);
            float f2 = numbertokenizer.next(20F);
            Point2d point2d = null;
            if(numbertokenizer.hasMoreTokens())
                point2d = new Point2d(numbertokenizer.next(0.0D), numbertokenizer.next(0.0D));
            NetChannel netchannel = null;
            int j1 = 0;
            if(net.isMirror())
            {
                netchannel = net.masterChannel();
                j1 = ((Integer)actors.get(curActor)).intValue();
                if(j1 == 0)
                {
                    actors.set(curActor++, null);
                    continue;
                }
            }
            RocketryGeneric rocketrygeneric = null;
            try
            {
                rocketrygeneric = RocketryGeneric.New(s, s1, netchannel, j1, l, d, d1, f, f1, i1, f2, point2d);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
            if(net.isMirror())
                actors.set(curActor++, rocketrygeneric);
            else
                actors.add(rocketrygeneric);
        }

    }

    private void loadHouses(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("Buildings");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        if(j == 0)
            return;
        if(net.isMirror())
        {
            spawnArg.netChannel = net.masterChannel();
            spawnArg.netIdRemote = ((Integer)actors.get(curActor)).intValue();
            HouseManager housemanager = new HouseManager(sectfile, "Buildings", net.masterChannel(), ((Integer)actors.get(curActor)).intValue());
            actors.set(curActor++, housemanager);
        } else
        {
            HouseManager housemanager1 = new HouseManager(sectfile, "Buildings", null, 0);
            actors.add(housemanager1);
        }
    }

    private void loadBornPlaces(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("BornPlace");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        if(j == 0)
            return;
        if(World.cur().airdrome == null)
            return;
        if(World.cur().airdrome.stay == null)
            return;
        World.cur().bornPlaces = new ArrayList(j);
        for(int k = 0; k < j; k++)
        {
            NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
            int l = numbertokenizer.next(0, 0, Army.amountNet() - 1);
            float f = numbertokenizer.next(1000, 500, 10000);
            double d = f * f;
            float f1 = numbertokenizer.next(0);
            float f2 = numbertokenizer.next(0);
            boolean flag = numbertokenizer.next(1) == 1;
            int i1 = 1000;
            int j1 = 200;
            int k1 = 0;
            int l1 = 0;
            int i2 = 0;
            int j2 = 5000;
            int k2 = 50;
            boolean flag1 = false;
            boolean flag2 = false;
            boolean flag3 = false;
            boolean flag4 = false;
            boolean flag5 = false;
            boolean flag6 = false;
            boolean flag7 = false;
            double d1 = 3.7999999999999998D;
            boolean flag8 = false;
            boolean flag9 = false;
            boolean flag10 = false;
            int l2 = 0;
            try
            {
                i1 = numbertokenizer.next(1000, 0, 10000);
                l2++;
                j1 = numbertokenizer.next(200, 0, 500);
                l2++;
                k1 = numbertokenizer.next(0, 0, 360);
                l2++;
                l1 = numbertokenizer.next(0, 0, 0x1869f);
                l2++;
                i2 = numbertokenizer.next(0, 0, 0x1869f);
                l2++;
                j2 = numbertokenizer.next(5000, 0, 0x1869f);
                l2++;
                k2 = numbertokenizer.next(50, 1, 0x1869f);
                l2++;
                if(numbertokenizer.next(0, 0, 1) == 1)
                {
                    flag2 = true;
                    l2++;
                }
                if(numbertokenizer.next(0, 0, 1) == 1)
                {
                    flag3 = true;
                    l2++;
                }
                if(numbertokenizer.next(0, 0, 1) == 1)
                {
                    flag4 = true;
                    l2++;
                }
                if(numbertokenizer.next(0, 0, 1) == 1)
                {
                    flag6 = true;
                    l2++;
                }
                if(numbertokenizer.next(0, 0, 1) == 1)
                {
                    flag7 = true;
                    l2++;
                }
                d1 = numbertokenizer.next(3.7999999999999998D, 0.0D, 10D);
                if(numbertokenizer.next(0, 0, 1) == 1)
                {
                    flag5 = true;
                    l2++;
                }
                if(numbertokenizer.next(0, 0, 1) == 1)
                {
                    flag8 = true;
                    l2++;
                }
                if(numbertokenizer.next(0, 0, 1) == 1)
                {
                    flag1 = true;
                    l2++;
                }
                if(numbertokenizer.next(0, 0, 1) == 1)
                {
                    flag9 = true;
                    l2++;
                }
                if(numbertokenizer.next(0, 0, 1) == 1)
                {
                    flag10 = true;
                    l2++;
                }
            }
            catch(Exception exception1)
            {
                System.out.println("Mission: no air spawn entries defined for HomeBase nr. " + k + ". value index: " + l2);
            }
            boolean flag11 = false;
            Point_Stay apoint_stay[][] = World.cur().airdrome.stay;
            if(!isDogfight())
            {
                flag11 = true;
            } else
            {
                for(int i3 = 0; i3 < apoint_stay.length; i3++)
                {
                    if(apoint_stay[i3] == null)
                        continue;
                    Point_Stay point_stay = apoint_stay[i3][apoint_stay[i3].length - 1];
                    if((double)((point_stay.x - f1) * (point_stay.x - f1) + (point_stay.y - f2) * (point_stay.y - f2)) > d)
                        continue;
                    flag11 = true;
                    break;
                }

            }
            if(flag11)
            {
                BornPlace bornplace = new BornPlace(f1, f2, l, f);
                bornplace.zutiRadarHeight_MIN = i2;
                bornplace.zutiRadarHeight_MAX = j2;
                bornplace.zutiRadarRange = k2;
                bornplace.zutiSpawnHeight = i1;
                bornplace.zutiSpawnSpeed = j1;
                bornplace.zutiSpawnOrient = k1;
                bornplace.zutiMaxBasePilots = l1;
                bornplace.zutiAirspawnIfCarrierFull = flag1;
                bornplace.zutiAirspawnOnly = flag2;
                bornplace.zutiDisableSpawning = flag6;
                bornplace.zutiEnableFriction = flag7;
                bornplace.zutiFriction = d1;
                bornplace.zutiEnablePlaneLimits = flag3;
                bornplace.zutiDecreasingNumberOfPlanes = flag4;
                bornplace.zutiIncludeStaticPlanes = flag5;
                bornplace.zutiBpIndex = k;
                bornplace.zutiStaticPositionOnly = flag8;
                bornplace.spawnFromStationatyPlanes = flag9;
                bornplace.restoreOriginalSpawnPoint = flag10;
                bornplace.bParachute = flag;
                World.cur().bornPlaces.add(bornplace);
                if(actors != null)
                {
                    int j3 = actors.size();
                    for(int l3 = 0; l3 < j3; l3++)
                    {
                        Actor actor = (Actor)actors.get(l3);
                        if(Actor.isValid(actor) && actor.pos != null && ZutiSupportMethods.isStaticActor(actor))
                        {
                            Point3d point3d = actor.pos.getAbsPoint();
                            double d2 = (point3d.x - (double)f1) * (point3d.x - (double)f1) + (point3d.y - (double)f2) * (point3d.y - (double)f2);
                            if(d2 <= d)
                                actor.setArmy(bornplace.army);
                        }
                    }

                }
                int k3 = sectfile.sectionIndex("BornPlace" + k);
                if(k3 >= 0)
                {
                    int i4 = sectfile.vars(k3);
                    for(int j4 = 0; j4 < i4; j4++)
                    {
                        String s = sectfile.line(k3, j4);
                        do
                        {
                            if(j4 + 1 >= i4)
                                break;
                            String s1 = sectfile.line(k3, j4 + 1);
                            if(!s1.startsWith("+ "))
                                break;
                            s = s + s1.substring(1);
                            j4++;
                        } while(true);
                        StringTokenizer stringtokenizer = new StringTokenizer(s);
                        ZutiAircraft zutiaircraft = new ZutiAircraft();
                        String s2 = "";
                        for(int k4 = 0; stringtokenizer.hasMoreTokens(); k4++)
                            switch(k4)
                            {
                            case 0: // '\0'
                                zutiaircraft.setAcName(stringtokenizer.nextToken());
                                break;

                            case 1: // '\001'
                                zutiaircraft.setMaxAllowed(Integer.valueOf(stringtokenizer.nextToken()).intValue());
                                break;

                            default:
                                s2 = s2 + " " + stringtokenizer.nextToken();
                                break;
                            }

                        zutiaircraft.setLoadedWeapons(s2, false);
                        String s3 = zutiaircraft.getAcName();
                        if(s3 != null)
                        {
                            s3 = s3.intern();
                            Class class1 = (Class)Property.value(s3, "airClass", null);
                            if(class1 != null)
                            {
                                if(bornplace.zutiAircrafts == null)
                                    bornplace.zutiAircrafts = new ArrayList();
                                bornplace.zutiAircrafts.add(zutiaircraft);
                            }
                        }
                    }

                }
                bornplace.zutiFillAirNames();
                zutiLoadBornPlaceCountries(bornplace, sectfile, k);
            }
        }

        try
        {
            zutiAssignBpToMovingCarrier();
        }
        catch(Exception exception)
        {
            System.out.println("Mission error, ID_15: " + exception.toString());
        }
    }

    private void loadTargets(SectFile sectfile)
    {
        if(Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isDogfight())
        {
            int i = sectfile.sectionIndex("Target");
            if(i < 0)
                return;
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
                Target.create(sectfile.line(i, k));

        }
    }

    private void loadViewPoint(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("StaticCamera");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        for(int k = 0; k < j; k++)
        {
            NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
            float f = numbertokenizer.next(0);
            float f1 = numbertokenizer.next(0);
            float f2 = numbertokenizer.next(100, 2, 10000);
            int l = numbertokenizer.next(0, 0, Army.amountNet() - 1);
            ActorViewPoint actorviewpoint = new ActorViewPoint();
            actorviewpoint.setArmy(l);
            Point3d point3d = new Point3d(f, f1, f2 + World.land().HQ_Air(f, f1));
            actorviewpoint.pos.setAbs(point3d);
            actorviewpoint.pos.reset();
            actorviewpoint.dreamFire(true);
            actorviewpoint.setName("StaticCamera_" + k);
            if(net.isMirror())
            {
                actorviewpoint.createNetObject(net.masterChannel(), ((Integer)actors.get(curActor)).intValue());
                actors.set(curActor++, actorviewpoint);
            } else
            {
                actorviewpoint.createNetObject(null, 0);
                actors.add(actorviewpoint);
            }
        }

    }

    private void checkBridgesAndHouses(SectFile sectfile)
    {
        int i = sectfile.sections();
        for(int j = 0; j < i; j++)
        {
            String s = sectfile.sectionName(j);
            if(s.endsWith("_Way"))
            {
                int l = sectfile.vars(j);
                for(int k1 = 0; k1 < l; k1++)
                {
                    String s1 = sectfile.var(j, k1);
                    if(s1.equals("GATTACK"))
                    {
                        SharedTokenizer.set(sectfile.value(j, k1));
                        SharedTokenizer.next((String)null);
                        SharedTokenizer.next((String)null);
                        SharedTokenizer.next((String)null);
                        SharedTokenizer.next((String)null);
                        String s2 = SharedTokenizer.next((String)null);
                        if(s2 != null && s2.startsWith("Bridge"))
                        {
                            LongBridge longbridge1 = (LongBridge)Actor.getByName(" " + s2);
                            if(longbridge1 != null && !longbridge1.isAlive())
                                longbridge1.BeLive();
                        }
                    }
                }

            } else
            if(s.endsWith("_Road"))
            {
                int i1 = sectfile.vars(j);
                for(int l1 = 0; l1 < i1; l1++)
                {
                    SharedTokenizer.set(sectfile.value(j, l1));
                    SharedTokenizer.next((String)null);
                    int j2 = (int)SharedTokenizer.next(1.0D);
                    if(j2 < 0)
                    {
                        j2 = -j2 - 1;
                        LongBridge longbridge = LongBridge.getByIdx(j2);
                        if(longbridge != null && !longbridge.isAlive())
                            longbridge.BeLive();
                    }
                }

            }
        }

        int k = sectfile.sectionIndex("Target");
        if(k < 0)
            return;
        int j1 = sectfile.vars(k);
        for(int i2 = 0; i2 < j1; i2++)
        {
            SharedTokenizer.set(sectfile.line(k, i2));
            int k2 = SharedTokenizer.next(0, 0, 7);
            if(k2 == 1 || k2 == 2 || k2 == 6 || k2 == 7)
            {
                SharedTokenizer.next(0);
                SharedTokenizer.next(0);
                SharedTokenizer.next(0);
                SharedTokenizer.next(0);
                int l2 = SharedTokenizer.next(0);
                int i3 = SharedTokenizer.next(0);
                int j3 = SharedTokenizer.next(1000, 50, 3000);
                int k3 = SharedTokenizer.next(0);
                String s3 = SharedTokenizer.next(null);
                if(s3 != null && s3.startsWith("Bridge"))
                    s3 = " " + s3;
                switch(k2)
                {
                case 3: // '\003'
                case 4: // '\004'
                case 5: // '\005'
                default:
                    break;

                case 1: // '\001'
                case 6: // '\006'
                    World.cur().statics.restoreAllHouses(l2, i3, j3);
                    break;

                case 2: // '\002'
                case 7: // '\007'
                    if(s3 == null)
                        break;
                    LongBridge longbridge2 = (LongBridge)Actor.getByName(s3);
                    if(longbridge2 != null && !longbridge2.isAlive())
                        longbridge2.BeLive();
                    break;
                }
            }
        }

    }

    public static void doMissionStarting()
    {
        ArrayList arraylist = new ArrayList(Engine.targets());
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)arraylist.get(j);
            if(Actor.isValid(actor))
                try
                {
                    actor.missionStarting();
                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                }
        }

    }

    public static void initRadioSounds()
    {
        if(radioStationsLoaded)
            return;
        Aircraft aircraft = World.getPlayerAircraft();
        if(aircraft != null)
        {
            ArrayList arraylist = Main.cur().mission.getBeacons(aircraft.FM.actor.getArmy());
            if(arraylist != null)
            {
                for(int i = 0; i < arraylist.size(); i++)
                {
                    Actor actor = (Actor)arraylist.get(i);
                    if(actor instanceof TypeHasRadioStation)
                    {
                        hasRadioStations = true;
                        aircraft.FM.AS.preLoadRadioStation(actor);
                        radioStationsLoaded = true;
                        CmdMusic.setCurrentVolume(0.001F);
                        return;
                    }
                }

            }
        }
    }

    public void doBegin()
    {
        if(bPlaying)
            return;
        if(Config.isUSE_RENDER())
        {
            Main3D.cur3D().setDrawLand(true);
            if(World.cur().diffCur.Clouds)
            {
                Main3D.cur3D().bDrawClouds = true;
                if(RenderContext.cfgSky.get() == 0)
                {
                    RenderContext.cfgSky.set(1);
                    RenderContext.cfgSky.apply();
                    RenderContext.cfgSky.reset();
                }
            } else
            {
                Main3D.cur3D().bDrawClouds = false;
            }
            CmdEnv.top().exec("fov 70");
            if(Main3D.cur3D().keyRecord != null)
            {
                Main3D.cur3D().keyRecord.clearPrevStates();
                Main3D.cur3D().keyRecord.clearRecorded();
                Main3D.cur3D().keyRecord.stopRecording(false);
                if(Main.cur().netServerParams.isSingle())
                    Main3D.cur3D().keyRecord.startRecording();
            }
            NetMissionTrack.countRecorded = 0;
            if(Main3D.cur3D().guiManager != null)
            {
                Main3D.cur3D().guiManager.setTimeGameActive(true);
                GUIPad.bStartMission = true;
            }
            if(!Main.cur().netServerParams.isCoop())
                doMissionStarting();
            if(Main.cur().netServerParams.isDogfight())
            {
                Time.setPause(false);
                RTSConf.cur.time.setEnableChangePause1(false);
            }
            CmdEnv.top().exec("music PUSH");
            CmdEnv.top().exec("music STOP");
            if(!Main3D.cur3D().isDemoPlaying())
                ForceFeedback.startMission();
            Joy.adapter().rePostMoves();
            viewSet = Main3D.cur3D().viewSet_Get();
            iconTypes = Main3D.cur3D().iconTypes();
        } else
        {
            doMissionStarting();
            Time.setPause(false);
        }
        if(net.isMaster())
        {
            sendCmd(10);
            doReplicateNotMissionActors(true);
        }
        if(Main.cur().netServerParams.isSingle())
        {
            Main.cur().netServerParams.setExtraOcclusion(false);
            AudioDevice.soundsOn();
        }
        if(Main.cur().netServerParams.isMaster() && (Main.cur().netServerParams.isCoop() || Main.cur().netServerParams.isSingle() || Main.cur().netServerParams.isDogfight()))
            World.cur().targetsGuard.activate();
        EventLog.type(true, "Mission: " + name() + " is Playing");
        EventLog.type("Mission BEGIN");
        if(Main.cur().netServerParams != null)
            Main.cur().netServerParams.zutiResetServerTime();
        bPlaying = true;
        if(Main.cur().netServerParams != null)
            Main.cur().netServerParams.USGSupdate();
    }

    public void doEnd()
    {
        try
        {
            if(!bPlaying)
                return;
            EventLog.type("Mission END");
            if(Config.isUSE_RENDER())
            {
                ForceFeedback.stopMission();
                if(Main3D.cur3D().guiManager != null)
                    Main3D.cur3D().guiManager.setTimeGameActive(false);
                NetMissionTrack.stopRecording();
                if(Main3D.cur3D().keyRecord != null)
                {
                    if(Main3D.cur3D().keyRecord.isPlaying())
                    {
                        Main3D.cur3D().keyRecord.stopPlay();
                        Main3D.cur3D().guiManager.setKeyboardGameActive(true);
                        Main3D.cur3D().guiManager.setMouseGameActive(true);
                        Main3D.cur3D().guiManager.setJoyGameActive(true);
                    }
                    Main3D.cur3D().keyRecord.stopRecording(true);
                }
                CmdEnv.top().exec("music POP");
                CmdEnv.top().exec("music PLAY");
            }
            RTSConf.cur.time.setEnableChangePause1(true);
            Time.setPause(true);
            if(net.isMaster())
                sendCmd(20);
            AudioDevice.soundsOff();
            Voice.endSession();
            bPlaying = false;
            if(Main.cur().netServerParams != null)
                Main.cur().netServerParams.USGSupdate();
        }
        catch(Exception exception)
        {
            System.out.println("Mission error, ID_16: " + exception.toString());
        }
    }

    public NetObj netObj()
    {
        return net;
    }

    private void sendCmd(int i)
    {
        if(net.isMirrored())
            try
            {
                List list = NetEnv.channels();
                int j = list.size();
                for(int k = 0; k < j; k++)
                {
                    NetChannel netchannel = (NetChannel)list.get(k);
                    if(netchannel != net.masterChannel() && netchannel.isReady() && netchannel.isMirrored(net) && (netchannel.userState == 4 || netchannel.userState == 0))
                    {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(i);
                        net.postTo(netchannel, netmsgguaranted);
                    }
                }

            }
            catch(Exception exception)
            {
                printDebug(exception);
            }
    }

    private void doReplicateNotMissionActors(boolean flag)
    {
        if(net.isMirrored())
        {
            List list = NetEnv.channels();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                NetChannel netchannel = (NetChannel)list.get(j);
                if(netchannel != net.masterChannel() && netchannel.isReady() && netchannel.isMirrored(net))
                    if(flag)
                    {
                        if(netchannel.userState == 4)
                            doReplicateNotMissionActors(netchannel, true);
                    } else
                    {
                        netchannel.userState = 1;
                    }
            }

        }
    }

    private void doReplicateNotMissionActors(NetChannel netchannel, boolean flag)
    {
        if(flag)
        {
            netchannel.userState = 0;
            HashMapInt hashmapint = NetEnv.cur().objects;
            for(HashMapIntEntry hashmapintentry = hashmapint.nextEntry(null); hashmapintentry != null; hashmapintentry = hashmapint.nextEntry(hashmapintentry))
            {
                NetObj netobj = (NetObj)hashmapintentry.getValue();
                if((netobj instanceof ActorNet) && !netchannel.isMirrored(netobj))
                {
                    ActorNet actornet = (ActorNet)netobj;
                    if(Actor.isValid(actornet.actor()) && !actornet.actor().isSpawnFromMission())
                        MsgNet.postRealNewChannel(netobj, netchannel);
                }
            }

        } else
        {
            netchannel.userState = 1;
        }
    }

    private void doResvMission(NetMsgInput netmsginput)
    {
        try
        {
            while(netmsginput.available() > 0) 
            {
                int i = netmsginput.readInt();
                if(i < 0)
                {
                    String s = netmsginput.read255();
                    sectFile.sectionAdd(s);
                } else
                {
                    sectFile.lineAdd(i, netmsginput.read255(), netmsginput.read255());
                }
            }
        }
        catch(Exception exception)
        {
            printDebug(exception);
            System.out.println("Bad format reseived missiion");
        }
    }

    private void doSendMission(NetChannel netchannel, int i)
    {
        try
        {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(i);
            int j = sectFile.sections();
            for(int k = 0; k < j; k++)
            {
                String s = sectFile.sectionName(k);
                if(!s.startsWith("$$$"))
                {
                    if(netmsgguaranted.size() >= 128)
                    {
                        net.postTo(netchannel, netmsgguaranted);
                        netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeByte(i);
                    }
                    netmsgguaranted.writeInt(-1);
                    netmsgguaranted.write255(s);
                    int l = sectFile.vars(k);
                    for(int i1 = 0; i1 < l; i1++)
                    {
                        String s1 = sectFile.var(k, i1);
                        String s2 = sectFile.value(k, i1);
                        if(netmsgguaranted.size() + s1.length() + s2.length() >= 250)
                        {
                            net.postTo(netchannel, netmsgguaranted);
                            netmsgguaranted = new NetMsgGuaranted();
                            netmsgguaranted.writeByte(i);
                        }
                        netmsgguaranted.writeInt(k);
                        netmsgguaranted.write255(s1);
                        netmsgguaranted.write255(s2);
                    }

                }
            }

            if(netmsgguaranted.size() > 1)
                net.postTo(netchannel, netmsgguaranted);
            netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(i + 1);
            net.postTo(netchannel, netmsgguaranted);
        }
        catch(Exception exception)
        {
            printDebug(exception);
        }
    }

    public void replicateTimeofDay()
    {
        if(!isServer())
            return;
        try
        {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(11);
            netmsgguaranted.writeFloat(World.getTimeofDay());
            net.post(netmsgguaranted);
        }
        catch(Exception exception) { }
    }

    private boolean isExistFile(String s)
    {
        boolean flag = false;
        try
        {
            SFSInputStream sfsinputstream = new SFSInputStream(s);
            sfsinputstream.close();
            flag = true;
        }
        catch(Exception exception) { }
        return flag;
    }

    private void netInput(NetMsgInput netmsginput)
        throws IOException
    {
        boolean flag = false;
        if((net instanceof Master) || netmsginput.channel() != net.masterChannel())
            flag = true;
        boolean flag1 = netmsginput.channel() instanceof NetChannelStream;
        NetMsgGuaranted netmsgguaranted = null;
        int i = netmsginput.readUnsignedByte();
        switch(i)
        {
        case 6: // '\006'
        case 7: // '\007'
        case 8: // '\b'
        case 9: // '\t'
        case 14: // '\016'
        case 15: // '\017'
        case 16: // '\020'
        case 17: // '\021'
        case 18: // '\022'
        case 19: // '\023'
        default:
            break;

        case 0: // '\0'
            netmsginput.channel().userState = 2;
            netmsgguaranted = new NetMsgGuaranted();
            if(flag)
            {
                if(flag1)
                {
                    NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted();
                    netmsgguaranted1.writeByte(13);
                    netmsgguaranted1.writeLong(Time.current());
                    net.postTo(netmsginput.channel(), netmsgguaranted1);
                }
                netmsgguaranted.writeByte(0);
                netmsgguaranted.write255(name);
                netmsgguaranted.writeLong(sectFinger);
                break;
            }
            name = netmsginput.read255();
            sectFinger = netmsginput.readLong();
            Main.cur().netMissionListener.netMissionState(0, 0.0F, name);
            if(!flag1)
                ((NetUser)NetEnv.host()).setMissProp("missions/" + name);
            String s = "missions/" + name;
            if(!flag1 && isExistFile(s))
            {
                sectFile = new SectFile(s, 0, false);
                if(sectFinger == sectFile.fingerExcludeSectPrefix("$$$"))
                {
                    netmsgguaranted.writeByte(3);
                    break;
                }
            }
            s = "missions/Net/Cache/" + sectFinger + ".mis";
            int ai[] = getSwTbl(s, sectFinger);
            sectFile = new SectFile(s, 0, false, ai);
            if(!flag1 && sectFinger == sectFile.fingerExcludeSectPrefix("$$$"))
            {
                netmsgguaranted.writeByte(3);
            } else
            {
                sectFile = new SectFile(s, 1, false, ai);
                sectFile.clear();
                netmsgguaranted.writeByte(1);
            }
            break;

        case 13: // '\r'
            if(!flag)
            {
                long l = netmsginput.readLong();
                RTSConf.cur.time.setCurrent(l);
                NetMissionTrack.playingStartTime = l;
            }
            break;

        case 1: // '\001'
            if(flag)
            {
                doSendMission(netmsginput.channel(), 1);
            } else
            {
                Main.cur().netMissionListener.netMissionState(1, 0.0F, null);
                doResvMission(netmsginput);
            }
            break;

        case 2: // '\002'
            if(!flag)
            {
                sectFile.saveFile();
                netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(3);
            }
            break;

        case 3: // '\003'
            if(flag)
            {
                int j = actors.size();
                for(int i1 = 0; i1 < j;)
                {
                    netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(3);
                    for(int j1 = 64; j1-- > 0 && i1 < j;)
                    {
                        Actor actor1 = (Actor)actors.get(i1++);
                        if(Actor.isValid(actor1))
                            netmsgguaranted.writeShort(actor1.net.idLocal());
                        else
                            netmsgguaranted.writeShort(0);
                    }

                    net.postTo(netmsginput.channel(), netmsgguaranted);
                }

                netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(4);
                netmsginput.channel().userState = 3;
            } else
            {
                Main.cur().netMissionListener.netMissionState(2, 0.0F, null);
                for(; netmsginput.available() > 0; actors.add(new Integer(netmsginput.readUnsignedShort())));
            }
            break;

        case 4: // '\004'
            if(flag)
            {
                if(isDogfight() || (netmsginput.channel() instanceof NetChannelOutStream))
                {
                    World.cur().statics.netBridgeSync(netmsginput.channel());
                    World.cur().statics.netHouseSync(netmsginput.channel());
                }
                for(int k = 0; k < actors.size(); k++)
                {
                    Actor actor = (Actor)actors.get(k);
                    if(Actor.isValid(actor))
                        try
                        {
                            NetChannel netchannel = netmsginput.channel();
                            netchannel.setMirrored(actor.net);
                            actor.netFirstUpdate(netmsginput.channel());
                        }
                        catch(Exception exception1)
                        {
                            printDebug(exception1);
                        }
                }

                if(Actor.isValid(World.cur().houseManager))
                    World.cur().houseManager.fullUpdateChannel(netmsginput.channel());
                netmsgguaranted = new NetMsgGuaranted();
                if(isPlaying())
                {
                    netmsgguaranted.writeByte(10);
                    net.postTo(netmsginput.channel(), netmsgguaranted);
                    netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(11);
                    netmsgguaranted.writeFloat(World.getTimeofDay());
                    net.postTo(netmsginput.channel(), netmsgguaranted);
                    netmsgguaranted = null;
                    doReplicateNotMissionActors(netmsginput.channel(), true);
                    trySendMsgStart(netmsginput.channel());
                } else
                {
                    netmsgguaranted.writeByte(5);
                    netmsginput.channel().userState = 4;
                }
            } else
            {
                netmsginput.channel().userState = 3;
                try
                {
                    load(name, sectFile, true);
                }
                catch(Exception exception)
                {
                    printDebug(exception);
                    Main.cur().netMissionListener.netMissionState(4, 0.0F, exception.getMessage());
                }
            }
            break;

        case 5: // '\005'
            if(!flag);
            break;

        case 10: // '\n'
            if(!(net instanceof Master) && netmsginput.channel() == net.masterChannel())
            {
                if(net.isMirrored())
                {
                    netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(10);
                    net.post(netmsgguaranted);
                    netmsgguaranted = null;
                }
                doReplicateNotMissionActors(true);
                doReplicateNotMissionActors(netmsginput.channel(), true);
                doBegin();
                Main.cur().netMissionListener.netMissionState(6, 0.0F, null);
            }
            break;

        case 11: // '\013'
            if(!(net instanceof Master) && netmsginput.channel() == net.masterChannel())
            {
                float f = netmsginput.readFloat();
                World.setTimeofDay(f);
                World.land().cubeFullUpdate();
            }
            break;

        case 20: // '\024'
            if(!(net instanceof Master) && netmsginput.channel() == net.masterChannel())
            {
                Main.cur().netMissionListener.netMissionState(7, 0.0F, null);
                doReplicateNotMissionActors(false);
                doReplicateNotMissionActors(netmsginput.channel(), false);
                doEnd();
                if(net.isMirrored())
                {
                    netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(20);
                    net.post(netmsgguaranted);
                    netmsgguaranted = null;
                }
            }
            break;

        case 12: // '\f'
            if(!(net instanceof Master) && netmsginput.channel() == net.masterChannel())
                Main.cur().netMissionListener.netMissionState(9, 0.0F, null);
            break;
        }
        if(netmsgguaranted != null && netmsgguaranted.size() > 0)
            net.postTo(netmsginput.channel(), netmsgguaranted);
    }

    public void trySendMsgStart(Object obj)
    {
        if(isDestroyed())
            return;
        NetChannel netchannel = (NetChannel)obj;
        if(netchannel.isDestroyed())
            return;
        HashMapInt hashmapint = RTSConf.cur.netEnv.objects;
        for(HashMapIntEntry hashmapintentry = null; (hashmapintentry = hashmapint.nextEntry(hashmapintentry)) != null;)
        {
            NetObj netobj = (NetObj)hashmapintentry.getValue();
            if(netobj != null && !netobj.isDestroyed() && !netobj.isCommon() && !netchannel.isMirrored(netobj) && netobj.masterChannel() != netchannel && (!(netchannel instanceof NetChannelOutStream) || !(netobj instanceof NetControl) && (!(netobj instanceof NetUser) || !netobj.isMaster() || !NetMissionTrack.isPlaying())) && (!(netobj instanceof GameTrack) || !netobj.isMirror()))
            {
                Object obj1 = netobj.superObj();
                if(!(obj1 instanceof Destroy) || !((Destroy)obj1).isDestroyed())
                {
                    (new MsgInvokeMethod_Object("trySendMsgStart", netchannel)).post(72, this, 0.0D);
                    return;
                }
            }
        }

        try
        {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(12);
            net.postTo(netchannel, netmsgguaranted);
        }
        catch(Exception exception)
        {
            printDebug(exception);
        }
    }

    private void createNetObject(NetChannel netchannel, int i)
    {
        setTime(true);
        if(netchannel == null)
        {
            net = new Master(this);
            doReplicateNotMissionActors(false);
        } else
        {
            net = new Mirror(this, netchannel, i);
            doReplicateNotMissionActors(netchannel, false);
        }
    }

    protected static void printDebug(Exception exception)
    {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
    }

    private int[] getSwTbl(String s, long l)
    {
        int i = (int)l;
        int j = Finger.Int(s);
        if(i < 0)
            i = -i;
        if(j < 0)
            j = -j;
        int k = (j + i / 7) % 16 + 15;
        int i1 = (j + i / 21) % Finger.kTable.length;
        if(k < 0)
            k = -k % 16;
        if(k < 10)
            k = 10;
        if(i1 < 0)
            i1 = -i1 % Finger.kTable.length;
        int ai[] = new int[k];
        for(int j1 = 0; j1 < k; j1++)
            ai[j1] = Finger.kTable[(i1 + j1) % Finger.kTable.length];

        return ai;
    }

    public ArrayList getAllActors()
    {
        return actors;
    }

    private String generateHayrakeCode(Point3d point3d)
    {
        double d = point3d.x;
        double d1 = point3d.y;
        long l = (long)(d + d1);
        Random random = new Random(l);
        byte abyte0[] = new byte[12];
        for(int i = 0; i < abyte0.length; i++)
        {
            for(boolean flag = false; !flag;)
            {
                byte byte0 = (byte)(random.nextInt(26) + 65);
                if(byte0 != 74 && byte0 != 81 && byte0 != 89 && byte0 <= 90)
                {
                    for(int j = 0; j < abyte0.length; j++)
                    {
                        if(byte0 == abyte0[j])
                            break;
                        if(j == abyte0.length - 1)
                        {
                            flag = true;
                            abyte0[i] = byte0;
                        }
                    }

                }
            }

        }

        String s = new String(abyte0);
        return s;
    }

    private void populateRunwayLights()
    {
        ArrayList arraylist = new ArrayList();
        World.getAirports(arraylist);
        for(int i = 0; i < arraylist.size(); i++)
            if(arraylist.get(i) instanceof AirportGround)
            {
                for(int j = 0; j < actors.size(); j++)
                    if(actors.get(j) instanceof TypeRunwayLight)
                    {
                        AirportGround airportground = (AirportGround)arraylist.get(i);
                        Actor actor = (Actor)actors.get(j);
                        double d = airportground.pos.getAbsPoint().x - actor.pos.getAbsPoint().x;
                        double d1 = airportground.pos.getAbsPoint().y - actor.pos.getAbsPoint().y;
                        if(Math.abs(d) < 2000D && Math.abs(d1) < 2000D && (actor.getArmy() == 1 || actor.getArmy() == 2))
                        {
                            SmokeGeneric smokegeneric = (SmokeGeneric)actor;
                            smokegeneric.setVisible(false);
                            airportground.addLights(smokegeneric);
                        }
                    }

            }

        for(int k = 0; k < actors.size(); k++)
            if(actors.get(k) instanceof SmokeGeneric)
                ((SmokeGeneric)actors.get(k)).setArmy(0);

    }

    private void populateBeacons()
    {
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        for(int i = 0; i < actors.size(); i++)
            if(actors.get(i) instanceof TypeHasBeacon)
            {
                Point3d point3d = ((Actor)actors.get(i)).pos.getAbsPoint();
                arraylist.add(((Object) (new Object[] {
                    actors.get(i), point3d
                })));
                if(actors.get(i) instanceof TypeHasLorenzBlindLanding)
                    ((Actor)actors.get(i)).missionStarting();
                if(actors.get(i) instanceof BigshipGeneric)
                    hayrakeMap.put((Actor)actors.get(i), "NDB");
            } else
            if(actors.get(i) instanceof TypeHasMeacon)
            {
                Point3d point3d1 = ((Actor)actors.get(i)).pos.getAbsPoint();
                arraylist1.add(((Object) (new Object[] {
                    actors.get(i), point3d1
                })));
            } else
            if(actors.get(i) instanceof TypeHasHayRake)
            {
                Point3d point3d2 = ((Actor)actors.get(i)).pos.getAbsPoint();
                String s = generateHayrakeCode(point3d2);
                arraylist.add(((Object) (new Object[] {
                    actors.get(i), point3d2
                })));
                hayrakeMap.put((Actor)actors.get(i), s);
            }

        if(arraylist.size() == 0)
            return;
        sortBeaconsList(arraylist);
        for(int j = 0; j < arraylist.size(); j++)
        {
            Object aobj[] = (Object[])arraylist.get(j);
            Actor actor = (Actor)aobj[0];
            if(((actor instanceof TypeHasRadioStation) || actor.getArmy() == 1) && beaconsRed.size() < 32)
                beaconsRed.add(aobj[0]);
            if(((actor instanceof TypeHasRadioStation) || actor.getArmy() == 2) && beaconsBlue.size() < 32)
                beaconsBlue.add(aobj[0]);
        }

        for(int k = 0; k < arraylist1.size(); k++)
        {
            Object aobj1[] = (Object[])arraylist1.get(k);
            Actor actor1 = (Actor)aobj1[0];
            if(actor1.getArmy() == 1 && meaconsRed.size() < 32)
                meaconsRed.add(aobj1[0]);
            else
            if(actor1.getArmy() == 2 && meaconsBlue.size() < 32)
                meaconsBlue.add(aobj1[0]);
        }

        arraylist.clear();
        arraylist = null;
        arraylist1.clear();
        arraylist1 = null;
    }

    public static void addHayrakesToOrdersTree()
    {
        for(int i = 0; i < 10; i++)
            Main3D.cur3D().ordersTree.addShipIDs(i, -1, null, "", "");

        if(!World.cur().diffCur.RealisticNavigationInstruments)
            return;
        int j = 0;
        Iterator iterator = hayrakeMap.entrySet().iterator();
        while(iterator.hasNext() && j < 10) 
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            Actor actor = (Actor)entry.getKey();
            if(actor.getArmy() != World.getPlayerArmy())
                continue;
            String s = (String)entry.getValue();
            String s1 = Property.stringValue(actor.getClass(), "i18nName", "");
            if(s1.equals(""))
                try
                {
                    String s2 = actor.getClass().toString().substring(actor.getClass().toString().indexOf("$") + 1);
                    s1 = I18N.technic(s2);
                }
                catch(Exception exception) { }
            int k = -1;
            if(beaconsRed.contains(actor))
                k = beaconsRed.indexOf(actor);
            else
            if(beaconsBlue.contains(actor))
                k = beaconsBlue.indexOf(actor);
            if(s.equals("NDB"))
            {
                Main3D.cur3D().ordersTree.addShipIDs(j, k, actor, s1, "");
            } else
            {
                boolean flag = Aircraft.hasPlaneZBReceiver(World.getPlayerAircraft());
                if(!flag)
                    continue;
                String s3 = s;
                if(s.length() == 12)
                    s3 = s.substring(0, 3) + " / " + s.substring(3, 6) + " / " + s.substring(6, 9) + " / " + s.substring(9, 12);
                else
                if(s.length() == 24)
                    s3 = s.substring(0, 2) + "-" + s.substring(2, 4) + "-" + s.substring(4, 6) + " / " + s.substring(6, 8) + "-" + s.substring(8, 10) + "-" + s.substring(10, 12) + " / " + s.substring(12, 14) + "-" + s.substring(14, 16) + "-" + s.substring(16, 18) + " / " + s.substring(18, 20) + "-" + s.substring(20, 22) + "-" + s.substring(22, 24);
                Main3D.cur3D().ordersTree.addShipIDs(j, k, actor, s1, "( " + s3 + " )");
            }
            j++;
        }
    }

    private void sortBeaconsList(List list)
    {
        boolean flag = false;
        do
        {
            for(int i = 0; i < list.size() - 1; i++)
            {
                flag = false;
                Object aobj[] = (Object[])list.get(i);
                Object aobj1[] = (Object[])list.get(i + 1);
                if((aobj[0] instanceof TypeHasHayRake) && !(aobj1[0] instanceof TypeHasHayRake) || (aobj[0] instanceof BigshipGeneric) && !(aobj1[0] instanceof BigshipGeneric))
                {
                    Object aobj2[] = aobj;
                    list.set(i, ((Object) (aobj1)));
                    list.set(i + 1, ((Object) (aobj2)));
                    flag = true;
                }
            }

        } while(flag);
    }

    public boolean hasBeacons(int i)
    {
        if(i == 1)
            return beaconsRed.size() > 0;
        if(i == 2)
            return beaconsBlue.size() > 0;
        else
            return false;
    }

    public ArrayList getBeacons(int i)
    {
        if(i == 1)
            return beaconsRed;
        if(i == 2)
            return beaconsBlue;
        else
            return null;
    }

    public ArrayList getMeacons(int i)
    {
        if(i == 1)
            return meaconsBlue;
        if(i == 2)
            return meaconsRed;
        else
            return null;
    }

    public String getHayrakeCodeOfCarrier(Actor actor)
    {
        if(hayrakeMap.containsKey(actor))
            return (String)hayrakeMap.get(actor);
        else
            return null;
    }

    private void zutiAssignBpToMovingCarrier()
    {
        for(int i = 0; i < actors.size(); i++)
        {
            Actor actor = (Actor)Main.cur().mission.actors.get(i);
            if(actor instanceof BigshipGeneric)
            {
                BigshipGeneric bigshipgeneric = (BigshipGeneric)actor;
                if(bigshipgeneric.getAirport() != null)
                {
                    boolean flag = true;
                    if(actor.name().indexOf("_Chief") > -1)
                        flag = false;
                    bigshipgeneric.zutiAssignBornPlace(flag);
                }
            }
        }

    }

    private void zutiResetMissionVariables()
    {
        if(ZutiSupportMethods.ZUTI_BANNED_PILOTS == null)
            ZutiSupportMethods.ZUTI_BANNED_PILOTS = new ArrayList();
        ZutiSupportMethods.ZUTI_BANNED_PILOTS.clear();
        if(ZutiSupportMethods.ZUTI_DEAD_TARGETS == null)
            ZutiSupportMethods.ZUTI_DEAD_TARGETS = new ArrayList();
        ZutiSupportMethods.ZUTI_DEAD_TARGETS.clear();
        if(GUI.pad != null)
            GUI.pad.zutiColorAirfields = true;
        ZutiSupportMethods.ZUTI_KIA_COUNTER = 0;
        ZutiSupportMethods.ZUTI_KIA_DELAY_CLEARED = false;
        zutiRadar_PlayerSideHasRadars = false;
        zutiRadar_ShipsAsRadar = false;
        zutiRadar_ShipRadar_MaxRange = 100;
        zutiRadar_ShipRadar_MinHeight = 100;
        zutiRadar_ShipRadar_MaxHeight = 5000;
        zutiRadar_ShipSmallRadar_MaxRange = 25;
        zutiRadar_ShipSmallRadar_MinHeight = 0;
        zutiRadar_ShipSmallRadar_MaxHeight = 2000;
        zutiRadar_ScoutsAsRadar = false;
        zutiRadar_ScoutRadar_MaxRange = 2;
        zutiRadar_ScoutRadar_DeltaHeight = 1500;
        zutiRadar_RefreshInterval = 0;
        zutiRadar_EnableTowerCommunications = true;
        zutiRadar_HideUnpopulatedAirstripsFromMinimap = false;
        ZUTI_RADAR_IN_ADV_MODE = false;
        zutiRadar_ScoutGroundObjects_Alpha = 5;
        ScoutsRed = new ArrayList();
        ScoutsBlue = new ArrayList();
        zutiRadar_ScoutCompleteRecon = false;
        zutiRadar_EnableBigShip_Radar = true;
        zutiRadar_EnableSmallShip_Radar = true;
        zutiMisc_DisableAIRadioChatter = false;
        zutiMisc_DespawnAIPlanesAfterLanding = true;
        zutiMisc_HidePlayersCountOnHomeBase = false;
        zutiMisc_EnableReflyOnlyIfBailedOrDied = false;
        zutiMisc_DisableReflyForMissionDuration = false;
        zutiMisc_ReflyKIADelay = 0;
        zutiMisc_MaxAllowedKIA = 0x7fffffff;
        zutiMisc_ReflyKIADelayMultiplier = 0.0F;
        if(Main.cur().netServerParams.reflyKIADelay > 0)
        {
            zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
            zutiMisc_ReflyKIADelay = Main.cur().netServerParams.reflyKIADelay;
        }
        if(Main.cur().netServerParams.reflyDisabled)
            zutiMisc_DisableReflyForMissionDuration = true;
        else
        if(Main.cur().netServerParams.maxAllowedKIA >= 0)
        {
            zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
            zutiMisc_MaxAllowedKIA = Main.cur().netServerParams.maxAllowedKIA;
        }
        if(Main.cur().netServerParams.reflyKIADelayMultiplier > 0.0F && zutiMisc_ReflyKIADelay != 0)
        {
            zutiMisc_EnableReflyOnlyIfBailedOrDied = true;
            zutiMisc_ReflyKIADelayMultiplier = Main.cur().netServerParams.reflyKIADelayMultiplier;
        }
        zutiMisc_BombsCat1_CratersVisibilityMultiplier = 1.0F;
        zutiMisc_BombsCat2_CratersVisibilityMultiplier = 1.0F;
        zutiMisc_BombsCat3_CratersVisibilityMultiplier = 1.0F;
    }

    private void zutiLoadBornPlaceCountries(BornPlace bornplace, SectFile sectfile, int i)
    {
        if(bornplace == null)
            return;
        if(bornplace != null && bornplace.zutiHomeBaseCountries == null)
            bornplace.zutiHomeBaseCountries = new ArrayList();
        bornplace.zutiLoadAllCountries();
        int j = sectfile.sectionIndex("BornPlaceCountries" + i);
        if(j >= 0)
        {
            bornplace.zutiHomeBaseCountries.clear();
            ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
            int k = sectfile.vars(j);
            for(int l = 0; l < k; l++)
                try
                {
                    String s = sectfile.var(j, l);
                    String s1 = resourcebundle.getString(s);
                    if(!bornplace.zutiHomeBaseCountries.contains(s1))
                        bornplace.zutiHomeBaseCountries.add(s1);
                }
                catch(Exception exception) { }

        }
    }

    private void zutiLoadScouts_Red(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("MDS_Scouts_Red");
        if(i > -1)
        {
            if(Main.cur().mission.ScoutsRed == null)
                Main.cur().mission.ScoutsRed = new ArrayList();
            Main.cur().mission.ScoutsRed.clear();
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                String s = sectfile.line(i, k);
                StringTokenizer stringtokenizer = new StringTokenizer(s);
                String s1;
                for(s1 = null; stringtokenizer.hasMoreTokens();)
                {
                    s1 = stringtokenizer.nextToken();
                    break;
                }

                if(s1 != null)
                {
                    s1 = s1.intern();
                    Class class1 = (Class)Property.value(s1, "airClass", null);
                    Main.cur().mission.ScoutsRed.add(s1);
                }
            }

        }
    }

    private void zutiLoadScouts_Blue(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("MDS_Scouts_Blue");
        if(i > -1)
        {
            if(Main.cur().mission.ScoutsBlue == null)
                Main.cur().mission.ScoutsBlue = new ArrayList();
            Main.cur().mission.ScoutsBlue.clear();
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                String s = sectfile.line(i, k);
                StringTokenizer stringtokenizer = new StringTokenizer(s);
                String s1;
                for(s1 = null; stringtokenizer.hasMoreTokens();)
                {
                    s1 = stringtokenizer.nextToken();
                    break;
                }

                if(s1 != null)
                {
                    s1 = s1.intern();
                    Class class1 = (Class)Property.value(s1, "airClass", null);
                    Main.cur().mission.ScoutsBlue.add(s1);
                }
            }

        }
    }

    private void zutiSetShipRadars()
    {
        if(zutiRadar_ShipRadar_MaxHeight == 0 && zutiRadar_ShipRadar_MaxRange == 0 && zutiRadar_ShipRadar_MinHeight == 0)
            zutiRadar_EnableBigShip_Radar = false;
        if(zutiRadar_ShipSmallRadar_MaxHeight == 0 && zutiRadar_ShipSmallRadar_MaxRange == 0 && zutiRadar_ShipSmallRadar_MinHeight == 0)
            zutiRadar_EnableSmallShip_Radar = false;
    }

    public static int getMissionDate(boolean flag)
    {
        int i = curYear();
        int k = curMonth();
        int i1 = curDay();
        int k1 = i * 10000 + k * 100 + i1;
        if(Main.cur().mission == null)
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            if(sectfile == null)
                return k1;
            String s = sectfile.get("MAIN", "MAP");
            int l1 = World.land().config.getDefaultMonth("maps/" + s);
            int j = sectfile.get("SEASON", "Year", 1940, 1900, Calendar.getInstance().get(Calendar.YEAR));
            int l = sectfile.get("SEASON", "Month", l1, 1, 12);
            int j1 = sectfile.get("SEASON", "Day", 15, 1, 31);
            k1 = j * 10000 + l * 100 + j1;
            int j2 = 0x1280540 + l1 * 100 + 15;
            if(flag && k1 == j2)
                k1 = 0;
        } else
        if(flag)
        {
            SectFile sectfile1 = Main.cur().currentMissionFile;
            if(sectfile1 == null)
                return 0;
            String s1 = sectfile1.get("MAIN", "MAP");
            int i2 = World.land().config.getDefaultMonth("maps/" + s1);
            int k2 = 0x1280540 + i2 * 100 + 15;
            if(k1 == k2)
                k1 = 0;
        }
        return k1;
    }

    public static float BigShipHpDiv()
    {
        if(Main.cur().mission == null)
            return 1.0F;
        else
            return Main.cur().mission.bigShipHpDiv;
    }

    private void printWithTime(String s)
    {
        if(shortDate == null)
            shortDate = DateFormat.getTimeInstance(2, Locale.GERMANY);
        Calendar calendar = Calendar.getInstance();
        System.out.print(shortDate.format(calendar.getTime()) + " ");
        System.out.println(s);
    }

    public static final boolean __DEBUG_TMBUG__ = false;
    public static final String DIR = "missions/";
    public static final String DIRNET = "missions/Net/Cache/";
    public static final float CLOUD_HEIGHT = 8000F;
    private String name;
    private SectFile sectFile;
    private long sectFinger;
    private ArrayList actors;
    private int curActor;
    private boolean bPlaying;
    private int curCloudsType;
    private float curCloudsHeight;
    protected static int viewSet = 0;
    protected static int iconTypes = 0;
    private static HashMap respawnMap = new HashMap();
    private static int curYear = 0;
    private static int curMonth = 0;
    private static int curDay = 0;
    private float curWindDirection;
    private float curWindVelocity;
    private float curGust;
    private float curTurbulence;
    private static ArrayList beaconsRed = new ArrayList();
    private static ArrayList beaconsBlue = new ArrayList();
    private static ArrayList meaconsRed = new ArrayList();
    private static ArrayList meaconsBlue = new ArrayList();
    private static Map hayrakeMap = new HashMap();
    private final int HAYRAKE_CODE_LENGTH = 12;
    public static boolean hasRadioStations = false;
    private static boolean radioStationsLoaded = false;
    private float bigShipHpDiv;
    private String player;
    private boolean _loadPlayer;
    private int playerNum;
    private HashMap mapWingTakeoff;
    private static SectFile chiefsIni;
    private static Point3d Loc = new Point3d();
    private static Orient Or = new Orient();
    private static Vector3f Spd = new Vector3f();
    private static Vector3d Spdd = new Vector3d();
    private static ActorSpawnArg spawnArg = new ActorSpawnArg();
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    public static final int NET_MSG_ID_NAME = 0;
    public static final int NET_MSG_ID_BODY = 1;
    public static final int NET_MSG_ID_BODY_END = 2;
    public static final int NET_MSG_ID_ACTORS = 3;
    public static final int NET_MSG_ID_ACTORS_END = 4;
    public static final int NET_MSG_ID_LOADED = 5;
    public static final int NET_MSG_ID_BEGIN = 10;
    public static final int NET_MSG_ID_TOD = 11;
    public static final int NET_MSG_ID_START = 12;
    public static final int NET_MSG_ID_TIME = 13;
    public static final int NET_MSG_ID_END = 20;
    protected NetObj net;
    public boolean zutiRadar_PlayerSideHasRadars;
    public int zutiRadar_RefreshInterval;
    public boolean zutiRadar_HideUnpopulatedAirstripsFromMinimap;
    public boolean zutiRadar_EnableTowerCommunications;
    public boolean zutiRadar_ShipsAsRadar;
    public int zutiRadar_ShipRadar_MaxRange;
    public int zutiRadar_ShipRadar_MinHeight;
    public int zutiRadar_ShipRadar_MaxHeight;
    public int zutiRadar_ShipSmallRadar_MaxRange;
    public int zutiRadar_ShipSmallRadar_MinHeight;
    public int zutiRadar_ShipSmallRadar_MaxHeight;
    public boolean zutiRadar_ScoutsAsRadar;
    public int zutiRadar_ScoutRadar_MaxRange;
    public int zutiRadar_ScoutRadar_DeltaHeight;
    public ArrayList ScoutsRed;
    public ArrayList ScoutsBlue;
    public int zutiRadar_ScoutGroundObjects_Alpha;
    public boolean zutiRadar_EnableBigShip_Radar;
    public boolean zutiRadar_EnableSmallShip_Radar;
    public boolean zutiRadar_ScoutCompleteRecon;
    public boolean zutiRadar_DisableVectoring;
    public static boolean ZUTI_RADAR_IN_ADV_MODE = false;
    public boolean zutiMisc_DisableAIRadioChatter;
    public boolean zutiMisc_DespawnAIPlanesAfterLanding;
    public boolean zutiMisc_HidePlayersCountOnHomeBase;
    public boolean zutiMisc_EnableReflyOnlyIfBailedOrDied;
    public boolean zutiMisc_DisableReflyForMissionDuration;
    public int zutiMisc_ReflyKIADelay;
    public int zutiMisc_MaxAllowedKIA;
    public float zutiMisc_ReflyKIADelayMultiplier;
    public float zutiMisc_BombsCat1_CratersVisibilityMultiplier;
    public float zutiMisc_BombsCat2_CratersVisibilityMultiplier;
    public float zutiMisc_BombsCat3_CratersVisibilityMultiplier;
    private static DateFormat shortDate = null;

    static 
    {
        Spawn.add(com.maddox.il2.game.Mission.class, new SPAWN());
    }







}
