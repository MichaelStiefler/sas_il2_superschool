package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.ToKGUtils;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class JU_88A4torp extends JU_88NEW implements TypeStormovik, TypeBomber, TypeScout, TypeHasToKG {

    public JU_88A4torp() {
        this.diveMechStage = 0;
        this.bNDives = false;
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 150F;
        this.fSightCurReadyness = 0.0F;
        this.fDiveRecoveryAlt = 850F;
        this.fDiveVelocity = 150F;
        this.fDiveAngle = 70F;
        this.fAOB = 0.0F;
        this.fShipSpeed = 15F;
        this.spreadAngle = 0;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            JU_88A4torp.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            JU_88A4torp.bChangedPit = true;
        }
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                break;

            case 2:
                this.FM.turret[1].setHealth(f);
                this.FM.turret[2].setHealth(f);
                break;

            case 3:
                this.FM.turret[3].setHealth(f);
                break;
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

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                this.hierMesh().chunkVisible("HMask4_D0", false);
                break;
        }
    }

    public void typeBomberUpdate(float f) {
        if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
            this.fSightCurReadyness -= 0.0666666F * f;
            if (this.fSightCurReadyness < 0.0F) {
                this.fSightCurReadyness = 0.0F;
            }
        }
        if (this.fSightCurReadyness < 1.0F) {
            this.fSightCurReadyness += 0.0333333F * f;
        } else if (this.bSightAutomation) {
            return;
        }
    }

    public boolean typeBomberToggleAutomation() {
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
        this.spreadAngle++;
        if (this.spreadAngle > 30) {
            this.spreadAngle = 30;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpread", new Object[] { new Integer(this.spreadAngle) });
        this.FM.AS.setSpreadAngle(this.spreadAngle);
    }

    public void typeBomberAdjDistanceMinus() {
        this.spreadAngle--;
        if (this.spreadAngle < 0) {
            this.spreadAngle = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpread", new Object[] { new Integer(this.spreadAngle) });
        this.FM.AS.setSpreadAngle(this.spreadAngle);
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
        this.fAOB++;
        if (this.fAOB > 180F) {
            this.fAOB = 180F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] { new Integer((int) this.fAOB) });
        ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
    }

    public void typeBomberAdjSideslipMinus() {
        this.fAOB--;
        if (this.fAOB < -180F) {
            this.fAOB = -180F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] { new Integer((int) this.fAOB) });
        ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
        this.fShipSpeed++;
        if (this.fShipSpeed > 35F) {
            this.fShipSpeed = 35F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) this.fShipSpeed) });
        ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
    }

    public void typeBomberAdjSpeedMinus() {
        this.fShipSpeed--;
        if (this.fShipSpeed < 0.0F) {
            this.fShipSpeed = 0.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) this.fShipSpeed) });
        ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(this.fSightCurDistance);
        netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
        netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedByte();
        this.bSightAutomation = (i & 1) != 0;
        this.bSightBombDump = (i & 2) != 0;
        this.fSightCurDistance = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
        this.fSightCurSideslip = -3F + (netmsginput.readUnsignedByte() / 33.33333F);
        this.fSightCurAltitude = this.fDiveRecoveryAlt = netmsginput.readFloat();
        this.fSightCurSpeed = this.fDiveVelocity = netmsginput.readUnsignedByte() * 2.5F;
        this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, -65F, 65F, 65F, -65F), 0.0F);
    }

    public void update(float f) {
        super.update(f);
        if ((Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) > 70F) && (this.FM.CT.getFlap() > 0.01D) && (this.FM.CT.FlapsControl != 0.0F)) {
            this.FM.CT.FlapsControl = 0.0F;
            World.cur();
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log("FlapsRaised");
            }
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 4; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    public boolean isSalvo() {
        return this.thisWeaponsName.indexOf("salvo") != -1;
    }

    public static boolean bChangedPit = false;
    public int            diveMechStage;
    public boolean        bNDives;
    private boolean       bSightAutomation;
    private boolean       bSightBombDump;
    private float         fSightCurDistance;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;
    public float          fDiveRecoveryAlt;
    public float          fDiveVelocity;
    public float          fDiveAngle;
    protected float       fAOB;
    protected float       fShipSpeed;
    protected int         spreadAngle;

    static {
        Class class1 = JU_88A4torp.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88A-4torp/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88A-4torp.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_88A4torp.class, CockpitJU_88A4torp_Observer.class, CockpitJU_88A4torp_NGunner.class, CockpitJU_88A4torp_RGunner.class, CockpitJU_88A4torp_BGunner.class });
        Property.set(class1, "LOSElevation", 1.0976F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 3, 3, 3, 3, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_CANNON01" });
    }
}
