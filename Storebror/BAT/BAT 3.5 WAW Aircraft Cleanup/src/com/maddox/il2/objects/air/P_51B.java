package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class P_51B extends P_51 {

    public P_51B() {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, -185F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.75F;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        String s1 = "";
        String s2 = "";
        int i = Mission.getMissionDate(true);
        if ((i > 0) && (i < 0x128a3de)) {
            s1 = "PreInvasion_";
        }
        if (regiment.country().equals("us")) {
            if (regiment.name().equals("15AF_332FG_099FS")) {
                s2 = "332FG_";
                s1 = "";
            } else if (regiment.name().startsWith("8AF_359FG")) {
                s2 = "359FG_";
            } else if (regiment.name().startsWith("8AF_352FG")) {
                s2 = "352FG_";
            }
        }
        return s1 + s2;
    }

    static {
        Class class1 = P_51B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-51");
        Property.set(class1, "meshNameDemo", "3DO/Plane/P-51B(USA)/hier.him");
        Property.set(class1, "meshName", "3DO/Plane/P-51B(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/P-51B(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-51B.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_51B.class });
        Property.set(class1, "LOSElevation", 1.03F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
