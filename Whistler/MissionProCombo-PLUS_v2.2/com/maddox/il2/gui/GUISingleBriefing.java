package com.maddox.il2.gui;

import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.*;
import com.maddox.il2.game.*;
import com.maddox.rts.CmdEnv;

public class GUISingleBriefing extends GUIBriefing
{
    public void enterPush(GameState gamestate)
    {
        World.cur().diffUser.set(World.cur().userCfg.singleDifficulty);
        clientRender();
        super.enterPush(gamestate);
        if(super.briefSound != null)
        {
            CmdEnv.top().exec("music PUSH");
            CmdEnv.top().exec("music LIST " + super.briefSound);
            CmdEnv.top().exec("music PLAY");
        }
    }

    public void enterPop(GameState gamestate)
    {
        if(gamestate.id() == 17)
        {
            World.cur().userCfg.singleDifficulty = World.cur().diffUser.get();
            World.cur().userCfg.saveConf();
        }
        super.client.activateWindow();
        super.enterPop(gamestate);
    }

    protected void doNext()
    {
        if(super.briefSound != null)
        {
            CmdEnv.top().exec("music POP");
            CmdEnv.top().exec("music STOP");
        }
        Main.stateStack().change(5);
    }

    protected void doLoodout()
    {
        Main.stateStack().push(54);
    }

    protected void doDiff()
    {
        Main.stateStack().push(17);
    }

    protected void doBack()
    {
        if(super.briefSound != null)
        {
            CmdEnv.top().exec("music POP");
            CmdEnv.top().exec("music PLAY");
        }
        Main.stateStack().pop();
    }

    protected void clientRender()
    {
        GUIBriefingGeneric.DialogClient dialogclient = super.dialogClient;
        dialogclient.draw(dialogclient.x1024(0.0F), dialogclient.y1024(633F), dialogclient.x1024(170F), dialogclient.y1024(48F), 1, i18n("brief.Back"));
        dialogclient.draw(dialogclient.x1024(194F), dialogclient.y1024(633F), dialogclient.x1024(208F), dialogclient.y1024(48F), 1, i18n("brief.Difficulty"));
        dialogclient.draw(dialogclient.x1024(680F), dialogclient.y1024(633F), dialogclient.x1024(176F), dialogclient.y1024(48F), 1, i18n("brief.Arming"));
        super.clientRender();
    }

    protected void clientSetPosSize()
    {
        GUIBriefingGeneric.DialogClient dialogclient = super.dialogClient;
        super.bLoodout.setPosC(dialogclient.x1024(768F), dialogclient.y1024(689F));
    }

    public GUISingleBriefing(GWindowRoot gwindowroot)
    {
        super(4);
        init(gwindowroot);
    }
}
