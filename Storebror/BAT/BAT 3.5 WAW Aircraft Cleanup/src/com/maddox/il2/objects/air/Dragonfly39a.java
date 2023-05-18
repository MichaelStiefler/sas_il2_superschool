package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.MGunMGFFki;
import com.maddox.rts.Property;

public class Dragonfly39a extends Dragonfly39bis implements TypeFighter {

    public Dragonfly39a() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.hierMesh().chunkVisible("MG37mmC", false);
        this.hierMesh().chunkVisible("MG20mmC", true);
        if (this.getGunByHookName("_CANNON01") instanceof MGunMGFFki) {
            this.FM.M.massEmpty -= 46F;
        }
    }

    static {
        Class class1 = Dragonfly39a.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Dragonfly39");
        Property.set(class1, "meshName", "3do/plane/Dragonfly39bis/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/Dragonfly39a.fmd:Dragonfly39bis_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDragonfly39bis.class });
        Property.set(class1, "LOSElevation", 0.8941F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalDev08", "_ExternalDev51", "_ExternalDev52", "_ExternalDev53", "_ExternalDev54", "_ExternalDev55", "_ExternalDev56", "_ExternalDev57", "_ExternalDev58", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", "_ExternalRock57", "_ExternalRock58", "_ExternalDev09", "_ExternalDev10", "_ExternalDev71" });
    }
}
