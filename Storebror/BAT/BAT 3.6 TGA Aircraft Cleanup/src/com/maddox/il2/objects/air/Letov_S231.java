package com.maddox.il2.objects.air;

import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Letov_S231 extends ParasolX {

    public Letov_S231() {
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
            this.hierMesh().chunkVisible("WGun5_D0", false);
            return;
        }
        if (this.thisWeaponsName.equals("4xVz30")) {
            this.hierMesh().chunkVisible("WGun1_D0", true);
            this.hierMesh().chunkVisible("WGun2_D0", true);
            this.hierMesh().chunkVisible("WGun3_D0", true);
            this.hierMesh().chunkVisible("WGun4_D0", true);
            this.hierMesh().chunkVisible("Wgun5_D0", false);
            return;
        }
        if (this.thisWeaponsName.equals("3xVickers")) {
            this.hierMesh().chunkVisible("WGun1_D0", true);
            this.hierMesh().chunkVisible("WGun2_D0", true);
            this.hierMesh().chunkVisible("WGun3_D0", false);
            this.hierMesh().chunkVisible("WGun4_D0", false);
            this.hierMesh().chunkVisible("WGun5_D0", true);
            return;
        } else {
            return;
        }
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.xyz[2] = f1 * -0.45F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        this.hierMesh().chunkSetLocate("Water_D0", Aircraft.xyz, Aircraft.ypr);
        super.update(f);
        this.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Goertz_D0", false);
            } else {
                this.hierMesh().chunkVisible("Goertz_D0", true);
            }
        }
        if (this.FM.isPlayers() && !Main3D.cur3D().isViewOutside()) {
            this.hierMesh().chunkVisible("Goertz_D0", false);
        }
    }

    static {
        Class class1 = Letov_S231.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "S231");
        Property.set(class1, "meshName", "3DO/Plane/S231/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/S231.fmd:Parasol2_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLetov_S231.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[5]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05" });
    }
}
