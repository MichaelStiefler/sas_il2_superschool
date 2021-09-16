package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitKurogane extends CockpitPilot {
    private class Variables {

        private Variables() {
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitKurogane.this.bNeedSetUp) {
                CockpitKurogane.this.reflectPlaneMats();
                CockpitKurogane.this.bNeedSetUp = false;
            }
            if (Kurogane.bChangedPit) {
                CockpitKurogane.this.reflectPlaneToModel();
                Kurogane.bChangedPit = false;
            }
            CockpitKurogane.this.setTmp = CockpitKurogane.this.setOld;
            CockpitKurogane.this.setOld = CockpitKurogane.this.setNew;
            CockpitKurogane.this.setNew = CockpitKurogane.this.setTmp;
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitKurogane() {
        super("3DO/Cockpit/Kurogane/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.bNeedSetUp = true;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Rudder", -120F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 46F), 0.0F);
        this.mesh.chunkSetAngles("zTemp", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 243.15F, 303.15F, -35F, 35F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 100F, 0.0F, 5F), CockpitKurogane.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zFuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 50F, 0.0F, 90F), 0.0F);
    }

    protected void reflectPlaneToModel() {
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    public void reflectCockpitState() {
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 48F, 96F, 144F, 192F, 240F };

}
