package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class S_T6 extends Texan implements TypeStormovik {

    public S_T6() {
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
                this.hierMesh().chunkVisible("Parabrisas_D0", false);
            } else {
                this.hierMesh().chunkVisible("Parabrisas_D0", true);
            }
        }
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
            ((Actor) (World.getPlayerAircraft())).pos.getAbsPoint();
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
        Class class1 = S_T6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "T6");
        Property.set(class1, "meshName", "3DO/Plane/S_T6(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1975.5F);
        Property.set(class1, "FlightModel", "FlightModels/T-6Texan.fmd:S_T6_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitS_T6_a.class, CockpitS_T6_b.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "" });
    }
}
