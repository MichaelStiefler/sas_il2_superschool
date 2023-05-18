package mainController;

import java.util.Iterator;

import model.PilotBlueprint;
import model.PilotSortie;
import model.SortieEvent;
import utility.Time;

class PilotSortieController {
    public static void newSortie(String name) {
        try {
            MainController.SORTIES.put(name, new PilotSortie(MainController.PILOTS.get(name), Time.getTime(), MainController.ACTIVEMISSION.getStartTime()));
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotSortieController.newSortie - Unhandled Exception creating new sortie for (" + name + "): " + ex);
        }
    }

    public static void sortieStart(PilotSortie sortie) {
        try {
            if (sortie != null) {
                sortie.setSortieStartTime(Time.getTime());
                sortie.setMissionStartTime(MainController.ACTIVEMISSION.getStartTime());
            } else {
                MainController.writeDebugLogFile(1, "PilotSortieController.sortieStart - Error Null passed in for Sortie");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotSortieController.sortieStart - Unhandled Exception setting sortie Start time: " + ex);
        }
    }

    public static void sortieEnd(String name, SortieEvent.EventType event, long sortieEndTime) {
        PilotSortie pilotSortie = null;
        pilotSortie = MainController.SORTIES.get(name);
        if (pilotSortie != null) {
            if (MainController.PILOTS.get(name).getAerodrome() != null) {
                AerodromeController.returnPlane(MainController.PILOTS.get(name).getAerodrome(), name);
            }
            // Add as the Last Sortie Event why the sortie is ending
            SortieEvent sortieEvent = new SortieEvent(sortieEndTime, event, MainController.PILOTS.get(name).getPilotId());
            pilotSortie.setSortieEvents(sortieEndTime, sortieEvent);
            MainController.SORTIES.remove(name);
            // If the pilot has not disconnected then call to update sortie info from IL2
            if (event != SortieEvent.EventType.PILOTDISCONNECT) {
                // Since we are ending the sortie put it in the ending list so a new sortie can be created
                // for this pilot while we wait for the server to respond to the userStat request
                MainController.ENDINGSORTIES.put(name, pilotSortie);
                if (MainController.PILOTS.get(name).getState() == PilotBlueprint.ValidStates.KICK) {
                    new CommandUserStat(MainController.PILOTS.get(name), CommandUserStat.UserStatActionType.KICK, 0, 4000);
                } else {
                    // Create a new sortie for the pilot then update the pilot stats with userSTAT
                    newSortie(name);
                    new CommandUserStat(MainController.PILOTS.get(name), CommandUserStat.UserStatActionType.SORTIEEND, 0, 4000);
                }
            } else {
                MySQLConnectionController.writeSortie(name, pilotSortie);
                PilotController.pilotRemove(name);
            }
        } else {
            // Create a new sortie for the pilot then update the pilot stats with userSTAT
            newSortie(name);
            new CommandUserStat(MainController.PILOTS.get(name), CommandUserStat.UserStatActionType.NEWSORTIE, 0, 4000);
        }
    }

    public static void sortieEndAll(SortieEvent.EventType event, long sortieEndTime) {
        Iterator<String> it = MainController.PILOTS.keySet().iterator();
        while (it.hasNext()) {
            try {
                // Key is actually name but this is for clarity
                String key = it.next();
                if (MainController.SORTIES.containsKey(key)) {
                    // Check Airplane back In
                    if (MainController.PILOTS.get(key).getAerodrome() != null) {
                        AerodromeController.returnPlane(MainController.PILOTS.get(key).getAerodrome(), key);
                    }
                    // The Sortie is over because of mission Ended so post that in Sortie.
                    if (MainController.PILOTS.get(key).getState() == PilotBlueprint.ValidStates.SORTIEBEGIN || MainController.PILOTS.get(key).getState() == PilotBlueprint.ValidStates.INFLIGHT) {
                        SortieEvent sortieEvent = new SortieEvent(sortieEndTime, event, MainController.PILOTS.get(key).getPilotId());
                        MainController.SORTIES.get(key).setSortieEvents(sortieEndTime, sortieEvent);
                    }
                    // New Added 9/23/08 to help with rare problem
                    if (MainController.ENDINGSORTIES.containsKey(key)) {
                        MainController.writeDebugLogFile(1, "PilotSortieController.sortieEndAll - Error Sortie already in endingsorties list for pilot (" + key + ")");
                    } else {
                        MainController.ENDINGSORTIES.put(key, MainController.SORTIES.get(key));
                    }
                    MainController.SORTIES.remove(key);
                }
                // Create a new sortie for Pilot
                newSortie(key);
                // Set Pilots State in Pilot Record
                MainController.PILOTS.get(key).setState(PilotBlueprint.ValidStates.REFLY);
                // Call UserStatCommand to update pilot's attribute values to current
                new CommandUserStat(MainController.PILOTS.get(key), CommandUserStat.UserStatActionType.SORTIEEND, 0, 40000);

                // Added a timer here so the IL2 server has time to respond to the user STAT command
                // before another one is sent.
                try {
                    Thread.sleep(300);
                } catch (Exception ex) {}

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "PilotSortieController.endAllSorties (" + event + ") - Error: " + ex);
            }
        }
    }

    public static int sortieGetSortieDuration(String name) {
        return Time.getTimeDuration(MainController.SORTIES.get(name).getSortieStartTime());
    }

    public static boolean isSortieValid(String name, PilotSortie sortie) {
        // This method checks to see if anything happened during the sortie. If nothing happened,
        // the pilot simply selected their plane then hit refly we don't wan to record the sortie.
        boolean sortieIsValid = false;
        // 1st make sure sortie exists

        // Next check to see if they are trigger happy.
        if (((MainController.PILOTS.get(name).getFiBull() - sortie.getFiBull()) > 0) || ((MainController.PILOTS.get(name).getFiRock() - sortie.getFiRock()) > 0) || ((MainController.PILOTS.get(name).getFiBomb() - sortie.getFiBomb()) > 0)) {
            sortieIsValid = true;
        } else {
            // Finally check if there are more than 2 Sortie Events
            Iterator<Long> it = sortie.getSortieEvents().keySet().iterator();
            int counter = 0;
            boolean otherEvent = false;
            while (it.hasNext()) {
                // Key is actually name but this is for clarity
                long key = it.next();
                if ((sortie.getSortieEvents().get(key).getEventType() != SortieEvent.EventType.MISSIONEND) && (sortie.getSortieEvents().get(key).getEventType() != SortieEvent.EventType.SELECTPLANE)
                        && (sortie.getSortieEvents().get(key).getEventType() != SortieEvent.EventType.LOADEDWEAPONS) && (sortie.getSortieEvents().get(key).getEventType() != SortieEvent.EventType.PILOTDISCONNECT)
                        && (sortie.getSortieEvents().get(key).getEventType() != SortieEvent.EventType.PILOTREFLY)) {
                    otherEvent = true;
                }
                counter++;
            }
            if (counter > 3 || otherEvent) {
                sortieIsValid = true;
            }
        }
        return sortieIsValid;
    }

    public static void sortieWrite(String name) {
        // Write the Sortie info
        // Note this is only used for testing purposes.

        // Temp for testing
        System.out.println("End of sortie for: " + MainController.SORTIES.get(name).getName());
        System.out.println("Army: " + MainController.SORTIES.get(name).getArmy());
        System.out.println("score: " + (MainController.PILOTS.get(name).getScore() - MainController.SORTIES.get(name).getScore()));
        System.out.println("planeMarkings: " + MainController.SORTIES.get(name).getPlaneMarkings());
        System.out.println("eAir: " + MainController.SORTIES.get(name).getEAir());
        System.out.println("fAir: " + (MainController.PILOTS.get(name).getFAir() - MainController.SORTIES.get(name).getFAir()));
        System.out.println("fiBull: " + (MainController.PILOTS.get(name).getFiBull() - MainController.SORTIES.get(name).getFiBull()));
        System.out.println("hiBull: " + (MainController.PILOTS.get(name).getHiBull() - MainController.SORTIES.get(name).getHiBull()));
        System.out.println("hiABull: " + (MainController.PILOTS.get(name).getHiABull() - MainController.SORTIES.get(name).getHiABull()));
        System.out.println("fiRock: " + (MainController.PILOTS.get(name).getFiRock() - MainController.SORTIES.get(name).getFiRock()));
        System.out.println("hiRock: " + (MainController.PILOTS.get(name).getHiRock() - MainController.SORTIES.get(name).getHiRock()));
        System.out.println("fiBomb: " + (MainController.PILOTS.get(name).getFiBomb() - MainController.SORTIES.get(name).getFiBomb()));
        System.out.println("hiBomb: " + (MainController.PILOTS.get(name).getHiBomb() - MainController.SORTIES.get(name).getHiBomb()));
        System.out.println("weapons: " + MainController.SORTIES.get(name).getWeapons());
        System.out.println("fuel: " + MainController.SORTIES.get(name).getFuel());
        System.out.println("plane: " + MainController.SORTIES.get(name).getPlane());
        System.out.println("eAirConfirmed: " + (MainController.PILOTS.get(name).getEAirConfirmed() - MainController.SORTIES.get(name).getEAirConfirmed()));

        Iterator<Long> it = MainController.SORTIES.get(name).getSortieEvents().keySet().iterator();
        while (it.hasNext()) {
            try {
                Long key = it.next();
                String event = MainController.SORTIES.get(name).getSortieEvents().get(key).toString();
                System.out.println("Pilot ( " + name + " ) Time: " + key + " Event: " + event);
            } catch (Exception e) {
                MainController.writeDebugLogFile(1, "PilotSortieController.writeSortie Error writing Sortie Events: " + e.toString());
            }
        }

    }
}
