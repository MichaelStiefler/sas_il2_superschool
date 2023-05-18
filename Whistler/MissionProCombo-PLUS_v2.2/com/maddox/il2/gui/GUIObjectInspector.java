package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.*;
import com.maddox.util.NumberTokenizer;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GUIObjectInspector extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
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
            if(gwindow == wPrev)
            {
                Main.stateStack().pop();
                return true;
            }
            if(gwindow == wView)
            {
                GUIObjectInspector.type = GUIObjectInspector.type;
                GUIObjectInspector.s_object = wTable.selectRow;
                GUIObjectInspector.s_scroll = wTable.vSB.pos();
                Main.stateStack().change(23);
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
//            GUISeparate.draw(this, GColor.Gray, x1024(320F), y1024(32F), 2.0F, y1024(655F));
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(40F), y1024(40F), x1024(240F), y1024(32F), 0, i18n("obj.SelectCountry"));
            draw(x1024(330F), y1024(110F), x1024(248F), y1024(32F), 0, i18n("obj.Description"));
            draw(x1024(104F), y1024(652F), x1024(192F), y1024(48F), 0, i18n("obj.Back"));
            draw(x1024(730F), y1024(652F), x1024(192F), y1024(48F), 2, i18n("obj.View"));
            setCanvasColorWHITE();
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            wPrev.setPosC(x1024(64F), y1024(676F));
            wView.setPosC(x1024(960F), y1024(676F));
            wCountry.setPosSize(x1024(40F), y1024(80F), x1024(250F), M(2.0F));
            wTable.setPosSize(x1024(40F), y1024(150F), x1024(250F), y1024(470F));
            wScrollDescription.setPosSize(x1024(330F), y1024(150F), x1024(655F), y1024(470F));
        }

        public DialogClient()
        {
        }
    }

    public class Descript extends GWindowDialogClient
    {

        public void render()
        {
            String s = null;
            if(wTable.selectRow >= 0)
            {
                s = ((ObjectInfo)wTable.objects.get(wTable.selectRow)).info;
                if(s != null && s.length() == 0)
                    s = null;
            }
            if(s != null)
            {
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                setCanvasFont(0);
                setCanvasColorBLACK();
                root.C.clip.y += gbevel.T.dy;
                root.C.clip.dy -= gbevel.T.dy + gbevel.B.dy;
                drawLines(gbevel.L.dx + 2.0F, gbevel.T.dy + 2.0F, s, 0, s.length(), win.dx - gbevel.L.dx - gbevel.R.dx - 4F, root.C.font.height);
            }
        }

        public void computeSize()
        {
            String s = null;
            if(wTable.selectRow >= 0)
            {
                s = ((ObjectInfo)wTable.objects.get(wTable.selectRow)).info;
                if(s != null && s.length() == 0)
                    s = null;
            }
            if(s != null)
            {
                win.dx = parentWindow.win.dx;
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                setCanvasFont(0);
                int i = computeLines(s, 0, s.length(), win.dx - gbevel.L.dx - gbevel.R.dx - 4F);
                win.dy = root.C.font.height * (float)i + gbevel.T.dy + gbevel.B.dy + 4F;
                if(win.dy > parentWindow.win.dy)
                {
                    win.dx = parentWindow.win.dx - lookAndFeel().getVScrollBarW();
                    int j = computeLines(s, 0, s.length(), win.dx - gbevel.L.dx - gbevel.R.dx - 4F);
                    win.dy = root.C.font.height * (float)j + gbevel.T.dy + gbevel.B.dy + 4F;
                }
            } else
            {
                win.dx = parentWindow.win.dx;
                win.dy = parentWindow.win.dy;
            }
        }

        public Descript()
        {
        }
    }

    public class ScrollDescript extends GWindowScrollingDialogClient
    {

        public void created()
        {
            fixed = wDescript = (Descript)create(new Descript());
            fixed.bNotify = true;
            bNotify = true;
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

        public void resized()
        {
            if(wDescript != null)
                wDescript.computeSize();
            super.resized();
            if(vScroll.isVisible())
            {
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                vScroll.setPos(win.dx - lookAndFeel().getVScrollBarW() - gbevel.R.dx, gbevel.T.dy);
                vScroll.setSize(lookAndFeel().getVScrollBarW(), win.dy - gbevel.T.dy - gbevel.B.dy);
            }
        }

        public void render()
        {
            setCanvasColorWHITE();
            GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
            lookAndFeel().drawBevel(this, 0.0F, 0.0F, win.dx, win.dy, gbevel, ((GUILookAndFeel)lookAndFeel()).basicelements, true);
        }

        public ScrollDescript()
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
            wScrollDescription.resized();
            if(wScrollDescription.vScroll.isVisible())
                wScrollDescription.vScroll.setPos(0.0F, true);
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

    static class ObjectInfo
    {
        public String key;
        public String name;
        public String info;
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
                try
                {
                    ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale, LDRres.loader());
                    info = resourcebundle.getString(s1);
                }
                catch(Exception exception)
                {
                    info = "No info available.";
                }
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
                    info = resourcebundle.getString(s1 + "_INFO");
                }
                catch(Exception exception)
                {
//                    name = s1;
                    info = "No info available.";
                }
                if(s3 != null)
                    reg = (Regiment)Actor.getByName(s3);
            }
        }
    }

    public void _enter()
    {
        World.cur().camouflage = 0;
        wCountry.setSelected(s_country, true, false);
        Main3D.menuMusicPlay(s_country != 0 ? "de" : "ru");
        fillObjects();
        client.activateWindow();
        wTable.resolutionChanged();
        if(wTable.countRows() > 0)
        {
            wTable.setSelect(s_object, 0);
            if(wTable.vSB.isVisible())
                wTable.vSB.setPos(s_scroll, true);
        }
    }

    public void _leave()
    {
        client.hideWindow();
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
        if("air".equals(type))
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
                        ObjectInfo objectinfo1 = new ObjectInfo("i18n/air", s3, s10, false, planeClass, regiment, camouflage);
                        wTable.objects.add(objectinfo1);
                    }
                    catch(Exception exception) { }
            }

        } else
        {
            String s1 = "i18n/" + type + ".ini";
            SectFile sectfile1 = new SectFile(s1, 0);
            String s2 = "i18n/" + type;
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

    public GUIObjectInspector(GWindowRoot gwindowroot)
    {
        super(22);
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("obj.infoI");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wCountry = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCountry.setEditable(false);
        fillCountries();
        wCountry.setSelected(s_country, true, false);
        wTable = new Table(dialogClient);
        dialogClient.create(wScrollDescription = new ScrollDescript());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        wPrev = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        wView = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public static String type;
    public static int s_country = 0;
    public static int s_object = 0;
    public static float s_scroll = 0.0F;
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIButton wPrev;
    public GUIButton wView;
    public GWindowComboControl wCountry;
    public Table wTable;
    public ScrollDescript wScrollDescription;
    public Descript wDescript;
    public GWindowVScrollBar wDistance;
    public static String cnt[] = {
        "", ""
    };

    static 
    {
        cnt[0] = "allies";
        cnt[1] = "axis";
    }
}
