package com.maddox.il2.gui;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexRegion;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.net.USGS;

public class GUIMainMenu extends GameState {
    public class DialogClient extends GUIDialogClient {

        public boolean notify(GWindow gwindow, int i, int j) {
            if (i != 2) return super.notify(gwindow, i, j);
            if (gwindow == GUIMainMenu.this.bInfo) {
                Main.stateStack().push(GameState.VIEW);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bPilot) {
                Main.stateStack().push(GameState.PLAYER_SELECT);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bControls) {
                Main.stateStack().push(GameState.CONTROLS);
                return true;
            }
            // TODO: +++ Mods Settings GUI by SAS~Storebror +++
            if (gwindow == GUIMainMenu.this.bMods) {
                Main.stateStack().push(GameState.MOD_SETTINGS);
                return true;
            }
            // TODO: --- Mods Settings GUI by SAS~Storebror ---
            if (gwindow == GUIMainMenu.this.bSingle) {
                Main.stateStack().push(GameState.SINGLE_SELECT);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bCampaigns) {
                Main.stateStack().push(GameState.CAMPAIGNS);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bQuick) {
                Main.stateStack().push(GameState.QUICK);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bTraining) {
                Main3D.cur3D().viewSet_Save();
                Main.stateStack().push(GameState.TRAINING_SELECT);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bRecord) {
                Main3D.cur3D().viewSet_Save();
                Main.stateStack().push(GameState.RECORD_SELECT);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bBuilder) {
                Main.stateStack().push(GameState.BUILDER);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bMultiplay) {
                Main.stateStack().push(GameState.NET);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bSetup) {
                Main.stateStack().push(GameState.SETUP);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bCredits) {
                Main.stateStack().push(GameState.CREDITS);
                return true;
            }
            if (gwindow == GUIMainMenu.this.bExit) {
                new GWindowMessageBox(this.root, 20F, true, GUIMainMenu.this.i18n("main.ConfirmQuit"), GUIMainMenu.this.i18n("main.ReallyQuit"), 1, 0.0F) {

                    public void result(int k) {
                        if (k == 3) Main.doGameExit();
                        else GUIMainMenu.this.client.activateWindow();
                    }

                };
                return true;
            } else return super.notify(gwindow, i, j);
        }

        public void render() {
            super.render();
            GUISeparate.draw(this, GColor.Gray, this.x1024(32F), this.y1024(336F), this.x1024(336F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, this.x1024(32F), this.y1024(496F), this.x1024(336F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, this.x1024(384F), this.y1024(32F), 2.0F, this.y1024(560F));
            GUISeparate.draw(this, GColor.Gray, this.x1024(400F), this.y1024(336F), this.x1024(288F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, this.x1024(400F), this.y1024(496F), this.x1024(288F), 2.0F);
            this.setCanvasColor(GColor.Gray);
            this.setCanvasFont(0);
            this.draw(this.x1024(104F), this.y1024(64F) - this.M(1.0F), this.x1024(256F), this.M(2.0F), 0, GUIMainMenu.this.i18n("main.SingleMissions"));
            this.draw(this.x1024(104F), this.y1024(120F) - this.M(1.0F), this.x1024(256F), this.M(2.0F), 0, GUIMainMenu.this.i18n("main.PilotCareer"));
            this.draw(this.x1024(104F), this.y1024(176F) - this.M(1.0F), this.x1024(256F), this.M(2.0F), 0, GUIMainMenu.this.i18n("main.Multiplay"));
            this.draw(this.x1024(104F), this.y1024(232F) - this.M(1.0F), this.x1024(256F), this.M(2.0F), 0, GUIMainMenu.this.i18n("main.Quick"));
            this.draw(this.x1024(104F), this.y1024(288F) - this.M(1.0F), this.x1024(256F), this.M(2.0F), 0, GUIMainMenu.this.i18n("main.Builder"));
            this.draw(this.x1024(104F), this.y1024(376F) - this.M(1.0F), this.x1024(256F), this.M(2.0F), 0, GUIMainMenu.this.i18n("main.Training"));
            this.draw(this.x1024(104F), this.y1024(457F) - this.M(1.0F), this.x1024(256F), this.M(2.0F), 0, GUIMainMenu.this.i18n("main.PlayTrack"));
            GUILookAndFeel guilookandfeel = (GUILookAndFeel) this.lookAndFeel();
            guilookandfeel.drawBevel(this, this.x1024(152F), this.y1024(560F) - this.M(1.0F), this.x1024(128F), this.y1024(16F) + this.M(2.0F), guilookandfeel.bevelRed, guilookandfeel.basicelements);
            this.draw(this.x1024(168F), this.y1024(568F) - this.M(1.0F), this.x1024(112F), this.M(2.0F), 0, GUIMainMenu.this.i18n("main.Quit"));
            guilookandfeel.drawBevel(this, this.x1024(416F), this.y1024(40F), this.x1024(264F), this.y1024(64F), guilookandfeel.bevelBlacked, guilookandfeel.basicelements);
            this.setCanvasFont(1);
            this.draw(this.x1024(416F), this.y1024(40F), this.x1024(272F), this.y1024(64F), 1, GUIMainMenu.this.i18n("main.PilotSelector"));
            this.setCanvasFont(0);
            // TODO: +++ Mods Settings GUI by SAS~Storebror +++
//            draw(x1024(416F), y1024(148F) - M(1.0F), x1024(200F), M(2.0F), 2, i18n("main.Pilot"));
//            draw(x1024(416F), y1024(288F) - M(1.0F), x1024(200F), M(2.0F), 2, i18n("main.Controls"));
            this.draw(this.x1024(416F), this.y1024(140F) - this.M(1.0F), this.x1024(200F), this.M(2.0F), 2, GUIMainMenu.this.i18n("main.Pilot"));
            this.draw(this.x1024(416F), this.y1024(240F) - this.M(1.0F), this.x1024(200F), this.M(2.0F), 2, GUIMainMenu.this.i18n("main.Controls"));
            // TODO: --- Mods Settings GUI by SAS~Storebror ---
            this.draw(this.x1024(416F), this.y1024(298F) - this.M(1.0F), this.x1024(200F), this.M(2.0F), 2, "Mods");

            this.draw(this.x1024(416F), this.y1024(376F) - this.M(1.0F), this.x1024(200F), this.M(2.0F), 2, GUIMainMenu.this.i18n("main.ViewObjects"));
            this.draw(this.x1024(416F), this.y1024(457F) - this.M(1.0F), this.x1024(200F), this.M(2.0F), 2, GUIMainMenu.this.i18n("main.Credits"));
            this.draw(this.x1024(416F), this.y1024(568F) - this.M(1.0F), this.x1024(200F), this.M(2.0F), 2, GUIMainMenu.this.i18n("main.Setup"));
        }

        public void setPosSize() {
            this.set1024PosSize(144F, 80F, 720F, 624F);
            GUIMainMenu.this.bSingle.setPosC(this.x1024(56F), this.y1024(64F));
            GUIMainMenu.this.bCampaigns.setPosC(this.x1024(56F), this.y1024(120F));
            GUIMainMenu.this.bMultiplay.setPosC(this.x1024(56F), this.y1024(176F));
            GUIMainMenu.this.bQuick.setPosC(this.x1024(56F), this.y1024(232F));
            GUIMainMenu.this.bBuilder.setPosC(this.x1024(56F), this.y1024(288F));
            GUIMainMenu.this.bTraining.setPosC(this.x1024(56F), this.y1024(376F));
            GUIMainMenu.this.bRecord.setPosC(this.x1024(56F), this.y1024(457F));
            GUIMainMenu.this.bExit.setPosC(this.x1024(120F), this.y1024(568F));
            // TODO: +++ Mods Settings GUI by SAS~Storebror +++
//            bPilot.setPosC(x1024(664F), y1024(148F));
//            pPilotName.setPosSize(x1024(424F), y1024(204F), x1024(264F), y1024(32F));
//            bControls.setPosC(x1024(664F), y1024(288F));
            GUIMainMenu.this.bPilot.setPosC(this.x1024(664F), this.y1024(140F));
            GUIMainMenu.this.pPilotName.setPosSize(this.x1024(424F), this.y1024(176F), this.x1024(264F), this.y1024(32F));
            GUIMainMenu.this.bControls.setPosC(this.x1024(664F), this.y1024(240F));
            GUIMainMenu.this.bMods.setPosC(this.x1024(664F), this.y1024(298F));
            // TODO: --- Mods Settings GUI by SAS~Storebror ---

            GUIMainMenu.this.bInfo.setPosC(this.x1024(664F), this.y1024(376F));
            GUIMainMenu.this.bCredits.setPosC(this.x1024(664F), this.y1024(457F));
            GUIMainMenu.this.bSetup.setPosC(this.x1024(664F), this.y1024(568F));
        }

        public DialogClient() {
        }
    }

    public void enterPop(GameState gamestate) {
        if (USGS.isUsing()) {
            Main.doGameExit();
            return;
        }
        if (Main.cur().netGameSpy != null) {
            Main.cur().netGameSpy.sendExiting();
            Main.doGameExit();
            return;
        } else {
            this._enter();
            return;
        }
    }

    public void _enter() {
        ((GUIRoot) this.dialogClient.root).setBackCountry(null, null);
        this.client.activateWindow();
    }

    public void _leave() {
        this.client.hideWindow();
    }

    public GUIMainMenu(GWindowRoot gwindowroot) {
        super(2);
        this.client = (GUIClient) gwindowroot.create(new GUIClient());
        this.dialogClient = (DialogClient) this.client.create(new DialogClient());
        this.infoMenu = (GUIInfoMenu) this.client.create(new GUIInfoMenu());
        this.infoMenu.info = this.i18n("main.info");
        this.infoName = (GUIInfoName) this.client.create(new GUIInfoName());
        GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
        this.bCampaigns = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bSingle = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bQuick = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bMultiplay = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bBuilder = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bTraining = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        this.bRecord = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bExit = (GUIButton) this.dialogClient.addEscape(new GUIButton(this.dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        this.bPilot = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bControls = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bMods = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bInfo = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bCredits = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bSetup = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.setup = WindowPreferences.create(gwindowroot);
        this.setup.hideWindow();
        this.texReg12B = new GTexRegion("GUI/game/staticelements.mat", 128F, 32F, 96F, 48F);
        this.pPilotName = new GUIPocket(this.dialogClient, "Demo 'demo' Player");
        this.dialogClient.activateWindow();
        this.client.hideWindow();

        // TODO: +++ Mods Settings GUI by SAS~Storebror +++
        new GUIModSettings(gwindowroot);
        // TODO: --- Mods Settings GUI by SAS~Storebror ---
    }

    GWindowFramed       setup;
    public GUIClient    client;
    public DialogClient dialogClient;
    public GUIInfoMenu  infoMenu;
    public GUIInfoName  infoName;
    public GTexRegion   texReg12B;
    public GUIPocket    pPilotName;
    public GUIButton    bInfo;
    public GUIButton    bPilot;
    public GUIButton    bControls;
    // TODO: +++ Mods Settings GUI by SAS~Storebror +++
    public GUIButton    bMods;
    // TODO: --- Mods Settings GUI by SAS~Storebror ---
    public GUIButton    bSingle;
    public GUIButton    bCampaigns;
    public GUIButton    bQuick;
    public GUIButton    bTraining;
    public GUIButton    bRecord;
    public GUIButton    bBuilder;
    public GUIButton    bMultiplay;
    public GUIButton    bSetup;
    public GUIButton    bCredits;
    public GUIButton    bExit;
}
