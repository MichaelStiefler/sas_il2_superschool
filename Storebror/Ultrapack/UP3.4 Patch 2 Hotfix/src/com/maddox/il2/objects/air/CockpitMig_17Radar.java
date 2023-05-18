package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitMig_17Radar extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitMig_17Radar.this.fm != null) {
                long t = Time.current();
                long tB = (long) (t / CockpitMig_17Radar.DIVIDER);
                if (tB != CockpitMig_17Radar.this.tBOld) {
                    CockpitMig_17Radar.this.tBOld = tB;
                    CockpitMig_17Radar.this.resetYPRmodifier();
                    float x = (t % CockpitMig_17Radar.REFRESH) / CockpitMig_17Radar.REFRESH;
                    Cockpit.xyz[0] = ((2.0F * x) - 1.0F) * CockpitMig_17Radar.RANGE;
                    CockpitMig_17Radar.this.mesh.chunkSetLocate("R-RadarBeam", Cockpit.xyz, Cockpit.ypr);
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

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
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        if (!this.bEntered) {
            return;
        } else {
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
            return;
        }
    }

    private void go_top() {
        CmdEnv.top().exec("fov 43.2");
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
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
        } else {
            return;
        }
    }

    public CockpitMig_17Radar() {
        super("3DO/Cockpit/R-Izumrud/Izumrud.him", "He111");
        this.FOV = 1.0D;
        this.tBOld = 0L;
        this.radarPlane = new ArrayList();
        this.bEntered = false;
    }

    public void reflectWorldToInstruments(float f0) {
        this.draw();
    }

    public void draw() {
        try {
            if (this.aircraft() != World.getPlayerAircraft())
                return;
            if (Actor.isValid(this.aircraft()) && Actor.isAlive(this.aircraft())) {
                long curTime = (Time.current() % 1000L) / 500L;
                if (curTime != this.to) {
                    this.to = curTime;
                    Point3d pointAC = this.aircraft().pos.getAbsPoint();
                    Orient orientAC = this.aircraft().pos.getAbsOrient();
                    this.radarPlane.clear();
                    List targets = Engine.targets();
                    for (int targetsIndex = 0; targetsIndex < targets.size(); targetsIndex++) {
                        Actor targetActor = (Actor) targets.get(targetsIndex);
                        if (!(targetActor instanceof Aircraft) || (targetActor == World.getPlayerAircraft())) {
                            continue;
                        }
                        Point3d pointOrtho = new Point3d();
                        pointOrtho.set(targetActor.pos.getAbsPoint());
                        pointOrtho.sub(pointAC);
                        orientAC.transformInv(pointOrtho);
                        if ((pointOrtho.x > RANGE_CLOSE) && (pointOrtho.x < RADAR_RANGE)) {
                            this.radarPlane.add(pointOrtho);
                        }
                    }

                }
                int targetNumber = 0;
                for (int radarPlaneIndex = 0; radarPlaneIndex < this.radarPlane.size(); radarPlaneIndex++) {
                    double x = ((Point3d) this.radarPlane.get(radarPlaneIndex)).x;
                    if ((x <= RANGE_CLOSE) || (targetNumber >= MAX_TARGETS)) {
                        continue;
                    }
                    this.FOV = 26D / x;
                    double NewZ = ((Point3d) this.radarPlane.get(radarPlaneIndex)).z * this.FOV;
                    double NewX = -((Point3d) this.radarPlane.get(radarPlaneIndex)).y * this.FOV;
                    double NewY = ((Point3d) this.radarPlane.get(radarPlaneIndex)).x / RADAR_RANGE;
                    if ((NewZ > ELEVATION_MAX_POSITIVE) || (NewZ < -ELEVATION_MAX_NEGATIVE)) {
                        continue;
                    }
                    float xCoord = (float) (NewX * SCREEN_X);
                    float yCoord = BASE_COORD_Y + (float) (NewY * SCREEN_Y);
                    String signalChunkName = "R-Signal" + ++targetNumber;
                    this.mesh.setCurChunk(signalChunkName);
                    this.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                    this.resetYPRmodifier();
                    Cockpit.xyz[0] = -xCoord;
                    Cockpit.xyz[2] = yCoord;
                    this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                    this.mesh.render();
                    if (!this.mesh.isChunkVisible(signalChunkName)) {
                        this.mesh.chunkVisible(signalChunkName, true);
                    }
                }

                for (int invisibleSignalIndex = targetNumber + 1; invisibleSignalIndex <= MAX_TARGETS; invisibleSignalIndex++) {
                    String invisibleSignalChunkName = "R-Signal" + invisibleSignalIndex;
                    if (this.mesh.isChunkVisible(invisibleSignalChunkName)) {
                        this.mesh.chunkVisible(invisibleSignalChunkName, false);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float              saveFov;
    private boolean            bEntered;
    private long               to;
    private double             FOV;
    private static final float RADAR_RANGE            = 14000F;
    private long               tBOld;
    private ArrayList          radarPlane;
    private static final float ELEVATION_MAX_POSITIVE = 26.0F;
    private static final float ELEVATION_MAX_NEGATIVE = 16.0F;
    double                     NewY                   = 15D;
    private static final float SCREEN_X               = 0.01F;
    private static final float SCREEN_Y               = 0.0722F;
    private static final float BASE_COORD_Y           = -0.0264F;
    private static final int   MAX_TARGETS            = 10;
    private static final float RANGE_CLOSE            = 5F;
    private static final float RANGE                  = 0.1F;
    private static final int   REFRESH                = 1300;
    private static final int   STEPS                  = 12;
    private static final int   DIVIDER                = REFRESH / STEPS;

    static {
        Property.set(CockpitMig_17Radar.class, "astatePilotIndx", 0);
    }
}
