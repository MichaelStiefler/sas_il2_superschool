/*
 * 4.13.1 Class, backported to UP3 by SAS~Storebror
 * 4.11+ TrackIR implementation by SAS~Storebror
 */

package com.maddox.rts;

public class MsgTrackIR extends Message {

    public int id() {
        return this.id;
    }

    public float yaw() {
        return this.yaw;
    }

    public float pitch() {
        return this.pitch;
    }

    public float roll() {
        return this.roll;
    }

    public float headX() {
        return this.headX;
    }

    public float headY() {
        return this.headY;
    }

    public float headZ() {
        return this.headZ;
    }

    public MsgTrackIR() {
        this.id = UNKNOWN;
    }

    public boolean invokeListener(Object listener) {
        if (listener instanceof MsgMouseListener)
            switch (this.id) {
                case 0:
                    // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
                    ((MsgTrackIRListener) listener).msgTrackIRAngles(this.yaw, this.pitch, this.roll, this.headX, this.headY, this.headZ);
                    // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---
                    return true;
            }
        return false;
    }

    public static final int ANGLES  = 0;
    public static final int UNKNOWN = -1;
    protected int           id;
    protected float         yaw;
    protected float         pitch;
    protected float         roll;
    protected float         headX;
    protected float         headY;
    protected float         headZ;
}
