package com.maddox.il2.objects.air;

import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.sound.SoundFX;
import com.maddox.sound.SoundPreset;

public class CockpitP_47DModPack extends CockpitPilot {

    class Variables {
        float throttle;
        float prop;
        float mix;
        float stage;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;
        float supercharge;

        private Variables() {
        }
    }

    class Interpolater extends InterpolateRef {
        public boolean tick() {
            if (CockpitP_47DModPack.this.fm != null) {
//                if (CockpitP_47DModPack.this.bNeedSetUp) {
//                    CockpitP_47DModPack.this.initLocalSounds();
//                    CockpitP_47DModPack.this.loadLocalSoundsFX();
//                    CockpitP_47DModPack.this.bNeedSetUp = false;
//                }
//                CockpitP_47DModPack.this.updateSound();
                CockpitP_47DModPack.this.setTmp = CockpitP_47DModPack.this.setOld;
                CockpitP_47DModPack.this.setOld = CockpitP_47DModPack.this.setNew;
                CockpitP_47DModPack.this.setNew = CockpitP_47DModPack.this.setTmp;
                CockpitP_47DModPack.this.setNew.throttle = 0.85F * CockpitP_47DModPack.this.setOld.throttle + CockpitP_47DModPack.this.fm.CT.PowerControl * 0.15F;
                CockpitP_47DModPack.this.setNew.prop = 0.85F * CockpitP_47DModPack.this.setOld.prop + CockpitP_47DModPack.this.fm.CT.getStepControl() * 0.15F;
                CockpitP_47DModPack.this.setNew.stage = 0.85F * CockpitP_47DModPack.this.setOld.stage + CockpitP_47DModPack.this.fm.EI.engines[0].getControlCompressor() * 0.15F;
                CockpitP_47DModPack.this.setNew.mix = 0.85F * CockpitP_47DModPack.this.setOld.mix + CockpitP_47DModPack.this.fm.EI.engines[0].getControlMix() * 0.15F;
                CockpitP_47DModPack.this.setNew.altimeter = CockpitP_47DModPack.this.fm.getAltitude();
                if (Math.abs(CockpitP_47DModPack.this.fm.Or.getKren()) < 45F) CockpitP_47DModPack.this.setNew.azimuth = (35F * CockpitP_47DModPack.this.setOld.azimuth - CockpitP_47DModPack.this.fm.Or.getYaw()) / 36F;
                if (CockpitP_47DModPack.this.setOld.azimuth > 270F && CockpitP_47DModPack.this.setNew.azimuth < 90F) CockpitP_47DModPack.this.setOld.azimuth -= 360F;
                if (CockpitP_47DModPack.this.setOld.azimuth < 90F && CockpitP_47DModPack.this.setNew.azimuth > 270F) CockpitP_47DModPack.this.setOld.azimuth += 360F;
                CockpitP_47DModPack.this.setNew.waypointAzimuth = (10F * CockpitP_47DModPack.this.setOld.waypointAzimuth + (CockpitP_47DModPack.this.waypointAzimuth() - CockpitP_47DModPack.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                CockpitP_47DModPack.this.setNew.vspeed = (199F * CockpitP_47DModPack.this.setOld.vspeed + CockpitP_47DModPack.this.fm.getVertSpeed()) / 200F;
                float f;
                if (CockpitP_47DModPack.this.fm.getAltitude() > 3000F) {
                    f = (float) Math.sin(1.0F * CockpitP_47DModPack.this.cvt(CockpitP_47DModPack.this.fm.getOverload(), 1.0F, 8F, 1.0F, 0.45F) * CockpitP_47DModPack.this.cvt(CockpitP_47DModPack.this.fm.AS.astatePilotStates[0], 0.0F, 100F, 1.0F, 0.1F)
                            * (0.001F * Time.current()));
                    if (f > 0.0F) {
                        CockpitP_47DModPack.this.pictBlinker += 0.3F;
                        if (CockpitP_47DModPack.this.pictBlinker > 1.0F) CockpitP_47DModPack.this.pictBlinker = 1.0F;
                    } else {
                        CockpitP_47DModPack.this.pictBlinker -= 0.3F;
                        if (CockpitP_47DModPack.this.pictBlinker < 0.0F) CockpitP_47DModPack.this.pictBlinker = 0.0F;
                    }
                }
                if (CockpitP_47DModPack.this.fm.EI.engines[0].getRPM() < 0.01F) CockpitP_47DModPack.this.pictTurba = 0.0F;
                else if (CockpitP_47DModPack.this.fm.getAltitude() < 1500F) CockpitP_47DModPack.this.pictTurba = 0.05F * CockpitP_47DModPack.this.fm.EI.engines[0].getManifoldPressure() * CockpitP_47DModPack.this.setNew.supercharge;
                else CockpitP_47DModPack.this.pictTurba = 0.2F * CockpitP_47DModPack.this.fm.EI.engines[0].getManifoldPressure() + 0.0001F * CockpitP_47DModPack.this.fm.getAltitude() * CockpitP_47DModPack.this.setNew.supercharge;
                if (CockpitP_47DModPack.this.aircraft() instanceof P_47ModPackAceMakerGunsight) {
                    P_47ModPackAceMakerGunsight amgAircraft = (P_47ModPackAceMakerGunsight) CockpitP_47DModPack.this.aircraft();
                    f = amgAircraft.k14Distance;
                    CockpitP_47DModPack.this.setNew.k14w = 5F * CockpitP_47DModPack.k14TargetWingspanScale[amgAircraft.k14WingspanType] / f;
                    CockpitP_47DModPack.this.setNew.k14w = 0.9F * CockpitP_47DModPack.this.setOld.k14w + 0.1F * CockpitP_47DModPack.this.setNew.k14w;
                    CockpitP_47DModPack.this.setNew.k14wingspan = 0.9F * CockpitP_47DModPack.this.setOld.k14wingspan + 0.1F * CockpitP_47DModPack.k14TargetMarkScale[amgAircraft.k14WingspanType];
                    CockpitP_47DModPack.this.setNew.k14mode = 0.8F * CockpitP_47DModPack.this.setOld.k14mode + 0.2F * amgAircraft.k14Mode;
                    Vector3d vector3d = CockpitP_47DModPack.this.aircraft().FM.getW();
                    double d = 0.00125D * f;
                    float f1 = (float) Math.toDegrees(d * vector3d.z);
                    float f2 = -(float) Math.toDegrees(d * vector3d.y);
                    float f3 = CockpitP_47DModPack.this.floatindex((f - 200F) * 0.04F, CockpitP_47DModPack.k14BulletDrop) - CockpitP_47DModPack.k14BulletDrop[0];
                    f2 += (float) Math.toDegrees(Math.atan(f3 / f));
                    CockpitP_47DModPack.this.setNew.k14x = 0.92F * CockpitP_47DModPack.this.setOld.k14x + 0.08F * f1;
                    CockpitP_47DModPack.this.setNew.k14y = 0.92F * CockpitP_47DModPack.this.setOld.k14y + 0.08F * f2;
                    if (CockpitP_47DModPack.this.setNew.k14x > 7F) CockpitP_47DModPack.this.setNew.k14x = 7F;
                    if (CockpitP_47DModPack.this.setNew.k14x < -7F) CockpitP_47DModPack.this.setNew.k14x = -7F;
                    if (CockpitP_47DModPack.this.setNew.k14y > 7F) CockpitP_47DModPack.this.setNew.k14y = 7F;
                    if (CockpitP_47DModPack.this.setNew.k14y < -7F) CockpitP_47DModPack.this.setNew.k14y = -7F;
                }
                CockpitP_47DModPack.this.setNew.supercharge = 0.99F * CockpitP_47DModPack.this.setOld.supercharge + 0.01F * CockpitP_47DModPack.this.setNew.throttle;
//                CockpitP_47DModPack.this.sfxCanopyWind(!CockpitP_47DModPack.this.bCanopyClosed);
//                CockpitP_47DModPack.this.sfxTurboSound(CockpitP_47DModPack.this.fm.EI.engines[0].getStage() > 0);
//                CockpitP_47DModPack.this.sfxBellSound(CockpitP_47DModPack.this.bRadarWarning);
//                CockpitP_47DModPack.this.sfxBellSound(true);
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) return 0.0F;
        else {
            waypoint.getP(Cockpit.P1);
            Cockpit.V.sub(Cockpit.P1, this.fm.Loc);
            return (float) (Math.toDegrees(Math.atan2(-Cockpit.V.y, Cockpit.V.x)));
        }
    }

    public CockpitP_47DModPack(String hierFile) {
        super(hierFile, "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictBlinker = 0.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = new String[] { "prib1", "prib2", "prib3", "prib4", "prib5", "prib6", "shkala", "prib1_d1", "prib2_d1", "prib3_d1", "prib4_d1", "prib5_d1", "prib6_d1" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.aircraft() instanceof P_47ModPackAceMakerGunsight) {
            int i = ((P_47ModPackAceMakerGunsight) this.aircraft()).k14Mode;
            boolean flag = i < 2;
            this.mesh.chunkVisible("Z_Z_RETICLE", flag);
            flag = i > 0;
            this.mesh.chunkVisible("Z_Z_RETICLE1", flag);
            this.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, this.setNew.k14x, this.setNew.k14y);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = this.setNew.k14w;
            for (int j = 1; j < 7; j++) {
                this.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
                this.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
            }
            this.mesh.chunkSetAngles("Z_Target1", this.setNew.k14wingspan, 0.0F, 0.0F);
        }

        if (this.bNeedSetUp) {
            this.initLocalSounds();
            this.loadLocalSoundsFX();
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }

        this.checkForEnemyBehind();
        this.changeSound();
        this.sfxFlapSound();
        
        this.sfxCanopyWind(!this.bCanopyClosed);
        this.sfxTurboSound(this.fm.EI.engines[0].getStage() > 0);
        this.sfxBellSound(this.bRadarWarning);
        
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.85F);
        this.mesh.chunkSetLocate("canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("armPedalL", 0.0F, -15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("armPedalR", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, -15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 20F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 16F);
        this.mesh.chunkSetAngles("supercharge", 0.0F, this.cvt(this.setNew.supercharge, 0.3F, 0.75F, -2F, 44F), 0.0F);
        this.mesh.chunkSetAngles("throtle", 0.0F, 0.0F, this.cvt(this.setNew.throttle, 0.0F, 1.1F, -6F, -68F));
        this.mesh.chunkSetAngles("prop", 0.0F, 70F * this.setNew.prop, 0.0F);
        this.mesh.chunkSetAngles("mixtura", 0.0F, 55F * this.setNew.mix, 0.0F);
        this.mesh.chunkSetAngles("flaplever", 0.0F, 0.0F, 70F * this.fm.CT.FlapsControl);
        this.mesh.chunkSetAngles("zfuelR", 0.0F, this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 981F, 0.0F, 6F), fuelGallonsScale), 0.0F);
        this.mesh.chunkSetAngles("zfuelL", 0.0F, -this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 981F, 0.0F, 4F), fuelGallonsAuxScale), 0.0F);
        this.mesh.chunkSetAngles("zacceleration", 0.0F, this.cvt(this.fm.getOverload(), -4F, 12F, -77F, 244F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zclimb", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F);
        this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -16F, 16F), 0.0F);
        this.mesh.chunkSetAngles("zManifold1a", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 210F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1c", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("zRPM1a", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 315F), 0.0F);
        this.mesh.chunkSetAngles("zoiltemp1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 300F, 0.0F, 120F), 0.0F);
        this.mesh.chunkSetAngles("ztempoil1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, -50F, 150F, 0.0F, 90F), 0.0F);
        this.mesh.chunkSetAngles("ZTemp1a", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -15F, 55F, 0.0F, 30F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zhorizont1a", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0328F, -0.0328F);
        this.mesh.chunkSetLocate("zhorizont1b", Cockpit.xyz, Cockpit.ypr);
        if (this.mesh.chunkFindCheck("zturborpm1a") > 0) this.mesh.chunkSetAngles("zturborpm1a", 0.0F, this.cvt(this.pictTurba, 0.0F, 2.0F, 0.0F, 207.5F), 0.0F);
        this.mesh.chunkSetAngles("zpressfuel1a", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.4F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("zpressoil1a", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, 60F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -5F, 5F, -5F, 5F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, 90F - this.setNew.azimuth, 0.0F);
        this.mesh.chunkSetAngles("zMagAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -65F, 65F, -65F, 65F), 0.0F);
        this.mesh.chunkSetAngles("zMagAzimuth1b", -90F + this.setNew.azimuth, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Zfas1a", 0.0F, this.cvt(this.fm.Gears.isHydroOperable() ? 0.8F : 0.0F, 0.0F, 1.0F, 0.0F, 110F), 0.0F);
        this.mesh.chunkSetAngles("zpresswater1a", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 200F, 0.0F, 250F), 0.0F);
        float f1 = this.fm.EI.engines[0].getRPM();
        f1 = 4.2F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("ZSuction1a", 0.0F, this.cvt(f1, 0.0F, 10F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("ZOxipress1a", 0.0F, 90F, 0.0F);
        Cockpit.xyz[2] = 0.01F * this.pictBlinker;
        this.mesh.chunkSetLocate("zBlink1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("zBlink2", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        this.mesh.chunkVisible("Z_Red1", this.fm.CT.getGear() < 0.05F || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_Red2", this.fm.M.fuel / this.fm.M.maxFuel < 0.15F);
        float f2 = (float) Math.sin(0.007F * Time.current());
        this.mesh.chunkVisible("Z_Red3", f2 * f2 + this.pictTurba - 0.7F > 0.5F);
        this.mesh.chunkVisible("Z_Green2", this.fm.AS.bNavLightsOn);
        this.mesh.chunkVisible("Z_Red4", this.fm.AS.bNavLightsOn);
        if (this.mesh.chunkFindCheck("zSlip") > 0) this.mesh.chunkSetAngles("zSlip", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -16F, 16F), 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            if (this.mesh.chunkFindCheck("Z_Holes1_D1") > 0) this.mesh.chunkVisible("Z_Holes1_D1", true);
            if (this.mesh.chunkFindCheck("Z_Holes1_D2") > 0) this.mesh.chunkVisible("Z_Holes1_D2", true);
            this.mesh.chunkVisible("pricel", false);
            if (this.mesh.chunkFindCheck("Z_Z_MASK") > 0) this.mesh.chunkVisible("Z_Z_MASK", true);
            if (this.mesh.chunkFindCheck("Z_Z_MASK2") > 0) this.mesh.chunkVisible("Z_Z_MASK2", true);
            if (this.mesh.chunkFindCheck("pricel_d1") > 0) this.mesh.chunkVisible("pricel_d1", true);
            if (this.mesh.chunkFindCheck("ZSlip") > 0) this.mesh.chunkVisible("ZSlip", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            if (this.mesh.chunkFindCheck("Z_Holes1_D1") > 0) this.mesh.chunkVisible("Z_Holes1_D1", true);
            if (this.mesh.chunkFindCheck("Z_Holes2_D1") > 0) this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_d1", true);
            this.mesh.chunkVisible("zamper", false);
            this.mesh.chunkVisible("zAzimuth1a", false);
            this.mesh.chunkVisible("zAzimuth1b", false);
            this.mesh.chunkVisible("zSpeed1a", false);
            this.mesh.chunkVisible("zacceleration", false);
            this.mesh.chunkVisible("zMagAzimuth1a", false);
            this.mesh.chunkVisible("zMagAzimuth1b", false);
            this.mesh.chunkVisible("zpresswater1a", false);
            this.mesh.chunkVisible("zclimb", false);
            this.mesh.chunkVisible("zRPM1a", false);
            this.mesh.chunkVisible("zoiltemp1a", false);
            this.mesh.chunkVisible("zfas1a", false);
            this.mesh.chunkVisible("zoxipress1a", false);
            if (this.mesh.chunkFindCheck("zturborpm1a") > 0) this.mesh.chunkVisible("zturborpm1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x4) == 0 || (this.fm.AS.astateCockpitState & 0x8) != 0) {
            if (this.mesh.chunkFindCheck("Z_Holes2_D1") > 0) this.mesh.chunkVisible("Z_Holes2_D1", true);
            if (this.mesh.chunkFindCheck("Z_Holes2_D2") > 0) this.mesh.chunkVisible("Z_Holes2_D2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("Z_OilSplats_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) == 0 || (this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_d1", true);
            this.mesh.chunkVisible("zClock1b", false);
            this.mesh.chunkVisible("zClock1a", false);
            this.mesh.chunkVisible("zfuelR", false);
            this.mesh.chunkVisible("zfuelL", false);
            this.mesh.chunkVisible("zsuction1a", false);
            this.mesh.chunkVisible("zTurn1a", false);
            this.mesh.chunkVisible("zSlide1a", false);
            this.mesh.chunkVisible("zhorizont1a", false);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
            this.mesh.chunkVisible("zpressfuel1a", false);
            this.mesh.chunkVisible("zpressoil1a", false);
            this.mesh.chunkVisible("ztempoil1a", false);
            if (this.mesh.chunkFindCheck("zturborpm1a") > 0) this.mesh.chunkVisible("zturbormp1a", false);
            this.mesh.chunkVisible("zManifold1a", false);
            this.mesh.chunkVisible("zBlink1", false);
            this.mesh.chunkVisible("zBlink2", false);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
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
    
    private SoundFX adjustFX(SoundFX soundfx, int usrFlag, double x, double y, double z) {
        soundfx.setParent(this.aircraft().getRootFX());
        soundfx.setUsrFlag(usrFlag);
        soundfx.setPosition(new Point3d(x, y, z));
        return soundfx;
    }

    private void loadLocalSoundsFX() {
        if (this.sounds != null) {
            if (this.sounds[7] == null) {
                SoundFX tempSfx = this.aircraft().newSound(sfxPreset, false, false);
                if (tempSfx != null) {
                    this.sounds[7] = adjustFX(tempSfx, 7, -0.5D, 0.7D, -0.1D);
                    this.sounds[7].setVolume(10.0F);
                    this.sndBell = this.sounds[7];
                    printDebugMessage("*** Bell sound loaded");
                }
            }
            if (this.sounds[8] == null) {
                SoundFX tempSfx = this.aircraft().newSound(sfxPreset, false, false);
                if (tempSfx != null) {
                    this.sndTurbo = this.sounds[8] = adjustFX(tempSfx, 8, 0.5D, 0.0D, -0.5D);
                    printDebugMessage("*** Turbo sound loaded");
                }
            }
            if (this.sounds[10] == null) {
                SoundFX tempSfx = this.aircraft().newSound(sfxPreset, false, false);
                if (tempSfx != null) {
                    ((P_47ModPack) this.aircraft()).soundWheels = this.sounds[10] = adjustFX(tempSfx, 10, 0.0D, 0.0D, -1D);
                    printDebugMessage("*** Wheels sound loaded");
                }
            }
            if (this.sounds[11] == null) {
                SoundFX tempSfx = this.aircraft().newSound(sfxPreset, false, false);
                if (tempSfx != null) {
                    ((P_47ModPack) this.aircraft()).soundGearUp = this.sounds[11] = adjustFX(tempSfx, 11, 0.0D, 0.0D, -1D);
                    printDebugMessage("*** GearUp sound loaded");
                }
            }
            if (this.sounds[12] == null) {
                SoundFX tempSfx = this.aircraft().newSound(sfxPreset, false, false);
                if (tempSfx != null) {
                    ((P_47ModPack) this.aircraft()).soundGearDn = this.sounds[12] = adjustFX(tempSfx, 12, 0.0D, 0.0D, -1D);
                    printDebugMessage("*** GearDn sound loaded");
                }
            }
            if (this.sounds[13] == null) {
                SoundFX tempSfx = this.aircraft().newSound(sfxPreset, false, false);
                if (tempSfx != null) {
                    this.sndFlapsEnd = this.sounds[13] = adjustFX(tempSfx, 13, 0.5D, 0.0D, -0.5D);
                    printDebugMessage("*** FlapsEnd sound loaded");
                }
            }
            if (this.sounds[14] == null) {
                SoundFX tempSfx = this.aircraft().newSound(sfxPreset, false, false);
                if (tempSfx != null) {
                    this.sndFlaps = this.sounds[14] = adjustFX(tempSfx, 14, 0.5D, 0.0D, -0.5D);
                    printDebugMessage("*** Flaps sound loaded");
                }
            }
            if (this.sounds[15] == null) {
                SoundFX tempSfx = this.aircraft().newSound(sfxPreset, false, false);
                if (tempSfx != null) {
                    this.sndWind = this.sounds[15] = adjustFX(tempSfx, 15, 1.0D, 0.0D, 0.0D);
                    printDebugMessage("*** Canopy wind sound loaded");
                }
            }
        }
    }
    
    

    private void sfxFlapSound() {
        if (((P_47ModPack) this.aircraft()).bFlaps) {
            if (this.sndFlaps != null) this.sndFlaps.setPlay(true);
            this.iFlapEndCycles = this.fm.CT.FlapsControl != 0.0F ? 10 : 20;
        }
        if (((P_47ModPack) this.aircraft()).bFlapsEnd) {
            if (this.sndFlapsEnd != null) this.sndFlapsEnd.setPlay(true);
            if (this.iFlapEndCycles-- == 0) {
                if (this.sndFlaps != null) this.sndFlaps.setPlay(false);
                ((P_47ModPack) this.aircraft()).bFlapsEnd = false;
            }
        }
    }

    private void sfxCanopyWind(boolean flag) {
        if (this.sndWind != null) {
            this.vol = this.fm.getSpeedKMH() / 180F;
            this.vol = this.vol <= 1.0F ? this.vol : 1.0F;
            this.sndWind.setVolume(this.vol);
            this.sndWind.setPlay(flag);
        }
    }

    private void sfxTurboSound(boolean flag) {
        if (this.sndTurbo != null) {
            this.vol = this.pictTurba * 0.5F + 0.2F;
            this.vol = this.vol <= 1.0F ? this.vol : 1.0F;
            this.sndTurbo.setVolume(this.vol);
            this.pitch = this.pictTurba * 2.0F + 1.0F;
            this.sndTurbo.setPitch(this.pitch);
            this.sndTurbo.setPlay(flag);
        }
    }

    private void initLocalSounds() {
        sfxPreset = null;
        sfxPreset = new SoundPreset("aircraft.cockpit_p47");
        this.sounds = new SoundFX[18];
        printDebugMessage("*** Local Sounds Loaded: aircraft.cockpit_p47.prs");
    }

    private void resetAcoustics(String s) {
        ((P_47ModPack) this.aircraft()).setAcoustics(null);
        this.acoustics = new Acoustics(s);
        this.acoustics.setParent(Engine.worldAcoustics());
        ((P_47ModPack) this.aircraft()).setAcoustics(this.acoustics);
        ((P_47ModPack) this.aircraft()).enableDoorSnd(true);
        this.acoustics.globFX = new ReverbFXRoom(0.45F);
//        this.bNeedSetUp = true;
        printDebugMessage("*** Acoustics Loaded:" + s);
    }

//    private void updateSound() {
//        if ((this.fm.CT.getCockpitDoor() == 0.0F || this.fm.CT.getCockpitDoor() == 1.0F) && Main3D.cur3D().isViewOutside() && !this.bMusicPresent && this.fm.isStationedOnGround()) {
//            CmdEnv.top().exec("music PLAY");
//            this.bMusicPresent = true;
//            printDebugMessage("*** Music set to play outside");
//        }
//    }

    private void changeSound() {
        if (this.fm.CT.getCockpitDoor() < 0.1F && !this.bCanopyClosed) {
            this.resetAcoustics("p47cls");
            this.bCanopyClosed = true;
            printDebugMessage("*** Canopy closed - music: " + this.bMusicPresent);
        }
        if (this.fm.CT.getCockpitDoor() > 0.1F && this.bCanopyClosed) {
            this.resetAcoustics("p47opn");
            this.bCanopyClosed = false;
            printDebugMessage("*** Canopy open - music: " + this.bMusicPresent);
        }
        if (this.fm.isStationedOnGround() && !Main3D.cur3D().isViewOutside()) {
            if (this.bMusicPresent && this.fm.CT.getCockpitDoor() < 0.1F) {
                CmdEnv.top().exec("music STOP");
                this.bMusicPresent = false;
                printDebugMessage("*** Music set to stop");
            }
            if (!this.bMusicPresent && this.fm.CT.getCockpitDoor() > 0.1F) {
                CmdEnv.top().exec("music PLAY");
                this.bMusicPresent = true;
                printDebugMessage("*** Music set to play");
            }
        }
    }

//    private void checkForEnemyBehindOld() {
//        Aircraft nearestEnemy = War.getNearestEnemy(this.aircraft(), 800F);
//        if (nearestEnemy != null && (nearestEnemy instanceof TypeFighter || nearestEnemy instanceof TypeStormovik)) {
//            this.danger = nearestEnemy.FM;
//            this.dist = (float) this.danger.Loc.distance(this.aircraft().FM.Loc);
//            VDanger.sub(this.danger.Loc, this.aircraft().FM.Loc);
//            VDanger.normalize();
//            this.bRadarWarning = VDanger.x < 0.0D && this.dist > 100F;
//            HUD.training("RWR:" + VDanger.x);
//        } else {
//            this.danger = null;
//            this.bRadarWarning = false;
//        }
//    }

    private void checkForEnemyBehind() {
        this.bRadarWarning = false;
        if (!(this.aircraft() instanceof P_47ModPack)) return;
        if (!((P_47ModPack)this.aircraft()).isRwrActive) return;
            
        double agl = this.aircraft().pos.getAbsPoint().z - Engine.cur.land.HQ(this.aircraft().pos.getAbsPoint().x, this.aircraft().pos.getAbsPoint().y);
        if (agl < 450) return;
        double maxDistance = 730D;
        if (agl < 950) maxDistance = Aircraft.cvt((float)agl, 450F, 950F, 300F, 730F);

        try {
            List list = Engine.targets();
            int targetsListSize = list.size();
            for (int targetsListIndex = 0; targetsListIndex < targetsListSize; targetsListIndex++) {
                Actor theTarget = (Actor) list.get(targetsListIndex);
                if (!(theTarget instanceof Aircraft)) continue;
                double targetDistance = GuidedMissileUtils.distanceBetween(this.aircraft(), theTarget);
                if (targetDistance > maxDistance) continue;
                if (targetDistance < 180D) continue;
                float targetAngle = GuidedMissileUtils.angleBetween(this.aircraft(), theTarget);
                if (targetAngle == Float.NaN) continue;
                if (targetAngle < 0F) targetAngle *= -1F;
                if (targetAngle < 140F) continue;
                if (targetAngle > 180F) continue;
//                DecimalFormat twoDigits = new DecimalFormat("0.##");
//                HUD.training("D:" + twoDigits.format(targetDistance) + ", A:" + twoDigits.format(targetAngle));
                this.bRadarWarning = true;
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sfxBellSound(boolean flag) {
        if (this.sndBell != null) this.sndBell.setPlay(flag);
    }

    public void destroy() {
        if (this.isDestroyed()) return;
        super.destroy();
        if (this.sndWind != null) this.sndWind.cancel();
        if (this.sndFlaps != null) this.sndFlaps.cancel();
        if (this.sndFlapsEnd != null) this.sndFlapsEnd.cancel();
        if (this.sndTurbo != null) this.sndTurbo.cancel();
        if (this.sndBell != null) this.sndBell.cancel();
    }

    protected static void printDebugMessage(String theMessage) {
        if (_DEBUG) System.out.println(theMessage);
    }

    private static boolean     _DEBUG                   = false;
    private Variables          setOld;
    protected Variables        setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictTurba;
    private float              pictBlinker;
    private boolean            bNeedSetUp;
    private static final float fuelGallonsScale[]       = { 0.0F, 8.25F, 17.5F, 36.5F, 54F, 90F, 108F };
    private static final float fuelGallonsAuxScale[]    = { 0.0F, 38F, 62.5F, 87F, 104F };
    private static final float speedometerScale[]       = { 0.0F, 5F, 47.5F, 92F, 134F, 180F, 227F, 241F, 255F, 262.5F, 270F, 283F, 296F, 312F, 328F };
    private static final float variometerScale[]        = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
    private static final float k14TargetMarkScale[]     = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F };
    private static final float k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F,
            10.782F, 10.789F };

//    private FlightModel        danger;
//    private static Vector3d    VDanger                  = new Vector3d();
    private boolean            bRadarWarning;
//    private float              dist;
//    private SoundFX            soundfx1;
    private SoundFX            sndWind;
    private SoundFX            sndTurbo;
    private SoundFX            sndFlaps;
    private SoundFX            sndFlapsEnd;
    private SoundFX            sndBell;
    private int                iFlapEndCycles;
    private boolean            bCanopyClosed;
    private boolean            bMusicPresent;
    private float              vol;
    private float              pitch;
}
