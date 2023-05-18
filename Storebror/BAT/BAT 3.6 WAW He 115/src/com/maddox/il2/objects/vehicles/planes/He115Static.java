package com.maddox.il2.objects.vehicles.planes;

public abstract class He115Static extends Plane {
    public static class He115 extends PlaneGeneric {
    }

    static {
        new PlaneGeneric.SPAWN(He115Static.He115.class);
    }
}
