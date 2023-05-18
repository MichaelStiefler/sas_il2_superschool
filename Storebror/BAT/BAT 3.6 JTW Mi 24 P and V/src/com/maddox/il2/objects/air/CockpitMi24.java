package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitMi24 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitMi24.this.fm != null) {
                CockpitMi24.this.setTmp = CockpitMi24.this.setOld;
                CockpitMi24.this.setOld = CockpitMi24.this.setNew;
                CockpitMi24.this.setNew = CockpitMi24.this.setTmp;
                CockpitMi24.this.setNew.throttle = (0.9F * CockpitMi24.this.setOld.throttle) + (CockpitMi24.this.fm.CT.PowerControl * 0.1F);
                CockpitMi24.this.setNew.starter = (0.94F * CockpitMi24.this.setOld.starter) + (0.06F * ((CockpitMi24.this.fm.EI.engines[0].getStage() > 0) && (CockpitMi24.this.fm.EI.engines[0].getStage() < 6) ? 1.0F : 0.0F));
                CockpitMi24.this.setNew.altimeter = CockpitMi24.this.fm.getAltitude();
                float f = CockpitMi24.this.waypointAzimuth();
                CockpitMi24.this.setNew.azimuth.setDeg(CockpitMi24.this.setOld.azimuth.getDeg(1.0F), CockpitMi24.this.fm.Or.azimut());
                if (CockpitMi24.this.useRealisticNavigationInstruments()) {
                    CockpitMi24.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitMi24.this.setOld.waypointAzimuth.setDeg(f - 90F);
                    CockpitMi24.this.setNew.radioCompassAzimuth.setDeg(CockpitMi24.this.setOld.radioCompassAzimuth.getDeg(0.02F), CockpitMi24.this.radioCompassAzimuthInvertMinus() - CockpitMi24.this.setOld.azimuth.getDeg(1.0F) - 90F);
                    if (CockpitMi24.this.fm.AS.listenLorenzBlindLanding && CockpitMi24.this.fm.AS.isAAFIAS) {
                        CockpitMi24.this.setNew.ilsLoc = ((10F * CockpitMi24.this.setOld.ilsLoc) + CockpitMi24.this.getBeaconDirection()) / 11F;
                        CockpitMi24.this.setNew.ilsGS = ((10F * CockpitMi24.this.setOld.ilsGS) + CockpitMi24.this.getGlidePath()) / 11F;
                    } else {
                        CockpitMi24.this.setNew.ilsLoc = 0.0F;
                        CockpitMi24.this.setNew.ilsGS = 0.0F;
                    }
                } else {
                    CockpitMi24.this.setNew.waypointAzimuth.setDeg(CockpitMi24.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitMi24.this.waypointAzimuth() - CockpitMi24.this.fm.Or.azimut());
                    CockpitMi24.this.setNew.radioCompassAzimuth.setDeg(CockpitMi24.this.setOld.radioCompassAzimuth.getDeg(0.1F), f - CockpitMi24.this.setOld.azimuth.getDeg(0.1F) - 90F);
                }
                Variables variables = CockpitMi24.this.setNew;
                float f1 = 0.9F * CockpitMi24.this.setOld.radioalt;
                float f2 = 0.1F;
                float f3 = CockpitMi24.this.fm.getAltitude();
                World.cur();
                World.land();
                variables.radioalt = f1 + (f2 * (f3 - Landscape.HQ_Air((float) CockpitMi24.this.fm.Loc.x, (float) CockpitMi24.this.fm.Loc.y)));
                CockpitMi24.this.setNew.vspeed = ((199F * CockpitMi24.this.setOld.vspeed) + CockpitMi24.this.fm.getVertSpeed()) / 200F;
                float f4 = ((Mi24X) CockpitMi24.this.aircraft()).k14Distance;
                CockpitMi24.this.setNew.k14w = (5F * CockpitMi24.k14TargetWingspanScale[((Mi24X) CockpitMi24.this.aircraft()).k14WingspanType]) / f4;
                CockpitMi24.this.setNew.k14w = (0.9F * CockpitMi24.this.setOld.k14w) + (0.1F * CockpitMi24.this.setNew.k14w);
                CockpitMi24.this.setNew.k14wingspan = (0.9F * CockpitMi24.this.setOld.k14wingspan) + (0.1F * CockpitMi24.k14TargetMarkScale[((Mi24X) CockpitMi24.this.aircraft()).k14WingspanType]);
                CockpitMi24.this.setNew.k14mode = (0.8F * CockpitMi24.this.setOld.k14mode) + (0.2F * ((Mi24X) CockpitMi24.this.aircraft()).k14Mode);
                Vector3d vector3d = CockpitMi24.this.aircraft().FM.getW();
                double d = 0.00125D * f4;
                float f5 = (float) Math.toDegrees(d * vector3d.z);
                float f6 = -(float) Math.toDegrees(d * vector3d.y);
                float f7 = 0.0F;
                if (((Mi24X) CockpitMi24.this.aircraft()).k14Mode == 1) {
                    f7 = f4 * 0.02F;
                }
                if (((Mi24X) CockpitMi24.this.aircraft()).k14Mode == 2) {
                    f7 = f4 * 0.14F;
                }
                if (((Mi24X) CockpitMi24.this.aircraft()).k14Mode == 4) {
                    f7 = CockpitMi24.this.floatindex((f4 - 200F) * 0.04F, CockpitMi24.k14BulletDrop) - CockpitMi24.k14BulletDrop[0];
                }
                f6 += (float) Math.toDegrees(Math.atan(f7 / f4));
                CockpitMi24.this.setNew.k14x = (0.92F * CockpitMi24.this.setOld.k14x) + (0.08F * f5);
                CockpitMi24.this.setNew.k14y = (0.92F * CockpitMi24.this.setOld.k14y) + (0.08F * f6);
                if (CockpitMi24.this.setNew.k14x > 7F) {
                    CockpitMi24.this.setNew.k14x = 7F;
                }
                if (CockpitMi24.this.setNew.k14x < -7F) {
                    CockpitMi24.this.setNew.k14x = -7F;
                }
                if (CockpitMi24.this.setNew.k14y > 7F) {
                    CockpitMi24.this.setNew.k14y = 7F;
                }
                if (CockpitMi24.this.setNew.k14y < -7F) {
                    CockpitMi24.this.setNew.k14y = -7F;
                }
                f2 = CockpitMi24.this.fm.getAltitude();
                f3 = (float) (-(Math.abs(CockpitMi24.this.fm.Vwld.length()) * Math.sin(Math.toRadians(Math.abs(CockpitMi24.this.fm.Or.getTangage())))) * 0.10189999639987946D);
                f3 += (float) Math.sqrt((f3 * f3) + (2.0F * f2 * 0.1019F));
                f4 = Math.abs((float) CockpitMi24.this.fm.Vwld.length()) * (float) Math.cos(Math.toRadians(Math.abs(CockpitMi24.this.fm.Or.getTangage())));
                f5 = ((f4 * f3) + 10F) - 10F;
                CockpitMi24.this.alpha = 90F - Math.abs(CockpitMi24.this.fm.Or.getTangage()) - (float) Math.toDegrees(Math.atan(f5 / f2));
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      vspeed;
        float      starter;
        float      altimeter;
        float      radioalt;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float      ilsLoc;
        float      ilsGS;
        float      k14wingspan;
        float      k14mode;
        float      k14x;
        float      k14y;
        float      k14w;

        private Variables() {
            this.throttle = 0.0F;
            this.starter = 0.0F;
            this.altimeter = 0.0F;
            this.vspeed = 0.0F;
            this.radioalt = 0.0F;
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((Mi24X) this.aircraft()).hierMesh().chunkVisible("Door1_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (Actor.isAlive(this.aircraft())) {
            ((Mi24X) this.aircraft()).hierMesh().chunkVisible("Door1_D0", true);
        }
        super.doFocusLeave();
    }

    public CockpitMi24() {
        super("3DO/Cockpit/Mi-24Pilot/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.cockpitNightMats = (new String[] { "Gause1", "Gause2", "Gause3", "Gause4", "Sidepanel", "instrument1" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public void reflectWorldToInstruments(float f) {
        if ((this.fm.AS.astateCockpitState & 2) == 0) {
            int i = ((Mi24X) this.aircraft()).k14Mode;
            if (i == 0) {
                this.mesh.chunkVisible("Z_Z_Bombmark1", false);
                this.mesh.chunkVisible("Z_Z_Rocketmark1", false);
            } else if ((i == 1) || (i == 2) || (i == 4)) {
                this.mesh.chunkVisible("Z_Z_Bombmark1", false);
                this.mesh.chunkVisible("Z_Z_Rocketmark1", true);
                this.mesh.chunkSetAngles("Z_Z_Rocketmark", -this.setNew.k14x * 0.75F, -this.setNew.k14y * 0.75F, 0.0F);
            } else if (i == 3) {
                this.mesh.chunkVisible("Z_Z_Bombmark1", true);
                this.mesh.chunkVisible("Z_Z_Rocketmark1", false);
                this.mesh.chunkSetAngles("Z_Z_Bombmark", 0.0F, -this.setNew.k14y, 0.0F);
                this.mesh.chunkSetAngles("Z_Z_Bombmark1", 0.0F, -this.alpha, 0.0F);
            }
        }
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Door", -90F * this.fm.CT.getCockpitDoor(), 0.0F, 0.0F);
         this.mesh.chunkSetAngles("Z_RPM1", -this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 16500F, 0.0F, 346.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", -this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 16500F, 0.0F, 346.5F), 0.0F, 0.0F);
        float rotorRPM = (float) ((Mi24X) this.aircraft()).rotorRPM;
        this.mesh.chunkSetAngles("Z_PropRPM", -this.cvt(rotorRPM, 0.0F, 264F, 0.0F, 346.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FUEL", -this.cvt(this.fm.M.fuel, 0.0F, 2500F, 0.0F, 301F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMPM1", -this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 264F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMPM2", -this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 120F, 0.0F, 264F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMPS1", -this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 150F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMPS4", -this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 150F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMPS2", -this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMPS3", -this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 120F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMPR1", -this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 150F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMPR2", -this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 150F, 0.0F, -120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMPR3", -this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 150F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMPR4", -this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 150F, 0.0F, -120F), 0.0F, 0.0F);
        float aPitch = ((Mi24X) this.aircraft()).aPitch;
        this.mesh.chunkSetAngles("Z_BladeAng", -this.cvt(aPitch, 0.0F, 1.153F, 0.0F, 204F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_G", -this.cvt(this.fm.getOverload(), -2F, 4F, -105F, 207F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Slide", this.cvt(this.fm.getAOS(), -40F, 40F, -111F, 111F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MIN", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_COMPASS1", 90F + this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_COMPASSB", -90F + this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_COMPASSC", this.setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_SPD", -this.cvt(this.fm.getSpeedKMH(), 50F, 450F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ALTB", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ALTS", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 10800F), 0.0F, 0.0F);
        float f1 = 0.9F * this.setOld.radioalt;
        float f2 = 0.1F;
        float f3 = this.fm.getAltitude();
        this.mesh.chunkSetAngles("Z_RadAlt", -this.floatindex(this.cvt(f1 + (f2 * (f3 - Landscape.HQ_Air((float) this.fm.Loc.x, (float) this.fm.Loc.y))), 0.0F, 700F, 0.0F, 8F), CockpitMi24.radaltScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_VertSpd", -this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.0F, 6F), CockpitMi24.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PVert", -this.cvt(this.setNew.vspeed, -10F, 10F, -51F, 51F), 0.0F, 0.0F);
        Vector3d vector3d = new Vector3d();
        this.fm.getSpeed(vector3d);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt((float) (vector3d.x * 3.6D), -25F, 50F, -0.022F, 0.047F);
        this.mesh.chunkSetLocate("Z_PSpd1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt((float) (vector3d.y * 3.6D), -25F, 25F, 0.027F, -0.027F);
        this.mesh.chunkSetLocate("Z_PSpd2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_HRZ", 0.0F, this.fm.Or.getKren(), -this.cvt(this.fm.Or.getTangage(), -40F, 40F, -21F, 21F));
        this.mesh.chunkSetAngles("Z_HRZEmer", 0.0F, this.fm.Or.getKren(), -this.cvt(this.fm.Or.getTangage(), -40F, 40F, -63F, 63F));
        this.mesh.chunkVisible("L_GearDown", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("L_GearUP", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
    }

    protected void reflectPlaneMats() {
        this.mesh.chunkVisible("Cockpit_D0", false);
        this.mesh.chunkVisible("CF_D0", false);
    }

    private float              alpha;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private static final float k14TargetMarkScale[]     = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float k14TargetWingspanScale[] = { 11F, 11.3F, 11.8F, 15F, 20F, 25F, 30F, 35F, 40F, 43.05F };
    private static final float k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };
    private static final float radaltScale[]            = { 0.0F, 155F, 181F, 207F, 232F, 257F, 281F, 303F };
    private static final float variometerScale[]        = { -180F, -141F, -78F, 0.0F, 78F, 141F, 180F };
    public Vector3f            w;

}
