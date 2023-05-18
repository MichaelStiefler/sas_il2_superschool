package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitSu_15 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitSu_15.this.fm != null) {
                CockpitSu_15.this.setTmp = CockpitSu_15.this.setOld;
                CockpitSu_15.this.setOld = CockpitSu_15.this.setNew;
                CockpitSu_15.this.setNew = CockpitSu_15.this.setTmp;
                CockpitSu_15.this.setNew.throttle = (0.9F * CockpitSu_15.this.setOld.throttle) + (CockpitSu_15.this.fm.CT.PowerControl * 0.1F);
                CockpitSu_15.this.setNew.starter = (0.94F * CockpitSu_15.this.setOld.starter) + (0.06F * ((CockpitSu_15.this.fm.EI.engines[0].getStage() <= 0) || (CockpitSu_15.this.fm.EI.engines[0].getStage() >= 6) ? 0.0F : 1.0F));
                CockpitSu_15.this.setNew.altimeter = CockpitSu_15.this.fm.getAltitude();
                float f = CockpitSu_15.this.waypointAzimuth();
                CockpitSu_15.this.setNew.azimuth.setDeg(CockpitSu_15.this.setOld.azimuth.getDeg(1.0F), CockpitSu_15.this.fm.Or.azimut());
                if (CockpitSu_15.this.useRealisticNavigationInstruments()) {
                    CockpitSu_15.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitSu_15.this.setOld.waypointAzimuth.setDeg(f - 90F);
                    CockpitSu_15.this.setNew.radioCompassAzimuth.setDeg(CockpitSu_15.this.setOld.radioCompassAzimuth.getDeg(0.02F), CockpitSu_15.this.radioCompassAzimuthInvertMinus() - CockpitSu_15.this.setOld.azimuth.getDeg(1.0F) - 90F);
                } else {
                    CockpitSu_15.this.setNew.waypointAzimuth.setDeg(CockpitSu_15.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitSu_15.this.waypointAzimuth() - CockpitSu_15.this.fm.Or.azimut());
                    CockpitSu_15.this.setNew.radioCompassAzimuth.setDeg(CockpitSu_15.this.setOld.radioCompassAzimuth.getDeg(0.1F), f - CockpitSu_15.this.setOld.azimuth.getDeg(0.1F) - 90F);
                }
                CockpitSu_15.this.setNew.vspeed = ((199F * CockpitSu_15.this.setOld.vspeed) + CockpitSu_15.this.fm.getVertSpeed()) / 200F;
                float f1 = ((Sukhoi_15) CockpitSu_15.this.aircraft()).k14Distance;
                CockpitSu_15.this.setNew.k14w = (5F * CockpitSu_15.k14TargetWingspanScale[((Sukhoi_15) CockpitSu_15.this.aircraft()).k14WingspanType]) / f1;
                CockpitSu_15.this.setNew.k14w = (0.9F * CockpitSu_15.this.setOld.k14w) + (0.1F * CockpitSu_15.this.setNew.k14w);
                CockpitSu_15.this.setNew.k14wingspan = (0.9F * CockpitSu_15.this.setOld.k14wingspan) + (0.1F * CockpitSu_15.k14TargetMarkScale[((Sukhoi_15) CockpitSu_15.this.aircraft()).k14WingspanType]);
                CockpitSu_15.this.setNew.k14mode = (0.8F * CockpitSu_15.this.setOld.k14mode) + (0.2F * ((Sukhoi_15) CockpitSu_15.this.aircraft()).k14Mode);
                Vector3d vector3d = CockpitSu_15.this.aircraft().FM.getW();
                double d = 0.00125D * f1;
                float f2 = (float) Math.toDegrees(d * vector3d.z);
                float f3 = -(float) Math.toDegrees(d * vector3d.y);
                float f4 = CockpitSu_15.this.floatindex((f1 - 200F) * 0.04F, CockpitSu_15.k14BulletDrop) - CockpitSu_15.k14BulletDrop[0];
                f3 += (float) Math.toDegrees(Math.atan(f4 / f1));
                CockpitSu_15.this.setNew.k14x = (0.92F * CockpitSu_15.this.setOld.k14x) + (0.08F * f2);
                CockpitSu_15.this.setNew.k14y = (0.92F * CockpitSu_15.this.setOld.k14y) + (0.08F * f3);
                if (CockpitSu_15.this.setNew.k14x > 7F) {
                    CockpitSu_15.this.setNew.k14x = 7F;
                }
                if (CockpitSu_15.this.setNew.k14x < -7F) {
                    CockpitSu_15.this.setNew.k14x = -7F;
                }
                if (CockpitSu_15.this.setNew.k14y > 7F) {
                    CockpitSu_15.this.setNew.k14y = 7F;
                }
                if (CockpitSu_15.this.setNew.k14y < -7F) {
                    CockpitSu_15.this.setNew.k14y = -7F;
                }
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
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
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
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    private float machNumber() {
        return ((Sukhoi_15) this.aircraft()).calculateMach();
    }

    void Init() {
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(300F, 0.0F, 0.0F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(300F, 0.0F, 0.0F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK3");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light3 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light3.light.setColor(300F, 0.0F, 0.0F);
        this.light3.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK3", this.light3);
        this.cockpitNightMats = (new String[] { "gauges1", "gauges2", "gauges3", "gauges4", "gauges5", "instrument" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
        this.FOV = 1.0D;
        this.ScX = 0.002D;
        this.ScY = 0.0000039D;
        this.ScZ = 0.001D;
        this.FOrigX = 0.0F;
        this.FOrigY = 0.0F;
        this.nTgts = 10;
        this.RRange = 20000F;
        this.RClose = 5F;
        this.BRange = 0.1F;
        this.BRefresh = 1300;
        this.BSteps = 12;
        this.BDiv = this.BRefresh / this.BSteps;
        this.tBOld = 0L;
        this.radarPlane = new ArrayList();
        this.radarPlanefriendly = new ArrayList();
        this.radarTracking = new ArrayList();
        this.trackzone = false;
    }

    public CockpitSu_15(String hierFile, String name) {
        super(hierFile, name);
        this.Init();
    }

    public CockpitSu_15() {
        this("3DO/Cockpit/Su-15/hier.him", "bf109");
    }

    public void reflectWorldToInstruments(float f) {
        if ((this.fm.AS.astateCockpitState & 2) == 0) {
            int i = ((Sukhoi_15) this.aircraft()).k14Mode;
            boolean flag = i < 1;
            this.mesh.chunkVisible("Z_Z_RETICLE", flag);
        }
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.6F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("stick", 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 10F);
        this.mesh.chunkSetAngles("leftrudder", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("rightrudder", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("throttle", 0.0F, -40.909F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Acceleration", this.cvt(this.fm.getOverload(), -4.5F, 10F, -110F, 220F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 340F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 10800F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.cvt(this.setNew.vspeed, -20F, 20F, -72F, 72F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("z_Enginespeed1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 20F), CockpitSu_15.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("z_Enginespeed2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 20F), CockpitSu_15.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ExTemp", this.cvt(this.fm.EI.engines[0].tOilOut, 20F, 175F, 0.0F, 265F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuel1", this.cvt(this.fm.M.fuel, 0.0F, this.fm.M.maxFuel, 0.0F, 285F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 0.0F, 50F * (this.pictGear = (0.82F * this.pictGear) + (0.18F * this.fm.CT.GearControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_Horizontal2", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Horizontal1", 1.2F * this.fm.Or.getTangage(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(this.fm.getSpeedKMH(), 200F, 2500F, 0.0F, 24F), CockpitSu_15.speedometerScale), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Turn1", this.cvt(this.w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mach", this.cvt(this.machNumber(), 0.5F, 3F, 0.0F, 349F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 90F + this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 90F + this.setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", -this.setNew.azimuth.getDeg(f) + this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (((Sukhoi_15) this.aircraft()).k14Mode >= 1) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
        } else {
            this.mesh.chunkVisible("Z_Z_RETICLE", true);
        }
        this.radarclutter();
    }

    public void radarclutter() {
        long l = Time.current() + World.Rnd().nextLong(-250L, 250L);
        if (this.fm.getAltitude() > 500F) {
            if ((this.fm.Or.getTangage() < (-20F + (8000F / this.fm.getAltitude()))) && (this.fm.getAltitude() < 5000F)) {
                this.clutter = true;
                if ((l > (this.t1 + 400L)) && (l < (this.t1 + 800L))) {
                    this.mesh.chunkVisible("Radarclutter1", true);
                    this.mesh.chunkVisible("Radarclutter2", false);
                    this.mesh.chunkVisible("Radarclutter3", false);
                } else if ((l > (this.t1 + 800L)) && (l < (this.t1 + 1200L))) {
                    this.mesh.chunkVisible("Radarclutter1", false);
                    this.mesh.chunkVisible("Radarclutter2", true);
                    this.mesh.chunkVisible("Radarclutter3", false);
                } else if (l > (this.t1 + 1200L)) {
                    this.t1 = l;
                    this.mesh.chunkVisible("Radarclutter1", false);
                    this.mesh.chunkVisible("Radarclutter2", false);
                    this.mesh.chunkVisible("Radarclutter3", true);
                }
                for (int i = 0; i <= this.nTgts; i++) {
                    String s1 = "Radarmark0" + i;
                    String s5 = "RadarA0" + i;
                    String s9 = "RadarB0" + i;
                    String s13 = "RadarIFF0" + i;
                    if (this.mesh.isChunkVisible(s13)) {
                        this.mesh.chunkVisible(s13, false);
                    }
                    if (this.mesh.isChunkVisible(s1)) {
                        this.mesh.chunkVisible(s1, false);
                    }
                    if (this.mesh.isChunkVisible(s5)) {
                        this.mesh.chunkVisible(s5, false);
                    }
                    if (this.mesh.isChunkVisible(s9)) {
                        this.mesh.chunkVisible(s9, false);
                    }
                }

                String s2 = "Radarlock";
                String s6 = "RadarLL";
                String s10 = "RadarLR";
                String s14 = "RadarLA";
                String s17 = "RadarLB";
                if (this.mesh.isChunkVisible(s2)) {
                    this.mesh.chunkVisible(s2, false);
                }
                if (this.mesh.isChunkVisible(s6)) {
                    this.mesh.chunkVisible(s6, false);
                }
                if (this.mesh.isChunkVisible(s10)) {
                    this.mesh.chunkVisible(s10, false);
                }
                if (this.mesh.isChunkVisible(s14)) {
                    this.mesh.chunkVisible(s14, false);
                }
                if (this.mesh.isChunkVisible(s17)) {
                    this.mesh.chunkVisible(s17, false);
                }
            } else {
                if (((Sukhoi_15) this.aircraft()).radarmode == 0) {
                    this.clutter = false;
                    this.tracking = true;
                    this.azimult = 0.0D;
                    this.tangage = 0.0D;
                    this.radarscan();
                    String s = "Radarlock";
                    String s3 = "RadarLL";
                    String s7 = "RadarLR";
                    String s11 = "RadarLA";
                    String s15 = "RadarLB";
                    if (this.mesh.isChunkVisible(s)) {
                        this.mesh.chunkVisible(s, false);
                    }
                    if (this.mesh.isChunkVisible(s3)) {
                        this.mesh.chunkVisible(s3, false);
                    }
                    if (this.mesh.isChunkVisible(s7)) {
                        this.mesh.chunkVisible(s7, false);
                    }
                    if (this.mesh.isChunkVisible(s11)) {
                        this.mesh.chunkVisible(s11, false);
                    }
                    if (this.mesh.isChunkVisible(s15)) {
                        this.mesh.chunkVisible(s15, false);
                    }
                    this.mesh.chunkVisible("Radarlockrange", true);
                    this.resetYPRmodifier();
                    Cockpit.xyz[0] = ((Sukhoi_15) this.aircraft()).lockrange;
                    this.mesh.chunkSetLocate("Radarlockrange", Cockpit.xyz, Cockpit.ypr);
                }
                if ((((Sukhoi_15) this.aircraft()).radarmode == 1) && !this.clutter) {
                    for (int j = 0; j <= (this.nTgts + 1); j++) {
                        String s4 = "Radarmark0" + j;
                        String s8 = "RadarA0" + j;
                        String s12 = "RadarB0" + j;
                        String s16 = "RadarIFF0" + j;
                        if (this.mesh.isChunkVisible(s16)) {
                            this.mesh.chunkVisible(s16, false);
                        }
                        if (this.mesh.isChunkVisible(s4)) {
                            this.mesh.chunkVisible(s4, false);
                        }
                        if (this.mesh.isChunkVisible(s8)) {
                            this.mesh.chunkVisible(s8, false);
                        }
                        if (this.mesh.isChunkVisible(s12)) {
                            this.mesh.chunkVisible(s12, false);
                        }
                    }

                    this.mesh.chunkVisible("Radarlockrange", false);
                    this.radartracking();
                }
                this.mesh.chunkVisible("Radarclutter1", false);
                this.mesh.chunkVisible("Radarclutter2", false);
                this.mesh.chunkVisible("Radarclutter3", false);
            }
        }
    }

    public void radarscan() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            long l = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if (l > (this.to + 1450L)) {
                this.radarPlane.clear();
            }
            if (Actor.isValid(aircraft) && Actor.isAlive(aircraft)) {
                if (l > (this.to + 2000L)) {
                    this.to = l;
                    Point3d point3d = aircraft.pos.getAbsPoint();
                    Orient orient = aircraft.pos.getAbsOrient();
                    List list = Engine.targets();
                    int i1 = list.size();
                    for (int k1 = 0; k1 < i1; k1++) {
                        Actor actor = (Actor) list.get(k1);
                        if ((actor instanceof Aircraft) && (actor != World.getPlayerAircraft()) && (actor.getArmy() != World.getPlayerArmy())) {
                            Vector3d vector3d = new Vector3d();
                            vector3d.set(point3d);
                            Point3d point3d1 = new Point3d();
                            point3d1.set(actor.pos.getAbsPoint());
                            point3d1.sub(point3d);
                            orient.transformInv(point3d1);
                            float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                            if ((point3d1.x > this.RClose) && (point3d1.x < ((double) this.RRange - (double) (350F * f))) && (point3d1.y < (point3d1.x * CockpitSu_15.tan25)) && (point3d1.y > (-point3d1.x * CockpitSu_15.tan25)) && (point3d1.z < (point3d1.x * CockpitSu_15.tan10)) && (point3d1.z > (-point3d1.x * CockpitSu_15.tan10))) {
                                this.radarPlane.add(point3d1);
                            }
                        }
                    }

                }
                int i = this.radarPlane.size();
                int j = 0;
                for (int k = 0; k < i; k++) {
                    double d = ((Point3d) this.radarPlane.get(k)).x;
                    if ((d > this.RClose) && (j <= this.nTgts)) {
                        this.FOV = 60D / d;
                        double d1 = -((Point3d) this.radarPlane.get(k)).y * this.FOV;
                        double d2 = ((Point3d) this.radarPlane.get(k)).x;
                        float f1 = this.FOrigX + (float) (d1 * this.ScX);
                        if (f1 > 0.07F) {
                            f1 = 0.07F;
                        }
                        if (f1 < -0.07F) {
                            f1 = -0.07F;
                        }
                        float f2 = this.FOrigY + (float) (d2 * this.ScY);
                        if (f2 > 0.07F) {
                            f2 = 0.07F;
                        }
                        if (f2 < -0.07F) {
                            f2 = -0.07F;
                        }
                        j++;
                        String s3 = "Radarmark0" + j;
                        this.mesh.setCurChunk(s3);
                        this.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                        this.resetYPRmodifier();
                        Cockpit.xyz[1] = -f1;
                        Cockpit.xyz[0] = f2;
                        this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        this.mesh.render();
                        if (!this.mesh.isChunkVisible(s3)) {
                            this.mesh.chunkVisible(s3, true);
                        }
                        String s4 = "RadarA0" + j;
                        String s5 = "RadarB0" + j;
                        if (((Point3d) this.radarPlane.get(k)).z > (((Point3d) this.radarPlane.get(k)).x * 0.0172686779D)) {
                            if (!this.mesh.isChunkVisible(s4)) {
                                this.mesh.chunkVisible(s4, true);
                            }
                            if (this.mesh.isChunkVisible(s5)) {
                                this.mesh.chunkVisible(s5, false);
                            }
                        }
                        if (((Point3d) this.radarPlane.get(k)).z < (-((Point3d) this.radarPlane.get(k)).x * 0.0172686779D)) {
                            if (!this.mesh.isChunkVisible(s5)) {
                                this.mesh.chunkVisible(s5, true);
                            }
                            if (this.mesh.isChunkVisible(s4)) {
                                this.mesh.chunkVisible(s4, false);
                            }
                        }
                        if ((((Point3d) this.radarPlane.get(k)).z >= (-((Point3d) this.radarPlane.get(k)).x * 0.0172686779D)) && (((Point3d) this.radarPlane.get(k)).z <= (((Point3d) this.radarPlane.get(k)).x * 0.0172686779D))) {
                            if (!this.mesh.isChunkVisible(s5)) {
                                this.mesh.chunkVisible(s5, true);
                            }
                            if (!this.mesh.isChunkVisible(s4)) {
                                this.mesh.chunkVisible(s4, true);
                            }
                        }
                    }
                }

                for (int j1 = j + 1; j1 <= this.nTgts; j1++) {
                    String s = "Radarmark0" + j1;
                    String s1 = "RadarA0" + j1;
                    String s2 = "RadarB0" + j1;
                    if (this.mesh.isChunkVisible(s)) {
                        this.mesh.chunkVisible(s, false);
                    }
                    if (this.mesh.isChunkVisible(s1)) {
                        this.mesh.chunkVisible(s1, false);
                    }
                    if (this.mesh.isChunkVisible(s2)) {
                        this.mesh.chunkVisible(s2, false);
                    }
                }

            }
            if ((this.radarPlane.size() == 0) && this.mesh.isChunkVisible("Radarmark00")) {
                this.mesh.chunkVisible("Radarmark00", false);
            }
            if (this.mesh.isChunkVisible("RadarA00")) {
                this.mesh.chunkVisible("RadarA00", false);
            }
            if (this.mesh.isChunkVisible("RadarB00")) {
                this.mesh.chunkVisible("RadarB00", false);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void radarscanIFF() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            long l = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if (l > (this.to + 1450L)) {
                this.radarPlanefriendly.clear();
            }
            if (Actor.isValid(aircraft) && Actor.isAlive(aircraft)) {
                if (l > (this.to + 2000L)) {
                    this.to = l;
                    Point3d point3d = aircraft.pos.getAbsPoint();
                    Orient orient = aircraft.pos.getAbsOrient();
                    List list = Engine.targets();
                    int i1 = list.size();
                    for (int k1 = 0; k1 < i1; k1++) {
                        Actor actor = (Actor) list.get(k1);
                        if ((actor instanceof Aircraft) && (actor != World.getPlayerAircraft()) && (actor.getArmy() != World.getPlayerArmy())) {
                            Vector3d vector3d = new Vector3d();
                            vector3d.set(point3d);
                            Point3d point3d1 = new Point3d();
                            point3d1.set(actor.pos.getAbsPoint());
                            point3d1.sub(point3d);
                            orient.transformInv(point3d1);
                            float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                            if ((point3d1.x > this.RClose) && (point3d1.x < ((double) this.RRange - (double) (350F * f))) && (point3d1.y < (point3d1.x * CockpitSu_15.tan25)) && (point3d1.y > (-point3d1.x * CockpitSu_15.tan25)) && (point3d1.z < (point3d1.x * CockpitSu_15.tan10)) && (point3d1.z > (-point3d1.x * CockpitSu_15.tan10))) {
                                this.radarPlanefriendly.add(point3d1);
                            }
                        }
                    }

                }
                int i = this.radarPlanefriendly.size();
                int j = 0;
                for (int k = 0; k < i; k++) {
                    double d = ((Point3d) this.radarPlanefriendly.get(k)).x;
                    if ((d > this.RClose) && (j <= this.nTgts)) {
                        this.FOV = 60D / d;
                        double d1 = -((Point3d) this.radarPlanefriendly.get(k)).y * this.FOV;
                        double d2 = ((Point3d) this.radarPlanefriendly.get(k)).x;
                        float f1 = this.FOrigX + (float) (d1 * this.ScX);
                        if (f1 > 0.07F) {
                            f1 = 0.07F;
                        }
                        if (f1 < -0.07F) {
                            f1 = -0.07F;
                        }
                        float f2 = this.FOrigY + (float) (d2 * this.ScY);
                        if (f2 > 0.07F) {
                            f2 = 0.07F;
                        }
                        if (f2 < -0.07F) {
                            f2 = -0.07F;
                        }
                        j++;
                        String s1 = "RadarIFF0" + j;
                        this.mesh.setCurChunk(s1);
                        this.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                        this.resetYPRmodifier();
                        Cockpit.xyz[1] = -f1;
                        Cockpit.xyz[0] = f2;
                        this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        this.mesh.render();
                        if (!this.mesh.isChunkVisible(s1)) {
                            this.mesh.chunkVisible(s1, true);
                        }
                    }
                }

                for (int j1 = j + 1; j1 <= this.nTgts; j1++) {
                    String s = "RadarIFF0" + j1;
                    if (this.mesh.isChunkVisible(s)) {
                        this.mesh.chunkVisible(s, false);
                    }
                }

            }
            if ((this.radarPlanefriendly.size() == 0) && this.mesh.isChunkVisible("RadarIFF00")) {
                this.mesh.chunkVisible("RadarIFF00", false);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void radartracking() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            long l = Time.current() + World.Rnd().nextLong(-15L, 15L);
            if (Actor.isValid(aircraft) && Actor.isAlive(aircraft) && ((this.trefresh + 30L) < l)) {
                this.trefresh = l;
                Point3d point3d = aircraft.pos.getAbsPoint();
                Orient orient = aircraft.pos.getAbsOrient();
                this.radarTracking.clear();
                List list = Engine.targets();
                int i = list.size();
                for (int j = 0; j < i; j++) {
                    Actor actor = (Actor) list.get(j);
                    if ((actor instanceof Aircraft) && (actor != World.getPlayerAircraft()) && (actor.getArmy() != World.getPlayerArmy()) && this.tracking) {
                        double d = 0.0D;
                        if ((this.azimult == 0.0D) && (this.tangage == 0.0D)) {
                            d = 0.16397023426D;
                        } else {
                            d = 0.02748866352D;
                        }
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(point3d);
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                        double d3 = 222222.22222222222D;
                        if (!this.trackzone) {
                            if ((point3d1.x < (((((Sukhoi_15) this.aircraft()).lockrange + 0.01F) * d3) - (200F * f))) && (point3d1.x > (((((Sukhoi_15) this.aircraft()).lockrange - 0.01F) * d3) - (200F * f))) && (point3d1.y < (this.tangage + (point3d1.x * d))) && (point3d1.y > (this.tangage - (point3d1.x * d))) && (point3d1.z < (this.azimult + (point3d1.x * d))) && (point3d1.z > (this.azimult - (point3d1.x * d)))) {
                                this.radarTracking.add(point3d1);
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Target acquired");
                                this.trackzone = true;
                            }
                        } else if ((point3d1.x < (((((Sukhoi_15) this.aircraft()).lockrange + 0.01F) * d3) - (200F * f))) && (point3d1.y < (this.tangage + (point3d1.x * d))) && (point3d1.y > (this.tangage - (point3d1.x * d))) && (point3d1.z < (this.azimult + (point3d1.x * d))) && (point3d1.z > (this.azimult - (point3d1.x * d)))) {
                            this.radarTracking.add(point3d1);
                        }
                    }
                }

                int k = this.radarTracking.size();
                int i1 = 0;
                for (int j1 = 0; (j1 < k) && (j1 < 1); j1++) {
                    double d1 = ((Point3d) this.radarTracking.get(j1)).x;
                    this.tangage = ((Point3d) this.radarTracking.get(j1)).y;
                    this.azimult = ((Point3d) this.radarTracking.get(j1)).z;
                    if ((d1 < 18000D) && (i1 <= this.nTgts)) {
                        this.FOV = 1680D / d1;
                        float fd2 = -(float) (((Point3d) this.radarTracking.get(j1)).y * this.FOV);
                        float fd4 = (float) (((Point3d) this.radarTracking.get(j1)).z * this.FOV);
                        float fd5 = (float) (((Point3d) this.radarTracking.get(j1)).x);
                        float f1 = fd2 * 0.0006F;
                        if (f1 > 0.075F) {
                            f1 = 0.07F;
                        }
                        if (f1 < -0.07F) {
                            f1 = -0.07F;
                        }
                        float f2 = fd4 * 0.0004F;
                        if (f2 > 0.07F) {
                            f2 = 0.07F;
                        }
                        if (f2 < -0.07F) {
                            f2 = -0.07F;
                        }
                        float f3 = fd5 * 0.0000029F;
                        if (f3 > 0.07F) {
                            f3 = 0.07F;
                        }
                        if (f3 < -0.07F) {
                            f3 = -0.07F;
                        }
                        float f4 = fd2 * 0.0005F;
                        float f5 = fd4 * 0.0003F;
                        if ((f4 > 0.07F) || (f4 < -0.07F) || (f5 > 0.035F) || (f5 < -0.035F)) {
                            this.tracking = false;
                            this.trackzone = false;
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Target lost");
                        }
                        i1++;
                        String s10 = "Radarlock";
                        this.mesh.setCurChunk(s10);
                        this.resetYPRmodifier();
                        Cockpit.xyz[1] = -f1;
                        Cockpit.xyz[0] = f2;
                        this.mesh.chunkSetLocate(s10, Cockpit.xyz, Cockpit.ypr);
                        this.mesh.render();
                        String s11 = "RadarLL";
                        String s12 = "RadarLR";
                        this.resetYPRmodifier();
                        Cockpit.xyz[0] = f3;
                        this.mesh.chunkSetLocate(s11, Cockpit.xyz, Cockpit.ypr);
                        this.resetYPRmodifier();
                        Cockpit.xyz[0] = -f3;
                        this.mesh.chunkSetLocate(s12, Cockpit.xyz, Cockpit.ypr);
                        if (!this.mesh.isChunkVisible(s10)) {
                            this.mesh.chunkVisible(s10, true);
                        }
                        if (!this.mesh.isChunkVisible(s11)) {
                            this.mesh.chunkVisible(s11, true);
                        }
                        if (!this.mesh.isChunkVisible(s12)) {
                            this.mesh.chunkVisible(s12, true);
                        }
                        String s13 = "RadarLA";
                        String s14 = "RadarLB";
                        if (f2 > 0.005F) {
                            if (!this.mesh.isChunkVisible(s13)) {
                                this.mesh.chunkVisible(s13, true);
                            }
                            if (this.mesh.isChunkVisible(s14)) {
                                this.mesh.chunkVisible(s14, false);
                            }
                        }
                        if (f2 < -0.005F) {
                            if (!this.mesh.isChunkVisible(s14)) {
                                this.mesh.chunkVisible(s14, true);
                            }
                            if (this.mesh.isChunkVisible(s13)) {
                                this.mesh.chunkVisible(s13, false);
                            }
                        }
                        if ((f2 < 0.005F) && (f2 > -0.005F)) {
                            if (!this.mesh.isChunkVisible(s14)) {
                                this.mesh.chunkVisible(s14, true);
                            }
                            if (!this.mesh.isChunkVisible(s13)) {
                                this.mesh.chunkVisible(s13, true);
                            }
                        }
                    } else {
                        String s2 = "Radarlock";
                        String s4 = "RadarLL";
                        String s6 = "RadarLR";
                        String s8 = "RadarLA";
                        String s9 = "RadarLB";
                        if (this.mesh.isChunkVisible(s2)) {
                            this.mesh.chunkVisible(s2, false);
                        }
                        if (this.mesh.isChunkVisible(s4)) {
                            this.mesh.chunkVisible(s4, false);
                        }
                        if (this.mesh.isChunkVisible(s6)) {
                            this.mesh.chunkVisible(s6, false);
                        }
                        if (this.mesh.isChunkVisible(s8)) {
                            this.mesh.chunkVisible(s8, false);
                        }
                        if (this.mesh.isChunkVisible(s9)) {
                            this.mesh.chunkVisible(s9, false);
                        }
                    }
                }

                if (k == 0) {
                    String s = "Radarlock";
                    String s1 = "RadarLL";
                    String s3 = "RadarLR";
                    String s5 = "RadarLA";
                    String s7 = "RadarLB";
                    if (this.mesh.isChunkVisible(s)) {
                        this.mesh.chunkVisible(s, false);
                    }
                    if (this.mesh.isChunkVisible(s1)) {
                        this.mesh.chunkVisible(s1, false);
                    }
                    if (this.mesh.isChunkVisible(s3)) {
                        this.mesh.chunkVisible(s3, false);
                    }
                    if (this.mesh.isChunkVisible(s5)) {
                        this.mesh.chunkVisible(s5, false);
                    }
                    if (this.mesh.isChunkVisible(s7)) {
                        this.mesh.chunkVisible(s7, false);
                    }
                    this.tracking = false;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.006F, 0.4F);
            this.light2.light.setEmit(0.006F, 0.4F);
            this.light3.light.setEmit(0.006F, 0.4F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.light3.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
        if (this.cockpitDimControl) {
            this.mesh.chunkVisible("Z_Z_MASKl", true);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "ZSh-3 Helmet: Visor Down");
        } else {
            this.mesh.chunkVisible("Z_Z_MASKl", false);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "ZSh-3 Helmet: Visor Up");
        }
    }

    private long                t1;
    private boolean             clutter;
    private boolean             tracking;
    private long                trefresh;
    private double              azimult;
    private double              tangage;
    private boolean             trackzone;
    private Variables           setOld;
    private Variables           setNew;
    private Variables           setTmp;
    private float               pictAiler;
    private float               pictElev;
    private float               pictGear;
    private LightPointActor     light1;
    private LightPointActor     light2;
    private LightPointActor     light3;
    public Vector3f             w;
    private long                to;
    double                      FOV;
    double                      ScX;
    double                      ScY;
    double                      ScZ;
    float                       FOrigX;
    float                       FOrigY;
    int                         nTgts;
    float                       RRange;
    float                       RClose;
    float                       BRange;
    int                         BRefresh;
    int                         BSteps;
    float                       BDiv;
    long                        tBOld;
    private ArrayList           radarPlane;
    private ArrayList           radarPlanefriendly;
    private ArrayList           radarTracking;
    protected float             offset;
    private static final float  speedometerScale[]       = { 19F, 55F, 90F, 105F, 118.8F, 131F, 144.2F, 157.8F, 171.4F, 185.2F, 198.5F, 212.1F, 226.3F, 239.8F, 252.1F, 265.7F, 277F, 291.1F, 302.2F, 314.4F, 324F, 335.8F, 346.8F, 359.5F };
    private static final float  rpmScale[]               = { -5F, 29F, 58F, 87F, 116F, 155F, 188F, 196.71F, 205.42F, 214.13F, 222.84F, 231.55F, 240.26F, 248.97F, 257.68F, 266.39F, 275.1F, 283.81F, 292.52F, 301.23F, 310F };
    private static final float  k14TargetMarkScale[]     = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float  k14TargetWingspanScale[] = { 11F, 11.3F, 11.8F, 15F, 20F, 25F, 30F, 35F, 40F, 43.05F };
    private static final float  k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };

    private static final double tan25                    = Math.tan(Math.toRadians(25D));
    private static final double tan10                    = Math.tan(Math.toRadians(10D));
}
