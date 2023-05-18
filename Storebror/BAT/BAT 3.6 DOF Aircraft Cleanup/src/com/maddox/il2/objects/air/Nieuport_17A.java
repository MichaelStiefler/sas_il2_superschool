package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class Nieuport_17A extends Scheme1 implements TypeFighter, TypeTNBFighter {

    public Nieuport_17A() {
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        Nieuport_17A.bChangedPit = true;
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("Pilot1")) {
            this.killPilot(shot.initiator, 0);
        }
        if (shot.chunkName.startsWith("Engine") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        super.msgShot(shot);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -27F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -27F * f, 0.0F);
    }

    protected void moveFlap(float f) {
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
    }

    public void update(float f) {
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER() && (this.FM.CT.PowerControl > 0.95F) && (this.FM.EI.engines[0].getRPM() > 400F)) {
            this.FM.AS.setSootState(this, 0, 1);
        } else {
            this.FM.AS.setSootState(this, 0, 0);
        }
        super.update(f);
    }

    protected void moveFan(float f) {
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        int i = 0;
        int j = 0;
        int k = 1;
        if (this.oldProp[j] < 2) {
            i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
            if (i >= 1) {
                i = 1;
            }
            if ((i != this.oldProp[j]) && this.hierMesh().isChunkVisible(Nieuport_17A.Props[j][this.oldProp[j]])) {
                this.hierMesh().chunkVisible(Nieuport_17A.Props[0][this.oldProp[j]], false);
                this.oldProp[j] = i;
                this.hierMesh().chunkVisible(Nieuport_17A.Props[j][i], true);
            }
        }
        if (i == 0) {
            this.propPos[j] = (this.propPos[j] + (57.3F * this.FM.EI.engines[0].getw() * f)) % 360F;
        } else {
            float f1 = 57.3F * this.FM.EI.engines[0].getw();
            f1 %= 2880F;
            f1 /= 2880F;
            if (f1 <= 0.5F) {
                f1 *= 2.0F;
            } else {
                f1 = (f1 * 2.0F) - 2.0F;
            }
            f1 *= 1200F;
            this.propPos[j] = (this.propPos[j] + (f1 * f)) % 360F;
        }
        this.hierMesh().chunkSetAngles(Nieuport_17A.Props[j][0], 0.0F, -this.propPos[j], 0.0F);
        if (this.oldProp[k] < 2) {
            i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
            if (i >= 1) {
                i = 1;
            }
            if ((i != this.oldProp[k]) && this.hierMesh().isChunkVisible(Nieuport_17A.Props[k][this.oldProp[k]])) {
                this.hierMesh().chunkVisible(Nieuport_17A.Props[k][this.oldProp[k]], false);
                this.oldProp[k] = i;
                this.hierMesh().chunkVisible(Nieuport_17A.Props[k][i], true);
            }
        }
        if (i == 0) {
            this.propPos[k] = (this.propPos[k] + (57.3F * this.FM.EI.engines[0].getw() * f)) % 360F;
        } else {
            float f2 = 57.3F * this.FM.EI.engines[0].getw();
            f2 %= 2880F;
            f2 /= 2880F;
            if (f2 <= 0.5F) {
                f2 *= 2.0F;
            } else {
                f2 = (f2 * 2.0F) - 2.0F;
            }
            f2 *= 1200F;
            this.propPos[k] = (this.propPos[k] + (f2 * f)) % 360F;
        }
        this.hierMesh().chunkSetAngles(Nieuport_17A.Props[k][0], 0.0F, -this.propPos[k], 0.0F);
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if ((explosion.chunkName != null) && (explosion.power > 0.0F) && explosion.chunkName.startsWith("Tail1")) {
            if (World.Rnd().nextFloat(0.0F, 0.015F) < explosion.power) {
                this.FM.AS.setControlsDamage(explosion.initiator, 1);
            }
            if (World.Rnd().nextFloat(0.0F, 0.02F) < explosion.power) {
                this.FM.AS.setControlsDamage(explosion.initiator, 2);
            }
        }
        super.msgExplosion(explosion);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                Aircraft.debugprintln(this, "*** Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(5.26F / (Math.abs((float) Aircraft.v1.x) + 1E-006F), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.3F), shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.55F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 2);
                                Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                            }
                            if (World.Rnd().nextFloat() < 0.55F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 1);
                                Aircraft.debugprintln(this, "*** Elevator Controls: Disabled..");
                            }
                        }
                        // fall through

                    case 2:
                        if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                        }
                        break;

                    case 3:
                        if ((this.getEnergyPastArmor(5.6F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Elevator Controls: Disabled..");
                        }
                        break;

                    case 4:
                    case 5:
                        if ((this.getEnergyPastArmor(2.3F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.31F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                        }
                        break;
                }
            }
            if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.5F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if (s.startsWith("xxsparli") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                }
                if (s.startsWith("xxsparri") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                }
                if (s.startsWith("xxsparro") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                }
                if (s.startsWith("xxstabl") && (this.getEnergyPastArmor(16.2F, shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D" + this.chunkDamageVisible("StabL"), shot.initiator);
                }
                if (s.startsWith("xxstabr") && (this.getEnergyPastArmor(16.2F, shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D" + this.chunkDamageVisible("StabR"), shot.initiator);
                }
            }
            if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            if (s.startsWith("xxeng")) {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("prop")) {
                    if ((this.getEnergyPastArmor(0.45F / (Math.abs((float) Aircraft.v1.x) + 0.0001F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.05F)) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
                    }
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(5.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 800F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 500F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 800F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(7.9F, shot);
                } else if (s.startsWith("xxeng1cyls")) {
                    if ((this.getEnergyPastArmor(World.Rnd().nextFloat(1.5F, 23.9F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.12F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 800F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 800F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.005F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(7.9F, shot);
                    }
                } else if (s.endsWith("eqpt")) {
                    if ((this.getEnergyPastArmor(2.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Magneto 0 Destroyed..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine Module: Magneto 1 Destroyed..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            Aircraft.debugprintln(this, "*** Engine Module: Prop Controls Cut..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            Aircraft.debugprintln(this, "*** Engine Module: Throttle Controls Cut..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                            Aircraft.debugprintln(this, "*** Engine Module: Mix Controls Cut..");
                        }
                    }
                } else if (s.endsWith("oil1")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
            }
            if (s.startsWith("xxoil")) {
                this.FM.AS.hitOil(shot.initiator, 0);
                this.getEnergyPastArmor(0.22F, shot);
                Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
            }
            if (s.startsWith("xxtank1") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.15F)) {
                if (this.FM.AS.astateTankStates[0] == 0) {
                    Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                    this.FM.AS.hitTank(shot.initiator, 0, 1);
                }
                if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.75F)) {
                    this.FM.AS.hitTank(shot.initiator, 0, 2);
                    Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                }
            }
            if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                if (s.endsWith("03")) {
                    Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.endsWith("04")) {
                    Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            }
            return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 1)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 1)) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 1)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 1)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 1)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 1)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 1)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 1)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
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

    protected int                 oldProp[]   = { 0, 0 };
    protected static final String Props[][]   = { { "Prop1_D0", "PropRot1_D0", "Prop1_D1" }, { "Prop2_D0", "PropRot2_D0", "Prop2_D1" } };
    protected float               kangle;
    public static boolean         bChangedPit = false;
    private float                 dynamoOrient;
    private boolean               bDynamoRotary;
    static {
        Class class1 = Nieuport_17A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Nieup");
        Property.set(class1, "meshName", "3DO/Plane/Nieuport-17a/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1918F);
        Property.set(class1, "yearExpired", 1926F);
        Property.set(class1, "FlightModel", "FlightModels/Niu17.fmd:Niu17_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitNieu17A.class });
        Property.set(class1, "LOSElevation", 0.7956F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_BombSpawn01" });
    }
}
