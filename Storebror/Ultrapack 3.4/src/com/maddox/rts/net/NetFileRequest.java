package com.maddox.rts.net;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.HUD;
import com.maddox.il2.net.NetFileServerNoseart;
import com.maddox.il2.net.NetFileServerPilot;
import com.maddox.il2.net.NetFileServerSkin;
import com.maddox.il2.net.NetUser;
import com.maddox.rts.Destroy;
import com.maddox.rts.HomePath;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetException;
import com.maddox.rts.NetObj;
import com.maddox.rts.Time;

public class NetFileRequest {

    public NetFileClient client() {
        debugLn("NetFileRequest client()=" + (this.client == null ? "null" : this.client.getClass().getName()));
        return this.client;
    }

    public NetFileServer server() {
        debugLn("NetFileRequest server()=" + (this.server == null ? "null" : this.server.getClass().getName()));
//		stackTrace(" server()=" + (server == null ? "null" : server.getClass().getName()));
        return this.server;
    }

    public Object serverData() {
        debugLn("NetFileRequest serverData()=" + (this.serverData == null ? "null" : this.serverData.getClass().getName()));
        return this.serverData;
    }

    public int prior() {
        debugLn("NetFileRequest prior()=" + this.prior);
        return this.prior;
    }

    public NetObj owner() {
        debugLn("NetFileRequest owner()=" + this.owner.idLocal());
        return this.owner;
    }

    public String ownerFileName() {
        debugLn("NetFileRequest ownerFileName()=" + this.ownerFileName);
        return this.ownerFileName;
    }

    public int state() {
        debugLn("NetFileRequest state()=" + this.state);
        return this.state;
    }

    public String localFileName() {
        debugLn("NetFileRequest localFileName()=" + this.localFileName);
        return this.localFileName;
    }

    public float complete() {
        debugLn("NetFileRequest complete()=" + this.complete);
        // Exception testException = new Exception("complete()=" + complete);
        // testException.printStackTrace();
        return this.complete;
    }

    public boolean isEnded() {
        debugLn("NetFileRequest isEnded()=" + (this.state != 1));
        return this.state != 1;
    }

    public String localFullFileName(String s, String s1) {
        debug("NetFileRequest localFullFileName(" + s + ", " + s1 + ")=");
        stackTrace("localFullFileName(" + s + ", " + s1 + ")");
        if (this.localFileName == null) {
            debugLn("null");
            return null;
        }
        if (this.ownerFileName.equals(this.localFileName)) if (s != null) {
            debugLn(s + "/" + this.localFileName);
            return s + "/" + this.localFileName;
        } else {
            debugLn(this.localFileName);
            return this.localFileName;
        }
        if (s1 != null) {
            debugLn(s1 + "/" + this.localFileName);
            return s1 + "/" + this.localFileName;
        } else {
            debugLn(this.localFileName);
            return this.localFileName;
        }
    }

    public void doRequest() {
        debugLn("NetFileRequest doRequest()");
        stackTrace("NetFileRequest doRequest()");
        if (this instanceof Destroy && ((Destroy) this).isDestroyed()) throw new NetException("NetFileRequest is destroyed");
        if (this.state() == 1) throw new NetException("Request in progress");
        if (this.client() == null) throw new NetException("Client == null");
        if (this.client() instanceof Destroy && ((Destroy) this.client()).isDestroyed()) throw new NetException("Client is destroyed");
        if (this.prior() < 0 || this.prior() > 255) throw new NetException("BAD parameter Proir");
        if (this.owner() == null) throw new NetException("Owner == null");
        if (this.owner() instanceof Destroy && this.owner().isDestroyed()) throw new NetException("Owner is destroyed");
        if (this.ownerFileName() == null || this.ownerFileName().length() == 0) throw new NetException("OwnerFileName is empty");
        if (HomePath.isFileSystemName(this.ownerFileName())) throw new NetException("Bad OwnerFileName");
        this.state = 2;
        this.localFileName = null;
        this.complete = 0.0F;
        if (this.server() instanceof Destroy && ((Destroy) this.server()).isDestroyed()) throw new NetException("Server is destroyed");
        if (!(this.server() instanceof NetObj)) throw new NetException("Server NOT NetObj");
        else {
            this.server().doRequest(this);
            if (this.fileType != "unknown") {
                StringBuffer logLine = new StringBuffer("D/L " + this.fileType + " (");
                if (this.ownerFileName() != null) logLine.append("owner filename=" + this.ownerFileName());
                if (this.ownerFileName() != null && this.localFileName() != null) logLine.append(", ");
                if (this.localFileName() != null) logLine.append("local filename=" + this.localFileName());
                logLine.append(") from " + this.userName + " starting!");
                System.out.println(logLine.toString());
                this.startTime = Time.current();
            }
            return;
        }
    }

    public void doCancel() {
        debugLn("NetFileRequest doCancel()");
        stackTrace("NetFileRequest doCancel()");
        if (this instanceof Destroy && ((Destroy) this).isDestroyed()) throw new NetException("NetFileRequest is destroyed");
        if (this.state() != 1) throw new NetException("Request NOT in progress");
        if (this.state == 2) return;
        if (this.server() instanceof Destroy && ((Destroy) this.server()).isDestroyed()) throw new NetException("Server is destroyed");
        else {
            this.server().doCancel(this);
            return;
        }
    }

    public void doAnswer() {
        debugLn("NetFileRequest doAnswer()");
        stackTrace("NetFileRequest doAnswer()");
        if (this.client() instanceof Destroy && ((Destroy) this.client()).isDestroyed()) return;
        else {
            this.client().netFileAnswer(this);
            return;
        }
    }

    public void setState(int i) {
        debugLn("NetFileRequest setState(" + i + ")");
        stackTrace("NetFileRequest setState(" + i + ")");
        this.state = i;
    }

    public void setLocalFileName(String s) {
        debugLn("NetFileRequest setLocalFileName(" + s + ")");
        stackTrace("NetFileRequest setLocalFileName(" + s + ")");
        this.localFileName = s;
    }

    public void setComplete(float f) {
        debugLn("NetFileRequest setComplete(" + f + ")");
        // Exception testException = new Exception("setComplete(" + f +")");
        // testException.printStackTrace();
        if (f > 0.0F) {
            if (f == 1.0F) {
                if (this.isTransferring) {
                    this.isTransferring = false;
                    this.hudLog(f);
                }
            } else {
                this.isTransferring = true;
                this.hudLog(f);
            }
        } else this.isTransferring = false;
        this.complete = f;
    }

    public String toString() {
        debug("NetFileRequest toString()=");
        // Exception testException = new Exception("toString()");
        // testException.printStackTrace();
        String s = null;
        switch (this.state) {
            case 2:
                s = "init";
                break;

            case 1:
                s = "progress " + (int) (100F * this.complete);
                break;

            case 0:
                s = "success";
                break;

            case -1:
                s = "owner disconnected";
                break;

            case -2:
                s = "not found";
                break;

            case -3:
                s = "io error";
                break;

            case -4:
                s = "canceled";
                break;

            default:
                s = "UNKNOWN";
                break;
        }
        if (this.localFileName() != null) {
            debugLn(this.owner() + ":" + this.ownerFileName() + " (" + this.localFileName() + ") state = " + s);
            return this.owner() + ":" + this.ownerFileName() + " (" + this.localFileName() + ") state = " + s;
        } else {
            debugLn(this.owner() + ":" + this.ownerFileName() + " state = " + s);
            return this.owner() + ":" + this.ownerFileName() + " state = " + s;
        }
    }

    public int hashCode() {
        debugLn("NetFileRequest hashCode()=" + this.owner.hashCode() + this.ownerFileName.hashCode());
        return this.owner.hashCode() + this.ownerFileName.hashCode();
    }

    public boolean equals(Object obj) {
        debug("NetFileRequest equals(" + (obj == null ? "null" : obj.getClass().getName()) + ")=");
        if (obj == this) {
            debugLn("true");
            return true;
        }
        if (obj == null) {
            debugLn("false");
            return false;
        }
        if (!(obj instanceof NetFileRequest)) {
            debugLn("false");
            return false;
        }
        NetFileRequest netfilerequest = (NetFileRequest) obj;
        if (this.serverData != null) {
            if (!this.serverData.equals(netfilerequest.serverData)) {
                debugLn("false");
                return false;
            }
        } else if (netfilerequest.serverData != null) {
            debugLn("false");
            return false;
        }
        debugLn("" + (netfilerequest.owner == this.owner && netfilerequest.ownerFileName.equals(this.ownerFileName)));
        return netfilerequest.owner == this.owner && netfilerequest.ownerFileName.equals(this.ownerFileName);
    }

    public NetFileRequest(NetFileClient netfileclient, NetFileServer netfileserver, int i, NetObj netobj, String s) {
        this(netfileclient, netfileserver, null, i, netobj, s);
    }

    public NetFileRequest(NetFileClient netfileclient, int i, NetObj netobj, String s) {
        this(netfileclient, null, null, i, netobj, s);
    }

    public NetFileRequest(NetFileClient netfileclient, NetFileServer netfileserver, Object obj, int i, NetObj netobj, String s) {
        debugLn("NetFileRequest(" + (netfileclient == null ? "null" : netfileclient.getClass().getName()) + ", " + (netfileserver == null ? "null" : netfileserver.getClass().getName()) + ", " + (obj == null ? "null" : obj.getClass().getName()) + ", " + i
                + ", " + (netobj == null ? "null" : netobj.getClass().getName()) + ", " + s + ")");
        stackTrace("NetFileRequest(" + (netfileclient == null ? "null" : netfileclient.getClass().getName()) + ", " + (netfileserver == null ? "null" : netfileserver.getClass().getName()) + ", " + (obj == null ? "null" : obj.getClass().getName()) + ", "
                + i + ", " + (netobj == null ? "null" : netobj.getClass().getName()) + ", " + s + ")");
        if (netfileclient instanceof NetUser) {
            NetUser netUser = (NetUser) netfileclient;
            debugLn("netfileclient fullName=" + netUser.fullName() + ", shortName=" + netUser.shortName() + ", uniqueName=" + netUser.uniqueName());
        }
        if (netobj instanceof NetUser) {
            NetUser netUser = (NetUser) netobj;
            debugLn("netobj fullName=" + netUser.fullName() + ", shortName=" + netUser.shortName() + ", uniqueName=" + netUser.uniqueName());
        }

        if (netfileclient instanceof NetUser) {
            this.userName = ((NetUser) netfileclient).shortName();
            if (netfileserver instanceof NetFileServerPilot) this.fileType = "Pilot Skin";
            else if (netfileserver instanceof NetFileServerSkin) this.fileType = "Aircraft Skin";
            else if (netfileserver instanceof NetFileServerNoseart) this.fileType = "Noseart";
        }
        this.state = 2;
        this.complete = 0.0F;
        this.client = netfileclient;
        this.server = netfileserver;
        this.serverData = obj;
        this.prior = i;
        this.owner = netobj;
        this.ownerFileName = s;
        if (this.server == null) this.server = NetEnv.cur().fileServerDef;
    }

    private void hudLog(float fComplete) {
        if (!Config.cur.bSkinDownloadNotifications) return;
        if (this.logID == -1) this.logID = HUD.makeIdLog();
        int iComplete = (int) (fComplete * 100F);
        if (iComplete - this.lastComplete < 2 && fComplete < 1.0F) return;
        this.lastComplete = iComplete;
        if (fComplete < 1.0F) HUD.log(this.logID, "D/L " + this.fileType + " from " + this.userName + " " + iComplete + "%", false);
        else {
            HUD.log(this.logID, "D/L " + this.fileType + " from " + this.userName + " finished!", false);
            if (this.fileType != "unknown" && this.startTime != 0L) {
                long timeRequired = Time.current() - this.startTime;
                StringBuffer logLine = new StringBuffer("D/L " + this.fileType + " (");
                if (this.ownerFileName() != null) logLine.append("owner filename=" + this.ownerFileName());
                if (this.ownerFileName() != null && this.localFileName() != null) logLine.append(", ");
                if (this.localFileName() != null) logLine.append("local filename=" + this.localFileName());
                logLine.append(") from " + this.userName + " finished, transfer took " + timeRequired + "ms!");
                System.out.println(logLine.toString());
            }
        }
    }

    private static void debug(String debugMessage) {
        if (!_DEBUG) return;
        System.out.print(debugMessage);
    }

    private static void debugLn(String debugMessage) {
        if (!_DEBUG) return;
        System.out.println(debugMessage);
    }

    private static void stackTrace(String traceMessage) {
        if (!_DEBUG_STACK_TRACE) return;
        Exception exception = new Exception(traceMessage);
        exception.printStackTrace();
    }

//	static {
//		NetFileTransport.MSG_DEFAULT_SIZE = 260;
//		NetFileTransport.MSG_DEFAULT_COUNT = 32;
//	}

    public static final boolean _DEBUG                     = false;
    public static final boolean _DEBUG_STACK_TRACE         = false;
    private String              fileType                   = "unknown";
    private String              userName                   = "unknown";
    private boolean             isTransferring             = false;
    private int                 logID                      = -1;
    private int                 lastComplete               = -1;
    // private static DecimalFormat twoPlaces = new DecimalFormat("#.##");
    private long                startTime                  = 0L;

    public static final int     STATE_INIT                 = 2;
    public static final int     STATE_PROGRESS             = 1;
    public static final int     STATE_SUCCESS              = 0;
    public static final int     STATE_ERR_OWNER_DISCONNECT = -1;
    public static final int     STATE_ERR_NOT_FOUND        = -2;
    public static final int     STATE_ERR_IO               = -3;
    public static final int     STATE_USER_CANCEL          = -4;
    public static final int     PRIOR_MIN                  = 0;
    public static final int     PRIOR_MAX                  = 255;
    private NetFileClient       client;
    private NetFileServer       server;
    protected Object            serverData;
    private int                 prior;
    private NetObj              owner;
    protected String            ownerFileName;
    private int                 state;
    private String              localFileName;
    private float               complete;
    public Object               _serverIn;
    public Object               _serverOut;
}
