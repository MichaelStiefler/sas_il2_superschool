package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class CockpitMBR_2AM34 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitMBR_2AM34.this.setTmp = CockpitMBR_2AM34.this.setOld;
            CockpitMBR_2AM34.this.setOld = CockpitMBR_2AM34.this.setNew;
            CockpitMBR_2AM34.this.setNew = CockpitMBR_2AM34.this.setTmp;
            CockpitMBR_2AM34.this.setNew.pictAiler = 0.85F * CockpitMBR_2AM34.this.setOld.pictAiler + 0.15F * CockpitMBR_2AM34.this.cvt(CockpitMBR_2AM34.this.fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F);
            CockpitMBR_2AM34.this.setNew.pictElev = 0.85F * CockpitMBR_2AM34.this.setOld.pictElev + 0.15F * CockpitMBR_2AM34.this.cvt(CockpitMBR_2AM34.this.fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F);
            CockpitMBR_2AM34.this.setNew.pictRudd = 0.85F * CockpitMBR_2AM34.this.setOld.pictRudd + 0.15F * CockpitMBR_2AM34.this.cvt(CockpitMBR_2AM34.this.fm.CT.getRudder(), -1F, 1.0F, -1F, 1.0F);
            CockpitMBR_2AM34.this.setNew.throttle = 0.9F * CockpitMBR_2AM34.this.setOld.throttle + 0.1F * CockpitMBR_2AM34.this.fm.EI.engines[0].getControlThrottle();
            CockpitMBR_2AM34.this.setNew.mix = 0.8F * CockpitMBR_2AM34.this.setOld.mix + 0.2F * CockpitMBR_2AM34.this.fm.EI.engines[0].getControlMix();
            if (CockpitMBR_2AM34.this.fm.EI.engines[0].getStage() == 1 || CockpitMBR_2AM34.this.fm.EI.engines[0].getStage() == 2) {
                if (CockpitMBR_2AM34.this.setNew.Airstartr < 1.0F) CockpitMBR_2AM34.this.setNew.Airstartr = CockpitMBR_2AM34.this.setOld.Airstartr + 0.1F;
                if (CockpitMBR_2AM34.this.setNew.Airstartr > 1.0F) CockpitMBR_2AM34.this.setNew.Airstartr = 1.0F;
            } else {
                if (CockpitMBR_2AM34.this.setNew.Airstartr > 0.0F) CockpitMBR_2AM34.this.setNew.Airstartr = CockpitMBR_2AM34.this.setOld.Airstartr - 0.1F;
                if (CockpitMBR_2AM34.this.setNew.Airstartr < 0.0F) CockpitMBR_2AM34.this.setNew.Airstartr = 0.0F;
            }
            CockpitMBR_2AM34.this.setNew.azimuth.setDeg(CockpitMBR_2AM34.this.setOld.azimuth.getDeg(1.0F), CockpitMBR_2AM34.this.fm.Or.azimut());
            if (CockpitMBR_2AM34.this.useRealisticNavigationInstruments()) {
                CockpitMBR_2AM34.this.setNew.waypointAzimuth.setDeg(CockpitMBR_2AM34.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitMBR_2AM34.this.fm.Or.azimut() + 90F);
                CockpitMBR_2AM34.this.setNew.beaconDirection.setDeg(CockpitMBR_2AM34.this.setOld.beaconDirection.getDeg(1.0F), CockpitMBR_2AM34.this.getBeaconDirection());
            } else {
                CockpitMBR_2AM34.this.setNew.waypointAzimuth.setDeg(CockpitMBR_2AM34.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitMBR_2AM34.this.waypointAzimuth(1.0F));
                CockpitMBR_2AM34.this.setNew.beaconDirection.setDeg(CockpitMBR_2AM34.this.setOld.beaconDirection.getDeg(1.0F), CockpitMBR_2AM34.this.waypointAzimuth(1.0F) - CockpitMBR_2AM34.this.fm.Or.azimut() - 90F);
            }
            CockpitMBR_2AM34.this.w.set(CockpitMBR_2AM34.this.fm.getW());
            CockpitMBR_2AM34.this.fm.Or.transform(CockpitMBR_2AM34.this.w);
            CockpitMBR_2AM34.this.setNew.turn = (12F * CockpitMBR_2AM34.this.setOld.turn + CockpitMBR_2AM34.this.w.z) / 13F;
            CockpitMBR_2AM34.this.setNew.altimeter = CockpitMBR_2AM34.this.fm.getAltitude();
            CockpitMBR_2AM34.this.setNew.manifold = 0.8F * CockpitMBR_2AM34.this.setOld.manifold + 0.2F * CockpitMBR_2AM34.this.fm.EI.engines[0].getManifoldPressure() * 76F;
            CockpitMBR_2AM34.this.setNew.vspeed = (99F * CockpitMBR_2AM34.this.setOld.vspeed + CockpitMBR_2AM34.this.fm.getVertSpeed()) / 100F;
            if (CockpitMBR_2AM34.this.fm.EI.engines[0].getRPM() > 200F) {
                if (CockpitMBR_2AM34.this.fm.M.fuel > 502.5F) CockpitMBR_2AM34.this.setNew.fuelPressure = 0.9F * CockpitMBR_2AM34.this.setOld.fuelPressure + 0.03F;
                else if (CockpitMBR_2AM34.this.fm.M.fuel > 1.0F) CockpitMBR_2AM34.this.setNew.fuelPressure = 0.9F * CockpitMBR_2AM34.this.setOld.fuelPressure + 0.035F;
            } else CockpitMBR_2AM34.this.setNew.fuelPressure = 0.9F * CockpitMBR_2AM34.this.setOld.fuelPressure;
            CockpitMBR_2AM34.this.setNew.fuelPressureV = 0.0F;
            if (CockpitMBR_2AM34.this.fm.getSpeed() > 5F) CockpitMBR_2AM34.this.setNew.fuelPressureV = 0.9F * CockpitMBR_2AM34.this.setOld.fuelPressureV + 0.1F * CockpitMBR_2AM34.this.setNew.fuelPressure + 0.001F * World.Rnd().nextFloat();
            else CockpitMBR_2AM34.this.setNew.fuelPressureV = 0.9F * CockpitMBR_2AM34.this.setOld.fuelPressureV;
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float         pictElev;
        float         pictAiler;
        float         pictRudd;
        float         throttle;
        float         mix;
        float         Airstartr;
        float         turn;
        float         altimeter;
        private float manifold;
        float         vspeed;
        float         fuelPressure;
        float         fuelPressureV;
        AnglesFork    waypointAzimuth;
        AnglesFork    beaconDirection;
        AnglesFork    azimuth;

        private Variables() {
            this.manifold = 0.0F;
            this.fuelPressure = 0.0F;
            this.fuelPressureV = 0.0F;
            this.waypointAzimuth = new AnglesFork();
            this.beaconDirection = new AnglesFork();
            this.azimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitMBR_2AM34() {
        super("3DO/Cockpit/MBR-2/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.oldStyle = false;
        this.ac = null;
        this.ac = (MBR_2xyz) this.aircraft();
        this.setNightMats(false);
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMP01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMP01", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMP02");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMP02", this.light2);
        AircraftLH.printCompassHeading = true;
        if (Mission.getMissionDate(false) < 0x12853c5) this.oldStyle = true;
        else {
            this.oldStyle = false;
            this.mesh.materialReplace("panel512", "panel512_1942");
        }
        this.mesh.chunkVisible("Prib_41", this.oldStyle);
        this.mesh.chunkVisible("Z_ND_Clock_H41", this.oldStyle);
        this.mesh.chunkVisible("Z_ND_Clock_M41", this.oldStyle);
        this.mesh.chunkVisible("Z_ND_Clock_S41", this.oldStyle);
        this.mesh.chunkVisible("Prib_42", !this.oldStyle);
        this.mesh.chunkVisible("Z_ND_Clock_H42", !this.oldStyle);
        this.mesh.chunkVisible("Z_ND_Clock_M42", !this.oldStyle);
        this.mesh.chunkVisible("Z_ND_Clock_S42", !this.oldStyle);
        this.mesh.chunkVisible("Z_ND_RPK", !this.oldStyle);
        if (this.aircraft().thisWeaponsName.endsWith("RS82")) {
            boolean flag = this.CameraHookReplace("CAMERAAIM", "CAMERA_RS");
            if (flag) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setTubeSight(true);
            }
        }
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public boolean CameraHookReplace(String s, String s1) {
        if (s.equals("CAMERA") || s.equals("CAMERAAIM") || s.equals("CAMERAUP")) try {
            HookNamed hooknamed = new HookNamed(this.mesh, s1);
            Loc loc = new Loc();
            loc.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            if (this.cameraCenter != null && s.equals("CAMERA")) {
                loc.get(this.cameraCenter);
//					this.setCameraOffset();
                return true;
            }
            Point3d cameraAim = (Point3d) Reflection.getValue(this, "cameraAim");
            if (cameraAim != null && s.equals("CAMERAAIM")) {
                loc.get(cameraAim);
                return true;
            }
            Point3d cameraUp = (Point3d) Reflection.getValue(this, "cameraUp");
            if (cameraUp != null && s.equals("CAMERAUP")) {
                loc.get(cameraUp);
                return true;
            }
        } catch (Exception exception) {
            return false;
        }
        return false;
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            if (!this.ac.blisterRemoved) this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Blister_D0", false);
            this.aircraft().hierMesh().chunkVisible("BlisterW_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.ac.blisterRemoved) this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        this.aircraft().hierMesh().chunkVisible("Blister_D0", true);
        this.aircraft().hierMesh().chunkVisible("BlisterW_D0", true);
        super.doFocusLeave();
    }

    private float getOilPressure(int i, int j, int k, float f, float f1) {
        float f2 = this.cvt(this.fm.EI.engines[i].getRPM(), j, 1.1F * k, 0.0F, 0.6875F);
        float f3 = 0.0F;
        float f4 = 0.0F;
        if (this.fm.EI.engines[i].getRPM() > 0.5F * j) {
            f3 = this.cvt(this.fm.EI.engines[i].tOilIn, 0.0F, 2.0F * f, 0.375F, -0.375F);
            f4 = 0.375F;
        }
        float f5 = this.cvt(f2 + f3 + f4, 0.0F, 1.0F, 0.0F, f1);
        return f5;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
            this.bNeedSetUp = false;
        }
        if (((MBR_2xyz) this.aircraft()).bChangedPit) {
            this.reflectPlaneToModel();
            ((MBR_2xyz) this.aircraft()).bChangedPit = false;
        }
        float f1 = 29.422F * this.setNew.pictAiler;
        float f2 = 10F * this.setNew.pictElev;
        this.mesh.chunkSetAngles("Wheel_Column", f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Wheel_L", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Wheel_R", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("WheelTros_down", 0.0F, -f2, 0.0F);
        this.mesh.chunkSetAngles("WheelTros_up", 0.0F, -f2, 0.0F);
        float f3 = this.cvt(-this.setNew.pictAiler, -0.99F, 0.99F, -0.019F, 0.019F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = f3;
        this.mesh.chunkSetLocate("WheelCh_L01", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -f3;
        this.mesh.chunkSetLocate("WheelCh_L02", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = f3;
        this.mesh.chunkSetLocate("WheelCh_R01", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -f3;
        this.mesh.chunkSetLocate("WheelCh_R02", Cockpit.xyz, Cockpit.ypr);
        float f4 = 12F * this.setNew.pictRudd;
        this.mesh.chunkSetAngles("Z_Pedal_L_tube", f4, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_R_tube", -f4, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_L_tros", 0.0F, -f4, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_R_tros", 0.0F, f4, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_L_plate", -1.7F * f4, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_R_plate", 1.7F * f4, 0.0F, 0.0F);
        if (this.ac.tiltCanopyOpened) {
            this.resetYPRmodifier();
            Cockpit.xyz[1] = this.cvt(this.ac.canopyF, 0.01F, 0.99F, 0.0F, 0.02F);
            Cockpit.ypr[0] = -this.ac.canopyF * 179F;
            this.mesh.chunkSetLocate("Z_Canopy", Cockpit.xyz, Cockpit.ypr);
        } else {
            this.mesh.chunkSetAngles("Z_Canopy", 0.0F, 0.0F, 0.0F);
            this.resetYPRmodifier();
            Cockpit.xyz[1] = this.cvt(this.ac.canopyF, 0.01F, 0.99F, 0.0F, 0.21F);
            this.mesh.chunkSetLocate("Z_Window", Cockpit.xyz, Cockpit.ypr);
        }
        float f5 = -50F * this.setNew.throttle;
        this.mesh.chunkSetAngles("Z_Throtle", f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle_tros", 0.0F, 0.0F, -f5);
        float f6 = -37.5F * this.setNew.mix;
        this.mesh.chunkSetAngles("Z_Mix", f6, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix_tros", 0.0F, 0.0F, -f6);
        float f7 = this.cvt(this.fm.EI.engines[0].getRPM(), 900F, 2000F, 0.0F, -30F);
        this.mesh.chunkSetAngles("Z_Ignition", f7, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ignition_tros", 0.0F, 0.0F, -f7);
        this.mesh.chunkSetAngles("Z_Flaps", 45F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        float f8 = 55F * this.fm.EI.engines[0].getControlRadiator();
        this.mesh.chunkSetAngles("Z_Radiator", f8, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiator_tros", 0.0F, 0.0F, -f8);
        float f9 = this.cvt(this.fm.EI.engines[0].getExtinguishers(), 0.0F, 11F, 0.0F, -180F);
        this.mesh.chunkSetAngles("Z_FireCran", f9, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FireCran_tros", 0.0F, 0.0F, -f9);
        this.mesh.chunkSetAngles("Z_Stabil_trim", this.cvt(this.fm.CT.getTrimElevatorControl(), -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_StabTrim", this.cvt(this.fm.CT.getTrimElevatorControl(), -0.5F, 0.5F, -30F, 28F), 0.0F, 0.0F);
        if (this.fm.AS.externalStoresDropped) this.mesh.chunkSetAngles("Z_EmrgBombDrop", -90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Air_Press02", 153F, 0.0F, 0.0F);
        if (this.ac.airstartr) this.mesh.chunkSetAngles("Z_Airstartr01", -90F * this.setNew.Airstartr, 0.0F, 0.0F);
        else this.mesh.chunkSetAngles("Z_Airstartr02", -90F * this.setNew.Airstartr, 0.0F, 0.0F);
        if (this.fm.EI.engines[0].getStage() == 1 || this.fm.EI.engines[0].getStage() == 2) {
            if (this.ac.airstartr) this.mesh.chunkSetAngles("Z_Air_Press01", -245F * this.setNew.Airstartr + 5F * World.Rnd().nextFloat(), 0.0F, 0.0F);
            if (!this.ac.airstartr) this.mesh.chunkSetAngles("Z_Air_Press03", -215F * this.setNew.Airstartr + 5F * World.Rnd().nextFloat(), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Air_Press01", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Air_Press03", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Magneto_SW", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.017F, -0.017F);
        Cockpit.ypr[0] = this.fm.Or.getKren();
        this.mesh.chunkSetLocate("Z_ND_AG", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_ND_AG_Fon", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Air_temp", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -70F, 70F, 52F, -52F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Dirrect", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        float f10 = -this.getBall(8D);
        this.mesh.chunkSetAngles("Z_ND_Pioner_B1", this.cvt(f10, -4F, 4F, -10F, 10F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Pioner_B2", this.cvt(f10, -4F, 4F, -10F, 10F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Pioner", this.cvt(this.setNew.turn, -0.23562F, 0.23562F, -30F, 30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Airspeed", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 450F, 0.0F, 9F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_alt_m", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_alt_km", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Boost", this.cvt(this.interp(this.setNew.manifold, this.setOld.manifold, f), 30F, 120F, 0.0F, -298F), 0.0F, 0.0F);
        if (this.oldStyle) {
            this.mesh.chunkSetAngles("Z_ND_Clock_H41", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_Clock_M41", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_Clock_S41", -this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_ND_Clock_H42", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_Clock_M42", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_Clock_S42", -this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_RPK", -this.cvt(this.setNew.beaconDirection.getDeg(1.0F), -30F, 30F, -45F, 45F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_ND_FuelPM", this.cvt(this.setNew.fuelPressure, 0.0F, 1.0F, 0.0F, -289F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_FuelP", this.cvt(this.setNew.fuelPressureV, 0.0F, 1.0F, 0.0F, -290F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_OilT_OUT", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 110F, 0.0F, 7F), oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_OilT_IN", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilIn, 40F, 110F, 0.0F, 7F), oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_OilPress", this.cvt(this.getOilPressure(0, 400, 1950, 75F, 3.5F), 0.0F, 15F, 0.0F, -267.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_OilPressM", this.cvt(this.getOilPressure(0, 400, 1950, 75F, 10F), 0.0F, 15F, 0.0F, -267.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_RPM", -this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 268F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Variom", this.cvt(this.setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F, 0.0F);
        float f11 = this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 1700F, 0.0F, 8F);
        if (this.fm.AS.bLandingLightOn) f11 -= 2.5F;
        if (this.fm.AS.bNavLightsOn) f11 -= 1.5F;
        this.mesh.chunkSetAngles("Z_ND_Voltmetr", -f11, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_WaterT_IN", this.cvt(this.fm.EI.engines[0].tWaterOut * (this.fm.EI.engines[0].tOilIn / this.fm.EI.engines[0].tOilOut), 0.0F, 120F, 0.0F, -112F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_WaterT_OUT", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, -112F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_K5_Arr2", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        MBR_2xyz mbr_2xyz = (MBR_2xyz) this.aircraft();
        this.mesh.chunkSetAngles("Z_ND_K5_Arr1", -mbr_2xyz.headingBug, 0.0F, 0.0F);
        if (this.ac.bRSArmed) this.mesh.chunkVisible("Sight", true);
        else this.mesh.chunkVisible("Sight", false);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("xGlassDm2", true);
            this.mesh.chunkVisible("xHullDm2", true);
            this.mesh.chunkVisible("Prib_5", false);
            this.mesh.chunkVisible("DPrib_5", true);
            this.mesh.chunkVisible("Z_Air_Press01", false);
            this.mesh.chunkVisible("Z_Air_Press02", false);
            this.mesh.chunkVisible("Z_Air_Press03", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Prib_3", false);
            this.mesh.chunkVisible("DPrib_3", true);
            this.mesh.chunkVisible("xGlassDm3", true);
            this.mesh.chunkVisible("xHullDm1", true);
            this.mesh.chunkVisible("Z_ND_AG", false);
            this.mesh.chunkVisible("Z_ND_FuelP", false);
            this.mesh.chunkVisible("Z_ND_OilPressM", false);
            this.mesh.chunkVisible("Z_ND_OilT_OUT", false);
            this.mesh.chunkVisible("Z_ND_WaterT_IN", false);
            this.mesh.chunkVisible("Z_ND_Air_temp", false);
            if (World.Rnd().nextFloat() < 0.75F) if (this.oldStyle) {
                this.mesh.chunkVisible("Prib_41", false);
                this.mesh.chunkVisible("DPrib_41", true);
                this.mesh.chunkVisible("Z_ND_Clock_H41", false);
                this.mesh.chunkVisible("Z_ND_Clock_M41", false);
                this.mesh.chunkVisible("Z_ND_Clock_S41", false);
            } else {
                this.mesh.chunkVisible("Prib_42", false);
                this.mesh.chunkVisible("DPrib_42", true);
                this.mesh.chunkVisible("Z_ND_Clock_H42", false);
                this.mesh.chunkVisible("Z_ND_Clock_M42", false);
                this.mesh.chunkVisible("Z_ND_Clock_S42", false);
                this.mesh.chunkVisible("Z_ND_RPK", false);
            }
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Prib_1", false);
            this.mesh.chunkVisible("DPrib_1", true);
            this.mesh.chunkVisible("xGlassDm1", true);
            if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xGlassDm5", true);
            this.mesh.chunkVisible("xHullDm4", true);
            if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xHullDm5", true);
            this.mesh.chunkVisible("Z_ND_Voltmetr", false);
            this.mesh.chunkVisible("Z_ND_Alt_KM", false);
            this.mesh.chunkVisible("Z_ND_Alt_M", false);
            this.mesh.chunkVisible("Z_ND_Pioner", false);
            this.mesh.chunkVisible("Z_ND_Pioner_B2", false);
            this.mesh.chunkVisible("Z_ND_Airspeed", false);
            this.mesh.chunkVisible("Z_ND_RPM", false);
            this.mesh.chunkVisible("Z_ND_FuelPM", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Prib_2", false);
            this.mesh.chunkVisible("DPrib_2", true);
            this.mesh.chunkVisible("xGlassDm4", true);
            if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xGlassDm5", true);
            this.mesh.chunkVisible("xHullDm3", true);
            if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xHullDm5", true);
            this.mesh.chunkVisible("Z_ND_Variom", false);
            this.mesh.chunkVisible("Z_ND_OilPress", false);
            this.mesh.chunkVisible("Z_ND_Boost", false);
            this.mesh.chunkVisible("Z_ND_WaterT_OUT", false);
            this.mesh.chunkVisible("Z_ND_OilT_IN", false);
            this.mesh.chunkVisible("Z_ND_Airspeed", false);
            this.mesh.chunkVisible("Z_ND_RPM", false);
            this.mesh.chunkVisible("Z_ND_FuelPM", false);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
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
//		System.out.println("thisWeaponsName=" + this.aircraft().thisWeaponsName);
        if (this.aircraft().thisWeaponsName.endsWith("Schlong")) this.mesh.chunkVisible("Schlong", true);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.5F, 0.6F);
            this.light2.light.setEmit(0.5F, 0.6F);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
        }
        this.setNightMats(false);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private boolean            bNeedSetUp;
    private boolean            oldStyle;
    private MBR_2xyz           ac;
    private static final float speedometerScale[] = { 0.0F, 8.745F, 17.49F, 54.38F, 103.28F, 159.8F, 217.47F, 273.84F, 328.7F, 382.4F };
    private static final float oilTempScale[]     = { 0.0F, 24.31F, 54.08F, 88.93F, 127.8F, 175.36F, 229.43F, 304.07F };

    static {
        Property.set(CockpitMBR_2AM34.class, "normZNs", new float[] { 0.8F, 0.8F, 0.95F, 1.27F });
    }
}
