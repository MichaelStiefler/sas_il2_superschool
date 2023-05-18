// Source File Name: ActiveMissile.java
// Author:           Storebror
package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Actor;

public class ActiveMissile {

  public Actor theOwner;
  public Actor theVictim;
  public int theOwnerArmy;
  public int theVictimArmy;
  public boolean isAI;

  public ActiveMissile(Actor owner, Actor victim, int ownerArmy, int victimArmy, boolean bAI) {
    this.theOwner = owner;
    this.theVictim = victim;
    this.theOwnerArmy = ownerArmy;
    this.theVictimArmy = victimArmy;
    this.isAI = bAI;
  }

  public ActiveMissile() {
  }
}
