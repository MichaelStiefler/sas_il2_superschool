package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.rts.Property;

public class A_37B extends A_37fuelReceiver implements TypeStormovik {

    public A_37B() {
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "A-37B_";
    }

    public float getFlowRate() {
        return A_37B.FlowRate;
    }

    public float getFuelReserve() {
        return A_37B.FuelReserve;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
    }

    public void update(float f) {
        super.update(f);
    }

    public static float FlowRate    = 10F;
    public static float FuelReserve = 1500F;

    static {
        Class class1 = A_37B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-37");
        Property.set(class1, "meshName", "3DO/Plane/A-37/hierB.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1966F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/A-37B.fmd:A37FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitT_37.class, CockpitA_37Bombardier.class, CockpitT_37_2.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_CANNON02", "_CANNON03" });
    }
}
