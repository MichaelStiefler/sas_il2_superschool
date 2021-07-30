/*4.10.1 class*/
package com.maddox.il2.builder;

import java.util.ArrayList;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.TexImage.TexImage;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuBarItem;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowRootMenu;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Land2Dn;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Runaway;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.tools.BridgesGenerator;
import com.maddox.rts.HomePath;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;

public class PlMapLoad extends Plugin {
    private static ArrayList lands             = new ArrayList();
    private static int       landLoaded        = -1;
    public static ArrayList  bridgeActors      = new ArrayList();
    public static boolean    bDrawNumberBridge = false;
    private static Point3d   _p3d              = new Point3d();
    private Point2d          p2d               = new Point2d();
    MenuItem[]               menuItem;
    // TODO: Disabled by |ZUTI|
    // private GWindowMessageBox loadMessageBox;
    private Land             _guiLand;

    private Zuti_WMapsList   zuti_wMapList     = null;
    public GWindowMenuItem   zuti_mMapList     = null;

    class MenuItem extends GWindowMenuItem {
        int indx;

        public void execute() {
            Land land = (Land) PlMapLoad.lands.get(this.indx);
            if (land != getLandLoaded()) if (!Plugin.builder.bMultiSelect) {
                PlMapLoad.this._guiLand = land;
                ((PlMission) Plugin.getPlugin("Mission")).loadNewMap();
            } else PlMapLoad.this.guiMapLoad(land);
        }

        public MenuItem(GWindowMenu gwindowmenu, String string, String string_0_, int i) {
            super(gwindowmenu, string, string_0_);
            this.indx = i;
        }
    }

    public static class Land {
        public int    indx;
        public String keyName;
        public String i18nName;
        public String fileName;
        public String dirName;
    }

    public static Land getLandLoaded() {
        if (landLoaded < 0) return null;
        return (Land) lands.get(landLoaded);
    }

    public static Land getLandForKeyName(String string) {
        for (int i = 0; i < lands.size(); i++) {
            Land land = (Land) lands.get(i);
            if (land.keyName.equals(string)) return land;
        }
        return null;
    }

    public static Land getLandForFileName(String string) {
        for (int i = 0; i < lands.size(); i++) {
            Land land = (Land) lands.get(i);
            if (land.fileName.equals(string)) return land;
        }
        return null;
    }

    public static String mapKeyName() {
        Land land = getLandLoaded();
        if (land == null) return null;
        return land.keyName;
    }

    public static String mapI18nName() {
        Land land = getLandLoaded();
        if (land == null) return null;
        return land.i18nName;
    }

    public static String mapFileName() {
        Land land = getLandLoaded();
        if (land == null) return null;
        return land.fileName;
    }

    public static String mapDirName() {
        Land land = getLandLoaded();
        if (land == null) return null;
        return land.dirName;
    }

    private static void bridgesClear() {
        for (int i = 0; i < bridgeActors.size(); i++) {
            Actor actor = (Actor) bridgeActors.get(i);
            actor.destroy();
        }
        bridgeActors.clear();
    }

    public static void bridgesCreate(TexImage teximage) {
        bridgesClear();
        if (teximage != null) {
            com.maddox.il2.tools.Bridge[] bridges = BridgesGenerator.getBridgesArray(teximage);
            for (int i = 0; i < bridges.length; i++) {
                Bridge bridge = new Bridge(i, bridges[i].type, bridges[i].x1, bridges[i].y1, bridges[i].x2, bridges[i].y2, 0.0F);
                Property.set(bridge, "builderSpawn", "");
                bridgeActors.add(bridge);
                _p3d.x = World.land().PIX2WORLDX((bridges[i].x1 + bridges[i].x2) / 2);
                _p3d.y = World.land().PIX2WORLDY((bridges[i].y1 + bridges[i].y2) / 2);
                _p3d.z = 0.0;
                PlMapLabel.insert(_p3d);
            }
            System.out.println("" + bridges.length + " bridges created");
        }
    }

    public void mapUnload() {
        landLoaded = -1;
        this.clearMenuItems();
        bridgesClear();
        Plugin.doMapLoaded();
        PathFind.unloadMap();
    }

    public boolean mapLoad(Land land) {
        if (getLandLoaded() == land) return true;
        Plugin.builder.deleteAll();
        bridgesClear();
        landLoaded = -1;
        this.clearMenuItems();
        PathFind.unloadMap();
        Main3D.cur3D().resetGame();
        Plugin.builder.tip(Plugin.i18n("Loading") + " " + land.i18nName + "...");
        SectFile sectfile = new SectFile("maps/" + land.fileName, 0);
        int i = sectfile.sectionIndex("MAP2D");
        if (i < 0) {
            Plugin.builder.tipErr("section [MAP2D] not found in 'maps/" + land.fileName);
            return false;
        }
        int i_1_ = sectfile.vars(i);
        if (i_1_ == 0) {
            Plugin.builder.tipErr("section [MAP2D] in 'maps/" + land.fileName + " is empty");
            return false;
        }
        try {
            if (Plugin.builder.bMultiSelect) World.land().LoadMap(land.fileName, null);
            else {
                int[] is = null;
                int i_2_ = sectfile.sectionIndex("static");
                if (i_2_ >= 0 && sectfile.vars(i_2_) > 0) {
                    String string = sectfile.var(i_2_, 0);
                    if (string != null && string.length() > 0) {
                        string = HomePath.concatNames("maps/" + land.fileName, string);
                        is = Statics.readBridgesEndPoints(string);
                    }
                }
                World.land().LoadMap(land.fileName, is);
            }
        } catch (Exception exception) {
            Plugin.builder.tipErr("World.land().LoadMap() error: " + exception);
            return false;
        }
        World.cur().setCamouflage(World.land().config.camouflage);
        if (Main3D.cur3D().land2D != null) {
            if (!Main3D.cur3D().land2D.isDestroyed()) Main3D.cur3D().land2D.destroy();
            Main3D.cur3D().land2D = null;
        }
        Main3D.cur3D().land2D = new Land2Dn(land.fileName, World.land().getSizeX(), World.land().getSizeY());
        Plugin.builder.computeViewMap2D(-1.0, 0.0, 0.0);
        PathFind.tShip = new TexImage();
        PathFind.tNoShip = new TexImage();
        boolean bool = false;
        int i_3_ = sectfile.sectionIndex("TMAPED");
        if (i_3_ >= 0) {
            int i_4_ = sectfile.vars(i_3_);
            if (i_4_ > 0) {
                String string = "maps/" + land.dirName + "/" + sectfile.var(i_3_, 0);
                try {
                    PathFind.tShip.LoadTGA(string);
                    PathFind.tNoShip.LoadTGA(string);
                    TexImage teximage = new TexImage();
                    teximage.LoadTGA("maps/" + land.dirName + "/" + World.land().config.typeMap);
                    for (int i_5_ = 0; i_5_ < teximage.sy; i_5_++)
                        for (int i_6_ = 0; i_6_ < teximage.sx; i_6_++) {
                            int i_7_ = teximage.I(i_6_, i_5_) & 0xe0;
                            if (i_7_ != 0) {
                                PathFind.tShip.I(i_6_, i_5_, PathFind.tShip.intI(i_6_, i_5_) & ~0xe0 | i_7_);
                                PathFind.tNoShip.I(i_6_, i_5_, PathFind.tNoShip.intI(i_6_, i_5_) & ~0xe0 | i_7_);
                            }
                        }
                    bool = true;
                } catch (Exception exception) {
                    /* empty */
                }
            }
        }
        if (!bool) try {
            PathFind.tShip.LoadTGA("maps/" + land.dirName + "/" + World.land().config.typeMap);
            PathFind.tNoShip.LoadTGA("maps/" + land.dirName + "/" + World.land().config.typeMap);
        } catch (Exception exception) {
            /* empty */
        }
        for (int i_8_ = 0; i_8_ < PathFind.tShip.sy; i_8_++)
            for (int i_9_ = 0; i_9_ < PathFind.tShip.sx; i_9_++) {
                if ((PathFind.tShip.I(i_9_, i_8_) & 0x1c) == 24) PathFind.tShip.I(i_9_, i_8_, PathFind.tShip.intI(i_9_, i_8_) & ~0x1c);
                if ((PathFind.tNoShip.I(i_9_, i_8_) & 0x1c) == 24) PathFind.tNoShip.I(i_9_, i_8_, PathFind.tNoShip.intI(i_9_, i_8_) & ~0x1c);
            }
        Landscape landscape = World.land();
        for (int i_10_ = 0; i_10_ < PathFind.tShip.sy; i_10_++)
            for (int i_11_ = 0; i_11_ < PathFind.tShip.sx; i_11_++)
                if ((PathFind.tShip.intI(i_11_, i_10_) & 0x1c) == 28) {
                    if (landscape != null) {
                        /* empty */
                    }
                    if (Landscape.estimateNoWater(i_11_, i_10_, 128) > 255 - Plugin.builder.conf.iWaterLevel) PathFind.tShip.I(i_11_, i_10_, PathFind.tShip.intI(i_11_, i_10_) & ~0x1c);
                }
        for (int i_12_ = 0; i_12_ < PathFind.tNoShip.sy; i_12_++)
            for (int i_13_ = 0; i_13_ < PathFind.tNoShip.sx; i_13_++)
                if ((PathFind.tNoShip.intI(i_13_, i_12_) & 0x1c) == 28) {
                    if (landscape != null) {
                        /* empty */
                    }
                    if (Landscape.estimateNoWater(i_13_, i_12_, 128) > 250) PathFind.tNoShip.I(i_13_, i_12_, PathFind.tNoShip.intI(i_13_, i_12_) & ~0x1c);
                }
        Plugin.builder.tip(land.i18nName);
        landLoaded = land.indx;
        if (this.menuItem != null) for (int i_14_ = 0; i_14_ < this.menuItem.length; i_14_++)
            this.menuItem[i_14_].bChecked = i_14_ == landLoaded;
        Plugin.doMapLoaded();
        PathFind.b = new com.maddox.il2.tools.Bridge[bridgeActors.size()];
        for (int i_15_ = 0; i_15_ < bridgeActors.size(); i_15_++) {
            Bridge bridge = (Bridge) bridgeActors.get(i_15_);
            int i_16_ = bridge.__indx;
            PathFind.b[i_16_] = new com.maddox.il2.tools.Bridge();
            PathFind.b[i_16_].x1 = bridge.__x1;
            PathFind.b[i_16_].y1 = bridge.__y1;
            PathFind.b[i_16_].x2 = bridge.__x2;
            PathFind.b[i_16_].y2 = bridge.__y2;
            PathFind.b[i_16_].type = bridge.__type;
        }
        PathFind.setMoverType(0);
        return true;
    }

    public void renderMap2D() {
        if (!Plugin.builder.isFreeView() && getLandLoaded() != null) {
            if (Plugin.builder.conf.bViewBridge) {
                Render.prepareStates();
                IconDraw.setColor(255, 255, 255, 255);
                for (int i = 0; i < bridgeActors.size(); i++) {
                    Bridge bridge = (Bridge) bridgeActors.get(i);
                    if (Plugin.builder.project2d(bridge.pos.getAbsPoint(), this.p2d)) IconDraw.render(bridge, this.p2d.x, this.p2d.y);
                }
                if (bDrawNumberBridge || Plugin.builder.bMultiSelect) for (int i = 0; i < bridgeActors.size(); i++) {
                    Bridge bridge = (Bridge) bridgeActors.get(i);
                    if (Plugin.builder.project2d(bridge.pos.getAbsPoint(), this.p2d)) TextScr.font().output(-16711936, (int) this.p2d.x + IconDraw.scrSizeX() / 2 + 2, (int) this.p2d.y - IconDraw.scrSizeY() / 2 - 2, 0.0F, "" + bridge.__indx);
                }
            }
            if (Plugin.builder.conf.bViewRunaway) {
                IconDraw.setColor(255, 255, 255, 255);
                for (Runaway runaway = World.cur().runawayList; runaway != null; runaway = runaway.next())
                    if (Plugin.builder.project2d(runaway.pos.getAbsPoint(), this.p2d)) IconDraw.render(runaway, this.p2d.x, this.p2d.y);
            }
        }
    }

    private void clearMenuItems() {
        if (this.menuItem != null) for (int i = 0; i < this.menuItem.length; i++)
            this.menuItem[i].bChecked = false;
    }

    // TODO: Comment by |ZUTI|: Original method
    /*
     * public void createGUI() { GWindowRootMenu gwindowrootmenu = (GWindowRootMenu)Plugin.builder.clientWindow.root; GWindowMenuBarItem gwindowmenubaritem = gwindowrootmenu.menuBar.getItem(0); GWindowMenuItem gwindowmenuitem =
     * (gwindowmenubaritem.subMenu.addItem(0, new GWindowMenuItem(gwindowmenubaritem.subMenu, Plugin.i18n("&MapLoad"), Plugin.i18n("TIPLoadLandscape")))); gwindowmenuitem.subMenu = (GWindowMenu)gwindowmenuitem.create(new GWindowMenu());
     * gwindowmenuitem.subMenu.close(false); int i = lands.size(); menuItem = new MenuItem[i]; for (int i_17_ = 0; i_17_ < i; i_17_++) { Land land = (Land)lands.get(i_17_); gwindowmenuitem.subMenu.addItem(menuItem[i_17_] = new
     * MenuItem((gwindowmenuitem.subMenu), land.i18nName, null, i_17_)); menuItem[i_17_].bChecked = false; } }
     */

    // TODO: |ZUTI| method
    // -------------------------------------------------------------------------------------
    public void createGUI() {
        GWindowRootMenu gwindowrootmenu = (GWindowRootMenu) Plugin.builder.clientWindow.root;
        GWindowMenuBarItem gwindowmenubaritem = gwindowrootmenu.menuBar.getItem(0);
        gwindowmenubaritem.subMenu.addItem(0, new GWindowMenuItem(gwindowmenubaritem.subMenu, Plugin.i18n("&MapLoad"), Plugin.i18n("TIPLoadLandscape")) {
            public void execute() {
                if (PlMapLoad.this.zuti_wMapList.isVisible()) PlMapLoad.this.zuti_wMapList.hideWindow();
                else PlMapLoad.this.zuti_wMapList.showWindow();
            }
        });

        this.zuti_wMapList = new Zuti_WMapsList();
        this.zuti_wMapList.setPlMapLoad(this);
        this.zuti_wMapList.loadMaps(lands);
    }
    // -------------------------------------------------------------------------------------

    public void guiMapLoad() {
        this.guiMapLoad(this._guiLand);
    }

    public void guiMapLoad(Land land) {
        this._guiLand = land;
        // TODO: Disabled by |ZUTI|
        // loadMessageBox = new GWindowMessageBox(Plugin.builder.clientWindow.root, 20.0F, true, Plugin.i18n("StandBy"), (Plugin.i18n("LoadingLandscape") + " " + land.i18nName), 4, 0.0F);
        // TODO: Added by |ZUTI|
        this.zuti_wMapList.setTitle(land.i18nName);
        new MsgAction(72, 0.0) {
            public void doAction() {
                PlMapLoad.this.mapLoad(PlMapLoad.this._guiLand);

                // TODO: Disabled by |ZUTI|
                /*
                 * loadMessageBox.close(false); loadMessageBox = null;
                 */

                // TODO: Added by |ZUTI|
                PlMapLoad.this.zuti_wMapList.hideWindow();
            }
        };
    }

    public void configure() {
        SectFile sectfile = new SectFile("maps/all.ini", 0);
        int i = sectfile.sectionIndex("all");
        if (i >= 0) {
            int i_19_ = sectfile.vars(i);
            for (int i_20_ = 0; i_20_ < i_19_; i_20_++) {
                Land land = new Land();
                land.indx = i_20_;
                land.keyName = sectfile.var(i, i_20_);
                land.fileName = sectfile.value(i, i_20_);
                land.dirName = land.fileName.substring(0, land.fileName.lastIndexOf("/"));
                land.i18nName = I18N.map(land.keyName);
                lands.add(land);
            }
        }
    }

    static {
        Property.set(PlMapLoad.class, "name", "MapLoad");
    }

    // TODO: |ZUTI| variables and methods
    // --------------------------------------------------------
    /**
     * Call this method when you want to load selected map.
     */
    public void zutiExecute(int index) {
        // TODO: Added by |ZUTI|
        // -------------------------------------------------
        ZutiPlMDS plMDS = (ZutiPlMDS) Plugin.getPlugin("ZutiMDS");
        plMDS.toggleAirdromeInfrastructureMenuItem.bChecked = false;
        plMDS.toggleSpawnPlaceIndicatorsMenuItem.bChecked = false;
        // -------------------------------------------------

        Land land = (Land) PlMapLoad.lands.get(index);
        if (land != getLandLoaded()) if (!Plugin.builder.bMultiSelect) {
            this._guiLand = land;
            ((PlMission) Plugin.getPlugin("Mission")).loadNewMap();
        } else this.guiMapLoad(land);
    }
    // ----------------------------------------------------------
}