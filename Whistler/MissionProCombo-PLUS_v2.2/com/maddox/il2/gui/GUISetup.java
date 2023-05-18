package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.game.*;

public class GUISetup extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bVideo)
            {
                Main.stateStack().push(12);
                return true;
            }
            if(gwindow == b3d)
            {
                Main.stateStack().push(11);
                return true;
            }
            if(gwindow == bSound)
            {
                Main.stateStack().push(13);
                return true;
            }
            if(gwindow == bInput)
            {
                Main.stateStack().push(53);
                return true;
            }
            if(gwindow == bNet)
            {
                Main.stateStack().push(52);
                return true;
            }
            if(gwindow == bMisc)
            {
                Main.stateStack().push(72);
                return true;
            }
            if(gwindow == bExit)
            {
                Main.stateStack().pop();
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(368F), x1024(306F), 2.0F);
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(112F), y1024(48F), x1024(208F), y1024(48F), 0, i18n("setup.VideoModes"));
            draw(x1024(112F), y1024(96F), x1024(208F), y1024(48F), 0, i18n("setup.VideoOptions"));
            draw(x1024(112F), y1024(144F), x1024(208F), y1024(48F), 0, i18n("setup.SoundSetup"));
            draw(x1024(112F), y1024(192F), x1024(208F), y1024(48F), 0, i18n("setup.Input"));
            draw(x1024(112F), y1024(240F), x1024(208F), y1024(48F), 0, i18n("setup.Network"));
            draw(x1024(112F), y1024(288F), x1024(208F), y1024(48F), 0, i18n("setup.Misc"));
            draw(x1024(112F), y1024(384F), x1024(208F), y1024(48F), 0, i18n("setup.Back"));
        }

        public void setPosSize()
        {
            set1024PosSize(368F, 207F, 368F, 464F);
            bVideo.setPosC(x1024(64F), y1024(72F));
            b3d.setPosC(x1024(64F), y1024(120F));
            bSound.setPosC(x1024(64F), y1024(168F));
            bInput.setPosC(x1024(64F), y1024(216F));
            bNet.setPosC(x1024(64F), y1024(264F));
            bMisc.setPosC(x1024(64F), y1024(312F));
            bExit.setPosC(x1024(64F), y1024(408F));
        }

        public DialogClient()
        {
        }
    }


    public void _enter()
    {
        client.activateWindow();
    }

    public void _leave()
    {
        client.hideWindow();
    }

    public GUISetup(GWindowRoot gwindowroot)
    {
        super(10);
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("setup.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bVideo = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        b3d = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bSound = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bInput = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bNet = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bMisc = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIButton bVideo;
    public GUIButton b3d;
    public GUIButton bSound;
    public GUIButton bInput;
    public GUIButton bNet;
    public GUIButton bMisc;
    public GUIButton bExit;
}