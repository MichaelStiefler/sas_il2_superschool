package com.maddox.il2.objects.vehicles.planes;

public abstract class PlaneWWINieu extends Plane {
// public static class NieuportN17 extends PlaneGeneric
// {
// }
    public static class NieuportN17b extends PlaneGeneric {
    }

    public static class NieuportN4 extends PlaneGeneric {
    }

    public static class NieuportN4arm extends PlaneGeneric {
    }

    static {
        new PlaneGeneric.SPAWN(NieuportN4.class);
        new PlaneGeneric.SPAWN(NieuportN4arm.class);
// new PlaneGeneric.SPAWN(NieuportN17.class);
        new PlaneGeneric.SPAWN(NieuportN17b.class);
    }
}
