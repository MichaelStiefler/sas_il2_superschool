package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawkU1_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawkV2_gn16;
import com.maddox.rts.Property;

public class A_6A_tanker extends A_6XYZ_tanker implements TypeDockable, TypeTankerDrogue {

    public A_6A_tanker() {
        this.maxSendRefuel = 10.093F;
    }

    public boolean isDrogueExtended() {
        return this.bDrogueExtended;
    }

    void checkChangeWeaponColors() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] != null) {
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkU1_gn16) {
                        ((FuelTankGun_TankSkyhawkU1_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    } else if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkV2_gn16) {
                        ((FuelTankGun_TankSkyhawkV2_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                    }
                }

            }
        }

    }

    public void onAircraftLoaded() {
        this.noFL = true;
        super.onAircraftLoaded();
    }

    void RATrot() {
        super.RATrot();
        this.hierMesh().chunkSetAngles("D704_rat", 0.0F, 0.0F, this.ratdeg);
        if (this.FM.getSpeedKMH() > 300F) {
            this.hierMesh().chunkVisible("D704_rat_rot", true);
            this.hierMesh().chunkVisible("D704_rat", false);
        } else {
            this.hierMesh().chunkVisible("D704_rat_rot", false);
            this.hierMesh().chunkVisible("D704_rat", true);
        }
    }

    void drogueRefuel(float f) {
        this.drogueRefuel(f, "D704_FuelLine1", "D704_Drogue1", "D704_Drogue1_Fold");
    }

    private boolean bDrogueExtended;
    private float   ratdeg;

    static {
        Class class1 = A_6A_tanker.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-6A");
        Property.set(class1, "meshName", "3DO/Plane/A-6A/A6Atanker.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1959F);
        Property.set(class1, "yearExpired", 1980F);
        Property.set(class1, "FlightModel", "FlightModels/A6A.fmd:A6");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA_6.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, A_6XYZ_tanker.triggers);
        Aircraft.weaponHooksRegister(class1, A_6XYZ_tanker.hooks);
    }
}
