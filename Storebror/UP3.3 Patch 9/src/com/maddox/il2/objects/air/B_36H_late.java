package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class B_36H_late extends B_36X implements TypeBomber {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.turret[0].health = 4.0F / (float)(this.FM.turretSkill + 1);
    }
    
    static {
        Class class1 = B_36H_late.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-36");
        Property.set(class1, "meshName", "3DO/Plane/B36_late/H.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1945.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-36H_late.fmd");
        Property.set(class1, "AutopilotElevatorAboveReferenceAltitudeFactor", 0.00016F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitB36Jets.class, CockpitB36_Bombardier.class, CockpitB36_Radar.class, CockpitB36_AGunner.class });

        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2 });
        Aircraft.weaponHooksRegister(class1, new java.lang.String[] { "_MGUN01", "_MGUN02", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", "_BombSpawn27", "_BombSpawn28", "_BombSpawn29", "_BombSpawn30", "_BombSpawn31", "_BombSpawn32", "_BombSpawn33", "_BombSpawn34", "_BombSpawn35", "_BombSpawn36", "_BombSpawn37", "_BombSpawn38", "_BombSpawn39", "_BombSpawn40", "_BombSpawn41", "_BombSpawn42", "_BombSpawn43", "_BombSpawn44", "_BombSpawn45", "_BombSpawn46", "_BombSpawn47", "_BombSpawn48", "_BombSpawn49", "_BombSpawn50", "_BombSpawn51", "_BombSpawn52", "_BombSpawn53", "_BombSpawn54", "_BombSpawn55", "_BombSpawn56", "_BombSpawn57", "_BombSpawn58",
                "_BombSpawn59", "_BombSpawn60", "_BombSpawn61", "_BombSpawn62", "_BombSpawn63", "_BombSpawn64", "_Rock01" });
    }
}
