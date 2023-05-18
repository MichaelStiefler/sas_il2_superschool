package com.maddox.rts;

public class RTSConfWin extends RTSConf {

    private int doUseMouse(int i) {
        this.mouse.setMouseCursorAdapter(null);
        if (i == 0) {
            this.mouseDX.destroy();
            this.mouseWin.destroy();
            this.mouse.setComputePos(false, false);
        } else if (i == 1) {
            if (this.mainWindow.hWnd() != 0) {
                this.mouseDX.destroy();
                this.mouseWin.create();
                this.mouse.setComputePos(false, true);
                this.mouse.setMouseCursorAdapter(this.mouseWin);
            }
        } else if (this.mainWindow.hWnd() != 0) {
            this.mouseDX.create(2);
            this.mouseWin.destroy();
            this.mouse.setComputePos(true, false);
        }
        this.mouse._clear();
        return i;
    }

    public void setUseMouse(int i) {
        if (this.useMouse == i) return;
        else {
            super.setUseMouse(this.doUseMouse(i));
            return;
        }
    }

    private boolean doUseJoy(boolean flag) {
        if (flag) {
            if (this.mainWindow.hWnd() != 0) try {
                this.joyDX.create();
            } catch (Exception exception) {
                System.out.println("DirectX Joystick NOT created: " + exception.getMessage());
                try {
                    this.joyWin.create();
                } catch (Exception exception1) {}
            }
            this.joy._clear();
            return true;
        } else {
            this.joyDX.destroy();
            this.joyWin.destroy();
            this.joy._clear();
            return false;
        }
    }

    public void useJoy(boolean flag) {
        if (this.bUseJoy == flag) return;
        else {
            super.useJoy(this.doUseJoy(flag));
            return;
        }
    }

    public void start() {
        if (!this.bStarted) {
            this.useMouse = this.doUseMouse(this.useMouse);
            if (this.mainWindow.hWnd() != 0) {
                this.keyboardDX.create();
                this.keyboardWin.create();
            }
            this.keyboard._clear();
            this.bUseJoy = this.doUseJoy(this.bUseJoy);
            if (this.bUseTrackIR) {
                this.trackIRWin.create();
                this.trackIR.setExist(this.trackIRWin.isCreated());
            }
            cur.hotKeyEnvs.resetGameCreate();
            super.start();
        }
    }

    public void stop() {
        if (this.bStarted) {
            this.doUseMouse(0);
            this.keyboardDX.destroy();
            this.keyboardWin.destroy();
            this.keyboard._clear();
            this.doUseJoy(false);
            if (this.bUseTrackIR) {
                this.trackIRWin.destroy();
                this.trackIR.setExist(false);
            }
            cur.hotKeyEnvs.resetGameClear();
            super.stop();
        }
    }

    public RTSConfWin() {
        super(null);
        this.mainWindow = new MainWin32(-10005, true);
        this.mouseDX = new MouseDX(-10004, 2, false);
        this.mouseWin = new MouseWin(-10003, false);
        this.keyboardDX = new KeyboardDX(-10002, 1, false);
        this.keyboardWin = new KeyboardWin(-10001, false);
        this.keyboardWin.setOnlyChars(true);
        this.joyDX = new JoyDX(100L, 3, false);
        this.joyWin = new JoyWin(100L, false);
        this.trackIRWin = new TrackIRWin(-10010, false);
    }

    public RTSConfWin(IniFile inifile, String s, int i) {
        super(null, inifile, s, i);
        this.mainWindow = new MainWin32(-10005, true);
        this.useMouse = inifile.get(s, "mouseUse", this.useMouse, 0, 2);
        this.mouseDX = new MouseDX(-10004, 2, false);
        this.mouseWin = new MouseWin(-10003, false);
        this.keyboardDX = new KeyboardDX(-10002, 1, false);
        this.keyboardWin = new KeyboardWin(-10001, false);
        this.keyboardWin.setOnlyChars(true);
        this.bUseJoy = inifile.get(s, "joyUse", this.bUseJoy);
        this.joyDX = new JoyDX(100L, 3, false);
        this.joyWin = new JoyWin(100L, false);
        this.bUseTrackIR = inifile.get(s, "trackIRUse", this.bUseTrackIR);
        this.trackIRWin = new TrackIRWin(-10010, false, inifile);
    }

    public MouseDX     mouseDX;
    public MouseWin    mouseWin;
    public KeyboardDX  keyboardDX;
    public KeyboardWin keyboardWin;
    public JoyDX       joyDX;
    public JoyWin      joyWin;
    public TrackIRWin  trackIRWin;
}
