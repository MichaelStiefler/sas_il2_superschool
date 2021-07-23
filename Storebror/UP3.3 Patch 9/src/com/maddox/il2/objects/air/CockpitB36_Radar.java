package com.maddox.il2.objects.air;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.gui.GUIRadarPanel;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB36_Radar extends CockpitPilot {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isPadlock()) {
                hookpilot.stopPadlock();
            }
            hookpilot.reset();
            this.enter();
            this.go_top();
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            this.leave();
            super.doFocusLeave();
            return;
        }
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 56");
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        Point3d point3d = new Point3d();
        point3d.set(0.0D, 0.0D, 0.0D);
        hookpilot.setTubeSight(point3d);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        this.ownAC = this.aircraft();
        if (this.RSetup != null) {
            this.RSetup._enter();
        }
        this.bEntered = true;
    }

    private void leave() {
        if (this.bEntered) {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            if (this.RSetup != null) {
                this.RSetup._leave();
            }
            this.bEntered = false;
        }
    }

    private void go_top() {
        HotKeyEnv.enable("PanView", true);
        HotKeyEnv.enable("SnapView", true);
    }

    private void go_bottom() {
        HotKeyEnv.enable("PanView", true);
        HotKeyEnv.enable("SnapView", true);
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused()) {
            return;
        }
        if (this.isToggleAim() == flag) {
            return;
        }
        if (flag) {
            this.go_bottom();
        } else {
            this.go_top();
        }
    }

    public CockpitB36_Radar() {
        super("3DO/Cockpit/B36-Radar/ANAPQ-13.him", "He111");
        this.imgW = 512;
        this.imgH = 512;
        this.x0 = 256;
        this.y0 = 256;
        this.imgCRT = new BufferedImage(this.imgW, this.imgH, 2);
        this.grCRT = this.imgCRT.createGraphics();
        this.bufInt = new int[this.imgW * this.imgH * 4];
        this.buf = new byte[this.imgW * this.imgH * 4];
        this.FOV = 1.0D;
        this.ScX = 0.0001D;
        this.ScY = 0.0001D;
        this.ScZ = 0.0001D;
        this.FOrigX = 0.0F;
        this.FOrigY = 0.0F;
        this.nTgts = 10;
        this.RLong = 50000F;
        this.RClose = 15F;
        this.ViewX = 0.05F;
        this.ViewY = 0.05F;
        this.Scale = 0;
        this.BRAze = 0.1F;
        this.BRefresh = 1300;
        this.BSteps = 12;
        this.BDiv = this.BRefresh / this.BSteps;
        this.tBOld = 0L;
        this.radarPlane = new ArrayList();
        this.lVector = 256;
        this.levels = new int[this.lVector];
        this.fIn = 0.05F;
        this.pAC = new Point3d();
        this.oAC = new Orient();
        this.pBeam = new Point3d();
        this.oBeam = new Orient();
        this.root = Main3D.cur3D().guiManager.root;
        this.RSetup = new GUIRadarPanel(this.root);
        this.AzBase = 0.0F;
        this.bRAzStab = false;
        this.bRSweep = true;
        this.fRSectAngle = 0;
        this.prepareBuf(255, 0);
        int i = this.mesh.chunkFind("R-AmberBeam");
        int j = this.mesh.materialFindInChunk("R-AmberBeamCW", i);
        this.matBeamCW = this.mesh.material(j);
        j = this.mesh.materialFindInChunk("R-AmberBeamACW", i);
        this.matBeamACW = this.mesh.material(j);
        j = this.mesh.materialFindInChunk("R-AmberBeamSw", i);
        this.matBeamSw = this.mesh.material(j);
        this.bEntered = false;
        this.bNeedSetUp = true;
    }

    public void initRadar(int i, int j, float f, float f1, int k, float f2, float f3, float f4, long l) {
        this.iRMODE = i;
        this.minAz = f;
        this.maxAz = f1;
        this.minEl = f2;
        this.maxEl = f3;
        this.stepsAz = (int) Math.round(2D * Math.PI * j * (Math.abs(this.maxAz - this.minAz) / 360F));
        this.incAz = (this.maxAz - this.minAz) / this.stepsAz;
        this.stepsEl = k;
        this.incEl = (this.maxEl - this.minEl) / this.stepsEl;
        this.tCycle = l;
        this.tTic = this.tCycle / this.stepsAz;
        this.range = f4;
        this.iVIEW = 0;
    }

    public void UpdateRadarVars() {
        if (this.RSetup.fRRange != this.range) {
            this.range = this.RSetup.fRRange;
        }
        this.fRGain = this.RSetup.fRGain;
        this.fRTunning = this.RSetup.fRTunning;
        this.bRAzStab = this.RSetup.bRAzStab;
        if (this.bRSweep != this.RSetup.bRSectScan) {
            this.bRSweep = this.RSetup.bRSectScan;
            if (!this.bRSweep) {
                this.initRadar(3, this.lVector, 0.0F, 360F, 1024, -85F, -10F, this.range, 5000L);
            } else {
                switch (this.RSetup.fRSectAngle) {
                    case -90:
                        this.initRadar(1, this.lVector, -120F, -60F, 1024, -85F, -10F, this.range, 5000L);
                        break;

                    case 0:
                        this.initRadar(1, this.lVector, -30F, 30F, 1024, -85F, -10F, this.range, 5000L);
                        break;

                    case 90:
                        this.initRadar(1, this.lVector, 60F, 120F, 1024, -85F, -10F, this.range, 5000L);
                        break;
                }
            }
        }
        if ((this.fRSectAngle != this.RSetup.fRSectAngle) && this.bRSweep) {
            switch (this.RSetup.fRSectAngle) {
                case -90:
                    this.initRadar(1, this.lVector, -120F, -60F, 1024, -85F, -10F, this.range, 5000L);
                    break;

                case 0:
                    this.initRadar(1, this.lVector, -30F, 30F, 1024, -85F, -10F, this.range, 5000L);
                    break;

                case 90:
                    this.initRadar(1, this.lVector, 60F, 120F, 1024, -85F, -10F, this.range, 5000L);
                    break;
            }
            this.fRSectAngle = this.RSetup.fRSectAngle;
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.fm != null) {
            if (this.bNeedSetUp) {
                this.initRadar(3, this.lVector, 0.0F, 360F, 1024, -85F, -10F, 5000F, 5000L);
                this.reflectPlaneMats();
                this.bNeedSetUp = false;
            }
            if (!Mission.isSingle()) {
                return;
            }
            long l = Time.current() / this.tTic;
            this.AzBase += ((((((this.bRAzStab ? this.oAC.azimut() + 90F : 0.0F) - this.AzBase) % 360F) + 540F) % 360F) - 180F) * 0.05F;
            this.to = l;
            if (Actor.isValid(this.ownAC) && Actor.isAlive(this.ownAC)) {
                this.UpdateRadarVars();
                for (int i = 0; i <= 6; i++) {
                    switch (this.iRMODE) {
                        default:
                            break;

                        case 3:
                            if (this.stepAz > this.stepsAz) {
                                this.stepAz = 0;
                            }
                            if (this.stepAz < 0) {
                                this.stepAz = this.stepsAz;
                            }
                            if (this.iVIEW != 2) {
                                this.mesh.materialReplace("R-AmberBeamCW", this.matBeamCW);
                                this.iVIEW = 2;
                                this.prepareBuf(255, 0);
                            }
                            break;

                        case 4:
                            if (this.stepAz > this.stepsAz) {
                                this.stepAz = 0;
                            }
                            if (this.stepAz < 0) {
                                this.stepAz = this.stepsAz;
                            }
                            if (this.iVIEW != 3) {
                                this.mesh.materialReplace("R-AmberBeamACW", this.matBeamCW);
                                this.iVIEW = 3;
                                this.prepareBuf(255, 0);
                            }
                            break;

                        case 1:
                            if (this.stepAz > this.stepsAz) {
                                this.stepAz = this.stepsAz;
                                this.iRMODE = 2;
                            }
                            if (this.iVIEW != 1) {
                                this.mesh.materialReplace("R-AmberBeamCW", this.matBeamSw);
                                this.iVIEW = 1;
                                this.prepareBuf(255, 0);
                            }
                            break;

                        case 2:
                            if (this.stepAz < 0) {
                                this.stepAz = 0;
                                this.iRMODE = 1;
                            }
                            if (this.iVIEW != 1) {
                                this.mesh.materialReplace("R-AmberBeamCW", this.matBeamSw);
                                this.iVIEW = 1;
                                this.prepareBuf(255, 0);
                            }
                            break;
                    }
                    this.Az = this.minAz + (this.stepAz * this.incAz);
                    this.rAz = (float) Math.toRadians(this.Az);
                    if ((i % 3) == 0) {
                        this.ownAC.pos.getAbs(this.pAC, this.oAC);
                        this.vectorInit();
                        this.drawLandscape();
                    }
                    this.vectorToImage();
                    switch (this.iRMODE) {
                        case 1:
                        case 3:
                            this.stepAz++;
                            break;

                        case 2:
                        case 4:
                            this.stepAz--;
                            break;
                    }
                }

                this.updateImage();
                this.mesh.chunkSetAngles("R-AmberBeam", 0.0F, this.AzBase + this.Az, 0.0F);
                this.mesh.chunkSetAngles("R-ScaleANAPQ13", 0.0F, this.AzBase, 0.0F);
                this.mesh.chunkSetAngles("R-ScopeMask", 0.0F, this.AzBase, 0.0F);
                this.mesh.chunkSetAngles("R-ScaleANAPQ13", 0.0F, this.AzBase, 0.0F);
                this.mesh.chunkSetAngles("R-Image", 0.0F, this.AzBase, 0.0F);
            }
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Pilot1"));
        this.mesh.materialReplace("Pilot1", mat);
    }

    protected void vectorInit() {
        for (int i = 0; i < 256; i++) {
            this.levels[i] = World.Rnd().nextInt(0, (int) (this.fRGain * 32F));
        }

    }

    protected void vectorToImage() {
        double d = Math.sin(this.rAz);
        double d1 = -Math.cos(this.rAz);
        for (int i = 0; i < 256; i++) {
            this.setA((int) Math.round(d * i) + this.x0, (int) Math.round(d1 * i) + this.y0, this.levels[i]);
        }

    }

    public void drawLandscape() {
        double d = Math.tan(Math.toRadians(this.minEl));
        double d1 = Math.tan(Math.toRadians(this.maxEl));
        double d2 = (d1 - d) / this.stepsEl;
        for (int i = 0; i < this.stepsEl; i++) {
            try {
                double d3 = d + (i * d2);
                this.pBeam.set(this.range, 0.0D, 0.0D);
                this.oBeam.set(this.oAC);
                this.oBeam.increment(this.Az, (float) Math.toDegrees(Math.atan(1.0D / d3)), 0.0F);
                this.oBeam.transform(this.pBeam);
                this.pBeam.add(this.pAC);
                Point3d point3d = new Point3d();
                Engine.land();
                if (Landscape.rayHitHQ(this.pAC, this.pBeam, point3d)) {
                    double d4 = this.pAC.distance(point3d);
                    int j = 0;
                    if (Engine.land().isWater(point3d.x, point3d.y)) {
                        j = 0;
                    } else {
                        j = (int) (this.fRGain * (32D + ((this.fRTunning * point3d.z) / 50D)));
                    }
                    if (d4 <= this.range) {
                        int k = (int) Math.round(255D * (d4 / this.range));
                        this.levels[k] += j;
                        if (this.levels[k] > 200) {
                            this.levels[k] = 200;
                        }
                    }
                }
            } catch (Exception exception) {
            }
        }

    }

    public void getSignals() {
        try {
            this.radarPlane.clear();
            List list = Engine.targets();
            int i = list.size();
            for (int j = 0; j < i; j++) {
                Actor actor = (Actor) list.get(j);
                if (actor != this.ownAC) {
                    Point3d point3d = new Point3d();
                    point3d.set(actor.pos.getAbsPoint());
                    point3d.sub(this.pAC);
                    this.oAC.transformInv(point3d);
                    double d1 = Math.abs(point3d.x);
                    double d2 = Math.abs(point3d.y);
                    if ((d1 >= this.RClose) && (d1 <= this.RLong) && (d2 >= this.RClose) && (d2 <= this.RLong)) {
                        this.radarPlane.add(point3d);
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void drawSignals() {
        try {
            int i = this.radarPlane.size();
            this.nt = 0;
            for (int k = 0; k < i; k++) {
                double d = ((Tuple3d) ((Point3d) this.radarPlane.get(k))).z;
                if (this.nt < this.nTgts) {
                    double d1 = ((Tuple3d) (this.radarPlane.get(k))).x;
                    double d2 = ((Tuple3d) (this.radarPlane.get(k))).y;
                    double d3 = Math.sqrt((d1 * d1) + (d2 * d2) + (d * d)) / Math.sqrt((d1 * d1) + (d2 * d2));
                    double d4 = -d2 * d3;
                    double d5 = d1 * d3;
                    float f = this.FOrigX + (float) (d4 * this.ScX);
                    float f1 = this.FOrigY + (float) (d5 * this.ScY);
                    if ((f > -this.ViewX) && (f < this.ViewX) && (f1 > -this.ViewY) && (f1 < this.ViewY)) {
                        this.nt++;
                        String s1 = "R-Signal" + this.nt;
                        this.mesh.setCurChunk(s1);
                        this.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                        this.resetYPRmodifier();
                        Cockpit.xyz[0] = -f;
                        Cockpit.xyz[2] = f1;
                        this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        this.mesh.render();
                        if (!this.mesh.isChunkVisible(s1)) {
                            this.mesh.chunkVisible(s1, true);
                        }
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        for (int j = this.nt + 1; j <= this.nTgts; j++) {
            String s = "R-Signal" + j;
            if (this.mesh.isChunkVisible(s)) {
                this.mesh.chunkVisible(s, false);
            }
        }

    }

    public void setRGBA(int i, int j, int k, int l, int i1, int j1) {
        int k1 = ((j * this.imgW) + i) * 4;
        this.buf[k1 + 0] = (byte) (k & 0xff);
        this.buf[k1 + 1] = (byte) (l & 0xff);
        this.buf[k1 + 1] = (byte) (i1 & 0xff);
        this.buf[k1 + 3] = (byte) (j1 & 0xff);
        this.bufEmpty = false;
        this.bChanged = true;
    }

    public void setA(int i, int j, int k) {
        int l = ((j * this.imgW) + i) * 4;
        this.buf[l + 3] = (byte) (k & 0xff);
        this.bufEmpty = false;
        this.bChanged = true;
    }

    public void prepareBuf(int i, int j) {
        for (int k = 0; k < this.imgH; k++) {
            for (int l = 0; l < this.imgW; l++) {
                int i1 = ((k * this.imgW) + l) * 4;
                this.buf[i1 + 0] = (byte) i;
                this.buf[i1 + 1] = (byte) i;
                this.buf[i1 + 2] = (byte) i;
                this.buf[i1 + 3] = (byte) j;
            }

        }

    }

    public void updateImage() {
        if (this.mat == null) {
            try {
                this.ch = this.mesh.chunkFind("R-Image");
                this.mt = this.mesh.materialFindInChunk("R-ImageWhite", this.ch);
                this.mat = this.mesh.material(this.mt);
                this.mat.setLayer(0);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            try {
                if (this.bChanged) {
                    this.mat.updateImage(this.imgW, this.imgH, 0x380004, this.buf);
                }
                this.bChanged = false;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void ints2bytes(int ai[], byte abyte0[], int i, int j, int k) {
        for (int l = 0; l < ai.length; l++) {
            int i1 = l * 3;
            int j1 = ai[l];
            abyte0[i1 + i] = (byte) j1;
            j1 >>= 8;
            abyte0[i1 + j] = (byte) j1;
            j1 >>= 8;
            abyte0[i1 + k] = (byte) j1;
        }

    }

    public static void ints2bytes(int ai[], byte abyte0[], int i, int j, int k, int l) {
        for (int i1 = 0; i1 < ai.length; i1++) {
            int j1 = i1 * 4;
            int k1 = ai[i1];
            abyte0[j1 + i] = (byte) k1;
            k1 >>= 8;
            abyte0[j1 + j] = (byte) k1;
            k1 >>= 8;
            abyte0[j1 + k] = (byte) k1;
            k1 >>= 8;
            abyte0[j1 + l] = (byte) k1;
        }

    }

    int               imgW;
    int               imgH;
    int               x0;
    int               y0;
    BufferedImage     imgCRT;
    Graphics          grCRT;
    int               bufInt[];
    byte              buf[];
    private boolean   bNeedSetUp;
    private float     saveFov;
    private boolean   bEntered;
    public boolean    bChanged;
    public Mat        mat;
    public int        ch;
    public int        mt;
    boolean           bufEmpty;
    int               nt;
    double            FOV;
    double            ScX;
    double            ScY;
    double            ScZ;
    float             FOrigX;
    float             FOrigY;
    int               nTgts;
    float             RLong;
    float             RClose;
    float             ViewX;
    float             ViewY;
    int               Scale;
    float             BRAze;
    int               BRefresh;
    int               BSteps;
    float             BDiv;
    long              tBOld;
    private ArrayList radarPlane;
    long              to;
    int               lVector;
    int               levels[];
    long              tCycle;
    long              tTic;
    float             minEl;
    float             maxEl;
    float             El;
    float             rEl;
    float             incEl;
    int               stepEl;
    int               stepsEl;
    float             minAz;
    float             maxAz;
    float             Az;
    float             rAz;
    float             incAz;
    int               stepAz;
    int               stepsAz;
    float             range;
    float             fIn;
    Point3d           pAC;
    Orient            oAC;
    Aircraft          ownAC;
    Point3d           pBeam;
    Orient            oBeam;
    GWindowRoot       root;
    GUIRadarPanel     RSetup;
    float             AzBase;
    public Mat        matBeamCW;
    public Mat        matBeamACW;
    public Mat        matBeamSw;
    public float      fRGain;
    public float      fRTunning;
    public boolean    bRAzStab;
    public boolean    bRSweep;
    public int        fRSectAngle;
    public int        iRMODE;
    public final int  MODE_NOSCAN          = 0;
    public final int  MODE_SWEEP_CW        = 1;
    public final int  MODE_SWEEP_ACW       = 2;
    public final int  MODE_SCAN_CW         = 3;
    public final int  MODE_SCAN_ACW        = 4;
    public int        iVIEW;
    public final int  VIEW_UNDEFINED       = 0;
    public final int  VIEW_SWEEP           = 1;
    public final int  VIEW_SCAN_CW         = 2;
    public final int  VIEW_SCAN_ACW        = 3;
    public final int  MODE_TRACK_NOSCAN    = 10;
    public final int  MODE_TRACK_CW        = 11;
    public final int  MODE_TRACK_ACW       = 12;
    public final int  MODE_TRACKSECTOR_CW  = 13;
    public final int  MODE_TRACKSECTOR_ACW = 14;

    static {
        Property.set(CockpitB36_Radar.class, "astatePilotIndx", 3);
    }
}
