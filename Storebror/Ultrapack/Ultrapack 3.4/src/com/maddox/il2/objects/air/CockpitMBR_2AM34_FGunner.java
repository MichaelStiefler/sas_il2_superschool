package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitMBR_2AM34_FGunner extends CockpitGunner {
    private class Variables {

        AnglesFork azimuth;
        float      altimeter;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("zTurret1A", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -orient.getTangage());
        float f = orient.getTangage();
        float f1 = 0.0F;
        if (f > 45F) f1 = 45F - f;
        else if (f < -10F) f1 = -10F - f;
        this.mesh.chunkSetAngles("CameraRodB", 0.0F, 0.0F, -f1);
        this.mesh.chunkSetAngles("CameraRodC", 0.0F, 0.0F, f1);
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
            return;
        }
        if (f < -120F && this.prevA0 > 120F) f += 360F;
        else if (f > 120F && this.prevA0 < -120F) this.prevA0 += 360F;
        float f3 = f - this.prevA0;
        float f12 = 0.001F * (Time.current() - this.prevTime);
        float f13 = Math.abs(f3 / f12);
        if (f13 > 120F) if (f > this.prevA0) f = this.prevA0 + 120F * f12;
        else if (f < this.prevA0) f = this.prevA0 - 120F * f12;
        this.prevTime = Time.current();
        f3 = 0.0F;
        if (f1 > 89.5F) f1 = 89.5F;
        if (f1 < -40F) f1 = -40F;
        if (f >= -90F && f <= 90F) {
            if (f1 < -40F) f1 = -40F;
        } else if (f >= -156.6F && f <= 156.6F) {
            float f4 = this.cvt(Math.abs(f), 90F, 156.6F, -40F, -11.89F);
            if (f1 < f4) f1 = f4;
        } else if (f >= -158.6F && f <= 158.6F) {
            float f5 = this.cvt(Math.abs(f), 156.6F, 158.6F, -11.89F, -3.09F);
            if (f1 < f5) f1 = f5;
        } else if (f > -162F && f < 162F) {
            float f6 = this.cvt(Math.abs(f), 158.6F, 162F, -3.09F, 18.69F);
            if (f1 < f6) f1 = f6;
        } else if (f1 < 40F) {
            if (f1 < 18.69F) f1 = 18.69F;
            if (f >= 0.0F) f = 162F;
            else f = -162F;
        } else {
            float f7 = this.cvt(Math.abs(f), 162F, 180F, 40F, 50F);
            if (f1 < f7) f1 = f7;
        }
        this.bDontShot = false;
        if ((f <= -111.55F || f >= 111.55F) && f1 > -11.63F && f1 < 3F) if (f1 > 2.5F) {
            if (f <= -113.44F || f >= 113.44F) {
                float f8 = this.cvt(Math.abs(f), 113.44F, 180F, 3F, 2.5F);
                this.MinDontShoot(f8, f1);
            } else if (f <= -112F && f >= -113.44F || f >= 112F && f <= 113.44F) {
                float f9 = this.cvt(Math.abs(f), 112F, 113.44F, 2.5F, 3F);
                this.MinDontShoot(f9, f1);
            }
        } else if (Math.abs(f) > 111.55F) {
            float f10 = this.cvt(Math.abs(f), 111.55F, 180F, 1.76F, -11.63F);
            this.MaxDontShoot(f10, f1);
        } else if (Math.abs(f) > 111.55F && Math.abs(f) < 112F) {
            float f11 = this.cvt(Math.abs(f), 111.55F, 112F, 1.76F, 2.5F);
            this.MaxDontShoot(f11, f1);
        }
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        this.prevA0 = f;
    }

    private void MinDontShoot(float f, float f1) {
        if (f1 < f) this.bDontShot = true;
        else this.bDontShot = false;
    }

    private void MaxDontShoot(float f, float f1) {
        if (f1 > f) this.bDontShot = true;
        else this.bDontShot = false;
    }

    protected void interpTick() {
        this.setTmp = this.setOld;
        this.setOld = this.setNew;
        this.setNew = this.setTmp;
        this.setNew.altimeter = this.fm.getAltitude();
        this.setNew.azimuth.setDeg(this.setOld.azimuth.getDeg(1.0F), this.fm.Or.azimut());
        if (this.bDontShot) this.bGunFire = false;
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        if (this.bDontShot) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitMBR_2AM34_FGunner() {
        super("3DO/Cockpit/MBR-2-FGun/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.Gun1 = null;
        this.bDontShot = false;
        this.ac = null;
        this.prevTime = -1L;
        this.prevA0 = 0.0F;
        this.hook1 = null;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.ac = (MBR_2xyz) this.aircraft();
        this.setNightMats(false);
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMP03");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light3 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light3.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light3.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMP03", this.light3);
        hooknamed = new HookNamed(this.mesh, "LAMP04");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light4 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light4.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light4.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMP04", this.light4);
        hooknamed = new HookNamed(this.mesh, "LAMP05");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light5 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light5.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light5.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMP05", this.light5);
        Aircraft aircraft = this.aircraft();
        try {
            if (!(aircraft.getGunByHookName("_MGUN01") instanceof GunEmpty)) this.Gun1 = aircraft.getGunByHookName("_MGUN01");
        } catch (Exception exception) {}
        this.Patrons = 0.5F;
        this.PatronsMat = null;
        int i = -1;
        i = this.mesh.materialFind("patron");
        if (i != -1) {
            this.PatronsMat = this.mesh.material(i);
            this.PatronsMat.setLayer(0);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("xHullDm7", true);
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xHullDm2", true);
            else this.mesh.chunkVisible("xHullDm4", true);
            this.mesh.chunkVisible("Prib_2", false);
            this.mesh.chunkVisible("DPrib_2", true);
            this.mesh.chunkVisible("Z_ND_Airspeed", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xHullDm1", true);
            else this.mesh.chunkVisible("xHullDm6", true);
            this.mesh.chunkVisible("Prib_1", false);
            this.mesh.chunkVisible("DPrib_1", true);
            this.mesh.chunkVisible("Z_ND_ACHO_H1", false);
            this.mesh.chunkVisible("Z_ND_ACHO_M1", false);
            this.mesh.chunkVisible("Z_ND_ACHO_S1", false);
            this.mesh.chunkVisible("Z_ND_Alt_M", false);
            this.mesh.chunkVisible("Z_ND_Alt_KM", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) this.mesh.chunkVisible("xHullDm3", true);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.bNeedSetUp = false;
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
        }
        if (((MBR_2xyz) this.aircraft()).bChangedPit) {
            this.reflectPlaneToModel();
            ((MBR_2xyz) this.aircraft()).bChangedPit = false;
        }
        this.mesh.chunkSetAngles("Z_ND_ACHO_H1", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_ACHO_M1", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_ACHO_S1", -this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Airspeed", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 400F, 0.0F, 9F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Alt_M", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Alt_KM", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_AN4", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        if (this.Gun1 != null) {
            if (this.bGunFire && this.Gun1.haveBullets() && this.PatronsMat != null) {
                this.Patrons -= -0.35F * f;
                this.PatronsMat.setLayer(0);
                this.PatronsMat.set((byte) 11, this.Patrons);
            }
            if (World.cur().diffCur.Limited_Ammo && this.Gun1.countBullets() < 5) this.mesh.chunkVisible("Patrons", false);
        } else this.mesh.chunkVisible("Patrons", false);
        if (this.ac.bRSArmed) this.mesh.chunkVisible("Sight", true);
        else this.mesh.chunkVisible("Sight", false);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D2o"));
        this.mesh.materialReplace("Matt1D2o", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0_00", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1_00", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2_00", hiermesh.isChunkVisible("CF_D2") || hiermesh.isChunkVisible("CF_D3"));
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light3.light.setEmit(0.6F, 0.8F);
            this.light4.light.setEmit(0.6F, 0.8F);
            this.light5.light.setEmit(0.6F, 0.8F);
        } else {
            this.light3.light.setEmit(0.0F, 0.0F);
            this.light4.light.setEmit(0.0F, 0.0F);
            this.light5.light.setEmit(0.0F, 0.0F);
        }
        this.setNightMats(false);
    }

    public boolean useMultiFunction() {
        return true;
    }

    public void doMultiFunction(boolean flag) {
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret1A_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            this.bNeedSetUp = true;
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret1A_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        super.doFocusLeave();
    }

    private LightPointActor    light3;
    private LightPointActor    light4;
    private LightPointActor    light5;
    private boolean            bNeedSetUp;
    private Gun                Gun1;
    private float              Patrons;
    private Mat                PatronsMat;
    private boolean            bDontShot;
    private MBR_2xyz           ac;
    private long               prevTime;
    private float              prevA0;
    private Hook               hook1;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private static final float speedometerScale[] = { 0.0F, 30F, 47F, 5F, 85F, 133.5F, 190F, 248.5F, 305F, 360F };

    static {
        Class class1 = CockpitMBR_2AM34_FGunner.class;
        Property.set(class1, "aiTuretNum", 0);
        Property.set(class1, "weaponControlNum", 10);
        Property.set(class1, "astatePilotIndx", 1);
        Property.set(class1, "normZN", 0.5F);
    }
}
