package mainController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Pilot;
import model.SortieEvent;
import utility.Time;

public class CommandHost implements CommandInterface {
    public enum HostActionType {
        FIRSTRUN, CHECKPILOTS
    }

    Pilot             pilot;
    Pattern           patternHost;
    Pattern           patternWaitFor = Pattern.compile("^\\<consoleN\\>\\<\\d+\\>");
    Matcher           m;
    ArrayList<String> commandDataReceived;
    long              startTime;
    HostActionType    hostAction;

    public CommandHost(Pilot pilot, HostActionType hostAction) {
        this.pilot = pilot;
        patternHost = Pattern.compile("^\\\\u0020\\d+:\\s*(.*?)\\s*\\[(\\d+)\\](\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}):\\d+\\\\n");
        commandDataReceived = new ArrayList<String>();
        startTime = Time.getTime();
        initialize();
        this.hostAction = hostAction;
    }

    private void initialize() {
        MainController.COMMANDPARSE.addCommandQueue(this);
        ServerCommandController.serverCommandSend("host");
    }

    public boolean addData(String line) {
        try {
            if ((m = patternHost.matcher(line)).find()) {
                commandDataReceived.add(line);
                return true;
            }

            if ((m = patternWaitFor.matcher(line)).find()) {
                MainController.COMMANDPARSE.removeCommandQueue(this);
                parseData();
                return true;
            }

            if (Time.getTimeDuration(startTime) > 5000) {
                MainController.writeDebugLogFile(1, "CommandHost.addData - Timed-Out waiting for (" + pilot.getName() + ")");
                MainController.COMMANDPARSE.removeCommandQueue(this);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "CommandHost.addData - Error Unhandled Exception: " + ex);
            MainController.writeDebugLogFile(1, " ** Line: " + line);

        }
        return false;
    }

    private void parseData() {
        switch (hostAction) {
            case FIRSTRUN:
                try {
                    MainController.writeDebugLogFile(2, "CommandHost.parseData(FIRSTRUN) - Data Received");
                    // Loop through all Players returned from Host command and create a Pilot record for them and add
                    // to the Pilots List
                    for (int i = 0; i < commandDataReceived.size(); i++) {
                        m = patternHost.matcher(commandDataReceived.get(i));
                        if (m.find()) {
                            // Added the lines below to strip out the Pilot Name from the Input line.
                            // Using the regex to get it strips off the leading/trailing spaces.
                            // A host command returns the following
                            // <number>: <pilotName> [<socket>]<ip>:<port>
                            String hostLine = commandDataReceived.get(i);
                            int nameStartIndex = hostLine.indexOf(":");
                            int nameStopIndex = hostLine.lastIndexOf("[");
                            String pilotName = hostLine.substring(nameStartIndex + 2, nameStopIndex - 1);
                            String ipAddress = m.group(3).trim();
                            int socket = Integer.parseInt(m.group(2));
                            PilotController.pilotAdd(pilotName, ipAddress, socket);
                        }
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "CommandHost.parseData(FIRSTRUN) - Error Unhandled Exception: " + ex);
                }
                break;
            case CHECKPILOTS:
                int currentPilotCount = 0;
                int pilotCountReturned = 0;
                int pilotRemovedCount = 0;
                try {
                    // Set Valid Connection to false for all pilots so they can be re-checked
                    Iterator<String> it = MainController.PILOTS.keySet().iterator();
                    while (it.hasNext()) {
                        // Key is actually name but this is for clarity
                        String key = it.next();
                        MainController.PILOTS.get(key).setValidConnection(false);
                        currentPilotCount++;
                    }
                    MainController.writeDebugLogFile(2, "CommandHost.parseData(CHECKPILOTS) Current Pilot Count (" + currentPilotCount + ")");
                    currentPilotCount = 0;
                    // Loop through all lines received from Host Command
                    for (int i = 0; i < commandDataReceived.size(); i++) {
                        m = patternHost.matcher(commandDataReceived.get(i));
                        if (m.find()) {
                            // Added the lines below to strip out the Pilot Name from the Input line.
                            // Using the regex to get it strips off the leading/trailing spaces.
                            // A host commands returns the following
                            // <number>: <pilotName> [<socket>]<ip>:<port>
                            Pilot newPilot = null;
                            String hostLine = commandDataReceived.get(i);
                            int nameStartIndex = hostLine.indexOf(":");
                            int nameStopIndex = hostLine.lastIndexOf("[");
                            String pilotName = hostLine.substring(nameStartIndex + 2, nameStopIndex - 1);
                            String ipAddress = m.group(3).trim();
                            int socket = Integer.parseInt(m.group(2));
                            // If the Pilot doesn't already exist in the Pilots list create the new pilot
                            // and add to the list
                            if (!MainController.PILOTS.containsKey(pilotName)) {
                                if (pilotName.equals(pilot.getName())) {
                                    newPilot = pilot;
                                    MainController.writeDebugLogFile(2, "CommandHost.parseData - Data Received for: (" + pilotName + ")");
                                } else {
                                    newPilot = new Pilot(pilotName, ipAddress, socket, Time.getTime());
                                    MainController.writeDebugLogFile(2, "CommandHost.parseData - Host PilotName(" + pilotName + ") Did not match EventLog PilotName(" + pilot.getName() + ") and was not in Pilot List");

                                }

                                MainController.PILOTS.put(pilotName, newPilot);
                                newPilot.setValidConnection(true);
                                MainController.writeDebugLogFile(2, "CommandHost.parseData - (" + pilotName + ") Added to Pilot List");
                                PilotController.pilotAddDataReceived(newPilot);

                            } else {
                                MainController.PILOTS.get(pilotName).setIPAddress(m.group(3).trim());
                                MainController.PILOTS.get(pilotName).setSocket(Integer.parseInt(m.group(2)));
                                MainController.PILOTS.get(pilotName).setValidConnection(true);

                                boolean invalid = PilotController.pilotValidateLogon(pilot);
                                if (invalid) {
                                    MainController.writeIPAccessLogFile(pilot.getName() + " (Banned Being Kicked)", pilot.getIPAddress());
                                    PilotController.pilotKick(pilot.getName(), SortieEvent.EventType.PILOTBANNED);
                                }
                            }
                            pilotCountReturned++;
                        }
                    }
                    MainController.writeDebugLogFile(2, "CommandHost.parseData(NEWPILOT) Host Returned Count (" + pilotCountReturned + ")");

                    it = MainController.PILOTS.keySet().iterator();
                    while (it.hasNext()) {
                        // Key is actually name but this is for clarity
                        String key = it.next();

                        // If pilot was not validated above then it is no longer in the game and should be removed.
                        if (!MainController.PILOTS.get(key).isValidConnection()) {
                            // OK we have a pilot still in the list which no longer is in the game.
                            // if (MainController.SORTIES.containsKey(key))
                            // {
                            // PilotSortieController.sortieEnd(key, SortieEvent.EventType.PILOTDISCONNECT, Time.getTime());
                            // }
                            // Remove pilot from the Pilots list
                            // it.remove();
                            // Remove Pilot from the GUI
                            // PilotPanelController.removePilot(key);
                            MainController.writeDebugLogFile(1, "CommandHost.parseData(NEWPILOT) - Error Pilot Name(" + key + ") is not found in IL2");
                            pilotRemovedCount++;
                        }
                        currentPilotCount++;
                    }
                    MainController.writeDebugLogFile(2, "CommandHost.parseData(NEWPILOT) Pilot Removed Count (" + pilotRemovedCount + ")");
                    MainController.writeDebugLogFile(2, "CommandHost.parseData(NEWPILOT) Current Pilot Count (" + currentPilotCount + ")");
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "CommandHost.parseData.NEWPILOT - Error exception checking Pilots: " + ex);
                }
                break;

        }

    }
}
