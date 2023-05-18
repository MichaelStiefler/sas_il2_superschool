package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111H20_NGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((HE_111xyz) ((Interpolate) this.fm).actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Head1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        ((HE_111xyz) ((Interpolate) this.fm).actor).bPitUnfocused = true;
        this.aircraft().hierMesh().chunkVisible("Cockpit_D0", this.aircraft().hierMesh().isChunkVisible("Nose_D0") || this.aircraft().hierMesh().isChunkVisible("Nose_D1") || this.aircraft().hierMesh().isChunkVisible("Nose_D2"));
        this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.aircraft().hierMesh().isChunkVisible("Turret1B_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        this.aircraft().hierMesh().chunkVisible("Head1_FAK", this.aircraft().hierMesh().isChunkVisible("Head1_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
        this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
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
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
        else {
            float f = orient.getYaw();
            float f1 = orient.getTangage();
            if (f < -25F) f = -25F;
            if (f > 15F) f = 15F;
            if (f1 > 0.0F) f1 = 0.0F;
            if (f1 < -40F) f1 = -40F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) this.mesh.chunkSetAngles("Butona", 0.15F, 0.0F, 0.0F);
            else this.mesh.chunkSetAngles("Butona", 0.0F, 0.0F, 0.0F);
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            else this.bGunFire = flag;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public CockpitHE_111H20_NGunner() {
        super("3DO/Cockpit/He-111H-20-NGun/hier_H20.him", "he111_gunner");
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("zColumn1", 0.0F, 0.0F, -10F * this.fm.CT.ElevatorControl);
        this.mesh.chunkSetAngles("zColumn2", 0.0F, 0.0F, -40F * this.fm.CT.AileronControl);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("ZHolesL_D1", true);
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("ZHolesL_D2", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("ZHolesR_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("ZHolesR_D2", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("ZHolesF_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("zOil_D1", true);
    }

    static {
        Property.set(CockpitHE_111H20_NGunner.class, "aiTuretNum", 0);
        Property.set(CockpitHE_111H20_NGunner.class, "weaponControlNum", 10);
        Property.set(CockpitHE_111H20_NGunner.class, "astatePilotIndx", 1);
    }
}
