package com.maddox.rts.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.maddox.rts.Compress;
import com.maddox.rts.Destroy;
import com.maddox.rts.Finger;
import com.maddox.rts.HomePath;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.util.HashMapExt;

public class NetFileServerDef extends NetObj implements NetFileServer {
    public class Out implements Destroy {

        public boolean openFile(String s) {
            if (f != null)
                return true;
            try {
                f = new FileInputStream(HomePath.toFileSystemName(s, 0));
                if (ptr > 0)
                    f.skip(ptr);
            } catch (Exception exception) {
                NetFileServerDef.printDebug(exception);
                f = null;
                return false;
            }
            if (compressMethod() != 0)
                buf = new byte[compressBlockSize()];
            return true;
        }

        public boolean isDestroyed() {
            return state == -1;
        }

        public void destroy() {
            if (isDestroyed())
                return;
            state = -1;
            if (f != null) {
                try {
                    f.close();
                } catch (Exception exception) {
                    NetFileServerDef.printDebug(exception);
                }
                f = null;
            }
        }

        public String toString() {
            switch (state) {
                case 0: // '\0'
                    return "state Init";

                case 1: // '\001'
                    return "state WaitCommand";

                case 2: // '\002'
                    return "state Send Finger";

                case 3: // '\003'
                    return "state Send Header";

                case 4: // '\004'
                    return "state Transfer";

                case -1:
                    return "state Destroy";
            }
            return "state UNKNOWN";
        }

        public static final int ST_INIT         = 0;
        public static final int ST_WAIT_COMMAND = 1;
        public static final int ST_SEND_FINGER  = 2;
        public static final int ST_SEND_HEADER  = 3;
        public static final int ST_SEND_DATA    = 4;
        public static final int ST_DESTROY      = -1;
        public int              state;
        public int              size;
        public int              shortSize;
        public long             finger;
        public long             shortFinger;
        public int              ptr;
        public byte             buf[];
        public int              bufSize;
        public int              bufCur;
        public FileInputStream  f;

        public Out() {
            state = 0;
        }
    }

    public class In implements Destroy {

        public boolean createFile() {
            try {
                if (localSize > 0)
                    f = new FileOutputStream(HomePath.toFileSystemName(fileName, 0), true);
                else
                    f = new FileOutputStream(HomePath.toFileSystemName(fileName, 0));
            } catch (Exception exception) {
                NetFileServerDef.printDebug(exception);
                return false;
            }
            if (compressMethod() != 0)
                buf = new byte[compressBlockSize()];
            return true;
        }

        public boolean isDestroyed() {
            return state == -1;
        }

        public void destroy() {
            if (isDestroyed())
                return;
            state = -1;
            if (f != null) {
                try {
                    f.close();
                } catch (Exception exception) {
                    NetFileServerDef.printDebug(exception);
                }
                f = null;
            }
        }

        public String toString() {
            String s;
            if (fileName != null)
                s = fileName + " ";
            else
                s = "";
            switch (state) {
                case 0: // '\0'
                    return s + "state Init";

                case 1: // '\001'
                    return s + "state Request Finger";

                case 2: // '\002'
                    return s + "state Answer Finger";

                case 3: // '\003'
                    return s + "state Request File";

                case 4: // '\004'
                    return s + "state Transfer";
            }
            return s + "state UNKNOWN";
        }

        public static final int ST_INIT           = 0;
        public static final int ST_REQUEST_FINGER = 1;
        public static final int ST_ANSWER_FINGER  = 2;
        public static final int ST_REQUEST_FILE   = 3;
        public static final int ST_TRANSFER       = 4;
        public static final int ST_DESTROY        = -1;
        public int              state;
        public String           fileName;
        public String           localFileName;
        public int              size;
        public int              localSize;
        public long             finger;
        public long             localFinger;
        public byte             buf[];
        public int              bufSize;
        public int              bufFill;
        public FileOutputStream f;

        public In() {
            state = 0;
        }
    }

    public class NetFile {

        public int hashCode() {
            return owner.hashCode() + ownerFileName.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (obj == null)
                return false;
            if (!(obj instanceof NetFile)) {
                return false;
            } else {
                NetFile netfile = (NetFile) obj;
                return netfile.owner == owner && netfile.ownerFileName.equals(ownerFileName);
            }
        }

        public NetObj owner;
        public String ownerFileName;
        public long   finger;
        public String localFileName;

        public NetFile(NetObj netobj, String s) {
            this(netobj, s, 0L, null);
        }

        public NetFile(NetObj netobj, String s, long l, String s1) {
            owner = netobj;
            ownerFileName = s;
            finger = l;
            localFileName = s1;
        }
    }

    public int compressMethod() {
        return 2;
    }

    public int compressBlockSize() {
//        return 32768;
        return 1048576;
    }

    public String primaryPath() {
        return null;
    }

    public String alternativePath() {
        return null;
    }

    protected byte[] commonBuf(int i) {
        if (commonBuf == null || commonBuf.length < i)
            commonBuf = new byte[i];
        return commonBuf;
    }

    public void destroyIn(NetFileRequest netfilerequest) {
        NetFileRequest netfilerequest1 = netfilerequest;
        if (netfilerequest1._serverIn == null || !(netfilerequest1._serverIn instanceof In)) {
            return;
        } else {
            ((In) netfilerequest1._serverIn).destroy();
            return;
        }
    }

    public void destroyOut(NetFileRequest netfilerequest) {
        NetFileRequest netfilerequest1 = netfilerequest;
        if (netfilerequest1._serverOut == null || !(netfilerequest1._serverIn instanceof Out)) {
            return;
        } else {
            ((Out) netfilerequest1._serverOut).destroy();
            return;
        }
    }

    public String getStateInInfo(NetFileRequest netfilerequest) {
        NetFileRequest netfilerequest1 = netfilerequest;
        if (netfilerequest1._serverIn == null || !(netfilerequest1._serverIn instanceof In))
            return null;
        else
            return ((In) netfilerequest1._serverIn).toString();
    }

    public String getStateOutInfo(NetFileRequest netfilerequest) {
        NetFileRequest netfilerequest1 = netfilerequest;
        if (netfilerequest1._serverOut == null || !(netfilerequest1._serverOut instanceof Out))
            return null;
        else
            return ((Out) netfilerequest1._serverOut).toString();
    }

    public boolean isStateDataTransfer(NetFileRequest netfilerequest) {
        NetFileRequest netfilerequest1 = netfilerequest;
        if (netfilerequest1._serverOut == null || !(netfilerequest1._serverOut instanceof Out)) {
            return false;
        } else {
            Out out = (Out) netfilerequest1._serverOut;
            return out.state == 3 || out.state == 4;
        }
    }

    public void doRequest(NetFileRequest netfilerequest) {
        NetFileRequest netfilerequest1 = netfilerequest;
        NetFile netfile = new NetFile(netfilerequest1.owner(), netfilerequest1.ownerFileName());
        NetFile netfile1 = (NetFile) fileCache.get(netfile);
        if (netfile1 != null) {
            netfilerequest1.setComplete(1.0F);
            netfilerequest1.setLocalFileName(netfile1.localFileName);
            netfilerequest1.setState(0);
            initServerOutData(netfilerequest1);
            netfilerequest1.doAnswer();
            return;
        }
        if (netfilerequest1.owner().isMaster()) {
            String s = filePrimaryName(netfilerequest1.ownerFileName());
            if (isFileExist(s)) {
                makeRequestComplete(netfilerequest1, netfilerequest1.ownerFileName(), Finger.file(0L, s, -1));
                netfilerequest1.doAnswer();
            } else {
                netfilerequest1.setState(-2);
                netfilerequest1.doAnswer();
            }
        } else if (netfilerequest1.owner().masterChannel() instanceof NetChannelInStream) {
            netfilerequest1.setState(-2);
            netfilerequest1.doAnswer();
        } else {
            netfilerequest1.setState(1);
            netfilerequest1._serverIn = new In();
            NetEnv.cur().fileTransport.doRequest(netfilerequest1);
        }
    }

    protected void makeRequestComplete(NetFileRequest netfilerequest, String s, long l) {
        netfilerequest.setLocalFileName(s);
        NetFile netfile = new NetFile(netfilerequest.owner(), netfilerequest.ownerFileName(), l, netfilerequest.localFileName());
        fileCache.put(netfile, netfile);
        netfilerequest.setComplete(1.0F);
        netfilerequest.setState(0);
        initServerOutData(netfilerequest);
    }

    public void doCancel(NetFileRequest netfilerequest) {
        NetFileRequest netfilerequest1 = netfilerequest;
        if (netfilerequest1.isEnded())
            return;
        netfilerequest1.setState(-4);
        netfilerequest1.doAnswer();
        if (netfilerequest1.owner().isDestroyed() || netfilerequest1.owner().isMaster()) {
            return;
        } else {
            NetEnv.cur().fileTransport.doCancel(netfilerequest1);
            return;
        }
    }

    public void doAnswer(NetFileRequest netfilerequest) {
        initServerOutData(netfilerequest);
        netfilerequest.doAnswer();
    }

    private void initServerOutData(NetFileRequest netfilerequest) {
        if (netfilerequest.state() != 0)
            return;
        if (netfilerequest.client() == null)
            return;
        if (!(netfilerequest.client() instanceof NetFileTransport.Answer))
            return;
        if ((netfilerequest.client() instanceof Destroy) && ((Destroy) netfilerequest.client()).isDestroyed()) {
            return;
        } else {
            netfilerequest._serverOut = new Out();
            return;
        }
    }

    protected In netIn(NetFileRequest netfilerequest) {
        if (netfilerequest._serverIn == null)
            return null;
        else
            return (In) netfilerequest._serverIn;
    }

    protected Out netOut(NetFileRequest netfilerequest) {
        if (netfilerequest._serverOut == null)
            return null;
        else
            return (Out) netfilerequest._serverOut;
    }

    public void getRequestData(NetFileRequest netfilerequest, NetMsgInput netmsginput) throws IOException {
        NetFileRequest netfilerequest1 = netfilerequest;
        Out out = netOut(netfilerequest1);
        if (out == null)
            return;
        if (out.state != 1)
            return;
        int i = netmsginput.readInt();
        if (netmsginput.available() == 0) {
            out.ptr = i;
            if (compressMethod() != 0)
                out.state = 3;
            else
                out.state = 4;
        } else {
            netmsginput.readLong();
            out.shortSize = i;
            out.shortFinger = Finger.file(0L, netfilerequest1.localFullFileName(primaryPath(), alternativePath()), i);
            out.state = 2;
        }
    }

    public int getAnswerState(NetFileRequest netfilerequest, int i) {
        NetFileRequest netfilerequest1 = netfilerequest;
        Out out = netOut(netfilerequest1);
        if (out == null)
            return 0;
        switch (out.state) {
            case -1:
            case 1: // '\001'
            default:
                return 0;

            case 0: // '\0'
                out.size = fileLength(netfilerequest1.localFullFileName(primaryPath(), alternativePath()));
                if (out.size == 0) {
                    return 4;
                } else {
                    out.finger = Finger.file(0L, netfilerequest1.localFullFileName(primaryPath(), alternativePath()), -1);
                    return 2;
                }

            case 2: // '\002'
                return 2;

            case 3: // '\003'
                if (out.bufSize > 0)
                    out.ptr += out.buf.length;
                if (out.ptr >= out.size) {
                    out.ptr = out.size;
                    return 3;
                }
                if (!out.openFile(netfilerequest1.localFullFileName(primaryPath(), alternativePath())))
                    return 4;
                try {
                    int j = out.buf.length;
                    int k = out.size - out.ptr;
                    if (j > k)
                        j = k;
                    out.f.read(out.buf, 0, j);
                    out.bufSize = Compress.code(compressMethod(), out.buf, j);
//                    System.out.println("NetFileServerDef getAnswerState(" + netfilerequest.ownerFileName + ", " + i + ") bufSize=" + out.bufSize);
                    out.bufCur = 0;
                } catch (Exception exception) {
                    printDebug(exception);
                    return 4;
                }
                return 2;

            case 4: // '\004'
                break;
        }
        if (out.size == out.ptr)
            return 3;
        if (!out.openFile(netfilerequest1.localFullFileName(primaryPath(), alternativePath())))
            return 4;
        if (compressMethod() == 0) {
            out.bufSize = i;
            if (out.bufSize > out.size - out.ptr)
                out.bufSize = out.size - out.ptr;
            try {
                out.f.read(commonBuf(out.bufSize), 0, out.bufSize);
                out.ptr += out.bufSize;
            } catch (Exception exception1) {
                printDebug(exception1);
                return 4;
            }
        }
        return 1;
    }

    public boolean sendAnswerData(NetFileRequest netfilerequest, NetMsgGuaranted netmsgguaranted, int i, int j) throws IOException {
        NetFileRequest netfilerequest1 = netfilerequest;
//        netfilerequest1.owner().masterChannel().
        Out out = netOut(netfilerequest1);
        if (out == null)
            return false;
        switch (out.state) {
            case -1:
            case 1: // '\001'
            default:
                return false;

            case 0: // '\0'
                netmsgguaranted.writeInt(j);
                netmsgguaranted.writeInt(out.size);
                netmsgguaranted.writeLong(out.finger);
                out.state = 1;
                return true;

            case 2: // '\002'
                netmsgguaranted.writeInt(j);
                netmsgguaranted.writeInt(out.shortSize);
                netmsgguaranted.writeLong(out.shortFinger);
                out.state = 1;
                return true;

            case 3: // '\003'
                netmsgguaranted.writeInt(j);
                netmsgguaranted.writeInt(out.bufSize);
                out.state = 4;
                return true;

            case 4: // '\004'
                netmsgguaranted.writeInt(j);
                break;
        }
        if (compressMethod() != 0) {
            int k = i;
            if (k > out.bufSize - out.bufCur)
                k = out.bufSize - out.bufCur;
//            System.out.println("NetFileServerDef sendAnswerData(" + netfilerequest.ownerFileName + ", " + netmsgguaranted.hashCode() + ", " + i + ", " + j + ") k=" + k);
            netmsgguaranted.write(out.buf, out.bufCur, k);
            out.bufCur += k;
            if (out.bufCur == out.bufSize)
                out.state = 3;
            return true;
        } else {
            netmsgguaranted.write(commonBuf(out.bufSize), 0, out.bufSize);
            return true;
        }
    }

    public int getAnswerData(NetFileRequest netfilerequest, NetMsgInput netmsginput) throws IOException {
        NetFileRequest netfilerequest1 = netfilerequest;
        In in = netIn(netfilerequest1);
        if (in == null)
            return 1;
        if (in.state != 4)
            return 1;
        int i = netmsginput.available();
        if (compressMethod() != 0) {
            netmsginput.read(in.buf, in.bufFill, i);
//            System.out.println("NetFileServerDef getAnswerData(" + netfilerequest.ownerFileName + ", " + netmsginput.hashCode() + ") i=" + i);
            in.bufFill += i;
            if (in.bufFill == in.bufSize) {
                int j = compressBlockSize();
                if (in.size - in.localSize < j)
                    j = in.size - in.localSize;
                if (in.bufSize < j)
                    Compress.decode(compressMethod(), in.buf, in.bufSize);
                try {
                    in.f.write(in.buf, 0, j);
                } catch (Exception exception1) {
                    printDebug(exception1);
                    return -3;
                }
                in.localSize += j;
                in.bufFill = 0;
            }
        } else {
            netmsginput.read(commonBuf(i), 0, i);
            try {
                in.f.write(commonBuf(i), 0, i);
            } catch (Exception exception) {
                printDebug(exception);
                return -3;
            }
            in.localSize += i;
        }
        netfilerequest1.setComplete((float) in.localSize / (float) in.size);
        if (in.localSize == in.size) {
            in.destroy();
            makeRequestComplete(netfilerequest1, in.localFileName, in.finger);
            return 0;
        } else {
            return 1;
        }
    }

    public int getAnswerExtData(NetFileRequest netfilerequest, NetMsgInput netmsginput) throws IOException {
        NetFileRequest netfilerequest1 = netfilerequest;
        In in = netIn(netfilerequest1);
        if (in == null)
            return 1;
        switch (in.state) {
            case -1:
            case 1: // '\001'
            case 3: // '\003'
            default:
                break;

            case 0: // '\0'
                in.size = netmsginput.readInt();
                in.finger = netmsginput.readLong();
                String s = filePrimaryName(netfilerequest1.ownerFileName());
                int i = fileLength(s);
                if (i > 0 && in.size == i) {
                    long l = Finger.file(0L, s, -1);
                    if (l == in.finger) {
                        makeRequestComplete(netfilerequest1, netfilerequest1.ownerFileName(), in.finger);
                        return 0;
                    }
                }
                in.fileName = fileAlternativeName(netfilerequest1.ownerFileName(), in.finger, true);
                in.localFileName = fileAlternativeName(netfilerequest1.ownerFileName(), in.finger, false);
                i = fileLength(in.fileName);
                if (i > 0)
                    if (i == in.size) {
                        long l1 = Finger.file(0L, in.fileName, -1);
                        if (l1 == in.finger) {
                            makeRequestComplete(netfilerequest1, in.localFileName, in.finger);
                            return 0;
                        }
                    } else {
                        if (i < in.size) {
                            in.localSize = i;
                            in.localFinger = Finger.file(0L, in.fileName, -1);
                            in.state = 1;
                        }
                        break;
                    }
                in.state = 3;
                if (!in.createFile())
                    return -3;
                break;

            case 2: // '\002'
                int j = netmsginput.readInt();
                long l2 = netmsginput.readLong();
                if (j != in.localSize || l2 != in.localFinger)
                    in.localSize = 0;
                in.state = 3;
                if (!in.createFile())
                    return -3;
                break;

            case 4: // '\004'
                if (compressMethod() != 0)
                    in.bufSize = netmsginput.readInt();
                break;
        }
        return 1;
    }

    public boolean sendRequestData(NetFileRequest netfilerequest, NetMsgGuaranted netmsgguaranted, int i, int j) throws IOException {
        NetFileRequest netfilerequest1 = netfilerequest;
        In in = netIn(netfilerequest1);
        if (in == null)
            return false;
        switch (in.state) {
            case -1:
            case 0: // '\0'
            case 2: // '\002'
            case 4: // '\004'
            default:
                return false;

            case 1: // '\001'
                netmsgguaranted.writeInt(j);
                netmsgguaranted.writeInt(in.localSize);
                netmsgguaranted.writeLong(in.localFinger);
                in.state = 2;
                return true;

            case 3: // '\003'
                netmsgguaranted.writeInt(j);
                break;
        }
        netmsgguaranted.writeInt(in.localSize);
        in.state = 4;
        return true;
    }

    public void msgNetDelChannel(NetChannel netchannel) {
        java.util.Map.Entry entry = fileCache.nextEntry(null);
        ArrayList arraylist = new ArrayList();
        for (; entry != null; entry = fileCache.nextEntry(entry)) {
            NetFile netfile = (NetFile) entry.getKey();
            if (netfile.owner.isDestroyed() || netfile.owner.masterChannel() == netchannel)
                arraylist.add(netfile);
        }

        for (int i = 0; i < arraylist.size(); i++)
            fileCache.remove(arraylist.get(i));

        arraylist.clear();
    }

    public void msgNetNewChannel(NetChannel netchannel) {}

    protected boolean isFileExist(String s) {
        File file = new File(HomePath.toFileSystemName(s, 0));
        return file.exists();
    }

    protected int fileLength(String s) {
        File file = new File(HomePath.toFileSystemName(s, 0));
        return (int) file.length();
    }

    protected String filePrimaryName(String s) {
        String s1 = primaryPath();
        if (s1 == null || s1.length() == 0)
            return s;
        if (s == null || s.length() == 0)
            return s;
        char c = s1.charAt(s1.length() - 1);
        if (c == '/' || c == '\\') {
            char c1 = s.charAt(0);
            if (c1 == '/' || c1 == '\\')
                return s1.substring(0, s1.length() - 1) + s;
            else
                return s1 + s;
        } else {
            return s1 + '/' + s;
        }
    }

    protected String fileAlternativeName(String s, long l, boolean flag) {
        String s1 = null;
        if (flag) {
            s1 = alternativePath();
            if (s1 != null && s1.length() == 0)
                s1 = null;
        }
        String s2 = null;
        int i = s.lastIndexOf('.');
        if (i >= 0)
            s2 = s.substring(i);
        if (s1 != null) {
            char c = s1.charAt(s1.length() - 1);
            if (c != '\\' && c != '/')
                if (s2 != null)
                    return s1 + "/" + l + s2;
                else
                    return s1 + "/" + l;
            if (s2 != null)
                return s1 + l + s2;
            else
                return s1 + l;
        }
        if (s2 != null)
            return "" + l + s2;
        else
            return "" + l;
    }

    public NetFileServerDef(int i) {
        super(null, i);
        fileCache = new HashMapExt();
    }

    protected HashMapExt fileCache;
    private byte         commonBuf[];

}
