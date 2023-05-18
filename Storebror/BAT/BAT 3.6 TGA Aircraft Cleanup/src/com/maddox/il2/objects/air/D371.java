package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class D371 extends ParasolX {

    public D371() {
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equals("default")) {
            this.hierMesh().chunkVisible("WGun1_D0", true);
            this.hierMesh().chunkVisible("WGun2_D0", true);
            this.hierMesh().chunkVisible("WGun3_D0", false);
            this.hierMesh().chunkVisible("WGun4_D0", false);
            this.hierMesh().chunkVisible("WCannon1_D0", false);
            this.hierMesh().chunkVisible("WCannon2_D0", false);
            return;
        }
        if (this.thisWeaponsName.equals("2xVickers+2PV1")) {
            this.hierMesh().chunkVisible("WGun1_D0", true);
            this.hierMesh().chunkVisible("WGun2_D0", true);
            this.hierMesh().chunkVisible("WGun3_D0", true);
            this.hierMesh().chunkVisible("WGun4_D0", true);
            this.hierMesh().chunkVisible("WCannon1_D0", false);
            this.hierMesh().chunkVisible("WCannon2_D0", false);
            return;
        }
        if (this.thisWeaponsName.equals("2xOerlikon")) {
            this.hierMesh().chunkVisible("WGun1_D0", false);
            this.hierMesh().chunkVisible("WGun2_D0", false);
            this.hierMesh().chunkVisible("WGun3_D0", false);
            this.hierMesh().chunkVisible("WGun4_D0", false);
            this.hierMesh().chunkVisible("WCannon1_D0", true);
            this.hierMesh().chunkVisible("WCannon2_D0", true);
            return;
        } else {
            return;
        }
    }

    static {
        Class class1 = D371.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D371");
        Property.set(class1, "meshName", "3DO/Plane/D372/hier1.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/D371.fmd:Parasol_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitD372.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02" });
    }
}
