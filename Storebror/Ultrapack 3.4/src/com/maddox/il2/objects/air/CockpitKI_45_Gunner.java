package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitKI_45_Gunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (!this.isRealMode()) {
            this.mesh.chunkSetAngles("AI_pivot", 0.0F, f, 0.0F);
            this.mesh.chunkSetAngles("ZT98mg", 0.0F, f1, 0.0F);
            float f2 = f;
            float f4 = f1;
            if (f4 > 50F) f4 = 50F;
            if (f4 < -4F) f4 = -4F;
            if (f4 > 0.0F) {
                float f6 = this.cvt(f4, 0.0F, 50F, 11F, 20F);
                if (f2 > f6) f2 = f6;
                if (f2 < -f6) f2 = -f6;
            } else {
                float f7 = this.cvt(f4, -4F, 0.0F, 0.0F, 11F);
                if (f2 > f7) f2 = f7;
                if (f2 < -f7) f2 = -f7;
            }
            this.mesh.chunkSetAngles("ZCameraPivotYaw", 0.0F, 0.0F, -f2);
            this.mesh.chunkSetAngles("ZCameraPivotTan", 0.0F, f4, 0.0F);
            this.mesh.chunkSetAngles("ZTurret1", 0.0F, 10F, 0.0F);
            this.mesh.chunkSetAngles("UP_pivot", 0.0F, 0.0F, 10F);
            this.mesh.chunkSetAngles("ZCameraRollPivot", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("ZTurret4", 0.0F, 0.0F, 0.0F);
        } else {
            float f3 = 50F;
            f1 *= 1.0F + Math.abs(f) * 0.0006153846F;
            float f5 = f1;
            if (f5 > f3) f5 = f3;
            this.mesh.chunkSetAngles("AI_pivot", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("ZTurret4", 0.0F, -f * 1.01F, 0.0F);
            this.mesh.chunkSetAngles("ZT98mg", 0.0F, f5, 0.0F);
            float f8 = this.cvt(Math.abs(f), 0.0F, 65F, -5F, -10F);
            if (f5 < f8) f5 = f8;
            this.mesh.chunkSetAngles("ZCameraPivotTan", 0.0F, f5, 0.0F);
            this.mesh.chunkSetAngles("ZCameraPivotYaw", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("ZCameraRollPivot", -f, 0.0F, 0.0F);
            float f9 = Math.min(10F, (float) Math.abs(f * 0.5D));
            if (f1 > f3) this.mesh.chunkSetAngles("ZTurret1", 0.0F, this.cvt(f1, f3, 75F, 10F, 25F), 0.0F);
            else if (f1 < 10F) {
                float f10 = this.cvt(f1, 0.0F, 10F, f9, 10F);
                this.mesh.chunkSetAngles("ZTurret1", 0.0F, f10, 0.0F);
                this.mesh.chunkSetAngles("UP_pivot", 0.0F, 0.0F, f10);
            } else this.mesh.chunkSetAngles("ZTurret1", 0.0F, 10F, 0.0F);
        }
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        float f3 = this.cvt(Math.abs(f), 0.0F, 65F, -4F, -19F);
        if (f1 < f3) f1 = f3;
        if (f1 > 75F) f1 = 75F;
        float f2;
        if (!this.isRealMode()) f2 = 25F;
        else f2 = 65F;
        if (f < 0.0F) {
            if (f < -f2) f = -f2;
        } else if (f > f2) f = f2;
        orient.setYPR(-f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[10] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
            if (this.iCocking > 0) this.iCocking = 0;
            else this.iCocking = 1;
        } else this.iCocking = 0;
        if (this.emitter != null) {
            this.iNewVisDrums = (int) (this.emitter.countBullets() / 70F);
            if (this.iNewVisDrums < this.iOldVisDrums) {
                this.iOldVisDrums = this.iNewVisDrums;
                this.mesh.chunkVisible("SpareMag1", this.iNewVisDrums > 13);
                this.mesh.chunkVisible("SpareMag2", this.iNewVisDrums > 12);
                this.mesh.chunkVisible("SpareMag3", this.iNewVisDrums > 11);
                this.mesh.chunkVisible("SpareMag4", this.iNewVisDrums > 10);
                this.mesh.chunkVisible("SpareMag5", this.iNewVisDrums > 9);
                this.mesh.chunkVisible("SpareMag6", this.iNewVisDrums > 8);
                this.mesh.chunkVisible("SpareMag7", this.iNewVisDrums > 7);
                this.mesh.chunkVisible("SpareMag8", this.iNewVisDrums > 6);
                this.mesh.chunkVisible("SpareMag9", this.iNewVisDrums > 5);
                this.mesh.chunkVisible("SpareMag10", this.iNewVisDrums > 4);
                this.mesh.chunkVisible("SpareMag11", this.iNewVisDrums > 3);
                this.mesh.chunkVisible("SpareMag12", this.iNewVisDrums > 2);
                this.mesh.chunkVisible("SpareMag13", this.iNewVisDrums > 1);
                this.mesh.chunkVisible("SpareMag14", this.iNewVisDrums > 0);
                this.sfxClick(13);
            }
        }
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[1] = this.iCocking * 0.1F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        this.mesh.chunkSetLocate("Zbuffer", Aircraft.xyz, Aircraft.ypr);
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[10] = this.bGunFire;
    }

    public CockpitKI_45_Gunner() {
        super("3DO/Cockpit/Ki-45-Gun/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.iCocking = 0;
        this.iOldVisDrums = 14;
        this.iNewVisDrums = 14;
        this.hook1 = null;
        this.cockpitNightMats = new String[] { "Instr", "DInstr" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        KI_45 ki_45 = (KI_45) this.aircraft();
        ki_45.registerGunner(this);
        if (this.aircraft() instanceof KI_45_TEI) {
            this.mesh.chunkVisible("Ho3", false);
            this.mesh.chunkVisible("Ho3spare", false);
        } else if (this.aircraft() instanceof KI_45_OTSU) {
            this.mesh.chunkVisible("Ho3", false);
            this.mesh.chunkVisible("Ho3spare", false);
            this.mesh.chunkVisible("floor", false);
            this.mesh.chunkVisible("T94", true);
        }
        if (this.emitter == null) {
            this.mesh.chunkVisible("SpareMag1", false);
            this.mesh.chunkVisible("SpareMag2", false);
            this.mesh.chunkVisible("SpareMag3", false);
            this.mesh.chunkVisible("SpareMag4", false);
            this.mesh.chunkVisible("SpareMag5", false);
            this.mesh.chunkVisible("SpareMag6", false);
            this.mesh.chunkVisible("SpareMag7", false);
            this.mesh.chunkVisible("SpareMag8", false);
            this.mesh.chunkVisible("SpareMag9", false);
            this.mesh.chunkVisible("SpareMag10", false);
            this.mesh.chunkVisible("SpareMag11", false);
            this.mesh.chunkVisible("SpareMag12", false);
            this.mesh.chunkVisible("SpareMag13", false);
            this.mesh.chunkVisible("SpareMag14", false);
        }
    }

    public void showJazz() {
        this.mesh.chunkVisible("HO5", true);
        this.mesh.chunkVisible("Tank", false);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("WireAnt_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
            this.aircraft().hierMesh().chunkVisible("WireAnt_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WireAnt", hiermesh.isChunkVisible("Keel1_D0") || hiermesh.isChunkVisible("Keel1_D1") || hiermesh.isChunkVisible("Keel1_D2"));
    }

    public void reflectWorldToInstruments(float f) {
        if (this.fm == null) return;
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Zneedspeed", 0.0F, this.floatindex(this.cvt(0.539957F * Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 700F, 0.0F, 14F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Zneedhour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Zneedmin", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
    }

    private boolean            bNeedSetUp;
    private int                iCocking;
    private int                iOldVisDrums;
    private int                iNewVisDrums;
    private static final float speedometerScale[] = { 0.0F, 8.6F, 23.6F, 64.2F, 114.5F, 172.8F, 239.4F, 299F, 360F, 417F, 479F, 533F, 582F, 627F, 657F };
    private Hook               hook1;

    static {
        Property.set(CockpitKI_45_Gunner.class, "normZN", 0.82F);
    }
}
