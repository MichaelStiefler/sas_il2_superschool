package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.rts.Time;

public class BombProximity extends Bomb {

    public int getFuzeType() {
        return this.fuze == null ? -1 : this.fuze.getFuzeType();
    }

    public void interpolateTick() {
        super.interpolateTick();
        if ((this.getFuzeType() == 10) && this.fuze.isArmed()) {
            this.pos.getTime(Time.current(), Bomb.P);
            if (this.pos.getRelPoint().z < (this.fuze.getDetonationDelay() + World.land().HQ(this.pos.getRelPoint().x, this.pos.getRelPoint().y))) {
                this.doMidAirExplosion();
            }
        }
    }
}
