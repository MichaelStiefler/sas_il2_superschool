package com.maddox.il2.engine.cmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.RendersMain;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Finger;
import com.maddox.rts.HomePath;
import com.maddox.rts.KryptoInputFilter;
import com.maddox.rts.KryptoOutputFilter;
import com.maddox.rts.MsgTimeOut;
import com.maddox.rts.MsgTimeOutListener;
import com.maddox.rts.ObjIO;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;
import com.maddox.sound.AudioDevice;
import com.maddox.util.NumberTokenizer;

public class CmdFPS extends Cmd implements MsgTimeOutListener {

    public void msgTimeOut(Object obj) {
        this.msg.post();
        if (!this.bGo) {
            return;
        }
        long l = Time.real();
        int i = RendersMain.frame();
        if (l >= (this.timePrev + 250L)) {
            this.fpsCur = (1000D * (i - this.framePrev)) / (l - this.timePrev);
            if (this.fpsMin > this.fpsCur) {
                this.fpsMin = this.fpsCur;
            }
            if (this.fpsMax < this.fpsCur) {
                this.fpsMax = this.fpsCur;
            }
            this.framePrev = i;
            this.timePrev = l;
        }
        if (this.framePrev == this.frameStart) {
            return;
        }
        if (this.bShow) {
            Render render = (Render) Actor.getByName("renderTextScr");
            if (render == null) {
                return;
            }
            TTFont ttfont = TextScr.font();
            int j = render.getViewPortWidth();
            int k = render.getViewPortHeight();
            String s = "fps:" + this.fpsInfo();
            int i1 = (int) ttfont.width(s);
            int j1 = k - ttfont.height() - 5;
            int k1 = (j - i1) / 2;
            TextScr.output(k1, j1, s);
        }
        if ((this.logPeriod > 0L) && (l >= (this.logPrintTime + this.logPeriod))) {
            System.out.println("fps:" + this.fpsInfo());
            this.logPrintTime = l;
        }
    }

    public Object exec(CmdEnv cmdenv, Map map) {
        boolean flag = false;
        this.checkMsg();
        if (map.containsKey(SHOW)) {
            this.bShow = true;
            flag = true;
        } else if (map.containsKey(HIDE)) {
            this.bShow = false;
            flag = true;
        }
        if (map.containsKey(LOG)) {
            int i = arg(map, LOG, 0, 5);
            if (i < 0) {
                i = 0;
            }
            this.logPeriod = i * 1000L;
            flag = true;
        }
        if (map.containsKey(PERF)) {
            AudioDevice.setPerformInfo(true);
            flag = true;
        }
        if (map.containsKey(START)) {
            if (this.bGo && (this.timeStart != this.timePrev) && (this.logPeriod == 0L)) {
                this.INFO_HARD("fps:" + this.fpsInfo());
            }
            this.timeStart = Time.real();
            this.frameStart = RendersMain.frame();
            this.fpsMin = 1000000D;
            this.fpsMax = 0.0D;
            this.fpsCur = 0.0D;
            this.timePrev = this.timeStart;
            this.framePrev = this.frameStart;
            this.logPrintTime = this.timeStart;
            this.bGo = true;
            flag = true;
        } else if (map.containsKey(STOP)) {
            if (this.bGo && (this.timeStart != this.timePrev) && (this.logPeriod == 0L)) {
                this.INFO_HARD("fps:" + this.fpsInfo());
            }
            this.bGo = false;
            flag = true;
        }
        if (map.containsKey(COD)) {
            System.out.println("Generating cod Loadouts...");
            this.generateCodLoadouts();
            System.out.println("cod Loadouts generated!");
            flag = true;
        }
        if (!flag) {
            this.INFO_HARD("  LOG  " + (this.logPeriod / 1000L));
            if (this.bShow) {
                this.INFO_HARD("  SHOW");
            } else {
                this.INFO_HARD("  HIDE");
            }
            if (this.bGo) {
                if (this.timeStart != this.timePrev) {
                    this.INFO_HARD("  " + this.fpsInfo());
                }
            } else {
                this.INFO_HARD("  STOPPED");
            }
        }
        return CmdEnv.RETURN_OK;
    }

    private String fpsInfo() {
        return "" + (int) Math.floor(this.fpsCur) + " avg:" + (int) Math.floor((1000D * (this.framePrev - this.frameStart)) / (this.timePrev - this.timeStart)) + " max:" + (int) Math.floor(this.fpsMax) + " min:" + (int) Math.floor(this.fpsMin) + " #fr:" + (this.framePrev - this.frameStart);
    }

    private void checkMsg() {
        if (this.msg == null) {
            this.msg = new MsgTimeOut();
            this.msg.setListener(this);
            this.msg.setNotCleanAfterSend();
            this.msg.setFlags(72);
        }
        if (!this.msg.busy()) {
            this.msg.post();
        }
    }

    public CmdFPS() {
        this.bGo = false;
        this.bShow = false;
        this.logPeriod = 5000L;
        this.logPrintTime = -1L;
        this.param.put(LOG, null);
        this.param.put(START, null);
        this.param.put(STOP, null);
        this.param.put(SHOW, null);
        this.param.put(HIDE, null);
        this.param.put(PERF, null);
        this.param.put(COD, null);
        this._properties.put("NAME", "fps");
        this._levelAccess = 1;
    }

    private void generateCodLoadouts() {
        String prohibitedLoadoutNameCharacters = " ,";
        boolean debugAirClassNames = false;
        SectFile codIniSectfile = new SectFile("cod.ini");
        if (codIniSectfile.sectionExist("Common")) {
            int commonSectionIndex = codIniSectfile.sectionIndex("Common");
            if (codIniSectfile.varExist(commonSectionIndex, "prohibitedLoadoutNameCharacters")) {
                prohibitedLoadoutNameCharacters = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "prohibitedLoadoutNameCharacters"));
                if (prohibitedLoadoutNameCharacters.startsWith("\"") && prohibitedLoadoutNameCharacters.endsWith("\"")) {
                    prohibitedLoadoutNameCharacters = prohibitedLoadoutNameCharacters.substring(1, prohibitedLoadoutNameCharacters.length()-1);
                    System.out.println("prohibited Loadout Name Characters set to: \"" + prohibitedLoadoutNameCharacters + "\"");
                }
            }
            if (codIniSectfile.varExist(commonSectionIndex, "debugAirClassNames")) {
                debugAirClassNames = codIniSectfile.value(commonSectionIndex, codIniSectfile.varIndex(commonSectionIndex, "debugAirClassNames")).trim().equals("1");
                System.out.println("debug Air Class Names set to: \"" + debugAirClassNames + "\"");
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
                    if (debugAirClassNames) System.out.println("Class '" + airplaneName + "'...");
                    
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
                        BufferedReader brDecoded = new BufferedReader(new InputStreamReader(new KryptoInputFilter(new SFSInputStream(Finger.LongFN(0L, "cod/" + Finger.incInt(classFingerInt, "adt"))), getSwTbl(classFingerInt))));
                        hasCoddedWeapons = true;
                        
                        pwu = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("loadouts/decoded/" + airplaneName, 0))));
                        pwu2 = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName("loadouts/decodedNormalized/" + airplaneName, 0))));
                        do {
                            String decodedLine = brDecoded.readLine();
                            if (decodedLine == null) {
                                break;
                            }
                            if (decodedLine.trim().length() < 1) continue;
                            pwu.println(decodedLine);
                            List list = new ArrayList();
                            StringTokenizer st = new StringTokenizer(decodedLine, ",");
                            while (st.hasMoreTokens()) {
                                String token = st.nextToken();
                                if (token.trim().length() == 0)
                                    token = " ";
                                list.add(token);
                            }
                            while (list.size() - 1 < hookLength) {
                                list.add(" ");
                            }
                            while (list.size() - 1 > hookLength) {
                                String removedItem = list.remove(list.size() - 1).toString();
                                if (removedItem.trim().length() > 0) {
                                    pwErrors.println(airplaneName + " (+) loadout " + list.get(0) + " needs trimming, but surplus element " + list.size() + " isn't empty (" + removedItem + ")");
                                }
                            }
                            Iterator li = list.iterator();
                            while (li.hasNext()) {
                                pwu2.print(li.next());
                                if (li.hasNext())
                                    pwu2.print(",");
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
                        PrintWriter pwk = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new KryptoOutputFilter(new FileOutputStream(HomePath.toFileSystemName("loadouts/new_cod/" + Finger.incInt(classFingerInt, "adt"), 0)), getSwTbl(classFingerInt)))));
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
                            if (loadoutIndex == 0 && loadoutsLine2.equalsIgnoreCase("default")) loadoutsLine2 = "default";
                            else if (loadoutIndex == loadoutArray.length - 1 && (loadoutsLine2.equalsIgnoreCase("none") || loadoutsLine2.equalsIgnoreCase("empty"))) loadoutsLine2 = "none";
                            if ((loadoutIndex != 0) && loadoutArray[loadoutIndex].toLowerCase().equals("default")) {
                                pwErrors.println(airplaneName + codIndicator + " loadoutArray[" + loadoutIndex + "]=" + loadoutArray[loadoutIndex] + ", but default is only allowed in first slot! (might indicate overlapping weapon slots)");
                            } else if ((loadoutIndex != (loadoutArray.length - 1)) && loadoutArray[loadoutIndex].toLowerCase().equals("none")) {
                                pwErrors.println(airplaneName + codIndicator + " loadoutArray[" + loadoutIndex + "]=" + loadoutArray[loadoutIndex] + ", but none is only allowed in last slot! (might indicate overlapping weapon slots)");
                            } else {
                                for (int prohibitedCharacterIndex = 0; prohibitedCharacterIndex<prohibitedLoadoutNameCharacters.length(); prohibitedCharacterIndex++) {
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
                                    if (slotIndex < hookLength)
                                        loadoutsLine2 += ", ";
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

    private boolean            bGo;
    private boolean            bShow;
    private long               timeStart;
    private int                frameStart;
    private double             fpsMin;
    private double             fpsMax;
    private double             fpsCur;
    private long               timePrev;
    private int                framePrev;
    private long               logPeriod;
    private long               logPrintTime;
    public final static String LOG   = "LOG";
    public final static String START = "START";
    public final static String STOP  = "STOP";
    public final static String SHOW  = "SHOW";
    public final static String HIDE  = "HIDE";
    public final static String PERF  = "PERF";
    public final static String COD   = "COD";
    private MsgTimeOut         msg;
}
