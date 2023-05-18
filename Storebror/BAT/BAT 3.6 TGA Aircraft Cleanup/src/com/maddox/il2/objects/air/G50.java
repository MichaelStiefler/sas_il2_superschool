package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class G50 extends Scheme1 implements TypeFighter, TypeTNBFighter {

    public G50() {
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals("fi")) {
            int i = Mission.getMissionDate(true);
            if (i > 0) {
                if (i < 0x1282df2) {
                    return "winterwar_";
                }
                if (i < 0x1282fd5) {
                    return "early_";
                }
            }
        }
        return "";
    }

    public float getEyeLevelCorrection() {
        return 0.05F;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);
        }
        return super.cutFM(i, j, actor);
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
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(14.2F, shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                        if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Control Column: Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 2:
                    case 3:
                    case 4:
                        if (this.getEnergyPastArmor(0.004F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        }
                        break;

                    case 5:
                        if (this.getEnergyPastArmor(0.004F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        }
                        break;

                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        if ((this.getEnergyPastArmor(0.12F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Controls: Aileron Controls: Disabled..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("prop") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F)) {
                    this.debuggunnery("Engine Module: Prop Governor Failed..");
                    this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                }
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 200000F)) {
                            this.debuggunnery("Engine Module: Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < 0.001F) {
                            this.debuggunnery("Engine Module: Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
                            this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 256000F));
                        this.debuggunnery("Engine Module: Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    if (World.Rnd().nextFloat() < 0.002F) {
                        this.debuggunnery("Engine Module: Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
                        this.FM.AS.hitEngine(shot.initiator, 0, 10);
                    }
                    this.getEnergyPastArmor(16F, shot);
                }
                if (s.endsWith("cyls") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 0.56F))) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.debuggunnery("Engine Module: Cylinder Case Broken - Engine Stuck..");
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                        this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        this.FM.AS.hitEngine(shot.initiator, 0, 3);
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
                }
                if (s.endsWith("supc") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    this.debuggunnery("Engine Module: Supercharger Out..");
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                }
                if (s.endsWith("eqpt") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F) && (World.Rnd().nextFloat() < 0.11F)) {
                    this.debuggunnery("Engine Module: Compressor Feed Out..");
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                }
                if (s.startsWith("xxeng1mag")) {
                    int j = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                }
                if (s.endsWith("oil1") && (this.getEnergyPastArmor(1.27F, shot) > 0.0F)) {
                    this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitTank(shot.initiator, k, 1);
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.11F)) {
                        this.FM.AS.hitTank(shot.initiator, k, 2);
                    }
                }
                return;
            }
            if (s.startsWith("xxmgun")) {
                int l = s.charAt(7) - 49;
                this.FM.AS.setJamBullets(0, l);
                return;
            }
            if (s.startsWith("xxammo")) {
                int i1 = s.charAt(7) - 48;
                if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                    switch (i1) {
                        case 1:
                            this.FM.AS.setJamBullets(0, 0);
                            break;

                        case 2:
                            this.FM.AS.setJamBullets(0, 1);
                            break;
                    }
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    this.debuggunnery("WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    this.debuggunnery("WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    this.debuggunnery("WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    this.debuggunnery("WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    this.debuggunnery("WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    this.debuggunnery("WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxhyd") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.startsWith("xxins")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
            return;
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
            if (s.startsWith("xcockpit")) {
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                }
                if (point3d.y > 0.0D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                } else {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                }
            }
        }
        if (!s.startsWith("xcockpit")) {
            if (s.startsWith("xeng")) {
                this.hitChunk("Engine1", shot);
            } else if (s.startsWith("xtail")) {
                if (this.chunkDamageVisible("Tail1") < 3) {
                    this.hitChunk("Tail1", shot);
                }
            } else if (s.startsWith("xkeel")) {
                this.hitChunk("Keel1", shot);
            } else if (s.startsWith("xrudder")) {
                if (this.chunkDamageVisible("Rudder1") < 1) {
                    this.hitChunk("Rudder1", shot);
                }
            } else if (s.startsWith("xstabl")) {
                this.hitChunk("StabL", shot);
            } else if (s.startsWith("xstabr")) {
                this.hitChunk("StabR", shot);
            } else if (s.startsWith("xvator")) {
                if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 1)) {
                    this.hitChunk("VatorL", shot);
                }
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
                if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                    this.hitChunk("AroneL", shot);
                }
                if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneL") < 1)) {
                    this.hitChunk("AroneR", shot);
                }
            } else if (s.startsWith("xgear")) {
                if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05F)) {
                    this.debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if (s.endsWith("2") && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
                    this.debuggunnery("Undercarriage: Stuck..");
                    this.FM.AS.setInternalDamage(shot.initiator, 3);
                }
            } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
                byte byte0 = 0;
                int j1;
                if (s.endsWith("a")) {
                    byte0 = 1;
                    j1 = s.charAt(6) - 49;
                } else if (s.endsWith("b")) {
                    byte0 = 2;
                    j1 = s.charAt(6) - 49;
                } else {
                    j1 = s.charAt(5) - 49;
                }
                this.hitFlesh(j1, shot, byte0);
            }
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -95F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -95F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -95F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -95F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -105F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -105F * f1, 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        G50.moveGear(this.hierMesh(), f, f1, f2);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.2F, 0.0F, 0.2F);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.2F, 0.0F, 0.2F);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
        }
    }

    static {
        Class class1 = G50.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "G.50");
        Property.set(class1, "meshName", "3DO/Plane/G-50(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_it", "3DO/Plane/G-50(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "originCountry", PaintScheme.countryItaly);
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1944.8F);
        Property.set(class1, "FlightModel", "FlightModels/G50.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitG50.class });
        Property.set(class1, "LOSElevation", 0.98615F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
