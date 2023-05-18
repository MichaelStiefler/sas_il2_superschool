package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_43_IB extends KI_43 {

    public KI_43_IB() {
        this.flapps = 0.0F;
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            for (int i = 1; i < 18; i++)
                this.hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, -22F * f1, 0.0F);

            for (int j = 2; j < 10; j++)
                this.hierMesh().chunkSetAngles("Cowflap" + j + "_D0", 0.0F, 22F * f1, 0.0F);

            for (int k = 11; k < 12; k++)
                this.hierMesh().chunkSetAngles("Cowflap" + k + "_D0", 0.0F, 22F * f1, 0.0F);

        }
    }

    private float flapps;

    static {
        Class class1 = KI_43_IB.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-43");
        Property.set(class1, "meshName", "3DO/Plane/Ki-43-Ib(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-43-Ib(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-43-Ia.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_43.class });
        Property.set(class1, "LOSElevation", 0.5265F);
        weaponTriggersRegister(class1, new int[] { 0, 0 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
