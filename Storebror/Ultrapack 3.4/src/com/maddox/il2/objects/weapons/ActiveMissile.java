// Source File Name: ActiveMissile.java
// Author:           Storebror
package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Time;

public class ActiveMissile {

    private boolean isAI;
    private Actor   theOwner;
    private int     theOwnerArmy;
    private Actor   theVictim;
    private int     theVictimArmy;
    private Missile theMissile;
    private long    launchTime;

    public long getLaunchTime() {
        return this.launchTime;
    }

    public void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }

    public boolean isAI() {
        return this.isAI;
    }

    public void setAI(boolean isAI) {
        this.isAI = isAI;
    }

    public Actor getOwner() {
        return this.theOwner;
    }

    public void setOwner(Actor theOwner) {
        this.theOwner = theOwner;
    }

    public int getOwnerArmy() {
        return this.theOwnerArmy;
    }

    public void setOwnerArmy(int theOwnerArmy) {
        this.theOwnerArmy = theOwnerArmy;
    }

    public Actor getVictim() {
        return this.theVictim;
    }

    public void setVictim(Actor theVictim) {
        this.theVictim = theVictim;
    }

    public int getVictimArmy() {
        return this.theVictimArmy;
    }

    public void setVictimArmy(int theVictimArmy) {
        this.theVictimArmy = theVictimArmy;
    }

    public Missile getMissile() {
        return this.theMissile;
    }

    public void setMissile(Missile theMissile) {
        this.theMissile = theMissile;
    }

    public boolean isValidMissile() {
        if (!Actor.isValid(this.theMissile)) return false;
        if (!Actor.isAlive(this.theMissile)) return false;
        if (!this.theMissile.isAlive()) return false;
        if (!Actor.isValid(this.theOwner)) return false;
        if (!Actor.isAlive(this.theOwner)) return false;
        if (!this.theOwner.isAlive()) return false;
        if (!Actor.isValid(this.theVictim)) return false;
        if (!Actor.isAlive(this.theVictim)) return false;
        if (!this.theVictim.isAlive()) return false;
        return true;
    }

    public ActiveMissile() {
    }

    public ActiveMissile(Missile missile, Actor owner, Actor victim, int ownerArmy, int victimArmy, boolean bAI) {
        this.theMissile = missile;
        this.theOwner = owner;
        this.theVictim = victim;
        this.theOwnerArmy = ownerArmy;
        this.theVictimArmy = victimArmy;
        this.isAI = bAI;
        this.launchTime = Time.current();
    }
}
