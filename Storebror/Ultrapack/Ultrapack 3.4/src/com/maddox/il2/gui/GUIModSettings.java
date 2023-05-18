/*
 * New Mod Settings GUI by SAS~Storebror
 */

package com.maddox.il2.gui;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowHSliderInt;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.rts.RTSConf;
import com.maddox.rts.RTSConfWin;
import com.maddox.sas1946.il2.util.Reflection;

public class GUIModSettings extends GameState {
    public GUIClient          client;
    public DialogClient       dialogClient;
    public GUIInfoMenu        infoMenu;
    public GUIInfoName        infoName;
    public GUISwitchBox3      sStabsForAll;
    public GUISwitchBox3      sNewTIR;
    public GUISwitchBox3      sHighSpeedNet;
    public GUISwitchBox3      sAddDefaultCountryNone;
    public GUISwitchBox3      sAutoNtrkRecording;
    public GUISwitchBox3      sAutoAdminLogin;
    public GWindowEditControl wAdminPassword;
    public GUISwitchBox3      sAutoUserLogin;
    public GWindowEditControl wUserPassword;
    public GUISwitchBox3      sOverrideOnlineCallsign;
    public GWindowEditControl wOnlineCallsign;
    public GUISwitchBox3      sSkinDownloadNotifications;

    public GWindowHSliderInt  hsDarkness;
    public GWindowHSliderInt  hsDiffuse;

    public GUIButton          bExit;

    private boolean           bUseTrackIR;

    public class DialogClient extends GUIDialogClient {
        public boolean notify(GWindow gwindow, int i, int i_0_) {
            if (i != 2) return super.notify(gwindow, i, i_0_);
            if (gwindow == GUIModSettings.this.bExit) {
                Main.stateStack().pop();
                return true;
            }
            return super.notify(gwindow, i, i_0_);
        }

        public void render() {
            super.render();
            GUISeparate.draw(this, GColor.Gray, this.x1024(32.0F), this.y1024(464.0F), this.x1024(768.0F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, this.x1024(32.0F), this.y1024(544.0F), this.x1024(768.0F), 2.0F);
            this.setCanvasColor(GColor.Gray);
            this.setCanvasFont(0);
            this.draw(this.x1024(96.0F), this.y1024(577.0F), this.x1024(224.0F), this.y1024(48.0F), 0, GUIModSettings.this.i18n("diff.Back"));
            float y = 32F;
            float x = 128F;
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "Stabilizers on all Aircraft");
            y += 64F;
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "Use Network File Transfer Boost");
            y += 64F;
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "Add Default Country \"None\"");
            y += 64F;
            if (GUIModSettings.this.bUseTrackIR) {
                this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "Use new TrackIR code");
                y += 64F;
            }
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "Automatic Track Recording");
            y += 64F;

            this.draw(this.x1024(20F), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "Night Darkness");
            this.draw(this.x1024(155F), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "dark");
            this.draw(this.x1024(392F), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "default");
            y += 64F;
            this.draw(this.x1024(20F), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "Diffuse Moonlight");
            this.draw(this.x1024(155F), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "dark");
            this.draw(this.x1024(392F), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 0, "default");
            y += 64F;

            y = 32F;
            x = 400F;
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 2, "Online Auto Admin Login");
            y += 64F;
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 2, "Online Admin Password");
            y += 64F;
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 2, "Online Auto User Login");
            y += 64F;
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 2, "Online User Password");
            y += 64F;
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 2, "Use Custom Online Callsign");
            y += 64F;
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 2, "Custom Online Callsign");
            y += 64F;
            this.draw(this.x1024(x), this.y1024(y), this.x1024(272.0F), this.y1024(48.0F), 2, "Skin Download Notifications");
            y += 64F;
        }

        public void setPosSize() {
            this.set1024PosSize(92.0F, 72.0F, 832.0F, 656.0F);
            float y = 56F;
            float x = 88F;
            GUIModSettings.this.sStabsForAll.setPosC(this.x1024(x), this.y1024(y));
            y += 64F;
            GUIModSettings.this.sHighSpeedNet.setPosC(this.x1024(x), this.y1024(y));
            y += 64F;
            GUIModSettings.this.sAddDefaultCountryNone.setPosC(this.x1024(x), this.y1024(y));
            y += 64F;
            if (GUIModSettings.this.bUseTrackIR) {
                GUIModSettings.this.sNewTIR.setPosC(this.x1024(x), this.y1024(y));
                y += 64F;
            }
            GUIModSettings.this.sAutoNtrkRecording.setPosC(this.x1024(x), this.y1024(y));
            y += 64F;

            GUIModSettings.this.hsDarkness.set1024PosSize(x + 100F, y - 12F, 200F, 32F);
            y += 64F;
            GUIModSettings.this.hsDiffuse.set1024PosSize(x + 100F, y - 12F, 200F, 32F);
            y += 64F;

            y = 56F;
            x = 760F;
            float xEditSize = 120F;
            float yEditSize = 32F;
            float xEditOffset = xEditSize / 2F;
            float yEditOffset = yEditSize / 2F;

            GUIModSettings.this.sAutoAdminLogin.setPosC(this.x1024(x), this.y1024(y));
            y += 64F;
            GUIModSettings.this.wAdminPassword.set1024PosSize(x - xEditOffset, y - yEditOffset, xEditSize, yEditSize);
            y += 64F;
            GUIModSettings.this.sAutoUserLogin.setPosC(this.x1024(x), this.y1024(y));
            y += 64F;
            GUIModSettings.this.wUserPassword.set1024PosSize(x - xEditOffset, y - yEditOffset, xEditSize, yEditSize);
            y += 64F;
            GUIModSettings.this.sOverrideOnlineCallsign.setPosC(this.x1024(x), this.y1024(y));
            y += 64F;
            GUIModSettings.this.wOnlineCallsign.set1024PosSize(x - xEditOffset, y - yEditOffset, xEditSize, yEditSize);
            y += 64F;
            GUIModSettings.this.sSkinDownloadNotifications.setPosC(this.x1024(x), this.y1024(y));
            y += 64F;

            GUIModSettings.this.bExit.setPosC(this.x1024(56.0F), this.y1024(602.0F));
        }
    }

    public void enterPush(GameState gamestate) {
        this._enter();
    }

    public void _enter() {
        this.reset();
        this.sStabsForAll.setEnable(true);
        this.sHighSpeedNet.setEnable(true);
        this.sAddDefaultCountryNone.setEnable(true);
        if (this.bUseTrackIR) this.sNewTIR.setEnable(true);
        this.sAutoNtrkRecording.setEnable(true);
        this.sAutoAdminLogin.setEnable(true);
        this.wAdminPassword.setEnable(true);
        this.sAutoUserLogin.setEnable(true);
        this.wUserPassword.setEnable(true);
        this.sOverrideOnlineCallsign.setEnable(true);
        this.wOnlineCallsign.setEnable(true);
        this.sSkinDownloadNotifications.setEnable(true);
        this.hsDarkness.setEnable(true);
        this.hsDiffuse.setEnable(true);
        this.client.activateWindow();
    }

    private void reset() {
        this.sStabsForAll.setChecked(Config.cur.bStabs4All, false);
        this.sHighSpeedNet.setChecked(Config.cur.bNetBoost, false);
        this.sAddDefaultCountryNone.setChecked(Config.cur.bAddDefaultCountryNone, false);
        if (this.bUseTrackIR) this.sNewTIR.setChecked(Config.cur.bNewTrackIR, false);
        this.sAutoNtrkRecording.setChecked(Config.cur.bAutoNtrkRecording, false);
        this.sAutoAdminLogin.setChecked(Config.cur.bUseAutoAdminLogin, false);
        this.wAdminPassword.setValue(Config.cur.sAutoAdminPassword);
        this.sAutoUserLogin.setChecked(Config.cur.bUseAutoUserLogin, false);
        this.wUserPassword.setValue(Config.cur.sAutoUserPassword);
        this.sOverrideOnlineCallsign.setChecked(Config.cur.bOverrideOnlineCallsign, false);
        this.wOnlineCallsign.setValue(Config.cur.sOnlineCallsign);
        this.sSkinDownloadNotifications.setChecked(Config.cur.bSkinDownloadNotifications, false);
        this.hsDarkness.setPos(Config.cur.iDarkness, false);
        this.hsDiffuse.setPos(Config.cur.iDiffuse, false);
    }

    public void _leave() {
        Config.cur.bStabs4All = this.sStabsForAll.bChecked;
        Config.cur.bNetBoost = this.sHighSpeedNet.bChecked;
        if (this.sHighSpeedNet.bChecked) Config.cur.netSpeed = Config.NET_SPEED_HIGH;
        Config.cur.bAddDefaultCountryNone = this.sAddDefaultCountryNone.bChecked;
        if (this.bUseTrackIR) {
            ((RTSConfWin) RTSConf.cur).trackIRWin.destroy();
            Config.cur.bNewTrackIR = this.sNewTIR.bChecked;
            ((RTSConfWin) RTSConf.cur).trackIRWin.create();
            Reflection.setBoolean(((RTSConfWin) RTSConf.cur).trackIR, "bExist", ((RTSConfWin) RTSConf.cur).trackIRWin.isCreated());
        }
        Config.cur.bAutoNtrkRecording = this.sAutoNtrkRecording.bChecked;

        Config.cur.bUseAutoAdminLogin = this.sAutoAdminLogin.bChecked;
        Config.cur.sAutoAdminPassword = this.wAdminPassword.getValue();
        Config.cur.bUseAutoUserLogin = this.sAutoUserLogin.bChecked;
        Config.cur.sAutoUserPassword = this.wUserPassword.getValue();
        Config.cur.bOverrideOnlineCallsign = this.sOverrideOnlineCallsign.bChecked;
        Config.cur.sOnlineCallsign = this.wOnlineCallsign.getValue();
        Config.cur.bSkinDownloadNotifications = this.sSkinDownloadNotifications.bChecked;
        Config.cur.iDarkness = this.hsDarkness.pos();
        Config.cur.iDiffuse = this.hsDiffuse.pos();

        this.client.hideWindow();
    }

    protected void clientInit(GWindowRoot gwindowroot) {
    }

    public GUIModSettings(GWindowRoot gwindowroot) {
        this(gwindowroot, MOD_SETTINGS);
    }

    protected GUIModSettings(GWindowRoot gwindowroot, int i) {
        super(i);
        if (RTSConf.cur.isUseTrackIR()) if (RTSConf.cur instanceof RTSConfWin) this.bUseTrackIR = true;
        this.client = (GUIClient) gwindowroot.create(new GUIClient());
        this.dialogClient = (DialogClient) this.client.create(new DialogClient());
        this.infoMenu = (GUIInfoMenu) this.client.create(new GUIInfoMenu());
        this.infoMenu.info = "Mod Settings";
        this.infoName = (GUIInfoName) this.client.create(new GUIInfoName());
        GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
        this.bExit = (GUIButton) this.dialogClient.addEscape(new GUIButton(this.dialogClient, gtexture, 0.0F, 96.0F, 48.0F, 48.0F));
        this.sStabsForAll = (GUISwitchBox3) this.dialogClient.addControl(new GUISwitchBox3(this.dialogClient));
        this.sHighSpeedNet = (GUISwitchBox3) this.dialogClient.addControl(new GUISwitchBox3(this.dialogClient));
        if (this.bUseTrackIR) this.sNewTIR = (GUISwitchBox3) this.dialogClient.addControl(new GUISwitchBox3(this.dialogClient));
        this.sAddDefaultCountryNone = (GUISwitchBox3) this.dialogClient.addControl(new GUISwitchBox3(this.dialogClient));
        this.sAutoNtrkRecording = (GUISwitchBox3) this.dialogClient.addControl(new GUISwitchBox3(this.dialogClient));
        this.sSkinDownloadNotifications = (GUISwitchBox3) this.dialogClient.addControl(new GUISwitchBox3(this.dialogClient));

        this.sAutoAdminLogin = (GUISwitchBox3) this.dialogClient.addControl(new GUISwitchBox3(this.dialogClient));
        this.wAdminPassword = (GWindowEditControl) this.dialogClient.addControl(new GWindowEditControl(this.dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
        this.wAdminPassword.bNumericOnly = false;
        this.wAdminPassword.bDelayedNotify = true;
        this.wAdminPassword.align = 1;

        this.sAutoUserLogin = (GUISwitchBox3) this.dialogClient.addControl(new GUISwitchBox3(this.dialogClient));
        this.wUserPassword = (GWindowEditControl) this.dialogClient.addControl(new GWindowEditControl(this.dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
        this.wUserPassword.bNumericOnly = false;
        this.wUserPassword.bDelayedNotify = true;
        this.wUserPassword.align = 1;

        this.sOverrideOnlineCallsign = (GUISwitchBox3) this.dialogClient.addControl(new GUISwitchBox3(this.dialogClient));
        this.wOnlineCallsign = (GWindowEditControl) this.dialogClient.addControl(new GWindowEditControl(this.dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
        this.wOnlineCallsign.bNumericOnly = false;
        this.wOnlineCallsign.bDelayedNotify = true;
        this.wOnlineCallsign.align = 1;

        this.dialogClient.addControl(this.hsDarkness = new GWindowHSliderInt(this.dialogClient));
        this.hsDarkness.setRange(0, Config.MAX_NIGHT_SETTINGS, Config.MAX_NIGHT_SETTINGS);
        this.dialogClient.addControl(this.hsDiffuse = new GWindowHSliderInt(this.dialogClient));
        this.hsDiffuse.setRange(0, Config.MAX_NIGHT_SETTINGS, Config.MAX_NIGHT_SETTINGS);

        this.clientInit(gwindowroot);
        this.dialogClient.activateWindow();
        this.client.hideWindow();
    }
}