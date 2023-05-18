package mainController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import model.Aerodrome;
import model.GUICommand;
import model.Mission;
import model.MissionObject;
import model.Pilot;
import model.PilotSortie;
import model.PlaneLimit;
import model.QueueObj;
import model.SortieEvent;

class AerodromeController {

    public static Aerodrome getAerodrome(ArrayList<Aerodrome> aerodromes, double locationX, double locationY) {
        Aerodrome returnAerodrome = null;
        for (Aerodrome aerodrome : aerodromes) {
            if (locationX == aerodrome.getAerodromeLocationX() && locationY == aerodrome.getAerodromeLocationY()) {
                returnAerodrome = aerodrome;
                break;
            }
        }
        return returnAerodrome;
    }

    public static Aerodrome findAerodrome(Mission mission, double locationX, double locationY) {
        try {
            Aerodrome returnAerodrome = null;
            for (Aerodrome aerodrome : mission.getMissionParameters().getAerodromes()) {
                // Check Coordinates for location within Aerodrome radius
                if ((java.lang.Math.pow(java.lang.Math.abs(locationX - aerodrome.getAerodromeLocationX()), 2) + java.lang.Math.pow(java.lang.Math.abs(locationY - aerodrome.getAerodromeLocationY()), 2)) <= java.lang.Math.pow(
                        aerodrome.getAerodromeRadius(), 2)) {
                    return aerodrome;
                }
            }
            return returnAerodrome;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AerodromeController.findAerodrome - Unhandled Exception Mission (" + mission.getMissionName() + ") Location X(" + locationX + ") Y(" + locationY + "): " + ex);
            return null;
        }
    }

    public static Aerodrome getSpawnAerodrome(Mission mission, double locationX, double locationY, String plane, int army) {
        try {
            Aerodrome returnAerodrome = null;
            for (Aerodrome aerodrome : mission.getMissionParameters().getAerodromes()) {
                // Check Coordinates for Spawn Point within Aerodrome radius
                if ((java.lang.Math.pow(java.lang.Math.abs(locationX - aerodrome.getAerodromeLocationX()), 2) + java.lang.Math.pow(java.lang.Math.abs(locationY - aerodrome.getAerodromeLocationY()), 2)) <= java.lang.Math.pow(
                        aerodrome.getAerodromeRadius(), 2)) {
                    // Coordinates are within the Aerodrome Radius, Check to see if Aerodrome has plane selected
                    if (aerodrome.getPlanes().containsKey(plane) && aerodrome.getArmy() == army) {
                        // Correct Aerodrome so use it.
                        returnAerodrome = aerodrome;
                        break;
                    } else if (aerodrome.getArmy() != army) {
                        // They have been spawned in over an enemy aerodrome so return null
                        MainController.writeDebugLogFile(1, "AerodromeController.getAerodrome - Spawn point(" + locationX + "/" + locationY + ") Plane(" + plane + ") Army(" + army + ") was over Enemy Aerodrome (" + aerodrome.getAerodromeMapGrid() + ")");
                        return null;
                    }
                }
                // Maybe a Moving Carrier that is no longer within spawn point so match if aerodrome plane/army match
                if (aerodrome.getPlanes().containsKey(plane) && aerodrome.getArmy() == army) {
                    returnAerodrome = aerodrome;
                }
            }
            if (returnAerodrome == null) {
                MainController.writeDebugLogFile(1, "AerodromeController.getAerodrome - Error Could not determine Aerodrome for Spawn Point(" + locationX + "/" + locationY + ") Plane(" + plane + ") Army(" + army + ")");
            }
            return returnAerodrome;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AerodromeController.getAerodrome - Unhandled Exception X(" + locationX + ") Y(" + locationY + ") Plane(" + plane + ") Army(" + army + "): " + ex);
            return null;
        }
    }

    public static PlaneLimit getPlaneLimit(Aerodrome aerodrome, String plane) {
        try {
            PlaneLimit returnPlaneLimit = null;
            Iterator<String> it = aerodrome.getPlanes().keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (key.equals(plane)) {
                    returnPlaneLimit = aerodrome.getPlanes().get(key);
                    break;
                }
            }
            return returnPlaneLimit;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AerodromeController.getPlaneLimit - Unhandled Exception Aerodrom(" + aerodrome.getAerodromeMapGrid() + ") Plane(" + plane + "):" + ex);
            return null;
        }
    }

    public static void planeLimitAdjustPlanesInUseCount(Aerodrome aerodrome, String plane, int count) {
        PlaneLimit planeLimit = AerodromeController.getPlaneLimit(aerodrome, plane);
        if (planeLimit != null) {
            planeLimit.setPlanesInUse(planeLimit.getPlanesInUse() + count);
        }
    }

    public static void planeLimitAdjustPlanesLostCount(Aerodrome aerodrome, String plane, int count) {
        PlaneLimit planeLimit = AerodromeController.getPlaneLimit(aerodrome, plane);
        if (planeLimit != null) {
            planeLimit.setPlanesLost(planeLimit.getPlanesLost() + count);
        }
    }

    public static boolean isPlaneAvailable(Aerodrome aerodrome, String plane) {
        try {
            boolean planeIsAvailable = true;
            PlaneLimit planeLimit = AerodromeController.getPlaneLimit(aerodrome, plane);
            if (planeLimit != null) {
                MainController.writeDebugLogFile(2,
                        "Plane(" + plane + ") In Use(" + planeLimit.getPlanesInUse() + ") In Use Limit(" + planeLimit.getPlanesInUseLimit() + ") Lost(" + planeLimit.getPlanesLost() + ") Total Limit(" + planeLimit.getPlaneTotalLimit() + ")");
                if (planeLimit.getPlanesInUseLimit() > 0 && planeLimit.getPlanesInUse() >= planeLimit.getPlanesInUseLimit()) {
                    planeIsAvailable = false;
                }
                if (planeLimit.getPlaneTotalLimit() > 0 && planeLimit.getPlanesInUse() + planeLimit.getPlanesLost() >= planeLimit.getPlaneTotalLimit()) {
                    planeIsAvailable = false;
                }
            } else {
                planeIsAvailable = false;
            }
            return planeIsAvailable;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AerodromeController.isPlaneAvailable - Unhandled Exception Plane(" + plane + ") Aerodrome(" + aerodrome.getAerodromeMapGrid() + "): " + ex);
            return true;
        }
    }

    public static ArrayList<String> planeLimitGetPlanesAvailable(Mission mission, int army) { // Set to some high limit
        int numberLeft = 0;
        int numberInUseLeft = 0;
        boolean totalCheck = false;
        boolean inUseCheck = false;
        String planeString = "";
        String aeroString = null;
        String armyText = null;
        ArrayList<String> returnList = new ArrayList<String>();
        try {
            if (army == MainController.REDARMY)
                armyText = "Red";
            else
                armyText = "Blue";
            int i = 0;
            for (Aerodrome aerodrome : mission.getMissionParameters().getAerodromes()) {   // We are only checking Aerodromes that match requested army
                if (aerodrome.getArmy() == army) {
                    aeroString = armyText + " Aerodrome ID(" + i + ")Grid(" + aerodrome.getAerodromeMapGrid() + ") : ";
                    // Loop through all plane limits in each aerodrome
                    Iterator<String> it = aerodrome.getPlanes().keySet().iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        PlaneLimit planeLimit = aerodrome.getPlanes().get(key);
                        if (planeLimit.getPlaneTotalLimit() > 0) {
                            totalCheck = true;
                            numberLeft = planeLimit.getPlaneTotalLimit() - (planeLimit.getPlanesLost() + planeLimit.getPlanesInUse());
                        }

                        if (planeLimit.getPlanesInUseLimit() > 0) {
                            inUseCheck = true;
                            numberInUseLeft = planeLimit.getPlanesInUseLimit() - planeLimit.getPlanesInUse();
                        }
                        if (totalCheck && inUseCheck)
                            planeString = aeroString + key + "( " + (Math.min(numberLeft, numberInUseLeft) + " Available )");
                        else if (totalCheck)
                            planeString = aeroString + key + "( " + numberLeft + " Available )";
                        else if (inUseCheck)
                            planeString = aeroString + key + "( " + numberInUseLeft + " Available )";

                        if (planeString.length() > 0) {
                            returnList.add(planeString);
                        }
                        planeString = "";
                        totalCheck = false;
                        inUseCheck = false;
                        numberInUseLeft = 0;
                        numberLeft = 0;
                    }
                    aeroString = null;
                }
                i++;
            }
            if (returnList.size() == 0) {
                returnList.add("No Plane Limits for " + armyText);
            }
            return returnList;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AerodromeController.planeLimitGetPlanesAvailable - Error Unhandled Exception: " + ex);
            return returnList;
        }
    }

    public static void checkOutPlane(PilotSortie sortie, Pilot pilot) {
        String pilotName = pilot.getName();
        // String asciiPilotName = pilot.getAsciiTextName();
        try {
            // Get Aerodrome
            Aerodrome aerodrome = null;
            String plane = sortie.getPlane();
            aerodrome = AerodromeController.getSpawnAerodrome(MainController.ACTIVEMISSION, sortie.getSpawnPointX(), sortie.getSpawnPointY(), plane, sortie.getArmy());
            pilot.setAerodrome(aerodrome);
            // Check to see if this plane has limited availability
            if (aerodrome == null) {
                MainController.writeDebugLogFile(1, "AerodromeController.checkOutPlane - Error Aerodrome value null for Pilot(" + pilotName + ")");
                // Aerodrome could not be found, most likely pilot spawned in over an enemy aerodrome, Kick Pilot if configured
                if (MainController.CONFIG.isBadSpawnKick()) {
                    MainController.writeDebugLogFile(1, "AerodromeController.checkOutPlane: Pilot (" + pilot.getName() + ") Kicked because no Aerodrome could be found for Spawn Point");
                    PilotController.pilotKick(pilot.getName(), SortieEvent.EventType.PILOTKICKED);
                }
            } else {
                if (AerodromeController.isPlaneAvailable(aerodrome, plane) || (pilot.getLastPlaneFlown().equals(plane) && pilot.getPlaneIsOKToFly())) {
                    pilot.setPlaneIsOKToFly(true);
                    pilot.setLastPlaneFlown(plane);
                    MainController.writeDebugLogFile(2, "Pilot(" + pilotName + ") Selectd Plane(" + plane + ") from Aerodrome (" + aerodrome.getAerodromeMapGrid() + ") which is Available to Fly");
                    MainController.SORTIES.get(pilotName).setAerodrome(aerodrome);
                } else {
                    AerodromeController.planeLimitNotification(pilot, MainController.SORTIES.get(pilotName).getSortieStartTime());
                    pilot.setPlaneIsOKToFly(false);
                }
                AerodromeController.planeLimitAdjustPlanesInUseCount(aerodrome, plane, 1);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AerodromeController.checkOutPlane - Unhandled Exception Pilot(" + pilot + "):" + ex);
        }
    }

    public static void returnPlane(Aerodrome aerodrome, String pilot) {
        try {
            if (aerodrome != null) {
                String plane = MainController.SORTIES.get(pilot).getPlane();
                if (MainController.SORTIES.get(pilot).getPlaneStatus() == PilotSortie.PlaneStatus.CRASHED || MainController.SORTIES.get(pilot).getPlaneStatus() == PilotSortie.PlaneStatus.SHOTDOWN) {
                    AerodromeController.planeLimitAdjustPlanesInUseCount(aerodrome, plane, -1);
                    AerodromeController.planeLimitAdjustPlanesLostCount(aerodrome, plane, 1);
                } else {
                    AerodromeController.planeLimitAdjustPlanesInUseCount(aerodrome, plane, -1);
                }
                MainController.writeDebugLogFile(2, "AerodromeController.returnPlane - Returned Plane (" + plane + ") for Pilot(" + pilot + ")");
                // Plane is returned so clear out Aerodrome
                MainController.PILOTS.get(pilot).setAerodrome(null);
            } else {
                MainController.writeDebugLogFile(1, "AerodromeController.returnPlane - Error Returning Plane for Pilot(" + pilot + ") Aerodrome null");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AerodromeController.returnPlane - Error returning plane for Pilot ( " + pilot + " ) :" + ex);
        }
    }

    private static void setAerodromeObjectsArmy(Mission mission, int army, Aerodrome aerodrome) {
        // Loop through all Mission Static objects and set the army if the object is within the
        // Aerodrome radius
        Iterator<String> it = mission.getMissionObjects().keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            try {
                MissionObject missionObject = mission.getMissionObjects().get(key);
                double locationX = missionObject.getLocationX();
                double locationY = missionObject.getLocationY();

                if ((java.lang.Math.pow(java.lang.Math.abs(locationX - aerodrome.getAerodromeLocationX()), 2) + java.lang.Math.pow(java.lang.Math.abs(locationY - aerodrome.getAerodromeLocationY()), 2)) <= java.lang.Math.pow(
                        aerodrome.getAerodromeRadius(), 2)) { // Object is within the Aerodrome area.
                    missionObject.setArmy(army);
                    MainController.writeDebugLogFile(2, "AerodromeController.setAerodromeObjectsArmy - Mission Object (" + missionObject.getMissionObjectName() + ") switched to army (" + army + ")");
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "AerodromeController.setAerodromeArmySide - Error Unhandled Exception Army (" + army + ") Aerodrome (" + aerodrome.getAerodromeMapGrid() + ")");
            }
        }

    }

    public static void aerodromeOverrun(Mission mission, int army, String aerodromeName) {
        try {
            int aerodromeIndex = Integer.valueOf(aerodromeName.substring(9));
            Aerodrome aerodrome = mission.getMissionParameters().getAerodromes().get(aerodromeIndex);
            if (army == MainController.REDARMY) {
                aerodrome.setPlanes(aerodrome.getOverrunRedPlanes());
            } else if (army == MainController.BLUEARMY) {
                aerodrome.setPlanes(aerodrome.getOverrunBluePlanes());
            }
            aerodrome.setArmy(army);
            setAerodromeObjectsArmy(mission, army, aerodrome);
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AerodromeController.aerodromeOverrun - Error Unhandled Exception Army (" + army + ") Aerodrome (" + aerodromeName + ")");
        }
    }

    public static void planeLimitNotification(Pilot pilot, long sortieID) {
        final Timer timer = new Timer();

        class PlaneLimitNotification extends TimerTask {

            Pilot pilot;
            long  sortieID;
            int   counter;
            long  waitTime = MainController.CONFIG.getAutoKickTimer() * 1000;

            // Constructor passes in Pilot name and counter
            public PlaneLimitNotification(Pilot pilot, int counter, long sortieID) {
                this.pilot = pilot;
                this.counter = counter;
                this.sortieID = sortieID;
            }

            public void run() { // Make sure the Sortie is still there.
                String pilotName = pilot.getName();
                if (MainController.SORTIES.containsKey(pilot.getName())) {
                    // Make sure we are working with the same sortie
                    if (MainController.SORTIES.get(pilot.getName()).getSortieStartTime() == sortieID) {
                        // Check the plane
                        if (AerodromeController.isPlaneAvailable(pilot.getAerodrome(), MainController.SORTIES.get(pilotName).getPlane())
                                || (pilot.getLastPlaneFlown().equals(MainController.SORTIES.get(pilotName).getPlane()) && MainController.PILOTS.get(pilotName).getPlaneIsOKToFly())) {
                            // do Nothing plane is not limited
                        } else {
                            ServerCommandController.serverCommandSend("chat WARNING: Plane Not Available (" + MainController.SORTIES.get(pilotName).getPlane() + "), CHANGE Planes or be KICKED TO \"" + pilot.getAsciiTextName() + "\"");
                            if (counter < 3) {
                                counter++;
                                timer.schedule(new PlaneLimitNotification(pilot, counter, sortieID), waitTime);
                            } else {  // The've been warned so now kick them.
                                MainController.writeDebugLogFile(1, "Player ( " + pilotName + " ) Kicked for selecting an unavailable plane ( " + MainController.SORTIES.get(pilotName).getPlane() + " )");
                                GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.KICKPILOT);
                                newGUICommand.setName(pilotName);
                                MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
                            }
                        }
                    }
                }
            }
        } // PlaneLimitNotification Class
        timer.schedule(new PlaneLimitNotification(pilot, 0, sortieID), 1);
    } // End planeLimitNotification method

}
