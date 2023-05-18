package com.maddox.il2.objects.vehicles.stationary;

public abstract class DLight {
    public static class DLightAirfield extends DLightGeneric {
    }

    static {
        new DLightGeneric.SPAWN(DLightAirfield.class);
    }
}
