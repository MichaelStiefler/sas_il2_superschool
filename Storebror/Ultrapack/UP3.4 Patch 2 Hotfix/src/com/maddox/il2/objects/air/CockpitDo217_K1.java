package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class CockpitDo217_K1 extends CockpitPilot {
    private class Variables {

        float      dimPos;
        float      altimeter;
        float      throttlel;
        float      throttler;
        float      vspeed;
        float      cons;
        float      elevTrim;
        float      rudderTrim;
        float      ailTrim;
        float      propL;
        float      propR;
        AnglesFork radioCompassAzimuth;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      beaconDirection;
        float      beaconRange;

        private Variables() {
            this.dimPos = 0.0F;
            this.radioCompassAzimuth = new AnglesFork();
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitDo217_K1.this.setTmp = CockpitDo217_K1.this.setOld;
            CockpitDo217_K1.this.setOld = CockpitDo217_K1.this.setNew;
            CockpitDo217_K1.this.setNew = CockpitDo217_K1.this.setTmp;
            if (CockpitDo217_K1.this.cockpitDimControl) {
                if (CockpitDo217_K1.this.setNew.dimPos < 1.0F) {
                    CockpitDo217_K1.this.setNew.dimPos = CockpitDo217_K1.this.setOld.dimPos + 0.03F;
                }
            } else if (CockpitDo217_K1.this.setNew.dimPos > 0.0F) {
                CockpitDo217_K1.this.setNew.dimPos = CockpitDo217_K1.this.setOld.dimPos - 0.03F;
            }
            CockpitDo217_K1.this.setNew.altimeter = CockpitDo217_K1.this.fm.getAltitude();
            CockpitDo217_K1.this.setNew.throttlel = ((10F * CockpitDo217_K1.this.setOld.throttlel) + CockpitDo217_K1.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitDo217_K1.this.setNew.throttler = ((10F * CockpitDo217_K1.this.setOld.throttler) + CockpitDo217_K1.this.fm.EI.engines[1].getControlThrottle()) / 11F;
            CockpitDo217_K1.this.setNew.vspeed = ((499F * CockpitDo217_K1.this.setOld.vspeed) + CockpitDo217_K1.this.fm.getVertSpeed()) / 500F;
            CockpitDo217_K1.this.setNew.elevTrim = (0.85F * CockpitDo217_K1.this.setOld.elevTrim) + (CockpitDo217_K1.this.fm.CT.getTrimElevatorControl() * 0.15F);
            CockpitDo217_K1.this.setNew.rudderTrim = (0.85F * CockpitDo217_K1.this.setOld.rudderTrim) + (CockpitDo217_K1.this.fm.CT.getTrimRudderControl() * 0.15F);
            CockpitDo217_K1.this.setNew.ailTrim = (0.85F * CockpitDo217_K1.this.setOld.ailTrim) + (CockpitDo217_K1.this.fm.CT.getTrimAileronControl() * 0.15F);
            CockpitDo217_K1.this.setNew.propL = (0.85F * CockpitDo217_K1.this.setOld.propL) + (CockpitDo217_K1.this.fm.EI.engines[0].getControlProp() * 0.15F);
            CockpitDo217_K1.this.setNew.propR = (0.85F * CockpitDo217_K1.this.setOld.propR) + (CockpitDo217_K1.this.fm.EI.engines[1].getControlProp() * 0.15F);
            float f = CockpitDo217_K1.this.prevFuel - CockpitDo217_K1.this.fm.M.fuel;
            CockpitDo217_K1.this.prevFuel = CockpitDo217_K1.this.fm.M.fuel;
            f /= 0.72F;
            f /= Time.tickLenFs();
            f *= 3600F;
            CockpitDo217_K1.this.setNew.cons = (0.91F * CockpitDo217_K1.this.setOld.cons) + (0.09F * f);
            float f1 = CockpitDo217_K1.this.waypointAzimuth();
            if (CockpitDo217_K1.this.useRealisticNavigationInstruments()) {
                CockpitDo217_K1.this.setNew.waypointAzimuth.setDeg(f1 - 90F);
                CockpitDo217_K1.this.setOld.waypointAzimuth.setDeg(f1 - 90F);
                CockpitDo217_K1.this.setNew.radioCompassAzimuth.setDeg(CockpitDo217_K1.this.setOld.radioCompassAzimuth.getDeg(0.02F), CockpitDo217_K1.this.radioCompassAzimuthInvertMinus() - CockpitDo217_K1.this.setOld.azimuth.getDeg(1.0F) - 90F);
            } else {
                CockpitDo217_K1.this.setNew.waypointAzimuth.setDeg(CockpitDo217_K1.this.setOld.waypointAzimuth.getDeg(0.1F), f1 - CockpitDo217_K1.this.setOld.azimuth.getDeg(1.0F));
            }
            CockpitDo217_K1.this.setNew.azimuth.setDeg(CockpitDo217_K1.this.setOld.azimuth.getDeg(1.0F), CockpitDo217_K1.this.fm.Or.azimut());
            CockpitDo217_K1.this.setNew.beaconDirection = ((10F * CockpitDo217_K1.this.setOld.beaconDirection) + CockpitDo217_K1.this.getBeaconDirection()) / 11F;
            CockpitDo217_K1.this.setNew.beaconRange = ((10F * CockpitDo217_K1.this.setOld.beaconRange) + CockpitDo217_K1.this.getBeaconRange()) / 11F;
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((Do217) this.fm.actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Interior1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Hmask1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Hmask2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
            this.aircraft().hierMesh().chunkVisible("Hmask3_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_D0", false);
            this.aircraft().hierMesh().chunkVisible("Hmask4_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_D1", false);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
            if (this.aircraft() instanceof Do217_K1) {
                this.mesh.chunkVisible("k2-Box", false);
                this.mesh.chunkVisible("k2-Cable", false);
                this.mesh.chunkVisible("k2-cushion", false);
                this.mesh.chunkVisible("k2-FuG203", false);
                this.mesh.chunkVisible("k2-gunsight", false);
            }
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        ((Do217) this.fm.actor).bPitUnfocused = true;
        this.aircraft().hierMesh().chunkVisible("Interior1_D0", true);
        if (!this.fm.AS.isPilotParatrooper(0)) {
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", !this.fm.AS.isPilotDead(0));
            this.aircraft().hierMesh().chunkVisible("Head1_D0", !this.fm.AS.isPilotDead(0));
            this.aircraft().hierMesh().chunkVisible("Pilot1_D1", this.fm.AS.isPilotDead(0));
        }
        if (!this.fm.AS.isPilotParatrooper(1)) {
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", !this.fm.AS.isPilotDead(1));
            this.aircraft().hierMesh().chunkVisible("Pilot2_D1", this.fm.AS.isPilotDead(1));
        }
        if (!this.fm.AS.isPilotParatrooper(2)) {
            this.aircraft().hierMesh().chunkVisible("Pilot3_D0", !this.fm.AS.isPilotDead(2));
            this.aircraft().hierMesh().chunkVisible("Pilot3_D1", this.fm.AS.isPilotDead(2));
        }
        if (!this.fm.AS.isPilotParatrooper(3)) {
            this.aircraft().hierMesh().chunkVisible("Pilot4_D0", !this.fm.AS.isPilotDead(3));
            this.aircraft().hierMesh().chunkVisible("Pilot4_D1", this.fm.AS.isPilotDead(3));
        }
        this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
        if (this.aircraft() instanceof Do217_K1) {
            this.mesh.chunkVisible("k2-Box", true);
            this.mesh.chunkVisible("k2-Cable", true);
            this.mesh.chunkVisible("k2-cushion", true);
            this.mesh.chunkVisible("k2-FuG203", true);
            this.mesh.chunkVisible("k2-gunsight", true);
        }
        super.doFocusLeave();
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(20F);
    }

    public CockpitDo217_K1() {
        super("3DO/Cockpit/Do217K1/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.prevFuel = 0.0F;
        this.Pn = new Point3d();
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(218F, 143F, 128F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.cockpitNightMats = (new String[] { "Peil1", "Peil2", "Instrument1", "Instrument2", "Instrument4", "Instrument5", "Instrument6", "Instrument7", "Instrument8", "Instrument9", "Needles" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
        this.w = new Vector3f();
        this.buzzerFX = this.aircraft().newSound("models.buzzthru", false);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void reflectWorldToInstruments(float f) {
        AircraftLH.printCompassHeading = true;
        this.mesh.chunkSetAngles("ZWheel", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 40F, 0.0F);
        this.mesh.chunkSetAngles("ZColumn", 0.0F, -7F - ((this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F), 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, -this.fm.CT.getRudder() * 10F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 10F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, 0.08F, -0.08F);
        this.mesh.chunkSetLocate("PedalRbar", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -0.08F, 0.08F);
        this.mesh.chunkSetLocate("PedalLbar", Cockpit.xyz, Cockpit.ypr);
        float f1 = this.interp(this.setNew.altimeter, this.setOld.altimeter, f);
        this.mesh.chunkSetAngles("z_HeightKm", 0.0F, this.cvt(f1, 0.0F, 10000F, 0.0F, 257F), 0.0F);
        this.mesh.chunkSetAngles("z_Height", 0.0F, this.cvt(f1, 0.0F, 20000F, 0.0F, 7200F), 0.0F);
        this.mesh.chunkSetAngles("Z_Alt1", 0.0F, this.cvt(f1, 0.0F, 6000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Alt1Set", 0.0F, (((Do217) this.aircraft()).fSightCurAltitude * 360F) / 6000F, 0.0F);
        this.Pn.set(this.fm.Loc);
        this.Pn.z = f1 - Engine.cur.land.HQ(this.Pn.x, this.Pn.y);
        this.mesh.chunkSetAngles("z_AFN101", 0.0F, this.cvt((float) this.Pn.z, 0.0F, 750F, 0.0F, 223F), 0.0F);
        this.mesh.chunkSetAngles("z_Speed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 50F, 800F, 0.0F, 15F), CockpitDo217_K1.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("z_VelX", 0.0F, this.floatindex(this.cvt(this.fm.getSpeedKMH(), 50F, 800F, 0.0F, 15F), CockpitDo217_K1.speedometerScale), 0.0F);
        float f2 = this.fm.EI.engines[0].getRPM();
        float f3 = this.fm.EI.engines[1].getRPM();
        this.mesh.chunkSetAngles("z_RPM1", 0.0F, this.cvt(f2, 600F, 3600F, -172F, 151F), 0.0F);
        this.mesh.chunkSetAngles("z_RPM2", 0.0F, this.cvt(f3, 600F, 3600F, -172F, 151F), 0.0F);
        this.mesh.chunkSetAngles("z_ATA1", 0.0F, this.pictManf1 = (0.9F * this.pictManf1) + (0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, -162.5F, 162.5F)), 0.0F);
        this.mesh.chunkSetAngles("z_ATA2", 0.0F, this.pictManf2 = (0.9F * this.pictManf2) + (0.1F * this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, -162.5F, 162.5F)), 0.0F);
        this.mesh.chunkSetAngles("Z_Hours", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minutes", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("z_ThrottleL", 0.0F, -25F * this.interp(this.setNew.throttlel, this.setOld.throttlel, f), 0.0F);
        this.mesh.chunkSetAngles("z_ThrottleR", 0.0F, -25F * this.interp(this.setNew.throttler, this.setOld.throttler, f), 0.0F);
        float f4 = this.setNew.vspeed * 9F;
        this.mesh.chunkSetAngles("Z_Variometer", 0.0F, f4, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Stick", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        float f5 = this.cvt(this.getBall(6D), -6F, 6F, -0.02F, 0.02F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -f5;
        this.mesh.chunkSetLocate("Z_Ball", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Z_Ball01", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Z_Ball02", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("z_AH1", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.011F, -0.015F);
        this.mesh.chunkSetLocate("Z_Horizon", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("z_ElevTrim", 0.0F, -this.cvt(this.interp(this.setNew.elevTrim, this.setOld.elevTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
        this.mesh.chunkSetAngles("z_RudderTrim", 0.0F, this.cvt(this.interp(this.setNew.rudderTrim, this.setOld.rudderTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
        this.mesh.chunkSetAngles("z_AileronTrim", 0.0F, this.cvt(this.interp(this.setNew.ailTrim, this.setOld.ailTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
        float f6 = this.setNew.cons;
        float f7 = (f6 * f2) / (f2 + f3);
        float f8 = (f6 * f3) / (f2 + f3);
        this.mesh.chunkSetAngles("z_FuelCons1", 0.0F, this.cvt(f7, 0.0F, 600F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("z_FuelCons2", 0.0F, this.cvt(f8, 0.0F, 600F, 0.0F, 360F), 0.0F);
        float f9 = this.fm.M.fuel / 0.72F;
        this.mesh.chunkSetAngles("z_Fuel3", 0.0F, this.cvt(f9, 0.0F, 1100F, 0.0F, 69F), 0.0F);
        this.mesh.chunkSetAngles("z_Fuel1", 0.0F, this.cvt(f9, 1100F, 2670F, 0.0F, 84F), 0.0F);
        this.mesh.chunkSetAngles("z_Fuel2", 0.0F, this.cvt(f9, 1100F, 2670F, 0.0F, 84F), 0.0F);
        this.mesh.chunkSetAngles("z_OilPres1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 15F, 0.0F, -135F), 0.0F);
        this.mesh.chunkSetAngles("z_OilPres2", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 15F, 0.0F, -135F), 0.0F);
        this.mesh.chunkSetAngles("z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F);
        this.mesh.chunkSetAngles("z_FuelPres2", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F);
        this.mesh.chunkSetAngles("Z_TempCylL", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
        this.mesh.chunkSetAngles("Z_TempCylR", 0.0F, this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
        this.mesh.chunkSetAngles("z_OAT", 0.0F, this.floatindex(this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -60F, 40F, 0.0F, 10F), CockpitDo217_K1.OATscale), 0.0F);
        this.mesh.chunkSetAngles("z_TrimIndicator", 0.0F, this.cvt(this.interp(this.setNew.elevTrim, this.setOld.elevTrim, f), -0.5F, 0.5F, 40F, -40F), 0.0F);
        this.mesh.chunkSetAngles("PropPitchLeverL", 0.0F, -70F * (1.0F - this.interp(this.setNew.propL, this.setOld.propL, f)), 0.0F);
        this.mesh.chunkSetAngles("PropPitchLeverR", 0.0F, -70F * (1.0F - this.interp(this.setNew.propR, this.setOld.propR, f)), 0.0F);
        this.mesh.chunkVisible("RWL_Ein", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("RWL_Aus", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("RWR_Ein", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("RWR_Aus", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("LK_Ein", this.fm.CT.getFlap() < 0.1F);
        this.mesh.chunkVisible("LK_Start", (this.fm.CT.getFlap() > 0.1F) && (this.fm.CT.getFlap() < 0.5F));
        this.mesh.chunkVisible("LK_Aus", this.fm.CT.getFlap() > 0.5F);
        this.mesh.chunkSetAngles("Z_Turret1A", 0.0F, -this.fm.turret[0].tu[0], 0.0F);
        this.mesh.chunkSetAngles("Z_Turret1B", 0.0F, this.fm.turret[0].tu[1], 0.0F);
        this.mesh.chunkSetAngles("Z_Turret3A", 0.0F, -this.fm.turret[2].tu[0], 0.0F);
        this.mesh.chunkSetAngles("Z_Turret3B", 0.0F, this.fm.turret[2].tu[1], 0.0F);
        this.mesh.chunkSetAngles("Z_Turret5A", 0.0F, -this.fm.turret[4].tu[0], 0.0F);
        this.mesh.chunkSetAngles("Z_Turret5B", 0.0F, this.fm.turret[4].tu[1], 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Z_Compass1", -this.setNew.waypointAzimuth.getDeg(f * 0.1F) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass2", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass4", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass5", this.setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass7", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass8", this.setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zNavP", -(this.setNew.waypointAzimuth.getDeg(f * 0.1F) - this.setNew.azimuth.getDeg(f)), 0.0F, 0.0F);
            float f10 = this.setNew.beaconDirection;
            float f11 = this.setNew.beaconRange;
            this.mesh.chunkSetAngles("z_AFN2a", 0.0F, this.cvt(f10, -40F, 40F, -32F, 32F), 0.0F);
            this.mesh.chunkSetAngles("z_AFN2b", 0.0F, 0.0F, this.cvt(f11, 0.0F, 1.0F, -20F, 15F));
            this.mesh.chunkVisible("AFN1_RED", this.isOnBlindLandingMarker());
        } else {
            this.mesh.chunkSetAngles("Z_Compass1", -this.setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass2", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass4", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass5", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass7", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass8", this.setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zNavP", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
    }

    protected void mydebugcockpit(String s) {
        System.out.println(s);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassHoles1", true);
            this.mesh.chunkVisible("XGlassHoles3", true);
            this.mesh.chunkVisible("Alt-dmg", true);
            this.mesh.chunkVisible("z_Alt", false);
            this.mesh.chunkVisible("Z_Alt1Set", false);
            this.mesh.chunkVisible("RPM-dmg", true);
            this.mesh.chunkVisible("z_RPM1", false);
            this.mesh.chunkVisible("z_RPM2", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassHoles7", true);
            this.mesh.chunkVisible("XGlassHoles4", true);
            this.mesh.chunkVisible("XGlassHoles2", true);
            this.mesh.chunkVisible("ATA-dmg", true);
            this.mesh.chunkVisible("z_ATA", false);
            this.mesh.chunkVisible("Speed-dmg", true);
            this.mesh.chunkVisible("z_Speed", false);
            this.mesh.chunkVisible("FuelCons2-dmg", true);
            this.mesh.chunkVisible("z_FuelCons2", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassHoles5", true);
            this.mesh.chunkVisible("XGlassHoles3", true);
            this.mesh.chunkVisible("OAT-dmg", true);
            this.mesh.chunkVisible("z_OAT", false);
            this.mesh.chunkVisible("Variometer-dmg", true);
            this.mesh.chunkVisible("z_Variometer", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassHoles1", true);
        }
        this.mesh.chunkVisible("XGlassHoles6", true);
        this.mesh.chunkVisible("XGlassHoles4", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassHoles6", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XGlassHoles7", true);
            this.mesh.chunkVisible("XGlassHoles2", true);
            this.mesh.chunkVisible("Oxy-dmg", true);
            this.mesh.chunkVisible("z_Oxy", false);
            this.mesh.chunkVisible("Press-dmg", true);
            this.mesh.chunkVisible("z_OilPres1", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("XGlassHoles5", true);
            this.mesh.chunkVisible("XGlassHoles2", true);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.0032F, 7.2F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private float              pictAiler;
    private float              pictElev;
    private float              prevFuel;
    protected SoundFX          buzzerFX;
    private static final float speedometerScale[] = { 0.0F, 17F, 41F, 66F, 93F, 119F, 139F, 164F, 188F, 214F, 239F, 266F, 292F, 317F, 341F, 372F };
    private static final float OATscale[]         = { 0.0F, 7F, 17F, 27F, 37F, 47F, 56F, 65F, 72F, 80F, 85F };
    private Point3d            Pn;
    private float              pictManf1;
    private float              pictManf2;
    public Vector3f            w;

}
