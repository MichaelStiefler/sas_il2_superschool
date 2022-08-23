package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class FW_190D9early extends FW_190DNEW {

//    public void update(float f) {
////        if (this.FM.CT.getTrimElevatorControl() > 0.361F) {
////            this.FM.CT.setTrimElevatorControl(0.361F);
////        }
////        if (this.FM.CT.getTrimElevatorControl() < -0.261F) {
////            this.FM.CT.setTrimElevatorControl(-0.261F);
////        }
//        super.update(f);
//    }

    static {
        Class class1 = FW_190D9early.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190D-9early/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190D9early.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D9early.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 3, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02" });
    }
}
