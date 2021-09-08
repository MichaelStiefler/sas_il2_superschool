package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector2f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Cockpit_Mariner_Radar extends CockpitPilot {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isPadlock()) {
                hookpilot.stopPadlock();
            }
            hookpilot.reset();
            this.enter();
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
        CmdEnv.top().exec("fov 43.2");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.updateTargetsList();
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
            this.bEntered = false;
        }
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public boolean isToggleUp() {
        return false;
    }

    public boolean isToggleAim() {
        if (!this.isFocused()) {
            return false;
        } else {
            return this.radarShortRange;
        }
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused()) {
            return;
        }
        if (this.radarShortRange == flag) {
            return;
        } else {
            this.distanceMax = flag ? 2000F : 5000F;
            this.beamTurnTime = flag ? 1500L : 3000L;
            this.radarShortRange = flag;
            return;
        }
    }

    public Cockpit_Mariner_Radar() {
        super("3DO/Cockpit/Mariner-Radar/hier.him", "He111");
        this.targetAngle = new int[Cockpit_Mariner_Radar.MAX_TARGETS];
        this.targetTrackingNumber = 0;
        this.distanceMin = 100F;
        this.distanceMax = 30000F;
        this.radarShortRange = false;
        this.ViewX = 0.05F;
        this.oldBeamAngleInt = 0;
        this.lAngNoise = 0;
        this.radarPlane = new ArrayList();
        this.beamTurnTime = 10000L;
        this.bEntered = false;
        this.bNeedSetUp = true;
        this.maxNumberOfContacts = 41;
        this.contacts = new Vector2f[4][this.maxNumberOfContacts];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < this.maxNumberOfContacts; j++) {
                this.contacts[i][j] = new Vector2f();
            }

        }

    }

    public void reflectWorldToInstruments(float f) {
        if (this.fm != null) {
            if (this.bNeedSetUp) {
                this.reflectPlaneMats();
                this.bNeedSetUp = false;
            }
            this.draw();
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat1 = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat1);
        mat1 = hiermesh.material(hiermesh.materialFind("Pilot1"));
        this.mesh.materialReplace("Pilot1", mat1);
    }

    private void updateTargetsList() {
        Aircraft aircraft = World.getPlayerAircraft();
        Point3d point3d = aircraft.pos.getAbsPoint();
        Orient orient = aircraft.pos.getAbsOrient();
        this.radarPlane.clear();
        List list = Engine.targets();
        for (int i = 0; i < list.size(); i++) {
            Actor actor = (Actor) list.get(i);
            if ((actor != World.getPlayerAircraft()) && ((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric))) {
                float f = 1.0F;
                ActorHMesh actorhmesh = (ActorHMesh) actor;
                if (actorhmesh.mesh() != null) {
                    try {
                        f = actorhmesh.mesh().collisionR() / 150F;
                    } catch (Exception exception) {
                    }
                }
                Point3d point3d1 = new Point3d();
                point3d1.set(actor.pos.getAbsPoint());
                point3d1.sub(point3d);
                orient.transformInv(point3d1);
                float f1 = (float) Math.abs(point3d1.x);
                float f2 = (float) Math.abs(point3d1.y);
                float f3 = (float) Math.sqrt((f1 * f1) + (f2 * f2));
                if ((f3 <= this.distanceMax) && (f3 >= this.distanceMin)) {
                    point3d1.z = f;
                    this.radarPlane.add(point3d1);
                }
            }
        }

    }

    public void draw() {
        Aircraft aircraft = World.getPlayerAircraft();
        if (Mission.isNet() || !Actor.isValid(aircraft) || !Actor.isAlive(aircraft)) {
            return;
        }
        int i = (int) (Time.current() % this.beamTurnTime / (this.beamTurnTime / 360F));
        if (this.oldBeamAngleInt > i) {
            this.updateTargetsList();
        }
        if (i != this.oldBeamAngleInt) {
            for (int j = 0; j < Cockpit_Mariner_Radar.MAX_TARGETS; j++) {
                boolean flag = false;
                if (i > this.oldBeamAngleInt) {
                    if ((this.targetAngle[j] > this.oldBeamAngleInt) && (this.targetAngle[j] <= i)) {
                        flag = true;
                    }
                } else if (this.targetAngle[j] > this.oldBeamAngleInt) {
                    flag = true;
                } else if (this.targetAngle[j] <= i) {
                    flag = true;
                }
                if (flag) {
                    String s = "R-Signal" + (j >= 9 ? "" : "0") + "" + (j + 1);
                    if (this.mesh.isChunkVisible(s)) {
                        this.mesh.chunkVisible(s, false);
                    }
                }
            }

            for (int k = 0; k < this.radarPlane.size(); k++) {
                float f = (float) ((Point3d) this.radarPlane.get(k)).x;
                float f1 = (float) ((Point3d) this.radarPlane.get(k)).y;
                float f2 = (float) (Math.sqrt((f * f) + (f1 * f1)) / Math.sqrt((f * f) + (f1 * f1))) / this.distanceMax / Cockpit_Mariner_Radar.screenPlotFactor;
                float f4 = f * f2;
                float f6 = f1 * f2;
                float f7;
                for (f7 = (float) Math.toDegrees(Math.atan2(f, f1)); f7 < 0.0F; f7 += 360F) {
                    ;
                }
                boolean flag1 = false;
                if (i > this.oldBeamAngleInt) {
                    if ((f7 >= this.oldBeamAngleInt) && (f7 <= i)) {
                        flag1 = true;
                    }
                } else if (f7 >= this.oldBeamAngleInt) {
                    flag1 = true;
                } else if (f7 <= i) {
                    flag1 = true;
                }
                if (flag1) {
                    this.targetTrackingNumber++;
                    if (this.targetTrackingNumber >= Cockpit_Mariner_Radar.MAX_TARGETS) {
                        this.targetTrackingNumber = 0;
                    }
                    this.targetAngle[this.targetTrackingNumber] = i;
                    String s2 = "R-Signal" + (this.targetTrackingNumber >= 9 ? "" : "0") + "" + (this.targetTrackingNumber + 1);
                    String s3 = "R-Signal_" + (this.targetTrackingNumber >= 9 ? "" : "0") + "" + (this.targetTrackingNumber + 1);
                    this.mesh.setCurChunk(s2);
                    this.resetYPRmodifier();
                    Cockpit.xyz[0] = f6;
                    Cockpit.xyz[2] = f4;
                    this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                    if (!this.mesh.isChunkVisible(s2)) {
                        this.mesh.chunkVisible(s2, true);
                        int i1 = (int) (((Point3d) this.radarPlane.get(k)).z * 4D);
                        this.mesh.setCurChunk(s2);
                        switch (i1) {
                            case 0:
                                this.mesh.materialReplace(s3, "R-Signal_025");
                                break;

                            case 1:
                                this.mesh.materialReplace(s3, "R-Signal_050");
                                break;

                            case 2:
                                this.mesh.materialReplace(s3, "R-Signal_075");
                                break;

                            default:
                                this.mesh.materialReplace(s3, "R-Signal_100");
                                break;
                        }
                    }
                }
            }

            this.oldBeamAngleInt = i;
            this.mesh.chunkSetAngles("R-RadarBeam", 0.0F, i, 0.0F);
            this.mesh.chunkSetAngles("R-RadarScreen", 0.0F, i, 0.0F);
            for (int l = this.lAngNoise + 1; l <= (this.oldBeamAngleInt / 2); l++) {
                String s1 = "R-Noise" + l;
                float f3 = (float) (aircraft.pos.getAbsPoint().z - World.land().HQ(aircraft.pos.getAbsPoint().x, aircraft.pos.getAbsPoint().y));
                if (World.cur().rnd.nextFloat() > (f3 / 10000F)) {
                    this.mesh.setCurChunk(s1);
                    this.resetYPRmodifier();
                    float f5 = World.cur().rnd.nextFloat(0.0F, this.ViewX);
                    Cockpit.xyz[0] = f5 * (float) Math.cos(Geom.DEG2RAD(l * 2.0F));
                    Cockpit.xyz[2] = f5 * (float) Math.sin(Geom.DEG2RAD(l * 2.0F));
                    this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                    if (!this.mesh.isChunkVisible(s1)) {
                        this.mesh.chunkVisible(s1, false);
                    }
                } else if (this.mesh.isChunkVisible(s1)) {
                    this.mesh.chunkVisible(s1, false);
                }
            }

            this.lAngNoise = this.oldBeamAngleInt / 2;
        }
    }

    private boolean          bNeedSetUp;
    private float            saveFov;
    private boolean          bEntered;
    public boolean           bChanged;
    public int               x0;
    public int               y0;
    public Mat               mat;
    public int               ch;
    public int               mt;
    private static final int MAX_TARGETS      = 32;
    private int              targetAngle[];
    private float            ViewX;
    private long             beamTurnTime;
    private int              maxNumberOfContacts;
    private Vector2f         contacts[][];
    private int              oldBeamAngleInt;
    private int              lAngNoise;
    private ArrayList        radarPlane;
    private boolean          radarShortRange;
    private static float     screenPlotFactor = 25F;
    private int              targetTrackingNumber;
    private float            distanceMin;
    private float            distanceMax;

    static {
        Property.set(Cockpit_Mariner_Radar.class, "astatePilotIndx", 1);
    }
}
