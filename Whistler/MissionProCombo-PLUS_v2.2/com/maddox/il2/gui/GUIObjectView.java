package com.maddox.il2.gui;

import com.maddox.JGP.*;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.ActorSimpleHMesh;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.rts.*;
import com.maddox.util.NumberTokenizer;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GUIObjectView extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(isMouseCaptured())
                return true;
            if(gwindow == wCountry)
            {
                fillObjects();
                int k = wCountry.getSelected();
                if(k >= 0)
                {
                    Main3D.menuMusicPlay(k != 0 ? "de" : "ru");
                    GUIObjectInspector.s_country = wCountry.getSelected();
                    if(wTable.countRows() > 0)
                    {
                        GUIObjectInspector.s_object = 0;
                        GUIObjectInspector.s_scroll = 0.0F;
                        wTable.setSelect(GUIObjectInspector.s_object, 0);
                        if(wTable.vSB.isVisible())
                            wTable.vSB.setPos(GUIObjectInspector.s_scroll, true);
                    }
                }
                return true;
            }
            if(gwindow == wText)
            {
                GUIObjectInspector.s_object = wTable.selectRow;
                GUIObjectInspector.s_scroll = wTable.vSB.pos();
                Main.stateStack().change(22);
                return true;
            }
            if(gwindow == wPrev)
            {
                GUIObjectInspector.s_object = wTable.selectRow;
                GUIObjectInspector.s_scroll = wTable.vSB.pos();
                Main.stateStack().change(22);
                Main.stateStack().pop();
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
//            GUISeparate.draw(this, GColor.Gray, x1024(40F), y1024(620F), x1024(250F), 2.0F);
//            GUISeparate.draw(this, GColor.Gray, x1024(320F), y1024(32F), 2.0F, y1024(650F));
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(40F), y1024(40F), x1024(240F), y1024(32F), 0, i18n("obj.SelectCountry"));
            draw(x1024(104F), y1024(652F), x1024(192F), y1024(48F), 0, i18n("obj.Back"));
            draw(x1024(730F), y1024(652F), x1024(192F), y1024(48F), 2, i18n("obj.Text"));
            root.C.font = helpFont;
            draw(x1024(330F), y1024(636F), x1024(560F), y1024(16F), 0, i18n("obj.Help0"));
            draw(x1024(330F), y1024(652F), x1024(470F), y1024(16F), 0, i18n("obj.Help1"));
            setCanvasColorWHITE();
            GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
            lookAndFeel().drawBevel(this, x1024(330F) - gbevel.L.dx, y1024(50F) - gbevel.T.dy, x1024(655F) + gbevel.R.dx * 2.0F, y1024(570F) + gbevel.B.dy * 2.0F, gbevel, ((GUILookAndFeel)lookAndFeel()).basicelements, false);
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            wPrev.setPosC(x1024(64F), y1024(676F));
            wText.setPosC(x1024(960F), y1024(676F));
            wCountry.setPosSize(x1024(40F), y1024(80F), x1024(250F), M(2.0F));
            wTable.setPosSize(x1024(40F), y1024(150F), x1024(250F), y1024(470F));
            wRenders.setPosSize(x1024(330F), y1024(50F), x1024(655F), y1024(570F));
        }

        public DialogClient()
        {
        }
    }

    public class Table extends GWindowTable
    {

        public int countRows()
        {
            return objects == null ? 0 : objects.size();
        }

        public void renderCell(int i, int j, boolean flag, float f, float f1)
        {
            setCanvasFont(0);
            String s = ((ObjectInfo)objects.get(i)).name;
            if(flag)
            {
                setCanvasColorBLACK();
                draw(0.0F, 0.0F, f, f1, lookAndFeel().regionWhite);
                setCanvasColorWHITE();
                draw(0.0F, 0.0F, f, f1, 0, s);
            } else
            {
                setCanvasColorBLACK();
                draw(0.0F, 0.0F, f, f1, 0, s);
            }
        }

        public void setSelect(int i, int j)
        {
            super.setSelect(i, j);
        }

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(super.notify(gwindow, i, j))
            {
                return true;
            } else
            {
                notify(i, j);
                return false;
            }
        }

        public void afterCreated()
        {
            super.afterCreated();
            bColumnsSizable = false;
            addColumn(I18N.gui("obj.ObjectTypesList"), null);
            vSB.scroll = rowHeight(0);
            bNotify = true;
            wClient.bNotify = true;
            resized();
        }

        public void resolutionChanged()
        {
            vSB.scroll = rowHeight(0);
            super.resolutionChanged();
        }

        public ArrayList objects;

        public Table(GWindow gwindow)
        {
            super(gwindow, 2.0F, 4F, 20F, 16F);
            objects = new ArrayList();
        }
    }

    public class WRenders extends GUIRenders
    {

        public void mouseMove(float f, float f1)
        {
            float f2 = root.mouseStep.dx;
            float f3 = root.mouseStep.dy;
            if(Mouse.adapter().isInvert())
                f3 = -f3;
            if(isMouseCaptured() && MODE_ROTATE)
            {
                ROT_X += f2 / 2.0F;
                ROT_Y -= f3 / 2.0F;
                if(bGround)
                {
                    if(ROT_Y > 20)
                        ROT_Y = 20;
                    if(ROT_Y < -50)
                        ROT_Y = -50;
                }
            }
            if(isMouseCaptured() && MODE_SCALE)
            {
                if(GUIObjectView.SCALE_FACTOR < GUIObjectView.Z_DIST_NEAR)
                {
                    if(f3 > 0)
                        GUIObjectView.SCALE_FACTOR += f3 / 2.0F;
                    else
                        GUIObjectView.SCALE_FACTOR -= f3 / 2.0F;
                } else
                {
                    GUIObjectView.SCALE_FACTOR -= f3 / 2.0F;
                    if(GUIObjectView.SCALE_FACTOR > GUIObjectView.Z_DIST_FAR)
                        GUIObjectView.SCALE_FACTOR = GUIObjectView.Z_DIST_FAR;
                    if(GUIObjectView.SCALE_FACTOR < GUIObjectView.Z_DIST_NEAR)
                        GUIObjectView.SCALE_FACTOR = GUIObjectView.Z_DIST_NEAR;
                }
            }
        }

        public void mouseButton(int i, boolean flag, float f, float f1)
        {
            if(i == 1 && flag)
            {
                mouseCursor = 0;
                mouseCapture(true);
                MODE_SCALE = true;
                MODE_ROTATE = false;
            }
            if(i == 0 && flag)
            {
                mouseCursor = 0;
                mouseCapture(true);
                MODE_ROTATE = true;
                MODE_SCALE = false;
            }
            if(!isMouseCaptured())
            {
                super.mouseButton(i, flag, f, f1);
                return;
            }
            if(!flag)
            {
                mouseCursor = 1;
                mouseCapture(false);
            }
        }

        public void created()
        {
            render3D = new _Render3D(renders, 1.0F);
            render3D.camera3D = new Camera3D();
            render3D.camera3D.set(50F, 1.0F, 20000F);
            render3D.setCamera(render3D.camera3D);
            LightEnvXY lightenvxy = new LightEnvXY();
            render3D.setLightEnv(lightenvxy);
            lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
            Vector3f vector3f = new Vector3f(-1F, 1.0F, -1F);
            vector3f.normalize();
            lightenvxy.sun().set(vector3f);
            bNotify = true;
        }

        public _Render3D render3D;
        public boolean MODE_SCALE;
        public boolean MODE_ROTATE;

        public WRenders()
        {
            MODE_SCALE = false;
            MODE_ROTATE = false;
        }
    }

    public class _Render3D extends Render
    {

        public void preRender()
        {
            checkMesh();
            if(Actor.isValid(actorMesh))
            {
//                if(animateMeshA != 0.0F || animateMeshT != 0.0F)
//                {
//                    actorMesh.pos.getAbs(_orient);
//                    _orient.set(_orient.azimut() + animateMeshA * wRenders.root.deltaTimeSec, _orient.tangage() + animateMeshT * wRenders.root.deltaTimeSec, 0.0F);
//                    actorMesh.pos.setAbs(_orient);
//                    actorMesh.pos.reset();
//                }
                worldMesh.draw.preRender(worldMesh);
                isShadow = (actorMesh.draw.preRender(actorMesh) & 4) != 0;

            }
        }

        public void render()
        {
            if(Actor.isValid(actorMesh))
            {
                Render.prepareStates();
                worldMesh.draw.render(worldMesh);
                if(isShadow && bGround)
                    actorMesh.draw.renderShadowProjective(actorMesh);
                actorMesh.draw.render(actorMesh);
            }
        }

        public void checkMesh()
        {
            int i = wTable.selectRow;
            if(i < 0)
            {
                if(Actor.isValid(actorMesh))
                    actorMesh.destroy();
                actorMesh = null;
            }
            ObjectInfo objectinfo = (ObjectInfo)wTable.objects.get(i);
            if(meshName == objectinfo.meshName && Actor.isValid(actorMesh))
            {
                double d = ((ActorMesh)actorMesh).mesh().visibilityR();
                if(bGround)
                {
                    d *= Math.cos(0.78539816339744828D) / Math.sin(((double)camera3D.FOV() * 3.1415926535897931D) / 180D / 2D);
                    d -= GUIObjectView.Z_GAP;
                    if(d < GUIObjectView.Z_DIST_NEAR)
                        GUIObjectView.Z_DIST_NEAR = d;
                    d = GUIObjectView.SCALE_FACTOR;
                    _point.set(-d, 0.0D, 0.0D);
                    _o.set(ROT_X, ROT_Y - 45, 0.0F);
                } else
                {
                    d *= Math.cos(0.26179938779914941D) / Math.sin(((double)camera3D.FOV() * 3.1415926535897931D) / 180D / 2D);
                    d -= GUIObjectView.Z_GAP;
                    if(d < GUIObjectView.Z_DIST_NEAR)
                        GUIObjectView.Z_DIST_NEAR = d;
                    d = GUIObjectView.SCALE_FACTOR;
                    _point.set(-d, 0.0D, 0.0D);
                    _o.set(ROT_X, ROT_Y, 0.0F);
                    for(int j = 1; j <= 6; j++)
                        if(((ActorSimpleHMesh)actorMesh).hierMesh().chunkFindCheck("Prop" + j + "_D0") != -1)
                            ((ActorSimpleHMesh)actorMesh).hierMesh().chunkSetAngles("Prop" + j + "_D0", 0.0F, -propRot + (float)(j * 50), 0.0F);

                    propRot = (propRot + 20F) % 360F;
                }
                _o.transform(_point);
                camera3D.pos.setAbs(_point, _o);
                return;
            }
            if(Actor.isValid(actorMesh))
                actorMesh.destroy();
            actorMesh = null;
            meshName = objectinfo.meshName;
            bGround = objectinfo._bGround;
            if(meshName == null)
                return;
            double d1 = 20D;
//            GUIObjectView.SCALE_FACTOR = GUIObjectView.Z_DIST_BORN;
//            ROT_Y = 20;
//            ROT_X = 220;
            worldMesh = new ActorSimpleMesh("3do/GUI/ObjectInspector/" + GUIObjectInspector.type + "/mono.sim");
            if(meshName.toLowerCase().endsWith(".sim"))
            {
                try
                {
                    actorMesh = new ActorSimpleMesh(meshName);
                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                    actorMesh = null;
                    return;
                }
                d1 = ((ActorMesh)actorMesh).mesh().visibilityR();
                double d2 = 0.0D;
                int k = ((ActorSimpleMesh)actorMesh).mesh().hookFind("Ground_Level");
                if(k != -1)
                {
                    Matrix4d matrix4d = new Matrix4d();
                    ((ActorSimpleMesh)actorMesh).mesh().hookMatrix(k, matrix4d);
                    double d3 = -matrix4d.m23;
                    ((ActorSimpleMesh)actorMesh).pos.setAbs(new Point3d(0.0D, 0.0D, d3));
                }
            } else
            {
                try
                {
                    actorMesh = new ActorSimpleHMesh(meshName);
                }
                catch(Exception exception1)
                {
                    System.out.println(exception1.getMessage());
                    actorMesh = null;
                    return;
                }
                d1 = ((ActorHMesh)actorMesh).hierMesh().visibilityR();
                double d4 = 0.0D;
                int l = ((ActorSimpleHMesh)actorMesh).mesh().hookFind("Ground_Level");
                if(l != -1)
                {
                    Matrix4d matrix4d1 = new Matrix4d();
                    ((ActorSimpleHMesh)actorMesh).mesh().hookMatrix(l, matrix4d1);
                    double d5 = -matrix4d1.m23;
                    ((ActorSimpleHMesh)actorMesh).pos.setAbs(new Point3d(0.0D, 0.0D, d5));
                }
                if(!bGround)
                {
                    if(objectinfo.camouflage != null)
                        World.cur().setCamouflage(objectinfo.camouflage);
                    Aircraft.prepareMeshCamouflage(meshName, ((ActorHMesh)actorMesh).hierMesh(), null, null);
                    if(objectinfo.reg != null)
                    {
                        PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(objectinfo.airClass, objectinfo.reg.country());
                        if(paintscheme != null)
                        {
                            int j1 = 0;
                            int k1 = 0;
                            int l1 = 0;
                            paintscheme.prepare(objectinfo.airClass, ((ActorHMesh)actorMesh).hierMesh(), objectinfo.reg, j1, k1, l1, true);
                        }
                    }
                }
                for(int i1 = 1; i1 <= 6; i1++)
                    if(((ActorSimpleHMesh)actorMesh).hierMesh().chunkFindCheck("Prop" + i1 + "_D0") != -1)
                    {
                        ((ActorSimpleHMesh)actorMesh).hierMesh().chunkVisible("Prop" + i1 + "_D0", false);
                        ((ActorSimpleHMesh)actorMesh).hierMesh().chunkVisible("PropRot" + i1 + "_D0", true);
                    }

            }
            getDistanceProperties();
            if(bGround)
            {
                d1 *= Math.cos(0.78539816339744828D) / Math.sin(((double)camera3D.FOV() * 3.1415926535897931D) / 180D / 2D);
                d1 -= GUIObjectView.Z_GAP;
                if(d1 < GUIObjectView.Z_DIST_NEAR)
                    GUIObjectView.Z_DIST_NEAR = d1;
                d1 = GUIObjectView.SCALE_FACTOR;
                _point.set(-d1, 0.0D, 0.0D);
                _o.set(ROT_X, ROT_Y - 45, 0.0F);
            } else
            {
                d1 *= Math.cos(0.26179938779914941D) / Math.sin(((double)camera3D.FOV() * 3.1415926535897931D) / 180D / 2D);
                d1 -= GUIObjectView.Z_GAP;
                if(d1 < GUIObjectView.Z_DIST_NEAR)
                    GUIObjectView.Z_DIST_NEAR = d1;
                d1 = GUIObjectView.SCALE_FACTOR;
                _point.set(-d1, 0.0D, 0.0D);
                _o.set(ROT_X, ROT_Y, 0.0F);
            }
            _o.transform(_point);
            camera3D.pos.setAbs(_point, _o);
            camera3D.pos.reset();
//            if(bGround)
//                animateMeshT = 0.0F;
        }

        public Camera3D camera3D;
        public String meshName;
        public Actor actorMesh;
        public Actor worldMesh;
//        public float animateMeshA;
//        public float animateMeshT;
        public boolean isShadow;

        public _Render3D(Renders renders, float f)
        {
            super(renders, f);
            meshName = null;
            actorMesh = null;
            worldMesh = null;
//            animateMeshA = 0.0F;
//            animateMeshT = 0.0F;
            isShadow = false;
            setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            useClearStencil(true);
        }
    }

    static class ObjectInfo
    {
        public String key;
        public String name;
//        public String info;
        public String meshName;
        public boolean _bGround;
        public Class airClass;
        public Regiment reg;
        public String camouflage;

        public ObjectInfo(String s, String s1, String s2, boolean flag, Class class1, String s3, String s4)
        {
            key = s1;
            meshName = s2;
            _bGround = flag;
            camouflage = s4;
            if(!flag)
            {
                name = I18N.plane(s1);
//                try
//                {
//                    ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale, LDRres.loader());
//                    info = resourcebundle.getString(s1);
//                }
//                catch(Exception exception)
//                {
//                    info = "No info available";
//                }
                airClass = class1;
                if(s3 != null)
                {
                    reg = (Regiment)Actor.getByName(s3);
                    meshName = Aircraft.getPropertyMesh(airClass, reg.country());
                }
            } else
            {
                name = I18N.technic(s1);
                try
                {
                    ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale, LDRres.loader());
                    if(name.equals(s1))
                        name = resourcebundle.getString(s1 + "_NAME");
//                    info = resourcebundle.getString(s1 + "_INFO");
                }
                catch(Exception exception)
                {
//                    name = s1;
//                    info = "No info available.";
                }
                if(s3 != null)
                    reg = (Regiment)Actor.getByName(s3);
            }
        }
    }

    public void enterPush(GameState gamestate)
    {
        GUIObjectInspector.s_object = 0;
        GUIObjectInspector.s_scroll = 0.0F;
        ROT_X = 225;
        ROT_Y = 0;
        getScaleFactor();
        _enter();
    }

    public void _enter()
    {
        wCountry.setSelected(GUIObjectInspector.s_country, true, false);
        Main3D.menuMusicPlay(GUIObjectInspector.s_country != 0 ? "de" : "ru");
        fillObjects();
        getDistanceProperties();
        client.activateWindow();
        wTable.resolutionChanged();
        if(wTable.countRows() > 0)
        {
            wTable.setSelect(GUIObjectInspector.s_object, 0);
            if(wTable.vSB.isVisible())
                wTable.vSB.setPos(GUIObjectInspector.s_scroll, true);
        }
    }

    public void _leave()
    {
        client.hideWindow();
    }

    public void getDistanceProperties()
    {
        String s = "NoName";
        try
        {
            ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/Distance", RTSConf.cur.locale, LDRres.loader());
            String s1 = resourcebundle.getString(GUIObjectInspector.type);
            NumberTokenizer numbertokenizer = new NumberTokenizer(s1);
            Z_DIST_NEAR = numbertokenizer.next(1000F);
            Z_DIST_FAR = numbertokenizer.next(1000F);
            Z_DIST_BORN = numbertokenizer.next(1000F);
            Z_GAP = numbertokenizer.next(1000F);
        }
        catch(Exception exception)
        {
            Z_DIST_NEAR = 20D;
            Z_DIST_FAR = 100D;
            Z_DIST_BORN = 30D;
            Z_GAP = 6D;
            System.out.println(GUIObjectInspector.type + ": error occured");
        }
//        if(bGround);
    }

    public void getScaleFactor()
    {
        String s = "NoName";
        try
        {
            ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/Distance", RTSConf.cur.locale, LDRres.loader());
            String s1 = resourcebundle.getString(GUIObjectInspector.type);
            NumberTokenizer numbertokenizer = new NumberTokenizer(s1);
            numbertokenizer.next();
            numbertokenizer.next();
            SCALE_FACTOR = numbertokenizer.next(1000F);
        }
        catch(Exception exception)
        {
            SCALE_FACTOR = 20D;
            System.out.println(GUIObjectInspector.type + ": error occured");
        }
    }

    public void fillCountries()
    {
        wCountry.clear();
        String s = "NoName";
        for(int i = 0; i < cnt.length; i++)
        {
            String s1;
            try
            {
                ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
                s1 = resourcebundle.getString(cnt[i]);
            }
            catch(Exception exception)
            {
                s1 = cnt[i];
            }
            wCountry.add(s1);
        }

    }

    public void fillObjects()
    {
        wTable.objects.clear();
        int i = wCountry.getSelected() + 1;
        if("air".equals(GUIObjectInspector.type))
        {
            String s = "com/maddox/il2/objects/air.ini";
            SectFile sectfile = new SectFile(s, 0);
            int j = sectfile.sectionIndex("AIR");
            int k = sectfile.vars(j);
            for(int i1 = 0; i1 < k; i1++)
            {
                String s3 = sectfile.var(j, i1);
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(j, i1));
                String air = numbertokenizer.next();
                int country = numbertokenizer.next(0);
                boolean flag = true;
                String regiment = null;
                String camouflage = null;
                while(numbertokenizer.hasMoreTokens())
                {
                    String token = numbertokenizer.next();

//                    if("NOINFO".equals(token))
//                    {
//                        flag = false;
//                        break;
//                    }

                    if("NOQUICK".equals(token))
                    {
                        flag = false;
                        break;
                    }

                    if("SUMMER".equals(token))
                        camouflage = token;
                    else
                    if("WINTER".equals(token))
                        camouflage = token;
                    else
                    if("DESERT".equals(token))
                        camouflage = token;
                    else
                        regiment = token;
                }
                if(flag && country == i)
                    try
                    {
                        Class planeClass = ObjIO.classForName(air);
//                        String s10 = Aircraft.getPropertyMeshDemo(planeClass, cnt[wCountry.getSelected()]);
                        String s10 = Aircraft.getPropertyMesh(planeClass, cnt[wCountry.getSelected()]);
                        ObjectInfo objectinfo1 = new ObjectInfo(null, s3, s10, false, planeClass, regiment, camouflage);
                        wTable.objects.add(objectinfo1);
                    }
                    catch(Exception exception) { }
            }

        } else
        {
            String s1 = "i18n/" + GUIObjectInspector.type + ".ini";
            SectFile sectfile1 = new SectFile(s1, 0);
            String s2 = "i18n/" + GUIObjectInspector.type;
            int l = sectfile1.sectionIndex("ALL");
            int j1 = sectfile1.vars(l);
            for(int k1 = 0; k1 < j1; k1++)
            {
                String s4 = sectfile1.var(l, k1);
                NumberTokenizer numbertokenizer1 = new NumberTokenizer(sectfile1.value(l, k1));
                String s6 = numbertokenizer1.next();
                int i2 = numbertokenizer1.next(0);
                if(i2 == 0)
                    i2 = i;
                if(i2 == i)
                {
                    ObjectInfo objectinfo = new ObjectInfo(s2, s4, s6, true, null, null, null);
                    wTable.objects.add(objectinfo);
                }
            }
        }
        wTable.resized();
    }

    public GUIObjectView(GWindowRoot gwindowroot)
    {
        super(23);
        propRot = 0.0F;
        _o = new Orient(0.0F, 0.0F, 0.0F);
//        ROT_X = 0;
//        ROT_Y = 0;
        bGround = false;
        _orient = new Orient();
        _point = new Point3d();
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("obj.infoV");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wCountry = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCountry.setEditable(false);
        fillCountries();
        wCountry.setSelected(GUIObjectInspector.s_country, true, false);
        wTable = new Table(dialogClient);
        dialogClient.create(wRenders = new WRenders());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        wPrev = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        wText = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        helpFont = GFont.New("arial8");
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public final float propDelta = 20F;
    public float propRot;
    public Orient _o;
    public int ROT_X;
    public int ROT_Y;
    public static double Z_GAP = 4D;
    public static double Z_DIST_BORN = 0.0D;
    public static double Z_DIST_NEAR = 0.0D;
    public static double Z_DIST_FAR = 100D;
    public static double SCALE_FACTOR = 0.0D;
    public boolean bGround;
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIButton wPrev;
    public GUIButton wText;
    public GWindowComboControl wCountry;
    public Table wTable;
    public WRenders wRenders;
    public GFont helpFont;
    public static String cnt[] = {
        "", ""
    };
    private Orient _orient;
    private Point3d _point;

    static 
    {
        cnt[0] = "allies";
        cnt[1] = "axis";
    }


}
