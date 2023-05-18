package com.maddox.il2.gui;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.*;
import com.maddox.il2.objects.ActorSimpleHMesh;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.util.HashMapExt;
import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class GUIAirArming extends GameState
{
    static class UserRegiment
    {
        protected String country;
        protected String branch;
        protected String fileName;
        protected char id[];
        protected String shortInfo;
        protected int gruppeNumber;

        public UserRegiment(String s)
            throws Exception
        {
            id = new char[2];
            gruppeNumber = 1;
            fileName = s;
            String s1 = Main.cur().netFileServerReg.primaryPath();
            PropertyResourceBundle propertyresourcebundle = new PropertyResourceBundle(new SFSInputStream(s1 + "/" + s));
            country = propertyresourcebundle.getString("country");
            country = country.toLowerCase().intern();
            branch = country;
            country = Regiment.getCountryFromBranch(branch);
            String s2 = propertyresourcebundle.getString("id");
            id[0] = s2.charAt(0);
            id[1] = s2.charAt(1);
            if((id[0] < '0' || id[0] > '9') && (id[0] < 'A' || id[0] > 'Z'))
                throw new RuntimeException("Bad regiment id[0]");
            if((id[1] < '0' || id[1] > '9') && (id[1] < 'A' || id[1] > 'Z'))
                throw new RuntimeException("Bad regiment id[1]");
            try
            {
                String s3 = propertyresourcebundle.getString("short");
                if(s3 == null || s3.length() == 0)
                    s3 = s;
                shortInfo = s3;
            }
            catch(Exception exception)
            {
                shortInfo = s;
            }
            try
            {
                String s4 = propertyresourcebundle.getString("gruppeNumber");
                if(s4 != null)
                {
                    try
                    {
                        gruppeNumber = Integer.parseInt(s4);
                    }
                    catch(Exception exception2) { }
                    if(gruppeNumber < 1)
                        gruppeNumber = 1;
                    if(gruppeNumber > 5)
                        gruppeNumber = 5;
                }
            }
            catch(Exception exception1) { }
        }
    }

    public class DialogClient extends GUIDialogClient
    {
        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            UserCfg usercfg = World.cur().userCfg;
            if(gwindow == bJoy)
            {
                Main.stateStack().push(53);
                return true;
            }
            if(gwindow == bRand)
            {
                randomAircraft();
                return true;
            }
            if(gwindow == bView)
            {
                toAirViewer();
                return true;
            }
            if(gwindow == cAircraft)
            {
                if(GUIAirArming.stateId != 2 && GUIAirArming.stateId != 4)
                    return true;
                if(GUIAirArming.stateId == 2)
                    usercfg.netAirName = airName();
                else
                if(GUIAirArming.stateId == 4)
                    quikPlane = airName();
                if(zutiSelectedBornPlace == null)
                    fillWeapons();
                else
                    zutiFillWeapons(zutiSelectedBornPlace);
                selectWeapon();
                fillSkins();
                selectSkin();
                selectNoseart();
                setMesh();
                prepareMesh();
                prepareWeapons();
                prepareSkin();
                preparePilot();
                prepareNoseart();
                return true;
            }
            if(gwindow == cCountry)
            {
                if(GUIAirArming.stateId != 2 && GUIAirArming.stateId != 4)
                    return true;
                fillRegiments();
                String s = (String)countryLst.get(cCountry.getSelected());
                ArrayList arraylist = (ArrayList)regList.get(s);
                Object obj = arraylist.get(cRegiment.getSelected());
                if(GUIAirArming.stateId == 2)
                {
                    if(obj instanceof Regiment)
                        usercfg.netRegiment = ((Regiment)obj).name();
                    else
                        usercfg.netRegiment = ((UserRegiment)obj).fileName;
                } else
                if(GUIAirArming.stateId == 4)
                    quikRegiment = ((Regiment)obj).name();
                selectNoseart();
                setMesh();
                prepareMesh();
                prepareWeapons();
                prepareSkin();
                preparePilot();
                prepareNoseart();
                return true;
            }
            if(gwindow == cRegiment)
            {
                if(GUIAirArming.stateId != 2 && GUIAirArming.stateId != 4)
                    return true;
                String s1 = (String)countryLst.get(cCountry.getSelected());
                ArrayList arraylist1 = (ArrayList)regList.get(s1);
                Object obj1 = arraylist1.get(cRegiment.getSelected());
                if(GUIAirArming.stateId == 2)
                {
                    if(obj1 instanceof Regiment)
                        usercfg.netRegiment = ((Regiment)obj1).name();
                    else
                        usercfg.netRegiment = ((UserRegiment)obj1).fileName;
                } else
                if(GUIAirArming.stateId == 4)
                    quikRegiment = ((Regiment)obj1).name();
                prepareMesh();
                prepareSkin();
                return true;
            }
            if(gwindow == cWeapon)
            {
                if(!bEnableWeaponChange)
                    return true;
                if(isNet())
                    usercfg.setWeapon(airName(), (String)weaponNames.get(cWeapon.getSelected()));
                else
                if(GUIAirArming.stateId == 4)
                    quikWeapon = (String)weaponNames.get(cWeapon.getSelected());
                else
                    Main.cur().currentMissionFile.set(planeName, "weapons", (String)weaponNames.get(cWeapon.getSelected()));
                prepareWeapons();
                return true;
            }
            if(gwindow == cSkin)
            {
                int k = cSkin.getSelected();
                if(GUIAirArming.stateId == 4)
                {
                    if(k == 0)
                        quikSkin[quikCurPlane] = null;
                    else
                        quikSkin[quikCurPlane] = cSkin.get(k);
                } else
                if(k == 0)
                    usercfg.setSkin(airName(), null);
                else
                    usercfg.setSkin(airName(), cSkin.get(k));
                prepareSkin();
                return true;
            }
            if(gwindow == cNoseart)
            {
                int l = cNoseart.getSelected();
                if(GUIAirArming.stateId == 4)
                {
                    if(l == 0)
                        quikNoseart[quikCurPlane] = null;
                    else
                        quikNoseart[quikCurPlane] = cNoseart.get(l);
                } else
                if(l == 0)
                    usercfg.setNoseart(airName(), null);
                else
                    usercfg.setNoseart(airName(), cNoseart.get(l));
                if(l == 0)
                {
                    setMesh();
                    prepareMesh();
                    prepareWeapons();
                    prepareSkin();
                    preparePilot();
                }
                prepareNoseart();
                return true;
            }
            if(gwindow == cPilot)
            {
                if(GUIAirArming.stateId == 4)
                {
                    if(cPilot.getSelected() == 0)
                        quikPilot[quikCurPlane] = null;
                    else
                        quikPilot[quikCurPlane] = cPilot.getValue();
                } else
                if(cPilot.getSelected() == 0)
                    usercfg.netPilot = null;
                else
                    usercfg.netPilot = cPilot.getValue();
                preparePilot();
                return true;
            }
            if(gwindow == wMashineGun)
            {
                usercfg.coverMashineGun = clampValue(wMashineGun, usercfg.coverMashineGun, 100F, 1000F);
                return true;
            }
            if(gwindow == wCannon)
            {
                usercfg.coverCannon = clampValue(wCannon, usercfg.coverCannon, 100F, 1000F);
                return true;
            }
            if(gwindow == wRocket)
            {
                usercfg.coverRocket = clampValue(wRocket, usercfg.coverRocket, 100F, 1000F);
                return true;
            }
            if(gwindow == wRocketDelay)
            {
                usercfg.rocketDelay = clampValue(wRocketDelay, usercfg.rocketDelay, 1.0F, 60F);
                return true;
            }
            if(gwindow == wBombDelay)
            {
                usercfg.bombDelay = clampValue(wBombDelay, usercfg.bombDelay, 0.0F, 10F);
                return true;
            }
            if(gwindow == cFuel)
            {
                if(bEnableWeaponChange)
                    if(isNet())
                        usercfg.fuel = (cFuel.getSelected() + 1) * 10;
                    else
                    if(GUIAirArming.stateId == 4)
                        quikFuel = (cFuel.getSelected() + 1) * 10;
                    else
                        Main.cur().currentMissionFile.set(planeName, "Fuel", (cFuel.getSelected() + 1) * 10);
            } else
            {
                if(gwindow == cSquadron)
                    if(GUIAirArming.stateId != 2)
                    {
                        return true;
                    } else
                    {
                        usercfg.netSquadron = cSquadron.getSelected();
                        prepareMesh();
                        return true;
                    }
                if(gwindow == cFuzeDelay)
                {
                    setFuzeDelay();
                    return true;
                }
                if(gwindow == cFuzeType)
                {
                    setFuzeType();
                    return true;
                }
                if(gwindow == wNumber)
                {
                    if(GUIAirArming.stateId != 2)
                        return true;
                    String s2 = wNumber.getValue();
                    int i1 = usercfg.netTacticalNumber;
                    try
                    {
                        i1 = Integer.parseInt(s2);
                    }
                    catch(Exception exception) { }
                    if(i1 < 1)
                        i1 = 1;
                    if(i1 > 99)
                        i1 = 99;
                    wNumber.setValue("" + i1, false);
                    usercfg.netTacticalNumber = i1;
                    prepareMesh();
                    return true;
                }
                if(gwindow == sNumberOn)
                {
                    if(GUIAirArming.stateId == 4)
                        quikNumberOn[quikCurPlane] = sNumberOn.isChecked();
                    else
                        usercfg.netNumberOn = sNumberOn.isChecked();
                    prepareMesh();
                } else
                {
                    if(gwindow == cPlane)
                    {
                        if(GUIAirArming.stateId != 4)
                            return true;
                        quikCurPlane = cPlane.getSelected();
                        if(quikPlayer && quikCurPlane == quikPlayerPosition)
                        {
                            wMashineGun.showWindow();
                            wCannon.showWindow();
                            wRocket.showWindow();
                            wRocketDelay.showWindow();
                            if(isFuzesEnabled())
                            {
                                wBombDelay.hideWindow();
                                cFuzeDelay.showWindow();
                                cFuzeType.showWindow();
                            } else
                            {
                                wBombDelay.showWindow();
                                cFuzeDelay.hideWindow();
                                cFuzeType.hideWindow();
                            }
                        } else
                        {
                            wMashineGun.hideWindow();
                            wCannon.hideWindow();
                            wRocket.hideWindow();
                            wRocketDelay.hideWindow();
                            wBombDelay.hideWindow();
                            cFuzeDelay.hideWindow();
                            cFuzeType.hideWindow();
                        }
                        sNumberOn.setChecked(quikNumberOn[quikCurPlane], false);
                        fillSkins();
                        selectSkin();
                        selectNoseart();
                        fillPilots();
                        selectPilot();
                        setMesh();
                        prepareMesh();
                        prepareWeapons();
                        prepareSkin();
                        preparePilot();
                        prepareNoseart();
                        return true;
                    }
                    if(gwindow == bBack)
                    {
                        switch(GUIAirArming.stateId)
                        {
                        case 2: // '\002'
                            ((NetUser)NetEnv.host()).replicateNetUserRegiment();
                            // fall through

                        case 3: // '\003'
                            ((NetUser)NetEnv.host()).replicateSkin();
                            ((NetUser)NetEnv.host()).replicateNoseart();
                            ((NetUser)NetEnv.host()).replicatePilot();
                            // fall through

                        case 4: // '\004'
                        default:
                            usercfg.saveConf();
                            destroyMesh();
                            airNames.clear();
                            cAircraft.clear(false);
                            regList.clear();
                            regHash.clear();
                            cRegiment.clear(false);
                            countryLst.clear();
                            cCountry.clear(false);
                            cWeapon.clear();
                            weaponNames.clear();
                            cSkin.clear(false);
                            cPilot.clear(false);
                            cNoseart.clear(false);
                            if(Main.stateStack().getPrevious() != null && Main.stateStack().getPrevious().id() != 54)
                            {
                                quikPlayer = false;
                                quikCurPlane = 0;
                                quikPlayerPosition = 0;
                            }
                            World.cur().setUserCovers();
                            Main.stateStack().pop();
                            fuzeMap.clear();
                            return true;
                        }
                    }
                }
            }
            return super.notify(gwindow, i, j);
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(640F), x1024(962F), 2.0F);
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
// SingleMission & QMB
//            if(GUIAirArming.stateId == 4)
            if(GUIAirArming.stateId == 4 || quikPlayer)
                draw(x1024(628F), y1024(22F), x1024(360F), M(2.0F), 0, i18n("quick.+/-") + (quikPlayer && quikCurPlane == quikPlayerPosition ? " (" + i18n("netair.Player") + ")" : ""));
            else
                draw(x1024(628F), y1024(22F), x1024(360F), M(2.0F), 0, "(" + i18n("netair.Player") + ")");
            draw(x1024(644F), y1024(22F), x1024(332F), M(2.0F), 1, i18n("neta.Aircraft"));
            draw(x1024(644F), y1024(86F), x1024(332F), M(2.0F), 1, i18n("neta.WeaponLoadout"));
            draw(x1024(644F), y1024(150F), x1024(332F), M(2.0F), 1, i18n("neta.Country"));
            draw(x1024(644F), y1024(214F), x1024(332F), M(2.0F), 1, i18n("neta.Regiment"));
            draw(x1024(644F), y1024(278F), x1024(332F), M(2.0F), 1, i18n("neta.Skin"));
            draw(x1024(644F), y1024(342F), x1024(332F), M(2.0F), 1, i18n("neta.Pilot"));
// Multiplayer
            if(GUIAirArming.stateId == 2)
            {
                draw(x1024(542F), y1024(492F), x1024(160F), M(2.0F), 2, i18n("neta.Number") + " ");
                draw(x1024(734F), y1024(492F), x1024(160F), M(2.0F), 2, i18n("neta.Squadron") + " ");
            } else
            {
                draw(x1024(564F), y1024(492F), x1024(220F), M(2.0F), 2, i18n("neta.NumberOn"));
            }
            GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
//            if(GUIAirArming.stateId != 4 || quikPlayer && quikCurPlane == quikPlayerPosition)
            if(isNet() || (quikPlayer && quikCurPlane == quikPlayerPosition))
            {
                draw(x1024(42F), y1024(412F), x1024(576F), M(2.0F), 1, i18n("neta.WeaponConver"));
                draw(x1024(4F), y1024(460F), x1024(160F), M(2.0F), 2, i18n("neta.MachineGuns") + " ");
                draw(x1024(293F), y1024(460F), x1024(160F), M(2.0F), 2, i18n("neta.Cannons") + " ");
                draw(x1024(246F), y1024(460F), x1024(48F), M(2.0F), 0, " " + i18n("neta.m."));
                draw(x1024(535F), y1024(460F), x1024(48F), M(2.0F), 0, " " + i18n("neta.m."));
                draw(x1024(4F), y1024(524F), x1024(160F), M(2.0F), 2, i18n("neta.Rockets") + " ");
                draw(x1024(293F), y1024(524F), x1024(160F), M(2.0F), 2, i18n("neta.RocketDelay") + " ");
                draw(x1024(246F), y1024(524F), x1024(48F), M(2.0F), 0, " " + i18n("neta.m."));
                draw(x1024(535F), y1024(524F), x1024(48F), M(2.0F), 0, " " + i18n("neta.sec."));
                boolean dash = false;
                for(int dashCount = 0; dashCount <= 16 * 32; dashCount += 16)
                {
                    if(dash)
                    {
                        GUISeparate.draw(this, GColor.Gray, x1024(64F + (float)dashCount), y1024(504F), x1024(16F), 1.0F);
                        GUISeparate.draw(this, GColor.Gray, x1024(64F + (float)dashCount), y1024(566F), x1024(16F), 1.0F);
                    }
                    dash = !dash;
                }
                if(isFuzesEnabled())
                {
                    draw(x1024(4F), y1024(588F), x1024(160F), M(2.0F), 2, i18n("neta.BombFuze") + " ");
                    if(getSelectedFuzeType() == 8)
                    {
                        int l5 = getSelectedFuzeType();
                        String l6 = cFuzeDelay.getValue();
                        float l7 = Float.parseFloat(l6);
                        float l8 = (l7 + 0.5F) / 0.3048F - 1.0F;
                        int l9 = Math.round(l8);
                        draw(x1024(293F), y1024(588F), x1024(160F), M(2.0F), 2, i18n("neta.BombFuzeAlt") + " ");
                        draw(x1024(535F), y1024(588F), x1024(48F), M(2.0F), 2, " " + i18n("neta.m.") + " (" + l9 + " " + i18n("neta.ft.") + ")");
                    } else
                    if(getSelectedFuzeType() == 9)
                    {
                        int k = getSelectedFuzeType();
                        String k1 = cFuzeDelay.getValue();
                        float k2 = Float.parseFloat(k1);
                        float k3 = (k2 * k2) / 0.071323F;
                        int k4 = Math.round(k3);
                        draw(x1024(293F), y1024(588F), x1024(160F), M(2.0F), 2, i18n("neta.BombFuzeDelay") + " ");
                        draw(x1024(535F), y1024(588F), x1024(48F), M(2.0F), 0, " " + i18n("neta.sec.") + " (" + k4 + " " + i18n("neta.ft.") + ")");
                    } else
                    if(getSelectedFuzeType() == 10)
                    {
                        int k5 = getSelectedFuzeType();
                        String k6 = cFuzeDelay.getValue();
                        float k7 = Float.parseFloat(k6);
                        float k8 = (k7 + 0.5F) / 0.3048F - 1.0F;
                        int k9 = Math.round(k8);
                        draw(x1024(293F), y1024(588F), x1024(160F), M(2.0F), 2, i18n("neta.BombFuzeAlt") + " ");
                        draw(x1024(535F), y1024(588F), x1024(48F), M(2.0F), 0, " " + i18n("neta.m.") + " (" + k9 + " " + i18n("neta.ft.") + ")");
                    } else
                    {
                        draw(x1024(293F), y1024(588F), x1024(160F), M(2.0F), 2, i18n("neta.BombFuzeDelay") + " ");
                        draw(x1024(535F), y1024(588F), x1024(48F), M(2.0F), 0, " " + i18n("neta.sec."));
                    }
                } else
                {
                    draw(x1024(4F), y1024(588F), x1024(160F), M(2.0F), 2, i18n("neta.BombDelay") + " ");
                    draw(x1024(246F), y1024(588F), x1024(48F), M(2.0F), 0, " " + i18n("neta.sec."));
                }
            }
            draw(x1024(626F), y1024(556F), x1024(160F), M(2.0F), 2, i18n("neta.FuelQuantity") + " ");
            draw(x1024(884F), y1024(556F), x1024(48F), M(2.0F), 0, " %");
            draw(x1024(96F), y1024(656F), x1024(320F), y1024(48F), 0, i18n("neta.Apply"));
            draw(x1024(326F), y1024(656F), x1024(620F), y1024(48F), 0, i18n("neta.Joystick"));
            if(GUIAirArming.stateId == 4)
            {
                draw(x1024(691F), y1024(656F), x1024(620F), y1024(48F), 0, i18n("Random"));
                draw(x1024(921F), y1024(656F), x1024(620F), y1024(48F), 0, i18n("Hangar"));
            }
            if(cNoseart.isVisible())
                draw(x1024(644F), y1024(406F), x1024(332F), M(2.0F), 1, i18n("neta.Noseart"));
            setCanvasColorWHITE();
            guilookandfeel.drawBevel(this, x1024(32F), y1024(32F), x1024(564F), y1024(367F), guilookandfeel.bevelComboDown, guilookandfeel.basicelements);
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            wMashineGun.setPosSize(x1024(166F), y1024(460F), x1024(80F), M(1.7F));
            wCannon.setPosSize(x1024(455F), y1024(460F), x1024(80F), M(1.7F));
            wRocket.setPosSize(x1024(166F), y1024(524F), x1024(80F), M(1.7F));
            wRocketDelay.setPosSize(x1024(455F), y1024(524F), x1024(80F), M(1.7F));
            wBombDelay.setPosSize(x1024(166F), y1024(588F), x1024(80F), M(1.7F));
            GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
            cFuzeType.setPosSize(x1024(166F), y1024(588F), x1024(170F), M(1.7F));
            cFuzeDelay.setPosSize(x1024(455F), y1024(588F), x1024(80F), M(1.7F));
            cFuel.setPosSize(x1024(788F), y1024(556F), x1024(96F), M(1.7F));
//            if(GUIAirArming.stateId == 4)
            if(GUIAirArming.stateId == 4 || quikPlayer)
                cAircraft.setPosSize(x1024(682F), y1024(54F), x1024(310F), M(1.7F));
            else
                cAircraft.setPosSize(x1024(628F), y1024(54F), x1024(364F), M(1.7F));
            cPlane.setPosSize(x1024(628F), y1024(54F), x1024(48F), M(1.7F));
            cWeapon.setPosSize(x1024(628F), y1024(118F), x1024(364F), M(1.7F));
            cCountry.setPosSize(x1024(628F), y1024(182F), x1024(364F), M(1.7F));
            cRegiment.setPosSize(x1024(628F), y1024(246F), x1024(364F), M(1.7F));
            cSkin.setPosSize(x1024(628F), y1024(310F), x1024(364F), M(1.7F));
            cPilot.setPosSize(x1024(628F), y1024(374F), x1024(364F), M(1.7F));
            wNumber.setPosSize(x1024(704F), y1024(492F), x1024(64F), M(1.7F));
            cSquadron.setPosSize(x1024(896F), y1024(492F), x1024(64F), M(1.7F));
            float f = 16F;
            float f1 = root.win.dx / root.win.dy;
            if(f1 < 1.0F)
                f /= 2.0F;
            sNumberOn.setPosC(x1024(846F), y1024(492F + f));
            cNoseart.setPosSize(x1024(628F), y1024(438F), x1024(364F), M(1.7F));
            GUILookAndFeel guilookandfeel1 = (GUILookAndFeel)lookAndFeel();
            GBevel gbevel = guilookandfeel1.bevelComboDown;
            renders.setPosSize(x1024(32F) + gbevel.L.dx, y1024(32F) + gbevel.T.dy, x1024(564F) - gbevel.L.dx - gbevel.R.dx, y1024(367F) - gbevel.T.dy - gbevel.B.dy);
            bBack.setPosC(x1024(56F), y1024(680F));
            bJoy.setPosC(x1024(286F), y1024(680F));
            bRand.setPosC(x1024(651F), y1024(680F));
            setShow(GUIAirArming.stateId == 4, bRand);
            bView.setPosC(x1024(881F), y1024(680F));
            setShow(GUIAirArming.stateId == 4, bView);
        }

        public DialogClient()
        {
        }
    }

    class _Render3D extends Render
    {

        public void preRender()
        {
            if(Actor.isValid(actorMesh))
            {
                if(animateMeshA != 0.0F || animateMeshT != 0.0F)
                {
                    actorMesh.pos.getAbs(_orient);
                    _orient.set(_orient.azimut() + animateMeshA * client.root.deltaTimeSec, _orient.tangage() + animateMeshT * client.root.deltaTimeSec, 0.0F);
                    float f;
                    for(f = _orient.getYaw(); f > 360F; f -= 360F);
                    for(; f < 0.0F; f += 360F);
                    _orient.setYaw(f);
                    actorMesh.pos.setAbs(_orient);
                    actorMesh.pos.reset();
                }
                actorMesh.draw.preRender(actorMesh);
                for(int i = 0; i < weaponMeshs.size(); i++)
                {
                    ActorMesh actormesh = (ActorMesh)weaponMeshs.get(i);
                    if(Actor.isValid(actormesh))
                        actormesh.draw.preRender(actormesh);
                }

            }
        }

        public void render()
        {
            if(Actor.isValid(actorMesh))
            {
                Render.prepareStates();
                actorMesh.draw.render(actorMesh);
                for(int i = 0; i < weaponMeshs.size(); i++)
                {
                    ActorMesh actormesh = (ActorMesh)weaponMeshs.get(i);
                    if(Actor.isValid(actormesh))
                        actormesh.draw.render(actormesh);
                }

            }
        }

        public _Render3D(Renders renders1, float f)
        {
            super(renders1, f);
            setClearColor(GWindowLookAndFeel.getUIBackgroundColor());
            useClearStencil(true);
        }
    }

    private void setShow(boolean flag, GWindow gwindow)
    {
        if(flag)
            gwindow.showWindow();
        else
            gwindow.hideWindow();
    }

    private boolean isNet()
    {
        return stateId == 2 || stateId == 3;
    }

    private String airName()
    {
        return (String)airNames.get(cAircraft.getSelected());
    }

    public void _enter()
    {
        render3D.setClearColor(GWindowLookAndFeel.getUIBackgroundColor());
        zutiSelectedBornPlace = null;
        try
        {
            if(resCountry == null)
                resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
            bEnableWeaponChange = true;
            cFuel.setEnable(true);
            cPlane.setEnable(true);
            UserCfg usercfg = World.cur().userCfg;
            wMashineGun.setValue("" + usercfg.coverMashineGun, false);
            wCannon.setValue("" + usercfg.coverCannon, false);
            wRocket.setValue("" + usercfg.coverRocket, false);
            wRocketDelay.setValue("" + usercfg.rocketDelay, false);
            wBombDelay.setValue("" + usercfg.bombDelay, false);
            SectFile sectfile = Main.cur().currentMissionFile;
            if(sectfile == null)
            {
                World.cur().setWeaponsConstant(false);
            } else
            {
                int j = sectfile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
                World.cur().setWeaponsConstant(j == 1);
            }
            switch(stateId)
            {
            case 0: // '\0'
            case 1: // '\001'
                if(quikPlayer)
                {
                    if(quikCurPlane == quikPlayerPosition)
                    {
                        wMashineGun.showWindow();
                        wCannon.showWindow();
                        wRocket.showWindow();
                        wRocketDelay.showWindow();
                        setFuzeControlsVisibility();
                    } else
                    {
                        wMashineGun.hideWindow();
                        wCannon.hideWindow();
                        wRocket.hideWindow();
                        wRocketDelay.hideWindow();
                        wBombDelay.hideWindow();
                        cFuzeDelay.hideWindow();
                        cFuzeType.hideWindow();
                    }
                    cPlane.showWindow();
                } else
                {
                    wMashineGun.showWindow();
                    wCannon.showWindow();
                    wRocket.showWindow();
                    wRocketDelay.showWindow();
                    setFuzeControlsVisibility();
                    cPlane.hideWindow();
                }
                wNumber.hideWindow();
                cSquadron.hideWindow();
                sNumberOn.showWindow();
                sNumberOn.setChecked(usercfg.netNumberOn, false);
                SectFile sectfile1 = Main.cur().currentMissionFile;
                String s = sectfile1.get("MAIN", "player", (String)null);
                planeName = s;
                String s1 = s.substring(0, s.length() - 1);
                String s2 = s1.substring(0, s1.length() - 1);
                Regiment regiment = (Regiment)Actor.getByName(s2);
                String s4 = sectfile1.get(planeName, "Class", (String)null);
                Class class2 = ObjIO.classForName(s4);
                String s6 = Property.stringValue(class2, "keyName", null);
                airNames.add(s6);
                cAircraft.add((Property.containsValue(class2, "cockpitClass") ? "" : "(AI) ") + I18N.plane(s6));
                cAircraft.setSelected(0, true, false);
                countryLst.add(regiment.branch());
                cCountry.add(resCountry.getString(regiment.branch()));
                cCountry.setSelected(0, true, false);
                ArrayList arraylist4 = new ArrayList();
                arraylist4.add(regiment);
                regList.put(regiment.branch(), arraylist4);
                cRegiment.add(regiment.shortInfo());
                cRegiment.setSelected(0, true, false);
                int l2 = sectfile1.get(planeName, "Fuel", 100, 0, 100);
                if(l2 <= 10)
                    cFuel.setSelected(0, true, false);
                else
                if(l2 <= 20)
                    cFuel.setSelected(1, true, false);
                else
                if(l2 <= 30)
                    cFuel.setSelected(2, true, false);
                else
                if(l2 <= 40)
                    cFuel.setSelected(3, true, false);
                else
                if(l2 <= 50)
                    cFuel.setSelected(4, true, false);
                else
                if(l2 <= 60)
                    cFuel.setSelected(5, true, false);
                else
                if(l2 <= 70)
                    cFuel.setSelected(6, true, false);
                else
                if(l2 <= 80)
                    cFuel.setSelected(7, true, false);
                else
                if(l2 <= 90)
                    cFuel.setSelected(8, true, false);
                else
                    cFuel.setSelected(9, true, false);
                if(quikPlayer)
                {
                    bEnableWeaponChange = quikCurPlane == quikPlayerPosition && quikPlayerPosition == 0 && !World.cur().isWeaponsConstant();
                } else
                {
                    playerNum = sectfile1.get("Main", "playerNum", 0);
                    if(stateId == 1)
                        bEnableWeaponChange = playerNum == 0 && !World.cur().isWeaponsConstant();
                    else
                        bEnableWeaponChange = !World.cur().isWeaponsConstant();
                }
                cFuel.setEnable(bEnableWeaponChange);
                if(quikPlayer)
                {
                    cPlane.clear(false);
                    cPlane.add("" + (quikCurPlane + 1));
                    cPlane.setSelected(0, true, false);
                    cPlane.setEnable(false);
                }
                break;

            case 3: // '\003'
                playerNum = -1;
                wMashineGun.showWindow();
                wCannon.showWindow();
                wRocket.showWindow();
                wRocketDelay.showWindow();
                setFuzeControlsVisibility();
                cPlane.hideWindow();
                wNumber.hideWindow();
                cSquadron.hideWindow();
                sNumberOn.showWindow();
                sNumberOn.setChecked(usercfg.netNumberOn, false);
                planeName = GUINetAircraft.selectedWingName();
                bEnableWeaponChange = !World.cur().isWeaponsConstant();
                int i = (int)usercfg.fuel;
                if(!bEnableWeaponChange)
                {
                    SectFile sectfile2 = Main.cur().currentMissionFile;
                    i = sectfile2.get(planeName, "Fuel", 100, 0, 100);
                }
                if(i <= 10)
                    cFuel.setSelected(0, true, false);
                else
                if(i <= 20)
                    cFuel.setSelected(1, true, false);
                else
                if(i <= 30)
                    cFuel.setSelected(2, true, false);
                else
                if(i <= 40)
                    cFuel.setSelected(3, true, false);
                else
                if(i <= 50)
                    cFuel.setSelected(4, true, false);
                else
                if(i <= 60)
                    cFuel.setSelected(5, true, false);
                else
                if(i <= 70)
                    cFuel.setSelected(6, true, false);
                else
                if(i <= 80)
                    cFuel.setSelected(7, true, false);
                else
                if(i <= 90)
                    cFuel.setSelected(8, true, false);
                else
                    cFuel.setSelected(9, true, false);
                cFuel.setEnable(bEnableWeaponChange);
                airNames.add(GUINetAircraft.selectedAircraftKeyName());
                cAircraft.add(GUINetAircraft.selectedAircraftName());
                cAircraft.setSelected(0, true, false);
                countryLst.add(GUINetAircraft.selectedRegiment().branch());
                cCountry.add(resCountry.getString(GUINetAircraft.selectedRegiment().branch()));
                cCountry.setSelected(0, true, false);
                ArrayList arraylist1 = new ArrayList();
                arraylist1.add(GUINetAircraft.selectedRegiment());
                regList.put(GUINetAircraft.selectedRegiment().branch(), arraylist1);
                cRegiment.add(GUINetAircraft.selectedRegiment().shortInfo());
                cRegiment.setSelected(0, true, false);
                break;

            case 2: // '\002'
                playerNum = -1;
                wMashineGun.showWindow();
                wCannon.showWindow();
                wRocket.showWindow();
                wRocketDelay.showWindow();
                setFuzeControlsVisibility();
                cPlane.hideWindow();
                wNumber.showWindow();
                cSquadron.showWindow();
                sNumberOn.hideWindow();
                if(usercfg.fuel <= 10F)
                    cFuel.setSelected(0, true, false);
                else
                if(usercfg.fuel <= 20F)
                    cFuel.setSelected(1, true, false);
                else
                if(usercfg.fuel <= 30F)
                    cFuel.setSelected(2, true, false);
                else
                if(usercfg.fuel <= 40F)
                    cFuel.setSelected(3, true, false);
                else
                if(usercfg.fuel <= 50F)
                    cFuel.setSelected(4, true, false);
                else
                if(usercfg.fuel <= 60F)
                    cFuel.setSelected(5, true, false);
                else
                if(usercfg.fuel <= 70F)
                    cFuel.setSelected(6, true, false);
                else
                if(usercfg.fuel <= 80F)
                    cFuel.setSelected(7, true, false);
                else
                if(usercfg.fuel <= 90F)
                    cFuel.setSelected(8, true, false);
                else
                    cFuel.setSelected(9, true, false);
                NetUser netuser = (NetUser)NetEnv.host();
                int k = netuser.getBornPlace();
                BornPlace bornplace = (BornPlace)World.cur().bornPlaces.get(k);
                zutiSelectedBornPlace = bornplace;
                ArrayList arraylist2 = zutiSelectedBornPlace.zutiHomeBaseCountries;
                ArrayList arraylist3 = zutiSelectedBornPlace.zutiGetAvailablePlanesList();
                if(arraylist3 != null)
                {
                    for(int j1 = 0; j1 < arraylist3.size(); j1++)
                    {
                        String s5 = (String)arraylist3.get(j1);
                        Class class3 = (Class)Property.value(s5, "airClass", null);
                        if(class3 != null && !airNames.contains(s5))
                        {
                            airNames.add(s5);
                            cAircraft.add(I18N.plane(s5));
                        }
                    }

                }
                if(airNames.size() == 0)
                {
                    World.cur().setUserCovers();
                    Main.stateStack().pop();
                    fuzeMap.clear();
                    new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, i18n("brief.BornPlace"), i18n("brief.NoPlanesLeft"), 3, 0.0F);
                    return;
                }
                List list1 = Regiment.getAll();
                TreeMap treemap1 = new TreeMap();
                int l1 = list1.size();
                for(int k2 = 0; k2 < l1; k2++)
                {
                    Regiment regiment2 = (Regiment)list1.get(k2);
                    String s12 = regiment2.name();
                    if(regHash.containsKey(s12))
                        continue;
                    regHash.put(s12, regiment2);
                    ArrayList arraylist7 = (ArrayList)regList.get(regiment2.branch());
                    if(arraylist7 == null)
                    {
                        String s15 = null;
                        try
                        {
                            s15 = resCountry.getString(regiment2.branch());
                        }
                        catch(Exception exception5)
                        {
                            continue;
                        }
                        arraylist7 = new ArrayList();
                        regList.put(regiment2.branch(), arraylist7);
                        treemap1.put(s15, regiment2.branch());
                    }
                    arraylist7.add(regiment2);
                }

                try
                {
                    String s10 = Main.cur().netFileServerReg.primaryPath();
                    File file = new File(HomePath.toFileSystemName(s10, 0));
                    File afile[] = file.listFiles();
                    if(afile != null)
                    {
                        for(int l3 = 0; l3 < afile.length; l3++)
                        {
                            File file1 = afile[l3];
                            if(!file1.isFile())
                                continue;
                            String s16 = file1.getName();
                            if(regHash.containsKey(s16))
                                continue;
                            String s17 = s16.toLowerCase();
                            if(s17.endsWith(".bmp") || s17.endsWith(".tga") || s17.length() > 123)
                                continue;
                            int k4 = BmpUtils.squareSizeBMP8Pal(s10 + "/" + s17 + ".bmp");
                            if(k4 != 64 && k4 != 128)
                            {
                                System.out.println("File " + s10 + "/" + s17 + ".bmp NOT loaded");
                                continue;
                            }
                            try
                            {
                                UserRegiment userregiment = new UserRegiment(s16);
                                regHash.put(s16, userregiment);
                                ArrayList arraylist9 = (ArrayList)regList.get(userregiment.branch);
                                if(arraylist9 == null)
                                {
                                    String s18 = null;
                                    try
                                    {
                                        s18 = resCountry.getString(userregiment.branch);
                                    }
                                    catch(Exception exception7)
                                    {
                                        continue;
                                    }
                                    arraylist9 = new ArrayList();
                                    regList.put(userregiment.branch, arraylist9);
                                    treemap1.put(s18, userregiment.branch);
                                }
                                arraylist9.add(userregiment);
                            }
                            catch(Exception exception6)
                            {
                                System.out.println(exception6.getMessage());
                                System.out.println("Regiment " + s16 + " NOT loaded");
                            }
                        }

                    }
                }
                catch(Exception exception2)
                {
                    System.out.println(exception2.getMessage());
                    exception2.printStackTrace();
                }
                for(Iterator iterator1 = treemap1.keySet().iterator(); iterator1.hasNext();)
                {
                    String s13 = (String)iterator1.next();
                    if(arraylist2 == null || arraylist2.size() == 0)
                    {
                        countryLst.add(treemap1.get(s13));
                        cCountry.add(s13);
                    } else
                    if(arraylist2.contains(s13))
                    {
                        countryLst.add(treemap1.get(s13));
                        cCountry.add(s13);
                    }
                }

                treemap1.clear();
                cCountry.setSelected(0, true, false);
                fillRegiments();
                wNumber.setValue("" + usercfg.netTacticalNumber, false);
                cSquadron.setSelected(usercfg.netSquadron, true, false);
                if(usercfg.netRegiment != null)
                {
                    Object obj1 = regHash.get(usercfg.netRegiment);
                    if(obj1 != null)
                    {
                        String s14 = null;
                        if(obj1 instanceof Regiment)
                            s14 = ((Regiment)obj1).branch();
                        else
                            s14 = ((UserRegiment)obj1).branch;
                        int i4;
                        for(i4 = 0; i4 < countryLst.size(); i4++)
                            if(s14.equals(countryLst.get(i4)))
                                break;

                        if(i4 < countryLst.size())
                        {
                            cCountry.setSelected(i4, true, false);
                            fillRegiments();
                            ArrayList arraylist8 = (ArrayList)regList.get(countryLst.get(i4));
                            if(arraylist8 != null)
                            {
                                for(int j4 = 0; j4 < arraylist8.size(); j4++)
                                {
                                    if(!obj1.equals(arraylist8.get(j4)))
                                        continue;
                                    cRegiment.setSelected(j4, true, false);
                                    break;
                                }

                            }
                        }
                    }
                }
                cAircraft.setSelected(-1, false, false);
                try
                {
                    for(int k3 = 0; k3 < airNames.size(); k3++)
                    {
                        if(!usercfg.netAirName.equals(airNames.get(k3)))
                            continue;
                        cAircraft.setSelected(k3, true, false);
                        break;
                    }

                }
                catch(Exception exception3) { }
                if(cAircraft.getSelected() < 0)
                {
                    cAircraft.setSelected(0, true, false);
                    usercfg.netAirName = (String)airNames.get(0);
                }
                if(usercfg.netRegiment == null && cRegiment.size() > 0)
                {
                    cRegiment.setSelected(-1, false, false);
                    cRegiment.setSelected(0, true, true);
                }
                break;

            case 4: // '\004'
                if(quikPlayer && quikCurPlane == quikPlayerPosition)
                {
                    wMashineGun.showWindow();
                    wCannon.showWindow();
                    wRocket.showWindow();
                    wRocketDelay.showWindow();
                    setFuzeControlsVisibility();
                } else
                {
                    wMashineGun.hideWindow();
                    wCannon.hideWindow();
                    wRocket.hideWindow();
                    wRocketDelay.hideWindow();
                    wBombDelay.hideWindow();
                    cFuzeDelay.hideWindow();
                    cFuzeType.hideWindow();
                }
                cPlane.showWindow();
                wNumber.hideWindow();
                cSquadron.hideWindow();
                sNumberOn.showWindow();
                if(!quikPlayer)
                    quikCurPlane = 0;
                sNumberOn.setChecked(quikNumberOn[quikCurPlane], false);
                if(quikFuel <= 10)
                    cFuel.setSelected(0, true, false);
                else
                if(quikFuel <= 20)
                    cFuel.setSelected(1, true, false);
                else
                if(quikFuel <= 30)
                    cFuel.setSelected(2, true, false);
                else
                if(quikFuel <= 40)
                    cFuel.setSelected(3, true, false);
                else
                if(quikFuel <= 50)
                    cFuel.setSelected(4, true, false);
                else
                if(quikFuel <= 60)
                    cFuel.setSelected(5, true, false);
                else
                if(quikFuel <= 70)
                    cFuel.setSelected(6, true, false);
                else
                if(quikFuel <= 80)
                    cFuel.setSelected(7, true, false);
                else
                if(quikFuel <= 90)
                    cFuel.setSelected(8, true, false);
                else
                    cFuel.setSelected(9, true, false);
                ArrayList arraylist = quikListPlane;
                for(int l = 0; l < arraylist.size(); l++)
                {
                    Class class1 = (Class)arraylist.get(l);
                    String s3 = Property.stringValue(class1, "keyName");
//                    if(!quikPlayer || Property.containsValue(class1, "cockpitClass"))
//                    {
                        airNames.add(s3);
//                        cAircraft.add(I18N.plane(s3));
                        cAircraft.add((String)quikListName.get(l));
//                    }
                }

                List list = Regiment.getAll();
                TreeMap treemap = new TreeMap();
                int i1 = list.size();
                for(int k1 = 0; k1 < i1; k1++)
                {
                    Regiment regiment1 = (Regiment)list.get(k1);
                    if(regiment1.getArmy() != quikArmy)
                        continue;
                    String s7 = regiment1.name();
                    if(regHash.containsKey(s7))
                        continue;
                    regHash.put(s7, regiment1);
                    ArrayList arraylist5 = (ArrayList)regList.get(regiment1.branch());
                    if(arraylist5 == null)
                    {
                        String s11 = null;
                        try
                        {
                            s11 = resCountry.getString(regiment1.branch());
                        }
                        catch(Exception exception4)
                        {
                            continue;
                        }
                        arraylist5 = new ArrayList();
                        regList.put(regiment1.branch(), arraylist5);
                        treemap.put(s11, regiment1.branch());
                    }
                    arraylist5.add(regiment1);
                }

                String s8;
                for(Iterator iterator = treemap.keySet().iterator(); iterator.hasNext(); cCountry.add(s8))
                {
                    s8 = (String)iterator.next();
                    countryLst.add(treemap.get(s8));
                }

                treemap.clear();
                cCountry.setSelected(0, true, false);
                fillRegiments();
                if(quikRegiment != null)
                {
                    Object obj = regHash.get(quikRegiment);
                    if(obj != null)
                    {
                        String s9 = ((Regiment)obj).branch();
                        int i3;
                        for(i3 = 0; i3 < countryLst.size(); i3++)
                            if(s9.equals(countryLst.get(i3)))
                                break;

                        if(i3 < countryLst.size())
                        {
                            cCountry.setSelected(i3, true, false);
                            fillRegiments();
                            ArrayList arraylist6 = (ArrayList)regList.get(countryLst.get(i3));
                            if(arraylist6 != null)
                            {
                                for(int j3 = 0; j3 < arraylist6.size(); j3++)
                                {
                                    if(!obj.equals(arraylist6.get(j3)))
                                        continue;
                                    cRegiment.setSelected(j3, true, false);
                                    break;
                                }

                            }
                        }
                    }
                }
                cAircraft.setSelected(-1, false, false);
                try
                {
                    for(int i2 = 0; i2 < airNames.size(); i2++)
                    {
                        if(!quikPlane.equals(airNames.get(i2)))
                            continue;
                        cAircraft.setSelected(i2, true, false);
                        break;
                    }

                }
                catch(Exception exception1) { }
                if(cAircraft.getSelected() < 0)
                {
                    cAircraft.setSelected(0, true, false);
                    quikPlane = (String)airNames.get(0);
                }
                if(quikRegiment == null && cRegiment.size() > 0)
                {
                    cRegiment.setSelected(-1, false, false);
                    cRegiment.setSelected(0, true, true);
                }
                cPlane.clear(false);
                for(int j2 = 0; j2 < quikPlanes; j2++)
                    cPlane.add("" + (j2 + 1));

                cPlane.setSelected(quikCurPlane, true, false);
                break;
            }
            if(zutiSelectedBornPlace != null && zutiSelectedBornPlace.zutiEnablePlaneLimits)
                zutiFillWeapons(zutiSelectedBornPlace);
            else
                fillWeapons();
            selectWeapon();
            fillSkins();
            selectSkin();
            fillPilots();
            selectPilot();
            fillNoseart();
            selectNoseart();
            setMesh();
            prepareMesh();
            prepareWeapons();
            prepareSkin();
            preparePilot();
            prepareNoseart();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            World.cur().setUserCovers();
            Main.stateStack().pop();
            fuzeMap.clear();
            return;
        }
        dialogClient.setPosSize();
        client.activateWindow();
    }

    public void _leave()
    {
        client.hideWindow();
    }

    private void fillRegiments()
    {
        if(stateId != 2 && stateId != 4)
            return;
        cRegiment.clear();
        int i = cCountry.getSelected();
        if(i < 0)
            return;
        String s = (String)countryLst.get(i);
        ArrayList arraylist = (ArrayList)regList.get(s);
        if(arraylist.size() > 0)
        {
            for(int j = 0; j < arraylist.size(); j++)
            {
                Object obj = arraylist.get(j);
                if(obj instanceof Regiment)
                    cRegiment.add(((Regiment)obj).shortInfo());
                else
                    cRegiment.add(((UserRegiment)obj).shortInfo);
            }

            cRegiment.setSelected(0, true, false);
        }
    }

    private void fillWeapons()
    {
        cWeapon.clear();
        weaponNames.clear();
        int i = cAircraft.getSelected();
        if(i < 0)
            return;
        Class class1 = (Class)Property.value(airNames.get(i), "airClass", null);
        String as[] = Aircraft.getWeaponsRegistered(class1);
        if(as != null && as.length > 0)
        {
            String s = (String)airNames.get(i);
            for(int j = 0; j < as.length; j++)
            {
                String s1 = as[j];
                if(!Aircraft.isWeaponDateOk(class1, s1))
                    continue;
                if(!bEnableWeaponChange)
                {
                    String s2 = Main.cur().currentMissionFile.get(planeName, "weapons", (String)null);
                    if(!s1.equals(s2))
                        continue;
                }
                weaponNames.add(s1);
                cWeapon.add(I18N.weapons(s, s1));
            }

            if(weaponNames.size() == 0)
            {
                weaponNames.add(as[0]);
                cWeapon.add(I18N.weapons(s, as[0]));
            }
            cWeapon.setSelected(0, true, false);
        }
    }

    private void selectWeapon()
    {
        if(bEnableWeaponChange)
        {
            UserCfg usercfg = World.cur().userCfg;
            String s = null;
            if(isNet())
                s = usercfg.getWeapon(airName());
            else
            if(stateId == 4)
                s = quikWeapon;
            else
                s = Main.cur().currentMissionFile.get(planeName, "weapons", (String)null);
            cWeapon.setSelected(-1, false, false);
            for(int i = 0; i < weaponNames.size(); i++)
            {
                String s1 = (String)weaponNames.get(i);
                if(!s1.equals(s))
                    continue;
                    cWeapon.setSelected(i, true, false);
                    break;
                }

            if(cWeapon.getSelected() < 0)
            {
                cWeapon.setSelected(0, true, false);
                if(isNet())
                    usercfg.setWeapon(airName(), (String)weaponNames.get(0));
                else
                if(stateId == 4)
                    quikWeapon = (String)weaponNames.get(0);
                else
                    Main.cur().currentMissionFile.set(planeName, "weapons", (String)weaponNames.get(0));
            }
        } else
        {
            cWeapon.setSelected(0, true, false);
        }
    }

    public static String validateFileName(String s)
    {
        if(s.indexOf('\\') >= 0)
            s = s.replace('\\', '_');
        if(s.indexOf('/') >= 0)
            s = s.replace('/', '_');
        if(s.indexOf('?') >= 0)
            s = s.replace('?', '_');
        return s;
    }

    private void fillSkins()
    {
        cSkin.clear();
        cSkin.add(i18n("neta.Default"));
        try
        {
            int i = cAircraft.getSelected();
            String s = Main.cur().netFileServerSkin.primaryPath();
            String s1 = validateFileName((String)airNames.get(i));
            File file = new File(HomePath.toFileSystemName(s + "/" + s1, 0));
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int j = 0; j < afile.length; j++)
                {
                    File file1 = afile[j];
                    if(file1.isFile())
                    {
                        String s2 = file1.getName();
                        String s3 = s2.toLowerCase();
                        if(s3.endsWith(".bmp") && s3.length() + s1.length() <= 122)
                        {
                            int k = BmpUtils.squareSizeBMP8Pal(s + "/" + s1 + "/" + s2);
                            if(k == 512 || k == 1024)
                                cSkin.add(s2);
                            else
                                System.out.println("Skin " + s + "/" + s1 + "/" + s2 + " NOT loaded");
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
        cSkin.setSelected(0, true, false);
    }

    private void selectSkin()
    {
        UserCfg usercfg = World.cur().userCfg;
        cSkin.setSelected(-1, false, false);
        String s = usercfg.getSkin(airName());
        if(stateId == 4)
            s = quikSkin[quikCurPlane];
        for(int i = 1; i < cSkin.size(); i++)
        {
            String s1 = cSkin.get(i);
            if(!s1.equals(s))
                continue;
                cSkin.setSelected(i, true, false);
                break;
            }

        if(cSkin.getSelected() < 0)
        {
            cSkin.setSelected(0, true, false);
            if(stateId == 4)
                quikSkin[quikCurPlane] = null;
            else
                usercfg.setSkin(airName(), null);
        }
    }

    private void fillPilots()
    {
        cPilot.clear();
        cPilot.add(i18n("neta.Default"));
        try
        {
            String s = Main.cur().netFileServerPilot.primaryPath();
            File file = new File(HomePath.toFileSystemName(s, 0));
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int i = 0; i < afile.length; i++)
                {
                    File file1 = afile[i];
                    if(file1.isFile())
                    {
                        String s1 = file1.getName();
                        String s2 = s1.toLowerCase();
                        if(s2.endsWith(".bmp") && s2.length() <= 122)
                            if(BmpUtils.checkBMP8Pal(s + "/" + s1, 256, 256))
                                cPilot.add(s1);
                            else
                                System.out.println("Pilot " + s + "/" + s1 + " NOT loaded");
                    }
                }

            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        cPilot.setSelected(0, true, false);
    }

    private void selectPilot()
    {
        UserCfg usercfg = World.cur().userCfg;
        cPilot.setSelected(-1, false, false);
        String s = usercfg.netPilot;
        if(stateId == 4)
            s = quikPilot[quikCurPlane];
        for(int i = 1; i < cPilot.size(); i++)
        {
            String s1 = cPilot.get(i);
            if(!s1.equals(s))
                continue;
                cPilot.setSelected(i, true, false);
                break;
            }

        if(cPilot.getSelected() < 0)
        {
            cPilot.setSelected(0, true, false);
            if(stateId == 4)
                quikPilot[quikCurPlane] = null;
            else
                usercfg.netPilot = null;
        }
    }

    private void fillNoseart()
    {
        cNoseart.clear();
        cNoseart.add(i18n("neta.None"));
        try
        {
            String s = Main.cur().netFileServerNoseart.primaryPath();
            File file = new File(HomePath.toFileSystemName(s, 0));
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int i = 0; i < afile.length; i++)
                {
                    File file1 = afile[i];
                    if(file1.isFile())
                    {
                        String s1 = file1.getName();
                        String s2 = s1.toLowerCase();
                        if(s2.endsWith(".bmp") && s2.length() <= 122)
                            if(BmpUtils.checkBMP8Pal(s + "/" + s1, 256, 512))
                                cNoseart.add(s1);
                            else
                                System.out.println("Noseart " + s + "/" + s1 + " NOT loaded");
                    }
                }

            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        cNoseart.setSelected(0, true, false);
    }

    private void selectNoseart()
    {
        UserCfg usercfg = World.cur().userCfg;
        cNoseart.setSelected(-1, false, false);
        boolean flag = true;
        int i = cAircraft.getSelected();
        if(i < 0)
        {
            flag = false;
        } else
        {
            Class class1 = (Class)Property.value(airNames.get(i), "airClass", null);
            flag = Property.intValue(class1, "noseart", 0) == 1;
            if(flag)
            {
                int j = cCountry.getSelected();
                if(j < 0)
                {
                    flag = false;
                } else
                {
                    String s1 = (String)countryLst.get(j);
                    String s3 = Regiment.getCountryFromBranch(s1);
                    flag = "us".equals(s3);
                }
            }
        }
        if(flag)
        {
            String s = usercfg.getNoseart(airName());
            if(stateId == 4)
                s = quikNoseart[quikCurPlane];
            for(int k = 1; k < cNoseart.size(); k++)
            {
                String s2 = cNoseart.get(k);
                if(!s2.equals(s))
                    continue;
                    cNoseart.setSelected(k, true, false);
                    break;
                }

            cNoseart.showWindow();
        } else
        {
            cNoseart.hideWindow();
        }
        if(cNoseart.getSelected() < 0)
        {
            cNoseart.setSelected(0, true, false);
            if(stateId == 4)
                quikNoseart[quikCurPlane] = null;
            else
                usercfg.setNoseart(airName(), null);
        }
    }

    private void randomAircraft()
    {
        int rand = TrueRandom.nextInt(0, cAircraft.size());
        String name = (String)quikListName.get(rand);
        if(name.trim().startsWith("*"))
        {
            randomAircraft();
            return;
        }
        cAircraft.setSelected(rand, true, true);
//        cSkin.setSelected(TrueRandom.nextInt(0, cSkin.size()), true, true);
        cWeapon.setSelected(cWeapon.size() > 2 ? TrueRandom.nextInt(0, cWeapon.size() - 1) : 0, true, true);
    }

    public void toAirViewer()
    {
        GUIAirViewer guiairviewer = (GUIAirViewer)GameState.get(73);
        try
        {
            guiairviewer.airNames = airNames;
//            guiairviewer.quikListPlane = quikListPlane;
            guiairviewer.quikListName = quikListName;
//            guiairviewer.quikArmy = quikArmy;
//            guiairviewer.quikPlane = (String)airNames.get(cAircraft.getSelected());
            guiairviewer.quikCurPlane = cPlane.getSelected();
            guiairviewer.quikCountry = Regiment.getCountryFromBranch((String)countryLst.get(cCountry.getSelected()));
            guiairviewer.quikRegiment = getSelectedRegiment();
            guiairviewer.quikWing = quikWing;
            Main.stateStack().push(73);
            guiairviewer.cAircraft.setSelected(cAircraft.getSelected(), true, true);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void enterPop(GameState gamestate)
    {
        if(!client.isActivated())
            client.activateWindow();
    }

    private void createRender()
    {
        renders = new GUIRenders(dialogClient) {

            public void mouseButton(int i, boolean flag, float f, float f1)
            {
                super.mouseButton(i, flag, f, f1);
                if(!flag)
                    return;
                if(i == 1)
                {
                    if(animateMeshA == 0.0F && animateMeshT == 0.0F)
                    {
                        if(Actor.isValid(actorMesh))
                        {
//                            f -= win.dx / 2.0F;
//                            if(Math.abs(f) < win.dx / 16F)
                            if(f < win.dx / 2.0F)
                                actorAzimut += 45F;
                            else
                                actorAzimut -= 45F;

//                            actorMesh.pos.setAbs(new Orient(90F, 0.0F, 0.0F));
//                            actorAzimut -= 45F;
                            actorMesh.pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
                        }
                    } else
                    {
                        animateMeshA = animateMeshT = 0.0F;
                        if(Actor.isValid(actorMesh))
                        {
                            actorAzimut = 90F;
                            actorMesh.pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
                        }

                    }
                } else
                if(i == 0)
                {
                    actorAzimut = 135F;
                    f -= win.dx / 2.0F;
                    if(Math.abs(f) < win.dx / 16F)
                        animateMeshA = 0.0F;
                    else
                        animateMeshA = (-128F * f) / win.dx;
                    f1 -= win.dy / 2.0F;
                    if(Math.abs(f1) < win.dy / 16F)
                        animateMeshT = 0.0F;
                    else
                        animateMeshT = (-128F * f1) / win.dy;
                }
            }
        }
;
        camera3D = new Camera3D();
        camera3D.set(50F, 1.0F, 800F);
        render3D = new _Render3D(renders.renders, 1.0F);
        render3D.setCamera(camera3D);
        LightEnvXY lightenvxy = new LightEnvXY();
        render3D.setLightEnv(lightenvxy);
        lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
        Vector3f vector3f = new Vector3f(-2F, 1.0F, -1F);
        vector3f.normalize();
        lightenvxy.sun().set(vector3f);
    }

    private void setMesh()
    {
        destroyMesh();
        int i = cAircraft.getSelected();
        if(i < 0)
            return;
        try
        {
            Class class1 = (Class)Property.value(airNames.get(i), "airClass", null);
            String s = (String)countryLst.get(cCountry.getSelected());
            String s1 = Regiment.getCountryFromBranch(s);
            String s2 = Aircraft.getPropertyMesh(class1, s1);
            actorMesh = new ActorSimpleHMesh(s2);
            double d = actorMesh.hierMesh().visibilityR();
            Aircraft.prepareMeshCamouflage(s2, actorMesh.hierMesh(), class1, getSelectedRegiment());
//            actorMesh.pos.setAbs(new Orient(actorAzimut, 0.0F, 0.0F));
            actorMesh.pos.setAbs(actorOrient);
            actorMesh.pos.reset();
            d *= Math.cos(0.26179938779914941D) / Math.sin(((double)camera3D.FOV() * 3.1415926535897931D) / 180D / 2D);
            camera3D.pos.setAbs(new Point3d(d, 0.0D, 0.0D), new Orient(180F, 0.0F, 0.0F));
            camera3D.pos.reset();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void destroyMesh()
    {
        if(Actor.isValid(actorMesh))
        {
            actorOrient = actorMesh.pos.getAbsOrient();
            actorMesh.destroy();
        }
        actorMesh = null;
        destroyWeaponMeshs();
    }

    private void destroyWeaponMeshs()
    {
        for(int i = 0; i < weaponMeshs.size(); i++)
        {
            ActorMesh actormesh = (ActorMesh)weaponMeshs.get(i);
            if(Actor.isValid(actormesh))
                actormesh.destroy();
        }

        weaponMeshs.clear();
    }

    private void prepareMesh()
    {
        if(!Actor.isValid(actorMesh))
            return;
        int i = cAircraft.getSelected();
        if(i < 0)
            return;
        switch(stateId)
        {
        default:
            break;

        case 3: // '\003'
            Class class1 = GUINetAircraft.selectedAircraftClass();
            Regiment regiment = GUINetAircraft.selectedRegiment();
            String s = regiment.country();
            String s2 = GUINetAircraft.selectedWingName();
            PaintScheme paintscheme1 = Aircraft.getPropertyPaintScheme(class1, s);
            if(paintscheme1 == null)
                return;
            int l = s2.charAt(s2.length() - 2) - 48;
            int i1 = s2.charAt(s2.length() - 1) - 48;
            int j1 = GUINetAircraft.selectedAircraftNumInWing();
            UserCfg usercfg1 = World.cur().userCfg;
            paintscheme1.prepare(class1, actorMesh.hierMesh(), regiment, l, i1, j1, usercfg1.netNumberOn);
            break;

        case 0: // '\0'
        case 1: // '\001'
        case 2: // '\002'
            Class class2 = (Class)Property.value(airNames.get(i), "airClass", null);
            PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(class2, (String)countryLst.get(cCountry.getSelected()));
            if(paintscheme == null)
                return;
            int k = cCountry.getSelected();
            if(k < 0)
                return;
            String s3 = (String)countryLst.get(k);
            ArrayList arraylist1 = (ArrayList)regList.get(s3);
            if(arraylist1 == null)
                return;
            Object obj = arraylist1.get(cRegiment.getSelected());
            Object obj1 = null;
            if(obj instanceof Regiment)
            {
                obj1 = (Regiment)obj;
                NetUserRegiment netuserregiment = ((NetUser)NetEnv.host()).netUserRegiment;
                ((NetUser)NetEnv.host()).setUserRegiment(netuserregiment.branch(), "", netuserregiment.aid(), netuserregiment.gruppeNumber());
            } else
            {
                UserRegiment userregiment = (UserRegiment)obj;
                ((NetUser)NetEnv.host()).setUserRegiment(userregiment.country, userregiment.fileName + ".bmp", userregiment.id, userregiment.gruppeNumber);
                obj1 = ((NetUser)NetEnv.host()).netUserRegiment;
            }
            if(obj1 == null)
                return;
            if(isNet())
            {
                if(cSquadron.getSelected() < 0)
                    return;
                UserCfg usercfg = World.cur().userCfg;
                boolean flag = usercfg.netNumberOn;
                if(stateId == 2)
                    flag = true;
                paintscheme.prepareNum(class2, actorMesh.hierMesh(), ((Regiment) (obj1)), cSquadron.getSelected(), 0, usercfg.netTacticalNumber, flag);
            } else
            {
                int k1 = planeName.charAt(planeName.length() - 2) - 48;
                int l1 = planeName.charAt(planeName.length() - 1) - 48;
                int i2 = Main.cur().currentMissionFile.get("Main", "playerNum", 0);
                UserCfg usercfg2 = World.cur().userCfg;
                paintscheme.prepare(class2, actorMesh.hierMesh(), ((Regiment) (obj1)), k1, l1, i2, usercfg2.netNumberOn);
            }
            break;

        case 4: // '\004'
            Class class3 = (Class)Property.value(airNames.get(i), "airClass", null);
            int j = cCountry.getSelected();
            if(j < 0)
                return;
            String s1 = (String)countryLst.get(j);
            ArrayList arraylist = (ArrayList)regList.get(s1);
            if(arraylist == null)
                return;
            Regiment regiment1 = (Regiment)arraylist.get(cRegiment.getSelected());
            if(regiment1 == null)
                return;
            PaintScheme paintscheme2 = Aircraft.getPropertyPaintScheme(class3, regiment1.country());
            if(paintscheme2 == null)
                return;
            paintscheme2.prepare(class3, actorMesh.hierMesh(), regiment1, quikSquadron, quikWing, quikCurPlane, quikNumberOn[quikCurPlane]);
            break;
        }
    }

    private void prepareWeapons()
    {
        destroyWeaponMeshs();
        if(!Actor.isValid(actorMesh))
            return;
        int i = cAircraft.getSelected();
        if(i < 0)
            return;
        Class class1 = (Class)Property.value(airNames.get(i), "airClass", null);
        String as[] = zutiSyncWeaponsLists(Aircraft.getWeaponsRegistered(class1));
        ArrayList arraylist = new ArrayList();
        if(as == null || as.length == 0)
            return;
        i = cWeapon.getSelected();
        if(i < 0 || i >= as.length)
            return;
        String as1[] = Aircraft.getWeaponHooksRegistered(class1);
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = Aircraft.getWeaponSlotsRegistered(class1, as[i]);
        if(as1 == null || a_lweaponslot == null)
            return;
        for(int j = 0; j < as1.length; j++)
        {
            if(as1[j] == null || a_lweaponslot[j] == null)
                continue;
            Class class2 = a_lweaponslot[j].clazz;
            if((com.maddox.il2.objects.weapons.BombGun.class).isAssignableFrom(class2))
            {
                arraylist.add(class2);
                if(!Property.containsValue(class2, "external"))
                    continue;
            }
            String s = Property.stringValue(class2, "mesh", null);
            if(s == null)
            {
                Class class3 = (Class)Property.value(class2, "bulletClass", null);
                if(class3 != null)
                    s = Property.stringValue(class3, "mesh", null);
            }
            if(s != null)
            try
            {
                ActorSimpleMesh actorsimplemesh = new ActorSimpleMesh(s);
                actorsimplemesh.pos.setBase(actorMesh, new HookNamed(actorMesh, as1[j]), false);
                actorsimplemesh.pos.changeHookToRel();
                actorsimplemesh.pos.resetAsBase();
                weaponMeshs.add(actorsimplemesh);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

//        if(stateId != 4 || quikPlayer && quikCurPlane == quikPlayerPosition)
        if(isNet() || (quikPlayer && quikCurPlane == quikPlayerPosition))
            populateFuzes(arraylist);
    }

    private void populateFuzes(List list)
    {
        if(!World.cur().diffCur.BombFuzes)
            return;
        fuzeMap.clear();
        cFuzeDelay.clear(false);
        for(int i = 0; i < list.size(); i++)
        {
            Class class1 = (Class)list.get(i);
            Class class2 = (Class)Property.value(class1, "bulletClass", null);
            Object aobj1[] = (Object[])Property.value(class2, "fuze", null);
            if(aobj1 != null)
            {
                for(int k = 0; k < aobj1.length; k++)
                {
                    Class class3 = (Class)aobj1[k];
                    int i1 = Property.intValue(class3, "type", 0);
                    String s1 = "arming.FuzeType_" + i1;
                    int k1 = Mission.getMissionDate(false);
                    int l1 = Property.intValue(class3, "dateStart", 0);
                    int i2 = Property.intValue(class3, "dateEnd", 0x1312cff);
                    if(k1 >= l1 && k1 <= i2)
                    {
                        Object obj;
                        if(fuzeMap.containsKey(s1))
                            obj = (List)fuzeMap.get(s1);
                        else
                            obj = new ArrayList();
                        addDelayValues(class3, ((List) (obj)));
                        if(!fuzeMap.containsKey(s1))
                            fuzeMap.put(s1, obj);
                    }
                }

                if(fuzeMap.size() == 0 && aobj1.length > 0)
                {
                    Class class4 = (Class)aobj1[0];
                    ArrayList arraylist = new ArrayList();
                    addDelayValues(class4, arraylist);
                    int j1 = Property.intValue(class4, "type", 0);
                    String s2 = "arming.FuzeType_" + j1;
                    fuzeMap.put(s2, arraylist);
                }
            }
        }

        Object aobj[] = sortStringArray(fuzeMap.keySet().toArray());
        int j = 0;
        UserCfg usercfg = World.cur().userCfg;
        cFuzeType.clear(false);
        for(int l = 0; l < aobj.length; l++)
        {
            String s = (String)aobj[l];

            cFuzeType.add(resourceBundle.getString(s));
            if(s.endsWith(usercfg.fuzeType + ""))
                j = l;
        }

        if(cFuzeType.size() > 0)
            cFuzeType.setSelected(j, true, true);
        setFuzeType();
    }

    private void addDelayValues(Class class1, List list)
    {
        float f = Property.floatValue(class1, "minDelay", -1F);
        float f1 = Property.floatValue(class1, "maxDelay", -1F);
        if(f == -1F && f1 == -1F)
        {
            float af[] = (float[])Property.value(class1, "fixedDelay", null);
            if(af != null)
            {
                for(int i = 0; i < af.length; i++)
                {
                    Float float1 = new Float(af[i]);
                    if(!list.contains(float1))
                        list.add(float1);
                }

            }
        } else
        {
            float f2 = 0.5F;
            if(f1 - f < 0.5F)
                f2 = 0.1F;
            if(f1 - f > 30F)
                f2 = 5F;
            for(float f3 = f; f3 < f1; f3 += f2)
            {
                Float float2 = new Float(f3);
                if(!list.contains(float2))
                    list.add(float2);
            }

            Float float3 = new Float(f1);
            if(!list.contains(float3))
                list.add(float3);
        }
    }

    private int getSelectedFuzeType()
    {
        int i = cFuzeType.getSelected();
        Object aobj[] = sortStringArray(fuzeMap.keySet().toArray());
        if(i < 0 || i > aobj.length - 1)
        {
            return -1;
        } else
        {
            String s = (String)aobj[i];
            String s1 = s.substring(16);
            return (new Integer(s1)).intValue();
        }
    }

    private void setFuzeType()
    {
        Object aobj[] = sortStringArray(fuzeMap.keySet().toArray());
        int i = cFuzeType.getSelected();
        if(i < 0 || i > aobj.length - 1)
            return;
        String s = (String)aobj[cFuzeType.getSelected()];
        int j = getSelectedFuzeType();
        if(j == -1)
            return;
        UserCfg usercfg = World.cur().userCfg;
        usercfg.fuzeType = j;
        List list = (List)fuzeMap.get(s);
        Object aobj1[] = sortFloatArray(list.toArray());
        cFuzeDelay.clear(false);
        int k = 0;
        for(int l = 0; l < aobj1.length; l++)
        {
            Float float1 = (Float)aobj1[l];
            if(usercfg.bombDelay == float1.floatValue())
                k = l;
            cFuzeDelay.add("" + float1.floatValue());
        }

        if(cFuzeDelay.size() > 0)
            cFuzeDelay.setSelected(k, true, true);
        setFuzeDelay();
    }

    private void setFuzeDelay()
    {
        if(!World.cur().diffCur.BombFuzes)
            return;
        String s = cFuzeDelay.getValue();
        if(s == null || s.equals(""))
        {
            return;
        } else
        {
            Float float1 = new Float(s);
            UserCfg usercfg = World.cur().userCfg;
            usercfg.bombDelay = float1.floatValue();
            return;
        }
    }

    private Object[] sortStringArray(Object aobj[])
    {
        boolean flag = true;
        do
        {
            flag = true;
            for(int i = 0; i < aobj.length - 1; i++)
            {
                String s = (String)aobj[i];
                String s1 = (String)aobj[i + 1];
                if(s.compareTo(s1) > 0)
                {
                    aobj[i] = s1;
                    aobj[i + 1] = s;
                    flag = false;
                }
            }

        } while(!flag);
        return aobj;
    }

    private Object[] sortFloatArray(Object aobj[])
    {
        boolean flag = true;
        do
        {
            flag = true;
            for(int i = 0; i < aobj.length - 1; i++)
            {
                Float float1 = (Float)aobj[i];
                Float float2 = (Float)aobj[i + 1];
                if(float1.floatValue() > float2.floatValue())
                {
                    aobj[i] = float2;
                    aobj[i + 1] = float1;
                    flag = false;
                }
            }

        } while(!flag);
        return aobj;
    }

    private Regiment getSelectedRegiment()
    {
        int i = cCountry.getSelected();
        if(i < 0)
            return null;
        String s = (String)countryLst.get(i);
        ArrayList arraylist = (ArrayList)regList.get(s);
        if(arraylist == null)
            return null;
        Object obj = arraylist.get(cRegiment.getSelected());
        Object obj1 = null;
        if(obj instanceof Regiment)
        {
            obj1 = (Regiment)obj;
        } else
        {
            UserRegiment userregiment = (UserRegiment)obj;
            obj1 = ((NetUser)NetEnv.host()).netUserRegiment;
        }
        return ((Regiment) (obj1));
    }

    private void prepareSkin()
    {
        int i = cSkin.getSelected();
        if(i < 0)
            return;
        Class class1 = (Class)Property.value(airNames.get(cAircraft.getSelected()), "airClass", null);
        String s = (String)countryLst.get(cCountry.getSelected());
        String s1 = Regiment.getCountryFromBranch(s);
        String s2 = Aircraft.getPropertyMesh(class1, s1);
        if(i == 0)
        {
            ((NetUser)NetEnv.host()).setSkin(null);
            Aircraft.prepareMeshCamouflage(s2, actorMesh.hierMesh(), class1, getSelectedRegiment());
        } else
        {
            String s3 = validateFileName(airName());
            String s4 = s3 + "/" + cSkin.get(i);
            String s5 = s2;
            int j = s5.lastIndexOf('/');
            if(j >= 0)
                s5 = s5.substring(0, j + 1) + "summer";
            else
                s5 = s5 + "summer";
            String s6 = "PaintSchemes/Cache/" + s3;
            try
            {
                File file = new File(HomePath.toFileSystemName(s6, 0));
                if(!file.isDirectory())
                {
                    file.mkdir();
                } else
                {
                    File afile[] = file.listFiles();
                    if(afile != null)
                    {
                        for(int k = 0; k < afile.length; k++)
                            if(afile[k] != null)
                            {
                                String s8 = afile[k].getName();
                                if(s8.regionMatches(true, s8.length() - 4, ".tg", 0, 3))
                                    afile[k].delete();
                            }

                    }
                }
            }
            catch(Exception exception)
            {
                return;
            }
            String s7 = Main.cur().netFileServerSkin.primaryPath();
            if(!BmpUtils.bmp8PalTo4TGA4(s7 + "/" + s4, s5, s6))
                return;
            Aircraft.prepareMeshCamouflage(s2, actorMesh.hierMesh(), s6, class1, getSelectedRegiment());
            ((NetUser)NetEnv.host()).setSkin(s4);
        }
    }

    private void preparePilot()
    {
        int i = cPilot.getSelected();
        if(i < 0)
            return;
        if(i == 0)
        {
            Class class1 = (Class)Property.value(airNames.get(cAircraft.getSelected()), "airClass", null);
            String s1 = (String)countryLst.get(cCountry.getSelected());
            String s3 = Regiment.getCountryFromBranch(s1);
            String s5 = Aircraft.getPropertyMesh(class1, s3);
            String s7 = HomePath.concatNames(s5, "pilot1.mat");
            Aircraft.prepareMeshPilot(actorMesh.hierMesh(), 0, s7, "3do/plane/textures/pilot1.tga");
            ((NetUser)NetEnv.host()).setPilot(null);
        } else
        {
            String s = Main.cur().netFileServerPilot.primaryPath();
            String s2 = cPilot.get(i);
            String s4 = s2.substring(0, s2.length() - 4);
            String s6 = "PaintSchemes/Cache/Pilot" + s4 + ".mat";
            String s8 = "PaintSchemes/Cache/Pilot" + s4 + ".tga";
            if(!BmpUtils.bmp8PalToTGA3(s + "/" + s2, s8))
                return;
            Aircraft.prepareMeshPilot(actorMesh.hierMesh(), 0, s6, s8);
            ((NetUser)NetEnv.host()).setPilot(s2);
        }
    }

    private void prepareNoseart()
    {
        int i = cNoseart.getSelected();
        if(i > 0)
        {
            String s = Main.cur().netFileServerNoseart.primaryPath();
            String s1 = cNoseart.get(i);
            String s2 = s1.substring(0, s1.length() - 4);
            String s3 = "PaintSchemes/Cache/Noseart0" + s2 + ".mat";
            String s4 = "PaintSchemes/Cache/Noseart0" + s2 + ".tga";
            String s5 = "PaintSchemes/Cache/Noseart1" + s2 + ".mat";
            String s6 = "PaintSchemes/Cache/Noseart1" + s2 + ".tga";
            if(!BmpUtils.bmp8PalTo2TGA4(s + "/" + s1, s4, s6))
                return;
            Aircraft.prepareMeshNoseart(actorMesh.hierMesh(), s3, s5, s4, s6);
            ((NetUser)NetEnv.host()).setNoseart(s1);
        }
    }

    private float clampValue(GWindowEditControl gwindoweditcontrol, float f, float f1, float f2)
    {
        String s = gwindoweditcontrol.getValue();
        try
        {
            f = Float.parseFloat(s);
        }
        catch(Exception exception) { }
        if(f < f1)
            f = f1;
        if(f > f2)
            f = f2;
        gwindoweditcontrol.setValue("" + f, false);
        return f;
    }

    public GUIAirArming(GWindowRoot gwindowroot)
    {
        super(55);
        fuzeMap = new HashMap();
        zutiSelectedBornPlace = null;
        airNames = new ArrayList();
        weaponNames = new ArrayList();
        regList = new HashMapExt();
        regHash = new HashMapExt();
        countryLst = new ArrayList();
        bEnableWeaponChange = true;
        quikPlayer = false;
        quikArmy = 1;
        quikPlanes = 4;
        quikPlane = "Il-2_M3";
        quikWeapon = "default";
        quikCurPlane = 0;
        quikRegiment = "r01";
        quikSquadron = 0;
        quikWing = 0;
        quikFuel = 100;
        quikListPlane = new ArrayList();
        quikListName = new ArrayList();
        quikPlayerPosition = 0;
        playerNum = -1;
        weaponMeshs = new ArrayList();
        animateMeshA = 0.0F;
        animateMeshT = 0.0F;
        _orient = new Orient();
        actorAzimut = 90F;
        actorOrient = new Orient(90F, 0.0F, 0.0F);
        resourceBundle = ResourceBundle.getBundle("i18n/gui", RTSConf.cur.locale, LDRres.loader());
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("neta.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        wMashineGun = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
        wMashineGun.bNumericOnly = wMashineGun.bNumericFloat = true;
        wMashineGun.bDelayedNotify = true;
        wCannon = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
        wCannon.bNumericOnly = wCannon.bNumericFloat = true;
        wCannon.bDelayedNotify = true;
        wRocket = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
        wRocket.bNumericOnly = wRocket.bNumericFloat = true;
        wRocket.bDelayedNotify = true;
        wRocketDelay = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
        wRocketDelay.bNumericOnly = wRocketDelay.bNumericFloat = true;
        wRocketDelay.bDelayedNotify = true;
        wBombDelay = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
        wBombDelay.bNumericOnly = wBombDelay.bNumericFloat = true;
        wBombDelay.bDelayedNotify = true;
        wNumber = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null) {

            public void keyboardKey(int i, boolean flag)
            {
                super.keyboardKey(i, flag);
                if(i == 10 && flag)
                    notify(2, 0);
            }

        }
);
        wNumber.bNumericOnly = true;
        wNumber.bDelayedNotify = true;
        wNumber.align = 1;
        sNumberOn = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        cFuel = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cFuel.setEditable(false);
        cFuel.add("10");
        cFuel.add("20");
        cFuel.add("30");
        cFuel.add("40");
        cFuel.add("50");
        cFuel.add("60");
        cFuel.add("70");
        cFuel.add("80");
        cFuel.add("90");
        cFuel.add("100");
        cAircraft = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cAircraft.setEditable(false);
        cAircraft.listVisibleLines = 24;
        cWeapon = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cWeapon.setEditable(false);
        cWeapon.listVisibleLines = 18;
        cCountry = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cCountry.setEditable(false);
        cCountry.listVisibleLines = 12;
        cRegiment = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cRegiment.setEditable(false);
        cRegiment.listVisibleLines = 12;
        cSkin = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cSkin.setEditable(false);
        cSkin.listVisibleLines = 18;
        cPilot = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cPilot.setEditable(false);
        cPilot.listVisibleLines = 12;
        cSquadron = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cSquadron.setEditable(false);
        cSquadron.editBox.align = cSquadron.align = 1;
        cSquadron.add("1");
        cSquadron.add("2");
        cSquadron.add("3");
        cSquadron.add("4");
        cPlane = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cPlane.setEditable(false);
        cPlane.editBox.align = cPlane.align = 1;
        cNoseart = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cNoseart.setEditable(false);
        cNoseart.listVisibleLines = 12;
        bBack = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bJoy = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bRand = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bView = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        cFuzeType = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cFuzeType.setEditable(false);
        cFuzeDelay = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
        cFuzeDelay.setEditable(false);
        createRender();
        dialogClient.activateWindow();
        client.hideWindow();
    }

    private void zutiFillWeapons(BornPlace bornplace)
    {
        cWeapon.clear();
        weaponNames.clear();
        int i = cAircraft.getSelected();
        ArrayList arraylist = bornplace.zutiGetAcLoadouts((String)airNames.get(i));
        if(arraylist == null || arraylist.size() < 1)
        {
            fillWeapons();
            return;
        }
        if(i < 0)
            return;
        Class class1 = (Class)Property.value(airNames.get(i), "airClass", null);
        String as[] = Aircraft.getWeaponsRegistered(class1);
        if(as != null && as.length > 0)
        {
            String s = (String)airNames.get(i);
            for(int j = 0; j < as.length; j++)
            {
                String s1 = as[j];
                if(!Aircraft.isWeaponDateOk(class1, s1))
                    continue;
                if(!bEnableWeaponChange)
                {
                    String s2 = Main.cur().currentMissionFile.get(planeName, "weapons", (String)null);
                    if(!s1.equals(s2))
                        continue;
                }
                if(arraylist.contains(s1))
                {
                    weaponNames.add(s1);
                    cWeapon.add(I18N.weapons(s, s1));
                }
            }

            if(weaponNames.size() == 0)
            {
                weaponNames.add(as[0]);
                cWeapon.add(I18N.weapons(s, as[0]));
            }
            cWeapon.setSelected(0, true, false);
        }
    }

    private String[] zutiSyncWeaponsLists(String as[])
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < as.length; i++)
            if(weaponNames.contains(as[i]))
                arraylist.add(as[i]);

        String as1[] = new String[arraylist.size()];
        for(int j = 0; j < arraylist.size(); j++)
            as1[j] = (String)arraylist.get(j);

        return as1;
    }

    private boolean isFuzesEnabled()
    {
        if(stateId == 4 || stateId == 0 || stateId == 1)
            return World.cur().diffUser.BombFuzes;
        else
            return World.cur().diffCur.BombFuzes;
    }

    private void setFuzeControlsVisibility()
    {
        if(isFuzesEnabled())
        {
            wBombDelay.hideWindow();
            cFuzeDelay.showWindow();
            cFuzeType.showWindow();
        } else
        {
            wBombDelay.showWindow();
            cFuzeDelay.hideWindow();
            cFuzeType.hideWindow();
        }
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GWindowEditControl wMashineGun;
    public GWindowEditControl wCannon;
    public GWindowEditControl wRocket;
    public GWindowEditControl wRocketDelay;
    public GWindowEditControl wBombDelay;
    public GWindowComboControl cFuel;
    public GWindowComboControl cAircraft;
    public GWindowComboControl cWeapon;
    public GWindowComboControl cCountry;
    public GWindowComboControl cRegiment;
    public GWindowComboControl cSkin;
    public GWindowComboControl cNoseart;
    public GWindowComboControl cPilot;
    public GWindowEditControl wNumber;
    public GWindowComboControl cSquadron;
    public GUISwitchBox3 sNumberOn;
    public GWindowComboControl cPlane;
    public GWindowComboControl cFuzeType;
    public GWindowComboControl cFuzeDelay;
    private Map fuzeMap;
    private ResourceBundle resourceBundle;
    public GUIButton bJoy;
    private BornPlace zutiSelectedBornPlace;
    public GUIButton bBack;
    public GUIButton bRand;
    public GUIButton bView;
    public static final int SINGLE = 0;
    public static final int CAMPAIGN = 1;
    public static final int DFIGHT = 2;
    public static final int COOP = 3;
    public static final int QUIK = 4;
    public static int stateId = 2;
    public ArrayList airNames;
    public ArrayList weaponNames;
    public HashMapExt regList;
    public HashMapExt regHash;
    public ResourceBundle resCountry;
    public ArrayList countryLst;
    private boolean bEnableWeaponChange;
    protected boolean quikPlayer;
    protected int quikArmy;
    protected int quikPlanes;
    protected String quikPlane;
    protected String quikWeapon;
    protected int quikCurPlane;
    protected String quikRegiment;
    protected int quikSquadron;
    protected int quikWing;
    protected String quikSkin[] = {
        null, null, null, null
    };
    protected String quikNoseart[] = {
        null, null, null, null
    };
    protected String quikPilot[] = {
        null, null, null, null
    };
    protected boolean quikNumberOn[] = {
        true, true, true, true
    };
    protected int quikFuel;
    protected ArrayList quikListPlane;
    protected ArrayList quikListName;
    protected int quikPlayerPosition;
    private String planeName;
    private int playerNum;
    public GUIRenders renders;
    public Camera3D camera3D;
    public _Render3D render3D;
    public ActorHMesh actorMesh;
    public ArrayList weaponMeshs;
    public float animateMeshA;
    public float animateMeshT;
    private Orient _orient;
    private float actorAzimut;
    private Orient actorOrient;
}
