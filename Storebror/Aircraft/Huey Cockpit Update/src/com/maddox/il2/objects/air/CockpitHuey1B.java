package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHuey1B extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHuey1B.this.fm != null) {
                CockpitHuey1B.this.setTmp = CockpitHuey1B.this.setOld;
                CockpitHuey1B.this.setOld = CockpitHuey1B.this.setNew;
                CockpitHuey1B.this.setNew = CockpitHuey1B.this.setTmp;
                CockpitHuey1B.this.setNew.throttle = (0.85F * CockpitHuey1B.this.setOld.throttle) + (0.15F * CockpitHuey1B.this.fm.CT.PowerControl);
                CockpitHuey1B.this.setNew.prop = (0.95F * CockpitHuey1B.this.setOld.prop) + (0.05F * CockpitHuey1B.this.fm.CT.getStepControl());
                CockpitHuey1B.this.setNew.engine = (0.85F * CockpitHuey1B.this.setOld.engine) + (0.15F * CockpitHuey1B.this.fm.CT.getStepControl());
                CockpitHuey1B.this.setNew.engineRPM = (0.98F * CockpitHuey1B.this.setOld.engineRPM) + (0.02F * CockpitHuey1B.this.fm.EI.engines[0].getRPM());
                CockpitHuey1B.this.setNew.gas = (0.95F * CockpitHuey1B.this.setOld.gas) + (0.05F * CockpitHuey1B.this.fm.EI.engines[0].getRPM()) + (0.1F * World.rnd().nextFloat());
                final float rotorRpm = (CockpitHuey1B.this.aircraft() instanceof HueyX) ? (float) ((HueyX) CockpitHuey1B.this.aircraft()).rotorrpm : (float) CockpitHuey1B.this.rotorrpm;
                CockpitHuey1B.this.setNew.rotorRPM = (0.95F * CockpitHuey1B.this.setOld.rotorRPM) + (0.05F * rotorRpm);
                CockpitHuey1B.this.setNew.torque = (0.98F * CockpitHuey1B.this.setOld.torque) + (0.02F * CockpitHuey1B.this.setNew.engineRPM);
                CockpitHuey1B.this.setNew.oilPress = (0.99F * CockpitHuey1B.this.setOld.oilPress) + (0.01F * CockpitHuey1B.this.cvt(CockpitHuey1B.this.setNew.engineRPM, 0F, 100F, 0F, 100F) * (1F - (CockpitHuey1B.this.fm.EI.engines[0].tOilOut / 375F)) * CockpitHuey1B.this.fm.EI.engines[0].getReadyness());
                CockpitHuey1B.this.setNew.transOil = (0.99F * CockpitHuey1B.this.setOld.transOil) + (0.01F * CockpitHuey1B.this.fm.EI.engines[0].tOilOut);
                CockpitHuey1B.this.setNew.transOilPress = (0.99F * CockpitHuey1B.this.setOld.transOilPress) + (0.01F * CockpitHuey1B.this.cvt(CockpitHuey1B.this.setNew.engineRPM, 0F, 250F, 0F, 100F) * (0.6F - (CockpitHuey1B.this.fm.EI.engines[0].tOilOut / 750F)) * CockpitHuey1B.this.fm.EI.engines[0].getReadyness());
                CockpitHuey1B.this.setNew.fuelPress = (0.95F * CockpitHuey1B.this.setOld.fuelPress) + (0.05F * Math.max(CockpitHuey1B.this.setNew.engineRPM * CockpitHuey1B.this.fm.EI.engines[0].getReadyness(), (CockpitHuey1B.this.fm.EI.engines[0].getStage() > 0) && (CockpitHuey1B.this.fm.EI.engines[0].getStage() < 7) ? 500F : 0F));
                CockpitHuey1B.this.setNew.stage = (0.85F * CockpitHuey1B.this.setOld.stage) + (0.15F * CockpitHuey1B.this.fm.EI.engines[0].getControlCompressor());
                CockpitHuey1B.this.setNew.mix = (0.85F * CockpitHuey1B.this.setOld.mix) + (0.15F * CockpitHuey1B.this.fm.EI.engines[0].getControlMix());
                CockpitHuey1B.this.setNew.altimeter = CockpitHuey1B.this.fm.getAltitude();
                CockpitHuey1B.this.setNew.azimuth = ((35F * CockpitHuey1B.this.setOld.azimuth) - CockpitHuey1B.this.fm.Or.getYaw()) / 36F;
                if ((CockpitHuey1B.this.setOld.azimuth > 270F) && (CockpitHuey1B.this.setNew.azimuth < 90F)) {
                    CockpitHuey1B.this.setOld.azimuth -= 360F;
                }
                if ((CockpitHuey1B.this.setOld.azimuth < 90F) && (CockpitHuey1B.this.setNew.azimuth > 270F)) {
                    CockpitHuey1B.this.setOld.azimuth += 360F;
                }
                CockpitHuey1B.this.setNew.waypointAzimuth = ((10F * CockpitHuey1B.this.setOld.waypointAzimuth) + CockpitHuey1B.this.waypointAzimuth() + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                CockpitHuey1B.this.setNew.vspeed = ((199F * CockpitHuey1B.this.setOld.vspeed) + CockpitHuey1B.this.fm.getVertSpeed()) / 200F;
                final Variables variables = CockpitHuey1B.this.setNew;
                final float f = 0.5F * CockpitHuey1B.this.setOld.radioalt;
                final float f1 = 0.5F;
                final float f2 = CockpitHuey1B.this.fm.getAltitude();
                World.cur();
                World.land();
                variables.radioalt = f + (f1 * (f2 - Landscape.HQ_Air((float) CockpitHuey1B.this.fm.Loc.x, (float) CockpitHuey1B.this.fm.Loc.y)));
                if (CockpitHuey1B.this.cockpitDimControl) {
                    if (CockpitHuey1B.this.setNew.dimPosition > 0.0F) {
                        CockpitHuey1B.this.setNew.dimPosition = CockpitHuey1B.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitHuey1B.this.setNew.dimPosition < 1.0F) {
                    CockpitHuey1B.this.setNew.dimPosition = CockpitHuey1B.this.setOld.dimPosition + 0.05F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float throttle;
        float prop;
        float engine;
        float engineRPM;
        float rotorRPM;
        float torque;
        float gas;
        float transOil;
        float oilPress;
        float transOilPress;
        float fuelPress;
        float mix;
        float stage;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;
        float radioalt;
        float dimPosition;

        private Variables() {
        }

    }

    protected float waypointAzimuth() {
        final WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        } else {
            waypoint.getP(this.tmpP);
            this.tmpV.sub(this.tmpP, this.fm.Loc);
            return (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x)));
        }
    }

    public CockpitHuey1B() {
        super("3DO/Cockpit/Huey1B/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.rotorrpm = 0;
        this.bNeedSetUp = true;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "Instrumentos001", "Instrumentos002", "Instrumentos003" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.setNew.dimPosition = 1.0F;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_Colectivo", 0.0F, 0.0F, -30F * this.setNew.throttle);
        this.mesh.chunkSetAngles("Z_Gases", 0.0F, 200F * this.setNew.throttle, 0.0F);
        this.mesh.chunkSetAngles("Z_Gases2", 0.0F, 200F * this.setNew.throttle, 0.0F);
        this.mesh.chunkSetAngles("Z_Palanca", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * -10F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * -10F);
        this.mesh.chunkSetAngles("Z_Palanca2", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * -10F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * -10F);
        this.mesh.chunkSetAngles("Z_Pedal_D", 0.0F, 0.0F, -15F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Pedal_I", 0.0F, 0.0F, 15F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Radiador", 0.0F, 0.0F, -60F * this.fm.EI.engines[0].getControlRadiator());
        if (this.fm.CT.PowerControl <= 0.11D) {
            this.mesh.chunkSetAngles("Z_clutch", 0.0F, 0.0F, 700F * this.fm.CT.PowerControl);
        }
        this.mesh.chunkSetAngles("Z_Magneto", 0.0F, 20F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F);
        this.mesh.chunkVisible("A_Fuel1", this.fm.M.fuel < 52F);
        this.mesh.chunkVisible("A_Fuel2", this.fm.M.fuel < 26F);
        this.resetYPRmodifier();
        if ((this.fm.EI.engines[0].getStage() > 0) && (this.fm.EI.engines[0].getStage() < 3)) {
            Cockpit.xyz[1] = 0.01F;
        }
        this.mesh.chunkSetLocate("Z_Starter", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("Z_Starter2", (this.fm.EI.engines[0].getStage() > 0) && (this.fm.EI.engines[0].getStage() < 3));
        this.mesh.chunkVisible("A_EngFire", this.fm.AS.astateEngineStates[0] > 2);
        if (this.fm.CT.bHasArrestorControl) {
            this.mesh.chunkVisible("Z_Flotadores_A", true);
            this.mesh.chunkVisible("Z_Flotadores_D", false);
            this.mesh.chunkVisible("A_Foats_A", true);
        }
        this.mesh.chunkVisible("A_Foats_R", this.fm.CT.getArrestor() > 0.05F);
        this.mesh.chunkSetAngles("Mira", 0.0F, this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F);
//        this.mesh.chunkVisible("Reticulo", this.setNew.dimPosition < 0.5F);
        this.mesh.chunkSetAngles("Z_Altimetro2", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimetro1", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimetro4", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimetro3", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
        final float speedValue = -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 330F, 0.0F, 9F), speedometerScale);
        this.mesh.chunkSetAngles("Z_ASI1", 0.0F, speedValue, 0.0F);
        this.mesh.chunkSetAngles("Z_ASI2", 0.0F, speedValue, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Horizonte1", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_Horizonte2", 0.0F, 0.0F, -this.fm.Or.getTangage());
        this.mesh.chunkSetAngles("Z_Horizonte3", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_Horizonte4", 0.0F, 0.0F, -this.fm.Or.getTangage());
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -90F + -this.setNew.azimuth, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, -90F + -this.interp(this.setNew.waypointAzimuth, this.setOld.waypointAzimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 0.0F, -90F + -this.setNew.azimuth, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", 0.0F, -90F + -this.interp(this.setNew.waypointAzimuth, this.setOld.waypointAzimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Variometro1", 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Variometro2", 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        if (this.aircraft() instanceof HueyX) {
            this.mesh.chunkSetAngles("Z_RotorRPM", 0.0F, -this.cvt(this.setNew.rotorRPM, 0F, 2.5F, 95F, 346F) - this.cvt(this.setNew.rotorRPM, 2.5F, 7.0F, 0F, 34F) - this.cvt(this.setNew.rotorRPM, 7.0F, 10.0F, 0F, 17F), 0.0F);
        } else {
            this.rotorrpm = Math.abs((int) ((this.fm.EI.engines[0].getw() * 1.0F) + (this.fm.Vwld.length() * 4D)));
            if (this.rotorrpm >= 250) {
                this.rotorrpm = 250;
            }
            if (this.rotorrpm <= 40) {
                this.rotorrpm = 0;
            }
            this.mesh.chunkSetAngles("Z_RotorRPM", 0.0F, -this.cvt(this.setNew.rotorRPM, 0F, 250F, 95F, 400F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_EngineRPM", 0.0F, -this.cvt(this.setNew.engineRPM, 0F, 100F, -148F, 104F) - this.cvt(this.setNew.engineRPM, 100F, 200F, 0F, 16F) - this.cvt(this.setNew.engineRPM, 200F, 2500F, 0F, 14F) - this.cvt(this.setNew.engineRPM, 2500F, 3000F, 0F, 20F), 0.0F);
        this.mesh.chunkSetAngles("Z_Torque", 0.0F, this.cvt(this.setNew.torque, 0F, 150F, -80F, -35F) + this.cvt(this.setNew.torque, 150F, 750F, 0F, 150F) + this.cvt(this.setNew.torque, 750F, 2700F, 0F, 55F), 0.0F);
        final float fGas = this.cvt(this.setNew.gas, 0F, 150F, 114.5F, -22F) - this.cvt(this.setNew.gas, 150F, 2500F, 0F, 133F);
        float fGas2 = 0F;
        if (this.setNew.gas < 150F) {
            fGas2 = this.cvt(this.setNew.gas % 30F, 0F, 30F, 114.5F, -245.5F);
        } else {
            fGas2 = this.cvt(this.setNew.gas % 470F, 0F, 470F, 114.5F, -245.5F);
        }
        this.mesh.chunkSetAngles("Z_Gas", 0.0F, fGas, 0.0F);
        this.mesh.chunkSetAngles("Z_Gas10th", 0.0F, fGas2, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, -this.cvt(this.fm.EI.engines[0].tWaterOut, 0F, 50F, 205F, 318F) - this.cvt(this.fm.EI.engines[0].tWaterOut, 50F, 750F, 0F, 54F) - this.cvt(this.fm.EI.engines[0].tWaterOut, 750F, 780F, 0F, 48F) - this.cvt(this.fm.EI.engines[0].tWaterOut, 780F, 850F, 0F, 65F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, -floatindex(this.fm.EI.engines[0].tOilOut, oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, -floatindex(this.setNew.oilPress, oilPressScale), 0.0F);
        this.mesh.chunkSetAngles("Z_TransOil1", 0.0F, -floatindex(this.setNew.transOil, oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("Z_TransOilpres", 0.0F, -floatindex(this.setNew.transOilPress, transOilPressScale), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, -floatindex(this.setNew.fuelPress, fuelPressScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel", 0.0F, -this.cvt(this.fm.M.fuel * 0.4536F, 0.0F, 150F, 0.0F, 315F), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass", this.setNew.azimuth, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hora", 0.0F, -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minuto", 0.0F, -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Palo", 0.0F, -this.cvt(this.w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Bola", 0.0F, -this.cvt(this.getBall(7D), -7F, 7F, -15F, 15F), 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
    }

    protected void reflectPlaneMats() {
        final HierMesh hiermesh = this.aircraft().hierMesh();
        final com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
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

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public static float floatindex(float f, float aaf[][]) {
        final int aafLen = aaf.length;
        if (aafLen == 0) {
            return 0F;
        }
        if ((aafLen == 1) || (f <= aaf[0][0])) {
            return aaf[0][1];
        }
        if (f >= aaf[aafLen - 1][0]) {
            return aaf[aafLen - 1][1];
        }
        int lower = 0;
        for (int higher = 1; higher < aafLen; higher++) {
            if (f < aaf[higher][0]) {
                return aaf[lower][1] + ((f - aaf[lower][0]) * ((aaf[higher][1] - aaf[lower][1]) / (aaf[higher][0] - aaf[lower][0])));
            }
            lower = higher;
        }
        return 0F;
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private int                rotorrpm;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[]     = { 0F, 24.3F, 108.5F, 149.5F, 187.5F, 236F, 282.5F, 321.5F, 341.5F, 360F };
    private static final float oilTempScale[][]       = { { -70F, 74F }, { -50F, 89F }, { 0F, 122F }, { 50F, 150F }, { 100F, 175F }, { 150F, 220F }, { 180F, 240F } };
    private static final float oilPressScale[][]      = { { 0F, 36F }, { 20F, 91F }, { 60F, 188F }, { 100F, 280F } };
    private static final float transOilPressScale[][] = { { 0F, 36F }, { 40F, 138F }, { 60F, 188F }, { 100F, 285F } };
    private static final float fuelPressScale[][]     = { { 0F, 0F }, { 400F, 35F }, { 2700F, 212F }, { 3200F, 300F } };
    private static final float variometerScale[]      = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
    private final Point3d      tmpP;
    private final Vector3d     tmpV;

    static {
        Property.set(CockpitHuey1B.class, "normZN", 0.5F);
        Property.set(CockpitHuey1B.class, "gsZN", 0.5F);
    }

}
