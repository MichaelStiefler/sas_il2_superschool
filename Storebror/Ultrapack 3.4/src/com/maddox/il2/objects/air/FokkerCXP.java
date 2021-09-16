package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class FokkerCXP extends Biplanexyz {
    public void update(float f) {
        super.update(f);
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("Blister2_D0", false);
            } else {
                this.hierMesh().chunkVisible("Blister1_D0", true);
                this.hierMesh().chunkVisible("Blister2_D0", true);
            }
        }
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D1", false);
            }
            this.hierMesh().chunkVisible("Blister2_D1", false);
            this.hierMesh().chunkVisible("Blister1_D2", false);
            this.hierMesh().chunkVisible("Blister2_D3", false);
        }
        if (Config.isUSE_RENDER() && (World.cur().camouflage == 1)) {
            this.hasSkis = true;
            this.hierMesh().chunkVisible("GearL1_D0", false);
            this.hierMesh().chunkVisible("GearR1_D0", false);
            this.hierMesh().chunkVisible("GearC1_D0", false);
            this.hierMesh().chunkVisible("SkiC1_D0", true);
            this.hierMesh().chunkVisible("SkiL1_D0", true);
            this.hierMesh().chunkVisible("SkiR1_D0", true);
            this.FM.CT.bHasBrakeControl = false;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        if ((World.cur().camouflage == 1) && (World.Rnd().nextFloat() > 0.1F)) {
            hiermesh.chunkVisible("GearL1_D0", false);
            hiermesh.chunkVisible("GearR1_D0", false);
            hiermesh.chunkVisible("GearC1_D0", false);
            hiermesh.chunkVisible("SkiC1_D0", true);
            hiermesh.chunkVisible("SkiL1_D0", true);
            hiermesh.chunkVisible("SkiR1_D0", true);
            hiermesh.chunkSetAngles("SkiL1_D0", 0.0F, 12F, 0.0F);
            hiermesh.chunkSetAngles("SkiR1_D0", 0.0F, 12F, 0.0F);
            hiermesh.chunkSetAngles("SkiC1_D0", 0.0F, 12F, 0.0F);
        }
    }

    public boolean hasSkis;

    static {
        Class class1 = FokkerCXP.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FokkerCX");
        Property.set(class1, "meshName", "3DO/Plane/FokkerCX/hierP.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/FokkerCXP.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFokkerCX.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24" });
    }
}
