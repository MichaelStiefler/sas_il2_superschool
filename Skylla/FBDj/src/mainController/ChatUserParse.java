package mainController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.UnicodeFormatter;

/*
 * Parse user commands from chat
 */

class ChatUserParse {
    private static final Pattern login       = Pattern.compile("^(?i)login\\s+(.*)\\\\n$");
    private static final Pattern nextmap     = Pattern.compile("^(?i)nextmap\\\\n$");
    private static final Pattern planes      = Pattern.compile("^(?i)planes\\\\n$");
    private static final Pattern objective   = Pattern.compile("^(?i)(?:objective|obj)\\<(red|blue)\\\\n$");
    private static final Pattern timeleft    = Pattern.compile("^(?i)timeleft|tl\\\\n$");
    private static final Pattern gunstat     = Pattern.compile("^(?i)gunstat|gs\\\\n$");
    private static final Pattern restriction = Pattern.compile("^(?i)(?:loadoutrestriction|load)\\<(red|blue)\\\\n$");
    private static final Pattern help        = Pattern.compile("^(?i)help\\\\n$");
    private static final Pattern teams       = Pattern.compile("^(?i)teams\\\\n$");
    private static final Pattern vote        = Pattern.compile("^(?i)vote\\s+(.*)\\\\n$");
    //TODO: skylla: request_mission command:
    private static final Pattern request	 = Pattern.compile("^(?i)request_mission\\s+(.*)\\\\n$");

    public static void parseUserCommand(String name, String command) {
//		System.out.println("ChatUser.parseUserCommand: Received data from " + name + ": " + command);

        Matcher m;
        try {
            if ((m = login.matcher(command)).find()) {
                String password = m.group(1);
                ChatUserController.userCommandLogin(name, password);
            }

            // Timeleft
            else if ((m = timeleft.matcher(command)).find()) {
                ChatUserController.userCommandTimeleft(name);
            }

            // gunstat
            else if ((m = gunstat.matcher(command)).find()) {
                ChatUserController.userCommandGunStat(name);
            }

            // Planes
            else if ((m = planes.matcher(command)).find()) {
                ChatUserController.userCommandPlanes(name);
            }

            // Next Mission
            else if ((m = nextmap.matcher(command)).find()) {
                ChatUserController.userCommandNextMap(name);
            }

            // Targets
            else if ((m = objective.matcher(command)).find()) {
                String targetColor = m.group(1).trim();
                ChatUserController.userCommandObjective(name, targetColor);
            }
            // Restrictions
            else if ((m = restriction.matcher(command)).find()) {
                String targetColor = m.group(1).trim();
                ChatUserController.userCommandRestriction(name, targetColor);
            }
            // Help
            else if ((m = help.matcher(command)).find()) {
                ChatUserController.userCommandHelp(name);
            }
            // Teams
            else if ((m = teams.matcher(command)).find()) {
                ChatUserController.userCommandTeams(name);   
            } 
            //TODO: skylla: request_mission command
            else if((m = request.matcher(command)).find()) {
            	String newMission = m.group(1).trim();
            	ChatUserController.userCommandRequest(name, newMission);
            //end
            } else if ((m = vote.matcher(command)).find()) {
                String vote = m.group(1);
                ChatUserController.userCommandVote(name, vote);
            } else {
                ServerCommandController.serverCommandSend("chat Command ( " + command + " ) not Recognized TO \"" + name + "\"");
//				System.out.println("ChatUserParse.parseUser: " + name + "Command not recognized: " + command);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ChatUserParse.parseUserCommand - Error on Command (" + command + ") error: " + ex);
        }
        // Log user command
        String unicodeName = name;

        if (name.contains("\\u")) {
            unicodeName = UnicodeFormatter.convertAsciiStringToUnicode(name);
        }
        String data = "User Command: " + unicodeName + ": " + command;
        LogController.writeChatLogFile(data);
    }
}
