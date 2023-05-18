// Source File Name: CockpitF86_Radar.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.*;

public class CockpitF86_Radar extends CockpitPilot {

  protected boolean doFocusEnter() {
    if (super.doFocusEnter()) {
      HookPilot hookpilot = HookPilot.current;
      hookpilot.doAim(false);
      enter();
      go_top();
      return true;
    } else {
      return false;
    }
  }

  protected void doFocusLeave() {
    if (!isFocused()) {
      return;
    } else {
      leave();
      super.doFocusLeave();
      return;
    }
  }

  private void enter() {
    saveFov = Main3D.FOVX;
    Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
    HookPilot hookpilot = HookPilot.current;
    if (hookpilot.isPadlock()) {
      hookpilot.stopPadlock();
    }
    hookpilot.doAim(true);
    hookpilot.setSimpleUse(true);
    HotKeyEnv.enable("PanView", false);
    HotKeyEnv.enable("SnapView", false);
    bEntered = true;
  }

  private void leave() {
    if (!bEntered) {
      return;
    } else {
      Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
      CmdEnv.top().exec("fov " + saveFov);
      HookPilot hookpilot = HookPilot.current;
      hookpilot.doAim(false);
      hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
      hookpilot.setSimpleUse(false);
      boolean flag = HotKeyEnv.isEnabled("aircraftView");
      HotKeyEnv.enable("PanView", flag);
      HotKeyEnv.enable("SnapView", flag);
      bEntered = false;
      return;
    }
  }

  private void go_top() {
    bBAiming = false;
    CmdEnv.top().exec("fov 33.3");
    HookPilot hookpilot = HookPilot.current;
    if (hookpilot.isPadlock()) {
      hookpilot.stopPadlock();
    }
    hookpilot.doAim(false);
    hookpilot.setSimpleUse(true);
    hookpilot.setSimpleAimOrient(aDiv, tDiv, 0.0F);
    HotKeyEnv.enable("PanView", false);
    HotKeyEnv.enable("SnapView", false);
  }

  private void go_bottom() {
    bBAiming = true;
    CmdEnv.top().exec("fov 23.913");
    HookPilot hookpilot = HookPilot.current;
    if (hookpilot.isPadlock()) {
      hookpilot.stopPadlock();
    }
    hookpilot.doAim(true);
    hookpilot.setSimpleUse(true);
    hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
    HotKeyEnv.enable("PanView", false);
    HotKeyEnv.enable("SnapView", false);
    bEntered = true;
  }

  public void destroy() {
    super.destroy();
    leave();
  }

  public void doToggleAim(boolean flag) {
    if (!isFocused()) {
      return;
    }
    if (isToggleAim() == flag) {
      return;
    }
    if (flag) {
      go_bottom();
    } else {
      go_top();
    }
  }

  public CockpitF86_Radar() {
    super("3DO/Cockpit/F86_Radar/hier.him", "he111");
    bEntered = false;
    bBAiming = false;

  }

  public void reflectWorldToInstruments(float f) {
  }
  private float saveFov;
  private float aAim;
  private float tAim;
  private float aDiv;
  private float tDiv;
  private boolean bEntered;
  private boolean bBAiming;

  static {
    java.lang.Class localClass = com.maddox.il2.objects.air.CockpitF86_Radar.class;
    Property.set(localClass, "astatePilotIndx", 0);
  }
}