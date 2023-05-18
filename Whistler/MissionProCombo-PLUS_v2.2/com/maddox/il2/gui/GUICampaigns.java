package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.game.*;
import com.maddox.il2.game.campaign.Campaign;
import com.maddox.rts.*;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GUICampaigns extends GameState
{
    static class Item
    {

        public String key;
        public Campaign camp;
        public GTexture icon;
        public String name;
        public String rank;
        public String diff;

        Item()
        {
        }
    }

    public class Table extends GWindowTable
    {

        public int countRows()
        {
            return campList != null ? campList.size() : 0;
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
                setCanvasColorWHITE();
                draw(0.0F, 0.0F, f1, f1, item.icon);
                f2 = 1.5F * f1;
                f -= f2;
                s = item.name;
                break;

            case 1: // '\001'
                s = item.rank;
                break;

            case 2: // '\002'
                if(item.camp._nawards >= 0)
                    s = "" + item.camp._nawards;
                else
                    s = "" + item.camp.awards(item.camp.score());
                k = 1;
                break;

            case 3: // '\003'
                if(item.camp.isComplete())
                    s = "100%";
                else
                    s = "" + item.camp.completeMissions();
                k = 1;
                break;

            case 4: // '\004'
                s = item.diff;
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
            super.bColumnsSizable = true;
            super.bSelectRow = true;
            addColumn(I18N.gui("camps.Career"), null);
            addColumn(I18N.gui("camps.Rank"), null);
            addColumn(I18N.gui("camps.Awards"), null);
            addColumn(I18N.gui("camps.Completed"), null);
            addColumn(I18N.gui("camps.Difficulty"), null);
            super.vSB.scroll = rowHeight(0);
            getColumn(0).setRelativeDx(11F);
            getColumn(1).setRelativeDx(6F);
            getColumn(2).setRelativeDx(5F);
            getColumn(3).setRelativeDx(5F);
            getColumn(4).setRelativeDx(6F);
            alignColumns();
            super.bNotify = true;
            super.wClient.bNotify = true;
            resized();
        }

        public void resolutionChanged()
        {
            super.vSB.scroll = rowHeight(0);
            super.resolutionChanged();
        }

        public ArrayList campList;

        public Table(GWindow gwindow)
        {
            super(gwindow);
            campList = new ArrayList();
        }
    }

    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bExit)
            {
                Main.stateStack().pop();
                return true;
            }
            if(gwindow == bNew)
            {
                Main.stateStack().change(26);
                return true;
            }
            if(gwindow == bStat)
            {
                int k = ((GWindowTable) (wTable)).selectRow;
                if(k < 0)
                    return true;
                Item item = (Item)wTable.campList.get(k);
                Main.cur().campaign = item.camp;
                if(item.camp.isDGen())
                    Main.stateStack().push(65);
                else
                    Main.stateStack().push(31);
                return true;
            }
            if(gwindow == bDiff)
            {
                int l = ((GWindowTable) (wTable)).selectRow;
                if(l < 0)
                {
                    return true;
                } else
                {
                    Item item1 = (Item)wTable.campList.get(l);
                    World.cur().diffUser.set(item1.camp.difficulty());
                    Main.stateStack().push(17);
                    return true;
                }
            }
            if(gwindow == bDel)
            {
                GWindowMessageBox gwindowmessagebox = new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 25F, true, I18N.gui("camps.Delete"), I18N.gui("campnew.Confirm"), 1, 0.0F) {

                      public void result(int m)
                      {
                          if(m == 3)
                              removeItem();
                      }
                  }
;
                return true;
            }
            if(gwindow == bStart)
            {
                doStart();
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            if(((GWindowTable) (wTable)).selectRow >= 0 && ((Item)wTable.campList.get(((GWindowTable) (wTable)).selectRow)).camp.isDGen())
                draw(x1024(80F), y1024(544F), x1024(336F), y1024(48F), 2, i18n("camps.Roster"));
            else
                draw(x1024(80F), y1024(544F), x1024(336F), y1024(48F), 2, i18n("camps.Statistics"));
            draw(x1024(728F), y1024(544F), x1024(200F), y1024(48F), 2, i18n("camps.View_Difficulty"));
            draw(x1024(96F), y1024(658F), x1024(128F), y1024(48F), 0, i18n("camps.Back"));
            draw(x1024(256F), y1024(658F), x1024(160F), y1024(48F), 2, i18n("camps.Delete"));
            draw(x1024(496F), y1024(658F), x1024(160F), y1024(48F), 2, i18n("camps.New"));
            draw(x1024(766F), y1024(658F), x1024(160F), y1024(48F), 2, i18n("camps.Load"));
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(624F), x1024(960F), 2.0F);
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            bStat.setPosC(x1024(456F), y1024(568F));
            bDiff.setPosC(x1024(968F), y1024(568F));
            bExit.setPosC(x1024(56F), y1024(682F));
            bDel.setPosC(x1024(456F), y1024(682F));
            bNew.setPosC(x1024(696F), y1024(682F));
            bStart.setPosC(x1024(968F), y1024(682F));
            wTable.set1024PosSize(32F, 32F, 960F, 480F);
        }

        public DialogClient()
        {
        }
    }


    public void enterPush(GameState gamestate)
    {
        enter(gamestate);
    }

    public void enterPop(GameState gamestate)
    {
        enter(gamestate);
    }

    public void enter(GameState gamestate)
    {
        if(gamestate != null && gamestate.id() == 58)
        {
            Main.cur().currentMissionFile = Main.cur().campaign.nextMission();
            if(Main.cur().currentMissionFile == null)
            {
                new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 40F, true, i18n("miss.Error"), i18n("miss.LoadFailed"), 3, 0.0F) {

                    public void result(int j)
                    {
                    }

                }
;
                return;
            }
            if(Main.cur().campaign.isDGen())
                Main.stateStack().change(62);
            else
                Main.stateStack().change(28);
            return;
        } else
        {
            _enter();
            return;
        }
    }

    public void _enter()
    {
        fillCampList();
        if(wTable.campList.size() == 0)
        {
            Main.stateStack().change(26);
            return;
        }
        wTable.resized();
        try
        {
            IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);
            int Item = inifile.get("LastSingleMission", "CampaignItem", 0);
            if(Item < wTable.countRows())
                wTable.setSelect(Item, 0);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        client.activateWindow();
    }

    public void _leave()
    {
        try
        {
            IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);
            inifile.set("LastSingleMission", "CampaignItem", ((GWindowTable) (wTable)).selectRow);
            inifile.saveFile();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        wTable.campList.clear();
        client.hideWindow();
    }

    private void fillCampList()
    {
        wTable.campList.clear();
        DifficultySettings difficultysettings = new DifficultySettings();
        SectFile sectfile = null;
        int i = 0;
        String s = "users/" + World.cur().userCfg.sId + "/campaigns.ini";
        sectfile = new SectFile(s, 0, false, World.cur().userCfg.krypto());
        i = sectfile.sectionIndex("list");
        if(i < 0)
            return;
        try
        {
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                Item item = new Item();
                item.key = sectfile.var(i, k);
                item.camp = (Campaign)ObjIO.fromString(sectfile.value(i, k));
                item.icon = GTexture.New("missions/campaign/" + item.camp.branch() + "/icon.mat");
                ResourceBundle resourcebundle = ResourceBundle.getBundle("missions/campaign/" + item.camp.branch() + "/" + item.camp.missionsDir() + "/info", RTSConf.cur.locale);
                item.name = resourcebundle.getString("Name");
                ResourceBundle resourcebundle1 = ResourceBundle.getBundle("missions/campaign/" + item.camp.branch() + "/" + "rank", RTSConf.cur.locale);
                item.rank = resourcebundle1.getString("" + item.camp.rank());
                difficultysettings.set(item.camp.difficulty());
                if(difficultysettings.isRealistic())
                    item.diff = i18n("camps.realistic");
                else
                if(difficultysettings.isNormal())
                    item.diff = i18n("camps.normal");
                else
                if(difficultysettings.isEasy())
                    item.diff = i18n("camps.easy");
                else
                    item.diff = i18n("camps.custom");
                wTable.campList.add(item);
            }

            if(wTable.campList.size() > 0)
                wTable.setSelect(0, 0);
        }
        catch(Exception exception)
        {
            wTable.campList.clear();
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            try
            {
                sectfile.sectionClear(i);
                sectfile.saveFile(sectfile.fileName());
            }
            catch(Exception exception1) { }
        }
    }

    private void removeItem()
    {
        int i = ((GWindowTable) (wTable)).selectRow;
        if(i < 0)
            return;
        String s = "users/" + World.cur().userCfg.sId + "/campaigns.ini";
        SectFile sectfile = new SectFile(s, 1, true, World.cur().userCfg.krypto());
        int j = sectfile.sectionIndex("list");
        int k = sectfile.varIndex(j, ((Item)wTable.campList.get(i)).key);
        try
        {
            Campaign campaign = (Campaign)ObjIO.fromString(sectfile.value(j, k));
            if(campaign.isDGen())
            {
                String s1 = "missions/campaign/" + campaign.branch() + "/" + campaign.missionsDir();
                File file = new File(HomePath.toFileSystemName(s1, 0));
                File afile[] = file.listFiles();
                if(afile != null)
                {
                    for(int i1 = 0; i1 < afile.length; i1++)
                    {
                        File file1 = afile[i1];
                        String s2 = file1.getName();
                        if(!".".equals(s2) && !"..".equals(s2))
                            file1.delete();
                    }

                }
                file.delete();
            }
            campaign.clearSavedStatics(sectfile);
        }
        catch(Exception exception) { }
        sectfile.lineRemove(j, k);
        sectfile.saveFile();
        wTable.campList.remove(i);
        int l = wTable.campList.size();
        if(l == 0)
        {
            wTable.setSelect(-1, 0);
            Main.stateStack().change(26);
        } else
        if(i == l)
            wTable.setSelect(i - 1, 0);
    }

    private void doStart()
    {
        int i = ((GWindowTable) (wTable)).selectRow;
        if(i < 0)
            return;
        Item item = (Item)wTable.campList.get(i);
        if(item.camp.isComplete())
            return;
        wTable.campList.clear();
        Main.cur().campaign = item.camp;
        Main3D.menuMusicPlay(Main.cur().campaign.country());
        String s = Main.cur().campaign.nextIntro();
        if(s != null)
        {
            GUIBWDemoPlay.demoFile = s;
            GUIBWDemoPlay.soundFile = null;
            Main.stateStack().push(58);
            return;
        }
        Main.cur().currentMissionFile = item.camp.nextMission();
        if(Main.cur().currentMissionFile == null)
        {
            new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 40F, true, i18n("miss.Error"), i18n("miss.LoadFailed"), 3, 0.0F) {

                public void result(int k)
                {
                }

            }
;
            return;
        }
        ((GUIRoot)((GWindow) (dialogClient)).root).setBackCountry("campaign", Main.cur().campaign.branch());
        if(Main.cur().campaign.isDGen())
            Main.stateStack().change(62);
        else
            Main.stateStack().change(28);
    }

    public GUICampaigns(GWindowRoot gwindowroot)
    {
        super(27);
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("camps.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wTable = new Table(dialogClient);
        GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bStat = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bDiff = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bDel = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bNew = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bStart = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public Table wTable;
    public GUIButton bDiff;
    public GUIButton bStat;
    public GUIButton bDel;
    public GUIButton bNew;
    public GUIButton bExit;
    public GUIButton bStart;


}
