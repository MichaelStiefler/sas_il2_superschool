package mainController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.UnicodeFormatter;

/*
 * Parses chat data from the ServerConnection, and commands from the GUI, and sends them to the appropriate controller.
 */

class CommandParse {
    public enum ChatType {
        ADMINCMD, USERCMD, MESSAGE, SERVER
    }

    private static final Pattern        chat      = Pattern.compile("Chat:\\s");
    private static final Pattern        admin     = Pattern.compile("Chat:\\s(.*):\\s\\\\t!(.*)");
    private static final Pattern        user      = Pattern.compile("Chat:\\s(.*):\\s\\\\t<(.*)");
    private static final Pattern        otherChat = Pattern.compile("Chat:\\s(.*):\\s\\\\t(.*)\\\\n$");
    // private static final Pattern event = Pattern.compile("Chat:\\s---\\s(.*)\\\\n$");

    private ArrayList<CommandInterface> commandQueue;

    public CommandParse() {
        commandQueue = new ArrayList<CommandInterface>();
    }

    public void parseCommand(String line) {
//		System.out.println("CommandParse.parseCommand Line received: " + line);

        Matcher mChat;

        try {
            // Check to see if this is a New Connection established with IL2
            if (line.startsWith("socket channel") && line.endsWith("is complete created\\n")) {
                try {
                    int socket = Integer.valueOf(line.split("'")[1]);
                    String[] lineSections = line.split(",");
                    int numSections = lineSections.length;
                    String ipAddress = line.split(",")[1].substring(4).split(":")[0];
                    String pilotName = line.split(",")[2].substring(1);
                    for (int i = 4; i < numSections; i++) {
                        pilotName = pilotName + ',' + lineSections[i - 1];
                    }
                    PilotController.pilotAdd(pilotName, ipAddress, socket);
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "CommandParse.parseCommand - Error Unhandled Exception processing new pilot " + ex);
                    MainController.writeDebugLogFile(1, "CommandParse.parseCommand - ** Line: " + line);
                }
            }
            // Next See if this is a Connection Being Closed with IL2
            else if (line.startsWith("socketConnection") && line.contains("lost.  Reason:")) {
                try {
                    int socket = Integer.valueOf(line.split(" ")[5]);
                    PilotController.pilotDisconnect(socket);
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "CommandParse.parseCommand - Error Unhandled Exception processing disconnect " + ex);
                    MainController.writeDebugLogFile(1, "CommandParse.parseCommand - ** Line: " + line);
                }
            }
            // See if it is a "Chat" message
            else if ((mChat = chat.matcher(line)).find()) {

                // Is it an Admin Command
                if ((mChat = admin.matcher(line)).find()) {
                    String name = mChat.group(1);
                    String command = mChat.group(2);

                    if (name.contains("\\u")) {
                        name = UnicodeFormatter.convertAsciiStringToUnicode(name);
                    }

                    AdminCommandParse.parseAdminCommand(name, command);
                    MySQLConnectionController.writeChatMessage(ChatType.ADMINCMD, name, command);
                }

                // Possibly a User Command
                else if ((mChat = user.matcher(line)).find()) {
                    String name = mChat.group(1);
                    String command = mChat.group(2);

                    ChatUserParse.parseUserCommand(name, command);
                    MySQLConnectionController.writeChatMessage(ChatType.USERCMD, name, command);
                }

                // // Event
                // else if ((mChat = event.matcher(line)).find())
                // {
                // String event = mChat.group(1);
                //
                // ChatEventParse.parseEvent(event);
                // }

                // Log other chat
                else if ((mChat = otherChat.matcher(line)).find()) {
                    String name = mChat.group(1);
                    String chitChat = mChat.group(2);

                    if (name.contains("\\u")) {
                        name = UnicodeFormatter.convertAsciiStringToUnicode(name);
                    }

                    if (chitChat.contains("\\u")) {
                        chitChat = UnicodeFormatter.convertAsciiStringToUnicode(chitChat);
                    }

                    if (name.equals("Server")) {
                        MySQLConnectionController.writeChatMessage(ChatType.SERVER, name, chitChat);
                    } else {
                        MySQLConnectionController.writeChatMessage(ChatType.MESSAGE, name, chitChat);
                        if (MainController.CONFIG.getBadLanguageKick() > 0) {
                            HashMap<String, String> badWordList = MainController.BADLANGUAGE.getBadWordList();
                            BadLanguageController checkChatLine = new BadLanguageController(name, chitChat, badWordList);
                            new Thread(checkChatLine).start();
                        }

                    }
                    String data = "Regular chat: " + name + ": " + chitChat;
                    LogController.writeChatLogFile(data);
//					System.out.println("Chat.parseChat: Chat data did not match a user or admin command, or event: " + line);
                }

            }
            // Check for commands waiting for data
            else if (commandQueue.size() > 0) {
                for (int i = 0; i < commandQueue.size(); i++) {
                    if (commandQueue.get(i).addData(line)) {
                        break;
                    }
                }
            } else {
//				System.out.println("CommandParse.parseCommand Not a chat message & no commands waiting: " + line);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "CommandParse.parseCommand - Error on line (" + line + ") error: " + ex);
        }
    }

    public synchronized void addCommandQueue(CommandInterface command) {
        commandQueue.add(command);
    }

    public synchronized void removeCommandQueue(CommandInterface command) {
        commandQueue.remove(command);
    }
}
