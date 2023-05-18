package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class DH_88 extends Scheme1 implements TypeScout {

    public DH_88() {
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, 30F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, 30F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, -30F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, 30F * f);
    }

    protected void moveFlap(float f) {
        float f1 = -40F * f;
        this.hierMesh().chunkSetAngles("Flap1_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Flap2_D0", 0.0F, 0.0F, f1);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -90F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -90F * f);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, 125F * f);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, 125F * f);
    }

    protected void moveGear(float f) {
        DH_88.moveGear(this.hierMesh(), f);
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
                if (s.endsWith("oil1") && (this.getEnergyPastArmor(4.21F, shot) > 0.0F)) {
                    this.FM.AS.hitOil(shot.initiator, i);
                    this.getEnergyPastArmor(0.42F, shot);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.06F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if (shot.powerType == 3) {
                        if (shot.power < 16100F) {
                            if ((this.FM.AS.astateTankStates[j] < 4) && (World.Rnd().nextFloat() < 0.21F)) {
                                this.FM.AS.hitTank(shot.initiator, j, 1);
                            }
                        } else {
                            this.FM.AS.hitTank(shot.initiator, j, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                        }
                    } else if (shot.power > 16100F) {
                        this.FM.AS.hitTank(shot.initiator, j, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                    }
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Tail1") < 2) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xrudder")) {
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
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 2) {
                this.hitChunk("WingLIn", shot);
            }
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 2) {
                this.hitChunk("WingRIn", shot);
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
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
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
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;
        }
    }

    static {
        Class class1 = DH_88.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Comet");
        Property.set(class1, "meshName", "3DO/Plane/COMET/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1940F);
        Property.set(class1, "FlightModel", "FlightModels/DH88.fmd:DH88_FM");
        Property.set(class1, "LOSElevation", 0.5265F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitDH88.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "null" });
    }
}
