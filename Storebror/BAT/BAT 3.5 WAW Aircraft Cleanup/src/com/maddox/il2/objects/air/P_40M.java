package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_40M extends P_40 {

    public P_40M() {
    }

    public void update(float f1) {
        super.update(f1);
        P_40M.f = Aircraft.cvt(this.FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 5F, -17F);
        this.hierMesh().chunkSetAngles("Water2_D0", 0.0F, P_40M.f, 0.0F);
        this.hierMesh().chunkSetAngles("Water3_D0", 0.0F, P_40M.f, 0.0F);
        P_40M.f = Math.min(P_40M.f, 0.0F);
        this.hierMesh().chunkSetAngles("Water1_D0", 0.0F, P_40M.f, 0.0F);
        this.hierMesh().chunkSetAngles("Water4_D0", 0.0F, P_40M.f, 0.0F);
    }

    private static float f;

    static {
        Class class1 = P_40M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-40");
        Property.set(class1, "meshName", "3DO/Plane/P-40M(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/P-40M(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_rz", "3DO/Plane/P-40M(RZ)/hier.him");
        Property.set(class1, "PaintScheme_rz", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-40M.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_40M.class });
        Property.set(class1, "LOSElevation", 1.0692F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 3, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb01" });
    }
}
