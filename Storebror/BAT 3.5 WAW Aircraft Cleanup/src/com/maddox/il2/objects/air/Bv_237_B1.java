package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.Property;

public class Bv_237_B1 extends Bv_237X implements TypeFighter, TypeStormovik, TypeX4Carrier {

    public Bv_237_B1() {
        prevWing = true;
        this.bToFire = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
    }

    public void onAircraftLoaded() {
        if (super.thisWeaponsName.startsWith("2 x Mk-103")) {
            this.hierMesh().chunkVisible("Gun01_D0", true);
            this.hierMesh().chunkVisible("Gun02_D0", true);
        } else {
            this.hierMesh().chunkVisible("Gun01_D0", false);
            this.hierMesh().chunkVisible("Gun02_D0", false);
        }
        if (super.thisWeaponsName.startsWith("50mm Cannon")) {
            this.hierMesh().chunkVisible("Gun03_D0", true);
        } else {
            this.hierMesh().chunkVisible("Gun03_D0", false);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(6.78F, shot);
                } else if (s.endsWith("g1")) {
                    this.getEnergyPastArmor(9.96F / (1E-005F + (float) Math.abs(((Tuple3d) (Aircraft.v1)).x)), shot);
                } else if (s.endsWith("g2")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(30F, 50F) / (1E-005F + (float) Math.abs(((Tuple3d) (Aircraft.v1)).x)), shot);
                }
            } else if (s.startsWith("xxcontrols")) {
                if (s.endsWith("1")) {
                    if (World.Rnd().nextFloat() < 0.12F) {
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.12F) {
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.12F) {
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Arone Controls Out..");
                    }
                } else if (s.endsWith("2")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                } else if (s.endsWith("3") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                    ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                }
            } else if (s.startsWith("xxspar")) {
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
                if (s.endsWith("e1")) {
                    this.getEnergyPastArmor(6F, shot);
                }
            } else if (s.startsWith("xxeng1")) {
                if (s.endsWith("prp") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                    ((FlightModelMain) (super.FM)).EI.engines[0].setKillPropAngleDevice(shot.initiator);
                }
                if (s.endsWith("cas") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                    if (World.Rnd().nextFloat() < (shot.power / 200000F)) {
                        ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 28000F)) {
                        ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Left..");
                    }
                    ((FlightModelMain) (super.FM)).EI.engines[0].setReadyness(shot.initiator, ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() + "..");
                }
                if (s.startsWith("xxeng2")) {
                    if (s.endsWith("cas") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                        if (World.Rnd().nextFloat() < (shot.power / 200000F)) {
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 28000F)) {
                            ((FlightModelMain) (super.FM)).EI.engines[1].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + ((FlightModelMain) (super.FM)).EI.engines[1].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[1].getCylinders() + " Left..");
                        }
                        ((FlightModelMain) (super.FM)).EI.engines[1].setReadyness(shot.initiator, ((FlightModelMain) (super.FM)).EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[1].getReadyness() + "..");
                    }
                    if (s.endsWith("cyl") && (this.getEnergyPastArmor(0.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < (((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 1.75F))) {
                        ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Left..");
                        if (((FlightModelMain) (super.FM)).AS.astateEngineStates[0] < 1) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                        this.getEnergyPastArmor(25F, shot);
                    }
                    if (s.endsWith("sup") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
                        ((FlightModelMain) (super.FM)).EI.engines[0].setKillCompressor(shot.initiator);
                    }
                } else if (s.startsWith("xxtank")) {
                    int i = s.charAt(6) - 49;
                    if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, 1);
                        if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.11F)) {
                            ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, 2);
                        }
                    }
                } else {
                    if (s.startsWith("xxmgunl1") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                        ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
                    }
                    if (s.startsWith("xxmgunr1") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                        ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 1);
                    }
                    if (s.startsWith("xxmgunl2") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                        ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 2);
                    }
                    if (s.startsWith("xxmgunr2") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                        ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 3);
                    }
                    if (s.startsWith("xxhispa1") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                        ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 0);
                    }
                    if (s.startsWith("xxhispa2") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                        ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 1);
                    }
                    if (s.startsWith("xxhispa3") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                        ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 2);
                    }
                    if (s.startsWith("xxhispa4") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                        ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 3);
                    }
                }
            } else if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
                this.hitChunk("CF", shot);
                if (((Tuple3d) (point3d)).x > -2.2000000000000002D) {
                    if (World.Rnd().nextFloat() < 0.1F) {
                        ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x40);
                    }
                    if ((((Tuple3d) (point3d)).x < -1D) && (((Tuple3d) (point3d)).z > 0.55000000000000004D)) {
                        ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
                    }
                    if (((Tuple3d) (point3d)).z > 0.65000000000000002D) {
                        ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
                    }
                    if (Math.abs(((Tuple3d) (Aircraft.v1)).x) < 0.80000001192092896D) {
                        if (((Tuple3d) (point3d)).y > 0.0D) {
                            if (World.Rnd().nextFloat() < 0.1F) {
                                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 4);
                            }
                            if (World.Rnd().nextFloat() < 0.1F) {
                                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 8);
                            }
                        } else {
                            if (World.Rnd().nextFloat() < 0.1F) {
                                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x10);
                            }
                            if (World.Rnd().nextFloat() < 0.1F) {
                                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x20);
                            }
                        }
                    }
                }
            } else if (s.startsWith("xeng")) {
                if (this.chunkDamageVisible("Engine1") < 3) {
                    this.hitChunk("Engine1", shot);
                }
            } else if (s.startsWith("xtail")) {
                if (this.chunkDamageVisible("Tail1") < 3) {
                    this.hitChunk("Tail1", shot);
                }
            } else if (s.startsWith("xkeel")) {
                this.hitChunk("Keel1", shot);
            } else if (s.startsWith("xrudder")) {
                this.hitChunk("Rudder1", shot);
            } else if (s.startsWith("xstab")) {
                if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 3)) {
                    this.hitChunk("StabL", shot);
                }
                if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 3)) {
                    this.hitChunk("StabR", shot);
                }
            } else if (s.startsWith("xvator")) {
                if (s.startsWith("xvatorl")) {
                    this.hitChunk("VatorL", shot);
                }
                if (s.startsWith("xvatorr")) {
                    this.hitChunk("VatorR", shot);
                }
            } else if (s.startsWith("xwing")) {
                if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
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
                        ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, 0);
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
            } else if (s.startsWith("xoil")) {
                if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                    this.debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, 0);
                }
            } else if (s.startsWith("xwater")) {
                if (((FlightModelMain) (super.FM)).AS.astateEngineStates[0] == 0) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                    ((FlightModelMain) (super.FM)).AS.doSetEngineState(shot.initiator, 0, 1);
                } else if (((FlightModelMain) (super.FM)).AS.astateEngineStates[0] == 1) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                    ((FlightModelMain) (super.FM)).AS.doSetEngineState(shot.initiator, 0, 2);
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
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 1.0F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -1F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 1.0F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -1F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public boolean        bToFire;
    private float         deltaAzimuth;
    private float         deltaTangage;
    public static boolean bChangedPit = false;
    public static boolean prevWing    = false;
    static {
        Class class1 = Bv_237_B1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bv-237-B1");
        Property.set(class1, "meshName", "3DO/Plane/Bv_237_B1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1955.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bv237B1.fmd:Bv237B1_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBv_237.class });
        Property.set(class1, "LOSElevation", 0.7394F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 3, 3, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb03", "_ExternalBomb04", "_CANNON05" });
    }
}
