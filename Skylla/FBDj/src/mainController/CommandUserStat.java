package mainController;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Pilot;
import model.PilotSortie;
import utility.Time;

public class CommandUserStat implements CommandInterface {
    public enum UserStatActionType {
        KICK, NEWSORTIE, SORTIEEND, UPDATEDATA, GUNSTAT
    }

    Pilot              pilot;
    boolean            pilotMatch;
    int                lineCounter;
    int                attempts       = 0;
    Pattern            patternUserStat;
    Pattern            patternWaitFor = Pattern.compile("^\\<consoleN\\>\\<\\d+\\>");
    Matcher            m;
    ArrayList<String>  commandDataReceived;
    long               startTime;
    UserStatActionType userStatAction;
    int                retryTime      = 0;
    int                commandLength  = 28;

    public CommandUserStat(Pilot pilot, UserStatActionType userStatAction, int attempts, int retryTime) {
        this.pilot = pilot;
        pilotMatch = false;
        lineCounter = 1;
        patternUserStat = Pattern.compile("(?:\\\\t)+(.+)\\\\n");
        commandDataReceived = new ArrayList<String>();
        startTime = Time.getTime();
        this.userStatAction = userStatAction;
        this.attempts = attempts;
        this.retryTime = retryTime;
        if (MainController.CONFIG.getServerVersion().equalsIgnoreCase("409m")) {
            this.commandLength = 26;
        }
        initialize();

    }

    private void initialize() {
        MainController.COMMANDPARSE.addCommandQueue(this);
        ServerCommandController.serverCommandSend("user \"" + pilot.getAsciiTextName() + "\" STAT");
    }

    public boolean addData(String line) {
        try {
            if ((m = patternUserStat.matcher(line)).find()) {
                if (!pilotMatch) {
                    if (pilot.getAsciiTextName().trim().equals(m.group(1).trim())) {
                        pilotMatch = true;
                        lineCounter++;
                        commandDataReceived.add(line);
                        return true;
                    }
                } else if (pilotMatch && lineCounter < commandLength + 1) {
                    commandDataReceived.add(line);
                    lineCounter++;
                    return true;
                }
            }

            if ((m = patternWaitFor.matcher(line)).find()) {
                if (commandDataReceived.size() > commandLength - 1) {
                    MainController.COMMANDPARSE.removeCommandQueue(this);
                    parseData();
                    return true;
                }
            }

            if (Time.getTimeDuration(startTime) > retryTime) {
                MainController.writeDebugLogFile(1, "CommandUserStat.addData - Timed-out waiting for a response for (" + pilot.getName() + ")");
                MainController.COMMANDPARSE.removeCommandQueue(this);
                // The user STAT command timed out, lets try twice more unless its for a Pilot Kick or Pilot has
                // disconnected

                if ((userStatAction != UserStatActionType.KICK) && (attempts < 2) && (MainController.PILOTS.get(pilot.getName()) != null)) {
                    attempts++;
                    new CommandUserStat(pilot, userStatAction, attempts, retryTime);
                } else {
                    finishSortie();
                }
            }
            return false;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "CommandUserStat.addData - Error Unhandled Exception with line (" + line + "): " + ex);
            // Added to help with a problem of repeating exceptions whenever a line is sent to here.
            MainController.COMMANDPARSE.removeCommandQueue(this);
            return false;
        }
    }

    private void parseData() {
        String[] userStatData = new String[commandLength];
        try {
            MainController.writeDebugLogFile(2, "CommandUserStat.parseData - Data Received for (" + pilot.getName() + ")");

            int x = 0;

            for (int i = 0; i < commandDataReceived.size(); i++) {
                Matcher m = patternUserStat.matcher(commandDataReceived.get(i));
                if (m.find()) {
                    userStatData[x] = m.group(1).trim();
                    x++;
                }
            }

            // Set the relevant Info in the Pilot Record
            pilot.setScore(Integer.parseInt(userStatData[1]));
            pilot.setEAir(Integer.parseInt(userStatData[3]));

            // This data is calculated through the eventLog now so it's here only to show the other into
            // pilot.setESAir(Integer.parseInt(userStatData[4]));
            // pilot.setETank(Integer.parseInt(userStatData[5]));
            // pilot.setECar(Integer.parseInt(userStatData[6]));
            // pilot.setEArt(Integer.parseInt(userStatData[7]));
            // pilot.setEAaa(Integer.parseInt(userStatData[8]));
            // pilot.setEWag(Integer.parseInt(userStatData[9]));
            // pilot.setEShip(Integer.parseInt(userStatData[10]));
            // pilot.setERadio(Integer.parseInt(userStatData[11]));
            // pilot.setFAir(Integer.parseInt(userStatData[12]));
            // pilot.setFSAir(Integer.parseInt(userStatData[13]));
            // pilot.setFTank(Integer.parseInt(userStatData[14]));
            // pilot.setFCar(Integer.parseInt(userStatData[15]));
            // pilot.setFArt(Integer.parseInt(userStatData[16]));
            // pilot.setFAaa(Integer.parseInt(userStatData[17]));
            // pilot.setFWag(Integer.parseInt(userStatData[18]));
            // pilot.setFShip(Integer.parseInt(userStatData[19]));
            // pilot.setFRadio(Integer.parseInt(userStatData[20]));
            pilot.setFiBull(Integer.parseInt(userStatData[commandLength - 7]));
            pilot.setHiBull(Integer.parseInt(userStatData[commandLength - 6]));
            pilot.setHiABull(Integer.parseInt(userStatData[commandLength - 5]));
            pilot.setFiRock(Integer.parseInt(userStatData[commandLength - 4]));
            pilot.setHiRock(Integer.parseInt(userStatData[commandLength - 3]));
            pilot.setFiBomb(Integer.parseInt(userStatData[commandLength - 2]));
            pilot.setHiBomb(Integer.parseInt(userStatData[commandLength - 1]));

        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "CommandUserStat.parseData - Error on pilot (" + pilot.getName() + ") " + ex);
            try {
                for (String line : userStatData) {
                    MainController.writeDebugLogFile(1, "** User Data(" + line + ")");
                }
            } catch (Exception e) {}

        }

        finishSortie();

    }

    private void finishSortie() {
        PilotSortie pilotSortie = null;
        try {
            switch (userStatAction) {
                case KICK:

                    // Pilot is being kicked so close out the sortie and kick them
                    pilotSortie = MainController.ENDINGSORTIES.get(pilot.getName());
                    if (pilotSortie != null) {
                        MySQLConnectionController.writeSortie(pilot.getName(), pilotSortie);
                        MainController.ENDINGSORTIES.remove(pilot.getName());
                    }
                    // Added this tidbit because sometimes the pilot could force themselves back onto the server
                    // and not have a socket number to destroy so also issues a kick command.
                    if (pilot.getSocket() > 0) {
                        ServerCommandController.serverCommandSend("channel " + pilot.getSocket() + " DESTROY");
                    } else {
                        ServerCommandController.serverCommandSend("kick \"" + pilot.getAsciiTextName() + "\"");
                    }
                    MainController.writeDebugLogFile(1, "CommandUserStat.parseData: " + pilot.getName() + " has been kicked!");
                    PilotController.pilotRemove(pilot.getName());
                    break;
                case SORTIEEND:
                    try {
                        // Normal Sortie End processing
                        pilotSortie = MainController.ENDINGSORTIES.get(pilot.getName());
                        if (pilotSortie != null) {
                            MySQLConnectionController.writeSortie(pilot.getName(), pilotSortie);
                            MainController.ENDINGSORTIES.remove(pilot.getName());
                            // Get the new sortie for the Pilot and set the starting values
                            PilotSortie newSortie = null;
                            newSortie = MainController.SORTIES.get(pilot.getName());
                            if (newSortie == null) {
                                PilotSortieController.newSortie(pilot.getName());
                                newSortie = MainController.SORTIES.get(pilot.getName());
                            }
                            newSortie.setScore(pilot.getScore());
                            newSortie.setEAir(pilot.getEAir());
                            newSortie.setFiBull(pilot.getFiBull());
                            newSortie.setHiBull(pilot.getHiBull());
                            newSortie.setHiABull(pilot.getHiABull());
                            newSortie.setFiRock(pilot.getFiRock());
                            newSortie.setHiRock(pilot.getHiRock());
                            newSortie.setFiBomb(pilot.getFiBomb());
                            newSortie.setHiBomb(pilot.getHiBomb());
                        } else {
                            MainController.writeDebugLogFile(1, "CommandUserStat.parseData(SORTIEEND) - No Sortie found in the EndingSorties list for pilot(" + pilot.getName() + ")");
                        }
                    } catch (Exception ex) {
                        MainController.writeDebugLogFile(1, "CommandUserStat.parseData(SORTIEEND) - Error Unhandled exception for pilot(" + pilot.getName() + "): " + ex);
                    }

                    break;
                case GUNSTAT:
                    PilotController.pilotGunStat(pilot);
                    break;
                case NEWSORTIE:
                    PilotSortie newSortie = null;
                    newSortie = MainController.SORTIES.get(pilot.getName());
                    if (newSortie == null) {
                        PilotSortieController.newSortie(pilot.getName());
                        newSortie = MainController.SORTIES.get(pilot.getName());
                    }
                    newSortie.setScore(pilot.getScore());
                    newSortie.setEAir(pilot.getEAir());
                    newSortie.setFiBull(pilot.getFiBull());
                    newSortie.setHiBull(pilot.getHiBull());
                    newSortie.setHiABull(pilot.getHiABull());
                    newSortie.setFiRock(pilot.getFiRock());
                    newSortie.setHiRock(pilot.getHiRock());
                    newSortie.setFiBomb(pilot.getFiBomb());
                    break;
                case UPDATEDATA:
                    break;
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "CommandUserStat.parseData - Error Unhandled Exception on pilot (" + pilot.getName() + "): " + ex);
        }

    }
}
