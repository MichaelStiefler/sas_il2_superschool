package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class SB2Uxyz extends Scheme1 implements TypeStormovik, TypeDiveBomber {

    public SB2Uxyz() {
        this.arrestor = 0.0F;
        this.bGunUp = false;
        this.btme = -1L;
        this.fGunPos = 0.0F;
        this.bWingInitState = false;
        this.bCanopyInitState = false;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("LL_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, 85F * f);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, 85F * f);
    }

    protected void moveGear(float f) {
        SB2Uxyz.moveGear(this.hierMesh(), f);
    }

    public void update(float f) {
        super.update(f);
        if (!this.bGunUp) {
            if (this.fGunPos > 0.0F) {
                this.fGunPos -= 0.2F * f;
                this.FM.turret[0].bIsOperable = false;
                this.hierMesh().chunkVisible("Turret1B_D0", false);
                this.hierMesh().chunkVisible("Turdown", true);
            }
        } else if (this.fGunPos < 1.0F) {
            this.fGunPos += 0.2F * f;
            if ((this.fGunPos > 0.8F) && (this.fGunPos < 0.9F)) {
                this.FM.turret[0].bIsOperable = true;
            }
            this.hierMesh().chunkVisible("Turret1B_D0", true);
            this.hierMesh().chunkVisible("Turdown", false);
        }
        if (this.fGunPos < 0.6F) {
            this.resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(this.fGunPos, 0.0F, 0.6F, 0.0F, -0.6F);
            this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
        } else {
            this.hierMesh().chunkSetAngles("Blister3_D0", 0.0F, Aircraft.cvt(this.fGunPos, 0.6F, 1.0F, 41F, 27F), 0.0F);
        }
        if (this.FM.turret[0].bIsAIControlled) {
            if ((this.FM.turret[0].target != null) && (this.FM.AS.astatePilotStates[2] < 90)) {
                this.bGunUp = true;
            }
            if (Time.current() > this.btme) {
                this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                if ((this.FM.turret[0].target == null) && (this.FM.AS.astatePilotStates[2] < 90)) {
                    this.bGunUp = false;
                }
            }
        }
        if (this.FM.CT.getArrestor() > 0.001F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -73.5F, 3.5F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = -1.224F * this.FM.Gears.arrestorVSink;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
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
                } else if (this.arrestor > this.FM.CT.getArrestor()) {
                    this.arrestor = this.FM.CT.getArrestor();
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
        if (this.FM.isPlayers() && (this.FM.Sq.squareElevators > 0.0F)) {
            RealFlightModel realflightmodel = (RealFlightModel) this.FM;
            if (realflightmodel.RealMode && (realflightmodel.indSpeed > 120F)) {
                float f5 = 1.0F + (0.005F * (120F - realflightmodel.indSpeed));
                if (f5 < 0.0F) {
                    f5 = 0.0F;
                }
                this.FM.SensPitch = 0.45F * f5;
                if (realflightmodel.indSpeed > 120F) {
                    this.FM.producedAM.y -= 1720F * (120F - realflightmodel.indSpeed);
                }
            } else {
                this.FM.SensPitch = 0.62F;
            }
        }
        if (!this.bWingInitState && this.FM.isStationedOnGround() && this.FM.isPlayers()) {
            this.FM.AS.setWingFold(this.FM.actor, 1);
            this.FM.CT.wingControl = 1.0F;
            this.bWingInitState = true;
        }
        if (!this.bCanopyInitState && this.FM.isStationedOnGround() && this.FM.isPlayers()) {
            this.FM.AS.setCockpitDoor(this.FM.actor, 1);
            this.FM.CT.cockpitDoorControl = 1.0F;
            this.bCanopyInitState = true;
        }
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 100F), 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -100F), 0.0F);
    }

    public void moveWingFold(float f) {
        this.moveWingFold(this.hierMesh(), f);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.58F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
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
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap_D0", 0.0F, 0.0F, 45F * f);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook_D0", 0.0F, 0.0F, 75F * this.arrestor);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
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
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 140000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 85000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    } else if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    } else {
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.002F);
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(6.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 0.75F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                }
                if (s.endsWith("oil1") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int i = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitTank(shot.initiator, i, 1);
                    if ((World.Rnd().nextFloat() < 0.05F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.6F))) {
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                    }
                }
                return;
            }
        }
        if (s.startsWith("xxcontrols")) {
            this.debuggunnery("Controls: Hit..");
            int i = s.charAt(10) - 48;
            switch (i) {
                default:
                    break;

                case 3:
                    if ((this.getEnergyPastArmor(4.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                        this.debuggunnery("Controls: Elevator Controls: Disabled..");
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if ((this.getEnergyPastArmor(0.002F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.11F)) {
                        this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;

                case 4:
                    if ((this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.12F)) {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        this.debuggunnery("Ailerons Controls Out..");
                    }
                    break;

                case 5:
                    if (this.getEnergyPastArmor(0.1F, shot) <= 0.0F) {
                        break;
                    }
                    if (World.Rnd().nextFloat() < 0.25F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        this.debuggunnery("*** Engine1 Throttle Controls Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.15F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        this.debuggunnery("*** Engine1 Prop Controls Out..");
                    }
                    break;
            }
            return;
        }
        if (s.startsWith("xxlock")) {
            this.debuggunnery("Lock Construction: Hit..");
            if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
            }
            if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
            }
            if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
            }
            if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
            }
            if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
            }
            return;
        }
        if (s.startsWith("xxoil1")) {
            if ((this.getEnergyPastArmor(0.25F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                this.FM.AS.hitOil(shot.initiator, 0);
                this.getEnergyPastArmor(0.22F, shot);
                this.debuggunnery("Engine Module: Oil Radiator 1 Pierced..");
            }
            return;
        }
        if (s.startsWith("xxspar")) {
            if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                this.debuggunnery("*** WingLIn Spars Damaged..");
                this.nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
            }
            if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                this.debuggunnery("*** WingRIn Spars Damaged..");
                this.nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
            }
            if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                this.debuggunnery("*** WingLOut Spars Damaged..");
                this.nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
            }
            if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                this.debuggunnery("*** WingROut Spars Damaged..");
                this.nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
            }
            if (s.startsWith("xxspark") && (this.chunkDamageVisible("Keel1") > 1) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                this.debuggunnery("*** Keel1 Spars Damaged..");
                this.nextDMGLevels(1, 2, "Keel1_D2" + this.chunkDamageVisible("Keel1"), shot.initiator);
            }
            if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 1) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                this.debuggunnery("*** Keel1 Spars Damaged..");
                this.nextDMGLevels(1, 2, "Tail1_D2" + this.chunkDamageVisible("Taill1"), shot.initiator);
            }
            if (s.startsWith("xxsparsl") && (this.chunkDamageVisible("StabL") > 1) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                this.debuggunnery("*** StabL Spars Damaged..");
                this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
            }
            if (s.startsWith("xxsparsr") && (this.chunkDamageVisible("StabR") > 1) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                this.debuggunnery("*** StabR Spars Damaged..");
                this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
            }
            return;
        }
        if (s.startsWith("xxmgun")) {
            if (s.endsWith("1")) {
                this.debuggunnery("Cowling Gun: Disabled..");
                this.FM.AS.setJamBullets(0, 0);
            }
            if (s.endsWith("2")) {
                this.debuggunnery("Cowling Gun: Disabled..");
                this.FM.AS.setJamBullets(0, 1);
            }
            return;
        }
        if (s.startsWith("xcf1")) {
            if (this.chunkDamageVisible("CF") < 2) {
                this.hitChunk("CF", shot);
            }
            if (World.Rnd().nextFloat() < 0.19F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            }
            if (World.Rnd().nextFloat() < 0.19F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            }
        } else if (s.startsWith("xcf2")) {
            if (this.chunkDamageVisible("Back") < 2) {
                this.hitChunk("Back", shot);
            }
            if (World.Rnd().nextFloat() < 0.19F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            }
            if (World.Rnd().nextFloat() < 0.19F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            }
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xnaca")) {
            if (this.chunkDamageVisible("Naca") < 2) {
                this.hitChunk("Naca", shot);
            }
        } else if (s.startsWith("xflaps")) {
            if (this.chunkDamageVisible("Flaps") < 2) {
                this.hitChunk("Flaps", shot);
            }
        } else if (s.startsWith("xblock1")) {
            if (this.chunkDamageVisible("Block1") < 2) {
                this.hitChunk("Block1", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 2) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 2)) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 2)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 2)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 2)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 2)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 2)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 2)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 2)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 2)) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j;
            if (s.endsWith("a")) {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else {
                j = s.charAt(5) - 49;
            }
            this.hitFlesh(j, shot, byte0);
        }
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

    boolean         bGunUp;
    public long     btme;
    public float    fGunPos;
    private float   arrestor;
    private boolean bWingInitState;
    private boolean bCanopyInitState;

    static {
        Class class1 = SB2Uxyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
