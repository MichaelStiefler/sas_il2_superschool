package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHalifaxBMkIII_AGunner extends CockpitGunner {

    protected boolean doFocusEnter()
    {
      if (super.doFocusEnter())
      {
        aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
        aircraft().hierMesh().chunkVisible("Turret3C_D0", false);
        return true;
      }
      return false;
    }
    
    protected void doFocusLeave()
    {
      aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
      aircraft().hierMesh().chunkVisible("Turret3C_D0", true);
      super.doFocusLeave();
    }
    
    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode())
            if (!this.aiTurret().bIsOperable) {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if (f < -38F)
                    f = -38F;
                if (f > 38F)
                    f = 38F;
                if (f1 > 38F)
                    f1 = 38F;
                if (f1 < -41F)
                    f1 = -41F;
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable)
                this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null)
                    this.hook1 = new HookNamed(this.aircraft(), "_MGUN05");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN05");
                if (this.hook2 == null)
                    this.hook2 = new HookNamed(this.aircraft(), "_MGUN06");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN06");
                if (this.hook3 == null)
                    this.hook3 = new HookNamed(this.aircraft(), "_MGUN09");
                this.doHitMasterAircraft(this.aircraft(), this.hook3, "_MGUN09");
                if (this.hook4 == null)
                    this.hook4 = new HookNamed(this.aircraft(), "_MGUN10");
                this.doHitMasterAircraft(this.aircraft(), this.hook4, "_MGUN10");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable)
                this.bGunFire = false;
            else
                this.bGunFire = flag;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public CockpitHalifaxBMkIII_AGunner() {
        super("3DO/Cockpit/Halifax-AGun/hier.him", "bf109");
//        super("3DO/Cockpit/BoultonPaulQuadTurret/HalifaxBMkIIIAGunner.him", "bf109");
        this.hook1 = null;
        this.hook2 = null;
        this.hook3 = null;
        this.hook4 = null;
    }

    private Hook    hook1;
    private Hook    hook2;
    private Hook    hook3;
    private Hook    hook4;

    static {
        Property.set(CockpitHalifaxBMkIII_AGunner.class, "aiTuretNum", 2);
        Property.set(CockpitHalifaxBMkIII_AGunner.class, "weaponControlNum", 12);
        Property.set(CockpitHalifaxBMkIII_AGunner.class, "astatePilotIndx", 4);
    }
}
