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

public class CockpitBa65K extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitBa65K.this.fm != null) {
                CockpitBa65K.this.setTmp = CockpitBa65K.this.setOld;
                CockpitBa65K.this.setOld = CockpitBa65K.this.setNew;
                CockpitBa65K.this.setNew = CockpitBa65K.this.setTmp;
                CockpitBa65K.this.setNew.throttle = (0.85F * CockpitBa65K.this.setOld.throttle) + (CockpitBa65K.this.fm.CT.PowerControl * 0.15F);
                CockpitBa65K.this.setNew.prop = (0.85F * CockpitBa65K.this.setOld.prop) + (CockpitBa65K.this.fm.CT.getStepControl() * 0.15F);
                CockpitBa65K.this.setNew.altimeter = CockpitBa65K.this.fm.getAltitude();
                float f = CockpitBa65K.this.waypointAzimuth();
                CockpitBa65K.this.setNew.waypointAzimuth.setDeg(CockpitBa65K.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitBa65K.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                CockpitBa65K.this.setNew.azimuth.setDeg(CockpitBa65K.this.setOld.azimuth.getDeg(1.0F), CockpitBa65K.this.fm.Or.azimut());
                CockpitBa65K.this.setNew.vspeed = ((199F * CockpitBa65K.this.setOld.vspeed) + CockpitBa65K.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    public CockpitBa65K() {
        super("3DO/Cockpit/Ba65K/hier.him", "i16");
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictMix = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.pictManf = 1.0F;
        this.pictFuel = 0.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "mat2_tr", "strum1dmg", "strum1", "strum2dmg", "strum2", "strum4dmg", "strum4", "strumsxdmg", "strumsx" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.5F);
        this.mesh.chunkSetLocate("Blister1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Column", 16F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 8F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Pedals", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", 46.36F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 96F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix", 46.25F * (this.pictMix = (0.85F * this.pictMix) + (0.15F * this.fm.EI.engines[0].getControlMix())), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch1", 54F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flap1", 40F * (this.pictFlap = (0.75F * this.pictFlap) + (0.25F * this.fm.CT.FlapsControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 90F * (this.pictGear = (0.8F * this.pictGear) + (0.2F * this.fm.CT.GearControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim1", -76.5F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilRad1", 91F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilRad2", 111F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 50F, 550F, 0.0F, 10F), CockpitBa65K.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 50F, 550F, 0.0F, 10F), CockpitBa65K.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.cvt(this.setNew.vspeed, -25F, 25F, 180F, -180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, -327F), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Turn1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, -21F, 21F), 0.0F);
        this.mesh.chunkSetAngles("Turn2", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -12F, 12F), 0.0F);
        this.mesh.chunkSetAngles("Z_ManfoldPress", 0.0F, this.pictManf = (0.9F * this.pictManf) + (0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.533288F, 1.33322F, 0.0F, -317F)), 0.0F);
        this.mesh.chunkSetAngles("Z_OilTemp1", 0.0F, -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 30F, 150F, 0.0F, 4F), CockpitBa65K.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress1", 0.0F, -this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 301.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress2", 0.0F, -this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 184F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress1", 0.0F, -this.cvt(this.fm.M.fuel > 1.0F ? 0.32F : 0.0F, 0.0F, 1.0F, 0.0F, 301.5F), 0.0F);
        float f1 = this.cvt(Math.abs(this.fm.Or.getTangage()), 0.0F, 12F, 1.0F, 0.0F);
        this.pictFuel = (0.92F * this.pictFuel) + (0.08F * this.cvt(this.fm.M.fuel, 0.0F, 234F, this.cvt(f1, 0.0F, 1.0F, 0.0F, 24.5F), this.cvt(f1, 0.0F, 1.0F, 215F, 205F)));
        this.mesh.chunkSetAngles("Z_FuelQuantity1", -this.pictFuel, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_EngTemp1", 0.0F, -this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 74F), 0.0F);
        this.mesh.chunkSetAngles("Z_AirPress1", 0.0F, -135F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkVisible("XLampGearUpR", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
            this.mesh.chunkVisible("XLampGearUpL", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
            this.mesh.chunkVisible("XLampGearDownR", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
            this.mesh.chunkVisible("XLampGearDownL", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        } else {
            this.mesh.chunkVisible("XLampGearUpR", false);
            this.mesh.chunkVisible("XLampGearUpL", false);
            this.mesh.chunkVisible("XLampGearDownR", false);
            this.mesh.chunkVisible("XLampGearDownL", false);
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        }
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        float f;
        for (f = (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x))); f <= -180F; f += 180F) {
            ;
        }
        while (f > 180F) {
            f -= 180F;
        }
        return f;
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
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

    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictMix;
    private float              pictFlap;
    private float              pictGear;
    private float              pictManf;
    private float              pictFuel;
    private static final float speedometerScale[] = { 0.0F, 29F, 63.5F, 98.5F, 115.5F, 132.5F, 165.5F, 202.5F, 241F, 280F, 316F };
    private static final float oilTempScale[]     = { 0.0F, 33F, 80F, 153F, 301.5F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
