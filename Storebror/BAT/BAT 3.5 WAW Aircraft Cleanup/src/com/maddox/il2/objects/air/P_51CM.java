package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class P_51CM extends P_51 {

    public P_51CM() {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.6F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
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

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        this.hierMesh().chunkVisible("Pilot1_D0", false);
        this.hierMesh().chunkVisible("pilot1a_D0", false);
        this.hierMesh().chunkVisible("pilot1b_D0", false);
        this.hierMesh().chunkVisible("pilot1c_D0", false);
        this.hierMesh().chunkVisible("pilot1d_D0", false);
        this.hierMesh().chunkVisible("pilot1e_D0", false);
        this.hierMesh().chunkVisible("Head1_D0", false);
        this.hierMesh().chunkVisible("Head1a_D0", false);
        this.hierMesh().chunkVisible("Head1b_D0", false);
        this.hierMesh().chunkVisible("Head1c_D0", false);
        this.hierMesh().chunkVisible("Head1d_D0", false);
        this.hierMesh().chunkVisible("HMask1_D0", false);
        this.hierMesh().chunkVisible("HangMask1_D0", false);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("pilot1a_D0", false);
                this.hierMesh().chunkVisible("pilot1b_D0", false);
                this.hierMesh().chunkVisible("pilot1c_D0", false);
                this.hierMesh().chunkVisible("pilot1d_D0", false);
                this.hierMesh().chunkVisible("pilot1e_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Head1a_D0", false);
                this.hierMesh().chunkVisible("Head1b_D0", false);
                this.hierMesh().chunkVisible("Head1c_D0", false);
                this.hierMesh().chunkVisible("Head1d_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("HangMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("pilot1a_D1", true);
                this.hierMesh().chunkVisible("pilot1b_D1", true);
                this.hierMesh().chunkVisible("pilot1c_D1", true);
                this.hierMesh().chunkVisible("pilot1d_D1", true);
                this.hierMesh().chunkVisible("pilot1e_D1", true);
                this.hierMesh().chunkVisible("Head1_D1", true);
                this.hierMesh().chunkVisible("Head1a_D1", true);
                this.hierMesh().chunkVisible("Head1b_D1", true);
                this.hierMesh().chunkVisible("Head1c_D1", true);
                this.hierMesh().chunkVisible("Head1d_D1", true);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HangMask1_D0", true);
            if (this.FM.AS.bIsAboutToBailout) {
                this.hierMesh().chunkVisible("HangMask1_D0", false);
            }
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            if ((this.FM.getAltitude() >= 3000F) || this.hierMesh().isChunkVisible("HMask1_D0") || this.FM.AS.bIsAboutToBailout) {
                this.hierMesh().chunkVisible("HangMask1_D0", false);
            }
        }
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
        return s1 + s2;
    }

    static {
        Class class1 = P_51CM.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mustang");
        Property.set(class1, "meshName", "3DO/Plane/MustangMkIII(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_gb", "3DO/Plane/MustangMkIII(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-51CM.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_51C.class });
        Property.set(class1, "LOSElevation", 1.03F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
