package com.maddox.il2.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.IniFile;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.SharedTokenizer;

public class GUIQuick extends GameState
{
    public static class DirFilter
        implements FilenameFilter
    {

        public boolean accept(File file, String s)
        {
            File file1 = new File(file, s);
            return file1.isDirectory();
        }

        public DirFilter()
        {
        }
    }

    public class IOState
    {

        public void getMap()
        {
            String as[] = {
                "Okinawa", "CoralSeaOnline", "Net8Islands", "Smolensk", "Moscow", "Crimea", "Kuban", "Bessarabia", "MTO", "Slovakia_winter", 
                "Slovakia_summer"
            };
            int i = Integer.parseInt(map);
            String s = as[i];
            map = s;
            wMap.setValue(I18N.map(s));
            mapChanged();
        }

        public void save()
        {
            our = OUR;
            scramble = bScramble;
            situation = wSituation.getSelected();
            map = getMapName();
            target = wTarget.getSelected();
            defence = wDefence.getSelected();
            altitude = wAltitude.getValue();
            pos = wPos.getValue();
            weather = wWeather.getSelected();
            cldheight = wCldHeight.getValue();
            timeH = wTimeHour.getSelected();
            timeM = wTimeMins.getSelected();
            noneTARGET = wLevel.getSelected();
        }

        public void loaded()
        {
            OUR = our;
            wMap.setValue(I18N.map(map));
            mapChanged();
            wSituation.setSelected(situation, true, false);
            wTarget.setSelected(target, true, false);
            wDefence.setSelected(defence, true, false);
            bScramble = scramble;
            wPos.setValue(pos);
            validateEditableComboControl(wPos);
            wAltitude.setValue(altitude);
            int i = Integer.parseInt(wAltitude.getValue());
            if(i < 100)
            {
                if(situation != 0)
                    pos = "700";
                else
                    pos = "0";
                cldheight = "2000";
                altitude = wAltitude.get(i);
                wAltitude.setValue(altitude);
                scramble = false;
            }
            validateEditableComboControl(wAltitude);
            wCldHeight.setValue(cldheight);
            validateEditableComboControl(wCldHeight);
            wWeather.setSelected(weather, true, false);
            wTimeHour.setSelected(timeH, true, false);
            wTimeMins.setSelected(timeM, true, false);
            wLevel.setSelected(noneTARGET, true, false);
            if(target == 0)
                noneTarget = true;
            else
                noneTarget = false;
        }

        public boolean our;
        public boolean scramble;
        public int situation;
        public String map;
        public int target;
        public int defence;
        public String altitude;
        public String pos;
        public int weather;
        public String cldheight;
        public int timeH;
        public int timeM;
        public int noneTARGET;

        public IOState()
        {
        }
    }

    class WComboNum extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if(k < 0)
                {
                    return true;
                } else
                {
                    wing[indx].planes = indx == 0 ? k + 1 : k;
                    return true;
                }
            } else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WComboNum(int i, GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
            indx = i;
        }
    }

    class WComboSkill extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if(k < 0)
                {
                    return true;
                } else
                {
                    wing[indx].skill = k;
                    return true;
                }
            } else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WComboSkill(int i, GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
            indx = i;
        }
    }

    class WComboPlane extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if(k < 0)
                    return true;
                ItemAir itemair;
                if(indx == 0)
                    itemair = (ItemAir)(bPlaneArrestor ? playerPlaneC : playerPlane).get(k);
                else
                    itemair = (ItemAir)(bPlaneArrestor ? aiPlaneC : aiPlane).get(k);
                String s = fillComboWeapon(dlg[indx].wLoadout, itemair, 0);
                wing[indx].setPlane(k);
                boolean flag = false;
                if(indx == 0)
                {
                    wing[indx].fromUserCfg();
                    String as[] = Aircraft.getWeaponsRegistered(wing[indx].plane.clazz);
                    int l = 0;
                    do
                    {
                        if(l >= as.length)
                            break;
                        if(as[l].equals(wing[indx].weapon))
                        {
                            fillComboWeapon(dlg[indx].wLoadout, wing[indx].plane, l);
                            flag = true;
                            break;
                        }
                        l++;
                    } while(true);
                }
                if(!flag)
                    wing[indx].weapon = s;
                return true;
            } else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WComboPlane(int i, GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
            indx = i;
        }
    }

    class WComboLoadout extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if(k < 0)
                    return true;
                wing[indx].setWeapon(k);
                if(indx == 0)
                    wing[indx].toUserCfg();
                return true;
            } else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WComboLoadout(int i, GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
            indx = i;
        }
    }

    class WButtonArming extends GUIButton
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                if(wing[indx].planes == 0)
                {
                    return true;
                } else
                {
                    indxAirArming = indx;
                    wing[indx].toAirArming();
                    GUIAirArming.stateId = 4;
                    Main.stateStack().push(55);
                    return true;
                }
            } else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WButtonArming(int i, GWindow gwindow, GTexture gtexture, float f, float f1, float f2, 
                float f3)
        {
            super(gwindow, gtexture, f, f1, f2, f3);
            indx = i;
        }
    }

    static class ItemDlg
    {

        public WComboNum wNum;
        public WComboSkill wSkill;
        public WComboPlane wPlane;
        public WComboLoadout wLoadout;
        public WButtonArming bArming;

        ItemDlg()
        {
        }
    }

    public class ItemWing
    {

        public String getPlane()
        {
            return plane.name;
        }

        public void setPlane(String s)
        {
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
            else
                arraylist = indx == 0 ? playerPlane : aiPlane;
            int i = 0;
            do
            {
                if(i >= arraylist.size())
                    break;
                plane = (ItemAir)arraylist.get(i);
                if(plane.name.equals(s))
                    break;
                i++;
            } while(true);
        }

        public void loaded()
        {
            dlg[indx].wNum.setSelected(indx == 0 ? planes - 1 : planes, true, false);
            dlg[indx].wSkill.setSelected(skill, true, false);
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
            else
                arraylist = indx == 0 ? playerPlane : aiPlane;
            int i = 0;
            do
            {
                if(i >= arraylist.size())
                    break;
                if(plane == arraylist.get(i))
                {
                    dlg[indx].wPlane.setSelected(i, true, false);
                    break;
                }
                i++;
            } while(true);
            String as[] = Aircraft.getWeaponsRegistered(plane.clazz);
            int j = 0;
            do
            {
                if(j >= as.length)
                    break;
                if(as[j].equals(weapon))
                {
                    fillComboWeapon(dlg[indx].wLoadout, plane, j);
                    break;
                }
                j++;
            } while(true);
            if(indx == 0)
                toUserCfg();
        }

        public void toUserCfg()
        {
            if(indx != 0)
            {
                return;
            } else
            {
                UserCfg usercfg = World.cur().userCfg;
                usercfg.setSkin(plane.name, skin[0]);
                usercfg.setNoseart(plane.name, noseart[0]);
                usercfg.netPilot = pilot[0];
                usercfg.setWeapon(plane.name, weapon);
                usercfg.netNumberOn = numberOn[0];
                return;
            }
        }

        public void fromUserCfg()
        {
            if(indx != 0)
            {
                return;
            } else
            {
                UserCfg usercfg = World.cur().userCfg;
                skin[0] = usercfg.getSkin(plane.name);
                noseart[0] = usercfg.getNoseart(plane.name);
                pilot[0] = usercfg.netPilot;
                weapon = usercfg.getWeapon(plane.name);
                numberOn[0] = usercfg.netNumberOn;
                return;
            }
        }
        
        public String prepareWing(SectFile sectfile) {
            return prepareWing(sectfile, true);
        }

        public String prepareWing(SectFile sectfile, boolean hasEightSections)
        {
            String s;
            if(indx < (hasEightSections?8:4))
            {
                s = OUR ? r01 : g01;
                if(!hasEightSections || indx < 4)
                    s = s + "0" + iwing;
                else
                    s = s + "1" + iwing;
            } else
            {
                s = OUR ? g01 : r01;
                if(!hasEightSections || indx < 12)
                    s = s + "0" + iwing;
                else
                    s = s + "1" + iwing;
            }
            int i = sectfile.sectionIndex("Wing");
            if(planes == 0)
            {
                sectfile.lineRemove(i, sectfile.varIndex(i, s));
                sectfile.sectionRemove(sectfile.sectionIndex(s));
                sectfile.sectionRemove(sectfile.sectionIndex(s + "_Way"));
                return null;
            }
            String s1 = null;
            int j;
            if(regiment != null)
            {
                if(hasEightSections && indx > 3 && indx < 8 || indx > 11)
                    s1 = regiment + "1" + iwing;
                else
                    s1 = regiment + "0" + iwing;
//                System.out.println("TEST: " + s);
                sectfile.lineRemove(i, sectfile.varIndex(i, s));
                sectfile.lineAdd(i, s1);
                sectfile.sectionRename(sectfile.sectionIndex(s + "_Way"), s1 + "_Way");
                sectfile.sectionRename(sectfile.sectionIndex(s), s1);
                j = sectfile.sectionIndex(s1);
            } else
            {
                j = sectfile.sectionIndex(s);
                s1 = s;
            }
            sectfile.sectionClear(j);
            sectfile.lineAdd(j, "Planes " + planes);
            sectfile.lineAdd(j, "Skill " + skill);
            sectfile.lineAdd(j, "Class " + plane.className);
            sectfile.lineAdd(j, "Fuel " + fuel);
            if(weapon != null)
                sectfile.lineAdd(j, "weapons " + weapon);
            else
                sectfile.lineAdd(j, "weapons default");
            for(int k = 0; k < planes; k++)
            {
                if(skin[k] != null)
                    sectfile.lineAdd(j, "skin" + k + " " + skin[k]);
                if(noseart[k] != null)
                    sectfile.lineAdd(j, "noseart" + k + " " + noseart[k]);
                if(pilot[k] != null)
                    sectfile.lineAdd(j, "pilot" + k + " " + pilot[k]);
                if(!numberOn[k])
                    sectfile.lineAdd(j, "numberOn" + k + " 0");
            }

            return s1;
        }

        public void prepereWay(SectFile sectfile, String as[], String as1[])
        {
            int i = sectfile.sectionIndex(as1[indx] + "_Way");
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                SharedTokenizer.set(sectfile.line(i, k));
                String s = SharedTokenizer.next("");
                String s1 = s + " " + SharedTokenizer.next("") + " " + SharedTokenizer.next("") + " ";
                String s2 = wAltitude.getValue();
                if(wSituation.getSelected() != 0)
                {
                    int l = 500;
                    try
                    {
                        l = Integer.parseInt(wAltitude.getValue());
                    }
                    catch(Exception exception) { }
                    if(wSituation.getSelected() == 1)
                    {
                        if(indx <= 7)
                            l += Integer.parseInt(wPos.getValue());
                    } else
                    if(indx > 7)
                        l += Integer.parseInt(wPos.getValue());
                    s2 = "" + l;
                }
                SharedTokenizer.next("");
                float f = (float)SharedTokenizer.next((plane.speedMin + plane.speedMax) / 2D, plane.speedMin, plane.speedMax);
                if("TAKEOFF".equals(s) || "LANDING".equals(s))
                {
                    s2 = "0";
                    f = 0.0F;
                }
                String s3 = SharedTokenizer.next((String)null);
                String s4 = SharedTokenizer.next((String)null);
                if(s3 != null && (GUIQuick.class$com$maddox$il2$objects$air$TypeTransport == null ? (GUIQuick.class$com$maddox$il2$objects$air$TypeTransport = GUIQuick._mthclass$("com.maddox.il2.objects.air.TypeTransport")) : GUIQuick.class$com$maddox$il2$objects$air$TypeTransport).isAssignableFrom(plane.clazz))
                    s3 = null;
                if(s3 != null)
                {
                    int i1 = 0;
                    do
                    {
                        if(i1 >= 8)
                            break;
                        if(s3.equals(as[i1]))
                        {
                            s3 = as1[i1];
                            break;
                        }
                        i1++;
                    } while(true);
                }
                if(s3 != null)
                {
                    if(s4 != null)
                        sectfile.line(i, k, s1 + s2 + " " + f + " " + s3 + " " + s4);
                    else
                        sectfile.line(i, k, s1 + s2 + " " + f + " " + s3);
                } else
                {
                    sectfile.line(i, k, s1 + s2 + " " + f);
                }
            }

        }

        public void setPlane(int i)
        {
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
            else
                arraylist = indx == 0 ? playerPlane : aiPlane;
            plane = (ItemAir)arraylist.get(i);
            for(int j = 0; j < 4; j++)
            {
                skin[j] = null;
                noseart[j] = null;
            }

        }

        public void setWeapon(int i)
        {
            String as[] = Aircraft.getWeaponsRegistered(plane.clazz);
            weapon = as[i];
        }

        public void toAirArming()
        {
            GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
            guiairarming.quikPlayer = indx == 0;
            if(indx < 8)
                guiairarming.quikArmy = OUR ? 1 : 2;
            else
                guiairarming.quikArmy = OUR ? 2 : 1;
            guiairarming.quikPlanes = planes;
            guiairarming.quikPlane = plane.name;
            guiairarming.quikWeapon = weapon;
            guiairarming.quikCurPlane = 0;
            guiairarming.quikRegiment = regiment;
            guiairarming.quikWing = iwing;
            guiairarming.quikFuel = fuel;
            for(int i = 0; i < 4; i++)
            {
                guiairarming.quikSkin[i] = skin[i];
                guiairarming.quikNoseart[i] = noseart[i];
                guiairarming.quikPilot[i] = pilot[i];
                guiairarming.quikNumberOn[i] = numberOn[i];
            }

            guiairarming.quikListPlane.clear();
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
            else
                arraylist = indx == 0 ? playerPlane : aiPlane;
            for(int j = 0; j < arraylist.size(); j++)
                guiairarming.quikListPlane.add(((ItemAir)arraylist.get(j)).clazz);

        }

        public void fromAirArming()
        {
            GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
            ItemAir itemair = null;
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
            else
                arraylist = indx == 0 ? playerPlane : aiPlane;
            int i = 0;
            do
            {
                if(i >= arraylist.size())
                    break;
                itemair = (ItemAir)arraylist.get(i);
                if(itemair.name.equals(guiairarming.quikPlane))
                    break;
                i++;
            } while(true);
            plane = itemair;
            weapon = guiairarming.quikWeapon;
            regiment = guiairarming.quikRegiment;
            fuel = guiairarming.quikFuel;
            for(int j = 0; j < 4; j++)
            {
                skin[j] = guiairarming.quikSkin[j];
                noseart[j] = guiairarming.quikNoseart[j];
                pilot[j] = guiairarming.quikPilot[j];
                numberOn[j] = guiairarming.quikNumberOn[j];
            }

            loaded();
        }

        public int indx;
        public int planes;
        public ItemAir plane;
        public String weapon;
        public String regiment;
        public int iwing;
        public String skin[] = {
            null, null, null, null
        };
        public String noseart[] = {
            null, null, null, null
        };
        public String pilot[] = {
            null, null, null, null
        };
        public boolean numberOn[] = {
            true, true, true, true
        };
        public int fuel;
        public int skill;

        public ItemWing(int i)
        {
            indx = 0;
            planes = 0;
            plane = null;
            weapon = "default";
            regiment = null;
            iwing = 0;
            fuel = 100;
            skill = 1;
            if(i == 0)
                planes = 1;
            indx = i;
            iwing = i % 4;
            if(indx == 0)
                plane = (ItemAir)(bPlaneArrestor ? playerPlaneC : playerPlane).get(0);
            else
                plane = (ItemAir)(bPlaneArrestor ? aiPlaneC : aiPlane).get(0);
            if(indx <= 7)
                regiment = r01;
            else
                regiment = g01;
        }
    }

    static class ItemAir
    {

        public String name;
        public String className;
        public Class clazz;
        public boolean bEnablePlayer;
        public double speedMin;
        public double speedMax;

        public ItemAir(String s, Class class1, String s1)
        {
            speedMin = 200D;
            speedMax = 500D;
            name = s;
            clazz = class1;
            className = s1;
            bEnablePlayer = Property.containsValue(class1, "cockpitClass");
            String s2 = Property.stringValue(class1, "FlightModel", null);
            if(s2 != null)
            {
                SectFile sectfile = FlightModelMain.sectFile(s2);
                speedMin = sectfile.get("Params", "Vmin", (float)speedMin);
                speedMax = sectfile.get("Params", "VmaxH", (float)speedMax);
            }
        }
    }

    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
//            System.out.println("notify(" + gwindow.hashCode() + ", " + i + ", " + j + ")");
            validateEditableComboControl(wCldHeight);
            validateEditableComboControl(wPos);
            validateEditableComboControl(wAltitude);
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bArmy)
            {
                if(OUR)
                    OUR = false;
                else
                    OUR = true;
                onArmyChange();
                if(r01.equals("usa01"))
                    Main3D.menuMusicPlay(OUR ? "us" : "ja");
                else
                    Main3D.menuMusicPlay(OUR ? "ru" : "de");
                for(int k = 0; k < 16; k++)
                    if(k <= 7)
                        wing[k].regiment = OUR ? r01 : g01;
                    else
                        wing[k].regiment = OUR ? g01 : r01;

                return true;
            }
            if(gwindow == wTarget)
            {
                if(wTarget.getSelected() == 4)
                    bScramble = true;
                else
                    bScramble = false;
                if(wTarget.getSelected() == 0)
                {
                    noneTarget = true;
                    wLevel.showWindow();
                } else
                {
                    noneTarget = false;
                    wLevel.hideWindow();
                }
                return true;
            }
            if(gwindow == bExit)
            {
                GUIQuick.setQMB(false);
                save(true);
                Main.stateStack().pop();
                return true;
            }
            if(gwindow == bDiff)
            {
                Main.stateStack().push(17);
                return true;
            }
            if(gwindow == bFly)
                if(bNoAvailableMissions)
                {
                    return true;
                } else
                {
                    validateEditableComboControl(wCldHeight);
                    validateEditableComboControl(wPos);
                    validateEditableComboControl(wAltitude);
                    startQuickMission();
                    return true;
                }
            if(gwindow == bReset)
            {
                setDefaultValues();
                return true;
            }
            if(gwindow == bStat)
            {
                Main.stateStack().push(71);
                return true;
            }
            if(gwindow == bNext)
            {
                bFirst = false;
                showHide();
                return true;
            }
            if(gwindow == bBack)
            {
                bFirst = true;
                showHide();
                return true;
            }
            if(gwindow == bLoad)
            {
                ssect = null;
                Main.stateStack().push(25);
                return true;
            }
            if(gwindow == bSave)
            {
                validateEditableComboControl(wCldHeight);
                validateEditableComboControl(wPos);
                validateEditableComboControl(wAltitude);
                save(false);
                Main.stateStack().push(24);
                return true;
            }
            if(gwindow == wPlaneList)
            {
                IniFile inifile = Config.cur.ini;
                inifile.set("QMB", "PlaneList", wPlaneList.getSelected());
                GUIQuick.setPlaneList(wPlaneList.getSelected());
                fillArrayPlanes();
                for(int l = 0; l < 16; l++)
                {
                    fillComboPlane(dlg[l].wPlane, l == 0);
                    int i1 = dlg[l].wPlane.getSelected();
                    if(i1 == 0)
                        dlg[l].wPlane.setSelected(1, false, false);
                    dlg[l].wPlane.setSelected(0, true, true);
                }

                return true;
            }
            if(gwindow == wMap)
            {
//                System.out.println("notify(" + gwindow.hashCode() + ", " + i + ", " + j + ") is wMap!");
                onArmyChange();
//                System.out.println("notify(" + gwindow.hashCode() + ", " + i + ", " + j + ") going to mapChanged!");
                mapChanged();
                if(Main.cur() != null)
                {
                    Main.cur();
                    if(Main.stateStack() != null)
                    {
                        Main.cur();
                        if(Main.stateStack().peek() != null)
                        {
                            Main.cur();
                            if(Main.stateStack().peek().id() == 14)
                                if(r01.equals("usa01"))
                                    Main3D.menuMusicPlay(OUR ? "us" : "ja");
                                else
                                    Main3D.menuMusicPlay(OUR ? "ru" : "de");
                        }
                    }
                }
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            setCanvasColorWHITE();
            GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(630F), x1024(924F), 2.5F);
            GUISeparate.draw(this, GColor.Gray, x1024(150F), y1024(640F), 1.0F, x1024(80F));
            GUISeparate.draw(this, GColor.Gray, x1024(720F), y1024(640F), 1.0F, x1024(80F));
            GUISeparate.draw(this, GColor.Gray, x1024(875F), y1024(640F), 1.0F, x1024(80F));
            GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(640F), 1.0F, x1024(46F));
            GUISeparate.draw(this, GColor.Gray, x1024(567F), y1024(640F), 1.0F, x1024(46F));
            GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(686F), x1024(30F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(537F), y1024(686F), x1024(30F), 2.0F);
            setCanvasFont(0);
            draw(x1024(0.0F), y1024(633F), x1024(170F), M(2.0F), 1, Localize("quick.BAC"));
            draw(x1024(143F), y1024(633F), x1024(170F), M(2.0F), 1, Localize("quick.LOD"));
            draw(x1024(285F), y1024(633F), x1024(170F), M(2.0F), 1, Localize("quick.SAV"));
            if(!bNoAvailableMissions)
                draw(x1024(427F), y1024(633F), x1024(170F), M(2.0F), 1, Localize("quick.FLY"));
            draw(x1024(569F), y1024(633F), x1024(170F), M(2.0F), 1, Localize("quick.DIF"));
            if(bFirst)
            {
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(500F), x1024(924F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(285F), x1024(924F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(130F), x1024(94F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(317F), x1024(94F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(110F), 2.0F, x1024(56F));
                GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(317F), 2.0F, x1024(28F));
                draw(x1024(710F), y1024(633F), x1024(170F), M(2.0F), 1, Localize("quick.RES"));
                draw(x1024(853F), y1024(633F), x1024(170F), M(2.0F), 1, Localize("quick.GFC"));
                setCanvasFont(1);
                draw(x1024(38F), y1024(13F), x1024(602F), M(2.0F), 0, Localize("quick.YOU"));
                draw(x1024(38F), y1024(108F), x1024(602F), M(2.0F), 0, Localize("quick.FRI"));
                draw(x1024(38F), y1024(291F), x1024(602F), M(2.0F), 0, Localize("quick.ENM"));
                setCanvasFont(0);
                draw(x1024(48F), y1024(38F), x1024(82F), M(2.0F), 0, Localize("quick.NUM"));
                draw(x1024(144F), y1024(38F), x1024(160F), M(2.0F), 0, Localize("quick.SKI"));
                draw(x1024(318F), y1024(38F), x1024(274F), M(2.0F), 0, Localize("quick.PLA"));
                draw(x1024(606F), y1024(38F), x1024(196F), M(2.0F), 0, Localize("quick.TNT"));
                draw(x1024(48F), y1024(143F), x1024(82F), M(2.0F), 0, Localize("quick.NUM"));
                draw(x1024(144F), y1024(143F), x1024(160F), M(2.0F), 0, Localize("quick.SKI"));
                draw(x1024(318F), y1024(143F), x1024(274F), M(2.0F), 0, Localize("quick.PLA"));
                draw(x1024(606F), y1024(143F), x1024(196F), M(2.0F), 0, Localize("quick.TNT"));
                draw(x1024(48F), y1024(320F), x1024(82F), M(2.0F), 0, Localize("quick.NUM"));
                draw(x1024(144F), y1024(320F), x1024(160F), M(2.0F), 0, Localize("quick.SKI"));
                draw(x1024(318F), y1024(320F), x1024(274F), M(2.0F), 0, Localize("quick.PLA"));
                draw(x1024(606F), y1024(320F), x1024(196F), M(2.0F), 0, Localize("quick.TNT"));
                draw(x1024(606F), y1024(508F), x1024(192F), M(2.0F), 0, Localize("quick.MAP"));
                draw(x1024(606F), y1024(542F), x1024(192F), M(2.0F), 0, Localize("quick.PLALST"));
                draw(x1024(318F), y1024(508F), x1024(100F), M(2.0F), 0, Localize("quick.ALT"));
                draw(x1024(318F), y1024(542F), x1024(100F), M(2.0F), 0, Localize("quick.SIT"));
                draw(x1024(318F), y1024(576F), x1024(100F), M(2.0F), 0, Localize("quick.POS"));
                draw(x1024(48F), y1024(508F), x1024(100F), M(2.0F), 0, Localize("quick.TAR"));
                draw(x1024(48F), y1024(576F), x1024(100F), M(2.0F), 0, Localize("quick.DEF"));
                if(noneTarget)
                    draw(x1024(48F), y1024(542F), x1024(100F), M(2.0F), 0, Localize("quick.+/-"));
                draw(x1024(320F), y1024(118F), x1024(528F), y1024(32F), 2, Localize("quick.ASET"));
                draw(x1024(320F), y1024(298F), x1024(528F), y1024(32F), 2, Localize("quick.ASET"));
                setCanvasFont(0);
                if(OUR)
                    draw(x1024(566F), y1024(21F), x1024(362F), M(2.0F), 2, Localize("quick.SEL_Allies"));
                else
                    draw(x1024(566F), y1024(21F), x1024(362F), M(2.0F), 2, Localize("quick.SEL_Axis"));
            } else
            {
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(500F), x1024(924F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(215F), x1024(924F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(288F), x1024(94F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(232F), 2.0F, x1024(112F));
                setCanvasFont(0);
                draw(x1024(318F), y1024(508F), x1024(120F), M(2.0F), 0, Localize("quick.WEA"));
                draw(x1024(606F), y1024(508F), x1024(120F), M(2.0F), 0, Localize("quick.CLD"));
                draw(x1024(48F), y1024(508F), x1024(100F), M(2.0F), 0, Localize("quick.TIM"));
                draw(x1024(213F), y1024(508F), x1024(8F), M(2.0F), 1, ":");
                draw(x1024(853F), y1024(633F), x1024(170F), M(2.0F), 1, Localize("quick.STAT"));
                draw(x1024(48F), y1024(37F), x1024(82F), M(2.0F), 0, Localize("quick.NUM"));
                draw(x1024(144F), y1024(37F), x1024(160F), M(2.0F), 0, Localize("quick.SKI"));
                draw(x1024(318F), y1024(37F), x1024(274F), M(2.0F), 0, Localize("quick.PLA"));
                draw(x1024(606F), y1024(37F), x1024(196F), M(2.0F), 0, Localize("quick.TNT"));
                draw(x1024(48F), y1024(320F), x1024(82F), M(2.0F), 0, Localize("quick.NUM"));
                draw(x1024(144F), y1024(320F), x1024(160F), M(2.0F), 0, Localize("quick.SKI"));
                draw(x1024(318F), y1024(320F), x1024(274F), M(2.0F), 0, Localize("quick.PLA"));
                draw(x1024(606F), y1024(320F), x1024(196F), M(2.0F), 0, Localize("quick.TNT"));
                draw(x1024(320F), y1024(274F), x1024(528F), y1024(32F), 2, Localize("quick.ASET"));
                setCanvasFont(1);
                draw(x1024(38F), y1024(13F), x1024(602F), M(2.0F), 0, Localize("quick.ADDFRI"));
                draw(x1024(38F), y1024(291F), x1024(602F), M(2.0F), 0, Localize("quick.ADDENM"));
            }
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            bExit.setPosC(x1024(85F), y1024(689F));
            bBack.setPosC(x1024(85F), y1024(689F));
            bLoad.setPosC(x1024(228F), y1024(689F));
            bSave.setPosC(x1024(370F), y1024(689F));
            bFly.setPosC(x1024(512F), y1024(689F));
            bStat.setPosC(x1024(939F), y1024(689F));
            bReset.setPosC(x1024(796F), y1024(689F));
            bArmy.setPosC(x1024(966F), y1024(39F));
            bNext.setPosC(x1024(939F), y1024(689F));
            bDiff.setPosC(x1024(654F), y1024(689F));
            wAltitude.setPosSize(x1024(432F), y1024(508F), x1024(160F), M(1.7F));
            wSituation.setPosSize(x1024(432F), y1024(542F), x1024(160F), M(1.7F));
            wPos.setPosSize(x1024(432F), y1024(576F), x1024(160F), M(1.7F));
            wTarget.setPosSize(x1024(142F), y1024(508F), x1024(160F), M(1.7F));
            wLevel.setPosSize(x1024(142F), y1024(542F), x1024(160F), M(1.7F));
            wDefence.setPosSize(x1024(142F), y1024(576F), x1024(160F), M(1.7F));
            wMap.setPosSize(x1024(688F), y1024(508F), x1024(256F), M(1.7F));
            wPlaneList.setPosSize(x1024(688F), y1024(542F), x1024(256F), M(1.7F));
            wWeather.setPosSize(x1024(432F), y1024(508F), x1024(160F), M(1.7F));
            wCldHeight.setPosSize(x1024(784F), y1024(508F), x1024(160F), M(1.7F));
            wTimeHour.setPosSize(x1024(132F), y1024(508F), x1024(80F), M(1.7F));
            wTimeMins.setPosSize(x1024(222F), y1024(508F), x1024(80F), M(1.7F));
            dlg[0].wNum.setPosSize(x1024(48F), y1024(70F), x1024(80F), M(1.7F));
            dlg[0].wSkill.setPosSize(x1024(144F), y1024(70F), x1024(160F), M(1.7F));
            dlg[0].wPlane.setPosSize(x1024(318F), y1024(70F), x1024(274F), M(1.7F));
            dlg[0].wLoadout.setPosSize(x1024(609F), y1024(70F), x1024(332F), M(1.7F));
            dlg[0].bArming.setPosC(x1024(959F), y1024(84F));
            for(int i = 0; i < 7; i++)
                if(i < 3)
                {
                    dlg[i + 1].wNum.setPosSize(x1024(48F), y1024(175 + 34 * i), x1024(80F), M(1.7F));
                    dlg[i + 1].wSkill.setPosSize(x1024(144F), y1024(175 + 34 * i), x1024(160F), M(1.7F));
                    dlg[i + 1].wPlane.setPosSize(x1024(318F), y1024(175 + 34 * i), x1024(274F), M(1.7F));
                    dlg[i + 1].wLoadout.setPosSize(x1024(609F), y1024(175 + 34 * i), x1024(332F), M(1.7F));
                    dlg[i + 1].bArming.setPosC(x1024(959F), y1024((175 + 34 * i + 16) - 2));
                } else
                {
                    dlg[i + 1].wNum.setPosSize(x1024(48F), y1024(104 + 34 * (i - 4)), x1024(80F), M(1.7F));
                    dlg[i + 1].wSkill.setPosSize(x1024(144F), y1024(104 + 34 * (i - 4)), x1024(160F), M(1.7F));
                    dlg[i + 1].wPlane.setPosSize(x1024(318F), y1024(104 + 34 * (i - 4)), x1024(274F), M(1.7F));
                    dlg[i + 1].wLoadout.setPosSize(x1024(609F), y1024(104 + 34 * (i - 4)), x1024(332F), M(1.7F));
                    dlg[i + 1].bArming.setPosC(x1024(959F), y1024((104 + 34 * (i - 4) + 16) - 2));
                }

            for(int j = 0; j < 8; j++)
                if(j < 4)
                {
                    dlg[j + 8].wNum.setPosSize(x1024(48F), y1024(352 + 34 * j), x1024(80F), M(1.7F));
                    dlg[j + 8].wSkill.setPosSize(x1024(144F), y1024(352 + 34 * j), x1024(160F), M(1.7F));
                    dlg[j + 8].wPlane.setPosSize(x1024(318F), y1024(352 + 34 * j), x1024(274F), M(1.7F));
                    dlg[j + 8].wLoadout.setPosSize(x1024(609F), y1024(352 + 34 * j), x1024(332F), M(1.7F));
                    dlg[j + 8].bArming.setPosC(x1024(959F), y1024((352 + 34 * j + 16) - 2));
                } else
                {
                    dlg[j + 8].wNum.setPosSize(x1024(48F), y1024(352 + 34 * (j - 4)), x1024(80F), M(1.7F));
                    dlg[j + 8].wSkill.setPosSize(x1024(144F), y1024(352 + 34 * (j - 4)), x1024(160F), M(1.7F));
                    dlg[j + 8].wPlane.setPosSize(x1024(318F), y1024(352 + 34 * (j - 4)), x1024(274F), M(1.7F));
                    dlg[j + 8].wLoadout.setPosSize(x1024(609F), y1024(352 + 34 * (j - 4)), x1024(332F), M(1.7F));
                    dlg[j + 8].bArming.setPosC(x1024(959F), y1024((352 + 34 * (j - 4) + 16) - 2));
                }

        }

        public DialogClient()
        {
        }
    }

    public class byI18N_name
        implements Comparator
    {

        public int compare(Object obj, Object obj1)
        {
            if(RTSConf.cur.locale.getLanguage().equals("ru"))
            {
                return collator.compare(I18N.plane(((ItemAir)obj).name), I18N.plane(((ItemAir)obj1).name));
            } else
            {
                Collator collator1 = Collator.getInstance(RTSConf.cur.locale);
                collator1.setStrength(1);
                collator1.setDecomposition(2);
                return collator1.compare(I18N.plane(((ItemAir)obj).name), I18N.plane(((ItemAir)obj1).name));
            }
        }

        public byI18N_name()
        {
        }
    }


    /*private*/ void defaultRegiments()
    {
        r01 = "r01";
        r010 = "r010";
        g01 = "g01";
        g010 = "g010";
        try
        {
            SectFile sectfile = new SectFile(currentMissionName, 0);
            if(sectfile.sectionIndex("r0100") < 0)
            {
                r01 = "usa01";
                r010 = "usa010";
                g01 = "ja01";
                g010 = "ja010";
            }
            for(int i = 0; i < 8; i++)
                if(OUR)
                {
                    wing[i].regiment = r01;
                    wing[i + 8].regiment = g01;
                } else
                {
                    wing[i].regiment = g01;
                    wing[i + 8].regiment = r01;
                }

        }
        catch(Exception exception)
        {
            System.out.println("WARNING: No quick missions in Missions folder.");
        }
    }

    private void mapChanged()
    {
        int i = wMap.getSelected();
//        System.out.println("mapChanged wMap.getSelected() = " + i);
        if(i >= 0 && i < _mapKey.length && !bNoAvailableMissions)
        {
            ArrayList arraylist = getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[wTarget.getSelected()] + (wTarget.getSelected() != 0 ? "" : noneTargetSuffix[wLevel.getSelected()]));
            // TODO: +++ Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! +++
            if (arraylist.size() < 1) arraylist = getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[wTarget.getSelected()]);
            // TODO: --- Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! ---
//            System.out.println("arraylist.size() = " + arraylist.size());
            Random random = new Random();
            if(arraylist.size() < 1)
                return;
            int j = random.nextInt(arraylist.size());
            String s = PREFIX + getMapName() + "/" + (String)arraylist.get(j);
            currentMissionName = s;
//            System.out.println("currentMissionName = " + s);
            SectFile sectfile = new SectFile(currentMissionName, 0);
            if(sectfile.sectionIndex("r0100") >= 0)
            {
//                System.out.println("has r0100!");
                r01 = "r01";
                r010 = "r010";
                g01 = "g01";
                g010 = "g010";
                for(int k = 0; k < 16; k++) {
                    if(k < 8)
                    {
                        if(wing[k].regiment.equals("usa01"))
                            wing[k].regiment = "r01";
                    } else {
                        if(wing[k].regiment.equals("ja01"))
                            wing[k].regiment = "g01";
                    }
                }

            } else
            {
//                System.out.println("has no r0100!");
                r01 = "usa01";
                r010 = "usa010";
                g01 = "ja01";
                g010 = "ja010";
                for(int l = 0; l < 16; l++)
                {
                    if(l < 8)
                    {
                        if(wing[l].regiment.equals("r01"))
                            wing[l].regiment = "usa01";
                    } else {
                        if(wing[l].regiment.equals("g01"))
                            wing[l].regiment = "ja01";
                    }
                }

            }
            String s1 = sectfile.get("MAIN", "MAP", (String)null);
            if(s1 != null)
            {
                try {
                    IniFile inifile = new IniFile("maps/" + s1, 0);
                    String s2 = inifile.get("WORLDPOS", "CAMOUFLAGE", "SUMMER");
                    if(World.cur() != null)
                        World.cur().setCamouflage(s2);
                } catch (Exception e) {
                    // At game start with online server set by parameter, there is no Quick GUI map selectable yet, so it's normal for this to fail.
                    // We just bail out here...
                    return;
                }
            }
        }
    }

    public void enterPush(GameState gamestate)
    {
        World.cur().diffUser.set(World.cur().userCfg.singleDifficulty);
        if(exisstFile("quicks/.last.quick"))
        {
            GUIQuickLoad guiquickload = (GUIQuickLoad)GameState.get(25);
            guiquickload.execute(".last", true);
            load();
        }
        wing[0].fromUserCfg();
        mapChanged();
        if(r01.equals("usa01"))
            Main3D.menuMusicPlay(OUR ? "us" : "ja");
        else
            Main3D.menuMusicPlay(OUR ? "ru" : "de");
        _enter();
    }

    public void enterPop(GameState gamestate)
    {
        if(gamestate.id() == 17)
        {
            World.cur().userCfg.singleDifficulty = World.cur().diffUser.get();
            World.cur().userCfg.saveConf();
        } else
        if(gamestate.id() == 55)
            wing[indxAirArming].fromAirArming();
        else
        if(gamestate.id() == 25)
        {
            wPlaneList.setSelected(0, true, false);
            setPlaneList(wPlaneList.getSelected());
            fillArrayPlanes();
            for(int i = 0; i < 16; i++)
            {
                fillComboPlane(dlg[i].wPlane, i == 0);
                int j = dlg[i].wPlane.getSelected();
                if(j == 0)
                    dlg[i].wPlane.setSelected(1, false, false);
                dlg[i].wPlane.setSelected(0, true, true);
            }

            load();
        }
        _enter();
    }

    public void _enter()
    {
        Main.cur().currentMissionFile = null;
        setQMB(true);
        String s = "users/" + World.cur().userCfg.sId + "/QMB.ini";
        if(!exisstFile(s))
            initStat();
        client.activateWindow();
    }

    public void _leave()
    {
        World.cur().userCfg.saveConf();
        client.hideWindow();
    }

    private static int getPlaneList()
    {
        return pl;
    }

    private static void setPlaneList(int i)
    {
        pl = i;
    }

    static boolean isQMB()
    {
        return bIsQuick;
    }

    static void setQMB(boolean flag)
    {
        bIsQuick = flag;
    }

    static void initStat()
    {
        String s = "users/" + World.cur().userCfg.sId + "/QMB.ini";
        SectFile sectfile = new SectFile(s, 1, false, World.cur().userCfg.krypto());
        String s1 = "MAIN";
        sectfile.sectionAdd(s1);
        int i = sectfile.sectionIndex(s1);
        float f = 0.0F;
        int j = 0;
        sectfile.lineAdd(i, "qmbTotalScore", "" + j);
        sectfile.lineAdd(i, "qmbTotalAirKill", "" + j);
        sectfile.lineAdd(i, "qmbTotalGroundKill", "" + j);
        sectfile.lineAdd(i, "qmbTotalBulletsFired", "" + j);
        sectfile.lineAdd(i, "qmbTotalBulletsHitAir", "" + j);
        sectfile.lineAdd(i, "qmbTotalBulletsFiredHitGround", "" + j);
        sectfile.lineAdd(i, "qmbTotalPctAir", "" + f);
        sectfile.lineAdd(i, "qmbTotalPctGround", "" + f);
        sectfile.lineAdd(i, "qmbTotalTankKill", "" + j);
        sectfile.lineAdd(i, "qmbTotalCarKill", "" + j);
        sectfile.lineAdd(i, "qmbTotalArtilleryKill", "" + j);
        sectfile.lineAdd(i, "qmbTotalAAAKill", "" + j);
        sectfile.lineAdd(i, "qmbTotalTrainKill", "" + j);
        sectfile.lineAdd(i, "qmbTotalShipKill", "" + j);
        sectfile.lineAdd(i, "qmbTotalAirStaticKill", "" + j);
        sectfile.lineAdd(i, "qmbTotalBridgeKill", "" + j);
        sectfile.lineAdd(i, "qmbTotalPara", "" + j);
        sectfile.lineAdd(i, "qmbTotalDead", "" + j);
        sectfile.lineAdd(i, "qmbTotalBombsFired", "" + j);
        sectfile.lineAdd(i, "qmbTotalBombsHit", "" + j);
        sectfile.lineAdd(i, "qmbTotalRocketsFired", "" + j);
        sectfile.lineAdd(i, "qmbTotalRocketsHit", "" + j);
        sectfile.lineAdd(i, "qmbTotalPctBomb", "" + f);
        sectfile.lineAdd(i, "qmbTotalPctRocket", "" + f);
        sectfile.saveFile();
    }

    private static boolean exisstFile(String s)
    {
        try
        {
            SFSInputStream sfsinputstream = new SFSInputStream(s);
            sfsinputstream.close();
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }

    private void validateEditableComboControl(GWindowComboControl gwindowcombocontrol)
    {
        String s = gwindowcombocontrol.getValue();
        if(s.equals(""))
        {
            gwindowcombocontrol.setSelected(0, true, false);
            s = gwindowcombocontrol.get(0);
        }
        int i = Integer.parseInt(s);
        if(i < Integer.parseInt(gwindowcombocontrol.get(0)))
        {
            gwindowcombocontrol.setSelected(0, true, false);
            i = Integer.parseInt(gwindowcombocontrol.getValue());
        }
        if(i > Integer.parseInt(gwindowcombocontrol.get(gwindowcombocontrol.size() - 1)))
        {
            gwindowcombocontrol.setSelected(gwindowcombocontrol.size() - 1, true, false);
            i = Integer.parseInt(gwindowcombocontrol.getValue());
        }
        for(int j = 0; j < gwindowcombocontrol.size(); j++)
            if(i == Integer.parseInt(gwindowcombocontrol.get(j)))
                gwindowcombocontrol.setSelected(j, true, false);

    }

    public void startQuickMission()
    {
//        System.out.println("getMapName() = " + getMapName());
//        System.out.println("getArmyString() = " + getArmyString());
//        System.out.println("wTarget.getSelected() = " + wTarget.getSelected());
//        System.out.println("wLevel.getSelected() = " + wLevel.getSelected());
//        System.out.println("_targetKey[wTarget.getSelected()] = " + _targetKey[wTarget.getSelected()]);
//        System.out.println("noneTargetSuffix[wLevel.getSelected()] = " + noneTargetSuffix[wLevel.getSelected()]);
        ArrayList arraylist = getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[wTarget.getSelected()] + (wTarget.getSelected() != 0 ? "" : noneTargetSuffix[wLevel.getSelected()]));
        // TODO: +++ Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! +++
        if (arraylist.size() < 1) arraylist = getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[wTarget.getSelected()]);
        // TODO: --- Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! ---
        Random random = new Random();
        int i = random.nextInt(arraylist.size());
        String s = PREFIX + getMapName() + "/" + (String)arraylist.get(i);
        try
        {
            SectFile sectfile = new SectFile(s, 0);
            sectfile.set("MAIN", "TIME", wTimeHour.getValue() + "." + wTimeMins.getSelected() * 25);
            // TODO: +++ Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! +++
            boolean hasEightSections = true;
            // TODO: --- Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! ---
            for(int j = 0; j < 8; j++)
            {
                String s1;
                if(j < 4)
                    s1 = r010 + Character.forDigit(j, 10);
                else
                    s1 = r01 + "1" + Character.forDigit(j - 4, 10);
                if(!sectfile.exist("Wing", s1))
                    // TODO: +++ Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! +++
//                    throw new Exception("Section Red " + s1 + " not found");
                {
                    hasEightSections = false;
                    break;
                }
                    // TODO: --- Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! ---
            }

            for(int k = 0; k < 8; k++)
            {
                String s2;
                if(k < 4)
                    s2 = g010 + Character.forDigit(k, 10);
                else
                    s2 = g01 + "1" + Character.forDigit(k - 4, 10);
                if(!sectfile.exist("Wing", s2))
                    // TODO: +++ Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! +++
//                    throw new Exception("Section Blue " + s2 + " not found");
                  {
                      hasEightSections = false;
                      break;
                  }
                  // TODO: --- Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! ---
            }

            sectfile.set("MAIN", "CloudType", wWeather.getSelected());
            sectfile.set("MAIN", "CloudHeight", Integer.parseInt(wCldHeight.getValue()));
 
            // TODO: +++ Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! +++
//            String as[] = new String[16];
//            String as1[] = new String[16];
//            for(int l = 0; l < 16; l++)
//            {
//                if(l < 8)
//                    as[l] = (OUR ? r01 : g01) + (l >= 4 ? "1" : "0") + l;
//                else
//                    as[l] = (OUR ? g01 : r01) + (l >= 12 ? "1" : "0") + l;
//                as1[l] = wing[l].prepareWing(sectfile);
//            }
//
//            if(as1[0] != null)
//                sectfile.set("MAIN", "player", as1[0]);
//            else
//                sectfile.set("MAIN", "player", as[0]);
//            for(int i1 = 0; i1 < 16; i1++)
//                if(as1[i1] != null)
//                    wing[i1].prepereWay(sectfile, as, as1);
            
            int totalSections = hasEightSections?16:8;
            
            String as[] = new String[totalSections];
            String as1[] = new String[totalSections];
            for(int l = 0; l < totalSections; l++)
            {
                if(l < (totalSections/2))
                    as[l] = (OUR ? r01 : g01) + (hasEightSections?(l >= 4 ? "1" : "0"):"0") + l;
                else
                    as[l] = (OUR ? g01 : r01) + (hasEightSections?(l >= 12 ? "1" : "0"):"0") + l;
                as1[l] = wing[l].prepareWing(sectfile, hasEightSections);
            }

            if(as1[0] != null)
                sectfile.set("MAIN", "player", as1[0]);
            else
                sectfile.set("MAIN", "player", as[0]);
            for(int i1 = 0; i1 < totalSections; i1++)
                if(as1[i1] != null)
                    wing[i1].prepereWay(sectfile, as, as1);
            // TODO: --- Modified by SAS~Storebror: Make QMB compatible with old style 4-section mission files! ---

            if(wDefence.getSelected() == 0)
            {
                for(int j1 = 0; j1 < 2; j1++)
                {
                    String s3 = j1 == 0 ? "Stationary" : "NStationary";
                    int i2 = sectfile.sectionIndex(s3);
                    if(i2 < 0)
                        continue;
                    sectfile.sectionRename(i2, "Stationary_Temp");
                    sectfile.sectionAdd(s3);
                    int k2 = sectfile.sectionIndex(s3);
                    int i3 = sectfile.vars(i2);
                    for(int j3 = 0; j3 < i3; j3++)
                    {
                        SharedTokenizer.set(sectfile.line(i2, j3));
                        String s6 = null;
                        if(j1 == 1)
                            s6 = SharedTokenizer.next("");
                        String s8 = SharedTokenizer.next("");
                        int l3 = SharedTokenizer.next(0);
                        String s10 = null;
                        if(s6 != null)
                            s10 = s6 + " " + s8 + " " + l3 + " " + SharedTokenizer.getGap();
                        else
                            s10 = s8 + " " + l3 + " " + SharedTokenizer.getGap();
                        if(l3 == 0)
                        {
                            sectfile.lineAdd(k2, s10);
                            continue;
                        }
                        if(l3 == 1 && OUR && !bScramble)
                        {
                            sectfile.lineAdd(k2, s10);
                            continue;
                        }
                        if(l3 == 2 && !OUR && !bScramble)
                        {
                            sectfile.lineAdd(k2, s10);
                            continue;
                        }
                        try
                        {
                            Class class1 = ObjIO.classForName(s8);
                            if((com.maddox.il2.objects.vehicles.artillery.AAA.class).isAssignableFrom(class1))
                                continue;
                            if(s8.startsWith("ships."))
                            {
                                SharedTokenizer.set(sectfile.line(i2, j3));
                                if(j1 == 1)
                                    s6 = SharedTokenizer.next("");
                                String s12 = SharedTokenizer.next("");
                                String s13 = SharedTokenizer.next("");
                                String s14 = SharedTokenizer.next("");
                                String s15 = SharedTokenizer.next("");
                                String s16 = SharedTokenizer.next("");
                                String s17 = SharedTokenizer.next("");
                                /*String s18 = */SharedTokenizer.next("");
                                String s19 = SharedTokenizer.next("");
                                if(j1 == 1)
                                    sectfile.lineAdd(k2, s6 + " " + s12 + " " + s13 + " " + s14 + " " + s15 + " " + s16 + " " + s17 + " " + 5940 + " " + s19);
                                else
                                    sectfile.lineAdd(k2, s12 + " " + s13 + " " + s14 + " " + s15 + " " + s16 + " " + s17 + " " + 5940 + " " + s19);
                            } else
                            {
                                sectfile.lineAdd(k2, s10);
                            }
                        }
                        catch(Throwable throwable) { }
                    }

                    sectfile.sectionRemove(i2);
                }

                int k1 = sectfile.sectionIndex("Chiefs");
                if(k1 >= 0)
                {
                    sectfile.sectionRename(k1, "Chiefs_Temp");
                    sectfile.sectionAdd("Chiefs");
                    int l1 = sectfile.sectionIndex("Chiefs");
                    int j2 = sectfile.vars(k1);
                    for(int l2 = 0; l2 < j2; l2++)
                    {
                        String s4 = sectfile.line(k1, l2);
                        SharedTokenizer.set(s4);
                        String s5 = SharedTokenizer.next("");
                        String s7 = SharedTokenizer.next("");
                        if(s7.startsWith("Ships."))
                        {
                            int k3 = SharedTokenizer.next(0);
                            if(k3 == 0)
                            {
                                sectfile.lineAdd(l1, s4);
                                continue;
                            }
                            if(k3 == 1 && OUR && !bScramble)
                            {
                                sectfile.lineAdd(l1, s4);
                                continue;
                            }
                            if(k3 == 2 && !OUR && !bScramble)
                            {
                                sectfile.lineAdd(l1, s4);
                            } else
                            {
                                /*String s9 = */SharedTokenizer.next("");
                                String s11 = SharedTokenizer.next("");
                                sectfile.lineAdd(l1, s5 + " " + s7 + " " + k3 + " " + 5940 + " " + s11);
                            }
                        } else
                        {
                            sectfile.lineAdd(l1, s4);
                        }
                    }

                    sectfile.sectionRemove(k1);
                }
            }
            GUIQuickStats.resetMissionStat();
            Main.cur().currentMissionFile = sectfile;
            Main.stateStack().push(5);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            System.out.println(">> no file: " + s + "");
            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Error", "Data file corrupt 1", 3, 0.0F);
            return;
        }
    }

    public void save(boolean flag)
    {
        try
        {
            ssect = new SectFile();
            ssect.sectionAdd("states");
            ioState.save();
            ssect.set("states", "head", ioState, false);
            for(int i = 0; i < 16; i++)
                ssect.set("states", "wing" + i, wing[i], false);

        }
        catch(Exception exception)
        {
            System.out.println("sorry, cant save");
            new GWindowMessageBox(client, 20F, true, "Error", "Can't save data file", 3, 0.0F);
        }
        GUIQuickSave guiquicksave = (GUIQuickSave)GameState.get(24);
        guiquicksave.sect = ssect;
        if(flag)
            guiquicksave.execute(".last", false);
    }

    public void load()
    {
        setDefaultValues();
        byte byte0 = 0;
        byte byte1 = 16;
        if(ssect == null)
            return;
        try
        {
            ssect.get("states", "head", ioState);
            if(ssect.get("states", "wing8", wing[8]) == null)
            {
                byte0 = 4;
                byte1 = 8;
                ioState.getMap();
            }
            for(int i = 0; i < byte1; i++)
            {
                byte byte2 = 0;
                if(byte0 > 0 && i > 3)
                    byte2 = 4;
                ssect.get("states", "wing" + i, wing[i + byte2]);
            }

        }
        catch(Exception exception)
        {
            new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt 2", 3, 0.0F);
        }
        onArmyChange();
        setShow(bFirst && noneTarget, wLevel);
    }

    private boolean checkCustomAirIni(String s)
    {
        SectFile sectfile = new SectFile(s);
        if(sectfile.sections() <= 0)
            return false;
        SectFile sectfile1 = new SectFile("com/maddox/il2/objects/air.ini");
        int i = sectfile.vars(0);
        for(int j = 0; j < i; j++)
            if(sectfile1.varExist(0, sectfile.var(0, j)))
                return true;

        return false;
    }

    public void fillArrayPlanes()
    {
        playerPlane.clear();
        playerPlaneC.clear();
        aiPlane.clear();
        aiPlaneC.clear();
        boolean flag = false;
        String s;
        if(getPlaneList() < 2)
        {
            s = "com/maddox/il2/objects/air.ini";
            if(getPlaneList() == 1)
                flag = true;
        } else
        if(checkCustomAirIni("Missions/Quick/QMBair_" + getPlaneList() + ".ini"))
        {
            s = "Missions/Quick/QMBair_" + getPlaneList() + ".ini";
        } else
        {
            s = "com/maddox/il2/objects/air.ini";
            wPlaneList.setSelected(0, true, false);
        }
        SectFile sectfile = new SectFile(s, 0);
        SectFile sectfile1 = new SectFile("com/maddox/il2/objects/air.ini");
//        boolean flag1 = false;
        int j = sectfile.sections();
        if(j <= 0)
            throw new RuntimeException("GUIQuick: file '" + s + "' is empty");
        for(int k = 0; k < j; k++)
        {
            int l = sectfile.vars(k);
            for(int i1 = 0; i1 < l; i1++)
            {
                String s1 = sectfile.var(k, i1);
                if(!sectfile1.varExist(0, s1))
                    continue;
                int i = sectfile1.varIndex(0, s1);
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile1.value(k, i));
                String s2 = numbertokenizer.next((String)null);
                boolean flag2 = true;
                do
                {
                    if(!numbertokenizer.hasMoreTokens())
                        break;
                    if(!"NOQUICK".equals(numbertokenizer.next()))
                        continue;
                    flag2 = false;
                    break;
                } while(true);
                if(!flag2)
                    continue;
                Class class1 = null;
                try
                {
                    class1 = ObjIO.classForName(s2);
                }
                catch(Exception exception)
                {
                    System.out.println("GUIQuick: class '" + s2 + "' not found");
                    break;
                }
                ItemAir itemair = new ItemAir(s1, class1, s2);
                if(itemair.bEnablePlayer)
                {
                    playerPlane.add(itemair);
                    if(AirportCarrier.isPlaneContainsArrestor(class1))
                        playerPlaneC.add(itemair);
                }
                aiPlane.add(itemair);
                if(AirportCarrier.isPlaneContainsArrestor(class1))
                    aiPlaneC.add(itemair);
            }

        }

        if(flag)
        {
            Collections.sort(playerPlane, new byI18N_name());
            Collections.sort(playerPlaneC, new byI18N_name());
            Collections.sort(aiPlane, new byI18N_name());
            Collections.sort(aiPlaneC, new byI18N_name());
        }
    }

    public void fillComboPlane(GWindowComboControl gwindowcombocontrol, boolean flag)
    {
        gwindowcombocontrol.clear();
        ArrayList arraylist = null;
        if(bPlaneArrestor)
            arraylist = flag ? playerPlaneC : aiPlaneC;
        else
            arraylist = flag ? playerPlane : aiPlane;
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            ItemAir itemair = (ItemAir)arraylist.get(j);
            gwindowcombocontrol.add(I18N.plane(itemair.name));
        }

        gwindowcombocontrol.setSelected(0, true, false);
    }

    public String fillComboWeapon(GWindowComboControl gwindowcombocontrol, ItemAir itemair, int i)
    {
        gwindowcombocontrol.clear();
        Class class1 = itemair.clazz;
        String as[] = Aircraft.getWeaponsRegistered(class1);
        if(as != null && as.length > 0)
        {
            for(int j = 0; j < as.length; j++)
            {
                String s = as[j];
                gwindowcombocontrol.add(I18N.weapons(itemair.name, s));
            }

            gwindowcombocontrol.setSelected(i, true, false);
        }
        return as[i];
    }

    public String Localize(String s)
    {
        return I18N.gui(s);
    }

    public GUIQuick(GWindowRoot gwindowroot)
    {
        super(14);
        russianMixedRules = "< ',' < '.' < '-' <\u0430,\u0410< a,A <\u0431,\u0411< b,B <\u0432,\u0412< v,V <\u0433,\u0413< g,G <\u0434,\u0414< d,D <\u0435,\u0415 < \u0451,\u0401 < \u0436,\u0416 < \u0437,\u0417< z,Z <\u0438,\u0418< i,I <\u0439,\u0419< j,J <\u043A,\u041A< k,K <\u043B,\u041B< l,L <\u043C,\u041C< m,M <\u043D,\u041D< n,N <\u043E,\u041E< o,O <\u043F,\u041F< p,P <\u0440,\u0420< r,R <\u0441,\u0421< s,S <\u0442,\u0422< t,T <\u0443,\u0423< u,U <\u0444,\u0424< f,F <\u0445,\u0425< h,H <\u0446,\u0426< c,C <\u0447,\u0427 < \u0448,\u0428 < \u0449,\u0429 < \u044A,\u042A < \u044B,\u042B< i,I <\u044C,\u042C < \u044D,\u042D< e,E <\u044E,\u042E < \u044F,\u042F< q,Q < x,X < y,Y";
        currentMissionName = "";
        bNoAvailableMissions = false;
        try
        {
            collator = new RuleBasedCollator(russianMixedRules);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        bFirst = true;
        OUR = true;
        bScramble = false;
        noneTarget = true;
        pl = Config.cur.ini.get("QMB", "PlaneList", 0, 0, 3);
        if(Config.cur.ini.get("QMB", "DumpPlaneList", 0, 0, 1) > 0)
            dumpFullPlaneList();
        File file = new File("Missions/Quick/QMBair_" + pl + ".ini");
        if(pl > 1)
            if(!file.exists())
                pl = 0;
            else
            if(!checkCustomAirIni("Missions/Quick/QMBair_" + pl + ".ini"))
                pl = 0;
        r01 = "r01";
        r010 = "r010";
        g01 = "g01";
        g010 = "g010";
        playerPlane = new ArrayList();
        aiPlane = new ArrayList();
        playerPlaneC = new ArrayList();
        aiPlaneC = new ArrayList();
        wing = new ItemWing[16];
        dlg = new ItemDlg[16];
        ioState = new IOState();
        bPlaneArrestor = false;
        fillArrayPlanes();
        for(int i = 0; i < 16; i++)
            wing[i] = new ItemWing(i);

        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = Localize("quick.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        GTexture gtexture1 = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons;
        bArmy = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 0.0F, 48F, 48F));
        bReset = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bBack = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bLoad = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bSave = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bFly = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        bNext = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bStat = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bDiff = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        wSituation = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wMap = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wPlaneList = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wTarget = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wPos = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wDefence = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wAltitude = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wWeather = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCldHeight = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wTimeHour = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wTimeMins = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wLevel = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wLevel.add("=");
        wLevel.add("+");
        wLevel.add("-");
        wLevel.setEditable(false);
        wLevel.setSelected(0, true, false);
        wLevel.posEnable = (new boolean[] {
            true, true, true
        });
        wAltitude.add("100");
        wAltitude.add("150");
        wAltitude.add("200");
        wAltitude.add("250");
        wAltitude.add("300");
        wAltitude.add("400");
        wAltitude.add("500");
        wAltitude.add("750");
        wAltitude.add("1000");
        wAltitude.add("1500");
        wAltitude.add("2000");
        wAltitude.add("3000");
        wAltitude.add("5000");
        wAltitude.add("7500");
        wAltitude.add("10000");
        wAltitude.setEditable(true);
        wAltitude.setNumericOnly(true);
        wAltitude.setSelected(8, true, false);
        wPos.add("0");
        wPos.add("500");
        wPos.add("1000");
        wPos.add("2000");
        wPos.add("3000");
        wPos.setEditable(true);
        wPos.setNumericOnly(true);
        wPos.setSelected(0, true, false);
        wTimeHour.add("00");
        wTimeHour.add("01");
        wTimeHour.add("02");
        wTimeHour.add("03");
        wTimeHour.add("04");
        wTimeHour.add("05");
        wTimeHour.add("06");
        wTimeHour.add("07");
        wTimeHour.add("08");
        wTimeHour.add("09");
        wTimeHour.add("10");
        wTimeHour.add("11");
        wTimeHour.add("12");
        wTimeHour.add("13");
        wTimeHour.add("14");
        wTimeHour.add("15");
        wTimeHour.add("16");
        wTimeHour.add("17");
        wTimeHour.add("18");
        wTimeHour.add("19");
        wTimeHour.add("20");
        wTimeHour.add("21");
        wTimeHour.add("22");
        wTimeHour.add("23");
        wTimeHour.setEditable(false);
        wTimeHour.setSelected(12, true, false);
        wTimeMins.add("00");
        wTimeMins.add("15");
        wTimeMins.add("30");
        wTimeMins.add("45");
        wTimeMins.setEditable(false);
        wTimeMins.setSelected(0, true, false);
        wWeather.add(Localize("quick.CLE"));
        wWeather.add(Localize("quick.GOO"));
        wWeather.add(Localize("quick.HAZ"));
        wWeather.add(Localize("quick.POO"));
        wWeather.add(Localize("quick.BLI"));
        wWeather.add(Localize("quick.RAI"));
        wWeather.add(Localize("quick.THU"));
        wWeather.setEditable(false);
        wWeather.setSelected(0, true, false);
        wCldHeight.add("500");
        wCldHeight.add("750");
        wCldHeight.add("1000");
        wCldHeight.add("1250");
        wCldHeight.add("1500");
        wCldHeight.add("1750");
        wCldHeight.add("2000");
        wCldHeight.add("2250");
        wCldHeight.add("2500");
        wCldHeight.add("2750");
        wCldHeight.add("3000");
        wCldHeight.setEditable(true);
        wCldHeight.setSelected(6, true, false);
        wCldHeight.setNumericOnly(true);
        wPlaneList.add(Localize("quick.STD"));
        wPlaneList.add(Localize("quick.ABC"));
        file = new File("Missions/Quick/QMBair_2.ini");
        if(file.exists() && checkCustomAirIni("Missions/Quick/QMBair_2.ini"))
            wPlaneList.add(Localize("quick.CUS1"));
        file = new File("Missions/Quick/QMBair_3.ini");
        if(file.exists() && checkCustomAirIni("Missions/Quick/QMBair_3.ini"))
            wPlaneList.add(Localize("quick.CUS2"));
        wPlaneList.setEditable(false);
        wPlaneList.setSelected(getPlaneList(), true, false);
        wSituation.add(Localize("quick.NON"));
        wSituation.add(Localize("quick.ADV"));
        wSituation.add(Localize("quick.DIS"));
        wSituation.setEditable(false);
        wSituation.setSelected(0, true, false);
        boolean aflag[] = new boolean[_targetKey.length];
        for(int j = 0; j < _targetKey.length; j++)
        {
            wTarget.add(Localize("quick." + _targetKey[j]));
            aflag[j] = true;
        }

        wTarget.setEditable(false);
        wTarget.setSelected(0, true, false);
        wTarget.posEnable = aflag;
        wDefence.add(Localize("quick.NOND"));
        wDefence.add(Localize("quick.AAA"));
        wDefence.setEditable(false);
        wDefence.setSelected(1, true, false);
        star = GTexture.New("GUI/Game/QM/star.mat");
        cross = GTexture.New("GUI/Game/QM/cross.mat");
        for(int k = 0; k < 16; k++)
        {
            dlg[k] = new ItemDlg();
            dlg[k].wNum = (WComboNum)dialogClient.addControl(new WComboNum(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wSkill = (WComboSkill)dialogClient.addControl(new WComboSkill(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wPlane = (WComboPlane)dialogClient.addControl(new WComboPlane(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wLoadout = (WComboLoadout)dialogClient.addControl(new WComboLoadout(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].bArming = (WButtonArming)dialogClient.addControl(new WButtonArming(k, dialogClient, gtexture1, 0.0F, 48F, 32F, 32F));
            dlg[k].wNum.setEditable(false);
            if(k == 0)
            {
                for(int l = 1; l < 5; l++)
                    dlg[k].wNum.add("" + l);

            } else
            {
                for(int i1 = 0; i1 < 5; i1++)
                    dlg[k].wNum.add("" + i1);

            }
            dlg[k].wNum.setSelected(0, true, false);
            dlg[k].wSkill.setEditable(false);
            dlg[k].wSkill.add(Localize("quick.ROO"));
            dlg[k].wSkill.add(Localize("quick.EXP"));
            dlg[k].wSkill.add(Localize("quick.VET"));
            dlg[k].wSkill.add(Localize("quick.ACE"));
            dlg[k].wSkill.setSelected(1, true, false);
            dlg[k].wPlane.setEditable(false);
            dlg[k].wPlane.listVisibleLines = 16;
            fillComboPlane(dlg[k].wPlane, k == 0);
            dlg[k].wLoadout.setEditable(false);
            fillComboWeapon(dlg[k].wLoadout, wing[k].plane, 0);
        }

        fillMapKey();
        wMap.setSelected(0, true, false);
        onArmyChange();
        wMap.setEditable(false);
//        System.out.println("TEST!!!!");
        mapChanged();
        dialogClient.activateWindow();
        showHide();
        client.hideWindow();
    }

    private void dumpFullPlaneList()
    {
        SectFile sectfile = new SectFile("com/maddox/il2/objects/air.ini");
        SectFile sectfile1 = new SectFile("./Missions/Quick/FullPlaneList.dump", 1);
        sectfile1.sectionAdd("AIR");
        int i = sectfile.vars(sectfile.sectionIndex("AIR"));
        for(int j = 0; j < i; j++)
            sectfile1.varAdd(0, sectfile.var(0, j));

    }

    private void fillMapKey()
    {
        DirFilter dirfilter = new DirFilter();
        folderNames = (new File(PREFIX)).list(dirfilter);
        if(folderNames != null)
        {
            _mapKey = new String[folderNames.length];
            for(int i = 0; i < _mapKey.length; i++)
                _mapKey[i] = folderNames[i];

        } else
        {
            _mapKey = new String[1];
            _mapKey[0] = Localize("quick.NOMISSIONS");
            bNoAvailableMissions = true;
        }
        resetwMap();
    }

    private ArrayList getAvailableMissionsS(String s, String s1)
    {
//        System.out.println("getAvailableMissionsS(" + s + ", " + s1 + ")");
        File file = new File(PREFIX + s);
        String as[] = file.list();
        ArrayList arraylist = new ArrayList();
        arraylist.clear();
        if(as == null)
            return arraylist;
        for(int i = 0; i < as.length; i++)
            if(as[i].startsWith(s1) && as[i].endsWith(".mis"))
                arraylist.add(as[i]);

        return arraylist;
    }

    private String getArmyString()
    {
        if(OUR)
            return "Red";
        else
            return "Blue";
    }

    private void resetwMap()
    {
        wMap.clear();
        if(bNoAvailableMissions)
        {
            wMap.add(Localize("quick.NOMISSIONS"));
            return;
        }
        for(int i = 0; i < _mapKey.length; i++)
        {
            if(getAvailableMissionsS(_mapKey[i], _mapKey[i] + getArmyString()).size() <= 0)
                continue;
            String s = _mapKey[i];
            String s1 = I18N.map(s);
            if(!s.equals(s1))
                wMap.add(s1);
            else
                wMap.add(I18N.map(_mapKey[i]));
        }

    }

    private String getMapName()
    {
        String s = wMap.getValue();
        for(int i = 0; i < _mapKey.length; i++)
        {
            String s1 = _mapKey[i];
            String s2 = I18N.map(s1);
            if(s2.equals(s))
                return s1;
        }

        return s;
    }

    private void onArmyChange()
    {
        String s = getMapName();
        String s1 = wMap.getValue();
        resetwMap();
        int i = wMap.list.indexOf(s1);
        if(getAvailableMissionsS(s, s + getArmyString()).size() > 0)
            wMap.setSelected(i, true, false);
        else
            wMap.setSelected(0, true, false);
        onTargetChange();
    }

    private void onTargetChange()
    {
        int i = wTarget.getSelected();
        int j = 0;
        for(int k = wTarget.size() - 1; k > -1; k--)
            if(getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[k]).size() > 0)
            {
                wTarget.posEnable[k] = true;
                j = k;
            } else
            {
                wTarget.posEnable[k] = false;
            }

        if(wTarget.posEnable[i])
            wTarget.setSelected(i, true, false);
        else
            wTarget.setSelected(j, true, false);
        checkNoneTarget();
    }

    private void checkNoneTarget()
    {
//        boolean flag = false;
        int j = 0;
        if(wTarget.getSelected() == 0)
        {
            noneTarget = true;
            int i = wLevel.getSelected();
            for(int k = wLevel.size() - 1; k > -1; k--)
                if(getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[0] + noneTargetSuffix[k]).size() > 0)
                {
                    wLevel.posEnable[k] = true;
                    j = k;
                } else
                {
                    wLevel.posEnable[k] = false;
                }

            if(wLevel.posEnable[i])
                wLevel.setSelected(i, true, false);
            else
                wLevel.setSelected(j, true, false);
            wLevel.showWindow();
        } else
        {
            noneTarget = false;
            wLevel.hideWindow();
        }
    }

    private void showHide()
    {
        for(int i = 0; i < 4; i++)
        {
            setShow(bFirst, dlg[i].wNum);
            setShow(bFirst, dlg[i].wSkill);
            setShow(bFirst, dlg[i].wPlane);
            setShow(bFirst, dlg[i].wLoadout);
            setShow(bFirst, dlg[i].bArming);
            setShow(bFirst, dlg[i + 8].wNum);
            setShow(bFirst, dlg[i + 8].wSkill);
            setShow(bFirst, dlg[i + 8].wPlane);
            setShow(bFirst, dlg[i + 8].wLoadout);
            setShow(bFirst, dlg[i + 8].bArming);
        }

        for(int j = 4; j < 8; j++)
        {
            setShow(!bFirst, dlg[j].wNum);
            setShow(!bFirst, dlg[j].wSkill);
            setShow(!bFirst, dlg[j].wPlane);
            setShow(!bFirst, dlg[j].wLoadout);
            setShow(!bFirst, dlg[j].bArming);
            setShow(!bFirst, dlg[j + 8].wNum);
            setShow(!bFirst, dlg[j + 8].wSkill);
            setShow(!bFirst, dlg[j + 8].wPlane);
            setShow(!bFirst, dlg[j + 8].wLoadout);
            setShow(!bFirst, dlg[j + 8].bArming);
        }

        setShow(bFirst, wPlaneList);
        setShow(bFirst, wTarget);
        setShow(bFirst, wMap);
        setShow(bFirst, bNext);
        setShow(bFirst, bExit);
        setShow(bFirst, bArmy);
        setShow(bFirst, wAltitude);
        setShow(bFirst, wDefence);
        setShow(bFirst, wSituation);
        setShow(bFirst, wPos);
        setShow(bFirst, bReset);
        setShow(bFirst && noneTarget, wLevel);
        setShow(!bFirst, bBack);
        setShow(!bFirst, bStat);
        setShow(!bFirst, wWeather);
        setShow(!bFirst, wTimeHour);
        setShow(!bFirst, wTimeMins);
        setShow(!bFirst, wCldHeight);
        setShow(!bNoAvailableMissions, bFly);
        dialogClient.doResolutionChanged();
        dialogClient.setPosSize();
    }

    private void setShow(boolean flag, GWindow gwindow)
    {
        if(flag)
            gwindow.showWindow();
        else
            gwindow.hideWindow();
    }

    private void setDefaultValues()
    {
        wAltitude.setSelected(8, true, false);
        wPos.setSelected(0, true, false);
        wTimeHour.setSelected(12, true, false);
        wTimeMins.setSelected(0, true, false);
        wWeather.setSelected(0, true, false);
        wCldHeight.setSelected(6, true, false);
        wDefence.setSelected(1, true, false);
        wSituation.setSelected(0, true, false);
        for(int i = 0; i < 16; i++)
        {
            dlg[i].wNum.setSelected(0, true, false);
            dlg[i].wSkill.setSelected(1, true, false);
            dlg[i].wPlane.setSelected(0, true, false);
            dlg[i].wLoadout.setSelected(0, true, false);
            dlg[i].wNum.notify(2, 0);
            dlg[i].wSkill.notify(2, 0);
            dlg[i].wPlane.notify(2, 0);
            dlg[i].wLoadout.notify(2, 0);
        }

    }

    static Class _mthclass$(String s)
    {
        Class class1;
        try
        {
            class1 = Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
        return class1;
    }

    private String russianMixedRules;
    private RuleBasedCollator collator;
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public SectFile ssect;
    public GTexture cross;
    public GTexture star;
    public boolean OUR;
    public boolean bScramble;
    public GUIButton bArmy;
    public GUIButton bNext;
    public GUIButton bStat;
    public GUIButton bExit;
    public GUIButton bBack;
    public GUIButton bLoad;
    public GUIButton bSave;
    public GUIButton bFly;
    public GUIButton bDiff;
    public GUIButton bReset;
    public GWindowComboControl wSituation;
    public GWindowComboControl wMap;
    public GWindowComboControl wTarget;
    public GWindowComboControl wPos;
    public GWindowComboControl wDefence;
    public GWindowComboControl wAltitude;
    public GWindowComboControl wWeather;
    public GWindowComboControl wCldHeight;
    public GWindowComboControl wTimeHour;
    public GWindowComboControl wTimeMins;
    public GWindowComboControl wLevel;
    public GWindowComboControl wPlaneList;
    private String r01;
    private String r010;
    private String g01;
    private String g010;
    private String _mapKey[];
    private String folderNames[];
    private String _targetKey[] = {
        "None", "Armor", "Bridge", "Airbase", "Scramble"
    };
    private ArrayList playerPlane;
    private ArrayList aiPlane;
    private ArrayList playerPlaneC;
    private ArrayList aiPlaneC;
    private ItemWing wing[];
    private ItemDlg dlg[];
    private IOState ioState;
    private int indxAirArming;
    private boolean bPlaneArrestor;
    private boolean bFirst;
    static boolean bIsQuick;
    private static int pl;
    private static final String PREFIX = "Missions/Quick/";
    private boolean noneTarget;
    private String noneTargetSuffix[] = {
        "N", "A", "D"
    };
    /*private*/ static final int NONE = 0;
    /*private*/ static final int ARMOR = 1;
    /*private*/ static final int BRIDGE = 2;
    /*private*/ static final int AIRBASE = 3;
    /*private*/ static final int SCRAMBLE = 4;
    /*private*/ static final int ADVANTAGE = 1;
    private String currentMissionName;
    private boolean bNoAvailableMissions;
    static Class class$com$maddox$il2$objects$air$TypeTransport;

    static 
    {
        ObjIO.fields(IOState.class, new String[] {
            "our", "situation", "map", "target", "defence", "altitude", "weather", "timeH", "timeM", "pos", 
            "cldheight", "scramble", "noneTARGET"
        });
        ObjIO.validate(IOState.class, "loaded");
        ObjIO.fields(ItemWing.class, new String[] {
            "planes", "weapon", "regiment", "skin", "noseart", "pilot", "numberOn", "fuel", "skill"
        });
        ObjIO.accessStr(ItemWing.class, "plane", "getPlane", "setPlane");
        ObjIO.validate(ItemWing.class, "loaded");
    }
}
