package com.maddox.il2.engine.cmd;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.maddox.il2.engine.Pre;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HomePath;
import com.maddox.rts.Property;

public class CmdPreload extends Cmd {

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
        if (map.containsKey(REGISTER)) {
            Pre.register(true);
        }
        if (map.containsKey(NOREGISTER)) {
            Pre.register(false);
        }
        if (map.containsKey(CLEAR)) {
            Pre.clear();
        }
        if (map.containsKey(SAVE)) {
            Pre.save(arg(map, SAVE, 0));
        }
        if (map.containsKey(LIST_LOADOUTS)) {
            this.ListLoadouts(arg(map, LIST_LOADOUTS, 0, LISTMODE_PADDED_CLASSNAME_SORTED));
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
        this.param.put(SAVE, null);
        this.param.put(REGISTER, null);
        this.param.put(NOREGISTER, null);
        this.param.put(CLEAR, null);
        this.param.put(LIST_LOADOUTS, null);
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
                final int curPaddingPos = airClassKeyName.length() + weaponsName.length() + 1 + PADDING_SPACES;
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

        if ((listMode & SORTED) != 0) {
            Collections.sort(listAircraft);
        }

        if ((listMode & PADDED) != 0) {
            maxLength = paddingPos + maxWeaponsLength;
        }
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' hh-mm-ss z");
        final Date date = new Date();
        PrintWriter pw;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(WEAPONS_LIST_FILE, 0))));
        } catch (final IOException IOE) {
            System.out.println("*** WEAPONS LIST DEBUG: Creating Weapons List File failed: " + IOE.getMessage());
            return;
        }

        final String headLine = "Created by SAS Loadout Lister 4.09+ on " + dateFormat.format(date);
        if (maxLength < (headLine.length() + 4)) {
            maxLength = headLine.length() + 4;
        }

        pw.println(repeat('#', maxLength));
        final int paddingLeft = (maxLength - headLine.length() - 2) / 2;
        final int paddingRight = maxLength - headLine.length() - 2 - paddingLeft;
        pw.println("#" + repeat(' ', paddingLeft) + headLine + repeat(' ', paddingRight) + "#");
        pw.println(repeat('#', maxLength));
        pw.println();

        for (int aircraftIndex = 0; aircraftIndex < listAircraft.size(); aircraftIndex++) {
            final ListAircraft curListAircraft = (ListAircraft) listAircraft.get(aircraftIndex);
            pw.println(repeat('#', maxLength));
            pw.println("# " + curListAircraft.getReadableName());
            pw.println(repeat('#', maxLength));
            final ArrayList loadouts = curListAircraft.getLoadouts();
            for (int loadoutIndex = 0; loadoutIndex < loadouts.size(); loadoutIndex++) {
                final ListLoadout curListLoadout = (ListLoadout) loadouts.get(loadoutIndex);
                pw.print(curListAircraft.getKeyName());
                pw.print(".");
                pw.print(curListLoadout.getLoadoutName());
                if (curListLoadout.getLoadoutName().equals(curListLoadout.getLoadoutReadableName()) && ((listMode & CLASSNAME) == 0)) {
                    pw.println();
                    continue;
                }
                if ((listMode & PADDED) != 0) {
                    final int curLength = curListAircraft.getKeyName().length() + curListLoadout.getLoadoutName().length() + 1;
                    pw.print(repeat(' ', paddingPos - curLength));
                } else {
                    pw.print(repeat(' ', PADDING_SPACES));
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

    public static String repeat(char chr, int times) {
        return new String(new char[times]).replace('\0', chr);
    }

    public static final String SAVE                               = "SAVE";
    public static final String REGISTER                           = "REGISTER";
    public static final String NOREGISTER                         = "NOREGISTER";
    public static final String CLEAR                              = "CLEAR";
    public static final String LIST_LOADOUTS                      = "LL";
    public static final int    PADDED                             = 0x1;
    public static final int    CLASSNAME                          = 0x2;
    public static final int    SORTED                             = 0x4;
    public static final int    LISTMODE_PADDED_CLASSNAME_SORTED   = PADDED | CLASSNAME | SORTED;
    public static final int    LISTMODE_UNPADDED_CLASSNAME_SORTED = CLASSNAME | SORTED;
    public static final int    LISTMODE_PADDED_SORTED             = PADDED | SORTED;
    public static final int    LISTMODE_SORTED                    = SORTED;
    public static final int    LISTMODE_PADDED_CLASSNAME          = PADDED | CLASSNAME;
    public static final int    LISTMODE_UNPADDED_CLASSNAME        = CLASSNAME;
    public static final int    LISTMODE_PADDED                    = PADDED;
    public static final int    LISTMODE_NONE                      = 0;
    public static final int    PADDING_SPACES                     = 2;
    public static final String WEAPONS_LIST_FILE                  = "weapons.properties";

}
