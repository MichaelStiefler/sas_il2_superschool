package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB5N2_Gunner extends CockpitGunner {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitB5N2_Gunner.this.ac != null && CockpitB5N2_Gunner.this.ac.bChangedPit) {
                CockpitB5N2_Gunner.this.reflectPlaneToModel();
                CockpitB5N2_Gunner.this.ac.bChangedPit = false;
            }
            if (CockpitB5N2_Gunner.this.fm != null) {
                CockpitB5N2_Gunner.this.setTmp = CockpitB5N2_Gunner.this.setOld;
                CockpitB5N2_Gunner.this.setOld = CockpitB5N2_Gunner.this.setNew;
                CockpitB5N2_Gunner.this.setNew = CockpitB5N2_Gunner.this.setTmp;
                CockpitB5N2_Gunner.this.setNew.azimuth.setDeg(CockpitB5N2_Gunner.this.setOld.azimuth.getDeg(1.0F), CockpitB5N2_Gunner.this.fm.Or.azimut());
                CockpitB5N2_Gunner.this.setNew.altimeter = CockpitB5N2_Gunner.this.fm.getAltitude();
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        AnglesFork azimuth;
        float      altimeter;

        private Variables() {
            this.azimuth = new AnglesFork();
        }
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("Turret1A", 0.0F, -f, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
        if (f > 50F) f = 50F;
        else if (f < -50F) f = -50F;
        if (f1 < -12F) {
            float f2 = Math.abs(f1 + 12F);
            f1 = -12F;
            this.resetYPRmodifier();
            if (f < 0.0F) {
                Cockpit.xyz[1] = this.cvt(f2, 0.0F, 23F, 0.0F, 0.2F);
                Cockpit.xyz[2] = this.cvt(f2, 0.0F, 23F, 0.0F, -0.2F);
            } else {
                Cockpit.xyz[1] = this.cvt(f2, 0.0F, 23F, 0.0F, -0.2F);
                Cockpit.xyz[2] = this.cvt(f2, 0.0F, 23F, 0.0F, -0.2F);
            }
            this.mesh.chunkSetLocate("Z_CameraSideLean", Cockpit.xyz, Cockpit.ypr);
        } else {
            this.resetYPRmodifier();
            this.mesh.chunkSetLocate("Z_CameraSideLean", Cockpit.xyz, Cockpit.ypr);
        }
        this.mesh.chunkSetAngles("Z_CameraA", 0.0F, -f, 0.0F);
        this.mesh.chunkSetAngles("Z_CameraB", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        float f2 = -35F;
        if (f < 0.0F) {
            if (f < -20F) f2 = this.cvt(f, -41F, -20F, -35F, -15F);
            else f2 = this.cvt(f, -20F, -10F, -15F, -8F);
        } else if (f > 20F) f2 = this.cvt(f, 20F, 41F, -15F, -35F);
        else f2 = this.cvt(f, 10F, 20F, -8F, -15F);
        if (f < -54F) f = -54F;
        if (f > 54F) f = 54F;
        if (f1 > 55F) f1 = 55F;
        if (f1 < f2) f1 = f2;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitB5N2_Gunner() {
        super("3DO/Cockpit/B5N2-Gunner/hier.him", "he111_gunner");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.bHandleMoved = false;
        this.ac = null;
        this.hook1 = null;
        this.bNeedSetUp = true;
        this.cockpitNightMats = new String[] { "dbombergauges", "bombergauges", "dgauges1", "dgauges4", "gauges1", "gauges4", "turnbankneedles", "Rotatinginvertedcompass", "fixinvertedcompass" };
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.setNightMats(false);
        this.ac = (B5Nxyz) this.aircraft();
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.5F);
        this.mesh.chunkSetLocate("Z_Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Need_Navairspeed", 0.0F, this.floatindex(this.cvt(0.539957F * Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 30F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_CompassReflected", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_CompassInverted", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_NavclockH", 0.0F, 180F + this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_NavclockM", 0.0F, 180F + this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        float f1 = Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F;
        if (f1 > 0.0F) this.mesh.chunkSetAngles("Z_Need_FreeairTemp", 0.0F, this.cvt(f1, 0.0F, 40F, 0.0F, 32F), 0.0F);
        else this.mesh.chunkSetAngles("Z_Need_FreeairTemp", 0.0F, this.cvt(f1, -60F, 0.0F, -48F, 0.0F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_NavAltim", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 21600F), 0.0F);
        this.mesh.chunkSetAngles("Torpedosight", ((B5N2) this.aircraft()).fSightCurSideslip, 0.0F, 0.0F);
        if (this.aircraft().FM.CT.Weapons[10] != null) {
            int i = this.aircraft().FM.CT.Weapons[10][0].countBullets() / 97 + 1;
            for (int k = 1; k < 6; k++)
                if (k < i) this.mesh.chunkVisible("T92Spare" + k, true);
                else this.mesh.chunkVisible("T92Spare" + k, false);

        } else for (int j = 1; j < 6; j++)
            this.mesh.chunkVisible("T92Spare" + j, false);
        this.resetYPRmodifier();
        if (this.fm.CT.WeaponControl[10]) {
            if (this.bHandleMoved) {
                Cockpit.xyz[1] = 0.0F;
                this.bHandleMoved = false;
            } else {
                Cockpit.xyz[1] = 0.01F;
                this.bHandleMoved = true;
            }
        } else {
            Cockpit.xyz[1] = 0.0F;
            this.bHandleMoved = false;
        }
        this.mesh.chunkSetLocate("Z_CockingHdl", Cockpit.xyz, Cockpit.ypr);
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

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("BlisterInterior1_D0", false);
            this.aircraft().hierMesh().chunkVisible("BlisterInterior2_D0", false);
            this.aircraft().hierMesh().chunkVisible("BlisterInterior3_D0", false);
            this.aircraft().hierMesh().chunkVisible("Interior_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
            this.aircraft().hierMesh().chunkVisible("HMask2_D0", false);
            this.aircraft().hierMesh().chunkVisible("HMask1_D0", false);
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
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", this.aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot1_D1", !this.aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", this.aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot2_D1", !this.aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
            this.aircraft().hierMesh().chunkVisible("Head1_D0", this.aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
            super.doFocusLeave();
            return;
        }
    }

    protected void reflectPlaneToModel() {
        this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
        this.aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
        this.aircraft().hierMesh().chunkVisible("HMask2_D0", false);
        this.aircraft().hierMesh().chunkVisible("HMask1_D0", false);
        this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
        this.mesh.chunkVisible("pilotNavigator_d0", this.aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
        this.mesh.chunkVisible("pilotNavigator_d1", !this.aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
        this.mesh.chunkVisible("pilot1_d0", this.aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
        this.mesh.chunkVisible("pilot1_d1", !this.aircraft().hierMesh().isChunkVisible("Pilot1Col_D0"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        com.maddox.il2.engine.Mat mat1 = hiermesh.material(hiermesh.materialFind("Pilot2"));
        this.mesh.materialReplace("Pilot2", mat1);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    private static final float speedometerScale[] = { 0.0F, 7F, 14F, 21F, 28F, 43.5F, 62F, 81F, 104.5F, 130F, 157F, 184.5F, 214F, 244.5F, 275.5F, 305F, 333F, 363F, 388F, 420F, 445F, 472F, 497F, 522F, 549F, 573F, 595F, 616F, 635F, 656F, 675F };
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private boolean            bHandleMoved;
    private B5Nxyz             ac;
    private Hook               hook1;
    private boolean            bNeedSetUp;

    static {
        Property.set(CockpitB5N2_Gunner.class, "aiTuretNum", 0);
        Property.set(CockpitB5N2_Gunner.class, "weaponControlNum", 10);
        Property.set(CockpitB5N2_Gunner.class, "astatePilotIndx", 1);
        Property.set(CockpitB5N2_Gunner.class, "normZN", 0.8F);
    }
}
