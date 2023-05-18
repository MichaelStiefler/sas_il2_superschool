/*Modified Main3D class for the SAS Engine Mod*/

package com.maddox.il2.game;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.order.OrdersTree;
import com.maddox.il2.game.HUD;
import com.maddox.il2.gui.*;
import com.maddox.il2.net.*;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.effects.*;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.TestRunway;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.lights.SearchlightGeneric;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.Rocket;
import com.maddox.il2.objects.weapons.MissileInterceptable;
import com.maddox.opengl.GLContext;
import com.maddox.opengl.gl;
import com.maddox.opengl.util.ScrShot;
import com.maddox.rts.*;
import com.maddox.rts.cmd.CmdSFS;
import com.maddox.sound.AudioDevice;
import java.io.*;
import java.util.*;

public class Main3D extends Main
{
    class CmdExit extends Cmd
    {

        public Object exec(CmdEnv cmdenv, Map map)
        {
            Main.doGameExit();
            return null;
        }

        CmdExit()
        {
        }
    }

    class CmdScreenSequence extends Cmd
    {

        public Object exec(CmdEnv cmdenv, Map map)
        {
            if(screenSequence == null)
                screenSequence = new ScreenSequence();
            screenSequence.doSave();
            return null;
        }

        CmdScreenSequence()
        {
        }
    }

    class ScreenSequence extends Actor
    {
        class Interpolater extends Interpolate
        {

            public boolean tick()
            {
                if(bSave)
                    shot.grab();
                return true;
            }

            Interpolater()
            {
            }
        }


        public void doSave()
        {
            bSave = !bSave;
        }

        public Object getSwitchListener(Message message)
        {
            return this;
        }

        protected void createActorHashCode()
        {
            makeActorRealHashCode();
        }

        boolean bSave;
        ScrShot shot;

        public ScreenSequence()
        {
            bSave = false;
            shot = new ScrShot("s");
            flags |= 0x4000;
            interpPut(new Interpolater(), "grabber", Time.current(), null);
        }
    }

    private static class TransformMirror extends TTFontTransform
    {

        public void get(float f, float f1, float af[])
        {
            af[0] = (x0 + dx) - f;
            af[1] = y0 + f1;
            af[2] = z0;
        }

        public void set(float f, float f1, float f2, float f3)
        {
            x0 = f;
            y0 = f1;
            z0 = f2;
            dx = f3;
        }

        private float x0;
        private float y0;
        private float dx;
        private float z0;

        private TransformMirror()
        {
        }

    }

    class FarActorFilter
        implements ActorFilter
    {

        private String lenToString(int i)
        {
            String s;
            if(i >= 1000)
                s = i / 1000 + ".";
            else
                s = ".";
            i %= 1000;
            if(i < 10)
                return s + "00";
            if(i < 100)
                return s + "0" + i / 10;
            else
                return s + i / 10;
        }

        private void drawPointer(int i, double d, int j, int k)
        {
            double d1 = Math.atan2(k, j);
            double d2 = Math.atan2(p2d.y - (double)k, p2d.x - (double)j);
            if(p2d.z > 1.0D)
                if(d2 > 0.0D)
                    d2 = -3.1415926535897931D + d2;
                else
                    d2 = 3.1415926535897931D + d2;
            double d3;
            double d4;
            if(d2 >= 0.0D)
            {
                if(d2 <= d1)
                {
                    d3 = j;
                    d4 = Math.tan(d2) * (double)j;
                } else
                if(d2 <= 3.1415926535897931D - d1)
                {
                    d4 = k;
                    d3 = Math.tan(1.5707963267948966D - d2) * (double)k;
                } else
                {
                    d3 = -j;
                    d4 = -Math.tan(d2) * (double)j;
                }
            } else
            if(d2 >= -d1)
            {
                d3 = j;
                d4 = Math.tan(d2) * (double)j;
            } else
            if(d2 >= -3.1415926535897931D + d1)
            {
                d4 = -k;
                d3 = -Math.tan(1.5707963267948966D - d2) * (double)k;
            } else
            {
                d3 = -j;
                d4 = -Math.tan(d2) * (double)j;
            }
            d3 += j;
            d4 += k;
            HUD.addPointer((float)d3, (float)d4, Army.color(i), (float)((1.0D - d / iconDrawMax) * 0.80000000000000004D + 0.20000000000000001D), (float)d2);
        }

        public boolean isUse(Actor actor, double d)
        {
            if(actor == iconFarViewActor)
                return false;
            if(d <= 5D)
                return false;
            int i = actor.getArmy();
            if(i == 0 && !isBomb(actor))
                return false;
            DotRange dotrange = i != World.getPlayerArmy() ? dotRangeFoe : dotRangeFriendly;
            double d1 = 1.0D;
            double d2 = 0.078D + (double)(1.2F / Main3D.FOVX);
            if(Main3D.FOVX < 24F)
                d2 = 0.16D;
            if((actor instanceof ActorMesh) && ((ActorMesh)actor).mesh() != null)
            {
                float f = ((ActorMesh)actor).mesh().visibilityR();
                if(f > 0.0F)
                    if(f > 100F)
                    {
                        float f1 = ((ActorMesh)actor).collisionR();
                        if(f1 > 0.0F)
                            d1 = (double)f1 * d2;
                    } else
                    {
                        d1 = (double)f * d2;
                    }
            }
            //TODO:
            if((actor instanceof Aircraft) || ((actor instanceof MissileInterceptable) && (((MissileInterceptable)actor).isReleased())))
            {
                if(d1 < 0.65000000000000002D)
                    d1 = 0.65000000000000002D;
                if(d1 > 2.2000000000000002D)
                    d1 = 2.2000000000000002D;
            } else
            {
                d1 *= 1.2D;
            }
            iconDrawMax = dotrange.dot(d1);
            if(d > iconDrawMax)
                return false;
            actor.pos.getRender(p3d);
            if(!project2d_cam(p3d, p2d))
                return false;
            if(p2d.z > 1.0D || p2d.x < iconClipX0 || p2d.x > iconClipX1 || p2d.y < iconClipY0 || p2d.y > iconClipY1)
            {
                if(bRenderMirror)
                    return false;
                //TODO:
                if((!(actor instanceof Aircraft)) && (!((actor instanceof MissileInterceptable) && (((MissileInterceptable)actor).isReleased()))))
                    return false;
                if(isViewInsideShow())
                    return false;
                if(World.cur().diffCur.No_Icons)
                    return false;
                if(iRenderIndx == 0)
                    drawPointer(i, d, render2D.getViewPortWidth() / 2, render2D.getViewPortHeight() / 2);
                return false;
            }
            int j = (int)(p2d.x - 1.0D);
            int k = (int)(p2d.y - 0.5D);
            int l = 0x7f7f7f;
            int i1 = 255;
            int j1 = 0;
            if(bEnableFog)
            {
                Render.enableFog(true);
                l = Landscape.getFogRGBA((float)p3d.x, (float)p3d.y, (float)p3d.z);
                j1 = l >>> 24;
                i1 -= j1;
                Render.enableFog(false);
            }
            int k1 = ((int)(dotrange.alphaDot(d * 2.2000000000000002D, d1) * 255D) & 0xff) << 24;
            int l1 = ((int)(dotrange.alphaDot(d, d1) * 255D) & 0xff) << 24;
            int i2 = k1 | (Main3D.iconFarColor & 0xff) * i1 + (l & 0xff) * j1 >>> 8 | ((Main3D.iconFarColor >>> 8 & 0xff) * i1 + (l >>> 8 & 0xff) * j1 >>> 8) << 8 | ((Main3D.iconFarColor >>> 16 & 0xff) * i1 + (l >>> 16 & 0xff) * j1 >>> 8) << 16;
            int j2 = l1 | (Main3D.iconFarColor >>> 1 & 0x3f) * i1 + (l >>> 0 & 0xff) * j1 >>> 8 | ((Main3D.iconFarColor >>> 9 & 0x3f) * i1 + (l >>> 8 & 0xff) * j1 >>> 8) << 8 | ((Main3D.iconFarColor >>> 17 & 0x3f) * i1 + (l >>> 16 & 0xff) * j1 >>> 8) << 16;
            if((actor instanceof Aircraft) || ((actor instanceof MissileInterceptable) && (((MissileInterceptable)actor).isReleased())))
            {
                if(d > iconAirDrawMin)
                {
                    Render.drawTile(j, (float)k + 1.0F, 2.0F, 1.0F, (float)(-p2d.z), iconFarMat, i2, 0.0F, 1.0F, 1.0F, -1F);
                    Render.drawTile(j, k, 2.0F, 1.0F, (float)(-p2d.z), iconFarMat, j2, 0.0F, 1.0F, 1.0F, -1F);
                }
            } else
            if(isBomb(actor))
            {
                if(d > iconSmallDrawMin)
                {
                    Render.drawTile(j, (float)k + 1.0F, 1.0F, 1.0F, (float)(-p2d.z), iconFarMat, i2, 0.0F, 1.0F, 1.0F, -1F);
                    Render.drawTile(j, k, 1.0F, 1.0F, (float)(-p2d.z), iconFarMat, j2, 0.0F, 1.0F, 1.0F, -1F);
                }
            } else
            if(d > iconGroundDrawMin)
            {
                Render.drawTile(j, (float)k + 1.0F, 2.0F, 1.0F, (float)(-p2d.z), iconFarMat, i2, 0.0F, 1.0F, 1.0F, -1F);
                Render.drawTile(j, k, 2.0F, 1.0F, (float)(-p2d.z), iconFarMat, j2, 0.0F, 1.0F, 1.0F, -1F);
            }
            k += 8;
            if(actor == iconFarPadlockActor && iconTypes() != 0)
            {
                iconFarPadlockItem.set(dotrange.colorIcon(d, i, i1), j, k, 0, (float)(-p2d.z), "");
                iconFarPadlockItem.bGround = !((actor instanceof Aircraft) || ((actor instanceof MissileInterceptable) && (((MissileInterceptable)actor).isReleased())));
                if(World.cur().diffCur.No_Icons)
                {
                    int k2 = ((int)(dotrange.alphaIcon(d) * (double)i1) & 0xff) << 24;
                    iconFarPadlockItem.color = k2 | 0xff00;
                }
            }
            if(!(actor instanceof Aircraft) && !((actor instanceof MissileInterceptable) && (((MissileInterceptable)actor).isReleased())))
                return false;
            if(World.cur().diffCur.No_Icons)
                return false;
            if(iconTypes() == 0)
                return false;
            if(d >= dotrange.icon())
                return false;
            String s = null;
            String s1 = null;
            String s2 = null;
            switch(iconTypes())
            {
            case 3: // '\003'
            default:
                if(d <= dotrange.type())
                {
                    s = Property.stringValue(actor.getClass(), iconFarFinger, null);
                    if(s == null)
                    {
                        s = actor.getClass().getName();
                        int l2 = s.lastIndexOf('.');
                        s = s.substring(l2 + 1);
                        Property.set(actor.getClass(), "iconFar_shortClassName", s);
                    }
                }
                // fall through

            case 2: // '\002'
              if(actor instanceof Aircraft) {
                if(d <= dotrange.id())
                    s1 = ((Aircraft)actor).typedName();
                if(Mission.isNet() && ((NetAircraft)actor).isNetPlayer() && i == World.getPlayerArmy() && d <= dotrange.name())
                {
                    NetUser netuser = ((NetAircraft)actor).netUser();
                    if(s1 != null)
                        s1 = s1 + " " + netuser.uniqueName();
                    else
                        s1 = netuser.uniqueName();
                }
              } else if(actor instanceof MissileInterceptable) {
                Property.set(actor.getClass(), "iconFar_shortClassName", s);
              }
                break;

            case 1: // '\001'
                break;
            }
            if(d <= dotrange.range())
                s2 = lenToString((int)d);
            String s3 = null;
            if(s2 != null)
                s3 = s2;
            if(s != null)
                if(s3 != null)
                    s3 = s3 + " " + s;
                else
                    s3 = s;
            if(s1 != null)
                if(s3 != null)
                    s3 = s3 + ":" + s1;
                else
                    s3 = s1;
            if(s3 != null)
                insertFarActorItem(dotrange.colorIcon(d, i, i1), j, k, (float)(-p2d.z), s3);
            return false;
        }

        Point3d p3d;
        Point3d p2d;
        Point3d camp;

        FarActorFilter()
        {
            p3d = new Point3d();
            p2d = new Point3d();
            camp = new Point3d();
        }
    }

    static class FarActorItem
    {

        public void set(int i, int j, int k, int l, float f, String s)
        {
            color = i;
            x = j;
            y = k;
            dx = l;
            z = f;
            str = s;
        }

        public int color;
        public int x;
        public int y;
        public int dx;
        public float z;
        public String str;
        public boolean bGround;

        public FarActorItem(int i, int j, int k, int l, float f, String s)
        {
            set(i, j, k, l, f, s);
        }
    }

    public class RenderMap2D extends Render
    {

        protected void contextResize(int i, int j)
        {
            super.contextResize(i, j);
            renderMap2DcontextResize(i, j);
            if(land2DText != null)
                land2DText.contextResized();
        }

        public void preRender()
        {
            preRenderMap2D();
            if(Main.state() != null && Main.state().id() == 18)
            {
                GUIBuilder guibuilder = (GUIBuilder)Main.state();
                guibuilder.builder.preRenderMap2D();
            }
        }

        public void render()
        {
            if(land2D != null)
                land2D.render();
            if(Main.state() != null && Main.state().id() == 18)
            {
                GUIBuilder guibuilder = (GUIBuilder)Main.state();
                guibuilder.builder.renderMap2D();
            } else
            if(land2DText != null)
                land2DText.render();
            renderMap2D();
        }

        public RenderMap2D(float f)
        {
            this(Engine.rendersMain(), f);
        }

        public RenderMap2D(Renders renders, float f)
        {
            super(renders, f);
            useClearDepth(false);
            setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
        }
    }

    public class RenderHUD extends Render
    {

        protected void contextResize(int i, int j)
        {
            super.contextResize(i, j);
            renderHUDcontextResize(i, j);
            hud.contextResize(i, j);
            renders().setCommonClearColor(viewPort[0] != 0.0F || viewPort[1] != 0.0F);
        }

        public void preRender()
        {
            hud.preRender();
            preRenderHUD();
        }

        public void render()
        {
            hud.render();
            renderHUD();
        }

        public RenderHUD(float f)
        {
            this(Engine.rendersMain(), f);
        }

        public RenderHUD(Renders renders, float f)
        {
            super(renders, f);
            useClearDepth(false);
            useClearColor(false);
        }
    }

    public class RenderCockpitMirror extends Render
    {

        protected void contextResize(int i, int j)
        {
            setViewPort(new int[] {
                mirrorX0(), mirrorY0(), mirrorWidth(), mirrorHeight()
            });
            if(camera != null)
                ((Camera3D)camera).set(((Camera3D)camera).FOV(), (float)mirrorWidth() / (float)mirrorHeight());
        }

        public boolean isShow()
        {
            if(viewMirror > 0 && renderCockpit.isShow())
                return cockpitCur.isExistMirror();
            else
                return false;
        }

        public void preRender()
        {
            if(Actor.isValid(cockpitCur) && cockpitCur.isFocused())
                cockpitCur.preRender(true);
        }

        public void render()
        {
            if(Actor.isValid(cockpitCur) && cockpitCur.isFocused())
            {
                cockpitCur.render(true);
                Render.flush();
                cockpitCur.grabMirrorFromScreen(mirrorX0(), mirrorY0(), mirrorWidth(), mirrorHeight());
            }
        }

        public RenderCockpitMirror(float f)
        {
            super(Engine.rendersMain(), f);
            useClearDepth(true);
            useClearColor(false);
            contextResized();
        }
    }

    public class RenderCockpit extends Render
    {

        public void preRender()
        {
            iRenderIndx = _indx;
            if(Actor.isValid(cockpitCur) && cockpitCur.isFocused())
                cockpitCur.preRender(false);
            iRenderIndx = 0;
        }

        public void render()
        {
            iRenderIndx = _indx;
            if(Actor.isValid(cockpitCur) && cockpitCur.isFocused())
                cockpitCur.render(false);
            iRenderIndx = 0;
        }

        public void getAspectViewPort(float af[])
        {
            if(_indx == 0)
            {
                super.getAspectViewPort(af);
                return;
            } else
            {
                _getAspectViewPort(_indx, af);
                return;
            }
        }

        public void getAspectViewPort(int ai[])
        {
            if(_indx == 0)
            {
                super.getAspectViewPort(ai);
                return;
            } else
            {
                _getAspectViewPort(_indx, ai);
                return;
            }
        }

        public boolean isShow()
        {
            if(_indx == 0)
                return super.isShow();
            else
                return Config.cur.isUse3Renders() && renderCockpit.isShow();
        }

        int _indx;

        public RenderCockpit(int i, float f)
        {
            this(i, Engine.rendersMain(), f);
        }

        public RenderCockpit(int i, Renders renders, float f)
        {
            super(renders, f);
            _indx = 0;
            _indx = i;
            useClearDepth(true);
            useClearColor(false);
            contextResized();
        }
    }

    public class Render2DMirror extends Render
    {

        protected void contextResize(int i, int j)
        {
            setViewPort(new int[] {
                mirrorX0(), mirrorY0(), mirrorWidth(), mirrorHeight()
            });
            if(camera != null)
                ((CameraOrtho2D)camera).set(0.0F, mirrorWidth(), 0.0F, mirrorHeight());
        }

        public boolean isShow()
        {
            return viewMirror > 0 && render3D0.isShow() && !isViewOutside() && cockpitCur.isExistMirror();
        }

        public void render()
        {
            bRenderMirror = true;
            if(bEnableFog)
                Render.enableFog(false);
            drawFarActors();
            if(bEnableFog)
                Render.enableFog(true);
            bRenderMirror = false;
        }

        public Render2DMirror(float f)
        {
            super(Engine.rendersMain(), f);
            useClearDepth(false);
            useClearColor(false);
            contextResized();
        }
    }

    public class Render3D1Mirror extends Render3DMirror
    {

        public void render()
        {
            bRenderMirror = true;
            doRender3D1(this);
            bRenderMirror = false;
        }

        public Render3D1Mirror(float f)
        {
            super(f);
            useClearColor(false);
            useClearDepth(false);
            useClearStencil(false);
        }
    }

    public class Render3D0Mirror extends Render3DMirror
    {

        public void preRender()
        {
            bRenderMirror = true;
            doPreRender3D(this);
            bRenderMirror = false;
        }

        public void render()
        {
            camera.activateWorldMode(0);
            gl.GetDoublev(2982, _modelMatrix3DMirror);
            gl.GetDoublev(2983, _projMatrix3DMirror);
            gl.GetIntegerv(2978, _viewportMirror);
            camera.deactivateWorldMode();
            bRenderMirror = true;
            doRender3D0(this);
            bRenderMirror = false;
        }

        public Render3D0Mirror(float f)
        {
            super(f);
            setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            useClearStencil(true);
        }
    }

    public class Render3DMirror extends Render
    {

        protected void contextResize(int i, int j)
        {
            setViewPort(new int[] {
                mirrorX0(), mirrorY0(), mirrorWidth(), mirrorHeight()
            });
            if(camera != null)
                ((Camera3D)camera).set(((Camera3D)camera).FOV(), (float)mirrorWidth() / (float)mirrorHeight());
        }

        public boolean isShow()
        {
            return viewMirror > 0 && render3D0.isShow() && !isViewOutside() && cockpitCur.isExistMirror();
        }

        public Render3DMirror(float f)
        {
            super(Engine.rendersMain(), f);
            contextResized();
        }
    }

    public class Render2D extends Render
    {

        public void render()
        {
            iRenderIndx = _indx;
            if(bEnableFog)
                Render.enableFog(false);
            drawFarActors();
            if(bEnableFog)
                Render.enableFog(true);
            iRenderIndx = 0;
        }

        public void getAspectViewPort(float af[])
        {
            if(_indx == 0)
            {
                super.getAspectViewPort(af);
                return;
            } else
            {
                _getAspectViewPort(_indx, af);
                return;
            }
        }

        public void getAspectViewPort(int ai[])
        {
            if(_indx == 0)
            {
                super.getAspectViewPort(ai);
                return;
            } else
            {
                _getAspectViewPort(_indx, ai);
                return;
            }
        }

        public boolean isShow()
        {
            if(_indx == 0)
                return super.isShow();
            if(Main.state() != null && Main.state().id() == 18)
                return false;
            else
                return Config.cur.isUse3Renders() && render2D.isShow();
        }

        int _indx;

        public Render2D(int i, float f)
        {
            this(i, Engine.rendersMain(), f);
        }

        public Render2D(int i, Renders renders, float f)
        {
            super(renders, f);
            _indx = 0;
            _indx = i;
            useClearDepth(false);
            useClearColor(false);
            contextResized();
        }
    }

    public class Render3D1 extends Render
    {

        public void preRender()
        {
            if(_indx == 0)
                drawTime();
        }

        public void render()
        {
            iRenderIndx = _indx;
            doRender3D1(this);
            if(Main.state() != null && Main.state().id() == 18 && iRenderIndx == 0)
            {
                GUIBuilder guibuilder = (GUIBuilder)Main.state();
                guibuilder.builder.render3D();
            }
            iRenderIndx = 0;
        }

        public void getAspectViewPort(float af[])
        {
            if(_indx == 0)
            {
                super.getAspectViewPort(af);
                return;
            } else
            {
                _getAspectViewPort(_indx, af);
                return;
            }
        }

        public void getAspectViewPort(int ai[])
        {
            if(_indx == 0)
            {
                super.getAspectViewPort(ai);
                return;
            } else
            {
                _getAspectViewPort(_indx, ai);
                return;
            }
        }

        public boolean isShow()
        {
            if(_indx == 0)
                return super.isShow();
            if(Main.state() != null && Main.state().id() == 18)
                return false;
            else
                return Config.cur.isUse3Renders() && render3D1.isShow();
        }

        int _indx;

        public Render3D1(int i, float f)
        {
            this(i, Engine.rendersMain(), f);
        }

        public Render3D1(int i, Renders renders, float f)
        {
            super(renders, f);
            _indx = 0;
            _indx = i;
            useClearColor(false);
            useClearDepth(false);
            useClearStencil(false);
            contextResized();
        }
    }

    public class Render3D0 extends Render
    {

        public void preRender()
        {
            iRenderIndx = _indx;
            if(_indx == 0)
                shadowPairsClear();
            doPreRender3D(this);
            iRenderIndx = 0;
        }

        public void render()
        {
            iRenderIndx = _indx;
            camera.activateWorldMode(0);
            gl.GetDoublev(2982, _modelMatrix3D[iRenderIndx]);
            gl.GetDoublev(2983, _projMatrix3D[iRenderIndx]);
            gl.GetIntegerv(2978, _viewport[iRenderIndx]);
            camera.deactivateWorldMode();
            doRender3D0(this);
            iRenderIndx = 0;
        }

        public void getAspectViewPort(float af[])
        {
            if(_indx == 0)
            {
                super.getAspectViewPort(af);
                return;
            } else
            {
                _getAspectViewPort(_indx, af);
                return;
            }
        }

        public void getAspectViewPort(int ai[])
        {
            if(_indx == 0)
            {
                super.getAspectViewPort(ai);
                return;
            } else
            {
                _getAspectViewPort(_indx, ai);
                return;
            }
        }

        public boolean isShow()
        {
            if(_indx == 0)
                return super.isShow();
            if(Main.state() != null && Main.state().id() == 18)
                return false;
            else
                return Config.cur.isUse3Renders() && render3D0.isShow();
        }

        int _indx;

        public Render3D0(int i, float f)
        {
            this(i, Engine.rendersMain(), f);
        }

        public Render3D0(int i, Renders renders, float f)
        {
            super(renders, f);
            _indx = 0;
            _indx = i;
            setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            useClearStencil(true);
            contextResized();
        }
    }

    class DrwArray
    {

        ArrayList drwSolidPlate;
        ArrayList drwTranspPlate;
        ArrayList drwShadowPlate;
        ArrayList drwSolid;
        ArrayList drwTransp;
        ArrayList drwShadow;

        DrwArray()
        {
            drwSolidPlate = new ArrayList();
            drwTranspPlate = new ArrayList();
            drwShadowPlate = new ArrayList();
            drwSolid = new ArrayList();
            drwTransp = new ArrayList();
            drwShadow = new ArrayList();
        }
    }

    class ShadowPairsFilter
        implements ActorFilter
    {

        public boolean isUse(Actor actor, double d)
        {
            if(actor != shadowPairsCur1 && (actor instanceof ActorHMesh) && d <= Main3D.shadowPairsR2 && ((ActorHMesh)actor).hierMesh() != null)
            {
                shadowPairsList2.add(shadowPairsCur1.hierMesh());
                shadowPairsList2.add(((ActorHMesh)actor).hierMesh());
            }
            return false;
        }

        ShadowPairsFilter()
        {
        }
    }

    public class Render3D0R extends Render
    {

        public void preRender()
        {
            if(Engine.land().ObjectsReflections_Begin(0) == 1)
            {
                getCamera().pos.getRender(__p, __o);
                boolean flag = true;
                if(Landscape.isExistMeshs())
                {
                    drwSolid.clear();
                    drwTransp.clear();
                    World _tmp = Engine.cur.world;
                    Engine.drawEnv().preRender(__p.x, __p.y, __p.z, World.MaxVisualDistance * 0.5F, 1, drwSolidL, drwTranspL, null, flag);
                    int i = drwSolidL.size();
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)drwSolidL.get(j);
                        if(actor instanceof ActorLandMesh)
                            drwSolid.add(actor);
                    }

                    i = drwTranspL.size();
                    for(int k = 0; k < i; k++)
                    {
                        Actor actor1 = (Actor)drwTranspL.get(k);
                        if(actor1 instanceof ActorLandMesh)
                            drwTransp.add(actor1);
                    }

                    flag = false;
                }
                World _tmp1 = Engine.cur.world;
                Engine.drawEnv().preRender(__p.x, __p.y, __p.z, World.MaxVisualDistance * 0.5F, 14, drwSolid, drwTransp, null, flag);
                Engine.land().ObjectsReflections_End();
            }
        }

        public void render()
        {
            if(Engine.land().ObjectsReflections_Begin(1) == 1)
            {
                draw(drwSolid, drwTransp);
                Engine.land().ObjectsReflections_End();
            }
        }

        public boolean isShow()
        {
            return bDrawLand && render3D0.isShow();
        }

        ArrayList drwSolidL;
        ArrayList drwTranspL;
        ArrayList drwSolid;
        ArrayList drwTransp;

        public Render3D0R(float f)
        {
            super(Engine.rendersMain(), f);
            drwSolidL = new ArrayList();
            drwTranspL = new ArrayList();
            drwSolid = new ArrayList();
            drwTransp = new ArrayList();
        }
    }

    public static class HookReflection extends HookRender
    {

        public boolean computeRenderPos(Actor actor, Loc loc, Loc loc1)
        {
            computePos(actor, loc, loc1);
            return true;
        }

        public void computePos(Actor actor, Loc loc, Loc loc1)
        {
            Point3d point3d = loc.getPoint();
            Orient orient = loc.getOrient();
            loc1.set(loc);
        }

        public HookReflection()
        {
        }
    }

    public static class Camera3DR extends Camera3D
    {

        public boolean activate(float f, int i, int j, int k, int l, int i1, int j1, 
                int k1, int l1, int i2, int j2)
        {
            Engine.land().ObjectsReflections_Begin(0);
            return super.activate(f, i / 2, j / 2, k, l, i1 / 2, j1 / 2, k1, l1, i2 / 2, j2 / 2);
        }

        public Camera3DR()
        {
        }
    }

    public class NetCamera3D extends Camera3D
    {

        public void set(float f)
        {
            super.set(f);
            _camera3D[1].set(f);
            _camera3D[1].pos.setRel(new Orient(-f, 0.0F, 0.0F));
            _camera3D[2].set(f);
            _camera3D[2].pos.setRel(new Orient(f, 0.0F, 0.0F));
            _cameraCockpit[1].set(f);
            _cameraCockpit[1].pos.setRel(new Orient(-f, 0.0F, 0.0F));
            _cameraCockpit[2].set(f);
            _cameraCockpit[2].pos.setRel(new Orient(f, 0.0F, 0.0F));
            camera3DR.set(f);
        }

        public NetCamera3D()
        {
        }
    }


    public Main3D()
    {
        bDrawIfNotFocused = false;
        bUseStartLog = false;
        bUseGUI = true;
        bShowStartIntro = false;
        _overLoad = new OverLoad[3];
        _sunGlare = new SunGlare[3];
        _lightsGlare = new LightsGlare[3];
        _sunFlare = new SunFlare[3];
        _sunFlareRender = new Render[3];
        bViewFly = false;
        bViewEnemy = false;
        bEnableFog = true;
        bDrawLand = false;
        bDrawClouds = false;
        _cinema = new Cinema[3];
        _render3D0 = new Render3D0[3];
        _render3D1 = new Render3D1[3];
        _camera3D = new Camera3D[3];
        _render2D = new Render2D[3];
        _camera2D = new CameraOrtho2D[3];
        _renderCockpit = new RenderCockpit[3];
        _cameraCockpit = new Camera3D[3];
        viewMirror = 2;
        iconTypes = 3;
        bLoadRecordedStates1Before = false;
        bRenderMirror = false;
        iRenderIndx = 0;
        _modelMatrix3D = new double[3][16];
        _projMatrix3D = new double[3][16];
        _viewport = new int[3][4];
        _modelMatrix3DMirror = new double[16];
        _projMatrix3DMirror = new double[16];
        _viewportMirror = new int[4];
        _dIn = new double[4];
        _dOut = new double[4];
        shadowPairsList1 = new ArrayList();
        shadowPairsMap1 = new HashMap();
        shadowPairsCur1 = null;
        shadowPairsList2 = new ArrayList();
        shadowPairsFilter = new ShadowPairsFilter();
        drwMirror = new DrwArray();
        __l = new Loc();
        __p = new Point3d();
        __o = new Orient();
        __v = new Vector3d();
        bShowTime = false;
        iconGroundDrawMin = 200D;
        iconSmallDrawMin = 100D;
        iconAirDrawMin = 1000D;
        iconDrawMax = 10000D;
        iconFarPadlockItem = new FarActorItem(0, 0, 0, 0, 0.0F, null);
        farActorFilter = new FarActorFilter();
        line3XYZ = new float[9];
        _lineP = new Point3d();
        _lineO = new Orient();
        transformMirror = new TransformMirror();
        lastTimeScreenShot = 0L;
        scrShot = null;
    }

    public static Main3D cur3D()
    {
        return (Main3D)cur();
    }

    public boolean isUseStartLog()
    {
        return bUseStartLog;
    }

    public boolean isShowStartIntro()
    {
        return bShowStartIntro;
    }

    public boolean isDrawLand()
    {
        return bDrawLand;
    }

    public void setDrawLand(boolean flag)
    {
        bDrawLand = flag;
    }

    public boolean isDemoPlaying()
    {
        if(playRecordedStreams != null)
            return true;
        if(keyRecord == null)
            return false;
        else
            return keyRecord.isPlaying();
    }

    public Actor viewActor()
    {
        return camera3D.pos.base();
    }

    public boolean isViewInsideShow()
    {
        if(!Actor.isValid(cockpitCur))
            return true;
        else
            return !cockpitCur.isNullShow();
    }

    public boolean isEnableRenderingCockpit()
    {
        if(!Actor.isValid(cockpitCur))
            return true;
        else
            return cockpitCur.isEnableRendering();
    }

    public boolean isViewOutside()
    {
        if(!Actor.isValid(cockpitCur))
            return true;
        else
            return !cockpitCur.isFocused();
    }

    public boolean isViewPadlock()
    {
        if(!Actor.isValid(cockpitCur))
            return false;
        else
            return cockpitCur.isPadlock();
    }

    public Actor getViewPadlockEnemy()
    {
        if(!Actor.isValid(cockpitCur))
            return null;
        else
            return cockpitCur.getPadlockEnemy();
    }

    public void setViewInsideShow(boolean flag)
    {
        if(isViewOutside() || isViewInsideShow() == flag)
            return;
        if(!Actor.isValid(cockpitCur))
        {
            return;
        } else
        {
            cockpitCur.setNullShow(!flag);
            return;
        }
    }

    public void setEnableRenderingCockpit(boolean flag)
    {
        if(isViewOutside() || isEnableRenderingCockpit() == flag)
            return;
        if(!Actor.isValid(cockpitCur))
        {
            return;
        } else
        {
            cockpitCur.setEnableRendering(flag);
            return;
        }
    }

    private void endViewFly()
    {
        if(!bViewFly)
        {
            return;
        } else
        {
            hookViewFly.use(false);
            Engine.soundListener().setUseBaseSpeed(true);
            bViewFly = false;
            return;
        }
    }

    private void endViewEnemy()
    {
        if(!bViewEnemy)
        {
            return;
        } else
        {
            hookViewEnemy.stop();
            bViewEnemy = false;
            return;
        }
    }

    private void endView()
    {
        if(!isViewOutside())
            return;
        if(bViewFly || bViewEnemy)
        {
            return;
        } else
        {
            hookView.use(false);
            return;
        }
    }

    private void endViewInside()
    {
        if(isViewOutside())
        {
            return;
        } else
        {
            cockpitCur.focusLeave();
            return;
        }
    }

    public void setViewFly(Actor actor)
    {
        endView();
        endViewEnemy();
        endViewInside();
        Selector.resetGame();
        hookViewFly.use(true);
        bViewFly = true;
        camera3D.pos.setRel(new Point3d(), new Orient());
        camera3D.pos.changeBase(actor, hookViewFly, false);
        camera3D.pos.resetAsBase();
        Engine.soundListener().setUseBaseSpeed(false);
    }

    private void setViewSomebody(Actor actor)
    {
        endView();
        endViewFly();
        endViewInside();
        bViewEnemy = true;
        camera3D.pos.setRel(new Point3d(), new Orient());
        camera3D.pos.changeBase(actor, hookViewEnemy, false);
        camera3D.pos.resetAsBase();
        if(actor instanceof ActorViewPoint)
            ((ActorViewPoint)actor).setViewActor(hookViewEnemy.enemy());
    }

    public void setViewEnemy(Actor actor, boolean flag, boolean flag1)
    {
        Actor actor1 = Selector.look(flag, flag1, camera3D, actor.getArmy(), -1, actor, true);
        if(!hookViewEnemy.start(actor, actor1, flag1, true))
        {
            if(bViewEnemy)
                setView(actor);
            return;
        } else
        {
            setViewSomebody(actor);
            return;
        }
    }

    public void setViewFriend(Actor actor, boolean flag, boolean flag1)
    {
        Actor actor1 = Selector.look(flag, flag1, camera3D, -1, actor.getArmy(), actor, true);
        if(!hookViewEnemy.start(actor, actor1, flag1, false))
        {
            if(bViewEnemy)
                setView(actor);
            return;
        } else
        {
            setViewSomebody(actor);
            return;
        }
    }

    public void setViewPadlock(boolean flag, boolean flag1)
    {
        if(isViewOutside())
            return;
        if(!cockpitCur.existPadlock())
            return;
        Aircraft aircraft = World.getPlayerAircraft();
        Actor actor;
        if(World.cur().diffCur.No_Icons)
            actor = Selector.look(true, flag1, camera3D, -1, -1, aircraft, false);
        else
        if(flag)
            actor = Selector.look(true, flag1, camera3D, -1, aircraft.getArmy(), aircraft, false);
        else
            actor = Selector.look(true, flag1, camera3D, aircraft.getArmy(), -1, aircraft, false);
        if(actor == null || actor == aircraft)
            return;
        if(!cockpitCur.startPadlock(actor))
            return;
        else
            return;
    }

    public void setViewEndPadlock()
    {
        if(isViewOutside())
            return;
        if(!cockpitCur.existPadlock())
        {
            return;
        } else
        {
            cockpitCur.endPadlock();
            return;
        }
    }

    public void setViewNextPadlock(boolean flag)
    {
        if(isViewOutside())
            return;
        if(!cockpitCur.existPadlock())
            return;
        Actor actor = Selector.next(flag);
        if(actor == null)
            return;
        if(!cockpitCur.startPadlock(actor))
            return;
        else
            return;
    }

    public void setViewPadlockForward(boolean flag)
    {
        if(isViewPadlock())
            cockpitCur.setPadlockForward(flag);
    }

    public void setViewInside()
    {
        if(!isViewOutside() && !isViewPadlock())
            return;
        if(!Actor.isValid(cockpitCur))
            return;
        if(!cockpitCur.isEnableFocusing())
        {
            return;
        } else
        {
            endView();
            endViewFly();
            endViewEnemy();
            endViewInside();
            cockpitCur.focusEnter();
            return;
        }
    }

    public void setViewFlow30(Actor actor)
    {
        setView(actor, true);
        hookView.set(actor, 30F, -30F);
        camera3D.pos.resetAsBase();
    }

    public void setViewFlow10(Actor actor, boolean flag)
    {
        hookView.setFollow(flag);
        setView(actor, true);
        hookView.set(actor, 10F, -10F);
        camera3D.pos.resetAsBase();
    }

    public void setView(Actor actor)
    {
        setView(actor, false);
    }

    public void setView(Actor actor, boolean flag)
    {
        if(viewActor() != actor || flag || bViewFly || bViewEnemy)
        {
            endViewFly();
            endViewEnemy();
            endViewInside();
            Selector.resetGame();
            hookView.use(true);
            camera3D.pos.setRel(new Point3d(), new Orient());
            camera3D.pos.changeBase(actor, hookView, false);
            camera3D.pos.resetAsBase();
        }
    }

    public int cockpitCurIndx()
    {
        if(cockpits == null || cockpitCur == null)
            return -1;
        for(int i = 0; i < cockpits.length; i++)
            if(cockpitCur == cockpits[i])
                return i;

        return -1;
    }

    public void beginStep(int i)
    {
        if(!bUseStartLog)
            if(i >= 0)
                ConsoleGL0.exclusiveDrawStep(I18N.gui("main.loading") + " " + i + "%", i);
            else
                ConsoleGL0.exclusiveDrawStep(null, -1);
    }

    public boolean beginApp(String s, String s1, int i)
    {
        Config.cur.mainSection = s1;
        Engine.cur = new Engine();
        Config.typeProvider();
        GLContext glcontext = Config.cur.createGlContext(Config.cur.ini.get(Config.cur.mainSection, "title", "il2"));
        return beginApp(glcontext, i);
    }

    public boolean beginApp(GLContext glcontext, int i)
    {
        Config.typeGlStrings();
        Config.cur.typeContextSettings(glcontext);
        bDrawIfNotFocused = Config.cur.ini.get("window", "DrawIfNotFocused", bDrawIfNotFocused);
        bShowStartIntro = Config.cur.ini.get("game", "intro", bShowStartIntro);
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
        bUseStartLog = Config.cur.ini.get("Console", "UseStartLog", bUseStartLog);
        if(bUseStartLog)
            ConsoleGL0.exclusiveDraw(true);
        else
            ConsoleGL0.exclusiveDraw("gui/background0.mat");
        beginStep(5);
        CmdEnv.top().exec("CmdLoad com.maddox.rts.cmd.CmdLoad");
        CmdEnv.top().exec("load com.maddox.rts.cmd.CmdFile PARAM CURENV on");
        CmdSFS.bMountError = false;
        CmdEnv.top().exec("file .rc");
        if(CmdSFS.bMountError)
            return false;
        beginStep(10);
        try
        {
            Engine.setWorldAcoustics("Landscape");
        }
        catch(Exception exception)
        {
            System.out.println("World Acoustics NOT initialized: " + exception.getMessage());
            return false;
        }
        Engine.soundListener().initDraw();
        beginStep(15);
        Regiment.loadAll();
        preloadNetClasses();
        beginStep(20);
        preloadAirClasses();
        beginStep(25);
        preloadChiefClasses();
        beginStep(30);
        preloadStationaryClasses();
        preload();
        beginStep(35);
        camera3D = new NetCamera3D();
        camera3D.setName("camera");
        camera3D.set(FOVX, 1.2F, 48000F);
        render3D0 = new Render3D0(0, 1.0F);
        render3D0.setSaveAspect(Config.cur.windowSaveAspect);
        render3D0.setName("render3D0");
        render3D0.setCamera(camera3D);
        Engine.lightEnv().sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
        Vector3f vector3f = new Vector3f(0.0F, 1.0F, -1F);
        vector3f.normalize();
        Engine.lightEnv().sun().set(vector3f);
        _camera3D[0] = camera3D;
        _render3D0[0] = render3D0;
        for(int j = 1; j < 3; j++)
        {
            _camera3D[j] = new Camera3D();
            _camera3D[j].set(FOVX, 1.2F, 48000F);
            _camera3D[j].pos.setBase(camera3D, null, false);
            _render3D0[j] = new Render3D0(j, 1.0F - (float)j * 0.001F);
            _render3D0[j].setSaveAspect(true);
            _render3D0[j].setCamera(_camera3D[j]);
        }

        _camera3D[1].pos.setRel(new Orient(-FOVX, 0.0F, 0.0F));
        _camera3D[2].pos.setRel(new Orient(FOVX, 0.0F, 0.0F));
        render3D1 = new Render3D1(0, 0.9F);
        render3D1.setSaveAspect(Config.cur.windowSaveAspect);
        render3D1.setName("render3D1");
        render3D1.setCamera(camera3D);
        for(int k = 1; k < 3; k++)
        {
            _render3D1[k] = new Render3D1(k, 0.9F - (float)k * 0.001F);
            _render3D1[k].setSaveAspect(true);
            _render3D1[k].setCamera(_camera3D[k]);
        }

        camera3DR = new Camera3DR();
        camera3DR.setName("cameraR");
        camera3DR.set(FOVX, 1.2F, 48000F);
        camera3DR.pos.setBase(camera3D, new HookReflection(), false);
        render3D0R = new Render3D0R(1.1F);
        render3D0R.setSaveAspect(Config.cur.windowSaveAspect);
        render3D0R.setName("render3D0R");
        render3D0R.setCamera(camera3DR);
        Engine.soundListener().pos.setBase(camera3D, null, false);
        TextScr.font();
        camera2D = new CameraOrtho2D();
        camera2D.setName("camera2D");
        render2D = new Render2D(0, 0.95F);
        render2D.setSaveAspect(Config.cur.windowSaveAspect);
        render2D.setName("render2D");
        render2D.setCamera(camera2D);
        camera2D.set(0.0F, render2D.getViewPortWidth(), 0.0F, render2D.getViewPortHeight());
        camera2D.set(0.0F, 1.0F);
        render2D.setShow(true);
        _camera2D[0] = camera2D;
        _render2D[0] = render2D;
        for(int l = 1; l < 3; l++)
        {
            _camera2D[l] = new CameraOrtho2D();
            _render2D[l] = new Render2D(l, 0.95F - (float)l * 0.001F);
            _render2D[l].setSaveAspect(true);
            _render2D[l].setCamera(_camera2D[l]);
            _camera2D[l].set(0.0F, _render2D[l].getViewPortWidth(), 0.0F, _render2D[l].getViewPortHeight());
            _camera2D[l].set(0.0F, 1.0F);
        }

        camera3DMirror = new com.maddox.il2.objects.air.Cockpit.Camera3DMirror();
        camera3DMirror.setName("cameraMirror");
        camera3DMirror.set(FOVX, 1.2F, 48000F);
        camera3DMirror.pos.setBase(camera3D, Cockpit.getHookCamera3DMirror(false), false);
        render3D0Mirror = new Render3D0Mirror(1.8F);
        render3D0Mirror.setName("render3D0Mirror");
        render3D0Mirror.setCamera(camera3DMirror);
        render3D1Mirror = new Render3D1Mirror(1.78F);
        render3D1Mirror.setName("render3D1Mirror");
        render3D1Mirror.setCamera(camera3DMirror);
        camera2DMirror = new CameraOrtho2D();
        camera2DMirror.setName("camera2DMirror");
        render2DMirror = new Render2DMirror(1.79F);
        render2DMirror.setName("render2DMirror");
        render2DMirror.setCamera(camera2DMirror);
        camera2DMirror.set(0.0F, render2DMirror.getViewPortWidth(), 0.0F, render2DMirror.getViewPortHeight());
        camera2DMirror.set(0.0F, 1.0F);
        cameraCockpit = new Camera3D();
        cameraCockpit.setName("cameraCockpit");
        cameraCockpit.set(FOVX, 0.05F, 12.5F);
        renderCockpit = new RenderCockpit(0, 0.5F);
        renderCockpit.setSaveAspect(Config.cur.windowSaveAspect);
        renderCockpit.setName("renderCockpit");
        renderCockpit.setCamera(cameraCockpit);
        renderCockpit.setShow(false);
        _cameraCockpit[0] = cameraCockpit;
        _renderCockpit[0] = renderCockpit;
        for(int i1 = 1; i1 < 3; i1++)
        {
            _cameraCockpit[i1] = new Camera3D();
            _cameraCockpit[i1].set(FOVX, 0.05F, 12.5F);
            _cameraCockpit[i1].pos.setBase(cameraCockpit, null, false);
            _renderCockpit[i1] = new RenderCockpit(i1, 0.5F - (float)i1 * 0.001F);
            _renderCockpit[i1].setSaveAspect(true);
            _renderCockpit[i1].setCamera(_cameraCockpit[i1]);
        }

        _cameraCockpit[1].pos.setRel(new Orient(-FOVX, 0.0F, 0.0F));
        _cameraCockpit[2].pos.setRel(new Orient(FOVX, 0.0F, 0.0F));
        cameraCockpitMirror = new com.maddox.il2.objects.air.Cockpit.Camera3DMirror();
        cameraCockpitMirror.pos.setBase(cameraCockpit, Cockpit.getHookCamera3DMirror(true), false);
        cameraCockpitMirror.setName("cameraCockpitMirror");
        cameraCockpitMirror.set(FOVX, 0.05F, 12.5F);
        renderCockpitMirror = new RenderCockpitMirror(1.77F);
        renderCockpitMirror.setName("renderCockpitMirror");
        renderCockpitMirror.setCamera(cameraCockpitMirror);
        cameraHUD = new CameraOrtho2D();
        cameraHUD.setName("cameraHUD");
        renderHUD = new RenderHUD(0.3F);
        renderHUD.setName("renderHUD");
        renderHUD.setCamera(cameraHUD);
        cameraHUD.set(0.0F, renderHUD.getViewPortWidth(), 0.0F, renderHUD.getViewPortHeight());
        cameraHUD.set(-1000F, 1000F);
        renderHUD.setShow(true);
        LightEnvXY lightenvxy = new LightEnvXY();
        renderHUD.setLightEnv(lightenvxy);
        lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
        vector3f = new Vector3f(0.0F, 1.0F, -1F);
        vector3f.normalize();
        lightenvxy.sun().set(vector3f);
        hud = new HUD();
        renderHUD.contextResized();
        drawFarActorsInit();
        cameraMap2D = new CameraOrtho2D();
        cameraMap2D.setName("cameraMap2D");
        renderMap2D = new RenderMap2D(0.2F);
        renderMap2D.setName("renderMap2D");
        renderMap2D.setCamera(cameraMap2D);
        cameraMap2D.set(0.0F, renderMap2D.getViewPortWidth(), 0.0F, renderMap2D.getViewPortHeight());
        renderMap2D.setShow(false);
        beginStep(40);
        _sunFlareRender[0] = SunFlare.newRender(0, 0.19F, _camera3D[0]);
        _sunFlareRender[0].setName("renderSunFlare");
        _sunFlareRender[0].setSaveAspect(Config.cur.windowSaveAspect);
        _sunFlareRender[0].setShow(false);
        for(int j1 = 1; j1 < 3; j1++)
        {
            _sunFlareRender[j1] = SunFlare.newRender(j1, 0.19F, _camera3D[j1]);
            _sunFlareRender[j1].setSaveAspect(true);
            _sunFlareRender[j1].setShow(false);
        }

        lightsGlare = new LightsGlare(0, 0.17F);
        lightsGlare.setSaveAspect(Config.cur.windowSaveAspect);
        lightsGlare.setCamera(new CameraOrtho2D());
        lightsGlare.setShow(false);
        _lightsGlare[0] = lightsGlare;
        for(int k1 = 1; k1 < 3; k1++)
        {
            _lightsGlare[k1] = new LightsGlare(k1, 0.17F - (float)k1 * 0.001F);
            _lightsGlare[k1].setSaveAspect(true);
            _lightsGlare[k1].setCamera(new CameraOrtho2D());
        }

        sunGlare = new SunGlare(0, 0.15F);
        sunGlare.setSaveAspect(Config.cur.windowSaveAspect);
        sunGlare.setCamera(new CameraOrtho2D());
        sunGlare.setShow(false);
        _sunGlare[0] = sunGlare;
        for(int l1 = 1; l1 < 3; l1++)
        {
            _sunGlare[l1] = new SunGlare(l1, 0.15F - (float)l1 * 0.001F);
            _sunGlare[l1].setSaveAspect(true);
            _sunGlare[l1].setCamera(new CameraOrtho2D());
        }

        overLoad = new OverLoad(0, 0.1F);
        overLoad.setSaveAspect(Config.cur.windowSaveAspect);
        overLoad.setCamera(new CameraOrtho2D());
        overLoad.setShow(false);
        _overLoad[0] = overLoad;
        for(int i2 = 1; i2 < 3; i2++)
        {
            _overLoad[i2] = new OverLoad(i2, 0.1F - (float)i2 * 0.001F);
            _overLoad[i2].setSaveAspect(true);
            _overLoad[i2].setCamera(new CameraOrtho2D());
        }

        darkerNight = new DarkerNight(0, 0.7F);
        darkerNight.setSaveAspect(Config.cur.windowSaveAspect);
        darkerNight.setCamera(new CameraOrtho2D());
        darkerNight.setShow(true);
        _cinema[0] = new Cinema(0, 0.09F);
        _cinema[0].setSaveAspect(Config.cur.windowSaveAspect);
        _cinema[0].setCamera(new CameraOrtho2D());
        _cinema[0].setShow(false);
        for(int j2 = 1; j2 < 3; j2++)
        {
            _cinema[j2] = new Cinema(j2, 0.09F - (float)j2 * 0.001F);
            _cinema[j2].setSaveAspect(true);
            _cinema[j2].setCamera(new CameraOrtho2D());
            _cinema[j2].setShow(false);
        }

        timeSkip = new TimeSkip(-1.1F);
        HotKeyEnv.fromIni("hotkeys", Config.cur.ini, Config.cur.ini.get(Config.cur.mainSection, "hotkeys", "hotkeys"));
        FreeFly.init("FreeFly");
        FreeFlyXYZ.init("FreeFlyXYZ");
        hookView = new HookView("HookView");
        hookView.setCamera(camera3D);
        hookViewFly = new HookViewFly("HookViewFly");
        hookViewEnemy = new HookViewEnemy();
        hookViewEnemy.setCamera(camera3D);
        HookPilot.New();
        HookPilot.current.setTarget(cameraCockpit);
        HookPilot.current.setTarget2(camera3D);
        HookKeys.New();
        aircraftHotKeys = new AircraftHotKeys();
        beginStep(45);
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
        ordersTree = new OrdersTree(true);
        beginStep(50);
        if(bUseGUI)
        {
            guiManager = GUI.create("gui");
            keyRecord = new KeyRecord();
            keyRecord.addExcludePrevCmd(278);
            Keyboard.adapter().setKeyEnable(27);
        }
        beginStep(90);
        initHotKeys();
        Voice.setEnableVoices(!Config.cur.ini.get("game", "NoChatter", false));
        beginStep(95);
        viewSet_Load();
        DeviceLink.start();
        onBeginApp();
        Time.setPause(false);
        RTSConf.cur.loopMsgs();
        Time.setPause(true);
        new MsgAction(64, 1.0D + Math.random() * 10D) {

            public void doAction()
            {
                try
                {
                    Class.forName("fbapi");
                    Main.doGameExit();
                }
                catch(Throwable throwable) { }
            }

        }
;
        bDrawClouds = true;
        TextScr.setColor(new Color4f(1.0F, 0.0F, 0.0F, 1.0F));
        RTSConf.cur.console.getEnv().exec("file rcu");
        beginStep(-1);
        createConsoleServer();
        return true;
    }

    public void setSaveAspect(boolean flag)
    {
        if(Config.cur.windowSaveAspect == flag)
        {
            return;
        } else
        {
            render3D0.setSaveAspect(flag);
            render3D1.setSaveAspect(flag);
            render2D.setSaveAspect(flag);
            renderCockpit.setSaveAspect(flag);
            _sunFlareRender[0].setSaveAspect(flag);
            lightsGlare.setSaveAspect(flag);
            sunGlare.setSaveAspect(flag);
            overLoad.setSaveAspect(flag);
            darkerNight.setSaveAspect(flag);
            _cinema[0].setSaveAspect(flag);
            Config.cur.windowSaveAspect = flag;
            return;
        }
    }

    public static void menuMusicPlay()
    {
        menuMusicPlay(_sLastMusic);
    }

    public static void menuMusicPlay(String s)
    {
        s = Regiment.getCountryFromBranch(s);
        _sLastMusic = s;
        CmdEnv.top().exec("music FILE music/menu/" + _sLastMusic);
    }

    public void viewSet_Load()
    {
        int i = Config.cur.ini.get("game", "viewSet", 0);
        viewSet_Set(i);
        iconTypes = Config.cur.ini.get("game", "iconTypes", 3, 0, 3);
    }

    public void viewSet_Save()
    {
        if(aircraftHotKeys != null)
        {
            Config.cur.ini.set("game", "viewSet", viewSet_Get());
            Config.cur.ini.set("game", "iconTypes", iconTypes());
        }
    }

    protected int viewSet_Get()
    {
        int i = 0;
        if(HookKeys.current != null && HookKeys.current.isPanView())
            i |= 1;
        if(HookPilot.current != null && HookPilot.current.isAim())
            i |= 2;
        i |= (viewMirror & 3) << 2;
        if(!aircraftHotKeys.isAutoAutopilot())
            i |= 0x10;
        i |= (HUD.drawSpeed() & 3) << 5;
        return i;
    }

    private void viewSet_Set(int i)
    {
        HookKeys.current.setMode((i & 1) != 0);
        HookPilot.current.doAim((i & 2) != 0);
        viewMirror = i >> 2 & 3;
        aircraftHotKeys.setAutoAutopilot((i & 0x10) == 0);
        HUD.setDrawSpeed(i >> 5 & 3);
    }

    public boolean isViewMirror()
    {
        return viewMirror > 0;
    }

    public int iconTypes()
    {
        return iconTypes;
    }

    protected void changeIconTypes()
    {
        iconTypes = (iconTypes + 1) % 4;
    }

    public void disableAllHotKeyCmdEnv()
    {
        List list = HotKeyCmdEnv.allEnv();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            HotKeyCmdEnv hotkeycmdenv = (HotKeyCmdEnv)list.get(j);
            hotkeycmdenv.enable(false);
        }

        HotKeyCmdEnv.enable("hotkeys", true);
        HotKeyCmdEnv.enable("$$$misc", true);
    }

    public void enableHotKeyCmdEnvs(String as[])
    {
        for(int i = 0; i < as.length; i++)
            HotKeyCmdEnv.enable(as[i], true);

    }

    public void enableOnlyHotKeyCmdEnvs(String as[])
    {
        disableAllHotKeyCmdEnv();
        enableHotKeyCmdEnvs(as);
    }

    public void enableOnlyHotKeyCmdEnv(String s)
    {
        disableAllHotKeyCmdEnv();
        HotKeyCmdEnv.enable(s, true);
    }

    public void enableGameHotKeyCmdEnvs()
    {
        enableHotKeyCmdEnvs(gameHotKeyCmdEnvs);
    }

    public void enableOnlyGameHotKeyCmdEnvs()
    {
        enableOnlyHotKeyCmdEnvs(gameHotKeyCmdEnvs);
    }

    public void enableCockpitHotKeys()
    {
        if(isDemoPlaying())
            return;
        if(!Actor.isValid(cockpitCur))
            return;
        String as[] = cockpitCur.getHotKeyEnvs();
        if(as == null)
            return;
        for(int i = 0; i < as.length; i++)
            if(as[i] != null)
                HotKeyEnv.enable(as[i], true);

    }

    public void disableCockpitHotKeys()
    {
        if(isDemoPlaying())
            return;
        if(!Actor.isValid(cockpitCur))
            return;
        String as[] = cockpitCur.getHotKeyEnvs();
        if(as == null)
            return;
        for(int i = 0; i < as.length; i++)
            if(as[i] != null)
                HotKeyEnv.enable(as[i], false);

    }

    private void _disableCockpitsHotKeys()
    {
        HotKeyEnv.enable("pilot", false);
        HotKeyEnv.enable("move", false);
        HotKeyEnv.enable("gunner", false);
    }

    public void disableCockpitsHotKeys()
    {
        if(isDemoPlaying())
        {
            return;
        } else
        {
            _disableCockpitsHotKeys();
            return;
        }
    }

    public void resetGameClear()
    {
        SearchlightGeneric.resetGame();
        disableCockpitsHotKeys();
        camera3D.pos.changeBase(null, null, false);
        camera3D.pos.setAbs(new Point3d(), new Orient());
        FreeFly.adapter().resetGame();
        if(HookPilot.current != null)
        {
            HookPilot.current.use(false);
            HookPilot.current.resetGame();
        }
        HookGunner.resetGame();
        if(HookKeys.current != null)
            HookKeys.current.resetGame();
        hookViewFly.reset();
        hookViewEnemy.reset();
        hookView.reset();
        hookView.resetGame();
        hookViewEnemy.resetGame();
        overLoad.setShow(false);
        for(int i = 0; i < 3; i++)
        {
            _lightsGlare[i].setShow(false);
            _lightsGlare[i].resetGame();
            _sunGlare[i].setShow(false);
            _sunGlare[i].resetGame();
        }

        Selector.resetGame();
        hud.resetGame();
        aircraftHotKeys.resetGame();
        bViewFly = false;
        bViewEnemy = false;
        ordersTree.resetGameClear();
        if(clouds != null)
        {
            clouds.destroy();
            clouds = null;
        }
        if(zip != null)
        {
            zip.destroy();
            zip = null;
        }
        sunFlareDestroy();
        if(Actor.isValid(spritesFog))
            spritesFog.destroy();
        spritesFog = null;
        if(land2D != null)
        {
            if(!land2D.isDestroyed())
                land2D.destroy();
            land2D = null;
        }
        if(land2DText != null)
        {
            if(!land2DText.isDestroyed())
                land2DText.destroy();
            land2DText = null;
        }
        if(cockpits != null)
        {
            for(int j = 0; j < cockpits.length; j++)
            {
                if(Actor.isValid(cockpits[j]))
                    cockpits[j].destroy();
                cockpits[j] = null;
            }

            cockpits = null;
        }
        cockpitCur = null;
        super.resetGameClear();
    }

    public void resetGameCreate()
    {
        super.resetGameCreate();
        Engine.soundListener().pos.setBase(camera3D, null, false);
        Engine.soundListener().setUseBaseSpeed(true);
    }

    public void resetUserClear()
    {
        World.cur().resetUser();
        aircraftHotKeys.resetUser();
        if(cockpits != null)
        {
            for(int i = 0; i < cockpits.length; i++)
            {
                if(Actor.isValid(cockpits[i]))
                    cockpits[i].destroy();
                cockpits[i] = null;
            }

            cockpits = null;
        }
        cockpitCur = null;
        super.resetUserClear();
    }

    public void sunFlareCreate()
    {
        sunFlareDestroy();
        for(int i = 0; i < 3; i++)
            _sunFlare[i] = new SunFlare(_sunFlareRender[i]);

    }

    public void sunFlareDestroy()
    {
        for(int i = 0; i < 3; i++)
        {
            if(Actor.isValid(_sunFlare[i]))
                _sunFlare[i].destroy();
            _sunFlare[i] = null;
        }

    }

    public void sunFlareShow(boolean flag)
    {
        for(int i = 0; i < 3; i++)
            _sunFlareRender[i].setShow(flag);

    }

    public KeyRecordCallback playRecordedMissionCallback()
    {
        return playRecordedMissionCallback;
    }

    public InOutStreams playRecordedStreams()
    {
        return playRecordedStreams;
    }

    NetChannelInStream playRecordedNetChannelIn()
    {
        return playRecordedNetChannelIn;
    }

    public GameTrack gameTrackRecord()
    {
        return gameTrackRecord;
    }

    public void setGameTrackRecord(GameTrack gametrack)
    {
        gameTrackRecord = gametrack;
    }

    public GameTrack gameTrackPlay()
    {
        return gameTrackPlay;
    }

    public void setGameTrackPlay(GameTrack gametrack)
    {
        gameTrackPlay = gametrack;
    }

    public void clearGameTrack(GameTrack gametrack)
    {
        if(gametrack == gameTrackRecord)
            gameTrackRecord = null;
        if(gametrack == gameTrackPlay)
            gameTrackPlay = null;
    }

    public String playRecordedMission(String s)
    {
        playBatchCurRecord = -1;
        playEndBatch = true;
        playRecordedStreams = null;
        return playRecordedMission(s, true);
    }

    public String playRecordedMission(String s, boolean flag)
    {
        playRecordedFile = s;
        if(playRecordedMissionCallback == null)
            playRecordedMissionCallback = new KeyRecordCallback() {

                public void playRecordedEnded()
                {
                    if(this != playRecordedMissionCallback)
                        return;
                    GameState gamestate = Main.state();
                    if(gamestate instanceof GUIRecordPlay)
                    {
                        GUIRecordPlay guirecordplay = (GUIRecordPlay)gamestate;
                        guirecordplay.doReplayMission(playRecordedFile, playEndBatch);
                    } else
                    if(gamestate instanceof GUITrainingPlay)
                    {
                        GUITrainingPlay guitrainingplay = (GUITrainingPlay)gamestate;
                        guitrainingplay.doQuitMission();
                        guitrainingplay.doExit();
                    } else
                    if(gamestate instanceof GUIBWDemoPlay)
                    {
                        GUIBWDemoPlay guibwdemoplay = (GUIBWDemoPlay)gamestate;
                        guibwdemoplay.doQuitMission();
                    }
                }

                public void doFirstHotCmd(boolean flag1)
                {
                    if(playRecordedStreams != null)
                    {
                        AircraftHotKeys _tmp = aircraftHotKeys;
                        AircraftHotKeys.bFirstHotCmd = flag1;
                        loadRecordedStates1(flag1);
                        if(!flag1)
                            loadRecordedStates2();
                    }
                }

            };
        if(playRecordedStreams != null)
        {
            try
            {
                playRecordedStreams.close();
            }
            catch(Exception exception) { }
            playRecordedStreams = null;
            NetMissionTrack.stopPlaying();
        }
        if(playRecordedNetChannelIn != null)
            playRecordedNetChannelIn.destroy();
        playRecordedNetChannelIn = null;
        if(InOutStreams.isExistAndValid(new File(s)))
            return playNetRecordedMission(s, flag);
        String s1 = s;
        SectFile sectfile = new SectFile(s, 0, false);
        int i = sectfile.sectionIndex("batch");
        if(i >= 0)
        {
            int j = sectfile.vars(i);
            if(j <= 0)
                return "Track file '" + s + "' is empty";
            playEndBatch = playBatchCurRecord != -1 && playBatchCurRecord == j - 2;
            if(j == 1)
                playEndBatch = true;
            playBatchCurRecord++;
            if(playBatchCurRecord >= j)
                playBatchCurRecord = 0;
            s1 = "Records/" + sectfile.line(i, playBatchCurRecord);
            if(InOutStreams.isExistAndValid(new File(s1)))
                return playNetRecordedMission(s1, flag);
            sectfile = new SectFile(s1, 0, false);
        } else
        {
            playEndBatch = true;
        }
        i = sectfile.sectionIndex("$$$record");
        if(i < 0)
            return "Track file '" + s1 + "' not included section [$$$record]";
        if(sectfile.vars(i) <= 10)
            return "Track file '" + s1 + "' is empty";
        int k = Integer.parseInt(sectfile.var(i, 0));
        if(k != 130)
            return "Track file '" + s1 + "' version is not supported";
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
        if(l1 != l2)
            return "Track file '" + s1 + "' is changed";
        World.cur().diffCur.set(l);
        World.cur().diffCur.Cockpit_Always_On = false;
        World.cur().diffCur.No_Outside_Views = false;
        World.cur().diffCur.No_Padlock = false;
        World.cur().userCoverMashineGun = f;
        World.cur().userCoverCannon = f1;
        World.cur().userCoverRocket = f2;
        World.cur().userRocketDelay = f3;
        World.cur().userBombDelay = f4;
        viewSet_Set(i1);
        iconTypes = j1;
        if(Main.cur().netServerParams == null)
        {
            new NetServerParams();
            Main.cur().netServerParams.setMode(2);
            new NetLocalControl();
        }
        try
        {
            Mission.loadFromSect(sectfile, true);
        }
        catch(Exception exception1)
        {
            System.out.println(exception1.getMessage());
            exception1.printStackTrace();
            return "Track file '" + s1 + "' load failed: " + exception1.getMessage();
        }
        playRecordedSect = sectfile;
        playRecorderIndx = i;
        playRecordedPlayFile = s1;
        if(flag)
            doRecordedPlayFirst();
        return null;
    }

    private String playNetRecordedMission(String paramString, boolean paramBoolean)
    {
    try {
      this.playRecordedStreams = new InOutStreams();
      this.playRecordedStreams.open(new File(paramString), false);

      InputStream localInputStream1 = this.playRecordedStreams.openStream("version");
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream1));
      int i = Integer.parseInt(localBufferedReader.readLine());
      int j = i;
      if (i >= 103)
        j = Integer.parseInt(localBufferedReader.readLine());
      localBufferedReader.close();
      if ((i != 100) && (i != 101) && (i != 102) && (i != 103)) {
        try { this.playRecordedStreams.close(); } catch (Exception localException3) {
        }this.playRecordedStreams = null;
        return "Track file '" + paramString + "' version is not supported";
      }

      loadRecordedStates0();
      InputStream localInputStream2 = this.playRecordedStreams.openStream("traffic");
      if (localInputStream2 == null)
        throw new Exception("Stream 'traffic' not found.");
      this.playRecordedNetChannelIn = new NetChannelInStream(localInputStream2, 1);
      RTSConf.cur.netEnv.addChannel(this.playRecordedNetChannelIn);
      this.playRecordedNetChannelIn.setStateInit(0);
      this.playRecordedNetChannelIn.userState = 1;
      NetMissionTrack.startPlaying(this.playRecordedStreams, i, j);
      if (paramBoolean) doRecordedPlayFirst(); 
    }
    catch (Exception localException1) {
      localException1.printStackTrace();
      if (this.playRecordedStreams != null) try {
          this.playRecordedStreams.close(); } catch (Exception localException2) {
        } this.playRecordedStreams = null;
      return "Track file '" + paramString + "' load failed: " + localException1.getMessage();
    }
    return null;
    }

    private void doRecordedPlayFirst()
    {
        _disableCockpitsHotKeys();
        HotKeyEnv.enable("misc", false);
        HotKeyEnv.enable("orders", false);
        HotKeyEnv.enable("timeCompression", false);
        HotKeyEnv.enable("aircraftView", false);
        HotKeyEnv.enable("HookView", false);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
    }

    public String startPlayRecordedMission()
    {
        if(playRecordedStreams != null)
            keyRecord.startPlay(playRecordedMissionCallback);
        else
        if(!keyRecord.startPlay(playRecordedSect, playRecorderIndx, 10, playRecordedMissionCallback))
            return "Track file '" + playRecordedPlayFile + "' load failed";
        return null;
    }

    public void stopPlayRecordedMission()
    {
        playRecordedSect = null;
        if(keyRecord.isPlaying())
            keyRecord.stopPlay();
        if(playRecordedStreams != null)
        {
            try
            {
                playRecordedStreams.close();
            }
            catch(Exception exception) { }
            playRecordedStreams = null;
            NetMissionTrack.stopPlaying();
        }
        if(playRecordedNetChannelIn != null)
            playRecordedNetChannelIn.destroy();
        playRecordedNetChannelIn = null;
        _disableCockpitsHotKeys();
        HotKeyEnv.enable("misc", true);
        HotKeyEnv.enable("orders", true);
        HotKeyEnv.enable("timeCompression", true);
        HotKeyEnv.enable("aircraftView", true);
        HotKeyEnv.enable("HookView", true);
        HotKeyEnv.enable("PanView", true);
        HotKeyEnv.enable("SnapView", true);
    }

    public void flyRecordedMission()
    {
        if(keyRecord.isPlaying())
        {
            keyRecord.stopPlay();
            playRecordedMissionCallback = null;
            if(Actor.isValid(cockpitCur))
                HotKeyCmd.exec("misc", "cockpitEnter" + cockpitCurIndx());
            enableCockpitHotKeys();
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

  public boolean saveRecordedMission(String paramString) {
    if (this.mission == null) return false;
    if (this.mission.isDestroyed()) return false;
    if (!this.keyRecord.isContainRecorded()) return false; try
    {
      SectFile localSectFile = this.mission.sectFile();
      int i = localSectFile.sectionIndex("$$$record");
      if (i >= 0)
        localSectFile.sectionClear(i);
      else {
        i = localSectFile.sectionAdd("$$$record");
      }
      localSectFile.lineAdd(i, "130", "");
      long l = Finger.incLong(this.mission.finger(), 130);
      int j = World.cur().diffCur.get();
      localSectFile.lineAdd(i, "" + j, "");
      l = Finger.incLong(this.mission.finger(), j);
      localSectFile.lineAdd(i, "" + World.cur().userCoverMashineGun, "");
      l = Finger.incLong(l, World.cur().userCoverMashineGun);
      localSectFile.lineAdd(i, "" + World.cur().userCoverCannon, "");
      l = Finger.incLong(l, World.cur().userCoverCannon);
      localSectFile.lineAdd(i, "" + World.cur().userCoverRocket, "");
      l = Finger.incLong(l, World.cur().userCoverRocket);
      localSectFile.lineAdd(i, "" + World.cur().userRocketDelay, "");
      l = Finger.incLong(l, World.cur().userRocketDelay);
      localSectFile.lineAdd(i, "" + World.cur().userBombDelay, "");
      l = Finger.incLong(l, World.cur().userBombDelay);
      localSectFile.lineAdd(i, "" + Mission.viewSet, "");
      l = Finger.incLong(l, Mission.viewSet);
      localSectFile.lineAdd(i, "" + Mission.iconTypes, "");
      l = Finger.incLong(l, Mission.iconTypes);

      localSectFile.lineAdd(i, "" + l, "");
      this.keyRecord.saveRecorded(localSectFile, i);
      return localSectFile.saveFile(paramString);
    } catch (Exception localException) {
    }
    return false;
  }

  public boolean saveRecordedStates0(InOutStreams paramInOutStreams)
  {
    try
    {
      PrintWriter localPrintWriter = new PrintWriter(paramInOutStreams.createStream("states0"));
      localPrintWriter.println(World.cur().diffCur.get());
      localPrintWriter.println(World.cur().userCoverMashineGun);
      localPrintWriter.println(World.cur().userCoverCannon);
      localPrintWriter.println(World.cur().userCoverRocket);
      localPrintWriter.println(World.cur().userRocketDelay);
      localPrintWriter.println(World.cur().userBombDelay);
      localPrintWriter.println(viewSet_Get());
      localPrintWriter.println(this.iconTypes);
      localPrintWriter.println(isViewOutside() ? "0" : "1");
      localPrintWriter.println(FOVX);
      localPrintWriter.flush(); localPrintWriter.close();
      return true;
    } catch (Exception localException) {
      System.out.println(localException.getMessage());
      localException.printStackTrace();
    }return false;
  }

  public boolean saveRecordedStates1(InOutStreams paramInOutStreams) {
    try {
      PrintWriter localPrintWriter = new PrintWriter(paramInOutStreams.createStream("states1"));
      HookView.cur().saveRecordedStates(localPrintWriter);
      HookPilot.cur().saveRecordedStates(localPrintWriter);
      HookGunner.saveRecordedStates(localPrintWriter);
      localPrintWriter.flush(); localPrintWriter.close();
      return true;
    } catch (Exception localException) {
      System.out.println(localException.getMessage());
      localException.printStackTrace();
    }return false;
  }

  public boolean saveRecordedStates2(InOutStreams paramInOutStreams) {
    try {
      PrintWriter localPrintWriter = new PrintWriter(paramInOutStreams.createStream("states2"));
      localPrintWriter.println(FOVX);
      int i = 0;
      if (this.hud.bDrawDashBoard) i |= 1;
      if (isViewInsideShow()) i |= 2;
      if ((Actor.isValid(this.cockpitCur)) && (this.cockpitCur.isToggleAim()))
        i |= 4;
      FlightModel localFlightModel = World.getPlayerFM();
      if ((localFlightModel != null) && (localFlightModel.AS.bShowSmokesOn))
        i |= 8;
      if (isEnableRenderingCockpit()) i |= 16;
      if ((Actor.isValid(this.cockpitCur)) && (this.cockpitCur.isToggleUp()))
        i |= 32;
      if ((Actor.isValid(this.cockpitCur)) && (this.cockpitCur.isToggleDim()))
        i |= 64;
      if ((Actor.isValid(this.cockpitCur)) && (this.cockpitCur.isToggleLight()))
        i |= 128;
      if ((Actor.isValid(this.cockpitCur)) && (!this.cockpitCur.isEnableRenderingBall()))
        i |= 256;
      localPrintWriter.println(i);
      localPrintWriter.flush(); localPrintWriter.close();
      return true;
    } catch (Exception localException) {
      System.out.println(localException.getMessage());
      localException.printStackTrace();
    }return false;
  }

    public void loadRecordedStates0()
    {
        try
        {
            InputStream inputstream = playRecordedStreams.openStream("states0");
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
            viewSet_Set(Integer.parseInt(bufferedreader.readLine()));
            iconTypes = Integer.parseInt(bufferedreader.readLine());
            bLoadRecordedStates1Before = Integer.parseInt(bufferedreader.readLine()) == 1;
            float f = Float.parseFloat(bufferedreader.readLine());
            if(f != FOVX)
                CmdEnv.top().exec("fov " + f);
            inputstream.close();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void loadRecordedStates1(boolean flag)
    {
        if(flag == bLoadRecordedStates1Before)
            try
            {
                InputStream inputstream = playRecordedStreams.openStream("states1");
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
                HookView.cur().loadRecordedStates(bufferedreader);
                HookPilot.cur().loadRecordedStates(bufferedreader);
                HookGunner.loadRecordedStates(bufferedreader);
                inputstream.close();
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
    }

    public void loadRecordedStates2()
    {
        try
        {
            InputStream inputstream = playRecordedStreams.openStream("states2");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            float f = Float.parseFloat(bufferedreader.readLine());
            if(f != FOVX)
                CmdEnv.top().exec("fov " + f);
            int i = Integer.parseInt(bufferedreader.readLine());
            hud.bDrawDashBoard = (i & 1) != 0;
            setViewInsideShow((i & 2) != 0);
            if(Actor.isValid(cockpitCur))
                cockpitCur.doToggleAim((i & 4) != 0);
            FlightModel flightmodel = World.getPlayerFM();
            if(flightmodel != null)
                flightmodel.AS.setAirShowState((i & 8) != 0);
            if(Actor.isValid(cockpitCur))
            {
                setEnableRenderingCockpit((i & 0x10) != 0);
                cockpitCur.doToggleUp((i & 0x20) != 0);
                if((i & 0x40) != 0 && !cockpitCur.isToggleDim())
                    cockpitCur.doToggleDim();
                if((i & 0x80) != 0 && !cockpitCur.isToggleLight())
                    cockpitCur.doToggleLight();
                cockpitCur.setEnableRenderingBall((i & 0x100) == 0);
            }
            inputstream.close();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void setRenderIndx(int i)
    {
        iRenderIndx = i;
    }

    public int getRenderIndx()
    {
        return iRenderIndx;
    }

    private void transform_point(double ad[], double ad1[], double ad2[])
    {
        ad[0] = ad1[0] * ad2[0] + ad1[4] * ad2[1] + ad1[8] * ad2[2] + ad1[12] * ad2[3];
        ad[1] = ad1[1] * ad2[0] + ad1[5] * ad2[1] + ad1[9] * ad2[2] + ad1[13] * ad2[3];
        ad[2] = ad1[2] * ad2[0] + ad1[6] * ad2[1] + ad1[10] * ad2[2] + ad1[14] * ad2[3];
        ad[3] = ad1[3] * ad2[0] + ad1[7] * ad2[1] + ad1[11] * ad2[2] + ad1[15] * ad2[3];
    }

    public boolean project2d(double d, double d1, double d2, Point3d point3d)
    {
        _dIn[0] = d;
        _dIn[1] = d1;
        _dIn[2] = d2;
        _dIn[3] = 1.0D;
        if(bRenderMirror)
        {
            transform_point(_dOut, _modelMatrix3DMirror, _dIn);
            transform_point(_dIn, _projMatrix3DMirror, _dOut);
        } else
        {
            transform_point(_dOut, _modelMatrix3D[iRenderIndx], _dIn);
            transform_point(_dIn, _projMatrix3D[iRenderIndx], _dOut);
        }
        if(_dIn[3] == 0.0D)
        {
            System.out.println("BAD glu.Project: " + d + " " + d1 + " " + d2);
            return false;
        }
        _dIn[0] /= _dIn[3];
        _dIn[1] /= _dIn[3];
        _dIn[2] /= _dIn[3];
        if(bRenderMirror)
        {
            point3d.x = (double)_viewportMirror[0] + ((1.0D + _dIn[0]) * (double)_viewportMirror[2]) / 2D;
            point3d.y = (double)_viewportMirror[1] + ((1.0D + _dIn[1]) * (double)_viewportMirror[3]) / 2D;
        } else
        {
            point3d.x = (double)_viewport[iRenderIndx][0] + ((1.0D + _dIn[0]) * (double)_viewport[iRenderIndx][2]) / 2D;
            point3d.y = (double)_viewport[iRenderIndx][1] + ((1.0D + _dIn[1]) * (double)_viewport[iRenderIndx][3]) / 2D;
        }
        point3d.z = (1.0D + _dIn[2]) / 2D;
        return true;
    }

    public boolean project2d_cam(double d, double d1, double d2, Point3d point3d)
    {
        if(!project2d(d, d1, d2, point3d))
            return false;
        if(bRenderMirror)
        {
            point3d.x -= _viewportMirror[0];
            point3d.y -= _viewportMirror[1];
        } else
        {
            point3d.x -= _viewport[iRenderIndx][0];
            point3d.y -= _viewport[iRenderIndx][1];
        }
        return true;
    }

    public boolean project2d_norm(double d, double d1, double d2, Point3d point3d)
    {
        _dIn[0] = d;
        _dIn[1] = d1;
        _dIn[2] = d2;
        _dIn[3] = 1.0D;
        if(bRenderMirror)
        {
            transform_point(_dOut, _modelMatrix3DMirror, _dIn);
            transform_point(_dIn, _projMatrix3DMirror, _dOut);
        } else
        {
            transform_point(_dOut, _modelMatrix3D[iRenderIndx], _dIn);
            transform_point(_dIn, _projMatrix3D[iRenderIndx], _dOut);
        }
        if(_dIn[3] == 0.0D)
        {
            System.out.println("BAD glu.Project2: " + d + " " + d1 + " " + d2);
            return false;
        } else
        {
            double d3 = 1.0D / _dIn[3];
            point3d.x = _dIn[0] * d3;
            point3d.y = _dIn[1] * d3;
            point3d.z = _dIn[2] * d3;
            return true;
        }
    }

    public boolean project2d(Point3d point3d, Point3d point3d1)
    {
        return project2d(point3d.x, point3d.y, point3d.z, point3d1);
    }

    public boolean project2d_cam(Point3d point3d, Point3d point3d1)
    {
        return project2d_cam(point3d.x, point3d.y, point3d.z, point3d1);
    }

    private void shadowPairsClear()
    {
        shadowPairsList1.clear();
        shadowPairsMap1.clear();
        shadowPairsList2.clear();
        shadowPairsCur1 = null;
    }

    private void shadowPairsAdd(ArrayList arraylist)
    {
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            Object obj = arraylist.get(j);
            if(!(obj instanceof BigshipGeneric) || (obj instanceof TestRunway))
                continue;
            BigshipGeneric bigshipgeneric = (BigshipGeneric)obj;
            if(Actor.isValid(bigshipgeneric.getAirport()) && !shadowPairsMap1.containsKey(bigshipgeneric))
            {
                shadowPairsList1.add(bigshipgeneric);
                shadowPairsMap1.put(bigshipgeneric, null);
            }
        }

    }

    private void shadowPairsRender()
    {
        int i = shadowPairsList1.size();
        if(i == 0)
            return;
        for(int j = 0; j < i; j++)
        {
            shadowPairsCur1 = (BigshipGeneric)shadowPairsList1.get(j);
            Point3d point3d = shadowPairsCur1.pos.getAbsPoint();
            double d = point3d.x - shadowPairsR;
            double d1 = point3d.y - shadowPairsR;
            double d2 = point3d.x + shadowPairsR;
            double d3 = point3d.y + shadowPairsR;
            Engine.drawEnv().getFiltered((AbstractCollection)null, d, d1, d2, d3, 14, shadowPairsFilter);
        }

        if(shadowPairsList2.size() == 0)
        {
            return;
        } else
        {
            HierMesh.renderShadowPairs(shadowPairsList2);
            return;
        }
    }

    private void doPreRender3D(Render render)
    {
        render.useClearColor(!bDrawLand || (RenderContext.texGetFlags() & 0x20) != 0);
        render.getCamera().pos.getRender(__p, __o);
        if(!bRenderMirror && iRenderIndx == 0)
        {
            SearchlightGeneric.lightPlanesBySearchlights();
            Actor actor = render.getCamera().pos.base();
            if(Actor.isValid(actor))
            {
                actor.getSpeed(__v);
                Camera.SetTargetSpeed((float)__v.x, (float)__v.y, (float)__v.z);
            } else
            {
                Camera.SetTargetSpeed(0.0F, 0.0F, 0.0F);
            }
        }
        Render.enableFog(bEnableFog);
        if(bDrawClouds && clouds != null)
            clouds.preRender();
        if(bDrawLand)
            Engine.land().preRender((float)__p.z, false);
        darkerNight.preRender();
        DrwArray drwarray = bRenderMirror ? drwMirror : drwMaster[iRenderIndx];
        World _tmp = Engine.cur.world;
        Engine.drawEnv().preRender(__p.x, __p.y, __p.z, World.MaxVisualDistance, 4, drwarray.drwSolid, drwarray.drwTransp, drwarray.drwShadow, true);
        World _tmp1 = Engine.cur.world;
        Engine.drawEnv().preRender(__p.x, __p.y, __p.z, World.MaxLongVisualDistance, 8, drwarray.drwSolid, drwarray.drwTransp, drwarray.drwShadow, false);
        if(!bRenderMirror)
        {
            shadowPairsAdd(drwarray.drwSolid);
            shadowPairsAdd(drwarray.drwTransp);
        }
        if(!bRenderMirror || viewMirror > 1)
        {
            World _tmp2 = Engine.cur.world;
            Engine.drawEnv().preRender(__p.x, __p.y, __p.z, World.MaxStaticVisualDistance, 2, drwarray.drwSolid, drwarray.drwTransp, drwarray.drwShadow, false);
            World _tmp3 = Engine.cur.world;
            Engine.drawEnv().preRender(__p.x, __p.y, __p.z, World.MaxPlateVisualDistance, 1, drwarray.drwSolidPlate, drwarray.drwTranspPlate, drwarray.drwShadowPlate, true);
        }
        BulletGeneric.preRenderAll();
        if(bEnableFog)
            Render.enableFog(false);
    }

    private void doRender3D0(Render render)
    {
        boolean flag = false;
        Render.enableFog(bEnableFog);
        if(bDrawLand)
        {
            Engine.lightEnv().prepareForRender(camera3D.pos.getAbsPoint(), 8000F);
            flag = Engine.land().render0(bRenderMirror) != 2;
            LightPoint.clearRender();
        }
        if(flag && bEnableFog)
            Render.enableFog(false);
        DrwArray drwarray = bRenderMirror ? drwMirror : drwMaster[iRenderIndx];
        plateToRenderArray(drwarray.drwSolidPlate, drwarray.drwSolid);
        plateToRenderArray(drwarray.drwTranspPlate, drwarray.drwTransp);
        plateToRenderArray(drwarray.drwShadowPlate, drwarray.drwShadow);
        MeshShared.renderArray(true);
        render.drawShadow(drwarray.drwShadow);
        if(flag && bEnableFog)
            Render.enableFog(true);
        if(bDrawLand)
            Engine.land().render1(bRenderMirror);
        int i = gl.GetError();
        if(i != 0)
            System.out.println("***( GL error: " + i + " (render3d0)");
    }

    private void doRender3D1(Render render)
    {
        if(bDrawClouds && clouds != null && RenderContext.cfgSky.get() > 0)
        {
            Engine.lightEnv().prepareForRender(camera3D.pos.getAbsPoint(), (float)RenderContext.cfgSky.get() * 4000F);
            SearchlightGeneric.lightCloudsBySearchlights();
            clouds.render();
            LightPoint.clearRender();
        }
        DrwArray drwarray = bRenderMirror ? drwMirror : drwMaster[iRenderIndx];
        render.draw(drwarray.drwSolid, drwarray.drwTransp);
        if(!bRenderMirror)
            shadowPairsRender();
        BulletGeneric.renderAll();
        if(bEnableFog)
        {
            Render.flush();
            Render.enableFog(false);
        }
        darkerNight.render();
    }

    private void plateToRenderArray(ArrayList arraylist, ArrayList arraylist1)
    {
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)arraylist.get(j);
            if(actor instanceof ActorLandMesh)
            {
                arraylist1.add(actor);
                continue;
            }
            if((actor instanceof ActorMesh) && (((ActorMesh)actor).mesh() instanceof MeshShared))
            {
                actor.pos.getRender(__l);
                if(!((MeshShared)((ActorMesh)actor).mesh()).putToRenderArray(__l))
                    actor.draw.render(actor);
            } else
            {
                actor.draw.render(actor);
            }
        }

        arraylist.clear();
    }

    public void _getAspectViewPort(int i, float af[])
    {
        af[0] = i != 1 ? 0.6666667F : 0.0F;
        af[1] = 0.0F;
        af[2] = 0.3333333F;
        af[3] = 1.0F;
    }

    public void _getAspectViewPort(int i, int ai[])
    {
        ai[0] = i != 1 ? (2 * RendersMain.width()) / 3 : 0;
        ai[1] = 0;
        ai[2] = RendersMain.width() / 3;
        ai[3] = RendersMain.height();
    }

    private void drawTime()
    {
        if(!hud.bDrawAllMessages)
            return;
        if(bShowTime || isDemoPlaying())
        {
            int i = TextScr.This().getViewPortWidth();
            long l = Time.current();
            if(NetMissionTrack.isPlaying())
                l -= NetMissionTrack.playingStartTime;
            int j = (int)((l / 1000L) % 60L);
            int k = (int)(l / 1000L / 60L);
            if(j > 9)
                TextScr.output(i - TextScr.font().height() * 3, 5, "" + k + ":" + j);
            else
                TextScr.output(i - TextScr.font().height() * 3, 5, "" + k + ":0" + j);
        }
    }

    public int mirrorX0()
    {
        return render3D0.getViewPortX0();
    }

    public int mirrorY0()
    {
        return 0;
    }

    public int mirrorWidth()
    {
        return 256;
    }

    public int mirrorHeight()
    {
        return 256;
    }

    public void preRenderHUD()
    {
    }

    public void renderHUD()
    {
    }

    public void renderHUDcontextResize(int i, int j)
    {
    }

    public void preRenderMap2D()
    {
    }

    public void renderMap2D()
    {
    }

    public void renderMap2DcontextResize(int i, int j)
    {
    }

    private void insertFarActorItem(int i, int j, int k, float f, String s)
    {
        int l = (int)iconFarFont.width(s);
        for(int i1 = 0; i1 < iconFarListLen[iRenderIndx]; i1++)
        {
            FarActorItem faractoritem = (FarActorItem)iconFarList[iRenderIndx].get(i1);
            if(f > faractoritem.z)
            {
                if(iconFarList[iRenderIndx].size() == iconFarListLen[iRenderIndx])
                {
                    FarActorItem faractoritem1 = new FarActorItem(i, j, k, l, f, s);
                    iconFarList[iRenderIndx].add(faractoritem1);
                } else
                {
                    FarActorItem faractoritem2 = (FarActorItem)iconFarList[iRenderIndx].get(iconFarListLen[iRenderIndx]);
                    faractoritem2.set(i, j, k, l, f, s);
                    iconFarList[iRenderIndx].remove(iconFarListLen[iRenderIndx]);
                    iconFarList[iRenderIndx].add(i1, faractoritem2);
                }
                iconFarListLen[iRenderIndx]++;
                return;
            }
        }

        if(iconFarList[iRenderIndx].size() == iconFarListLen[iRenderIndx])
        {
            FarActorItem faractoritem3 = new FarActorItem(i, j, k, l, f, s);
            iconFarList[iRenderIndx].add(faractoritem3);
        } else
        {
            FarActorItem faractoritem4 = (FarActorItem)iconFarList[iRenderIndx].get(iconFarListLen[iRenderIndx]);
            faractoritem4.set(i, j, k, l, f, s);
        }
        iconFarListLen[iRenderIndx]++;
    }

    private void clipFarActorItems()
    {
        int i = iconFarFont.height();
        for(int j = 0; j < iconFarListLen[iRenderIndx]; j++)
        {
            FarActorItem faractoritem = (FarActorItem)iconFarList[iRenderIndx].get(j);
            for(int k = j + 1; k < iconFarListLen[iRenderIndx]; k++)
            {
                FarActorItem faractoritem1 = (FarActorItem)iconFarList[iRenderIndx].get(k);
                if(faractoritem1.x + faractoritem1.dx >= faractoritem.x && faractoritem1.x <= faractoritem.x + faractoritem.dx && faractoritem1.y + i >= faractoritem.y && faractoritem1.y <= faractoritem.y + i)
                {
                    iconFarList[iRenderIndx].remove(k);
                    iconFarList[iRenderIndx].add(faractoritem1);
                    k--;
                    iconFarListLen[iRenderIndx]--;
                }
            }

        }

    }

    private void clearFarActorItems()
    {
        iconFarListLen[iRenderIndx] = 0;
    }

    private boolean isBomb(Actor actor)
    {
        return (actor instanceof Bomb) || (actor instanceof Rocket);
    }

    protected void drawFarActors()
    {
        if(Main.state() != null && Main.state().id() == 18)
            return;
        if(iconFarMat == null)
            return;
        iconFarFontHeight = iconFarFont.height();
        iconClipX0 = -2D;
        iconClipY0 = -1D;
        if(bRenderMirror)
        {
            iconClipX1 = (float)render2DMirror.getViewPortWidth() + 2.0F;
            iconClipY1 = (float)render2DMirror.getViewPortHeight() + 1.0F;
        } else
        {
            iconClipX1 = (float)_render2D[iRenderIndx].getViewPortWidth() + 2.0F;
            iconClipY1 = (float)_render2D[iRenderIndx].getViewPortHeight() + 1.0F;
        }
        iconFarPlayerActor = World.getPlayerAircraft();
        iconFarViewActor = viewActor();
        iconFarPadlockItem.str = null;
        iconFarPadlockActor = getViewPadlockEnemy();
        _camera3D[iRenderIndx].pos.getRender(farActorFilter.camp);
        Point3d point3d = farActorFilter.camp;
        float f = Engine.lightEnv().sun().ToLight.z;
        if(f < 0.0F)
            f = 0.0F;
        float f2 = Engine.lightEnv().sun().Ambient + Engine.lightEnv().sun().Diffuze * (0.25F + 0.4F * f);
        if(RenderContext.cfgHardwareShaders.get() > 0)
        {
            f = Engine.lightEnv().sun().Ambient + f * Engine.lightEnv().sun().Diffuze;
            if(f > 1.0F)
                f2 *= f;
        }
        int j = (int)(127F * f2);
        if(j > 255)
            j = 255;
        iconFarColor = j | j << 8 | j << 16;
        List list = Engine.targets();
        int k = list.size();
        for(int l = 0; l < k; l++)
        {
            Actor actor = (Actor)list.get(l);
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d3 = point3d.distance(point3d1);
            if(d3 < 25000D)
                farActorFilter.isUse(actor, d3);
        }

        iconFarPlayerActor = null;
        iconFarViewActor = null;
        iconFarPadlockActor = null;
        if(iconFarListLen[iRenderIndx] != 0)
        {
            clipFarActorItems();
            for(int i = 0; i < iconFarListLen[iRenderIndx]; i++)
            {
                FarActorItem faractoritem = (FarActorItem)iconFarList[iRenderIndx].get(i);
                if(bRenderMirror)
                {
                    transformMirror.set(faractoritem.x - faractoritem.dx, faractoritem.y, faractoritem.z, faractoritem.dx);
                    iconFarFont.transform(transformMirror, faractoritem.color, faractoritem.str);
                } else
                {
                    iconFarFont.output(faractoritem.color, faractoritem.x, faractoritem.y, faractoritem.z, faractoritem.str);
                }
            }

        }
        if(iconFarPadlockItem.str != null)
        {
            if(!iconFarPadlockItem.bGround)
            {
                iconFarPadlockItem.x++;
                iconFarPadlockItem.y += -7.5F;
                float f1 = 16F;
                line3XYZ[0] = (float)((double)iconFarPadlockItem.x - (double)f1 * 0.86599999999999999D);
                line3XYZ[1] = (float)((double)iconFarPadlockItem.y - (double)f1 * 0.5D);
                line3XYZ[2] = iconFarPadlockItem.z;
                line3XYZ[3] = (float)((double)iconFarPadlockItem.x + (double)f1 * 0.86599999999999999D);
                line3XYZ[4] = (float)((double)iconFarPadlockItem.y - (double)f1 * 0.5D);
                line3XYZ[5] = iconFarPadlockItem.z;
                line3XYZ[6] = iconFarPadlockItem.x;
                line3XYZ[7] = (float)iconFarPadlockItem.y + f1;
                line3XYZ[8] = iconFarPadlockItem.z;
            } else
            {
                camera3D.pos.getRender(_lineP, _lineO);
                double d = ((double)(-_lineO.getKren()) * 3.1415926535897931D) / 180D;
                double d1 = Math.sin(d);
                double d2 = Math.cos(d);
                iconFarPadlockItem.x++;
                iconFarPadlockItem.y += -7.5F;
                float f3 = 16F;
                line3XYZ[0] = iconFarPadlockItem.x;
                line3XYZ[1] = iconFarPadlockItem.y;
                line3XYZ[2] = iconFarPadlockItem.z;
                line3XYZ[3] = (float)((double)iconFarPadlockItem.x + d2 * (double)f3 * 0.25D + d1 * 1.5D * (double)f3);
                line3XYZ[4] = (float)(((double)iconFarPadlockItem.y - d1 * (double)f3 * 0.25D) + d2 * 1.5D * (double)f3);
                line3XYZ[5] = iconFarPadlockItem.z;
                line3XYZ[6] = (float)(((double)iconFarPadlockItem.x - d2 * (double)f3 * 0.25D) + d1 * 1.5D * (double)f3);
                line3XYZ[7] = (float)((double)iconFarPadlockItem.y + d1 * (double)f3 * 0.25D + d2 * 1.5D * (double)f3);
                line3XYZ[8] = iconFarPadlockItem.z;
            }
            Render.drawBeginLines(-1);
            Render.drawLines(line3XYZ, 3, 1.0F, iconFarPadlockItem.color, Mat.TESTZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND, 5);
            Render.drawEnd();
        }
        clearFarActorItems();
    }

    protected void drawFarActorsInit()
    {
        iconFarMat = Mat.New("icons/faractor.mat");
        iconFarFont = TTFont.get("arialSmallZ");
        iconFarFinger = Finger.Int("iconFar_shortClassName");
    }

    public void initHotKeys()
    {
        CmdEnv.top().setCommand(new CmdExit(), "exit", "exit game");
        HotKeyCmdEnv.setCurrentEnv("hotkeys");
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "exit") {

            public void begin()
            {
                Main.doGameExit();
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ScreenShot") {

            public void begin()
            {
                if(scrShot == null)
                    scrShot = new ScrShot("grab");
                if(Mission.isNet())
                {
                    long l = Time.real();
                    if(lastTimeScreenShot + 10000L < l)
                        lastTimeScreenShot = l;
                    else
                        return;
                }
                scrShot.grab();
            }

        }
);
        CmdEnv.top().setCommand(new CmdScreenSequence(), "avi", "start/stop save screen shot sequence");
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ScreenSequence") {

            public void begin()
            {
                if(screenSequence == null)
                    screenSequence = new ScreenSequence();
                screenSequence.doSave();
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "land") {

            public void begin()
            {
                if(RTSConf.cur.console.getEnv().levelAccess() == 0)
                    setDrawLand(!isDrawLand());
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "clouds") {

            public void begin()
            {
                if(Mission.isSingle() && RTSConf.cur.console.getEnv().levelAccess() == 0)
                    bDrawClouds = !bDrawClouds;
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "showTime") {

            public void begin()
            {
                if(RTSConf.cur.console.getEnv().levelAccess() == 0)
                    bShowTime = !bShowTime;
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "pause") {

            public void begin()
            {
                if(TimeSkip.isDo())
                    return;
                if(Time.isEnableChangePause())
                {
                    Time.setPause(!Time.isPaused());
                    if(Config.cur.isSoundUse())
                        if(Time.isPaused())
                            AudioDevice.soundsOff();
                        else
                            AudioDevice.soundsOn();
                }
            }

        }
);
    }

    public static float FOVX = 70F;
    public static final float ZNEAR = 1.2F;
    public static final float ZFAR = 48000F;
    protected boolean bDrawIfNotFocused;
    protected boolean bUseStartLog;
    protected boolean bUseGUI;
    private boolean bShowStartIntro;
    public GUIWindowManager guiManager;
    public KeyRecord keyRecord;
    public OrdersTree ordersTree;
    public TimeSkip timeSkip;
    public HookView hookView;
    public HookViewFly hookViewFly;
    public HookViewEnemy hookViewEnemy;
    public AircraftHotKeys aircraftHotKeys;
    public Cockpit cockpits[];
    public Cockpit cockpitCur;
    public OverLoad overLoad;
    public OverLoad _overLoad[];
    public SunGlare sunGlare;
    public SunGlare _sunGlare[];
    public LightsGlare lightsGlare;
    public LightsGlare _lightsGlare[];
    private SunFlare _sunFlare[];
    public Render _sunFlareRender[];
    private boolean bViewFly;
    private boolean bViewEnemy;
    public boolean bEnableFog;
    private boolean bDrawLand;
    public EffClouds clouds;
    public Zip zip;
    public boolean bDrawClouds;
    public SpritesFog spritesFog;
    public Cinema _cinema[];
    public Render3D0R render3D0R;
    public Camera3DR camera3DR;
    public Render3D0 render3D0;
    public Render3D1 render3D1;
    public Camera3D camera3D;
    public Render3D0 _render3D0[];
    public Render3D1 _render3D1[];
    public Camera3D _camera3D[];
    public Render2D render2D;
    public CameraOrtho2D camera2D;
    public Render2D _render2D[];
    public CameraOrtho2D _camera2D[];
    public Render3D0Mirror render3D0Mirror;
    public Render3D1Mirror render3D1Mirror;
    public Camera3D camera3DMirror;
    public Render2DMirror render2DMirror;
    public CameraOrtho2D camera2DMirror;
    public RenderCockpit renderCockpit;
    public Camera3D cameraCockpit;
    public RenderCockpit _renderCockpit[];
    public Camera3D _cameraCockpit[];
    public RenderCockpitMirror renderCockpitMirror;
    public Camera3D cameraCockpitMirror;
    public RenderHUD renderHUD;
    public CameraOrtho2D cameraHUD;
    public HUD hud;
    public RenderMap2D renderMap2D;
    public CameraOrtho2D cameraMap2D;
    public Land2D land2D;
    public Land2DText land2DText;
    public DarkerNight darkerNight;
    private static String _sLastMusic = "ru";
    protected int viewMirror;
    private int iconTypes;
    public static final String gameHotKeyCmdEnvs[] = {
        "Console", "hotkeys", "HookView", "PanView", "SnapView", "pilot", "move", "gunner", "misc", "orders", 
        "aircraftView", "timeCompression", "gui"
    };
    public static final String builderHotKeyCmdEnvs[] = {
        "Console", "builder", "hotkeys"
    };
    KeyRecordCallback playRecordedMissionCallback;
    String playRecordedFile;
    int playBatchCurRecord;
    boolean playEndBatch;
    SectFile playRecordedSect;
    int playRecorderIndx;
    String playRecordedPlayFile;
    InOutStreams playRecordedStreams;
    NetChannelInStream playRecordedNetChannelIn;
    GameTrack gameTrackRecord;
    GameTrack gameTrackPlay;
    private boolean bLoadRecordedStates1Before;
    private boolean bRenderMirror;
    private int iRenderIndx;
    protected double _modelMatrix3D[][];
    protected double _projMatrix3D[][];
    protected int _viewport[][];
    protected double _modelMatrix3DMirror[];
    protected double _projMatrix3DMirror[];
    protected int _viewportMirror[];
    private double _dIn[];
    private double _dOut[];
    private static double shadowPairsR;
    private static double shadowPairsR2;
    private ArrayList shadowPairsList1;
    private HashMap shadowPairsMap1;
    private BigshipGeneric shadowPairsCur1;
    private ArrayList shadowPairsList2;
    private ActorFilter shadowPairsFilter;
    private DrwArray drwMaster[] = {
        new DrwArray(), new DrwArray(), new DrwArray()
    };
    private DrwArray drwMirror;
    private Loc __l;
    private Point3d __p;
    private Orient __o;
    private Vector3d __v;
    private boolean bShowTime;
    public static final String ICONFAR_PROPERTY = "iconFar_shortClassName";
    public static final float iconFarActorSizeX = 2F;
    public static final float iconFarActorSizeY = 1F;
    public static final float iconFarSmallActorSize = 1F;
    public static int iconFarColor = 0x7f7f7f;
    protected double iconGroundDrawMin;
    protected double iconSmallDrawMin;
    protected double iconAirDrawMin;
    protected double iconDrawMax;
    private Mat iconFarMat;
    private TTFont iconFarFont;
    private int iconFarFinger;
    private float iconFarFontHeight;
    private double iconClipX0;
    private double iconClipX1;
    private double iconClipY0;
    private double iconClipY1;
    private Actor iconFarPlayerActor;
    private Actor iconFarViewActor;
    private Actor iconFarPadlockActor;
    private FarActorItem iconFarPadlockItem;
    private ArrayList iconFarList[] = {
        new ArrayList(), new ArrayList(), new ArrayList()
    };
    private int iconFarListLen[] = {
        0, 0, 0
    };
    private FarActorFilter farActorFilter;
    private float line3XYZ[];
    private Point3d _lineP;
    private Orient _lineO;
    private TransformMirror transformMirror;
    private long lastTimeScreenShot;
    private ScrShot scrShot;
    private ScreenSequence screenSequence;

    static 
    {
        shadowPairsR = 1000D;
        shadowPairsR2 = shadowPairsR * shadowPairsR;
    }


































}