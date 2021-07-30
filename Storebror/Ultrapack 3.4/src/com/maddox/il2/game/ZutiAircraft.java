package com.maddox.il2.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import com.maddox.il2.builder.Zuti_WAircraftProperties;
import com.maddox.il2.net.BornPlace;

public class ZutiAircraft {
    private static int   MAX_LOADOUTS               = 9;
    public static String DEFAULT_AIRCRAFT_RED_SIDE  = "HurricaneMkIIb";
    public static String DEFAULT_AIRCRAFT_BLUE_SIDE = "Bf-109E-4/B";

    private ArrayList    selectedWeaponNames        = null;
    private ArrayList    weaponNames                = null;
    private String       aircraftName               = null;
    private int          numberOfAircraft           = 0;
    private int          maxFuelSelection           = 9;
    private String       acNameFromClass            = null;

    public ZutiAircraft() {
    }

    /**
     * Method returns basic AC statuses like this: acName, currentNumberOfAc, maxFuel, loadouts
     *
     * @return
     */
    public String getAcStatusLine() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getAcName());
        sb.append(" ");
        sb.append(this.getNumberOfAIrcraft());
        sb.append(" ");
        sb.append(this.getMaxFuelSelection());
        sb.append(" ");
        if (this.selectedWeaponNames == null || this.selectedWeaponNames.size() == 0) this.setDefaultLoadouts();
        for (int i = 0; i < this.selectedWeaponNames.size(); i++) {
            sb.append((String) this.selectedWeaponNames.get(i));
            sb.append(" ");
        }

        return sb.toString();
    }

    public void setAcName(String name) {
        this.aircraftName = name;

        this.fillWeapons();
    }

    public String getAcName() {
        return this.aircraftName;
    }

    public void setNumberOfAircraft(int number) {
        this.numberOfAircraft = number;
    }

    public int getNumberOfAIrcraft() {
        return this.numberOfAircraft;
    }

    public void setMaxFuelSelection(int number) {
        this.maxFuelSelection = number;
    }

    public int getMaxFuelSelection() {
        return this.maxFuelSelection;
    }

    private void fillWeapons() {
        this.weaponNames = new ArrayList();
        Class class1 = (Class) com.maddox.rts.Property.value(this.aircraftName, "airClass", null);

        String as[] = com.maddox.il2.objects.air.Aircraft.getWeaponsRegistered(class1);
        if (as != null && as.length > 0) {
            for (int j = 0; j < as.length; j++) {
                String s1 = as[j];

                this.weaponNames.add(s1);
                // weaponNames.add(com.maddox.il2.game.I18N.weapons(aircraftName, s1));
            }

            // Add default option
            if (this.weaponNames.size() == 0) this.weaponNames.add(as[0]);
            // weaponNames.add(com.maddox.il2.game.I18N.weapons(aircraftName, as[0]));
        }

        // listWeapons();
    }

    /**
     * This method sets variables for ac numbers, fuel and loadouts to indicate that no limitations were applied to this AC.
     */
    public void setNoLimitations() {
        this.setMaxFuelSelection(9);
        this.setNumberOfAircraft(0);
        this.setSelectedWeapons(this.getWeaponI18NNames());
    }

    /**
     * Method adds first nine loadouts for aircraft type.
     */
    public void setDefaultLoadouts() {
        if (this.selectedWeaponNames == null) this.selectedWeaponNames = new ArrayList();
        this.selectedWeaponNames.clear();

        int numberOfLoadouts = this.weaponNames.size();
        if (numberOfLoadouts > MAX_LOADOUTS) numberOfLoadouts = MAX_LOADOUTS;
        // System.out.println("Setting default 9 loadouts for ac >" + this.getAcName() + "<");
        for (int i = 0; i < numberOfLoadouts; i++)
            this.selectedWeaponNames.add(this.weaponNames.get(i));
        // System.out.println(" Adding loadout: " + weaponNames.get(i));
    }

    /**
     * Method returns I18N name for specific game loadout defignation.
     *
     * @param name
     * @return
     */
    public String getWeaponI18NName(String name) {
        return I18N.weapons(this.aircraftName, name);
    }

    /**
     * Method returns loadout name based on specified index. This index is then compared against full loadout array.
     *
     * @param index
     * @return
     */
    public String getLoadoutById(int index) {
        if (this.selectedWeaponNames == null || this.selectedWeaponNames.size() < 1) {
            // No selected weapons -> choose from complete weapons list
            if (index < this.weaponNames.size()) return (String) this.weaponNames.get(index);

            return null;
        }

        if (index > this.selectedWeaponNames.size() - 1) return null;

        return (String) this.selectedWeaponNames.get(index);
    }

    /**
     * Method returns list of selected loadout options. The names are in I18N format.
     *
     * @return
     */
    public List getSelectedWeaponI18NNames() {
        ArrayList list = new ArrayList();
        if (this.selectedWeaponNames == null) return list;

        for (int i = 0; i < this.selectedWeaponNames.size(); i++) {
            list.add(this.getWeaponI18NName((String) this.selectedWeaponNames.get(i)));

            if (list.size() >= Zuti_WAircraftProperties.MAX_LOADOUTS) break;
        }
        return list;
    }

    /**
     * Method returns a list of names of ALL available loadout options for current AC. Loadout names are I18N.
     *
     * @return
     */
    public ArrayList getWeaponI18NNames() {
        ArrayList list = new ArrayList();
        if (this.weaponNames == null) return list;

        for (int i = 0; i < this.weaponNames.size(); i++)
            list.add(this.getWeaponI18NName((String) this.weaponNames.get(i)));
        return list;
    }

    /**
     * Method prints weapon names (all of them) for current AC. Loadout names are in game format.
     *
     * @return
     */
    public void listWeapons() {
        for (int i = 0; i < this.weaponNames.size(); i++)
            System.out.println("   " + this.weaponNames.get(i).toString());
    }

    /**
     * Method returns string that represents loadout ids that are available (selected) for current AC.
     *
     * @return
     */
    private String getSelectedWeaponsIds() {
        if (this.selectedWeaponNames == null) // System.out.println("SelectedWeaponsNames list is null!");
            return "";
        if (this.selectedWeaponNames.size() == 0) // System.out.println("SelectedWeaponsNames list is 0!");
            return "";

        String result = "";
        ArrayList list = this.getWeaponI18NNames();
        if (this.selectedWeaponNames.size() == list.size()) for (int i = 0; i < list.size(); i++) {
            result += " " + i;

            if (i >= Zuti_WAircraftProperties.MAX_LOADOUTS) break;
        }
        else {
            ArrayList resultsList = new ArrayList();
            String selectedWeaponI18NName = null;
            for (int j = 0; j < this.selectedWeaponNames.size(); j++) {
                selectedWeaponI18NName = this.getWeaponI18NName((String) this.selectedWeaponNames.get(j));
                for (int i = 0; i < list.size(); i++)
                    // System.out.println(selectedWeaponI18NName + " vs " + list.get(i) );
                    if (selectedWeaponI18NName.equals(list.get(i))) {
                        resultsList.add(new Integer(i));
                        break;
                    }

                if (j >= Zuti_WAircraftProperties.MAX_LOADOUTS) break;
            }

            // Sort the list
            Collections.sort(resultsList);

            // Compile result string
            for (int i = 0; i < resultsList.size(); i++)
                result += " " + ((Integer) resultsList.get(i)).intValue();
        }

        return result.trim();
    }

    /**
     * Call this method with valid i18n loadout names in specified list. Those names will then be set as selected loadout options.
     *
     * @param list
     */
    public void setSelectedWeapons(ArrayList list) {
        // here we have I18N weapon names
        if (this.selectedWeaponNames == null) this.selectedWeaponNames = new ArrayList();

        this.selectedWeaponNames.clear();

        for (int i = 0; i < list.size(); i++)
            this.selectedWeaponNames.add(list.get(i));
    }

    /**
     * Call this method when mission is loading in game.
     *
     * @param list
     */
    public void setLoadedWeapons(String line, boolean loadI18Nnames) {
        if (this.selectedWeaponNames == null) this.selectedWeaponNames = new ArrayList();
        this.selectedWeaponNames.clear();

        if (line == null) return;

        // Fill only selected loadouts that are defined in "line" parameter
        ArrayList selection = new ArrayList();
        StringTokenizer stringtokenizer = new StringTokenizer(line);
        while (stringtokenizer.hasMoreTokens())
            try {
                selection.add(Integer.valueOf(stringtokenizer.nextToken()));
            } catch (Exception ex) {}

        // Sort loaded loadouts
        Collections.sort(selection);

        for (int i = 0; i < selection.size(); i++)
            try {
                Integer index = (Integer) selection.get(i);
                if (loadI18Nnames) this.selectedWeaponNames.add(this.getWeaponI18NName((String) this.weaponNames.get(index.intValue())));
                // System.out.println("ZutiAircraft - " + getWeaponI18NName((String)weaponNames.get(index.intValue())));
                else this.selectedWeaponNames.add(this.weaponNames.get(index.intValue()));
            } catch (Exception ex) {}
    }

    /**
     * Method returns aircraft line that needs to be saved into mission file. Line contains aircraft name + maxAc + maxFuel + loaudouts.
     *
     * @param limitationsEnabled
     * @return
     */
    public String getMissionLine(boolean limitationsEnabled) {
        String loadouts = "";
        if (limitationsEnabled) loadouts = this.getSelectedWeaponsIds();

        return this.aircraftName + " " + this.numberOfAircraft + " " + this.maxFuelSelection + " " + loadouts;
    }

    /**
     * Method decreases number of aircraft that are available at given home base.
     */
    public void decreaseNumberOfAircraft(BornPlace bp) {
        // Manage these numbers only if you are server!
        if (Main.cur().netServerParams != null &&
        // Main.cur().netServerParams.isMaster() &&
                bp.zutiEnablePlaneLimits) // If number of aircraft is 0, do nothing.
            if (this.numberOfAircraft > 0) this.numberOfAircraft--;

        System.out.println("ZutiAircraft - number of aircraft >" + this.getAcName() + "< =  " + this.numberOfAircraft);
    }

    /**
     * Method increases number of aircraft that are available at given home base.
     */
    public void increaseNumberOfAircraft(BornPlace bp) {
        // Manage these numbers only if you are server!
        if (Main.cur().netServerParams != null &&
        // Main.cur().netServerParams.isMaster() &&
                bp.zutiEnablePlaneLimits)
            this.numberOfAircraft++;

        System.out.println("ZutiAircraft - number of aircraft >" + this.getAcName() + "< =  " + this.numberOfAircraft);
    }

    /**
     * Indicates if aircraft is available at specified home base or not.
     *
     * @param bp
     * @return
     */
    public boolean isAvailable(BornPlace bp) {
        if (bp == null) return false;

        if (!bp.zutiEnablePlaneLimits) // If home base does not support decreasing aircraft numbers aircraft is always available.
            return true;
        else if (this.numberOfAircraft > 0) // System.out.println("RETURNING TRUE2 for ac >" + this.aircraftName + "<. Number of this type is >" + numberOfAircraft + "<");
            return true;

        return false;
    }

    /**
     * Method returns aircraft short name based on it's class.
     *
     * @return
     */
    public String getName_ShortAircraftNameFromClass() {
        if (this.acNameFromClass == null) this.acNameFromClass = this.getShortAcNameFromClass(((Class) com.maddox.rts.Property.value(this.aircraftName, "airClass", null)).toString());

        return this.acNameFromClass;
    }

    private String getShortAcNameFromClass(String value) {
        try {
            return value.substring(value.lastIndexOf(".") + 1, value.length());
        } catch (Exception ex) {
            return "";
        }
    }

    public String toString() {
        return this.getAcName();
    }

    public boolean equals(Object o) {
        if (o instanceof ZutiAircraft) {
            ZutiAircraft inKeyObject = (ZutiAircraft) o;

            // System.out.println("Comparing: >" + this.getAcName() + "< vs >" + inKeyObject.getAcName() + "<");

            if (this.getAcName().equals(inKeyObject.getAcName())) // System.out.println(" EQUALS!");
                return true;
        } else {
            // System.out.println("--- WRONG COMPARE OBJECT! " + o.toString());
        }

        // System.out.println(" DIFFERENT!");
        return false;
    }

    public int hashCode() {
        return this.getAcName().hashCode();
    }
}