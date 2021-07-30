package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitFI_156 extends CockpitPilot {
    private class Variables {

        float altimeter;
        float throttle;
        float dimPosition;
        float azimuth;
        float waypointAzimuth;
        float mix;
        float vspeed;

        private Variables() {
        }

        Variables(Variables variables) {
            this();
        }
    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitFI_156.this.bNeedSetUp) {
                CockpitFI_156.this.reflectPlaneMats();
                CockpitFI_156.this.bNeedSetUp = false;
            }
            CockpitFI_156.this.setTmp = CockpitFI_156.this.setOld;
            CockpitFI_156.this.setOld = CockpitFI_156.this.setNew;
            CockpitFI_156.this.setNew = CockpitFI_156.this.setTmp;
            CockpitFI_156.this.setNew.altimeter = CockpitFI_156.this.fm.getAltitude();
            if (CockpitFI_156.this.cockpitDimControl) {
                if (CockpitFI_156.this.setNew.dimPosition > 0.0F) CockpitFI_156.this.setNew.dimPosition = CockpitFI_156.this.setOld.dimPosition - 0.05F;
            } else if (CockpitFI_156.this.setNew.dimPosition < 1.0F) CockpitFI_156.this.setNew.dimPosition = CockpitFI_156.this.setOld.dimPosition + 0.05F;
            CockpitFI_156.this.setNew.throttle = (10F * CockpitFI_156.this.setOld.throttle + CockpitFI_156.this.fm.CT.PowerControl) / 11F;
            CockpitFI_156.this.setNew.mix = (8F * CockpitFI_156.this.setOld.mix + CockpitFI_156.this.fm.EI.engines[0].getControlMix()) / 9F;
            CockpitFI_156.this.setNew.azimuth = CockpitFI_156.this.fm.Or.getYaw();
            if (CockpitFI_156.this.setOld.azimuth > 270F && CockpitFI_156.this.setNew.azimuth < 90F) CockpitFI_156.this.setOld.azimuth -= 360F;
            if (CockpitFI_156.this.setOld.azimuth < 90F && CockpitFI_156.this.setNew.azimuth > 270F) CockpitFI_156.this.setOld.azimuth += 360F;
            CockpitFI_156.this.setNew.waypointAzimuth = (10F * CockpitFI_156.this.setOld.waypointAzimuth + (CockpitFI_156.this.waypointAzimuth() - CockpitFI_156.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
            CockpitFI_156.this.buzzerFX(CockpitFI_156.this.fm.CT.getGear() < 0.999999F && CockpitFI_156.this.fm.CT.getFlap() > 0.0F);
            CockpitFI_156.this.setNew.vspeed = (199F * CockpitFI_156.this.setOld.vspeed + CockpitFI_156.this.fm.getVertSpeed()) / 200F;
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) return 0.0F;
        else {
            waypoint.getP(this.tmpP);
            this.tmpV.sub(this.tmpP, this.fm.Loc);
            return (float) (57.295779513082323D * Math.atan2(this.tmpV.y, this.tmpV.x));
        }
    }

    public CockpitFI_156() {
        super("3DO/Cockpit/FI_156/hier.him", "bf109");
        this.setOld = new Variables(null);
        this.setNew = new Variables(null);
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
//        this.pictManifold = 0.0F;
        this.bNeedSetUp = true;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.setNew.dimPosition = 1.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.cockpitNightMats = new String[] { "ZClocks1", "ZClocks1DMG", "ZClocks2", "ZClocks3", "FW190A4Compass", "oxigen" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
//        this.mesh.chunkSetAngles("Z_ATA1", 15.5F + this.cvt(this.pictManifold = 0.75F * this.pictManifold + 0.25F * this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 336F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelQuantity1", -44.5F + this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 8F), fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Iengtemprad1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 58.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_EngTemp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 58.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress1", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        float f1;
        if (this.aircraft().isFMTrackMirror()) f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        else {
            f1 = this.cvt((this.setNew.azimuth - this.setOld.azimuth) / Time.tickLenFs(), -3F, 3F, 30F, -30F);
            if (this.aircraft().fmTrack() != null) this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
        }
        this.mesh.chunkSetAngles("Z_TurnBank1", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(6D), -6F, 6F, -7F, 7F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVSI", this.cvt(this.setNew.vspeed, -15F, 15F, -160F, 160F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Azimuth1", -this.interp(this.setNew.waypointAzimuth, this.setOld.waypointAzimuth, f), 0.0F, 0.0F);
//        this.mesh.chunkSetAngles("Z_PropPitch1", 270F - (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60F, 0.0F, 0.0F);
//        this.mesh.chunkSetAngles("Z_PropPitch2", 105F - (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5F, 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_FuelRed1", this.fm.M.fuel < 36F);
//        this.mesh.chunkVisible("Z_GearLRed1", this.fm.CT.getGear() == 0.0F);
//        this.mesh.chunkVisible("Z_GearRRed1", this.fm.CT.getGear() == 0.0F);
//        this.mesh.chunkVisible("Z_GearLGreen1", this.fm.CT.getGear() == 1.0F);
//        this.mesh.chunkVisible("Z_GearRGreen1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 15F, 0.0F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 10F);
        this.mesh.chunkSetAngles("Z_PedalStrut", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalStrut2", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", this.interp(this.setNew.throttle, this.setOld.throttle, f) * 57.72727F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle_D1", this.interp(this.setNew.throttle, this.setOld.throttle, f) * 27F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle_tube", -this.interp(this.setNew.throttle, this.setOld.throttle, f) * 57.72727F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix", this.interp(this.setNew.mix, this.setOld.mix, f) * 70F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix_tros", -this.interp(this.setNew.mix, this.setOld.mix, f) * 70F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", -45F + 28.333F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("CF_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("CF_D0", true);
        super.doFocusLeave();
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
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("Z_Throttle", false);
            this.mesh.chunkVisible("Z_Throttle_tube", false);
            this.mesh.chunkVisible("Z_Throttle_D1", true);
            this.mesh.chunkVisible("Z_Throttle_TD1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) if (this.type == 0 || this.type == 1) this.mesh.chunkVisible("Z_OilSplatE4_D1", true);
        else this.mesh.chunkVisible("Z_OilSplats_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("Z_HullDamage3", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Z_HullDamage2", true);
            if (this.type == 0 || this.type == 1) this.mesh.chunkVisible("Z_Holes2E4_D1", true);
            else this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
    }

    protected void reflectPlaneMats() {
//        if (Actor.isValid(this.fm.actor)) {
//            if (this.fm.actor instanceof FI_156)
//                this.type = TYPE_FI_156;
//            switch (this.type) {
//                case TYPE_FI_156: // '\0'
//                    this.mesh.chunkVisible("Body", false);
//                    this.mesh.chunkVisible("BodyE4", true);
//                    this.mesh.chunkVisible("Top", false);
//                    this.mesh.chunkVisible("TopE4", true);
//                    this.mesh.chunkVisible("PanelE4_D0", true);
//                    this.mesh.chunkVisible("PanelE4B_D0", false);
//                    this.mesh.chunkVisible("PanelE7_D0", false);
//                    this.mesh.chunkVisible("oxigen-7", true);
//                    this.mesh.chunkVisible("oxigen-7z", false);
//                    break;
//            }
//            // TODO: Storebror, Bugfix required to reflect correct cockpit type in case of repairing Cockpit Damage
//            //------------------------------------------
//            ZutiSupportMethods_Air.backupCockpit(this);
//            //------------------------------------------
//        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
//    private float              pictManifold;
    private boolean            bNeedSetUp;
    private int                type;
//    private static final int   TYPE_FI_156        = 0;
    private static final float speedometerScale[] = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
    private static final float rpmScale[]         = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };
    private static final float fuelScale[]        = { 0.0F, 9F, 21F, 29.5F, 37F, 48F, 61.5F, 75.5F, 92F, 92F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
