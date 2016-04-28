//////////////////////////////////////////////////////////////////////////////////////////
//   FMBFull MODded
//   4.111 MODded by PAL
//   lifted to 4.12 by SAS~Storebror
//   Change to Display Country and Airforce for the planes
//   and avoid of selecting Placeholder items
/////////////////////////////////////////////////////////////////////////////////////////

//By PAL, required, some changes not applicable for 4.101

package com.maddox.il2.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.GCaption;
import com.maddox.gwindow.GNotifyListener;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowLookAndFeel;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BmpUtils;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.GUIRenders;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.LightEnvXY;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.Renders;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.gui.GUIAirArming;
import com.maddox.il2.objects.ActorSimpleHMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.rts.Finger;
import com.maddox.rts.HomePath;
import com.maddox.rts.LDRres;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.util.HashMapInt;
import com.maddox.util.NumberTokenizer;
//By PAL, from 4.11

public class PlMisAir extends Plugin
{
	//+++ TODO: 4.12 changed code +++
    static class Country
    {

        public String name;
        public String i18nName;

        Country()
        {
        }
    }
    //--- TODO: 4.12 changed code ---
    	
    class _Render3D extends Render
    {

        public void preRender()
        {
            checkMesh(planeIndx);
            if(Actor.isValid(actorMesh[planeIndx]))
            {
                if(animateMeshA[planeIndx] != 0.0F || animateMeshT[planeIndx] != 0.0F)
                {
                    actorMesh[planeIndx].pos.getAbs(_orient);
                    _orient.set(_orient.azimut() + animateMeshA[planeIndx] * Main3D.cur3D().guiManager.root.deltaTimeSec, _orient.tangage() + animateMeshT[planeIndx] * Main3D.cur3D().guiManager.root.deltaTimeSec, 0.0F);
                    _orient.wrap360();
                    actorMesh[planeIndx].pos.setAbs(_orient);
                    actorMesh[planeIndx].pos.reset();
                }
                actorMesh[planeIndx].draw.preRender(actorMesh[planeIndx]);
            }
        }

        public void render()
        {
            if(Actor.isValid(actorMesh[planeIndx]))
            {
                Render.prepareStates();
                actorMesh[planeIndx].draw.render(actorMesh[planeIndx]);
            }
        }

        int planeIndx;

        public _Render3D(Renders renders1, float f)
        {
            super(renders1, f);
            planeIndx = _planeIndx;
            
            //+++ TODO: 4.12 change +++
            //setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            setClearColor(GWindowLookAndFeel.getUIBackgroundColor());
            //--- TODO: 4.12 change ---
      
            useClearStencil(true);
        }
    }

    class ViewItem extends GWindowMenuItem
    {

        public void execute()
        {
            bChecked = !bChecked;
            viewType(indx);
        }

        int indx;

        public ViewItem(int i, GWindowMenu gwindowmenu, String s, String s1)
        {
            super(gwindowmenu, s, s1);
            indx = i;
        }
    }

    public static class DefaultArmy
    {

        public void save(PathAir pathair)
        {
            iRegiment = pathair.iRegiment;
            iSquadron = pathair.iSquadron;
            iWing = pathair.iWing;
            
            //+++ TODO: 4.12 changed code +++
            sCountry = pathair.sCountry;
            //--- TODO: 4.12 changed code ---

        }

        public void load(PathAir pathair)
        {
            pathair.iRegiment = iRegiment;
            pathair.iSquadron = iSquadron;
            pathair.iWing = iWing;
            
            //+++ TODO: 4.12 changed code +++
            pathair.sCountry = sCountry;
            //--- TODO: 4.12 changed code ---
        }

        public int iRegiment;
        public int iSquadron;
        public int iWing;

        //+++ TODO: 4.12 changed code +++
        public String sCountry;
        //--- TODO: 4.12 changed code ---
        
        public DefaultArmy()
        {
        }
    }

    public static class Type
    {

        public String name;
        public Item item[];

        public Type(String s, Item aitem[])
        {
            name = s;
            item = aitem;
        }
    }

    public static class Item
    {

        public String name;
        public Class clazz;
        public int army;
        public boolean bEnablePlayer;
        public double speedMin;
        public double speedMax;
        public double speedRunway;

        public Item(String s, Class class1, int i)
        {
            speedMin = 200D;
            speedMax = 500D;
            speedRunway = 200D;
            name = s;
            clazz = class1;
            army = i;
            bEnablePlayer = Property.containsValue(class1, "cockpitClass");
            String s1 = Property.stringValue(class1, "FlightModel", null);
            if(s1 != null)
            {
                SectFile sectfile = FlightModelMain.sectFile(s1);
                speedMin = sectfile.get("Params", "Vmin", (float)speedMin);
                speedMax = sectfile.get("Params", "VmaxH", (float)speedMax);
                speedRunway = speedMin;
            }
        }
    }

    public PlMisAir()
    {
        defaultHeight = 500D;
        defaultSpeed = 300D;
        int i = Army.amountSingle();
        if(i < Army.amountNet())
            i = Army.amountNet();
        defaultArmy = new DefaultArmy[i];
        for(int j = 0; j < i; j++)
            defaultArmy[j] = new DefaultArmy();

        iArmyRegimentList = 0;
        
        //+++ TODO: 4.12 changed code +++
        sCountry = "";
        bSpawnFromStationary = false;
        //--- TODO: 4.12 changed code ---
        
        regimentList = new ArrayList();
        	branchList = new ArrayList(); //By PAL
        viewMap = new HashMapInt();
        _actorInfo = new String[2];
        tabSkin = new com.maddox.gwindow.GWindowTabDialogClient.Tab[4];
        _squads = new Object[4];
        _wings = new Object[4];
        wPlayer = new GWindowCheckBox[4];
        wSkills = new GWindowComboControl[4];
        wSkins = new GWindowComboControl[4];
        wNoseart = new GWindowComboControl[4];
        wPilots = new GWindowComboControl[4];
//By PAL, new from 4.11
        wSpawnPointSet = new GWindowButton[4];
        wSpawnPointClear = new GWindowButton[4];
        wSpawnPointLabel = new GWindowLabel[4];
                
        renders = new GUIRenders[4];
        camera3D = new Camera3D[4];
        render3D = new _Render3D[4];
        meshName = null;
        actorMesh = new ActorSimpleHMesh[4];
        _orient = new Orient();
    }

	//+++ TODO: 4.12 changed code +++
//    private void makeRegimentList(int i)
//    {
//        if(iArmyRegimentList == i)
//            return;
    private boolean makeRegimentList(int i, String s)
    {
        if(iArmyRegimentList == i && sCountry.equals(s))
            return false;
        initCountry();
        wCountry.clear(false);
        ArrayList arraylist = listCountry[i];
        for(int j0 = 0; j0 < arraylist.size(); j0++)
        {
            Country country = (Country)arraylist.get(j0);
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
        sCountry = s;
    	
    	
    	
        regimentList.clear();
        List list = Regiment.getAll();
        int j = list.size();
        
        String branchLast = ""; //By PAL
        branchList.clear(); //By PAL
        for(int k = 0; k < j; k++)
        {
            Regiment regiment = (Regiment)list.get(k);
            
            //+++ TODO: 4.12 changed code +++
//            if(regiment.getArmy() == i)      
            //--- TODO: 4.12 changed code ---
            
            if(regiment.getArmy() == i && regiment.branch().equals(s))
                regimentList.add(regiment);
 
            if(regiment.getArmy() == i) //By PAL
            {
	            String branch = regiment.branch(); //By PAL
	            if (branch.equalsIgnoreCase(branchLast)) continue;	            
            	branchList.add(branch);
            	branchLast = branch;		                        	
            }               
        }

        iArmyRegimentList = i;
        wRegiment.clear(false);
        wRegiment.setSelected(-1, false, false);            
        j = regimentList.size();
        if(wRegiment.posEnable == null || wRegiment.posEnable.length < j)
            wRegiment.posEnable = new boolean[j];
            
            
        //By PAL
//        ResourceBundle resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
        for(int l = 0; l < j; l++)
        {
            Regiment regiment1 = (Regiment)regimentList.get(l);
//            String s1 = I18N.regimentShort(regiment1.shortInfo());
            s = I18N.regimentShort(regiment1.shortInfo());
            if(s != null && s.length() > 0 && s.charAt(0) == '<')
                s = I18N.regimentInfo(regiment1.info());
                
            String ctry = regiment1.country(); //By PAL
            String desc = "";
//            try
//            {
//            	//desc = " - " + resCountry.getString(regiment1.branch());
//            }
//            catch(Exception e){}
                       	            
            wRegiment.add("[ " + ctry + desc + " ] : " + s);  //By PAL
            
            wRegiment.posEnable[l] = true;
        }
        
//        wBranch.clear(false); //BY PAL
//        wBranch.setSelected(-1, false, false);   //By PAL
//        j = branchList.size(); //By PAL, previously created
//        for(int l = 0; l < j; l++)
//        {        
//            try
//            {
//            	String s2 = resCountry.getString((String)branchList.get(l));
//            	wBranch.add(s2);            	
//            }        
//        	catch(Exception e){}
//        }
        
        //+++ TODO: 4.12 changed code +++
        wRegiment.setSelected(0, true, false);
        return true;
        //--- TODO: 4.12 changed code ---
    }
/*
    public void load(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("Wing");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        Point3d point3d = new Point3d();
        for(int k = 0; k < j; k++)
        {
            String s = sectfile.var(i, k);
            String s1 = s + "_Way";
            if(!sectfile.sectionExist(s))
            {
                builder.tipErr("MissionLoad: Section '" + s + "' not found");
                continue;
            }
            int l = sectfile.sectionIndex(s1);
            if(l < 0)
            {
                builder.tipErr("MissionLoad: Section '" + s1 + "' not found");
                continue;
            }
            int i1 = sectfile.vars(l);
            if(i1 == 0)
            {
                builder.tipErr("MissionLoad: Section '" + s1 + "' is empty");
                continue;
            }
            String s2 = sectfile.get(s, "Class", (String)null);
            if(s2 == null)
            {
                builder.tipErr("MissionLoad: In section '" + s + "' field 'Class' not present");
                continue;
            }
            Class class1 = null;
            try
            {
                class1 = ObjIO.classForName(s2);
            }
            catch(Exception exception)
            {
                builder.tipErr("MissionLoad: In section '" + s + "' field 'Class' contains unknown class");
                continue;
            }
            int j1 = 0;
            int k1 = 0;
            j1 = 0;
            do
            {
                if(j1 >= type.length)
                    break;
                for(k1 = 0; k1 < type[j1].item.length && type[j1].item[k1].clazz != class1; k1++);
                if(k1 < type[j1].item.length)
                    break;
                j1++;
            } while(true);
            if(j1 >= type.length)
            {
                builder.tipErr("MissionLoad: In section '" + s + "' field 'Class' contains unknown class");
                continue;
            }
            boolean flag = sectfile.get(s, "OnlyAI", 0, 0, 1) == 1;
            boolean flag1 = sectfile.get(s, "Parachute", 1, 0, 1) == 1;
            int l1 = sectfile.get(s, "Fuel", 100, 0, 100);
            int i2 = sectfile.get(s, "Planes", 1, 1, 4);
            int j2 = -1;
            if(sectfile.exist(s, "Skill"))
                j2 = sectfile.get(s, "Skill", 1, 0, 3);
            int ai[] = new int[4];
            for(int k2 = 0; k2 < 4; k2++)
                if(sectfile.exist(s, "Skill" + k2))
                    ai[k2] = sectfile.get(s, "Skill" + k2, 1, 0, 3);
                else
                    ai[k2] = j2 != -1 ? j2 : 1;

            String as[] = new String[4];
            for(int l2 = 0; l2 < 4; l2++)
                as[l2] = sectfile.get(s, "skin" + l2, (String)null);

            String as1[] = new String[4];
            for(int i3 = 0; i3 < 4; i3++)
                as1[i3] = sectfile.get(s, "noseart" + i3, (String)null);

            String as2[] = new String[4];
            for(int j3 = 0; j3 < 4; j3++)
                as2[j3] = sectfile.get(s, "pilot" + j3, (String)null);

            boolean aflag[] = new boolean[4];
            for(int k3 = 0; k3 < 4; k3++)
                aflag[k3] = sectfile.get(s, "numberOn" + k3, 1, 0, 1) == 1;

            String s3 = s.substring(0, s.length() - 2);
            Regiment regiment = (Regiment)Actor.getByName(s3);
            int l3 = 0;
            int i4 = 0;
            int j4 = 0;
            if(regiment != null)
            {
                makeRegimentList(regiment.getArmy());
                l3 = regimentList.indexOf(regiment);
                if(l3 >= 0)
                {
                    j4 = Character.getNumericValue(s.charAt(s.length() - 1)) - Character.getNumericValue('0');
                    i4 = Character.getNumericValue(s.charAt(s.length() - 2)) - Character.getNumericValue('0');
                    if(j4 < 0)
                        j4 = 0;
                    if(j4 > 3)
                        j4 = 3;
                    if(i4 < 0)
                        i4 = 0;
                    if(i4 > 3)
                        i4 = 3;
//By PAL, review:                        
int br = branchList.indexOf(regiment.branch()); //By PAL, review if OK
wBranch.setSelected(br, true, false);
                        
                } else
                {
                    regiment = null;
                }
            }
            if(regiment == null)
            {
                int k4 = sectfile.get(s, "Army", 0);
                if(k4 < 1 || k4 >= Builder.armyAmount())
                    k4 = 1;
                makeRegimentList(k4);
                regiment = (Regiment)regimentList.get(0);
                s3 = regiment.name();
            }
            String s4 = sectfile.get(s, "weapons", (String)null);
            int l4 = sectfile.get(s, "StartTime", 0);
            if(l4 < 0)
                l4 = 0;
//By PAL, from 4.11
            String as3[] = new String[4];
            for(int i5 = 0; i5 < 4; i5++)
                as3[i5] = sectfile.get(s, "spawn" + i5, (String)null);
                
            PathAir pathair = new PathAir(builder.pathes, j1, k1);
            pathair.setArmy(regiment.getArmy());
            pathair.sRegiment = s3;
            pathair.iRegiment = l3;
            pathair.iSquadron = i4;
            pathair.iWing = j4;
            pathair.fuel = l1;
            pathair.bOnlyAI = flag;
            pathair.bParachute = flag1;
            pathair.planes = i2;
            pathair.skill = j2;
            pathair.skills = ai;
            pathair.skins = as;
            pathair.noseart = as1;
            pathair.pilots = as2;
            pathair.bNumberOn = aflag;
            pathair.weapons = s4;
//By PAL, from 4.11
            pathair.weapons = s4;
            pathair.setSpawnPointPlaneName(0, as3[0]);
            pathair.setSpawnPointPlaneName(1, as3[1]);
            pathair.setSpawnPointPlaneName(2, as3[2]);
            pathair.setSpawnPointPlaneName(3, as3[3]);
                        
            if(!searchEnabledSlots(pathair))
            {
                pathair.destroy();
                builder.tipErr("MissionLoad: Section '" + s + "', regiment table very small");
                continue;
            }
            pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
            Property.set(pathair, "builderPlugin", this);
            pathair.drawing(viewMap.containsKey(j1));
            Object obj = null;
            for(int i5 = 0; i5 < i1; i5++)
            {
                String s5 = sectfile.var(l, i5);
                byte byte0 = -1;
                if("NORMFLY".equals(s5))
                    byte0 = 0;
                else
                if("TAKEOFF".equals(s5))
                    byte0 = 1;
                else
                if("LANDING".equals(s5))
                    byte0 = 2;
                else
                if("GATTACK".equals(s5))
                {
                    byte0 = 3;
                } else
                {
                    builder.tipErr("MissionLoad: Section '" + s1 + "' contains unknown type waypoint");
                    pathair.destroy();
                    continue;
                }
                String s6 = sectfile.value(l, i5);
                if(s6 == null || s6.length() <= 0)
                {
                    builder.tipErr("MissionLoad: Section '" + s1 + "' type '" + s5 + "' is empty");
                    pathair.destroy();
                    continue;
                }
                NumberTokenizer numbertokenizer = new NumberTokenizer(s6);
                point3d.x = numbertokenizer.next(-1E+030D, -1E+030D, 1E+030D);
                point3d.y = numbertokenizer.next(-1E+030D, -1E+030D, 1E+030D);
                double d = numbertokenizer.next(0.0D, 0.0D, 10000D);
                double d1 = numbertokenizer.next(0.0D, 0.0D, 1000D);
                String s7 = null;
                int j5 = 0;
                String s8 = null;
                s7 = numbertokenizer.next(null);
                if(s7 != null)
                    if(s7.equals("&0"))
                    {
                        s8 = s7;
                        s7 = null;
                    } else
                    if(s7.equals("&1"))
                    {
                        s8 = s7;
                        s7 = null;
                    } else
                    {
                        j5 = numbertokenizer.next(0);
                        s8 = numbertokenizer.next(null);
                    }
                switch(byte0)
                {
                default:
                    break;

                case 0: // '\0'
                    byte0 = 0;
                    break;

                case 3: // '\003'
                    if(s7 != null && s7.startsWith("Bridge"))
                        s7 = " " + s7;
                    byte0 = 3;
                    break;

                case 1: // '\001'
                    byte0 = 1;
                    break;

                case 2: // '\002'
                    byte0 = 2;
                    break;
                }
                obj = new PAir(pathair, ((PPoint) (obj)), point3d, byte0, d, d1);
                if(s7 != null)
                {
                    ((PAir)obj).iTarget = j5; //By PAL ((PAir)obj)
                    ((PAir)obj).sTarget = s7; //By PAL ((PAir)obj)
                }
                if(s8 != null && s8.equals("&1"))
                    ((PAir)obj).bRadioSilence = true; //By PAL ((PAir)obj)
            }

            if(l4 > 0)
            {
                PAir pair = (PAir)pathair.point(0);
                pair.time = (double)l4 * 60D;
                pathair.computeTimes(false);
            }
        }

    }*/

//By PAL, new Load from 4.11
    public void load(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("Wing");
        if(i < 0)
            return;
        int j = sectfile.vars(i);
        Point3d point3d = new Point3d();
        for(int k = 0; k < j; k++)
        {
            String s = sectfile.var(i, k);
            String s1 = s + "_Way";
            if(!sectfile.sectionExist(s))
            {
                builder.tipErr("MissionLoad: Section '" + s + "' not found");
                continue;
            }
            int l = sectfile.sectionIndex(s1);
            if(l < 0)
            {
                builder.tipErr("MissionLoad: Section '" + s1 + "' not found");
                continue;
            }
            int i1 = sectfile.vars(l);
            if(i1 == 0)
            {
                builder.tipErr("MissionLoad: Section '" + s1 + "' is empty");
                continue;
            }
            String s2 = sectfile.get(s, "Class", (String)null);
            if(s2 == null)
            {
                builder.tipErr("MissionLoad: In section '" + s + "' field 'Class' not present");
                continue;
            }
            Class class1 = null;
            try
            {
                class1 = ObjIO.classForName(s2);
            }
            catch(Exception exception)
            {
                builder.tipErr("MissionLoad: In section '" + s + "' field 'Class' contains unknown class");
                continue;
            }
            int j1 = 0;
            int k1 = 0;
            j1 = 0;
            do
            {
                if(j1 >= type.length)
                    break;
                for(k1 = 0; k1 < type[j1].item.length && type[j1].item[k1].clazz != class1; k1++);
                if(k1 < type[j1].item.length)
                    break;
                j1++;
            } while(true);
            if(j1 >= type.length)
            {
                builder.tipErr("MissionLoad: In section '" + s + "' field 'Class' contains unknown class");
                continue;
            }
            boolean flag = sectfile.get(s, "OnlyAI", 0, 0, 1) == 1;
            boolean flag1 = sectfile.get(s, "Parachute", 1, 0, 1) == 1;
            int l1 = sectfile.get(s, "Fuel", 100, 0, 100);
            int i2 = sectfile.get(s, "Planes", 1, 1, 4);
            int j2 = -1;
            if(sectfile.exist(s, "Skill"))
                j2 = sectfile.get(s, "Skill", 1, 0, 3);
            int ai[] = new int[4];
            for(int k2 = 0; k2 < 4; k2++)
                if(sectfile.exist(s, "Skill" + k2))
                    ai[k2] = sectfile.get(s, "Skill" + k2, 1, 0, 3);
                else
                    ai[k2] = j2 != -1 ? j2 : 1;

            String as[] = new String[4];
            for(int l2 = 0; l2 < 4; l2++)
                as[l2] = sectfile.get(s, "skin" + l2, (String)null);

            String as1[] = new String[4];
            for(int i3 = 0; i3 < 4; i3++)
                as1[i3] = sectfile.get(s, "noseart" + i3, (String)null);

            String as2[] = new String[4];
            for(int j3 = 0; j3 < 4; j3++)
                as2[j3] = sectfile.get(s, "pilot" + j3, (String)null);

            boolean aflag[] = new boolean[4];
            for(int k3 = 0; k3 < 4; k3++)
                aflag[k3] = sectfile.get(s, "numberOn" + k3, 1, 0, 1) == 1;

            String s3 = s.substring(0, s.length() - 2);
            Regiment regiment = (Regiment)Actor.getByName(s3);
            int l3 = 0;
            int i4 = 0;
            int j4 = 0;
            if(regiment != null)
            {
            	
            	//+++ TODO: 4.12 changed code +++
//                makeRegimentList(regiment.getArmy());
                makeRegimentList(regiment.getArmy(), regiment.branch());
                //--- TODO: 4.12 changed code ---
                
                l3 = regimentList.indexOf(regiment);
                if(l3 >= 0)
                {
                    j4 = Character.getNumericValue(s.charAt(s.length() - 1)) - Character.getNumericValue('0');
                    i4 = Character.getNumericValue(s.charAt(s.length() - 2)) - Character.getNumericValue('0');
                    if(j4 < 0)
                        j4 = 0;
                    if(j4 > 3)
                        j4 = 3;
                    if(i4 < 0)
                        i4 = 0;
                    if(i4 > 3)
                        i4 = 3;
//By PAL, review:                        
//int br = branchList.indexOf(regiment.branch()); //By PAL, review if OK
//wBranch.setSelected(br, true, false);                        
                } else
                {
                    regiment = null;
                }
            }
            if(regiment == null)
            {
                int k4 = sectfile.get(s, "Army", 0);
                if(k4 < 1 || k4 >= Builder.armyAmount())
                    k4 = 1;
                
                //+++ TODO: 4.12 changed code +++
//                makeRegimentList(k4);
                makeRegimentList(k4, null);
                //--- TODO: 4.12 changed code ---
                
                regiment = (Regiment)regimentList.get(0);
                s3 = regiment.name();
            }
            String s4 = sectfile.get(s, "weapons", (String)null);
            int l4 = sectfile.get(s, "StartTime", 0);
            if(l4 < 0)
                l4 = 0;
            String as3[] = new String[4];
            for(int i5 = 0; i5 < 4; i5++)
                as3[i5] = sectfile.get(s, "spawn" + i5, (String)null);

            PathAir pathair = new PathAir(builder.pathes, j1, k1);
            pathair.setArmy(regiment.getArmy());
            pathair.sRegiment = s3;
            pathair.iRegiment = l3;
            
            //+++ TODO: 4.12 changed code +++
            pathair.sCountry = regiment.branch();
            //--- TODO: 4.12 changed code ---
            
            pathair.iSquadron = i4;
            pathair.iWing = j4;
            pathair.fuel = l1;
            pathair.bOnlyAI = flag;
            pathair.bParachute = flag1;
            pathair.planes = i2;
            pathair.skill = j2;
            pathair.skills = ai;
            pathair.skins = as;
            pathair.noseart = as1;
            pathair.pilots = as2;
            pathair.bNumberOn = aflag;
            pathair.weapons = s4;
            pathair.setSpawnPointPlaneName(0, as3[0]);
            pathair.setSpawnPointPlaneName(1, as3[1]);
            pathair.setSpawnPointPlaneName(2, as3[2]);
            pathair.setSpawnPointPlaneName(3, as3[3]);
            if(!searchEnabledSlots(pathair))
            {
                pathair.destroy();
                builder.tipErr("MissionLoad: Section '" + s + "', regiment table very small");
                continue;
            }
            pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
            Property.set(pathair, "builderPlugin", this);
            pathair.drawing(viewMap.containsKey(j1));
            PAir pair = null;
            for(int j5 = 0; j5 < i1; j5++)
            {
                String s5 = sectfile.var(l, j5);
                byte byte0 = -1;
                int k5 = 0;
                if(s5.startsWith("NORMFLY"))
                {
                    byte0 = 0;
                    if(s5.endsWith("_401"))
                        k5 = 401;
                    else
                    if(s5.endsWith("_402"))
                        k5 = 402;
                    else
                    if(s5.endsWith("_403"))
                        k5 = 403;
                    else
                    if(s5.endsWith("_404"))
                        k5 = 404;
                    else
                    if(s5.endsWith("_405"))
                        k5 = 405;
                    
                    //+++ TODO: 4.12 changed code +++
                    else
                    if(s5.endsWith("_407"))
                        k5 = 407;
                    //--- TODO: 4.12 changed code ---
                    
                } else
                if(s5.startsWith("TAKEOFF"))
                {
                    byte0 = 1;
                    if(s5.endsWith("_001"))
                        k5 = 1;
                    else
                    if(s5.endsWith("_002"))
                        k5 = 2;
                    else
                    if(s5.endsWith("_003"))
                        k5 = 3;
                    
                    //+++ TODO: 4.12 changed code +++
                    else
                    if(s5.endsWith("_004"))
                        k5 = 4;
                    else
                    if(s5.endsWith("_005"))
                        k5 = 4;
                    //--- TODO: 4.12 changed code ---
                    
                } else
                if(s5.startsWith("LANDING"))
                {
                    byte0 = 2;
                    if(s5.endsWith("_101"))
                        k5 = 101;
                    else
                    if(s5.endsWith("_102"))
                        k5 = 102;
                    else
                    if(s5.endsWith("_103"))
                        k5 = 103;
                    else
                    if(s5.endsWith("_104"))
                        k5 = 104;
                } else
                if(s5.startsWith("GATTACK"))
                {
                    byte0 = 3;
                } else
                {
                    builder.tipErr("MissionLoad: Section '" + s1 + "' contains unknown type waypoint");
                    pathair.destroy();
                    continue;
                }
                String s6 = sectfile.value(l, j5);
                if(s6 == null || s6.length() <= 0)
                {
                    builder.tipErr("MissionLoad: Section '" + s1 + "' type '" + s5 + "' is empty");
                    pathair.destroy();
                    continue;
                }
                NumberTokenizer numbertokenizer = new NumberTokenizer(s6);
                point3d.x = numbertokenizer.next(-1E+030D, -1E+030D, 1E+030D);
                point3d.y = numbertokenizer.next(-1E+030D, -1E+030D, 1E+030D);
                double d = numbertokenizer.next(0.0D, 0.0D, 10000D);
                double d1 = numbertokenizer.next(0.0D, 0.0D, 1000D);
                String s7 = null;
                int l5 = 0;
                String s8 = null;
                s7 = numbertokenizer.next(null);
                if(s7 != null)
                    if(s7.equals("&0"))
                    {
                        s8 = s7;
                        s7 = null;
                    } else
                    if(s7.equals("&1"))
                    {
                        s8 = s7;
                        s7 = null;
                    } else
                    {
                        l5 = numbertokenizer.next(0);
                        s8 = numbertokenizer.next(null);
                    }
                
                //+++ TODO: 4.12 changed code +++
                String s90 = null;
                s90 = numbertokenizer.next(null);
                if(s90 != null && s90.startsWith("F"))
                    s90 = s90.substring(1);
                //--- TODO: 4.12 changed code ---
                
                switch(byte0)
                {
                default:
                    break;

                case 0: // '\0'
                    byte0 = 0;
                    break;

                case 3: // '\003'
                    if(s7 != null && s7.startsWith("Bridge"))
                        s7 = " " + s7;
                    byte0 = 3;
                    break;

                case 1: // '\001'
                    byte0 = 1;
                    break;

                case 2: // '\002'
                    byte0 = 2;
                    break;
                }
                
                //+++ TODO: 4.12 changed code +++
//                obj = new PAir(pathair, ((PPoint) (obj)), point3d, byte0, d, d1);
                pair = new PAir(pathair, ((PPoint) (pair)), point3d, byte0, d, d1, null);
                //--- TODO: 4.12 changed code ---
                
                pair.waypointType = k5;
                if(k5 > 0 && byte0 != 2)
                {
                    String s9 = sectfile.value(l, j5 + 1);
                    NumberTokenizer numbertokenizer1 = new NumberTokenizer(s9);
                    switch(k5)
                    {
                    case 401: 
                    case 402: 
                    case 403: 
                    case 404: 
                    case 405: 
                    	pair.cycles = numbertokenizer1.next(0);
                    	pair.delayTimer = numbertokenizer1.next(0);
                    	pair.orient = numbertokenizer1.next(0);
                    	pair.baseSize = numbertokenizer1.next(0);
                    	pair.altDifference = numbertokenizer1.next(0);
                        break;

                        //+++ TODO: 4.12 changed code +++
                    case 407: 
                    	pair.cycles = numbertokenizer1.next(0);
                    	pair.delayTimer = numbertokenizer1.next(0);
                    	pair.orient = numbertokenizer1.next(0);
                    	pair.baseSize = numbertokenizer1.next(0);
                        break;
                        //--- TODO: 4.12 changed code ---

                    case 1: // '\001'
                    case 2: // '\002'
                    case 3: // '\003'
                    	
                    	//+++ TODO: 4.12 changed code +++
                    case 4: // '\004'
                    case 5: // '\005'
                    	//--- TODO: 4.12 changed code ---
                    	
                    	pair.targetTrigger = numbertokenizer1.next(0);
                    	pair.delayTimer = numbertokenizer1.next(0);
                    	pair.takeoffSpacing = numbertokenizer1.next(0);
                    	pair.ignoreAlt = numbertokenizer1.next(0);
                        break;
                    }
                    j5++;
                }
//By PAL, from 4.111
                if(s7 != null)
                {
                	pair.iTarget = l5;
                	pair.sTarget = s7;
                }
                if(s8 != null && s8.equals("&1"))
                	pair.bRadioSilence = true;
                
                //+++ TODO: 4.12 changed code +++
                if(s90 != null)
                	pair.formation = Integer.parseInt(s90);
                //--- TODO: 4.12 changed code ---
                
            }

            if(l4 > 0)
            {
                pair = (PAir)pathair.point(0);
                pair.time = (double)l4 * 60D;
                pathair.computeTimes(false);
            }
        }

    }
    
/*Old Save
    public boolean save(SectFile sectfile)
    {
        if(builder.pathes == null)
            return true;
        int i = sectfile.sectionIndex("Wing");
        Object aobj[] = builder.pathes.getOwnerAttached();
label0:
        for(int j = 0; j < aobj.length; j++)
        {
            Actor actor = (Actor)aobj[j];
            if(actor == null)
                break;
            if(!(actor instanceof PathAir))
                continue;
            PathAir pathair = (PathAir)actor;
            int k = pathair.points();
            if(i <= -1)
                i = sectfile.sectionAdd("Wing");
            String s = pathair.sRegiment + pathair.iSquadron + pathair.iWing;
            String s1 = s + "_Way";
            sectfile.lineAdd(i, s, "");
            int l = sectfile.sectionAdd(s);
            sectfile.lineAdd(l, "Planes", "" + pathair.planes);
            if(pathair.bOnlyAI)
                sectfile.lineAdd(l, "OnlyAI", "1");
            if(!pathair.bParachute)
                sectfile.lineAdd(l, "Parachute", "0");
            if(pathair.skill != -1)
                sectfile.lineAdd(l, "Skill", "" + pathair.skill);
            for(int i1 = 0; i1 < 4; i1++)
                if(pathair.skill != pathair.skills[i1])
                    sectfile.lineAdd(l, "Skill" + i1, "" + pathair.skills[i1]);

            for(int j1 = 0; j1 < 4; j1++)
                if(pathair.skins[j1] != null)
                    sectfile.lineAdd(l, "skin" + j1, pathair.skins[j1]);

            for(int k1 = 0; k1 < 4; k1++)
                if(pathair.noseart[k1] != null)
                    sectfile.lineAdd(l, "noseart" + k1, pathair.noseart[k1]);

            for(int l1 = 0; l1 < 4; l1++)
                if(pathair.pilots[l1] != null)
                    sectfile.lineAdd(l, "pilot" + l1, pathair.pilots[l1]);

            for(int i2 = 0; i2 < 4; i2++)
                if(!pathair.bNumberOn[i2])
                    sectfile.lineAdd(l, "numberOn" + i2, "0");

            sectfile.lineAdd(l, "Class", ObjIO.classGetName(type[pathair._iType].item[pathair._iItem].clazz));
            sectfile.lineAdd(l, "Fuel", "" + pathair.fuel);
            if(pathair.weapons != null)
                sectfile.lineAdd(l, "weapons", "" + pathair.weapons);
            else
                sectfile.lineAdd(l, "weapons", "none");
            PAir pair = (PAir)pathair.point(0);
            if(pair.time > 0.0D)
                sectfile.lineAdd(l, "StartTime", "" + (int)Math.round(pair.time / 60D));
            int j2 = sectfile.sectionAdd(s1);
            int k2 = 0;
            do
            {
                if(k2 >= k)
                    continue label0;
                PAir pair1 = (PAir)pathair.point(k2);
                Point3d point3d = pair1.pos.getAbsPoint();
                switch(pair1.type())
                {
                case 0: // '\0'
                    sectfile.lineAdd(j2, "NORMFLY", fmt(point3d.x) + " " + fmt(point3d.y) + " " + fmt(pair1.height) + " " + fmt(pair1.speed) + saveTarget(pair1) + saveRadioSilence(pair1));
                    break;

                case 3: // '\003'
                    sectfile.lineAdd(j2, "GATTACK", fmt(point3d.x) + " " + fmt(point3d.y) + " " + fmt(pair1.height) + " " + fmt(pair1.speed) + saveTarget(pair1) + saveRadioSilence(pair1));
                    break;

                case 1: // '\001'
                    sectfile.lineAdd(j2, "TAKEOFF", fmt(point3d.x) + " " + fmt(point3d.y) + " 0 0" + saveTarget(pair1) + saveRadioSilence(pair1));
                    break;

                case 2: // '\002'
                    sectfile.lineAdd(j2, "LANDING", fmt(point3d.x) + " " + fmt(point3d.y) + " 0 0" + saveTarget(pair1) + saveRadioSilence(pair1));
                    break;
                }
                k2++;
            } while(true);
        }

        return true;
    }*/
    
//By PAL, all Save from 4.111
    public boolean save(SectFile sectfile)
    {
        if(builder.pathes == null)
            return true;
        int i = sectfile.sectionIndex("Wing");
        Object aobj[] = builder.pathes.getOwnerAttached();
label0:
        for(int j = 0; j < aobj.length; j++)
        {
            Actor actor = (Actor)aobj[j];
            if(actor == null)
                break;
            if(!(actor instanceof PathAir))
                continue;
            PathAir pathair = (PathAir)actor;
            int k = pathair.points();
            if(i <= -1)
                i = sectfile.sectionAdd("Wing");
            String s = pathair.sRegiment + pathair.iSquadron + pathair.iWing;
            String s1 = s + "_Way";
            sectfile.lineAdd(i, s, "");
            int l = sectfile.sectionAdd(s);
            sectfile.lineAdd(l, "Planes", "" + pathair.planes);
            if(pathair.bOnlyAI)
                sectfile.lineAdd(l, "OnlyAI", "1");
            if(!pathair.bParachute)
                sectfile.lineAdd(l, "Parachute", "0");
            if(pathair.skill != -1)
                sectfile.lineAdd(l, "Skill", "" + pathair.skill);
            for(int i1 = 0; i1 < 4; i1++)
                if(pathair.skill != pathair.skills[i1])
                    sectfile.lineAdd(l, "Skill" + i1, "" + pathair.skills[i1]);

            for(int j1 = 0; j1 < 4; j1++)
                if(pathair.skins[j1] != null)
                    sectfile.lineAdd(l, "skin" + j1, pathair.skins[j1]);

            for(int k1 = 0; k1 < 4; k1++)
                if(pathair.noseart[k1] != null)
                    sectfile.lineAdd(l, "noseart" + k1, pathair.noseart[k1]);

            for(int l1 = 0; l1 < 4; l1++)
                if(pathair.pilots[l1] != null)
                    sectfile.lineAdd(l, "pilot" + l1, pathair.pilots[l1]);

            for(int i2 = 0; i2 < 4; i2++)
                if(!pathair.bNumberOn[i2])
                    sectfile.lineAdd(l, "numberOn" + i2, "0");

            sectfile.lineAdd(l, "Class", ObjIO.classGetName(type[pathair._iType].item[pathair._iItem].clazz));
            sectfile.lineAdd(l, "Fuel", "" + pathair.fuel);
            if(pathair.weapons != null)
                sectfile.lineAdd(l, "weapons", "" + pathair.weapons);
            else
                sectfile.lineAdd(l, "weapons", "none");
            PAir pair = (PAir)pathair.point(0);
            if(pair.time > 0.0D)
                sectfile.lineAdd(l, "StartTime", "" + (int)Math.round(pair.time / 60D));
            
            //+++ TODO: 4.12 changed code +++
//            for(int j2 = 0; j2 < 4; j2++)
//                if(pathair.getSpawnPoint(j2) != null)
//                    sectfile.lineAdd(l, "spawn" + j2, pathair.getSpawnPoint(j2).name());
            boolean flag = true;
            for(int j2 = 0; j2 < 4; j2++)
            {
                if(pathair.getSpawnPoint(j2) != null)
                {
                    sectfile.lineAdd(l, "spawn" + j2, pathair.getSpawnPoint(j2).name());
                    continue;
                }
                if(j2 < pathair.planes)
                    flag = false;
            }
            //--- TODO: 4.12 changed code ---

            int k2 = sectfile.sectionAdd(s1);
            int l2 = 0;
            do
            {
                if(l2 >= k)
                    continue label0;
                PAir pair1 = (PAir)pathair.point(l2);
                Point3d point3d = pair1.pos.getAbsPoint();
                switch(pair1.type())
                {
                default:
                    break;

                case 0: // '\0'
                    String s2 = "";
                    if(pair1.waypointType > 0)
                        s2 = "_" + pair1.waypointType;
                    
                    //+++ TODO: 4.12 changed code +++
//                    sectfile.lineAdd(k2, "NORMFLY" + s2, fmt(point3d.x) + " " + fmt(point3d.y) + " " + fmt(pair1.height) + " " + fmt(pair1.speed) + saveTarget(pair1) + saveRadioSilence(pair1));
                    sectfile.lineAdd(k2, "NORMFLY" + s2, fmt(point3d.x) + " " + fmt(point3d.y) + " " + fmt(pair1.height) + " " + fmt(pair1.speed) + saveTarget(pair1) + saveRadioSilence(pair1) + saveFormation(pair1));
                    //--- TODO: 4.12 changed code ---
                    
                    if(pair1.waypointType > 400)
                        sectfile.lineAdd(k2, "TRIGGERS", pair1.cycles + " " + pair1.delayTimer + " " + (int)pair1.orient + " " + pair1.baseSize + " " + pair1.altDifference);
                    break;

                case 3: // '\003'
                    String s3 = "";
                    if(pair1.waypointType > 0)
                        s3 = "_" + pair1.waypointType;
                    
                    //+++ TODO: 4.12 changed code +++
//                    sectfile.lineAdd(k2, "GATTACK" + s3, fmt(point3d.x) + " " + fmt(point3d.y) + " " + fmt(pair1.height) + " " + fmt(pair1.speed) + saveTarget(pair1) + saveRadioSilence(pair1));
                    sectfile.lineAdd(k2, "GATTACK" + s3, fmt(point3d.x) + " " + fmt(point3d.y) + " " + fmt(pair1.height) + " " + fmt(pair1.speed) + saveTarget(pair1) + saveRadioSilence(pair1) + saveFormation(pair1));
                    //--- TODO: 4.12 changed code ---
                    
                    if(pair1.waypointType > 200)
                        sectfile.lineAdd(k2, "TRIGGERS", pair1.cycles + " " + pair1.delayTimer + " " + (int)pair1.orient + " " + pair1.baseSize + " " + pair1.targetTrigger + " " + pair1.altDifference);
                    break;

                case 1: // '\001'
                    String s4 = "";
                    if(pair1.waypointType > 0)
                    
                    //+++ TODO: 4.12 changed code +++
//                        s4 = "_00" + pair1.waypointType;
                    {
                        int i3 = pair1.waypointType;
                        if(i3 == 4 && flag)
                            i3 = 5;
                        s4 = "_00" + i3;
                    }
                    //--- TODO: 4.12 changed code ---
                    
                    sectfile.lineAdd(k2, "TAKEOFF" + s4, fmt(point3d.x) + " " + fmt(point3d.y) + " 0 0" + saveTarget(pair1) + saveRadioSilence(pair1));
                    if(pair1.waypointType > 0)
                        sectfile.lineAdd(k2, "TRIGGERS", pair1.targetTrigger + " " + pair1.delayTimer + " " + pair1.takeoffSpacing + " " + pair1.ignoreAlt);
                    break;

                case 2: // '\002'
                    String s5 = "";
                    if(pair1.waypointType > 0)
                        s5 = "_" + pair1.waypointType;
                    sectfile.lineAdd(k2, "LANDING" + s5, fmt(point3d.x) + " " + fmt(point3d.y) + " 0 0" + saveTarget(pair1) + saveRadioSilence(pair1));
                    break;
                }
                l2++;
            } while(true);
        }

        return true;
    }    

    private String fmt(double d)
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

    private String saveTarget(PAir pair)
    {
        if(!Actor.isValid(pair.getTarget()))
            return "";
        if(pair.getTarget() instanceof PPoint)
        {
            PPoint ppoint = (PPoint)pair.getTarget();
            Path path = (Path)ppoint.getOwner();
            return " " + path.name() + " " + path.pointIndx(ppoint);
        } else
        {
            return " " + pair.getTarget().name() + " 0";
        }
    }

    private String saveRadioSilence(PAir pair)
    {
        return " " + (pair.bRadioSilence ? "&1" : "&0");
    }

    //+++ TODO: 4.12 changed code +++
    private String saveFormation(PAir pair)
    {
        if(pair.formation == 0)
            return "";
        else
            return " F" + pair.formation;
    }
    //--- TODO: 4.12 changed code ---

    private void clampSpeed(PAir pair)
    {
        PathAir pathair = (PathAir)pair.getOwner();
        if(pair.type() == 0 || pair.type() == 3)
        {
            if(pair.speed < type[pathair._iType].item[pathair._iItem].speedMin)
                pair.speed = type[pathair._iType].item[pathair._iItem].speedMin;
            if(pair.speed > type[pathair._iType].item[pathair._iItem].speedMax)
                pair.speed = type[pathair._iType].item[pathair._iItem].speedMax;
        } else
        {
            pair.speed = 0.0D;
        }
    }

    private void clampSpeed(PathAir pathair)
    {
        int i = pathair.points();
        for(int j = 0; j < i; j++)
            clampSpeed((PAir)pathair.point(j));

    }
    
//By PAL, from 4.1111 DJ-Decompiler
/*    public void insert(Loc loc, boolean flag)
    {
        PathAir pathair = null;
        Point3d point3d;
        int i;
        int j;
        Path path;
        point3d = loc.getPoint();
        i = builder.wSelect.comboBox1.getSelected();
        j = builder.wSelect.comboBox2.getSelected();
        if(builder.selectedPath() == null)
            break MISSING_BLOCK_LABEL_98;
        path = builder.selectedPath();
        if(!(path instanceof PathAir))
            return;
        pathair = (PathAir)path;
        if(i - startComboBox1 != pathair._iType || j != pathair._iItem)
            builder.setSelected(null);
        if(builder.selectedPoint() == null)
            break MISSING_BLOCK_LABEL_163;
        PAir pair1 = (PAir)builder.selectedPoint();
        if(pair1.type() == 2)
            return;
        PAir pair;
        pair = new PAir(builder.selectedPath(), builder.selectedPoint(), point3d, 0, defaultHeight, defaultSpeed);
        break MISSING_BLOCK_LABEL_372;
        if(i < startComboBox1 || i >= startComboBox1 + type.length)
            return;
        i -= startComboBox1;
        if(j < 0 || j >= type[i].item.length)
            return;
        pathair = new PathAir(builder.pathes, i, j);
        pathair.setArmy(type[i].item[j].army);
        defaultArmy[type[i].item[j].army].load(pathair);
        if(!searchEnabledSlots(pathair))
        {
            pathair.destroy();
            return;
        }
        pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
        Property.set(pathair, "builderPlugin", this);
        pathair.drawing(viewMap.containsKey(i));
        pair = new PAir(pathair, null, point3d, 0, defaultHeight, defaultSpeed);
        clampSpeed((PAir)pair);
        builder.setSelected(pair);
        PlMission.setChanged();
        /*break MISSING_BLOCK_LABEL_420;
        Exception exception;
        exception;
        if(pathair != null && pathair.points() == 0)
            pathair.destroy();
        System.out.println(exception);*/
        
//  //By PAL, v4.111 by JD-GUI      
//  public void insert(Loc paramLoc, boolean paramBoolean) {
//    PathAir localPathAir = null;
//    try
//    {
//      Point3d localPoint3d = paramLoc.getPoint();
//      int i = builder.wSelect.comboBox1.getSelected();
//      int j = builder.wSelect.comboBox2.getSelected();
//      Object localObject;
//      if (builder.selectedPath() != null) {
//        localObject = builder.selectedPath();
//        if (!(localObject instanceof PathAir))
//          return;
//        localPathAir = (PathAir)localObject;
//        if ((i - this.startComboBox1 != localPathAir._iType) || (j != localPathAir._iItem))
//        {
//          builder.setSelected(null);
//        }
//      }
//      PAir localPAir;
//      if (builder.selectedPoint() != null) {
//        localObject = (PAir)builder.selectedPoint();
//        if (((PAir)localObject).type() == 2)
//          return;
//        localPAir = new PAir(builder.selectedPath(), builder.selectedPoint(), localPoint3d, 0, this.defaultHeight, this.defaultSpeed);
//      } else {
//        if ((i < this.startComboBox1) || (i >= this.startComboBox1 + this.type.length))
//          return;
//        i -= this.startComboBox1;
//        if ((j < 0) || (j >= this.type[i].item.length)) {
//          return;
//        }
//        localPathAir = new PathAir(builder.pathes, i, j);
//        localPathAir.setArmy(this.type[i].item[j].army);
//        this.defaultArmy[this.type[i].item[j].army].load(localPathAir);
//
//        if (!searchEnabledSlots(localPathAir)) {
//          localPathAir.destroy();
//          return;
//        }
//        localPathAir.setName(localPathAir.sRegiment + localPathAir.iSquadron + localPathAir.iWing);
//        Property.set(localPathAir, "builderPlugin", this);
//        localPathAir.drawing(this.viewMap.containsKey(i));
//        localPAir = new PAir(localPathAir, null, localPoint3d, 0, this.defaultHeight, this.defaultSpeed);
//      }
//      clampSpeed(localPAir);
//      builder.setSelected(localPAir);
//      PlMission.setChanged();
//    } catch (Exception localException) {
//      if ((localPathAir != null) && (localPathAir.points() == 0))
//        localPathAir.destroy();
//      System.out.println(localException);
//    }
//  }    
    
    //+++ TODO: 4.12 changed code +++
    // by Storebror from DJ 4.12 decompiled sources, fixed try-catch-block
    public void insert(Loc loc, boolean flag)
    {
        PathAir pathair = null;
        try
        {
	        Point3d point3d;
	        int i = builder.wSelect.comboBox1.getSelected();
	        int j = builder.wSelect.comboBox2.getSelected();
	        Path path;
	        point3d = loc.getPoint();
	        if(builder.selectedPath() != null) {
	        	path = builder.selectedPath();
		        if(!(path instanceof PathAir))
		            return;
		        pathair = (PathAir)path;
		        if(i - startComboBox1 != pathair._iType || j != pathair._iItem)
		            builder.setSelected(null);
	        }
	        PAir pair1;
	        if(builder.selectedPoint() != null) {
		        PAir pair = (PAir)builder.selectedPoint();
		        if(pair.type() == 2)
		            return;
		        int k = 0;
		        if(pair.type() == 1 && pair.waypointType == 4)
		            k = 1;
		        pair1 = new PAir(builder.selectedPath(), builder.selectedPoint(), point3d, k, defaultHeight, defaultSpeed, pair);
	        } else {
		        if(i < startComboBox1 || i >= startComboBox1 + type.length)
		            return;
		        i -= startComboBox1;
		        if(j < 0 || j >= type[i].item.length)
		            return;
		        pathair = new PathAir(builder.pathes, i, j);
		        pathair.setArmy(type[i].item[j].army);
		        defaultArmy[type[i].item[j].army].load(pathair);
		        if(!searchEnabledSlots(pathair))
		        {
		            pathair.destroy();
		            return;
		        }
		        pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
		        Property.set(pathair, "builderPlugin", this);
		        pathair.drawing(viewMap.containsKey(i));
		        pair1 = new PAir(pathair, null, point3d, 0, defaultHeight, defaultSpeed, null);
	        }
	        clampSpeed(pair1);
	        builder.setSelected(pair1);
	        PlMission.setChanged();
        } catch (Exception exception) {
            if ((pathair != null) && (pathair.points() == 0))
            	pathair.destroy();
            System.out.println(exception);
          }
    }
//--- TODO: 4.12 changed code ---    

/* By PAL, old Insert    
    public void insert(Loc paramLoc, boolean paramBoolean) {
    PathAir localPathAir = null;
    try
    {
      Point3d localPoint3d = paramLoc.getPoint();
      int i = builder.wSelect.comboBox1.getSelected();
      int j = builder.wSelect.comboBox2.getSelected();
      Object localObject;
      if (builder.selectedPath() != null) {
        localObject = builder.selectedPath();
        if (!(localObject instanceof PathAir))
          return;
        localPathAir = (PathAir)localObject;
        if ((i - this.startComboBox1 != localPathAir._iType) || (j != localPathAir._iItem))
        {
          builder.setSelected(null);
        }
      }
      PAir localPAir;
      if (builder.selectedPoint() != null) {
        localObject = (PAir)builder.selectedPoint();
        if (((PAir)localObject).type() == 2)
          return;
        localPAir = new PAir(builder.selectedPath(), builder.selectedPoint(), localPoint3d, 0, this.defaultHeight, this.defaultSpeed);
      } else {
        if ((i < this.startComboBox1) || (i >= this.startComboBox1 + this.type.length))
          return;
        i -= this.startComboBox1;
        if ((j < 0) || (j >= this.type[i].item.length)) {
          return;
        }
        localPathAir = new PathAir(builder.pathes, i, j);
        localPathAir.setArmy(this.type[i].item[j].army);
        this.defaultArmy[this.type[i].item[j].army].load(localPathAir);

        if (!searchEnabledSlots(localPathAir)) {
          localPathAir.destroy();
          return;
        }
        localPathAir.setName(localPathAir.sRegiment + localPathAir.iSquadron + localPathAir.iWing);
        Property.set(localPathAir, "builderPlugin", this);
        localPathAir.drawing(this.viewMap.containsKey(i));
        localPAir = new PAir(localPathAir, null, localPoint3d, 0, this.defaultHeight, this.defaultSpeed);
      }
      clampSpeed(localPAir);
      builder.setSelected(localPAir);
      PlMission.setChanged();
    } catch (Exception localException) {
      if ((localPathAir != null) && (localPathAir.points() == 0))
        localPathAir.destroy();
      System.out.println(localException);
    }
  } */
    
//By PAL, ver ac�
    public void changeType()
    {
        int i = builder.wSelect.comboBox1.getSelected() - startComboBox1;
        int j = builder.wSelect.comboBox2.getSelected();
        PathAir pathair = (PathAir)builder.selectedPath();
        if(i != pathair._iType)
            return;
        pathair.skins = new String[4];
        pathair.noseart = new String[4];
      
       	Class class1 = type[i].item[j].clazz;
       	
        	///////////////////////////////////////////////////////////////////////////////////////////////////////////////                    
			//              By PAL, avoid selecting placeholder
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////                  
			int j0 = j;
            while(ObjIO.classGetName(class1).equalsIgnoreCase(PlaceholderLabel)) 
            {
            	if(j >= builder.wSelect.comboBox2.size() - 1) break;
            	j++;
            	class1 = type[i].item[j].clazz;
            }
            if(j >= builder.wSelect.comboBox2.size() - 1)
            {
                j = j0; //I start in the original value
                while(ObjIO.classGetName(class1).equalsIgnoreCase(PlaceholderLabel)) //By PAL, avoid selecting placeholder
                {
                	if(j <= 0) break;
                	j--;
                	class1 = type[i].item[j].clazz;
                }                	
            }                           	    
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

		if (j != j0)
			builder.wSelect.comboBox2.setSelected(j, true, false);

        pathair._iItem = j;      
        	
        String as[] = Aircraft.getWeaponsRegistered(class1);
        if(as != null && as.length > 0)
        {
            int k = 0;
            do
            {
                if(k >= as.length)
                    break;
                String s = as[k];
                //if(s.equalsIgnoreCase(pathair.weapons))
//By PAL, from v4.111
                if(Aircraft.isWeaponDateOk(class1, as[k]) && s.equalsIgnoreCase(pathair.weapons))
                    break;
                k++;
            } while(true);
            if(k == as.length)
                pathair.weapons = as[0];
        }
        clampSpeed(pathair);
        fillDialogWay();
        fillSkins();
        fillNoseart();
        syncSkins();
        syncNoseart();
        syncPilots();
        resetMesh();
        PlMission.setChanged();
    }

    public void configure()
    {
        if(getPlugin("Mission") == null)
            throw new RuntimeException("PlMisAir: plugin 'Mission' not found");
        pluginMission = (PlMission)getPlugin("Mission");
        if(sectFile == null)
            throw new RuntimeException("PlMisAir: field 'sectFile' not defined");
        SectFile sectfile = new SectFile(sectFile, 0);
        int i = sectfile.sections();
        if(i <= 0)
            throw new RuntimeException("PlMisAir: file '" + sectFile + "' is empty");
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
                int i1 = numbertokenizer.next(1, 1, Builder.armyAmount() - 1);
                Class class1 = null;
                try
                {
                    class1 = ObjIO.classForName(s2);
                }
                catch(Exception exception)
                {
                    throw new RuntimeException("PlMisAir: class '" + s2 + "' not found");
                }
                aitem[l] = new Item(s1, class1, i1);
            }

            type[j] = new Type(s, aitem);
        }

    }

    void viewUpdate()
    {
        if(builder.pathes == null)
            return;
        Object aobj[] = builder.pathes.getOwnerAttached();
        for(int i = 0; i < aobj.length; i++)
        {
            Actor actor = (Actor)aobj[i];
            if(actor == null)
                break;
            if(actor instanceof PathAir)
            {
                PathAir pathair1 = (PathAir)actor;
                pathair1.drawing(viewMap.containsKey(pathair1._iType));
            }
        }

        if(builder.selectedPath() != null)
        {
            Path path = builder.selectedPath();
            if(path instanceof PathAir)
            {
                PathAir pathair = (PathAir)path;
                if(!viewMap.containsKey(pathair._iType))
                    builder.setSelected(null);
            }
        }
        if(!builder.isFreeView())
            builder.repaint();
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
        viewType(i, viewType[i].bChecked);
    }

    public void viewTypeAll(boolean flag)
    {
        for(int i = 0; i < type.length; i++)
            if(viewType[i].bChecked != flag)
            {
                viewType[i].bChecked = flag;
                viewType(i, flag);
            }

    }

    private void fillComboBox1()
    {
        startComboBox1 = builder.wSelect.comboBox1.size();
        for(int i = 0; i < type.length; i++)
            builder.wSelect.comboBox1.add(I18N.technic(type[i].name));

        if(startComboBox1 == 0)
            builder.wSelect.comboBox1.setSelected(0, true, false);
    }

    private void fillComboBox2(int i, int j)
    {
        if(i < startComboBox1 || i >= startComboBox1 + type.length)
            return;
        if(builder.wSelect.curFilledType != i)
        {
            builder.wSelect.curFilledType = i;
            builder.wSelect.comboBox2.clear(false);
            for(int k = 0; k < type[i - startComboBox1].item.length; k++)
                builder.wSelect.comboBox2.add(I18N.plane(type[i - startComboBox1].item[k].name));

            builder.wSelect.comboBox1.setSelected(i, true, false);
        }
        builder.wSelect.comboBox2.setSelected(j, true, false);
        Class class1 = type[i].item[j].clazz;
        builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, currentCountry()), false);
    }

    public String[] actorInfo(Actor actor)
    {
        PathAir pathair = (PathAir)actor.getOwner();
        _actorInfo[0] = I18N.technic(type[pathair._iType].name) + "." + I18N.plane(type[pathair._iType].item[pathair._iItem].name) + ":" + pathair.typedName;
        PAir pair = (PAir)actor;
        int i = pathair.pointIndx(pair);
        _actorInfo[1] = "(" + i + ") " + timeSecToString(pair.time + (double)(int)(World.getTimeofDay() * 60F * 60F));
        return _actorInfo;
    }
//By PAL, from 4.101
/*
    public void syncSelector()
    {
        builder.wSelect.tabsClient.addTab(1, tabActor);
        fillEditActor();
        PathAir pathair = (PathAir)builder.selectedPath();
        fillComboBox2(pathair._iType + startComboBox1, pathair._iItem);
        builder.wSelect.tabsClient.addTab(2, tabWay);
        fillDialogWay();
        PathAir pathair1 = (PathAir)builder.selectedPath();
        for(int i = 0; i < pathair1.planes; i++)
        {
            builder.wSelect.tabsClient.addTab(i + 3, tabSkin[i]);
            fillEditSkin(i);
        }

        fillSkins();
        fillNoseart();
        fillPilots();
        syncSkins();
        syncNoseart();
        syncPilots();
        resetMesh();
    }*/
    
//By PAL, SyncSlector from 4.111
    public void syncSelector()
    {
        builder.wSelect.tabsClient.addTab(1, tabActor);
        PathAir pathair = (PathAir)builder.selectedPath();
        fillComboBox2(pathair._iType + startComboBox1, pathair._iItem);
        builder.wSelect.tabsClient.addTab(2, tabWay);
        builder.wSelect.tabsClient.addTab(3, tabWay2);
        fillEditActor();
        fillDialogWay();
        PathAir pathair1 = (PathAir)builder.selectedPath();
        for(int i = 0; i < pathair1.planes; i++)
        {
            builder.wSelect.tabsClient.addTab(i + 4, tabSkin[i]);
            fillEditSkin(i);
        }

        fillSkins();
        fillNoseart();
        fillPilots();
        syncSkins();
        syncNoseart();
        syncPilots();
        resetMesh();
    }    

    public void updateSelector()
    {
        fillDialogWay();
    }

    public void updateSelectorMesh()
    {
        PathAir pathair = (PathAir)builder.selectedPath();
        PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(type[pathair._iType].item[pathair._iItem].clazz, pathair.regiment().country());
        if(paintscheme != null)
        {
            HierMesh hiermesh = builder.wSelect.getHierMesh();
            if(hiermesh != null)
                paintscheme.prepare(type[pathair._iType].item[pathair._iItem].clazz, hiermesh, pathair.regiment(), pathair.iSquadron, pathair.iWing, 0);
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
                if(i1 < startComboBox1 || i1 >= startComboBox1 + type.length)
                    return false;
                int j1 = Plugin.builder.wSelect.comboBox2.getSelected();
                Class class1 = type[i1 - startComboBox1].item[j1].clazz;
                Plugin.builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, currentCountry()), false);
                resetMesh();
                fillSkins();
                fillNoseart();
                fillPilots();
                syncSkins();
                syncNoseart();
                syncPilots();
                PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                if(pathair != null)
                    pathair.updateTypedName();
                fillEditActor();
                return false;
            }

        }
);
        int i;
        for(i = builder.mDisplayFilter.subMenu.size() - 1; i >= 0 && pluginMission.viewBridge != builder.mDisplayFilter.subMenu.getItem(i); i--);
        if(--i >= 0)
        {
            int j = i;
            i = type.length - 1;
            viewType = new ViewItem[type.length];
            for(; i >= 0; i--)
            {
                ViewItem viewitem = null;
                if("de".equals(RTSConf.cur.locale.getLanguage()))
                    viewitem = (ViewItem)builder.mDisplayFilter.subMenu.addItem(j, new ViewItem(i, builder.mDisplayFilter.subMenu, I18N.technic(type[i].name) + " " + i18n("show"), null));
                else
                    viewitem = (ViewItem)builder.mDisplayFilter.subMenu.addItem(j, new ViewItem(i, builder.mDisplayFilter.subMenu, i18n("show") + " " + I18N.technic(type[i].name), null));
                viewitem.bChecked = true;
                viewType[i] = viewitem;
                viewType(i, true);
            }

        }
        initEditActor();
        initEditWay();
        initEditWay2(); //By PAL, new from 4.111        
        initEditSkin();
    }

    private boolean searchEnabledSlots(PathAir pathair)
    {
    	//+++ TODO: 4.12 changed code +++
//        makeRegimentList(pathair.getArmy());
        makeRegimentList(pathair.getArmy(), pathair.sCountry);
        //--- TODO: 4.12 changed code ---
        
        int i = regimentList.size();
        if(pathair.iRegiment < 0)
            pathair.iRegiment = 0;
        if(pathair.iRegiment >= i)
            pathair.iRegiment = i - 1;
        for(int j = 0; j < i; j++)
        {
            Regiment regiment = (Regiment)regimentList.get(pathair.iRegiment);
            pathair.sRegiment = regiment.name();
            if(isEnabledRegiment(regiment))
            {
                for(int k = 0; k < 4; k++)
                {
                    ResSquadron ressquadron = (ResSquadron)Actor.getByName(regiment.name() + pathair.iSquadron);
                    if(ressquadron == null)
                    {
                        defaultArmy[pathair.getArmy()].save(pathair);
                        return true;
                    }
                    if(isEnabledSquad(ressquadron))
                    {
                        for(int l = 0; l < 4; l++)
                        {
                            if(Actor.getByName(regiment.name() + pathair.iSquadron + pathair.iWing) == null)
                            {
                                defaultArmy[pathair.getArmy()].save(pathair);
                                return true;
                            }
                            pathair.iWing = (pathair.iWing + 1) % 4;
                        }

                    }
                    pathair.iWing = 0;
                    pathair.iSquadron = (pathair.iSquadron + 1) % 4;
                }

            } else
            {
                pathair.iWing = 0;
            }
            pathair.iSquadron = 0;
            pathair.iRegiment = (pathair.iRegiment + 1) % i;
            
            //+++ TODO: 4.12 changed code +++
            pathair.sCountry = regiment.branch();
            //--- TODO: 4.12 changed code ---
            
        }

        return false;
    }

    private void fillEnabledRegiments(int i)
    {
        int j = regimentList.size();
        for(int k = 0; k < j; k++)
            if(k == i)
            {
                wRegiment.posEnable[k] = true;
            } else
            {
                Regiment regiment = (Regiment)regimentList.get(k);
                wRegiment.posEnable[k] = isEnabledRegiment(regiment);
            }

    }

    private String currentCountry()
    {
        if(wRegiment == null)
            return "ru";
        int i = wRegiment.getSelected();
        if(i < 0)
            return "ru";
        else
            return ((Regiment)regimentList.get(i)).country();
    }

    private boolean isEnabledRegiment(Regiment regiment)
    {
        if(regiment.getOwnerAttachedCount() < 4)
            return true;
        _squads = regiment.getOwnerAttached(_squads);
        boolean flag = false;
        for(int i = 0; i < 4; i++)
        {
            ResSquadron ressquadron = (ResSquadron)_squads[i];
            _squads[i] = null;
            if(ressquadron == null || isEnabledSquad(ressquadron))
                flag = true;
        }

        return flag;
    }

    private void fillEnabledSquads(Regiment regiment, int i)
    {
        for(int j = 0; j < 4; j++)
            wSquadron.posEnable[j] = true;

        _squads = regiment.getOwnerAttached(_squads);
        for(int k = 0; k < 4; k++)
        {
            ResSquadron ressquadron = (ResSquadron)_squads[k];
            if(ressquadron == null)
                break;
            _squads[k] = null;
            if(ressquadron.index() != i)
                wSquadron.posEnable[ressquadron.index()] = isEnabledSquad(ressquadron);
        }

    }

    private boolean isEnabledSquad(ResSquadron ressquadron)
    {
        return ressquadron.getAttachedCount() < 4;
    }

    private void fillEnabledWings(ResSquadron ressquadron, int i)
    {
        for(int j = 0; j < 4; j++)
            wWing.posEnable[j] = true;

        _wings = ressquadron.getAttached(_wings);
        for(int k = 0; k < 4; k++)
        {
            PathAir pathair = (PathAir)_wings[k];
            if(pathair == null)
                break;
            _wings[k] = null;
            if(pathair.iWing != i)
                wWing.posEnable[pathair.iWing] = false;
        }

    }

    private void controlResized(GWindowDialogClient gwindowdialogclient, GWindow gwindow)
    {
        if(gwindow == null)
        {
            return;
        } else
        {
            gwindow.setSize(gwindowdialogclient.win.dx - gwindow.win.x - gwindowdialogclient.lAF().metric(1.0F), gwindow.win.dy);
            return;
        }
    }

    private void editResized(GWindowDialogClient gwindowdialogclient)
    {
        //controlResized(gwindowdialogclient, wArmy); //By PAL, to avoid resizing
//        controlResized(gwindowdialogclient, wBranch); //By PAL  
        
        //+++ TODO: 4.12 changed code +++
        controlResized(gwindowdialogclient, wCountry);
        //--- TODO: 4.12 changed code ---
        
        controlResized(gwindowdialogclient, wRegiment);               
        controlResized(gwindowdialogclient, wSquadron);
        controlResized(gwindowdialogclient, wWing);
        controlResized(gwindowdialogclient, wWeapons);
        controlResized(gwindowdialogclient, wFuel);
        controlResized(gwindowdialogclient, wPlanes);
        controlResized(gwindowdialogclient, wSkill);
    }

    public void initEditActor()
    {
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)builder.wSelect.tabsClient.create(new GWindowDialogClient() {

            public void resized()
            {
                super.resized();
                editResized(this);
                
                //+++ TODO: 4.12 changed code +++
                if(render3D != null)
                {
                    for(int i = 0; i < render3D.length; i++)
                        render3D[i].setClearColor(GWindowLookAndFeel.getUIBackgroundColor());
                }
                //--- TODO: 4.12 changed code ---
            }

        }
);
        tabActor = builder.wSelect.tabsClient.createTab(i18n("AircraftActor"), gwindowdialogclient);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, i18n("Army"), null));        
        //gwindowdialogclient.addControl(wArmy = new GWindowComboControl(gwindowdialogclient, 9F, 1.0F, 7F)
        gwindowdialogclient.addControl(wArmy = new GWindowComboControl(gwindowdialogclient, 9F, 1.0F, 5F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                for(int i = 1; i < Builder.armyAmount(); i++)
                    add(I18N.army(Army.name(i)));

            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                int k = getSelected() + 1;
                int l = pathair.getArmy();
                int i1 = pathair.iRegiment;
                int j1 = pathair.iSquadron;
                int k1 = pathair.iWing;
                
                //+++ TODO: 4.12 changed code +++
                String s = pathair.sCountry;
                //--- TODO: 4.12 changed code ---
                
                pathair.setArmy(k);
                defaultArmy[k].load(pathair);
                if(!searchEnabledSlots(pathair))
                {
                    pathair.setArmy(l);
                    pathair.iRegiment = i1;
                    pathair.iSquadron = j1;
                    pathair.iWing = k1;
                    
                    //+++ TODO: 4.12 changed code +++
                    pathair.sCountry = s;
                    //--- TODO: 4.12 changed code ---
                    
                    searchEnabledSlots(pathair);
                }
                /*
                    //By PAL, select corresponding Branch in Combo
                    Regiment regiment = (Regiment)regimentList.get(pathair.iRegiment);			  		
			  		int br = branchList.indexOf(regiment.branch());
        			wBranch.setSelected(br, true, false);                
                */
                pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                fillEditActor();
                fillNoseart();
                syncNoseart();
                Class class1 = type[pathair._iType].item[pathair._iItem].clazz;
                Plugin.builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, currentCountry()), false);
                resetMesh();
                if(Path.player == pathair)
                    PlMission.cur.missionArmy = k;
                PlMission.setChanged();
                PlMission.checkShowCurrentArmy();
                return false;
            }

        }
);
        
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, i18n("Country"), null));
//        gwindowdialogclient.addControl(wCountry = new GWindowComboControl(gwindowdialogclient, 9F, 3F, 7F) {
        gwindowdialogclient.addControl(wCountry = new GWindowComboControl(gwindowdialogclient, 15F, 1.0F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                int k = getSelected();
                Country country = (Country)listCountry[iArmyRegimentList].get(k);
	            System.out.println("wCountry selection changed.");
                if(makeRegimentList(iArmyRegimentList, country.name))
                {
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    pathair.sCountry = country.name;
                    pathair.iRegiment = 0;
                    searchEnabledSlots(pathair);
                    pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                    defaultArmy[pathair.getArmy()].save(pathair);
                    fillEditActor();
                    fillNoseart();
                    syncNoseart();
                    Class class1 = type[pathair._iType].item[pathair._iItem].clazz;
                    Plugin.builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, currentCountry()), false);
                    resetMesh();
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        
//By PAL
      //  gwindowdialogclient.addControl(wBranch = new GWindowComboControl(gwindowdialogclient, 9.0F, 21F, 5F) {
//	  gwindowdialogclient.addControl(wBranch = new GWindowComboControl(gwindowdialogclient, 15F, 1.0F, 7F) {
//            public void afterCreated()
//            {
//                super.afterCreated();
//                setEditable(false);
//            }
//
//            public boolean notify(int i, int j)
//            {
//                if(i != 2)
//                {
//                    return false;
//                } else
//                {
//                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
//                    //pathair.iRegiment = getSelected();
//                    
//                    int ct = getSelected();
//                    if (ct < 0) ct = 0;
//                    
//                    String branch = (String)branchList.get(ct);
//        			int si = regimentList.size();			                          	
//			        for(int l = 0; l < si; l++)
//			        {
//			            Regiment regiment1 = (Regiment)regimentList.get(l);
//			            System.out.println("wBranch selection changed. regiment1.branch()="+regiment1.branch()+", branch="+branch);
////			            if (branch.equalsIgnoreCase(regiment1.branch()))
//				        if (true)
//			            {
//			            	pathair.iRegiment = l; //By PAL, I select the first of this country
//			            	break;			            	
//			            }			            
//			        }    	
//
//                    searchEnabledSlots(pathair);                    
//                    pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
//                    defaultArmy[pathair.getArmy()].save(pathair);
//                    fillEditActor();
//                    fillNoseart();
//                    syncNoseart();
//                    Class class1 = type[pathair._iType].item[pathair._iItem].clazz;
//                    Plugin.builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, currentCountry()), false);
//                    resetMesh();
//                    PlMission.setChanged();
//                    return false;
//                }
//            }
//
//        }
//);
        gwindowdialogclient.addControl(wRegiment = new GWindowComboControl(gwindowdialogclient, 9.0F, 3F, 7F) {
//        gwindowdialogclient.addControl(wRegiment = new GWindowComboControl(gwindowdialogclient, 1.0F, 3F, 15F) {
            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                {
                    return false;
                } else
                {
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    pathair.iRegiment = getSelected();
                    /*
                    //By PAL, select corresponding Branch in Combo
                    Regiment regiment = (Regiment)regimentList.get(getSelected());			  		
			  		int br = branchList.indexOf(regiment.branch());
        			wBranch.setSelected(br, true, false);
        			*/	                    
                    searchEnabledSlots(pathair);
                    pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                    defaultArmy[pathair.getArmy()].save(pathair);
                    fillEditActor();
                    fillNoseart();
                    syncNoseart();
                    Class class1 = type[pathair._iType].item[pathair._iItem].clazz;
                    Plugin.builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, currentCountry()), false);
                    resetMesh();
                    PlMission.setChanged();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, i18n("Squadron"), null));
        gwindowdialogclient.addControl(wSquadron = new GWindowComboControl(gwindowdialogclient, 9F, 5F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add("1");
                add("2");
                add("3");
                add("4");
                posEnable = new boolean[4];
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                {
                    return false;
                } else
                {
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    pathair.iSquadron = getSelected();
                    searchEnabledSlots(pathair);
                    pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                    defaultArmy[pathair.getArmy()].save(pathair);
                    fillEditActor();
                    resetMesh();
                    PlMission.setChanged();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 7F, 1.3F, i18n("Wing"), null));
        gwindowdialogclient.addControl(wWing = new GWindowComboControl(gwindowdialogclient, 9F, 7F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add("1");
                add("2");
                add("3");
                add("4");
                posEnable = new boolean[4];
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                {
                    return false;
                } else
                {
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    pathair.iWing = getSelected();
                    pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                    defaultArmy[pathair.getArmy()].save(pathair);
                    fillEditActor();
                    resetMesh();
                    PlMission.setChanged();
                    return false;
                }
            }

        }
);
        lWeapons = new ArrayList();
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 7F, 1.3F, i18n("Weapons"), null));
        gwindowdialogclient.addControl(wWeapons = new GWindowComboControl(gwindowdialogclient, 9F, 9F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                int k = getSelected();
                if(k < 0)
                    pathair.weapons = null;
                else
                    pathair.weapons = (String)lWeapons.get(k);
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 7F, 1.3F, i18n("Fuel"), null));
        gwindowdialogclient.addControl(wFuel = new GWindowEditControl(gwindowdialogclient, 9F, 11F, 7F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bDelayedNotify = true;
                bNumericOnly = true;
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                String s = getValue();
                int k = 0;
                if(s != null)
                    try
                    {
                        k = Integer.parseInt(s);
                    }
                    catch(Exception exception)
                    {
                        setValue("" + ((PathAir)Plugin.builder.selectedPath()).fuel, false);
                        return false;
                    }
                if(k < 0)
                    k = 0;
                else
                if(k > 100)
                    k = 100;
                pathair.fuel = k;
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13F, 7F, 1.3F, i18n("Planes"), null));
        gwindowdialogclient.addControl(wPlanes = new GWindowComboControl(gwindowdialogclient, 9F, 13F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add("1");
                add("2");
                add("3");
                add("4");
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                {
                    return false;
                } else
                {
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    pathair.planes = getSelected() + 1;
                    checkEditSkinTabs();
                    checkEditSkinSkills();
                    PlMission.setChanged();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 15F, 7F, 1.3F, i18n("Skill"), null));
        gwindowdialogclient.addControl(wSkill = new GWindowComboControl(gwindowdialogclient, 9F, 15F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add(Plugin.i18n("Rookie"));
                add(Plugin.i18n("Average"));
                add(Plugin.i18n("Veteran"));
                add(Plugin.i18n("Ace"));
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                pathair.skill = getSelected();
                for(int k = 0; k < 4; k++)
                    pathair.skills[k] = pathair.skill;

                checkEditSkinSkills();
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 17F, 7F, 1.3F, i18n("OnlyAI"), null));
        gwindowdialogclient.addControl(wOnlyAI = new GWindowCheckBox(gwindowdialogclient, 9F, 17F, null) {

            public void preRender()
            {
                super.preRender();
                PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                if(pathair == null)
                {
                    return;
                } else
                {
                    setChecked(pathair.bOnlyAI, false);
                    setEnable(type[pathair._iType].item[pathair._iItem].bEnablePlayer);
                    return;
                }
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                pathair.bOnlyAI = isChecked();
                if(isChecked() && Path.player == pathair)
                {
                    Path.player = null;
                    Path.playerNum = 0;
                }
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 19F, 7F, 1.3F, i18n("Parachute"), null));
        gwindowdialogclient.addControl(wParachute = new GWindowCheckBox(gwindowdialogclient, 9F, 19F, null) {

            public void preRender()
            {
                super.preRender();
                PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                if(pathair == null)
                {
                    return;
                } else
                {
                    setChecked(pathair.bParachute, false);
                    return;
                }
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                {
                    return false;
                } else
                {
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    pathair.bParachute = isChecked();
                    PlMission.setChanged();
                    return false;
                }
            }

        }
);
    }
    
//By PAL, new from 4.111
    public void initEditWay2()
    {
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)builder.wSelect.tabsClient.create(new GWindowDialogClient() {

            public void resized()
            {
                super.resized();
                editResized(this);
            }

        }
);
        tabWay2 = builder.wSelect.tabsClient.createTab(i18n("WaypointOptions"), gwindowdialogclient);
        gwindowdialogclient.addLabel(wDelayTimerLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, i18n("Delay"), null));
        gwindowdialogclient.addLabel(wDelayTimerLabel2 = new GWindowLabel(gwindowdialogclient, 15F, 3F, 4F, 1.3F, i18n("min"), null));
        gwindowdialogclient.addControl(wDelayTimer = new GWindowEditControl(gwindowdialogclient, 9F, 3F, 5F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                PAir pair = (PAir)Plugin.builder.selectedPoint();
                int k = Integer.parseInt(wDelayTimer.getValue());
                if(k < 0)
                    k = 0;
                if(k > 1800)
                    k = 1800;
                pair.delayTimer = k;
                if(k > 0 && wTakeOffType.getSelected() == 0)
                    pair.waypointType = 1;
                setValue("" + k, false);
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wTakeOffTypeLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, i18n("TakeOffType"), null));
        gwindowdialogclient.addControl(wTakeOffType = new GWindowComboControl(gwindowdialogclient, 9F, 1.0F, 9F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add(Plugin.i18n("TakeoffTNormal"));
                add(Plugin.i18n("TakeoffTPairs"));
                add(Plugin.i18n("TakeoffTLine"));
                
                //+++ TODO: 4.12 changed code +++
                add(Plugin.i18n("TakeoffTTaxi"));
                posEnable = new boolean[5];
                for(int i = 0; i < 5; i++)
                    posEnable[i] = true;
                //--- TODO: 4.12 changed code ---
                
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    pair.waypointType = wTakeOffType.getSelected();
                    if(pair.waypointType > 0)
                        pair.waypointType++;
                    else
                    if(pair.delayTimer > 0)
                        pair.waypointType++;
                    fillDialogWay2();
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wTakeoffSpacingLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, i18n("TakeOffSpacing"), null));
        gwindowdialogclient.addLabel(wTakeoffSpacingLabel2 = new GWindowLabel(gwindowdialogclient, 15F, 5F, 4F, 1.3F, i18n("meters"), null));
        gwindowdialogclient.addControl(wTakeoffSpacing = new GWindowEditControl(gwindowdialogclient, 9F, 5F, 5F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                //TODO: +++ 4.12.1 changed code +++                
                bNumericOnly = false;
//                bNumericOnly = true;
                //TODO: --- 4.12.1 changed code ---
                
                bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                PAir pair = (PAir)Plugin.builder.selectedPoint();
                int k = Integer.parseInt(wTakeoffSpacing.getValue());
                
                //+++ TODO: 4.12 changed code +++
//                if(k < 0)
//                    k = 0;
//                if(k > 1800)
//                    k = 1000;
                if(k < -1000)
                    k = -1000;
                if(k > 1000)
                    k = 1000;
                //--- TODO: 4.12 changed code ---
                
                pair.takeoffSpacing = k;
                setValue("" + k, false);
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wLandingTypeLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, i18n("LandingType"), null));
        gwindowdialogclient.addControl(wLandingType = new GWindowComboControl(gwindowdialogclient, 9F, 1.0F, 9F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add(Plugin.i18n("LandingTypeLP"));
                add(Plugin.i18n("LandingTypeRP"));
                add(Plugin.i18n("LandingTypeSLP"));
                add(Plugin.i18n("LandingTypeSRP"));
                add(Plugin.i18n("LandingTypeStraightIn"));
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    if(wLandingType.getSelected() > 0)
                        pair.waypointType = wLandingType.getSelected() + 100;
                    else
                        pair.waypointType = 0;
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wNormFlyTypeLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, i18n("NormflyType"), null));
        gwindowdialogclient.addControl(wNormFlyType = new GWindowComboControl(gwindowdialogclient, 9F, 1.0F, 9F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add(Plugin.i18n("NormflyTypeNormal"));
                add(Plugin.i18n("NormflyTypeCAP"));
                
                //+++ TODO: 4.12 changed code +++
                add(Plugin.i18n("ArtSpot"));
                //--- TODO: 4.12 changed code ---
                
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    if(wNormFlyType.getSelected() == 1)
                        pair.waypointType = 401;
                    else
                    	
                    	//+++ TODO: 4.12 changed code +++
                        if(wNormFlyType.getSelected() == 2)
                            pair.waypointType = 407;
                        else
                        	//--- TODO: 4.12 changed code ---
                        	
                        pair.waypointType = 0;
                    fillDialogWay2();
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wCAPTypeLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, i18n("CAPType"), null));
        gwindowdialogclient.addControl(wCAPType = new GWindowComboControl(gwindowdialogclient, 9F, 3F, 9F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add(Plugin.i18n("CAPTypeTriangle"));
                add(Plugin.i18n("CAPTypeSquare"));
                add(Plugin.i18n("CAPTypePentagon"));
                add(Plugin.i18n("CAPTypeHexagon"));
                add(Plugin.i18n("CAPTypeRandom"));
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    pair.waypointType = wCAPType.getSelected() + 401;
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wCAPCyclesLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, i18n("CAPCycles"), null));
        gwindowdialogclient.addControl(wCAPCycles = new GWindowEditControl(gwindowdialogclient, 9F, 5F, 5F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    int k = Integer.parseInt(wCAPCycles.getValue());
                    if(k < 0)
                        k = 0;
                    if(k > 1000)
                        k = 1000;
                    pair.cycles = k;
                    setValue("" + k);
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wCAPTimerLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 7F, 1.3F, i18n("CAPTimer"), null));
        gwindowdialogclient.addLabel(wCAPTimerLabelMin = new GWindowLabel(gwindowdialogclient, 15F, 7F, 7F, 1.3F, i18n("min"), null));
        gwindowdialogclient.addControl(wCAPTimer = new GWindowEditControl(gwindowdialogclient, 9F, 7F, 5F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    int k = Integer.parseInt(wCAPTimer.getValue());
                    if(k < 0)
                        k = 0;
                    if(k > 10000)
                        k = 10000;
                    pair.delayTimer = k;
                    setValue("" + k);
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wCAPOrientLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 7F, 1.3F, i18n("CAPAngle"), null));
        gwindowdialogclient.addLabel(wCAPOrientLabelDeg = new GWindowLabel(gwindowdialogclient, 15F, 9F, 7F, 1.3F, i18n("CAPdeg"), null));
        gwindowdialogclient.addControl(wCAPOrient = new GWindowEditControl(gwindowdialogclient, 9F, 9F, 5F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    int k = Integer.parseInt(wCAPOrient.getValue());
                    if(k < 0)
                        k = 0;
                    if(k > 360)
                        k = 360;
                    pair.orient = k;
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wCAPBSizeLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 7F, 1.3F, i18n("CAPBaseSize"), null));
        gwindowdialogclient.addLabel(wCAPBSizeLabelKM = new GWindowLabel(gwindowdialogclient, 15F, 11F, 7F, 1.3F, i18n("km"), null));
        gwindowdialogclient.addControl(wCAPBSize = new GWindowEditControl(gwindowdialogclient, 9F, 11F, 5F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    int k = Integer.parseInt(wCAPBSize.getValue());
                    if(k < 1)
                        k = 1;
                    if(k > 10000)
                        k = 10000;
                    pair.baseSize = k;
                    setValue("" + k);
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wCAPAltDifferenceLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 13F, 7F, 1.3F, i18n("CAPAltDifference"), null));
        gwindowdialogclient.addLabel(wCAPAltDifferenceLabelM = new GWindowLabel(gwindowdialogclient, 15F, 13F, 7F, 1.3F, i18n("meters"), null));
        gwindowdialogclient.addControl(wCAPAltDifference = new GWindowEditControl(gwindowdialogclient, 9F, 13F, 5F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    pair.altDifference = Integer.parseInt(wCAPAltDifference.getValue());
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wGAttackNoOptionInfoLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 19F, 1.3F, i18n("GAttackNoOptions"), null));
    }

    private void showHideWay2()
    {
        PAir pair = (PAir)Plugin.builder.selectedPoint();
        wTakeOffType.hideWindow();
        wTakeOffTypeLabel.hideWindow();
        wTakeoffSpacing.hideWindow();
        wTakeoffSpacingLabel.hideWindow();
        wTakeoffSpacingLabel2.hideWindow();
        wDelayTimer.hideWindow();
        wDelayTimerLabel.hideWindow();
        wDelayTimerLabel2.hideWindow();
        wLandingType.hideWindow();
        wLandingTypeLabel.hideWindow();
        wNormFlyType.hideWindow();
        wNormFlyTypeLabel.hideWindow();
        wCAPType.hideWindow();
        wCAPTypeLabel.hideWindow();
        wCAPCycles.hideWindow();
        wCAPCyclesLabel.hideWindow();
        wCAPTimer.hideWindow();
        wCAPTimerLabel.hideWindow();
        wCAPTimerLabelMin.hideWindow();
        wCAPOrient.hideWindow();
        wCAPOrientLabel.hideWindow();
        wCAPOrientLabelDeg.hideWindow();
        wCAPBSize.hideWindow();
        wCAPBSizeLabel.hideWindow();
        wCAPBSizeLabelKM.hideWindow();
        wCAPAltDifference.hideWindow();
        wCAPAltDifferenceLabel.hideWindow();
        wCAPAltDifferenceLabelM.hideWindow();
        wGAttackNoOptionInfoLabel.hideWindow();
        switch(pair.type())
        {
        default:
            break;

        case 0: // '\0'
            wNormFlyType.showWindow();
            wNormFlyTypeLabel.showWindow();
            if(wNormFlyType.getSelected() == 1)
            {
                wCAPType.showWindow();
                wCAPTypeLabel.showWindow();
                wCAPCycles.showWindow();
                wCAPCyclesLabel.showWindow();
                wCAPTimer.showWindow();
                wCAPTimerLabel.showWindow();
                wCAPTimerLabelMin.showWindow();
                wCAPOrient.showWindow();
                wCAPOrientLabel.showWindow();
                wCAPOrientLabelDeg.showWindow();
                wCAPBSize.showWindow();
                wCAPBSizeLabel.showWindow();
                wCAPBSizeLabelKM.showWindow();
                wCAPAltDifference.showWindow();
                wCAPAltDifferenceLabel.showWindow();
                wCAPAltDifferenceLabelM.showWindow();
            }
            
            //+++ TODO: 4.12 changed code +++
            if(wNormFlyType.getSelected() == 2)
            {
                wCAPCycles.showWindow();
                wCAPCyclesLabel.showWindow();
                wCAPTimer.showWindow();
                wCAPTimerLabel.showWindow();
                wCAPTimerLabelMin.showWindow();
                wCAPOrient.showWindow();
                wCAPOrientLabel.showWindow();
                wCAPOrientLabelDeg.showWindow();
                wCAPBSize.showWindow();
                wCAPBSizeLabel.showWindow();
                wCAPBSizeLabelKM.showWindow();
            }
            //--- TODO: 4.12 changed code ---
            
            break;

        case 1: // '\001'
            wTakeOffType.showWindow();
            wTakeOffTypeLabel.showWindow();
            wDelayTimer.showWindow();
            wDelayTimerLabel.showWindow();
            wDelayTimerLabel2.showWindow();
            if(wTakeOffType.getSelected() > 0)
            {
                wTakeoffSpacing.showWindow();
                wTakeoffSpacingLabel.showWindow();
                wTakeoffSpacingLabel2.showWindow();
            }
            break;

        case 2: // '\002'
            wLandingType.showWindow();
            wLandingTypeLabel.showWindow();
            break;

        case 3: // '\003'
            wGAttackNoOptionInfoLabel.showWindow();
            break;
        }
    }    

    private void fillEditEnabled(PathAir pathair)
    {
    	//+++ TODO: 4.12 changed code +++
//        makeRegimentList(pathair.getArmy());
        makeRegimentList(pathair.getArmy(), pathair.sCountry);
        //--- TODO: 4.12 changed code ---
        
        fillEnabledRegiments(pathair.iRegiment);
        fillEnabledSquads(pathair.regiment(), pathair.iSquadron);
        fillEnabledWings(pathair.squadron(), pathair.iWing);
        defaultArmy[pathair.getArmy()].save(pathair);
    }

	private void selectCountrySubList(PathAir pathair)
	{
	    Regiment regiment1 = (Regiment)regimentList.get(pathair.iRegiment);
        int lStart = 0;
        int Count = 0;
        String ctry1 = regiment1.country(); //By PAL
        for (int i=0; i < wRegiment.size(); i++)
        {
        	Regiment regiment2 = (Regiment)regimentList.get(i);
        	String ctry2 = regiment2.country(); //By PAL       	
        	if (ctry1.equalsIgnoreCase(ctry2))
        	{	
        		if (Count == 0) lStart = i; //By PAL, first found
        		Count++;
        	}
        	else
        	if (Count > 0) break; //By PAL, si terminaron, chau      	
        }
        wRegiment.setEditable(true);
  		if (Count > 0)
  		{
	        wRegiment.listStartLine = lStart;
	        wRegiment.listCountLines = Count;  			
  		}
  		else
  		{
  			wRegiment.listStartLine = 0;
  			wRegiment.listCountLines = wRegiment.size() - wRegiment.listStartLine;
  		}
  		wRegiment.setEditable(false);
	}


    public void fillEditActor()
    {
        PathAir pathair = (PathAir)builder.selectedPath();
        if(pathair == null)
            return;
        fillEditEnabled(pathair);
        wRegiment.setSelected(pathair.iRegiment, true, false);
                
        //By PAL       
	    selectCountrySubList(pathair);
//        Regiment regiment = (Regiment)regimentList.get(pathair.iRegiment);			  		
//  		int br = branchList.indexOf(regiment.branch());
//  		if (br != wBranch.getSelected())
//			wBranch.setSelected(br, true, false); 	                            
        
        wSquadron.setSelected(pathair.iSquadron, true, false);
        wWing.setSelected(pathair.iWing, true, false);
        wArmy.setSelected(pathair.getArmy() - 1, true, false);
        wFuel.setValue("" + pathair.fuel, false);
        wPlanes.setSelected(pathair.planes - 1, true, false);
        if(pathair.skill != -1)
            wSkill.setSelected(pathair.skill, true, false);
        else
            wSkill.setValue(i18n("Custom"));
        wWeapons.clear(false);
        lWeapons.clear();
        Class class1 = type[pathair._iType].item[pathair._iItem].clazz;
        String as[] = Aircraft.getWeaponsRegistered(class1);
        String s = type[pathair._iType].item[pathair._iItem].name;
//By PAL, from 4.111
        int i = Aircraft.getWeaponsRegistered(class1).length;
        wWeapons.posEnable = new boolean[i];
        if(as != null && as.length > 0)
        {
            int j = -1;
            for(int k = 0; k < as.length; k++)
            {
                String s1 = as[k];
                wWeapons.add(I18N.weapons(s, s1));
                lWeapons.add(s1);
                if(!Aircraft.isWeaponDateOk(class1, s1))
                    wWeapons.posEnable[k] = false;
                else
                    wWeapons.posEnable[k] = true;
                if(!s1.equalsIgnoreCase(pathair.weapons))
                    continue;
                j = k;
                if(!wWeapons.posEnable[k])
                    j = 0;
            }

            if(j == -1)
                j = 0;
            wWeapons.setSelected(j, true, false);
        }
    }

    private void fillDialogWay()
    {
        PAir pair = (PAir)builder.selectedPoint();
        PathAir pathair = (PathAir)builder.selectedPath();
        int i = pathair.pointIndx(pair);
        wPrev.setEnable(i > 0);
        wNext.setEnable(i < pathair.points() - 1);
        wCur.cap = new GCaption("" + i + "(" + pathair.points() + ")");
        wHeight.setValue("" + (int)pair.height, false);
        wSpeed.setValue("" + (int)pair.speed, false);
        wType.setSelected(pair.type(), true, false);
        fillDialogWay2(); //By PAL, new from 4.11        
        for(int j = 0; j < PAir.types.length; j++)
            wType.posEnable[j] = true;

        //+++ TODO: 4.12 changed code +++
//        if(i > 0)
//            wType.posEnable[1] = false;
        int k0 = pair.formation;
        if(k0 <= 0)
            k0 = 0;
        else
            k0--;
        wFormations.setSelected(k0, true, false);
        if(i > 0)
        {
            PAir pair1 = (PAir)pathair.point(i - 1);
            if(pair1.type() != 1)
                wType.posEnable[1] = false;
        }
        //--- TODO: 4.12 changed code ---
        
        if(i < pathair.points() - 1)
            wType.posEnable[2] = false;
        _curPointType = pair.type();
        int k = (int)Math.round(pair.time / 60D + (double)(World.getTimeofDay() * 60F));
        wTimeH.setValue("" + (k / 60) % 24, false);
        wTimeM.setValue("" + k % 60, false);
        if(pair.getTarget() != null)
        {
            if(pair.getTarget() instanceof PPoint)
            {
                if(pair.getTarget() instanceof PAir)
                    wTarget.cap.set(((PathAir)pair.getTarget().getOwner()).typedName);
                else
                if(pair.getTarget() instanceof PNodes)
                    wTarget.cap.set(Property.stringValue(pair.getTarget().getOwner(), "i18nName", ""));
                else
                    wTarget.cap.set(pair.getTarget().getOwner().name());
            } else
            {
                wTarget.cap.set(Property.stringValue(pair.getTarget().getClass(), "i18nName", ""));
            }
        } else
        {
            wTarget.cap.set("");
        }
    }
//By PAL, new from 4.111
    private void fillDialogWay2()
    {
        PAir pair = (PAir)Plugin.builder.selectedPoint();
        int i = pair.waypointType;
        switch(pair.type())
        {
        //+++ TODO: 4.12 changed code +++
//        case 3: // '\003'
        //--- TODO: 4.12 changed code ---
        
        default:
            break;

        case 0: // '\0'
            if(i > 400)
            {
                wNormFlyType.setSelected(1, true, false);
                wCAPType.setSelected(i - 401, true, false);
                break;
            }
            
            //+++ TODO: 4.12 changed code +++
//            if(i == 0)
//                wNormFlyType.setSelected(i, true, false);
//            break;
            if(i == 0)
            {
                wNormFlyType.setSelected(i, true, false);
                break;
            }
            if(i == 407)
                wNormFlyType.setSelected(2, true, false);
            break;
            //--- TODO: 4.12 changed code ---

        case 1: // '\001'
            wTakeOffType.setSelected(i <= 0 ? 0 : i - 1, true, false);
            wTakeoffSpacing.setValue("" + pair.takeoffSpacing, false);
            break;

        case 2: // '\002'
            wLandingType.setSelected(i + (i <= 100 ? 0 : -100), true, false);
            break;
            
            //+++ TODO: 4.12 changed code +++
        case 3: // '\003'
            pair.waypointType = 0;
            break;
            //--- TODO: 4.12 changed code ---
            
        }
        wDelayTimer.setValue("" + pair.delayTimer, false);
        wTakeoffSpacing.setValue("" + pair.takeoffSpacing, false);
        wCAPCycles.setValue("" + pair.cycles, false);
        wCAPTimer.setValue("" + pair.delayTimer, false);
        wCAPOrient.setValue("" + (int)pair.orient, false);
        wCAPBSize.setValue("" + pair.baseSize, false);
        wCAPAltDifference.setValue("" + pair.altDifference, false);
        showHideWay2();
    }
    
    public void initEditWay()
    {
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)builder.wSelect.tabsClient.create(new GWindowDialogClient());
        tabWay = builder.wSelect.tabsClient.createTab(i18n("Waypoint"), gwindowdialogclient);
        gwindowdialogclient.addControl(wPrev = new GWindowButton(gwindowdialogclient, 1.0F, 1.0F, 5F, 1.6F, i18n("&Prev"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    int k = pathair.pointIndx(pair);
                    if(k > 0)
                    {
                        Plugin.builder.setSelected(pathair.point(k - 1));
                        fillDialogWay();
                        Plugin.builder.repaint();
                    }
                    fillDialogWay2(); //By PAL, new from 4.11                    
                    return true;
                } else
                {
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addControl(wNext = new GWindowButton(gwindowdialogclient, 9F, 1.0F, 5F, 1.6F, i18n("&Next"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    int k = pathair.pointIndx(pair);
                    if(k < pathair.points() - 1)
                    {
                        Plugin.builder.setSelected(pathair.point(k + 1));
                        fillDialogWay();
                        Plugin.builder.repaint();
                    }
                    fillDialogWay2(); //By PAL, new from 4.11                    
                    return true;
                } else
                {
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(wCur = new GWindowLabel(gwindowdialogclient, 15F, 1.0F, 4F, 1.6F, "1(1)", null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, i18n("Height"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 15F, 3F, 4F, 1.3F, i18n("[M]"), null));
        gwindowdialogclient.addControl(wHeight = new GWindowEditControl(gwindowdialogclient, 9F, 3F, 5F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                PAir pair = (PAir)Plugin.builder.selectedPoint();
                String s = getValue();
                double d = 0.0D;
                try
                {
                    d = Double.parseDouble(s);
                }
                catch(Exception exception)
                {
                    setValue("" + ((PAir)Plugin.builder.selectedPoint()).height, false);
                    return false;
                }
                if(d < 0.0D)
                    d = 0.0D;
                else
                if(d > 12000D)
                    d = 12000D;
                pair.height = d;
                defaultHeight = d;
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, i18n("Speed"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 15F, 5F, 4F, 1.3F, i18n("[kM/H]"), null));
        gwindowdialogclient.addControl(wSpeed = new GWindowEditControl(gwindowdialogclient, 9F, 5F, 5F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                    return false;
                PAir pair = (PAir)Plugin.builder.selectedPoint();
                String s = getValue();
                double d = 0.0D;
                try
                {
                    d = Double.parseDouble(s);
                }
                catch(Exception exception)
                {
                    setValue("" + ((PAir)Plugin.builder.selectedPoint()).speed, false);
                    return false;
                }
                PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                if(pair.type() == 1 || pair.type() == 2)
                    d = 0.0D;
                else
                if(d < type[pathair._iType].item[pathair._iItem].speedMin)
                {
                    d = type[pathair._iType].item[pathair._iItem].speedMin;
                    defaultSpeed = d;
                } else
                if(d > type[pathair._iType].item[pathair._iItem].speedMax)
                {
                    d = type[pathair._iType].item[pathair._iItem].speedMax;
                    defaultSpeed = d;
                } else
                {
                    defaultSpeed = d;
                }
                pair.speed = d;
                pathair.computeTimes();
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 7F, 1.3F, i18n("Time"), null));
        gwindowdialogclient.addControl(wTimeH = new GWindowEditControl(gwindowdialogclient, 9F, 7F, 2.0F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
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
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 11.2F, 7F, 1.0F, 1.3F, ":", null));
        gwindowdialogclient.addControl(wTimeM = new GWindowEditControl(gwindowdialogclient, 11.5F, 7F, 2.0F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                bNumericOnly = true;
                bDelayedNotify = true;
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
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 7F, 1.3F, i18n("lType"), null));
        gwindowdialogclient.addControl(wType = new GWindowComboControl(gwindowdialogclient, 9F, 9F, 7F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                for(int i = 0; i < PAir.types.length; i++)
                    add(Plugin.i18n(PAir.types[i]));

                boolean aflag[] = new boolean[PAir.types.length];
                for(int j = 0; j < PAir.types.length; j++)
                    aflag[j] = true;

                posEnable = aflag;
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    if(_curPointType != iSelected)
                    {
                        PAir pair = (PAir)Plugin.builder.selectedPoint();
                        
                        //+++ TODO: 4.12 changed code +++
                        PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                        int k = pathair.pointIndx(pair);
                        //--- TODO: 4.12 changed code ---
                        
                        boolean flag = true;
                        if(_curPointType == 1 || _curPointType == 2)
                            flag = false;
                        boolean flag1 = true;
                        if(iSelected == 1 || iSelected == 2)
                            flag1 = false;
                        if(flag != flag1)
                            if(flag1)
                            {
                                pair.height = defaultHeight;
                                pair.speed = defaultSpeed;
                                clampSpeed(pair);
                                wHeight.setValue("" + (int)defaultHeight, false);
                                wSpeed.setValue("" + (int)pair.speed, false);
                            } else
                            {
                                wHeight.setValue("0", false);
                                wSpeed.setValue("0", false);
                                pair.height = 0.0D;
                                pair.speed = 0.0D;
                            }
                        _curPointType = iSelected;
                        pair.setType(iSelected);
                        fillDialogWay2(); //By PAL, new from 4.11                        
                        PlMission.setChanged();
                        
                        //+++ TODO: 4.12 changed code +++
//                        if(iSelected == 1 || iSelected == 2)
                        if(iSelected == 1 && k == 0 || iSelected == 2)
                        //--- TODO: 4.12 changed code ---
                        {
                            Airport airport = Airport.nearest(pair.pos.getAbsPoint(), -1, 7);
                            if(airport != null)
                                if(airport.nearestRunway(pair.pos.getAbsPoint(), PlMisAir.nearestRunway))
                                    pair.pos.setAbs(PlMisAir.nearestRunway.getPoint());
                                else
                                    pair.pos.setAbs(airport.pos.getAbsPoint());
                            pair.setTarget(null);
                            pair.sTarget = null;
                            wTarget.cap.set("");
                        } else
                        {
                            Actor actor = pair.getTarget();
                            if(actor != null && (iSelected == 0 && !(actor instanceof PAir) || iSelected == 3 && (actor instanceof PAir)))
                            {
                                pair.setTarget(null);
                                pair.sTarget = null;
                                wTarget.cap.set("");
                            }
                        }
                        
                        //+++ TODO: 4.12 changed code +++
//                        PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                        pathair = (PathAir)Plugin.builder.selectedPath();
                        //--- TODO: 4.12 changed code ---
                        
                        pathair.computeTimes();
                    }
                    return true;
                } else
                {
                    return false;
                }
            }

        }
);
        
        //+++ TODO: 4.12 changed code +++
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19F, 9F, 7F, 1.3F, i18n("Formation"), null));
        gwindowdialogclient.addControl(wFormations = new GWindowComboControl(gwindowdialogclient, 25F, 9F, 12F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                boolean aflag[] = new boolean[PAir.formations.length];
                for(int i = 0; i < PAir.formations.length; i++)
                {
                    add(Plugin.i18n(PAir.formations[i]));
                    aflag[i] = true;
                }

                posEnable = aflag;
            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    int k = wFormations.getSelected();
                    if(k < 0)
                        k = 0;
                    if(k > 7)
                        k = 7;
                    if(k == 0)
                        pair.formation = k;
                    else
                        pair.formation = k + 1;
                    PlMission.setChanged();
                    return true;
                } else
                {
                    return false;
                }
            }

        }
);
//--- TODO: 4.12 changed code ---
        
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 7F, 1.3F, i18n("RadioSilence"), null));
        gwindowdialogclient.addControl(wRadioSilence = new GWindowCheckBox(gwindowdialogclient, 9F, 11F, null) {

            public void preRender()
            {
                super.preRender();
                PAir pair = (PAir)Plugin.builder.selectedPoint();
                if(pair == null)
                {
                    return;
                } else
                {
                    setChecked(pair.bRadioSilence, false);
                    return;
                }
            }

            public boolean notify(int i, int j)
            {
                if(i != 2)
                {
                    return false;
                } else
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    pair.bRadioSilence = isChecked();
                    PlMission.setChanged();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13F, 7F, 1.3F, i18n("Target"), null));
        gwindowdialogclient.addLabel(wTarget = new GWindowLabel(gwindowdialogclient, 9F, 13F, 7F, 1.3F, "", null));
        gwindowdialogclient.addControl(wTargetSet = new GWindowButton(gwindowdialogclient, 1.0F, 15F, 5F, 1.6F, i18n("&Set"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                    Plugin.builder.beginSelectTarget();
                return false;
            }

        }
);
        gwindowdialogclient.addControl(wTargetClear = new GWindowButton(gwindowdialogclient, 9F, 15F, 5F, 1.6F, i18n("&Clear"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    PAir pair = (PAir)Plugin.builder.selectedPoint();
                    pair.setTarget(null);
                    pair.sTarget = null;
                    wTarget.cap.set("");
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
    }

    private void getTimeOut()
    {
        PAir pair = (PAir)builder.selectedPoint();
        PathAir pathair = (PathAir)builder.selectedPath();
        String s = wTimeH.getValue();
        double d = 0.0D;
        try
        {
            d = Double.parseDouble(s);
        }
        catch(Exception exception) { }
        if(d < 0.0D)
            d = 0.0D;
        if(d > 23D)
            d = 23D;
        s = wTimeM.getValue();
        double d1 = 0.0D;
        try
        {
            d1 = Double.parseDouble(s);
        }
        catch(Exception exception1) { }
        if(d1 < 0.0D)
            d1 = 0.0D;
        if(d1 > 59D)
            d1 = 59D;
        double d2 = (d * 60D + d1) * 60D - (double)Math.round(World.getTimeofDay() * 60F * 60F);
        if(d2 < 0.0D)
            d2 = 0.0D;
        int i = pathair.pointIndx(pair);
        if(i == 0)
        {
            if(pathair == Path.player)
                pair.time = 0.0D;
            else
                pair.time = d2;
        } else
        if(pair.type() != 2)
        {
            PPoint ppoint = pathair.point(i - 1);
            double d3 = d2 - ppoint.time;
            double d4 = 0.0D;
            if(d3 <= 0.0D)
            {
                d4 = type[pathair._iType].item[pathair._iItem].speedMax;
            } else
            {
                double d5 = ppoint.pos.getAbsPoint().distance(pair.pos.getAbsPoint());
                d4 = ((d5 / d3) * 3600D) / 1000D;
                if(d4 > type[pathair._iType].item[pathair._iItem].speedMax)
                    d4 = type[pathair._iType].item[pathair._iItem].speedMax;
                if(d4 < type[pathair._iType].item[pathair._iItem].speedMin)
                    d4 = type[pathair._iType].item[pathair._iItem].speedMin;
            }
            pair.speed = d4;
        }
        pathair.computeTimes();
        PlMission.setChanged();
    }

    private void fillEditSkin(int i)
    {
        PathAir pathair = (PathAir)builder.selectedPath();
        if(pathair == null)
        {
            return;
        } else
        {
            wSkills[i].setSelected(pathair.skills[i], true, false);
            return;
        }
    }

    private void fillSkins()
    {
        for(int i = 0; i < 4; i++)
        {
            wSkins[i].clear(false);
            wSkins[i].add(i18n("Default"));
        }

        String s;
        int j;
        s = Main.cur().netFileServerSkin.primaryPath();
        j = builder.wSelect.comboBox1.getSelected();
        if(j < startComboBox1 || j >= startComboBox1 + type.length)
            return;
        try
        {
            j -= startComboBox1;
            int k = builder.wSelect.comboBox2.getSelected();
            String s1 = GUIAirArming.validateFileName(type[j].item[k].name);
            File file = new File(HomePath.toFileSystemName(s + "/" + s1, 0));
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int l = 0; l < afile.length; l++)
                {
                    File file1 = afile[l];
                    if(file1.isFile())
                    {
                        String s2 = file1.getName();
                        String s3 = s2.toLowerCase();
                        if(s3.endsWith(".bmp") && s3.length() + s1.length() <= 122)
                        {
                            int i1 = BmpUtils.squareSizeBMP8Pal(s + "/" + s1 + "/" + s2);
                            if(i1 == 512 || i1 == 1024)
                            {
                                for(int j1 = 0; j1 < 4; j1++)
                                    wSkins[j1].add(s2);

                            } else
                            {
                                System.out.println("Skin " + s + "/" + s1 + "/" + s2 + " NOT loaded");
                            }
                        }
                    }
                }

            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return;
    }

    private void syncSkins()
    {
        if(!(builder.selectedPath() instanceof PathAir))
            return;
        PathAir pathair = (PathAir)builder.selectedPath();
        if(pathair == null)
            return;
        for(int i = 0; i < 4; i++)
            if(!syncComboControl(wSkins[i], pathair.skins[i]))
                pathair.skins[i] = null;

    }

    private void fillNoseart()
    {
        for(int i = 0; i < 4; i++)
        {
            wNoseart[i].clear(false);
            wNoseart[i].add(i18n("None"));
        }

        PathAir pathair = (PathAir)builder.selectedPath();
        if(pathair == null)
            return;
        Class class1 = type[pathair._iType].item[pathair._iItem].clazz;
        boolean flag = Property.intValue(class1, "noseart", 0) != 1;
        if(!flag)
        {
            int j = pathair.iRegiment;
            if(j < 0 && j >= regimentList.size())
            {
                flag = true;
            } else
            {
                Regiment regiment = (Regiment)regimentList.get(j);
                flag = !"us".equals(regiment.country());
            }
        }
        if(flag)
        {
            for(int k = 0; k < 4; k++)
                wNoseart[k].setEnable(false);

            return;
        }
        for(int l = 0; l < 4; l++)
            wNoseart[l].setEnable(true);

        try
        {
            String s = Main.cur().netFileServerNoseart.primaryPath();
            File file = new File(HomePath.toFileSystemName(s, 0));
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int i1 = 0; i1 < afile.length; i1++)
                {
                    File file1 = afile[i1];
                    if(file1.isFile())
                    {
                        String s1 = file1.getName();
                        String s2 = s1.toLowerCase();
                        if(s2.endsWith(".bmp") && s2.length() <= 122)
                            if(BmpUtils.checkBMP8Pal(s + "/" + s1, 256, 512))
                            {
                                for(int j1 = 0; j1 < 4; j1++)
                                    wNoseart[j1].add(s1);

                            } else
                            {
                                System.out.println("Noseart " + s + "/" + s1 + " NOT loaded");
                            }
                    }
                }

            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void syncNoseart()
    {
        PathAir pathair = (PathAir)builder.selectedPath();
        if(pathair == null)
            return;
        for(int i = 0; i < 4; i++)
            if(!syncComboControl(wNoseart[i], pathair.noseart[i]))
                pathair.noseart[i] = null;

    }

    private void fillPilots()
    {
        for(int i = 0; i < 4; i++)
        {
            wPilots[i].clear(false);
            wPilots[i].add(i18n("Default"));
        }

        try
        {
            String s = Main.cur().netFileServerPilot.primaryPath();
            File file = new File(HomePath.toFileSystemName(s, 0));
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int j = 0; j < afile.length; j++)
                {
                    File file1 = afile[j];
                    if(file1.isFile())
                    {
                        String s1 = file1.getName();
                        String s2 = s1.toLowerCase();
                        if(s2.endsWith(".bmp") && s2.length() <= 122)
                            if(BmpUtils.checkBMP8Pal(s + "/" + s1, 256, 256))
                            {
                                for(int k = 0; k < 4; k++)
                                    wPilots[k].add(s1);

                            } else
                            {
                                System.out.println("Pilot " + s + "/" + s1 + " NOT loaded");
                            }
                    }
                }

            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void syncPilots()
    {
        PathAir pathair = (PathAir)builder.selectedPath();
        if(pathair == null)
            return;
        for(int i = 0; i < 4; i++)
            if(!syncComboControl(wPilots[i], pathair.pilots[i]))
                pathair.pilots[i] = null;

    }

    private boolean syncComboControl(GWindowComboControl gwindowcombocontrol, String s)
    {
        if(s == null)
        {
            gwindowcombocontrol.setSelected(0, true, false);
            return true;
        }
        int i = gwindowcombocontrol.size();
        for(int j = 1; j < i; j++)
        {
            String s1 = gwindowcombocontrol.get(j);
            if(s.equals(s1))
            {
                gwindowcombocontrol.setSelected(j, true, false);
                return true;
            }
        }

        gwindowcombocontrol.setSelected(0, true, false);
        return false;
    }
/* Old:
    private void checkEditSkinTabs()
    {
        PathAir pathair = (PathAir)builder.selectedPath();
        int i = builder.wSelect.tabsClient.sizeTabs();
        if(pathair.planes == i - 3)
            return;
        if(pathair.planes > i - 3)
        {
            for(int j = i - 3; j < pathair.planes; j++)
            {
                builder.wSelect.tabsClient.addTab(j + 3, tabSkin[j]);
                fillEditSkin(j);
            }

        } else
        {
            int k = builder.wSelect.tabsClient.current;
            for(; builder.wSelect.tabsClient.sizeTabs() - 3 > pathair.planes; builder.wSelect.tabsClient.removeTab(builder.wSelect.tabsClient.sizeTabs() - 1));
            builder.wSelect.tabsClient.setCurrent(k, false);
            if(pathair == Path.player && pathair.planes >= Path.playerNum)
                Path.playerNum = 0;
        }
    }*/
    
//By PAL, new from 4.111
    private void checkEditSkinTabs()
    {
        PathAir pathair = (PathAir)builder.selectedPath();
        int i = builder.wSelect.tabsClient.sizeTabs();
        if(pathair.planes == i - 4)
            return;
        if(pathair.planes > i - 4)
        {
            for(int j = i - 4; j < pathair.planes; j++)
            {
                builder.wSelect.tabsClient.addTab(j + 4, tabSkin[j]);
                fillEditSkin(j);
            }

        } else
        {
            int k = builder.wSelect.tabsClient.current;
            for(; builder.wSelect.tabsClient.sizeTabs() - 4 > pathair.planes; builder.wSelect.tabsClient.removeTab(builder.wSelect.tabsClient.sizeTabs() - 1));
            builder.wSelect.tabsClient.setCurrent(k, false);
            if(pathair == Path.player && pathair.planes >= Path.playerNum)
                Path.playerNum = 0;
        }
    }

    private void checkEditSkinSkills()
    {
        PathAir pathair = (PathAir)builder.selectedPath();
        if(pathair == null)
            return;
        int i = pathair.skills[0];
        wSkills[0].setSelected(pathair.skills[0], true, false);
        pathair.skill = -2;
        for(int j = 1; j < pathair.planes; j++)
        {
            if(pathair.skills[j] != i)
                pathair.skill = -1;
            wSkills[j].setSelected(pathair.skills[j], true, false);
        }

        if(pathair.skill == -1)
        {
            wSkill.setValue(i18n("Custom"));
        } else
        {
            pathair.skill = i;
            wSkill.setSelected(pathair.skill, true, false);
        }
    }

    private void resetMesh()
    {
        for(int i = 0; i < 4; i++)
            resetMesh(i);

    }

    private void resetMesh(int i)
    {
        if(Actor.isValid(actorMesh[i]))
        {
            actorMesh[i].destroy();
            actorMesh[i] = null;
        }
    }

    private void checkMesh(int i)
    {
        if(Actor.isValid(actorMesh[i]))
            return;
        PathAir pathair = (PathAir)builder.selectedPath();
        if(pathair == null)
        {
            return;
        } else
        {
            Class class1 = type[pathair._iType].item[pathair._iItem].clazz;
            meshName = Aircraft.getPropertyMesh(class1, pathair.regiment().country());
            createMesh(i);
            PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(class1, pathair.regiment().country());
            paintscheme.prepare(class1, actorMesh[i].hierMesh(), pathair.regiment(), pathair.iSquadron, pathair.iWing, i, pathair.bNumberOn[i]);
            prepareSkin(i, class1, pathair.skins[i]);
            prepareNoseart(i, pathair.noseart[i]);
            preparePilot(i, pathair.pilots[i]);
            return;
        }
    }

    private void createMesh(int i)
    {
        if(Actor.isValid(actorMesh[i]))
            return;
        if(meshName == null)
        {
            return;
        } else
        {
            double d = 20D;
            actorMesh[i] = new ActorSimpleHMesh(meshName);
            d = actorMesh[i].hierMesh().visibilityR();
            actorMesh[i].pos.setAbs(new Orient(90F, 0.0F, 0.0F));
            d *= Math.cos(0.26179938779914941D) / Math.sin(((double)camera3D[i].FOV() * 3.1415926535897931D) / 180D / 2D);
            camera3D[i].pos.setAbs(new Point3d(d, 0.0D, 0.0D), new Orient(180F, 0.0F, 0.0F));
            camera3D[i].pos.reset();
            return;
        }
    }

/*Old:   private void prepareSkin(int i, Class class1, String s)
    {
        if(!Actor.isValid(actorMesh[i]))
            return;
        if(s != null)
        {
            s = Main.cur().netFileServerSkin.primaryPath() + "/" + GUIAirArming.validateFileName(Property.stringValue(class1, "keyName", null)) + "/" + s;
            String s1 = "PaintSchemes/Cache/" + Finger.file(0L, s, -1);
            Aircraft.prepareMeshSkin(meshName, actorMesh[i].hierMesh(), s, s1);
        } else
        {
            Aircraft.prepareMeshCamouflage(meshName, actorMesh[i].hierMesh());
        }
    }*/
    
//By PAL, from 4.111    
    private void prepareSkin(int i, Class class1, String s)
    {
        if(!Actor.isValid(actorMesh[i]))
            return;
        PathAir pathair = (PathAir)builder.selectedPath();
        if(s != null)
        {
            s = Main.cur().netFileServerSkin.primaryPath() + "/" + GUIAirArming.validateFileName(Property.stringValue(class1, "keyName", null)) + "/" + s;
            String s1 = "PaintSchemes/Cache/" + Finger.file(0L, s, -1);
            //By PAL, 4.11 only:
            Aircraft.prepareMeshSkin(meshName, actorMesh[i].hierMesh(), s, s1, pathair.regiment());
            //By PAL, from 4.101 version
            	//Aircraft.prepareMeshSkin(meshName, actorMesh[i].hierMesh(), s, s1);
        } else
        {
            //By PAL, 4.11 only:
            Aircraft.prepareMeshCamouflage(meshName, actorMesh[i].hierMesh(), class1, pathair.regiment());
            //By PAL, from 4.101 version
            	//Aircraft.prepareMeshCamouflage(meshName, actorMesh[i].hierMesh());
        }
    }  
    
    private void prepareNoseart(int i, String s)
    {
        if(!Actor.isValid(actorMesh[i]))
            return;
        if(s != null)
        {
            String s1 = Main.cur().netFileServerNoseart.primaryPath() + "/" + s;
            String s2 = s.substring(0, s.length() - 4);
            String s3 = "PaintSchemes/Cache/Noseart0" + s2 + ".tga";
            String s4 = "PaintSchemes/Cache/Noseart0" + s2 + ".mat";
            String s5 = "PaintSchemes/Cache/Noseart1" + s2 + ".tga";
            String s6 = "PaintSchemes/Cache/Noseart1" + s2 + ".mat";
            if(BmpUtils.bmp8PalTo2TGA4(s1, s3, s5))
                Aircraft.prepareMeshNoseart(actorMesh[i].hierMesh(), s4, s6, s3, s5, null);
        }
    }

    private void preparePilot(int i, String s)
    {
        if(!Actor.isValid(actorMesh[i]))
            return;
        if(s != null)
        {
            String s1 = Main.cur().netFileServerPilot.primaryPath() + "/" + s;
            String s2 = s.substring(0, s.length() - 4);
            String s3 = "PaintSchemes/Cache/Pilot" + s2 + ".tga";
            String s4 = "PaintSchemes/Cache/Pilot" + s2 + ".mat";
            if(BmpUtils.bmp8PalToTGA3(s1, s3))
                Aircraft.prepareMeshPilot(actorMesh[i].hierMesh(), 0, s4, s3, null);
        }
    }
    
//By PAL, new in 4.111

    public void dateChanged()
    {
        PathAir pathair = (PathAir)builder.selectedPath();
        if(pathair == null)
            return;
        for(int i = 0; i < 4; i++)
            resetMesh(i);

        fillEditActor(); //By PAL, only in v4.111
        PlMission.setChanged();
    }
    
    private void initEditSkin()
    {
        for(_planeIndx = 0; _planeIndx < 4; _planeIndx++)
        {
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)builder.wSelect.tabsClient.create(new GWindowDialogClient() {

                public void resized()
                {
                    super.resized();
                    wSkills[planeIndx].setSize(win.dx - wSkills[planeIndx].win.x - lookAndFeel().metric(1.0F), wSkills[planeIndx].win.dy);
                    wSkins[planeIndx].setSize(win.dx - wSkins[planeIndx].win.x - lookAndFeel().metric(1.0F), wSkins[planeIndx].win.dy);
                    wNoseart[planeIndx].setSize(win.dx - wNoseart[planeIndx].win.x - lookAndFeel().metric(1.0F), wNoseart[planeIndx].win.dy);
                    wPilots[planeIndx].setSize(win.dx - wPilots[planeIndx].win.x - lookAndFeel().metric(1.0F), wPilots[planeIndx].win.dy);
                    renders[planeIndx].setSize(win.dx - renders[planeIndx].win.x - lookAndFeel().metric(1.0F), win.dy - renders[planeIndx].win.y - lookAndFeel().metric(1.0F));
                }

                int planeIndx;

            
            {
                planeIndx = _planeIndx;
            }
            }
);
            tabSkin[_planeIndx] = builder.wSelect.tabsClient.createTab(i18n("Plane" + (1 + _planeIndx)), gwindowdialogclient);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, i18n("Player"), null));
            gwindowdialogclient.addControl(wPlayer[_planeIndx] = new GWindowCheckBox(gwindowdialogclient, 9F, 1.0F, null) {

                public void preRender()
                {
                    super.preRender();
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    
                    //+++ TODO: 4.12 changed code +++
//                    if(pathair == null)
//                    {
//                        return;
//                    } else
//                    {
//                        setChecked(pathair == Path.player && planeIndx == Path.playerNum, false);
//                        setEnable(type[pathair._iType].item[pathair._iItem].bEnablePlayer && !pathair.bOnlyAI);
//                        return;
//                    }
                    if(pathair == null)
                        return;
                    setChecked(pathair == Path.player && planeIndx == Path.playerNum, false);
                    setEnable(type[pathair._iType].item[pathair._iItem].bEnablePlayer && !pathair.bOnlyAI);
                    if(!isEnable() && isChecked())
                    {
                        setChecked(false, false);
                        Path.player = null;
                        Path.playerNum = 0;
                        PlMission.setChanged();
                    }
                    //--- TODO: 4.12 changed code ---
                    
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                        return false;
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    if(pathair == null)
                        return false;
                    if(isChecked())
                    {
                        Path.player = pathair;
                        Path.playerNum = planeIndx;
                        PAir pair = (PAir)pathair.point(0);
                        pair.time = 0.0D;
                        PlMission.cur.missionArmy = pathair.getArmy();
                        pathair.computeTimes();
                    } else
                    {
                        Path.player = null;
                        Path.playerNum = 0;
                    }
                    PlMission.setChanged();
                    return false;
                }

                int planeIndx;

            
            {
                planeIndx = _planeIndx;
            }
            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, i18n("Skill"), null));
            gwindowdialogclient.addControl(wSkills[_planeIndx] = new GWindowComboControl(gwindowdialogclient, 9F, 3F, 7F) {

                public void afterCreated()
                {
                    super.afterCreated();
                    setEditable(false);
                    add(Plugin.i18n("Rookie"));
                    add(Plugin.i18n("Average"));
                    add(Plugin.i18n("Veteran"));
                    add(Plugin.i18n("Ace"));
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                        pathair.skills[planeIndx] = getSelected();
                        checkEditSkinSkills();
                        PlMission.setChanged();
                        return false;
                    }
                }

                int planeIndx;

            
            {
                planeIndx = _planeIndx;
            }
            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, i18n("Skin"), null));
            gwindowdialogclient.addControl(wSkins[_planeIndx] = new GWindowComboControl(gwindowdialogclient, 9F, 5F, 7F) {

                public void afterCreated()
                {
                    super.afterCreated();
                    setEditable(false);
                    add(Plugin.i18n("Default"));
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                        return false;
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    if(getSelected() == 0)
                        pathair.skins[planeIndx] = null;
                    else
                        pathair.skins[planeIndx] = get(getSelected());
                    resetMesh(planeIndx);
                    PlMission.setChanged();
                    return false;
                }

                int planeIndx;

            
            {
                planeIndx = _planeIndx;
            }
            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 7F, 1.3F, i18n("Noseart"), null));
            gwindowdialogclient.addControl(wNoseart[_planeIndx] = new GWindowComboControl(gwindowdialogclient, 9F, 7F, 7F) {

                public void afterCreated()
                {
                    super.afterCreated();
                    setEditable(false);
                    add(Plugin.i18n("None"));
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                        return false;
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    if(getSelected() == 0)
                        pathair.noseart[planeIndx] = null;
                    else
                        pathair.noseart[planeIndx] = get(getSelected());
                    resetMesh(planeIndx);
                    PlMission.setChanged();
                    return false;
                }

                int planeIndx;

            
            {
                planeIndx = _planeIndx;
            }
            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 7F, 1.3F, i18n("Pilot"), null));
            gwindowdialogclient.addControl(wPilots[_planeIndx] = new GWindowComboControl(gwindowdialogclient, 9F, 9F, 7F) {

                public void afterCreated()
                {
                    super.afterCreated();
                    setEditable(false);
                    add(Plugin.i18n("Default"));
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                        return false;
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    if(getSelected() == 0)
                        pathair.pilots[planeIndx] = null;
                    else
                        pathair.pilots[planeIndx] = get(getSelected());
                    resetMesh(planeIndx);
                    PlMission.setChanged();
                    return false;
                }

                int planeIndx;

            
            {
                planeIndx = _planeIndx;
            }
            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 7F, 1.3F, i18n("NumberOn"), null));
            gwindowdialogclient.addControl(wPlayer[_planeIndx] = new GWindowCheckBox(gwindowdialogclient, 9F, 11F, null) {

                public void preRender()
                {
                    super.preRender();
                    PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                    if(pathair == null)
                    {
                        return;
                    } else
                    {
                        setChecked(pathair.bNumberOn[planeIndx], false);
                        return;
                    }
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                        pathair.bNumberOn[planeIndx] = isChecked();
                        resetMesh(planeIndx);
                        PlMission.setChanged();
                        return false;
                    }
                }

                int planeIndx;

            
            {
                planeIndx = _planeIndx;
            }
            }
);

            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13F, 7F, 1.3F, i18n("SpawnPoint"), null));
            gwindowdialogclient.addControl(wSpawnPointSet[_planeIndx] = new GWindowButton(gwindowdialogclient, 9F, 12.5F, 5F, 1.6F, i18n("&Set"), null) {

                public boolean notify(int i, int j)
                {
                	//+++ TODO: 4.12 changed code +++
//                    if(i == 2)
//                        Plugin.builder.beginSelectSpawnPoint();
                    if(i == 2)
                    {
                        Plugin.builder.beginSelectSpawnPoint();
                        bSpawnFromStationary = true;
                    }
                    //--- TODO: 4.12 changed code ---
                    
                    return false;
                }

            }
);
            gwindowdialogclient.addControl(wSpawnPointClear[_planeIndx] = new GWindowButton(gwindowdialogclient, 15F, 12.5F, 5F, 1.6F, i18n("&Clear"), null) {

                public boolean notify(int i, int j)
                {
                    if(i == 2)
                    {
                        PathAir pathair = (PathAir)Plugin.builder.selectedPath();
                        int k = getSelectedPlaneIndex();
                        pathair.setSpawnPoint(k, null);
                        
                        //+++ TODO: 4.12 changed code +++
                        bSpawnFromStationary = false;
                        int l = 0;
                        do
                        {
                            if(l >= 4)
                                break;
                            if(pathair.getSpawnPoint(l) != null)
                            {
                                bSpawnFromStationary = true;
                                break;
                            }
                            l++;
                        } while(true);
                        //--- TODO: 4.12 changed code ---
                        
                        wSpawnPointLabel[k].cap.set(Plugin.i18n("NotSet"));
                        PlMission.setChanged();
                    }
                    return false;
                }

            }
);
            gwindowdialogclient.addLabel(wSpawnPointLabel[_planeIndx] = new GWindowLabel(gwindowdialogclient, 21F, 13F, 15F, 1.3F, i18n("NotSet"), null));
            renders[_planeIndx] = new GUIRenders(gwindowdialogclient, 1.0F, 15F, 17F, 7F, true) {
            //renders[_planeIndx] = new GUIRenders(gwindowdialogclient, 1.0F, 13F, 17F, 7F, true) {

                public void mouseButton(int i, boolean flag, float f, float f1)
                {
                    super.mouseButton(i, flag, f, f1);
                    if(!flag)
                        return;
                    if(i == 1)
                    {
                        animateMeshA[planeIndx] = animateMeshT[planeIndx] = 0.0F;
                        if(Actor.isValid(actorMesh[planeIndx]))
                            actorMesh[planeIndx].pos.setAbs(new Orient(90F, 0.0F, 0.0F));
                    } else
                    if(i == 0)
                    {
                        f -= win.dx / 2.0F;
                        if(Math.abs(f) < win.dx / 16F)
                            animateMeshA[planeIndx] = 0.0F;
                        else
                            animateMeshA[planeIndx] = (-128F * f) / win.dx;
                        f1 -= win.dy / 2.0F;
                        if(Math.abs(f1) < win.dy / 16F)
                            animateMeshT[planeIndx] = 0.0F;
                        else
                            animateMeshT[planeIndx] = (-128F * f1) / win.dy;
                    }
                }

                int planeIndx;

            
            {
                planeIndx = _planeIndx;
            }
            }
;
            camera3D[_planeIndx] = new Camera3D();
            camera3D[_planeIndx].set(50F, 1.0F, 800F);
            render3D[_planeIndx] = new _Render3D(renders[_planeIndx].renders, 1.0F);
            render3D[_planeIndx].setCamera(camera3D[_planeIndx]);
            LightEnvXY lightenvxy = new LightEnvXY();
            render3D[_planeIndx].setLightEnv(lightenvxy);
            lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
            Vector3f vector3f = new Vector3f(-2F, 1.0F, -1F);
            vector3f.normalize();
            lightenvxy.sun().set(vector3f);
        }

    }

    public void freeResources()
    {
        resetMesh();
    }   

//By PAL, new from 4.11

    public int getSelectedPlaneIndex()
    {
        for(int i = 0; i < tabSkin.length; i++)
            if(tabSkin[i] != null && tabSkin[i].isCurrent())
                return i;

        return -1;
    }
    
    //+++ TODO: 4.12 changed code +++    
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
            if(hashmap.containsKey(regiment.branch()))
                continue;
            int k = regiment.getArmy();
            if(k < 0 || k > 2)
                continue;
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
    //--- TODO: 4.12 changed code ---    
    
//    static Class _mthclass$(String s)  //Added by PAL // Stripped by SAS~Storebror
//    {
//        java.lang.Class class1;
//        try
//        {
//            class1 = java.lang.Class.forName(s);
//        }
//        catch(java.lang.ClassNotFoundException classnotfoundexception)
//        {
//            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
//        }
//        return class1;
//    }

    protected Type type[];
    double defaultHeight;
    double defaultSpeed;
    private DefaultArmy defaultArmy[];
    private int iArmyRegimentList;
    
    //+++ TODO: 4.12 changed code +++
    private String sCountry;
    //--- TODO: 4.12 changed code ---
    
    private ArrayList regimentList;
    private ArrayList branchList; //By PAL
    private PlMission pluginMission;
    private int startComboBox1;
    ViewItem viewType[];
    HashMapInt viewMap;
    private String _actorInfo[];
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabActor;
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabWay;
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabSkin[];
    GWindowComboControl wArmy;
    GWindowComboControl wCountry;
    //+++ TODO: 4.12 changed code +++
    GWindowComboControl wRegiment;
    //--- TODO: 4.12 changed code ---
//    GWindowComboControl wBranch; //By PAL
    GWindowComboControl wSquadron;
    GWindowComboControl wWing;
    GWindowComboControl wWeapons;
    ArrayList lWeapons;
    GWindowEditControl wFuel;
    GWindowComboControl wPlanes;
    GWindowComboControl wSkill;
    GWindowCheckBox wOnlyAI;
    GWindowCheckBox wParachute;
    private Object _squads[];
    private Object _wings[];
    GWindowButton wPrev;
    GWindowButton wNext;
    GWindowLabel wCur;
    GWindowEditControl wHeight;
    GWindowEditControl wSpeed;
    GWindowEditControl wTimeH;
    GWindowEditControl wTimeM;
    GWindowComboControl wType;
    GWindowCheckBox wRadioSilence;
    GWindowLabel wTarget;
    GWindowButton wTargetSet;
    GWindowButton wTargetClear;
    //+++ TODO: 4.12 changed code +++
    GWindowComboControl wFormations;
    //--- TODO: 4.12 changed code ---
    private int _curPointType;
    private static Loc nearestRunway = new Loc();
    GWindowCheckBox wPlayer[];
    GWindowComboControl wSkills[];
    GWindowComboControl wSkins[];
    GWindowComboControl wNoseart[];
    GWindowComboControl wPilots[];
//By PAL, new from 4.11
    GWindowButton wSpawnPointSet[];
    GWindowButton wSpawnPointClear[];
    GWindowLabel wSpawnPointLabel[];
        
    GUIRenders renders[];
    Camera3D camera3D[];
    _Render3D render3D[];
    String meshName;
    ActorSimpleHMesh actorMesh[];
    float animateMeshA[] = {
        0.0F, 0.0F, 0.0F, 0.0F
    };
    float animateMeshT[] = {
        0.0F, 0.0F, 0.0F, 0.0F
    };
    private Orient _orient;
    //+++ TODO: 4.12 changed code +++
    protected boolean bSpawnFromStationary;
    //--- TODO: 4.12 changed code ---
    private int _planeIndx;
    //+++ TODO: 4.12 changed code +++
    private ArrayList listCountry[];
    private HashMap mapCountry[];
    //--- TODO: 4.12 changed code ---
//By PAL, frmo 4.11
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabWay2;
    GWindowComboControl wLandingType;
    GWindowLabel wLandingTypeLabel;
    GWindowComboControl wTakeOffType;
    GWindowLabel wTakeOffTypeLabel;
    GWindowEditControl wDelayTimer;
    GWindowLabel wDelayTimerLabel;
    GWindowLabel wDelayTimerLabel2;
    GWindowEditControl wTakeoffSpacing;
    GWindowLabel wTakeoffSpacingLabel;
    GWindowLabel wTakeoffSpacingLabel2;
    GWindowComboControl wNormFlyType;
    GWindowLabel wNormFlyTypeLabel;
    GWindowComboControl wCAPType;
    GWindowLabel wCAPTypeLabel;
    GWindowEditControl wCAPCycles;
    GWindowLabel wCAPCyclesLabel;
    GWindowEditControl wCAPTimer;
    GWindowLabel wCAPTimerLabel;
    GWindowLabel wCAPTimerLabelMin;
    GWindowEditControl wCAPOrient;
    GWindowLabel wCAPOrientLabel;
    GWindowLabel wCAPOrientLabelDeg;
    GWindowEditControl wCAPBSize;
    GWindowLabel wCAPBSizeLabel;
    GWindowLabel wCAPBSizeLabelKM;
    GWindowEditControl wCAPAltDifference;
    GWindowLabel wCAPAltDifferenceLabel;
    GWindowLabel wCAPAltDifferenceLabelM;
    GWindowLabel wGAttackNoOptionInfoLabel;    
    
        private String PlaceholderLabel = "air.Placeholder"; //By PAL    

    static 
    {
        Property.set(com.maddox.il2.builder.PlMisAir.class, "name", "MisAir");
    }

}