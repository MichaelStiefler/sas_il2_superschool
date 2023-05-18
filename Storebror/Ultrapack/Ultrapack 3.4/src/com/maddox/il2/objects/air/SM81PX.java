package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SM81PX extends SM81x implements TypeBomber, TypeTransport {
    public SM81PX() {
        this.numTurrets = 4;
    }

    static {
        Class class1 = SM81PX.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SM81");
        Property.set(class1, "meshName", "3DO/Plane/SM81/hierPX.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1936.8F);
        Property.set(class1, "yearExpired", 1944.5F);
        Property.set(class1, "FlightModel", "FlightModels/SM81PX.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSM81x.class, CockpitSM81x_Bombardier.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", "_BombSpawn27", "_BombSpawn28", "_BombSpawn29", "_BombSpawn30", "_BombSpawn31", "_BombSpawn32", "_BombSpawn33", "_BombSpawn34", "_BombSpawn35", "_BombSpawn36", "_BombSpawn37", "_BombSpawn38", "_BombSpawn39", "_BombSpawn40", "_BombSpawn41", "_BombSpawn42", "_BombSpawn43", "_BombSpawn44", "_BombSpawn45", "_BombSpawn46", "_BombSpawn47", "_BombSpawn48", "_BombSpawn49", "_ExternalBomb49", "_BombSpawn50", "_ExternalBomb50", "_BombSpawn51", "_ExternalBomb51", "_BombSpawn52", "_ExternalBomb52",
                        "_BombSpawn53", "_ExternalBomb53", "_BombSpawn54", "_ExternalBomb54", "_BombSpawn55", "_ExternalBomb55", "_BombSpawn56", "_ExternalBomb56", "_BombSpawn57", "_ExternalBomb57", "_BombSpawn58", "_ExternalBomb58", "_BombSpawn59", "_ExternalBomb59", "_BombSpawn60", "_ExternalBomb60", "_BombSpawn61", "_ExternalBomb61", "_BombSpawn62", "_ExternalBomb62", "_BombSpawn63", "_ExternalBomb63", "_BombSpawn64", "_ExternalBomb64", "_BombSpawn65", "_ExternalBomb65", "_BombSpawn66", "_ExternalBomb66", "_BombSpawn67", "_ExternalBomb67", "_BombSpawn68", "_ExternalBomb68", "_BombSpawn69", "_ExternalBomb69", "_BombSpawn70", "_ExternalBomb70", "_BombSpawn71", "_ExternalBomb71", "_BombSpawn72", "_ExternalBomb72", "_BombSpawn73", "_ExternalBomb73", "_BombSpawn74", "_ExternalBomb74", "_BombSpawn75", "_ExternalBomb75", "_BombSpawn76", "_ExternalBomb76", "_BombSpawn77", "_BombSpawn78", "_BombSpawn79", "_BombSpawn80", "_BombSpawn81", "_BombSpawn82", "_BombSpawn83", "_BombSpawn84", "_BombSpawn85", "_BombSpawn86",
                        "_BombSpawn87", "_BombSpawn88", "_BombSpawn89", "_BombSpawn90", "_BombSpawn91", "_BombSpawn92" });
    }
}
