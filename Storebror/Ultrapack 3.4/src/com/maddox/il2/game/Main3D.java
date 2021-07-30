package com.maddox.il2.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorLandMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.BulletGeneric;
import com.maddox.il2.engine.Camera;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.ConsoleGL0;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GUIWindowManager;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookRender;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Land2D;
import com.maddox.il2.engine.Land2DText;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightEnvXY;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.RenderContext;
import com.maddox.il2.engine.Renders;
import com.maddox.il2.engine.RendersMain;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.TTFontTransform;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.engine.hotkey.FreeFly;
import com.maddox.il2.engine.hotkey.FreeFlyXYZ;
import com.maddox.il2.engine.hotkey.HookGunner;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.engine.hotkey.HookView;
import com.maddox.il2.engine.hotkey.HookViewEnemy;
import com.maddox.il2.engine.hotkey.HookViewFly;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.order.OrdersTree;
import com.maddox.il2.gui.GUI;
import com.maddox.il2.gui.GUIBWDemoPlay;
import com.maddox.il2.gui.GUIBuilder;
import com.maddox.il2.gui.GUIRecordPlay;
import com.maddox.il2.gui.GUITrainingPlay;
import com.maddox.il2.net.Connect;
import com.maddox.il2.net.NetLocalControl;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Cockpit;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.il2.objects.effects.Cinema;
import com.maddox.il2.objects.effects.DarkerNight;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.effects.LightsGlare;
import com.maddox.il2.objects.effects.OverLoad;
import com.maddox.il2.objects.effects.SpritesFog;
import com.maddox.il2.objects.effects.SunFlare;
import com.maddox.il2.objects.effects.SunGlare;
import com.maddox.il2.objects.effects.Zip;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.TestRunway;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.lights.SearchlightGeneric;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.Rocket;
import com.maddox.opengl.GLContext;
import com.maddox.opengl.gl;
import com.maddox.opengl.util.ScrShot;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Finger;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.InOutStreams;
import com.maddox.rts.KeyRecord;
import com.maddox.rts.KeyRecordCallback;
import com.maddox.rts.Keyboard;
import com.maddox.rts.Message;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;
import com.maddox.rts.cmd.CmdSFS;
import com.maddox.sound.AudioDevice;

public class Main3D extends Main {
    class CmdExit extends Cmd {

        public Object exec(CmdEnv cmdenv, Map map) {
            Main.doGameExit();
            return null;
        }

        CmdExit() {
        }
    }

    class CmdScreenSequence extends Cmd {

        public Object exec(CmdEnv cmdenv, Map map) {
            if (Main3D.this.screenSequence == null) Main3D.this.screenSequence = new ScreenSequence();
            Main3D.this.screenSequence.doSave();
            return null;
        }

        CmdScreenSequence() {
        }
    }

    class ScreenSequence extends Actor {
        class Interpolater extends Interpolate {

            public boolean tick() {
                if (ScreenSequence.this.bSave) ScreenSequence.this.shot.grab();
                return true;
            }

            Interpolater() {
            }
        }

        public void doSave() {
            this.bSave = !this.bSave;
        }

        public Object getSwitchListener(Message message) {
            return this;
        }

        protected void createActorHashCode() {
            this.makeActorRealHashCode();
        }

        boolean bSave;
        ScrShot shot;

        public ScreenSequence() {
            this.bSave = false;
            this.shot = new ScrShot("s");
            this.flags |= 0x4000;
            this.interpPut(new Interpolater(), "grabber", Time.current(), null);
        }
    }

    private static class TransformMirror extends TTFontTransform {

        public void get(float f, float f1, float af[]) {
            af[0] = this.x0 + this.dx - f;
            af[1] = this.y0 + f1;
            af[2] = this.z0;
        }

        public void set(float f, float f1, float f2, float f3) {
            this.x0 = f;
            this.y0 = f1;
            this.z0 = f2;
            this.dx = f3;
        }

        private float x0;
        private float y0;
        private float dx;
        private float z0;

        private TransformMirror() {
        }

    }

    class FarActorFilter implements ActorFilter {

        private String lenToString(int i) {
            String s;
            if (i >= 1000) s = i / 1000 + ".";
            else s = ".";
            i %= 1000;
            if (i < 10) return s + "00";
            if (i < 100) return s + "0" + i / 10;
            else return s + i / 10;
        }

        private void drawPointer(int i, double d, int j, int k) {
            double d1 = Math.atan2(k, j);
            double d2 = Math.atan2(this.p2d.y - k, this.p2d.x - j);
            if (this.p2d.z > 1.0D) if (d2 > 0.0D) d2 = -Math.PI + d2;
            else d2 = Math.PI + d2;
            double d3;
            double d4;
            if (d2 >= 0.0D) {
                if (d2 <= d1) {
                    d3 = j;
                    d4 = Math.tan(d2) * j;
                } else if (d2 <= Math.PI - d1) {
                    d4 = k;
                    d3 = Math.tan(Math.PI / 2D - d2) * k;
                } else {
                    d3 = -j;
                    d4 = -Math.tan(d2) * j;
                }
            } else if (d2 >= -d1) {
                d3 = j;
                d4 = Math.tan(d2) * j;
            } else if (d2 >= -Math.PI + d1) {
                d4 = -k;
                d3 = -Math.tan(Math.PI / 2D - d2) * k;
            } else {
                d3 = -j;
                d4 = -Math.tan(d2) * j;
            }
            d3 += j;
            d4 += k;
            HUD.addPointer((float) d3, (float) d4, Army.color(i), (float) ((1.0D - d / Main3D.this.iconDrawMax) * 0.8D + 0.2D), (float) d2);
        }

        public boolean isUse(Actor actor, double d) {
            if (actor == Main3D.this.iconFarViewActor) return false;
            if (d <= 5D) return false;
            int i = actor.getArmy();
            if (i == 0 && !Main3D.this.isBomb(actor)) return false;
            DotRange dotrange = i != World.getPlayerArmy() ? Main3D.this.dotRangeFoe : Main3D.this.dotRangeFriendly;
            double d1 = 1.0D;
            double d2 = 0.078D + 1.2F / Main3D.FOVX;
            if (Main3D.FOVX < 24F) d2 = 0.16D;
            if (actor instanceof ActorMesh && ((ActorMesh) actor).mesh() != null) {
                float f = ((ActorMesh) actor).mesh().visibilityR();
                if (f > 0.0F) if (f > 100F) {
                    float f1 = ((ActorMesh) actor).collisionR();
                    if (f1 > 0.0F) d1 = f1 * d2;
                } else d1 = f * d2;
            }
            if (actor instanceof Aircraft) {
                if (d1 < 0.65D) d1 = 0.65D;
                if (d1 > 2.2D) d1 = 2.2D;
            } else d1 *= 1.2D;
            Main3D.this.iconDrawMax = dotrange.dot(d1);
            if (d > Main3D.this.iconDrawMax) return false;
            actor.pos.getRender(this.p3d);
            if (!Main3D.this.project2d_cam(this.p3d, this.p2d)) return false;
            if (this.p2d.z > 1.0D || this.p2d.x < Main3D.this.iconClipX0 || this.p2d.x > Main3D.this.iconClipX1 || this.p2d.y < Main3D.this.iconClipY0 || this.p2d.y > Main3D.this.iconClipY1) {
                if (Main3D.this.bRenderMirror) return false;
                if (!(actor instanceof Aircraft)) return false;
                if (Main3D.this.isViewInsideShow()) return false;
                if (World.cur().diffCur.No_Icons) return false;
                if (Main3D.this.iRenderIndx == 0) this.drawPointer(i, d, Main3D.this.render2D.getViewPortWidth() / 2, Main3D.this.render2D.getViewPortHeight() / 2);
                return false;
            }
            int j = (int) (this.p2d.x - 1.0D);
            int k = (int) (this.p2d.y - 0.5D);
            int l = 0x7f7f7f;
            int i1 = 255;
            int j1 = 0;
            if (Main3D.this.bEnableFog) {
                Render.enableFog(true);
                l = Landscape.getFogRGBA((float) this.p3d.x, (float) this.p3d.y, (float) this.p3d.z);
                j1 = l >>> 24;
                i1 -= j1;
                Render.enableFog(false);
            }
            int k1 = ((int) (dotrange.alphaDot(d * 2.2D, d1) * 255D) & 0xff) << 24;
            int l1 = ((int) (dotrange.alphaDot(d, d1) * 255D) & 0xff) << 24;
            int i2 = k1 | (Main3D.iconFarColor & 0xff) * i1 + (l & 0xff) * j1 >>> 8 | (Main3D.iconFarColor >>> 8 & 0xff) * i1 + (l >>> 8 & 0xff) * j1 >>> 8 << 8 | (Main3D.iconFarColor >>> 16 & 0xff) * i1 + (l >>> 16 & 0xff) * j1 >>> 8 << 16;
            int j2 = l1 | (Main3D.iconFarColor >>> 1 & 0x3f) * i1 + (l >>> 0 & 0xff) * j1 >>> 8 | (Main3D.iconFarColor >>> 9 & 0x3f) * i1 + (l >>> 8 & 0xff) * j1 >>> 8 << 8 | (Main3D.iconFarColor >>> 17 & 0x3f) * i1 + (l >>> 16 & 0xff) * j1 >>> 8 << 16;
            if (actor instanceof Aircraft) {
                if (d > Main3D.this.iconAirDrawMin) {
                    Render.drawTile(j, k + 1.0F, 2.0F, 1.0F, (float) -this.p2d.z, Main3D.this.iconFarMat, i2, 0.0F, 1.0F, 1.0F, -1F);
                    Render.drawTile(j, k, 2.0F, 1.0F, (float) -this.p2d.z, Main3D.this.iconFarMat, j2, 0.0F, 1.0F, 1.0F, -1F);
                }
            } else if (Main3D.this.isBomb(actor)) {
                if (d > Main3D.this.iconSmallDrawMin) {
                    Render.drawTile(j, k + 1.0F, 1.0F, 1.0F, (float) -this.p2d.z, Main3D.this.iconFarMat, i2, 0.0F, 1.0F, 1.0F, -1F);
                    Render.drawTile(j, k, 1.0F, 1.0F, (float) -this.p2d.z, Main3D.this.iconFarMat, j2, 0.0F, 1.0F, 1.0F, -1F);
                }
            } else if (d > Main3D.this.iconGroundDrawMin) {
                Render.drawTile(j, k + 1.0F, 2.0F, 1.0F, (float) -this.p2d.z, Main3D.this.iconFarMat, i2, 0.0F, 1.0F, 1.0F, -1F);
                Render.drawTile(j, k, 2.0F, 1.0F, (float) -this.p2d.z, Main3D.this.iconFarMat, j2, 0.0F, 1.0F, 1.0F, -1F);
            }
            k += 8;
            if (actor == Main3D.this.iconFarPadlockActor && Main3D.this.iconTypes() != 0) {
                Main3D.this.iconFarPadlockItem.set(dotrange.colorIcon(d, i, i1), j, k, 0, (float) -this.p2d.z, "");
                Main3D.this.iconFarPadlockItem.bGround = !(actor instanceof Aircraft);
                if (World.cur().diffCur.No_Icons) {
                    int k2 = ((int) (dotrange.alphaIcon(d) * i1) & 0xff) << 24;
                    Main3D.this.iconFarPadlockItem.color = k2 | 0xff00;
                }
            }
            if (!(actor instanceof Aircraft)) return false;
            if (World.cur().diffCur.No_Icons) return false;
            if (Main3D.this.iconTypes() == 0) return false;
            if (d >= dotrange.icon()) return false;
            String s = null;
            String s1 = null;
            String s2 = null;
            switch (Main3D.this.iconTypes()) {
                case 3: // '\003'
                default:
                    if (d <= dotrange.type()) {
                        s = Property.stringValue(actor.getClass(), Main3D.this.iconFarFinger, null);
                        if (s == null) {
                            s = actor.getClass().getName();
                            int l2 = s.lastIndexOf('.');
                            s = s.substring(l2 + 1);
                            Property.set(actor.getClass(), "iconFar_shortClassName", s);
                        }
                    }
                    // fall through

                case 2: // '\002'
                    if (d <= dotrange.id()) s1 = ((Aircraft) actor).typedName();
                    if (Mission.isNet() && ((NetAircraft) actor).isNetPlayer() && i == World.getPlayerArmy() && d <= dotrange.name()) {
                        NetUser netuser = ((NetAircraft) actor).netUser();
                        if (s1 != null) s1 = s1 + " " + netuser.uniqueName();
                        else s1 = netuser.uniqueName();
                    }
                    break;

                case 1: // '\001'
                    break;
            }
            if (d <= dotrange.range()) s2 = this.lenToString((int) d);
            String s3 = null;
            if (s2 != null) s3 = s2;
            if (s != null) if (s3 != null) s3 = s3 + " " + s;
            else s3 = s;
            if (s1 != null) if (s3 != null) s3 = s3 + ":" + s1;
            else s3 = s1;
            if (s3 != null) Main3D.this.insertFarActorItem(dotrange.colorIcon(d, i, i1), j, k, (float) -this.p2d.z, s3);
            return false;
        }

        Point3d p3d;
        Point3d p2d;
        Point3d camp;

        FarActorFilter() {
            this.p3d = new Point3d();
            this.p2d = new Point3d();
            this.camp = new Point3d();
        }
    }

    static class FarActorItem {

        public void set(int i, int j, int k, int l, float f, String s) {
            this.color = i;
            this.x = j;
            this.y = k;
            this.dx = l;
            this.z = f;
            this.str = s;
        }

        public int     color;
        public int     x;
        public int     y;
        public int     dx;
        public float   z;
        public String  str;
        public boolean bGround;

        public FarActorItem(int i, int j, int k, int l, float f, String s) {
            this.set(i, j, k, l, f, s);
        }
    }

    public class RenderMap2D extends Render {

        protected void contextResize(int i, int j) {
            super.contextResize(i, j);
            Main3D.this.renderMap2DcontextResize(i, j);
            if (Main3D.this.land2DText != null) Main3D.this.land2DText.contextResized();
        }

        public void preRender() {
            Main3D.this.preRenderMap2D();
            if (Main.state() != null && Main.state().id() == 18) {
                GUIBuilder guibuilder = (GUIBuilder) Main.state();
                guibuilder.builder.preRenderMap2D();
            }
        }

        public void render() {
            if (Main3D.this.land2D != null) Main3D.this.land2D.render();
            if (Main.state() != null && Main.state().id() == 18) {
                GUIBuilder guibuilder = (GUIBuilder) Main.state();
                guibuilder.builder.renderMap2D();
            } else if (Main3D.this.land2DText != null) Main3D.this.land2DText.render();
            Main3D.this.renderMap2D();
        }

        public RenderMap2D(float f) {
            this(Engine.rendersMain(), f);
        }

        public RenderMap2D(Renders renders, float f) {
            super(renders, f);
            this.useClearDepth(false);
            this.setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
        }
    }

    public class RenderHUD extends Render {

        protected void contextResize(int i, int j) {
            super.contextResize(i, j);
            Main3D.this.renderHUDcontextResize(i, j);
            Main3D.this.hud.contextResize(i, j);
            this.renders().setCommonClearColor(this.viewPort[0] != 0.0F || this.viewPort[1] != 0.0F);
        }

        public void preRender() {
            Main3D.this.hud.preRender();
            Main3D.this.preRenderHUD();
        }

        public void render() {
            Main3D.this.hud.render();
            Main3D.this.renderHUD();
        }

        public RenderHUD(float f) {
            this(Engine.rendersMain(), f);
        }

        public RenderHUD(Renders renders, float f) {
            super(renders, f);
            this.useClearDepth(false);
            this.useClearColor(false);
        }
    }

    public class RenderCockpitMirror extends Render {

        protected void contextResize(int i, int j) {
            this.setViewPort(new int[] { Main3D.this.mirrorX0(), Main3D.this.mirrorY0(), Main3D.this.mirrorWidth(), Main3D.this.mirrorHeight() });
            if (this.camera != null) ((Camera3D) this.camera).set(((Camera3D) this.camera).FOV(), (float) Main3D.this.mirrorWidth() / (float) Main3D.this.mirrorHeight());
        }

        public boolean isShow() {
            if (Main3D.this.viewMirror > 0 && Main3D.this.renderCockpit.isShow()) return Main3D.this.cockpitCur.isExistMirror();
            else return false;
        }

        public void preRender() {
            if (Actor.isValid(Main3D.this.cockpitCur) && Main3D.this.cockpitCur.isFocused()) Main3D.this.cockpitCur.preRender(true);
        }

        public void render() {
            if (Actor.isValid(Main3D.this.cockpitCur) && Main3D.this.cockpitCur.isFocused()) {
                Main3D.this.cockpitCur.render(true);
                Render.flush();
                Main3D.this.cockpitCur.grabMirrorFromScreen(Main3D.this.mirrorX0(), Main3D.this.mirrorY0(), Main3D.this.mirrorWidth(), Main3D.this.mirrorHeight());
            }
        }

        public RenderCockpitMirror(float f) {
            super(Engine.rendersMain(), f);
            this.useClearDepth(true);
            this.useClearColor(false);
            this.contextResized();
        }
    }

    public class RenderCockpit extends Render {

        public void preRender() {
            Main3D.this.iRenderIndx = this._indx;
            if (Actor.isValid(Main3D.this.cockpitCur) && Main3D.this.cockpitCur.isFocused()) Main3D.this.cockpitCur.preRender(false);
            Main3D.this.iRenderIndx = 0;
        }

        public void render() {
            Main3D.this.iRenderIndx = this._indx;
            if (Actor.isValid(Main3D.this.cockpitCur) && Main3D.this.cockpitCur.isFocused()) Main3D.this.cockpitCur.render(false);
            Main3D.this.iRenderIndx = 0;
        }

        public void getAspectViewPort(float af[]) {
            if (this._indx == 0) {
                super.getAspectViewPort(af);
                return;
            } else {
                Main3D.this._getAspectViewPort(this._indx, af);
                return;
            }
        }

        public void getAspectViewPort(int ai[]) {
            if (this._indx == 0) {
                super.getAspectViewPort(ai);
                return;
            } else {
                Main3D.this._getAspectViewPort(this._indx, ai);
                return;
            }
        }

        public boolean isShow() {
            if (this._indx == 0) return super.isShow();
            else return Config.cur.isUse3Renders() && Main3D.this.renderCockpit.isShow();
        }

        int _indx;

        public RenderCockpit(int i, float f) {
            this(i, Engine.rendersMain(), f);
        }

        public RenderCockpit(int i, Renders renders, float f) {
            super(renders, f);
            this._indx = 0;
            this._indx = i;
            this.useClearDepth(true);
            this.useClearColor(false);
            this.contextResized();
        }
    }

    public class Render2DMirror extends Render {

        protected void contextResize(int i, int j) {
            this.setViewPort(new int[] { Main3D.this.mirrorX0(), Main3D.this.mirrorY0(), Main3D.this.mirrorWidth(), Main3D.this.mirrorHeight() });
            if (this.camera != null) ((CameraOrtho2D) this.camera).set(0.0F, Main3D.this.mirrorWidth(), 0.0F, Main3D.this.mirrorHeight());
        }

        public boolean isShow() {
            return Main3D.this.viewMirror > 0 && Main3D.this.render3D0.isShow() && !Main3D.this.isViewOutside() && Main3D.this.cockpitCur.isExistMirror();
        }

        public void render() {
            Main3D.this.bRenderMirror = true;
            if (Main3D.this.bEnableFog) Render.enableFog(false);
            Main3D.this.drawFarActors();
            if (Main3D.this.bEnableFog) Render.enableFog(true);
            Main3D.this.bRenderMirror = false;
        }

        public Render2DMirror(float f) {
            super(Engine.rendersMain(), f);
            this.useClearDepth(false);
            this.useClearColor(false);
            this.contextResized();
        }
    }

    public class Render3D1Mirror extends Render3DMirror {

        public void render() {
            Main3D.this.bRenderMirror = true;
            Main3D.this.doRender3D1(this);
            Main3D.this.bRenderMirror = false;
        }

        public Render3D1Mirror(float f) {
            super(f);
            this.useClearColor(false);
            this.useClearDepth(false);
            this.useClearStencil(false);
        }
    }

    public class Render3D0Mirror extends Render3DMirror {

        public void preRender() {
            Main3D.this.bRenderMirror = true;
            Main3D.this.doPreRender3D(this);
            Main3D.this.bRenderMirror = false;
        }

        public void render() {
            this.camera.activateWorldMode(0);
            gl.GetDoublev(2982, Main3D.this._modelMatrix3DMirror);
            gl.GetDoublev(2983, Main3D.this._projMatrix3DMirror);
            gl.GetIntegerv(2978, Main3D.this._viewportMirror);
            this.camera.deactivateWorldMode();
            Main3D.this.bRenderMirror = true;
            Main3D.this.doRender3D0(this);
            Main3D.this.bRenderMirror = false;
        }

        public Render3D0Mirror(float f) {
            super(f);
            this.setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            this.useClearStencil(true);
        }
    }

    public class Render3DMirror extends Render {

        protected void contextResize(int i, int j) {
            this.setViewPort(new int[] { Main3D.this.mirrorX0(), Main3D.this.mirrorY0(), Main3D.this.mirrorWidth(), Main3D.this.mirrorHeight() });
            if (this.camera != null) ((Camera3D) this.camera).set(((Camera3D) this.camera).FOV(), (float) Main3D.this.mirrorWidth() / (float) Main3D.this.mirrorHeight());
        }

        public boolean isShow() {
            return Main3D.this.viewMirror > 0 && Main3D.this.render3D0.isShow() && !Main3D.this.isViewOutside() && Main3D.this.cockpitCur.isExistMirror();
        }

        public Render3DMirror(float f) {
            super(Engine.rendersMain(), f);
            this.contextResized();
        }
    }

    public class Render2D extends Render {

        public void render() {
            Main3D.this.iRenderIndx = this._indx;
            if (Main3D.this.bEnableFog) Render.enableFog(false);
            Main3D.this.drawFarActors();
            if (Main3D.this.bEnableFog) Render.enableFog(true);
            Main3D.this.iRenderIndx = 0;
        }

        public void getAspectViewPort(float af[]) {
            if (this._indx == 0) {
                super.getAspectViewPort(af);
                return;
            } else {
                Main3D.this._getAspectViewPort(this._indx, af);
                return;
            }
        }

        public void getAspectViewPort(int ai[]) {
            if (this._indx == 0) {
                super.getAspectViewPort(ai);
                return;
            } else {
                Main3D.this._getAspectViewPort(this._indx, ai);
                return;
            }
        }

        public boolean isShow() {
            if (this._indx == 0) return super.isShow();
            if (Main.state() != null && Main.state().id() == 18) return false;
            else return Config.cur.isUse3Renders() && Main3D.this.render2D.isShow();
        }

        int _indx;

        public Render2D(int i, float f) {
            this(i, Engine.rendersMain(), f);
        }

        public Render2D(int i, Renders renders, float f) {
            super(renders, f);
            this._indx = 0;
            this._indx = i;
            this.useClearDepth(false);
            this.useClearColor(false);
            this.contextResized();
        }
    }

    public class Render3D1 extends Render {

        public void preRender() {
            if (this._indx == 0) Main3D.this.drawTime();
        }

        public void render() {
            Main3D.this.iRenderIndx = this._indx;
            Main3D.this.doRender3D1(this);
            if (Main.state() != null && Main.state().id() == 18 && Main3D.this.iRenderIndx == 0) {
                GUIBuilder guibuilder = (GUIBuilder) Main.state();
                guibuilder.builder.render3D();
            }
            Main3D.this.iRenderIndx = 0;
        }

        public void getAspectViewPort(float af[]) {
            if (this._indx == 0) {
                super.getAspectViewPort(af);
                return;
            } else {
                Main3D.this._getAspectViewPort(this._indx, af);
                return;
            }
        }

        public void getAspectViewPort(int ai[]) {
            if (this._indx == 0) {
                super.getAspectViewPort(ai);
                return;
            } else {
                Main3D.this._getAspectViewPort(this._indx, ai);
                return;
            }
        }

        public boolean isShow() {
            if (this._indx == 0) return super.isShow();
            if (Main.state() != null && Main.state().id() == 18) return false;
            else return Config.cur.isUse3Renders() && Main3D.this.render3D1.isShow();
        }

        int _indx;

        public Render3D1(int i, float f) {
            this(i, Engine.rendersMain(), f);
        }

        public Render3D1(int i, Renders renders, float f) {
            super(renders, f);
            this._indx = 0;
            this._indx = i;
            this.useClearColor(false);
            this.useClearDepth(false);
            this.useClearStencil(false);
            this.contextResized();
        }
    }

    public class Render3D0 extends Render {

        public void preRender() {
            Main3D.this.iRenderIndx = this._indx;
            if (this._indx == 0) Main3D.this.shadowPairsClear();
            Main3D.this.doPreRender3D(this);
            Main3D.this.iRenderIndx = 0;
        }

        public void render() {
            Main3D.this.iRenderIndx = this._indx;
            this.camera.activateWorldMode(0);
            gl.GetDoublev(2982, Main3D.this._modelMatrix3D[Main3D.this.iRenderIndx]);
            gl.GetDoublev(2983, Main3D.this._projMatrix3D[Main3D.this.iRenderIndx]);
            gl.GetIntegerv(2978, Main3D.this._viewport[Main3D.this.iRenderIndx]);
            this.camera.deactivateWorldMode();
            Main3D.this.doRender3D0(this);
            Main3D.this.iRenderIndx = 0;
        }

        public void getAspectViewPort(float af[]) {
            if (this._indx == 0) {
                super.getAspectViewPort(af);
                return;
            } else {
                Main3D.this._getAspectViewPort(this._indx, af);
                return;
            }
        }

        public void getAspectViewPort(int ai[]) {
            if (this._indx == 0) {
                super.getAspectViewPort(ai);
                return;
            } else {
                Main3D.this._getAspectViewPort(this._indx, ai);
                return;
            }
        }

        public boolean isShow() {
            if (this._indx == 0) return super.isShow();
            if (Main.state() != null && Main.state().id() == 18) return false;
            else return Config.cur.isUse3Renders() && Main3D.this.render3D0.isShow();
        }

        int _indx;

        public Render3D0(int i, float f) {
            this(i, Engine.rendersMain(), f);
        }

        public Render3D0(int i, Renders renders, float f) {
            super(renders, f);
            this._indx = 0;
            this._indx = i;
            this.setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            this.useClearStencil(true);
            this.contextResized();
        }
    }

    class DrwArray {

        ArrayList drwSolidPlate;
        ArrayList drwTranspPlate;
        ArrayList drwShadowPlate;
        ArrayList drwSolid;
        ArrayList drwTransp;
        ArrayList drwShadow;

        DrwArray() {
            this.drwSolidPlate = new ArrayList();
            this.drwTranspPlate = new ArrayList();
            this.drwShadowPlate = new ArrayList();
            this.drwSolid = new ArrayList();
            this.drwTransp = new ArrayList();
            this.drwShadow = new ArrayList();
        }
    }

    class ShadowPairsFilter implements ActorFilter {

        public boolean isUse(Actor actor, double d) {
            if (actor != Main3D.this.shadowPairsCur1 && actor instanceof ActorHMesh && d <= Main3D.shadowPairsR2 && ((ActorHMesh) actor).hierMesh() != null) {
                Main3D.this.shadowPairsList2.add(Main3D.this.shadowPairsCur1.hierMesh());
                Main3D.this.shadowPairsList2.add(((ActorHMesh) actor).hierMesh());
            }
            return false;
        }

        ShadowPairsFilter() {
        }
    }

    public class Render3D0R extends Render {

        public void preRender() {
            if (Engine.land().ObjectsReflections_Begin(0) == 1) {
                this.getCamera().pos.getRender(Main3D.this.__p, Main3D.this.__o);
                boolean flag = true;
                if (Landscape.isExistMeshs()) {
                    this.drwSolid.clear();
                    this.drwTransp.clear();
                    Engine.drawEnv().preRender(Main3D.this.__p.x, Main3D.this.__p.y, Main3D.this.__p.z, World.MaxVisualDistance * 0.5F, 1, this.drwSolidL, this.drwTranspL, null, flag);
                    int i = this.drwSolidL.size();
                    for (int j = 0; j < i; j++) {
                        Actor actor = (Actor) this.drwSolidL.get(j);
                        if (actor instanceof ActorLandMesh) this.drwSolid.add(actor);
                    }

                    i = this.drwTranspL.size();
                    for (int k = 0; k < i; k++) {
                        Actor actor1 = (Actor) this.drwTranspL.get(k);
                        if (actor1 instanceof ActorLandMesh) this.drwTransp.add(actor1);
                    }

                    flag = false;
                }
                Engine.drawEnv().preRender(Main3D.this.__p.x, Main3D.this.__p.y, Main3D.this.__p.z, World.MaxVisualDistance * 0.5F, 14, this.drwSolid, this.drwTransp, null, flag);
                Engine.land().ObjectsReflections_End();
            }
        }

        public void render() {
            if (Engine.land().ObjectsReflections_Begin(1) == 1) {
                this.draw(this.drwSolid, this.drwTransp);
                Engine.land().ObjectsReflections_End();
            }
        }

        public boolean isShow() {
            return Main3D.this.bDrawLand && Main3D.this.render3D0.isShow();
        }

        ArrayList drwSolidL;
        ArrayList drwTranspL;
        ArrayList drwSolid;
        ArrayList drwTransp;

        public Render3D0R(float f) {
            super(Engine.rendersMain(), f);
            this.drwSolidL = new ArrayList();
            this.drwTranspL = new ArrayList();
            this.drwSolid = new ArrayList();
            this.drwTransp = new ArrayList();
        }
    }

    public static class HookReflection extends HookRender {

        public boolean computeRenderPos(Actor actor, Loc loc, Loc loc1) {
            this.computePos(actor, loc, loc1);
            return true;
        }

        public void computePos(Actor actor, Loc loc, Loc loc1) {
// Point3d point3d = loc.getPoint();
// Orient orient = loc.getOrient();
            loc1.set(loc);
        }

        public HookReflection() {
        }
    }

    public static class Camera3DR extends Camera3D {

        public boolean activate(float f, int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2) {
            Engine.land().ObjectsReflections_Begin(0);
            return super.activate(f, i / 2, j / 2, k, l, i1 / 2, j1 / 2, k1, l1, i2 / 2, j2 / 2);
        }

        public Camera3DR() {
        }
    }

    public class NetCamera3D extends Camera3D {

        public void set(float f) {
            super.set(f);
            Main3D.this._camera3D[1].set(f);
            Main3D.this._camera3D[1].pos.setRel(new Orient(-f, 0.0F, 0.0F));
            Main3D.this._camera3D[2].set(f);
            Main3D.this._camera3D[2].pos.setRel(new Orient(f, 0.0F, 0.0F));
            Main3D.this._cameraCockpit[1].set(f);
            Main3D.this._cameraCockpit[1].pos.setRel(new Orient(-f, 0.0F, 0.0F));
            Main3D.this._cameraCockpit[2].set(f);
            Main3D.this._cameraCockpit[2].pos.setRel(new Orient(f, 0.0F, 0.0F));
            Main3D.this.camera3DR.set(f);
        }

        public NetCamera3D() {
        }
    }

    public Main3D() {
        this.bDrawIfNotFocused = false;
        this.bUseStartLog = false;
        this.bUseGUI = true;
        this.bShowStartIntro = false;
        this._overLoad = new OverLoad[3];
        this._sunGlare = new SunGlare[3];
        this._lightsGlare = new LightsGlare[3];
        this._sunFlare = new SunFlare[3];
        this._sunFlareRender = new Render[3];
        this.bViewFly = false;
        this.bViewEnemy = false;
        this.bEnableFog = true;
        this.bDrawLand = false;
        this.bDrawClouds = false;
        this._cinema = new Cinema[3];
        this._render3D0 = new Render3D0[3];
        this._render3D1 = new Render3D1[3];
        this._camera3D = new Camera3D[3];
        this._render2D = new Render2D[3];
        this._camera2D = new CameraOrtho2D[3];
        this._renderCockpit = new RenderCockpit[3];
        this._cameraCockpit = new Camera3D[3];
        this.viewMirror = 2;
        this.iconTypes = 3;
        this.bLoadRecordedStates1Before = false;
        this.bRenderMirror = false;
        this.iRenderIndx = 0;
        this._modelMatrix3D = new double[3][16];
        this._projMatrix3D = new double[3][16];
        this._viewport = new int[3][4];
        this._modelMatrix3DMirror = new double[16];
        this._projMatrix3DMirror = new double[16];
        this._viewportMirror = new int[4];
        this._dIn = new double[4];
        this._dOut = new double[4];
        this.shadowPairsList1 = new ArrayList();
        this.shadowPairsMap1 = new HashMap();
        this.shadowPairsCur1 = null;
        this.shadowPairsList2 = new ArrayList();
        this.shadowPairsFilter = new ShadowPairsFilter();
        this.drwMirror = new DrwArray();
        this.__l = new Loc();
        this.__p = new Point3d();
        this.__o = new Orient();
        this.__v = new Vector3d();
        this.bShowTime = false;
        this.iconGroundDrawMin = 200D;
        this.iconSmallDrawMin = 100D;
        this.iconAirDrawMin = 1000D;
        this.iconDrawMax = 10000D;
        this.iconFarPadlockItem = new FarActorItem(0, 0, 0, 0, 0.0F, null);
        this.farActorFilter = new FarActorFilter();
        this.line3XYZ = new float[9];
        this._lineP = new Point3d();
        this._lineO = new Orient();
        this.transformMirror = new TransformMirror();
        this.lastTimeScreenShot = 0L;
        this.scrShot = null;
    }

    public static Main3D cur3D() {
        return (Main3D) cur();
    }

    public boolean isUseStartLog() {
        return this.bUseStartLog;
    }

    public boolean isShowStartIntro() {
        return this.bShowStartIntro;
    }

    public boolean isDrawLand() {
        return this.bDrawLand;
    }

    public void setDrawLand(boolean flag) {
        this.bDrawLand = flag;
    }

    public boolean isDemoPlaying() {
        if (this.playRecordedStreams != null) return true;
        if (this.keyRecord == null) return false;
        else return this.keyRecord.isPlaying();
    }

    public Actor viewActor() {
        return this.camera3D.pos.base();
    }

    public boolean isViewInsideShow() {
        if (!Actor.isValid(this.cockpitCur)) return true;
        else return !this.cockpitCur.isNullShow();
    }

    public boolean isEnableRenderingCockpit() {
        if (!Actor.isValid(this.cockpitCur)) return true;
        else return this.cockpitCur.isEnableRendering();
    }

    public boolean isViewOutside() {
        if (!Actor.isValid(this.cockpitCur)) return true;
        else return !this.cockpitCur.isFocused();
    }

    public boolean isViewPadlock() {
        if (!Actor.isValid(this.cockpitCur)) return false;
        else return this.cockpitCur.isPadlock();
    }

    public Actor getViewPadlockEnemy() {
        if (!Actor.isValid(this.cockpitCur)) return null;
        else return this.cockpitCur.getPadlockEnemy();
    }

    public void setViewInsideShow(boolean flag) {
        if (this.isViewOutside() || this.isViewInsideShow() == flag) return;
        if (!Actor.isValid(this.cockpitCur)) return;
        else {
            this.cockpitCur.setNullShow(!flag);
            return;
        }
    }

    public void setEnableRenderingCockpit(boolean flag) {
        if (this.isViewOutside() || this.isEnableRenderingCockpit() == flag) return;
        if (!Actor.isValid(this.cockpitCur)) return;
        else {
            this.cockpitCur.setEnableRendering(flag);
            return;
        }
    }

    private void endViewFly() {
        if (!this.bViewFly) return;
        else {
            this.hookViewFly.use(false);
            Engine.soundListener().setUseBaseSpeed(true);
            this.bViewFly = false;
            return;
        }
    }

    private void endViewEnemy() {
        if (!this.bViewEnemy) return;
        else {
            this.hookViewEnemy.stop();
            this.bViewEnemy = false;
            return;
        }
    }

    private void endView() {
        if (!this.isViewOutside()) return;
        if (this.bViewFly || this.bViewEnemy) return;
        else {
            this.hookView.use(false);
            return;
        }
    }

    private void endViewInside() {
        if (this.isViewOutside()) return;
        else {
            this.cockpitCur.focusLeave();
            return;
        }
    }

    public void setViewFly(Actor actor) {
        this.endView();
        this.endViewEnemy();
        this.endViewInside();
        Selector.resetGame();
        this.hookViewFly.use(true);
        this.bViewFly = true;
        this.camera3D.pos.setRel(new Point3d(), new Orient());
        this.camera3D.pos.changeBase(actor, this.hookViewFly, false);
        this.camera3D.pos.resetAsBase();
        Engine.soundListener().setUseBaseSpeed(false);
    }

    private void setViewSomebody(Actor actor) {
        this.endView();
        this.endViewFly();
        this.endViewInside();
        this.bViewEnemy = true;
        this.camera3D.pos.setRel(new Point3d(), new Orient());
        this.camera3D.pos.changeBase(actor, this.hookViewEnemy, false);
        this.camera3D.pos.resetAsBase();
        if (actor instanceof ActorViewPoint) ((ActorViewPoint) actor).setViewActor(this.hookViewEnemy.enemy());
    }

    public void setViewEnemy(Actor actor, boolean flag, boolean flag1) {
        Actor actor1 = Selector.look(flag, flag1, this.camera3D, actor.getArmy(), -1, actor, true);
        if (!this.hookViewEnemy.start(actor, actor1, flag1, true)) {
            if (this.bViewEnemy) this.setView(actor);
            return;
        } else {
            this.setViewSomebody(actor);
            return;
        }
    }

    public void setViewFriend(Actor actor, boolean flag, boolean flag1) {
        Actor actor1 = Selector.look(flag, flag1, this.camera3D, -1, actor.getArmy(), actor, true);
        if (!this.hookViewEnemy.start(actor, actor1, flag1, false)) {
            if (this.bViewEnemy) this.setView(actor);
            return;
        } else {
            this.setViewSomebody(actor);
            return;
        }
    }

    public void setViewPadlock(boolean flag, boolean flag1) {
        if (this.isViewOutside()) return;
        if (!this.cockpitCur.existPadlock()) return;
        Aircraft aircraft = World.getPlayerAircraft();
        Actor actor;
        if (World.cur().diffCur.No_Icons) actor = Selector.look(true, flag1, this.camera3D, -1, -1, aircraft, false);
        else if (flag) actor = Selector.look(true, flag1, this.camera3D, -1, aircraft.getArmy(), aircraft, false);
        else actor = Selector.look(true, flag1, this.camera3D, aircraft.getArmy(), -1, aircraft, false);
        if (actor == null || actor == aircraft) return;
        if (!this.cockpitCur.startPadlock(actor)) return;
        else return;
    }

    public void setViewEndPadlock() {
        if (this.isViewOutside()) return;
        if (!this.cockpitCur.existPadlock()) return;
        else {
            this.cockpitCur.endPadlock();
            return;
        }
    }

    public void setViewNextPadlock(boolean flag) {
        if (this.isViewOutside()) return;
        if (!this.cockpitCur.existPadlock()) return;
        Actor actor = Selector.next(flag);
        if (actor == null) return;
        if (!this.cockpitCur.startPadlock(actor)) return;
        else return;
    }

    public void setViewPadlockForward(boolean flag) {
        if (this.isViewPadlock()) this.cockpitCur.setPadlockForward(flag);
    }

    public void setViewInside() {
        if (!this.isViewOutside() && !this.isViewPadlock()) return;
        if (!Actor.isValid(this.cockpitCur)) return;
        if (!this.cockpitCur.isEnableFocusing()) return;
        else {
            this.endView();
            this.endViewFly();
            this.endViewEnemy();
            this.endViewInside();
            this.cockpitCur.focusEnter();
            return;
        }
    }

    public void setViewFlow30(Actor actor) {
        this.setView(actor, true);
        this.hookView.set(actor, 30F, -30F);
        this.camera3D.pos.resetAsBase();
    }

    public void setViewFlow10(Actor actor, boolean flag) {
        this.hookView.setFollow(flag);
        this.setView(actor, true);
        this.hookView.set(actor, 10F, -10F);
        this.camera3D.pos.resetAsBase();
    }

    public void setView(Actor actor) {
        this.setView(actor, false);
    }

    public void setView(Actor actor, boolean flag) {
        if (this.viewActor() != actor || flag || this.bViewFly || this.bViewEnemy) {
            this.endViewFly();
            this.endViewEnemy();
            this.endViewInside();
            Selector.resetGame();
            this.hookView.use(true);
            this.camera3D.pos.setRel(new Point3d(), new Orient());
            this.camera3D.pos.changeBase(actor, this.hookView, false);
            this.camera3D.pos.resetAsBase();
        }
    }

    public int cockpitCurIndx() {
        if (this.cockpits == null || this.cockpitCur == null) return -1;
        for (int i = 0; i < this.cockpits.length; i++)
            if (this.cockpitCur == this.cockpits[i]) return i;

        return -1;
    }

    public void beginStep(int i) {
        if (!this.bUseStartLog) if (i >= 0) ConsoleGL0.exclusiveDrawStep(I18N.gui("main.loading") + " " + i + "%", i);
        else ConsoleGL0.exclusiveDrawStep(null, -1);
    }

    public boolean beginApp(String s, String s1, int i) {
        Config.cur.mainSection = s1;
        Engine.cur = new Engine();
        Config.typeProvider();
        GLContext glcontext = Config.cur.createGlContext(Config.cur.ini.get(Config.cur.mainSection, "title", "il2"));
        return this.beginApp(glcontext, i);
    }

    public boolean beginApp(GLContext glcontext, int i) {
        Config.typeGlStrings();
        Config.cur.typeContextSettings(glcontext);
        this.bDrawIfNotFocused = Config.cur.ini.get("window", "DrawIfNotFocused", this.bDrawIfNotFocused);
        this.bShowStartIntro = Config.cur.ini.get("game", "intro", this.bShowStartIntro);
        RTSConf.cur.start();
        PaintScheme.init();
        NetEnv.cur().connect = new Connect();
        NetEnv.cur();
        NetEnv.host().destroy();
        new NetUser("No Name");
        NetEnv.active(true);
        Config.cur.beginSound();
        RenderContext.activate(glcontext);
        RendersMain.setSaveAspect(true);
        RendersMain.setGlContext(glcontext);
        RendersMain.setTickPainting(true);
        TTFont.font[0] = TTFont.get("arialSmall");
        TTFont.font[1] = TTFont.get("arial10");
        TTFont.font[2] = TTFont.get("arialb12");
        TTFont.font[3] = TTFont.get("courSmall");
        ConsoleGL0.init("Console", i);
        this.bUseStartLog = Config.cur.ini.get("Console", "UseStartLog", this.bUseStartLog);
        if (this.bUseStartLog) ConsoleGL0.exclusiveDraw(true);
        else ConsoleGL0.exclusiveDraw("gui/background0.mat");
        this.beginStep(5);
        CmdEnv.top().exec("CmdLoad com.maddox.rts.cmd.CmdLoad");
        CmdEnv.top().exec("load com.maddox.rts.cmd.CmdFile PARAM CURENV on");
        CmdSFS.bMountError = false;
        CmdEnv.top().exec("file .rc");
        if (CmdSFS.bMountError) return false;
//        CmdEnv.top().exec("CmdLoad com.maddox.rts.cmd.CmdSFSAutoMount NAME sfsautomount HELP sfsautomount [MOUNT <folder name>] [UNMOUNT <folder name>]");
//        CmdEnv.top().exec("sfsautomount MOUNT SFS_UP");
        this.beginStep(10);
        try {
            Engine.setWorldAcoustics("Landscape");
        } catch (Exception exception) {
            System.out.println("World Acoustics NOT initialized: " + exception.getMessage());
            return false;
        }
        Engine.soundListener().initDraw();
        this.beginStep(15);
        Regiment.loadAll();
        this.preloadNetClasses();
        this.beginStep(20);
        this.preloadAirClasses();
        this.beginStep(25);
        this.preloadChiefClasses();
        this.beginStep(30);
        this.preloadStationaryClasses();
        this.preload();
        this.beginStep(35);
        this.camera3D = new NetCamera3D();
        this.camera3D.setName("camera");
        this.camera3D.set(FOVX, 1.2F, 48000F);
        this.render3D0 = new Render3D0(0, 1.0F);
        this.render3D0.setSaveAspect(Config.cur.windowSaveAspect);
        this.render3D0.setName("render3D0");
        this.render3D0.setCamera(this.camera3D);
        Engine.lightEnv().sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
        Vector3f vector3f = new Vector3f(0.0F, 1.0F, -1F);
        vector3f.normalize();
        Engine.lightEnv().sun().set(vector3f);
        this._camera3D[0] = this.camera3D;
        this._render3D0[0] = this.render3D0;
        for (int j = 1; j < 3; j++) {
            this._camera3D[j] = new Camera3D();
            this._camera3D[j].set(FOVX, 1.2F, 48000F);
            this._camera3D[j].pos.setBase(this.camera3D, null, false);
            this._render3D0[j] = new Render3D0(j, 1.0F - j * 0.001F);
            this._render3D0[j].setSaveAspect(true);
            this._render3D0[j].setCamera(this._camera3D[j]);
        }

        this._camera3D[1].pos.setRel(new Orient(-FOVX, 0.0F, 0.0F));
        this._camera3D[2].pos.setRel(new Orient(FOVX, 0.0F, 0.0F));
        this.render3D1 = new Render3D1(0, 0.9F);
        this.render3D1.setSaveAspect(Config.cur.windowSaveAspect);
        this.render3D1.setName("render3D1");
        this.render3D1.setCamera(this.camera3D);
        for (int k = 1; k < 3; k++) {
            this._render3D1[k] = new Render3D1(k, 0.9F - k * 0.001F);
            this._render3D1[k].setSaveAspect(true);
            this._render3D1[k].setCamera(this._camera3D[k]);
        }

        this.camera3DR = new Camera3DR();
        this.camera3DR.setName("cameraR");
        this.camera3DR.set(FOVX, 1.2F, 48000F);
        this.camera3DR.pos.setBase(this.camera3D, new HookReflection(), false);
        this.render3D0R = new Render3D0R(1.1F);
        this.render3D0R.setSaveAspect(Config.cur.windowSaveAspect);
        this.render3D0R.setName("render3D0R");
        this.render3D0R.setCamera(this.camera3DR);
        Engine.soundListener().pos.setBase(this.camera3D, null, false);
        TextScr.font();
        this.camera2D = new CameraOrtho2D();
        this.camera2D.setName("camera2D");
        this.render2D = new Render2D(0, 0.95F);
        this.render2D.setSaveAspect(Config.cur.windowSaveAspect);
        this.render2D.setName("render2D");
        this.render2D.setCamera(this.camera2D);
        this.camera2D.set(0.0F, this.render2D.getViewPortWidth(), 0.0F, this.render2D.getViewPortHeight());
        this.camera2D.set(0.0F, 1.0F);
        this.render2D.setShow(true);
        this._camera2D[0] = this.camera2D;
        this._render2D[0] = this.render2D;
        for (int l = 1; l < 3; l++) {
            this._camera2D[l] = new CameraOrtho2D();
            this._render2D[l] = new Render2D(l, 0.95F - l * 0.001F);
            this._render2D[l].setSaveAspect(true);
            this._render2D[l].setCamera(this._camera2D[l]);
            this._camera2D[l].set(0.0F, this._render2D[l].getViewPortWidth(), 0.0F, this._render2D[l].getViewPortHeight());
            this._camera2D[l].set(0.0F, 1.0F);
        }

        this.camera3DMirror = new com.maddox.il2.objects.air.Cockpit.Camera3DMirror();
        this.camera3DMirror.setName("cameraMirror");
        this.camera3DMirror.set(FOVX, 1.2F, 48000F);
        this.camera3DMirror.pos.setBase(this.camera3D, Cockpit.getHookCamera3DMirror(false), false);
        this.render3D0Mirror = new Render3D0Mirror(1.8F);
        this.render3D0Mirror.setName("render3D0Mirror");
        this.render3D0Mirror.setCamera(this.camera3DMirror);
        this.render3D1Mirror = new Render3D1Mirror(1.78F);
        this.render3D1Mirror.setName("render3D1Mirror");
        this.render3D1Mirror.setCamera(this.camera3DMirror);
        this.camera2DMirror = new CameraOrtho2D();
        this.camera2DMirror.setName("camera2DMirror");
        this.render2DMirror = new Render2DMirror(1.79F);
        this.render2DMirror.setName("render2DMirror");
        this.render2DMirror.setCamera(this.camera2DMirror);
        this.camera2DMirror.set(0.0F, this.render2DMirror.getViewPortWidth(), 0.0F, this.render2DMirror.getViewPortHeight());
        this.camera2DMirror.set(0.0F, 1.0F);
        this.cameraCockpit = new Camera3D();
        this.cameraCockpit.setName("cameraCockpit");
        this.cameraCockpit.set(FOVX, 0.05F, 12.5F);
        this.renderCockpit = new RenderCockpit(0, 0.5F);
        this.renderCockpit.setSaveAspect(Config.cur.windowSaveAspect);
        this.renderCockpit.setName("renderCockpit");
        this.renderCockpit.setCamera(this.cameraCockpit);
        this.renderCockpit.setShow(false);
        this._cameraCockpit[0] = this.cameraCockpit;
        this._renderCockpit[0] = this.renderCockpit;
        for (int i1 = 1; i1 < 3; i1++) {
            this._cameraCockpit[i1] = new Camera3D();
            this._cameraCockpit[i1].set(FOVX, 0.05F, 12.5F);
            this._cameraCockpit[i1].pos.setBase(this.cameraCockpit, null, false);
            this._renderCockpit[i1] = new RenderCockpit(i1, 0.5F - i1 * 0.001F);
            this._renderCockpit[i1].setSaveAspect(true);
            this._renderCockpit[i1].setCamera(this._cameraCockpit[i1]);
        }

        this._cameraCockpit[1].pos.setRel(new Orient(-FOVX, 0.0F, 0.0F));
        this._cameraCockpit[2].pos.setRel(new Orient(FOVX, 0.0F, 0.0F));
        this.cameraCockpitMirror = new com.maddox.il2.objects.air.Cockpit.Camera3DMirror();
        this.cameraCockpitMirror.pos.setBase(this.cameraCockpit, Cockpit.getHookCamera3DMirror(true), false);
        this.cameraCockpitMirror.setName("cameraCockpitMirror");
        this.cameraCockpitMirror.set(FOVX, 0.05F, 12.5F);
        this.renderCockpitMirror = new RenderCockpitMirror(1.77F);
        this.renderCockpitMirror.setName("renderCockpitMirror");
        this.renderCockpitMirror.setCamera(this.cameraCockpitMirror);
        this.cameraHUD = new CameraOrtho2D();
        this.cameraHUD.setName("cameraHUD");
        this.renderHUD = new RenderHUD(0.3F);
        this.renderHUD.setName("renderHUD");
        this.renderHUD.setCamera(this.cameraHUD);
        this.cameraHUD.set(0.0F, this.renderHUD.getViewPortWidth(), 0.0F, this.renderHUD.getViewPortHeight());
        this.cameraHUD.set(-1000F, 1000F);
        this.renderHUD.setShow(true);
        LightEnvXY lightenvxy = new LightEnvXY();
        this.renderHUD.setLightEnv(lightenvxy);
        lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
        vector3f = new Vector3f(0.0F, 1.0F, -1F);
        vector3f.normalize();
        lightenvxy.sun().set(vector3f);
        this.hud = new HUD();
        this.renderHUD.contextResized();
        this.drawFarActorsInit();
        this.cameraMap2D = new CameraOrtho2D();
        this.cameraMap2D.setName("cameraMap2D");
        this.renderMap2D = new RenderMap2D(0.2F);
        this.renderMap2D.setName("renderMap2D");
        this.renderMap2D.setCamera(this.cameraMap2D);
        this.cameraMap2D.set(0.0F, this.renderMap2D.getViewPortWidth(), 0.0F, this.renderMap2D.getViewPortHeight());
        this.renderMap2D.setShow(false);
        this.beginStep(40);
        this._sunFlareRender[0] = SunFlare.newRender(0, 0.19F, this._camera3D[0]);
        this._sunFlareRender[0].setName("renderSunFlare");
        this._sunFlareRender[0].setSaveAspect(Config.cur.windowSaveAspect);
        this._sunFlareRender[0].setShow(false);
        for (int j1 = 1; j1 < 3; j1++) {
            this._sunFlareRender[j1] = SunFlare.newRender(j1, 0.19F, this._camera3D[j1]);
            this._sunFlareRender[j1].setSaveAspect(true);
            this._sunFlareRender[j1].setShow(false);
        }

        this.lightsGlare = new LightsGlare(0, 0.17F);
        this.lightsGlare.setSaveAspect(Config.cur.windowSaveAspect);
        this.lightsGlare.setCamera(new CameraOrtho2D());
        this.lightsGlare.setShow(false);
        this._lightsGlare[0] = this.lightsGlare;
        for (int k1 = 1; k1 < 3; k1++) {
            this._lightsGlare[k1] = new LightsGlare(k1, 0.17F - k1 * 0.001F);
            this._lightsGlare[k1].setSaveAspect(true);
            this._lightsGlare[k1].setCamera(new CameraOrtho2D());
        }

        this.sunGlare = new SunGlare(0, 0.15F);
        this.sunGlare.setSaveAspect(Config.cur.windowSaveAspect);
        this.sunGlare.setCamera(new CameraOrtho2D());
        this.sunGlare.setShow(false);
        this._sunGlare[0] = this.sunGlare;
        for (int l1 = 1; l1 < 3; l1++) {
            this._sunGlare[l1] = new SunGlare(l1, 0.15F - l1 * 0.001F);
            this._sunGlare[l1].setSaveAspect(true);
            this._sunGlare[l1].setCamera(new CameraOrtho2D());
        }

        this.overLoad = new OverLoad(0, 0.1F);
        this.overLoad.setSaveAspect(Config.cur.windowSaveAspect);
        this.overLoad.setCamera(new CameraOrtho2D());
        this.overLoad.setShow(false);
        this._overLoad[0] = this.overLoad;
        for (int i2 = 1; i2 < 3; i2++) {
            this._overLoad[i2] = new OverLoad(i2, 0.1F - i2 * 0.001F);
            this._overLoad[i2].setSaveAspect(true);
            this._overLoad[i2].setCamera(new CameraOrtho2D());
        }

        this.darkerNight = new DarkerNight(0, 0.7F);
        this.darkerNight.setSaveAspect(Config.cur.windowSaveAspect);
        this.darkerNight.setCamera(new CameraOrtho2D());
        this.darkerNight.setShow(true);
        this._cinema[0] = new Cinema(0, 0.09F);
        this._cinema[0].setSaveAspect(Config.cur.windowSaveAspect);
        this._cinema[0].setCamera(new CameraOrtho2D());
        this._cinema[0].setShow(false);
        for (int j2 = 1; j2 < 3; j2++) {
            this._cinema[j2] = new Cinema(j2, 0.09F - j2 * 0.001F);
            this._cinema[j2].setSaveAspect(true);
            this._cinema[j2].setCamera(new CameraOrtho2D());
            this._cinema[j2].setShow(false);
        }

        this.timeSkip = new TimeSkip(-1.1F);
        HotKeyEnv.fromIni("hotkeys", Config.cur.ini, Config.cur.ini.get(Config.cur.mainSection, "hotkeys", "hotkeys"));
        FreeFly.init("FreeFly");
        FreeFlyXYZ.init("FreeFlyXYZ");
        this.hookView = new HookView("HookView");
        this.hookView.setCamera(this.camera3D);
        this.hookViewFly = new HookViewFly("HookViewFly");
        this.hookViewEnemy = new HookViewEnemy();
        this.hookViewEnemy.setCamera(this.camera3D);
        HookPilot.New();
        HookPilot.current.setTarget(this.cameraCockpit);
        HookPilot.current.setTarget2(this.camera3D);
        HookKeys.New();
        this.aircraftHotKeys = new AircraftHotKeys();
        this.beginStep(45);
        HotKeyCmdEnv.enable("default", false);
        HotKeyCmdEnv.enable("Console", false);
        HotKeyCmdEnv.enable("hotkeys", false);
        HotKeyCmdEnv.enable("HookView", false);
        HotKeyCmdEnv.enable("PanView", false);
        HotKeyCmdEnv.enable("SnapView", false);
        HotKeyCmdEnv.enable("pilot", false);
        HotKeyCmdEnv.enable("move", false);
        HotKeyCmdEnv.enable("gunner", false);
        HotKeyEnv.enable("pilot", false);
        HotKeyEnv.enable("move", false);
        HotKeyEnv.enable("gunner", false);
        HotKeyCmdEnv.enable("misc", false);
        HotKeyCmdEnv.enable("$$$misc", true);
        HotKeyEnv.enable("$$$misc", true);
        HotKeyCmdEnv.enable("orders", false);
        HotKeyCmdEnv.enable("aircraftView", false);
        HotKeyCmdEnv.enable("timeCompression", false);
        HotKeyCmdEnv.enable("gui", false);
        HotKeyCmdEnv.enable("builder", false);
        HotKeyCmdEnv.enable("MouseXYZ", false);
        HotKeyCmdEnv.enable("FreeFly", false);
        HotKeyCmdEnv.enable("FreeFlyXYZ", false);
        World.cur().userCfg = UserCfg.loadCurrent();
        World.cur().setUserCovers();
        this.ordersTree = new OrdersTree(true);
        this.beginStep(50);
        if (this.bUseGUI) {
            this.guiManager = GUI.create("gui");
            this.keyRecord = new KeyRecord();
            this.keyRecord.addExcludePrevCmd(278);
            Keyboard.adapter().setKeyEnable(27);
        }
        this.beginStep(90);
        this.initHotKeys();
        Voice.setEnableVoices(!Config.cur.ini.get("game", "NoChatter", false));
        this.beginStep(95);
        this.viewSet_Load();
        DeviceLink.start();
        this.onBeginApp();
        Time.setPause(false);
        RTSConf.cur.loopMsgs();
        Time.setPause(true);
        new MsgAction(64, 1.0D + Math.random() * 10D) {

            public void doAction() {
                try {
                    Class.forName("fbapi");
                    Main.doGameExit();
                } catch (Throwable throwable) {}
            }

        };
        this.bDrawClouds = true;
        TextScr.setColor(new Color4f(1.0F, 0.0F, 0.0F, 1.0F));
        RTSConf.cur.console.getEnv().exec("file rcu");
        this.beginStep(-1);
        this.createConsoleServer();
        return true;
    }

    public void setSaveAspect(boolean flag) {
        if (Config.cur.windowSaveAspect == flag) return;
        else {
            this.render3D0.setSaveAspect(flag);
            this.render3D1.setSaveAspect(flag);
            this.render2D.setSaveAspect(flag);
            this.renderCockpit.setSaveAspect(flag);
            this._sunFlareRender[0].setSaveAspect(flag);
            this.lightsGlare.setSaveAspect(flag);
            this.sunGlare.setSaveAspect(flag);
            this.overLoad.setSaveAspect(flag);
            this.darkerNight.setSaveAspect(flag);
            this._cinema[0].setSaveAspect(flag);
            Config.cur.windowSaveAspect = flag;
            return;
        }
    }

    public static void menuMusicPlay() {
        menuMusicPlay(_sLastMusic);
    }

    public static void menuMusicPlay(String s) {
        s = Regiment.getCountryFromBranch(s);
        _sLastMusic = s;
        CmdEnv.top().exec("music FILE music/menu/" + _sLastMusic);
    }

    public void viewSet_Load() {
        int i = Config.cur.ini.get("game", "viewSet", 0);
        this.viewSet_Set(i);
        this.iconTypes = Config.cur.ini.get("game", "iconTypes", 3, 0, 3);
    }

    public void viewSet_Save() {
        if (this.aircraftHotKeys != null) {
            Config.cur.ini.set("game", "viewSet", this.viewSet_Get());
            Config.cur.ini.set("game", "iconTypes", this.iconTypes());
        }
    }

    protected int viewSet_Get() {
        int i = 0;
        if (HookKeys.current != null && HookKeys.current.isPanView()) i |= 1;
        if (HookPilot.current != null && HookPilot.current.isAim()) i |= 2;
        i |= (this.viewMirror & 3) << 2;
        if (!this.aircraftHotKeys.isAutoAutopilot()) i |= 0x10;
        i |= (HUD.drawSpeed() & 3) << 5;
        return i;
    }

    private void viewSet_Set(int i) {
        HookKeys.current.setMode((i & 1) != 0);
        HookPilot.current.doAim((i & 2) != 0);
        this.viewMirror = i >> 2 & 3;
        this.aircraftHotKeys.setAutoAutopilot((i & 0x10) == 0);
        HUD.setDrawSpeed(i >> 5 & 3);
    }

    public boolean isViewMirror() {
        return this.viewMirror > 0;
    }

    public int iconTypes() {
        return this.iconTypes;
    }

    protected void changeIconTypes() {
        this.iconTypes = (this.iconTypes + 1) % 4;
    }

    public void disableAllHotKeyCmdEnv() {
        List list = HotKeyCmdEnv.allEnv();
        int i = list.size();
        for (int j = 0; j < i; j++) {
            HotKeyCmdEnv hotkeycmdenv = (HotKeyCmdEnv) list.get(j);
            hotkeycmdenv.enable(false);
        }

        HotKeyCmdEnv.enable("hotkeys", true);
        HotKeyCmdEnv.enable("$$$misc", true);
    }

    public void enableHotKeyCmdEnvs(String as[]) {
        for (int i = 0; i < as.length; i++)
            HotKeyCmdEnv.enable(as[i], true);

    }

    public void enableOnlyHotKeyCmdEnvs(String as[]) {
        this.disableAllHotKeyCmdEnv();
        this.enableHotKeyCmdEnvs(as);
    }

    public void enableOnlyHotKeyCmdEnv(String s) {
        this.disableAllHotKeyCmdEnv();
        HotKeyCmdEnv.enable(s, true);
    }

    public void enableGameHotKeyCmdEnvs() {
        this.enableHotKeyCmdEnvs(gameHotKeyCmdEnvs);
    }

    public void enableOnlyGameHotKeyCmdEnvs() {
        this.enableOnlyHotKeyCmdEnvs(gameHotKeyCmdEnvs);
    }

    public void enableCockpitHotKeys() {
        if (this.isDemoPlaying()) return;
        if (!Actor.isValid(this.cockpitCur)) return;
        String as[] = this.cockpitCur.getHotKeyEnvs();
        if (as == null) return;
        for (int i = 0; i < as.length; i++)
            if (as[i] != null) HotKeyEnv.enable(as[i], true);

    }

    public void disableCockpitHotKeys() {
        if (this.isDemoPlaying()) return;
        if (!Actor.isValid(this.cockpitCur)) return;
        String as[] = this.cockpitCur.getHotKeyEnvs();
        if (as == null) return;
        for (int i = 0; i < as.length; i++)
            if (as[i] != null) HotKeyEnv.enable(as[i], false);

    }

    private void _disableCockpitsHotKeys() {
        HotKeyEnv.enable("pilot", false);
        HotKeyEnv.enable("move", false);
        HotKeyEnv.enable("gunner", false);
    }

    public void disableCockpitsHotKeys() {
        if (this.isDemoPlaying()) return;
        else {
            this._disableCockpitsHotKeys();
            return;
        }
    }

    public void resetGameClear() {
        SearchlightGeneric.resetGame();
        this.disableCockpitsHotKeys();
        this.camera3D.pos.changeBase(null, null, false);
        this.camera3D.pos.setAbs(new Point3d(), new Orient());
        FreeFly.adapter().resetGame();
        if (HookPilot.current != null) {
            HookPilot.current.use(false);
            HookPilot.current.resetGame();
        }
        HookGunner.resetGame();
        if (HookKeys.current != null) HookKeys.current.resetGame();
        this.hookViewFly.reset();
        this.hookViewEnemy.reset();
        this.hookView.reset();
        this.hookView.resetGame();
        this.hookViewEnemy.resetGame();
        this.overLoad.setShow(false);
        for (int i = 0; i < 3; i++) {
            this._lightsGlare[i].setShow(false);
            this._lightsGlare[i].resetGame();
            this._sunGlare[i].setShow(false);
            this._sunGlare[i].resetGame();
        }

        Selector.resetGame();
        this.hud.resetGame();
        this.aircraftHotKeys.resetGame();
        this.bViewFly = false;
        this.bViewEnemy = false;
        this.ordersTree.resetGameClear();
        if (this.clouds != null) {
            this.clouds.destroy();
            this.clouds = null;
        }
        if (this.zip != null) {
            this.zip.destroy();
            this.zip = null;
        }
        this.sunFlareDestroy();
        if (Actor.isValid(this.spritesFog)) this.spritesFog.destroy();
        this.spritesFog = null;
        if (this.land2D != null) {
            if (!this.land2D.isDestroyed()) this.land2D.destroy();
            this.land2D = null;
        }
        if (this.land2DText != null) {
            if (!this.land2DText.isDestroyed()) this.land2DText.destroy();
            this.land2DText = null;
        }
        if (this.cockpits != null) {
            for (int j = 0; j < this.cockpits.length; j++) {
                if (Actor.isValid(this.cockpits[j])) this.cockpits[j].destroy();
                this.cockpits[j] = null;
            }

            this.cockpits = null;
        }
        this.cockpitCur = null;
        super.resetGameClear();
    }

    public void resetGameCreate() {
        super.resetGameCreate();
        Engine.soundListener().pos.setBase(this.camera3D, null, false);
        Engine.soundListener().setUseBaseSpeed(true);
    }

    public void resetUserClear() {
        World.cur().resetUser();
        this.aircraftHotKeys.resetUser();
        if (this.cockpits != null) {
            for (int i = 0; i < this.cockpits.length; i++) {
                if (Actor.isValid(this.cockpits[i])) this.cockpits[i].destroy();
                this.cockpits[i] = null;
            }

            this.cockpits = null;
        }
        this.cockpitCur = null;
        super.resetUserClear();
    }

    public void sunFlareCreate() {
        this.sunFlareDestroy();
        for (int i = 0; i < 3; i++)
            this._sunFlare[i] = new SunFlare(this._sunFlareRender[i]);

    }

    public void sunFlareDestroy() {
        for (int i = 0; i < 3; i++) {
            if (Actor.isValid(this._sunFlare[i])) this._sunFlare[i].destroy();
            this._sunFlare[i] = null;
        }

    }

    public void sunFlareShow(boolean flag) {
        for (int i = 0; i < 3; i++)
            this._sunFlareRender[i].setShow(flag);

    }

    public KeyRecordCallback playRecordedMissionCallback() {
        return this.playRecordedMissionCallback;
    }

    public InOutStreams playRecordedStreams() {
        return this.playRecordedStreams;
    }

    NetChannelInStream playRecordedNetChannelIn() {
        return this.playRecordedNetChannelIn;
    }

    public GameTrack gameTrackRecord() {
        return this.gameTrackRecord;
    }

    public void setGameTrackRecord(GameTrack gametrack) {
        this.gameTrackRecord = gametrack;
    }

    public GameTrack gameTrackPlay() {
        return this.gameTrackPlay;
    }

    public void setGameTrackPlay(GameTrack gametrack) {
        this.gameTrackPlay = gametrack;
    }

    public void clearGameTrack(GameTrack gametrack) {
        if (gametrack == this.gameTrackRecord) this.gameTrackRecord = null;
        if (gametrack == this.gameTrackPlay) this.gameTrackPlay = null;
    }

    public String playRecordedMission(String s) {
        this.playBatchCurRecord = -1;
        this.playEndBatch = true;
        this.playRecordedStreams = null;
        return this.playRecordedMission(s, true);
    }

    public String playRecordedMission(String s, boolean flag) {
        this.playRecordedFile = s;
        if (this.playRecordedMissionCallback == null) this.playRecordedMissionCallback = new KeyRecordCallback() {

            public void playRecordedEnded() {
                if (this != Main3D.this.playRecordedMissionCallback) return;
                GameState gamestate = Main.state();
                if (gamestate instanceof GUIRecordPlay) {
                    GUIRecordPlay guirecordplay = (GUIRecordPlay) gamestate;
                    guirecordplay.doReplayMission(Main3D.this.playRecordedFile, Main3D.this.playEndBatch);
                } else if (gamestate instanceof GUITrainingPlay) {
                    GUITrainingPlay guitrainingplay = (GUITrainingPlay) gamestate;
                    guitrainingplay.doQuitMission();
                    guitrainingplay.doExit();
                } else if (gamestate instanceof GUIBWDemoPlay) {
                    GUIBWDemoPlay guibwdemoplay = (GUIBWDemoPlay) gamestate;
                    guibwdemoplay.doQuitMission();
                }
            }

            public void doFirstHotCmd(boolean flag1) {
                if (Main3D.this.playRecordedStreams != null) {
                    AircraftHotKeys.bFirstHotCmd = flag1;
                    Main3D.this.loadRecordedStates1(flag1);
                    if (!flag1) Main3D.this.loadRecordedStates2();
                }
            }

        };
        if (this.playRecordedStreams != null) {
            try {
                this.playRecordedStreams.close();
            } catch (Exception exception) {}
            this.playRecordedStreams = null;
            NetMissionTrack.stopPlaying();
        }
        if (this.playRecordedNetChannelIn != null) this.playRecordedNetChannelIn.destroy();
        this.playRecordedNetChannelIn = null;
        if (InOutStreams.isExistAndValid(new File(s))) return this.playNetRecordedMission(s, flag);
        String s1 = s;
        SectFile sectfile = new SectFile(s, 0, false);
        int i = sectfile.sectionIndex("batch");
        if (i >= 0) {
            int j = sectfile.vars(i);
            if (j <= 0) return "Track file '" + s + "' is empty";
            this.playEndBatch = this.playBatchCurRecord != -1 && this.playBatchCurRecord == j - 2;
            if (j == 1) this.playEndBatch = true;
            this.playBatchCurRecord++;
            if (this.playBatchCurRecord >= j) this.playBatchCurRecord = 0;
            s1 = "Records/" + sectfile.line(i, this.playBatchCurRecord);
            if (InOutStreams.isExistAndValid(new File(s1))) return this.playNetRecordedMission(s1, flag);
            sectfile = new SectFile(s1, 0, false);
        } else this.playEndBatch = true;
        i = sectfile.sectionIndex("$$$record");
        if (i < 0) return "Track file '" + s1 + "' not included section [$$$record]";
        if (sectfile.vars(i) <= 10) return "Track file '" + s1 + "' is empty";
        int k = Integer.parseInt(sectfile.var(i, 0));
        if (k != 130) return "Track file '" + s1 + "' version is not supported";
        int l = Integer.parseInt(sectfile.var(i, 1));
        float f = Float.parseFloat(sectfile.var(i, 2));
        float f1 = Float.parseFloat(sectfile.var(i, 3));
        float f2 = Float.parseFloat(sectfile.var(i, 4));
        float f3 = Float.parseFloat(sectfile.var(i, 5));
        float f4 = Float.parseFloat(sectfile.var(i, 6));
        int i1 = Integer.parseInt(sectfile.var(i, 7));
        int j1 = Integer.parseInt(sectfile.var(i, 8));
        long l1 = Long.parseLong(sectfile.var(i, 9));
        long l2 = sectfile.fingerExcludeSectPrefix("$$$");
        l2 = Finger.incLong(l2, l);
        l2 = Finger.incLong(l2, f);
        l2 = Finger.incLong(l2, f1);
        l2 = Finger.incLong(l2, f2);
        l2 = Finger.incLong(l2, f3);
        l2 = Finger.incLong(l2, f4);
        l2 = Finger.incLong(l2, i1);
        l2 = Finger.incLong(l2, j1);
        if (l1 != l2) return "Track file '" + s1 + "' is changed";
        World.cur().diffCur.set(l);
        World.cur().diffCur.Cockpit_Always_On = false;
        World.cur().diffCur.No_Outside_Views = false;
        World.cur().diffCur.No_Padlock = false;
        World.cur().userCoverMashineGun = f;
        World.cur().userCoverCannon = f1;
        World.cur().userCoverRocket = f2;
        World.cur().userRocketDelay = f3;
        World.cur().userBombDelay = f4;
        this.viewSet_Set(i1);
        this.iconTypes = j1;
        if (Main.cur().netServerParams == null) {
            new NetServerParams();
            Main.cur().netServerParams.setMode(2);
            new NetLocalControl();
        }
        try {
            Mission.loadFromSect(sectfile, true);
        } catch (Exception exception1) {
            System.out.println(exception1.getMessage());
            exception1.printStackTrace();
            return "Track file '" + s1 + "' load failed: " + exception1.getMessage();
        }
        this.playRecordedSect = sectfile;
        this.playRecorderIndx = i;
        this.playRecordedPlayFile = s1;
        if (flag) this.doRecordedPlayFirst();
        return null;
    }

    private String playNetRecordedMission(String s, boolean flag) {
        try {
            this.playRecordedStreams = new InOutStreams();
            this.playRecordedStreams.open(new File(s), false);
            InputStream inputstream = this.playRecordedStreams.openStream("version");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            int i = Integer.parseInt(bufferedreader.readLine());
            int j = i;
            if (i >= 103) j = Integer.parseInt(bufferedreader.readLine());
            bufferedreader.close();
            if (i != 100 && i != 101 && i != 102 && i != 103) {
                try {
                    this.playRecordedStreams.close();
                } catch (Exception localException3) {}
                this.playRecordedStreams = null;
                return "Track file '" + s + "' version is not supported";
            }
            this.loadRecordedStates0();
            InputStream inputstream1 = this.playRecordedStreams.openStream("traffic");
            if (inputstream1 == null) throw new Exception("Stream 'traffic' not found.");
            this.playRecordedNetChannelIn = new NetChannelInStream(inputstream1, 1);
            RTSConf.cur.netEnv.addChannel(this.playRecordedNetChannelIn);
            this.playRecordedNetChannelIn.setStateInit(0);
            this.playRecordedNetChannelIn.userState = 1;
            NetMissionTrack.startPlaying(this.playRecordedStreams, i, j);
            if (flag) this.doRecordedPlayFirst();
        } catch (Exception exception) {
            exception.printStackTrace();
            if (this.playRecordedStreams != null) try {
                this.playRecordedStreams.close();
            } catch (Exception exception1) {}
            this.playRecordedStreams = null;
            return "Track file '" + s + "' load failed: " + exception.getMessage();
        }
        return null;
    }

    private void doRecordedPlayFirst() {
        this._disableCockpitsHotKeys();
        HotKeyEnv.enable("misc", false);
        HotKeyEnv.enable("orders", false);
        HotKeyEnv.enable("timeCompression", false);
        HotKeyEnv.enable("aircraftView", false);
        HotKeyEnv.enable("HookView", false);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
    }

    public String startPlayRecordedMission() {
        if (this.playRecordedStreams != null) this.keyRecord.startPlay(this.playRecordedMissionCallback);
        else if (!this.keyRecord.startPlay(this.playRecordedSect, this.playRecorderIndx, 10, this.playRecordedMissionCallback)) return "Track file '" + this.playRecordedPlayFile + "' load failed";
        return null;
    }

    public void stopPlayRecordedMission() {
        this.playRecordedSect = null;
        if (this.keyRecord.isPlaying()) this.keyRecord.stopPlay();
        if (this.playRecordedStreams != null) {
            try {
                this.playRecordedStreams.close();
            } catch (Exception exception) {}
            this.playRecordedStreams = null;
            NetMissionTrack.stopPlaying();
        }
        if (this.playRecordedNetChannelIn != null) this.playRecordedNetChannelIn.destroy();
        this.playRecordedNetChannelIn = null;
        this._disableCockpitsHotKeys();
        HotKeyEnv.enable("misc", true);
        HotKeyEnv.enable("orders", true);
        HotKeyEnv.enable("timeCompression", true);
        HotKeyEnv.enable("aircraftView", true);
        HotKeyEnv.enable("HookView", true);
        HotKeyEnv.enable("PanView", true);
        HotKeyEnv.enable("SnapView", true);
    }

    public void flyRecordedMission() {
        if (this.keyRecord.isPlaying()) {
            this.keyRecord.stopPlay();
            this.playRecordedMissionCallback = null;
            if (Actor.isValid(this.cockpitCur)) HotKeyCmd.exec("misc", "cockpitEnter" + this.cockpitCurIndx());
            this.enableCockpitHotKeys();
            HotKeyEnv.enable("misc", true);
            HotKeyEnv.enable("orders", true);
            HotKeyEnv.enable("timeCompression", true);
            HotKeyEnv.enable("aircraftView", true);
            HotKeyEnv.enable("HookView", true);
            HotKeyEnv.enable("PanView", true);
            HotKeyEnv.enable("SnapView", true);
            ForceFeedback.startMission();
        }
    }

    public boolean saveRecordedMission(String s) {
        if (this.mission == null) return false;
        if (this.mission.isDestroyed()) return false;
        if (!this.keyRecord.isContainRecorded()) return false;
        try {
            SectFile sectfile = this.mission.sectFile();
            int i = sectfile.sectionIndex("$$$record");
            if (i >= 0) sectfile.sectionClear(i);
            else i = sectfile.sectionAdd("$$$record");
            sectfile.lineAdd(i, "130", "");
            long l = Finger.incLong(this.mission.finger(), 130);
            int j = World.cur().diffCur.get();
            sectfile.lineAdd(i, "" + j, "");
            l = Finger.incLong(this.mission.finger(), j);
            sectfile.lineAdd(i, "" + World.cur().userCoverMashineGun, "");
            l = Finger.incLong(l, World.cur().userCoverMashineGun);
            sectfile.lineAdd(i, "" + World.cur().userCoverCannon, "");
            l = Finger.incLong(l, World.cur().userCoverCannon);
            sectfile.lineAdd(i, "" + World.cur().userCoverRocket, "");
            l = Finger.incLong(l, World.cur().userCoverRocket);
            sectfile.lineAdd(i, "" + World.cur().userRocketDelay, "");
            l = Finger.incLong(l, World.cur().userRocketDelay);
            sectfile.lineAdd(i, "" + World.cur().userBombDelay, "");
            l = Finger.incLong(l, World.cur().userBombDelay);
            sectfile.lineAdd(i, "" + Mission.viewSet, "");
            l = Finger.incLong(l, Mission.viewSet);
            sectfile.lineAdd(i, "" + Mission.iconTypes, "");
            l = Finger.incLong(l, Mission.iconTypes);
            sectfile.lineAdd(i, "" + l, "");
            this.keyRecord.saveRecorded(sectfile, i);
            return sectfile.saveFile(s);
        } catch (Exception exception) {}
        return false;
    }

    public boolean saveRecordedStates0(InOutStreams inoutstreams) {
        try {
            PrintWriter printwriter = new PrintWriter(inoutstreams.createStream("states0"));
            printwriter.println(World.cur().diffCur.get());
            printwriter.println(World.cur().userCoverMashineGun);
            printwriter.println(World.cur().userCoverCannon);
            printwriter.println(World.cur().userCoverRocket);
            printwriter.println(World.cur().userRocketDelay);
            printwriter.println(World.cur().userBombDelay);
            printwriter.println(this.viewSet_Get());
            printwriter.println(this.iconTypes);
            printwriter.println(this.isViewOutside() ? "0" : "1");
            printwriter.println(FOVX);
            printwriter.flush();
            printwriter.close();
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return false;
    }

    public boolean saveRecordedStates1(InOutStreams inoutstreams) {
        try {
            PrintWriter printwriter = new PrintWriter(inoutstreams.createStream("states1"));
            HookView.cur().saveRecordedStates(printwriter);
            HookPilot.cur().saveRecordedStates(printwriter);
            HookGunner.saveRecordedStates(printwriter);
            printwriter.flush();
            printwriter.close();
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return false;
    }

    public boolean saveRecordedStates2(InOutStreams inoutstreams) {
        try {
            PrintWriter printwriter = new PrintWriter(inoutstreams.createStream("states2"));
            printwriter.println(FOVX);
            int i = 0;
            if (this.hud.bDrawDashBoard) i |= 1;
            if (this.isViewInsideShow()) i |= 2;
            if (Actor.isValid(this.cockpitCur) && this.cockpitCur.isToggleAim()) i |= 4;
            FlightModel flightmodel = World.getPlayerFM();
            if (flightmodel != null && flightmodel.AS.bShowSmokesOn) i |= 8;
            if (this.isEnableRenderingCockpit()) i |= 0x10;
            if (Actor.isValid(this.cockpitCur) && this.cockpitCur.isToggleUp()) i |= 0x20;
            if (Actor.isValid(this.cockpitCur) && this.cockpitCur.isToggleDim()) i |= 0x40;
            if (Actor.isValid(this.cockpitCur) && this.cockpitCur.isToggleLight()) i |= 0x80;
            if (Actor.isValid(this.cockpitCur) && !this.cockpitCur.isEnableRenderingBall()) i |= 0x100;
            printwriter.println(i);
            printwriter.flush();
            printwriter.close();
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return false;
    }

    public void loadRecordedStates0() {
        try {
            InputStream inputstream = this.playRecordedStreams.openStream("states0");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            World.cur().diffCur.set(Integer.parseInt(bufferedreader.readLine()));
            World.cur().diffCur.Cockpit_Always_On = false;
            World.cur().diffCur.No_Outside_Views = false;
            World.cur().diffCur.No_Padlock = false;
            World.cur().userCoverMashineGun = Float.parseFloat(bufferedreader.readLine());
            World.cur().userCoverCannon = Float.parseFloat(bufferedreader.readLine());
            World.cur().userCoverRocket = Float.parseFloat(bufferedreader.readLine());
            World.cur().userRocketDelay = Float.parseFloat(bufferedreader.readLine());
            World.cur().userBombDelay = Float.parseFloat(bufferedreader.readLine());
            this.viewSet_Set(Integer.parseInt(bufferedreader.readLine()));
            this.iconTypes = Integer.parseInt(bufferedreader.readLine());
            this.bLoadRecordedStates1Before = Integer.parseInt(bufferedreader.readLine()) == 1;
            float f = Float.parseFloat(bufferedreader.readLine());
            if (f != FOVX) CmdEnv.top().exec("fov " + f);
            inputstream.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void loadRecordedStates1(boolean flag) {
        if (flag == this.bLoadRecordedStates1Before) try {
            InputStream inputstream = this.playRecordedStreams.openStream("states1");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            HookView.cur().loadRecordedStates(bufferedreader);
            HookPilot.cur().loadRecordedStates(bufferedreader);
            HookGunner.loadRecordedStates(bufferedreader);
            inputstream.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void loadRecordedStates2() {
        try {
            InputStream inputstream = this.playRecordedStreams.openStream("states2");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            float f = Float.parseFloat(bufferedreader.readLine());
            if (f != FOVX) CmdEnv.top().exec("fov " + f);
            int i = Integer.parseInt(bufferedreader.readLine());
            this.hud.bDrawDashBoard = (i & 1) != 0;
            this.setViewInsideShow((i & 2) != 0);
            if (Actor.isValid(this.cockpitCur)) this.cockpitCur.doToggleAim((i & 4) != 0);
            FlightModel flightmodel = World.getPlayerFM();
            if (flightmodel != null) flightmodel.AS.setAirShowState((i & 8) != 0);
            if (Actor.isValid(this.cockpitCur)) {
                this.setEnableRenderingCockpit((i & 0x10) != 0);
                this.cockpitCur.doToggleUp((i & 0x20) != 0);
                if ((i & 0x40) != 0 && !this.cockpitCur.isToggleDim()) this.cockpitCur.doToggleDim();
                if ((i & 0x80) != 0 && !this.cockpitCur.isToggleLight()) this.cockpitCur.doToggleLight();
                this.cockpitCur.setEnableRenderingBall((i & 0x100) == 0);
            }
            inputstream.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void setRenderIndx(int i) {
        this.iRenderIndx = i;
    }

    public int getRenderIndx() {
        return this.iRenderIndx;
    }

    private void transform_point(double ad[], double ad1[], double ad2[]) {
        ad[0] = ad1[0] * ad2[0] + ad1[4] * ad2[1] + ad1[8] * ad2[2] + ad1[12] * ad2[3];
        ad[1] = ad1[1] * ad2[0] + ad1[5] * ad2[1] + ad1[9] * ad2[2] + ad1[13] * ad2[3];
        ad[2] = ad1[2] * ad2[0] + ad1[6] * ad2[1] + ad1[10] * ad2[2] + ad1[14] * ad2[3];
        ad[3] = ad1[3] * ad2[0] + ad1[7] * ad2[1] + ad1[11] * ad2[2] + ad1[15] * ad2[3];
    }

    public boolean project2d(double d, double d1, double d2, Point3d point3d) {
        this._dIn[0] = d;
        this._dIn[1] = d1;
        this._dIn[2] = d2;
        this._dIn[3] = 1.0D;
        if (this.bRenderMirror) {
            this.transform_point(this._dOut, this._modelMatrix3DMirror, this._dIn);
            this.transform_point(this._dIn, this._projMatrix3DMirror, this._dOut);
        } else {
            this.transform_point(this._dOut, this._modelMatrix3D[this.iRenderIndx], this._dIn);
            this.transform_point(this._dIn, this._projMatrix3D[this.iRenderIndx], this._dOut);
        }
        if (this._dIn[3] == 0.0D) {
            System.out.println("BAD glu.Project: " + d + " " + d1 + " " + d2);
            return false;
        }
        this._dIn[0] /= this._dIn[3];
        this._dIn[1] /= this._dIn[3];
        this._dIn[2] /= this._dIn[3];
        if (this.bRenderMirror) {
            point3d.x = this._viewportMirror[0] + (1.0D + this._dIn[0]) * this._viewportMirror[2] / 2D;
            point3d.y = this._viewportMirror[1] + (1.0D + this._dIn[1]) * this._viewportMirror[3] / 2D;
        } else {
            point3d.x = this._viewport[this.iRenderIndx][0] + (1.0D + this._dIn[0]) * this._viewport[this.iRenderIndx][2] / 2D;
            point3d.y = this._viewport[this.iRenderIndx][1] + (1.0D + this._dIn[1]) * this._viewport[this.iRenderIndx][3] / 2D;
        }
        point3d.z = (1.0D + this._dIn[2]) / 2D;
        return true;
    }

    public boolean project2d_cam(double d, double d1, double d2, Point3d point3d) {
        if (!this.project2d(d, d1, d2, point3d)) return false;
        if (this.bRenderMirror) {
            point3d.x -= this._viewportMirror[0];
            point3d.y -= this._viewportMirror[1];
        } else {
            point3d.x -= this._viewport[this.iRenderIndx][0];
            point3d.y -= this._viewport[this.iRenderIndx][1];
        }
        return true;
    }

    public boolean project2d_norm(double d, double d1, double d2, Point3d point3d) {
        this._dIn[0] = d;
        this._dIn[1] = d1;
        this._dIn[2] = d2;
        this._dIn[3] = 1.0D;
        if (this.bRenderMirror) {
            this.transform_point(this._dOut, this._modelMatrix3DMirror, this._dIn);
            this.transform_point(this._dIn, this._projMatrix3DMirror, this._dOut);
        } else {
            this.transform_point(this._dOut, this._modelMatrix3D[this.iRenderIndx], this._dIn);
            this.transform_point(this._dIn, this._projMatrix3D[this.iRenderIndx], this._dOut);
        }
        if (this._dIn[3] == 0.0D) {
            System.out.println("BAD glu.Project2: " + d + " " + d1 + " " + d2);
            return false;
        } else {
            double d3 = 1.0D / this._dIn[3];
            point3d.x = this._dIn[0] * d3;
            point3d.y = this._dIn[1] * d3;
            point3d.z = this._dIn[2] * d3;
            return true;
        }
    }

    public boolean project2d(Point3d point3d, Point3d point3d1) {
        return this.project2d(point3d.x, point3d.y, point3d.z, point3d1);
    }

    public boolean project2d_cam(Point3d point3d, Point3d point3d1) {
        return this.project2d_cam(point3d.x, point3d.y, point3d.z, point3d1);
    }

    private void shadowPairsClear() {
        this.shadowPairsList1.clear();
        this.shadowPairsMap1.clear();
        this.shadowPairsList2.clear();
        this.shadowPairsCur1 = null;
    }

    private void shadowPairsAdd(ArrayList arraylist) {
        int i = arraylist.size();
        for (int j = 0; j < i; j++) {
            Object obj = arraylist.get(j);
            if (!(obj instanceof BigshipGeneric) || obj instanceof TestRunway) continue;
            BigshipGeneric bigshipgeneric = (BigshipGeneric) obj;
            if (Actor.isValid(bigshipgeneric.getAirport()) && !this.shadowPairsMap1.containsKey(bigshipgeneric)) {
                this.shadowPairsList1.add(bigshipgeneric);
                this.shadowPairsMap1.put(bigshipgeneric, null);
            }
        }

    }

    private void shadowPairsRender() {
        int i = this.shadowPairsList1.size();
        if (i == 0) return;
        for (int j = 0; j < i; j++) {
            this.shadowPairsCur1 = (BigshipGeneric) this.shadowPairsList1.get(j);
            Point3d point3d = this.shadowPairsCur1.pos.getAbsPoint();
            double d = point3d.x - shadowPairsR;
            double d1 = point3d.y - shadowPairsR;
            double d2 = point3d.x + shadowPairsR;
            double d3 = point3d.y + shadowPairsR;
            Engine.drawEnv().getFiltered((AbstractCollection) null, d, d1, d2, d3, 14, this.shadowPairsFilter);
        }

        if (this.shadowPairsList2.size() == 0) return;
        else {
            HierMesh.renderShadowPairs(this.shadowPairsList2);
            return;
        }
    }

    private void doPreRender3D(Render render) {
        render.useClearColor(!this.bDrawLand || (RenderContext.texGetFlags() & 0x20) != 0);
        render.getCamera().pos.getRender(this.__p, this.__o);
        if (!this.bRenderMirror && this.iRenderIndx == 0) {
            SearchlightGeneric.lightPlanesBySearchlights();
            Actor actor = render.getCamera().pos.base();
            if (Actor.isValid(actor)) {
                actor.getSpeed(this.__v);
                Camera.SetTargetSpeed((float) this.__v.x, (float) this.__v.y, (float) this.__v.z);
            } else Camera.SetTargetSpeed(0.0F, 0.0F, 0.0F);
        }
        Render.enableFog(this.bEnableFog);
        if (this.bDrawClouds && this.clouds != null) this.clouds.preRender();
        if (this.bDrawLand) Engine.land().preRender((float) this.__p.z, false);
        this.darkerNight.preRender();
        DrwArray drwarray = this.bRenderMirror ? this.drwMirror : this.drwMaster[this.iRenderIndx];
        Engine.drawEnv().preRender(this.__p.x, this.__p.y, this.__p.z, World.MaxVisualDistance, 4, drwarray.drwSolid, drwarray.drwTransp, drwarray.drwShadow, true);
        Engine.drawEnv().preRender(this.__p.x, this.__p.y, this.__p.z, World.MaxLongVisualDistance, 8, drwarray.drwSolid, drwarray.drwTransp, drwarray.drwShadow, false);
        if (!this.bRenderMirror) {
            this.shadowPairsAdd(drwarray.drwSolid);
            this.shadowPairsAdd(drwarray.drwTransp);
        }
        if (!this.bRenderMirror || this.viewMirror > 1) {
            Engine.drawEnv().preRender(this.__p.x, this.__p.y, this.__p.z, World.MaxStaticVisualDistance, 2, drwarray.drwSolid, drwarray.drwTransp, drwarray.drwShadow, false);
            Engine.drawEnv().preRender(this.__p.x, this.__p.y, this.__p.z, World.MaxPlateVisualDistance, 1, drwarray.drwSolidPlate, drwarray.drwTranspPlate, drwarray.drwShadowPlate, true);
        }
        BulletGeneric.preRenderAll();
        if (this.bEnableFog) Render.enableFog(false);
    }

    private void doRender3D0(Render render) {
        boolean flag = false;
        Render.enableFog(this.bEnableFog);
        if (this.bDrawLand) {
            Engine.lightEnv().prepareForRender(this.camera3D.pos.getAbsPoint(), 8000F);
            flag = Engine.land().render0(this.bRenderMirror) != 2;
            LightPoint.clearRender();
        }
        if (flag && this.bEnableFog) Render.enableFog(false);
        DrwArray drwarray = this.bRenderMirror ? this.drwMirror : this.drwMaster[this.iRenderIndx];
        this.plateToRenderArray(drwarray.drwSolidPlate, drwarray.drwSolid);
        this.plateToRenderArray(drwarray.drwTranspPlate, drwarray.drwTransp);
        this.plateToRenderArray(drwarray.drwShadowPlate, drwarray.drwShadow);
        MeshShared.renderArray(true);
        render.drawShadow(drwarray.drwShadow);
        if (flag && this.bEnableFog) Render.enableFog(true);
        if (this.bDrawLand) Engine.land().render1(this.bRenderMirror);
        int i = gl.GetError();
        if (i != 0) System.out.println("***( GL error: " + i + " (render3d0)");
    }

    private void doRender3D1(Render render) {
        if (this.bDrawClouds && this.clouds != null && RenderContext.cfgSky.get() > 0) {
            Engine.lightEnv().prepareForRender(this.camera3D.pos.getAbsPoint(), RenderContext.cfgSky.get() * 4000F);
            SearchlightGeneric.lightCloudsBySearchlights();
            this.clouds.render();
            LightPoint.clearRender();
        }
        DrwArray drwarray = this.bRenderMirror ? this.drwMirror : this.drwMaster[this.iRenderIndx];
        render.draw(drwarray.drwSolid, drwarray.drwTransp);
        if (!this.bRenderMirror) this.shadowPairsRender();
        BulletGeneric.renderAll();
        if (this.bEnableFog) {
            Render.flush();
            Render.enableFog(false);
        }
        this.darkerNight.render();
    }

    private void plateToRenderArray(ArrayList arraylist, ArrayList arraylist1) {
        int i = arraylist.size();
        for (int j = 0; j < i; j++) {
            Actor actor = (Actor) arraylist.get(j);
            if (actor instanceof ActorLandMesh) {
                arraylist1.add(actor);
                continue;
            }
            if (actor instanceof ActorMesh && ((ActorMesh) actor).mesh() instanceof MeshShared) {
                actor.pos.getRender(this.__l);
                if (!((MeshShared) ((ActorMesh) actor).mesh()).putToRenderArray(this.__l)) actor.draw.render(actor);
            } else actor.draw.render(actor);
        }

        arraylist.clear();
    }

    public void _getAspectViewPort(int i, float af[]) {
        af[0] = i != 1 ? 0.6666667F : 0.0F;
        af[1] = 0.0F;
        af[2] = 0.3333333F;
        af[3] = 1.0F;
    }

    public void _getAspectViewPort(int i, int ai[]) {
        ai[0] = i != 1 ? 2 * RendersMain.width() / 3 : 0;
        ai[1] = 0;
        ai[2] = RendersMain.width() / 3;
        ai[3] = RendersMain.height();
    }

    private void drawTime() {
        if (!this.hud.bDrawAllMessages) return;
        if (this.bShowTime || this.isDemoPlaying()) {
            int i = TextScr.This().getViewPortWidth();
            long l = Time.current();
            if (NetMissionTrack.isPlaying()) l -= NetMissionTrack.playingStartTime;
            int j = (int) (l / 1000L % 60L);
            int k = (int) (l / 1000L / 60L);
            if (j > 9) TextScr.output(i - TextScr.font().height() * 3, 5, "" + k + ":" + j);
            else TextScr.output(i - TextScr.font().height() * 3, 5, "" + k + ":0" + j);
        }
    }

    public int mirrorX0() {
        return this.render3D0.getViewPortX0();
    }

    public int mirrorY0() {
        return 0;
    }

    public int mirrorWidth() {
        return 256;
    }

    public int mirrorHeight() {
        return 256;
    }

    public void preRenderHUD() {
    }

    public void renderHUD() {
    }

    public void renderHUDcontextResize(int i, int j) {
    }

    public void preRenderMap2D() {
    }

    public void renderMap2D() {
    }

    public void renderMap2DcontextResize(int i, int j) {
    }

    private void insertFarActorItem(int i, int j, int k, float f, String s) {
        int l = (int) this.iconFarFont.width(s);
        for (int i1 = 0; i1 < this.iconFarListLen[this.iRenderIndx]; i1++) {
            FarActorItem faractoritem = (FarActorItem) this.iconFarList[this.iRenderIndx].get(i1);
            if (f > faractoritem.z) {
                if (this.iconFarList[this.iRenderIndx].size() == this.iconFarListLen[this.iRenderIndx]) {
                    FarActorItem faractoritem1 = new FarActorItem(i, j, k, l, f, s);
                    this.iconFarList[this.iRenderIndx].add(faractoritem1);
                } else {
                    FarActorItem faractoritem2 = (FarActorItem) this.iconFarList[this.iRenderIndx].get(this.iconFarListLen[this.iRenderIndx]);
                    faractoritem2.set(i, j, k, l, f, s);
                    this.iconFarList[this.iRenderIndx].remove(this.iconFarListLen[this.iRenderIndx]);
                    this.iconFarList[this.iRenderIndx].add(i1, faractoritem2);
                }
                this.iconFarListLen[this.iRenderIndx]++;
                return;
            }
        }

        if (this.iconFarList[this.iRenderIndx].size() == this.iconFarListLen[this.iRenderIndx]) {
            FarActorItem faractoritem3 = new FarActorItem(i, j, k, l, f, s);
            this.iconFarList[this.iRenderIndx].add(faractoritem3);
        } else {
            FarActorItem faractoritem4 = (FarActorItem) this.iconFarList[this.iRenderIndx].get(this.iconFarListLen[this.iRenderIndx]);
            faractoritem4.set(i, j, k, l, f, s);
        }
        this.iconFarListLen[this.iRenderIndx]++;
    }

    private void clipFarActorItems() {
        int i = this.iconFarFont.height();
        for (int j = 0; j < this.iconFarListLen[this.iRenderIndx]; j++) {
            FarActorItem faractoritem = (FarActorItem) this.iconFarList[this.iRenderIndx].get(j);
            for (int k = j + 1; k < this.iconFarListLen[this.iRenderIndx]; k++) {
                FarActorItem faractoritem1 = (FarActorItem) this.iconFarList[this.iRenderIndx].get(k);
                if (faractoritem1.x + faractoritem1.dx >= faractoritem.x && faractoritem1.x <= faractoritem.x + faractoritem.dx && faractoritem1.y + i >= faractoritem.y && faractoritem1.y <= faractoritem.y + i) {
                    this.iconFarList[this.iRenderIndx].remove(k);
                    this.iconFarList[this.iRenderIndx].add(faractoritem1);
                    k--;
                    this.iconFarListLen[this.iRenderIndx]--;
                }
            }

        }

    }

    private void clearFarActorItems() {
        this.iconFarListLen[this.iRenderIndx] = 0;
    }

    private boolean isBomb(Actor actor) {
        return actor instanceof Bomb || actor instanceof Rocket;
    }

    protected void drawFarActors() {
        if (Main.state() != null && Main.state().id() == 18) return;
        if (this.iconFarMat == null) return;
// iconFarFontHeight = iconFarFont.height();
        this.iconClipX0 = -2D;
        this.iconClipY0 = -1D;
        if (this.bRenderMirror) {
            this.iconClipX1 = this.render2DMirror.getViewPortWidth() + 2.0F;
            this.iconClipY1 = this.render2DMirror.getViewPortHeight() + 1.0F;
        } else {
            this.iconClipX1 = this._render2D[this.iRenderIndx].getViewPortWidth() + 2.0F;
            this.iconClipY1 = this._render2D[this.iRenderIndx].getViewPortHeight() + 1.0F;
        }
// iconFarPlayerActor = World.getPlayerAircraft();
        this.iconFarViewActor = this.viewActor();
        this.iconFarPadlockItem.str = null;
        this.iconFarPadlockActor = this.getViewPadlockEnemy();
        this._camera3D[this.iRenderIndx].pos.getRender(this.farActorFilter.camp);
        Point3d point3d = this.farActorFilter.camp;
        float f = Engine.lightEnv().sun().ToLight.z;
        if (f < 0.0F) f = 0.0F;
        float f2 = Engine.lightEnv().sun().Ambient + Engine.lightEnv().sun().Diffuze * (0.25F + 0.4F * f);
        if (RenderContext.cfgHardwareShaders.get() > 0) {
            f = Engine.lightEnv().sun().Ambient + f * Engine.lightEnv().sun().Diffuze;
            if (f > 1.0F) f2 *= f;
        }
        int j = (int) (127F * f2);
        if (j > 255) j = 255;
        iconFarColor = j | j << 8 | j << 16;
        List list = Engine.targets();
        int k = list.size();
        for (int l = 0; l < k; l++) {
            Actor actor = (Actor) list.get(l);
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d3 = point3d.distance(point3d1);
            if (d3 < 25000D) this.farActorFilter.isUse(actor, d3);
        }

// iconFarPlayerActor = null;
        this.iconFarViewActor = null;
        this.iconFarPadlockActor = null;
        if (this.iconFarListLen[this.iRenderIndx] != 0) {
            this.clipFarActorItems();
            for (int i = 0; i < this.iconFarListLen[this.iRenderIndx]; i++) {
                FarActorItem faractoritem = (FarActorItem) this.iconFarList[this.iRenderIndx].get(i);
                if (this.bRenderMirror) {
                    this.transformMirror.set(faractoritem.x - faractoritem.dx, faractoritem.y, faractoritem.z, faractoritem.dx);
                    this.iconFarFont.transform(this.transformMirror, faractoritem.color, faractoritem.str);
                } else this.iconFarFont.output(faractoritem.color, faractoritem.x, faractoritem.y, faractoritem.z, faractoritem.str);
            }

        }
        if (this.iconFarPadlockItem.str != null) {
            if (!this.iconFarPadlockItem.bGround) {
                this.iconFarPadlockItem.x++;
                this.iconFarPadlockItem.y += -7.5F;
                float f1 = 16F;
                this.line3XYZ[0] = (float) (this.iconFarPadlockItem.x - f1 * 0.866D);
                this.line3XYZ[1] = (float) (this.iconFarPadlockItem.y - f1 * 0.5D);
                this.line3XYZ[2] = this.iconFarPadlockItem.z;
                this.line3XYZ[3] = (float) (this.iconFarPadlockItem.x + f1 * 0.866D);
                this.line3XYZ[4] = (float) (this.iconFarPadlockItem.y - f1 * 0.5D);
                this.line3XYZ[5] = this.iconFarPadlockItem.z;
                this.line3XYZ[6] = this.iconFarPadlockItem.x;
                this.line3XYZ[7] = this.iconFarPadlockItem.y + f1;
                this.line3XYZ[8] = this.iconFarPadlockItem.z;
            } else {
                this.camera3D.pos.getRender(this._lineP, this._lineO);
                double d = -this._lineO.getKren() * Math.PI / 180D;
                double d1 = Math.sin(d);
                double d2 = Math.cos(d);
                this.iconFarPadlockItem.x++;
                this.iconFarPadlockItem.y += -7.5F;
                float f3 = 16F;
                this.line3XYZ[0] = this.iconFarPadlockItem.x;
                this.line3XYZ[1] = this.iconFarPadlockItem.y;
                this.line3XYZ[2] = this.iconFarPadlockItem.z;
                this.line3XYZ[3] = (float) (this.iconFarPadlockItem.x + d2 * f3 * 0.25D + d1 * 1.5D * f3);
                this.line3XYZ[4] = (float) (this.iconFarPadlockItem.y - d1 * f3 * 0.25D + d2 * 1.5D * f3);
                this.line3XYZ[5] = this.iconFarPadlockItem.z;
                this.line3XYZ[6] = (float) (this.iconFarPadlockItem.x - d2 * f3 * 0.25D + d1 * 1.5D * f3);
                this.line3XYZ[7] = (float) (this.iconFarPadlockItem.y + d1 * f3 * 0.25D + d2 * 1.5D * f3);
                this.line3XYZ[8] = this.iconFarPadlockItem.z;
            }
            Render.drawBeginLines(-1);
            Render.drawLines(this.line3XYZ, 3, 1.0F, this.iconFarPadlockItem.color, Mat.TESTZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND, 5);
            Render.drawEnd();
        }
        this.clearFarActorItems();
    }

    protected void drawFarActorsInit() {
        this.iconFarMat = Mat.New("icons/faractor.mat");
        this.iconFarFont = TTFont.get("arialSmallZ");
        this.iconFarFinger = Finger.Int("iconFar_shortClassName");
    }

    public void initHotKeys() {
        CmdEnv.top().setCommand(new CmdExit(), "exit", "exit game");
        HotKeyCmdEnv.setCurrentEnv("hotkeys");
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "exit") {

            public void begin() {
                Main.doGameExit();
            }

        });
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ScreenShot") {

            public void begin() {
                if (Main3D.this.scrShot == null) Main3D.this.scrShot = new ScrShot("grab");
                if (Mission.isNet()) {
                    long l = Time.real();
                    if (Main3D.this.lastTimeScreenShot + 10000L < l) Main3D.this.lastTimeScreenShot = l;
                    else return;
                }
                Main3D.this.scrShot.grab();
            }

        });
        CmdEnv.top().setCommand(new CmdScreenSequence(), "avi", "start/stop save screen shot sequence");
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ScreenSequence") {

            public void begin() {
                if (Main3D.this.screenSequence == null) Main3D.this.screenSequence = new ScreenSequence();
                Main3D.this.screenSequence.doSave();
            }

        });
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "land") {

            public void begin() {
                if (RTSConf.cur.console.getEnv().levelAccess() == 0) Main3D.this.setDrawLand(!Main3D.this.isDrawLand());
            }

        });
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "clouds") {

            public void begin() {
                if (Mission.isSingle() && RTSConf.cur.console.getEnv().levelAccess() == 0) Main3D.this.bDrawClouds = !Main3D.this.bDrawClouds;
            }

        });
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "showTime") {

            public void begin() {
                if (RTSConf.cur.console.getEnv().levelAccess() == 0) Main3D.this.bShowTime = !Main3D.this.bShowTime;
            }

        });
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "pause") {

            public void begin() {
                if (TimeSkip.isDo()) return;
                if (Time.isEnableChangePause()) {
                    Time.setPause(!Time.isPaused());
                    if (Config.cur.isSoundUse()) if (Time.isPaused()) AudioDevice.soundsOff();
                    else AudioDevice.soundsOn();
                }
            }

        });
    }

    public static float        FOVX                   = 70F;
    public static final float  ZNEAR                  = 1.2F;
    public static final float  ZFAR                   = 48000F;
    protected boolean          bDrawIfNotFocused;
    protected boolean          bUseStartLog;
    protected boolean          bUseGUI;
    private boolean            bShowStartIntro;
    public GUIWindowManager    guiManager;
    public KeyRecord           keyRecord;
    public OrdersTree          ordersTree;
    public TimeSkip            timeSkip;
    public HookView            hookView;
    public HookViewFly         hookViewFly;
    public HookViewEnemy       hookViewEnemy;
    public AircraftHotKeys     aircraftHotKeys;
    public Cockpit             cockpits[];
    public Cockpit             cockpitCur;
    public OverLoad            overLoad;
    public OverLoad            _overLoad[];
    public SunGlare            sunGlare;
    public SunGlare            _sunGlare[];
    public LightsGlare         lightsGlare;
    public LightsGlare         _lightsGlare[];
    private SunFlare           _sunFlare[];
    public Render              _sunFlareRender[];
    private boolean            bViewFly;
    private boolean            bViewEnemy;
    public boolean             bEnableFog;
    private boolean            bDrawLand;
    // TODO: +++ Cloud visibility fix +++
    // public EffClouds clouds; // REMOVE THIS!!! It hides the Main's parameter of the same name!
    // TODO: --- Cloud visibility fix ---
    public Zip                 zip;
    public boolean             bDrawClouds;
    public SpritesFog          spritesFog;
    public Cinema              _cinema[];
    public Render3D0R          render3D0R;
    public Camera3DR           camera3DR;
    public Render3D0           render3D0;
    public Render3D1           render3D1;
    public Camera3D            camera3D;
    public Render3D0           _render3D0[];
    public Render3D1           _render3D1[];
    public Camera3D            _camera3D[];
    public Render2D            render2D;
    public CameraOrtho2D       camera2D;
    public Render2D            _render2D[];
    public CameraOrtho2D       _camera2D[];
    public Render3D0Mirror     render3D0Mirror;
    public Render3D1Mirror     render3D1Mirror;
    public Camera3D            camera3DMirror;
    public Render2DMirror      render2DMirror;
    public CameraOrtho2D       camera2DMirror;
    public RenderCockpit       renderCockpit;
    public Camera3D            cameraCockpit;
    public RenderCockpit       _renderCockpit[];
    public Camera3D            _cameraCockpit[];
    public RenderCockpitMirror renderCockpitMirror;
    public Camera3D            cameraCockpitMirror;
    public RenderHUD           renderHUD;
    public CameraOrtho2D       cameraHUD;
    public HUD                 hud;
    public RenderMap2D         renderMap2D;
    public CameraOrtho2D       cameraMap2D;
    public Land2D              land2D;
    public Land2DText          land2DText;
    public DarkerNight         darkerNight;
    private static String      _sLastMusic            = "ru";
    protected int              viewMirror;
    private int                iconTypes;
    public static final String gameHotKeyCmdEnvs[]    = { "Console", "hotkeys", "HookView", "PanView", "SnapView", "pilot", "move", "gunner", "misc", "orders", "aircraftView", "timeCompression", "gui" };
    public static final String builderHotKeyCmdEnvs[] = { "Console", "builder", "hotkeys" };
    KeyRecordCallback          playRecordedMissionCallback;
    String                     playRecordedFile;
    int                        playBatchCurRecord;
    boolean                    playEndBatch;
    SectFile                   playRecordedSect;
    int                        playRecorderIndx;
    String                     playRecordedPlayFile;
    InOutStreams               playRecordedStreams;
    NetChannelInStream         playRecordedNetChannelIn;
    GameTrack                  gameTrackRecord;
    GameTrack                  gameTrackPlay;
    private boolean            bLoadRecordedStates1Before;
    private boolean            bRenderMirror;
    private int                iRenderIndx;
    protected double           _modelMatrix3D[][];
    protected double           _projMatrix3D[][];
    protected int              _viewport[][];
    protected double           _modelMatrix3DMirror[];
    protected double           _projMatrix3DMirror[];
    protected int              _viewportMirror[];
    private double             _dIn[];
    private double             _dOut[];
    private static double      shadowPairsR;
    private static double      shadowPairsR2;
    private ArrayList          shadowPairsList1;
    private HashMap            shadowPairsMap1;
    private BigshipGeneric     shadowPairsCur1;
    private ArrayList          shadowPairsList2;
    private ActorFilter        shadowPairsFilter;
    private DrwArray           drwMaster[]            = { new DrwArray(), new DrwArray(), new DrwArray() };
    private DrwArray           drwMirror;
    private Loc                __l;
    private Point3d            __p;
    private Orient             __o;
    private Vector3d           __v;
    private boolean            bShowTime;
    public static final String ICONFAR_PROPERTY       = "iconFar_shortClassName";
    public static final float  iconFarActorSizeX      = 2F;
    public static final float  iconFarActorSizeY      = 1F;
    public static final float  iconFarSmallActorSize  = 1F;
    public static int          iconFarColor           = 0x7f7f7f;
    protected double           iconGroundDrawMin;
    protected double           iconSmallDrawMin;
    protected double           iconAirDrawMin;
    protected double           iconDrawMax;
    private Mat                iconFarMat;
    private TTFont             iconFarFont;
    private int                iconFarFinger;
// private float iconFarFontHeight;
    private double             iconClipX0;
    private double             iconClipX1;
    private double             iconClipY0;
    private double             iconClipY1;
// private Actor iconFarPlayerActor;
    private Actor              iconFarViewActor;
    private Actor              iconFarPadlockActor;
    private FarActorItem       iconFarPadlockItem;
    private ArrayList          iconFarList[]          = { new ArrayList(), new ArrayList(), new ArrayList() };
    private int                iconFarListLen[]       = { 0, 0, 0 };
    private FarActorFilter     farActorFilter;
    private float              line3XYZ[];
    private Point3d            _lineP;
    private Orient             _lineO;
    private TransformMirror    transformMirror;
    private long               lastTimeScreenShot;
    private ScrShot            scrShot;
    private ScreenSequence     screenSequence;

    static {
        shadowPairsR = 1000D;
        shadowPairsR2 = shadowPairsR * shadowPairsR;
    }

}
