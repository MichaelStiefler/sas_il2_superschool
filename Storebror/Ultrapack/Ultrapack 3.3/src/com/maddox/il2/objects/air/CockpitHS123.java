package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHS123 extends CockpitPilot {
    private class Variables {

        float      altimeter;
        float      throttle;
        float      dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      turn;
        float      mix;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHS123.this.fm != null) {
                if (CockpitHS123.this.bNeedSetUp) {
                    CockpitHS123.this.reflectPlaneMats();
                    CockpitHS123.this.bNeedSetUp = false;
                }
                if (((HS123) CockpitHS123.this.aircraft()).bChangedPit) {
                    CockpitHS123.this.reflectPlaneToModel();
                    ((HS123) CockpitHS123.this.aircraft()).bChangedPit = false;
                }
                CockpitHS123.this.setTmp = CockpitHS123.this.setOld;
                CockpitHS123.this.setOld = CockpitHS123.this.setNew;
                CockpitHS123.this.setNew = CockpitHS123.this.setTmp;
                CockpitHS123.this.setNew.altimeter = CockpitHS123.this.fm.getAltitude();
                if (CockpitHS123.this.cockpitDimControl) {
                    if (CockpitHS123.this.setNew.dimPosition > 0.0F) CockpitHS123.this.setNew.dimPosition = CockpitHS123.this.setOld.dimPosition - 0.05F;
                } else if (CockpitHS123.this.setNew.dimPosition < 1.0F) CockpitHS123.this.setNew.dimPosition = CockpitHS123.this.setOld.dimPosition + 0.05F;
                CockpitHS123.this.setNew.throttle = (10F * CockpitHS123.this.setOld.throttle + CockpitHS123.this.fm.CT.PowerControl) / 11F;
                CockpitHS123.this.setNew.mix = (8F * CockpitHS123.this.setOld.mix + CockpitHS123.this.fm.EI.engines[0].getControlMix()) / 9F;
                float f = CockpitHS123.this.waypointAzimuth();
                CockpitHS123.this.setNew.waypointAzimuth.setDeg(CockpitHS123.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitHS123.this.setOld.azimuth.getDeg(1.0F));
                CockpitHS123.this.setNew.azimuth.setDeg(CockpitHS123.this.setOld.azimuth.getDeg(1.0F), CockpitHS123.this.fm.Or.azimut());
                CockpitHS123.this.w.set(CockpitHS123.this.fm.getW());
                CockpitHS123.this.fm.Or.transform(CockpitHS123.this.w);
                CockpitHS123.this.setNew.turn = (12F * CockpitHS123.this.setOld.turn + CockpitHS123.this.w.z) / 13F;
            }
            return true;
        }

    }

    protected void setCameraOffset() {
        this.cameraCenter.add(0.0D, 0.02D, 0.0D);
    }

    public CockpitHS123() {
        super("3DO/Cockpit/Hs_123/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictManifold = 0.0F;
        this.bNeedSetUp = true;
        this.w = new Vector3f();
        this.setNew.dimPosition = 1.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(227F, 65F, 33F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(227F, 65F, 33F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = new String[] { "ZClocks1", "ZClocks1DMG", "ZClocks2", "ZClocks3", "FW190A4Compass", "oxigen" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (((HS123) this.aircraft()).bChangedPit) {
            this.reflectPlaneToModel();
            ((HS123) this.aircraft()).bChangedPit = false;
        }
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ReviTinter", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ReviTint", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 40F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ATA1", 15.5F + this.cvt(this.pictManifold = 0.75F * this.pictManifold + 0.25F * this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 336F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelQuantity1", -44.5F + this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 8F), fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Iengtemprad1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 58.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_EngTemp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 58.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress1", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(6D), -6F, 6F, -7F, 7F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Azimuth1", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch1", 270F - (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch2", 105F - (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5F, 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_FuelRed1", this.fm.M.fuel < 36F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 15F, 0.0F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 10F);
        this.mesh.chunkSetAngles("Z_PedalStrut", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalStrut2", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", this.interp(this.setNew.throttle, this.setOld.throttle, f) * 57.72727F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle_tube", -this.interp(this.setNew.throttle, this.setOld.throttle, f) * 57.72727F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix", this.interp(this.setNew.mix, this.setOld.mix, f) * 70F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix_tros", -this.interp(this.setNew.mix, this.setOld.mix, f) * 70F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", -45F + 28.333F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.005F, 0.5F);
            this.light2.light.setEmit(0.005F, 0.5F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("Z_HullDamage1", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0 && (this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Z_HullDamage1", true);
            this.mesh.chunkVisible("Z_HullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("Z_HullDamage4", true);
        if ((this.fm.AS.astateCockpitState & 8) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("Z_OilSplats_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("Z_HullDamage3", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("Z_HullDamage2", true);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.materialReplace("Gloss1D0o", hiermesh.material(hiermesh.materialFind("Gloss1D0o")));
        this.mesh.materialReplace("Gloss1D1o", hiermesh.material(hiermesh.materialFind("Gloss1D1o")));
        this.mesh.materialReplace("Gloss1D2o", hiermesh.material(hiermesh.materialFind("Gloss1D2o")));
        this.mesh.materialReplace("Gloss2D0o", hiermesh.material(hiermesh.materialFind("Gloss2D0o")));
        this.mesh.materialReplace("Gloss2D1o", hiermesh.material(hiermesh.materialFind("Gloss2D1o")));
        this.mesh.materialReplace("Matt1D0o", hiermesh.material(hiermesh.materialFind("Matt1D0o")));
        this.mesh.materialReplace("Matt1D1o", hiermesh.material(hiermesh.materialFind("Matt1D1o")));
        this.mesh.materialReplace("Matt1D2o", hiermesh.material(hiermesh.materialFind("Matt1D2o")));
        this.mesh.materialReplace("Matt2D0o", hiermesh.material(hiermesh.materialFind("Matt2D0o")));
        this.mesh.materialReplace("Matt2D1o", hiermesh.material(hiermesh.materialFind("Matt2D1o")));
        this.mesh.materialReplace("Matt2D2o", hiermesh.material(hiermesh.materialFind("Matt2D2o")));
        this.mesh.materialReplace("OverlayD1o", hiermesh.material(hiermesh.materialFind("OverlayD1o")));
        this.mesh.materialReplace("OverlayD2o", hiermesh.material(hiermesh.materialFind("OverlayD2o")));
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private float              pictAiler;
    private float              pictElev;
    private float              pictManifold;
    private boolean            bNeedSetUp;
    public Vector3f            w;
    private static final float speedometerScale[] = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
    private static final float rpmScale[]         = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };
    private static final float fuelScale[]        = { 0.0F, 9F, 21F, 29.5F, 37F, 48F, 61.5F, 75.5F, 92F, 92F };

    static {
        Property.set(CockpitHS123.class, "normZN", 0.72F);
    }
}
