package model;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

// Stores admin information for players and remote GUIs

public class Admin implements Serializable {
    private static final long serialVersionUID   = 1L;

    // Login
    private String            name;
    private String            adminHost          = "";
    private String            password;
    private boolean           gui                = false;

    // Remote gui security
    private boolean           allowed            = true;
    private int               badLogins          = 0;

    // Command access flags
    private boolean           login              = true;
    private boolean           passwordChange     = true;
    private boolean           kick               = true;
    private boolean           banAdd             = false;
    private boolean           banRemove          = false;
    private boolean           reservedNameAdd    = true;
    private boolean           reservedNameRemove = true;
    private boolean           missionExtend      = false;
    private boolean           missionRestart     = false;
    private boolean           loadMission        = false;
    private boolean           pingKick           = false;
    private boolean           console            = false;
    private boolean           fbdjStop           = false;
    private boolean           fbdjRestart        = false;
    //------------------------------------
    //TODO: skylla: Admin-Veto:
    private boolean			  veto				 = false;
    //------------------------------------

    public Admin(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isPasswordChange() {
        return passwordChange;
    }

    public void setPasswordChange(boolean passwordChange) {
        this.passwordChange = passwordChange;
    }

    public boolean isKick() {
        return kick;
    }

    public void setKick(boolean kick) {
        this.kick = kick;
    }

    public boolean isBanAdd() {
        return banAdd;
    }

    public void setBanAdd(boolean banAdd) {
        this.banAdd = banAdd;
    }

    public boolean isBanRemove() {
        return banRemove;
    }

    public void setBanRemove(boolean banRemove) {
        this.banRemove = banRemove;
    }

    public boolean isMissionExtend() {
        return missionExtend;
    }

    public void setMissionExtend(boolean missionExtend) {
        this.missionExtend = missionExtend;
        //------------------------------------
        //TODO: skylla: Admin-Veto
        setVeto();
        //------------------------------------
    }
    
    //------------------------------------
    //TODO: skylla: Admin-Veto:
    
    public void setVeto() {
    	this.veto = missionExtend;
    }
    
    public boolean isVeto() {
    	return this.veto;
    }
    //------------------------------------
    
    public boolean isMissionRestart() {
        return missionRestart;
    }

    public void setMissionRestart(boolean missionRestart) {
        this.missionRestart = missionRestart;
    }

    public boolean isPingKick() {
        return pingKick;
    }

    public void setPingKick(boolean pingKick) {
        this.pingKick = pingKick;
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public boolean isFBDjStop() {
        return fbdjStop;
    }

    public boolean isGui() {
        return gui;
    }

    public void setGui(boolean gui) {
        this.gui = gui;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public int getBadLogins() {
        return badLogins;
    }

    public void setBadLogins(int badLogins) {
        this.badLogins = badLogins;
    }

    public boolean isLoadMission() {
        return loadMission;
    }

    public void setLoadMission(boolean loadMission) {
        this.loadMission = loadMission;
    }

    public boolean isFbdjStop() {
        return fbdjStop;
    }

    public void setFbdjStop(boolean fbdjStop) {
        this.fbdjStop = fbdjStop;
    }

    public boolean isFbdjRestart() {
        return fbdjRestart;
    }

    public void setFbdjRestart(boolean fbdjRestart) {
        this.fbdjRestart = fbdjRestart;
    }

    public boolean isReservedNameAdd() {
        return reservedNameAdd;
    }

    public void setReservedNameAdd(boolean reservedNameAdd) {
        this.reservedNameAdd = reservedNameAdd;
    }

    public boolean isReservedNameRemove() {
        return reservedNameRemove;
    }

    public void setReservedNameRemove(boolean reservedNameRemove) {
        this.reservedNameRemove = reservedNameRemove;
    }

    /**
     * 
     * @param ipAddress
     *            The ip address used by the admin
     * @return True if admin host name matches with supplied ip address, otherwise returns false
     * @throws UnknownHostException
     */
    public boolean isAdminHost(String ipAddress) {

        InetAddress inetAddress = null;

        try {
            inetAddress = InetAddress.getByName(this.getAdminHost());

        } catch (UnknownHostException e) {
            return false; // Unknow host returns false
        }

        return inetAddress.getHostAddress().equals(ipAddress);

    }

    /**
     * @param adminHost
     *            the adminHost to set
     */
    public void setAdminHost(String adminHost) {
        this.adminHost = adminHost;
    }

    /**
     * @return the adminHost
     */
    public String getAdminHost() {
        return adminHost;
    }

}