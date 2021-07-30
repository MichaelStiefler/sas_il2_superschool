package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class P_51A extends P_51 {

    public P_51A() {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        if (f <= 0.5D) this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f * 2.0F, 0.0F);
        else {
            float f1 = (f - 0.5F) * 2.0F;
            this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, -185F * f1, 0.0F);
        }
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    static {
        Class class1 = P_51A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-51");
        Property.set(class1, "meshNameDemo", "3DO/Plane/P-51A(GB)/hier.him");
        Property.set(class1, "meshName", "3DO/Plane/P-51A(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_gb", "3DO/Plane/P-51A(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1942.8F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-51A.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_51B.class });
        Property.set(class1, "LOSElevation", 1.03F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
