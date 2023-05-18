package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class D_3801 extends MS400X {

    public D_3801() {
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("OilRad_D0", 0.0F, -20F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
    }

    static {
        Class class1 = D_3801.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D3801");
        Property.set(class1, "meshName", "3DO/Plane/D-3801(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1951.8F);
        Property.set(class1, "FlightModel", "FlightModels/MS410.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMorane40.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01" });
    }
}
