package com.maddox.rts;

public class MsgKeyboard extends Message {
    public static final int PRESS   = 0;
    public static final int RELEASE = 1;
    public static final int CHAR    = 2;
    public static final int UNKNOWN = -1;
    protected int           id      = -1;
    protected int           key     = -1;

    public int id() {
        return this.id;
    }

    public int button() {
        return this.key;
    }

    public int key() {
        return this.key;
    }

    public int getchar() {
        return this.key;
    }

    public void setPress(int i) {
        this.id = 0;
        this.key = i;
    }

    public void setRelease(int i) {
        this.id = 1;
        this.key = i;
    }

    public void setChar(int i) {
        this.id = 2;
        this.key = i;
    }

    public boolean invokeListener(Object o) {
        if (RTSConf.cur.console.getEnv().existAtom("DEBUG_CANOPY", true)) {
            System.out.println("### UP3.4 CockpitDoor DEBUG ### MsgKeyboard.invokeListener(" + o.getClass().getName() + ") id=" + this.id + ", key=" + this.key);
        }
        if ((o instanceof MsgKeyboardListener)) {
            switch (this.id) {
                case 0:
                case 1:
                    ((MsgKeyboardListener) o).msgKeyboardKey(this.key, this.id == 0);
                    return true;
                case 2:
                    ((MsgKeyboardListener) o).msgKeyboardChar((char) this.key);
                    return true;
            }
            return true;
        }

        return false;
    }
}
