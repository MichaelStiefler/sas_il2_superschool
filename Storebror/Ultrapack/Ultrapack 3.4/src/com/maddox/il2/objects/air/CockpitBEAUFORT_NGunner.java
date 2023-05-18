package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitBEAUFORT_NGunner extends CockpitGunner {

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("z_Turret1A", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("z_Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f1 > 45F) f1 = 45F;
        if (f1 < -40F) f1 = -40F;
        if (f > 40F) f = 40F;
        if (f < -40F) f = -40F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN04");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN04");
            if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN05");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN05");
            if (this.iCocking > 0) this.iCocking = 0;
            else this.iCocking = 1;
        } else this.iCocking = 0;
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    protected void reflectPlaneMats() {
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassHoles1", true);
            this.mesh.chunkVisible("XGlassHoles3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassHoles7", true);
            this.mesh.chunkVisible("XGlassHoles4", true);
            this.mesh.chunkVisible("XGlassHoles2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassHoles5", true);
            this.mesh.chunkVisible("XGlassHoles3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XGlassHoles1", true);
        this.mesh.chunkVisible("XGlassHoles6", true);
        this.mesh.chunkVisible("XGlassHoles4", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("XGlassHoles6", true);
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XGlassHoles7", true);
            this.mesh.chunkVisible("XGlassHoles2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("XGlassHoles5", true);
            this.mesh.chunkVisible("XGlassHoles2", true);
        }
    }

    public CockpitBEAUFORT_NGunner() {
        super("3DO/Cockpit/Do217k1/NGunnerBEAUFORT.him", "he111_gunner");
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.hook1 = null;
        this.iCocking = 0;
    }

    public Vector3f w;
    private boolean bNeedSetUp;
    private Hook    hook1;
    private Hook    hook2;
    private int     iCocking;

    static {
        Property.set(CockpitBEAUFORT_NGunner.class, "aiTuretNum", 1);
        Property.set(CockpitBEAUFORT_NGunner.class, "weaponControlNum", 11);
        Property.set(CockpitBEAUFORT_NGunner.class, "astatePilotIndx", 2);
    }
}
