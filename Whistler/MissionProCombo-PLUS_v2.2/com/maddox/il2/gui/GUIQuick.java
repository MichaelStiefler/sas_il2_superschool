package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.builder.PlMission;
import com.maddox.il2.builder.Plugin;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.util.*;
import java.io.*;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.*;

public class GUIQuick extends GameState
{
    public class IOState
    {

        public void getMap()
        {
            int i = parseIntOwn(map);
            if(i >= 0 && i < _mapKey.length)
            {
                String s = _mapKey[i];
                map = s;
                wMap.setValue(I18N.map(s));
                mapChanged(false);
            } else
            {
                if(client.isActivated())
                    new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 40F, true, "Error", "The 4.09m legacy mission loaded has a map with code " + i + " which is not installed now!", 3, 0.0F);
                map = "";
            }
        }

        public void save()
        {
            our = OUR;
            scramble = bScramble;
            situation = wSituation.getSelected();
            map = getMapName();
            target = wTargetList.getSelected();
            defence = wDefence.getSelected();
            altitude = wAltitude.getValue();
            pos = wPos.getValue();
            weather = wWeather.getSelected();
            cldheight = wCldHeight.getValue();
            wind = wRandomWind.getSelected();
            date = wSetDate.getValue();
            timeH = wTimeHour.getSelected();
            timeM = wTimeMins.getSelected();
            noneTARGET = noneTargetNum;
            posInFlight = wPosInFlight.getSelected();
        }

        public void loaded()
        {
            OUR = our;
            wMap.setValue(I18N.map(map));
            mapChanged(false);
            updateCountries();
            wSituation.setSelected(situation, true, false);
            if(target < wTargetList.size())
                wTargetList.setSelected(target, true, false);
            else
                wTargetList.setSelected(-1, false, false);
            wDefence.setSelected(defence, true, false);
            bScramble = scramble;
            wPos.setValue(pos);
            validateEditableComboControl(wPos);
            wAltitude.setValue(altitude);
            int i = parseIntOwn(wAltitude.getValue());
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
            wCldHeight.setEnable(World.cur().diffUser.Clouds);
            validateEditableComboControl(wCldHeight);
            wRandomWind.setSelected((World.cur().diffUser.Wind_N_Turbulence ? wind : 0), true, false);
            wRandomWind.setEnable(World.cur().diffUser.Wind_N_Turbulence);
            wWeather.setSelected(weather, true, false);
            wSetDate.setValue(date, false);
            wTimeHour.setSelected(timeH, true, false);
            wTimeMins.setSelected(timeM, true, false);
            wPosInFlight.setSelected(posInFlight, true, false);
            sArrestorOn.setEnable(playerPlaneC.size() > 0);
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
        public int wind;
        public String date;
        public int timeH;
        public int timeM;
        public int noneTARGET;
        public int posInFlight;

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
                UpdateComboNum(indx);
                int k = getSelected();
                if(k < 0)
                    return true;
                try
                {
                    if(indx == 0)
                    {
                        wPosInFlight.setSelected(0, true, false);
                        for(int l = 0; l < 4; l++)
                            if(l <= k)
                                wPosInFlight.posEnable[l] = true;
                            else
                                wPosInFlight.posEnable[l] = false;
                    }
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
                wing[indx].planes = indx != 0 ? k : k + 1;
                return true;
            }
            if(i == 15)
            {
                UpdateComboNum(indx);
                int k = getSelected();
                wing[indx].planes = indx != 0 ? k : k + 1;
                return true;
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
                int kn = getFirstNonPlaceholder(k, indx == 0);
                if(kn != k)
                {
                    setSelected(kn, true, false);
                    k = kn;
                }
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
                } else
                if(indx > 0 && dlg[indx].wNum.getSelected() == 0)
                    dlg[indx].wNum.setSelected(1, true, true);
                if(!flag)
                    wing[indx].weapon = s;
                return true;
            }
            if(i == 15)
            {
                if(indx > 0 && dlg[indx].wNum.getSelected() == 0)
                    dlg[indx].wNum.setSelected(1, true, true);
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

    class WComboCountry extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if(k < 0)
                    return true;
                String s = OUR ? r01 : g01;
                if(indx < 8)
                {
                    if(OUR)
                    {
                        if(countryLstRed.size() > k)
                        {
                            s = (String)countryLstRed.get(k);
                            ArrayList arraylist = (ArrayList)regList.get(s);
                            Object obj = arraylist.get(0);
                            wing[indx].regiment = ((Regiment)obj).name();
                        }
                    } else
                    if(countryLstBlue.size() > k)
                    {
                        s = (String)countryLstBlue.get(k);
                        ArrayList arraylist = (ArrayList)regList.get(s);
                        Object obj = arraylist.get(0);
                        wing[indx].regiment = ((Regiment)obj).name();
                    }
                } else
                if(!OUR)
                {
                    if(countryLstRed.size() > k)
                    {
                        s = (String)countryLstRed.get(k);
                        ArrayList arraylist = (ArrayList)regList.get(s);
                        Object obj = arraylist.get(0);
                        wing[indx].regiment = ((Regiment)obj).name();
                    }
                } else
                if(countryLstBlue.size() > k)
                {
                    s = (String)countryLstBlue.get(k);
                    ArrayList arraylist = (ArrayList)regList.get(s);
                    Object obj = arraylist.get(0);
                    wing[indx].regiment = ((Regiment)obj).name();
                }
                return true;
            } else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WComboCountry(int i, GWindow gwindow, float f, float f1, float f2)
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
        public WComboCountry wCountry;
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
                arraylist = indx != 0 ? aiPlaneC : playerPlaneC;
            else
                arraylist = indx != 0 ? aiPlane : playerPlane;
            int i = 0;
            do
            {
                if(i >= arraylist.size())
                    break;
                plane = (ItemAir)arraylist.get(i);
                if(plane.name.equals(s))
                {
                    int in = getFirstNonPlaceholder(i, indx == 0);
                    plane = (ItemAir)arraylist.get(in);
                    break;
                }
                i++;
            } while(true);
            if(i >= arraylist.size())
            {
                int in = getFirstNonPlaceholder(0, indx == 0);
                plane = (ItemAir)arraylist.get(in);
            }
        }

        public void loaded()
        {
            dlg[indx].wNum.setSelected(indx != 0 ? planes : planes - 1, true, false);
            if(indx == 0)
            {
                wPosInFlight.posEnable[1] = dlg[indx].wNum.getSelected() > 0 ? true : false;
                wPosInFlight.posEnable[2] = dlg[indx].wNum.getSelected() > 1 ? true : false;
                wPosInFlight.posEnable[3] = dlg[indx].wNum.getSelected() > 2 ? true : false;
            }
            dlg[indx].wSkill.setSelected(skill, true, false);
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx != 0 ? aiPlaneC : playerPlaneC;
            else
                arraylist = indx != 0 ? aiPlane : playerPlane;
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
            UpdateComboNum(indx);
            fillComboCountry(dlg[indx].wCountry, indx);
            selectCountries(indx);
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
                usercfg.setSkin(plane.name, skin[playerNum]);
                usercfg.setNoseart(plane.name, noseart[playerNum]);
                usercfg.netPilot = pilot[playerNum];
                usercfg.setWeapon(plane.name, weapon);
                usercfg.netNumberOn = numberOn[playerNum];
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
                skin[playerNum] = usercfg.getSkin(plane.name);
                noseart[playerNum] = usercfg.getNoseart(plane.name);
                pilot[playerNum] = usercfg.netPilot;
                weapon = usercfg.getWeapon(plane.name);
                numberOn[playerNum] = usercfg.netNumberOn;
                return;
            }
        }

        public String prepareWing409(SectFile sectfile)
        {
            String s;
            if(indx <= 3)
                s = (OUR ? r010 : g010) + iwing;
            else
                s = (OUR ? g010 : r010) + iwing;
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
                s1 = regiment + "0" + iwing;
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

        public void prepereWay409(SectFile sectfile, String as[], String as1[])
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
                        l = parseIntOwn(wAltitude.getValue());
                    }
                    catch(Exception exception) { }
                    if(wSituation.getSelected() == 1)
                    {
                        if(indx <= 3)
                            l += parseIntOwn(wPos.getValue());
                    } else
                    if(indx > 3)
                        l += parseIntOwn(wPos.getValue());
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
//                if(s3 != null && (GUIQuick.class$com$maddox$il2$objects$air$TypeTransport == null ? (GUIQuick.class$com$maddox$il2$objects$air$TypeTransport = GUIQuick._mthclass$("com.maddox.il2.objects.air.TypeTransport")) : GUIQuick.class$com$maddox$il2$objects$air$TypeTransport).isAssignableFrom(plane.clazz))
                if(s3 != null && TypeTransport.class.isAssignableFrom(plane.clazz))
                    s3 = null;
                if(s3 != null)
                {
                    for(int i1 = 0; i1 < 8; i1++)
                    {
                        if(!s3.equals(as[i1]))
                            continue;
                        s3 = as1[i1];
                        break;
                    }

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

        public String prepareWing(SectFile sectfile)
        {
            String s;
            if(indx < 8)
            {
                s = OUR ? r01 : g01;
                if(indx < 4)
                    s = s + "0" + iwing;
                else
                    s = s + "1" + iwing;
            } else
            {
                s = OUR ? g01 : r01;
                if(indx < 12)
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
                if(indx > 3 && indx < 8 || indx > 11)
                    s1 = regiment + "1" + iwing;
                else
                    s1 = regiment + "0" + iwing;
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
                if(s.startsWith("TRIGGERS"))
                {
                    String s1 = s + " " + SharedTokenizer.next("") + " " + SharedTokenizer.next("") + " " + SharedTokenizer.next("") + " " + SharedTokenizer.next("") + " ";
                    sectfile.line(i, k, s1);
                } else
                {
                    String s2 = s + " " + SharedTokenizer.next("") + " " + SharedTokenizer.next("") + " ";
                    String s3 = wAltitude.getValue();
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
                        s3 = "" + l;
                    }
                    SharedTokenizer.next("");
                    float f = (float)SharedTokenizer.next((plane.speedMin + plane.speedMax) / 2D, plane.speedMin, plane.speedMax);
                    if(s.startsWith("TAKEOFF") || s.startsWith("LANDING"))
                    {
                        s3 = "0";
                        f = 0.0F;
                    }
                    String s4 = SharedTokenizer.next((String)null);
                    String s5 = SharedTokenizer.next((String)null);
//                    if(s4 != null && (GUIQuick.class$com$maddox$il2$objects$air$TypeTransport == null ? (GUIQuick.class$com$maddox$il2$objects$air$TypeTransport = GUIQuick._mthclass$("com.maddox.il2.objects.air.TypeTransport")) : GUIQuick.class$com$maddox$il2$objects$air$TypeTransport).isAssignableFrom(plane.clazz))
                    if(s4 != null && TypeTransport.class.isAssignableFrom(plane.clazz))
                        s4 = null;
                    if(s4 != null)
                    {
                        int i1 = 0;
                        do
                        {
                            if(i1 >= 8)
                                break;
                            if(s4.equals(as[i1]))
                            {
                                s4 = as1[i1];
                                break;
                            }
                            i1++;
                        } while(true);
                    }
                    if(s4 != null)
                    {
                        if(s5 != null)
                            sectfile.line(i, k, s2 + s3 + " " + f + " " + s4 + " " + s5);
                        else
                            sectfile.line(i, k, s2 + s3 + " " + f + " " + s4);
                    } else
                    {
                        sectfile.line(i, k, s2 + s3 + " " + f);
                    }
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
//            guiairarming.quikPlayerPosition = playerNum;
            guiairarming.quikPlayerPosition = wPosInFlight.getSelected();
            if(indx <= 7)
                guiairarming.quikArmy = OUR ? 1 : 2;
            else
                guiairarming.quikArmy = OUR ? 2 : 1;
            guiairarming.quikPlanes = planes;
            guiairarming.quikPlane = plane.name;
            guiairarming.quikWeapon = weapon;
//            guiairarming.quikCurPlane = 0;
            guiairarming.quikCurPlane = indx == 0 ? wPosInFlight.getSelected() : 0;
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
            guiairarming.quikListName.clear();
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
            else
                arraylist = indx == 0 ? playerPlane : aiPlane;
            for(int j = 0; j < arraylist.size(); j++)
            {
                ItemAir itemair = (ItemAir)arraylist.get(j);
                guiairarming.quikListPlane.add(itemair.clazz);
                if(itemair.className.equalsIgnoreCase(PlaceholderLabel))
                    guiairarming.quikListName.add(itemair.name);
                else
                    guiairarming.quikListName.add((itemair.bEnablePlayer ? "" : "(AI) ") + I18N.plane(itemair.name));
            }
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
        public int playerNum;
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
        public String origPlaneName;

        public ItemWing(int i)
        {
            indx = 0;
            planes = 0;
            plane = null;
            playerNum = 0;
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
                mapChanged(true);
                if(music.equals("usa01"))
                    Main3D.menuMusicPlay(OUR ? "us" : "ja");
                else
                    Main3D.menuMusicPlay(OUR ? "ru" : "de");
                for(int k = 0; k < 16; k++)
                    if(k <= 7)
                        wing[k].regiment = OUR ? r01 : g01;
                    else
                        wing[k].regiment = OUR ? g01 : r01;

                updateCountries();
                return true;
            }
            if(gwindow == wTargetList)
            {
                processTargetList();
                return true;
            }
            if(gwindow == bExit)
            {
                GUIQuick.setQMB(false);
                save(true);
                if(Main.stateStack().getPrevious() != null && Main.stateStack().getPrevious().id() == 18)
                    Main.stateStack().push(2);
                else
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
                    bFirst = true;
                    showHide();
                    validateEditableComboControl(wCldHeight);
                    validateEditableComboControl(wPos);
                    validateEditableComboControl(wAltitude);
                    startQuickMissionMulti();
                    return true;
                }
            if(gwindow == bRandom)
            {
                randomMission();
                if(!bFirst && !b16Flights)
                {
                    bFirst = true;
                    showHide();
                }
                return true;
            }
            if(gwindow == bReset)
            {
                if(wMap.getSelected() == -1)
                    wMap.setSelected(0, true, true);
                mapChanged(true);
//                if(wPlaneList.getSelected() != 0)
//                    wPlaneList.setSelected(0, true, true);
                setDefaultValues();
                if(!bFirst && !b16Flights)
                {
                    bFirst = true;
                    showHide();
                }
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
                bFirst = true;
                showHide();
                ssect = null;
                Main.stateStack().push(25);
                return true;
            }
            if(gwindow == bSave)
            {
                bFirst = true;
                showHide();
                validateEditableComboControl(wCldHeight);
                validateEditableComboControl(wPos);
                validateEditableComboControl(wAltitude);
                save(false);
                Main.stateStack().push(24);
                return true;
            }
            if(gwindow == wPlaneList)
            {
                if(getPlaneList() > 1 || wPlaneList.getSelected() > 1)
                {
                    GUIQuick.setPlaneList(wPlaneList.getSelected());
                    dlg[0].wPlane.setSelected(0, true, true);
                    dlg[0].wLoadout.setSelected(0, true, false);
                    fillArrayPlanes();
                    if(playerPlaneC.size() == 0)
                        bPlaneArrestor = false;
                    sArrestorOn.bChecked = bPlaneArrestor;
                    sArrestorOn.setEnable(playerPlaneC.size() > 0);
                    for(int l = 0; l < 16; l++)
                        fillComboPlane(dlg[l].wPlane, l == 0);
                    softReset();
                } else
                {
                    ArrayList OldPlanes = new ArrayList(16);
                    int Planes[] = new int[16];
                    int Weapons[] = new int[16];
                    for(int l = 0; l < 16; l++)
                    {
                        ArrayList arraylist = null;
                        if(bPlaneArrestor)
                            arraylist = l != 0 ? aiPlaneC : playerPlaneC;
                        else
                            arraylist = l != 0 ? aiPlane : playerPlane;
                        ItemAir plane = (ItemAir)arraylist.get(dlg[l].wPlane.getSelected());
                        Planes[l] = parseIntOwn(dlg[l].wNum.getValue());
                        OldPlanes.add(plane.name);
                        Weapons[l] = dlg[l].wLoadout.getSelected();
                    }

                    IniFile inifile = Config.cur.ini;
                    inifile.set("QMB", "PlaneList", wPlaneList.getSelected() == 1 ? 1 : 0);
                    GUIQuick.setPlaneList(wPlaneList.getSelected());
                    fillArrayPlanes();
                    for(int l = 0; l < 16; l++)
                        fillComboPlane(dlg[l].wPlane, l == 0);
                    for(int l = 0; l < 16; l++)
                    {
                        ArrayList arraylist = null;
                        if(bPlaneArrestor)
                            arraylist = l != 0 ? aiPlaneC : playerPlaneC;
                        else
                            arraylist = l != 0 ? aiPlane : playerPlane;
                        int it = 0;
                        do
                        {
                            if(it >= arraylist.size())
                                break;
                            String s = (String)OldPlanes.get(l);
                            ItemAir plane = (ItemAir)arraylist.get(it);
                            if(plane.name.equalsIgnoreCase(s))
                                break;
                            it++;
                        } while(true);
                        dlg[l].wPlane.setSelected(it, false, false);
                        if(Weapons[l] > -1 && Weapons[l] < dlg[l].wLoadout.size())
                            dlg[l].wLoadout.setSelected(Weapons[l], false, false);
                        dlg[l].wNum.setSelected(Planes[l], false, false);
                        UpdateComboNum(l);
                    }
                }
                return true;
            }
            if(gwindow == sParachuteOn)
                return true;
            if(gwindow == sArrestorOn)
            {
                if(bPlaneArrestor)
                {
                    bPlaneArrestor = false;
                    sArrestorOn.bChecked = bPlaneArrestor;
                    ArrayList OldPlanes = new ArrayList(16);
                    int Planes[] = new int[16];
                    int Weapons[] = new int[16];
                    for(int l = 0; l < 16; l++)
                    {
                        ArrayList arraylist = null;
                        arraylist = l != 0 ? aiPlaneC : playerPlaneC;
                        ItemAir plane = (ItemAir)arraylist.get(dlg[l].wPlane.getSelected());
                        Planes[l] = parseIntOwn(dlg[l].wNum.getValue());
                        OldPlanes.add(plane.name);
                        Weapons[l] = dlg[l].wLoadout.getSelected();
                    }
                    fillArrayPlanes();
                    for(int l = 0; l < 16; l++)
                        fillComboPlane(dlg[l].wPlane, l == 0);
                    for(int l = 0; l < 16; l++)
                    {
                        ArrayList arraylist = null;
                        arraylist = l != 0 ? aiPlane : playerPlane;
                        int it = 0;
                        do
                        {
                            if(it >= arraylist.size())
                                break;
                            String s = (String)OldPlanes.get(l);
                            ItemAir plane = (ItemAir)arraylist.get(it);
                            if(plane.name.equalsIgnoreCase(s))
                                break;
                            it++;
                        } while(true);
                        dlg[l].wPlane.setSelected(it, false, false);
                        if(Weapons[l] > -1 && Weapons[l] < dlg[l].wLoadout.size())
                            dlg[l].wLoadout.setSelected(Weapons[l], false, false);
                        dlg[l].wNum.setSelected(Planes[l], false, false);
                        UpdateComboNum(l);
                    }
                } else
                {
                    GWindowMessageBox gwindowmessagebox = new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 30F, true, localize("quick.PLALST"), "QMB will now be partially reset. Continue?", 1, 0.0F) {

                        public void result(int k)
                        {
                            if(k == 3)
                            {
                                dlg[0].wPlane.setSelected(0, true, true);
                                dlg[0].wLoadout.setSelected(0, true, false);
                                fillArrayPlanes();
                                bPlaneArrestor = !bPlaneArrestor;
                                for(int l = 0; l < 16; l++)
                                    fillComboPlane(dlg[l].wPlane, l == 0);
                                softReset();
                            }
                            sArrestorOn.bChecked = bPlaneArrestor;
                        }
                    }
;
                }
                return true;
            }
            if(gwindow == bFMB)
            {
                bFirst = true;
                showHide();
                GUIQuick.setQMB(false);
                save(true);
                Main.stateStack().push(18);
                if(GUIQuick.exisstFile(currentMissionName))
                    PlMission.doLoadMissionFile(currentMissionName, currentMissionName);
                return true;
            }
            if(gwindow == bBrief)
            {
                if(sDesc.length() > 2)
                    new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 45F, true, "Mission Briefing", sDesc, 3, 0.0F);
                return true;
            }
            if(gwindow == bDate)
                return true;
            if(gwindow == wMap)
            {
                onArmyChange();
                mapChanged(false);
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
                                if(music.equals("usa01"))
                                    Main3D.menuMusicPlay(OUR ? "us" : "ja");
                                else
                                    Main3D.menuMusicPlay(OUR ? "ru" : "de");
                        }
                    }
                }
                return true;
            }
            if(gwindow == wSetDate)
            {
                doValidateDate();
                return true;
            }
            if(gwindow == wPosInFlight)
            {
                wing[0].playerNum = wPosInFlight.getSelected();
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
            GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(628F), x1024(924F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(150F), y1024(640F), 2.0F, y1024(80F));
            GUISeparate.draw(this, GColor.Gray, x1024(320F), y1024(640F), 2.0F, y1024(80F));
            GUISeparate.draw(this, GColor.Gray, x1024(705F), y1024(640F), 2.0F, y1024(80F));
            GUISeparate.draw(this, GColor.Gray, x1024(875F), y1024(640F), 2.0F, y1024(80F));
            GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(640F), 2.0F, y1024(46F));
            GUISeparate.draw(this, GColor.Gray, x1024(566F), y1024(640F), 2.0F, y1024(46F));
            GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(686F), x1024(30F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(537F), y1024(686F), x1024(30F), 2.0F);
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(0.0F), y1024(636F), x1024(170F), M(2.0F), 1, localize("quick.BAC"));
            draw(x1024(108F), y1024(636F), x1024(170F), M(2.0F), 1, localize("quick.LOD"));
            draw(x1024(195F), y1024(636F), x1024(170F), M(2.0F), 1, localize("quick.SAV"));
            if(!bNoAvailableMissions)
                draw(x1024(427F), y1024(636F), x1024(170F), M(2.0F), 1, localize("quick.FLY"));
            draw(x1024(548F), y1024(636F), x1024(170F), M(2.0F), 1, localize("quick.DIF"));

            draw(x1024(663F), y1024(636F), x1024(170F), M(2.0F), 1, localize("Random"));
            draw(x1024(307F), y1024(636F), x1024(170F), M(2.0F), 1, localize("F.M.B."));
            draw(x1024(750F), y1024(636F), x1024(170F), M(2.0F), 1, localize("quick.RES"));
            if(bFirst)
            {
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(496F), x1024(924F), 2.0F);
                setCanvasColor(GColor.Gray);
//                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(285F), x1024(924F), 2.0F);
// Advanced Setup Horizontal
//                GUISeparate.draw(this, GColor.White, x1024(940F), y1024(130F), x1024(18F), 2.0F);
//                GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(317F), x1024(94F), 2.0F);
// Advanced Setup Vertical
//                GUISeparate.draw(this, GColor.White, x1024(958F), y1024(110F), 2.0F, y1024(56F));
//                GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(317F), 2.0F, y1024(28F));
//                setCanvasColor(GColor.Gray);
                draw(x1024(853F), y1024(636F), x1024(170F), M(2.0F), 1, localize("quick.GFC"));
                setCanvasFont(1);
                draw(x1024(38F), y1024(36F), x1024(602F), M(2.0F), 0, localize("quick.YOU"));
                draw(x1024(38F), y1024(131F), x1024(602F), M(2.0F), 0, localize("quick.FRI"));
                draw(x1024(38F), y1024(292F), x1024(602F), M(2.0F), 0, localize("quick.ENM"));
                setCanvasFont(0);
                draw(x1024(302F), y1024(32F), x1024(80F), M(2.0F), 2, localize("netair.Position"));
                draw(x1024(52F), y1024(65F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(114F), y1024(65F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(225F), y1024(65F), x1024(274F), M(2.0F), 0, localize("quick.PLA") + (bPlaneArrestor ? " (" + localize("Carrier-based") + ")" : ""));
                draw(x1024(488F), y1024(65F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                draw(x1024(776F), y1024(65F), x1024(196F), M(2.0F), 0, localize("singleSelect.Country"));
                draw(x1024(52F), y1024(160F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(114F), y1024(160F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(225F), y1024(160F), x1024(274F), M(2.0F), 0, localize("quick.PLA") + (bPlaneArrestor ? " (" + localize("Carrier-based") + ")" : ""));
                draw(x1024(488F), y1024(160F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                draw(x1024(776F), y1024(160F), x1024(196F), M(2.0F), 0, localize("singleSelect.Country"));
                draw(x1024(52F), y1024(321F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(114F), y1024(321F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(225F), y1024(321F), x1024(274F), M(2.0F), 0, localize("quick.PLA") + (bPlaneArrestor ? " (" + localize("Carrier-based") + ")" : ""));
                draw(x1024(488F), y1024(321F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                draw(x1024(776F), y1024(321F), x1024(196F), M(2.0F), 0, localize("singleSelect.Country"));

                draw(x1024(10F), y1024(535F), x1024(64F), M(2.0F), 2, localize("dgendetail.Date"));
                wSetDate.align = wSetDate.isActivated() ? 0 : 1;
                draw(x1024(10F), y1024(569F), x1024(64F), M(2.0F), 2, localize("quick.TIM"));

                draw(x1024(216F), y1024(518F), x1024(64F), M(2.0F), 2, localize("quick.WEA"));
                draw(x1024(216F), y1024(552F), x1024(64F), M(2.0F), 2, localize("quick.CLD"));
                draw(x1024(216F), y1024(586F), x1024(64F), M(2.0F), 2, localize("Wind"));

                draw(x1024(406F), y1024(518F), x1024(80F), M(2.0F), 2, localize("quick.ALT"));
                draw(x1024(406F), y1024(552F), x1024(80F), M(2.0F), 2, localize("quick.SIT"));
                draw(x1024(406F), y1024(586F), x1024(80F), M(2.0F), 2, localize("quick.POS"));

                draw(x1024(608F), y1024(518F), x1024(80F), M(2.0F), 2, localize("quick.MAP"));
                draw(x1024(608F), y1024(552F), x1024(80F), M(2.0F), 2, localize("dgendetail.Mission"));
                if(wTargetList.size() > 0)
                    draw(x1024(698F), y1024(586F), x1024(290F), M(2.0F), 0, localize("quick.TAR") + ": " + wTarget + " (" + (b16Flights ? "16 " : "8 ") + localize("Flights") + ")");

//                draw(x1024(362F), y1024(114F), x1024(570F), y1024(32F), 2, localize("quick.ASET"));
//                draw(x1024(320F), y1024(298F), x1024(528F), y1024(32F), 2, localize("quick.ASET"));
                setCanvasFont(0);
                if(OUR)
                    draw(x1024(575F), y1024(36F), x1024(362F), M(2.0F), 2, localize("quick.SEL") + " " + localize("Red"));
//                    draw(x1024(566F), y1024(29F), x1024(362F), M(2.0F), 2, localize("quick.SEL_Allies"));
                else
                    draw(x1024(575F), y1024(36F), x1024(362F), M(2.0F), 2, localize("quick.SEL") + " " + localize("Blue"));
//                    draw(x1024(566F), y1024(29F), x1024(362F), M(2.0F), 2, localize("quick.SEL_Axis"));
            } else
            {
                if(b16Flights)
                {
//                    GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(285F), x1024(924F), 2.0F);
//                    GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(288F), x1024(94F), 2.0F);
//                    GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(232F), 2.0F, y1024(112F));
                    draw(x1024(52F), y1024(125F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                    draw(x1024(114F), y1024(125F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                    draw(x1024(225F), y1024(125F), x1024(274F), M(2.0F), 0, localize("quick.PLA") + (bPlaneArrestor ? " (" + localize("Carrier-based") + ")" : ""));
                    draw(x1024(488F), y1024(125F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                    draw(x1024(776F), y1024(125F), x1024(196F), M(2.0F), 0, localize("singleSelect.Country"));
                    draw(x1024(52F), y1024(321F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                    draw(x1024(114F), y1024(321F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                    draw(x1024(225F), y1024(321F), x1024(274F), M(2.0F), 0, localize("quick.PLA") + (bPlaneArrestor ? " (" + localize("Carrier-based") + ")" : ""));
                    draw(x1024(488F), y1024(321F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                    draw(x1024(776F), y1024(321F), x1024(196F), M(2.0F), 0, localize("singleSelect.Country"));
//                    draw(x1024(320F), y1024(274F), x1024(528F), y1024(32F), 2, localize("quick.ASET"));
                }
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(496F), x1024(924F), 2.0F);
                setCanvasColor(GColor.Gray);
                setCanvasFont(0);
                draw(x1024(148F), y1024(535F), x1024(80F), M(2.0F), 2, localize("quick.DEF"));
                draw(x1024(148F), y1024(569F), x1024(80F), M(2.0F), 2, localize("Parachutes"));
//                if(((GWindowCheckBox) (sParachuteOn)).bChecked)
//                    draw(x1024(665F), y1024(569F), x1024(250F), M(2.0F), 0, "(Default: pilots and crew might bail out)");
//                else
//                    draw(x1024(665F), y1024(569F), x1024(250F), M(2.0F), 0, "(Custom: pilots and crew won't bail out)");
                draw(x1024(533F), y1024(535F), x1024(80F), M(2.0F), 2, localize("quick.PLALST"));
                draw(x1024(533F), y1024(569F), x1024(80F), M(2.0F), 2, localize("Carrier-based"));
//                draw(x1024(412F), y1024(569F), x1024(150F), M(2.0F), 0, "(Applies to all screens)");
                draw(x1024(854F), y1024(636F), x1024(170F), M(2.0F), 1, localize("quick.STAT"));
                if(b16Flights)
                {
                    setCanvasFont(1);
                    draw(x1024(38F), y1024(95F), x1024(602F), M(2.0F), 0, localize("quick.ADDFRI"));
                    draw(x1024(38F), y1024(292F), x1024(602F), M(2.0F), 0, localize("quick.ADDENM"));
                }
                setCanvasFont(0);
            }
        }

        public void setPosSize()
        {
//            float f = 16F;
//            float f1 = ((GWindow) (super.root)).win.dx / ((GWindow) (super.root)).win.dy;
//            if(f1 < 1.0F)
//                f /= 2.0F;
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            bArmy.setPosC(x1024(966F), y1024(38F + 16F));

            bExit.setPosC(x1024(85F), y1024(689F));
            bLoad.setPosC(x1024(193F), y1024(689F));
            bSave.setPosC(x1024(280F), y1024(689F));
            bFMB.setPosC(x1024(392F), y1024(689F));
            bFly.setPosC(x1024(512F), y1024(689F));
            bDiff.setPosC(x1024(633F), y1024(689F));
            bRandom.setPosC(x1024(748F), y1024(689F));
            bReset.setPosC(x1024(835F), y1024(689F));
            bNext.setPosC(x1024(939F), y1024(689F));
            bBack.setPosC(x1024(85F), y1024(689F));
            bStat.setPosC(x1024(939F), y1024(689F));

            wSetDate.setPosSize(x1024(84F), y1024(535F), x1024(125F) - ComboButtonWidth, M(1.7F));
            bDate.setPosC(x1024(209F) - M(1.7F / 2), y1024(535F) + M(1.7F / 2));
            wTimeHour.setPosSize(x1024(84F), y1024(569F), x1024(60F), M(1.7F));
            wTimeMins.setPosSize(x1024(149F), y1024(569F), x1024(60F), M(1.7F));

            wWeather.setPosSize(x1024(290F), y1024(518F), x1024(125F), M(1.7F));
            wCldHeight.setPosSize(x1024(290F), y1024(552F), x1024(125F), M(1.7F));
            wRandomWind.setPosSize(x1024(290F), y1024(586F), x1024(125F), M(1.7F));

            wAltitude.setPosSize(x1024(496F), y1024(518F), x1024(125F), M(1.7F));
            wSituation.setPosSize(x1024(496F), y1024(552F), x1024(125F), M(1.7F));
            wPos.setPosSize(x1024(496F), y1024(586F), x1024(125F), M(1.7F));

            wMap.setPosSize(x1024(698F), y1024(518F), x1024(270F), M(1.7F));
            wTargetList.setPosSize(x1024(698F), y1024(552F), x1024(270F), M(1.7F));
            bBrief.setPosC(x1024(686F) - M(1.7F / 2), y1024(586F) + M(1.7F / 2));

            float f = 16F;
            float f1 = root.win.dx / root.win.dy;
            if(f1 < 1.0F)
                f /= 2.0F;
            wDefence.setPosSize(x1024(242F), y1024(535F), x1024(160F), M(1.7F));
            sParachuteOn.setPosC(x1024(322F), y1024(569F + f));
            wPlaneList.setPosSize(x1024(627F), y1024(535F), x1024(160F), M(1.7F));
            sArrestorOn.setPosC(x1024(707F), y1024(569F + f));

            wPosInFlight.setPosSize(x1024(392F), y1024(32F), x1024(200F), M(1.7F));

            dlg[0].wNum.setPosSize(x1024(48F), y1024(97F), x1024(48F), M(1.7F));
            dlg[0].wSkill.setPosSize(x1024(110F), y1024(97F), x1024(92F), M(1.7F));
            dlg[0].wPlane.setPosSize(x1024(217F), y1024(97F), x1024(245F), M(1.7F));
            dlg[0].wLoadout.setPosSize(x1024(480F), y1024(97F), x1024(275F), M(1.7F));
            dlg[0].wCountry.setPosSize(x1024(772F), y1024(97F), x1024(165F), M(1.7F));
//            dlg[0].bArming.setPosC(x1024(960F), y1024(91F + f - 4.0F));
            dlg[0].bArming.setPosC(x1024(960F), y1024(96F + 12.0F));
            for(int i = 0; i < 7; i++)
                if(i < 3)
                {
                    dlg[i + 1].wNum.setPosSize(x1024(48F), y1024(192 + 34 * i), x1024(48F), M(1.7F));
                    dlg[i + 1].wSkill.setPosSize(x1024(110F), y1024(192 + 34 * i), x1024(92F), M(1.7F));
                    dlg[i + 1].wPlane.setPosSize(x1024(217F), y1024(192 + 34 * i), x1024(245F), M(1.7F));
                    dlg[i + 1].wLoadout.setPosSize(x1024(480F), y1024(192 + 34 * i), x1024(275F), M(1.7F));
                    dlg[i + 1].wCountry.setPosSize(x1024(772F), y1024(192 + 34 * i), x1024(165F), M(1.7F));
                    dlg[i + 1].bArming.setPosC(x1024(960F), y1024((float)(191 + 34 * i) + 12.0F));
                } else
                {
                    dlg[i + 1].wNum.setPosSize(x1024(48F), y1024(192 + 34 * (i - 4)), x1024(48F), M(1.7F));
                    dlg[i + 1].wSkill.setPosSize(x1024(110F), y1024(192 + 34 * (i - 4)), x1024(92F), M(1.7F));
                    dlg[i + 1].wPlane.setPosSize(x1024(217F), y1024(192 + 34 * (i - 4)), x1024(245F), M(1.7F));
                    dlg[i + 1].wLoadout.setPosSize(x1024(480F), y1024(192 + 34 * (i - 4)), x1024(275F), M(1.7F));
                    dlg[i + 1].wCountry.setPosSize(x1024(772F), y1024(192 + 34 * (i - 4)), x1024(165F), M(1.7F));
                    dlg[i + 1].bArming.setPosC(x1024(960F), y1024((float)(191 + 34 * (i - 4)) + 12.0F));
                }

            for(int j = 0; j < 8; j++)
                if(j < 4)
                {
                    dlg[j + 8].wNum.setPosSize(x1024(48F), y1024(353 + 34 * j), x1024(48F), M(1.7F));
                    dlg[j + 8].wSkill.setPosSize(x1024(110F), y1024(353 + 34 * j), x1024(92F), M(1.7F));
                    dlg[j + 8].wPlane.setPosSize(x1024(217F), y1024(353 + 34 * j), x1024(245F), M(1.7F));
                    dlg[j + 8].wLoadout.setPosSize(x1024(480F), y1024(353 + 34 * j), x1024(275F), M(1.7F));
                    dlg[j + 8].wCountry.setPosSize(x1024(772F), y1024(353 + 34 * j), x1024(165F), M(1.7F));
                    dlg[j + 8].bArming.setPosC(x1024(960F), y1024((float)(352 + 34 * j) + 12.0F));
                } else
                {
                    dlg[j + 8].wNum.setPosSize(x1024(48F), y1024(353 + 34 * (j - 4)), x1024(48F), M(1.7F));
                    dlg[j + 8].wSkill.setPosSize(x1024(110F), y1024(353 + 34 * (j - 4)), x1024(92F), M(1.7F));
                    dlg[j + 8].wPlane.setPosSize(x1024(217F), y1024(353 + 34 * (j - 4)), x1024(245F), M(1.7F));
                    dlg[j + 8].wLoadout.setPosSize(x1024(480F), y1024(353 + 34 * (j - 4)), x1024(275F), M(1.7F));
                    dlg[j + 8].wCountry.setPosSize(x1024(772F), y1024(353 + 34 * (j - 4)), x1024(165F), M(1.7F));
                    dlg[j + 8].bArming.setPosC(x1024(960F), y1024((float)(352 + 34 * (j - 4)) + 12.0F));
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

    private void randomMission()
    {
        for(int i = 0; i < 16; i++)
        {
            if(bFirst)
            {
                if(i < 4 || (i > 7 && i < 12))
                {
                    dlg[i].wNum.setSelected(TrueRandom.nextInt(i == 0 ? 0 : 1, i == 0 ? 4 : 5), true, true);
                    dlg[i].wSkill.setSelected(TrueRandom.nextInt(0, 4), true, true);
                    dlg[i].wPlane.setSelected(TrueRandom.nextInt(0, dlg[i].wPlane.size()), true, true);
                    dlg[i].wLoadout.setSelected(dlg[i].wLoadout.size() > 2 ? TrueRandom.nextInt(0, dlg[i].wLoadout.size() - 1) : 0, true, true);
                    dlg[i].wCountry.setSelected(TrueRandom.nextInt(0, dlg[i].wCountry.size()), true, true);
                } else
                {
                    dlg[i].wSkill.setSelected(1, true, true);
                    dlg[i].wPlane.setSelected(0, true, true);
                    dlg[i].wLoadout.setSelected(0, true, true);
                    dlg[i].wNum.setSelected(0, true, true);
                }
            } else
            {
                dlg[i].wNum.setSelected(TrueRandom.nextInt(i == 0 ? 0 : 1, i == 0 ? 4 : 5), true, true);
                dlg[i].wSkill.setSelected(TrueRandom.nextInt(0, 4), true, true);
                dlg[i].wPlane.setSelected(TrueRandom.nextInt(0, dlg[i].wPlane.size()), true, true);
                dlg[i].wLoadout.setSelected(dlg[i].wLoadout.size() > 2 ? TrueRandom.nextInt(0, dlg[i].wLoadout.size() - 1) : 0, true, true);
                dlg[i].wCountry.setSelected(TrueRandom.nextInt(0, dlg[i].wCountry.size()), true, true);
            }
            UpdateComboNum(i);
        }
        wPosInFlight.setSelected(0, true, false);
    }

    public boolean isLeap(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    protected void doValidateDate()
    {
        wSetDate.setValue(wSetDate.getValue().trim(), false);
        boolean flag = false;
        int day = 0, month = 0, year = 0;
        String date = wSetDate.getValue();
        if(date.length() > 1 && (date.substring(1,2).equals(".") || date.substring(1,2).equals("/") || date.substring(1,2).equals("-")))
            date = "0" + date.substring(0);
        if(date.length() > 4 && (date.substring(4,5).equals(".") || date.substring(4,5).equals("/") || date.substring(4,5).equals("-")))
            date = date.substring(0,3) + "0" + date.substring(3);
        if(date.length() == 10 && (date.substring(2,3).equals(".") || date.substring(2,3).equals("/") || date.substring(2,3).equals("-")) && (date.substring(5,6).equals(".") || date.substring(5,6).equals("/") || date.substring(5,6).equals("-")))
        {
            try
            {
                day = Integer.parseInt(date.substring(0,2));
                month = Integer.parseInt(date.substring(3,5));
                year = Integer.parseInt(date.substring(6,10));
            }
            catch(NumberFormatException exception)
            {
                day = month = year = 0;
                flag = true;
            }

        } else
        {
            flag = true;
        }
        if(flag)
        {
            date = "15.06.1940";
            new GWindowMessageBox(dialogClient, 30F, true, "Mission Date", "Incorrect format. Date reset to default.", 3, 0.0F);
        } else
        {
            Calendar calendar = Calendar.getInstance();
            date = date.substring(0,2) + "." + date.substring(3,5) + "." + date.substring(6,10);
            if(day < 1)
            {
                day = 1;
                date = "0" + Integer.toString(day) + date.substring(2,10);
            } else
            if(day > 31)
            {
                day = 31;
                date = Integer.toString(day) + date.substring(2,10);
            }
            if(month < 1)
            {
                month = 1;
                date = date.substring(0,3) + "0" + Integer.toString(month) + date.substring(5,10);
            } else
            if(month > 12)
            {
                month = 12;
                date = date.substring(0,3) + Integer.toString(month) + date.substring(5,10);
            }
            if(year < 1900)
            {
                year = 1900;
                date = date.substring(0,6) + Integer.toString(year);
            }
            else if(year > calendar.get(Calendar.YEAR))
            {
                year = calendar.get(Calendar.YEAR);
                date = date.substring(0,6) + Integer.toString(year);
            }
            if(month == 2 && day > 28)
            {
                day = isLeap(year) ? 29 : 28;
                date = Integer.toString(day) + date.substring(2,10);
            }
            else if((month == 4 || month == 6 || month == 9 || month == 11) && day > 30)
            {
                day = 30;
                date = Integer.toString(day) + date.substring(2,10);
            }
        }
        wSetDate.setValue(date, false);
    }

    public void updateCountries()
    {
        for(int i3 = 0; i3 < 16; i3++)
        {
            fillComboCountry(dlg[i3].wCountry, i3);
            selectCountries(i3);
        }

    }

    private void UpdateComboNum(int indx)
    {
        int i = parseIntOwn(dlg[indx].wNum.getValue());
        if(i == 0)
        {
            dlg[indx].wPlane.setValue("");
            dlg[indx].wSkill.setEnable(false);
            dlg[indx].wLoadout.setEnable(false);
            dlg[indx].wCountry.setEnable(false);
        } else
        {
            dlg[indx].wSkill.setEnable(true);
            dlg[indx].wLoadout.setEnable(true);
            dlg[indx].wCountry.setEnable(true);
            dlg[indx].wPlane.setSelected(dlg[indx].wPlane.getSelected(), true, false);
            dlg[indx].wSkill.setSelected(dlg[indx].wSkill.getSelected(), true, false);
            dlg[indx].wLoadout.setSelected(dlg[indx].wLoadout.getSelected(), true, false);
            dlg[indx].wCountry.setSelected(dlg[indx].wCountry.getSelected(), true, false);
        }
    }

    private int getFirstNonPlaceholder(int k, boolean Player)
    {
        ArrayList arraylist = null;
        if(bPlaneArrestor)
            arraylist = Player ? playerPlaneC : aiPlaneC;
        else
            arraylist = Player ? playerPlane : aiPlane;
        int k0 = k;
        ItemAir itemair;
        for(itemair = (ItemAir)arraylist.get(k); itemair.className.equalsIgnoreCase(PlaceholderLabel); itemair = (ItemAir)arraylist.get(k))
        {
            if(k >= arraylist.size() - 1)
                break;
            k++;
        }

        if(k >= arraylist.size() - 1)
        {
            k = k0;
            for(; itemair.className.equalsIgnoreCase(PlaceholderLabel); itemair = (ItemAir)arraylist.get(k))
            {
                if(k <= 0)
                    break;
                k--;
            }

        }
        return k;
    }

    private int parseIntOwn(String s)
    {
        try
        {
            int i = Integer.parseInt(s);
            return i;
        }
        catch(Exception exception)
        {
            System.out.println("Error on parseInt: " + s);
        }
        return -1;
    }

    private void mapChanged(boolean Reset)
    {
        int i = wMap.getSelected();
        if(i >= 0 && i < _mapKey.length && !bNoAvailableMissions)
        {
            updateMissionsList();
            processTargetList();
            String s = "";
            if(wTargetList.getSelected() >= 0)
                s = MissionsFolder + getMapName() + "/" + getMapName() + ((String)filesTargetList.get(wTargetList.getSelected())).trim() + ".mis";
            currentMissionName = s;
            if(client.isActivated())
            {
                s = null;
                try
                {
                    String Result = null;
                    SectFile sectfile = new SectFile(currentMissionName, 0);
                    s = sectfile.get("MAIN", "MAP");
                    if(s == null)
                        Result = "No map defined in mission file!\n'MAP' section is missing.";
                    SectFile sectfile1 = new SectFile("maps/" + s);
                    String s1 = sectfile1.get("MAP", "TypeMap", (String)null);
                    if(s1 == null)
                        Result = "Bad map in mission file!\nThe map '" + s + "' definition is not correct.";
                    NumberTokenizer numbertokenizer = new NumberTokenizer(s1);
                    if(numbertokenizer.hasMoreTokens())
                    {
                        numbertokenizer.next();
                        if(numbertokenizer.hasMoreTokens())
                            s1 = numbertokenizer.next();
                    }
                    s1 = HomePath.concatNames("maps/" + s, s1);
                    int ai[] = new int[3];
                    if(!Mat.tgaInfo(s1, ai))
                        Result = "Bad map in mission file!\nThe map '" + s + "' definition is not correct.";
                    if(Result != null)
                        new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 20F, true, "Warning", Result, 3, 0.0F);
                }
                catch(Exception exception)
                {
                    new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 40F, true, "Warning", "The map '" + s + "' doesn't seem to be installed in your IL-2 game.\nThis mission cannot be played!", 3, 0.0F);
                }
            }
            try
            {
                SectFile sectfile = new SectFile(currentMissionName, 0);
                defaultRegiments(Reset);
                String s1 = sectfile.get("MAIN", "MAP", (String)null);
                if(s1 != null)
                {
                    IniFile inifile = new IniFile("maps/" + s1, 0);
                    String s2 = inifile.get("WORLDPOS", "CAMOUFLAGE", "SUMMER");
                    if(World.cur() != null)
                        World.cur().setCamouflage(s2);
                }
            }
            catch(Exception exception)
            {
                new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 40F, true, "Warning", "The map '" + s + "' doesn't seem to be installed in your IL-2 game.\nThis mission cannot be played!", 3, 0.0F);
            }
        }
        updateCountries();
    }

    public void enterPush(GameState gamestate)
    {
        if(bNoAvailableMissions)
        {
            System.out.println(">> No Quick Missions available!");
            String Title = "QMBPro Error";
            String Msg = "No Quick Missions found!!!\n\nEither the folder '" + MissionsFolder + "' is missing\n\n" + "or there are no Quick Missions in the folder '" + MissionsFolder + "'.\n\n" + "Your settings or folder structure are incorrect!";
            new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 40F, true, Title, Msg, 3, 0.0F) {

                public void result(int k)
                {
                    Main.stateStack().pop();
                }

            }
;
            return;
        }
        World.cur().diffUser.set(World.cur().userCfg.singleDifficulty);
        if(exisstFile("users/" + World.cur().userCfg.sId + "/.last.quick"))
        {
            ssect = new SectFile("users/" + World.cur().userCfg.sId + "/.last.quick", 0);
            load();
        } else
        if(exisstFile("quicks/.last.quick"))
        {
            GUIQuickLoad guiquickload = (GUIQuickLoad)GameState.get(25);
            guiquickload.execute(".last", true);
            load();
        } else
        {
            mapChanged(true);
        }
        wing[0].fromUserCfg();
        if(music.equals("usa01"))
            Main3D.menuMusicPlay(OUR ? "us" : "ja");
        else
            Main3D.menuMusicPlay(OUR ? "ru" : "de");
        _enter();
    }

    public void enterPop(GameState gamestate)
    {
// Difficulty
        if(gamestate.id() == 17)
        {
            World.cur().userCfg.singleDifficulty = World.cur().diffUser.get();
            World.cur().userCfg.saveConf();
            if(!World.cur().diffUser.Wind_N_Turbulence)
                wRandomWind.setSelected(0, true, false);
            wCldHeight.setEnable(World.cur().diffUser.Clouds);
            wRandomWind.setEnable(World.cur().diffUser.Wind_N_Turbulence);
        } else
// AirArming
        if(gamestate.id() == 55)
        {
            wing[indxAirArming].fromAirArming();
            selectCountries(indxAirArming);
        } else
// QuickLoad
        if(gamestate.id() == 25)
        {
            if(wPlaneList.getSelected() > 1)
            {
                wPlaneList.setSelected(0, true, false);
                setPlaneList(wPlaneList.getSelected());
                dlg[0].wPlane.setSelected(0, true, true);
                dlg[0].wLoadout.setSelected(0, true, false);
            }
            fillArrayPlanes();
            bPlaneArrestor = false;
            sArrestorOn.bChecked = bPlaneArrestor;
            sArrestorOn.setEnable(playerPlaneC.size() > 0);
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
        setColors();
        String s = "users/" + World.cur().userCfg.sId + "/QMB.ini";
        if(!exisstFile(s))
            initStat();
        client.activateWindow();
        Mission.resetDate();
        try
        {
            IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);
            sParachuteOn.bChecked = inifile.get("LastSingleMission", "ParachuteOn", true);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void _leave()
    {
        try
        {
            IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);
            if(inifile.get("LastSingleMission", "ParachuteOn", true) != ((GWindowCheckBox) (sParachuteOn)).bChecked)
                inifile.set("LastSingleMission", "ParachuteOn", ((GWindowCheckBox) (sParachuteOn)).bChecked);
            inifile.saveFile();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        World.cur().userCfg.saveConf();
        client.hideWindow();
    }

    public void startQuickMissionMulti()
    {
        String s = currentMissionName;
        try
        {
            SectFile sectfile = new SectFile(s, 0);
            sectfile.set("MAIN", "TIME", wTimeHour.getSelected() + "." + wTimeMins.getSelected() * 25);

            if(!sectfile.sectionExist("SEASON"))
                sectfile.sectionAdd("SEASON");
            int _season = sectfile.sectionIndex("SEASON");
            sectfile.sectionClear(_season);
            sectfile.lineAdd(_season, "Year", "" + Integer.parseInt(wSetDate.getValue().substring(6,10)));
            sectfile.lineAdd(_season, "Month", "" + Integer.parseInt(wSetDate.getValue().substring(3,5)));
            sectfile.lineAdd(_season, "Day", "" + Integer.parseInt(wSetDate.getValue().substring(0,2)));

            if(!sectfile.sectionExist("WEATHER"))
                sectfile.sectionAdd("WEATHER");
            int _weather = sectfile.sectionIndex("WEATHER");
            sectfile.sectionClear(_weather);
            sectfile.lineAdd(_weather, "WindDirection", "" + TrueRandom.nextInt(0, 360) + ".0");
            if(wRandomWind.getSelected() == 1)
            {
                sectfile.lineAdd(_weather, "WindSpeed", "" + TrueRandom.nextInt(1, 5) + ".0");
                sectfile.lineAdd(_weather, "Gust", "" + TrueRandom.nextInt(1, 5));
                sectfile.lineAdd(_weather, "Turbulence", "" + TrueRandom.nextInt(1, 3));
            } else
            if(wRandomWind.getSelected() == 2)
            {
                sectfile.lineAdd(_weather, "WindSpeed", "" + TrueRandom.nextInt(5, 10) + ".0");
                sectfile.lineAdd(_weather, "Gust", "" + TrueRandom.nextInt(5, 9));
                sectfile.lineAdd(_weather, "Turbulence", "" + TrueRandom.nextInt(3, 5));
            } else
            if(wRandomWind.getSelected() == 3)
            {
                sectfile.lineAdd(_weather, "WindSpeed", "" + TrueRandom.nextInt(10, 15) + ".0");
                sectfile.lineAdd(_weather, "Gust", "" + TrueRandom.nextInt(9, 13));
                sectfile.lineAdd(_weather, "Turbulence", "" + TrueRandom.nextInt(5, 7));
            } else
            {
                sectfile.lineAdd(_weather, "WindSpeed", "0.0");
                sectfile.lineAdd(_weather, "Gust", "0");
                sectfile.lineAdd(_weather, "Turbulence", "0");
            }

            sectfile.set("MAIN", "playerNum", wPosInFlight.getSelected());
            int n = 16;
            int a = sectfile.sectionIndex("Wing");
            if(a >= 0)
                n = sectfile.vars(a);
            if(n < 16)
            {
                for(int j = 0; j < 4; j++)
                {
                    String s1 = r010 + Character.forDigit(j, 10);
                    if(!sectfile.exist("Wing", s1))
                        throw new Exception("Section Red " + s1 + " not found");
                }

                for(int k = 0; k < 4; k++)
                {
                    String s2 = g010 + Character.forDigit(k, 10);
                    if(!sectfile.exist("Wing", s2))
                        throw new Exception("Section Blue " + s2 + " not found");
                }

                sectfile.set("MAIN", "CloudType", wWeather.getSelected());
                sectfile.set("MAIN", "CloudHeight", wCldHeight.getValue());
                String as[] = new String[8];
                String as1[] = new String[8];
                for(int l = 0; l < 8; l++)
                {
                    if(l <= 3)
                        as[l] = (OUR ? r010 : g010) + l;
                    else
                        as[l] = (OUR ? g010 : r010) + l;
                    if(l <= 3)
                        as1[l] = wing[l].prepareWing409(sectfile);
                    else
                        as1[l] = wing[l + 4].prepareWing409(sectfile);
                }

                if(as1[0] != null)
                    sectfile.set("MAIN", "player", as1[0]);
                else
                    sectfile.set("MAIN", "player", as[0]);
                for(int i1 = 0; i1 < 8; i1++)
                    if(as1[i1] != null)
                        wing[i1].prepereWay409(sectfile, as, as1);

                if(wDefence.getSelected() == 0)
                {
                    for(int j1 = 0; j1 < 2; j1++)
                    {
                        String s3 = j1 != 0 ? "NStationary" : "Stationary";
                        int l1 = sectfile.sectionIndex(s3);
                        if(l1 >= 0)
                        {
                            sectfile.sectionRename(l1, "Stationary_Temp");
                            sectfile.sectionAdd(s3);
                            int j2 = sectfile.sectionIndex(s3);
                            int l2 = sectfile.vars(l1);
                            for(int j3 = 0; j3 < l2; j3++)
                            {
                                SharedTokenizer.set(sectfile.line(l1, j3));
                                String s5 = null;
                                if(j1 == 1)
                                    s5 = SharedTokenizer.next("");
                                String s7 = SharedTokenizer.next("");
                                int k3 = SharedTokenizer.next(0);
                                String s9 = null;
                                if(s5 != null)
                                    s9 = s5 + " " + s7 + " " + k3 + " " + SharedTokenizer.getGap();
                                else
                                    s9 = s7 + " " + k3 + " " + SharedTokenizer.getGap();
                                if(k3 == 0)
                                    sectfile.lineAdd(j2, s9);
                                else
                                if(k3 == 1 && OUR && !bScramble)
                                    sectfile.lineAdd(j2, s9);
                                else
                                if(k3 == 2 && !OUR && !bScramble)
                                    sectfile.lineAdd(j2, s9);
                                else
                                    try
                                    {
                                        Class class1 = ObjIO.classForName(s7);
                                        if(!(com.maddox.il2.objects.vehicles.artillery.AAA.class).isAssignableFrom(class1))
                                            if(s7.startsWith("ships."))
                                            {
                                                SharedTokenizer.set(sectfile.line(l1, j3));
                                                if(j1 == 1)
                                                    s5 = SharedTokenizer.next("");
                                                String s12 = SharedTokenizer.next("");
                                                String s13 = SharedTokenizer.next("");
                                                String s14 = SharedTokenizer.next("");
                                                String s15 = SharedTokenizer.next("");
                                                String s16 = SharedTokenizer.next("");
                                                String s17 = SharedTokenizer.next("");
                                                SharedTokenizer.next("");
                                                String s19 = SharedTokenizer.next("");
                                                if(j1 == 1)
                                                    sectfile.lineAdd(j2, s5 + " " + s12 + " " + s13 + " " + s14 + " " + s15 + " " + s16 + " " + s17 + " " + 5940 + " " + s19);
                                                else
                                                    sectfile.lineAdd(j2, s12 + " " + s13 + " " + s14 + " " + s15 + " " + s16 + " " + s17 + " " + 5940 + " " + s19);
                                            } else
                                            {
                                                sectfile.lineAdd(j2, s9);
                                            }
                                    }
                                    catch(Throwable throwable) { }
                            }

                            sectfile.sectionRemove(l1);
                        }
                    }

                    int k1 = sectfile.sectionIndex("Chiefs");
                    if(k1 >= 0)
                    {
                        sectfile.sectionRename(k1, "Chiefs_Temp");
                        sectfile.sectionAdd("Chiefs");
                        int i2 = sectfile.sectionIndex("Chiefs");
                        int k2 = sectfile.vars(k1);
                        for(int i3 = 0; i3 < k2; i3++)
                        {
                            String s4 = sectfile.line(k1, i3);
                            SharedTokenizer.set(s4);
                            String s6 = SharedTokenizer.next("");
                            String s8 = SharedTokenizer.next("");
                            if(s8.startsWith("Ships."))
                            {
                                int l3 = SharedTokenizer.next(0);
                                if(l3 == 0)
                                    sectfile.lineAdd(i2, s4);
                                else
                                if(l3 == 1 && OUR && !bScramble)
                                    sectfile.lineAdd(i2, s4);
                                else
                                if(l3 == 2 && !OUR && !bScramble)
                                {
                                    sectfile.lineAdd(i2, s4);
                                } else
                                {
                                    SharedTokenizer.next("");
                                    String s11 = SharedTokenizer.next("");
                                    sectfile.lineAdd(i2, s6 + " " + s8 + " " + l3 + " " + 5940 + " " + s11);
                                }
                            } else
                            {
                                sectfile.lineAdd(i2, s4);
                            }
                        }

                        sectfile.sectionRemove(k1);
                    }
                }
            } else
            {
                for(int j = 0; j < 8; j++)
                {
                    String s1;
                    if(j < 4)
                        s1 = r010 + Character.forDigit(j, 10);
                    else
                        s1 = r01 + "1" + Character.forDigit(j - 4, 10);
                    if(!sectfile.exist("Wing", s1))
                        throw new Exception("Section " + s1 + " not found");
                }

                for(int k = 0; k < 8; k++)
                {
                    String s2;
                    if(k < 4)
                        s2 = g010 + Character.forDigit(k, 10);
                    else
                        s2 = g01 + "1" + Character.forDigit(k - 4, 10);
                    if(!sectfile.exist("Wing", s2))
                        throw new Exception("Section " + s2 + " not found");
                }

                sectfile.set("MAIN", "CloudType", wWeather.getSelected());
                sectfile.set("MAIN", "CloudHeight", parseIntOwn(wCldHeight.getValue()));
                String as[] = new String[16];
                String as1[] = new String[16];
                for(int l = 0; l < 16; l++)
                {
                    if(l < 8)
                        as[l] = (OUR ? r01 : g01) + (l < 4 ? "0" : "1") + l;
                    else
                        as[l] = (OUR ? g01 : r01) + (l < 12 ? "0" : "1") + l;
                    as1[l] = wing[l].prepareWing(sectfile);
                }

                if(as1[0] != null)
                    sectfile.set("MAIN", "player", as1[0]);
                else
                    sectfile.set("MAIN", "player", as[0]);
                for(int i1 = 0; i1 < 16; i1++)
                    if(as1[i1] != null)
                        wing[i1].prepereWay(sectfile, as, as1);

                if(wDefence.getSelected() == 0)
                {
                    for(int j1 = 0; j1 < 2; j1++)
                    {
                        String s3 = j1 != 0 ? "NStationary" : "Stationary";
                        int i2 = sectfile.sectionIndex(s3);
                        if(i2 >= 0)
                        {
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
                                    sectfile.lineAdd(k2, s10);
                                else
                                if(l3 == 1 && OUR && !bScramble)
                                    sectfile.lineAdd(k2, s10);
                                else
                                if(l3 == 2 && !OUR && !bScramble)
                                    sectfile.lineAdd(k2, s10);
                                else
                                    try
                                    {
                                        Class class1 = ObjIO.classForName(s8);
                                        if(!(com.maddox.il2.objects.vehicles.artillery.AAA.class).isAssignableFrom(class1))
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
                                                SharedTokenizer.next("");
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
                                    sectfile.lineAdd(l1, s4);
                                else
                                if(k3 == 1 && OUR && !bScramble)
                                    sectfile.lineAdd(l1, s4);
                                else
                                if(k3 == 2 && !OUR && !bScramble)
                                {
                                    sectfile.lineAdd(l1, s4);
                                } else
                                {
                                    SharedTokenizer.next("");
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
            }
            if(!((GWindowCheckBox) (sParachuteOn)).bChecked)
            {
                String sp = sectfile.get("MAIN", "player", (String)null);
                int z = sectfile.sectionIndex("Wing");
                if(z >= 0)
                {
                    int k = sectfile.vars(z);
                    for(int i1 = 0; i1 < k; i1++)
                    {
                        String sa = sectfile.var(z, i1);
                        if(!sa.equalsIgnoreCase(sp))
                            sectfile.set(sa, "Parachute", "0");
                    }

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
            new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
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
        {
            guiquicksave.execute(".last", false);
            ssect.saveFile("users/" + World.cur().userCfg.sId + "/.last.quick");
        }
    }

    private void fillMissingWingRegiments(boolean flag)
    {
        for(int i = 0; i < 16; i++)
            if(i <= 7)
                wing[i].regiment = flag ? r01 : g01;
            else
                wing[i].regiment = flag ? g01 : r01;

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
                fillMissingWingRegiments(ioState.our);
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

            onArmyChange();
            try
            {
                SectFile sectfile = new SectFile("PaintSchemes/regiments.ini", 0);
                for(int j = 0; j < 8; j++)
                {
                    byte byte3 = 2;
                    if(ioState.our)
                    {
                        if(j > 7)
                            byte3 = 3;
                    } else
                    if(j < 8)
                        byte3 = 3;
                    if(!sectfile.varExist(byte3, wing[j].regiment))
                    {
                        String s = wing[j].regiment;
                        String s1 = wing[j + 4].regiment;
                        wing[j].regiment = wing[j + 8].regiment;
                        wing[j + 4].regiment = s;
                        wing[j + 8].regiment = s1;
                    }
                }

            }
            catch(Exception exceptionNew2)
            {
                System.out.println("sorry, Error on paintscheme regiments");
                new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
            }
        }
        catch(Exception exception)
        {
            new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt 2", 3, 0.0F);
        }
        showHide();
    }

    public void load411()
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
                fillMissingWingRegiments(ioState.our);
                byte0 = 4;
                byte1 = 8;
                ioState.getMap();
            }
            try
            {
                for(int i = 0; i < byte1; i++)
                {
                    byte byte2 = 0;
                    if(byte0 > 0 && i > 3)
                        byte2 = 4;
                    ssect.get("states", "wing" + i, wing[i + byte2]);
                    if(!wing[i + byte2].origPlaneName.equals(wing[i + byte2].getPlane()))
                    {
                        setPlaneList(0);
                        wPlaneList.setSelected(0, true, false);
                        fillArrayPlanes();
                        for(int k = 0; k < 16; k++)
                            fillComboPlane(dlg[k].wPlane, k == 0);

                        i = -1;
                    }
                }

            }
            catch(Exception exceptionNew1)
            {
                System.out.println("sorry, Error on paintscheme regiments");
                new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
            }
            try
            {
                SectFile sectfile = new SectFile("PaintSchemes/regiments.ini", 0);
                for(int j = 0; j < 8; j++)
                {
                    byte byte3 = 2;
                    if(ioState.our)
                    {
                        if(j > 7)
                            byte3 = 3;
                    } else
                    if(j < 8)
                        byte3 = 3;
                    if(!sectfile.varExist(byte3, wing[j].regiment))
                    {
                        String s = wing[j].regiment;
                        String s1 = wing[j + 4].regiment;
                        wing[j].regiment = wing[j + 8].regiment;
                        wing[j + 4].regiment = s;
                        wing[j + 8].regiment = s1;
                    }
                }

            }
            catch(Exception exceptionNew2)
            {
                System.out.println("sorry, Error on paintscheme regiments");
                new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
            }
        }
        catch(Exception exception)
        {
            System.out.println("sorry, data corrupt");
            new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
        }
        onArmyChange();
//        setShow(bFirst && noneTarget, wLevel);
    }

    public void fillArrayPlanes()
    {
        playerPlane.clear();
        playerPlaneC.clear();
        aiPlane.clear();
        aiPlaneC.clear();
        boolean flag = false;
        if(getPlaneList() < 2)
        {
            planeListName = "com/maddox/il2/objects/air.ini";
            if(getPlaneList() == 1)
                flag = true;
        } else
        if(checkCustomAirIni(MissionsFolder + "QMBair_" + getPlaneList() + ".ini"))
        {
            planeListName = MissionsFolder + "QMBair_" + getPlaneList() + ".ini";
        } else
        {
            planeListName = "com/maddox/il2/objects/air.ini";
            wPlaneList.setSelected(0, true, false);
        }
        SectFile sectfile = new SectFile(planeListName, 0);
        SectFile sectfile1 = new SectFile("com/maddox/il2/objects/air.ini");
        int j = sectfile.sections();
        if(j <= 0)
            throw new RuntimeException("GUIQuick: file '" + planeListName + "' is empty");
        int k = 0;
        do
        {
            if(k >= j)
                break;
            int l = sectfile.vars(k);
            for(int i1 = 0; i1 < l; i1++)
            {
                String s = sectfile.var(k, i1);
                if(!sectfile1.varExist(0, s))
                    continue;
                int i = sectfile1.varIndex(0, s);
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile1.value(0, i));
                String s1 = numbertokenizer.next((String)null);
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
                if(flag2)
                {
                    Class class1 = null;
                    try
                    {
                        class1 = ObjIO.classForName(s1);
                    }
                    catch(Exception exception)
                    {
                        throw new RuntimeException("PlMisAir(GUIQuick): class '" + s1 + "' not found");
                    }
                    ItemAir itemair = new ItemAir(s, class1, s1);
                    if(itemair.bEnablePlayer)
                    {
                        if(!flag || (flag && !itemair.className.equalsIgnoreCase(PlaceholderLabel)))
                        {
                            playerPlane.add(itemair);
                            if(AirportCarrier.isPlaneContainsArrestor(class1))
                                playerPlaneC.add(itemair);
                        }
                    }
                    if(!flag || (flag && !itemair.className.equalsIgnoreCase(PlaceholderLabel)))
                    {
                        aiPlane.add(itemair);
                        if(AirportCarrier.isPlaneContainsArrestor(class1))
                            aiPlaneC.add(itemair);
                    }
                }
            }

            k++;
        } while(true);
        if(flag)
        {
            Collections.sort(playerPlane, new byI18N_name());
            Collections.sort(playerPlaneC, new byI18N_name());
            Collections.sort(aiPlane, new byI18N_name());
            Collections.sort(aiPlaneC, new byI18N_name());
        }
    }

    public void fillArrayCountry()
    {
        if(resCountry == null)
            resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
        countryLstRed.clear();
        countryLstBlue.clear();
        List list = Regiment.getAll();
        TreeMap treemap = new TreeMap();
        int k1 = list.size();
        for(int i2 = 0; i2 < k1; i2++)
        {
            Regiment regiment1 = (Regiment)list.get(i2);
            if(regiment1.getArmy() == 2)
                continue;
            String s9 = regiment1.name();
            if(regHash.containsKey(s9))
                continue;
            regHash.put(s9, regiment1);
            ArrayList arraylist5 = (ArrayList)regList.get(regiment1.branch());
            if(arraylist5 == null)
            {
                String s15 = null;
                try
                {
                    s15 = resCountry.getString(regiment1.branch());
                }
                catch(Exception exception4)
                {
                    continue;
                }
                arraylist5 = new ArrayList();
                regList.put(regiment1.branch(), arraylist5);
                treemap.put(s15, regiment1.branch());
            }
            arraylist5.add(regiment1);
        }

        String s10;
        for(Iterator iterator = treemap.keySet().iterator(); iterator.hasNext(); countryLstRed.add(treemap.get(s10)))
            s10 = (String)iterator.next();

        treemap.clear();
        for(int i2 = 0; i2 < k1; i2++)
        {
            Regiment regiment1 = (Regiment)list.get(i2);
            if(regiment1.getArmy() == 1)
                continue;
            String s9 = regiment1.name();
            if(regHash.containsKey(s9))
                continue;
            regHash.put(s9, regiment1);
            ArrayList arraylist5 = (ArrayList)regList.get(regiment1.branch());
            if(arraylist5 == null)
            {
                String s15 = null;
                try
                {
                    s15 = resCountry.getString(regiment1.branch());
                }
                catch(Exception exception4)
                {
                    continue;
                }
                arraylist5 = new ArrayList();
                regList.put(regiment1.branch(), arraylist5);
                treemap.put(s15, regiment1.branch());
            }
            arraylist5.add(regiment1);
        }

        String s11;
        for(Iterator iterator = treemap.keySet().iterator(); iterator.hasNext(); countryLstBlue.add(treemap.get(s11)))
            s11 = (String)iterator.next();

        treemap.clear();
    }

    public void fillComboCountry(GWindowComboControl gwindowcombocontrol, int Num)
    {
        try
        {
            if(resCountry == null)
                resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
            gwindowcombocontrol.clear();
            if(Num < 8)
            {
                if(OUR)
                {
                    int lr = countryLstRed.size();
                    for(int i = 0; i < lr; i++)
                        try
                        {
                            String s = resCountry.getString((String)countryLstRed.get(i));
                            gwindowcombocontrol.add(s);
                        }
                        catch(Exception exception)
                        {
                            gwindowcombocontrol.add("" + i);
                        }

                } else
                {
                    int lb = countryLstBlue.size();
                    for(int i = 0; i < lb; i++)
                        try
                        {
                            String s = resCountry.getString((String)countryLstBlue.get(i));
                            gwindowcombocontrol.add(s);
                        }
                        catch(Exception exception)
                        {
                            gwindowcombocontrol.add("" + i);
                        }

                }
            } else
            if(!OUR)
            {
                int lr = countryLstRed.size();
                for(int i = 0; i < lr; i++)
                    try
                    {
                        String s = resCountry.getString((String)countryLstRed.get(i));
                        gwindowcombocontrol.add(s);
                    }
                    catch(Exception exception)
                    {
                        gwindowcombocontrol.add("" + i);
                    }

            } else
            {
                int lb = countryLstBlue.size();
                for(int i = 0; i < lb; i++)
                    try
                    {
                        String s = resCountry.getString((String)countryLstBlue.get(i));
                        gwindowcombocontrol.add(s);
                    }
                    catch(Exception exception)
                    {
                        gwindowcombocontrol.add("" + i);
                    }

            }
        }
        catch(Exception e) { }
        gwindowcombocontrol.setSelected(0, true, false);
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
            if(gwindowcombocontrol != dlg[0].wPlane && !itemair.bEnablePlayer)
                gwindowcombocontrol.add("(AI) " + I18N.plane(itemair.name));
            else
                gwindowcombocontrol.add(I18N.plane(itemair.name));
        }

        gwindowcombocontrol.setSelected(getFirstNonPlaceholder(0, flag), true, false);
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

    public String localize(String s)
    {
        return I18N.gui(s);
    }

    private void defaultRegiments(boolean cleanRegs)
    {
        boolean r0100 = false;
        try
        {
            SectFile sectfile = new SectFile(currentMissionName, 0);
            int n = 16;
            int a = sectfile.sectionIndex("Wing");
            if(a >= 0)
                n = sectfile.vars(a);
            b16Flights = n == 16;
            showHide();
            if(!b16Flights)
                n = 8;
            for(int i = 0; i < n; i++)
            {
                String s = sectfile.var(a, i);
                if(s.equalsIgnoreCase("r0100"))
                    r0100 = true;
            }

        }
        catch(Exception exception)
        {
            System.out.println("WARNING: No quick missions in Missions folder.");
        }
        if(r0100)
        {
            r01 = "r01";
            r010 = "r010";
            g01 = "g01";
            g010 = "g010";
        } else
        {
            r01 = "usa01";
            r010 = "usa010";
            g01 = "ja01";
            g010 = "ja010";
        }
        if(cleanRegs)
        {
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
        music = r01;
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
        try
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
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            System.out.println("Cannot start Statistics!");
            return;
        }
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

    private static boolean existSFSFile(String s)
    {
        try
        {
            SFSInputStream sfsinputstream = new SFSInputStream(s);
            sfsinputstream.close();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    private void validateEditableComboControl(GWindowComboControl gwindowcombocontrol)
    {
        if(gwindowcombocontrol.size() == 0)
        {
            gwindowcombocontrol.setSelected(-1, false, false);
            return;
        }
        String s = gwindowcombocontrol.getValue();
        if(s.equals(""))
        {
            gwindowcombocontrol.setSelected(0, true, false);
            s = gwindowcombocontrol.get(0);
        }
        int i = parseIntOwn(s);
        if(i < parseIntOwn(gwindowcombocontrol.get(0)))
        {
            gwindowcombocontrol.setSelected(0, true, false);
            i = parseIntOwn(gwindowcombocontrol.getValue());
        }
        if(i > parseIntOwn(gwindowcombocontrol.get(gwindowcombocontrol.size() - 1)))
        {
            gwindowcombocontrol.setSelected(gwindowcombocontrol.size() - 1, true, false);
            i = parseIntOwn(gwindowcombocontrol.getValue());
        }
        for(int j = 0; j < gwindowcombocontrol.size(); j++)
            if(i == parseIntOwn(gwindowcombocontrol.get(j)))
                gwindowcombocontrol.setSelected(j, true, false);

    }

    private boolean checkCustomAirIni(String s)
    {
        try
        {
            SectFile sectfile = new SectFile(s);
            if(sectfile.sections() <= 0)
                return false;
            SectFile sectfile1 = new SectFile("com/maddox/il2/objects/air.ini");
            int i = sectfile.vars(0);
            for(int j = 0; j < i; j++)
                if(sectfile1.varExist(0, sectfile.var(0, j)))
                    return true;

        }
        catch(Exception e) { }
        return false;
    }

    private void dumpFullPlaneList()
    {
        try
        {
            SectFile sectfile = new SectFile("com/maddox/il2/objects/air.ini");
            SectFile sectfile1 = new SectFile(MissionsFolder + "FullPlaneList.dump", 1);
            sectfile1.sectionAdd("AIR");
            int i = sectfile.vars(sectfile.sectionIndex("AIR"));
            for(int j = 0; j < i; j++)
                sectfile1.varAdd(0, sectfile.var(0, j));

        }
        catch(Exception e)
        {
            System.out.println("Error creating FullPlaneList.dump: probably Mission Folder doesn't exist");
        }
    }

    private void fillMapKey()
    {
        try
        {
            DirFilter dirfilter = new DirFilter();
            folderNames = (new File(MissionsFolder)).list(dirfilter);
            if(folderNames != null)
            {
                _mapKey = new String[folderNames.length];
                for(int i = 0; i < _mapKey.length; i++)
                    _mapKey[i] = folderNames[i];

            } else
            {
                _mapKey = new String[1];
                _mapKey[0] = localize("quick.NOMISSIONS");
                bNoAvailableMissions = true;
            }
        }
        catch(Exception e) { }
        resetwMap();
    }

    private ArrayList getAvailableMissionsS(String s, String s1)
    {
        File file = new File(MissionsFolder + s);
        String as[] = file.list();
        ArrayList arraylist = new ArrayList();
        arraylist.clear();
        if(as == null)
            return arraylist;
        for(int i = 0; i < as.length; i++)
            if(as[i].startsWith(s1) && as[i].endsWith(".mis"))
                if(aKnownGoodMissions.contains(as[i]))
                    arraylist.add(as[i]);
                else
                if(!aKnownBadMissions.contains(as[i]))
                {
                    String currentMissionName = MissionsFolder + s + "/" + as[i];
                    try
                    {
                        SectFile sectfile = new SectFile(currentMissionName, 0, false);
                        if(sectfile.sections() != 0)
                        {
                            String s2 = sectfile.get("MAIN", "MAP");
                            if(s2 != null)
                                if(aKnownBadMaps.contains(s2))
                                    aKnownBadMissions.add(as[i]);
                                else
                                if(!existSFSFile("maps/" + s2))
                                {
                                    aKnownBadMaps.add(s2);
                                    aKnownBadMissions.add(as[i]);
                                } else
                                {
                                    aKnownGoodMissions.add(as[i]);
                                    arraylist.add(as[i]);
                                }
                        }
                    }
                    catch(Exception exception) { }
                }

        return arraylist;
    }

    public void setColors()
    {
        bUseColor = Config.cur.ini.get("Mods", "PALMODsColor", 1, 0, 1);
        colorRed = bUseColor == 1 ? 0xff000080 : 0;
        colorBlue = bUseColor == 1 ? 0xff800000 : 0;
        if(OUR)
        {
//            wTargetList.setEditTextColor(colorRed);
//            wDefence.setEditTextColor(colorRed);
//            wTarget.setCanvasColor(colorRed);
            for(int i = 0; i < 8; i++)
            {
                dlg[i].wNum.setEditTextColor(colorRed);
                dlg[i].wSkill.setEditTextColor(colorRed);
                dlg[i].wPlane.setEditTextColor(colorRed);
                dlg[i].wLoadout.setEditTextColor(colorRed);
                dlg[i].wCountry.setEditTextColor(colorRed);
            }

            for(int i = 8; i < 16; i++)
            {
                dlg[i].wNum.setEditTextColor(colorBlue);
                dlg[i].wSkill.setEditTextColor(colorBlue);
                dlg[i].wPlane.setEditTextColor(colorBlue);
                dlg[i].wLoadout.setEditTextColor(colorBlue);
                dlg[i].wCountry.setEditTextColor(colorBlue);
            }

        } else
        {
//            wTargetList.setEditTextColor(colorBlue);
//            wDefence.setEditTextColor(colorBlue);
//            wTarget.setCanvasColor(colorBlue);
            for(int i = 0; i < 8; i++)
            {
                dlg[i].wNum.setEditTextColor(colorBlue);
                dlg[i].wSkill.setEditTextColor(colorBlue);
                dlg[i].wPlane.setEditTextColor(colorBlue);
                dlg[i].wLoadout.setEditTextColor(colorBlue);
                dlg[i].wCountry.setEditTextColor(colorBlue);
            }

            for(int i = 8; i < 16; i++)
            {
                dlg[i].wNum.setEditTextColor(colorRed);
                dlg[i].wSkill.setEditTextColor(colorRed);
                dlg[i].wPlane.setEditTextColor(colorRed);
                dlg[i].wLoadout.setEditTextColor(colorRed);
                dlg[i].wCountry.setEditTextColor(colorRed);
            }

        }
    }

    public void selectCountries(int indx)
    {
        ArrayList countryLst;
        if(indx < 8)
            countryLst = OUR ? countryLstRed : countryLstBlue;
        else
            countryLst = OUR ? countryLstBlue : countryLstRed;
        String s = wing[indx].regiment;
        if(s != null)
        {
            Object obj = regHash.get(s);
            if(obj != null)
            {
                String s13 = ((Regiment)obj).branch();
                for(int k3 = 0; k3 < countryLst.size(); k3++)
                {
                    if(!s13.equals(countryLst.get(k3)))
                        continue;
                    dlg[indx].wCountry.setSelected(k3, true, false);
                    break;
                }

            }
        }
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
            wMap.add(localize("quick.NOMISSIONS"));
            return;
        }
        for(int i = 0; i < _mapKey.length; i++)
            if(getAvailableMissionsS(_mapKey[i], _mapKey[i] + getArmyString()).size() > 0)
            {
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
        {
            wMap.setSelected(i, true, false);
            updateMissionsList();
            processTargetList();
        } else
        {
            wMap.setSelected(-1, false, false);
        }
        setColors();
    }

    private void updateMissionsList()
    {
        int firstNone = -1;
        int newIndex = -1;
        int newTypeIndex = -1;
        String lastMission = "";
        if(wTargetList.getSelected() >= 0)
            lastMission = (String)filesTargetList.get(wTargetList.getSelected());
        String lastTypeMis = "";
        if(!lastMission.toLowerCase().startsWith(getArmyString().toLowerCase()))
            if(lastMission.toLowerCase().startsWith("red"))
                lastMission = "Blue" + lastMission.substring("red".length());
            else
            if(lastMission.toLowerCase().startsWith("blue"))
                lastMission = "Red" + lastMission.substring("blue".length());
        if(lastMission.length() >= 2)
            lastTypeMis = lastMission.substring(0, lastMission.length() - 2);
        ArrayList arraylist = getAvailableMissionsS(getMapName(), getMapName() + getArmyString());
        if(arraylist.size() == 0)
        {
            wTargetList.clear();
            wTargetList.setValue(localize("miss.LoadFailed"));
        } else
        {
            wTargetList.clear();
            filesTargetList.clear();
            for(int j = 0; j < arraylist.size(); j++)
            {
                String sm = (String)arraylist.get(j);
                String nToAdd = sm.substring(getMapName().length(), sm.toLowerCase().lastIndexOf(".mis"));
                filesTargetList.add(nToAdd);
                String s = getName(MissionsFolder + getMapName() + "/" + sm);
                if(s != "")
                    wTargetList.add(s);
                else
                    wTargetList.add(nToAdd);
                if(nToAdd.equalsIgnoreCase(lastMission))
                    newIndex = j;
                if(newTypeIndex == -1 && nToAdd.length() >= 2 && nToAdd.substring(0, nToAdd.length() - 2).equalsIgnoreCase(lastTypeMis))
                    newTypeIndex = j;
                if(firstNone == -1 && sm.toLowerCase().lastIndexOf("none") > -1)
                    firstNone = j;
            }

            if(newIndex > -1)
                wTargetList.setSelected(newIndex, true, false);
            else
            if(newTypeIndex > -1)
                wTargetList.setSelected(newTypeIndex, true, false);
            else
            if(firstNone > -1)
                wTargetList.setSelected(firstNone, true, false);
            else
                wTargetList.setSelected(0, true, false);
        }
    }

    private String getName(String s)
    {
        for(int i1 = s.length() - 1; i1 > 0; i1--)
        {
            char c = s.charAt(i1);
            if(c == '\\' || c == '/')
                break;
            if(c != '.')
                continue;
            s = s.substring(0, i1);
            break;
        }

        String dP = "";
        try
        {
            ResourceBundle resourcebundle1 = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            String st = resourcebundle1.getString("Name");
            for(int i1 = 0; i1 < st.length(); i1++)
            {
                char c = st.charAt(i1);
                dP = c != '\n' ? dP + c : dP + " ";
            }

            if(dP.length() < 2)
                dP = "";
        }
        catch(Exception exception) { }
        return dP;
    }

    private String getShort(String s)
    {
        for(int i1 = s.length() - 1; i1 > 0; i1--)
        {
            char c = s.charAt(i1);
            if(c == '\\' || c == '/')
                break;
            if(c != '.')
                continue;
            s = s.substring(0, i1);
            break;
        }

        String dP = "";
        try
        {
            ResourceBundle resourcebundle1 = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            String st = resourcebundle1.getString("Short");
            for(int i1 = 0; i1 < st.length(); i1++)
            {
                char c = st.charAt(i1);
                dP = c != '\n' ? dP + c : dP + " ";
            }

            if(dP.length() < 2)
                dP = "";
        }
        catch(Exception exception) { }
        return dP;
    }

    private String getDesc(String s)
    {
        for(int i1 = s.length() - 1; i1 > 0; i1--)
        {
            char c = s.charAt(i1);
            if(c == '\\' || c == '/')
                break;
            if(c != '.')
                continue;
            s = s.substring(0, i1);
            break;
        }

        String dP = "";
        try
        {
            ResourceBundle resourcebundle1 = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            String st = resourcebundle1.getString("Description");
            if(dP.length() < 2 || dP == null)
                dP = st;
        }
        catch(Exception exception) { }
        return dP;
    }

    private void processTargetList()
    {
        if(bNoAvailableMissions)
        {
//            wTarget.setValue("");
            wTarget = "N/A";
            noneTarget = false;
            bScramble = false;
            return;
        }
        noneTargetNum = 0;
        String ar = getArmyString().toLowerCase();
        String s = "";
        if(wTargetList.getSelected() >= 0)
            s = ((String)filesTargetList.get(wTargetList.getSelected())).toLowerCase();
        if(s.startsWith(ar + "nonen"))
        {
            noneTarget = true;
//            wTarget.setValue("None, Neutral");
            wTarget = "None, Neutral";
            bScramble = false;
        } else
        if(s.startsWith(ar + "nonea"))
        {
            noneTarget = true;
            noneTargetNum = 1;
//            wTarget.setValue("None, Advantage");
            wTarget = "None, Advantage";
            bScramble = false;
        } else
        if(s.startsWith(ar + "noned"))
        {
            noneTarget = true;
            noneTargetNum = -1;
//            wTarget.setValue("None, Disadvantage");
            wTarget = "None, Disadvantage";
            bScramble = false;
        } else
        if(s.startsWith(ar + "none"))
        {
            noneTarget = true;
//            wTarget.setValue(_targetKey[0]);
            wTarget = _targetKey[0];
            bScramble = false;
        } else
        if(s.startsWith(ar + "armor"))
        {
//            wTarget.setValue(_targetKey[1]);
            wTarget = _targetKey[1];
            noneTarget = false;
            bScramble = false;
        } else
        if(s.startsWith(ar + "bridge"))
        {
//            wTarget.setValue(_targetKey[2]);
            wTarget = _targetKey[2];
            noneTarget = false;
            bScramble = false;
        } else
        if(s.startsWith(ar + "airbase"))
        {
//            wTarget.setValue(_targetKey[3]);
            wTarget = _targetKey[3];
            noneTarget = false;
            bScramble = false;
        } else
        if(s.startsWith(ar + "scramble"))
        {
//            wTarget.setValue(_targetKey[4]);
            wTarget = _targetKey[4];
            bScramble = true;
            noneTarget = false;
        } else
        if(s.startsWith(ar + "strike"))
        {
//            wTarget.setValue(_targetKey[5]);
            wTarget = _targetKey[5];
            bScramble = false;
            noneTarget = false;
        } else
        {
            s = "";
            if(wTargetList.getSelected() >= 0)
            {
                s = (String)filesTargetList.get(wTargetList.getSelected());
                if(s.length() > 0)
                {
                    for(int i = s.length() - 1; i >= ar.length(); i--)
                    {
                        if(Character.isDigit(s.charAt(i)))
                            continue;
                        s = s.substring(ar.length(), i + 1);
                        break;
                    }

                }
            }
//            wTarget.setValue(s);
            wTarget = s;
            noneTarget = false;
            bScramble = false;
        }
        currentMissionName = MissionsFolder + getMapName() + "/" + getMapName() + s + ".mis";
        sShort = getShort(currentMissionName);
        sDesc = getDesc(currentMissionName);
        setShow(bFirst && sDesc.length() > 2, bBrief);
    }

    private void showHide()
    {
        for(int i = 0; i < 4; i++)
        {
            setShow(bFirst, dlg[i].wNum);
            setShow(bFirst, dlg[i].wSkill);
            setShow(bFirst, dlg[i].wPlane);
            setShow(bFirst, dlg[i].wLoadout);
            setShow(bFirst, dlg[i].wCountry);
            setShow(bFirst, dlg[i].bArming);
            setShow(bFirst, dlg[i + 8].wNum);
            setShow(bFirst, dlg[i + 8].wSkill);
            setShow(bFirst, dlg[i + 8].wPlane);
            setShow(bFirst, dlg[i + 8].wLoadout);
            setShow(bFirst, dlg[i + 8].wCountry);
            setShow(bFirst, dlg[i + 8].bArming);
        }

        for(int j = 4; j < 8; j++)
        {
            setShow(!bFirst && b16Flights, dlg[j].wNum);
            setShow(!bFirst && b16Flights, dlg[j].wSkill);
            setShow(!bFirst && b16Flights, dlg[j].wPlane);
            setShow(!bFirst && b16Flights, dlg[j].wLoadout);
            setShow(!bFirst && b16Flights, dlg[j].wCountry);
            setShow(!bFirst && b16Flights, dlg[j].bArming);
            setShow(!bFirst && b16Flights, dlg[j + 8].wNum);
            setShow(!bFirst && b16Flights, dlg[j + 8].wSkill);
            setShow(!bFirst && b16Flights, dlg[j + 8].wPlane);
            setShow(!bFirst && b16Flights, dlg[j + 8].wLoadout);
            setShow(!bFirst && b16Flights, dlg[j + 8].wCountry);
            setShow(!bFirst && b16Flights, dlg[j + 8].bArming);
        }

        setShow(bFirst, wPosInFlight);
        setShow(bFirst, wSetDate);
        setShow(bFirst, bDate);
        setShow(bFirst, wTimeHour);
        setShow(bFirst, wTimeMins);
        setShow(bFirst, wWeather);
        setShow(bFirst, wCldHeight);
        setShow(bFirst, wRandomWind);
//        setShow(bFirst, wTarget);
        setShow(bFirst, wTargetList);
        setShow(bFirst, wMap);
        setShow(bFirst, bNext);
        setShow(bFirst, bExit);
        setShow(bFirst, bArmy);
        setShow(bFirst, wAltitude);
        setShow(bFirst, wSituation);
        setShow(bFirst, wPos);
        setShow(bFirst && sDesc.length() > 2, bBrief);
        setShow(!bFirst, bBack);
        setShow(!bFirst, bStat);
        setShow(!bNoAvailableMissions, bFly);
        setShow(!bFirst, wDefence);
        setShow(!bFirst, wPlaneList);
        setShow(!bFirst, sArrestorOn);
        setShow(!bFirst, sParachuteOn);
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


    private void softReset()
    {
        wPosInFlight.setSelected(0, true, false);
        dlg[0].wSkill.setSelected(1, true, true);
        int wPlaneSelected = dlg[0].wPlane.getSelected();
        int wLoadoutSelected =  dlg[0].wLoadout.getSelected();
        dlg[0].wPlane.setSelected(-1, false, true);
        dlg[0].wLoadout.setSelected(-1, false, true);
        dlg[0].wPlane.setSelected(wPlaneSelected, true, true);
        dlg[0].wLoadout.setSelected(wLoadoutSelected, true, false);
        dlg[0].wNum.setSelected(0, true, true);
        UpdateComboNum(0);
        for(int i = 1; i < 16; i++)
        {
            dlg[i].wSkill.setSelected(1, true, true);
            dlg[i].wPlane.setSelected(0, true, true);
            dlg[i].wLoadout.setSelected(0, true, false);
            dlg[i].wNum.setSelected(0, true, true);
            UpdateComboNum(i);
        }
        for(int k = 0; k < 16; k++)
            if(k <= 7)
                wing[k].regiment = OUR ? r01 : g01;
            else
                wing[k].regiment = OUR ? g01 : r01;

        for(int i = 0; i < 16; i++)
        {
            fillComboCountry(dlg[i].wCountry, i);
            selectCountries(i);
        }
    }


    private void setDefaultValues()
    {
//        if(wPlaneList.getSelected() != 0)
//            wPlaneList.setSelected(0, true, true);
        wAltitude.setSelected(8, true, false);
        wPos.setSelected(0, true, false);
        wSetDate.setValue("15.06.1940", false);
        wTimeHour.setSelected(12, true, false);
        wTimeMins.setSelected(0, true, false);
        wDefence.setSelected(1, true, false);
        wWeather.setSelected(0, true, false);
        wCldHeight.setSelected(5, true, false);
        wRandomWind.setSelected(0, true, false);
        wSituation.setSelected(0, true, false);
        wPosInFlight.setSelected(0, true, false);
        dlg[0].wSkill.setSelected(1, true, true);
        int wPlaneSelected = dlg[0].wPlane.getSelected();
        int wLoadoutSelected =  dlg[0].wLoadout.getSelected();
        dlg[0].wPlane.setSelected(-1, false, true);
        dlg[0].wLoadout.setSelected(-1, false, true);
        dlg[0].wPlane.setSelected(wPlaneSelected, true, true);
        dlg[0].wLoadout.setSelected(wLoadoutSelected, true, false);
        dlg[0].wNum.setSelected(0, true, true);
        UpdateComboNum(0);
        for(int i = 1; i < 16; i++)
        {
            dlg[i].wSkill.setSelected(1, true, true);
            dlg[i].wPlane.setSelected(0, true, true);
            dlg[i].wLoadout.setSelected(0, true, false);
            dlg[i].wNum.setSelected(0, true, true);
            UpdateComboNum(i);
        }
        for(int k = 0; k < 16; k++)
            if(k <= 7)
                wing[k].regiment = OUR ? r01 : g01;
            else
                wing[k].regiment = OUR ? g01 : r01;

        for(int i = 0; i < 16; i++)
        {
            fillComboCountry(dlg[i].wCountry, i);
            selectCountries(i);
        }
        sParachuteOn.bChecked = true;
    }

    public GUIQuick(GWindowRoot gwindowroot)
    {
        super(14);
        Config.cur.ini.set("Mods", "PALMODsColor", Config.cur.ini.get("Mods", "PALMODsColor", 1, 0, 1));
//        MissionsFolder = Config.cur.ini.get("Mods", "PALQMBMissions", "Missions/QuickQMBPro/");
//        Config.cur.ini.set("Mods", "PALQMBMissions", Config.cur.ini.get("Mods", "PALQMBMissions", "QuickQMBPro"));
//        MissionsFolder = "Missions/" + Config.cur.ini.get("Mods", "PALQMBMissions", "QuickQMBPro") + "/";
        MissionsFolder = "Missions/QuickQMBPro/";
        PlaceholderLabel = "air.Placeholder";
        noneTargetNum = 0;
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
        b16Flights = true;
        OUR = true;
        bScramble = false;
        noneTarget = true;
        setPlaneList(Config.cur.ini.get("QMB", "PlaneList", 0, 0, 3));
        if(Config.cur.ini.get("QMB", "DumpPlaneList", 0, 0, 1) > 0)
            dumpFullPlaneList();
        try
        {
            File file = new File(MissionsFolder + "QMBair_" + pl + ".ini");
            if(file.exists())
            {
                if(pl > 1 && !checkCustomAirIni(MissionsFolder + "QMBair_" + pl + ".ini"))
                    pl = 0;
            } else
            if(pl > 1)
                pl = 0;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        r01 = "r01";
        r010 = "r010";
        g01 = "g01";
        g010 = "g010";
        filesTargetList = new ArrayList();
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
        infoMenu.info = localize("quick.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        GTexture gtexture1 = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons;
        bArmy = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 0.0F, 48F, 48F));
        bRandom = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bReset = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 144F, 48F, 48F));
        bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bBack = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bLoad = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bSave = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bFly = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        bNext = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bStat = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bDiff = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bFMB = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bBrief = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture1, 64F, 48F, 32F, 32F));
        bDate = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture1, 192F, 208F, 32F, 32F));
        ComboButtonWidth = gwindowroot.lookAndFeel().getHScrollBarW();
        wSituation = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wMap = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wPlaneList = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wTargetList = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
//        wTarget = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric(), 2.0F, ""));
        wTarget = "N/A";
        wPos = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wDefence = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wAltitude = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wWeather = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCldHeight = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wRandomWind = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wSetDate = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 2.0F, 2.0F, 20F, 2.0F, null));
        wSetDate.align = 1;
        wSetDate.bDelayedNotify = true;
        wSetDate.bEnableDoubleClick[0] = false;
        wSetDate.setValue("15.06.1940", false);
        wTimeHour = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wTimeMins = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wPosInFlight = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wPosInFlight.add("1. Flight Leader");
        wPosInFlight.add("2. Flight Wingman");
        wPosInFlight.add("3. Element Leader");
        wPosInFlight.add("4. Element Wingman");
        wPosInFlight.setEditable(false);
        wPosInFlight.posEnable = new boolean[4];
        wPosInFlight.posEnable[0] = true;
        wPosInFlight.posEnable[1] = false;
        wPosInFlight.posEnable[2] = false;
        wPosInFlight.posEnable[3] = false;
        wPosInFlight.setSelected(0, true, false);
        sParachuteOn = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sArrestorOn = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
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
        wTimeHour.add("00h");
        wTimeHour.add("01h");
        wTimeHour.add("02h");
        wTimeHour.add("03h");
        wTimeHour.add("04h");
        wTimeHour.add("05h");
        wTimeHour.add("06h");
        wTimeHour.add("07h");
        wTimeHour.add("08h");
        wTimeHour.add("09h");
        wTimeHour.add("10h");
        wTimeHour.add("11h");
        wTimeHour.add("12h");
        wTimeHour.add("13h");
        wTimeHour.add("14h");
        wTimeHour.add("15h");
        wTimeHour.add("16h");
        wTimeHour.add("17h");
        wTimeHour.add("18h");
        wTimeHour.add("19h");
        wTimeHour.add("20h");
        wTimeHour.add("21h");
        wTimeHour.add("22h");
        wTimeHour.add("23h");
        wTimeHour.setEditable(false);
        wTimeHour.editBox.align = wTimeHour.align = 1;
        wTimeHour.setSelected(12, true, false);
        wTimeMins.add("00m");
        wTimeMins.add("15m");
        wTimeMins.add("30m");
        wTimeMins.add("45m");
        wTimeMins.setEditable(false);
        wTimeMins.editBox.align = wTimeMins.align = 1;
        wTimeMins.setSelected(0, true, false);
        wWeather.add(localize("quick.CLE"));
        wWeather.add(localize("quick.GOO"));
        wWeather.add(localize("quick.HAZ"));
        wWeather.add(localize("quick.POO"));
        wWeather.add(localize("quick.BLI"));
        wWeather.add(localize("quick.RAI"));
        wWeather.add(localize("quick.THU"));
        wWeather.setEditable(false);
        wWeather.setSelected(0, true, false);
        wCldHeight.add("300");
        wCldHeight.add("500");
        wCldHeight.add("750");
        wCldHeight.add("1000");
        wCldHeight.add("1500");
        wCldHeight.add("2000");
        wCldHeight.add("3000");
        wCldHeight.add("4000");
        wCldHeight.add("5000");
        wCldHeight.setEditable(true);
        wCldHeight.setSelected(6, true, false);
        wCldHeight.setNumericOnly(true);
        wRandomWind.add(Plugin.i18n("None"));
        wRandomWind.add(Plugin.i18n("Low"));
        wRandomWind.add(Plugin.i18n("Moderate"));
        wRandomWind.add(Plugin.i18n("Strong"));
        wRandomWind.setEditable(false);
        wRandomWind.setSelected(0, true, false);
        wPlaneList.add(localize("quick.STD"));
        wPlaneList.add(localize("quick.ABC"));
        try
        {
            File file = new File(MissionsFolder + "QMBair_2.ini");
            if(file.exists() && checkCustomAirIni(MissionsFolder + "QMBair_2.ini"))
                wPlaneList.add(localize("quick.CUS1"));
            file = new File(MissionsFolder + "QMBair_3.ini");
            if(file.exists() && checkCustomAirIni(MissionsFolder + "QMBair_3.ini"))
                wPlaneList.add(localize("quick.CUS2"));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        wPlaneList.setEditable(false);
        wPlaneList.setSelected(getPlaneList(), true, false);
        wSituation.add(localize("quick.NON"));
        wSituation.add(localize("quick.ADV"));
        wSituation.add(localize("quick.DIS"));
        wSituation.setEditable(false);
        wSituation.setSelected(0, true, false);
        wTargetList.setEditable(false);
//        wTarget.setEditable(false);
        wDefence.add(localize("quick.NOND"));
        wDefence.add(localize("quick.AAA"));
        wDefence.setEditable(false);
        wDefence.setSelected(1, true, false);
        star = GTexture.New("GUI/Game/QM/star.mat");
        cross = GTexture.New("GUI/Game/QM/cross.mat");
        regList = new HashMapExt();
        regHash = new HashMapExt();
        countryLstRed = new ArrayList();
        countryLstBlue = new ArrayList();
        fillArrayCountry();
        for(int k = 0; k < 16; k++)
        {
            dlg[k] = new ItemDlg();
            dlg[k].wNum = (WComboNum)dialogClient.addControl(new WComboNum(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wSkill = (WComboSkill)dialogClient.addControl(new WComboSkill(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wPlane = (WComboPlane)dialogClient.addControl(new WComboPlane(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wLoadout = (WComboLoadout)dialogClient.addControl(new WComboLoadout(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wCountry = (WComboCountry)dialogClient.addControl(new WComboCountry(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
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

            dlg[k].wNum.editBox.align = dlg[k].wNum.align = 1;
            dlg[k].wNum.setSelected(0, true, false);
            dlg[k].wSkill.setEditable(false);
            dlg[k].wSkill.add(localize("quick.ROO"));
            dlg[k].wSkill.add(localize("quick.EXP"));
            dlg[k].wSkill.add(localize("quick.VET"));
            dlg[k].wSkill.add(localize("quick.ACE"));
            dlg[k].wSkill.setSelected(1, true, false);
            dlg[k].wPlane.setEditable(false);
            fillComboPlane(dlg[k].wPlane, k == 0);
            dlg[k].wLoadout.setEditable(false);
            fillComboWeapon(dlg[k].wLoadout, wing[k].plane, 0);
            dlg[k].wCountry.setEditable(false);
            fillComboCountry(dlg[k].wCountry, k);
        }

//        dlg[0].wPlane.listVisibleLines = Config.cur.ini.get("Mods", "PALQMBLinesPl0", ((GUIRoot)((GWindow) (gwindowroot)).root).isWide() ? 26 : 30);
//        dlg[0].wPlane.listVisibleLines = Config.cur.ini.get("Mods", "PALQMBLinesPl0", 24);
        dlg[0].wPlane.listVisibleLines = 24;
        dlg[0].wLoadout.listVisibleLines = 18;
        dlg[0].wCountry.listVisibleLines = 12;
        for(int i = 1; i < 16; i++)
        {
//            dlg[i].wPlane.listVisibleLines = Config.cur.ini.get("Mods", "PALQMBLinesPl" + i, 24);
            dlg[i].wPlane.listVisibleLines = 24;
            dlg[i].wLoadout.listVisibleLines = 18;
            dlg[i].wCountry.listVisibleLines = 12;
        }
//        wMap.listVisibleLines = Config.cur.ini.get("Mods", "PALQMBLinesMap", 24);
        wMap.listVisibleLines = 24;
        fillMapKey();
        if(_mapKey.length > 0)
            wMap.setSelected(0, true, false);
        else
            wMap.setSelected(-1, false, false);
        wMap.setEditable(false);
        onArmyChange();
        defaultRegiments(true);
        updateCountries();
        dialogClient.activateWindow();
        client.hideWindow();
    }

    private int bUseColor;
    private int colorRed;
    private int colorBlue;
    private String MissionsFolder;
    public ArrayList countryLstRed;
    public ArrayList countryLstBlue;
    public ResourceBundle resCountry;
    public HashMapExt regList;
    public HashMapExt regHash;
    private String PlaceholderLabel;
    private String music;
    private String sDesc;
    private String sShort;
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
    public GUIButton bRandom;
    public GUIButton bReset;
    public GUIButton bFMB;
    public GUIButton bBrief;
    public GUIButton bDate;
    public GUISwitchBox3 sParachuteOn;
    public GUISwitchBox3 sArrestorOn;
    public GWindowComboControl wSituation;
    public GWindowComboControl wMap;
//    public GWindowEditControl wTarget;
    private String wTarget;
    public GWindowComboControl wTargetList;
    public GWindowComboControl wPos;
    public GWindowComboControl wDefence;
    public GWindowComboControl wAltitude;
    public GWindowComboControl wWeather;
    public GWindowComboControl wCldHeight;
    public GWindowComboControl wRandomWind;
    public GWindowEditControl wSetDate;
    public GWindowComboControl wTimeHour;
    public GWindowComboControl wTimeMins;
    public GWindowComboControl wPlaneList;
    public GWindowComboControl wPosInFlight;
    private String r01;
    private String r010;
    private String g01;
    private String g010;
    private String _mapKey[];
    private String folderNames[];
    private String _targetKey[] = {
        "None", "Armor", "Bridge", "Airbase", "Scramble", "Strike"
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
    private boolean b16Flights;
    private ArrayList filesTargetList;
    static boolean bIsQuick;
    private static int pl;
    private boolean noneTarget;
    private int noneTargetNum;
    private static final int NONE = 0;
    private static final int ARMOR = 1;
    private static final int BRIDGE = 2;
    private static final int AIRBASE = 3;
    private static final int SCRAMBLE = 4;
    private static final int STRIKE = 4;
    private String currentMissionName;
    private boolean bNoAvailableMissions;
    private String planeListName;
    private String russianMixedRules;
    private RuleBasedCollator collator;
//    static Class class$com$maddox$il2$objects$air$TypeTransport;
    private static ArrayList aKnownGoodMissions;
    private static ArrayList aKnownBadMissions;
    private static ArrayList aKnownBadMaps;
    protected float ComboButtonWidth;

    static
    {
        aKnownGoodMissions = new ArrayList();
        aKnownBadMissions = new ArrayList();
        aKnownBadMaps = new ArrayList();
        ObjIO.fields(com.maddox.il2.gui.GUIQuick$IOState.class, new String[] {
            "our", "situation", "map", "target", "defence", "altitude", "weather", "timeH", "timeM", "pos",
            "cldheight", "scramble", "noneTARGET", "posInFlight", "date", "wind"
        });
        ObjIO.validate(com.maddox.il2.gui.GUIQuick$IOState.class, "loaded");
        ObjIO.fields(com.maddox.il2.gui.GUIQuick$ItemWing.class, new String[] {
            "planes", "weapon", "regiment", "skin", "noseart", "pilot", "numberOn", "fuel", "skill"
        });
        ObjIO.accessStr(com.maddox.il2.gui.GUIQuick$ItemWing.class, "plane", "getPlane", "setPlane");
        ObjIO.validate(com.maddox.il2.gui.GUIQuick$ItemWing.class, "loaded");
        aKnownGoodMissions.clear();
        aKnownBadMissions.clear();
        aKnownBadMaps.clear();
    }
}
