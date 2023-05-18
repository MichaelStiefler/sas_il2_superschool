package com.maddox.il2.builder;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorSimpleHMesh;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Property;
import java.io.*;
import java.util.*;

public class WSelect extends GWindowFramed
{
    class _Render3D extends Render
    {
        public void preRender()
        {
            if(!Actor.isValid(actorMesh) && actorMesh != null && meshName != null)
                setMesh(meshName, bGround);
            if(Actor.isValid(actorMesh))
            {
                if(animateMeshA != 0.0F || animateMeshT != 0.0F)
                {
                    actorMesh.pos.getAbs(_orient);
                    _orient.set(_orient.azimut() + animateMeshA * root.deltaTimeSec, _orient.tangage() + animateMeshT * root.deltaTimeSec, 0.0F);
                    _orient.wrap360();
                    actorMesh.pos.setAbs(_orient);
                    actorMesh.pos.reset();
                }
                actorMesh.draw.preRender(actorMesh);
            }
        }

        public void render()
        {
            if(Actor.isValid(actorMesh))
            {
                Render.prepareStates();
                actorMesh.draw.render(actorMesh);
            }
        }

        public _Render3D(Renders renders1, float f)
        {
            super(renders1, f);
//            setClearColor(GWindowLookAndFeel.getUIBackgroundColor());
//            setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            useClearColor(true);
            useClearStencil(true);
        }
    }


    public void clearExtendTabs()
    {
        for(int i = tabsClient.sizeTabs(); i-- > 1;)
            tabsClient.removeTab(i);

    }

    public boolean isMeshVisible()
    {
        return renders.bVisible;
    }

    public HierMesh getHierMesh()
    {
        if(!Actor.isValid(actorMesh))
            return null;
        if(!(actorMesh instanceof ActorHMesh))
            return null;
        else
            return ((ActorHMesh)actorMesh).hierMesh();
    }

    public void windowShown()
    {
        builder.mSelectItem.bChecked = true;
        if(firstShow)
        {
            clientWindow.parentWindow.setSize(lookAndFeel().metric(30F), lookAndFeel().metric(50F));
            renders.showWindow();
            setMesh(meshName, bGround);
            _resized();
            firstShow = false;
        }
        super.windowShown();
        doUpdateMesh();
    }

    public void windowHidden()
    {
        builder.mSelectItem.bChecked = false;
        super.windowHidden();
    }

    private void doUpdateMesh()
    {
        Path path = builder.selectedPath();
        if(path == null)
        {
            return;
        } else
        {
            Plugin plugin = (Plugin)Property.value(path, "builderPlugin");
            plugin.updateSelectorMesh();
            return;
        }
    }

    public void setMesh(String s, boolean flag)
    {
        setMesh(s, flag, null, null);
    }

    public void setMesh(String s, boolean flag, Class class1, Regiment regiment)
    {
        if(meshName == s && Actor.isValid(actorMesh))
            return;
        if(Actor.isValid(actorMesh))
            actorMesh.destroy();
        render3D.setClearColor(new Color4f(0.5882353F, 0.6470588F, 0.6666667F, 1.0F));
        actorMesh = null;
        meshName = s;
        bGround = flag;
//        if(s == null)
//        {
//            wShow.bEnable = false;
//            if(renders.bVisible)
//            {
//                renders.hideWindow();
//                wShow.cap = new GCaption(Plugin.i18n("ButtonShow"));
//            }
//            return;
//        }
//        wShow.bEnable = true;
        if(s == null)
            return;
        if(!renders.bVisible)
            return;
        if(!renders.isVisible())
        {
            renders.hideWindow();
            wShow.cap = new GCaption(Plugin.i18n("ButtonShow"));
            return;
        }
        double d = 20D;
        if(s.toLowerCase().endsWith(".sim"))
        {
            actorMesh = new ActorSimpleMesh(s);
            d = ((ActorMesh)actorMesh).mesh().visibilityR();
        } else
        {
            actorMesh = new ActorSimpleHMesh(s);
            d = ((ActorHMesh)actorMesh).hierMesh().visibilityR();
            if(!flag)
                Aircraft.prepareMeshCamouflage(s, ((ActorHMesh)actorMesh).hierMesh(), class1, regiment);
        }
        if(flag)
        {
            actorAzimut = 30F;
            actorMesh.pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
            d *= Math.cos(0.78539816339744828D) / Math.sin(((double)camera3D.FOV() * 3.1415926535897931D) / 180D / 2D);
            camera3D.pos.setAbs(new Point3d(d, 0.0D, d * 0.90000000000000002D), new Orient(180F, -45F, 0.0F));
        } else
        {
            actorAzimut = 90F;
            actorMesh.pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
            d *= Math.cos(0.26179938779914941D) / Math.sin(((double)camera3D.FOV() * 3.1415926535897931D) / 180D / 2D);
            camera3D.pos.setAbs(new Point3d(d, 0.0D, 0.0D), new Orient(180F, 0.0F, 0.0F));
        }
        render3D.setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
        camera3D.pos.reset();
//        if(flag)
//            animateMeshT = 0.0F;
        doUpdateMesh();
    }

    public boolean export(String s)
    {
        try
        {
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(s)));
            for(int j = 0; j < comboBox2.size(); j++)
                printwriter.println(comboBox2.get(j));
            printwriter.close();
            return true;
        }
        catch(Exception exception)
        {
            return false;
        }
    }

    public void created()
    {
        bAlwaysOnTop = true;
        super.created();
        title = Plugin.i18n("Object");
        clientWindow = create(new GWindowTabDialogClient());
        tabsClient = (GWindowTabDialogClient)clientWindow;
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)tabsClient.create(new GWindowDialogClient());
        tabsClient.addTab(Plugin.i18n("Type"), gwindowdialogclient);
        gwindowdialogclient.addControl(comboBox1 = new GWindowComboControl(gwindowdialogclient, 1.0F, 1.0F, 18F) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                    builder.setSelected(null);
                return super.notify(i, j);
            }

        }
);
        comboBox1.setEditable(false);
        comboBox1.listCountLines = comboBox1.listVisibleLines = 16;
        gwindowdialogclient.addControl(comboBox2 = new GWindowComboControl(gwindowdialogclient, 1.0F, 2.5F, 18F) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    if(Actor.isValid(builder.selectedPath()))
                    {
                        Plugin plugin = (Plugin)Property.value(builder.selectedPath(), "builderPlugin");
                        plugin.changeType();
                    } else
                    if(Actor.isValid(builder.selectedActor()))
                    {
                        Plugin plugin1 = (Plugin)Property.value(builder.selectedActor(), "builderPlugin");
                        if(plugin1 != null)
                            plugin1.changeType();
                        else
                        if(builder.bMultiSelect)
                        {
                            Plugin plugin2 = Plugin.getPlugin("MapActors");
                            plugin2.changeType();
                        }
                    }
                    if(comboBox1.get(comboBox1.getSelected()).equals(Plugin.i18n("buildings")))
                        builder.wBrowse.comboBox2.setSelected(comboBox2.getSelected(), true, true);
                }
                return super.notify(i, j);
            }

        }
);
        comboBox2.setEditable(false);
        comboBox2.listCountLines = comboBox2.listVisibleLines = 32;
        gwindowdialogclient.addControl(wPrev = new GWindowButton(gwindowdialogclient, 1.0F, 4F, 1.4F, 1.4F, "<", null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    if(comboBox2.getSelected() > 0)
                        comboBox2.setSelected(comboBox2.getSelected() - 1, true, true);
                }
                return super.notify(i, j);
            }
        }
);
        gwindowdialogclient.addControl(wNext = new GWindowButton(gwindowdialogclient, 3F, 4F, 1.4F, 1.4F, ">", null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    if(comboBox2.getSelected() < comboBox2.size() - 1)
                        comboBox2.setSelected(comboBox2.getSelected() + 1, true, true);
                }
                return super.notify(i, j);
            }
        }
);
        gwindowdialogclient.addControl(wShow = new GWindowButton(gwindowdialogclient, 5F, 4F, 18F, 1.4F, Plugin.i18n("ButtonHide"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                    if(renders.bVisible)
                    {
                        renders.hideWindow();
                        cap = new GCaption(Plugin.i18n("ButtonShow"));
                    } else
                    {
                        Actor.destroy(actorMesh);
                        renders.showWindow();
                        cap = new GCaption(Plugin.i18n("ButtonHide"));
                        if(Actor.isValid(actorMesh))
                            actorMesh.destroy();
                        setMesh(meshName, bGround);
                        _resized();
                    }
                return super.notify(i, j);
            }
        }
);
//        wShow.bEnable = false;
        gwindowdialogclient.addControl(wExport = new GWindowButton(gwindowdialogclient, 1.0F, 4F, 18F, 1.4F, "Export...", null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(root, true, "Export List", "", new GFileFilter[] {
                        new GFileFilterName("Text files", new String[] {
                            "*.txt"
                        })
                    }) {

                        public void result(String s)
                        {
                            if(s != null)
                            {
                                if(!s.toLowerCase().endsWith(".txt"))
                                     s = s + ".txt";
                                if(export(s))
                                    new GWindowMessageBox(root, 25F, true, "Export List", "Object list successfully exported to:\n\n" + s, 3, 0.0F);
                            }
                        }

                    }
;
                    gwindowfilesaveas.wEdit.setValue("FMB " + comboBox1.getValue(), false);
                }
                return super.notify(i, j);
            }
        }
);
        renders = new GUIRenders(gwindowdialogclient, 1.0F, 6F, 18F, 12F, true) {

            public void mouseButton(int i, boolean flag, float f, float f1)
            {
                super.mouseButton(i, flag, f, f1);
                if(!flag)
                    return;
                if(i == 1)
                {
                    if(animateMeshA == 0.0F && animateMeshT == 0.0F)
                    {
                        if(Actor.isValid(actorMesh))
                        {
                            if(f < win.dx / 2.0F)
                                actorAzimut += 45F;
                            else
                                actorAzimut -= 45F;
                            actorMesh.pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
                        }
                    } else
                    {
                        animateMeshA = animateMeshT = 0.0F;
                        if(Actor.isValid(actorMesh))
                        {
                            if(bGround)
                                actorAzimut = 30F;
                            else
                                actorAzimut = 90F;
                            actorMesh.pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
                        }
                    }
                } else
                if(i == 0)
                {
                    if(bGround)
                        actorAzimut = 75F;
                    else
                        actorAzimut = 135F;
                    f -= win.dx / 2.0F;
                    if(Math.abs(f) < win.dx / 16F)
                        animateMeshA = 0.0F;
                    else
                        animateMeshA = (-128F * f) / win.dx;
//                    if(!bGround)
//                    {
                        f1 -= win.dy / 2.0F;
                        if(Math.abs(f1) < win.dy / 16F)
                            animateMeshT = 0.0F;
                        else
                            animateMeshT = (-128F * f1) / win.dy;
//                    }
                }
            }

            public void windowShown()
            {
                super.windowShown();
                doUpdateMesh();
            }

        }
;
        camera3D = new Camera3D();
        camera3D.set(50F, 1.0F, 800F);
        render3D = new _Render3D(renders.renders, 1.0F);
        render3D.setCamera(camera3D);
        renders.hideWindow();
        LightEnvXY lightenvxy = new LightEnvXY();
        render3D.setLightEnv(lightenvxy);
        lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
        Vector3f vector3f = new Vector3f(-2F, 1.0F, -1F);
        vector3f.normalize();
        lightenvxy.sun().set(vector3f);
        resized();
    }

    public void _resized()
    {
        if(comboBox1 == null)
            return;
        comboBox1.setSize(comboBox1.parentWindow.win.dx - lookAndFeel().metric(2.0F), comboBox1.win.dy);
        comboBox2.setSize(comboBox2.parentWindow.win.dx - lookAndFeel().metric(2.0F), comboBox2.win.dy);
        wExport.setPos(wExport.parentWindow.win.dx - lookAndFeel().metric(8.4F), wExport.win.y);
        wExport.setSize(lookAndFeel().metric(6F), wExport.win.dy);
        wShow.setSize(wExport.win.x - lookAndFeel().metric(5.6F), wShow.win.dy);
        float f = renders.parentWindow.win.dy - lookAndFeel().metric(7F);
        if(f <= 10F)
            f = 10F;
        renders.setSize(renders.parentWindow.win.dx - lookAndFeel().metric(2.0F), f);
    }

    public void resized()
    {
        super.resized();
        _resized();
//        if(render3D != null)
//            render3D.setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
    }

    public void afterCreated()
    {
        super.afterCreated();
        close(false);
    }

    public WSelect(Builder builder1, GWindow gwindow)
    {
        curFilledType = -1;
        animateMeshA = 0.0F;
        animateMeshT = 0.0F;
        _orient = new Orient();
        actorAzimut = 0.0F;
        builder = builder1;
        firstShow = true;
        doNew(gwindow, 2.0F, 2.0F, 20F, 25F, true);
    }

    public GWindowTabDialogClient tabsClient;
    public GWindowComboControl comboBox1;
    public GWindowComboControl comboBox2;
    public int curFilledType;
    public GWindowButton wPrev;
    public GWindowButton wNext;
    public GWindowButton wShow;
    public GWindowButton wExport;
    GUIRenders renders;
    Camera3D camera3D;
    _Render3D render3D;
    String meshName;
    Actor actorMesh;
    boolean bGround;
    float animateMeshA;
    float animateMeshT;
    Builder builder;
    private Orient _orient;
    private float actorAzimut;
    private boolean firstShow;
}
