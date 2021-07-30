/*
 * 4.13.1 Class, backported to UP3 by SAS~Storebror
 * 4.11+ TrackIR implementation by SAS~Storebror
 */

package com.maddox.rts;

public class TrackIR implements MsgAddListenerListener, MsgRemoveListenerListener {

    public static TrackIR adapter() {
        return RTSConf.cur.trackIR;
    }

    public boolean isExist() {
        return this.bExist;
    }

    public void getAngles(float angles[]) {
        angles[0] = this.yaw;
        angles[1] = this.pitch;
        angles[2] = this.roll;
        // for 4.10 backward compatibility
        if (angles.length < 4) return;
        angles[3] = this.headX;
        angles[4] = this.headY;
        angles[5] = this.headZ;
    }

    public Object[] getListeners() {
        return this.listeners.get();
    }

    public Object[] getRealListeners() {
        return this.realListeners.get();
    }

    public void msgAddListener(Object listener, Object dummy) {
        if (Message.current().isRealTime()) this.realListeners.insListener(listener);
        else this.listeners.insListener(listener);
    }

    public void msgRemoveListener(Object listener, Object dummy) {
        if (Message.current().isRealTime()) this.realListeners.removeListener(listener);
        else this.listeners.removeListener(listener);
    }

    protected void setExist(boolean newExist) {
        this.bExist = newExist;
    }

    protected void clear() {
        this.yaw = this.pitch = this.roll = 0.0F;
        this.headX = this.headY = this.headZ = 0.0F;
    }

    // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
    protected void setAngles(float yaw, float pitch, float roll, float headX, float headY, float headZ) {
        if (this.yaw == yaw && this.pitch == pitch && this.roll == roll && this.headX == headX && this.headY == headY && this.headZ == headZ) return;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.headX = headX;
        this.headY = headY;
        this.headZ = headZ;
        Object listeners[] = this.realListeners.get();
        if (listeners != null) {
            MsgTrackIR msgtrackir = (MsgTrackIR) this.cache.get();
            msgtrackir.id = 0;
            msgtrackir.yaw = this.yaw;
            msgtrackir.pitch = this.pitch;
            msgtrackir.roll = this.roll;
            msgtrackir.headX = this.headX;
            msgtrackir.headY = this.headY;
            msgtrackir.headZ = this.headZ;
            msgtrackir.post(64, listeners, Time.currentReal(), this);
        }
        if (!Time.isPaused()) {
            Object timeNotPausedListeners[] = this.listeners.get();
            if (timeNotPausedListeners != null) {
                MsgTrackIR timeNotPausedMsgTrackIR = (MsgTrackIR) this.cache.get();
                timeNotPausedMsgTrackIR.id = 0;
                timeNotPausedMsgTrackIR.yaw = this.yaw;
                timeNotPausedMsgTrackIR.pitch = this.pitch;
                timeNotPausedMsgTrackIR.roll = this.roll;
                timeNotPausedMsgTrackIR.headX = this.headX;
                timeNotPausedMsgTrackIR.headY = this.headY;
                timeNotPausedMsgTrackIR.headZ = this.headZ;
                timeNotPausedMsgTrackIR.post(ANGLES, timeNotPausedListeners, Time.current(), this);
            }
        }
    }
    // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---

    // Original 4.10/UP3 method for backward compatibility
    protected void setAngles(float yaw, float pitch, float roll) {
        this.setAngles(yaw, pitch, roll, 0F, 0F, 0F);
    }

    protected TrackIR(IniFile inifile, String dummy) {
        this.bExist = false;
        this.listeners = new Listeners();
        this.realListeners = new Listeners();
        this.cache = new MessageCache(MsgTrackIR.class);
        this.clear();
    }

    public static final int ANGLES  = 0;
    public static final int UNKNOWN = -1;
    private boolean         bExist;
    private Listeners       listeners;
    private Listeners       realListeners;
    private MessageCache    cache;
    private float           yaw;
    private float           pitch;
    private float           roll;
    private float           headX;
    private float           headY;
    private float           headZ;
}
