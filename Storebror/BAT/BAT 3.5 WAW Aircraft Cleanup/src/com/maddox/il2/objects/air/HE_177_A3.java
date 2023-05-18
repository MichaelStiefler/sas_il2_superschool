package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.ToKGUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class HE_177_A3 extends HE_177X implements TypeBomber, TypeTransport, TypeX4Carrier, TypeGuidedBombCarrier, TypeHasToKG {

    public HE_177_A3() {
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.isGuidingBomb = false;
        this.hasToKG = false;
        this.spreadAngle = 0;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("A51") || this.thisWeaponsName.startsWith("A53") || this.thisWeaponsName.startsWith("A54")) {
            if (this.thisWeaponsName.indexOf("293") != -1) {
                this.hierMesh().chunkVisible("PylonHs293C", true);
            } else if (this.thisWeaponsName.indexOf("FritzX") != -1) {
                this.hierMesh().chunkVisible("PylonFritzXC", true);
            } else if (this.thisWeaponsName.indexOf("LT50") != -1) {
                this.hierMesh().chunkVisible("PylonLT50L", true);
                this.hierMesh().chunkVisible("PylonLT50R", true);
                this.hierMesh().chunkVisible("PylonLT50C1", true);
                this.hierMesh().chunkVisible("PylonLT50C2", true);
                this.hasToKG = true;
            } else if (this.thisWeaponsName.indexOf("SC2500") != -1) {
                this.hierMesh().chunkVisible("PylonSC2500C", true);
            } else {
                this.hierMesh().chunkVisible("PylonC", true);
            }
        }
        if (this.thisWeaponsName.startsWith("A52") || this.thisWeaponsName.startsWith("A53")) {
            if (this.thisWeaponsName.indexOf("293") != -1) {
                this.hierMesh().chunkVisible("PylonHs293L", true);
                this.hierMesh().chunkVisible("PylonHs293R", true);
            } else if ((this.thisWeaponsName.indexOf("FritzX") != -1) || (this.thisWeaponsName.indexOf("SC") != -1)) {
                this.hierMesh().chunkVisible("PylonFritzXL", true);
                this.hierMesh().chunkVisible("PylonFritzXR", true);
            } else if (this.thisWeaponsName.indexOf("LT50") != -1) {
                this.hierMesh().chunkVisible("PylonLT50C1", true);
                this.hierMesh().chunkVisible("PylonLT50C2", true);
                this.hasToKG = true;
            } else {
                this.hierMesh().chunkVisible("PylonL", true);
                this.hierMesh().chunkVisible("PylonR", true);
            }
        }
        if (this.thisWeaponsName.startsWith("A5")) {
            this.FM.CT.bHasBayDoorControl = false;
        }
    }

    public void update(float f) {
        super.update(f);
        this.checkAftVentralGun();
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if ((Main3D.cur3D().cockpitCurIndx() != 3) && (Main3D.cur3D().cockpitCurIndx() != 4) && (Time.current() > this.ventralGunnerTargetCheckTime)) {
            this.ventralGunnerTargetCheckTime = Time.current() + World.Rnd().nextLong(5000L, 20000L);
            if (this.FM.turret.length != 0) {
                if (this.FM.turret[2].target == null) {
                    this.setVentralGunnerDirection(0);
                } else {
                    this.setVentralGunnerDirection(1);
                }
            }
        }
    }

    public boolean isSalvo() {
        return this.thisWeaponsName.indexOf("spread") == -1;
    }

    public void typeBomberAdjAltitudePlus() {
        if (this.isGuidingBomb) {
            this.typeX4CAdjAttitudePlus();
            return;
        }
        if (this.hasToKG) {
            this.fShipSpeed++;
            if (this.fShipSpeed > 35F) {
                this.fShipSpeed = 35F;
            }
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) this.fShipSpeed) });
            return;
        } else {
            super.typeBomberAdjAltitudePlus();
            return;
        }
    }

    public void typeBomberAdjAltitudeMinus() {
        if (this.isGuidingBomb) {
            this.typeX4CAdjAttitudeMinus();
            return;
        }
        if (this.hasToKG) {
            this.fShipSpeed--;
            if (this.fShipSpeed < 0.0F) {
                this.fShipSpeed = 0.0F;
            }
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) this.fShipSpeed) });
            return;
        } else {
            super.typeBomberAdjAltitudeMinus();
            return;
        }
    }

    public void typeBomberAdjSideslipPlus() {
        if (this.isGuidingBomb) {
            this.typeX4CAdjSidePlus();
            return;
        }
        if (this.hasToKG) {
            this.fAOB++;
            if (this.fAOB > 180F) {
                this.fAOB = 180F;
            }
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] { new Integer((int) this.fAOB) });
            return;
        }
        this.fSightCurSideslip += 0.05F;
        if (this.fSightCurSideslip > 3F) {
            this.fSightCurSideslip = 3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
    }

    public void typeBomberAdjSideslipMinus() {
        if (this.isGuidingBomb) {
            this.typeX4CAdjSideMinus();
            return;
        }
        if (this.hasToKG) {
            this.fAOB--;
            if (this.fAOB < -180F) {
                this.fAOB = -180F;
            }
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] { new Integer((int) this.fAOB) });
            return;
        } else {
            super.typeBomberAdjSideslipMinus();
            return;
        }
    }

    public void typeBomberAdjDistancePlus() {
        if (this.isGuidingBomb) {
            this.typeX4CAdjAttitudePlus();
            return;
        }
        if (this.hasToKG) {
            this.fShipSpeed++;
            if (this.fShipSpeed > 35F) {
                this.fShipSpeed = 35F;
            }
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) this.fShipSpeed) });
            return;
        } else {
            super.typeBomberAdjDistancePlus();
            return;
        }
    }

    public void typeBomberAdjDistanceMinus() {
        if (this.isGuidingBomb) {
            this.typeX4CAdjAttitudeMinus();
            return;
        }
        if (this.hasToKG) {
            this.fShipSpeed--;
            if (this.fShipSpeed < 0.0F) {
                this.fShipSpeed = 0.0F;
            }
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) this.fShipSpeed) });
            return;
        } else {
            super.typeBomberAdjDistanceMinus();
            return;
        }
    }

    public void typeBomberAdjSpeedPlus() {
        if (this.isGuidingBomb) {
            this.typeX4CAdjAttitudePlus();
            return;
        }
        if (this.hasToKG) {
            this.fShipSpeed++;
            if (this.fShipSpeed > 35F) {
                this.fShipSpeed = 35F;
            }
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) this.fShipSpeed) });
            return;
        } else {
            super.typeBomberAdjSpeedPlus();
            return;
        }
    }

    public void typeBomberAdjSpeedMinus() {
        if (this.isGuidingBomb) {
            this.typeX4CAdjAttitudeMinus();
            return;
        }
        if (this.hasToKG) {
            this.fShipSpeed--;
            if (this.fShipSpeed < 0.0F) {
                this.fShipSpeed = 0.0F;
            }
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) this.fShipSpeed) });
            return;
        } else {
            super.typeBomberAdjSpeedMinus();
            return;
        }
    }

    public boolean typeGuidedBombCisMasterAlive() {
        return this.isMasterAlive;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag) {
        this.isMasterAlive = flag;
    }

    public boolean typeGuidedBombCgetIsGuiding() {
        return this.isGuidingBomb;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag) {
        this.isGuidingBomb = flag;
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 0.002F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -0.002F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -0.002F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    private float   deltaAzimuth;
    private float   deltaTangage;
    private boolean isGuidingBomb;
    private boolean isMasterAlive;
    protected float fAOB;
    protected float fShipSpeed;
    public boolean  hasToKG;
    protected int   spreadAngle;

    static {
        Class class1 = HE_177_A3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He-177");
        Property.set(class1, "meshName", "3do/plane/He-177A-3/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1939.5F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/He-177A-3.fmd:He177_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHE_177.class, CockpitHE_177_Bombardier.class, CockpitHE_177_A3_NGunner.class, CockpitHE_177_A3_FGunner.class, CockpitHE_177_A3_BGunner.class, CockpitHE_177_A3_PGunner.class, CockpitHE_177_A3_TGunner.class, CockpitHE_177_A3_RGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 14, 15, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn02", "_BombSpawn03", "_BombSpawn03", "_BombSpawn04", "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_BombSpawn06", "_BombSpawn06", "_BombSpawn07", "_BombSpawn07", "_BombSpawn08", "_BombSpawn08", "_BombSpawn09", "_BombSpawn09", "_BombSpawn10", "_BombSpawn10", "_BombSpawn11", "_BombSpawn11", "_BombSpawn12", "_BombSpawn12", "_BombSpawn13", "_BombSpawn13", "_BombSpawn14", "_BombSpawn14", "_BombSpawn15", "_BombSpawn15", "_BombSpawn16", "_BombSpawn16", "_BombSpawn17", "_BombSpawn17", "_BombSpawn18", "_BombSpawn18", "_BombSpawn19", "_BombSpawn19", "_BombSpawn20", "_BombSpawn20", "_BombSpawn21", "_BombSpawn21", "_BombSpawn22", "_BombSpawn22", "_BombSpawn23", "_BombSpawn23", "_BombSpawn24", "_BombSpawn24", "_BombSpawn25", "_BombSpawn25", "_BombSpawn26", "_BombSpawn26", "_BombSpawn27", "_BombSpawn27", "_BombSpawn28", "_BombSpawn28", "_BombSpawn29",
                        "_BombSpawn29", "_BombSpawn30", "_BombSpawn30", "_BombSpawn31", "_BombSpawn31", "_BombSpawn32", "_BombSpawn32", "_BombSpawn33", "_BombSpawn33", "_BombSpawn34", "_BombSpawn34", "_BombSpawn35", "_BombSpawn35", "_BombSpawn36", "_BombSpawn36", "_BombSpawn37", "_BombSpawn37", "_BombSpawn38", "_BombSpawn38", "_BombSpawn39", "_BombSpawn39", "_BombSpawn40", "_BombSpawn40", "_BombSpawn41", "_BombSpawn41", "_BombSpawn42", "_BombSpawn42", "_BombSpawn43", "_BombSpawn43", "_BombSpawn44", "_BombSpawn44", "_BombSpawn45", "_BombSpawn45", "_BombSpawn46", "_BombSpawn46", "_BombSpawn47", "_BombSpawn47", "_BombSpawn48", "_BombSpawn48", "_BombSpawn49", "_BombSpawn49", "_BombSpawn50", "_BombSpawn50", "_BombSpawn51", "_BombSpawn51", "_BombSpawn52", "_BombSpawn52", "_BombSpawn53", "_BombSpawn53", "_BombSpawn54", "_BombSpawn54", "_BombSpawn55", "_BombSpawn55", "_BombSpawn56", "_BombSpawn56", "_BombSpawn57", "_BombSpawn57", "_BombSpawn58", "_BombSpawn58", "_BombSpawn59", "_BombSpawn59", "_BombSpawn60",
                        "_BombSpawn60", "_BombSpawn61", "_BombSpawn61", "_BombSpawn62", "_BombSpawn62", "_BombSpawn63", "_BombSpawn63", "_BombSpawn64", "_BombSpawn64", "_BombSpawn65", "_BombSpawn65", "_BombSpawn66", "_BombSpawn66", "_BombSpawn67", "_BombSpawn67", "_BombSpawn68", "_BombSpawn68", "_BombSpawn69", "_BombSpawn69", "_BombSpawn70", "_BombSpawn70", "_BombSpawn71", "_BombSpawn71", "_BombSpawn72", "_BombSpawn72", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb11", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb13", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb31", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06" });
    }
}
