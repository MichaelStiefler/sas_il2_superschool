package com.maddox.il2.objects.vehicles.planes;

public abstract class Parasol2Static extends Plane {
    public static class Bulldog_II extends PlaneGeneric {
    }

// public static class Bulldog_IV extends PlaneGeneric
// {
// }
    public static class Fury_S extends PlaneGeneric {
    }

    public static class GL32 extends PlaneGeneric {
    }

    public static class GL450 extends PlaneGeneric {
    }

    public static class GL633 extends PlaneGeneric {
    }

    public static class Letov_S231 extends PlaneGeneric {
    }

    public static class Ni52 extends PlaneGeneric {
    }

    static {
        new PlaneGeneric.SPAWN(Bulldog_II.class);
// new PlaneGeneric.SPAWN(Bulldog_IV.class);
        new PlaneGeneric.SPAWN(Fury_S.class);
        new PlaneGeneric.SPAWN(GL32.class);
        new PlaneGeneric.SPAWN(GL450.class);
        new PlaneGeneric.SPAWN(GL633.class);
        new PlaneGeneric.SPAWN(Letov_S231.class);
        new PlaneGeneric.SPAWN(Ni52.class);
    }
}
