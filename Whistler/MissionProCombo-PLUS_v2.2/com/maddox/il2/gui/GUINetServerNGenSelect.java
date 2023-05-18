package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.net.USGS;
import com.maddox.rts.*;
import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class GUINetServerNGenSelect extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bExit)
            {
                GUINetServer.exitServer(true);
                return true;
            }
            if(gwindow == bDel)
            {
                if(wTable.selectRow < 0)
                    return true;
                if(wTable.selectRow >= wTable.campList.size())
                    return true;
                GUINetServerNGenSelect.cur = null;
                Item item = (Item)wTable.campList.get(wTable.selectRow);
                try
                {
                    String s = item.fileName;
                    int k = s.lastIndexOf("/conf.dat");
                    if(k >= 0)
                        s = s.substring(0, k);
                    File file = new File(HomePath.get(0), s);
                    clearDir(file);
                    wTable.campList.remove(wTable.selectRow);
                    if(wTable.selectRow >= wTable.campList.size())
                        wTable.setSelect(wTable.campList.size() - 1, 0);
                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    return true;
                }
                return true;
            }
            if(gwindow == bStart)
            {
                if(wTable.selectRow < 0)
                    return true;
                if(wTable.selectRow >= wTable.campList.size())
                    return true;
                GUINetServerNGenSelect.cur = null;
                Item item1 = (Item)wTable.campList.get(wTable.selectRow);
                if(item1.bNew)
                    try
                    {
                        String s1 = item1.prefix;
                        int l = 1;
                        do
                        {
                            if(l <= 0)
                                break;
                            File file1 = new File(HomePath.get(0), "missions/net/ngen/" + s1 + l);
                            if(!file1.exists())
                            {
                                s1 = "missions/net/ngen/" + s1 + l;
                                file1.mkdirs();
                                break;
                            }
                            l++;
                        } while(true);
                        String s2 = s1 + "/conf.dat";
                        Item item2 = new Item(item1);
                        item2.bNew = false;
                        item2.fileName = s2;
                        SectFile sectfile = new SectFile(item1.fileName, 0, true, null, RTSConf.charEncoding, true);
                        sectfile.saveFile(s2);
                        GUINetServerNGenSelect.cur = item2;
                    }
                    catch(Exception exception1)
                    {
                        System.out.println(exception1.getMessage());
                        exception1.printStackTrace();
                        return true;
                    }
                else
                    GUINetServerNGenSelect.cur = item1;
                if(GUINetServerNGenSelect.cur != null)
                    Main.stateStack().change(69);
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        private void clearDir(File file)
        {
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int i = 0; i < afile.length; i++)
                {
                    File file1 = afile[i];
                    String s = file1.getName();
                    if(".".equals(s) || "..".equals(s))
                        continue;
                    if(file1.isDirectory())
                        clearDir(file1);
                    else
                        file1.delete();
                }

            }
            file.delete();
        }

        public void preRender()
        {
            super.preRender();
            if(wTable.isEnableDel())
            {
                if(!bDel.isVisible())
                    bDel.showWindow();
            } else
            if(bDel.isVisible())
                bDel.hideWindow();
            if(wTable.isEnableLoad())
            {
                if(!bStart.isVisible())
                    bStart.showWindow();
            } else
            if(bStart.isVisible())
                bStart.hideWindow();
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(624F), x1024(960F), 2.0F);
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(96F), y1024(658F), x1024(128F), y1024(48F), 0, !USGS.isUsed() && Main.cur().netGameSpy == null ? i18n("netsms.MainMenu") : i18n("main.Quit"));
            if(wTable.isEnableDel())
                draw(x1024(256F), y1024(658F), x1024(160F), y1024(48F), 2, i18n("camps.Delete"));
            if(wTable.isEnableLoad())
                draw(x1024(766F), y1024(658F), x1024(160F), y1024(48F), 2, i18n("camps.Load"));
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            bExit.setPosC(x1024(56F), y1024(682F));
            bDel.setPosC(x1024(456F), y1024(682F));
            bStart.setPosC(x1024(968F), y1024(682F));
            wTable.set1024PosSize(32F, 32F, 960F, 480F);
        }

        public DialogClient()
        {
        }
    }

    public class Table extends GWindowTable
    {

        public int countRows()
        {
            return campList == null ? 0 : campList.size();
        }

        public void renderCell(int i, int j, boolean flag, float f, float f1)
        {
            setCanvasFont(0);
            if(flag)
            {
                setCanvasColorBLACK();
                draw(0.0F, 0.0F, f, f1, lookAndFeel().regionWhite);
            }
            Item item = (Item)campList.get(i);
            float f2 = 0.0F;
            String s = null;
            int k = 0;
            switch(j)
            {
            case 0: // '\0'
                s = item.name;
                break;

            case 1: // '\001'
                if(item.bNew)
                    s = I18N.gui("ngens.new");
                else
                if(item.bEnd)
                    s = I18N.gui("ngens.complete");
                else
                    s = I18N.gui("ngens.progress");
                k = 1;
                break;

            case 2: // '\002'
                s = "" + item.missions;
                k = 1;
                break;

            case 3: // '\003'
                s = item.note;
                break;
            }
            if(flag)
            {
                setCanvasColorWHITE();
                draw(f2, 0.0F, f, f1, k, s);
            } else
            {
                setCanvasColorBLACK();
                draw(f2, 0.0F, f, f1, k, s);
            }
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
            bColumnsSizable = true;
            bSelectRow = true;
            addColumn(I18N.gui("ngens.name"), null);
            addColumn(I18N.gui("ngens.state"), null);
            addColumn(I18N.gui("ngens.missions"), null);
            addColumn(I18N.gui("ngens.note"), null);
            vSB.scroll = rowHeight(0);
            getColumn(0).setRelativeDx(10F);
            getColumn(1).setRelativeDx(5F);
            getColumn(2).setRelativeDx(5F);
            getColumn(3).setRelativeDx(20F);
            alignColumns();
            bNotify = true;
            wClient.bNotify = true;
            resized();
        }

        public boolean isEnableDel()
        {
            if(campList == null)
                return false;
            if(selectRow < 0)
                return false;
            if(selectRow >= campList.size())
            {
                return false;
            } else
            {
                Item item = (Item)campList.get(selectRow);
                return !item.bNew;
            }
        }

        public boolean isEnableLoad()
        {
            if(campList == null)
                return false;
            if(selectRow < 0)
                return false;
            if(selectRow >= campList.size())
            {
                return false;
            } else
            {
                Item item = (Item)campList.get(selectRow);
                return !item.bEnd;
            }
        }

        public void resolutionChanged()
        {
            vSB.scroll = rowHeight(0);
            super.resolutionChanged();
        }

        public ArrayList campList;

        public Table(GWindow gwindow)
        {
            super(gwindow);
            campList = new ArrayList();
        }
    }

    static class Item
    {

        public boolean equals(Object obj)
        {
            if(obj == null)
                return false;
            if(!(obj instanceof Item))
            {
                return false;
            } else
            {
                Item item = (Item)obj;
                return fileName.equalsIgnoreCase(item.fileName);
            }
        }

        public boolean bNew;
        public boolean bEnd;
        public String prefix;
        public String fileName;
        public String name;
        public int missions;
        public String note;

        public Item()
        {
            name = "";
            missions = 0;
            note = "";
        }

        public Item(Item item)
        {
            name = "";
            missions = 0;
            note = "";
            bNew = item.bNew;
            bEnd = item.bEnd;
            fileName = item.fileName;
            name = item.name;
            missions = item.missions;
            note = item.note;
        }
    }


    public void _enter()
    {
        NetEnv.cur().connect.bindEnable(true);
        Main.cur().netServerParams.USGSupdate();
        fillCampList();
        wTable.resized();
        client.activateWindow();
    }

    public void _leave()
    {
        wTable.campList.clear();
        client.hideWindow();
    }

    private void fillCampList()
    {
        String s = RTSConf.cur.locale.getLanguage();
        String s1 = "campaign";
        String s2 = null;
        if(!"us".equals(s))
            s2 = "_" + s + ".dat";
        String s3 = ".dat";
        File file = new File(HomePath.get(0), "ngen");
        File afile[] = file.listFiles();
        String as[] = file.list();
        if(as == null || as.length == 0)
            return;
        TreeMap treemap = new TreeMap();
        for(int i = 0; i < as.length; i++)
        {
            if(afile[i].isDirectory() || afile[i].isHidden())
                continue;
            String s4 = as[i];
            if(s4 == null)
                continue;
            s4 = s4.toLowerCase();
            if(s4.length() <= s1.length() || !s4.regionMatches(true, 0, s1, 0, s1.length()))
                continue;
            int l = -1;
            boolean flag = false;
            if(s2 != null && s4.length() > s2.length() && s4.regionMatches(true, s4.length() - s2.length(), s2, 0, s2.length()))
            {
                l = s4.length() - s2.length();
                flag = true;
            }
            if(l == -1 && s4.length() > s3.length() && s4.regionMatches(true, s4.length() - s3.length(), s3, 0, s3.length()))
            {
                l = s4.length() - s3.length();
                if(s4.length() > s3.length() + 3 && s4.charAt(s4.length() - s3.length() - 3) == '_')
                    continue;
                flag = false;
            }
            if(l < s1.length())
                continue;
            String s7 = s4.substring(s1.length(), l);
            if(flag || !treemap.containsKey(s7))
                treemap.put(s7, s4);
        }

        if(treemap.size() == 0)
            return;
        Item item1;
        for(Iterator iterator = treemap.keySet().iterator(); iterator.hasNext(); wTable.campList.add(item1))
        {
            String s5 = (String)iterator.next();
            String s6 = (String)treemap.get(s5);
            item1 = new Item();
            item1.bNew = true;
            item1.prefix = s5;
            item1.fileName = "ngen/" + s6;
            SectFile sectfile = new SectFile(item1.fileName, 4, true, null, RTSConf.charEncoding, true);
            item1.name = sectfile.get("$locale", "name", "");
            item1.note = sectfile.get("$locale", "note", "");
        }

        file = new File(HomePath.get(0), "missions/net/ngen");
        afile = file.listFiles();
        if(afile != null && afile.length > 0)
        {
            for(int j = 0; j < afile.length;)
            {
                if(!afile[j].isDirectory() || afile[j].isHidden())
                    continue;
                try
                {
                    File file1 = new File(afile[j], "conf.dat");
                    if(!file1.exists())
                        continue;
                    Item item2 = new Item();
                    item2.bNew = false;
                    item2.fileName = "missions/net/ngen/" + afile[j].getName().toLowerCase() + "/conf.dat";
                    SectFile sectfile1 = new SectFile(item2.fileName, 4, true, null, RTSConf.charEncoding, true);
                    item2.bEnd = sectfile1.get("$select", "complete", false);
                    item2.name = sectfile1.get("$locale", "name", "");
                    item2.note = sectfile1.get("$locale", "note", "");
                    wTable.campList.add(item2);
                    int i1 = sectfile1.sectionIndex("$missions");
                    if(i1 >= 0)
                        item2.missions = sectfile1.vars(i1);
                    continue;
                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    j++;
                }
            }

        }
        if(wTable.campList.size() == 0)
            return;
        if(cur != null)
        {
            int k = 0;
            do
            {
                if(k >= wTable.campList.size())
                    break;
                Item item = (Item)wTable.campList.get(k);
                if(cur.equals(item))
                    break;
                k++;
            } while(true);
            if(k < wTable.campList.size())
                wTable.setSelect(k, 0);
            else
                wTable.setSelect(0, 0);
        } else
        {
            wTable.setSelect(0, 0);
        }
    }

    public GUINetServerNGenSelect(GWindowRoot gwindowroot)
    {
        super(68);
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("ngens.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wTable = new Table(dialogClient);
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bDel = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bStart = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public Table wTable;
    public GUIButton bExit;
    public GUIButton bDel;
    public GUIButton bStart;
    static Item cur = null;
}
