package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class D521_ALA extends MS400X {

    public D521_ALA() {
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("OilRad_D0", 0.0F, -20F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
    }

    static {
        Class class1 = D521_ALA.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D521");
        Property.set(class1, "meshName_fi", "3DO/Plane/D521-ALA(fi)/hier.him");
        Property.set(class1, "PaintScheme_fi", new PaintSchemeFCSPar02());
        Property.set(class1, "meshName", "3DO/Plane/D521-ALA(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1951.8F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109E-4.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHURRI.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01" });
    }
}
