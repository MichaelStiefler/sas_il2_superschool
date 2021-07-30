package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_111H16_TGunner extends CockpitGunner {
    private class Variables {

        float dimPosition;

        private Variables() {
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitHE_111H16_TGunner.this.setTmp = CockpitHE_111H16_TGunner.this.setOld;
            CockpitHE_111H16_TGunner.this.setOld = CockpitHE_111H16_TGunner.this.setNew;
            CockpitHE_111H16_TGunner.this.setNew = CockpitHE_111H16_TGunner.this.setTmp;
            if (CockpitHE_111H16_TGunner.this.cockpitDimControl) {
                if (CockpitHE_111H16_TGunner.this.setNew.dimPosition > 0.0F) CockpitHE_111H16_TGunner.this.setNew.dimPosition = CockpitHE_111H16_TGunner.this.setOld.dimPosition - 0.05F;
            } else if (CockpitHE_111H16_TGunner.this.setNew.dimPosition < 1.0F) CockpitHE_111H16_TGunner.this.setNew.dimPosition = CockpitHE_111H16_TGunner.this.setOld.dimPosition + 0.05F;
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            if (this.fm.actor instanceof HE_111) ((HE_111) this.fm.actor).bPitUnfocused = false;
            else if (this.fm.actor instanceof HE_111xyz) ((HE_111xyz) this.fm.actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Kolpak_D0", false);
            this.aircraft().hierMesh().chunkVisible("Korzina_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            if (this.fm.actor instanceof HE_111) ((HE_111) this.fm.actor).bPitUnfocused = true;
            else if (this.fm.actor instanceof HE_111xyz) ((HE_111xyz) this.fm.actor).bPitUnfocused = true;
            this.aircraft().hierMesh().chunkVisible("Kolpak_D0", true);
            this.aircraft().hierMesh().chunkVisible("Korzina_D0", true);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot4_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot4_D1"));
            super.doFocusLeave();
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("ReVi16ATint", this.cvt(this.setNew.dimPosition, 0.0F, 1.0F, 40F, 0.0F), 0.0F, 0.0F);
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("TurretA", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
        if (f > 36F) f = 36F;
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
        if (f < -40F) f = -40F;
        if (f > 40F) f = 40F;
        if (f1 > 60F) f1 = 60F;
        if (f1 < -5F) f1 = -5F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
        }
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

    public CockpitHE_111H16_TGunner() {
        super("3DO/Cockpit/He-111P-4-TGun/hier-H16.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.hook1 = null;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.setNew.dimPosition = this.setOld.dimPosition = 1.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    private boolean   bNeedSetUp;
    private Hook      hook1;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;

    static {
        Property.set(CockpitHE_111H16_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitHE_111H16_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitHE_111H16_TGunner.class, "astatePilotIndx", 2);
    }
}
