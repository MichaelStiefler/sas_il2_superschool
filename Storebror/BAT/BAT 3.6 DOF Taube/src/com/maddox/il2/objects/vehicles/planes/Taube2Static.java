package com.maddox.il2.objects.vehicles.planes;

public abstract class Taube2Static extends Plane {

    public static class Taube2 extends PlaneGeneric {
        public Taube2() {
        }
    }

    public Taube2Static() {
    }

    static {
        new PlaneGeneric.SPAWN(Taube2Static.Taube2.class);
    }
}
