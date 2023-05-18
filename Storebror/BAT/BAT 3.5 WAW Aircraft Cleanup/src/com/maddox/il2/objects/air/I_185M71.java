package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class I_185M71 extends I_185 {

    public I_185M71() {
        this.flapps = 0.0F;
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            this.hierMesh().chunkSetAngles("Water1_D0", 0.0F, -20F * f1, 0.0F);
            for (int i = 1; i < 9; i++) {
                String s = "Oil" + i + "_D0";
                this.hierMesh().chunkSetAngles(s, 0.0F, -15F * f1, 0.0F);
            }

        }
        super.update(f);
    }

    private float flapps;

    static {
        Class class1 = I_185M71.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-185");
        Property.set(class1, "meshName", "3DO/Plane/I-185(M-71)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/I-185M-71.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_185M71.class });
        Property.set(class1, "LOSElevation", 0.89135F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03" });
    }
}
