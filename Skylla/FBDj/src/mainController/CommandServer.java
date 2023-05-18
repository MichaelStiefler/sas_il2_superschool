package mainController;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.Time;

public class CommandServer implements CommandInterface {

    Pattern           patternWaitFor = Pattern.compile("^\\<consoleN\\>\\<\\d+\\>");
    Matcher           m;

    @SuppressWarnings("unused")
    private String    serverVersion;
    ArrayList<String> commandDataReceived;
    long              startTime;

    public CommandServer() {
        this.serverVersion = "Unknown";
//		patternHost = Pattern.compile("^\\\\u0020\\d+:\\s*(.*?)\\s*\\[(\\d+)\\](\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}):\\d+\\\\n");
        commandDataReceived = new ArrayList<String>();
        startTime = Time.getTime();
        initialize();
    }

    private void initialize() {
        MainController.COMMANDPARSE.addCommandQueue(this);
        ServerCommandController.serverCommandSend("server");
    }

    public boolean addData(String line) {
        try {
            commandDataReceived.add(line);
            if ((m = patternWaitFor.matcher(line)).find()) {
                MainController.COMMANDPARSE.removeCommandQueue(this);
                parseData();
                return true;
            }
            if (Time.getTimeDuration(startTime) > 5000) {
                MainController.writeDebugLogFile(1, "CommandServer.addData - Timed-Out");
                MainController.COMMANDPARSE.removeCommandQueue(this);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "CommandServer.addData - Error Unhandled Exception: " + ex);
            MainController.writeDebugLogFile(1, " ** Line: " + line);

        }
        return false;

    }

    private void parseData() {
        try {
            for (int i = 0; i < commandDataReceived.size(); i++) {
                String response = commandDataReceived.get(i);
                if (response.startsWith("Description:")) {
                    String version = response.split("\\(")[1];
                    version = version.split("\\)")[0];
                    MainController.writeDebugLogFile(1, "IL2 Server Version(" + version + ")");
                    MainController.CONFIG.setServerVersion(version);
                    break;
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "CommandServer.parseData - Error Unhandled Exception: " + ex);
        }
    }
}
