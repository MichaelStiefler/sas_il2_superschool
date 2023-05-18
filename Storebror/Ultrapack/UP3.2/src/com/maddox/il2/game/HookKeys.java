package com.maddox.il2.game;

import java.util.ArrayList;

import com.maddox.il2.engine.hotkey.HookGunner;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.engine.hotkey.HookView;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyCmdMouseMove;
import com.maddox.rts.HotKeyCmdTrackIRAngles;

public class HookKeys {
    class HotKeyCmdSnap extends HotKeyCmd {

        boolean b;

        public HotKeyCmdSnap(boolean flag, String s, String s1) {
            super(flag, s, s1);
            HookKeys.this.snapLst.add(this);
        }
    }

    public void resetGame() {
        this.az = 0;
        this.tan = 0;
        this.actAz = 0;
        this.actTan = 0;
        int i = this.snapLst.size();
        for (int j = 0; j < i; j++)
            ((HotKeyCmdSnap) this.snapLst.get(j)).b = false;

    }

    public void setMode(boolean flag) {
        this.bPanView = flag;
        this.az = 0;
        this.tan = 0;
        this.actAz = 0;
        this.actTan = 0;
        int i = this.snapLst.size();
        for (int j = 0; j < i; j++)
            ((HotKeyCmdSnap) this.snapLst.get(j)).b = false;

    }

    public boolean isPanView() {
        return this.bPanView;
    }

    private boolean snapSet(boolean flag, int i, int j) {
        if (this.bPanView)
            return false;
        if (flag) {
            if (i != 10) {
                this.actAz++;
                this.az += i;
            }
            if (j != 10) {
                this.actTan++;
                this.tan += j;
            }
        } else {
            if (i != 10) {
                this.actAz--;
                this.az -= i;
            }
            if (j != 10) {
                this.actTan--;
                this.tan -= j;
            }
        }
        float f = this.actAz <= 0 ? 0.0F : (float) this.az / (float) this.actAz;
        float f1 = this.actTan <= 0 ? 0.0F : (float) this.tan / (float) this.actTan;
        HookPilot.current.snapSet(f, f1);
        HookGunner.doSnapSet(f, f1);
        HookView.current.snapSet(f, f1);
        return true;
    }

    private void panSet(int i, int j) {
        if (!this.bPanView) {
            return;
        } else {
            HookPilot.current.panSet(i, j);
            HookGunner.doPanSet(i, j);
            HookView.current.panSet(i, j);
            return;
        }
    }

    private void initSnapKeys() {
        String s = "SnapView";
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "SnapPanSwitch", "00") {

            public void begin() {
                HookKeys.this.setMode(!HookKeys.this.isPanView());
            }

            public void created() {
                this.setRecordId(60);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_0_0", "01") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 0, 0);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 0, 0);
            }

            public void created() {
                this.setRecordId(61);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_0_1", "02") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 0, 1);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 0, 1);
            }

            public void created() {
                this.setRecordId(62);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_0_m1", "03") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 0, -1);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 0, -1);
            }

            public void created() {
                this.setRecordId(63);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_m1_0", "04") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, -1, 0);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, -1, 0);
            }

            public void created() {
                this.setRecordId(64);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_1_0", "05") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 1, 0);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 1, 0);
            }

            public void created() {
                this.setRecordId(65);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_m1_1", "06") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, -1, 1);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, -1, 1);
            }

            public void created() {
                this.setRecordId(66);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_1_1", "07") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 1, 1);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 1, 1);
            }

            public void created() {
                this.setRecordId(67);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_m1_m1", "08") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, -1, -1);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, -1, -1);
            }

            public void created() {
                this.setRecordId(68);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_1_m1", "09") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 1, -1);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 1, -1);
            }

            public void created() {
                this.setRecordId(69);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_m3_0", "10") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, -3, 0);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, -3, 0);
            }

            public void created() {
                this.setRecordId(70);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_3_0", "11") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 3, 0);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 3, 0);
            }

            public void created() {
                this.setRecordId(71);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_m3_1", "12") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, -3, 1);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, -3, 1);
            }

            public void created() {
                this.setRecordId(72);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_3_1", "13") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 3, 1);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 3, 1);
            }

            public void created() {
                this.setRecordId(73);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_m3_m1", "14") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, -3, -1);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, -3, -1);
            }

            public void created() {
                this.setRecordId(74);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_3_m1", "15") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 3, -1);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 3, -1);
            }

            public void created() {
                this.setRecordId(75);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_0_2", "16") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 10, 2);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 10, 2);
            }

            public void created() {
                this.setRecordId(76);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_m2_2", "17") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, -2, 2);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, -2, 2);
            }

            public void created() {
                this.setRecordId(77);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_2_2", "18") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 2, 2);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 2, 2);
            }

            public void created() {
                this.setRecordId(78);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_0_m2", "19") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 10, -2);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 10, -2);
            }

            public void created() {
                this.setRecordId(79);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_m2_m2", "20") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, -2, -2);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, -2, -2);
            }

            public void created() {
                this.setRecordId(80);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_2_m2", "21") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 2, -2);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 2, -2);
            }

            public void created() {
                this.setRecordId(81);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_m2_0", "22") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, -2, 0);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, -2, 0);
            }

            public void created() {
                this.setRecordId(82);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdSnap(true, "Snap_2_0", "23") {

            public void begin() {
                this.b = HookKeys.this.snapSet(true, 2, 0);
            }

            public void end() {
                if (this.b)
                    HookKeys.this.snapSet(false, 2, 0);
            }

            public void created() {
                this.setRecordId(83);
            }

        });
    }

    private void initPanKeys() {
        String s = "PanView";
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdMouseMove(true, "Mouse") {

            public void move(int i, int j, int k) {
                if (HookPilot.current != null)
                    HookPilot.current.mouseMove(i, j, k);
            }

            public void created() {
                this.sortingName = null;
                this.setRecordId(50);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdTrackIRAngles(true, "TrackIR") {

            public void angles(float yaw, float pitch, float roll) {
                if (HookPilot.current != null)
                    HookPilot.current.viewSet(-yaw, -pitch);
                if (HookGunner.current() != null)
                    HookGunner.current().viewSet(-yaw, -pitch);
                if (HookView.current != null)
                    HookView.current.viewSet(-yaw, -pitch);
            }

            // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
            public void angles(float yaw, float pitch, float roll, float headX, float headY, float headZ) {
                if (HookPilot.current != null) {
                    HookPilot.current.viewSet(-yaw, -pitch, -roll);
//                    HookPilot.current.moveHead(headX, headY, headZ);
                }
                if (HookGunner.current() != null)
                    HookGunner.current().viewSet(-yaw, -pitch);
                if (HookView.current != null)
                    HookView.current.viewSet(-yaw, -pitch);
            }
            // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---

            public void created() {
                this.sortingName = null;
                this.setRecordId(53);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "PanReset", "01") {

            public void begin() {
                HookKeys.this.panSet(0, 0);
            }

            public void created() {
                this.setRecordId(90);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "PanUp", "02") {

            public void begin() {
                HookKeys.this.panSet(0, 1);
            }

            public void created() {
                this.setRecordId(97);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "PanDown", "03") {

            public void begin() {
                HookKeys.this.panSet(0, -1);
            }

            public void created() {
                this.setRecordId(98);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "PanLeft2", "04") {

            public void begin() {
                HookKeys.this.panSet(-1, 0);
            }

            public void created() {
                this.setRecordId(92);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "PanRight2", "05") {

            public void begin() {
                HookKeys.this.panSet(1, 0);
            }

            public void created() {
                this.setRecordId(95);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "PanLeft", "06") {

            public void begin() {
                HookKeys.this.panSet(-1, 1);
            }

            public void created() {
                this.setRecordId(91);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "PanRight", "07") {

            public void begin() {
                HookKeys.this.panSet(1, 1);
            }

            public void created() {
                this.setRecordId(94);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "PanLeft3", "08") {

            public void begin() {
                HookKeys.this.panSet(-1, -1);
            }

            public void created() {
                this.setRecordId(93);
            }

        });
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "PanRight3", "09") {

            public void begin() {
                HookKeys.this.panSet(1, -1);
            }

            public void created() {
                this.setRecordId(96);
            }

        });
    }

    private HookKeys() {
        this.bPanView = true;
        this.az = 0;
        this.tan = 0;
        this.actAz = 0;
        this.actTan = 0;
        this.snapLst = new ArrayList();
        this.initPanKeys();
        this.initSnapKeys();
    }

    public static HookKeys New() {
        if (current == null)
            current = new HookKeys();
        return current;
    }

    public boolean         bPanView;
    private int            az;
    private int            tan;
    private int            actAz;
    private int            actTan;
    private ArrayList      snapLst;
    public static HookKeys current;

}
