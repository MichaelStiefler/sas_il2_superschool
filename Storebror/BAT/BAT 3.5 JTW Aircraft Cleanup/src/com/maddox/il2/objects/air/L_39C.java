package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class L_39C extends L39A {

    public L_39C() {
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            if ((this.FM.EI.engines[0].getPowerOutput() > 0.45F) && (this.FM.EI.engines[0].getStage() == 6)) {
                if (this.FM.EI.engines[0].getPowerOutput() > 0.65F) {
                    if (this.FM.EI.engines[0].getPowerOutput() > 1.001F) {
                        this.FM.AS.setSootState(this, 0, 3);
                    } else {
                        this.FM.AS.setSootState(this, 0, 2);
                    }
                } else {
                    this.FM.AS.setSootState(this, 0, 1);
                }
            } else if ((this.FM.EI.engines[0].getPowerOutput() <= 0.45F) || (this.FM.EI.engines[0].getStage() < 6)) {
                this.FM.AS.setSootState(this, 0, 0);
            }
        }
    }

    public void doSetSootState(int i, int j) {
        switch (j) {
            case 0:
                Eff3DActor.setIntesity(this.turbo, 0.0F);
                Eff3DActor.setIntesity(this.turbosmoke, 0.0F);
                Eff3DActor.setIntesity(this.afterburner, 0.0F);
                break;

            case 1:
                Eff3DActor.setIntesity(this.turbo, 0.0F);
                Eff3DActor.setIntesity(this.turbosmoke, 1.0F);
                Eff3DActor.setIntesity(this.afterburner, 0.0F);
                break;

            case 2:
                Eff3DActor.setIntesity(this.turbo, 1.0F);
                Eff3DActor.setIntesity(this.turbosmoke, 1.0F);
                Eff3DActor.setIntesity(this.afterburner, 0.0F);
                break;

            case 3:
                Eff3DActor.setIntesity(this.turbo, 1.0F);
                Eff3DActor.setIntesity(this.turbosmoke, 1.0F);
                Eff3DActor.setIntesity(this.afterburner, 1.0F);
                break;
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 100F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("tr")) {
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Head2_D0", true);
            this.hierMesh().chunkVisible("HMask2_D0", true);
        }
        if (this.thisWeaponsName.endsWith("armed")) {
            this.hierMesh().chunkVisible("PylonL2", true);
            this.hierMesh().chunkVisible("PylonR2", true);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
    }

    private Eff3DActor turbo;
    private Eff3DActor turbosmoke;
    private Eff3DActor afterburner;

    static {
        Class class1 = L_39C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "L-39");
        Property.set(class1, "meshName", "3DO/Plane/L-39C/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/L-39C.fmd:L39");
        Property.set(class1, "cockpitClass", new Class[] { CockpitL39.class, CockpitL39_2.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 0, 0, 9, 9, 3, 3, 3, 3, 2, 2, 2, 2, 9, 2, 2, 2, 2, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 2, 2, 9, 2, 2, 9, 9, 9, 9, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalRock01", "_ExternalRock02", "_MGUN01", "_MGUN02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalDev05", "_ExternalRock31", "_ExternalRock32",
                "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalDev16", "_ExternalDev17", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalDev18", "_ExternalDev19" });
    }
}
