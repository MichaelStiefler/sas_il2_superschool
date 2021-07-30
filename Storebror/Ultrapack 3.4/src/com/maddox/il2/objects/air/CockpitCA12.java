package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitCA12 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitCA12.this.fm != null) {
                CockpitCA12.this.setTmp = CockpitCA12.this.setOld;
                CockpitCA12.this.setOld = CockpitCA12.this.setNew;
                CockpitCA12.this.setNew = CockpitCA12.this.setTmp;
                CockpitCA12.this.setNew.throttle = (10F * CockpitCA12.this.setOld.throttle + CockpitCA12.this.fm.CT.PowerControl) / 11F;
                CockpitCA12.this.setNew.altimeter = CockpitCA12.this.fm.getAltitude();
                if (Math.abs(CockpitCA12.this.fm.Or.getKren()) < 30F) CockpitCA12.this.setNew.azimuth = (35F * CockpitCA12.this.setOld.azimuth + -CockpitCA12.this.fm.Or.getYaw()) / 36F;
                if (CockpitCA12.this.setOld.azimuth > 270F && CockpitCA12.this.setNew.azimuth < 90F) CockpitCA12.this.setOld.azimuth -= 360F;
                if (CockpitCA12.this.setOld.azimuth < 90F && CockpitCA12.this.setNew.azimuth > 270F) CockpitCA12.this.setOld.azimuth += 360F;
                CockpitCA12.this.setNew.waypointAzimuth = (10F * CockpitCA12.this.setOld.waypointAzimuth + (CockpitCA12.this.waypointAzimuth() - CockpitCA12.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                CockpitCA12.this.setNew.vspeed = (199F * CockpitCA12.this.setOld.vspeed + CockpitCA12.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float throttle;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;

        private Variables() {
        }

    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) return 0.0F;
        else {
            waypoint.getP(this.tmpP);
            this.tmpV.sub(this.tmpP, this.fm.Loc);
            return (float) (57.295779513082323D * Math.atan2(-this.tmpV.y, this.tmpV.x));
        }
    }

    public CockpitCA12() {
        super("3DO/Cockpit/CA12/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = new String[] { "F2ABoxes", "F2Acables", "F2Agauges", "F2Agauges1", "F2Agauges3", "F2AWindshields", "F2Azegary4" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) ((Interpolate) this.fm).actor).getGunByHookName("_MGUN01");
            this.gun[1] = ((Aircraft) ((Interpolate) this.fm).actor).getGunByHookName("_MGUN02");
        }
        this.resetYPRmodifier();
        this.mesh.chunkVisible("XLampArrest", this.fm.CT.getGear() > 0.99F && this.fm.Gears.cgear);
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.725F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 25F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, 10F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl));
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.pictAiler, -1F, 1.0F, -0.054F, 0.054F);
        this.mesh.chunkSetLocate("Z_ColumnR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Z_ColumnL", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -0.0575F, 0.0575F);
        this.mesh.chunkSetLocate("Z_Pedal_L", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, 0.0575F, -0.0575F);
        this.mesh.chunkSetLocate("Z_Pedal_R", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.EI.engines[0].getStage() > 0 && this.fm.EI.engines[0].getStage() < 3) Cockpit.xyz[1] = 0.02825F;
        this.mesh.chunkSetLocate("Z_Starter", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Ign_Switch", 0.0F, -20F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", 0.0F, 100F * (this.pictThtl = 0.9F * this.pictThtl + 0.1F * this.fm.EI.engines[0].getControlThrottle()), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture", 0.0F, 91.66667F * (this.pictMix = 0.9F * this.pictMix + 0.1F * this.fm.EI.engines[0].getControlMix()), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.pictProp = 0.9F * this.pictProp + 0.1F * this.fm.EI.engines[0].getControlProp(), 0.0F, 1.0F, 0.0F, -0.035F);
        this.mesh.chunkSetLocate("Z_Pitch", Cockpit.xyz, Cockpit.ypr);
        if (this.fm.CT.FlapsControl == 0.0F && this.fm.CT.getFlap() != 0.0F) this.mesh.chunkSetAngles("Z_Flaps", 0.0F, 0.0F, 0.0F);
        else if (this.fm.CT.FlapsControl == 1.0F && this.fm.CT.getFlap() != 1.0F) this.mesh.chunkSetAngles("Z_Flaps", 0.0F, -70F, 0.0F);
        else this.mesh.chunkSetAngles("Z_Flaps", 0.0F, -35F, 0.0F);
        if (this.fm.CT.GearControl == 0.0F && this.fm.CT.getGear() != 0.0F) this.mesh.chunkSetAngles("Z_Gearlever", 0.0F, 4F, 0.0F);
        else if (this.fm.CT.GearControl == 1.0F && this.fm.CT.getGear() != 1.0F) this.mesh.chunkSetAngles("Z_Gearlever", 0.0F, -70F, 0.0F);
        else this.mesh.chunkSetAngles("Z_Gearlever", 0.0F, -35F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass", 90F + this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, -0.135F);
        this.mesh.chunkSetLocate("Z_Gear", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, -0.04155F, 0.0211F);
        this.mesh.chunkSetLocate("Z_Flap", Cockpit.xyz, Cockpit.ypr);
        if (this.gun[0] != null && this.gun[0].haveBullets()) {
            this.mesh.chunkSetAngles("Z_Ammo_W1", 0.0F, -0.36F * this.gun[0].countBullets(), 0.0F);
            this.mesh.chunkSetAngles("Z_Ammo_W2", 0.0F, -3.6F * this.gun[0].countBullets(), 0.0F);
            this.mesh.chunkSetAngles("Z_Ammo_W3", 0.0F, -36F * this.gun[0].countBullets(), 0.0F);
        }
        if (this.gun[1] != null && this.gun[1].haveBullets()) {
            this.mesh.chunkSetAngles("Z_Ammo_W4", 0.0F, -0.36F * this.gun[1].countBullets(), 0.0F);
            this.mesh.chunkSetAngles("Z_Ammo_W5", 0.0F, -3.6F * this.gun[1].countBullets(), 0.0F);
            this.mesh.chunkSetAngles("Z_Ammo_W6", 0.0F, -36F * this.gun[1].countBullets(), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Manifold", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 1.693189F, 20F, 340F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Alt_Large", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Alt_Small", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 981.5598F, 0.0F, 53F), speedometerScale), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Turn", this.cvt(this.w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Bank", this.cvt(this.getBall(8D), -8F, 8F, 12F, -12F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb", this.cvt(this.setNew.vspeed, -20F, 20F, -180F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Clock_Min", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Clock_H", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hor_Handle", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.019F, -0.019F);
        this.mesh.chunkSetLocate("Z_Hor_Handle2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Temp_Handle", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 400F, 0.0F, 100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp_Eng", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 170F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil_Eng", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel_Eng", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 10F * this.fm.EI.engines[0].getPowerOutput(), 0.0F, 20F, 0.0F, 180F), 0.0F, 0.0F);
        if (this.fm.EI.engines[0].getRPM() < 1000F) this.mesh.chunkSetAngles("Z_Tahometr_Eng", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 1000F, 0.0F, 90F), 0.0F, 0.0F);
        else this.mesh.chunkSetAngles("Z_Tahometr_Eng", this.cvt(this.fm.EI.engines[0].getRPM(), 1000F, 3500F, 90F, 540F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Carbmixtemp", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 213.09F, 333.09F, -180F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel_1", this.cvt(this.fm.M.fuel, 0.0F, 504F, 0.0F, -272.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel_2", this.cvt(this.fm.M.fuel, 0.0F, 504F, 0.0F, -272.5F), 0.0F, 0.0F);
        if (this.fm.Gears.isHydroOperable()) this.mesh.chunkSetAngles("Z_Hydropress", 133F, 0.0F, 0.0F);
        else this.mesh.chunkSetAngles("Z_Hydropress", 1.0F, 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("Bullethole_1", true);
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("Bullethole_2", true);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    private Gun                gun[]              = { null, null };
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictThtl;
    private float              pictMix;
    private float              pictProp;
    private static final float speedometerScale[] = { 0.0F, 0.5F, 1.0F, 2.0F, 5.5F, 14F, 20F, 26F, 33.5F, 42F, 50.5F, 60.5F, 71.5F, 81.5F, 95.2F, 108.5F, 122.5F, 137F, 152F, 166.7F, 182F, 198F, 214.5F, 231F, 247.5F, 263.5F, 278.5F, 294F, 307F, 317F,
            330.5F, 343F, 355.5F, 367.5F, 379.5F, 391.5F, 404F, 417F, 430.5F, 444F, 458F, 473.5F, 487.5F, 503.5F, 519.5F, 535.5F, 552F, 569.5F, 586F, 602.5F, 619F, 631.5F, 643F, 648.5F };
    private Point3d            tmpP;
    private Vector3d           tmpV;
}
