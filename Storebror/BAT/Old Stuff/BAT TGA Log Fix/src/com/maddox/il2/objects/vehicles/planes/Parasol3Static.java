package com.maddox.il2.objects.vehicles.planes;

public abstract class Parasol3Static extends Plane {
    public static class Aero_A101 extends PlaneGeneric {
    }

    public static class Hart extends PlaneGeneric {
    }

    public static class HartMercury extends PlaneGeneric {
    }

    public static class Hind extends PlaneGeneric {
    }

    public static class Hispano_E30 extends PlaneGeneric {
    }

    public static class Hispano_E30T extends PlaneGeneric {
    }

    public static class OspreyC extends PlaneGeneric {
    }

    public static class OspreyL extends PlaneGeneric {
    }

    public static class OspreyS extends PlaneGeneric {
    }

    public static class VildebeestL extends PlaneGeneric {
    }

    public static class VildebeestLIII extends PlaneGeneric {
    }

    public static class VildebeestLIV extends PlaneGeneric {
    }

// public static class VildebeestS extends PlaneGeneric
// {
// }
    static {
        new PlaneGeneric.SPAWN(Aero_A101.class);
        new PlaneGeneric.SPAWN(Hispano_E30T.class);
        new PlaneGeneric.SPAWN(Hispano_E30.class);
        new PlaneGeneric.SPAWN(OspreyS.class);
        new PlaneGeneric.SPAWN(OspreyC.class);
        new PlaneGeneric.SPAWN(OspreyL.class);
        new PlaneGeneric.SPAWN(Hart.class);
        new PlaneGeneric.SPAWN(HartMercury.class);
        new PlaneGeneric.SPAWN(Hind.class);
        new PlaneGeneric.SPAWN(VildebeestL.class);
        new PlaneGeneric.SPAWN(VildebeestLIII.class);
        new PlaneGeneric.SPAWN(VildebeestLIV.class);
// new PlaneGeneric.SPAWN(VildebeestS.class);
    }
}
