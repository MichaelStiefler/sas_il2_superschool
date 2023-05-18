package com.maddox.il2.objects.vehicles.radars;

import com.maddox.il2.ai.ground.TgtFlak;
import com.maddox.il2.objects.vehicles.artillery.AAA;

public abstract class RotatingRadar {
    public static class Freya extends RadarFreya implements TgtFlak, AAA {
    }

    static {
        new RotatingRadarGeneric.SPAWN(Freya.class);
    }
}
