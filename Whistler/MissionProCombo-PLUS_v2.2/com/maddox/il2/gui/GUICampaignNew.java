package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.GUIWindowManager;
import com.maddox.il2.game.*;
import com.maddox.il2.game.campaign.*;
import com.maddox.rts.*;
import com.maddox.util.UnicodeTo8bit;
import java.io.*;
import java.util.*;

public class GUICampaignNew extends GameState
{
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
            if(gwindow == bDifficulty)
            {
                World.cur().diffUser.set(difficulty);
                Main.stateStack().push(17);
                return true;
            }
            if(gwindow == bStart)
            {
                if(dgenCampaignPrefix == null)
                {
                    String s = country + campaign;
                    String s1 = "users/" + World.cur().userCfg.sId + "/campaigns.ini";
                    if(exestFile(s1))
                    {
                        SectFile sectfile = new SectFile(s1, 0, true, World.cur().userCfg.krypto());
                        int k = sectfile.sectionIndex("list");
                        if(k >= 0 && sectfile.varExist(k, s))
                        {
                            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 30F, true, i18n("campnew.Confirm"), i18n("campnew.Exist"), 1, 0.0F) {

                                public void result(int l)
                                {
                                    if(l == 3)
                                        doStartCampaign();
                                    else
                                        client.activateWindow();
                                }

                            }
;
                            return true;
                        }
                    }
                }
                if(dgenCampaignPrefix == null)
                    doStartCampaign();
                else
                    doStartDGenCampaign();
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(560F), x1024(293F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(341F), y1024(32F), 2.0F, y1024(608F));
            setCanvasFont(0);
            setCanvasColor(GColor.Gray);
            draw(x1024(64F), y1024(120F), x1024(240F), y1024(32F), 0, i18n("campnew.Country"));
            draw(x1024(64F), y1024(194F), x1024(240F), y1024(32F), 0, i18n("campnew.Rank"));
            draw(x1024(389F), y1024(46F), x1024(357F), y1024(32F), 0, i18n("campnew.Career"));
            draw(x1024(389F), y1024(120F), x1024(357F), y1024(32F), 0, i18n("campnew.Description"));
            draw(x1024(104F), y1024(592F), x1024(192F), y1024(48F), 0, i18n("campnew.MainMenu"));
            draw(x1024(278F), y1024(592F), x1024(216F), y1024(48F), 2, i18n("campnew.Difficulty"));
            draw(x1024(428F), y1024(592F), x1024(216F), y1024(48F), 2, i18n("campnew.Start"));
            if(countryIcon != null)
                draw(x1024(160F), y1024(48F), x1024g(64F), y1024g(64F), countryIcon);
            if(wRank.size() == 7 && wRank.getSelected() == 0)
                draw(x1024(37F), y1024(268F), x1024(240F), y1024(32F), 2, i18n("(Lowest Rank selected)"));
        }

        public void setPosSize()
        {
            set1024PosSize(80F, 64F, 848F, 672F);
            wCountry.setPosSize(x1024(48F), y1024(156F), x1024(261F), M(2.0F));
            wRank.setPosSize(x1024(48F), y1024(230F), x1024(261F), M(2.0F));
            wTip.setPosSize(x1024(48F), y1024(450F), x1024(261F), y1024(100F));
            wCampaign.setPosSize(x1024(373F), y1024(82F), x1024(411F), M(2.0F));
            wScrollDescription.setPosSize(x1024(373F), y1024(156F), x1024(411F), y1024(404F));
            bExit.setPosC(x1024(56F), y1024(616F));
            bDifficulty.setPosC(x1024(542F), y1024(616F));
            bStart.setPosC(x1024(692F), y1024(616F));
        }

        public DialogClient()
        {
        }
    }

    public class WComboCampaign extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if(k < 0)
                    return true;
                Object obj = campaignLst.get(k);
                if(obj instanceof String)
                {
                    campaign = (String)obj;
                    dgenCampaignPrefix = null;
                    fillInfo();
                } else
                {
                    DGenCampaign dgencampaign = (DGenCampaign)obj;
                    campaign = dgencampaign.name;
                    dgenCampaignPrefix = dgencampaign.prefix;
                    dgenCampaignFileName = dgencampaign.fileName;
                    textDescription = dgencampaign.description;
                    wScrollDescription.resized();
                }
                return true;
            } else
            {
                return super.notify(i, j);
            }
        }

        public WComboCampaign(GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
        }
    }

    public class WComboCountry extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if(k < 0)
                {
                    return true;
                } else
                {
                    fillCountry(k);
                    Main3D.menuMusicPlay((String)countryLst.get(k));
                    ((GUIRoot)root).setBackCountry("campaign", (String)countryLst.get(k));
                    return true;
                }
            } else
            {
                return super.notify(i, j);
            }
        }

        public WComboCountry(GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
        }
    }

    public class Descript extends GWindowDialogClient
    {

        public void render()
        {
            String s = textDescription;
            if(s != null)
            {
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                setCanvasFont(0);
                setCanvasColorBLACK();
                drawLines(gbevel.L.dx + 2.0F, gbevel.T.dy + 2.0F, s, 0, s.length(), win.dx - gbevel.L.dx - gbevel.R.dx - 4F, root.C.font.height);
            }
        }

        public void computeSize()
        {
            String s = textDescription;
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

    public class WTip extends GWindow
    {
        public void render()
        {
            String s = "Tip: ";
            if(World.cur().diffUser.NoInstantSuccess)
                s = s + "Uncheck \"" + i18n("diff.NoInstantSuccess") + "\" in the difficulty settings to continue playing a campaign even if you do not achieve mission goals. ";
            s = s + "Difficulty cannot be changed during the campaign.";
            setCanvasFont(0);
            setCanvasColor(GColor.Gray);
            drawLines(0.0F, -root.C.font.descender, s, 0, s.length(), win.dx, root.C.font.height);
//            setCanvasColorBLACK();
        }

        public WTip()
        {
        }
    }

    static class DGenCampaign
    {

        String fileName;
        String prefix;
        String name;
        String description;

        DGenCampaign()
        {
        }
    }


    public void enterPush(GameState gamestate)
    {
        dgenCampaignPrefix = null;
        World.cur().diffUser.set(World.cur().userCfg.singleDifficulty);
        enter(gamestate);
    }

    public void enterPop(GameState gamestate)
    {
        if(gamestate.id() == 17)
        {
            World.cur().userCfg.singleDifficulty = World.cur().diffUser.get();
            World.cur().userCfg.saveConf();
        }
        enter(gamestate);
    }

    public void enter(GameState gamestate)
    {
        if(gamestate != null && gamestate.id() == 58)
        {
            Main.cur().currentMissionFile = Main.cur().campaign.nextMission();
            if(Main.cur().currentMissionFile == null)
            {
                new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, i18n("miss.Error"), i18n("miss.LoadFailed"), 3, 0.0F) {

                    public void result(int i)
                    {
                    }

                }
;
                return;
            } else
            {
                Main.stateStack().change(28);
                return;
            }
        } else
        {
            World.cur().diffUser.set(World.cur().userCfg.singleDifficulty);
            difficulty = World.cur().diffUser.get();
            _enter();
            return;
        }
    }

    public void _enter()
    {
        init();
        int i = wCountry.getSelected();
        if(i >= 0)
            Main3D.menuMusicPlay((String)countryLst.get(i));
        client.activateWindow();
    }

    public void _leave()
    {
        client.hideWindow();
    }

    private boolean exestFile(String s)
    {
        try
        {
            SFSInputStream sfsinputstream = new SFSInputStream(s);
            sfsinputstream.close();
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }

    private void init()
    {
        if(bInited)
            return;
        resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
        _scanMap.put(resCountry.getString("ru"), "ru");
        _scanMap.put(resCountry.getString("de"), "de");
        File file = new File(HomePath.get(0), "missions/campaign");
        if(file != null)
        {
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int i = 0; i < afile.length; i++)
                {
                    if(!afile[i].isDirectory() || afile[i].isHidden())
                        continue;
                    String s1 = afile[i].getName().toLowerCase();
                    String s2 = null;
                    try
                    {
                        s2 = resCountry.getString(s1);
                    }
                    catch(Exception exception)
                    {
                        continue;
                    }
                    if(!_scanMap.containsKey(s2))
                        _scanMap.put(s2, s1);
                }

            }
        }
        String s;
        for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wCountry.add(s))
        {
            s = (String)iterator.next();
            countryLst.add(_scanMap.get(s));
        }

        _scanMap.clear();
        wCountry.setSelected(-1, false, true);
        if(countryLst.size() > 0)
            wCountry.setSelected(0, true, true);
        bInited = true;
    }

    private boolean fillRank()
    {
        try
        {
            resRank = ResourceBundle.getBundle("missions/campaign/" + country + "/" + "rank", RTSConf.cur.locale);
            wRank.add(resRank.getString("0"));
            wRank.add(resRank.getString("1"));
            wRank.add(resRank.getString("2"));
            wRank.add(resRank.getString("3"));
            wRank.add(resRank.getString("4"));
            wRank.add(resRank.getString("5"));
            wRank.add(resRank.getString("6"));
        }
        catch(Exception exception)
        {
            return false;
        }
        wRank.setSelected(0, true, false);
        return true;
    }

    private boolean fillCampaign()
    {
        campaignLst.clear();
        wCampaign.clear(false);
        _scanMap.clear();
        String s = "missions/campaign/" + country + "/all.ini";
        if(exestFile(s))
        {
            SectFile sectfile = new SectFile(s, 0);
            int i = sectfile.sectionIndex("list");
            if(i >= 0)
            {
                int j = sectfile.vars(i);
                for(int l = 0; l < j; l++)
                {
                    String s3 = sectfile.var(i, l);
                    _scanMap.put(s3.toLowerCase(), null);
                }

            }
        }
        File file = new File(HomePath.get(0), "missions/campaign/" + country);
        if(file != null)
        {
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int k = 0; k < afile.length; k++)
                    if(afile[k].isDirectory() && !afile[k].isHidden())
                    {
                        String s2 = afile[k].getName();
                        if(s2.indexOf(" ") < 0)
                            _scanMap.put(s2.toLowerCase(), null);
                    }

            }
        }
        if(_scanMap.size() > 0)
        {
            for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext();)
            {
                String s1 = (String)iterator.next();
                try
                {
                    ResourceBundle resourcebundle = ResourceBundle.getBundle("missions/campaign/" + country + "/" + s1 + "/info", RTSConf.cur.locale);
                    String s4 = resourcebundle.getString("Name");
                    SectFile sectfile1 = new SectFile("missions/campaign/" + country + "/" + s1 + "/campaign.ini", 0);
                    int i1 = sectfile1.sectionIndex("list");
                    if(i1 >= 0 && sectfile1.vars(i1) > 0 && sectfile1.get("Main", "ExecGenerator", (String)null) == null)
                    {
                        campaignLst.add(s1);
                        wCampaign.add(s4);
                    }
                }
                catch(Exception exception) { }
            }

            _scanMap.clear();
        }
        fillDGen();
        if(campaignLst.size() == 0)
        {
            return false;
        } else
        {
            wCampaign.setSelected(-1, false, true);
            wCampaign.setSelected(0, true, true);
            return true;
        }
    }

    private String getPrefix(String s, String s1, String s2)
    {
        if(!s.toLowerCase().startsWith(s1.toLowerCase()))
            return null;
        if(!s.toLowerCase().endsWith(s2.toLowerCase()))
            return null;
        try
        {
            String s3 = s.substring(s1.length(), s.length() - s2.length());
            if(s3.indexOf("_") != -1)
                return null;
            else
                return s3;
        }
        catch(IndexOutOfBoundsException indexoutofboundsexception)
        {
            return null;
        }
    }

    private void fillDGen()
    {
        String s = RTSConf.cur.locale.getLanguage();
        String s1 = "campaigns" + country;
        String s2 = null;
        if(!"us".equals(s))
            s2 = "_" + s + ".dat";
        String s3 = ".dat";
        File file = new File(HomePath.toFileSystemName("dgen", 0));
        String as[] = file.list();
        if(as == null || as.length == 0)
            return;
        HashMap hashmap = new HashMap();
        if(s2 != null)
        {
            for(int i = 0; i < as.length; i++)
            {
                String s4 = as[i];
                if(s4 != null)
                {
                    String s6 = getPrefix(s4, s1, s2);
                    if(s6 != null)
                    {
                        DGenCampaign dgencampaign = new DGenCampaign();
                        dgencampaign.fileName = s4;
                        dgencampaign.prefix = s6;
                        hashmap.put(s6, dgencampaign);
                    }
                }
            }

        }
        for(int j = 0; j < as.length; j++)
        {
            String s5 = as[j];
            if(s5 != null)
            {
                String s7 = getPrefix(s5, s1, s3);
                if(s7 != null && !hashmap.containsKey(s7))
                {
                    DGenCampaign dgencampaign1 = new DGenCampaign();
                    dgencampaign1.fileName = s5;
                    dgencampaign1.prefix = s7;
                    hashmap.put(s7, dgencampaign1);
                }
            }
        }

        if(hashmap.size() == 0)
            return;
        _scanMap.clear();
        for(Iterator iterator = hashmap.keySet().iterator(); iterator.hasNext();)
        {
            String s8 = (String)iterator.next();
            DGenCampaign dgencampaign2 = (DGenCampaign)hashmap.get(s8);
            try
            {
                BufferedReader bufferedreader = new BufferedReader(new SFSReader("dgen/" + dgencampaign2.fileName, RTSConf.charEncoding));
                dgencampaign2.name = UnicodeTo8bit.load(bufferedreader.readLine(), false);
                dgencampaign2.description = UnicodeTo8bit.load(bufferedreader.readLine(), false);
                bufferedreader.close();
                if(dgencampaign2.name != null && dgencampaign2.name.length() > 0 && dgencampaign2.description != null && dgencampaign2.description.length() > 0)
                    _scanMap.put(dgencampaign2.name, dgencampaign2);
            }
            catch(Exception exception) { }
        }

        hashmap.clear();
        DGenCampaign dgencampaign3;
        for(Iterator iterator1 = _scanMap.keySet().iterator(); iterator1.hasNext(); wCampaign.add(dgencampaign3.name))
        {
            String s9 = (String)iterator1.next();
            dgencampaign3 = (DGenCampaign)_scanMap.get(s9);
            campaignLst.add(dgencampaign3);
        }

        _scanMap.clear();
    }

    private void fillInfo()
    {
        textDescription = null;
        try
        {
            ResourceBundle resourcebundle = ResourceBundle.getBundle("missions/campaign/" + country + "/" + campaign + "/info", RTSConf.cur.locale);
            textDescription = resourcebundle.getString("Description");
        }
        catch(Exception exception) { }
        wScrollDescription.resized();
    }

    private void fillCountry(int i)
    {
        wRank.clear(false);
        wCampaign.clear(false);
        campaign = null;
        dgenCampaignPrefix = null;
        textDescription = null;
        country = (String)countryLst.get(i);
        countryIcon = null;
        if(!fillRank())
        {
            wRank.clear(false);
            country = null;
            return;
        }
        if(!fillCampaign())
        {
            wRank.clear(false);
            wCampaign.clear(false);
            country = null;
            return;
        } else
        {
            countryIcon = GTexture.New("missions/campaign/" + country + "/icon.mat");
            return;
        }
    }

    private void doStartDGenCampaign()
    {
        Main.cur().campaign = new CampaignDGen(dgenCampaignFileName, country, difficulty, wRank.getSelected(), dgenCampaignPrefix);
        Main.stateStack().change(61);
    }

    private void doStartCampaign()
    {
        Campaign campaign1 = null;
        try
        {
            String s = country + campaign;
            String s2 = "users/" + World.cur().userCfg.sId + "/campaigns.ini";
            SectFile sectfile = new SectFile(s2, 1, false, World.cur().userCfg.krypto());
            SectFile sectfile1 = new SectFile("missions/campaign/" + country + "/" + campaign + "/campaign.ini", 0);
            String s3 = sectfile1.get("Main", "Class", (String)null);
            Class class1 = ObjIO.classForName(s3);
            campaign1 = (Campaign)class1.newInstance();
            class1 = ObjIO.classForName(sectfile1.get("Main", "awardsClass", (String)null));
            Awards awards = (Awards)class1.newInstance();
            campaign1.init(awards, country, campaign, difficulty, wRank.getSelected());
            campaign1._epilogueTrack = sectfile1.get("Main", "EpilogueTrack", (String)null);
            sectfile.set("list", s, campaign1, true);
            campaign1.clearSavedStatics(sectfile);
            sectfile.saveFile();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return;
        }
        Main.cur().campaign = campaign1;
        String s1 = Main.cur().campaign.nextIntro();
        if(s1 != null)
        {
            GUIBWDemoPlay.demoFile = s1;
            GUIBWDemoPlay.soundFile = null;
            Main.stateStack().push(58);
            return;
        }
        Main.cur().currentMissionFile = campaign1.nextMission();
        if(Main.cur().currentMissionFile == null)
        {
            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, i18n("miss.Error"), i18n("miss.LoadFailed"), 3, 0.0F) {

                public void result(int i)
                {
                }

            }
;
            return;
        } else
        {
            Main.stateStack().change(28);
            return;
        }
    }

    public GUICampaignNew(GWindowRoot gwindowroot)
    {
        super(26);
        dgenCampaignPrefix = null;
        dgenCampaignFileName = null;
        bInited = false;
        countryLst = new ArrayList();
        campaignLst = new ArrayList();
        _scanMap = new TreeMap();
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("campnew.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wCountry = (WComboCountry)dialogClient.addControl(new WComboCountry(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCountry.setEditable(false);
        wCountry.listVisibleLines = 10;
        wCampaign = (WComboCampaign)dialogClient.addControl(new WComboCampaign(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCampaign.setEditable(false);
        wCampaign.listVisibleLines = 14;
        wRank = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wRank.setEditable(false);
        dialogClient.create(wScrollDescription = new ScrollDescript());
        dialogClient.create(wTip = new WTip());
        wTip.bNotify = true;
        GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bDifficulty = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bStart = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public static final String HOME_DIR = "missions/campaign";
    public static final String RANK_FILE = "rank";
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public WComboCountry wCountry;
    public GTexture countryIcon;
    public WComboCampaign wCampaign;
    public GWindowComboControl wRank;
    public ScrollDescript wScrollDescription;
    public Descript wDescript;
    public WTip wTip;
    public GUIButton bExit;
    public GUIButton bDifficulty;
    public GUIButton bStart;
    public String country;
    public String campaign;
    public String dgenCampaignPrefix;
    public String dgenCampaignFileName;
    public String textDescription;
    public long difficulty;
    public boolean bInited;
    public ResourceBundle resRank;
    public ResourceBundle resCountry;
    public ArrayList countryLst;
    public ArrayList campaignLst;
    private TreeMap _scanMap;
}
