package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class KI_15xyz extends Scheme1 implements TypeScout, TypeDiveBomber, TypeStormovik {

    public KI_15xyz() {
        this.pilot2kill = false;
        this.BlisTurOpen = false;
        this.aircraft = null;
        this.bCanopyInitState = false;
    }

    protected void moveFan(float f) {
        int i = 0;
        for (int j = 0; j < this.FM.EI.getNum(); j++) {
            if (this.oldProp[j] < 2) {
                i = Math.abs((int) (this.FM.EI.engines[j].getw() * 0.06F));
                if (i >= 1) i = 1;
                if (i != this.oldProp[j] && this.hierMesh().isChunkVisible(Aircraft.Props[j][this.oldProp[j]])) {
                    this.hierMesh().chunkVisible(Aircraft.Props[j][this.oldProp[j]], false);
                    this.oldProp[j] = i;
                    this.hierMesh().chunkVisible(Aircraft.Props[j][i], true);
                }
            }
            if (i == 0) this.propPos[j] = (this.propPos[j] + 57.3F * this.FM.EI.engines[j].getw() * f) % 360F;
            else {
                float f1 = 57.3F * this.FM.EI.engines[j].getw();
                f1 %= 2880F;
                f1 /= 2880F;
                if (f1 <= 0.5F) f1 *= 2.0F;
                else f1 = f1 * 2.0F - 2.0F;
                f1 *= 1200F;
                this.propPos[j] = (this.propPos[j] + f1 * f) % 360F;
            }
            this.hierMesh().chunkSetAngles(Aircraft.Props[j][0], 0.0F, this.propPos[j], 0.0F);
        }

    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
    }

    public void update(float f) {
        if (this.FM.isPlayers() && this.FM.Sq.squareElevators > 0.0F) {
            if (this.FM.getSpeedKMH() > 380F && this.FM.getSpeedKMH() < 381F) {
                this.FM.SensPitch = 0.2F;
                this.FM.producedAM.y -= 400F * (300F - this.FM.getSpeedKMH());
            }
            if (this.FM.getSpeedKMH() >= 381F) {
                this.FM.SensPitch = 0.3F;
                this.FM.producedAM.y -= 250F * (300F - this.FM.getSpeedKMH());
            } else this.FM.SensPitch = 0.55F;
        }
        if (!this.bCanopyInitState && this.FM.isStationedOnGround() && this.FM.isPlayers()) {
            this.FM.AS.setCockpitDoor(this.FM.actor, 1);
            this.FM.CT.cockpitDoorControl = 1.0F;
            this.bCanopyInitState = true;
            System.out.println("*** Initial canopy state: " + (this.FM.CT.getCockpitDoor() != 1.0F ? "closed" : "open"));
        }
        super.update(f);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void moveFlap(float f) {
        float f1 = -30F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh1, float f1) {
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void rareAction(float f, boolean flag) {
        Aircraft aircraft1 = War.getNearestEnemy(this, 6000F);
        if (!this.pilot2kill && aircraft1 != null && !this.FM.AS.bIsAboutToBailout) {
            this.BlisTurOpen = true;
            this.hierMesh().chunkVisible("Turret1B_D0", true);
            this.hierMesh().chunkVisible("Turret1C_D0", true);
            this.hierMesh().chunkVisible("TurretCG_D0", true);
            this.hierMesh().chunkVisible("Gundown_D0", false);
            this.hierMesh().chunkVisible("Down1C_D0", false);
            this.hierMesh().chunkVisible("DownCG_D0", false);
        }
        if (!this.pilot2kill && aircraft1 == null && !this.FM.AS.bIsAboutToBailout && this.BlisTurOpen) {
            this.BlisTurOpen = false;
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret1C_D0", false);
            this.hierMesh().chunkVisible("TurretCG_D0", false);
            this.hierMesh().chunkVisible("Gundown_D0", true);
            this.hierMesh().chunkVisible("Down1C_D0", true);
            this.hierMesh().chunkVisible("DownCG_D0", true);
        }
        if (flag) for (int i = 1; i < 3; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.64F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (!this.pilot2kill && !this.FM.AS.bIsAboutToBailout && !this.BlisTurOpen && this.FM.Gears.onGround()) {
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.64F);
            this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
        } else {
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.0F);
            this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        this.hierMesh().chunkVisible("Pilot1_D0", false);
        this.hierMesh().chunkVisible("Head1_D0", false);
        this.hierMesh().chunkVisible("HMask1_D0", false);
        this.hierMesh().chunkVisible("Pilot1_D1", false);
        this.hierMesh().chunkVisible("Pilot2_D0", false);
        this.hierMesh().chunkVisible("HMask2_D0", false);
        this.hierMesh().chunkVisible("Turret1C_D0", false);
        this.hierMesh().chunkVisible("TurretCG_D0", false);
        this.hierMesh().chunkVisible("Gundown_D0", false);
        this.hierMesh().chunkVisible("Down1C_D0", false);
        this.hierMesh().chunkVisible("DownCG_D0", false);
        this.hierMesh().chunkVisible("Turret1B_D0", true);
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
                this.FM.turret[0].bIsOperable = false;
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("Turret1B_D0", true);
                this.hierMesh().chunkVisible("Turret1C_D0", true);
                this.hierMesh().chunkVisible("TurretCG_D0", true);
                this.hierMesh().chunkVisible("Gundown_D0", false);
                this.hierMesh().chunkVisible("Down1C_D0", false);
                this.hierMesh().chunkVisible("DownCG_D0", false);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxcontrols")) {
                if ((s.endsWith("1") || s.endsWith("2")) && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                }
                if (s.endsWith("3") && this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                }
                if (s.endsWith("4") && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                }
                if (s.endsWith("5") && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 140000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 85000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.01F) this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    else {
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.002F);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.75F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 48000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.startsWith("xxeng1mag")) {
                    int i = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                }
                if (s.endsWith("oil1")) {
                    if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.endsWith("lm1") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D2", shot.initiator);
                }
                if (s.endsWith("rm1") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D2", shot.initiator);
                }
                if (s.endsWith("lo1") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
                }
                if (s.endsWith("ro1") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
                }
                if ((s.endsWith("sl1") || s.endsWith("sl2")) && this.chunkDamageVisible("StabL") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if ((s.endsWith("sr1") || s.endsWith("sr2")) && this.chunkDamageVisible("StabR") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if (s.startsWith("xxspark") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 48;
                int k = 0;
                switch (j) {
                    case 1:
                        k = World.Rnd().nextInt(0, 1);
                        break;

                    case 2:
                        k = World.Rnd().nextInt(2, 3);
                        break;

                    case 3:
                        k = World.Rnd().nextInt(1, 2);
                        break;
                }
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitTank(shot.initiator, k, 1);
                    if (World.Rnd().nextFloat() < 0.05F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F) this.FM.AS.hitTank(shot.initiator, k, 2);
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.aircraft instanceof KI_15_I && s.startsWith("xcf")) this.hitChunk("CF", shot);
            if (this.aircraft instanceof KI_15_II && s.startsWith("xcf")) this.hitChunk("CF1", shot);
        } else if (!s.startsWith("xblister")) if (s.startsWith("xeng")) this.hitChunk("Engine1", shot);
        else if (s.startsWith("xtail")) this.hitChunk("Tail1", shot);
        else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xflap")) {
            if (this.chunkDamageVisible("Flap01") < 2) this.hitChunk("Flap1", shot);
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
            if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
            if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xgear")) {
            if (s.startsWith("xgearl") && this.chunkDamageVisible("GearL2") < 2) this.hitChunk("VatorL", shot);
            if (s.startsWith("xgearr") && this.chunkDamageVisible("GearR2") < 2) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xWingLMid")) {
                if (this.chunkDamageVisible("WingLMid") < 2) this.hitChunk("WingLMid", shot);
                if (World.Rnd().nextFloat() < shot.mass + 0.02F) this.FM.AS.hitOil(shot.initiator, 0);
            }
            if (s.startsWith("xWingRMid") && this.chunkDamageVisible("WingRMid") < 2) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xWingLOut") && this.chunkDamageVisible("WingLOut") < 2) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xWingROut") && this.chunkDamageVisible("WingROut") < 2) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xnaca")) {
            if (this.aircraft instanceof KI_15_I && s.startsWith("xnaca") && this.chunkDamageVisible("Naca") < 2) this.hitChunk("Naca", shot);
            if (this.aircraft instanceof KI_15_II && s.startsWith("xnaca") && this.chunkDamageVisible("Naca1") < 2) this.hitChunk("Naca1", shot);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l;
            if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else l = s.charAt(5) - 49;
            this.hitFlesh(l, shot, byte0);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -19F) {
                    f = -19F;
                    flag = false;
                }
                if (f > 19F) {
                    f = 19F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 35F) {
                    f1 = 35F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
    }

    public void typeDiveBomberAdjAltitudeMinus() {
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
    }

    public static boolean bChangedPit = false;
    private boolean       pilot2kill;
    private boolean       BlisTurOpen;
    public Aircraft       aircraft;
    private boolean       bCanopyInitState;

    static {
        Class class1 = KI_15xyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
