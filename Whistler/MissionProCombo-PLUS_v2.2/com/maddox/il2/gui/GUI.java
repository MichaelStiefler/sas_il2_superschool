package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.rts.*;

public class GUI
{

    public GUI()
    {
    }

    public static void activate()
    {
        activate(true);
    }

    public static void activate(boolean flag)
    {
        GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
        guiwindowmanager.activateKeyboard(true);
        guiwindowmanager.activateMouse(true);
        if(!Mission.isPlaying() || Mission.isSingle() || Main3D.cur3D().isDemoPlaying())
        {
            guiwindowmanager.activateTime(true);
            if(RTSConf.cur.time.isEnableChangePause1())
                Time.setPause(true);
            guiwindowmanager.activateJoy(true);
            RendersMain.setRenderFocus((Render)Actor.getByName("renderGUI"));
        }
        if(flag)
            Main3D.cur3D().disableAllHotKeyCmdEnv();
    }

    public static void activateJoy()
    {
        GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
        guiwindowmanager.activateJoy(true);
    }

    public static void activate(boolean flag, boolean flag1)
    {
        GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
        guiwindowmanager.activateTime(true);
        if(flag)
        {
            guiwindowmanager.activateMouse(true);
            Mouse.adapter().setFocus(guiwindowmanager);
        }
        if(flag1)
        {
            guiwindowmanager.activateKeyboard(true);
            Keyboard.adapter().setFocus(guiwindowmanager);
        }
    }

    public static void unActivate()
    {
        GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
        if(Mouse.adapter().focus() == guiwindowmanager)
            Mouse.adapter().setFocus(null);
        if(Keyboard.adapter().focus() == guiwindowmanager)
            Keyboard.adapter().setFocus(null);
        guiwindowmanager.unActivateAll();
        RendersMain.setRenderFocus(null);
        Main3D.cur3D().enableOnlyGameHotKeyCmdEnvs();
    }

    public static void chatActivate()
    {
        if(chatDlg.isVisible())
        {
            if(Main3D.cur3D().isDemoPlaying())
                return;
            GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
            chatDlg.wEdit.setEditable(true);
            Main3D.cur3D().hud.stopNetStat();
            chatDlg.wEdit.activateWindow();
            if(Keyboard.adapter().focus() == guiwindowmanager)
                return;
            if(guiwindowmanager.isKeyboardActive())
                return;
            activate(true, true);
        }
    }

    public static void chatUnactivate()
    {
        if(chatDlg.isVisible())
        {
            GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
            if(Mouse.adapter().focus() == guiwindowmanager && Keyboard.adapter().focus() == guiwindowmanager)
            {
                if(pad.isActive())
                {
                    guiwindowmanager.activateKeyboard(false);
                    Keyboard.adapter().setFocus(null);
                } else
                {
                    unActivate();
                }
                chatDlg.wEdit.setEditable(false);
            }
        }
    }

    private static void initHotKeys()
    {
        HotKeyCmdEnv.addCmd(envName, new HotKeyCmd(true, "activate") {

            public void end()
            {
                GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
                GameState gamestate = Main.state();
                if(gamestate == null)
                    return;
                if(BackgroundTask.isExecuted())
                    return;
                if(GUI.pad.isActive())
                {
                    GUI.pad.leave(false);
                    return;
                } else
                {
                    RTSConf.cur.hotKeyEnvs.endAllActiveCmd(false);
                    gamestate.doQuitMission();
                    return;
                }
            }

        }
);
    }

    public static GUIWindowManager create(String s)
    {
        envName = s;
        HotKeyEnv.fromIni(envName, Config.cur.ini, "HotKey " + envName);
        initHotKeys();
        GUIRoot guiroot = new GUIRoot();
        GUIWindowManager guiwindowmanager = new GUIWindowManager(-2F, guiroot, new GUILookAndFeel(), "renderGUI");
        guiwindowmanager.activateKeyboard(true);
        guiwindowmanager.activateMouse(true);
        guiwindowmanager.activateTime(true);
        Time.setPause(true);
        guiwindowmanager.activateJoy(true);
        RendersMain.setRenderFocus((Render)Actor.getByName("renderGUI"));
        Main3D.cur3D().beginStep(50);
        new GUIPlayerSelect(guiroot);
        GUIMainMenu guimainmenu = new GUIMainMenu(guiroot);
        new GUISingleSelect(guiroot);
        new GUISingleBriefing(guiroot);
        new GUISingleMission(guiroot);
        new GUISingleComplete(guiroot);
        Main3D.cur3D().beginStep(55);
        new GUISingleStat(guiroot);
        new GUIRecordSelect(guiroot);
        new GUIRecordPlay(guiroot);
        new GUIRecordSave(guiroot);
        new GUIRecordNetSave(guiroot);
        new GUISetup(guiroot);
        Main3D.cur3D().beginStep(60);
        new GUISetup3D1(guiroot);
        new GUISetupVideo(guiroot);
        new GUISetupSound(guiroot);
        new GUISetupNet(guiroot);
        new GUISetupMisc(guiroot);
        new GUISetupInput(guiroot);
        new GUIQuick(guiroot);
        Main3D.cur3D().beginStep(65);
        new GUIView(guiroot);
        new GUICredits(guiroot);
        new GUIDifficulty(guiroot);
        Main3D.cur3D().beginStep(70);
        new GUIBuilder(guiroot);
        Main3D.cur3D().beginStep(75);
        new GUIControls(guiroot);
        pad = new GUIPad(guiroot);
        new GUIObjectInspector(guiroot);
        Main3D.cur3D().beginStep(80);
        new GUIObjectView(guiroot);
        new GUIQuickLoad(guiroot);
        new GUIQuickSave(guiroot);
        new GUICampaigns(guiroot);
        new GUICampaignNew(guiroot);
        Main3D.cur3D().beginStep(85);
        new GUICampaignBriefing(guiroot);
        new GUICampaignMission(guiroot);
        new GUICampaignStat(guiroot);
        new GUICampaignStatView(guiroot);
        new GUIAwards(guiroot);
        new GUIQuickStats(guiroot);
        new GUINet(guiroot);
        new GUINetNewClient(guiroot);
        new GUINetNewServer(guiroot);
        new GUINetClient(guiroot);
        new GUINetServer(guiroot);
        new GUINetServerMisSelect(guiroot);
        new GUINetServerDBrief(guiroot);
        new GUINetClientDBrief(guiroot);
        new GUINetDifficulty(guiroot);
        new GUINetServerDMission(guiroot);
        new GUINetClientDMission(guiroot);
        new GUINetServerCBrief(guiroot);
        new GUINetClientCBrief(guiroot);
        new GUINetServerCMission(guiroot);
        new GUINetClientCMission(guiroot);
        new GUINetAircraft(guiroot);
        new GUINetCScore(guiroot);
        new GUINetCStart(47, guiroot);
        new GUINetCStart(48, guiroot);
        new GUIArming(guiroot);
        new GUIAirArming(guiroot);
        new GUIAirViewer(guiroot);
        new GUITrainingSelect(guiroot);
        new GUITrainingPlay(guiroot);
        new GUIBWDemoPlay(guiroot);
        new GUIDGenNew(guiroot);
        new GUIDGenBriefing(guiroot);
        new GUIDGenMission(guiroot);
        new GUIDGenDeBriefing(guiroot);
        new GUIDGenRoster(guiroot);
        new GUIDGenDocs(guiroot);
        new GUIDGenPilot(guiroot);
        new GUIDGenPilotDetail(guiroot);
        new GUINetServerNGenSelect(guiroot);
        new GUINetServerNGenProp(guiroot);
        chatDlg = new GUIChatDialog(guiroot);
        chatDlg.hideWindow();
        Main3D.cur3D().beginStep(90);
        guiroot.lAF().bSoundEnable = true;
        Main3D.cur3D().enableOnlyHotKeyCmdEnv(envName);
        GUIMainMenu guimainmenu1 = (GUIMainMenu)GameState.get(2);
        guimainmenu1.pPilotName.cap = new GCaption(World.cur().userCfg.name + " '" + World.cur().userCfg.callsign + "' " + World.cur().userCfg.surname);
        Main.stateStack().push(guimainmenu);
        guiroot.C.alpha = 224;
        return guiwindowmanager;
    }

    public static String envName;
    public static GUIPad pad;
    public static GUIChatDialog chatDlg;
}