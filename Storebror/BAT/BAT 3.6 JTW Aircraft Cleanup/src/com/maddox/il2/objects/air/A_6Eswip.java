package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.Pylon_AN_AWW13_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU130_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU131_Cap_gn16;
import com.maddox.rts.Property;

public class A_6Eswip extends A_6fuelReceiver {

    public A_6Eswip() {
        this.bHasLAUcaps = false;
    }

    private void checkChangeWeaponColors() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] == null) {
                continue;
            }
            for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                if (this.FM.CT.Weapons[i][j] instanceof Pylon_AN_AWW13_gn16) {
                    ((Pylon_AN_AWW13_gn16) this.FM.CT.Weapons[i][j]).matGray();
                }
                if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16) {
                    this.bHasLAUcaps = true;
                    continue;
                }
                if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16) {
                    this.bHasLAUcaps = true;
                    continue;
                }
                if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16) {
                    this.bHasLAUcaps = true;
                }
            }

        }

    }

    private void checkDeleteLAUcaps() {
        if (this.FM.CT.saveWeaponControl[2]) {
            for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
                if (this.FM.CT.Weapons[i] == null) {
                    continue;
                }
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16) {
                        ((Pylon_LAU10_Cap_gn16) this.FM.CT.Weapons[i][j]).jettisonCap();
                        continue;
                    }
                    if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16) {
                        ((Pylon_LAU130_Cap_gn16) this.FM.CT.Weapons[i][j]).jettisonCap();
                        continue;
                    }
                    if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16) {
                        ((Pylon_LAU131_Cap_gn16) this.FM.CT.Weapons[i][j]).jettisonCap();
                    }
                }

            }

            this.bHasLAUcaps = false;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.turret[0].bIsAIControlled = false;
        this.fuelReceiveRate = 10.093F;
    }

    public void update(float f) {
        if (this.bHasLAUcaps) {
            this.checkDeleteLAUcaps();
        }
        super.update(f);
    }

    public void missionStarting() {
        super.missionStarting();
        this.checkChangeWeaponColors();
    }

    private boolean bHasLAUcaps;

    static {
        Class class1 = A_6Eswip.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-6E");
        Property.set(class1, "meshName", "3DO/Plane/A-6E_swip/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1990F);
        Property.set(class1, "yearExpired", 1997F);
        Property.set(class1, "FlightModel", "FlightModels/A6Eswip.fmd:A6");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA_6.class, CockpitA_6_Bombardier.class, CockpitA_6FLIR.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 7, 8, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_ExternalDev06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb01", "_Bomb06", "_ExternalBomb07", "_Bomb08", "_ExternalBomb09", "_Bomb10", "_ExternalBomb11", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_Bomb12", "_Bomb13", "_ExternalBomb14", "_ExternalBomb15", "_Bomb16", "_ExternalBomb17", "_ExternalBomb18", "_Bomb19NOUSE", "_ExternalBomb20", "_Bomb21", "_Bomb22NOUSE", "_ExternalBomb23", "_Bomb24", "_Bomb25", "_ExternalBomb26", "_ExternalBomb27", "_Bomb28", "_Bomb29", "_ExternalBomb30", "_ExternalBomb31", "_Bomb32", "_Bomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalBomb37", "_ExternalBomb38", "_ExternalBomb39",
                        "_ExternalBomb40", "_ExternalBomb41", "_ExternalBomb42", "_ExternalBomb43", "_ExternalBomb44", "_ExternalBomb45", "_ExternalBomb46", "_ExternalBomb47", "_ExternalBomb48", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Rock29", "_Rock30", "_ExternalRock31", "_ExternalRock31", "_ExternalRock32", "_ExternalRock32", "_Rock33", "_Rock34", "_ExternalRock35", "_ExternalRock35", "_ExternalRock36", "_ExternalRock36",
                        "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_Flare01", "_Chaff01", "_ExternalRock39", "_ExternalRock39", "_ExternalRock40", "_ExternalRock40", "_ExternalRock41", "_ExternalRock41", "_ExternalRock42", "_ExternalRock42" });
    }
}
