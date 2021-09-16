package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitSpitF22 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitSpitF22.this.bNeedSetUp) {
                CockpitSpitF22.this.reflectPlaneMats();
                CockpitSpitF22.this.bNeedSetUp = false;
            }
            if (CockpitSpitF22.this.fm != null) {
                CockpitSpitF22.this.setTmp = CockpitSpitF22.this.setOld;
                CockpitSpitF22.this.setOld = CockpitSpitF22.this.setNew;
                CockpitSpitF22.this.setNew = CockpitSpitF22.this.setTmp;
                CockpitSpitF22.this.setNew.throttle = (0.92F * CockpitSpitF22.this.setOld.throttle) + (0.08F * CockpitSpitF22.this.fm.CT.PowerControl);
                CockpitSpitF22.this.setNew.prop = (0.92F * CockpitSpitF22.this.setOld.prop) + (0.08F * CockpitSpitF22.this.fm.EI.engines[0].getControlProp());
                CockpitSpitF22.this.setNew.mix = (0.92F * CockpitSpitF22.this.setOld.mix) + (0.08F * CockpitSpitF22.this.fm.EI.engines[0].getControlMix());
                CockpitSpitF22.this.setNew.altimeter = CockpitSpitF22.this.fm.getAltitude();
                if (Math.abs(CockpitSpitF22.this.fm.Or.getKren()) < 30F) {
                    CockpitSpitF22.this.setNew.azimuth = (0.97F * CockpitSpitF22.this.setOld.azimuth) + (0.03F * -CockpitSpitF22.this.fm.Or.getYaw());
                }
                if ((CockpitSpitF22.this.setOld.azimuth > 270F) && (CockpitSpitF22.this.setNew.azimuth < 90F)) {
                    CockpitSpitF22.this.setOld.azimuth -= 360F;
                }
                if ((CockpitSpitF22.this.setOld.azimuth < 90F) && (CockpitSpitF22.this.setNew.azimuth > 270F)) {
                    CockpitSpitF22.this.setOld.azimuth += 360F;
                }
                CockpitSpitF22.this.setNew.waypointAzimuth = (0.91F * CockpitSpitF22.this.setOld.waypointAzimuth) + (0.09F * (CockpitSpitF22.this.waypointAzimuth() - CockpitSpitF22.this.setOld.azimuth)) + World.Rnd().nextFloat(-10F, 10F);
                CockpitSpitF22.this.setNew.vspeed = (0.99F * CockpitSpitF22.this.setOld.vspeed) + (0.01F * CockpitSpitF22.this.fm.getVertSpeed());
                float f = ((SPITFIREF22) CockpitSpitF22.this.aircraft()).k14Distance;
                CockpitSpitF22.this.setNew.k14w = (5F * CockpitSpitF22.k14TargetWingspanScale[((SPITFIREF22) CockpitSpitF22.this.aircraft()).k14WingspanType]) / f;
                CockpitSpitF22.this.setNew.k14w = (0.9F * CockpitSpitF22.this.setOld.k14w) + (0.1F * CockpitSpitF22.this.setNew.k14w);
                CockpitSpitF22.this.setNew.k14wingspan = (0.9F * CockpitSpitF22.this.setOld.k14wingspan) + (0.1F * CockpitSpitF22.k14TargetMarkScale[((SPITFIREF22) CockpitSpitF22.this.aircraft()).k14WingspanType]);
                CockpitSpitF22.this.setNew.k14mode = (0.8F * CockpitSpitF22.this.setOld.k14mode) + (0.2F * ((SPITFIREF22) CockpitSpitF22.this.aircraft()).k14Mode);
                Vector3d vector3d = CockpitSpitF22.this.aircraft().FM.getW();
                double d = 0.00125D * f;
                float f1 = (float) Math.toDegrees(d * vector3d.z);
                float f2 = -(float) Math.toDegrees(d * vector3d.y);
                float f3 = CockpitSpitF22.this.floatindex((f - 200F) * 0.04F, CockpitSpitF22.k14BulletDrop) - CockpitSpitF22.k14BulletDrop[0];
                f2 += (float) Math.toDegrees(Math.atan(f3 / f));
                CockpitSpitF22.this.setNew.k14x = (0.92F * CockpitSpitF22.this.setOld.k14x) + (0.08F * f1);
                CockpitSpitF22.this.setNew.k14y = (0.92F * CockpitSpitF22.this.setOld.k14y) + (0.08F * f2);
                if (CockpitSpitF22.this.setNew.k14x > 7F) {
                    CockpitSpitF22.this.setNew.k14x = 7F;
                }
                if (CockpitSpitF22.this.setNew.k14x < -7F) {
                    CockpitSpitF22.this.setNew.k14x = -7F;
                }
                if (CockpitSpitF22.this.setNew.k14y > 7F) {
                    CockpitSpitF22.this.setNew.k14y = 7F;
                }
                if (CockpitSpitF22.this.setNew.k14y < -7F) {
                    CockpitSpitF22.this.setNew.k14y = -7F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float throttle;
        float prop;
        float mix;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;

        private Variables() {
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        } else {
            waypoint.getP(this.tmpP);
            this.tmpV.sub(this.tmpP, this.fm.Loc);
            return (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x)));
        }
    }

    protected void setCameraOffset() {
        this.cameraCenter.add(0.03D, 0.0D, 0.0D);
    }

    public CockpitSpitF22() {
        super("3DO/Cockpit/SpitfireFRXIVE/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictBrake = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.pictManf = 1.0F;
        this.bNeedSetUp = true;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "COMPASS", "BORT2", "prib_five", "prib_five_damage", "prib_one", "prib_one_damage", "prib_three", "prib_three_damage", "prib_two", "prib_two_damage", "text13", "text15" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Blister2_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Blister2_D0", true);
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float f) {
        int i = ((SPITFIREF22) this.aircraft()).k14Mode;
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

        this.resetYPRmodifier();
        boolean flag1 = true;
        try {
            if (this.fm.CT.bMoveSideDoor) {
                this.mesh.chunkSetAngles("Door", 0.0F, -170F * this.fm.CT.getCockpitDoor(), 0.0F);
                flag1 = false;
            }
        } catch (Throwable throwable) {
        }
        if (flag1) {
            Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
            this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        }
        this.mesh.chunkVisible("XLampGearUpL", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("XLampGearDownL", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("XLampboost", this.fm.EI.engines[0].getControlCompressor() > 0);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 8F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_Column", -30F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Target1", this.setNew.k14wingspan, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang01a", -5F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang01b", -9F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang01c", -12F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang02a", -5F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang02b", -7.5F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang02c", -15F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang03a", -5F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang03b", -8.5F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Shlang03c", -18.5F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Stick_Shtok01", 0.0F, 0.0F, 8F * this.pictElev);
        this.mesh.chunkSetAngles("Z_ColumnSwitch", -18F * (this.pictBrake = (0.89F * this.pictBrake) + (0.11F * this.fm.CT.BrakeControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", this.cvt(this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 1.1F, 20F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BasePedal", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = 0.0578F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -0.0578F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Gear1", -160F + (160F * (this.pictGear = (0.89F * this.pictGear) + (0.11F * this.fm.CT.GearControl))), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 160F * (this.pictFlap = (0.89F * this.pictFlap) + (0.11F * this.fm.CT.FlapsControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim1", 1000F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 1000F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", -30F * this.setNew.prop, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("COMPASS_M", -this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SHKALA_DIRECTOR", this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STREL_ALT_LONG", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
        this.mesh.chunkSetAngles("STREL_ALT_SHORT", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
        this.mesh.chunkSetAngles("STREL_ALT_SHRT1", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -108F));
        this.mesh.chunkSetAngles("STRELKA_BOOST", 0.0F, 0.0F, -this.cvt(this.pictManf = (0.91F * this.pictManf) + (0.09F * this.fm.EI.engines[0].getManifoldPressure()), 0.5173668F, 2.72369F, -70F, 250F));
        this.mesh.chunkSetAngles("STRELKA_FUEL", 0.0F, 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 378.54F, 0.0F, 68F));
        this.mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 1000F, 5000F, 2.0F, 10F), CockpitSpitF22.rpmScale));
        this.mesh.chunkSetAngles("STRELK_TEMP_OIL", 0.0F, 0.0F, -this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 100F, 0.0F, 271F));
        this.mesh.chunkSetAngles("STRELK_TEMP_RAD", 0.0F, 0.0F, -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitSpitF22.radScale));
        this.mesh.chunkSetAngles("STR_OIL_LB", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, -37F), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("STREL_TURN_DOWN", 0.0F, 0.0F, -this.cvt(this.w.z, -0.23562F, 0.23562F, -48F, 48F));
        this.mesh.chunkSetAngles("STRELK_TURN_UP", 0.0F, 0.0F, -this.cvt(this.getBall(8D), -8F, 8F, 35F, -35F));
        this.mesh.chunkVisible("STRELK_V_SHORT", false);
        this.mesh.chunkSetAngles("STRELK_V_LONG", 0.0F, 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 223.52F, 0.0F, 25F), CockpitSpitF22.speedometerScale));
        this.mesh.chunkSetAngles("STRELKA_VY", 0.0F, 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), CockpitSpitF22.variometerScale));
        this.mesh.chunkSetAngles("STRELKA_GOR", 0.0F, 0.0F, this.fm.Or.getKren());
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.032F, -0.032F);
        this.mesh.chunkSetLocate("STRELKA_GOS", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("STR_CLIMB", 0.0F, 0.0F, this.cvt(this.fm.CT.trimElevator, -0.5F, 0.5F, -35.23F, 35.23F));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    protected void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictBrake;
    private float              pictFlap;
    private float              pictGear;
    private float              pictManf;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[]       = { 0.0F, 7.5F, 17.5F, 37F, 63F, 88.5F, 114.5F, 143F, 171.5F, 202.5F, 228.5F, 255.5F, 282F, 309F, 336.5F, 366.5F, 394F, 421F, 448.5F, 474.5F, 500.5F, 530F, 557.5F, 584F, 609F, 629F };
    private static final float radScale[]               = { 0.0F, 3F, 7F, 13.5F, 21.5F, 27F, 34.5F, 50.5F, 71F, 94F, 125F, 161F, 202.5F, 253F, 315.5F };
    private static final float rpmScale[]               = { 0.0F, 0.0F, 0.0F, 22F, 58F, 103.5F, 152.5F, 193.5F, 245F, 281.5F, 311.5F };
    private static final float variometerScale[]        = { -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F };
    private static final float k14TargetMarkScale[]     = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F };
    private static final float k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

    static {
        Property.set(CockpitSpitF22.class, "normZN", 0.72F);
        Property.set(CockpitSpitF22.class, "gsZN", 0.66F);
    }

}
