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
                if (netmsgfiltered.isLocked())
                    netmsgfiltered.unLock(netchannel);
                netmsgfiltered.clear();
                netmsgfiltered.prior = 1.1F;
                netmsgfiltered.writeShort(i);
                netmsgfiltered.writeByte(j - 1);
                postRealTo(Time.currentReal(), netchannel, netmsgfiltered);
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
                if (netmsgfiltered.isLocked())
                    netmsgfiltered.unLock(netchannel);
                netmsgfiltered.clear();
                netmsgfiltered.prior = 1.1F;
                netmsgfiltered.writeShort(i);
                postRealTo(Time.currentReal(), netchannel, netmsgfiltered);
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
            if (netmsginput.channel().isDestroying())
                return;
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
                postTo(netchannel, netmsgguaranted);
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public void doSetTimeout(NetChannel netchannel, int i) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(4);
                netmsgguaranted.writeInt(i);
                postTo(netchannel, netmsgguaranted);
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
            }
        }

        public void doDestroy(NetChannel netchannel) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(0);
                netmsgguaranted.write255(netchannel.diagnosticMessage);
                postTo(netchannel, netmsgguaranted);
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
                if ((netchannel.flags & 4) != 0)
                    if (NetEnv.cur().control == null) {
                        new NetControlLock(netchannel);
                        netmsgguaranted.writeByte(0);
                    } else {
                        netmsgguaranted.writeByte(1);
                    }
                postTo(netchannel, netmsgguaranted);
                if ((netchannel.flags & 4) == 0)
                    netchannel.controlStartInit();
            } catch (Exception exception) {
                NetChannel.printDebug(exception);
                netchannel.destroy();
            }
        }

        public void msgNet(NetMsgInput netmsginput) {
            NetChannel netchannel;
            netchannel = netmsginput.channel();
            if (netchannel.isDestroying())
                return;
            try {
                byte byte0 = netmsginput.readByte();
                switch (byte0) {
                    default:
                        break;

                    case MSG_DESTROY: // '\0'
                        if (netmsginput.available() > 0)
                            netchannel.destroy(netmsginput.read255());
                        else
                            netchannel.destroy();
                        break;

                    case MSG_REQUEST_CREATING: // '\001'
                        int i = netmsginput.readByte();
                        double d = (double) netmsginput.readInt() / 1000D;
                        netchannel.flags = i;
                        if ((i & 4) != 0) {
                            boolean flag = netmsginput.readByte() != 0;
                            if (flag) {
                                if (NetEnv.cur().control == null) {
                                    new NetControlLock(netchannel);
                                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                                    netmsgguaranted.writeByte(2);
                                    postTo(netchannel, netmsgguaranted);
                                } else {
                                    if (NetEnv.cur().control instanceof NetControlLock)
                                        netchannel.destroy("Remote control slot is locked.");
                                    else
                                        netchannel.destroy("Only TREE network structure is supported.");
                                    break;
                                }
                            } else {
                                if (NetEnv.cur().control == null) {
                                    new NetControl(null);
                                } else {
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
                        } else {
                            netchannel.controlStartInit();
                        }
                        if (d > netchannel.maxSendSpeed)
                            doSetSpeed(netchannel, netchannel.maxSendSpeed);
                        else
                            netchannel.setMaxSendSpeed(d);
                        break;

                    case MSG_ASK_CREATING: // '\002'
                        MsgNet.postRealNewChannel(NetEnv.cur().control, netchannel);
                        break;

                    case MSG_SET_SPEED: // '\003'
                        double d1 = (double) netmsginput.readInt() / 1000D;
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
        return id;
    }

    public int remoteId() {
        return remoteId;
    }

    public NetSocket socket() {
        return socket;
    }

    public NetAddress remoteAddress() {
        return remoteAddress;
    }

    public int remotePort() {
        return remotePort;
    }

    public NetObj getMirror(int i) {
        return (NetObj) (NetObj) objects.get(i);
    }

    public boolean isInitRemote() {
        return (id & 1) == 1;
    }

    public boolean isMirrored(NetObj netobj) {
        return mirrored.containsKey(netobj);
    }

    public void setMirrored(NetObj netobj) {
        if (!mirrored.containsKey(netobj)) {
            mirrored.put(netobj, null);
            netobj.countMirrors++;
        }
    }

    public boolean isRealTime() {
        return (flags & 1) != 0;
    }

    public boolean isPublic() {
        return (flags & 2) != 0;
    }

    public boolean isGlobal() {
        return (flags & 4) != 0;
    }

    public void setInitStamp(int i) {
        initStamp = i;
    }

    public int getInitStamp() {
        return initStamp;
    }

    public int state() {
        return state;
    }

    public void setStateInit(int i) {
        state = state & 0xc0000000 | i & 0x3fffffff;
    }

    public boolean isReady() {
        return state == 0;
    }

    public boolean isIniting() {
        return (state & 0x3fffffff) != 0;
    }

    public boolean isDestroyed() {
        return state < 0;
    }

    public boolean isDestroying() {
        return (state & 0xc0000000) != 0;
    }

    public boolean isSortGuaranted() {
        return bSortGuaranted;
    }

    public void startSortGuaranted() {
        bSortGuaranted = true;
    }

    public List filters() {
        return filters;
    }

    public void filterAdd(NetFilter netfilter) {
        int i = filters.size();
        for (int j = 0; j < i; j++) {
            NetFilter netfilter1 = (NetFilter) filters.get(j);
            if (!netfilter1.filterEnableAdd(this, netfilter))
                return;
        }

        filters.add(netfilter);
    }

    public void filterRemove(NetFilter netfilter) {
        int i = filters.indexOf(this);
        if (i >= 0)
            filters.remove(i);
    }

    protected void statIn(boolean flag, NetObj netobj, NetMsgInput netmsginput) {
        if (stat == null)
            return;
        byte byte0 = 0;
        byte byte1 = 0;
        int i = 0;
        if (netmsginput.buf != null) {
            i = netmsginput.available();
            byte0 = i <= 0 ? 0 : netmsginput.buf[0];
            byte1 = i <= 1 ? 0 : netmsginput.buf[1];
        }
        try {
            stat.inc(this, netobj, flag, false, i, byte0, byte1);
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    protected void statOut(boolean flag, NetObj netobj, NetMsgOutput netmsgoutput) {
        if (stat == null)
            return;
        byte byte0 = 0;
        byte byte1 = 0;
        int i = 0;
        if (netmsgoutput.buf != null) {
            i = netmsgoutput.size();
            byte0 = i <= 0 ? 0 : netmsgoutput.buf[0];
            byte1 = i <= 1 ? 0 : netmsgoutput.buf[1];
        }
        try {
            stat.inc(this, netobj, flag, true, i, byte0, byte1);
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    public void destroy(String s) {
        if ((state & 0xc0000000) != 0) {
            return;
        } else {
            diagnosticMessage = s;
            destroy();
            return;
        }
    }

    public void destroy() {
        if ((state & 0xc0000000) != 0)
            return;
        timeDestroyed = Time.currentReal() + 1000L;
        state |= 0x40000000;
        if ((state & 0x3fffffff) == 0)
            connect.channelDestroying(this, diagnosticMessage);
        do {
            if (objects.isEmpty())
                break;
            HashMapIntEntry hashmapintentry = objects.nextEntry(null);
            if (hashmapintentry == null)
                break;
            NetObj netobj = (NetObj) hashmapintentry.getValue();
            int i = hashmapintentry.getKey();
            destroyNetObj(netobj);
            if (objects.containsKey(i))
                objects.remove(i);
        } while (true);
        do {
            if (mirrored.isEmpty())
                break;
            java.util.Map.Entry entry = mirrored.nextEntry(null);
            if (entry == null)
                break;
            NetObj netobj1 = (NetObj) entry.getKey();
            netobj1.countMirrors--;
            mirrored.remove(netobj1);
            MsgNet.postRealDelChannel(netobj1, this);
        } while (true);
        HashMapInt hashmapint = NetEnv.cur().objects;
        for (HashMapIntEntry hashmapintentry1 = hashmapint.nextEntry(null); hashmapintentry1 != null; hashmapintentry1 = hashmapint.nextEntry(hashmapintentry1)) {
            NetObj netobj2 = (NetObj) hashmapintentry1.getValue();
            if (netobj2.isCommon())
                MsgNet.postRealDelChannel(netobj2, this);
        }

        channelObj.doDestroy(this);
    }

    protected boolean update() {
        if (state < 0)
            return false;
        if ((state & 0x40000000) != 0 && Time.currentReal() > timeDestroyed) {
            if ((state & 0x3fffffff) != 0) {
                connect.channelNotCreated(this, diagnosticMessage);
                if (NetEnv.cur().control != null && (NetEnv.cur().control instanceof NetControlLock)) {
                    NetControlLock netcontrollock = (NetControlLock) NetEnv.cur().control;
                    if (netcontrollock.channel() == this)
                        netcontrollock.destroy();
                }
            }
            state = 0x80000000;
            clearSortGuaranted();
            clearSendGMsgs();
            clearReceivedGMsgs();
            clearSendFMsgs();
            return false;
        }
        flushReceivedGuarantedMsgs();
        if (isTimeout())
            destroy("Timeout.");
        return true;
    }

    private static boolean winLT(int i, int j, int k) {
        if (i == j)
            return false;
        if (i > j)
            return i - j < k / 2;
        else
            return i + ((k - j) + 1) < k / 2;
    }

    private static boolean winDownDelta(int i, int j, int k, int l) {
        if (k <= i)
            i -= k;
        else
            i = (l - (k - i)) + 1;
        if (j == i)
            return true;
        else
            return winLT(j, i, l);
    }

    private static boolean winUpDelta(int i, int j, int k, int l) {
        if (k <= j)
            j -= k;
        else
            j = (l - (k - j)) + 1;
        if (j == i)
            return true;
        else
            return winLT(i, j, l);
    }

    private static int winDeltaLT(int i, int j, int k) {
        if (j <= i)
            return i - j;
        else
            return i + ((k - j) + 1);
    }

    protected void putMessageSpawn(NetMsgSpawn netmsgspawn) throws IOException {
        if (state < 0)
            throw new NetException("Channel is destroyed");
        if (netmsgspawn.size() > 255)
            throw new IOException("Output message is very long");
        if ((state & 0x40000000) != 0)
            throw new NetException("Channel is closed for spawning objects");
        if (bSortGuaranted && !isReferenceOk(netmsgspawn)) {
            holdGMsg(netmsgspawn);
            return;
        } else {
            setGMsg(netmsgspawn, 3);
            mirrored.put(netmsgspawn._sender, null);
            netmsgspawn._sender.countMirrors++;
            return;
        }
    }

    protected void putMessageDestroy(NetMsgDestroy netmsgdestroy) throws IOException {
        if (state < 0)
            throw new NetException("Channel is destroyed");
        if (netmsgdestroy.size() > 255)
            throw new IOException("Output message is very long");
        if (bSortGuaranted && !isReferenceOk(netmsgdestroy)) {
            holdGMsg(netmsgdestroy);
            return;
        } else {
            setGMsg(netmsgdestroy, 4);
            mirrored.remove(netmsgdestroy._sender);
            netmsgdestroy._sender.countMirrors--;
            return;
        }
    }

    protected void putMessage(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (state < 0)
            throw new NetException("Channel is destroyed");
        if (netmsgguaranted.size() > 255)
            throw new IOException("Output message is very long");
        if (bSortGuaranted && !isReferenceOk(netmsgguaranted)) {
            holdGMsg(netmsgguaranted);
            return;
        }
        int i = getIndx(netmsgguaranted._sender);
        if (i == -1) {
            throw new NetException("Put Guaranted message to NOT mirrored object [" + netmsgguaranted._sender + "] (" + id() + ")");
        } else {
            setGMsg(netmsgguaranted, i);
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
                int l = getIndx(netobj);
             // +++ TODO: Storebror: Avoid excessive logging of net messages that are being tried to send to not mirrored objects +++
//                if (l == -1)
//                    throw new NetException("Put Guaranted message referenced to NOT mirrored object [" + netmsgguaranted._sender + "] -> [" + netobj + "] (" + id() + ")");
                if (l != -1)
               // --- TODO: Storebror: Avoid excessive logging of net messages that are being tried to send to not mirrored objects ---
                _tmpOut.writeShort(l);
            }

        }
        NetChannelGMsgOutput netchannelgmsgoutput = new NetChannelGMsgOutput();
        sendGMsgSequenceNum = sendGMsgSequenceNum + 1 & 0xffff;
        netchannelgmsgoutput.sequenceNum = sendGMsgSequenceNum;
        netchannelgmsgoutput.objIndex = i;
        netchannelgmsgoutput.iObjects = abyte0;
        netchannelgmsgoutput.timeLastSend = 0L;
        netchannelgmsgoutput.msg = netmsgguaranted;
        sendGMsgs.add(netchannelgmsgoutput);
        netmsgguaranted.lockInc();
        return netchannelgmsgoutput;
    }

    private boolean isReferenceOk(NetMsgGuaranted netmsgguaranted) {
        if (!(netmsgguaranted instanceof NetMsgSpawn) && !(netmsgguaranted instanceof NetMsgDestroy) && getIndx(netmsgguaranted._sender) == -1)
            return false;
        List list = netmsgguaranted.objects();
        if (list == null)
            return true;
        int i = list.size();
        for (int j = 0; j < i; j++) {
            NetObj netobj = (NetObj) list.get(j);
            if (getIndx(netobj) == -1)
                return false;
        }

        return true;
    }

    private void holdGMsg(NetMsgGuaranted netmsgguaranted) {
        holdGMsgs.add(netmsgguaranted);
        netmsgguaranted.lockInc();
    }

    private void flushHoldedGMsgs() throws IOException {
        for (boolean flag = true; flag;) {
            flag = false;
            int i = holdGMsgs.size();
            int j = 0;
            while (j < i) {
                NetMsgGuaranted netmsgguaranted = (NetMsgGuaranted) holdGMsgs.get(j);
                if (isReferenceOk(netmsgguaranted)) {
                    if (netmsgguaranted instanceof NetMsgSpawn)
                        putMessageSpawn((NetMsgSpawn) netmsgguaranted);
                    else if (netmsgguaranted instanceof NetMsgDestroy)
                        putMessageDestroy((NetMsgDestroy) netmsgguaranted);
                    else
                        putMessage(netmsgguaranted);
                    netmsgguaranted.lockDec();
                    flag = true;
                    holdGMsgs.remove(j);
                    j--;
                    i--;
                }
                j++;
            }
        }

    }

    public void stopSortGuaranted() throws IOException {
        if (!bSortGuaranted)
            return;
        flushHoldedGMsgs();
        bSortGuaranted = false;
        int i = holdGMsgs.size();
        if (i > 0) {
            System.err.println("Channel '" + id + "' cycled guaranted messages dump:");
            for (int j = 0; j < i; j++) {
                NetMsgGuaranted netmsgguaranted = (NetMsgGuaranted) holdGMsgs.get(j);
                netmsgguaranted.lockDec();
                if (netmsgguaranted.isRequiredAsk())
                    MsgNetAskNak.postReal(Time.currentReal(), netmsgguaranted._sender, false, netmsgguaranted, this);
                System.err.print(" " + netmsgguaranted.toString() + " (" + netmsgguaranted._sender.toString() + ") Data:");
                for (int k=0; k<netmsgguaranted.dataLength(); k++) System.err.print(" " + Integer.toHexString(netmsgguaranted.data()[k]));
                System.err.println();
            }

            holdGMsgs.clear();
            //return;
            throw new IOException("Cycled guaranted messages");
        } else {
            return;
        }
    }

    protected void clearSortGuaranted() {
        int i = holdGMsgs.size();
        for (int j = 0; j < i; j++) {
            NetMsgGuaranted netmsgguaranted = (NetMsgGuaranted) holdGMsgs.get(j);
            netmsgguaranted.lockDec();
            if (netmsgguaranted.isRequiredAsk())
                MsgNetAskNak.postReal(Time.currentReal(), netmsgguaranted._sender, false, netmsgguaranted, this);
        }

        holdGMsgs.clear();
    }

    protected void putMessage(NetMsgFiltered netmsgfiltered) throws IOException {
        if (state < 0)
            throw new NetException("Channel is destroyed");
        if (netmsgfiltered.size() > 255)
            throw new IOException("Output message is very long");
        if (getIndx(netmsgfiltered._sender) == -1)
            return;
        List list = netmsgfiltered.objects();
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; i--) {
                int j = getIndx((NetObj) list.get(i));
                if (j == -1)
                    return;
            }

        }
        filteredTickMsgs.put(netmsgfiltered, this);
        netmsgfiltered.lockInc();
    }

    private int getIndx(NetObj netobj) {
        if (netobj == null)
            return 0;
        int i = netobj.idLocal & 0x7fff;
        if (netobj.isMirror()) {
            if (netobj.masterChannel == this)
                i = netobj.idRemote;
            else
                i |= 0x8000;
        } else if (!netobj.isCommon())
            i |= 0x8000;
        if ((i & 0x8000) != 0 && !mirrored.containsKey(netobj))
            return -1;
        else
            return i;
    }

    protected boolean unLockMessage(NetMsgFiltered netmsgfiltered) {
        if (filteredTickMsgs.remove(netmsgfiltered) != null)
            netmsgfiltered.lockDec();
        return !netmsgfiltered.isLocked();
    }

    public int gSendQueueLenght() {
        return sendGMsgs.size();
    }

    public int gSendQueueSize() {
        int i = 0;
        int j = sendGMsgs.size();
        for (int k = 0; k < j; k++) {
            NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) sendGMsgs.get(k);
            i += netchannelgmsgoutput.msg.size();
        }

        return i;
    }

    private void askMessageReceive(int i) {
        int j = sendGMsgs.size();
        if (j == 0)
            return;
        if (j > MESSAGE_SEQUENCE_FRAME)
            j = MESSAGE_SEQUENCE_FRAME;
        NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) sendGMsgs.get(0);
        if (winLT(netchannelgmsgoutput.sequenceNum, i, 65535))
            return;
        netchannelgmsgoutput = (NetChannelGMsgOutput) sendGMsgs.get(j - 1);
        if (winLT(i, netchannelgmsgoutput.sequenceNum, 65535)) {
            System.err.println("Channel '" + id + "' reseived ask for NOT sended message " + i + " " + netchannelgmsgoutput.sequenceNum);
            return;
        }
        int k = 0;
        do {
            if (k >= j)
                break;
            netchannelgmsgoutput = (NetChannelGMsgOutput) sendGMsgs.get(k++);
            NetMsgGuaranted netmsgguaranted = netchannelgmsgoutput.msg;
            netmsgguaranted.lockDec();
            if (netmsgguaranted.isRequiredAsk())
                MsgNetAskNak.postReal(Time.currentReal(), netmsgguaranted._sender, true, netmsgguaranted, this);
            netchannelgmsgoutput.msg = null;
        } while (netchannelgmsgoutput.sequenceNum != i);
        sendGMsgs.removeRange(0, k);
    }

    protected void clearSendGMsgs() {
        int i = sendGMsgs.size();
        if (i == 0)
            return;
        for (int j = 0; j < i; j++) {
            NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) sendGMsgs.get(j);
            NetMsgGuaranted netmsgguaranted = netchannelgmsgoutput.msg;
            netmsgguaranted.lockDec();
            if (netmsgguaranted.isRequiredAsk())
                MsgNetAskNak.postReal(Time.currentReal(), netmsgguaranted._sender, false, netmsgguaranted, this);
        }

        sendGMsgs.clear();
    }

    private void nakMessageReceive(int i, int j) {
        int k = sendGMsgs.size();
        if (k == 0)
            return;
        if (k > MESSAGE_SEQUENCE_FRAME)
            k = MESSAGE_SEQUENCE_FRAME;
        int l = 0;
        for (NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) sendGMsgs.get(l); j > 0 && winLT(netchannelgmsgoutput.sequenceNum, i, 65535); i = i + 1 & 0xffff)
            j--;

        if (j == 0)
            return;
        do {
            if (k <= 0)
                break;
            NetChannelGMsgOutput netchannelgmsgoutput1 = (NetChannelGMsgOutput) sendGMsgs.get(l);
            if (netchannelgmsgoutput1.sequenceNum == i)
                break;
            k--;
            l++;
        } while (true);
        if (k == 0)
            return;
        while (k-- > 0 && j-- > 0) {
            NetChannelGMsgOutput netchannelgmsgoutput2 = (NetChannelGMsgOutput) sendGMsgs.get(l++);
            netchannelgmsgoutput2.timeLastSend = 0L;
        }
    }

    private void tryNakMessageSend() {
        if (receiveGMsgs.size() == 0)
            return;
        if (lastTimeNakMessageSend + (long) ((ping() * 3) / 2) > Time.currentReal())
            return;
        lastTimeNakMessageSend = Time.currentReal();
        int i = receiveGMsgSequenceNum + 1 & 0xffff;
        int j;
        for (j = 0; j < 256 && !receiveGMsgs.containsKey(i); j++)
            i = i + 1 & 0xffff;

        j--;
        nakMessage.send(receiveGMsgSequenceNum + 1 & 0xffff, j, this);
    }

    protected boolean receivedGuarantedMsg(NetMsgInput netmsginput, int i) {
        if (receiveGMsgSequenceNum == i)
            return true;
        if (winLT(receiveGMsgSequenceNum, i, 65535))
            return true;
        if ((receiveGMsgSequenceNum + 1 & 0xffff) == i) {
            receiveGMsgSequenceNum = i;
            if (getMessageObj != null && (receiveGMsgSequenceNumPosted + 1 & 0xffff) == i) {
                receiveGMsgSequenceNumPosted = i;
                postReceivedGMsg(Time.currentReal(), getMessageObj, netmsginput);
            } else {
                NetChannelGMsgInput netchannelgmsginput = new NetChannelGMsgInput();
                netchannelgmsginput.sequenceNum = i;
                netchannelgmsginput.objIndex = getMessageObjIndex;
                netchannelgmsginput.msg = netmsginput;
                receiveGMsgs.put(i, netchannelgmsginput);
            }
            if (getMessageObj == spawnMessage)
                addWaitSpawn(i, netmsginput);
            flushReceivedGuarantedMsgs();
            return true;
        }
        if (!receiveGMsgs.containsKey(i)) {
            NetChannelGMsgInput netchannelgmsginput1 = new NetChannelGMsgInput();
            netchannelgmsginput1.sequenceNum = i;
            netchannelgmsginput1.objIndex = getMessageObjIndex;
            netchannelgmsginput1.msg = netmsginput;
            receiveGMsgs.put(i, netchannelgmsginput1);
            if (getMessageObj == spawnMessage)
                addWaitSpawn(i, netmsginput);
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
            receiveGMsgs.put(i, netchannelgmsginput);
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    private void removeWaitSpawn(int i) {
        for (HashMapIntEntry hashmapintentry = null; (hashmapintentry = receiveGMsgs.nextEntry(hashmapintentry)) != null;) {
            NetChannelGMsgInput netchannelgmsginput = (NetChannelGMsgInput) hashmapintentry.getValue();
            if (netchannelgmsginput.objIndex == i && (netchannelgmsginput.sequenceNum & 0x10000) != 0) {
                receiveGMsgs.remove(netchannelgmsginput.sequenceNum);
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
            if (!isEnableFlushReceivedGuarantedMsgs() || receiveGMsgs.size() <= 0)
                break;
            NetChannelGMsgInput netchannelgmsginput = (NetChannelGMsgInput) receiveGMsgs.get(receiveGMsgSequenceNumPosted + 1 & 0xffff | 0x10000);
            if (netchannelgmsginput != null) {
                int i = netchannelgmsginput.objIndex;
                NetObj netobj1 = (NetObj) objects.get(i);
                if (netobj1 == null && isExistSpawnPosted(i))
                    break;
                receiveGMsgs.remove(netchannelgmsginput.sequenceNum);
                continue;
            }
            netchannelgmsginput = (NetChannelGMsgInput) receiveGMsgs.get(receiveGMsgSequenceNumPosted + 1 & 0xffff);
            if (netchannelgmsginput == null)
                break;
            NetObj netobj = null;
            int j = netchannelgmsginput.objIndex;
            if ((j & 0x8000) != 0) {
                j &= 0xffff7fff;
                netobj = (NetObj) objects.get(j);
                if (netobj == null && isExistSpawnPosted(j))
                    break;
            } else {
                netobj = (NetObj) NetEnv.cur().objects.get(j);
            }
            receiveGMsgSequenceNumPosted = netchannelgmsginput.sequenceNum;
            receiveGMsgs.remove(receiveGMsgSequenceNumPosted);
            if (netobj != null)
                postReceivedGMsg(l, netobj, netchannelgmsginput.msg);
            netchannelgmsginput.msg = null;
        } while (true);
    }

    protected void postReceivedGMsg(long l, NetObj netobj, NetMsgInput netmsginput) {
        statIn(false, netobj, netmsginput);
        MsgNet.postReal(l, netobj, netmsginput);
    }

    protected void clearReceivedGMsgs() {
        receiveGMsgs.clear();
    }

    private boolean isExistSpawnPosted(int i) {
        try {
            MessageQueue messagequeue = RTSConf.cur.queueRealTime;
            synchronized (messagequeue) {
                int j = 0;
                do {
                    Message message = messagequeue.peekByIndex(j++);
                    if (message == null)
                        break;
                    if ((message instanceof MsgNet) && message.listener() == spawnMessage && (message.sender() instanceof NetMsgInput)) {
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
        int i = sendGMsgs.size();
        if (i == 0)
            return 0;
        if (i > MESSAGE_SEQUENCE_FRAME)
            i = MESSAGE_SEQUENCE_FRAME;
        long l1 = l - (long) (2 * ping());
        int j = 0;
        do {
            if (j >= i)
                break;
            NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) sendGMsgs.get(j);
            if (l1 > netchannelgmsgoutput.timeLastSend) {
                sequenceNumSendGMsgs = netchannelgmsgoutput.sequenceNum;
                firstIndxSendGMsgs = j;
                break;
            }
            j++;
        } while (true);
        if (j == i)
            return 0;
        if (i > j + 128)
            i = j + 128;
        int k = 0;
        do {
            if (j >= i)
                break;
            NetChannelGMsgOutput netchannelgmsgoutput1 = (NetChannelGMsgOutput) sendGMsgs.get(j);
            if (l1 < netchannelgmsgoutput1.timeLastSend)
                break;
            computeMessageLen(netchannelgmsgoutput1.msg);
            k += netchannelgmsgoutput1.msg._len;
            j++;
        } while (true);
        return k;
    }

    protected int computeCountSendGMsgs(int i) {
        int j = sendGMsgs.size();
        if (j > MESSAGE_SEQUENCE_FRAME)
            j = MESSAGE_SEQUENCE_FRAME;
        int k = firstIndxSendGMsgs;
        int l = 0;
        guarantedSizeMsgs = 0;
        do {
            if (k >= j || i <= 0)
                break;
            NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) sendGMsgs.get(k++);
            i -= netchannelgmsgoutput.msg._len;
            if (i < 0 && l > 0)
                break;
            guarantedSizeMsgs += netchannelgmsgoutput.msg._len;
            l++;
        } while (true);
        return l;
    }

    protected int computeSizeSendFMsgs(long l) {
        filteredSizeMsgs = 0;
        filteredMinSizeMsgs = 0;
        for (java.util.Map.Entry entry = filteredTickMsgs.nextEntry(null); entry != null; entry = filteredTickMsgs.nextEntry(entry)) {
            NetMsgFiltered netmsgfiltered = (NetMsgFiltered) entry.getKey();
            computeMessageLen(netmsgfiltered, l);
            if (netmsgfiltered.prior > 1.0F) {
                netmsgfiltered._prior = netmsgfiltered.prior;
                filteredMinSizeMsgs += netmsgfiltered._len;
                filteredSizeMsgs += netmsgfiltered._len;
                filteredSortMsgs.add(netmsgfiltered);
                continue;
            }
            int i = filters.size();
            if (i > 0) {
                float f = -1F;
                for (int j = 0; j < i; j++) {
                    float f1 = ((NetFilter) filters.get(j)).filterNetMessage(this, netmsgfiltered);
                    if (f1 > 1.0F)
                        f1 = 1.0F;
                    if (f1 > f)
                        f = f1;
                }

                if (f < 0.0F)
                    netmsgfiltered._prior = netmsgfiltered.prior + 0.5F * ((float) Math.random() - 0.5F);
                else
                    netmsgfiltered._prior = f + 0.2F * ((float) Math.random() - 0.5F);
            } else {
                netmsgfiltered._prior = netmsgfiltered.prior + 0.5F * ((float) Math.random() - 0.5F);
            }
            if (netmsgfiltered._prior < 0.0F)
                netmsgfiltered._prior = 0.0F;
            if (netmsgfiltered._prior > 1.0F)
                netmsgfiltered._prior = 1.0F;
            filteredSizeMsgs += netmsgfiltered._len;
            filteredSortMsgs.add(netmsgfiltered);
        }

        filteredTickMsgs.clear();
        return filteredSizeMsgs;
    }

    protected void clearSendFMsgs() {
        for (java.util.Map.Entry entry = filteredTickMsgs.nextEntry(null); entry != null; entry = filteredTickMsgs.nextEntry(entry)) {
            NetMsgFiltered netmsgfiltered = (NetMsgFiltered) entry.getKey();
            netmsgfiltered.lockDec();
        }

        filteredTickMsgs.clear();
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
        if (filteredSizeMsgs == 0)
            return 0;
        Collections.sort(filteredSortMsgs, timeComparator);
        int k = filters.size();
        if (k > 0) {
            int i1 = filteredSortMsgs.size();
            for (int j1 = 0; j1 < k; j1++) {
                NetFilter netfilter = (NetFilter) filters.get(j1);
                for (int k1 = 0; k1 < i1; k1++)
                    netfilter.filterNetMessagePosting(this, (NetMsgFiltered) filteredSortMsgs.get(k1));

            }

        }
        return filteredSizeMsgs;
    }

    protected boolean sendPacket(NetMsgOutput netmsgoutput, NetPacket netpacket) {
        long l = Time.real();
        if (isTimeout()) {
            destroy("Timeout.");
            return false;
        }
        boolean flag = false;
        tryNakMessageSend();
        int i = getSendPacketLen(l);
        if (l >= lastPacketSendTime + (long) (maxTimeout / 4)) {
            if (i < 20)
                i = 20;
            flag = true;
        }
        if (i <= 0)
            return false;
        int j = 6;
        if (isRealTime())
            j += 6;
        i -= j;
        if (i <= 0)
            return false;
        int k = 0;
        int i1 = computeSizeSendGMsgs(l);
        if (i1 > 0) {
            i -= 3;
            if (swTbl != null)
                i--;
        }
        if (i <= 0)
            return false;
        int j1 = computeSizeSendFMsgs(l);
        if (j1 + i1 == 0 && !flag)
            return false;
        if (j1 + i1 > i) {
            int k1 = j1;
            int j2 = i1;
            if (j2 > i)
                j2 = i;
            k1 = i - j2;
            if (k1 < filteredMinSizeMsgs)
                k1 = filteredMinSizeMsgs;
            j2 = i - k1;
            if (j2 < 0) {
                if (i1 > 0) {
                    i += 3;
                    if (swTbl != null)
                        i++;
                }
                j2 = 0;
            }
            if (j2 > 0 && j2 < i1) {
                k = computeCountSendGMsgs(j2);
                j2 = guarantedSizeMsgs;
                if (i - j2 > k1)
                    k1 = i - j2;
            } else {
                k = computeCountSendGMsgs(j2);
                j2 = guarantedSizeMsgs;
            }
            j1 = k1;
            i1 = j2;
        } else {
            k = computeCountSendGMsgs(i1);
        }
        j1 = fillFilteredArrayMessages(l, j1);
        if (j1 + i1 == 0 && !flag)
            return false;
        try {
            netmsgoutput.clear();
            netmsgoutput.writeShort(remoteId);
            netmsgoutput.writeByte(crcInit[0]);
            netmsgoutput.writeByte(crcInit[1]);
            sendSequenceNum = sendSequenceNum + 1 & 0x3fff;
            if (k > 0)
                sendSequenceNum |= 0x8000;
            if (isRealTime()) {
                netmsgoutput.writeShort(sendSequenceNum | 0x4000);
                sendTime(netmsgoutput, sendSequenceNum, l, j + j1 + i1);
            } else {
                netmsgoutput.writeShort(sendSequenceNum);
                sendTime(netmsgoutput, sendSequenceNum, l, j + j1 + i1);
            }
            if (k > 0) {
                netmsgoutput.writeShort(sequenceNumSendGMsgs);
                netmsgoutput.writeByte(k - 1);
                if (swTbl != null)
                    netmsgoutput.writeByte(0);
                int l1 = netmsgoutput.dataLength();
                int k2 = firstIndxSendGMsgs;
                while (k-- > 0) {
                    NetChannelGMsgOutput netchannelgmsgoutput = (NetChannelGMsgOutput) sendGMsgs.get(k2++);
                    netchannelgmsgoutput.timeLastSend = l;
                    putMessage(netmsgoutput, netchannelgmsgoutput.objIndex, netchannelgmsgoutput.msg, netchannelgmsgoutput.iObjects);
                }
                if (swTbl != null) {
                    int j3 = netmsgoutput.dataLength();
                    int k3 = j3 - l1;
                    if (k3 > 0) {
                        if (k3 > 255)
                            k3 = 255;
                        netmsgoutput.data()[l1 - 1] = (byte) k3;
                        cdata(netmsgoutput.data(), l1, k3);
                    }
                }
            }
            int i2 = filteredSortMsgs.size();
            for (int l2 = 0; l2 < i2; l2++) {
                NetMsgFiltered netmsgfiltered = (NetMsgFiltered) filteredSortMsgs.get(l2);
                putMessage(netmsgoutput, l, netmsgfiltered);
                netmsgfiltered.lockDec();
            }

            filteredSortMsgs.clear();
            int i3 = CRC16.checksum(0, netmsgoutput.data(), 0, netmsgoutput.dataLength());
            netmsgoutput.data()[2] = (byte) (i3 >>> 8 & 0xff);
            netmsgoutput.data()[3] = (byte) (i3 & 0xff);
            netpacket.setLength(netmsgoutput.dataLength());
            netpacket.setAddress(remoteAddress);
            netpacket.setPort(remotePort);
            socket.send(netpacket);
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
        abyte0[j + 2] = crcInit[0];
        abyte0[j + 3] = crcInit[1];
        int i1 = CRC16.checksum(0, abyte0, j, k);
        if (i != i1)
            return false;
        if (isTimeout()) {
            destroy("Timeout.");
            return true;
        }
        i = netmsginput.readUnsignedShort();
        boolean flag = false;
        boolean flag1 = (i & 0x8000) != 0;
        boolean flag2 = true;
        boolean flag3 = (i & 0x4000) == 0 ? false : true;
        i &= 0x3fff;
        if (receiveSequenceNum == i)
            return true;
        if (winLT(receiveSequenceNum, i, PACKET_SEQUENCE_FULL)) {
            if (!flag1)
                return true;
            int j1 = winDeltaLT(receiveSequenceNum, i, PACKET_SEQUENCE_FULL);
            if (j1 > 31)
                return true;
            if ((1 << j1 & receiveSequenceMask) != 0)
                return true;
            flag2 = false;
            flag = true;
        } else {
            for (; receiveSequenceNum != i; receiveSequenceNum = receiveSequenceNum + 1 & 0x3fff)
                receiveSequenceMask <<= 1;

            receiveSequenceMask |= 1;
        }
        long l1 = receiveTime(l, netmsginput, i, flag3, flag);
        if (flag1) {
            int k1 = netmsginput.readUnsignedShort();
            int i2 = netmsginput.readUnsignedByte() + 1;
            if (swTbl != null) {
                int j2 = netmsginput.readUnsignedByte();
                cdata(netmsginput.buf, netmsginput.pos, j2);
            }
            boolean flag4 = false;
            while (i2-- > 0 && netmsginput.available() > 0) {
                NetMsgInput netmsginput2 = getMessage(netmsginput);
                if (receivedGuarantedMsg(netmsginput2, k1))
                    flag4 = true;
                k1 = k1 + 1 & 0xffff;
            }
            if (flag4)
                askMessage.send(receiveGMsgSequenceNum, this);
        }
        boolean flag5 = true;
        if (flag2)
            while (netmsginput.available() > 0) {
                NetMsgInput netmsginput1 = getMessage(netmsginput, l1);
                if (netmsginput1 != null) {
                    statIn(true, getMessageObj, netmsginput1);
                    MsgNet.postReal(getMessageTime, getMessageObj, netmsginput1);
                } else {
                    flag5 = false;
                }
            }
        if (flag5)
            lastPacketOkReceiveTime = lastPacketReceiveTime;
        if (checkC >= checkTimeSpeedInterval)
            destroy("Timeout .");
        return true;
    }

    protected void putMessage(NetMsgOutput netmsgoutput, int i, NetMsgGuaranted netmsgguaranted, byte abyte0[]) throws IOException {
        statOut(false, netmsgguaranted._sender, netmsgguaranted);
        netmsgoutput.writeShort(i);
        netmsgoutput.writeByte(netmsgguaranted.size());
        if (netmsgguaranted.dataLength() > 0)
            netmsgoutput.write(netmsgguaranted.data(), 0, netmsgguaranted.dataLength());
        if (abyte0 != null)
            netmsgoutput.write(abyte0, 0, abyte0.length);
        statNumSendGMsgs++;
        statSizeSendGMsgs += netmsgguaranted.size();
    }

    protected void putMessage(NetMsgOutput netmsgoutput, long l, NetMsgFiltered netmsgfiltered) throws IOException {
        putMessage(netmsgoutput, l, netmsgfiltered, netmsgfiltered._time);
    }

    protected void putMessage(NetMsgOutput netmsgoutput, long l, NetMsgFiltered netmsgfiltered, long l1) throws IOException {
        statOut(true, netmsgfiltered._sender, netmsgfiltered);
        netmsgoutput.writeShort(getIndx(netmsgfiltered._sender));
        statHSizeSendFMsgs += 2;
        int k = netmsgfiltered.size();
        if (k < 32) {
            int i = k;
            if (netmsgfiltered.isIncludeTime() && isRealTime()) {
                int i1 = (int) (l1 - l);
                char c = '\0';
                if (i1 < 0) {
                    i1 = -i1;
                    c = '\200';
                }
                if ((i1 & 0xffffff80) == 0) {
                    netmsgoutput.writeByte(i | 0x40);
                    netmsgoutput.writeByte(c | i1);
                    statHSizeSendFMsgs += 2;
                } else if ((i1 & 0xffff8000) == 0) {
                    netmsgoutput.writeByte(i | 0x80);
                    netmsgoutput.writeByte(i1 >> 8 & 0x7f | c);
                    netmsgoutput.writeByte(i1 & 0xff);
                    statHSizeSendFMsgs += 3;
                } else {
                    netmsgoutput.writeByte(i | 0xc0);
                    if (i1 > 0x7fffff)
                        i1 = 0x7fffff;
                    netmsgoutput.writeByte(i1 >> 16 & 0x7f | c);
                    netmsgoutput.writeByte(i1 >> 8 & 0xff);
                    netmsgoutput.writeByte(i1 & 0xff);
                    statHSizeSendFMsgs += 4;
                }
            } else {
                netmsgoutput.writeByte(i);
                statHSizeSendFMsgs++;
            }
        } else {
            int j = 32;
            if (netmsgfiltered.isIncludeTime() && isRealTime()) {
                int j1 = (int) (l1 - l);
                if (j1 < 0) {
                    j1 = -j1;
                    j |= 0x10;
                }
                if ((j1 & 0xfffffff0) == 0) {
                    netmsgoutput.writeByte(j | 0x40 | j1 & 0xf);
                    netmsgoutput.writeByte(k);
                    statHSizeSendFMsgs += 2;
                } else if ((j1 & 0xfffff000) == 0) {
                    netmsgoutput.writeByte(j | 0x80 | j1 >> 8 & 0xf);
                    netmsgoutput.writeByte(k);
                    netmsgoutput.writeByte(j1 & 0xff);
                    statHSizeSendFMsgs += 3;
                } else {
                    if (j1 > 0xfffff)
                        j1 = 0xfffff;
                    netmsgoutput.writeByte(j | 0xc0 | j1 >> 16 & 0xf);
                    netmsgoutput.writeByte(k);
                    netmsgoutput.writeByte(j1 >> 8 & 0xff);
                    netmsgoutput.writeByte(j1 & 0xff);
                    statHSizeSendFMsgs += 4;
                }
            } else {
                netmsgoutput.writeByte(j);
                netmsgoutput.writeByte(k);
                statHSizeSendFMsgs += 2;
            }
        }
        if (netmsgfiltered.dataLength() > 0)
            netmsgoutput.write(netmsgfiltered.data(), 0, netmsgfiltered.dataLength());
        List list = netmsgfiltered.objects();
        if (list != null) {
            for (int k1 = list.size() - 1; k1 >= 0; k1--)
                netmsgoutput.writeShort(getIndx((NetObj) list.get(k1)));

        }
        statNumSendFMsgs++;
        statSizeSendFMsgs += netmsgfiltered.size();
    }

    private void computeMessageLen(NetMsgGuaranted netmsgguaranted) {
        netmsgguaranted._len = 3 + netmsgguaranted.size();
    }

    protected void computeMessageLen(NetMsgFiltered netmsgfiltered, long l) {
        computeMessageLen(netmsgfiltered, netmsgfiltered._time, l);
    }

    protected void computeMessageLen(NetMsgFiltered netmsgfiltered, long l, long l1) {
        int i = 3;
        int j = netmsgfiltered.size();
        i += j;
        if (j < 32) {
            if (netmsgfiltered.isIncludeTime() && isRealTime()) {
                int k = (int) (l - l1);
                if (k < 0)
                    k = -k;
                if ((k & 0xffffff80) == 0)
                    i++;
                else if ((k & 0xffff8000) == 0)
                    i += 2;
                else
                    i += 3;
            }
        } else {
            i++;
            if (netmsgfiltered.isIncludeTime() && isRealTime()) {
                int i1 = (int) (l - l1);
                if (i1 < 0)
                    i1 = -i1;
                if ((i1 & 0xfffffff0) == 0)
                    i += 0;
                else if ((i1 & 0xfffff000) == 0)
                    i++;
                else
                    i += 2;
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
            netobj = (NetObj) objects.get(i);
        } else {
            netobj = (NetObj) NetEnv.cur().objects.get(i);
        }
        int j = netmsginput.readUnsignedByte();
        getMessageObj = netobj;
        statHSizeReseivedMsgs += 3;
        NetMsgInput netmsginput1 = new NetMsgInput();
        if (j > 0) {
            byte abyte0[] = new byte[j];
            netmsginput.read(abyte0);
            netmsginput1.setData(this, true, abyte0, 0, j);
            statSizeReseivedMsgs += j;
        } else {
            netmsginput1.setData(this, true, null, 0, 0);
        }
        statNumReseivedMsgs++;
        return netmsginput1;
    }

    protected NetMsgInput getMessage(NetMsgInput netmsginput, long l) throws IOException {
        int i = netmsginput.readUnsignedShort();
        getMessageObjIndex = i;
        NetObj netobj = null;
        if ((i & 0x8000) != 0) {
            i &= 0xffff7fff;
            netobj = (NetObj) objects.get(i);
        } else {
            netobj = (NetObj) NetEnv.cur().objects.get(i);
        }
        int j = netmsginput.readUnsignedByte();
        statHSizeReseivedMsgs += 3;
        boolean flag = (j & 0xc0) != 0;
        boolean flag1 = (j & 0x20) == 0;
        long l1 = l;
        int k;
        if (flag1) {
            k = j & 0x1f;
            if (flag) {
                int i1 = j >> 6;
                int k1 = netmsginput.readUnsignedByte();
                statHSizeReseivedMsgs++;
                boolean flag2 = (k1 & 0x80) != 0;
                k1 &= 0x7f;
                while (--i1 > 0) {
                    k1 = k1 << 8 | netmsginput.readUnsignedByte();
                    statHSizeReseivedMsgs++;
                }
                l1 += flag2 ? -k1 : k1;
            }
        } else {
            k = netmsginput.readUnsignedByte();
            statHSizeReseivedMsgs++;
            if (flag) {
                int j1 = j >> 6;
                int i2 = j & 0xf;
                boolean flag3 = (j & 0x10) != 0;
                while (--j1 > 0) {
                    i2 = i2 << 8 | netmsginput.readUnsignedByte();
                    statHSizeReseivedMsgs++;
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
            } else {
                netmsginput1.setData(this, false, null, 0, 0);
            }
            statSizeReseivedMsgs += k;
            statNumReseivedMsgs++;
            return netmsginput1;
        } else {
            netmsginput.skipBytes(k);
            statSizeReseivedMsgs += k;
            statNumReseivedMsgs++;
            return null;
        }
    }

    protected static void printDebug(Exception exception) {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
    }

    protected NetChannel(int i, int j, int k, NetSocket netsocket, NetAddress netaddress, int l, NetConnect netconnect) {
        objects = new HashMapInt();
        mirrored = new HashMapExt();
        filters = new ArrayList();
        userState = -1;
        bSortGuaranted = false;
        holdGMsgs = new ArrayList();
        sendGMsgs = new NetChannelArrayList();
        sendGMsgSequenceNum = 0;
        receiveGMsgs = new HashMapInt();
        receiveGMsgSequenceNum = 0;
        receiveGMsgSequenceNumPosted = 0;
        lastTimeNakMessageSend = 0L;
        filteredTickMsgs = new HashMapExt();
        sendSequenceNum = 0;
        receiveSequenceNum = 0;
        receiveSequenceMask = 1;
        lastCheckTimeSpeed = 0L;
        diagnosticMessage = "";
        sendHistory = new NetChannelCycleHistory(64);
        maxTimeout = MAX_TIMEOUT_DEFAULT;
        receiveCountPackets = 0;
        bCheckTimeSpeed = false;
        checkT = new long[32];
        checkR = new long[32];
        checkI = -1;
        checkC = 0;
        swTbl = null;
        askMessageOut = new NetMsgFiltered();
        nakMessageOut = new NetMsgFiltered();
        flags = i;
        id = j;
        remoteId = k;
        socket = netsocket;
        remoteAddress = netaddress;
        remotePort = l;
        if ((j & 1) != 0)
            bCheckTimeSpeed = bCheckServerTimeSpeed;
        else
            bCheckTimeSpeed = bCheckClientTimeSpeed;
        state = 1;
        lastPacketReceiveTime = Time.real();
        setMaxSendSpeed(netsocket.getMaxSpeed());
        connect = netconnect;
        if (!isInitRemote())
            channelObj.doRequestCreating(this);
    }

    private void doCheckTimeSpeed() {
        try {
            if (isDestroying())
                return;
            if (state == 0 && Mission.isPlaying()) {
                int i = AudioDevice.getControl(611);
                long l = Time.real();
                long l1 = l - lastCheckTimeSpeed;
                if (i >= 0 && lastCheckTimeSpeed != 0L && l1 > 400L && l1 < 800L) {
                    double d = (double) i / 44100D;
                    double d1 = (double) l1 / 1000D;
                    if (Math.abs(1.0D - d / d1) > 0.04D) {
                        new MsgAction(64, 10D, this) {

                            public void doAction(Object obj) {
                                NetChannel netchannel = (NetChannel) obj;
                                if (!netchannel.isDestroying())
                                    netchannel.destroy("Timeout .");
                            }

                        };
                        return;
                    }
                }
                lastCheckTimeSpeed = l;
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
        objects = new HashMapInt();
        mirrored = new HashMapExt();
        filters = new ArrayList();
        userState = -1;
        bSortGuaranted = false;
        holdGMsgs = new ArrayList();
        sendGMsgs = new NetChannelArrayList();
        sendGMsgSequenceNum = 0;
        receiveGMsgs = new HashMapInt();
        receiveGMsgSequenceNum = 0;
        receiveGMsgSequenceNumPosted = 0;
        lastTimeNakMessageSend = 0L;
        filteredTickMsgs = new HashMapExt();
        sendSequenceNum = 0;
        receiveSequenceNum = 0;
        receiveSequenceMask = 1;
        lastCheckTimeSpeed = 0L;
        diagnosticMessage = "";
        sendHistory = new NetChannelCycleHistory(64);
        maxTimeout = MAX_TIMEOUT_DEFAULT;
        receiveCountPackets = 0;
        bCheckTimeSpeed = false;
        checkT = new long[32];
        checkR = new long[32];
        checkI = -1;
        checkC = 0;
        swTbl = null;
        askMessageOut = new NetMsgFiltered();
        nakMessageOut = new NetMsgFiltered();
    }

    protected void controlStartInit() {
        created();
    }

    private void created() {
        connect.channelCreated(this);
    }

    protected void setMaxSendSpeed(double d) {
        maxSendSpeed = d;
        if ((id & 1) == 1)
            maxChSendSpeed = (2D * maxSendSpeed) / 3D;
        else
            maxChSendSpeed = maxSendSpeed / 3D;
    }

    public int ping() {
        return ping;
    }

    public int pingTo() {
        return pingTo;
    }

    public int getMaxTimeout() {
        return maxTimeout;
    }

    public void setMaxTimeout(int i) {
        if (this.maxTimeout != MAX_TIMEOUT_DEFAULT && i < this.maxTimeout) {
            if (this.lastMaxTimeoutSkipMessage == -1L || Time.current() > this.lastMaxTimeoutSkipMessage + SKIP_TIMEOUT_MESSAGE_INTERVAL) {
                this.lastMaxTimeoutSkipMessage = Time.current();
                System.out.println("Attempt to change max channel timeout from " + this.maxTimeout + "ms to " + i + "ms skipped for NetChannel id=" + this.id + ", remote id=" + this.remoteId + ", remote port=" + this.remotePort());
            }
        }

        // TODO: Changed by Storebror, minimum Channel Timeout set to 1000ms.
        // if(i < 0)
        if (i < 1000)
            i = 1000;
        // ---
        if (i > 0x1ffff)
            i = 0x1ffff;
        if (maxTimeout != i) {
            maxTimeout = i;
            channelObj.doSetTimeout(this, i);
        }
    }

    public int getCurTimeout() {
        return (int) (Time.real() - lastPacketReceiveTime);
    }

    public int getCurTimeoutOk() {
        return (int) (Time.real() - lastPacketOkReceiveTime);
    }

    private boolean isTimeout() {
        if (!isRealTime())
            return false;
        return getCurTimeout() >= getMaxTimeout();
    }

    public boolean isRequeredSendPacket(long l) {
        return l > nextPacketSendTime;
    }

    protected int getSendPacketLen(long l) {
        if (l < nextPacketSendTime)
            return 0;
        if (pingToSpeed > 0.10000000000000001D) {
            curPacketSendSpeed = sendHistory.speed(nextPacketSendTime - (long) ping, nextPacketSendTime, maxChSendSpeed);
            curPacketSendSpeed /= pingToSpeed + 1.0D;
            lastDownSendTime = l;
            lastDownSendSpeed = curPacketSendSpeed;
        } else if (pingToSpeed < -0.10000000000000001D) {
            lastDownSendTime = l;
            lastDownSendSpeed = curPacketSendSpeed;
        } else if (ping > 0 && lastDownSendTime > 0L) {
            curPacketSendSpeed = maxChSendSpeed - (maxChSendSpeed - lastDownSendSpeed) * Math.exp(-(l - lastDownSendTime) / (long) ping);
            if (curPacketSendSpeed == 0.98999999999999999D * maxChSendSpeed)
                lastDownSendTime = 0L;
        } else {
            curPacketSendSpeed = maxChSendSpeed;
        }
        if (curPacketSendSpeed < 0.10000000000000001D)
            curPacketSendSpeed = 0.10000000000000001D;
        double d = (pingTo * 2) / 3;
        if (d < 10D)
            d = 10D;
        double d1 = curPacketSendSpeed * d;
        int i = socket.getHeaderSize();
        if (d1 < 256D + (double) i)
            d1 = 256D + (double) i;
        if (d1 > (double) (socket.getMaxDataSize() + i))
            d1 = socket.getMaxDataSize() + i;
        return (int) d1 - i;
    }

    private void printDouble(double d) {
        if (d < 0.0D) {
            d = -d;
            System.out.print('-');
        } else {
            System.out.print('+');
        }
        System.out.print((int) d + ".");
        int i = (int) ((d - (double) (int) d) * 100D);
        if (i < 10)
            System.out.print("0");
        System.out.print(i);
    }

    protected void sendTime(NetMsgOutput netmsgoutput, int i, long l, int j) throws IOException {
        int k = socket.getHeaderSize();
        double d = (double) (j + k) / curPacketSendSpeed;
        if (d < 10D)
            d = 10D;
        nextPacketSendTime = l + (long) d;
        lastPacketSendTime = l;
        if (isRealTime())
            sendHistory.put(i, j + k, l);
        else
            return;
        netmsgoutput.writeShort((int) l & 0xffff);
        int i1 = (int) (l - lastPacketReceiveTime);
        netmsgoutput.writeShort(i1 & 0xffff);
        int j1 = lastPacketReceiveSequenceNum & 0x3fff;
        j1 |= ((int) l >> 16 & 1) << 14;
        j1 |= (i1 >> 16 & 1) << 15;
        netmsgoutput.writeShort(j1);
    }

    public long remoteClockOffset() {
        if (receiveCountPackets > TIME_OFFSET_SUM)
            return remoteClockOffsetSum / 256L;
        if (receiveCountPackets > 0)
            return remoteClockOffsetSum / (long) receiveCountPackets;
        else
            return 0L;
    }

    private long receiveTime(long l, NetMsgInput netmsginput, int i, boolean flag, boolean flag1) throws IOException {
        if (!flag)
            return Time.currentReal();
        long l1 = l;
        int j = netmsginput.readUnsignedShort();
        int k = netmsginput.readUnsignedShort();
        int i1 = netmsginput.readUnsignedShort();
        if (flag1)
            return l;
        j |= (i1 >> 14 & 1) << 16;
        k |= (i1 >> 15 & 1) << 16;
        i1 &= 0x3fff;
        long l2;
        for (l2 = lastPacketReceiveRemoteTime & 0xfffffffffffe0000L | (long) j; l2 < lastPacketReceiveRemoteTime; l2 += 0x20000L)
            ;
        int j1 = sendHistory.getIndex(i1);
        if (j1 >= 0) {
            receiveCountPackets++;
            long l3 = sendHistory.getTime(j1);
            int k1 = (int) (l - l3) - k;
            if (k1 < 0) {
                k1 = 0;
                k = (int) (l - l3);
            }
            long l4 = (l + l3) / 2L - (l2 - (long) (k / 2));
            if (receiveCountPackets > TIME_OFFSET_SUM) {
                remoteClockOffsetSum = (remoteClockOffsetSum * 255L) / 256L + l4;
                l4 = remoteClockOffsetSum / 256L;
                long l5 = l - (l2 + l4);
                if (l5 < 0L) {
                    l4 = l - l2;
                    remoteClockOffsetSum = 256L * l4;
                }
                if (l5 > (long) k1) {
                    l4 = l - (long) k1 - l2;
                    remoteClockOffsetSum = 256L * l4;
                }
            } else {
                remoteClockOffsetSum += l4;
                l4 = remoteClockOffsetSum / (long) receiveCountPackets;
                long l6 = l - (l2 + l4);
                if (l6 < 0L) {
                    l4 = l - l2;
                    remoteClockOffsetSum = (long) receiveCountPackets * l4;
                }
                if (l6 > (long) k1) {
                    l4 = l - (long) k1 - l2;
                    remoteClockOffsetSum = (long) receiveCountPackets * l4;
                }
            }
            l1 = l2 + l4;
            long l7 = l - l1;
            int i2 = k1 - (int) l7;
            int j2 = pingTo;
            if (receiveCountPackets > TIME_PING_SUM_START) {
                int k2 = TIME_PING_SUM_START;
                if (ping > 0) {
                    k2 = (TIME_PING_SUM + ping / 2) / ping;
                    if (k2 < TIME_PING_SUM_START)
                        k2 = TIME_PING_SUM_START;
                }
                pingSum = (pingSum * (k2 - 1)) / countPingSum + k1;
                pingToSum = (pingToSum * (k2 - 1)) / countPingSum + i2;
                countPingSum = k2;
            } else {
                pingSum += k1;
                pingToSum += i2;
                countPingSum = receiveCountPackets;
            }
            ping = pingSum / countPingSum;
            pingTo = pingToSum / countPingSum;
            if (receiveCountPackets > 1 && l > lastPacketReceiveTime) {
                pingToSpeed = (double) (pingTo - j2) / (double) (l - lastPacketReceiveTime);
                if (bCheckTimeSpeed)
                    if (checkI < 32) {
                        if (checkI < 0 || l - checkT[checkI] > 1000L) {
                            checkI++;
                            checkT[checkI & 0x1f] = l;
                            checkR[checkI & 0x1f] = l2 + (long) (ping - pingTo);
                        }
                    } else if (l - checkT[checkI & 0x1f] > 1000L) {
                        checkI++;
                        long l8 = checkT[checkI & 0x1f];
                        long l9 = checkR[checkI & 0x1f];
                        long l10 = l2 + (long) (ping - pingTo);
                        checkT[checkI & 0x1f] = l;
                        checkR[checkI & 0x1f] = l10;
                        double d = Math.abs(1.0D - ((double) l10 - (double) l9) / ((double) l - (double) l8));
                        if (d > checkTimeSpeedDifferense)
                            checkC++;
                        else
                            checkC = 0;
                    }
            }
        }
        lastPacketReceiveTime = l;
        lastPacketReceiveRemoteTime = l2;
        lastPacketReceiveSequenceNum = i;
        return l1;
    }

    private void cdata(byte abyte0[], int i, int j) {
        if (swTbl == null)
            return;
        int k = j % swTbl.length;
        if (i + j > abyte0.length)
            j = abyte0.length - i;
        while (j > 0) {
            abyte0[i] = (byte) (abyte0[i] ^ swTbl[k]);
            i++;
            j--;
            k = (k + 1) % swTbl.length;
        }
    }

    public double getMaxSpeed() {
        return maxSendSpeed;
    }

    public void setMaxSpeed(double d) {
        if (d == maxSendSpeed)
            return;
        if (socket.maxChannels == 0) {
            setMaxSendSpeed(d);
            socket.setMaxSpeed(d);
            channelObj.doSetSpeed(this, d);
            return;
        }
        if (d > socket.getMaxSpeed()) {
            d = socket.getMaxSpeed();
            if (d == maxSendSpeed)
                return;
        }
        setMaxSendSpeed(d);
        channelObj.doSetSpeed(this, d);
    }

    protected static void destroyNetObj(NetObj netobj) {
        if (netobj == null)
            return;
        try {
            Object obj = netobj;
            Object obj1 = netobj.superObj();
            if (obj1 != null && (obj1 instanceof Destroy)) {
                obj = (Destroy) obj1;
                if (!((Destroy) (obj)).isDestroyed())
                    ((Destroy) (obj)).destroy();
            } else if (!((Destroy) (obj)).isDestroyed())
                ((Destroy) (obj)).destroy();
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    protected static void classInit() {
        if (bClassInited) {
            return;
        } else {
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
        doCheckTimeSpeed();
        printDouble(0D);
        if (DEBUG) {
            if (destroyMessage == null) {}
        }
    }

    private long                             lastMaxTimeoutSkipMessage     = -1L;
    private static final long                SKIP_TIMEOUT_MESSAGE_INTERVAL = 10000;
    private static final int                 MAX_TIMEOUT_DEFAULT           = 0x1FFFF;
    // ...

    private static final boolean             DEBUG                         = false;
    public static boolean                    bCheckServerTimeSpeed         = true;
    public static boolean                    bCheckClientTimeSpeed         = false;
    public static int                        checkTimeSpeedInterval        = 17;
    public static double                     checkTimeSpeedDifferense      = 0.2D;
    protected int                            id;
    protected int                            remoteId;
    protected NetSocket                      socket;
    protected NetAddress                     remoteAddress;
    protected int                            remotePort;
    protected HashMapInt                     objects;
    protected HashMapExt                     mirrored;
    protected ArrayList                      filters;
    public static final int                  REAL_TIME                     = 1;
    public static final int                  PUBLIC                        = 2;
    public static final int                  GLOBAL                        = 4;
    protected int                            flags;
    public static final int                  STATE_DESTROYED               = 0x80000000;
    public static final int                  STATE_DO_DESTROY              = 0x40000000;
    public static final int                  STATE_READY                   = 0;
    public static final int                  STATE_INITMASK                = 0x3fffffff;
    public int                               userState;
    protected int                            state;
    private int                              initStamp;
    private boolean                          bSortGuaranted;
    public NetChannelStat                    stat;
    private long                             timeDestroyed;
    protected static final int               MESSAGE_SEQUENCE_FULL         = 65535;
    private static final int                 MESSAGE_SEQUENCE_FRAME        = 1023;
    private static final int                 PACKET_SEQUENCE_FULL          = 16383;
    private static NetMsgOutput              _tmpOut                       = new NetMsgOutput();
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
    protected static ArrayList               filteredSortMsgs              = new ArrayList();
    private static int                       filteredSizeMsgs;
    protected static int                     filteredMinSizeMsgs;
    public byte                              crcInit[]                     = { 65, 3 };
    protected int                            sendSequenceNum;
    private int                              receiveSequenceNum;
    private int                              receiveSequenceMask;
    protected static long                    getMessageTime;
    protected static NetObj                  getMessageObj;
    protected static int                     getMessageObjIndex;
    private long                             lastCheckTimeSpeed;
    private NetConnect                       connect;
    private String                           diagnosticMessage;
    private static NetChannelPriorComparator priorComparator               = new NetChannelPriorComparator();
    private static NetChannelTimeComparator  timeComparator                = new NetChannelTimeComparator();
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
    private static final int                 TIME_OFFSET_SUM               = 256;
    private static final int                 TIME_PING_SUM_START           = 4;
    private static final int                 TIME_PING_SUM                 = 2000;
    protected static final double            MIN_SPEED_SEND                = 0.10000000000000001D;
    protected static final double            MIN_TIME_SEND                 = 10D;
    protected static final double            MIN_LEN_SEND                  = 256D;
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
    private static boolean                   bClassInited                  = false;
}
