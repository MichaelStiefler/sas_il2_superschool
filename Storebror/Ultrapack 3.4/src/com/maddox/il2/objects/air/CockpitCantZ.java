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

public class CockpitCantZ extends CockpitPilot {
    private class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitCantZ.this.fm != null) {
                CockpitCantZ.this.setTmp = CockpitCantZ.this.setOld;
                CockpitCantZ.this.setOld = CockpitCantZ.this.setNew;
                CockpitCantZ.this.setNew = CockpitCantZ.this.setTmp;
                CockpitCantZ.this.setNew.throttle1 = (0.9F * CockpitCantZ.this.setOld.throttle1) + (0.1F * CockpitCantZ.this.fm.EI.engines[0].getControlThrottle());
                CockpitCantZ.this.setNew.prop1 = (0.9F * CockpitCantZ.this.setOld.prop1) + (0.1F * CockpitCantZ.this.fm.EI.engines[0].getControlProp());
                CockpitCantZ.this.setNew.mix1 = (0.8F * CockpitCantZ.this.setOld.mix1) + (0.2F * CockpitCantZ.this.fm.EI.engines[0].getControlMix());
                CockpitCantZ.this.setNew.man1 = (0.92F * CockpitCantZ.this.setOld.man1) + (0.08F * CockpitCantZ.this.fm.EI.engines[0].getManifoldPressure());
                CockpitCantZ.this.setNew.throttle2 = (0.9F * CockpitCantZ.this.setOld.throttle2) + (0.1F * CockpitCantZ.this.fm.EI.engines[1].getControlThrottle());
                CockpitCantZ.this.setNew.prop2 = (0.9F * CockpitCantZ.this.setOld.prop2) + (0.1F * CockpitCantZ.this.fm.EI.engines[1].getControlProp());
                CockpitCantZ.this.setNew.mix2 = (0.8F * CockpitCantZ.this.setOld.mix2) + (0.2F * CockpitCantZ.this.fm.EI.engines[1].getControlMix());
                CockpitCantZ.this.setNew.man2 = (0.92F * CockpitCantZ.this.setOld.man2) + (0.08F * CockpitCantZ.this.fm.EI.engines[1].getManifoldPressure());
                CockpitCantZ.this.setNew.altimeter = CockpitCantZ.this.fm.getAltitude();
                CockpitCantZ.this.setNew.azimuth.setDeg(CockpitCantZ.this.setOld.azimuth.getDeg(1.0F), CockpitCantZ.this.fm.Or.azimut());
                CockpitCantZ.this.setNew.vspeed = ((100F * CockpitCantZ.this.setOld.vspeed) + CockpitCantZ.this.fm.getVertSpeed()) / 101F;
                float f = CockpitCantZ.this.waypointAzimuth();
                CockpitCantZ.this.setNew.waypointAzimuth.setDeg(CockpitCantZ.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitCantZ.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                CockpitCantZ.this.setNew.waypointDirection.setDeg(CockpitCantZ.this.setOld.waypointDirection.getDeg(1.0F), f);
                CockpitCantZ.this.setNew.inert = (0.999F * CockpitCantZ.this.setOld.inert) + (0.001F * (CockpitCantZ.this.fm.EI.engines[0].getStage() == 6 ? 0.867F : 0.0F));
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      prop1;
        float      prop2;
        float      mix1;
        float      mix2;
        float      man1;
        float      man2;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork waypointDirection;
        float      vspeed;
        float      inert;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.waypointDirection = new AnglesFork();
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((CantZ506B) this.fm.actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Interior_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            ((CantZ506B) this.fm.actor).bPitUnfocused = true;
            this.aircraft().hierMesh().chunkVisible("Interior_D0", true);
            super.doFocusLeave();
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
        while (f <= -180F) {
            f += 360F;
        }
        while (f > 180F) {
            f -= 360F;
        }
        return f;
    }

    public CockpitCantZ() {
        super("3DO/Cockpit/CantZ/hier.him", "he111");
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "GP1", "GP2", "GP_II_DM", "GP_III_DM", "GP3", "GP_IV_DM", "GP_IV", "GP4", "GP5", "GP6", "GP7", "GP8", "GP9", "compass", "instr", "Ita_Needles", "gauges5", "throttle", "Eqpt_II", "Trans_II", "Trans_VI_Pilot", "Trans_VII_Pilot" });
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
        this.mesh.chunkSetAngles("Z_Throtle1", -45F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", -45F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", -45F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", -45F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -0.095F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -Cockpit.xyz[1];
        this.mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Columnbase", -8F * (this.pictElev = (0.65F * this.pictElev) + (0.35F * this.fm.CT.ElevatorControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", -45F * (this.pictAiler = (0.65F * this.pictAiler) + (0.35F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter3", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter4", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitCantZ.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitCantZ.speedometerScale), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.018F, -0.018F);
        this.mesh.chunkSetLocate("Z_TurnBank1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_TurnBank1Q", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.w.z, -0.23562F, 0.23562F, -27F, 27F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.getBall(7D), -7F, 7F, 10F, -10F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.cvt(this.setNew.vspeed, -30F, 30F, 180F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 15F, 345F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3000F, 15F, 345F), 0.0F, 0.0F);
        float f1 = 0.0F;
        if (this.fm.M.fuel > 1.0F) {
            f1 = this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 570F, 0.0F, 0.26F);
        }
        this.mesh.chunkSetAngles("Z_FuelPres1", this.cvt(f1, 0.0F, 1.0F, 0.0F, -270F), 0.0F, 0.0F);
        f1 = 0.0F;
        if (this.fm.M.fuel > 1.0F) {
            f1 = this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 570F, 0.0F, 0.26F);
        }
        this.mesh.chunkSetAngles("Z_FuelPres2", this.cvt(f1, 0.0F, 1.0F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.setNew.man1, 0.33339F, 1.38661F, -162F, 162F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres2", this.cvt(this.setNew.man1, 0.33339F, 1.38661F, -162F, 162F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FlapPos", this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkVisible("XRGearUp", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLGearUp", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("XRGearDn", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLGearDn", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkSetAngles("Zfuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 3117F, 0.0F, 245F), 0.0F);
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
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 10.5F, 42.5F, 85F, 125F, 165.5F, 181F, 198F, 214.5F, 231F, 249F, 266.5F, 287.5F, 308F, 326.5F, 346F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
