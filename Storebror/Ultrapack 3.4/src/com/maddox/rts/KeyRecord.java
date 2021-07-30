package com.maddox.rts;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.maddox.il2.engine.Config;
import com.maddox.util.HashMapInt;
import com.maddox.util.SharedTokenizer;

public class KeyRecord implements MsgHotKeyCmdListener, MsgTimeOutListener {
    class NetKeyRecord extends NetObj {

        public void post(MsgEvent msgevent) {
            if (!KeyRecord.this.isRecording()) return;
            if (this.out == null || this.out.isDestroying()) return;
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeInt(msgevent.id);
                if (msgevent.p9 != -1) {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                    netmsgguaranted.writeInt(msgevent.p5);
                    netmsgguaranted.writeInt(msgevent.p6);
                    netmsgguaranted.writeInt(msgevent.p7);
                    netmsgguaranted.writeInt(msgevent.p8);
                    netmsgguaranted.writeInt(msgevent.p9);
                } else if (msgevent.p8 != -1) {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                    netmsgguaranted.writeInt(msgevent.p5);
                    netmsgguaranted.writeInt(msgevent.p6);
                    netmsgguaranted.writeInt(msgevent.p7);
                    netmsgguaranted.writeInt(msgevent.p8);
                } else if (msgevent.p7 != -1) {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                    netmsgguaranted.writeInt(msgevent.p5);
                    netmsgguaranted.writeInt(msgevent.p6);
                    netmsgguaranted.writeInt(msgevent.p7);
                } else if (msgevent.p6 != -1) {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                    netmsgguaranted.writeInt(msgevent.p5);
                    netmsgguaranted.writeInt(msgevent.p6);
                } else if (msgevent.p5 != -1) {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                    netmsgguaranted.writeInt(msgevent.p5);
                } else if (msgevent.p4 != -1) {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                } else if (msgevent.p3 != -1) {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                } else if (msgevent.p2 != -1) {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                } else netmsgguaranted.writeInt(msgevent.p1);
                this.postTo(this.out, netmsgguaranted);
                this.out.flush();
            } catch (Exception exception) {
                printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (!KeyRecord.this.isPlaying()) return true;
            MsgEvent msgevent = new MsgEvent();
            msgevent.time = Time.current();
            msgevent.id = netmsginput.readInt();
            if (netmsginput.available() != 0) {
                msgevent.p1 = netmsginput.readInt();
                if (netmsginput.available() != 0) {
                    msgevent.p2 = netmsginput.readInt();
                    if (netmsginput.available() != 0) {
                        msgevent.p3 = netmsginput.readInt();
                        if (netmsginput.available() != 0) {
                            msgevent.p4 = netmsginput.readInt();
                            if (netmsginput.available() != 0) {
                                msgevent.p5 = netmsginput.readInt();
                                if (netmsginput.available() != 0) {
                                    msgevent.p6 = netmsginput.readInt();
                                    if (netmsginput.available() != 0) {
                                        msgevent.p7 = netmsginput.readInt();
                                        if (netmsginput.available() != 0) {
                                            msgevent.p8 = netmsginput.readInt();
                                            if (netmsginput.available() != 0) {
                                                msgevent.p9 = netmsginput.readInt();
                                                if (netmsginput.available() != 0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            KeyRecord.this.hotPlayer.msgTimeOut(msgevent);
            return true;
        }

        public void msgNetNewChannel(NetChannel netchannel) {
            if (netchannel instanceof NetChannelOutStream) this.out = (NetChannelOutStream) netchannel;
        }

        public void msgNetDelChannel(NetChannel netchannel) {
            if (netchannel == this.out) this.out = null;
            else if (netchannel instanceof NetChannelInStream && KeyRecord.this.isPlaying() && KeyRecord.this.callBack != null) KeyRecord.this.callBack.playRecordedEnded();
        }

        NetChannelOutStream out;

        public NetKeyRecord(int i) {
            super(null, i);
        }
    }

    class HotPlayer implements MsgTimeOutListener {

        public void msgTimeOut(Object obj) {
            if (KeyRecord.this.hotPlayer != this) return;
            MsgEvent msgevent = (MsgEvent) obj;
            HotKeyCmd hotkeycmd = HotKeyCmd.getByRecordedId(msgevent.id);
            if (hotkeycmd == null) return;
            if (hotkeycmd instanceof HotKeyCmdMouseMove) {
                if (!hotkeycmd.hotKeyCmdEnv().hotKeyEnv().isEnabled()) {
                    HotKeyCmdMouseMove hotkeycmdmousemove = (HotKeyCmdMouseMove) hotkeycmd;
                    hotkeycmdmousemove._exec(msgevent.p1, msgevent.p2, 0);
                }
            } else if (hotkeycmd instanceof HotKeyCmdTrackIRAngles) {
                if (!hotkeycmd.hotKeyCmdEnv().hotKeyEnv().isEnabled()) {
                    HotKeyCmdTrackIRAngles hotkeycmdtrackirangles = (HotKeyCmdTrackIRAngles) hotkeycmd;
                    hotkeycmdtrackirangles._exec(msgevent.p1 / 100F, msgevent.p2 / 100F, msgevent.p3 / 100F);
                }
            } else if (hotkeycmd instanceof HotKeyCmdMove) {
                if (!hotkeycmd.hotKeyCmdEnv().hotKeyEnv().isEnabled()) {
                    HotKeyCmdMove hotkeycmdmove = (HotKeyCmdMove) hotkeycmd;
                    hotkeycmdmove._exec(msgevent.p1);
                }
            } else if (hotkeycmd instanceof HotKeyCmdRedirect) {
                if (!hotkeycmd.hotKeyCmdEnv().hotKeyEnv().isEnabled()) {
                    HotKeyCmdRedirect hotkeycmdredirect = (HotKeyCmdRedirect) hotkeycmd;
                    hotkeycmdredirect._exec(msgevent.p2, msgevent.p3, msgevent.p4, msgevent.p5, msgevent.p6, msgevent.p7, msgevent.p8, msgevent.p9);
                }
            } else if (!hotkeycmd.hotKeyCmdEnv().hotKeyEnv().isEnabled() || KeyRecord.this.bFirstHotCmd) {
                KeyRecord.this.curPlayArg0 = msgevent.p2;
                KeyRecord.this.curPlayArg1 = msgevent.p3;
                KeyRecord.this.curPlaySArg0 = msgevent.arg0;
                KeyRecord.this.curPlaySArg1 = msgevent.arg1;
                if (KeyRecord.this.bFirstHotCmd && KeyRecord.this.callBack != null) {
                    KeyRecord.this.callBack.doFirstHotCmd(true);
                    hotkeycmd._exec(msgevent.p1 == 1);
                    KeyRecord.this.callBack.doFirstHotCmd(false);
                } else hotkeycmd._exec(msgevent.p1 == 1);
                if (KeyRecord.this.bFirstHotCmd && msgevent.p1 == 1) hotkeycmd._exec(false);
                KeyRecord.this.bFirstHotCmd = false;
            }
        }

        public HotPlayer() {
            KeyRecord.this.bFirstHotCmd = true;
        }
    }

    class FingerPlayer implements MsgTimeOutListener {

        public void msgTimeOut(Object obj) {
            if (KeyRecord.this.fingerCalculator == null) return;
            if (KeyRecord.this.fingerPlayer != this) return;
            if (obj == null) {
                if (KeyRecord.this.bSave) {
                    int ai[] = KeyRecord.this.fingerCalculator.calculateFingers();
                    KeyRecord.this.recordLst.add(new MsgEvent(Time.current(), 0, ai, KeyRecord.this.fingersSize));
                    MsgTimeOut.post(Time.current() + KeyRecord.this.fingerCalculator.checkPeriod(), 0x80000001, this, null);
                }
            } else if (KeyRecord.this.bPlay) {
                MsgEvent msgevent = (MsgEvent) obj;
                KeyRecord.this.fingers[0] = msgevent.p1;
                if (KeyRecord.this.fingersSize > 1) {
                    KeyRecord.this.fingers[1] = msgevent.p2;
                    if (KeyRecord.this.fingersSize > 2) {
                        KeyRecord.this.fingers[2] = msgevent.p3;
                        if (KeyRecord.this.fingersSize > 3) {
                            KeyRecord.this.fingers[3] = msgevent.p4;
                            if (KeyRecord.this.fingersSize > 4) {
                                KeyRecord.this.fingers[4] = msgevent.p5;
                                if (KeyRecord.this.fingersSize > 5) {
                                    KeyRecord.this.fingers[5] = msgevent.p6;
                                    if (KeyRecord.this.fingersSize > 6) {
                                        KeyRecord.this.fingers[6] = msgevent.p7;
                                        if (KeyRecord.this.fingersSize > 7) {
                                            KeyRecord.this.fingers[7] = msgevent.p8;
                                            if (KeyRecord.this.fingersSize > 8) KeyRecord.this.fingers[8] = msgevent.p9;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                KeyRecord.this.fingerCalculator.checkFingers(KeyRecord.this.fingers);
            }
        }

        FingerPlayer() {
        }
    }

    public void addExcludeCmdEnv(String s) {
        this.excludeCmdEnv.put(s, null);
    }

    public void clearListExcludeCmdEnv() {
        this.excludeCmdEnv.clear();
    }

    public void addExcludePrevCmd(int i) {
        this.excludePrevCmd.put(i, null);
    }

    public void setEnablePlayArgs(boolean flag) {
        this.bEnablePlayArgs = flag;
    }

    public boolean isEnablePlayArgs() {
        return this.bEnablePlayArgs || this.bFirstHotCmd;
    }

    public boolean isExistPlayArgs() {
        return (this.bEnablePlayArgs || this.bFirstHotCmd) && this._isExistPlayArgs();
    }

    public boolean _isExistPlayArgs() {
        return this.curPlayArg0 != -1 || this.curPlayArg1 != -1;
    }

    public boolean isExistPlaySArgs() {
        return (this.bEnablePlayArgs || this.bFirstHotCmd) && this._isExistPlaySArgs();
    }

    public boolean _isExistPlaySArgs() {
        return this.curPlaySArg0 != null || this.curPlaySArg1 != null;
    }

    public int getPlayArg0() {
        return this.bEnablePlayArgs ? this.curPlayArg0 : -1;
    }

    public int getPlayArg1() {
        return this.bEnablePlayArgs ? this.curPlayArg1 : -1;
    }

    public String getPlaySArg0() {
        return this.bEnablePlayArgs ? this.curPlaySArg0 : null;
    }

    public String getPlaySArg1() {
        return this.bEnablePlayArgs ? this.curPlaySArg1 : null;
    }

    public int _getPlayArg0() {
        return this.curPlayArg0;
    }

    public int _getPlayArg1() {
        return this.curPlayArg1;
    }

    public String _getPlaySArg0() {
        return this.curPlaySArg0;
    }

    public String _getPlaySArg1() {
        return this.curPlaySArg1;
    }

    public void msgTimeOut(Object obj) {
        try {
            long l = Time.current() + 2 * Time.tickLen();
            long l1 = l;
            boolean flag = this.playLst.size() > 0;
            for (; this.playLst.size() > 0; this.playLst.removeFirst()) {
                MsgEvent msgevent = (MsgEvent) this.playLst.getFirst();
                if (msgevent.time > l) break;
                l1 = msgevent.time;
                if (msgevent.id == 0) {
                    if (this.fingerPlayer != null) MsgTimeOut.post(msgevent.time, 0x80000001, this.fingerPlayer, msgevent);
                    continue;
                }
                HotKeyCmd hotkeycmd = HotKeyCmd.getByRecordedId(msgevent.id);
                if (hotkeycmd != null) MsgTimeOut.post(msgevent.time, this.hotPlayer, msgevent);
            }

            if (this.playLst.size() > 0) {
                this.tickMsg.setFlags(24);
                this.tickMsg.post();
            } else if (flag) {
                this.tickMsg.setFlags(0);
                this.tickMsg.setTime(l1);
                this.tickMsg.post();
            } else if (this.callBack != null) this.callBack.playRecordedEnded();
        } catch (Exception exception) {
            if (this.callBack != null) this.callBack.playRecordedEnded();
        }
    }

    public void setCurRecordArgs(NetObj netobj, NetObj netobj1) {
        if (netobj != null) this.curRecordArg0 = netobj.idLocal();
        if (netobj1 != null) this.curRecordArg1 = netobj1.idLocal();
    }

    public void setCurRecordArg0(NetObj netobj) {
        if (netobj != null) this.curRecordArg0 = netobj.idLocal();
    }

    public void setCurRecordArg1(NetObj netobj) {
        if (netobj != null) this.curRecordArg1 = netobj.idLocal();
    }

    public void setCurRecordSArgs(String s, String s1) {
        this.curRecordSArg0 = s;
        this.curRecordSArg1 = s1;
    }

    public void setCurRecordSArg0(String s) {
        this.curRecordSArg0 = s;
    }

    public void setCurRecordSArg1(String s) {
        this.curRecordSArg1 = s;
    }

    public void msgHotKeyCmd(HotKeyCmd hotkeycmd, boolean flag, boolean flag1) {
        if (hotkeycmd.recordId() != 0) {
            if (this.excludeCmdEnv.containsKey(hotkeycmd.hotKeyCmdEnv().name())) return;
            if (hotkeycmd instanceof HotKeyCmdMouseMove) {
                // TODO: +++ Disable old "TRK" track recording
                // if(!bSave || flag1)
                if (!this.bSave || flag1 || this.isDisabled())
                    // TODO: --- Disable old "TRK" track recording
                    return;
                HotKeyCmdMouseMove hotkeycmdmousemove = (HotKeyCmdMouseMove) hotkeycmd;
                this.saveEvent(new MsgEvent(Time.current(), hotkeycmd.recordId(), hotkeycmdmousemove._dx, hotkeycmdmousemove._dy));
            } else if (hotkeycmd instanceof HotKeyCmdTrackIRAngles) {
                // TODO: +++ Disable old "TRK" track recording
                // if(!bSave || flag1)
                if (!this.bSave || flag1 || this.isDisabled())
                    // TODO: --- Disable old "TRK" track recording
                    return;
                HotKeyCmdTrackIRAngles hotkeycmdtrackirangles = (HotKeyCmdTrackIRAngles) hotkeycmd;
                this.saveEvent(new MsgEvent(Time.current(), hotkeycmd.recordId(), (int) (hotkeycmdtrackirangles._yaw * 100F), (int) (hotkeycmdtrackirangles._pitch * 100F), (int) (hotkeycmdtrackirangles._roll * 100F)));
            } else if (hotkeycmd instanceof HotKeyCmdMove) {
                // TODO: +++ Disable old "TRK" track recording
                // if(!bSave || flag1)
                if (!this.bSave || flag1 || this.isDisabled())
                    // TODO: --- Disable old "TRK" track recording
                    return;
                HotKeyCmdMove hotkeycmdmove = (HotKeyCmdMove) hotkeycmd;
                this.saveEvent(new MsgEvent(Time.current(), hotkeycmd.recordId(), hotkeycmdmove._mov));
            } else if (hotkeycmd instanceof HotKeyCmdRedirect) {
                // TODO: +++ Disable old "TRK" track recording
                // if(!bSave || flag1)
                if (!this.bSave || flag1 || this.isDisabled())
                    // TODO: --- Disable old "TRK" track recording
                    return;
                HotKeyCmdRedirect hotkeycmdredirect = (HotKeyCmdRedirect) hotkeycmd;
                this.saveEvent(new MsgEvent(Time.current(), hotkeycmd.recordId(), hotkeycmdredirect.idRedirect(), hotkeycmdredirect._r[0], hotkeycmdredirect._r[1], hotkeycmdredirect._r[2], hotkeycmdredirect._r[3], hotkeycmdredirect._r[4],
                        hotkeycmdredirect._r[5], hotkeycmdredirect._r[6], hotkeycmdredirect._r[7]));
            } else if (flag1) {
                this.curRecordArg0 = -1;
                this.curRecordArg1 = -1;
                this.curRecordSArg0 = null;
                this.curRecordSArg1 = null;
            } else {
                MsgEvent msgevent = null;
                if (this.curRecordArg0 != -1 || this.curRecordArg1 != -1) {
                    if (!this.excludePrevCmd.containsKey(hotkeycmd.recordId())) {
                        this.prevRecordId = hotkeycmd.recordId();
                        this.prevRecordArg0 = this.curRecordArg0;
                        this.prevRecordArg1 = this.curRecordArg1;
                    }
                    if (this.bSave) this.saveEvent(msgevent = new MsgEvent(Time.current(), hotkeycmd.recordId(), flag ? 1 : 0, this.curRecordArg0, this.curRecordArg1));
                } else if (this.bSave) this.saveEvent(msgevent = new MsgEvent(Time.current(), hotkeycmd.recordId(), flag ? 1 : 0));
                if (msgevent != null) {
                    msgevent.arg0 = this.curRecordSArg0;
                    msgevent.arg1 = this.curRecordSArg1;
                }
            }
        }
    }

    public boolean startPlay(KeyRecordCallback keyrecordcallback) {
        this.callBack = keyrecordcallback;
        Keyboard.adapter()._clear();
        Mouse.adapter()._clear();
        Joy.adapter()._clear();
        this.hotPlayer = new HotPlayer();
        this.bPlay = true;
        return this.bPlay;
    }

    public boolean startPlay(SectFile sectfile, int i, int j, KeyRecordCallback keyrecordcallback) {
        this.callBack = keyrecordcallback;
        this.bPlay = false;
        this.playLst.clear();
        int k = sectfile.vars(i);
        long l = 0L;
        for (int i1 = j; i1 < k; i1++) {
            String s = sectfile.line(i, i1);
            SharedTokenizer.set(s);
            if (SharedTokenizer.hasMoreTokens()) {
                MsgEvent msgevent = new MsgEvent();
                long l1 = SharedTokenizer.next(0);
                l += l1;
                msgevent.time = l;
                this.playLst.add(this.parseEvent(msgevent));
            }
        }

        if (this.playLst.size() > 0) {
            Keyboard.adapter()._clear();
            Mouse.adapter()._clear();
            Joy.adapter()._clear();
            this.tickMsg.remove();
            this.tickMsg.setFlags(0);
            this.tickMsg.setTime(0L);
            this.tickMsg.post();
            this.hotPlayer = new HotPlayer();
        }
        this.bPlay = true;
        return this.bPlay;
    }

    private MsgEvent parseEvent(MsgEvent msgevent) {
        try {
            SharedTokenizer._nextWord();
            msgevent.id = SharedTokenizer._getInt();
            SharedTokenizer._nextWord();
            msgevent.p1 = SharedTokenizer._getInt();
            SharedTokenizer._nextWord();
            msgevent.p2 = SharedTokenizer._getInt();
            SharedTokenizer._nextWord();
            msgevent.p3 = SharedTokenizer._getInt();
            SharedTokenizer._nextWord();
            msgevent.p4 = SharedTokenizer._getInt();
            SharedTokenizer._nextWord();
            msgevent.p5 = SharedTokenizer._getInt();
            SharedTokenizer._nextWord();
            msgevent.p6 = SharedTokenizer._getInt();
            SharedTokenizer._nextWord();
            msgevent.p7 = SharedTokenizer._getInt();
            SharedTokenizer._nextWord();
            msgevent.p8 = SharedTokenizer._getInt();
            SharedTokenizer._nextWord();
            msgevent.p9 = SharedTokenizer._getInt();
        } catch (Exception exception) {
            msgevent.arg0 = SharedTokenizer._getString();
            SharedTokenizer._nextWord();
            msgevent.arg1 = SharedTokenizer._getString();
        }
        return msgevent;
    }

    public void stopPlay() {
        this.bPlay = false;
        this.callBack = null;
        this.hotPlayer = null;
        this.playLst.clear();
    }

    public boolean isPlaying() {
        return this.bPlay;
    }

    public boolean isRecording() {
        return this.bSave;
    }

    public boolean isContainRecorded() {
        return this.recordLst.size() > 0;
    }

    public void clearRecorded() {
        this.recordLst.clear();
    }

    public void clearPrevStates() {
        this.prevRecordId = 0;
    }

    private void saveEvent(MsgEvent msgevent) {
        if (this.bNet) this.net.post(msgevent);
        else
            // TODO: +++ Disable old "TRK" track recording
            if (Config.cur.saveTrk)
                // TODO: --- Disable old "TRK" track recording
                this.recordLst.add(msgevent);
    }

    public void startRecordingNet() {
        this.bSave = true;
        this.bNet = true;
        this.fingerPlayer = null;
        if (this.prevRecordId != 0) {
            this.saveEvent(new MsgEvent(Time.current(), this.prevRecordId, 1, this.prevRecordArg0, this.prevRecordArg1));
            this.saveEvent(new MsgEvent(Time.current(), this.prevRecordId, 0, this.prevRecordArg0, this.prevRecordArg1));
        }
    }

    public void startRecording() {
        this.bSave = true;
        this.bNet = false;
        if (this.fingerCalculator != null) {
            this.fingerPlayer = new FingerPlayer();
            MsgTimeOut.post(Time.current(), 0x80000001, this.fingerPlayer, null);
        }
    }

    public void stopRecording(boolean flag) {
        if (flag) this.saveEvent(new MsgEvent(Time.current(), -1, 0));
        this.bSave = false;
        this.fingerPlayer = null;
    }

    public void setFingerCalculator(KeyRecordFinger keyrecordfinger) {
        this.fingerCalculator = keyrecordfinger;
        this.fingersSize = this.fingerCalculator.countSaveFingers();
    }

    public boolean saveRecorded(SectFile sectfile, int i) {
        if (this.recordLst.size() > 0) try {
            long l = 0L;
            for (Iterator iterator = this.recordLst.iterator(); iterator.hasNext();) {
                MsgEvent msgevent = (MsgEvent) iterator.next();
                long l1 = msgevent.time - l;
                l = msgevent.time;
                String s = null;
                if (msgevent.p9 != -1) s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4 + " " + msgevent.p5 + " " + msgevent.p6 + " " + msgevent.p7 + " " + msgevent.p8 + " " + msgevent.p9;
                else if (msgevent.p8 != -1) s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4 + " " + msgevent.p5 + " " + msgevent.p6 + " " + msgevent.p7 + " " + msgevent.p8;
                else if (msgevent.p7 != -1) s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4 + " " + msgevent.p5 + " " + msgevent.p6 + " " + msgevent.p7;
                else if (msgevent.p6 != -1) s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4 + " " + msgevent.p5 + " " + msgevent.p6;
                else if (msgevent.p5 != -1) s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4 + " " + msgevent.p5;
                else if (msgevent.p4 != -1) s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4;
                else if (msgevent.p3 != -1) s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3;
                else if (msgevent.p2 != -1) s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2;
                else s = "" + l1 + " " + msgevent.id + " " + msgevent.p1;
                if (msgevent.arg0 == null && msgevent.arg1 == null) sectfile.lineAdd(i, s);
                else if (msgevent.arg1 == null) sectfile.lineAdd(i, s + " " + msgevent.arg0);
                else sectfile.lineAdd(i, s + " " + msgevent.arg0 + " " + msgevent.arg1);
            }

        } catch (Exception exception) {
            System.out.println("Track file saved failed: " + exception.getMessage());
            return false;
        }
        return true;
    }

    // TODO: +++ Disable old "TRK" track recording
    private boolean isDisabled() {
        return !Config.cur.saveTrk && !this.bNet;
    }
    // TODO: --- Disable old "TRK" track recording

    public KeyRecord() {
        this.playLst = new LinkedList();
        this.recordLst = new LinkedList();
        this.excludeCmdEnv = new HashMap();
        this.excludePrevCmd = new HashMapInt();
        this.bPlay = false;
        this.bSave = false;
        this.bNet = false;
        this.bFirstHotCmd = true;
        this.fingers = new int[9];
        this.fingersSize = 0;
        this.tickMsg = new MsgTimeOut();
        this.curPlayArg0 = -1;
        this.curPlayArg1 = -1;
        this.curPlaySArg0 = null;
        this.curPlaySArg1 = null;
        this.bEnablePlayArgs = true;
        this.prevRecordId = 0;
        this.prevRecordArg0 = -1;
        this.prevRecordArg1 = -1;
        this.curRecordArg0 = -1;
        this.curRecordArg1 = -1;
        this.curRecordSArg0 = null;
        this.curRecordSArg1 = null;
        this.tickMsg.setListener(this);
        this.tickMsg.setNotCleanAfterSend();
        this.tickMsg.setFlags(24);
        RTSConf.cur.hotKeyCmdEnvs.msgAddListener(this, null);
        this.net = new NetKeyRecord(9);
    }

    private LinkedList        playLst;
    private LinkedList        recordLst;
    private HashMap           excludeCmdEnv;
    private HashMapInt        excludePrevCmd;
    private boolean           bPlay;
    private boolean           bSave;
    private boolean           bNet;
    private KeyRecordCallback callBack;
    private boolean           bFirstHotCmd;
    private KeyRecordFinger   fingerCalculator;
    private FingerPlayer      fingerPlayer;
    private int               fingers[];
    private int               fingersSize;
    private HotPlayer         hotPlayer;
    private MsgTimeOut        tickMsg;
    private NetKeyRecord      net;
    public static final int   ArgNONE = -1;
    private int               curPlayArg0;
    private int               curPlayArg1;
    private String            curPlaySArg0;
    private String            curPlaySArg1;
    private boolean           bEnablePlayArgs;
    private int               prevRecordId;
    private int               prevRecordArg0;
    private int               prevRecordArg1;
    private int               curRecordArg0;
    private int               curRecordArg1;
    private String            curRecordSArg0;
    private String            curRecordSArg1;
}
