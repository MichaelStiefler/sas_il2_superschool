package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.il2.objects.weapons.BombTorpMk13a;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class TBD1A extends Scheme1 implements TypeStormovik, TypeBomber, TypeTwoPitchProp, TypeSeaPlane {

    public TBD1A() {
        this.bombardierDoors = 0.0F;
        this.openDoors = false;
        this.flapps = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            for (int i = 0; i < aobj.length; i++) {
                if (aobj[i] instanceof BombTorpMk13a) {
                    this.FM.AS.disablePilot(1);
                    this.FM.M.massEmpty -= 80F;
                    if (Config.isUSE_RENDER()) {
                        this.hierMesh().chunkVisible("Bombdoor_D0", false);
                        this.hierMesh().chunkVisible("Pilot2_D0", false);
                        this.hierMesh().chunkVisible("Head2_D0", false);
                        this.hierMesh().chunkVisible("HMask2_D0", false);
                    }
                }
            }

        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("HMask2B_D0", false);
            this.hierMesh().chunkVisible("HMask3_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
            this.hierMesh().chunkVisible("HMask2B_D0", this.hierMesh().isChunkVisible("Pilot2B_D0"));
            this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
        }
        if (this.FM instanceof Maneuver) {
            Maneuver maneuver = (Maneuver) this.FM;
            if ((maneuver.get_maneuver() == 43) && (((FlightModelMain) (maneuver)).CT.Weapons[3] != null)) {
                for (int i = 0; i < ((FlightModelMain) (maneuver)).CT.Weapons[3].length; i++) {
                    if ((((FlightModelMain) (maneuver)).CT.Weapons[3][i] != null) && !(((FlightModelMain) (maneuver)).CT.Weapons[3][i] instanceof BombGunNull) && (((FlightModelMain) (maneuver)).CT.Weapons[3][i].countBullets() != 0)) {
                        this.openDoors = true;
                        return;
                    }
                }

            }
            this.openDoors = false;
        }
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        this.hierMesh().chunkVisible("WingRMid_Act1", f > 0.01F);
        this.hierMesh().chunkVisible("WingRMid_Act2", f > 0.01F);
        this.hierMesh().chunkVisible("WingLMid_Act1", f > 0.01F);
        this.hierMesh().chunkVisible("WingLMid_Act2", f > 0.01F);
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, -150F * f, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, -150F * f, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_Act1", 0.0F, -80F * f, 0.0F);
        hiermesh.chunkSetAngles("WingLMid_Act1", 0.0F, -80F * f, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_Act2", 0.0F, -65F * f, 0.0F);
        hiermesh.chunkSetAngles("WingLMid_Act2", 0.0F, -65F * f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        s = s.toLowerCase();
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.equals("xxarmorp1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(10F, 15F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                        if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                        }
                        break;

                    case 2:
                        if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                            this.debuggunnery("Controls: Ailerones Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3:
                        if ((World.Rnd().nextFloat() < 0.95F) && (this.getEnergyPastArmor(4.253F, shot) > 0.0F)) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;

                    case 4:
                        if ((World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        break;

                    case 5:
                        if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                            this.debuggunnery("Controls: Ailerones Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 280000F)) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 100000F)) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(0.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 0.66F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 1000000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("mag1")) {
                    this.debuggunnery("Engine Module: Magneto 1 Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                }
                if (s.endsWith("mag2")) {
                    this.debuggunnery("Engine Module: Magneto 2 Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                }
                if (s.endsWith("eqpt")) {
                    if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        }
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("oil")) {
                    if ((World.Rnd().nextFloat() < 0.6F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                        this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    }
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxgun")) {
                int j = 0;
                byte byte1 = 0;
                if (!s.endsWith("1")) {
                    byte1 = 10;
                }
                if (!s.endsWith("3")) {
                    j = 1;
                }
                if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
                    this.FM.AS.setJamBullets(byte1, j);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                if ((this.getEnergyPastArmor(0.25F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if ((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.5F / (float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z)), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if ((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if ((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if ((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if ((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if ((s.endsWith("lo1") || s.endsWith("lo2")) && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if ((s.endsWith("ro1") || s.endsWith("ro2")) && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if ((s.endsWith("sl1") || s.endsWith("sl2") || s.endsWith("sl3") || s.endsWith("sl4") || s.endsWith("sl5")) && (this.chunkDamageVisible("StabL") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
                }
                if ((s.endsWith("sr1") || s.endsWith("sr2") || s.endsWith("sr3") || s.endsWith("sr4") || s.endsWith("sr5")) && (this.chunkDamageVisible("StabR") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
                }
                if (s.startsWith("xxspark") && (this.chunkDamageVisible("Keel1") > 1) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.10000000149011612D)) && (this.getEnergyPastArmor(3.4000000953674316D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.hitTank(shot.initiator, k, 1);
                    if ((World.Rnd().nextFloat() < 0.02F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.11F))) {
                        this.FM.AS.hitTank(shot.initiator, k, 2);
                    }
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xengine1")) {
            this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl")) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr")) {
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
            if (s.startsWith("xWingLIn") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglmid")) {
                if (this.chunkDamageVisible("WingLMid") < 3) {
                    this.hitChunk("WingLMid", shot);
                }
                if (World.Rnd().nextFloat() < (shot.mass + 0.02F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
            }
            if (s.startsWith("xwingrmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                this.hitChunk("WingRMid", shot);
            }
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel")) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner")) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l;
            if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else {
                l = s.charAt(5) - 49;
            }
            this.hitFlesh(l, shot, byte0);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -135F) {
                    f = -135F;
                }
                if (f > 135F) {
                    f = 135F;
                }
                if (f1 < -69F) {
                    f1 = -69F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                float f2;
                for (f2 = Math.abs(f); f2 > 180F; f2 -= 180F) {

                }
                if (f1 < -AircraftLH.floatindex(Aircraft.cvt(f2, 0.0F, 180F, 0.0F, 36F), af)) {
                    f1 = -AircraftLH.floatindex(Aircraft.cvt(f2, 0.0F, 180F, 0.0F, 36F), af);
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = f * 0.4F;
        this.hierMesh().chunkSetLocate("BlisterHindgeA", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("BlisterHindgeB", 0.0F, 0.0F, Aircraft.cvt(f, 0.25F, 0.75F, 0.0F, 17F));
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void moveWingFold(float f) {
        this.moveWingFold(this.hierMesh(), f);
    }

    protected void moveFlap(float f) {
        float f1 = -50F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2:
                this.FM.turret[0].setHealth(f);
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
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("Pilot2B_D0", false);
                this.hierMesh().chunkVisible("Head2B_D0", false);
                this.hierMesh().chunkVisible("HMask2B_D0", false);
                break;

            case 2:
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Head3_D0", false);
                break;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.FM.Gears.hitCentreGear();
                this.hierMesh().chunkVisible("Wire1_D0", false);
                break;

            case 10:
                this.doWreck("GearR3_D0");
                this.FM.Gears.hitRightGear();
                break;

            case 9:
                this.doWreck("GearL3_D0");
                this.FM.Gears.hitLeftGear();
                break;

            case 11:
                this.hierMesh().chunkVisible("Wire1_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.EI.engines[0].getControlProp() < 0.5F) {
            this.FM.EI.engines[0].setControlProp(0.0F);
        } else {
            this.FM.EI.engines[0].setControlProp(1.0F);
        }
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            for (int i = 0; i <= 12; i++) {
                this.hierMesh().chunkSetAngles("radiator" + i + "_D0", 0.0F, -20F * f1, 0.0F);
            }

        }
        if (this.openDoors && (this.bombardierDoors < 1.0F)) {
            this.bombardierDoors = this.bombardierDoors + (0.15F * f);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Head2_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("Pilot2B_D0", true);
            this.hierMesh().chunkVisible("Head2B_D0", true);
            if (this.FM.getAltitude() > 3000F) {
                this.hierMesh().chunkVisible("HMask2B_D0", true);
            }
        } else if (!this.openDoors && (this.bombardierDoors > 0.0F)) {
            this.bombardierDoors = this.bombardierDoors - (0.15F * f);
            if (this.bombardierDoors < 0.0F) {
                this.bombardierDoors = 0.0F;
                this.hierMesh().chunkVisible("Pilot2_D0", true);
                this.hierMesh().chunkVisible("Head2_D0", true);
                if (this.FM.getAltitude() > 3000F) {
                    this.hierMesh().chunkVisible("HMask2_D0", true);
                }
                this.hierMesh().chunkVisible("Pilot2B_D0", false);
                this.hierMesh().chunkVisible("Head2B_D0", false);
                this.hierMesh().chunkVisible("HMask2B_D0", false);
            }
        }
        this.hierMesh().chunkSetAngles("Blister4_d0", 0.0F, -90F * this.bombardierDoors, 0.0F);
        this.hierMesh().chunkSetAngles("Blister5_d0", 0.0F, -90F * this.bombardierDoors, 0.0F);
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
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
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals("us")) {
            int i = Mission.getMissionDate(true);
            if (i > 0) {
                if (i < 0x128310f) {
                    return "41_";
                }
                if (i < 0x1285563) {
                    return "early42_";
                }
            }
        }
        return "";
    }

    private float   bombardierDoors;
    private boolean openDoors;
    private float   flapps;

    static {
        Class class1 = TBD1A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "TBD");
        Property.set(class1, "meshName", "3DO/Plane/TBD-1A(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "meshName_us", "3DO/Plane/TBD-1A(US)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar01());
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/TBD-1A.fmd:TBD-1A_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTBD1.class, CockpitTBD_TGunner.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN04", "_ExternalBomb00", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalDev00", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06" });
    }
}
