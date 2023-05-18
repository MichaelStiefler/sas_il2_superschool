package mainController;

import java.io.File;

import model.AIWing;
import model.Aerodrome;
import model.ConfigurationItem;
import model.Event;
import model.IL2StaticObject;
import model.Location;
import model.MissionFile;
import model.MissionObject;
import model.MissionObjectEvent;
import model.Pilot;
import model.PilotBlueprint;
import model.PilotSortie;
import model.SortieEvent;
import utility.Coordinates;
import utility.Time;

class EventLogController {

    public static void processMissionEvent(SortieEvent.EventType eventType, long eventTime) {
        switch (eventType) {
            case MISSIONBEGIN:
                try {
                    MainController.ACTIVEMISSION.setStartTime(eventTime);
                    MainController.writeDebugLogFile(2, "EventLogController.processMissionEvent - Mission Begin Received");
                    MySQLConnectionController.newMission();
                    MissionStatusPanelController.initializeData(MainController.ACTIVEMISSION);
                    PilotPanelController.refreshPilotPanel();
                    MySQLConnectionController.writeActiveMission(MainController.ACTIVEMISSION);
                    MySQLConnectionController.writeActivePilots();
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.MISSIONBEGIN - Error: " + ex);
                }
                break;
            case MISSIONEND:
                try {
                    if (MainController.ACTIVEMISSION.getStartTime() > 0) {
                        PilotSortieController.sortieEndAll(SortieEvent.EventType.MISSIONEND, eventTime);
                        MainController.ACTIVEMISSION.setEndTime(Time.getTime());
                        // TODO: Added by Storebror to avoid flooding the Stats with missions where no sorties have been flown.
                        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        if (MySQLConnectionController.hasSorties(MainController.ACTIVEMISSION)) {
                            MySQLConnectionController.endMission(MainController.ACTIVEMISSION);
                            MySQLConnectionController.validateSorties();
                        } else {
                            MySQLConnectionController.removeUnflownMission(MainController.ACTIVEMISSION);
                        }
//                        MySQLConnectionController.endMission(MainController.ACTIVEMISSION);
//                        MySQLConnectionController.validateSorties();
                        // ----------------------------------------------------------------------------------------------------
                        MainController.MISSIONCONTROL.setMissionEndReceived(true);
                        MainController.writeDebugLogFile(2, "EventLogController.processMissionEvent - Mission End Received");
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.MISSIONEND - Error: " + ex);
                }
                break;
            default:
                break;

        }
    }

    public static void processMissionWon(SortieEvent.EventType eventType, int victor, String eventLine) {
        try {
            // set temp mission
            int startMissionName = eventLine.lastIndexOf("[") + 1;
            String newMissionStuff = eventLine.substring(startMissionName, eventLine.lastIndexOf("]"));
            String missionName = newMissionStuff.split(",")[0];
            String difficultyName = newMissionStuff.split(",")[1];
            MissionFile tempMission = null;

            if (tempMission == null) {
                // check to see if there is a directory embedded in name
                String directoryMarker = File.separator;
                if (missionName.indexOf(directoryMarker) > -1) {
                    int startMissionNameIndex = missionName.lastIndexOf(directoryMarker);
                    String missionDirectory = missionName.substring(0, startMissionNameIndex);
                    missionName = missionName.substring(startMissionNameIndex + 1);
                    if (missionName.lastIndexOf(".mis") > -1) {
                        missionName = missionName.substring(0, missionName.lastIndexOf(".mis"));
                    }
                    tempMission = new MissionFile(missionName);
                    tempMission.setDirectory(missionDirectory);
                } else {
                    tempMission = new MissionFile(missionName);
                }
            }
            tempMission.setDifficulty(difficultyName);
            Boolean validMission = MainController.validateMission(tempMission);
            if (validMission) {
                MainController.MISSIONCONTROL.setTempMission(tempMission);
            } else {
                MainController.writeDebugLogFile(1, "EventLogController.processMissionWon - Unable to open Mission ( " + missionName + " ) in path ( " + MainController.CONFIG.getServerDirectory() + "Missions/" + MainController.MISSIONPATH + " )");
            }
            MainController.ACTIVEMISSION.setWinner(victor);
            MainController.MISSIONCONTROL.setMissionOver(true);
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.processMissionWon - Error Unhandled Exception: " + ex);
        }
    }

    public static void processActionEvent(SortieEvent.EventType eventType, long eventTime, String pilotName, String planeName, String eventAction) {
        SortieEvent pilotSortieEvent;

        Pilot pilot = null;
        pilot = MainController.PILOTS.get(pilotName);

        switch (eventType) {
            case PILOTCONNECT:
                try {
                    if (pilot != null) {
                        MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") connected OK");
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PILOTCONNECT - Error: " + ex);
                }
                break;
            case PILOTDISCONNECT:
                try {
                    if (pilot == null) {
                        MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") Disconnected OK");
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PILOTDISCONNECT - Error: " + ex);
                }
                break;
            case PILOTREFLY:
                try {
                    if (pilot != null) {
                        pilot.setState(PilotBlueprint.ValidStates.REFLY);
                    }
                    PilotSortieController.sortieEnd(pilotName, eventType, eventTime);
                    PilotPanelController.updatePilot(pilotName);
                    MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PILOTREFLY - Error: " + ex);
                }
                break;
            case LOADEDWEAPONS:
                try {
                    MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                    String loadOut = eventAction;
                    int fuelIndex = loadOut.indexOf(" fuel ");
                    String weapons = loadOut.substring(1, fuelIndex - 1);
                    String fuelLoad = loadOut.substring(fuelIndex + 6);
                    PilotSortie sortie = null;
                    sortie = MainController.SORTIES.get(pilotName);

                    // Check to make sure Pilot has a Sortie & is not being kicked
                    if (sortie == null && pilot.getState() != PilotBlueprint.ValidStates.KICK) {
                        MainController.writeDebugLogFile(1, "EventLogController.LOADEDWEAPONS - Error No Sortie defined for Pilot (" + pilotName + ")");
                        PilotSortieController.newSortie(pilotName);
                        sortie = MainController.SORTIES.get(pilotName);
                    }
                    if (pilot == null) {
                        MainController.writeDebugLogFile(1, "EventLogController.LOADEDWEAPONS - Error Pilot record does not exist for (" + pilotName + ")");
                    }

                    // Make sure Pilot Id exists
                    if (pilot.getPilotId() == 0) {
                        pilot.setPilotId(MySQLConnectionController.getPilotId(MainController.PILOTS.get(pilotName).getName()));
                    }
                    PilotSortieController.sortieStart(sortie);
                    if (sortie != null) {
                        sortie.setPlane(planeName);
                        sortie.setWeapons(weapons);
                        sortie.setFuel(fuelLoad);
                        pilot.setState(PilotBlueprint.ValidStates.SORTIEBEGIN);
                        pilotSortieEvent = new SortieEvent(eventTime, eventType, pilot.getPilotId());
                        sortie.setSortieEvents(eventTime, pilotSortieEvent);
                        sortie.setPilotStatus(PilotSortie.PilotStatus.OK);
                        sortie.setPlaneStatus(PilotSortie.PlaneStatus.OK);
                        // Update pilot's 'user' information
                        new CommandUser(pilot, 0);
                    } else {
                        MainController.writeDebugLogFile(1, "EventLogController.processActionEvent(LOADWEAPONS) Error - Sortie not found for Pilot(" + pilotName + ")");
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.processActionEvent(LOADEDWEAPONS) - Error unhandled exception pilot(" + pilotName + ") plane(" + planeName + ") action(" + eventAction + "): " + ex);
                    if (pilot != null) {
                        new CommandUser(pilot, 0);
                    }
                }
                break;
            default:
                break;
        }
    }

    public static void processSeatChangeEvent(SortieEvent.EventType eventType, long eventTime, String pilotName, String planeName, String eventAction, int playerPosition) {
        Pilot pilot = null;

        try {
            pilot = MainController.PILOTS.get(pilotName);
            if (pilot == null) {
                MainController.writeDebugLogFile(1, "EventLogController.processSeatChangeEvent - Error Pilot(" + pilotName + ") not found in Pilots List");
            }
            if (!MainController.SORTIES.containsKey(pilotName)) {
                PilotSortieController.newSortie(pilotName);
            }
            // Set the Player position within Plane
            MainController.PILOTS.get(pilotName).setPlayerPosition(playerPosition);
            // The "Selected Seat Event can also mean the start of a sortie, so check just in case.
            if (MainController.PILOTS.get(pilotName).getState() == PilotBlueprint.ValidStates.CONNECTED || MainController.PILOTS.get(pilotName).getState() == PilotBlueprint.ValidStates.REFLY) {
                processLocationEvent(SortieEvent.EventType.SELECTPLANE, eventTime, pilotName, planeName, eventAction);
            } else {
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") Changed Seats");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.CHANGEDPOSITION - Error unhandled Exception for pilot(" + pilotName + ") plane(" + planeName + "): " + ex);
        }

    }

    public static Location getLocation(String locationString) {
        double locationX;
        double locationY;
        String mapGrid;
        Location location = new Location();
//        location.setLocationX(0);
//        location.setLocationY(0);

        try {
            locationX = Double.valueOf(locationString.split(" ")[0]);
            locationY = Double.valueOf(locationString.split(" ")[1]);
            if (Double.isNaN(locationX)) {
                locationX = 0.0;
            }
            if (Double.isNaN(locationY)) {
                locationY = 0.0;
            }
            location.setLocationX(locationX);
            location.setLocationY(locationY);

            mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), locationX, locationY);
            location.setMapGridReference(mapGrid);

            return location;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.getLocation - Exception with Location " + ex);
            MainController.writeDebugLogFile(1, " ** EventLog location: " + locationString);
            return location;
        }
    }

    public static void processLocationEvent(SortieEvent.EventType eventType, long eventTime, String pilotName, String planeName, String eventAction) {
        SortieEvent pilotSortieEvent = null;
        Location eventLocation;
        Pilot pilot = null;
        MissionObject missionObject = null;

        eventLocation = getLocation(eventAction);

        switch (eventType) {
            case SELECTPLANE:
                try {
                    pilot = MainController.PILOTS.get(pilotName);
                    if (pilot == null) {
                        MainController.writeDebugLogFile(1, "EventLogController.processLocationEvent(SELECTPLANE) - Error Pilot(" + pilotName + ") not found in Pilots List");
                    }
                    if (!MainController.SORTIES.containsKey(pilotName)) {
                        PilotSortieController.newSortie(pilotName);
                    }
                    MainController.SORTIES.get(pilotName).setSpawnPointX(eventLocation.getLocationX());
                    MainController.SORTIES.get(pilotName).setSpawnPointY(eventLocation.getLocationY());

                    MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType + " Spawn Location( " + eventLocation.getLocationX() + "/" + eventLocation.getLocationY() + " ) Plane ( " + planeName + " )");
                    pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.SELECTPLANE - Error unhandled Exception for pilot(" + pilotName + ") plane(" + planeName + "): " + ex);
                }

                break;
            case PILOTTAKEOFF:
                try {
                    if (MainController.SORTIES.get(pilotName).getPlane().equals("Unknown")) {
                        MainController.SORTIES.get(pilotName).setPlane(planeName);
                        MainController.writeDebugLogFile(1, "EventLogController.processLocationEvent(PILOTTAKEOFF) - Error Plane name was Unknown for (" + pilotName + ")");
                    }
                    MainController.PILOTS.get(pilotName).setState(PilotBlueprint.ValidStates.INFLIGHT);
                    pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                    MainController.SORTIES.get(pilotName).setPlaneStatus(PilotSortie.PlaneStatus.INFLIGHT);
                    // Update GUI Pilots page.
                    PilotPanelController.updatePilot(pilotName);
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PILOTTAKEOFF - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;
            case PILOTLANDED:
                try {
                    pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());

                    if (MainController.SORTIES.containsKey(pilotName)) {
                        MainController.SORTIES.get(pilotName).setPlaneStatus(PilotSortie.PlaneStatus.LANDED);
                    }
                    PilotPanelController.updatePilot(pilotName);
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PILOTLANDED - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;
            case PLANEDAMAGEDONGROUND:
                try {

                    if (MainController.SORTIES.containsKey(pilotName)) {
                        MainController.SORTIES.get(pilotName).setPlaneStatus(PilotSortie.PlaneStatus.DAMAGED);
                    }
                    // If the Pilot has not taken off yet and we count Air Kills on Ground then mark plane as not OK to Fly.
                    if (MainController.CONFIG.getCountFAirKillonGround() && MainController.PILOTS.get(pilotName).getState() == PilotBlueprint.ValidStates.SORTIEBEGIN
                            || MainController.PILOTS.get(pilotName).getState() != PilotBlueprint.ValidStates.SORTIEBEGIN) {
                        MainController.PILOTS.get(pilotName).setPlaneIsOKToFly(false);
                        pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PLANEDAMAGEDONGROUND - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;
            case PLANECRASHED:
                // Updated this to include Static objects as the EventLog sometimes shows them as being crashed.
                try {
                    String mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), eventLocation.getLocationX(), eventLocation.getLocationY());
                    pilot = MainController.PILOTS.get(pilotName);
                    // See if it was a Pilot that Crashed
                    if (pilot != null) {
                        pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                        // Increase the Total Plane lost counter
                        // Check to see if plane crashed before takeoff, if so only count plane
                        // lost limit if configuration flag is set.
                        if (MainController.CONFIG.getCountFAirKillonGround() && MainController.PILOTS.get(pilotName).getState() == PilotBlueprint.ValidStates.SORTIEBEGIN
                                || MainController.PILOTS.get(pilotName).getState() != PilotBlueprint.ValidStates.SORTIEBEGIN) {
                            MainController.PILOTS.get(pilotName).setPlaneIsOKToFly(false);
                            MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, MainController.SORTIES.get(pilotName).getArmy(), IL2StaticObject.ObjectType.PLANE, 1);
                        }

                        if (MainController.SORTIES.containsKey(pilotName)) {
                            MainController.SORTIES.get(pilotName).setPlaneStatus(PilotSortie.PlaneStatus.CRASHED);
                        }
                        MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                    } else if (EventLogParser.isAIWing(pilotName) != null) {
                        // AI Plane Crashed
                        AIWing aiWing = EventLogParser.isAIWing(pilotName);
                        MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, aiWing.getArmy(), IL2StaticObject.ObjectType.AIPLANE, 1);
                        MissionObjectEvent missionObjectEvent = new MissionObjectEvent(eventTime, SortieEvent.EventType.CRASHED, 0);
                        missionObjectEvent.setMissionObjectName(aiWing.getName());
                        missionObjectEvent.setArmy(aiWing.getArmy());
                        missionObjectEvent.setIL2ObjectId(0);
                        missionObjectEvent.setIL2ObjectName(aiWing.getPlane());
                        missionObjectEvent.setIL2ObjectType(IL2StaticObject.ObjectType.AIPLANE);
                        missionObjectEvent.setLocationX(eventLocation.getLocationX());
                        missionObjectEvent.setLocationY(eventLocation.getLocationY());
                        missionObjectEvent.setTargetGrid(mapGrid);
                        MySQLConnectionController.writeMissionObjectEvent(missionObjectEvent);
                        MainController.writeDebugLogFile(2, "AIPlane (" + pilotName + "/" + aiWing.getPlane() + ") CRASHED");
                    } else if (MainController.ACTIVEMISSION.getMissionObjects().get(pilotName) != null) {
                        missionObject = MainController.ACTIVEMISSION.getMissionObjects().get(pilotName);
                        if (missionObject != null) {
                            // It was a Static Object that crashed so set object as lost and Record a MissionObjectEvent
                            MissionController.setMissionObjectLost(missionObject);
                            MissionObjectEvent missionObjectEvent = new MissionObjectEvent(eventTime, SortieEvent.EventType.CRASHED, missionObject.getGroundObject().getIl2ObjectID());
                            missionObjectEvent.setMissionObjectName(missionObject.getMissionObjectName());
                            missionObjectEvent.setArmy(missionObject.getArmy());
                            missionObjectEvent.setIL2ObjectId(missionObject.getGroundObject().getIl2ObjectID());
                            missionObjectEvent.setIL2ObjectName(missionObject.getGroundObject().getObjectName());
                            missionObjectEvent.setIL2ObjectType(missionObject.getGroundObject().getObjectType());
                            missionObjectEvent.setOpponentId(missionObject.getGroundObject().getIl2ObjectID());
                            missionObjectEvent.setOpponentName(missionObject.getGroundObject().getObjectName());
                            missionObjectEvent.setOpponentArmy(missionObject.getArmy());
                            missionObjectEvent.setOpponentObjectType(missionObject.getGroundObject().getObjectType());
                            missionObjectEvent.setLocationX(eventLocation.getLocationX());
                            missionObjectEvent.setLocationY(eventLocation.getLocationY());
                            missionObjectEvent.setTargetGrid(mapGrid);
                            MySQLConnectionController.writeMissionObjectEvent(missionObjectEvent);
                            MainController.writeDebugLogFile(2, "EventLogController.processLocationEvent - Mission Object (" + pilotName + "/" + missionObject.getGroundObject().getObjectName() + ") CRASHED");
                        }
                    } else {
                        // Unknown Object Crashed
                        MainController.writeDebugLogFile(1, "EventLogController.processLocationEvent - Unknown Object (" + pilotName + ") Crashed");
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PLANECRASHED - Error: " + ex);
                }
                break;
            case PILOTBAILEDOUT:
                try {
                    pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());

                    if (MainController.SORTIES.containsKey(pilotName)) {
                        MainController.SORTIES.get(pilotName).setBailedOut(true);
                        MainController.SORTIES.get(pilotName).setPilotStatus(PilotSortie.PilotStatus.BAILEDOUT);
                        MainController.SORTIES.get(pilotName).setPlaneStatus(PilotSortie.PlaneStatus.CRASHED);
                    }
                    PilotPanelController.updatePilot(pilotName);
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PILOTBAILEDOUT - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;
            case PILOTCAPTURED:
                try {
                    pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());

                    MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, MainController.PILOTS.get(pilotName).getArmy(), IL2StaticObject.ObjectType.PILOT, 1);
                    if (MainController.SORTIES.containsKey(pilotName)) {
                        MainController.SORTIES.get(pilotName).setPilotStatus(PilotSortie.PilotStatus.CAPTURED);
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PILOTCAPTURED - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;
            case PILOTWOUNDED:
                try {
                    if (MainController.CONFIG.getCountFAirKillonGround() && MainController.PILOTS.get(pilotName).getState() == PilotBlueprint.ValidStates.SORTIEBEGIN
                            || MainController.PILOTS.get(pilotName).getState() != PilotBlueprint.ValidStates.SORTIEBEGIN) {
                        pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                    }
                    if (MainController.SORTIES.containsKey(pilotName)) {
                        MainController.SORTIES.get(pilotName).setPilotStatus(PilotSortie.PilotStatus.WOUNDED);
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PILOTWOUNDED - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;
            case PILOTHEAVILYWOUNDED:
                try {
                    if (MainController.CONFIG.getCountFAirKillonGround() && MainController.PILOTS.get(pilotName).getState() == PilotBlueprint.ValidStates.SORTIEBEGIN
                            || MainController.PILOTS.get(pilotName).getState() != PilotBlueprint.ValidStates.SORTIEBEGIN) {
                        pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                    }
                    if (MainController.SORTIES.containsKey(pilotName)) {
                        MainController.SORTIES.get(pilotName).setPilotStatus(PilotSortie.PilotStatus.WOUNDED);
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PILOTHEAVILYWOUNDED - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;
            case PILOTKILLEDAT:
                try {
                    if (MainController.CONFIG.getCountFAirKillonGround() && MainController.PILOTS.get(pilotName).getState() == PilotBlueprint.ValidStates.SORTIEBEGIN
                            || MainController.PILOTS.get(pilotName).getState() != PilotBlueprint.ValidStates.SORTIEBEGIN) {
                        MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, MainController.PILOTS.get(pilotName).getArmy(), IL2StaticObject.ObjectType.PILOT, 1);
                        pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                        MainController.PILOTS.get(pilotName).setDeathCount(MainController.PILOTS.get(pilotName).getDeathCount() + 1);
                    }
                    if (MainController.SORTIES.containsKey(pilotName)) {
                        MainController.SORTIES.get(pilotName).setPilotStatus(PilotSortie.PilotStatus.KIA);
                    }
                    PilotPanelController.updatePilot(pilotName);
                    MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                    PilotController.deathKick(MainController.PILOTS.get(pilotName));
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.PILOTKILLEDAT - Error: " + ex);
                }
                break;

            // MDS RRR Events
            case MDS_RRR_REARM_BOMBS:
            case MDS_RRR_REARM_GUNS:
            case MDS_RRR_REARM_ROCKETS:
            case MDS_RRR_UNLOAD_BOMBS:
            case MDS_RRR_UNLOAD_GUNS:
            case MDS_RRR_UNLOAD_ROCKETS:
            case MDS_RRR_FUEL_UNLOAD:
            case MDS_RRR_REPAIR_AIRCRAFT:
                try {
                    pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController." + eventType.toString() + " - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;

            case MDS_RRR_FUEL_REFUEL:
                try {
                    pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                    int refuelStartIndex = eventAction.indexOf(">") + 1;
                    int refuelEndIndex = eventAction.indexOf("kg<");
                    pilotSortieEvent.setOpponentName(eventAction.substring(refuelStartIndex, refuelEndIndex));
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController." + eventType.toString() + " - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;
            case MDS_RRR_REPAIR_ENGINE:
                try {
                    pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                    int engineStartIndex = eventAction.indexOf(">") + 1;
                    int engineEndIndex = eventAction.indexOf("<");
                    pilotSortieEvent.setOpponentName(eventAction.substring(engineStartIndex, engineEndIndex));
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController." + eventType.toString() + " - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;
            case MDS_RRR_CHANGE_LOADOUT:
                try {
                    pilotSortieEvent = new SortieEvent(eventTime, eventType, MainController.PILOTS.get(pilotName).getPilotId());
                    int loadoutStartIndex = eventAction.indexOf(">") + 1;
                    int loadoutEndIndex = eventAction.indexOf("<");
                    pilotSortieEvent.setOpponentName(eventAction.substring(loadoutStartIndex, loadoutEndIndex));
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController." + eventType.toString() + " - Error: " + ex);
                }
                MainController.writeDebugLogFile(2, "Pilot (" + pilotName + ") " + eventType);
                break;

            default:
                break;
        } // End of switch/case statement

        try {
            // Add Sortie Event for Pilot
            if (pilotSortieEvent != null) {
                pilotSortieEvent.setLocationX(eventLocation.getLocationX());
                pilotSortieEvent.setLocationY(eventLocation.getLocationY());
                String mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), eventLocation.getLocationX(), eventLocation.getLocationY());
                pilotSortieEvent.setTargetGrid(mapGrid);
                MainController.SORTIES.get(pilotName).setSortieEvents(eventTime, pilotSortieEvent);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.processLocationEvent Error updating SortieEvent: " + ex);
        }

    } // End processLocationevent

    private static void processPilotAttackedObjectEvent(SortieEvent.EventType eventType, long eventTime, MissionObject missionObject, Pilot pilot, Location eventLocation) {
        String victimName;
        SortieEvent attackerSortieEvent = null;
        MainController.writeDebugLogFile(2, eventTime + " - Attacker (" + pilot.getName() + ") " + eventType + " (" + missionObject.getMissionObjectName() + "/" + missionObject.getGroundObject().getObjectName() + ")");

        if (MainController.SORTIES.containsKey(pilot.getName())) {
            if (missionObject.getObjectType() == IL2StaticObject.ObjectType.AIPLANE) {
                victimName = missionObject.getMissionObjectName() + "/" + missionObject.getGroundObject().getDisplayName();
            } else {
                victimName = missionObject.getGroundObject().getDisplayName();
            }
            attackerSortieEvent = new SortieEvent(eventTime, eventType, pilot.getPilotId());
            attackerSortieEvent.setOpponentId(missionObject.getGroundObject().getIl2ObjectID());
            attackerSortieEvent.setOpponentName(victimName);
            attackerSortieEvent.setOpponentArmy(missionObject.getArmy());
            attackerSortieEvent.setOpponentObjectType(missionObject.getObjectType());
            attackerSortieEvent.setLocationX(eventLocation.getLocationX());
            attackerSortieEvent.setLocationY(eventLocation.getLocationY());
            attackerSortieEvent.setTargetGrid(missionObject.getTargetGrid());
            MainController.SORTIES.get(pilot.getName()).setSortieEvents(eventTime, attackerSortieEvent);

            if (eventType == SortieEvent.EventType.DESTROYED) {
                MissionController.setMissionObjectLost(missionObject);
            }

            if (missionObject.getArmy() == MainController.SORTIES.get(pilot.getName()).getArmy()) {
                announceFriendlyFireMessage(pilot, victimName, eventType);
                // Send out Friendly Fire message based on Configuration Setting
                // if (MainController.CONFIG.getAnnounceFriendlyFire() != ConfigurationItem.ChatReciepients.NONE)
                // {
                // String receipient;
                // if (MainController.CONFIG.getAnnounceFriendlyFire() == ConfigurationItem.ChatReciepients.PILOT)
                // {
                // receipient = UnicodeFormatter.convertUnicodeToString(pilot.getName());
                // }
                // else if (MainController.CONFIG.getAnnounceFriendlyFire() == ConfigurationItem.ChatReciepients.ARMY)
                // {
                // receipient = "ARMY "+Integer.toString(missionObject.getArmy());
                // }
                // else
                // {
                // receipient = MainController.CONFIG.getAnnounceFriendlyFire().toString();
                // }
                // ServerCommandController.serverCommandSend("chat ** ( " + UnicodeFormatter.convertUnicodeToString(pilot.getName()) + " )  CEASE FIRE ** you are attacking a FRIENDLY TO \"" + receipient + "\"");
                // }
                // Since they attacked a Friendly Target subtract twice the value of the target
                MainController.SORTIES.get(pilot.getName()).setBonusPoints(MainController.SORTIES.get(pilot.getName()).getBonusPoints() + missionObject.getGroundObject().getPointValue() * -2);
            } else {
                // Add any bonus points for the Target
                MainController.SORTIES.get(pilot.getName()).setBonusPoints(MainController.SORTIES.get(pilot.getName()).getBonusPoints() + missionObject.getGroundObject().getPointValue());
                // Check to see if action was near a spawn base
                baseAttack(pilot, missionObject.getGroundObject().getObjectName(), missionObject.getArmy(), eventType, eventLocation.getLocationX(), eventLocation.getLocationY());
            }

            // If pilot Destroyed the object then increase their Ground object counter
            if (missionObject.getArmy() == pilot.getArmy() && eventType == SortieEvent.EventType.DESTROYED) {
                pilot.setFGroundUnit(pilot.getFGroundUnit() + 1);
            } else {
                pilot.setEGroundUnit(pilot.getEGroundUnit() + 1);
            }

            if (pilot.getFGroundUnit() >= MainController.CONFIG.getFriendlyGroundBan()) {
                MainController.writeDebugLogFile(1, "Banning Pilot(" + pilot.getName() + ") for Friendly Ground Kills( " + pilot.getFGroundUnit() + " )");
                SortieEvent banSortieEvent = new SortieEvent(eventTime, SortieEvent.EventType.PILOTBANNEDFGROUND, pilot.getPilotId());
                banSortieEvent.setLocationX(MainController.CONFIG.getFKillBanDuration());
                MainController.SORTIES.get(pilot.getName()).setSortieEvents((eventTime + 1), banSortieEvent);
                PilotBanController.pilotBanAdd(pilot.getName(), pilot.getIPAddress(), "Pilot Reached Friendly Ground Kill ban limit", Math.round(MainController.CONFIG.getFKillBanDuration() * 3600000.0));
            }
        }
        try {
            if (attackerSortieEvent != null) {
                MainController.SORTIES.get(pilot.getName()).setSortieEvents(eventTime, attackerSortieEvent);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.processPilotAttackedObjectEvent writing Sortie Events: " + ex);
        }

    }

    private static void processPilotAttackedObjectEvent(Event event) {
//		Boolean friendlyFire = false;
        String victimName;
        SortieEvent attackerSortieEvent = null;
        Pilot pilot = (Pilot) event.getAttackerObject();
        MissionObject missionObject = (MissionObject) event.getVictimObject();
        MainController.writeDebugLogFile(2, event.getEventTime() + " - Attacker (" + pilot.getName() + ") " + event.getEventType() + " (" + missionObject.getMissionObjectName() + "/" + missionObject.getGroundObject().getObjectName() + ")");

        if (MainController.SORTIES.containsKey(pilot.getName())) {
            if (missionObject.getObjectType() == IL2StaticObject.ObjectType.AIPLANE) {
                victimName = missionObject.getMissionObjectName() + "/" + missionObject.getGroundObject().getDisplayName();
            } else {
                victimName = missionObject.getGroundObject().getDisplayName();
            }
            attackerSortieEvent = new SortieEvent(event.getEventTime(), event.getEventType(), pilot.getPilotId());
            attackerSortieEvent.setOpponentId(missionObject.getGroundObject().getIl2ObjectID());
            attackerSortieEvent.setOpponentName(victimName);
            attackerSortieEvent.setOpponentArmy(missionObject.getArmy());
            attackerSortieEvent.setOpponentObjectType(missionObject.getObjectType());
            attackerSortieEvent.setLocationX(event.getLocation().getLocationX());
            attackerSortieEvent.setLocationY(event.getLocation().getLocationY());
            attackerSortieEvent.setTargetGrid(event.getLocation().getMapGridReference());
//			attackerSortieEvent.setDamage(event.getDamage());
//			attackerSortieEvent.setDamageValue(event.getDamageValue());
            MainController.SORTIES.get(pilot.getName()).setSortieEvents(event.getEventTime(), attackerSortieEvent);

            if (event.getEventType() == SortieEvent.EventType.DESTROYED) {
                MissionController.setMissionObjectLost(missionObject);
            }

            if (missionObject.getArmy() == MainController.SORTIES.get(pilot.getName()).getArmy()) {
                announceFriendlyFireMessage(pilot, victimName, event.getEventType());
                // Since they attacked a Friendly Target subtract twice the value of the target
                if (event.getDamageValue() > 0.0) {
//					MainController.SORTIES.get(pilot.getName()).setBonusPoints(MainController.SORTIES.get(pilot.getName()).getBonusPoints()
//							+ Math.round( (double)missionObject.getGroundObject().getBaseValue() * event.getDamageValue() * -2.0));
                    MainController.SORTIES.get(pilot.getName()).setBonusPoints(MainController.SORTIES.get(pilot.getName()).getBonusPoints());
                }

            } else {

                if (event.getDamageValue() > 0.0) {
//	    			MainController.writeDebugLogFile(2, "EventLogController.processPilotAttackedObjectEvent Damage Value(" + event.getDamageValue() + ") base Value("+missionObject.getGroundObject().getBaseValue()+
//	    					 ") Points Awarded("+Math.round( (double)missionObject.getGroundObject().getBaseValue() * event.getDamageValue())+")");
//					MainController.SORTIES.get(pilot.getName()).setBonusPoints(MainController.SORTIES.get(pilot.getName()).getBonusPoints()
//							+ Math.round( (double)missionObject.getGroundObject().getBaseValue() * event.getDamageValue() ) );
                    MainController.writeDebugLogFile(2, "EventLogController.processPilotAttackedObjectEvent Damage Value(" + event.getDamageValue() + ")");
                    MainController.SORTIES.get(pilot.getName()).setBonusPoints(MainController.SORTIES.get(pilot.getName()).getBonusPoints());
                }

                // Check to see if action was near a spawn base
                baseAttack(pilot, missionObject.getGroundObject().getObjectName(), missionObject.getArmy(), event.getEventType(), event.getLocation().getLocationX(), event.getLocation().getLocationY());

            }

            // If pilot Destroyed the object then increase their Ground object counter
            if (missionObject.getArmy() == pilot.getArmy() && event.getEventType() == SortieEvent.EventType.DESTROYED) {
                pilot.setFGroundUnit(pilot.getFGroundUnit() + 1);
                MainController.SORTIES.get(pilot.getName()).setBonusPoints(MainController.SORTIES.get(pilot.getName()).getBonusPoints() + missionObject.getGroundObject().getPointValue() * -2);

            } else if (event.getEventType() == SortieEvent.EventType.DESTROYED) {
                pilot.setEGroundUnit(pilot.getEGroundUnit() + 1);
                // Add any bonus points for the Target
                MainController.SORTIES.get(pilot.getName()).setBonusPoints(MainController.SORTIES.get(pilot.getName()).getBonusPoints() + missionObject.getGroundObject().getPointValue());
            }

            if (pilot.getFGroundUnit() >= MainController.CONFIG.getFriendlyGroundBan()) {
                MainController.writeDebugLogFile(1, "Banning Pilot(" + pilot.getName() + ") for Friendly Ground Kills( " + pilot.getFGroundUnit() + " )");
                SortieEvent banSortieEvent = new SortieEvent(event.getEventTime(), SortieEvent.EventType.PILOTBANNEDFGROUND, pilot.getPilotId());
                banSortieEvent.setLocationX(MainController.CONFIG.getFKillBanDuration());
                MainController.SORTIES.get(pilot.getName()).setSortieEvents((event.getEventTime() + 1), banSortieEvent);
                PilotBanController.pilotBanAdd(pilot.getName(), pilot.getIPAddress(), "Pilot Reached Friendly Ground Kill ban limit", Math.round(MainController.CONFIG.getFKillBanDuration() * 3600000.0));
            }
        }
        try {
            if (attackerSortieEvent != null) {
                MainController.SORTIES.get(pilot.getName()).setSortieEvents(event.getEventTime(), attackerSortieEvent);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.processPilotAttackedObjectEvent writing Sortie Events: " + ex);
        }

    }

//	private static void processAIWingAttackedObjectEvent(SortieEvent.EventType eventType, long eventTime, MissionObject missionObject, String wingName, Location eventLocation)
//	{
//		MissionObjectEvent missionObjectEvent = null;
//		try
//		{
//			MainController.writeDebugLogFile(2, eventTime + " - Attacker (" + wingName + ") "+eventType+" (" + missionObject.getMissionObjectName()+"/"+missionObject.getGroundObject().getObjectName() + ")");
//			AIWing aiWing =  EventLogParser.isAIWing(wingName);       
//			missionObjectEvent = new MissionObjectEvent(eventTime, eventType, 0);
//			missionObjectEvent.setMissionObjectName(aiWing.getName());
//			missionObjectEvent.setArmy(aiWing.getArmy());
//			missionObjectEvent.setIL2ObjectId(0);
//			missionObjectEvent.setIL2ObjectName(wingName);
//			missionObjectEvent.setIL2ObjectType(IL2StaticObject.ObjectType.AIPLANE);
//			missionObjectEvent.setOpponentId(missionObject.getGroundObject().getIl2ObjectID());
//			missionObjectEvent.setOpponentName(missionObject.getGroundObject().getObjectName());
//			missionObjectEvent.setOpponentArmy(missionObject.getArmy());
//			missionObjectEvent.setOpponentObjectType(missionObject.getGroundObject().getObjectType());
//			missionObjectEvent.setLocationX(eventLocation.getLocationX());
//			missionObjectEvent.setLocationY(eventLocation.getLocationY());
//			missionObjectEvent.setTargetGrid(missionObject.getTargetGrid());
//
//			if (missionObjectEvent != null)
//			{
//				MySQLConnectionController.writeMissionObjectEvent(missionObjectEvent);
//			}
//		}
//		catch (Exception ex)
//		{
//			MainController.writeDebugLogFile(1, "EventLogController.processAIWingAttackedObjectEvent writing Sortie Events: " + ex);
//		}
//	}

    private static void processObjectAttackedObjectEvent(SortieEvent.EventType eventType, long eventTime, MissionObject victimObject, MissionObject attackerObject, Location eventLocation) {

        MissionObjectEvent missionObjectEvent = null;
        String victimName;
        try {
            if (victimObject.getObjectType() == IL2StaticObject.ObjectType.AIPLANE) {
                victimName = victimObject.getMissionObjectName() + "/" + victimObject.getGroundObject().getDisplayName();
            } else {
                victimName = victimObject.getGroundObject().getDisplayName();
            }
            MainController.writeDebugLogFile(2, eventTime + " - Attacker (" + attackerObject.getMissionObjectName() + "/" + attackerObject.getGroundObject().getObjectName() + ") " + eventType + " (" + victimObject.getMissionObjectName() + "/"
                    + victimObject.getGroundObject().getObjectName() + ")");
            missionObjectEvent = new MissionObjectEvent(eventTime, eventType, attackerObject.getGroundObject().getIl2ObjectID());
            missionObjectEvent.setMissionObjectName(attackerObject.getMissionObjectName() + "/" + attackerObject.getGroundObject().getDisplayName());
            missionObjectEvent.setArmy(attackerObject.getArmy());
            missionObjectEvent.setIL2ObjectId(attackerObject.getGroundObject().getIl2ObjectID());
            missionObjectEvent.setIL2ObjectName(attackerObject.getGroundObject().getObjectName());
            missionObjectEvent.setIL2ObjectType(attackerObject.getObjectType());
            missionObjectEvent.setOpponentId(victimObject.getGroundObject().getIl2ObjectID());
            missionObjectEvent.setOpponentName(victimName);
            missionObjectEvent.setOpponentArmy(victimObject.getArmy());
            missionObjectEvent.setOpponentObjectType(victimObject.getObjectType());
            missionObjectEvent.setLocationX(eventLocation.getLocationX());
            missionObjectEvent.setLocationY(eventLocation.getLocationY());
            missionObjectEvent.setTargetGrid(victimObject.getTargetGrid());
            if (eventType == SortieEvent.EventType.DESTROYED) {
                MissionController.setMissionObjectLost(victimObject);
            }
            if (missionObjectEvent != null) {
                MySQLConnectionController.writeMissionObjectEvent(missionObjectEvent);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.processObjectAttackedObjectEvent writing Sortie Events: " + ex);
        }

    }

    public static void processEvent(Event event) {
        // Find out what the victim was (pilot, mission object, etc)
        MissionObject missionObject = MainController.ACTIVEMISSION.getMissionObjects().get(event.getVictimName());
        if (missionObject == null) {
            Pilot victimPilot = MainController.PILOTS.get(event.getVictimName());
            if (victimPilot == null) {
                if (event.getVictimName().contains("Bridge")) {
                    MissionObject tempObject = new MissionObject();
                    tempObject.setArmy(0);
                    tempObject.setLocationX(event.getLocation().getLocationX());
                    tempObject.setLocationY(event.getLocation().getLocationY());
                    tempObject.setMissionObjectName(event.getVictimName());
                    tempObject.setTargetGrid(event.getLocation().getMapGridReference());

                    IL2StaticObject bridgeObject = new IL2StaticObject("Bridge", IL2StaticObject.ObjectType.BRIDGE, "Bridge");
                    bridgeObject.setIl2ObjectID(0);
                    bridgeObject.setPointValue(20);
                    tempObject.setGroundObject(bridgeObject);
                    MainController.writeDebugLogFile(2, "EventLogController.processEvent - Event victim was a Bridge");
                    event.setVictimObject(tempObject);
                } else {
                    MainController.writeDebugLogFile(2, "EventLogController.processEvent - Error Mission Object (" + event.getVictimName() + ") was not found in mission");
                }
            } else {
                event.setVictimObject(victimPilot);
            }
        } else {
            event.setVictimObject(missionObject);
        }

        Pilot attackPilot = MainController.PILOTS.get(event.getAttackerName());
        if (attackPilot != null) {
            event.setAttackerObject(attackPilot);
            processPilotAttackedObjectEvent(event);
        } else {
            // Attacking Object was a Static object placed on map so Get it's name and Type
            MissionObject attackingMissionObject = MainController.ACTIVEMISSION.getMissionObjects().get(event.getAttackerName());
            if (attackingMissionObject != null) {
                event.setAttackerObject(attackingMissionObject);
//				processObjectAttackedObjectEvent(eventType, eventTime, missionObject, attackingMissionObject, eventLocation);
            } else {
                MainController.writeDebugLogFile(1, "EventLogController.processEvent - Error: Attacker Not found in Pilot list or Mission Objects: " + event.getAttackerName());
            }
        }

    }

    public static void processAttackingEvent(Event event) {
        // Is Attacker a Pilot ?
        if (event.getAttackerObject() instanceof Pilot) {

        } else if (event.getAttackerObject() instanceof MissionObject) {

        } else {
            MainController.writeDebugLogFile(1, "EventLogController.processAttackingEvent - Error: Attacker Not a Pilot or Mission Object: " + event.getAttackerName());
        }

    }

    public static void processObjectEvent(SortieEvent.EventType eventType, long eventTime, String victimName, String eventAction) {
        String attackerName;
        int attackInfoEndIndex;
        MissionObject missionObject;
        Location eventLocation;
        Pilot attackPilot;

        try {
            attackInfoEndIndex = eventAction.lastIndexOf(" at ");
            attackerName = eventAction.substring(0, attackInfoEndIndex).trim();
            attackerName = (String) EventLogParser.parsePilotPlanePosition(attackerName, false)[0];
            attackerName = EventLogParser.checkPilotName(attackerName, false);

            eventLocation = getLocation(eventAction.substring(attackInfoEndIndex + 4));

            // Was the Damaged Object a Mission Object ?
            missionObject = MainController.ACTIVEMISSION.getMissionObjects().get(victimName);
            if (missionObject == null) {
                if (victimName.contains("Bridge")) {
                    // Object was a bridge so lets record that option
                    MissionObject tempObject = new MissionObject();
                    tempObject.setArmy(0);
                    tempObject.setLocationX(eventLocation.getLocationX());
                    tempObject.setLocationY(eventLocation.getLocationY());
                    tempObject.setMissionObjectName(victimName);
                    String mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), eventLocation.getLocationX(), eventLocation.getLocationY());
                    tempObject.setTargetGrid(mapGrid);

                    IL2StaticObject bridgeObject = new IL2StaticObject("Bridge", IL2StaticObject.ObjectType.BRIDGE, "Bridge");
                    bridgeObject.setIl2ObjectID(0);
                    bridgeObject.setPointValue(0);
                    tempObject.setGroundObject(bridgeObject);
                    missionObject = tempObject;
                    MainController.writeDebugLogFile(2, "EventLogController.processObjectEvent - Victim was Bridge added to Mission Objects");
                }
                MainController.writeDebugLogFile(2, "EventLogController.processObjectEvent - Error Mission Object (" + victimName + ") was not found in mission");
            }
            if (missionObject != null) {
                attackPilot = MainController.PILOTS.get(attackerName);
                if (attackPilot != null) {
                    processPilotAttackedObjectEvent(eventType, eventTime, missionObject, attackPilot, eventLocation);
                } else {
                    // Attacking Object was a Static object placed on map so Get it's name and Type
                    MissionObject attackingMissionObject = MainController.ACTIVEMISSION.getMissionObjects().get(attackerName);
                    if (attackingMissionObject != null) {
                        processObjectAttackedObjectEvent(eventType, eventTime, missionObject, attackingMissionObject, eventLocation);
                    } else {
                        MainController.writeDebugLogFile(1, "EventLogController.processObjectEvent - Error: Attacker Not found in Pilot list or Mission Objects: " + attackerName);
                    }
                }
                return;
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.processObjectByEvent - Error Unhandled Exception: " + ex);
        }
    }

    public static void processVictimEvent(SortieEvent.EventType eventType, long eventTime, String victimName, String victimObject, String eventAction) {
        int attackInfoEndIndex;
//		int victimArmy = 0;
//		int victimObjectId;
        String attackerName = null;
        String attackerPlaneName = null;
        int attackerArmy = 0;
//		String attackerObject = null;
//		SortieEvent victimSortieEvent = null;
        SortieEvent attackerSortieEvent = null;
        MissionObjectEvent missionObjectEvent = null;
        Location eventLocation;

        MissionObject missionObject = null;
        Pilot attackPilot = null;
        Pilot victimPilot = null;

        String aObjectName;
        String vObjectName;

        attackInfoEndIndex = eventAction.lastIndexOf(" at ");

        eventLocation = getLocation(eventAction.substring(attackInfoEndIndex + 4));

        switch (eventType) {

            default:
                try {

                    Object[] pilotPlanePosition = EventLogParser.parsePilotPlanePosition(eventAction.substring(0, attackInfoEndIndex), false);
                    attackerName = (String) pilotPlanePosition[0];
                    attackerName = EventLogParser.checkPilotName(attackerName, false);
                    attackerPlaneName = (String) pilotPlanePosition[1];
                    if (pilotPlanePosition[4]!=null) attackerArmy = (Integer)pilotPlanePosition[4];

                    SortieEvent.EventType attackerEvent = SortieEvent.EventType.PLANEDAMAGED;
                    SortieEvent.EventType victimEvent = SortieEvent.EventType.PLANEDAMAGEDBY;
                    String mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), eventLocation.getLocationX(), eventLocation.getLocationY());

                    // Get the Attacking Pilot Record
                    attackPilot = MainController.PILOTS.get(attackerName);
                    // Get the Victim Pilot Record
                    victimPilot = MainController.PILOTS.get(victimName);

                    if (eventType == SortieEvent.EventType.SHOTDOWN) {
                        attackerEvent = SortieEvent.EventType.SHOTDOWN;
                        victimEvent = SortieEvent.EventType.SHOTDOWNBY;
                    } else if (eventType == SortieEvent.EventType.PILOTKILLEDBY) {
                        attackerEvent = SortieEvent.EventType.KILLED;
                        victimEvent = SortieEvent.EventType.PILOTKILLEDBY;
                    } else if (eventType == SortieEvent.EventType.PLANEDAMAGEDBY) {
                        attackerEvent = SortieEvent.EventType.PLANEDAMAGED;
                        victimEvent = SortieEvent.EventType.PLANEDAMAGEDBY;
                    } else if (eventType == SortieEvent.EventType.PILOTCHUTEKILLED) {
                        attackerEvent = SortieEvent.EventType.KILLEDPILOTINCHUTE;
                        victimEvent = SortieEvent.EventType.PILOTCHUTEKILLED;
                    }

                    if (attackPilot != null) {

                        EventLogController.pilotAttackerEvent(attackPilot, victimName, victimObject, eventType, eventTime, eventLocation.getLocationX(), eventLocation.getLocationY());

                        // Check to see if Attacker has reached Friendly Air kill ban limit
                        if (attackPilot.getFAir() >= MainController.CONFIG.getFriendlyAirBan()) {
                            MainController.writeDebugLogFile(1, "Banning Pilot(" + attackerName + ") for Friendly Air Kills( " + attackPilot.getFAir() + " )");
                            attackerSortieEvent = new SortieEvent(eventTime, SortieEvent.EventType.PILOTBANNEDFAIR, MainController.PILOTS.get(attackerName).getPilotId());
                            attackerSortieEvent.setLocationX(MainController.CONFIG.getFKillBanDuration());
                            MainController.SORTIES.get(attackerName).setSortieEvents((eventTime + 1), attackerSortieEvent);
                            attackerSortieEvent = null;
                            // Config setting for Ban duration is in Hours so multiply to get milliseconds
                            PilotBanController.pilotBanAdd(attackerName, attackPilot.getIPAddress(), "Pilot Reached Friendly Air Kill ban limit", Math.round(MainController.CONFIG.getFKillBanDuration() * 3600000.0));
                        }
                    } else if (MainController.ACTIVEMISSION.getMissionObjects().get(attackerName) != null) {
                        // Attacking Object was a Mission object
                        missionObject = MainController.ACTIVEMISSION.getMissionObjects().get(attackerName);

                        if (missionObject.getObjectType() == IL2StaticObject.ObjectType.AIPLANE) {
                            aObjectName = missionObject.getMissionObjectName() + "/" + missionObject.getGroundObject().getDisplayName();
                        } else {
                            aObjectName = missionObject.getGroundObject().getDisplayName();
                        }

                        if (victimPilot != null) {
                            EventLogController.pilotVictimEvent(victimPilot, attackerName, attackerPlaneName, victimPilot.getArmy() == attackerArmy, eventType, eventTime, eventLocation.getLocationX(), eventLocation.getLocationY());
                        } else if (MainController.ACTIVEMISSION.getMissionObjects().get(victimName) != null) {
                            MissionObject vMissionObject = MainController.ACTIVEMISSION.getMissionObjects().get(victimName);

                            if (vMissionObject.getObjectType() == IL2StaticObject.ObjectType.AIPLANE) {
                                vObjectName = vMissionObject.getMissionObjectName() + "/" + vMissionObject.getGroundObject().getDisplayName();
                            } else {
                                vObjectName = vMissionObject.getGroundObject().getDisplayName();
                            }

                            if (eventType == SortieEvent.EventType.SHOTDOWN) {
                                MissionController.setMissionObjectLost(vMissionObject);
                            }
                            // Attacker was an AI Plane, create Stats Event
                            missionObjectEvent = new MissionObjectEvent(eventTime, attackerEvent, 0);
                            missionObjectEvent.setArmy(missionObject.getArmy());
                            missionObjectEvent.setMissionObjectName(aObjectName);
                            missionObjectEvent.setIL2ObjectName(missionObject.getGroundObject().getDisplayName());
                            missionObjectEvent.setIL2ObjectType(missionObject.getObjectType());
                            missionObjectEvent.setOpponentId(0);
                            missionObjectEvent.setOpponentName(vObjectName);
                            missionObjectEvent.setOpponentArmy(vMissionObject.getArmy());
                            missionObjectEvent.setOpponentObjectType(vMissionObject.getObjectType());
                            missionObjectEvent.setOpponentSortieStartTime(0);
                            missionObjectEvent.setLocationX(eventLocation.getLocationX());
                            missionObjectEvent.setLocationY(eventLocation.getLocationY());
                            missionObjectEvent.setTargetGrid(mapGrid);
                            MySQLConnectionController.writeMissionObjectEvent(missionObjectEvent);

                            // AI Victim Event
                            missionObjectEvent = new MissionObjectEvent(eventTime, victimEvent, 0);
                            missionObjectEvent.setArmy(vMissionObject.getArmy());
                            missionObjectEvent.setMissionObjectName(vObjectName);
                            missionObjectEvent.setIL2ObjectName(vMissionObject.getGroundObject().getDisplayName());
                            missionObjectEvent.setIL2ObjectType(vMissionObject.getObjectType());
                            missionObjectEvent.setOpponentId(0);
                            missionObjectEvent.setOpponentName(aObjectName);
                            missionObjectEvent.setOpponentArmy(missionObject.getArmy());
                            missionObjectEvent.setOpponentObjectType(missionObject.getObjectType());
                            missionObjectEvent.setOpponentSortieStartTime(0);
                            missionObjectEvent.setLocationX(eventLocation.getLocationX());
                            missionObjectEvent.setLocationY(eventLocation.getLocationY());
                            missionObjectEvent.setTargetGrid(mapGrid);
                            MySQLConnectionController.writeMissionObjectEvent(missionObjectEvent);

                        } else {
                            // Unknown Victim
                            MainController.writeDebugLogFile(1, "EventLogController.processVictimEvent - Error Unknown Victim (" + victimName + ") for Attacker (" + attackerName + ")");
                        }
                    } else
                    // Attacker not found in Pilots, AI Planes, or Mission Objects this should not happen so log it.
                    {
                        MainController.writeDebugLogFile(1, "EventLogController.processVictimEvent - Error - Attacker Not found (" + attackerName + ") Event (" + eventType + ") Victim (" + victimName + ")");
                    }

                    MainController.writeDebugLogFile(2, eventTime + " Attacker(" + attackerName + ")/Victim(" + victimName + ") Event(" + eventType + ")");
                    missionObjectEvent = null;
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "EventLogController.processVictimEvent - Error Unhandled Exception : " + ex);
                    MainController.writeDebugLogFile(1, " *** EventType (" + eventType + ") Victim (" + victimName + ") Attacker (" + attackerName + ")");
                }
                break;

        }
        try {
            if (attackerSortieEvent != null) {
                MainController.SORTIES.get(attackerName).setSortieEvents(eventTime, attackerSortieEvent);
            }
//			if (victimSortieEvent != null)
//			{
//				MainController.SORTIES.get(victimName).setSortieEvents(eventTime, victimSortieEvent);
//     		}
            if (missionObjectEvent != null) {
                MySQLConnectionController.writeMissionObjectEvent(missionObjectEvent);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.processVictimEvent writing Sortie Events: " + ex);
        }

    } // End processVictimEvent

    private static void pilotVictimEvent(Pilot victimPilot, String attackerName, String attackerPlaneName, boolean friendly, SortieEvent.EventType eventType, long eventTime, double locationX, double locationY) {
        SortieEvent victimSortieEvent = null;
        MissionObjectEvent missionObjectEvent = null;
        SortieEvent.EventType attackerEvent = SortieEvent.EventType.PLANEDAMAGED;
        SortieEvent.EventType victimEvent = SortieEvent.EventType.PLANEDAMAGEDBY;
        String mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), locationX, locationY);

        String victimName = victimPilot.getName();

        if (eventType == SortieEvent.EventType.SHOTDOWN) {
            attackerEvent = SortieEvent.EventType.SHOTDOWN;
            victimEvent = SortieEvent.EventType.SHOTDOWNBY;
            // Victims Plane is not OK to Fly
            MainController.PILOTS.get(victimName).setPlaneIsOKToFly(false);
            // Victim Army lost a Plane
            MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, MainController.PILOTS.get(victimName).getArmy(), IL2StaticObject.ObjectType.PLANE, 1);
            if (MainController.SORTIES.containsKey(victimName)) {
                MainController.SORTIES.get(victimName).setPlaneStatus(PilotSortie.PlaneStatus.SHOTDOWN);
            }
        } else if (eventType == SortieEvent.EventType.PILOTKILLEDBY) {
            attackerEvent = SortieEvent.EventType.KILLED;
            victimEvent = SortieEvent.EventType.PILOTKILLEDBY;
            // Victim Army lost a Pilot
            MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, victimPilot.getArmy(), IL2StaticObject.ObjectType.PILOT, 1);
            victimPilot.setDeathCount(victimPilot.getDeathCount() + 1);
            if (MainController.SORTIES.containsKey(victimName)) {
                MainController.SORTIES.get(victimName).setPilotStatus(PilotSortie.PilotStatus.KIA);
            }
        } else if (eventType == SortieEvent.EventType.PLANEDAMAGEDBY) {
            attackerEvent = SortieEvent.EventType.PLANEDAMAGED;
            victimEvent = SortieEvent.EventType.PLANEDAMAGEDBY;
        } else if (eventType == SortieEvent.EventType.PILOTCHUTEKILLED) {
            attackerEvent = SortieEvent.EventType.KILLEDPILOTINCHUTE;
            victimEvent = SortieEvent.EventType.PILOTCHUTEKILLED;
            // Victim Army lost a Pilot
            MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, victimPilot.getArmy(), IL2StaticObject.ObjectType.PILOT, 1);
            victimPilot.setDeathCount(victimPilot.getDeathCount() + 1);
        }

        announcePilotVictimMessages(attackerPlaneName, victimPilot, victimEvent, friendly, locationX, locationY);
        MissionObject missionObject = MainController.ACTIVEMISSION.getMissionObjects().get(attackerName);

        if (missionObject != null) {
            // Ground Object was the Attacker
            missionObjectEvent = new MissionObjectEvent(eventTime, attackerEvent, missionObject.getGroundObject().getIl2ObjectID());
            missionObjectEvent.setArmy(missionObject.getArmy());
            missionObjectEvent.setMissionObjectName(missionObject.getMissionObjectName() + "/" + missionObject.getGroundObject().getDisplayName());
            missionObjectEvent.setIL2ObjectId(missionObject.getGroundObject().getIl2ObjectID());
            missionObjectEvent.setIL2ObjectName(missionObject.getGroundObject().getDisplayName());
            missionObjectEvent.setIL2ObjectType(missionObject.getObjectType());
            missionObjectEvent.setOpponentId(victimPilot.getPilotId());
            missionObjectEvent.setOpponentName(victimName);
            missionObjectEvent.setOpponentArmy(victimPilot.getArmy());
            missionObjectEvent.setOpponentObjectType(IL2StaticObject.ObjectType.PILOT);
            missionObjectEvent.setOpponentSortieStartTime(MainController.SORTIES.get(victimName).getSortieStartTime());
            missionObjectEvent.setLocationX(locationX);
            missionObjectEvent.setLocationY(locationY);
            missionObjectEvent.setTargetGrid(mapGrid);
            MySQLConnectionController.writeMissionObjectEvent(missionObjectEvent);

            // Victim Event
            victimSortieEvent = new SortieEvent(eventTime, victimEvent, MainController.PILOTS.get(victimName).getPilotId());
            victimSortieEvent.setOpponentId(missionObject.getGroundObject().getIl2ObjectID());
            victimSortieEvent.setOpponentArmy(missionObject.getArmy());
            victimSortieEvent.setOpponentObjectType(missionObject.getObjectType());
            victimSortieEvent.setOpponentName(missionObject.getMissionObjectName() + "/" + missionObject.getGroundObject().getDisplayName());
            victimSortieEvent.setOpponentSortieStartTime(0);
            victimSortieEvent.setLocationX(locationX);
            victimSortieEvent.setLocationY(locationY);
            MainController.SORTIES.get(victimName).setSortieEvents(eventTime, victimSortieEvent);
            victimSortieEvent = null;

        } else {
            // Unknown Attacker
            MainController.writeDebugLogFile(1, "EventLogController.pilotAttackerEvent - Error Unknown Attacker (" + attackerName + ") for Victim (" + victimName + ")");
        }

        PilotPanelController.updatePilot(victimName);
        PilotController.deathKick(MainController.PILOTS.get(victimName));

    }

    private static void pilotAttackerEvent(Pilot attackPilot, String victimName, String victimObject, SortieEvent.EventType eventType, long eventTime, double locationX, double locationY) {
        SortieEvent attackerSortieEvent = null;
        SortieEvent victimSortieEvent = null;
        MissionObjectEvent missionObjectEvent = null;
        SortieEvent.EventType attackerEvent = SortieEvent.EventType.PLANEDAMAGED;
        SortieEvent.EventType victimEvent = SortieEvent.EventType.PLANEDAMAGEDBY;

        try {
            String attackerName = attackPilot.getName();
            String mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), locationX, locationY);

            if (eventType == SortieEvent.EventType.SHOTDOWN) {
                attackerEvent = SortieEvent.EventType.SHOTDOWN;
                victimEvent = SortieEvent.EventType.SHOTDOWNBY;
            } else if (eventType == SortieEvent.EventType.PILOTKILLEDBY) {
                attackerEvent = SortieEvent.EventType.KILLED;
                victimEvent = SortieEvent.EventType.PILOTKILLEDBY;
            } else if (eventType == SortieEvent.EventType.PLANEDAMAGEDBY) {
                attackerEvent = SortieEvent.EventType.PLANEDAMAGED;
                victimEvent = SortieEvent.EventType.PLANEDAMAGEDBY;
            } else if (eventType == SortieEvent.EventType.PILOTCHUTEKILLED) {
                attackerEvent = SortieEvent.EventType.KILLEDPILOTINCHUTE;
                victimEvent = SortieEvent.EventType.PILOTCHUTEKILLED;
            }

            Pilot victimPilot = MainController.PILOTS.get(victimName);

            if (victimPilot != null) {
                if (MainController.PILOTS.get(victimName).getArmy() == attackPilot.getArmy()) {
                    countFriendlyEvent(attackPilot, victimPilot, eventType, eventTime, locationX, locationY);
                } else {
                    MainController.PILOTS.get(victimName).setPlaneIsOKToFly(false);
                    if (eventType == SortieEvent.EventType.SHOTDOWN) {
                        // Increase the Plane lost counter
                        MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, MainController.PILOTS.get(victimName).getArmy(), IL2StaticObject.ObjectType.PLANE, 1);
                        attackPilot.setEAirConfirmed(attackPilot.getEAirConfirmed() + 1);
                        MainController.SORTIES.get(victimName).setPlaneStatus(PilotSortie.PlaneStatus.SHOTDOWN);
                    } else if (eventType == SortieEvent.EventType.PILOTKILLEDBY) {
                        // Increate the Pilot lost counter
                        MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, victimPilot.getArmy(), IL2StaticObject.ObjectType.PILOT, 1);
                        victimPilot.setDeathCount(victimPilot.getDeathCount() + 1);
                        MainController.SORTIES.get(victimName).setPilotStatus(PilotSortie.PilotStatus.KIA);
                        // Added this to correct issue of player getting PK's then refly and plane is not tallied as 'lost'
//						MainController.SORTIES.get(victimName).setPlaneStatus(PilotSortie.PlaneStatus.SHOTDOWN);
                    } else if (eventType == SortieEvent.EventType.PILOTCHUTEKILLED) {
                        // Increate the Pilot lost counter
                        MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, victimPilot.getArmy(), IL2StaticObject.ObjectType.PILOT, 1);
                        victimPilot.setDeathCount(victimPilot.getDeathCount() + 1);
                        if (attackerName.intern() == victimName.intern()) { // -- Pilot was killed by their own plane bailing out (Happens a good bit)
                            victimEvent = SortieEvent.EventType.KILLEDBAILINGOUT;
                            MainController.SORTIES.get(victimName).setPilotStatus(PilotSortie.PilotStatus.KIABAILINGOUT);
                        } else {
                            MainController.SORTIES.get(victimName).setPilotStatus(PilotSortie.PilotStatus.KIACHUTEKILLED);
                        }
                    }
                    // Set Sortie Event Information
                    attackerSortieEvent = new SortieEvent(eventTime, attackerEvent, attackPilot.getPilotId());
                    attackerSortieEvent.setOpponentId(MainController.PILOTS.get(victimName).getPilotId());
                    attackerSortieEvent.setOpponentName(victimName);
                    attackerSortieEvent.setOpponentArmy(MainController.PILOTS.get(victimName).getArmy());
                    attackerSortieEvent.setOpponentObjectType(IL2StaticObject.ObjectType.PILOT);
                    attackerSortieEvent.setOpponentSortieStartTime(MainController.SORTIES.get(victimName).getSortieStartTime());
                    attackerSortieEvent.setLocationX(locationX);
                    attackerSortieEvent.setLocationY(locationY);
                    attackerSortieEvent.setTargetGrid(mapGrid);
                    MainController.SORTIES.get(attackPilot.getName()).setSortieEvents(eventTime, attackerSortieEvent);
                    attackerSortieEvent = null;

                    victimSortieEvent = new SortieEvent(eventTime, victimEvent, MainController.PILOTS.get(victimName).getPilotId());
                    victimSortieEvent.setOpponentId(MainController.PILOTS.get(attackerName).getPilotId());
                    victimSortieEvent.setOpponentArmy(MainController.PILOTS.get(attackerName).getArmy());
                    victimSortieEvent.setOpponentObjectType(IL2StaticObject.ObjectType.PILOT);
                    victimSortieEvent.setOpponentName(attackerName);
                    victimSortieEvent.setOpponentSortieStartTime(MainController.SORTIES.get(attackerName).getSortieStartTime());
                    victimSortieEvent.setLocationX(locationX);
                    victimSortieEvent.setLocationY(locationY);
                    victimSortieEvent.setTargetGrid(mapGrid);
                    MainController.SORTIES.get(victimName).setSortieEvents(eventTime, victimSortieEvent);
                    victimSortieEvent = null;
                }
                announcePilotMessages(attackPilot, victimPilot, attackerEvent, MainController.PILOTS.get(victimName).getArmy() == attackPilot.getArmy(), locationX, locationY);
                PilotPanelController.updatePilot(victimName);
                PilotController.deathKick(MainController.PILOTS.get(victimName));

            } else if (EventLogParser.isMissionObject(victimName)) {
                // Victim was an AIPlane
                MissionObject missionObject = MainController.ACTIVEMISSION.getMissionObjects().get(victimName);
                if (eventType == SortieEvent.EventType.SHOTDOWN) {
                    MissionController.setMissionObjectLost(missionObject);
                    if (attackPilot.getArmy() != missionObject.getArmy()) {
                        attackPilot.setEAirConfirmed(attackPilot.getEAirConfirmed() + 1);
                    } else {
                        attackPilot.setFAir(attackPilot.getFAir() + 1);
                    }
                    announcePilotAttackerMessages(attackPilot, victimObject, attackerEvent, attackPilot.getArmy() == missionObject.getArmy(), locationX, locationY);
                }
                if (attackPilot.getArmy() == missionObject.getArmy()) {
                    EventLogController.announceFriendlyFireMessage(attackPilot, missionObject.getGroundObject().getDisplayName(), eventType);
                }
                // Set Sortie Event Information
                attackerSortieEvent = new SortieEvent(eventTime, attackerEvent, MainController.PILOTS.get(attackerName).getPilotId());
                attackerSortieEvent.setOpponentName(victimName + "/" + missionObject.getGroundObject().getDisplayName());
                attackerSortieEvent.setOpponentId(missionObject.getGroundObject().getIl2ObjectID());
                attackerSortieEvent.setOpponentArmy(missionObject.getArmy());
                attackerSortieEvent.setOpponentSortieStartTime(0);
                attackerSortieEvent.setOpponentObjectType(missionObject.getObjectType());
                attackerSortieEvent.setLocationX(locationX);
                attackerSortieEvent.setLocationY(locationY);
                attackerSortieEvent.setTargetGrid(mapGrid);
                MainController.SORTIES.get(attackerName).setSortieEvents(eventTime, attackerSortieEvent);
                attackerSortieEvent = null;

                // Victim AI Event
                missionObjectEvent = new MissionObjectEvent(eventTime, victimEvent, 0);
                missionObjectEvent.setArmy(missionObject.getArmy());
                missionObjectEvent.setMissionObjectName(victimName + "/" + missionObject.getGroundObject().getDisplayName());
                missionObjectEvent.setIL2ObjectId(missionObject.getGroundObject().getIl2ObjectID());
                missionObjectEvent.setIL2ObjectName(missionObject.getGroundObject().getDisplayName());
                missionObjectEvent.setIL2ObjectType(missionObject.getObjectType());
                missionObjectEvent.setOpponentId(MainController.PILOTS.get(attackerName).getPilotId());
                missionObjectEvent.setOpponentName(attackPilot.getName());
                missionObjectEvent.setOpponentArmy(MainController.SORTIES.get(attackerName).getArmy());
                missionObjectEvent.setOpponentObjectType(IL2StaticObject.ObjectType.PILOT);
                missionObjectEvent.setTargetGrid(mapGrid);
                missionObjectEvent.setOpponentSortieStartTime(MainController.SORTIES.get(attackerName).getSortieStartTime());
                MySQLConnectionController.writeMissionObjectEvent(missionObjectEvent);

            } else {
                // Unknown Victim
                MainController.writeDebugLogFile(1, "EventLogController.pilotAttackerEvent - Error Unknown Victim (" + victimName + ") for Attacker (" + attackerName + ")");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.pilotAttackerEvent - Error Unhandled Exception VictimName(" + victimName + "): " + ex);
        }
    }

    public static void countFriendlyEvent(Pilot attackPilot, Pilot victimPilot, SortieEvent.EventType eventType, long eventTime, double locationX, double locationY) {
        SortieEvent victimSortieEvent = null;
        SortieEvent attackerSortieEvent = null;
        SortieEvent.EventType attackerEvent = SortieEvent.EventType.PLANEDAMAGED;
        SortieEvent.EventType victimEvent = SortieEvent.EventType.PLANEDAMAGEDBY;
        String mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), locationX, locationY);

        // Check to See if we are counting Friendly Kills on the Ground
        if (!MainController.CONFIG.getCountFAirKillonGround() && (victimPilot.getState() == PilotBlueprint.ValidStates.SORTIEBEGIN || victimPilot.getState() == PilotBlueprint.ValidStates.REFLY)
                && (attackPilot.getState() == PilotBlueprint.ValidStates.SORTIEBEGIN || attackPilot.getState() == PilotBlueprint.ValidStates.REFLY)) {
            MainController.writeDebugLogFile(2, "EventLogController.processVictimEvent - " + eventType + " on Ground does not count");
            // If we're in here, then both pilots were on the ground and the configuration is set to not count Air kills that occur on the ground
            // This most likely occurs on Carrier Ops when someone spawns in front of another.
        } else {
            MainController.writeDebugLogFile(2, "EventLogController.processVictimEvent - " + eventType + " on Ground Counts");

            // Increase the Plane lost counter
            if (eventType == SortieEvent.EventType.PILOTKILLEDBY) {
                attackPilot.setFAir(attackPilot.getFAir() + 1);
                MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, victimPilot.getArmy(), IL2StaticObject.ObjectType.PILOT, 1);
                attackerEvent = SortieEvent.EventType.KILLED;
                victimEvent = SortieEvent.EventType.PILOTKILLEDBY;
                victimPilot.setDeathCount(victimPilot.getDeathCount() + 1);
                PilotController.deathKick(victimPilot);
            } else if (eventType == SortieEvent.EventType.SHOTDOWN) {
                attackPilot.setFAir(attackPilot.getFAir() + 1);
                MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, victimPilot.getArmy(), IL2StaticObject.ObjectType.PLANE, 1);
                // Set Victims Plane is OK to Fly to False
                victimPilot.setPlaneIsOKToFly(false);
                attackerEvent = SortieEvent.EventType.SHOTDOWN;
                victimEvent = SortieEvent.EventType.SHOTDOWNBY;
            } else if (eventType == SortieEvent.EventType.PLANEDAMAGEDBY) {
                attackerEvent = SortieEvent.EventType.PLANEDAMAGED;
                victimEvent = SortieEvent.EventType.PLANEDAMAGEDBY;
            } else if (eventType == SortieEvent.EventType.PILOTCHUTEKILLED) {
                attackerEvent = SortieEvent.EventType.KILLEDPILOTINCHUTE;
                victimEvent = SortieEvent.EventType.PILOTCHUTEKILLED;

                MissionCountObjectivesController.adjustMissionLostCount(MainController.ACTIVEMISSION, victimPilot.getArmy(), IL2StaticObject.ObjectType.PILOT, 1);
                victimPilot.setDeathCount(victimPilot.getDeathCount() + 1);
                if (attackPilot.getName().intern() == victimPilot.getName().intern()) { // -- Pilot was killed by their own plane bailing out (Happens a good bit)
                    victimEvent = SortieEvent.EventType.KILLEDBAILINGOUT;
                    MainController.SORTIES.get(victimPilot.getName()).setPilotStatus(PilotSortie.PilotStatus.KIABAILINGOUT);
                } else {
                    MainController.SORTIES.get(victimPilot.getName()).setPilotStatus(PilotSortie.PilotStatus.KIACHUTEKILLED);
                }
            }

            EventLogController.announceFriendlyFireMessage(attackPilot, victimPilot.getName(), eventType);

            // Set Sortie Event Information
            attackerSortieEvent = new SortieEvent(eventTime, attackerEvent, attackPilot.getPilotId());
            attackerSortieEvent.setOpponentId(victimPilot.getPilotId());
            attackerSortieEvent.setOpponentName(victimPilot.getName());
            attackerSortieEvent.setOpponentArmy(victimPilot.getArmy());
            attackerSortieEvent.setOpponentObjectType(IL2StaticObject.ObjectType.PILOT);
            attackerSortieEvent.setOpponentSortieStartTime(MainController.SORTIES.get(victimPilot.getName()).getSortieStartTime());
            attackerSortieEvent.setLocationX(locationX);
            attackerSortieEvent.setLocationY(locationY);
            attackerSortieEvent.setTargetGrid(mapGrid);
            MainController.SORTIES.get(attackPilot.getName()).setSortieEvents(eventTime, attackerSortieEvent);
            attackerSortieEvent = null;

            victimSortieEvent = new SortieEvent(eventTime, victimEvent, victimPilot.getPilotId());
            victimSortieEvent.setOpponentId(attackPilot.getPilotId());
            victimSortieEvent.setOpponentArmy(attackPilot.getArmy());
            victimSortieEvent.setOpponentObjectType(IL2StaticObject.ObjectType.PILOT);
            victimSortieEvent.setOpponentName(attackPilot.getName());
            victimSortieEvent.setOpponentSortieStartTime(MainController.SORTIES.get(attackPilot.getName()).getSortieStartTime());
            victimSortieEvent.setLocationX(locationX);
            victimSortieEvent.setLocationY(locationY);
            victimSortieEvent.setTargetGrid(mapGrid);
            MainController.SORTIES.get(victimPilot.getName()).setSortieEvents(eventTime, victimSortieEvent);
            victimSortieEvent = null;

        }

    }

    private static void announceFriendlyFireMessage(Pilot pilot, String victimName, SortieEvent.EventType eventType) {
        String chatMessage = "";
        if (eventType == SortieEvent.EventType.PILOTKILLEDBY) {
            chatMessage = "** ( " + pilot.getName() + " ) Killed Friendly Pilot ( " + victimName + " )";
        } else if (eventType == SortieEvent.EventType.SHOTDOWN) {
            chatMessage = "** ( " + pilot.getName() + " ) Shotdown Friendly Pilot ( " + victimName + " )";
        } else if (eventType == SortieEvent.EventType.PLANEDAMAGEDBY || eventType == SortieEvent.EventType.DAMAGED || eventType == SortieEvent.EventType.DESTROYED) {
            chatMessage = "** ( " + pilot.getName() + " ) CEASE FIRE ** you are attacking a Friendly";
        }
        // Send Friendly fire message over chat based on Configuration Setting
        if (MainController.CONFIG.getAnnounceFriendlyFire() != ConfigurationItem.ChatReciepients.NONE) {
            String receipient = "";
            if (MainController.CONFIG.getAnnounceFriendlyFire() == ConfigurationItem.ChatReciepients.PILOT) {
                receipient = pilot.getAsciiTextName();
            } else if (MainController.CONFIG.getAnnounceFriendlyFire() == ConfigurationItem.ChatReciepients.ARMY) {
                receipient = "ARMY " + Integer.toString(pilot.getArmy());
            } else if (MainController.CONFIG.getAnnounceFriendlyFire() == ConfigurationItem.ChatReciepients.ALL) {
                receipient = MainController.CONFIG.getAnnounceFriendlyFire().toString();
            }
            if (chatMessage.length() > 0 && receipient.length() > 0) {
                ServerCommandController.serverCommandSend("chat " + chatMessage + " TO \"" + receipient + "\"");
            }
        }
    }

    private static void announcePilotMessages(Pilot attackerPilot, Pilot victimPilot, SortieEvent.EventType eventType, Boolean friendly, double locationX, double locationY) {
        try {
            String chatMessage = "";
            String friendlyMsg = "";
            if (friendly) {
                friendlyMsg = "FRIENDLY ";
            }
            String mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), locationX, locationY);
            if (eventType == SortieEvent.EventType.KILLED) {
                chatMessage = "** ( " + attackerPilot.getName() + " ) Killed " + friendlyMsg + "Pilot ( " + victimPilot.getName() + " ) at [" + mapGrid + "]";
            } else if (eventType == SortieEvent.EventType.SHOTDOWN) {
                chatMessage = "** ( " + attackerPilot.getName() + " ) Shotdown " + friendlyMsg + "Pilot ( " + victimPilot.getName() + " ) at [" + mapGrid + "]";
            } else if (eventType == SortieEvent.EventType.PLANEDAMAGED) {
                if (friendly) {
                    chatMessage = "** ( " + attackerPilot.getName() + " ) CEASE FIRE ** you are attacking a Friendly";
                } else {
                    chatMessage = "** ( " + attackerPilot.getName() + " ) Damaged ( " + victimPilot.getName() + "'s ) Plane at [" + mapGrid + "]";
                }
            }
            // Send Friendly fire message over chat based on Configuration Setting
            if (friendly && MainController.CONFIG.getAnnounceFriendlyFire() != ConfigurationItem.ChatReciepients.NONE) {
                String receipient = "";
                String receipient2 = "";
                if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.PILOT) {
                    receipient = attackerPilot.getAsciiTextName();
                    receipient2 = victimPilot.getAsciiTextName();
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ARMY) {
                    receipient = "ARMY " + Integer.toString(attackerPilot.getArmy());
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ALL) {
                    receipient = MainController.CONFIG.getAnnounceFriendlyFire().toString();
                }
                if (chatMessage.length() > 0 && receipient.length() > 0) {
                    ServerCommandController.serverCommandSend("chat " + chatMessage + " TO \"" + receipient + "\"");
                }
                if (chatMessage.length() > 0 && receipient2.length() > 0) {
                    ServerCommandController.serverCommandSend("chat " + chatMessage + " TO \"" + receipient2 + "\"");
                }

            }
            if (!friendly) {
                String receipient = "";
                String receipient2 = "";
                if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.PILOT) {
                    receipient = attackerPilot.getAsciiTextName();
                    receipient2 = victimPilot.getAsciiTextName();
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ARMY) {
                    receipient = "ARMY " + Integer.toString(attackerPilot.getArmy());
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ALL) {
                    receipient = MainController.CONFIG.getAnnounceShotdownMsg().toString();
                }
                if (chatMessage.length() > 0 && receipient.length() > 0) {
                    ServerCommandController.serverCommandSend("chat " + chatMessage + " TO \"" + receipient + "\"");
                }
                if (chatMessage.length() > 0 && receipient2.length() > 0) {
                    ServerCommandController.serverCommandSend("chat " + chatMessage + " TO \"" + receipient2 + "\"");
                }
                // Check to see if action happened near Victim's Army Spawn base's
                baseAttack(attackerPilot, victimPilot.getName(), victimPilot.getArmy(), eventType, locationX, locationY);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.announcePilotMessages - Error Unhandled exception: " + ex);
        }
    }

    private static void announcePilotVictimMessages(String attackerPlaneName, Pilot victimPilot, SortieEvent.EventType eventType, Boolean friendly, double locationX, double locationY) {
        if (attackerPlaneName == null) {
            MainController.writeDebugLogFile(2, "EventLogController.announcePilotVictimMessages attackerPlaneName=null");
            return;
        }
        try {
            String chatMessage = "";
            String friendlyMsg = "";
            if (friendly) {
                friendlyMsg = "FRIENDLY ";
            }
            String mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), locationX, locationY);
            if (eventType == SortieEvent.EventType.KILLED) {
                chatMessage = "** AI ( " + attackerPlaneName + " ) Killed " + friendlyMsg + "Pilot ( " + victimPilot.getName() + " ) at [" + mapGrid + "]";
            } else if (eventType == SortieEvent.EventType.SHOTDOWN) {
                chatMessage = "** AI ( " + attackerPlaneName + " ) Shotdown " + friendlyMsg + "Pilot ( " + victimPilot.getName() + " ) at [" + mapGrid + "]";
            } else if (eventType == SortieEvent.EventType.PLANEDAMAGED) {
                chatMessage = "** AI ( " + attackerPlaneName + " ) Damaged ( " + victimPilot.getName() + "'s ) Plane at [" + mapGrid + "]";
            }
            // Send Friendly fire message over chat based on Configuration Setting
            if (friendly && MainController.CONFIG.getAnnounceFriendlyFire() != ConfigurationItem.ChatReciepients.NONE) {
                String receipient = "";
                if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.PILOT) {
                    receipient = victimPilot.getAsciiTextName();
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ARMY) {
                    receipient = "ARMY " + Integer.toString(victimPilot.getArmy());
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ALL) {
                    receipient = MainController.CONFIG.getAnnounceFriendlyFire().toString();
                }
                if (chatMessage.length() > 0 && receipient.length() > 0) {
                    ServerCommandController.serverCommandSend("chat " + chatMessage + " TO \"" + receipient + "\"");
                }

            }
            if (!friendly) {
                String receipient = "";
                if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.PILOT) {
                    receipient = victimPilot.getAsciiTextName();
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ARMY) {
                    receipient = "ARMY " + Integer.toString(victimPilot.getArmy());
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ALL) {
                    receipient = MainController.CONFIG.getAnnounceShotdownMsg().toString();
                }
                if (chatMessage.length() > 0 && receipient.length() > 0) {
                    ServerCommandController.serverCommandSend("chat " + chatMessage + " TO \"" + receipient + "\"");
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.announcePilotVictimMessages - Error Unhandled exception: " + ex);
        }
    }

    private static void announcePilotAttackerMessages(Pilot attackerPilot, String victimObject, SortieEvent.EventType eventType, Boolean friendly, double locationX, double locationY) {
        if (victimObject == null) {
            MainController.writeDebugLogFile(2, "EventLogController.announcePilotAttackerMessages victimObject=null");
            return;
        }
        try {
            String chatMessage = "";
            String friendlyMsg = "";
            if (friendly) {
                friendlyMsg = "FRIENDLY ";
            }
            String mapGrid = Coordinates.getCoordinates(MainController.ACTIVEMISSION.getMapName(), MainController.ACTIVEMISSION.isBigMap(), locationX, locationY);
            if (eventType == SortieEvent.EventType.KILLED) {
                chatMessage = "** ( " + attackerPilot.getName() + " ) Killed " + friendlyMsg + "AI Plane ( " + victimObject + " ) at [" + mapGrid + "]";
            } else if (eventType == SortieEvent.EventType.SHOTDOWN) {
                chatMessage = "** ( " + attackerPilot.getName() + " ) Shotdown " + friendlyMsg + "AI Plane ( " + victimObject + " ) at [" + mapGrid + "]";
            } else if (eventType == SortieEvent.EventType.PLANEDAMAGED) {
                chatMessage = "** ( " + attackerPilot.getName() + " ) Damaged AI Plane( " + victimObject + " ) at [" + mapGrid + "]";
            }
            // Send Friendly fire message over chat based on Configuration Setting
            if (friendly && MainController.CONFIG.getAnnounceFriendlyFire() != ConfigurationItem.ChatReciepients.NONE) {
                String receipient = "";
                if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.PILOT) {
                    receipient = attackerPilot.getAsciiTextName();
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ARMY) {
                    receipient = "ARMY " + Integer.toString(attackerPilot.getArmy());
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ALL) {
                    receipient = MainController.CONFIG.getAnnounceFriendlyFire().toString();
                }
                if (chatMessage.length() > 0 && receipient.length() > 0) {
                    ServerCommandController.serverCommandSend("chat " + chatMessage + " TO \"" + receipient + "\"");
                }

            }
            if (!friendly) {
                String receipient = "";
                if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.PILOT) {
                    receipient = attackerPilot.getAsciiTextName();
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ARMY) {
                    receipient = "ARMY " + Integer.toString(attackerPilot.getArmy());
                } else if (MainController.CONFIG.getAnnounceShotdownMsg() == ConfigurationItem.ChatReciepients.ALL) {
                    receipient = MainController.CONFIG.getAnnounceShotdownMsg().toString();
                }
                if (chatMessage.length() > 0 && receipient.length() > 0) {
                    ServerCommandController.serverCommandSend("chat " + chatMessage + " TO \"" + receipient + "\"");
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "EventLogController.announcePilotVictimMessages - Error Unhandled exception: " + ex);
        }
    }

    public static void baseAttack(Pilot attackPilot, String victimName, int victimArmy, SortieEvent.EventType eventType, double locationX, double locationY) {
        if (MainController.CONFIG.isLogBaseAttack()) {
            // Check to see if location is near friendlybase if it is then notify admins of potential base attacks
            String aerodromeMapGrid = "";
//			String chatMessage = "";
            Aerodrome aerodrome = AerodromeController.findAerodrome(MainController.ACTIVEMISSION, locationX, locationY);
            if (aerodrome != null && aerodrome.getArmy() == victimArmy) {
                aerodromeMapGrid = aerodrome.getAerodromeMapGrid();
                MainController.writeDebugLogFile(2, "EventLogController.baseAttack - Pilot (" + attackPilot.getName() + ") attacked (" + victimName + ") near base at (" + aerodromeMapGrid + ")");
                MySQLConnectionController.updateBaseAttackLog(attackPilot.getName(), victimName, eventType.toString(), aerodromeMapGrid, MainController.ACTIVEMISSION.getStartTime(), MainController.ACTIVEMISSION.getMissionName());

//				chatMessage = "** WARNING: Pilot ("+attackPilot.getName()+") Attacked Pilot ("+victimName.getName()+") near Base ("+aerodromeMapGrid+") **";
//				ArrayList<String> adminList = AdminController.getConnectedAdmins(); 
//				if (adminList != null)
//				{
//					for (String admin : adminList)
//					{
//						ServerCommandController.serverCommandSend("chat "+chatMessage+" TO \"" + admin + "\"");
//					}
//				}

            }

        }

    }

}