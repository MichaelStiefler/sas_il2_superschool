package com.maddox.il2.objects.vehicles.stationary;

import com.maddox.il2.ai.ground.TgtTrain;
import com.maddox.il2.objects.vehicles.artillery.SWagon;

public abstract class StationaryWCatTRN extends Stationary {
    public static class Wagon17 extends StationaryGeneric implements TgtTrain, SWagon {
    }

    public static class Wagon19 extends StationaryGeneric implements TgtTrain, SWagon {
    }

//    public static class Wagon26 extends StationaryGeneric
//        implements TgtTrain, SWagon
//    {
//    }
//    public static class Wagon27 extends StationaryGeneric
//        implements TgtTrain, SWagon
//    {
//    }
//    public static class Wagon28 extends StationaryGeneric
//        implements TgtTrain, SWagon
//    {
//    }
//    public static class Wagon29 extends StationaryGeneric
//        implements TgtTrain, SWagon
//    {
//    }
//    public static class Wagon30 extends StationaryGeneric
//        implements TgtTrain, SWagon
//    {
//    }
//    public static class Wagon31 extends StationaryGeneric
//        implements TgtTrain, SWagon
//    {
//    }
//    public static class Wagon32 extends StationaryGeneric
//        implements TgtTrain, SWagon
//    {
//    }
//    public static class Wagon33 extends StationaryGeneric
//        implements TgtTrain, SWagon
//    {
//    }
//    public static class Wagon34 extends StationaryGeneric
//        implements TgtTrain, SWagon
//    {
//    }
//    public static class Wagon38 extends StationaryGeneric
//        implements TgtTrain, SWagon
//    {
//    }
    static {
        new StationaryGeneric.SPAWN(Wagon17.class);
        new StationaryGeneric.SPAWN(Wagon19.class);
//        new StationaryGeneric.SPAWN(Wagon26.class);
//        new StationaryGeneric.SPAWN(Wagon27.class);
//        new StationaryGeneric.SPAWN(Wagon28.class);
//        new StationaryGeneric.SPAWN(Wagon29.class);
//        new StationaryGeneric.SPAWN(Wagon30.class);
//        new StationaryGeneric.SPAWN(Wagon31.class);
//        new StationaryGeneric.SPAWN(Wagon32.class);
//        new StationaryGeneric.SPAWN(Wagon33.class);
//        new StationaryGeneric.SPAWN(Wagon34.class);
//        new StationaryGeneric.SPAWN(Wagon38.class);
    }
}
