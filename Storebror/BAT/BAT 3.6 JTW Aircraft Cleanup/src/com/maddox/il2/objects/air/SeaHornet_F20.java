package com.maddox.il2.objects.air;

import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class SeaHornet_F20 extends DH103 {

    public SeaHornet_F20() {
        this.arrestor = 0.0F;
        this.RSOKilled = false;
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, 150F * f, 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, -150F * f, 0.0F);
    }

    public void moveWingFold(float f) {
        if (f < 0.001F) {
            this.setGunPodsOn(true);
            this.hideWingWeapons(false);
        } else {
            this.setGunPodsOn(false);
            this.FM.CT.WeaponControl[0] = false;
            this.hideWingWeapons(true);
        }
        this.moveWingFold(this.hierMesh(), f);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("TailHook_D0", 0.0F, 35F * f, 0.0F);
        this.arrestor = f;
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
        if (this.thisWeaponsName.startsWith("08")) {
            this.hierMesh().chunkVisible("S2ASM", true);
            this.hierMesh().chunkVisible("S4ASM", true);
            this.hierMesh().chunkVisible("S6ASM", true);
        }
        if (this.thisWeaponsName.startsWith("09")) {
            this.hierMesh().chunkVisible("S2ASM", true);
            this.hierMesh().chunkVisible("S3FAB", true);
            this.hierMesh().chunkVisible("S4ASM", true);
            this.hierMesh().chunkVisible("S5FAB", true);
            this.hierMesh().chunkVisible("S6ASM", true);
        }
        if (this.thisWeaponsName.startsWith("10")) {
            this.hierMesh().chunkVisible("S4KAB", true);
        }
        if (this.thisWeaponsName.startsWith("11")) {
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S4KAB", true);
            this.hierMesh().chunkVisible("S6PTB", true);
        }
        if (this.thisWeaponsName.startsWith("12")) {
            this.hierMesh().chunkVisible("S2ASM", true);
            this.hierMesh().chunkVisible("S4KAB", true);
            this.hierMesh().chunkVisible("S6ASM", true);
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
        }
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-33F * this.FM.Gears.arrestorVSink) / 57F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.2F) {
                    f2 = 0.2F;
                }
                if (f2 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f2));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
                }
                if (this.arrestor < 0.0F) {
                    this.arrestor = 0.0F;
                } else if (this.arrestor > 1.0F) {
                    this.arrestor = 1.0F;
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
    }

    private float arrestor;
    static {
        Class class1 = SeaHornet_F20.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SeaHornet F.20");
        Property.set(class1, "meshName", "3DO/Plane/SeaHornetF20(Multi1)/Hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1946.8F);
        Property.set(class1, "yearExpired", 1957.7F);
        Property.set(class1, "FlightModel", "FlightModels/DH103-F20.fmd:DH103-F20_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDH103.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
