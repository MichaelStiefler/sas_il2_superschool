package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111P2_Late_BGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Korzina_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret4B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            ((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = true;
            this.aircraft().hierMesh().chunkVisible("Korzina_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret4B_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot4_D0"));
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot4_D1"));
            super.doFocusLeave();
        }
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretBA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretBB", 0.0F, orient.getTangage(), 0.0F);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("TurretLA", 0.0F, this.fm.turret[3].tu[0], 0.0F);
        this.mesh.chunkSetAngles("TurretLB", 0.0F, this.fm.turret[3].tu[1], 0.0F);
        this.mesh.chunkSetAngles("TurretRA", 0.0F, this.fm.turret[4].tu[0], 0.0F);
        this.mesh.chunkSetAngles("TurretRB", 0.0F, this.fm.turret[4].tu[1], 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -35F) f = -35F;
        if (f > 40F) f = 40F;
        if (f1 > 46F) f1 = 46F;
        if (f1 < -30F) f1 = -30F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
            if (this.iCocking > 0) this.iCocking = 0;
            else this.iCocking = 1;
            this.iNewVisDrums = (int) (this.emitter.countBullets() / 250F);
            if (this.iNewVisDrums < this.iOldVisDrums) {
                this.iOldVisDrums = this.iNewVisDrums;
                this.mesh.chunkVisible("DrumB1", this.iNewVisDrums > 1);
                this.mesh.chunkVisible("DrumB2", this.iNewVisDrums > 0);
                this.sfxClick(13);
            }
        } else this.iCocking = 0;
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -0.07F * this.iCocking;
        this.mesh.chunkSetLocate("LeverB", Cockpit.xyz, Cockpit.ypr);
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitHE_111P2_Late_BGunner() {
        super("3DO/Cockpit/He-111P-4-BGun/hier-H2.him", "he111_gunner");
        this.hook1 = null;
        this.iCocking = 0;
        this.iOldVisDrums = 2;
        this.iNewVisDrums = 2;
        HookNamed hooknamed = new HookNamed(this.mesh, "LIGHT1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(203F, 198F, 161F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LIGHT1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LIGHT2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(203F, 198F, 161F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LIGHT2", this.light2);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.004F, 6.05F);
            this.light2.light.setEmit(1.1F, 0.2F);
            this.mesh.chunkVisible("Flare", true);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.mesh.chunkVisible("Flare", false);
            this.setNightMats(false);
        }
    }

    public void reflectCockpitState() {
        if (this.fm.AS.astateCockpitState != 0) this.mesh.chunkVisible("Holes_D1", true);
    }

    private LightPointActor light1;
    private LightPointActor light2;
    private Hook            hook1;
    private int             iCocking;
    private int             iOldVisDrums;
    private int             iNewVisDrums;

    static {
        Property.set(CockpitHE_111P2_Late_BGunner.class, "aiTuretNum", 2);
        Property.set(CockpitHE_111P2_Late_BGunner.class, "weaponControlNum", 12);
        Property.set(CockpitHE_111P2_Late_BGunner.class, "astatePilotIndx", 3);
    }
}
