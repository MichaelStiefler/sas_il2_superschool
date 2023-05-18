package model;

public class GUICommand {

    public enum GUICommandType {
        // Mission Commands
        ADJTIMELEFT, ADJCOUNTOBJECTIVE, ADJTARGETOBJECTIVE, ADJPLANELIMIT, ADJREDCNTOBJNEEDED, ADJBLUECNTOBJNEEDED, ADJREDTGTOBJNEEDED, ADJBLUETGTOBJNEEDED,
        // Pilot Commands
        CHAT, KICKPILOT, ADDBAN, ADJBAN, REMOVEBAN, KICKPILOTMARKINGS, KICKPILOTLANGUAGE,
        // Admin Commands
        ADDADMIN, ADJADMIN, REMOVEADMIN,
        // ReservedName Commands
        ADDRESERVEDNAME, ADJRESERVEDNAME, REMOVERESERVEDNAME,
        // Mission commands
        RUNTEMPMISSION
    }

    private GUICommandType guiCommand;
    private int            army;
    private String         name;
    private String         password;
    private String         IpAddress;
    private long           duration;
    private Object         changeObject;
    private Object         value;

    public GUICommand(GUICommandType command) {
        this.guiCommand = command;
    }

    public GUICommandType getGuiCommand() {
        return guiCommand;
    }

    public void setGuiCommand(GUICommandType guiCommand) {
        this.guiCommand = guiCommand;
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Object getChangeObject() {
        return changeObject;
    }

    public void setChangeObject(Object changeObject) {
        this.changeObject = changeObject;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
