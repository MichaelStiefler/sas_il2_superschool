/*Using 4.09 class because TD changed theirs with refly button blocking here...*/
package com.maddox.il2.gui;

import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.game.Main;
import com.maddox.il2.net.NetUser;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetEnv;
import com.maddox.sound.AudioDevice;

public class GUINetClientDMission extends GUINetMission {
    protected void clientRender() {
        GUINetMission.DialogClient dialogclient = this.dialogClient;
        if (this.bEnableReFly) {
            GUINetMission.DialogClient dialogclient_0_ = dialogclient;
            float f = dialogclient.x1024(96.0F);
            float f_1_ = dialogclient.y1024(400.0F);
            float f_2_ = dialogclient.x1024(224.0F);
            float f_3_ = dialogclient.y1024(48.0F);
            if (dialogclient != null) {
                /* empty */
            }
            dialogclient_0_.draw(f, f_1_, f_2_, f_3_, 0, this.i18n("miss.ReFly"));
        }
        GUINetMission.DialogClient dialogclient_4_ = dialogclient;
        float f = dialogclient.x1024(96.0F);
        float f_5_ = dialogclient.y1024(464.0F);
        float f_6_ = dialogclient.x1024(224.0F);
        float f_7_ = dialogclient.y1024(48.0F);
        if (dialogclient != null) {
            /* empty */
        }
        dialogclient_4_.draw(f, f_5_, f_6_, f_7_, 0, this.i18n("miss.Disconnect"));
    }

    protected void doReFly() {
        this.checkCaptured();
        this.destroyPlayerActor();
        ((NetUser) NetEnv.host()).sendStatInc();
        EventLog.onRefly(((NetUser) NetEnv.host()).shortName());
        AudioDevice.soundsOff();
        Main.stateStack().change(40);
    }

    protected void doExit() {
        GUINetClientGuard guinetclientguard = (GUINetClientGuard) Main.cur().netChannelListener;
        if (guinetclientguard != null) guinetclientguard.dlgDestroy(new GUINetClientGuard.DestroyExec() {
            public void destroy(GUINetClientGuard guinetclientguard_9_) {
                GUINetClientDMission.this.checkCaptured();
                GUINetClientDMission.this.destroyPlayerActor();
                ((NetUser) NetEnv.host()).sendStatInc();
                new MsgAction(64, 2.0) {
                    public void doAction() {
                        GUINetClientGuard guinetclientguard_11_ = (GUINetClientGuard) Main.cur().netChannelListener;
                        if (guinetclientguard_11_ != null) guinetclientguard_11_.destroy(true);
                    }
                };
            }
        });
    }

    public GUINetClientDMission(GWindowRoot gwindowroot) {
        super(43);
        this.init(gwindowroot);
        this.infoMenu.info = this.i18n("miss.NetCDInfo");
    }
}