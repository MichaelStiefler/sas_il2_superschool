package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitHS126 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitHS126.this.setTmp = CockpitHS126.this.setOld;
            CockpitHS126.this.setOld = CockpitHS126.this.setNew;
            CockpitHS126.this.setNew = CockpitHS126.this.setTmp;
            CockpitHS126.this.setNew.throttle = (10F * CockpitHS126.this.setOld.throttle + CockpitHS126.this.fm.CT.PowerControl) / 11F;
            CockpitHS126.this.setNew.vspeed = (199F * CockpitHS126.this.setOld.vspeed + CockpitHS126.this.fm.getVertSpeed()) / 200F;
            float f = CockpitHS126.this.waypointAzimuth();
            if (CockpitHS126.this.useRealisticNavigationInstruments()) {
                CockpitHS126.this.setNew.waypointAzimuth.setDeg(f - 90F);
                CockpitHS126.this.setOld.waypointAzimuth.setDeg(f - 90F);
            } else CockpitHS126.this.setNew.waypointAzimuth.setDeg(CockpitHS126.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitHS126.this.setOld.azimuth.getDeg(1.0F));
            CockpitHS126.this.setNew.azimuth.setDeg(CockpitHS126.this.setOld.azimuth.getDeg(1.0F), CockpitHS126.this.fm.Or.azimut());
            CockpitHS126.this.w.set(CockpitHS126.this.fm.getW());
            CockpitHS126.this.fm.Or.transform(CockpitHS126.this.w);
            CockpitHS126.this.setNew.turn = (12F * CockpitHS126.this.setOld.turn + CockpitHS126.this.w.z) / 13F;
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      turn;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    public CockpitHS126(String arg0, String arg1) {
        super(arg0, arg1);
    }

    public CockpitHS126() {
        super("3DO/Cockpit/Hs126A/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.w = new Vector3f();
        this.cockpitNightMats = new String[] { "gauges1", "gauges2", "gauges3", "gauges4", "gauges5", "gauges6", "gauges1_d1", "gauges2_d1", "gauges3_d1", "gauges4_d1" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkVisible("Z_GearLRed1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_GearRRed1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_GearLGreen1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_GearRGreen1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkSetAngles("Stick", (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 15F, 0.0F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 10F);
        this.resetYPRmodifier();
        if (this.fm.CT.WeaponControl[3]) Cockpit.xyz[1] = -0.01F;
        this.mesh.chunkSetLocate("Fire1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Brake", 0.0F, 0.0F, 11.2F * this.fm.CT.getBrake());
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, 22.2F - 80F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("Throttle_rod", 0.0F, 0.0F, -22.2F + 80F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("Magneto", 0.0F, 0.0F, 16.66667F * this.fm.EI.engines[0].getControlMagnetos());
        this.mesh.chunkSetAngles("Flaps", 0.0F, 0.0F, -50F * (this.pictFlap = 0.85F * this.pictFlap + 0.15F * this.fm.CT.FlapsControl));
        if (this.fm.Gears.isHydroOperable()) this.mesh.chunkSetAngles("Gears", 0.0F, 0.0F, -50F * (this.pictGear = 0.85F * this.pictGear + 0.15F * this.fm.CT.GearControl));
        else this.mesh.chunkSetAngles("H-manual", 0.0F, 0.0F, this.fm.CT.GearControl < 0.1F ? 0.0F : -22.5F);
        this.mesh.chunkSetAngles("Radiator", 0.0F, 0.0F, -50F * (this.pictRadiator = 0.85F * this.pictRadiator + 0.15F * this.fm.EI.engines[0].getControlRadiator()));
        float f1 = Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH());
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, f1 > 100F ? this.cvt(f1, 100F, 800F, 22.5F, 337.5F) : this.cvt(f1, 0.0F, 100F, 0.0F, 22.5F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.fm.getAltitude(), 0.0F, 16000F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1c", 0.0F, this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zTOil1a", 0.0F, (f1 = this.fm.EI.engines[0].tOilOut) > 100F ? 180F + 3.725F * (f1 - 100F) : this.floatindex(this.cvt(f1, 0.0F, 100F, 0.0F, 5F), oilTScale), 0.0F);
        this.mesh.chunkSetAngles("zGasPrs1a", 0.0F, this.floatindex(this.cvt(1.388889F * this.fm.M.fuel, 0.0F, 450F, 0.0F, 9F), fuelScale), 0.0F);
        this.mesh.chunkSetAngles("zTure1a", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 500F, 3000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -20F, 20F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 18F, -18F), 0.0F);
        this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -15F, 15F), 0.0F);
        this.mesh.chunkSetAngles("zPitch1a", 0.0F, 270F - (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60F, 0.0F);
        this.mesh.chunkSetAngles("zPitch1b", 0.0F, 105F - (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5F, 0.0F);
        this.mesh.chunkSetAngles("zPress1a", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.67F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("zPress1b", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 20F, 0.0F, 124F), 0.0F);
        this.mesh.chunkSetAngles("zPressure1a", 0.0F, this.cvt(this.pictManifold = 0.85F * this.pictManifold + 0.15F * this.fm.EI.engines[0].getManifoldPressure(), 0.266644F, 1.866508F, 0.0F, 360F), 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Z_Azimuth1", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.waypointAzimuth.getDeg(f), 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("Z_Azimuth1", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
    }

    protected float waypointAzimuth() {
        this.waypointAzimuthInvertMinus(30F);
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) return 0.0F;
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        float fWP = (float) (57.295779513082323D * Math.atan2(this.tmpV.x, this.tmpV.y));
        for (fWP += World.Rnd().nextFloat(-20F, 20F); fWP < -180F; fWP += 180F)
            ;
        for (; fWP > 180F; fWP -= 180F)
            ;
        return fWP;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("Z_Holes1_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_d1", true);
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_d1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("Z_Holes2_D1", true);
        if ((this.fm.AS.astateCockpitState & 8) != 0);
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("Z_OilSplats_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("Z_Holes2_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictGear;
    private float              pictRadiator;
    private float              pictManifold;
    private Point3d            tmpP;
    private Vector3d           tmpV;
    public Vector3f            w;
    private static final float oilTScale[] = { 0.0F, 32F, 50.05F, 78F, 123.5F, 180F };
    private static final float fuelScale[] = { 0.0F, 10.5F, 30F, 71F, 114F, 148.5F, 175.5F, 202.5F, 232F, 258F };
}
