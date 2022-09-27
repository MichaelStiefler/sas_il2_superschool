package com.maddox.il2.engine.cmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.builder.PlMapLoad.Land;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Pre;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.fm.Squares;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Finger;
import com.maddox.rts.HomePath;
import com.maddox.rts.KryptoInputFilter;
import com.maddox.rts.KryptoOutputFilter;
import com.maddox.rts.NetEnv;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SFSReader;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.sas1946.Junidecode;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.util.NumberTokenizer;

public class CmdPreload extends Cmd {

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
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNum() {
            return this.num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getBullets() {
            return this.bullets;
        }

        public void setBullets(int bullets) {
            this.bullets = bullets;
        }

        public int getType() {
            return this.type;
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

    public class ListAircraft implements Comparable {
        private final ArrayList loadouts;
        private final String    keyName;
        private final String    readableName;

        public ListAircraft(String keyName, String readableName) {
            this.loadouts = new ArrayList();
            this.keyName = keyName;
            this.readableName = readableName;
        }

        public ArrayList getLoadouts() {
            return this.loadouts;
        }

        public void addLoadout(String loadoutName, String loadoutReadableName) {
            this.loadouts.add(new ListLoadout(loadoutName, loadoutReadableName));
        }

        public String getKeyName() {
            return this.keyName;
        }

        public String getReadableName() {
            return this.readableName;
        }

        public int compareTo(Object o) {
            if (!(o instanceof ListAircraft)) {
                throw new IllegalArgumentException();
            }
            return this.keyName.compareTo(((ListAircraft) o).keyName);
        }
    }

    public class ListLoadout {
        private String loadoutName;
        private String loadoutReadableName;

        public ListLoadout(String loadoutName, String loadoutReadableName) {
            this.loadoutName = loadoutName;
            this.loadoutReadableName = loadoutReadableName;
        }

        public String getLoadoutName() {
            return this.loadoutName;
        }

        public void setLoadoutName(String loadoutName) {
            this.loadoutName = loadoutName;
        }

        public String getLoadoutReadableName() {
            return this.loadoutReadableName;
        }

        public void setLoadoutReadableName(String loadoutReadableName) {
            this.loadoutReadableName = loadoutReadableName;
        }
    }

    public Object exec(CmdEnv cmdenv, Map map) {
        if (map.containsKey(CmdPreload.REGISTER)) {
            Pre.register(true);
        }
        if (map.containsKey(CmdPreload.NOREGISTER)) {
            Pre.register(false);
        }
        if (map.containsKey(CmdPreload.CLEAR)) {
            Pre.clear();
        }
        if (map.containsKey(CmdPreload.SAVE)) {
            Pre.save(Cmd.arg(map, CmdPreload.SAVE, 0));
        }
        if (map.containsKey(CmdPreload.LIST_LOADOUTS)) {
            this.ListLoadouts(Cmd.arg(map, CmdPreload.LIST_LOADOUTS, 0, CmdPreload.LISTMODE_PADDED_CLASSNAME_SORTED));
        }
        if (map.containsKey(FMINFO)) {
            if (Mission.isNet() && (NetEnv.hosts().size() > 1)) {
                System.out.println("Network mission detected, disabling fm debug.");
            } else {
                CmdPreload.readConfig();
                if (map.containsKey(SWITCH)) {
                    if (!Maneuver.showFM) {
                        Maneuver.showFM = true;
                    } else {
                        Maneuver.showFM = false;
                    }
                } else if (map.containsKey(DUMP) && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead()) {
                    CmdPreload.dumpFlightModel(World.getPlayerAircraft().getClass().getName(), false);
//                    FlightModel flightmodel = World.getPlayerFM();
//                    if (flightmodel != null)
//                        flightmodel.drawData();
                } else if (map.containsKey(DMPALL) && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead()) {
                    //dumpDesc = Reflection.getString(Config.class, "VERSION");
                    CmdPreload.dumpStart = 0;
                    if (Cmd.nargs(map, DMPALL) > 1) {
                        CmdPreload.dumpStart = Cmd.arg(map, DMPALL, 1, 0);
                        CmdPreload.dumpDesc = Cmd.arg(map, DMPALL, 0);
                    } else if (Cmd.nargs(map, DMPALL) > 0) {
                        String arg0 = Cmd.arg(map, DMPALL, 0);
                        try {
                            int intArg0 = Integer.parseInt(arg0);
                            CmdPreload.dumpStart = intArg0;
                        } catch (NumberFormatException nfe) {
                            CmdPreload.dumpDesc = arg0;
                        }
                    }
                    CmdPreload.dumpAllFlightModels();
                    RTSConf.setRequestExitApp(true);
                } else if (map.containsKey(DMPLIST) && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead()) {
                    //dumpDesc = Reflection.getString(Config.class, "VERSION");
                    CmdPreload.dumpFlightModelsFromList();
                    RTSConf.setRequestExitApp(true);
                } else if (map.containsKey(FMDMP) && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead()) {
//                  iFuelLevel = 100;
                    if (Cmd.nargs(map, FMDMP) > 0) {
                        CmdPreload.dumpFlightModel(Cmd.arg(map, FMDMP, 0), true);
                        RTSConf.setRequestExitApp(true);
                    } else {
                        CmdPreload.dumpFlightModel(World.getPlayerAircraft().getClass().getName(), true);
                        RTSConf.setRequestExitApp(true);
                    }
                } else if (map.containsKey(DMPFILES)) {
                    System.out.println("Generating Flight Model File Parameter Matrix...");
                    this.dumpFmFiles();
                    System.out.println("Flight Model File Parameter Matrix generated!");
                }
            }
        }
        if (map.containsKey(CmdPreload.COD)) {
            System.out.println("Generating cod Loadouts...");
            this.generateCodLoadouts();
            System.out.println("cod Loadouts generated!");
        }
        if (map.containsKey(CmdPreload.FBDJ)) {
            System.out.println("Generating FBDj Files...");
            this.generateFbdjFiles();
            System.out.println("FBDj Files generated!");
        }
        if (map.containsKey("_$$")) {
            final List list = (List) map.get("_$$");
            for (int i = 0; i < list.size(); i++) {
                Pre.loading((String) list.get(i));
            }

        }
        return CmdEnv.RETURN_OK;
    }

    public CmdPreload() {
        this.param.put(CmdPreload.SAVE, null);
        this.param.put(CmdPreload.REGISTER, null);
        this.param.put(CmdPreload.NOREGISTER, null);
        this.param.put(CmdPreload.CLEAR, null);
        this.param.put(CmdPreload.LIST_LOADOUTS, null);
        this.param.put(CmdPreload.FMINFO, null);
        this.param.put(CmdPreload.SWITCH, null);
        this.param.put(CmdPreload.DUMP, null);
        this.param.put(CmdPreload.DMPALL, null);
        this.param.put(CmdPreload.DMPFILES, null);
        this.param.put(CmdPreload.DMPLIST, null);
        this.param.put(CmdPreload.FMDMP, null);
        this.param.put(CmdPreload.COD, null);
        this.param.put(CmdPreload.FBDJ, null);
        this._properties.put("NAME", "preload");
        this._levelAccess = 1;
    }

    private void ListLoadouts(int listMode) {
        final List airClasses = Main.cur().airClasses;
        int paddingPos = 0;
        int maxLength = 0;
        int maxWeaponsLength = 0;
        final ArrayList listAircraft = new ArrayList(airClasses.size());
        for (int airClassIndex = 0; airClassIndex < airClasses.size(); airClassIndex++) {
            final Class airClass = (Class) airClasses.get(airClassIndex);
            String airClassName = airClass.getName();
            final String airClassKeyName = Property.stringValue(airClass, "keyName");
            final String airClassReadableName = I18N.plane(airClassKeyName);
            if (!airClassName.startsWith("com.maddox.il2.objects.air")) {
                continue;
            }
            airClassName = airClassName.substring(airClassName.lastIndexOf('.') + 1);
            if (airClassName.equals("Placeholder")) {
                continue;
            }
            final String weapons[] = Aircraft.getWeaponsRegistered(airClass);
            if ((weapons == null) || (weapons.length == 0)) {
                continue;
            }
            final ListAircraft curListAircraft = new ListAircraft(airClassKeyName, airClassReadableName);
            listAircraft.add(curListAircraft);
            if (maxLength < (airClassReadableName.length() + 2)) {
                maxLength = airClassReadableName.length() + 2;
            }
            for (int weaponsIndex = 0; weaponsIndex < weapons.length; weaponsIndex++) {
                final String weaponsName = weapons[weaponsIndex];
                final String weaponsReadableName = I18N.weapons(airClassKeyName, weaponsName);
                curListAircraft.addLoadout(weaponsName, weaponsReadableName);
                final int curPaddingPos = airClassKeyName.length() + weaponsName.length() + 1 + CmdPreload.PADDING_SPACES;
                if (paddingPos < curPaddingPos) {
                    paddingPos = curPaddingPos;
                }
                if (maxWeaponsLength < weaponsReadableName.length()) {
                    maxWeaponsLength = weaponsReadableName.length();
                }
                final int curLength = curPaddingPos + weaponsReadableName.length();
                if (maxLength < curLength) {
                    maxLength = curLength;
                }
            }
        }

        if ((listMode & CmdPreload.SORTED) != 0) {
            Collections.sort(listAircraft);
        }

        if ((listMode & CmdPreload.PADDED) != 0) {
            maxLength = paddingPos + maxWeaponsLength;
        }
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' hh-mm-ss z");
        final Date date = new Date();
        PrintWriter pw;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(CmdPreload.WEAPONS_LIST_FILE, 0))));
        } catch (final IOException IOE) {
            System.out.println("*** WEAPONS LIST DEBUG: Creating Weapons List File failed: " + IOE.getMessage());
            return;
        }

        final String headLine = "Created by SAS Loadout Lister 4.09+ on " + dateFormat.format(date);
        if (maxLength < (headLine.length() + 4)) {
            maxLength = headLine.length() + 4;
        }

        pw.println(CmdPreload.repeat('#', maxLength));
        final int paddingLeft = (maxLength - headLine.length() - 2) / 2;
        final int paddingRight = maxLength - headLine.length() - 2 - paddingLeft;
        pw.println("#" + CmdPreload.repeat(' ', paddingLeft) + headLine + CmdPreload.repeat(' ', paddingRight) + "#");
        pw.println(CmdPreload.repeat('#', maxLength));
        pw.println();

        for (int aircraftIndex = 0; aircraftIndex < listAircraft.size(); aircraftIndex++) {
            final ListAircraft curListAircraft = (ListAircraft) listAircraft.get(aircraftIndex);
            pw.println(CmdPreload.repeat('#', maxLength));
            pw.println("# " + curListAircraft.getReadableName());
            pw.println(CmdPreload.repeat('#', maxLength));
            final ArrayList loadouts = curListAircraft.getLoadouts();
            for (int loadoutIndex = 0; loadoutIndex < loadouts.size(); loadoutIndex++) {
                final ListLoadout curListLoadout = (ListLoadout) loadouts.get(loadoutIndex);
                pw.print(curListAircraft.getKeyName());
                pw.print(".");
                pw.print(curListLoadout.getLoadoutName());
                if (curListLoadout.getLoadoutName().equals(curListLoadout.getLoadoutReadableName()) && ((listMode & CmdPreload.CLASSNAME) == 0)) {
                    pw.println();
                    continue;
                }
                if ((listMode & CmdPreload.PADDED) != 0) {
                    final int curLength = curListAircraft.getKeyName().length() + curListLoadout.getLoadoutName().length() + 1;
                    pw.print(CmdPreload.repeat(' ', paddingPos - curLength));
                } else {
                    pw.print(CmdPreload.repeat(' ', CmdPreload.PADDING_SPACES));
                }
                pw.println(curListLoadout.getLoadoutReadableName());
            }
            pw.println();

        }

        switch (listMode) {
            case LISTMODE_PADDED_CLASSNAME:
            default:
                break;
            case LISTMODE_UNPADDED_CLASSNAME:
                break;
            case LISTMODE_PADDED:
                break;
            case LISTMODE_NONE:
                break;
        }
        pw.close();
    }

    private void generateCodLoadouts() {
        String prohibitedLoadoutNameCharacters = " ,";
        boolean debugAirClassNames = false;
        boolean replaceSpacesInLoadoutNames = false;
        SectFile codIniSectfile = new SectFile("settings/cod.ini");
        if (codIniSectfile.sectionExist("Common")) {
            int commonSectionIndex = codIniSectfile.sectionIndex("Common");
            if (codIniSectfile.varExist(commonSectionIndex, "prohibitedLoadoutNameCharacters")) {
                prohibitedLoadoutNameCharacters = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "prohibitedLoadoutNameCharacters"));
                if (prohibitedLoadoutNameCharacters.startsWith("\"") && prohibitedLoadoutNameCharacters.endsWith("\"")) {
                    prohibitedLoadoutNameCharacters = prohibitedLoadoutNameCharacters.substring(1, prohibitedLoadoutNameCharacters.length() - 1);
                    System.out.println("prohibited Loadout Name Characters set to: \"" + prohibitedLoadoutNameCharacters + "\"");
                }
            }
            if (codIniSectfile.varExist(commonSectionIndex, "debugAirClassNames")) {
                debugAirClassNames = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "debugAirClassNames")).trim().equals("1");
                System.out.println("debug Air Class Names set to: \"" + debugAirClassNames + "\"");
            }
            if (codIniSectfile.varExist(commonSectionIndex, "replaceSpacesInLoadoutNames")) {
                replaceSpacesInLoadoutNames = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "replaceSpacesInLoadoutNames")).trim().equals("1");
                System.out.println("replace Spaces in Loadout Names set to: \"" + replaceSpacesInLoadoutNames + "\"");
            }
        }

        SectFile airIniSectfile = new SectFile("com/maddox/il2/objects/air.ini");
        File loadoutsDir = new File(HomePath.toFileSystemName("loadouts/new/", 0));
        if (!loadoutsDir.exists()) {
            loadoutsDir.mkdirs();
        }
        File loadoutsDir2 = new File(HomePath.toFileSystemName("loadouts/newNormalized/", 0));
        if (!loadoutsDir2.exists()) {
            loadoutsDir2.mkdirs();
        }
        File loadoutsDirCsv = new File(HomePath.toFileSystemName("loadouts/csv/", 0));
        if (!loadoutsDirCsv.exists()) {
            loadoutsDirCsv.mkdirs();
        }
        File loadoutsDirCsv2 = new File(HomePath.toFileSystemName("loadouts/csvNormalized/", 0));
        if (!loadoutsDirCsv2.exists()) {
            loadoutsDirCsv2.mkdirs();
        }
        File loadoutsDirDecoded = new File(HomePath.toFileSystemName("loadouts/decoded/", 0));
        if (!loadoutsDirDecoded.exists()) {
            loadoutsDirDecoded.mkdirs();
        }
        File loadoutsDirDecoded2 = new File(HomePath.toFileSystemName("loadouts/decodedNormalized/", 0));
        if (!loadoutsDirDecoded2.exists()) {
            loadoutsDirDecoded2.mkdirs();
        }
        File loadoutsDirCod = new File(HomePath.toFileSystemName("loadouts/cod/", 0));
        if (!loadoutsDirCod.exists()) {
            loadoutsDirCod.mkdirs();
        }
        File loadoutsDirNewCod = new File(HomePath.toFileSystemName("loadouts/new_cod/", 0));
        if (!loadoutsDirNewCod.exists()) {
            loadoutsDirNewCod.mkdirs();
        }
        try {
            PrintWriter pwErrors = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("loadouts/errors.txt", 0))));
            for (int sectionIndex = 0; sectionIndex < airIniSectfile.sections(); sectionIndex++) {
                for (int varsIndex = 0; varsIndex < airIniSectfile.vars(sectionIndex); varsIndex++) {
                    boolean hasCoddedWeapons = false;
                    NumberTokenizer numbertokenizer = new NumberTokenizer(airIniSectfile.value(sectionIndex, varsIndex));
                    String airplaneName = numbertokenizer.next((String) null);
                    Class airplaneClass = null;
                    try {
                        airplaneClass = ObjIO.classForName(airplaneName);
                    } catch (Exception exception) {
                        System.out.println("Class '" + airplaneName + "' not found");
                        continue;
                    }
                    if (debugAirClassNames) {
                        System.out.println("Class '" + airplaneName + "'...");
                    }

                    if (!airplaneName.startsWith("air.")) {
                        airplaneName = "###ERROR### " + airplaneName + " ###ERROR###";
                        pwErrors.println(airplaneName);
                    }
                    airplaneName = airplaneName.substring(4);
                    if (airplaneName.toLowerCase().startsWith("placeholder")) {
                        continue;
                    }

                    String[] hooks = Aircraft.getWeaponHooksRegistered(airplaneClass);
                    int[] triggers = Aircraft.getWeaponTriggersRegistered(airplaneClass);
                    String[] loadoutArray = Aircraft.getWeaponsRegistered(airplaneClass);
                    int hookLength = hooks.length;
                    int triggerLength = triggers.length;

                    int classFingerInt = Finger.Int("ce" + airplaneClass.getName() + "vd");
                    PrintWriter pwu = null;
                    PrintWriter pwu2 = null;
                    try {
                        BufferedReader brDecoded = new BufferedReader(new InputStreamReader(new KryptoInputFilter(new SFSInputStream(Finger.LongFN(0L, "cod/" + Finger.incInt(classFingerInt, "adt"))), CmdPreload.getSwTbl(classFingerInt))));
                        hasCoddedWeapons = true;

                        pwu = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("loadouts/decoded/" + airplaneName, 0))));
                        pwu2 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("loadouts/decodedNormalized/" + airplaneName, 0))));
                        do {
                            String decodedLine = brDecoded.readLine();
                            if (decodedLine == null) {
                                break;
                            }
                            if (decodedLine.trim().length() < 1) {
                                continue;
                            }
                            pwu.println(decodedLine);
                            List list = new ArrayList();
                            StringTokenizer st = new StringTokenizer(decodedLine, ",");
                            while (st.hasMoreTokens()) {
                                String token = st.nextToken();
                                if (token.trim().length() == 0) {
                                    token = " ";
                                }
                                list.add(token);
                            }
                            while ((list.size() - 1) < hookLength) {
                                list.add(" ");
                            }
                            while ((list.size() - 1) > hookLength) {
                                String removedItem = list.remove(list.size() - 1).toString();
                                if (removedItem.trim().length() > 0) {
                                    pwErrors.println(airplaneName + " (+) loadout " + list.get(0) + " needs trimming, but surplus element " + list.size() + " isn't empty (" + removedItem + ")");
                                }
                            }
                            Iterator li = list.iterator();
                            while (li.hasNext()) {
                                pwu2.print(li.next());
                                if (li.hasNext()) {
                                    pwu2.print(",");
                                }
                            }
                            pwu2.println();
                        } while (true);
                        //pwu.flush();
                        pwu.close();
                        //pwu2.flush();
                        pwu2.close();
                        brDecoded.close();

                        BufferedReader brEncrypted = new BufferedReader(new InputStreamReader(new SFSInputStream(Finger.LongFN(0L, "cod/" + Finger.incInt(classFingerInt, "adt")))));
                        BufferedWriter bwEncrypted = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(HomePath.toFileSystemName("loadouts/cod/" + Finger.incInt(classFingerInt, "adt"), 0))));
                        char[] cBuf = new char[1024];
                        do {
                            int iRead = brEncrypted.read(cBuf, 0, 1024);
                            if (iRead == -1) {
                                break;
                            }
                            bwEncrypted.write(cBuf, 0, iRead);
                        } while (true);
                        bwEncrypted.close();
                        brEncrypted.close();
                    } catch (Exception e) {
                        if (hasCoddedWeapons) {
                            e.printStackTrace();
                        }
                        hasCoddedWeapons = false;
                    }

                    String codIndicator = hasCoddedWeapons ? " (+) " : " (-) ";

                    if (hookLength != triggerLength) {
                        pwErrors.println(airplaneName + codIndicator + " hookLength != triggerLength");
                    }
                    try {
                        PrintWriter pwAircraft = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("loadouts/new/" + airplaneName, 0))));
                        PrintWriter pwAircraft2 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("loadouts/newNormalized/" + airplaneName, 0))));
                        PrintWriter pwk = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new KryptoOutputFilter(new FileOutputStream(HomePath.toFileSystemName("loadouts/new_cod/" + Finger.incInt(classFingerInt, "adt"), 0)), CmdPreload.getSwTbl(classFingerInt)))));
                        PrintWriter pwAircraftCsv = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("loadouts/csv/" + airplaneName + ".csv", 0))));
                        PrintWriter pwAircraftCsv2 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("loadouts/csvNormalized/" + airplaneName + ".csv", 0))));
                        pwAircraftCsv.println("sep=,");
                        pwAircraftCsv2.println("sep=,");

                        for (int hookIndex = 0; hookIndex < hookLength; hookIndex++) {
                            pwAircraftCsv.print(",");
                            pwAircraftCsv.print(hooks[hookIndex]);
                            pwAircraftCsv2.print(",");
                            pwAircraftCsv2.print(hooks[hookIndex]);
                        }
                        pwAircraftCsv.println();
                        pwAircraftCsv2.println();
                        for (int triggerIndex = 0; triggerIndex < triggerLength; triggerIndex++) {
                            pwAircraftCsv.print(",");
                            pwAircraftCsv.print(triggers[triggerIndex]);
                            pwAircraftCsv2.print(",");
                            pwAircraftCsv2.print(triggers[triggerIndex]);
                        }
                        pwAircraftCsv.println();
                        pwAircraftCsv2.println();

                        boolean lastElementNotNone = false;
                        if (loadoutArray.length < 2) {
                            pwErrors.println(airplaneName + codIndicator + " loadoutArray.length < 2");
                        } else {
                            if (!loadoutArray[0].equals("default")) {
                                pwErrors.println(airplaneName + codIndicator + " loadoutArray[0]=" + loadoutArray[0] + ", but first element needs to be default (case sensitive!)");
                            }
                            if (!loadoutArray[loadoutArray.length - 1].equals("none")) {
                                lastElementNotNone = true;
                            }
                        }
                        for (int loadoutIndex = 0; loadoutIndex < loadoutArray.length; loadoutIndex++) {
                            String loadoutsLine = loadoutArray[loadoutIndex];
                            String loadoutsLine2 = loadoutArray[loadoutIndex];
                            if (replaceSpacesInLoadoutNames && (loadoutsLine2.indexOf(' ') != -1)) {
                                loadoutsLine2 = loadoutsLine2.replace(' ', '_');
                            }
                            if ((loadoutIndex == 0) && loadoutsLine2.equalsIgnoreCase("default")) {
                                loadoutsLine2 = "default";
                            } else if ((loadoutIndex == (loadoutArray.length - 1)) && (loadoutsLine2.equalsIgnoreCase("none") || loadoutsLine2.equalsIgnoreCase("empty"))) {
                                loadoutsLine2 = "none";
                            }
                            if ((loadoutIndex != 0) && loadoutArray[loadoutIndex].toLowerCase().equals("default")) {
                                pwErrors.println(airplaneName + codIndicator + " loadoutArray[" + loadoutIndex + "]=" + loadoutArray[loadoutIndex] + ", but default is only allowed in first slot! (might indicate overlapping weapon slots)");
                            } else if ((loadoutIndex != (loadoutArray.length - 1)) && loadoutArray[loadoutIndex].toLowerCase().equals("none")) {
                                pwErrors.println(airplaneName + codIndicator + " loadoutArray[" + loadoutIndex + "]=" + loadoutArray[loadoutIndex] + ", but none is only allowed in last slot! (might indicate overlapping weapon slots)");
                            } else {
                                for (int prohibitedCharacterIndex = 0; prohibitedCharacterIndex < prohibitedLoadoutNameCharacters.length(); prohibitedCharacterIndex++) {
                                    if (loadoutArray[loadoutIndex].indexOf(prohibitedLoadoutNameCharacters.charAt(prohibitedCharacterIndex)) > -1) {
                                        pwErrors.println(airplaneName + codIndicator + " loadout " + loadoutArray[loadoutIndex] + " name contains prohibited Characters!");
                                        break;
                                    }
                                }
                            }
//                            } else if (loadoutArray[loadoutIndex].indexOf(' ') > -1) {
//                                pwErrors.println(airplaneName + codIndicator + " loadout " + loadoutArray[loadoutIndex] + " name contains spaces!");
//                            }

                            Aircraft._WeaponSlot[] weaponSlotArray = Aircraft.getWeaponSlotsRegistered(airplaneClass, loadoutArray[loadoutIndex]);
                            if (weaponSlotArray == null) {
                                pwErrors.println(airplaneName + codIndicator + " loadout " + loadoutArray[loadoutIndex] + " isn't a registered slot on this aircraft class!");
                                pwAircraft.println(loadoutsLine);
                                pwAircraftCsv.println(loadoutsLine);
                                continue;
                            }
                            if (hookLength != weaponSlotArray.length) {
                                pwErrors.println(airplaneName + codIndicator + " loadout " + loadoutArray[loadoutIndex] + " length (" + weaponSlotArray.length + ") doesn't match hook length (" + hookLength + ")");
                            }
                            for (int slotIndex = 0; slotIndex < weaponSlotArray.length; slotIndex++) {
                                if (weaponSlotArray[slotIndex] == null) {
                                    loadoutsLine += ", ";
                                    if (slotIndex < hookLength) {
                                        loadoutsLine2 += ", ";
                                    }
                                } else {
                                    String weaponName = ObjIO.classGetName(weaponSlotArray[slotIndex].clazz);
                                    if (!weaponName.startsWith("weapons.")) {
                                        weaponName = "###ERROR### " + weaponName + " ###ERROR###";
                                    } else {
                                        weaponName = weaponName.substring(8);
                                    }
                                    loadoutsLine += "," + weaponSlotArray[slotIndex].trigger + " " + weaponName + " " + weaponSlotArray[slotIndex].bullets;
                                    if (slotIndex < hookLength) {
                                        loadoutsLine2 += "," + weaponSlotArray[slotIndex].trigger + " " + weaponName + " " + weaponSlotArray[slotIndex].bullets;
                                    } else {
                                        pwErrors.println(airplaneName + codIndicator + " loadout " + loadoutArray[loadoutIndex] + " needs trimming, but surplus element " + slotIndex + " isn't empty (" + weaponSlotArray[slotIndex].trigger + " " + weaponName + " " + weaponSlotArray[slotIndex].bullets + ")");
                                    }
                                }
                            }

                            if (weaponSlotArray.length < hookLength) {
                                for (int slotIndex = weaponSlotArray.length; slotIndex < hookLength; slotIndex++) {
                                    loadoutsLine2 += ", ";
                                }
                            }

                            pwAircraft.println(loadoutsLine);
                            pwAircraftCsv.println(loadoutsLine);
                            pwAircraft2.println(loadoutsLine2);
                            pwAircraftCsv2.println(loadoutsLine2);
                            pwk.println(loadoutsLine);
                        }
                        pwAircraft.close();
                        pwAircraftCsv.close();
                        pwAircraft2.close();
                        pwAircraftCsv2.close();
                        pwk.close();
                        if (lastElementNotNone) {
                            pwErrors.println(airplaneName + codIndicator + " loadoutArray[" + (loadoutArray.length - 1) + "]=" + loadoutArray[loadoutArray.length - 1] + ", but last element needs to be none (case sensitive!)");
                        }
                    } catch (Exception e) {
                        System.out.println("Error on Aircraft " + airplaneName);
                        e.printStackTrace();
                    }
                }
            }
            pwErrors.close();
        } catch (Exception oe) {
            oe.printStackTrace();
        }
    }

    private static int[] getSwTbl(int i) {
        if (i < 0) {
            i = -i;
        }
        int ims = (i % 16) + 11;
        int ktl = i % Finger.kTable.length;
        if (ims < 0) {
            ims = -ims % 16;
        }
        if (ims < 10) {
            ims = 10;
        }
        if (ktl < 0) {
            ktl = -ktl % Finger.kTable.length;
        }
        int[] aiRetval = new int[ims];
        for (int aiRetvalIndex = 0; aiRetvalIndex < ims; aiRetvalIndex++) {
            aiRetval[aiRetvalIndex] = Finger.kTable[((ktl + aiRetvalIndex) % Finger.kTable.length)];
        }
        return aiRetval;
    }

    public static String repeat(char chr, int times) {
        return new String(new char[times]).replace('\0', chr);
    }

    public static String convertMillisecondsToHMmSs(long milliseconds) {
        long ms = milliseconds % 1000L;
        long s = (milliseconds / 1000L) % 60L;
        long m = (milliseconds / (1000L * 60L)) % 60L;
        long h = (milliseconds / (1000L * 60L * 60L)) % 24L;
        return CmdPreload.df2.format(h) + ":" + CmdPreload.df2.format(m) + ":" + CmdPreload.df2.format(s) + "." + CmdPreload.df3.format(ms);
    }

    private static void readConfig() {
        if (CmdPreload.settingsInitialized) {
            return;
        }
        System.out.println("InfoMod FullAuto 4.10+ reading settings...");
        SectFile codIniSectfile = new SectFile("settings/infomod.ini");
        CmdPreload.fuelLevel = codIniSectfile.get("Common", "fuelLevel", -1, -1, 100);
        CmdPreload.radiatorSetting = codIniSectfile.get("Common", "radiatorSetting", 100, 0, 100);
        CmdPreload.dumpDesc = codIniSectfile.get("Common", "dumpDesc", Reflection.getString(Config.class, "VERSION"));
        if (CmdPreload.fuelLevel == -1) {
            System.out.println("Fuel Level = 25%, 50%, 75% and 100%");
        } else {
            System.out.println("Fuel Level = " + CmdPreload.fuelLevel + "%");
        }
        System.out.println("Radiator Setting = " + CmdPreload.radiatorSetting + "%");
        System.out.println("Flight Model Descriptor = " + CmdPreload.dumpDesc);
        CmdPreload.settingsInitialized = true;
    }

    private static void dumpFlightModelsFromList() {
        System.out.println("*** FM DUMP (LIST) START ! ***");
        CmdPreload.fullDump = true;
        int saveMouseMode = RTSConf.cur.getUseMouse();
        RTSConf.cur.setUseMouse(0);
        try {
            BufferedReader listReader = new BufferedReader(new SFSReader("settings/dumplist.ini"));
            for (;;) {
                String listLine = listReader.readLine();
                if (listLine == null) {
                    break;
                }
                listLine = listLine.trim();
                if (listLine.startsWith("//") || listLine.startsWith(";") || listLine.startsWith("#") || (listLine.length() == 0)) {
                    continue;
                }
                Class airClass = Class.forName("com.maddox.il2.objects.air." + listLine);
                System.out.println("Dumping Flight Model for " + listLine);
                CmdEnv.top().exec("fps FMINFO FMDMP " + airClass.getName());
//              System.out.flush();
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
        CmdPreload.fullDump = false;
        System.out.println("*** FM DUMP (LIST) FINISHED ! ***");
        System.out.flush();
        RTSConf.cur.setUseMouse(saveMouseMode);
        RTSConf.setRequestExitApp(true);
    }

    private static void dumpAllFlightModels() {
        System.out.println("*** FM DUMP START ! ***");
        CmdPreload.fullDump = true;
        int saveMouseMode = RTSConf.cur.getUseMouse();
        RTSConf.cur.setUseMouse(0);
        for (int iAirClassIterator = CmdPreload.dumpStart; iAirClassIterator < Main.cur().airClasses.size(); iAirClassIterator++) {
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
        CmdPreload.fullDump = false;
        System.out.println("*** FM DUMP FINISHED ! ***");
        RTSConf.cur.setUseMouse(saveMouseMode);
        RTSConf.setRequestExitApp(true);
    }

    private static void dumpFlightModel(String theAircraftClass, boolean reloadAircraft) {
        HUD.training("dumpFlightModel " + theAircraftClass);
        try {
            if (!theAircraftClass.startsWith("com.maddox.il2.objects.air.")) {
                theAircraftClass = "com.maddox.il2.objects.air." + theAircraftClass;
            }
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

            String airName = CmdPreload.getAirNameForClassName(airClass.getName().substring(27));
            String i18Name = I18N.plane(airName);

//              try {
//                  i18Name = new String(i18Name.getBytes("US-ASCII"));
//              } catch (UnsupportedEncodingException e) {
//                  e.printStackTrace();
//              }

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
            CmdPreload.speedFile = "FlightModels/DataSpeed/" + i18Name;
            CmdPreload.turnFile = "FlightModels/DataTurn/" + i18Name;
            CmdPreload.craftFile = "FlightModels/DataCraft/" + i18Name;
            CmdPreload.weaponFile = "FlightModels/DataWeapon/" + i18Name;
            CmdPreload.auxFile = "FlightModels/DataAux/" + i18Name;
            CmdPreload.drawData(theFlightModel, CmdPreload.fuelLevel, CmdPreload.speedFile, CmdPreload.turnFile, CmdPreload.auxFile);
            CmdPreload.drawCraft(theFlightModel, CmdPreload.craftFile + " (" + CmdPreload.dumpDesc + ").txt");
            CmdPreload.dumpWeaponList(theAircraft, airName, CmdPreload.weaponFile + " (" + CmdPreload.dumpDesc + ").txt");
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
//              System.out.println("drawData " + i);
                Wing.G = (M.getFullMass() - ((M.maxFuel * i) / 100F)) * Atmosphere.g();
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
                CmdPreload.drawGraphs(Wing, s1, i, true);
                Wing.setFlaps(0.0F);

                // TODO: Added by SAS~Storebror, full speed usually requires radiators to be fully open!
                boolean hasRadiatorControl = false;
                for (int engineIndex = 0; engineIndex < fm.EI.getNum(); engineIndex++) {
                    if (fm.EI.engines[engineIndex].isHasControlRadiator()) {
                        hasRadiatorControl = true;
                        break;
                    }
                }
                if (hasRadiatorControl) {
                    float oldCxMin = Reflection.getFloat(Wing, "CxMin");
                    float newCxMin = oldCxMin + ((fm.radiatorCX * CmdPreload.radiatorSetting) / 100F);
                    Reflection.setFloat(Wing, "CxMin", newCxMin);
                }

                s1 = speedFile + " " + (100 - i) + "% fuel" + " (" + CmdPreload.dumpDesc + ")_speed.txt";
                CmdPreload.drawSpeed(fm, s1);
                Wing.setFlaps(0.0F);
                String s2 = auxFile + " avail thrust (" + CmdPreload.dumpDesc + ").txt";
                String s3 = auxFile + " thrust summary " + (100 - i) + "% fuel (" + CmdPreload.dumpDesc + ").txt";
                String s4 = auxFile + " req thrust " + (100 - i) + "% fuel (" + CmdPreload.dumpDesc + ").txt";
                if (!CmdPreload.fullDump) {
                    CmdPreload.drawZhukovski(fm, s2, s3, s4);
                }
                Wing.setFlaps(0.0F);
            }
        } else {
            int i = 100 - iFuelLevel;
            if (i < 0) {
                i = 0;
            }
            Wing.G = (M.getFullMass() - ((M.maxFuel * i) / 100F)) * Atmosphere.g();
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
            CmdPreload.drawGraphs(Wing, s1, i, false);
            Wing.setFlaps(0.0F);

            // TODO: Added by SAS~Storebror, full speed usually requires radiators to be fully open!
            boolean hasRadiatorControl = false;
            for (int engineIndex = 0; engineIndex < fm.EI.getNum(); engineIndex++) {
                if (fm.EI.engines[engineIndex].isHasControlRadiator()) {
                    hasRadiatorControl = true;
                    break;
                }
            }
            if (hasRadiatorControl) {
                float oldCxMin = Reflection.getFloat(Wing, "CxMin");
                float newCxMin = oldCxMin + ((fm.radiatorCX * CmdPreload.radiatorSetting) / 100F);
                Reflection.setFloat(Wing, "CxMin", newCxMin);
            }

            s1 = speedFile + " (" + CmdPreload.dumpDesc + ")_speed.txt";
            CmdPreload.drawSpeed(fm, s1);
            Wing.setFlaps(0.0F);
            if (!CmdPreload.fullDump) {
                String s2 = auxFile + " avail thrust (" + CmdPreload.dumpDesc + ").txt";
                String s3 = auxFile + " thrust summary " + (100 - i) + "% fuel (" + CmdPreload.dumpDesc + ").txt";
                String s4 = auxFile + " req thrust " + (100 - i) + "% fuel (" + CmdPreload.dumpDesc + ").txt";
                CmdPreload.drawZhukovski(fm, s2, s3, s4);
            }
            Wing.setFlaps(0.0F);
        }

        if (!CmdPreload.fullDump) {

            String s = speedFile + " data (" + CmdPreload.dumpDesc + ").txt";
            PrintWriter printwriter = null;
            try {
                File file = new File(HomePath.toFileSystemName(s, 0));
                file.getParentFile().mkdirs();
                printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
                printwriter.println("***MASS DATA***");
                printwriter.println("Empty: " + M.massEmpty);
                printwriter.println("Takeoff (reference only, not used directly in game): " + M.maxWeight);
                printwriter.println("Max fuel: " + M.maxFuel);
                if (M.maxNitro > 0.0F) {
                    printwriter.println("Max nitro: " + M.maxNitro);
                }
                printwriter.println("");
                printwriter.println("***POWER PLANT DATA***");
                printwriter.println("Number of engines: " + EI.engines.length);
                for (int j = 0; j < EI.engines.length; j++) {
                    if (EI.engines[j] != null) {
                        printwriter.println("*Engine " + (j + 1) + " data*");
                        CmdPreload.DataToFile(EI.engines[j], printwriter);
                    }
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
                printwriter.println("Vmax0: " + (int) (fm.Vmax * 3.6000000000000001D));
                printwriter.println("VmaxH: " + (int) (fm.VmaxH * 3.6000000000000001D));
                printwriter.println("Vmin: " + (int) (fm.Vmin * 3.6000000000000001D));
                printwriter.println("VminFLAPS_MAXRPM: " + (int) (fm.VminFLAPS * 3.6000000000000001D));
                printwriter.println("VminFLAPS_MINRPM: " + (int) (Wing.V_land * 3.6000000000000001D));
                printwriter.println("VmaxFLAPS: " + (int) (fm.VmaxFLAPS * 3.6000000000000001D));
                printwriter.println("VmaxAllowed: " + (int) (fm.VmaxAllowed * 3.6000000000000001D));
                printwriter.println("V_turn: " + (int) (Wing.V_turn * 3.6000000000000001D));
                printwriter.println("V_climb: " + (int) (Wing.V_climb * 3.6000000000000001D));
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
                if (printwriter != null) {
                    printwriter.close();
                }
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
            for (int engineIndex = 0; engineIndex < fm.EI.getNum(); engineIndex++) {
                Motor m = fm.EI.engines[engineIndex];
                for (int altitudeIndex = 0; altitudeIndex < m.compressorAltitudes.length; altitudeIndex++) {
                    int compressorAltitude = (int) m.compressorAltitudes[altitudeIndex] + 3000;
                    if (compressorAltitude > topAltitude) {
                        topAltitude = compressorAltitude;
//                  System.out.println("engines[" + engineIndex + "].compressorAltitudes[" + altitudeIndex + "] = " + m.compressorAltitudes[altitudeIndex] + ", compressorAltitude = " + compressorAltitude + ", topAltitude = " + topAltitude);
                    }
                }
            }
            topAltitude = (int) Math.ceil(topAltitude / 100D) * 100;

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
                    float f2 = k3 / 3.6F;
                    int l3 = (int) EI.forcePropAOA(f2, j2, 1.1F, true);
//                    System.out.println("EI.forcePropAOA for altitude " + j2 + "m = " + l3);
                    if (l3 <= 0) {
                        break;
                    }
                    printwriter.print("\t" + l3);
                    if (l3 > k2) {
                        k2 = l3;
                        l2 = k3;
                    }
                    int i4 = 0x186a0;
                    if (k3 >= 50) {

                        // TODO: +++ Test new Mass Calculation
                        fm.M.requestFuel(0F);
                        Wing.G = fm.M.getFullMass() * Atmosphere.g();
                        // -----------------------------------

                        float f3 = Wing.getClimb(f2, j2, l3);
//                        System.out.println("Wing.getClimb for altitude " + j2 + "m = " + f3);
                        if (f3 > f1) {
                            f1 = f3;
                            i3 = k3;
                        }
                        if ((f3 > 0.0F) && !flag) {
                            flag = true;
                        }
                        if ((f3 < 0.0F) && flag && (j3 == 0)) {
                            j3 = k3;
                        }
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
                if (j2 == 0) {
                    l = j3;
                }
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
            if (printwriter != null) {
                printwriter.close();
            }
            if (printwriter1 != null) {
                printwriter1.close();
            }
            if (printwriter2 != null) {
                printwriter2.close();
            }
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
//      System.out.println("drawSpeed " + s);
        Polares Wing = (Polares) Reflection.getValue(fm, "Wing");
        EnginesInterface EI = fm.EI;
        try {
            File file = new File(HomePath.toFileSystemName(s, 0));
            file.getParentFile().mkdirs();
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));

            int topAltitude = 10000;
            for (int engineIndex = 0; engineIndex < fm.EI.getNum(); engineIndex++) {
                Motor m = fm.EI.engines[engineIndex];
                for (int altitudeIndex = 0; altitudeIndex < m.compressorAltitudes.length; altitudeIndex++) {
                    int compressorAltitude = (int) m.compressorAltitudes[altitudeIndex] + 3000;
                    if (compressorAltitude > topAltitude) {
                        topAltitude = compressorAltitude;
//                    System.out.println("engines[" + engineIndex + "].compressorAltitudes[" + altitudeIndex + "] = " + m.compressorAltitudes[altitudeIndex] + ", compressorAltitude = " + compressorAltitude + ", topAltitude = " + topAltitude);
                    }
                }
            }
            topAltitude = (int) Math.ceil(topAltitude / 100D) * 100;

            for (int i = 0; i <= topAltitude; i += 100) {
                float f = -1000F;
                float f1 = -1000F;
                int j = 50;
                boolean isClimb = false;
                do {
                    if (j >= 1500) {
                        break;
                    }
                    float f2 = EI.forcePropAOA(j / 3.6F, i, 1.1F, true);

                    // TODO: +++ Test new Mass Calculation
                    fm.M.requestFuel(0F);
                    Wing.G = fm.M.getFullMass() * Atmosphere.g();
                    // -----------------------------------

                    float f4 = Wing.getClimb(j / 3.6F, i, f2);
//                    System.out.println("110%% altitude " + i + "m EI.forcePropAOA = " + f2 + ", Wing.getClimb = " + f4);
                    //System.out.println("i=" + i + ", j=" + j + ", f2=" + f2 + ", f4=" + f4 + ", f=" + f + ", f1=" + f1);
                    if (f4 > f) {
                        f = f4;
                    }
                    if (f4 > 0F) {
                        isClimb = true;
                    }
//                    if (f4 < 0.0F && f4 < f) {
                    if ((f4 < 0.0F) && isClimb) {
                        f1 = j;
                        break;
                    }
                    j++;
                } while (true);
                f1 *= 0.982F; // TODO: Added by SAS~Storebror, for some reason IL-2 Compare tends to give slightly unachievable speed values otherwise
                if (f1 < 0.0F) {
                    break;
//                    printwriter.print("\t");
//                else
//                    printwriter.print(f1 + "\t");
                }

                printwriter.print(i + "\t");
                printwriter.print(f1 + "\t");

                printwriter.print((f * Wing.Vyfac) + "\t");
                f = -1000F;
                f1 = -1000F;
                j = 50;
                do {
                    if (j >= 1500) {
                        break;
                    }
                    float f3 = EI.forcePropAOA(j / 3.6F, i, 1.0F, false);

                    // TODO: +++ Test new Mass Calculation
                    fm.M.requestFuel(0F);
                    Wing.G = fm.M.getFullMass() * Atmosphere.g();
                    // -----------------------------------

                    float f5 = Wing.getClimb(j / 3.6F, i, f3);
//                    System.out.println("100%% altitude " + i + "m EI.forcePropAOA = " + f3 + ", Wing.getClimb = " + f5);
                    if (f5 > f) {
                        f = f5;
                    }
                    if ((f5 < 0.0F) && (f5 < f)) {
                        f1 = j;
                        break;
                    }
                    j++;
                } while (true);
                f1 *= 0.982F; // TODO: Added by SAS~Storebror, for some reason IL-2 Compare tends to give slightly unachievable speed values otherwise
                if (f1 < 0.0F) {
                    printwriter.print("\t");
                } else {
                    printwriter.print(f1 + "\t");
                }
                printwriter.print((f * Wing.Vyfac) + "\t");
                printwriter.println();
            }

            printwriter.close();
        } catch (IOException ioexception) {
            System.out.println("File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
    }

    public static void DataToFile(Motor m, PrintWriter printwriter) {
        if (printwriter == null) {
            return;
        }
        printwriter.print("Rotate direction: ");
        if (Reflection.getInt(m, "propDirection") == 0) {
            printwriter.println("left");
        } else if (1 == Reflection.getInt(m, "propDirection")) {
            printwriter.println("right");
        }
        if ((Reflection.getInt(m, "type") == 0) || (Reflection.getInt(m, "type") == 1) || (Reflection.getInt(m, "type") == 7)) {
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
        for (int i = 0; i <= m.compressorMaxStep; i++) {
            printwriter.println("Compressor altitude: " + m.compressorAltitudes[i]);
        }

        printwriter.println("Autopropeller : " + (m.isAllowsAutoProp() ? "+" : "-"));
        printwriter.println("Autoradiator : " + (m.isAllowsAutoRadiator() ? "+" : "-"));
    }

    public static void drawGraphs(Polares p, String s, int fuel, boolean bDrawFuel) {
        float f = -10000F;
        if (!CmdPreload.fullDump) {
            try {
                File file = new File(HomePath.toFileSystemName(s + " Polares (" + CmdPreload.dumpDesc + ").txt", 0));
                file.getParentFile().mkdirs();
                PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s + " Polares (" + CmdPreload.dumpDesc + ").txt", 0))));
                for (int i = -90; i < 90; i++) {
                    printwriter.print(i + "\t");
                }

                printwriter.println();
                for (int i2 = 0; i2 <= 5; i2++) {
                    p.setFlaps(i2 * 0.2F);
                    for (int j = -90; j < 90; j++) {
                        printwriter.print(p.new_Cya(j) + "\t");
                    }

                    printwriter.println();
                    for (int k = -90; k < 90; k++) {
                        printwriter.print(p.new_Cxa(k) + "\t");
                    }

                    printwriter.println();
                    if (i2 == 0) {
                        for (int l = -90; l < 90; l++) {
                            float f9 = p.new_Cya(l) / p.new_Cxa(l);
                            printwriter.print((f9 * 0.1F) + "\t");
                            if (f < f9) {
                                f = f9;
                            }
                        }

                        printwriter.println();
                    }
                    for (int i1 = -90; i1 < 90; i1++) {
                        float f10 = Reflection.getFloat(p, "Cy0") + (p.lineCyCoeff * i1);
                        if ((f10 < 2D) && (f10 > -2D)) {
                            printwriter.print(f10 + "\t");
                        } else {
                            printwriter.print("\t");
                        }
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
                File file = new File(HomePath.toFileSystemName(s + " " + (100 - fuel) + "% fuel" + " (" + CmdPreload.dumpDesc + ")_turn.txt", 0));
                file.getParentFile().mkdirs();
                printwriter1 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s + " " + (100 - fuel) + "% fuel" + " (" + CmdPreload.dumpDesc + ")_turn.txt", 0))));
            } else {
                File file = new File(HomePath.toFileSystemName(s + " (" + CmdPreload.dumpDesc + ")_turn.txt", 0));
                file.getParentFile().mkdirs();
                printwriter1 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s + " (" + CmdPreload.dumpDesc + ")_turn.txt", 0))));
            }
            for (int j1 = 120; j1 < 620; j1 += 2) {
                printwriter1.print(j1 + "\t");
            }

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
                        float f11 = k1 * 0.27778F;
                        float f13 = p.S * Reflection.getFloat(p, "Ro") * f11 * f11;
                        float f15 = (2.0F * p.G) / f13;
                        float f17 = p.getAoAbyCy(f15);
                        float f19 = f17 - p.AOAMinCx;
                        float f21 = 0.5F * (Reflection.getFloat(p, "CxMin") + (Reflection.getFloat(p, "parabCxCoeff") * f19 * f19)) * f13;
                        float normP[] = (float[]) Reflection.getValue(p, "normP");
                        float f23 = (f11 * (normP[(int) f11] - f21)) / p.G;
                        if ((j2 == 0) && (f2 < f23)) {
                            f2 = f23;
                            f4 = f11;
                        }
                        if (f23 < -10F) {
                            printwriter1.print("\t");
                        } else {
                            printwriter1.print((f23 * p.Vyfac) + "\t");
                        }
                    }

                    printwriter1.println();
                }
                for (int l1 = 120; l1 < 620; l1 += 2) {
                    float f12 = l1 * 0.27778F;
                    float f14 = p.S * Reflection.getFloat(p, "R1000") * f12 * f12;
                    float maxP[] = (float[]) Reflection.getValue(p, "maxP");
                    float f16 = (2.0F * maxP[(int) f12]) / f14;
                    float f18 = (float) Math.sqrt((f16 - Reflection.getFloat(p, "CxMin")) / Reflection.getFloat(p, "parabCxCoeff"));
                    float f20 = p.AOAMinCx + f18;
                    if (f20 > p.AOACritH) {
                        f20 = p.AOACritH;
                    }
                    float f22 = (0.5F * p.new_Cya(f20) * f14) / p.G;
                    float f24 = (float) Math.sqrt((f22 * f22) - 1.0F);
                    float f25 = 0.0F;
                    if (f24 > 0.2F) {
                        f25 = (6.283185F * f12) / (9.8F * f24);
                    }
                    if (f25 > 40F) {
                        f25 = 0.0F;
                    }
                    if (f25 == 0.0F) {
                        printwriter1.print("\t");
                    } else {
                        printwriter1.print((f25 * p.Tfac) + "\t");
                    }
                    if ((j2 == 0) && (f25 > 0.0F) && (f6 > f25)) {
                        f6 = f25;
                        f8 = f12;
                    }
                }

                printwriter1.println();
            }

            printwriter1.println("M_takeoff:\t" + (p.G / 9.8F));
            printwriter1.println("K_max:\t" + f);
            printwriter1.println("T_turn:\t" + (f6 * p.Tfac));
            printwriter1.println("V_turn:\t" + (f8 * 3.6F));
            printwriter1.println("Vz_climb:\t" + (f2 * p.Vyfac));
            printwriter1.println("V_climb:\t" + (f4 * 3.6F));
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
                    if (weaponSlot[j] == null) {
                        continue;
                    }
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
                        if (weaponClass.equals(tmpClass)) {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                    try {
                        tmpClass = Class.forName("com.maddox.il2.objects.weapons.GunNull");
                        if (weaponClass.equals(tmpClass)) {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                    try {
                        tmpClass = Class.forName("com.maddox.il2.objects.weapons.MGunNull");
                        if (weaponClass.equals(tmpClass)) {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                    try {
                        tmpClass = Class.forName("com.maddox.il2.objects.weapons.MGunNullGeneric");
                        if (weaponClass.equals(tmpClass)) {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                    try {
                        tmpClass = Class.forName("com.maddox.il2.objects.weapons.RocketGunNull");
                        if (weaponClass.equals(tmpClass)) {
                            continue;
                        }
                    } catch (Exception e) {
                    }
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
                        if (weaponClass.equals(tmpClass)) {
                            continue;
                        }
                    } catch (Exception e) {
                    }
                    String weaponName = weaponClass.getName();
                    weaponName = CmdPreload.stripLeading(weaponName, "com.maddox.il2.objects.weapons.");
                    if (weaponName.startsWith("BombTorp")) {
                        weaponName = CmdPreload.stripLeading(weaponName, "BombTorp");
                        weaponName = CmdPreload.translateBombTorp(weaponName);
                    }
                    if (weaponName.startsWith("Bomb")) {
                        weaponName = CmdPreload.stripLeading(weaponName, "Bomb");
                        weaponName = CmdPreload.translateBomb(weaponName);
                    }
                    weaponName = CmdPreload.stripLeading(weaponName, "Cannon");
                    if (weaponName.startsWith("FuelTank_")) {
                        weaponName = CmdPreload.stripLeading(weaponName, "FuelTank_");
                        weaponName = CmdPreload.translateFuelTank(weaponName);
                    }
                    weaponName = CmdPreload.stripLeading(weaponName, "MMGun");
                    if (weaponName.startsWith("MGun")) {
                        weaponName = CmdPreload.stripLeading(weaponName, "MGun");
                        weaponName = CmdPreload.translateMGun(weaponName);
                    }
                    weaponName = CmdPreload.stripLeading(weaponName, "MachineGun");
                    weaponName = CmdPreload.stripLeading(weaponName, "Missile");
                    weaponName = CmdPreload.stripLeading(weaponName, "Pylon");
                    if (weaponName.startsWith("Rocket")) {
                        weaponName = CmdPreload.stripLeading(weaponName, "Rocket");
                        weaponName = CmdPreload.translateRocket(weaponName);
                    }
                    weaponName = CmdPreload.stripLeading(weaponName, "Torpedo");
                    CmdPreload.addWeaponSlot(weaponName, num, bullets, type, weaponSlotList);
                }

                boolean firstSlotPrinted = false;
                printwriter.print(weaponSlotClearName + ":");

                for (int j = 0; j <= WeaponSlot.TYPE_MAX; j++) {
                    for (int k = 0; k < weaponSlotList.size(); k++) {
                        WeaponSlot ws = (WeaponSlot) weaponSlotList.get(k);
                        if (ws.type != j) {
                            continue;
                        }
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
                if (!firstSlotPrinted) {
                    printwriter.print("nothing;");
                }
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
        if (fullString.startsWith(leadingString)) {
            return fullString.substring(leadingString.length());
        }
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
            if (bFound) {
                break;
            }
        }

        return sRetVal;
    }

    private static String translateMGun(String weaponName) {
        if (weaponName.startsWith("ADEN30")) {
            return "30mm Aden Cannon";
        }
        if (weaponName.startsWith("B20")) {
            return "20mm Berezin B-20";
        }
        if (weaponName.startsWith("BK37")) {
            return "BK 3.7 Cannon";
        }
        if (weaponName.startsWith("BK75")) {
            return "BK 7.5 Cannon";
        }
        if (weaponName.startsWith("Bofors40")) {
            return "4cm Bofors Cannon";
        }
        if (weaponName.startsWith("BredaSAFAT127")) {
            return "12.7mm Breda-SAFAT Heavy Gun";
        }
        if (weaponName.startsWith("BredaSAFAT77")) {
            return "7.7mm Breda-SAFAT Gun";
        }
        if (weaponName.startsWith("BredaSAFATSM127")) {
            return "12.7mm Breda-SAFAT Heavy Gun";
        }
        if (weaponName.startsWith("BredaSAFATSM77")) {
            return "7.7mm Breda-SAFAT Gun";
        }
        if (weaponName.startsWith("Browning303")) {
            return "Browning .303";
        }
        if (weaponName.startsWith("Browning50")) {
            return "Browning .50";
        }
        if (weaponName.startsWith("ColtMk12")) {
            return "20mm Colt-Browning MK 12 Cannon";
        }
        if (weaponName.startsWith("DA762")) {
            return "DA 7.62mm Gun";
        }
        if (weaponName.startsWith("HispanoMkI")) {
            return "20mm Hispano Mk. I Cannon";
        }
        if (weaponName.startsWith("Ho103")) {
            return "12.7mm Ho-103 Heavy Gun";
        }
        if (weaponName.startsWith("Ho115")) {
            return "30mm Ho-155 Cannon";
        }
        if (weaponName.startsWith("Ho5")) {
            return "20mm Ho-5 Cannon";
        }
        if (weaponName.startsWith("M4_75")) {
            return "75mm M4 Cannon";
        }
        if (weaponName.startsWith("M4")) {
            return "37mm M4 Cannon";
        }
        if (weaponName.startsWith("M9")) {
            return "37mm M9 Cannon";
        }
        if (weaponName.startsWith("MAC1934")) {
            return "7.5mm MAC 1934 Gun";
        }
        if (weaponName.startsWith("MG131")) {
            return "13mm MG 131 Heavy Gun";
        }
        if (weaponName.startsWith("MG15120")) {
            return "20mm MG 151 Cannon";
        }
        if (weaponName.startsWith("MG151")) {
            return "15mm MG 151 Heavy Gun";
        }
        if (weaponName.startsWith("MG15")) {
            return "7.92mm MG 15 Gun";
        }
        if (weaponName.startsWith("MG17")) {
            return "7.92mm MG 17 Gun";
        }
        if (weaponName.startsWith("MG213")) {
            return "20mm MG 213 Cannon";
        }
        if (weaponName.startsWith("MG81")) {
            return "7.92mm MG 81 Gun";
        }
        if (weaponName.startsWith("MGFF")) {
            return "20mm MG-FF Cannon";
        }
        if (weaponName.startsWith("MK101")) {
            return "30mm MK 101 Cannon";
        }
        if (weaponName.startsWith("MK103")) {
            return "30mm MK 103 Cannon";
        }
        if (weaponName.startsWith("MK108")) {
            return "30mm MK 108 Cannon";
        }
        if (weaponName.startsWith("MK213")) {
            return "30mm MK 213 Cannon";
        }
        if (weaponName.startsWith("MK214")) {
            return "50mm MK 214 Cannon";
        }
        if (weaponName.startsWith("Madsen20")) {
            return "20mm Madsen Cannon";
        }
        if (weaponName.startsWith("Mauser213")) {
            return "20mm MG 213 Cannon";
        }
        if (weaponName.startsWith("MiniGun")) {
            return "7.62mm M134 Minigun";
        }
        if (weaponName.startsWith("Molins_57")) {
            return "57mm Molins Cannon";
        }
        if (weaponName.startsWith("N37")) {
            return "37mm Nudelman Cannon";
        }
        if (weaponName.startsWith("N57")) {
            return "57mm Nudelman Cannon";
        }
        if (weaponName.startsWith("NR23")) {
            return "23mm Nudelman-Richter Cannon";
        }
        if (weaponName.startsWith("NS23")) {
            return "23mm Nudelman Cannon";
        }
        if (weaponName.startsWith("NS37")) {
            return "37mm Nudelman Cannon";
        }
        if (weaponName.startsWith("NS45")) {
            return "45mm Nudelman Cannon";
        }
        if (weaponName.startsWith("PV1")) {
            return "7.62mm PV-1 Gun";
        }
        if (weaponName.startsWith("PaK40")) {
            return "75mm PaK-40 Cannon";
        }
        if (weaponName.startsWith("Scotti127")) {
            return "12.7 mm Isotta-Fraschini Scotti Gun";
        }
        if (weaponName.startsWith("Sh37")) {
            return "37mm Ho-203 Cannon";
        }
        if (weaponName.startsWith("ShKAS")) {
            return "7.62mm ShKAS Gun";
        }
        if (weaponName.startsWith("ShVAK")) {
            return "20mm ShVAK Cannon";
        }
        if (weaponName.startsWith("Type2")) {
            return "13mm Type 2 Heavy Gun";
        }
        if (weaponName.startsWith("Type5")) {
            return "30mm Type 5 Cannon";
        }
        if (weaponName.startsWith("Type99")) {
            return "20mm Type 99 Cannon";
        }
        if (weaponName.startsWith("UB")) {
            return "12.7mm UB Heavy Gun";
        }
        if (weaponName.startsWith("VYa")) {
            return "23mm VYa Cannon";
        }
        if (weaponName.startsWith("Vickers40mm")) {
            return "40mm Vickers Cannon";
        }
        if (weaponName.startsWith("VikkersK")) {
            return "Vikkers .303";
        }
        if (weaponName.startsWith("Vz30")) {
            return "7.92mm vz.30 Gun";
        }
        return weaponName;
    }

    private static String translateBombTorp(String weaponName) {
        if (weaponName.startsWith("45_36")) {
            return "935kg 46-36 Torpedo";
        }
        if (weaponName.startsWith("Torp650")) {
            return "4.5ton Type 65 Torpedo";
        }
        if (weaponName.startsWith("F5B")) {
            return "765kg F5b Torpedo";
        }
        if (weaponName.startsWith("FFF")) {
            return "350kg Motobomba FFF Torpedo";
        }
        if (weaponName.startsWith("Fiume")) {
            return "1700kg Fiume Torpedo";
        }
        if (weaponName.startsWith("LTF5Practice")) {
            return "765kg F5b Practice Torpedo";
        }
        if (weaponName.startsWith("Mk12")) {
            return "702kg Mk. XII Torpedo";
        }
        if (weaponName.startsWith("Mk13")) {
            return "1005kg Mk. XIII Torpedo";
        }
        if (weaponName.startsWith("Mk34")) {
            return "522kg Mk. 34 Torpedo";
        }
        if (weaponName.startsWith("Type91")) {
            return "848kg Type 91 Torpedo";
        }
        return weaponName;
    }

    private static String translateBomb(String weaponName) {
        if (weaponName.startsWith("1000lbs")) {
            return "1000lbs Bomb";
        }
        if (weaponName.startsWith("100Lbs")) {
            return "100lbs Bomb";
        }
        if (weaponName.startsWith("100kg")) {
            return "100kg Bomb";
        }
        if (weaponName.startsWith("10kg")) {
            return "10kg Bomb";
        }
        if (weaponName.startsWith("110GalNapalm")) {
            return "110gal Napalm Canister";
        }
        if (weaponName.startsWith("12000Tallboy")) {
            return "12tons Tallboy Bomb";
        }
        if (weaponName.startsWith("154Napalm")) {
            return "154gal Napalm Canister";
        }
        if (weaponName.startsWith("15kg")) {
            return "15kg Bomb";
        }
        if (weaponName.startsWith("1600lbs")) {
            return "1600lbs Bomb";
        }
        if (weaponName.startsWith("175Napalm")) {
            return "175gal Napalm Canister";
        }
        if (weaponName.startsWith("2000lbs")) {
            return "2000lbs Bomb";
        }
        if (weaponName.startsWith("20kg")) {
            return "20kg Bomb";
        }
        if (weaponName.startsWith("250kg")) {
            return "250kg Bomb";
        }
        if (weaponName.startsWith("250lbs")) {
            return "250lbs Bomb";
        }
        if (weaponName.startsWith("25kg")) {
            return "25kg Bomb";
        }
        if (weaponName.startsWith("300lbs")) {
            return "300lbs Bomb";
        }
        if (weaponName.startsWith("30kg")) {
            return "30kg";
        }
        if (weaponName.startsWith("4512")) {
            return "935kg 45-12 Torpedo";
        }
        if (weaponName.startsWith("500kg")) {
            return "500kg Bomb";
        }
        if (weaponName.startsWith("500lbs")) {
            return "500lbs Bomb";
        }
        if (weaponName.startsWith("50kg")) {
            return "50kg Bomb";
        }
        if (weaponName.startsWith("5327")) {
            return "1710kg 53-27 Torpedo";
        }
        if (weaponName.startsWith("5339")) {
            return "1780kg 53-39 Torpedo";
        }
        if (weaponName.startsWith("600kg")) {
            return "600kg Bomb";
        }
        if (weaponName.startsWith("60kg")) {
            return "60kg Bomb";
        }
        if (weaponName.startsWith("750lbs")) {
            return "750lbs Bomb";
        }
        if (weaponName.startsWith("75GalNapalm") || weaponName.startsWith("75Napalm")) {
            return "75gal Napalm Canister";
        }
        if (weaponName.startsWith("800kg")) {
            return "800kg Bomb";
        }
        if (weaponName.startsWith("AB1000")) {
            return "1000kg Bomb Dispenser";
        }
        if (weaponName.startsWith("AB23")) {
            return "23kg Bomb Dispenser";
        }
        if (weaponName.startsWith("AB250")) {
            return "250kg Bomb Dispenser";
        }
        if (weaponName.startsWith("AB500")) {
            return "500kg Bomb Dispenser";
        }
        if (weaponName.startsWith("AO10")) {
            return "500kg Bomb Dispenser";
        }
        if (weaponName.startsWith("Ampoule")) {
            return "1.2kg Ampoule";
        }
        if (weaponName.startsWith("B22")) {
            return "2.2kg Bomblet";
        }
        if (weaponName.startsWith("Blu2Napalm")) {
            return "100gal Napalm Canister";
        }
        if (weaponName.startsWith("CBU24")) {
            return "800lbs Cluster Bomb";
        }
        if (weaponName.startsWith("CargoA")) {
            return "Cargo Container";
        }
        if (weaponName.startsWith("FAB1000")) {
            return "1000kg Bomb";
        }
        if (weaponName.startsWith("FAB100")) {
            return "100kg Bomb";
        }
        if (weaponName.startsWith("FAB2000")) {
            return "2000kg Bomb";
        }
        if (weaponName.startsWith("FAB250")) {
            return "250kg Bomb";
        }
        if (weaponName.startsWith("FAB5000")) {
            return "5000kg Bomb";
        }
        if (weaponName.startsWith("FAB500")) {
            return "500kg Bomb";
        }
        if (weaponName.startsWith("FAB50")) {
            return "50kg Bomb";
        }
        if (weaponName.startsWith("FatMan")) {
            return "10300lbs 21kt Fat Man Nuke";
        }
        if (weaponName.startsWith("HC4000")) {
            return "4000lbs High Capacity Bomb";
        }
        if (weaponName.startsWith("IT_100")) {
            return "100kg Bomb";
        }
        if (weaponName.startsWith("IT_250")) {
            return "250kg Bomb";
        }
        if (weaponName.startsWith("IT_500")) {
            return "500kg Bomb";
        }
        if (weaponName.startsWith("IT_50")) {
            return "50kg Bomb";
        }
        if (weaponName.startsWith("IT_630")) {
            return "630kg Bomb";
        }
        if (weaponName.startsWith("LittleBoy")) {
            return "9700lbs 15kt Little Boy Buke";
        }
        if (weaponName.startsWith("Mk12")) {
            return "1200lbs 14kt Nuke";
        }
        if (weaponName.startsWith("Mk24Flare")) {
            return "27lbs Flare";
        }
        if (weaponName.startsWith("Mk53Charge")) {
            return "325lbs Depth Charge";
        }
        if (weaponName.startsWith("Mk7")) {
            return "1680lbs 61kt Nuke";
        }
        if (weaponName.startsWith("Mk81")) {
            return "250lb Retarding Bomb";
        }
        if (weaponName.startsWith("Mk82")) {
            return "500lb Retarding Bomb";
        }
        if (weaponName.startsWith("Mk83")) {
            return "1000lb Retarding Bomb";
        }
        if (weaponName.startsWith("PC1600")) {
            return "1600kg Anti-Armor Bomb";
        }
        if (weaponName.startsWith("PTAB25")) {
            return "120kg Anti-Tank Cassette";
        }
        if (weaponName.startsWith("ParaFlare")) {
            return "8kg Flare on Parachute";
        }
        if (weaponName.startsWith("Parafrag8")) {
            return "10kg Anti-Personell Fragmentation Bomb on Parachute";
        }
        if (weaponName.startsWith("PhBall")) {
            return "0.5kg Phosphorous Ball";
        }
        if (weaponName.startsWith("PuW100")) {
            return "100kg Bomb";
        }
        if (weaponName.startsWith("PuW125")) {
            return "125kg Bomb";
        }
        if (weaponName.startsWith("PuW300")) {
            return "300kg Bomb";
        }
        if (weaponName.startsWith("PuW50")) {
            return "50kg Bomb";
        }
        if (weaponName.startsWith("SAB100")) {
            return "100kg Flare Bomb";
        }
        if (weaponName.startsWith("SAB15")) {
            return "15kg Flare Bomb";
        }
        if (weaponName.startsWith("SB1000")) {
            return "1000kg High Capacity Bomb";
        }
        if (weaponName.startsWith("SC1000")) {
            return "1000kg Bomb";
        }
        if (weaponName.startsWith("SC1800")) {
            return "1800kg Bomb";
        }
        if (weaponName.startsWith("SC2000")) {
            return "2000kg Bomb";
        }
        if (weaponName.startsWith("SC2500")) {
            return "2500kg Bomb";
        }
        if (weaponName.startsWith("SC250")) {
            return "250kg Bomb";
        }
        if (weaponName.startsWith("SC500")) {
            return "500kg Bomb";
        }
        if (weaponName.startsWith("SC50")) {
            return "50kg Bomb";
        }
        if (weaponName.startsWith("SC70")) {
            return "70kg Bomb";
        }
        if (weaponName.startsWith("SD1000")) {
            return "1000kg Semi-Armor-Piercing Bomb";
        }
        if (weaponName.startsWith("SD2A")) {
            return "2kg Bomblet";
        }
        if (weaponName.startsWith("SD4HL")) {
            return "4kg Bomblet";
        }
        if (weaponName.startsWith("SD500")) {
            return "500kg Semi-Armor-Piercing Bomb";
        }
        if (weaponName.startsWith("Spezzone")) {
            return "1kg Incendiary Bomb";
        }
        if (weaponName.startsWith("Starthilfe")) {
            return "RATO Unit";
        }
        if (weaponName.startsWith("TI")) {
            return "Target Indicator Container";
        }
        if (weaponName.startsWith("Ti")) {
            return "Target Indicator";
        }
        if (weaponName.startsWith("Type3AntiAir")) {
            return "20lb Anti-Aircraft Rocket";
        }
        if (weaponName.startsWith("WalterStarthilferakete")) {
            return "RATO Unit";
        }
        if (weaponName.startsWith("let2Kg")) {
            return "2kg Bomblet";
        }
        return weaponName;
    }

    private static String translateRocket(String weaponName) {
        if (weaponName.startsWith("132")) {
            return "50lbs RS-132 Rocket";
        }
        if (weaponName.startsWith("4andHalfInch")) {
            return "4.5'' M8 Rocket";
        }
        if (weaponName.startsWith("5inchZuni")) {
            return "5'' Zuni Rocket";
        }
        if (weaponName.startsWith("82")) {
            return "80lbs Rocket";
        }
        if (weaponName.startsWith("90")) {
            return "90lbs Rocket";
        }
        if (weaponName.startsWith("BRS132")) {
            return "50lbs BRS-132 Armor Piercing Rocket";
        }
        if (weaponName.startsWith("BRS82")) {
            return "15lbs BRS-82 Armor Piercing Rocket";
        }
        if (weaponName.startsWith("Bat")) {
            return "1600lb Bat Bomb";
        }
        if (weaponName.startsWith("FritzX")) {
            return "1400kg Guided Bomb";
        }
        if (weaponName.startsWith("H19")) {
            return "Rocket Launchers";
        }
        if (weaponName.startsWith("HS_293")) {
            return "1045kg Hs 293 Guided Missile";
        }
        if (weaponName.startsWith("HVAR2")) {
            return "2.5'' HVAR Rocket";
        }
        if (weaponName.startsWith("HVAR5AP")) {
            return "134lbs 5'' Armor Piercing HVAR Rocket";
        }
        if (weaponName.startsWith("HVAR5") || weaponName.startsWith("HVARF84")) {
            return "134lbs 5'' HVAR Rocket";
        }
        if (weaponName.startsWith("HYDRA")) {
            return "13.6lb FFAR Rocket";
        }
        if (weaponName.startsWith("LR130")) {
            return "LR-130 Rocket Launcher";
        }
        if (weaponName.startsWith("LR55")) {
            return "LR-55 Rocket Launcher";
        }
        if (weaponName.startsWith("M13")) {
            return "95lbs M-13 Rocket";
        }
        if (weaponName.startsWith("PB1")) {
            return "6.5kg Anti-Tank Rocket";
        }
        if (weaponName.startsWith("PB2")) {
            return "5.1kg Anti-Tank Rocket";
        }
        if (weaponName.startsWith("PC1000")) {
            return "1000kg Anti-Armor Rocket-Bomb";
        }
        if (weaponName.startsWith("PhBall")) {
            return "Phosphorous Ball";
        }
        if (weaponName.startsWith("R4M")) {
            return "3.85kg Anti-Aircraft Rocket";
        }
        if (weaponName.startsWith("ROFS132")) {
            return "50lbs ROFS-132 Fragmentation Rocket";
        }
        if (weaponName.startsWith("RS132")) {
            return "50lbs RS-132 Rocket";
        }
        if (weaponName.startsWith("RS82")) {
            return "15lbs RS-82 Rocket";
        }
        if (weaponName.startsWith("Razon")) {
            return "1000lbs Razon Guided Bomb";
        }
        if (weaponName.startsWith("S5")) {
            return "4kg S5 Rocket";
        }
        if (weaponName.startsWith("SURA_AP")) {
            return "11kg Armor Piercing SURA Rocket";
        }
        if (weaponName.startsWith("SURA_HE")) {
            return "11kg High Explosive SURA Rocket";
        }
        if (weaponName.startsWith("SimpleM13")) {
            return "95lbs M-13 Rocket";
        }
        if (weaponName.startsWith("SimpleRS132")) {
            return "50lbs RS-132 Rocket";
        }
        if (weaponName.startsWith("SimpleRS150")) {
            return "70lbs RS-150 Rocket";
        }
        if (weaponName.startsWith("SimpleRS82")) {
            return "15lbs RS-82 Rocket";
        }
        if (weaponName.startsWith("TinyTim")) {
            return "1255lbs Tiny Tim Rocket";
        }
        if (weaponName.startsWith("Type3Mk27")) {
            return "20lb Anti-Aircraft Rocket";
        }
        if (weaponName.startsWith("WPL5")) {
            return "Bazooka Rocket";
        }
        if (weaponName.startsWith("WPT6")) {
            return "Bazooka Rocket";
        }
        if (weaponName.startsWith("WfrGr21")) {
            return "112.6kg 21cm Anti-Aircraft Rocket Mortar";
        }
        if (weaponName.startsWith("X4")) {
            return "60kg X4 Guided Missile";
        }
        return weaponName;
    }

    private static String translateFuelTank(String weaponName) {
        if (weaponName.startsWith("200gal")) {
            return "200gal Tank";
        }
        if (weaponName.startsWith("Dag") || weaponName.startsWith("Ju87") || weaponName.startsWith("Nag") || weaponName.startsWith("Tank0Centre")) {
            return "300l Tank";
        }
        if (weaponName.startsWith("Tank0Underwing")) {
            return "150l Tank";
        }
        if (weaponName.startsWith("Tank0Wooden")) {
            return "300l Tank";
        }
        if (weaponName.startsWith("Tank0")) {
            return "300l Tank";
        }
        if (weaponName.startsWith("Tank100gal")) {
            return "100gal Tank";
        }
        if (weaponName.startsWith("Tank100i16")) {
            return "100l Tank";
        }
        if (weaponName.startsWith("Tank108gal")) {
            return "108gal Tank";
        }
        if (weaponName.startsWith("Tank110gal")) {
            return "110gal Tank";
        }
        if (weaponName.startsWith("Tank120")) {
            return "120l Tank";
        }
        if (weaponName.startsWith("Tank150gal")) {
            return "150gal Tank";
        }
        if (weaponName.startsWith("Tank154gal")) {
            return "154gal Tank";
        }
        if (weaponName.startsWith("Tank165gal")) {
            return "165gal Tank";
        }
        if (weaponName.startsWith("Tank178gal")) {
            return "178gal Tank";
        }
        if (weaponName.startsWith("Tank207gal")) {
            return "207gal Tank";
        }
        if (weaponName.startsWith("Tank230gal")) {
            return "230gal Tank";
        }
        if (weaponName.startsWith("Tank240")) {
            return "240l Tank";
        }
        if (weaponName.startsWith("Tank44gal")) {
            return "44gal Tank";
        }
        if (weaponName.startsWith("Tank50l")) {
            return "50l Tank";
        }
        if (weaponName.startsWith("Tank60gal")) {
            return "60gal Tank";
        }
        if (weaponName.startsWith("Tank75gal")) {
            return "75gal Tank";
        }
        if (weaponName.startsWith("Tank80")) {
            return "80l Tank";
        }
        if (weaponName.startsWith("Tank900")) {
            return "900l Tank";
        }
        if (weaponName.startsWith("TankA5M")) {
            return "160l Tank";
        }
        if (weaponName.startsWith("TankAD4")) {
            return "150gal Tank";
        }
        if (weaponName.startsWith("TankC120")) {
            return "120gal Combat Tank";
        }
        if (weaponName.startsWith("TankF4F")) {
            return "150gal Tank";
        }
        if (weaponName.startsWith("TankF4U")) {
            return "150gal Tank";
        }
        if (weaponName.startsWith("TankKi43Underwing")) {
            return "200l Tank";
        }
        if (weaponName.startsWith("TankKi44Underwing")) {
            return "100l Tank";
        }
        if (weaponName.startsWith("TankKi61Underwing")) {
            return "200l Tank";
        }
        if (weaponName.startsWith("TankKi84")) {
            return "150l Tank";
        }
        if (weaponName.startsWith("TankN1K1")) {
            return "300l Tank";
        }
        if (weaponName.startsWith("TankP38")) {
            return "75gal Tank";
        }
        if (weaponName.startsWith("TankSpit30")) {
            return "30gal Tank";
        }
        if (weaponName.startsWith("TankSpit45")) {
            return "45gal Tank";
        }
        if (weaponName.startsWith("TankSpit90")) {
            return "90gal Tank";
        }
        if (weaponName.startsWith("TankT6")) {
            return "20gal Tank";
        }
        if (weaponName.startsWith("TankTempest")) {
            return "90gal Tank";
        }
        if (weaponName.startsWith("Type_D")) {
            return "300l Tank";
        }
        return weaponName;
    }
    
    private void generateFbdjFiles() {
        boolean debugAirClassNames = false;
        boolean generateIL2PlaneLoadouts = false;
        boolean generateIl2objectsUpdates = false;
        boolean generateLoadoutUpdates = false;
        boolean generateIl2Air = false;
        boolean generateIl2Maps = false;
        boolean generateStationaryAircraft = false;
        String tableName = "fbdjstats";
        SectFile codIniSectfile = new SectFile("settings/fbdj.ini");
        if (codIniSectfile.sectionExist("Common")) {
            int commonSectionIndex = codIniSectfile.sectionIndex("Common");
            if (codIniSectfile.varExist(commonSectionIndex, "debugAirClassNames")) {
                debugAirClassNames = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "debugAirClassNames")).trim().equals("1");
                System.out.println("Debug Air Class Names set to: \"" + debugAirClassNames + "\"");
            }
            if (codIniSectfile.varExist(commonSectionIndex, "tableName")) {
                tableName = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "tableName")).trim();
                System.out.println("Table Name set to: \"" + tableName + "\"");
            }
            if (codIniSectfile.varExist(commonSectionIndex, "generateIL2PlaneLoadouts")) {
                generateIL2PlaneLoadouts = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "generateIL2PlaneLoadouts")).trim().equals("1");
                System.out.println("Generate IL2 Plane Loadouts set to: \"" + generateIL2PlaneLoadouts + "\"");
            }
            if (codIniSectfile.varExist(commonSectionIndex, "generateIl2objectsUpdates")) {
                generateIl2objectsUpdates = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "generateIl2objectsUpdates")).trim().equals("1");
                System.out.println("Generate Il2 objects Updates set to: \"" + generateIl2objectsUpdates + "\"");
            }
            if (codIniSectfile.varExist(commonSectionIndex, "generateLoadoutUpdates")) {
                generateLoadoutUpdates = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "generateLoadoutUpdates")).trim().equals("1");
                System.out.println("Generate Loadout Updates set to: \"" + generateLoadoutUpdates + "\"");
            }
            if (codIniSectfile.varExist(commonSectionIndex, "generateIl2Air")) {
                generateIl2Air = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "generateIl2Air")).trim().equals("1");
                System.out.println("Generate Il2 Air set to: \"" + generateIl2Air + "\"");
            }
            if (codIniSectfile.varExist(commonSectionIndex, "generateIl2Maps")) {
                generateIl2Maps = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "generateIl2Maps")).trim().equals("1");
                System.out.println("Generate Il2 Maps set to: \"" + generateIl2Maps + "\"");
            }
            if (codIniSectfile.varExist(commonSectionIndex, "generateStationaryAircraft")) {
                generateStationaryAircraft = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "generateStationaryAircraft")).trim().equals("1");
                System.out.println("Generate Il2 Maps set to: \"" + generateStationaryAircraft + "\"");
            }
        }

        SectFile airIniSectfile = new SectFile("com/maddox/il2/objects/air.ini");
        File fbdjDir = new File(HomePath.toFileSystemName("fbdj/", 0));
        if (!fbdjDir.exists()) {
            fbdjDir.mkdirs();
        }
        PrintWriter pwLoadoutsCsv = null;
        PrintWriter pwLoadoutsSql = null;
        PrintWriter pwObjectsSql = null;
        try {
            if (generateIL2PlaneLoadouts) pwLoadoutsCsv = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("fbdj/fbdjIL2PlaneLoadouts.csv", 0), false)));
            if (generateLoadoutUpdates) pwLoadoutsSql = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("fbdj/loadoutUpdates.sql", 0), false)));
            if (generateIl2objectsUpdates) pwObjectsSql = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("fbdj/il2objectsUpdates.sql", 0), false)));
            if (generateLoadoutUpdates) pwLoadoutsSql.println("use " + tableName + ";");
            if (generateLoadoutUpdates) pwLoadoutsSql.println("INSERT INTO planeLoadouts (planeName, weapons, weaponsDescription, sortieType, primaryAirForce) VALUES");
            if (generateIl2objectsUpdates) pwObjectsSql.println("use " + tableName + ";");
            if (generateIl2objectsUpdates) pwObjectsSql.println("INSERT INTO `il2Objects` (`objectType`,`il2ObjectName`,`displayName`,`pointValue`) VALUES");
            boolean firstLoadoutsSqlStatement = true;
            boolean firstObjectsSqlStatement = true;
            int sectionIndex = airIniSectfile.sectionIndex("AIR");
            int numVars = airIniSectfile.vars(sectionIndex);
                for (int varsIndex = 0; varsIndex < numVars; varsIndex++) {
                    String airplaneNameDef = airIniSectfile.var(sectionIndex, varsIndex);
                    if (airplaneNameDef.startsWith("*") || airplaneNameDef.startsWith("#") || airplaneNameDef.startsWith("//")) continue;
                    String airplaneNameI18N = I18N.plane(airplaneNameDef);
                    NumberTokenizer numbertokenizer = new NumberTokenizer(airIniSectfile.value(sectionIndex, varsIndex));
                    String airplaneName = numbertokenizer.next((String) null);
                    Class airplaneClass = null;
                    try {
                        airplaneClass = ObjIO.classForName(airplaneName);
                    } catch (Exception exception) {
                        System.out.println("Class '" + airplaneName + "' not found");
                        continue;
                    }
                    if (debugAirClassNames) {
                        System.out.println("Class '" + airplaneName + "'...");
                    }

                    if (!airplaneName.startsWith("air.")) {
                        System.out.println("###ERROR### " + airplaneName + " is not inside air. package! ###ERROR###");
                        continue;
                    }
                    airplaneName = airplaneName.substring(4);
                    if (airplaneName.toLowerCase().startsWith("placeholder")) {
                        continue;
                    }
                    if (airplaneNameDef.equals(airplaneNameI18N)) {
                        System.out.println("###WARNING### " + airplaneNameDef + " has no readable name in plane.properties ###WARNING###");
                    }

                    String[] loadoutArray = Aircraft.getWeaponsRegistered(airplaneClass);

                    try {
                        if (firstObjectsSqlStatement) {
                            firstObjectsSqlStatement = false;
                        } else {
                            if (generateIl2objectsUpdates) pwObjectsSql.println(",");
                        }
                        if (generateIl2objectsUpdates) {
                            pwObjectsSql.println("(\"Plane\",\"" + airplaneNameDef + "\",\"" + airplaneNameI18N + "\",0),");
                            pwObjectsSql.print("(\"Plane\",\"" + airplaneName + "\",\"" + airplaneNameI18N + "\",0)");
                        }
                        for (int loadoutIndex = 0; loadoutIndex < loadoutArray.length; loadoutIndex++) {
                            Aircraft._WeaponSlot[] weaponSlotArray = Aircraft.getWeaponSlotsRegistered(airplaneClass, loadoutArray[loadoutIndex]);
                            if (weaponSlotArray == null) {
                                System.out.println(airplaneName + " loadout " + loadoutArray[loadoutIndex] + " isn't a registered slot on this aircraft class!");
                                continue;
                            }
                            String weaponsNameI18N = I18N.weapons(airplaneNameDef, loadoutArray[loadoutIndex]);
                            if (loadoutArray[loadoutIndex].equals(weaponsNameI18N)) {
                                System.out.println("###WARNING### " + airplaneNameDef + " loadout " + weaponsNameI18N + " has no readable name in weapons.properties ###WARNING###");
                            }
                            char[] check = weaponsNameI18N.toCharArray();
                            StringBuffer weaponsNameI18NSql = new StringBuffer();
                            StringBuffer weaponsNameI18NCsv = new StringBuffer();
                            for(int i=0; i < check.length; i++){
                                if(check[i] == '\"'){
                                    weaponsNameI18NSql.append("\\\"");
                                    weaponsNameI18NCsv.append("\"\"");
                                }
                                else{
                                    weaponsNameI18NSql.append(check[i]);
                                    weaponsNameI18NCsv.append(check[i]);
                                }
                            }
                            if (generateIL2PlaneLoadouts) pwLoadoutsCsv.println("\"" + airplaneNameDef + "\",\"" + loadoutArray[loadoutIndex] + "\",\"" + weaponsNameI18NCsv + "\"");
                            boolean isBomber = false;
                            boolean allEmpty = true;
                            for (int slotIndex = 0; slotIndex < weaponSlotArray.length; slotIndex++) {
                                if (weaponSlotArray[slotIndex] != null) {
                                    if (weaponSlotArray[slotIndex].trigger == 3) isBomber = true;
                                    allEmpty = false;
                                }
                                if (isBomber && !allEmpty) break;
                            }
                            String country = fbdjAircraftCountry(airplaneClass);
                            String type = allEmpty?"Recon":isBomber?"Bomber":"Fighter";
                            if (firstLoadoutsSqlStatement) {
                                firstLoadoutsSqlStatement = false;
                            } else {
                                if (generateLoadoutUpdates) pwLoadoutsSql.println(",");
                            }
                            if (generateLoadoutUpdates) pwLoadoutsSql.print("(\"" + airplaneNameDef + "\",\"" + loadoutArray[loadoutIndex] + "\",\"" + weaponsNameI18NSql + "\",\"" + type + "\",\"" + country + "\")");
                            
                        }
                    } catch (Exception e) {
                        System.out.println("Error on Aircraft " + airplaneName);
                        e.printStackTrace();
                    }
                }
        } catch (Exception oe) {
            oe.printStackTrace();
        } finally {
            if (pwLoadoutsSql != null) {
                pwLoadoutsSql.println();
                pwLoadoutsSql.println("ON DUPLICATE KEY UPDATE weaponsDescription=VALUES(weaponsDescription), sortieType=VALUES(sortieType), primaryAirForce=VALUES(primaryAirForce);");
                pwLoadoutsSql.close();
            }
            if (pwLoadoutsCsv != null) {
                pwLoadoutsCsv.close();
            }
            if (pwObjectsSql != null) {
                pwObjectsSql.println();
                pwObjectsSql.println("ON DUPLICATE KEY UPDATE displayName=VALUES(displayName), pointValue=VALUES(pointValue);");
                pwObjectsSql.close();
            }
        }
        
        if (generateIl2Air) {
            PrintWriter pwAirTxt = null;
            BufferedReader brAirIni = null;
            try {
                pwAirTxt = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("fbdj/IL2air.txt", 0), false)));
                brAirIni = new BufferedReader(new SFSReader("com/maddox/il2/objects/air.ini"));
                String line = brAirIni.readLine();
                while(line != null) {
                    line = line.trim();
                    if (line.length() > 0 && !(line.startsWith("*") || line.startsWith("#") || line.startsWith("//"))) {
                        pwAirTxt.println(line);
                    }
                    line = brAirIni.readLine();
                }
            } catch (Exception oe) {
                oe.printStackTrace();
            } finally {
                if (pwAirTxt != null) {
                    pwAirTxt.close();
                }
                if (brAirIni != null) {
                    try {
                        brAirIni.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        if (generateStationaryAircraft) {
            SectFile stationaryIniSectfile = new SectFile("com/maddox/il2/objects/stationary.ini", 0);
            PrintWriter pwStationaryCsv = null;
            try {
                pwStationaryCsv = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("fbdj/fbdjIL2StaticObjects.csv", 0), false)));
                int sectionIndex = stationaryIniSectfile.sectionIndex("StationaryPlanes");
                if (sectionIndex >= 0) {
                    int numVars = stationaryIniSectfile.vars(sectionIndex);
                    for (int varIndex = 0; varIndex < numVars; varIndex++) {
                        String s = stationaryIniSectfile.value(sectionIndex, varIndex);
                        StringTokenizer st = new StringTokenizer(s);
                        if (st.hasMoreTokens()) {
                            String nextToken = st.nextToken();
//                            System.out.println("nextToken=" + nextToken);
//                            Object o = Spawn.get_WithSoftClass("com.maddox.il2.objects." + nextToken);
                            PlaneGeneric.SPAWN spawn = (PlaneGeneric.SPAWN)Spawn.get_WithSoftClass("com.maddox.il2.objects." + nextToken);
                            try {
//                                Class airplaneClass = Property.classValue((Class)o, "airClass");
//                                Class airplaneClass = o.getClass();
                                Class airplaneClass = spawn.proper.clazz;
                                String keyName = Property.stringValue(airplaneClass, "keyName");
                                String airplaneNameI18N = I18N.plane(keyName);
                                if (debugAirClassNames) {
                                    System.out.println("Class '" + airplaneClass.getName() + "' keyName='" + keyName + "' airplaneNameI18N='" + airplaneNameI18N + "' nextToken='" + nextToken + "'...");
                                }
                                pwStationaryCsv.println("\"Plane\",\"" + airplaneClass.getName().substring(27) + "\",\"" + airplaneNameI18N + "\",0");
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                System.out.println("Class for '" + s + "' not found");
                                continue;
                            }

                        }
                    }
                }
            } catch (Exception oe) {
                oe.printStackTrace();
            } finally {
                if (pwStationaryCsv != null) {
                    pwStationaryCsv.close();
                }
            }
            
        }

        if (generateIl2Maps) {
            SectFile mapsAllIniSectfile = new SectFile("maps/all.ini", 0);
            PrintWriter pwIl2MapsTxt = null;
            String curMapFile = Reflection.getString(World.land(), "MapName");
            try {
                pwIl2MapsTxt = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("fbdj/IL2Maps.txt", 0), false)));
                int sectionIndex = mapsAllIniSectfile.sectionIndex("all");
                if (sectionIndex >= 0) {
                    int numVars = mapsAllIniSectfile.vars(sectionIndex);
                    for (int varIndex = 0; varIndex < numVars; varIndex++) {
                        Land land = new Land();
                        land.indx = varIndex;
                        land.keyName = mapsAllIniSectfile.var(sectionIndex, varIndex);
                        if (land.keyName.startsWith("*") || land.keyName.startsWith("#") || land.keyName.startsWith("/") || land.keyName.startsWith("-") || land.keyName.startsWith("+")) continue;
                        land.fileName = mapsAllIniSectfile.value(sectionIndex, varIndex);
                        land.dirName = land.fileName.substring(0, land.fileName.lastIndexOf("/"));
                        land.i18nName = I18N.map(land.keyName);
                        try {
                            new SFSInputStream("maps/" + land.fileName).close();
                        } catch (Throwable t) {
                            System.out.println("### Coudln't find map " + land.i18nName + " load.ini file " + land.fileName + "!!!###");
                        }
                        World.land().UnLoadMap();
                        World.land().LoadMap(land.fileName, null);
                        float mapsize = World.land().getSizeX() / 1000F;
                        System.out.println("Map " + land.i18nName + " (" + land.fileName + ") x size = " + mapsize);
                        String fileName = land.fileName;
                        if (fileName.toLowerCase().endsWith(".ini")) fileName = fileName.substring(0, fileName.length() - 4);
                        pwIl2MapsTxt.print(fileName);
                        pwIl2MapsTxt.print(",0,0,");
                        pwIl2MapsTxt.print(mapsize > 260F?"Large,\"":"Small,\"");
                        pwIl2MapsTxt.print(land.i18nName);
                        pwIl2MapsTxt.println("\"");
                        pwIl2MapsTxt.flush();
                    }
                }
                if (curMapFile != null && curMapFile.length() > 0) {
                    World.land().UnLoadMap();
                    World.land().LoadMap(curMapFile, null);
                }
            } catch (Exception oe) {
                oe.printStackTrace();
            } finally {
                if (pwIl2MapsTxt != null) {
                    pwIl2MapsTxt.close();
                }
            }
        }

    }
    
    private String fbdjAircraftCountry(Class airclass) {
        String origin = Property.stringValue(airclass, "originCountry");
        if (origin.equalsIgnoreCase(PaintScheme.countryBritain)) return "RAF";
        if (origin.equalsIgnoreCase(PaintScheme.countryFinland)) return "Finland";
        if (origin.equalsIgnoreCase(PaintScheme.countryFrance)) return "France";
        if (origin.equalsIgnoreCase(PaintScheme.countryGermany)) return "Germany";
        if (origin.equalsIgnoreCase(PaintScheme.countryHungary)) return "Hungary";
        if (origin.equalsIgnoreCase(PaintScheme.countryItaly)) return "Italy";
        if (origin.equalsIgnoreCase(PaintScheme.countryJapan)) return "IJA";
        if (origin.equalsIgnoreCase(PaintScheme.countryNetherlands)) return "Netherlands";
        if (origin.equalsIgnoreCase(PaintScheme.countryNewZealand)) return "NewZealand";
        if (origin.equalsIgnoreCase(PaintScheme.countryPoland)) return "Poland";
        if (origin.equalsIgnoreCase(PaintScheme.countryRomania)) return "Romania";
        if (origin.equalsIgnoreCase(PaintScheme.countryRussia)) return "USSR";
        if (origin.equalsIgnoreCase(PaintScheme.countrySlovakia)) return "Slovakia";
        if (origin.equalsIgnoreCase(PaintScheme.countryUSA)) return "USAAF";
        return "Unknown";       
    }
    
    
    
    public void dumpFmFiles() {
        SectFile airIniSectfile = new SectFile("com/maddox/il2/objects/air.ini");
        int maxCrewFunction = 0;
        int maxFlapStage = 0;
        int maxEngine = 0;
        int maxVarWingStage = 0;
        
        try {
            PrintWriter pwErrors = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("FlightModels/errors.txt", 0))));
            for (int sectionIndex = 0; sectionIndex < airIniSectfile.sections(); sectionIndex++) {
                for (int varsIndex = 0; varsIndex < airIniSectfile.vars(sectionIndex); varsIndex++) {
                    NumberTokenizer numbertokenizer = new NumberTokenizer(airIniSectfile.value(sectionIndex, varsIndex));
                    String airplaneName = numbertokenizer.next((String) null);
                    Class airplaneClass = null;
                    try {
                        airplaneClass = ObjIO.classForName(airplaneName);
                    } catch (Exception exception) {
                        System.out.println("Class '" + airplaneName + "' not found");
                        continue;
                    }
                    if (!airplaneName.startsWith("air.")) {
                        airplaneName = "###ERROR### " + airplaneName + " ###ERROR###";
                        pwErrors.println(airplaneName);
                    }
                    airplaneName = airplaneName.substring(4);
                    if (airplaneName.toLowerCase().startsWith("placeholder")) {
                        continue;
                    }
                    String aircraftFmFile = Property.stringValue(airplaneClass, "FlightModel", null);
                    SectFile aircraftFmSectFile = FlightModelMain.sectFile(aircraftFmFile);
                    
                    int crewFunction = 0;
                    int flapStage = 0;
                    int engine = 0;
                    int varWingStage = 0;
                    
                    for (int i=0; i<100; i++) {
                        if (aircraftFmSectFile.get("Aircraft", "CrewFunction" + i, -1) != -1) crewFunction = i;
                    }
                    
                    for (int i=0; i<100; i++) {
                        if (aircraftFmSectFile.get("Controls", "CFlapStage" + i, -1.0F) != -1.0F) flapStage = i;
                    }
                    
                    for (int i=0; i<100; i++) {
                        if (aircraftFmSectFile.get("Controls", "CVarWingStage" + i, -1.0F) != -1.0F) varWingStage = i;
                    }
                    
                    for (int i=0; i<100; i++) {
                        if (aircraftFmSectFile.get("Engine", "Engine" + i + "Family", "").length() > 0) engine = i;
                    }
                    
                    if (crewFunction > maxCrewFunction) maxCrewFunction = crewFunction;
                    if (flapStage > maxFlapStage) maxFlapStage = flapStage;
                    if (engine > maxEngine) maxEngine = engine;
                    if (varWingStage > maxVarWingStage) maxVarWingStage = varWingStage;
                }
            }
            pwErrors.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String[] aircraft1 = {"Type", "Crew", "Wingspan", "Length", "Seaplane", "Canard", "Jet", "JetHiV"};
        String[] aircraft2 = {"FormationDefault", "FormationOffset", "FormationScaleCoeff"};
        String[] aircraftValues = new String[aircraft1.length + aircraft2.length + maxCrewFunction];
        int aircraftValueIndex = 0;
        for (int i=0; i<aircraft1.length; i++) aircraftValues[aircraftValueIndex++] = aircraft1[i];
        for (int i=0; i<maxCrewFunction; i++) aircraftValues[aircraftValueIndex++] = "CrewFunction" + i;
        for (int i=0; i<aircraft2.length; i++) aircraftValues[aircraftValueIndex++] = aircraft2[i];
        
        String[] controls1 = {"CAileron", "CAileronTrim", "CElevator", "CElevatorTrim", "CRudder", "CRudderTrim", "CFlap", "CFlapPos"};
        String[] controls2 = {"CFlapStageMax", "CDiveBrake", "CInterceptor", "CEngine", "CVectoredThrust", "CUndercarriage", "GearPeriod", "WingPeriod", "CockpitDoorPeriod", "AirBrakePeriod", "CLockTailwheel", "CStabilizer", "CArrestorHook", "CWingFold", "CCockpitDoor", "CBayDoors", "CBombBay", "CAileronThreshold", "CRudderThreshold", "CElevatorThreshold", "DefaultAileronTrim", "DefaultElevatorTrim", "DefaultRudderTrim", "CFlapTakeoffGroundRatio", "CFlapTakeoffCarrierRatio", "CTargetPitchDegreeAITakeoffRotation", "CTargetPitchDegreeAITakeoffClimb", "CTargetPitchDegreeAIApproach", "AILandingWPDownwindSpd", "AILandingWPGearDown", "AILandingWPHookDown", "AILandingWPFlapFullDown", "AILandingWPFlapHalfDown", "AILandingWP360OverHeadApproach", "AILandingWP360PitchPlus", "CLimitModeOfAilerons", "CLimitThresholdSpeedKMH", "CLimitRatioAileron", "CLimitRatioElevatorPlus", "CLimitRatioElevatorMinus", "CLimitRatioElevatorMinusALWAYS", "CLimitRatioAITakeoffElevatorPlus", "CLimitRatioRudder", "CFlapStageText", "CFlapBlown", "CVarWing", "CVarWingPos", "CVarWingStageMax"};
        String[] controls3 = {"CVarWingStageText", "CVarIncidence", "CCatLaunchBar", "CRefuel", "CDragChute", "CDiffBrake", "cElectricProp", "OnlineCockpitDoor", "OnlineArrestorHook", "OnlineWingFold", "OnlineBombBay"};
        String[] controlsValues = new String[controls1.length + controls2.length + controls3.length + maxFlapStage + maxVarWingStage];
        int controlsValueIndex = 0;
        for (int i=0; i<controls1.length; i++) controlsValues[controlsValueIndex++] = controls1[i];
        for (int i=0; i<maxFlapStage; i++) controlsValues[controlsValueIndex++] = "CFlapStage" + i;
        for (int i=0; i<controls2.length; i++) controlsValues[controlsValueIndex++] = controls2[i];
        for (int i=0; i<maxVarWingStage; i++) controlsValues[controlsValueIndex++] = "CVarWingStage" + i;
        for (int i=0; i<controls3.length; i++) controlsValues[controlsValueIndex++] = controls3[i];
        
        String[] engineValues = new String[maxEngine * 11];
        int engineValueIndex = 0;
        for (int i=0; i<maxEngine; i++) {
            engineValues[engineValueIndex++] = "Engine" + i + "Family";
            engineValues[engineValueIndex++] = "Engine" + i + "SubModel";
        }
        for (int i=0; i<maxEngine; i++) {
            engineValues[engineValueIndex++] = "Position" + i + "x";
            engineValues[engineValueIndex++] = "Position" + i + "y";
            engineValues[engineValueIndex++] = "Position" + i + "z";
            engineValues[engineValueIndex++] = "PropPosition" + i + "x";
            engineValues[engineValueIndex++] = "PropPosition" + i + "y";
            engineValues[engineValueIndex++] = "PropPosition" + i + "z";
            engineValues[engineValueIndex++] = "Vector" + i + "x";
            engineValues[engineValueIndex++] = "Vector" + i + "y";
            engineValues[engineValueIndex++] = "Vector" + i + "z";
        }
        
        
        String[] sections = {"Aircraft", "Mass", "MassFactors", "Controls", "Squares", "Toughness", "Arm", "Engine", "Gear", "Params", "Polares", "SOUND"};
        String[][] values = {
                aircraftValues,
                {"Empty", "TakeOff", "Oil", "Fuel", "Nitro"},
                {"MassFactor", "Altitude", "FuelFactor", "Altitude", "ParasiteFactor", "Altitude"},
                controlsValues,
                {"Wing", "Aileron", "Flap", "Stabilizer", "Elevator", "Keel", "Rudder", "Wing_In", "Wing_Mid", "Wing_Out", "FuselageCxS", "AirbrakeCxS"},
                {"AroneL", "AroneR", "CF", "Engine1", "Engine2", "Engine3", "Engine4", "GearL2", "GearR2", "Keel1", "Keel2", "Nose", "Oil", "Rudder1", "Rudder2", "StabL", "StabR", "Tail1", "Tail2", "Turret1B", "Turret2B", "Turret3B", "Turret4B", "Turret5B", "Turret6B", "VatorL", "VatorR", "WingLIn", "WingLMid", "WingLOut", "WingRIn", "WingRMid", "WingROut", "Flap01", "Flap02", "Flap03", "Flap04"},
                {"Aileron", "Flap", "Stabilizer", "Keel", "Elevator", "Rudder", "Wing_In", "Wing_Mid", "Wing_Out", "Wing_V", "GCenter", "GCenterZ", "GC_AOA_Shift", "GC_Flaps_Shift", "GC_Gear_Shift"},
                engineValues,
                {"SpringsStiffness", "TailStiffness", "ShockAbsorber", "SinkFactor", "FromIni", "H", "Pitch", "WaterClipList", "WheelDiameterLR", "WheelDiameterC", "NoseGearMaxSteer", "CatGearOffsetX", "CatGearMesh", "ChockLOffsetX", "ChockLOffsetY", "ChockROffsetX", "ChockROffsetY", "ChockCOffsetX", "ChockCOffsetY", "ChockLMesh", "ChockRMesh", "ChockCMesh", "ChockCarrierLMesh", "ChockCarrierRMesh", "ChockCarrierCMesh"},
                {"CriticalAOA", "CriticalCy", "CriticalAOAFlap", "CriticalCyFlap", "SpinTailAlpha", "SpinCxLoss", "SpinCyLoss", "Vmin", "Vmax", "VmaxAllowed", "VmaxH", "HofVmax", "VminFLAPS", "VmaxFLAPS", "VmaxGEAR", "VjamFLAPS", "Vlanding", "VtakeoffRot", "VminAI", "Vz_climb", "V_climb", "T_turn", "V_turn", "K_max", "Cy0_max", "FlapsMult", "FlapsAngSh", "G_CLASS_COEFF", "G_CLASS", "Range", "CruiseSpeed", "SensYaw", "SensPitch", "SensRoll", "ReferenceWeight"},
                {"lineCyCoeff", "AOAMinCx_Shift", "Cy0_0", "AOACritH_0", "AOACritL_0", "CyCritH_0", "CyCritL_0", "CxMin_0", "parabCxCoeff_0", "Cy0_1", "AOACritH_1", "AOACritL_1", "CyCritH_1", "CyCritL_1", "CxMin_1", "parabCxCoeff_1", "parabAngle", "Decline", "maxDistAng", "draw_graphs", "mc3", "mc4", "mm", "mz", "CyBFlap", "ThrustBFlap"},
                {"Engine"}
        };
        
        File loadoutsDir = new File(HomePath.toFileSystemName("FlightModels", 0));
        if (!loadoutsDir.exists()) {
            loadoutsDir.mkdirs();
        }
        try {
            PrintWriter pwErrors = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("FlightModels/errors.txt", 0))));
            PrintWriter pwFmd = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("FlightModels/fmd.csv", 0))));
            pwFmd.print("\"\",\"\",\"\"");
            for (int i=0; i<sections.length;i++) {
                for (int j=0;j<values[i].length;j++) {
                    pwFmd.print(",\"" + sections[i] + "\"");
                }
            }
            pwFmd.println();
            pwFmd.print("\"Aircraft Name\",\"fmd File\",\"DiffFM File\"");
            for (int i=0; i<sections.length;i++) {
                for (int j=0;j<values[i].length;j++) {
                    pwFmd.print(",\"" + values[i][j] + "\"");
                }
            }
            pwFmd.println();
            
            for (int sectionIndex = 0; sectionIndex < airIniSectfile.sections(); sectionIndex++) {
                for (int varsIndex = 0; varsIndex < airIniSectfile.vars(sectionIndex); varsIndex++) {
                    NumberTokenizer numbertokenizer = new NumberTokenizer(airIniSectfile.value(sectionIndex, varsIndex));
                    String airplaneName = numbertokenizer.next((String) null);
                    Class airplaneClass = null;
                    try {
                        airplaneClass = ObjIO.classForName(airplaneName);
                    } catch (Exception exception) {
                        System.out.println("Class '" + airplaneName + "' not found");
                        continue;
                    }
                    if (!airplaneName.startsWith("air.")) {
                        airplaneName = "###ERROR### " + airplaneName + " ###ERROR###";
                        pwErrors.println(airplaneName);
                    }
                    airplaneName = airplaneName.substring(4);
                    if (airplaneName.toLowerCase().startsWith("placeholder")) {
                        continue;
                    }
                    String aircraftFmFile = Property.stringValue(airplaneClass, "FlightModel", null);
                    SectFile aircraftFmSectFile = FlightModelMain.sectFile(aircraftFmFile);
                    loadMassFactors(aircraftFmSectFile);
                    if (aircraftFmFile.toLowerCase().startsWith("flightmodels/")) aircraftFmFile = aircraftFmFile.substring(13);
                    final String airClassKeyName = Property.stringValue(airplaneClass, "keyName");
                    final String airClassReadableName = I18N.plane(airClassKeyName);
                    int diffFmSeparatorIndex = aircraftFmFile.indexOf(":");
                    pwFmd.print("\""+airClassReadableName+"\"");
//                    pwFmd.print(",\"" +  + "\"");
                    if (diffFmSeparatorIndex > -1) {
                        pwFmd.print(",\"" + aircraftFmFile.substring(0, diffFmSeparatorIndex) + "\"");
                        pwFmd.print(",\"" + aircraftFmFile.substring(diffFmSeparatorIndex + 1) + "\"");
                    } else {
                        pwFmd.print(",\"" + aircraftFmFile + "\"");
                        pwFmd.print(",\"\"");
                    }
                    for (int i=0; i<sections.length;i++) {
                        if (sections[i].equals("MassFactors")) {
                            String theMassFactors = "";
                            String theMassFactorAltitudes = "";
                            String theParasiteFactors = "";
                            String theParasiteFactorAltitudes = "";
                            String theFuelFactors = "";
                            String theFuelFactorAltitudes = "";
                            
                            int massFactorsSectionIndex = aircraftFmSectFile.sectionIndex("MassFactors");
                            if (massFactorsSectionIndex != -1 && aircraftFmSectFile.vars(massFactorsSectionIndex) > 0) {
                                if (massFactors == null) {
                                    theMassFactors = "" + massFactor;
                                } else {
                                    for (int j=0; j<massFactors.length;j++) {
                                        theMassFactors += (j==0?"":",") + massFactors[j];
                                    }
                                }
                                if (massFactorAltitudes != null) {
                                    for (int j=0; j<massFactorAltitudes.length;j++) {
                                        theMassFactorAltitudes += (j==0?"":",") + massFactorAltitudes[j];
                                    }
                                }
                                if (parasiteFactors == null) {
                                    theParasiteFactors = "" + parasiteFactor;
                                } else {
                                    for (int j=0; j<parasiteFactors.length;j++) {
                                        theParasiteFactors += (j==0?"":",") + parasiteFactors[j];
                                    }
                                }
                                if (parasiteFactorAltitudes != null) {
                                    for (int j=0; j<parasiteFactorAltitudes.length;j++) {
                                        theParasiteFactorAltitudes += (j==0?"":",") + parasiteFactorAltitudes[j];
                                    }
                                }
                                if (fuelFactors == null) {
                                    theFuelFactors = "" + fuelFactor;
                                } else {
                                    for (int j=0; j<fuelFactors.length;j++) {
                                        theFuelFactors += (j==0?"":",") + fuelFactors[j];
                                    }
                                }
                                if (fuelFactorAltitudes != null) {
                                    for (int j=0; j<fuelFactorAltitudes.length;j++) {
                                        theFuelFactorAltitudes += (j==0?"":",") + fuelFactorAltitudes[j];
                                    }
                                }
                                pwFmd.print(",\"" + theMassFactors + "\"");
                                pwFmd.print(",\"" + theMassFactorAltitudes + "\"");
                                pwFmd.print(",\"" + theFuelFactors + "\"");
                                pwFmd.print(",\"" + theFuelFactorAltitudes + "\"");
                                pwFmd.print(",\"" + theParasiteFactors + "\"");
                                pwFmd.print(",\"" + theParasiteFactorAltitudes + "\"");
                                
                            } else {
                                for (int j=0; j<6;j++) pwFmd.print(",\"\"");
                            }
                        } else {
                            for (int j=0;j<values[i].length;j++) {
                                pwFmd.print(",\"" + aircraftFmSectFile.get(sections[i], values[i][j], "") + "\"");
                            }
                        }
                    }
                    pwFmd.println();
                }
            }
            pwErrors.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadMassFactors(SectFile sectfile) {
        massFactor = 1F;
        massFactors = null;
        massFactorAltitudes = null;
        parasiteFactor = 1F;
        parasiteFactors = null;
        parasiteFactorAltitudes = null;
        fuelFactor = 1F;
        fuelFactors = null;
        fuelFactorAltitudes = null;
        
        int sectionIndex = sectfile.sectionIndex("MassFactors");
        if (sectionIndex == -1)
            return;
        ArrayList massFactorList = new ArrayList();
        ArrayList massFactorAltitudeList = new ArrayList();
        ArrayList parasiteFactorList = new ArrayList();
        ArrayList parasiteFactorAltitudeList = new ArrayList();
        ArrayList fuelFactorList = new ArrayList();
        ArrayList fuelFactorAltitudeList = new ArrayList();
        ArrayList varNames = new ArrayList(Arrays.asList(new String[] { "MassFactor", "ParasiteFactor", "FuelFactor" }));
        int vars = sectfile.vars(sectionIndex);
        for (int var = 0; var < vars; var++) {
            try {
                String varName = sectfile.var(sectionIndex, var);
                if (varNames.contains(varName)) {
                    float factor = parseFloat(sectfile.value(sectionIndex, var), Float.NaN);
                    float alt = Float.NaN;
                    if (!Float.isNaN(factor)) {
                        if (var + 1 < vars) {
                            String var2Name = sectfile.var(sectionIndex, var + 1);
                            if (var2Name.equalsIgnoreCase("Altitude")) {
                                alt = parseFloat(sectfile.value(sectionIndex, ++var), Float.NaN);
                            }
                        }
                    }
                    switch (varNames.indexOf(varName)) {
                        case 0:
                            if (Float.isNaN(alt)) massFactor = factor;
                            else {
                                if (massFactor == 1F) {
                                    massFactorAltitudeList.add(new Float(alt));
                                    massFactorList.add(new Float(factor));
                                }
                            }
                            break;
                        case 1:
                            if (Float.isNaN(alt)) parasiteFactor = factor;
                            else {
                                if (parasiteFactor == 1F) {
                                    parasiteFactorAltitudeList.add(new Float(alt));
                                    parasiteFactorList.add(new Float(factor));
                                }
                            }
                            break;
                        case 2:
                            if (Float.isNaN(alt)) fuelFactor = factor;
                            else {
                                if (fuelFactor == 1F) {
                                    fuelFactorAltitudeList.add(new Float(alt));
                                    fuelFactorList.add(new Float(factor));
                                }
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (massFactorList.size() == 1) {
            massFactor = ((Float)massFactorList.get(0)).floatValue();
        } else if (massFactorList.size() > 1) {
            massFactorAltitudes = new float[massFactorList.size()];
            massFactors = new float[massFactorList.size()];
            for (int index = 0; index < massFactorList.size(); index++) {
                massFactorAltitudes[index] = ((Float)massFactorAltitudeList.get(index)).floatValue();
                massFactors[index] = ((Float)massFactorList.get(index)).floatValue();
                sort(massFactorAltitudes, massFactors);
            }
        }

        if (parasiteFactorList.size() == 1) {
            parasiteFactor = ((Float)parasiteFactorList.get(0)).floatValue();
        } else if (parasiteFactorList.size() > 1) {
            parasiteFactorAltitudes = new float[parasiteFactorList.size()];
            parasiteFactors = new float[parasiteFactorList.size()];
            for (int index = 0; index < parasiteFactorList.size(); index++) {
                parasiteFactorAltitudes[index] = ((Float)parasiteFactorAltitudeList.get(index)).floatValue();
                parasiteFactors[index] = ((Float)parasiteFactorList.get(index)).floatValue();
                sort(parasiteFactorAltitudes, parasiteFactors);
            }
        }

        if (fuelFactorList.size() == 1) {
            fuelFactor = ((Float)fuelFactorList.get(0)).floatValue();
        } else if (fuelFactorList.size() > 1) {
            fuelFactorAltitudes = new float[fuelFactorList.size()];
            fuelFactors = new float[fuelFactorList.size()];
            for (int index = 0; index < fuelFactorList.size(); index++) {
                fuelFactorAltitudes[index] = ((Float)fuelFactorAltitudeList.get(index)).floatValue();
                fuelFactors[index] = ((Float)fuelFactorList.get(index)).floatValue();
                sort(fuelFactorAltitudes, fuelFactors);
            }
        }
        
        if (massFactors == null) {
            System.out.println("MassFactor=" + massFactor);
        } else {
            for (int i=0; i<massFactors.length; i++) {
                System.out.println("MassFactor for " + massFactorAltitudes[i] + "m altitude = " + massFactors[i]);
            }
        }
        if (MASSFACTOR_DEBUG) {
            if (parasiteFactors == null) {
                System.out.println("ParasiteFactor=" + parasiteFactor);
            } else {
                for (int i=0; i<parasiteFactors.length; i++) {
                    System.out.println("ParasiteFactor for " + parasiteFactorAltitudes[i] + "m altitude = " + parasiteFactors[i]);
                }
            }
            if (fuelFactors == null) {
                System.out.println("FuelFactor=" + fuelFactor);
            } else {
                for (int i=0; i<fuelFactors.length; i++) {
                    System.out.println("FuelFactor for " + fuelFactorAltitudes[i] + "m altitude = " + fuelFactors[i]);
                }
            }
        }
    }

    private float parseFloat(String floatString, float defaultValue) {
        try
        {
            return Float.parseFloat(floatString);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    private static class InterpolateComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
           float[] f0 = (float[])arg0;
           float[] f1 = (float[])arg1;
           if (f0[0] < f1[0]) return -1;
           else if (f0[0] > f1[0]) return -1;
           else return 0;
        }
        
    }

    public static void sort(float[] x, float[] y) {
        float[][] values = new float[x.length][2];
        for (int i=0; i<x.length; i++) {
            values[i][0] = x[i];
            values[i][1] = y[i];
        }
        Arrays.sort(values, new InterpolateComparator());
        for (int i=0; i<x.length; i++) {
            x[i] = values[i][0];
            y[i] = values[i][1];
        }
    }

    private float massFactor = 1F;
    private float[] massFactors = null;
    private float[] massFactorAltitudes = null;
    private float parasiteFactor = 1F;
    private float[] parasiteFactors = null;
    private float[] parasiteFactorAltitudes = null;
    private float fuelFactor = 1F;
    private float[] fuelFactors = null;
    private float[] fuelFactorAltitudes = null;
    private static final boolean MASSFACTOR_DEBUG = false;


    private static final DecimalFormat df2                                = new DecimalFormat("00");
    private static final DecimalFormat df3                                = new DecimalFormat("000");
    public static final String         SAVE                               = "SAVE";
    public static final String         REGISTER                           = "REGISTER";
    public static final String         NOREGISTER                         = "NOREGISTER";
    public static final String         CLEAR                              = "CLEAR";
    public static final String         LIST_LOADOUTS                      = "LL";
    public static final String         FMINFO                             = "FMINFO";
    public static final String         SWITCH                             = "SWITCH";
    public static final String         DUMP                               = "DUMP";
    public static final String         DMPALL                             = "DMPALL";
    public static final String         DMPLIST                            = "DMPLIST";
    public static final String         FMDMP                              = "FMDMP";
    public static final String         COD                                = "COD";
    public static final String         FBDJ                               = "FBDJ";
    public static final String         DMPFILES                           = "DMPFILES";
    public static final int            PADDED                             = 0x1;
    public static final int            CLASSNAME                          = 0x2;
    public static final int            SORTED                             = 0x4;
    public static final int            LISTMODE_PADDED_CLASSNAME_SORTED   = CmdPreload.PADDED | CmdPreload.CLASSNAME | CmdPreload.SORTED;
    public static final int            LISTMODE_UNPADDED_CLASSNAME_SORTED = CmdPreload.CLASSNAME | CmdPreload.SORTED;
    public static final int            LISTMODE_PADDED_SORTED             = CmdPreload.PADDED | CmdPreload.SORTED;
    public static final int            LISTMODE_SORTED                    = CmdPreload.SORTED;
    public static final int            LISTMODE_PADDED_CLASSNAME          = CmdPreload.PADDED | CmdPreload.CLASSNAME;
    public static final int            LISTMODE_UNPADDED_CLASSNAME        = CmdPreload.CLASSNAME;
    public static final int            LISTMODE_PADDED                    = CmdPreload.PADDED;
    public static final int            LISTMODE_NONE                      = 0;
    public static final int            PADDING_SPACES                     = 2;
    public static final String         WEAPONS_LIST_FILE                  = "weapons.properties";

    public static String               dumpDesc                           = "unkown";
    public static int                  dumpStart                          = 0;
    private static int                 fuelLevel                          = -1;
    private static int                 radiatorSetting                    = 100;
    private static boolean             fullDump                           = false;
    private static boolean             settingsInitialized                = false;
    public static String               turnFile;
    public static String               speedFile;
    public static String               auxFile;
    public static String               craftFile;
    public static String               weaponFile;
}
