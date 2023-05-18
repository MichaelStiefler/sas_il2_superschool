package com.maddox.il2.objects.air;

import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class OspreyL extends Biplanexyz {

    public OspreyL() {
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, -40F * f, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        this.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("AroneL_D0", false);
                this.hierMesh().chunkVisible("AroneR_D0", false);
                this.hierMesh().chunkVisible("StrutsL_D0", false);
                this.hierMesh().chunkVisible("StrutsR_D0", false);
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
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D1", false);
            }
            this.hierMesh().chunkVisible("Blister1_D2", false);
            this.hierMesh().chunkVisible("AroneL_D1", false);
            this.hierMesh().chunkVisible("AroneR_D1", false);
            this.hierMesh().chunkVisible("StrutsL_D1", false);
            this.hierMesh().chunkVisible("StrutsR_D1", false);
            this.hierMesh().chunkVisible("AroneL_D2", false);
            this.hierMesh().chunkVisible("AroneR_D2", false);
            this.hierMesh().chunkVisible("StrutsL_D2", false);
            this.hierMesh().chunkVisible("StrutsR_D2", false);
            this.hierMesh().chunkVisible("WingLMid_D1", false);
            this.hierMesh().chunkVisible("WingRMid_D1", false);
            this.hierMesh().chunkVisible("WingLOut_D1", false);
            this.hierMesh().chunkVisible("WingROut_D1", false);
            this.hierMesh().chunkVisible("WireL_D1", false);
            this.hierMesh().chunkVisible("WireR_D1", false);
            this.hierMesh().chunkVisible("CF_D1", false);
            this.hierMesh().chunkVisible("Engine1_D1", false);
            this.hierMesh().chunkVisible("WingLMid_D2", false);
            this.hierMesh().chunkVisible("WingRMid_D2", false);
            this.hierMesh().chunkVisible("WingLOut_D2", false);
            this.hierMesh().chunkVisible("WingROut_D2", false);
            this.hierMesh().chunkVisible("WireL_D2", false);
            this.hierMesh().chunkVisible("WireR_D2", false);
            this.hierMesh().chunkVisible("CF_D2", false);
            this.hierMesh().chunkVisible("Engine1_D2", false);
            this.hierMesh().chunkVisible("SlatL_D1", false);
            this.hierMesh().chunkVisible("SlatR_D1", false);
            this.hierMesh().chunkVisible("SlatL_D2", false);
            this.hierMesh().chunkVisible("SlatR_D2", false);
        }
    }

    static {
        Class class1 = OspreyL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "OspreyL");
        Property.set(class1, "meshName", "3DO/Plane/Osprey/hierL.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/OspreyL.fmd:OspreyL_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitOspreyL.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12" });
    }
}
