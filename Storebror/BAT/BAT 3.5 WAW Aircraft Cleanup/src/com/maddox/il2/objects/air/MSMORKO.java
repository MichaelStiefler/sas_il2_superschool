package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MSMORKO extends MS400X {

    public MSMORKO() {
        this.flapps = 0.0F;
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            this.hierMesh().chunkSetAngles("OilRad_D0", 0.0F, -20F * f1, 0.0F);
        }
        super.update(f);
    }

    private float flapps;

    static {
        Class class1 = MSMORKO.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Morane");
        Property.set(class1, "meshNameDemo", "3DO/Plane/MSMorko(fi)/hier.him");
        Property.set(class1, "meshName_fi", "3DO/Plane/MSMorko(fi)/hier.him");
        Property.set(class1, "PaintScheme_fi", new PaintSchemeFCSPar02());
        Property.set(class1, "meshName", "3DO/Plane/MSMorko(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1951.8F);
        Property.set(class1, "FlightModel", "FlightModels/MSMorko.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMSMorko.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01" });
    }
}
