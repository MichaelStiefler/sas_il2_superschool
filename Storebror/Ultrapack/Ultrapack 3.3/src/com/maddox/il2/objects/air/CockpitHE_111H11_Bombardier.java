package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_111H11_Bombardier extends CockpitPilot {
    private class Variables {

        float      cons;
        float      consL;
        float      consR;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            float f = 0F, f1 = 0F;
            if (CockpitHE_111H11_Bombardier.this.aircraft() instanceof HE_111) {
                f = ((HE_111) CockpitHE_111H11_Bombardier.this.aircraft()).fSightCurForwardAngle;
                f1 = ((HE_111) CockpitHE_111H11_Bombardier.this.aircraft()).fSightCurSideslip;
            } else if (CockpitHE_111H11_Bombardier.this.aircraft() instanceof HE_111xyz) {
                f = ((HE_111xyz) CockpitHE_111H11_Bombardier.this.aircraft()).fSightCurForwardAngle;
                f1 = ((HE_111xyz) CockpitHE_111H11_Bombardier.this.aircraft()).fSightCurSideslip;
            }
            CockpitHE_111H11_Bombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            if (CockpitHE_111H11_Bombardier.this.bEntered) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setInstantOrient(CockpitHE_111H11_Bombardier.this.aAim + 10F * f1, CockpitHE_111H11_Bombardier.this.tAim + f, 0.0F);
            }
            CockpitHE_111H11_Bombardier.this.mesh.chunkSetAngles("TurretA", 0.0F, CockpitHE_111H11_Bombardier.this.aircraft().FM.turret[0].tu[0], 0.0F);
            CockpitHE_111H11_Bombardier.this.mesh.chunkSetAngles("TurretB", 0.0F, CockpitHE_111H11_Bombardier.this.aircraft().FM.turret[0].tu[1], 0.0F);
            CockpitHE_111H11_Bombardier.this.setTmp = CockpitHE_111H11_Bombardier.this.setOld;
            CockpitHE_111H11_Bombardier.this.setOld = CockpitHE_111H11_Bombardier.this.setNew;
            CockpitHE_111H11_Bombardier.this.setNew = CockpitHE_111H11_Bombardier.this.setTmp;
            float f2 = CockpitHE_111H11_Bombardier.this.waypointAzimuthInvertMinus(30F);
            if (CockpitHE_111H11_Bombardier.this.useRealisticNavigationInstruments()) {
                CockpitHE_111H11_Bombardier.this.setNew.waypointAzimuth.setDeg(f2 - 90F);
                CockpitHE_111H11_Bombardier.this.setOld.waypointAzimuth.setDeg(f2 - 90F);
                CockpitHE_111H11_Bombardier.this.setNew.radioCompassAzimuth.setDeg(CockpitHE_111H11_Bombardier.this.setOld.radioCompassAzimuth.getDeg(0.02F),
                        CockpitHE_111H11_Bombardier.this.radioCompassAzimuthInvertMinus() - CockpitHE_111H11_Bombardier.this.setOld.azimuth.getDeg(1.0F) - 90F);
            } else CockpitHE_111H11_Bombardier.this.setNew.waypointAzimuth.setDeg(CockpitHE_111H11_Bombardier.this.setOld.waypointAzimuth.getDeg(0.1F), f2 - CockpitHE_111H11_Bombardier.this.setOld.azimuth.getDeg(1.0F));
            CockpitHE_111H11_Bombardier.this.setNew.azimuth.setDeg(CockpitHE_111H11_Bombardier.this.setOld.azimuth.getDeg(1.0F), CockpitHE_111H11_Bombardier.this.fm.Or.azimut());
            float f3 = CockpitHE_111H11_Bombardier.this.prevFuel - CockpitHE_111H11_Bombardier.this.fm.M.fuel;
            CockpitHE_111H11_Bombardier.this.prevFuel = CockpitHE_111H11_Bombardier.this.fm.M.fuel;
            f3 /= 0.72F;
            f3 /= Time.tickLenFs();
            f3 *= 3600F;
            CockpitHE_111H11_Bombardier.this.setNew.cons = 0.9F * CockpitHE_111H11_Bombardier.this.setOld.cons + 0.1F * f3;
            float f4 = CockpitHE_111H11_Bombardier.this.fm.EI.engines[0].getEngineForce().x;
            float f5 = CockpitHE_111H11_Bombardier.this.fm.EI.engines[1].getEngineForce().x;
            if (f4 < 100F || CockpitHE_111H11_Bombardier.this.fm.EI.engines[0].getRPM() < 600F) f4 = 1.0F;
            if (f5 < 100F || CockpitHE_111H11_Bombardier.this.fm.EI.engines[1].getRPM() < 600F) f5 = 1.0F;
            CockpitHE_111H11_Bombardier.this.setNew.consL = 0.9F * CockpitHE_111H11_Bombardier.this.setOld.consL + 0.1F * (CockpitHE_111H11_Bombardier.this.setNew.cons * f4) / (f4 + f5);
            CockpitHE_111H11_Bombardier.this.setNew.consR = 0.9F * CockpitHE_111H11_Bombardier.this.setOld.consR + 0.1F * (CockpitHE_111H11_Bombardier.this.setNew.cons * f5) / (f4 + f5);
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            if (this.fm.actor instanceof HE_111) ((HE_111) this.fm.actor).bPitUnfocused = false;
            else if (this.fm.actor instanceof HE_111xyz) ((HE_111xyz) this.fm.actor).bPitUnfocused = false;
            this.bTurrVisible = this.aircraft().hierMesh().isChunkVisible("Turret1C_D0");
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Head1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            this.aircraft().hierMesh().chunkVisible("Window_D0", false);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.11D, 0.018D, -0.08D);
            hookpilot.setTubeSight(point3d);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
            if (this.fm.actor instanceof HE_111) ((HE_111) this.fm.actor).bPitUnfocused = false;
            else if (this.fm.actor instanceof HE_111xyz) ((HE_111xyz) this.fm.actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.bTurrVisible);
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", this.aircraft().hierMesh().isChunkVisible("Nose_D0") || this.aircraft().hierMesh().isChunkVisible("Nose_D1") || this.aircraft().hierMesh().isChunkVisible("Nose_D2"));
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.aircraft().hierMesh().isChunkVisible("Turret1B_D0"));
            this.aircraft().hierMesh().chunkVisible("Window_D0", true);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
            this.aircraft().hierMesh().chunkVisible("Head1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
            this.leave();
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter() {
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, -90F, 0.0F);
        this.enteringAim = true;
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setInstantOrient(this.aAim, this.tAim, 0.0F);
        hookpilot.setSimpleUse(true);
        this.doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(0.0F, -90F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if (!this.bEntered) return;
        else {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.setInstantOrient(0.0F, -90F, 0.0F);
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            this.doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            return;
        }
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused()) return;
        if (this.isToggleAim() == flag) return;
        if (flag) this.prepareToEnter();
        else this.leave();
    }

    public CockpitHE_111H11_Bombardier() {
        super("3DO/Cockpit/He-111P-4-Bombardier/hier-H11.him", "he111");
        this.enteringAim = false;
        this.bEntered = false;
        try {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            this.aAim = loc.getOrient().getAzimut();
            this.tAim = loc.getOrient().getTangage();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        this.cockpitNightMats = new String[] { "clocks1C", "clocks2C", "clocks3B", "clocks4", "clocks5", "clocks6", "clocks8C" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.setOld = new Variables();
        this.setNew = new Variables();
        AircraftLH.printCompassHeading = true;
        this.prevFuel = 0.0F;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isAimReached()) {
                this.enteringAim = false;
                this.enter();
            } else if (!hookpilot.isAim()) this.enteringAim = false;
        }
        if (this.bEntered) {
            boolean flag = false;
            if (this.aircraft() instanceof HE_111) {
                this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((HE_111) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
                flag = ((HE_111) this.aircraft()).fSightCurReadyness > 0.93F;
            } else if (this.aircraft() instanceof HE_111xyz) {
                this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((HE_111xyz) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
                flag = ((HE_111xyz) this.aircraft()).fSightCurReadyness > 0.93F;
            }
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", flag);
            this.mesh.chunkVisible("zAngleMark", flag);
        } else {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zAngleMark", false);
        }
        if (this.aircraft() instanceof HE_111) {
            this.mesh.chunkSetAngles("zAltWheel", 0.0F, this.cvt(((HE_111) this.aircraft()).fSightCurAltitude, 0.0F, 10000F, 0.0F, 375.8333F), 0.0F);
            this.mesh.chunkSetAngles("zAnglePointer", 0.0F, ((HE_111) this.aircraft()).fSightCurForwardAngle, 0.0F);
            this.mesh.chunkSetAngles("zAngleWheel", 0.0F, -10F * ((HE_111) this.aircraft()).fSightCurForwardAngle, 0.0F);
            this.mesh.chunkSetAngles("zSpeedPointer", 0.0F, this.cvt(((HE_111) this.aircraft()).fSightCurSpeed, 150F, 600F, 0.0F, 60F), 0.0F);
            this.mesh.chunkSetAngles("zSpeedWheel", 0.0F, 0.333F * ((HE_111) this.aircraft()).fSightCurSpeed, 0.0F);
        } else if (this.aircraft() instanceof HE_111xyz) {
            this.mesh.chunkSetAngles("zAltWheel", 0.0F, this.cvt(((HE_111xyz) this.aircraft()).fSightCurAltitude, 0.0F, 10000F, 0.0F, 375.8333F), 0.0F);
            this.mesh.chunkSetAngles("zAnglePointer", 0.0F, ((HE_111xyz) this.aircraft()).fSightCurForwardAngle, 0.0F);
            this.mesh.chunkSetAngles("zAngleWheel", 0.0F, -10F * ((HE_111xyz) this.aircraft()).fSightCurForwardAngle, 0.0F);
            this.mesh.chunkSetAngles("zSpeedPointer", 0.0F, this.cvt(((HE_111xyz) this.aircraft()).fSightCurSpeed, 150F, 600F, 0.0F, 60F), 0.0F);
            this.mesh.chunkSetAngles("zSpeedWheel", 0.0F, 0.333F * ((HE_111xyz) this.aircraft()).fSightCurSpeed, 0.0F);
        }
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 550F, 0.0F, 11F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt(this.fm.getAltitude(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", -this.cvt(this.fm.getAltitude(), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkVisible("zRed1", this.fm.CT.BayDoorControl > 0.66F);
        this.mesh.chunkVisible("zYellow1", this.fm.CT.BayDoorControl < 0.33F);
        this.mesh.chunkSetAngles("zRPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3600F, 0.0F, 6F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3600F, 0.0F, 6F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 328F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost2", this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 328F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp1", this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 120F, 0.0F, 70F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp2", this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 120F, 0.0F, 70F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 70F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 120F, 0.0F, 70F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, -135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-2", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, -135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-2", this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFC-1", this.cvt(this.setNew.consL, 0.0F, 500F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFC-2", this.cvt(this.setNew.consR, 0.0F, 500F, 0.0F, -270F), 0.0F, 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Z_Compass3", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass4", -this.setNew.waypointAzimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass5", this.setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Compass4", -this.setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("ZHolesL_D1", true);
            this.mesh.chunkVisible("PanelR_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("ZHolesL_D2", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("ZHolesR_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("ZHolesR_D2", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("ZHolesF_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("zOil_D1", true);
    }

    private boolean            enteringAim;
    private static final float angleScale[]       = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
    private static final float speedometerScale[] = { 0.0F, 6.01F, 18.5F, 43.65F, 75.81F, 113.39F, 152F, 192.26F, 232.24F, 270.67F, 308.39F, 343.38F };
    private static final float rpmScale[]         = { 0.0F, 14.7F, 76.15F, 143.86F, 215.97F, 282.68F, 346.18F };
    private boolean            bTurrVisible;
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              prevFuel;

    static {
        Property.set(CockpitHE_111H11_Bombardier.class, "astatePilotIndx", 1);
    }
}
