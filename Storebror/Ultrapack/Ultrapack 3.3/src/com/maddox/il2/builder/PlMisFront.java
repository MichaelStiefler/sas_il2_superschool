/* 4.10.1 class */
package com.maddox.il2.builder;

import java.util.ArrayList;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.gwindow.GNotifyListener;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Front;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.game.I18N;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

public class PlMisFront extends Plugin {
    public static final int TILE_SIZE = 64;
    public static final int M         = 16;
    protected ArrayList     allActors = new ArrayList();
    Item[]                  item      = new Item[Army.amountNet() - 1];
    ArrayList               markers;
    int                     tNx;
    int                     tNy;
    double                  camWorldXOffset;
    double                  camWorldYOffset;
    float                   camLeft;
    float                   camBottom;
    int                     _tNx;
    int                     _tNy;
    Mat[]                   mats;
    Mat                     baseTileMat;
    byte[]                  buf;
    byte[]                  _mask;
    private Point2d         p2d;
    private PlMission       pluginMission;
    private int             startComboBox1;
    private GWindowMenuItem viewType;
    private String[]        _actorInfo;

    static class Item {
        public int army;

        public Item(int i) {
            this.army = i;
        }
    }

    public PlMisFront() {
        for (int i = 0; i < this.item.length; i++)
            this.item[i] = new Item(i + 1);
        this.markers = new ArrayList();
        this.buf = new byte[16384];
        this._mask = new byte[4356];
        this.p2d = new Point2d();
        this._actorInfo = new String[1];
    }

    private void tilesUpdate() {
        /* empty */
    }

    public void preRenderMap2D() {
        if (!Plugin.builder.isFreeView() && this.viewType.bChecked) {
            Front.preRender(true);
            if (Front.isMarkersUpdated()) this.tilesUpdate();
        }
    }

    public void renderMap2DAfter() {
        if (!Plugin.builder.isFreeView() && this.viewType.bChecked) {
            Front.render(Plugin.builder.isView3D());
            IconDraw.setScrSize(Plugin.builder.conf.iconSize * 2, Plugin.builder.conf.iconSize * 2);
            Actor actor = Plugin.builder.selectedActor();
            int i = this.allActors.size();
            for (int i_0_ = 0; i_0_ < i; i_0_++) {
                ActorFrontMarker actorfrontmarker = (ActorFrontMarker) this.allActors.get(i_0_);
                if (Plugin.builder.project2d(actorfrontmarker.pos.getAbsPoint(), this.p2d)) {
                    int i_1_ = Army.color(actorfrontmarker.getArmy());
                    if (actorfrontmarker == actor) i_1_ = Builder.colorSelected();
                    IconDraw.setColor(i_1_);
                    IconDraw.render(actorfrontmarker, this.p2d.x + Plugin.builder.conf.iconSize * 2 / 3, this.p2d.y + Plugin.builder.conf.iconSize * 2 / 3);
                }
            }
            IconDraw.setScrSize(Plugin.builder.conf.iconSize, Plugin.builder.conf.iconSize);
        }
    }

    public boolean save(SectFile sectfile) {
        int i = this.allActors.size();
        if (i == 0) return true;
        int i_2_ = sectfile.sectionAdd("FrontMarker");
        for (int i_3_ = 0; i_3_ < i; i_3_++) {
            Actor actor = (Actor) this.allActors.get(i_3_);
            sectfile.lineAdd(i_2_, "FrontMarker" + i_3_ + " " + this.fmt(actor.pos.getAbsPoint().x) + " " + this.fmt(actor.pos.getAbsPoint().y) + " " + actor.getArmy());
        }
        return true;
    }

    private String fmt(double d) {
        boolean bool = d < 0.0;
        if (bool) d = -d;
        double d_4_ = d + 0.0050 - (int) d;
        if (d_4_ >= 0.1) return (bool ? "-" : "") + (int) d + "." + (int) (d_4_ * 100.0);
        return (bool ? "-" : "") + (int) d + ".0" + (int) (d_4_ * 100.0);
    }

    public void load(SectFile sectfile) {
        int i = sectfile.sectionIndex("FrontMarker");
        if (i >= 0) {
            int i_5_ = sectfile.vars(i);
            Point3d point3d = new Point3d();
            for (int i_6_ = 0; i_6_ < i_5_; i_6_++) {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, i_6_));
                numbertokenizer.next((String) null);
                point3d.x = numbertokenizer.next(0.0);
                point3d.y = numbertokenizer.next(0.0);
                int i_7_ = numbertokenizer.next(1, 1, Army.amountNet() - 1);

                // TODO: Disabled by |ZUTI|: why would we want to disable loading all front markers in FMB?
                // if (i_7_ <= Army.amountSingle() - 1)
                this.insert(point3d, false, i_7_);
            }
        }
    }

    public void deleteAll() {
        int i = this.allActors.size();
        for (int i_8_ = 0; i_8_ < i; i_8_++) {
            Actor actor = (Actor) this.allActors.get(i_8_);
            actor.destroy();
        }
        this.allActors.clear();
        Front.setMarkersChanged();
    }

    public void delete(Actor actor) {
        this.allActors.remove(actor);
        actor.destroy();
        Front.setMarkersChanged();
    }

    private ActorFrontMarker insert(Point3d point3d, boolean bool, int army) {
        // TODO: Added by |ZUTI|: if any suitable front marker carriers are found nearby, change position of front
        // marker so that it sits on top of that actor.
        // ----------------------------------------------------
        Point3d result = ZutiSupportMethods_Builder.getClosestFrontMarkerCarrier(point3d, ZutiSupportMethods_Builder.FRONT_MARKER_RADIUS * ZutiSupportMethods_Builder.FRONT_MARKER_RADIUS_SHIP_MULTI, army);
        if (result != null) {
            point3d = result;
            // Add some separation for easier selecting of marker and actor in FMB
            point3d.y = point3d.y + 10;
        }
        // ----------------------------------------------------

        ActorFrontMarker actorfrontmarker;
        try {
            String string = Plugin.i18n("FrontMarker") + " " + I18N.army(Army.name(army));
            ActorFrontMarker actorfrontmarker_9_ = new ActorFrontMarker(string, army, point3d);
            Property.set(actorfrontmarker_9_, "builderSpawn", "");
            Property.set(actorfrontmarker_9_, "builderPlugin", this);
            this.allActors.add(actorfrontmarker_9_);
            if (bool) Plugin.builder.setSelected(actorfrontmarker_9_);
            PlMission.setChanged();
            Front.setMarkersChanged();
            actorfrontmarker = actorfrontmarker_9_;
        } catch (Exception exception) {
            return null;
        }
        return actorfrontmarker;
    }

    public void insert(Loc loc, boolean bool) {
        int i = Plugin.builder.wSelect.comboBox1.getSelected();
        int i_10_ = Plugin.builder.wSelect.comboBox2.getSelected();
        if (i == this.startComboBox1 && i_10_ >= 0 && i_10_ < this.item.length) this.insert(loc.getPoint(), bool, this.item[i_10_].army);
    }

    private void updateView() {
        int i = this.allActors.size();
        for (int i_11_ = 0; i_11_ < i; i_11_++) {
            ActorFrontMarker actorfrontmarker = (ActorFrontMarker) this.allActors.get(i_11_);
            actorfrontmarker.drawing(this.viewType.bChecked);
        }
    }

    public void configure() {
        if (Plugin.getPlugin("Mission") == null) throw new RuntimeException("PlMisFront: plugin 'Mission' not found");
        this.pluginMission = (PlMission) Plugin.getPlugin("Mission");
    }

    private void fillComboBox2(int i) {
        if (i == this.startComboBox1) {
            if (Plugin.builder.wSelect.curFilledType != i) {
                Plugin.builder.wSelect.curFilledType = i;
                Plugin.builder.wSelect.comboBox2.clear(false);
                for (int i_12_ = 0; i_12_ < this.item.length; i_12_++)
                    Plugin.builder.wSelect.comboBox2.add(Plugin.i18n("FrontMarker") + " " + I18N.army(Army.name(this.item[i_12_].army)));
                Plugin.builder.wSelect.comboBox1.setSelected(i, true, false);
            }
            Plugin.builder.wSelect.comboBox2.setSelected(0, true, false);
            Plugin.builder.wSelect.setMesh(null, true);
        }
    }

    public void viewTypeAll(boolean bool) {
        this.viewType.bChecked = bool;
        this.updateView();
    }

    public String[] actorInfo(Actor actor) {
        ActorFrontMarker actorfrontmarker = (ActorFrontMarker) actor;
        this._actorInfo[0] = actorfrontmarker.i18nKey;
        return this._actorInfo;
    }

    public void syncSelector() {
        ActorFrontMarker actorfrontmarker = (ActorFrontMarker) Plugin.builder.selectedActor();
        this.fillComboBox2(this.startComboBox1);
        Plugin.builder.wSelect.comboBox2.setSelected(actorfrontmarker.getArmy() - 1, true, false);
    }

    public void createGUI() {
        this.startComboBox1 = Plugin.builder.wSelect.comboBox1.size();
        Plugin.builder.wSelect.comboBox1.add(Plugin.i18n("FrontMarker"));
        Plugin.builder.wSelect.comboBox1.addNotifyListener(new GNotifyListener() {
            public boolean notify(GWindow gwindow, int i, int i_14_) {
                int i_15_ = Plugin.builder.wSelect.comboBox1.getSelected();
                if (i_15_ >= 0 && i == 2) PlMisFront.this.fillComboBox2(i_15_);
                return false;
            }
        });
        int i;
        for (i = Plugin.builder.mDisplayFilter.subMenu.size() - 1; i >= 0; i--)
            if (this.pluginMission.viewBridge == Plugin.builder.mDisplayFilter.subMenu.getItem(i)) break;
        if (--i >= 0) {
            this.viewType = Plugin.builder.mDisplayFilter.subMenu.addItem(i, new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("showFrontMarker"), null) {
                public void execute() {
                    this.bChecked = !this.bChecked;
                    PlMisFront.this.updateView();
                }
            });
            this.viewType.bChecked = true;
        }
    }

    public void freeResources() {
    }

    static {
        Property.set(PlMisFront.class, "name", "MisFront");
    }
}