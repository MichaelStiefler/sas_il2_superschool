package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class HE_111H2 extends HE_111 {

    public HE_111H2() {
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay5_D0", 0.0F, 74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay6_D0", 0.0F, -94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay7_D0", 0.0F, 74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay8_D0", 0.0F, -94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay9_D0", 0.0F, -74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay10_D0", 0.0F, 94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay11_D0", 0.0F, -74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay12_D0", 0.0F, 94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay13_D0", 0.0F, -74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay14_D0", 0.0F, 94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay15_D0", 0.0F, -74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay16_D0", 0.0F, 94F * f, 0.0F);
    }

    static {
        Class class1 = HE_111H2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He-111");
        Property.set(class1, "meshName", "3do/plane/He-111H-2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1939.5F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/He-111H-2.fmd");
        Property.set(class1, "cockpitClass",
                new Class[] { CockpitHE_111H2.class, CockpitHE_111H2_Bombardier.class, CockpitHE_111H2_NGunner.class, CockpitHE_111H2_TGunner.class, CockpitHE_111H2_BGunner.class, CockpitHE_111H2_LGunner.class, CockpitHE_111H2_RGunner.class });
        weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 3, 3, 3, 3, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08" });
    }
}
