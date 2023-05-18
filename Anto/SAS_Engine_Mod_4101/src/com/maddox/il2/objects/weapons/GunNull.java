// Source File Name: GunNull.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.air.TypeGuidedMissileCarrier;

public class GunNull extends Gun {

  public GunNull() {
    flags = flags | 0x4004;
    pos = new ActorPosMove(this);
  }
  private boolean hasBullets = true;

  public void emptyGun() {
    this.hasBullets = false;
  }

  public BulletEmitter detach(HierMesh hiermesh, int i) {
    return this;
  }

  public void initRealisticGunnery(boolean flag) {
  }

  public boolean isEnablePause() {
    return false;
  }

  public float bulletMassa() {
    return 0.0F;
  }

  public float bulletSpeed() {
    return 0.0F;
  }

  public int countBullets() {
    if (this.getOwner() != null) {
      if (this.getOwner() instanceof TypeGuidedMissileCarrier) {
        return ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().hasMissiles() ? Integer.MAX_VALUE : 0;
      }
    }
    return this.hasBullets ? Integer.MAX_VALUE : 0;
  }

  public boolean haveBullets() {
    if (this.getOwner() != null) {
      if (this.getOwner() instanceof TypeGuidedMissileCarrier) {
        return ((TypeGuidedMissileCarrier) this.getOwner()).getGuidedMissileUtils().hasMissiles();
      }
    }
    return this.hasBullets;
  }

  public void loadBullets() {
    com.maddox.il2.ai.EventLog.type("loadBullets");
    this.hasBullets = true;
  }

  public void loadBullets(int i) {
    com.maddox.il2.ai.EventLog.type("loadBullets " + i);
    this.hasBullets = true;
  }

  public Class bulletClass() {
    return null;
  }

  public void setBulletClass(Class class1) {
  }

  public boolean isShots() {
    return false;
  }

  public void shots(int i, float f) {
  }

  public void shots(int i) {
  }

  public String getHookName() {
    return "Body";
  }

  public void set(Actor actor, String s, Loc loc) {
  }

  public void set(Actor actor, String s, String s1) {
  }

  public void set(Actor actor, String s) {
  }
}
