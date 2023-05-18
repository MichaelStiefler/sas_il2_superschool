package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class D520 extends Scheme1 implements TypeFighter {

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, 0.5F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("OilRad_D0", 0.0F, -20F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -87F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 87F * f);
        hiermesh.chunkSetAngles("Antenna_D0", 0.0F, -90F * f, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
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

    protected void moveFlap(float f) {
        float f1 = -50F * f;
        this.hierMesh().chunkSetAngles("FlapL_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("FlapR_D0", 0.0F, f1, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) this.getEnergyPastArmor(14.2F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                if (s.endsWith("1")) {
                    if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                        this.debuggunnery("Controls: Control Column: Hit, Controls Destroyed..");
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                } else if (s.endsWith("2") || s.endsWith("3")) {
                    if (this.getEnergyPastArmor(0.002F, shot) > 0.0F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                    }
                } else if ((s.endsWith("4") || s.endsWith("5")) && World.Rnd().nextFloat() < 0.3F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Ailerones Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("prop") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F) {
                    this.debuggunnery("Engine Module: Prop Governor Failed..");
                    this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                }
                if (s.endsWith("gear") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F) {
                    this.debuggunnery("Engine Module: Prop Governor Damaged..");
                    this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
                }
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 200000F) {
                            this.debuggunnery("Engine Module: Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < shot.power / 50000F) {
                            this.debuggunnery("Engine Module: Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                        if (World.Rnd().nextFloat() < shot.power / 28000F) {
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                            this.debuggunnery("Engine Module: Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        }
                        if (World.Rnd().nextFloat() < 0.08F) {
                            this.debuggunnery("Engine Module: Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
                            this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                        }
                        this.debuggunnery("Engine Module: Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.debuggunnery("Engine Module: Crank Case Hit - Engine Stalled..");
                        this.FM.EI.engines[0].setEngineStops(shot.initiator);
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.debuggunnery("Engine Module: Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
                        this.FM.AS.hitEngine(shot.initiator, 0, 10);
                    }
                    this.getEnergyPastArmor(16F, shot);
                }
                if ((s.endsWith("cyl1") || s.endsWith("cyl2")) && this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.72F) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.debuggunnery("Engine Module: Cylinder Case Broken - Engine Stuck..");
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                    if (World.Rnd().nextFloat() < shot.power / 24000F) {
                        this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        this.FM.AS.hitEngine(shot.initiator, 0, 3);
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
                }
                if (s.endsWith("supc") && this.getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
                    this.debuggunnery("Engine Module: Supercharger Out..");
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                }
                if (s.endsWith("eqpt") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F && World.Rnd().nextFloat() < 0.89F && World.Rnd().nextFloat() < 0.11F) {
                    this.debuggunnery("Engine Module: Compressor Feed Out..");
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                }
                if (s.startsWith("xxeng1mag")) {
                    int i = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                }
                if (s.endsWith("oil1") && this.getEnergyPastArmor(1.27F, shot) > 0.0F) {
                    this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitTank(shot.initiator, j, 1);
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F) this.FM.AS.hitTank(shot.initiator, j, 2);
                }
                return;
            }
            if (s.startsWith("xxcannon")) {
                if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setJamBullets(1, 0);
                return;
            }
            if (s.startsWith("xxmgun")) {
                int k = s.charAt(7) - 49;
                this.FM.AS.setJamBullets(0, k);
                return;
            }
            if (s.startsWith("xxammo")) {
                int l = s.charAt(7) - 48;
                if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) switch (l) {
                    case 1:
                        this.FM.AS.setJamBullets(0, 0);
                        break;

                    case 2:
                        this.FM.AS.setJamBullets(0, 1);
                        break;
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("Rudder Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.endsWith("al") && this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
                    this.debuggunnery("AroneL Lock Damaged..");
                    this.nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
                }
                if (s.endsWith("ar") && this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
                    this.debuggunnery("AroneR Lock Damaged..");
                    this.nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) this.hitChunk("CF", shot);
        if (!s.startsWith("xcockpit")) if (s.startsWith("xeng")) this.hitChunk("Engine1", shot);
        else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) this.hitChunk("Keel1", shot);
        else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
        else if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
        else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
            if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
            if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 1) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneL") < 1) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if ((s.endsWith("2a") || s.endsWith("2b")) && World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i1;
            if (s.endsWith("a")) {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else i1 = s.charAt(5) - 49;
            this.hitFlesh(i1, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 9:
            case 33:
                this.hierMesh().chunkVisible("GearL3_D0", false);
                break;

            case 10:
            case 36:
                this.hierMesh().chunkVisible("GearR3_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    static {
        Class class1 = D520.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D520");
        Property.set(class1, "meshName", "3DO/Plane/D520(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "originCountry", PaintScheme.countryFrance);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/D520.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitD520.class });
        Property.set(class1, "LOSElevation", 0.85935F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01" });
    }
}
