package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.game.campaign.Campaign;
import java.io.PrintStream;
import java.util.ArrayList;

public class GUIDGenPilot extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bBack)
            {
                Main.stateStack().pop();
                return true;
            }
            if(gwindow == bDetailProfile)
            {
                if(pilot.bPlayer)
                    Main.stateStack().push(67);
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(96F), y1024(656F), x1024(288F), y1024(48F), 0, i18n("camps.Back"));
            if(bDetailProfile.isVisible())
                draw(x1024(464F), y1024(656F), x1024(460F), y1024(48F), 2, i18n("dgenpilot.Detail"));
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(624F), x1024(960F), 2.0F);
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            bBack.setPosC(x1024(56F), y1024(680F));
            bDetailProfile.setPosC(x1024(968F), y1024(680F));
            wScroll.set1024PosSize(32F, 32F, 962F, 560F);
            bViewAward.set1024gPosSize(32F, 304F, 192F, 164F);
        }

        public DialogClient()
        {
        }
    }

    public class Scroll extends GWindowScrollingDialogClient
    {

        public void created()
        {
            fixed = wInfo = (Info)create(new Info());
            fixed.bNotify = true;
            bNotify = true;
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
            if(wInfo != null)
                wInfo.computeSize();
            super.resized();
            if(vScroll.isVisible())
            {
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                vScroll.setPos(win.dx - lookAndFeel().getVScrollBarW() - gbevel.R.dx, gbevel.T.dy);
                vScroll.setSize(lookAndFeel().getVScrollBarW(), win.dy - gbevel.T.dy - gbevel.B.dy);
            }
        }

        public void render()
        {
            setCanvasColorWHITE();
            GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
            lookAndFeel().drawBevel(this, 0.0F, 0.0F, win.dx, win.dy, gbevel, ((GUILookAndFeel)lookAndFeel()).basicelements, true);
        }

        public Scroll()
        {
        }
    }

    public class Info extends GWindowDialogClient
    {

        public void afterCreated()
        {
            super.afterCreated();
//            fnt = GFont.New("courSmall");
            fnt = root.textFonts[0];

        }

        public void render()
        {
            if(pilot == null)
                return;
            clipRegion.set(root.C.clip);
            pushClip();
            GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
            clipRegion.y += gbevel.T.dy + 2.0F;
            clipRegion.dy -= gbevel.T.dy + gbevel.B.dy + 4F;
            root.C.clip.set(clipRegion);
            if(bMatPhotoValid)
            {
                int i = root.C.alpha;
                root.C.alpha = 255;
                setCanvasColorWHITE();
                draw(x1024(32F), y1024(32F), x1024g(192F), y1024g(256F), matPhoto, 0.0F, 0.0F, 192F, 256F);
                root.C.alpha = i;
            }
            GUISeparate.draw(this, myBrass, x1024(416F), y1024(128F), x1024(480F), 1.0F);
            GUISeparate.draw(this, myBrass, x1024(416F), y1024(160F), x1024(480F), 1.0F);
            GUISeparate.draw(this, myBrass, x1024(416F), y1024(192F), x1024(224F), 1.0F);
            GUISeparate.draw(this, myBrass, x1024(800F), y1024(192F), x1024(96F), 1.0F);
            GUISeparate.draw(this, myBrass, x1024(416F), y1024(224F), x1024(480F), 1.0F);
            GUISeparate.draw(this, myBrass, x1024(416F), y1024(256F), x1024(112F), 1.0F);
            GUISeparate.draw(this, myBrass, x1024(416F), y1024(288F), x1024(112F), 1.0F);
            GUISeparate.draw(this, myBrass, x1024(416F), y1024(320F), x1024(112F), 1.0F);
            setCanvasColorBLACK();
            root.C.font = fnt;
            draw(x1024(272F), y1024(112F), x1024(128F), y1024(16F), 2, i18n("dgenpilot.Name"));
            draw(x1024(272F), y1024(144F), x1024(128F), y1024(16F), 2, i18n("dgenpilot.Surname"));
            draw(x1024(272F), y1024(176F), x1024(128F), y1024(16F), 2, i18n("dgenpilot.Rank"));
            draw(x1024(272F), y1024(208F), x1024(128F), y1024(16F), 2, i18n("dgenpilot.Place"));
            draw(x1024(272F), y1024(240F), x1024(128F), y1024(16F), 2, i18n("dgenpilot.Sorties"));
            draw(x1024(272F), y1024(272F), x1024(128F), y1024(16F), 2, i18n("dgenpilot.Kills"));
            draw(x1024(272F), y1024(304F), x1024(128F), y1024(16F), 2, i18n("dgenpilot.GroundKills"));
            draw(x1024(648F), y1024(176F), x1024(144F), y1024(16F), 2, i18n("dgenpilot.Year"));
            setCanvasFont(0);
            draw(x1024(400F), y1024(16F), x1024(368F), y1024(32F), 1, i18n("dgenpilot.Title"));
            draw(x1024(416F), y1024(100F), x1024(480F), y1024(32F), 0, pilot.firstName);
            draw(x1024(416F), y1024(132F), x1024(480F), y1024(32F), 0, pilot.lastName);
            draw(x1024(416F), y1024(164F), x1024(224F), y1024(32F), 0, "" + pilot.sRank);
            draw(x1024(800F), y1024(164F), x1024(96F), y1024(32F), 0, pilot.dateBirth);
            draw(x1024(416F), y1024(196F), x1024(480F), y1024(32F), 0, pilot.placeBirth);
            draw(x1024(416F), y1024(228F), x1024(112F), y1024(32F), 0, "" + pilot.sorties);
            draw(x1024(416F), y1024(260F), x1024(112F), y1024(32F), 0, "" + pilot.kills);
            draw(x1024(416F), y1024(292F), x1024(112F), y1024(32F), 0, "" + pilot.ground);
            int j = (int)y1024(352F);
            for(int k = 0; k < pilot.events.size(); k++)
            {
                String s = (String)pilot.events.get(k);
                int l = drawLines(x1024(288F), j, s, 0, s.length(), x1024(590F), root.C.font.height - root.C.font.descender);
                j = (int)((float)j + (float)l * (root.C.font.height - root.C.font.descender));
            }

            popClip();
        }

        public void doComputeDy()
        {
            dy = (int)y1024(352F);
            setCanvasFont(0);
            for(int i = 0; i < pilot.events.size(); i++)
            {
                String s = (String)pilot.events.get(i);
                int j = computeLines(s, 0, s.length(), x1024(590F));
                dy += (float)j * (root.C.font.height - root.C.font.descender);
            }

            dy -= 2.0F * root.C.font.descender;
            computeSize();
        }

        public void computeSize()
        {
            win.dx = parentWindow.win.dx - lookAndFeel().getVScrollBarW();
            win.dy = parentWindow.win.dy;
            if((float)dy > win.dy)
                win.dy = dy;
        }

        private GFont fnt;
        private GColor myBrass;
        private GRegion clipRegion;
        private int dy;

        public Info()
        {
            myBrass = new GColor(99, 89, 74);
            clipRegion = new GRegion();
            dy = 0;
        }
    }

    class WAwardButton extends GWindowButton
    {

        public boolean notify(int i, int j)
        {
            if(i != 2)
            {
                return super.notify(i, j);
            } else
            {
                GUIAwards.indexIcons = pilot.medals;
                Main.stateStack().push(32);
                return true;
            }
        }

        public void render()
        {
            super.render();
            if(lastAward != null)
            {
                setCanvasColorWHITE();
                int i = root.C.alpha;
                root.C.alpha = 255;
                System.out.println("AVARD: " + (3F * win.dx) / 5F + " x " + (3F * win.dy) / 5F);
                if(bDown)
                    draw(5F, 5F, x1024g(186F), y1024g(158F), lastAward);
                else
                    draw(3F, 3F, x1024g(186F), y1024g(158F), lastAward);
                root.C.alpha = i;
            }
        }

        public WAwardButton(GWindow gwindow)
        {
            super(gwindow);
        }
    }


    public void enterPush(GameState gamestate)
    {
        pilot = ((GUIDGenRoster)gamestate).pilotCur;
        if(pilot.bPlayer)
            bDetailProfile.showWindow();
        else
            bDetailProfile.hideWindow();
        wInfo.doComputeDy();
        wScroll.resized();
        String s = pilot.photo;
        if(s != null && !s.endsWith(".bmp"))
            s = s + ".bmp";
        if(s != null && BmpUtils.bmp8Pal192x256ToTGA3(s, "PaintSchemes/Cache/photo.tga"))
        {
            matPhoto.mat.set('\0', "PaintSchemes/Cache/photo.tga");
            bMatPhotoValid = true;
        } else
        {
            bMatPhotoValid = false;
        }
        lastAward = null;
        if(pilot.medals != null)
        {
            int i = pilot.medals.length - 1;
            if(Main.cur().campaign.branch().equals("de") && World.cur().isHakenAllowed())
                lastAward = GTexture.New("missions/campaign/de/awardh" + pilot.medals[i] + ".mat");
            else
            if(Main.cur().campaign.branch().equals("fi") && World.cur().isHakenAllowed())
                lastAward = GTexture.New("missions/campaign/fi/awardh" + pilot.medals[i] + ".mat");
            else
                lastAward = GTexture.New("missions/campaign/" + Main.cur().campaign.branch() + "/award" + pilot.medals[i] + ".mat");
            bViewAward.showWindow();
        } else
        {
            bViewAward.hideWindow();
        }
        client.activateWindow();
    }

    public void enterPop(GameState gamestate)
    {
        client.activateWindow();
    }

    public void _leave()
    {
        client.hideWindow();
    }

    public GUIDGenPilot(GWindowRoot gwindowroot)
    {
        super(66);
        pilot = null;
        bMatPhotoValid = false;
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("dgenpilot.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        matPhoto = (GUITexture)GUITexture.New("gui/game/photo.mat");
        matPhoto.mat.setLayer(0);
        dialogClient.create(wScroll = new Scroll());
        bViewAward = (WAwardButton)wInfo.addControl(new WAwardButton(wInfo));
        GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bBack = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bDetailProfile = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIDGenRoster.Pilot pilot;
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public Scroll wScroll;
    public Info wInfo;
    public WAwardButton bViewAward;
    public GUIButton bBack;
    public GUIButton bDetailProfile;
    private GUITexture matPhoto;
    private boolean bMatPhotoValid;
    private GTexture lastAward;



}
