package com.maddox.il2.objects.vehicles.stationary;

public abstract class AviaskinsProjectsStationary {
    public static class BMP2 extends StationaryGeneric {
    }

    public static class BRDM_2 extends StationaryGeneric {
    }

    public static class GAZ69 extends StationaryGeneric {
    }

    public static class GAZ69_Tent extends StationaryGeneric {
    }

    public static class GAZ69_m extends StationaryGeneric {
    }

//    public static class Gogol extends StationaryGeneric
//    {
//    }
    public static class HUMVWEE extends StationaryGeneric {
    }

    public static class HUMVWEEgun extends StationaryGeneric {
    }

    public static class Lebedenko extends StationaryGeneric {
    }

    public static class MS_1 extends StationaryGeneric {
    }

    public static class T_80 extends StationaryGeneric {
    }

    public static class Tram extends StationaryGeneric {
    }

    public static class TramS extends StationaryGeneric {
    }

    public static class TramS_R extends StationaryGeneric {
    }

    static {
        new StationaryGeneric.SPAWN(BMP2.class);
        new StationaryGeneric.SPAWN(BRDM_2.class);
        new StationaryGeneric.SPAWN(GAZ69.class);
        new StationaryGeneric.SPAWN(GAZ69_m.class);
        new StationaryGeneric.SPAWN(GAZ69_Tent.class);
//        new StationaryGeneric.SPAWN(Gogol.class);
        new StationaryGeneric.SPAWN(HUMVWEE.class);
        new StationaryGeneric.SPAWN(HUMVWEEgun.class);
        new StationaryGeneric.SPAWN(Lebedenko.class);
        new StationaryGeneric.SPAWN(MS_1.class);
        new StationaryGeneric.SPAWN(T_80.class);
        new StationaryGeneric.SPAWN(Tram.class);
        new StationaryGeneric.SPAWN(TramS.class);
        new StationaryGeneric.SPAWN(TramS_R.class);
    }
}
