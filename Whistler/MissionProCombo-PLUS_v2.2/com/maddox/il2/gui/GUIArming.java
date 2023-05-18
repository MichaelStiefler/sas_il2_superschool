package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.rts.*;
import java.io.PrintStream;

public class GUIArming extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bBack)
            {
                GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
                guiairarming.quikPlayer = false;
                guiairarming.quikCurPlane = 0;
                guiairarming.quikPlayerPosition = 0;
                Main.stateStack().pop();
                return true;
            }
            if(gwindow == bAirArming)
            {
                if(bSingleMission)
                    GUIAirArming.stateId = 0;
                else
                    GUIAirArming.stateId = 1;
                Main.stateStack().push(55);
            }
            return super.notify(gwindow, i, j);
        }

        public void render()
        {
            super.render();
//            setCanvasColorWHITE();
            setCanvasColor(GColor.Gray);
            draw(x1024(80F), y1024(32F), x1024g(64F), y1024g(64F), texRegiment);
            setCanvasFont(1);
            draw(x1024(160F), y1024(48F), x1024(784F), y1024(32F), 0, I18N.regimentInfo(regiment.info()));
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(480F), x1024(962F), 2.0F);
            setCanvasFont(0);
            draw(x1024(64F), y1024(144F), x1024(464F), y1024(32F), 1, i18n("arming.Aircraft"));
            draw(x1024(608F), y1024(144F), x1024(384F), y1024(32F), 1, i18n("arming.Weapons"));
            draw(x1024(96F), y1024(536F), x1024(320F), y1024(48F), 0, i18n("arming.Apply"));
            draw(x1024(430F), y1024(536F), x1024(492F), y1024(48F), 2, i18n("arming.WeaponDist"));
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 92F, 1024F, 616F);
            for(int i = 0; i < 4; i++)
            {
                pAircraft[i].set1024PosSize(32F, 192 + i * 64, 544F, 32F);
                cWeapon[i].set1024PosSize(608F, 192 + i * 64, 384F, 32F);
            }

            bBack.setPosC(x1024(56F), y1024(560F));
            bAirArming.setPosC(x1024(960F), y1024(560F));
        }

        public DialogClient()
        {
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


    public void enterPush(GameState gamestate)
    {
        bSingleMission = gamestate.id() == 4;
        super.enterPush(gamestate);
    }

    public void _enter()
    {
        com.maddox.il2.ai.UserCfg usercfg = World.cur().userCfg;
        try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            String s = sectfile.get("MAIN", "player", (String)null);
            String s1 = s.substring(0, s.length() - 1);
            String s2 = s1.substring(0, s1.length() - 1);
            regiment = (Regiment)Actor.getByName(s2);
            Mat mat = PaintScheme.makeMat(regiment.name(), regiment.fileNameTga(), 1.0F, 1.0F, 1.0F);
            texRegiment = GTexture.New(mat.Name());
            int i = sectfile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
            World.cur().setWeaponsConstant(i == 1);
            playerNum = sectfile.get("MAIN", "playerNum", 0);
            slot = new Slot[4];
            playerSlot = -1;
            int j = sectfile.sectionIndex("Wing");
            int k = sectfile.vars(j);
            for(int l = 0; l < k; l++)
            {
                String s3 = sectfile.var(j, l);
                if(s3.startsWith(s1))
                {
                    int j1 = s3.charAt(s3.length() - 1) - 48;
                    if(s3.equals(s))
                        playerSlot = j1;
                    int k1 = sectfile.get(s3, "Planes", 0, 0, 4);
                    Slot slot2 = new Slot();
                    slot[j1] = slot2;
                    slot2.wingName = s3;
                    slot2.players = k1;
                    slot2.fuel = sectfile.get(s3, "Fuel", 100, 0, 100);
                    String s4 = sectfile.get(s3, "Class", (String)null);
                    slot2.planeClass = ObjIO.classForName(s4);
                    slot2.planeKey = Property.stringValue(slot2.planeClass, "keyName", null);
                    String s5 = sectfile.get(s3, "weapons", (String)null);
                    slot2.weapons = Aircraft.getWeaponsRegistered(slot2.planeClass);
                    slot2.weapon = 0;
                    for(int i2 = 0; i2 < slot2.weapons.length; i2++)
                    {
                        if(!slot2.weapons[i2].equals(s5))
                            continue;
                        slot2.weapon = i2;
                        break;
                    }

                }
            }

            for(int i1 = 0; i1 < 4; i1++)
                if(slot[i1] == null)
                {
                    pAircraft[i1].setEnable(false);
                    cWeapon[i1].clear(false);
                } else
                {
                    Slot slot1 = slot[i1];
                    if(World.cur().isWeaponsConstant())
                        slot1.bEnable = false;
                    else
                    if(bSingleMission)
                        slot1.bEnable = true;
                    else
                    if(playerNum == 0)
                    {
                        if(playerSlot == 0)
                            slot1.bEnable = true;
                        else
                            slot1.bEnable = playerSlot == i1;
                    } else
                    {
                        slot1.bEnable = false;
                    }
                    String planeName = (Property.containsValue(slot1.planeClass, "cockpitClass") ? "" : "(AI) ") + I18N.plane(slot1.planeKey);
                    pAircraft[i1].cap = new GCaption((i1 == playerSlot ? ">>> " : "") + slot1.players + "x " + planeName + (i1 == playerSlot ? " <<<" : ""));
//                    if(i1 == playerSlot)
//                        pAircraft[i1].color = 255;
//                    else
                        pAircraft[i1].color = 0;
                    pAircraft[i1].setEnable(true);
                    cWeapon[i1].clear(false);
                    cWeapon[i1].posEnable = new boolean[slot1.weapons.length];
                    if(slot1.bEnable)
                    {
                        for(int l1 = 0; l1 < slot1.weapons.length; l1++)
                        {
                            if(!Aircraft.isWeaponDateOk(slot1.planeClass, slot1.weapons[l1]))
                                cWeapon[i1].posEnable[l1] = false;
                            else
                                cWeapon[i1].posEnable[l1] = true;
                            cWeapon[i1].add(I18N.weapons(slot1.planeKey, slot1.weapons[l1]));
                        }

                        cWeapon[i1].setSelected(slot1.weapon, true, false);
                    } else
                    {
                        cWeapon[i1].add(I18N.weapons(slot1.planeKey, slot1.weapons[slot1.weapon]));
                        cWeapon[i1].setSelected(0, true, false);
                    }
                }

        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            Main.stateStack().pop();
            return;
        }
        client.activateWindow();
    }

    public void _leave()
    {
        try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            for(int i = 0; i < 4; i++)
                if(slot[i] != null)
                {
                    Slot slot1 = slot[i];
                    if(slot1.bEnable)
                        sectfile.set(slot1.wingName, "weapons", slot1.weapons[cWeapon[i].getSelected()]);
                }

            regiment = null;
            texRegiment = null;
            slot = null;
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        World.cur().setUserCovers();
        client.hideWindow();
    }

    private float clampValue(GWindowEditControl gwindoweditcontrol, float f, float f1, float f2)
    {
        String s = gwindoweditcontrol.getValue();
        try
        {
            f = Float.parseFloat(s);
        }
        catch(Exception exception) { }
        if(f < f1)
            f = f1;
        if(f > f2)
            f = f2;
        gwindoweditcontrol.setValue("" + f, false);
        return f;
    }

    public GUIArming(GWindowRoot gwindowroot)
    {
        super(54);
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("arming.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        pAircraft = new GUIPocket[4];
        cWeapon = new GWindowComboControl[4];
        for(int i = 0; i < 4; i++)
        {
            pAircraft[i] = new GUIPocket(dialogClient, "");
            pAircraft[i].setEnable(false);
            cWeapon[i] = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
            cWeapon[i].setEditable(false);
        }

        GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bBack = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bAirArming = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIPocket pAircraft[];
    public GWindowComboControl cWeapon[];
    public GUIButton bAirArming;
    public GUIButton bBack;
    private Regiment regiment;
    private GTexture texRegiment;
    private Slot slot[];
    private int playerNum;
    private int playerSlot;
    private boolean bSingleMission;



}
