package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class RB_36H_late extends B_36X implements TypeBomber {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.turret[0].health = 4.0F / (float)(this.FM.turretSkill + 1);
    }
    
    static {
        Class class1 = RB_36H_late.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "RB-36");
        Property.set(class1, "meshName", "3DO/Plane/B36_late/RBH.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1945.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/RB-36H_late.fmd");
        Property.set(class1, "AutopilotElevatorAboveReferenceAltitudeFactor", 0.00016F);

        Property.set(class1, "cockpitClass", new Class[] { CockpitB36Jets.class, CockpitB36_Bombardier.class, CockpitB36_Radar.class, CockpitB36_AGunner.class });

        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_Rock01" });
    }
}
