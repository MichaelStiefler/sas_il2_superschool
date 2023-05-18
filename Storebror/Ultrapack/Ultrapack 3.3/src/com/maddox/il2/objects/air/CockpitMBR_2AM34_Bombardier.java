package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitMBR_2AM34_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitMBR_2AM34_Bombardier.this.fm != null) {
                if (CockpitMBR_2AM34_Bombardier.this.bNeedSetUp) {
                    CockpitMBR_2AM34_Bombardier.this.reflectPlaneMats();
                    CockpitMBR_2AM34_Bombardier.this.bNeedSetUp = false;
                }
                CockpitMBR_2AM34_Bombardier.this.setTmp = CockpitMBR_2AM34_Bombardier.this.setOld;
                CockpitMBR_2AM34_Bombardier.this.setOld = CockpitMBR_2AM34_Bombardier.this.setNew;
                CockpitMBR_2AM34_Bombardier.this.setNew = CockpitMBR_2AM34_Bombardier.this.setTmp;
                if (CockpitMBR_2AM34_Bombardier.this.bEntered) {
                    MBR_2xyz mbr_2xyz = (MBR_2xyz) CockpitMBR_2AM34_Bombardier.this.aircraft();
                    float f = mbr_2xyz.fSightCurForwardAngle + mbr_2xyz.tSAim;
                    float f1 = -mbr_2xyz.fSightCurSideslip;
                    CockpitMBR_2AM34_Bombardier.this.mesh.chunkSetAngles("BlackBox", f1, -mbr_2xyz.kSAim, -f);
                    HookPilot hookpilot = HookPilot.current;
                    hookpilot.setInstantOrient(-f1, CockpitMBR_2AM34_Bombardier.this.tAim + f, 0.0F);
                } else {
                    MBR_2xyz mbr_2xyz1 = (MBR_2xyz) CockpitMBR_2AM34_Bombardier.this.aircraft();
                    CockpitMBR_2AM34_Bombardier.this.mesh.chunkSetAngles("OPB1", -mbr_2xyz1.fSightCurSideslip, mbr_2xyz1.kSAim, mbr_2xyz1.tSAim);
                    CockpitMBR_2AM34_Bombardier.this.mesh.chunkSetAngles("Z_OPB_b", -mbr_2xyz1.fSightCurSideslip, 0.0F, 0.0F);
                }
                CockpitMBR_2AM34_Bombardier.this.setNew.altimeter = CockpitMBR_2AM34_Bombardier.this.fm.getAltitude();
                CockpitMBR_2AM34_Bombardier.this.setNew.azimuth.setDeg(CockpitMBR_2AM34_Bombardier.this.setOld.azimuth.getDeg(1.0F), CockpitMBR_2AM34_Bombardier.this.fm.Or.azimut());
                if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3] != null) {
                    CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 0;
                    if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3].length == 8) {
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][7].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 1;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][6].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 2;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][5].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 3;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][4].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 4;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][3].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 5;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][2].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 6;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][1].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 7;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][0].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 8;
                    }
                    if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3].length == 6) {
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][5].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 1;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][4].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 2;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][3].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 3;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][2].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 4;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][1].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 5;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][0].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 6;
                    }
                    if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3].length == 4) {
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][3].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 1;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][2].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 2;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][1].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 3;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][0].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 4;
                    }
                    if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3].length == 2) {
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][1].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 1;
                        if (CockpitMBR_2AM34_Bombardier.this.fm.CT.Weapons[3][0].haveBullets()) CockpitMBR_2AM34_Bombardier.this.OstatokBomb = 2;
                    }
                }
                CockpitMBR_2AM34_Bombardier.this.setNew.Asbr2 = CockpitMBR_2AM34_Bombardier.this.setOld.Asbr2 = 0.0F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        AnglesFork azimuth;
        float      altimeter;
        float      Asbr2;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    public float[] getBombSightFovs() {
        return this.defaultBSightFoVsOPB;
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.25D, 0.0D, -0.09D);
            hookpilot.setTubeSight(point3d);
            this.aircraft().FM.turret[0].bIsAIControlled = false;
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
            this.leave();
            this.aircraft().FM.turret[0].bIsAIControlled = true;
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter() {
        if (this.OPBOff) return;
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, -90F, 0.0F);
        this.enteringAim = true;
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov " + this.saveBSFov);
        Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
        HookPilot hookpilot = HookPilot.current;
        MBR_2xyz mbr_2xyz = (MBR_2xyz) this.aircraft();
        hookpilot.setSimpleAimOrient(this.aAim, this.tAim + mbr_2xyz.tSAim, mbr_2xyz.kSAim);
        hookpilot.setInstantOrient(this.aAim, this.tAim + mbr_2xyz.tSAim, mbr_2xyz.kSAim);
        hookpilot.setSimpleUse(true);
        this.doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(0.0F, -90F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if (!this.bEntered) return;
        else {
            this.saveBSFov = Main3D.FOVX;
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.setInstantOrient(0.0F, -90F, 0.0F);
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            this.doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            return;
        }
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused()) return;
        if (this.isToggleAim() == flag) return;
        if (flag) this.prepareToEnter();
        else this.leave();
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public CockpitMBR_2AM34_Bombardier() {
        super("3DO/Cockpit/MBR-2-Bombardier/hier.him", "bf109");
        this.enteringAim = false;
        this.bNeedSetUp = true;
        this.OPBOff = false;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.saveBSFov = 50F;
        this.bEntered = false;
        this.OstatokBomb = 0;
        try {
            Loc loc = new Loc();
            HookNamed hooknamed1 = new HookNamed(this.mesh, "CAMERAAIM");
            hooknamed1.computePos(this, new Loc(), loc);
            this.aAim = loc.getOrient().getAzimut();
            this.tAim = loc.getOrient().getTangage();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        this.setNightMats(false);
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMP03");
        Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
        this.light3 = new LightPointActor(new LightPoint(), loc1.getPoint());
        this.light3.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light3.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMP03", this.light3);
        hooknamed = new HookNamed(this.mesh, "LAMP04");
        loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
        this.light4 = new LightPointActor(new LightPoint(), loc1.getPoint());
        this.light4.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light4.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMP04", this.light4);
        hooknamed = new HookNamed(this.mesh, "LAMP05");
        loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
        this.light5 = new LightPointActor(new LightPoint(), loc1.getPoint());
        this.light5.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light5.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMP05", this.light5);
        this.interpPut(new Interpolater(), null, Time.current(), null);
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

    public void reflectWorldToInstruments(float f) {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isAimReached()) {
                this.enteringAim = false;
                this.enter();
            } else if (!hookpilot.isAim()) this.enteringAim = false;
        }
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.fm.getAltitude() < 100F) {
            this.OPBOff = true;
            this.mesh.chunkVisible("OPBOpen", false);
            this.mesh.chunkVisible("OPB1", false);
            this.mesh.chunkVisible("Z_OPB_b", false);
            this.mesh.chunkVisible("Z_ND_StW", false);
            this.mesh.chunkVisible("Z_ND_StWsmall", false);
            this.mesh.chunkVisible("OPBClose", true);
            this.mesh.chunkVisible("OPBOff", true);
        } else {
            this.OPBOff = false;
            this.mesh.chunkVisible("OPBOpen", true);
            this.mesh.chunkVisible("OPB1", true);
            this.mesh.chunkVisible("Z_OPB_b", true);
            this.mesh.chunkVisible("Z_ND_StW", true);
            this.mesh.chunkVisible("Z_ND_StWsmall", true);
            this.mesh.chunkVisible("OPBClose", false);
            this.mesh.chunkVisible("OPBOff", false);
        }
        this.mesh.chunkSetAngles("Z_ND_ACHO_H1", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_ACHO_M1", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_ACHO_S1", -this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Airspeed", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 450F, 0.0F, 9F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Alt_M", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Alt_KM", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_AN4", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        if (this.fm.AS.externalStoresDropped) this.mesh.chunkSetAngles("Z_EmrgBombDrop", -90F, 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_FlareESBR", this.fm.CT.saveWeaponControl[3] && this.OstatokBomb > 0);
        this.mesh.chunkSetAngles("Z_ESBR01", 0.0F, 0.0F, this.floatindex(this.OstatokBomb, ESBR_Scale));
        this.mesh.chunkSetAngles("Z_ESBR03", 0.0F, 0.0F, Atmosphere.temperature((float) this.fm.Loc.z) >= 258F ? 0.0F : 150F);
        this.mesh.chunkSetAngles("Z_EmrgBombDrop", -30F * this.setNew.Asbr2, 0.0F, 0.0F);
        this.mesh.chunkVisible("BlackBox", this.bEntered);
        this.mesh.chunkVisible("zReticle", this.bEntered);
        this.mesh.chunkVisible("zMark1", this.bEntered);
        this.mesh.chunkVisible("zMark2", this.bEntered);
        this.mesh.chunkVisible("zBulb", this.bEntered);
        this.mesh.chunkVisible("zRefraction", this.bEntered);
        if (this.bEntered) {
            MBR_2xyz mbr_2xyz = (MBR_2xyz) this.aircraft();
            this.mesh.chunkSetAngles("zMark1", mbr_2xyz.fSightCurForwardAngle * 3.675F, 0.0F, 0.0F);
            float f1 = this.cvt(mbr_2xyz.fSightSetForwardAngle, -15F, 75F, -15F, 75F);
            this.mesh.chunkSetAngles("zMark2", f1 * 3.675F, 0.0F, 0.0F);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = this.cvt(this.fm.Or.getKren() + mbr_2xyz.kSAim, -13F, 13F, 0.23F, -0.23F);
            Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage() + mbr_2xyz.tSAim, -13F, 13F, -0.23F, 0.23F);
            Cockpit.ypr[0] = this.cvt(this.fm.Or.getKren(), -45F, 45F, -180F, 180F);
            this.mesh.chunkSetLocate("zBulb", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[0] = this.cvt(Cockpit.xyz[0], -0.23F, 0.23F, 0.0095F, -0.0095F);
            Cockpit.xyz[1] = this.cvt(Cockpit.xyz[1], -0.23F, 0.23F, 0.0095F, -0.0095F);
            this.mesh.chunkSetLocate("zRefraction", Cockpit.xyz, Cockpit.ypr);
        }
    }

    protected void reflectPlaneMats() {
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("xHullDm6", true);
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xHullDm2", true);
            else this.mesh.chunkVisible("xHullDm4", true);
            this.mesh.chunkVisible("Prib_2", false);
            this.mesh.chunkVisible("DPrib_2", true);
            this.mesh.chunkVisible("Z_ND_Airspeed", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xHullDm1", true);
            else this.mesh.chunkVisible("xHullDm5", true);
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

    private boolean            enteringAim;
    private LightPointActor    light3;
    private LightPointActor    light4;
    private LightPointActor    light5;
    private boolean            bNeedSetUp;
    private boolean            OPBOff;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private static final float speedometerScale[]     = { 0.0F, 8.745F, 17.49F, 54.38F, 103.28F, 159.8F, 217.47F, 273.84F, 328.7F, 382.4F };
    private static final float ESBR_Scale[]           = { 0.0F, -17.8F, -39.5F, -52.8F, -68.5F, -80.5F, -95F, -108.6F, -121.7F };
    private final float        defaultBSightFoVsOPB[] = { 15F, 50F, 65F };
    private float              saveFov;
    private float              saveBSFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;
    private int                OstatokBomb;

    static {
        Class class1 = CockpitMBR_2AM34_Bombardier.class;
        Property.set(class1, "astatePilotIndx", 1);
        Property.set(class1, "aiTuretNum", -2);
        Property.set(class1, "weaponControlNum", 3);
    }

}
