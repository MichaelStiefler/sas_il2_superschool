/*4.10.1 class*/
package com.maddox.il2.engine;

import java.util.Map;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapXY16;

public class Landscape {
    private int                month;
    private int                day;
    public LandConf            config        = new LandConf();
    public static final float  WoodH         = 16.0F;
    public static final int    TILE          = 8;
    public static final int    TILEMASK      = 7;
    private static boolean     bNoWater      = false;
    static final int           CH_RATIO      = 4;
    static final int           CTILE         = 32;
    static final int           MTILE         = 256;
    static final int           MTILEMASK     = 255;
    public static final float  PixelSize     = 200.0F;
    public static final float  PixPerMeter   = 0.0050F;
    private static float[]     EQNBuf        = new float[4];
    private static double[]    EQNBufD       = new double[4];
    private static float[]     _RayStart     = new float[3];
    private static float[]     _RayEnd       = new float[3];
    private static float[]     _RayHit       = new float[3];
    private static double[]    _RayHitD      = new double[6];
    String                     MapName       = "";
    private Vector3f           sunRise       = new Vector3f();
    private static float[]     _sunmoon      = new float[6];
    public static final int    MESH_STEP     = 200;
    public static final float  MESH_STEP_MUL = 0.0050F;
    private static HashMapXY16 meshMapXY;
    private static HashMapExt  meshMapRay    = new HashMapExt();

    static class MeshCell {
        ActorLandMesh m;
        float         hMin;
        float         hMax;

        MeshCell(ActorLandMesh actorlandmesh, float f, float f_0_) {
            this.m = actorlandmesh;
            this.hMin = f;
            this.hMax = f_0_;
        }
    }

    public final int WORLD2PIXX(double d) {
        return (int) (d * 0.004999999888241291);
    }

    public final int WORLD2PIXX(float f) {
        return (int) (f * 0.0050F);
    }

    public final int WORLD2PIXY(double d) {
        return (int) (getSizeYpix() - 1.0F - d * 0.004999999888241291);
    }

    public final int WORLD2PIXY(float f) {
        return (int) (getSizeYpix() - 1.0F - f * 0.0050F);
    }

    public final float PIX2WORLDX(float f) {
        return f * 200.0F;
    }

    public final float PIX2WORLDY(float f) {
        return (getSizeYpix() - 1 - f) * 200.0F;
    }

    public static native int getPixelMapT(int i, int i_1_);

    public static native void setPixelMapT(int i, int i_2_, int i_3_);

    public static native int getPixelMapH(int i, int i_4_);

    public static native void setPixelMapH(int i, int i_5_, int i_6_);

    public final boolean isWater(double d, double d_7_) {
        if (bNoWater) return false;
        return isWater((float) d, (float) d_7_);
    }

    private static final native boolean isWater(float f, float f_8_);

    public static final native int estimateNoWater(int i, int i_9_, int i_10_);

    public static final native int getSizeXpix();

    public static final native int getSizeYpix();

    public float getSizeX() {
        return 200.0F * getSizeXpix();
    }

    public float getSizeY() {
        return 200.0F * getSizeYpix();
    }

    public final double Hmax(double d, double d_11_) {
        return Hmax((float) d, (float) d_11_);
    }

    public final double Hmin(double d, double d_12_) {
        return Hmin((float) d, (float) d_12_);
    }

    public static final float H(float f, float f_13_) {
        if (meshMapXY != null) {
            int i = (int) (f * 0.0050F);
            int i_14_ = (int) (f_13_ * 0.0050F);
            MeshCell meshcell = (MeshCell) meshMapXY.get(i_14_, i);
            if (meshcell != null && Actor.isValid(meshcell.m)) {
                float f_15_ = cH(f, f_13_);
                float f_16_ = meshcell.m.cHQ(f, f_13_);
                return f_16_ > f_15_ ? f_16_ : f_15_;
            }
        }
        return cH(f, f_13_);
    }

    public static final float Hmax(float f, float f_17_) {
        if (meshMapXY != null) {
            int i = (int) (f * 0.0050F);
            int i_18_ = (int) (f_17_ * 0.0050F);
            MeshCell meshcell = (MeshCell) meshMapXY.get(i_18_, i);
            if (meshcell != null && Actor.isValid(meshcell.m)) return meshcell.hMax;
        }
        return cHmax(f, f_17_);
    }

    public static final float Hmin(float f, float f_19_) {
        if (meshMapXY != null) {
            int i = (int) (f * 0.0050F);
            int i_20_ = (int) (f_19_ * 0.0050F);
            MeshCell meshcell = (MeshCell) meshMapXY.get(i_20_, i);
            if (meshcell != null && Actor.isValid(meshcell.m)) return meshcell.hMin;
        }
        return cHmin(f, f_19_);
    }

    private static final native float cH(float f, float f_21_);

    private static final native float cHmax(float f, float f_22_);

    private static final native float cHmin(float f, float f_23_);

    public final double HQ_Air(double d, double d_24_) {
        return HQ_Air((float) d, (float) d_24_);
    }

    public static final float HQ_Air(float f, float f_25_) {
        if (meshMapXY != null) {
            int i = (int) (f * 0.0050F);
            int i_26_ = (int) (f_25_ * 0.0050F);
            MeshCell meshcell = (MeshCell) meshMapXY.get(i_26_, i);
            if (meshcell != null && Actor.isValid(meshcell.m)) {
                float f_27_ = cHQ_Air(f, f_25_);
                float f_28_ = meshcell.m.cHQ(f, f_25_);
                return f_28_ > f_27_ ? f_28_ : f_27_;
            }
        }
        return cHQ_Air(f, f_25_);
    }

    private static final native float cHQ_Air(float f, float f_29_);

    public final double HQ_ForestHeightHere(double d, double d_30_) {
        return HQ_forestHeightHere((float) d, (float) d_30_);
    }

    public static final float HQ_forestHeightHere(float f, float f_31_) {
        return cHQ_forestHeightHere(f, f_31_);
    }

    private static final native float cHQ_forestHeightHere(float f, float f_32_);

    public final int HQ_RoadTypeHere(double d, double d_33_) {
        return HQRoadTypeHere((float) d, (float) d_33_);
    }

    public static final native int HQRoadTypeHere(float f, float f_34_);

    public final double HQ(double d, double d_35_) {
        return HQ((float) d, (float) d_35_);
    }

    public static final float HQ(float f, float f_36_) {
        if (meshMapXY != null) {
            int i = (int) (f * 0.0050F);
            int i_37_ = (int) (f_36_ * 0.0050F);
            MeshCell meshcell = (MeshCell) meshMapXY.get(i_37_, i);
            if (meshcell != null && Actor.isValid(meshcell.m)) {
                float f_38_ = cHQ(f, f_36_);
                float f_39_ = meshcell.m.cHQ(f, f_36_);
                return f_39_ > f_38_ ? f_39_ : f_38_;
            }
        }
        return cHQ(f, f_36_);
    }

    private static final native float cHQ(float f, float f_40_);

    public final void N(double d, double d_41_, Vector3f vector3f) {
        if (meshMapXY != null) {
            int i = (int) (d * 0.004999999888241291);
            int i_42_ = (int) (d_41_ * 0.004999999888241291);
            MeshCell meshcell = (MeshCell) meshMapXY.get(i_42_, i);
            if (meshcell != null && Actor.isValid(meshcell.m)) {
                float f = cHQ((float) d, (float) d_41_);
                float f_43_ = meshcell.m.cHQ(d, d_41_);
                if (f_43_ >= f && meshcell.m.cNormal(d, d_41_, EQNBuf)) {
                    vector3f.set(EQNBuf);
                    return;
                }
            }
        }
        EQNBuf[0] = (float) d;
        EQNBuf[1] = (float) d_41_;
        EQNBuf[2] = -1.0F;
        cEQN(EQNBuf);
        vector3f.set(EQNBuf);
    }

    public final void N(double d, double d_44_, Vector3d vector3d) {
        if (meshMapXY != null) {
            int i = (int) (d * 0.004999999888241291);
            int i_45_ = (int) (d_44_ * 0.004999999888241291);
            MeshCell meshcell = (MeshCell) meshMapXY.get(i_45_, i);
            if (meshcell != null && Actor.isValid(meshcell.m)) {
                float f = cHQ((float) d, (float) d_44_);
                float f_46_ = meshcell.m.cHQ(d, d_44_);
                if (f_46_ >= f && meshcell.m.cNormal(d, d_44_, EQNBuf)) {
                    vector3d.set(EQNBuf[0], EQNBuf[1], EQNBuf[2]);
                    return;
                }
            }
        }
        EQNBuf[0] = (float) d;
        EQNBuf[1] = (float) d_44_;
        EQNBuf[2] = -1.0F;
        cEQN(EQNBuf);
        vector3d.set(EQNBuf[0], EQNBuf[1], EQNBuf[2]);
    }

    public final void N(float f, float f_47_, Vector3f vector3f) {
        if (meshMapXY != null) {
            int i = (int) (f * 0.0050F);
            int i_48_ = (int) (f_47_ * 0.0050F);
            MeshCell meshcell = (MeshCell) meshMapXY.get(i_48_, i);
            if (meshcell != null && Actor.isValid(meshcell.m)) {
                float f_49_ = cHQ(f, f_47_);
                float f_50_ = meshcell.m.cHQ(f, f_47_);
                if (f_50_ >= f_49_ && meshcell.m.cNormal(f, f_47_, EQNBuf)) {
                    vector3f.set(EQNBuf[0], EQNBuf[1], EQNBuf[2]);
                    return;
                }
            }
        }
        EQNBuf[0] = f;
        EQNBuf[1] = f_47_;
        EQNBuf[2] = -1.0F;
        cEQN(EQNBuf);
        vector3f.set(EQNBuf[0], EQNBuf[1], EQNBuf[2]);
    }

    public final double EQN(double d, double d_51_, Vector3d vector3d) {
        if (meshMapXY != null) {
            int i = (int) (d * 0.004999999888241291);
            int i_52_ = (int) (d_51_ * 0.004999999888241291);
            MeshCell meshcell = (MeshCell) meshMapXY.get(i_52_, i);
            if (meshcell != null && Actor.isValid(meshcell.m)) {
                float f = cHQ((float) d, (float) d_51_);
                float f_53_ = meshcell.m.cHQ(d, d_51_);
                if (f_53_ >= f && meshcell.m.cPlane(d, d_51_, EQNBufD)) {
                    vector3d.set(EQNBufD[0], EQNBufD[1], EQNBufD[2]);
                    return EQNBufD[3];
                }
            }
        }
        EQNBuf[0] = (float) d;
        EQNBuf[1] = (float) d_51_;
        EQNBuf[2] = 1.0F;
        cEQN(EQNBuf);
        vector3d.set(EQNBuf[0], EQNBuf[1], EQNBuf[2]);
        return EQNBuf[3];
    }

    public final float EQN(float f, float f_54_, Vector3f vector3f) {
        if (meshMapXY != null) {
            int i = (int) (f * 0.0050F);
            int i_55_ = (int) (f_54_ * 0.0050F);
            MeshCell meshcell = (MeshCell) meshMapXY.get(i_55_, i);
            if (meshcell != null && Actor.isValid(meshcell.m)) {
                float f_56_ = cHQ(f, f_54_);
                float f_57_ = meshcell.m.cHQ(f, f_54_);
                if (f_57_ >= f_56_ && meshcell.m.cPlane(f, f_54_, EQNBufD)) {
                    vector3f.set((float) EQNBufD[0], (float) EQNBufD[1], (float) EQNBufD[2]);
                    return (float) EQNBufD[3];
                }
            }
        }
        EQNBuf[0] = f;
        EQNBuf[1] = f_54_;
        EQNBuf[2] = 1.0F;
        cEQN(EQNBuf);
        vector3f.set(EQNBuf[0], EQNBuf[1], EQNBuf[2]);
        return EQNBuf[3];
    }

    public static boolean rayHitHQ(Point3d point3d, Point3d point3d_58_, Point3d point3d_59_) {
        _RayStart[0] = (float) point3d.x;
        _RayStart[1] = (float) point3d.y;
        _RayStart[2] = (float) point3d.z;
        _RayEnd[0] = (float) point3d_58_.x;
        _RayEnd[1] = (float) point3d_58_.y;
        _RayEnd[2] = (float) point3d_58_.z;
        boolean bool = cRayHitHQ(_RayStart, _RayEnd, _RayHit);
        point3d_59_.x = _RayHit[0];
        point3d_59_.y = _RayHit[1];
        point3d_59_.z = _RayHit[2];
        if (meshMakeMapRay(point3d, point3d_58_)) {
            float f = 1.0F;
            if (bool) {
                double d = point3d.distance(point3d_58_);
                double d_60_ = point3d.distance(point3d_59_);
                if (d > 0.0) f = (float) (d_60_ / d);
            }
            _RayHitD[0] = point3d.x;
            _RayHitD[1] = point3d.y;
            _RayHitD[2] = point3d.z;
            _RayHitD[3] = point3d_58_.x;
            _RayHitD[4] = point3d_58_.y;
            _RayHitD[5] = point3d_58_.z;
            boolean bool_61_ = false;
            for (Map.Entry entry = meshMapRay.nextEntry(null); entry != null; entry = meshMapRay.nextEntry(entry)) {
                ActorLandMesh actorlandmesh = (ActorLandMesh) entry.getKey();
                float f_62_ = actorlandmesh.cRayHit(_RayHitD);
                if (f_62_ >= 0.0F && f_62_ < f) {
                    f = f_62_;
                    bool_61_ = true;
                }
            }
            meshMapRay.clear();
            if (bool_61_) {
                point3d_59_.interpolate(point3d, point3d_58_, f);
                bool = true;
            }
        }
        return bool;
    }

    private static final native void cEQN(float[] fs);

    public void setRoadsFunDrawing(boolean bool) {
        cSetRoadsFunDrawing(bool);
    }

    public static native int getFogAverageRGBA();

    public static native float getDynamicFogAlpha();

    public static native int getDynamicFogRGB();

    public void renderBridgeRoad(Mat mat, int i, int i_63_, int i_64_, int i_65_, int i_66_, float f) {
        cRenderBridgeRoad(mat.cppObject(), i, i_63_, i_64_, i_65_, i_66_, f);
    }

    public void LoadMap(String string, int[] is) throws Exception {
        this.LoadMap(string, is, true);
    }

    public void LoadMap(String string, int[] is, int i, int i_67_) throws Exception {
        this.LoadMap(string, is, true, i, i_67_);
    }

    public void UnLoadMap() {
        if (meshMapXY != null) meshMapXY.clear();
        meshMapXY = null;
        cUnloadMap();
    }

    public void ReLoadMap() throws Exception {
        if (!"".equals(this.MapName)) this.LoadMap(this.MapName, null, false, this.month, this.day);
    }

    private void LoadMap(String string, int[] is, boolean bool) throws Exception {
        this.LoadMap(string, is, bool, this.config.month, 15);
    }

    private void LoadMap(String string, int[] is, boolean bool, int i, int i_68_) throws Exception {
        this.month = i;
        this.day = i_68_;
        if (meshMapXY != null) meshMapXY.clear();
        meshMapXY = null;
        int i_69_ = 0;
        if (is != null) i_69_ = is.length / 2;
        this.MapName = string;
        this.config.set("maps/" + this.MapName);

        // TODO: Changed by |ZUTI|
        bNoWater = "ICE".equals(ZutiSupportMethods_Engine.WATER_STATE);

        World.Sun().resetCalendar();
        // TODO: +++ TD AI code backport from 4.13 +++
        World.Sun().setAstronomic(this.config.declin, this.month, this.day, World.getTimeofDay(), 0.0F);
        // TODO: --- TD AI code backport from 4.13 ---
        if (Config.isUSE_RENDER()) setAstronomic(this.config.declin, this.month, this.day, World.getTimeofDay(), World.Sun().moonPhase);
        if (!cLoadMap(string, is, i_69_, bool)) throw new RuntimeException("Landscape '" + string + "' loading error");
    }

    public void cubeFullUpdate() {
        Actor actor = Actor.getByName("camera");
        if (Actor.isValid(actor)) this.preRender((float) actor.pos.getAbsPoint().z, true);
    }

    public void cubeFullUpdate(float f) {
        this.preRender(f, true);
    }

    public int nightTime(float f, int i) {
        float f_70_ = (float) Math.toRadians(90 - this.config.declin);
        float f_71_ = (float) Math.cos(f_70_);
        float f_72_ = (float) Math.sin(f_70_);
        float f_73_ = (float) Math.toRadians(this.config.month * 30 + 15 - 80);
        int i_74_ = 0;
        int i_75_ = 0;
        for (;;) {
            float f_76_ = 6.2831855F * f / 24.0F;
            float f_77_ = (float) Math.sin(f_76_);
            float f_78_ = (float) Math.cos(f_76_);
            float f_79_ = (float) Math.sin((float) Math.toRadians(22.5) * (float) Math.sin(f_73_));
            this.sunRise.set(f_77_, f_78_ * f_71_ + f_79_ * f_72_, f_79_ * f_71_ - f_78_ * f_72_);
            this.sunRise.normalize();
            int i_80_ = 600;
            if (i_75_ + i_80_ > i) i_80_ = i - i_75_;
            if (i_80_ == 0) break;
            i_75_ += i_80_;
            if (this.sunRise.z < -0.1F) i_74_ += i_80_;
            f += i_80_ / 3600.0F;
            if (f >= 24.0F) f -= 24.0F;
        }
        return i_74_;
    }

    public void preRender(float f, boolean bool) {
        Sun sun = World.Sun();
        // TODO: +++ TD AI code backport from 4.13 +++
//		if (bool || cIsCubeUpdated())
//		{
//			sun.setAstronomic(config.declin, month, day, World.getTimeofDay());
//			setAstronomic(config.declin, month, day, World.getTimeofDay(), sun.moonPhase);
//		}
        if (bool || cIsCubeUpdated() || sun.needsCubeUpdate(f)) {
            sun.setAstronomic(this.config.declin, this.month, this.day, World.getTimeofDay(), f);
            setAstronomic(this.config.declin, this.month, this.day, World.getTimeofDay(), sun.moonPhase);
        }
        // TODO: --- TD AI code backport from 4.13 ---
        _sunmoon[0] = sun.ToSun.x;
        _sunmoon[1] = sun.ToSun.y;
        _sunmoon[2] = sun.ToSun.z;
        _sunmoon[3] = sun.ToMoon.x;
        _sunmoon[4] = sun.ToMoon.y;
        _sunmoon[5] = sun.ToMoon.z;
        cPreRender(f / 2.0F, bool, _sunmoon);
    }

    public int render0(boolean bool) {
        return cRender0(bool ? 1 : 0);
    }

    public void render1(boolean bool) {
        cRender1(bool ? 1 : 0);
    }

    public int ObjectsReflections_Begin(int i) {
        return cRefBeg(i);
    }

    public void ObjectsReflections_End() {
        cRefEnd();
    }

    public static native int getFogRGBA(float f, float f_81_, float f_82_);

    public static native int ComputeVisibilityOfLandCells(float f, int i, int i_83_, int i_84_, int i_85_, int i_86_, int i_87_, int i_88_, int i_89_, int[] is);

    public static native int Compute2DBoundBoxOfVisibleLand(float f, int i, int i_90_, float[] fs);

    public static native void MarkStaticActorsCells(int i, int i_91_, int i_92_, int i_93_, int i_94_, int[] is);

    public static native void MarkActorCellWithTrees(int i, int i_95_, int i_96_, int[] is, int i_97_);

    private static native boolean cIsCubeUpdated();

    private static native void cSetRoadsFunDrawing(boolean bool);

    private static native void cPreRender(float f, boolean bool, float[] fs);

    private static native int cRender0(int i);

    private static native void cRender1(int i);

    private static native boolean cLoadMap(String string, int[] is, int i, boolean bool);

    private static native void cUnloadMap();

    private static native void cRenderBridgeRoad(int i, int i_98_, int i_99_, int i_100_, int i_101_, int i_102_, float f);

    private static native int cRefBeg(int i);

    private static native int cRefEnd();

    private static native boolean cRayHitHQ(float[] fs, float[] fs_103_, float[] fs_104_);

    private static native void setAstronomic(int i, int i_105_, int i_106_, float f, float f_107_);

    public static boolean isExistMeshs() {
        return meshMapXY != null;
    }

    public static boolean isExistMesh(int i, int i_108_) {
        return meshMapXY.get(i_108_, i) != null;
    }

    public static void meshAdd(ActorLandMesh actorlandmesh) {
        double d = actorlandmesh.mesh().visibilityR();
        Point3d point3d = actorlandmesh.pos.getAbsPoint();
        int i = (int) ((point3d.x - d - 200.0) / 200.0);
        int i_109_ = (int) ((point3d.x + d + 200.0) / 200.0);
        int i_110_ = (int) ((point3d.y - d - 200.0) / 200.0);
        int i_111_ = (int) ((point3d.y + d + 200.0) / 200.0);
        float f = 10000.0F;
        float f_112_ = -10000.0F;
        for (int i_113_ = i_110_; i_113_ < i_111_; i_113_++)
            for (int i_114_ = i; i_114_ < i_109_; i_114_++) {
                boolean bool = false;
                double d_115_ = 25.0;
                for (int i_116_ = 1; i_116_ < 8; i_116_++) {
                    double d_117_ = i_113_ * 200.0 + i_116_ * d_115_;
                    for (int i_118_ = 1; i_118_ < 8; i_118_++) {
                        double d_119_ = i_114_ * 200.0 + i_118_ * d_115_;
                        float f_120_ = actorlandmesh.cHQ((float) d_119_, (float) d_117_);
                        if (f_120_ > -10000.0F) {
                            bool = true;
                            if (f_120_ > f_112_) f_112_ = f_120_;
                            if (f_120_ < f) f = f_120_;
                        }
                    }
                }
                if (bool) {
                    if (meshMapXY == null) meshMapXY = new HashMapXY16();
                    float f_121_ = i_114_ * 200.0F + 100.0F;
                    float f_122_ = i_113_ * 200.0F + 100.0F;
                    float f_123_ = cHmax(f_121_, f_122_);
                    float f_124_ = cHmax(f_121_, f_122_);
                    if (f_124_ > f_112_) f_112_ = f_124_;
                    if (f_123_ > f) f = f_123_;
                    meshMapXY.put(i_113_, i_114_, new MeshCell(actorlandmesh, f, f_112_));
                }
            }
    }

    private static boolean meshMakeMapRay(Point3d point3d, Point3d point3d_125_) {
        if (meshMapXY == null) return false;
        int i = (int) point3d.x / 200;
        int i_126_ = (int) point3d.y / 200;
        int i_127_ = Math.abs((int) point3d_125_.x / 200 - i) + Math.abs((int) point3d_125_.y / 200 - i_126_) + 1;
        if (i_127_ > 1000) return false;
        MeshCell meshcell = (MeshCell) meshMapXY.get(i_126_, i);
        if (meshcell != null && Actor.isValid(meshcell.m)) meshMapRay.put(meshcell.m, meshcell.m);
        if (i_127_ > 1) {
            int i_128_ = 1;
            if (point3d_125_.x < point3d.x) i_128_ = -1;
            int i_129_ = 1;
            if (point3d_125_.y < point3d.y) i_129_ = -1;
            if (Math.abs(point3d_125_.x - point3d.x) >= Math.abs(point3d_125_.y - point3d.y)) {
                double d = Math.abs(point3d.y % 200.0);
                double d_130_ = 200.0 * (point3d_125_.y - point3d.y) / Math.abs(point3d_125_.x - point3d.x);
                if (d_130_ >= 0.0) for (int i_131_ = 1; i_131_ < i_127_; i_131_++) {
                    if (d < 200.0) {
                        i += i_128_;
                        d += d_130_;
                    } else {
                        i_126_ += i_129_;
                        d -= 200.0;
                    }
                    meshcell = (MeshCell) meshMapXY.get(i_126_, i);
                    if (meshcell != null && Actor.isValid(meshcell.m)) meshMapRay.put(meshcell.m, meshcell.m);
                }
                else for (int i_132_ = 1; i_132_ < i_127_; i_132_++) {
                    if (d > 0.0) {
                        i += i_128_;
                        d += d_130_;
                    } else {
                        i_126_ += i_129_;
                        d += 200.0;
                    }
                    meshcell = (MeshCell) meshMapXY.get(i_126_, i);
                    if (meshcell != null && Actor.isValid(meshcell.m)) meshMapRay.put(meshcell.m, meshcell.m);
                }
            } else {
                double d = Math.abs(point3d.x % 200.0);
                double d_133_ = 200.0 * (point3d_125_.x - point3d.x) / Math.abs(point3d_125_.y - point3d.y);
                if (d_133_ >= 0.0) for (int i_134_ = 1; i_134_ < i_127_; i_134_++) {
                    if (d < 200.0) {
                        i_126_ += i_129_;
                        d += d_133_;
                    } else {
                        i += i_128_;
                        d -= 200.0;
                    }
                    meshcell = (MeshCell) meshMapXY.get(i_126_, i);
                    if (meshcell != null && Actor.isValid(meshcell.m)) meshMapRay.put(meshcell.m, meshcell.m);
                }
                else for (int i_135_ = 1; i_135_ < i_127_; i_135_++) {
                    if (d > 0.0) {
                        i_126_ += i_129_;
                        d += d_133_;
                    } else {
                        i += i_128_;
                        d += 200.0;
                    }
                    meshcell = (MeshCell) meshMapXY.get(i_126_, i);
                    if (meshcell != null && Actor.isValid(meshcell.m)) meshMapRay.put(meshcell.m, meshcell.m);
                }
            }
        }
        return !meshMapRay.isEmpty();
    }
}