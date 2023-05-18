package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Taube14 extends TaubeX implements TypeFighter, TypeScout, TypeStormovik {

    public Taube14() {
    }

    static {
        Class class1 = Taube14.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Taube");
        Property.set(class1, "meshName", "3do/plane/Taube14/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1967.8F);
        Property.set(class1, "FlightModel", "FlightModels/Taube.fmd:Taube_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTaube14_PIL.class, CockpitTaube14_Ob.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalBomb01" });
    }
}
