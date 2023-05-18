package model;

public class Pilot extends PilotBlueprint {
    boolean         validConnection        = false;
    boolean         admin;
    private int     adminLoginCheck        = 0;
    boolean         reservedName;
    private int     reservedNameLoginCheck = 0;
    String          connectionComment      = "OK";
    String          disconnectComment      = "Normal";
    private boolean missionVote            = false;

    public Pilot(String name, String ipAddress, int socket, long time) {
        this.name = name;
        this.connectTime = time;
        this.ipAddress = ipAddress;
        this.socket = socket;
        this.asciiTextName = name;
    }

    public boolean isMissionVote() {
        return missionVote;
    }

    public void setMissionVote(boolean missionVote) {
        this.missionVote = missionVote;
    }

    public String getDisconnectComment() {
        return disconnectComment;
    }

    public void setDisconnectComment(String disconnectComment) {
        this.disconnectComment = disconnectComment;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isReservedName() {
        return reservedName;
    }

    public void setReservedName(boolean reservedName) {
        this.reservedName = reservedName;
    }

    public int getAdminLoginCheck() {
        return adminLoginCheck;
    }

    public void setAdminLoginCheck(int adminLoginCheck) {
        this.adminLoginCheck = adminLoginCheck;
    }

    public int getReservedNameLoginCheck() {
        return reservedNameLoginCheck;
    }

    public void setReservedNameLoginCheck(int reservedNameLoginCheck) {
        this.reservedNameLoginCheck = reservedNameLoginCheck;
    }

    public boolean isValidConnection() {
        return validConnection;
    }

    public void setValidConnection(boolean validConnection) {
        this.validConnection = validConnection;
    }

    public String getConnectionComment() {
        return connectionComment;
    }

    public void setConnectionComment(String connectionComment) {
        this.connectionComment = connectionComment;
    }
}
