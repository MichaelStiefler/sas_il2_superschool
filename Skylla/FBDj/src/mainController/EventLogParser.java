package mainController;

import java.util.Iterator;

import model.AIWing;
import model.Event;
import model.MissionObject;
import model.SortieEvent;
import utility.Time;
import utility.UnicodeFormatter;

class EventLogParser {

    /**
     * 
     * @param pilotName
     *            The pilotName used in the eventlog
     * @return pilotName from PILOTS list if a match is found
     * 
     */
    public static String checkPilotName(String pilotName, boolean disconnect) {
        int pilotNameByteLength = 0;
        int byteCount = 0;
        try {
            // Most pilot names will already be in the list so check that 1st
            if (MainController.PILOTS.containsKey(pilotName)) {
                return pilotName;
            }
            // Pilot Name was not in the list, see if there was embedded unicode
            else {
                String convertedName = UnicodeFormatter.convertAsciiStringToUnicode(pilotName);
                if (MainController.PILOTS.containsKey(convertedName)) {
                    return convertedName;
                }
            }
            // Next check to see if their was a duplicate of this name and it is in the list as
            // the duplicate name designated with a '0' on the end
            if (MainController.PILOTS.containsKey(pilotName + '0')) {
                return pilotName + '0';
            }
            // Check for a mission Object
            else if (MainController.ACTIVEMISSION.getMissionObjects().containsKey(pilotName)) {
                return pilotName;
            } else if (isAIWing(pilotName) != null) {
                // AI Pilot
                return pilotName;
            }
            // Not a mission Object either, lets assume it's a name with cryllic characters.
            // These show up in the eventlog file as a '?' for each cryllic character
            else {
                // Get the number of bytes that make up the name by adding the number of "?"
                // Since that is how cryllic characters are represented in the EventLog
                for (int i = 0; i < pilotName.length(); i++) {
                    if (pilotName.substring(i, i + 1).equals("?")) {
                        pilotNameByteLength++;
                    }
                }

                // Loop through the Pilots List
                Iterator<String> it = MainController.PILOTS.keySet().iterator();
                while (it.hasNext()) {
                    // Does a name have a cryllic character in it ?
                    String key = it.next();
                    String asciiName = MainController.PILOTS.get(key).getAsciiTextName();
                    if (asciiName.contains("\\u")) {
                        // A name has a cryllic character in it, now count how many are there
//						int keyLength = asciiName.length();
                        for (int i = 0; i < asciiName.length(); i++) {
                            if (i < asciiName.length() - 2) {
                                if (asciiName.subSequence(i, i + 2).equals("\\u")) {
                                    byteCount++;
                                }
                            }
                        }
                        // If the byte count in the pilots name in the list is the same as the pilotName passed in.
                        // we'll assume that this is the pilot and return the proper name
                        if (byteCount == pilotNameByteLength) {
                            // Probable match
                            return key;
                        }
                    }
                }
                // If EventLog event is disconnect, then this is not an error as the pilot is already gone.
                if (!disconnect) {
                    MainController.writeDebugLogFile(1, "EventLogParser.checkPilotName - Unknown pilotName (" + pilotName + ") from eventLog");
                }
            }
            return pilotName;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogParser.checkPilotName - Exception with Pilot Name(" + pilotName + ")");
            return pilotName;
        }
    }

    public static AIWing isAIWing(String pilotName) {
        try {
            Iterator<String> it = MainController.ACTIVEMISSION.getAiWings().keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (MainController.ACTIVEMISSION.getAiWings().get(key).getPlane().equals(pilotName) || key.equals(pilotName)) {
                    return MainController.ACTIVEMISSION.getAiWings().get(key);
                }
            }
            return null;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogParser.isAIWing - Unhandled Exception pilotName(" + pilotName + ") " + ex);
            return null;
        }
    }

    public static boolean isMissionObject(String objectName) {
        if (MainController.ACTIVEMISSION.getMissionObjects().containsKey(objectName)) {
            MainController.writeDebugLogFile(2, "EventLogParser.isMissionObject - (" + objectName + ") is a mission object");
            return true;
        } else {
            MainController.writeDebugLogFile(2, "EventLogParser.isMissionObject - (" + objectName + ") is NOT a mission object");
            return false;
        }
    }

    public static Object[] parsePilotPlanePosition(String parseValueIn, boolean getPosition) {
        int positionIndex = 0;
        String pilotPlaneName = "";
        Object[] parsedObject = new Object[5];

        parsedObject[0] = "Unknown";    // Pilot or AI Wing Name
        parsedObject[1] = "Unknown";    // Plane Name
        parsedObject[2] = 0;            // Player Position
        parsedObject[3] = "Unknown";    // AIPlane Identifer
        parsedObject[4] = 0;            // Army
        try {
            // Check for Player Position info ("(x)" at the end where x is position number)
            // With HSFX this can also be _x where x is the player position
            if (getPosition && parseValueIn.endsWith(")") && parseValueIn.lastIndexOf("(") > 0) {
                positionIndex = parseValueIn.lastIndexOf("(");
                parsedObject[2] = Integer.parseInt(parseValueIn.substring(positionIndex + 1, parseValueIn.lastIndexOf(")")));
                pilotPlaneName = parseValueIn.substring(0, positionIndex);
            } else if (parseValueIn.endsWith("_0")) {
                pilotPlaneName = parseValueIn.substring(0, parseValueIn.lastIndexOf("_0"));
            } else {
                pilotPlaneName = parseValueIn;
            }

            try {
                // With MDS 1.2 the Pilot/Plane string is in a different format !! Verify which format 1st then
                // parse to get the correct data.

                if (pilotPlaneName.contains("{") && pilotPlaneName.contains("}") && pilotPlaneName.contains(";")) {
                    // We can be reasonably sure this is version 1.2 so parse in that manner
                    String properName = pilotPlaneName.substring(0, pilotPlaneName.indexOf("{"));
                    String contentsData = pilotPlaneName.substring(pilotPlaneName.indexOf("{") + 1);
                    
                    // TODO: Storebror trying to get rid of "NONAME" planes
                    String[] contentsDataSplit = contentsData.split(";");
                    if (properName.trim().equals("NONAME")) {
                        if (contentsDataSplit[0].length() > 4) {
                            boolean isNumeric = true;
                            for (int nameIndex = contentsDataSplit[0].length()-1; nameIndex>contentsDataSplit[0].length()-4; nameIndex--) {
                                if (!Character.isDigit(contentsDataSplit[0].charAt(nameIndex))) {
                                    isNumeric = false;
                                    break;
                                }
                            }
                            if (isNumeric) {
                                properName = contentsDataSplit[0].substring(0, contentsDataSplit[0].length()-1);
                            }
                        }
                    }
                    
                    if (isAIWing(properName) != null) {
                        parsedObject[0] = contentsDataSplit[0];   // AI Plane Identifier
                        parsedObject[3] = properName;                   // AI Wing
                        parsedObject[1] = contentsDataSplit[1];   // Plane Name
                    } else {
                        parsedObject[0] = properName.trim();               // Pilot Name
                    }
                    if (contentsDataSplit[1].contains(":"))   // Plane Name
                    {
                        parsedObject[1] = contentsDataSplit[1].split(":")[1];     // Plane Name
                        if (properName.equals("NONAME")) {
                            parsedObject[0] = contentsDataSplit[1].split(":")[0];
                        }
                    }
                    String armyName = contentsDataSplit[2].replace("}", " ");
                    parsedObject[4] = Integer.valueOf(armyName.trim());  // Army
                } else {
                    String[] parsedData = pilotPlaneName.split(":");
                    AIWing wingName = isAIWing(parsedData[0]);
                    if (wingName != null) {
                        for (int i = 0; i < wingName.getPlaneCount(); i++) {
                            String tmpName = wingName.getName() + i;
                            MissionObject missionObject = MainController.ACTIVEMISSION.getMissionObjects().get(tmpName);
                            if (missionObject != null) {
                                if (!missionObject.getDestroyed()) {
                                    parsedObject[0] = tmpName;
                                    break;
                                }
                            }
                            parsedObject[0] = wingName.getName() + '1';
                        }
                    } else {
                        parsedObject[0] = parsedData[0];
                    }
                    if (parsedData.length > 1) {
                        parsedObject[1] = parsedData[1];
                    }

                }

            } catch (Exception ex) {
                parsedObject[0] = "Unknown";
                parsedObject[1] = "Unknown";
            }
            parsedObject[0] = parsedObject[0];
            parsedObject[1] = parsedObject[1].toString().trim();
            return parsedObject;

        } catch (Exception e) {
            MainController.writeDebugLogFile(1, "EventLogParser.parsePlanePilotPosition - Unhandled Exception parsing(" + parseValueIn + "):" + e);
            return parsedObject;
        }
    }

    public static void parseEvent(long eventTime, String eventLine) {

        String eventAction;
        int endDateTimeIndex;
        int pilotEndIndex;
        int victimEndIndex;
        String pilotName;
        String planeName;
        int playerPosition;
        String victimName;
        String victimObject;

        MainController.writeDebugLogFile(2, "EventLogParser.parseEvent - New Event Log Line:\n" + " ** (" + eventLine + ") ** ");

        if (eventLine.startsWith("[")) {
            try {
                // We are using the current Unix date/timestamp so trim the Date/Time stamp off the event line
                endDateTimeIndex = eventLine.indexOf("]");
                eventAction = eventLine.substring(endDateTimeIndex + 2, eventLine.length());

                if (eventAction.startsWith("Mission:")) // Mission that is playing
                {
                    if (eventAction.contains("BLUE WON")) {
                        EventLogController.processMissionWon(SortieEvent.EventType.MISSIONWON, MainController.BLUEARMY, eventAction);
                    } else if (eventAction.contains("RED WON")) {
                        EventLogController.processMissionWon(SortieEvent.EventType.MISSIONWON, MainController.REDARMY, eventAction);
                    }
                } else if (eventAction.startsWith("Mission BEGIN")) {
                    EventLogController.processMissionEvent(SortieEvent.EventType.MISSIONBEGIN, eventTime);
                } else if (eventAction.startsWith("Mission END")) {
                    EventLogController.processMissionEvent(SortieEvent.EventType.MISSIONEND, eventTime);
                } else if (eventAction.endsWith("has connected")) {
                    pilotEndIndex = eventAction.indexOf(" has connected");
                    pilotName = eventAction.substring(0, pilotEndIndex);
                    EventLogController.processActionEvent(SortieEvent.EventType.PILOTCONNECT, eventTime, pilotName, null, eventAction);
                } else if (eventAction.endsWith("has disconnected")) {
                    pilotEndIndex = eventAction.indexOf(" has disconnected");
                    pilotName = eventAction.substring(0, pilotEndIndex);
                    pilotName = checkPilotName(pilotName, true);
                    EventLogController.processActionEvent(SortieEvent.EventType.PILOTDISCONNECT, eventTime, pilotName, null, eventAction);
                } else if (eventAction.endsWith(" entered refly menu")) {
                    // Call pilot class again to close out a sortie if it has not already been done so.
                    pilotEndIndex = eventAction.indexOf(" entered refly menu");
                    pilotName = eventAction.substring(0, pilotEndIndex);
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processActionEvent(SortieEvent.EventType.PILOTREFLY, eventTime, pilotName, null, eventAction);
                } else if (eventAction.indexOf(" seat occupied by ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" seat occupied by ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), true);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    playerPosition = (Integer) pilotPlanePosition[2];
                    pilotName = checkPilotName(pilotName, false);
                    int eventLocationNDX = eventAction.lastIndexOf(" at ");
                    String eventLocation = eventAction.substring(eventLocationNDX + 4);
                    EventLogController.processSeatChangeEvent(SortieEvent.EventType.SELECTPLANE, eventTime, pilotName, planeName, eventLocation, playerPosition);
                } else if (eventAction.indexOf(" loaded weapons ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" loaded weapons ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processActionEvent(SortieEvent.EventType.LOADEDWEAPONS, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 16));
                } else if (eventAction.indexOf(" in flight at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" in flight at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processLocationEvent(SortieEvent.EventType.PILOTTAKEOFF, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 14));
                } else if (eventAction.indexOf(" landed at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" landed at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        EventLogController.processLocationEvent(SortieEvent.EventType.PILOTLANDED, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 11));
                    }
                } else if (eventAction.indexOf(" damaged on the ground at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" damaged on the ground at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        EventLogController.processLocationEvent(SortieEvent.EventType.PLANEDAMAGEDONGROUND, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 26));
                    }
                } else if (eventAction.indexOf(" damaged by landscape at ") > -1) {
                    // Plane has hit some solid object on the ground and been damaged
                    // (i.e. sandbags, wall, etc)
                    pilotEndIndex = eventAction.indexOf(" damaged by landscape at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        EventLogController.processLocationEvent(SortieEvent.EventType.PLANEDAMAGEDONGROUND, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 25));
                    }
                } else if (eventAction.indexOf(" damaged by NONAME at ") > -1) {
                    // Not sure why IL2 dies this, But the plane crashed into something on the ground that it did not recognize
                    // Plane has hit some solid object on the ground and been damaged
                    // (i.e. sandbags, wall, etc)
                    pilotEndIndex = eventAction.indexOf(" damaged by NONAME at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        EventLogController.processLocationEvent(SortieEvent.EventType.PLANEDAMAGEDONGROUND, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 22));
                    }
                } else if (eventAction.indexOf(" shot down by landscape at ") > -1) {
                    // A feature of IL2 which says you have been shot down by landscape.
                    // Don't know if this should be recorded or not ?
                    pilotEndIndex = eventAction.indexOf(" shot down by landscape at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        EventLogController.processLocationEvent(SortieEvent.EventType.PLANECRASHED, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 27));
                    }
                } else if (eventAction.indexOf(" crashed at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" crashed at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        EventLogController.processLocationEvent(SortieEvent.EventType.PLANECRASHED, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 12));
                    }
                } else if (eventAction.indexOf(" successfully bailed out at ") > -1) {
                    // I'm guessing this message comes up after the pilot makes it out of the plane
                    // No processing needed, but in case here's some basics
                    // pilotEndIndex = eventAction.indexOf("(0) successfully bailed out at ");
                    // pilotName = parsePilotPlane(eventAction.substring(0, pilotEndIndex))[0];
                    // planeName = parsePilotPlane(eventAction.substring(0, pilotEndIndex))[1];
                    // EventLogController.processLocationEvent(SortieEvents.EventType.PILOTBAILEDOUT, eventTime, pilotName, planeName,eventAction.substring(pilotEndIndex
                    // + 31));
                } else if (eventAction.indexOf(" bailed out at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" bailed out at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), true);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    playerPosition = (Integer) pilotPlanePosition[2];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        if (playerPosition == MainController.PILOTS.get(pilotName).getPlayerPosition()) {
                            EventLogController.processLocationEvent(SortieEvent.EventType.PILOTBAILEDOUT, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 15));
                        } else {
                            MainController.writeDebugLogFile(2, "EventLogParser.parseEvent - Bailed Event Seat Position (" + playerPosition + ") does not match Pilot(" + pilotName + ") position ("
                                    + MainController.PILOTS.get(pilotName).getPlayerPosition() + ")");
                        }
                    } else if (playerPosition == 0)  // Was the Pilot
                    {
                        MissionController.setMissionObjectLost(MainController.ACTIVEMISSION.getMissionObjects().get(pilotName));
                        MainController.writeDebugLogFile(2, "EventLogParser.parseEvent - Bailed Event AIPLANE(" + pilotName + ") Position " + playerPosition + ")");
                    }
                } else if (eventAction.indexOf(" was captured at ") > -1) {
                    // Self explanitory after a pilot bails out.
                    pilotEndIndex = eventAction.indexOf(" was captured at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), true);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    playerPosition = (Integer) pilotPlanePosition[2];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        if (playerPosition == MainController.PILOTS.get(pilotName).getPlayerPosition()) {
                            EventLogController.processLocationEvent(SortieEvent.EventType.PILOTCAPTURED, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 17));
                        } else {
                            MainController.writeDebugLogFile(2, "EventLogParser.parseEvent - Captured Event Seat Position (" + playerPosition + ") does not match Pilot(" + pilotName + ") position ("
                                    + MainController.PILOTS.get(pilotName).getPlayerPosition() + ")");
                        }
                    }
                } else if (eventAction.indexOf(" was wounded at ") > -1) {
                    // Tells when a Pilot has been wounded
                    pilotEndIndex = eventAction.indexOf(" was wounded at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), true);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    playerPosition = (Integer) pilotPlanePosition[2];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        if (playerPosition == MainController.PILOTS.get(pilotName).getPlayerPosition()) {
                            EventLogController.processLocationEvent(SortieEvent.EventType.PILOTWOUNDED, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 16));
                        } else {
                            MainController.writeDebugLogFile(2, "EventLogParser.parseEvent - Wounded Event Seat Position (" + playerPosition + ") does not match Pilot(" + pilotName + ") position ("
                                    + MainController.PILOTS.get(pilotName).getPlayerPosition() + ")");
                        }
                    }
                } else if (eventAction.indexOf(" was heavily wounded at ") > -1) {
                    // Tells when a Pilot has been Heavily wounded. Usually follows the above message
                    pilotEndIndex = eventAction.indexOf(" was heavily wounded at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), true);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    playerPosition = (Integer) pilotPlanePosition[2];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        if (playerPosition == MainController.PILOTS.get(pilotName).getPlayerPosition()) {
                            EventLogController.processLocationEvent(SortieEvent.EventType.PILOTHEAVILYWOUNDED, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 24));
                        } else {
                            MainController.writeDebugLogFile(2, "EventLogParser.parseEvent - Heavily Wounded Event Seat Position (" + playerPosition + ") does not match Pilot(" + pilotName + ") position ("
                                    + MainController.PILOTS.get(pilotName).getPlayerPosition() + ")");
                        }
                    }
                } else if (eventAction.indexOf(" was killed at ") > -1) {
                    // Call to pilot class and update their death count
                    pilotEndIndex = eventAction.indexOf(" was killed at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), true);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    playerPosition = (Integer) pilotPlanePosition[2];
                    pilotName = checkPilotName(pilotName, false);
                    if (!isMissionObject(pilotName)) {
                        if (playerPosition == MainController.PILOTS.get(pilotName).getPlayerPosition()) {
                            EventLogController.processLocationEvent(SortieEvent.EventType.PILOTKILLEDAT, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 15));
                        } else {
                            MainController.writeDebugLogFile(2, "EventLogParser.parseEvent - Killed AT Event Seat Position (" + playerPosition + ") does not match Pilot(" + pilotName + ") position ("
                                    + MainController.PILOTS.get(pilotName).getPlayerPosition() + ")");
                        }
                    }
                } else if (eventAction.indexOf(" shot down by ") > -1) {
                    victimEndIndex = eventAction.indexOf(" shot down by ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, victimEndIndex), false);
                    victimName = (String) pilotPlanePosition[0];
                    victimObject = (String) pilotPlanePosition[1];
                    victimName = checkPilotName(victimName, false);
                    EventLogController.processVictimEvent(SortieEvent.EventType.SHOTDOWN, eventTime, victimName, victimObject, eventAction.substring(victimEndIndex + 14));
                } else if (eventAction.indexOf(" was killed by ") > -1) {
                    victimEndIndex = eventAction.indexOf(" was killed by ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, victimEndIndex), true);
                    victimName = (String) pilotPlanePosition[0];
                    victimObject = (String) pilotPlanePosition[1];
                    victimName = checkPilotName(victimName, false);
                    playerPosition = (Integer) pilotPlanePosition[2];
                    if (!isMissionObject(victimName) && playerPosition == MainController.PILOTS.get(victimName).getPlayerPosition()) {
                        EventLogController.processVictimEvent(SortieEvent.EventType.PILOTKILLEDBY, eventTime, victimName, victimObject, eventAction.substring(victimEndIndex + 15));
                    } else {

                        if (isMissionObject(victimName)) {
                            MainController.writeDebugLogFile(2, "EventLogParser.parseEvent - AI Plane ( " + victimName + " ) Position (" + playerPosition + ") KIA");
                        } else {
                            MainController.writeDebugLogFile(2, "EventLogParser.parseEvent - Killed BY Event Seat Position (" + playerPosition + ") does not match Pilot(" + victimName + ") position ("
                                    + MainController.PILOTS.get(victimName).getPlayerPosition() + ")");
                        }
                    }
                } else if (eventAction.startsWith("BallisticDamage") || eventAction.startsWith("ExplosiveDamage")) {
                    String[] args = new String[2];
                    args[0] = eventAction.split("  damaged by  ")[0];
                    args[1] = eventAction.split("  damaged by  ")[1];
                    victimName = args[0].split(" ")[1];
                    String damage = args[0].split(" ")[2];
                    float damageValue = Float.parseFloat(args[0].split(" ")[3]);
                    // String damageType = args[0].split(" ")[2];
                    Event event = new Event(eventTime, SortieEvent.EventType.DAMAGED);
                    event.setDamage(damage);
                    event.setDamageValue(damageValue);
                    event.setVictimName(victimName);

                    int attackInfoEndIndex = args[1].lastIndexOf(" at ");
                    String attackerName = args[1].substring(0, attackInfoEndIndex).trim();
                    attackerName = (String) EventLogParser.parsePilotPlanePosition(attackerName, false)[0];
                    attackerName = EventLogParser.checkPilotName(attackerName, false);

                    event.setAttackerName(attackerName);

                    event.setLocation(EventLogController.getLocation(args[1].substring(attackInfoEndIndex + 4)));

                    EventLogController.processEvent(event);

                    // EventLogController.processObjectEvent(SortieEvent.EventType.DAMAGED, eventTime, victimName, args[1]);
                }

                else if (eventAction.indexOf(" damaged by ") > -1) {
                    victimEndIndex = eventAction.indexOf(" damaged by ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, victimEndIndex), false);
                    victimName = (String) pilotPlanePosition[0];
                    victimObject = (String) pilotPlanePosition[1];
                    if (isMissionObject(victimName)) {
                        EventLogController.processObjectEvent(SortieEvent.EventType.DAMAGED, eventTime, victimName, eventAction.substring(victimEndIndex + 12));
                    } else {
                        victimName = checkPilotName(victimName, false);
                        EventLogController.processVictimEvent(SortieEvent.EventType.PLANEDAMAGEDBY, eventTime, victimName, victimObject, eventAction.substring(victimEndIndex + 12));
                    }
                } else if (eventAction.indexOf(" was killed in his chute by ") > -1) {
                    // Someone's parachute was destroyed.
                    victimEndIndex = eventAction.indexOf(" was killed in his chute by ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, victimEndIndex), true);
                    victimName = (String) pilotPlanePosition[0];
                    victimObject = (String) pilotPlanePosition[1];
                    victimName = checkPilotName(victimName, false);
                    playerPosition = (Integer) pilotPlanePosition[2];
                    if (isMissionObject(victimName) || playerPosition == MainController.PILOTS.get(victimName).getPlayerPosition()) {
                        EventLogController.processVictimEvent(SortieEvent.EventType.PILOTCHUTEKILLED, eventTime, victimName, victimObject, eventAction.substring(victimEndIndex + 28));
                    } else {
                        MainController.writeDebugLogFile(2, "EventLogParser.parseEvent - Killed in Chute Event Seat Position (" + playerPosition + ") does not match Pilot(" + victimName + ") position ("
                                + MainController.PILOTS.get(victimName).getPlayerPosition() + ")");
                    }

                } else if (eventAction.indexOf("has chute destroyed by ") > -1) {
                    // Can also use "(0) has chute destroyed by " to tell when the pilot has been chute killed
                    // Victim's parachute was destroyed. No actionable event.
                    // victimEndIndex = eventAction.indexOf("(0) has chute destroyed by ");
                    // victimName = parsePilotPlane(eventAction.substring(0, victimEndIndex))[0];
                    // victimObject = parsePilotPlane(eventAction.substring(0, victimEndIndex))[1];
                    // EventLogController.processVictimEvent(SortieEvents.EventType.PILOTCHUTEDESTROYED, eventTime, victimName, victimObject,
                    // eventAction.substring(victimEndIndex +
                    // 27));
                } else if (eventAction.indexOf(" destroyed by ") > -1) {
                    // Set when one pilot or ground object destroys a ground target.
                    victimEndIndex = eventAction.indexOf(" destroyed by ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, victimEndIndex), false);
                    victimName = (String) pilotPlanePosition[0];
                    victimObject = (String) pilotPlanePosition[1];
                    EventLogController.processObjectEvent(SortieEvent.EventType.DESTROYED, eventTime, victimName, eventAction.substring(victimEndIndex + 14));
//					EventLogController.processObjectEvent(SortieEvent.EventType.DESTROYEDBY, eventTime, victimName, victimObject, eventAction
//							.substring(victimEndIndex + 14));
                }

                else if (eventAction.indexOf(" overrun by army ") > -1) {
                    // Base was overrun
                    String strArmy;
                    String[] overrunLine = eventAction.split(" ");
                    String aerodromeName = overrunLine[0];
                    int army = Integer.valueOf(overrunLine[4]);
                    if (army == MainController.REDARMY) {
                        strArmy = "Red";
                    } else {
                        strArmy = "Blue";
                    }
                    AerodromeController.aerodromeOverrun(MainController.ACTIVEMISSION, army, aerodromeName);
                    ServerCommandController.serverCommandSend("chat Aerodrome (" + aerodromeName + ") overrun by " + strArmy + " TO ALL");
                }

                // / MDS RRR Events

                else if (eventAction.indexOf(" rearmed bombs at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" rearmed bombs at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_REARM_BOMBS, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 18));
                } else if (eventAction.indexOf(" rearmed guns at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" rearmed guns at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_REARM_GUNS, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 17));
                } else if (eventAction.indexOf(" rearmed rockets at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" rearmed rockets at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_REARM_ROCKETS, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 20));
                } else if (eventAction.indexOf(" unloaded bombs at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" unloaded bombs at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_UNLOAD_BOMBS, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 19));
                } else if (eventAction.indexOf(" unloaded bullets at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" unloaded bullets at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_UNLOAD_GUNS, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 21));
                } else if (eventAction.indexOf(" unloaded rockets at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" unloaded rockets at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_UNLOAD_ROCKETS, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 21));
                } else if (eventAction.indexOf(" refueled aircraft to >") > -1) {
                    pilotEndIndex = eventAction.indexOf(" refueled aircraft to >");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    String eventActionString = eventAction.substring(pilotEndIndex + 23);
                    String fuelLoadString = eventActionString.substring(0, eventActionString.indexOf(" at"));
                    String locationString = eventActionString.substring(fuelLoadString.length());
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_FUEL_REFUEL, eventTime, pilotName, planeName, locationString + " >" + fuelLoadString);
//                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_FUEL_REFUEL, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 23));
                } else if (eventAction.indexOf(" unloaded fuel at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" unloaded fuel at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_FUEL_UNLOAD, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 18));
                } else if (eventAction.indexOf(" repaired aircraft at ") > -1) {
                    pilotEndIndex = eventAction.indexOf(" repaired aircraft at ");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_REPAIR_AIRCRAFT, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 22));
                } else if (eventAction.indexOf(" repaired engine >") > -1) {
                    pilotEndIndex = eventAction.indexOf(" repaired engine >");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    String eventActionString = eventAction.substring(pilotEndIndex + 18);
                    String engineString = eventActionString.substring(0, eventActionString.indexOf(" at"));
                    String locationString = eventActionString.substring(engineString.length());
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_REPAIR_ENGINE, eventTime, pilotName, planeName, locationString + " >" + engineString);
//                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_REPAIR_ENGINE, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 18));
                } else if (eventAction.indexOf(" changed loadout to >") > -1) {
                    pilotEndIndex = eventAction.indexOf(" changed loadout to >");
                    Object[] pilotPlanePosition = parsePilotPlanePosition(eventAction.substring(0, pilotEndIndex), false);
                    pilotName = (String) pilotPlanePosition[0];
                    planeName = (String) pilotPlanePosition[1];
                    pilotName = checkPilotName(pilotName, false);
                    String eventActionString = eventAction.substring(pilotEndIndex + 21);
                    String loadoutString = eventActionString.substring(0, eventActionString.indexOf(" at"));
                    String locationString = eventActionString.substring(loadoutString.length());
                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_CHANGE_LOADOUT, eventTime, pilotName, planeName, locationString + " >" + loadoutString);
//                    EventLogController.processLocationEvent(SortieEvent.EventType.MDS_RRR_CHANGE_LOADOUT, eventTime, pilotName, planeName, eventAction.substring(pilotEndIndex + 21));
                }

            } catch (Exception ex) {
                String log = "EventLogParser.parseEvent - Error parsing the following line in event log:\n" + " **       " + eventLine + " ** ERROR:" + ex.getMessage();
                MainController.writeDebugLogFile(1, log);
            }
        } // End if
    }

    public static void main(String args[]) {
//		String line = "[10:57:10 PM] TUSA/Blujay:SpitfireMkIXeCLP:25259:1 shot down by Gryphon:Bf-109G-14early:15670:2 at 102828.34 64325.44";
//		String line = "[12:54:37 AM] 207_Static destroyed by Vampyre:Fw-190G-8 at 73727.88 200876.9";
//      String line = "[5:56:02 AM] 67_Static:48826:2 shot down by 615otu_Stirwenn:Pe-2series359:72377:1 at 95435.336 102089.72";
//		String line = "[9:59:16 AM] RAF238WildWillie:Ju-52/3mg4e(-1) was captured at 94028.88 151045.89";
//		String line = "[7:33:37 AM] *|Kz|Papago*yT:B5N2(0) successfully bailed out at 99497.32 52749.887";
//		String line = "[7:51:09 AM] FAe_Dutchie:F4F-3 shot down by landscape at 117260.66 41167.105";
//     	String line = "[9:15:08 AM] Underdog:F6F-3 shot down by KU_Tetsu:A6M3a-22ko at 200482.89 201202.8";
//		String line = "[1:11:11 AM] 12AF_86FBG_526FBS01{12AF_86FBG_526FBS011;B-29;1} damaged by NONAME{RAF238WildWillie_1;RAF238WildWillie:MiG-15(bis)late;2} at 31582.875 399780.56";
//		String line = "[1:13:03 AM] NONAME{RAF238WildWillie_0;RAF238WildWillie:MiG-15(bis)late;2} shot down by 12AF_86FBG_526FBS01{12AF_86FBG_526FBS012;B-29;1} at 40653.17 407765.62";
//		String line = "[4:23:24 PM] 12AF_86FBG_527FBS01{12AF_86FBG_527FBS011;B-29;1} damaged by 12AF_86FBG_527FBS01{12AF_86FBG_527FBS012;B-29;1} at 53247.566 363020.72";
//		String line = "[7:11:37 AM] D3A1(1) was wounded at 260433.44 35281.707";
//      String line = "[8:42:23 PM] ExplosiveDamage 12_Static Hull2 1.3894584  damaged by  [JASP]-ERIK_0  at 78079.08 87576.06";
//      String line = "[7:12:08 PM] ExplosiveDamage:NearMiss 241_Static Hull2 0.052989047  damaged by  419_Static  at 103283.38 115592.54";
//      String line = "[5:23:59 AM] BallisticDamage 578_Static Body 0.49415022  damaged by  586_Static  at 122400.64 137685.69";

//        String line = "[11:17:40 PM] gerula:Fw-190D-11(0) seat occupied by gerula at 0.0 0.0";
//        Pilot pilot = new Pilot("gerula", "1.2.3.4", 0, 0);
//        MainController.PILOTS = new HashMap<String, Pilot>();
//        MainController.PILOTS.put("gerula", pilot);
//        
//        MainController.SORTIES = new HashMap<String, PilotSortie>();
//        MainController.ACTIVEMISSION = new Mission();

        String line = "[11:21:18 PM] UM_NN00{UM_NN003;P-51C-NT;1} damaged by gerula{gerula_0;gerula:Fw-190D-11;2} at 58791.664 24681.596";

        parseEvent(Time.getTime(), line);
    }
}
