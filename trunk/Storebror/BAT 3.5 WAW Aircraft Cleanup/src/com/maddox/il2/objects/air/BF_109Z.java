package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class BF_109Z extends Scheme2 implements TypeFighter {

    public BF_109Z() {
        this.cockpitDoor_ = 0.0F;
        this.fMaxKMHSpeedForOpenCanopy = 250F;
        this.kangle = 0.0F;
        this.bHasBlister = true;
        this.flapps = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        float f3 = Aircraft.cvt(f, 0.05F, 0.49F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f3, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", -33.5F * f3, 0.0F, 0.0F);
        f3 = Aircraft.cvt(f, 0.12F, 0.95F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 77.5F * f3, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 33.5F * f3, 0.0F, 0.0F);
        f3 = Aircraft.cvt(f1, 0.3F, 0.82F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f3, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 33.5F * f3, 0.0F, 0.0F);
        f3 = Aircraft.cvt(f1, 0.34F, 0.78F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -77.5F * f3, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", -33.5F * f3, 0.0F, 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        moveGear(this.hierMesh(), f, f1, f2);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30F * f, 0.0F);
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("GearL6_D0", 0.0F, -((FlightModelMain) (super.FM)).Gears.gWheelAngles[0], 0.0F);
        this.hierMesh().chunkSetAngles("GearR6_D0", 0.0F, -((FlightModelMain) (super.FM)).Gears.gWheelAngles[1], 0.0F);
        this.hierMesh().chunkSetAngles("GearC4_D0", 0.0F, -((FlightModelMain) (super.FM)).Gears.gWheelAngles[2], 0.0F);
        if (super.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
        }
        if (Math.abs(this.flapps - this.kangle) > 0.01F) {
            this.flapps = this.kangle;
            this.hierMesh().chunkSetAngles("Flap01L_D0", 0.0F, -20F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap01U_D0", 0.0F, 20F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap02L_D0", 0.0F, -20F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap02U_D0", 0.0F, 20F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap01L2_D0", 0.0F, -20F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap01U2_D0", 0.0F, 20F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap02L2_D0", 0.0F, -20F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap02U2_D0", 0.0F, 20F * this.kangle, 0.0F);
        }
        this.kangle = (0.95F * this.kangle) + (0.05F * ((FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator());
        if (this.kangle > 1.0F) {
            this.kangle = 1.0F;
        }
        super.update(f);
        this.CombustionFlame();
        if ((((FlightModelMain) (super.FM)).CT.getCockpitDoor() > 0.20000000000000001D) && this.bHasBlister && (super.FM.getSpeedKMH() > this.fMaxKMHSpeedForOpenCanopy) && (this.hierMesh().chunkFindCheck("Blister1_D0") != -1)) {
            try {
                if (this == World.getPlayerAircraft()) {
                    ((CockpitBF_109Z) Main3D.cur3D().cockpitCur).removeCanopy();
                }
            } catch (Exception exception) {
            }
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(((FlightModelMain) (super.FM)).Vwld);
            wreckage.setSpeed(vector3d);
            this.bHasBlister = false;
            ((FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = false;
            super.FM.setGCenter(-0.5F);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (((FlightModelMain) (super.FM)).AS.astateTankStates[2] > 5) {
                ((FlightModelMain) (super.FM)).AS.repairTank(2);
            }
            if (((FlightModelMain) (super.FM)).AS.astateTankStates[3] > 5) {
                ((FlightModelMain) (super.FM)).AS.repairTank(3);
            }
        }
        if (super.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
        }
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 5: // '\005'
            case 6: // '\006'
            case 7: // '\007'
            case 8: // '\b'
            case 11: // '\013'
            case 12: // '\f'
            case 14: // '\016'
            case 15: // '\017'
            case 16: // '\020'
            case 17: // '\021'
            case 21: // '\025'
            case 22: // '\026'
            case 23: // '\027'
            case 24: // '\030'
            case 25: // '\031'
            case 26: // '\032'
            case 27: // '\033'
            case 28: // '\034'
            case 29: // '\035'
            case 30: // '\036'
            case 31: // '\037'
            case 32: // ' '
            case 33: // '!'
            case 34: // '"'
            case 35: // '#'
            default:
                break;

            case 3: // '\003'
            case 4: // '\004'
                return false;

            case 18: // '\022'
                if (World.Rnd().nextFloat() >= 0.5F) {
                    break;
                }
                this.cut("Keel2");
                if (World.Rnd().nextFloat() < 0.5F) {
                    this.cut("Tail2");
                }
                break;

            case 13: // '\r'
                this.cut("WingRIn");
                this.cut("Engine2");
                this.cut("Tail2");
                super.FM.cut(36, j, actor);
                super.FM.cut(4, j, actor);
                super.FM.cut(20, j, actor);
                if (World.Rnd().nextFloat() < 0.5F) {
                    this.cut("StabR");
                    super.FM.cut(18, j, actor);
                    super.FM.cut(17, j, actor);
                }
                break;

            case 19: // '\023'
                this.cut("StabR");
                super.FM.cut(18, j, actor);
                super.FM.cut(17, j, actor);
                break;

            case 20: // '\024'
                this.cut("StabR");
                super.FM.cut(18, j, actor);
                super.FM.cut(17, j, actor);
                break;

            case 36: // '$'
                this.cut("Engine2");
                this.cut("Nose");
                this.cut("Tail2");
                super.FM.cut(4, j, actor);
                super.FM.cut(13, j, actor);
                super.FM.cut(20, j, actor);
                break;

            case 9: // '\t'
                this.cut("GearL4");
                break;

            case 10: // '\n'
                this.cut("GearR4");
                break;
        }
        return super.cutFM(i, j, actor);
    }

    private void reflectGlassState(int i) {
        this.GlassState |= i;
        switch (this.GlassState & 3) {
            case 1: // '\001'
                this.hierMesh().materialReplace("Glass2", "ZBulletsHoles");
                break;

            case 2: // '\002'
                this.hierMesh().materialReplace("Glass2", "GlassOil");
                break;

            case 3: // '\003'
                this.hierMesh().materialReplace("Glass2", "GlassOilHoles");
                break;
        }
        switch (this.GlassState & 0xc) {
            case 4: // '\004'
                this.hierMesh().materialReplace("GlassW", "ZBulletsHoles");
                break;

            case 8: // '\b'
                this.hierMesh().materialReplace("GlassW", "Wounded");
                this.hierMesh().chunkVisible("Gore2_D0", true);
                break;

            case 12: // '\f'
                this.hierMesh().materialReplace("GlassW", "WoundedHoles");
                this.hierMesh().chunkVisible("Gore2_D0", true);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                Aircraft.debugprintln(this, "*** Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F), shot);
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
                    this.reflectGlassState(5);
                    Aircraft.debugprintln(this, "*** Armor Glass: Hit..");
                    if (shot.power <= 0.0F) {
                        Aircraft.debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.doRicochetBack(shot);
                        }
                    }
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(0.5F, shot);
                } else if (s.endsWith("p3")) {
                    if (((Tuple3d) (point3d)).z < -0.27000000000000002D) {
                        this.getEnergyPastArmor(4.0999999046325684D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-006D), shot);
                    } else {
                        this.getEnergyPastArmor(8.1000003814697266D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-006D), shot);
                    }
                } else if (s.endsWith("p4")) {
                    this.getEnergyPastArmor(10.100000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-006D), shot);
                } else if (s.endsWith("p5")) {
                    this.getEnergyPastArmor(10.100000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-006D), shot);
                } else if (s.endsWith("p6")) {
                    this.getEnergyPastArmor(12.100000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-006D), shot);
                } else if (s.endsWith("a1")) {
                    if (World.Rnd().nextFloat() < 0.5F) {
                        shot.powerType = 0;
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(5F, 7F), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                if (s.length() > 11) {
                    i = 10 + (s.charAt(11) - 48);
                }
                switch (i) {
                    default:
                        break;

                    case 1: // '\001'
                    case 4: // '\004'
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                        }
                        break;

                    case 2: // '\002'
                    case 3: // '\003'
                    case 8: // '\b'
                        if ((this.getEnergyPastArmor(0.12F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.1F)) {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls: Disabled..");
                        }
                        break;

                    case 5: // '\005'
                    case 6: // '\006'
                    case 10: // '\n'
                    case 11: // '\013'
                        if ((this.getEnergyPastArmor(0.002F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.1F)) {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Elevator Controls: Disabled / Strings Broken..");
                        }
                        break;

                    case 7: // '\007'
                    case 9: // '\t'
                        if ((this.getEnergyPastArmor(2.3F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.2F)) {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                            Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                        }
                        break;

                    case 12: // '\f'
                        if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
                            Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 13: // '\r'
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 8);
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart1") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.5F / (float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z)), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if (s.startsWith("xxspart2") && (this.chunkDamageVisible("Tail2") > 2) && (this.getEnergyPastArmor(3.5F / (float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z)), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Tail2 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail2_D3", shot.initiator);
                }
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxwj")) {
                if (this.getEnergyPastArmor(12.5F, shot) > 0.0F) {
                    if (s.endsWith("l1")) {
                        Aircraft.debugprintln(this, "*** WingL Console Lock Destroyed..");
                        this.nextDMGLevels(4, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                    }
                    if (s.endsWith("l2") || s.endsWith("r2")) {
                        Aircraft.debugprintln(this, "*** WingR Console Lock Destroyed..");
                        this.nextDMGLevels(4, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                    }
                    if (s.endsWith("r1")) {
                        Aircraft.debugprintln(this, "*** WingR Outer Console Lock Destroyed..");
                        this.nextDMGLevels(4, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                    }
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr1") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockr2") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder2 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int j = s.charAt(5) - 49;
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("pipe")) {
                    if ((World.Rnd().nextFloat() < 0.1F) && (((FlightModelMain) (super.FM)).CT.Weapons[0] != null)) {
                        ((FlightModelMain) (super.FM)).AS.setJamBullets(0, j);
                        Aircraft.debugprintln(this, "*** Engine" + j + ": Nose Nozzle Pipe Bent..");
                    }
                    this.getEnergyPastArmor(0.3F, shot);
                } else if (s.endsWith("prop")) {
                    if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.8F)) {
                        if (World.Rnd().nextFloat() < 0.5F) {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, j, 3);
                            Aircraft.debugprintln(this, "*** Engine" + j + ": Prop Governor Hit, Disabled..");
                        } else {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, j, 4);
                            Aircraft.debugprintln(this, "*** Engine" + j + ": Prop Governor Hit, Damaged..");
                        }
                    }
                } else if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < 0.5F) {
                            ((FlightModelMain) (super.FM)).EI.engines[j].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine" + j + ": Bullet Jams Reductor Gear..");
                        } else {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, j, 3);
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, j, 4);
                            Aircraft.debugprintln(this, "*** Engine" + j + ": Reductor Gear Damaged, Prop Governor Failed..");
                        }
                    }
                } else if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        Aircraft.debugprintln(this, "*** Engine" + j + ": Supercharger Disabled..");
                    }
                } else if (s.endsWith("feed")) {
                    if ((this.getEnergyPastArmor(3.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F) && (((FlightModelMain) (super.FM)).EI.engines[j].getPowerOutput() > 0.7F)) {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, j, 100);
                        Aircraft.debugprintln(this, "*** Engine" + j + ": Pressurized Fuel Line Pierced, Fuel Flamed..");
                    }
                } else if (s.endsWith("fuel")) {
                    if (this.getEnergyPastArmor(1.1F, shot) > 0.0F) {
                        ((FlightModelMain) (super.FM)).EI.engines[j].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + j + ": Fuel Line Stalled, Engine Stalled..");
                    }
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, j);
                            Aircraft.debugprintln(this, "*** Engine" + j + ": Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, j, 2);
                            Aircraft.debugprintln(this, "*** Engine" + j + ": Crank Case Hit, Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[j].getReadyness() + "..");
                        }
                        ((FlightModelMain) (super.FM)).EI.engines[j].setReadyness(shot.initiator, ((FlightModelMain) (super.FM)).EI.engines[j].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine" + j + ": Crank Case Hit, Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[j].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(22.5F, shot);
                } else if (s.endsWith("cyl1") || s.endsWith("cyl2")) {
                    if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < (((FlightModelMain) (super.FM)).EI.engines[j].getCylindersRatio() * 1.75F))) {
                        ((FlightModelMain) (super.FM)).EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine" + j + ": Cylinders Hit, " + ((FlightModelMain) (super.FM)).EI.engines[j].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[j].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, j, 3);
                            Aircraft.debugprintln(this, "*** Engine" + j + ": Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.01F) {
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, j);
                            Aircraft.debugprintln(this, "*** Engine" + j + ": Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                } else if (s.endsWith("mag1") || s.endsWith("mag2")) {
                    int j1 = s.charAt(9) - 49;
                    ((FlightModelMain) (super.FM)).EI.engines[j].setMagnetoKnockOut(shot.initiator, j1);
                    Aircraft.debugprintln(this, "*** Engine" + j + ": Magneto " + j1 + " Destroyed..");
                } else if (s.endsWith("sync")) {
                    if ((this.getEnergyPastArmor(2.1F, shot) <= 0.0F) || (World.Rnd().nextFloat() < 0.5F)) {
                        ;
                    }
                } else if (s.endsWith("oil1")) {
                    ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, j);
                    this.reflectGlassState(2);
                    Aircraft.debugprintln(this, "*** Engine" + j + ": Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                    int k = s.charAt(5) - 49;
                    if (k == 7) {
                        k = 0;
                    }
                    if (k == 8) {
                        k = 1;
                    }
                    ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, k);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.reflectGlassState(2);
                    Aircraft.debugprintln(this, "*** Engine" + k + ": Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 48;
                switch (l) {
                    default:
                        break;

                    case 1: // '\001'
                    case 2: // '\002'
                        if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            if (((FlightModelMain) (super.FM)).AS.astateTankStates[2] == 0) {
                                Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                                ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 1);
                                ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, 2, 1);
                            }
                            if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) {
                                ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 2);
                                Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                            }
                        }
                        break;

                    case 3: // '\003'
                    case 4: // '\004'
                        if ((this.getEnergyPastArmor(0.1F, shot) <= 0.0F) || (World.Rnd().nextFloat() >= 0.25F)) {
                            break;
                        }
                        if (((FlightModelMain) (super.FM)).AS.astateTankStates[3] == 0) {
                            Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                            ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 3, 1);
                            ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, 3, 1);
                        }
                        if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) {
                            ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 3, 2);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                        }
                        break;

                    case 5: // '\005'
                        if ((this.getEnergyPastArmor(0.1F, shot) <= 0.0F) || (World.Rnd().nextFloat() >= 0.25F)) {
                            break;
                        }
                        if (((FlightModelMain) (super.FM)).AS.astateTankStates[1] == 0) {
                            Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                            ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 1, 1);
                            ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, 1, 1);
                        }
                        if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) {
                            ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 1, 2);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxcannon")) {
                if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) {
                    int i1 = s.charAt(9) - 49;
                    Aircraft.debugprintln(this, "*** Cannon(" + i1 + "): Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(0, i1);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
                }
                return;
            }
            if (s.startsWith("xxammo")) {
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
            if (s.startsWith("xcockpit")) {
                if (((Tuple3d) (point3d)).z > 0.40000000000000002D) {
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
                    this.reflectGlassState(5);
                    if (World.Rnd().nextFloat() < 0.1F) {
                        ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x20);
                        this.reflectGlassState(5);
                    }
                } else if (((Tuple3d) (point3d)).y > 1.7649999999999999D) {
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 4);
                } else {
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x10);
                    this.reflectGlassState(5);
                }
                if (((Tuple3d) (point3d)).x > 0.20000000000000001D) {
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x40);
                }
            }
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail1")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xtail2")) {
            if (this.chunkDamageVisible("Tail2") < 3) {
                this.hitChunk("Tail2", shot);
            }
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) {
                this.hitChunk("Keel2", shot);
            }
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 1) {
                this.hitChunk("Rudder2", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 1)) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 1)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                this.hitChunk("WingLMid", shot);
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
            int k1;
            if (s.endsWith("a")) {
                byte0 = 1;
                k1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k1 = s.charAt(6) - 49;
            } else {
                k1 = s.charAt(5) - 49;
            }
            this.hitFlesh(k1, shot, byte0);
            if (((FlightModelMain) (super.FM)).AS.getPilotHealth(0) < 1.0F) {
                this.reflectGlassState(8);
            }
        }
    }

    public void doWoundPilot(int j, float f1) {
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0: // '\0'
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                if (!((FlightModelMain) (super.FM)).AS.bIsAboutToBailout && World.cur().isHighGore()) {
                    if (this.hierMesh().isChunkVisible("Blister1_D0")) {
                        this.hierMesh().chunkVisible("Gore1_D0", true);
                    }
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    public void moveCockpitDoor(float f) {
        if (this.bHasBlister) {
            this.resetYPRmodifier();
            this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            if (Config.isUSE_RENDER()) {
                if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                    Main3D.cur3D().cockpits[0].onDoorMoved(f);
                }
                this.setDoorSnd(f);
            }
        }
    }

    private void CombustionFlame() {
        int i = World.Rnd().nextInt(0, 100);
        int j = World.Rnd().nextInt(1, 12);
        switch (i) {
            case 1: // '\001'
                this.random = "01";
                break;

            case 8: // '\b'
                this.random = "02";
                break;

            case 14: // '\016'
                this.random = "03";
                break;

            case 21: // '\025'
                this.random = "04";
                break;

            case 30: // '\036'
                this.random = "05";
                break;

            case 38: // '&'
                this.random = "06";
                break;

            case 44: // ','
                this.random = "07";
                break;

            case 68: // 'D'
                this.random = "08";
                break;

            case 70: // 'F'
                this.random = "09";
                break;

            case 81: // 'Q'
                this.random = "10";
                break;

            case 89: // 'Y'
                this.random = "11";
                break;

            case 94: // '^'
                this.random = "12";
                break;
        }
        switch (j) {
            case 1: // '\001'
                this.random3 = "01";
                break;

            case 2: // '\002'
                this.random3 = "03";
                break;

            case 3: // '\003'
                this.random3 = "05";
                break;

            case 4: // '\004'
                this.random3 = "07";
                break;

            case 5: // '\005'
                this.random3 = "09";
                break;

            case 6: // '\006'
                this.random3 = "11";
                break;

            case 7: // '\007'
                this.random3 = "02";
                break;

            case 8: // '\b'
                this.random3 = "04";
                break;

            case 9: // '\t'
                this.random3 = "06";
                break;

            case 10: // '\n'
                this.random3 = "08";
                break;

            case 11: // '\013'
                this.random3 = "10";
                break;

            case 12: // '\f'
                this.random3 = "12";
                break;
        }
        if ((((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 6) && (((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.85F)) {
            Eff3DActor.New(this, this.findHook("_Engine1EF_" + this.random3), null, 1.0F, "3DO/Effects/Fireworks/HolyGrail2.eff", -1F);
        }
        if ((((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 1) && (((FlightModelMain) (super.FM)).EI.engines[0].getStage() < 3) && (super.FM.getSpeedKMH() < 10F)) {
            Eff3DActor.New(this, this.findHook("_Engine1EF_" + this.random), null, 1.0F, "3DO/Effects/Aircraft/HolyGrail1.eff", -1F);
        }
        if ((((FlightModelMain) (super.FM)).EI.engines[1].getStage() == 6) && (((FlightModelMain) (super.FM)).EI.engines[1].getPowerOutput() > 0.85F)) {
            Eff3DActor.New(this, this.findHook("_Engine2EF_" + this.random3), null, 1.0F, "3DO/Effects/Fireworks/HolyGrail2.eff", -1F);
        }
        if ((((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 1) && (((FlightModelMain) (super.FM)).EI.engines[1].getStage() < 3) && (super.FM.getSpeedKMH() < 10F)) {
            Eff3DActor.New(this, this.findHook("_Engine2EF_" + this.random), null, 1.0F, "3DO/Effects/Aircraft/HolyGrail1.eff", -1F);
        }
    }

    private String random;
    private String random3;
    public float   cockpitDoor_;
    private float  fMaxKMHSpeedForOpenCanopy;
    private float  kangle;
    public boolean bHasBlister;
    private float  flapps;
    private int    GlassState;

    static {
        Class class1 = BF_109Z.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109Z/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944.5F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109Z.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109Z.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 9, 9, 9, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON05", "_CANNON04", "_ExternalDev01", "_ExternalDev03", "_ExternalDev02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
