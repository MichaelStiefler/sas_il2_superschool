package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Do217_M1 extends Do217 {

    protected void moveBayDoor(float f) {
        if (f < 0.02F) {
            this.hierMesh().chunkVisible("Bay_D0", true);
            for (int i = 1; i <= 9; i++) {
                this.hierMesh().chunkVisible("BayL0" + i + "_D0", false);
                this.hierMesh().chunkVisible("BayR0" + i + "_D0", false);
            }

        } else {
            this.hierMesh().chunkVisible("Bay_D0", false);
            for (int j = 1; j <= 9; j++) {
                this.hierMesh().chunkVisible("BayL0" + j + "_D0", true);
                this.hierMesh().chunkVisible("BayR0" + j + "_D0", true);
            }

            boolean flag = f < 0.8F;
            this.hierMesh().chunkVisible("BayL03_D0", flag);
            this.hierMesh().chunkVisible("BayR03_D0", flag);
            this.hierMesh().chunkVisible("BayL06_D0", flag);
            this.hierMesh().chunkVisible("BayR06_D0", flag);
            this.hierMesh().chunkSetAngles("BayL01_D0", 0.0F, Aircraft.cvt(f, 0.04F, 0.7F, 0.0F, 120.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayL04_D0", 0.0F, Aircraft.cvt(f, 0.04F, 0.7F, 0.0F, 120.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayR01_D0", 0.0F, Aircraft.cvt(f, 0.04F, 0.7F, 0.0F, -120.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayR04_D0", 0.0F, Aircraft.cvt(f, 0.04F, 0.7F, 0.0F, -120.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayL02_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.98F, 0.0F, -155.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayL05_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.98F, 0.0F, -155.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayR02_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.98F, 0.0F, 155.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayR05_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.98F, 0.0F, 155.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayL03_D0", 0.0F, Aircraft.cvt(f, 0.4F, 0.9F, 0.0F, -150.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayL06_D0", 0.0F, Aircraft.cvt(f, 0.4F, 0.9F, 0.0F, -150.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayR03_D0", 0.0F, Aircraft.cvt(f, 0.4F, 0.9F, 0.0F, 150.5F), 0.0F);
            this.hierMesh().chunkSetAngles("BayR06_D0", 0.0F, Aircraft.cvt(f, 0.4F, 0.9F, 0.0F, 150.5F), 0.0F);
            if (this.thisWeaponsName.endsWith("Torpedo")) {
                this.hierMesh().chunkVisible("BayL09_D0", flag);
                this.hierMesh().chunkVisible("BayR09_D0", flag);
                this.hierMesh().chunkSetAngles("BayL07_D0", 0.0F, Aircraft.cvt(f, 0.04F, 0.7F, 0.0F, 120.5F), 0.0F);
                this.hierMesh().chunkSetAngles("BayL08_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.98F, 0.0F, -155.5F), 0.0F);
                this.hierMesh().chunkSetAngles("BayL09_D0", 0.0F, Aircraft.cvt(f, 0.4F, 0.9F, 0.0F, -150.5F), 0.0F);
                this.hierMesh().chunkSetAngles("BayR07_D0", 0.0F, Aircraft.cvt(f, 0.04F, 0.7F, 0.0F, -120.5F), 0.0F);
                this.hierMesh().chunkSetAngles("BayR08_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.98F, 0.0F, 150.5F), 0.0F);
                this.hierMesh().chunkSetAngles("BayR09_D0", 0.0F, Aircraft.cvt(f, 0.4F, 0.9F, 0.0F, 150.5F), 0.0F);
            }
        }
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("Radiator1_D0", 0.0F, -30F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        this.hierMesh().chunkSetAngles("Radiator2_D0", 0.0F, -30F * this.FM.EI.engines[1].getControlRadiator(), 0.0F);
        super.update(f);
    }

    protected void mydebug(String s1) {
    }

    static {
        Class class1 = Do217_M1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Do-217");
        Property.set(class1, "meshName", "3do/plane/Do217_M1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Do217M-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDo217_M1.class, CockpitDo217_Bombardier.class, CockpitDo217_NGunner.class, CockpitDo217_TGunner.class, CockpitDo217_BGunner.class, CockpitDo217_LGunner.class, CockpitDo217_RGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 12, 13, 14, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN10", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09",
                "_BombSpawn10", "_BombSpawn05" });
    }
}
