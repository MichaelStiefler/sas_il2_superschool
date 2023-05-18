package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class JU_188beta extends JU_88NEW implements TypeBomber, TypeDiveBomber, TypeScout {

    public JU_188beta() {
        this.diveMechStage = 0;
        this.bNDives = false;
        this.bDropsBombs = false;
        this.needsToOpenBombays = false;
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
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("10xSC50")) {
            this.FM.M.fuel += 900F;
            this.FM.M.maxFuel += 900F;
            this.needsToOpenBombays = true;
        } else if (this.thisWeaponsName.startsWith("28xSC50") || this.thisWeaponsName.startsWith("18xSC50") || this.thisWeaponsName.startsWith("6xSC250") || this.thisWeaponsName.startsWith("Strafer_28xSC50")) {
            this.needsToOpenBombays = true;
        } else {
            this.FM.CT.bHasBayDoorControl = false;
        }
    }

    protected void moveBayDoor(float f) {
        if (this.needsToOpenBombays) {
            this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 85F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -85F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 85F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -85F * f, 0.0F);
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 4; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void update(float f) {
        this.updateJU87D5(f);
        this.updateJU87(f);
        super.update(f);
        if (Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) > 70F && this.FM.CT.getFlap() > 0.01D && this.FM.CT.FlapsControl != 0.0F) {
            this.FM.CT.FlapsControl = 0.0F;
            Actor actor = this.FM.actor;
            World.cur();
            if (actor == World.getPlayerAircraft()) HUD.log("FlapsRaised");
        }
    }

    public void updateJU87(float f) {
        if (this == World.getPlayerAircraft() && this.FM instanceof RealFlightModel) if (((RealFlightModel) this.FM).isRealMode()) switch (this.diveMechStage) {
            case 0:
                if (this.bNDives && this.FM.CT.AirBrakeControl == 1.0F && this.FM.Loc.z > this.fDiveRecoveryAlt) {
                    this.diveMechStage++;
                    this.bNDives = false;
                } else this.bNDives = this.FM.CT.AirBrakeControl != 1.0F;
                break;

            case 1:
                this.FM.CT.setTrimElevatorControl(-0.25F);
                this.FM.CT.trimElevator = -0.25F;
                if (this.FM.CT.AirBrakeControl == 0.0F || this.FM.CT.saveWeaponControl[3] || this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].countBullets() == 0) {
                    if (this.FM.CT.AirBrakeControl == 0.0F) this.diveMechStage++;
                    if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].countBullets() == 0) this.diveMechStage++;
                }
                break;

            case 2:
                this.FM.CT.setTrimElevatorControl(0.45F);
                this.FM.CT.trimElevator = 0.45F;
                if (this.FM.CT.AirBrakeControl == 0.0F || this.FM.Or.getTangage() > 0.0F) this.diveMechStage++;
                break;

            case 3:
                this.FM.CT.setTrimElevatorControl(0.0F);
                this.FM.CT.trimElevator = 0.0F;
                this.diveMechStage = 0;
                break;
        }
        else {
            this.FM.CT.setTrimElevatorControl(0.0F);
            this.FM.CT.trimElevator = 0.0F;
        }
        if (this.bDropsBombs && this.FM.isTick(3, 0) && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets())
            this.FM.CT.WeaponControl[3] = true;
    }

    public void updateJU87D5(float f) {
        this.fDiveAngle = -this.FM.Or.getTangage();
        if (this.fDiveAngle > 89F) this.fDiveAngle = 89F;
        if (this.fDiveAngle < 10F) this.fDiveAngle = 10F;
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -90F * f, 0.0F);
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

    public boolean typeBomberToggleAutomation() {
        this.bSightAutomation = !this.bSightAutomation;
        this.bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
        return this.bSightAutomation;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle++;
        if (this.fSightCurForwardAngle > 85F) this.fSightCurForwardAngle = 85F;
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) this.typeBomberToggleAutomation();
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle--;
        if (this.fSightCurForwardAngle < 0.0F) this.fSightCurForwardAngle = 0.0F;
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) this.typeBomberToggleAutomation();
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 0.05F;
        if (this.fSightCurSideslip > 3F) this.fSightCurSideslip = 3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.05F;
        if (this.fSightCurSideslip < -3F) this.fSightCurSideslip = -3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 850F;
        this.typeDiveBomberAdjAltitudeReset();
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 10000F) this.fSightCurAltitude = 10000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        this.typeDiveBomberAdjAltitudePlus();
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 500F) this.fSightCurAltitude = 500F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        this.typeDiveBomberAdjAltitudeMinus();
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 150F;
        this.typeDiveBomberAdjVelocityReset();
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 10F;
        if (this.fSightCurSpeed > 700F) this.fSightCurSpeed = 700F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
        this.typeDiveBomberAdjVelocityPlus();
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 10F;
        if (this.fSightCurSpeed < 150F) this.fSightCurSpeed = 150F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
        this.typeDiveBomberAdjVelocityMinus();
    }

    public void typeBomberUpdate(float f) {
        if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
            this.fSightCurReadyness -= 0.0666666F * f;
            if (this.fSightCurReadyness < 0.0F) this.fSightCurReadyness = 0.0F;
        }
        if (this.fSightCurReadyness < 1.0F) this.fSightCurReadyness += 0.0333333F * f;
        else if (this.bSightAutomation) {
            this.fSightCurDistance -= this.fSightCurSpeed / 3.6F * f;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / this.fSightCurAltitude));
            if (this.fSightCurDistance < this.fSightCurSpeed / 3.6F * Math.sqrt(this.fSightCurAltitude * (2F / 9.81F))) this.bSightBombDump = true;
            if (this.bSightBombDump) if (this.FM.isTick(3, 0)) {
                if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
                    this.FM.CT.WeaponControl[3] = true;
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                }
            } else this.FM.CT.WeaponControl[3] = false;
        }
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
        this.fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
        this.fSightCurAltitude = this.fDiveRecoveryAlt = netmsginput.readFloat();
        this.fSightCurSpeed = this.fDiveVelocity = netmsginput.readUnsignedByte() * 2.5F;
        this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
        this.fDiveRecoveryAlt += 10F;
        if (this.fDiveRecoveryAlt > 10000F) this.fDiveRecoveryAlt = 10000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
    }

    public void typeDiveBomberAdjAltitudeMinus() {
        this.fDiveRecoveryAlt -= 10F;
        if (this.fDiveRecoveryAlt < 500F) this.fDiveRecoveryAlt = 500F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
        this.fDiveVelocity += 10F;
        if (this.fDiveVelocity > 700F) this.fDiveVelocity = 700F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fDiveVelocity) });
    }

    public void typeDiveBomberAdjVelocityMinus() {
        this.fDiveVelocity -= 10F;
        if (this.fDiveVelocity < 150F) this.fDiveVelocity = 150F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fDiveVelocity) });
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, -65F, 65F, 65F, -65F), 0.0F);
    }

    public static boolean bChangedPit = false;
    public int            diveMechStage;
    public boolean        bNDives;
    private boolean       bDropsBombs;
    private boolean       needsToOpenBombays;
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

    static {
        Class class1 = JU_188beta.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju188A2");
        Property.set(class1, "meshName", "3DO/Plane/Ju-188A-2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-188A-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_188.class, CockpitJU_188_Bombardier.class, CockpitJU_188_NGunner.class, CockpitJU_188_RGunner.class, CockpitJU_188_BGunner.class });
        Property.set(class1, "LOSElevation", 1.0976F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 11, 12, 13, 13, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_BombSpawn01", "_BombSpawn02" });
    }
}
