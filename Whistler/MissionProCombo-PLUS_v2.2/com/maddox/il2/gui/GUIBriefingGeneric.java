package com.maddox.il2.gui;

import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.builder.PlMission;
import com.maddox.il2.builder.Plugin;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.*;
import com.maddox.il2.game.campaign.Campaign;
import com.maddox.il2.net.NetFileServerSkin;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.*;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.SharedTokenizer;
import com.maddox.util.UnicodeTo8bit;
import java.io.*;
//import java.text.Collator;
//import java.text.RuleBasedCollator;
import java.util.*;

public class GUIBriefingGeneric extends GameState
{

//    public class byI18N_name
//        implements Comparator
//    {
//        public int compare(Object obj, Object obj1)
//        {
//            if(RTSConf.cur.locale.getLanguage().equals("ru"))
//            {
//                return collator.compare(I18N.plane(((ItemAir)obj).name), I18N.plane(((ItemAir)obj1).name));
//            } else
//            {
//                Collator collator1 = Collator.getInstance(RTSConf.cur.locale);
//                collator1.setStrength(1);
//                collator1.setDecomposition(2);
//                return collator1.compare(I18N.plane(((ItemAir)obj).name), I18N.plane(((ItemAir)obj1).name));
//            }
//        }
//
//        public byI18N_name()
//        {
//        }
//    }

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

    static class Slot
    {

        boolean bEnable;
        String wingName;
        int players;
        int fuel;
        Class planeClass;
        String planeKey;
        String weapons[];
        int weapon;

        Slot()
        {
        }
    }

    public class RenderMap2D extends Render
    {

        public void preRender()
        {
            if(main.land2D == null)
            {
                return;
            } else
            {
                Front.preRender(false);
                return;
            }
        }

        public void render()
        {
            if(main.land2D == null)
                return;
            main.land2D.render();
            if(main.land2DText != null)
                main.land2DText.render();
            drawGrid2D();
            Front.render(false);
            int i = (int)Math.round((32D * (double)((GWindow) (((GWindow) (renders)).root)).win.dx) / 1024D);
            float f = ((GWindow) (((GWindow) (client)).root)).win.dx / ((GWindow) (((GWindow) (client)).root)).win.dy;
            float f1 = 1.333333F / f;
            i = (int)((float)i * f1);
            IconDraw.setScrSize(i, i);
            doRenderMap2D();
            SquareLabels.draw(cameraMap2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
        }

        public RenderMap2D(Renders renders1, float f)
        {
            super(renders1, f);
            useClearDepth(false);
            useClearColor(false);
        }
    }

    public class Descript extends GWindowDialogClient
    {

        public void render()
        {
            String s = textDescription();
            if(s != null)
            {
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                setCanvasFont(0);
                if(bNotMultiPlay)
                {
                    if(OriginalArmy == 1)
                        setCanvasColor(colorRed);
                    else
                        setCanvasColor(colorBlue);
                } else
                {
                    setCanvasColorBLACK();
                }
                super.root.C.clip.y += gbevel.T.dy;
                super.root.C.clip.dy -= gbevel.T.dy + gbevel.B.dy;
                drawLines(gbevel.L.dx + 2.0F, gbevel.T.dy + 2.0F, s, 0, s.length(), super.win.dx - gbevel.L.dx - gbevel.R.dx - 4F, super.root.C.font.height);
            }
        }

        public void computeSize()
        {
            String s = textDescription();
            if(s != null)
            {
                super.win.dx = super.parentWindow.win.dx;
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                setCanvasFont(0);
                int i = computeLines(s, 0, s.length(), super.win.dx - gbevel.L.dx - gbevel.R.dx - 4F);
                super.win.dy = super.root.C.font.height * (float)i + gbevel.T.dy + gbevel.B.dy + 4F;
                if(super.win.dy > super.parentWindow.win.dy)
                {
                    super.win.dx = super.parentWindow.win.dx - lookAndFeel().getVScrollBarW();
                    int j = computeLines(s, 0, s.length(), super.win.dx - gbevel.L.dx - gbevel.R.dx - 4F);
                    super.win.dy = super.root.C.font.height * (float)j + gbevel.T.dy + gbevel.B.dy + 4F;
                }
            } else
            {
                super.win.dx = super.parentWindow.win.dx;
                super.win.dy = super.parentWindow.win.dy;
            }
        }

        public Descript()
        {
        }
    }

    public class ScrollDescript extends GWindowScrollingDialogClient
    {

        public void created()
        {
            super.fixed = wDescript = createDescript(this);
            super.fixed.bNotify = true;
            super.bNotify = true;
        }

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(super.notify(gwindow, i, j))
            {
                return true;
            } else
            {
                notify(i, j);
                return false;
            }
        }

        public void resized()
        {
            if(wDescript != null)
                wDescript.computeSize();
            super.resized();
            if(super.vScroll.isVisible())
            {
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                super.vScroll.setPos(super.win.dx - lookAndFeel().getVScrollBarW() - gbevel.R.dx, gbevel.T.dy);
                super.vScroll.setSize(lookAndFeel().getVScrollBarW(), super.win.dy - gbevel.T.dy - gbevel.B.dy);
            }
        }

        public void render()
        {
            setCanvasColorWHITE();
            GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
            lookAndFeel().drawBevel(this, 0.0F, 0.0F, super.win.dx, super.win.dy, gbevel, ((GUILookAndFeel)lookAndFeel()).basicelements, true);
        }

        public ScrollDescript()
        {
        }
    }

    public class DialogClient extends GUIDialogClient
    {
        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == wWeather)
            {
                doUpdateMissionParams();
                return true;
            }
            if(gwindow == wCldHeight)
            {
                doUpdateMissionParams();
                return true;
            }
            if(gwindow == wSetDate)
            {
                doValidateDate();
                doUpdateMissionParams();
                return true;
            }
            if(gwindow == wTimeHour)
            {
                doUpdateMissionParams();
                return true;
            }
            if(gwindow == wTimeMins)
            {
                doUpdateMissionParams();
                return true;
            }
            if(gwindow == wSelectFlight)
            {
                doUpdateFlight();
                return true;
            }
            if(gwindow == wSelectPlane)
            {
                int k = wSelectPlane.getSelected();
                if(k < 0)
                {
                    return true;
                } else
                {
                    wSelectPlane.setSelected(getFirstNonPlaceholder(k), true, false);
                    doUpdatePlane();
                    doUpdateSkin();
                    return true;
                }
            }
            if(gwindow == wSelectWeapons)
            {
                doUpdateWeapons();
                return true;
            }
            if(gwindow == wSelectSkin)
            {
                doUpdateSkin();
                return true;
            }
            if(gwindow == wSelectNum)
            {
                doUpdateNum();
                doUpdateSkin();
                return true;
            }
//            if(gwindow == sOwnSkinOn)
//                return true;
//            if(gwindow == sParachuteOn)
//                return true;
            if(gwindow == bDate)
                return true;
            if(gwindow == bFMB)
            {
                try
                {
                    if(!bSingleMission)
                    {
                        SectFile sectfile = Main.cur().currentMissionFile;
                        sectfile.set("MAIN", "player", originalFlightName);
                    }
                    SaveMissionDescription(OriginalFileName, LastMissionFile);
                    _leave();
                    PlMission.doSetReload(false);
                    Main.stateStack().push(18);
                    PlMission.doLoadMissionFile(LastMissionFile, OriginalFileName);
                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                }
                return true;
            }
            if(gwindow == bSave)
            {
                SaveAs();
                return true;
            }
            if(gwindow == bReset)
            {
                if(bSingleMission)
                {
                    Main.cur().currentMissionFile = new SectFile(OriginalFileName, 0);
                    OriginalFileName = null;
                    _enter();
                } else
                {
                    Main.cur().currentMissionFile = new SectFile(LastMissionFile, 0);
                    Main.cur().currentMissionFile.setFileName(OriginalFileName);
                    OriginalFileName = null;
                    if(!Main.cur().campaign.isDGen())
                        bMissionUnlocked = !bUnlock.isVisible();
                    _enter();
                }
                return true;
            }
            if(gwindow == bUnlock)
            {
                GWindowMessageBox gwindowmessagebox = new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 40F, true, "Campaign Manager", "You are about to unlock the mission in its original format. This should allow you to continue playing the campaign but it might not play out as intended.\n\nUnlock mission?", 1, 0.0F) {

                    public void result(int k)
                    {
                        if(k == 3)
                        {
                            Main.cur().currentMissionFile = new SectFile(OriginalFileName, 0);
                            OriginalFileName = null;
                            bMissionUnlocked = true;
                            _enter();
                        }
                    }
                }
;
                return true;
            }
            if(gwindow == bPrev)
            {
                if(bNotMultiPlay && bSingleMission)
                    Main.cur().currentMissionFile.saveFile(LastMissionFile);
                if(Main.stateStack().getPrevious() != null && Main.stateStack().getPrevious().id() == 18)
                {
                    if(briefSound != null)
                    {
                        CmdEnv.top().exec("music POP");
                        CmdEnv.top().exec("music PLAY");
                    }
                    PlMission.doSetReload(false);
                    Main.stateStack().push(2);
                } else
                {
                    doBack();
                }
                return true;
            }
            if(gwindow == bNext)
            {
                if(bNotMultiPlay)
                {
                    try
                    {
                        SectFile sectfile = Main.cur().currentMissionFile;
                        FlightName = sectfile.get("MAIN", "player", (String)null);
                        if(FlightName != null)
                        {
                            PlaneName = sectfile.get(FlightName, "Class", (String)null);
                            if(PlaneName != null)
                            {
                                Class planeClass = ObjIO.classForName(PlaneName);
                                String CurrentPlane = Property.stringValue(planeClass, "keyName", (String)null);
                                if(Property.value(planeClass, "cockpitClass") == null)
                                {
                                    ShowMessage(I18N.gui("warning.Warning"), "Aircraft '" + CurrentPlane + "' is for AI only. Select a different plane.");
                                } else
                                {
                                    String OldFlightName = FlightName;
                                    if(!bSingleMission)
                                    {
                                        sectfile.set("MAIN", "player", originalFlightName);
                                        sectfile.set("MAIN", "playerNum", OriginalPlayerNum);
                                    }
                                    Main.cur().currentMissionFile.saveFile(LastMissionFile);
                                    sectfile.set(FlightName, "OnlyAI", 0);
                                    sectfile.set(FlightName, "StartTime", 0);
                                    if(bSingleMission)
                                    {
                                        String playerAircraft = Property.stringValue(planeClass, "keyName", (String)null);
//                                        String LastSkin = World.cur().userCfg.getSkin(playerAircraft);
//                                        String LastNoseArt = World.cur().userCfg.getNoseart(playerAircraft);
                                        int playerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);
                                        String NewSkin = sectfile.get(FlightName, "skin" + playerNum, (String)null);
                                        String NewNoseArt = sectfile.get(FlightName, "noseart" + playerNum, (String)null);
                                        String NewPilot = sectfile.get(FlightName, "pilot" + playerNum, i18n("neta.Default"));
                                        boolean NewNumberOn = sectfile.get(FlightName, "numberOn" + playerNum, 1, 0, 1) == 1;
                                        if(playerAircraft != null)
                                        {
                                            if(NewSkin != null)
                                            {
                                                World.cur().userCfg.setSkin(playerAircraft, NewSkin);
                                                World.cur().userCfg.setNoseart(playerAircraft, NewNoseArt);
                                            } else
                                            {
                                                World.cur().userCfg.setSkin(playerAircraft, i18n("neta.Default"));
                                                World.cur().userCfg.setNoseart(playerAircraft, i18n("neta.Default"));
                                            }
                                        }
                                        World.cur().userCfg.netPilot = NewPilot;
                                        World.cur().userCfg.netNumberOn = NewNumberOn;
                                    }
//                                    if(PlaneName != null && !sOwnSkinOn.isChecked())
//                                    {
//                                        Class planeClass = ObjIO.classForName(PlaneName);
//                                        LastPlaneSkin = Property.stringValue(planeClass, "keyName", (String)null);
//                                        LastSkin = World.cur().userCfg.getSkin(LastPlaneSkin);
//                                        LastNoseArt = World.cur().userCfg.getNoseart(LastPlaneSkin);
//                                        if(FlightName != null)
//                                        {
//                                            int playerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);
//                                            String NewSkin = sectfile.get(FlightName, "skin" + playerNum, (String)null);
//                                            String NewNoseArt = sectfile.get(FlightName, "noseart" + playerNum, (String)null);
//                                            String NewPilot = sectfile.get(FlightName, "pilot" + playerNum, i18n("neta.Default"));
//                                            boolean NewNumberOn = sectfile.get(FlightName, "numberOn" + playerNum, 1, 0, 1) == 1;
//                                            if(LastPlaneSkin != null)
//                                                if(NewSkin != null)
//                                                {
//                                                    World.cur().userCfg.setSkin(LastPlaneSkin, NewSkin);
//                                                    World.cur().userCfg.setNoseart(LastPlaneSkin, NewNoseArt);
//                                                } else
//                                                {
//                                                    World.cur().userCfg.setSkin(LastPlaneSkin, i18n("neta.Default"));
//                                                    World.cur().userCfg.setNoseart(LastPlaneSkin, i18n("neta.Default"));
//                                                }
//                                            World.cur().userCfg.netPilot = NewPilot;
//                                            World.cur().userCfg.netNumberOn = NewNumberOn;
//                                        }
//                                    }
//                                    if(!sParachuteOn.isChecked())
//                                    {
//                                        int n = sectfile.sectionIndex("Wing");
//                                        if(n >= 0)
//                                        {
//                                            int k = sectfile.vars(n);
//                                            for(int i1 = 0; i1 < k; i1++)
//                                            {
//                                                String s = sectfile.var(n, i1);
//                                                if(!s.equalsIgnoreCase(FlightName))
//                                                    sectfile.set(s, "Parachute", "0");
//                                            }
//
//                                        }
//                                    }
                                    if(!bSingleMission && !OldFlightName.equalsIgnoreCase(originalFlightName))
                                    {
                                        String Message = "You will be flying in the original Flight '" + originalFlightName + "' with aircraft '" + CurrentPlane + "' in position #" + (OriginalPlayerNum + 1);
                                        new GWindowMessageBox(((GWindow) (client)).root, 30F, true, "Campaign Manager", Message, 3, 0.0F) {

                                            public void result(int i)
                                            {
                                                if(i == 2)
                                                    doNext();
                                            }
                                        }
;
                                    } else
                                    {
                                        doNext();
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
                } else
                {
                    doNext();
                }
                return true;
            }
            if(gwindow == bDifficulty)
            {
                doDiff();
                return true;
            }
            if(gwindow == bLoodout)
            {
//                if(bSingleMission && !sOwnSkinOn.isChecked())
                if(bSingleMission)
                {
                    toAirArming();
                } else
                {
                    if(bNotMultiPlay)
                    {
                        GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
                        SectFile sectfile = Main.cur().currentMissionFile;
                        try
                        {
                            guiairarming.quikPlayerPosition = FlightName.equalsIgnoreCase(originalFlightName) ? OriginalPlayerNum : -1;
                            guiairarming.quikCurPlane = sectfile.get("MAIN", "playerNum", 0, 0, 3);
                            guiairarming.quikPlayer = true;
                        }
                        catch(Exception exception)
                        {
                            System.out.println(exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                    doLoodout();
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
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(640F), x1024(960F), 2.0F);
// Fly & Apply lines
            if(bNext.isVisible())
            {
                GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(686F), x1024(30F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(537F), y1024(686F), x1024(30F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(650F), 2.0F, y1024(36F));
                GUISeparate.draw(this, GColor.Gray, x1024(566F), y1024(650F), 2.0F, y1024(36F));
            }
            setCanvasColorWHITE();
            GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
// Map
            if(bNotMultiPlay)
                guilookandfeel.drawBevel(this, x1024(32F), y1024(42F), x1024(528F), y1024(505F), guilookandfeel.bevelComboDown, guilookandfeel.basicelements);
            else
                guilookandfeel.drawBevel(this, x1024(32F), y1024(42F), x1024(528F), y1024(550F), guilookandfeel.bevelComboDown, guilookandfeel.basicelements);
            setCanvasFont(0);
            setCanvasColor(GColor.Gray);
            if(bNotMultiPlay)
            {
                draw(x1024(37F), y1024(42F) - M(2.0F), x1024(170F), M(2.0F), 0, MissionMap);
                if(Main.state() != null && Main.state().id() != 64)
                {
                    draw(x1024(581F), y1024(633F), x1024(170F), y1024(48F), 1, i18n("quick.RES"));
                    if(bSingleMission)
                    {
                        draw(x1024(783F), y1024(633F), x1024(170F), y1024(48F), 1, i18n("F.M.B."));
                        draw(x1024(883F), y1024(633F), x1024(170F), y1024(48F), 1, i18n("quick.SAV"));
                    } else if(!Main.cur().campaign.isDGen())
                    {
                        if(bUnlock.isVisible())
                            draw(x1024(833F), y1024(633F), x1024(170F), y1024(48F), 1, i18n("Unlock"));
                        else
                            draw(x1024(833F), y1024(657F), x1024(170F), y1024(48F), 1, "- " + i18n("Unlocked") + " -");
                    }
//                    draw(x1024(597F), y1024(509F), x1024(220F), M(2.0F), 0, sOwnSkinOn.isChecked() ? "Player Skin" : "Mission Skin");
                    draw(x1024(10F), y1024(566F), x1024(64F), M(2.0F), 2, i18n("Flight"));
//                    draw(x1024(265F), y1024(566F), x1024(64F), M(2.0F), 2, i18n("netair.Position"));
                    draw(x1024(265F), y1024(566F), x1024(64F), M(2.0F), 2, i18n("quick.+/-"));
                    draw(x1024(10F), y1024(600F), x1024(64F), M(2.0F), 2, i18n("neta.Skin"));
                    draw(x1024(518F), y1024(566F), x1024(64F), M(2.0F), 2, i18n("quick.PLA"));
//                    draw(x1024(518F), y1024(600F), x1024(64F), M(2.0F), 2, i18n("quick.TNT"));
                    draw(x1024(518F), y1024(600F), x1024(64F), M(2.0F), 2, i18n("arming.Weapons"));
                }
                draw(x1024(612F) - x1024(35F), y1024(475F), x1024(64F), M(2.0F), 2, i18n("dgendetail.Date"));
                wSetDate.align = wSetDate.isActivated() ? 0 : 1;
                draw(x1024(612F) - x1024(35F), y1024(509F), x1024(64F), M(2.0F), 2, i18n("quick.TIM"));
                draw(x1024(808F) - x1024(35F), y1024(475F), x1024(64F), M(2.0F), 2, i18n("quick.WEA"));
                draw(x1024(808F) - x1024(35F), y1024(509F), x1024(64F), M(2.0F), 2, i18n("diff.Clouds"));
                GUISeparate.draw(this, GColor.Gray, x1024(592F), y1024(547F), x1024(400F), 2.0F);
            }
            clientRender();
        }

        public void resized()
        {
            super.resized();
            if(renders != null)
            {
                GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
                GBevel gbevel = guilookandfeel.bevelComboDown;
                if(bNotMultiPlay)
                    renders.setPosSize(x1024(32F) + gbevel.L.dx, y1024(42F) + gbevel.T.dy, x1024(528F) - gbevel.L.dx - gbevel.R.dx, y1024(505F) - gbevel.T.dy - gbevel.B.dy);
                else
                    renders.setPosSize(x1024(32F) + gbevel.L.dx, y1024(42F) + gbevel.T.dy, x1024(528F) - gbevel.L.dx - gbevel.R.dx, y1024(550F) - gbevel.T.dy - gbevel.B.dy);
            }
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            bPrev.setPosC(x1024(85F), y1024(689F));
            bDifficulty.setPosC(x1024(298F), y1024(689F));
            bLoodout.setPosC(x1024(768F), y1024(689F));
            bNext.setPosC(x1024(512F), y1024(689F));
            bReset.setPosC(x1024(666F), y1024(689F));
            bFMB.setPosC(x1024(868F), y1024(689F));
            bSave.setPosC(x1024(968F), y1024(689F));
            bUnlock.setPosC(x1024(918F), y1024(689F));
            if(!bNotMultiPlay)
            {
                wScrollDescription.setPosSize(x1024(592F), y1024(42F), x1024(400F), y1024(550F));
            } else
            {
//                if(bSingleMission)
//                    wScrollDescription.setPosSize(x1024(592F), y1024(42F), x1024(400F), y1024(505F));
//                else
                    wScrollDescription.setPosSize(x1024(592F), y1024(42F), x1024(400F), y1024(414F));

//                sOwnSkinOn.setPosC(x1024(733F), y1024(519F));
                wSelectFlight.setPosSize(x1024(84F), y1024(566F), x1024(205F), M(1.7F));
                wSelectNum.setPosSize(x1024(339F), y1024(566F), x1024(180F), M(1.7F));
                wSelectSkin.setPosSize(x1024(84F), y1024(600F), x1024(435F), M(1.7F));
                wSelectPlane.setPosSize(x1024(592F), y1024(566F), x1024(400F), M(1.7F));
                wSelectWeapons.setPosSize(x1024(592F), y1024(600F), x1024(400F), M(1.7F));
                wSetDate.setPosSize(x1024(686F) - x1024(35F), y1024(475F), x1024(125F) - ComboButtonWidth, M(1.7F));
                bDate.setPosC(x1024(811F) - x1024(35F) - M(1.7F / 2), y1024(475F) + M(1.7F / 2));
                wTimeHour.setPosSize(x1024(686F) - x1024(35F), y1024(509F), x1024(60F), M(1.7F));
                wTimeMins.setPosSize(x1024(751F) - x1024(35F), y1024(509F), x1024(60F), M(1.7F));
                wWeather.setPosSize(x1024(882F) - x1024(35F), y1024(475F), x1024(110F), M(1.7F));
                wCldHeight.setPosSize(x1024(882F) - x1024(35F), y1024(509F), x1024(110F), M(1.7F));
            }
            clientSetPosSize();
        }


        public DialogClient()
        {
        }
    }

//    class DlgFileConfirmSave extends GWindowFileBoxExec
//    {
//
//        public boolean isCloseBox()
//        {
//            return bClose;
//        }
//
//        public void exec(GWindowFileBox gwindowfilebox, String s)
//        {
//            box = gwindowfilebox;
//            bClose = true;
//            if(s == null || box.files.size() == 0)
//            {
//                box.endExec();
//            } else
//            {
//                int i = s.lastIndexOf("/");
//                if(i >= 0)
//                    s = s.substring(i + 1);
//                for(int j = 0; j < box.files.size(); j++)
//                {
//                    String s1 = ((File)box.files.get(j)).getName();
//                    if(s.compareToIgnoreCase(s1) == 0)
//                    {
//                        new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 20F, true, I18N.gui("warning.Warning"), I18N.gui("warning.ReplaceFile"), 1, 0.0F) {
//
//                            public void result(int k)
//                            {
//                                if(k != 3)
//                                    bClose = false;
//                                box.endExec();
//                            }
//
//                        }
//;
//                        return;
//                    }
//                }
//
//                return;
//            }
//        }
//
//        GWindowFileBox box;
//        boolean bClose;
//
//        DlgFileConfirmSave()
//        {
//            bClose = true;
//        }
//    }

    public void ShowMessage(String a, String b)
    {
        new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 40F, true, a, b, 3, 0.0F);
    }

    public void SaveAs()
    {
        missionFileName = OriginalFileName;
        lastOpenFile = OriginalFileName;
        GWindowMessageBox gwindowmessagebox = new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 25F, true, I18N.gui("warning.Warning"), I18N.gui("warning.ReplaceFile"), 1, 0.0F) {

              public void result(int k)
              {
                  if(k == 3)
                    Main.cur().currentMissionFile.saveFile(OriginalFileName);
//                      bClose = false;
//                  box.endExec();
              }

          }
;

//        GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(((GWindowManager) (Main3D.cur3D().guiManager)).root, true, Plugin.i18n("SaveMission") + ": " + OriginalFileName, "missions/", new GFileFilter[] {
//            new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] {
//                "*.mis"
//            })
//        }) {
//
//            public void result(String s)
//            {
//                if(s != null)
//                {
//                    s = checkMisExtension(s);
//                    missionFileName = s;
//                    lastOpenFile = s;
//                    SaveMissionDescription(OriginalFileName, s);
//                    Main.cur().currentMissionFile.saveFile(s);
//                }
//            }
//
//        }
//;
//        ((GWindowFramed) (gwindowfilesaveas)).clientWindow.resized();
//        gwindowfilesaveas.exec = new DlgFileConfirmSave();
//        if(lastOpenFile != null)
//            gwindowfilesaveas.setSelectFile(lastOpenFile);

    }

//    private String checkMisExtension(String s)
//    {
//        if(!s.toLowerCase().endsWith(".mis"))
//            return s + ".mis";
//        else
//            return s;
//    }

    private String textFileName(String tFileName, boolean flag)
    {
        String s = "";
        if(flag)
        {
            String s1 = Locale.getDefault().getLanguage();
            String s2 = RTSConf.cur.locale.getLanguage();
            if(s2.equalsIgnoreCase("ru") && !s1.equalsIgnoreCase("ru"))
                s1 = "us";
            if(s1 == null || s1.length() < 2)
                s1 = "us";
            if(!"us".equals(s1))
                s = "_" + s1;
        }
//        String s2 = tFileName;
//        for(int i = s2.length() - 1; i >= 0; i--)
//        {
//            char c = s2.charAt(i);
//            if(c == '/' || c == '\\')
//                break;
//            if(c == '.')
//                return s2.substring(0, i) + s + ".properties";
//        }
        return tFileName + s + ".properties";
    }

    private void SaveMissionDescription(String OldFileName, String NewFileName)
    {
//        String nameProps = "";
//        String shortProps = "";
//        String descriptionProps = "";
//        String s = OldFileName;
//        for(int i = s.length() - 1; i > 0; i--)
//        {
//            char c = s.charAt(i);
//            if(c == '\\' || c == '/')
//                break;
//            if(c != '.')
//                continue;
//            s = s.substring(0, i);
//            break;
//        }
//
//        try
//        {
//            ResourceBundle resourcebundleN = ResourceBundle.getBundle(s, RTSConf.cur.locale);
//            nameProps = resourcebundleN.getString("Name");
//        }
//        catch(Exception exceptionProps) { }
//        try
//        {
//            ResourceBundle resourcebundleP = ResourceBundle.getBundle(s, RTSConf.cur.locale);
//            shortProps = resourcebundleP.getString("Short");
//        }
//        catch(Exception exceptionShort) { }
//        try
//        {
//            ResourceBundle resourcebundleD = ResourceBundle.getBundle(s, RTSConf.cur.locale);
//            descriptionProps = resourcebundleD.getString("Description");
//        }
//        catch(Exception exceptionDescription) { }
        String s = NewFileName;
        int i = s.lastIndexOf(".");
        if(i >= 0)
            s = s.substring(0, i);
        PrintWriter printwriter = null;
        try
        {
            printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(textFileName(s, true), 0))));
            if(textName == null)
                textName = " ";
            printwriter.println("Name " + UnicodeTo8bit.save(textName, false));
            if(textShort == null)
                textShort = " ";
            printwriter.println("Short " + UnicodeTo8bit.save(textShort, false));
//            if(textDescription == null)
//                textDescription = " ";
//            if(textDescription.toLowerCase().indexOf(HeaderDescript.toLowerCase().trim()) == 0)
//                textDescription = DescriptString == null ? textDescription : DescriptString + "\n" + textDescription;
//            printwriter.println("Description " + UnicodeTo8bit.save(textDescription, false));
            if(textLong == null)
                textLong = " ";
            printwriter.println("Description " + UnicodeTo8bit.save(textLong, false));
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        if(printwriter != null)
            try
            {
                printwriter.close();
            }
            catch(Exception exception1) { }
    }

    private int getFirstNonPlaceholder(int k)
    {
        int k0 = k;
        ItemAir itemair;
        for(itemair = (ItemAir)aiPlane.get(k0); itemair.className.equalsIgnoreCase(PlaceholderLabel); itemair = (ItemAir)aiPlane.get(k))
        {
            if(k >= wSelectPlane.size() - 1)
                break;
            k++;
        }

        if(k >= wSelectPlane.size() - 1)
        {
            k = k0;
            for(; itemair.className.equalsIgnoreCase(PlaceholderLabel); itemair = (ItemAir)aiPlane.get(k))
            {
                if(k <= 0)
                    break;
                k--;
            }

        }
        return k;
    }

    public void enterPush(GameState gamestate)
    {
        bSingleMission = false;
        if(gamestate.id() == 3)
        {
            bSingleMission = true;
            OriginalFileName = null;
        }
        if(gamestate.id() == 18)
        {
            bSingleMission = true;
            OriginalFileName = null;
        }
        selectedFlightName = null;
        _enter();
    }

    public void enter(GameState gamestate)
    {
        if(gamestate.id() == 27)
        {
            bSingleMission = false;
            OriginalFileName = null;
            selectedFlightName = null;
        }
        super.enter(gamestate);
    }

    public void enterPop(GameState gamestate)
    {
        if(gamestate.id() == 18)
        {
            Main.cur().currentMissionFile = new SectFile(OriginalFileName, 0);
            OriginalFileName = null;
            selectedFlightName = null;
            _enter();
        }
        if(gamestate.id() == 55)
        {
            fromAirArming();
            if(!bSingleMission)
                bCampaignChanged = false;
            _enter();
        }
        if(!client.isActivated())
            client.activateWindow();
        if(gamestate.id() == 54)
        {
            try
            {
                SectFile sectfile = Main.cur().currentMissionFile;
                if(FlightName != null && PlaneName != null)
                {
                    Class planeClass = ObjIO.classForName(PlaneName);
                    String LastPlane = Property.stringValue(planeClass, "keyName", (String)null);
                    String LastSkin = World.cur().userCfg.getSkin(LastPlane);
                    String LastNoseArt = World.cur().userCfg.getNoseart(LastPlane);
                    String LastPilot = World.cur().userCfg.netPilot;
                    boolean NumberOn = World.cur().userCfg.netNumberOn;
                    if(LastSkin == null || LastSkin.length() == 0)
                        LastSkin = i18n("neta.Default");
                    sectfile.set(FlightName, "skin" + wSelectNum.getSelected(), LastSkin);
                    if(LastNoseArt == null || LastNoseArt.length() == 0)
                        LastNoseArt = "";
                    sectfile.set(FlightName, "noseart" + wSelectNum.getSelected(), LastNoseArt);
                    if(LastPilot == null || LastPilot.length() == 0)
                        LastPilot = "";
                    sectfile.set(FlightName, "pilot" + wSelectNum.getSelected(), LastPilot);
                    sectfile.set(FlightName, "numberOn" + wSelectNum.getSelected(), NumberOn ? 1 : 0);
                    String LastWeapon = sectfile.get(FlightName, "weapons", i18n("neta.Default"));
                    wSelectWeapons.setSelected(0, false, false);
                    for(int i = 0; i < wSelectWeapons.size(); i++)
                    {
                        String s1 = Aircraft.getWeaponsRegistered(planeClass)[i];
                        if(!s1.equalsIgnoreCase(LastWeapon))
                            continue;
                        wSelectWeapons.setSelected(i, true, false);
                        break;
                    }
                    wSelectWeapons.setValue((String)wSelectWeapons.list.get(wSelectWeapons.getSelected()));
                    wSelectSkin.setSelected(0, false, false);
                    for(int i = 0; i < wSelectSkin.size(); i++)
                    {
                        if(!LastSkin.equalsIgnoreCase((String)wSelectSkin.list.get(i)))
                            continue;
                        wSelectSkin.setSelected(i, true, false);
                        break;
                    }
                    wSelectSkin.setValue((String)wSelectSkin.list.get(wSelectSkin.getSelected()) + (NumberOn ? " + " + i18n("Markings") : ""));
                }
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
        if(bNotMultiPlay)
            wCldHeight.setEnable(World.cur().diffUser.Clouds);
    }

    public void toAirArming()
    {
        GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
        try
        {
            guiairarming.quikListPlane.clear();
            guiairarming.quikListName.clear();
            ArrayList arraylist = null;
            arraylist = aiPlane;
            for(int j = 0; j < arraylist.size(); j++)
            {
                ItemAir itemair = (ItemAir)arraylist.get(j);
                guiairarming.quikListPlane.add(itemair.clazz);
                if(itemair.className.equalsIgnoreCase(PlaceholderLabel))
                    guiairarming.quikListName.add(itemair.name);
                else
                    guiairarming.quikListName.add((itemair.bEnablePlayer ? "" : "(AI) ") + I18N.plane(itemair.name));
            }
            SectFile sectfile = Main.cur().currentMissionFile;
            guiairarming.quikPlayer = true;
            guiairarming.quikPlayerPosition = wSelectNum.getSelected();
            guiairarming.quikArmy = sectfile.get("MAIN", "army", 1, 1, 2);
            guiairarming.quikPlanes = sectfile.get(FlightName, "Planes", 1, 1, 4);
            guiairarming.quikCurPlane = sectfile.get("MAIN", "playerNum", 0, 0, 3);
            try
            {
                ItemAir plane = (ItemAir)aiPlane.get(wSelectPlane.getSelected());
                guiairarming.quikPlane = plane.name;
            }
            catch(Exception exception) { }
            guiairarming.quikWeapon = sectfile.get(FlightName, "weapons", i18n("neta.Default"));
//            guiairarming.quikCurPlane = 0;
            guiairarming.quikCurPlane = wSelectNum.getSelected();
            if(FlightName.length() > 2)
            {
                guiairarming.quikRegiment = FlightName.substring(0, FlightName.length() - 2);
                guiairarming.quikWing = Integer.parseInt(FlightName.substring(FlightName.length() - 2));
            }
            guiairarming.quikFuel = sectfile.get(FlightName, "Fuel", 100);
            for(int i = 0; i < 4; i++)
            {
                guiairarming.quikSkin[i] = sectfile.get(FlightName, "skin" + i, i18n("neta.Default"));
                guiairarming.quikNoseart[i] = sectfile.get(FlightName, "noseart" + i, i18n("neta.Default"));
                guiairarming.quikPilot[i] = sectfile.get(FlightName, "pilot" + i, i18n("neta.Default"));
                guiairarming.quikNumberOn[i] = sectfile.get(FlightName, "numberOn" + i, 1, 0, 1) == 1;
            }
            GUIAirArming.stateId = 4;
            Main.stateStack().push(55);
            guiairarming.cPlane.setSelected(wSelectNum.getSelected(), true, false);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void fromAirArming()
    {
        if(Mission.isNet())
            return;
        GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
        try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            int prePlanes = sectfile.get(FlightName, "Planes", 1, 1, 4);
            int preSkill = sectfile.get(FlightName, "Skill", 1, 0, 3);
            String NewFlightName = guiairarming.quikRegiment + (guiairarming.quikWing >= 10 ? "" + guiairarming.quikWing : "0" + guiairarming.quikWing);
            if(NewFlightName.length() > 2)
            {
                int i = sectfile.sectionIndex("Wing");
                if(prePlanes == 0)
                {
                    sectfile.lineRemove(i, sectfile.varIndex(i, NewFlightName));
                    sectfile.sectionRemove(sectfile.sectionIndex(NewFlightName));
                    sectfile.sectionRemove(sectfile.sectionIndex(NewFlightName + "_Way"));
                }
                if(!NewFlightName.equalsIgnoreCase(FlightName))
                {
                    sectfile.lineRemove(i, sectfile.varIndex(i, FlightName));
                    sectfile.lineAdd(i, NewFlightName);
                    sectfile.sectionRename(sectfile.sectionIndex(FlightName + "_Way"), NewFlightName + "_Way");
                    sectfile.sectionRename(sectfile.sectionIndex(FlightName), NewFlightName);
                }
                int j = sectfile.sectionIndex(NewFlightName);
                if(j > 0)
                {
                    sectfile.sectionClear(j);
                    sectfile.lineAdd(j, "Planes " + prePlanes);
                    sectfile.lineAdd(j, "Skill " + preSkill);
                    sectfile.lineAdd(j, "Fuel " + guiairarming.quikFuel);
                    if(guiairarming.quikWeapon != null)
                        sectfile.lineAdd(j, "weapons " + guiairarming.quikWeapon);
                    else
                        sectfile.lineAdd(j, "weapons default");
                    for(int k = 0; k < prePlanes; k++)
                    {
                        if(guiairarming.quikSkin[k] != null)
                            sectfile.lineAdd(j, "skin" + k + " " + guiairarming.quikSkin[k]);
                        if(guiairarming.quikNoseart[k] != null)
                            sectfile.lineAdd(j, "noseart" + k + " " + guiairarming.quikNoseart[k]);
                        if(guiairarming.quikPilot[k] != null)
                            sectfile.lineAdd(j, "pilot" + k + " " + guiairarming.quikPilot[k]);
//                        if(!guiairarming.quikNumberOn[k])
//                            sectfile.lineAdd(j, "numberOn" + k + " 0");
                        sectfile.lineAdd(j, "numberOn" + k + (guiairarming.quikNumberOn[k] ? " 1" : " 0"));
                    }

                    ItemAir itemair = null;
                    ArrayList arraylist = null;
                    arraylist = aiPlane;
                    i = 0;
                    do
                    {
                        if(i >= arraylist.size())
                            break;
                        itemair = (ItemAir)arraylist.get(i);
                        if(itemair.name.equals(guiairarming.quikPlane))
                            break;
                        i++;
                    } while(true);
                    sectfile.lineAdd(j, "Class " + itemair.className);
                    PlaneName = itemair.className;
                }
                if(originalFlightName.equalsIgnoreCase(FlightName))
                    originalFlightName = NewFlightName;
                selectedFlightName = NewFlightName;
                FlightName = NewFlightName;
                sectfile.set("MAIN", "player", FlightName);
            }
            sectfile.saveFile(LastMissionFile);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void setColors()
    {
        bUseColor = Config.cur.ini.get("Mods", "PALMODsColor", 1, 0, 1);
        colorRed = bUseColor == 1 ? 0xff000080 : 0;
        colorBlue = bUseColor == 1 ? 0xff800000 : 0;
        colorGray = 0xff505050;
    }

    public void _enter()
    {
        setColors();
        if(!client.isActivated())
            client.activateWindow();
        bNotMultiPlay = !Mission.isNet();
//        try
//        {
//            IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);
//            sParachuteOn.bChecked = inifile.get("LastSingleMission", "ParachuteOn", true);
//            if(bSingleMission)
//                sOwnSkinOn.bChecked = false;
//            else
//                sOwnSkinOn.bChecked = inifile.get("LastSingleMission", "OwnSkinOn", false);
//            inifile.saveFile();
//        }
//        catch(Exception exception)
//        {
//            System.out.println(exception.getMessage());
//            exception.printStackTrace();
//        }
        try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            if(selectedFlightName == null)
                originalFlightName = sectfile.get("MAIN", "player", (String)null);
            selectedFlightName = null;
            OriginalArmy = sectfile.get("MAIN", "army", 1, 1, 2);
            if(bNotMultiPlay)
                OriginalPlaneName = sectfile.get(originalFlightName, "Class", (String)null);
            OriginalPlayerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);
            if(originalFlightName != null && originalFlightName.length() > 2)
            {
                String FlightName1 = originalFlightName.substring(0, originalFlightName.length() - 1);
                String FlightName2 = FlightName1.substring(0, FlightName1.length() - 1);
                Regiment regiment = (Regiment)Actor.getByName(FlightName2);
                if(regiment.getArmy() == 1)
                {
                    OriginalArmy = 1;
                    sectfile.set("MAIN", "army", 1);
                } else
                {
                    OriginalArmy = 2;
                    sectfile.set("MAIN", "army", 2);
                }
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        if(bNotMultiPlay)
        {
            fillArrayPlanes();
            String FormerFileName = null;
//            sParachuteOn.showWindow();
//            if(bSingleMission)
//                sOwnSkinOn.hideWindow();
//            else
//                sOwnSkinOn.showWindow();
            wWeather.showWindow();
            wCldHeight.showWindow();
            wSetDate.showWindow();
            bDate.showWindow();
            wTimeHour.showWindow();
            wTimeMins.showWindow();
            if(Main.state() != null && Main.state().id() != 64)
            {
                bReset.showWindow();
                if(bSingleMission)
                {
                    bFMB.showWindow();
                    bSave.showWindow();
                } else if(!Main.cur().campaign.isDGen())
                {
                    bUnlock.showWindow();
                    if(bMissionUnlocked)
                    {
                        bUnlock.hideWindow();
                        bMissionUnlocked = false;
                    }
                }
                wSelectFlight.showWindow();
                wSelectPlane.showWindow();
                wSelectNum.showWindow();
                wSelectWeapons.showWindow();
//            if(bSingleMission)
//                wSelectSkin.hideWindow();
//            else
                wSelectSkin.showWindow();
            }
            if(bSingleMission)
            {
                FormerFileName = OriginalFileName;
                OriginalFileName = Main.cur().currentMissionFile.fileName();
                if(!OriginalFileName.equalsIgnoreCase(FormerFileName))
                {
                    try
                    {
                        Main.cur().currentMissionFile.saveFile(LastMissionFile);
                        Main.cur().currentMissionFile = new SectFile(LastMissionFile, 0);
                        Main.cur().currentMissionFile.setFileName(OriginalFileName);
                    }
                    catch(Exception exception)
                    {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                } else
                {
                    try
                    {
                        Main.cur().currentMissionFile = new SectFile(LastMissionFile, 0);
                        Main.cur().currentMissionFile.setFileName(OriginalFileName);
                    }
                    catch(Exception exception)
                    {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                }
                try
                {
//                    Class planeClass = ObjIO.classForName(OriginalPlaneName);
//                    DescriptString = "*** Mission brief for Flight " + originalFlightName + " ***\n*** with plane " + Property.stringValue(planeClass, "keyName", null) + " of the " + (OriginalArmy != 1 ? "Blue army ***" : "Red army ***");
                    DescriptString = "*** Briefing for Flight '" + originalFlightName + "' of the " + (OriginalArmy != 1 ? "Blue Army" : "Red Army") + " ***";
                }
                catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                }
            } else
            {
                FormerFileName = OriginalFileName;
                OriginalFileName = Main.cur().currentMissionFile.fileName();
                if(curMissionNum != Main.cur().missionCounter || !OriginalFileName.equalsIgnoreCase(FormerFileName))
                {
                    try
                    {
                        Main.cur().currentMissionFile.saveFile(LastMissionFile);
                        Main.cur().currentMissionFile = new SectFile(LastMissionFile, 0);
                        Main.cur().currentMissionFile.setFileName(OriginalFileName);
                    }
                    catch(Exception exception)
                    {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                } else
                {
                    try
                    {
                        Main.cur().currentMissionFile = new SectFile(LastMissionFile, 0);
                        Main.cur().currentMissionFile.setFileName(OriginalFileName);
                    }
                    catch(Exception exception)
                    {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                }
                if(originalFlightName.equalsIgnoreCase(Main.cur().campaign.originalFlightName()) || !bCampaignChanged)
                {
                    try
                    {
//                        Class planeClass = ObjIO.classForName(OriginalPlaneName);
                        DescriptString = "*** Briefing for Flight '" + originalFlightName + "' of the " + (OriginalArmy != 1 ? "Blue Army" : "Red Army") + " ***";
                    }
                    catch(Exception exception)
                    {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                } else if(!Main.cur().campaign.isDGen())
                {
                    try
                    {
// ERRORS                 ResourceBundle resourcebundle = ResourceBundle.getBundle("missions/campaign/" + Main.cur().campaign.branch() + "/" + "rank", RTSConf.cur.locale);
//                        Class oriplaneClass = ObjIO.classForName(Main.cur().campaign.originalPlaneName());
//                        Class planeClass = ObjIO.classForName(OriginalPlaneName);
//                        DescriptString = "*** This mission was modified by IL-2 Campaign Manager! ***\n*** It was originally for the Flight " + Main.cur().campaign.originalFlightName() + " with plane " + Property.stringValue(oriplaneClass, "keyName", null) + " of the " + (OriginalArmy != 1 ? "Blue army ***" : "Red army ***") + "\n\n Because your rank of '" + resourcebundle.getString("" + Main.cur().campaign.originalRank()) + "' was not enough for the mission, you will be flying the " + originalFlightName + " Flight with plane " + Property.stringValue(planeClass, "keyName", null) + "\n\n --- You will allways fly in this Flight even if you change the plane for it ---";
//                        ShowMessage("Warning", "*** This mission was modified by IL-2 Campaign Manager! ***\nThe rank of '" + resourcebundle.getString("" + Main.cur().campaign.originalRank()) + "' you have is not enough to fly the " + Property.stringValue(oriplaneClass, "keyName", null) + " in the position " + (Main.cur().campaign.originalPlayerNum() + 1) + " of the Flight " + Main.cur().campaign.originalFlightName());

//                        ResourceBundle resourcebundle = ResourceBundle.getBundle("missions/campaign/" + Main.cur().campaign.branch() + "/" + "rank", RTSConf.cur.locale);
//                        ShowMessage("Campaign Over", "Your rank '" + resourcebundle.getString("" + Main.cur().campaign.originalRank()) + "' is not high enough to continue this campaign as intended. Please start a new campaign with a higher rank.\n\nAlternatively, click Reset to unlock the original mission.");
                        DescriptString = "*** Briefing for Flight '" + Main.cur().campaign.originalFlightName() + "' of the " + (OriginalArmy != 1 ? "Blue Army" : "Red Army") + " ***";
                    }
                    catch(Exception exception)
                    {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                }
            }
            bCampaignChanged = true;
            try
            {
                SectFile sectfile = Main.cur().currentMissionFile;
                briefSound = sectfile.get("MAIN", "briefSound");
                String s = Main.cur().currentMissionFile.fileName();
                String s1 = sectfile.get("MAIN", "MAP");
                fillComboPlane(wSelectPlane);
                fillSelectFlight();
                int v = (int)sectfile.get("MAIN", "CloudType", 0, 0, 6);
                wWeather.setSelected(v, true, false);
                v = (int)sectfile.get("MAIN", "CloudHeight", 1000F, 300F, 5000F);
                wCldHeight.clear();
                int[] cldHeight = new int[] { 300, 500, 750, 1000, 1250, 1500, 1750, 2000, 2250, 2500, 2750, 3000, 4000, 5000 };
                boolean selected = false;
                for(int i = 0; i < cldHeight.length; i++)
                {
                    wCldHeight.add("" + cldHeight[i] + "m");
                    if(!selected && cldHeight[i] == v)
                    {
                        wCldHeight.setSelected(i, true, false);
                        selected = true;
                    }
                    if(!selected && i < cldHeight.length && v < cldHeight[i + 1])
                    {
                        wCldHeight.add("" + v + "m");
                        wCldHeight.setSelected(i + 1, true, false);
                        selected = true;
                    }
                }
                wCldHeight.setEnable(World.cur().diffUser.Clouds);
//                wCldHeight.add("300m");
//                wCldHeight.add("500m");
//                wCldHeight.add("750m");
//                wCldHeight.add("1000m");
//                wCldHeight.add("1250m");
//                wCldHeight.add("1500m");
//                wCldHeight.add("1750m");
//                wCldHeight.add("2000m");
//                wCldHeight.add("2250m");
//                wCldHeight.add("2500m");
//                wCldHeight.add("2750m");
//                wCldHeight.add("3000m");
//                wCldHeight.add("4000m");
//                wCldHeight.add("5000m");
//                String sv = v + "m";
//                if(wCldHeight.list.indexOf(sv) < 0)
//                {
//                    sv = v + "m";
//                    wCldHeight.add(sv);
//                    wCldHeight.setSelected(wCldHeight.list.size() - 1, true, false);
//                } else
//                {
//                    wCldHeight.setSelected(wCldHeight.list.indexOf(sv), true, false);
//                }
//                wCldHeight.setEnable(World.cur().diffUser.Clouds);
                float t = sectfile.get("MAIN", "TIME", 12F, 0.0F, 23.99F);
                int h = (int)t;
                String sv = (h >= 10 ? "" : "0") + h + "h";
                v = wTimeHour.list.indexOf(sv);
                if(v < 0)
                    v = 12;
                wTimeHour.setSelected(v, true, false);
                int m = (int)((t % 1.0F) * 60F);
                sv = (m >= 10 ? "" : "0") + m + "m";
                v = wTimeMins.list.indexOf(sv);
                if(v < 0)
                    v = 0;
                wTimeMins.setSelected(v, true, false);

                if(!OriginalFileName.equalsIgnoreCase(FormerFileName) || !s1.equalsIgnoreCase(curMapName) || curMissionNum != Main.cur().missionCounter || main.land2D == null)
                {
                    dialogClient.resized();
                    fillTextDescription();
                    fillMap();
                    Front.loadMission(sectfile);
                    curMissionName = s;
                    curMapName = s1;
                    curMissionNum = Main.cur().missionCounter;
                }

                if(!sectfile.sectionExist("SEASON"))
                {
                    sectfile.sectionAdd("SEASON");
                    int i1 = sectfile.sectionIndex("SEASON");
                    int i2 = World.land().config.getDefaultMonth("maps/" + s1);
                    sectfile.lineAdd(i1, "Year", "1940");
                    sectfile.lineAdd(i1, "Month", "" + i2);
                    sectfile.lineAdd(i1, "Day", "15");
                    OriginalDate = "15." + (i2 < 10 ? "0" + i2 : "" + i2) + ".1940";
                } else
                {
                    int year = sectfile.get("SEASON", "Year", 1940, 1900, Calendar.getInstance().get(Calendar.YEAR));
                    int month = sectfile.get("SEASON", "Month", World.land().config.getDefaultMonth("maps/" + s1), 1, 12);
                    int day = sectfile.get("SEASON", "Day", 15, 1, 31);
                    OriginalDate = (day < 10 ? "0" + day : "" + day) + "." + (month < 10 ? "0" + month : "" + month) + "." + year;
                }
                wSetDate.setValue(OriginalDate, false);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        } else
        {
//            sOwnSkinOn.hideWindow();
//            sParachuteOn.hideWindow();
            wWeather.hideWindow();
            wCldHeight.hideWindow();
            wSetDate.hideWindow();
            bDate.hideWindow();
            wTimeHour.hideWindow();
            wTimeMins.hideWindow();
            wSelectFlight.hideWindow();
            wSelectPlane.hideWindow();
            wSelectNum.hideWindow();
            wSelectWeapons.hideWindow();
            wSelectSkin.hideWindow();
            bReset.hideWindow();
            bFMB.hideWindow();
            bSave.hideWindow();
            bUnlock.hideWindow();
            try
            {
                SectFile sectfile = Main.cur().currentMissionFile;
                briefSound = sectfile.get("MAIN", "briefSound");
                String s = Main.cur().currentMissionFile.fileName();
                String s1 = sectfile.get("MAIN", "MAP");
                if(!s.equals(curMissionName) || !s1.equals(curMapName) || curMissionNum != Main.cur().missionCounter || main.land2D == null)
                {
                    dialogClient.resized();
                    fillTextDescription();
                    fillMap();
                    Front.loadMission(sectfile);
                    curMissionName = s;
                    curMapName = s1;
                    curMissionNum = Main.cur().missionCounter;
                }
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
        Front.setMarkersChanged();
        wScrollDescription.resized();
        if(((GWindowScrollingDialogClient) (wScrollDescription)).vScroll.isVisible())
            ((GWindowScrollingDialogClient) (wScrollDescription)).vScroll.setPos(0.0F, true);
    }

    public void _leave()
    {
//        try
//        {
//            IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);
//            if(!bSingleMission)
//            {
//                if(inifile.get("LastSingleMission", "OwnSkinOn", false) != ((GWindowCheckBox) (sOwnSkinOn)).bChecked)
//                    inifile.set("LastSingleMission", "OwnSkinOn", ((GWindowCheckBox) (sOwnSkinOn)).bChecked);
//            }
//            if(inifile.get("LastSingleMission", "ParachuteOn", true) != ((GWindowCheckBox) (sParachuteOn)).bChecked)
//                inifile.set("LastSingleMission", "ParachuteOn", ((GWindowCheckBox) (sParachuteOn)).bChecked);
//            inifile.saveFile();
//        }
//        catch(Exception exception)
//        {
//            System.out.println(exception.getMessage());
//            exception.printStackTrace();
//        }
        client.hideWindow();
    }

    private void setPosCamera(float f, float f1)
    {
        float f2 = (float)((double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
        cameraMap2D.worldXOffset = f - f2 / 2.0F;
        float f3 = (float)((double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
        cameraMap2D.worldYOffset = f1 - f3 / 2.0F;
        clipCamera();
    }

    private void scaleCamera()
    {
        cameraMap2D.worldScale = (scale[curScale] * ((GWindow) (((GWindow) (renders)).root)).win.dx) / 1024F;
    }

    private void clipCamera()
    {
        if(cameraMap2D.worldXOffset < -Main3D.cur3D().land2D.worldOfsX())
            cameraMap2D.worldXOffset = -Main3D.cur3D().land2D.worldOfsX();
        float f = (float)((double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
        if(cameraMap2D.worldXOffset > Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - (double)f)
            cameraMap2D.worldXOffset = Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - (double)f;
        if(cameraMap2D.worldYOffset < -Main3D.cur3D().land2D.worldOfsY())
            cameraMap2D.worldYOffset = -Main3D.cur3D().land2D.worldOfsY();
        float f1 = (float)((double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
        if(cameraMap2D.worldYOffset > Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - (double)f1)
            cameraMap2D.worldYOffset = Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - (double)f1;
    }

    private void computeScales()
    {
        float f = (((GWindow) (renders)).win.dx * 1024F) / ((GWindow) (((GWindow) (renders)).root)).win.dx;
        float f1 = (((GWindow) (renders)).win.dy * 768F) / ((GWindow) (((GWindow) (renders)).root)).win.dy;
        int i = 0;
        float f2 = 0.064F;
        do
        {
            if(i >= scale.length)
                break;
            scale[i] = f2;
            float f3 = landDX * f2;
            if(f3 < f)
                break;
            float f5 = landDY * f2;
            if(f5 < f1)
                break;
            f2 /= 2.0F;
            i++;
        } while(true);
        scales = i;
        if(scales < scale.length)
        {
            float f4 = f / landDX;
            float f6 = f1 / landDY;
            scale[i] = f4;
            if(f6 > f4)
                scale[i] = f6;
            scales = i + 1;
        }
        curScale = scales - 1;
        curScaleDirect = -1;
    }

    private void drawGrid2D()
    {
        int i = gridStep();
        int j = (int)((cameraMap2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) / (double)i);
        int k = (int)((cameraMap2D.worldYOffset + Main3D.cur3D().land2D.worldOfsY()) / (double)i);
        double d = (double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
        double d1 = (double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale;
        int l = (int)(d / (double)i) + 2;
        int i1 = (int)(d1 / (double)i) + 2;
        float f = (float)(((double)(j * i) - cameraMap2D.worldXOffset - Main3D.cur3D().land2D.worldOfsX()) * cameraMap2D.worldScale + 0.5D);
        float f1 = (float)(((double)(k * i) - cameraMap2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY()) * cameraMap2D.worldScale + 0.5D);
        float f2 = (float)((double)(l * i) * cameraMap2D.worldScale);
        float f3 = (float)((double)(i1 * i) * cameraMap2D.worldScale);
        float f4 = (float)((double)i * cameraMap2D.worldScale);
        _gridCount = 0;
        Render.drawBeginLines(-1);
        for(int j1 = 0; j1 <= i1; j1++)
        {
            float f5 = f1 + (float)j1 * f4;
            char c = (j1 + k) % 10 == 0 ? '\300' : '\177';
            line2XYZ[0] = f;
            line2XYZ[1] = f5;
            line2XYZ[2] = 0.0F;
            line2XYZ[3] = f + f2;
            line2XYZ[4] = f5;
            line2XYZ[5] = 0.0F;
            Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c << 16 | c << 8 | c, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
            if(c == '\300')
                drawGridText(0, (int)f5, (k + j1) * i);
        }

        for(int k1 = 0; k1 <= l; k1++)
        {
            float f6 = f + (float)k1 * f4;
            char c1 = (k1 + j) % 10 == 0 ? '\300' : '\177';
            line2XYZ[0] = f6;
            line2XYZ[1] = f1;
            line2XYZ[2] = 0.0F;
            line2XYZ[3] = f6;
            line2XYZ[4] = f1 + f3;
            line2XYZ[5] = 0.0F;
            Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c1 << 16 | c1 << 8 | c1, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
            if(c1 == '\300')
                drawGridText((int)f6, 0, (j + k1) * i);
        }

        Render.drawEnd();
        drawGridText();
    }

    private int gridStep()
    {
        float f = cameraMap2D.right - cameraMap2D.left;
        float f1 = cameraMap2D.top - cameraMap2D.bottom;
        double d = f;
        if(f1 < f)
            d = f1;
        d /= cameraMap2D.worldScale;
        int i = 0x186a0;
        for(int j = 0; j < 5 && (double)(i * 3) > d; j++)
            i /= 10;

        return i;
    }

    private void drawGridText(int i, int j, int k)
    {
        if(i < 0 || j < 0 || k <= 0 || _gridCount == 20)
        {
            return;
        } else
        {
            _gridX[_gridCount] = i;
            _gridY[_gridCount] = j;
            _gridVal[_gridCount] = k;
            _gridCount++;
            return;
        }
    }

    private void drawGridText()
    {
        for(int i = 0; i < _gridCount; i++)
            gridFont.output(0xffc0c0c0, _gridX[i] + 2, _gridY[i] + 2, 0.0F, _gridVal[i] / 1000 + "." + (_gridVal[i] % 1000) / 100);

        _gridCount = 0;
    }

    protected void doRenderMap2D()
    {
    }

    protected void doMouseButton(int i, boolean flag, float f, float f1)
    {
        if(i == 2)
            bMPressed = flag;
        if(i == 1)
            bRPressed = flag;
        if(i == 0)
        {
            bLPressed = flag;
            renders.mouseCursor = bLPressed ? 7 : 3;
        }
    }

    protected void doMouseMove(float f, float f1)
    {
        if(bLPressed && ((GWindow) (renders)).mouseCursor == 7)
        {
            cameraMap2D.worldXOffset -= (double)((GWindow) (renders)).root.mouseStep.dx / cameraMap2D.worldScale;
            cameraMap2D.worldYOffset += (double)((GWindow) (renders)).root.mouseStep.dy / cameraMap2D.worldScale;
            clipCamera();
        }
    }

    protected void doMouseWheel(float f, float f1, float v)
    {
        f -= ((GWindow) (renders)).win.x;
        f1 -= ((GWindow) (renders)).win.y;
        if(f < 0.0F)
            f = ((GWindow) (renders)).win.x;
        if(f1 < 0.0F)
            f1 = ((GWindow) (renders)).win.y;
        float f2 = (float)(cameraMap2D.worldXOffset + (double)f / cameraMap2D.worldScale);
        float f3 = (float)(cameraMap2D.worldYOffset + (double)(((GWindow) (renders)).win.dy - f1 - 1.0F) / cameraMap2D.worldScale);
//        if(bPALZoomInUp)
//            v = -v;
        if(v > 0.0F && curScale < scales - 1)
            curScale++;
        if(v < 0.0F && curScale > 0)
            curScale--;
        scaleCamera();
        f2 = (float)((double)f2 - (double)(f - ((GWindow) (renders)).win.dx / 2.0F) / cameraMap2D.worldScale);
        f3 = (float)((double)f3 + (double)(f1 - ((GWindow) (renders)).win.dy / 2.0F) / cameraMap2D.worldScale);
        setPosCamera(f2, f3);
    }

    protected void doMouseRelMove(float f, float f1, float f2)
    {
        if((double)f2 < 0.001D && (double)f2 > -0.001D)
            return;
        if((double)f2 < 0.0D)
            curScaleDirect = 1;
        if((double)f2 > 0.0D)
            curScaleDirect = -1;
        float f3 = ((GWindow) (renders)).root.mousePos.x - ((GWindow) (renders)).win.x - ((GWindow) (renders)).parentWindow.win.x;
        float f4 = ((GWindow) (renders)).root.mousePos.y - ((GWindow) (renders)).win.y - ((GWindow) (renders)).parentWindow.win.y;
        float f5 = (float)(cameraMap2D.worldXOffset + (double)f3 / cameraMap2D.worldScale);
        float f6 = (float)(cameraMap2D.worldYOffset + (double)(((GWindow) (renders)).win.dy - f4 - 1.0F) / cameraMap2D.worldScale);
        curScale += curScaleDirect;
        if(curScaleDirect < 0)
        {
            if(curScale < 0)
                curScale = 0;
        } else
        if(curScale == scales)
            curScale = scales - 1;
        scaleCamera();
        f5 = (float)((double)f5 - (double)(f3 - ((GWindow) (renders)).win.dx / 2.0F) / cameraMap2D.worldScale);
        f6 = (float)((double)f6 + (double)(f4 - ((GWindow) (renders)).win.dy / 2.0F) / cameraMap2D.worldScale);
        setPosCamera(f5, f6);
    }

    protected void createRenderWindow(GWindow gwindow)
    {
        renders = new GUIRenders(gwindow, 0.0F, 0.0F, 1.0F, 1.0F, false) {

            public void mouseButton(int i, boolean flag, float f, float f1)
            {
                doMouseButton(i, flag, f, f1);
            }

            public void mouseMove(float f, float f1)
            {
                doMouseMove(f, f1);
            }

            public void mouseRelMove(float f, float f1, float f2)
            {
                doMouseRelMove(f, f1, f2);
            }

        }
;
        renders.mouseCursor = 3;
        renders.bNotify = true;
        cameraMap2D = new CameraOrtho2D();
        cameraMap2D.worldScale = scale[curScale];
        renderMap2D = new RenderMap2D(renders.renders, 1.0F);
        renderMap2D.setCamera(cameraMap2D);
        renderMap2D.setShow(true);
        LightEnvXY lightenvxy = new LightEnvXY();
        renderMap2D.setLightEnv(lightenvxy);
        lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
        Vector3f vector3f = new Vector3f(1.0F, -2F, -1F);
        vector3f.normalize();
        lightenvxy.sun().set(vector3f);
        gridFont = TTFont.font[1];
        if(World.cur().smallMapWPLabels)
        {
            waypointFont = TTFont.font[0];
            bigFontMultip = 1.0F;
        } else
        {
            waypointFont = TTFont.font[1];
            bigFontMultip = 2.0F;
        }
        emptyMat = Mat.New("icons/empty.mat");
        main = Main3D.cur3D();
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
            date = OriginalDate;
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

    protected void doUpdateMissionParams()
    {
        try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            sectfile.set("MAIN", "CloudType", wWeather.getSelected());
            String s = wCldHeight.getValue();
            if(s.charAt(s.length() - 1) == 'm')
                s = s.substring(0, s.length() - 1);
            sectfile.set("MAIN", "CloudHeight", s + ".0");
            int m = (int)((double)wTimeMins.getSelected() / 0.59999999999999998D);
            s = (m >= 10 ? "" : "0") + m;
            sectfile.set("MAIN", "TIME", wTimeHour.getSelected() + "." + s);
            sectfile.set("SEASON", "Day", "" + Integer.parseInt(wSetDate.getValue().substring(0,2)));
            sectfile.set("SEASON", "Month", "" + Integer.parseInt(wSetDate.getValue().substring(3,5)));
            sectfile.set("SEASON", "Year", "" + Integer.parseInt(wSetDate.getValue().substring(6,10)));
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    protected void doUpdateFlight()
    {
        try
        {
            FlightName = originalFlightName;
            SectFile sectfile = Main.cur().currentMissionFile;
            int j = sectfile.sectionIndex("Wing");
            if(j >= 0)
            {
                if(wSelectFlight.getSelected() > -1)
                    FlightName = sectfile.var(j, wSelectFlight.getSelected());
                sectfile.set("MAIN", "player", FlightName);
            }
            PlaneName = sectfile.get(FlightName, "Class", (String)null);
            int NoPlanes = sectfile.get(FlightName, "Planes", 1, 1, 4);
            wSelectNum.setSelected(0, true, false);
            for(int l = 0; l < 4; l++)
            {
                if(l < NoPlanes)
                    wSelectNum.posEnable[l] = true;
                else
                    wSelectNum.posEnable[l] = false;
            }
            if(FlightName.equalsIgnoreCase(originalFlightName))
                wSelectNum.setSelected(OriginalPlayerNum, true, false);
            if(FlightName != null && FlightName.length() > 2)
            {
                String FlightName1 = FlightName.substring(0, FlightName.length() - 1);
                String FlightName2 = FlightName1.substring(0, FlightName1.length() - 1);
                Regiment regiment = (Regiment)Actor.getByName(FlightName2);
                if(regiment.getArmy() == 1)
                {
                    wSelectFlight.setEditTextColor(colorRed);
                    wSelectPlane.setEditTextColor(colorRed);
                    wSelectNum.setEditTextColor(colorRed);
                    wSelectWeapons.setEditTextColor(colorRed);
                    wSelectSkin.setEditTextColor(colorRed);
                    sectfile.set("MAIN", "army", 1);
                } else
                {
                    wSelectFlight.setEditTextColor(colorBlue);
                    wSelectPlane.setEditTextColor(colorBlue);
                    wSelectNum.setEditTextColor(colorBlue);
                    wSelectWeapons.setEditTextColor(colorBlue);
                    wSelectSkin.setEditTextColor(colorBlue);
                    sectfile.set("MAIN", "army", 2);
                }
            }
            wSelectPlane.setSelected(0, false, false);
            for(int k = 0; k < aiPlane.size(); k++)
            {
                ItemAir plane = (ItemAir)aiPlane.get(k);
                if(!plane.className.equalsIgnoreCase(PlaneName))
                    continue;
                wSelectPlane.setSelected(k, true, false);
                break;
            }

            doUpdatePlane();
            doUpdateNum();
            doUpdateSkin();
            dialogClient.resized();
            fillMap();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    protected void doUpdatePlane()
    {
        try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            if(FlightName != null && wSelectPlane.getSelected() > -1)
            {
                ItemAir plane = (ItemAir)aiPlane.get(wSelectPlane.getSelected());
                sectfile.set(FlightName, "Class", plane.className);
                PlaneName = plane.className;
                String LastWeapon = sectfile.get(FlightName, "weapons", i18n("neta.Default"));
                if(!bSingleMission && !FlightName.equalsIgnoreCase(originalFlightName))
                {
                    wSelectNum.setEditTextColor(colorGray);
                    wSelectPlane.setEditTextColor(colorGray);
                    wSelectWeapons.setEditTextColor(colorGray);
                    wSelectSkin.setEditTextColor(colorGray);
                } else
                {
                    String FlightName1 = FlightName.substring(0, FlightName.length() - 1);
                    String FlightName2 = FlightName1.substring(0, FlightName1.length() - 1);
                    Regiment regiment = (Regiment)Actor.getByName(FlightName2);
                    if(regiment.getArmy() == 1)
                        wSelectPlane.setEditTextColor(colorRed);
                    else
                        wSelectPlane.setEditTextColor(colorBlue);
                }
                wSelectNum.setEnable(true);
                wSelectSkin.setEnable(true);
                wSelectWeapons.setEnable(true);
                fillComboWeapon(wSelectWeapons, plane, 0);
                fillComboSkin(wSelectSkin, plane);
                if(bSingleMission)
                {
                    if(FlightName.equalsIgnoreCase(originalFlightName))
                    {
                        wSelectFlight.list.set(wSelectFlight.getSelected(), "> " + FlightName + " (" + plane.name + ")");
                        wSelectFlight.setValue("> " + FlightName + " (" + plane.name + ")");
                    } else
                    {
                        wSelectFlight.list.set(wSelectFlight.getSelected(), FlightName + " (" + plane.name + ")");
                        wSelectFlight.setValue(FlightName + " (" + plane.name + ")");
                    }
                } else
                if(FlightName.equalsIgnoreCase(originalFlightName))
                {
                    wSelectFlight.list.set(wSelectFlight.getSelected(), "> " + FlightName + " (" + plane.name + ")");
                    wSelectFlight.setValue("> " + FlightName + " (" + plane.name + ")");
                } else
                {
                    wSelectFlight.list.set(wSelectFlight.getSelected(), FlightName + " (" + plane.name + ")");
                    wSelectFlight.setValue(FlightName + " (" + plane.name + ")");
                }
                LastWeapon = sectfile.get(FlightName, "weapons", i18n("neta.Default"));
                wSelectWeapons.setSelected(0, false, false);
                for(int i = 0; i < wSelectWeapons.size(); i++)
                {
                    String s1 = Aircraft.getWeaponsRegistered(plane.clazz)[i];
                    if(!s1.equalsIgnoreCase(LastWeapon))
                        continue;
                    wSelectWeapons.setSelected(i, true, false);
                    break;
                }

                wSelectWeapons.setValue((String)wSelectWeapons.list.get(wSelectWeapons.getSelected()));
                doUpdateWeapons();
                String CurrentSkin = sectfile.get(FlightName, "skin" + wSelectNum.getSelected(), (String)null);
                if(CurrentSkin == null)
                    CurrentSkin = i18n("neta.Default");
                wSelectSkin.setSelected(0, false, false);
                for(int i = 0; i < wSelectSkin.size(); i++)
                {
                    if(!CurrentSkin.equalsIgnoreCase((String)wSelectSkin.list.get(i)))
                        continue;
                    wSelectSkin.setSelected(i, true, false);
                    break;
                }
                wSelectSkin.setValue((String)wSelectSkin.list.get(wSelectSkin.getSelected()));
                sectfile.set(FlightName, "skin" + wSelectNum.getSelected(), wSelectSkin.getValue());
            } else
            {
                wSelectNum.setEnable(false);
                wSelectSkin.setEnable(false);
                wSelectWeapons.setEnable(false);
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    protected void doUpdateNum()
    {
        try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            sectfile.set("MAIN", "playerNum", wSelectNum.getSelected());
            if(sectfile.get("MAIN", "player", (String)null).equalsIgnoreCase(originalFlightName))
                OriginalPlayerNum = wSelectNum.getSelected();
            String CurrentSkin = sectfile.get(FlightName, "skin" + wSelectNum.getSelected(), (String)null);
            if(CurrentSkin == null)
                CurrentSkin = i18n("neta.Default");
            wSelectSkin.setSelected(0, false, false);
            for(int i = 0; i < wSelectSkin.size(); i++)
            {
                if(!CurrentSkin.equalsIgnoreCase((String)wSelectSkin.list.get(i)))
                    continue;
                wSelectSkin.setSelected(i, true, false);
                break;
            }
            wSelectSkin.setValue((String)wSelectSkin.list.get(wSelectSkin.getSelected()));
            sectfile.set(FlightName, "skin" + wSelectNum.getSelected(), wSelectSkin.getValue());
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    protected void doUpdateWeapons()
    {
        try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            if(FlightName != null)
            {
                ItemAir plane = (ItemAir)aiPlane.get(wSelectPlane.getSelected());
                String CurrentWeapon = Aircraft.getWeaponsRegistered(plane.clazz)[wSelectWeapons.getSelected()];
                sectfile.set(FlightName, "weapons", CurrentWeapon);
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    protected void doUpdateSkin()
    {
        try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            if(FlightName != null)
            {
//                int NoPlanes = sectfile.get(FlightName, "Planes", 1, 1, 4);
//                for(int i = 0; i < NoPlanes; i++)
//                    sectfile.set(FlightName, "skin" + i, (String)wSelectSkin.list.get(wSelectSkin.getSelected()));
                World.cur().userCfg.netNumberOn = sectfile.get(FlightName, "numberOn" + wSelectNum.getSelected(), 1, 0, 1) == 1;
                sectfile.set(FlightName, "skin" + wSelectNum.getSelected(), (String)wSelectSkin.list.get(wSelectSkin.getSelected()));
                wSelectSkin.setValue((String)wSelectSkin.list.get(wSelectSkin.getSelected()) + (sectfile.get(FlightName, "numberOn" + wSelectNum.getSelected(), 1, 0, 1) == 1 ? " + " + i18n("Markings") : ""));
                String tmpPlaneName = sectfile.get(FlightName, "Class", (String)null);
                if(tmpPlaneName != null)
                {
                    Class tmpClassName = ObjIO.classForName(tmpPlaneName);
                    tmpPlaneName = Property.stringValue(tmpClassName, "keyName", (String)null);
                    if(tmpPlaneName != null)
                        World.cur().userCfg.setSkin(tmpPlaneName, (String)wSelectSkin.list.get(wSelectSkin.getSelected()));
                }
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    protected void fillSelectFlight()
    {
        int playerSlot = 0;
        PlaneName = "";
        wSelectFlight.clear();
        try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            FlightName = sectfile.get("MAIN", "player", (String)null);
            if(FlightName != null && FlightName.length() > 2)
            {
                String FlightName1 = FlightName.substring(0, FlightName.length() - 1);
                String FlightName2 = FlightName1.substring(0, FlightName1.length() - 1);
                Regiment regiment = (Regiment)Actor.getByName(FlightName2);
                if(regiment.getArmy() == 1)
                {
                    wSelectFlight.setEditTextColor(colorRed);
                    wSelectPlane.setEditTextColor(colorRed);
                    wSelectNum.setEditTextColor(colorRed);
                    wSelectWeapons.setEditTextColor(colorRed);
                    wSelectSkin.setEditTextColor(colorRed);
                } else
                {
                    wSelectFlight.setEditTextColor(colorBlue);
                    wSelectPlane.setEditTextColor(colorBlue);
                    wSelectNum.setEditTextColor(colorBlue);
                    wSelectWeapons.setEditTextColor(colorBlue);
                    wSelectSkin.setEditTextColor(colorBlue);
                }
            }
            int i = sectfile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
            World.cur().setWeaponsConstant(i == 1);
            int j = sectfile.sectionIndex("Wing");
            if(j >= 0)
            {
                int k = sectfile.vars(j);
//                if(wSelectFlight.posEnable == null || wSelectFlight.posEnable.length < k)
//                    wSelectFlight.posEnable = new boolean[k];
                for(int i1 = 0; i1 < k; i1++)
                {

                    String s = sectfile.var(j, i1);
//                    boolean StartsLater = sectfile.get(s, "StartTime", 0) > 0;
                    String s1 = sectfile.get(s, "Class", (String)null);
                    Class planeClass = null;
                    try
                    {
                        planeClass = ObjIO.classForName(s1);
                    }
                    catch(Exception exception)
                    {
                        ItemAir plane = (ItemAir)aiPlane.get(getFirstNonPlaceholder(0));
                        String s2 = plane.className;
                        sectfile.set(s, "Class", s2);
                        try
                        {
                            planeClass = ObjIO.classForName(s2);
                            ShowMessage(I18N.gui("warning.Warning"), "The air class '" + s1 + "' does not exist in your game." + "\n\nAircraft '" + s2 + "' will be used instead of the original plane.");
                        }
                        catch(Exception exc) { }
                    }
                    if(s.equalsIgnoreCase(originalFlightName))
                    {
                        if(bSingleMission)
                            wSelectFlight.add("> " + s + " (" + Property.stringValue(planeClass, "keyName", null) + ")");
                        else
                            wSelectFlight.add("> " + s + " (" + Property.stringValue(planeClass, "keyName", null) + ")");
                    } else
                    {
                        wSelectFlight.add(s + " (" + Property.stringValue(planeClass, "keyName", null) + ")");
                    }
                    if(s.equalsIgnoreCase(FlightName))
                    {
                        playerSlot = i1;
                        PlaneName = s1;
                    }
//                    wSelectFlight.posEnable[i1] = !StartsLater;
//                    wSelectFlight.posEnable[i1] = s.equalsIgnoreCase(originalFlightName) ? true : !StartsLater;
//                    wSelectFlight.posEnable[i1] = true;
                }
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        wSelectFlight.setEditable(false);
        wSelectPlane.setEditable(false);
        wSelectWeapons.setEditable(false);
        wSelectSkin.setEditable(false);
        wSelectFlight.setSelected(-1, false, false);
//        if(wSelectFlight.posEnable[playerSlot])
//        {
            wSelectFlight.setSelected(playerSlot, true, false);
//        } else
//        {
//            for(int k = 0; k < wSelectFlight.size(); k++)
//            {
//                if(!wSelectFlight.posEnable[k])
//                    continue;
//                wSelectFlight.setSelected(k, true, false);
//                break;
//            }
//        }
        doUpdateFlight();
    }

    protected void fillMap()
        throws Exception
    {
        SectFile sectfile = Main.cur().currentMissionFile;
        String s = sectfile.get("MAIN", "MAP");
        if(s == null)
            throw new Exception("No MAP in mission file ");
        else
        {
            MissionMap = s;
            SectFile sectfile2 = new SectFile("maps/all.ini", 0);
            int i2 = sectfile2.sectionIndex("all");
            int j2 = sectfile2.vars(i2);
            for(int k2 = 0; k2 < j2; k2++)
            {
                if(MissionMap.equalsIgnoreCase(sectfile2.value(i2, k2)))
                {
                    MissionMap = I18N.map(sectfile2.var(i2, k2));
                    break;
                }
            }
        }
        SectFile sectfile1 = new SectFile("maps/" + s);
        String s1 = sectfile1.get("MAP", "TypeMap", (String)null);
        if(s1 == null)
            throw new Exception("Bad MAP description in mission file ");
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
            throw new Exception("Bad MAP description in mission file ");
        landDX = (float)ai[0] * 200F;
        landDY = (float)ai[1] * 200F;
        if(main.land2D != null)
        {
            if(!main.land2D.isDestroyed())
                main.land2D.destroy();
            main.land2D = null;
        }
        s1 = null;
        int i = sectfile1.sectionIndex("MAP2D");
        if(i >= 0)
        {
            int j = sectfile1.vars(i);
            if(j > 0)
            {
                main.land2D = new Land2Dn(s, landDX, landDY);
                landDX = (float)main.land2D.mapSizeX();
                landDY = (float)main.land2D.mapSizeY();
            }
        }
        if(main.land2DText == null)
            main.land2DText = new Land2DText();
        else
            main.land2DText.clear();
        int k = sectfile1.sectionIndex("text");
        if(k >= 0 && sectfile1.vars(k) > 0)
        {
            String s2 = sectfile1.var(k, 0);
            main.land2DText.load(HomePath.concatNames("maps/" + s, s2));
        }
        computeScales();
        scaleCamera();
        setPosCamera(landDX / 2.0F, landDY / 2.0F);
    }

    protected void prepareTextDescription(int i)
    {
        if(textDescription == null)
            return;
        if(textArmyDescription == null || textArmyDescription.length != i)
            textArmyDescription = new String[i];
        for(int j = 0; j < i; j++)
        {
            textArmyDescription[j] = null;
            prepareTextDescriptionArmy(j);
        }

    }

    private void prepareTextDescriptionArmy(int i)
    {
        String s = (Army.name(i) + ">").toUpperCase();
        int j = 0;
        int k = textDescription.length();
        StringBuffer stringbuffer = new StringBuffer();
        do
        {
            if(j >= k)
                break;
            int l = textDescription.indexOf("<ARMY", j);
            if(l >= j)
            {
                if(l > j)
                    subString(stringbuffer, textDescription, j, l);
                int i1 = textDescription.indexOf("</ARMY>", l);
                if(i1 == -1)
                    i1 = k;
                for(l += "<ARMY".length(); l < k && Character.isSpaceChar(textDescription.charAt(l)); l++);
                if(l == k)
                {
                    j = k;
                    break;
                }
                if(textDescription.startsWith(s, l))
                {
                    l += s.length();
                    if(l < i1 && textDescription.charAt(l) == '\n')
                        l++;
                    subString(stringbuffer, textDescription, l, i1);
                }
                j = i1 + "</ARMY>".length();
                if(j < k && textDescription.charAt(j) == '\n')
                    j++;
            } else
            {
                subString(stringbuffer, textDescription, j, k);
                j = k;
            }
        } while(true);
        textArmyDescription[i] = new String(stringbuffer);
    }

    private void subString(StringBuffer stringbuffer, String s, int i, int j)
    {
        while(i < j) 
            stringbuffer.append(s.charAt(i++));
    }

    private void setMissionDate(String s)
    {
        String s1 = s;
        if(Main.cur().currentMissionFile == null)
            return;
        if(Main.cur().currentMissionFile.sectionExist("SEASON"))
            return;
        try
        {
            s = s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."));
            int i = Integer.parseInt(s);
            int j = i / 10000 + 1940;
            i %= 10000;
            int k = i / 100;
            i %= 100;
            int l = i;
            Main.cur().currentMissionFile.sectionAdd("SEASON");
            int i1 = Main.cur().currentMissionFile.sectionIndex("SEASON");
            Main.cur().currentMissionFile.lineAdd(i1, "Year", "" + j);
            Main.cur().currentMissionFile.lineAdd(i1, "Month", "" + k);
            Main.cur().currentMissionFile.lineAdd(i1, "Day", "" + l);
            Main.cur().currentMissionFile.saveFile(s1);
        }
        catch(Exception exception) { }
    }

    protected void fillTextDescription()
    {
        try
        {
            if(bNotMultiPlay)
            {
                String s = Main.cur().currentMissionFile.fileName();
                if(Main.cur().campaign != null && Main.cur().campaign.isDGen())
                    setMissionDate(s);
                int i = s.length() - 1;
                do
                {
                    if(i <= 0)
                        break;
                    char c = s.charAt(i);
                    if(c == '\\' || c == '/')
                        break;
                    if(c == '.')
                    {
                        s = s.substring(0, i);
                        break;
                    }
                    i--;
                } while(true);
                if(bSingleMission)
                {
                    textName = null;
                    textShort = null;
                    textLong = null;
                    textDescription = null;
                    BufferedReader bufferedreader = null;
                    try
                    {
                        try
                        {
                            bufferedreader = new BufferedReader(new SFSReader(textFileName(s, true)));
                        }
                        catch(Exception exception)
                        {
                            bufferedreader = new BufferedReader(new SFSReader(textFileName(s, false)));
                        }
                        do
                        {
                            String st1 = bufferedreader.readLine();
                            if(st1 == null)
                                break;
                            int j = st1.length();
                            if(j != 0)
                            {
                                SharedTokenizer.set(st1);
                                String st2 = SharedTokenizer.next();
                                if(st2 != null)
                                    if("Name".compareToIgnoreCase(st2) == 0)
                                    {
                                        String st3 = SharedTokenizer.getGap();
                                        if(st3 != null)
                                            textName = UnicodeTo8bit.load(st3, false);
                                    } else
                                    if("Short".compareToIgnoreCase(st2) == 0)
                                    {
                                        String st4 = SharedTokenizer.getGap();
                                        if(st4 != null)
                                            textShort = UnicodeTo8bit.load(st4, false);
                                    } else
                                    if("Description".compareToIgnoreCase(st2) == 0)
                                    {
                                        String st5 = SharedTokenizer.getGap();
                                        if(st5 != null)
                                        {
                                            textLong = UnicodeTo8bit.load(st5, false);
                                            textDescription = (DescriptString == null ? "\n" : DescriptString + "\n\n") + textLong;
                                        }
                                    }
                            }
                        } while(true);
                    }
                    catch(Exception exception) { }
                    if(bufferedreader != null)
                        try
                        {
                            bufferedreader.close();
                        }
                        catch(Exception exception1) { }
                } else
                {
                    ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale);
                    textDescription = (DescriptString == null ? "\n" : DescriptString + "\n\n") + resourcebundle.getString("Description");
                }
            } else
            {
                String s = Main.cur().currentMissionFile.fileName();
                for(int i = s.length() - 1; i > 0; i--)
                {
                    char c = s.charAt(i);
                    if(c == '\\' || c == '/')
                        break;
                    if(c != '.')
                        continue;
                    s = s.substring(0, i);
                    break;
                }

                ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale);
                textDescription = resourcebundle.getString("Description");
            }
        }
        catch(Exception exception)
        {
            textDescription = null;
            textArmyDescription = null;
        }
    }

    protected String textDescription()
    {
        return textDescription;
    }

    protected Descript createDescript(GWindow gwindow)
    {
        return (Descript)gwindow.create(new Descript());
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

//    private void dumpFullPlaneList()
//    {
//        SectFile sectfile = new SectFile("com/maddox/il2/objects/air.ini");
//        SectFile sectfile1 = new SectFile("./Missions/Quick/FullPlaneList.dump", 1);
//        sectfile1.sectionAdd("AIR");
//        int i = sectfile.vars(sectfile.sectionIndex("AIR"));
//        for(int j = 0; j < i; j++)
//            sectfile1.varAdd(0, sectfile.var(0, j));
//    }

    public void fillArrayPlanes()
    {
//        playerPlane.clear();
//        playerPlaneC.clear();
        aiPlane.clear();
//        aiPlaneC.clear();
//        pl = Config.cur.ini.get("QMB", "PlaneList", 0, 0, 3);
//        if(pl > 1)
//            pl = 0;
//        boolean flag = pl == 1;
        String s = "com/maddox/il2/objects/air.ini";
        SectFile sectfile = new SectFile(s, 0);
        SectFile sectfile1 = new SectFile("com/maddox/il2/objects/air.ini");
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
//                if(itemair.bEnablePlayer)
//                {
//                    if(!flag || (flag && !itemair.className.equalsIgnoreCase(PlaceholderLabel)))
//                    {
//                        playerPlane.add(itemair);
//                        if(AirportCarrier.isPlaneContainsArrestor(class1))
//                            playerPlaneC.add(itemair);
//                    }
//                }
//                if(!flag || (flag && !itemair.className.equalsIgnoreCase(PlaceholderLabel)))
//                {
                    aiPlane.add(itemair);
//                    if(AirportCarrier.isPlaneContainsArrestor(class1))
//                        aiPlaneC.add(itemair);
//                }
            }

        }

//        if(flag)
//        {
//            Collections.sort(playerPlane, new byI18N_name());
//            Collections.sort(playerPlaneC, new byI18N_name());
//            Collections.sort(aiPlane, new byI18N_name());
//            Collections.sort(aiPlaneC, new byI18N_name());
//        }
    }

    public void fillComboPlane(GWindowComboControl gwindowcombocontrol)
    {
        gwindowcombocontrol.clear();
        ArrayList arraylist = null;
//        if(bPlaneArrestor)
//            arraylist = flag ? playerPlaneC : aiPlaneC;
//        else
//            arraylist = flag ? playerPlane : aiPlane;
        arraylist = aiPlane;
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            ItemAir itemair = (ItemAir)arraylist.get(j);
            gwindowcombocontrol.add((itemair.bEnablePlayer ? "" : "(AI) ") + I18N.plane(itemair.name));
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

    private void fillComboSkin(GWindowComboControl gwindowcombocontrol, ItemAir itemair)
    {
        gwindowcombocontrol.clear();
        gwindowcombocontrol.add(i18n("neta.Default"));
        try
        {
            String s = Main.cur().netFileServerSkin.primaryPath();
            String s1 = validateFileName(itemair.name);
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
                                gwindowcombocontrol.add(s2);
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
        gwindowcombocontrol.setSelected(0, true, false);
    }

    protected void doNext()
    {
    }

    protected void doDiff()
    {
    }

    protected void doBack()
    {
    }

    protected void doLoodout()
    {
    }

    protected void clientRender()
    {
    }

    protected void clientSetPosSize()
    {
    }

    protected void clientInit(GWindowRoot gwindowroot1)
    {
    }

    protected String infoMenuInfo()
    {
        return "????????";
    }

    protected void init(GWindowRoot gwindowroot)
    {
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = infoMenuInfo();
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        createRenderWindow(dialogClient);
        dialogClient.create(wScrollDescription = new ScrollDescript());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        com.maddox.gwindow.GTexture gtexture1 = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons;
        bPrev = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bDifficulty = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bLoodout = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bNext = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        bReset = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 144F, 48F, 48F));
//        sOwnSkinOn = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
//        sParachuteOn = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        bFMB = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bSave = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bUnlock = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 144F, 48F, 48F));
        bDate = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture1, 192F, 208F, 32F, 32F));
        ComboButtonWidth = gwindowroot.lookAndFeel().getHScrollBarW();
        wSelectFlight = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wSelectNum = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wSelectNum.add("1. Flight Leader");
        wSelectNum.add("2. Flight Wingman");
        wSelectNum.add("3. Element Leader");
        wSelectNum.add("4. Element Wingman");
        wSelectNum.setEditable(false);
        wSelectNum.posEnable = new boolean[4];
        wSelectNum.posEnable[0] = true;
        wSelectNum.posEnable[1] = false;
        wSelectNum.posEnable[2] = false;
        wSelectNum.posEnable[3] = false;
        wSelectPlane = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wSelectWeapons = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
//        wSelectSkin = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 10.5F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wSelectSkin = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 10.5F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()) {
            public void setSelected(int i, boolean setValue, boolean notify)
            {
                super.setSelected(i, setValue, notify);
                if(setValue && notify)
                    doUpdateSkin();
            }
        }
);
        wWeather = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCldHeight = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCldHeight.setEditable(false);
        wSetDate = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 2.0F, 2.0F, 20F, 2.0F, null));
        wSetDate.align = 1;
        wSetDate.bDelayedNotify = true;
        wSetDate.bEnableDoubleClick[0] = false;
        wTimeHour = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wTimeMins = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        for(int j = 0; j < 24; j++)
            wTimeHour.add(j >= 10 ? "" + j + "h" : "0" + j + "h");

        wTimeHour.setEditable(false);
        wTimeHour.editBox.align = wTimeHour.align = 1;
        wTimeHour.setSelected(0, true, false);
        for(int j = 0; j < 60; j++)
            wTimeMins.add(j >= 10 ? "" + j + "m" : "0" + j + "m");

        wTimeMins.setEditable(false);
        wTimeMins.editBox.align = wTimeMins.align = 1;
        wTimeMins.setSelected(0, true, false);
        wWeather.add(I18N.gui("quick.CLE"));
        wWeather.add(I18N.gui("quick.GOO"));
        wWeather.add(I18N.gui("quick.HAZ"));
        wWeather.add(I18N.gui("quick.POO"));
        wWeather.add(I18N.gui("quick.BLI"));
        wWeather.add(I18N.gui("quick.RAI"));
        wWeather.add(I18N.gui("quick.THU"));
        wWeather.setEditable(false);
//        sOwnSkinOn.hideWindow();
//        sParachuteOn.hideWindow();
        wWeather.hideWindow();
        wCldHeight.hideWindow();
        wSetDate.hideWindow();
        bDate.hideWindow();
        wTimeHour.hideWindow();
        wTimeMins.hideWindow();
        wSelectFlight.hideWindow();
        wSelectPlane.hideWindow();
        wSelectNum.hideWindow();
        wSelectWeapons.hideWindow();
        wSelectSkin.hideWindow();
        bReset.hideWindow();
        bFMB.hideWindow();
        bSave.hideWindow();
        bUnlock.hideWindow();
        wSelectPlane.listVisibleLines = 24;
        wSelectFlight.listVisibleLines = 12;
        wSelectNum.listVisibleLines = 4;
        wSelectWeapons.listVisibleLines = 18;
        wSelectSkin.listVisibleLines = 18;
//        russianMixedRules = "< ',' < '.' < '-' <\u0430,\u0410< a,A <\u0431,\u0411< b,B <\u0432,\u0412< v,V <\u0433,\u0413< g,G <\u0434,\u0414< d,D <\u0435,\u0415 < \u0451,\u0401 < \u0436,\u0416 < \u0437,\u0417< z,Z <\u0438,\u0418< i,I <\u0439,\u0419< j,J <\u043A,\u041A< k,K <\u043B,\u041B< l,L <\u043C,\u041C< m,M <\u043D,\u041D< n,N <\u043E,\u041E< o,O <\u043F,\u041F< p,P <\u0440,\u0420< r,R <\u0441,\u0421< s,S <\u0442,\u0422< t,T <\u0443,\u0423< u,U <\u0444,\u0424< f,F <\u0445,\u0425< h,H <\u0446,\u0426< c,C <\u0447,\u0427 < \u0448,\u0428 < \u0449,\u0429 < \u044A,\u042A < \u044B,\u042B< i,I <\u044C,\u042C < \u044D,\u042D< e,E <\u044E,\u042E < \u044F,\u042F< q,Q < x,X < y,Y";
//        try
//        {
//            collator = new RuleBasedCollator(russianMixedRules);
//        }
//        catch(Exception exception)
//        {
//            exception.printStackTrace();
//        }
//        if(Config.cur.ini.get("QMB", "DumpPlaneList", 0, 0, 1) > 0)
//            dumpFullPlaneList();
//        playerPlane = new ArrayList();
        aiPlane = new ArrayList();
//        playerPlaneC = new ArrayList();
//        aiPlaneC = new ArrayList();
//        bPlaneArrestor = false;
        clientInit(gwindowroot);
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIBriefingGeneric(int i)
    {
        super(i);
//        LastNoseArt = null;
        OriginalArmy = 1;
        originalFlightName = null;
        selectedFlightName = null;
        OriginalPlaneName = null;
        OriginalPlayerNum = 0;
        bNotMultiPlay = false;
        bSingleMission = false;
        bCampaignChanged = true;
        bMissionUnlocked = false;
        LastMissionFile = "Missions/Single/LastMission.mis";
//        HeaderDescript = "*** Original Mission brief for Flight ";
        PlaceholderLabel = "air.Placeholder";
        bMPressed = false;
//        bPALZoomInUp = Config.cur.ini.get("Mods", "PALZoomInUp", false);
//        MPVer = "";
        curMissionNum = -1;
        briefSound = null;
        bigFontMultip = 1.0F;
        scales = scale.length;
        curScale = scales - 1;
        curScaleDirect = -1;
        line2XYZ = new float[6];
        _gridX = new int[20];
        _gridY = new int[20];
        _gridVal = new int[20];
        bLPressed = false;
        bRPressed = false;
        bMPressed = false;
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public ScrollDescript wScrollDescription;
    public Descript wDescript;
    public GUIButton bLoodout;
    public GUIButton bDifficulty;
    public GUIButton bPrev;
    public GUIButton bNext;
    public String textName;
    public String textShort;
    public String textLong;
    public String textDescription;
    public String textArmyDescription[];
    public String curMissionName;
    public int curMissionNum;
    public String curMapName;
    protected String briefSound;
    protected Main3D main;
    protected GUIRenders renders;
    protected RenderMap2D renderMap2D;
    protected CameraOrtho2D cameraMap2D;
    protected TTFont gridFont;
    protected TTFont waypointFont;
    protected float bigFontMultip;
    protected Mat emptyMat;
    protected float scale[] = {
        0.064F, 0.032F, 0.016F, 0.008F, 0.004F, 0.002F, 0.001F, 0.0005F, 0.00025F
    };
    protected int scales;
    protected int curScale;
    protected int curScaleDirect;
    protected float landDX;
    protected float landDY;
    private float line2XYZ[];
    private int _gridCount;
    private int _gridX[];
    private int _gridY[];
    private int _gridVal[];
    protected boolean bLPressed;
    protected boolean bRPressed;
    public GUIButton bReset;
    public GUIButton bFMB;
    public GUIButton bSave;
    public GUIButton bUnlock;
    public GWindowComboControl wSelectPlane;
    public GWindowComboControl wSelectWeapons;
    public GWindowComboControl wSelectSkin;
//    private ArrayList playerPlane;
    private ArrayList aiPlane;
//    private ArrayList playerPlaneC;
//    private ArrayList aiPlaneC;
//    private boolean bPlaneArrestor;
    public String FlightName;
    public String PlaneName;
//    public String LastPlaneSkin;
//    public String LastSkin;
//    public String LastNoseArt;
    public int OriginalArmy;
    public String originalFlightName;
    public String selectedFlightName;
    public String OriginalPlaneName;
    public int OriginalPlayerNum;
    public String OriginalDate;
    public String MissionMap;
    private boolean bNotMultiPlay;
    private boolean bSingleMission;
    private boolean bCampaignChanged;
    private boolean bMissionUnlocked;
//    public GUISwitchBox3 sOwnSkinOn;
//    public GUISwitchBox3 sParachuteOn;
    public GWindowComboControl wSelectFlight;
    public GWindowComboControl wSelectNum;
    private String LastMissionFile;
//    private String HeaderDescript;
    private String PlaceholderLabel;
    public String OriginalFileName;
    protected String missionFileName;
    protected String lastOpenFile;
    boolean bMPressed;
//    int lastGameState;
    public String DescriptString;
    private int bUseColor;
    private int colorRed;
    private int colorBlue;
    private int colorGray;
//    private boolean bPALZoomInUp;
//    private String MPVer;
//    private static int pl;
//    private String russianMixedRules;
//    private RuleBasedCollator collator;
    public GWindowComboControl wWeather;
    public GWindowComboControl wCldHeight;
    public GWindowEditControl wSetDate;
    public GUIButton bDate;
    public GWindowComboControl wTimeHour;
    public GWindowComboControl wTimeMins;
    protected float ComboButtonWidth;
}
