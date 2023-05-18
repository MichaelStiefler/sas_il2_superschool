package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class JU_86 extends Scheme2 implements TypeBomber, TypeTransport {

    public JU_86() {
        this.bGunUp = false;
        this.btme = -1L;
        this.fGunPos = 0.0F;
        this.bChangedExts = false;
        bChangedPit = true;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 250F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightSetForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
    }

    public void update(float f) {
        super.update(f);
        if (!this.bGunUp) {
            if (this.fGunPos > 0.0F) {
                this.fGunPos -= 0.2F * f;
                this.FM.turret[2].bIsOperable = false;
            }
        } else if (this.fGunPos < 1.0F) {
            this.fGunPos += 0.2F * f;
            if (this.fGunPos > 0.8F && this.fGunPos < 0.9F) this.FM.turret[2].bIsOperable = true;
        }
        if (this.fGunPos < 0.6F) {
            this.resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(this.fGunPos, 0.0F, 0.6F, 0.0F, 0.58F);
            this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (this.FM.turret[2].bIsAIControlled) {
            if (this.FM.turret[2].target != null && this.FM.AS.astatePilotStates[2] < 90) this.bGunUp = true;
            if (Time.current() > this.btme) {
                this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                if (this.FM.turret[2].target == null && this.FM.AS.astatePilotStates[2] < 90) this.bGunUp = false;
            }
        }
        if (this.FM.isPlayers() && this.FM.Sq.squareElevators > 0.0F) {
            if (this.FM.getSpeedKMH() > 350F && this.FM.getSpeedKMH() < 400F) {
                this.FM.SensPitch = 0.25F;
                this.FM.producedAM.y -= 300F * (200F - this.FM.getSpeedKMH());
            }
            if (this.FM.getSpeedKMH() >= 401F) {
                this.FM.SensPitch = 0.21F;
                this.FM.producedAM.y -= 150F * (200F - this.FM.getSpeedKMH());
            } else this.FM.SensPitch = 0.55F;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -135F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 135F * f, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -25F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 25F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("FlapL_D0", 0.0F, 0.0F, 40F * f);
        this.hierMesh().chunkSetAngles("FlapR_D0", 0.0F, 0.0F, 40F * f);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("BayL_D0", 0.0F, 90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayR_D0", 0.0F, -90F * f, 0.0F);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
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

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;

            case 2:
                this.FM.turret[1].bIsOperable = false;
                break;

            case 3:
                this.FM.turret[2].bIsOperable = false;
                break;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
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

            case 1:
                if (f < -75F) {
                    f = -75F;
                    flag = false;
                }
                if (f > 75F) {
                    f = 75F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -60F) {
                    f = -60F;
                    flag = false;
                }
                if (f > 60F) {
                    f = 60F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
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

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
            this.mydebuggunnery("*** Bomb Payload Detonates..");
            this.FM.AS.hitTank(shot.initiator, 0, 100);
            this.FM.AS.hitTank(shot.initiator, 1, 100);
            this.FM.AS.hitTank(shot.initiator, 2, 100);
            this.FM.AS.hitTank(shot.initiator, 3, 100);
            this.msgCollision(this, "CF_D0", "CF_D0");
        }
        if (s.startsWith("xxspar")) {
            this.getEnergyPastArmor(1.0F, shot);
            if ((s.endsWith("tail1") || s.endsWith("tail2")) && World.Rnd().nextFloat() < 0.1F && this.chunkDamageVisible("Tail1") > 2
                    && this.getEnergyPastArmor(15.9F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
                this.mydebuggunnery("*** Tail1 Spars Broken in Half..");
                this.msgCollision(this, "Tail1_D0", "Tail1_D0");
            }
        }
        if (s.startsWith("xxcontrols")) {
            int i = s.charAt(10) - 48;
            switch (i) {
                default:
                    break;

                case 1:
                case 2:
                    if (this.getEnergyPastArmor(1.0F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < 0.08F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.mydebuggunnery("Evelator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.08F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.mydebuggunnery("Rudder Controls Out..");
                        }
                    }
                    break;

                case 3:
                case 4:
                    if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        this.mydebuggunnery("Ailerons Controls Out..");
                    }
                    break;
            }
        }
        if (s.startsWith("xx")) {
            if (s.startsWith("xxeng")) {
                int i = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(1.7F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(20000F, 140000F) < shot.power) this.FM.AS.setEngineStuck(shot.initiator, i);
                        if (World.Rnd().nextFloat(10000F, 50000F) < shot.power) this.FM.AS.hitEngine(shot.initiator, i, 2);
                    } else if (World.Rnd().nextFloat() < 0.04F) this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    else this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.02F);
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 0.9878F) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        if (World.Rnd().nextFloat() < shot.power / 48000F) this.FM.AS.hitEngine(shot.initiator, 0, 2);
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("oil1") && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.FM.AS.hitOil(shot.initiator, 0);
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.4F) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if (World.Rnd().nextFloat() < 0.003F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.2F) this.FM.AS.hitTank(shot.initiator, j, 4);
                }
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) this.hitChunk("CF", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 2) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) this.hitChunk("Keel2", shot);
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 2) this.hitChunk("Rudder2", shot);
        } else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 2) this.hitChunk("WingLIn", shot);
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 2) this.hitChunk("WingRIn", shot);
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 2) this.hitChunk("WingLOut", shot);
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 2) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 2) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneR") < 2) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xflap")) {
            if (s.startsWith("xflapl") && this.chunkDamageVisible("FlapL") < 2) this.hitChunk("FlapL", shot);
            if (s.startsWith("xflapr") && this.chunkDamageVisible("FlapR") < 2) this.hitChunk("FlapR", shot);
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
            if (this.getEnergyPastArmor(0.2F, shot) > 800F) {
                this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 70000F));
                this.FM.AS.hitOil(shot.initiator, 0);
                this.mydebuggunnery("*** Engine1 Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + ".. State = " + this.FM.AS.astateEngineStates[0]);
                if (this.FM.EI.engines[0].getReadyness() < 0.85F && this.FM.AS.astateEngineStates[0] == 0) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                }
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
            if (this.getEnergyPastArmor(0.2F, shot) > 800F) {
                this.FM.EI.engines[1].setReadyness(shot.initiator, this.FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 70000F));
                this.FM.AS.hitOil(shot.initiator, 1);
                this.mydebuggunnery("*** Engine2 Hit - Readyness Reduced to " + this.FM.EI.engines[1].getReadyness() + ".. State = " + this.FM.AS.astateEngineStates[1]);
                if (this.FM.EI.engines[1].getReadyness() < 0.85F && this.FM.AS.astateEngineStates[1] == 0) {
                    this.FM.AS.hitEngine(shot.initiator, 1, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 1, 1);
                }
            }
        } else if (s.startsWith("xturret")) {
            if (s.startsWith("xturret1")) this.FM.AS.setJamBullets(10, 0);
            if (s.startsWith("xturret2")) this.FM.AS.setJamBullets(11, 0);
            if (s.startsWith("xturret3")) this.FM.AS.setJamBullets(12, 0);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k;
            if (s.endsWith("a")) {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else k = s.charAt(5) - 49;
            this.hitFlesh(k, shot, byte0);
        }
    }

    protected void mydebuggunnery(String s1) {
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 5; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

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
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.2F;
        if (this.fSightCurForwardAngle < -15F) this.fSightCurForwardAngle = -15F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
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

    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurForwardAngle;
    public float          fSightSetForwardAngle;
    public float          fSightCurSideslip;
    public boolean        bChangedExts;
    public static boolean bChangedPit = false;
    boolean               bGunUp;
    public long           btme;
    public float          fGunPos;

    static {
        Class class1 = JU_86.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
