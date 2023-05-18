package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_61_II_KAI extends KI_100 {

    public KI_61_II_KAI() {
        this.flapps = 0.0F;
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            this.hierMesh().chunkSetAngles("Water_D0", 0.0F, -30F * f1, 0.0F);
        }
    }

    private float flapps;

    static {
        Class class1 = KI_61_II_KAI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-61");
        Property.set(class1, "meshName", "3DO/Plane/Ki-61-II(Kai)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-61-II(Kai)(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-61-IIKai.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_100.class });
        Property.set(class1, "LOSElevation", 0.85935F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
