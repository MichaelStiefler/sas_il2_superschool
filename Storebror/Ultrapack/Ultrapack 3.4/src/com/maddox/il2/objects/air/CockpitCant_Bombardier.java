package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;

public class CockpitCant_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitCant_Bombardier.this.fm != null) {
                CockpitCant_Bombardier.this.setTmp = CockpitCant_Bombardier.this.setOld;
                CockpitCant_Bombardier.this.setOld = CockpitCant_Bombardier.this.setNew;
                CockpitCant_Bombardier.this.setNew = CockpitCant_Bombardier.this.setTmp;
                CockpitCant_Bombardier.this.setNew.altimeter = CockpitCant_Bombardier.this.fm.getAltitude();
                CockpitCant_Bombardier.this.setNew.azimuth.setDeg(CockpitCant_Bombardier.this.setOld.azimuth.getDeg(1.0F), 90F + CockpitCant_Bombardier.this.fm.Or.azimut());
            }
            float f = ((CantZ1007) CockpitCant_Bombardier.this.aircraft()).fSightCurForwardAngle;
            float f1 = 0.0F;
            if (CockpitCant_Bombardier.this.bEntered) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setSimpleAimOrient(CockpitCant_Bombardier.this.aAim + f1, CockpitCant_Bombardier.this.tAim + f, 0.0F);
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        AnglesFork azimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            ((CantZ1007) this.fm.actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Interior_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            ((CantZ1007) this.fm.actor).bPitUnfocused = true;
            this.aircraft().hierMesh().chunkVisible("Interior_D0", this.aircraft().hierMesh().isChunkVisible("CF_D0"));
            this.leave();
            super.doFocusLeave();
        }
    }

    private void enter() {
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(this.aAim, this.tAim, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        if (this.bEntered) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
        }
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
        if (this.isFocused() && (this.isToggleAim() != flag)) {
            if (flag) {
                this.enter();
            } else {
                this.leave();
            }
        }
    }

    public CockpitCant_Bombardier() {
        super("3DO/Cockpit/Cant-Bombardier/hier.him", "he111");
        this.bEntered = false;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        try {
            Loc loc = new Loc();
            HookNamed hooknamed1 = new HookNamed(this.mesh, "CAMERAAIM");
            hooknamed1.computePos(this, this.pos.getAbs(), loc);
            this.aAim = loc.getOrient().getAzimut();
            this.tAim = loc.getOrient().getTangage();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
        this.light1 = new LightPointActor(new LightPoint(), loc1.getPoint());
        this.light1.light.setColor(109F, 99F, 90F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.cockpitNightMats = (new String[] { "Panel", "Needles" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.0032F, 7.2F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.reflectPlaneToModel();
        this.resetYPRmodifier();
        float f1 = this.interp(this.setNew.altimeter, this.setOld.altimeter, f) * 0.072F;
        this.mesh.chunkSetAngles("Z_Altimeter", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 460F, 0.0F, 23F), CockpitCant_Bombardier.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("z_Hour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("z_Minute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("z_Second", 0.0F, this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, 90F - this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -28F, 28F), 0.0F);
        if (!((NetAircraft) (this.aircraft())).thisWeaponsName.startsWith("1x")) {
            this.resetYPRmodifier();
            float f2 = this.fm.Or.getPitch();
            if (f2 > 360F) {
                f2 -= 360F;
            }
            f2 *= 0.00872664F;
            float f3 = ((CantZ1007) this.aircraft()).fSightSetForwardAngle - (float) Math.toRadians(f2);
            float f4 = (float) (0.16915999352931976D * Math.tan(f3));
            if (f4 < 0.032F) {
                f4 = 0.032F;
            } else if (f4 > 0.21F) {
                f4 = 0.21F;
            }
            float f5 = f4 * 0.667F;
            Cockpit.xyz[0] = f4;
            this.mesh.chunkSetLocate("ZCursor1", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[0] = f5;
            this.mesh.chunkSetLocate("ZCursor2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetAngles("Cylinder", 0.0F, ((CantZ1007) this.aircraft()).fSightCurSideslip, 0.0F);
        }
    }

    protected void mydebugcockpit(String s1) {
    }

    public void reflectCockpitState() {
        if (this.fm.AS.astateCockpitState != 0) {
            if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
                this.mesh.chunkVisible("XGlassHoles3", true);
            }
            if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
                this.mesh.chunkVisible("XGlassHoles3", true);
            }
        }
    }

    protected void reflectPlaneMats() {
    }

    protected void reflectPlaneToModel() {
    }

    private static final float speedometerScale[] = { 0.0F, 10F, 20F, 30F, 50F, 68F, 88F, 109F, 126F, 142F, 159F, 176F, 190F, 206F, 220F, 238F, 253F, 270F, 285F, 300F, 312F, 325F, 337F, 350F, 360F };
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private LightPointActor    light1;
    private boolean            bNeedSetUp;

}
