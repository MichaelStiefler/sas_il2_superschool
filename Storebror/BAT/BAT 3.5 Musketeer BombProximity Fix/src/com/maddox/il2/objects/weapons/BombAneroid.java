package com.maddox.il2.objects.weapons;

import com.maddox.rts.Time;

public class BombAneroid extends Bomb {

    public int getFuzeType() {
        return this.fuze == null ? -1 : this.fuze.getFuzeType();
    }

    public void interpolateTick() {
        super.interpolateTick();
        if ((this.getFuzeType() == 8) && this.fuze.isArmed()) {
            this.pos.getTime(Time.current(), Bomb.P);
            if (this.pos.getRelPoint().z < this.fuze.getDetonationDelay()) {
                this.doMidAirExplosion();
            }
        }
    }
}
