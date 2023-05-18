package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class PE_2SERIES244 extends PE_2NEW implements TypeBomber, TypeDiveBomber, TypeTransport {

    public PE_2SERIES244() {
        this.tme0 = 0L;
        this.tme1 = 0L;
        this.pilot2kill = false;
        this.pilot2catapult = false;
        this.pilot3catapult = false;
        this.Sturman = false;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC99_D0", 0.0F, 75F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 112.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -27F * (float) Math.sin(f * Math.PI), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -170.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 112.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -27F * (float) Math.sin(f * Math.PI), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 170.5F * f, 0.0F);
        float f1 = 0.0F;
        if (f > 0.002F) f1 = Math.max(-f * 1500F, -90F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.833333F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, -0.833333F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, -f1);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, f1);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, -f1);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, f1);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    protected void moveBayDoor(float f) {
        if (this.thisWeaponsName.startsWith("4fab50") || this.thisWeaponsName.startsWith("4fab100")) {
            this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -85F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 85F * f, 0.0F);
        }
    }

    public boolean canOpenBombBay() {
        if (this.BombLoadType == 3) for (int i = 0; i < this.FM.CT.Weapons[3].length; i++)
            if (this.FM.CT.Weapons[3][i].haveBullets() && (this.FM.CT.Weapons[3][i].getHookName().startsWith("_ExternalBomb03") || this.FM.CT.Weapons[3][i].getHookName().startsWith("_ExternalBomb04"))) return false;
        return true;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.turret.length != 0) this.FM.turret[1].bIsOperable = false;
        if (this.thisWeaponsName.startsWith("default") || this.thisWeaponsName.startsWith("none")) this.BombLoadType = 0;
        else if (this.thisWeaponsName.startsWith("4fab50") || this.thisWeaponsName.startsWith("4fab100")) this.BombLoadType = 5;
        else if (this.thisWeaponsName.endsWith("2fab250") || this.thisWeaponsName.startsWith("2fab500")) this.BombLoadType = 2;
        else if (this.thisWeaponsName.endsWith("4fab250") || this.thisWeaponsName.startsWith("2fab2502fab100")) this.BombLoadType = 4;
        else this.BombLoadType = 0;
    }

    public void update(float f) {
        if (this.FM.AS.astatePlayerIndex == 1 && !this.pilot2catapult) {
            if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[1]) this.setSturman(0);
            else if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[2]) this.setSturman(1);
        } else if (!this.pilot2catapult && Time.current() > this.tme0) {
            this.tme0 = Time.current() + 1000L;
            if (this.FM.turret.length != 0 && !this.pilot2kill) this.setSturman(1);
        }
        if (!this.pilot3catapult) if (this.FM.AS.astatePlayerIndex == 2) {
            if (!this.FM.turret[1].bIsAIControlled) this.setRadist(1);
        } else if (Time.current() > this.tme1) {
            this.tme1 = Time.current() + 2000L;
            if (this.FM.turret.length != 0) {
                com.maddox.il2.engine.Actor actor = null;
                if (this.FM.turret[1].bIsOperable) actor = this.FM.turret[1].target;
                if (actor == null) this.setRadist(0);
                else this.setRadist(1);
            }
        }
        super.update(f);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2a_D0", false);
                this.hierMesh().chunkVisible("HMask2a_D0", false);
                if (!this.Sturman) this.hierMesh().chunkVisible("Pilot2_D1", true);
                else this.hierMesh().chunkVisible("Pilot2a_D1", true);
                this.pilot2kill = true;
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3) {
                if (this.FM.AS.astateTankStates[1] < 4 && World.Rnd().nextFloat() < 0.025F) this.FM.AS.hitTank(this, 1, 1);
                if (this.FM.getSpeedKMH() > 200F && World.Rnd().nextFloat() < 0.025F) this.nextDMGLevel("Keel1_D0", 0, this);
                if (this.FM.getSpeedKMH() > 200F && World.Rnd().nextFloat() < 0.025F) this.nextDMGLevel("StabL_D0", 0, this);
                if (World.Rnd().nextFloat() < 0.25F) this.nextDMGLevel("WingLIn_D0", 0, this);
            }
            if (this.FM.AS.astateEngineStates[1] > 3) {
                if (this.FM.AS.astateTankStates[2] < 4 && World.Rnd().nextFloat() < 0.025F) this.FM.AS.hitTank(this, 2, 1);
                if (this.FM.getSpeedKMH() > 200F && World.Rnd().nextFloat() < 0.025F) this.nextDMGLevel("Keel2_D0", 0, this);
                if (this.FM.getSpeedKMH() > 200F && World.Rnd().nextFloat() < 0.025F) this.nextDMGLevel("StabR_D0", 0, this);
                if (World.Rnd().nextFloat() < 0.25F) this.nextDMGLevel("WingRIn_D0", 0, this);
            }
        }
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("HMask2a_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
            this.hierMesh().chunkVisible("HMask2a_D0", this.hierMesh().isChunkVisible("Pilot2a_D0"));
        }
        if (this.hierMesh().chunkFindCheck("HMask3_D0") > 0) if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask3_D0", false);
        else this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
        if (this.hierMesh().chunkFindCheck("HMask3a_D0") > 0) if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask3a_D0", false);
        else this.hierMesh().chunkVisible("HMask3a_D0", this.hierMesh().isChunkVisible("Pilot3a_D0"));
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -110F) {
                    f = -110F;
                    flag = false;
                }
                if (f > 90F) {
                    f = 90F;
                    flag = false;
                }
                if (f1 < -6F) {
                    f1 = -6F;
                    flag = false;
                }
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -25F) {
                    f = -25F;
                    flag = false;
                }
                if (f > 25F) {
                    f = 25F;
                    flag = false;
                }
                if (f1 < -60F) {
                    f1 = -60F;
                    flag = false;
                }
                if (f1 > 2.0F) {
                    f1 = 2.0F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    private void setSturman(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2a_D0", true);
                this.FM.turret[0].bIsOperable = false;
                this.Sturman = true;
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2a_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D0", true);
                this.FM.turret[0].bIsOperable = true;
                this.Sturman = false;
                break;
        }
    }

    private void setRadist(int i) {
        this.hierMesh().chunkVisible("Pilot3_D0", false);
        this.hierMesh().chunkVisible("Pilot3a_D0", false);
        this.FM.turret[1].bIsOperable = false;
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot3_D0", true);
                this.FM.turret[1].bIsOperable = true;
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot3a_D0", true);
                this.FM.turret[1].bIsOperable = true;
                break;
        }
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                break;

            case 2:
                this.FM.turret[1].setHealth(f);
                break;
        }
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 0.2F;
        if (this.fSightCurForwardAngle > 75F) this.fSightCurForwardAngle = 75F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Float(this.fSightCurForwardAngle * 1.0F) });
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.2F;
        if (this.fSightCurForwardAngle < -15F) this.fSightCurForwardAngle = -15F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Float(this.fSightCurForwardAngle * 1.0F) });
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip++;
        if (this.fSightCurSideslip > 45F) this.fSightCurSideslip = 45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip--;
        if (this.fSightCurSideslip < -45F) this.fSightCurSideslip = -45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 6000F) this.fSightCurAltitude = 6000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 300F) this.fSightCurAltitude = 300F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 5F;
        if (this.fSightCurSpeed > 650F) this.fSightCurSpeed = 650F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 5F;
        if (this.fSightCurSpeed < 50F) this.fSightCurSpeed = 50F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        double d = this.fSightCurSpeed / 3.6D * Math.sqrt(this.fSightCurAltitude * 0.203873598D);
        d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
        this.fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / this.fSightCurAltitude));
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
        netmsgguaranted.writeFloat(this.fSightCurForwardAngle);
        netmsgguaranted.writeFloat(this.fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readFloat();
        this.fSightCurSideslip = netmsginput.readFloat();
    }

    public void doRemoveBodyFromPlane(int i) {
        switch (i) {
            case 1:
                super.doRemoveBodyFromPlane(i);
                break;

            case 2:
                super.doRemoveBodyFromPlane(i);
                this.pilot2catapult = true;
                break;

            case 3:
                super.doRemoveBodyFromPlane(i);
                this.pilot3catapult = true;
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xxeng1")) {
            this.hitBoneM82(s, shot, point3d, 0);
            return;
        }
        if (s.startsWith("xxeng2")) {
            this.hitBoneM82(s, shot, point3d, 1);
            return;
        } else {
            super.hitBone(s, shot, point3d);
            return;
        }
    }

    protected void hitBoneM82(String s, Shot shot, Point3d point3d, int i) {
        if (s.endsWith("prop") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) this.FM.EI.engines[i].setKillPropAngleDevice(shot.initiator);
        if (s.endsWith("base")) {
            if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                if (World.Rnd().nextFloat() < shot.power / 140000F) {
                    this.FM.AS.setEngineStuck(shot.initiator, i);
                    Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                }
                if (World.Rnd().nextFloat() < shot.power / 85000F) {
                    this.FM.AS.hitEngine(shot.initiator, i, 2);
                    Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                }
            } else if (World.Rnd().nextFloat() < 0.01F) this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
            else {
                this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.002F);
                Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[i].getReadyness() + "..");
            }
            this.getEnergyPastArmor(12F, shot);
        }
        if (s.endsWith("cyls")) {
            if (this.getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 0.75F) {
                this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
                if (World.Rnd().nextFloat() < shot.power / 48000F) {
                    this.FM.AS.hitEngine(shot.initiator, i, 2);
                    Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                }
            }
            this.getEnergyPastArmor(25F, shot);
        }
        if (s.endsWith("supc")) {
            if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) this.FM.EI.engines[i].setKillCompressor(shot.initiator);
            this.getEnergyPastArmor(2.0F, shot);
        }
        if (s.endsWith("eqpt")) {
            if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                if (Aircraft.Pd.y > 0.0D && Aircraft.Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power) this.FM.EI.engines[i].setMagnetoKnockOut(shot.initiator, 0);
                if (Aircraft.Pd.y < 0.0D && Aircraft.Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power) this.FM.EI.engines[i].setMagnetoKnockOut(shot.initiator, 1);
                if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 4);
                if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 0);
                if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 6);
                if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 1);
            }
            this.getEnergyPastArmor(2.0F, shot);
        }
    }

    private long    tme0;
    private long    tme1;
    private boolean pilot2kill;
    private boolean pilot2catapult;
    private boolean pilot3catapult;
    private boolean Sturman;

    static {
        Class class1 = PE_2SERIES244.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Pe-2");
        Property.set(class1, "meshName", "3DO/Plane/Pe-2series244/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Pe-2series244.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitPE2_244.class, CockpitPE2_244_Bombardier.class, CockpitPE2_244_TGunner.class, CockpitPE2_402_BGunner.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 11, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04" });
    }
}
