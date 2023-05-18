package mainController;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Pilot;
import model.PilotSortie;
import utility.Time;
import utility.UnicodeFormatter;

public class CommandUser implements CommandInterface {
    Pilot             pilot;
    Pattern           patternUser;
    Pattern           patternUserData;
    Pattern           patternWaitFor = Pattern.compile("^\\<consoleN\\>\\<\\d+\\>");
    Matcher           m;
    ArrayList<String> commandDataReceived;
    long              startTime;
    int               attempts       = 0;

    public CommandUser(Pilot pilot, int attempts) {
        this.pilot = pilot;
//      changed to accomadate special characters in the pilot name and leading spaces
//		patternUser = Pattern.compile("^\\\\u0020\\d+\\s+" + pilot.getName() + "\\s+(\\d+)\\s+(-\\d+|\\d+)\\s*\\((\\d)\\)(?:Red|Blue|None)\\s*(.*)\\s+\\S+\\\\n$");
        patternUserData = Pattern.compile("\\s+(\\d+)\\s+(-\\d+|\\d+)\\s*\\((\\d)\\)(?:Red|Blue|None)\\s*(.*)\\s+\\S+\\\\n$");
        commandDataReceived = new ArrayList<String>();
        startTime = Time.getTime();
        this.attempts = attempts;
        initialize();
    }

    private void initialize() {
        MainController.COMMANDPARSE.addCommandQueue(this);
//		ServerCommandController.serverCommandSend("user \"" + pilot.getName() + "\"");
        ServerCommandController.serverCommandSend("user \"" + pilot.getAsciiTextName() + "\"");
    }

    public boolean addData(String line) {
//      Pattern matching does not work here because of special characters that can be in the name as well as leading/trailing spaces
//		if ((m = patternUser.matcher(line)).find())
        if (line.contains(pilot.getAsciiTextName()) && !line.startsWith("\\u0020N")) {
//			MainController.writeDebugLogFile(2, "CommandUser.addData patternUser match for ("+pilot.getName()+")");
            commandDataReceived.add(line);
            return true;
        }

        if ((m = patternWaitFor.matcher(line)).find()) {
            if (commandDataReceived.size() > 0) {
                MainController.COMMANDPARSE.removeCommandQueue(this);
                parseData();
                return true;
            }
        }

        if (Time.getTimeDuration(startTime) > 4000) {
            MainController.writeDebugLogFile(1, "CommandUser.addData - Timed-Out waiting for data for (" + pilot.getName() + ")");
            MainController.COMMANDPARSE.removeCommandQueue(this);
            if (attempts < 2) {
                attempts++;
                new CommandUser(pilot, attempts);
            } else {
                MainController.writeDebugLogFile(1, "CommandUser.addData - Timed-Out twice terminating user command for pilot (" + pilot.getName() + ")");
            }
        }

        return false;
    }

    private void parseData() {
        for (int i = 0; i < commandDataReceived.size(); i++) {
//			System.out.println("data:("+commandDataReceived.get(i)+")");
            m = patternUserData.matcher(commandDataReceived.get(i));
            if (m.find()) {
                MainController.writeDebugLogFile(2, "CommandUser.parseData: Data Received for Pilot (" + pilot.getName() + ")");

                try {
                    pilot.setPing(Integer.parseInt(m.group(1).trim())); // ping
                    pilot.setScore(Integer.parseInt(m.group(2).trim())); // score
                    pilot.setArmy(Integer.parseInt(m.group(3).trim())); // army
                    String markings = m.group(4).trim();
                    if (markings.contains("\\u")) {
                        markings = UnicodeFormatter.convertAsciiStringToUnicode(markings);
                    }
                    pilot.setPlaneMarkings(markings); // markings

                    // Now set the Info in the Sortie as well
                    PilotSortie sortie = null;
                    sortie = MainController.SORTIES.get(pilot.getName());
                    if (sortie != null) {
                        MainController.SORTIES.get(pilot.getName()).setArmy(pilot.getArmy());
                        MainController.SORTIES.get(pilot.getName()).setPlaneMarkings(pilot.getPlaneMarkings());
                        AerodromeController.checkOutPlane(sortie, pilot);
                    }
                    // Update The GUI with new Pilot Info
                    PilotPanelController.updatePilot(pilot.getName());
                    // Weapon Check Removed for version 2.0 as Weapon Checks are done by IL2 now.
                    // Weapon Check Reintroduced on UP3 backport
                    PlaneLoadoutRestrictionController.weaponsCheck(pilot.getName(), MainController.SORTIES.get(pilot.getName()).getSortieStartTime());
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "CommandUser.parseData - Error: " + ex);
                    MainController.writeDebugLogFile(1, " ** Data for Above ( " + commandDataReceived.get(i) + " )");
                }
            }
        }
    }
}
