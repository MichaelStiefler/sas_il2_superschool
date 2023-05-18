package com.maddox.il2.engine;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import com.maddox.il2.game.Main;
import com.maddox.opengl.GLCaps;
import com.maddox.opengl.GLContext;
import com.maddox.opengl.GLContextException;
import com.maddox.opengl.GLInitCaps;
import com.maddox.opengl.Provider;
import com.maddox.opengl.ProviderException;
import com.maddox.opengl.gl;
import com.maddox.rts.CLASS;
import com.maddox.rts.Console;
import com.maddox.rts.Cpu86ID;
import com.maddox.rts.IniFile;
import com.maddox.rts.MainWin32;
import com.maddox.rts.NetChannel;
import com.maddox.rts.RTSConf;
import com.maddox.rts.RTSConfWin;
import com.maddox.rts.ScreenMode;
import com.maddox.rts.net.SocksUdpSocket;
import com.maddox.sound.AudioDevice;
import com.maddox.util.UnicodeTo8bit;

public class Config {

    public boolean isUse3Renders() {
        return windowUse3Renders && windowSaveAspect;
    }

    public void checkWindowUse3Renders() {
        if (!windowUse3Renders) {
            return;
        } else {
            windowUse3Renders = windowWidth == windowHeight * 4;
            return;
        }
    }

    public static boolean isUSE_RENDER() {
        return bUseRender;
    }

    private void loadGlProvider() {
        String s = ini.get("GLPROVIDER", "GL", (String) null);
        if (s != null)
            glLib = s;
        gluLib = ini.get("GLPROVIDER", "GLU", (String) null);
    }

    public void load() throws GLContextException, ProviderException {
        loadNet();
        loadGame();
        loadConsole();
        if (isUSE_RENDER()) {
            windowWidth = ini.get("window", "width", windowWidth);
            windowHeight = ini.get("window", "height", windowHeight);
            windowColourBits = ini.get("window", "ColourBits", windowColourBits);
            windowDepthBits = ini.get("window", "DepthBits", windowDepthBits);
            windowStencilBits = ini.get("window", "StencilBits", windowStencilBits);
            windowFullScreen = ini.get("window", "FullScreen", windowFullScreen);
            windowChangeScreenRes = ini.get("window", "ChangeScreenRes", windowChangeScreenRes);
            windowEnableResize = ini.get("window", "EnableResize", windowEnableResize);
            windowEnableClose = ini.get("window", "EnableClose", windowEnableClose);
            windowSaveAspect = ini.get("window", "SaveAspect", windowSaveAspect);
            windowUse3Renders = ini.get("window", "Use3Renders", windowUse3Renders);
            
         // TODO: +++ Mods Settings GUI by SAS~Storebror +++
            this.bStabs4All = ini.get("Mods", "Stabs4All", 0) != 0;
            this.bNewTrackIR = ini.get("Mods", "NewTrackIR", 0) != 0;
            this.bAutoNtrkRecording = ini.get("Mods", "AutoNtrk", 0) != 0;
            this.iDarkness = ini.get("Mods", "Darkness", MAX_NIGHT_SETTINGS);
            this.iDiffuse = ini.get("Mods", "Diffuse", MAX_NIGHT_SETTINGS);
         // TODO: --- Mods Settings GUI by SAS~Storebror ---
            
            checkWindowUse3Renders();
            if (windowChangeScreenRes) {
                windowFullScreen = true;
            } else {
                ScreenMode screenmode = ScreenMode.startup();
                if (windowColourBits != screenmode.colourBits())
                    windowColourBits = screenmode.colourBits();
            }
            loadGlProvider();
            loadEngine();
            Provider.GLload(glLib);
            if (gluLib != null)
                Provider.GLUload(gluLib);
            beforeLoadSound();
        }
        // TODO: +++ Mods Settings GUI by SAS~Storebror +++
        this.bInstantLog = ini.get("Mods", "InstantLog", 2) == 1;
        this.bPipedLog = ini.get("Mods", "InstantLog", 2) == 2;
        // TODO: --- Mods Settings GUI by SAS~Storebror ---
        // TODO: +++ Additional Log Settings by SAS~Storebror +++
        this.bLogDate = ini.get("Mods", "LogDate", 1) != 0;
        this.bLogMilliseconds = ini.get("Mods", "LogMilliseconds", 1) != 0;
        this.bLogTicks = ini.get("Mods", "LogTicks", 1) != 0;
        this.iEventLogFlushTimeout = ini.get("Mods", "EventLogFlushTimeout", 1000);
        this.iLogFlushTimeout = ini.get("Mods", "LogFlushTimeout", 1000);
        // TODO: --- Additional Log Settings by SAS~Storebror ---
    }

    public void save() {
        if (isUSE_RENDER()) {
            ini.setValue("window", "width", Integer.toString(windowWidth));
            ini.setValue("window", "height", Integer.toString(windowHeight));
            ini.setValue("window", "ColourBits", Integer.toString(windowColourBits));
            ini.setValue("window", "DepthBits", Integer.toString(windowDepthBits));
            ini.setValue("window", "StencilBits", Integer.toString(windowStencilBits));
            ini.setValue("window", "FullScreen", windowFullScreen ? "1" : "0");
            ini.setValue("window", "ChangeScreenRes", windowChangeScreenRes ? "1" : "0");
            ini.setValue("window", "EnableResize", windowEnableResize ? "1" : "0");
            ini.setValue("window", "EnableClose", windowEnableClose ? "1" : "0");
            ini.setValue("window", "SaveAspect", windowSaveAspect ? "1" : "0");
            ini.setValue("window", "Use3Renders", windowUse3Renders ? "1" : "0");
            ini.setValue("GLPROVIDER", "GL", glLib);
            if (gluLib != null)
                ini.setValue("GLPROVIDER", "GLU", gluLib);
            // TODO: +++ Mods Settings GUI by SAS~Storebror +++
            ini.setValue("Mods", "Stabs4All", this.bStabs4All ? "1" : "0");
            ini.setValue("Mods", "NewTrackIR", this.bNewTrackIR ? "1" : "0");
            ini.setValue("Mods", "AutoNtrk", this.bAutoNtrkRecording ? "1" : "0");
            ini.setValue("Mods", "Darkness", "" + this.iDarkness);
            ini.setValue("Mods", "Diffuse", "" + this.iDiffuse);
            ini.setValue("Mods", "EventLogFlushTimeout", "" + this.iEventLogFlushTimeout);
            ini.setValue("Mods", "LogFlushTimeout", "" + this.iLogFlushTimeout);
            // TODO: --- Mods Settings GUI by SAS~Storebror ---
            saveSound();
            saveEngine();
        }
        // TODO: +++ Mods Settings GUI by SAS~Storebror +++
        ini.setValue("Mods", "InstantLog", this.bPipedLog? "2" : this.bInstantLog?"1":"0");
        // TODO: --- Mods Settings GUI by SAS~Storebror ---
        saveConsole();
        saveNet();
        ini.saveFile();
    }

    private void loadNet() {
        netLocalPort = ini.get("NET", "localPort", netLocalPort, 1000, 65000);
        netRemotePort = ini.get("NET", "remotePort", netRemotePort, 1000, 65000);
        netSpeed = ini.get("NET", "speed", netSpeed, 300, 0xf4240);
        // TODO: +++ Mods Settings GUI by SAS~Storebror +++
        this.bNetBoost = ini.get("Mods", "HighSpeedNet", 0) != 0;
        if (this.bNetBoost) 
            netSpeed = NET_SPEED_HIGH;
        this.bAddDefaultCountryNone = ini.get("Mods", "AddDefaultCountryNone", 0) != 0;
        this.bUseAutoAdminLogin = ini.get("Mods", "useNetLogin", 0) != 0;
        this.sAutoAdminPassword = ini.get("Mods", "netLogin", "");
        this.bUseAutoUserLogin = ini.get("Mods", "useNetLoginUser", 0) != 0;
        this.sAutoUserPassword = ini.get("Mods", "netLoginUser", "");
        this.bOverrideOnlineCallsign = ini.get("Mods", "useNetCallsign", 0) != 0;
        this.sOnlineCallsign = ini.get("Mods", "netCallsign", "");
        this.bSkinDownloadNotifications = ini.get("Mods", "SkinDownloadNotifications", 1) != 0;
        // TODO: +++ Override Online Callsign - by SAS~Storebror +++
        if (Main.cur().netGameSpy != null && this.bOverrideOnlineCallsign && this.sOnlineCallsign.length() > 0) {
            Main.cur().netGameSpy.userName = UnicodeTo8bit.load(this.sOnlineCallsign);
        }
        // TODO: --- Override Online Callsign - by SAS~Storebror ---
        // TODO: --- Mods Settings GUI by SAS~Storebror ---
        netLocalHost = ini.get("NET", "localHost", netLocalHost);
        if (netLocalHost != null)
            try {
                InetAddress inetaddress = InetAddress.getByName(netLocalHost);
                DatagramSocket datagramsocket = new DatagramSocket(netLocalPort, inetaddress);
                datagramsocket.close();
            } catch (Exception exception) {
                System.out.println("Unknown net address: " + netLocalHost);
                netLocalHost = null;
            }
        if (isUSE_RENDER()) {
            netRemotePort = ini.get("NET", "remotePort", netRemotePort, 1000, 65000);
            netRemoteHost = ini.get("NET", "remoteHost", netRemoteHost);
            netRouteChannels = ini.get("NET", "routeChannels", netRouteChannels, 0, 16);
            netServerChannels = ini.get("NET", "serverChannels", netServerChannels, 1, 31);
            zutiLoadServers();
        } else {
            netServerChannels = ini.get("NET", "serverChannels", netServerChannels, 1, 128);
        }
        netSkinDownload = ini.get("NET", "SkinDownload", netSkinDownload);
        netServerName = UnicodeTo8bit.load(ini.get("NET", "serverName", ""));
        netServerDescription = UnicodeTo8bit.load(ini.get("NET", "serverDescription", ""));
        NetChannel.bCheckServerTimeSpeed = ini.get("NET", "checkServerTimeSpeed", NetChannel.bCheckServerTimeSpeed);
        NetChannel.bCheckClientTimeSpeed = ini.get("NET", "checkClientTimeSpeed", NetChannel.bCheckClientTimeSpeed);
        NetChannel.checkTimeSpeedDifferense = ini.get("NET", "checkTimeSpeedDifferense", (float) NetChannel.checkTimeSpeedDifferense, 0.01F, 1000F);
        NetChannel.checkTimeSpeedInterval = ini.get("NET", "checkTimeSpeedInterval", NetChannel.checkTimeSpeedInterval, 1, 1000);
        SocksUdpSocket.setProxyHost(ini.get("NET", "socksHost", (String) null));
        SocksUdpSocket.setProxyPort(ini.get("NET", "socksPort", 1080));
        SocksUdpSocket.setProxyUser(ini.get("NET", "socksUser", (String) null));
        SocksUdpSocket.setProxyPassword(ini.get("NET", "socksPwd", (String) null));
    }

    private void saveNet() {
        ini.setValue("NET", "localPort", "" + netLocalPort);
        ini.setValue("NET", "speed", "" + netSpeed);
        if (isUSE_RENDER()) {
            ini.setValue("NET", "remotePort", "" + netRemotePort);
            ini.setValue("NET", "remoteHost", netRemoteHost);
            ini.setValue("NET", "routeChannels", "" + netRouteChannels);
            for (int i = 0; i < zutiServerNames.size(); i++) {
                String s = "";
                if (i < 10)
                    s = "00" + (new Integer(i)).toString();
                else if (i > 9 && i < 100)
                    s = "0" + (new Integer(i)).toString();
                else if (i > 99)
                    s = (new Integer(i)).toString();
                ini.setValue("NET", "remoteHost_" + s, (String) zutiServerNames.get(i));
            }

        }
        
     // TODO: +++ Mods Settings GUI by SAS~Storebror +++
        ini.setValue("Mods", "HighSpeedNet", this.bNetBoost ? "1" : "0");
        ini.setValue("Mods", "AddDefaultCountryNone", this.bAddDefaultCountryNone ? "1" : "0");
        ini.setValue("Mods", "useNetLogin", this.bUseAutoAdminLogin ? "1" : "0");
        ini.setValue("Mods", "netLogin", this.sAutoAdminPassword);
        ini.setValue("Mods", "useNetLoginUser", this.bUseAutoUserLogin ? "1" : "0");
        ini.setValue("Mods", "netLoginUser", this.sAutoUserPassword);
        ini.setValue("Mods", "useNetCallsign", this.bOverrideOnlineCallsign ? "1" : "0");
        ini.setValue("Mods", "netCallsign", this.sOnlineCallsign);
        ini.setValue("Mods", "SkinDownloadNotifications", this.bSkinDownloadNotifications ? "1" : "0");
     // TODO: --- Mods Settings GUI by SAS~Storebror ---
        
        ini.setValue("NET", "serverChannels", "" + netServerChannels);
        ini.setValue("NET", "SkinDownload", netSkinDownload ? "1" : "0");
        ini.setValue("NET", "serverName", UnicodeTo8bit.save(netServerName, false));
        ini.setValue("NET", "serverDescription", UnicodeTo8bit.save(netServerDescription, false));
        if (SocksUdpSocket.getProxyHost() != null)
            ini.setValue("NET", "socksHost", SocksUdpSocket.getProxyHost());
        else
            ini.setValue("NET", "socksHost", "");
        if (SocksUdpSocket.getProxyPort() != 1080)
            ini.setValue("NET", "socksPort", "" + SocksUdpSocket.getProxyPort());
        else
            ini.deleteValue("NET", "socksPort");
        if (SocksUdpSocket.getProxyUser() != null)
            ini.setValue("NET", "socksUser", SocksUdpSocket.getProxyUser());
        else
            ini.deleteValue("NET", "socksUser");
        if (SocksUdpSocket.getProxyPassword() != null)
            ini.setValue("NET", "socksPwd", SocksUdpSocket.getProxyPassword());
        else
            ini.deleteValue("NET", "socksPwd");
    }

    private void loadGame() {
        b3dgunners = ini.get("game", "3dgunners", b3dgunners);
        clear_cache = ini.get("game", "ClearCache", clear_cache);
        newCloudsRender = ini.get("game", "TypeClouds", newCloudsRender);
    }

    private void loadConsole() {
        RTSConf.cur.console.bWrap = ini.get("Console", "WRAP", 1, 0, 1) == 1;
        RTSConf.cur.console.setMaxHistoryOut(ini.get("Console", "HISTORY", 128, 0, 10000));
        RTSConf.cur.console.setMaxHistoryCmd(ini.get("Console", "HISTORYCMD", 128, 0, 10000));
        RTSConf.cur.console.setPageHistoryOut(ini.get("Console", "PAGE", 20, 1, 100));
        RTSConf.cur.console.setPause(ini.get("Console", "PAUSE", 1, 0, 1) == 1);
        RTSConf.cur.console.setLogKeep(ini.get("Console", "LOGKEEP", 1, 0, 1) == 1);
        String s = ini.getValue("Console", "LOGFILE");
        System.out.println("LOGFILE=" + s);
        if (s.length() > 0)
            RTSConf.cur.console.setLogFileName(s);
        else
            RTSConf.cur.console.setLogFileName("log.lst");
        // +++ Strange things happening here when using IL-2 with kegety's dumper, that's why we have to dump-log this value.
        int iLog = ini.get("Console", "LOG", 0, 0, 1);
        System.out.println("LOG=" + iLog);
        RTSConf.cur.console.log(iLog == 1);
        //RTSConf.cur.console.log(ini.get("Console", "LOG", 0, 0, 1) == 1);
        // --- Strange things happening here when using IL-2 with kegety's dumper, that's why we have to dump-log this value.
        Console.setTypeTimeInLogFile(ini.get("Console", "LOGTIME", 0, 0, 1) == 1);
        s = ini.getValue("Console", "LOAD");
        if (s.length() > 0)
            RTSConf.cur.console.load(s);
    }

    private void saveConsole() {
        if (RTSConf.cur.console.getEnv().levelAccess() == 0) {
            ini.setValue("Console", "WRAP", Integer.toString(RTSConf.cur.console.bWrap ? 1 : 0));
            ini.setValue("Console", "PAUSE", Integer.toString(RTSConf.cur.console.isPaused() ? 1 : 0));
            ini.setValue("Console", "PAGE", Integer.toString(RTSConf.cur.console.pageHistoryOut()));
        }
        ini.setValue("Console", "HISTORY", Integer.toString(RTSConf.cur.console.maxHistoryOut()));
        ini.setValue("Console", "HISTORYCMD", Integer.toString(RTSConf.cur.console.maxHistoryCmd()));
        ini.setValue("Console", "LOGFILE", RTSConf.cur.console.logFileName());
        ini.setValue("Console", "LOG", Integer.toString(RTSConf.cur.console.isLog() ? 1 : 0));
        ini.setValue("Console", "LOGKEEP", Integer.toString(RTSConf.cur.console.isLogKeep() ? 1 : 0));
        ini.setValue("Console", "LOGTIME", Integer.toString(Console.isTypeTimeInLogFile() ? 1 : 0));
        String s = ini.getValue("Console", "SAVE");
        if (s.length() > 0)
            RTSConf.cur.console.save(s);
        RTSConf.cur.console.log(false);
    }

    public void loadEngine() {
        loadEngine(ini.get("GLPROVIDER", "GL", (String) null));
    }

    public void loadEngine(String s) {
        if (s == null)
            s = glLib;
        String s1 = ini.get("GLPROVIDERS", "DirectX", "");
        String s2 = "Render_OpenGL";
        if (s1 != null && s.equalsIgnoreCase(s1))
            s2 = "Render_DirectX";
        RenderContext.loadConfig(ini, s2);
    }

    private void saveEngine() {
        RenderContext.saveConfig();
    }

    public boolean isDebugSound() {
        return bDebugSound;
    }

    public boolean isSoundUse() {
        return bSoundUse;
    }

    private void beforeLoadSound() {
        bSoundUse = ini.get("sound", "SoundUse", false);
        bDebugSound = ini.get("sound", "DebugSound", bDebugSound);
    }

    private void saveSound() {
        ini.setValue("sound", "SoundUse", bSoundUse ? "1" : "0");
        AudioDevice.saveControls(ini, "sound");
    }

    public void beginSound() {
        AudioDevice.setMessageMode(bDebugSound ? 1 : 0);
        AudioDevice.loadControls(ini, "sound");
        if (bSoundUse) {
            bSoundUse = false;
            if (RTSConf.cur != null && (RTSConf.cur instanceof RTSConfWin)) {
                int i = RTSConf.cur.mainWindow.hWnd();
                if (i != 0)
                    if (AudioDevice.initialize(i, engineDllName() + ".dll"))
                        bSoundUse = true;
                    else
                        System.err.println("AudioDevice NOT initialized");
            }
        } else {
            AudioDevice.initialize(0, engineDllName() + ".dll");
        }
        AudioDevice.resetControls();
    }

    public void endSound() {
        if (bSoundUse)
            AudioDevice.done();
    }

    public GLInitCaps getGLCaps() {
        GLInitCaps glinitcaps = new GLInitCaps();
        glinitcaps.setDoubleBuffered(true);
        glinitcaps.setColourBits(windowColourBits);
        glinitcaps.setDepthBits(windowDepthBits);
        glinitcaps.setStencilBits(windowStencilBits);
        return glinitcaps;
    }

    public void setGLCaps(GLCaps glcaps) {
        windowColourBits = glcaps.getColourBits();
        windowDepthBits = glcaps.getDepthBits();
        windowStencilBits = glcaps.getStencilBits();
    }

    public GLContext createGlContext(GLContext glcontext, boolean flag, boolean flag1, int i, int j, int k, int l, int i1) throws GLContextException {
        ScreenMode screenmode = ScreenMode.readCurrent();
        if (flag) {
            if (ScreenMode.set(i, j, k)) {
                i = ScreenMode.current().width();
                j = ScreenMode.current().height();
                k = ScreenMode.current().colourBits();
            } else {
                throw new GLContextException("ScreenMode NOT changed");
            }
        } else if (ScreenMode.current() != ScreenMode.startup())
            ScreenMode.restore();
        boolean flag2 = false;
        if (flag1)
            flag2 = ((MainWin32) RTSConf.cur.mainWindow).create(windowTitle, i, j);
        else
            flag2 = ((MainWin32) RTSConf.cur.mainWindow).create(windowTitle, windowEnableClose, windowEnableResize, i, j);
        if (!flag2) {
            ScreenMode.set(screenmode);
            throw new GLContextException("Window NOT created");
        }
        i = RTSConf.cur.mainWindow.width();
        j = RTSConf.cur.mainWindow.height();
        GLInitCaps glinitcaps = new GLInitCaps();
        glinitcaps.setDoubleBuffered(true);
        glinitcaps.setColourBits(k);
        glinitcaps.setDepthBits(l);
        glinitcaps.setStencilBits(i1);
        try {
            if (glcontext != null) {
                glcontext.changeWin32(glinitcaps, RTSConf.cur.mainWindow.hWnd(), true, i, j);
            } else {
                glcontext = new GLContext(glinitcaps);
                glcontext.createWin32(RTSConf.cur.mainWindow.hWnd(), true, i, j);
            }
        } catch (GLContextException glcontextexception) {
            ((MainWin32) RTSConf.cur.mainWindow).destroy();
            ScreenMode.set(screenmode);
            throw glcontextexception;
        }
        windowFullScreen = flag1;
        windowChangeScreenRes = flag;
        windowWidth = RTSConf.cur.mainWindow.width();
        windowHeight = RTSConf.cur.mainWindow.height();
        windowColourBits = glcontext.getCaps().getColourBits();
        windowDepthBits = glcontext.getCaps().getDepthBits();
        windowStencilBits = glcontext.getCaps().getStencilBits();
        checkWindowUse3Renders();
        return glcontext;
    }

    public GLContext createGlContext(String s) throws GLContextException {
        windowTitle = s;
        return createGlContext(null, windowChangeScreenRes, windowFullScreen, windowWidth, windowHeight, windowColourBits, windowDepthBits, windowStencilBits);
    }

    public static void typeCurrentScreenMode() {
        ScreenMode screenmode = ScreenMode.current();
        System.err.println("Current screen mode: " + screenmode.width() + "x" + screenmode.height() + "x" + screenmode.colourBits());
    }

    public static void typeScreenModes() {
        ScreenMode ascreenmode[] = ScreenMode.all();
        System.err.print("Screen modes: ");
        for (int i = 0; i < ascreenmode.length; i++) {
            if (i % 4 == 0)
                System.err.println("");
            System.err.print("\t" + i + " " + ascreenmode[i].width() + "x" + ascreenmode[i].height() + "x" + ascreenmode[i].colourBits());
        }

        System.err.println("");
    }

    public static void typeGLCaps() {
        GLCaps aglcaps[] = Provider.getGLCaps();
        System.err.println("Caps OpenGL library:");
        for (int i = 0; i < aglcaps.length; i++) {
            System.err.print(i);
            System.err.print(aglcaps[i].getDevice() != 1 ? "  DRAW_TO_BITMAP" : "  DRAW_TO_WINDOW");
            System.err.print(aglcaps[i].isDoubleBuffered() ? "  DOUBLEBUFFER" : "  SINGLEBUFFER");
            System.err.print(aglcaps[i].isStereo() ? "  STEREO" : "  NOSTEREO");
            System.err.println(aglcaps[i].getPixelType() != 1 ? "  TYPE_COLOURINDEX" : "  TYPE_RGBA");
            System.err.print("  ColourBits: " + aglcaps[i].getColourBits());
            System.err.print("  AlphaBits: " + aglcaps[i].getAlphaBits());
            System.err.print("  AccumBits: " + aglcaps[i].getAccumBits());
            System.err.print("  DepthBits: " + aglcaps[i].getDepthBits());
            System.err.println("  StencilBits: " + aglcaps[i].getStencilBits());
        }

    }

    public static void typeGlStrings() {
        System.err.println("OpenGL library:");
        System.err.println("  Vendor: " + gl.GetString(7936));
        System.err.println("  Render: " + gl.GetString(7937));
        System.err.println("  Version: " + gl.GetString(7938));
        System.err.println("  Extensions: " + gl.GetString(7939));
    }

    public static void typeProvider() {
        System.err.println("OpenGL provider: " + Provider.GLname());
        try {
            System.err.println("GLU provider: " + Provider.GLUname());
        } catch (Exception exception) {}
    }

    public void typeContextSettings(GLContext glcontext) {
        System.err.println("Size: " + glcontext.width() + "x" + glcontext.height());
        System.err.println("ColorBits: " + glcontext.getCaps().getColourBits());
        System.err.println("DepthBits: " + glcontext.getCaps().getDepthBits());
        System.err.println("StencilBits: " + glcontext.getCaps().getStencilBits());
        System.err.println("isDoubleBuffered: " + glcontext.getCaps().isDoubleBuffered());
    }

    public static boolean isAppEditor() {
        return cur.bAppEditor;
    }

    public static String engineDllName() {
        if (CLASS.ser() != 0) {
            if (Cpu86ID.getVendor() == 1 && Cpu86ID.isSSE4())
                return "il2corSSE4";
            if (Cpu86ID.getVendor() == 1 && Cpu86ID.isSSE3())
                return "il2corSSE3";
            if (Cpu86ID.getVendor() == 1 && Cpu86ID.isSSE2())
                return "il2coreP4";
            else
                return "il2_core";
        } else {
            Cpu86ID.getVendor();
            return "il2_server";
        }
    }

    public Config(IniFile inifile, boolean flag) {
        ini = null;
        mainSection = "il2";
        windowTitle = "Il2";
        windowWidth = 640;
        windowHeight = 480;
        windowColourBits = 16;
        windowDepthBits = 16;
        windowStencilBits = 0;
        windowFullScreen = true;
        windowChangeScreenRes = true;
        windowEnableResize = false;
        windowEnableClose = false;
        windowSaveAspect = true;
        windowUse3Renders = false;
        glLib = "opengl32.dll";
        gluLib = null;
        netLocalPort = 21000;
        netRemotePort = 21000;
        netLocalHost = null;
        netRemoteHost = "";
        netSpeed = 5000;
        netRouteChannels = 0;
        netServerChannels = 8;
        netSkinDownload = true;
        netServerName = "";
        netServerDescription = "";
        b3dgunners = false;
        clear_cache = true;
        newCloudsRender = true;
        zutiServerNames = null;
        bSoundUse = false;
        bDebugSound = false;
        bAppEditor = false;
        zutiServerNames = new ArrayList();
        bUseRender = flag;
        GObj.loadNative();
        ini = inifile;
        load();
    }

    private void zutiLoadServers() {
        for (int i = 0; i < 255; i++) {
            String s = "";
            if (i < 10)
                s = "00" + (new Integer(i)).toString();
            else if (i > 9 && i < 100)
                s = "0" + (new Integer(i)).toString();
            else if (i > 99)
                s = (new Integer(i)).toString();
            String s1 = ini.get("NET", "remoteHost_" + s, "");
            if (s1.trim().length() > 0)
                zutiServerNames.add(s1);
        }

    }

    public ArrayList zutiGetServerNames() {
        return zutiServerNames;
    }

    public void zutiAddServerName(String s) {
        if (!zutiServerNames.contains(s))
            zutiServerNames.add(s);
    }

    public static String getVersion() {
        return _VERSION;
    }

    public static int getTrackSingleVersion() {
        return _TRACK_SINGLE_VERSION;
    }
    
    public static final String  JRE                           = "JRE";
    public static final boolean AEROMASH                      = false;
    public static final String  PRODUCT                       = "FB_PF";
    private static final String _VERSION                      = "4.10.1_(UP_EDITION)";
    private static final int    _TRACK_SINGLE_VERSION         = 40;
    public static final int     TRACK_NET_VERSION             = 104;
    public static final boolean TRACK_CHECK                   = false;
    public static final String  VERSION                       = "4.10.1m_(UP_EDITION)";
    public static final String  NET_VERSION                   = "FB_PF_v_4.10.1m_(UP_EDITION)";
    public static final int     TRACK_SINGLE_VERSION          = 140;
    public static String        LOCALE                        = "RU";
    public static final boolean bCHECK_LOCALE                 = true;
    public static final String  RELEASE                       = "ON";
    public static final String  PROTECT                       = "OFF";
    public static final boolean bCHECK_EXPIRED                = false;
    public static final int     EXPIRED_YEAR                  = 2005;
    public static final int     EXPIRED_MONTH                 = 11;
    public static final int     EXPIRED_DAY                   = 1;
    public static Config        cur;
    public static final int     ADAPTER_INTERPOLATOR_TICK_POS = -1000;
    public static final int     ADAPTER_COLLISION_TICK_POS    = -999;
    public static final int     ADAPTER_RENDER_TICK_POS       = 0x7fffffff;
    public static final String  CONSOLE                       = "Console";
    public static final String  C_WRAP                        = "WRAP";
    public static final String  C_HISTORY                     = "HISTORY";
    public static final String  C_HISTORYCMD                  = "HISTORYCMD";
    public static final String  C_PAGE                        = "PAGE";
    public static final String  C_PAUSE                       = "PAUSE";
    public static final String  C_LOG                         = "LOG";
    public static final String  C_LOGKEEP                     = "LOGKEEP";
    public static final String  C_LOGTIME                     = "LOGTIME";
    public static final String  C_LOGFILE                     = "LOGFILE";
    public static final String  C_LOAD                        = "LOAD";
    public static final String  C_SAVE                        = "SAVE";
    public static final String  WINDOW                        = "window";
    public static final String  WIDTH                         = "width";
    public static final String  HEIGHT                        = "height";
    public static final String  COLOURBITS                    = "ColourBits";
    public static final String  DEPTHBITS                     = "DepthBits";
    public static final String  STENCILBITS                   = "StencilBits";
    public static final String  FULLSCREEN                    = "FullScreen";
    public static final String  CHANGESCREENRES               = "ChangeScreenRes";
    public static final String  ENABLERESIZE                  = "EnableResize";
    public static final String  ENABLECLOSE                   = "EnableClose";
    public static final String  SAVEASPECT                    = "SaveAspect";
    public static final String  USE3RENDERS                   = "Use3Renders";
    public static final String  GLPROVIDER                    = "GLPROVIDER";
    public static final String  GL_LIB                        = "GL";
    public static final String  GLU_LIB                       = "GLU";
    public static final String  SOUND                         = "sound";
    public static final String  SOUND_USE                     = "SoundUse";
    public static final String  SOUND_DEBUG                   = "DebugSound";
    public static final String  NET                           = "NET";
    public static final String  NET_LOCAL_PORT                = "localPort";
    public static final String  NET_LOCAL_HOST                = "localHost";
    public static final String  NET_REMOTE_PORT               = "remotePort";
    public static final String  NET_REMOTE_HOST               = "remoteHost";
    public static final String  NET_SPEED                     = "speed";
    public static final String  NET_ROUTE_CHANNELS            = "routeChannels";
    public static final String  NET_SERVER_CHANNELS           = "serverChannels";
    public static final String  NET_SKIN_DOWNLOAD             = "SkinDownload";
    public static final String  NET_SERVER_NAME               = "serverName";
    public static final String  NET_SERVER_DESCRIPTION        = "serverDescription";
    public static final String  NET_SOCKS_HOST                = "socksHost";
    public static final String  NET_SOCKS_PORT                = "socksPort";
    public static final String  NET_SOCKS_USER                = "socksUser";
    public static final String  NET_SOCKS_PWD                 = "socksPwd";
    public static final String  GAME                          = "game";
    public static final String  GAME_3DGUNNERS                = "3dgunners";
    public static final String  GAME_CLEARCACHE               = "ClearCache";
    public static final String  GAME_TYPECLOUDS               = "TypeClouds";
    public IniFile              ini;
    public String               mainSection;
    public String               windowTitle;
    public int                  windowWidth;
    public int                  windowHeight;
    public int                  windowColourBits;
    public int                  windowDepthBits;
    public int                  windowStencilBits;
    public boolean              windowFullScreen;
    public boolean              windowChangeScreenRes;
    public boolean              windowEnableResize;
    public boolean              windowEnableClose;
    public boolean              windowSaveAspect;
    public boolean              windowUse3Renders;
    public String               glLib;
    public String               gluLib;
    public int                  netLocalPort;
    public int                  netRemotePort;
    public String               netLocalHost;
    public String               netRemoteHost;
    public int                  netSpeed;
    public int                  netRouteChannels;
    public int                  netServerChannels;
    public boolean              netSkinDownload;
    public String               netServerName;
    public String               netServerDescription;
    public boolean              b3dgunners;
    public boolean              clear_cache;
    public boolean              newCloudsRender;
    private static boolean      bUseRender                    = false;
    public ArrayList            zutiServerNames;
    protected boolean           bSoundUse;
    private boolean             bDebugSound;
    public boolean              bAppEditor;
 // TODO: +++ Mods Settings GUI by SAS~Storebror +++
    public static final int     NET_SPEED_HIGH                = 1000000;
    public boolean              bStabs4All;
    public boolean              bNetBoost;
    public boolean              bNewTrackIR;
    public boolean              bAddDefaultCountryNone;
    public boolean              bAutoNtrkRecording;
    public boolean              bUseAutoAdminLogin;
    public String               sAutoAdminPassword;
    public boolean              bUseAutoUserLogin;
    public String               sAutoUserPassword;
    public boolean              bOverrideOnlineCallsign;
    public String               sOnlineCallsign;
    public int                  iDarkness;
    public int                  iDiffuse;
    public boolean              bSkinDownloadNotifications;
    public boolean              bInstantLog;
    public static final int     MIN_NIGHT_SETTINGS = 0;
    public static final int     MAX_NIGHT_SETTINGS = 10;
 // TODO: --- Mods Settings GUI by SAS~Storebror ---
 // TODO: +++ Additional Log Settings by SAS~Storebror +++
    public boolean              bLogDate = true;
    public boolean              bLogMilliseconds = true;
    public boolean              bLogTicks = true;
    public boolean              bPipedLog = true;
    public int                  iEventLogFlushTimeout = 1000;
    public int                  iLogFlushTimeout = 1000;
 // TODO: --- Additional Log Settings by SAS~Storebror ---
 // TODO: +++ Widescreen backport from 4.12 by SAS~Storebror +++
    public boolean              windowsWideScreenFoV = false;
 // TODO: --- Widescreen backport from 4.12 by SAS~Storebror ---
}
