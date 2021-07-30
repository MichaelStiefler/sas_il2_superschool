package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

/*
 * Nose Gunner for Sunderland Mk.II by Barnesy/CWatson/Freemodding
 * Latest edit: 2016-03-08 by SAS~Storebror
 */
public class CockpitSunderlandNGunner extends CockpitGunner {

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            // When shooting, let gun recoil shake plane accordingly
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
        }
        // Check if fuselage damage state has changed
        int curDamageState = ((Sunderlandxyz) this.aircraft()).getCfDamageVisible();
        if (curDamageState != this.lastDamageState) {
            // If so, hide new damage state fuselage mesh (fuselage shall not be visible as long as turret is manned)
            // and keep track of damage state so it can be set visible again when player leaves turret
            this.aircraft().hierMesh().chunkVisible("CF_D" + this.lastDamageState, false);
            curDamageState = this.lastDamageState;
        }

    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();

        /*
         * limit turret movement to FN turret physical limits and avoid collision with fuselage
         */
        if (f < -60F) f = -60F;
        if (f > 55F) f = 55F;
        if (f1 < -30F) f1 = -30F;
        if (f1 > 45F) f1 = 45F;

        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            /*
             * When entering turret position, make sure to hide external 3D model of the turret, hide the gunner and hide the plane's fuselage, they shall not be seen from inside the turret!
             */
            this.aircraft().hierMesh().chunkVisible("Turret1A_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1A1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head3_D0", false);
            this.aircraft().hierMesh().chunkVisible("HMASK3_D0", false);

            // When entering turret, save current fuselage damage state for visibility toggle
            ((Sunderlandxyz) this.aircraft()).updateDamageState();
            this.lastDamageState = ((Sunderlandxyz) this.aircraft()).getCfDamageVisible();
            this.aircraft().hierMesh().chunkVisible("CF_D" + this.lastDamageState, false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
            /*
             * When leaving this turret, regain visibility of the turret's external 3D model, show gunner if alive and show the fuselage in it's appropriate damage state again!
             */
            this.aircraft().hierMesh().chunkVisible("Turret1A_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret1A1_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D0", !this.aircraft().hierMesh().isChunkVisible("Pilot3_D1"));
            this.aircraft().hierMesh().chunkVisible("Head3_D0", !this.aircraft().hierMesh().isChunkVisible("Pilot3_D1"));
            this.aircraft().hierMesh().chunkVisible("CF_D" + this.lastDamageState, true);
            super.doFocusLeave();
            return;
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.reflectPlaneToModel();
    }

    protected void reflectPlaneMats() {
        // Apply plane skin to turret frame parts
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    protected void reflectPlaneToModel() {
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
    }

    public CockpitSunderlandNGunner() {
        super("3DO/Cockpit/SunderlandNGun/hier.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.l = new Loc();
        this.hook1 = null;
    }

    private boolean bNeedSetUp;
    Loc             l;
    private Hook    hook1;
    private int     lastDamageState;

    static {
        Property.set(CockpitSunderlandNGunner.class, "aiTuretNum", 0);
        Property.set(CockpitSunderlandNGunner.class, "weaponControlNum", 10);
        Property.set(CockpitSunderlandNGunner.class, "astatePilotIndx", 2);
        Property.set(CockpitSunderlandNGunner.class, "normZN", 0.3F);
        Property.set(CockpitSunderlandNGunner.class, "gsZN", 0.3F);
    }
}
