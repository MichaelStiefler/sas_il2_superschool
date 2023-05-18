package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.GUIWindowManager;
import com.maddox.il2.game.*;
import com.maddox.il2.net.*;
import com.maddox.rts.*;
import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class GUINetServerMisSelect extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2 || loadMessageBox != null)
                return super.notify(gwindow, i, j);
            if(gwindow == wPrev)
            {
                GUINetServer.exitServer(true);
                return true;
            }
            if(gwindow == wDirs)
            {
                fillFiles();
                return true;
            }
            if(gwindow == wDifficulty)
            {
                Main.stateStack().push(41);
                return true;
            }
            if(gwindow == wNext)
            {
                if(wDirs.getValue() == null)
                    return true;
                int k = wTable.selectRow;
                if(k < 0 || k >= wTable.files.size())
                {
                    return true;
                } else
                {
                    FileMission filemission = (FileMission)wTable.files.get(k);
                    Main.cur().currentMissionFile = new SectFile(HOME_DIR() + "/" + wDirs.getValue() + "/" + filemission.fileName, 0);
                    doLoadMission();
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
            GUISeparate.draw(this, GColor.Gray, x1024(432F), y1024(412F), x1024(384F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(416F), y1024(32F), 2.0F, y1024(460F));
            setCanvasFont(0);
            setCanvasColor(GColor.Gray);
            draw(x1024(64F), y1024(46F), x1024(240F), y1024(32F), 0, i18n("netsms.MissionType"));
            draw(x1024(64F), y1024(120F), x1024(240F), y1024(32F), 0, i18n("netsms.Missions"));
            draw(x1024(464F), y1024(120F), x1024(248F), y1024(32F), 0, i18n("netsms.Description"));
            draw(x1024(104F), y1024(444F), x1024(192F), y1024(48F), 0, !USGS.isUsed() && Main.cur().netGameSpy == null ? i18n("netsms.MainMenu") : i18n("main.Quit"));
            draw(x1024(496F), y1024(444F), x1024(128F), y1024(48F), 0, i18n("brief.Difficulty"));
            draw(x1024(528F), y1024(444F), x1024(216F), y1024(48F), 2, i18n("netsms.Load"));
        }

        public void setPosSize()
        {
            set1024PosSize(80F, 138F, 848F, 524F);
            wPrev.setPosC(x1024(56F), y1024(468F));
            wDifficulty.setPosC(x1024(456F), y1024(468F));
            wNext.setPosC(x1024(792F), y1024(468F));
            wDirs.setPosSize(x1024(48F), y1024(82F), x1024(336F), M(2.0F));
            wTable.setPosSize(x1024(48F), y1024(156F), x1024(336F), y1024(256F));
            wDescript.setPosSize(x1024(448F), y1024(168F), x1024(354F), y1024(208F));
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
                s = ((FileMission)wTable.files.get(wTable.selectRow)).description;
                if(s != null && s.length() == 0)
                    s = null;
            }
            if(s != null)
            {
                setCanvasFont(0);
//                setCanvasColorBLACK();
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
            String s = ((FileMission)files.get(i)).name;
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
            addColumn(I18N.gui("netsms.Mission_files"), null);
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
        public String description;

        public FileMission(String s, String s1)
        {
            fileName = s1;
            try
            {
                String s2 = s1;
                int i = s2.lastIndexOf(".");
                if(i >= 0)
                    s2 = s2.substring(0, i);
                ResourceBundle resourcebundle = ResourceBundle.getBundle(s + "/" + s2, RTSConf.cur.locale);
                name = resourcebundle.getString("Name");
                description = resourcebundle.getString("Short");
            }
            catch(Exception exception)
            {
                name = s1;
                description = null;
            }
        }
    }

    class MissionListener
        implements MsgBackgroundTaskListener
    {

        public void msgBackgroundTaskStarted(BackgroundTask backgroundtask)
        {
        }

        public void msgBackgroundTaskStep(BackgroundTask backgroundtask)
        {
            loadMessageBox.message = (int)backgroundtask.percentComplete() + "% " + I18N.gui(backgroundtask.messageComplete());
        }

        public void msgBackgroundTaskStoped(BackgroundTask backgroundtask)
        {
            BackgroundTask.removeListener(this);
            if(backgroundtask.isComplete())
                missionLoaded();
            else
                missionBad(I18N.gui("miss.LoadBad") + " " + backgroundtask.messageCancel());
        }

        public MissionListener()
        {
            BackgroundTask.addListener(this);
        }
    }


    private void doLoadMission()
    {
        loadMessageBox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, i18n("netsms.StandBy"), i18n("netsms.Loading_simulation"), 5, 0.0F) {

            public void result(int i)
            {
                if(i == 1)
                    BackgroundTask.cancel(I18N.gui("miss.UserCancel"));
            }

        }
;
        new MsgAction(72, 0.0D) {

            public void doAction()
            {
                if(Mission.cur() != null)
                    Mission.cur().destroy();
                try
                {
                    new MissionListener();
                    Mission.loadFromSect(Main.cur().currentMissionFile, true);
                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    missionBad(I18N.gui("miss.LoadBad"));
                }
            }

        }
;
    }

    public void missionLoaded()
    {
        new MsgAction(72, 0.0D) {

            public void doAction()
            {
                GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
                if(loadMessageBox != null)
                {
                    loadMessageBox.close(false);
                    loadMessageBox = null;
                }
                if(Main.cur().netServerParams.isDogfight())
                {
                    ((NetUser)NetEnv.host()).setBornPlace(-1);
                    CmdEnv.top().exec("mission BEGIN");
                    Main.stateStack().change(39);
                } else
                if(Main.cur().netServerParams.isCoop())
                {
                    ((NetUser)NetEnv.host()).resetAllPlaces();
                    CmdEnv.top().exec("mission BEGIN");
                    int i = GUINetAircraft.serverPlace();
                    if(i != -1)
                        ((NetUser)NetEnv.host()).requestPlace(i);
                    Main.stateStack().change(45);
                }
            }

        }
;
    }

    private void missionBad(String s)
    {
        loadMessageBox.close(false);
        loadMessageBox = null;
        new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, i18n("netsms.Error"), s, 3, 0.0F) {

            public void result(int i)
            {
            }

        }
;
    }

    public String HOME_DIR()
    {
        if(Main.cur().netServerParams.isCoop())
            return "missions/net/coop";
        if(Main.cur().netServerParams.isDogfight())
            return "missions/net/dogfight";
        else
            return "missions/net";
    }

    public void enter(GameState gamestate)
    {
        World.cur().diffCur.set(World.cur().userCfg.netDifficulty);
        Main.cur().netServerParams.setDifficulty(World.cur().diffCur.get());
        if(gamestate.id() == 35)
        {
            _enter();
        } else
        {
            if(Main.cur().netServerParams.isCoop())
            {
                NetEnv.cur().connect.bindEnable(true);
                Main.cur().netServerParams.USGSupdate();
            }
            client.activateWindow();
        }
    }

    public void enterPop(GameState gamestate)
    {
        if(gamestate.id() == 41)
        {
            World.cur().userCfg.netDifficulty = World.cur().diffCur.get();
            World.cur().userCfg.saveConf();
            Main.cur().netServerParams.setDifficulty(World.cur().diffCur.get());
        }
        client.activateWindow();
    }

    public void _enter()
    {
        fillDirs();
        if(Main.cur().netServerParams.isCoop())
        {
            infoMenu.info = i18n("netsms.infoC");
            NetEnv.cur().connect.bindEnable(true);
            Main.cur().netServerParams.USGSupdate();
        } else
        if(Main.cur().netServerParams.isDogfight())
            infoMenu.info = i18n("netsms.infoD");
        else
            infoMenu.info = i18n("netsms.infoM");
        client.activateWindow();
    }

    public void _leave()
    {
        client.hideWindow();
    }

    public void fillDirs()
    {
        File file = new File(HomePath.get(0), HOME_DIR());
        File afile[] = file.listFiles();
        wDirs.clear(false);
        if(afile == null || afile.length == 0)
        {
            wTable.files.clear();
            wTable.setSelect(-1, 0);
            return;
        }
        for(int i = 0; i < afile.length; i++)
            if(afile[i].isDirectory() && !afile[i].isHidden() && !".".equals(afile[i].getName()) && !"..".equals(afile[i].getName()))
                _scanMap.put(afile[i].getName(), null);

        for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wDirs.add((String)iterator.next()));
        if(_scanMap.size() > 0)
            wDirs.setSelected(0, true, false);
        _scanMap.clear();
        fillFiles();
    }

    public void fillFiles()
    {
        wTable.files.clear();
        String s = wDirs.getValue();
        if(s != null)
        {
            String s1 = HOME_DIR() + "/" + s;
            File file = new File(HomePath.get(0), s1);
            File afile[] = file.listFiles();
            if(afile != null && afile.length > 0)
            {
                for(int i = 0; i < afile.length; i++)
                    if(!afile[i].isDirectory() && !afile[i].isHidden() && afile[i].getName().toLowerCase().lastIndexOf(".properties") < 0)
                    {
                        FileMission filemission = new FileMission(s1, afile[i].getName());
                        _scanMap.put(filemission.fileName, filemission);
                    }

                for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wTable.files.add(_scanMap.get(iterator.next())));
                if(_scanMap.size() > 0)
                    wTable.setSelect(0, 0);
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
    }

    public GUINetServerMisSelect(GWindowRoot gwindowroot)
    {
        super(38);
        _scanMap = new TreeMap();
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("netsms.infoM");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wDirs = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wDirs.setEditable(false);
        wDirs.listVisibleLines = 14;
        wTable = new Table(dialogClient);
        dialogClient.create(wDescript = new WDescript());
        wDescript.bNotify = true;
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        wPrev = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        wDifficulty = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        wNext = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public static final String HOME_DIR = "missions/net";
    public static final String HOME_DIR_DOGFIGHT = "missions/net/dogfight";
    public static final String HOME_DIR_COOP = "missions/net/coop";
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIButton wPrev;
    public GUIButton wDifficulty;
    public GUIButton wNext;
    public GWindowComboControl wDirs;
    public Table wTable;
    public WDescript wDescript;
    private GWindowMessageBox loadMessageBox;
    public TreeMap _scanMap;




}
