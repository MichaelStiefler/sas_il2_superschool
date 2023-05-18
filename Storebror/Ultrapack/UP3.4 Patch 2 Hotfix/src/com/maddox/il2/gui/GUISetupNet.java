package com.maddox.il2.gui;

import java.util.List;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetSocket;

public class GUISetupNet extends GameState
{
  public GUIClient client;
  public DialogClient dialogClient;
  public GUIInfoMenu infoMenu;
  public GUIInfoName infoName;
  public GWindowComboControl comboSpeed;
  public GUISwitchBox2 sSkinOn;
  public GUIButton bExit;
//  private int[] speed = { 900, 1500, 3000, 5000, 10000, 25000, 100000, 200000 };
  private int[] speed = { 100000, 250000, 1000000, 2000000, 4000000 };

  public void _enter() {
    setComboSpeed();
    this.sSkinOn.setChecked(Config.cur.netSkinDownload, false);

    this.client.activateWindow();
  }

  public void _leave()
  {
    this.client.hideWindow();
  }

  private void setComboSpeed() {
    int speed = Config.cur.netSpeed;
    int comboIndex = 0;

    for (comboIndex = 0; (comboIndex < this.speed.length - 1) && 
      (speed >= (this.speed[comboIndex] + this.speed[(comboIndex + 1)]) / 2); comboIndex++);
    if (comboIndex == this.speed.length - 1)
      comboIndex = this.speed.length - 2;
    this.comboSpeed.setSelected(comboIndex, true, false);
  }

  private void setSpeed(int speed) {
    if (Config.cur.netSpeed == speed)
      return;
    Config.cur.netSpeed = speed;
    List netList = NetEnv.socketsBlock();
    for (int i = 0; i < netList.size(); i++) {
        NetSocket netSocket = (NetSocket)netList.get(i);
        netSocket.setMaxSpeed(speed / 1000.0F);
    }
    netList = NetEnv.socketsNoBlock();
    for (int i = 0; i < netList.size(); i++) {
      NetSocket netSocket = (NetSocket)netList.get(i);
      netSocket.setMaxSpeed(speed / 1000.0F);
    }

    netList = NetEnv.channels();
    for (int i = 0; i < netList.size(); i++) {
        NetChannel netChannel = (NetChannel)netList.get(i);
      if (netChannel.isReady())
          netChannel.setMaxSpeed(speed / 1000.0F);
    }
  }

  public GUISetupNet(GWindowRoot paramGWindowRoot)
  {
    super(52);
    this.client = ((GUIClient)paramGWindowRoot.create(new GUIClient()));
    this.dialogClient = ((DialogClient)this.client.create(new DialogClient()));

    this.infoMenu = ((GUIInfoMenu)this.client.create(new GUIInfoMenu()));
    this.infoMenu.info = i18n("setupNet.info");
    this.infoName = ((GUIInfoName)this.client.create(new GUIInfoName()));

    GTexture localGTexture = ((GUILookAndFeel)paramGWindowRoot.lookAndFeel()).buttons2;

    this.dialogClient.addControl(this.comboSpeed = new GWindowComboControl(this.dialogClient, 0.0F, 0.0F, 1.0F));
    this.comboSpeed.setEditable(false);
    this.comboSpeed.resized();
//    this.comboSpeed.add(i18n("setupNet.Modem(9.6K)"));
//    this.comboSpeed.add(i18n("setupNet.Modem(14.4K)"));
//    this.comboSpeed.add(i18n("setupNet.Modem(28.8K)"));
//    this.comboSpeed.add(i18n("setupNet.Modem(56K)"));
//    this.comboSpeed.add(i18n("setupNet.ISDN"));
//    this.comboSpeed.add(i18n("setupNet.Cable,xDSL"));
//    this.comboSpeed.add(i18n("setupNet.LAN"));
    this.comboSpeed.add(i18n("setupNet.0MB"));
    this.comboSpeed.add(i18n("setupNet.1MB"));
    this.comboSpeed.add(i18n("setupNet.10MB"));
    this.comboSpeed.add(i18n("setupNet.100MB"));

    this.sSkinOn = ((GUISwitchBox2)this.dialogClient.addControl(new GUISwitchBox2(this.dialogClient)));
    this.bExit = ((GUIButton)this.dialogClient.addEscape(new GUIButton(this.dialogClient, localGTexture, 0.0F, 96.0F, 48.0F, 48.0F)));

    this.dialogClient.activateWindow();
    this.client.hideWindow();
  }

  public class DialogClient extends GUIDialogClient
  {
    public DialogClient()
    {
    }

    public boolean notify(GWindow paramGWindow, int paramInt1, int paramInt2)
    {
      if (paramInt1 != 2) return super.notify(paramGWindow, paramInt1, paramInt2);

      if (paramGWindow == GUISetupNet.this.comboSpeed) {
        int i = GUISetupNet.this.comboSpeed.getSelected();
        if (i < 0) return true;
        int j = GUISetupNet.this.speed[i];
        GUISetupNet.this.setSpeed(j);
        return true;
      }
      if (paramGWindow == GUISetupNet.this.bExit) {
        boolean bool = GUISetupNet.this.sSkinOn.isChecked();
        if (Config.cur.netSkinDownload != bool) {
          Config.cur.netSkinDownload = bool;
        }

        Main.stateStack().pop();
        return true;
      }
      return super.notify(paramGWindow, paramInt1, paramInt2);
    }

    public void render() {
      super.render();
      GUISeparate.draw(this, GColor.Gray, x1024(32.0F), y1024(176.0F), x1024(512.0F), 2.0F);
      setCanvasColor(GColor.Gray);
      setCanvasFont(0);
      draw(x1024(32.0F), y1024(32.0F), x1024(208.0F), y1024(32.0F), 0, GUISetupNet.this.i18n("setupNet.Internet"));

      draw(x1024(32.0F), y1024(96.0F), x1024(432.0F), y1024(48.0F), 2, GUISetupNet.this.i18n("setupNet.Skin"));
      draw(x1024(88.0F), y1024(208.0F), x1024(136.0F), y1024(48.0F), 0, GUISetupNet.this.i18n("setupNet.Back"));
    }

    public void setPosSize() {
      set1024PosSize(224.0F, 176.0F, 576.0F, 288.0F);

      GUISetupNet.this.comboSpeed.set1024PosSize(256.0F, 32.0F, 272.0F, 32.0F);

      GUISetupNet.this.sSkinOn.setPosC(x1024(520.0F), y1024(120.0F));
      GUISetupNet.this.bExit.setPosC(x1024(56.0F), y1024(232.0F));
    }
  }
}