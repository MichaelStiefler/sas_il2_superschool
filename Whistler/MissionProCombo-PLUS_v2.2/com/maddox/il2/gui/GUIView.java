package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.game.*;

public class GUIView extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bAir)
            {
                GUIObjectInspector.type = "air";
                Main.stateStack().push(23);
                return true;
            }
            if(gwindow == bTank)
            {
                GUIObjectInspector.type = "tanks";
                Main.stateStack().push(23);
                return true;
            }
            if(gwindow == bVechicle)
            {
                GUIObjectInspector.type = "vehicle";
                Main.stateStack().push(23);
                return true;
            }
            if(gwindow == bChip)
            {
                GUIObjectInspector.type = "ship";
                Main.stateStack().push(23);
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
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(368F), x1024(320F), 2.0F);
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(118F), y1024(56F) - M(1.0F), x1024(224F), M(2.0F), 0, i18n("view.Aircraft"));
            draw(x1024(118F), y1024(120F) - M(1.0F), x1024(224F), M(2.0F), 0, i18n("view.Tanks"));
            draw(x1024(118F), y1024(184F) - M(1.0F), x1024(224F), M(2.0F), 0, i18n("view.Vehicles"));
            draw(x1024(118F), y1024(248F) - M(1.0F), x1024(224F), M(2.0F), 0, i18n("view.Ships"));
            draw(x1024(118F), y1024(425F) - M(1.0F), x1024(224F), M(2.0F), 0, i18n("view.MainMenu"));
        }

        public void setPosSize()
        {
            set1024PosSize(334F, 176F, 384F, 480F);
            bAir.setPosC(x1024(70F), y1024(56F));
            bTank.setPosC(x1024(70F), y1024(120F));
            bVechicle.setPosC(x1024(70F), y1024(184F));
            bChip.setPosC(x1024(70F), y1024(248F));
            bExit.setPosC(x1024(70F), y1024(425F));
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

    public GUIView(GWindowRoot gwindowroot)
    {
        super(15);
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("view.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bAir = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bTank = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bVechicle = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bChip = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIButton bAir;
    public GUIButton bTank;
    public GUIButton bVechicle;
    public GUIButton bChip;
    public GUIButton bExit;
}
