package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class A_6E_tanker extends A_6XYZ_tanker implements TypeDockable, TypeTankerDrogue {

    public A_6E_tanker() {
        this.maxSendRefuel = 11.101F;
    }

    public boolean isDrogueExtended() {
        return this.bDrogueExtended;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.turret[0].bIsAIControlled = false;
    }

    void RATrot() {
        super.RATrot();
        this.hierMesh().chunkSetAngles("AA42R_rat", 0.0F, 0.0F, this.ratdeg);
        if (this.FM.getSpeedKMH() > 300F) {
            this.hierMesh().chunkVisible("AA42R_rat_rot", true);
            this.hierMesh().chunkVisible("AA42R_rat", false);
        } else {
            this.hierMesh().chunkVisible("AA42R_rat_rot", false);
            this.hierMesh().chunkVisible("AA42R_rat", true);
        }
    }

    void drogueRefuel(float f) {
        super.drogueRefuel(f, "AA42R_FuelLine1", "AA42R_Drogue1", "AA42R_Drogue1_Fold");
    }

    private boolean bDrogueExtended;
    private float   ratdeg;

    static {
        Class class1 = A_6E_tanker.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-6E");
        Property.set(class1, "meshName", "3DO/Plane/A-6E/A6Etanker.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1977F);
        Property.set(class1, "yearExpired", 1997F);
        Property.set(class1, "FlightModel", "FlightModels/A6E.fmd:A6");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA_6.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, A_6XYZ_tanker.triggers);
        Aircraft.weaponHooksRegister(class1, A_6XYZ_tanker.hooks);
    }
}
