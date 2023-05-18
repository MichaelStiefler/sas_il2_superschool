package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitF11C extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitF11C.this.bNeedSetUp) {
                CockpitF11C.this.reflectPlaneMats();
                CockpitF11C.this.bNeedSetUp = false;
            }
            if ((CockpitF11C.this.ac != null) && ((Avia_B5xx) (CockpitF11C.this.ac)).bChangedPit) {
                CockpitF11C.this.reflectPlaneToModel();
                CockpitF11C.this.ac.bChangedPit = false;
            }
            CockpitF11C.this.setTmp = CockpitF11C.this.setOld;
            CockpitF11C.this.setOld = CockpitF11C.this.setNew;
            CockpitF11C.this.setNew = CockpitF11C.this.setTmp;
            if (((CockpitF11C.this.fm.AS.astateCockpitState & 2) != 0) && (CockpitF11C.this.setNew.stbyPosition < 1.0F)) {
                CockpitF11C.this.setNew.stbyPosition = CockpitF11C.this.setOld.stbyPosition + 0.0125F;
                CockpitF11C.this.setOld.stbyPosition = CockpitF11C.this.setNew.stbyPosition;
            }
            CockpitF11C.this.setNew.altimeter = CockpitF11C.this.fm.getAltitude();
            CockpitF11C.this.setNew.throttle = ((10F * CockpitF11C.this.setOld.throttle) + CockpitF11C.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitF11C.this.setNew.mix = ((10F * CockpitF11C.this.setOld.mix) + CockpitF11C.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitF11C.this.setNew.radiator = ((10F * CockpitF11C.this.setOld.radiator) + CockpitF11C.this.fm.EI.engines[0].getControlRadiator()) / 11F;
            CockpitF11C.this.setNew.prop = CockpitF11C.this.setOld.prop;
            if (CockpitF11C.this.setNew.prop < (CockpitF11C.this.fm.EI.engines[0].getControlProp() - 0.01F)) {
                CockpitF11C.this.setNew.prop += 0.0025F;
            }
            if (CockpitF11C.this.setNew.prop > (CockpitF11C.this.fm.EI.engines[0].getControlProp() + 0.01F)) {
                CockpitF11C.this.setNew.prop -= 0.0025F;
            }
            CockpitF11C.this.w.set(CockpitF11C.this.fm.getW());
            CockpitF11C.this.fm.Or.transform(CockpitF11C.this.w);
            CockpitF11C.this.setNew.turn = ((12F * CockpitF11C.this.setOld.turn) + CockpitF11C.this.w.z) / 13F;
            CockpitF11C.this.setNew.vspeed = ((299F * CockpitF11C.this.setOld.vspeed) + CockpitF11C.this.fm.getVertSpeed()) / 300F;
            CockpitF11C.this.pictSupc = (0.8F * CockpitF11C.this.pictSupc) + (0.2F * CockpitF11C.this.fm.EI.engines[0].getControlCompressor());
            if (CockpitF11C.this.cockpitDimControl) {
                if (CockpitF11C.this.setNew.dimPos < 1.0F) {
                    CockpitF11C.this.setNew.dimPos = CockpitF11C.this.setOld.dimPos + 0.03F;
                }
            } else if (CockpitF11C.this.setNew.dimPos > 0.0F) {
                CockpitF11C.this.setNew.dimPos = CockpitF11C.this.setOld.dimPos - 0.03F;
            }
            if (!CockpitF11C.this.fm.Gears.bTailwheelLocked && (CockpitF11C.this.tailWheelLock < 1.0F)) {
                CockpitF11C.this.tailWheelLock = CockpitF11C.this.tailWheelLock + 0.05F;
            } else if (CockpitF11C.this.fm.Gears.bTailwheelLocked && (CockpitF11C.this.tailWheelLock > 0.0F)) {
                CockpitF11C.this.tailWheelLock = CockpitF11C.this.tailWheelLock - 0.05F;
            }
            CockpitF11C.this.updateCompass();
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float    altimeter;
        float    throttle;
        float    mix;
        float    radiator;
        float    prop;
        float    turn;
        float    vspeed;
        float    stbyPosition;
        float    dimPos;
        Point3d  planeLoc;
        Point3d  planeMove;
        Vector3d compassPoint[];
        Vector3d cP[];

        private Variables() {
            this.planeLoc = new Point3d();
            this.planeMove = new Point3d();
            this.compassPoint = new Vector3d[4];
            this.cP = new Vector3d[4];
            this.compassPoint[0] = new Vector3d(0.0D, Math.sqrt(1.0D - (CockpitF11C.compassZ * CockpitF11C.compassZ)), CockpitF11C.compassZ);
            this.compassPoint[1] = new Vector3d(-Math.sqrt(1.0D - (CockpitF11C.compassZ * CockpitF11C.compassZ)), 0.0D, CockpitF11C.compassZ);
            this.compassPoint[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - (CockpitF11C.compassZ * CockpitF11C.compassZ)), CockpitF11C.compassZ);
            this.compassPoint[3] = new Vector3d(Math.sqrt(1.0D - (CockpitF11C.compassZ * CockpitF11C.compassZ)), 0.0D, CockpitF11C.compassZ);
            this.cP[0] = new Vector3d(0.0D, Math.sqrt(1.0D - (CockpitF11C.compassZ * CockpitF11C.compassZ)), CockpitF11C.compassZ);
            this.cP[1] = new Vector3d(-Math.sqrt(1.0D - (CockpitF11C.compassZ * CockpitF11C.compassZ)), 0.0D, CockpitF11C.compassZ);
            this.cP[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - (CockpitF11C.compassZ * CockpitF11C.compassZ)), CockpitF11C.compassZ);
            this.cP[3] = new Vector3d(Math.sqrt(1.0D - (CockpitF11C.compassZ * CockpitF11C.compassZ)), 0.0D, CockpitF11C.compassZ);
        }

    }

    public CockpitF11C() {
        super("3DO/Cockpit/HawkII/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictSupc = 0.0F;
        this.tailWheelLock = 1.0F;
        this.ac = null;
        this.rpmGeneratedPressure = 0.0F;
        this.oilPressure = 0.0F;
        this.compassFirst = 0;
        this.ac = (F11CHawkII) this.aircraft();
        this.cockpitNightMats = (new String[] { "Compass", "gauge1", "gauge2", "gauge3", "gauge4", "gauge5", "DM_gauge1", "DM_gauge2" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    private void initCompass() {
        this.accel = new Vector3d();
        this.compassSpeed = new Vector3d[4];
        this.compassSpeed[0] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.compassSpeed[1] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.compassSpeed[2] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.compassSpeed[3] = new Vector3d(0.0D, 0.0D, 0.0D);
        float af[] = { 87F, 77.5F, 65.3F, 41.5F, -0.3F, -43.5F, -62.9F, -64F, -66.3F, -75.8F };
        float af1[] = { 55.8F, 51.5F, 47F, 40.1F, 33.8F, 33.7F, 32.7F, 35.1F, 46.6F, 61F };
        float f = this.cvt(Engine.land().config.declin, -90F, 90F, 9F, 0.0F);
        float f1 = this.floatindex(f, af);
        this.compassNorth = new Vector3d(0.0D, Math.cos(0.017452777777777779D * f1), -Math.sin(0.017452777777777779D * f1));
        this.compassSouth = new Vector3d(0.0D, -Math.cos(0.017452777777777779D * f1), Math.sin(0.017452777777777779D * f1));
        float f2 = this.floatindex(f, af1);
        this.compassNorth.scale((f2 / 600F) * Time.tickLenFs());
        this.compassSouth.scale((f2 / 600F) * Time.tickLenFs());
        this.segLen1 = 2D * Math.sqrt(1.0D - (CockpitF11C.compassZ * CockpitF11C.compassZ));
        this.segLen2 = this.segLen1 / Math.sqrt(2D);
        this.compassLimit = -1D * Math.sin(0.01745328888888889D * CockpitF11C.compassLimitAngle);
        this.compassLimit *= this.compassLimit;
        this.compassAcc = 4.66666666D * Time.tickLenFs();
        this.compassSc = 0.101936799D / Time.tickLenFs() / Time.tickLenFs();
    }

    private void updateCompass() {
        if (this.compassFirst == 0) {
            this.initCompass();
            this.fm.getLoc(this.setOld.planeLoc);
        }
        this.fm.getLoc(this.setNew.planeLoc);
        this.setNew.planeMove.set(this.setNew.planeLoc);
        this.setNew.planeMove.sub(this.setOld.planeLoc);
        this.accel.set(this.setNew.planeMove);
        this.accel.sub(this.setOld.planeMove);
        this.accel.scale(this.compassSc);
        this.accel.x = -this.accel.x;
        this.accel.y = -this.accel.y;
        this.accel.z = -this.accel.z - 1.0D;
        this.accel.scale(this.compassAcc);
        if (this.accel.length() > (-CockpitF11C.compassZ * 0.7D)) {
            this.accel.scale((-CockpitF11C.compassZ * 0.7D) / this.accel.length());
        }
        for (int i = 0; i < 4; i++) {
            this.compassSpeed[i].set(this.setOld.compassPoint[i]);
            this.compassSpeed[i].sub(this.setNew.compassPoint[i]);
        }

        for (int j = 0; j < 4; j++) {
            double d = this.compassSpeed[j].length();
            d = 0.985D / (1.0D + (d * d * 15D));
            this.compassSpeed[j].scale(d);
        }

        Vector3d vector3d = new Vector3d();
        vector3d.set(this.setOld.compassPoint[0]);
        vector3d.add(this.setOld.compassPoint[1]);
        vector3d.add(this.setOld.compassPoint[2]);
        vector3d.add(this.setOld.compassPoint[3]);
        vector3d.normalize();
        for (int k = 0; k < 4; k++) {
            Vector3d vector3d1 = new Vector3d();
            double d1 = vector3d.dot(this.compassSpeed[k]);
            vector3d1.set(vector3d);
            d1 *= 0.28D;
            vector3d1.scale(-d1);
            this.compassSpeed[k].add(vector3d1);
        }

        for (int l = 0; l < 4; l++) {
            this.compassSpeed[l].add(this.accel);
        }

        this.compassSpeed[0].add(this.compassNorth);
        this.compassSpeed[2].add(this.compassSouth);
        for (int i1 = 0; i1 < 4; i1++) {
            this.setNew.compassPoint[i1].set(this.setOld.compassPoint[i1]);
            this.setNew.compassPoint[i1].add(this.compassSpeed[i1]);
        }

        vector3d.set(this.setNew.compassPoint[0]);
        vector3d.add(this.setNew.compassPoint[1]);
        vector3d.add(this.setNew.compassPoint[2]);
        vector3d.add(this.setNew.compassPoint[3]);
        vector3d.scale(0.25D);
        Vector3d vector3d2 = new Vector3d(vector3d);
        vector3d2.normalize();
        vector3d2.scale(-CockpitF11C.compassZ);
        vector3d2.sub(vector3d);
        for (int j1 = 0; j1 < 4; j1++) {
            this.setNew.compassPoint[j1].add(vector3d2);
        }

        for (int k1 = 0; k1 < 4; k1++) {
            this.setNew.compassPoint[k1].normalize();
        }

        for (int l1 = 0; l1 < 2; l1++) {
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[2], this.segLen1);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[3], this.segLen1);
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[1], this.segLen2);
            this.compassDist(this.setNew.compassPoint[2], this.setNew.compassPoint[3], this.segLen2);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[2], this.segLen2);
            this.compassDist(this.setNew.compassPoint[3], this.setNew.compassPoint[0], this.segLen2);
            for (int i2 = 0; i2 < 4; i2++) {
                this.setNew.compassPoint[i2].normalize();
            }

            this.compassDist(this.setNew.compassPoint[3], this.setNew.compassPoint[0], this.segLen2);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[2], this.segLen2);
            this.compassDist(this.setNew.compassPoint[2], this.setNew.compassPoint[3], this.segLen2);
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[1], this.segLen2);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[3], this.segLen1);
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[2], this.segLen1);
            for (int j2 = 0; j2 < 4; j2++) {
                this.setNew.compassPoint[j2].normalize();
            }

        }

        Orientation orientation = new Orientation();
        this.fm.getOrient(orientation);
        for (int k2 = 0; k2 < 4; k2++) {
            this.setNew.cP[k2].set(this.setNew.compassPoint[k2]);
            orientation.transformInv(this.setNew.cP[k2]);
        }

        Vector3d vector3d3 = new Vector3d();
        vector3d3.set(this.setNew.cP[0]);
        vector3d3.add(this.setNew.cP[1]);
        vector3d3.add(this.setNew.cP[2]);
        vector3d3.add(this.setNew.cP[3]);
        vector3d3.scale(0.25D);
        Vector3d vector3d4 = new Vector3d();
        vector3d4.set(vector3d3);
        vector3d4.normalize();
        float f = (float) ((vector3d4.x * vector3d4.x) + (vector3d4.y * vector3d4.y));
        if ((f > this.compassLimit) || (vector3d3.z > 0.0D)) {
            for (int l2 = 0; l2 < 4; l2++) {
                this.setNew.cP[l2].set(this.setOld.cP[l2]);
                this.setNew.compassPoint[l2].set(this.setOld.cP[l2]);
                orientation.transform(this.setNew.compassPoint[l2]);
            }

            vector3d3.set(this.setNew.cP[0]);
            vector3d3.add(this.setNew.cP[1]);
            vector3d3.add(this.setNew.cP[2]);
            vector3d3.add(this.setNew.cP[3]);
            vector3d3.scale(0.25D);
        }
        vector3d4.set(this.setNew.cP[0]);
        vector3d4.sub(vector3d3);
        double d2 = -Math.atan2(vector3d3.y, -vector3d3.z);
        this.vectorRot2(vector3d3, d2);
        this.vectorRot2(vector3d4, d2);
        double d3 = Math.atan2(vector3d3.x, -vector3d3.z);
        this.vectorRot1(vector3d4, -d3);
        double d4 = Math.atan2(vector3d4.y, vector3d4.x);
        this.mesh.chunkSetAngles("NeedCompass_A", -(float) ((d2 * 180D) / Math.PI), -(float) ((d3 * 180D) / Math.PI), 0.0F);
        this.mesh.chunkSetAngles("NeedCompass_B", 0.0F, (float) (90D - ((d4 * 180D) / Math.PI)), 0.0F);
        this.compassFirst++;
    }

    private void vectorRot1(Vector3d vector3d, double d) {
        double d1 = Math.sin(d);
        double d2 = Math.cos(d);
        double d3 = (vector3d.x * d2) - (vector3d.z * d1);
        vector3d.z = (vector3d.x * d1) + (vector3d.z * d2);
        vector3d.x = d3;
    }

    private void vectorRot2(Vector3d vector3d, double d) {
        double d1 = Math.sin(d);
        double d2 = Math.cos(d);
        double d3 = (vector3d.y * d2) - (vector3d.z * d1);
        vector3d.z = (vector3d.y * d1) + (vector3d.z * d2);
        vector3d.y = d3;
    }

    private void compassDist(Vector3d vector3d, Vector3d vector3d1, double d) {
        Vector3d vector3d2 = new Vector3d();
        vector3d2.set(vector3d);
        vector3d2.sub(vector3d1);
        double d1 = vector3d2.length();
        if (d1 < 0.000001D) {
            d1 = 0.000001D;
        }
        d1 = (d - d1) / d1 / 2D;
        vector3d2.scale(d1);
        vector3d.add(vector3d2);
        vector3d1.sub(vector3d2);
    }

    public void reflectWorldToInstruments(float f) {
        float f1 = 0.0F;
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (((F11CHawkII) this.aircraft()).bChangedPit) {
            this.reflectPlaneToModel();
            ((F11CHawkII) this.aircraft()).bChangedPit = false;
        }
        this.mesh.chunkSetAngles("NeedManPress", 0.0F, this.pictManifold = (0.85F * this.pictManifold) + (0.15F * this.cvt(this.fm.EI.engines[0].getManifoldPressure() * 760F, 260F, 1200F, 33.3F, 360F)), 0.0F);
        f1 = -15F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl));
        float f2 = 14F * (this.pictElev = (0.85F * this.pictElev) + (0.2F * this.fm.CT.ElevatorControl));
        this.mesh.chunkSetAngles("StickB", 0.0F, -f1, f2);
        f1 = this.fm.CT.getRudder();
        this.mesh.chunkSetAngles("Pedal_L", 0.0F, 18F * f1, 0.0F);
        this.mesh.chunkSetAngles("Pedal_R", 0.0F, 18F * f1, 0.0F);
        this.mesh.chunkSetAngles("TQHandle", 0.0F, 0.0F, 75F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("MixLvr", 0.0F, 0.0F, 65F * this.interp(this.setNew.mix, this.setOld.mix, f));
        this.mesh.chunkSetAngles("CowlFlapLvr", 0.0F, -70F * this.interp(this.setNew.radiator, this.setOld.radiator, f), 0.0F);
        this.mesh.chunkSetAngles("NeedAlt_Km", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 450F), 0.0F);
        float f3 = Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH());
        if (f3 < 100F) {
            this.mesh.chunkSetAngles("NeedSpeed", 0.0F, -this.cvt(f3, 0.0F, 100F, 0.0F, -28.4F), 0.0F);
        } else if (f3 < 200F) {
            this.mesh.chunkSetAngles("NeedSpeed", 0.0F, -this.cvt(f3, 100F, 200F, -28.4F, -102F), 0.0F);
        } else if (f3 < 300F) {
            this.mesh.chunkSetAngles("NeedSpeed", 0.0F, -this.cvt(f3, 200F, 300F, -102F, -191.5F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedSpeed", 0.0F, -this.cvt(f3, 300F, 450F, -191.5F, -326F), 0.0F);
        }
        this.mesh.chunkSetAngles("NeedFuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 250F, 0.0F, 255.5F), 0.0F);
        f1 = this.fm.EI.engines[0].tOilOut;
        if (f1 < 20F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f1, 0.0F, 20F, 0.0F, 15F), 0.0F);
        } else if (f1 < 40F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f1, 20F, 40F, 15F, 50F), 0.0F);
        } else if (f1 < 60F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f1, 40F, 60F, 50F, 102.5F), 0.0F);
        } else if (f1 < 80F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f1, 60F, 80F, 102.5F, 186F), 0.0F);
        } else if (f1 < 100F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f1, 80F, 100F, 186F, 283F), 0.0F);
        } else if (f1 < 120F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f1, 100F, 120F, 283F, 314F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f1, 120F, 140F, 314F, 345F), 0.0F);
        }
        f1 = this.fm.EI.engines[0].getRPM();
        this.mesh.chunkSetAngles("NeedRPM", 0.0F, this.cvt(f1, 0.0F, 3000F, 0.0F, 310F), 0.0F);
        if ((this.fm.Or.getKren() < -110F) || (this.fm.Or.getKren() > 110F)) {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure - 0.5F;
        } else if (f1 < this.rpmGeneratedPressure) {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure - ((this.rpmGeneratedPressure - f1) * 0.01F);
        } else {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure + ((f1 - this.rpmGeneratedPressure) * 0.001F);
        }
        if (this.rpmGeneratedPressure < 600F) {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 0.0F, 600F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure < 900F) {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 600F, 900F, 4F, 7F);
        } else {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 900F, 1200F, 7F, 10F);
        }
        float f4 = 0.0F;
        if (this.fm.EI.engines[0].tOilOut > 90F) {
            f4 = this.cvt(this.fm.EI.engines[0].tOilOut, 90F, 120F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[0].tOilOut < 50F) {
            f4 = this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 50F, 1.5F, 0.9F);
        } else {
            f4 = this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        float f5 = f4 * this.fm.EI.engines[0].getReadyness() * this.oilPressure;
        if (f5 < 12F) {
            this.mesh.chunkSetAngles("NeedOilPress", 0.0F, this.cvt(f5, 0.0F, 12F, 0.0F, 230F), 0.0F);
        } else if (f5 < 16F) {
            this.mesh.chunkSetAngles("NeedOilPress", 0.0F, this.cvt(f5, 12F, 16F, 230F, 285F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedOilPress", 0.0F, this.cvt(f5, 16F, 32F, 285F, 320F), 0.0F);
        }
        f1 = this.fm.EI.engines[0].tWaterOut;
        if (f1 < 20F) {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f1, 0.0F, 20F, 0.0F, 15F), 0.0F);
        } else if (f1 < 40F) {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f1, 20F, 40F, 15F, 50F), 0.0F);
        } else if (f1 < 60F) {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f1, 40F, 60F, 50F, 109F), 0.0F);
        } else if (f1 < 80F) {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f1, 60F, 80F, 109F, 192F), 0.0F);
        } else if (f1 < 100F) {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f1, 80F, 100F, 192F, 294F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f1, 100F, 111.5F, 294F, 356.1F), 0.0F);
        }
        this.mesh.chunkSetAngles("NeedFuelPress", 0.0F, this.cvt(this.rpmGeneratedPressure, 0.0F, 1200F, 0.0F, 260F), 0.0F);
        this.mesh.chunkSetAngles("NeedBank", 0.0F, this.cvt(this.setNew.turn, -0.2F, 0.2F, 22.5F, -22.5F), 0.0F);
        this.mesh.chunkSetAngles("NeedTurn", 0.0F, -this.cvt(this.getBall(8D), -8F, 8F, 9.5F, -9.5F), 0.0F);
        this.mesh.chunkSetAngles("NeedClimb", 0.0F, -this.cvt(this.setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F);
        this.mesh.chunkSetAngles("TrimIndicator", 120F * -this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ElevTrim", 0.0F, 600F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("IgnitionSwitch", 0.0F, 90F * (1 + this.fm.EI.engines[0].getControlMagnetos()), 0.0F);
        if (this.fm.CT.bHasBrakeControl) {
            float f6 = this.fm.CT.getBrake();
            this.mesh.chunkSetAngles("BrakeLever", f6 * 20F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedAirPR", 0.0F, -this.cvt(f6 + (f6 * this.fm.CT.getRudder()), 0.0F, 1.5F, 0.0F, 148F), 0.0F);
            this.mesh.chunkSetAngles("NeedAirPL", 0.0F, this.cvt(f6 - (f6 * this.fm.CT.getRudder()), 0.0F, 1.5F, 0.0F, 148F), 0.0F);
            this.mesh.chunkSetAngles("NeedAirP", 0.0F, 110F - (f6 * 20F), 0.0F);
        }
        this.mesh.chunkSetAngles("Trigger1", 10F * (this.fm.CT.saveWeaponControl[0] ? 1.0F : 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Trigger2", 10F * (this.fm.CT.saveWeaponControl[1] ? 1.0F : 0.0F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("DamageGlass1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Gages1_d0", false);
            this.mesh.chunkVisible("Gages1_d1", true);
            this.mesh.chunkVisible("NeedOilTemp", false);
            this.mesh.chunkVisible("NeedSpeed", false);
            this.mesh.chunkVisible("NeedWatTemp", false);
            this.mesh.chunkVisible("NeedAlt_Km", false);
            this.mesh.chunkVisible("DamageHull1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Gages2_d0", false);
            this.mesh.chunkVisible("Gages2_d1", true);
            this.mesh.chunkVisible("NeedClimb", false);
            this.mesh.chunkVisible("NeedBank", false);
            this.mesh.chunkVisible("NeedTurn", false);
            this.mesh.chunkVisible("DamageHull1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("DamageHull2", true);
            this.mesh.chunkVisible("DamageGlass3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("DamageHull3", true);
            this.mesh.chunkVisible("DamageGlass3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("DamageGlass2", true);
            this.mesh.chunkVisible("DamageHull4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("OilSplats", true);
        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
    }

    public void doToggleUp(boolean flag) {
        super.doToggleUp(flag);
    }

    public void destroy() {
        super.destroy();
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        this.mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
        this.mesh.materialReplace("Matt2D2o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Interior_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            this.aircraft().hierMesh().chunkVisible("Interior_D0", true);
            super.doFocusLeave();
        }
    }

    private Variables     setOld;
    private Variables     setNew;
    private Variables     setTmp;
    private Vector3f      w;
    private boolean       bNeedSetUp;
    private float         pictAiler;
    private float         pictElev;
    private float         pictSupc;
    private float         pictManifold;
    private float         tailWheelLock;
    private F11CHawkII    ac;
    private float         rpmGeneratedPressure;
    private float         oilPressure;
    private static double compassZ          = -0.2D;
    private double        segLen1;
    private double        segLen2;
    private double        compassLimit;
    private static double compassLimitAngle = 25D;
    private Vector3d      compassSpeed[];
    int                   compassFirst;
    private Vector3d      accel;
    private Vector3d      compassNorth;
    private Vector3d      compassSouth;
    private double        compassAcc;
    private double        compassSc;

}
