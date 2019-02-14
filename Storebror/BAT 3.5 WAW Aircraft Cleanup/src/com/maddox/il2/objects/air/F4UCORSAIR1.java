package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class F4UCORSAIR1 extends F4U {

    public F4UCORSAIR1() {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.625F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.06845F);
        Aircraft.ypr[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 1.0F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.13F);
        Aircraft.ypr[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -8F);
        this.hierMesh().chunkSetLocate("Pilot1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals("us")) {
            int i = Mission.getMissionDate(true);
            if ((i > 0) && (i < 0x1287ce4)) {
                return "us_early_";
            } else {
                return "us_";
            }
        } else {
            return "";
        }
    }

    static {
        Class class1 = F4UCORSAIR1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F4U");
        Property.set(class1, "meshName", "3DO/Plane/CorsairMkI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_gb", "3DO/Plane/CorsairMkI(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_rz", "3DO/Plane/CorsairMkI(RZ)/hier.him");
        Property.set(class1, "PaintScheme_rz", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_us", "3DO/Plane/CorsairMkI(GB)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/F4U-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF4U1.class });
        Property.set(class1, "LOSElevation", 1.0151F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb02", "_ExternalBomb03" });
    }
}
