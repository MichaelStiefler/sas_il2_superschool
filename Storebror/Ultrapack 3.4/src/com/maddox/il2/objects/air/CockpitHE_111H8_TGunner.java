package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111H8_TGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Kolpak_D0", false);
            this.aircraft().hierMesh().chunkVisible("Korzina_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret3C_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            ((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = true;
            this.aircraft().hierMesh().chunkVisible("Kolpak_D0", true);
            this.aircraft().hierMesh().chunkVisible("Korzina_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret3C_D0", true);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot4_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot4_D1"));
            super.doFocusLeave();
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("TurretFA", 0.0F, this.fm.turret[2].tu[0], 0.0F);
        this.mesh.chunkSetAngles("TurretFB", 0.0F, this.fm.turret[2].tu[1], 0.0F);
        float f1 = this.fm.CT.getCockpitDoor();
        this.resetYPRmodifier();
        if (f1 > 0.2F) Cockpit.xyz[1] = (f1 - 0.2F) * -0.8125F;
        this.mesh.chunkSetLocate("Hood", Cockpit.xyz, Cockpit.ypr);
        float f2 = f1 * 275F;
        if (f2 < 10F) this.mesh.chunkSetAngles("Hood_S1", 0.0F, 0.0F, -f2);
        else this.mesh.chunkSetAngles("Hood_S1", 0.0F, 0.0F, -10F);
        if (f2 < 31.6F) this.mesh.chunkSetAngles("Hood_S2", 0.0F, 0.0F, -f2);
        else this.mesh.chunkSetAngles("Hood_S2", 0.0F, 0.0F, -31.6F);
        if (f2 < 55F) this.mesh.chunkSetAngles("Hood_S3", 0.0F, 0.0F, -f2);
        else this.mesh.chunkSetAngles("Hood_S3", 0.0F, 0.0F, -55F);
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, 0.0F, -orient.getTangage());
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        float f2 = this.fm.CT.getCockpitDoor();
        if (f2 < 0.99F) {
            f = 0.0F;
            f1 = 0.0F;
        } else {
            if (f < -75F) f = -75F;
            if (f > 75F) f = 75F;
            if (f1 > 63F) f1 = 63F;
            if (f1 < -3F) f1 = -3F;
        }
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
            if (this.iCocking > 0) this.iCocking = 0;
            else this.iCocking = 1;
            this.iNewVisDrums = (int) (this.emitter.countBullets() / 1.0F);
            if (this.iNewVisDrums < this.iOldVisDrums) {
                this.iOldVisDrums = this.iNewVisDrums;
                this.mesh.chunkVisible("Drum1", this.iNewVisDrums > 700);
                this.mesh.chunkVisible("Drum2", this.iNewVisDrums > 450);
                this.mesh.chunkVisible("Drum3", this.iNewVisDrums > 150);
                this.sfxClick(13);
            }
        } else this.iCocking = 0;
        this.mesh.chunkSetAngles("CockingLever", -0.75F * this.iCocking, 0.0F, 0.0F);
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public void reflectCockpitState() {
        if (this.fm.AS.astateCockpitState != 0) this.mesh.chunkVisible("Holes_D1", true);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
    }

    public CockpitHE_111H8_TGunner() {
        super("3DO/Cockpit/He-111P-4-TGun/hier-H8.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.hook1 = null;
        this.iCocking = 0;
        this.iOldVisDrums = 1000;
        this.iNewVisDrums = 1000;
    }

    private boolean bNeedSetUp;
    private Hook    hook1;
    private int     iCocking;
    private int     iOldVisDrums;
    private int     iNewVisDrums;

    static {
        Property.set(CockpitHE_111H8_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitHE_111H8_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitHE_111H8_TGunner.class, "astatePilotIndx", 2);
    }
}
