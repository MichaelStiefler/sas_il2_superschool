package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Mission;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public abstract class CockpitB24D_FGunner extends CockpitGunner {
    private class Variables {

        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float      altimeter;
        float      wiper;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }
    }

    protected void interpTick() {
        this.setTmp = this.setOld;
        this.setOld = this.setNew;
        this.setNew = this.setTmp;
        this.setNew.altimeter = this.fm.getAltitude();
        float f = this.waypointAzimuth();
        this.setNew.azimuth.setDeg(this.setOld.azimuth.getDeg(1.0F), this.fm.Or.azimut());
        this.setNew.waypointAzimuth.setDeg(this.setOld.waypointAzimuth.getDeg(0.1F), f);
        this.setNew.radioCompassAzimuth.setDeg(this.setOld.radioCompassAzimuth.getDeg(0.1F), f - this.setOld.azimuth.getDeg(0.1F) - 90F);
        switch (this.iWiper) {
            default:
                break;

            case 0:
                if (Mission.curCloudsType() > 4 && this.fm.getSpeedKMH() < 220F && this.fm.getAltitude() < Mission.curCloudsHeight() + 300F) this.iWiper = 1;
                break;

            case 1:
                this.setNew.wiper = this.setOld.wiper - 0.05F;
                if (this.setNew.wiper < -1.03F) this.iWiper++;
                if (this.wiState >= 2) break;
                if (this.wiState == 0) {
                    if (this.fxw == null) {
                        this.fxw = this.aircraft().newSound("aircraft.wiper", false);
                        if (this.fxw != null) {
                            this.fxw.setParent(this.aircraft().getRootFX());
                            this.fxw.setPosition(this.sfxPos);
                        }
                    }
                    if (this.fxw != null) this.fxw.play(this.wiStart);
                }
                if (this.fxw != null) {
                    this.fxw.play(this.wiRun);
                    this.wiState = 2;
                }
                break;

            case 2:
                this.setNew.wiper = this.setOld.wiper + 0.05F;
                if (this.setNew.wiper > 1.03F) this.iWiper++;
                if (this.wiState > 1) this.wiState = 1;
                break;

            case 3:
                this.setNew.wiper = this.setOld.wiper - 0.05F;
                if (this.setNew.wiper >= 0.02F) break;
                if (this.fm.getSpeedKMH() > 250F || this.fm.getAltitude() > Mission.curCloudsHeight() + 400F) this.iWiper++;
                else this.iWiper = 1;
                break;

            case 4:
                this.setNew.wiper = this.setOld.wiper;
                this.iWiper = 0;
                this.wiState = 0;
                if (this.fxw != null) this.fxw.cancel();
                break;
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitB24D_FGunner(String s) {
        super(s, "bf109");
        this.internalBombs = new BulletEmitter[24];
        this.fxw = null;
        this.wiStart = new Sample("wip_002_s.wav", 256, 65535);
        this.wiRun = new Sample("wip_002.wav", 256, 65535);
        this.wiState = 0;
        this.iWiper = 0;
        this.patron = 1.0F;
        this.patronMat = null;
        this.salvoLever = 0.0F;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.cockpitNightMats = new String[] { "APanelSides", "Needles2", "oxygen2", "RadioComp", "textrbm9", "texture25" };
        this.setNightMats(false);
        for (int i = 0; i < this.internalBombs.length; i++) {
            String s1 = "_BombSpawn";
            if (i < 10) s1 = s1 + "0" + (i + 1);
            else s1 = s1 + (i + 1);
            this.internalBombs[i] = this.getBomb(s1);
        }

        int j = -1;
        j = this.mesh.materialFind("50CalRound");
        if (j != -1) {
            this.patronMat = this.mesh.material(j);
            this.patronMat.setLayer(0);
        }
    }

    private BulletEmitter getBomb(String s) {
        BulletEmitter bulletemitter = this.aircraft().getBulletEmitterByHookName(s);
        return bulletemitter;
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("zHolesG_D1", true);
    }

    protected boolean doFocusEnter() {
        return super.doFocusEnter();
    }

    protected void doFocusLeave() {
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float f) {
        float f1 = ((B_24D140CO) this.aircraft()).fSightCurForwardAngle;
        float f2 = ((B_24D140CO) this.aircraft()).fSightCurAltitude;
        float f3 = ((B_24D140CO) this.aircraft()).fSightCurSpeed;
        boolean flag = ((B_24D140CO) this.aircraft()).bSightClutch;
        float f4 = ((B_24D140CO) this.aircraft()).fSightCurSideslip;
        float f5 = ((B_24D140CO) this.aircraft()).fSightHeadTurn * 30F;
        float f6 = ((B_24D140CO) this.aircraft()).getBombSightPDI();
        this.mesh.chunkSetAngles("zFootBall", f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zNPDI", f6 * -10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBsClutch", f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBSArm01", f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBSArm02", f6 * 10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zClutchLever", flag ? 0.0F : 30F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zNAutoClutch", f6 * -10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSightAngle", 0.0F, 0.0F, -f1);
        this.mesh.chunkSetAngles("zMirDrClutch", 0.0F, 0.0F, 5F * f1);
        this.mesh.chunkSetAngles("zBSpeed", 0.0F, 0.0F, -(f3 * 0.5F) - f2 * 0.1F);
        this.mesh.chunkSetAngles("zNTurnDrift", f4, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zWiperBottom", this.cvt(this.interp(this.setNew.wiper, this.setOld.wiper, f), -1F, 1.0F, -61F, 61F), 0.0F, 0.0F);
        for (int i = 0; i < this.internalBombs.length; i++)
            this.mesh.chunkVisible("rack" + (i + 1) + "On", this.internalBombs[i].haveBullets());

        this.mesh.chunkSetLocate("zArmingPLRod", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zHSalvoLock", 0.0F, 0.0F, this.salvoLever * 30F);
        this.mesh.chunkSetAngles("zHSalvoLockRod", 0.0F, 0.0F, this.salvoLever * 29.9F);
        this.mesh.chunkSetAngles("zHSalvoLkPL", 0.0F, 0.0F, this.salvoLever * 20.5F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = 0.0075F * this.salvoLever;
        this.mesh.chunkSetLocate("zHSalvoLkPLRod", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("bombReleaseOn", this.fm.CT.saveWeaponControl[3]);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F));
        this.mesh.chunkSetAngles("zAlt1", 0.0F, 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F));
        this.mesh.chunkSetAngles("zCompass1", 0.0F, 0.0F, -this.setNew.azimuth.getDeg(f) - 90F);
        this.mesh.chunkSetAngles("zCompass2", 0.0F, 0.0F, -this.setNew.waypointAzimuth.getDeg(f * 0.1F) - 90F);
        this.mesh.chunkSetAngles("zCompass3", -this.setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed1", 0.0F, 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale));
        if (this.fm.CT.WeaponControl[0] && this.patronMat != null) {
            this.patron += 0.07F * f;
            this.patronMat.setLayer(0);
            this.patronMat.set((byte) 11, this.patron);
        }
        this.mesh.chunkSetAngles("zO2CylPress", 0.0F, 0.0F, -130F);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    private SoundFX            fxw;
    private Sample             wiStart;
    private Sample             wiRun;
    private int                wiState;
    private int                iWiper;
    private float              patron;
    private Mat                patronMat;
    private float              salvoLever;
    private BulletEmitter      internalBombs[];
    private static final float speedometerScale[] = { 0.0F, 2.5F, 54F, 104F, 154.5F, 205.5F, 224F, 242F, 259.5F, 277.5F, 296.25F, 314F, 334F, 344.5F };
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
}
