package com.maddox.il2.objects.air;

import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_111H11_NGunner extends CockpitGunner {
    private class Variables {

        float cons;
        float consL;
        float consR;

        private Variables() {
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitHE_111H11_NGunner.this.setTmp = CockpitHE_111H11_NGunner.this.setOld;
            CockpitHE_111H11_NGunner.this.setOld = CockpitHE_111H11_NGunner.this.setNew;
            CockpitHE_111H11_NGunner.this.setNew = CockpitHE_111H11_NGunner.this.setTmp;
            float f = CockpitHE_111H11_NGunner.this.prevFuel - CockpitHE_111H11_NGunner.this.fm.M.fuel;
            CockpitHE_111H11_NGunner.this.prevFuel = CockpitHE_111H11_NGunner.this.fm.M.fuel;
            f /= 0.72F;
            f /= Time.tickLenFs();
            f *= 3600F;
            CockpitHE_111H11_NGunner.this.setNew.cons = 0.9F * CockpitHE_111H11_NGunner.this.setOld.cons + 0.1F * f;
            float f1 = CockpitHE_111H11_NGunner.this.fm.EI.engines[0].getEngineForce().x;
            float f2 = CockpitHE_111H11_NGunner.this.fm.EI.engines[1].getEngineForce().x;
            if (f1 < 100F || CockpitHE_111H11_NGunner.this.fm.EI.engines[0].getRPM() < 600F) f1 = 1.0F;
            if (f2 < 100F || CockpitHE_111H11_NGunner.this.fm.EI.engines[1].getRPM() < 600F) f2 = 1.0F;
            CockpitHE_111H11_NGunner.this.setNew.consL = 0.9F * CockpitHE_111H11_NGunner.this.setOld.consL + 0.1F * (CockpitHE_111H11_NGunner.this.setNew.cons * f1) / (f1 + f2);
            CockpitHE_111H11_NGunner.this.setNew.consR = 0.9F * CockpitHE_111H11_NGunner.this.setOld.consR + 0.1F * (CockpitHE_111H11_NGunner.this.setNew.cons * f2) / (f1 + f2);
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            if (this.fm.actor instanceof HE_111) ((HE_111) this.fm.actor).bPitUnfocused = false;
            else if (this.fm.actor instanceof HE_111xyz) ((HE_111xyz) this.fm.actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Head1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Window_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (this.fm.actor instanceof HE_111) ((HE_111) this.fm.actor).bPitUnfocused = true;
        else if (this.fm.actor instanceof HE_111xyz) ((HE_111xyz) this.fm.actor).bPitUnfocused = true;
        this.aircraft().hierMesh().chunkVisible("Cockpit_D0", this.aircraft().hierMesh().isChunkVisible("Nose_D0") || this.aircraft().hierMesh().isChunkVisible("Nose_D1") || this.aircraft().hierMesh().isChunkVisible("Nose_D2"));
        this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.aircraft().hierMesh().isChunkVisible("Turret1B_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        this.aircraft().hierMesh().chunkVisible("Head1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
        this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
        this.aircraft().hierMesh().chunkVisible("Window_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("TurretA", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
        if (f > 15F) f = 15F;
        if (f1 < -21F) f1 = -21F;
        this.mesh.chunkSetAngles("CameraRodA", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("CameraRodB", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -25F) f = -25F;
        if (f > 15F) f = 15F;
        if (f1 > 0.0F) f1 = 0.0F;
        if (f1 < -40F) f1 = -40F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) this.mesh.chunkSetAngles("Butona", 0.15F, 0.0F, 0.0F);
        else this.mesh.chunkSetAngles("Butona", 0.0F, 0.0F, 0.0F);
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitHE_111H11_NGunner() {
        super("3DO/Cockpit/He-111P-4-NGun/hier-H11.him", "he111_gunner");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.cockpitNightMats = new String[] { "clocks2C", "clocks3B", "clocks4", "clocks5", "clocks6", "clocks8C" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.prevFuel = 0.0F;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("zColumn1", 0.0F, 0.0F, -10F * this.fm.CT.ElevatorControl);
        this.mesh.chunkSetAngles("zColumn2", 0.0F, 0.0F, -40F * this.fm.CT.AileronControl);
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

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              prevFuel;
    private static final float rpmScale[] = { 0.0F, 14.7F, 76.15F, 143.86F, 215.97F, 282.68F, 346.18F };

    static {
        Property.set(CockpitHE_111H11_NGunner.class, "aiTuretNum", 0);
        Property.set(CockpitHE_111H11_NGunner.class, "weaponControlNum", 10);
        Property.set(CockpitHE_111H11_NGunner.class, "astatePilotIndx", 1);
    }
}
