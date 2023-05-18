package com.maddox.il2.objects.air;

public class D37X extends ParasolX {
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
        if (this.thisWeaponsName.equals("2xVickers")) {
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
}
