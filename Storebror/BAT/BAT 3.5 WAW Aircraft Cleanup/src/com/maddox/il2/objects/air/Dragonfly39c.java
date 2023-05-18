package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.MGunB20ki;
import com.maddox.il2.objects.weapons.MGunHo115ki;
import com.maddox.il2.objects.weapons.MGunHo203si;
import com.maddox.il2.objects.weapons.MGunHo5ki;
import com.maddox.il2.objects.weapons.MGunM4ki;
import com.maddox.il2.objects.weapons.MGunMK103ki;
import com.maddox.il2.objects.weapons.MGunMK108ki;
import com.maddox.il2.objects.weapons.MGunNS37ki;
import com.maddox.rts.Property;

public class Dragonfly39c extends Dragonfly39bis implements TypeFighter {

    public Dragonfly39c() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_CANNON01") instanceof MGunM4ki) {
            this.FM.M.massEmpty -= 40F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunNS37ki) {
            this.FM.M.massEmpty -= 34F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunHo203si) {
            this.FM.M.massEmpty -= 64F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunMK103ki) {
            this.hierMesh().chunkVisible("MG37mmC", false);
            this.hierMesh().chunkVisible("MG30mmC", true);
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunMK108ki) {
            this.hierMesh().chunkVisible("MG37mmC", false);
            this.hierMesh().chunkVisible("MG30mmC", true);
            this.FM.M.massEmpty -= 80F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunHo115ki) {
            this.hierMesh().chunkVisible("MG37mmC", false);
            this.hierMesh().chunkVisible("MG30mmC", true);
            this.FM.M.massEmpty -= 100F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunHo5ki) {
            this.hierMesh().chunkVisible("MG37mmC", false);
            this.hierMesh().chunkVisible("MG20mmC", true);
            this.FM.M.massEmpty -= 119F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunB20ki) {
            this.hierMesh().chunkVisible("MG37mmC", false);
            this.hierMesh().chunkVisible("MG20mmC", true);
            this.FM.M.massEmpty -= 149F;
        }
        if (this.thisWeaponsName.startsWith("light")) {
            this.hierMesh().chunkVisible("MG37mmC", false);
            this.hierMesh().chunkVisible("MG20mmC", true);
            this.FM.M.massEmpty -= 100F;
        }
    }

    static {
        Class class1 = Dragonfly39c.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Dragonfly39");
        Property.set(class1, "meshName", "3do/plane/Dragonfly39bis/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Dragonfly39b.fmd:Dragonfly39bis_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDragonfly39bis.class });
        Property.set(class1, "LOSElevation", 0.8941F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalRock01", "_ExternalRock02", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalDev08", "_ExternalDev51", "_ExternalDev52", "_ExternalDev53", "_ExternalDev54", "_ExternalDev55", "_ExternalDev56", "_ExternalDev57", "_ExternalDev58", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53",
                "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", "_ExternalRock57", "_ExternalRock58", "_ExternalDev09", "_ExternalDev61", "_ExternalDev62", "_ExternalRock61", "_ExternalRock62", "_ExternalRock63", "_ExternalRock64", "_ExternalRock65", "_ExternalRock66", "_ExternalRock67", "_ExternalRock68", "_ExternalDev10", "_ExternalDev71" });
    }
}
