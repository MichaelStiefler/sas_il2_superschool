package mainController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import model.BadLanguage;
import model.Configuration;
import model.MissionControl;
import model.Pilot;
import model.PilotSortie;
import model.PlanePilotsStatusMessages;
import model.PlanePilotsStatusWarnings;
import model.QueueObj;
import model.SortieEvent;
import thread.EventLogReader;
import thread.ServerListen;
import utility.SocketConnection;
import utility.Time;
import viewController.MainWindowController;

public class ConnectController {
    public static void connect() {
        // Configuration
        MainController.configInitialize();

        // Mission
        MainController.MISSIONCONTROL = new MissionControl();

        // Pilot
        MainController.PILOTS = new HashMap<String, Pilot>();
        MainController.SORTIES = new HashMap<String, PilotSortie>();
        MainController.ENDINGSORTIES = new HashMap<String, PilotSortie>();

        // GeoIP
        MainController.IPLOOKUPSERVICE = PilotGeoIPController.initialize(MainController.GEODBFILE);

        // Queues/Parsers
        MainController.QUEUEOBJECTS = new ArrayList<QueueObj>();
        MainController.COMMANDPARSE = new CommandParse();
        MainController.AUTHENTICATIONQUEUE = new ArrayList<String>();

        // Bad Language
        MainController.BADLANGUAGE = new BadLanguage(MainController.BADLANGUAGEFILENAME);
        if (MainController.CONFIG.getBadLanguageKick() > 0) {
            MainController.BADLANGUAGE = BadLanguageController.updateBadLanguageList(MainController.BADLANGUAGE);
        }

        // Server connection
        MainController.SERVERCONN = new SocketConnection(MainController.CONFIG.getServerIP(), MainController.CONFIG.getServerPort());
        if (MainController.SERVERCONN.connect()) {

            MainWindowController.setStatusMessage("Connected to Server (" + MainController.CONFIG.getServerIP() + ")");
            MainController.CONNECTED = true;

            // Server connection listen thread
            MainController.SERVERLISTEN = new ServerListen(MainController.SERVERCONN);
            new Thread(MainController.SERVERLISTEN).start();

            // Give the Listener time to get started before checking on Connection status
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {}
        } else {
            MainWindowController.setStatusMessage("Failed to Connect to Server (" + MainController.CONFIG.getServerIP() + ")");
            MainWindowController.displayMessage("Failed to Connect to Server", "Error");
            disconnect();
        }

        // Check for Pilots already in and load 1st mission
        if (MainController.CONNECTED) {
            MainWindowController.setStatusMessage("Listener Connection Successful");
            // Get Server Version
            new CommandServer();
            // Load pilots already connected
            PilotController.pilotFirstRun();
            // Check for Scheduled Mission or Mission Cycle to be run
            ScheduledEventController.checkForEvent();
            // Load Mission Cycle
            MissionController.loadMissionCycle();
            // Give the Host Command time to finish before loading a mission or starting with the eventlog
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {}

            MainController.EVENTLOG = new EventLogReader(MainController.CONFIG.getEventLog());
            // EventLog thread
            new Thread(MainController.EVENTLOG).start();

            // Set Ping and Icon settings on Server
            setIL2ServerSettings(MainController.CONFIG);

            // Load a Mission. If mission load fails, disconnect
            if (!MissionController.missionControllerLoadNext()) {
                MainWindowController.displayMessage("Mission Failed to Load", "Error");
                disconnect();
            }

            // Set PlanePilot Messages
            if (MainController.CONFIG.getAnnouncePlanePilotLosses().equals("ALL")) {
                MainController.PLANEPILOTMESSAGES = new PlanePilotsStatusMessages(PlanePilotsStatusMessages.Type.ALL, MainController.CONFIG.getPlanePilotLossLimit());
            } else if (MainController.CONFIG.getAnnouncePlanePilotLosses().equals("ARMY")) {
                MainController.PLANEPILOTMESSAGES = new PlanePilotsStatusMessages(PlanePilotsStatusMessages.Type.TEAM, MainController.CONFIG.getPlanePilotLossLimit());
            } else {
                MainController.PLANEPILOTMESSAGES = new PlanePilotsStatusMessages(PlanePilotsStatusMessages.Type.NONE, MainController.CONFIG.getPlanePilotLossLimit());
            }

            // Set PlanePilot Warning Messages
            // These will show up at the intervals set below
            ArrayList<Integer> planePilotWarningInterval = new ArrayList<Integer>(5);
            try {
                String strWarningInterval = MainController.CONFIG.getPlanePilotLossWarningInterval();
                String[] intervals = strWarningInterval.split(",");
                for (int i = 0; i < intervals.length; i++) {
                    planePilotWarningInterval.add(Integer.valueOf(intervals[i].trim()));
                }

                if (MainController.CONFIG.getPlanePilotLossWarning().equals("ALL")) {
                    MainController.PLANEPILOTWARNING = new PlanePilotsStatusWarnings(planePilotWarningInterval, PlanePilotsStatusWarnings.Type.ALL);
                } else if (MainController.CONFIG.getPlanePilotLossWarning().equals("ARMY")) {
                    MainController.PLANEPILOTWARNING = new PlanePilotsStatusWarnings(planePilotWarningInterval, PlanePilotsStatusWarnings.Type.TEAM);
                }
                if (MainController.CONFIG.getPlanePilotLossWarning().equals("NONE")) {
                    MainController.PLANEPILOTWARNING = new PlanePilotsStatusWarnings(planePilotWarningInterval, PlanePilotsStatusWarnings.Type.NONE);
                }

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "ConnectController.connect - Error Unhandled exception parsing planePilotWarningInterval: " + ex);
            }
            if (MainController.CONFIG.getAnnouncePlanePilotLosses().equals("ALL")) {
                MainController.PLANEPILOTWARNING = new PlanePilotsStatusWarnings(planePilotWarningInterval, PlanePilotsStatusWarnings.Type.ALL);
            } else if (MainController.CONFIG.getAnnouncePlanePilotLosses().equals("ARMY")) {
                MainController.PLANEPILOTWARNING = new PlanePilotsStatusWarnings(planePilotWarningInterval, PlanePilotsStatusWarnings.Type.TEAM);
            } else {
                MainController.PLANEPILOTWARNING = new PlanePilotsStatusWarnings(planePilotWarningInterval, PlanePilotsStatusWarnings.Type.NONE);
            }

        }

        // Counters for things we want to check less often
        int pilotLoginCheckThrottle = 1;
        int missionOverCheckThrottle = 1;
        int schedulerThrottle = 1;
        // CONNECT LOOP
        while (MainController.CONNECTED) {
            try {
                // Check Input Queue
                MainParseController.processQueue(MainController.doQueue(null));
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "ConnectController.connect - Error Unhandled exception checking process queue: " + ex);
            }
            // If Mission is not over, Check/Update mission status
            if (!MainController.MISSIONCONTROL.getMissionOver()) {
                // The Sleep timer for this loop is 0.5 seconds. Mission Over is checked every 10 seconds
                if (missionOverCheckThrottle > 20) {
                    // Check to see if mission should be stopped
                    try {
                        MissionController.missionOverCheck();
                    } catch (Exception ex) {
                        MainController.writeDebugLogFile(1, "ConnectController.connect - Error Unhandled excpetion in Checking to see if Mission is over: " + ex);
                    }
                    missionOverCheckThrottle = 0;
                }
                missionOverCheckThrottle++;
            } else {
                // Mission is over, gracefully close old mission and start a new one

                // 1st lets clear out the mission vote if one was initiated.
                MissionController.clearMissionVote();

                if (!MainController.MISSIONCONTROL.getMissionOverMessageSent()) {
                    MissionController.missionControllerOverNotify();
                } else if (MainController.MISSIONCONTROL.getMissionEndTimeLimitExpired() && !MainController.MISSIONCONTROL.getMissionEndSent()) {
                    ServerCommandController.serverCommandSend("mission END");
                    MainController.writeDebugLogFile(2, "ConnectController.connect: Connect Loop: Mission End command sent");
                    MainController.MISSIONCONTROL.setMissionEndSent(true);
                }
                // Wait until Mission End processing has finished or 30 seconds has expired before
                // loading the next mission.
                else if (MainController.MISSIONCONTROL.getMissionEndReceived()) {
                    MainController.writeDebugLogFile(1,
                            "ConnectController.connect: Connect Loop: Mission ( " + MainController.ACTIVEMISSION.getMissionName() + " ) Ended after " + (MainController.ACTIVEMISSION.getEndTime() - MainController.ACTIVEMISSION.getStartTime()) / 60000
                                    + " Minutes");
                    pilotLoginCheckThrottle = 1;
                    missionOverCheckThrottle = 1;

                    // Check Input Queue now to process the user STAT request
                    try {
                        MainParseController.processQueue(MainController.doQueue(null));
                    } catch (Exception ex) {
                        MainController.writeDebugLogFile(1, "PilotSortieController.sortieEndAll - Error Unhandled exception checking process queue: " + ex);
                    }

                    // Load a Mission. If mission load fails, disconnect
                    if (!MissionController.missionControllerLoadNext()) {
                        MainWindowController.displayMessage("Mission Failed to Load", "Error");
                        disconnect();
                    }
                    MainController.PLANEPILOTMESSAGES.ResetAll();
                    MainController.PLANEPILOTWARNING.ResetAll();

                    // Reload Bad word list. Allows you to update list while FBDj is running
                    if (MainController.CONFIG.getBadLanguageKick() > 0) {
                        BadLanguageController.updateBadLanguageList(MainController.BADLANGUAGE);
                    }
                }
            }

            // ReservedName and Admin authentication checks
            if (pilotLoginCheckThrottle > 40) {
                // Check authentication if in queue
                if (MainController.AUTHENTICATIONQUEUE.size() > 0) {
                    PilotController.pilotValidateAuthentication();
                }
                // Reset counter
                pilotLoginCheckThrottle = 0;

                // Since were here every 20 seconds lets update the Active data in Stats
                // And update the Mission Status Tab in the GUI
                try {
                    MySQLConnectionController.writeActiveMission(MainController.ACTIVEMISSION);
                    MySQLConnectionController.writeActivePilots();
                    MissionStatusPanelController.initializeData(MainController.ACTIVEMISSION);
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "ConnectController.connect update mission status Error: " + ex);
                }
            }
            pilotLoginCheckThrottle++;

            // Check for calendar Events every minute
            if (schedulerThrottle > 120) {
                ScheduledEventController.checkForEvent();
                schedulerThrottle = 0;
            }
            schedulerThrottle++;

            // Throttle the loop
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                MainController.writeDebugLogFile(1, "ConnectController.connect: Connect Loop: InterruptedException!");
                e.printStackTrace();
            }
        } // CONNECT LOOP
    }

    public static void disconnect() {
        if (MainController.CONNECTED) {
            MainController.writeDebugLogFile(1, "ConnectController.disconnect: Beginning disconnect.");
            MainWindowController.setStatusMessage("Stopping Mission");

            // CLOSE OUT EVERYTHING
            // Stop ConnectController.connect while loop
            MainController.CONNECTED = false;

            // Stop EventLogReader
            MainController.EVENTLOG.stop();
            MainController.writeDebugLogFile(1, "ConnectController.disconnect: EventLogReader thread stopped.");

            // End all sorties
            PilotSortieController.sortieEndAll(SortieEvent.EventType.PILOTDISCONNECT, Time.getTime());

            // End the Mission and Close it out in the stats
            if (MainController.ACTIVEMISSION != null) {
                MainController.ACTIVEMISSION.setEndTime(Time.getTime());
                MySQLConnectionController.endMission(MainController.ACTIVEMISSION);

                // Wait 3 seconds to allow Sorties to be processed.
                try {
                    MainWindowController.setStatusMessage("Stopping Mission.");
                    Thread.sleep(1000);
                    MainWindowController.setStatusMessage("Stopping Mission..");
                    Thread.sleep(1000);
                    MainWindowController.setStatusMessage("Stopping Mission...");
                    Thread.sleep(1000);
                    MainWindowController.setStatusMessage("Stopping Mission....");
                } catch (Exception ex) {}
            }

            MainWindowController.setStatusMessage("Closing Connections");

            // Finish off Sorties
            MySQLConnectionController.validateSorties();

            // Stop SocketListener
            MainController.SERVERLISTEN.stop();
            // Disconnect socket
            MainController.SERVERCONN.disconnect();
            MainController.writeDebugLogFile(1, "ConnectController.disconnect: Server connection thread stopped and socket terminated.");
            // Clear Mission Status Panel
            MissionStatusPanelController.removeMissionData();

            MainController.ACTIVEMISSION = null;
            MainController.MISSIONCONTROL = null;
            // Pilot
            MainController.PILOTS = null;
            MainController.SORTIES = null;
            // Admin
            // Queues/Parsers
            MainController.QUEUEOBJECTS = null;
            MainController.AUTHENTICATIONQUEUE = null;
            MainController.COMMANDPARSE = null;
            // Server connection
            MainController.SERVERCONN = null;
            // Server connection listen thread
            MainController.SERVERLISTEN = null;
            // EventLog
            MainController.EVENTLOG = null;
        }

        MainController.writeDebugLogFile(1, "ConnectController.disconnect: Disconnect complete.");
        MainWindowController.setStatusMessage("Server Disconnected");

    }

    public static void reConnect() {
        final Timer timer = new Timer();

        class ReConnect extends TimerTask {

            int  counter;
            long waitTime = 5000;

            // Constructor passes in Pilot name and counter
            public ReConnect(int counter) {
                this.counter = counter;
            }

            public void run() {
                MainController.writeDebugLogFile(1, "ConnectController.reConnect - Shutting down everything");
                new Thread(new StopMainController()).start();

                try {
                    Thread.sleep(10000);
                } catch (Exception ex) {}

                MainController.writeDebugLogFile(1, "ConnectController.reConnect - Attempting ReStart");
                MainWindowController.setStatusMessage("Attempting Restart");

                new Thread(new MainController()).start();

                try {
                    Thread.sleep(20000);
                } catch (Exception ex) {}

                if (MainController.CONNECTED) {
                    MainController.writeDebugLogFile(1, "ConnectController.reConnect - ReStart Successful");
                } else {
                    if (counter < 3) {
                        // Did not re-connect so wait 5 seconds and try again.
                        counter++;
                        MainController.writeDebugLogFile(1, "ConnectController.reConnect - ReStart UnSuccessful - retry in 5 sec");
                        MainWindowController.setStatusMessage("Restart to Try Again");

                        timer.schedule(new ReConnect(counter), waitTime);
                    } else {
                        MainController.writeDebugLogFile(1, "ConnectController.reConnect - Error Could not re-establish Connection after 3 attempts, shutting down.");
                        MainController.disconnect();
                        MainWindowController.setStatusMessage("Restart Failed");
                    }
                }
            }
        }

        // When weaponsCheck method is called 1st time put it on the timer
        timer.schedule(new ReConnect(0), 1);

    }

    public static void setIL2ServerSettings(Configuration config) {
        try {
            // If Ping Kick is enabled then send down the ping kick settings.
            if (config.enablePingKick()) {
                String pingCmd = "maxping " + config.getMaxPing() + " DELAY " + config.getPingDelay() + " WARNINGS " + config.getPingWarnings();
                ServerCommandController.serverCommandSend(pingCmd);
            } else {
                ServerCommandController.serverCommandSend("maxping OFF");
            }

            // Send down the Icon Settings for Friendly
            String iconCmd = "mp_dotrange FRIENDLY COLOR " + (Double) config.getIconSettings().get("fColor").getValue() + " DOT " + (Double) config.getIconSettings().get("fDot").getValue() + " RANGE "
                    + (Double) config.getIconSettings().get("fRange").getValue() + " TYPE " + (Double) config.getIconSettings().get("fType").getValue() + " ID " + (Double) config.getIconSettings().get("fId").getValue() + " NAME "
                    + (Double) config.getIconSettings().get("fName").getValue();
            ServerCommandController.serverCommandSend(iconCmd);

            // Send down the Icon Settings for Enemy/Foe
            iconCmd = "mp_dotrange FOE COLOR " + (Double) config.getIconSettings().get("eColor").getValue() + " DOT " + (Double) config.getIconSettings().get("eDot").getValue() + " RANGE " + (Double) config.getIconSettings().get("eRange").getValue()
                    + " TYPE " + (Double) config.getIconSettings().get("eType").getValue() + " ID " + (Double) config.getIconSettings().get("eId").getValue() + " NAME " + (Double) config.getIconSettings().get("eName").getValue();
            ServerCommandController.serverCommandSend(iconCmd);
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ConnectController.setIL2ServerSettings - Error Unhandled Exception: " + ex);
        }

    }
}
