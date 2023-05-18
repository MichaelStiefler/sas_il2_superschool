package com.maddox.rts;

import java.util.List;

import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;

public abstract class HotKeyCmd {
    protected HotKeyCmdEnv hotKeyCmdEnv;
    protected String       sName;
    public String          sortingName;
    protected boolean      bActive     = false;

    protected boolean      bEnabled    = true;

    protected boolean      bRealTime   = true;
    public int             modifierKey;
    public int             key;
    private int            recordId    = 0;

    static HashMapInt      mapRecorded = new HashMapInt();

    public HotKeyCmdEnv hotKeyCmdEnv() {
        return this.hotKeyCmdEnv;
    }

    public int recordId() {
        return this.recordId;
    }

    protected void setRecordId(int i) {
        this.recordId = i;
        HotKeyCmd.mapRecorded.put(this.recordId, this);
    }

    public String name() {
        return this.sName;
    }

    public boolean isDisableIfTimePaused() {
        return false;
    }

    public boolean isRealTime() {
        return this.bRealTime;
    }

    public boolean isTickInTime(boolean flag) {
        return flag == this.bRealTime;
    }

    public boolean isActive() {
        return this.bActive;
    }

    public boolean isEnabled() {
        return this.bEnabled;
    }

    public void enable(boolean flag) {
        if (RTSConf.cur.console.getEnv().existAtom("DEBUG_CANOPY", true)) {
            System.out.println("### UP3.4 CockpitDoor DEBUG ### HotKeyCmd.enable(" + flag + "), name=" + this.name());
        }
        this.bEnabled = flag;
    }

    public void begin() {
    }

    public void tick() {
    }

    public void end() {
    }

    public final void start(int i, int j) {
        if (i != 538 && i != 539 && j != 716 && RTSConf.cur.console.getEnv().existAtom("DEBUG_CANOPY", true) && !this.name().startsWith("trim")) {
            System.out.println("### UP3.4 CockpitDoor DEBUG ### HotKeyCmd.start(" + i + ", " + j + "), name=" + this.name());
        }
        this.modifierKey = i;
        this.key = j;
        this.bActive = true;
        try {
            this.begin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void play() {
        if (this.bActive) {
            try {
                this.tick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public final void stop() {
        if (RTSConf.cur.console.getEnv().existAtom("DEBUG_CANOPY", true) && !this.name().startsWith("trim")) {
            System.out.println("### UP3.4 CockpitDoor DEBUG ### HotKeyCmd.stop(), name=" + this.name());
        }
        this.bActive = false;
        try {
            this.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final void _cancel() {
        if (RTSConf.cur.console.getEnv().existAtom("DEBUG_CANOPY", true)) {
            System.out.println("### UP3.4 CockpitDoor DEBUG ### HotKeyCmd._cancel(), name=" + this.name());
        }
        this.bActive = false;
    }

    public void created() {
    }

    public HotKeyCmd(boolean flag, String s) {
        this(flag, s, s);
    }

    public HotKeyCmd(boolean flag, String s1, String s2) {
        this.bRealTime = flag;
        this.sName = s1;
        this.sortingName = s2;
        this.created();
    }

    public static HotKeyCmd getByRecordedId(int i) {
        return (HotKeyCmd) HotKeyCmd.mapRecorded.get(i);
    }

    public static int exec(double d, String s1, String s2) {
        return HotKeyCmd.exec(false, false, d, s1, s2);
    }

    public static int exec(String s1, String s2) {
        int i = HotKeyCmd.exec(true, true, 0.0D, s1, s2);
        if (i > 0) {
            HotKeyCmd.exec(true, false, 0.0D, s1, s2);
        }
        return i;
    }

    public static int exec(boolean flag, String s1, String s2) {
        return HotKeyCmd.exec(true, flag, 0.0D, s1, s2);
    }

    private static int exec(boolean flag1, boolean flag2, double d, String s1, String s2) {
        int i = 0;
        boolean bool = Time.isPaused();
        HashMapExt hashMapExt = RTSConf.cur.hotKeyEnvs.active;
        List list = HotKeyCmdEnv.allEnv();
        for (int j = 0; j < list.size(); j++) {
            HotKeyCmdEnv hotKeyCmdEnv = (HotKeyCmdEnv) list.get(j);
            if ((!hotKeyCmdEnv.isEnabled()) || ((s1 != null) && (!s1.equals(hotKeyCmdEnv.name())))) {
                continue;
            }
            HotKeyCmd hotKeyCmd = hotKeyCmdEnv.get(s2);
            if ((hotKeyCmd == null) || (!hotKeyCmd.isEnabled()) || ((bool) && (!hotKeyCmd.isRealTime()))) {
                continue;
            }
            if (flag1) {
                if (flag2) {
                    if (!hotKeyCmd.isActive()) {
                        hashMapExt.put(hotKeyCmd, null);
                        RTSConf.cur.hotKeyCmdEnvs.post(hotKeyCmd, true, true);
                        hotKeyCmd.start(0, 0);
                        RTSConf.cur.hotKeyCmdEnvs.post(hotKeyCmd, true, false);
                        i++;
                    }
                } else if (hotKeyCmd.isActive()) {
                    hashMapExt.remove(hotKeyCmd);
                    RTSConf.cur.hotKeyCmdEnvs.post(hotKeyCmd, false, true);
                    hotKeyCmd.stop();
                    RTSConf.cur.hotKeyCmdEnvs.post(hotKeyCmd, false, false);
                    i++;
                }

            } else if (!hotKeyCmd.isActive()) {
                hashMapExt.put(hotKeyCmd, null);
                RTSConf.cur.hotKeyCmdEnvs.post(hotKeyCmd, true, true);
                hotKeyCmd.start(0, 0);
                RTSConf.cur.hotKeyCmdEnvs.post(hotKeyCmd, true, false);
                if (d > 0.0D) {
                    new MsgAction(hotKeyCmd.isRealTime() ? 64 : 0, d, hotKeyCmd) {
                        public void doAction(Object paramObject) {
                            HotKeyCmd localHotKeyCmd = (HotKeyCmd) paramObject;
                            if (localHotKeyCmd.isActive()) {
                                RTSConf.cur.hotKeyEnvs.active.remove(localHotKeyCmd);
                                RTSConf.cur.hotKeyCmdEnvs.post(localHotKeyCmd, false, true);
                                localHotKeyCmd.stop();
                                RTSConf.cur.hotKeyCmdEnvs.post(localHotKeyCmd, false, false);
                            }
                        }
                    };
                } else {
                    hashMapExt.remove(hotKeyCmd);
                    RTSConf.cur.hotKeyCmdEnvs.post(hotKeyCmd, false, true);
                    hotKeyCmd.stop();
                    RTSConf.cur.hotKeyCmdEnvs.post(hotKeyCmd, false, false);
                }
                i++;
            }

        }

        return i;
    }

    public void _exec(boolean flag) {
        if (RTSConf.cur.console.getEnv().existAtom("DEBUG_CANOPY", true)) {
            System.out.println("### UP3.4 CockpitDoor DEBUG ### HotKeyCmd._exec(" + flag + ") Time.isPaused()=" + Time.isPaused() +
            ", isRealTime()=" + this.isRealTime() + 
            ", hotKeyCmdEnv.isEnabled()=" + this.hotKeyCmdEnv.isEnabled() + 
            ", isActive()=" + this.isActive());
        }
        if (((Time.isPaused()) && (!this.isRealTime())) || !this.hotKeyCmdEnv.isEnabled()) {
            return;
        }
        HashMapExt hashMapExt = RTSConf.cur.hotKeyEnvs.active;
        if (flag) {
            if (!this.isActive()) {
                hashMapExt.put(this, null);
                RTSConf.cur.hotKeyCmdEnvs.post(this, true, true);
                this.start(0, 0);
                RTSConf.cur.hotKeyCmdEnvs.post(this, true, false);
            }
        } else if (this.isActive()) {
            hashMapExt.remove(this);
            RTSConf.cur.hotKeyCmdEnvs.post(this, false, true);
            this.stop();
            RTSConf.cur.hotKeyCmdEnvs.post(this, false, false);
        }
    }
}
