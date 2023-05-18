package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class MustangMkI extends P_51 {

    public MustangMkI() {
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

    static {
        Class class1 = MustangMkI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-51");
        Property.set(class1, "meshName", "3DO/Plane/MustangMkI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_gb", "3DO/Plane/MustangMkI(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1943.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-51A.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_51B.class });
        Property.set(class1, "LOSElevation", 1.03F);
        Aircraft.weaponTriggersRegister(class1, new int[8]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08" });
    }
}
