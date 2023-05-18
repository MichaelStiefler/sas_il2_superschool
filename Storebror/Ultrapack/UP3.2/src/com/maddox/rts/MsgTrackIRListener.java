/*
 * 4.13.1 Class, backported to UP3 by SAS~Storebror
 * 4.11+ TrackIR implementation by SAS~Storebror
 */

package com.maddox.rts;

public interface MsgTrackIRListener {

    // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
    public abstract void msgTrackIRAngles(float yaw, float pitch, float roll, float headX, float headY, float headZ);
    // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---
    
    // Original 4.10 (UP3) method
    public abstract void msgTrackIRAngles(float yaw, float pitch, float roll);
}
