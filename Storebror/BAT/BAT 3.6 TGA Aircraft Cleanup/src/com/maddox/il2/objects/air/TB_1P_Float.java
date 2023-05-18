package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class TB_1P_Float extends TB_1_ANT4 implements TypeBomber, TypeTransport, TypeSeaPlane {

    public TB_1P_Float() {
    }

    public void moveWheelSink() {
    }

    static {
        Class class1 = TB_1P_Float.class;
        TB_1_ANT4.tb1InitStatic(class1);
        Property.set(class1, "meshName", "3DO/Plane/TB_1P_Float/hier.him");
        Property.set(class1, "FlightModel", "FlightModels/TB_1P.fmd:ANT4_FM");
    }
}
