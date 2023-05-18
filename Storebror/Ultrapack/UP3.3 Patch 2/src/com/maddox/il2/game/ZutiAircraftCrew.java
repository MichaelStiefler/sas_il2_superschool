package com.maddox.il2.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.maddox.il2.engine.Config;

public class ZutiAircraftCrew {
    private Map    crew             = null;
    private String acName           = null;
    private int    numberOfCockpits = 0;

    public ZutiAircraftCrew(String acName) {
        this.acName = acName;
        this.numberOfCockpits = ZutiSupportMethods_Multicrew.getMaxCockpitsForAircraft(acName);
        printDebugMessage("Cockpits for AC >" + acName + "< = " + numberOfCockpits);
        this.crew = new HashMap();
    }

    /**
     * Clears crew map.
     */
    public void clearCrewMap() {
        this.crew.clear();
    }

    /**
     * Method returns all crew and their position in one or more lines, depending of line length. Entries in line are ordered like this: key,value key,value key,value...
     *
     * @return
     */
    public List getCrewLines() {
        List lines = new ArrayList();
        StringBuffer sb = new StringBuffer();

        Iterator iterator = this.crew.keySet().iterator();
        while (iterator.hasNext()) {
            Integer key = (Integer) iterator.next();
            String value = (String) this.crew.get(key);

            sb.append(key.intValue());
            sb.append(",");
            sb.append(value);
            sb.append(" ");

            if (sb.toString().length() > 200) {
                lines.add(sb.toString().trim());
                sb = new StringBuffer();
            }
        }

        // Add remainder of the weapon releases
        lines.add(sb.toString().trim());

        return lines;
    }

    /**
     * Returns list of crew names.
     *
     * @return
     */
    public List getCrewList() {
        List list = new ArrayList();
        try {
            Iterator iterator = this.crew.keySet().iterator();
            Integer cockpitNr = null;
            while (iterator.hasNext()) {
                cockpitNr = (Integer) iterator.next();
                if (cockpitNr.intValue() > -1) {
                    String username = (String) this.crew.get(cockpitNr);
                    if (username != null) {
                        list.add(username);
                        printDebugMessage("Added user: " + username);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    /**
     * Map key=Integer-cockpit num, map value=String-username
     *
     * @return
     */
    public Map getCrewMap() {
        return this.crew;
    }

    private Integer getCurrentUserPosition(String username) {
        if (this.crew != null) {
            String user = null;
            Integer key = null;
            Iterator iterator = this.crew.keySet().iterator();
            while (iterator.hasNext()) {
                key = (Integer) iterator.next();
                user = (String) this.crew.get(key);
                if (user != null && user.equals(username)) return key;
            }
        }

        return null;
    }

    /**
     * This method just sets new received position data for specified user. First, method resets users current position (sets it to null) and then sets user to specified new position. If position is -1 then user is not assigned new cockpit number.
     *
     * @param username
     * @param position
     */
    public void setUserPosition(String username, int position) {
        Integer currentPosition = this.getCurrentUserPosition(username);
        if (currentPosition != null) {
            // Reset users old position
            this.crew.put(currentPosition, null);
            printDebugMessage("setUserPosition: position >" + currentPosition + "< owner set to null!");
        }

        // Set user to new position
        if (position > -1) this.crew.put(new Integer(position), username);
    }

    /**
     * Can user occupy desired crew position? Method checks if desired position is free and sets it. If it is not, method searches for next free position. In case none is found, -1 is returned otherwise number of free position is returned.
     *
     * @param username
     * @param requestedPosition
     * @param isPilot
     * @return
     */
    public int checkRequestedPosition(String username, int requestedPosition, boolean isPilot) {
        if (this.crew == null) return -1;

        printDebugMessage("checkRequestedPosition: Is requesting player a pilot? " + isPilot);
        // System.out.println("zutiAircraftCrew - AC cockpits nr: " + numberOfCockpits);
        if (!isPilot && requestedPosition == 0) requestedPosition = 1;

        Integer currentPosition = this.getCurrentUserPosition(username);
        Integer targetPosition = new Integer(requestedPosition);
        if (this.crew.get(targetPosition) == null) {
            // Desired position is free, reset old position and set user to new one
            if (currentPosition != null) {
                this.crew.put(currentPosition, null);
                printDebugMessage("Reset position: " + currentPosition);
            }

            this.crew.put(targetPosition, username);

            // For testing purposes: printout crew map
            this.mapPrintout();

            return targetPosition.intValue();

        } else // Desired position is not free, get next available one
            for (int x = 0; x < this.numberOfCockpits - 1; x++) {
                int cockpitNr = (requestedPosition + x + 1) % this.numberOfCockpits;

                if (!isPilot && cockpitNr == 0) continue;

                targetPosition = new Integer(cockpitNr);
                if (this.crew.get(targetPosition) == null) break;
                else targetPosition = new Integer(-1);
            }

        // Free current owned position
        if (currentPosition != null) this.crew.put(currentPosition, null);
        // Occupy new position
        this.crew.put(targetPosition, username);

        // For testing purposes: printout crew map
        this.mapPrintout();

        return targetPosition.intValue();
    }

    /**
     * Get aircraft name.
     *
     * @return
     */
    public String getAcName() {
        return this.acName;
    }

    /**
     * Set aircraft name
     *
     * @param acName
     */
    public void setAcName(String acName) {
        this.acName = acName;
    }

    /**
     * Get number of cockpits for this aircraft.
     *
     * @return
     */
    public int getNrOfCockpits() {
        return this.numberOfCockpits;
    }

    /**
     * Shows aircraft crew positions
     */
    public void mapPrintout() {
        if (curDebugLevel() == 0) return;
        printDebugMessage("Crew data for AC: " + this.acName);
        Iterator iterator = crew.keySet().iterator();
        while( iterator.hasNext() ) {
            Integer key = (Integer)iterator.next();
            String value = (String)crew.get(key);
            printDebugMessage("Key=" + key + ", value=" + value);
            }
        printDebugMessage("=====================================");
    }

    public boolean equals(Object o) {
        if (!(o instanceof ZutiAircraftCrew)) return false;

        ZutiAircraftCrew inKeyObject = (ZutiAircraftCrew) o;
        if (this.getAcName().equals(inKeyObject.getAcName())) return true;

        return false;
    }

    public int hashCode() {
        return this.acName.hashCode();
    }
    
    private static int       debugLevel    = Integer.MIN_VALUE;
    private static final int DEBUG_DEFAULT = 0;

    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE) debugLevel = Config.cur.ini.get("Mods", "DEBUG_ZAC", DEBUG_DEFAULT);
        return debugLevel;
    }

    public static void printDebugMessage(String theMessage) {
        if (curDebugLevel() == 0) return;
        System.out.println("ZutiAircraftCrew - " + theMessage);
    }
}