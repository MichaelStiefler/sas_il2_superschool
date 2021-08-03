package com.maddox.il2.ai;

import com.maddox.il2.ai.ground.ChiefGround;
import com.maddox.il2.engine.Actor;

class TEscort extends Target
{

    public void destroy()
    {
        super.destroy();
        actor = null;
    }

    boolean checkActor()
    {
        if(actor != null && alive > 0)
            return true;
        if(World.cur().triggersGuard.getListTriggerAircraftSpawn().contains(nameTarget))
            return false;
        actor = Actor.getByName(nameTarget);
        if(actor != null && alive == -1)
        {
            if((actor instanceof ChiefGround) && ((ChiefGround)actor).isPacked() && actor.isAlive())
                return false;
            int i = actor.getOwnerAttachedCount();
            if(i > 0)
            {
                alive = i - Math.round((float)(i * destructLevel) / 100F);
                if(alive == 0)
                    alive = 1;
            }
        }
        return actor != null;
    }

    protected boolean checkPeriodic()
    {
        if(!checkActor())
            return false;
        if(!Actor.isValid(actor))
        {
            setDiedFlag(true);
            return true;
        }
        int i = actor.getOwnerAttachedCount();
        if(i == 0 && !(actor instanceof Wing) || alive == -1)
            return false;
        int j = 0;
        int k = 0;
        for(int l = 0; l < i; l++)
        {
            Actor actor1 = (Actor)actor.getOwnerAttached(l);
            if(!Actor.isAlive(actor1))
                continue;
            j++;
            if(actor1.isTaskComplete())
                k++;
        }

        if(j < alive)
        {
            setDiedFlag(true);
            return true;
        }
        if(k >= alive)
        {
            setTaskCompleteFlag(true);
            setDiedFlag(true);
            return true;
        } else
        {
            return false;
        }
    }

    protected boolean checkActorDied(Actor actor1)
    {
        if(!checkActor())
            return false;
        if(actor == actor1)
        {
            setDiedFlag(true);
            return true;
        } else
        {
            return checkPeriodic();
        }
    }

    protected boolean checkTaskComplete(Actor actor1)
    {
        if(!checkActor())
            return false;
        if(actor1 == actor)
        {
            setTaskCompleteFlag(true);
            setDiedFlag(true);
            return true;
        } else
        {
            return checkPeriodic();
        }
    }

    protected boolean checkTimeoutOff()
    {
        setTaskCompleteFlag(true);
        setDiedFlag(true);
        return true;
    }

    public TEscort(int i, int j, String s, int k)
    {
        super(i, j);
        alive = -1;
        nameTarget = s;
        destructLevel = k;
    }

    String nameTarget;
    Actor actor;
    int destructLevel;
    int alive;
}
