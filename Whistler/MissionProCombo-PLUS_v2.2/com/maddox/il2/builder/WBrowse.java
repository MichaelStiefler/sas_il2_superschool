package com.maddox.il2.builder;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.*;
import com.maddox.il2.engine.*;
//import com.maddox.il2.objects.ActorSimpleHMesh;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.rts.Property;
import java.io.*;
import java.util.*;

public class WBrowse extends GWindowFramed
{
    class _Render3D extends Render
    {

        public void preRender()
        {
            if(!Actor.isValid(actorMesh[itemIndx]) && actorMesh[itemIndx] != null && meshName[itemIndx] != null)
                setMesh(itemIndx, meshName[itemIndx], bGround);
            if(Actor.isValid(actorMesh[itemIndx]))
            {
                actorMesh[itemIndx].draw.preRender(actorMesh[itemIndx]);
            }
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
//            setClearColor(GWindowLookAndFeel.getUIBackgroundColor());
            setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            useClearColor(itemIndx == 16);
            useClearStencil(true);
        }
    }

//    public boolean isMeshVisible()
//    {
//        return renders.bVisible;
//    }

//    public HierMesh getHierMesh()
//    {
//        if(!Actor.isValid(actorMesh))
//            return null;
//        if(!(actorMesh instanceof ActorHMesh))
//            return null;
//        else
//            return ((ActorHMesh)actorMesh).hierMesh();
//    }

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
        for(int itemIndx = 0; itemIndx < 44; itemIndx++)
            renders[itemIndx].showWindow();
        clientWindow.parentWindow.setSize(clientWindow.parentWindow.win.dx, bWinSizeY);
        wShow.cap = new GCaption(Plugin.i18n("Collapse"));
        bSizable = true;
        bCollapsed = false;
        int itemCurrent = comboBox2.getSelected();
        comboBox2.setSelected(-1, false, false);
        comboBox2.setSelected(itemCurrent, true, true);
        resized();
    }

    public void viewerCollapse()
    {
        for(int itemIndx = 0; itemIndx < 44; itemIndx++)
            renders[itemIndx].hideWindow();
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

//    public void setMesh(String s, boolean flag)
//    {
//        setMesh(s, flag, null, null);
//    }

//    public void setMesh(String s, boolean flag, Class class1, Regiment regiment)
    public void setMesh(int itemIndx, String s, boolean flag)
    {
        if(isViewerCollapsed())
            return;
        if(meshName[itemIndx] == s && Actor.isValid(actorMesh[itemIndx]))
            return;
        if(Actor.isValid(actorMesh[itemIndx]))
            actorMesh[itemIndx].destroy();
        actorMesh[itemIndx] = null;
        meshName[itemIndx] = s;
        bGround = flag;
        if(s == null)
            return;
        double d = 20D;
//        if(s.toLowerCase().endsWith(".sim"))
//        {
//            actorMesh = new ActorSimpleMesh(s);
            actorMesh[itemIndx] = new ActorSimpleMesh(s);
//            d = ((ActorMesh)actorMesh)[itemIndx].mesh().visibilityR();
            d = actorMesh[itemIndx].mesh().visibilityR();
//        } else
//        {
//            actorMesh = new ActorSimpleHMesh(s);
//            actorMesh[itemIndx] = new ActorSimpleHMesh(s);
//            d = ((ActorHMesh)actorMesh)[itemIndx].hierMesh().visibilityR();
//        }
        if(flag)
        {
            actorAzimut = 30F;
            actorMesh[itemIndx].pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
            d *= Math.cos(0.78539816339744828D) / Math.sin(((double)camera3D[itemIndx].FOV() * 3.1415926535897931D) / 180D / 2D);
            camera3D[itemIndx].pos.setAbs(new Point3d(d, 0.0D, d * 0.90000000000000002D), new Orient(180F, -45F, 0.0F));
        } else
        {
            actorAzimut = 90F;
            actorMesh[itemIndx].pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
            d *= Math.cos(0.26179938779914941D) / Math.sin(((double)camera3D[itemIndx].FOV() * 3.1415926535897931D) / 180D / 2D);
            camera3D[itemIndx].pos.setAbs(new Point3d(d, 0.0D, 0.0D), new Orient(180F, 0.0F, 0.0F));
        }
        camera3D[itemIndx].pos.reset();
        doUpdateMesh();
    }

    public void created()
    {
        bAlwaysOnTop = true;
        super.created();
//        title = Plugin.i18n("Object");
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
                    int catIndx = 0;
                    if(Plugin.builder.wSelect.comboBox1.size() > catIndx)
                    {
                        for(catIndx = Plugin.builder.wSelect.comboBox1.size() - 1; catIndx >= 0; catIndx--)
                        {
                           if(Plugin.builder.wSelect.comboBox1.get(catIndx).equals(Plugin.i18n("buildings")))
                               break;
                        }

                        int itemCurrent = comboBox2.getSelected();
                        Plugin.builder.wSelect.comboBox1.setSelected(catIndx, true, true);
                        Plugin.builder.wSelect.comboBox2.setSelected(itemCurrent, true, true);

                        comboBox1.setSelected(comboBox1.size() - 1, true, false);
                        for(int k = 1; k < comboBox1.size(); k++)
                        {
                            if(itemCurrent >= allTitles[k - 1] && itemCurrent < allTitles[k])
                            {
                                comboBox1.setSelected(k - 1, true, false);
                                break;
                            }
                        }

//                    if(Actor.isValid(builder.selectedPath()))
//                    {
//                        Plugin plugin = (Plugin)Property.value(builder.selectedPath(), "builderPlugin");
//                        plugin.changeType();
//                    } else
//                    if(Actor.isValid(builder.selectedActor()))
//                    {
//                        Plugin plugin1 = (Plugin)Property.value(builder.selectedActor(), "builderPlugin");
//                        if(plugin1 != null)
//                            plugin1.changeType();
//                        else
//                        if(builder.bMultiSelect)
//                        {
//                            Plugin plugin2 = Plugin.getPlugin("MapActors");
//                            plugin2.changeType();
//                        }
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
                {
                    if(bCollapsed)
                        viewerExpand();
                    else
                        viewerCollapse();
                }
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
                    int favKey = comboBox2.getSelected() - 16 + thisIndx;
                    if(i == 1)
                    {
                        Actor actor = builder.selectedActor();
                        if(actor != null && comboBox2.getSelected() == favKey)
                        {
                            Plugin plugin = (Plugin)Property.value(actor, "builderPlugin");
                            if(plugin instanceof PlMisHouse)
                                builder.setSelected(null);
                        }
                    } else
                    if(i == 0)
                    {
                        if(favKey >= 0 && favKey < comboBox2.size())
                        {
                            if(comboBox2.getSelected() == favKey)
                            {
                                String favTxt = comboBox2.get(favKey);
                                if(favTxt.startsWith("(\u2665) "))
                                    favTxt = favTxt.substring(4, favTxt.length());
                                else
                                    favTxt = "(\u2665) " + favTxt;
                                comboBox2.remove(favKey);
                                comboBox2.add(favKey, favTxt);
                                comboBox2.setValue(favTxt);
                            } else
                            {
                                comboBox2.setSelected(favKey, true, true);
                            }
                        }
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
//        if(comboBox1 == null)
//            return;
        if(comboBox2 == null)
            return;
        comboBox1.setSize(comboBox1.parentWindow.win.dx * 0.35F, comboBox1.win.dy);
        comboBox2.setPos(comboBox2.parentWindow.win.dx * 0.35F + lookAndFeel().metric(2.0F), comboBox2.win.y);
        comboBox2.setSize(comboBox2.parentWindow.win.dx * 0.35F, comboBox2.win.dy);
//        float f = renders.parentWindow.win.dy - lookAndFeel().metric(7F);
//        if(f <= 10F)
//            f = 10F;
//        renders.setSize(renders.parentWindow.win.dx - lookAndFeel().metric(2.0F), f);
        wShow.setPos(comboBox2.parentWindow.win.dx * 0.70F + lookAndFeel().metric(3.0F), comboBox2.win.y - 2.0F);
        wShow.setSize(wShow.parentWindow.win.dx * 0.30F - lookAndFeel().metric(4.0F), wShow.win.dy);

        float chunkY = lookAndFeel().metric(3.0F);
        float chunkH = clientWindow.win.dx / 11F;
        float chunkV = (clientWindow.win.dy - chunkY) / 4F;

        int itemIndx = 0;
        for(int col = 0; col < 4; col++)
        {
            for(int row = 0; row < 11; row++)
           {
                renders[itemIndx].setPosSize(chunkH * (float)row, chunkY + chunkV * (float)col, chunkH, chunkV);
                itemIndx++;
            }
        }
/*
        for(int itemIndx = 0; itemIndx < 44; itemIndx++)
        {
            if(itemIndx > 32)
            {
                renders[itemIndx].setPos(chunkH * (float)(itemIndx - 33), renders[itemIndx - 33].win.y + chunkV * 3F);
                renders[itemIndx].setSize(chunkH, chunkV);
            }
            else if(itemIndx > 21)
            {
                renders[itemIndx].setPos(chunkH * (float)(itemIndx - 22), renders[itemIndx - 22].win.y + chunkV * 2F);
                renders[itemIndx].setSize(chunkH, chunkV);
            }
            else if(itemIndx > 10)
            {
                renders[itemIndx].setPos(chunkH * (float)(itemIndx - 11), renders[itemIndx - 11].win.y + chunkV * 1F);
                renders[itemIndx].setSize(chunkH, chunkV);
            }
            else
            {
                renders[itemIndx].setPos(chunkH * (float)(itemIndx - 0), renders[itemIndx - 0].win.y + chunkV * 0F);
                renders[itemIndx].setSize(chunkH, chunkV);
            }
        }
*/
    }

    public void resized()
    {
        super.resized();
        _resized();
/*
        for(int itemIndx = 0; itemIndx < 44; itemIndx++)
        {
            if(render3D[itemIndx] != null)
            {
                render3D[itemIndx].setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
                render3D[itemIndx].useClearColor(itemIndx == 16);
            }
        }
*/
    }

    public void afterCreated()
    {
        super.afterCreated();
        close(false);
    }

    public WBrowse(Builder builder1, GWindow gwindow)
    {
        curFilledType = -1;
        _orient = new Orient();
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
    public int[] allTitles;
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
    private Orient _orient;
    private float actorAzimut;
}
