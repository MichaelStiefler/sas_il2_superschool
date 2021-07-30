package com.maddox.il2.objects.air;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_177A3_BGunner extends CockpitGunner {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHE_177A3_BGunner.this.fm != null) {
                CockpitHE_177A3_BGunner.this.setTmp = CockpitHE_177A3_BGunner.this.setOld;
                CockpitHE_177A3_BGunner.this.setOld = CockpitHE_177A3_BGunner.this.setNew;
                CockpitHE_177A3_BGunner.this.setNew = CockpitHE_177A3_BGunner.this.setTmp;
                CockpitHE_177A3_BGunner.this.setNew.AirEnemy = 0.95F * CockpitHE_177A3_BGunner.this.setOld.AirEnemy + 0.05F * (((HE_177A3) CockpitHE_177A3_BGunner.this.fm.actor).bAirEnemy ? 1.0F : 0.0F);
                if (CockpitHE_177A3_BGunner.this.cockpitDimControl) {
                    if (CockpitHE_177A3_BGunner.this.setNew.dimPosition > 0.0F) CockpitHE_177A3_BGunner.this.setNew.dimPosition = CockpitHE_177A3_BGunner.this.setOld.dimPosition - 0.05F;
                } else if (CockpitHE_177A3_BGunner.this.setNew.dimPosition < 1.0F) CockpitHE_177A3_BGunner.this.setNew.dimPosition = CockpitHE_177A3_BGunner.this.setOld.dimPosition + 0.05F;
                float f = CockpitHE_177A3_BGunner.this.waypointAzimuth();
                if (CockpitHE_177A3_BGunner.this.useRealisticNavigationInstruments()) {
                    CockpitHE_177A3_BGunner.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitHE_177A3_BGunner.this.setOld.waypointAzimuth.setDeg(f - 90F);
                    CockpitHE_177A3_BGunner.this.setNew.radioCompassAzimuth.setDeg(CockpitHE_177A3_BGunner.this.setOld.radioCompassAzimuth.getDeg(0.02F),
                            CockpitHE_177A3_BGunner.this.radioCompassAzimuthInvertMinus() - CockpitHE_177A3_BGunner.this.setOld.azimuth.getDeg(1.0F) - 90F);
                } else CockpitHE_177A3_BGunner.this.setNew.waypointAzimuth.setDeg(CockpitHE_177A3_BGunner.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitHE_177A3_BGunner.this.setOld.azimuth.getDeg(1.0F));
                CockpitHE_177A3_BGunner.this.setNew.azimuth.setDeg(CockpitHE_177A3_BGunner.this.setOld.azimuth.getDeg(1.0F), CockpitHE_177A3_BGunner.this.fm.Or.azimut());
                CockpitHE_177A3_BGunner.this.setNew.throttle1 = 0.85F * CockpitHE_177A3_BGunner.this.setOld.throttle1 + CockpitHE_177A3_BGunner.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitHE_177A3_BGunner.this.setNew.throttle2 = 0.85F * CockpitHE_177A3_BGunner.this.setOld.throttle2 + CockpitHE_177A3_BGunner.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitHE_177A3_BGunner.this.setNew.pictAiler = 0.85F * CockpitHE_177A3_BGunner.this.setOld.pictAiler + 0.15F * CockpitHE_177A3_BGunner.this.cvt(CockpitHE_177A3_BGunner.this.fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F);
                CockpitHE_177A3_BGunner.this.setNew.pictElev = 0.85F * CockpitHE_177A3_BGunner.this.setOld.pictElev + 0.15F * CockpitHE_177A3_BGunner.this.cvt(CockpitHE_177A3_BGunner.this.fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F);
                CockpitHE_177A3_BGunner.this.setNew.elevTrim = 0.85F * CockpitHE_177A3_BGunner.this.setOld.elevTrim + 0.15F * CockpitHE_177A3_BGunner.this.fm.CT.trimElevator;
                CockpitHE_177A3_BGunner.this.setNew.rudderTrim = 0.85F * CockpitHE_177A3_BGunner.this.setOld.rudderTrim + 0.15F * CockpitHE_177A3_BGunner.this.fm.CT.trimRudder;
                CockpitHE_177A3_BGunner.this.setNew.ailTrim = 0.85F * CockpitHE_177A3_BGunner.this.setOld.ailTrim + 0.15F * CockpitHE_177A3_BGunner.this.fm.CT.trimAileron;
                if (Math.toDegrees(CockpitHE_177A3_BGunner.this.fm.EI.engines[0].getPropPhi()) < 36D) {
                    CockpitHE_177A3_BGunner.this.setNew.prop1 = 0.85F * CockpitHE_177A3_BGunner.this.setOld.prop1 + ((FlightModelMain) CockpitHE_177A3_BGunner.this.fm).EI.engines[0].getControlProp() * 0.15F;
                    CockpitHE_177A3_BGunner.this.setNew._prop1 = CockpitHE_177A3_BGunner.this.cvt(CockpitHE_177A3_BGunner.this.setNew.prop1, 0.0F, 1.0F, -22F, -63F);
                } else {
                    CockpitHE_177A3_BGunner.this.setNew.prop1 = 0.85F * CockpitHE_177A3_BGunner.this.setOld.prop1;
                    CockpitHE_177A3_BGunner.this.setNew._prop1 = CockpitHE_177A3_BGunner.this.cvt(CockpitHE_177A3_BGunner.this.setNew.prop1, 0.0F, 1.0F, 0.0F, -63F);
                }
                if (Math.toDegrees(CockpitHE_177A3_BGunner.this.fm.EI.engines[1].getPropPhi()) < 36D) {
                    CockpitHE_177A3_BGunner.this.setNew.prop2 = 0.85F * CockpitHE_177A3_BGunner.this.setOld.prop2 + ((FlightModelMain) CockpitHE_177A3_BGunner.this.fm).EI.engines[1].getControlProp() * 0.15F;
                    CockpitHE_177A3_BGunner.this.setNew._prop2 = CockpitHE_177A3_BGunner.this.cvt(CockpitHE_177A3_BGunner.this.setNew.prop2, 0.0F, 1.0F, -22F, -63F);
                } else {
                    CockpitHE_177A3_BGunner.this.setNew.prop2 = 0.85F * CockpitHE_177A3_BGunner.this.setOld.prop2;
                    CockpitHE_177A3_BGunner.this.setNew._prop2 = CockpitHE_177A3_BGunner.this.cvt(CockpitHE_177A3_BGunner.this.setNew.prop2, 0.0F, 1.0F, 0.0F, -63F);
                }
                CockpitHE_177A3_BGunner.this.w.set(CockpitHE_177A3_BGunner.this.fm.getW());
                CockpitHE_177A3_BGunner.this.fm.Or.transform(CockpitHE_177A3_BGunner.this.w);
                CockpitHE_177A3_BGunner.this.setNew.turn = (12F * CockpitHE_177A3_BGunner.this.setOld.turn + CockpitHE_177A3_BGunner.this.w.z) / 13F;
                CockpitHE_177A3_BGunner.this.setNew.altimeter = 0.85F * CockpitHE_177A3_BGunner.this.setOld.altimeter + CockpitHE_177A3_BGunner.this.fm.getAltitude() * 0.15F;
                CockpitHE_177A3_BGunner.this.setNew.vspeed = (99F * CockpitHE_177A3_BGunner.this.setOld.vspeed + CockpitHE_177A3_BGunner.this.fm.getVertSpeed()) / 100F;
                float f1 = CockpitHE_177A3_BGunner.this.fm.Or.getKren();
                float f2 = CockpitHE_177A3_BGunner.this.fm.Or.getTangage();
                if (f1 > 55F || f1 < -55F || f2 < -55F || f2 > 55F) CockpitHE_177A3_BGunner.this.Pn.z = 250D;
                else {
                    CockpitHE_177A3_BGunner.this.Pn.set(CockpitHE_177A3_BGunner.this.fm.Loc);
                    CockpitHE_177A3_BGunner.this.Pn.z = CockpitHE_177A3_BGunner.this.fm.getAltitude() - Engine.cur.land.HQ(((Tuple3d) CockpitHE_177A3_BGunner.this.Pn).x, ((Tuple3d) CockpitHE_177A3_BGunner.this.Pn).y);
                    double d = CockpitHE_177A3_BGunner.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f1));
                    double d1 = CockpitHE_177A3_BGunner.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f2));
                    CockpitHE_177A3_BGunner.this.Pn.z = (float) Math.sqrt(d * d + d1 * d1 + CockpitHE_177A3_BGunner.this.Pn.z * CockpitHE_177A3_BGunner.this.Pn.z);
                    if (CockpitHE_177A3_BGunner.this.fm.CT.getGear() > 0.5F) CockpitHE_177A3_BGunner.this.Pn.z = CockpitHE_177A3_BGunner.this.cvt((float) CockpitHE_177A3_BGunner.this.Pn.z, 0.0F, 150F, 0.0F, 250F);
                    else CockpitHE_177A3_BGunner.this.Pn.z = CockpitHE_177A3_BGunner.this.cvt((float) CockpitHE_177A3_BGunner.this.Pn.z, 0.0F, 750F, 0.0F, 250F);
                }
                CockpitHE_177A3_BGunner.this.setNew.AFN101 = 0.9F * CockpitHE_177A3_BGunner.this.setOld.AFN101 + 0.1F * (float) CockpitHE_177A3_BGunner.this.Pn.z;
                CockpitHE_177A3_BGunner.this.setNew.beaconDirection = (10F * CockpitHE_177A3_BGunner.this.setOld.beaconDirection + CockpitHE_177A3_BGunner.this.getBeaconDirection()) / 11F;
                CockpitHE_177A3_BGunner.this.setNew.beaconRange = (10F * CockpitHE_177A3_BGunner.this.setOld.beaconRange + CockpitHE_177A3_BGunner.this.getBeaconRange()) / 11F;
                CockpitHE_177A3_BGunner.this.setNew.pictManf1 = 0.9F * CockpitHE_177A3_BGunner.this.setOld.pictManf1 + 0.1F * CockpitHE_177A3_BGunner.this.cvt(CockpitHE_177A3_BGunner.this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                CockpitHE_177A3_BGunner.this.setNew.pictManf2 = 0.8F * CockpitHE_177A3_BGunner.this.setOld.pictManf2 + 0.2F * CockpitHE_177A3_BGunner.this.cvt(CockpitHE_177A3_BGunner.this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                CockpitHE_177A3_BGunner.this.setNew.pictManf3 = 0.9F * CockpitHE_177A3_BGunner.this.setOld.pictManf3 + 0.1F * CockpitHE_177A3_BGunner.this.cvt(CockpitHE_177A3_BGunner.this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                CockpitHE_177A3_BGunner.this.setNew.pictManf4 = 0.8F * CockpitHE_177A3_BGunner.this.setOld.pictManf4 + 0.2F * CockpitHE_177A3_BGunner.this.cvt(CockpitHE_177A3_BGunner.this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                float f3 = CockpitHE_177A3_BGunner.this.setNew.prevFuel - CockpitHE_177A3_BGunner.this.fm.M.fuel;
                CockpitHE_177A3_BGunner.this.setNew.prevFuel = CockpitHE_177A3_BGunner.this.fm.M.fuel;
                f3 /= 0.72F;
                f3 /= Time.tickLenFs();
                f3 *= 3600F;
                CockpitHE_177A3_BGunner.this.setNew.cons = 0.9F * CockpitHE_177A3_BGunner.this.setOld.cons + 0.1F * f3;
                float f4 = CockpitHE_177A3_BGunner.this.fm.EI.engines[0].getEngineForce().x;
                float f5 = CockpitHE_177A3_BGunner.this.fm.EI.engines[1].getEngineForce().x;
                float f6 = CockpitHE_177A3_BGunner.this.setNew.cons;
                CockpitHE_177A3_BGunner.this.setNew.consumptionL = 0.9F * CockpitHE_177A3_BGunner.this.setOld.consumptionL + 0.1F * (f6 * f4) / (f4 + f5 + 1.0F);
                CockpitHE_177A3_BGunner.this.setNew.consumptionR = 0.9F * CockpitHE_177A3_BGunner.this.setOld.consumptionR + 0.1F * (f6 * f5) / (f4 + f5 + 1.0F);
                CockpitHE_177A3_BGunner.this.setNew.bombDoor = 0.9F * CockpitHE_177A3_BGunner.this.setOld.bombDoor + 0.1F * CockpitHE_177A3_BGunner.this.fm.CT.getBayDoor();
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      pictAiler;
        float      pictElev;
        float      elevTrim;
        float      rudderTrim;
        float      ailTrim;
        float      prop1;
        float      _prop1;
        float      prop2;
        float      _prop2;
        float      turn;
        float      altimeter;
        float      vspeed;
        float      AFN101;
        float      beaconDirection;
        float      beaconRange;
        float      pictManf1;
        float      pictManf2;
        float      pictManf3;
        float      pictManf4;
        float      prevFuel;
        float      cons;
        float      consumptionL;
        float      consumptionR;
        float      bombDoor;
        float      AirEnemy;
        float      dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Interior1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Interior1_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        super.doFocusLeave();
    }

    public boolean isToggleAim() {
        return this.bAiming;
    }

    public void doToggleAim(boolean flag) {
    }

    public void doToggleAim2(boolean flag) {
        this.bAiming = flag;
        this.mesh.chunkVisible("F_Reticle131R", !flag);
        this.mesh.chunkVisible("F_Reticle131L", flag);
        this.doToggleAim(flag);
    }

    public Hook getHookCameraGun() {
        if (this.bAiming) return super.getHookCameraGun();
        else return this.ObserverHook;
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = -orient.getTangage();
        float f2 = Geom.DEG2RAD(f);
        float f3 = Geom.DEG2RAD(f1);
        float f4 = (float) Math.asin(Math.tan(f2) * Math.cos(f3) * Math.cos(f2));
        float f5 = (float) Math.atan(Math.tan(f3) / Math.cos(f2));
        float f6 = Geom.RAD2DEG(f4);
        float f7 = Geom.RAD2DEG(f5);
        this.mesh.chunkSetAngles("Z_Turret131B", f6, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Turret131A", 0.0F, 0.0F, f7);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, -3F, 0.0F);
        else {
            float f = 0.0F;
            float f1 = -3F;
            if (this.aircraft().FM.CT.getBayDoor() <= 0.05F) {
                float f2 = orient.getYaw();
                float f3 = orient.getTangage();
                if (f2 >= 0.0F) {
                    if (f3 >= -35F) {
                        if (f3 >= -3F && f2 < 40.03915F) {
                            if (f2 > 40.03915F) f2 = 40.03915F;
                            if (f3 > this.cvt(f2, 0.0F, 40.03915F, -3F, -2.311216F)) f3 = this.cvt(f2, 0.0F, 40.03915F, -3F, -2.311216F);
                        } else {
                            if (f3 > -2.311216F) f3 = -2.311216F;
                            if (f2 > this.cvt(f3, -35F, -2.311216F, 59F, 40.03915F)) f2 = this.cvt(f3, -35F, -2.311216F, 59F, 40.03915F);
                        }
                    } else if (f3 >= -51.67F) {
                        if (f2 > this.cvt(f3, -51.67F, -35F, 71.48F, 59F)) f2 = this.cvt(f3, -51.67F, -35F, 71.48F, 59F);
                    } else {
                        if (f2 > 71.48F) f2 = 71.48F;
                        if (f3 < this.cvt(f2, 0.0F, 71.48F, -80F, -51.67F)) f3 = this.cvt(f2, 0.0F, 71.48F, -80F, -51.67F);
                    }
                } else if (f3 >= -35F) {
                    if (f3 >= -3F && f2 > -40.03915F) {
                        if (f2 < -40.03915F) f2 = -40.03915F;
                        if (f3 > this.cvt(f2, -40.03915F, 0.0F, -2.311216F, -3F)) f3 = this.cvt(f2, -40.03915F, 0.0F, -2.311216F, -3F);
                    } else {
                        if (f3 > -2.311216F) f3 = -2.311216F;
                        if (f2 < this.cvt(f3, -35F, -2.311216F, -59F, -40.03915F)) f2 = this.cvt(f3, -35F, -2.311216F, -59F, -40.03915F);
                    }
                } else if (f3 >= -43.29834F) {
                    if (f2 < this.cvt(f3, -43.29834F, -35F, -42.30127F, -59F)) f2 = this.cvt(f3, -43.29834F, -35F, -42.30127F, -59F);
                } else if (f3 < -56.89526F) {
                    if (f2 < -57.896F) f2 = -57.896F;
                    if (f3 < this.cvt(f2, -57.896F, 0.0F, -56.89526F, -80F)) f3 = this.cvt(f2, -57.896F, 0.0F, -56.89526F, -80F);
                } else if (f2 < this.cvt(f3, -56.89526F, -43.29834F, -57.896F, -42.30127F)) f2 = this.cvt(f3, -56.89526F, -43.29834F, -57.896F, -42.30127F);
                f = f2;
                f1 = f3;
            }
            this.CurrentYaw = f;
            if (this.isToggleAim()) {
                if (this.CurrentYaw > 15F) this.doToggleAim2(false);
            } else if (this.CurrentYaw < -15F) this.doToggleAim2(true);
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
        }
    }

    protected void interpTick() {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            else this.bGunFire = flag;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.gun != null) {
            this.resetYPRmodifier();
            this.countBullets = this.gun.countBullets();
            Cockpit.xyz[0] = -this.cvt(this.countBullets, 0.0F, 1000F, 0.054F, 0.0F);
            this.mesh.chunkSetLocate("Z_LSK2A", Cockpit.xyz, Cockpit.ypr);
            this.resetYPRmodifier();
        }
        if (((HE_177A3) this.aircraft()).iRust < 1) {
            float f2 = this.aircraft().FM.CT.getBayDoor();
            this.mesh.chunkSetAngles("Bay1_D0", 0.0F, -74F * f2, 0.0F);
            this.mesh.chunkSetAngles("Bay2_D0", 0.0F, -90F * f2, 0.0F);
            this.mesh.chunkSetAngles("Bay3_D0", 0.0F, -74F * f2, 0.0F);
            this.mesh.chunkSetAngles("Bay4_D0", 0.0F, -90F * f2, 0.0F);
        }
        if (this.aircraft().FM.CT.getBayDoor() > 0.05F) {
            this.mesh.chunkSetAngles("Z_Turret131B", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Turret131A", 0.0F, 0.0F, 3F);
        }
        this.mesh.chunkSetAngles("Z_MG131TintL", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 45F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MG131TintR", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 45F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Lotfe7", ((HE_177A3) this.fm.actor).fSightCurSideslip * -10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gears", this.fm.CT.getGear() != 1.0F || this.fm.CT.getGear() != 1.0F || !this.fm.Gears.lgear || !this.fm.Gears.rgear ? 0.0F : 90F, 0.0F, 0.0F);
        float f3 = this.fm.CT.getFlap();
        float f4 = 0.0F;
        if (f3 > 0.5F) f4 = 90F;
        else if (f3 > 0.1F && f3 < 0.5F) f4 = 45F;
        this.mesh.chunkSetAngles("Z_Flaps", f4, 0.0F, 0.0F);
        this.mesh.chunkVisible("F_EngOverheatL", this.fm.AS.astateEngineStates[0] > 4);
        this.mesh.chunkVisible("F_EngOverheatR", this.fm.AS.astateEngineStates[1] > 4);
        if (this.setNew.pictElev < 0.0F) this.mesh.chunkSetAngles("Z_CColumn", 0.0F, 15F * -this.setNew.pictElev, 0.0F);
        else this.mesh.chunkSetAngles("Z_CColumn", 0.0F, 15F * -this.setNew.pictElev, 0.0F);
        this.mesh.chunkSetAngles("Z_Handle", 93F * this.setNew.pictAiler, 0.0F, 0.0F);
        float f5 = this.fm.CT.getRudder();
        this.mesh.chunkSetAngles("Z_RichagL", -35F * f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RichagR", 35F * f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Koromislo1L", 0.0F, 0.0F, 25F * f5);
        this.mesh.chunkSetAngles("Z_PedalBomL", 0.0F, 0.0F, -25F * f5);
        this.mesh.chunkSetAngles("Z_Koromislo1R", 0.0F, 0.0F, -25F * f5);
        this.mesh.chunkSetAngles("Z_PedalBomR", 0.0F, 0.0F, 25F * f5);
        float f6 = this.fm.CT.getBrake();
        float f7 = this.fm.CT.getBrake();
        float f8 = 20F * f6;
        float f9 = 20F * f7;
        double d = Math.cos(Geom.DEG2RAD(142.0612F + f8));
        double d1 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d);
        float f10 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d) / d1));
        float f11 = 21.38197F - f10;
        float f12 = 89.43449F + f11 + 35F * f5;
        double d2 = Math.sqrt(0.035435102880001068D + d1 * d1 - 0.37648427486419678D * d1 * (float) Math.cos(Geom.DEG2RAD(f12)));
        float f13 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d2 * d2) / 0.031839244067668915D));
        float f14 = 89.43554F - f13;
        this.mesh.chunkSetAngles("Z_Tiaga2L", f14, 0.0F, 0.0F);
        float f15 = (float) Math.acos((0.035435102880001068D + d2 * d2 - d1 * d1) / (0.37648427486419678D * d2));
        float f16 = (float) Math.acos((0.0071521135978400707D + d2 * d2 - 0.035434890538454056D) / (0.16914033889770508D * d2));
        float f17 = Geom.RAD2DEG(f15 + f16) - 90.56514F;
        this.mesh.chunkSetAngles("Z_LBrake", 0.0F, 0.0F, f17);
        double d3 = Math.cos(Geom.DEG2RAD(142.0612F + f9));
        double d4 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d3);
        float f18 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d3) / d4));
        float f19 = 21.38197F - f18;
        float f20 = 89.43449F + f19 - 35F * f5;
        double d5 = Math.sqrt(0.035435102880001068D + d4 * d4 - 0.37648427486419678D * d4 * (float) Math.cos(Geom.DEG2RAD(f20)));
        float f21 = (float) Math.acos((0.035435102880001068D + d5 * d5 - d4 * d4) / (0.37648427486419678D * d5));
        float f22 = (float) Math.acos((0.0071521135978400707D + d5 * d5 - 0.035434890538454056D) / (0.16914033889770508D * d5));
        float f23 = Geom.RAD2DEG(f21 + f22) - 90.56514F;
        this.mesh.chunkSetAngles("Z_RBrake", 0.0F, 0.0F, f23);
        this.mesh.chunkSetAngles("Z_PropPitch1", -this.setNew._prop1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch2", -this.setNew._prop2, 0.0F, 0.0F);
        float f24 = (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin());
        f24 = (int) (f24 / 0.2F) * 0.2F;
        this.mesh.chunkSetAngles("Z_N_Clock1_M", f24 * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Clock1_H", f24 * 5F, 0.0F, 0.0F);
        float f25 = (float) Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[1].getPropPhiMin());
        f25 = (int) (f25 / 0.2F) * 0.2F;
        this.mesh.chunkSetAngles("Z_N_Clock2_M", f25 * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Clock2_H", f25 * 5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BombRelease", this.cvt(this.setNew.bombDoor, 0.0F, 1.0F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_OxPress2", -200F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E1CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E2CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E3CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E4CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E1OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E2OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E3OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E4OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirPress1", -170F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirPress2", -170F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LandingLight", this.fm.AS.bLandingLightOn ? 90F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NavLights", this.fm.AS.bNavLightsOn ? 90F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_CockpitLight", this.cockpitLightControl ? 90F : 0.0F, 0.0F, 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Fl22338b", -(this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f)), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Fl22338c", this.setNew.waypointAzimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Fl22338b", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Fl22338c", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Course2a", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
//        mesh.chunkSetAngles("Z_Course2b", ((HE_177A3)aircraft()).getCourse(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MG81B", 0.0F, this.fm.turret[0].tu[0], 0.0F);
        this.mesh.chunkSetAngles("Z_MG81A", 0.0F, this.fm.turret[0].tu[1], 0.0F);
        float f26;
        for (f26 = -this.fm.turret[3].tu[0]; f26 < -180F; f26 += 360F)
            ;
        for (; f26 > 180F; f26 -= 360F)
            ;
        float f27 = this.fm.turret[3].tu[1];
        this.mesh.chunkSetAngles("Z_TurretA", -f26, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurretB", 0.0F, 0.0F, -f27);
        this.mesh.chunkSetAngles("Z_tLink", 0.0F, 0.0F, this.floatindex(this.cvt(f27, 0.0F, 90F, 0.0F, 18F), Turret_Z1));
        this.mesh.chunkSetAngles("Z_tHandle", 0.0F, 0.0F, -this.floatindex(this.cvt(f27, 0.0F, 90F, 0.0F, 18F), Turret_Z2));
        this.mesh.chunkSetAngles("Z_tReviVal", 0.0F, -f26 * 5F, 0.0F);
        this.CalculateRevi(f26, f27);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = 0.185F * this.setNew.AirEnemy;
        this.mesh.chunkSetLocate("Z_TurretA_Seat", Cockpit.xyz, Cockpit.ypr);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("xGlassDm1", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("xHullDm1", true);
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("xGlass1", false);
            this.mesh.chunkVisible("xGlass1_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("xGlassDm2", true);
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("xGlass3", false);
            this.mesh.chunkVisible("xGlass3_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("xGlass5", false);
            this.mesh.chunkVisible("xGlass5_dmg", true);
        }
        if (!this.cockpitLightControl) {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    public CockpitHE_177A3_BGunner() {
        super("3DO/Cockpit/He-177A-3-BGun/hier.him", "he111_gunner");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.bAiming = true;
        this.ObserverHook = null;
        this.CurrentYaw = 0.0F;
        this.gun = null;
        this.countBullets = 0;
        this.ObserverHook = new HookNamed(this.mesh, "CAMERAAIM");
        this.setNew.dimPosition = this.setOld.dimPosition = 0.0F;
        this.cockpitDimControl = false;
        this.w = new Vector3f();
        this.Pn = new Point3d();
        this.cockpitNightMats = new String[] { "CompGrad", "Fl20342na", "Fl20342", "Fl20516", "Fl20841", "Fl22334b", "Fl22334c", "Fl22561", "Fl30489", "Fl30532", "Gauge20", "NeedlesnLights" };
        // this.hidePilot = true;
        if (this.gun == null) {
            this.gun = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN03");
            this.countBullets = this.gun.countBullets();
        }
        this.setNightMats(false);
        this.interpPut(new Interpolater(), (Object) null, Time.current(), (Message) null);
    }

    public void CalculateRevi(float f, float f1) {
        ((HE_177A3) this.fm.actor).CalculateRevi(-f, f1);
        float f2 = ((HE_177A3) this.fm.actor).getB1_HeadTangage();
        float f3 = ((HE_177A3) this.fm.actor).getB1_HeadYaw();
        float f4 = ((HE_177A3) this.fm.actor).getB1_HeadTangage1();
        float f5 = ((HE_177A3) this.fm.actor).getB1_ReviYaw();
        float f6 = ((HE_177A3) this.fm.actor).getB1_ReviX();
        float f7 = ((HE_177A3) this.fm.actor).getB1_ReviY();
        this.mesh.chunkSetAngles("Z_TurretB2", 0.0F, 0.0F, f2);
        this.mesh.chunkSetAngles("Z_Turret_Revi", 0.0F, f3, 0.0F);
        this.mesh.chunkSetAngles("Z_tRevi1", -f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_tRevi2", f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_tRevi4", f, 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -f7;
        this.mesh.chunkSetLocate("Z_tRevi5", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = f6;
        this.mesh.chunkSetLocate("Z_tRevi3", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_tRevi6", 0.0F, 0.0F, f4);
        this.mesh.chunkSetAngles("Z_tRevi7", f5, 0.0F, 0.0F);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private Point3d            Pn;
    private static final float Turret_Z1[] = { 0.0F, 4.235F, 8.64F, 13.21F, 17.92F, 22.79F, 27.82F, 32.99F, 38.29F, 43.75F, 49.36F, 55.12F, 61.05F, 67.13F, 73.4F, 79.87F, 86.55F, 93.45F, 100.61F };
    private static final float Turret_Z2[] = { 0.0F, 2.125F, 4.32F, 6.58F, 8.87F, 11.188F, 13.53F, 15.88F, 18.21F, 20.52F, 22.78F, 24.98F, 27.105F, 29.13F, 31.03F, 32.76F, 34.33F, 35.68F, 36.78F };
//    private static final float IAS_Scale[] = {
//        0.0F, 8F, 18.4F, 41.26F, 67.26F, 94.36F, 119.58F, 141F, 166.1F, 190.43F,
//        216.05F, 241.18F, 267.5F, 293.8F, 318.19F, 341.7F
//    };
    private static final float Fl20342_Scale[] = { 0.0F, 5.5F, 11F, 17.5F, 25F, 33F, 41.5F, 50.5F, 59.5F, 67F, 73.5F, 80.25F, 85.5F, 90F };
    final float                constAB         = 0.03866613F;
    final float                constAC         = 0.1832059F;
    final float                constOA         = 0.0391989F;
    final float                constOB         = 0.05015091F;
    final float                constAC2        = 0.1882421F;
    final float                constBD2        = 0.1882416F;
    final float                constCD2        = 0.08457017F;
    private boolean            bNeedSetUp;
    private boolean            bAiming;
    private Hook               ObserverHook;
    private float              CurrentYaw;
    private Gun                gun;
    private int                countBullets;

    static {
        Class class1 = CockpitHE_177A3_BGunner.class;
        Property.set(class1, "aiTuretNum", 2);
        Property.set(class1, "weaponControlNum", 12);
        Property.set(class1, "astatePilotIndx", 3);
//		Property.set(class1, "normZN", 3.2F);
        Property.set(class1, "normZN", 0.6F); // Ensure Torpedo & large external bomb visibility
    }
}
