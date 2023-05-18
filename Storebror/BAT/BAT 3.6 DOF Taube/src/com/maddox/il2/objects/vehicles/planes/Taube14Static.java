package com.maddox.il2.objects.vehicles.planes;

public abstract class Taube14Static extends Plane {

    public static class Taube14 extends PlaneGeneric {
        public Taube14() {
        }
    }

    public Taube14Static() {
    }

    static {
        new PlaneGeneric.SPAWN(Taube14Static.Taube14.class);
    }
}
