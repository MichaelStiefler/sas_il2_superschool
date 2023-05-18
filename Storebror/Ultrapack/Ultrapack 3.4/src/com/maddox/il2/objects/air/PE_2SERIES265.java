package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class PE_2SERIES265 extends PE_2NEW implements TypeBomber, TypeDiveBomber, TypeTransport {

    public PE_2SERIES265() {
        this.tme0 = 0L;
        this.tme1 = 0L;
        this.tme2 = 0L;
        this.oldbgunner = 1;
        this.shkas = false;
        this.pilot2kill = false;
        this.pilot2catapult = false;
        this.pilot3catapult = false;
        this.Sturman = false;
    }

    public boolean canOpenBombBay() {
        if (this.BombLoadType == 3) for (int i = 0; i < this.FM.CT.Weapons[3].length; i++)
            if (this.FM.CT.Weapons[3][i].haveBullets() && (this.FM.CT.Weapons[3][i].getHookName().startsWith("_ExternalBomb03") || this.FM.CT.Weapons[3][i].getHookName().startsWith("_ExternalBomb04"))) return false;
        return true;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.turret.length != 0) {
            this.FM.turret[1].bIsOperable = false;
            this.FM.turret[2].bIsOperable = false;
            this.FM.turret[3].bIsOperable = false;
        }
        this.gun3 = this.getGunByHookName("_MGUN03");
        this.gun4 = this.getGunByHookName("_MGUN04");
        if (this.thisWeaponsName.startsWith("default") || this.thisWeaponsName.startsWith("none")) this.BombLoadType = 0;
        else if (this.thisWeaponsName.startsWith("6fab50") || this.thisWeaponsName.startsWith("6fab100")) this.BombLoadType = 1;
        else if (this.thisWeaponsName.endsWith("2fab250") || this.thisWeaponsName.startsWith("2fab500")) this.BombLoadType = 2;
        else if (this.thisWeaponsName.endsWith("2fab2502fab100")) this.BombLoadType = 3;
        else if (this.thisWeaponsName.endsWith("4fab250")) this.BombLoadType = 4;
        else this.BombLoadType = 0;
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("OilLuk1_D0", 0.0F, 0.0F, 9F * this.FM.EI.engines[0].getControlRadiator());
        this.hierMesh().chunkSetAngles("OilLuk2_D0", 0.0F, 0.0F, 9F * this.FM.EI.engines[1].getControlRadiator());
        if (this.FM.AS.astatePlayerIndex == 1 && !this.pilot2catapult) {
            if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[1]) this.setSturman(0);
            else if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[2]) this.setSturman(1);
        } else if (!this.pilot2catapult && Time.current() > this.tme0) {
            this.tme0 = Time.current() + 1000L;
            if (this.FM.turret.length != 0 && !this.pilot2kill) this.setSturman(1);
        }
        if (!this.pilot3catapult) if (this.FM.AS.astatePlayerIndex == 2) {
            if (!this.FM.turret[1].bIsAIControlled) this.setRadist(1);
            else if (!this.FM.turret[2].bIsAIControlled) this.setRadist(20);
            else if (!this.FM.turret[3].bIsAIControlled) this.setRadist(30);
        } else if (Time.current() > this.tme1) {
            this.tme1 = Time.current() + 2000L;
            if (this.FM.turret.length != 0) {
                Actor actor = null;
                for (int i = 1; i < 4; i++)
                    if (this.FM.turret[i].bIsOperable) actor = this.FM.turret[i].target;

                if (actor == null) this.setRadist(0);
                else if (Actor.isValid(actor)) {
                    this.pos.getAbs(Aircraft.tmpLoc2);
                    actor.pos.getAbs(Aircraft.tmpLoc3);
                    Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
                    if (Aircraft.tmpLoc3.getPoint().x < -Math.abs(Aircraft.tmpLoc3.getPoint().y)) this.setRadist(1);
                    else {
                        this.gun3.loadBullets(Math.min(this.gun3.countBullets(), this.gun4.countBullets()));
                        this.gun4.loadBullets(this.gun3.countBullets());
                        if (Aircraft.tmpLoc3.getPoint().y > 0.0D) {
                            if (Time.current() > this.tme2) if (this.oldbgunner == 3 && !this.shkas) {
                                this.tme2 = Time.current() + 10000L;
                                this.shkas = true;
                            } else {
                                this.setRadist(2);
                                this.shkas = false;
                            }
                        } else if (Time.current() > this.tme2) if (this.oldbgunner == 2 && !this.shkas) {
                            this.tme2 = Time.current() + 10000L;
                            this.shkas = true;
                        } else {
                            this.setRadist(3);
                            this.shkas = false;
                        }
                    }
                }
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
        if (this.hierMesh().chunkFindCheck("HMask3b_D0") > 0) if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask3b_D0", false);
        else this.hierMesh().chunkVisible("HMask3b_D0", this.hierMesh().isChunkVisible("Pilot3b_D0"));
        if (this.hierMesh().chunkFindCheck("HMask3c_D0") > 0) if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask3c_D0", false);
        else this.hierMesh().chunkVisible("HMask3c_D0", this.hierMesh().isChunkVisible("Pilot3c_D0"));
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

            case 2:
                if (!this.FM.turret[1].bIsAIControlled) {
                    f = 0.0F;
                    f1 = 0.0F;
                    flag = false;
                    break;
                }
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                break;

            case 3:
                if (!this.FM.turret[1].bIsAIControlled) {
                    f = 0.0F;
                    f1 = 0.0F;
                    flag = false;
                    break;
                }
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
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
        this.hierMesh().chunkVisible("Pilot3b_D0", false);
        this.hierMesh().chunkVisible("Pilot3c_D0", false);
        this.FM.turret[1].bIsOperable = false;
        this.FM.turret[2].bIsOperable = false;
        this.FM.turret[3].bIsOperable = false;
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot3_D0", true);
                this.FM.turret[1].bIsOperable = true;
                this.oldbgunner = 1;
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot3a_D0", true);
                this.FM.turret[1].bIsOperable = true;
                this.oldbgunner = 1;
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3b_D0", true);
                this.FM.turret[2].bIsOperable = true;
                this.hierMesh().chunkVisible("Turret3B_D0", true);
                this.hierMesh().chunkVisible("Turret4B_D0", false);
                this.oldbgunner = 2;
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot3c_D0", true);
                this.FM.turret[3].bIsOperable = true;
                this.hierMesh().chunkVisible("Turret3B_D0", false);
                this.hierMesh().chunkVisible("Turret4B_D0", true);
                this.oldbgunner = 3;
                break;

            case 20:
                this.hierMesh().chunkVisible("Pilot3b_D0", true);
                this.FM.turret[2].bIsOperable = true;
                this.oldbgunner = 2;
                break;

            case 30:
                this.hierMesh().chunkVisible("Pilot3c_D0", true);
                this.FM.turret[3].bIsOperable = true;
                this.oldbgunner = 3;
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
                this.FM.turret[2].setHealth(f);
                this.FM.turret[3].setHealth(f);
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
                this.doRemoveBodyChunkFromPlane("Pilot3b");
                this.doRemoveBodyChunkFromPlane("Head3b");
                this.doRemoveBodyChunkFromPlane("Pilot3c");
                this.doRemoveBodyChunkFromPlane("Head3c");
                this.pilot3catapult = true;
                break;
        }
    }

    private long    tme0;
    private long    tme1;
    private long    tme2;
    private int     oldbgunner;
    private boolean shkas;
    private boolean pilot2kill;
    private boolean pilot2catapult;
    private boolean pilot3catapult;
    private boolean Sturman;
    private Gun     gun3;
    private Gun     gun4;

    static {
        Class class1 = PE_2SERIES265.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Pe-2");
        Property.set(class1, "meshName", "3DO/Plane/Pe-2series265/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Pe-2series359.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitPE2_265.class, CockpitPE2_265_Bombardier.class, CockpitPE2_265_TGunner.class, CockpitPE2_402_BGunner.class, CockpitPE2_402_LGunner.class, CockpitPE2_402_RGunner.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 1, 10, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON02", "_CANNON01", "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03",
                "_BombSpawn04", "_BombSpawn05", "_BombSpawn06" });
    }
}
