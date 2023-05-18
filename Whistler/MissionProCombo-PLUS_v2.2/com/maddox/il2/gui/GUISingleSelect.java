package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.builder.PlMission;
import com.maddox.il2.game.*;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.util.SharedTokenizer;
import com.maddox.util.UnicodeTo8bit;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class GUISingleSelect extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == wPrev)
            {
                Main.stateStack().pop();
                return true;
            }
            if(gwindow == sTitle)
            {
                int row = wTable.selectRow;
                float pos = wTable.vSB.pos();
                fillFiles(false);
                wTable.setSelect(row, 0);
                wTable.vSB.setPos(pos, true);
                return true;
            }
            if(gwindow == wLoad)
            {
                FileMission filemission = (FileMission)wTable.files.get(wTable.selectRow);
                Main.stateStack().push(18);
                String currentMissionName = "missions/" + wFolders.getValue() + "/" + country.toUpperCase() + "/" + wDirs.getValue() + "/" + filemission.fileName;
                PlMission.doLoadMissionFile(currentMissionName, currentMissionName);
                return true;
            }
            if(gwindow == wFolders)
            {
                fillCountries(false);
//                int k = wFolders.getSelected();
//                if(k >= 0)
//                {
//                    ((GUIRoot)root).setBackCountry("single", (String)countryLst.get(k), wDirs.getValue());
//                }
                return true;
            }
            if(gwindow == wCountry)
            {
                fillDirs(false);
                int k = wCountry.getSelected();
                if(k >= 0)
                {
                    Main3D.menuMusicPlay((String)countryLst.get(k));
                    ((GUIRoot)root).setBackCountry("single", (String)countryLst.get(k), wDirs.getValue());
                }
                return true;
            }
            if(gwindow == wDirs)
            {
                fillFiles(false);
                int l = wCountry.getSelected();
                if(l >= 0)
                    ((GUIRoot)root).setBackCountry("single", (String)countryLst.get(l), wDirs.getValue());
                return true;
            }
            if(gwindow == wRand)
            {
                fillCountries(true);
                return true;
            }
            if(gwindow == wNext)
            {
                if(wDirs.getValue() == null)
                    return true;
                int i1 = wTable.selectRow;
                if(i1 < 0 || i1 >= wTable.files.size())
                {
                    return true;
                } else
                {
                    FileMission filemission = (FileMission)wTable.files.get(i1);
//                    int j1 = wCountry.getSelected();
                    Main.cur().currentMissionFile = new SectFile("missions/" + wFolders.getValue() + "/" + country.toUpperCase() + "/" + wDirs.getValue() + "/" + filemission.fileName, 0);
                    Main.stateStack().push(4);
                    return true;
                }
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(432F), y1024(560F), x1024(384F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(416F), y1024(32F), 2.0F, y1024(608F));
            setCanvasFont(0);
            setCanvasColor(GColor.Gray);
            draw(x1024(64F), y1024(46F), x1024(240F), y1024(32F), 0, i18n("Folder"));
            draw(x1024(64F), y1024(120F), x1024(240F), y1024(32F), 0, i18n("singleSelect.Country"));
            draw(x1024(64F), y1024(194F), x1024(240F), y1024(32F), 0, i18n("singleSelect.MissType"));
            draw(x1024(64F), y1024(268F), x1024(240F), y1024(32F), 0, i18n("singleSelect.Miss"));
//            draw(x1024(464F), y1024(120F), x1024(248F), y1024(32F), 0, i18n("singleSelect.Desc"));
            draw(x1024(104F), y1024(592F), x1024(192F), y1024(48F), 0, i18n("singleSelect.MainMenu"));
            draw(x1024(528F), y1024(592F), x1024(216F), y1024(48F), 2, i18n("singleSelect.Brief"));
            draw(x1024(104F), y1024(592F), x1024(216F), y1024(48F), 2, i18n("Titles"));
            draw(x1024(512F), y1024(592F), x1024(216F), y1024(48F), 0, i18n("Random"));
        }

        public void setPosSize()
        {
            set1024PosSize(80F, 64F, 848F, 672F);
            wPrev.setPosC(x1024(56F), y1024(616F));
            sTitle.setPosC(x1024(368F), y1024(616F));
            wRand.setPosC(x1024(464F), y1024(616F));
            wNext.setPosC(x1024(792F), y1024(616F));
            wFolders.setPosSize(x1024(48F), y1024(82F), x1024(336F), M(2.0F));
            wCountry.setPosSize(x1024(48F), y1024(156F), x1024(336F), M(2.0F));
            wDirs.setPosSize(x1024(48F), y1024(230F), x1024(336F), M(2.0F));
            wTable.setPosSize(x1024(48F), y1024(304F), x1024(336F), y1024(256F));
//            pDescript.setPosSize(x1024(448F), y1024(120F), x1024(354F), y1024(32F));
            wDescript.setPosSize(x1024(448F), y1024(120F), x1024(354F), y1024(420F));
        }

        public DialogClient()
        {
        }
    }

    public class WDescript extends GWindow
    {

        public void render()
        {
            String s = null;
            if(wTable.selectRow >= 0)
            {
                String s1 = ((FileMission)wTable.files.get(wTable.selectRow)).name;
                s = s1;
                String s2 = ((FileMission)wTable.files.get(wTable.selectRow)).shortDesc;
                if(s2 != null && !s2.trim().equalsIgnoreCase(s))
                    s = s + "\n\n" + s2;
                String s3 = ((FileMission)wTable.files.get(wTable.selectRow)).longDesc;
                if(s3 != null && (s2 == null || !s3.trim().equalsIgnoreCase(s2.trim())))
                    s = s.trim() + "\n\n\n" + s3;
            }
            if(s != null)
            {
                setCanvasFont(0);
//                setCanvasColorWHITE();
                setCanvasColor(GColor.Gray);
                drawLines(0.0F, -root.C.font.descender, s, 0, s.length(), win.dx, root.C.font.height);
                setCanvasColorBLACK();
            }
        }

        public WDescript()
        {
        }
    }

    public class Table extends GWindowTable
    {

        public int countRows()
        {
            return files == null ? 0 : files.size();
        }

        public void renderCell(int i, int j, boolean flag, float f, float f1)
        {
            setCanvasFont(0);
            String s = sTitle.isChecked() ? ((FileMission)files.get(i)).name : ((FileMission)files.get(i)).fileName;
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

        public void afterCreated()
        {
            super.afterCreated();
            bColumnsSizable = false;
            addColumn(I18N.gui("singleSelect.MissFiles"), null);
            vSB.scroll = rowHeight(0);
            resized();
        }

        public void resolutionChanged()
        {
            vSB.scroll = rowHeight(0);
            super.resolutionChanged();
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

        public ArrayList files;

        public Table(GWindow gwindow)
        {
            super(gwindow, 2.0F, 4F, 20F, 16F);
            files = new ArrayList();
            bNotify = true;
            wClient.bNotify = true;
        }
    }

    static class FileMission
    {

        public String fileName;
        public String name;
        public String shortDesc;
        public String longDesc;

        private String textFileName(String tFileName, boolean flag)
        {
            String s = "";
            if(flag)
            {
                String s1 = Locale.getDefault().getLanguage();
                String s2 = RTSConf.cur.locale.getLanguage();
                if(s2.equalsIgnoreCase("ru") && !s1.equalsIgnoreCase("ru"))
                    s1 = "us";
                if(s1 == null || s1.length() < 2)
                    s1 = "us";
                if(!"us".equals(s1))
                    s = "_" + s1;
            }
//            String s2 = tFileName;
//            for(int i = s2.length() - 1; i >= 0; i--)
//            {
//                char c = s2.charAt(i);
//                if(c == '/' || c == '\\')
//                    break;
//                if(c == '.')
//                    return s2.substring(0, i) + s + ".properties";
//            }
            return tFileName + s + ".properties";
        }

        public FileMission(String s, String s1)
        {
            fileName = s1;
            name = fileName;
            shortDesc = null;
            longDesc = null;
            String s2 = s1;
            int i = s2.lastIndexOf(".");
            if(i >= 0)
                s2 = s2.substring(0, i);
            BufferedReader bufferedreader = null;
            try
            {
                try
                {
                    bufferedreader = new BufferedReader(new SFSReader(textFileName(s + "/" + s2, true)));
                }
                catch(Exception exception)
                {
                    bufferedreader = new BufferedReader(new SFSReader(textFileName(s + "/" + s2, false)));
                }
                do
                {
                    String st1 = bufferedreader.readLine();
                    if(st1 == null)
                        break;
                    int j = st1.length();
                    if(j != 0)
                    {
                        SharedTokenizer.set(st1);
                        String st2 = SharedTokenizer.next();
                        if(st2 != null)
                            if("Name".compareToIgnoreCase(st2) == 0)
                            {
                                String st3 = SharedTokenizer.getGap();
                                if(st3 != null)
                                {
                                    String tempName = UnicodeTo8bit.load(st3, false);
                                    if(!tempName.trim().equals(""))
                                        name = tempName;
                                }
                            } else
                            if("Short".compareToIgnoreCase(st2) == 0)
                            {
                                String st4 = SharedTokenizer.getGap();
                                if(st4 != null)
                                    shortDesc = UnicodeTo8bit.load(st4, false);
                            } else
                            if("Description".compareToIgnoreCase(st2) == 0)
                            {
                                String st5 = SharedTokenizer.getGap();
                                if(st5 != null)
                                    longDesc = UnicodeTo8bit.load(st5, false);
                            }
                    }
                } while(true);
            }
            catch(Exception exception) { }
            if(bufferedreader != null)
                try
                {
                    bufferedreader.close();
                }
                catch(Exception exception1) { }
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
        try
        {
            IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);
            inifile.set("LastSingleMission", "LastFolder", wFolders.getSelected());
            inifile.set("LastSingleMission", "LastCountry", wCountry.getSelected());
            inifile.set("LastSingleMission", "LastDir", wDirs.getSelected());
            inifile.set("LastSingleMission", "LastMission", wTable.selectRow);
            inifile.set("LastSingleMission", "ShowTitles", sTitle.isChecked() ? 1 : 0);
            inifile.saveFile();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        client.hideWindow();
    }

    private void init()
    {
//        if(bInited)
//            return;
        try
        {
            IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);
            int LastFolder = inifile.get("LastSingleMission", "LastFolder", 0);
            int LastCountry = inifile.get("LastSingleMission", "LastCountry", 0);
            int LastDir = inifile.get("LastSingleMission", "LastDir", 0);
            int LastMission = inifile.get("LastSingleMission", "LastMission", 0);
            sTitle.setChecked(inifile.get("LastSingleMission", "ShowTitles", 1) == 1 ? true : false, false);
            fillFolders();
            if(LastFolder >= 0 && LastFolder < wFolders.size())
            {
                wFolders.setSelected(LastFolder, true, true);
                if(LastCountry >= 0 && LastCountry < wCountry.size())
                {
                    wCountry.setSelected(LastCountry, true, true);
                    if(LastDir >= 0 && LastDir < wDirs.size())
                    {
                        wDirs.setSelected(LastDir, true, true);
                        if(LastMission >= 0 && LastMission < wTable.countRows())
                        {
                            wTable.setSelect(LastMission, 0);
                            wTable.vSB.setPos((float)wTable.selectRow * wTable.rowHeight(0) - wTable.rowHeight(0) * 3.0F, true);
                        }
                    }
                }
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            fillFolders();
        }
//        bInited = true;
    }

    public void fillFolders()
    {
//        int i = wFolders.getSelected();
//        if(i < 0)
//        {
//            wCountry.clear(false);
//            wDirs.clear(false);
//            wTable.files.clear();
//            wTable.setSelect(-1, 0);
//            return;
//        }
//        country = (String)countryLst.get(i);
        File file = new File(HomePath.get(0), "missions");
        File afile[] = file.listFiles();
        wFolders.clear(false);
//        if(afile == null || afile.length == 0)
//        {
//            wTable.files.clear();
//            wTable.setSelect(-1, 0);
//            return;
//        }
        for(int j = 0; j < afile.length; j++)
            if(afile[j].isDirectory() && !afile[j].isHidden() && !".".equals(afile[j].getName()) && !"..".equals(afile[j].getName()) && afile[j].getName().toLowerCase().startsWith("single"))
                _scanMap.put(afile[j].getName(), null);

        for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wFolders.add((String)iterator.next()));
        if(_scanMap.size() > 0)
            wFolders.setSelected(0, true, false);
        _scanMap.clear();
        fillCountries(false);
    }

    private void fillCountries(boolean rand)
    {
        resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
//        _scanMap.put(resCountry.getString("ru"), "ru");
//        _scanMap.put(resCountry.getString("de"), "de");
        File file = new File(HomePath.get(0), "missions/" + wFolders.getValue());
        wCountry.clear(false);
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
        countryLst.clear();
        for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wCountry.add(s))
        {
            s = (String)iterator.next();
            countryLst.add(_scanMap.get(s));
        }
        _scanMap.clear();
        wCountry.setSelected(-1, false, true);
        if(countryLst.size() > 0)
            wCountry.setSelected(((rand && countryLst.size() > 1) ? TrueRandom.nextInt(0, countryLst.size()) : 0), true, true);
        fillDirs(rand);
    }

    public void fillDirs(boolean rand)
    {
        countryIcon = null;
        country = null;
        int i = wCountry.getSelected();
        if(i < 0)
        {
            wDirs.clear(false);
            wTable.files.clear();
            wTable.setSelect(-1, 0);
            return;
        }
        country = (String)countryLst.get(i);
        File file = new File(HomePath.get(0), "missions/" + wFolders.getValue() + "/" + country);
        File afile[] = file.listFiles();
        wDirs.clear(false);
        if(afile == null || afile.length == 0)
        {
            wTable.files.clear();
            wTable.setSelect(-1, 0);
            return;
        }
        for(int j = 0; j < afile.length; j++)
            if(afile[j].isDirectory() && !afile[j].isHidden() && !".".equals(afile[j].getName()) && !"..".equals(afile[j].getName()))
                _scanMap.put(afile[j].getName(), null);
        for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wDirs.add((String)iterator.next()));
        if(_scanMap.size() > 0)
            wDirs.setSelected(((rand && _scanMap.size() > 1) ? TrueRandom.nextInt(0, _scanMap.size()) : 0), true, false);
        _scanMap.clear();
        if(wDirs.size() > 0)
            fillFiles(rand);
        else
        {
            wTable.files.clear();
            wTable.setSelect(-1, 0);
        }
    }

    public void fillFiles(boolean rand)
    {
        wTable.files.clear();
        String s = wDirs.getValue();
        int i = wCountry.getSelected();
        if(s != null)
        {
            String s1 = "missions/" + wFolders.getValue() + "/" + country + "/" + s;
            File file = new File(HomePath.get(0), s1);
            File afile[] = file.listFiles();
            if(afile != null && afile.length > 0)
            {
                for(int j = 0; j < afile.length; j++)
//                    if(!afile[j].isDirectory() && !afile[j].isHidden() && afile[j].getName().toLowerCase().lastIndexOf(".properties") < 0)
                    if(!afile[j].isDirectory() && !afile[j].isHidden() && afile[j].getName().toLowerCase().lastIndexOf(".mis") != - 1)
                    {
                        FileMission filemission = new FileMission(s1, afile[j].getName());
                        _scanMap.put(filemission.fileName, filemission);
                    }

                for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wTable.files.add(_scanMap.get(iterator.next())));
                if(_scanMap.size() > 0)
                    wTable.setSelect(((rand && _scanMap.size() > 1) ? TrueRandom.nextInt(0, _scanMap.size()) : 0), 0);
                else
                    wTable.setSelect(-1, 0);
                _scanMap.clear();
            } else
            {
                wTable.setSelect(-1, 0);
            }
        } else
        {
            wTable.setSelect(-1, 0);
        }
        wTable.resized();
        wTable.vSB.setPos((float)wTable.selectRow * wTable.rowHeight(0) - wTable.rowHeight(0) * 3.0F, true);
    }

    public GUISingleSelect(GWindowRoot gwindowroot)
    {
        super(3);
//        bInited = false;
        countryLst = new ArrayList();
        _scanMap = new TreeMap();
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("singleSelect.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wFolders = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wFolders.setEditable(false);
        wCountry = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCountry.setEditable(false);
        wCountry.listVisibleLines = 10;
        wDirs = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wDirs.setEditable(false);
        wDirs.listVisibleLines = 14;
        wTable = new Table(dialogClient);
        dialogClient.create(wDescript = new WDescript());
        wDescript.bNotify = true;
//        pDescript = new GUIPocket(dialogClient, "");
        GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        wPrev = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        sTitle = (GUISwitchBox2)dialogClient.addControl(new GUISwitchBox2(dialogClient));
        wRand = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        wNext = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public static final String HOME_DIR = "missions/single";
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIButton wPrev;
    public GUIButton wLoad;
    public GUISwitchBox2 sTitle;
    public GUIButton wRand;
    public GUIButton wNext;
    public GWindowComboControl wFolders;
    public GWindowComboControl wCountry;
    public GTexture countryIcon;
    public GWindowComboControl wDirs;
//    public GUIPocket pDescript;
    public Table wTable;
    public WDescript wDescript;
    public String country;
//    public boolean bInited;
    public ResourceBundle resCountry;
    public ArrayList countryLst;
    public TreeMap _scanMap;
}
