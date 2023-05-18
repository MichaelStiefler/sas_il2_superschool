package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class F84F1 extends F84 implements TypeFastJet {

    public F84F1() {
        this.APmode1 = false;
        this.APmode2 = false;
    }

    public void onAircraftLoaded() {
        this.bHasBoosterLoadout = this.thisWeaponsName.endsWith("JATO");
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER()) {
            this.turbo = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            this.turbosmoke = Eff3DActor.New(this, this.findHook("_Engine1ES_01"), null, 1.0F, "3DO/Effects/Aircraft/GraySmallTSPD.eff", -1F);
            this.afterburner = Eff3DActor.New(this, this.findHook("_Engine1EF_02"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurner.eff", -1F);
            Eff3DActor.setIntesity(this.turbo, 0.0F);
            Eff3DActor.setIntesity(this.turbosmoke, 0.0F);
            Eff3DActor.setIntesity(this.afterburner, 0.0F);
        }
        if (this.thisWeaponsName.endsWith("8xHVAR")) {
            this.hierMesh().chunkVisible("pylon2L_D0", false);
            this.hierMesh().chunkVisible("pylon2R_D0", false);
        }
        if (this.thisWeaponsName.endsWith("8xHVAR+JATO")) {
            this.hierMesh().chunkVisible("pylon2L_D0", false);
            this.hierMesh().chunkVisible("pylon2R_D0", false);
        }
        if (this.thisWeaponsName.endsWith("16xHVAR")) {
            this.hierMesh().chunkVisible("pylon1L_D0", false);
            this.hierMesh().chunkVisible("pylon1R_D0", false);
        }
        if (this.thisWeaponsName.endsWith("16xHVAR+JATO")) {
            this.hierMesh().chunkVisible("pylon1L_D0", false);
            this.hierMesh().chunkVisible("pylon1R_D0", false);
        }
        if (this.thisWeaponsName.endsWith("24xHVAR+JATO")) {
            this.hierMesh().chunkVisible("pylon1L_D0", false);
            this.hierMesh().chunkVisible("pylon1R_D0", false);
            this.hierMesh().chunkVisible("pylon2L_D0", false);
            this.hierMesh().chunkVisible("pylon2R_D0", false);
        }
        if (this.thisWeaponsName.endsWith("24xHVAR")) {
            this.hierMesh().chunkVisible("pylon1L_D0", false);
            this.hierMesh().chunkVisible("pylon1R_D0", false);
            this.hierMesh().chunkVisible("pylon2L_D0", false);
            this.hierMesh().chunkVisible("pylon2R_D0", false);
        }
        if (this.thisWeaponsName.startsWith("Def")) {
            this.hierMesh().chunkVisible("pylon1L_D0", false);
            this.hierMesh().chunkVisible("pylon1R_D0", false);
            this.hierMesh().chunkVisible("pylon2L_D0", false);
            this.hierMesh().chunkVisible("pylon2R_D0", false);
        }
    }

    public void applySootState() {
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
            this.setExhaustFlame(Math.round(Aircraft.cvt(this.FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
        }
    }

    public void update(float f) {
        this.computeSubsonicLimiter();
        super.update(f);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        F84.moveGear(hiermesh, f, f1, f2, 90F, 90F);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F, 40F * f);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F, 40F * f);
    }

    public void doSetSootState(int i, int j) {
        if (j < 0) {
            j = 0;
        }
        if (j > 3) {
            j = 3;
        }
        Eff3DActor.setIntesity(this.turbo, F84F1.sootStates[j][0]);
        Eff3DActor.setIntesity(this.turbosmoke, F84F1.sootStates[j][1]);
        Eff3DActor.setIntesity(this.afterburner, F84F1.sootStates[j][2]);
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 20) {
            if (!this.APmode1) {
                this.APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                this.FM.AP.setStabAltitude(1000F);
            } else if (this.APmode1) {
                this.APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                this.FM.AP.setStabAltitude(false);
            }
        }
        if (i == 21) {
            if (!this.APmode2) {
                this.APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                this.FM.AP.setStabDirection(true);
                this.FM.CT.bHasRudderControl = false;
            } else if (this.APmode2) {
                this.APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                this.FM.AP.setStabDirection(false);
                this.FM.CT.bHasRudderControl = true;
            }
        }
    }

    public void computeSubsonicLimiter() {
        float f = Aircraft.cvt(this.calculateMach(), 0.5F, 0.9F, 0.0F, 6000F);
        float f1 = Aircraft.cvt(this.FM.getAltitude(), 0.0F, 11000F, 1.0F, 0.46F);
        if (this.FM.EI.engines[0].getThrustOutput() < 1.001D) {
            this.FM.producedAF.x -= f * f1;
        }
    }

    private static float[][]   sootStates = { { 0.0F, 0.0F, 0.0F }, { 0.0F, 1.0F, 0.0F }, { 1.0F, 1.0F, 0.0F }, { 1.0F, 1.0F, 1.0F } };

    private Eff3DActor         turbo;
    private Eff3DActor         turbosmoke;
    private Eff3DActor         afterburner;
    public boolean             APmode1;
    public boolean             APmode2;

    static {
        Class class1 = F84F1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F84F");
        Property.set(class1, "meshName", "3DO/Plane/F84F/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/F84F1.fmd:F84_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF84F1.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26" });
    }
}
