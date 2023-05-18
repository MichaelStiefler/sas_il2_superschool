package com.maddox.rts.net;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import com.maddox.il2.engine.Config;
import com.maddox.rts.Destroy;
import com.maddox.rts.MsgAction;
import com.maddox.rts.MsgNetAskNakListener;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.util.HashMapExt;

public class NetFileTransport extends NetObj implements MsgNetAskNakListener {
    public class Request implements Destroy {

        public void typeState(PrintStream printstream) {
            String s = null;
            switch (this.state) {
                case ST_INIT:
                    s = "Init";
                    break;

                case ST_WAIT_PARENT:
                    s = "Wait Parent Request";
                    break;

                case ST_SEND_NAME:
                    s = "Send Name";
                    break;

                case ST_SEND_DATA:
                    s = "Send Data";
                    break;

                case ST_SEND_CANCEL:
                    s = "Send Cancel";
                    break;

                case ST_DESTROY:
                    s = "Destroy";
                    break;

                default:
                    s = "UNKNOWN";
                    break;
            }
            printstream.println("Request(" + this.localId + ") state = " + s);
            printstream.println("  request: " + this.nreq.toString());
            if (this.nreq._serverIn != null) printstream.println("  serverIn:  " + this.nreq.server().getStateInInfo(this.nreq));
            if (this.nreq._serverOut != null) printstream.println("  serverOut: " + this.nreq.server().getStateOutInfo(this.nreq));
        }

        public void netInput(NetMsgInput netmsginput, int i) throws IOException {
            if (this.parent != null) return;
            if (this.isDestroyed()) return;
            if (this.state == ST_SEND_CANCEL || this.state == ST_INIT || this.state == ST_WAIT_PARENT) return;
            int j = 1;
            boolean flag = true;
            switch (i) {
                case ID_AR_DATA:
                    NetFileTransport.this.DEBUGR(this.localId, "get data");
                    flag = false;
                    j = this.nreq.server().getAnswerData(this.nreq, netmsginput);
                    break;

                case ID_AR_EXT_DATA:
                    NetFileTransport.this.DEBUGR(this.localId, "get ext data");
                    flag = false;
                    j = this.nreq.server().getAnswerExtData(this.nreq, netmsginput);
                    break;

                case ID_AR_SUCCESS:
                    NetFileTransport.this.DEBUGR(this.localId, "get 'success'");
                    j = 0;
                    break;

                case ID_AR_DISCONNECT:
                    NetFileTransport.this.DEBUGR(this.localId, "get 'owner disconnect'");
                    j = -1;
                    break;

                case ID_AR_NOT_FOUND:
                    NetFileTransport.this.DEBUGR(this.localId, "get 'not found'");
                    j = -2;
                    break;

                case ID_AR_IO:
                    NetFileTransport.this.DEBUGR(this.localId, "get 'io error'");
                    j = -3;
                    break;

                default:
                    return;
            }
            if (j == 1) return;
            if (flag) this.destroy();
            else this.state = ST_SEND_CANCEL;
            this._doAnswer(j);
        }

        private void _doAnswer(int i) {
            this.nreq.setState(i);
            NetFileTransport.this.doAnswer(this.nreq);
            if (this.parent == null) {
                ArrayList arraylist = (ArrayList) NetFileTransport.this.chRequest.get(this.channel);
                if (arraylist == null) { // TODO: By SAS~Storebror - destroy invalid requests
                    this.destroy();
                    return;
                }
                int j = arraylist.size();
                for (int k = 0; k < j; k++) {
                    Request request = (Request) arraylist.get(k);
                    if (request.parent == this && !request.isDestroyed()) {
                        request.destroy();
                        request.nreq.setState(i);
                        request.nreq.setLocalFileName(this.nreq.localFileName());
                        NetFileTransport.this.doAnswer(request.nreq);
                    }
                }

            }
        }

        public void delChannel() {
            if (this.isDestroyed()) return;
            else {
                this.destroy();
                this._doAnswer(-1);
                return;
            }
        }

        public void cancel() {
            if (this.isDestroyed()) return;
            this.bCanceled = true;
            if (this.parent == null) {
                boolean flag = false;
                ArrayList arraylist = (ArrayList) NetFileTransport.this.chRequest.get(this.channel);
                if (arraylist == null) { // TODO: By SAS~Storebror - destroy invalid requests
                    this.destroy();
                    return;
                }
                int i = arraylist.size();
                int j = 0;
                do {
                    if (j >= i) break;
                    Request request1 = (Request) arraylist.get(j);
                    if (request1.parent == this && !request1.isDestroyed()) {
                        flag = true;
                        break;
                    }
                    j++;
                } while (true);
                if (!flag) if (this.state == ST_INIT) this.destroy();
                else this.state = ST_SEND_CANCEL;
                if (!this.nreq.isEnded()) {
                    this.nreq.setState(NetFileRequest.STATE_USER_CANCEL);
                    NetFileTransport.this.doAnswer(this.nreq);
                }
            } else {
                Request request = this.parent;
                this.destroy();
                if (!this.nreq.isEnded()) {
                    this.nreq.setState(-4);
                    NetFileTransport.this.doAnswer(this.nreq);
                }
                if (request.bCanceled) request.cancel();
            }
        }

        public boolean netOutput(NetMsgGuaranted netmsgguaranted, int i) throws IOException {
            if (this.parent != null) return false;
            switch (this.state) {
                case ST_WAIT_PARENT:
                default:
                    return false;

                case ST_INIT:
                    NetFileTransport.this.DEBUGR(this.localId, "send 'spawn begin'");
                    this.writeId(netmsgguaranted, ID_RA_SPAWN_BEGIN);
                    netmsgguaranted.writeByte(this.nreq.prior());
                    netmsgguaranted.writeNetObj(this.nreq.owner());
                    netmsgguaranted.writeNetObj((NetObj) this.nreq.server());
                    netmsgguaranted.writeShort(this.nreq.ownerFileName().length());
                    this.state = ST_SEND_NAME;
                    return true;

                case ST_SEND_NAME:
                    NetFileTransport.this.DEBUGR(this.localId, "send 'name'");
                    this.writeId(netmsgguaranted, ID_RA_SPAWN_NAME);
                    if (this.writeStr(netmsgguaranted, i, this.nreq.ownerFileName())) this.state = ST_SEND_DATA;
                    return true;

                case ST_SEND_DATA:
                    boolean flag = this.nreq.server().sendRequestData(this.nreq, netmsgguaranted, i, 0 | this.localId | 1);
                    if (flag) NetFileTransport.this.DEBUGR(this.localId, "send data");
                    return flag;

                case ST_SEND_CANCEL:
                    NetFileTransport.this.DEBUGR(this.localId, "send 'cancel'");
                    this.writeId(netmsgguaranted, ID_RA_CANCEL);
                    this.destroy();
                    return true;
            }
        }

        private void writeId(NetMsgGuaranted netmsgguaranted, int i) throws IOException {
            netmsgguaranted.writeInt(i << 28 | this.localId | 1);
        }

        private boolean writeStr(NetMsgGuaranted netmsgguaranted, int i, String s) throws IOException {
            int j = s.length() * 2;
            int k;
            for (k = 0; k < i && k + this.strOffset < j; k += 2) {
                int l = (k + this.strOffset) / 2;
                char c = s.charAt(l);
                netmsgguaranted.writeByte(c & 0xff);
                netmsgguaranted.writeByte(c >> 8 & 0xff);
            }

            this.strOffset += k;
            return this.strOffset == j;
        }

        public boolean isDestroyed() {
            return this.state == ST_DESTROY;
        }

        public void destroy() {
            if (this.isDestroyed()) return;
            else {
                this.nreq.server().destroyIn(this.nreq);
                this.state = ST_DESTROY;
                NetFileTransport.this.bCheckDestroyed = true;
                return;
            }
        }

        public Request           parent;
        public NetChannel        channel;
        public NetFileRequest    nreq;
        public int               localId;
        private static final int ST_INIT        = 0;
        private static final int ST_SEND_NAME   = 1;
        private static final int ST_SEND_DATA   = 2;
        private static final int ST_SEND_CANCEL = 3;
        private static final int ST_WAIT_PARENT = 4;
        private static final int ST_DESTROY     = -1;
        private int              state;
        private boolean          bCanceled;
        private int              strOffset;

        public Request(NetFileRequest netfilerequest, int i) {
            this.state = ST_INIT;
            this.bCanceled = false;
            this.strOffset = 0;
            this.nreq = netfilerequest;
            this.localId = i;
            this.channel = netfilerequest.owner().masterChannel();
            ArrayList arraylist = (ArrayList) NetFileTransport.this.chRequest.get(this.channel);
            if (arraylist == null) { // TODO: By SAS~Storebror - destroy invalid requests
                this.destroy();
                return;
            }
            int k = arraylist.size();
            int j = 0;
            do {
                if (j >= k) break;
                Request request = (Request) arraylist.get(j);
                if (request.parent == null && request.nreq.equals(netfilerequest)) {
                    this.parent = request;
                    break;
                }
                j++;
            } while (true);
            j = 0;
            do {
                if (j >= k) break;
                Request request1 = (Request) arraylist.get(j);
                if (netfilerequest.prior() > request1.nreq.prior()) {
                    arraylist.add(j, this);
                    break;
                }
                j++;
            } while (true);
            if (j == k) arraylist.add(this);
            if (this.parent == null) NetFileTransport.this.requestPostMsg(this.channel);
            else this.state = ST_WAIT_PARENT;
        }
    }

    public class Answer implements NetFileClient, Destroy {

        public void typeState(PrintStream printstream) {
            String s = null;
            switch (this.state) {
                case ST_INIT:
                    s = "Init";
                    break;

                case ST_TRANSFER:
                    s = "Transfer";
                    break;

                case ST_DISCONNECT:
                    s = "Owner Disconnect";
                    break;

                case ST_NOT_FOUND:
                    s = "Not Found";
                    break;

                case ST_IO:
                    s = "IO Error";
                    break;

                case ST_DESTROY:
                    s = "Destroy";
                    break;

                default:
                    s = "UNKNOWN";
                    break;
            }
            printstream.println("Answer(" + this.remoteId + ") state = " + s);
            printstream.println("  request: " + this.nreq.toString());
            if (this.nreq._serverIn != null) printstream.println("  serverIn:  " + this.nreq.server().getStateInInfo(this.nreq));
            if (this.nreq._serverOut != null) printstream.println("  serverOut: " + this.nreq.server().getStateOutInfo(this.nreq));
        }

        public void netInput(NetMsgInput netmsginput, int i) throws IOException {
            if (this.isDestroyed()) return;
            if (i == 3) {
                NetFileTransport.this.DEBUGA(this.remoteId, "input 'cancel'");
                this.delChannel();
                return;
            }
            switch (this.state) {
                default:
                    break;

                case ST_INIT:
                    if (i == 2) {
                        String s = this.readStr(netmsginput);
                        if (this.nreq.ownerFileName == null) this.nreq.ownerFileName = s;
                        else this.nreq.ownerFileName = this.nreq.ownerFileName + s;
                        if (this.ownerFileNameLength == this.nreq.ownerFileName.length()) {
                            this.state = ST_TRANSFER;
                            try { // TODO: By SAS~Storebror, sorround with try/catch and return "IO Error" state in case of error.
                                this.nreq.doRequest();
                            } catch (Exception e) {
                                System.out.println("Caught Exception in NetFileTransport.Answer.netInput(netmsginput, " + i + "):");
                                e.printStackTrace();
                                this.nreq.setState(NetFileRequest.STATE_ERR_IO);
                            }
                        }
                    }
                    break;

                case ST_TRANSFER:
                    if (i == 0) {
                        NetFileTransport.this.DEBUGA(this.remoteId, "input data");
                        this.nreq.server().getRequestData(this.nreq, netmsginput);
                    }
                    break;
            }
        }

        public void delChannel() {
            this.destroy();
            if (this.nreq.server() instanceof Destroy && ((Destroy) this.nreq.server()).isDestroyed()) return;
            if (this.nreq.state() == 1) this.nreq.doCancel();
        }

        public void netFileAnswer(NetFileRequest netfilerequest) {
            switch (netfilerequest.state()) {
                case -1:
                    this.state = ST_DISCONNECT;
                    break;

                case -2:
                    this.state = ST_NOT_FOUND;
                    break;

                case -3:
                    this.state = ST_IO;
                    break;
            }
            NetFileTransport.this.requestPostMsg(this.channel);
        }

        public boolean netOutput(NetMsgGuaranted netmsgguaranted, int i) throws IOException {
            switch (this.state) {
                case ID_AR_EXT_DATA:
                    int j = this.nreq.server().getAnswerState(this.nreq, i);
                    switch (j) {
                        case ID_AR_EXT_DATA:
                            NetFileTransport.this.DEBUGA(this.remoteId, "send data");
                            this.nreq.server().sendAnswerData(this.nreq, netmsgguaranted, i, ID_RA_DATA | this.remoteId);
                            return true;

                        case ID_AR_SUCCESS:
                            NetFileTransport.this.DEBUGA(this.remoteId, "send ext data");
                            this.nreq.server().sendAnswerData(this.nreq, netmsgguaranted, i, 0x10000000 | this.remoteId);
                            return true;

                        case ID_AR_DISCONNECT:
                            NetFileTransport.this.DEBUGA(this.remoteId, "send 'success'");
                            this.writeId(netmsgguaranted, ID_AR_SUCCESS);
                            this.destroy();
                            return true;

                        case ID_AR_NOT_FOUND:
                            NetFileTransport.this.DEBUGA(this.remoteId, "send 'io error 0'");
                            this.writeId(netmsgguaranted, ID_AR_IO);
                            this.destroy();
                            return true;
                    }
                    break;

                case ID_AR_SUCCESS:
                    NetFileTransport.this.DEBUGA(this.remoteId, "send 'owner disconnect'");
                    this.writeId(netmsgguaranted, ID_AR_DISCONNECT);
                    this.destroy();
                    return true;

                case ID_AR_DISCONNECT:
                    NetFileTransport.this.DEBUGA(this.remoteId, "send 'not found'");
                    this.writeId(netmsgguaranted, ID_AR_NOT_FOUND);
                    this.destroy();
                    return true;

                case ID_AR_NOT_FOUND:
                    NetFileTransport.this.DEBUGA(this.remoteId, "send 'io error 1'");
                    this.writeId(netmsgguaranted, ID_AR_IO);
                    this.destroy();
                    return true;
            }
            return false;
        }

        private void writeId(NetMsgGuaranted netmsgguaranted, int i) throws IOException {
            netmsgguaranted.writeInt(i << 28 | this.remoteId);
        }

        private String readStr(NetMsgInput netmsginput) throws IOException {
            StringBuffer stringbuffer = new StringBuffer();
            int i;
            for (; netmsginput.available() > 0; stringbuffer.append((char) i))
                i = netmsginput.readUnsignedByte() | netmsginput.readUnsignedByte() << 8;

            return stringbuffer.toString();
        }

        public boolean isDestroyed() {
            return this.state == -1;
        }

        public void destroy() {
            if (this.isDestroyed()) return;
            else {
                this.nreq.server().destroyOut(this.nreq);
                this.state = -1;
                NetFileTransport.this.bCheckDestroyed = true;
                return;
            }
        }

        public NetChannel        channel;
        public NetFileRequest    nreq;
        public int               ownerFileNameLength;
        public int               remoteId;
        private static final int ST_INIT       = 0;
        private static final int ST_TRANSFER   = 1;
        private static final int ST_DISCONNECT = 2;
        private static final int ST_NOT_FOUND  = 3;
        private static final int ST_IO         = 4;
        private static final int ST_DESTROY    = -1;
        private int              state;

        public Answer(NetChannel netchannel, int i, NetObj netobj, NetFileServer netfileserver, int j, int k) {
            this.state = 0;
            this.channel = netchannel;
            this.remoteId = j;
            this.ownerFileNameLength = k;
            this.nreq = new NetFileRequest(this, netfileserver, null, i, netobj, null);
            ArrayList arraylist = (ArrayList) NetFileTransport.this.chAnswer.get(netchannel);
            int l = arraylist.size();
            for (int i1 = 0; i1 < l; i1++) {
                Answer answer = (Answer) arraylist.get(i1);
                if (this.nreq.prior() > answer.nreq.prior()) {
                    arraylist.add(i1, this);
                    return;
                }
            }

            arraylist.add(this);
        }
    }

    static class MsgParam {

        int count;
        int size;

        MsgParam() {
        }
    }

    private void DEBUG(String s) {
    }

    private void DEBUGA(int i, String s) {
    }

    private void DEBUGR(int i, String s) {
    }

    public static int MSG_FULL_SIZE(int i) {
        return i + 4;
    }

    public void messageSetParams(NetChannel netchannel, int i, int j) {
//        Exception test = new Exception("messageSetParams (" + netchannel.id() + ", " + i + ", " + j + ")");
//        test.printStackTrace();
        if (i < 1) i = 1;
        if (i > 64) i = 64;
        if (j < 32) j = 32;
        if (j > MSG_DEFAULT_SIZE_HIGHSPEED) j = MSG_DEFAULT_SIZE_HIGHSPEED;
        j &= -2;
        MsgParam msgparam = (MsgParam) this.chMsgParam.get(netchannel);
        if (msgparam == null) msgparam = new MsgParam();
        msgparam.count = i;
        msgparam.size = j;
        this.chMsgParam.put(netchannel, msgparam);
    }

    public int messageCount(NetChannel netchannel) {
        MsgParam msgparam = (MsgParam) this.chMsgParam.get(netchannel);
        return msgparam.count;
    }

    public int messageSize(NetChannel netchannel) {
        MsgParam msgparam = (MsgParam) this.chMsgParam.get(netchannel);
        return msgparam.size;
    }

    public void doRequest(NetFileRequest netfilerequest) {
        new Request(netfilerequest, this.nextLocalId & 0xfffffff);
        this.nextLocalId += 2;
    }

    public void doCancel(NetFileRequest netfilerequest) {
        NetChannel netchannel = netfilerequest.owner().masterChannel();
        ArrayList arraylist = (ArrayList) this.chRequest.get(netchannel);
        if (arraylist == null) { // TODO: By SAS~Storebror - destroy invalid requests
            this.destroy();
            return;
        }
        int j = arraylist.size();
        int i = 0;
        do {
            if (i >= j) break;
            Request request = (Request) arraylist.get(i);
            if (request.nreq.equals(netfilerequest)) {
                request.cancel();
                break;
            }
            i++;
        } while (true);
        this.bCheckDestroyed = true;
        this.requestPostMsg(netchannel);
    }

    private void doAnswer(NetFileRequest netfilerequest) {
        if (netfilerequest.server() instanceof Destroy && ((Destroy) netfilerequest.server()).isDestroyed()) return;
        else {
            netfilerequest.server().doAnswer(netfilerequest);
            return;
        }
    }

    public boolean netInput(NetMsgInput netmsginput) throws IOException {
        NetChannel netchannel = netmsginput.channel();
        if (this.chAnswer.get(netchannel) == null) return true;
        int i = netmsginput.readInt();
        int j = i >> 28 & 0xf;
        i &= 0xfffffff;
        boolean flag = (i & 1) == 1;
        i &= -2;
        if (flag) {
            this.DEBUGA(i, "input message...");
            if (j == 1) {
                this.DEBUG("spawn Answer");
                int k = netmsginput.readUnsignedByte();
                NetObj netobj = netmsginput.readNetObj();
                NetFileServer netfileserver = (NetFileServer) netmsginput.readNetObj();
                int l1 = netmsginput.readUnsignedShort();
                new Answer(netchannel, k, netobj, netfileserver, i, l1);
            } else {
                ArrayList arraylist = (ArrayList) this.chAnswer.get(netchannel);
                if (arraylist == null) return true;
                int l = arraylist.size();
                int j1 = 0;
                do {
                    if (j1 >= l) break;
                    Answer answer = (Answer) arraylist.get(j1);
                    if (answer.remoteId == i) {
                        answer.netInput(netmsginput, j);
                        break;
                    }
                    j1++;
                } while (true);
            }
        } else {
            this.DEBUGR(i, "input message...");
            ArrayList arraylist1 = (ArrayList) this.chRequest.get(netchannel);
            if (arraylist1 == null) return true;
            int i1 = arraylist1.size();
            int k1 = 0;
            do {
                if (k1 >= i1) break;
                Request request = (Request) arraylist1.get(k1);
                if (request.localId == i) {
                    request.netInput(netmsginput, j);
                    break;
                }
                k1++;
            } while (true);
        }
        this.doCheckDestroyed();
        this.requestPostMsg(netmsginput.channel());
        return true;
    }

    private NetMsgGuaranted getMsgOut(NetChannel netchannel) {
        if (netchannel.isDestroying()) return null;
        MsgParam msgparam = (MsgParam) this.chMsgParam.get(netchannel);
        this._curMaxMsgSize = msgparam.size;
        int i = msgparam.count;
        ArrayList arraylist = (ArrayList) this.chMsg.get(netchannel);
        int j = arraylist.size() - i;
        if (j > 0) {
            int k = 0;
            do {
                if (k >= arraylist.size()) break;
                NetMsgGuaranted netmsgguaranted = (NetMsgGuaranted) arraylist.get(k);
                if (!netmsgguaranted.isLocked()) {
                    arraylist.remove(k);
                    k--;
                    if (--j == 0) break;
                }
                k++;
            } while (true);
        } else if (j < 0) {
            j = -j;
            for (int l = 0; l < j; l++) {
                NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(MSG_FULL_SIZE(this._curMaxMsgSize));
                netmsgguaranted1.setRequiredAsk(true);
                arraylist.add(netmsgguaranted1);
            }

        }
        j = arraylist.size();
        for (int i1 = 0; i1 < j; i1++) {
            NetMsgGuaranted netmsgguaranted2 = (NetMsgGuaranted) arraylist.get(i1);
            if (!netmsgguaranted2.isLocked()) return netmsgguaranted2;
        }

        return null;
    }

    private void requestPostMsg(NetChannel netchannel) {
        NetMsgGuaranted netmsgguaranted = this.getMsgOut(netchannel);
        if (netmsgguaranted == null) return;
        else {
            new MsgAction(true, netchannel) {

                public void doAction(Object obj) {
                    NetChannel netchannel1 = (NetChannel) obj;
                    if (netchannel1.isDestroying()) return;
                    NetMsgGuaranted netmsgguaranted1 = NetFileTransport.this.getMsgOut(netchannel1);
                    if (netmsgguaranted1 == null) return;
                    else {
                        NetFileTransport.this.doNetOutput(netmsgguaranted1, netchannel1);
                        return;
                    }
                }

            };
            return;
        }
    }

    private void doNetOutput(NetMsgGuaranted netmsgguaranted, NetChannel netchannel) {
        boolean flag;
        do {
            netmsgguaranted = this.getMsgOut(netchannel);
            if (netmsgguaranted == null) break;
            int i = this._curMaxMsgSize;
            flag = false;
            try {
                netmsgguaranted.clear();
                ArrayList arraylist = (ArrayList) this.chRequest.get(netchannel);
                int j = arraylist.size();
                int i1 = 0;
                do {
                    if (i1 >= j) break;
                    Request request = (Request) arraylist.get(i1);
                    if (flag = request.netOutput(netmsgguaranted, i)) break;
                    i1++;
                } while (true);
                if (!flag) {
                    ArrayList arraylist1 = (ArrayList) this.chAnswer.get(netchannel);
                    int k = arraylist1.size();
                    int j1 = 0;
                    do {
                        if (j1 >= k) break;
                        Answer answer = (Answer) arraylist1.get(j1);
                        if (!answer.nreq.server().isStateDataTransfer(answer.nreq) && (flag = answer.netOutput(netmsgguaranted, i))) break;
                        j1++;
                    } while (true);
                }
                if (!flag) {
                    ArrayList arraylist2 = (ArrayList) this.chAnswer.get(netchannel);
                    int l = arraylist2.size();
                    int k1 = 0;
                    do {
                        if (k1 >= l) break;
                        Answer answer1 = (Answer) arraylist2.get(k1);
                        if (flag = answer1.netOutput(netmsgguaranted, i)) break;
                        k1++;
                    } while (true);
                }
                if (flag) this.postTo(netchannel, netmsgguaranted);
                this.doCheckDestroyed();
            } catch (Exception exception) {
                printDebug(exception);
                this.requestPostMsg(netchannel);
                flag = false;
            }
        } while (flag);
    }

    public void msgNetAsk(NetMsgGuaranted netmsgguaranted, NetChannel netchannel) {
        this.doNetOutput(netmsgguaranted, netchannel);
    }

    public void msgNetNak(NetMsgGuaranted netmsgguaranted, NetChannel netchannel) {
    }

    public void msgNetDelChannel(NetChannel netchannel) {
        this.delChannel(netchannel);
    }

    public void msgNetNewChannel(NetChannel netchannel) {
        this.newChannel(netchannel);
    }

    private void delChannel(NetChannel netchannel) {
        ArrayList arraylist = (ArrayList) this.chAnswer.get(netchannel);
        if (arraylist != null) {
            int i = arraylist.size();
            for (int k = 0; k < i; k++) {
                Answer answer = (Answer) arraylist.get(k);
                answer.delChannel();
            }

            arraylist.clear();
            this.chAnswer.remove(netchannel);
        }
        arraylist = (ArrayList) this.chRequest.get(netchannel);
        if (arraylist != null) {
            int j = arraylist.size();
            for (int l = 0; l < j; l++) {
                Request request = (Request) arraylist.get(l);
                request.delChannel();
            }

            arraylist.clear();
            this.chRequest.remove(netchannel);
        }
        this.chMsg.remove(netchannel);
        this.chMsgParam.remove(netchannel);
        this.doCheckDestroyed();
    }

    private void newChannel(NetChannel netchannel) {
        if (this.chRequest.containsKey(netchannel)) return;
        if (netchannel instanceof NetChannelInStream) return;
        else {
            this.chRequest.put(netchannel, new ArrayList());
            this.chAnswer.put(netchannel, new ArrayList());
            if (Config.cur.bNetBoost) this.messageSetParams(netchannel, MSG_DEFAULT_COUNT_HIGHSPEED, MSG_DEFAULT_SIZE_HIGHSPEED);
            else this.messageSetParams(netchannel, MSG_DEFAULT_COUNT, MSG_DEFAULT_SIZE);

            // TODO: +++ NetBoost by SAS~Storebror +++
            if (bDEBUG) {
                // TODO: --- NetBoost by SAS~Storebror ---
                String usesNetBoost = "unknown";
                MsgParam msgparam = (MsgParam) this.chMsgParam.get(netchannel);
                if (msgparam != null) usesNetBoost = msgparam.count == MSG_DEFAULT_COUNT_HIGHSPEED && msgparam.size == MSG_DEFAULT_SIZE_HIGHSPEED ? "true" : "false";
                System.out.println("Net Channel ID=" + netchannel.id() + ", Remote Address=" + netchannel.remoteAddress().getHostAddress() + ", maxSpeed=" + netchannel.getMaxSpeed() + ", bNetBoost=" + usesNetBoost);
            }

            this.chMsg.put(netchannel, new ArrayList());
            return;
        }
    }

    private void doCheckDestroyed() {
        if (!this.bCheckDestroyed) return;
        this.bCheckDestroyed = false;
        for (java.util.Map.Entry entry = this.chAnswer.nextEntry(null); entry != null; entry = this.chAnswer.nextEntry(entry)) {
            ArrayList arraylist = (ArrayList) entry.getValue();
            for (int i = 0; i < arraylist.size(); i++) {
                Answer answer = (Answer) arraylist.get(i);
                if (answer.isDestroyed()) {
                    arraylist.remove(i);
                    i--;
                }
            }

        }

        for (java.util.Map.Entry entry1 = this.chRequest.nextEntry(null); entry1 != null; entry1 = this.chRequest.nextEntry(entry1)) {
            ArrayList arraylist1 = (ArrayList) entry1.getValue();
            for (int j = 0; j < arraylist1.size(); j++) {
                Request request = (Request) arraylist1.get(j);
                if (request.isDestroyed()) {
                    arraylist1.remove(j);
                    j--;
                }
            }

        }

    }

    public void destroy() {
        super.destroy();
    }

    public void typeStates(PrintStream printstream) {
        for (java.util.Map.Entry entry = this.chAnswer.nextEntry(null); entry != null; entry = this.chAnswer.nextEntry(entry)) {
            ArrayList arraylist = (ArrayList) entry.getValue();
            for (int i = 0; i < arraylist.size(); i++) {
                Answer answer = (Answer) arraylist.get(i);
                answer.typeState(printstream);
            }

        }

        for (java.util.Map.Entry entry1 = this.chRequest.nextEntry(null); entry1 != null; entry1 = this.chRequest.nextEntry(entry1)) {
            ArrayList arraylist1 = (ArrayList) entry1.getValue();
            for (int j = 0; j < arraylist1.size(); j++) {
                Request request = (Request) arraylist1.get(j);
                request.typeState(printstream);
            }

        }

    }

    public NetFileTransport(int i) {
        super(null, i);
        this.nextLocalId = 0;
        this.chRequest = new HashMapExt();
        this.chAnswer = new HashMapExt();
        this.chMsg = new HashMapExt();
        this.chMsgParam = new HashMapExt();
        this.bCheckDestroyed = false;
    }

    private static final boolean bDEBUG                      = false;
    public static int            MSG_DEFAULT_COUNT           = 2;
    public static int            MSG_DEFAULT_SIZE            = 64;
    // TODO: +++ NetBoost by SAS~Storebror +++
    public static int            MSG_DEFAULT_COUNT_HIGHSPEED = 64;
    public static int            MSG_DEFAULT_SIZE_HIGHSPEED  = 250;
    // TODO: --- NetBoost by SAS~Storebror ---
    private int                  nextLocalId;
    private HashMapExt           chRequest;
    private HashMapExt           chAnswer;
    private HashMapExt           chMsg;
    private HashMapExt           chMsgParam;
    private boolean              bCheckDestroyed;
    private static final int     ID_RA_SPAWN_BEGIN           = 1;
    private static final int     ID_RA_SPAWN_NAME            = 2;
    private static final int     ID_RA_DATA                  = 0;
    private static final int     ID_RA_CANCEL                = 3;
    private static final int     ID_AR_DATA                  = 0;
    private static final int     ID_AR_EXT_DATA              = 1;
    private static final int     ID_AR_SUCCESS               = 2;
    private static final int     ID_AR_DISCONNECT            = 3;
    private static final int     ID_AR_NOT_FOUND             = 4;
    private static final int     ID_AR_IO                    = 5;
    private int                  _curMaxMsgSize;

}
