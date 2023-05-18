// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:17:26
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ActiveMissile.java

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class ActiveMissile
{

    public long getLaunchTime()
    {
        return launchTime;
    }

    public void setLaunchTime(long l)
    {
        launchTime = l;
    }

    public boolean isAI()
    {
        return isAI;
    }

    public void setAI(boolean flag)
    {
        isAI = flag;
    }

    public Actor getOwner()
    {
        return theOwner;
    }

    public void setOwner(Actor actor)
    {
        theOwner = actor;
    }

    public int getOwnerArmy()
    {
        return theOwnerArmy;
    }

    public void setOwnerArmy(int i)
    {
        theOwnerArmy = i;
    }

    public Actor getVictim()
    {
        return theVictim;
    }

    public void setVictim(Actor actor)
    {
        theVictim = actor;
    }

    public int getVictimArmy()
    {
        return theVictimArmy;
    }

    public void setVictimArmy(int i)
    {
        theVictimArmy = i;
    }

    public Missile getMissile()
    {
        return theMissile;
    }

    public void setMissile(Missile missile)
    {
        theMissile = missile;
    }

    public boolean isValidMissile()
    {
        if(!Actor.isValid(theMissile))
            return false;
        if(!Actor.isAlive(theMissile))
            return false;
        if(!theMissile.isAlive())
            return false;
        if(!Actor.isValid(theOwner))
            return false;
        if(!Actor.isAlive(theOwner))
            return false;
        if(!theOwner.isAlive())
            return false;
        if(!Actor.isValid(theVictim))
            return false;
        if(!Actor.isAlive(theVictim))
            return false;
        return theVictim.isAlive();
    }

    public ActiveMissile()
    {
    }

    public ActiveMissile(Missile missile, Actor actor, Actor actor1, int i, int j, boolean flag)
    {
        theMissile = missile;
        theOwner = actor;
        theVictim = actor1;
        theOwnerArmy = i;
        theVictimArmy = j;
        isAI = flag;
        launchTime = Time.current();
    }

    private boolean isAI;
    private Actor theOwner;
    private int theOwnerArmy;
    private Actor theVictim;
    private int theVictimArmy;
    private Missile theMissile;
    private long launchTime;
}