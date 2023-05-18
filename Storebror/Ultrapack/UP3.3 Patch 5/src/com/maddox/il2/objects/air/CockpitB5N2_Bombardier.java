package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitB5N2_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitB5N2_Bombardier.this.ac != null && CockpitB5N2_Bombardier.this.ac.bChangedPit) {
                CockpitB5N2_Bombardier.this.reflectPlaneToModel();
                CockpitB5N2_Bombardier.this.ac.bChangedPit = false;
            }
            if (CockpitB5N2_Bombardier.this.fm != null) {
                CockpitB5N2_Bombardier.this.setTmp = CockpitB5N2_Bombardier.this.setOld;
                CockpitB5N2_Bombardier.this.setOld = CockpitB5N2_Bombardier.this.setNew;
                CockpitB5N2_Bombardier.this.setNew = CockpitB5N2_Bombardier.this.setTmp;
                if (CockpitB5N2_Bombardier.this.bEntered) {
                    float f = ((B5N2) CockpitB5N2_Bombardier.this.aircraft()).fSightCurForwardAngle + 3.2F;
                    float f1 = -((B5N2) CockpitB5N2_Bombardier.this.aircraft()).fSightCurSideslip;
                    CockpitB5N2_Bombardier.this.mesh.chunkSetAngles("BlackBox", f1, 0.0F, -f);
                    HookPilot hookpilot = HookPilot.current;
                    hookpilot.setSimpleAimOrient(-f1, CockpitB5N2_Bombardier.this.tAim + f, 0.0F);
                }
                CockpitB5N2_Bombardier.this.setNew.azimuth.setDeg(CockpitB5N2_Bombardier.this.setOld.azimuth.getDeg(1.0F), CockpitB5N2_Bombardier.this.fm.Or.azimut());
                CockpitB5N2_Bombardier.this.setNew.altimeter = CockpitB5N2_Bombardier.this.fm.getAltitude();
                CockpitB5N2_Bombardier.this.setNew.flaps = 0.9F * CockpitB5N2_Bombardier.this.setOld.flaps + 0.1F * CockpitB5N2_Bombardier.this.fm.CT.FlapsControl;
                CockpitB5N2_Bombardier.this.setNew.gear = 0.7F * CockpitB5N2_Bombardier.this.setOld.gear + 0.3F * CockpitB5N2_Bombardier.this.fm.CT.GearControl;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        AnglesFork azimuth;
        float      gear;
        float      flaps;
        float      altimeter;

        private Variables() {
            this.azimuth = new AnglesFork();
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            this.aircraft().hierMesh().chunkVisible("BlisterInterior1_D0", false);
            this.aircraft().hierMesh().chunkVisible("BlisterInterior2_D0", false);
            this.aircraft().hierMesh().chunkVisible("BlisterInterior3_D0", false);
            this.aircraft().hierMesh().chunkVisible("Interior_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1A_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
            this.aircraft().hierMesh().chunkVisible("HMask1_D0", false);
            this.aircraft().hierMesh().chunkVisible("HMask3_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
            this.aircraft().hierMesh().chunkVisible("BlisterInterior1_D0", true);
            this.aircraft().hierMesh().chunkVisible("BlisterInterior2_D0", true);
            this.aircraft().hierMesh().chunkVisible("BlisterInterior3_D0", true);
            this.aircraft().hierMesh().chunkVisible("Interior_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret1A_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", this.aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot1_D1", !this.aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", this.aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot2_D1", !this.aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot3_D0", this.aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot3_D1", !this.aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
            this.aircraft().hierMesh().chunkVisible("Head1_D0", this.aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            this.leave();
            super.doFocusLeave();
            return;
        }
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 24.0");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(this.aAim, this.tAim, 0.0F);
        hookpilot.doAim(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setSimpleAimOrient(-27F, -50F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if (!this.bEntered) return;
        else {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.setSimpleAimOrient(-27F, -50F, 0.0F);
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            hookpilot1.doAim(false);
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
        if (flag) this.enter();
        else this.leave();
    }

    public CockpitB5N2_Bombardier() {
        super("3DO/Cockpit/B5N2-Bombardier/hier.him", "he111");
        this.bNeedSetUp = true;
        this.ac = null;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
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
        this.cockpitNightMats = new String[] { "dbombergauges", "bombergauges", "dgauges1", "dgauges4", "gauges1", "gauges4", "turnbankneedles", "Rotatinginvertedcompass", "fixinvertedcompass" };
        this.ac = (B5Nxyz) this.aircraft();
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkVisible("BlackBox", this.bEntered);
        this.mesh.chunkVisible("zReticle", this.bEntered);
        this.mesh.chunkVisible("zMark1", this.bEntered);
        this.mesh.chunkVisible("zMark2", this.bEntered);
        this.mesh.chunkVisible("zBulb", this.bEntered);
        this.mesh.chunkVisible("zRefraction", this.bEntered);
        if (this.bEntered) {
            this.mesh.chunkSetAngles("zMark1", ((B5N2) this.aircraft()).fSightCurForwardAngle * 3.675F, 0.0F, 0.0F);
            float f1 = this.cvt(((B5N2) this.aircraft()).fSightSetForwardAngle, -15F, 75F, -15F, 75F);
            this.mesh.chunkSetAngles("zMark2", f1 * 3.675F, 0.0F, 0.0F);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = this.cvt(this.fm.Or.getKren() * Math.abs(this.fm.Or.getKren()), -1225F, 1225F, -0.23F, 0.23F);
            Cockpit.xyz[1] = this.cvt((this.fm.Or.getTangage() - 1.0F) * Math.abs(this.fm.Or.getTangage() - 1.0F), -1225F, 1225F, 0.23F, -0.23F);
            Cockpit.ypr[0] = this.cvt(this.fm.Or.getKren(), -45F, 45F, -180F, 180F);
            this.mesh.chunkSetLocate("zBulb", Cockpit.xyz, Cockpit.ypr);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = this.cvt(Cockpit.xyz[0], -0.23F, 0.23F, 0.0095F, -0.0095F);
            Cockpit.xyz[1] = this.cvt(Cockpit.xyz[1], -0.23F, 0.23F, 0.0095F, -0.0095F);
            this.mesh.chunkSetLocate("zRefraction", Cockpit.xyz, Cockpit.ypr);
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.655F);
        this.mesh.chunkSetLocate("Z_Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Need_Navairspeed", 0.0F, this.floatindex(this.cvt(0.539957F * Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 30F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_CompassReflected", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_CompassInverted", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_NavclockH", 0.0F, 180F + this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_NavclockM", 0.0F, 180F + this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        float f2 = Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F;
        if (f2 > 0.0F) this.mesh.chunkSetAngles("Z_Need_FreeairTemp", 0.0F, this.cvt(f2, 0.0F, 40F, 0.0F, 32F), 0.0F);
        else this.mesh.chunkSetAngles("Z_Need_FreeairTemp", 0.0F, this.cvt(f2, -60F, 0.0F, -48F, 0.0F), 0.0F);
        float f3 = this.fm.turret[0].tu[0];
        float f4 = this.fm.turret[0].tu[1];
        this.mesh.chunkSetAngles("Z_Gun1", 0.0F, f3, 0.0F);
        this.mesh.chunkSetAngles("Z_T92Mg", 0.0F, f4, 0.0F);
        this.mesh.chunkSetAngles("Z_LandingGear", 0.0F, 70F * this.setNew.gear, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps", 0.0F, 70F * this.setNew.flaps, 0.0F);
        this.mesh.chunkSetAngles("Z_CowlFlaps", 0.0F, -75F + this.fm.EI.engines[0].getControlRadiator() * 75F, 0.0F);
        this.mesh.chunkSetAngles("Z_RudderTrim", 0.0F, 40F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_AileronTrim", 0.0F, 40F * this.fm.CT.getTrimAileronControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Alt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 21600F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Alt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 2160F), 0.0F);
        this.mesh.chunkSetAngles("Z_TorpedoSight1", 0.0F, ((B5N2) this.aircraft()).fSightCurSideslip, 0.0F);
        this.mesh.chunkSetAngles("Z_TorpedoSight2", 0.0F, ((B5N2) this.aircraft()).fSightCurSideslip, 0.0F);
        this.mesh.chunkSetAngles("Z_TorpedoSight3", 0.0F, -this.cvt(((B5N2) this.aircraft()).fSightCurSideslip, -40F, 40F, -48F, 48F), 0.0F);
        this.mesh.chunkSetAngles("Torpedosight", this.cvt(((B5N2) this.aircraft()).fSightCurSideslip, -40F, 40F, -48F, 48F), 0.0F, 0.0F);
        if (this.aircraft().FM.CT.Weapons[10] != null && this.aircraft().FM.CT.Weapons[10][0] != null) {
            int i = this.aircraft().FM.CT.Weapons[10][0].countBullets() / 97 + 1;
            for (int k = 1; k < 6; k++)
                if (k < i) this.mesh.chunkVisible("T92Spare" + k, true);
                else this.mesh.chunkVisible("T92Spare" + k, false);

        } else for (int j = 1; j < 6; j++)
            this.mesh.chunkVisible("T92Spare" + j, false);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("holesMiddle", true);
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("holesGunner", true);
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("holesCanopyNav", true);
            this.mesh.chunkVisible("DNavGauge2", true);
            this.mesh.chunkVisible("NavGauge2", false);
            this.mesh.chunkVisible("Z_Need_FreeairTemp", false);
            this.mesh.chunkVisible("Z_Need_NavclockH", false);
            this.mesh.chunkVisible("Z_Need_NavclockM", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("ZOil", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("holesCanopyNav", true);
            this.mesh.chunkVisible("DNavGauge1", true);
            this.mesh.chunkVisible("NavGauge1", false);
            this.mesh.chunkVisible("Z_Need_Navairspeed", false);
            this.mesh.chunkVisible("Z_Need_Alt1a", false);
            this.mesh.chunkVisible("Z_Need_Alt1b", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("holesCanopy", true);
        if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("holesFront", true);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    protected void reflectPlaneToModel() {
        this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
        this.aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
        this.aircraft().hierMesh().chunkVisible("HMask1_D0", false);
        this.aircraft().hierMesh().chunkVisible("HMask3_D0", false);
        this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
        this.mesh.chunkVisible("pilot1_d0", this.aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
        this.mesh.chunkVisible("pilot1_d1", !this.aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
        this.mesh.chunkVisible("pilotgunner_d0", this.aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
        this.mesh.chunkVisible("pilotgunner_d1", !this.aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Pilot2"));
        this.mesh.materialReplace("Pilot2", mat);
    }

    private boolean            bNeedSetUp;
    private B5Nxyz             ac;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private boolean            enteringAim;
    private static final float speedometerScale[] = { 0.0F, 7F, 14F, 21F, 28F, 43.5F, 62F, 81F, 104.5F, 130F, 157F, 184.5F, 214F, 244.5F, 275.5F, 305F, 333F, 363F, 388F, 420F, 445F, 472F, 497F, 522F, 549F, 573F, 595F, 616F, 635F, 656F, 675F };
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;

    static {
        Property.set(CockpitB5N2_Bombardier.class, "astatePilotIndx", 0);
        Property.set(CockpitB5N2_Bombardier.class, "normZNs", new float[] { 0.7F, 0.45F, 0.7F, 0.45F });
        Property.set(CockpitB5N2_Bombardier.class, "gsZN", 0.5F);
    }
}
