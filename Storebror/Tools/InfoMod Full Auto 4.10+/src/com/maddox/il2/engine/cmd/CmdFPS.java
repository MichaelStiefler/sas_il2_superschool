package com.maddox.il2.engine.cmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.maddox.JGP.Color4f;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.RendersMain;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.fm.Squares;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HomePath;
import com.maddox.rts.MsgTimeOut;
import com.maddox.rts.MsgTimeOutListener;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SFSReader;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;
import com.maddox.sas1946.Junidecode;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.AudioDevice;
import com.maddox.util.NumberTokenizer;

public class CmdFPS extends Cmd implements MsgTimeOutListener {

    public static class WeaponSlot {
        private String          name;
        private int             num;
        private int             bullets;
        private int             type;

        public static final int TYPE_GUN        = 0;
        public static final int TYPE_ROCKET     = 1;
        public static final int TYPE_BOMB       = 2;
        public static final int TYPE_TANK       = 3;
        public static final int TYPE_GUN_GUNNER = 4;

        public static final int TYPE_MAX        = 4;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getBullets() {
            return bullets;
        }

        public void setBullets(int bullets) {
            this.bullets = bullets;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public WeaponSlot() {
            this.name = "";
            this.num = 0;
            this.bullets = 0;
        }
    }

    public void msgTimeOut(Object obj) {
        this.msg.post();
        if (!this.bGo) {
            return;
        }
        long l = Time.real();
        int i = RendersMain.frame();
        if (l >= (this.timePrev + Math.min(this.logExtPeriod, 250L))) {
            this.fpsCur = (1000D * (i - this.framePrev)) / (l - this.timePrev);
            if (this.fpsCur < this.fpsLast && this.fpsLast > 0.0D) {
                this.fpsMinValid = true;
            }
            this.fpsLast = this.fpsCur;
            if (this.fpsMinValid && this.fpsMin > this.fpsCur) {
                this.fpsMin = this.fpsCur;
            }
            if (this.fpsMax < this.fpsCur) {
                this.fpsMax = this.fpsCur;
            }
            if ((this.fpsMinValid && this.fpsMinRecent > this.fpsCur) || ((l - this.lastFpsMin) > this.recentResetTimeout)) {
                this.fpsMinRecent = this.fpsCur;
                this.lastFpsMin = l;
            }
            if ((this.fpsMaxRecent < this.fpsCur) || ((l - this.lastFpsMax) > this.recentResetTimeout)) {
                this.fpsMaxRecent = this.fpsCur;
                this.lastFpsMax = l;
            }
            this.framePrev = i;
            this.timePrev = l;
            this.checkMissionStatus();
            this.updateFrameTimeList(i, l);
        }
        if (l >= (this.timePrevLogExt + this.logExtPeriod)) {
            this.timePrevLogExt = l;
            if (this.logExt && Mission.isPlaying()) {
                this.logFpsToFile();
            }
        }
        if (this.framePrev == this.frameStart) {
            return;
        }
        if (this.bShow) {
            this.renderFps();
        }
        if ((this.logPeriod > 0L) && (l >= (this.logPrintTime + this.logPeriod))) {
            System.out.println("fps:" + this.fpsInfo());
            this.logPrintTime = l;
        }
    }

    private void renderFps() {
        ttfont = TextScr.font();
        ttcolor = TextScr.color();
        TextScr.setFont(TTFont.font[TTFont.SMALL]);
        TextScr.setColor(FPS_COLOR);
        this.renderText(10, 0, "min", "" + (int) Math.floor(this.fpsMin == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMin), false);
        this.renderText(10, 1, "average", "" + (int) Math.floor((1000D * (this.framePrev - this.frameStart)) / (this.timePrev - this.timeStart)), false);
        this.renderText(10, 2, "max", "" + (int) Math.floor(this.fpsMax), false);
        this.renderText(10, 3, "\u2190 total", null, false);
        this.renderText(10, 4, "frame#", "" + (this.framePrev - this.frameStart), false);
        this.renderText(10, 5, "FPS", "" + (int) Math.floor(this.fpsCur), false);
        this.renderText(10, 6, "recent \u2192", null, false);
        this.renderText(10, 7, "min", "" + (int) Math.floor(this.fpsMinRecent == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMinRecent), false);
        this.renderText(10, 8, "average", "" + (int) Math.floor((1000D * (this.framePrev - ((Integer) this.frameList.get(0)).intValue())) / (this.timePrev - ((Long) this.timeList.get(0)).longValue())), false);
        this.renderText(10, 9, "max", "" + (int) Math.floor(this.fpsMaxRecent), false);
        TextScr.setFont(ttfont);
        TextScr.setColor(ttcolor);
    }

    private void renderText(int slotMax, int slot, String caption, String value, boolean changeFont) {
        Render render = (Render) Actor.getByName("renderTextScr");
        if (render == null) {
            return;
        }
        if (changeFont) {
            ttfont = TextScr.font();
            ttcolor = TextScr.color();
        }
        int viewPortWidth = render.getViewPortWidth();
        int viewPortXOffset = 0;
        if (viewPortWidth > VIEWPORT_WIDTH_MAX) {
            viewPortXOffset = (viewPortWidth - VIEWPORT_WIDTH_MAX) / 2;
            viewPortWidth = VIEWPORT_WIDTH_MAX;
        }
        int viewPortHeight = render.getViewPortHeight();
        if (changeFont) {
            TextScr.setFont(TTFont.font[TTFont.SMALL]);
            TextScr.setColor(FPS_COLOR);
        }
        int widthSegment = viewPortWidth / slotMax;
        int widthCaption = (int) TTFont.font[TTFont.SMALL].width(caption);
        int captionMargin = value == null ? (CAPTION_MARGIN + VALUE_MARGIN) / 2 : CAPTION_MARGIN;
        int heightCaption = viewPortHeight - TTFont.font[TTFont.SMALL].height() - captionMargin;
        int widthCaptionOffset = viewPortXOffset + widthSegment * slot + (widthSegment - widthCaption) / 2;
        TextScr.output(widthCaptionOffset, heightCaption, caption);
        if (value != null) {
            int widthValue = (int) TTFont.font[TTFont.SMALL].width(value);
            int heightValue = viewPortHeight - TTFont.font[TTFont.SMALL].height() - VALUE_MARGIN;
            int widthValueOffset = viewPortXOffset + widthSegment * slot + (widthSegment - widthValue) / 2;
            TextScr.output(widthValueOffset, heightValue, value);
        }
        if (changeFont) {
            TextScr.setFont(ttfont);
            TextScr.setColor(ttcolor);
        }
    }

    public Object exec(CmdEnv cmdenv, Map map) {

        boolean flag = false;
        checkMsg();
        if (map.containsKey("SHOW")) {
            bShow = true;
            int tmpRecentResetTimeout = arg(map, "SHOW", 0, (int) RECENT_RESET_DEFAULT);
            if (tmpRecentResetTimeout < 1) {
                tmpRecentResetTimeout = 1;
            }
            this.recentResetTimeout = tmpRecentResetTimeout;
            System.out.println("Enhanced FPS Counter Recent Reset Timeout = " + this.recentResetTimeout + " milliseconds.");
            flag = true;
        } else if (map.containsKey("HIDE")) {
            bShow = false;
            flag = true;
        }
        if (map.containsKey("LOG")) {
            int i = arg(map, "LOG", 0, 5);
            if (i < 0)
                i = 0;
            logPeriod = (long) i * 1000L;
            flag = true;
        }
        if (map.containsKey("LOGEXT")) {
            int tmpLogExt = arg(map, "LOGEXT", 0, 1);
            this.logExt = (tmpLogExt==1);
            int tmpLogExtPeriod = arg(map, "LOGEXT", 1, (int) LOG_EXT_PERIOD_DEFAULT);
            if (tmpLogExtPeriod < 1) {
                tmpLogExtPeriod = 1;
            }
            this.logExtPeriod = tmpLogExtPeriod;
            String logLine = "Enhanced FPS Logging ";
            if (!this.logExt) logLine+= "de";
            logLine+= "activated";
            if (this.logExt) {
                logLine+= ", file logging period = " + this.logExtPeriod + " milliseconds";
            }
            logLine+= ".";
            System.out.println(logLine);
            flag = true;
        }
        if (map.containsKey("PERF")) {
            AudioDevice.setPerformInfo(true);
            flag = true;
        }
        if (map.containsKey("START")) {
            if (this.bGo && (this.timeStart != this.timePrev) && (this.logPeriod == 0L)) {
                this.INFO_HARD("fps:" + this.fpsInfo());
            }
            this.fpsMin = this.fpsMinRecent = FPS_MIN_DEFAULT;
            this.fpsMax = this.fpsCur = this.fpsLast = this.fpsMaxRecent = 0.0D;
            this.timePrev = this.timePrevLogExt = this.logPrintTime = this.timeStart = Time.real();
            this.framePrev = this.frameStart = RendersMain.frame();
            this.fpsMinValid = false;
            this.initFrameTimeList();
            this.bGo = true;
            flag = true;
        } else if (map.containsKey("STOP")) {
            if (this.bGo && (this.timeStart != this.timePrev) && (this.logPeriod == 0L)) {
                this.INFO_HARD("fps:" + this.fpsInfo());
            }
            this.bGo = false;
            flag = true;
        }
        if (map.containsKey("FMINFO")) {
            flag = true;
            if (Mission.isNet() && NetEnv.hosts().size() > 1) {
                System.out.println("Network mission detected, disabling fm debug.");
            } else {
            	readConfig();
                if (map.containsKey("SWITCH")) {
                    if (!Maneuver.showFM)
                        Maneuver.showFM = true;
                    else
                        Maneuver.showFM = false;
                } else if (map.containsKey("DUMP") && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead()) {
                    dumpFlightModel(World.getPlayerAircraft().getClass().getName(), false);
//                    FlightModel flightmodel = World.getPlayerFM();
//                    if (flightmodel != null)
//                        flightmodel.drawData();
                } else if (map.containsKey("DMPALL") && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead()) {
                    //dumpDesc = Reflection.getString(Config.class, "VERSION");
                    dumpStart = 0;
                    if (nargs(map, "DMPALL") > 1) {
                        dumpStart = arg(map, "DMPALL", 1, 0);
                        dumpDesc = arg(map, "DMPALL", 0);
                    } else if (nargs(map, "DMPALL") > 0) {
                    	String arg0 = arg(map, "DMPALL", 0);
                    	try {
                    		int intArg0 = Integer.parseInt(arg0);
                    		dumpStart = intArg0;
                    	} catch (NumberFormatException nfe) {
                    		dumpDesc = arg0;
                    	}
                    }
                    dumpAllFlightModels();
                    RTSConf.setRequestExitApp(true);
               } else if (map.containsKey("DMPLIST") && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead()) {
                    //dumpDesc = Reflection.getString(Config.class, "VERSION");
                    dumpFlightModelsFromList();
                    RTSConf.setRequestExitApp(true);
               } else if (map.containsKey("FMDMP") && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead()) {
//                	iFuelLevel = 100;
                    if (nargs(map, "FMDMP") > 0) {
                        dumpFlightModel(arg(map, "FMDMP", 0), true);
                        RTSConf.setRequestExitApp(true);
                    } else {
                        dumpFlightModel(World.getPlayerAircraft().getClass().getName(), true);
                        RTSConf.setRequestExitApp(true);
                    }
                }
            }
        }
        if (!flag) {
            INFO_HARD("  LOG  " + logPeriod / 1000L);
            if (bShow)
                INFO_HARD("  SHOW");
            else
                INFO_HARD("  HIDE");
            if (bGo) {
                if (timeStart != timePrev)
                    INFO_HARD("  " + fpsInfo());
            } else {
                INFO_HARD("  STOPPED");
            }
        }
        return CmdEnv.RETURN_OK;
    }

    private String fpsInfo() {
        // @formatter:off
        return "" + (int) Math.floor(this.fpsCur)
             + " avg:" + (int) Math.floor((1000D * (this.framePrev - this.frameStart)) / (this.timePrev - this.timeStart))
             + " max:" + (int) Math.floor(this.fpsMax)
             + " min:" + (int) Math.floor(this.fpsMin == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMin)
             + " # RECENT"
             + " avg:" + (int) Math.floor((1000D * (this.framePrev - ((Integer) this.frameList.get(0)).intValue())) / (this.timePrev - ((Long) this.timeList.get(0)).longValue()))
             + " max:" + (int) Math.floor(this.fpsMaxRecent)
             + " min:" + (int) Math.floor(this.fpsMinRecent == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMinRecent)
             + " #"
             + " #fr:" + (this.framePrev - this.frameStart);
     // @formatter:on
    }

    private void checkMsg() {
        if (msg == null) {
            msg = new MsgTimeOut();
            msg.setListener(this);
            msg.setNotCleanAfterSend();
            msg.setFlags(72);
        }
        if (!msg.busy())
            msg.post();
    }

    public CmdFPS() {
        bGo = false;
        bShow = false;
        logPeriod = 5000L;
        logPrintTime = -1L;
        this.logExtPeriod = LOG_EXT_PERIOD_DEFAULT;
        this.missionName = "unknown";
        this.lastFpsMax = 0;
        this.lastFpsMin = 0;
        this.logExt = false;
        this.missionPlaying = false;
        this.recentResetTimeout = RECENT_RESET_DEFAULT;
        this.param.put(LOG, null);
        this.param.put(LOGEXT, null);
        this.param.put(START, null);
        this.param.put(STOP, null);
        this.param.put(SHOW, null);
        this.param.put(HIDE, null);
        this.param.put(PERF, null);
        param.put("FMINFO", null);
        param.put("SWITCH", null);
        param.put("DUMP", null);
        param.put("DMPALL", null);
        param.put("DMPLIST", null);
        param.put("FMDMP", null);
        _properties.put("NAME", "fps");
        _levelAccess = 1;
    }

    private void checkMissionStatus() {
        boolean missionActive = Mission.isPlaying();
        if (this.missionPlaying != missionActive) {
            this.missionPlaying = missionActive;
            if (!missionActive) {
                this.flushFpsLogFile();
                this.missionName = "unknown";
            } else {
                this.frameStart = this.framePrev = RendersMain.frame();
                this.fpsMin = this.fpsMinRecent = FPS_MIN_DEFAULT;
                this.fpsMax = this.fpsCur = this.fpsLast = this.fpsMaxRecent = 0.0D;
                this.timePrev = this.timePrevLogExt = this.logPrintTime = this.timeStart = Time.real();
                this.fpsMinValid = false;
                this.initFrameTimeList();
            }
        }
    }

    private String getMissionName() {
        if (!Mission.isPlaying())
            return "unknown";
        String curMissionName = Mission.cur().name();
        int lastSeparatorPos = Math.max(curMissionName.lastIndexOf("/"), curMissionName.lastIndexOf("\\"));
        if (lastSeparatorPos != -1)
            curMissionName = curMissionName.substring(lastSeparatorPos + 1);
        return curMissionName;
    }

    private void updateFrameTimeList(int i, long l) {
        this.frameList.add(new Integer(i));
        this.timeList.add(new Long(l));
        while (l >= (this.recentResetTimeout + ((Long) this.timeList.get(0)).longValue())) {
            this.timeList.remove(0);
            this.frameList.remove(0);
        }
    }

    private void initFrameTimeList() {
        if (this.frameList == null) {
            this.frameList = new ArrayList();
        }
        if (this.timeList == null) {
            this.timeList = new ArrayList();
        }
        this.frameList.clear();
        this.timeList.clear();
    }

    public static String convertMillisecondsToHMmSs(long milliseconds) {
        long ms = milliseconds % 1000L;
        long s = (milliseconds / 1000L) % 60L;
        long m = (milliseconds / (1000L * 60L)) % 60L;
        long h = (milliseconds / (1000L * 60L * 60L)) % 24L;
        return df2.format(h) + ":" + df2.format(m) + ":" + df2.format(s) + "." + df3.format(ms);
    }

    private static void readConfig() {
    	if (settingsInitialized) return;
    	System.out.println("InfoMod FullAuto 4.10+ reading settings...");
        SectFile codIniSectfile = new SectFile("settings/infomod.ini");
        fuelLevel = codIniSectfile.get("Common", "fuelLevel", -1, -1, 100);
        radiatorSetting = codIniSectfile.get("Common", "radiatorSetting", 100, 0, 100);
        dumpDesc = codIniSectfile.get("Common", "dumpDesc", Reflection.getString(Config.class, "VERSION"));
        if (fuelLevel == -1)
        	System.out.println("Fuel Level = 25%, 50%, 75% and 100%");
        else
        	System.out.println("Fuel Level = " + fuelLevel + "%");
        System.out.println("Radiator Setting = " + radiatorSetting + "%");
        System.out.println("Flight Model Descriptor = " + dumpDesc);
        settingsInitialized = true;
    }

    private static void dumpFlightModelsFromList() {
        System.out.println("*** FM DUMP (LIST) START ! ***");
        fullDump = true;
        int saveMouseMode = RTSConf.cur.getUseMouse();
        RTSConf.cur.setUseMouse(0);
		try {
			BufferedReader listReader = new BufferedReader(new SFSReader("settings/dumplist.ini"));
			for (;;) {
				String listLine = listReader.readLine();
				if (listLine == null) break;
				listLine = listLine.trim();
				if (listLine.startsWith("//") || listLine.startsWith(";") || listLine.startsWith("#")) continue;
				if (listLine.length() == 0) continue;
				Class airClass = Class.forName("com.maddox.il2.objects.air." + listLine);
				//System.out.println("Dumping Flight Model for " + listLine);
				CmdEnv.top().exec("fps FMINFO FMDMP " + airClass.getName());
				System.out.flush();
				String logFileName = RTSConf.cur.console.logFileName();
				Object consoleLogFile = Reflection.getValue(RTSConf.cur.console, "log");
				PrintWriter pw = (PrintWriter) Reflection.getValue(consoleLogFile, "f");
				pw.flush();
				pw.close();
				try {
					pw = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(logFileName, 0), true));
					Reflection.setValue(consoleLogFile, "f", pw);
				} catch (FileNotFoundException fne) {
					fne.printStackTrace();
				}
			}
			listReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		fullDump = false;
		System.out.println("*** FM DUMP (LIST) FINISHED ! ***");
		RTSConf.cur.setUseMouse(saveMouseMode);
		RTSConf.setRequestExitApp(true);
	}

    private static void dumpAllFlightModels() {
        System.out.println("*** FM DUMP START ! ***");
        fullDump = true;
        int saveMouseMode = RTSConf.cur.getUseMouse();
        RTSConf.cur.setUseMouse(0);
        for (int iAirClassIterator = dumpStart; iAirClassIterator < Main.cur().airClasses.size(); iAirClassIterator++) {
            Class airClass = (Class) Main.cur().airClasses.get(iAirClassIterator);
//            System.out.println("" + iAirClassIterator + ". dumpFlightModel " + airClass.getName());
            if (airClass.getName().toLowerCase().indexOf("placeholder") != -1) {
//                System.out.println("...is PlaceHolder, choose next.");
                continue;
            }
            CmdEnv.top().exec("fps FMINFO FMDMP " + airClass.getName());
            System.out.flush();
            String logFileName = RTSConf.cur.console.logFileName();
            Object consoleLogFile = Reflection.getValue(RTSConf.cur.console, "log");
            PrintWriter pw = (PrintWriter)Reflection.getValue(consoleLogFile, "f");
            pw.flush();
            pw.close();
            try {
                pw = new PrintWriter(new FileOutputStream(HomePath.toFileSystemName(logFileName, 0), true));
                Reflection.setValue(consoleLogFile, "f", pw);
            } catch (FileNotFoundException fne) {
                fne.printStackTrace();
            }

        }
        fullDump = false;
        System.out.println("*** FM DUMP FINISHED ! ***");
        RTSConf.cur.setUseMouse(saveMouseMode);
        RTSConf.setRequestExitApp(true);
    }

    private static void dumpFlightModel(String theAircraftClass, boolean reloadAircraft) {
        HUD.training("dumpFlightModel " + theAircraftClass);
        try {
            if (!theAircraftClass.startsWith("com.maddox.il2.objects.air."))
                theAircraftClass = "com.maddox.il2.objects.air." + theAircraftClass;
            Class airClass = Class.forName(theAircraftClass);
            Aircraft theAircraft = World.getPlayerAircraft();
            FlightModel theFlightModel = theAircraft.FM;
            if (reloadAircraft) {
                World.getPlayerAircraft().destroy();
                World.setPlayerAircraft((Aircraft) airClass.newInstance());
                theAircraft = World.getPlayerAircraft();
                String aircraftFM = Property.stringValue(airClass, "FlightModel", null);
                if (aircraftFM != null) {
                    theAircraft.interpEnd("FlightModel");
                    theAircraft.setFM(aircraftFM, 1, true);

                    String weaponSlot = "default";
                    String[] registeredWeapons = Aircraft.getWeaponsRegistered(theAircraft.getClass());
                    boolean slotFound = false;
                    for (int weaponIndex = 0; weaponIndex < registeredWeapons.length; weaponIndex++) {
                        if (registeredWeapons[weaponIndex].equals(weaponSlot)) {
                            slotFound = true;
                            break;
                        }
                    }
                    if (!slotFound) {
                        weaponSlot = registeredWeapons[0];
                        System.out.println("\"default\" weapon slot does not exist on Aircraft " + theAircraft.getClass().getName() + ", using slot \"" + weaponSlot + "\" instead!");
                    }

                    try {
                        Reflection.invokeMethod(theAircraft, "weaponsLoad", new Class[] { String.class }, new Object[] { weaponSlot });
                    } catch (Exception e) {
                        System.out.println("Failed to load Weapon Slot \"" + weaponSlot + "\" for Aircraft " + theAircraft.getClass().getName() + ", FM data might not be accurate!");
                    }
                }

                theFlightModel = theAircraft.FM;
                theFlightModel.M.computeParasiteMass(theFlightModel.CT.Weapons);
                theFlightModel.M.fuel = theFlightModel.M.maxFuel;
                theFlightModel.M.nitro = theFlightModel.M.maxNitro;
                theFlightModel.M.requestNitro(0);
            }

            String airName = getAirNameForClassName(airClass.getName().substring(27));
            String i18Name = I18N.plane(airName);

//            	try {
//            		i18Name = new String(i18Name.getBytes("US-ASCII"));
//        		} catch (UnsupportedEncodingException e) {
//        			e.printStackTrace();
//        		}

            i18Name = Junidecode.unidecode(i18Name);

            i18Name = i18Name.replace('<', '_');
            i18Name = i18Name.replace('>', '_');
            i18Name = i18Name.replace(':', '_');
            i18Name = i18Name.replace('"', '_');
            i18Name = i18Name.replace('/', '_');
            i18Name = i18Name.replace('\\', '_');
            i18Name = i18Name.replace('|', '_');
            i18Name = i18Name.replace('?', '_');
            i18Name = i18Name.replace('*', '_');
            i18Name = i18Name.replace('\t', ' ');

            System.out.print("dumping FM data of " + i18Name + " ... ");
            speedFile = "FlightModels/DataSpeed/" + i18Name;
            turnFile = "FlightModels/DataTurn/" + i18Name;
            craftFile = "FlightModels/DataCraft/" + i18Name;
            weaponFile = "FlightModels/DataWeapon/" + i18Name;
            auxFile = "FlightModels/DataAux/" + i18Name;
            drawData(theFlightModel, fuelLevel, speedFile, turnFile, auxFile);
            drawCraft(theFlightModel, craftFile + " (" + CmdFPS.dumpDesc + ").txt");
            dumpWeaponList(theAircraft, airName, weaponFile + " (" + CmdFPS.dumpDesc + ").txt");
            System.out.println("done.");
        } catch (Exception e) {
            System.out.println("Error in dumpFlightModel(" + theAircraftClass + "):");
            e.printStackTrace();
        }
    }

    public static void drawData(FlightModel fm, int iFuelLevel, String speedFile, String turnFile, String auxFile) {
        Polares Wing = (Polares) Reflection.getValue(fm, "Wing");
        Mass M = fm.M;
        Squares Sq = fm.Sq;
        Wing.G = M.getFullMass() * Atmosphere.g();
        Wing.S = Sq.squareWing;
        EnginesInterface EI = fm.EI;
        Controls CT = fm.CT;

        if (iFuelLevel < 0) {
            for (int i = 0; i <= 75; i += 25) {
//            	System.out.println("drawData " + i);
                Wing.G = (M.getFullMass() - (M.maxFuel * (float) i) / 100F) * Atmosphere.g();
                for (int k = 0; k < 250; k++) {
                    float normP[] = (float[]) Reflection.getValue(Wing, "normP");
                    float maxP[] = (float[]) Reflection.getValue(Wing, "maxP");
                    normP[k] = EI.forcePropAOA(k, 0.0F, 1.0F, false);
                    maxP[k] = EI.forcePropAOA(k, 1000F, 1.1F, true);
//                    System.out.println("normP["+k+"]=" + normP[k] + ", maxP[" + k + "]=" + maxP[k]);
                    Reflection.setValue(Wing, "normP", normP);
                    Reflection.setValue(Wing, "maxP", maxP);
                }

                String s1 = turnFile;
                Wing.setFlaps(0.0F);
                drawGraphs(Wing, s1, i, true);
                Wing.setFlaps(0.0F);

                // TODO: Added by SAS~Storebror, full speed usually requires radiators to be fully open!
                boolean hasRadiatorControl = false;
                for (int engineIndex = 0; engineIndex<fm.EI.getNum(); engineIndex++) {
                	if (fm.EI.engines[engineIndex].isHasControlRadiator()) {
                		hasRadiatorControl = true;
                		break;
                	}
                }
                if (hasRadiatorControl) {
	                float oldCxMin = Reflection.getFloat(Wing, "CxMin");
	                float newCxMin = oldCxMin + (fm.radiatorCX * (float)radiatorSetting / 100F);
	                Reflection.setFloat(Wing, "CxMin", newCxMin);
                }

                s1 = speedFile + " " + (100 - i) + "% fuel" + " (" + CmdFPS.dumpDesc + ")_speed.txt";
                drawSpeed(fm, s1);
                Wing.setFlaps(0.0F);
                String s2 = auxFile + " avail thrust (" + CmdFPS.dumpDesc + ").txt";
                String s3 = auxFile + " thrust summary " + (100 - i) + "% fuel (" + CmdFPS.dumpDesc + ").txt";
                String s4 = auxFile + " req thrust " + (100 - i) + "% fuel (" + CmdFPS.dumpDesc + ").txt";
                if (!fullDump) drawZhukovski(fm, s2, s3, s4);
                Wing.setFlaps(0.0F);
            }
        } else {
            int i = 100 - iFuelLevel;
            if (i < 0)
                i = 0;
            Wing.G = (M.getFullMass() - (M.maxFuel * (float) i) / 100F) * Atmosphere.g();
            for (int k = 0; k < 250; k++) {
                float normP[] = (float[]) Reflection.getValue(Wing, "normP");
                float maxP[] = (float[]) Reflection.getValue(Wing, "maxP");
                normP[k] = EI.forcePropAOA(k, 0.0F, 1.0F, false);
                maxP[k] = EI.forcePropAOA(k, 1000F, 1.1F, true);
                Reflection.setValue(Wing, "normP", normP);
                Reflection.setValue(Wing, "maxP", maxP);
            }

            String s1 = turnFile;
            Wing.setFlaps(0.0F);
            drawGraphs(Wing, s1, i, false);
            Wing.setFlaps(0.0F);

            // TODO: Added by SAS~Storebror, full speed usually requires radiators to be fully open!
            boolean hasRadiatorControl = false;
            for (int engineIndex = 0; engineIndex<fm.EI.getNum(); engineIndex++) {
            	if (fm.EI.engines[engineIndex].isHasControlRadiator()) {
            		hasRadiatorControl = true;
            		break;
            	}
            }
            if (hasRadiatorControl) {
                float oldCxMin = Reflection.getFloat(Wing, "CxMin");
                float newCxMin = oldCxMin + (fm.radiatorCX * (float)radiatorSetting / 100F);
                Reflection.setFloat(Wing, "CxMin", newCxMin);
            }

            s1 = speedFile + " (" + CmdFPS.dumpDesc + ")_speed.txt";
            drawSpeed(fm, s1);
            Wing.setFlaps(0.0F);
            if (!fullDump) {
            String s2 = auxFile + " avail thrust (" + CmdFPS.dumpDesc + ").txt";
            String s3 = auxFile + " thrust summary " + (100 - i) + "% fuel (" + CmdFPS.dumpDesc + ").txt";
            String s4 = auxFile + " req thrust " + (100 - i) + "% fuel (" + CmdFPS.dumpDesc + ").txt";
            drawZhukovski(fm, s2, s3, s4);
            }
            Wing.setFlaps(0.0F);
        }

        if (!fullDump) {

        String s = speedFile + " data (" + CmdFPS.dumpDesc + ").txt";
        PrintWriter printwriter = null;
        try {
            File file = new File(HomePath.toFileSystemName(s, 0));
            file.getParentFile().mkdirs();
            printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
            printwriter.println("***MASS DATA***");
            printwriter.println("Empty: " + M.massEmpty);
            printwriter.println("Takeoff (reference only, not used directly in game): " + M.maxWeight);
            printwriter.println("Max fuel: " + M.maxFuel);
            if (M.maxNitro > 0.0F)
                printwriter.println("Max nitro: " + M.maxNitro);
            printwriter.println("");
            printwriter.println("***POWER PLANT DATA***");
            printwriter.println("Number of engines: " + EI.engines.length);
            for (int j = 0; j < EI.engines.length; j++)
                if (EI.engines[j] != null) {
                    printwriter.println("*Engine " + (j + 1) + " data*");
                    DataToFile(EI.engines[j], printwriter);
                }

            printwriter.println("");
            printwriter.println("***CONTROLS DATA***");
            printwriter.println("Aileron trim: " + (CT.bHasAileronTrim ? "+" : "-"));
            printwriter.println("Elevator trim: " + (CT.bHasElevatorTrim ? "+" : "-"));
            printwriter.println("Rudder trim: " + (CT.bHasRudderTrim ? "+" : "-"));
            printwriter.println("Flap: " + (CT.bHasFlapsControl ? "+" : "-"));
            printwriter.println("Flap 3-positions: " + (CT.bHasFlapsControlRed ? "-" : "+"));
            printwriter.println("Airbrake: " + (CT.bHasAirBrakeControl ? "+" : "-"));
            printwriter.println("Retractable gear: " + (CT.bHasGearControl ? "+" : "-"));
            printwriter.println("Arrestor hook: " + (CT.bHasArrestorControl ? "+" : "-"));
            printwriter.println("Wing fold: " + (CT.bHasWingControl ? "+" : "-"));
            printwriter.println("Cockpit door: " + (CT.bHasCockpitDoorControl ? "+" : "-"));
            printwriter.println("Wheel brakes: " + (CT.bHasBrakeControl ? "+" : "-"));
            printwriter.println("Tail gear lock: " + (CT.bHasLockGearControl ? "+" : "-"));
            printwriter.println("");
            printwriter.println("***COMMON DATA***");
            printwriter.println("Crew: " + fm.crew);
            printwriter.println("HofVmax: " + fm.HofVmax);
            printwriter.println("Vmax0: " + (int) ((double) fm.Vmax * 3.6000000000000001D));
            printwriter.println("VmaxH: " + (int) ((double) fm.VmaxH * 3.6000000000000001D));
            printwriter.println("Vmin: " + (int) ((double) fm.Vmin * 3.6000000000000001D));
            printwriter.println("VminFLAPS_MAXRPM: " + (int) ((double) fm.VminFLAPS * 3.6000000000000001D));
            printwriter.println("VminFLAPS_MINRPM: " + (int) ((double) Wing.V_land * 3.6000000000000001D));
            printwriter.println("VmaxFLAPS: " + (int) ((double) fm.VmaxFLAPS * 3.6000000000000001D));
            printwriter.println("VmaxAllowed: " + (int) ((double) fm.VmaxAllowed * 3.6000000000000001D));
            printwriter.println("V_turn: " + (int) ((double) Wing.V_turn * 3.6000000000000001D));
            printwriter.println("V_climb: " + (int) ((double) Wing.V_climb * 3.6000000000000001D));
            printwriter.println("AOA_crit: " + Wing.AOA_crit);
            printwriter.println("T_turn: " + Wing.T_turn);
            printwriter.println("Vz_climb: " + Wing.Vz_climb);
            printwriter.println("K_max: " + Wing.K_max);
            printwriter.println("Cy0_max: " + Wing.Cy0_max);
            printwriter.println("FlapsMult: " + Wing.FlapsMult);
            printwriter.println("FlapsAngSh: " + Wing.FlapsAngSh);
            printwriter.println("");
            printwriter.println("***AERODYNAMICS DATA***");
            printwriter.println("Flaps-independent data:");
            printwriter.println("Wing square: " + Wing.S);
            printwriter.println("lineCyCoeff: " + Wing.lineCyCoeff);
            printwriter.println("parabAngle: " + Wing.parabAngle);
            printwriter.println("declineCoeff: " + Wing.declineCoeff);
            printwriter.println("maxDistAng: " + Wing.maxDistAng);
            printwriter.println("AOAMinCx_Shift: " + Wing.AOAMinCx_Shift);
            printwriter.println("No Flaps data:");
            printwriter.println("Cy0_0: " + Wing.Cy0_0);
            printwriter.println("AOACritH_0: " + Wing.AOACritH_0);
            printwriter.println("AOACritL_0: " + Wing.AOACritL_0);
            printwriter.println("CyCritH_0: " + Wing.CyCritH_0);
            printwriter.println("CyCritL_0: " + Wing.CyCritL_0);
            printwriter.println("parabCxCoeff_0: " + Wing.parabCxCoeff_0);
            printwriter.println("CxMin_0: " + Wing.CxMin_0);
            printwriter.println("Maximum Flaps data:");
            printwriter.println("Cy0_1: " + Wing.Cy0_1);
            printwriter.println("AOACritH_1: " + Wing.AOACritH_1);
            printwriter.println("AOACritL_1: " + Wing.AOACritL_1);
            printwriter.println("CyCritH_1: " + Wing.CyCritH_1);
            printwriter.println("CyCritL_1: " + Wing.CyCritL_1);
            printwriter.println("parabCxCoeff_1: " + Wing.parabCxCoeff_1);
            printwriter.println("CxMin_1: " + Wing.CxMin_1);
        } catch (IOException ioexception) {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        } finally {
            if (printwriter != null)
                printwriter.close();
        }
        }
        Wing.G = M.maxWeight * Atmosphere.g();
        return;
    }

    private static void drawZhukovski(FlightModel fm, String s, String s1, String s2) {
        Polares Wing = (Polares) Reflection.getValue(fm, "Wing");
        EnginesInterface EI = fm.EI;

        PrintWriter printwriter = null;
        PrintWriter printwriter1 = null;
        PrintWriter printwriter2 = null;
        try {
            File file = new File(HomePath.toFileSystemName(s, 0));
            File file1 = new File(HomePath.toFileSystemName(s1, 0));
            File file2 = new File(HomePath.toFileSystemName(s2, 0));
            file.getParentFile().mkdirs();
            file1.getParentFile().mkdirs();
            file2.getParentFile().mkdirs();
            printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
            printwriter1 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s1, 0))));
            printwriter2 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s2, 0))));
            int i = 0;
            int j = 0;
            int k = 0;
            int l = 0;
            int i1 = 0;
            int j1 = 0;
            float f = 0.0F;
            int k1 = 0;
            int l1 = 0;
            for (int i2 = 0; i2 <= 1500; i2++) {
                printwriter.print("\t" + i2);
                printwriter2.print("\t" + i2);
            }

            printwriter1.println("Height\tMaxThrust\tSpeedMaxThrust\tMaxClimb\tSpeedMaxClimb\tMaxSpeed");

            int topAltitude = 10000;
            for (int engineIndex=0; engineIndex<fm.EI.getNum(); engineIndex++) {
            	Motor m = fm.EI.engines[engineIndex];
            	for (int altitudeIndex=0; altitudeIndex<m.compressorAltitudes.length; altitudeIndex++) {
            		int compressorAltitude = (int)m.compressorAltitudes[altitudeIndex] + 3000;
            		if (compressorAltitude > topAltitude) topAltitude = compressorAltitude;
            	}
            }
            topAltitude = (int)Math.ceil((double)topAltitude / 100D) * 100;

            for (int j2 = 0; j2 <= topAltitude; j2 += 100) {
                int k2 = 0;
                int l2 = 0;
                float f1 = 0.0F;
                int i3 = 0;
                int j3 = 0;
                boolean flag = false;
                printwriter.println();
                printwriter.print(j2);
                printwriter2.println();
                printwriter2.print(j2);
                for (int k3 = 0; k3 <= 1500; k3++) {
                    float f2 = (float) k3 * 0.27778F;
                    int l3 = (int) EI.forcePropAOA(f2, j2, 1.1F, true);
                    if (l3 <= 0)
                        break;
                    printwriter.print("\t" + l3);
                    if (l3 > k2) {
                        k2 = l3;
                        l2 = k3;
                    }
                    int i4 = 0x186a0;
                    if (k3 >= 50) {
                        float f3 = Wing.getClimb(f2, j2, l3);
                        if (f3 > f1) {
                            f1 = f3;
                            i3 = k3;
                        }
                        if (f3 > 0.0F && !flag)
                            flag = true;
                        if (f3 < 0.0F && flag && j3 == 0)
                            j3 = k3;
                        i4 = l3 - (int) ((f3 * Wing.G) / f2);
                    }
                    printwriter2.print("\t" + i4);
                }

                if (k2 > i) {
                    i = k2;
                    j = j2;
                    k = l2;
                }
                if (f1 > f) {
                    f = f1;
                    k1 = i3;
                    l1 = j2;
                }
                if (j3 > i1) {
                    i1 = j3;
                    j1 = j2;
                }
                if (j2 == 0)
                    l = j3;
                printwriter1.println(j2 + "\t" + k2 + "\t" + l2 + "\t" + f1 + "\t" + i3 + "\t" + j3);
            }

            printwriter1.println("\tSummary");
            printwriter1.println("Maximum thrust is " + i + " newtons at " + j + " meters height and " + k + " kph speed.");
            printwriter1.println("Maximum climb is " + f + " mps at " + l1 + " meters height and " + k1 + " kph speed.");
            printwriter1.println("Maximum speed is " + i1 + " kph at " + j1 + " meters height.");
            printwriter1.println("SL speed is " + l + " kph.");
        } catch (IOException ioexception) {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        } finally {
            if (printwriter != null)
                printwriter.close();
            if (printwriter1 != null)
                printwriter1.close();
            if (printwriter2 != null)
                printwriter2.close();
        }
        return;
    }

    public static void drawCraft(FlightModel fm, String s) {
        try {
            File file = new File(HomePath.toFileSystemName(s, 0));
            file.getParentFile().mkdirs();
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
            printwriter.println("0=" + (int) (fm.VmaxAllowed * 3.6F));
            printwriter.close();
        } catch (IOException ioexception) {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
    }

    public static void drawSpeed(FlightModel fm, String s) {
//    	System.out.println("drawSpeed " + s);
        Polares Wing = (Polares) Reflection.getValue(fm, "Wing");
        EnginesInterface EI = fm.EI;
        try {
            File file = new File(HomePath.toFileSystemName(s, 0));
            file.getParentFile().mkdirs();
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));

            int topAltitude = 10000;
            for (int engineIndex=0; engineIndex<fm.EI.getNum(); engineIndex++) {
            	Motor m = fm.EI.engines[engineIndex];
            	for (int altitudeIndex=0; altitudeIndex<m.compressorAltitudes.length; altitudeIndex++) {
            		int compressorAltitude = (int)m.compressorAltitudes[altitudeIndex] + 3000;
            		if (compressorAltitude > topAltitude) topAltitude = compressorAltitude;
            	}
            }
            topAltitude = (int)Math.ceil((double)topAltitude / 100D) * 100;

            for (int i = 0; i <= topAltitude; i += 100) {
                float f = -1000F;
                float f1 = -1000F;
                int j = 50;
                boolean isClimb = false;
                do {
                    if (j >= 1500)
                        break;
                    float f2 = EI.forcePropAOA((float) j / 3.6F, i, 1.1F, true);
                    float f4 = Wing.getClimb((float) j / 3.6F, i, f2);
                    //System.out.println("i=" + i + ", j=" + j + ", f2=" + f2 + ", f4=" + f4 + ", f=" + f + ", f1=" + f1);
                    if (f4 > f)
                        f = f4;
                    if (f4 > 0F) isClimb = true;
//                    if (f4 < 0.0F && f4 < f) {
                    if (f4 < 0.0F && isClimb) {
                        f1 = j;
                        break;
                    }
                    j++;
                } while (true);
                f1 *= 0.982F; // TODO: Added by SAS~Storebror, for some reason IL-2 Compare tends to give slightly unachievable speed values otherwise
                if (f1 < 0.0F)
                	break;
//                    printwriter.print("\t");
//                else
//                    printwriter.print(f1 + "\t");

                printwriter.print(i + "\t");
                printwriter.print(f1 + "\t");

                printwriter.print(f * Wing.Vyfac + "\t");
                f = -1000F;
                f1 = -1000F;
                j = 50;
                do {
                    if (j >= 1500)
                        break;
                    float f3 = EI.forcePropAOA((float) j / 3.6F, i, 1.0F, false);
                    float f5 = Wing.getClimb((float) j / 3.6F, i, f3);
                    if (f5 > f)
                        f = f5;
                    if (f5 < 0.0F && f5 < f) {
                        f1 = j;
                        break;
                    }
                    j++;
                } while (true);
                f1 *= 0.982F; // TODO: Added by SAS~Storebror, for some reason IL-2 Compare tends to give slightly unachievable speed values otherwise
                if (f1 < 0.0F)
                    printwriter.print("\t");
                else
                    printwriter.print(f1 + "\t");
                printwriter.print(f * Wing.Vyfac + "\t");
                printwriter.println();
            }

            printwriter.close();
        } catch (IOException ioexception) {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
    }

    public static void DataToFile(Motor m, PrintWriter printwriter) {
        if (printwriter == null)
            return;
        printwriter.print("Rotate direction: ");
        if (Reflection.getInt(m, "propDirection") == 0)
            printwriter.println("left");
        else if (1 == Reflection.getInt(m, "propDirection"))
            printwriter.println("right");
        if (Reflection.getInt(m, "type") == 0 || Reflection.getInt(m, "type") == 1 || Reflection.getInt(m, "type") == 7) {
            printwriter.println("Horse powers: " + Reflection.getFloat(m, "horsePowers"));
            printwriter.println("Engine mass: " + Reflection.getFloat(m, "engineMass"));
        } else {
            printwriter.println("Thrust: " + Reflection.getFloat(m, "thrustMax"));
        }
        printwriter.println("Boost factor: " + Reflection.getFloat(m, "engineBoostFactor"));
        printwriter.println("WEP boost factor: " + Reflection.getFloat(m, "engineAfterburnerBoostFactor"));
        printwriter.println("Throttle control: " + (Reflection.getBoolean(m, "bHasThrottleControl") ? "+" : "-"));
        printwriter.println("Afterburner control: " + (Reflection.getBoolean(m, "bHasAfterburnerControl") ? "+" : "-"));
        printwriter.println("Propeller control: " + (Reflection.getBoolean(m, "bHasPropControl") ? "+" : "-"));
        printwriter.println("Mixture control: " + (Reflection.getBoolean(m, "bHasMixControl") ? "+" : "-"));
        printwriter.println("Magneto control: " + (Reflection.getBoolean(m, "bHasMagnetoControl") ? "+" : "-"));
        printwriter.println("Compressor control: " + (Reflection.getBoolean(m, "bHasCompressorControl") ? "+" : "-"));
        printwriter.println("Feather control: " + (Reflection.getBoolean(m, "bHasFeatherControl") ? "+" : "-"));
        printwriter.println("Radiator control: " + (Reflection.getBoolean(m, "bHasRadiatorControl") ? "+" : "-"));
        printwriter.println("Extinguishers control: " + (Reflection.getBoolean(m, "bHasExtinguisherControl") ? "+" : "-"));
        printwriter.println("Compressor steps: " + (m.compressorMaxStep + 1));
        for (int i = 0; i <= m.compressorMaxStep; i++)
            printwriter.println("Compressor altitude: " + m.compressorAltitudes[i]);

        printwriter.println("Autopropeller : " + (m.isAllowsAutoProp() ? "+" : "-"));
        printwriter.println("Autoradiator : " + (m.isAllowsAutoRadiator() ? "+" : "-"));
    }

    public static void drawGraphs(Polares p, String s, int fuel, boolean bDrawFuel) {
        float f = -10000F;
        if (!fullDump) {
        try {
            File file = new File(HomePath.toFileSystemName(s + " Polares (" + CmdFPS.dumpDesc + ").txt", 0));
            file.getParentFile().mkdirs();
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s + " Polares (" + CmdFPS.dumpDesc + ").txt", 0))));
            for (int i = -90; i < 90; i++)
                printwriter.print(i + "\t");

            printwriter.println();
            for (int i2 = 0; i2 <= 5; i2++) {
                p.setFlaps((float) i2 * 0.2F);
                for (int j = -90; j < 90; j++)
                    printwriter.print(p.new_Cya(j) + "\t");

                printwriter.println();
                for (int k = -90; k < 90; k++)
                    printwriter.print(p.new_Cxa(k) + "\t");

                printwriter.println();
                if (i2 == 0) {
                    for (int l = -90; l < 90; l++) {
                        float f9 = p.new_Cya(l) / p.new_Cxa(l);
                        printwriter.print(f9 * 0.1F + "\t");
                        if (f < f9)
                            f = f9;
                    }

                    printwriter.println();
                }
                for (int i1 = -90; i1 < 90; i1++) {
                    float f10 = Reflection.getFloat(p, "Cy0") + p.lineCyCoeff * (float) i1;
                    if ((double) f10 < 2D && (double) f10 > -2D)
                        printwriter.print(f10 + "\t");
                    else
                        printwriter.print("\t");
                }

                printwriter.println();
            }

            printwriter.close();
        } catch (IOException ioexception) {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
        }
        try {
            PrintWriter printwriter1;
            if (bDrawFuel) {
                File file = new File(HomePath.toFileSystemName(s + " " + (100 - fuel) + "% fuel" + " (" + CmdFPS.dumpDesc + ")_turn.txt", 0));
                file.getParentFile().mkdirs();
                printwriter1 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s + " " + (100 - fuel) + "% fuel" + " (" + CmdFPS.dumpDesc + ")_turn.txt", 0))));
            }
            else {
                File file = new File(HomePath.toFileSystemName(s + " (" + CmdFPS.dumpDesc + ")_turn.txt", 0));
                file.getParentFile().mkdirs();
                printwriter1 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s + " (" + CmdFPS.dumpDesc + ")_turn.txt", 0))));
            }
            for (int j1 = 120; j1 < 620; j1 += 2)
                printwriter1.print(j1 + "\t");

            printwriter1.println();
            float f2 = -10000F;
            float f4 = 300F;
            float f6 = 10000F;
            float f8 = 300F;
            for (int j2 = 0; j2 <= 3; j2++) {
                switch (j2) {
                    case 0:
                        p.setFlaps(0.0F);
                        break;

                    case 1:
                        p.setFlaps(0.2F);
                        break;

                    case 2:
                        p.setFlaps(0.33F);
                        break;

                    case 3:
                        p.setFlaps(1.0F);
                        break;
                }
                if (j2 == 0) {
                    for (int k1 = 120; k1 < 620; k1 += 2) {
                        float f11 = (float) k1 * 0.27778F;
                        float f13 = p.S * Reflection.getFloat(p, "Ro") * f11 * f11;
                        float f15 = (2.0F * p.G) / f13;
                        float f17 = p.getAoAbyCy(f15);
                        float f19 = f17 - p.AOAMinCx;
                        float f21 = 0.5F * (Reflection.getFloat(p, "CxMin") + Reflection.getFloat(p, "parabCxCoeff") * f19 * f19) * f13;
                        float normP[] = (float[]) Reflection.getValue(p, "normP");
                        float f23 = (f11 * (normP[(int) f11] - f21)) / p.G;
                        if (j2 == 0 && f2 < f23) {
                            f2 = f23;
                            f4 = f11;
                        }
                        if (f23 < -10F)
                            printwriter1.print("\t");
                        else
                            printwriter1.print(f23 * p.Vyfac + "\t");
                    }

                    printwriter1.println();
                }
                for (int l1 = 120; l1 < 620; l1 += 2) {
                    float f12 = (float) l1 * 0.27778F;
                    float f14 = p.S * Reflection.getFloat(p, "R1000") * f12 * f12;
                    float maxP[] = (float[]) Reflection.getValue(p, "maxP");
                    float f16 = (2.0F * maxP[(int) f12]) / f14;
                    float f18 = (float) Math.sqrt((f16 - Reflection.getFloat(p, "CxMin")) / Reflection.getFloat(p, "parabCxCoeff"));
                    float f20 = p.AOAMinCx + f18;
                    if (f20 > p.AOACritH)
                        f20 = p.AOACritH;
                    float f22 = (0.5F * p.new_Cya(f20) * f14) / p.G;
                    float f24 = (float) Math.sqrt(f22 * f22 - 1.0F);
                    float f25 = 0.0F;
                    if (f24 > 0.2F)
                        f25 = (6.283185F * f12) / (9.8F * f24);
                    if (f25 > 40F)
                        f25 = 0.0F;
                    if (f25 == 0.0F)
                        printwriter1.print("\t");
                    else
                        printwriter1.print(f25 * p.Tfac + "\t");
                    if (j2 == 0 && f25 > 0.0F && f6 > f25) {
                        f6 = f25;
                        f8 = f12;
                    }
                }

                printwriter1.println();
            }

            printwriter1.println("M_takeoff:\t" + p.G / 9.8F);
            printwriter1.println("K_max:\t" + f);
            printwriter1.println("T_turn:\t" + f6 * p.Tfac);
            printwriter1.println("V_turn:\t" + f8 * 3.6F);
            printwriter1.println("Vz_climb:\t" + f2 * p.Vyfac);
            printwriter1.println("V_climb:\t" + f4 * 3.6F);
            printwriter1.println("CxMin_LandFlaps:\t" + Reflection.getFloat(p, "CxMin"));
            p.setFlaps(0.33F);
            printwriter1.println("CxMin_TOFlaps:\t" + Reflection.getFloat(p, "CxMin"));
            p.setFlaps(0.2F);
            printwriter1.println("CxMin_CombatFlaps:\t" + Reflection.getFloat(p, "CxMin"));
            p.setFlaps(0.0F);
            printwriter1.println("CxMin_NoFlaps:\t" + Reflection.getFloat(p, "CxMin"));
            printwriter1.close();
        } catch (IOException ioexception1) {
            System.out.println("File save failed: " + ioexception1.getMessage());
            ioexception1.printStackTrace();
        }
    }

    private static void dumpWeaponList(Aircraft theAircraft, String aircraftName, String weaponFile) {
        try {
            File file = new File(HomePath.toFileSystemName(weaponFile, 0));
            file.getParentFile().mkdirs();
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(weaponFile, 0))));
            Class theAircraftClass = theAircraft.getClass();
            ArrayList weaponSlotNames = (ArrayList) Property.value(theAircraftClass, "weaponsList");
           // int weaponTriggers[] = Aircraft.getWeaponTriggersRegistered(theAircraftClass);
            for (int i = 0; i < weaponSlotNames.size(); i++) {
                String weaponSlotName = (String) weaponSlotNames.get(i);
                String weaponSlotClearName = I18N.weapons(aircraftName, weaponSlotName);
                weaponSlotClearName = weaponSlotClearName.replace(':', '·');
                weaponSlotClearName = weaponSlotClearName.replace('=', '·');
                weaponSlotClearName = weaponSlotClearName.replace(';', '·');
                ArrayList weaponSlotList = new ArrayList();
                Aircraft._WeaponSlot weaponSlot[] = Aircraft.getWeaponSlotsRegistered(theAircraftClass, weaponSlotName);
                for (int j = 0; j < weaponSlot.length; j++) {
                    int type = 0;
                    if (weaponSlot[j] == null)
                        continue;
                    //if (weaponTriggers.length <= j) continue;
                    //switch (weaponTriggers[j]) {
                    switch (weaponSlot[j].trigger) {
                        case 2:
                            type = WeaponSlot.TYPE_ROCKET;
                            break;
                        case 3:
                            type = WeaponSlot.TYPE_BOMB;
                            break;
                        case 9:
                            type = WeaponSlot.TYPE_TANK;
                            break;
                        default:
                            //if (weaponTriggers[j] < 2) {
                            if (weaponSlot[j].trigger < 2) {
                                type = WeaponSlot.TYPE_GUN;
                            //} else if (weaponTriggers[j] < 10) {
                            } else if (weaponSlot[j].trigger < 10) {
                                type = WeaponSlot.TYPE_ROCKET;
                            } else {
                                type = WeaponSlot.TYPE_GUN_GUNNER;
                            }
                            break;
                    }
                    Class weaponClass = weaponSlot[j].clazz;
                    Class tmpClass = null;
                    try {
                        tmpClass = Class.forName("com.maddox.il2.objects.weapons.BombGunNull");
                        if (weaponClass.equals(tmpClass)) continue;
                    } catch (Exception e) {}
                    try {
                        tmpClass = Class.forName("com.maddox.il2.objects.weapons.GunNull");
                        if (weaponClass.equals(tmpClass)) continue;
                    } catch (Exception e) {}
                    try {
                        tmpClass = Class.forName("com.maddox.il2.objects.weapons.MGunNull");
                        if (weaponClass.equals(tmpClass)) continue;
                    } catch (Exception e) {}
                    try {
                        tmpClass = Class.forName("com.maddox.il2.objects.weapons.MGunNullGeneric");
                        if (weaponClass.equals(tmpClass)) continue;
                    } catch (Exception e) {}
                    try {
                        tmpClass = Class.forName("com.maddox.il2.objects.weapons.RocketGunNull");
                        if (weaponClass.equals(tmpClass)) continue;
                    } catch (Exception e) {}
                    int num = 1;
                    int bullets = weaponSlot[j].bullets;
                    if (BombGun.class.isAssignableFrom(weaponClass) || RocketGun.class.isAssignableFrom(weaponClass) || RocketBombGun.class.isAssignableFrom(weaponClass) || FuelTankGun.class.isAssignableFrom(weaponClass)) {
                        bullets *= Property.intValue(weaponClass, "bullets", 1);
                        weaponClass = (Class) Property.value(weaponClass, "bulletClass");
                        num = bullets;
                    } else if (type == WeaponSlot.TYPE_TANK) {
                        continue;
                    }
                    try {
                        tmpClass = Class.forName("com.maddox.il2.objects.weapons.RocketNull");
                        if (weaponClass.equals(tmpClass)) continue;
                    } catch (Exception e) {}
                    String weaponName = weaponClass.getName();
                    weaponName = stripLeading(weaponName, "com.maddox.il2.objects.weapons.");
                    if (weaponName.startsWith("BombTorp")) {
                        weaponName = stripLeading(weaponName, "BombTorp");
                        weaponName = translateBombTorp(weaponName);
                    }
                    if (weaponName.startsWith("Bomb")) {
                        weaponName = stripLeading(weaponName, "Bomb");
                        weaponName = translateBomb(weaponName);
                    }
                    weaponName = stripLeading(weaponName, "Cannon");
                    if (weaponName.startsWith("FuelTank_")) {
                        weaponName = stripLeading(weaponName, "FuelTank_");
                        weaponName = translateFuelTank(weaponName);
                    }
                    weaponName = stripLeading(weaponName, "MMGun");
                    if (weaponName.startsWith("MGun")) {
                        weaponName = stripLeading(weaponName, "MGun");
                        weaponName = translateMGun(weaponName);
                    }
                    weaponName = stripLeading(weaponName, "MachineGun");
                    weaponName = stripLeading(weaponName, "Missile");
                    weaponName = stripLeading(weaponName, "Pylon");
                    if (weaponName.startsWith("Rocket")) {
                        weaponName = stripLeading(weaponName, "Rocket");
                        weaponName = translateRocket(weaponName);
                    }
                    weaponName = stripLeading(weaponName, "Torpedo");
                    addWeaponSlot(weaponName, num, bullets, type, weaponSlotList);
                }

                boolean firstSlotPrinted = false;
                printwriter.print(weaponSlotClearName + ":");

                for (int j = 0; j <= WeaponSlot.TYPE_MAX; j++) {
                    for (int k = 0; k < weaponSlotList.size(); k++) {
                        WeaponSlot ws = (WeaponSlot) weaponSlotList.get(k);
                        if (ws.type != j)
                            continue;
                        if (firstSlotPrinted) {
                            switch (ws.getType()) {
                                case WeaponSlot.TYPE_ROCKET:
                                    printwriter.print("rocket:");
                                    break;
                                case WeaponSlot.TYPE_BOMB:
                                    printwriter.print("bomb:");
                                    break;
                                case WeaponSlot.TYPE_GUN_GUNNER:
                                    printwriter.print("gunner:");
                                    break;
                                case WeaponSlot.TYPE_TANK:
                                    printwriter.print("fueltank:");
                                    break;
                                case WeaponSlot.TYPE_GUN:
                                default:
                                    printwriter.print("gun:");
                                    break;
                            }
                        }
                        if (ws.getNum() > 1) {
                            printwriter.print(ws.getNum() + "x ");
                        }
                        printwriter.print(ws.getName());
                        if (ws.getBullets() > ws.getNum()) {
                            printwriter.print(" (" + ws.getBullets() + " shots)");
                        }
                        if (ws.getType() == WeaponSlot.TYPE_GUN_GUNNER) {
                            printwriter.print(" (gunner)");
                        }
                        printwriter.print(";");
                        firstSlotPrinted = true;
                    }
                }
                if (!firstSlotPrinted)
                    printwriter.print("nothing;");
                printwriter.println();
            }
            printwriter.close();
        } catch (Exception e) {
            System.out.println("dumpWeaponList failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addWeaponSlot(String name, int num, int bullets, int type, ArrayList weaponSlotList) {
        for (int i = 0; i < weaponSlotList.size(); i++) {
            WeaponSlot weaponSlot = (WeaponSlot) weaponSlotList.get(i);
            if (weaponSlot.getName().equalsIgnoreCase(name)) {
                weaponSlot.setNum(weaponSlot.getNum() + num);
                weaponSlot.setBullets(weaponSlot.getBullets() + bullets);
                return;
            }
        }
        WeaponSlot weaponSlot = new WeaponSlot();
        weaponSlot.setName(name);
        weaponSlot.setNum(num);
        weaponSlot.setBullets(bullets);
        weaponSlot.setType(type);
        weaponSlotList.add(weaponSlot);
    }

    private static String stripLeading(String fullString, String leadingString) {
        if (fullString.startsWith(leadingString))
            return fullString.substring(leadingString.length());
        return fullString;
    }

    private static String getAirNameForClassName(String theClassName) {
        String sRetVal = theClassName;
        SectFile sectfile = new SectFile("com/maddox/il2/objects/air.ini");
        boolean bFound = false;
        for (int i = 0; i < sectfile.sections(); i++) {
            for (int j = 0; j < sectfile.vars(i); j++) {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(i, j));
                String s = numbertokenizer.next((String) null);
                if (s.substring(4).equalsIgnoreCase(theClassName)) {
                    sRetVal = sectfile.var(i, j);
                    bFound = true;
                    break;
                }
            }
            if (bFound)
                break;
        }

        return sRetVal;
    }

    private static String translateMGun(String weaponName) {
        if (weaponName.startsWith("ADEN30"))
            return "30mm Aden Cannon";
        if (weaponName.startsWith("B20"))
            return "20mm Berezin B-20";
        if (weaponName.startsWith("BK37"))
            return "BK 3.7 Cannon";
        if (weaponName.startsWith("BK75"))
            return "BK 7.5 Cannon";
        if (weaponName.startsWith("Bofors40"))
            return "4cm Bofors Cannon";
        if (weaponName.startsWith("BredaSAFAT127"))
            return "12.7mm Breda-SAFAT Heavy Gun";
        if (weaponName.startsWith("BredaSAFAT77"))
            return "7.7mm Breda-SAFAT Gun";
        if (weaponName.startsWith("BredaSAFATSM127"))
            return "12.7mm Breda-SAFAT Heavy Gun";
        if (weaponName.startsWith("BredaSAFATSM77"))
            return "7.7mm Breda-SAFAT Gun";
        if (weaponName.startsWith("Browning303"))
            return "Browning .303";
        if (weaponName.startsWith("Browning50"))
            return "Browning .50";
        if (weaponName.startsWith("ColtMk12"))
            return "20mm Colt-Browning MK 12 Cannon";
        if (weaponName.startsWith("DA762"))
            return "DA 7.62mm Gun";
        if (weaponName.startsWith("HispanoMkI"))
            return "20mm Hispano Mk. I Cannon";
        if (weaponName.startsWith("Ho103"))
            return "12.7mm Ho-103 Heavy Gun";
        if (weaponName.startsWith("Ho115"))
            return "30mm Ho-155 Cannon";
        if (weaponName.startsWith("Ho5"))
            return "20mm Ho-5 Cannon";
        if (weaponName.startsWith("M4_75"))
            return "75mm M4 Cannon";
        if (weaponName.startsWith("M4"))
            return "37mm M4 Cannon";
        if (weaponName.startsWith("M9"))
            return "37mm M9 Cannon";
        if (weaponName.startsWith("MAC1934"))
            return "7.5mm MAC 1934 Gun";
        if (weaponName.startsWith("MG131"))
            return "13mm MG 131 Heavy Gun";
        if (weaponName.startsWith("MG15120"))
            return "20mm MG 151 Cannon";
        if (weaponName.startsWith("MG151"))
            return "15mm MG 151 Heavy Gun";
        if (weaponName.startsWith("MG15"))
            return "7.92mm MG 15 Gun";
        if (weaponName.startsWith("MG17"))
            return "7.92mm MG 17 Gun";
        if (weaponName.startsWith("MG213"))
            return "20mm MG 213 Cannon";
        if (weaponName.startsWith("MG81"))
            return "7.92mm MG 81 Gun";
        if (weaponName.startsWith("MGFF"))
            return "20mm MG-FF Cannon";
        if (weaponName.startsWith("MK101"))
            return "30mm MK 101 Cannon";
        if (weaponName.startsWith("MK103"))
            return "30mm MK 103 Cannon";
        if (weaponName.startsWith("MK108"))
            return "30mm MK 108 Cannon";
        if (weaponName.startsWith("MK213"))
            return "30mm MK 213 Cannon";
        if (weaponName.startsWith("MK214"))
            return "50mm MK 214 Cannon";
        if (weaponName.startsWith("Madsen20"))
            return "20mm Madsen Cannon";
        if (weaponName.startsWith("Mauser213"))
            return "20mm MG 213 Cannon";
        if (weaponName.startsWith("MiniGun"))
            return "7.62mm M134 Minigun";
        if (weaponName.startsWith("Molins_57"))
            return "57mm Molins Cannon";
        if (weaponName.startsWith("N37"))
            return "37mm Nudelman Cannon";
        if (weaponName.startsWith("N57"))
            return "57mm Nudelman Cannon";
        if (weaponName.startsWith("NR23"))
            return "23mm Nudelman-Richter Cannon";
        if (weaponName.startsWith("NS23"))
            return "23mm Nudelman Cannon";
        if (weaponName.startsWith("NS37"))
            return "37mm Nudelman Cannon";
        if (weaponName.startsWith("NS45"))
            return "45mm Nudelman Cannon";
        if (weaponName.startsWith("PV1"))
            return "7.62mm PV-1 Gun";
        if (weaponName.startsWith("PaK40"))
            return "75mm PaK-40 Cannon";
        if (weaponName.startsWith("Scotti127"))
            return "12.7 mm Isotta-Fraschini Scotti Gun";
        if (weaponName.startsWith("Sh37"))
            return "37mm Ho-203 Cannon";
        if (weaponName.startsWith("ShKAS"))
            return "7.62mm ShKAS Gun";
        if (weaponName.startsWith("ShVAK"))
            return "20mm ShVAK Cannon";
        if (weaponName.startsWith("Type2"))
            return "13mm Type 2 Heavy Gun";
        if (weaponName.startsWith("Type5"))
            return "30mm Type 5 Cannon";
        if (weaponName.startsWith("Type99"))
            return "20mm Type 99 Cannon";
        if (weaponName.startsWith("UB"))
            return "12.7mm UB Heavy Gun";
        if (weaponName.startsWith("VYa"))
            return "23mm VYa Cannon";
        if (weaponName.startsWith("Vickers40mm"))
            return "40mm Vickers Cannon";
        if (weaponName.startsWith("VikkersK"))
            return "Vikkers .303";
        if (weaponName.startsWith("Vz30"))
            return "7.92mm vz.30 Gun";
        return weaponName;
    }

    private static String translateBombTorp(String weaponName) {
        if (weaponName.startsWith("45_36"))
            return "935kg 46-36 Torpedo";
        if (weaponName.startsWith("Torp650"))
            return "4.5ton Type 65 Torpedo";
        if (weaponName.startsWith("F5B"))
            return "765kg F5b Torpedo";
        if (weaponName.startsWith("FFF"))
            return "350kg Motobomba FFF Torpedo";
        if (weaponName.startsWith("Fiume"))
            return "1700kg Fiume Torpedo";
        if (weaponName.startsWith("LTF5Practice"))
            return "765kg F5b Practice Torpedo";
        if (weaponName.startsWith("Mk12"))
            return "702kg Mk. XII Torpedo";
        if (weaponName.startsWith("Mk13"))
            return "1005kg Mk. XIII Torpedo";
        if (weaponName.startsWith("Mk34"))
            return "522kg Mk. 34 Torpedo";
        if (weaponName.startsWith("Type91"))
            return "848kg Type 91 Torpedo";
        return weaponName;
    }

    private static String translateBomb(String weaponName) {
        if (weaponName.startsWith("1000lbs"))
            return "1000lbs Bomb";
        if (weaponName.startsWith("100Lbs"))
            return "100lbs Bomb";
        if (weaponName.startsWith("100kg"))
            return "100kg Bomb";
        if (weaponName.startsWith("10kg"))
            return "10kg Bomb";
        if (weaponName.startsWith("110GalNapalm"))
            return "110gal Napalm Canister";
        if (weaponName.startsWith("12000Tallboy"))
            return "12tons Tallboy Bomb";
        if (weaponName.startsWith("154Napalm"))
            return "154gal Napalm Canister";
        if (weaponName.startsWith("15kg"))
            return "15kg Bomb";
        if (weaponName.startsWith("1600lbs"))
            return "1600lbs Bomb";
        if (weaponName.startsWith("175Napalm"))
            return "175gal Napalm Canister";
        if (weaponName.startsWith("2000lbs"))
            return "2000lbs Bomb";
        if (weaponName.startsWith("20kg"))
            return "20kg Bomb";
        if (weaponName.startsWith("250kg"))
            return "250kg Bomb";
        if (weaponName.startsWith("250lbs"))
            return "250lbs Bomb";
        if (weaponName.startsWith("25kg"))
            return "25kg Bomb";
        if (weaponName.startsWith("300lbs"))
            return "300lbs Bomb";
        if (weaponName.startsWith("30kg"))
            return "30kg";
        if (weaponName.startsWith("4512"))
            return "935kg 45-12 Torpedo";
        if (weaponName.startsWith("500kg"))
            return "500kg Bomb";
        if (weaponName.startsWith("500lbs"))
            return "500lbs Bomb";
        if (weaponName.startsWith("50kg"))
            return "50kg Bomb";
        if (weaponName.startsWith("5327"))
            return "1710kg 53-27 Torpedo";
        if (weaponName.startsWith("5339"))
            return "1780kg 53-39 Torpedo";
        if (weaponName.startsWith("600kg"))
            return "600kg Bomb";
        if (weaponName.startsWith("60kg"))
            return "60kg Bomb";
        if (weaponName.startsWith("750lbs"))
            return "750lbs Bomb";
        if (weaponName.startsWith("75GalNapalm"))
            return "75gal Napalm Canister";
        if (weaponName.startsWith("75Napalm"))
            return "75gal Napalm Canister";
        if (weaponName.startsWith("800kg"))
            return "800kg Bomb";
        if (weaponName.startsWith("AB1000"))
            return "1000kg Bomb Dispenser";
        if (weaponName.startsWith("AB23"))
            return "23kg Bomb Dispenser";
        if (weaponName.startsWith("AB250"))
            return "250kg Bomb Dispenser";
        if (weaponName.startsWith("AB500"))
            return "500kg Bomb Dispenser";
        if (weaponName.startsWith("AO10"))
            return "500kg Bomb Dispenser";
        if (weaponName.startsWith("Ampoule"))
            return "1.2kg Ampoule";
        if (weaponName.startsWith("B22"))
            return "2.2kg Bomblet";
        if (weaponName.startsWith("Blu2Napalm"))
            return "100gal Napalm Canister";
        if (weaponName.startsWith("CBU24"))
            return "800lbs Cluster Bomb";
        if (weaponName.startsWith("CargoA"))
            return "Cargo Container";
        if (weaponName.startsWith("FAB1000"))
            return "1000kg Bomb";
        if (weaponName.startsWith("FAB100"))
            return "100kg Bomb";
        if (weaponName.startsWith("FAB2000"))
            return "2000kg Bomb";
        if (weaponName.startsWith("FAB250"))
            return "250kg Bomb";
        if (weaponName.startsWith("FAB5000"))
            return "5000kg Bomb";
        if (weaponName.startsWith("FAB500"))
            return "500kg Bomb";
        if (weaponName.startsWith("FAB50"))
            return "50kg Bomb";
        if (weaponName.startsWith("FatMan"))
            return "10300lbs 21kt Fat Man Nuke";
        if (weaponName.startsWith("HC4000"))
            return "4000lbs High Capacity Bomb";
        if (weaponName.startsWith("IT_100"))
            return "100kg Bomb";
        if (weaponName.startsWith("IT_250"))
            return "250kg Bomb";
        if (weaponName.startsWith("IT_500"))
            return "500kg Bomb";
        if (weaponName.startsWith("IT_50"))
            return "50kg Bomb";
        if (weaponName.startsWith("IT_630"))
            return "630kg Bomb";
        if (weaponName.startsWith("LittleBoy"))
            return "9700lbs 15kt Little Boy Buke";
        if (weaponName.startsWith("Mk12"))
            return "1200lbs 14kt Nuke";
        if (weaponName.startsWith("Mk24Flare"))
            return "27lbs Flare";
        if (weaponName.startsWith("Mk53Charge"))
            return "325lbs Depth Charge";
        if (weaponName.startsWith("Mk7"))
            return "1680lbs 61kt Nuke";
        if (weaponName.startsWith("Mk81"))
            return "250lb Retarding Bomb";
        if (weaponName.startsWith("Mk82"))
            return "500lb Retarding Bomb";
        if (weaponName.startsWith("Mk83"))
            return "1000lb Retarding Bomb";
        if (weaponName.startsWith("PC1600"))
            return "1600kg Anti-Armor Bomb";
        if (weaponName.startsWith("PTAB25"))
            return "120kg Anti-Tank Cassette";
        if (weaponName.startsWith("ParaFlare"))
            return "8kg Flare on Parachute";
        if (weaponName.startsWith("Parafrag8"))
            return "10kg Anti-Personell Fragmentation Bomb on Parachute";
        if (weaponName.startsWith("PhBall"))
            return "0.5kg Phosphorous Ball";
        if (weaponName.startsWith("PuW100"))
            return "100kg Bomb";
        if (weaponName.startsWith("PuW125"))
            return "125kg Bomb";
        if (weaponName.startsWith("PuW300"))
            return "300kg Bomb";
        if (weaponName.startsWith("PuW50"))
            return "50kg Bomb";
        if (weaponName.startsWith("SAB100"))
            return "100kg Flare Bomb";
        if (weaponName.startsWith("SAB15"))
            return "15kg Flare Bomb";
        if (weaponName.startsWith("SB1000"))
            return "1000kg High Capacity Bomb";
        if (weaponName.startsWith("SC1000"))
            return "1000kg Bomb";
        if (weaponName.startsWith("SC1800"))
            return "1800kg Bomb";
        if (weaponName.startsWith("SC2000"))
            return "2000kg Bomb";
        if (weaponName.startsWith("SC2500"))
            return "2500kg Bomb";
        if (weaponName.startsWith("SC250"))
            return "250kg Bomb";
        if (weaponName.startsWith("SC500"))
            return "500kg Bomb";
        if (weaponName.startsWith("SC50"))
            return "50kg Bomb";
        if (weaponName.startsWith("SC70"))
            return "70kg Bomb";
        if (weaponName.startsWith("SD1000"))
            return "1000kg Semi-Armor-Piercing Bomb";
        if (weaponName.startsWith("SD2A"))
            return "2kg Bomblet";
        if (weaponName.startsWith("SD4HL"))
            return "4kg Bomblet";
        if (weaponName.startsWith("SD500"))
            return "500kg Semi-Armor-Piercing Bomb";
        if (weaponName.startsWith("Spezzone"))
            return "1kg Incendiary Bomb";
        if (weaponName.startsWith("Starthilfe"))
            return "RATO Unit";
        if (weaponName.startsWith("TI"))
            return "Target Indicator Container";
        if (weaponName.startsWith("Ti"))
            return "Target Indicator";
        if (weaponName.startsWith("Type3AntiAir"))
            return "20lb Anti-Aircraft Rocket";
        if (weaponName.startsWith("WalterStarthilferakete"))
            return "RATO Unit";
        if (weaponName.startsWith("let2Kg"))
            return "2kg Bomblet";
        return weaponName;
    }

    private static String translateRocket(String weaponName) {
        if (weaponName.startsWith("132"))
            return "50lbs RS-132 Rocket";
        if (weaponName.startsWith("4andHalfInch"))
            return "4.5'' M8 Rocket";
        if (weaponName.startsWith("5inchZuni"))
            return "5'' Zuni Rocket";
        if (weaponName.startsWith("82"))
            return "80lbs Rocket";
        if (weaponName.startsWith("90"))
            return "90lbs Rocket";
        if (weaponName.startsWith("BRS132"))
            return "50lbs BRS-132 Armor Piercing Rocket";
        if (weaponName.startsWith("BRS82"))
            return "15lbs BRS-82 Armor Piercing Rocket";
        if (weaponName.startsWith("Bat"))
            return "1600lb Bat Bomb";
        if (weaponName.startsWith("FritzX"))
            return "1400kg Guided Bomb";
        if (weaponName.startsWith("H19"))
            return "Rocket Launchers";
        if (weaponName.startsWith("HS_293"))
            return "1045kg Hs 293 Guided Missile";
        if (weaponName.startsWith("HVAR2"))
            return "2.5'' HVAR Rocket";
        if (weaponName.startsWith("HVAR5AP"))
            return "134lbs 5'' Armor Piercing HVAR Rocket";
        if (weaponName.startsWith("HVAR5"))
            return "134lbs 5'' HVAR Rocket";
        if (weaponName.startsWith("HVARF84"))
            return "134lbs 5'' HVAR Rocket";
        if (weaponName.startsWith("HYDRA"))
            return "13.6lb FFAR Rocket";
        if (weaponName.startsWith("LR130"))
            return "LR-130 Rocket Launcher";
        if (weaponName.startsWith("LR55"))
            return "LR-55 Rocket Launcher";
        if (weaponName.startsWith("M13"))
            return "95lbs M-13 Rocket";
        if (weaponName.startsWith("PB1"))
            return "6.5kg Anti-Tank Rocket";
        if (weaponName.startsWith("PB2"))
            return "5.1kg Anti-Tank Rocket";
        if (weaponName.startsWith("PC1000"))
            return "1000kg Anti-Armor Rocket-Bomb";
        if (weaponName.startsWith("PhBall"))
            return "Phosphorous Ball";
        if (weaponName.startsWith("R4M"))
            return "3.85kg Anti-Aircraft Rocket";
        if (weaponName.startsWith("ROFS132"))
            return "50lbs ROFS-132 Fragmentation Rocket";
        if (weaponName.startsWith("RS132"))
            return "50lbs RS-132 Rocket";
        if (weaponName.startsWith("RS82"))
            return "15lbs RS-82 Rocket";
        if (weaponName.startsWith("Razon"))
            return "1000lbs Razon Guided Bomb";
        if (weaponName.startsWith("S5"))
            return "4kg S5 Rocket";
        if (weaponName.startsWith("SURA_AP"))
            return "11kg Armor Piercing SURA Rocket";
        if (weaponName.startsWith("SURA_HE"))
            return "11kg High Explosive SURA Rocket";
        if (weaponName.startsWith("SimpleM13"))
            return "95lbs M-13 Rocket";
        if (weaponName.startsWith("SimpleRS132"))
            return "50lbs RS-132 Rocket";
        if (weaponName.startsWith("SimpleRS150"))
            return "70lbs RS-150 Rocket";
        if (weaponName.startsWith("SimpleRS82"))
            return "15lbs RS-82 Rocket";
        if (weaponName.startsWith("TinyTim"))
            return "1255lbs Tiny Tim Rocket";
        if (weaponName.startsWith("Type3Mk27"))
            return "20lb Anti-Aircraft Rocket";
        if (weaponName.startsWith("WPL5"))
            return "Bazooka Rocket";
        if (weaponName.startsWith("WPT6"))
            return "Bazooka Rocket";
        if (weaponName.startsWith("WfrGr21"))
            return "112.6kg 21cm Anti-Aircraft Rocket Mortar";
        if (weaponName.startsWith("X4"))
            return "60kg X4 Guided Missile";
        return weaponName;
    }

    private static String translateFuelTank(String weaponName) {
        if (weaponName.startsWith("200gal"))
            return "200gal Tank";
        if (weaponName.startsWith("Dag"))
            return "300l Tank";
        if (weaponName.startsWith("Ju87"))
            return "300l Tank";
        if (weaponName.startsWith("Nag"))
            return "300l Tank";
        if (weaponName.startsWith("Tank0Centre"))
            return "300l Tank";
        if (weaponName.startsWith("Tank0Underwing"))
            return "150l Tank";
        if (weaponName.startsWith("Tank0Wooden"))
            return "300l Tank";
        if (weaponName.startsWith("Tank0"))
            return "300l Tank";
        if (weaponName.startsWith("Tank100gal"))
            return "100gal Tank";
        if (weaponName.startsWith("Tank100i16"))
            return "100l Tank";
        if (weaponName.startsWith("Tank108gal"))
            return "108gal Tank";
        if (weaponName.startsWith("Tank110gal"))
            return "110gal Tank";
        if (weaponName.startsWith("Tank120"))
            return "120l Tank";
        if (weaponName.startsWith("Tank150gal"))
            return "150gal Tank";
        if (weaponName.startsWith("Tank154gal"))
            return "154gal Tank";
        if (weaponName.startsWith("Tank165gal"))
            return "165gal Tank";
        if (weaponName.startsWith("Tank178gal"))
            return "178gal Tank";
        if (weaponName.startsWith("Tank207gal"))
            return "207gal Tank";
        if (weaponName.startsWith("Tank230gal"))
            return "230gal Tank";
        if (weaponName.startsWith("Tank240"))
            return "240l Tank";
        if (weaponName.startsWith("Tank44gal"))
            return "44gal Tank";
        if (weaponName.startsWith("Tank50l"))
            return "50l Tank";
        if (weaponName.startsWith("Tank60gal"))
            return "60gal Tank";
        if (weaponName.startsWith("Tank75gal"))
            return "75gal Tank";
        if (weaponName.startsWith("Tank80"))
            return "80l Tank";
        if (weaponName.startsWith("Tank900"))
            return "900l Tank";
        if (weaponName.startsWith("TankA5M"))
            return "160l Tank";
        if (weaponName.startsWith("TankAD4"))
            return "150gal Tank";
        if (weaponName.startsWith("TankC120"))
            return "120gal Combat Tank";
        if (weaponName.startsWith("TankF4F"))
            return "150gal Tank";
        if (weaponName.startsWith("TankF4U"))
            return "150gal Tank";
        if (weaponName.startsWith("TankKi43Underwing"))
            return "200l Tank";
        if (weaponName.startsWith("TankKi44Underwing"))
            return "100l Tank";
        if (weaponName.startsWith("TankKi61Underwing"))
            return "200l Tank";
        if (weaponName.startsWith("TankKi84"))
            return "150l Tank";
        if (weaponName.startsWith("TankN1K1"))
            return "300l Tank";
        if (weaponName.startsWith("TankP38"))
            return "75gal Tank";
        if (weaponName.startsWith("TankSpit30"))
            return "30gal Tank";
        if (weaponName.startsWith("TankSpit45"))
            return "45gal Tank";
        if (weaponName.startsWith("TankSpit90"))
            return "90gal Tank";
        if (weaponName.startsWith("TankT6"))
            return "20gal Tank";
        if (weaponName.startsWith("TankTempest"))
            return "90gal Tank";
        if (weaponName.startsWith("Type_D"))
            return "300l Tank";
        return weaponName;
    }

    private void logFpsToFile() {
        if (this.fpsLogWriter == null) {
            this.missionName = getMissionName();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh-mm-ss z");
            Date date = new Date();
            try {
                this.fpsLogWriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(FPS_LOG_FILE + " (" + this.missionName + " @ " + dateFormat.format(date) + ")" + FPS_LOG_FILE_EXT, 0))));
                this.fpsLogWriter.println("Time,FPS Current,FPS Average,FPS Max,FPS Min,Recent FPS Average,Recent FPS Max,Recent FPS Min,Frame");
            } catch (IOException IOE) {
                System.out.println("*** FPS DEBUG: Creating FPS Log File failed: " + IOE.getMessage());
            }
        }
        // @formatter:off
        this.fpsLogWriter.println(
                convertMillisecondsToHMmSs(Time.real() - this.timeStart) + ","
              + (int) Math.floor(this.fpsCur) + ","
              + (int) Math.floor((1000D * (this.framePrev - this.frameStart)) / (this.timePrev - this.timeStart)) + ","
              + (int) Math.floor(this.fpsMax) + ","
              + (int) Math.floor(this.fpsMin == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMin) + ","
              + (int) Math.floor((1000D * (this.framePrev - ((Integer) this.frameList.get(0)).intValue())) / (this.timePrev - ((Long) this.timeList.get(0)).longValue())) + ","
              + (int) Math.floor(this.fpsMaxRecent) + ","
              + (int) Math.floor(this.fpsMinRecent == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMinRecent) + ","
              + (this.framePrev - this.frameStart));
        // @formatter:on
    }

    public void flushFpsLogFile() {
        if (this.fpsLogWriter == null) {
            return;
        }
        this.fpsLogWriter.flush();
        this.fpsLogWriter.close();
        this.fpsLogWriter = null;
    }


    private static final double        FPS_MIN_DEFAULT        = 1000000D;
    private static final long          RECENT_RESET_DEFAULT   = 10000;
    private static final long          LOG_EXT_PERIOD_DEFAULT = 250L;
    private static final int           VIEWPORT_WIDTH_MAX     = 600;
    private static final int           CAPTION_MARGIN         = -1;
    private static final int           VALUE_MARGIN           = 8;
    private static final Color4f       FPS_COLOR              = new Color4f(0.0F, 1.0F, 1.0F, 1.0F);
    private static TTFont              ttfont;
    private static Color4f             ttcolor;
    private double                     fpsMinRecent;
    private double                     fpsMaxRecent;
    private double                     fpsLast;
    private boolean                    fpsMinValid;
    private String                     missionName;
    private long                       recentResetTimeout;
    private long                       logExtPeriod;
    private long                       timePrevLogExt;
    private ArrayList                  frameList;
    private ArrayList                  timeList;
    private static final DecimalFormat df2                    = new DecimalFormat("00");
    private static final DecimalFormat df3                    = new DecimalFormat("000");
    public static final String         LOGEXT                 = "LOGEXT";

    private static final String FPS_LOG_FILE = "FPS_LOG";
    private static final String FPS_LOG_FILE_EXT = ".csv";
    private PrintWriter fpsLogWriter = null;

    private boolean            bGo;
    private boolean            bShow;
    private long               timeStart;
    private int                frameStart;
    private double             fpsMin;
    private double             fpsMax;
    private double             fpsCur;
    private long               timePrev;
    private long               lastFpsMax = 0;
    private long               lastFpsMin = 0;
//    private static final long  CLEAR_FPS_MIN_MAX_TIMEOUT = 10000;
    private int                framePrev;
    private boolean            logExt = false;
    private boolean            missionPlaying = false;
    private long               logPeriod;
    private long               logPrintTime;
    public static final String LOG        = "LOG";
    public static final String START      = "START";
    public static final String STOP       = "STOP";
    public static final String SHOW       = "SHOW";
    public static final String HIDE       = "HIDE";
    public static final String PERF       = "PERF";
    private MsgTimeOut         msg;
    public static String       dumpDesc   = "unkown";
    public static int          dumpStart  = 0;
    private static int         fuelLevel = -1;
    private static int         radiatorSetting = 100;
    private static boolean     fullDump = false;
    private static boolean     settingsInitialized = false;
    public static String       turnFile;
    public static String       speedFile;
    public static String       auxFile;
    public static String       craftFile;
    public static String       weaponFile;
}
