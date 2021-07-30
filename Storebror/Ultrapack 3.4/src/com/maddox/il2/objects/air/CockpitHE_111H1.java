package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_111H1 extends CockpitPilot {
    private class Variables {

        float      altimeter;
        float      throttlel;
        float      throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float      beaconDirection;
        float      beaconRange;
        float      turn;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitHE_111H1.this.setTmp = CockpitHE_111H1.this.setOld;
            CockpitHE_111H1.this.setOld = CockpitHE_111H1.this.setNew;
            CockpitHE_111H1.this.setNew = CockpitHE_111H1.this.setTmp;
            CockpitHE_111H1.this.setNew.altimeter = CockpitHE_111H1.this.fm.getAltitude();
            CockpitHE_111H1.this.setNew.throttlel = (10F * CockpitHE_111H1.this.setOld.throttlel + CockpitHE_111H1.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitHE_111H1.this.setNew.throttler = (10F * CockpitHE_111H1.this.setOld.throttler + CockpitHE_111H1.this.fm.EI.engines[1].getControlThrottle()) / 11F;
            CockpitHE_111H1.this.w.set(CockpitHE_111H1.this.fm.getW());
            CockpitHE_111H1.this.fm.Or.transform(CockpitHE_111H1.this.w);
            CockpitHE_111H1.this.setNew.turn = (12F * CockpitHE_111H1.this.setOld.turn + CockpitHE_111H1.this.w.z) / 13F;
            CockpitHE_111H1.this.mesh.chunkSetAngles("zTurretA", 0.0F, CockpitHE_111H1.this.fm.turret[0].tu[0], 0.0F);
            CockpitHE_111H1.this.mesh.chunkSetAngles("zTurretB", 0.0F, CockpitHE_111H1.this.fm.turret[0].tu[1], 0.0F);
            float f = CockpitHE_111H1.this.waypointAzimuth();
            if (CockpitHE_111H1.this.useRealisticNavigationInstruments()) {
                CockpitHE_111H1.this.setNew.waypointAzimuth.setDeg(f - 90F);
                CockpitHE_111H1.this.setOld.waypointAzimuth.setDeg(f - 90F);
                CockpitHE_111H1.this.setNew.radioCompassAzimuth.setDeg(CockpitHE_111H1.this.setOld.radioCompassAzimuth.getDeg(0.02F), CockpitHE_111H1.this.radioCompassAzimuthInvertMinus() - CockpitHE_111H1.this.setOld.azimuth.getDeg(1.0F) - 90F);
            } else CockpitHE_111H1.this.setNew.waypointAzimuth.setDeg(CockpitHE_111H1.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitHE_111H1.this.setOld.azimuth.getDeg(1.0F));
            CockpitHE_111H1.this.setNew.azimuth.setDeg(CockpitHE_111H1.this.setOld.azimuth.getDeg(1.0F), CockpitHE_111H1.this.fm.Or.azimut());
            CockpitHE_111H1.this.setNew.vspeed = (499F * CockpitHE_111H1.this.setOld.vspeed + CockpitHE_111H1.this.fm.getVertSpeed()) / 500F;
            CockpitHE_111H1.this.setNew.beaconDirection = (10F * CockpitHE_111H1.this.setOld.beaconDirection + CockpitHE_111H1.this.getBeaconDirection()) / 11F;
            CockpitHE_111H1.this.setNew.beaconRange = (10F * CockpitHE_111H1.this.setOld.beaconRange + CockpitHE_111H1.this.getBeaconRange()) / 11F;
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = false;
            this.bTurrVisible = this.aircraft().hierMesh().isChunkVisible("Turret1C_D0");
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Head1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            this.aircraft().hierMesh().chunkVisible("Window_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        ((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = true;
        this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.bTurrVisible);
        this.aircraft().hierMesh().chunkVisible("Cockpit_D0", this.aircraft().hierMesh().isChunkVisible("Nose_D0") || this.aircraft().hierMesh().isChunkVisible("Nose_D1") || this.aircraft().hierMesh().isChunkVisible("Nose_D2"));
        this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.aircraft().hierMesh().isChunkVisible("Turret1B_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        this.aircraft().hierMesh().chunkVisible("Head1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
        this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
        this.aircraft().hierMesh().chunkVisible("Window_D0", true);
        super.doFocusLeave();
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitHE_111H1() {
        super("3DO/Cockpit/He-111P-4/hier-H1.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.w = new Vector3f();
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(218F, 143F, 128F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.cockpitNightMats = new String[] { "clocks1", "clocks2", "clocks3", "clocks4", "clocks5", "clocks6", "clocks7", "AFN-1" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.fm.isTick(44, 0)) {
            if ((this.fm.AS.astateCockpitState & 8) == 0) {
                this.mesh.chunkVisible("Z_GearLRed1", this.fm.CT.getGear() == 0.0F || this.fm.Gears.isAnyDamaged());
                this.mesh.chunkVisible("Z_GearRRed1", this.fm.CT.getGear() == 0.0F || this.fm.Gears.isAnyDamaged());
                this.mesh.chunkVisible("Z_GearLGreen1", this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear);
                this.mesh.chunkVisible("Z_GearRGreen1", this.fm.CT.getGear() == 1.0F && this.fm.Gears.rgear);
            } else {
                this.mesh.chunkVisible("Z_GearLRed1", false);
                this.mesh.chunkVisible("Z_GearRRed1", false);
                this.mesh.chunkVisible("Z_GearLGreen1", false);
                this.mesh.chunkVisible("Z_GearRGreen1", false);
            }
            if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
                this.mesh.chunkVisible("zFuelWarning1", this.fm.M.fuel < 600F);
                this.mesh.chunkVisible("zFuelWarning2", this.fm.M.fuel < 600F);
            }
        }
        this.mesh.chunkSetAngles("zColumn1", 0.0F, 0.0F, -10F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl));
        this.mesh.chunkSetAngles("zColumn2", 0.0F, 0.0F, -40F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl));
        if (this.fm.CT.getRudder() > 0.0F) {
            this.mesh.chunkSetAngles("zPedalL", 0.0F, 0.0F, -10F * this.fm.CT.getRudder());
            this.mesh.chunkSetAngles("zPedalR", 0.0F, 0.0F, -45F * this.fm.CT.getRudder());
        } else {
            this.mesh.chunkSetAngles("zPedalL", 0.0F, 0.0F, -45F * this.fm.CT.getRudder());
            this.mesh.chunkSetAngles("zPedalR", 0.0F, 0.0F, -10F * this.fm.CT.getRudder());
        }
        this.mesh.chunkSetAngles("zOilFlap1", 0.0F, 0.0F, -50F * this.fm.EI.engines[0].getControlRadiator());
        this.mesh.chunkSetAngles("zOilFlap2", 0.0F, 0.0F, -50F * this.fm.EI.engines[1].getControlRadiator());
        this.mesh.chunkSetAngles("zMix1", 0.0F, 0.0F, -30F * this.fm.EI.engines[0].getControlMix());
        this.mesh.chunkSetAngles("zMix2", 0.0F, 0.0F, -30F * this.fm.EI.engines[1].getControlMix());
        this.mesh.chunkSetAngles("zFlaps1", 0.0F, 0.0F, -45F * this.fm.CT.FlapsControl);
        if (this.fm.EI.engines[0].getControlProp() >= 0.0F) this.mesh.chunkSetAngles("zPitch1", 0.0F, 0.0F, -65F * this.fm.EI.engines[0].getControlProp());
        if (this.fm.EI.engines[1].getControlProp() >= 0.0F) this.mesh.chunkSetAngles("zPitch2", 0.0F, 0.0F, -65F * this.fm.EI.engines[1].getControlProp());
        this.mesh.chunkSetAngles("zThrottle1", 0.0F, 0.0F, -33.6F * this.interp(this.setNew.throttlel, this.setOld.throttlel, f));
        this.mesh.chunkSetAngles("zThrottle2", 0.0F, 0.0F, -33.6F * this.interp(this.setNew.throttler, this.setOld.throttler, f));
        this.mesh.chunkSetAngles("zCompressor1", 0.0F, 0.0F, -25F * this.fm.EI.engines[0].getControlCompressor());
        this.mesh.chunkSetAngles("zCompressor2", 0.0F, 0.0F, -25F * this.fm.EI.engines[1].getControlCompressor());
        this.mesh.chunkSetAngles("zHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSecond", this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAH1", 0.0F, 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("zAH2", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -30F, 30F, -6.5F, 6.5F));
        this.mesh.chunkSetAngles("zTurnBank", this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
        float f1 = this.getBall(4.5D);
        this.mesh.chunkSetAngles("zBall", this.cvt(f1, -4F, 4F, -8F, 8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBall2", this.cvt(f1, -4.5F, 4.5F, -9F, 9F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVSI", this.cvt(this.setNew.vspeed, -10F, 10F, -178F, 178F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 550F, 0.0F, 11F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("zCompass", -this.setNew.azimuth.getDeg(f) + this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zRepeater", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass4", -this.setNew.waypointAzimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass5", this.setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zMagnetic", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zNavP", -(this.setNew.waypointAzimuth.getDeg(f * 0.1F) - this.setNew.azimuth.getDeg(f)), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("zRepeater", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass4", -this.setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zCompass", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zMagnetic", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zNavP", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zRPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 15F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3000F, 0.0F, 15F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost2", this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp1", this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 120F, 30F, 100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp2", this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 120F, 30F, 100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 30F, 100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 120F, 30F, 100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-3", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-4", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-3", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-4", this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zExtT", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 213.09F, 313.09F, -60F, 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel1", this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 2000F, 34.5F, 140F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel2", this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 2000F, 34.5F, 140F), 0.0F, 0.0F);
        float f2 = (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin());
        f2 = (int) (-f2 / 0.2F) * 0.2F;
        this.mesh.chunkSetAngles("zProp1-1", f2 * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zProp1-2", f2 * 5F, 0.0F, 0.0F);
        f2 = (float) Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin());
        f2 = (int) (-f2 / 0.2F) * 0.2F;
        this.mesh.chunkSetAngles("zProp2-1", f2 * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zProp2-2", f2 * 5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFlapsIL", 145F * this.fm.CT.getFlap(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFlapsIR", 145F * this.fm.CT.getFlap(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("AFN1", 0.0F, this.cvt(this.setNew.beaconDirection, -45F, 45F, -14F, 14F), 0.0F);
        this.mesh.chunkSetAngles("AFN2", 0.0F, this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, 26.5F, -26.5F), 0.0F);
        this.mesh.chunkVisible("AFN1_RED", this.isOnBlindLandingMarker());
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("ZHolesL_D1", true);
            this.mesh.chunkVisible("PanelL_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("ZHolesL_D2", true);
            this.mesh.chunkVisible("PanelFloat_D1", true);
            this.mesh.chunkVisible("PanelFloat_D0", false);
            this.mesh.chunkVisible("zProp1-1", false);
            this.mesh.chunkVisible("zProp1-2", false);
            this.mesh.chunkVisible("zProp2-1", false);
            this.mesh.chunkVisible("zProp2-2", false);
            this.mesh.chunkVisible("zFlapsIL", false);
            this.mesh.chunkVisible("zFlapsIR", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("ZHolesR_D1", true);
            this.mesh.chunkVisible("PanelR_D1", true);
            this.mesh.chunkVisible("zFlapsIR", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("ZHolesR_D2", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("ZHolesF_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("PanelT_D1", true);
            this.mesh.chunkVisible("zOFP1-3", false);
            this.mesh.chunkVisible("zOFP1-4", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("zOil_D1", true);
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
    public Vector3f            w;
    private boolean            bTurrVisible;
    private static final float speedometerScale[] = { 0.0F, 6.01F, 18.5F, 43.65F, 75.81F, 113.39F, 152F, 192.26F, 232.24F, 270.67F, 308.39F, 343.38F };
    private static final float rpmScale[]         = { 0.0F, 5F, 9F, 13.8F, 37.27F, 62.32F, 90F, 120.43F, 150.36F, 179F, 211.26F, 242.65F, 271.55F, 296.88F, 320.76F, 344.05F };

    static {
        Property.set(CockpitHE_111H1.class, "normZNs", new float[] { 1.0F, 1.0F, 1.25F, 1.0F });
    }
}
