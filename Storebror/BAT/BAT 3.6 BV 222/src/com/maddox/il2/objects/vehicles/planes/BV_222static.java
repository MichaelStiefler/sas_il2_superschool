
package com.maddox.il2.objects.vehicles.planes;

public abstract class BV_222static {
    public static class BV_222_1 extends PlaneGeneric {

        public BV_222_1() {
        }
    }

    public BV_222static() {
    }

    static {
        new PlaneGeneric.SPAWN(BV_222static.BV_222_1.class);
    }
}
