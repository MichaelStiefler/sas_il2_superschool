package com.maddox.il2.game;

import java.util.Map;
import com.maddox.rts.BackgroundLoop;
import com.maddox.sound.BaseObject;
import java.lang.Integer;
import java.lang.Exception;
import java.lang.Thread;
import com.maddox.rts.BackgroundTask;
import java.lang.System;
import java.lang.Runtime;
import com.maddox.il2.ai.World;
import java.util.List;
import com.maddox.rts.Cmd;
import com.maddox.il2.net.Chat;
import com.maddox.il2.net.GameSpy;
import com.maddox.rts.NetSocket;
import java.lang.Object;
import com.maddox.il2.net.NetLocalControl;
import com.maddox.il2.net.NetUSGSControl;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.net.USGS;
import com.maddox.rts.Time;
import com.maddox.il2.ai.Regiment;
import com.maddox.rts.CmdEnv;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.Connect;
import com.maddox.rts.NetEnv;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Config;
import com.maddox.rts.RTSConf;
import com.maddox.rts.IniFile;
import java.lang.String;
import com.maddox.il2.game.Main;

public class DServer extends Main
{
    private static String cmdFile;
    private static int localPort;
    private static String[] args;
    public int myInt;
    
    public DServer() {
        super();
        this.myInt = 0;
    }
    
    public boolean beginApp(final String paramString1, final String paramString2, final int paramInt) {
        final IniFile localIniFile = new IniFile(paramString1);
        RTSConf.cur = new RTSConf(localIniFile, "rts", paramInt);
        Config.cur = new Config(localIniFile, false);
        Config.cur.mainSection = paramString2;
        Engine.cur = new Engine();
        new Background();
        RTSConf.cur.start();
        PaintScheme.init();
        NetEnv.cur().connect = new Connect();
        NetEnv.cur();
        NetEnv.host().destroy();
        new NetUser("Server");
        NetEnv.active(true);
        Config.cur.beginSound();
        CmdEnv.top().exec("CmdLoad com.maddox.rts.cmd.CmdLoad");
        CmdEnv.top().exec("load com.maddox.rts.cmd.CmdFile PARAM CURENV on");
        CmdEnv.top().exec("file .rcs");
        Regiment.loadAll();
        this.preloadNetClasses();
        this.preloadAirClasses();
        this.preloadChiefClasses();
        this.preloadStationaryClasses();
        Time.setPause(false);
        RTSConf.cur.loopMsgs();
        Time.setPause(true);
        CmdEnv.top().exec("socket LISTENER 1");
        if (USGS.isUsed()) {
            Config.cur.netServerChannels = USGS.maxclients;
        }
        if (DServer.localPort == 0) {
            DServer.localPort = Config.cur.netLocalPort;
        }
        String str = "socket udp CREATE LOCALPORT " + DServer.localPort + " SPEED " + Config.cur.netSpeed + " CHANNELS " + Config.cur.netServerChannels;
        if (Config.cur.netLocalHost != null && Config.cur.netLocalHost.length() > 0) {
            str = str + " LOCALHOST " + Config.cur.netLocalHost;
        }
        CmdEnv.top().exec(str);
        new NetServerParams();
        if (USGS.isUsed()) {
            new NetUSGSControl();
        }
        else {
            new NetLocalControl();
        }
        Main.cur().netServerParams.setMode(0);
        if (USGS.isUsed()) {
            Main.cur().netServerParams.setServerName(USGS.room);
        }
        else {
            Main.cur().netServerParams.setServerName(Config.cur.netServerName);
        }
        if ("".equals(Main.cur().netServerParams.serverName())) {
            Main.cur().netServerParams.setServerName("Server");
        }
        Main.cur().netServerParams.serverDescription = Config.cur.netServerDescription;
        int i = Config.cur.ini.get("NET", "difficulty", 193791);
        if (Config.cur.newCloudsRender) {
            i |= 0x1000000;
        }
        else {
            i &= 0xFEFFFFFF;
        }
        Main.cur().netServerParams.setDifficulty(i);
        Main.cur().netServerParams.setMaxUsers(Config.cur.netServerChannels);
        if (USGS.isUsed()) {
            Main.cur().netServerParams.setType(48);
        }
        if (super.netGameSpy != null) {
            Main.cur().netServerParams.setType(32);
            final List localList = NetEnv.socketsBlock();
            if (localList != null && localList.size() > 0) {
                super.netGameSpy.set(null, (NetSocket)localList.get(0), Config.cur.netLocalPort);
            }
        }
        else {
            super.netGameSpyListener = new GameSpy();
            if (USGS.isUsed()) {
                super.netGameSpyListener.setListenerOnly(USGS.room);
            }
            else {
                super.netGameSpyListener.setListenerOnly(null);
            }
        }
        new Chat();
        CmdEnv.top().setCommand(new CmdExit(), "exit", "exit game");
        this.onBeginApp();
        RTSConf.cur.console.getEnv().exec("file rcu");
        RTSConf.cur.console.getEnv().exec("file " + DServer.cmdFile);
        new ConsoleIO().start();
        this.createConsoleServer();
        return true;
    }
    
    public void endApp() {
        USGS.stopDedicated();
        if (RTSConf.cur != null) {
            RTSConf.cur.stop();
        }
        Config.cur.ini.setValue("NET", "difficulty", "" + World.cur().diffCur.get());
        Config.cur.save();
    }
    
    public void loopApp() {
        long l1 = Time.real();
        final IniFile inifile = new IniFile("il2server.ini", 0);
        int iMaxRam = inifile.get("Settings", "RamSize", 512);
        iMaxRam *= (int)0.9;
        final int iMaxRamGC = (int)(iMaxRam * 0.8);
        while (!RTSConf.isRequestExitApp()) {
            synchronized (RTSConf.lockObject()) {
                final Runtime runtime = Runtime.getRuntime();
                final long allocatedMemory = runtime.totalMemory();
//                final long freeMemory = runtime.freeMemory();
                if (allocatedMemory > 1048576 * iMaxRam) {
                    System.out.println("Starting manual Garbage collection ...");
                    System.gc();
                    final long allocatedMemoryGC = runtime.totalMemory();
                    System.out.println("Manual Garbage collection freed " + (allocatedMemory - allocatedMemoryGC) / 1024L + " KB RAM.");
                    if (allocatedMemory > 1048576 * iMaxRamGC) {
                        Main.iExitCode = -1;
                        RTSConf.setRequestExitApp(true);
                    }
                }
                if (BackgroundTask.isExecuted()) {
                    BackgroundTask.doRun();
                }
                else {
                    final boolean bool1;
                    final boolean bool2;
                    final String str;
                    synchronized (super.oCommandSync) {
                        bool1 = super.bCommand;
                        bool2 = super.bCommandNet;
                        str = super.sCommand;
                        super.bCommand = false;
                    }
                    if (bool1) {
                        if (bool2) {
                            if (super.consoleServer != null) {
                                super.consoleServer.bEnableType = false;
                            }
                            System.out.println(str);
                            if (super.consoleServer != null) {
                                super.consoleServer.bEnableType = true;
                            }
                        }
                        else {
                            if (super.consoleServer != null) {
                                super.consoleServer.type(str + "\n");
                            }
                            RTSConf.cur.console.logFilePrintln(str);
                        }
                        RTSConf.cur.console.getEnv().exec(str);
                        if (bool2) {
                            if (super.consoleServer != null) {
                                super.consoleServer.bEnableType = false;
                            }
                            System.out.print(RTSConf.cur.console._getPrompt());
                            System.out.flush();
                            if (super.consoleServer != null) {
                                super.consoleServer.bEnableType = true;
                            }
                        }
                        if (super.consoleServer != null) {
                            super.consoleServer.typeNum();
                        }
                    }
                    RTSConf.cur.loopMsgs();
                    USGS.engine();
                    long l2 = 1L + Time.endReal() - Time.real();
                    if (l2 <= 0L && l1 + 1000L < Time.real()) {
                        l2 = 1L;
                    }
                    if (l2 <= 0L) {
                        continue;
                    }
                    try {
                        Thread.sleep(l2);
                    }
                    catch (Exception ex) {}
                    l1 = Time.real();
                }
            }
        }
    }
    
    public void onBeginApp() {
    }
    
    private static boolean tryStartGS() {
        if (DServer.args == null) {
            return true;
        }
        int i = -1;
        for (int j = 0; j < DServer.args.length; ++j) {
            if ("-GS".equals(DServer.args[j])) {
                i = j;
                break;
            }
        }
        if (i == -1) {
            return true;
        }
        String str1 = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        for (int k = i + 1; k < DServer.args.length - 1; ++k) {
            if ("-name".equals(DServer.args[k])) {
                str1 = DServer.args[k + 1];
                ++k;
            }
            else if ("-room".equals(DServer.args[k])) {
                str2 = DServer.args[k + 1];
                ++k;
            }
            else if ("-port".equals(DServer.args[k])) {
                str3 = DServer.args[k + 1];
                ++k;
                try {
                    DServer.localPort = Integer.parseInt(str3);
                    if (DServer.localPort < 0) {
                        DServer.localPort = 0;
                    }
                }
                catch (Exception localException) {}
            }
            else if ("-maxclients".equals(DServer.args[k])) {
                str4 = DServer.args[k + 1];
                ++k;
            }
            else if ("-lobbyaddr".equals(DServer.args[k])) {
                str5 = DServer.args[k + 1];
                ++k;
            }
            else if ("-lobbyport".equals(DServer.args[k])) {
                str6 = DServer.args[k + 1];
                ++k;
            }
            else if ("-groupid".equals(DServer.args[k])) {
                str7 = DServer.args[k + 1];
                ++k;
            }
        }
        return USGS.tryStartDedicated(str1, str2, str4, str5, str6, str7);
    }
    
    public static void main(final String[] paramArrayOfString) {
        System.out.println("UP 3.0 RC4 dedicated server without obfuscation");
        DServer.args = paramArrayOfString;
        String str1 = "confs.ini";
        if (DServer.args != null) {
            for (int i = 0; i < DServer.args.length - 1; ++i) {
                if ("-conf".equals(DServer.args[i])) {
                    str1 = DServer.args[i + 1];
                }
                else if ("-cmd".equals(DServer.args[i])) {
                    DServer.cmdFile = DServer.args[i + 1];
                }
            }
        }
        if (!tryStartGS()) {
            return;
        }
        BaseObject.setEnabled(false);
        final DServer localDServer = new DServer();
        if (!USGS.isUsed() && DServer.args != null) {
            for (int j = 0; j < DServer.args.length; ++j) {
                if ("-room".equals(DServer.args[j])) {
                    if (++j < DServer.args.length) {
                        localDServer.netGameSpy = new GameSpy();
                        localDServer.netGameSpy.roomName = DServer.args[j];
                        localDServer.netGameSpy.set(DServer.args[j], null, 0);
                    }
                }
                else if ("-master".equals(DServer.args[j]) && ++j < DServer.args.length) {
                    final String str2 = DServer.args[j];
                    final int k = str2.indexOf(":");
                    if (k >= 0) {
                        if (k > 0) {
                            GameSpy.MASTER_ADDR = str2.substring(0, k);
                        }
                        try {
                            GameSpy.MASTER_PORT = Integer.parseInt(str2.substring(k + 1));
                        }
                        catch (Exception localException) {}
                    }
                    else {
                        GameSpy.MASTER_ADDR = str2;
                    }
                }
            }
        }
        Main.exec(localDServer, str1, "il2 server", 1);
    }
    
    static {
        DServer.cmdFile = "server.cmd";
        DServer.localPort = 0;
    }
    
    class ConsoleIO extends Thread
    {
        byte[] inputBuf;
        
        ConsoleIO() {
            super();
            this.inputBuf = new byte[2048];
        }
        
        public void run() {
            int i = 1;
            while (RTSConf.cur != null && !RTSConf.isRequestExitApp()) {
                try {
                    if (i != 0) {
                        if (DServer.this.consoleServer != null) {
                            DServer.this.consoleServer.bEnableType = false;
                        }
                        System.out.print(RTSConf.cur.console.getEnv().curNumCmd() + 1 + ">");
                        System.out.flush();
                        if (DServer.this.consoleServer != null) {
                            DServer.this.consoleServer.bEnableType = true;
                        }
                        i = 0;
                    }
                    int j = 0;
                    try {
                        j = System.in.read(this.inputBuf);
                    }
                    catch (Exception localException3) {
                        break;
                    }
                    if (j < 0) {
                        break;
                    }
                    while (j > 0 && this.inputBuf[j - 1] < 32) {
                        --j;
                    }
                    if (j > 0) {
                        while (true) {
                            synchronized (DServer.this.oCommandSync) {
                                if (RTSConf.isRequestExitApp()) {
                                    break;
                                }
                                if (!DServer.this.bCommand) {
                                    DServer.this.sCommand = new String(this.inputBuf, 0, j);
                                    DServer.this.bCommandNet = false;
                                    DServer.this.bCommand = true;
                                    break;
                                }
                            }
                            try {
                                Thread.sleep(10L);
                            }
                            catch (Exception localException3) {}
                        }
                        while (true) {
                            synchronized (DServer.this.oCommandSync) {
                                if (RTSConf.isRequestExitApp()) {
                                    break;
                                }
                                if (!DServer.this.bCommand) {
                                    break;
                                }
                            }
                            try {
                                Thread.sleep(10L);
                            }
                            catch (Exception localException2) {}
                        }
                        i = 1;
                    }
                    else {
                        RTSConf.cur.console.logFilePrintln("");
                        if (DServer.this.consoleServer != null) {
                            DServer.this.consoleServer.type("\n");
                        }
                        System.out.print(RTSConf.cur.console.getEnv().curNumCmd() + 1 + ">");
                        System.out.flush();
                    }
                }
                catch (Exception ex) {}
                try {
                    Thread.sleep(1L);
                }
                catch (Exception localException3) {}
            }
        }
    }
    
    private class Background extends BackgroundLoop
    {
        protected void step() {
            RTSConf.cur.loopMsgs();
        }
        
        public Background() {
            super();
            this.setThisAsCurrent();
        }
    }
    
    class CmdExit extends Cmd
    {
        CmdExit() {
            super();
            super._levelAccess = 1;
        }
        
        public Object exec(final CmdEnv paramCmdEnv, final Map paramMap) {
            if (DServer.this.netGameSpy != null) {
                DServer.this.netGameSpy.sendExiting();
            }
            Main.doGameExit();
            return null;
        }
    }
}
