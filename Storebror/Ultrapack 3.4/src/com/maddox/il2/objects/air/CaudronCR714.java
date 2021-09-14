package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CaudronCR714 extends MS400X {
    public void update(float f) {
        this.hierMesh().chunkSetAngles("OilRad_D0", 0.0F, -20F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
    }

    static {
        Class class1 = CaudronCR714.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Cyclone");
        Property.set(class1, "meshName", "3DO/Plane/CaudronCR714(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1951.8F);
        Property.set(class1, "FlightModel", "FlightModels/CaudronCR714.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMC_202.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
