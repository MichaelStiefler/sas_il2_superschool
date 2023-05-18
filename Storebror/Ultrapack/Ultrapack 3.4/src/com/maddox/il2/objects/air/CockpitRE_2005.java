package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitRE_2005 extends CockpitPilot {
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

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitRE_2005.this.fm != null) {
                CockpitRE_2005.this.setTmp = CockpitRE_2005.this.setOld;
                CockpitRE_2005.this.setOld = CockpitRE_2005.this.setNew;
                CockpitRE_2005.this.setNew = CockpitRE_2005.this.setTmp;
                CockpitRE_2005.this.setNew.throttle = (0.85F * CockpitRE_2005.this.setOld.throttle) + (CockpitRE_2005.this.fm.CT.PowerControl * 0.15F);
                CockpitRE_2005.this.setNew.prop = (0.85F * CockpitRE_2005.this.setOld.prop) + (CockpitRE_2005.this.fm.CT.getStepControl() * 0.15F);
                CockpitRE_2005.this.setNew.altimeter = CockpitRE_2005.this.fm.getAltitude();
                float f = CockpitRE_2005.this.waypointAzimuth();
                CockpitRE_2005.this.setNew.waypointAzimuth.setDeg(CockpitRE_2005.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitRE_2005.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                CockpitRE_2005.this.setNew.azimuth.setDeg(CockpitRE_2005.this.setOld.azimuth.getDeg(1.0F), CockpitRE_2005.this.fm.Or.azimut());
                CockpitRE_2005.this.setNew.vspeed = ((199F * CockpitRE_2005.this.setOld.vspeed) + CockpitRE_2005.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitRE_2005() {
        super("3DO/Cockpit/RE2005/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.pictManf = 1.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "mat2_tr", "strum1dmg", "strum2dmg", "strum4dmg", "strum1", "strum2", "strum4", "strumsxdmg", "strumsx", "ruddersystem" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN02");
            this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN03");
            this.gun[3] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN04");
            if (this.gun[2].countBullets() <= 0) {
                this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON01");
                this.gun[3] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
            }
        }
        this.mesh.chunkVisible("Z_Z_RETICLE", !this.cockpitDimControl);
        this.mesh.chunkVisible("Z_Z_RETICLE2", this.cockpitDimControl);
        this.mesh.chunkSetAngles("Z_Column", 16F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 8F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Pedals", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", -49.54F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 14F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch1", -61.5F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch2", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flap1", 45F * (this.pictFlap = (0.75F * this.pictFlap) + (0.25F * this.fm.CT.FlapsControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", -32F * (this.pictGear = (0.8F * this.pictGear) + (0.2F * this.fm.CT.GearControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim1", -76.5F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, -0.06325F);
        this.mesh.chunkSetLocate("Z_OilRad1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = this.cvt(this.fm.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, -0.01625F);
        this.mesh.chunkSetLocate("Z_OilRad2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_OilRad3", 0.0F, 0.0F, 0.0F);
        if (this.gun[0].haveBullets()) {
            this.mesh.chunkSetAngles("Z_AmmoCounter1", 0.36F * this.gun[0].countBullets(), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_AmmoCounter2", 3.6F * this.gun[0].countBullets(), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_AmmoCounter3", 36F * this.gun[0].countBullets(), 0.0F, 0.0F);
        }
        if (this.gun[1].haveBullets()) {
            this.mesh.chunkSetAngles("Z_AmmoCounter4", 0.36F * this.gun[1].countBullets(), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_AmmoCounter5", 3.6F * this.gun[1].countBullets(), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_AmmoCounter6", 36F * this.gun[1].countBullets(), 0.0F, 0.0F);
        }
        if (this.gun[2].haveBullets()) {
            this.mesh.chunkSetAngles("Z_AmmoCounter7", this.cvt(this.gun[2].countBullets(), 0.0F, 500F, 7F, 358F), 0.0F, 0.0F);
        }
        if (this.gun[3].haveBullets()) {
            this.mesh.chunkSetAngles("Z_AmmoCounter8", this.cvt(this.gun[3].countBullets(), 0.0F, 500F, 7F, 358F), 0.0F, 0.0F);
        }
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, -0.06055F);
        this.mesh.chunkSetLocate("Z_Cooling", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, 0.05475F);
        this.mesh.chunkSetLocate("Z_FlapPos", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 100F, 700F, 0.0F, 12F), CockpitRE_2005.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.cvt(this.setNew.vspeed, -25F, 25F, -180F, 180F), 0.0F, 0.0F);
        float f1 = this.setNew.azimuth.getDeg(f) - 90F;
        if (f1 < 0.0F) {
            f1 += 360F;
        }
        this.mesh.chunkSetAngles("Z_Compass1", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 322.5F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Turn1", this.cvt(this.w.z, -0.23562F, 0.23562F, 21F, -21F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turn2", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -11F, 11F), 0.0F);
        this.mesh.chunkSetAngles("Z_ManfoldPress", -(this.pictManf = (0.9F * this.pictManf) + (0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.5F, 2.0F, 0.0F, -300F))), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_EngTemp1", this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 30F, 130F, 0.0F, 10F), CockpitRE_2005.waterTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilTemp1", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 30F, 150F, 0.0F, 4F), CockpitRE_2005.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 184F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AirPress1", -135F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AirPress2", -135F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress1", this.cvt(this.fm.M.fuel > 1.0F ? 0.32F : 0.0F, 0.0F, 1.0F, 0.0F, -300F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.M.fuel, 0.0F, 252F, 0.0F, 0.3661F);
        this.mesh.chunkSetLocate("Z_FuelQuantity1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0135F, -0.0135F);
        this.mesh.chunkSetLocate("Z_Horison1c", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Horison1a", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Horison1b", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkVisible("XLampGunL", this.gun[0].countBullets() < 90);
        this.mesh.chunkVisible("XLampGunR", this.gun[1].countBullets() < 90);
        this.mesh.chunkVisible("XLampCanL", this.gun[2].countBullets() < 90);
        this.mesh.chunkVisible("XLampCanR", this.gun[3].countBullets() < 90);
        this.mesh.chunkVisible("XLampSPIA", this.fm.EI.engines[0].getRPM() < 1200F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkVisible("XLampGearUpR", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
            this.mesh.chunkVisible("XLampGearUpL", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
            this.mesh.chunkVisible("XLampGearDownR", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
            this.mesh.chunkVisible("XLampGearDownL", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
            this.mesh.chunkVisible("XLampGearDownC", this.fm.CT.getGear() > 0.99F);
        } else {
            this.mesh.chunkVisible("XLampGearUpR", false);
            this.mesh.chunkVisible("XLampGearUpL", false);
            this.mesh.chunkVisible("XLampGearDownR", false);
            this.mesh.chunkVisible("XLampGearDownL", false);
            this.mesh.chunkVisible("XLampGearDownC", false);
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

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictGear;
    private float              pictManf;
    private BulletEmitter      gun[]              = { null, null, null, null };
    private static final float speedometerScale[] = { 0.0F, 38.5F, 77F, 100.5F, 125F, 147F, 169.5F, 193.5F, 216.5F, 240.5F, 265F, 287.5F, 303.5F };
    private static final float waterTempScale[]   = { 0.0F, 20.5F, 37F, 52F, 73.5F, 95.5F, 120F, 150F, 192F, 245F, 302F };
    private static final float oilTempScale[]     = { 0.0F, 33F, 80F, 153F, 301.5F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
