package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.rts.*;
import com.maddox.il2.ai.DifficultySettings;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.*;
import com.maddox.rts.IniFile;
import com.maddox.util.UnicodeTo8bit;
//import com.maddox.rts.SectFile;
//import com.maddox.util.NumberTokenizer;
//import com.maddox.util.UnicodeTo8bit;
import java.io.*;
import java.util.*;

public class GUIDifficulty extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bExit)
            {
                Main.stateStack().pop();
                return true;
            }
            if(gwindow == bFlightModels)
            {
                bPageFlightModel = true;
                bPageWeapons = false;
                bPageViews = false;
                bPageIcons = false;
//                bPageMisc = false;
                bPageGameplay = false;
                bPageMods = false;
                showHide();
                return true;
            }
            if(gwindow == bWeapons)
            {
                bPageFlightModel = false;
                bPageWeapons = true;
                bPageViews = false;
                bPageIcons = false;
//                bPageMisc = false;
                bPageGameplay = false;
                bPageMods = false;
                showHide();
                return true;
            }
            if(gwindow == bViews)
            {
                bPageFlightModel = false;
                bPageWeapons = false;
                bPageViews = true;
                bPageIcons = false;
//                bPageMisc = false;
                bPageGameplay = false;
                bPageMods = false;
                showHide();
                return true;
            }
            if(gwindow == bIcons)
            {
                bPageFlightModel = false;
                bPageWeapons = false;
                bPageViews = false;
                bPageIcons = true;
//                bPageMisc = false;
                bPageGameplay = false;
                bPageMods = false;
                showHide();
                return true;
            }
            if(gwindow == bGameplay)
            {
                bPageFlightModel = false;
                bPageWeapons = false;
                bPageViews = false;
                bPageIcons = false;
//                bPageMisc = false;
                bPageGameplay = true;
                bPageMods = false;
                showHide();
                return true;
            }
            if(gwindow == bMods)
            {
                bPageFlightModel = false;
                bPageWeapons = false;
                bPageViews = false;
                bPageIcons = false;
//                bPageMisc = false;
                bPageGameplay = false;
                bPageMods = true;
                showHide();
                return true;
            }
            if(gwindow == bEasy)
            {
                settings().setEasy();
                reset();
                return true;
            }
            if(gwindow == bNormal)
            {
                settings().setNormal();
                reset();
                return true;
            }
            if(gwindow == bHard)
            {
                settings().setRealistic();
                reset();
                return true;
            }
            if(gwindow == bCustomLoad)
            {
//                settings().set(Config.cur.ini.get("game", "CustomDiff", 1932181828863L));
                IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 0);
                settings().set(inifile.get("CustomSettings", "Difficulty", 1932181828863L));
                reset();
                return true;
            }
            if(gwindow == bCustomSave)
            {
//                DifficultySettings difficultysettings = settings();
//                Config.cur.ini.set("game", "CustomDiff", difficultysettings.get());
                changed();
//                Config.cur.ini.set("game", "CustomDiff", settings().get());
                IniFile inifile1 = new IniFile(World.cur().userCfg.iniFileName(), 1);
                inifile1.set("CustomSettings", "Difficulty", settings().get());
                inifile1.saveFile();
                new GWindowMessageBox(((GWindowManager) (Main3D.cur3D().guiManager)).root, 25F, true, "Difficulty", "Custom difficulty settings saved.", 3, 0.0F);
                return true;
            }
            if(gwindow == sNo_Map_Icons)
            {
                sNo_Fog_Of_War_Icons.setEnable(sNo_Map_Icons.isChecked());
                if(!sNo_Map_Icons.isChecked())
                    sNo_Fog_Of_War_Icons.setChecked(true, false);
            } else
            if(gwindow == sNo_Outside_Views)
                enableAndDisableViewSwitches();
            else
            if(gwindow == sSharedKills)
                enableAndDisableSharedKillSwitches();
            return super.notify(gwindow, i, j);
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(360F), y1024(32F), 2.0F, y1024(580F));
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(640F), x1024(768F), 2.0F);
// Mods
            if(!isMultiplay())
                GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(319F), x1024(300F), 2.0F);
// Custom
            if(bEnable && !bPageMods)
                GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(500F), x1024(300F), 2.0F);
            setCanvasFont(0);
//            setCanvasColor(GColor.Black);
//            DifficultySettings difficultysettings = settings();
//            draw(x1024(96F), y1024(600F), x1024(224F), y1024(48F), 0, "Diff. Code: " + " " + difficultysettings.get());
            setCanvasColor(GColor.Gray);
            draw(x1024(96F), y1024(657F), x1024(224F), y1024(48F), 0, i18n("misc.Apply_Back"));
            draw(x1024(96F), y1024(28F), x1024(272F), y1024(48F), 0, i18n("diff.FlightModels"));
            draw(x1024(96F), y1024(84F), x1024(272F), y1024(48F), 0, i18n("diff.Weapons"));
            draw(x1024(96F), y1024(140F), x1024(272F), y1024(48F), 0, i18n("diff.Views"));
            draw(x1024(96F), y1024(196F), x1024(272F), y1024(48F), 0, i18n("diff.Icons"));
            draw(x1024(96F), y1024(252F), x1024(272F), y1024(48F), 0, i18n("diff.Misc"));
            if(!isMultiplay())
                draw(x1024(96F), y1024(336F), x1024(272F), y1024(48F), 0, i18n("Mods"));
            if(bEnable && !bPageMods)
            {
                draw(x1024(224F), y1024(657F), x1024(128F), y1024(48F), 2, i18n("diff.Easy"));
                draw(x1024(416F), y1024(657F), x1024(128F), y1024(48F), 2, i18n("diff.Normal"));
                draw(x1024(608F), y1024(657F), x1024(128F), y1024(48F), 2, i18n("diff.Hard"));
                draw(x1024(160F), y1024(517F), x1024(272F), y1024(48F), 0, "Save current settings");
                draw(x1024(160F), y1024(573F), x1024(272F), y1024(48F), 0, "Load custom difficulty");
            }
            if(bPageFlightModel)
            {
                draw(x1024(528F), y1024(28F), x1024(272F), y1024(48F), 0, i18n("diff.SeparateEStart"));
                draw(x1024(528F), y1024(84F), x1024(272F), y1024(48F), 0, i18n("diff.ComplexEManagement"));
                draw(x1024(528F), y1024(140F), x1024(272F), y1024(48F), 0, i18n("diff.Engine"));
                draw(x1024(528F), y1024(196F), x1024(272F), y1024(48F), 0, i18n("diff.Torque"));
                draw(x1024(528F), y1024(252F), x1024(272F), y1024(48F), 0, i18n("diff.Flutter"));
                draw(x1024(528F), y1024(308F), x1024(272F), y1024(48F), 0, i18n("diff.Stalls"));
                draw(x1024(528F), y1024(364F), x1024(272F), y1024(48F), 0, i18n("diff.Blackouts"));
                draw(x1024(528F), y1024(420F), x1024(272F), y1024(48F), 0, i18n("diff.G_Limits"));
                draw(x1024(528F), y1024(476F), x1024(272F), y1024(48F), 0, i18n("diff.Reliability"));
            } else
            if(bPageWeapons)
            {
                draw(x1024(528F), y1024(28F), x1024(272F), y1024(48F), 0, i18n("diff.RealisticGun"));
                draw(x1024(528F), y1024(84F), x1024(272F), y1024(48F), 0, i18n("diff.LimitedAmmo"));
                draw(x1024(528F), y1024(140F), x1024(272F), y1024(48F), 0, i18n("diff.LimitedFuel"));
                draw(x1024(528F), y1024(196F), x1024(272F), y1024(48F), 0, i18n("diff.BombFuzes"));
                draw(x1024(528F), y1024(252F), x1024(272F), y1024(48F), 0, i18n("diff.FragileTorps"));
                draw(x1024(528F), y1024(308F), x1024(272F), y1024(48F), 0, i18n("diff.RocketSpread"));
            } else
            if(bPageViews)
            {
                draw(x1024(528F), y1024(28F), x1024(272F), y1024(48F), 0, i18n("diff.NoOutside"));
                draw(x1024(528F), y1024(84F), x1024(272F), y1024(48F), 0, i18n("diff.NoOwnViews"));
                draw(x1024(528F), y1024(140F), x1024(272F), y1024(48F), 0, i18n("diff.NoEnemyViews"));
                draw(x1024(528F), y1024(196F), x1024(272F), y1024(48F), 0, i18n("diff.NoFriendlyViews"));
                draw(x1024(528F), y1024(252F), x1024(272F), y1024(48F), 0, i18n("diff.NoAircraftViews"));
                draw(x1024(528F), y1024(308F), x1024(272F), y1024(48F), 0, i18n("diff.NoSeaUnitViews"));
                draw(x1024(528F), y1024(364F), x1024(272F), y1024(48F), 0, i18n("diff.Cockpit"));
                draw(x1024(528F), y1024(420F), x1024(272F), y1024(48F), 0, i18n("diff.NoSpeedBar"));
                draw(x1024(528F), y1024(476F), x1024(272F), y1024(48F), 0, i18n("diff.NoPadlock"));
                draw(x1024(528F), y1024(532F), x1024(272F), y1024(48F), 0, i18n("diff.NoGroundPadlock"));
            } else
            if(bPageIcons)
            {
                draw(x1024(528F), y1024(28F), x1024(272F), y1024(48F), 0, i18n("diff.NoMapIcons"));
                draw(x1024(528F), y1024(84F), x1024(272F), y1024(48F), 0, i18n("diff.NoPlayerIcon"));
                draw(x1024(528F), y1024(140F), x1024(272F), y1024(48F), 0, i18n("diff.NoFogOfWarIcons"));
                draw(x1024(528F), y1024(196F), x1024(272F), y1024(48F), 0, i18n("diff.NoMinimapPath"));
                draw(x1024(528F), y1024(252F), x1024(272F), y1024(48F), 0, i18n("diff.NoIcons"));
            } else
            if(bPageGameplay)
            {
                draw(x1024(528F), y1024(28F), x1024(272F), y1024(48F), 0, i18n("diff.Vulnerability"));
                draw(x1024(528F), y1024(84F), x1024(272F), y1024(48F), 0, i18n("diff.RealisticPilotVulnerability"));
                draw(x1024(528F), y1024(140F), x1024(272F), y1024(48F), 0, i18n("diff.NoInstantSuccess"));
                draw(x1024(528F), y1024(196F), x1024(272F), y1024(48F), 0, i18n("diff.Takeoff"));
                draw(x1024(528F), y1024(252F), x1024(272F), y1024(48F), 0, i18n("diff.RealisticLand"));
                draw(x1024(528F), y1024(308F), x1024(272F), y1024(48F), 0, i18n("diff.RealisticNavInstr"));
                draw(x1024(528F), y1024(364F), x1024(272F), y1024(48F), 0, i18n("diff.SharedKills"));
                draw(x1024(528F), y1024(420F), x1024(272F), y1024(48F), 0, i18n("diff.SharedKillsHistorical"));
                draw(x1024(528F), y1024(476F), x1024(272F), y1024(48F), 0, i18n("diff.Head"));
                draw(x1024(528F), y1024(532F), x1024(272F), y1024(48F), 0, i18n("diff.Wind"));
                draw(x1024(528F), y1024(588F), x1024(272F), y1024(48F), 0, i18n("diff.Clouds"));
            } else
            if(bPageMods)
            {
                draw(x1024(460F), y1024(657F), x1024(272F), y1024(48F), 1, "(A restart might be needed for some changes to take effect)");
            }
        }

        public void setGreenButtonPos()
        {
            bFlightModels.setEnable(!bPageFlightModel);
            bWeapons.setEnable(!bPageWeapons);
            bViews.setEnable(!bPageViews);
            bIcons.setEnable(!bPageIcons);
            bGameplay.setEnable(!bPageGameplay);
            bMods.setEnable(!bPageMods);
            if(bPageFlightModel)
                bGreen.setPosC(x1024(56F), y1024(53F));
            if(bPageWeapons)
                bGreen.setPosC(x1024(56F), y1024(109F));
            if(bPageViews)
                bGreen.setPosC(x1024(56F), y1024(165F));
            if(bPageIcons)
                bGreen.setPosC(x1024(56F), y1024(221F));
            if(bPageGameplay)
                bGreen.setPosC(x1024(56F), y1024(277F));
//            if(bPageMisc)
//                bGreen.setPosC(x1024(56F), y1024(333F));
            if(bPageMods)
                bGreen.setPosC(x1024(56F), y1024(361F));
            bGreen.setEnable(true);
            bGreen.setKeyFocus();
        }

        public void setPosSize()
        {
            set1024PosSize(92F, 32F, 832F, 736F);
            sSeparateEStart.setPosC(x1024(488F), y1024(52F));
            sComplexEManagement.setPosC(x1024(488F), y1024(108F));
            sEngine_Overheat.setPosC(x1024(488F), y1024(164F));
            sTorque_N_Gyro_Effects.setPosC(x1024(488F), y1024(220F));
            sFlutter_Effect.setPosC(x1024(488F), y1024(276F));
            sStalls_N_Spins.setPosC(x1024(488F), y1024(332F));
            sBlackouts_N_Redouts.setPosC(x1024(488F), y1024(388F));
            sG_Limits.setPosC(x1024(488F), y1024(444F));
            sReliability.setPosC(x1024(488F), y1024(500F));
            sRealistic_Gunnery.setPosC(x1024(488F), y1024(52F));
            sLimited_Ammo.setPosC(x1024(488F), y1024(108F));
            sLimited_Fuel.setPosC(x1024(488F), y1024(164F));
            sBombFuzes.setPosC(x1024(488F), y1024(220F));
            sFragileTorps.setPosC(x1024(488F), y1024(276F));
            sRealisticRocketSpread.setPosC(x1024(488F), y1024(332F));
            sNo_Outside_Views.setPosC(x1024(488F), y1024(52F));
            sNoOwnPlayerViews.setPosC(x1024(488F), y1024(108F));
            sNoEnemyViews.setPosC(x1024(488F), y1024(164F));
            sNoFriendlyViews.setPosC(x1024(488F), y1024(220F));
            sNoAircraftViews.setPosC(x1024(488F), y1024(276F));
            sNoSeaUnitViews.setPosC(x1024(488F), y1024(332F));
            sCockpit_Always_On.setPosC(x1024(488F), y1024(388F));
            sNoSpeedBar.setPosC(x1024(488F), y1024(444F));
            sNo_Padlock.setPosC(x1024(488F), y1024(500F));
            sNo_GroundPadlock.setPosC(x1024(488F), y1024(556F));
            sNo_Map_Icons.setPosC(x1024(488F), y1024(52F));
            sNo_Player_Icon.setPosC(x1024(488F), y1024(108F));
            sNo_Fog_Of_War_Icons.setPosC(x1024(488F), y1024(164F));
            sNoMinimapPath.setPosC(x1024(488F), y1024(220F));
            sNo_Icons.setPosC(x1024(488F), y1024(276F));
            sVulnerability.setPosC(x1024(488F), y1024(52F));
            sRealisticPilotVulnerability.setPosC(x1024(488F), y1024(108F));
            sHead_Shake.setPosC(x1024(488F), y1024(164F));
            sWind_N_Turbulence.setPosC(x1024(488F), y1024(220F));
            sClouds.setPosC(x1024(488F), y1024(276F));
            sNoInstantSuccess.setPosC(x1024(488F), y1024(332F));
            sTakeoff_N_Landing.setPosC(x1024(488F), y1024(388F));
            sRealistic_Landings.setPosC(x1024(488F), y1024(444F));
            sRealisticNavigationInstruments.setPosC(x1024(488F), y1024(500F));
            sVulnerability.setPosC(x1024(488F), y1024(52F));
            sRealisticPilotVulnerability.setPosC(x1024(488F), y1024(108F));
            sNoInstantSuccess.setPosC(x1024(488F), y1024(164F));
            sTakeoff_N_Landing.setPosC(x1024(488F), y1024(220F));
            sRealistic_Landings.setPosC(x1024(488F), y1024(276F));
            sRealisticNavigationInstruments.setPosC(x1024(488F), y1024(332F));
            sSharedKills.setPosC(x1024(488F), y1024(388F));
            sSharedKillsHistorical.setPosC(x1024(488F), y1024(444F));
            sHead_Shake.setPosC(x1024(488F), y1024(500F));
            sWind_N_Turbulence.setPosC(x1024(488F), y1024(556F));
            sClouds.setPosC(x1024(488F), y1024(612F));
            bExit.setPosC(x1024(56F), y1024(682F));
            bEasy.setPosC(x1024(392F), y1024(682F));
            bNormal.setPosC(x1024(584F), y1024(682F));
            bHard.setPosC(x1024(776F), y1024(682F));
            bCustomSave.setPosC(x1024(120F), y1024(542F));
            bCustomLoad.setPosC(x1024(120F), y1024(598F));
            bFlightModels.setPosC(x1024(56F), y1024(53F));
            bWeapons.setPosC(x1024(56F), y1024(109F));
            bViews.setPosC(x1024(56F), y1024(165F));
            bIcons.setPosC(x1024(56F), y1024(221F));
            bGameplay.setPosC(x1024(56F), y1024(277F));
            bMods.setPosC(x1024(56F), y1024(361F));
            wTable.setPosSize(x1024(392F), y1024(48F), x1024(408F), y1024(440F));
            wScrollDescription.setPosSize(x1024(392F), y1024(512F), x1024(408F), y1024(100F));
        }

        public DialogClient()
        {
        }
    }

    private static class ModItem
    {
        String name;
        String value;

        private ModItem()
        {

        }
    }

    public class Table extends GWindowTable
    {
//        public void addUser()
//        {
//            int i = selectRow + 1;
//            mods.add(i, new UserCfg(UserCfg.defName, UserCfg.defCallsign, UserCfg.defSurname));
//            setSelect(i, 0);
//            resized();
//        }

//        public void removeUser()
//        {
//            if(mods.size() <= 1)
//                return;
//            int i = selectRow;
//            if(i < 0)
//                return;
//            UserCfg usercfg = (UserCfg)mods.get(i);
//            if(usercfg.sId != null)
//                usercfg.removeUserDir();
//            mods.remove(i);
//            if(editor != null)
//            {
//                ((GWindow)editor).hideWindow();
//                editor = null;
//            }
//            if(--i < 0)
//                i = 0;
//            setSelect(i, 0);
//            resized();
//        }

        public int countRows()
        {
            return mods == null ? 0 : mods.size();
        }

//        public void columnClicked(int i)
//        {
//            sort(i);
//        }

        public boolean isCellEditable(int i, int j)
        {
            return j == 1;
        }

        public GWindowCellEdit getCellEdit(int i, int j)
        {
            ModItem moditem = (ModItem)mods.get(i);
            textDescription = fillDescription(moditem.name);
            wScrollDescription.resized();
            GWindowCellEdit gwindowcelledit = super.getCellEdit(i, j);
            if(gwindowcelledit != null && (gwindowcelledit instanceof GWindowEditControl))
            {
                GWindowEditControl gwindoweditcontrol = (GWindowEditControl)gwindowcelledit;
                gwindoweditcontrol.maxLength = 32;
            }
            return gwindowcelledit;
        }

        public Object getValueAt(int i, int j)
        {
            ModItem moditem = (ModItem)mods.get(i);
            switch(j)
            {
            case 0: // '\0'
                return moditem.name;

            case 1: // '\001'
                return moditem.value;
            }
            return null;
        }

        public void setValueAt(Object obj, int i, int j)
        {
            if(i < 0 || j < 0)
                return;
            if(i >= mods.size())
                return;
            ModItem moditem = (ModItem)mods.get(i);
            String s = (String)obj;
            if(s == null || s.trim().length() == 0)
                s = "null";
            switch(j)
            {
            case 0: // '\0'
                moditem.name = s;
                break;

            case 1: // '\001'
                moditem.value = s;
                break;
            }
        }

        public void afterCreated()
        {
            super.afterCreated();
            bColumnsSizable = true;
            addColumn("Name", null);
            addColumn("Value", null);
            vSB.scroll = rowHeight(0);
//            getColumn(0).setRelativeDx(15F);
//            getColumn(1).setRelativeDx(9F);
//            getColumn(2).setRelativeDx(9F);
            alignColumns();
            resized();
        }

        public void resolutionChanged()
        {
            vSB.scroll = rowHeight(0);
            super.resolutionChanged();
        }

        public ArrayList mods;

        public Table(GWindow gwindow)
        {
//            super(gwindow, 2.0F, 4F, 20F, 16F);
            super(gwindow);
            bColumnsSizable = true;
            bSelectRow = true;
            mods = new ArrayList();
        }
    }

    public class Descript extends GWindowDialogClient
    {
        public void render()
        {
            String s = textDescription;
            if(s != null)
            {
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                setCanvasFont(0);
                setCanvasColorBLACK();
                super.root.C.clip.y += gbevel.T.dy;
                super.root.C.clip.dy -= gbevel.T.dy + gbevel.B.dy;
                drawLines(gbevel.L.dx + 2.0F, gbevel.T.dy + 2.0F, s, 0, s.length(), super.win.dx - gbevel.L.dx - gbevel.R.dx - 4F, super.root.C.font.height);
            }
        }

        public void computeSize()
        {
            String s = textDescription;
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

    private void loadModList()
    {
//        wTable.mods.clear();
        String modList[] = Config.cur.ini.getVariables("Mods");
        if(modList != null && modList.length > 0)
        {
            for(int i = 0; i < modList.length; i++)
            {
                String modValue = Config.cur.ini.get("Mods", modList[i], "null");
                ModItem moditem = new ModItem();
                moditem.name = modList[i];
                moditem.value = modValue;
                wTable.mods.add(moditem);
            }
        }
    }

    private void saveModList()
    {
        String modList[] = Config.cur.ini.getVariables("Mods");
        if(modList != null && modList.length > 0)
        {
            for(int i = 0; i < wTable.mods.size(); i++)
            {
                ModItem moditem = (ModItem)wTable.mods.get(i);
                Config.cur.ini.set("Mods", moditem.name, moditem.value);
            }
        }
    }

    protected String fillDescription(String s)
    {
        String modDescription = "";
        BufferedReader bufferedreader = null;
        try
        {
            bufferedreader = new BufferedReader(new SFSReader("i18n/Mods/" + s + ".properties"));
            do
            {
                String thisLine = bufferedreader.readLine();
                if(thisLine == null)
                    break;
                modDescription += UnicodeTo8bit.load(thisLine) + "\n";
            } while(true);
        }
        catch(Exception exception) {

            return "Description not available";
        }
        if(bufferedreader != null)
            try
            {
                bufferedreader.close();
            }
            catch(Exception exception1) { }

        return modDescription;
    }

    protected Descript createDescript(GWindow gwindow)
    {
        return (Descript)gwindow.create(new Descript());
    }

    public void enterPush(GameState gamestate)
    {
        if(gamestate.id() == 27)
            bEnable = false;
        else
            bEnable = true;
        _enter();
    }

    protected DifficultySettings settings()
    {
        return World.cur().diffUser;
    }

    private boolean isMultiplay()
    {
        return Mission.isServer() || Mission.isNet();
    }

    public void _enter()
    {
        reset();
        sReliability.setEnable(bEnable);
        sG_Limits.setEnable(bEnable);
        sRealisticPilotVulnerability.setEnable(bEnable);
        sRealisticNavigationInstruments.setEnable(bEnable);
        sNo_Player_Icon.setEnable(bEnable);
        sBombFuzes.setEnable(bEnable);
        sFragileTorps.setEnable(bEnable);
        sNoEnemyViews.setEnable(bEnable);
        sNoFriendlyViews.setEnable(bEnable);
        sNoSeaUnitViews.setEnable(bEnable);
        sNoAircraftViews.setEnable(bEnable);
        sNoOwnPlayerViews.setEnable(bEnable);
        sRealisticRocketSpread.setEnable(bEnable);
        sSharedKills.setEnable(bEnable);
        sSharedKillsHistorical.setEnable(bEnable);
        sNo_GroundPadlock.setEnable(bEnable);
        sWind_N_Turbulence.setEnable(bEnable);
        sFlutter_Effect.setEnable(bEnable);
        sStalls_N_Spins.setEnable(bEnable);
        sBlackouts_N_Redouts.setEnable(bEnable);
        sEngine_Overheat.setEnable(bEnable);
        sTorque_N_Gyro_Effects.setEnable(bEnable);
        sRealistic_Landings.setEnable(bEnable);
        sTakeoff_N_Landing.setEnable(bEnable);
        sCockpit_Always_On.setEnable(bEnable);
        sNo_Outside_Views.setEnable(bEnable);
        sHead_Shake.setEnable(bEnable);
        sNo_Icons.setEnable(bEnable);
        sNo_Map_Icons.setEnable(bEnable);
        sRealistic_Gunnery.setEnable(bEnable);
        sLimited_Ammo.setEnable(bEnable);
        sLimited_Fuel.setEnable(bEnable);
        sVulnerability.setEnable(bEnable);
        sNo_Padlock.setEnable(bEnable);
        sClouds.setEnable(bEnable);
        sSeparateEStart.setEnable(bEnable);
        sNoInstantSuccess.setEnable(bEnable);
        sNoMinimapPath.setEnable(bEnable);
        sNoSpeedBar.setEnable(bEnable);
        sComplexEManagement.setEnable(bEnable);
        if(!isMultiplay())
            loadModList();
        if(isMultiplay() && bPageMods)
        {
            bPageFlightModel = true;
            bPageWeapons = false;
            bPageViews = false;
            bPageIcons = false;
            bPageGameplay = false;
            bPageMods = false;
        }
        setShow(!isMultiplay(), bMods);
        setShow(bEnable && !bPageMods, bEasy);
        setShow(bEnable && !bPageMods, bNormal);
        setShow(bEnable && !bPageMods, bHard);
        setShow(bEnable && !bPageMods, bCustomLoad);
        setShow(bEnable && !bPageMods, bCustomSave);
        showHide();
        enableAndDisableViewSwitches();
        enableAndDisableSharedKillSwitches();
        client.activateWindow();
    }

    private void enableAndDisableViewSwitches()
    {
        sNoOwnPlayerViews.setEnable((!sNo_Outside_Views.isChecked()) & bEnable);
        sNoEnemyViews.setEnable((!sNo_Outside_Views.isChecked()) & bEnable);
        sNoFriendlyViews.setEnable((!sNo_Outside_Views.isChecked()) & bEnable);
        sNoSeaUnitViews.setEnable((!sNo_Outside_Views.isChecked()) & bEnable);
        sNoAircraftViews.setEnable((!sNo_Outside_Views.isChecked()) & bEnable);
        if(sNo_Outside_Views.isChecked())
        {
            sNoOwnPlayerViews.setChecked(true, false);
            sNoEnemyViews.setChecked(true, false);
            sNoFriendlyViews.setChecked(true, false);
            sNoSeaUnitViews.setChecked(true, false);
            sNoAircraftViews.setChecked(true, false);
        }
    }

    private void enableAndDisableSharedKillSwitches()
    {
        sSharedKillsHistorical.setEnable(sSharedKills.isChecked() & bEnable);
        if(!sSharedKills.isChecked())
            sSharedKillsHistorical.setChecked(false, false);
    }

    private void setShow(boolean flag, GWindow gwindow)
    {
        if(flag)
            gwindow.showWindow();
        else
            gwindow.hideWindow();
    }

    private void showHide()
    {
        setShow(bPageFlightModel, sSeparateEStart);
        setShow(bPageFlightModel, sComplexEManagement);
        setShow(bPageFlightModel, sEngine_Overheat);
        setShow(bPageFlightModel, sTorque_N_Gyro_Effects);
        setShow(bPageFlightModel, sFlutter_Effect);
        setShow(bPageFlightModel, sStalls_N_Spins);
        setShow(bPageFlightModel, sBlackouts_N_Redouts);
        setShow(bPageFlightModel, sG_Limits);
        setShow(bPageFlightModel, sReliability);
        setShow(bPageWeapons, sRealistic_Gunnery);
        setShow(bPageWeapons, sLimited_Ammo);
        setShow(bPageWeapons, sLimited_Fuel);
        setShow(bPageWeapons, sBombFuzes);
        setShow(bPageWeapons, sFragileTorps);
        setShow(bPageWeapons, sRealisticRocketSpread);
        setShow(bPageViews, sCockpit_Always_On);
        setShow(bPageViews, sNo_Outside_Views);
        setShow(bPageViews, sNoEnemyViews);
        setShow(bPageViews, sNoFriendlyViews);
        setShow(bPageViews, sNoAircraftViews);
        setShow(bPageViews, sNoOwnPlayerViews);
        setShow(bPageViews, sNoSeaUnitViews);
        setShow(bPageViews, sNoSpeedBar);
        setShow(bPageViews, sNo_Padlock);
        setShow(bPageViews, sNo_GroundPadlock);
        setShow(bPageIcons, sNo_Map_Icons);
        setShow(bPageIcons, sNo_Player_Icon);
        setShow(bPageIcons, sNo_Fog_Of_War_Icons);
        setShow(bPageIcons, sNoMinimapPath);
        setShow(bPageIcons, sNo_Icons);
        setShow(bPageGameplay, sVulnerability);
        setShow(bPageGameplay, sRealisticPilotVulnerability);
        setShow(bPageGameplay, sNoInstantSuccess);
        setShow(bPageGameplay, sTakeoff_N_Landing);
        setShow(bPageGameplay, sRealistic_Landings);
        setShow(bPageGameplay, sRealisticNavigationInstruments);
        setShow(bPageGameplay, sSharedKills);
        setShow(bPageGameplay, sSharedKillsHistorical);
        setShow(bPageGameplay, sHead_Shake);
        setShow(bPageGameplay, sWind_N_Turbulence);
        setShow(bPageGameplay, sClouds);
        setShow(bPageMods, wTable);
        setShow(bPageMods, wScrollDescription);
        setShow(bEnable && !bPageMods, bEasy);
        setShow(bEnable && !bPageMods, bNormal);
        setShow(bEnable && !bPageMods, bHard);
        setShow(bEnable && !bPageMods, bCustomLoad);
        setShow(bEnable && !bPageMods, bCustomSave);
        dialogClient.doResolutionChanged();
        dialogClient.setPosSize();
        dialogClient.setGreenButtonPos();
    }

    private void reset()
    {
        DifficultySettings difficultysettings = settings();
        sReliability.setChecked(difficultysettings.Reliability, false);
        sG_Limits.setChecked(difficultysettings.G_Limits, false);
        sRealisticPilotVulnerability.setChecked(difficultysettings.RealisticPilotVulnerability, false);
        sRealisticNavigationInstruments.setChecked(difficultysettings.RealisticNavigationInstruments, false);
        sNo_Player_Icon.setChecked(difficultysettings.No_Player_Icon, false);
        sNo_Fog_Of_War_Icons.setChecked(difficultysettings.No_Fog_Of_War_Icons, false);
        sBombFuzes.setChecked(difficultysettings.BombFuzes, false);
        sFragileTorps.setChecked(difficultysettings.FragileTorps, false);
        sNoEnemyViews.setChecked(difficultysettings.NoEnemyViews, false);
        sNoFriendlyViews.setChecked(difficultysettings.NoFriendlyViews, false);
        sNoSeaUnitViews.setChecked(difficultysettings.NoSeaUnitViews, false);
        sNoAircraftViews.setChecked(difficultysettings.NoAircraftViews, false);
        sNoOwnPlayerViews.setChecked(difficultysettings.NoOwnPlayerViews, false);
        sRealisticRocketSpread.setChecked(difficultysettings.RealisticRocketSpread, false);
        sSharedKills.setChecked(difficultysettings.SharedKills, false);
        sSharedKillsHistorical.setChecked(difficultysettings.SharedKillsHistorical, false);
        sNo_GroundPadlock.setChecked(difficultysettings.No_GroundPadlock, false);
        sWind_N_Turbulence.setChecked(difficultysettings.Wind_N_Turbulence, false);
        sFlutter_Effect.setChecked(difficultysettings.Flutter_Effect, false);
        sStalls_N_Spins.setChecked(difficultysettings.Stalls_N_Spins, false);
        sBlackouts_N_Redouts.setChecked(difficultysettings.Blackouts_N_Redouts, false);
        sEngine_Overheat.setChecked(difficultysettings.Engine_Overheat, false);
        sTorque_N_Gyro_Effects.setChecked(difficultysettings.Torque_N_Gyro_Effects, false);
        sRealistic_Landings.setChecked(difficultysettings.Realistic_Landings, false);
        sTakeoff_N_Landing.setChecked(difficultysettings.Takeoff_N_Landing, false);
        sCockpit_Always_On.setChecked(difficultysettings.Cockpit_Always_On, false);
        sNo_Outside_Views.setChecked(difficultysettings.No_Outside_Views, false);
        sHead_Shake.setChecked(difficultysettings.Head_Shake, false);
        sNo_Icons.setChecked(difficultysettings.No_Icons, false);
        sNo_Map_Icons.setChecked(difficultysettings.No_Map_Icons, false);
        sRealistic_Gunnery.setChecked(difficultysettings.Realistic_Gunnery, false);
        sLimited_Ammo.setChecked(difficultysettings.Limited_Ammo, false);
        sLimited_Fuel.setChecked(difficultysettings.Limited_Fuel, false);
        sVulnerability.setChecked(difficultysettings.Vulnerability, false);
        sNo_Padlock.setChecked(difficultysettings.No_Padlock, false);
        sClouds.setChecked(difficultysettings.Clouds, false);
        sSeparateEStart.setChecked(difficultysettings.SeparateEStart, false);
        sNoInstantSuccess.setChecked(difficultysettings.NoInstantSuccess, false);
        sNoMinimapPath.setChecked(difficultysettings.NoMinimapPath, false);
        sNoSpeedBar.setChecked(difficultysettings.NoSpeedBar, false);
        sComplexEManagement.setChecked(difficultysettings.ComplexEManagement, false);
        sNo_Fog_Of_War_Icons.setEnable(sNo_Map_Icons.isChecked() & bEnable);
        enableAndDisableViewSwitches();
        enableAndDisableSharedKillSwitches();
    }

    private void changed()
    {
        DifficultySettings difficultysettings = settings();
        difficultysettings.Reliability = sReliability.isChecked();
        difficultysettings.RealisticNavigationInstruments = sRealisticNavigationInstruments.isChecked();
        difficultysettings.G_Limits = sG_Limits.isChecked();
        difficultysettings.RealisticPilotVulnerability = sRealisticPilotVulnerability.isChecked();
        difficultysettings.No_Player_Icon = sNo_Player_Icon.isChecked();
        difficultysettings.No_Fog_Of_War_Icons = sNo_Fog_Of_War_Icons.isChecked();
        difficultysettings.BombFuzes = sBombFuzes.isChecked();
        difficultysettings.FragileTorps = sFragileTorps.isChecked();
        difficultysettings.NoEnemyViews = sNoEnemyViews.isChecked();
        difficultysettings.NoFriendlyViews = sNoFriendlyViews.isChecked();
        difficultysettings.NoSeaUnitViews = sNoSeaUnitViews.isChecked();
        difficultysettings.NoAircraftViews = sNoAircraftViews.isChecked();
        difficultysettings.NoOwnPlayerViews = sNoOwnPlayerViews.isChecked();
        difficultysettings.RealisticRocketSpread = sRealisticRocketSpread.isChecked();
        difficultysettings.Wind_N_Turbulence = sWind_N_Turbulence.isChecked();
        difficultysettings.Flutter_Effect = sFlutter_Effect.isChecked();
        difficultysettings.Stalls_N_Spins = sStalls_N_Spins.isChecked();
        difficultysettings.Blackouts_N_Redouts = sBlackouts_N_Redouts.isChecked();
        difficultysettings.Engine_Overheat = sEngine_Overheat.isChecked();
        difficultysettings.Torque_N_Gyro_Effects = sTorque_N_Gyro_Effects.isChecked();
        difficultysettings.Realistic_Landings = sRealistic_Landings.isChecked();
        difficultysettings.Takeoff_N_Landing = sTakeoff_N_Landing.isChecked();
        difficultysettings.Cockpit_Always_On = sCockpit_Always_On.isChecked();
        difficultysettings.No_Outside_Views = sNo_Outside_Views.isChecked();
        difficultysettings.Head_Shake = sHead_Shake.isChecked();
        difficultysettings.No_Icons = sNo_Icons.isChecked();
        difficultysettings.No_Map_Icons = sNo_Map_Icons.isChecked();
        difficultysettings.Realistic_Gunnery = sRealistic_Gunnery.isChecked();
        difficultysettings.Limited_Ammo = sLimited_Ammo.isChecked();
        difficultysettings.Limited_Fuel = sLimited_Fuel.isChecked();
        difficultysettings.Vulnerability = sVulnerability.isChecked();
        difficultysettings.No_Padlock = sNo_Padlock.isChecked();
        difficultysettings.Clouds = sClouds.isChecked();
        difficultysettings.SeparateEStart = sSeparateEStart.isChecked();
        difficultysettings.NoInstantSuccess = sNoInstantSuccess.isChecked();
        difficultysettings.NoMinimapPath = sNoMinimapPath.isChecked();
        difficultysettings.NoSpeedBar = sNoSpeedBar.isChecked();
        difficultysettings.ComplexEManagement = sComplexEManagement.isChecked();
        difficultysettings.SharedKills = sSharedKills.isChecked();
        difficultysettings.SharedKillsHistorical = sSharedKillsHistorical.isChecked();
        difficultysettings.No_GroundPadlock = sNo_GroundPadlock.isChecked();
    }

    public void _leave()
    {
        if(bEnable)
        {
            DifficultySettings difficultysettings = settings();
            difficultysettings.Reliability = sReliability.isChecked();
            difficultysettings.RealisticNavigationInstruments = sRealisticNavigationInstruments.isChecked();
            difficultysettings.G_Limits = sG_Limits.isChecked();
            difficultysettings.RealisticPilotVulnerability = sRealisticPilotVulnerability.isChecked();
            difficultysettings.No_Player_Icon = sNo_Player_Icon.isChecked();
            difficultysettings.No_Fog_Of_War_Icons = sNo_Fog_Of_War_Icons.isChecked();
            difficultysettings.BombFuzes = sBombFuzes.isChecked();
            difficultysettings.FragileTorps = sFragileTorps.isChecked();
            difficultysettings.NoEnemyViews = sNoEnemyViews.isChecked();
            difficultysettings.NoFriendlyViews = sNoFriendlyViews.isChecked();
            difficultysettings.NoSeaUnitViews = sNoSeaUnitViews.isChecked();
            difficultysettings.NoAircraftViews = sNoAircraftViews.isChecked();
            difficultysettings.NoOwnPlayerViews = sNoOwnPlayerViews.isChecked();
            difficultysettings.RealisticRocketSpread = sRealisticRocketSpread.isChecked();
            difficultysettings.Wind_N_Turbulence = sWind_N_Turbulence.isChecked();
            difficultysettings.Flutter_Effect = sFlutter_Effect.isChecked();
            difficultysettings.Stalls_N_Spins = sStalls_N_Spins.isChecked();
            difficultysettings.Blackouts_N_Redouts = sBlackouts_N_Redouts.isChecked();
            difficultysettings.Engine_Overheat = sEngine_Overheat.isChecked();
            difficultysettings.Torque_N_Gyro_Effects = sTorque_N_Gyro_Effects.isChecked();
            difficultysettings.Realistic_Landings = sRealistic_Landings.isChecked();
            difficultysettings.Takeoff_N_Landing = sTakeoff_N_Landing.isChecked();
            difficultysettings.Cockpit_Always_On = sCockpit_Always_On.isChecked();
            difficultysettings.No_Outside_Views = sNo_Outside_Views.isChecked();
            difficultysettings.Head_Shake = sHead_Shake.isChecked();
            difficultysettings.No_Icons = sNo_Icons.isChecked();
            difficultysettings.No_Map_Icons = sNo_Map_Icons.isChecked();
            difficultysettings.Realistic_Gunnery = sRealistic_Gunnery.isChecked();
            difficultysettings.Limited_Ammo = sLimited_Ammo.isChecked();
            difficultysettings.Limited_Fuel = sLimited_Fuel.isChecked();
            difficultysettings.Vulnerability = sVulnerability.isChecked();
            difficultysettings.No_Padlock = sNo_Padlock.isChecked();
            difficultysettings.Clouds = sClouds.isChecked();
            difficultysettings.SeparateEStart = sSeparateEStart.isChecked();
            difficultysettings.NoInstantSuccess = sNoInstantSuccess.isChecked();
            difficultysettings.NoMinimapPath = sNoMinimapPath.isChecked();
            difficultysettings.NoSpeedBar = sNoSpeedBar.isChecked();
            difficultysettings.ComplexEManagement = sComplexEManagement.isChecked();
            difficultysettings.SharedKills = sSharedKills.isChecked();
            difficultysettings.SharedKillsHistorical = sSharedKillsHistorical.isChecked();
            difficultysettings.No_GroundPadlock = sNo_GroundPadlock.isChecked();
        }
        if(!isMultiplay())
            saveModList();
        wTable.mods.clear();
        client.hideWindow();
    }

    protected void clientInit(GWindowRoot gwindowroot)
    {
    }

    public GUIDifficulty(GWindowRoot gwindowroot)
    {
        this(gwindowroot, 17);
    }

    protected GUIDifficulty(GWindowRoot gwindowroot, int i)
    {
        super(i);
        bEnable = true;
        bPageFlightModel = true;
        bPageWeapons = false;
        bPageViews = false;
        bPageIcons = false;
//        bPageMisc = false;
        bPageGameplay = false;
        bPageMods = false;
        textDescription = null;
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        wTable = new Table(dialogClient);
        dialogClient.create(wScrollDescription = new ScrollDescript());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("diff.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bEasy = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bNormal = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bHard = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bCustomLoad = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bCustomSave = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        sWind_N_Turbulence = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sFlutter_Effect = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sStalls_N_Spins = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sBlackouts_N_Redouts = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sEngine_Overheat = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sTorque_N_Gyro_Effects = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sRealistic_Landings = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sTakeoff_N_Landing = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sCockpit_Always_On = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNo_Outside_Views = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sHead_Shake = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNo_Icons = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNo_Map_Icons = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sRealistic_Gunnery = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sLimited_Ammo = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sLimited_Fuel = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sVulnerability = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNo_Padlock = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sClouds = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sSeparateEStart = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNoInstantSuccess = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNoMinimapPath = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNoSpeedBar = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sComplexEManagement = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sReliability = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sRealisticNavigationInstruments = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sG_Limits = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sRealisticPilotVulnerability = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNo_Player_Icon = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNo_Fog_Of_War_Icons = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sBombFuzes = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sFragileTorps = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNoEnemyViews = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNoFriendlyViews = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNoSeaUnitViews = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNoAircraftViews = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNoOwnPlayerViews = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sRealisticRocketSpread = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sSharedKills = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sSharedKillsHistorical = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sNo_GroundPadlock = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        bWeapons = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bFlightModels = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bViews = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bGameplay = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bIcons = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bMods = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bGreen = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        clientInit(gwindowroot);
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUISwitchBox3 sWind_N_Turbulence;
    public GUISwitchBox3 sFlutter_Effect;
    public GUISwitchBox3 sStalls_N_Spins;
    public GUISwitchBox3 sBlackouts_N_Redouts;
    public GUISwitchBox3 sEngine_Overheat;
    public GUISwitchBox3 sTorque_N_Gyro_Effects;
    public GUISwitchBox3 sRealistic_Landings;
    public GUISwitchBox3 sTakeoff_N_Landing;
    public GUISwitchBox3 sCockpit_Always_On;
    public GUISwitchBox3 sNo_Outside_Views;
    public GUISwitchBox3 sHead_Shake;
    public GUISwitchBox3 sNo_Icons;
    public GUISwitchBox3 sNo_Map_Icons;
    public GUISwitchBox3 sRealistic_Gunnery;
    public GUISwitchBox3 sLimited_Ammo;
    public GUISwitchBox3 sLimited_Fuel;
    public GUISwitchBox3 sVulnerability;
    public GUISwitchBox3 sNo_Padlock;
    public GUISwitchBox3 sClouds;
    public GUISwitchBox3 sSeparateEStart;
    public GUISwitchBox3 sNoInstantSuccess;
    public GUISwitchBox3 sNoMinimapPath;
    public GUISwitchBox3 sNoSpeedBar;
    public GUISwitchBox3 sComplexEManagement;
    public GUISwitchBox3 sReliability;
    public GUISwitchBox3 sG_Limits;
    public GUISwitchBox3 sRealisticPilotVulnerability;
    public GUISwitchBox3 sRealisticNavigationInstruments;
    public GUISwitchBox3 sNo_Player_Icon;
    public GUISwitchBox3 sNo_Fog_Of_War_Icons;
    public GUISwitchBox3 sBombFuzes;
    public GUISwitchBox3 sFragileTorps;
    public GUISwitchBox3 sNoEnemyViews;
    public GUISwitchBox3 sNoFriendlyViews;
    public GUISwitchBox3 sNoSeaUnitViews;
    public GUISwitchBox3 sNoAircraftViews;
    public GUISwitchBox3 sNoOwnPlayerViews;
    public GUISwitchBox3 sRealisticRocketSpread;
    public GUISwitchBox3 sSharedKills;
    public GUISwitchBox3 sSharedKillsHistorical;
    public GUISwitchBox3 sNo_GroundPadlock;
    public GUIButton bExit;
    public GUIButton bEasy;
    public GUIButton bNormal;
    public GUIButton bHard;
    public GUIButton bCustomLoad;
    public GUIButton bCustomSave;
    public GUIButton bWeapons;
    public GUIButton bFlightModels;
    public GUIButton bViews;
    public GUIButton bIcons;
    public GUIButton bGameplay;
    public GUIButton bMods;
    public GUIButton bGreen;
    public boolean bEnable;
    public boolean bPageFlightModel;
    public boolean bPageWeapons;
    public boolean bPageViews;
    public boolean bPageIcons;
//    public boolean bPageMisc;
    public boolean bPageGameplay;
    public boolean bPageMods;
    public Table wTable;
    public Descript wDescript;
    public ScrollDescript wScrollDescription;
    public String textDescription;
    private static final int ROW_D = 56;
    private static final int ROW_1 = 28;
    private static final int ROW_2 = 84;
    private static final int ROW_3 = 140;
    private static final int ROW_4 = 196;
    private static final int ROW_5 = 252;
    private static final int ROW_6 = 308;
    private static final int ROW_7 = 364;
    private static final int ROW_8 = 420;
    private static final int ROW_9 = 476;
    private static final int ROW_10 = 532;
    private static final int ROW_11 = 588;

}
