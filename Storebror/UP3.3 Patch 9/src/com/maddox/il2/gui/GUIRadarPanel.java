package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.*;
import com.maddox.rts.*;
import java.util.ResourceBundle;

public class GUIRadarPanel extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public void initHotKeyESC()
        {
            String envName = "gui";
            HotKeyEnv.fromIni(envName, Config.cur.ini, "HotKey " + envName);
            HotKeyCmdEnv.addCmd(envName, new HotKeyCmd(true, "activate") {

                public void end()
                {
                    GameState gamestate = Main.state();
                    if(gamestate == null)
                        return;
                    if(BackgroundTask.isExecuted())
                        return;
                    if(isVisible())
                        _leave();
                    RTSConf.cur.hotKeyEnvs.endAllActiveCmd(false);
                    gamestate.doQuitMission();
                }

            }
);
        }

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == sRange)
            {
                fRRange = (bUseKm ? 1000F : 1852F) * fRRangeV[sRange.getState()];
                HUD.log("Range: " + fRRangeV[sRange.getState()] + (bUseKm ? "Km" : "Nmi"));
                return true;
            }
            if(gwindow == sGain)
            {
                fRGain = (float)sGain.getState() / 15F;
                return true;
            }
            if(gwindow == sTunning)
            {
                fRTunning = (float)sTunning.getState() / 15F;
                return true;
            }
            if(gwindow == sSectorAngle)
            {
                fRSectAngle = fRSectAngleV[sSectorAngle.getState()];
                if(bRSectScan)
                    HUD.log("Sector Angle: " + fRSectAngle + "deg.");
                return true;
            }
            if(gwindow == sSectorScan)
            {
                bRSectScan = sSectorScan.isChecked();
                HUD.log("Sector Scan " + (bRSectScan ? "Enabled" : "Disabled"));
                return true;
            }
            if(gwindow == sAzStabilizer)
            {
                bRAzStab = sAzStabilizer.isChecked();
                HUD.log((bRAzStab ? "" : "No ") + "Azimuth Stabilization");
                return true;
            }
            if(gwindow == sKmNmi)
            {
                bUseKm = sKmNmi.isChecked();
                HUD.log((bUseKm ? "Km" : "Nautical Miles") + " Units...");
                return true;
            } else
            {
                update();
                return super.notify(gwindow, i, j);
            }
        }

        public void setPosSize()
        {
            set1024PosSize(800F, 0.0F, 224F, 400F);
            pRadar.setPosSize(x1024(50F), y1024(20F), x1024(138F), M(1.7F));
            sRange.setPosC(x1024(60F), y1024(84F));
            sGain.setPosC(x1024(60F), y1024(148F));
            sTunning.setPosC(x1024(60F), y1024(216F));
            sSectorAngle.setPosC(x1024(60F), y1024(276F));
            sSectorScan.setPosC(x1024(176F), y1024(276F));
            sAzStabilizer.setPosC(x1024(176F), y1024(340F));
            sKmNmi.setPosC(x1024(60F), y1024(340F));
        }

        public void render()
        {
            super.render();
            setCanvasFont(0);
            setCanvasColor(GColor.Gray);
            draw(x1024(95F), y1024(58F), x1024(160F), y1024(48F), 0, i18n("Range") + ": " + (int)fRRangeV[sRange.getState()] + (bUseKm ? " Km" : " Nmi"));
            draw(x1024(95F), y1024(128F), x1024(160F), y1024(48F), 0, i18n("Gain") + ": " + (int)(fRGain * 100F));
            draw(x1024(95F), y1024(184F), x1024(160F), y1024(48F), 0, i18n("Tunning") + ": " + (int)(fRTunning * 100F));
            draw(x1024(45F), y1024(282F), x1024(160F), y1024(48F), 0, i18n("Angle"));
            draw(x1024(134F), y1024(285F), x1024(160F), y1024(48F), 0, i18n("Sector Scan"));
            draw(x1024(131F), y1024(349F), x1024(160F), y1024(48F), 0, i18n("Azimuth Stab."));
            setCanvasColorWHITE();
//            draw(x1024(36F), y1024(117F), x1024g(64F), y1024g(64F), texFull);
//            draw(x1024(36F), y1024(186F), x1024g(64F), y1024g(64F), texFull);
            draw(x1024(36F), y1024(117F), x1024(64F), y1024(64F), texFull);
            draw(x1024(36F), y1024(186F), x1024(64F), y1024(64F), texFull);
        }

        private void initResource()
        {
            try
            {
                resource = ResourceBundle.getBundle("i18n/controls", RTSConf.cur.locale, LDRres.loader());
            }
            catch(Exception exception) { }
        }

        ResourceBundle resource;



        public DialogClient()
        {
        }
    }


    public void _enter()
    {
        update();
        if(!Main3D.cur3D().guiManager.isMouseActive())
        {
            dialogClient.mouseCursor = 3;
            GUI.activate(true, false);
            if(!client.isActivated())
                client.activateWindow();
        }
    }

    public void _leave()
    {
        if(client.isActivated())
        {
            dialogClient.mouseCursor = 1;
            GUI.unActivate();
        }
        client.hideWindow();
    }

    public void update()
    {
    }

    public GUIRadarPanel(GWindowRoot gwindowroot)
    {
        super(100);
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        dialogClient.initResource();
        dialogClient.initHotKeyESC();
        texFull = new GTexRegion("GUI/game/staticelements.mat", 192F, 160F, 64F, 64F);
        pRadar = new GUIPocket(dialogClient, i18n("AN/APQ-13"));
        sGain = (GUISwitchN)dialogClient.addControl(new GUISwitch16(dialogClient, new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
            10, 11, 12, 13, 14
        }, new boolean[] {
            true, true, true, true, true, true, true, true, true, true, 
            true, true, true, true, true
        }));
        sTunning = (GUISwitchN)dialogClient.addControl(new GUISwitch16(dialogClient, new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
            10, 11, 12, 13, 14
        }, new boolean[] {
            true, true, true, true, true, true, true, true, true, true, 
            true, true, true, true, true
        }));
        sRange = (GUISwitchN)dialogClient.addControl(new GUISwitch7(dialogClient, new int[] {
            1, 2, 3, 4, 5
        }, new boolean[] {
            true, true, true, true, true
        }));
        sSectorAngle = (GUISwitchN)dialogClient.addControl(new GUISwitch7(dialogClient, new int[] {
            0, 3, 6
        }, new boolean[] {
            true, true, true
        }));
        sKmNmi = (GUISwitchBox)dialogClient.addControl(new GUISwitchBox(dialogClient));
        sSectorScan = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        sAzStabilizer = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
        texIndicator = GTexture.New("GUI/game/buttons.mat");
        dialogClient.activateWindow();
        client.hideWindow();
        sRange.setState(1, true);
        sSectorAngle.setState(1, true);
        sGain.setState(8, true);
        sTunning.setState(8, true);
    }

    public float fRRange;
    public float fRRangeV[] = {
        5F, 10F, 20F, 50F, 100F
    };
    public float fRGain;
    public float fRTunning;
    public boolean bRSectScan;
    public int fRSectAngle;
    public int fRSectAngleV[] = {
        -90, 0, 90
    };
    public boolean bRAzStab;
    public boolean bUseKm;
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUISwitchN sSectorAngle;
    public GUISwitchN sGain;
    public GUISwitchN sTunning;
    public GUISwitchN sRange;
    public GUISwitchBox3 sSectorScan;
    public GUISwitchBox3 sAzStabilizer;
    public GUISwitchBox sKmNmi;
    public float fFactRange;
    public GUIPocket pRadar;
    public GTexture texIndicator;
    public GTexRegion texFull;
}
