package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.GameSpy;
import com.maddox.il2.net.USGS;
import com.maddox.rts.*;

public class GUIMainMenu extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bInfo)
            {
                Main.stateStack().push(15);
                return true;
            }
            if(gwindow == bPilot)
            {
                Main.stateStack().push(1);
                return true;
            }
            if(gwindow == bControls)
            {
                Main.stateStack().push(20);
                return true;
            }
            if(gwindow == bSingle)
            {
                Main.stateStack().push(3);
                return true;
            }
            if(gwindow == bCampaigns)
            {
                Main.stateStack().push(27);
                return true;
            }
            if(gwindow == bQuick)
            {
                Main.stateStack().push(14);
                return true;
            }
            if(gwindow == bTraining)
            {
                Main3D.cur3D().viewSet_Save();
                Main.stateStack().push(56);
                return true;
            }
            if(gwindow == bRecord)
            {
                Main3D.cur3D().viewSet_Save();
                Main.stateStack().push(7);
                return true;
            }
            if(gwindow == bBuilder)
            {
                Main.stateStack().push(18);
                return true;
            }
            if(gwindow == bMultiplay)
            {
                Main.stateStack().push(33);
                return true;
            }
            if(gwindow == sMusicOn)
            {
                sMusicOn.update();
                return true;
            }
            if(gwindow == bSetup)
            {
                Main.stateStack().push(10);
                return true;
            }
            if(gwindow == bCredits)
            {
                Main.stateStack().push(16);
                return true;
            }
            if(gwindow == bExit)
            {
                new GWindowMessageBox(root, 20F, true, i18n("main.ConfirmQuit"), i18n("main.ReallyQuit"), 1, 0.0F) {

                    public void result(int k)
                    {
                        if(k == 3)
                            Main.doGameExit();
                        else
                            client.activateWindow();
                    }

                }
;
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(336F), x1024(336F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(496F), x1024(336F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(384F), y1024(32F), 2.0F, y1024(560F));
            GUISeparate.draw(this, GColor.Gray, x1024(400F), y1024(336F), x1024(288F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(400F), y1024(496F), x1024(288F), 2.0F);
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(96F), y1024(64F) - M(1.0F), x1024(256F), M(2.0F), 0, i18n("main.SingleMissions"));
            draw(x1024(96F), y1024(120F) - M(1.0F), x1024(256F), M(2.0F), 0, i18n("main.PilotCareer"));
            draw(x1024(96F), y1024(176F) - M(1.0F), x1024(256F), M(2.0F), 0, i18n("main.Multiplay"));
            draw(x1024(96F), y1024(232F) - M(1.0F), x1024(256F), M(2.0F), 0, i18n("main.Quick"));
            draw(x1024(96F), y1024(288F) - M(1.0F), x1024(256F), M(2.0F), 0, i18n("main.Builder"));
            draw(x1024(96F), y1024(384F) - M(1.0F), x1024(256F), M(2.0F), 0, i18n("main.Training"));
            draw(x1024(96F), y1024(449F) - M(1.0F), x1024(256F), M(2.0F), 0, i18n("main.PlayTrack"));
            GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
            guilookandfeel.drawBevel(this, x1024(152F), y1024(544F) - M(1.0F), x1024(128F), y1024(16F) + M(2.0F), guilookandfeel.bevelRed, guilookandfeel.basicelements);
            draw(x1024(168F), y1024(552F) - M(1.0F), x1024(112F), M(2.0F), 0, i18n("main.Quit"));
            guilookandfeel.drawBevel(this, x1024(416F), y1024(40F), x1024(264F), y1024(64F), guilookandfeel.bevelBlacked, guilookandfeel.basicelements);
            setCanvasFont(1);
            draw(x1024(416F), y1024(40F), x1024(272F), y1024(64F), 1, i18n("main.PilotSelector"));
//            draw(x1024(416F), y1024(33F), x1024(272F), y1024(57F), 1, "Battlefield \267 Airborne \267 Tactical");
//            draw(x1024(416F), y1024(48F), x1024(272F), y1024(72F), 1, "DAWN  OF  FLIGHT");
//            draw(x1024(416F), y1024(48F), x1024(272F), y1024(72F), 1, "THE  GOLDEN  AGE");
//            draw(x1024(416F), y1024(48F), x1024(272F), y1024(72F), 1, "WORLD  AT  WAR");
//            draw(x1024(416F), y1024(48F), x1024(272F), y1024(72F), 1, "THE  JET  AGE");
            setCanvasFont(0);
            draw(x1024(424F), y1024(148F) - M(1.0F), x1024(200F), M(2.0F), 2, i18n("main.Pilot"));
            draw(x1024(424F), y1024(288F) - M(1.0F), x1024(200F), M(2.0F), 2, i18n("main.Controls"));
            draw(x1024(424F), y1024(384F) - M(1.0F), x1024(200F), M(2.0F), 2, i18n("main.ViewObjects"));
            draw(x1024(424F), y1024(449F) - M(1.0F), x1024(200F), M(2.0F), 2, i18n("main.Credits"));
            if("".equalsIgnoreCase(RTSConf.cur.locale.getLanguage()) || "us".equalsIgnoreCase(RTSConf.cur.locale.getLanguage()))
                draw(x1024(464F), y1024(552F) - M(1.0F), x1024(200F), M(2.0F), 0, i18n("sound.Play_music"));
            else
                draw(x1024(464F), y1024(552F) - M(1.0F), x1024(200F), M(2.0F), 0, i18n("sound.Music"));
            draw(x1024(424F), y1024(552F) - M(1.0F), x1024(200F), M(2.0F), 2, i18n("main.Setup"));
        }

        public void setPosSize()
        {
            set1024PosSize(144F, 80F, 720F, 624F);
            bSingle.setPosC(x1024(56F), y1024(64F));
            bCampaigns.setPosC(x1024(56F), y1024(120F));
            bMultiplay.setPosC(x1024(56F), y1024(176F));
            bQuick.setPosC(x1024(56F), y1024(232F));
            bBuilder.setPosC(x1024(56F), y1024(288F));
            bTraining.setPosC(x1024(56F), y1024(384F));
            bRecord.setPosC(x1024(56F), y1024(449F));
            bExit.setPosC(x1024(120F), y1024(552F));
            bPilot.setPosC(x1024(664F), y1024(148F));
            pPilotName.setPosSize(x1024(416F), y1024(204F), x1024(264F), y1024(32F));
            bControls.setPosC(x1024(664F), y1024(288F));
            bInfo.setPosC(x1024(664F), y1024(384F));
            bCredits.setPosC(x1024(664F), y1024(449F));
            sMusicOn.setPosC(x1024(432F), y1024(552F));
            bSetup.setPosC(x1024(664F), y1024(552F));
        }


        public DialogClient()
        {
        }
    }


    public void enterPop(GameState gamestate)
    {
        if(USGS.isUsing())
        {
            Main.doGameExit();
            return;
        }
        if(Main.cur().netGameSpy != null)
        {
            Main.cur().netGameSpy.sendExiting();
            Main.doGameExit();
            return;
        } else
        {
            _enter();
            return;
        }
    }

    public void _enter()
    {
        ((GUIRoot)dialogClient.root).setBackCountry(null, null);
        client.activateWindow();
    }

    public void _leave()
    {
        client.hideWindow();
    }

    public GUIMainMenu(GWindowRoot gwindowroot)
    {
        super(2);
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("main.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bCampaigns = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bSingle = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bQuick = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bMultiplay = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bBuilder = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bTraining = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        bRecord = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bPilot = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bControls = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bInfo = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bCredits = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        sMusicOn = (GUISwitchBox2)dialogClient.addControl(new GUISwitchBox2(dialogClient, (CfgFlags)CfgTools.get("MusFlags"), 0, true));
        bSetup = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        setup = WindowPreferences.create(gwindowroot);
        setup.hideWindow();
        texReg12B = new GTexRegion("GUI/game/staticelements.mat", 128F, 32F, 96F, 48F);
        pPilotName = new GUIPocket(dialogClient, "Demo 'demo' Player");
        dialogClient.activateWindow();
        client.hideWindow();
    }

    GWindowFramed setup;
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GTexRegion texReg12B;
    public GUIPocket pPilotName;
    public GUIButton bInfo;
    public GUIButton bPilot;
    public GUIButton bControls;
    public GUIButton bSingle;
    public GUIButton bCampaigns;
    public GUIButton bQuick;
    public GUIButton bTraining;
    public GUIButton bRecord;
    public GUIButton bBuilder;
    public GUIButton bMultiplay;
    public GUISwitchBox2 sMusicOn;
    public GUIButton bSetup;
    public GUIButton bCredits;
    public GUIButton bExit;
}
