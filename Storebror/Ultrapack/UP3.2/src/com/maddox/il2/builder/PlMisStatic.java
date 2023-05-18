/* 4.10.1 class */
package com.maddox.il2.builder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.gwindow.GNotifyListener;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowTabDialogClient;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.game.I18N;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.ships.Ship.RwyTransp;
import com.maddox.il2.objects.ships.Ship.RwyTranspSqr;
import com.maddox.il2.objects.ships.Ship.RwyTranspWide;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.il2.objects.vehicles.planes.Plane.SpawnplaceMarker;
import com.maddox.il2.objects.vehicles.planes.Plane.Spawnplaceholder;
import com.maddox.il2.objects.vehicles.radios.LorenzBlindLandingBeacon;
import com.maddox.il2.objects.vehicles.stationary.SirenGeneric;
import com.maddox.il2.objects.vehicles.stationary.SmokeGeneric;
import com.maddox.rts.LDRres;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.util.NumberTokenizer;

public class PlMisStatic extends Plugin {
    private ArrayList[]        listCountry;
    private HashMap[]          mapCountry;
    protected ArrayList        allActors   = new ArrayList();
    Type[]                     type;
    private Point3d            p3d         = new Point3d();
    private Point2d            p2d         = new Point2d();
    private Point3d            p           = new Point3d();
    private Orient             o           = new Orient();
    private ActorSpawnArg      spawnArg    = new ActorSpawnArg();
    private PlMission          pluginMission;
    private int                startComboBox1;
    ViewItem[]                 viewType;
    HashMap                    viewClasses = new HashMap();
    private String[]           _actorInfo  = new String[2];
    GWindowTabDialogClient.Tab tabActor;
    GWindowLabel               wName;
    GWindowComboControl        wArmy;
    GWindowLabel               wLTimeOutH;
    GWindowEditControl         wTimeOutH;
    GWindowLabel               wLTimeOutM;
    GWindowEditControl         wTimeOutM;
    GWindowLabel               wLCountry;
    GWindowComboControl        wCountry;
    GWindowLabel               wLSleepM;
    GWindowLabel               wLSleepS;
    GWindowEditControl         wSleepM;
    GWindowEditControl         wRHide;
    GWindowLabel               wL1RHide;
    GWindowLabel               wL2RHide;
    GWindowEditControl         wSleepS;
    GWindowLabel               wLSkill;
    GWindowComboControl        wSkill;
    GWindowLabel               wLSlowfire;
    GWindowEditControl         wSlowfire;

    class ViewItem extends GWindowMenuItem {
        int indx;

        public void execute() {
            bChecked = !bChecked;
            viewType(indx);
        }

        public ViewItem(int i, GWindowMenu gwindowmenu, String string, String string_0_) {
            super(gwindowmenu, string, string_0_);
            indx = i;
        }
    }

    static class Type {
        public String name;
        public Item[] item;

        public Type(String string, Item[] items) {
            name = string;
            item = items;
        }
    }

    static class Item {
        public String     name;
        public Class      clazz;
        public int        army;
        public ActorSpawn spawn;
        public String     country;

        public Item(String string, Class var_class, int i) {
            name = string;
            clazz = var_class;
            army = i;
            if (var_class != null) {
                spawn = (ActorSpawn) Spawn.get(var_class.getName());
                String string_1_ = I18N.technic(string);
                if (string_1_.equals(string)) {
                    string_1_ = Property.stringValue(var_class, "i18nName", string_1_);
                    if ("Plane".equals(string_1_)) {
                        Class var_class_2_ = ((Class) Property.value(var_class, "airClass", null));
                        if (var_class_2_ != null) {
                            String string_3_ = Property.stringValue(var_class_2_, "keyName", null);
                            if (string_3_ != null)
                                string_1_ = I18N.plane(string_3_);
                        }
                    }
                }
                Property.set(var_class, "i18nName", string_1_);
            }
        }
    }

    static class Country {
        public String name;
        public String i18nName;
    }

    private void initCountry() {
        if (listCountry == null) {
            listCountry = new ArrayList[3];
            mapCountry = new HashMap[3];
            for (int i = 0; i < 3; i++) {
                listCountry[i] = new ArrayList();
                mapCountry[i] = new HashMap();
            }
            ResourceBundle resourcebundle;
            try {
                resourcebundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
            } catch (Exception exception) {
                resourcebundle = null;
            }
            HashMap hashmap = new HashMap();
            List list = Regiment.getAll();
            for (int i = 0; i < list.size(); i++) {
                Regiment regiment = (Regiment) list.get(i);
                if (!hashmap.containsKey(regiment.country())) {
                    int i_4_ = regiment.getArmy();
                    if (i_4_ >= 0 && i_4_ <= 2) {
                        hashmap.put(regiment.country(), null);
                        Country country = new Country();
                        country.name = regiment.country();

                        // TODO: Added by |ZUTI|: country name check
                        if (country.name != null) {
                            if (resourcebundle != null) {
                                // TODO: Added by |ZUTI|: added try/catch block
                                try {
                                    country.i18nName = resourcebundle.getString(country.name);
                                } catch (Exception ex) {
                                    country.i18nName = country.name;
                                }
                            } else
                                country.i18nName = country.name;
                        }

                        listCountry[i_4_].add(country);
                        mapCountry[i_4_].put(country.name, new Integer(listCountry[i_4_].size() - 1));
                    }
                }
            }
        }
    }

    public void mapLoaded() {
        deleteAll();
    }

    public void deleteAll() {
        for (int i = 0; i < allActors.size(); i++) {
            Actor actor = (Actor) allActors.get(i);
            if (Actor.isValid(actor))
                actor.destroy();
        }
        allActors.clear();
    }

    public void delete(Actor actor) {
        allActors.remove(actor);
        actor.destroy();
    }

    public void renderMap2D() {
        if (!builder.isFreeView()) {
            Actor actor = builder.selectedActor();
            Render.prepareStates();
            for (int i = 0; i < allActors.size(); i++) {
                Actor actor_5_ = (Actor) allActors.get(i);
                if (Actor.isValid(actor_5_) && actor_5_.icon != null && viewClasses.containsKey(actor_5_.getClass())) {
                    p3d.set(actor_5_.pos.getAbsPoint());
                    if (actor_5_ instanceof SmokeGeneric)
                        p3d.z = Engine.land().HQ(p3d.x, p3d.y);
                    if (builder.project2d(p3d, p2d)) {
                        int i_6_ = actor_5_.getArmy();
                        boolean bool = builder.conf.bShowArmy[i_6_];
                        if (bool) {
                            int i_7_;
                            if (builder.isMiltiSelected(actor_5_))
                                i_7_ = (Builder.colorMultiSelected(Army.color(actor_5_.getArmy())));
                            else if (actor_5_ == actor)
                                i_7_ = Builder.colorSelected();
                            else
                                i_7_ = Army.color(actor_5_.getArmy());
                            IconDraw.setColor(i_7_);
                            IconDraw.render(actor_5_, p2d.x, p2d.y);
                            if (Plugin.builder.conf.bShowName) {
                                String string = Property.stringValue(actor_5_.getClass(), "i18nName", "");
                                Plugin.builder.smallFont.output(i_7_, (float) ((int) p2d.x + IconDraw.scrSizeX() / 2 + 2), (float) ((int) p2d.y + Plugin.builder.smallFont.height() - Plugin.builder.smallFont.descender() - IconDraw.scrSizeY() / 2 - 2),
                                        0.0F, string);
                            }
                        }
                    }
                }
            }
        }
    }

    public void load(SectFile sectfile) {
        initCountry();
        int i = sectfile.sectionIndex("Stationary");
        if (i >= 0) {
            int i_8_ = sectfile.vars(i);
            for (int i_9_ = 0; i_9_ < i_8_; i_9_++) {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, i_9_));
                insert(null, numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0), numbertokenizer.next(0.0), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F), numbertokenizer.next((String) null),
                        numbertokenizer.next((String) null), numbertokenizer.next((String) null));
            }
        }
        i = sectfile.sectionIndex("NStationary");
        if (i >= 0) {
            int varsCount = sectfile.vars(i);
            for (int i_11_ = 0; i_11_ < varsCount; i_11_++) {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, i_11_));
                insert(numbertokenizer.next(""), numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0), numbertokenizer.next(0.0), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F), numbertokenizer.next((String) null),
                        numbertokenizer.next((String) null), numbertokenizer.next((String) null));
            }

            // TODO: Added by |ZUTI|: Load spawn place place holders as designated stationary aircraft
            // -------------------------------------------------------------
            List zutiSpawnPlacePlaceHolders = ZutiSupportMethods_Builder.getPreparedSpawnPlacePlaceHolders(sectfile, varsCount);
            for (int x = 0; x < zutiSpawnPlacePlaceHolders.size(); x++) {
                NumberTokenizer numbertokenizer = new NumberTokenizer((String) zutiSpawnPlacePlaceHolders.get(x));
                insert(numbertokenizer.next(""), numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0), numbertokenizer.next(0.0), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F), numbertokenizer.next((String) null),
                        numbertokenizer.next((String) null), numbertokenizer.next((String) null));
            }
            // -------------------------------------------------------------
        }
        Engine.drawEnv().staticTrimToSize();
    }

    public boolean save(SectFile sectfile) {
        Orient orient = new Orient();
        int i = sectfile.sectionAdd("NStationary");
        for (int i_12_ = 0; i_12_ < allActors.size(); i_12_++) {
            Actor actor = (Actor) allActors.get(i_12_);
            if (Actor.isValid(actor) && Property.containsValue(actor, "builderSpawn")) {
                Point3d point3d = actor.pos.getAbsPoint();
                Orient orient_13_ = actor.pos.getAbsOrient();
                orient.set(orient_13_);
                orient.wrap360();
                float f = Property.floatValue(actor, "timeout", 0.0F);
                if (actor instanceof PlaneGeneric) {
                    // TODO: Added by |ZUTI|: save all but those that are serving as spawn place holders
                    // -----------------------------------------------------
                    if (actor instanceof Spawnplaceholder || actor instanceof SpawnplaceMarker) {
                        // System.out.println("Skipping one for saving as NStationary: " + actor.toString());
                        continue;
                    }
                    // -----------------------------------------------------

                    String string = ((PlaneGeneric) actor).country;
                    sectfile.lineAdd(i, actor.name(), (ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(point3d.x, point3d.y, orient.azimut()) + " " + f + " " + string));
                } else if (actor instanceof ShipGeneric || actor instanceof BigshipGeneric) {
                    int i_14_ = Property.intValue(actor, "sleep", 0);
                    int i_15_ = Property.intValue(actor, "skill", 2);
                    float f_16_ = Property.floatValue(actor, "slowfire", 1.0F);
                    sectfile.lineAdd(i, actor.name(), (ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(point3d.x, point3d.y, orient.azimut()) + " " + f + " " + i_14_ + " " + i_15_ + " " + f_16_));
                } else if (actor instanceof ArtilleryGeneric) {
                    int i_17_ = Property.intValue(actor, "radius_hide", 0);
                    sectfile.lineAdd(i, actor.name(), (ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(point3d.x, point3d.y, orient.azimut()) + " " + f + " " + i_17_));
                } else if (actor instanceof SmokeGeneric)
                    sectfile.lineAdd(i, actor.name(), (ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(point3d.x, point3d.y, orient.azimut()) + " " + formatValue(point3d.z)));
                else
                    sectfile.lineAdd(i, actor.name(), (ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(point3d.x, point3d.y, orient.azimut()) + " " + f));
            }
        }
        return true;
    }

    private String formatPos(double d, double d_18_, float f) {
        return (formatValue(d) + " " + formatValue(d_18_) + " " + formatValue((double) f));
    }

    private String formatValue(double d) {
        boolean bool = d < 0.0;
        if (bool)
            d = -d;
        double d_19_ = d + 0.0050 - (double) (int) d;
        if (d_19_ >= 0.1)
            return (bool ? "-" : "") + (int) d + "." + (int) (d_19_ * 100.0);
        return (bool ? "-" : "") + (int) d + ".0" + (int) (d_19_ * 100.0);
    }

    private void makeName(Actor actor, String string) {
        if (string != null && Actor.getByName(string) == null)
            actor.setName(string);
        else {
            int i = 0;
            for (;;) {
                string = i + "_Static";
                if (Actor.getByName(string) == null)
                    break;
                i++;
            }
            actor.setName(string);
        }
    }

    // TODO: Altered by |ZUTI| from private to public
    public Actor insert(String string, String string_20_, int i, double d, double d_21_, float f, float f_22_, String string_23_, String string_24_, String string_25_) {
        Class var_class;
        try {
            var_class = ObjIO.classForName(string_20_);
        } catch (Exception exception) {
            builder.tipErr("MissionLoad: class '" + string_20_ + "' not found");
            return null;
        }
        ActorSpawn actorspawn = (ActorSpawn) Spawn.get(var_class.getName(), false);
        if (actorspawn == null) {
            builder.tipErr("MissionLoad: ActorSpawn for '" + string_20_ + "' not found");
            return null;
        }
        spawnArg.clear();
        if (string != null) {
            if ("NONAME".equals(string))
                string = null;
            if (Actor.getByName(string) != null)
                string = null;
        }
        if (i < 0 && i >= Builder.armyAmount()) {
            builder.tipErr("MissionLoad: Wrong actor army '" + i + "'");
            return null;
        }
        spawnArg.army = i;
        spawnArg.armyExist = true;
        spawnArg.country = string_23_;
        Chief.new_DELAY_WAKEUP = 0.0F;
        ArtilleryGeneric.new_RADIUS_HIDE = 0.0F;
        if (string_23_ != null) {
            try {
                Chief.new_DELAY_WAKEUP = (float) Integer.parseInt(string_23_);
                ArtilleryGeneric.new_RADIUS_HIDE = Chief.new_DELAY_WAKEUP;
            } catch (Exception exception) {
                /* empty */
            }
        }
        Chief.new_SKILL_IDX = 2;
        if (string_24_ != null) {
            try {
                Chief.new_SKILL_IDX = Integer.parseInt(string_24_);
            } catch (Exception exception) {
                /* empty */
            }
            if (Chief.new_SKILL_IDX < 0 || Chief.new_SKILL_IDX > 3) {
                builder.tipErr("MissionLoad: Wrong actor skill '" + Chief.new_SKILL_IDX + "'");
                return null;
            }
        }
        Chief.new_SLOWFIRE_K = 1.0F;
        if (string_25_ != null) {
            try {
                Chief.new_SLOWFIRE_K = Float.parseFloat(string_25_);
            } catch (Exception exception) {
                /* empty */
            }
            if (Chief.new_SLOWFIRE_K < 0.5F || Chief.new_SLOWFIRE_K > 100.0F) {
                builder.tipErr("MissionLoad: Wrong actor slowfire '" + Chief.new_SLOWFIRE_K + "'");
                return null;
            }
        }
        p.set(d, d_21_, 0.0);
        spawnArg.point = p;
        o.set(f, 0.0F, 0.0F);
        spawnArg.orient = o;
        Actor actor;
        try {
            Actor actor_26_ = actorspawn.actorSpawn(spawnArg);
            if (actor_26_ instanceof SirenGeneric)
                actor_26_.setArmy(i);
            if (actor_26_ instanceof SmokeGeneric) {
                p.z = (double) f_22_;
                actor_26_.pos.setAbs(p);
                actor_26_.pos.reset();
            }
            builder.align(actor_26_);
            Property.set(actor_26_, "builderSpawn", "");
            Property.set(actor_26_, "builderPlugin", this);
            if (!actor_26_.isRealTimeFlag())
                actor_26_.interpCancelAll();
            makeName(actor_26_, string);
            allActors.add(actor_26_);

            if ((actor_26_ instanceof RwyTransp) || (actor_26_ instanceof RwyTranspWide) || (actor_26_ instanceof RwyTranspSqr))
                ((BigshipGeneric) actor_26_).showTransparentRunwayRed();
            if (actor_26_ instanceof LorenzBlindLandingBeacon)
                ((LorenzBlindLandingBeacon) actor_26_).showGuideArrows();

            if (actor_26_ instanceof ArtilleryGeneric) {
                Property.set(actor_26_, "timeout", f_22_);
                Property.set(actor_26_, "radius_hide", (int) ArtilleryGeneric.new_RADIUS_HIDE);
            }
            if (actor_26_ instanceof ShipGeneric || actor_26_ instanceof BigshipGeneric) {
                Property.set(actor_26_, "sleep", (int) Chief.new_DELAY_WAKEUP);
                Property.set(actor_26_, "skill", Chief.new_SKILL_IDX);
                Property.set(actor_26_, "slowfire", Chief.new_SLOWFIRE_K);
            }
            actor = actor_26_;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        return actor;
    }

    private Actor insert(ActorSpawn actorspawn, Loc loc, int i, boolean bool, String string) {
        spawnArg.clear();
        spawnArg.point = loc.getPoint();
        spawnArg.orient = loc.getOrient();
        spawnArg.army = i;
        spawnArg.armyExist = true;
        spawnArg.country = string;
        Actor actor;
        try {
            Actor actor_27_ = actorspawn.actorSpawn(spawnArg);

            // TODO: Added by |ZUTI|: Check if it is spawn place holder and if it is placed inside stand alone bp
            // ----------------------------------------------------------------
            if (actor_27_ instanceof Spawnplaceholder && !ZutiSupportMethods_Builder.isOnStandAloneBornPlace(loc.getX(), loc.getY())) {
                new GWindowMessageBox(builder.clientWindow.root, 20.0F, true, i18n("mds.spawnPlaceHolderInfo"), i18n("mds.spawnPlaceHolderNotPossible"), 3, 0.0F);
                actor_27_.destroy();
                actor_27_ = null;
                return null;
            }
            // ----------------------------------------------------------------

            if ((actor_27_ instanceof RwyTransp) || (actor_27_ instanceof RwyTranspWide) || (actor_27_ instanceof RwyTranspSqr))
                ((BigshipGeneric) actor_27_).showTransparentRunwayRed();
            if (actor_27_ instanceof LorenzBlindLandingBeacon)
                ((LorenzBlindLandingBeacon) actor_27_).showGuideArrows();

            if (actor_27_ instanceof SirenGeneric)
                actor_27_.setArmy(i);
            builder.align(actor_27_);
            Property.set(actor_27_, "builderSpawn", "");
            Property.set(actor_27_, "builderPlugin", this);
            if (!actor_27_.isRealTimeFlag())
                actor_27_.interpCancelAll();
            if (actor_27_ instanceof ArtilleryGeneric) {
                Property.set(actor_27_, "timeout", 0.0F);
                Property.set(actor_27_, "radius_hide", 0);
            }
            if (actor_27_ instanceof ShipGeneric || actor_27_ instanceof BigshipGeneric) {
                Property.set(actor_27_, "sleep", 0);
                Property.set(actor_27_, "skill", 2);
                Property.set(actor_27_, "slowfire", 1.0F);
            }
            makeName(actor_27_, null);
            allActors.add(actor_27_);
            if (bool)
                builder.setSelected(actor_27_);
            PlMission.setChanged();
            actor = actor_27_;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        return actor;
    }

    public void insert(Loc loc, boolean bool) {
        int i = builder.wSelect.comboBox1.getSelected();
        int i_28_ = builder.wSelect.comboBox2.getSelected();
        if (i >= startComboBox1 && i < startComboBox1 + type.length) {
            i -= startComboBox1;
            if (i_28_ >= 0 && i_28_ < type[i].item.length) {
                ActorSpawn actorspawn = type[i].item[i_28_].spawn;
                insert(actorspawn, loc, type[i].item[i_28_].army, bool, type[i].item[i_28_].country);
            }
        }
    }

    public void changeType() {
        int i = builder.wSelect.comboBox1.getSelected() - startComboBox1;
        int i_29_ = builder.wSelect.comboBox2.getSelected();
        Actor actor = builder.selectedActor();
        Loc loc = actor.pos.getAbs();
        Actor actor_30_ = insert(type[i].item[i_29_].spawn, loc, type[i].item[i_29_].army, true, type[i].item[i_29_].country);
        if (builder.selectedActor() != actor) {
            allActors.remove(actor);
            String string = actor.name();
            actor.destroy();
            actor_30_.setName(string);
        }
    }

    public void configure() {
        if (getPlugin("Mission") == null)
            throw new RuntimeException("PlMisStatic: plugin 'Mission' not found");
        pluginMission = (PlMission) getPlugin("Mission");
        if (sectFile == null)
            throw new RuntimeException("PlMisStatic: field 'sectFile' not defined");
        SectFile sectfile = new SectFile(sectFile, 0);
        int i = sectfile.sections();
        if (i <= 0)
            throw new RuntimeException("PlMisStatic: file '" + sectFile + "' is empty");
        type = new Type[i];
        for (int i_31_ = 0; i_31_ < i; i_31_++) {
            String string = sectfile.sectionName(i_31_);
            int i_32_ = sectfile.vars(i_31_);
            Item[] items = new Item[i_32_];
            for (int i_33_ = 0; i_33_ < i_32_; i_33_++) {
                String string_34_ = sectfile.var(i_31_, i_33_);
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(i_31_, i_33_));
                String string_35_ = numbertokenizer.next((String) null);
                int i_36_ = numbertokenizer.next(0, 0, Builder.armyAmount() - 1);
                int i_37_ = string_35_.indexOf(' ');
                if (i_37_ > 0)
                    string_35_ = string_35_.substring(0, i_37_);
                Class var_class;
                try {
                    var_class = ObjIO.classForName(string_35_);
                } catch (Exception exception) {
                    throw new RuntimeException("PlMisStatic: class '" + string_35_ + "' not found");
                }
                int i_38_ = string_35_.lastIndexOf('$');
                if (i_38_ >= 0) {
                    String string_39_ = string_35_.substring(0, i_38_);
                    try {
                        ObjIO.classForName(string_39_);
                    } catch (Exception exception) {
                        throw new RuntimeException("PlMisStatic: Outer class '" + string_39_ + "' not found");
                    }
                }
                items[i_33_] = new Item(string_34_, var_class, i_36_);
            }
            type[i_31_] = new Type(string, items);
        }
    }

    void viewUpdate() {
        for (int i = 0; i < allActors.size(); i++) {
            Actor actor = (Actor) allActors.get(i);
            if (Actor.isValid(actor) && Property.containsValue(actor, "builderSpawn"))
                actor.drawing(viewClasses.containsKey(actor.getClass()));
        }
        if (Actor.isValid(builder.selectedActor()) && !builder.selectedActor().isDrawing())
            builder.setSelected(null);
        if (!builder.isFreeView())
            builder.repaint();
    }

    void viewType(int i, boolean bool) {
        int i_40_ = type[i].item.length;
        for (int i_41_ = 0; i_41_ < i_40_; i_41_++) {
            if (bool)
                viewClasses.put(type[i].item[i_41_].clazz, type[i].item[i_41_]);
            else
                viewClasses.remove(type[i].item[i_41_].clazz);
        }
        viewUpdate();
    }

    void viewType(int i) {
        viewType(i, viewType[i].bChecked);
    }

    public void viewTypeAll(boolean bool) {
        for (int i = 0; i < type.length; i++) {
            if (viewType[i].bChecked != bool) {
                viewType[i].bChecked = bool;
                viewType(i, bool);
            }
        }
    }

    private void fillComboBox1() {
        startComboBox1 = builder.wSelect.comboBox1.size();
        for (int i = 0; i < type.length; i++)
            builder.wSelect.comboBox1.add(I18N.technic(type[i].name));
        if (startComboBox1 == 0)
            builder.wSelect.comboBox1.setSelected(0, true, false);
    }

    private void fillComboBox2(int i, int i_42_) {
        if (i >= startComboBox1 && i < startComboBox1 + type.length) {
            if (builder.wSelect.curFilledType != i) {
                builder.wSelect.curFilledType = i;
                builder.wSelect.comboBox2.clear(false);
                for (int i_43_ = 0; i_43_ < type[i - startComboBox1].item.length; i_43_++)
                    builder.wSelect.comboBox2.add(Property.stringValue((type[i - startComboBox1].item[i_43_].clazz), "i18nName", ""));
                builder.wSelect.comboBox1.setSelected(i, true, false);
            }
            builder.wSelect.comboBox2.setSelected(i_42_, true, false);
            fillComboBox2Render(i, i_42_);
        }
    }

    private void fillComboBox2Render(int i, int i_44_) {
        try {
            Class var_class = type[i - startComboBox1].item[i_44_].clazz;
            if (PlaneGeneric.class.isAssignableFrom(var_class)) {
                Class var_class_45_ = (Class) Property.value(var_class, "airClass", null);
                int i_46_ = type[i - startComboBox1].item[i_44_].army;
                String string = null;
                if (Actor.isValid(builder.selectedActor())) {
                    i_46_ = builder.selectedActor().getArmy();
                    string = ((PlaneGeneric) builder.selectedActor()).country;
                    type[i - startComboBox1].item[i_44_].country = string;
                    type[i - startComboBox1].item[i_44_].army = i_46_;
                }
                Regiment regiment = Regiment.findFirst(string, i_46_);
                String string_47_ = Aircraft.getPropertyMesh(var_class_45_, regiment.country());
                builder.wSelect.setMesh(string_47_, false);
                if (builder.wSelect.getHierMesh() != null) {
                    PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(var_class_45_, regiment.country());
                    paintscheme.prepareNum(var_class_45_, builder.wSelect.getHierMesh(), regiment, (int) (Math.random() * 3.0), (int) (Math.random() * 3.0), (int) (Math.random() * 98.0 + 1.0));
                }
            } else {
                String string = Property.stringValue(var_class, "meshName", null);
                if (string == null) {
                    Method method = var_class.getMethod("getMeshNameForEditor", null);
                    string = (String) method.invoke(var_class, null);
                }
                builder.wSelect.setMesh(string, true);
            }
        } catch (Exception exception) {
            builder.wSelect.setMesh(null, true);
        }
    }

    public String[] actorInfo(Actor actor) {
        Class var_class = actor.getClass();
        for (int i = 0; i < type.length; i++) {
            for (int i_48_ = 0; i_48_ < type[i].item.length; i_48_++) {
                if (var_class == type[i].item[i_48_].clazz) {
                    _actorInfo[0] = (I18N.technic(type[i].name) + "." + Property.stringValue(type[i].item[i_48_].clazz, "i18nName", ""));
                    float f = Property.floatValue(actor, "timeout", 0.0F);
                    if (f > 0.0F)
                        _actorInfo[1] = (timeSecToString((double) (f * 60.0F + (float) (int) (World.getTimeofDay() * 60.0F * 60.0F))));
                    else
                        _actorInfo[1] = null;
                    return _actorInfo;
                }
            }
        }
        return null;
    }

    public void syncSelector() {
        Actor actor = builder.selectedActor();
        Class var_class = actor.getClass();
        for (int i = 0; i < type.length; i++) {
            for (int i_49_ = 0; i_49_ < type[i].item.length; i_49_++) {
                if (var_class == type[i].item[i_49_].clazz) {
                    fillComboBox2(i + startComboBox1, i_49_);
                    builder.wSelect.tabsClient.addTab(1, tabActor);
                    wName.cap.set(Property.stringValue((type[i].item[i_49_].clazz), "i18nName", ""));
                    wArmy.setSelected(actor.getArmy(), true, false);
                    if (actor instanceof ArtilleryGeneric) {
                        float f = Property.floatValue(actor, "timeout", 0.0F);
                        wTimeOutH.setValue("" + (int) (f / 60.0F % 24.0F), false);
                        wTimeOutM.setValue("" + (int) (f % 60.0F), false);
                        wLTimeOutH.showWindow();
                        wTimeOutH.showWindow();
                        wLTimeOutM.showWindow();
                        wTimeOutM.showWindow();
                        int i_50_ = Property.intValue(actor, "radius_hide", 0);
                        wL1RHide.showWindow();
                        wL2RHide.showWindow();
                        wRHide.showWindow();
                        wRHide.setValue("" + i_50_, false);
                    } else {
                        wLTimeOutH.hideWindow();
                        wTimeOutH.hideWindow();
                        wLTimeOutM.hideWindow();
                        wTimeOutM.hideWindow();
                        wL1RHide.hideWindow();
                        wL2RHide.hideWindow();
                        wRHide.hideWindow();
                    }
                    if (PlaneGeneric.class.isAssignableFrom(var_class)) {
                        PlaneGeneric planegeneric = (PlaneGeneric) actor;
                        fillCountry(actor.getArmy(), planegeneric.country);
                        wLCountry.showWindow();
                        wCountry.showWindow();
                    } else {
                        wLCountry.hideWindow();
                        wCountry.hideWindow();
                    }
                    if (actor instanceof ShipGeneric || actor instanceof BigshipGeneric) {
                        wLSleepM.showWindow();
                        wSleepM.showWindow();
                        wLSleepS.showWindow();
                        wSleepS.showWindow();
                        int i_51_ = Property.intValue(actor, "sleep", 0);
                        wSleepM.setValue("" + i_51_ / 60 % 99, false);
                        wSleepS.setValue("" + i_51_ % 60, false);
                        wLSkill.showWindow();
                        wSkill.showWindow();
                        int i_52_ = Property.intValue(actor, "skill", 2);
                        wSkill.setSelected(i_52_, true, false);
                        wLSlowfire.showWindow();
                        wSlowfire.showWindow();
                        float f = Property.floatValue(actor, "slowfire", 1.0F);
                        wSlowfire.setValue("" + f);
                    } else {
                        wLSleepM.hideWindow();
                        wSleepM.hideWindow();
                        wLSleepS.hideWindow();
                        wSleepS.hideWindow();
                        wLSkill.hideWindow();
                        wSkill.hideWindow();
                        wLSlowfire.hideWindow();
                        wSlowfire.hideWindow();
                    }
                    return;
                }
            }
        }
    }

    private String fillCountry(int i, String string) {
        initCountry();
        wCountry.clear(false);
        ArrayList arraylist = listCountry[i];
        for (int i_53_ = 0; i_53_ < arraylist.size(); i_53_++) {
            Country country = (Country) arraylist.get(i_53_);
            wCountry.add(country.i18nName);
        }
        if (string != null && !mapCountry[i].containsKey(string))
            string = null;
        if (string == null) {
            switch (i) {
                case 0:
                    string = "nn";
                    break;
                case 1:
                    string = "ru";
                    break;
                case 2:
                    string = "de";
                    break;
            }
        }
        Integer integer = (Integer) mapCountry[i].get(string);
        wCountry.setSelected(integer.intValue(), true, false);
        return string;
    }

    private void controlResized(GWindowDialogClient gwindowdialogclient, GWindow gwindow) {
        if (gwindow != null)
            gwindow.setSize((gwindowdialogclient.win.dx - gwindow.win.x - gwindowdialogclient.lAF().metric(1.0F)), gwindow.win.dy);
    }

    private void editResized(GWindowDialogClient gwindowdialogclient) {
        controlResized(gwindowdialogclient, wName);
        controlResized(gwindowdialogclient, wArmy);
        controlResized(gwindowdialogclient, wCountry);
    }

    public void createGUI() {
        fillComboBox1();
        fillComboBox2(0, 0);
        builder.wSelect.comboBox1.addNotifyListener(new GNotifyListener() {
            public boolean notify(GWindow gwindow, int i, int i_55_) {
                int i_56_ = Plugin.builder.wSelect.comboBox1.getSelected();
                if (i_56_ >= 0 && i == 2)
                    PlMisStatic.this.fillComboBox2(i_56_, 0);
                return false;
            }
        });
        builder.wSelect.comboBox2.addNotifyListener(new GNotifyListener() {
            public boolean notify(GWindow gwindow, int i, int i_58_) {
                if (i != 2)
                    return false;
                int i_59_ = Plugin.builder.wSelect.comboBox1.getSelected();
                if (i_59_ < startComboBox1 || i_59_ >= startComboBox1 + type.length)
                    return false;
                int i_60_ = Plugin.builder.wSelect.comboBox2.getSelected();
                PlMisStatic.this.fillComboBox2Render(i_59_, i_60_);
                return false;
            }
        });
        int i;
        for (i = builder.mDisplayFilter.subMenu.size() - 1; i >= 0 && (pluginMission.viewBridge != builder.mDisplayFilter.subMenu.getItem(i)); i--) {
            /* empty */
        }
        if (--i >= 0) {
            int i_61_ = i;
            i = type.length - 1;
            viewType = new ViewItem[type.length];
            for (/**/; i >= 0; i--) {
                ViewItem viewitem;
                if ("de".equals(RTSConf.cur.locale.getLanguage()))
                    viewitem = ((ViewItem) (builder.mDisplayFilter.subMenu.addItem(i_61_, new ViewItem(i, builder.mDisplayFilter.subMenu, (I18N.technic(type[i].name) + " " + i18n("show")), null))));
                else
                    viewitem = ((ViewItem) (builder.mDisplayFilter.subMenu.addItem(i_61_, new ViewItem(i, builder.mDisplayFilter.subMenu, (i18n("show") + " " + I18N.technic(type[i].name)), null))));
                viewitem.bChecked = true;
                viewType[i] = viewitem;
                viewType(i, true);
            }
        }
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) builder.wSelect.tabsClient.create(new GWindowDialogClient() {
            public void resized() {
                super.resized();
                PlMisStatic.this.editResized(this);
            }
        });
        tabActor = builder.wSelect.tabsClient.createTab(i18n("StaticActor"), gwindowdialogclient);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7.0F, 1.3F, i18n("Name"), null));
        gwindowdialogclient.addLabel(wName = new GWindowLabel(gwindowdialogclient, 9.0F, 1.0F, 7.0F, 1.3F, "", null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3.0F, 7.0F, 1.3F, i18n("Army"), null));
        gwindowdialogclient.addControl(wArmy = new GWindowComboControl(gwindowdialogclient, 9.0F, 3.0F, 7.0F) {
            public void afterCreated() {
                super.afterCreated();
                setEditable(false);
                for (int i_66_ = 0; i_66_ < Builder.armyAmount(); i_66_++)
                    add(I18N.army(Army.name(i_66_)));
            }

            public boolean notify(int i_67_, int i_68_) {
                if (i_67_ != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                int i_69_ = getSelected();
                actor.setArmy(i_69_);
                PlMission.setChanged();
                PlMission.checkShowCurrentArmy();
                Class var_class = actor.getClass();
                if (PlaneGeneric.class.isAssignableFrom(var_class)) {
                    PlaneGeneric planegeneric = (PlaneGeneric) actor;
                    String string = PlMisStatic.this.fillCountry(i_69_, planegeneric.country);
                    planegeneric.country = string;
                    planegeneric.activateMesh(true);
                    for (int i_70_ = 0; i_70_ < type.length; i_70_++) {
                        for (int i_71_ = 0; i_71_ < type[i_70_].item.length; i_71_++) {
                            if (var_class == type[i_70_].item[i_71_].clazz) {
                                PlMisStatic.this.fillComboBox2Render(i_70_ + startComboBox1, i_71_);
                                break;
                            }
                        }
                    }
                }
                return false;
            }
        });
        gwindowdialogclient.addLabel(wLCountry = new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, 7.0F, 1.3F, I18N.gui("neta.Country"), null));
        gwindowdialogclient.addControl(wCountry = new GWindowComboControl(gwindowdialogclient, 9.0F, 5.0F, 7.0F) {
            public void afterCreated() {
                super.afterCreated();
                setEditable(false);
            }

            public boolean notify(int i_75_, int i_76_) {
                if (i_75_ != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                Class var_class = actor.getClass();
                if (!PlaneGeneric.class.isAssignableFrom(var_class))
                    return false;
                int i_77_ = getSelected();
                Country country = (Country) listCountry[actor.getArmy()].get(i_77_);
                PlaneGeneric planegeneric = (PlaneGeneric) actor;
                planegeneric.country = country.name;
                planegeneric.activateMesh(true);
                for (int i_78_ = 0; i_78_ < type.length; i_78_++) {
                    for (int i_79_ = 0; i_79_ < type[i_78_].item.length; i_79_++) {
                        if (var_class == type[i_78_].item[i_79_].clazz) {
                            PlMisStatic.this.fillComboBox2Render(i_78_ + startComboBox1, i_79_);
                            break;
                        }
                    }
                }
                return false;
            }
        });
        GWindowDialogClient gwindowdialogclient_80_ = gwindowdialogclient;
        gwindowdialogclient_80_.addLabel(wLTimeOutH = new GWindowLabel(gwindowdialogclient_80_, 1.0F, 5.0F, 7.0F, 1.3F, i18n("TimeOut"), null));
        gwindowdialogclient_80_.addControl(wTimeOutH = new GWindowEditControl(gwindowdialogclient_80_, 9.0F, 5.0F, 2.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i_85_, int i_86_) {
                if (i_85_ != 2)
                    return false;
                PlMisStatic.this.getTimeOut();
                return false;
            }
        });
        gwindowdialogclient_80_.addLabel(wLTimeOutM = new GWindowLabel(gwindowdialogclient_80_, 11.2F, 5.0F, 1.0F, 1.3F, ":", null));
        gwindowdialogclient_80_.addControl(wTimeOutM = new GWindowEditControl(gwindowdialogclient_80_, 11.5F, 5.0F, 2.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i_91_, int i_92_) {
                if (i_91_ != 2)
                    return false;
                PlMisStatic.this.getTimeOut();
                return false;
            }
        });
        gwindowdialogclient_80_.addLabel(wL1RHide = new GWindowLabel(gwindowdialogclient_80_, 1.0F, 7.0F, 7.0F, 1.3F, i18n("RHide"), null));
        gwindowdialogclient_80_.addLabel(wL2RHide = new GWindowLabel(gwindowdialogclient_80_, 14.0F, 7.0F, 4.0F, 1.3F, i18n("[M]"), null));
        gwindowdialogclient_80_.addControl(wRHide = new GWindowEditControl(gwindowdialogclient_80_, 9.0F, 7.0F, 4.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i_97_, int i_98_) {
                if (i_97_ != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                int i_99_ = Property.intValue(actor, "radius_hide", 0);
                String string = getValue();
                try {
                    i_99_ = (int) Double.parseDouble(string);
                    if (i_99_ < 0) {
                        i_99_ = 0;
                        setValue("" + i_99_, false);
                    }
                } catch (Exception exception) {
                    setValue("" + i_99_, false);
                    return false;
                }
                Property.set(actor, "radius_hide", i_99_);
                PlMission.setChanged();
                return false;
            }
        });
        gwindowdialogclient.addLabel(wLSleepM = new GWindowLabel(gwindowdialogclient, 1.0F, 5.0F, 7.0F, 1.3F, i18n("Sleep"), null));
        gwindowdialogclient.addControl(wSleepM = new GWindowEditControl(gwindowdialogclient, 9.0F, 5.0F, 2.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i_104_, int i_105_) {
                if (i_104_ != 2)
                    return false;
                PlMisStatic.this.getSleep();
                return false;
            }
        });
        gwindowdialogclient.addLabel(wLSleepS = new GWindowLabel(gwindowdialogclient, 11.2F, 5.0F, 1.0F, 1.3F, ":", null));
        gwindowdialogclient.addControl(wSleepS = new GWindowEditControl(gwindowdialogclient, 11.5F, 5.0F, 2.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i_110_, int i_111_) {
                if (i_110_ != 2)
                    return false;
                PlMisStatic.this.getSleep();
                return false;
            }
        });
        gwindowdialogclient.addLabel(wLSkill = new GWindowLabel(gwindowdialogclient, 1.0F, 7.0F, 7.0F, 1.3F, i18n("Skill"), null));
        gwindowdialogclient.addControl(wSkill = new GWindowComboControl(gwindowdialogclient, 9.0F, 7.0F, 7.0F) {
            public void afterCreated() {
                super.afterCreated();
                setEditable(false);
                add(Plugin.i18n("Rookie"));
                add(Plugin.i18n("Average"));
                add(Plugin.i18n("Veteran"));
                add(Plugin.i18n("Ace"));
            }

            public boolean notify(int i_115_, int i_116_) {
                if (i_115_ != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                Property.set(actor, "skill", getSelected());
                PlMission.setChanged();
                return false;
            }
        });
        gwindowdialogclient.addLabel(wLSlowfire = new GWindowLabel(gwindowdialogclient, 1.0F, 9.0F, 7.0F, 1.3F, i18n("Slowfire"), null));
        gwindowdialogclient.addControl(wSlowfire = new GWindowEditControl(gwindowdialogclient, 9.0F, 9.0F, 3.0F, 1.3F, "") {
            public void afterCreated() {
                super.afterCreated();
                bNumericOnly = bNumericFloat = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i_121_, int i_122_) {
                if (i_121_ != 2)
                    return false;
                String string = getValue();
                float f = 1.0F;
                try {
                    f = Float.parseFloat(string);
                } catch (Exception exception) {
                    /* empty */
                }
                if (f < 0.5F)
                    f = 0.5F;
                if (f > 100.0F)
                    f = 100.0F;
                setValue("" + f, false);
                Actor actor = Plugin.builder.selectedActor();
                Property.set(actor, "slowfire", f);
                PlMission.setChanged();
                return false;
            }
        });
    }

    private void getTimeOut() {
        String string = wTimeOutH.getValue();
        double d = 0.0;
        try {
            d = Double.parseDouble(string);
        } catch (Exception exception) {
            /* empty */
        }
        if (d < 0.0)
            d = 0.0;
        if (d > 12.0)
            d = 12.0;
        string = wTimeOutM.getValue();
        double d_123_ = 0.0;
        try {
            d_123_ = Double.parseDouble(string);
        } catch (Exception exception) {
            /* empty */
        }
        if (d_123_ < 0.0)
            d_123_ = 0.0;
        if (d_123_ > 60.0)
            d_123_ = 60.0;
        float f = (float) (d * 60.0 + d_123_);
        Actor actor = builder.selectedActor();
        Property.set(actor, "timeout", f);
        PlMission.setChanged();
    }

    private void getSleep() {
        String string = wSleepM.getValue();
        double d = 0.0;
        try {
            d = Double.parseDouble(string);
        } catch (Exception exception) {
            /* empty */
        }
        if (d < 0.0)
            d = 0.0;
        if (d > 99.0)
            d = 99.0;
        string = wSleepS.getValue();
        double d_124_ = 0.0;
        try {
            d_124_ = Double.parseDouble(string);
        } catch (Exception exception) {
            /* empty */
        }
        if (d_124_ < 0.0)
            d_124_ = 0.0;
        if (d_124_ > 60.0)
            d_124_ = 60.0;
        Actor actor = builder.selectedActor();
        Property.set(actor, "sleep", (int) (d * 60.0 + d_124_));
        PlMission.setChanged();
    }

    public String mis_getProperties(Actor actor) {
        Orient orient = new Orient();
        String string = "";
        int i = builder.wSelect.comboBox1.getSelected();
        int i_125_ = builder.wSelect.comboBox2.getSelected();
        if (i < startComboBox1 || i >= startComboBox1 + type.length)
            return string;
        i -= startComboBox1;
        if (i_125_ < 0 || i_125_ >= type[i].item.length)
            return string;
        if (Actor.isValid(actor) && Property.containsValue(actor, "builderSpawn")) {
            Point3d point3d = actor.pos.getAbsPoint();
            Orient orient_126_ = actor.pos.getAbsOrient();
            orient.set(orient_126_);
            orient.wrap360();
            float f = Property.floatValue(actor, "timeout", 0.0F);
            if (actor instanceof PlaneGeneric) {
                String string_127_ = ((PlaneGeneric) actor).country;
                string = (" 1_" + actor.name() + " " + ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(point3d.x, point3d.y, orient.azimut()) + " " + f + " " + string_127_);
            } else if (actor instanceof ShipGeneric || actor instanceof BigshipGeneric) {
                int i_128_ = Property.intValue(actor, "sleep", 0);
                int i_129_ = Property.intValue(actor, "skill", 2);
                float f_130_ = Property.floatValue(actor, "slowfire", 1.0F);
                string = (" 1_" + actor.name() + " " + ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(point3d.x, point3d.y, orient.azimut()) + " " + f + " " + i_128_ + " " + i_129_ + " " + f_130_);
            } else if (actor instanceof ArtilleryGeneric) {
                int i_131_ = Property.intValue(actor, "radius_hide", 0);
                string = (" 1_" + actor.name() + " " + ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(point3d.x, point3d.y, orient.azimut()) + " " + f + " " + i_131_);
            } else if (actor instanceof SmokeGeneric)
                string = (" 1_" + actor.name() + " " + ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(point3d.x, point3d.y, orient.azimut()) + " " + formatValue(point3d.z));
            else
                string = (" 1_" + actor.name() + " " + ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(point3d.x, point3d.y, orient.azimut()) + " " + f);
        }
        return string;
    }

    public Actor mis_insert(Loc loc, String string) {
        int i = builder.wSelect.comboBox1.getSelected();
        int i_132_ = builder.wSelect.comboBox2.getSelected();
        if (i < startComboBox1 || i >= startComboBox1 + type.length)
            return null;
        i -= startComboBox1;
        if (i_132_ < 0 || i_132_ >= type[i].item.length)
            return null;
        NumberTokenizer numbertokenizer = new NumberTokenizer(string);
        numbertokenizer.next("");
        String string_134_ = numbertokenizer.next("");
        int i_135_ = numbertokenizer.next(0);
        double d = numbertokenizer.next(0.0);
        double d_136_ = numbertokenizer.next(0.0);
        d = loc.getPoint().x;
        d_136_ = loc.getPoint().y;
        Actor actor = insert(null, string_134_, i_135_, d, d_136_, numbertokenizer.next(0.0F), numbertokenizer.next(0.0F), numbertokenizer.next((String) null), numbertokenizer.next((String) null), numbertokenizer.next((String) null));
        return actor;
    }

    public boolean mis_validateSelected(int i, int i_137_) {
        if (i < startComboBox1 || i >= startComboBox1 + type.length)
            return false;
        i -= startComboBox1;
        if (i_137_ < 0 || i_137_ >= type[i].item.length)
            return false;
        return true;
    }

    static {
        Property.set(PlMisStatic.class, "name", "MisStatic");
    }
}
