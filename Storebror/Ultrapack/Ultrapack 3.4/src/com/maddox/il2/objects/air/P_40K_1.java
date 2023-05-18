package com.maddox.il2.objects.air;

import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class P_40K_1 extends P_40 implements TypeStormovik {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getBulletEmitterByHookName("_ExternalDev01") instanceof GunEmpty && this.getBulletEmitterByHookName("_ExternalBomb01") instanceof GunEmpty && this.getBulletEmitterByHookName("_ExternalBomb04") instanceof GunEmpty)
            this.hierMesh().chunkVisible("Pilon_D0", false);
        else this.hierMesh().chunkVisible("Pilon_D0", true);
        if (this.getBulletEmitterByHookName("_ExternalBomb02") instanceof GunEmpty && this.getBulletEmitterByHookName("_ExternalBomb06") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("Pilon2_D0", false);
            this.hierMesh().chunkVisible("Pilon3_D0", false);
        } else {
            this.hierMesh().chunkVisible("Pilon2_D0", true);
            this.hierMesh().chunkVisible("Pilon3_D0", true);
        }
        if (this.thisWeaponsName.endsWith("light")) {
            this.FM.M.massEmpty = 3159F;
            return;
        } else return;
    }

    public void update(float f1) {
        super.update(f1);
        f = Aircraft.cvt(this.FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 5F, -17F);
        this.hierMesh().chunkSetAngles("Water2_D0", 0.0F, f, 0.0F);
        this.hierMesh().chunkSetAngles("Water3_D0", 0.0F, f, 0.0F);
        f = Math.min(f, 0.0F);
        this.hierMesh().chunkSetAngles("Water1_D0", 0.0F, f, 0.0F);
        this.hierMesh().chunkSetAngles("Water4_D0", 0.0F, f, 0.0F);
        if (this.FM.EI.engines[0].getControlAfterburner()) {
            this.FM.EI.engines[0].setAfterburnerType(11);
            HUD.logRightBottom("BOOST / WEP ENABLED!");
        }
    }

    private static float f;

    static {
        Class class1 = P_40K_1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-40K");
        Property.set(class1, "meshName_gb", "3DO/Plane/P_40K_2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar05());
        Property.set(class1, "meshName", "3DO/Plane/P_40K_1(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/P_40K_1(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-40K.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_40M.class });
        Property.set(class1, "LOSElevation", 1.0692F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalDev01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05",
                "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09" });
    }
}
