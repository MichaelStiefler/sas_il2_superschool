/* 4.10.1 class */
package com.maddox.il2.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods_Multicrew;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.CockpitPilot;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.NetGunner;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.NetEnv;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;

public class GUINetAircraft extends GameState {
    public GUIClient    client;
    public DialogClient dialogClient;
    public GUIInfoMenu  infoMenu;
    public GUIInfoName  infoName;
    public GUIButton    bLoodout;
    public GUIButton    bPrev;
    public GUIButton    bFly;
    public Table        wTable;
    public SectFile     sectMission;
    public int          serverPlace = -1;

    // TODO: Changed by |ZUTI|: private to protected
    // -----------------------------------------------
    public static GameState GAME_STATE   = new GameState(0);
    protected static int[]  crewFunction = new int[20];
    public GUIButton        zutiRefreshButton;
    // -----------------------------------------------

    public class DialogClient extends GUIDialogClient {
        public boolean notify(GWindow gwindow, int i, int i_0_) {
            try {
                if (i != 2) return super.notify(gwindow, i, i_0_);
                if (gwindow == GUINetAircraft.this.bPrev) {
                    if (Main.cur().netServerParams.isMaster()) {
                        // TODO: Edited by |ZUTI|
                        if (!GUINetAircraft.ZUTI_IS_GAME_DF) Main.stateStack().change(45);
                        else Main.stateStack().change(39);
                    } else // TODO: Edited by |ZUTI|
                        if (!GUINetAircraft.ZUTI_IS_GAME_DF) Main.stateStack().change(46);
                        else Main.stateStack().change(40);
                    return true;
                }
                if (gwindow == GUINetAircraft.this.bLoodout) {
                    if (((NetUser) NetEnv.host()).getPlace() == -1) return true;
                    if (selectedCockpitNum() > 0) return true;
                    GUIAirArming.stateId = 3;
                    Main.stateStack().push(55);
                    return true;
                }
                if (gwindow == GUINetAircraft.this.bFly) {
                    if (((NetUser) NetEnv.host()).getPlace() == -1) return true;

                    GUINetAircraft.this._doFly();
                    return true;
                }
                // TODO: Added by |ZUTI|
                // --------------------------------------------------
                if (gwindow == GUINetAircraft.this.zutiRefreshButton) {
                    if (ZutiSupportMethods_GUI.ZUTI_LAST_CREW_RESYNC == 0 || ZutiSupportMethods_GUI.ZUTI_LAST_CREW_RESYNC + 10000 < Time.current()) {
                        ZutiSupportMethods_GUI.ZUTI_LAST_CREW_RESYNC = Time.current();

                        if (Mission.isDogfight()) ZutiSupportMethods_NetSend.requestNetPlacesUpdate();

                        GUINetAircraft.this.fillListItems();

                        new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, GAME_STATE.i18n("mds.info.crewPositions"), GAME_STATE.i18n("mds.info.crewRefreshed"), 3, 0.0F);
                    } else new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, GAME_STATE.i18n("mds.info.crewPositions"), GAME_STATE.i18n("mds.info.crewRefreshLimit"), 3, 0.0F);
                    return true;
                }
                // --------------------------------------------------
                return super.notify(gwindow, i, i_0_);
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        public void render() {
            try {
                super.render();
                GUISeparate.draw(this, GColor.Gray, this.x1024(32.0F), this.y1024(624.0F), this.x1024(960.0F), 2.0F);
                this.setCanvasColor(GColor.Gray);
                this.setCanvasFont(0);
                this.draw(this.x1024(96.0F), this.y1024(656.0F), this.x1024(128.0F), this.y1024(48.0F), 0, GUINetAircraft.this.i18n("netair.Brief"));
                this.draw(this.x1024(528.0F), this.y1024(656.0F), this.x1024(176.0F), this.y1024(48.0F), 2, GUINetAircraft.this.i18n("netair.Arming"));
                this.draw(this.x1024(768.0F), this.y1024(656.0F), this.x1024(160.0F), this.y1024(48.0F), 2, GUINetAircraft.this.i18n("netair.Fly"));

                // TODO: Added by |ZUTI|
                // ------------------------------------------------------------------
                this.draw(this.x1024(350.0F), this.y1024(656.0F), this.x1024(160.0F), this.y1024(48.0F), 2, GUINetAircraft.this.i18n("mds.refreshList"));
                // ------------------------------------------------------------------
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void setPosSize() {
            try {
                this.set1024PosSize(0.0F, 32.0F, 1024.0F, 736.0F);
                GUINetAircraft.this.wTable.set1024PosSize(32.0F, 32.0F, 960.0F, 560.0F);
                GUINetAircraft.this.bPrev.setPosC(this.x1024(56.0F), this.y1024(680.0F));
                GUINetAircraft.this.bLoodout.setPosC(this.x1024(744.0F), this.y1024(680.0F));

                // TODO: Added by |ZUTI|
                // ------------------------------------------
                GUINetAircraft.this.zutiRefreshButton.setPosC(this.x1024(550.0F), this.y1024(680.0F));
                // ------------------------------------------

                GUINetAircraft.this.bFly.setPosC(this.x1024(968.0F), this.y1024(680.0F));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class Table extends GWindowTable {
        public ArrayList items = new ArrayList();

        public int countRows() {
            try {
                if (this.items == null) return 0;
                int i = this.items.size();
                NetUser netuser = (NetUser) NetEnv.host();
                if (netuser.getPlace() == -1) i++;
                List list = NetEnv.hosts();
                for (int i_1_ = 0; i_1_ < list.size(); i_1_++) {
                    netuser = (NetUser) list.get(i_1_);
                    if (netuser.getPlace() == -1) i++;
                }
                return i;
            } catch (Exception ex) {
                ex.printStackTrace();
                return -1;
            }
        }

        public void renderCell(int i, int i_2_, boolean bool, float f, float f_3_) {
            try {
                this.setCanvasFont(0);
                if (bool) {
                    this.setCanvasColorBLACK();
                    this.draw(0.0F, 0.0F, f, f_3_, this.lookAndFeel().regionWhite);
                }
                if (bool) this.setCanvasColorWHITE();
                else this.setCanvasColorBLACK();
                Item item = null;
                if (i < this.items.size()) item = (Item) this.items.get(i);
                String string = null;
                int i_4_ = 1;
                switch (i_2_) {
                    case 0:
                        if (item != null) {
                            string = "" + item.indexInArmy;
                            if (item.reg.getArmy() == 1) this.setCanvasColor(GColor.Red);
                            else this.setCanvasColor(GColor.Blue);
                        }
                        break;
                    case 1:
                        if (item != null) {
                            float f_5_ = f_3_;
                            this.setCanvasColorWHITE();
                            this.draw(0.0F, 0.0F, f_5_, f_5_, item.texture);
                            if (bool) this.setCanvasColorWHITE();
                            else this.setCanvasColorBLACK();

                            // TODO: Altered by |ZUTI|: showing live AC pilot name instead of regiment
                            // --------------------------------------------------------
                            if (item.zutiLiveAcName == null) this.draw(1.5F * f_5_, 0.0F, f - 1.5F * f_5_, f_3_, 0, item.reg.shortInfo());
                            else this.draw(1.5F * f_5_, 0.0F, f - 1.5F * f_5_, f_3_, 0, item.zutiLiveAcName);
                            // --------------------------------------------------------
                        }
                        return;
                    case 2:
                        if (item != null) {
                            i_4_ = 0;
                            string = I18N.plane(item.keyName);
                        }
                        break;
                    case 3:
                        if (item != null) string = item.number;
                        break;
                    case 4:
                        if (item != null) string = item.cocName;
                        break;
                    case 5:
                        i_4_ = 0;
                        if (item != null) string = GUINetAircraft.this.findPlayer(i);
                        else {
                            int i_6_ = i - this.items.size();
                            NetUser netuser = (NetUser) NetEnv.host();
                            if (netuser.getPlace() == -1) if (i_6_ == 0) string = netuser.uniqueName();
                            else i_6_--;
                            if (string == null) {
                                List list = NetEnv.hosts();
                                for (int i_7_ = 0; i_7_ < list.size(); i_7_++) {
                                    netuser = (NetUser) list.get(i_7_);
                                    if (netuser.getPlace() == -1) {
                                        if (i_6_ == 0) {
                                            string = netuser.uniqueName();
                                            break;
                                        }
                                        i_6_--;
                                    }
                                }
                            }
                        }
                        break;
                }
                if (string != null) this.draw(0.0F, 0.0F, f, f_3_, i_4_, string);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void setSelect(int i, int i_8_) {
            try {
                if (GUINetAircraft.this.serverPlace == -1 && this.items != null && i >= 0 && i < this.items.size()) {
                    ((NetUser) NetEnv.host()).requestPlace(i);
                    ((NetUser) NetEnv.host()).setPilot(null);
                    ((NetUser) NetEnv.host()).setSkin(null);
                    ((NetUser) NetEnv.host()).setNoseart(null);
                    Item item = (Item) this.items.get(i);
                    EventLog.onTryOccupied("" + item.wingName + item.iAircraft, (NetUser) NetEnv.host(), NetAircraft.netCockpitAstatePilotIndx(item.clsAircraft, item.iCockpitNum));
                }
                super.setSelect(i, i_8_);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void afterCreated() {
            try {
                super.afterCreated();
                this.bColumnsSizable = true;
                this.bSelecting = true;
                this.bSelectRow = true;
                this.addColumn("N", null);
                this.addColumn(I18N.gui("netair.Regiment"), null);
                this.addColumn(I18N.gui("netair.Plane"), null);
                this.addColumn(I18N.gui("netair.Number"), null);
                this.addColumn(I18N.gui("netair.Position"), null);
                this.addColumn(I18N.gui("netair.Player"), null);
                this.vSB.scroll = this.rowHeight(0);
                this.getColumn(0).setRelativeDx(1.0F);
                this.getColumn(1).setRelativeDx(4.0F);
                this.getColumn(2).setRelativeDx(4.0F);
                this.getColumn(3).setRelativeDx(4.0F);
                this.getColumn(4).setRelativeDx(4.0F);
                this.getColumn(5).setRelativeDx(4.0F);
                this.alignColumns();
                this.wClient.bNotify = true;
                this.resized();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void resolutionChanged() {
            try {
                this.vSB.scroll = this.rowHeight(0);
                super.resolutionChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public Table(GWindow gwindow) {
            super(gwindow, 0.0F, 0.0F, 100.0F, 100.0F);
        }
    }

    public static class Item {
        public int      indexInArmy;
        public int      iSectWing;
        public int      iAircraft;
        public int      iCockpitNum;
        public String   cocName;
        public String   wingName;
        public String   keyName;
        public Regiment reg;
        public GTexture texture;
        public String   number;
        public Class    clsAircraft;

        // TODO: Added by |ZUTI|
        // ----------------------------------
        public String zutiLiveAcName = null;

        public String zutiGetUniqueId() {
            if (this.zutiLiveAcName == null) return this.reg.shortInfo() + this.keyName + this.number;

            return this.zutiLiveAcName + this.keyName + this.number;
        }
        // ----------------------------------
    }

    public static Item getItem(int i) {
        try {
            if (i < 0) return null;

            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);

            // TODO: Added by |ZUTI|
            // ----------------------------------
            if (guinetaircraft == null) guinetaircraft = new GUINetAircraft();
            // ----------------------------------

            guinetaircraft.fillListItems();

            if (!guinetaircraft.zutiIsDSConsole) {
                if (i >= guinetaircraft.wTable.items.size()) return null;
                return (Item) guinetaircraft.wTable.items.get(i);
            } else {
                if (i >= guinetaircraft.zutiItems.size()) return null;
                return (Item) guinetaircraft.zutiItems.get(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected static boolean isSelectedValid() {
        try {
            int i = ((NetUser) NetEnv.host()).getPlace();
            if (i < 0) return false;
            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);
            guinetaircraft.fillListItems();
            return i < guinetaircraft.wTable.items.size();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    protected static int selectedCockpitNum() {
        try {
            int i = ((NetUser) NetEnv.host()).getPlace();
            if (i < 0) return 0;
            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);
            guinetaircraft.fillListItems();
            Item item = (Item) guinetaircraft.wTable.items.get(i);
            return item.iCockpitNum;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    protected static String selectedSpawnName() {
        try {
            int i = ((NetUser) NetEnv.host()).getPlace();
            if (i < 0) return null;
            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);
            guinetaircraft.fillListItems();
            Item item = (Item) guinetaircraft.wTable.items.get(i);
            return item.wingName + item.iAircraft;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ZUTI_ERROR_NAME";
        }
    }

    protected static Regiment selectedRegiment() {
        try {
            int i = ((NetUser) NetEnv.host()).getPlace();
            if (i < 0) return null;
            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);
            guinetaircraft.fillListItems();
            return ((Item) guinetaircraft.wTable.items.get(i)).reg;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected static String selectedAircraftName() {
        try {
            return I18N.plane(selectedAircraftKeyName());
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ZUTI_ERROR_AIRCRAFT_NAME_1";
        }
    }

    protected static String selectedAircraftKeyName() {
        try {
            int i = ((NetUser) NetEnv.host()).getPlace();
            if (i < 0) return null;
            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);
            guinetaircraft.fillListItems();
            return ((Item) guinetaircraft.wTable.items.get(i)).keyName;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ZUTI_ERROR_AIRCRAFT_NAME_2";
        }
    }

    protected static int selectedAircraftNumInWing() {
        try {
            int i = ((NetUser) NetEnv.host()).getPlace();
            if (i < 0) return 0;
            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);
            guinetaircraft.fillListItems();
            return ((Item) guinetaircraft.wTable.items.get(i)).iAircraft;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    protected static String selectedWingName() {
        try {
            int i = ((NetUser) NetEnv.host()).getPlace();
            if (i < 0) return null;
            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);
            guinetaircraft.fillListItems();
            return ((Item) guinetaircraft.wTable.items.get(i)).wingName;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ZUTI_ERROR_WING_NAME";
        }
    }

    protected static Class selectedAircraftClass() {
        try {
            int i = ((NetUser) NetEnv.host()).getPlace();
            if (i < 0) return null;
            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);
            guinetaircraft.fillListItems();
            return ((Item) guinetaircraft.wTable.items.get(i)).clsAircraft;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static int serverPlace() {
        try {
            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);

            // TODO: Added by |ZUTI|
            // ----------------------------------
            if (guinetaircraft == null) guinetaircraft = new GUINetAircraft();
            // ----------------------------------

            guinetaircraft.fillListItems();

            if (guinetaircraft.sectMission == null) return -1;
            String string = guinetaircraft.sectMission.get("MAIN", "player", (String) null);
            if (string == null) return guinetaircraft.serverPlace = -1;
            int i = guinetaircraft.sectMission.get("MAIN", "playerNum", 0, 0, 3);
            int i_9_ = guinetaircraft.wTable.items.size();
            for (int i_10_ = 0; i_10_ < i_9_; i_10_++) {
                Item item = null;

                // TODO: Added by |ZUTI|
                // ----------------------------------
                if (!guinetaircraft.zutiIsDSConsole) item = (Item) guinetaircraft.wTable.items.get(i_10_);
                else item = (Item) guinetaircraft.zutiItems.get(i_10_);

                if (item.wingName.equals(string) && item.iAircraft == i) return guinetaircraft.serverPlace = i_10_;
            }
            return guinetaircraft.serverPlace = -1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    private void fillListItems() {
        try {
            if (Mission.cur() == null || Mission.cur().isDestroyed()) {
                this.sectMission = null;

                if (!this.zutiIsDSConsole) this.wTable.items.clear();
                else // TODO: Added by |ZUTI|
                     // ----------------------------------
                    this.zutiItems.clear();
                // ----------------------------------
            } else if (this.sectMission != Mission.cur().sectFile() && !GUINetAircraft.ZUTI_IS_GAME_DF) {
                this.wTable.items.clear();
                this.wTable.setSelect(-1, -1);

                this.serverPlace = -1;
                SectFile sectfile = Mission.cur().sectFile();

                this.createListItems(sectfile, 1);
                this.createListItems(sectfile, 2);

                this.sectMission = sectfile;
            }
            // TODO: Added by |ZUTI|: for dogfight mode
            // ----------------------------------
            else if (GUINetAircraft.ZUTI_IS_GAME_DF) {
                if (!this.zutiIsDSConsole) {
                    ZutiSupportMethods_Multicrew.updateNetUsersCrewPlaces();
                    this.wTable.items.clear();
                    this.wTable.setSelect(-1, -1);
                } else this.zutiItems.clear();

                this.serverPlace = -1;
                SectFile sectfile = Mission.cur().sectFile();

                boolean isNetMaster = false;
                if (Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster()) isNetMaster = true;

                List listRed = ZutiSupportMethods_GUI.createListItemsForMultiCrewEnabledAircraft(this.zutiIsDSConsole, sectfile, 1, isNetMaster);
                List listBlue = ZutiSupportMethods_GUI.createListItemsForMultiCrewEnabledAircraft(this.zutiIsDSConsole, sectfile, 2, isNetMaster);

                Collections.sort(listRed, new ZutiSupportMethods_Multicrew.CrewTableItem_CompareByName());
                Collections.sort(listBlue, new ZutiSupportMethods_Multicrew.CrewTableItem_CompareByName());

                Item tmpItem = null;
                if (!this.zutiIsDSConsole) {
                    int index = 1;
                    // Add red AC to table list
                    for (int i = 0; i < listRed.size(); i++) {
                        tmpItem = (Item) listRed.get(i);
                        tmpItem.indexInArmy = index;
                        this.wTable.items.add(tmpItem);

                        index++;
                    }
                    // Add blue AC to table list
                    for (int i = 0; i < listBlue.size(); i++) {
                        tmpItem = (Item) listBlue.get(i);
                        tmpItem.indexInArmy = index;
                        this.wTable.items.add(tmpItem);

                        index++;
                    }
                } else {
                    int index = 1;
                    // Add red AC to table list
                    for (int i = 0; i < listRed.size(); i++) {
                        tmpItem = (Item) listRed.get(i);
                        tmpItem.indexInArmy = index;
                        this.zutiItems.add(tmpItem);

                        index++;
                    }
                    // Add blue AC to table list
                    for (int i = 0; i < listBlue.size(); i++) {
                        tmpItem = (Item) listBlue.get(i);
                        tmpItem.indexInArmy = index;
                        this.zutiItems.add(tmpItem);

                        index++;
                    }
                }

                this.sectMission = sectfile;
            }
            // ----------------------------------
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createListItems(SectFile sectfile, int i) {
        try {
            int i_11_ = sectfile.sectionIndex("Wing");
            if (i_11_ >= 0) {
                int i_12_ = sectfile.vars(i_11_);
                int i_13_ = 1;
                for (int i_14_ = 0; i_14_ < i_12_; i_14_++) {
                    String string = sectfile.var(i_11_, i_14_);
                    if (sectfile.sectionIndex(string) >= 0 && string.length() >= 3) {
                        Regiment regiment = (Regiment) Actor.getByName(string.substring(0, string.length() - 2));
                        if (regiment.getArmy() == i) {
                            String string_15_ = sectfile.get(string, "Class", (String) null);
                            if (string_15_ != null) {
                                Class var_class;
                                try {
                                    var_class = ObjIO.classForName(string_15_);
                                } catch (Exception exception) {
                                    continue;
                                }
                                int i_16_ = sectfile.get(string, "StartTime", 0);
                                if (i_16_ <= 0) {
                                    boolean bool = sectfile.get(string, "OnlyAI", 0, 0, 1) == 1;
                                    if (!bool) {
                                        Object object_17_ = Property.value(var_class, "cockpitClass");
                                        if (object_17_ != null) {
                                            Class[] var_classes = null;
                                            int i_19_;
                                            if (object_17_ instanceof Class) i_19_ = 1;
                                            else {
                                                var_classes = (Class[]) object_17_;
                                                i_19_ = var_classes.length;
                                            }
                                            String string_20_ = Property.stringValue(var_class, "keyName", null);
                                            PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(var_class, regiment.country());
                                            int i_21_ = string.charAt(string.length() - 2) - 48;
                                            int i_22_ = string.charAt(string.length() - 1) - 48;
                                            Mat mat = PaintScheme.makeMatGUI(regiment.name() + "GUI", regiment.fileNameTga(), 1.0F, 1.0F, 1.0F);
                                            GTexture gtexture = GTexture.New(mat.Name());
                                            crewFunction[0] = 1;
                                            for (int i_24_ = 1; i_24_ < crewFunction.length; i_24_++)
                                                crewFunction[i_24_] = 7;
                                            String string_25_ = Property.stringValue(var_class, "FlightModel", null);
                                            SectFile sectfile_26_ = FlightModelMain.sectFile(string_25_);
                                            int i_27_ = sectfile_26_.get("Aircraft", "Crew", 1, 1, 20);
                                            for (int i_28_ = 0; i_28_ < crewFunction.length; i_28_++)
                                                crewFunction[i_28_] = sectfile_26_.get("Aircraft", "CrewFunction" + i_28_, crewFunction[i_28_], 1, AircraftState.astateHUDPilotHits.length);
                                            int i_29_ = sectfile.get(string, "Planes", 1, 1, 4);
                                            for (int i_30_ = 0; i_30_ < i_29_; i_30_++)
                                                for (int i_31_ = 0; i_31_ < i_19_; i_31_++)
                                                    if (i_31_ <= 0 || !CockpitPilot.class.isAssignableFrom(var_classes[i_31_])) {
                                                        Item item = new Item();
                                                        item.indexInArmy = i_13_++;
                                                        item.iSectWing = i_14_;
                                                        item.iAircraft = i_30_;
                                                        item.iCockpitNum = i_31_;
                                                        item.wingName = string;
                                                        item.keyName = string_20_;
                                                        item.cocName = AircraftState.astateHUDPilotHits[1];
                                                        if (var_classes != null) {
                                                            int i_32_ = Property.intValue(var_classes[i_31_], "astatePilotIndx", 0);
                                                            if (i_32_ < i_27_) {
                                                                i_32_ = crewFunction[i_32_];
                                                                if (i_32_ < AircraftState.astateHUDPilotHits.length) item.cocName = AircraftState.astateHUDPilotHits[i_32_];
                                                            }
                                                        }
                                                        item.cocName = I18N.hud_log(item.cocName);
                                                        item.reg = regiment;
                                                        item.clsAircraft = var_class;
                                                        item.number = paintscheme.typedName(var_class, regiment, i_21_, i_22_, i_30_);
                                                        item.texture = gtexture;

                                                        this.wTable.items.add(item);
                                                        System.out.println("Item added to the list!");
                                                    }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String findPlayer(int i) {
        try {
            NetUser netuser = (NetUser) NetEnv.host();
            if (netuser.getPlace() == i) return netuser.uniqueName();
            List list = NetEnv.hosts();
            for (int i_33_ = 0; i_33_ < list.size(); i_33_++) {
                netuser = (NetUser) list.get(i_33_);
                if (netuser.getPlace() == i) return netuser.uniqueName();
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ZUTI_ERROR_PLAYER_NAME";
        }
    }

    public void _enter() {
        try {
            // TODO: Added by |ZUTI|
            // ----------------------------------------------------
            GUINetAircraft.ZUTI_IS_GAME_DF = Mission.isDogfight();
            // ZutiSupportMethods_GUI.showAllMultiCrewEnabledAIrcraft();
            // ----------------------------------------------------

            this.fillListItems();
            this.client.activateWindow();
            this.wTable.resized();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void _leave() {
        try {
            this.client.hideWindow();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected static void doFly() {
        try {
            GUINetAircraft guinetaircraft = (GUINetAircraft) GameState.get(44);
            guinetaircraft._doFly();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void _doFly() {
        try {
            int i = ((NetUser) NetEnv.host()).getPlace();
            if (i >= 0) {
                this.fillListItems();

                // TODO: Edited by |ZUTI|
                // ----------------------------------
                Item item = null;
                if (!this.zutiIsDSConsole) item = (Item) this.wTable.items.get(i);
                else item = (Item) this.zutiItems.get(i);
                // ----------------------------------

                // TODO: Altered by |ZUTI|
                if (item.iCockpitNum == 0 && !GUINetAircraft.ZUTI_IS_GAME_DF) {
                    UserCfg usercfg = World.cur().userCfg;
                    int i_34_ = Main.cur().currentMissionFile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
                    if (i_34_ != 1) {
                        boolean bool = usercfg.getWeapon(item.keyName) != null;
                        if (bool) bool = Aircraft.weaponsExist(item.clsAircraft, usercfg.getWeapon(item.keyName));
                        if (!bool) {
                            GUIAirArming.stateId = 3;
                            Main.stateStack().push(55);
                            return;
                        }
                    }
                    Main.cur().resetUser();
                    ((NetUser) NetEnv.host()).checkReplicateSkin(item.keyName);
                    ((NetUser) NetEnv.host()).checkReplicateNoseart(item.keyName);
                    ((NetUser) NetEnv.host()).checkReplicatePilot();
                    String string = usercfg.getWeapon(item.keyName);
                    int i_35_ = (int) usercfg.fuel;
                    if (i_34_ == 1) {
                        string = Main.cur().currentMissionFile.get(selectedWingName(), "weapons", "default");
                        i_35_ = Main.cur().currentMissionFile.get(selectedWingName(), "Fuel", 100, 0, 100);
                    }
                    try {
                        CmdEnv.top().exec("spawn " + item.clsAircraft.getName() + " PLAYER NAME net" + item.wingName + item.iAircraft + " WEAPONS " + string + " FUEL " + i_35_ + (usercfg.netNumberOn ? "" : " NUMBEROFF") + " OVR");
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                    Aircraft aircraft = World.getPlayerAircraft();
                    if (!Actor.isValid(aircraft)) {
                        GWindowMessageBox gwindowmessagebox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, "Create aircraft", "Selected aircraft NOT created", 3, 0.0F);
                        GUINetClientGuard guinetclientguard = (GUINetClientGuard) Main.cur().netChannelListener;
                        if (guinetclientguard != null) guinetclientguard.curMessageBox = gwindowmessagebox;
                        return;
                    }
                } else {
                    Main.cur().resetUser();
                    // ((NetUser)NetEnv.host()).checkReplicatePilot();

                    /*
                     * String s = item.wingName + item.iAircraft; java.lang.String s1 = " " + s + "(" + item.iCockpitNum + ")"; System.out.println("WingName: " + item.wingName); System.out.println("iAircraft: " + item.iAircraft);
                     * System.out.println("Aircraft 1: " + s); System.out.println("Aircraft 2: " + s1);
                     *
                     * System.out.println("Entering: " + item.wingName + ", " + item.iAircraft + ", " + 0 + ", " + item.iCockpitNum);
                     */
                    NetGunner gunner = new NetGunner("" + item.wingName + item.iAircraft, (NetUser) NetEnv.host(), 0, item.iCockpitNum);

                    // TODO: Added by |ZUTI|: sync your position with other crew members
                    // ---------------------------------------------------------------------------------------
                    ZutiSupportMethods_NetSend.requestNewCockpitPosition(gunner.getAircraftName(), item.iCockpitNum, false);
                    ZutiSupportMethods_NetSend.requestAircraftCrew(gunner.getAircraftName());
                    // ---------------------------------------------------------------------------------------
                }

                // TODO: Edited by |ZUTI|
                if (!GUINetAircraft.ZUTI_IS_GAME_DF) ((NetUser) NetEnv.host()).doWaitStartCoopMission();

                if (Main.cur().netServerParams.isMaster()) {
                    // TODO: Edited by |ZUTI|
                    if (!GUINetAircraft.ZUTI_IS_GAME_DF) {
                        NetEnv.cur().connect.bindEnable(false);

                        Main.cur().netServerParams.USGSupdate();
                        if (Main.cur().netGameSpy != null) Main.cur().netGameSpy.sendStatechanged();

                        Main.stateStack().change(45);
                        Main.stateStack().change(47);
                    } else {
                        // TODO: Added by |ZUTI|
                        // Dogfight requires different GUI screens...
                        // This one is if we are hosting through IL2
                        Main.stateStack().change(39);

                        ZutiSupportMethods_Net.startDogfightGame(Main.cur().netServerParams);
                        com.maddox.il2.gui.GUI.unActivate();
                        com.maddox.rts.HotKeyCmd.exec("aircraftView", "CockpitView");
                        com.maddox.il2.game.Main.stateStack().change(42);
                    }
                } else // TODO: Edited by |ZUTI|
                    if (!GUINetAircraft.ZUTI_IS_GAME_DF) {
                        Main.stateStack().change(46);
                        Main.stateStack().change(48);
                    } else {
                        // TODO: Edited by |ZUTI|: And this part is for dogfight client GUI screens.
                        Main.stateStack().change(40);

                        ZutiSupportMethods_Net.startDogfightGame(Main.cur().netServerParams);
                        com.maddox.il2.gui.GUI.unActivate();
                        com.maddox.rts.HotKeyCmd.exec("aircraftView", "CockpitView");
                        com.maddox.il2.game.Main.stateStack().change(43);
                    }
            }

            // TODO: Added by |ZUTI|
            // ----------------------------------
            // disable joystick!
            ZutiSupportMethods_Multicrew.setJojstickState();
            // remove player from previously selected home base (if any)
            NetUser netuser = (NetUser) NetEnv.host();
            if (netuser != null) netuser.setBornPlace(-1);
            // ----------------------------------
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void init(GWindowRoot gwindowroot) {
        try {
            this.client = (GUIClient) gwindowroot.create(new GUIClient());
            this.dialogClient = (DialogClient) this.client.create(new DialogClient());
            this.infoMenu = (GUIInfoMenu) this.client.create(new GUIInfoMenu());
            this.infoMenu.info = this.i18n("netair.info");
            this.infoName = (GUIInfoName) this.client.create(new GUIInfoName());
            this.wTable = new Table(this.dialogClient);
            GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
            this.bPrev = (GUIButton) this.dialogClient.addEscape(new GUIButton(this.dialogClient, gtexture, 0.0F, 96.0F, 48.0F, 48.0F));
            this.bLoodout = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));

            // TODO: Added by |ZUTI|
            // -------------------------------------------------------------
            this.zutiRefreshButton = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
            // -------------------------------------------------------------

            this.bFly = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
            this.dialogClient.activateWindow();
            this.client.hideWindow();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public GUINetAircraft(GWindowRoot gwindowroot) {
        super(44);
        try {
            this.init(gwindowroot);
            this.zutiIsDSConsole = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // TODO: |ZUTI| methods
    // ----------------------------------------------
    private static boolean ZUTI_IS_GAME_DF = false;
    protected boolean      zutiIsDSConsole = false;
    protected ArrayList    zutiItems       = null;

    // New constructor for DS server
    public GUINetAircraft() {
        super(44);
        this.zutiIsDSConsole = true;
        GUINetAircraft.ZUTI_IS_GAME_DF = true;
        this.zutiItems = new ArrayList();
    }
    // ----------------------------------------------
}