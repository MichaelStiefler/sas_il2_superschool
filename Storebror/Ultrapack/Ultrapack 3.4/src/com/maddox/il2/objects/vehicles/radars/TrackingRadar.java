package com.maddox.il2.objects.vehicles.radars;

import com.maddox.il2.ai.ground.TgtFlak;
import com.maddox.il2.objects.vehicles.artillery.AAA;

public abstract class TrackingRadar {
    public static class Wurzburg extends TrackingRadarGeneric implements TgtFlak, AAA {
    }

    static {
        new TrackingRadarGeneric.SPAWN(Wurzburg.class);
    }
}
