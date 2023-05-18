package com.maddox.il2.gui;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.*;
import com.maddox.il2.objects.ActorSimpleHMesh;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.rts.*;
import com.maddox.util.HashMapExt;
import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class GUIAirViewer extends GameState
{
    public class DialogClient extends GUIDialogClient
    {
        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == cAircraft)
            {
//                quikPlane = airName();
                for(int itemIndx = 0; itemIndx < 20; itemIndx++)
                    setMesh(itemIndx);
                return true;
            } else
            {
                if(gwindow == bRotate)
                {
                    actorAzimut += 45F;
                    for(int itemIndx = 0; itemIndx < 20; itemIndx++)
                        if(Actor.isValid(actorMesh[itemIndx]))
                            actorMesh[itemIndx].pos.setAbs(new Orient((isPlaceholder(itemIndx) ? actorAzimut - 180F : actorAzimut), 0.0F, 0.0F));
                } else
                if(gwindow == bBack)
                {
                    int currentSelected = cAircraft.getSelected();
                    airNames = new ArrayList();
//                    quikListPlane = new ArrayList();
                    quikListName = new ArrayList();
                    cAircraft.clear(false);
                    for(int itemIndx = 0; itemIndx < 20; itemIndx++)
                        destroyMesh(itemIndx);
                    Main.stateStack().pop();
                    GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
                    guiairarming.cAircraft.setSelected(currentSelected, true, true);
                    return true;
                }
            }
            return super.notify(gwindow, i, j);
        }

        public void render()
        {
            super.render();
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(96F), y1024(656F), x1024(320F), y1024(48F), 0, i18n("neta.Apply"));
            draw(x1024(608F), y1024(656F), x1024(320F), y1024(48F), 2, i18n("Rotate"));

            float chunkPadX = x1024(32F);
            float chunkPadY = y1024(32F);
            float chunkHor = x1024(192F);
            float chunkVer = y1024(125F);
            float chunkTxt = y1024(24F);
            float chunkSep = x1024(2F);
            float chunkMar = chunkSep * 3F;

            GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
            guilookandfeel.drawBevel(this, chunkPadX - chunkSep, chunkPadY - chunkSep, chunkHor * 5F + chunkSep * 2F, chunkVer * 4F + chunkTxt * 4F + chunkSep * 2F, guilookandfeel.bevelComboDown, guilookandfeel.basicelements);

            int itemIndx = 0;
            for(int col = 0; col < 4; col++)
            {
                for(int row = 0; row < 5; row++)
                {
                    String planeName = planeName(itemIndx);
                    drawLines(chunkPadX + chunkMar + chunkHor * (float)row, chunkPadY + chunkVer * (float)(col + 1) + chunkSep + chunkTxt * (float)col, planeName, 0, planeName.length(), chunkHor - chunkMar * 2F, root.C.font.height, 1);
                    itemIndx++;
                }
            }
/*
            for(int itemIndx = 0; itemIndx < 20; itemIndx++)
            {
                String planeName = planeName(itemIndx);
                if(itemIndx > 14)
                    drawLines(chunkPadX + chunkMar + chunkHor * (float)(itemIndx - 15), chunkPadY + chunkVer * 4F + chunkSep + chunkTxt * 3F, planeName, 0, planeName.length(), chunkHor - chunkMar * 2F, root.C.font.height, 1);
                else if(itemIndx > 9)
                    drawLines(chunkPadX + chunkMar + chunkHor * (float)(itemIndx - 10), chunkPadY + chunkVer * 3F + chunkSep + chunkTxt * 2F, planeName, 0, planeName.length(), chunkHor - chunkMar * 2F, root.C.font.height, 1);
                else if(itemIndx > 4)
                    drawLines(chunkPadX + chunkMar + chunkHor * (float)(itemIndx - 5), chunkPadY + chunkVer * 2F + chunkSep + chunkTxt * 1F, planeName, 0, planeName.length(), chunkHor - chunkMar * 2F, root.C.font.height, 1);
                else
                    drawLines(chunkPadX + chunkMar + chunkHor * (float)(itemIndx - 0), chunkPadY + chunkVer * 1F + chunkSep + chunkTxt * 0F, planeName, 0, planeName.length(), chunkHor - chunkMar * 2F, root.C.font.height, 1);
            }
*/
            setCanvasColorWHITE();
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            cAircraft.setPosSize(x1024(330F), y1024(656F), x1024(364F), M(1.7F));

//            GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
//            GBevel gbevel = guilookandfeel.bevelComboDown;
//            renders.setPosSize(x1024(32F) + gbevel.L.dx, y1024(32F) + gbevel.T.dy, x1024(564F) - gbevel.L.dx - gbevel.R.dx, y1024(367F) - gbevel.T.dy - gbevel.B.dy);

            float chunkPadX = x1024(32F);
            float chunkPadY = y1024(32F);
            float chunkHor = x1024(192F);
            float chunkVer = y1024(125F);
            float chunkTxt = y1024(24F);

            int itemIndx = 0;
            for(int col = 0; col < 4; col++)
            {
                for(int row = 0; row < 5; row++)
                {
                    renders[itemIndx].setPosSize(chunkPadX + chunkHor * (float)row, chunkPadY + (chunkVer + chunkTxt) * (float)col, chunkHor, chunkVer);
                    itemIndx++;
                }
            }
/*
            for(int itemIndx = 0; itemIndx < 20; itemIndx++)
            {
                if(itemIndx > 14)
                    renders[itemIndx].setPosSize(chunkPadX + chunkHor * (float)(itemIndx - 15), chunkPadY + (chunkVer + chunkTxt) * 3F, chunkHor, chunkVer);
                else if(itemIndx > 9)
                    renders[itemIndx].setPosSize(chunkPadX + chunkHor * (float)(itemIndx - 10), chunkPadY + (chunkVer + chunkTxt) * 2F, chunkHor, chunkVer);
                else if(itemIndx > 4)
                    renders[itemIndx].setPosSize(chunkPadX + chunkHor * (float)(itemIndx - 5), chunkPadY + (chunkVer + chunkTxt) * 1F, chunkHor, chunkVer);
                else
                    renders[itemIndx].setPosSize(chunkPadX + chunkHor * (float)(itemIndx - 0), chunkPadY + (chunkVer + chunkTxt) * 0F, chunkHor, chunkVer);
            }
*/
            bBack.setPosC(x1024(56F), y1024(680F));
            bRotate.setPosC(x1024(968F), y1024(680F));
        }

        public DialogClient()
        {
        }
    }

    class _Render3D extends Render
    {
        public void preRender()
        {
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
            if(itemIndx == 7)
                setClearColor(new Color4f(0.79F, 0.82F, 0.82F, 1.0F));
            else
                setClearColor(new Color4f(0.69F, 0.73F, 0.73F, 1.0F));

/*
            if(itemIndx == 7)
                setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            else if(itemIndx % 2 == 0)
                setClearColor(new Color4f(0.5882353F, 0.6470588F, 0.6666667F, 1.0F));
            else
                setClearColor(new Color4f(0.6862745F, 0.7333333F, 0.7333333F, 1.0F));
*/

/*
    setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F)); // blue
    setClearColor(new Color4f(0.6862745F, 0.7333333F, 0.7333333F, 1.0F)); // light gray
    setClearColor(new Color4f(0.7215686F, 0.6784314F, 0.627451F, 1.0F)); // orange
    setClearColor(new Color4f(0.5882353F, 0.6470588F, 0.6666667F, 1.0F)); // dark grey
    setClearColor(new Color4f(0.5960785F, 0.6235294F, 0.6627451F, 1.0F)); // dark red
    setClearColor(GWindowLookAndFeel.getUIBackgroundColor());
*/

//            useClearColor(itemIndx == 7);
            useClearStencil(true);
        }
    }

//    private String airName(int itemIndx)
//    {
//        return (String)airNames.get(itemIndx);
//    }

    private String planeName(int itemIndx)
    {
        int i = cAircraft.getSelected() + itemIndx - 7;
        String name = (i < 0 || i > cAircraft.size() - 1) ? "" : (String)quikListName.get(i);
        return name.trim().startsWith("*") ? "" : name;
    }

    private boolean isPlaceholder(int itemIndx)
    {
        int i = cAircraft.getSelected() + itemIndx - 7;
        String name = (i < 0 || i > cAircraft.size() - 1) ? "" : (String)quikListName.get(i);
        return name.trim().startsWith("*") ? true : false;
    }

    public void _enter()
    {
        try
        {
            for(int l = 0; l < quikListName.size(); l++)
                cAircraft.add((String)quikListName.get(l));
            cAircraft.setSelected(-1, false, false);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            Main.stateStack().pop();
            return;
        }
        dialogClient.setPosSize();
        client.activateWindow();
    }

    public void _leave()
    {
        client.hideWindow();
    }

    private void createRender()
    {
        for(_itemIndx = 0; _itemIndx < 20; _itemIndx++)
        {
            renders[_itemIndx] = new GUIRenders(dialogClient) {

                public void mouseButton(int i, boolean flag, float f, float f1)
                {
                    super.mouseButton(i, flag, f, f1);
                    if(!flag)
                        return;
                    if(i == 0)
                    {
                        int favKey = cAircraft.getSelected() - 7 + thisIndx;
                        if(favKey >= 0 && favKey < cAircraft.size())
                        {
                            if(cAircraft.getSelected() != favKey)
                                cAircraft.setSelected(favKey, true, true);
                        }
                    }
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
    }

    public static void removeBody(HierMesh hiermesh, int i)
    {
        removeCombo(hiermesh, "Pilot" + i);
        removeCombo(hiermesh, "Head" + i);
        removeCombo(hiermesh, "HMask" + i);
        removeCombo(hiermesh, "Pilot" + i + "a");
        removeCombo(hiermesh, "Head" + i + "a");
        removeSingle(hiermesh, "Pilot" + i + "_FAK");
        removeSingle(hiermesh, "Head" + i + "_FAK");
        removeSingle(hiermesh, "Pilot" + i + "_FAL");
        removeSingle(hiermesh, "Head" + i + "_FAL");
    }

    protected static void removeCombo(HierMesh hiermesh, String s)
    {
        if(hiermesh.chunkFindCheck(s + "_D0") != -1)
            hiermesh.chunkVisible(s + "_D0", false);
        if(hiermesh.chunkFindCheck(s + "_D1") != -1)
            hiermesh.chunkVisible(s + "_D1", false);
    }

    protected static void removeSingle(HierMesh hiermesh, String s)
    {
        if(hiermesh.chunkFindCheck(s) != -1)
            hiermesh.chunkVisible(s, false);
    }

    private void setMesh(int itemIndx)
    {
        destroyMesh(itemIndx);
        int i = cAircraft.getSelected() + itemIndx - 7;
        if(i < 0 || i > cAircraft.size() - 1)
            return;
        try
        {
            Class class1 = (Class)Property.value(airNames.get(i), "airClass", null);
            String s2 = Aircraft.getPropertyMesh(class1, quikCountry);
            actorMesh[itemIndx] = new ActorSimpleHMesh(s2);
            for(int i1 = 1; i1 < 10; i1++)
                removeBody(actorMesh[itemIndx].hierMesh(), i1);
            double d = actorMesh[itemIndx].hierMesh().visibilityR();
            Aircraft.prepareMeshCamouflage(s2, actorMesh[itemIndx].hierMesh(), class1, quikRegiment);
            actorMesh[itemIndx].pos.setAbs(new Orient((isPlaceholder(itemIndx) ? actorAzimut - 180F : actorAzimut), 0.0F, 0.0F));
            actorMesh[itemIndx].pos.reset();
            d *= Math.cos(0.26179938779914941D) / Math.sin(((double)camera3D[itemIndx].FOV() * 3.1415926535897931D) / 180D / 2D);
            camera3D[itemIndx].pos.setAbs(new Point3d(d, 0.0D, 0.0D), new Orient(180F, 0.0F, 0.0F));
            camera3D[itemIndx].pos.reset();

//        prepareMesh(itemIndx);

            PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(class1, quikRegiment.country());
            if(paintscheme != null)
                paintscheme.prepare(class1, actorMesh[itemIndx].hierMesh(), quikRegiment, quikSquadron, quikWing, quikCurPlane, false);

//        prepareSkin(itemIndx);
//        preparePilot(itemIndx);

        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void destroyMesh(int itemIndx)
    {
        if(Actor.isValid(actorMesh[itemIndx]))
            actorMesh[itemIndx].destroy();
        actorMesh[itemIndx] = null;
    }

    public GUIAirViewer(GWindowRoot gwindowroot)
    {
        super(73);
        airNames = new ArrayList();
//        quikArmy = 1;
//        quikPlane = "Il-2_M3";
        quikCurPlane = 0;
        quikRegiment = null;
        quikSquadron = 0;
        quikWing = 0;
//        quikListPlane = new ArrayList();
        quikListName = new ArrayList();
//        _orient = new Orient();
        actorAzimut = 270F;
        resourceBundle = ResourceBundle.getBundle("i18n/gui", RTSConf.cur.locale, LDRres.loader());
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("Hangar");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        cAircraft = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cAircraft.setEditable(false);
        cAircraft.listVisibleLines = 24;
        bBack = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bRotate = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        renders = new GUIRenders[20];
        camera3D = new Camera3D[20];
        render3D = new _Render3D[20];
        actorMesh = new ActorSimpleHMesh[20];
        createRender();
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GWindowComboControl cAircraft;
    private ResourceBundle resourceBundle;
    public GUIButton bBack;
    public GUIButton bRotate;
    public ArrayList airNames;
//    protected int quikArmy;
//    protected String quikPlane;
    protected int quikCurPlane;
    protected String quikCountry;
    protected Regiment quikRegiment;
    protected int quikSquadron;
    protected int quikWing;
//    protected ArrayList quikListPlane;
    protected ArrayList quikListName;
    public GUIRenders renders[];
    public Camera3D camera3D[];
    public _Render3D render3D[];
    public ActorHMesh actorMesh[];
    private int _itemIndx;
//    private Orient _orient;
    private float actorAzimut;
}
