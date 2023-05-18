package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB24D_TGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret2B_D0", this.aircraft().hierMesh().isChunkVisible("Turret2A_D0"));
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Tmain", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Cradle", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("CradleU", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("CradleRods", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("GunPull", 0.0F, orient.getTangage(), 0.0F);
        this.yaw = orient.getYaw();
        this.tan = orient.getTangage();
    }

    public void clipAnglesGun(Orient orient) {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        for (; f < -180F; f += 360F)
            ;
        for (; f > 180F; f -= 360F)
            ;
        for (; this.prevA0 < -180F; this.prevA0 += 360F)
            ;
        for (; this.prevA0 > 180F; this.prevA0 -= 360F)
            ;
        if (!this.isRealMode()) {
            this.prevA0 = f;
            this.prevA1 = f1;
            return;
        }
        if (f < -this.yawVelMax && this.prevA0 > this.yawVelMax) f += 360F;
        else if (f > this.yawVelMax && this.prevA0 < -this.yawVelMax) this.prevA0 += 360F;
        float f3 = f - this.prevA0;
        float f4 = 0.001F * (Time.current() - this.prevTime);
        float f5 = Math.abs(f3 / f4);
        if (f5 > this.yawVelMax) if (f > this.prevA0) f = this.prevA0 + this.yawVelMax * f4;
        else if (f < this.prevA0) f = this.prevA0 - this.yawVelMax * f4;
        if (f1 > 85F) f1 = 85F;
        if (f1 < -6.5F) f1 = -6.5F;
        f3 = f1 - this.prevA1;
        f4 = 0.001F * (Time.current() - this.prevTime);
        f5 = Math.abs(f3 / f4);
        if (f5 > this.tanVelMax) if (f1 > this.prevA1) f1 = this.prevA1 + this.tanVelMax * f4;
        else if (f1 < this.prevA1) f1 = this.prevA1 - this.tanVelMax * f4;
        this.prevTime = Time.current();
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        this.prevA0 = f;
        this.prevA1 = f1;
    }

    protected void interpTick() {
        if (this.bNeedSetUp) {
            this.prevTime = Time.current() - 1L;
            this.bNeedSetUp = false;
            this.reflectPlaneMats();
        }
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            this.ammoBeltAnim += 0.25F;
            if (this.ammoBeltAnim > 1.0F) this.ammoBeltAnim = 0.0F;
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN05");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN05");
            if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN06");
            this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN06");
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.prevTime = Time.current() - 1L;
            this.bNeedSetUp = false;
            this.reflectPlaneMats();
        }
        float f1 = this.prevYaw - this.yaw;
        float f2 = this.prevTan - this.tan;
        float f3 = 0.001F * (Time.current() - this.prevMoveTime);
        float f4 = Math.abs(f1 / f3);
        if (Float.isNaN(f4)) f4 = 0.0F;
        float f5 = 1.0F;
        if (f1 < 0.0F) f5 = -1F;
        this.controlYaw = 0.9F * this.controlYaw + 0.1F * this.cvt(f4, 0.0F, this.yawVelMax, 0.0F, 30F * f5);
        this.mesh.chunkSetAngles("TContrl", 0.0F, this.controlYaw, 0.0F);
        f4 = Math.abs(f2 / f3);
        if (Float.isNaN(f4)) f4 = 0.0F;
        f5 = 1.0F;
        if (f2 < 0.0F) f5 = -1F;
        this.controlTan = 0.9F * this.controlTan + 0.1F * this.cvt(f4, 0.0F, this.tanVelMax, 0.0F, 30F * f5);
        this.mesh.chunkSetAngles("Handles", 0.0F, this.controlTan, 0.0F);
        this.prevYaw = this.yaw;
        this.prevTan = this.tan;
        this.prevMoveTime = Time.current();
        B_24D140CO b_24d140co = (B_24D140CO) this.aircraft();
        this.resetYPRmodifier();
        if (this.bGunFire) {
            BulletEmitter abulletemitter[] = this.fm.CT.Weapons[this.weaponControlNum()];
            if (!b_24d140co.topLeftGunJammed) {
                this.mesh.chunkSetAngles("TBoostr2L", 0.0F, this.ammoBeltAnim * -40F, 0.0F);
                if (World.cur().diffCur.Limited_Ammo) {
                    int i = abulletemitter[0].countBullets();
                    for (int l = 1; l <= 11; l++)
                        this.mesh.chunkVisible("Ammo" + l + "L", i > 13 - l);

                }
            }
            if (!b_24d140co.topRightGunJammed) {
                this.mesh.chunkSetAngles("TBoostr2R", 0.0F, this.ammoBeltAnim * -40F, 0.0F);
                if (World.cur().diffCur.Limited_Ammo) {
                    int j = abulletemitter[1].countBullets();
                    for (int i1 = 1; i1 <= 11; i1++)
                        this.mesh.chunkVisible("Ammo" + i1 + "R", j > 13 - i1);

                }
            }
        }
        float f6 = this.tan;
        if (f6 < 0.0F) f6 = 0.0F;
        for (int k = 1; k <= 11; k++) {
            Aircraft.xyz[1] = f6 * (11 - k) * -5E-005F;
            if (!b_24d140co.topRightGunJammed) {
                Aircraft.xyz[2] = this.beltAnimCoords[k - 1][1] * this.ammoBeltAnim * 0.05F;
                Aircraft.xyz[0] = this.beltAnimCoords[k - 1][0] * this.ammoBeltAnim * 0.05F;
                Aircraft.ypr[1] = this.beltAnimRot[k - 1] * -this.ammoBeltAnim;
            }
            this.mesh.chunkSetLocate("Ammo" + k + "R", Aircraft.xyz, Aircraft.ypr);
            if (!b_24d140co.topLeftGunJammed) {
                Aircraft.xyz[2] = this.beltAnimCoords[k - 1][1] * this.ammoBeltAnim * 0.05F;
                Aircraft.xyz[0] = this.beltAnimCoords[k - 1][0] * this.ammoBeltAnim * -0.05F;
                Aircraft.ypr[1] = this.beltAnimRot[k - 1] * this.ammoBeltAnim;
            }
            this.mesh.chunkSetLocate("Ammo" + k + "L", Aircraft.xyz, Aircraft.ypr);
        }

    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("Glass_Dmg", true);
    }

    public CockpitB24D_TGunner() {
        super("3DO/Cockpit/B-24D-TGun/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.yawVelMax = 43F;
        this.tanVelMax = 30F;
        this.yaw = 0.0F;
        this.tan = 0.0F;
        this.prevTan = 0.0F;
        this.prevYaw = 0.0F;
        this.prevMoveTime = -1L;
        this.controlYaw = 0.0F;
        this.controlTan = 0.0F;
        this.ammoBeltAnim = 0.0F;
        this.prevTime = -1L;
        this.prevA0 = 0.0F;
        this.prevA1 = 0.0F;
        this.hook1 = null;
        this.hook2 = null;
        this.cockpitNightMats = new String[] { "Tcrwgear" };
        this.setNightMats(false);
        if (this.emitter == null) for (int i = 1; i <= 11; i++) {
            this.mesh.chunkVisible("Ammo" + i + "L", false);
            this.mesh.chunkVisible("Ammo" + i + "R", false);
        }
    }

    private boolean     bNeedSetUp;
    private float       yawVelMax;
    private float       tanVelMax;
    private float       yaw;
    private float       tan;
    private float       prevTan;
    private float       prevYaw;
    private long        prevMoveTime;
    private float       controlYaw;
    private float       controlTan;
    private float       ammoBeltAnim;
    private final float beltAnimCoords[][] = { { 0.019F, 0.622F }, { 0.078F, 0.561F }, { 0.431F, 0.377F }, { 0.549F, -0.016F }, { 0.529F, -0.212F }, { 0.506F, -0.053F }, { 0.512F, -0.051F }, { 0.487F, 0.115F }, { 0.445F, 0.257F }, { 0.491F, 0.152F },
            { 0.491F, 0.0F } };
    private final float beltAnimRot[]      = { 0.0F, 42F, 38F, 18F, -12F, 0.0F, -15F, -15F, 10F, 10F, 0.0F };
    private long        prevTime;
    private float       prevA0;
    private float       prevA1;
    private Hook        hook1;
    private Hook        hook2;

    static {
        Property.set(CockpitB24D_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitB24D_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitB24D_TGunner.class, "astatePilotIndx", 3);
        Property.set(CockpitB24D_TGunner.class, "normZN", 1.2F);
    }
}
