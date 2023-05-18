package mainController;

import java.util.ArrayList;
import java.util.HashMap;

import model.BadLanguage;
import utility.Time;

public class BadLanguageController implements Runnable {

    String                  chatLine;
    String                  playerName;
    HashMap<String, String> badWordList;

    public BadLanguageController(String playerName, String chatLine, HashMap<String, String> badWordList) {
        this.playerName = playerName;
        this.chatLine = chatLine;
        this.badWordList = badWordList;
    }

    public static BadLanguage updateBadLanguageList(BadLanguage currentBadLanguage) {
        long lastDBUpdateTime = 0;
        HashMap<String, String> badWordList = null;
        try {
            if (MainController.isStatsOn()) {
                lastDBUpdateTime = MySQLConnectionController.getLastLanguageUpdate();
                MainController.writeDebugLogFile(2, "BadLanguageController.updateBadLanguageList - DB updated (" + lastDBUpdateTime + ") last list update (" + currentBadLanguage.getLastUpdateTime() + ")");
                if (lastDBUpdateTime != 0 && lastDBUpdateTime > currentBadLanguage.getLastUpdateTime()) {
                    badWordList = MySQLConnectionController.getBadWordList();
                    currentBadLanguage.setBadWordList(badWordList);
                } else {
                    MainController.writeDebugLogFile(2, "BadLanguageController.updateBadLanguageList - No updates, using current list");
                    badWordList = currentBadLanguage.getBadWordList();
                }
            }
            if (badWordList == null) {
                badWordList = loadBadLanguageList(currentBadLanguage.getFileName());
            }
            if (lastDBUpdateTime == 0) {
                lastDBUpdateTime = Time.getTime();
            }
            if (badWordList == null) {
                badWordList = new HashMap<String, String>();
                MainController.writeDebugLogFile(1, "BadLanguageController.updateBadLanguageList - Error Failed to load any bad words");
            }
            currentBadLanguage.setBadWordList(badWordList);
            currentBadLanguage.setLastUpdateTime(lastDBUpdateTime);
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "BadLanguageController.updateBadLanguageList - Error Unhandled Exception: " + ex);
        }
        return currentBadLanguage;
    }

    static HashMap<String, String> loadBadLanguageList(String badLanguageFileName) {

        HashMap<String, String> badWordList = new HashMap<String, String>();
        try {
            ArrayList<String> badLanguageFile = FileController.fileRead(MainController.CONFIG.getConfigDirectory() + badLanguageFileName);

            // Loop through all lines of the mission file
            for (String line : badLanguageFile) {
                line = line.trim();
                if (!line.startsWith("//")) {
                    line = line.toUpperCase();
                    badWordList.put(line, line);
                }
            }
            if (badWordList.size() > 0) {
                MainController.writeDebugLogFile(1, "BadLanguageController.loadBadLanguageList - Loaded ( " + badWordList.size() + " ) words from file");
            } else {
                badWordList = null;
            }
            return badWordList;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "BadLanguageController.loadBadLanguageList - Unhandled Exception loading file: " + ex);
            return badWordList;
        }
    }

    static boolean isBadWord(HashMap<String, String> badWordList, String word) {
        boolean isBadWord = false;
        if (badWordList.containsKey(word.toUpperCase())) {
            isBadWord = true;
        }
        return isBadWord;
    }

    public void run() {
        try {
            boolean badWordInChat = false;
            String[] charLineParsed = chatLine.split(" ");
            for (String word : charLineParsed) {
                if (isBadWord(this.badWordList, word)) {
                    badWordInChat = true;
                    break;
                }
            }
            if (badWordInChat) {
                PilotController.badLanguageCheck(playerName);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "BadLanguageController.run - Error Unhandled Exception checking Chat for player (" + playerName + ") Chat(" + chatLine + "): " + ex);
        }
    }
}
