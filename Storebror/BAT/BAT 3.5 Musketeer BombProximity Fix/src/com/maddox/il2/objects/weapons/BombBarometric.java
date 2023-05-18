package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.rts.Time;

public class BombBarometric extends Bomb {

    public int getFuzeType() {
        return this.fuze == null ? -1 : this.fuze.getFuzeType();
    }

    public void interpolateTick() {
        super.interpolateTick();
        double groundElevation = 0D;
        switch (this.getFuzeType()) {
            case 10:
                groundElevation = World.land().HQ(this.pos.getRelPoint().x, this.pos.getRelPoint().y);
                // intentional fall-through!
            case 8:
                this.pos.getTime(Time.current(), Bomb.P);
                if (this.pos.getRelPoint().z < (this.fuze.getDetonationDelay() + groundElevation)) {
                    this.doMidAirExplosion();
                }
                break;
        }
    }
}
