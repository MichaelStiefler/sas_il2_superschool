
package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class IL_2_1941Late extends IL_2 {

    public void doWoundPilot(int i, float f) {
        if (i == 1) {
            this.FM.turret[0].setHealth(f);
        }
    }

    public void doMurderPilot(int i) {
        if (i == 1) {
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot2_D1", true);
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1A_D1", true);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -135F) {
            af[0] = -135F;
            flag = false;
        } else if (af[0] > 135F) {
            af[0] = 135F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if (f < 20F) {
            if (af[1] < -10F) {
                af[1] = -10F;
                flag = false;
            }
        } else if (af[1] < -15F) {
            af[1] = -15F;
            flag = false;
        }
        if (af[1] > 45F) {
            af[1] = 45F;
            flag = false;
        }
        if (!flag) {
            return false;
        }
        float f1 = af[1];
        if ((f < 2.0F) && (f1 < 17F)) {
            return false;
        }
        if (f1 > -5F) {
            return true;
        }
        if (f1 > -12F) {
            f1 += 12F;
            return f > (12F + (f1 * 2.571429F));
        } else {
            f1 = -f1;
            return f > f1;
        }
    }

    static {
        Class class1 = IL_2_1941Late.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "IL2");
        Property.set(class1, "meshName", "3do/plane/Il-2-1941Late(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "meshName_ru", "3do/plane/Il-2-1941Late/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeBCSPar02());
        Property.set(class1, "yearService", 1941.2F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Il-2-1941-late.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitIL_2_1942.class, CockpitIL2_GunnerOpenFieldMod.class });
        Property.set(class1, "LOSElevation", 0.81F);
        Property.set(class1, "Handicap", 1.0F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 3, 3, 10, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_Cannon01", "_Cannon02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_BombSpawn01", "_BombSpawn02", "_MGUN03", "_MGUN04" });
    }
}
