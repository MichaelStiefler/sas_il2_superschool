package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB24D extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitB24D.this.bNeedSetUp) {
                CockpitB24D.this.bNeedSetUp = false;
                CockpitB24D.this.reflectPlaneMats();
            }
            if (CockpitB24D.this.fm != null) {
                CockpitB24D.this.setTmp = CockpitB24D.this.setOld;
                CockpitB24D.this.setOld = CockpitB24D.this.setNew;
                CockpitB24D.this.setNew = CockpitB24D.this.setTmp;
                CockpitB24D.this.setNew.throttle1 = 0.85F * CockpitB24D.this.setOld.throttle1 + CockpitB24D.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitB24D.this.setNew.throttle2 = 0.85F * CockpitB24D.this.setOld.throttle2 + CockpitB24D.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitB24D.this.setNew.throttle3 = 0.85F * CockpitB24D.this.setOld.throttle3 + CockpitB24D.this.fm.EI.engines[2].getControlThrottle() * 0.15F;
                CockpitB24D.this.setNew.throttle4 = 0.85F * CockpitB24D.this.setOld.throttle4 + CockpitB24D.this.fm.EI.engines[3].getControlThrottle() * 0.15F;
                CockpitB24D.this.setNew.supercharger1 = 0.85F * CockpitB24D.this.setOld.supercharger1 + CockpitB24D.this.fm.EI.engines[0].getControlCompressor() * 0.15F;
                CockpitB24D.this.setNew.supercharger2 = 0.85F * CockpitB24D.this.setOld.supercharger2 + CockpitB24D.this.fm.EI.engines[1].getControlCompressor() * 0.15F;
                CockpitB24D.this.setNew.supercharger3 = 0.85F * CockpitB24D.this.setOld.supercharger3 + CockpitB24D.this.fm.EI.engines[2].getControlCompressor() * 0.15F;
                CockpitB24D.this.setNew.supercharger4 = 0.85F * CockpitB24D.this.setOld.supercharger4 + CockpitB24D.this.fm.EI.engines[3].getControlCompressor() * 0.15F;
                CockpitB24D.this.setNew.mix1 = 0.85F * CockpitB24D.this.setOld.mix1 + CockpitB24D.this.fm.EI.engines[0].getControlMix() * 0.15F;
                CockpitB24D.this.setNew.mix2 = 0.85F * CockpitB24D.this.setOld.mix2 + CockpitB24D.this.fm.EI.engines[1].getControlMix() * 0.15F;
                CockpitB24D.this.setNew.mix3 = 0.85F * CockpitB24D.this.setOld.mix3 + CockpitB24D.this.fm.EI.engines[2].getControlMix() * 0.15F;
                CockpitB24D.this.setNew.mix4 = 0.85F * CockpitB24D.this.setOld.mix4 + CockpitB24D.this.fm.EI.engines[3].getControlMix() * 0.15F;
                float f = 30F;
                if (CockpitB24D.this.flapsLever != 0.0F && CockpitB24D.this.flaps == CockpitB24D.this.fm.CT.getFlap()) {
                    CockpitB24D.this.flapsLever *= 0.8F;
                    if (Math.abs(CockpitB24D.this.flapsLever) < 0.1F) CockpitB24D.this.flapsLever = 0.0F;
                } else if (CockpitB24D.this.flaps < CockpitB24D.this.fm.CT.getFlap()) {
                    CockpitB24D.this.flaps = CockpitB24D.this.fm.CT.getFlap();
                    CockpitB24D.this.flapsLever += 2.0F;
                    if (CockpitB24D.this.flapsLever > f) CockpitB24D.this.flapsLever = f;
                } else if (CockpitB24D.this.flaps > CockpitB24D.this.fm.CT.getFlap()) {
                    CockpitB24D.this.flaps = CockpitB24D.this.fm.CT.getFlap();
                    CockpitB24D.this.flapsLever -= 2.0F;
                    if (CockpitB24D.this.flapsLever < -f) CockpitB24D.this.flapsLever = -f;
                }
                if (CockpitB24D.this.gearsLever != 0.0F && CockpitB24D.this.gears == CockpitB24D.this.fm.CT.getGear()) {
                    CockpitB24D.this.gearsLever *= 0.8F;
                    if (Math.abs(CockpitB24D.this.gearsLever) < 0.1F) CockpitB24D.this.gearsLever = 0.0F;
                } else if (CockpitB24D.this.gears < CockpitB24D.this.fm.CT.getGear()) {
                    CockpitB24D.this.gears = CockpitB24D.this.fm.CT.getGear();
                    CockpitB24D.this.gearsLever += 2.0F;
                    if (CockpitB24D.this.gearsLever > f) CockpitB24D.this.gearsLever = f;
                } else if (CockpitB24D.this.gears > CockpitB24D.this.fm.CT.getGear()) {
                    CockpitB24D.this.gears = CockpitB24D.this.fm.CT.getGear();
                    CockpitB24D.this.gearsLever -= 2.0F;
                    if (CockpitB24D.this.gearsLever < -f) CockpitB24D.this.gearsLever = -f;
                }
                CockpitB24D.this.setNew.altimeter = CockpitB24D.this.fm.getAltitude();
                float f1 = CockpitB24D.this.waypointAzimuth();
                CockpitB24D.this.setNew.azimuth.setDeg(CockpitB24D.this.setOld.azimuth.getDeg(1.0F), CockpitB24D.this.fm.Or.azimut());
                CockpitB24D.this.setNew.waypointAzimuth.setDeg(CockpitB24D.this.setOld.waypointAzimuth.getDeg(0.1F), f1);
                CockpitB24D.this.setNew.radioCompassAzimuth.setDeg(CockpitB24D.this.setOld.radioCompassAzimuth.getDeg(0.1F), f1 - CockpitB24D.this.setOld.azimuth.getDeg(0.1F) - 90F);
                CockpitB24D.this.engine1PropPitch = 0.5F * CockpitB24D.this.engine1PropPitch + 0.5F * CockpitB24D.this.fm.EI.engines[0].getControlProp();
                CockpitB24D.this.engine2PropPitch = 0.5F * CockpitB24D.this.engine2PropPitch + 0.5F * CockpitB24D.this.fm.EI.engines[1].getControlProp();
                CockpitB24D.this.engine3PropPitch = 0.5F * CockpitB24D.this.engine3PropPitch + 0.5F * CockpitB24D.this.fm.EI.engines[2].getControlProp();
                CockpitB24D.this.engine4PropPitch = 0.5F * CockpitB24D.this.engine4PropPitch + 0.5F * CockpitB24D.this.fm.EI.engines[3].getControlProp();
                for (int i = 0; i < 4; i++) {
                    float f2 = CockpitB24D.this.fm.EI.engines[i].getControlRadiator();
                    if (f2 < CockpitB24D.this.enginePrevRadiators[i]) CockpitB24D.this.engineRadiators[i] -= 0.2D;
                    else if (f2 > CockpitB24D.this.enginePrevRadiators[i]) CockpitB24D.this.engineRadiators[i] += 0.2D;
                    else CockpitB24D.this.engineRadiators[i] *= 0.5F;
                    if (CockpitB24D.this.engineRadiators[i] > 1.0F) CockpitB24D.this.engineRadiators[i] = 1.0F;
                    if (CockpitB24D.this.engineRadiators[i] < -1F) CockpitB24D.this.engineRadiators[i] = -1F;
                    CockpitB24D.this.enginePrevRadiators[i] = f2;
                }

                CockpitB24D.this.setNew.vspeed = (199F * CockpitB24D.this.setOld.vspeed + CockpitB24D.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      throttle3;
        float      throttle4;
        float      supercharger1;
        float      supercharger2;
        float      supercharger3;
        float      supercharger4;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float      vspeed;
        float      mix1;
        float      mix2;
        float      mix3;
        float      mix4;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }
    }

    public CockpitB24D() {
        super("3DO/Cockpit/B-24D-Cockpit/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.gearsLever = 0.0F;
        this.gears = 0.0F;
        this.flapsLever = 0.0F;
        this.flaps = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.pictManf3 = 1.0F;
        this.pictManf4 = 1.0F;
        this.engine1PropPitch = 0.0F;
        this.engine2PropPitch = 0.0F;
        this.engine3PropPitch = 0.0F;
        this.engine4PropPitch = 0.0F;
        this.bombReleaseTimer = 4000F;
        this.isSlideRight = false;
        this.dialsR1Dmg = false;
        this.dialsR2Dmg = false;
        this.dialsL1Dmg = false;
        this.dialsL2Dmg = false;
        this.dialDmg1 = 0.0F;
        this.cockpitNightMats = new String[] { "BC434Gg", "Gauges", "Gauges_Dmg", "Gyrocompass", "Needles", "Station" };
        this.setNightMats(false);
        this.ac = (B_24D140CO) this.aircraft();
        this.ac.registerPit(this);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.light1 = new LightPointActor(new LightPoint(), new Point3d(4.12D, 0.25D, 1.48D));
        this.light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
    }

    public void reflectWorldToInstruments(float f) {
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = this.fm.CT.getCockpitDoor() * 0.5F;
        if (Aircraft.xyz[1] < 0.01D) Aircraft.xyz[1] = 0.0F;
        if (this.isSlideRight) this.mesh.chunkSetLocate("BlisterRight", Aircraft.xyz, Aircraft.ypr);
        else this.mesh.chunkSetLocate("BlisterLeft", Aircraft.xyz, Aircraft.ypr);
        this.mesh.chunkSetAngles("zThrottle1", 90F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zThrottle2", 90F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zThrottle3", 90F * this.interp(this.setNew.throttle3, this.setOld.throttle3, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zThrottle4", 90F * this.interp(this.setNew.throttle4, this.setOld.throttle4, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMixture1", this.floatindex(this.cvt(this.setNew.mix1, 0.0F, 1.2F, 0.0F, 3F), mixScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMixture2", this.floatindex(this.cvt(this.setNew.mix2, 0.0F, 1.2F, 0.0F, 3F), mixScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMixture3", this.floatindex(this.cvt(this.setNew.mix3, 0.0F, 1.2F, 0.0F, 3F), mixScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMixture4", this.floatindex(this.cvt(this.setNew.mix4, 0.0F, 1.2F, 0.0F, 3F), mixScale), 0.0F, 0.0F);
        float f1 = 0.0F;
        if (this.engine1PropPitch > 0.0F && this.engine2PropPitch > 0.0F && this.engine3PropPitch > 0.0F && this.engine4PropPitch > 0.0F) {
            this.mesh.chunkSetAngles("zPropPitchGUp", 0.0F, 0.0F, this.engine1PropPitch * -115F);
            f1 = -this.cvt(this.engine1PropPitch, 0.8F, 1.0F, 0.01F, 20F);
        } else if (this.engine1PropPitch < 0.0F && this.engine2PropPitch < 0.0F && this.engine3PropPitch < 0.0F && this.engine4PropPitch < 0.0F) {
            this.mesh.chunkSetAngles("zPropPGangDown", 0.0F, 0.0F, this.engine1PropPitch * 115F);
            f1 = this.cvt(-this.engine1PropPitch, 0.8F, 1.0F, 0.01F, 20F);
        } else {
            this.mesh.chunkSetAngles("zPropPGangDown", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zPropPitchGUp", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zPropPitch1", 0.0F, 0.0F, f1 != 0.0F ? f1 : this.engine1PropPitch * 20F);
        this.mesh.chunkSetAngles("zPropPitch2", 0.0F, 0.0F, f1 != 0.0F ? f1 : this.engine2PropPitch * 20F);
        this.mesh.chunkSetAngles("zPropPitch3", 0.0F, 0.0F, f1 != 0.0F ? f1 : this.engine3PropPitch * 20F);
        this.mesh.chunkSetAngles("zPropPitch4", 0.0F, 0.0F, f1 != 0.0F ? f1 : this.engine4PropPitch * 20F);
        f1 = 0.0F;
        if (this.engineRadiators[0] > 0.0F && this.engineRadiators[1] > 0.0F && this.engineRadiators[2] > 0.0F && this.engineRadiators[3] > 0.0F) {
            this.mesh.chunkSetAngles("zCowlFGOpen", 0.0F, 0.0F, this.engineRadiators[0] * -115F);
            f1 = -this.cvt(this.engineRadiators[0], 0.8F, 1.0F, 0.01F, 20F);
        } else if (this.engineRadiators[0] < 0.0F && this.engineRadiators[1] < 0.0F && this.engineRadiators[2] < 0.0F && this.engineRadiators[3] < 0.0F) {
            this.mesh.chunkSetAngles("zCowlFGClose", 0.0F, 0.0F, this.engineRadiators[0] * 115F);
            f1 = this.cvt(-this.engineRadiators[0], 0.8F, 1.0F, 0.01F, 20F);
        } else {
            this.mesh.chunkSetAngles("zCowlFGClose", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zCowlFGOpen", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zCowlFlaps1", 0.0F, 0.0F, f1 != 0.0F ? f1 : this.engineRadiators[0] * 20F);
        this.mesh.chunkSetAngles("zCowlFlaps2", 0.0F, 0.0F, f1 != 0.0F ? f1 : this.engineRadiators[1] * 20F);
        this.mesh.chunkSetAngles("zCowlFlaps3", 0.0F, 0.0F, f1 != 0.0F ? f1 : this.engineRadiators[2] * 20F);
        this.mesh.chunkSetAngles("zCowlFlaps4", 0.0F, 0.0F, f1 != 0.0F ? f1 : this.engineRadiators[3] * 20F);
        this.mesh.chunkSetAngles("zLandingLights", 0.0F, 0.0F, this.fm.AS.bLandingLightOn ? -30F : 0.0F);
        this.mesh.chunkSetAngles("zCockpitLights", 0.0F, this.cockpitLightControl ? -60F : 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = -(this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 0.1F;
        Cockpit.ypr[0] = (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 45F;
        this.mesh.chunkSetLocate("zSteering1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("zSteering2", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = -this.fm.CT.getRudder() * 0.1F;
        this.mesh.chunkSetLocate("zPedalsL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = this.fm.CT.getRudder() * 0.1F;
        this.mesh.chunkSetLocate("zPedalsR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zBrakesL", -this.fm.CT.getBrake() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBrakesR", -this.fm.CT.getBrake() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAutopilotGang", 0.0F, 0.0F, this.fm.CT.StabilizerControl ? 20F : 0.0F);
        this.mesh.chunkSetAngles("zGear", this.gearsLever, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFlaps", this.flapsLever, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("z_AHorZ2", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        this.mesh.chunkSetLocate("z_AHorZ1", Cockpit.xyz, Cockpit.ypr);
        if (!this.dialsR2Dmg) {
            this.mesh.chunkSetAngles("z_ManP1", 0.0F, this.pictManf1 = 0.9F * this.pictManf1 + 0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.334F, 2.00526F, 0.0F, -322F), 0.0F);
            this.mesh.chunkSetAngles("z_ManP2", 0.0F, this.pictManf2 = 0.9F * this.pictManf2 + 0.1F * this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.334F, 2.00526F, 0.0F, -322F), 0.0F);
        }
        this.mesh.chunkSetAngles("z_ManP3", 0.0F, this.pictManf3 = 0.9F * this.pictManf3 + 0.1F * this.cvt(this.fm.EI.engines[2].getManifoldPressure(), 0.334F, 2.00526F, 0.0F, -322F), 0.0F);
        this.mesh.chunkSetAngles("z_ManP4", 0.0F, this.pictManf4 = 0.9F * this.pictManf4 + 0.1F * this.cvt(this.fm.EI.engines[3].getManifoldPressure(), 0.334F, 2.00526F, 0.0F, -322F), 0.0F);
        boolean flag = false;
        float f2 = this.cvt(this.fm.M.fuel, 0.0F, 100F, 0.0F, 1.0F);
        for (int i = 0; i < 4; i++) {
            if (this.fm.EI.engines[i].getStage() > 0 && this.fm.EI.engines[i].getStage() < 4) this.mesh.chunkSetAngles("zStarter" + (i + 1), 0.0F, 0.0F, -25F);
            else if (this.fm.EI.engines[i].getStage() >= 4 && this.fm.EI.engines[i].getStage() < 6) this.mesh.chunkSetAngles("zStarter" + (i + 1), 0.0F, 0.0F, 25F);
            else this.mesh.chunkSetAngles("zStarter" + (i + 1), 0.0F, 0.0F, 0.0F);
            float f5 = this.fm.EI.engines[i].getRPM();
            if (i < 2 && !this.dialsR1Dmg || i > 1 && !this.dialsR2Dmg) this.mesh.chunkSetAngles("z_RPM" + (i + 1), 0.0F, -this.cvt(f5, 0.0F, 4500F, 0.0F, 323F), 0.0F);
            if (this.fm.Or.getKren() < -110F || this.fm.Or.getKren() > 110F) this.rpmGeneratedPressure[i] = this.rpmGeneratedPressure[i] - 2.0F;
            else if (f5 < this.rpmGeneratedPressure[i]) this.rpmGeneratedPressure[i] = this.rpmGeneratedPressure[i] - (this.rpmGeneratedPressure[i] - f5) * 0.01F;
            else this.rpmGeneratedPressure[i] = this.rpmGeneratedPressure[i] + (f5 - this.rpmGeneratedPressure[i]) * 0.001F;
            if (this.rpmGeneratedPressure[i] < 800F) this.oilPressure[i] = this.cvt(this.rpmGeneratedPressure[i], 0.0F, 800F, 0.0F, 4F);
            else if (this.rpmGeneratedPressure[i] < 2000F) this.oilPressure[i] = this.cvt(this.rpmGeneratedPressure[i], 800F, 2000F, 4F, 5F);
            else this.oilPressure[i] = this.cvt(this.rpmGeneratedPressure[i], 2000F, 3000F, 5F, 5.8F);
            float f7 = 0.0F;
            if (this.fm.EI.engines[i].tOilIn > 90F) f7 = this.cvt(this.fm.EI.engines[i].tOilIn, 90F, 110F, 1.1F, 1.5F);
            else if (this.fm.EI.engines[i].tOilIn < 50F) f7 = this.cvt(this.fm.EI.engines[i].tOilIn, 0.0F, 50F, 2.0F, 0.9F);
            else f7 = this.cvt(this.fm.EI.engines[i].tOilIn, 50F, 90F, 0.9F, 1.1F);
            float f9 = f7 * this.fm.EI.engines[i].getReadyness() * this.oilPressure[i];
            this.mesh.chunkSetAngles("z_OilP" + (i + 1), 0.0F, -this.cvt(f9, 0.0F, 7F, 0.0F, 300F), 0.0F);
            if (i < 2 && !this.dialsR1Dmg || i > 1 && !this.dialsR2Dmg) this.mesh.chunkSetAngles("z_FuelP" + (i + 1), 0.0F, -this.cvt(this.rpmGeneratedPressure[i], 0.0F, 2000F * f2, 0.0F, 200F), 0.0F);
            if (i > 1 && !this.dialsR1Dmg || i < 2 && !this.dialsR2Dmg) this.mesh.chunkSetAngles("z_ECylT" + (i + 1), 0.0F, -this.cvt(this.fm.EI.engines[i].tWaterOut, 0.0F, 300F, 0.0F, 41F), 0.0F);
            if (!this.dialsR1Dmg) this.mesh.chunkSetAngles("z_OilT" + (i + 1), 0.0F, -this.cvt(this.fm.EI.engines[i].tOilIn, 30F, 150F, 0.0F, 41F), 0.0F);
            if (this.fm.EI.engines[i].getStage() > 0 && this.fm.EI.engines[i].getStage() < 6) flag = true;
        }

        if (flag) this.mesh.chunkSetAngles("zBattery", 0.0F, -60F, 0.0F);
        else this.mesh.chunkSetAngles("zBattery", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour", 0.0F, -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Min", 0.0F, -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Sec", 0.0F, -this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("z_Flap", 0.0F, -90F * this.fm.CT.getFlap(), 0.0F);
        if (this.fm.CT.bHasBrakeControl) {
            this.mesh.chunkSetAngles("z_HBrkI", 0.0F, -this.cvt(this.fm.CT.getBrake(), 0.0F, 1.5F, 0.0F, 160F), 0.0F);
            this.mesh.chunkSetAngles("z_HBrkO", 0.0F, 0.0F, this.cvt(this.fm.CT.getBrake(), 0.0F, 1.5F, 0.0F, 160F));
        }
        this.mesh.chunkSetAngles("Z_HydSys", 0.0F, this.fm.Gears.bIsHydroOperable ? -165.5F : 0.0F, 0.0F);
        if (!this.dialsL1Dmg) {
            float f3 = 0.25F * this.fm.EI.engines[0].getRPM() + 0.25F * this.fm.EI.engines[1].getRPM() + 0.25F * this.fm.EI.engines[2].getRPM() + 0.25F * this.fm.EI.engines[3].getRPM();
            f3 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f3))));
            this.mesh.chunkSetAngles("Z_Suction", 0.0F, -this.cvt(f3, 0.0F, 10F, 0.0F, 280F), 0.0F);
        }
        this.mesh.chunkSetAngles("zMagneto1", 0.0F, -30F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F);
        this.mesh.chunkSetAngles("zMagneto2", 0.0F, -30F * this.fm.EI.engines[1].getControlMagnetos(), 0.0F);
        this.mesh.chunkSetAngles("zMagneto3", 0.0F, -30F * this.fm.EI.engines[2].getControlMagnetos(), 0.0F);
        this.mesh.chunkSetAngles("zMagneto4", 0.0F, -30F * this.fm.EI.engines[3].getControlMagnetos(), 0.0F);
        if (this.fm.EI.engines[0].getControlMagnetos() == 0 && this.fm.EI.engines[1].getControlMagnetos() == 0 && this.fm.EI.engines[2].getControlMagnetos() == 0 && this.fm.EI.engines[3].getControlMagnetos() == 0)
            this.mesh.chunkSetAngles("zIgnMaster", 0.0F, -25F, 0.0F);
        else this.mesh.chunkSetAngles("zIgnMaster", 0.0F, 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = 0.0F;
        if (this.fm.CT.WeaponControl[3]) {
            Cockpit.xyz[2] = 0.03F;
            if (this.bombReleaseTimer == 4000F) this.bombReleaseTimer -= Time.tickLenFms() * f;
        }
        this.mesh.chunkSetLocate("zBombRelease", Cockpit.xyz, Cockpit.ypr);
        if (this.fm.CT.WeaponControl[3] && this.bombReleaseTimer == 4000F) this.bombReleaseTimer -= Time.tickLenFms() * f;
        if (this.bombReleaseTimer < 4000F && this.bombReleaseTimer > 0.0F) {
            this.bombReleaseTimer -= Time.tickLenFms() * f;
            this.mesh.chunkVisible("xBombRelGreen", true);
        } else this.mesh.chunkVisible("xBombRelGreen", false);
        this.mesh.chunkSetAngles("zSupercharger1", 90F * this.interp(this.setNew.supercharger1, this.setOld.supercharger1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSupercharger2", 90F * this.interp(this.setNew.supercharger2, this.setOld.supercharger2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSupercharger3", 90F * this.interp(this.setNew.supercharger3, this.setOld.supercharger3, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSupercharger4", 90F * this.interp(this.setNew.supercharger4, this.setOld.supercharger4, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("z_Comp", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("z_GyComp", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        if (this.fm.EI.engines[0].getControlFeather() != 0 || this.fm.EI.engines[1].getControlFeather() != 0 || this.fm.EI.engines[2].getControlFeather() != 0 || this.fm.EI.engines[3].getControlFeather() != 0)
            this.mesh.chunkSetAngles("zFeatherCover", 0.0F, 0.0F, -55F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.fm.EI.engines[0].getControlFeather() * 0.01F;
        this.mesh.chunkSetLocate("zFeather1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.fm.EI.engines[1].getControlFeather() * 0.01F;
        this.mesh.chunkSetLocate("zFeather2", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.fm.EI.engines[2].getControlFeather() * 0.01F;
        this.mesh.chunkSetLocate("zFeather3", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.fm.EI.engines[3].getControlFeather() * 0.01F;
        this.mesh.chunkSetLocate("zFeather4", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zAileronTScale", 180F * this.fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAileronTWheel", 720F * this.fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zElevTScale", 180F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zElevTWheel", 720F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRudderTKnob", 0.0F, 1040F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("zRudderTScale", 0.0F, 260F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkVisible("xBDoorsRed", this.fm.CT.getBayDoor() == 1.0F);
        this.mesh.chunkVisible("xWLockGreen", this.fm.CT.getGear() > 0.99F && this.fm.Gears.cgear && this.fm.CT.getGear() > 0.99F && this.fm.Gears.rgear && this.fm.CT.getGear() > 0.99F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("xGunButtonUp", this.fm.CT.WeaponControl[1]);
        this.mesh.chunkVisible("xGunButtonDn", !this.fm.CT.WeaponControl[1]);
        if (!this.dialsL1Dmg) {
            this.mesh.chunkSetAngles("z_AirTemp", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -45F, 45F, 55F, -55F), 0.0F);
            this.mesh.chunkSetAngles("Z_MPH", 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 482.8F, 0.0F, 15F), speedometerScale), 0.0F);
            this.mesh.chunkSetAngles("z_RComp", 0.0F, -this.setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F);
            float f4 = ((B_24D140CO) this.aircraft()).getBombSightPDI();
            float f6 = Math.abs(f4);
            float f8 = this.floatindex(this.cvt(f6, 0.0F, 0.6666667F, 0.0F, 4F), pdiScale);
            float f10 = 1.0F;
            if (f4 > 0.0F) f10 = -1F;
            this.mesh.chunkSetAngles("z_PDI", 0.0F, f8 * f10, 0.0F);
        } else this.mesh.chunkSetAngles("z_RComp", 0.0F, -this.fm.Or.getRoll() + 180F, 0.0F);
        if (!this.dialsL2Dmg) {
            this.mesh.chunkSetAngles("z_Alt1", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
            this.mesh.chunkSetAngles("z_Alt2", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
            this.mesh.chunkSetAngles("z_Alt3", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 360F), 0.0F);
            this.mesh.chunkSetAngles("Z_Climb", 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
            this.w.set(this.fm.getW());
            this.fm.Or.transform(this.w);
            this.mesh.chunkSetAngles("z_TurnB", 0.0F, -this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
            this.mesh.chunkSetAngles("z_TurnBB", 0.0F, -this.cvt(this.getBall(6D), -6F, 6F, -9.7F, 9.7F), 0.0F);
        } else {
            this.dialDmg1 += f * 3F;
            this.mesh.chunkSetAngles("z_Alt2", 0.0F, this.dialDmg1, 0.0F);
            this.mesh.chunkSetAngles("z_TurnBB", 0.0F, -this.cvt(this.getBall(0.1D) - this.fm.Or.getKren(), -10F, 10F, -9.7F, 9.7F), 0.0F);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("PnlDialsR1", false);
            this.mesh.chunkVisible("PnlDialsR1_Dmg", true);
            this.dialsR1Dmg = true;
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("PnlDialsL1", false);
            this.mesh.chunkVisible("PnlDialsL1_Dmg", true);
            this.dialsL1Dmg = true;
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("PnlDialsL2", false);
            this.mesh.chunkVisible("PnlDialsL2_Dmg", true);
            this.dialsL2Dmg = true;
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("PnlDialsL2", false);
            this.mesh.chunkVisible("PnlDialsL2_Dmg", true);
            this.dialsL2Dmg = true;
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("PnlDialsR2", false);
            this.mesh.chunkVisible("PnlDialsR2_Dmg", true);
            this.dialsR2Dmg = true;
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Pilot1"));
        this.mesh.materialReplace("Pilot1", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
            this.light1.light.setEmit(0.4F, 1.0F);
        } else {
            this.setNightMats(false);
            this.light1.light.setEmit(0.0F, 0.0F);
        }
    }

    private void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HierMesh hiermesh = this.aircraft().hierMesh();
            hiermesh.chunkVisible("Blister1_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        if (!hiermesh.isChunkVisible("Nose_Cap")) hiermesh.chunkVisible("Blister1_D0", true);
        super.doFocusLeave();
    }

    public boolean isViewRight() {
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        HookPilot.current.computePos(this, loc, loc1);
        float f = loc1.getOrient().getYaw();
        if (f < 0.0F) this.isSlideRight = true;
        else this.isSlideRight = false;
        return this.isSlideRight;
    }

    private B_24D140CO         ac;
    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              gearsLever;
    private float              gears;
    private float              flapsLever;
    private float              flaps;
    private float              pictManf1;
    private float              pictManf2;
    private float              pictManf3;
    private float              pictManf4;
    private float              engine1PropPitch;
    private float              engine2PropPitch;
    private float              engine3PropPitch;
    private float              engine4PropPitch;
    private float              engineRadiators[]      = { 0.0F, 0.0F, 0.0F, 0.0F };
    private float              enginePrevRadiators[]  = { 0.0F, 0.0F, 0.0F, 0.0F };
    private float              rpmGeneratedPressure[] = { 0.0F, 0.0F, 0.0F, 0.0F };
    private float              oilPressure[]          = { 0.0F, 0.0F, 0.0F, 0.0F };
    private float              bombReleaseTimer;
    private boolean            isSlideRight;
    private LightPointActor    light1;
    private boolean            dialsR1Dmg;
    private boolean            dialsR2Dmg;
    private boolean            dialsL1Dmg;
    private boolean            dialsL2Dmg;
    private float              dialDmg1;
    private static final float pdiScale[]             = { 0.0F, 21.75F, 32.25F, 38.25F, 45F };
    private static final float speedometerScale[]     = { 0.0F, 8F, 17.5F, 38F, 65F, 94F, 126.5F, 162.5F, 198F, 233F, 266F, 280.5F, 296F, 312F, 328F, 344F };
    private static final float variometerScale[]      = { -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 130F, 157F, 180F };
    private static final float mixScale[]             = { 0.0F, 17F, 44F, 105F };

    static {
        Property.set(CockpitB24D.class, "normZNs", new float[] { 0.75F, 0.75F, 1.27F, 1.56F });
    }
}
