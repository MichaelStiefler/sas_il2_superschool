package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class AN_26 extends Scheme1 implements TypeTransport {

    public AN_26() {
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveFlap(float f) {
        float f1 = 40F * f;
        this.hierMesh().chunkSetAngles("Flap1_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Flap2_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Flap3_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Flap4_D0", 0.0F, 0.0F, f1);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -30F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -30F * f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -85F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -85F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 130F * f);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 130F * f);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, -120F * f);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 90F * f, 0.0F);
        if (f < 0.5F) {
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 85F), 0.0F);
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -85F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.9F, 0.99F, 85F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f, 0.9F, 0.99F, -85F, 0.0F), 0.0F);
        }
        if (f < 0.5F) {
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 80F), 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -80F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.9F, 0.99F, 80F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.9F, 0.99F, -80F, 0.0F), 0.0F);
        }
        if (f < 0.5F) {
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 80F), 0.0F);
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -80F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.9F, 0.99F, 80F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.9F, 0.99F, -80F, 0.0F), 0.0F);
        }
    }

    protected void moveGear(float f) {
        AN_26.moveGear(this.hierMesh(), f);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay_D0", 0.0F, 40F * f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxeng")) {
                int i = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(1.7F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(20000F, 140000F) < shot.power) {
                            this.FM.AS.setEngineStuck(shot.initiator, i);
                            this.debuggunnery("*** Engine" + i + " Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat(10000F, 50000F) < shot.power) {
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                            this.debuggunnery("*** Engine" + i + " Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.04F) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    } else {
                        this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.02F);
                        this.debuggunnery("*** Engine" + i + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[i].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(0.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[i].getCylindersRatio() * 0.9878F))) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        this.debuggunnery("*** Engine" + i + " Cylinders Hit, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            this.debuggunnery("*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) {
                        this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("oil1")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                        this.debuggunnery("Engine Module: Oil Tank Pierced..");
                    }
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(1.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.4F)) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if ((World.Rnd().nextFloat() < 0.003F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.2F))) {
                        this.FM.AS.hitTank(shot.initiator, j, 4);
                        this.debuggunnery("Fuel Tank (" + j + "): Hit..");
                    }
                }
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) {
                this.hitChunk("StabL", shot);
            }
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 1) {
                this.hitChunk("VatorL", shot);
            }
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 1) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) {
                this.hitChunk("WingLMid", shot);
            }
        } else if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) {
                this.hitChunk("WingRMid", shot);
            }
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) {
                this.hitChunk("WingLOut", shot);
            }
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 1) {
                this.hitChunk("AroneL", shot);
            }
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 1) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 3) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 3) {
                this.hitChunk("Engine2", shot);
            }
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.endsWith("2") && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F)) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k;
            if (s.endsWith("a") || s.endsWith("a2")) {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else if (s.endsWith("b") || s.endsWith("b2")) {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else {
                k = s.charAt(5) - 49;
            }
            this.hitFlesh(k, shot, byte0);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    static {
        Class class1 = AN_26.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AN26");
        Property.set(class1, "meshName", "3DO/Plane/AN26/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/AN26.fmd:AN26_FM");
        Property.set(class1, "cockpitClass", new Class[] { Cockpit_AN26.class });
        Property.set(class1, "LOSElevation", 0.5265F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01" });
    }
}
