/*
 * CockpitPilot - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitPilot extends Cockpit {
    private String[]  hotKeyEnvs   = { "pilot", "move" };
    protected float   stepAzimut   = 45.0F;
    protected float   stepTangage  = 30.0F;
    protected float   minMaxAzimut = 145.0F;
    protected float   maxTangage   = 90.0F;
    protected float   minTangage   = -60.0F;
    protected Point3d cameraCenter = new Point3d();
    private Point3d   cameraAim;
    private Point3d   cameraUp;
    private boolean   bBeaconKeysEnabled;
    private float     saveZN;
    protected float   normZN;
    protected float   gsZN;
    private double    pictBall     = 0.0;
    private long      oldBallTime  = 0L;
    // TODO: +++ 4.12.2 adaptations by SAS~Storebror
    public float[]    limits6DoF   = { 0.7F, 0.055F, -0.07F, 0.11F, 0.12F, -0.11F, 0.03F, -0.03F };
    private boolean   isSimpleUse  = false;
    // TODO: --- 4.12.2 adaptations by SAS~Storebror

    class Interpolater extends InterpolateRef {
        public boolean tick() {
            if (CockpitPilot.this.isPadlock()) HookPilot.current.checkPadlockState();
            return true;
        }
    }

    public boolean isEnableHotKeysOnOutsideView() {
        return true;
    }

    public String[] getHotKeyEnvs() {
        return this.hotKeyEnvs;
    }

    private float ZNear(float f) {
        if (f < 0.0F) return -1.0F;
        Camera3D camera3d = (Camera3D) Actor.getByName("camera");
        float f_0_ = camera3d.ZNear;
        camera3d.ZNear = f;
        return f_0_;
    }

    protected boolean doFocusEnter() {
        HookPilot hookpilot = HookPilot.current;
        Aircraft aircraft = this.aircraft();
        Main3D main3d = Main3D.cur3D();
        hookpilot.setCenter(this.cameraCenter);
        hookpilot.setAim(this.cameraAim);
        hookpilot.setUp(this.cameraUp);
        if (!NetMissionTrack.isPlaying() || NetMissionTrack.playingOriginalVersion() > 101) {
            hookpilot.setSteps(this.stepAzimut, this.stepTangage);
            hookpilot.setMinMax(this.minMaxAzimut, this.minTangage, this.maxTangage);
        } else {
            hookpilot.setSteps(45.0F, 30.0F);
            hookpilot.setMinMax(135.0F, -60.0F, 90.0F);
        }
        hookpilot.reset();
        hookpilot.use(true);
        aircraft.setAcoustics(this.acoustics);
        if (this.acoustics != null) {
            aircraft.enableDoorSnd(true);
            if (this.acoustics.getEnvNum() == 2) aircraft.setDoorSnd(1.0F);
        }
        main3d.camera3D.pos.setRel(new Point3d(), new Orient());
        main3d.camera3D.pos.setBase(aircraft, hookpilot, false);
        main3d.camera3D.pos.resetAsBase();
        this.pos.resetAsBase();
        this.aircraft().setMotorPos(main3d.camera3D.pos.getAbsPoint());
        main3d.cameraCockpit.pos.setRel(new Point3d(), new Orient());
        main3d.cameraCockpit.pos.setBase(this, hookpilot, false);
        main3d.cameraCockpit.pos.resetAsBase();
        main3d.overLoad.setShow(true);
        main3d.renderCockpit.setShow(true);
        aircraft.drawing(!this.isNullShow());
        this.saveZN = this.ZNear(HookPilot.current.isAim() ? this.gsZN : this.normZN);
        this.bBeaconKeysEnabled = ((AircraftLH) this.aircraft()).bWantBeaconKeys;
        ((AircraftLH) this.aircraft()).bWantBeaconKeys = true;
        return true;
    }

    protected void doFocusLeave() {
        this.saveZN = this.ZNear(this.saveZN);
        ((AircraftLH) this.aircraft()).bWantBeaconKeys = this.bBeaconKeysEnabled;
        HookPilot hookpilot = HookPilot.current;
        Aircraft aircraft = this.aircraft();
        Main3D main3d = Main3D.cur3D();
        hookpilot.use(false);
        main3d.camera3D.pos.setRel(new Point3d(), new Orient());
        main3d.camera3D.pos.setBase(null, null, false);
        main3d.cameraCockpit.pos.setRel(new Point3d(), new Orient());
        main3d.cameraCockpit.pos.setBase(null, null, false);
        main3d.overLoad.setShow(false);
        main3d.renderCockpit.setShow(false);
        if (Actor.isValid(aircraft)) aircraft.drawing(true);
        // TODO: Fixed by SAS~Storebror to avoid null dereference
//        if (aircraft != null) aircraft.setAcoustics(null);
        if (aircraft == null) return;
        aircraft.setAcoustics(null);
        aircraft.enableDoorSnd(false);
        aircraft.setMotorPos(null);
    }

    public boolean existPadlock() {
        return true;
    }

    public boolean isPadlock() {
        if (!this.isFocused()) return false;
        HookPilot hookpilot = HookPilot.current;
        return hookpilot.isPadlock();
    }

    public Actor getPadlockEnemy() {
        if (!this.isFocused()) return null;
        HookPilot hookpilot = HookPilot.current;
        return hookpilot.isPadlock() ? hookpilot.getEnemy() : null;
    }

    public boolean startPadlock(Actor actor) {
        if (!this.isFocused()) return false;
        HookPilot hookpilot = HookPilot.current;
        return hookpilot.startPadlock(actor);
    }

    public void stopPadlock() {
        if (this.isFocused()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.stopPadlock();
        }
    }

    public void endPadlock() {
        if (this.isFocused()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.endPadlock();
        }
    }

    public void setPadlockForward(boolean bool) {
        if (this.isFocused()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setForward(bool);
        }
    }

    public boolean isToggleAim() {
        if (!this.isFocused()) return false;
        HookPilot hookpilot = HookPilot.current;
        return hookpilot.isAim();
    }

    public void doToggleAim(boolean bool) {
        if (this.isFocused()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(bool);
            if (bool) this.ZNear(this.gsZN);
            else this.ZNear(this.normZN);
        }
    }

    public boolean isToggleUp() {
        if (!this.isFocused()) return false;
        HookPilot hookpilot = HookPilot.current;
        return hookpilot.isUp();
    }

    public void doToggleUp(boolean bool) {
        if (this.isFocused() && (!bool || this.cameraUp != null)) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doUp(bool);
        }
    }

    public CockpitPilot(String string, String string_1_) {
        super(string, string_1_);
        HookNamed hooknamed = new HookNamed(this.mesh, "CAMERA");
        Loc loc = new Loc();
        hooknamed.computePos(this, this.pos.getAbs(), loc);
        loc.get(this.cameraCenter);
        try {
            hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
            loc.set(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            this.cameraAim = new Point3d();
            loc.get(this.cameraAim);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        try {
            hooknamed = new HookNamed(this.mesh, "CAMERAUP");
            loc.set(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            this.cameraUp = new Point3d();
            loc.get(this.cameraUp);
        } catch (Exception exception) {
            /* empty */
        }
        this.pos.setBase(this.aircraft(), new Cockpit.HookOnlyOrient(), false);
        this.interpPut(new Interpolater(), "CockpitPilot", Time.current(), null);
        if (HookPilot.current != null) HookPilot.current.doUp(false);
        this.normZN = Property.floatValue(this.getClass(), "normZN", -1.0F);
        this.gsZN = Property.floatValue(this.getClass(), "gsZN", -1.0F);

        // TODO: Added by |ZUTI|: reset multi crew bomber position
        // -----------------------------------------------------------
        if (string.indexOf("Bombardier") > -1) this.pos.setAbs(new Point3d(0, 0, 0), new Orient(0, 0, 0));
        // System.out.println("BOMBARDIER COCKPIT POSITION RESET!");
    }

    protected float getBall(double d) {
        double d_2_ = 0.0;
        long l = Time.current();
        long l_3_ = l - this.oldBallTime;
        this.oldBallTime = l;
        if (l_3_ > 200L) l_3_ = 200L;
        double d_4_ = 3.8E-4 * l_3_;
        if (-this.fm.getBallAccel().z > 0.0010) {
            d_2_ = Math.toDegrees(Math.atan2(this.fm.getBallAccel().y, -this.fm.getBallAccel().z));
            if (d_2_ > 20.0) d_2_ = 20.0;
            else if (d_2_ < -20.0) d_2_ = -20.0;
            this.pictBall = (1.0 - d_4_) * this.pictBall + d_4_ * d_2_;
        } else {
            if (this.pictBall > 0.0) d_2_ = 20.0;
            else d_2_ = -20.0;
            this.pictBall = (1.0 - d_4_) * this.pictBall + d_4_ * d_2_;
        }
        if (this.pictBall > d) this.pictBall = d;
        else if (this.pictBall < -d) this.pictBall = -d;
        return (float) this.pictBall;
    }

    // TODO: +++ 4.12.2 adaptations by SAS~Storebror
    public float[] get6DoFLimits() {
        return this.limits6DoF;
    }

    public void doSetSimpleUse(boolean paramBoolean) {
        this.isSimpleUse = paramBoolean;
        if (this.gsZN != -1.0F && this.isSimpleUse) this.ZNear(this.gsZN);
        else if (this.normZN != -1.0F && !this.isSimpleUse) this.ZNear(this.normZN);
    }
    // TODO: --- 4.12.2 adaptations by SAS~Storebror

    static {
        Property.set(CockpitPilot.class, "astatePilotIndx", 0);
    }
}
