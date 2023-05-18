package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;

public class BombAirburst extends Bomb {

    public int getFuzeType() {
        return this.fuze == null ? -1 : this.fuze.getFuzeType();
    }

    public void start() {
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(0.9F, 1.6F);
    }

    public void interpolateTick() {
        super.interpolateTick();
        if ((this.getFuzeType() == 7) && this.fuze.isArmed() && (this.curTm > (this.ttcurTM + this.delayExplosion))) {
            this.doExplosion(this, "Airburst_Bomb");
        }
    }

    protected void doExplosion(Actor actor, String s) {
        this.pos.getRel(Bomb.P, Bomb.Or);
        MsgExplosion.send(actor, s, Bomb.P, this.getOwner(), 10.1F, 8.4F, 1, 1200F);
        this.doExplosion(actor, s);
    }

    private float ttcurTM;
}
