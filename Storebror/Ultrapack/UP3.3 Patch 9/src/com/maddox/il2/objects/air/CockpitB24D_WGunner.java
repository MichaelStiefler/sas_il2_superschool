package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;

public abstract class CockpitB24D_WGunner extends CockpitGunner {

    public CockpitB24D_WGunner(String s) {
        super(s, "he111_gunner");
        this.bNeedSetUp = true;
        this.lastRTan = 0.0F;
        this.lastRYaw = 0.0F;
        this.lastLTan = 0.0F;
        this.lastLYaw = 0.0F;
        this.cockpitNightMats = new String[] { "alpha2", "oxygen2" };
        this.setNightMats(false);
        HookNamed hooknamed = new HookNamed(this.mesh, "_IntLight01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOKW1", this.light1);
        hooknamed = new HookNamed(this.mesh, "_IntlLight02");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOKW2", this.light2);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("skin", mat);
    }

    public void reflectWorldToInstruments(float f) {
        this.resetYPRmodifier();
        float f1 = ((B_24D140CO) this.aircraft()).fBallPos;
        if (((B_24D140CO) this.aircraft()).bBallRemoved) f1 = 1.0F;
        if (f1 < 0.02F) {
            Cockpit.xyz[2] = 0.5F * f1;
            this.mesh.chunkSetLocate("Turret3C_D0", Cockpit.xyz, Cockpit.ypr);
        } else {
            Cockpit.xyz[2] = -0.72F * f1 + 0.01F;
            this.mesh.chunkSetLocate("Turret3C_D0", Cockpit.xyz, Cockpit.ypr);
        }
        this.mesh.chunkSetAngles("zBallHook01", 0.0F, 0.0F, this.cvt(f1, 0.0F, 0.02F, 0.0F, -12F));
        this.mesh.chunkSetAngles("zBallHook02", 0.0F, 0.0F, this.cvt(f1, 0.0F, 0.02F, 0.0F, -12F));
        this.mesh.chunkSetAngles("zPulley01", 0.0F, 0.0F, f1 * 1170F);
        this.mesh.chunkSetAngles("zPulley02", 0.0F, 0.0F, f1 * 1170F);
        this.mesh.chunkSetAngles("zDrumNut", f1 * 10000F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpindleForward", f1 * 2070F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpindleRear", f1 * 2070F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFrameDrumRear", f1 * 110F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFrameDrumFwd", f1 * 110F, 0.0F, 0.0F);
        this.mesh.chunkVisible("zSafetyWireAdd", f1 > 0.5D);
        this.mesh.chunkSetAngles("zSperryTrunion", this.aircraft().FM.turret[2].tu[0], 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSperryBall", 0.0F, this.aircraft().FM.turret[2].tu[1], 0.0F);
        float f2 = ((B_24D140CO) this.aircraft()).fLGunPos;
        float f3 = ((B_24D140CO) this.aircraft()).fRGunPos;
        f2 = 1.0F - f2;
        f3 = 1.0F - f3;
        this.resetYPRmodifier();
        if (f3 < 0.04F) Cockpit.xyz[2] = this.cvt(f3, 0.0F, 0.03F, 0.0F, -0.02F);
        else if (f3 < 0.09F) Cockpit.xyz[2] = this.cvt(f3, 0.05F, 0.08F, -0.02F, 0.045F);
        else if (f3 < 0.85F) Cockpit.xyz[2] = this.cvt(f3, 0.81F, 0.84F, 0.045F, -0.02F);
        else Cockpit.xyz[2] = this.cvt(f3, 0.86F, 0.89F, -0.02F, 0.0F);
        if (f3 < 0.06F) Cockpit.ypr[0] = this.cvt(f3, 0.03F, 0.05F, 0.0F, -60F);
        else Cockpit.ypr[0] = this.cvt(f3, 0.84F, 0.86F, -60F, 0.0F);
        this.mesh.chunkSetLocate("GunLockRod_R", Cockpit.xyz, Cockpit.ypr);
        if (f3 > 0.01F) {
            if (f3 < 0.1F) this.mesh.chunkSetAngles("GunSwivelR_D0", 0.0F, this.cvt(f3, 0.05F, 0.2F, -this.lastRYaw, 38F), 0.0F);
            else this.mesh.chunkSetAngles("GunSwivelR_D0", 0.0F, this.cvt(f3, 0.2F, 0.57F, 38F, -100F), 0.0F);
            if (f3 < 0.15F) this.mesh.chunkSetAngles("WeaponRight_D0", 0.0F, this.cvt(f3, 0.08F, 0.15F, this.lastRTan, 0.0F), 0.0F);
            else this.mesh.chunkSetAngles("WeaponRight_D0", 0.0F, this.cvt(f3, 0.2F, 0.55F, 0.0F, -50F), 0.0F);
        }
        if (f3 < 0.5F) this.mesh.chunkSetAngles("GunTubesR", this.cvt(f3, 0.15F, 0.43F, 0.0F, -90F), 0.0F, 0.0F);
        else this.mesh.chunkSetAngles("GunTubesR", this.cvt(f3, 0.5F, 0.79F, -90F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("HatchDoorR", 0.0F, this.cvt(f3, 0.4F, 0.54F, 0.0F, 135F), 0.0F);
        this.mesh.chunkSetAngles("WinDRBar", this.cvt(f3, 0.9F, 1.0F, 0.0F, -28F), 0.0F, 0.0F);
        this.mesh.chunkVisible("WindDR", f3 < 0.95D);
        this.resetYPRmodifier();
        if (f2 < 0.04F) Cockpit.xyz[2] = this.cvt(f2, 0.0F, 0.03F, 0.0F, 0.02F);
        else if (f2 < 0.09F) Cockpit.xyz[2] = this.cvt(f2, 0.05F, 0.08F, 0.02F, -0.045F);
        else if (f2 < 0.85F) Cockpit.xyz[2] = this.cvt(f2, 0.81F, 0.84F, -0.045F, 0.02F);
        else Cockpit.xyz[2] = this.cvt(f2, 0.86F, 0.89F, 0.02F, 0.0F);
        if (f2 < 0.06F) Cockpit.ypr[0] = this.cvt(f2, 0.03F, 0.05F, 0.0F, -60F);
        else Cockpit.ypr[0] = this.cvt(f2, 0.84F, 0.86F, -60F, 0.0F);
        this.mesh.chunkSetLocate("GunLockRod_L", Cockpit.xyz, Cockpit.ypr);
        if (f2 > 0.01F) {
            if (f2 < 0.1F) this.mesh.chunkSetAngles("GunSwivelL_D0", 0.0F, this.cvt(f2, 0.05F, 0.2F, this.lastLYaw, -38F), 0.0F);
            else this.mesh.chunkSetAngles("GunSwivelL_D0", 0.0F, this.cvt(f2, 0.2F, 0.57F, -38F, 100F), 0.0F);
            if (f2 < 0.15F) this.mesh.chunkSetAngles("WeaponLeft_D0", 0.0F, this.cvt(f2, 0.08F, 0.15F, this.lastLTan, 0.0F), 0.0F);
            else this.mesh.chunkSetAngles("WeaponLeft_D0", 0.0F, this.cvt(f2, 0.2F, 0.55F, 0.0F, -50F), 0.0F);
        }
        if (f2 < 0.5F) this.mesh.chunkSetAngles("GunTubesL", this.cvt(f2, 0.15F, 0.43F, 0.0F, -90F), 0.0F, 0.0F);
        else this.mesh.chunkSetAngles("GunTubesL", this.cvt(f2, 0.5F, 0.79F, -90F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("HatchDoorL", 0.0F, this.cvt(f2, 0.4F, 0.54F, 0.0F, 135F), 0.0F);
        this.mesh.chunkSetAngles("WinDLBar", this.cvt(f2, 0.9F, 1.0F, 0.0F, -28F), 0.0F, 0.0F);
        this.mesh.chunkVisible("WindDL", f2 < 0.95D);
        if (((B_24D140CO) this.aircraft()).bBallRemoved) {
            this.mesh.chunkVisible("zSperryBall", false);
            this.mesh.chunkVisible("zSperryBallDoor", false);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
            this.light1.light.setEmit(0.8F, 4F);
            this.light2.light.setEmit(0.8F, 4F);
        } else {
            this.setNightMats(false);
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
        }
    }

    public void reflectCockpitState() {
//        if ((this.fm.AS.astateCockpitState & 4) != 0);
//        if ((this.fm.AS.astateCockpitState & 8) != 0);
//        if ((this.fm.AS.astateCockpitState & 0x10) != 0);
//        if ((this.fm.AS.astateCockpitState & 0x20) != 0);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Tail1Int_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Tail1Int_D0", true);
        super.doFocusLeave();
    }

    protected boolean       bNeedSetUp;
    private LightPointActor light1;
    private LightPointActor light2;
    protected float         lastRTan;
    protected float         lastRYaw;
    protected float         lastLTan;
    protected float         lastLYaw;
}
