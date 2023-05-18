package com.maddox.il2.builder;

import com.maddox.JGP.Point3d;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.bridges.*;
import com.maddox.il2.objects.buildings.House;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.util.HashMapInt;
import java.util.AbstractCollection;
import java.util.BitSet;

public class PlMisDestruction extends Plugin
{
    class WDialog extends GWindowFramed
    {

        public void windowShown()
        {
            mDialog.bChecked = true;
            if(tiles == null)
                tilesNew();
            super.windowShown();
        }

        public void windowHidden()
        {
            mDialog.bChecked = false;
            super.windowHidden();
        }

        public void created()
        {
            bAlwaysOnTop = true;
            super.created();
            title = Plugin.i18n("Destruction");
            GWindowDialogClient gwindowdialogclient;
            clientWindow = create(gwindowdialogclient = new GWindowDialogClient());
            GWindowLabel gwindowlabel;
            gwindowdialogclient.addLabel(gwindowlabel = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 11F, 1.3F, Plugin.i18n("DestLight"), null));
            gwindowlabel.align = 2;
            gwindowdialogclient.addLabel(gwindowlabel = new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 11F, 1.3F, Plugin.i18n("DestSize"), null));
            gwindowlabel.align = 2;
            gwindowdialogclient.addLabel(gwindowlabel = new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 11F, 1.3F, Plugin.i18n("DestValue"), null));
            gwindowlabel.align = 2;
            if(Plugin.builder.conf.iLightDestruction < 0)
                Plugin.builder.conf.iLightDestruction = 0;
            if(Plugin.builder.conf.iLightDestruction > 255)
                Plugin.builder.conf.iLightDestruction = 255;
            gwindowdialogclient.addControl(wLight = new GWindowHSliderInt(gwindowdialogclient, 0, 256, Plugin.builder.conf.iLightDestruction, 13F, 1.0F, 10F) {

                public boolean notify(int i, int j)
                {
                    Plugin.builder.conf.iLightDestruction = pos();
                    return super.notify(i, j);
                }

                public void created()
                {
                    bSlidingNotify = true;
                }

            }
);
            wLight.toolTip = Plugin.i18n("TIPDestLight");
            wLight.resized();
            gwindowdialogclient.addControl(wSize = new GWindowHSliderInt(gwindowdialogclient, 0, 7, 0, 13F, 3F, 10F) {

                public boolean notify(int i, int j)
                {
                    fillSize = (int)Math.pow(2D, pos());
                    return super.notify(i, j);
                }

                public void created()
                {
                    bSlidingNotify = true;
                }

            }
);
            wSize.toolTip = Plugin.i18n("TIPDestSize");
            wSize.resized();
            gwindowdialogclient.addControl(wValue = new GWindowHSliderInt(gwindowdialogclient, 0, 256, fillValue, 13F, 5F, 10F) {

                public boolean notify(int i, int j)
                {
                    fillValue = pos();
                    return super.notify(i, j);
                }

                public void created()
                {
                    bSlidingNotify = true;
                }

            }
);
            wValue.toolTip = Plugin.i18n("TIPDestValue");
            wValue.resized();
        }

        public void afterCreated()
        {
            super.afterCreated();
            resized();
            close(false);
        }

        public GWindowHSliderInt wLight;
        public GWindowHSliderInt wSize;
        public GWindowHSliderInt wValue;


        public WDialog()
        {
            doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 25F, 9F, true);
            bSizable = false;
        }
    }

    class SelectFilter
        implements ActorFilter
    {

        public void reset(double d)
        {
            _Actor = null;
            _maxLen2 = d;
        }

        public Actor get()
        {
            return _Actor;
        }

        public boolean isUse(Actor actor, double d)
        {
            if(d <= _maxLen2)
            {
                if(actor instanceof BridgeSegment)
                    if(Plugin.builder.conf.bViewBridge)
                        actor = actor.getOwner();
                    else
                        return true;
                if(actor instanceof Bridge)
                {
                    if(!Plugin.builder.conf.bViewBridge)
                        return true;
                } else
                if(!(actor instanceof House))
                    return true;
                if(_Actor == null)
                {
                    _Actor = actor;
                    _Len2 = d;
                } else
                if(d < _Len2)
                {
                    _Actor = actor;
                    _Len2 = d;
                }
            }
            return true;
        }

        private Actor _Actor;
        private double _Len2;
        private double _maxLen2;

        SelectFilter()
        {
            _Actor = null;
        }
    }

    class Tile
    {

        public void setPix(int i, int j, int k)
        {
            int l = (j * 64 + i) * 4;
            k &= 0xff;
            buf[l + 0] = (byte)k;
            buf[l + 1] = (byte)(255 - k);
            buf[l + 3] = -1;
            bufEmpty = false;
            bChanged = true;
            tilesChanged = true;
        }

        public boolean fillFromStatic(boolean flag)
        {
            Statics statics = World.cur().statics;
            HashMapInt hashmapint = statics.allBlocks();
            if(!bufEmpty && !flag)
            {
                for(int i = 0; i < buf.length; i++)
                    buf[i] = 0;

                bufEmpty = true;
            }
            for(int j = 0; j < 64; j++)
            {
                for(int k = 0; k < 64; k++)
                {
                    int l = statics.key(j + y0, k + x0);
                    com.maddox.il2.objects.Statics.Block block = (com.maddox.il2.objects.Statics.Block)hashmapint.get(l);
                    if(block == null)
                        continue;
                    if(flag)
                        return true;
                    float f = block.getDestruction();
                    setPix(k, j, (int)(f * 255F));
                }

            }

            return false;
        }

        public void updateImage()
        {
            if(mat == null)
            {
                mat = (Mat)baseTileMat.Clone();
                mat.Rename(null);
                mat.setLayer(0);
            }
            fillFromStatic(false);
            mat.updateImage(64, 64, 0x380004, buf);
            bChanged = false;
        }

        public boolean bChanged;
        public int x0;
        public int y0;
        public Mat mat;

        public Tile(int i, int j)
        {
            bChanged = true;
            x0 = i;
            y0 = j;
        }
    }


    public PlMisDestruction()
    {
        fillSize = 1;
        fillValue = 127;
        tilesChanged = false;
        buf = new byte[16384];
        bufEmpty = true;
        _startFill = new Point3d();
        _endFill = new Point3d();
        _stepFill = new Point3d();
        _selectFilter = new SelectFilter();
        findedActor = null;
    }

    public boolean isActive()
    {
        if(builder.isFreeView())
            return false;
        if(tiles == null)
            return false;
        else
            return mDialog.bChecked;
    }

    private void tilesDel()
    {
        if(tiles == null)
        {
            return;
        } else
        {
            tiles = (Tile[][])null;
            return;
        }
    }

    private void tilesNew()
    {
        tilesDel();
        int i = Landscape.getSizeXpix();
        int j = Landscape.getSizeYpix();
        int k = ((i + 64) - 1) / 64;
        int l = ((j + 64) - 1) / 64;
        tiles = new Tile[l][k];
        Tile tile = null;
        for(int i1 = 0; i1 < l; i1++)
        {
            for(int j1 = 0; j1 < k; j1++)
            {
                if(tile == null)
                {
                    tile = new Tile(j1 * 64, i1 * 64);
                } else
                {
                    tile.x0 = j1 * 64;
                    tile.y0 = i1 * 64;
                }
                if(tile.fillFromStatic(true))
                {
                    tiles[i1][j1] = tile;
                    tile = null;
                    tilesChanged = true;
                }
            }

        }

    }

    public void preRenderMap2D()
    {
        if(!isActive())
            return;
        if(!tilesChanged)
            return;
        int i = tiles[0].length;
        int j = tiles.length;
        for(int k = 0; k < j; k++)
        {
            for(int l = 0; l < i; l++)
            {
                Tile tile = tiles[k][l];
                if(tile != null && tile.bChanged)
                    tile.updateImage();
            }

        }

        tilesChanged = false;
    }

    public void renderMap2D()
    {
        if(!isActive())
            return;
        CameraOrtho2D cameraortho2d = (CameraOrtho2D)Render.currentCamera();
        int i = tiles[0].length;
        int j = tiles.length;
        float f = 12800F;
        float f1 = (float)((double)f * cameraortho2d.worldScale);
        int k = builder.conf.iLightDestruction << 24 | 0xffffff;
        for(int l = 0; l < j; l++)
        {
            float f2 = (float)(((double)((float)l * f) - cameraortho2d.worldYOffset) * cameraortho2d.worldScale);
            if(f2 > cameraortho2d.top)
                break;
            if(f2 + f1 < cameraortho2d.bottom)
                continue;
            for(int i1 = 0; i1 < i; i1++)
            {
                float f3 = (float)(((double)((float)i1 * f) - cameraortho2d.worldXOffset) * cameraortho2d.worldScale);
                if(f3 > cameraortho2d.right)
                    break;
                if(f3 + f1 < cameraortho2d.left)
                    continue;
                Tile tile = tiles[l][i1];
                if(tile != null && tile.mat != null)
                    Render.drawTile(f3, f2, f1, f1, 0.0F, tile.mat, k, 0.0F, 0.0F, 1.0F, 1.0F);
            }

        }

    }

    public void mapLoaded()
    {
        tilesDel();
        if(mDialog.bChecked)
            wDialog.hideWindow();
    }

    public void load(SectFile sectfile)
    {
        World.cur().statics.restoreAllBridges();
        World.cur().statics.restoreAllHouses();
        World.cur().statics.loadStateBridges(sectfile, false);
        World.cur().statics.loadStateHouses(sectfile, false);
        if(tiles == null)
            return;
        int i = tiles[0].length;
        int j = tiles.length;
        for(int k = 0; k < j; k++)
        {
            for(int l = 0; l < i; l++)
            {
                Tile tile = tiles[k][l];
                if(tile != null)
                    tile.bChanged = true;
            }

        }

        tilesChanged = true;
    }

    public boolean save(SectFile sectfile)
    {
        int i = sectfile.sectionAdd("Bridge");
        World.cur().statics.saveStateBridges(sectfile, i);
        i = sectfile.sectionAdd("House");
        World.cur().statics.saveStateHouses(sectfile, i);
        return true;
    }

    private void fill(Point3d point3d, int i)
    {
        Statics statics = World.cur().statics;
        HashMapInt hashmapint = statics.allBlocks();
        int j = (int)(point3d.x / 200D);
        int k = (int)(point3d.y / 200D);
        i |= 1;
        int l = j - i / 2;
        int i1 = k - i / 2;
        for(int j1 = i1; j1 < i1 + i; j1++)
        {
            for(int k1 = l; k1 < l + i; k1++)
            {
                int l1 = statics.key(j1, k1);
                com.maddox.il2.objects.Statics.Block block = (com.maddox.il2.objects.Statics.Block)hashmapint.get(l1);
                if(block != null)
                {
                    int i2 = k1 / 64;
                    int j2 = j1 / 64;
                    tiles[j2][i2].bChanged = true;
                    tilesChanged = true;
                    block.setDestruction((float)fillValue / 255F);
                    PlMission.setChanged();
                }
            }

        }

    }

    private void _doFill()
    {
        double d = _endFill.distance(_startFill);
        int i = (int)Math.round(d / 200D) + 1;
        float f = 1.0F / (float)i;
        for(int j = 0; j <= i; j++)
        {
            _stepFill.interpolate(_startFill, _endFill, (float)j * f);
            fill(_stepFill, fillSize);
        }

    }

    public void beginFill(Point3d point3d)
    {
        if(!isActive())
        {
            return;
        } else
        {
            _startFill.set(point3d);
            return;
        }
    }

    public void fill(Point3d point3d)
    {
        if(!isActive())
        {
            return;
        } else
        {
            _endFill.set(point3d);
            _doFill();
            _startFill.set(point3d);
            return;
        }
    }

    public void endFill(Point3d point3d)
    {
    }

    public void configure()
    {
        if(getPlugin("Mission") == null)
        {
            throw new RuntimeException("PlMisDestruction: plugin 'Mission' not found");
        } else
        {
            pluginMission = (PlMission)getPlugin("Mission");
            return;
        }
    }

    public Actor selectNear(Point3d point3d, double d)
    {
        _selectFilter.reset(d * d);
        Engine.drawEnv().getFiltered((AbstractCollection)null, point3d.x - d, point3d.y - d, point3d.x + d, point3d.y + d, 15, _selectFilter);
        return _selectFilter.get();
    }

    public void fillPopUpMenu(GWindowMenuPopUp gwindowmenupopup, Point3d point3d)
    {
        if(!isActive())
            return;
        findedActor = selectNear(point3d, 50D);
        if(findedActor == null)
            return;
        gwindowmenupopup.addItem("-", null);
        if(findedActor instanceof Bridge)
        {
            if(Actor.isAlive(findedActor))
                gwindowmenupopup.addItem(new GWindowMenuItem(gwindowmenupopup, i18n("De&stroyBridge"), i18n("TIPDestroyBridge")) {

                    public void execute()
                    {
                        doBridge(true);
                    }

                }
);
            else
                gwindowmenupopup.addItem(new GWindowMenuItem(gwindowmenupopup, i18n("Re&storeBridge"), i18n("TIPRestoreBridge")) {

                    public void execute()
                    {
                        doBridge(false);
                    }

                }
);
        } else
        if(Actor.isAlive(findedActor))
            gwindowmenupopup.addItem(new GWindowMenuItem(gwindowmenupopup, i18n("De&stroyObject"), i18n("TIPDestroyObject")) {

                public void execute()
                {
                    doHouse(true);
                }

            }
);
        else
            gwindowmenupopup.addItem(new GWindowMenuItem(gwindowmenupopup, i18n("Re&storeObject"), i18n("TIPRestoreObject")) {

                public void execute()
                {
                    doHouse(false);
                }

            }
);
    }

    private void doHouse(boolean flag)
    {
        Point3d point3d = findedActor.pos.getAbsPoint();
        int i = (int)(point3d.x / 64D / 200D);
        int j = (int)(point3d.y / 64D / 200D);
        Tile tile = tiles[j][i];
        if(tile == null)
        {
            return;
        } else
        {
            tile.bChanged = true;
            tilesChanged = true;
            findedActor.setDiedFlag(flag);
            PlMission.setChanged();
            return;
        }
    }

    private void doBridge(boolean flag)
    {
        LongBridge longbridge = (LongBridge)findedActor;
        if(flag)
        {
            int i = longbridge.NumStateBits();
            BitSet bitset = new BitSet(i);
            int j = (int)((float)(fillValue * i) / 255F);
            if(j == 0)
                j = 1;
            if(j > i)
                j = i;
            int k = j;
            do
            {
                if(k <= 0)
                    break;
                int l = (int)(Math.random() * (double)j);
                if(!bitset.get(l))
                {
                    bitset.set(l);
                    k--;
                }
            } while(true);
            longbridge.SetStateOfSegments(bitset);
        } else
        {
            longbridge.BeLive();
        }
        PlMission.setChanged();
    }

    public void createGUI()
    {
        baseTileMat = Mat.New("3do/builder/tile.mat");
        mDialog = builder.mView.subMenu.addItem(2, new GWindowMenuItem(builder.mView.subMenu, i18n("De&struction"), i18n("TIPDestruction")) {

            public void execute()
            {
                if(wDialog.isVisible())
                    wDialog.hideWindow();
                else
                if(PlMapLoad.getLandLoaded() != null)
                    wDialog.showWindow();
            }

        }
);
        wDialog = new WDialog();
    }

    public void freeResources()
    {
        findedActor = null;
    }

    public static final int TILE_SIZE = 64;
    GWindowMenuItem mDialog;
    WDialog wDialog;
    private int fillSize;
    private int fillValue;
    Tile tiles[][];
    boolean tilesChanged;
    Mat baseTileMat;
    byte buf[];
    boolean bufEmpty;
    private Point3d _startFill;
    private Point3d _endFill;
    private Point3d _stepFill;
    private PlMission pluginMission;
    private SelectFilter _selectFilter;
    Actor findedActor;

    static 
    {
        Property.set(com.maddox.il2.builder.PlMisDestruction.class, "name", "MisDestruction");
    }






}
