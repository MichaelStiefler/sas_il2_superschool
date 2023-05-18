/*4.10.1 Class*/
package com.maddox.il2.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.USGS;
import com.maddox.rts.BackgroundTask;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HomePath;
import com.maddox.rts.MsgAction;
import com.maddox.rts.MsgBackgroundTaskListener;
import com.maddox.rts.NetEnv;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;

public class GUINetServerMisSelect extends GameState {
    public static final String HOME_DIR          = "missions/net";
    public static final String HOME_DIR_DOGFIGHT = "missions/net/dogfight";
    public static final String HOME_DIR_COOP     = "missions/net/coop";
    public GUIClient           client;
    public DialogClient        dialogClient;
    public GUIInfoMenu         infoMenu;
    public GUIInfoName         infoName;
    public GUIButton           wPrev;
    public GUIButton           wDifficulty;
    public GUIButton           wNext;
    public GWindowComboControl wDirs;
    public Table               wTable;
    public WDescript           wDescript;
    private GWindowMessageBox  loadMessageBox;
    public TreeMap             _scanMap          = new TreeMap();

    public class DialogClient extends GUIDialogClient {
        public boolean notify(GWindow gwindow, int i, int i_0_) {
            if (i != 2 || GUINetServerMisSelect.this.loadMessageBox != null) return super.notify(gwindow, i, i_0_);
            if (gwindow == GUINetServerMisSelect.this.wPrev) {
                GUINetServer.exitServer(true);
                return true;
            }
            if (gwindow == GUINetServerMisSelect.this.wDirs) {
                GUINetServerMisSelect.this.fillFiles();
                return true;
            }
            if (gwindow == GUINetServerMisSelect.this.wDifficulty) {
                Main.stateStack().push(41);
                return true;
            }
            if (gwindow == GUINetServerMisSelect.this.wNext) {
                if (GUINetServerMisSelect.this.wDirs.getValue() == null) return true;
                int i_1_ = GUINetServerMisSelect.this.wTable.selectRow;
                if (i_1_ < 0 || i_1_ >= GUINetServerMisSelect.this.wTable.files.size()) return true;
                FileMission filemission = (FileMission) GUINetServerMisSelect.this.wTable.files.get(i_1_);
                Main.cur().currentMissionFile = new SectFile(GUINetServerMisSelect.this.HOME_DIR() + "/" + GUINetServerMisSelect.this.wDirs.getValue() + "/" + filemission.fileName, 0);
                GUINetServerMisSelect.this.doLoadMission();
                return true;
            }
            return super.notify(gwindow, i, i_0_);
        }

        public void render() {
            super.render();
            GUISeparate.draw(this, GColor.Gray, this.x1024(432.0F), this.y1024(546.0F), this.x1024(384.0F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, this.x1024(416.0F), this.y1024(32.0F), 2.0F, this.y1024(608.0F));
            this.setCanvasColor(GColor.Gray);
            this.setCanvasFont(0);
            this.draw(this.x1024(64.0F), this.y1024(156.0F), this.x1024(240.0F), this.y1024(32.0F), 0, GUINetServerMisSelect.this.i18n("netsms.MissionType"));
            this.draw(this.x1024(64.0F), this.y1024(264.0F), this.x1024(240.0F), this.y1024(32.0F), 0, GUINetServerMisSelect.this.i18n("netsms.Missions"));
            this.draw(this.x1024(464.0F), this.y1024(264.0F), this.x1024(248.0F), this.y1024(32.0F), 0, GUINetServerMisSelect.this.i18n("netsms.Description"));
            this.draw(this.x1024(104.0F), this.y1024(592.0F), this.x1024(192.0F), this.y1024(48.0F), 0, USGS.isUsed() || Main.cur().netGameSpy != null ? GUINetServerMisSelect.this.i18n("main.Quit") : GUINetServerMisSelect.this.i18n("netsms.MainMenu"));
            this.draw(this.x1024(496.0F), this.y1024(592.0F), this.x1024(128.0F), this.y1024(48.0F), 0, GUINetServerMisSelect.this.i18n("brief.Difficulty"));
            this.draw(this.x1024(528.0F), this.y1024(592.0F), this.x1024(216.0F), this.y1024(48.0F), 2, GUINetServerMisSelect.this.i18n("netsms.Load"));
        }

        public void setPosSize() {
            this.set1024PosSize(80.0F, 64.0F, 848.0F, 672.0F);
            GUINetServerMisSelect.this.wPrev.setPosC(this.x1024(56.0F), this.y1024(616.0F));
            GUINetServerMisSelect.this.wDifficulty.setPosC(this.x1024(456.0F), this.y1024(616.0F));
            GUINetServerMisSelect.this.wNext.setPosC(this.x1024(792.0F), this.y1024(616.0F));
            GUINetServerMisSelect.this.wDirs.setPosSize(this.x1024(48.0F), this.y1024(192.0F), this.x1024(336.0F), this.M(2.0F));
            GUINetServerMisSelect.this.wTable.setPosSize(this.x1024(48.0F), this.y1024(304.0F), this.x1024(336.0F), this.y1024(256.0F));
            GUINetServerMisSelect.this.wDescript.setPosSize(this.x1024(448.0F), this.y1024(312.0F), this.x1024(354.0F), this.y1024(212.0F));
        }
    }

    public class WDescript extends GWindow {
        public void render() {
            String string = null;
            if (GUINetServerMisSelect.this.wTable.selectRow >= 0) {
                string = ((FileMission) GUINetServerMisSelect.this.wTable.files.get(GUINetServerMisSelect.this.wTable.selectRow)).description;
                if (string != null && string.length() == 0) string = null;
            }
            if (string != null) {
                this.setCanvasFont(0);
                this.setCanvasColorBLACK();
                this.drawLines(0.0F, -this.root.C.font.descender, string, 0, string.length(), this.win.dx, this.root.C.font.height);
            }
        }
    }

    public class Table extends GWindowTable {
        public ArrayList files = new ArrayList();

        public int countRows() {
            return this.files != null ? this.files.size() : 0;
        }

        public void renderCell(int i, int i_2_, boolean bool, float f, float f_3_) {
            this.setCanvasFont(0);
            String string = ((FileMission) this.files.get(i)).name;
            if (bool) {
                this.setCanvasColorBLACK();
                this.draw(0.0F, 0.0F, f, f_3_, this.lookAndFeel().regionWhite);
                this.setCanvasColorWHITE();
                this.draw(0.0F, 0.0F, f, f_3_, 0, string);
            } else {
                this.setCanvasColorBLACK();
                this.draw(0.0F, 0.0F, f, f_3_, 0, string);
            }
        }

        public void afterCreated() {
            super.afterCreated();
            this.bColumnsSizable = false;
            this.addColumn(I18N.gui("netsms.Mission_files"), null);
            this.vSB.scroll = this.rowHeight(0);
            this.resized();
        }

        public void resolutionChanged() {
            this.vSB.scroll = this.rowHeight(0);
            super.resolutionChanged();
        }

        public boolean notify(GWindow gwindow, int i, int i_4_) {
            if (super.notify(gwindow, i, i_4_)) return true;
            this.notify(i, i_4_);
            return false;
        }

        public Table(GWindow gwindow) {
            super(gwindow, 2.0F, 4.0F, 20.0F, 16.0F);
            this.bNotify = true;
            this.wClient.bNotify = true;
        }
    }

    static class FileMission {
        public String fileName;
        public String name;
        public String description;

        public FileMission(String string, String string_5_) {
            this.fileName = string_5_;
            try {
                String string_6_ = string_5_;
                int i = string_6_.lastIndexOf(".");
                if (i >= 0) string_6_ = string_6_.substring(0, i);
                ResourceBundle resourcebundle = ResourceBundle.getBundle(string + "/" + string_6_, RTSConf.cur.locale);
                this.name = resourcebundle.getString("Name");
                this.description = resourcebundle.getString("Short");
            } catch (Exception exception) {
                this.name = string_5_;
                this.description = null;
            }
        }
    }

    class MissionListener implements MsgBackgroundTaskListener {
        public void msgBackgroundTaskStarted(BackgroundTask backgroundtask) {
            /* empty */
        }

        public void msgBackgroundTaskStep(BackgroundTask backgroundtask) {
            GUINetServerMisSelect.this.loadMessageBox.message = (int) backgroundtask.percentComplete() + "% " + I18N.gui(backgroundtask.messageComplete());
        }

        public void msgBackgroundTaskStoped(BackgroundTask backgroundtask) {
            BackgroundTask.removeListener(this);
            if (backgroundtask.isComplete()) GUINetServerMisSelect.this.missionLoaded();
            else GUINetServerMisSelect.this.missionBad(I18N.gui("miss.LoadBad") + " " + backgroundtask.messageCancel());
        }

        public MissionListener() {
            BackgroundTask.addListener(this);
        }
    }

    private void doLoadMission() {
        this.loadMessageBox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, this.i18n("netsms.StandBy"), this.i18n("netsms.Loading_simulation"), 5, 0.0F) {
            public void result(int i) {
                if (i == 1) BackgroundTask.cancel(I18N.gui("miss.UserCancel"));
            }
        };
        new MsgAction(72, 0.0) {
            public void doAction() {
                if (Mission.cur() != null) Mission.cur().destroy();
                try {
                    new MissionListener();
                    Mission.loadFromSect(Main.cur().currentMissionFile, true);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    GUINetServerMisSelect.this.missionBad(I18N.gui("miss.LoadBad"));
                }
            }
        };
    }

    public void missionLoaded() {
        new MsgAction(72, 0.0) {
            public void doAction() {
                // com.maddox.il2.engine.GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
                if (GUINetServerMisSelect.this.loadMessageBox != null) {
                    GUINetServerMisSelect.this.loadMessageBox.close(false);
                    GUINetServerMisSelect.this.loadMessageBox = null;
                }
                if (Main.cur().netServerParams.isDogfight()) {
                    ((NetUser) NetEnv.host()).setBornPlace(-1);
                    CmdEnv.top().exec("mission BEGIN");
                    Main.stateStack().change(39);
                } else if (Main.cur().netServerParams.isCoop()) {
                    ((NetUser) NetEnv.host()).resetAllPlaces();
                    CmdEnv.top().exec("mission BEGIN");
                    int i = GUINetAircraft.serverPlace();
                    if (i != -1) ((NetUser) NetEnv.host()).requestPlace(i);
                    Main.stateStack().change(45);
                }
            }
        };
    }

    private void missionBad(String string) {
        this.loadMessageBox.close(false);
        this.loadMessageBox = null;
        new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, this.i18n("netsms.Error"), string, 3, 0.0F) {
            public void result(int i) {
                /* empty */
            }
        };
    }

    public String HOME_DIR() {
        if (Main.cur().netServerParams.isCoop()) return "missions/net/coop";
        if (Main.cur().netServerParams.isDogfight()) return "missions/net/dogfight";
        return "missions/net";
    }

    public void enter(GameState gamestate) {
        World.cur().diffCur.set(World.cur().userCfg.netDifficulty);
        Main.cur().netServerParams.setDifficulty(World.cur().diffCur.get());
        if (gamestate.id() == 35) this._enter();
        else {
            if (Main.cur().netServerParams.isCoop()) {
                NetEnv.cur().connect.bindEnable(true);
                Main.cur().netServerParams.USGSupdate();
            }
            this.client.activateWindow();
        }
    }

    public void enterPop(GameState gamestate) {
        if (gamestate.id() == 41) {
            World.cur().userCfg.netDifficulty = World.cur().diffCur.get();
            World.cur().userCfg.saveConf();
            Main.cur().netServerParams.setDifficulty(World.cur().diffCur.get());
        }
        this.client.activateWindow();
    }

    public void _enter() {
        this.fillDirs();
        if (Main.cur().netServerParams.isCoop()) {
            this.infoMenu.info = this.i18n("netsms.infoC");
            NetEnv.cur().connect.bindEnable(true);
            Main.cur().netServerParams.USGSupdate();
        } else if (Main.cur().netServerParams.isDogfight()) this.infoMenu.info = this.i18n("netsms.infoD");
        else this.infoMenu.info = this.i18n("netsms.infoM");
        this.client.activateWindow();
    }

    public void _leave() {
        this.client.hideWindow();
    }

    public void fillDirs() {
        File file = new File(HomePath.get(0), this.HOME_DIR());
        File[] files = file.listFiles();
        this.wDirs.clear(false);
        if (files == null || files.length == 0) {
            this.wTable.files.clear();
            this.wTable.setSelect(-1, 0);
        } else {
            for (int i = 0; i < files.length; i++)
                if (files[i].isDirectory() && !files[i].isHidden() && !".".equals(files[i].getName()) && !"..".equals(files[i].getName())) this._scanMap.put(files[i].getName(), null);
            Iterator iterator = this._scanMap.keySet().iterator();
            while (iterator.hasNext())
                this.wDirs.add((String) iterator.next());
            if (this._scanMap.size() > 0) this.wDirs.setSelected(0, true, false);
            this._scanMap.clear();
            this.fillFiles();
        }
    }

    public void fillFiles() {
        this.wTable.files.clear();
        String string = this.wDirs.getValue();
        if (string != null) {
            String string_16_ = this.HOME_DIR() + "/" + string;
            File file = new File(HomePath.get(0), string_16_);
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++)
                    // TODO: Added by |ZUTI|: show only files that end with .mis
                    // if (!files[i].isDirectory() && !files[i].isHidden() && files[i].getName().toLowerCase().lastIndexOf(".properties") < 0)
                    if (!files[i].isDirectory() && !files[i].isHidden() && files[i].getName().toLowerCase().lastIndexOf(".mis") >= 0 && files[i].getName().toLowerCase().lastIndexOf(".mismds") < 0) {
                        FileMission filemission = new FileMission(string_16_, files[i].getName());
                        this._scanMap.put(filemission.fileName, filemission);
                    }
                Iterator iterator = this._scanMap.keySet().iterator();
                while (iterator.hasNext())
                    this.wTable.files.add(this._scanMap.get(iterator.next()));

                // TODO: Added by |ZUTI|: order table content ignoring upper/downer cases in mission names
                // ---------------------------------------------------
                Collections.sort(this.wTable.files, new ZutiSupportMethods_GUI.Missions_CompareByFileName());
                // ---------------------------------------------------

                if (this._scanMap.size() > 0) this.wTable.setSelect(0, 0);
                else this.wTable.setSelect(-1, 0);
                this._scanMap.clear();
            } else this.wTable.setSelect(-1, 0);
        } else this.wTable.setSelect(-1, 0);
        this.wTable.resized();
    }

    public GUINetServerMisSelect(GWindowRoot gwindowroot) {
        super(38);
        this.client = (GUIClient) gwindowroot.create(new GUIClient());
        this.dialogClient = (DialogClient) this.client.create(new DialogClient());
        this.infoMenu = (GUIInfoMenu) this.client.create(new GUIInfoMenu());
        this.infoMenu.info = this.i18n("netsms.infoM");
        this.infoName = (GUIInfoName) this.client.create(new GUIInfoName());
        this.wDirs = (GWindowComboControl) this.dialogClient.addControl(new GWindowComboControl(this.dialogClient, 2.0F, 2.0F, 20.0F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        this.wDirs.setEditable(false);
        this.wTable = new Table(this.dialogClient);
        this.dialogClient.create(this.wDescript = new WDescript());
        this.wDescript.bNotify = true;
        GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
        this.wPrev = (GUIButton) this.dialogClient.addEscape(new GUIButton(this.dialogClient, gtexture, 0.0F, 96.0F, 48.0F, 48.0F));
        this.wDifficulty = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
        this.wNext = (GUIButton) this.dialogClient.addDefault(new GUIButton(this.dialogClient, gtexture, 0.0F, 192.0F, 48.0F, 48.0F));
        this.dialogClient.activateWindow();
        this.client.hideWindow();
    }
}
