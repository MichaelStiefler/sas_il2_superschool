package com.maddox.rts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.maddox.il2.game.Mission;
import com.maddox.sound.AudioDevice;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;

public class NetChannel implements Destroy {
    static class NakMessage extends NetObj {

        public void send(int i, int j, NetChannel netchannel) {
            try {
                NetMsgFiltered netmsgfiltered = netchannel.nakMessageOut;
                if (netmsgfiltered.isLocked()) netmsgfiltered.unLock(netchannel);
                netmsgfiltered.clear();
                netmsgfiltered.prior = 1.1F;
                netmsgfiltered.writeShort(i);
                netmsgfiltered.writeByte(j - 1);
                this.postRealTo(Time.currentReal(), netchannel, netmsgfiltered);
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public void msgNet(NetMsgInput netmsginput) {
            try {
                int i = netmsginput.readUnsignedShort();
                int j = netmsginput.readUnsignedByte() + 1;
                netmsginput.channel().nakMessageReceive(i, j);
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public NakMessage(int i) {
            super(null, i);
        }
    }

    static class AskMessage extends NetObj {

        public void send(int i, NetChannel netchannel) {
            try {
                NetMsgFiltered netmsgfiltered = netchannel.askMessageOut;
                if (netmsgfiltered.isLocked()) netmsgfiltered.unLock(netchannel);
                netmsgfiltered.clear();
                netmsgfiltered.prior = 1.1F;
                netmsgfiltered.writeShort(i);
                this.postRealTo(Time.currentReal(), netchannel, netmsgfiltered);
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public void msgNet(NetMsgInput netmsginput) {
            try {
                int i = netmsginput.readUnsignedShort();
                netmsginput.channel().askMessageReceive(i);
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public AskMessage(int i) {
            super(null, i);
        }
    }

    static class DestroyMessage extends NetObj {

        public void msgNet(NetMsgInput netmsginput) {
            try {
                NetChannel.destroyNetObj(netmsginput.readNetObj());
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public DestroyMessage(int i) {
            super(null, i);
        }
    }

    static class SpawnMessage extends NetObj {

        public void msgNet(NetMsgInput netmsginput) {
            if (netmsginput.channel().isDestroying()) return;
            try {
                int i = netmsginput.readInt();
                int j = netmsginput.readUnsignedShort();
                netmsginput.channel().removeWaitSpawn(j);
                netmsginput.fixed();
                NetSpawn netspawn = (NetSpawn) Spawn.get(i);
                netspawn.netSpawn(j, netmsginput);
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public SpawnMessage(int i) {
            super(null, i);
        }
    }

    static class ChannelObj extends NetObj {

        public void doSetSpeed(NetChannel netchannel, double d) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(3);
                netmsgguaranted.writeInt((int) (d * 1000D));
                this.postTo(netchannel, netmsgguaranted);
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public void doSetTimeout(NetChannel netchannel, int i) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(4);
                netmsgguaranted.writeInt(i);
                this.postTo(netchannel, netmsgguaranted);
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public void doDestroy(NetChannel netchannel) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(0);
                netmsgguaranted.write255(netchannel.diagnosticMessage);
                this.postTo(netchannel, netmsgguaranted);
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public void doRequestCreating(NetChannel netchannel) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(1);
                netmsgguaranted.writeByte(netchannel.flags);
                netmsgguaranted.writeInt((int) (netchannel.maxSendSpeed * 1000D));
                if ((netchannel.flags & 4) != 0) if (NetEnv.cur().control == null) {
                    new NetControlLock(netchannel);
                    netmsgguaranted.writeByte(0);
                } else netmsgguaranted.writeByte(1);
                this.postTo(netchannel, netmsgguaranted);
                if ((netchannel.flags & 4) == 0) netchannel.controlStartInit();
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
                netchannel.destroy();
            }
        }

        public void msgNet(NetMsgInput netmsginput) {
            NetChannel netchannel;
            netchannel = netmsginput.channel();
            if (netchannel.isDestroying()) return;
            try {
                byte byte0 = netmsginput.readByte();
                switch (byte0) {
                    default:
                        break;

                    case MSG_DESTROY: // '\0'
                        if (netmsginput.available() > 0) netchannel.destroy(netmsginput.read255());
                        else netchannel.destroy();
                        break;

                    case MSG_REQUEST_CREATING: // '\001'
                        int i = netmsginput.readByte();
                        double d = netmsginput.readInt() / 1000D;
                        netchannel.flags = i;
                        if ((i & 4) != 0) {
                            boolean flag = netmsginput.readByte() != 0;
                            if (flag) {
                                if (NetEnv.cur().control == null) {
                                    new NetControlLock(netchannel);
                                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                                    netmsgguaranted.writeByte(2);
                                    this.postTo(netchannel, netmsgguaranted);
                                } else {
                                    if (NetEnv.cur().control instanceof NetControlLock) netchannel.destroy("Remote control slot is locked.");
                                    else netchannel.destroy("Only TREE network structure is supported.");
                                    break;
                                }
                            } else {
                                if (NetEnv.cur().control == null) new NetControl(null);
                                else {
                                    if (NetEnv.cur().control instanceof NetControlLock) {
                                        netchannel.destroy("Remote control slot is locked.");
                                        break;
                                    }
                                    if (!(NetEnv.cur().control instanceof NetControl)) {
                                        netchannel.destroy("Remote control slot is cracked.");
                                        break;
                                    }
                                }
                                MsgNet.postRealNewChannel(NetEnv.cur().control, netchannel);
                            }
                        } else netchannel.controlStartInit();
                        if (d > netchannel.maxSendSpeed) this.doSetSpeed(netchannel, netchannel.maxSendSpeed);
                        else netchannel.setMaxSendSpeed(d);
                        break;

                    case MSG_ASK_CREATING: // '\002'
                        MsgNet.postRealNewChannel(NetEnv.cur().control, netchannel);
                        break;

                    case MSG_SET_SPEED: // '\003'
                        double d1 = netmsginput.readInt() / 1000D;
                        netchannel.setMaxSpeed(d1);
                        break;

                    case MSG_SET_TIMEOUT: // '\004'
                        int j = netmsginput.readInt();
                        netchannel.setMaxTimeout(j);
                        break;
                }
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
            return;
        }

        private static final int MSG_DESTROY          = 0;
        private static final int MSG_REQUEST_CREATING = 1;
        private static final int MSG_ASK_CREATING     = 2;
        private static final int MSG_SET_SPEED        = 3;
        private static final int MSG_SET_TIMEOUT      = 4;

        public ChannelObj(int i) {
            super(null, i);
        }
    }

    public int id() {
        return this.id;
    }

    public int remoteId() {
        return this.remoteId;
    }

    public NetSocket socket() {
        return this.socket;
    }

    public NetAddress remoteAddress() {
        return this.remoteAddress;
    }

    public int remotePort() {
        return this.remotePort;
    }

    public NetObj getMirror(int i) {
        return (NetObj) this.objects.get(i);
    }

    public boolean isInitRemote() {
        return (this.id & 1) == 1;
    }

    public boolean isMirrored(NetObj netobj) {
        return this.mirrored.containsKey(netobj);
    }

    public void setMirrored(NetObj netobj) {
        if (!this.mirrored.containsKey(netobj)) {
            this.mirrored.put(netobj, null);
            netobj.countMirrors++;
        }
    }

    public boolean isRealTime() {
        return (this.flags & 1) != 0;
    }

    public boolean isPublic() {
        return (this.flags & 2) != 0;
    }

    public boolean isGlobal() {
        return (this.flags & 4) != 0;
    }

    public void setInitStamp(int i) {
        this.initStamp = i;
    }

    public int getInitStamp() {
        return this.initStamp;
    }

    public int state() {
        return this.state;
    }

    public void setStateInit(int i) {
        this.state = this.state & 0xc0000000 | i & 0x3fffffff;
    }

    public boolean isReady() {
        return this.state == 0;
    }

    public boolean isIniting() {
        return (this.state & 0x3fffffff) != 0;
    }

    public boolean isDestroyed() {
        return this.state < 0;
    }

    public boolean isDestroying() {
        return (this.state & 0xc0000000) != 0;
    }

    public boolean isSortGuaranted() {
        return this.bSortGuaranted;
    }

    public void startSortGuaranted() {
        this.bSortGuaranted = true;
    }

    public List filters() {
        return this.filters;
    }

    public void filterAdd(NetFilter netfilter) {
        int i = this.filters.size();
        for (int j = 0; j < i; j++) {
            NetFilter netfilter1 = (NetFilter) this.filters.get(j);
            if (!netfilter1.filterEnableAdd(this, netfilter)) return;
        }

        this.filters.add(netfilter);
    }

    public void filterRemove(NetFilter netfilter) {
        int i = this.filters.indexOf(this);
        if (i >= 0) this.filters.remove(i);
    }

    protected void statIn(boolean flag, NetObj netobj, NetMsgInput netmsginput) {
        if (this.stat == null) return;
        byte byte0 = 0;
        byte byte1 = 0;
        int i = 0;
        if (netmsginput.buf != null) {
            i = netmsginput.available();
            byte0 = i <= 0 ? 0 : netmsginput.buf[0];
            byte1 = i <= 1 ? 0 : netmsginput.buf[1];
        }
        try {
            this.stat.inc(this, netobj, flag, false, i, byte0, byte1);
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    protected void statOut(boolean flag, NetObj netobj, NetMsgOutput netmsgoutput) {
        if (this.stat == null) return;
        byte byte0 = 0;
        byte byte1 = 0;
        int i = 0;
        if (netmsgoutput.buf != null) {
            i = netmsgoutput.size();
            byte0 = i <= 0 ? 0 : netmsgoutput.buf[0];
            byte1 = i <= 1 ? 0 : netmsgoutput.buf[1];
        }
        try {
            this.stat.inc(this, netobj, flag, true, i, byte0, byte1);
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    public void destroy(String s) {
        if ((this.state & 0xc0000000) != 0) return;
        else {
            this.diagnosticMessage = s;
            this.destroy();
            return;
        }
    }

    public void destroy() {
        if ((this.state & 0xc0000000) != 0) return;
        this.timeDestroyed = Time.currentReal() + 1000L;
        this.state |= 0x40000000;
        if ((this.state & 0x3fffffff) == 0) this.connect.channelDestroying(this, this.diagnosticMessage);
        do {
            if (this.objects.isEmpty()) break;
            HashMapIntEntry hashmapintentry = this.objects.nextEntry(null);
            if (hashmapintentry == null) break;
            NetObj netobj = (NetObj) hashmapintentry.getValue();
            int i = hashmapintentry.getKey();
            destroyNetObj(netobj);
            if (this.objects.containsKey(i)) this.objects.remove(i);
        } while (true);
        do {
            if (this.mirrored.isEmpty()) break;
            java.util.Map.Entry entry = this.mirrored.nextEntry(null);
            if (entry == null) break;
            NetObj netobj1 = (NetObj) entry.getKey();
            netobj1.countMirrors--;
            this.mirrored.remove(netobj1);
            MsgNet.postRealDelChannel(netobj1, this);
        } while (true);
        HashMapInt hashmapint = NetEnv.cur().objects;
        for (HashMapIntEntry hashmapintentry1 = hashmapint.nextEntry(null); hashmapintentry1 != null; hashmapintentry1 = hashmapint.nextEntry(hashmapintentry1)) {
            NetObj netobj2 = (NetObj) hashmapintentry1.getValue();
            if (netobj2.isCommon()) MsgNet.postRealDelChannel(netobj2, this);
        }

        channelObj.doDestroy(this);
    }

    protected boolean update() {
        if (this.state < 0) return false;
        if ((this.state & 0x40000000) != 0 && Time.currentReal() > this.timeDestroyed) {
            if ((this.state & 0x3fffffff) != 0) {
                this.connect.channelNotCreated(this, this.diagnosticMessage);
                if (NetEnv.cur().control != null && NetEnv.cur().control instanceof NetControlLock) {
                    NetControlLock netcontrollock = (NetControlLock) NetEnv.cur().control;
                    if (netcontrollock.channel() == this) netcontrollock.destroy();
                }
            }
            this.state = 0x80000000;
            this.clearSortGuaranted();
            this.clearSendGMsgs();
            this.clearReceivedGMsgs();
            this.clearSendFMsgs();
            return false;
        }
        this.flushReceivedGuarantedMsgs();
        if (this.isTimeout()) this.destroy("Timeout.");
        return true;
    }

    private static boolean winLT(int i, int j, int k) {
        if (i == j) return false;
        if (i > j) return i - j < k / 2;
        else return i + k - j + 1 < k / 2;
    }

    private static boolean winDownDelta(int i, int j, int k, int l) {
        if (k <= i) i -= k;
        else i = l - (k - i) + 1;
        if (j == i) return true;
        else return winLT(j, i, l);
    }

    private static boolean winUpDelta(int i, int j, int k, int l) {
        if (k <= j) j -= k;
        else j = l - (k - j) + 1;
        if (j == i) return true;
        else return winLT(i, j, l);
    }

    private static int winDeltaLT(int i, int j, int k) {
        if (j <= i) return i - j;
        else return i + k - j + 1;
    }

    protected void putMessageSpawn(NetMsgSpawn netmsgspawn) throws IOException {
        if (this.state < 0) throw new NetException("Channel is destroyed");
        if (netmsgspawn.size() > 255) throw new IOException("Output message is very long (" + netmsgspawn.size() + " bytes)");
        if ((this.state & 0x40000000) != 0) throw new NetException("Channel is closed for spawning objects");
        if (this.bSortGuaranted && !this.isReferenceOk(netmsgspawn)) {
            this.holdGMsg(netmsgspawn);
            return;
        } else {
            this.setGMsg(netmsgspawn, 3);
            this.mirrored.put(netmsgspawn._sender, null);
            netmsgspawn._sender.countMirrors++;
            return;
        }
    }

    protected void putMessageDestroy(NetMsgDestroy netmsgdestroy) throws IOException {
        if (this.state < 0) throw new NetException("Channel is destroyed");
        if (netmsgdestroy.size() > 255) throw new IOException("Output message is very long");
        if (this.bSortGuaranted && !this.isReferenceOk(netmsgdestroy)) {
            this.holdGMsg(netmsgdestroy);
            return;
        } else {
            this.setGMsg(netmsgdestroy, 4);
            this.mirrored.remove(netmsgdestroy._sender);
            netmsgdestroy._sender.countMirrors--;
            return;
        }
    }

    protected void putMessage(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (this.state < 0) throw new NetException("Channel is destroyed");
        if (netmsgguaranted.size() > 255) throw new IOException("Output message is very long (" + netmsgguaranted.size() + " bytes).");
        if (this.bSortGuaranted && !this.isReferenceOk(netmsgguaranted)) {
            this.holdGMsg(netmsgguaranted);
            return;
        }
        int i = this.getIndx(netmsgguaranted._sender);
        if (i == -1) throw new NetException("Put Guaranted message to NOT mirrored object [" + netmsgguaranted._sender + "] (" + this.id() + ")");
        else {
            this.setGMsg(netmsgguaranted, i);
            return;
        }
    }

    private NetChannelGMsgOutput setGMsg(NetMsgGuaranted netmsgguaranted, int i) throws IOException {
        List list = netmsgguaranted.objects();
        byte abyte0[] = null;
        if (list != null) {
            int j = list.size();
            abyte0 = new byte[2 * j];
            _tmpOut.buf = abyte0;
            _tmpOut.count = 0;
            for (int k = j - 1; k >= 0; k--) {
                NetObj netobj = (NetObj) list.get(k);
                int l = this.getIndx(netobj);
                // +++ TODO: Storebror: Avoid excessive logging of net messages that are being tried to send to not mirrored objects +++
//                if (l == -1)
//                    throw new NetException("Put Guaranted message referenced to NOT mirrored object [" + netmsgguaranted._sender + "] -> [" + netobj + "] (" + id() + ")");
                if (l != -1)
                    // --- TODO: Storebror: Avoid excessive logging of net messages that are being tried to send to not mirrored objects ---
                    _tmpOut.writeShort(l);
            }

        }
        NetChannelGMsgOutput netchannelgmsgoutput = new NetChannelGMsgOutput();
        this.sendGMsgSequenceNum = this.sendGMsgSequenceNum + 1 & 0xffff;
        netchannelgmsgoutput.sequenceNum = this.sendGMsgSequenceNum;
        netchannelgmsgoutput.objIndex = i;
        netchannelgmsgoutput.iObjects = abyte0;
        netchannelgmsgoutput.timeLastSend = 0L;
        netchannelgmsgoutput.msg = netmsgguaranted;
        this.sendGMsgs.add(netchannelgmsgoutput);
        netmsgguaranted.lockInc();
        return netchannelgmsgoutput;
    }

    private boolean isReferenceOk(NetMsgGuaranted netmsgguaranted) {
        if (!(netmsgguaranted instanceof NetMsgSpawn) && !(netmsgguaranted instanceof NetMsgDestroy) && this.getIndx(netmsgguaranted._sender) == -1) return false;
        List list = netmsgguaranted.objects();
        if (list == null) return true;
        int i = list.size();
        for (int j = 0; j < i; j++) {
            NetObj netobj = (NetObj) list.get(j);
            if (this.getIndx(netobj) == -1) return false;
        }

        return true;
    }

    private void holdGMsg(NetMsgGuaranted netmsgguaranted) {
        this.holdGMsgs.add(netmsgguaranted);
        netmsgguaranted.lockInc();
    }

    private void flushHoldedGMsgs() throws IOException {
        for (boolean flag = true; flag;) {
            flag = false;
            int i = this.holdGMsgs.size();
            int j = 0;
            while (j < i) {
                NetMsgGuaranted netmsgguaranted = (NetMsgGuaranted) this.holdGMsgs.get(j);
                if (this.isReferenceOk(netmsgguaranted)) {
                    if (netmsgguaranted instanceof NetMsgSpawn) this.putMessageSpawn((NetMsgSpawn) netmsgguaranted);
                    else if (netmsgguaranted instanceof NetMsgDestroy) this.putMessageDestroy((NetMsgDestroy) netmsgguaranted);
                    else this.putMessage(netmsgguaranted);
                    netmsgguaranted.lockDec();
                    flag = true;
                    this.holdGMsgs.remove(j);
                    j--;
                    i--;
                }
                j++;
            }
        }

    }

    public void stopSortGuaranted() throws IOException {
        if (!this.bSortGuaranted) return;
        this.flushHoldedGMsgs();
        this.bSortGuaranted = false;
        int i = this.holdGMsgs.size();
        if (i > 0) {
            System.err.println("Channel '" + this.id + "' cycled guaranted messages dump:");
            for (int j = 0; j < i; j++) {
                NetMsgGuaranted netmsgguaranted = (NetMsgGuaranted) this.holdGMsgs.get(j);
                netmsgguaranted.lockDec();
                if (netmsgguaranted.isRequiredAsk()) MsgNetAskNak.postReal(Time.currentReal(), netmsgguaranted._sender, false, netmsgguaranted, this);
                System.err.print(" " + netmsgguaranted.toString() + " (" + netmsgguaranted._sender.toString() + ") Data:");
                for (int k = 0; k < netmsgguaranted.dataLength(); k++)
                    System.err.print(" " + Integer.toHexString(netmsgguaranted.data()[k]));
                System.err.println();
            }

            this.holdGMsgs.clear();
            // return;
            throw new IOException("Cycled guaranted messages");
        } else return;
    }

    protected void clearSortGuaranted() {
        int i = this.holdGMsgs.size();
        for (int j = 0; j < i; j++) {
            NetMsgGuaranted netmsgguaranted = (NetMsgGuaranted) this.holdGMsgs.get(j);
            netmsgguaranted.lockDec();
            if (netmsgguaranted.isRequiredAsk()) MsgNetAskNak.postReal(Time.currentReal(), netmsgguaranted._sender, false, netmsgguaranted, this);
        }

        this.holdGMsgs.clear();
    }

    protected void putMessage(NetMsgFiltered netmsgfiltered) throws IOException {
        if (this.state < 0) throw new NetException("Channel is destroyed");
        if (netmsgfiltered.size() > 255) throw new IOException("Output message is very long");
        if (this.getIndx(netmsgfiltered._sender) == -1) return;
        List list = netmsgfiltered.objects();
        if (list != null) for (int i = list.size() - 1; i >= 0; i--) {
            int j = this.getIndx((NetObj) list.get(i));
            if (j == -1) return;
        }
        this.filteredTickMsgs.put(netmsgfiltered, this);
        netmsgfiltered.lockInc();
    }

    private int getIndx(NetObj netobj) {
        if (netobj == null) return 0;
        int i = netobj.idLocal & 0x7fff;
        if (netobj.isMirror()) {
            if (netobj.masterChannel == this) i = netobj.idRemote;
            else i |= 0x8000;
        } else if (!netobj.isCommon()) i |= 0x8000;
        if ((i & 0x8000) != 0 && !this.mirrored.containsKey(netobj)) return -1;
        else return i;
    }

    protected boolean unLockMessage(NetMsgFiltered netmsgfiltered) {
        if (this.filteredTickMsgs.remove(netmsgfiltered) != null) netmsgfiltered.lockDec();
        return !netmsgfiltered.isLocked();
    }

    public int gSendQueueLenght() {
        return this.sendGMsgs.size();
    }

    public int gSendQueueSize() {
        int i = 0;
        int j = this.sendGMsgs.size();
        for (int k = 0; k < j; k++) {
            NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) this.sendGMsgs.get(k);
            i += netchannelgmsgoutput.msg.size();
        }

        return i;
    }

    private void askMessageReceive(int i) {
        int j = this.sendGMsgs.size();
        if (j == 0) return;
        if (j > MESSAGE_SEQUENCE_FRAME) j = MESSAGE_SEQUENCE_FRAME;
        NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) this.sendGMsgs.get(0);
        if (winLT(netchannelgmsgoutput.sequenceNum, i, 65535)) return;
        netchannelgmsgoutput = (NetChannelGMsgOutput) this.sendGMsgs.get(j - 1);
        if (winLT(i, netchannelgmsgoutput.sequenceNum, 65535)) {
            System.err.println("Channel '" + this.id + "' reseived ask for NOT sended message " + i + " " + netchannelgmsgoutput.sequenceNum);
            return;
        }
        int k = 0;
        do {
            if (k >= j) break;
            netchannelgmsgoutput = (NetChannelGMsgOutput) this.sendGMsgs.get(k++);
            NetMsgGuaranted netmsgguaranted = netchannelgmsgoutput.msg;
            netmsgguaranted.lockDec();
            if (netmsgguaranted.isRequiredAsk()) MsgNetAskNak.postReal(Time.currentReal(), netmsgguaranted._sender, true, netmsgguaranted, this);
            netchannelgmsgoutput.msg = null;
        } while (netchannelgmsgoutput.sequenceNum != i);
        this.sendGMsgs.removeRange(0, k);
    }

    protected void clearSendGMsgs() {
        int i = this.sendGMsgs.size();
        if (i == 0) return;
        for (int j = 0; j < i; j++) {
            NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) this.sendGMsgs.get(j);
            NetMsgGuaranted netmsgguaranted = netchannelgmsgoutput.msg;
            netmsgguaranted.lockDec();
            if (netmsgguaranted.isRequiredAsk()) MsgNetAskNak.postReal(Time.currentReal(), netmsgguaranted._sender, false, netmsgguaranted, this);
        }

        this.sendGMsgs.clear();
    }

    private void nakMessageReceive(int i, int j) {
        int k = this.sendGMsgs.size();
        if (k == 0) return;
        if (k > MESSAGE_SEQUENCE_FRAME) k = MESSAGE_SEQUENCE_FRAME;
        int l = 0;
        for (NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) this.sendGMsgs.get(l); j > 0 && winLT(netchannelgmsgoutput.sequenceNum, i, 65535); i = i + 1 & 0xffff)
            j--;

        if (j == 0) return;
        do {
            if (k <= 0) break;
            NetChannelGMsgOutput netchannelgmsgoutput1 = (NetChannelGMsgOutput) this.sendGMsgs.get(l);
            if (netchannelgmsgoutput1.sequenceNum == i) break;
            k--;
            l++;
        } while (true);
        if (k == 0) return;
        while (k-- > 0 && j-- > 0) {
            NetChannelGMsgOutput netchannelgmsgoutput2 = (NetChannelGMsgOutput) this.sendGMsgs.get(l++);
            netchannelgmsgoutput2.timeLastSend = 0L;
        }
    }

    private void tryNakMessageSend() {
        if (this.receiveGMsgs.size() == 0) return;
        if (this.lastTimeNakMessageSend + this.ping() * 3 / 2 > Time.currentReal()) return;
        this.lastTimeNakMessageSend = Time.currentReal();
        int i = this.receiveGMsgSequenceNum + 1 & 0xffff;
        int j;
        for (j = 0; j < 256 && !this.receiveGMsgs.containsKey(i); j++)
            i = i + 1 & 0xffff;

        j--;
        nakMessage.send(this.receiveGMsgSequenceNum + 1 & 0xffff, j, this);
    }

    protected boolean receivedGuarantedMsg(NetMsgInput netmsginput, int i) {
        if (this.receiveGMsgSequenceNum == i) return true;
        if (winLT(this.receiveGMsgSequenceNum, i, 65535)) return true;
        if ((this.receiveGMsgSequenceNum + 1 & 0xffff) == i) {
            this.receiveGMsgSequenceNum = i;
            if (getMessageObj != null && (this.receiveGMsgSequenceNumPosted + 1 & 0xffff) == i) {
                this.receiveGMsgSequenceNumPosted = i;
                this.postReceivedGMsg(Time.currentReal(), getMessageObj, netmsginput);
            } else {
                NetChannelGMsgInput netchannelgmsginput = new NetChannelGMsgInput();
                netchannelgmsginput.sequenceNum = i;
                netchannelgmsginput.objIndex = getMessageObjIndex;
                netchannelgmsginput.msg = netmsginput;
                this.receiveGMsgs.put(i, netchannelgmsginput);
            }
            if (getMessageObj == spawnMessage) this.addWaitSpawn(i, netmsginput);
            this.flushReceivedGuarantedMsgs();
            return true;
        }
        if (!this.receiveGMsgs.containsKey(i)) {
            NetChannelGMsgInput netchannelgmsginput1 = new NetChannelGMsgInput();
            netchannelgmsginput1.sequenceNum = i;
            netchannelgmsginput1.objIndex = getMessageObjIndex;
            netchannelgmsginput1.msg = netmsginput;
            this.receiveGMsgs.put(i, netchannelgmsginput1);
            if (getMessageObj == spawnMessage) this.addWaitSpawn(i, netmsginput);
        }
        return false;
    }

    private void addWaitSpawn(int i, NetMsgInput netmsginput) {
        i = i + 1 & 0xffff | 0x10000;
        try {
            netmsginput.readInt();
            int k = netmsginput.readUnsignedShort();
            netmsginput.reset();
            NetChannelGMsgInput netchannelgmsginput = new NetChannelGMsgInput();
            netchannelgmsginput.sequenceNum = i;
            netchannelgmsginput.objIndex = k;
            netchannelgmsginput.msg = null;
            this.receiveGMsgs.put(i, netchannelgmsginput);
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    private void removeWaitSpawn(int i) {
        for (HashMapIntEntry hashmapintentry = null; (hashmapintentry = this.receiveGMsgs.nextEntry(hashmapintentry)) != null;) {
            NetChannelGMsgInput netchannelgmsginput = (NetChannelGMsgInput) hashmapintentry.getValue();
            if (netchannelgmsginput.objIndex == i && (netchannelgmsginput.sequenceNum & 0x10000) != 0) {
                this.receiveGMsgs.remove(netchannelgmsginput.sequenceNum);
                return;
            }
        }

    }

    protected boolean isEnableFlushReceivedGuarantedMsgs() {
        return true;
    }

    protected void flushReceivedGuarantedMsgs() {
        long l = Time.currentReal();
        do {
            if (!this.isEnableFlushReceivedGuarantedMsgs() || this.receiveGMsgs.size() <= 0) break;
            NetChannelGMsgInput netchannelgmsginput = (NetChannelGMsgInput) this.receiveGMsgs.get(this.receiveGMsgSequenceNumPosted + 1 & 0xffff | 0x10000);
            if (netchannelgmsginput != null) {
                int i = netchannelgmsginput.objIndex;
                NetObj netobj1 = (NetObj) this.objects.get(i);
                if (netobj1 == null && this.isExistSpawnPosted(i)) break;
                this.receiveGMsgs.remove(netchannelgmsginput.sequenceNum);
                continue;
            }
            netchannelgmsginput = (NetChannelGMsgInput) this.receiveGMsgs.get(this.receiveGMsgSequenceNumPosted + 1 & 0xffff);
            if (netchannelgmsginput == null) break;
            NetObj netobj = null;
            int j = netchannelgmsginput.objIndex;
            if ((j & 0x8000) != 0) {
                j &= 0xffff7fff;
                netobj = (NetObj) this.objects.get(j);
                if (netobj == null && this.isExistSpawnPosted(j)) break;
            } else netobj = (NetObj) NetEnv.cur().objects.get(j);
            this.receiveGMsgSequenceNumPosted = netchannelgmsginput.sequenceNum;
            this.receiveGMsgs.remove(this.receiveGMsgSequenceNumPosted);
            if (netobj != null) this.postReceivedGMsg(l, netobj, netchannelgmsginput.msg);
            netchannelgmsginput.msg = null;
        } while (true);
    }

    protected void postReceivedGMsg(long l, NetObj netobj, NetMsgInput netmsginput) {
        this.statIn(false, netobj, netmsginput);
        MsgNet.postReal(l, netobj, netmsginput);
    }

    protected void clearReceivedGMsgs() {
        this.receiveGMsgs.clear();
    }

    private boolean isExistSpawnPosted(int i) {
        try {
            MessageQueue messagequeue = RTSConf.cur.queueRealTime;
            synchronized (messagequeue) {
                int j = 0;
                do {
                    Message message = messagequeue.peekByIndex(j++);
                    if (message == null) break;
                    if (message instanceof MsgNet && message.listener() == spawnMessage && message.sender() instanceof NetMsgInput) {
                        NetMsgInput netmsginput = (NetMsgInput) message.sender();
                        netmsginput.readInt();
                        int l = netmsginput.readUnsignedShort();
                        netmsginput.reset();
                        if (i == l) {
                            boolean flag = true;
                            return flag;
                        }
                    }
                } while (true);
            }
        } catch (Exception exception) {
            printDebug(exception);
        }
        return false;
    }

    protected int computeSizeSendGMsgs(long l) {
        int i = this.sendGMsgs.size();
        if (i == 0) return 0;
        if (i > MESSAGE_SEQUENCE_FRAME) i = MESSAGE_SEQUENCE_FRAME;
        long l1 = l - 2 * this.ping();
        int j = 0;
        do {
            if (j >= i) break;
            NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) this.sendGMsgs.get(j);
            if (l1 > netchannelgmsgoutput.timeLastSend) {
                sequenceNumSendGMsgs = netchannelgmsgoutput.sequenceNum;
                firstIndxSendGMsgs = j;
                break;
            }
            j++;
        } while (true);
        if (j == i) return 0;
        if (i > j + 128) i = j + 128;
        int k = 0;
        do {
            if (j >= i) break;
            NetChannelGMsgOutput netchannelgmsgoutput1 = (NetChannelGMsgOutput) this.sendGMsgs.get(j);
            if (l1 < netchannelgmsgoutput1.timeLastSend) break;
            this.computeMessageLen(netchannelgmsgoutput1.msg);
            k += netchannelgmsgoutput1.msg._len;
            j++;
        } while (true);
        return k;
    }

    protected int computeCountSendGMsgs(int i) {
        int j = this.sendGMsgs.size();
        if (j > MESSAGE_SEQUENCE_FRAME) j = MESSAGE_SEQUENCE_FRAME;
        int k = firstIndxSendGMsgs;
        int l = 0;
        guarantedSizeMsgs = 0;
        do {
            if (k >= j || i <= 0) break;
            NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) this.sendGMsgs.get(k++);
            i -= netchannelgmsgoutput.msg._len;
            if (i < 0 && l > 0) break;
            guarantedSizeMsgs += netchannelgmsgoutput.msg._len;
            l++;
        } while (true);
        return l;
    }

    protected int computeSizeSendFMsgs(long l) {
        filteredSizeMsgs = 0;
        filteredMinSizeMsgs = 0;
        for (java.util.Map.Entry entry = this.filteredTickMsgs.nextEntry(null); entry != null; entry = this.filteredTickMsgs.nextEntry(entry)) {
            NetMsgFiltered netmsgfiltered = (NetMsgFiltered) entry.getKey();
            this.computeMessageLen(netmsgfiltered, l);
            if (netmsgfiltered.prior > 1.0F) {
                netmsgfiltered._prior = netmsgfiltered.prior;
                filteredMinSizeMsgs += netmsgfiltered._len;
                filteredSizeMsgs += netmsgfiltered._len;
                filteredSortMsgs.add(netmsgfiltered);
                continue;
            }
            int i = this.filters.size();
            if (i > 0) {
                float f = -1F;
                for (int j = 0; j < i; j++) {
                    float f1 = ((NetFilter) this.filters.get(j)).filterNetMessage(this, netmsgfiltered);
                    if (f1 > 1.0F) f1 = 1.0F;
                    if (f1 > f) f = f1;
                }

                if (f < 0.0F) netmsgfiltered._prior = netmsgfiltered.prior + 0.5F * ((float) Math.random() - 0.5F);
                else netmsgfiltered._prior = f + 0.2F * ((float) Math.random() - 0.5F);
            } else netmsgfiltered._prior = netmsgfiltered.prior + 0.5F * ((float) Math.random() - 0.5F);
            if (netmsgfiltered._prior < 0.0F) netmsgfiltered._prior = 0.0F;
            if (netmsgfiltered._prior > 1.0F) netmsgfiltered._prior = 1.0F;
            filteredSizeMsgs += netmsgfiltered._len;
            filteredSortMsgs.add(netmsgfiltered);
        }

        this.filteredTickMsgs.clear();
        return filteredSizeMsgs;
    }

    protected void clearSendFMsgs() {
        for (java.util.Map.Entry entry = this.filteredTickMsgs.nextEntry(null); entry != null; entry = this.filteredTickMsgs.nextEntry(entry)) {
            NetMsgFiltered netmsgfiltered = (NetMsgFiltered) entry.getKey();
            netmsgfiltered.lockDec();
        }

        this.filteredTickMsgs.clear();
    }

    protected int fillFilteredArrayMessages(long l, int i) {
        if (filteredSizeMsgs > i) {
            Collections.sort(filteredSortMsgs, priorComparator);
            for (int j = filteredSortMsgs.size() - 1; j >= 0 && filteredSizeMsgs > i; j--) {
                NetMsgFiltered netmsgfiltered = (NetMsgFiltered) filteredSortMsgs.get(j);
                netmsgfiltered.lockDec();
                filteredSizeMsgs -= netmsgfiltered._len;
                filteredSortMsgs.remove(j);
            }

        }
        if (filteredSizeMsgs == 0) return 0;
        Collections.sort(filteredSortMsgs, timeComparator);
        int k = this.filters.size();
        if (k > 0) {
            int i1 = filteredSortMsgs.size();
            for (int j1 = 0; j1 < k; j1++) {
                NetFilter netfilter = (NetFilter) this.filters.get(j1);
                for (int k1 = 0; k1 < i1; k1++)
                    netfilter.filterNetMessagePosting(this, (NetMsgFiltered) filteredSortMsgs.get(k1));

            }

        }
        return filteredSizeMsgs;
    }

    protected boolean sendPacket(NetMsgOutput netmsgoutput, NetPacket netpacket) {
        long l = Time.real();
        if (this.isTimeout()) {
            this.destroy("Timeout.");
            return false;
        }
        boolean flag = false;
        this.tryNakMessageSend();
        int i = this.getSendPacketLen(l);
        if (l >= this.lastPacketSendTime + this.maxTimeout / 4) {
            if (i < 20) i = 20;
            flag = true;
        }
        if (i <= 0) return false;
        int j = 6;
        if (this.isRealTime()) j += 6;
        i -= j;
        if (i <= 0) return false;
        int k = 0;
        int i1 = this.computeSizeSendGMsgs(l);
        if (i1 > 0) {
            i -= 3;
            if (this.swTbl != null) i--;
        }
        if (i <= 0) return false;
        int j1 = this.computeSizeSendFMsgs(l);
        if (j1 + i1 == 0 && !flag) return false;
        if (j1 + i1 > i) {
            int k1 = j1;
            int j2 = i1;
            if (j2 > i) j2 = i;
            k1 = i - j2;
            if (k1 < filteredMinSizeMsgs) k1 = filteredMinSizeMsgs;
            j2 = i - k1;
            if (j2 < 0) {
                if (i1 > 0) {
                    i += 3;
                    if (this.swTbl != null) i++;
                }
                j2 = 0;
            }
            if (j2 > 0 && j2 < i1) {
                k = this.computeCountSendGMsgs(j2);
                j2 = guarantedSizeMsgs;
                if (i - j2 > k1) k1 = i - j2;
            } else {
                k = this.computeCountSendGMsgs(j2);
                j2 = guarantedSizeMsgs;
            }
            j1 = k1;
            i1 = j2;
        } else k = this.computeCountSendGMsgs(i1);
        j1 = this.fillFilteredArrayMessages(l, j1);
        if (j1 + i1 == 0 && !flag) return false;
        try {
            netmsgoutput.clear();
            netmsgoutput.writeShort(this.remoteId);
            netmsgoutput.writeByte(this.crcInit[0]);
            netmsgoutput.writeByte(this.crcInit[1]);
            this.sendSequenceNum = this.sendSequenceNum + 1 & 0x3fff;
            if (k > 0) this.sendSequenceNum |= 0x8000;
            if (this.isRealTime()) {
                netmsgoutput.writeShort(this.sendSequenceNum | 0x4000);
                this.sendTime(netmsgoutput, this.sendSequenceNum, l, j + j1 + i1);
            } else {
                netmsgoutput.writeShort(this.sendSequenceNum);
                this.sendTime(netmsgoutput, this.sendSequenceNum, l, j + j1 + i1);
            }
            if (k > 0) {
                netmsgoutput.writeShort(sequenceNumSendGMsgs);
                netmsgoutput.writeByte(k - 1);
                if (this.swTbl != null) netmsgoutput.writeByte(0);
                int l1 = netmsgoutput.dataLength();
                int k2 = firstIndxSendGMsgs;
                while (k-- > 0) {
                    NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) this.sendGMsgs.get(k2++);
                    netchannelgmsgoutput.timeLastSend = l;
                    this.putMessage(netmsgoutput, netchannelgmsgoutput.objIndex, netchannelgmsgoutput.msg, netchannelgmsgoutput.iObjects);
                }
                if (this.swTbl != null) {
                    int j3 = netmsgoutput.dataLength();
                    int k3 = j3 - l1;
                    if (k3 > 0) {
                        if (k3 > 255) k3 = 255;
                        netmsgoutput.data()[l1 - 1] = (byte) k3;
                        this.cdata(netmsgoutput.data(), l1, k3);
                    }
                }
            }
            int i2 = filteredSortMsgs.size();
            for (int l2 = 0; l2 < i2; l2++) {
                NetMsgFiltered netmsgfiltered = (NetMsgFiltered) filteredSortMsgs.get(l2);
                this.putMessage(netmsgoutput, l, netmsgfiltered);
                netmsgfiltered.lockDec();
            }

            filteredSortMsgs.clear();
            int i3 = CRC16.checksum(0, netmsgoutput.data(), 0, netmsgoutput.dataLength());
            netmsgoutput.data()[2] = (byte) (i3 >>> 8 & 0xff);
            netmsgoutput.data()[3] = (byte) (i3 & 0xff);
            netpacket.setLength(netmsgoutput.dataLength());
            netpacket.setAddress(this.remoteAddress);
            netpacket.setPort(this.remotePort);
            this.socket.send(netpacket);
        } catch (Exception exception) {
            printDebug(exception);
        }
        return false;
    }

    protected boolean receivePacket(NetMsgInput netmsginput, long l) throws IOException {
        netmsginput.readUnsignedShort();
        int i = netmsginput.readUnsignedShort();
        byte abyte0[] = netmsginput.buf;
        int j = netmsginput.pos - 4;
        int k = netmsginput.available() + 4;
        abyte0[j + 2] = this.crcInit[0];
        abyte0[j + 3] = this.crcInit[1];
        int i1 = CRC16.checksum(0, abyte0, j, k);
        if (i != i1) return false;
        if (this.isTimeout()) {
            this.destroy("Timeout.");
            return true;
        }
        i = netmsginput.readUnsignedShort();
        boolean flag = false;
        boolean flag1 = (i & 0x8000) != 0;
        boolean flag2 = true;
        boolean flag3 = (i & 0x4000) == 0 ? false : true;
        i &= 0x3fff;
        if (this.receiveSequenceNum == i) return true;
        if (winLT(this.receiveSequenceNum, i, PACKET_SEQUENCE_FULL)) {
            if (!flag1) return true;
            int j1 = winDeltaLT(this.receiveSequenceNum, i, PACKET_SEQUENCE_FULL);
            if (j1 > 31) return true;
            if ((1 << j1 & this.receiveSequenceMask) != 0) return true;
            flag2 = false;
            flag = true;
        } else {
            for (; this.receiveSequenceNum != i; this.receiveSequenceNum = this.receiveSequenceNum + 1 & 0x3fff)
                this.receiveSequenceMask <<= 1;

            this.receiveSequenceMask |= 1;
        }
        long l1 = this.receiveTime(l, netmsginput, i, flag3, flag);
        if (flag1) {
            int k1 = netmsginput.readUnsignedShort();
            int i2 = netmsginput.readUnsignedByte() + 1;
            if (this.swTbl != null) {
                int j2 = netmsginput.readUnsignedByte();
                this.cdata(netmsginput.buf, netmsginput.pos, j2);
            }
            boolean flag4 = false;
            while (i2-- > 0 && netmsginput.available() > 0) {
                NetMsgInput netmsginput2 = this.getMessage(netmsginput);
                if (this.receivedGuarantedMsg(netmsginput2, k1)) flag4 = true;
                k1 = k1 + 1 & 0xffff;
            }
            if (flag4) askMessage.send(this.receiveGMsgSequenceNum, this);
        }
        boolean flag5 = true;
        if (flag2) while (netmsginput.available() > 0) {
            NetMsgInput netmsginput1 = this.getMessage(netmsginput, l1);
            if (netmsginput1 != null) {
                this.statIn(true, getMessageObj, netmsginput1);
                MsgNet.postReal(getMessageTime, getMessageObj, netmsginput1);
            } else flag5 = false;
        }
        if (flag5) this.lastPacketOkReceiveTime = this.lastPacketReceiveTime;
        if (this.checkC >= checkTimeSpeedInterval) this.destroy("Timeout .");
        return true;
    }

    protected void putMessage(NetMsgOutput netmsgoutput, int i, NetMsgGuaranted netmsgguaranted, byte abyte0[]) throws IOException {
        this.statOut(false, netmsgguaranted._sender, netmsgguaranted);
        netmsgoutput.writeShort(i);
        netmsgoutput.writeByte(netmsgguaranted.size());
        if (netmsgguaranted.dataLength() > 0) netmsgoutput.write(netmsgguaranted.data(), 0, netmsgguaranted.dataLength());
        if (abyte0 != null) netmsgoutput.write(abyte0, 0, abyte0.length);
        this.statNumSendGMsgs++;
        this.statSizeSendGMsgs += netmsgguaranted.size();
    }

    protected void putMessage(NetMsgOutput netmsgoutput, long l, NetMsgFiltered netmsgfiltered) throws IOException {
        this.putMessage(netmsgoutput, l, netmsgfiltered, netmsgfiltered._time);
    }

    protected void putMessage(NetMsgOutput netmsgoutput, long l, NetMsgFiltered netmsgfiltered, long l1) throws IOException {
        this.statOut(true, netmsgfiltered._sender, netmsgfiltered);
        netmsgoutput.writeShort(this.getIndx(netmsgfiltered._sender));
        this.statHSizeSendFMsgs += 2;
        int k = netmsgfiltered.size();
        if (k < 32) {
            int i = k;
            if (netmsgfiltered.isIncludeTime() && this.isRealTime()) {
                int i1 = (int) (l1 - l);
                char c = '\0';
                if (i1 < 0) {
                    i1 = -i1;
                    c = '\200';
                }
                if ((i1 & 0xffffff80) == 0) {
                    netmsgoutput.writeByte(i | 0x40);
                    netmsgoutput.writeByte(c | i1);
                    this.statHSizeSendFMsgs += 2;
                } else if ((i1 & 0xffff8000) == 0) {
                    netmsgoutput.writeByte(i | 0x80);
                    netmsgoutput.writeByte(i1 >> 8 & 0x7f | c);
                    netmsgoutput.writeByte(i1 & 0xff);
                    this.statHSizeSendFMsgs += 3;
                } else {
                    netmsgoutput.writeByte(i | 0xc0);
                    if (i1 > 0x7fffff) i1 = 0x7fffff;
                    netmsgoutput.writeByte(i1 >> 16 & 0x7f | c);
                    netmsgoutput.writeByte(i1 >> 8 & 0xff);
                    netmsgoutput.writeByte(i1 & 0xff);
                    this.statHSizeSendFMsgs += 4;
                }
            } else {
                netmsgoutput.writeByte(i);
                this.statHSizeSendFMsgs++;
            }
        } else {
            int j = 32;
            if (netmsgfiltered.isIncludeTime() && this.isRealTime()) {
                int j1 = (int) (l1 - l);
                if (j1 < 0) {
                    j1 = -j1;
                    j |= 0x10;
                }
                if ((j1 & 0xfffffff0) == 0) {
                    netmsgoutput.writeByte(j | 0x40 | j1 & 0xf);
                    netmsgoutput.writeByte(k);
                    this.statHSizeSendFMsgs += 2;
                } else if ((j1 & 0xfffff000) == 0) {
                    netmsgoutput.writeByte(j | 0x80 | j1 >> 8 & 0xf);
                    netmsgoutput.writeByte(k);
                    netmsgoutput.writeByte(j1 & 0xff);
                    this.statHSizeSendFMsgs += 3;
                } else {
                    if (j1 > 0xfffff) j1 = 0xfffff;
                    netmsgoutput.writeByte(j | 0xc0 | j1 >> 16 & 0xf);
                    netmsgoutput.writeByte(k);
                    netmsgoutput.writeByte(j1 >> 8 & 0xff);
                    netmsgoutput.writeByte(j1 & 0xff);
                    this.statHSizeSendFMsgs += 4;
                }
            } else {
                netmsgoutput.writeByte(j);
                netmsgoutput.writeByte(k);
                this.statHSizeSendFMsgs += 2;
            }
        }
        if (netmsgfiltered.dataLength() > 0) netmsgoutput.write(netmsgfiltered.data(), 0, netmsgfiltered.dataLength());
        List list = netmsgfiltered.objects();
        if (list != null) for (int k1 = list.size() - 1; k1 >= 0; k1--)
            netmsgoutput.writeShort(this.getIndx((NetObj) list.get(k1)));
        this.statNumSendFMsgs++;
        this.statSizeSendFMsgs += netmsgfiltered.size();
    }

    private void computeMessageLen(NetMsgGuaranted netmsgguaranted) {
        netmsgguaranted._len = 3 + netmsgguaranted.size();
    }

    protected void computeMessageLen(NetMsgFiltered netmsgfiltered, long l) {
        this.computeMessageLen(netmsgfiltered, netmsgfiltered._time, l);
    }

    protected void computeMessageLen(NetMsgFiltered netmsgfiltered, long l, long l1) {
        int i = 3;
        int j = netmsgfiltered.size();
        i += j;
        if (j < 32) {
            if (netmsgfiltered.isIncludeTime() && this.isRealTime()) {
                int k = (int) (l - l1);
                if (k < 0) k = -k;
                if ((k & 0xffffff80) == 0) i++;
                else if ((k & 0xffff8000) == 0) i += 2;
                else i += 3;
            }
        } else {
            i++;
            if (netmsgfiltered.isIncludeTime() && this.isRealTime()) {
                int i1 = (int) (l - l1);
                if (i1 < 0) i1 = -i1;
                if ((i1 & 0xfffffff0) == 0) i += 0;
                else if ((i1 & 0xfffff000) == 0) i++;
                else i += 2;
            }
        }
        netmsgfiltered._len = i;
    }

    protected NetMsgInput getMessage(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedShort();
        getMessageObjIndex = i;
        NetObj netobj = null;
        if ((i & 0x8000) != 0) {
            i &= 0xffff7fff;
            netobj = (NetObj) this.objects.get(i);
        } else netobj = (NetObj) NetEnv.cur().objects.get(i);
        int j = netmsginput.readUnsignedByte();
        getMessageObj = netobj;
        this.statHSizeReseivedMsgs += 3;
        NetMsgInput netmsginput1 = new NetMsgInput();
        if (j > 0) {
            byte abyte0[] = new byte[j];
            netmsginput.read(abyte0);
            netmsginput1.setData(this, true, abyte0, 0, j);
            this.statSizeReseivedMsgs += j;
        } else netmsginput1.setData(this, true, null, 0, 0);
        this.statNumReseivedMsgs++;
        return netmsginput1;
    }

    protected NetMsgInput getMessage(NetMsgInput netmsginput, long l) throws IOException {
        int i = netmsginput.readUnsignedShort();
        getMessageObjIndex = i;
        NetObj netobj = null;
        if ((i & 0x8000) != 0) {
            i &= 0xffff7fff;
            netobj = (NetObj) this.objects.get(i);
        } else netobj = (NetObj) NetEnv.cur().objects.get(i);
        int j = netmsginput.readUnsignedByte();
        this.statHSizeReseivedMsgs += 3;
        boolean flag = (j & 0xc0) != 0;
        boolean flag1 = (j & 0x20) == 0;
        long l1 = l;
        int k;
        if (flag1) {
            k = j & 0x1f;
            if (flag) {
                int i1 = j >> 6;
                int k1 = netmsginput.readUnsignedByte();
                this.statHSizeReseivedMsgs++;
                boolean flag2 = (k1 & 0x80) != 0;
                k1 &= 0x7f;
                while (--i1 > 0) {
                    k1 = k1 << 8 | netmsginput.readUnsignedByte();
                    this.statHSizeReseivedMsgs++;
                }
                l1 += flag2 ? -k1 : k1;
            }
        } else {
            k = netmsginput.readUnsignedByte();
            this.statHSizeReseivedMsgs++;
            if (flag) {
                int j1 = j >> 6;
                int i2 = j & 0xf;
                boolean flag3 = (j & 0x10) != 0;
                while (--j1 > 0) {
                    i2 = i2 << 8 | netmsginput.readUnsignedByte();
                    this.statHSizeReseivedMsgs++;
                }
                l1 += flag3 ? -i2 : i2;
            }
        }
        getMessageTime = l1;
        getMessageObj = netobj;
        if (netobj != null) {
            NetMsgInput netmsginput1 = new NetMsgInput();
            if (k > 0) {
                byte abyte0[] = new byte[k];
                netmsginput.read(abyte0);
                netmsginput1.setData(this, false, abyte0, 0, k);
            } else netmsginput1.setData(this, false, null, 0, 0);
            this.statSizeReseivedMsgs += k;
            this.statNumReseivedMsgs++;
            return netmsginput1;
        } else {
            netmsginput.skipBytes(k);
            this.statSizeReseivedMsgs += k;
            this.statNumReseivedMsgs++;
            return null;
        }
    }

    protected static void printDebug(Exception exception) {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
    }

    protected NetChannel(int i, int j, int k, NetSocket netsocket, NetAddress netaddress, int l, NetConnect netconnect) {
        this.objects = new HashMapInt();
        this.mirrored = new HashMapExt();
        this.filters = new ArrayList();
        this.userState = -1;
        this.bSortGuaranted = false;
        this.holdGMsgs = new ArrayList();
        this.sendGMsgs = new NetChannelArrayList();
        this.sendGMsgSequenceNum = 0;
        this.receiveGMsgs = new HashMapInt();
        this.receiveGMsgSequenceNum = 0;
        this.receiveGMsgSequenceNumPosted = 0;
        this.lastTimeNakMessageSend = 0L;
        this.filteredTickMsgs = new HashMapExt();
        this.sendSequenceNum = 0;
        this.receiveSequenceNum = 0;
        this.receiveSequenceMask = 1;
        this.lastCheckTimeSpeed = 0L;
        this.diagnosticMessage = "";
        this.sendHistory = new NetChannelCycleHistory(64);
        this.maxTimeout = MAX_TIMEOUT_DEFAULT;
        this.receiveCountPackets = 0;
        this.bCheckTimeSpeed = false;
        this.checkT = new long[32];
        this.checkR = new long[32];
        this.checkI = -1;
        this.checkC = 0;
        this.swTbl = null;
        this.askMessageOut = new NetMsgFiltered();
        this.nakMessageOut = new NetMsgFiltered();
        this.flags = i;
        this.id = j;
        this.remoteId = k;
        this.socket = netsocket;
        this.remoteAddress = netaddress;
        this.remotePort = l;
        if ((j & 1) != 0) this.bCheckTimeSpeed = bCheckServerTimeSpeed;
        else this.bCheckTimeSpeed = bCheckClientTimeSpeed;
        this.state = 1;
        this.lastPacketReceiveTime = Time.real();
        this.setMaxSendSpeed(netsocket.getMaxSpeed());
        this.connect = netconnect;
        if (!this.isInitRemote()) channelObj.doRequestCreating(this);
    }

    private void doCheckTimeSpeed() {
        try {
            if (this.isDestroying()) return;
            if (this.state == 0 && Mission.isPlaying()) {
                int i = AudioDevice.getControl(611);
                long l = Time.real();
                long l1 = l - this.lastCheckTimeSpeed;
                if (i >= 0 && this.lastCheckTimeSpeed != 0L && l1 > 400L && l1 < 800L) {
                    double d = i / 44100D;
                    double d1 = l1 / 1000D;
                    if (Math.abs(1.0D - d / d1) > 0.04D) {
                        new MsgAction(64, 10D, this) {

                            public void doAction(Object obj) {
                                NetChannel netchannel = (NetChannel) obj;
                                if (!netchannel.isDestroying()) netchannel.destroy("Timeout .");
                            }

                        };
                        return;
                    }
                }
                this.lastCheckTimeSpeed = l;
            }
            new MsgAction(64, 0.5D, this) {

                public void doAction(Object obj) {
                    NetChannel netchannel = (NetChannel) obj;
                    netchannel.doCheckTimeSpeed();
                }

            };
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    protected NetChannel() {
        this.objects = new HashMapInt();
        this.mirrored = new HashMapExt();
        this.filters = new ArrayList();
        this.userState = -1;
        this.bSortGuaranted = false;
        this.holdGMsgs = new ArrayList();
        this.sendGMsgs = new NetChannelArrayList();
        this.sendGMsgSequenceNum = 0;
        this.receiveGMsgs = new HashMapInt();
        this.receiveGMsgSequenceNum = 0;
        this.receiveGMsgSequenceNumPosted = 0;
        this.lastTimeNakMessageSend = 0L;
        this.filteredTickMsgs = new HashMapExt();
        this.sendSequenceNum = 0;
        this.receiveSequenceNum = 0;
        this.receiveSequenceMask = 1;
        this.lastCheckTimeSpeed = 0L;
        this.diagnosticMessage = "";
        this.sendHistory = new NetChannelCycleHistory(64);
        this.maxTimeout = MAX_TIMEOUT_DEFAULT;
        this.receiveCountPackets = 0;
        this.bCheckTimeSpeed = false;
        this.checkT = new long[32];
        this.checkR = new long[32];
        this.checkI = -1;
        this.checkC = 0;
        this.swTbl = null;
        this.askMessageOut = new NetMsgFiltered();
        this.nakMessageOut = new NetMsgFiltered();
    }

    protected void controlStartInit() {
        this.created();
    }

    private void created() {
        this.connect.channelCreated(this);
    }

    protected void setMaxSendSpeed(double d) {
        this.maxSendSpeed = d;
        if ((this.id & 1) == 1) this.maxChSendSpeed = 2D * this.maxSendSpeed / 3D;
        else this.maxChSendSpeed = this.maxSendSpeed / 3D;
    }

    public int ping() {
        return this.ping;
    }

    public int pingTo() {
        return this.pingTo;
    }

    public int getMaxTimeout() {
        return this.maxTimeout;
    }

    public void setMaxTimeout(int i) {
        if (this.maxTimeout != MAX_TIMEOUT_DEFAULT && i < this.maxTimeout) if (this.lastMaxTimeoutSkipMessage == -1L || Time.current() > this.lastMaxTimeoutSkipMessage + SKIP_TIMEOUT_MESSAGE_INTERVAL) {
            this.lastMaxTimeoutSkipMessage = Time.current();
            System.out.println("Attempt to change max channel timeout from " + this.maxTimeout + "ms to " + i + "ms skipped for NetChannel id=" + this.id + ", remote id=" + this.remoteId + ", remote port=" + this.remotePort());
        }

        // TODO: Changed by Storebror, minimum Channel Timeout set to 1000ms.
        // if(i < 0)
        if (i < 1000) i = 1000;
        // ---
        if (i > 0x1ffff) i = 0x1ffff;
        if (this.maxTimeout != i) {
            this.maxTimeout = i;
            channelObj.doSetTimeout(this, i);
        }
    }

    public int getCurTimeout() {
        return (int) (Time.real() - this.lastPacketReceiveTime);
    }

    public int getCurTimeoutOk() {
        return (int) (Time.real() - this.lastPacketOkReceiveTime);
    }

    private boolean isTimeout() {
        if (!this.isRealTime()) return false;
        return this.getCurTimeout() >= this.getMaxTimeout();
    }

    public boolean isRequeredSendPacket(long l) {
        return l > this.nextPacketSendTime;
    }

    protected int getSendPacketLen(long l) {
        if (l < this.nextPacketSendTime) return 0;
        if (this.pingToSpeed > 0.1D) {
            this.curPacketSendSpeed = this.sendHistory.speed(this.nextPacketSendTime - this.ping, this.nextPacketSendTime, this.maxChSendSpeed);
            this.curPacketSendSpeed /= this.pingToSpeed + 1.0D;
            this.lastDownSendTime = l;
            this.lastDownSendSpeed = this.curPacketSendSpeed;
        } else if (this.pingToSpeed < -0.1D) {
            this.lastDownSendTime = l;
            this.lastDownSendSpeed = this.curPacketSendSpeed;
        } else if (this.ping > 0 && this.lastDownSendTime > 0L) {
            this.curPacketSendSpeed = this.maxChSendSpeed - (this.maxChSendSpeed - this.lastDownSendSpeed) * Math.exp(-(l - this.lastDownSendTime) / this.ping);
            // TODO: Fixed by SAS~Storebror: Something seems wrong with the down time calculation...
//           if (this.curPacketSendSpeed == 0.99D * this.maxChSendSpeed) this.lastDownSendTime = 0L;
            if (this.curPacketSendSpeed >= 0.99D * this.maxChSendSpeed) this.lastDownSendTime = 0L;
        } else this.curPacketSendSpeed = this.maxChSendSpeed;
        if (this.curPacketSendSpeed < 0.1D) this.curPacketSendSpeed = 0.1D;
        double d = this.pingTo * 2 / 3;
        if (d < 10D) d = 10D;
        double d1 = this.curPacketSendSpeed * d;
        int i = this.socket.getHeaderSize();
        if (d1 < 256D + i) d1 = 256D + i;
        if (d1 > this.socket.getMaxDataSize() + i) d1 = this.socket.getMaxDataSize() + i;
        return (int) d1 - i;
    }

    private void printDouble(double d) {
        if (d < 0.0D) {
            d = -d;
            System.out.print('-');
        } else System.out.print('+');
        System.out.print((int) d + ".");
        int i = (int) ((d - (int) d) * 100D);
        if (i < 10) System.out.print("0");
        System.out.print(i);
    }

    protected void sendTime(NetMsgOutput netmsgoutput, int i, long l, int j) throws IOException {
        int k = this.socket.getHeaderSize();
        double d = (j + k) / this.curPacketSendSpeed;
        if (d < 10D) d = 10D;
        this.nextPacketSendTime = l + (long) d;
        this.lastPacketSendTime = l;
        if (this.isRealTime()) this.sendHistory.put(i, j + k, l);
        else return;
        netmsgoutput.writeShort((int) l & 0xffff);
        int i1 = (int) (l - this.lastPacketReceiveTime);
        netmsgoutput.writeShort(i1 & 0xffff);
        int j1 = this.lastPacketReceiveSequenceNum & 0x3fff;
        j1 |= ((int) l >> 16 & 1) << 14;
        j1 |= (i1 >> 16 & 1) << 15;
        netmsgoutput.writeShort(j1);
    }

    public long remoteClockOffset() {
        if (this.receiveCountPackets > TIME_OFFSET_SUM) return this.remoteClockOffsetSum / 256L;
        if (this.receiveCountPackets > 0) return this.remoteClockOffsetSum / this.receiveCountPackets;
        else return 0L;
    }

    private long receiveTime(long l, NetMsgInput netmsginput, int i, boolean flag, boolean flag1) throws IOException {
        if (!flag) return Time.currentReal();
        long l1 = l;
        int j = netmsginput.readUnsignedShort();
        int k = netmsginput.readUnsignedShort();
        int i1 = netmsginput.readUnsignedShort();
        if (flag1) return l;
        j |= (i1 >> 14 & 1) << 16;
        k |= (i1 >> 15 & 1) << 16;
        i1 &= 0x3fff;
        long l2;
        for (l2 = this.lastPacketReceiveRemoteTime & 0xfffffffffffe0000L | j; l2 < this.lastPacketReceiveRemoteTime; l2 += 0x20000L)
            ;
        int j1 = this.sendHistory.getIndex(i1);
        if (j1 >= 0) {
            this.receiveCountPackets++;
            long l3 = this.sendHistory.getTime(j1);
            int k1 = (int) (l - l3) - k;
            if (k1 < 0) {
                k1 = 0;
                k = (int) (l - l3);
            }
            long l4 = (l + l3) / 2L - (l2 - k / 2);
            if (this.receiveCountPackets > TIME_OFFSET_SUM) {
                this.remoteClockOffsetSum = this.remoteClockOffsetSum * 255L / 256L + l4;
                l4 = this.remoteClockOffsetSum / 256L;
                long l5 = l - (l2 + l4);
                if (l5 < 0L) {
                    l4 = l - l2;
                    this.remoteClockOffsetSum = 256L * l4;
                }
                if (l5 > k1) {
                    l4 = l - k1 - l2;
                    this.remoteClockOffsetSum = 256L * l4;
                }
            } else {
                this.remoteClockOffsetSum += l4;
                l4 = this.remoteClockOffsetSum / this.receiveCountPackets;
                long l6 = l - (l2 + l4);
                if (l6 < 0L) {
                    l4 = l - l2;
                    this.remoteClockOffsetSum = this.receiveCountPackets * l4;
                }
                if (l6 > k1) {
                    l4 = l - k1 - l2;
                    this.remoteClockOffsetSum = this.receiveCountPackets * l4;
                }
            }
            l1 = l2 + l4;
            long l7 = l - l1;
            int i2 = k1 - (int) l7;
            int j2 = this.pingTo;
            if (this.receiveCountPackets > TIME_PING_SUM_START) {
                int k2 = TIME_PING_SUM_START;
                if (this.ping > 0) {
                    k2 = (TIME_PING_SUM + this.ping / 2) / this.ping;
                    if (k2 < TIME_PING_SUM_START) k2 = TIME_PING_SUM_START;
                }
                this.pingSum = this.pingSum * (k2 - 1) / this.countPingSum + k1;
                this.pingToSum = this.pingToSum * (k2 - 1) / this.countPingSum + i2;
                this.countPingSum = k2;
            } else {
                this.pingSum += k1;
                this.pingToSum += i2;
                this.countPingSum = this.receiveCountPackets;
            }
            this.ping = this.pingSum / this.countPingSum;
            this.pingTo = this.pingToSum / this.countPingSum;
            if (this.receiveCountPackets > 1 && l > this.lastPacketReceiveTime) {
                this.pingToSpeed = (double) (this.pingTo - j2) / (double) (l - this.lastPacketReceiveTime);
                if (this.bCheckTimeSpeed) if (this.checkI < 32) {
                    if (this.checkI < 0 || l - this.checkT[this.checkI] > 1000L) {
                        this.checkI++;
                        this.checkT[this.checkI & 0x1f] = l;
                        this.checkR[this.checkI & 0x1f] = l2 + this.ping - this.pingTo;
                    }
                } else if (l - this.checkT[this.checkI & 0x1f] > 1000L) {
                    this.checkI++;
                    long l8 = this.checkT[this.checkI & 0x1f];
                    long l9 = this.checkR[this.checkI & 0x1f];
                    long l10 = l2 + this.ping - this.pingTo;
                    this.checkT[this.checkI & 0x1f] = l;
                    this.checkR[this.checkI & 0x1f] = l10;
                    double d = Math.abs(1.0D - ((double) l10 - (double) l9) / ((double) l - (double) l8));
                    if (d > checkTimeSpeedDifferense) this.checkC++;
                    else this.checkC = 0;
                }
            }
        }
        this.lastPacketReceiveTime = l;
        this.lastPacketReceiveRemoteTime = l2;
        this.lastPacketReceiveSequenceNum = i;
        return l1;
    }

    private void cdata(byte abyte0[], int i, int j) {
        if (this.swTbl == null) return;
        int k = j % this.swTbl.length;
        if (i + j > abyte0.length) j = abyte0.length - i;
        while (j > 0) {
            abyte0[i] = (byte) (abyte0[i] ^ this.swTbl[k]);
            i++;
            j--;
            k = (k + 1) % this.swTbl.length;
        }
    }

    public double getMaxSpeed() {
        return this.maxSendSpeed;
    }

    public void setMaxSpeed(double d) {
        if (d == this.maxSendSpeed) return;
        if (this.socket.maxChannels == 0) {
            this.setMaxSendSpeed(d);
            this.socket.setMaxSpeed(d);
            channelObj.doSetSpeed(this, d);
            return;
        }
        if (d > this.socket.getMaxSpeed()) {
            d = this.socket.getMaxSpeed();
            if (d == this.maxSendSpeed) return;
        }
        this.setMaxSendSpeed(d);
        channelObj.doSetSpeed(this, d);
    }

    protected static void destroyNetObj(NetObj netobj) {
        if (netobj == null) return;
        try {
            Object obj = netobj;
            Object obj1 = netobj.superObj();
            if (obj1 != null && obj1 instanceof Destroy) {
                obj = obj1;
                if (!((Destroy) obj).isDestroyed()) ((Destroy) obj).destroy();
            } else if (!((Destroy) obj).isDestroyed()) ((Destroy) obj).destroy();
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    protected static void classInit() {
        if (bClassInited) return;
        else {
            channelObj = new ChannelObj(5);
            askMessage = new AskMessage(1);
            destroyMessage = new DestroyMessage(4);
            nakMessage = new NakMessage(2);
            spawnMessage = new SpawnMessage(3);
            bClassInited = true;
            return;
        }
    }

    // TODO: Added by Storebror, call otherwise unused functions in order to prevent compiler warnings.
    void dummy() {
        winDownDelta(0, 1, 2, 3);
        winUpDelta(0, 1, 2, 3);
        this.doCheckTimeSpeed();
        this.printDouble(0D);
        if (DEBUG) if (destroyMessage == null) {}
    }

    private long              lastMaxTimeoutSkipMessage     = -1L;
    private static final long SKIP_TIMEOUT_MESSAGE_INTERVAL = 10000;
    private static final int  MAX_TIMEOUT_DEFAULT           = 0x1FFFF;
    // ...

    private static final boolean             DEBUG                    = false;
    public static boolean                    bCheckServerTimeSpeed    = true;
    public static boolean                    bCheckClientTimeSpeed    = false;
    public static int                        checkTimeSpeedInterval   = 17;
    public static double                     checkTimeSpeedDifferense = 0.2D;
    protected int                            id;
    protected int                            remoteId;
    protected NetSocket                      socket;
    protected NetAddress                     remoteAddress;
    protected int                            remotePort;
    protected HashMapInt                     objects;
    protected HashMapExt                     mirrored;
    protected ArrayList                      filters;
    public static final int                  REAL_TIME                = 1;
    public static final int                  PUBLIC                   = 2;
    public static final int                  GLOBAL                   = 4;
    protected int                            flags;
    public static final int                  STATE_DESTROYED          = 0x80000000;
    public static final int                  STATE_DO_DESTROY         = 0x40000000;
    public static final int                  STATE_READY              = 0;
    public static final int                  STATE_INITMASK           = 0x3fffffff;
    public int                               userState;
    protected int                            state;
    private int                              initStamp;
    private boolean                          bSortGuaranted;
    public NetChannelStat                    stat;
    private long                             timeDestroyed;
    protected static final int               MESSAGE_SEQUENCE_FULL    = 65535;
    private static final int                 MESSAGE_SEQUENCE_FRAME   = 1023;
    private static final int                 PACKET_SEQUENCE_FULL     = 16383;
    private static NetMsgOutput              _tmpOut                  = new NetMsgOutput();
    private ArrayList                        holdGMsgs;
    protected NetChannelArrayList            sendGMsgs;
    private int                              sendGMsgSequenceNum;
    protected HashMapInt                     receiveGMsgs;
    private int                              receiveGMsgSequenceNum;
    private int                              receiveGMsgSequenceNumPosted;
    private long                             lastTimeNakMessageSend;
    private static int                       firstIndxSendGMsgs;
    private static int                       sequenceNumSendGMsgs;
    protected static int                     guarantedSizeMsgs;
    private HashMapExt                       filteredTickMsgs;
    protected static ArrayList               filteredSortMsgs         = new ArrayList();
    private static int                       filteredSizeMsgs;
    protected static int                     filteredMinSizeMsgs;
    public byte                              crcInit[]                = { 65, 3 };
    protected int                            sendSequenceNum;
    private int                              receiveSequenceNum;
    private int                              receiveSequenceMask;
    protected static long                    getMessageTime;
    protected static NetObj                  getMessageObj;
    protected static int                     getMessageObjIndex;
    private long                             lastCheckTimeSpeed;
    private NetConnect                       connect;
    private String                           diagnosticMessage;
    private static NetChannelPriorComparator priorComparator          = new NetChannelPriorComparator();
    private static NetChannelTimeComparator  timeComparator           = new NetChannelTimeComparator();
    public int                               statNumSendGMsgs;
    public int                               statSizeSendGMsgs;
    public int                               statNumSendFMsgs;
    public int                               statSizeSendFMsgs;
    public int                               statHSizeSendFMsgs;
    public int                               statNumFilteredMsgs;
    public int                               statSizeFilteredMsgs;
    public int                               statNumReseivedMsgs;
    public int                               statSizeReseivedMsgs;
    public int                               statHSizeReseivedMsgs;
    private static final int                 TIME_OFFSET_SUM          = 256;
    private static final int                 TIME_PING_SUM_START      = 4;
    private static final int                 TIME_PING_SUM            = 2000;
    protected static final double            MIN_SPEED_SEND           = 0.10000000000000001D;
    protected static final double            MIN_TIME_SEND            = 10D;
    protected static final double            MIN_LEN_SEND             = 256D;
    protected double                         maxSendSpeed;
    protected double                         maxChSendSpeed;
    NetChannelCycleHistory                   sendHistory;
    private int                              maxTimeout;
    protected long                           lastPacketSendTime;
    protected long                           nextPacketSendTime;
    private int                              ping;
    private int                              pingTo;
    private double                           pingToSpeed;
    private long                             lastDownSendTime;
    private double                           lastDownSendSpeed;
    private double                           curPacketSendSpeed;
    private long                             lastPacketReceiveTime;
    private long                             lastPacketOkReceiveTime;
    private int                              lastPacketReceiveSequenceNum;
    private long                             lastPacketReceiveRemoteTime;
    private long                             remoteClockOffsetSum;
    private int                              receiveCountPackets;
    private int                              pingSum;
    private int                              pingToSum;
    private int                              countPingSum;
    private boolean                          bCheckTimeSpeed;
    private long                             checkT[];
    private long                             checkR[];
    private int                              checkI;
    private int                              checkC;
    public byte                              swTbl[];
    private static ChannelObj                channelObj;
    private static SpawnMessage              spawnMessage;
    private static DestroyMessage            destroyMessage;
    private static AskMessage                askMessage;
    private NetMsgFiltered                   askMessageOut;
    private static NakMessage                nakMessage;
    private NetMsgFiltered                   nakMessageOut;
    private static boolean                   bClassInited             = false;
}
