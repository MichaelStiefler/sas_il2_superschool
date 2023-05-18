package com.maddox.il2.objects.air;

import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitHE_115 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitHE_115.this.setTmp = CockpitHE_115.this.setOld;
            CockpitHE_115.this.setOld = CockpitHE_115.this.setNew;
            CockpitHE_115.this.setNew = CockpitHE_115.this.setTmp;
            CockpitHE_115.this.setNew.altimeter = CockpitHE_115.this.fm.getAltitude();
            if (CockpitHE_115.this.cockpitDimControl) {
                if (CockpitHE_115.this.setNew.dimPosition > 0.0F) CockpitHE_115.this.setNew.dimPosition = CockpitHE_115.this.setNew.dimPosition - 0.05F;
            } else if (CockpitHE_115.this.setNew.dimPosition < 1.0F) CockpitHE_115.this.setNew.dimPosition = CockpitHE_115.this.setNew.dimPosition + 0.05F;
            CockpitHE_115.this.setNew.throttle1 = 0.91F * CockpitHE_115.this.setOld.throttle1 + 0.09F * CockpitHE_115.this.fm.EI.engines[0].getControlThrottle();
            CockpitHE_115.this.setNew.throttle2 = 0.91F * CockpitHE_115.this.setOld.throttle2 + 0.09F * CockpitHE_115.this.fm.EI.engines[1].getControlThrottle();
            CockpitHE_115.this.setNew.mix1 = 0.88F * CockpitHE_115.this.setOld.mix1 + 0.12F * CockpitHE_115.this.fm.EI.engines[0].getControlMix();
            CockpitHE_115.this.setNew.mix2 = 0.88F * CockpitHE_115.this.setOld.mix2 + 0.12F * CockpitHE_115.this.fm.EI.engines[1].getControlMix();
            CockpitHE_115.this.setNew.azimuth = CockpitHE_115.this.fm.Or.getYaw();
            if (CockpitHE_115.this.setOld.azimuth > 270F && CockpitHE_115.this.setNew.azimuth < 90F) CockpitHE_115.this.setOld.azimuth -= 360F;
            if (CockpitHE_115.this.setOld.azimuth < 90F && CockpitHE_115.this.setNew.azimuth > 270F) CockpitHE_115.this.setOld.azimuth += 360F;
            CockpitHE_115.this.setNew.waypointAzimuth = (10F * CockpitHE_115.this.setOld.waypointAzimuth + (CockpitHE_115.this.waypointAzimuth() - CockpitHE_115.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
            Variables variables = CockpitHE_115.this.setNew;
            float f = 0.9F * CockpitHE_115.this.setOld.radioalt;
            float f1 = 0.1F;
            float f2 = CockpitHE_115.this.fm.getAltitude();
            World.cur();
            World.land();
            variables.radioalt = f + f1 * (f2 - Landscape.HQ_Air((float) CockpitHE_115.this.fm.Loc.x, (float) CockpitHE_115.this.fm.Loc.y));
            CockpitHE_115.this.setNew.vspeed = (199F * CockpitHE_115.this.setOld.vspeed + CockpitHE_115.this.fm.getVertSpeed()) / 200F;
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float altimeter;
        float throttle1;
        float throttle2;
        float dimPosition;
        float azimuth;
        float waypointAzimuth;
        float mix1;
        float mix2;
        float vspeed;
        float radioalt;

        private Variables() {
        }

    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) return 0.0F;
        else {
            waypoint.getP(Cockpit.P1);
            Cockpit.V.sub(Cockpit.P1, this.fm.Loc);
            return (float) (57.295779513082323D * Math.atan2(Cockpit.V.y, Cockpit.V.x));
        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        boolean flag = hiermesh.isChunkVisible("Engine1_D0") || hiermesh.isChunkVisible("Engine1_D1") || hiermesh.isChunkVisible("Engine1_D2");
        this.mesh.chunkVisible("EnginLeft", flag);
        flag = hiermesh.isChunkVisible("Engine2_D0") || hiermesh.isChunkVisible("Engine2_D1") || hiermesh.isChunkVisible("Engine2_D2");
        this.mesh.chunkVisible("EnginRight", flag);
    }

    public CockpitHE_115() {
        super("3DO/Cockpit/He115/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictManifold1 = 0.0F;
        this.pictManifold2 = 0.0F;
        this.setNew.dimPosition = 0.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.cockpitNightMats = new String[] { "bague1", "bague2", "boitier", "cadran1", "cadran2", "cadran3", "cadran4", "cadran5", "cadran6", "cadran7", "cadran8", "consoledr2", "enggauge", "fils", "gauche", "skala" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.resetYPRmodifier();
        Cockpit.xyz[2] = 0.06815F * this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, -(this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 10F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, -(this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 15F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[1]) Cockpit.xyz[2] = 0.00545F;
        this.mesh.chunkSetLocate("Z_Columnbutton1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = -0.05F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.05F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Throttle1", this.interp(this.setNew.throttle1, this.setOld.throttle1, f) * 52.2F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle2", this.interp(this.setNew.throttle2, this.setOld.throttle2, f) * 52.2F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", this.interp(this.setNew.mix1, this.setOld.mix2, f) * 52.2F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", this.interp(this.setNew.mix1, this.setOld.mix2, f) * 52.2F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pitch1", this.fm.EI.engines[0].getControlProp() * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pitch2", this.fm.EI.engines[1].getControlProp() * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiat1", 0.0F, 0.0F, this.fm.EI.engines[0].getControlRadiator() * 15F);
        this.mesh.chunkSetAngles("Z_Radiat2", 0.0F, 0.0F, this.fm.EI.engines[1].getControlRadiator() * 15F);
        this.mesh.chunkSetAngles("Z_Compass1", this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Azimuth1", -this.interp(this.setNew.waypointAzimuth, this.setOld.waypointAzimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        float f1;
        if (this.aircraft().isFMTrackMirror()) f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        else {
            f1 = this.cvt((this.setNew.azimuth - this.setOld.azimuth) / Time.tickLenFs(), -3F, 3F, 21F, -21F);
            if (this.aircraft().fmTrack() != null) this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
        }
        this.mesh.chunkSetAngles("Z_TurnBank1", f1, 0.0F, 0.0F);
        f1 = this.getBall(4D);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(f1, -4F, 4F, 10F, -10F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(f1, -3.8F, 3.8F, 9F, -9F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank4", this.cvt(f1, -3.3F, 3.3F, 7.5F, -7.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Horizon1", 0.0F, 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("Z_Horizon2", this.cvt(this.fm.Or.getTangage(), -45F, 45F, -23F, 23F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.0F, 12F), variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speed1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ATA1", this.cvt(this.pictManifold1 = 0.75F * this.pictManifold1 + 0.25F * this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ATA2", this.cvt(this.pictManifold2 = 0.75F * this.pictManifold2 + 0.25F * this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", this.cvt(this.interp(this.setNew.radioalt, this.setOld.radioalt, f), 0.0F, 750F, 0.0F, 228.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt3", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 14000F, 0.0F, 313F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 2820F, 0.0F, 66.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 233.09F, 313.09F, -42.5F, 42.4F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 68F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp3", this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 120F, 0.0F, 68F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AirPressure1", 170F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Autopilot1", -this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Autopilot2", this.interp(this.setNew.waypointAzimuth, this.setOld.waypointAzimuth, f), 0.0F, 0.0F);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("HullDamage3", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("HullDamage2", true);
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("HullDamage1", true);
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("XGlassDamage3", true);
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("HullDamage1", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XGlassDamage4", true);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictManifold1;
    private float              pictManifold2;
    private static final float speedometerScale[] = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
    private static final float rpmScale[]         = { 0.0F, 36.5F, 70F, 111F, 149.5F, 186.5F, 233.5F, 282.5F, 308F, 318.5F };
    private static final float variometerScale[]  = { -130.5F, -119.5F, -109.5F, -96F, -83F, -49.5F, 0.0F, 49.5F, 83F, 96F, 109.5F, 119.5F, 130.5F };
}
