package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.hotkey.HookView;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.*;
import java.io.*;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class GUISetupMisc extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bGeneral)
            {
                setPage(0);
                return true;
            }
            if(gwindow == bServer)
            {
                setPage(1);
                return true;
            }
            if(gwindow == bOnlineStats)
            {
                setPage(2);
                return true;
            }
            if(gwindow == b6DoF)
            {
                setPage(3);
                return true;
            }
            if(gwindow == bHUD)
            {
                setPage(6);
                return true;
            }
            if(gwindow == bGUI)
            {
                setPage(5);
                return true;
            }
            if(gwindow == bIcons)
            {
                setPage(7);
                return true;
            }
            if(gwindow == bResetIconsFriend)
            {
                setDefaultIcons(1);
                return true;
            }
            if(gwindow == bResetIconsFoe)
            {
                setDefaultIcons(2);
                return true;
            }
            if(gwindow == bBack)
            {
                applyChanges();
                Main.stateStack().pop();
                return true;
            }
            if(gwindow == sUIColor)
                setUIColor();
            else
            if(gwindow == sUIDetail)
                setUIDetail();
            else
            if(gwindow == sUIBackground)
                setUIBackground();
            else
            if(gwindow == sScreenshotType)
                setScreenshotType();
            else
            if(gwindow == sDisableNetStatStatistics)
                enableAndDisableStats();
            return super.notify(gwindow, i, j);
        }

        public void render()
        {
            super.render();
            setCanvasFont(0);
            if(page == 7)
            {
                GUISeparate.draw(this, GColor.Gray, x1024(290F), y1024(95F), x1024(510F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(624F), x1024(222F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(272F), y1024(32F), 2.0F, y1024(680F));
            } else
            {
                GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(624F), x1024(768F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(272F), y1024(32F), 2.0F, y1024(560F));
            }
            if(page != 1)
                if(page != 2);
            setCanvasColor(GColor.Gray);
            draw(x1024(96F), y1024(30F), x1024(160F), y1024(48F), 0, i18n("misc.General"));
            draw(x1024(96F), y1024(94F), x1024(160F), y1024(48F), 0, i18n("misc.HUD"));
            draw(x1024(96F), y1024(158F), x1024(160F), y1024(48F), 0, i18n("misc.OnlineStats"));
            draw(x1024(96F), y1024(222F), x1024(160F), y1024(48F), 0, i18n("misc.Server"));
            draw(x1024(96F), y1024(286F), x1024(160F), y1024(48F), 0, i18n("misc.6dof"));
            draw(x1024(96F), y1024(350F), x1024(160F), y1024(48F), 0, i18n("misc.userinterface"));
            draw(x1024(96F), y1024(414F), x1024(160F), y1024(48F), 0, i18n("Icons"));
            float f = 400F;
            float f2 = 1.0F;
            float f3 = 1.0F;
            float f4 = root.win.dx / root.win.dy;
            if(f4 < 1.0F)
            {
                f2 = 0.5F;
                f3 = 1.5F;
            }
            if(page == 0)
            {
                setCanvasColorWHITE();
                setCanvasColor(GColor.Gray);
                draw(x1024(f), y1024(30F), x1024(272F), y1024(48F), 0, i18n("misc.Chatter"));
                draw(x1024(f), y1024(88F), x1024(272F), y1024(48F), 0, i18n("misc.ShowParatrooperViews"));
                draw(x1024(f), y1024(146F), x1024(272F), y1024(48F), 0, i18n("misc.MorseChat"));
                draw(x1024(f), y1024(204F), x1024(272F), y1024(48F), 0, i18n("misc.SmallMapWPLabels"));
                draw(x1024(f), y1024(262F), x1024(272F), y1024(48F), 0, i18n("misc.SaveTrk"));
                draw(x1024(f), y1024(349F), x1024(272F), y1024(48F), 0, i18n("misc.Screenshot_type"));
                draw(x1024(340F - 30F * f3), y1024(349F + 30F * f2), x1024(272F), y1024(48F), 0, "tga");
                draw(x1024(340F - 15F * f3), y1024(349F - 35F * f2), x1024(272F), y1024(48F), 0, "tga+jpg");
                draw(x1024(340F + 30F * f3), y1024(349F + 30F * f2), x1024(272F), y1024(48F), 0, "jpg");
                draw(x1024(f), y1024(436F), x1024(272F), y1024(48F), 0, i18n("misc.MapAlpha"));
                draw(x1024(f - 280F), y1024(465F), x1024(272F), y1024(48F), 2, i18n("misc.transparent"));
                draw(x1024(f + 245F), y1024(465F), x1024(272F), y1024(48F), 0, i18n("misc.opaque"));
            } else
            if(page == 6)
            {
                draw(x1024(f), y1024(30F), x1024(272F), y1024(48F), 0, i18n("misc.SubTitles"));
                draw(x1024(f), y1024(88F), x1024(272F), y1024(48F), 0, i18n("misc.HudLog"));
                draw(x1024(f), y1024(146F), x1024(272F), y1024(48F), 0, i18n("misc.MissionInfoHud"));
                draw(x1024(f), y1024(204F), x1024(272F), y1024(48F), 0, i18n("misc.KillInfoHud"));
                draw(x1024(f), y1024(262F), x1024(272F), y1024(48F), 0, i18n("misc.ShowMorseAsText"));
                draw(x1024(f), y1024(320F), x1024(272F), y1024(48F), 0, i18n("misc.RecordingIndicator"));
            } else
            if(page == 1)
            {
                draw(x1024(f), y1024(30F), x1024(272F), y1024(48F), 0, i18n("misc.allowCustomSounds"));
                draw(x1024(f), y1024(88F), x1024(272F), y1024(48F), 0, i18n("misc.allowMorseAsText"));
            } else
            if(page == 2)
            {
                draw(x1024(f), y1024(30F), x1024(272F), y1024(48F), 0, i18n("misc.disableNetStatStatistics"));
                draw(x1024(f), y1024(88F), x1024(272F), y1024(48F), 0, i18n("misc.showPilotNumber"));
                draw(x1024(f), y1024(146F), x1024(272F), y1024(48F), 0, i18n("misc.showPilotPing"));
                draw(x1024(f), y1024(204F), x1024(272F), y1024(48F), 0, i18n("misc.showPilotName"));
                draw(x1024(f), y1024(262F), x1024(272F), y1024(48F), 0, i18n("misc.showPilotScore"));
                draw(x1024(f), y1024(320F), x1024(272F), y1024(48F), 0, i18n("misc.showPilotArmy"));
                draw(x1024(f), y1024(378F), x1024(272F), y1024(48F), 0, i18n("misc.showPilotACDesignation"));
                draw(x1024(f), y1024(436F), x1024(272F), y1024(48F), 0, i18n("misc.showPilotACType"));
                draw(x1024(f), y1024(494F), x1024(272F), y1024(48F), 0, i18n("misc.showTeamScore"));
                draw(x1024(f), y1024(552F), x1024(272F), y1024(48F), 0, i18n("misc.cumulativeTeamScore"));
            } else
            if(page == 3)
            {
                draw(x1024(400F), y1024(30F), x1024(272F), y1024(48F), 1, i18n("misc.movement_speed"));
                draw(x1024(300F), y1024(88F), x1024(272F), y1024(48F), 0, i18n("misc.leanFB"));
                draw(x1024(300F), y1024(146F), x1024(272F), y1024(48F), 0, i18n("misc.leanLR"));
                draw(x1024(300F), y1024(204F), x1024(272F), y1024(48F), 0, i18n("misc.raiseUD"));
                draw(x1024(500F), y1024(88F), x1024(272F), y1024(48F), 0, i18n("misc.min"));
                draw(x1024(500F), y1024(146F), x1024(272F), y1024(48F), 0, i18n("misc.min"));
                draw(x1024(500F), y1024(204F), x1024(272F), y1024(48F), 0, i18n("misc.min"));
                draw(x1024(780F), y1024(88F), x1024(272F), y1024(48F), 0, i18n("misc.max"));
                draw(x1024(780F), y1024(146F), x1024(272F), y1024(48F), 0, i18n("misc.max"));
                draw(x1024(780F), y1024(204F), x1024(272F), y1024(48F), 0, i18n("misc.max"));
                GUISeparate.draw(this, GColor.Gray, x1024(300F), y1024(291F), x1024(500F), 2.0F);
                draw(x1024(300F), y1024(320F), x1024(272F), y1024(48F), 0, i18n("misc.rubberBand"));
                draw(x1024(500F), y1024(320F), x1024(272F), y1024(48F), 0, i18n("misc.min"));
                draw(x1024(780F), y1024(320F), x1024(272F), y1024(48F), 0, i18n("misc.max"));
            } else
            if(page == 5)
            {
                draw(x1024(f), y1024(88F), x1024(272F), y1024(48F), 0, i18n("misc.UI_Color"));
                draw(x1024(340F - 22F * f3), y1024(88F + 30F * f2), x1024(272F), y1024(48F), 0, "1");
                draw(x1024(340F - 28F * f3), y1024(88F - 5F * f2), x1024(272F), y1024(48F), 0, "2");
                draw(x1024(340F - 14F * f3), y1024(88F - 33F * f2), x1024(272F), y1024(48F), 0, "3");
                draw(x1024(340F + 14F * f3), y1024(88F - 33F * f2), x1024(272F), y1024(48F), 0, "4");
                draw(x1024(340F + 28F * f3), y1024(88F - 5F * f2), x1024(272F), y1024(48F), 0, "5");
                draw(x1024(340F + 22F * f3), y1024(88F + 30F * f2), x1024(272F), y1024(48F), 0, "6");
                draw(x1024(f), y1024(204F), x1024(272F), y1024(48F), 0, i18n("misc.UI_Detail"));
                draw(x1024(340F - 22F * f3), y1024(204F + 30F * f2), x1024(272F), y1024(48F), 0, "1");
                draw(x1024(340F - 28F * f3), y1024(204F - 5F * f2), x1024(272F), y1024(48F), 0, "2");
                draw(x1024(340F - 14F * f3), y1024(204F - 33F * f2), x1024(272F), y1024(48F), 0, "3");
                draw(x1024(340F + 14F * f3), y1024(204F - 33F * f2), x1024(272F), y1024(48F), 0, "4");
                draw(x1024(340F + 28F * f3), y1024(204F - 5F * f2), x1024(272F), y1024(48F), 0, "5");
                draw(x1024(340F + 22F * f3), y1024(204F + 30F * f2), x1024(272F), y1024(48F), 0, "6");
                draw(x1024(f), y1024(320F), x1024(272F), y1024(48F), 0, i18n("misc.UI_Background"));
                draw(x1024(340F - 25F * f3), y1024(320F + 30F * f2), x1024(272F), y1024(48F), 0, "en");
                draw(x1024(340F - 30F * f3), y1024(320F - 15F * f2), x1024(272F), y1024(48F), 0, "ru");
                draw(x1024(340F), y1024(320F - 33F * f2), x1024(272F), y1024(48F), 0, "de");
                draw(x1024(340F + 30F * f3), y1024(320F - 15F * f2), x1024(272F), y1024(48F), 0, "jp");
                draw(x1024(340F + 25F * f3), y1024(320F + 30F * f2), x1024(272F), y1024(48F), 0, "rnd");
            } else
            if(page == 7)
            {
                float f1 = 300F;
                draw(x1024(f1), y1024(30F), x1024(272F), y1024(48F), 0, "Icon Units");
                draw(x1024(f1 + 195F), y1024(30F), x1024(272F), y1024(48F), 0, "Meters");
                draw(x1024(f1 + 385F), y1024(30F), x1024(272F), y1024(48F), 0, "Feet");
                draw(x1024(f1), y1024(15F + 100F), x1024(120F), y1024(48F), 1, "DISTANCES (m)");
                draw(x1024(f1 + 185F), y1024(15F + 100F), x1024(272F), y1024(48F), 0, i18n("quick.FRI"));
                draw(x1024(f1 + 375F), y1024(15F + 100F), x1024(272F), y1024(48F), 0, i18n("quick.ENM"));
                draw(x1024(f1), y1024(58F + 100F), x1024(120F), y1024(48F), 1, "Dot");
                draw(x1024(f1), y1024(111F + 100F), x1024(120F), y1024(48F), 1, "Color");
                draw(x1024(f1), y1024(164F + 100F), x1024(120F), y1024(48F), 1, "Type");
                draw(x1024(f1), y1024(217F + 100F), x1024(120F), y1024(48F), 1, "Name");
                draw(x1024(f1), y1024(270F + 100F), x1024(120F), y1024(48F), 1, "Id");
                draw(x1024(f1), y1024(323F + 100F), x1024(120F), y1024(48F), 1, "Range");
                draw(x1024(f1), y1024(376F + 100F), x1024(120F), y1024(48F), 1, "Alt. Icon");
                draw(x1024(f1), y1024(429F + 100F), x1024(120F), y1024(48F), 1, "Alt. Color");
                draw(x1024(f1), y1024(482F + 100F), x1024(120F), y1024(48F), 1, "Alt. Symbol");
                draw(x1024(f1), y1024(656F), x1024(120F), y1024(48F), 1, "Reset to Defaults");
            }
            draw(x1024(96F), y1024(656F), x1024(288F), y1024(48F), 0, i18n("misc.Apply_Back"));
        }

        private void initResource()
        {
            try
            {
                resource = ResourceBundle.getBundle("i18n/controls", RTSConf.cur.locale, LDRres.loader());
            }
            catch(Exception exception) { }
        }

        private String resName(String s)
        {
            if(resource == null)
                return s;
            try
            {
                return resource.getString(s);
            }
            catch(Exception exception)
            {
                return s;
            }
        }

        private void setPage(int i)
        {
            page = i;
            set(sChatter, 0);
            set(sParatrooperViews, 0);
            set(sEnableMorseChat, 0);
            set(sSmallMapWPLabels, 0);
            set(sSaveTrk, 0);
            set(sScreenshotType, 0);
            set(sliderMapAlpha, 0);
            set(sSubTitles, 6);
            set(sHudLog, 6);
            set(sMissionInfoHud, 6);
            set(sKillInfoHud, 6);
            set(sShowMorseAsText, 6);
            set(sRecordingIndicator, 6);
            set(sAllowCustomSounds, 1);
            set(sAllowMorseAsText, 1);
            set(sDisableNetStatStatistics, 2);
            set(sShowPilotNumber, 2);
            set(sShowPilotPing, 2);
            set(sShowPilotName, 2);
            set(sShowPilotScore, 2);
            set(sShowPilotArmy, 2);
            set(sShowTeamScore, 2);
            set(sCumulativeTeamScore, 2);
            set(sShowPilotACDesignation, 2);
            set(sShowPilotACType, 2);
            set(sliderLeanFB, 3);
            set(sliderLeanLR, 3);
            set(sliderRaiseUD, 3);
            set(sliderRubberBand, 3);
            set(sUIColor, 5);
            set(sUIDetail, 5);
            set(sUIBackground, 5);
            set(wDot, 7);
            set(wColor, 7);
            set(wType, 7);
            set(wName, 7);
            set(wId, 7);
            set(wRange, 7);
            set(wAltIcon, 7);
            set(wAltColor, 7);
            set(wAltSymbol, 7);
            set(wDotE, 7);
            set(wColorE, 7);
            set(wTypeE, 7);
            set(wNameE, 7);
            set(wIdE, 7);
            set(wRangeE, 7);
            set(wAltIconE, 7);
            set(wAltColorE, 7);
            set(wAltSymbolE, 7);
            set(bResetIconsFriend, 7);
            set(bResetIconsFoe, 7);
            set(sIconUnits, 7);
            setGreenButtonPos();
        }

        public void setGreenButtonPos()
        {
            bGeneral.setEnable(page != 0);
            bHUD.setEnable(page != 6);
            bServer.setEnable(page != 1);
            bOnlineStats.setEnable(page != 2);
            b6DoF.setEnable(page != 3);
            bGUI.setEnable(page != 5);
            bIcons.setEnable(page != 7);
            switch(page)
            {
            case 0: // '\0'
                bGreen.setPosC(x1024(56F), y1024(54F));
                break;

            case 6: // '\006'
                bGreen.setPosC(x1024(56F), y1024(118F));
                break;

            case 2: // '\002'
                bGreen.setPosC(x1024(56F), y1024(182F));
                break;

            case 1: // '\001'
                bGreen.setPosC(x1024(56F), y1024(246F));
                break;

            case 3: // '\003'
                bGreen.setPosC(x1024(56F), y1024(310F));
                break;

            case 5: // '\005'
                bGreen.setPosC(x1024(56F), y1024(374F));
                break;

            case 7: // '\007'
                bGreen.setPosC(x1024(56F), y1024(438F));
                break;
            }
            bGreen.setEnable(true);
            bGreen.setKeyFocus();
        }

        public void setPosSize()
        {
            set1024PosSize(96F, 32F, 832F, 736F);
            bGeneral.setPosC(x1024(56F), y1024(54F));
            bHUD.setPosC(x1024(56F), y1024(118F));
            bOnlineStats.setPosC(x1024(56F), y1024(182F));
            bServer.setPosC(x1024(56F), y1024(246F));
            b6DoF.setPosC(x1024(56F), y1024(310F));
            bGUI.setPosC(x1024(56F), y1024(374F));
            bIcons.setPosC(x1024(56F), y1024(438F));
            bGreen.setPosC(x1024(56F), y1024(54F));
            float f = 350F;
            float f1 = lookAndFeel().getHSliderIntH();
            sChatter.setPosC(x1024(f), y1024(54F));
            sParatrooperViews.setPosC(x1024(f), y1024(112F));
            sEnableMorseChat.setPosC(x1024(f), y1024(170F));
            sSmallMapWPLabels.setPosC(x1024(f), y1024(228F));
            sSaveTrk.setPosC(x1024(f), y1024(286F));
            sScreenshotType.setPosC(x1024(f - 5F), y1024(373F));
            sliderMapAlpha.setPosSize(x1024(f + 50F), y1024(465F) + (y1024(32F) - f1), x1024(240F), f1);
            sUIColor.setPosC(x1024(f - 5F), y1024(112F));
            sUIDetail.setPosC(x1024(f - 5F), y1024(228F));
            sUIBackground.setPosC(x1024(f - 5F), y1024(344F));
            sSubTitles.setPosC(x1024(f), y1024(54F));
            sHudLog.setPosC(x1024(f), y1024(112F));
            sMissionInfoHud.setPosC(x1024(f), y1024(170F));
            sKillInfoHud.setPosC(x1024(f), y1024(228F));
            sShowMorseAsText.setPosC(x1024(f), y1024(286F));
            sRecordingIndicator.setPosC(x1024(f), y1024(344F));
            sAllowCustomSounds.setPosC(x1024(f), y1024(54F));
            sAllowMorseAsText.setPosC(x1024(f), y1024(112F));
            sDisableNetStatStatistics.setPosC(x1024(f), y1024(54F));
            sShowPilotNumber.setPosC(x1024(f), y1024(112F));
            sShowPilotPing.setPosC(x1024(f), y1024(170F));
            sShowPilotName.setPosC(x1024(f), y1024(228F));
            sShowPilotScore.setPosC(x1024(f), y1024(286F));
            sShowPilotArmy.setPosC(x1024(f), y1024(344F));
            sShowPilotACDesignation.setPosC(x1024(f), y1024(402F));
            sShowPilotACType.setPosC(x1024(f), y1024(460F));
            sShowTeamScore.setPosC(x1024(f), y1024(518F));
            sCumulativeTeamScore.setPosC(x1024(f), y1024(576F));
            sliderLeanFB.setPosSize(x1024(535F), y1024(88F) + (y1024(32F) - f1), x1024(240F), f1);
            sliderLeanLR.setPosSize(x1024(535F), y1024(146F) + (y1024(32F) - f1), x1024(240F), f1);
            sliderRaiseUD.setPosSize(x1024(535F), y1024(204F) + (y1024(32F) - f1), x1024(240F), f1);
            sliderRubberBand.setPosSize(x1024(535F), y1024(320F) + (y1024(32F) - f1), x1024(240F), f1);
            sIconUnits.setPosC(x1024(625F), y1024(55F));
            wDot.setPosSize(x1024(485F), y1024(83F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wColor.setPosSize(x1024(485F), y1024(136F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wType.setPosSize(x1024(485F), y1024(189F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wName.setPosSize(x1024(485F), y1024(242F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wId.setPosSize(x1024(485F), y1024(295F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wRange.setPosSize(x1024(485F), y1024(348F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wAltIcon.setPosSize(x1024(485F), y1024(401F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wAltColor.setPosSize(x1024(485F), y1024(454F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wAltSymbol.setPosSize(x1024(485F), y1024(507F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wDotE.setPosSize(x1024(675F), y1024(83F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wColorE.setPosSize(x1024(675F), y1024(136F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wTypeE.setPosSize(x1024(675F), y1024(189F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wNameE.setPosSize(x1024(675F), y1024(242F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wIdE.setPosSize(x1024(675F), y1024(295F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wRangeE.setPosSize(x1024(675F), y1024(348F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wAltIconE.setPosSize(x1024(675F), y1024(401F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wAltColorE.setPosSize(x1024(675F), y1024(454F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            wAltSymbolE.setPosSize(x1024(675F), y1024(507F + 100F) + (y1024(10F) - M(1.7F)), x1024(100F), M(1.7F));
            bResetIconsFriend.setPosC(x1024(525F), y1024(680F));
            bResetIconsFoe.setPosC(x1024(715F), y1024(680F));
            bBack.setPosC(x1024(56F), y1024(680F));
        }

        ResourceBundle resource;

        public DialogClient()
        {
        }
    }


    public void _enter()
    {
        update();
        client.activateWindow();
    }

    public void _leave()
    {
        client.hideWindow();
    }

    public void update()
    {
        sUIColor.setState(Config.cur.windowsUIColor, false);
        sUIDetail.setState(Config.cur.windowsUIDetail, false);
        sUIBackground.setState(Config.cur.windowsUIBackground, false);
        int i = Config.cur.screenshotType;
        if(i == 1)
            i = 2;
        else
        if(i == 2)
            i = 1;
        if(i > 2)
            i = 2;
        sScreenshotType.setState(i, false);
        sSubTitles.setChecked(!Config.cur.bNoSubTitles, false);
        sChatter.setChecked(!Config.cur.bNoChatter, false);
        sHudLog.setChecked(!Config.cur.bNoHudLog, false);
        sMissionInfoHud.setChecked(!World.cur().noMissionInfoHud, false);
        sKillInfoHud.setChecked(!World.cur().noKillInfoHud, false);
        sParatrooperViews.setChecked(!World.cur().noParaTrooperViews, false);
        sShowMorseAsText.setChecked(World.cur().showMorseAsText, false);
        sEnableMorseChat.setChecked(!World.cur().blockMorseChat, false);
        sSmallMapWPLabels.setChecked(World.cur().smallMapWPLabels, false);
        sRecordingIndicator.setChecked(Config.cur.bRec, false);
        sSaveTrk.setChecked(Config.cur.saveTrk, false);
        sliderMapAlpha.setPos((int)(Config.cur.mapAlpha * 100F), false);
        if(Main.cur().netServerParams == null)
        {
            new NetServerParams("load");
            allowNSPEdit = true;
        } else
        {
            allowNSPEdit = false;
        }
        sAllowCustomSounds.setChecked(Main.cur().netServerParams.allowCustomSounds, false);
        sAllowMorseAsText.setChecked(Main.cur().netServerParams.allowMorseAsText, false);
        sDisableNetStatStatistics.setChecked(Main.cur().netServerParams.netStat_DisableStatistics, false);
        sShowPilotNumber.setChecked(Main.cur().netServerParams.netStat_ShowPilotNumber, false);
        sShowPilotPing.setChecked(Main.cur().netServerParams.netStat_ShowPilotPing, false);
        sShowPilotName.setChecked(Main.cur().netServerParams.netStat_ShowPilotName, false);
        sShowPilotScore.setChecked(Main.cur().netServerParams.netStat_ShowPilotScore, false);
        sShowPilotArmy.setChecked(Main.cur().netServerParams.netStat_ShowPilotArmy, false);
        sShowTeamScore.setChecked(Main.cur().netServerParams.netStat_ShowTeamScore, false);
        sCumulativeTeamScore.setChecked(Main.cur().netServerParams.netStat_CumulativeTeamScore, false);
        sShowPilotACDesignation.setChecked(Main.cur().netServerParams.netStat_ShowPilotACDesignation, false);
        sShowPilotACType.setChecked(Main.cur().netServerParams.netStat_ShowPilotACType, false);
        enableAndDisableStats();
        sAllowCustomSounds.setEnable(allowNSPEdit);
        sAllowMorseAsText.setEnable(allowNSPEdit);
        sDisableNetStatStatistics.setEnable(allowNSPEdit);
        sShowPilotNumber.setEnable(allowNSPEdit);
        sShowPilotPing.setEnable(allowNSPEdit);
        sShowPilotName.setEnable(allowNSPEdit);
        sShowPilotScore.setEnable(allowNSPEdit);
        sShowPilotArmy.setEnable(allowNSPEdit);
        sShowTeamScore.setEnable(allowNSPEdit);
        sCumulativeTeamScore.setEnable(allowNSPEdit);
        sShowPilotACDesignation.setEnable(allowNSPEdit);
        sShowPilotACType.setEnable(allowNSPEdit);
        sSaveTrk.setEnable(!Mission.isPlaying());
        sliderLeanFB.setPos((int)(HookView.koofLeanF * 2.0F * 100F) - 1, false);
        sliderLeanLR.setPos((int)(HookView.koofLeanS * 2.0F * 100F) - 1, false);
        sliderRaiseUD.setPos((int)(HookView.koofRaise * 2.0F * 100F) - 1, false);
        sliderRubberBand.setPos(100 - (int)(HookView.rubberBandStretch * 12.5F * 100F), false);
        sIconUnits.setChecked(Config.cur.iconUnits == 0, false);
        loadIcons();
    }

    private void enableAndDisableStats()
    {
        sShowPilotNumber.setEnable(!sDisableNetStatStatistics.isChecked());
        sShowPilotPing.setEnable(!sDisableNetStatStatistics.isChecked());
        sShowPilotName.setEnable(!sDisableNetStatStatistics.isChecked());
        sShowPilotScore.setEnable(!sDisableNetStatStatistics.isChecked());
        sShowPilotArmy.setEnable(!sDisableNetStatStatistics.isChecked());
        sShowPilotACDesignation.setEnable(!sDisableNetStatStatistics.isChecked());
        sShowPilotACType.setEnable(!sDisableNetStatStatistics.isChecked());
        sShowTeamScore.setEnable(!sDisableNetStatStatistics.isChecked());
        sCumulativeTeamScore.setEnable(!sDisableNetStatStatistics.isChecked());
    }

    private void set(GWindow gwindow, int i)
    {
        if(gwindow == null)
            return;
        if(page == i)
            gwindow.showWindow();
        else
            gwindow.hideWindow();
    }

    private void setUIColor()
    {
        int i = sUIColor.getState();
        Config.cur.windowsUIColor = i;
        dialogClient.doReloadTextures();
        dialogClient.lAF().reloadTextures();
    }

    private void setUIDetail()
    {
        int i = sUIDetail.getState();
        Config.cur.windowsUIDetail = i;
        dialogClient.doReloadTextures();
    }

    private void setUIBackground()
    {
        int i = sUIBackground.getState();
        Config.cur.windowsUIBackground = i;
        gwRoot.doResolutionChanged();
        dialogClient.setGreenButtonPos();
    }

    private void setScreenshotType()
    {
        int i = sScreenshotType.getState();
        int j = i;
        if(i == 1)
            j = 2;
        else
        if(i == 2)
            j = 1;
        Config.cur.screenshotType = j;
    }

    private void applyChanges()
    {
        Config.cur.bNoSubTitles = !sSubTitles.isChecked();
        Config.cur.bNoChatter = !sChatter.isChecked();
        Config.cur.bNoHudLog = !sHudLog.isChecked();
        Config.cur.bRec = sRecordingIndicator.isChecked();
        Config.cur.saveTrk = sSaveTrk.isChecked();
        Voice.setEnableVoices(!Config.cur.bNoChatter);
        World.cur().noParaTrooperViews = !sParatrooperViews.isChecked();
        World.cur().noMissionInfoHud = !sMissionInfoHud.isChecked();
        World.cur().noKillInfoHud = !sKillInfoHud.isChecked();
        World.cur().showMorseAsText = sShowMorseAsText.isChecked();
        World.cur().smallMapWPLabels = sSmallMapWPLabels.isChecked();
        World.cur().blockMorseChat = !sEnableMorseChat.isChecked();
        if(GUI.pad != null)
            GUI.pad.setFont(World.cur().smallMapWPLabels);
        if(allowNSPEdit)
        {
            Main.cur().netServerParams.allowCustomSounds = sAllowCustomSounds.isChecked();
            Main.cur().netServerParams.allowMorseAsText = sAllowMorseAsText.isChecked();
            Main.cur().netServerParams.netStat_DisableStatistics = sDisableNetStatStatistics.isChecked();
            Main.cur().netServerParams.netStat_ShowPilotNumber = sShowPilotNumber.isChecked();
            Main.cur().netServerParams.netStat_ShowPilotPing = sShowPilotPing.isChecked();
            Main.cur().netServerParams.netStat_ShowPilotName = sShowPilotName.isChecked();
            Main.cur().netServerParams.netStat_ShowPilotScore = sShowPilotScore.isChecked();
            Main.cur().netServerParams.netStat_ShowPilotArmy = sShowPilotArmy.isChecked();
            Main.cur().netServerParams.netStat_ShowTeamScore = sShowTeamScore.isChecked();
            Main.cur().netServerParams.netStat_CumulativeTeamScore = sCumulativeTeamScore.isChecked();
            Main.cur().netServerParams.netStat_ShowPilotACDesignation = sShowPilotACDesignation.isChecked();
            Main.cur().netServerParams.netStat_ShowPilotACType = sShowPilotACType.isChecked();
            Main.cur().netServerParams.save();
            Main.cur().netServerParams.destroy();
        }
        HookView.koofLeanF = ((float)sliderLeanFB.pos * 0.01F + 0.01F) * 0.5F;
        HookView.koofLeanS = ((float)sliderLeanLR.pos * 0.01F + 0.01F) * 0.5F;
        HookView.koofRaise = ((float)sliderRaiseUD.pos * 0.01F + 0.01F) * 0.5F;
        HookView.rubberBandStretch = (float)(100 - sliderRubberBand.pos) * 0.01F * 0.08F;
        HookView.saveConfig();
        Config.cur.mapAlpha = (float)sliderMapAlpha.pos / 100F;
        Config.cur.iconUnits = sIconUnits.isChecked() ? 0 : 1;
        saveIconsFile();
    }

    void setDefaultIcons(int i)
    {
        if(i == 1)
        {
            wDot.setValue("14000");
            wColor.setValue("6000");
            wType.setValue("6000");
            wName.setValue("6000");
            wId.setValue("6000");
            wRange.setValue("6000");
            wAltIcon.setValue("100");
            wAltColor.setSelected(0, true, false);
            wAltSymbol.setValue("+");
        } else
        {
            wDotE.setValue("14000");
            wColorE.setValue("6000");
            wTypeE.setValue("6000");
            wNameE.setValue("6000");
            wIdE.setValue("6000");
            wRangeE.setValue("6000");
            wAltIconE.setValue("100");
            wAltColorE.setSelected(0, true, false);
            wAltSymbolE.setValue("+");
        }
    }

    void loadIcons()
    {
        String s = "users/" + World.cur().userCfg.sId + "/Icons";
        if(existFile(s))
        {
            try
            {
                File file = new File(s);
                BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
                String s1;
                for(int i = 0; (s1 = bufferedreader.readLine()) != null; i++)
                {
//                    System.out.println(" DOT RANGE line   =   " + s1);
                    for(StringTokenizer stringtokenizer = new StringTokenizer(s1); stringtokenizer.hasMoreTokens();)
                        if(i == 0)
                        {
                            stringtokenizer.nextToken();
                            stringtokenizer.nextToken();
                            stringtokenizer.nextToken();
                            float f = Float.parseFloat(stringtokenizer.nextToken());
                            wDot.setValue("" + (int)(f * 1000F));
                            stringtokenizer.nextToken();
                            f = Float.parseFloat(stringtokenizer.nextToken());
                            wColor.setValue("" + (int)(f * 1000F));
                            stringtokenizer.nextToken();
                            f = Float.parseFloat(stringtokenizer.nextToken());
                            wRange.setValue("" + (int)(f * 1000F));
                            stringtokenizer.nextToken();
                            f = Float.parseFloat(stringtokenizer.nextToken());
                            wType.setValue("" + (int)(f * 1000F));
                            stringtokenizer.nextToken();
                            f = Float.parseFloat(stringtokenizer.nextToken());
                            wId.setValue("" + (int)(f * 1000F));
                            stringtokenizer.nextToken();
                            f = Float.parseFloat(stringtokenizer.nextToken());
                            wName.setValue("" + (int)(f * 1000F));
                            stringtokenizer.nextToken();
                            f = Float.parseFloat(stringtokenizer.nextToken());
                            wAltIcon.setValue("" + (int)(f * 1000F));
                            stringtokenizer.nextToken();
                            wAltSymbol.setValue(stringtokenizer.nextToken());
                            stringtokenizer.nextToken();
                            f = Float.parseFloat(stringtokenizer.nextToken());
                            if((int)f < 0 || (int)f >= altColorName.length)
                                f = 0.0F;
                            wAltColor.setSelected((int)f, true, false);
                        } else
                        {
                            stringtokenizer.nextToken();
                            stringtokenizer.nextToken();
                            stringtokenizer.nextToken();
                            float f1 = Float.parseFloat(stringtokenizer.nextToken());
                            wDotE.setValue("" + (int)(f1 * 1000F));
                            stringtokenizer.nextToken();
                            f1 = Float.parseFloat(stringtokenizer.nextToken());
                            wColorE.setValue("" + (int)(f1 * 1000F));
                            stringtokenizer.nextToken();
                            f1 = Float.parseFloat(stringtokenizer.nextToken());
                            wRangeE.setValue("" + (int)(f1 * 1000F));
                            stringtokenizer.nextToken();
                            f1 = Float.parseFloat(stringtokenizer.nextToken());
                            wTypeE.setValue("" + (int)(f1 * 1000F));
                            stringtokenizer.nextToken();
                            f1 = Float.parseFloat(stringtokenizer.nextToken());
                            wIdE.setValue("" + (int)(f1 * 1000F));
                            stringtokenizer.nextToken();
                            f1 = Float.parseFloat(stringtokenizer.nextToken());
                            wNameE.setValue("" + (int)(f1 * 1000F));
                            stringtokenizer.nextToken();
                            f1 = Float.parseFloat(stringtokenizer.nextToken());
                            wAltIconE.setValue("" + (int)(f1 * 1000F));
                            stringtokenizer.nextToken();
                            wAltSymbolE.setValue(stringtokenizer.nextToken());
                            stringtokenizer.nextToken();
                            f1 = Float.parseFloat(stringtokenizer.nextToken());
                            if((int)f1 < 0 || (int)f1 >= altColorName.length)
                                f1 = 0.0F;
                            wAltColorE.setSelected((int)f1, true, false);
                        }

                }

                bufferedreader.close();
            }
            catch(FileNotFoundException filenotfoundexception)
            {
                filenotfoundexception.printStackTrace();
            }
            catch(IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        } else
        {
            setDefaultIcons(1);
            setDefaultIcons(2);
        }
    }

    private void saveIconsFile()
    {
        CmdEnv.top().exec(iconString(true));
        CmdEnv.top().exec(iconString(false));
        String s = "users/" + World.cur().userCfg.sId + "/Icons";
        try
        {
            PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
            printwriter.print(iconString(true));
            printwriter.println();
            printwriter.print(iconString(false));
            printwriter.println();
            printwriter.close();
        }
        catch(IOException ioexception)
        {
            System.out.println("CDR File save failed: " + ioexception.getMessage());
            ioexception.printStackTrace();
        }
    }

    private static boolean existFile(String s)
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

    private String iconString(boolean flag)
    {
        String s;
        if(flag)
        {
            int dot = Integer.parseInt(wDot.getValue().trim().length() == 0 ? "14000" : wDot.getValue());
            int color = Integer.parseInt(wColor.getValue().trim().length() == 0 ? "6000" : wColor.getValue());
            int range = Integer.parseInt(wRange.getValue().trim().length() == 0 ? "6000" : wRange.getValue());
            int type = Integer.parseInt(wType.getValue().trim().length() == 0 ? "6000" : wType.getValue());
            int id = Integer.parseInt(wId.getValue().trim().length() == 0 ? "6000" : wId.getValue());
            int name = Integer.parseInt(wName.getValue().trim().length() == 0 ? "6000" : wName.getValue());
            int icon = Integer.parseInt(wAltIcon.getValue().trim().length() == 0 ? "100" : wAltIcon.getValue());

            if(dot < 5)
                dot = 5;
            if(dot > 25000)
                dot = 25000;
            if(color < 5)
                color = 5;
            if(color > dot)
                color = dot;
            if(type < 5)
                type = 5;
            if(type > dot)
                type = dot;
            if(name < 5)
                name = 5;
            if(name > dot)
                name = dot;
            if(id < 5)
                id = 5;
            if(id > dot)
                id = dot;
            if(range < 5)
                range = 5;
            if(range > dot)
                range = dot;
            if(icon < 5)
                icon = 5;
            if(icon > dot)
                icon = dot;

            wDot.setValue("" + dot);
            wColor.setValue("" + color);
            wRange.setValue("" + range);
            wType.setValue("" + type);
            wId.setValue("" + id);
            wName.setValue("" + name);
            wAltIcon.setValue("" + icon);

            if(wAltSymbol.getValue().trim().length() == 0)
                wAltSymbol.setValue("+");

            if(wAltColor.getSelected() < 0 || wAltColor.getSelected() >= altColorName.length)
                wAltColor.setSelected(0, true, false);

            s = "mp_dotrange FRIENDLY DOT " + (float)Integer.parseInt(wDot.getValue()) * 0.001F + " COLOR " + (float)Integer.parseInt(wColor.getValue()) * 0.001F + " RANGE " + (float)Integer.parseInt(wRange.getValue()) * 0.001F + " TYPE " + (float)Integer.parseInt(wType.getValue()) * 0.001F + " ID " + (float)Integer.parseInt(wId.getValue()) * 0.001F + " NAME " + (float)Integer.parseInt(wName.getValue()) * 0.001F + " ALTICON " + (float)Integer.parseInt(wAltIcon.getValue()) * 0.001F + " ALTSYMBOL " + wAltSymbol.getValue() + " ALTCOLOR " + wAltColor.getSelected();
        } else
        {
            int dot = Integer.parseInt(wDotE.getValue().trim().length() == 0 ? "14000" : wDotE.getValue());
            int color = Integer.parseInt(wColorE.getValue().trim().length() == 0 ? "6000" : wColorE.getValue());
            int range = Integer.parseInt(wRangeE.getValue().trim().length() == 0 ? "6000" : wRangeE.getValue());
            int type = Integer.parseInt(wTypeE.getValue().trim().length() == 0 ? "6000" : wTypeE.getValue());
            int id = Integer.parseInt(wIdE.getValue().trim().length() == 0 ? "6000" : wIdE.getValue());
            int name = Integer.parseInt(wNameE.getValue().trim().length() == 0 ? "6000" : wNameE.getValue());
            int icon = Integer.parseInt(wAltIconE.getValue().trim().length() == 0 ? "100" : wAltIconE.getValue());

            if(dot < 5)
                dot = 5;
            if(dot > 25000)
                dot = 25000;
            if(color < 5)
                color = 5;
            if(color > dot)
                color = dot;
            if(type < 5)
                type = 5;
            if(type > dot)
                type = dot;
            if(name < 5)
                name = 5;
            if(name > dot)
                name = dot;
            if(id < 5)
                id = 5;
            if(id > dot)
                id = dot;
            if(range < 5)
                range = 5;
            if(range > dot)
                range = dot;
            if(icon < 5)
                icon = 5;
            if(icon > dot)
                icon = dot;

            wDotE.setValue("" + dot);
            wColorE.setValue("" + color);
            wRangeE.setValue("" + range);
            wTypeE.setValue("" + type);
            wIdE.setValue("" + id);
            wNameE.setValue("" + name);
            wAltIconE.setValue("" + icon);

            if(wAltSymbolE.getValue().trim().length() == 0)
                wAltSymbolE.setValue("+");

            if(wAltColorE.getSelected() < 0 || wAltColorE.getSelected() >= altColorName.length)
                wAltColorE.setSelected(0, true, false);

            s = "mp_dotrange FOE DOT " + (float)Integer.parseInt(wDotE.getValue()) * 0.001F + " COLOR " + (float)Integer.parseInt(wColorE.getValue()) * 0.001F + " RANGE " + (float)Integer.parseInt(wRangeE.getValue()) * 0.001F + " TYPE " + (float)Integer.parseInt(wTypeE.getValue()) * 0.001F + " ID " + (float)Integer.parseInt(wIdE.getValue()) * 0.001F + " NAME " + (float)Integer.parseInt(wNameE.getValue()) * 0.001F + " ALTICON " + (float)Integer.parseInt(wAltIconE.getValue()) * 0.001F + " ALTSYMBOL " + wAltSymbolE.getValue() + " ALTCOLOR " + wAltColorE.getSelected();
        }
        return s;
    }

    public GUISetupMisc(GWindowRoot gwindowroot)
    {
        super(72);
        page = 0;
        allowNSPEdit = false;
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        dialogClient.initResource();
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        gwRoot = gwindowroot;
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("setup.Misc");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        bGeneral = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bServer = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bOnlineStats = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        b6DoF = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bGUI = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bHUD = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bIcons = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        sUIColor = (GUISwitchN)dialogClient.addControl(new GUISwitch16(dialogClient, new int[] {
            0, 3, 6, 9, 12, 15
        }, new boolean[] {
            true, true, true, true, true, true
        }));
        sUIDetail = (GUISwitchN)dialogClient.addControl(new GUISwitch16(dialogClient, new int[] {
            0, 3, 6, 9, 12, 15
        }, new boolean[] {
            true, true, true, true, true, true
        }));
        sUIBackground = (GUISwitchN)dialogClient.addControl(new GUISwitch16(dialogClient, new int[] {
            0, 4, 8, 11, 15
        }, new boolean[] {
            true, true, true, true, true
        }));
        sScreenshotType = (GUISwitchN)dialogClient.addControl(new GUISwitch16(dialogClient, new int[] {
            0, 8, 15
        }, new boolean[] {
            true, true, true
        }));
        sSubTitles = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sRecordingIndicator = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sChatter = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sHudLog = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sParatrooperViews = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sMissionInfoHud = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sKillInfoHud = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sShowMorseAsText = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sSmallMapWPLabels = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sEnableMorseChat = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sSaveTrk = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sAllowCustomSounds = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sAllowMorseAsText = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sDisableNetStatStatistics = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sShowPilotNumber = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sShowPilotPing = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sShowPilotName = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sShowPilotScore = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sShowPilotArmy = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sShowTeamScore = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sCumulativeTeamScore = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sShowPilotACDesignation = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sShowPilotACType = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        dialogClient.addControl(sliderLeanFB = new GWindowHSliderInt(dialogClient));
        sliderLeanFB.setRange(0, 100, 0);
        dialogClient.addControl(sliderLeanLR = new GWindowHSliderInt(dialogClient));
        sliderLeanLR.setRange(0, 100, 0);
        dialogClient.addControl(sliderRaiseUD = new GWindowHSliderInt(dialogClient));
        sliderRaiseUD.setRange(0, 100, 0);
        dialogClient.addControl(sliderRubberBand = new GWindowHSliderInt(dialogClient));
        sliderRubberBand.setRange(0, 100, 0);
        dialogClient.addControl(sliderMapAlpha = new GWindowHSliderInt(dialogClient));
        sliderMapAlpha.setRange(0, 100, 0);
        bBack = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bGreen = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        wDot = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wDotE = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wColor = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wColorE = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wType = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wTypeE = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wName = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wNameE = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wId = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wIdE = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wRange = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wRangeE = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wAltIcon = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wAltIconE = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wAltColor = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wAltColorE = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wAltSymbol = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wAltSymbolE = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        sIconUnits = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        for(int i = 0; i < altColorName.length; i++)
        {
            wAltColor.add(altColorName[i]);
            wAltColorE.add(altColorName[i]);
        }

        for(int j = 1; j < 26; j++)
        {
            wDot.add("" + j * 1000);
            wColor.add("" + j * 1000);
            wType.add("" + j * 1000);
            wName.add("" + j * 1000);
            wId.add("" + j * 1000);
            wRange.add("" + j * 1000);
            wAltIcon.add("" + j * 1000);
            wDotE.add("" + j * 1000);
            wColorE.add("" + j * 1000);
            wTypeE.add("" + j * 1000);
            wNameE.add("" + j * 1000);
            wIdE.add("" + j * 1000);
            wRangeE.add("" + j * 1000);
            wAltIconE.add("" + j * 1000);
        }
        wAltSymbol.add(".");
        wAltSymbol.add("•");
        wAltSymbol.add("*");
        wAltSymbol.add("¤");
        wAltSymbol.add("+");
        wAltSymbol.add("x");
        wAltSymbol.add("v");
        wAltSymbol.add("Y");
        wAltSymbolE.add(".");
        wAltSymbolE.add("•");
        wAltSymbolE.add("*");
        wAltSymbolE.add("¤");
        wAltSymbolE.add("+");
        wAltSymbolE.add("x");
        wAltSymbolE.add("v");
        wAltSymbolE.add("Y");
        wAltColor.setEditable(false);
        wAltColorE.setEditable(false);
        wDot.setNumericOnly(true);
        wDotE.setNumericOnly(true);
        wColor.setNumericOnly(true);
        wColorE.setNumericOnly(true);
        wType.setNumericOnly(true);
        wTypeE.setNumericOnly(true);
        wName.setNumericOnly(true);
        wNameE.setNumericOnly(true);
        wId.setNumericOnly(true);
        wIdE.setNumericOnly(true);
        wRange.setNumericOnly(true);
        wRangeE.setNumericOnly(true);
        wAltIcon.setNumericOnly(true);
        wAltIconE.setNumericOnly(true);
        wAltSymbol.setEditable(true);
        wAltSymbol.setMaxLength(1);
        wAltSymbolE.setEditable(true);
        wAltSymbolE.setMaxLength(1);
        bResetIconsFriend = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bResetIconsFoe = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        loadIcons();
        dialogClient.setPage(0);
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    private static final int GENERAL = 0;
    private static final int SERVER = 1;
    private static final int ONLINE_STATS = 2;
    private static final int SIX_DOF = 3;
    private static final int UI = 5;
    private static final int HUD = 6;
    private static final int ICONS = 7;
    private GWindowRoot gwRoot;
    private int page;
    private boolean allowNSPEdit;
    public GUIButton bGeneral;
    public GUIButton bHUD;
    public GUIButton bServer;
    public GUIButton bOnlineStats;
    public GUIButton b6DoF;
    public GUIButton bGUI;
    public GUIButton bIcons;
    public GUIButton bBack;
    public GUISwitchBox3 sSubTitles;
    public GUISwitchBox3 sChatter;
    public GUISwitchBox3 sHudLog;
    public GUISwitchBox3 sMissionInfoHud;
    public GUISwitchBox3 sKillInfoHud;
    public GUISwitchBox3 sParatrooperViews;
    public GUISwitchBox3 sShowMorseAsText;
    public GUISwitchBox3 sEnableMorseChat;
    public GUISwitchBox3 sSmallMapWPLabels;
    public GUISwitchBox3 sRecordingIndicator;
    public GUISwitchBox3 sSaveTrk;
    public GUISwitchN sScreenshotType;
    public GWindowHSliderInt sliderMapAlpha;
    public GUISwitchBox3 sAllowCustomSounds;
    public GUISwitchBox3 sAllowMorseAsText;
    public GUISwitchBox3 sDisableNetStatStatistics;
    public GUISwitchBox3 sShowPilotNumber;
    public GUISwitchBox3 sShowPilotPing;
    public GUISwitchBox3 sShowPilotName;
    public GUISwitchBox3 sShowPilotScore;
    public GUISwitchBox3 sShowPilotArmy;
    public GUISwitchBox3 sShowTeamScore;
    public GUISwitchBox3 sCumulativeTeamScore;
    public GUISwitchBox3 sShowPilotACDesignation;
    public GUISwitchBox3 sShowPilotACType;
    public GWindowHSliderInt sliderLeanFB;
    public GWindowHSliderInt sliderLeanLR;
    public GWindowHSliderInt sliderRaiseUD;
    public GWindowHSliderInt sliderRubberBand;
    public GUISwitchN sUIColor;
    public GUISwitchN sUIDetail;
    public GUISwitchN sUIBackground;
    public GWindowComboControl wDot;
    public GWindowComboControl wColor;
    public GWindowComboControl wType;
    public GWindowComboControl wName;
    public GWindowComboControl wId;
    public GWindowComboControl wRange;
    public GWindowComboControl wAltIcon;
    public GWindowComboControl wAltColor;
    public GWindowComboControl wAltSymbol;
    public GWindowComboControl wDotE;
    public GWindowComboControl wColorE;
    public GWindowComboControl wTypeE;
    public GWindowComboControl wNameE;
    public GWindowComboControl wIdE;
    public GWindowComboControl wRangeE;
    public GWindowComboControl wAltIconE;
    public GWindowComboControl wAltColorE;
    public GWindowComboControl wAltSymbolE;
    public GUIButton bResetIconsFriend;
    public GUIButton bResetIconsFoe;
    public GUISwitchBox3 sIconUnits;
    public GUIButton bGreen;
    private static final int ROW_D = 58;
    private static final int ROW_1 = 30;
    private static final int ROW_2 = 88;
    private static final int ROW_3 = 146;
    private static final int ROW_4 = 204;
    private static final int ROW_5 = 262;
    private static final int ROW_6 = 320;
    private static final int ROW_7 = 378;
    private static final int ROW_8 = 436;
    private static final int ROW_9 = 494;
    private static final int ROW_10 = 552;
    private static final int ROW_11 = 610;
    private static final int ROW_12 = 668;
    private static final int ROW_SS = 349;
    private static final int MAIN_ROW_D = 64;
    private static final int MAIN_ROW_1 = 30;
    private static final int MAIN_ROW_2 = 94;
    private static final int MAIN_ROW_3 = 158;
    private static final int MAIN_ROW_4 = 222;
    private static final int MAIN_ROW_5 = 286;
    private static final int MAIN_ROW_6 = 350;
    private static final int MAIN_ROW_7 = 414;
    private String altColorName[] = {
        "Black", "Red", "Blue", "Green", "Gold", "Purple", "Aqua", "Maroon", "Navy", "Emerald", 
        "Olive", "Magenta", "Teal", "Orange", "Turquoise", "Brown", "Salad", "White"
    };
}
