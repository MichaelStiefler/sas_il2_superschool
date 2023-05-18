package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class J9N1S_Kikka extends KikkaS123 implements TypeFighter, TypeBNZFighter {

    public J9N1S_Kikka() {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            J9N1S_Kikka.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            J9N1S_Kikka.bChangedPit = true;
        }
    }

    public void doMurderPilot(int i) {
        super.doMurderPilot(i);
        if (this.FM.isPlayers()) {
            J9N1S_Kikka.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = J9N1S_Kikka.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Kikka");
        Property.set(class1, "meshName", "3DO/Plane/J9N1-S-Kikka/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1944.1F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Kikka-NF.fmd:Kikka_NF_FM");
        Property.set(class1, "cockpitClass", new Class[] { Cockpit_KikkaNF.class, Cockpit_Kikka_SR.class });
        Property.set(class1, "LOSElevation", 0.74185F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
    }
}
