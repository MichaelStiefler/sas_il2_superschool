package com.maddox.il2.objects.air;

import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class SM81T extends SM81x implements TypeBomber, TypeTransport {

    public SM81T() {
    }

    public void update(float f) {
        super.update(f);
        super.onAircraftLoaded();
        if (super.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("CF_D0", false);
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("Cockpit_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
            } else {
                this.hierMesh().chunkVisible("CF_D0", true);
                this.hierMesh().chunkVisible("Blister1_D0", true);
                this.hierMesh().chunkVisible("Cockpit_D0", true);
                this.hierMesh().chunkVisible("Head1_D0", true);
                this.hierMesh().chunkVisible("HMask1_D0", true);
                this.hierMesh().chunkVisible("Pilot1_D0", true);
                this.hierMesh().chunkVisible("HMask2_D0", true);
                this.hierMesh().chunkVisible("Pilot2_D0", true);
            }
        }
        if (super.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("CF_D1", false);
            }
            this.hierMesh().chunkVisible("CF_D2", false);
            this.hierMesh().chunkVisible("CF_D3", false);
            this.hierMesh().chunkVisible("Pilot1_D1", false);
            this.hierMesh().chunkVisible("Pilot2_D1", false);
            this.hierMesh().chunkVisible("Blister1_D1", false);
            this.hierMesh().chunkVisible("Cockpit_D1", false);
            this.hierMesh().chunkVisible("Blister1_D2", false);
            this.hierMesh().chunkVisible("Cockpit_D2", false);
            this.hierMesh().chunkVisible("Blister1_D3", false);
            this.hierMesh().chunkVisible("Cockpit_D3", false);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0: // '\0'
                if (f1 < 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                if (f1 > 70F) {
                    f1 = 70F;
                    flag = false;
                }
                break;

            case 1: // '\001'
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > -2F) {
                    f1 = -2F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (super.thisWeaponsName.equals("18xPara")) {
            this.hierMesh().chunkVisible("Door_D0", false);
            return;
        } else {
            this.hierMesh().chunkVisible("Door_D0", true);
            return;
        }
    }

    static {
        Class class1 = SM81T.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SM81");
        Property.set(class1, "meshName", "3DO/Plane/SM81/hierT.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1936.8F);
        Property.set(class1, "yearExpired", 1944.5F);
        Property.set(class1, "FlightModel", "FlightModels/SM81PX.fmd:SM81_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSM81x.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_BombSpawn00", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", "_BombSpawn27", "_BombSpawn28", "_BombSpawn29", "_BombSpawn30", "_BombSpawn31", "_BombSpawn32", "_BombSpawn33", "_BombSpawn34", "_BombSpawn35", "_BombSpawn36", "_BombSpawn37", "_BombSpawn38", "_BombSpawn39", "_BombSpawn40", "_BombSpawn41", "_BombSpawn42", "_BombSpawn43", "_BombSpawn44", "_BombSpawn45", "_BombSpawn46", "_BombSpawn47", "_BombSpawn48", "_BombSpawn49", "_ExternalBomb49", "_BombSpawn50", "_ExternalBomb50", "_BombSpawn51", "_ExternalBomb51", "_BombSpawn52", "_ExternalBomb52",
                        "_BombSpawn53", "_ExternalBomb53", "_BombSpawn54", "_ExternalBomb54", "_BombSpawn55", "_ExternalBomb55", "_BombSpawn56", "_ExternalBomb56", "_BombSpawn57", "_ExternalBomb57", "_BombSpawn58", "_ExternalBomb58", "_BombSpawn59", "_ExternalBomb59", "_BombSpawn60", "_ExternalBomb60", "_BombSpawn61", "_ExternalBomb61", "_BombSpawn62", "_ExternalBomb62", "_BombSpawn63", "_ExternalBomb63", "_BombSpawn64", "_ExternalBomb64", "_BombSpawn65", "_ExternalBomb65", "_BombSpawn66", "_ExternalBomb66", "_BombSpawn67", "_ExternalBomb67", "_BombSpawn68", "_ExternalBomb68", "_BombSpawn69", "_ExternalBomb69", "_BombSpawn70", "_ExternalBomb70", "_BombSpawn71", "_ExternalBomb71", "_BombSpawn72", "_ExternalBomb72", "_BombSpawn73", "_ExternalBomb73", "_BombSpawn74", "_ExternalBomb74", "_BombSpawn75", "_ExternalBomb75", "_BombSpawn76", "_ExternalBomb76", "_BombSpawn77", "_BombSpawn78", "_BombSpawn79", "_BombSpawn80", "_BombSpawn81", "_BombSpawn82", "_BombSpawn83", "_BombSpawn84", "_BombSpawn85", "_BombSpawn86",
                        "_BombSpawn87", "_BombSpawn88", "_BombSpawn89", "_BombSpawn90", "_BombSpawn91", "_BombSpawn92" });
    }
}
