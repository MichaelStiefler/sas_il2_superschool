package com.maddox.rts;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.maddox.il2.engine.Config;
import com.maddox.util.HashMapInt;
import com.maddox.util.SharedTokenizer;

public class KeyRecord
    implements MsgHotKeyCmdListener, MsgTimeOutListener
{
    class NetKeyRecord extends NetObj
    {

        public void post(MsgEvent msgevent)
        {
            if(!isRecording())
                return;
            if(out == null || out.isDestroying())
                return;
            try
            {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeInt(msgevent.id);
                if(msgevent.p9 != -1)
                {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                    netmsgguaranted.writeInt(msgevent.p5);
                    netmsgguaranted.writeInt(msgevent.p6);
                    netmsgguaranted.writeInt(msgevent.p7);
                    netmsgguaranted.writeInt(msgevent.p8);
                    netmsgguaranted.writeInt(msgevent.p9);
                } else
                if(msgevent.p8 != -1)
                {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                    netmsgguaranted.writeInt(msgevent.p5);
                    netmsgguaranted.writeInt(msgevent.p6);
                    netmsgguaranted.writeInt(msgevent.p7);
                    netmsgguaranted.writeInt(msgevent.p8);
                } else
                if(msgevent.p7 != -1)
                {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                    netmsgguaranted.writeInt(msgevent.p5);
                    netmsgguaranted.writeInt(msgevent.p6);
                    netmsgguaranted.writeInt(msgevent.p7);
                } else
                if(msgevent.p6 != -1)
                {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                    netmsgguaranted.writeInt(msgevent.p5);
                    netmsgguaranted.writeInt(msgevent.p6);
                } else
                if(msgevent.p5 != -1)
                {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                    netmsgguaranted.writeInt(msgevent.p5);
                } else
                if(msgevent.p4 != -1)
                {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                    netmsgguaranted.writeInt(msgevent.p4);
                } else
                if(msgevent.p3 != -1)
                {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                    netmsgguaranted.writeInt(msgevent.p3);
                } else
                if(msgevent.p2 != -1)
                {
                    netmsgguaranted.writeInt(msgevent.p1);
                    netmsgguaranted.writeInt(msgevent.p2);
                } else
                {
                    netmsgguaranted.writeInt(msgevent.p1);
                }
                postTo(out, netmsgguaranted);
                out.flush();
            }
            catch(Exception exception)
            {
                printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(!isPlaying())
                return true;
            MsgEvent msgevent = new MsgEvent();
            msgevent.time = Time.current();
            msgevent.id = netmsginput.readInt();
            if(netmsginput.available() != 0)
            {
                msgevent.p1 = netmsginput.readInt();
                if(netmsginput.available() != 0)
                {
                    msgevent.p2 = netmsginput.readInt();
                    if(netmsginput.available() != 0)
                    {
                        msgevent.p3 = netmsginput.readInt();
                        if(netmsginput.available() != 0)
                        {
                            msgevent.p4 = netmsginput.readInt();
                            if(netmsginput.available() != 0)
                            {
                                msgevent.p5 = netmsginput.readInt();
                                if(netmsginput.available() != 0)
                                {
                                    msgevent.p6 = netmsginput.readInt();
                                    if(netmsginput.available() != 0)
                                    {
                                        msgevent.p7 = netmsginput.readInt();
                                        if(netmsginput.available() != 0)
                                        {
                                            msgevent.p8 = netmsginput.readInt();
                                            if(netmsginput.available() != 0)
                                            {
                                                msgevent.p9 = netmsginput.readInt();
                                                if(netmsginput.available() != 0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            hotPlayer.msgTimeOut(msgevent);
            return true;
        }

        public void msgNetNewChannel(NetChannel netchannel)
        {
            if(netchannel instanceof NetChannelOutStream)
                out = (NetChannelOutStream)netchannel;
        }

        public void msgNetDelChannel(NetChannel netchannel)
        {
            if(netchannel == out)
                out = null;
            else
            if((netchannel instanceof NetChannelInStream) && isPlaying() && callBack != null)
                callBack.playRecordedEnded();
        }

        NetChannelOutStream out;

        public NetKeyRecord(int i)
        {
            super(null, i);
        }
    }

    class HotPlayer
        implements MsgTimeOutListener
    {

        public void msgTimeOut(Object obj)
        {
            if(hotPlayer != this)
                return;
            MsgEvent msgevent = (MsgEvent)obj;
            HotKeyCmd hotkeycmd = HotKeyCmd.getByRecordedId(msgevent.id);
            if(hotkeycmd == null)
                return;
            if(hotkeycmd instanceof HotKeyCmdMouseMove)
            {
                if(!hotkeycmd.hotKeyCmdEnv().hotKeyEnv().isEnabled())
                {
                    HotKeyCmdMouseMove hotkeycmdmousemove = (HotKeyCmdMouseMove)hotkeycmd;
                    hotkeycmdmousemove._exec(msgevent.p1, msgevent.p2, 0);
                }
            } else
            if(hotkeycmd instanceof HotKeyCmdTrackIRAngles)
            {
                if(!hotkeycmd.hotKeyCmdEnv().hotKeyEnv().isEnabled())
                {
                    HotKeyCmdTrackIRAngles hotkeycmdtrackirangles = (HotKeyCmdTrackIRAngles)hotkeycmd;
                    hotkeycmdtrackirangles._exec((float)msgevent.p1 / 100F, (float)msgevent.p2 / 100F, (float)msgevent.p3 / 100F);
                }
            } else
            if(hotkeycmd instanceof HotKeyCmdMove)
            {
                if(!hotkeycmd.hotKeyCmdEnv().hotKeyEnv().isEnabled())
                {
                    HotKeyCmdMove hotkeycmdmove = (HotKeyCmdMove)hotkeycmd;
                    hotkeycmdmove._exec(msgevent.p1);
                }
            } else
            if(hotkeycmd instanceof HotKeyCmdRedirect)
            {
                if(!hotkeycmd.hotKeyCmdEnv().hotKeyEnv().isEnabled())
                {
                    HotKeyCmdRedirect hotkeycmdredirect = (HotKeyCmdRedirect)hotkeycmd;
                    hotkeycmdredirect._exec(msgevent.p2, msgevent.p3, msgevent.p4, msgevent.p5, msgevent.p6, msgevent.p7, msgevent.p8, msgevent.p9);
                }
            } else
            if(!hotkeycmd.hotKeyCmdEnv().hotKeyEnv().isEnabled() || bFirstHotCmd)
            {
                curPlayArg0 = msgevent.p2;
                curPlayArg1 = msgevent.p3;
                curPlaySArg0 = msgevent.arg0;
                curPlaySArg1 = msgevent.arg1;
                if(bFirstHotCmd && callBack != null)
                {
                    callBack.doFirstHotCmd(true);
                    hotkeycmd._exec(msgevent.p1 == 1);
                    callBack.doFirstHotCmd(false);
                } else
                {
                    hotkeycmd._exec(msgevent.p1 == 1);
                }
                if(bFirstHotCmd && msgevent.p1 == 1)
                    hotkeycmd._exec(false);
                bFirstHotCmd = false;
            }
        }

        public HotPlayer()
        {
            bFirstHotCmd = true;
        }
    }

    class FingerPlayer
        implements MsgTimeOutListener
    {

        public void msgTimeOut(Object obj)
        {
            if(fingerCalculator == null)
                return;
            if(fingerPlayer != this)
                return;
            if(obj == null)
            {
                if(bSave)
                {
                    int ai[] = fingerCalculator.calculateFingers();
                    recordLst.add(new MsgEvent(Time.current(), 0, ai, fingersSize));
                    MsgTimeOut.post(Time.current() + (long)fingerCalculator.checkPeriod(), 0x80000001, this, null);
                }
            } else
            if(bPlay)
            {
                MsgEvent msgevent = (MsgEvent)obj;
                fingers[0] = msgevent.p1;
                if(fingersSize > 1)
                {
                    fingers[1] = msgevent.p2;
                    if(fingersSize > 2)
                    {
                        fingers[2] = msgevent.p3;
                        if(fingersSize > 3)
                        {
                            fingers[3] = msgevent.p4;
                            if(fingersSize > 4)
                            {
                                fingers[4] = msgevent.p5;
                                if(fingersSize > 5)
                                {
                                    fingers[5] = msgevent.p6;
                                    if(fingersSize > 6)
                                    {
                                        fingers[6] = msgevent.p7;
                                        if(fingersSize > 7)
                                        {
                                            fingers[7] = msgevent.p8;
                                            if(fingersSize > 8)
                                                fingers[8] = msgevent.p9;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                fingerCalculator.checkFingers(fingers);
            }
        }

        FingerPlayer()
        {
        }
    }


    public void addExcludeCmdEnv(String s)
    {
        excludeCmdEnv.put(s, null);
    }

    public void clearListExcludeCmdEnv()
    {
        excludeCmdEnv.clear();
    }

    public void addExcludePrevCmd(int i)
    {
        excludePrevCmd.put(i, null);
    }

    public void setEnablePlayArgs(boolean flag)
    {
        bEnablePlayArgs = flag;
    }

    public boolean isEnablePlayArgs()
    {
        return bEnablePlayArgs || bFirstHotCmd;
    }

    public boolean isExistPlayArgs()
    {
        return (bEnablePlayArgs || bFirstHotCmd) && _isExistPlayArgs();
    }

    public boolean _isExistPlayArgs()
    {
        return curPlayArg0 != -1 || curPlayArg1 != -1;
    }

    public boolean isExistPlaySArgs()
    {
        return (bEnablePlayArgs || bFirstHotCmd) && _isExistPlaySArgs();
    }

    public boolean _isExistPlaySArgs()
    {
        return curPlaySArg0 != null || curPlaySArg1 != null;
    }

    public int getPlayArg0()
    {
        return bEnablePlayArgs ? curPlayArg0 : -1;
    }

    public int getPlayArg1()
    {
        return bEnablePlayArgs ? curPlayArg1 : -1;
    }

    public String getPlaySArg0()
    {
        return bEnablePlayArgs ? curPlaySArg0 : null;
    }

    public String getPlaySArg1()
    {
        return bEnablePlayArgs ? curPlaySArg1 : null;
    }

    public int _getPlayArg0()
    {
        return curPlayArg0;
    }

    public int _getPlayArg1()
    {
        return curPlayArg1;
    }

    public String _getPlaySArg0()
    {
        return curPlaySArg0;
    }

    public String _getPlaySArg1()
    {
        return curPlaySArg1;
    }

    public void msgTimeOut(Object obj)
    {
        try
        {
            long l = Time.current() + (long)(2 * Time.tickLen());
            long l1 = l;
            boolean flag = playLst.size() > 0;
            for(; playLst.size() > 0; playLst.removeFirst())
            {
                MsgEvent msgevent = (MsgEvent)playLst.getFirst();
                if(msgevent.time > l)
                    break;
                l1 = msgevent.time;
                if(msgevent.id == 0)
                {
                    if(fingerPlayer != null)
                        MsgTimeOut.post(msgevent.time, 0x80000001, fingerPlayer, msgevent);
                    continue;
                }
                HotKeyCmd hotkeycmd = HotKeyCmd.getByRecordedId(msgevent.id);
                if(hotkeycmd != null)
                    MsgTimeOut.post(msgevent.time, hotPlayer, msgevent);
            }

            if(playLst.size() > 0)
            {
                tickMsg.setFlags(24);
                tickMsg.post();
            } else
            if(flag)
            {
                tickMsg.setFlags(0);
                tickMsg.setTime(l1);
                tickMsg.post();
            } else
            if(callBack != null)
                callBack.playRecordedEnded();
        }
        catch(Exception exception)
        {
            if(callBack != null)
                callBack.playRecordedEnded();
        }
    }

    public void setCurRecordArgs(NetObj netobj, NetObj netobj1)
    {
        if(netobj != null)
            curRecordArg0 = netobj.idLocal();
        if(netobj1 != null)
            curRecordArg1 = netobj1.idLocal();
    }

    public void setCurRecordArg0(NetObj netobj)
    {
        if(netobj != null)
            curRecordArg0 = netobj.idLocal();
    }

    public void setCurRecordArg1(NetObj netobj)
    {
        if(netobj != null)
            curRecordArg1 = netobj.idLocal();
    }

    public void setCurRecordSArgs(String s, String s1)
    {
        curRecordSArg0 = s;
        curRecordSArg1 = s1;
    }

    public void setCurRecordSArg0(String s)
    {
        curRecordSArg0 = s;
    }

    public void setCurRecordSArg1(String s)
    {
        curRecordSArg1 = s;
    }

    public void msgHotKeyCmd(HotKeyCmd hotkeycmd, boolean flag, boolean flag1)
    {
        if(hotkeycmd.recordId() != 0)
        {
            if(excludeCmdEnv.containsKey(hotkeycmd.hotKeyCmdEnv().name()))
                return;
            if(hotkeycmd instanceof HotKeyCmdMouseMove)
            {
                // TODO: +++ Disable old "TRK" track recording
                // if(!bSave || flag1)
                if(!bSave || flag1 || isDisabled())
                // TODO: --- Disable old "TRK" track recording
                    return;
                HotKeyCmdMouseMove hotkeycmdmousemove = (HotKeyCmdMouseMove)hotkeycmd;
                saveEvent(new MsgEvent(Time.current(), hotkeycmd.recordId(), hotkeycmdmousemove._dx, hotkeycmdmousemove._dy));
            } else
            if(hotkeycmd instanceof HotKeyCmdTrackIRAngles)
            {
                // TODO: +++ Disable old "TRK" track recording
                // if(!bSave || flag1)
                if(!bSave || flag1 || isDisabled())
                // TODO: --- Disable old "TRK" track recording
                    return;
                HotKeyCmdTrackIRAngles hotkeycmdtrackirangles = (HotKeyCmdTrackIRAngles)hotkeycmd;
                saveEvent(new MsgEvent(Time.current(), hotkeycmd.recordId(), (int)(hotkeycmdtrackirangles._yaw * 100F), (int)(hotkeycmdtrackirangles._pitch * 100F), (int)(hotkeycmdtrackirangles._roll * 100F)));
            } else
            if(hotkeycmd instanceof HotKeyCmdMove)
            {
                // TODO: +++ Disable old "TRK" track recording
                // if(!bSave || flag1)
                if(!bSave || flag1 || isDisabled())
                // TODO: --- Disable old "TRK" track recording
                    return;
                HotKeyCmdMove hotkeycmdmove = (HotKeyCmdMove)hotkeycmd;
                saveEvent(new MsgEvent(Time.current(), hotkeycmd.recordId(), hotkeycmdmove._mov));
            } else
            if(hotkeycmd instanceof HotKeyCmdRedirect)
            {
                // TODO: +++ Disable old "TRK" track recording
                // if(!bSave || flag1)
                if(!bSave || flag1 || isDisabled())
                // TODO: --- Disable old "TRK" track recording
                    return;
                HotKeyCmdRedirect hotkeycmdredirect = (HotKeyCmdRedirect)hotkeycmd;
                saveEvent(new MsgEvent(Time.current(), hotkeycmd.recordId(), hotkeycmdredirect.idRedirect(), hotkeycmdredirect._r[0], hotkeycmdredirect._r[1], hotkeycmdredirect._r[2], hotkeycmdredirect._r[3], hotkeycmdredirect._r[4], hotkeycmdredirect._r[5], hotkeycmdredirect._r[6], hotkeycmdredirect._r[7]));
            } else
            if(flag1)
            {
                curRecordArg0 = -1;
                curRecordArg1 = -1;
                curRecordSArg0 = null;
                curRecordSArg1 = null;
            } else
            {
                MsgEvent msgevent = null;
                if(curRecordArg0 != -1 || curRecordArg1 != -1)
                {
                    if(!excludePrevCmd.containsKey(hotkeycmd.recordId()))
                    {
                        prevRecordId = hotkeycmd.recordId();
                        prevRecordArg0 = curRecordArg0;
                        prevRecordArg1 = curRecordArg1;
                    }
                    if(bSave)
                        saveEvent(msgevent = new MsgEvent(Time.current(), hotkeycmd.recordId(), flag ? 1 : 0, curRecordArg0, curRecordArg1));
                } else
                if(bSave)
                    saveEvent(msgevent = new MsgEvent(Time.current(), hotkeycmd.recordId(), flag ? 1 : 0));
                if(msgevent != null)
                {
                    msgevent.arg0 = curRecordSArg0;
                    msgevent.arg1 = curRecordSArg1;
                }
            }
        }
    }

    public boolean startPlay(KeyRecordCallback keyrecordcallback)
    {
        callBack = keyrecordcallback;
        Keyboard.adapter()._clear();
        Mouse.adapter()._clear();
        Joy.adapter()._clear();
        hotPlayer = new HotPlayer();
        bPlay = true;
        return bPlay;
    }

    public boolean startPlay(SectFile sectfile, int i, int j, KeyRecordCallback keyrecordcallback)
    {
        callBack = keyrecordcallback;
        bPlay = false;
        playLst.clear();
        int k = sectfile.vars(i);
        long l = 0L;
        for(int i1 = j; i1 < k; i1++)
        {
            String s = sectfile.line(i, i1);
            SharedTokenizer.set(s);
            if(SharedTokenizer.hasMoreTokens())
            {
                MsgEvent msgevent = new MsgEvent();
                long l1 = SharedTokenizer.next(0);
                l += l1;
                msgevent.time = l;
                playLst.add(parseEvent(msgevent));
            }
        }

        if(playLst.size() > 0)
        {
            Keyboard.adapter()._clear();
            Mouse.adapter()._clear();
            Joy.adapter()._clear();
            tickMsg.remove();
            tickMsg.setFlags(0);
            tickMsg.setTime(0L);
            tickMsg.post();
            hotPlayer = new HotPlayer();
        }
        bPlay = true;
        return bPlay;
    }

    private MsgEvent parseEvent(MsgEvent msgevent)
    {
        try
        {
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
        }
        catch(Exception exception)
        {
            msgevent.arg0 = SharedTokenizer._getString();
            SharedTokenizer._nextWord();
            msgevent.arg1 = SharedTokenizer._getString();
        }
        return msgevent;
    }

    public void stopPlay()
    {
        bPlay = false;
        callBack = null;
        hotPlayer = null;
        playLst.clear();
    }

    public boolean isPlaying()
    {
        return bPlay;
    }

    public boolean isRecording()
    {
        return bSave;
    }

    public boolean isContainRecorded()
    {
        return recordLst.size() > 0;
    }

    public void clearRecorded()
    {
        recordLst.clear();
    }

    public void clearPrevStates()
    {
        prevRecordId = 0;
    }

    private void saveEvent(MsgEvent msgevent)
    {
        if(bNet)
            net.post(msgevent);
        else
            // TODO: +++ Disable old "TRK" track recording
        	if(Config.cur.saveTrk)
            // TODO: --- Disable old "TRK" track recording
            recordLst.add(msgevent);
    }

    public void startRecordingNet()
    {
        bSave = true;
        bNet = true;
        fingerPlayer = null;
        if(prevRecordId != 0)
        {
            saveEvent(new MsgEvent(Time.current(), prevRecordId, 1, prevRecordArg0, prevRecordArg1));
            saveEvent(new MsgEvent(Time.current(), prevRecordId, 0, prevRecordArg0, prevRecordArg1));
        }
    }

    public void startRecording()
    {
        bSave = true;
        bNet = false;
        if(fingerCalculator != null)
        {
            fingerPlayer = new FingerPlayer();
            MsgTimeOut.post(Time.current(), 0x80000001, fingerPlayer, null);
        }
    }

    public void stopRecording(boolean flag)
    {
        if(flag)
            saveEvent(new MsgEvent(Time.current(), -1, 0));
        bSave = false;
        fingerPlayer = null;
    }

    public void setFingerCalculator(KeyRecordFinger keyrecordfinger)
    {
        fingerCalculator = keyrecordfinger;
        fingersSize = fingerCalculator.countSaveFingers();
    }

    public boolean saveRecorded(SectFile sectfile, int i)
    {
        if(recordLst.size() > 0)
            try
            {
                long l = 0L;
                for(Iterator iterator = recordLst.iterator(); iterator.hasNext();)
                {
                    MsgEvent msgevent = (MsgEvent)iterator.next();
                    long l1 = msgevent.time - l;
                    l = msgevent.time;
                    String s = null;
                    if(msgevent.p9 != -1)
                        s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4 + " " + msgevent.p5 + " " + msgevent.p6 + " " + msgevent.p7 + " " + msgevent.p8 + " " + msgevent.p9;
                    else
                    if(msgevent.p8 != -1)
                        s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4 + " " + msgevent.p5 + " " + msgevent.p6 + " " + msgevent.p7 + " " + msgevent.p8;
                    else
                    if(msgevent.p7 != -1)
                        s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4 + " " + msgevent.p5 + " " + msgevent.p6 + " " + msgevent.p7;
                    else
                    if(msgevent.p6 != -1)
                        s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4 + " " + msgevent.p5 + " " + msgevent.p6;
                    else
                    if(msgevent.p5 != -1)
                        s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4 + " " + msgevent.p5;
                    else
                    if(msgevent.p4 != -1)
                        s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3 + " " + msgevent.p4;
                    else
                    if(msgevent.p3 != -1)
                        s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2 + " " + msgevent.p3;
                    else
                    if(msgevent.p2 != -1)
                        s = "" + l1 + " " + msgevent.id + " " + msgevent.p1 + " " + msgevent.p2;
                    else
                        s = "" + l1 + " " + msgevent.id + " " + msgevent.p1;
                    if(msgevent.arg0 == null && msgevent.arg1 == null)
                        sectfile.lineAdd(i, s);
                    else
                    if(msgevent.arg1 == null)
                        sectfile.lineAdd(i, s + " " + msgevent.arg0);
                    else
                        sectfile.lineAdd(i, s + " " + msgevent.arg0 + " " + msgevent.arg1);
                }

            }
            catch(Exception exception)
            {
                System.out.println("Track file saved failed: " + exception.getMessage());
                return false;
            }
        return true;
    }

    // TODO: +++ Disable old "TRK" track recording
    private boolean isDisabled()
    {
        return !Config.cur.saveTrk && !bNet;
    }
    // TODO: --- Disable old "TRK" track recording


    public KeyRecord()
    {
        playLst = new LinkedList();
        recordLst = new LinkedList();
        excludeCmdEnv = new HashMap();
        excludePrevCmd = new HashMapInt();
        bPlay = false;
        bSave = false;
        bNet = false;
        bFirstHotCmd = true;
        fingers = new int[9];
        fingersSize = 0;
        tickMsg = new MsgTimeOut();
        curPlayArg0 = -1;
        curPlayArg1 = -1;
        curPlaySArg0 = null;
        curPlaySArg1 = null;
        bEnablePlayArgs = true;
        prevRecordId = 0;
        prevRecordArg0 = -1;
        prevRecordArg1 = -1;
        curRecordArg0 = -1;
        curRecordArg1 = -1;
        curRecordSArg0 = null;
        curRecordSArg1 = null;
        tickMsg.setListener(this);
        tickMsg.setNotCleanAfterSend();
        tickMsg.setFlags(24);
        RTSConf.cur.hotKeyCmdEnvs.msgAddListener(this, null);
        net = new NetKeyRecord(9);
    }

    private LinkedList playLst;
    private LinkedList recordLst;
    private HashMap excludeCmdEnv;
    private HashMapInt excludePrevCmd;
    private boolean bPlay;
    private boolean bSave;
    private boolean bNet;
    private KeyRecordCallback callBack;
    private boolean bFirstHotCmd;
    private KeyRecordFinger fingerCalculator;
    private FingerPlayer fingerPlayer;
    private int fingers[];
    private int fingersSize;
    private HotPlayer hotPlayer;
    private MsgTimeOut tickMsg;
    private NetKeyRecord net;
    public static final int ArgNONE = -1;
    private int curPlayArg0;
    private int curPlayArg1;
    private String curPlaySArg0;
    private String curPlaySArg1;
    private boolean bEnablePlayArgs;
    private int prevRecordId;
    private int prevRecordArg0;
    private int prevRecordArg1;
    private int curRecordArg0;
    private int curRecordArg1;
    private String curRecordSArg0;
    private String curRecordSArg1;
}
