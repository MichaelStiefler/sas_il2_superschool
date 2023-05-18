package com.maddox.il2.objects.vehicles.planes;

public abstract class TaubeStatic extends Plane {

    public static class Taube extends PlaneGeneric {
        public Taube() {
        }
    }

    public TaubeStatic() {
    }

    static {
        new PlaneGeneric.SPAWN(TaubeStatic.Taube.class);
    }
}
