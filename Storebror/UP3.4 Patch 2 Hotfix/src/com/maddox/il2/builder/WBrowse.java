package com.maddox.il2.builder;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.GCaption;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.GUIRenders;
import com.maddox.il2.engine.LightEnvXY;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.Renders;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.rts.Property;

public class WBrowse extends GWindowFramed
{
    class _Render3D extends Render
    {

        public void preRender()
        {
            if(!Actor.isValid(actorMesh[itemIndx]) && actorMesh[itemIndx] != null && meshName[itemIndx] != null)
                setMesh(itemIndx, meshName[itemIndx], bGround);
            if(Actor.isValid(actorMesh[itemIndx]))
                actorMesh[itemIndx].draw.preRender(actorMesh[itemIndx]);
        }

        public void render()
        {
            if(Actor.isValid(actorMesh[itemIndx]))
            {
                Render.prepareStates();
                actorMesh[itemIndx].draw.render(actorMesh[itemIndx]);
            }
        }

        int itemIndx;

        public _Render3D(Renders renders1, float f)
        {
            super(renders1, f);
            itemIndx = _itemIndx;
            setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            useClearColor(itemIndx == 16);
            useClearStencil(true);
        }
    }


    public void windowShown()
    {
        builder.mViewerItem.bChecked = true;
        super.windowShown();
        doUpdateMesh();
    }

    public void windowHidden()
    {
        if(!isViewerCollapsed())
            viewerCollapse();
        builder.mViewerItem.bChecked = false;
        super.windowHidden();
    }

    public void viewerExpand()
    {
        for(int i = 0; i < 44; i++)
            renders[i].showWindow();

        clientWindow.parentWindow.setSize(clientWindow.parentWindow.win.dx, bWinSizeY);
        wShow.cap = new GCaption(Plugin.i18n("Collapse"));
        bSizable = true;
        bCollapsed = false;
        int j = comboBox2.getSelected();
        comboBox2.setSelected(-1, false, false);
        comboBox2.setSelected(j, true, true);
        resized();
    }

    public void viewerCollapse()
    {
        for(int i = 0; i < 44; i++)
            renders[i].hideWindow();

        bWinSizeY = clientWindow.parentWindow.win.dy;
        clientWindow.parentWindow.setSize(clientWindow.parentWindow.win.dx, comboBox2.win.dy * 4F);
        wShow.cap = new GCaption(Plugin.i18n("Expand"));
        bSizable = false;
        bCollapsed = true;
        resized();
    }

    public boolean isViewerCollapsed()
    {
        return bCollapsed;
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

    public void setMesh(int i, String s, boolean flag)
    {
        if(isViewerCollapsed())
            return;
        if(meshName[i] == s && Actor.isValid(actorMesh[i]))
            return;
        if(Actor.isValid(actorMesh[i]))
            actorMesh[i].destroy();
        actorMesh[i] = null;
        meshName[i] = s;
        bGround = flag;
        if(s == null)
            return;
        double d = 20D;
        actorMesh[i] = new ActorSimpleMesh(s);
        d = actorMesh[i].mesh().visibilityR();
        if(flag)
        {
            actorAzimut = 30F;
            actorMesh[i].pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
            d *= Math.cos(0.78539816339744828D) / Math.sin(((double)camera3D[i].FOV() * 3.1415926535897931D) / 180D / 2D);
            camera3D[i].pos.setAbs(new Point3d(d, 0.0D, d * 0.90000000000000002D), new Orient(180F, -45F, 0.0F));
        } else
        {
            actorAzimut = 90F;
            actorMesh[i].pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
            d *= Math.cos(0.26179938779914941D) / Math.sin(((double)camera3D[i].FOV() * 3.1415926535897931D) / 180D / 2D);
            camera3D[i].pos.setAbs(new Point3d(d, 0.0D, 0.0D), new Orient(180F, 0.0F, 0.0F));
        }
        camera3D[i].pos.reset();
        doUpdateMesh();
    }

    public void created()
    {
        bAlwaysOnTop = true;
        super.created();
        title = "Object Browser";
        clientWindow = create(new GWindowDialogClient());
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)clientWindow;
        gwindowdialogclient.addControl(comboBox1 = new GWindowComboControl(gwindowdialogclient, 1.0F, 1.0F, 18F) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                    comboBox2.setSelected(allTitles[comboBox1.getSelected()], true, true);
                return super.notify(i, j);
            }

        }
);
        comboBox1.setEditable(false);
        comboBox1.listCountLines = comboBox1.listVisibleLines = 32;
        gwindowdialogclient.addControl(comboBox2 = new GWindowComboControl(gwindowdialogclient, 1.0F, 1.0F, 18F) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    int k = 0;
                    if(Plugin.builder.wSelect.comboBox1.size() > k)
                    {
                        int l;
                        for(l = Plugin.builder.wSelect.comboBox1.size() - 1; l >= 0; l--)
                            if(Plugin.builder.wSelect.comboBox1.get(l).equals(Plugin.i18n("buildings")))
                                break;

                        int i1 = comboBox2.getSelected();
                        Plugin.builder.wSelect.comboBox1.setSelected(l, true, true);
                        Plugin.builder.wSelect.comboBox2.setSelected(i1, true, true);
                        comboBox1.setSelected(comboBox1.size() - 1, true, false);
                        for(int j1 = 1; j1 < comboBox1.size(); j1++)
                        {
                            if(i1 < allTitles[j1 - 1] || i1 >= allTitles[j1])
                                continue;
                            comboBox1.setSelected(j1 - 1, true, false);
                            break;
                        }

                    }
                }
                return super.notify(i, j);
            }

        }
);
        comboBox2.setEditable(false);
        comboBox2.listCountLines = comboBox2.listVisibleLines = 32;
        gwindowdialogclient.addControl(wShow = new GWindowButton(gwindowdialogclient, 1.0F, 1.0F, 18F, 1.4F, Plugin.i18n("Expand"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                    if(bCollapsed)
                        viewerExpand();
                    else
                        viewerCollapse();
                return super.notify(i, j);
            }

        }
);
        for(_itemIndx = 0; _itemIndx < 44; _itemIndx++)
        {
            renders[_itemIndx] = new GUIRenders(gwindowdialogclient, 1.0F, 3F, 18F, 12F, true) {

                public void mouseButton(int i, boolean flag, float f, float f1)
                {
                    super.mouseButton(i, flag, f, f1);
                    if(!flag)
                        return;
                    int j = (comboBox2.getSelected() - 16) + thisIndx;
                    if(i == 1)
                    {
                        Actor actor = builder.selectedActor();
                        if(actor != null && comboBox2.getSelected() == j)
                        {
                            Plugin plugin = (Plugin)Property.value(actor, "builderPlugin");
                            if(plugin instanceof PlMisHouse)
                                builder.setSelected(null);
                        }
                    } else
                    if(i == 0 && j >= 0 && j < comboBox2.size())
                        if(comboBox2.getSelected() == j)
                        {
                            String s = comboBox2.get(j);
                            if(s.startsWith("(\u2665) "))
                                s = s.substring(4, s.length());
                            else
                                s = "(\u2665) " + s;
                            comboBox2.remove(j);
                            comboBox2.add(j, s);
                            comboBox2.setValue(s);
                        } else
                        {
                            comboBox2.setSelected(j, true, true);
                        }
                }

                public void windowShown()
                {
                    super.windowShown();
                    doUpdateMesh();
                }

                int thisIndx;

            
            {
                thisIndx = _itemIndx;
            }
            }
;
            camera3D[_itemIndx] = new Camera3D();
            camera3D[_itemIndx].set(50F, 1.0F, 800F);
            render3D[_itemIndx] = new _Render3D(renders[_itemIndx].renders, 1.0F);
            render3D[_itemIndx].setCamera(camera3D[_itemIndx]);
            LightEnvXY lightenvxy = new LightEnvXY();
            render3D[_itemIndx].setLightEnv(lightenvxy);
            lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
            Vector3f vector3f = new Vector3f(-2F, 1.0F, -1F);
            vector3f.normalize();
            lightenvxy.sun().set(vector3f);
        }

        resized();
        viewerCollapse();
    }

    public void _resized()
    {
        if(comboBox2 == null)
            return;
        comboBox1.setSize(comboBox1.parentWindow.win.dx * 0.35F, comboBox1.win.dy);
        comboBox2.setPos(comboBox2.parentWindow.win.dx * 0.35F + lookAndFeel().metric(2.0F), comboBox2.win.y);
        comboBox2.setSize(comboBox2.parentWindow.win.dx * 0.35F, comboBox2.win.dy);
        wShow.setPos(comboBox2.parentWindow.win.dx * 0.7F + lookAndFeel().metric(3F), comboBox2.win.y - 2.0F);
        wShow.setSize(wShow.parentWindow.win.dx * 0.3F - lookAndFeel().metric(4F), wShow.win.dy);
        float f = lookAndFeel().metric(3F);
        float f1 = clientWindow.win.dx / 11F;
        float f2 = (clientWindow.win.dy - f) / 4F;
        int i = 0;
        for(int j = 0; j < 4; j++)
        {
            for(int k = 0; k < 11; k++)
            {
                renders[i].setPosSize(f1 * (float)k, f + f2 * (float)j, f1, f2);
                i++;
            }

        }

    }

    public void resized()
    {
        super.resized();
        _resized();
    }

    public void afterCreated()
    {
        super.afterCreated();
        close(false);
    }

    public WBrowse(Builder builder1, GWindow gwindow)
    {
        curFilledType = -1;
        actorAzimut = 0.0F;
        builder = builder1;
        bCollapsed = true;
        renders = new GUIRenders[44];
        camera3D = new Camera3D[44];
        render3D = new _Render3D[44];
        meshName = new String[44];
        actorMesh = new ActorSimpleMesh[44];
        doNew(gwindow, 2.0F, 2.0F, 70F, 50F, true);
    }

    public GWindowComboControl comboBox1;
    public int allTitles[];
    public GWindowComboControl comboBox2;
    public GWindowButton wShow;
    public int curFilledType;
    GUIRenders renders[];
    Camera3D camera3D[];
    _Render3D render3D[];
    String meshName[];
    ActorSimpleMesh actorMesh[];
    private int _itemIndx;
    boolean bGround;
    Builder builder;
    private boolean bCollapsed;
    private float bWinSizeY;
    private float actorAzimut;
}