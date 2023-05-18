package com.maddox.il2.objects.vehicles.planes;

public abstract class StatSU_15 {
    public static class Su_15TM extends PlaneGeneric {

        public Su_15TM() {
        }
    }

    public static class Su_15T extends PlaneGeneric {

        public Su_15T() {
        }
    }

    public static class Su_15 extends PlaneGeneric {

        public Su_15() {
        }
    }

    public StatSU_15() {
    }

    static {
        new PlaneGeneric.SPAWN(StatSU_15.Su_15.class);
        new PlaneGeneric.SPAWN(StatSU_15.Su_15T.class);
        new PlaneGeneric.SPAWN(StatSU_15.Su_15TM.class);
    }
}
