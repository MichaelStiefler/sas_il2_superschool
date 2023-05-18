package com.maddox.il2.builder;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.Army;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.buildings.House;
import com.maddox.il2.objects.buildings.Plate;
import com.maddox.rts.*;
import com.maddox.util.NumberTokenizer;
import java.io.PrintStream;
import java.util.*;

public class PlMisHouse extends Plugin
{
    class ViewType extends GWindowMenuItem
    {

        public void execute()
        {
            bChecked = !bChecked;
            viewTypeAll(bChecked);
        }

        public ViewType(GWindowMenu gwindowmenu, String s, String s1)
        {
            super(gwindowmenu, s, s1);
        }
    }

    static class Type
    {

        public int indx;
        public String name;
        public ActorSpawn spawn;
        public String fullClassName;
        public String shortClassName;

        public Type(int i, String s, String s1)
        {
            indx = i;
            name = s;
            fullClassName = s1;
            shortClassName = fullClassName.substring("com.maddox.il2.objects.buildings.".length());
            spawn = (ActorSpawn)Spawn.get_WithSoftClass(fullClassName);
        }
    }


    public PlMisHouse()
    {
        allActors = new ArrayList();
        allTitles = new ArrayList();
        allTypes = new HashMap();
        houseIcon = null;
        p2d = new Point2d();
        p = new Point3d();
        o = new Orient();
        spawnArg = new ActorSpawnArg();
        bView = true;
        viewClasses = new HashMap();
        _actorInfo = new String[1];
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
        if(builder.isFreeView())
            return;
        if(!bView)
            return;
        Actor actor = builder.selectedActor();

        Plugin plugin = (Plugin)Property.value(actor, "builderPlugin");
        if(plugin instanceof PlMisHouse)
        {
            long l = Time.currentReal();
            long l1 = 1000L;
            double d = (2D * (double)(l % l1)) / (double)l1;
            if(d >= 1.0D)
                d = 2D - d;
            int alfa = (int)(100D * d);
            builder.wBrowse.render3D[16].setClearColor(new Color4f(0.51F, 0.87F, 0.92F, (float)alfa / 100F));

        } else
        {
            builder.wBrowse.render3D[16].setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
        }

        Render.prepareStates();
        for(int j = 0; j < allActors.size(); j++)
        {
            Actor actor1 = (Actor)allActors.get(j);
            if(!Actor.isValid(actor1) || actor1.icon == null || !builder.project2d(actor1.pos.getAbsPoint(), p2d))
                continue;
            int k = actor1.getArmy();
            boolean flag = builder.conf.bShowArmy[k];
            if(!flag)
                continue;
            int i;
            if(builder.isMiltiSelected(actor1))
                i = Builder.colorMultiSelected(Army.color(actor1.getArmy()));
            else
            if(actor1 == actor)
                i = Builder.colorSelected();
            else
                i = Army.color(actor1.getArmy());
            IconDraw.setColor(i);
            IconDraw.render(actor1, p2d.x, p2d.y);
        }
    }

    public void load(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("Buildings");
        if(i >= 0)
        {
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
                numbertokenizer.next("");
                insert("com.maddox.il2.objects.buildings." + numbertokenizer.next(""), numbertokenizer.next(1) == 1, numbertokenizer.next(0.0D), numbertokenizer.next(0.0D), numbertokenizer.next(0.0F), false);
            }

        }
    }

    public boolean save(SectFile sectfile)
    {
        Orient orient = new Orient();
        int i = sectfile.sectionAdd("Buildings");
        for(int j = 0; j < allActors.size(); j++)
        {
            Actor actor = (Actor)allActors.get(j);
            if(Actor.isValid(actor))
            {
                Point3d point3d = actor.pos.getAbsPoint();
                Orient orient1 = actor.pos.getAbsOrient();
                orient.set(orient1);
                orient.wrap360();
                Type type1 = (Type)Property.value(actor, "builderType", null);
                sectfile.lineAdd(i, j + "_bld", type1.shortClassName + " " + (actor.isAlive() ? "1 " : "0 ") + formatPos(point3d.x, point3d.y, orient.azimut()));
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

    private Actor insert(String s, boolean flag, double d, double d1, float f, 
            boolean flag1)
    {
        ActorSpawn actorspawn = (ActorSpawn)Spawn.get_WithSoftClass(s, false);
        if(actorspawn == null)
        {
            builder.tipErr("PlMisHouse: ActorSpawn for '" + s + "' not found");
            return null;
        }
        spawnArg.clear();
        p.set(d, d1, 0.0D);
        spawnArg.point = p;
        o.set(f, 0.0F, 0.0F);
        spawnArg.orient = o;
        try
        {
            Actor actor = actorspawn.actorSpawn(spawnArg);
            if(!flag)
                actor.setDiedFlag(true);
            Property.set(actor, "builderSpawn", "");
            Property.set(actor, "builderPlugin", this);
            Type type1 = (Type)allTypes.get(s);
            Property.set(actor, "builderType", type1);
            actor.icon = houseIcon;
            allActors.add(actor);
            builder.align(actor);
            if(flag1)
                builder.setSelected(actor);
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

    private Actor insert(Type type1, Loc loc, boolean flag)
    {
        spawnArg.clear();
        spawnArg.point = loc.getPoint();
        spawnArg.orient = loc.getOrient();
        return insert(type1.fullClassName, true, loc.getPoint().x, loc.getPoint().y, loc.getOrient().getAzimut(), flag);
    }

    public void insert(Loc loc, boolean flag)
    {
        int i = builder.wSelect.comboBox1.getSelected();
        int j = builder.wSelect.comboBox2.getSelected();
        if(i != startComboBox1)
            return;
        if(j < 0 || j >= type.length)
        {
            return;
        } else
        {
            insert(type[j], loc, flag);
            return;
        }
    }

    public void changeType()
    {
        int i = builder.wSelect.comboBox1.getSelected() - startComboBox1;
        int j = builder.wSelect.comboBox2.getSelected();
        Actor actor = builder.selectedActor();
        Loc loc = actor.pos.getAbs();
        Actor actor1 = insert(type[j], loc, true);
        if(builder.selectedActor() != actor)
        {
            allActors.remove(actor);
            actor.destroy();
            PlMission.setChanged();
        }
    }

    public void changeType(boolean flag, boolean flag1)
    {
        if(flag1)
            return;
        if(builder.wSelect.comboBox1.getSelected() != startComboBox1)
            return;
        int i = builder.wSelect.comboBox2.getSelected();
        if(flag)
        {
            if(++i >= type.length)
                i = 0;
        } else
        if(--i < 0)
            i = type.length - 1;
        Actor actor = builder.selectedActor();
        Loc loc = actor.pos.getAbs();
        Actor actor1 = insert(type[i], loc, true);
        if(builder.selectedActor() != actor)
        {
            allActors.remove(actor);
            actor.destroy();
            PlMission.setChanged();
        }
        fillComboBox2(startComboBox1, i);
    }

    public void configure()
    {
        if(getPlugin("Mission") == null)
            throw new RuntimeException("PlMisHouse: plugin 'Mission' not found");
        pluginMission = (PlMission)getPlugin("Mission");
        if(sectFile == null)
            throw new RuntimeException("PlMisHouse: field 'sectFile' not defined");
        SectFile sectfile = new SectFile(sectFile, 0);
        int i = sectfile.sections();
        if(i <= 0)
            throw new RuntimeException("PlMisHouse: file '" + sectFile + "' is empty");
        Type atype[] = new Type[i];
        int j = 0;
        int objectCount = 0;
        for(int k = 0; k < i; k++)
        {
            String s = sectfile.sectionName(k);
            if(s.equals("***"))
            {
                String title = sectfile.varExist(k, "Title") ? sectfile.value(k, sectfile.varIndex(k, "Title")) : "Unnamed Section";
                if(allTitles.size() > 0 && objectCount == 0)
                    allTitles.remove(allTitles.size() - 1);
                allTitles.add("" + j + " " + title);
                objectCount = 0;
            }
            if(s.indexOf("House$") < 0 && s.indexOf("Plate$") < 0 || s.indexOf("Plate$") != -1 && s.indexOf("Kronstadt") != -1)
                continue;
            objectCount++;
            String s1 = s;
            String s2 = "";
            int i1 = s.lastIndexOf('$');
            if(i1 >= 0)
            {
                s1 = s.substring(0, i1);
                s2 = s.substring(i1 + 1);
            }
            Class class1 = null;
            try
            {
                class1 = ObjIO.classForName(s1);
            }
            catch(Exception exception)
            {
                throw new RuntimeException("PlMisHouse: class '" + s1 + "' not found");
            }
            if(i1 >= 0)
                s = class1.getName() + "$" + s2;
            else
                s = class1.getName();
            String s3 = i18n(s2);
            atype[j] = new Type(j, j + " " + s3, s);
            allTypes.put(s, atype[j]);
            j++;
        }

        type = new Type[j];
        for(int l = 0; l < j; l++)
        {
            type[l] = atype[l];
            atype[l] = null;
        }

        atype = null;
        houseIcon = IconDraw.get("icons/objHouse.mat");
    }

    void viewUpdate()
    {
        for(int i = 0; i < allActors.size(); i++)
        {
            Actor actor = (Actor)allActors.get(i);
            if(Actor.isValid(actor))
                actor.drawing(bView);
        }

        if(Actor.isValid(builder.selectedActor()) && !builder.selectedActor().isDrawing())
            builder.setSelected(null);
        if(!builder.isFreeView())
            builder.repaint();
    }

    public void viewTypeAll(boolean flag)
    {
        bView = flag;
        viewCheckBox.bChecked = bView;
        viewUpdate();
    }

    private void fillComboBox1()
    {
        startComboBox1 = builder.wSelect.comboBox1.size();
        builder.wSelect.comboBox1.add(i18n("buildings"));
        if(startComboBox1 == 0)
            builder.wSelect.comboBox1.setSelected(0, true, false);
// Viewer

        builder.wBrowse.allTitles = new int[allTitles.size()];
        for(int k = 0; k < allTitles.size(); k++)
        {
            String titleVal = "" + allTitles.get(k);
            int titleSep = titleVal.indexOf(" ");
            int titleKey = Integer.parseInt(titleVal.substring(0, titleSep));
            titleVal = titleVal.substring(titleSep + 1);
            builder.wBrowse.allTitles[k] = titleKey;
            if(titleKey < type.length)
                builder.wBrowse.comboBox1.add(titleVal);
        }
        if(builder.wBrowse.comboBox1.size() == 0)
        {
            builder.wBrowse.allTitles = new int[1];
            builder.wBrowse.allTitles[0] = 0;
            builder.wBrowse.comboBox1.add("No Sections");
        }
        builder.wBrowse.comboBox1.setSelected(0, true, false);

        for(int k = 0; k < type.length; k++)
            builder.wBrowse.comboBox2.add(type[k].name);
        builder.wBrowse.comboBox2.setSelected(0, true, false);
        fillViewerComboBox2Render(0);
    }

    private void fillComboBox2(int i, int j)
    {
        if(i != startComboBox1)
            return;
        if(builder.wSelect.curFilledType != i)
        {
            builder.wSelect.curFilledType = i;
            builder.wSelect.comboBox2.clear(false);
            for(int k = 0; k < type.length; k++)
                builder.wSelect.comboBox2.add(type[k].name);
            builder.wSelect.comboBox1.setSelected(i, true, false);
        }
        builder.wSelect.comboBox2.setSelected(j, true, false);
        fillComboBox2Render(i, j);
        builder.wBrowse.comboBox2.setSelected(j, true, false);
        fillViewerComboBox2Render(j);
    }

    private void fillComboBox2Render(int i, int j)
    {
        try
        {
            Type type1 = type[j];
            if(type1.shortClassName.startsWith("Plate$"))
            {
                com.maddox.il2.objects.buildings.Plate.SPAWN spawn = (com.maddox.il2.objects.buildings.Plate.SPAWN)type1.spawn;
                builder.wSelect.setMesh(spawn.prop.MeshName, true);
            } else
            {
                com.maddox.il2.objects.buildings.House.SPAWN spawn1 = (com.maddox.il2.objects.buildings.House.SPAWN)type1.spawn;
                builder.wSelect.setMesh(spawn1.prop.MESH0_NAME, true);
            }
        }
        catch(Exception exception)
        {
            builder.wSelect.setMesh(null, true);
        }
    }

    private void fillViewerComboBox2Render(int j)
    {
        for(int itemIndx = -16; itemIndx < 28; itemIndx++)
        {
             try
             {
                 Type type1 = type[j + itemIndx];
                 if(type1.shortClassName.startsWith("Plate$"))
                 {
                     com.maddox.il2.objects.buildings.Plate.SPAWN spawn = (com.maddox.il2.objects.buildings.Plate.SPAWN)type1.spawn;
                     builder.wBrowse.setMesh(itemIndx + 16, spawn.prop.MeshName, true);
                 } else
                 {
                     com.maddox.il2.objects.buildings.House.SPAWN spawn1 = (com.maddox.il2.objects.buildings.House.SPAWN)type1.spawn;
                     builder.wBrowse.setMesh(itemIndx + 16, spawn1.prop.MESH0_NAME, true);
                 }
            }
            catch(Exception exception)
            {
                builder.wBrowse.setMesh(itemIndx + 16, null, true);
            }
        }
    }

    public String[] actorInfo(Actor actor)
    {
        Type type1 = (Type)Property.value(actor, "builderType", null);
        if(type1 != null)
        {
            _actorInfo[0] = type1.name;
            return _actorInfo;
        } else
        {
            return null;
        }
    }

    public void syncSelector()
    {
        Actor actor = builder.selectedActor();
        Type type1 = (Type)Property.value(actor, "builderType", null);
        if(type1 == null)
        {
            return;
        } else
        {
            fillComboBox2(startComboBox1, type1.indx);
            return;
        }
    }

    public void createGUI()
    {
        fillComboBox1();
        fillComboBox2(0, 0);
        builder.wSelect.comboBox1.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int k, int l)
            {
                int i1 = Plugin.builder.wSelect.comboBox1.getSelected();
                if(i1 >= 0 && k == 2)
                    fillComboBox2(i1, 0);
                return false;
            }
        }
);
        builder.wSelect.comboBox2.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int k, int l)
            {
                if(k != 2)
                    return false;
                int i1 = Plugin.builder.wSelect.comboBox1.getSelected();
                if(i1 != startComboBox1)
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
        builder.wBrowse.comboBox2.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int k, int l)
            {
                if(k != 2)
                    return false;
                fillViewerComboBox2Render(Plugin.builder.wBrowse.comboBox2.getSelected());
                return false;
            }
        }
);

        int i;
        for(i = builder.mDisplayFilter.subMenu.size() - 1; i >= 0 && pluginMission.viewBridge != builder.mDisplayFilter.subMenu.getItem(i); i--);
        if(--i >= 0)
        {
            int j = i;
            if("de".equals(RTSConf.cur.locale.getLanguage()))
                viewCheckBox = (ViewType)builder.mDisplayFilter.subMenu.addItem(i, new ViewType(builder.mDisplayFilter.subMenu, i18n("buildings") + " " + i18n("show"), null));
            else
                viewCheckBox = (ViewType)builder.mDisplayFilter.subMenu.addItem(i, new ViewType(builder.mDisplayFilter.subMenu, i18n("show") + " " + i18n("buildings"), null));
            viewTypeAll(true);
        }
    }

    public String mis_getProperties(Actor actor)
    {
        String s = "";
        int i = builder.wSelect.comboBox1.getSelected();
        int j = builder.wSelect.comboBox2.getSelected();
        if(i != startComboBox1)
            return s;
        if(j < 0 || j >= type.length)
        {
            return s;
        } else
        {
            Orient orient = new Orient();
            Point3d point3d = actor.pos.getAbsPoint();
            Orient orient1 = actor.pos.getAbsOrient();
            orient.set(orient1);
            orient.wrap360();
            Type type1 = (Type)Property.value(actor, "builderType", null);
            String s1 = "1_bld " + type1.shortClassName + " " + (actor.isAlive() ? "1 " : "0 ") + formatPos(point3d.x, point3d.y, orient.azimut());
            return s1;
        }
    }

    public Actor mis_insert(Loc loc, String s)
    {
        int i = builder.wSelect.comboBox1.getSelected();
        int k = builder.wSelect.comboBox2.getSelected();
        if(i != startComboBox1)
            return null;
        if(k < 0 || k >= type.length)
        {
            return null;
        } else
        {
            NumberTokenizer numbertokenizer = new NumberTokenizer(s);
            numbertokenizer.next("");
            String s1 = numbertokenizer.next("");
            int j = numbertokenizer.next(1);
            double d = numbertokenizer.next(0.0D);
            d = numbertokenizer.next(0.0D);
            Actor actor = insert("com.maddox.il2.objects.buildings." + s1, j == 1, loc.getPoint().x, loc.getPoint().y, numbertokenizer.next(0.0F), false);
            return actor;
        }
    }

    public boolean mis_validateSelected(int i, int j)
    {
        if(i != startComboBox1)
            return false;
        return j >= 0 && j < type.length;
    }

    private ArrayList allActors;
    private ArrayList allTitles;
    private HashMap allTypes;
    private Mat houseIcon;
    Type type[];
    private Point2d p2d;
    private Point3d p;
    private Orient o;
    private ActorSpawnArg spawnArg;
    private PlMission pluginMission;
    private int startComboBox1;
    private boolean bView;
    private ViewType viewCheckBox;
    HashMap viewClasses;
    private String _actorInfo[];
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabActor;
    GWindowLabel wName;
    GWindowComboControl wArmy;

    static 
    {
        Property.set(com.maddox.il2.builder.PlMisHouse.class, "name", "MisHouse");
    }



}
