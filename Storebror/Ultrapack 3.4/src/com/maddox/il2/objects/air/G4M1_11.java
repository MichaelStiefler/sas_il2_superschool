package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class G4M1_11 extends G4M implements TypeBomber {

    public G4M1_11() {
        this.ftpos = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.CT.Weapons[3] != null) {
            this.hierMesh().chunkVisible("Bay1_D0", false);
            this.hierMesh().chunkVisible("Bay2_D0", false);
        }
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.turret[1].tu[0];
        float f2 = this.FM.turret[1].tu[1];
        f1 -= 360F;
        if (Math.abs(f1) > 2.0F || Math.abs(f2) > 2.0F) {
            float f3 = (float) Math.toDegrees(Math.atan2(f1, f2));
            this.ftpos = 0.8F * this.ftpos + 0.2F * f3;
            this.hierMesh().chunkSetAngles("Turret2E_D0", 0.0F, this.ftpos, 0.0F);
        }
    }

    private float ftpos;

    static {
        Class class1 = G4M1_11.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "G4M");
        Property.set(class1, "meshName", "3DO/Plane/G4M1-11(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ja", "3DO/Plane/G4M1-11(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/G4M1-11.fmd");
        Property.set(class1, "cockpitClass",
                new Class[] { CockpitG4M1_11.class, CockpitG4M1_11_Bombardier.class, CockpitG4M1_11_NGunner.class, CockpitG4M1_11_AGunner.class, CockpitG4M1_11_TGunner.class, CockpitG4M1_11_RGunner.class, CockpitG4M1_11_LGunner.class });
        Property.set(class1, "LOSElevation", 1.4078F);
        weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03" });
    }
}
