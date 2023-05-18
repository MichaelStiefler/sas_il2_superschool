package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Texan_AT6 extends Texan123 implements TypeStormovik {

    public Texan_AT6() {
        this.bHasBlister = true;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.7F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.7F);
        this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
        Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void update(float f) {
        this.onAircraftLoaded();
        super.update(f);
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("Blister2_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
            } else if (this.bHasBlister) {
                this.hierMesh().chunkVisible("Blister1_D0", true);
                this.hierMesh().chunkVisible("Blister2_D0", true);
                this.hierMesh().chunkVisible("Pilot1_D0", true);
                this.hierMesh().chunkVisible("Head1_D0", true);
            }
            World.getPlayerAircraft().pos.getAbsPoint();
            if (this.FM.AS.bIsAboutToBailout) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("Blister2_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
            }
            if (this.hierMesh().isChunkVisible("Pilot1_D1")) {
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
            }
        }
    }

    public boolean bHasBlister;

    static {
        Class class1 = Texan_AT6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Texan");
        Property.set(class1, "meshName", "3DO/Plane/Texan-AT6/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1975.5F);
        Property.set(class1, "FlightModel", "FlightModels/T6_Texan.fmd:T6_TEXAN_FM");
        Property.set(class1, "cockpitClass", new Class[] { Cockpit_Texan_F.class, Cockpit_Texan_R.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 3, 3, 3, 9, 9, 9, 0, 0, 0, 0, 9, 9, 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev04", "_ExternalDev05", "_MGUN09", "_MGUN10" });
    }
}
