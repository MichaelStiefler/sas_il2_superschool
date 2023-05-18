package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitP1Y1_Gunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", 0.0F, orient.getYaw(), -orient.getYaw() * orient.getTangage() * 0.009F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage() * (1F - Math.abs(orient.getYaw()) * 0.005F), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -33F) f = -33F;
        if (f > 33F) f = 33F;
        if (f1 > 62F) f1 = 62F;
        if (f1 < -3F) f1 = -3F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret1A_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
            this.aircraft().hierMesh().chunkVisible("Turret1A_D0", true);
            super.doFocusLeave();
            return;
        }
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

    public CockpitP1Y1_Gunner() {
        super("3DO/Cockpit/P1Y1-Gun/hier.him", "he111_gunner");
        this.hook1 = null;
        this.bNeedSetUp = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XHullDamage1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XHullDamage2", true);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private Hook    hook1;
    private boolean bNeedSetUp;

    static {
        Property.set(CockpitP1Y1_Gunner.class, "aiTuretNum", 0);
        Property.set(CockpitP1Y1_Gunner.class, "weaponControlNum", 10);
        Property.set(CockpitP1Y1_Gunner.class, "astatePilotIndx", 2);
    }
}
