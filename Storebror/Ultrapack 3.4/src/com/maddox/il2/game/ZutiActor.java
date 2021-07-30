package com.maddox.il2.game;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Time;

public class ZutiActor extends Actor {
    public void startInterpolator() {
        this.interpPut(new ZutiInterpolator(), "zutiInterpolator", Time.current(), null);
    }
}
