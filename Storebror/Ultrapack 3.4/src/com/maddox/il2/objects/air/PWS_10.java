package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class PWS_10 extends Scheme1 implements TypeFighter, TypeTNBFighter {

    public PWS_10() {
        PWS_10.bChangedPit = true;
        this.bDynamoOperational = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            PWS_10.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            PWS_10.bChangedPit = true;
        }
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder1L_D0", 0.0F, 25F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder1R_D0", 0.0F, 25F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, 20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, 20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, 20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, 20F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -25F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -25F * f, 0.0F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 4F, 0.0F, 4F);
        this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, 55F * f, 0.0F);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 4F, 0.0F, 4F);
        this.hierMesh().chunkSetAngles("Gearl3_D0", 0.0F, -40F * f, 0.0F);
        Aircraft.xyz[2] = -0.63F * f;
        this.hierMesh().chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 4F, 0.0F, 4F);
        this.hierMesh().chunkSetAngles("GearR2_D0", 0.0F, -55F * f, 0.0F);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 4F, 0.0F, 4F);
        this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 40F * f, 0.0F);
        Aircraft.xyz[2] = -0.63F * f;
        this.hierMesh().chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        f1 = (((-90F * f1) - 0.0F) * f1) + 0.0F;
        this.kangle = (0.95F * this.kangle) - (0.05F * f1);
        this.hierMesh().chunkSetAngles("radiator1_D0", 0.0F, this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator2_D0", 0.0F, this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator3_D0", 0.0F, this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator4_D0", 0.0F, this.kangle, 0.0F);
        super.update(f);
    }

    protected void moveFan(float f) {
        if (this.bDynamoOperational) {
            this.pk = Math.abs((int) (this.FM.Vwld.length() / 3D));
            if (this.pk >= 1) {
                this.pk = 1;
            }
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("GenProp_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("GenPropRot_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        this.hierMesh().chunkSetAngles("GenProp_D0", 0.0F, this.dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
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
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    case 1:
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.3F), shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 2);
                                Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                            }
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 1);
                                Aircraft.debugprintln(this, "*** Elevator Controls: Disabled..");
                            }
                        }
                        // fall through

                    case 2:
                    case 3:
                        if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                        }
                        break;
                }
            }
            if (s.startsWith("xxeng1")) {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12.7F, shot);
                } else if (s.startsWith("xxeng1cyls")) {
                    if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.12F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.005F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                } else if (s.endsWith("mag")) {
                    if ((this.getEnergyPastArmor(0.2721F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Magneto 0 Destroyed..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine Module: Magneto 1 Destroyed..");
                        }
                    }
                } else if (s.endsWith("oil1")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xwater")) {
                if (this.FM.AS.astateEngineStates[0] == 0) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                } else if (this.FM.AS.astateEngineStates[0] == 1) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 2);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                this.FM.AS.hitOil(shot.initiator, 0);
                this.getEnergyPastArmor(0.22F, shot);
                Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
            } else if (s.startsWith("xxtank1")) {
                int j = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.4F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.99F)) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j, 2);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.25F)) {
                        this.FM.AS.hitTank(shot.initiator, j, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
            } else if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                } else if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                } else if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                } else if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                } else if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            } else if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    Aircraft.debugprintln(this, "*** Machine Gun #1: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    Aircraft.debugprintln(this, "*** Machine Gun #2: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            } else if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(9.5F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D2", shot.initiator);
                } else if (s.startsWith("xxsparli") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                } else if (s.startsWith("xxsparri") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                } else if (s.startsWith("xxsparlo") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                } else if (s.startsWith("xxsparro") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                }
            }
            return;
        }
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k;
            if (s.endsWith("a")) {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else {
                k = s.charAt(5) - 49;
            }
            if (k == 2) {
                k = 1;
            }
            Aircraft.debugprintln(this, "*** hitFlesh..");
            this.hitFlesh(k, shot, byte0);
        } else if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 2) {
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
        } else if (s.startsWith("xgear")) {
            if (s.startsWith("xgearb")) {
                this.hitChunk("GearB", shot);
            }
            if (s.startsWith("xgearl")) {
                this.hitChunk("GearL2", shot);
            }
            if (s.startsWith("xgearr")) {
                this.hitChunk("GearR2", shot);
            }
        }
    }

    protected float       kangle;
    public static boolean bChangedPit;
    private boolean       bDynamoOperational;
    private float         dynamoOrient;
    private boolean       bDynamoRotary;
    private int           pk;

    static {
        Class class1 = PWS_10.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "PWS_10");
        Property.set(class1, "meshName", "3DO/Plane/PWS-10(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_pl", "3DO/Plane/PWS-10/hier.him");
        Property.set(class1, "PaintScheme_pl", new PaintSchemeFCSPar01());
        Property.set(class1, "meshName_sp", "3DO/Plane/PWS-10(Spain)/hier.him");
        Property.set(class1, "PaintScheme_sp", new PaintSchemeFMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryPoland);
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1939F);
        Property.set(class1, "FlightModel", "FlightModels/PWS-10.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitPWS_10.class });
        Property.set(class1, "LOSElevation", 0.87195F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
