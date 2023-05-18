package com.maddox.il2.engine;

import com.maddox.JGP.Matrix3d;
import com.maddox.JGP.Quat4f;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;

public class Orient {

    protected static float DEG2RAD(float f) {
        return (float) Math.toRadians(f);
    }

    protected static float RAD2DEG(float f) {
        return (float) Math.toDegrees(f);
    }

    public Orient() {
        this.Yaw = 0.0F;
        this.Pitch = 0.0F;
        this.Roll = 0.0F;
    }

    public Orient(float f, float f1, float f2) {
        this.Yaw = 0.0F;
        this.Pitch = 0.0F;
        this.Roll = 0.0F;
        this.set(f, f1, f2);
    }

    public void set(float f, float f1, float f2) {
        this.Yaw = -f;
        this.Pitch = -f1;
        this.Roll = f2;
    }

    public void setYPR(float f, float f1, float f2) {
        this.Yaw = f;
        this.Pitch = -f1;
        this.Roll = -f2;
    }

    public void set(Orient orient1) {
        this.Yaw = orient1.Yaw;
        this.Pitch = orient1.Pitch;
        this.Roll = orient1.Roll;
    }

    public final float azimut() {
        return this.getAzimut();
    }

    public final float kren() {
        return this.getKren();
    }

    public final float tangage() {
        return this.getTangage();
    }

    public float getAzimut() {
        return Circ - this.Yaw;
    }

    public float getTangage() {
        return -this.Pitch;
    }

    public float getKren() {
        return this.Roll;
    }

    public float getYaw() {
        return this.Yaw;
    }

    public void setYaw(float f) {
        this.Yaw = f;
    }

    public float getPitch() {
        return Circ - this.Pitch;
    }

    public float getRoll() {
        return Circ - this.Roll;
    }

    public void setRoll(float f) {
        this.Roll = -f;
    }

    public void get(float af[]) {
        af[0] = Circ - this.Yaw;
        af[1] = -this.Pitch;
        af[2] = this.Roll;
    }

    public void getYPR(float af[]) {
        this.wrap();
        af[0] = this.Yaw;
        af[1] = Circ - this.Pitch;
        af[2] = Circ - this.Roll;
    }

    protected void makeMatrix(Matrix3d matrix3d) {
        matrix3d.setEulers(DEG2RAD(this.Yaw), DEG2RAD(this.Pitch), DEG2RAD(this.Roll));
    }

    protected void makeMatrixInv(Matrix3d matrix3d) {
        matrix3d.setEulersInv(DEG2RAD(this.Yaw), DEG2RAD(this.Pitch), DEG2RAD(this.Roll));
    }

    protected void makeQuat(Quat4f quat4f) {
        quat4f.setEulers(DEG2RAD(this.Yaw), DEG2RAD(this.Pitch), DEG2RAD(this.Roll));
    }

    public void add(Orient orient1, Orient orient2) {
        orient2.getMatrix(M_0);
        orient1.getMatrix(M_1);
        M_.mul(M_0, M_1);
        M_.getEulers(tmpd);
        this.Yaw = RAD2DEG((float) tmpd[0]);
        this.Pitch = RAD2DEG((float) tmpd[1]);
        this.Roll = RAD2DEG((float) tmpd[2]);
    }

    public void increment(Orient orient1) {
        this.add(orient1, this);
    }

    public void add(Orient orient1) {
        this.add(this, orient1);
    }

    public void sub(Orient orient1, Orient orient2) {
        orient2.getMatrix(M_0);
        orient1.getMatrix(M_1);
        M_.mulTransposeLeft(M_0, M_1);
        M_.getEulers(tmpd);
        this.Yaw = RAD2DEG((float) tmpd[0]);
        this.Pitch = RAD2DEG((float) tmpd[1]);
        this.Roll = RAD2DEG((float) tmpd[2]);
    }

    public void increment(float f, float f1, float f2) {
        Tmp.set(f, f1, f2);
        this.add(Tmp, this);
    }

    public void sub(Orient orient1) {
        this.sub(this, orient1);
    }

    public void wrap() {
        this.Yaw = (this.Yaw + 2880F) % Circ;
        this.Pitch = (this.Pitch + 2880F) % Circ;
        this.Roll = (this.Roll + 2880F) % Circ;
        if (this.Pitch > Circ_2) this.Pitch -= Circ;
        if (this.Pitch > Circ_4) {
            this.Pitch = Circ_2 - this.Pitch;
            this.Roll += Circ_2;
            this.Yaw += Circ_2;
        } else if (this.Pitch < -Circ_4) {
            this.Pitch = -Circ_2 - this.Pitch;
            this.Roll += Circ_2;
            this.Yaw += Circ_2;
        }
        while (this.Roll > Circ_2)
            this.Roll -= Circ;
        while (this.Yaw > Circ_2)
            this.Yaw -= Circ;
    }

    public void wrap360() {
        this.Yaw %= Circ;
        this.Pitch %= Circ;
        this.Roll %= Circ;
    }

    public void getMatrix(Matrix3d matrix3d) {
        this.makeMatrix(matrix3d);
    }

    public void getMatrixInv(Matrix3d matrix3d) {
        this.makeMatrixInv(matrix3d);
    }

    public void getQuat(Quat4f quat4f) {
        this.makeQuat(quat4f);
    }

    public void transform(Tuple3d tuple3d) {
        this.getMatrix(M_);
        M_.transform(tuple3d);
    }

    public void transform(Tuple3f tuple3f) {
        this.getMatrix(M_);
        M_.transform(tuple3f);
    }

    public void transform(Tuple3d tuple3d, Tuple3d tuple3d1) {
        this.getMatrix(M_);
        M_.transform(tuple3d, tuple3d1);
    }

    public void transform(Tuple3f tuple3f, Tuple3f tuple3f1) {
        this.getMatrix(M_);
        M_.transform(tuple3f, tuple3f1);
    }

    public void transformInv(Tuple3d tuple3d) {
        this.getMatrixInv(Mi_);
        Mi_.transform(tuple3d);
    }

    public void transformInv(Tuple3f tuple3f) {
        this.getMatrixInv(Mi_);
        Mi_.transform(tuple3f);
    }

    public void transformInv(Tuple3d tuple3d, Tuple3d tuple3d1) {
        this.getMatrixInv(Mi_);
        Mi_.transform(tuple3d, tuple3d1);
    }

    public void transformInv(Tuple3f tuple3f, Tuple3f tuple3f1) {
        this.getMatrixInv(Mi_);
        Mi_.transform(tuple3f, tuple3f1);
    }

    public void interpolate(Orient orient1, Orient orient2, float f) {
        orient1.getQuat(Q_0);
        orient2.getQuat(Q_1);
        Q1.interpolate(Q_0, Q_1, f);
        Q1.getEulers(tmpf);
        this.Yaw = RAD2DEG(tmpf[0]);
        this.Pitch = RAD2DEG(tmpf[1]);
        this.Roll = RAD2DEG(tmpf[2]);
    }

    public void interpolate(Orient orient1, float f) {
        this.getQuat(Q_0);
        orient1.getQuat(Q_1);
        Q_0.interpolate(Q_1, f);
        Q_0.getEulers(tmpf);
        this.Yaw = RAD2DEG(tmpf[0]);
        this.Pitch = RAD2DEG(tmpf[1]);
        this.Roll = RAD2DEG(tmpf[2]);
    }

    public int hashCode() {
        int i = Float.floatToIntBits(this.Yaw);
        int j = Float.floatToIntBits(this.Pitch);
        int k = Float.floatToIntBits(this.Roll);
        return i ^ j ^ k;
    }

    public boolean equals(Object orientObject) {
        if (!(orientObject instanceof Orient)) return false;
        Orient orient = (Orient)orientObject;
        // TODO: Fixed by SAS~Storebror: Replace test for floating point equality by equality within a certain range, due to rounding errors in floating point calculations.
        return (Math.abs(this.Yaw - orient.Yaw) < .0000001D && Math.abs(this.Pitch - orient.Pitch) < .0000001D && Math.abs(this.Roll - orient.Roll) < .0000001D);
//        return (this.Yaw == orient.Yaw && this.Pitch == orient.Pitch && this.Roll == orient.Roll);
    }

    public boolean epsilonEquals(Orient orient1, float f) {
        return Math.abs(orient1.Yaw - this.Yaw) <= f && Math.abs(orient1.Pitch - this.Pitch) <= f && Math.abs(orient1.Roll - this.Roll) <= f;
    }

    public String toString() {
        return "(" + this.Yaw + ", " + this.Pitch + ", " + this.Roll + ")";
    }

    public void orient(Vector3f vector3f) {
        VV.set(vector3f);
        this.orient(VV);
    }

    public void orient(Vector3d vector3d) {
        float f = (float) Math.cos(DEG2RAD(this.Yaw));
        float f1 = (float) Math.sin(DEG2RAD(this.Yaw));
        Vt.z = vector3d.z;
        Vt.x = vector3d.x * f + vector3d.y * f1;
        Vt.y = vector3d.y * f - vector3d.x * f1;
        this.setYPR(this.Yaw, -RAD2DEG((float) Math.atan2(Vt.x, Vt.z)), RAD2DEG((float) Math.asin(Vt.y)));
    }

    public void setAT0(Vector3f vector3f) {
        this.set(-RAD2DEG((float) Math.atan2(vector3f.y, vector3f.x)), RAD2DEG((float) Math.atan2(vector3f.z, Math.sqrt(vector3f.x * vector3f.x + vector3f.y * vector3f.y))), 0.0F);
    }

    public void setAT0(Vector3d vector3d) {
        this.set(-RAD2DEG((float) Math.atan2(vector3d.y, vector3d.x)), RAD2DEG((float) Math.atan2(vector3d.z, Math.sqrt(vector3d.x * vector3d.x + vector3d.y * vector3d.y))), 0.0F);
    }

    protected float               Yaw;
    protected float               Pitch;
    protected float               Roll;
    protected static Matrix3d     M_     = new Matrix3d();
    protected static Matrix3d     Mi_    = new Matrix3d();
    protected static Matrix3d     M_0    = new Matrix3d();
    protected static Matrix3d     M_1    = new Matrix3d();
    protected static Quat4f       Q_0    = new Quat4f();
    protected static Quat4f       Q_1    = new Quat4f();
    protected static final float  PI     = (float)Math.PI;
    private static float          tmpf[] = new float[3];
    private static double         tmpd[] = new double[3];
    private static final Orient   Tmp    = new Orient();
    private static final float    Circ   = 360F;
    private static final float    Circ_2 = 180F;
    private static final float    Circ_4 = 90F;
    private static final Quat4f   Q1     = new Quat4f();
    private static final Vector3d Vt     = new Vector3d();
    private static final Vector3d VV     = new Vector3d();

}
