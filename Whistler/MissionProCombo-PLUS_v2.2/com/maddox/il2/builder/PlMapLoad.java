package com.maddox.il2.builder;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple2d;
import com.maddox.JGP.Tuple2f;
import com.maddox.JGP.Tuple3d;
import com.maddox.TexImage.TexImage;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuBar;
import com.maddox.gwindow.GWindowMenuBarItem;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRootMenu;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AirportMaritime;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Airdrome;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Land2D;
import com.maddox.il2.engine.Land2Dn;
import com.maddox.il2.engine.LandConf;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Runaway;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.tools.BridgesGenerator;
import com.maddox.rts.HomePath;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import java.io.PrintStream;
import java.util.ArrayList;

public class PlMapLoad extends Plugin
{
    public static class Land
    {

        public int indx;
        public String keyName;
        public String i18nName;
        public String fileName;
        public String dirName;

        public Land()
        {
        }
    }

    class MenuItem extends GWindowMenuItem
    {

        public void execute()
        {
            Land land = (Land)PlMapLoad.lands.get(indx);
            if(land == PlMapLoad.getLandLoaded())
                return;
            if(!Plugin.builder.bMultiSelect)
            {
                _guiLand = land;
                ((PlMission)Plugin.getPlugin("Mission")).loadNewMap();
            } else
            {
                guiMapLoad(land);
            }
        }

        int indx;

        public MenuItem(GWindowMenu gwindowmenu, String s, String s1, int i)
        {
            super(gwindowmenu, s, s1);
            indx = i;
        }
    }


    public PlMapLoad()
    {
        lineNXYZ = null;
        p2d = null;
        menuItem = null;
        loadMessageBox = null;
        _guiLand = null;
        lineNXYZ = new float[6];
        p2d = new Point2d();
    }

    public static Land getLandLoaded()
    {
        if(landLoaded < 0)
            return null;
        else
            return (Land)lands.get(landLoaded);
    }

    public static Land getLandForKeyName(String s)
    {
        for(int i = 0; i < lands.size(); i++)
        {
            Land land = (Land)lands.get(i);
            if(land.keyName.equals(s))
                return land;
        }

        return null;
    }

    public static Land getLandForFileName(String s)
    {
        for(int i = 0; i < lands.size(); i++)
        {
            Land land = (Land)lands.get(i);
            if(land.fileName.equals(s))
                return land;
        }

        return null;
    }

    public static String mapKeyName()
    {
        Land land = getLandLoaded();
        if(land == null)
            return null;
        else
            return land.keyName;
    }

    public static String mapI18nName()
    {
        Land land = getLandLoaded();
        if(land == null)
            return null;
        else
            return land.i18nName;
    }

    public static String mapFileName()
    {
        Land land = getLandLoaded();
        if(land == null)
            return null;
        else
            return land.fileName;
    }

    public static String mapDirName()
    {
        Land land = getLandLoaded();
        if(land == null)
            return null;
        else
            return land.dirName;
    }

    private static void bridgesClear()
    {
        for(int i = 0; i < bridgeActors.size(); i++)
        {
            Actor actor = (Actor)bridgeActors.get(i);
            actor.destroy();
        }

        bridgeActors.clear();
    }

    public static void bridgesCreate(TexImage teximage)
    {
        bridgesClear();
        if(teximage != null)
        {
            com.maddox.il2.tools.Bridge abridge[] = BridgesGenerator.getBridgesArray(teximage);
            for(int i = 0; i < abridge.length; i++)
            {
                Bridge bridge = new Bridge(i, abridge[i].type, abridge[i].x1, abridge[i].y1, abridge[i].x2, abridge[i].y2, 0.0F);
                Property.set(bridge, "builderSpawn", "");
                bridgeActors.add(bridge);
                _p3d.x = World.land().PIX2WORLDX((abridge[i].x1 + abridge[i].x2) / 2);
                _p3d.y = World.land().PIX2WORLDY((abridge[i].y1 + abridge[i].y2) / 2);
                _p3d.z = 0.0D;
                PlMapLabel.insert(_p3d);
            }

            System.out.println("" + abridge.length + " bridges created");
        }
    }

    public void mapUnload()
    {
        landLoaded = -1;
        clearMenuItems();
        bridgesClear();
        Plugin.doMapLoaded();
        PathFind.unloadMap();
    }

    public boolean mapLoad(Land land)
    {
        if(getLandLoaded() == land)
            return true;
        Plugin.builder.deleteAll();
        bridgesClear();
        landLoaded = -1;
        clearMenuItems();
        PathFind.unloadMap();
        Main3D.cur3D().resetGame();
        Plugin.builder.tip(Plugin.i18n("Loading") + " " + land.i18nName + "...");
        SectFile sectfile = new SectFile("maps/" + land.fileName, 0);
        int i = sectfile.sectionIndex("MAP2D");
        if(i < 0)
        {
            Plugin.builder.tipErr("section [MAP2D] not found in 'maps/" + land.fileName);
            return false;
        }
        int j = sectfile.vars(i);
        if(j == 0)
        {
            Plugin.builder.tipErr("section [MAP2D] in 'maps/" + land.fileName + " is empty");
            return false;
        }
        try
        {
            if(Plugin.builder.bMultiSelect)
            {
                World.land().LoadMap(land.fileName, null);
            } else
            {
                int ai[] = null;
                int k = sectfile.sectionIndex("static");
                if(k >= 0 && sectfile.vars(k) > 0)
                {
                    String s = sectfile.var(k, 0);
                    if(s != null && s.length() > 0)
                    {
                        s = HomePath.concatNames("maps/" + land.fileName, s);
                        ai = Statics.readBridgesEndPoints(s);
                    }
                }
                World.land().LoadMap(land.fileName, ai);
            }
        }
        catch(Exception exception)
        {
            Plugin.builder.tipErr("World.land().LoadMap() error: " + exception);
            return false;
        }
        World.cur().setCamouflage(World.land().config.camouflage);
        if(Main3D.cur3D().land2D != null)
        {
            if(!Main3D.cur3D().land2D.isDestroyed())
                Main3D.cur3D().land2D.destroy();
            Main3D.cur3D().land2D = null;
        }
        Main3D.cur3D().land2D = new Land2Dn(land.fileName, World.land().getSizeX(), World.land().getSizeY());
        Plugin.builder.computeViewMap2D(-1D, 0.0D, 0.0D);
        PathFind.tShip = new TexImage();
        PathFind.tNoShip = new TexImage();
        boolean flag = false;
        int l = sectfile.sectionIndex("TMAPED");
        if(l >= 0)
        {
            int i1 = sectfile.vars(l);
            if(i1 > 0)
            {
                String s1 = "maps/" + land.dirName + "/" + sectfile.var(l, 0);
                try
                {
                    PathFind.tShip.LoadTGA(s1);
                    PathFind.tNoShip.LoadTGA(s1);
                    TexImage teximage = new TexImage();
                    teximage.LoadTGA("maps/" + land.dirName + "/" + World.land().config.typeMap);
                    for(int j3 = 0; j3 < teximage.sy; j3++)
                    {
                        for(int l3 = 0; l3 < teximage.sx; l3++)
                        {
                            int i4 = teximage.I(l3, j3) & 0xe0;
                            if(i4 != 0)
                            {
                                PathFind.tShip.I(l3, j3, PathFind.tShip.intI(l3, j3) & 0xffffff1f | i4);
                                PathFind.tNoShip.I(l3, j3, PathFind.tNoShip.intI(l3, j3) & 0xffffff1f | i4);
                            }
                        }

                    }

                    flag = true;
                }
                catch(Exception exception2) { }
            }
        }
        if(!flag)
            try
            {
                PathFind.tShip.LoadTGA("maps/" + land.dirName + "/" + World.land().config.typeMap);
                PathFind.tNoShip.LoadTGA("maps/" + land.dirName + "/" + World.land().config.typeMap);
            }
            catch(Exception exception1) { }
        for(int j1 = 0; j1 < PathFind.tShip.sy; j1++)
        {
            for(int k1 = 0; k1 < PathFind.tShip.sx; k1++)
            {
                if((PathFind.tShip.I(k1, j1) & 0x1c) == 24)
                    PathFind.tShip.I(k1, j1, PathFind.tShip.intI(k1, j1) & 0xffffffe3);
                if((PathFind.tNoShip.I(k1, j1) & 0x1c) == 24)
                    PathFind.tNoShip.I(k1, j1, PathFind.tNoShip.intI(k1, j1) & 0xffffffe3);
            }

        }

        Landscape landscape = World.land();
        for(int l1 = 0; l1 < PathFind.tShip.sy; l1++)
        {
            for(int l2 = 0; l2 < PathFind.tShip.sx; l2++)
                if((PathFind.tShip.intI(l2, l1) & 0x1c) == 28)
                {
                    Landscape _tmp = landscape;
                    if(Landscape.estimateNoWater(l2, l1, 128) > 255 - Plugin.builder.conf.iWaterLevel)
                        PathFind.tShip.I(l2, l1, PathFind.tShip.intI(l2, l1) & 0xffffffe3);
                }

        }

        for(int i2 = 0; i2 < PathFind.tNoShip.sy; i2++)
        {
            for(int i3 = 0; i3 < PathFind.tNoShip.sx; i3++)
                if((PathFind.tNoShip.intI(i3, i2) & 0x1c) == 28)
                {
                    Landscape _tmp1 = landscape;
                    if(Landscape.estimateNoWater(i3, i2, 128) > 250)
                        PathFind.tNoShip.I(i3, i2, PathFind.tNoShip.intI(i3, i2) & 0xffffffe3);
                }

        }

        Plugin.builder.tip(land.i18nName);
        landLoaded = land.indx;
        if(menuItem != null)
        {
            for(int j2 = 0; j2 < menuItem.length; j2++)
                menuItem[j2].bChecked = j2 == landLoaded;

        }
        Plugin.doMapLoaded();
        PathFind.b = new com.maddox.il2.tools.Bridge[bridgeActors.size()];
        for(int k2 = 0; k2 < bridgeActors.size(); k2++)
        {
            Bridge bridge = (Bridge)bridgeActors.get(k2);
            int k3 = bridge.__indx;
            PathFind.b[k3] = new com.maddox.il2.tools.Bridge();
            PathFind.b[k3].x1 = bridge.__x1;
            PathFind.b[k3].y1 = bridge.__y1;
            PathFind.b[k3].x2 = bridge.__x2;
            PathFind.b[k3].y2 = bridge.__y2;
            PathFind.b[k3].type = bridge.__type;
        }

        PathFind.setMoverType(0);
        return true;
    }

    public void renderMap2D()
    {
        if(Plugin.builder.isFreeView())
            return;
        if(getLandLoaded() == null)
            return;
        if(Plugin.builder.conf.bViewBridge)
        {
            Render.prepareStates();
            IconDraw.setColor(255, 255, 255, 255);
            for(int i = 0; i < bridgeActors.size(); i++)
            {
                Bridge bridge = (Bridge)bridgeActors.get(i);
                if(Plugin.builder.project2d(((Actor) (bridge)).pos.getAbsPoint(), p2d))
                    IconDraw.render(bridge, ((Tuple2d) (p2d)).x, ((Tuple2d) (p2d)).y);
            }

            if(bDrawNumberBridge || Plugin.builder.bMultiSelect)
            {
                for(int j = 0; j < bridgeActors.size(); j++)
                {
                    Bridge bridge1 = (Bridge)bridgeActors.get(j);
                    if(Plugin.builder.project2d(((Actor) (bridge1)).pos.getAbsPoint(), p2d))
                        TextScr.font().output(0xff00ff00, (int)((Tuple2d) (p2d)).x + IconDraw.scrSizeX() / 2 + 2, (int)((Tuple2d) (p2d)).y - IconDraw.scrSizeY() / 2 - 2, 0.0F, "" + bridge1.__indx);
                }

            }
        }
        if(Plugin.builder.conf.bViewRunaway)
        {
            IconDraw.setColor(255, 255, 255, 255);
            for(Runaway runaway = World.cur().runawayList; runaway != null; runaway = runaway.next())
                if(Plugin.builder.project2d(((Actor) (runaway)).pos.getAbsPoint(), p2d))
                    IconDraw.render(runaway, ((Tuple2d) (p2d)).x, ((Tuple2d) (p2d)).y);

        }
        if(Plugin.builder.conf.bViewSpawn && Plugin.builder.isView3D())
        {
            com.maddox.il2.ai.air.Point_Stay apoint_stay[][] = World.cur().airdrome.stay;
            com.maddox.il2.ai.air.Point_Taxi apoint_taxi[][] = World.cur().airdrome.taxi;
            Point2d point2d = new Point2d();
            for(int k = 0; k < apoint_stay.length; k++)
            {
                Point3d point3d = new Point3d();
                if(apoint_stay[k].length >= 2)
                {
                    point3d.set(((Tuple2f) (apoint_stay[k][1])).x, ((Tuple2f) (apoint_stay[k][1])).y, Landscape.HQ_Air(((Tuple2f) (apoint_stay[k][1])).x, ((Tuple2f) (apoint_stay[k][1])).y));
                    Point3d point3d1 = new Point3d();
                    point3d1.set(((Tuple2f) (apoint_stay[k][0])).x, ((Tuple2f) (apoint_stay[k][0])).y, Landscape.HQ_Air(((Tuple2f) (apoint_stay[k][1])).x, ((Tuple2f) (apoint_stay[k][1])).y));
                    Airport airport = Airport.nearest(point3d, -1, 3);
                    boolean flag = Plugin.builder.project2d(point3d, p2d);
                    boolean flag1 = Plugin.builder.project2d(point3d1, point2d);
                    if(airport instanceof AirportMaritime)
                    {
                        IconDraw.setColor(173, 223, 255, 255);
                        if(flag || flag1)
                        {
                            lineNXYZ[0] = (float)((Tuple2d) (p2d)).x;
                            lineNXYZ[1] = (float)((Tuple2d) (p2d)).y;
                            lineNXYZ[2] = 0.0F;
                            lineNXYZ[3] = (float)((Tuple2d) (point2d)).x;
                            lineNXYZ[4] = (float)((Tuple2d) (point2d)).y;
                            lineNXYZ[5] = 0.0F;
                            Render.drawLines(lineNXYZ, 2, 3F, -8275, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND, 3);
                            if(flag)
                                IconDraw.render(Mat.New("icons/spawnPointMaritime.mat"), ((Tuple2d) (p2d)).x, ((Tuple2d) (p2d)).y);
                            if(flag1)
                                IconDraw.render(Mat.New("icons/normfly.mat"), ((Tuple2d) (point2d)).x, ((Tuple2d) (point2d)).y);
                        }
                    } else
                    {
                        IconDraw.setColor(64, 128, 128, 255);
                        if(flag || flag1)
                        {
                            point3d.set(((Tuple2f) (apoint_stay[k][0])).x, ((Tuple2f) (apoint_stay[k][0])).y, Landscape.HQ_Air(((Tuple2f) (apoint_stay[k][1])).x, ((Tuple2f) (apoint_stay[k][1])).y));
                            Plugin.builder.project2d(point3d, point2d);
                            lineNXYZ[0] = (float)((Tuple2d) (p2d)).x;
                            lineNXYZ[1] = (float)((Tuple2d) (p2d)).y;
                            lineNXYZ[2] = 0.0F;
                            lineNXYZ[3] = (float)((Tuple2d) (point2d)).x;
                            lineNXYZ[4] = (float)((Tuple2d) (point2d)).y;
                            lineNXYZ[5] = 0.0F;
                            Render.drawLines(lineNXYZ, 2, 3F, 0xff808040, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 3);
                            if(flag)
                                IconDraw.render(Mat.New("icons/spawnPointGround.mat"), ((Tuple2d) (p2d)).x, ((Tuple2d) (p2d)).y);
                            if(flag1)
                                IconDraw.render(Mat.New("icons/normfly.mat"), ((Tuple2d) (point2d)).x, ((Tuple2d) (point2d)).y);
                        }
                    }
                }
            }

            for(int l = 0; l < apoint_taxi.length; l++)
            {
                Render.drawBeginLines(-1);
                float af[] = new float[apoint_taxi[l].length * 3];
                for(int i1 = 0; i1 < apoint_taxi[l].length; i1++)
                {
                    Point3d point3d2 = new Point3d();
                    point3d2.set(((Tuple2f) (apoint_taxi[l][i1])).x, ((Tuple2f) (apoint_taxi[l][i1])).y, Landscape.HQ_Air(((Tuple2f) (apoint_taxi[l][i1])).x, ((Tuple2f) (apoint_taxi[l][i1])).y));
                    Plugin.builder.project2d(point3d2, p2d);
                    af[i1 * 3 + 0] = (float)((Tuple2d) (p2d)).x;
                    af[i1 * 3 + 1] = (float)((Tuple2d) (p2d)).y;
                    af[i1 * 3 + 2] = 0.0F;
                }

                Render.drawLines(af, apoint_taxi[l].length, 3F, -1, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND, 3);
                Render.drawEnd();
                for(int j1 = 0; j1 < apoint_taxi[l].length; j1++)
                {
                    Point3d point3d3 = new Point3d();
                    point3d3.set(((Tuple2f) (apoint_taxi[l][j1])).x, ((Tuple2f) (apoint_taxi[l][j1])).y, Landscape.HQ_Air(((Tuple2f) (apoint_taxi[l][j1])).x, ((Tuple2f) (apoint_taxi[l][j1])).y));
                    IconDraw.setColor(255, 255, 255, 255);
                    if(Plugin.builder.project2d(point3d3, p2d))
                        IconDraw.render(Mat.New("icons/normfly.mat"), ((Tuple2d) (p2d)).x, ((Tuple2d) (p2d)).y);
                }

            }

        }
    }

    private void clearMenuItems()
    {
        if(menuItem != null)
        {
            for(int i = 0; i < menuItem.length; i++)
                menuItem[i].bChecked = false;

        }
    }

    public void createGUI()
    {
        int x = 0;
        GWindowRootMenu gwindowrootmenu = (GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root;
        GWindowMenuBarItem gwindowmenubaritem = gwindowrootmenu.menuBar.getItem(1);
        GWindowMenuItem gwindowmenuitem = null;
        int i = lands.size();
        menuItem = new MenuItem[i];
        for(int j = 0; j < i; j++)
        {
            Land land = (Land)lands.get(j);
            if(land.i18nName.indexOf("*") == 0 || gwindowmenuitem == null)
            {
                if(land.i18nName.indexOf("*") == -1)
                    gwindowmenuitem = gwindowmenubaritem.subMenu.addItem(x++, new GWindowMenuItem(gwindowmenubaritem.subMenu, Plugin.i18n("&MapLoad"), Plugin.i18n("TIPLoadLandscape")));
                else
                    gwindowmenuitem = gwindowmenubaritem.subMenu.addItem(x++, new GWindowMenuItem(gwindowmenubaritem.subMenu, land.i18nName, Plugin.i18n("TIPLoadLandscape") + " from " + land.i18nName));
                gwindowmenuitem.subMenu = (GWindowMenu)gwindowmenuitem.create(new GWindowMenu());
                gwindowmenuitem.subMenu.close(false);
            }
            if(gwindowmenuitem != null)
            {
                gwindowmenuitem.subMenu.addItem(menuItem[j] = new MenuItem(gwindowmenuitem.subMenu, land.i18nName, null, j));
                menuItem[j].bChecked = false;
            }
        }

    }

    public void guiMapLoad()
    {
        guiMapLoad(_guiLand);
    }

    public void guiMapLoad(Land land)
    {
        _guiLand = land;
        loadMessageBox = new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 20F, true, Plugin.i18n("StandBy"), Plugin.i18n("LoadingLandscape") + " " + land.i18nName, 4, 0.0F);
        new MsgAction(72, 0.0D) {

            public void doAction()
            {
                mapLoad(_guiLand);
                loadMessageBox.close(false);
                loadMessageBox = null;
            }

        }
;
    }

    public void configure()
    {
        SectFile sectfile = new SectFile("maps/all.ini", 0);
        int i = sectfile.sectionIndex("all");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        for(int k = 0; k < j; k++)
        {
            Land land = new Land();
            land.indx = k;
            land.keyName = sectfile.var(i, k);
            land.fileName = sectfile.value(i, k);
            land.dirName = land.fileName.substring(0, land.fileName.lastIndexOf("/"));
            land.i18nName = I18N.map(land.keyName);
            lands.add(land);
        }

    }

    private static ArrayList lands = new ArrayList();
    private static int landLoaded = -1;
    public static ArrayList bridgeActors = new ArrayList();
    public static boolean bDrawNumberBridge = false;
    private static Point3d _p3d = new Point3d();
    private float lineNXYZ[];
    private Point2d p2d;
    MenuItem menuItem[];
    private GWindowMessageBox loadMessageBox;
    private Land _guiLand;

    static 
    {
        Property.set(com.maddox.il2.builder.PlMapLoad.class, "name", "MapLoad");
    }
}
