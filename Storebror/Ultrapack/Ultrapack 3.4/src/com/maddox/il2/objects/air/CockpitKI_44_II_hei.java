package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitKI_44_II_hei extends CockpitPilot {
    private class Variables {

        float      dimPos;
        float      emdimPos;
        float      throttle;
        float      prop;
        float      mix;
        float      altimeter;
        float      man;
        float      vspeed;
        float      manifold;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork waypointDeviation;

        private Variables() {
            this.dimPos = 0.0F;
            this.emdimPos = 0.0F;
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.waypointDeviation = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitKI_44_II_hei.this.fm != null) {
                CockpitKI_44_II_hei.this.setTmp = CockpitKI_44_II_hei.this.setOld;
                CockpitKI_44_II_hei.this.setOld = CockpitKI_44_II_hei.this.setNew;
                CockpitKI_44_II_hei.this.setNew = CockpitKI_44_II_hei.this.setTmp;
                if (CockpitKI_44_II_hei.this.cockpitDimControl) {
                    if (CockpitKI_44_II_hei.this.setNew.dimPos < 1.0F) {
                        CockpitKI_44_II_hei.this.setNew.dimPos = CockpitKI_44_II_hei.this.setOld.dimPos + 0.03F;
                    }
                } else if (CockpitKI_44_II_hei.this.setNew.dimPos > 0.0F) {
                    CockpitKI_44_II_hei.this.setNew.dimPos = CockpitKI_44_II_hei.this.setOld.dimPos - 0.03F;
                }
                if ((CockpitKI_44_II_hei.this.fm.AS.astateCockpitState & 2) != 0) {
                    CockpitKI_44_II_hei.this.setNew.emdimPos = CockpitKI_44_II_hei.this.setOld.emdimPos + 0.03F;
                    if (CockpitKI_44_II_hei.this.setNew.emdimPos > 1.0F) {
                        CockpitKI_44_II_hei.this.setNew.emdimPos = 1.0F;
                    }
                }
                CockpitKI_44_II_hei.this.setNew.manifold = (0.8F * CockpitKI_44_II_hei.this.setOld.manifold) + (0.2F * CockpitKI_44_II_hei.this.fm.EI.engines[0].getManifoldPressure());
                CockpitKI_44_II_hei.this.setNew.throttle = (0.8F * CockpitKI_44_II_hei.this.setOld.throttle) + (0.2F * CockpitKI_44_II_hei.this.fm.CT.PowerControl);
                CockpitKI_44_II_hei.this.setNew.prop = (0.8F * CockpitKI_44_II_hei.this.setOld.prop) + (0.2F * CockpitKI_44_II_hei.this.fm.EI.engines[0].getControlProp());
                CockpitKI_44_II_hei.this.setNew.mix = (0.8F * CockpitKI_44_II_hei.this.setOld.mix) + (0.2F * CockpitKI_44_II_hei.this.fm.EI.engines[0].getControlMix());
                CockpitKI_44_II_hei.this.setNew.man = (0.92F * CockpitKI_44_II_hei.this.setOld.man) + (0.08F * CockpitKI_44_II_hei.this.fm.EI.engines[0].getManifoldPressure());
                CockpitKI_44_II_hei.this.setNew.altimeter = CockpitKI_44_II_hei.this.fm.getAltitude();
                float f = CockpitKI_44_II_hei.this.waypointAzimuth();
                CockpitKI_44_II_hei.this.setNew.azimuth.setDeg(CockpitKI_44_II_hei.this.setOld.azimuth.getDeg(1.0F), CockpitKI_44_II_hei.this.fm.Or.azimut());
                CockpitKI_44_II_hei.this.setNew.waypointDeviation.setDeg(CockpitKI_44_II_hei.this.setOld.waypointDeviation.getDeg(0.1F), (f - CockpitKI_44_II_hei.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-5F, 5F));
                CockpitKI_44_II_hei.this.setNew.waypointAzimuth.setDeg(CockpitKI_44_II_hei.this.setOld.waypointAzimuth.getDeg(1.0F), f);
                CockpitKI_44_II_hei.this.setNew.vspeed = (0.5F * CockpitKI_44_II_hei.this.setOld.vspeed) + (0.5F * CockpitKI_44_II_hei.this.fm.getVertSpeed());
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        }
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        float f = (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x)));
        while (f > 180F) {
            f -= 360F;
        }
        while (f <= -180F) {
            f += 360F;
        }
        return f;
    }

    public CockpitKI_44_II_hei() {
        super("3DO/Cockpit/Ki-44-II-hei/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "D_gauge", "D_gauge1", "D_gauge2", "D_gauge4", "D_gauge5", "gauge", "gauge1", "gauge2", "gauge3", "gauge4", "gauge5", "gauge6", "radio" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.59F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Radiat", 0.0F, -450F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 70F * this.setNew.throttle, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPitch1", 77F * this.setNew.prop, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMix1", 64.1F * this.setNew.mix, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalBase", 30F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 30F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 30F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 8F, 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[0]) {
            Cockpit.xyz[2] = 0.0036F;
        }
        this.mesh.chunkSetLocate("Z_Trigger1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.0F;
        if (this.fm.CT.saveWeaponControl[1]) {
            Cockpit.xyz[2] = 0.00675F;
        }
        this.mesh.chunkSetLocate("Z_Trigger2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Flaps", 90F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ReViTinter", 0.0F, this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, 90F), 0.0F);
        this.mesh.chunkSetAngles("Sight_rev", 0.0F, this.cvt(this.interp(this.setNew.emdimPos, this.setOld.emdimPos, f), 0.0F, 1.0F, 0.0F, 170F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 1440F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 14400F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 700F, 0.0F, 14F), CockpitKI_44_II_hei.speedometerScale), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(8D), -8F, 8F, -14F, 14F), 0.0F, 0.0F);
        float f1 = this.setNew.vspeed;
        if (Math.abs(f1) < 5F) {
            this.mesh.chunkSetAngles("Z_Climb1", this.cvt(f1, -5F, 5F, -90F, 90F), 0.0F, 0.0F);
        } else if (f1 > 0.0F) {
            this.mesh.chunkSetAngles("Z_Climb1", this.cvt(f1, 5F, 30F, 90F, 180F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Climb1", this.cvt(f1, -30F, -5F, -180F, -90F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Compass2", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3200F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel3", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 150F, 0.0F, 241F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, 255F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel4", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 1.0F, 0.0F, -330F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.setNew.manifold, 0.466712F, 1.533288F, -162.5F, 162.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 68.25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 5.5F, 0.0F, 295.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 76.8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 200F, 0.0F, 64F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hydpres1", 0.0F, this.fm.Gears.isHydroOperable() ? -176F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oxypres1", 220F, 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_Red1", this.fm.CT.getGear() < 0.01F);
        this.mesh.chunkVisible("Z_Red2", true);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() > 0.99F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Pricel_D0", false);
            this.mesh.chunkVisible("Pricel_D1", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("Z_Pres1", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_Oilpres1", false);
            this.mesh.chunkVisible("Z_Temp1", false);
            this.mesh.chunkVisible("Z_Temp2", false);
            this.mesh.chunkVisible("Z_Gunpres1", false);
            this.mesh.chunkVisible("Z_Hydpres1", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Pilot2"));
        this.mesh.materialReplace("Pilot2", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("Pilot2_D0", hiermesh.isChunkVisible("Pilot2_D0"));
        this.mesh.chunkVisible("Pilot2_D1", hiermesh.isChunkVisible("Pilot2_D1"));
        this.mesh.chunkVisible("Turret1B_D0", hiermesh.isChunkVisible("Turret1B_D0"));
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 7F, 22F, 61.5F, 116F, 175.5F, 241F, 298.5F, 356.7F, 417.5F, 480.5F, 537F, 585F, 628.5F, 658F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
