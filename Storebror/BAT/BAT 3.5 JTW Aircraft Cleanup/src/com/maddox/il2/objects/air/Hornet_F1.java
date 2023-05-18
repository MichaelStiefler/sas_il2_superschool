package com.maddox.il2.objects.air;

import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class Hornet_F1 extends DH103 {

    public Hornet_F1() {
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.radarmode = 5;
                break;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            default:
                return super.cutFM(i, j, actor);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("01")) {
            this.hierMesh().chunkVisible("S1ASR", true);
            this.hierMesh().chunkVisible("S7ASR", true);
        }
        if (this.thisWeaponsName.startsWith("02")) {
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S6FAB", true);
        }
        if (this.thisWeaponsName.startsWith("03")) {
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S6FAB", true);
        }
        if (this.thisWeaponsName.startsWith("04")) {
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S6PTB", true);
        }
        if (this.thisWeaponsName.startsWith("05")) {
            this.hierMesh().chunkVisible("S1ASR", true);
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S6FAB", true);
            this.hierMesh().chunkVisible("S7ASR", true);
        }
        if (this.thisWeaponsName.startsWith("06")) {
            this.hierMesh().chunkVisible("S1ASR", true);
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S6FAB", true);
            this.hierMesh().chunkVisible("S7ASR", true);
        }
        if (this.thisWeaponsName.startsWith("07")) {
            this.hierMesh().chunkVisible("S1ASR", true);
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S6PTB", true);
            this.hierMesh().chunkVisible("S7ASR", true);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if ((this.FM.Gears.nearGround() || this.FM.Gears.onGround()) && (this.FM.CT.getCockpitDoor() == 1.0F)) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    public void update(float f) {
        super.update(f);
        if (!(this.FM instanceof Pilot)) {
            return;
        } else {
            return;
        }
    }

    static {
        Class class1 = Hornet_F1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hornet F.1");
        Property.set(class1, "meshName", "3DO/Plane/HornetF1(Multi1)/Hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1946.8F);
        Property.set(class1, "yearExpired", 1957.7F);
        Property.set(class1, "FlightModel", "FlightModels/DH103-F1.fmd:DH103-F1_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDH103.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
