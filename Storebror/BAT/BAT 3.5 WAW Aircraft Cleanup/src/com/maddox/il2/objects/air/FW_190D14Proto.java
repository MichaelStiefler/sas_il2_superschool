package com.maddox.il2.objects.air;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.Property;

public class FW_190D14Proto extends FW_190DB603NEW {

    public FW_190D14Proto() {
        this.kangle = 0.0F;
    }

    protected void moveGear(float f) {
        FW_190DB603NEW.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() >= 0.98F) {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        this.FM.Sq.dragParasiteCx = 0.0F;
    }

    public void update(float f) {
        for (int i = 1; i < 15; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * this.kangle, 0.0F);
        }

        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        super.update(f);
    }

    private float kangle;

    static {
        Class class1 = FW_190D14Proto.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190D-14(Beta)/hier_D14_proto.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        byte byte0 = 9;
        try {
            if (FlightModelMain.sectFile("FlightModels/Fw-190D-14.fmd") != null) {
                byte0 = 15;
            } else if (FlightModelMain.sectFile("FlightModels/Fw-190D-15.fmd") != null) {
                byte0 = 13;
            }
        } catch (Exception exception) {
        }
        switch (byte0) {
            case 9:
                Property.set(class1, "FlightModel", "FlightModels/Fw-190D-9.fmd");
                break;

            case 13:
                Property.set(class1, "FlightModel", "FlightModels/Fw-190D-14.fmd");
                break;

            case 15:
                Property.set(class1, "FlightModel", "FlightModels/Fw-190D-15.fmd");
                break;

            case 10:
            case 11:
            case 12:
            case 14:
            default:
                Property.set(class1, "FlightModel", "FlightModels/Fw-190D-9.fmd");
                break;
        }
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D14.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 9, 9, 1, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 9, 9, 1, 1, 9, 9, 2, 2, 9, 9, 1, 1, 9, 9, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalDev02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev05", "_ExternalDev06", "_CANNON09", "_CANNON10", "_ExternalDev07", "_ExternalDev08", "_CANNON11", "_CANNON12", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_CANNON13", "_CANNON14", "_CANNON15" });
    }
}
