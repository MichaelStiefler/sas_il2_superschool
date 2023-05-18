package com.maddox.il2.builder;

import com.maddox.JGP.*;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.gui.GUIAirArming;
import com.maddox.il2.net.NetFileServerSkin;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.il2.objects.ships.*;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.planes.Plane;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.il2.objects.vehicles.radios.LorenzBlindLandingBeacon;
import com.maddox.il2.objects.vehicles.stationary.SirenGeneric;
import com.maddox.il2.objects.vehicles.stationary.SmokeGeneric;
import com.maddox.rts.*;
import com.maddox.util.NumberTokenizer;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.*;

public class PlMisStatic extends Plugin
{
    class ViewItem extends GWindowMenuItem
    {

        public void execute()
        {
            super.bChecked = !super.bChecked;
            viewType(indx);
        }

        int indx;

        public ViewItem(int i, GWindowMenu gwindowmenu, String s, String s1)
        {
            super(gwindowmenu, s, s1);
            indx = 0;
            indx = i;
        }
    }

    static class Type
    {

        public String name;
        public Item item[];

        public Type(String s, Item aitem[])
        {
            name = null;
            item = null;
            name = s;
            item = aitem;
        }
    }

    static class Item
    {

        public String name;
        public Class clazz;
        public int army;
        public ActorSpawn spawn;
        public String country;

        public Item(String s, Class class1, int i)
        {
            name = null;
            clazz = null;
            army = 0;
            spawn = null;
            country = null;
            name = s;
            clazz = class1;
            army = i;
            if(class1 != null)
            {
                spawn = (ActorSpawn)Spawn.get(class1.getName());
                String s1 = I18N.technic(s);
                if(s1.equals(s))
                {
                    s1 = Property.stringValue(class1, "i18nName", s1);
                    if("Plane".equals(s1))
                    {
                        Class class2 = (Class)Property.value(class1, "airClass", null);
                        if(class2 != null)
                        {
                            String s2 = Property.stringValue(class2, "keyName", null);
                            if(s.equals("GenericSpawnPointPlane"))
                                s2 = s;
                            if(s2 != null)
                                s1 = I18N.plane(s2);
                        }
                    }
                }
                Property.set(class1, "i18nName", s1);
            }
        }
    }

    static class Country
    {

        public String name;
        public String i18nName;

        Country()
        {
            name = null;
            i18nName = null;
        }
    }


    public PlMisStatic()
    {
        listCountry = null;
        mapCountry = null;
        allActors = null;
        type = null;
        p3d = null;
        p2d = null;
        p = null;
        o = null;
        spawnArg = null;
        pluginMission = null;
        startComboBox1 = 0;
        viewType = null;
        viewClasses = null;
        _actorInfo = null;
        tabActor = null;
        wName = null;
        wArmy = null;
        wLTimeOutH = null;
        wTimeOutH = null;
        wLTimeOutM = null;
        wTimeOutM = null;
        wLCountry = null;
        wLSkin = null;
        wCountry = null;
        wSkin = null;
        wLSleepM = null;
        wLSleepS = null;
        wSleepM = null;
        wRHide = null;
        wL1RHide = null;
        wL2RHide = null;
        wSleepS = null;
        wLSkill = null;
        wSkill = null;
        wLSlowfire = null;
        wSlowfire = null;
        wLAllowStaticSpawnUsage = null;
        wAllowStaticSpawnUsage = null;
        wLUseMarkings = null;
        wUseMarkings = null;
        wLRestoreWrecked = null;
        wRestoreWrecked = null;
        wLSpotter = null;
        wSpotter = null;
        pathes = null;
        points = null;
        allActors = new ArrayList();
        p3d = new Point3d();
        p2d = new Point2d();
        p = new Point3d();
        o = new Orient();
        spawnArg = new ActorSpawnArg();
        viewClasses = new HashMap();
        _actorInfo = new String[2];
        pathes = new Object[1];
        points = new Object[1];
    }

    private void initCountry()
    {
        if(listCountry != null)
            return;
        listCountry = new ArrayList[3];
        mapCountry = new HashMap[3];
        for(int i = 0; i < 3; i++)
        {
            listCountry[i] = new ArrayList();
            mapCountry[i] = new HashMap();
        }

        ResourceBundle resourcebundle;
        try
        {
            resourcebundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
        }
        catch(Exception exception)
        {
            resourcebundle = null;
        }
        HashMap hashmap = new HashMap();
        List list = Regiment.getAll();
        for(int j = 0; j < list.size(); j++)
        {
            Regiment regiment = (Regiment)list.get(j);
            if(!hashmap.containsKey(regiment.branch()))
            {
                int k = regiment.getArmy();
                if(k >= 0 && k <= 2)
                {
                    hashmap.put(regiment.branch(), null);
                    Country country = new Country();
                    country.name = regiment.branch();
                    if(resourcebundle != null)
                        country.i18nName = resourcebundle.getString(country.name);
                    else
                        country.i18nName = country.name;
                    listCountry[k].add(country);
                    mapCountry[k].put(country.name, new Integer(listCountry[k].size() - 1));
                }
            }
        }

    }

    public void mapLoaded()
    {
        deleteAll();
    }

    public void deleteAll()
    {
        for(int i = 0; i < allActors.size(); i++)
        {
            Actor actor = (Actor)allActors.get(i);
            if(Actor.isValid(actor))
                actor.destroy();
        }

        allActors.clear();
    }

    public void delete(Actor actor)
    {
        allActors.remove(actor);
        actor.destroy();
    }

    public void renderMap2D()
    {
        if(Plugin.builder.isFreeView())
            return;
        Actor actor = Plugin.builder.selectedActor();
        Render.prepareStates();
        for(int i = 0; i < allActors.size(); i++)
        {
            Actor actor1 = (Actor)allActors.get(i);
            if(Actor.isValid(actor1) && actor1.icon != null && viewClasses.containsKey(actor1.getClass()))
            {
                p3d.set(actor1.pos.getAbsPoint());
                if(actor1 instanceof SmokeGeneric)
                    p3d.z = Engine.land().HQ(((Tuple3d) (p3d)).x, ((Tuple3d) (p3d)).y);
                if(Plugin.builder.project2d(p3d, p2d))
                {
                    int j = actor1.getArmy();
                    boolean flag = Plugin.builder.conf.bShowArmy[j];
                    if(flag)
                    {
                        int k;
                        if(Plugin.builder.isMiltiSelected(actor1))
                            k = Builder.colorMultiSelected(Army.color(actor1.getArmy()));
                        else
                        if(actor1 == actor)
                            k = Builder.colorSelected();
                        else
                            k = Army.color(actor1.getArmy());
                        IconDraw.setColor(k);
                        IconDraw.render(actor1, ((Tuple2d) (p2d)).x, ((Tuple2d) (p2d)).y);
                        if(Plugin.builder.conf.bShowName)
                        {
                            String s = Property.stringValue(actor1.getClass(), "i18nName", "");
                            Plugin.builder.smallFont.output(k, (int)((Tuple2d) (p2d)).x + IconDraw.scrSizeX() / 2 + 2, ((int)((Tuple2d) (p2d)).y + Plugin.builder.smallFont.height()) - Plugin.builder.smallFont.descender() - IconDraw.scrSizeY() / 2 - 2, 0.0F, s);
                        }
                    }
                }
            }
        }

    }

    public void load(SectFile sectfile)
    {
        initCountry();
        int i = sectfile.sectionIndex("Stationary");
        if(i >= 0)
        {
            int j = sectfile.vars(i);
            for(int l = 0; l < j; l++)
            {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, l));
                insert(null, numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0D), numbertokenizer.next(0.0D), numbertokenizer.next(0.0F), numbertokenizer.next(0.0F), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next(0));
            }

        }
        i = sectfile.sectionIndex("NStationary");
        if(i >= 0)
        {
            int k = sectfile.vars(i);
            for(int i1 = 0; i1 < k; i1++)
            {
                NumberTokenizer numbertokenizer1 = new NumberTokenizer(sectfile.line(i, i1));
                insert(numbertokenizer1.next(""), numbertokenizer1.next(""), numbertokenizer1.next(0), numbertokenizer1.next(0.0D), numbertokenizer1.next(0.0D), numbertokenizer1.next(0.0F), numbertokenizer1.next(0.0F), numbertokenizer1.next((String)null), numbertokenizer1.next((String)null), numbertokenizer1.next((String)null), numbertokenizer1.next((String)null), numbertokenizer1.next(0));
            }

        }
        Engine.drawEnv().staticTrimToSize();
    }

    public boolean save(SectFile sectfile)
    {
        Orient orient = new Orient();
        int i = sectfile.sectionAdd("NStationary");
        for(int j = 0; j < allActors.size(); j++)
        {
            Actor actor = (Actor)allActors.get(j);
            if(Actor.isValid(actor) && Property.containsValue(actor, "builderSpawn"))
            {
                Point3d point3d = actor.pos.getAbsPoint();
                Orient orient1 = actor.pos.getAbsOrient();
                orient.set(orient1);
                orient.wrap360();
                float f = Property.floatValue(actor, "timeout", 0.0F);
                if(actor instanceof PlaneGeneric)
                {
                    String s = ((PlaneGeneric)actor).branch;
                    String s1 = null;
                    byte byte0 = 1;
                    if(!((PlaneGeneric)actor).getAllowSpawnUse())
                        byte0 = 0;
                    else
                    if(((PlaneGeneric)actor).getRestoreWrecked())
                        byte0 = 2;
                    if(((PlaneGeneric)actor).getSkin() != null)
                    {
                        s1 = ((PlaneGeneric)actor).getSkin();
                        if(s1.indexOf(' ') != -1)
                        {
                            char ac[] = s1.toCharArray();
                            for(int i2 = 0; i2 < ac.length; i2++)
                                if(ac[i2] == ' ')
                                    ac[i2] = '\\';

                            s1 = new String(ac);
                        }
                    }
                    int l1 = 0;
                    if(((PlaneGeneric)actor).getUseMarkings())
                        l1 = 1;
                    sectfile.lineAdd(i, actor.name(), ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, orient.azimut()) + " " + f + " " + s + " " + byte0 + " 1.0 " + s1 + " " + l1);
                } else
                if((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric))
                {
                    int k = Property.intValue(actor, "sleep", 0);
                    int i1 = Property.intValue(actor, "skill", 2);
                    float f1 = Property.floatValue(actor, "slowfire", 1.0F);
                    sectfile.lineAdd(i, actor.name(), ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, orient.azimut()) + " " + f + " " + k + " " + i1 + " " + f1);
                } else
                if(actor instanceof ArtilleryGeneric)
                {
                    int l = Property.intValue(actor, "radius_hide", 0);
                    int j1 = Property.intValue(actor, "skill", 2);
                    int k1 = Property.intValue(actor, "spotter", 1);
                    sectfile.lineAdd(i, actor.name(), ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, orient.azimut()) + " " + f + " " + l + " " + j1 + " " + k1);
                } else
                if(actor instanceof SmokeGeneric)
                    sectfile.lineAdd(i, actor.name(), ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, orient.azimut()) + " " + formatValue(((Tuple3d) (point3d)).z));
                else
                    sectfile.lineAdd(i, actor.name(), ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, orient.azimut()) + " " + f);
            }
        }

        return true;
    }

    private String formatPos(double d, double d1, float f)
    {
        return formatValue(d) + " " + formatValue(d1) + " " + formatValue(f);
    }

    private String formatValue(double d)
    {
        boolean flag = d < 0.0D;
        if(flag)
            d = -d;
        double d1 = (d + 0.0050000000000000001D) - (double)(int)d;
        if(d1 >= 0.10000000000000001D)
            return (flag ? "-" : "") + (int)d + "." + (int)(d1 * 100D);
        else
            return (flag ? "-" : "") + (int)d + ".0" + (int)(d1 * 100D);
    }

    private void makeName(Actor actor, String s)
    {
        if(s != null && Actor.getByName(s) == null)
        {
            actor.setName(s);
            return;
        }
        int i = 0;
        do
        {
            s = i + "_Static";
            if(Actor.getByName(s) != null)
            {
                i++;
            } else
            {
                actor.setName(s);
                return;
            }
        } while(true);
    }

    private Actor insert(String s, String s1, int i, double d, double d1, 
            float f, float f1, String s2, String s3, String s4, String s5, int j)
    {
        Class class1 = null;
        try
        {
            class1 = ObjIO.classForName(s1);
        }
        catch(Exception exception)
        {
            Plugin.builder.tipErr("MissionLoad: class '" + s1 + "' not found");
            return null;
        }
        ActorSpawn actorspawn = (ActorSpawn)Spawn.get(class1.getName(), false);
        if(actorspawn == null)
        {
            Plugin.builder.tipErr("MissionLoad: ActorSpawn for '" + s1 + "' not found");
            return null;
        }
        spawnArg.clear();
        if(s != null)
        {
            if("NONAME".equals(s))
                s = null;
            if(Actor.getByName(s) != null)
                s = null;
        }
        if(i < 0 && i >= Builder.armyAmount())
        {
            Plugin.builder.tipErr("MissionLoad: Wrong actor army '" + i + "'");
            return null;
        }
        spawnArg.army = i;
        spawnArg.armyExist = true;
        spawnArg.country = s2;
        Chief.new_DELAY_WAKEUP = 0.0F;
        ArtilleryGeneric.new_RADIUS_HIDE = 0.0F;
        if(s2 != null)
            try
            {
                Chief.new_DELAY_WAKEUP = Integer.parseInt(s2);
                ArtilleryGeneric.new_RADIUS_HIDE = Chief.new_DELAY_WAKEUP;
            }
            catch(Exception exception1) { }
        Chief.new_SKILL_IDX = 2;
        ArtilleryGeneric.new_SKILL = 1;
        if(s3 != null)
        {
            try
            {
                Chief.new_SKILL_IDX = Integer.parseInt(s3);
                ArtilleryGeneric.new_SKILL = Chief.new_SKILL_IDX;
            }
            catch(Exception exception2) { }
            if(Chief.new_SKILL_IDX < 0 || Chief.new_SKILL_IDX > 3)
            {
                Plugin.builder.tipErr("MissionLoad: Wrong actor skill '" + Chief.new_SKILL_IDX + "'");
                return null;
            }
        }
        Chief.new_SLOWFIRE_K = 1.0F;
        if(s4 != null)
        {
            try
            {
                Chief.new_SLOWFIRE_K = Float.parseFloat(s4);
                ArtilleryGeneric.new_SPOTTER = Chief.new_SLOWFIRE_K > 1.0F;
            }
            catch(Exception exception3) { }
            if(Chief.new_SLOWFIRE_K < 0.5F || Chief.new_SLOWFIRE_K > 100F)
            {
                Plugin.builder.tipErr("MissionLoad: Wrong actor slowfire '" + Chief.new_SLOWFIRE_K + "'");
                return null;
            }
        }
        p.set(d, d1, 0.0D);
        spawnArg.point = p;
        o.set(f, 0.0F, 0.0F);
        spawnArg.orient = o;
        if(s5 != null && s5.equalsIgnoreCase("null"))
            s5 = null;
        spawnArg.paramFileName = s5;
        if(j == 0)
            spawnArg.bNumberOn = false;
        else
            spawnArg.bNumberOn = true;
        try
        {
            Actor actor = actorspawn.actorSpawn(spawnArg);
            if(actor instanceof SirenGeneric)
                actor.setArmy(i);
            if(actor instanceof SmokeGeneric)
            {
                p.z = f1;
                actor.pos.setAbs(p);
                actor.pos.reset();
            }
            Plugin.builder.align(actor);
            Property.set(actor, "builderSpawn", "");
            Property.set(actor, "builderPlugin", this);
            if(!actor.isRealTimeFlag())
                actor.interpCancelAll();
            makeName(actor, s);
            allActors.add(actor);
            if(actor instanceof PlaneGeneric)
            {
                if(Chief.new_SKILL_IDX == 0)
                    ((PlaneGeneric)actor).setAllowSpawnUse(false);
                else
                if(Chief.new_SKILL_IDX == 2)
                    ((PlaneGeneric)actor).setRestoreWrecked(true);
                ((PlaneGeneric)actor).setSkin(s5);
                if(j == 0)
                    ((PlaneGeneric)actor).setUseMarkings(false);
                else
                    ((PlaneGeneric)actor).setUseMarkings(true);
            }
            if((actor instanceof com.maddox.il2.objects.ships.Ship.RwyTransp) || (actor instanceof com.maddox.il2.objects.ships.Ship.RwyTranspWide) || (actor instanceof com.maddox.il2.objects.ships.Ship.RwyTranspSqr))
                ((BigshipGeneric)actor).showTransparentRunwayRed();
            if(actor instanceof LorenzBlindLandingBeacon)
                ((LorenzBlindLandingBeacon)actor).showGuideArrows();
            if(actor instanceof com.maddox.il2.objects.vehicles.planes.Plane.GenericSpawnPointPlane)
                ((PlaneGeneric)actor).showTransparentRed();
            if(actor instanceof ArtilleryGeneric)
            {
                Property.set(actor, "timeout", f1);
                Property.set(actor, "radius_hide", (int)ArtilleryGeneric.new_RADIUS_HIDE);
                Property.set(actor, "skill", ArtilleryGeneric.new_SKILL);
                Property.set(actor, "spotter", Chief.new_SLOWFIRE_K);
            }
            if((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric))
            {
                Property.set(actor, "sleep", (int)Chief.new_DELAY_WAKEUP);
                Property.set(actor, "skill", Chief.new_SKILL_IDX);
                Property.set(actor, "slowfire", Chief.new_SLOWFIRE_K);
            }
            return actor;
        }
        catch(Exception exception4)
        {
            System.out.println(exception4.getMessage());
            exception4.printStackTrace();
            return null;
        }
    }

    private Actor insert(ActorSpawn actorspawn, Loc loc, int i, boolean flag, String s)
    {
        spawnArg.clear();
        spawnArg.point = loc.getPoint();
        spawnArg.orient = loc.getOrient();
        spawnArg.army = i;
        spawnArg.armyExist = true;
        spawnArg.country = s;
        try
        {
            Actor actor = actorspawn.actorSpawn(spawnArg);
            if((actor instanceof com.maddox.il2.objects.ships.Ship.RwyTransp) || (actor instanceof com.maddox.il2.objects.ships.Ship.RwyTranspWide) || (actor instanceof com.maddox.il2.objects.ships.Ship.RwyTranspSqr))
                ((BigshipGeneric)actor).showTransparentRunwayRed();
            if(actor instanceof LorenzBlindLandingBeacon)
                ((LorenzBlindLandingBeacon)actor).showGuideArrows();
            if(actor instanceof com.maddox.il2.objects.vehicles.planes.Plane.GenericSpawnPointPlane)
                ((PlaneGeneric)actor).showTransparentRed();
            if(actor instanceof SirenGeneric)
                actor.setArmy(i);
            Plugin.builder.align(actor);
            Property.set(actor, "builderSpawn", "");
            Property.set(actor, "builderPlugin", this);
            if(!actor.isRealTimeFlag())
                actor.interpCancelAll();
            if(actor instanceof ArtilleryGeneric)
            {
                Property.set(actor, "timeout", 0.0F);
                Property.set(actor, "radius_hide", 0);
                Property.set(actor, "skill", 1);
                Property.set(actor, "slowfire", 0.0F);
            }
            if((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric))
            {
                Property.set(actor, "sleep", 0);
                Property.set(actor, "skill", 2);
                Property.set(actor, "slowfire", 1.0F);
            }
            makeName(actor, null);
            allActors.add(actor);
            if(flag)
                Plugin.builder.setSelected(actor);
            PlMission.setChanged();
            return actor;
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }

    public void insert(Loc loc, boolean flag)
    {
        int i = Plugin.builder.wSelect.comboBox1.getSelected();
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        if(i < startComboBox1 || i >= startComboBox1 + type.length)
            return;
        i -= startComboBox1;
        if(j < 0 || j >= type[i].item.length)
        {
            return;
        } else
        {
            ActorSpawn actorspawn = type[i].item[j].spawn;
            insert(actorspawn, loc, type[i].item[j].army, flag, type[i].item[j].country);
            return;
        }
    }

    public void changeType()
    {
        int i = Plugin.builder.wSelect.comboBox1.getSelected() - startComboBox1;
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        Actor actor = Plugin.builder.selectedActor();
        Loc loc = actor.pos.getAbs();
        Actor actor1 = insert(type[i].item[j].spawn, loc, type[i].item[j].army, true, type[i].item[j].country);
        if(Plugin.builder.selectedActor() != actor)
        {
            allActors.remove(actor);
            String s = actor.name();
            actor.destroy();
            actor1.setName(s);
            if(Plugin.builder.selectedActor() instanceof PlaneGeneric)
                fillSkins();
        }
    }

    public void configure()
    {
        if(Plugin.getPlugin("Mission") == null)
            throw new RuntimeException("PlMisStatic: plugin 'Mission' not found");
        pluginMission = (PlMission)Plugin.getPlugin("Mission");
        if(super.sectFile == null)
            throw new RuntimeException("PlMisStatic: field 'sectFile' not defined");
        SectFile sectfile = new SectFile(super.sectFile, 0);
        int i = sectfile.sections();
        if(i <= 0)
            throw new RuntimeException("PlMisStatic: file '" + super.sectFile + "' is empty");
        type = new Type[i];
        for(int j = 0; j < i; j++)
        {
            String s = sectfile.sectionName(j);
            int k = sectfile.vars(j);
            Item aitem[] = new Item[k];
            for(int l = 0; l < k; l++)
            {
                String s1 = sectfile.var(j, l);
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(j, l));
                String s2 = numbertokenizer.next((String)null);
                int i1 = numbertokenizer.next(0, 0, Builder.armyAmount() - 1);
                Class class1 = null;
                int j1 = s2.indexOf(' ');
                if(j1 > 0)
                    s2 = s2.substring(0, j1);
                try
                {
                    class1 = ObjIO.classForName(s2);
                }
                catch(Exception exception)
                {
                    throw new RuntimeException("PlMisStatic: class '" + s2 + "' not found");
                }
                int k1 = s2.lastIndexOf('$');
                if(k1 >= 0)
                {
                    String s3 = s2.substring(0, k1);
                    try
                    {
                        ObjIO.classForName(s3);
                    }
                    catch(Exception exception1)
                    {
                        throw new RuntimeException("PlMisStatic: Outer class '" + s3 + "' not found");
                    }
                }
                aitem[l] = new Item(s1, class1, i1);
            }

            type[j] = new Type(s, aitem);
        }

    }

    void viewUpdate()
    {
        for(int i = 0; i < allActors.size(); i++)
        {
            Actor actor = (Actor)allActors.get(i);
            if(Actor.isValid(actor) && Property.containsValue(actor, "builderSpawn"))
                actor.drawing(viewClasses.containsKey(actor.getClass()));
        }

        if(Actor.isValid(Plugin.builder.selectedActor()) && !Plugin.builder.selectedActor().isDrawing())
            Plugin.builder.setSelected(null);
        if(!Plugin.builder.isFreeView())
            Plugin.builder.repaint();
    }

    void viewType(int i, boolean flag)
    {
        int j = type[i].item.length;
        for(int k = 0; k < j; k++)
            if(flag)
                viewClasses.put(type[i].item[k].clazz, type[i].item[k]);
            else
                viewClasses.remove(type[i].item[k].clazz);

        viewUpdate();
    }

    void viewType(int i)
    {
        viewType(i, ((GWindowMenuItem) (viewType[i])).bChecked);
    }

    public void viewTypeAll(boolean flag)
    {
        for(int i = 0; i < type.length; i++)
            if(((GWindowMenuItem) (viewType[i])).bChecked != flag)
            {
                viewType[i].bChecked = flag;
                viewType(i, flag);
            }

    }

    private void fillComboBox1()
    {
        startComboBox1 = Plugin.builder.wSelect.comboBox1.size();
        for(int i = 0; i < type.length; i++)
            Plugin.builder.wSelect.comboBox1.add(I18N.technic(type[i].name));

        if(startComboBox1 == 0)
            Plugin.builder.wSelect.comboBox1.setSelected(0, true, false);
    }

    private void fillComboBox2(int i, int j)
    {
        if(i < startComboBox1 || i >= startComboBox1 + type.length)
            return;
        if(Plugin.builder.wSelect.curFilledType != i)
        {
            Plugin.builder.wSelect.curFilledType = i;
            Plugin.builder.wSelect.comboBox2.clear(false);
            for(int k = 0; k < type[i - startComboBox1].item.length; k++)
                Plugin.builder.wSelect.comboBox2.add(Property.stringValue(type[i - startComboBox1].item[k].clazz, "i18nName", ""));

            Plugin.builder.wSelect.comboBox1.setSelected(i, true, false);
        }
        Plugin.builder.wSelect.comboBox2.setSelected(j, true, false);
        fillComboBox2Render(i, j);
    }

    private void fillComboBox2Render(int i, int j)
    {
        try
        {
            Class class1 = type[i - startComboBox1].item[j].clazz;
            if((com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class).isAssignableFrom(class1))
            {
                Class class2 = (Class)Property.value(class1, "airClass", null);
                int k = type[i - startComboBox1].item[j].army;
                String s1 = null;
                String s2 = null;
                if(Actor.isValid(Plugin.builder.selectedActor()))
                {
                    k = Plugin.builder.selectedActor().getArmy();
                    s2 = ((PlaneGeneric)Plugin.builder.selectedActor()).branch;
                    s1 = Regiment.getCountryFromBranch(s2);
                    type[i - startComboBox1].item[j].country = s1;
                    type[i - startComboBox1].item[j].army = k;
                }
                Regiment regiment = Regiment.findFirst(s1, s2, k);
                String s3 = Aircraft.getPropertyMesh(class2, regiment.country());
                Plugin.builder.wSelect.setMesh(s3, false, class2, regiment);
                if(Plugin.builder.wSelect.getHierMesh() != null)
                {
                    PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(class2, regiment.country());
                    paintscheme.prepareNum(class2, Plugin.builder.wSelect.getHierMesh(), regiment, (int)(Math.random() * 3D), (int)(Math.random() * 3D), (int)(Math.random() * 98D + 1.0D));
                    Aircraft.prepareMeshCamouflage(s3, Plugin.builder.wSelect.getHierMesh(), class2, regiment);
                }
            } else
            {
                String s = Property.stringValue(class1, "meshName", null);
                if(s == null)
                {
                    Method method = class1.getMethod("getMeshNameForEditor", null);
                    s = (String)method.invoke(class1, null);
                }
                Plugin.builder.wSelect.setMesh(s, true);
            }
        }
        catch(Exception exception)
        {
            Plugin.builder.wSelect.setMesh(null, true);
        }
    }

    public String[] actorInfo(Actor actor)
    {
        Class class1 = actor.getClass();
        for(int i = 0; i < type.length; i++)
        {
            for(int j = 0; j < type[i].item.length; j++)
                if(class1 == type[i].item[j].clazz)
                {
                    _actorInfo[0] = I18N.technic(type[i].name) + "." + Property.stringValue(type[i].item[j].clazz, "i18nName", "");
                    float f = Property.floatValue(actor, "timeout", 0.0F);
                    if(f > 0.0F)
                        _actorInfo[1] = Plugin.timeSecToString(f * 60F + (float)(int)(World.getTimeofDay() * 60F * 60F));
                    else
                        _actorInfo[1] = null;
                    return _actorInfo;
                }

        }

        return null;
    }

    public void syncSelector()
    {
        Actor actor = Plugin.builder.selectedActor();
        Class class1 = actor.getClass();
        for(int i = 0; i < type.length; i++)
        {
            for(int j = 0; j < type[i].item.length; j++)
                if(class1 == type[i].item[j].clazz)
                {
                    fillComboBox2(i + startComboBox1, j);
                    Plugin.builder.wSelect.tabsClient.addTab(1, tabActor);
                    ((GWindowDialogControl) (wName)).cap.set(Property.stringValue(type[i].item[j].clazz, "i18nName", ""));
                    wArmy.setSelected(actor.getArmy(), true, false);
                    if(actor instanceof ArtilleryGeneric)
                    {
                        float f = Property.floatValue(actor, "timeout", 0.0F);
                        wTimeOutH.setValue("" + (int)((f / 60F) % 24F), false);
                        wTimeOutM.setValue("" + (int)(f % 60F), false);
                        wLTimeOutH.showWindow();
                        wTimeOutH.showWindow();
                        wLTimeOutM.showWindow();
                        wTimeOutM.showWindow();
                        int l = Property.intValue(actor, "radius_hide", 0);
                        wL1RHide.showWindow();
                        wL2RHide.showWindow();
                        wRHide.showWindow();
                        wRHide.setValue("" + l, false);
                        wLSkill.showWindow();
                        wSkill.showWindow();
                        int j1 = Property.intValue(actor, "skill", 1);
                        wSkill.setSelected(j1, true, false);
                        wLSpotter.showWindow();
                        wSpotter.showWindow();
                        wLSpotter.setEnable(true);
                        float f2 = Property.floatValue(actor, "spotter", 1.0F);
                        wSpotter.setChecked(f2 > 1.0F, false);
                        wSpotter.setEnable(true);
                    } else
                    {
                        wLTimeOutH.hideWindow();
                        wTimeOutH.hideWindow();
                        wLTimeOutM.hideWindow();
                        wTimeOutM.hideWindow();
                        wL1RHide.hideWindow();
                        wL2RHide.hideWindow();
                        wRHide.hideWindow();
                        wLSpotter.hideWindow();
                        wSpotter.hideWindow();
                    }
                    if((com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class).isAssignableFrom(class1))
                    {
                        PlaneGeneric planegeneric = (PlaneGeneric)actor;
                        fillCountry(actor.getArmy(), planegeneric.branch);
                        wAllowStaticSpawnUsage.setChecked(planegeneric.getAllowSpawnUse(), false);
                        wRestoreWrecked.setChecked(planegeneric.getRestoreWrecked(), false);
                        wRestoreWrecked.setEnable(planegeneric.getAllowSpawnUse());
                        wLRestoreWrecked.setEnable(planegeneric.getAllowSpawnUse());
                        wLCountry.showWindow();
                        wCountry.showWindow();
                        wAllowStaticSpawnUsage.showWindow();
                        wLAllowStaticSpawnUsage.showWindow();
                        wLRestoreWrecked.showWindow();
                        wRestoreWrecked.showWindow();
                        wLSkin.showWindow();
                        wSkin.showWindow();
                        wUseMarkings.showWindow();
                        wLUseMarkings.showWindow();
                        fillSkins();
                    } else
                    {
                        wLCountry.hideWindow();
                        wCountry.hideWindow();
                        wAllowStaticSpawnUsage.hideWindow();
                        wLAllowStaticSpawnUsage.hideWindow();
                        wLRestoreWrecked.hideWindow();
                        wRestoreWrecked.hideWindow();
                        wLSkin.hideWindow();
                        wSkin.hideWindow();
                        wUseMarkings.hideWindow();
                        wLUseMarkings.hideWindow();
                    }
                    if((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric))
                    {
                        wLSleepM.showWindow();
                        wSleepM.showWindow();
                        wLSleepS.showWindow();
                        wSleepS.showWindow();
                        int k = Property.intValue(actor, "sleep", 0);
                        wSleepM.setValue("" + (k / 60) % 99, false);
                        wSleepS.setValue("" + k % 60, false);
                        wLSkill.showWindow();
                        wSkill.showWindow();
                        int i1 = Property.intValue(actor, "skill", 2);
                        wSkill.setSelected(i1, true, false);
                        wLSlowfire.showWindow();
                        wSlowfire.showWindow();
                        float f1 = Property.floatValue(actor, "slowfire", 1.0F);
                        wSlowfire.setValue("" + f1);
                    } else
                    {
                        wLSleepM.hideWindow();
                        wSleepM.hideWindow();
                        wLSleepS.hideWindow();
                        wSleepS.hideWindow();
                        if(!(actor instanceof ArtilleryGeneric))
                        {
                            wLSkill.hideWindow();
                            wSkill.hideWindow();
                        }
                        wLSlowfire.hideWindow();
                        wSlowfire.hideWindow();
                    }
                    return;
                }

        }

    }

    private String fillCountry(int i, String s)
    {
        initCountry();
        wCountry.clear(false);
        ArrayList arraylist = listCountry[i];
        for(int j = 0; j < arraylist.size(); j++)
        {
            Country country = (Country)arraylist.get(j);
            wCountry.add(country.i18nName);
        }

        if(s != null && !mapCountry[i].containsKey(s))
            s = null;
        if(s == null)
            switch(i)
            {
            case 0: // '\0'
                s = "nn";
                break;

            case 1: // '\001'
                s = "ru";
                break;

            case 2: // '\002'
                s = "de";
                break;
            }
        Integer integer = (Integer)mapCountry[i].get(s);
        wCountry.setSelected(integer.intValue(), true, false);
        return s;
    }

    private void controlResized(GWindowDialogClient gwindowdialogclient, GWindow gwindow)
    {
        if(gwindow == null)
        {
            return;
        } else
        {
            gwindow.setSize(((GWindow) (gwindowdialogclient)).win.dx - gwindow.win.x - gwindowdialogclient.lAF().metric(1.0F), gwindow.win.dy);
            return;
        }
    }

    private void editResized(GWindowDialogClient gwindowdialogclient)
    {
        controlResized(gwindowdialogclient, wName);
        controlResized(gwindowdialogclient, wArmy);
        controlResized(gwindowdialogclient, wCountry);
        controlResized(gwindowdialogclient, wSkin);
    }

    public void createGUI()
    {
        fillComboBox1();
        fillComboBox2(0, 0);
        Plugin.builder.wSelect.comboBox1.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int k, int l)
            {
                int i1 = Plugin.builder.wSelect.comboBox1.getSelected();
                if(i1 >= 0 && k == 2)
                    fillComboBox2(i1, 0);
                return false;
            }

        }
);
        Plugin.builder.wSelect.comboBox2.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int k, int l)
            {
                if(k != 2)
                    return false;
                int i1 = Plugin.builder.wSelect.comboBox1.getSelected();
                if(i1 < startComboBox1 || i1 >= startComboBox1 + type.length)
                {
                    return false;
                } else
                {
                    int j1 = Plugin.builder.wSelect.comboBox2.getSelected();
                    fillComboBox2Render(i1, j1);
                    return false;
                }
            }

        }
);
        int i;
        for(i = Plugin.builder.mDisplayFilter.subMenu.size() - 1; i >= 0; i--)
            if(pluginMission.viewBridge == Plugin.builder.mDisplayFilter.subMenu.getItem(i))
                break;

        if(--i >= 0)
        {
            int j = i;
            i = type.length - 1;
            viewType = new ViewItem[type.length];
            for(; i >= 0; i--)
            {
                ViewItem viewitem = null;
                if("de".equals(RTSConf.cur.locale.getLanguage()))
                    viewitem = (ViewItem)Plugin.builder.mDisplayFilter.subMenu.addItem(j, new ViewItem(i, Plugin.builder.mDisplayFilter.subMenu, I18N.technic(type[i].name) + " " + Plugin.i18n("show"), null));
                else
                    viewitem = (ViewItem)Plugin.builder.mDisplayFilter.subMenu.addItem(j, new ViewItem(i, Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("show") + " " + I18N.technic(type[i].name), null));
                viewitem.bChecked = true;
                viewType[i] = viewitem;
                viewType(i, true);
            }

        }
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient() {

            public void resized()
            {
                super.resized();
                editResized(this);
            }

        }
);
        tabActor = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("StaticActor"), gwindowdialogclient);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 11F, 1.3F, Plugin.i18n("Name"), null));
        gwindowdialogclient.addLabel(wName = new GWindowLabel(gwindowdialogclient, 13F, 1.0F, 7F, 1.3F, "", null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 11F, 1.3F, Plugin.i18n("Army"), null));
        gwindowdialogclient.addControl(wArmy = new GWindowComboControl(gwindowdialogclient, 13F, 3F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                for(int k = 0; k < Builder.armyAmount(); k++)
                    add(I18N.army(Army.name(k)));

            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                int i1 = getSelected();
                actor.setArmy(i1);
                PlMission.setChanged();
                PlMission.checkShowCurrentArmy();
                Class class1 = actor.getClass();
                if((com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class).isAssignableFrom(class1))
                {
                    PlaneGeneric planegeneric = (PlaneGeneric)actor;
                    String s = fillCountry(i1, planegeneric.branch);
                    planegeneric.branch = s;
                    planegeneric.activateMesh(true);
                    for(int j1 = 0; j1 < type.length; j1++)
                    {
                        for(int k1 = 0; k1 < type[j1].item.length; k1++)
                        {
                            if(class1 != type[j1].item[k1].clazz)
                                continue;
                            fillComboBox2Render(j1 + startComboBox1, k1);
                            break;
                        }

                    }

                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wLCountry = new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 11F, 1.3F, Plugin.i18n("Country"), null));
        gwindowdialogclient.addControl(wCountry = new GWindowComboControl(gwindowdialogclient, 13F, 5F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                Class class1 = actor.getClass();
                if(!(com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class).isAssignableFrom(class1))
                    return false;
                int i1 = getSelected();
                Country country = (Country)listCountry[actor.getArmy()].get(i1);
                PlaneGeneric planegeneric = (PlaneGeneric)actor;
                planegeneric.branch = country.name;
                planegeneric.activateMesh(true);
                for(int j1 = 0; j1 < type.length; j1++)
                {
                    for(int k1 = 0; k1 < type[j1].item.length; k1++)
                    {
                        if(class1 != type[j1].item[k1].clazz)
                            continue;
                        fillComboBox2Render(j1 + startComboBox1, k1);
                        break;
                    }

                }

                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wLSkin = new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 11F, 1.3F, Plugin.i18n("Skin"), null));
        gwindowdialogclient.addControl(wSkin = new GWindowComboControl(gwindowdialogclient, 13F, 7F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add(Plugin.i18n("Default"));
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                Class class1 = actor.getClass();
                if(!(com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class).isAssignableFrom(class1))
                    return false;
                int i1 = getSelected();
                PlaneGeneric planegeneric = (PlaneGeneric)actor;
                if(i1 == -1)
                    fillSkins();
                if(i1 <= 0)
                    planegeneric.setSkin(null);
                else
                    planegeneric.setSkin(get(getSelected()));
                planegeneric.activateMesh(true);
                PlMission.setChanged();
                for(int j1 = 0; j1 < type.length; j1++)
                {
                    for(int k1 = 0; k1 < type[j1].item.length; k1++)
                    {
                        if(class1 != type[j1].item[k1].clazz)
                            continue;
                        fillComboBox2Render(j1 + startComboBox1, k1);
                        break;
                    }

                }

                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wLUseMarkings = new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 11F, 1.3F, Plugin.i18n("NumberOn"), null));
        gwindowdialogclient.addControl(wUseMarkings = new GWindowCheckBox(gwindowdialogclient, 13F, 9F, "") {

            public void afterCreated()
            {
                super.afterCreated();
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                Class class1 = actor.getClass();
                if(!(com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class).isAssignableFrom(class1))
                    return false;
                PlaneGeneric planegeneric = (PlaneGeneric)actor;
                planegeneric.setUseMarkings(isChecked());
                planegeneric.activateMesh(true);
                PlMission.setChanged();
                for(int i1 = 0; i1 < type.length; i1++)
                {
                    for(int j1 = 0; j1 < type[i1].item.length; j1++)
                    {
                        if(class1 != type[i1].item[j1].clazz)
                            continue;
                        fillComboBox2Render(i1 + startComboBox1, j1);
                        break;
                    }

                }

                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wLAllowStaticSpawnUsage = new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 11F, 1.3F, Plugin.i18n("AllowSpawn"), null));
        gwindowdialogclient.addControl(wAllowStaticSpawnUsage = new GWindowCheckBox(gwindowdialogclient, 13F, 11F, "") {

            public void afterCreated()
            {
                super.afterCreated();
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                Class class1 = actor.getClass();
                if(!(com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class).isAssignableFrom(class1))
                {
                    return false;
                } else
                {
                    PlaneGeneric planegeneric = (PlaneGeneric)actor;
                    planegeneric.setAllowSpawnUse(isChecked());
                    wLRestoreWrecked.setEnable(isChecked());
                    wRestoreWrecked.setEnable(isChecked());
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(wLRestoreWrecked = new GWindowLabel(gwindowdialogclient, 1.0F, 13F, 11F, 1.3F, Plugin.i18n("RestoreWrecked"), null));
        gwindowdialogclient.addControl(wRestoreWrecked = new GWindowCheckBox(gwindowdialogclient, 13F, 13F, "") {

            public void afterCreated()
            {
                super.afterCreated();
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                Class class1 = actor.getClass();
                if(!(com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class).isAssignableFrom(class1))
                {
                    return false;
                } else
                {
                    PlaneGeneric planegeneric = (PlaneGeneric)actor;
                    planegeneric.setRestoreWrecked(isChecked());
                    return false;
                }
            }

        }
);
        GWindowDialogClient gwindowdialogclient1 = gwindowdialogclient;
        gwindowdialogclient1.addLabel(wLSpotter = new GWindowLabel(gwindowdialogclient1, 1.0F, 13F, 11F, 1.3F, Plugin.i18n("SpotterUsed"), null));
        gwindowdialogclient1.addControl(wSpotter = new GWindowCheckBox(gwindowdialogclient1, 13F, 13F, "") {

            public void afterCreated()
            {
                super.afterCreated();
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                int i1 = Property.intValue(actor, "spotter", 1);
                try
                {
                    if(isChecked())
                        i1 = 2;
                    else
                        i1 = 1;
                }
                catch(Exception exception)
                {
                    isChecked();
                    return false;
                }
                Property.set(actor, "spotter", i1);
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient1.addLabel(wLTimeOutH = new GWindowLabel(gwindowdialogclient1, 1.0F, 5F, 11F, 1.3F, Plugin.i18n("TimeOut"), null));
        gwindowdialogclient1.addControl(wTimeOutH = new GWindowEditControl(gwindowdialogclient1, 13F, 5F, 2.0F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                {
                    return false;
                } else
                {
                    getTimeOut();
                    return false;
                }
            }

        }
);
        gwindowdialogclient1.addLabel(wLTimeOutM = new GWindowLabel(gwindowdialogclient1, 15.2F, 5F, 1.0F, 1.3F, ":", null));
        gwindowdialogclient1.addControl(wTimeOutM = new GWindowEditControl(gwindowdialogclient1, 15.5F, 5F, 2.0F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                {
                    return false;
                } else
                {
                    getTimeOut();
                    return false;
                }
            }

        }
);
        gwindowdialogclient1.addLabel(wL1RHide = new GWindowLabel(gwindowdialogclient1, 1.0F, 7F, 11F, 1.3F, Plugin.i18n("RHide"), null));
        gwindowdialogclient1.addLabel(wL2RHide = new GWindowLabel(gwindowdialogclient1, 18F, 7F, 4F, 1.3F, Plugin.i18n("[M]"), null));
        gwindowdialogclient1.addControl(wRHide = new GWindowEditControl(gwindowdialogclient1, 13F, 7F, 4F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                    return false;
                Actor actor = Plugin.builder.selectedActor();
                int i1 = Property.intValue(actor, "radius_hide", 0);
                String s = getValue();
                try
                {
                    i1 = (int)Double.parseDouble(s);
                    if(i1 < 0)
                    {
                        i1 = 0;
                        setValue("" + i1, false);
                    }
                }
                catch(Exception exception)
                {
                    setValue("" + i1, false);
                    return false;
                }
                Property.set(actor, "radius_hide", i1);
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wLSleepM = new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, Plugin.i18n("Sleep"), null));
        gwindowdialogclient.addControl(wSleepM = new GWindowEditControl(gwindowdialogclient, 9F, 5F, 2.0F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                {
                    return false;
                } else
                {
                    getSleep();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(wLSleepS = new GWindowLabel(gwindowdialogclient, 11.2F, 5F, 1.0F, 1.3F, ":", null));
        gwindowdialogclient.addControl(wSleepS = new GWindowEditControl(gwindowdialogclient, 11.5F, 5F, 2.0F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                {
                    return false;
                } else
                {
                    getSleep();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(wLSkill = new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 7F, 1.3F, Plugin.i18n("Skill"), null));
        gwindowdialogclient.addControl(wSkill = new GWindowComboControl(gwindowdialogclient, 9F, 11F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add(Plugin.i18n("Rookie"));
                add(Plugin.i18n("Average"));
                add(Plugin.i18n("Veteran"));
                add(Plugin.i18n("Ace"));
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                {
                    return false;
                } else
                {
                    Actor actor = Plugin.builder.selectedActor();
                    Property.set(actor, "skill", getSelected());
                    PlMission.setChanged();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(wLSlowfire = new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 7F, 1.3F, Plugin.i18n("Slowfire"), null));
        gwindowdialogclient.addControl(wSlowfire = new GWindowEditControl(gwindowdialogclient, 9F, 9F, 3F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = super.bNumericFloat = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                    return false;
                String s = getValue();
                float f = 1.0F;
                try
                {
                    f = Float.parseFloat(s);
                }
                catch(Exception exception) { }
                if(f < 0.5F)
                    f = 0.5F;
                if(f > 100F)
                    f = 100F;
                setValue("" + f, false);
                Actor actor = Plugin.builder.selectedActor();
                Property.set(actor, "slowfire", f);
                PlMission.setChanged();
                return false;
            }

        }
);
    }

    private void getTimeOut()
    {
        String s = wTimeOutH.getValue();
        double d = 0.0D;
        try
        {
            d = Double.parseDouble(s);
        }
        catch(Exception exception) { }
        if(d < 0.0D)
            d = 0.0D;
        if(d > 12D)
            d = 12D;
        s = wTimeOutM.getValue();
        double d1 = 0.0D;
        try
        {
            d1 = Double.parseDouble(s);
        }
        catch(Exception exception1) { }
        if(d1 < 0.0D)
            d1 = 0.0D;
        if(d1 > 60D)
            d1 = 60D;
        float f = (float)(d * 60D + d1);
        Actor actor = Plugin.builder.selectedActor();
        Property.set(actor, "timeout", f);
        PlMission.setChanged();
    }

    private void getSleep()
    {
        String s = wSleepM.getValue();
        double d = 0.0D;
        try
        {
            d = Double.parseDouble(s);
        }
        catch(Exception exception) { }
        if(d < 0.0D)
            d = 0.0D;
        if(d > 99D)
            d = 99D;
        s = wSleepS.getValue();
        double d1 = 0.0D;
        try
        {
            d1 = Double.parseDouble(s);
        }
        catch(Exception exception1) { }
        if(d1 < 0.0D)
            d1 = 0.0D;
        if(d1 > 60D)
            d1 = 60D;
        Actor actor = Plugin.builder.selectedActor();
        Property.set(actor, "sleep", (int)(d * 60D + d1));
        PlMission.setChanged();
    }

    public String mis_getProperties(Actor actor)
    {
        Orient orient = new Orient();
        String s = "";
        int i = Plugin.builder.wSelect.comboBox1.getSelected();
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        if(i < startComboBox1 || i >= startComboBox1 + type.length)
            return s;
        i -= startComboBox1;
        if(j < 0 || j >= type[i].item.length)
            return s;
        if(Actor.isValid(actor) && Property.containsValue(actor, "builderSpawn"))
        {
            Point3d point3d = actor.pos.getAbsPoint();
            Orient orient1 = actor.pos.getAbsOrient();
            orient.set(orient1);
            orient.wrap360();
            float f = Property.floatValue(actor, "timeout", 0.0F);
            if(actor instanceof PlaneGeneric)
            {
                String s1 = ((PlaneGeneric)actor).branch;
                s = " 1_" + actor.name() + " " + ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, orient.azimut()) + " " + f + " " + s1;
            } else
            if((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric))
            {
                int k = Property.intValue(actor, "sleep", 0);
                int i1 = Property.intValue(actor, "skill", 2);
                float f1 = Property.floatValue(actor, "slowfire", 1.0F);
                s = " 1_" + actor.name() + " " + ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, orient.azimut()) + " " + f + " " + k + " " + i1 + " " + f1;
            } else
            if(actor instanceof ArtilleryGeneric)
            {
                int l = Property.intValue(actor, "radius_hide", 0);
                s = " 1_" + actor.name() + " " + ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, orient.azimut()) + " " + f + " " + l;
            } else
            if(actor instanceof SmokeGeneric)
                s = " 1_" + actor.name() + " " + ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, orient.azimut()) + " " + formatValue(((Tuple3d) (point3d)).z);
            else
                s = " 1_" + actor.name() + " " + ObjIO.classGetName(actor.getClass()) + " " + actor.getArmy() + " " + formatPos(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, orient.azimut()) + " " + f;
        }
        return s;
    }

    public Actor mis_insert(Loc loc, String s)
    {
        int i = Plugin.builder.wSelect.comboBox1.getSelected();
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        if(i < startComboBox1 || i >= startComboBox1 + type.length)
            return null;
        i -= startComboBox1;
        if(j < 0 || j >= type[i].item.length)
        {
            return null;
        } else
        {
            NumberTokenizer numbertokenizer = new NumberTokenizer(s);
            String s1 = numbertokenizer.next("");
            String s2 = numbertokenizer.next("");
            int k = numbertokenizer.next(0);
            double d = numbertokenizer.next(0.0D);
            double d1 = numbertokenizer.next(0.0D);
            d = ((Tuple3d) (loc.getPoint())).x;
            d1 = ((Tuple3d) (loc.getPoint())).y;
            Actor actor = insert(null, s2, k, d, d1, numbertokenizer.next(0.0F), numbertokenizer.next(0.0F), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next((String)null), numbertokenizer.next(1));
            return actor;
        }
    }

    public boolean mis_validateSelected(int i, int j)
    {
        if(i < startComboBox1 || i >= startComboBox1 + type.length)
            return false;
        i -= startComboBox1;
        return j >= 0 && j < type[i].item.length;
    }

    public void dateChanged()
    {
        Actor actor = Plugin.builder.selectedActor();
        if(actor != null)
        {
            Class class1 = actor.getClass();
            if((com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class).isAssignableFrom(class1))
            {
                int j = wCountry.getSelected();
                Country country = (Country)listCountry[actor.getArmy()].get(j);
                PlaneGeneric planegeneric = (PlaneGeneric)actor;
                planegeneric.branch = country.name;
                planegeneric.activateMesh(true);
                for(int k = 0; k < type.length; k++)
                {
                    for(int l = 0; l < type[k].item.length; l++)
                    {
                        if(class1 != type[k].item[l].clazz)
                            continue;
                        fillComboBox2Render(k + startComboBox1, l);
                        break;
                    }

                }

            }
        }
        for(int i = 0; i < allActors.size(); i++)
        {
            Actor actor1 = (Actor)allActors.get(i);
            if(actor1 != null)
            {
                Class class2 = actor1.getClass();
                if((com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class).isAssignableFrom(class2))
                {
                    PlaneGeneric planegeneric1 = (PlaneGeneric)actor1;
                    Class class3 = (Class)Property.value(class2, "airClass", null);
                    int i1 = planegeneric1.getArmy();
                    String s = null;
                    String s1 = null;
                    if(Actor.isValid(planegeneric1))
                    {
                        s1 = planegeneric1.branch;
                        s = Regiment.getCountryFromBranch(s1);
                        planegeneric1.activateMesh(true);
                    }
                    Regiment regiment = Regiment.findFirst(s, s1, i1);
                    String s2 = Aircraft.getPropertyMesh(class3, regiment.country());
                    if(planegeneric1.hierMesh() != null)
                    {
                        PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(class3, regiment.country());
                        paintscheme.prepareNum(class3, planegeneric1.hierMesh(), regiment, (int)(Math.random() * 3D), (int)(Math.random() * 3D), (int)(Math.random() * 98D + 1.0D));
                        Aircraft.prepareMeshCamouflage(s2, planegeneric1.hierMesh(), class3, regiment);
                    }
                }
            }
        }

    }

    private void fillSkins()
    {
        wSkin.clear(false);
        wSkin.add(Plugin.i18n("Default"));
        try
        {
            String s = Main.cur().netFileServerSkin.primaryPath();
            int i = Plugin.builder.wSelect.comboBox1.getSelected();
            if(i < startComboBox1 || i >= startComboBox1 + type.length)
                return;
            i -= startComboBox1;
            int j = Plugin.builder.wSelect.comboBox2.getSelected();
            Class class1 = (Class)Property.value(type[i].item[j].clazz, "airClass", null);
            String s1 = GUIAirArming.validateFileName(Property.stringValue(class1, "keyName", null));
            File file = new File(HomePath.toFileSystemName(s + "/" + s1, 0));
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int k = 0; k < afile.length; k++)
                {
                    File file1 = afile[k];
                    if(file1.isFile())
                    {
                        String s2 = file1.getName();
                        String s3 = s2.toLowerCase();
                        if(s3.endsWith(".bmp") && s3.length() + s1.length() <= 122)
                        {
                            int l = BmpUtils.squareSizeBMP8Pal(s + "/" + s1 + "/" + s2);
                            if(l == 512 || l == 1024)
                                wSkin.add(s2);
                            else
                                System.out.println("Skin " + s + "/" + s1 + "/" + s2 + " NOT loaded");
                        }
                    }
                }

            }
            Actor actor = Plugin.builder.selectedActor();
            if(actor instanceof PlaneGeneric)
            {
                PlaneGeneric planegeneric = (PlaneGeneric)actor;
                boolean flag = planegeneric.getUseMarkings();
                String s4 = planegeneric.getSkin();
                boolean flag1 = false;
                if(s4 != null)
                {
                    for(int i1 = 0; i1 < wSkin.size(); i1++)
                    {
                        String s5 = wSkin.get(i1);
                        if(!s5.equals(s4))
                            continue;
                        wSkin.setSelected(i1, true, true);
                        flag1 = true;
                        break;
                    }

                    if(!flag1)
                        flag = true;
                }
                if(!flag1)
                    wSkin.setSelected(0, true, false);
                wUseMarkings.setChecked(flag, true);
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private ArrayList listCountry[];
    private HashMap mapCountry[];
    protected ArrayList allActors;
    Type type[];
    private Point3d p3d;
    private Point2d p2d;
    private Point3d p;
    private Orient o;
    private ActorSpawnArg spawnArg;
    private PlMission pluginMission;
    private int startComboBox1;
    ViewItem viewType[];
    HashMap viewClasses;
    private String _actorInfo[];
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabActor;
    GWindowLabel wName;
    GWindowComboControl wArmy;
    GWindowLabel wLTimeOutH;
    GWindowEditControl wTimeOutH;
    GWindowLabel wLTimeOutM;
    GWindowEditControl wTimeOutM;
    GWindowLabel wLCountry;
    GWindowLabel wLSkin;
    GWindowComboControl wCountry;
    GWindowComboControl wSkin;
    GWindowLabel wLSleepM;
    GWindowLabel wLSleepS;
    GWindowEditControl wSleepM;
    GWindowEditControl wRHide;
    GWindowLabel wL1RHide;
    GWindowLabel wL2RHide;
    GWindowEditControl wSleepS;
    GWindowLabel wLSkill;
    GWindowComboControl wSkill;
    GWindowLabel wLSlowfire;
    GWindowEditControl wSlowfire;
    GWindowLabel wLAllowStaticSpawnUsage;
    GWindowCheckBox wAllowStaticSpawnUsage;
    GWindowLabel wLUseMarkings;
    GWindowCheckBox wUseMarkings;
    GWindowLabel wLRestoreWrecked;
    GWindowCheckBox wRestoreWrecked;
    GWindowLabel wLSpotter;
    GWindowCheckBox wSpotter;
    private Object pathes[];
    private Object points[];

    static 
    {
        Property.set(com.maddox.il2.builder.PlMisStatic.class, "name", "MisStatic");
    }









}
