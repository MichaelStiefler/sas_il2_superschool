package com.maddox.il2.builder;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.I18N;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import com.maddox.util.NumberTokenizer;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Locale;

public class PlMisChief extends Plugin
{
    static class Item
    {

        public String name;
        public int iSectUnits;
        public String shortClassName;
        public int army;
        public String iconName;
        public double speed;
        public boolean bAirport;

        public Item(int i, String s, String s1, String s2, SectFile sectfile, int j, String s3)
        {
            bAirport = false;
            name = s1;
            iconName = s3;
            iSectUnits = sectfile.sectionIndex(s + "." + s1);
            if(iSectUnits < 0)
                throw new RuntimeException("PlMisChief: section '" + s + "." + s1 + "' not found");
            army = j;
            if(i == 3)
            {
                speed = 11.111111640930176D;
            } else
            {
                try
                {
                    speed = -1D;
                    int k = sectfile.vars(iSectUnits);
                    for(int l = 0; l < k; l++)
                    {
                        String s4 = sectfile.var(iSectUnits, l);
                        Class class1 = PlMisChief.ForceClassLoading(s4);
                        double d = Property.doubleValue(class1, "speed", -1D);
                        if(d < 0.0D)
                            throw new RuntimeException("PlMisChief: property 'speed' NOT found in class '" + s4);
                        if(d > 0.0D && (speed < 0.0D || d < speed))
                            speed = d;
                        if("true".equals(Property.stringValue(class1, "IsAirport", (String)null)))
                            bAirport = true;
                    }

                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    throw new RuntimeException(exception.toString());
                }
                if(speed <= 0.0D)
                    throw new RuntimeException("PlMisChief: section '" + s + "." + s1 + "' speed == 0");
                if(i == 2)
                    speed *= 2D;
            }
        }
    }

    static class Type
    {

        public String name;
        public int moveType;
        public Item item[];

        public Type(String s, int i, Item aitem[])
        {
            name = s;
            moveType = i;
            item = aitem;
        }
    }

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
            indx = i;
        }
    }


    public PlMisChief()
    {
        p = new Point3d();
        roadOffset = new int[1];
        timeMinAbs = new int[1];
        viewMap = new HashMapInt();
        _actorInfo = new String[2];
    }

    public static Class ForceClassLoading(String s)
    {
        Class class1;
        try
        {
            class1 = Class.forName(s);
        }
        catch(Exception exception)
        {
            System.out.println("PlMisChief: class '" + s + "' not found.");
            exception.printStackTrace();
            throw new RuntimeException("Failure");
        }
        int i = s.lastIndexOf('$');
        if(i >= 0)
        {
            String s1 = s.substring(0, i);
            try
            {
                ObjIO.classForName(s1);
            }
            catch(Exception exception1)
            {
                System.out.println("PlMisChief: outer class '" + s1 + "' not found.");
                exception1.printStackTrace();
                throw new RuntimeException("Failure");
            }
        }
        return class1;
    }

    public static int moveType(int i)
    {
        PlMisChief plmischief = (PlMisChief)Plugin.getPlugin("MisChief");
        return plmischief.type[i].moveType;
    }

    public static double speed(int i, int j)
    {
        PlMisChief plmischief = (PlMisChief)Plugin.getPlugin("MisChief");
        return plmischief.type[i].item[j].speed;
    }

    public static boolean isAirport(int i, int j)
    {
        PlMisChief plmischief = (PlMisChief)Plugin.getPlugin("MisChief");
        return plmischief.type[i].item[j].bAirport;
    }

    private String makeName()
    {
        int i = 0;
        do
        {
            String s = i + "_Chief";
            if(Actor.getByName(s) == null)
                return s;
            i++;
        } while(true);
    }

    public void load(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("Chiefs");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        for(int k = 0; k < j; k++)
        {
            NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
            String s = numbertokenizer.next("");
            String s1 = numbertokenizer.next("");
            int l = numbertokenizer.next(0);
            if(l < 1 && l >= Builder.armyAmount())
            {
                Plugin.builder.tipErr("MissionLoad: Wrong chief's army '" + l + "' for actor '" + s + "'");
                continue;
            }
            float f = numbertokenizer.next(0.0F);
            int i1 = numbertokenizer.next(2, 0, 3);
            float f1 = numbertokenizer.next(1.0F, 0.5F, 100F);
            if(fSectsUnits.sectionIndex(s1) < 0)
            {
                Plugin.builder.tipErr("MissionLoad: Wrong chief's type '" + s1 + "'");
                String header = "MissionPro by PAL - Notice";
                String text = "The chief's type '" + s1 + "' doesn't exist in your current installation!\nYou should manually Edit the .mis file to replace the object.";
                new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 40F, true, header, text, 4, 0.0F);
                continue;
            }
            int j1 = s1.indexOf('.');
            if(j1 <= 0)
            {
                Plugin.builder.tipErr("MissionLoad: Wrong chief's type '" + s1 + "'");
                String header = "MissionPro by PAL - Notice";
                String text = "The chief's type '" + s1 + "' doesn't exist in your current installation!\nYou should manually Edit the .mis file to replace the object.";
                new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 40F, true, header, text, 4, 0.0F);
                continue;
            }
            String s2 = s1.substring(0, j1);
            String s3 = s1.substring(j1 + 1);
            String s4 = fSectsUnits.get(s2, s3);
            if(s4 == null)
            {
                Plugin.builder.tipErr("MissionLoad: Wrong chief's type '" + s1 + "'");
                String header = "MissionPro by PAL - Notice";
                String text = "The chief's type '" + s1 + "' doesn't exist in your current installation!\nYou should manually Edit the .mis file to replace the object.";
                new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 40F, true, header, text, 4, 0.0F);
                continue;
            }
            if(Actor.getByName(s) != null)
            {
                Plugin.builder.tipErr("MissionLoad: actor '" + s + "' alredy exist");
                continue;
            }
            int k1;
            for(k1 = 0; k1 < type.length && !type[k1].name.equals(s2); k1++);
            if(k1 == type.length)
            {
                Plugin.builder.tipErr("MissionLoad: Wrong chief's category '" + s1 + "'");
                continue;
            }
            int l1;
            for(l1 = 0; l1 < type[k1].item.length && !type[k1].item[l1].name.equals(s3); l1++);
            if(l1 == type[k1].item.length)
            {
                Plugin.builder.tipErr("MissionLoad: Wrong chief's type '" + s1 + "'");
                String header = "MissionPro by PAL - Notice";
                String text = "The chief's type '" + s1 + "' doesn't exist in your current installation!\nYou should manually Edit the .mis file to replace the object.";
                new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 40F, true, header, text, 4, 0.0F);
                continue;
            }
            int i2 = sectfile.sectionIndex(s + "_Road");
            if(i2 < 0)
            {
                Plugin.builder.tipErr("MissionLoad: Wrong chief's road '" + s + "'");
                continue;
            }
            PathChief pathchief = new PathChief(Plugin.builder.pathes, type[k1].moveType, k1, l1, fSectsUnits, type[k1].item[l1].iSectUnits, p);
            pathchief.setName(s);
            pathchief.setArmy(l);
            pathchief.drawing(viewMap.containsKey(k1));
            pathchief._sleep = Math.round(f);
            pathchief._skill = i1;
            pathchief._slowfire = f1;
            Property.set(pathchief, "builderPlugin", this);
            Property.set(pathchief, "i18nName", I18N.technic(type[k1].item[l1].name));
            PNodes pnodes = new PNodes(pathchief, null, type[k1].item[l1].iconName, null);
            roadOffset[0] = 0;
            try
            {
                do
                {
                    timeMinAbs[0] = 0;
                    pnodes.posXYZ = loadRoadSegments(sectfile, i2, roadOffset, timeMinAbs, type[k1].item[l1].speed / 2D);
                    pnodes.speed = roadSpeed;
                    clampSpeed(pnodes);
                    if(pnodes.posXYZ == null)
                        break;
                    pnodes.timeoutMin = timeMinAbs[0];
                    pnodes = new PNodes(pathchief, pnodes, null);
                } while(true);
            }
            catch(Exception exception)
            {
                Plugin.builder.tipErr("MissionLoad: Wrong chief's road '" + s + "'");
                pathchief.destroy();
                continue;
            }
            int j2 = pathchief.points();
            if(j2 < 2)
            {
                Plugin.builder.tipErr("MissionLoad: Wrong chief's road '" + s + "'");
            } else
            {
                for(int k2 = 0; k2 < j2 - 1; k2++)
                {
                    pnodes = (PNodes)pathchief.point(k2);
                    p.set(pnodes.posXYZ[0], pnodes.posXYZ[1], pnodes.posXYZ[2]);
                    ((Actor) (pnodes)).pos.setAbs(p);
                }

                pnodes = (PNodes)pathchief.point(j2 - 2);
                int l2 = pnodes.posXYZ.length / 4 - 1;
                p.set(pnodes.posXYZ[l2 * 4 + 0], pnodes.posXYZ[l2 * 4 + 1], pnodes.posXYZ[l2 * 4 + 2]);
                pnodes = (PNodes)pathchief.point(j2 - 1);
                ((Actor) (pnodes)).pos.setAbs(p);
                pathchief.updateUnitsPos();
                pathchief.computeTimesLoaded();
            }
        }

    }

    private float[] loadRoadSegments(SectFile sectfile, int i, int ai[], int ai1[], double d)
    {
        float af[] = null;
        int j = ai[0];
        if(sectfile.vars(i) <= j)
            return null;
        NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, j));
        numbertokenizer.next(0.0F);
        numbertokenizer.next(0.0F);
        numbertokenizer.next(0.0F);
        ai1[0] = numbertokenizer.next(0);
        int k = numbertokenizer.next(0);
        if(k == 0)
            return null;
        roadSpeed = numbertokenizer.next(d);
        af = new float[k * 4];
        for(int l = 0; k-- > 0; l += 4)
        {
            NumberTokenizer numbertokenizer1 = new NumberTokenizer(sectfile.line(i, j++));
            af[l + 0] = numbertokenizer1.next(0.0F);
            af[l + 1] = numbertokenizer1.next(0.0F);
            Engine.land();
            af[l + 2] = Landscape.HQ(af[l + 0], af[l + 1]);
            af[l + 3] = numbertokenizer1.next(0.0F);
            numbertokenizer1.next(0);
        }

        ai[0] = j - 1;
        return af;
    }

    private void saveRoadSegment(PNodes pnodes, int i, SectFile sectfile, int j, double d)
    {
        float af[] = pnodes.posXYZ;
        float f = af[i * 4 + 0];
        float f1 = af[i * 4 + 1];
        float f3 = af[i * 4 + 3];
        if(i == 0)
        {
            if(pnodes.timeoutMin > 0.0D)
                sectfile.lineAdd(j, formatPosFloat(f), formatPosFloat(f1) + " " + formatPosFloat(f3) + " " + Math.round((((PPoint) (pnodes)).time + pnodes.timeoutMin * 60D) / 60D) + " " + af.length / 4 + " " + d);
            else
                sectfile.lineAdd(j, formatPosFloat(f), formatPosFloat(f1) + " " + formatPosFloat(f3) + " 0 " + af.length / 4 + " " + d);
        } else
        {
            sectfile.lineAdd(j, formatPosFloat(f), formatPosFloat(f1) + " " + formatPosFloat(f3));
        }
    }

    public boolean save(SectFile sectfile)
    {
        if(Plugin.builder.pathes == null)
            return true;
        int i = sectfile.sectionIndex("Chiefs");
        Object aobj[] = Plugin.builder.pathes.getOwnerAttached();
        for(int j = 0; j < aobj.length; j++)
        {
            Actor actor = (Actor)aobj[j];
            if(actor == null)
                break;
            if(actor instanceof PathChief)
            {
                PathChief pathchief = (PathChief)actor;
                int k = pathchief.points();
                if(k < 2)
                {
                    new GWindowMessageBox(((GWindow) (Plugin.builder.viewWindow)).root, 20F, true, "Save error", "Chief '" + pathchief.name() + "' contains only one waypoint", 3, 0.0F);
                    return false;
                }
                if(i <= -1)
                    i = sectfile.sectionAdd("Chiefs");
                String s = pathchief.name();
                String s1 = type[pathchief._iType].name + "." + type[pathchief._iType].item[pathchief._iItem].name;
                int l = pathchief.getArmy();
                String s2 = s + "_Road";
                if(type[pathchief._iType].moveType == 2)
                    sectfile.lineAdd(i, s, s1 + " " + l + " " + pathchief._sleep + " " + pathchief._skill + " " + pathchief._slowfire);
                else
                    sectfile.lineAdd(i, s, s1 + " " + l);
                int i1 = sectfile.sectionAdd(s2);
                for(int j1 = 0; j1 < k - 1; j1++)
                {
                    PNodes pnodes1 = (PNodes)pathchief.point(j1);
                    int k1 = pnodes1.posXYZ.length / 4;
                    for(int l1 = 0; l1 < k1 - 1; l1++)
                        saveRoadSegment(pnodes1, l1, sectfile, i1, pnodes1.speed);

                }

                PNodes pnodes = (PNodes)pathchief.point(k - 2);
                saveRoadSegment(pnodes, pnodes.posXYZ.length / 4 - 1, sectfile, i1, pnodes.speed);
            }
        }

        return true;
    }

    private String formatPosFloat(float f)
    {
        boolean flag = (double)f < 0.0D;
        if(flag)
            f = -f;
        double d = ((double)f + 0.0050000000000000001D) - (double)(int)f;
        if(d >= 0.10000000000000001D)
            return (flag ? "-" : "") + (int)f + "." + (int)(d * 100D);
        else
            return (flag ? "-" : "") + (int)f + ".0" + (int)(d * 100D);
    }

    public void insert(Loc paramLoc, boolean paramBoolean)
    {
        Object localObject = null;
        try
        {
            Point3d localPoint3d = paramLoc.getPoint();
            int i = Plugin.builder.wSelect.comboBox1.getSelected();
            int j = Plugin.builder.wSelect.comboBox2.getSelected();
            if(Plugin.builder.selectedPath() != null)
            {
                localObject = Plugin.builder.selectedPath();
                if(!(localObject instanceof PathChief))
                    return;
                PathChief localPathChief = (PathChief)localObject;
                if(i - startComboBox1 != localPathChief._iType || j != localPathChief._iItem)
                    Plugin.builder.setSelected(null);
            }
            PNodes localPNodes;
            if(Plugin.builder.selectedPoint() != null)
            {
                if(type[i - startComboBox1].moveType == 3 && ((Path)localObject).points() >= 2)
                    return;
                localPNodes = new PNodes(Plugin.builder.selectedPath(), Plugin.builder.selectedPoint(), localPoint3d);
                localPNodes.speed = type[i - startComboBox1].item[j].speed / 2D;
            } else
            {
                if(i < startComboBox1 || i >= startComboBox1 + type.length)
                    return;
                i -= startComboBox1;
                if(j < 0 || j >= type[i].item.length)
                    return;
                localObject = new PathChief(Plugin.builder.pathes, type[i].moveType, i, j, fSectsUnits, type[i].item[j].iSectUnits, localPoint3d);
                ((Path)localObject).setName(makeName());
                ((Path)localObject).setArmy(type[i].item[j].army);
                Property.set(localObject, "builderPlugin", this);
                Property.set(localObject, "i18nName", I18N.technic(type[i].item[j].name));
                ((Path)localObject).drawing(viewMap.containsKey(i));
                localPNodes = new PNodes((Path)localObject, null, type[i].item[j].iconName, localPoint3d);
                localPNodes.speed = type[i].item[j].speed / 2D;
            }
            Plugin.builder.setSelected(localPNodes);
            PlMission.setChanged();
        }
        catch(Exception localException)
        {
            if(localObject != null && ((Path)localObject).points() == 0)
                ((Path)localObject).destroy();
            System.out.println(localException);
        }
    }

    private void clampSpeed(PNodes pnodes)
    {
        PathChief pathchief = (PathChief)pnodes.getOwner();
        if(pnodes.speed > type[pathchief._iType].item[pathchief._iItem].speed)
            pnodes.speed = type[pathchief._iType].item[pathchief._iItem].speed;
        if(pnodes.speed < 1.0D)
            pnodes.speed = 1.0D;
    }

    private void clampSpeed(PathChief pathchief)
    {
        int i = pathchief.points();
        for(int j = 0; j < i; j++)
            clampSpeed((PNodes)pathchief.point(j));

    }

    public void changeType()
    {
        int i = Plugin.builder.wSelect.comboBox1.getSelected() - startComboBox1;
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
        if(i != pathchief._iType)
            return;
        pathchief.setUnits(i, j, fSectsUnits, type[i].item[j].iSectUnits, ((Actor) (pathchief.point(0))).pos.getAbsPoint());
        Property.set(pathchief, "i18nName", I18N.technic(type[i].item[j].name));
        if(moveType(i) == 2)
            clampSpeed(pathchief);
        pathchief.updateUnitsPos();
        PlMission.setChanged();
    }

    public void configure()
    {
        if(Plugin.getPlugin("Mission") == null)
            throw new RuntimeException("PlMisChief: plugin 'Mission' not found");
        pluginMission = (PlMission)Plugin.getPlugin("Mission");
        if(super.sectFile == null)
            throw new RuntimeException("PlMisChief: field 'sectFile' not defined");
        SectFile sectfile = new SectFile(super.sectFile, 0);
        int i = sectfile.sections();
        if(i <= 0)
            throw new RuntimeException("PlMisChief: file '" + super.sectFile + "' is empty");
        int j = 0;
        for(int k = 0; k < i; k++)
        {
            String s = sectfile.sectionName(k);
            if(s.indexOf('.') < 0)
                j++;
        }

        if(j == 0)
            throw new RuntimeException("PlMisChief: file '" + super.sectFile + "' is empty");
        fSectsUnits = sectfile;
        type = new Type[j];
        int l = 0;
        for(int i1 = 0; i1 < i; i1++)
        {
            String s1 = sectfile.sectionName(i1);
            if(s1.indexOf('.') < 0)
            {
                int j1 = sectfile.vars(i1);
                if(j1 < 2)
                    throw new RuntimeException("PlMisChief: file '" + super.sectFile + "', section '" + s1 + "' missing moveType and/or class");
                if(sectfile.varIndex(i1, "moveType") != 0)
                    throw new RuntimeException("PlMisChief: file '" + super.sectFile + "', section '" + s1 + "' moveType must be first row");
                String s2 = sectfile.value(i1, 0);
                byte byte0 = 0;
                if("VEHICLE".equals(s2))
                    byte0 = 0;
                else
                if("TROOPER".equals(s2))
                    byte0 = 1;
                else
                if("SHIP".equals(s2))
                    byte0 = 2;
                else
                if("TRAIN".equals(s2))
                    byte0 = 3;
                else
                    throw new RuntimeException("PlMisChief: file '" + super.sectFile + "', section '" + s1 + "' unknown moveType '" + s2 + "'");
                Item aitem[] = new Item[j1 - 1];
                for(int k1 = 1; k1 < j1; k1++)
                {
                    String s3 = sectfile.var(i1, k1);
                    NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(i1, k1));
                    String s4 = numbertokenizer.next((String)null);
                    int l1 = numbertokenizer.next(1, 1, Builder.armyAmount() - 1);
                    String s5 = numbertokenizer.next("icons/tank.mat");
                    aitem[k1 - 1] = new Item(byte0, s1, s3, s4, sectfile, l1, s5);
                }

                type[l++] = new Type(s1, byte0, aitem);
            }
        }

        fSectsEmpty = new SectFile();
    }

    void viewUpdate()
    {
        if(Plugin.builder.pathes == null)
            return;
        Object aobj[] = Plugin.builder.pathes.getOwnerAttached();
        for(int i = 0; i < aobj.length; i++)
        {
            Actor actor = (Actor)aobj[i];
            if(actor == null)
                break;
            if(actor instanceof PathChief)
            {
                PathChief pathchief1 = (PathChief)actor;
                pathchief1.drawing(viewMap.containsKey(pathchief1._iType));
            }
        }

        if(Plugin.builder.selectedPath() != null)
        {
            Path path = Plugin.builder.selectedPath();
            if(path instanceof PathChief)
            {
                PathChief pathchief = (PathChief)path;
                if(!viewMap.containsKey(pathchief._iType))
                    Plugin.builder.setSelected(null);
            }
        }
        if(!Plugin.builder.isFreeView())
            Plugin.builder.repaint();
    }

    void viewType(int i, boolean flag)
    {
        if(flag)
            viewMap.put(i, null);
        else
            viewMap.remove(i);
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
                Plugin.builder.wSelect.comboBox2.add(I18N.technic(type[i - startComboBox1].item[k].name));

            Plugin.builder.wSelect.comboBox1.setSelected(i, true, false);
        }
        Plugin.builder.wSelect.comboBox2.setSelected(j, true, false);
        setSelectorMesh();
    }

    public boolean setSelectorMesh()
    {
        int i = Plugin.builder.wSelect.comboBox1.getSelected();
        if(i < startComboBox1 || i >= startComboBox1 + type.length)
            return false;
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        try
        {
            String s = fSectsUnits.var(type[i - startComboBox1].item[j].iSectUnits, 0);
            Class class1 = ForceClassLoading(s);
            String s1 = Property.stringValue(class1, "meshName", null);
            if(s1 == null)
            {
                Method method = class1.getMethod("getMeshNameForEditor", null);
                s1 = (String)method.invoke(class1, null);
            }
            Plugin.builder.wSelect.setMesh(s1, true);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            Plugin.builder.wSelect.setMesh(null, true);
            return false;
        }
        return true;
    }

    public String[] actorInfo(Actor actor)
    {
        PathChief pathchief = (PathChief)actor.getOwner();
        _actorInfo[0] = I18N.technic(type[pathchief._iType].name) + "." + I18N.technic(type[pathchief._iType].item[pathchief._iItem].name);
        PNodes pnodes = (PNodes)actor;
        int i = pathchief.pointIndx(pnodes);
        if(pnodes.timeoutMin > 0.0D)
            _actorInfo[1] = "(" + i + ") in:" + Plugin.timeSecToString(((PPoint) (pnodes)).time + (double)(int)(World.getTimeofDay() * 60F * 60F)) + " out:" + Plugin.timeSecToString(((PPoint) (pnodes)).time + (double)(int)(World.getTimeofDay() * 60F * 60F) + pnodes.timeoutMin * 60D);
        else
            _actorInfo[1] = "(" + i + ") " + Plugin.timeSecToString(((PPoint) (pnodes)).time + (double)(int)(World.getTimeofDay() * 60F * 60F));
        return _actorInfo;
    }

    public void syncSelector()
    {
        PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
        fillComboBox2(pathchief._iType + startComboBox1, pathchief._iItem);
        Plugin.builder.wSelect.tabsClient.addTab(1, tabActor);
        ((GWindowDialogControl) (wName)).cap.set(Property.stringValue(pathchief, "i18nName", ""));
        wArmy.setSelected(pathchief.getArmy() - 1, true, false);
        if(moveType(pathchief._iType) == 2)
        {
            wLSleepM.showWindow();
            wSleepM.showWindow();
            wLSleepS.showWindow();
            wSleepS.showWindow();
            wSleepM.setValue("" + (pathchief._sleep / 60) % 99, false);
            wSleepS.setValue("" + pathchief._sleep % 60, false);
            wLSkill.showWindow();
            wSkill.showWindow();
            wSkill.setSelected(pathchief._skill, true, false);
            wLSlowfire.showWindow();
            wSlowfire.showWindow();
            wSlowfire.setValue("" + pathchief._slowfire);
        } else
        {
            wLSleepM.hideWindow();
            wSleepM.hideWindow();
            wLSleepS.hideWindow();
            wSleepS.hideWindow();
            wLSkill.hideWindow();
            wSkill.hideWindow();
            wLSlowfire.hideWindow();
            wSlowfire.hideWindow();
        }
        if(moveType(pathchief._iType) != 3)
        {
            Plugin.builder.wSelect.tabsClient.addTab(2, tabWay);
            fillDialogWay();
        }
    }

    public void updateSelector()
    {
        fillDialogWay();
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
                if(!setSelectorMesh())
                {
                    return false;
                } else
                {
                    PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
                    ((GWindowDialogControl) (wName)).cap.set(Property.stringValue(pathchief, "i18nName", ""));
                    return false;
                }
            }

        }
);
        int i;
        for(i = Plugin.builder.mDisplayFilter.subMenu.size() - 1; i >= 0 && pluginMission.viewBridge != Plugin.builder.mDisplayFilter.subMenu.getItem(i); i--);
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
        tabActor = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("ChiefActor"), gwindowdialogclient);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, Plugin.i18n("Name"), null));
        gwindowdialogclient.addLabel(wName = new GWindowLabel(gwindowdialogclient, 9F, 1.0F, 7F, 1.3F, "", null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, Plugin.i18n("Army"), null));
        gwindowdialogclient.addControl(wArmy = new GWindowComboControl(gwindowdialogclient, 9F, 3F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                for(int k = 1; k < Builder.armyAmount(); k++)
                    add(I18N.army(Army.name(k)));

            }

            public boolean notify(int k, int l)
            {
                if(k != 2)
                {
                    return false;
                } else
                {
                    Path path = Plugin.builder.selectedPath();
                    int i1 = getSelected() + 1;
                    path.setArmy(i1);
                    PlMission.setChanged();
                    PlMission.checkShowCurrentArmy();
                    return false;
                }
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
        gwindowdialogclient.addLabel(wLSkill = new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 7F, 1.3F, Plugin.i18n("Skill"), null));
        gwindowdialogclient.addControl(wSkill = new GWindowComboControl(gwindowdialogclient, 9F, 7F, 7F) {

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
                    PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
                    pathchief._skill = getSelected();
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
                PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
                pathchief._slowfire = f;
                PlMission.setChanged();
                return false;
            }

        }
);
        initEditWay();
    }

    private void getSleep()
    {
        PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
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
        pathchief._sleep = (int)(d * 60D + d1);
        PlMission.setChanged();
    }

    private void fillDialogWay()
    {
        PNodes pnodes = (PNodes)Plugin.builder.selectedPoint();
        PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
        int i = pathchief.pointIndx(pnodes);
        wCur.cap = new GCaption("" + i + "(" + pathchief.points() + ")");
        wTime.cap = new GCaption(Plugin.timeSecToString(((PPoint) (pnodes)).time + (double)(World.getTimeofDay() * 60F * 60F)));
        wPrev.setEnable(i > 0);
        if(i < pathchief.points() - 1)
        {
            wNext.setEnable(true);
            wTimeOutH.setEnable(true);
            wTimeOutM.setEnable(true);
            wTimeOutH.setValue("" + (int)((pnodes.timeoutMin / 60D) % 24D), false);
            wTimeOutM.setValue("" + (int)(pnodes.timeoutMin % 60D), false);
        } else
        {
            pnodes.timeoutMin = 0.0D;
            wNext.setEnable(false);
            wTimeOutH.setEnable(false);
            wTimeOutM.setEnable(false);
            wTimeOutH.setValue("0", false);
            wTimeOutM.setValue("0", false);
        }
        if(moveType(pathchief._iType) != 2)
        {
            wLSpeed0.hideWindow();
            wLSpeed1.hideWindow();
            wSpeed.hideWindow();
        } else
        {
            wLSpeed0.showWindow();
            wLSpeed1.showWindow();
            wSpeed.showWindow();
            wSpeed.setValue("" + (int)Math.round(pnodes.speed * 3.6000000000000001D), false);
        }
    }

    public void initEditWay()
    {
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient());
        tabWay = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("Waypoint"), gwindowdialogclient);
        gwindowdialogclient.addControl(wPrev = new GWindowButton(gwindowdialogclient, 1.0F, 1.0F, 5F, 1.6F, Plugin.i18n("&Prev"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PNodes pnodes = (PNodes)Plugin.builder.selectedPoint();
                    PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
                    int k = pathchief.pointIndx(pnodes);
                    if(k > 0)
                    {
                        Plugin.builder.setSelected(pathchief.point(k - 1));
                        fillDialogWay();
                        Plugin.builder.repaint();
                    }
                    return true;
                } else
                {
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addControl(wNext = new GWindowButton(gwindowdialogclient, 9F, 1.0F, 5F, 1.6F, Plugin.i18n("&Next"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PNodes pnodes = (PNodes)Plugin.builder.selectedPoint();
                    PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
                    int k = pathchief.pointIndx(pnodes);
                    if(k < pathchief.points() - 1)
                    {
                        Plugin.builder.setSelected(pathchief.point(k + 1));
                        fillDialogWay();
                        Plugin.builder.repaint();
                    }
                    return true;
                } else
                {
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(wCur = new GWindowLabel(gwindowdialogclient, 15F, 1.0F, 4F, 1.6F, "1(1)", null));
        gwindowdialogclient.addLabel(wLTime = new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, Plugin.i18n("Time"), null));
        gwindowdialogclient.addLabel(wTime = new GWindowLabel(gwindowdialogclient, 9F, 3F, 6F, 1.3F, "0:00", null));
        gwindowdialogclient.addLabel(wLTimeOutH = new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, Plugin.i18n("TimeOut"), null));
        gwindowdialogclient.addControl(wTimeOutH = new GWindowEditControl(gwindowdialogclient, 9F, 5F, 2.0F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
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
        gwindowdialogclient.addLabel(wLTimeOutM = new GWindowLabel(gwindowdialogclient, 11.2F, 5F, 1.0F, 1.3F, ":", null));
        gwindowdialogclient.addControl(wTimeOutM = new GWindowEditControl(gwindowdialogclient, 11.5F, 5F, 2.0F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
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
        gwindowdialogclient.addLabel(wLSpeed0 = new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 7F, 1.3F, Plugin.i18n("Speed"), null));
        gwindowdialogclient.addLabel(wLSpeed1 = new GWindowLabel(gwindowdialogclient, 15F, 7F, 4F, 1.3F, Plugin.i18n("[kM/H]"), null));
        gwindowdialogclient.addControl(wSpeed = new GWindowEditControl(gwindowdialogclient, 9F, 7F, 5F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                PNodes pnodes = (PNodes)Plugin.builder.selectedPoint();
                String s = getValue();
                double d = 0.0D;
                try
                {
                    d = Double.parseDouble(s) / 3.6000000000000001D;
                }
                catch(Exception exception)
                {
                    setValue("" + (int)Math.round(((PNodes)Plugin.builder.selectedPoint()).speed * 3.6000000000000001D), false);
                    return false;
                }
                PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
                if(d > type[pathchief._iType].item[pathchief._iItem].speed)
                    d = type[pathchief._iType].item[pathchief._iItem].speed;
                pnodes.speed = d;
                pathchief.computeTimes();
                PlMission.setChanged();
                return false;
            }

        }
);
    }

    private void getTimeOut()
    {
        PNodes pnodes = (PNodes)Plugin.builder.selectedPoint();
        PathChief pathchief = (PathChief)Plugin.builder.selectedPath();
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
        pnodes.timeoutMin = d * 60D + d1;
        pathchief.computeTimes();
        PlMission.setChanged();
    }

    SectFile fSectsUnits;
    SectFile fSectsEmpty;
    Type type[];
    private Point3d p;
    private int roadOffset[];
    private int timeMinAbs[];
    private double roadSpeed;
    private PlMission pluginMission;
    private int startComboBox1;
    ViewItem viewType[];
    HashMapInt viewMap;
    private String _actorInfo[];
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabActor;
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabWay;
    GWindowLabel wName;
    GWindowComboControl wArmy;
    GWindowLabel wLSleepM;
    GWindowEditControl wSleepM;
    GWindowLabel wLSleepS;
    GWindowEditControl wSleepS;
    GWindowLabel wLSkill;
    GWindowComboControl wSkill;
    GWindowLabel wLSlowfire;
    GWindowEditControl wSlowfire;
    GWindowButton wPrev;
    GWindowButton wNext;
    GWindowLabel wCur;
    GWindowLabel wLTime;
    GWindowLabel wTime;
    GWindowLabel wLTimeOutH;
    GWindowEditControl wTimeOutH;
    GWindowLabel wLTimeOutM;
    GWindowEditControl wTimeOutM;
    GWindowLabel wLSpeed0;
    GWindowLabel wLSpeed1;
    GWindowEditControl wSpeed;

    static 
    {
        Property.set(com.maddox.il2.builder.PlMisChief.class, "name", "MisChief");
    }
}
