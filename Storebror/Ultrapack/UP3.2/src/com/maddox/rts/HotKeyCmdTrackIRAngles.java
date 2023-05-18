/*
 * 4.13.1 Class, backported to UP3 by SAS~Storebror
 * 4.11+ TrackIR implementation by SAS~Storebror
 */

package com.maddox.rts;

public abstract class HotKeyCmdTrackIRAngles extends HotKeyCmd {

    public void angles(float yaw, float pitch, float roll, float headX, float headY, float headZ) {}

    public void angles(float yaw, float pitch, float roll) {}

    public final void setAngles(float yaw, float pitch, float roll, float headX, float headY, float headZ) {
        this._yaw = yaw;
        this._pitch = pitch;
        this._roll = roll;
        this._headX = headX;
        this._headY = headY;
        this._headZ = headZ;
    }

    // Original 4.10/UP3 method for backward compatibility 
    public final void setAngles(float yaw, float pitch, float roll) {
        this.setAngles(yaw, pitch, roll, 0F, 0F, 0F);
    }

    public final void doAngles() {
        this.bActive = true;
        if (TrackIRWin.isUseNewTrackIR())
            this.angles(this._yaw, this._pitch, this._roll, this._headX, this._headY, this._headZ);
        else
            this.angles(this._yaw, this._pitch, this._roll);
//        DecimalFormat twoDigits = new DecimalFormat("#.##");
//        HUD.training("" + twoDigits.format(this._yaw)
//                  + "-" + twoDigits.format(this._pitch)
//                  + "-" + twoDigits.format(this._roll)
//                  + "-" + twoDigits.format(this._headX)
//                  + "-" + twoDigits.format(this._headY)
//                  + "-" + twoDigits.format(this._headZ)
//                          );
        this.bActive = false;
    }

    public HotKeyCmdTrackIRAngles(boolean isRealTime, String name) {
        super(isRealTime, name);
    }

    // Original 4.10/UP3 method for backward compatibility 
    public void _exec(float yaw, float pitch, float roll) {
        this._exec(yaw, pitch, roll, 0F, 0F, 0F);
    }
    
    public void _exec(float yaw, float pitch, float roll, float headX, float headY, float headZ) {
        if (Time.isPaused() && !this.isRealTime())
            return;
        if (!this.hotKeyCmdEnv.isEnabled()) {
            return;
        } else {
            this.setAngles(yaw, pitch, roll, headX, headY, headZ);
            RTSConf.cur.hotKeyCmdEnvs.post(this, true, true);
            this.doAngles();
            RTSConf.cur.hotKeyCmdEnvs.post(this, true, false);
            return;
        }
    }

    protected float _yaw;
    protected float _pitch;
    protected float _roll;
    protected float _headX;
    protected float _headY;
    protected float _headZ;
}
