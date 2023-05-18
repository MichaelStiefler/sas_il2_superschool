package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.Time;

public class BombTiOn extends BombTiBase1 {

    public void start() {
        super.start();
        Eff3DActor.New(this, null, new Loc(), 1.0F, "3DO/Effects/Fireworks/TIOrange.eff", (this.t1 - Time.current()) / 1000F);
    }

    static {
        BombTiBase1.initStatic(BombTiOn.class);
    }
}
