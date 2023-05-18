package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.game.I18N;
import com.maddox.sound.*;
import java.util.HashMap;

public class GUILookAndFeel extends GWin95LookAndFeel
{

    public GUILookAndFeel()
    {
        sndKey1 = new Sample("key_01.wav");
        sndKey2 = new Sample("key_02.wav");
        guiSound = new SoundFX(new SoundPreset("interface"));
        sounds = new HashMap();
        sounds.put("comboShow", sndKey1);
        sounds.put("comboHide", sndKey2);
        sounds.put("clickCheckBox", sndKey2);
        sounds.put("clickButton", sndKey1);
        sounds.put("clickSwitch", sndKey2);
        spaceComboList = 3F;
        minScrollMSize = 0.5F;
        spaceButton = 0.0F;
        spaceFramedTitle = 0.25F;
        _titleRegion = new GRegion();
    }

    public void soundPlay(String s)
    {
        if(super.bSoundEnable)
            guiSound.play((Sample)sounds.get(s));
    }

    public void drawSeparateH(GWindow gwindow, float f, float f1, float f2)
    {
        GUISeparate.draw(gwindow, GColor.Gray, f, f1, f2, 2.0F);
    }

    public void drawSeparateW(GWindow gwindow, float f, float f1, float f2)
    {
        GUISeparate.draw(gwindow, GColor.Gray, f, f1, 2.0F, f2);
    }

    public void render(GWindowTable gwindowtable)
    {
        gwindowtable.setCanvasColorWHITE();
        drawBevel(gwindowtable, 0.0F, 0.0F, ((GWindow) (gwindowtable)).win.dx, ((GWindow) (gwindowtable)).win.dy, bevelComboDown, basicelements, true);
        GRegion gregion = ((GWindow) (gwindowtable)).root.C.clip;
        gregion.x += bevelComboDown.L.dx;
        gregion.y += bevelComboDown.T.dy;
        gregion.dx -= bevelComboDown.L.dx + bevelComboDown.R.dx;
        gregion.dy -= bevelComboDown.T.dy + bevelComboDown.B.dy;
    }

    public GRegion getClientRegion(GWindowTable gwindowtable, GRegion gregion, float f)
    {
        gregion.x = bevelComboDown.L.dx + f;
        gregion.y = bevelComboDown.T.dy + f;
        gregion.dx = ((GWindow) (gwindowtable)).win.dx - gregion.x - bevelComboDown.R.dx - f;
        gregion.dy = ((GWindow) (gwindowtable)).win.dy - gregion.y - bevelComboDown.B.dy - f;
        return gregion;
    }

    public void render(GWindowEditControl gwindoweditcontrol)
    {
        gwindoweditcontrol.setCanvasColorWHITE();
        drawBevel(gwindoweditcontrol, 0.0F, 0.0F, ((GWindow) (gwindoweditcontrol)).win.dx, ((GWindow) (gwindoweditcontrol)).win.dy, bevelComboDown, basicelements, true);
        render(gwindoweditcontrol, bevelComboDown.L.dx);
    }

    public float xComboListItem(int i, float f, float f1)
    {
        if(i == 0 || f1 >= f)
            return 0.0F;
        if(i == 2)
            return f - f1;
        if(i == 1)
            return (f - f1) / 2.0F;
        else
            return 0.0F;
    }

    public void renderComboList(GWindowComboControl gwindowcombocontrol)
    {
        com.maddox.gwindow.GWindowComboControl.ListArea listarea = gwindowcombocontrol.listArea;
        int i = ((GWindow) (listarea)).root.C.alpha;
        ((GWindow) (listarea)).root.C.alpha = 255;
        listarea.setCanvasColorWHITE();
        drawBevel(listarea, 0.0F, 0.0F, ((GWindow) (listarea)).win.dx, ((GWindow) (listarea)).win.dy, bevelComboUp, basicelements);
        GRegion gregion = listarea.getClientRegion();
        gregion.x += bevelComboUp.L.dx + spaceComboList;
        gregion.y += bevelComboUp.T.dy;
        gregion.dx -= bevelComboUp.L.dx + bevelComboUp.R.dx + 2.0F * spaceComboList;
        gregion.dy -= bevelComboUp.T.dy + bevelComboUp.B.dy;
        if(listarea.pushClipRegion(gregion, true, 0.0F))
        {
            listarea.setCanvasColorBLACK();
            listarea.setCanvasFont(((GWindowDialogControl) (gwindowcombocontrol)).font);
            GFont gfont = ((GWindow) (gwindowcombocontrol)).root.C.font;
            float f = (getComboHline() - gfont.height) / 2.0F;
            int j = gwindowcombocontrol.listStartLine;
            for(int k = 0; k < gwindowcombocontrol.listCountLines; k++)
            {
                GSize gsize = gfont.size(gwindowcombocontrol.get(j));
                if(j == gwindowcombocontrol.listSelected)
                {
                    listarea.draw(0.0F, f, ((GWindow) (listarea)).win.dx, gsize.dy, super.elements, 4F, 98F, 1.0F, 1.0F);
                    listarea.setCanvasColorWHITE();
                    listarea.draw(xComboListItem(((GWindowDialogControl) (gwindowcombocontrol)).align, gregion.dx, gsize.dx), f, gwindowcombocontrol.get(j));
                    listarea.setCanvasColorBLACK();
                } else
                if(gwindowcombocontrol.posEnable != null && !gwindowcombocontrol.posEnable[j])
                {
                    listarea.setCanvasColorWHITE();
                    listarea.draw(0.0F, f, ((GWindow) (listarea)).win.dx, gsize.dy, super.elements, 2.0F, 16F, 1.0F, 1.0F);
                    listarea.draw(xComboListItem(((GWindowDialogControl) (gwindowcombocontrol)).align, gregion.dx, gsize.dx) + 1.0F, f + 1.0F, gwindowcombocontrol.get(j));
                    listarea.setCanvasColor(0x7f7f7f);
                    listarea.draw(xComboListItem(((GWindowDialogControl) (gwindowcombocontrol)).align, gregion.dx, gsize.dx), f, gwindowcombocontrol.get(j));
                    listarea.setCanvasColorBLACK();
                } else
                {
                    listarea.draw(xComboListItem(((GWindowDialogControl) (gwindowcombocontrol)).align, gregion.dx, gsize.dx), f, gwindowcombocontrol.get(j));
                }
                j++;
                f += getComboHline();
            }

            listarea.popClip();
        }
        ((GWindow) (listarea)).root.C.alpha = i;
    }

    public void setupComboList(GWindowComboControl gwindowcombocontrol)
    {
        ((GWindow) (gwindowcombocontrol.listArea)).win.dx = ((GWindow) (gwindowcombocontrol)).win.dx;
        gwindowcombocontrol.listCountLines = gwindowcombocontrol.listVisibleLines;
        if(gwindowcombocontrol.listCountLines > gwindowcombocontrol.size())
            gwindowcombocontrol.listCountLines = gwindowcombocontrol.size();
        ((GWindow) (gwindowcombocontrol.listArea)).win.dy = (float)gwindowcombocontrol.listCountLines * getComboHline() + bevelComboUp.B.dy + bevelComboUp.T.dy;
        GPoint gpoint = gwindowcombocontrol.windowToGlobal(0.0F, ((GWindow) (gwindowcombocontrol)).win.dy);
        if(gpoint.y + ((GWindow) (gwindowcombocontrol.listArea)).win.dy > ((GWindow) (((GWindow) (gwindowcombocontrol)).root)).win.dy)
            ((GWindow) (gwindowcombocontrol.listArea)).win.y = -((GWindow) (gwindowcombocontrol.listArea)).win.dy;
        else
            ((GWindow) (gwindowcombocontrol.listArea)).win.y = ((GWindow) (gwindowcombocontrol)).win.dy;
        if(gwindowcombocontrol.windowToGlobal(0.0F, ((GWindow) (gwindowcombocontrol.listArea)).win.y).y < 0.0F)
            ((GWindow) (gwindowcombocontrol.listArea)).win.y = gwindowcombocontrol.globalToWindow(0.0F, 0.0F).y;
        ((GWindow) (gwindowcombocontrol.listArea)).win.x = 0.0F;
        if(gwindowcombocontrol.listCountLines < gwindowcombocontrol.size())
        {
            ((GWindow) (gwindowcombocontrol.scrollBar)).win.dx = getVScrollBarW();
            ((GWindow) (gwindowcombocontrol.scrollBar)).win.dy = ((GWindow) (gwindowcombocontrol.listArea)).win.dy;
            ((GWindow) (gwindowcombocontrol.scrollBar)).win.x = ((GWindow) (gwindowcombocontrol.listArea)).win.dx - ((GWindow) (gwindowcombocontrol.scrollBar)).win.dx;
            ((GWindow) (gwindowcombocontrol.scrollBar)).win.y = 0.0F;
            if(gwindowcombocontrol.iSelected >= 0)
            {
                gwindowcombocontrol.listStartLine = gwindowcombocontrol.iSelected;
                if(gwindowcombocontrol.listStartLine + gwindowcombocontrol.listCountLines > gwindowcombocontrol.size())
                    gwindowcombocontrol.listStartLine = gwindowcombocontrol.size() - gwindowcombocontrol.listCountLines;
            } else
            {
                gwindowcombocontrol.listStartLine = 0;
            }
            gwindowcombocontrol.scrollBar.setRange(0.0F, gwindowcombocontrol.size(), gwindowcombocontrol.listCountLines, 1.0F, gwindowcombocontrol.listStartLine);
            gwindowcombocontrol.scrollBar.showWindow();
        } else
        {
            gwindowcombocontrol.scrollBar.hideWindow();
            gwindowcombocontrol.listStartLine = 0;
        }
        gwindowcombocontrol.listSelected = gwindowcombocontrol.iSelected;
        ((GWindow) (gwindowcombocontrol.listArea)).win.dx -= getVScrollBarW();
    }

    public void render(GWindowComboControl gwindowcombocontrol)
    {
        int i = ((GWindow) (gwindowcombocontrol)).root.C.alpha;
        ((GWindow) (gwindowcombocontrol)).root.C.alpha = 255;
        gwindowcombocontrol.setCanvasColorWHITE();
        drawBevel(gwindowcombocontrol, 0.0F, 0.0F, ((GWindow) (gwindowcombocontrol)).win.dx - getVScrollBarW(), ((GWindow) (gwindowcombocontrol)).win.dy, bevelComboDown, basicelements);
        ((GWindow) (gwindowcombocontrol)).root.C.alpha = i;
    }

    public void setupComboEditBox(GWindowEditBox gwindoweditbox)
    {
        ((GWindow) (gwindoweditbox)).win.x = bevelComboDown.L.dx;
        ((GWindow) (gwindoweditbox)).win.y = bevelComboDown.T.dy;
        ((GWindow) (gwindoweditbox)).win.dx = ((GWindow) (gwindoweditbox)).parentWindow.win.dx - bevelComboDown.L.dx - bevelComboDown.R.dx - getVScrollBarW();
        ((GWindow) (gwindoweditbox)).win.dy = ((GWindow) (gwindoweditbox)).parentWindow.win.dy - bevelComboDown.T.dy - bevelComboDown.B.dy;
    }

    public void setupComboButton(GWindowButtonTexture gwindowbuttontexture)
    {
        gwindowbuttontexture.texUP = CBButtonUP;
        gwindowbuttontexture.texDOWN = CBButtonDOWN;
        gwindowbuttontexture.texDISABLE = CBButtonUP;
        gwindowbuttontexture.texOVER = CBButtonUP;
        ((GWindow) (gwindowbuttontexture)).win.dx = getVScrollBarW();
        ((GWindow) (gwindowbuttontexture)).win.dy = getVScrollBarH();
        ((GWindow) (gwindowbuttontexture)).win.x = ((GWindow) (gwindowbuttontexture)).parentWindow.win.dx - ((GWindow) (gwindowbuttontexture)).win.dx;
        ((GWindow) (gwindowbuttontexture)).win.y = 0.0F;
    }

    public float getComboH()
    {
        return getVScrollBarH() + bevelComboDown.B.dy + bevelComboDown.T.dy;
    }

    public float getComboHmetric()
    {
        return getComboH() / metric();
    }

    public float getComboHline()
    {
        return metric(1.2F);
    }

    public void render(GWindowVScrollBar gwindowvscrollbar)
    {
        float f = gwindowvscrollbar.yM - ((GWindow) (gwindowvscrollbar.uButton)).win.dy;
        float f1 = ((GWindow) (gwindowvscrollbar.dButton)).win.y - gwindowvscrollbar.yM - gwindowvscrollbar.dyM;
        if(f < 0.0F && f1 < 0.0F)
        {
            return;
        } else
        {
            int i = ((GWindow) (gwindowvscrollbar)).root.C.alpha;
            ((GWindow) (gwindowvscrollbar)).root.C.alpha = 255;
            gwindowvscrollbar.setCanvasColorWHITE();
            gwindowvscrollbar.draw(0.0F, ((GWindow) (gwindowvscrollbar.uButton)).win.dy, ((GWindow) (gwindowvscrollbar)).win.dx, ((GWindow) (gwindowvscrollbar)).win.dy - ((GWindow) (gwindowvscrollbar.uButton)).win.dy - ((GWindow) (gwindowvscrollbar.dButton)).win.dy, buttons, 0.0F, 143F, 32F, 2.0F);
            gwindowvscrollbar.draw(0.0F, gwindowvscrollbar.yM, ((GWindow) (gwindowvscrollbar)).win.dx, gwindowvscrollbar.dyM, buttons, 192F, 112F, 32F, 64F);
            ((GWindow) (gwindowvscrollbar)).root.C.alpha = i;
            return;
        }
    }

    public void setupVScrollBarSizes(GWindowVScrollBar gwindowvscrollbar)
    {
        float f = ((GWindow) (gwindowvscrollbar)).win.dy - 2.0F * getVScrollBarH();
        float f1 = getVScrollBarH();
        if(f <= 0.0F)
            f1 = ((GWindow) (gwindowvscrollbar)).win.dy / 2.0F;
        gwindowvscrollbar.uButton.setSize(getVScrollBarW(), f1);
        gwindowvscrollbar.uButton.setPos(0.0F, 0.0F);
        gwindowvscrollbar.dButton.setSize(getVScrollBarW(), f1);
        gwindowvscrollbar.dButton.setPos(0.0F, ((GWindow) (gwindowvscrollbar)).win.dy - f1);
        float f2 = 2.0F * f1;
        if(f2 > f)
        {
            gwindowvscrollbar.dyM = 0.0F;
            gwindowvscrollbar.yM = (gwindowvscrollbar.pos / (gwindowvscrollbar.posMax - gwindowvscrollbar.posMin)) * (((GWindow) (gwindowvscrollbar)).win.dy - f1);
            if(gwindowvscrollbar.yM < 0.0F)
                gwindowvscrollbar.yM = -gwindowvscrollbar.yM;
            gwindowvscrollbar.mButton.hideWindow();
        } else
        {
            gwindowvscrollbar.dyM = 2.0F * f1;
            gwindowvscrollbar.yM = (gwindowvscrollbar.pos / (gwindowvscrollbar.posMax - gwindowvscrollbar.posMin)) * (((GWindow) (gwindowvscrollbar)).win.dy - 4F * f1);
            if(gwindowvscrollbar.yM < 0.0F)
                gwindowvscrollbar.yM = -gwindowvscrollbar.yM;
            gwindowvscrollbar.yM += f1;
            gwindowvscrollbar.mButton.showWindow();
            gwindowvscrollbar.mButton.setSize(getVScrollBarW(), 2.0F * f1);
            gwindowvscrollbar.mButton.setPos(0.0F, gwindowvscrollbar.yM);
            gwindowvscrollbar.mButton._bNoRender = true;
        }
    }

    public void render(GWindowHScrollBar gwindowhscrollbar)
    {
        float f = gwindowhscrollbar.xM - ((GWindow) (gwindowhscrollbar.lButton)).win.dx;
        float f1 = ((GWindow) (gwindowhscrollbar.rButton)).win.x - gwindowhscrollbar.xM - gwindowhscrollbar.dxM;
        if(f < 0.0F && f1 < 0.0F)
        {
            return;
        } else
        {
            int i = ((GWindow) (gwindowhscrollbar)).root.C.alpha;
            ((GWindow) (gwindowhscrollbar)).root.C.alpha = 255;
            gwindowhscrollbar.setCanvasColorWHITE();
            gwindowhscrollbar.draw(((GWindow) (gwindowhscrollbar.lButton)).win.dx, 0.0F, ((GWindow) (gwindowhscrollbar)).win.dx - ((GWindow) (gwindowhscrollbar.lButton)).win.dx - ((GWindow) (gwindowhscrollbar.rButton)).win.dx, ((GWindow) (gwindowhscrollbar)).win.dy, buttons, 95F, 144F, 1.0F, 32F);
            gwindowhscrollbar.draw(gwindowhscrollbar.xM, 0.0F, gwindowhscrollbar.dxM, ((GWindow) (gwindowhscrollbar)).win.dy, buttons, 128F, 112F, 64F, 32F);
            ((GWindow) (gwindowhscrollbar)).root.C.alpha = i;
            return;
        }
    }

    public void setupHScrollBarSizes(GWindowHScrollBar gwindowhscrollbar)
    {
        float f = ((GWindow) (gwindowhscrollbar)).win.dx - 2.0F * getHScrollBarW();
        float f1 = getHScrollBarW();
        if(f <= 0.0F)
            f1 = ((GWindow) (gwindowhscrollbar)).win.dx / 2.0F;
        gwindowhscrollbar.lButton.setSize(f1, getHScrollBarH());
        gwindowhscrollbar.lButton.setPos(0.0F, 0.0F);
        gwindowhscrollbar.rButton.setSize(f1, getHScrollBarH());
        gwindowhscrollbar.rButton.setPos(((GWindow) (gwindowhscrollbar)).win.dx - f1, 0.0F);
        gwindowhscrollbar.mButton._bNoRender = true;
        gwindowhscrollbar.dxM = 2.0F * f1;
        gwindowhscrollbar.xM = (gwindowhscrollbar.pos / (gwindowhscrollbar.posMax - gwindowhscrollbar.posMin)) * (((GWindow) (gwindowhscrollbar)).win.dx - 4F * f1);
        if(gwindowhscrollbar.xM < 0.0F)
            gwindowhscrollbar.xM = -gwindowhscrollbar.xM;
        gwindowhscrollbar.xM += f1;
        gwindowhscrollbar.mButton.setSize(2.0F * f1, getHScrollBarH());
        gwindowhscrollbar.mButton.setPos(gwindowhscrollbar.xM, 0.0F);
        gwindowhscrollbar.mButton._bNoRender = true;
    }

    public float getHScrollBarW()
    {
        return metric(1.6F);
    }

    public float getHScrollBarH()
    {
        return metric(1.6F);
    }

    public float getVScrollBarW()
    {
        return metric(1.6F);
    }

    public float getVScrollBarH()
    {
        return metric(1.6F);
    }

    public void setupScrollButtonUP(GWindowButtonTexture gwindowbuttontexture)
    {
        gwindowbuttontexture.texUP = super.SBupButtonUP;
        gwindowbuttontexture.texDOWN = super.SBupButtonDOWN;
        gwindowbuttontexture.texDISABLE = super.SBupButtonDISABLE;
        gwindowbuttontexture.texOVER = super.SBupButtonOVER;
    }

    public void setupScrollButtonDOWN(GWindowButtonTexture gwindowbuttontexture)
    {
        gwindowbuttontexture.texUP = super.SBdownButtonUP;
        gwindowbuttontexture.texDOWN = super.SBdownButtonDOWN;
        gwindowbuttontexture.texDISABLE = super.SBdownButtonDISABLE;
        gwindowbuttontexture.texOVER = super.SBdownButtonOVER;
    }

    public void setupScrollButtonLEFT(GWindowButtonTexture gwindowbuttontexture)
    {
        gwindowbuttontexture.texUP = super.SBleftButtonUP;
        gwindowbuttontexture.texDOWN = super.SBleftButtonDOWN;
        gwindowbuttontexture.texDISABLE = super.SBleftButtonDISABLE;
        gwindowbuttontexture.texOVER = super.SBleftButtonOVER;
    }

    public void setupScrollButtonRIGHT(GWindowButtonTexture gwindowbuttontexture)
    {
        gwindowbuttontexture.texUP = super.SBrightButtonUP;
        gwindowbuttontexture.texDOWN = super.SBrightButtonDOWN;
        gwindowbuttontexture.texDISABLE = super.SBrightButtonDISABLE;
        gwindowbuttontexture.texOVER = super.SBrightButtonOVER;
    }

    public void render(GWindowButton gwindowbutton)
    {
        int i = ((GWindow) (gwindowbutton)).root.C.alpha;
        ((GWindow) (gwindowbutton)).root.C.alpha = 255;
        gwindowbutton.setCanvasColorWHITE();
        if(!((GWindowDialogControl) (gwindowbutton)).bEnable)
        {
            drawBevel(gwindowbutton, 0.0F, 0.0F, ((GWindow) (gwindowbutton)).win.dx, ((GWindow) (gwindowbutton)).win.dy, bevelButtonUP, basicelements);
            if(((GWindowDialogControl) (gwindowbutton)).cap != null)
            {
                GRegion gregion = gwindowbutton.getClientRegion();
                float f = gregion.dx;
                float f2 = gregion.dy;
                if(gwindowbutton.pushClipRegion(gregion, ((GWindow) (gwindowbutton)).bClip, 0.0F))
                {
                    renderTextDialogControl(gwindowbutton, 1.0F, 1.0F, f, f2, 0xffffff, false);
                    renderTextDialogControl(gwindowbutton, 0.0F, 0.0F, f, f2, 0x7f7f7f, false);
                    gwindowbutton.popClip();
                }
            }
            ((GWindow) (gwindowbutton)).root.C.alpha = i;
            return;
        }
        if(((GWindowDialogControl) (gwindowbutton)).bDown && !gwindowbutton.bDrawOnlyUP)
            drawBevel(gwindowbutton, 0.0F, 0.0F, ((GWindow) (gwindowbutton)).win.dx, ((GWindow) (gwindowbutton)).win.dy, bevelButtonDOWN, basicelements);
        else
            drawBevel(gwindowbutton, 0.0F, 0.0F, ((GWindow) (gwindowbutton)).win.dx, ((GWindow) (gwindowbutton)).win.dy, bevelButtonUP, basicelements);
        if(((GWindowDialogControl) (gwindowbutton)).cap != null)
        {
            GRegion gregion1 = gwindowbutton.getClientRegion();
            float f1 = gregion1.dx;
            float f3 = gregion1.dy;
            if(gwindowbutton.pushClipRegion(gregion1, ((GWindow) (gwindowbutton)).bClip, 0.0F))
            {
                if(((GWindowDialogControl) (gwindowbutton)).bDown)
                    renderTextDialogControl(gwindowbutton, 1.0F, 1.0F, f1, f3, ((GWindowDialogControl) (gwindowbutton)).color, gwindowbutton.isDefault());
                else
                    renderTextDialogControl(gwindowbutton, 0.0F, 0.0F, f1, f3, ((GWindowDialogControl) (gwindowbutton)).color, gwindowbutton.isDefault());
                gwindowbutton.popClip();
            }
        }
        ((GWindow) (gwindowbutton)).root.C.alpha = i;
    }

    public void render(GWindowButtonTexture gwindowbuttontexture)
    {
        int i = ((GWindow) (gwindowbuttontexture)).root.C.alpha;
        ((GWindow) (gwindowbuttontexture)).root.C.alpha = 255;
        super.render(gwindowbuttontexture);
        ((GWindow) (gwindowbuttontexture)).root.C.alpha = i;
    }

    public GSize getMinSize(GWindowButton gwindowbutton, GSize gsize)
    {
        gsize = getMinSizeDialogControl(gwindowbutton, gsize);
        gsize.dx += bevelButtonUP.L.dx / 2.0F + bevelButtonUP.R.dx / 2.0F + 2.0F * metric(spaceButton);
        gsize.dy += bevelButtonUP.T.dy / 2.0F + bevelButtonUP.B.dy / 2.0F + 2.0F * metric(spaceButton);
        return gsize;
    }

    public GRegion getClientRegion(GWindowButton gwindowbutton, GRegion gregion, float f)
    {
        gregion = getClientRegionDialogControl(gwindowbutton, gregion, f);
        gregion.x += bevelButtonUP.L.dx / 2.0F;
        gregion.y += bevelButtonUP.T.dy / 2.0F;
        gregion.dx -= bevelButtonUP.L.dx / 2.0F + bevelButtonUP.R.dx / 2.0F;
        gregion.dy -= bevelButtonUP.T.dy / 2.0F + bevelButtonUP.B.dy / 2.0F;
        return gregion;
    }

    public void render(GWindowClient gwindowclient)
    {
        gwindowclient.setCanvasColorWHITE();
        gwindowclient.draw(0.0F, 0.0F, ((GWindow) (gwindowclient)).win.dx, ((GWindow) (gwindowclient)).win.dy, basicelements, bevelComboUp.Area);
    }

    public void setupFrameCloseBox(GWindowFrameCloseBox gwindowframeclosebox)
    {
        gwindowframeclosebox.texUP = super.closeBoxUP;
        gwindowframeclosebox.texDOWN = super.closeBoxDOWN;
        gwindowframeclosebox.texDISABLE = super.closeBoxDISABLE;
        gwindowframeclosebox.texOVER = super.closeBoxOVER;
    }

    public void frameSetCloseBoxPos(GWindowFramed gwindowframed)
    {
        gwindowframed.closeBox.hideWindow();
    }

    public int frameHitTest(GWindowFramed gwindowframed, float f, float f1)
    {
        GRegion gregion = gwindowframed.getClientRegion();
        if(f < 0.0F || f > ((GWindow) (gwindowframed)).win.dx || f1 < 0.0F || f1 > ((GWindow) (gwindowframed)).win.dy)
            return 0;
        if(f >= gregion.x && f < gregion.x + gregion.dx && f1 >= gregion.y && f1 < gregion.y + gregion.dy)
            return 0;
        if(f < gregion.x)
            if(f1 < bevelButtonUP.T.dy)
                return 1;
            else
                return f1 >= gregion.y + gregion.dy ? 6 : 4;
        if(f >= gregion.x + gregion.dx)
            if(f1 < bevelButtonUP.T.dy)
                return 3;
            else
                return f1 >= gregion.y + gregion.dy ? 8 : 5;
        if(f1 < bevelButtonUP.T.dy)
            return 2;
        else
            return f1 >= gregion.y + gregion.dy ? 7 : 9;
    }

    public void render(GWindowFramed gwindowframed)
    {
        float f = framedTitleHeight(gwindowframed);
        gwindowframed.setCanvasColorWHITE();
        drawBevel(gwindowframed, 0.0F, f, ((GWindow) (gwindowframed)).win.dx, ((GWindow) (gwindowframed)).win.dy - f, bevelComboUp, basicelements, false);
        GBevel gbevel = bevelButtonUP;
        drawBevel(gwindowframed, 0.0F, 0.0F, ((GWindow) (gwindowframed)).win.dx, f, gbevel, basicelements);
        if(gwindowframed.title != null)
        {
            if(!gwindowframed.isActivated())
                gwindowframed.setCanvasColor(0xc0c0c0);
            gwindowframed.setCanvasFont(1);
            _titleRegion.x = bevelButtonUP.L.dx;
            _titleRegion.y = bevelButtonUP.T.dy;
            if(gwindowframed.closeBox != null)
                _titleRegion.dx = ((GWindow) (gwindowframed)).win.dx - bevelButtonUP.L.dx - bevelButtonUP.R.dx - metric();
            else
                _titleRegion.dx = ((GWindow) (gwindowframed)).win.dx - bevelButtonUP.L.dx - bevelButtonUP.R.dx;
            _titleRegion.dy = f - bevelButtonUP.T.dy - bevelButtonUP.B.dy;
            if(gwindowframed.pushClipRegion(_titleRegion, ((GWindow) (gwindowframed)).bClip, metric(spaceFramedTitle)))
            {
                gwindowframed.draw(0.0F, 0.0F, gwindowframed.title);
                gwindowframed.popClip();
            }
        }
    }

    private float framedTitleHeight(GWindowFramed gwindowframed)
    {
        float f = metric();
        if(f > ((GWindow) (gwindowframed)).root.textFonts[1].height)
            f = ((GWindow) (gwindowframed)).root.textFonts[1].height;
        if(gwindowframed.title != null && gwindowframed.title.length() > 0)
            return f + 2.0F * metric(spaceFramedTitle) + bevelButtonUP.T.dy + bevelButtonUP.B.dy;
        else
            return bevelButtonUP.T.dy + bevelButtonUP.B.dy;
    }

    public GSize getMinSize(GWindowFramed gwindowframed, GSize gsize)
    {
        float f = 50F;
        float f1 = 50F;
        if(gwindowframed.clientWindow != null)
        {
            GSize gsize1 = gwindowframed.clientWindow.getMinSize();
            f = gsize1.dx;
            f1 = gsize1.dy;
        }
        if(gwindowframed.menuBar != null)
        {
            GSize gsize2 = gwindowframed.menuBar.getMinSize();
            f1 += gsize2.dy;
        }
        f += bevelComboUp.L.dx + bevelComboUp.R.dx;
        f1 += bevelComboUp.T.dy + bevelComboUp.B.dy + framedTitleHeight(gwindowframed);
        gsize.set(f, f1);
        return gsize;
    }

    public GRegion getClientRegion(GWindowFramed gwindowframed, GRegion gregion, float f)
    {
        gregion.x = bevelComboUp.L.dx + f;
        gregion.y = framedTitleHeight(gwindowframed) + super.bevelFW.T.dy + f;
        if(gwindowframed.menuBar != null)
            gregion.y += ((GWindow) (gwindowframed.menuBar)).win.dy;
        gregion.dx = ((GWindow) (gwindowframed)).win.dx - gregion.x - bevelComboUp.R.dx - f;
        gregion.dy = ((GWindow) (gwindowframed)).win.dy - gregion.y - bevelComboUp.B.dy - f;
        return gregion;
    }

    public String i18n(String s)
    {
        return I18N.gwindow(s);
    }

    public void reloadTextures()
    {
        super.reloadTextures();
        basicelements = GTexture.New("GUI/game/basicelements" + GWindowLookAndFeel.getUIColorString() + ".mat");
        super.elements = GTexture.New("GUI/game/elements" + GWindowLookAndFeel.getUIColorString() + ".mat");
        super.elementsStreched = GTexture.New("GUI/game/elementss" + GWindowLookAndFeel.getUIColorString() + ".mat");
        super.regionWhite = new GTexRegion(super.elements, 5F, 17F, 1.0F, 1.0F);
        super.closeBoxUP = new GTexRegion(super.elementsStreched, 4F, 32F, 10F, 9F);
        super.closeBoxDOWN = new GTexRegion(super.elementsStreched, 4F, 43F, 10F, 9F);
        super.closeBoxDISABLE = super.closeBoxOVER = super.closeBoxUP;
        super.checkBoxCheckEnable = new GTexRegion(super.elementsStreched, 32F, 64F, 13F, 13F);
        super.checkBoxCheckDisable = new GTexRegion(super.elementsStreched, 45F, 64F, 13F, 13F);
        super.checkBoxUnCheckEnable = new GTexRegion(super.elementsStreched, 58F, 64F, 13F, 13F);
        super.checkBoxUnCheckDisable = new GTexRegion(super.elementsStreched, 71F, 64F, 13F, 13F);
    }

    public void init(GWindowRoot gwindowroot)
    {
        buttons = GTexture.New("GUI/game/buttons.mat");
        buttons2 = GTexture.New("GUI/game/buttons2.mat");
        basicelements = GTexture.New("GUI/game/basicelements.mat");
        basicelements = GTexture.New("GUI/game/basicelements" + GWindowLookAndFeel.getUIColorString() + ".mat");
        bevelGreen = new GBevel();
        bevelGreen.set(new GRegion(160F, 192F, 64F, 64F), new GRegion(191F, 223F, 2.0F, 2.0F));
        bevelRed = new GBevel();
        bevelRed.set(new GRegion(64F, 192F, 32F, 32F), new GRegion(69F, 197F, 22F, 22F));
        bevelBlacked = new GBevel();
        bevelBlacked.set(new GRegion(32F, 192F, 32F, 32F), new GRegion(41F, 201F, 14F, 14F));
        bevelComboUp = new GBevel();
        bevelComboUp.set(new GRegion(32F, 224F, 16F, 16F), new GRegion(36F, 228F, 8F, 8F));
        bevelComboDown = new GBevel();
        bevelComboDown.set(new GRegion(32F, 240F, 16F, 16F), new GRegion(35F, 243F, 10F, 10F));
        bevelButtonUP = new GBevel();
        bevelButtonUP.set(new GRegion(160F, 160F, 32F, 32F), new GRegion(172F, 172F, 8F, 8F));
        bevelButtonDOWN = new GBevel();
        bevelButtonDOWN.set(new GRegion(192F, 160F, 32F, 32F), new GRegion(204F, 172F, 8F, 8F));
        bevelAward = new GBevel();
        bevelAward.set(new GRegion(160F, 160F, 32F, 32F), new GRegion(165F, 166F, 21F, 20F));
        super.elements = GTexture.New("GUI/game/elements.mat");
        super.elementsStreched = GTexture.New("GUI/game/elementss.mat");
        super.elements = GTexture.New("GUI/game/elements" + GWindowLookAndFeel.getUIColorString() + ".mat");
        super.elementsStreched = GTexture.New("GUI/game/elementss" + GWindowLookAndFeel.getUIColorString() + ".mat");
        super.cursors = GTexture.New("GUI/game/cursors.mat");
        super.cursorsStreched = GTexture.New("GUI/game/cursorss.mat");
        super.regionWhite = new GTexRegion(super.elements, 5F, 17F, 1.0F, 1.0F);
        super.bevelUP.set(new GRegion(4F, 16F, 16F, 16F), new GRegion(6F, 18F, 12F, 12F));
        super.bevelTabDialogClient = super.bevelUP;
        super.bevelDOWN.set(new GRegion(52F, 37F, 16F, 16F), new GRegion(54F, 39F, 12F, 12F));
        super.bevelUPsmall.set(new GRegion(19F, 60F, 11F, 10F), new GRegion(20F, 61F, 9F, 8F));
        super.bevelDOWNsmall.set(new GRegion(19F, 71F, 10F, 9F), new GRegion(20F, 72F, 8F, 7F));
        super.bevelFW.set(new GRegion(0.0F, 16F, 128F, 112F), new GRegion(4F, 16F, 120F, 108F));
        super.bevelTitleActive.set(new GRegion(0.0F, 0.0F, 128F, 16F), new GRegion(4F, 4F, 120F, 11F));
        super.bevelTitleInactive.set(new GRegion(70F, 18F, 53F, 16F), new GRegion(74F, 22F, 45F, 11F));
        super.bevelFW.Area.dx = 0.0F;
        super.bevelBlack.set(new GRegion(4F, 98F, 4F, 4F), new GRegion(5F, 99F, 2.0F, 2.0F));
        super.bevelBlack.Area.dx = 0.0F;
        super.bevelBlack50.set(new GRegion(32F, 16F, 12F, 10F), new GRegion(33F, 17F, 10F, 8F));
        super.bevelBlack50.Area.dx = 0.0F;
        super.bevelSeparate.set(new GRegion(87F, 69F, 8F, 7F), new GRegion(89F, 71F, 4F, 3F));
        super.bevelSeparate.Area.dx = 0.0F;
        super.bevelTabCUR.set(new GRegion(4F, 82F, 53F, 15F), new GRegion(8F, 86F, 45F, 8F));
        super.bevelTabCUR.Area.dx = 0.0F;
        super.bevelTab.set(new GRegion(57F, 82F, 54F, 13F), new GRegion(61F, 87F, 46F, 8F));
        super.bevelTab.Area.dx = 0.0F;
        super.closeBoxUP = new GTexRegion(super.elementsStreched, 4F, 32F, 10F, 9F);
        super.closeBoxDOWN = new GTexRegion(super.elementsStreched, 4F, 43F, 10F, 9F);
        super.closeBoxDISABLE = super.closeBoxOVER = super.closeBoxUP;
        super.SBupButtonUP = new GTexRegion(buttons, 0.0F, 112F, 32F, 32F);
        super.SBupButtonDOWN = new GTexRegion(buttons, 32F, 112F, 32F, 32F);
        super.SBupButtonDISABLE = super.SBupButtonOVER = super.SBupButtonUP;
        super.SBdownButtonUP = new GTexRegion(buttons, 0.0F, 144F, 32F, 32F);
        super.SBdownButtonDOWN = new GTexRegion(buttons, 32F, 144F, 32F, 32F);
        super.SBdownButtonDISABLE = super.SBdownButtonOVER = super.SBdownButtonUP;
        super.SBleftButtonUP = new GTexRegion(buttons, 64F, 144F, 32F, 32F);
        super.SBleftButtonDOWN = new GTexRegion(buttons, 96F, 144F, 32F, 32F);
        super.SBleftButtonDISABLE = super.SBleftButtonOVER = super.SBleftButtonUP;
        super.SBrightButtonUP = new GTexRegion(buttons, 64F, 112F, 32F, 32F);
        super.SBrightButtonDOWN = new GTexRegion(buttons, 96F, 112F, 32F, 32F);
        super.SBrightButtonDISABLE = super.SBrightButtonOVER = super.SBrightButtonUP;
        CBButtonUP = new GTexRegion(buttons, 128F, 144F, 32F, 32F);
        CBButtonDOWN = new GTexRegion(buttons, 160F, 144F, 32F, 32F);
        super.checkBoxCheckEnable = new GTexRegion(super.elementsStreched, 32F, 64F, 13F, 13F);
        super.checkBoxCheckDisable = new GTexRegion(super.elementsStreched, 45F, 64F, 13F, 13F);
        super.checkBoxUnCheckEnable = new GTexRegion(super.elementsStreched, 58F, 64F, 13F, 13F);
        super.checkBoxUnCheckDisable = new GTexRegion(super.elementsStreched, 71F, 64F, 13F, 13F);
        gwindowroot.textFonts[0] = GFont.New("arial10");
        gwindowroot.textFonts[1] = GFont.New("arialb10");
        super.metric = (int)(gwindowroot.textFonts[0].height + 0.5F);
        gwindowroot.mouseCursors[0] = new GCursorTexRegion(super.cursors, 32F, 96F, 0.0F, 0.0F, 0.0F, 0.0F, 0);
        gwindowroot.mouseCursors[1] = new GCursorTexRegion(super.cursors, 0.0F, 0.0F, 32F, 32F, 5F, 4F, 1);
        gwindowroot.mouseCursors[2] = new GCursorTexRegion(super.cursors, 32F, 0.0F, 32F, 32F, 15F, 15F, 2);
        gwindowroot.mouseCursors[3] = new GCursorTexRegion(super.cursors, 64F, 0.0F, 32F, 32F, 5F, 4F, 3);
        gwindowroot.mouseCursors[4] = new GCursorTexRegion(super.cursors, 96F, 0.0F, 32F, 32F, 5F, 4F, 4);
        gwindowroot.mouseCursors[5] = new GCursorTexRegion(super.cursors, 0.0F, 32F, 32F, 32F, 14F, 15F, 5);
        gwindowroot.mouseCursors[6] = new GCursorTexRegion(super.cursors, 32F, 32F, 32F, 32F, 5F, 4F, 6);
        gwindowroot.mouseCursors[7] = new GCursorTexRegion(super.cursors, 64F, 32F, 32F, 32F, 5F, 4F, 7);
        gwindowroot.mouseCursors[8] = new GCursorTexRegion(super.cursors, 96F, 32F, 32F, 32F, 5F, 4F, 8);
        gwindowroot.mouseCursors[9] = new GCursorTexRegion(super.cursors, 0.0F, 64F, 32F, 32F, 5F, 4F, 9);
        gwindowroot.mouseCursors[10] = new GCursorTexRegion(super.cursors, 32F, 64F, 32F, 32F, 5F, 4F, 10);
        gwindowroot.mouseCursors[11] = new GCursorTexRegion(super.cursors, 64F, 64F, 32F, 32F, 5F, 4F, 11);
        gwindowroot.mouseCursors[12] = new GCursorTexRegion(super.cursors, 96F, 64F, 32F, 32F, 5F, 4F, 12);
        gwindowroot.mouseCursors[13] = new GCursorTexRegion(super.cursors, 0.0F, 96F, 32F, 32F, 5F, 4F, 13);
        super.selectMenuIcon = new GTexRegion(super.cursorsStreched, 116F, 104F, 12F, 12F);
        super.subMenuIcon = new GTexRegion(super.cursorsStreched, 116F, 116F, 12F, 12F);
    }

    public GTexture buttons;
    public GTexture buttons2;
    public GTexture basicelements;
    public GBevel bevelGreen;
    public GBevel bevelRed;
    public GBevel bevelBlacked;
    public GBevel bevelComboDown;
    public GBevel bevelComboUp;
    public GBevel bevelButtonDOWN;
    public GBevel bevelButtonUP;
    public GBevel bevelAward;
    protected Sample sndKey1;
    protected Sample sndKey2;
    protected SoundFX guiSound;
    public HashMap sounds;
    public GTexRegion CBButtonUP;
    public GTexRegion CBButtonDOWN;
    float spaceComboList;
    float minScrollMSize;
    float spaceButton;
    float spaceFramedTitle;
    private GRegion _titleRegion;
}