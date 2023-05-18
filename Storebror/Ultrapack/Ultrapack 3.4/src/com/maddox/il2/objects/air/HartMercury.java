package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class HartMercury extends HartXYZ {
    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, -40F * f, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("AroneL_D0", false);
                this.hierMesh().chunkVisible("AroneR_D0", false);
                this.hierMesh().chunkVisible("StrutsL_D0", false);
                this.hierMesh().chunkVisible("StrutsR_D0", false);
                this.hierMesh().chunkVisible("WingLIn_D0", false);
                this.hierMesh().chunkVisible("WingRIn_D0", false);
                this.hierMesh().chunkVisible("WingLMid_D0", false);
                this.hierMesh().chunkVisible("WingRMid_D0", false);
                this.hierMesh().chunkVisible("WingLOut_D0", false);
                this.hierMesh().chunkVisible("WingROut_D0", false);
                this.hierMesh().chunkVisible("WireL_D0", false);
                this.hierMesh().chunkVisible("WireR_D0", false);
                this.hierMesh().chunkVisible("CF_D0", false);
                this.hierMesh().chunkVisible("Engine1_D0", false);
                this.hierMesh().chunkVisible("SlatL_D0", false);
                this.hierMesh().chunkVisible("SlatR_D0", false);
            } else {
                this.hierMesh().chunkVisible("Blister1_D0", true);
                this.hierMesh().chunkVisible("AroneL_D0", true);
                this.hierMesh().chunkVisible("AroneR_D0", true);
                this.hierMesh().chunkVisible("StrutsL_D0", true);
                this.hierMesh().chunkVisible("StrutsR_D0", true);
                this.hierMesh().chunkVisible("WingLIn_D0", true);
                this.hierMesh().chunkVisible("WingRIn_D0", true);
                this.hierMesh().chunkVisible("WingLMid_D0", true);
                this.hierMesh().chunkVisible("WingRMid_D0", true);
                this.hierMesh().chunkVisible("WingLOut_D0", true);
                this.hierMesh().chunkVisible("WingROut_D0", true);
                this.hierMesh().chunkVisible("WireL_D0", true);
                this.hierMesh().chunkVisible("WireR_D0", true);
                this.hierMesh().chunkVisible("CF_D0", true);
                this.hierMesh().chunkVisible("Engine1_D0", true);
                this.hierMesh().chunkVisible("SlatL_D0", true);
                this.hierMesh().chunkVisible("SlatR_D0", true);
            }
        }
//        if (this.FM.isPlayers()) {
//            if (!Main3D.cur3D().isViewOutside()) {
//                this.hierMesh().chunkVisible("Blister1_D1", false);
//            }
//            this.hierMesh().chunkVisible("Blister1_D2", false);
//            this.hierMesh().chunkVisible("AroneL_D1", false);
//            this.hierMesh().chunkVisible("AroneR_D1", false);
//            this.hierMesh().chunkVisible("StrutsL_D1", false);
//            this.hierMesh().chunkVisible("StrutsR_D1", false);
//            this.hierMesh().chunkVisible("AroneL_D2", false);
//            this.hierMesh().chunkVisible("AroneR_D2", false);
//            this.hierMesh().chunkVisible("StrutsL_D2", false);
//            this.hierMesh().chunkVisible("StrutsR_D2", false);
//            this.hierMesh().chunkVisible("WingLIn_D1", false);
//            this.hierMesh().chunkVisible("WingRIn_D1", false);
//            this.hierMesh().chunkVisible("WingLMid_D1", false);
//            this.hierMesh().chunkVisible("WingRMid_D1", false);
//            this.hierMesh().chunkVisible("WingLOut_D1", false);
//            this.hierMesh().chunkVisible("WingROut_D1", false);
//            this.hierMesh().chunkVisible("WireL_D1", false);
//            this.hierMesh().chunkVisible("WireR_D1", false);
//            this.hierMesh().chunkVisible("CF_D1", false);
//            this.hierMesh().chunkVisible("Engine1_D1", false);
//            this.hierMesh().chunkVisible("WingLIn_D2", false);
//            this.hierMesh().chunkVisible("WingRIn_D2", false);
//            this.hierMesh().chunkVisible("WingLMid_D2", false);
//            this.hierMesh().chunkVisible("WingRMid_D2", false);
//            this.hierMesh().chunkVisible("WingLOut_D2", false);
//            this.hierMesh().chunkVisible("WingROut_D2", false);
//            this.hierMesh().chunkVisible("WireL_D2", false);
//            this.hierMesh().chunkVisible("WireR_D2", false);
//            this.hierMesh().chunkVisible("CF_D2", false);
//            this.hierMesh().chunkVisible("Engine1_D2", false);
//            this.hierMesh().chunkVisible("SlatL_D1", false);
//            this.hierMesh().chunkVisible("SlatR_D1", false);
//            this.hierMesh().chunkVisible("SlatL_D2", false);
//            this.hierMesh().chunkVisible("SlatR_D2", false);
//        }
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
        Class class1 = HartMercury.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hart");
        Property.set(class1, "meshName", "3DO/Plane/Osprey/hierHM.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/HartM.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitOspreyL.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 9, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb17", "_ExternalBomb18", "_ExternalDev11", "_ExternalDev12", "_ExternalBomb19", "_ExternalBomb20" });
    }
}
