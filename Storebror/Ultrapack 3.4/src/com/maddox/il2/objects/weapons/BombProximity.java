package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.rts.Time;

public class BombProximity extends Bomb
{

    public int getFuzeType()
    {
        return fuze != null ? fuze.getFuzeType() : -1;
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(getFuzeType() == 10 && fuze.isArmed())
        {
            pos.getTime(Time.current(), Bomb.P);
            if(pos.getRelPoint().z < (double)fuze.getDetonationDelay() + World.land().HQ(pos.getRelPoint().x, pos.getRelPoint().y))
                doMidAirExplosion();
        }
    }
}
