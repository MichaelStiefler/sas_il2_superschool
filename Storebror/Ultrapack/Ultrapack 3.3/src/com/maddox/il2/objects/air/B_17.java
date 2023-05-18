package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public abstract class B_17 extends Scheme4 implements TypeTransport {

    public B_17() {
    }

    private static final float getAngleValue(float f, float af[]) {
        if (f <= 0.0F) return af[1];
        if (f >= 1.0F) return af[af.length - 1];
        for (int i = 0; i < 0.5F * af.length; i++)
            if (af[i + i] < f && f < af[i + i + 2]) {
                float f1 = (f - af[i + i]) / (af[i + i + 2] - af[i + i]);
                return af[i + i + 1] + f1 * (af[i + i + 3] - af[i + i + 1]);
            }

        return 0.0F;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        int k1 = 0;
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1") || s.endsWith("p1")) this.getEnergyPastArmor(2.4F, shot);
                else if (s.endsWith("p9")) this.getEnergyPastArmor(16.87D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                if (s.length() == 12) i = 10 + s.charAt(11) - 48;
                switch (i) {
                    case 3:
                    case 4:
                    case 17:
                    case 18:
                    case 19:
                    default:
                        break;

                    case 1:
                    case 2:
                        if (World.Rnd().nextFloat() < 0.125F && this.getEnergyPastArmor(5.2F, shot) > 0.0F) {
                            debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        this.getEnergyPastArmor(2.0F, shot);
                        break;

                    case 5:
                    case 6:
                        if (World.Rnd().nextFloat() < 0.001F) this.FM.AS.setControlsDamage(shot.initiator, 1);
                        break;

                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        k1 = i - 7;
                        if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, k1, 1);
                            if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, k1, 6);
                            if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, k1, 7);
                        }
                        debugprintln(this, "*** Engine (" + k1 + ") Controls: Hit, Engine Controls (Partially) Disabled..");
                        break;

                    case 11:
                    case 12:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            debugprintln(this, "*** Aileron Controls Out..");
                        }
                        break;

                    case 13:
                    case 14:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            debugprintln(this, "*** Elevator Controls Out..");
                        }
                        break;

                    case 15:
                    case 16:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            debugprintln(this, "*** Rudder Controls Out..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspark") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxbomb")) {
                if (World.Rnd().nextFloat() < 0.001F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
                    debugprintln(this, "*** Bomb Payload Detonates..");
                    this.FM.AS.hitTank(shot.initiator, 0, 10);
                    this.FM.AS.hitTank(shot.initiator, 1, 10);
                    this.FM.AS.hitTank(shot.initiator, 2, 10);
                    this.FM.AS.hitTank(shot.initiator, 3, 10);
                    this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int j = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 140000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                            debugprintln(this, "*** Engine (" + j + ") Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 85000F) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            debugprintln(this, "*** Engine (" + j + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.005F) this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                    else {
                        this.FM.EI.engines[j].setReadyness(shot.initiator, this.FM.EI.engines[j].getReadyness() - 0.00082F);
                        debugprintln(this, "*** Engine (" + j + ") Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 0.75F) {
                        this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        debugprintln(this, "*** Engine (" + j + ") Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 18000F) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            debugprintln(this, "*** Engine (" + j + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("mag1")) {
                    this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0);
                    debugprintln(this, "*** Engine (" + j + ") Module: Magneto #0 Destroyed..");
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("mag2")) {
                    this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1);
                    debugprintln(this, "*** Engine (" + j + ") Module: Magneto #1 Destroyed..");
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("prop") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    if (World.Rnd().nextFloat() < 0.5F) this.FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                    else this.FM.EI.engines[j].setKillPropAngleDeviceSpeeds(shot.initiator);
                    this.getEnergyPastArmor(15.1F, shot);
                    debugprintln(this, "*** Engine (" + j + ") Module: Prop Governor Fails..");
                }
                if (s.endsWith("oil1") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.AS.setOilState(shot.initiator, j, 1);
                    debugprintln(this, "*** Engine (" + j + ") Module: Oil Filter Pierced..");
                }
                if (s.endsWith("supc") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.EI.engines[j].setKillCompressor(shot.initiator);
                    debugprintln(this, "*** Engine (" + j + ") Module: Compressor Stops..");
                    this.getEnergyPastArmor(2.6F, shot);
                }
                return;
            }
            if (s.startsWith("xxoiltank")) {
                byte byte0 = 1;
                if (s.endsWith("2")) byte0 = 2;
                if (this.getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2435F) this.FM.AS.hitOil(shot.initiator, byte0);
                debugprintln(this, "*** Engine (" + byte0 + ") Module: Oil Tank Pierced..");
                return;
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.06F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[k] == 0) {
                        this.FM.AS.hitTank(shot.initiator, k, 1);
                        this.FM.AS.doSetTankState(shot.initiator, k, 1);
                    }
                    if (shot.powerType == 3) {
                        if (shot.power < 16100F) {
                            if (this.FM.AS.astateTankStates[k] < 4 && World.Rnd().nextFloat() < 0.21F) this.FM.AS.hitTank(shot.initiator, k, 1);
                        } else this.FM.AS.hitTank(shot.initiator, k, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                    } else if (shot.power > 16100F) this.FM.AS.hitTank(shot.initiator, k, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                }
                return;
            }
            if (s.startsWith("xxactu0")) {
                int l = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(6.3F, shot) > 0.0F && this.FM.AS.isMaster()) this.FM.turret[l].bIsOperable = false;
                return;
            }
            if (s.startsWith("xxammo")) {
                int i1 = 10 * (s.charAt(6) - 48) + s.charAt(7) - 48;
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F) {
                    switch (i1) {
                        case 1:
                            i1 = 10;
                            k1 = 0;
                            break;

                        case 2:
                            i1 = 10;
                            k1 = 1;
                            break;

                        case 3:
                            i1 = 11;
                            k1 = 0;
                            break;

                        case 4:
                            i1 = 12;
                            k1 = 0;
                            break;

                        case 5:
                            i1 = 13;
                            k1 = 0;
                            break;

                        case 6:
                            i1 = 13;
                            k1 = 1;
                            break;

                        case 7:
                            i1 = 14;
                            k1 = 0;
                            break;

                        case 8:
                            i1 = 14;
                            k1 = 1;
                            break;

                        case 9:
                            i1 = 15;
                            k1 = 0;
                            break;

                        case 10:
                            i1 = 16;
                            k1 = 0;
                            break;

                        case 11:
                            i1 = 17;
                            k1 = 0;
                            break;

                        case 12:
                            i1 = 17;
                            k1 = 1;
                            break;
                    }
                    this.FM.AS.setJamBullets(i1, k1);
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            return;
        }
        if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
            return;
        }
        if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
            return;
        }
        if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
            return;
        }
        if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
            return;
        }
        if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
            return;
        }
        if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
            return;
        }
        if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
            return;
        }
        if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            return;
        }
        if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            return;
        }
        if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
            return;
        }
        if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            return;
        }
        if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            return;
        }
        if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
            return;
        }
        if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 2) this.hitChunk("AroneL", shot);
            return;
        }
        if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 2) this.hitChunk("AroneR", shot);
            return;
        }
        if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
            return;
        }
        if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
            return;
        }
        if (s.startsWith("xengine3")) {
            if (this.chunkDamageVisible("Engine3") < 2) this.hitChunk("Engine3", shot);
            return;
        }
        if (s.startsWith("xengine4")) {
            if (this.chunkDamageVisible("Engine4") < 2) this.hitChunk("Engine4", shot);
            return;
        }
        if (s.startsWith("xgear")) {
            if (World.Rnd().nextFloat() < 0.05F) {
                debugprintln(this, "*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
            }
            return;
        }
        if (s.startsWith("xturret")) return;
        if (s.startsWith("xnose")) return;
        if (s.startsWith("xmgun")) {
            int j1 = 10 * (s.charAt(5) - 48) + s.charAt(6) - 48;
            if (this.getEnergyPastArmor(1.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F) {
                switch (j1) {
                    case 1:
                        j1 = 10;
                        k1 = 0;
                        break;

                    case 2:
                        j1 = 10;
                        k1 = 1;
                        break;

                    case 3:
                        j1 = 11;
                        k1 = 0;
                        break;

                    case 4:
                        j1 = 12;
                        k1 = 0;
                        break;

                    case 5:
                        j1 = 13;
                        k1 = 0;
                        break;

                    case 6:
                        j1 = 13;
                        k1 = 1;
                        break;

                    case 7:
                        j1 = 14;
                        k1 = 0;
                        break;

                    case 8:
                        j1 = 14;
                        k1 = 1;
                        break;

                    case 9:
                        j1 = 15;
                        k1 = 0;
                        break;

                    case 10:
                        j1 = 16;
                        k1 = 0;
                        break;

                    case 11:
                        j1 = 17;
                        k1 = 0;
                        break;

                    case 12:
                        j1 = 17;
                        k1 = 1;
                        break;
                }
                this.FM.AS.setJamBullets(j1, k1);
            }
            return;
        }
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte1 = 0;
            int l1;
            if (s.endsWith("a")) {
                byte1 = 1;
                l1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte1 = 2;
                l1 = s.charAt(6) - 49;
            } else l1 = s.charAt(5) - 49;
            this.hitFlesh(l1, shot, byte1);
            return;
        } else return;
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if (explosion.chunkName != null && explosion.power > 0.0F) {
            if (explosion.chunkName.equals("Tail1_D3")) return;
            if (explosion.chunkName.equals("WingLIn_D3")) return;
            if (explosion.chunkName.equals("WingRIn_D3")) return;
            if (explosion.chunkName.equals("WingLMid_D3")) return;
            if (explosion.chunkName.equals("WingRMid_D3")) return;
            if (explosion.chunkName.equals("WingLOut_D3")) return;
            if (explosion.chunkName.equals("WingROut_D3")) return;
        }
        super.msgExplosion(explosion);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 27F * f);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, getAngleValue(f, anglesL2));
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, getAngleValue(f, anglesL3));
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, getAngleValue(f, anglesL4));
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, -0.85F * f);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, 45F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, getAngleValue(f, anglesL2));
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, getAngleValue(f, anglesL3));
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, getAngleValue(f, anglesL4));
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, -0.85F * f);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, 45F * f);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                this.hitProp(1, j, actor);
                this.FM.EI.engines[1].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 9));
                // fall through

            case 34:
                this.hitProp(0, j, actor);
                this.FM.EI.engines[0].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(2, 8));
                this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 5));
                // fall through

            case 35:
                this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(0, 4));
                break;

            case 36:
                this.hitProp(2, j, actor);
                this.FM.EI.engines[2].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 9));
                // fall through

            case 37:
                this.hitProp(3, j, actor);
                this.FM.EI.engines[3].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 5));
                this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(2, 8));
                // fall through

            case 38:
                this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(0, 4));
                break;

            case 25:
                this.FM.turret[0].bIsOperable = false;
                return false;

            case 26:
                this.FM.turret[1].bIsOperable = false;
                return false;

            case 27:
                this.FM.turret[2].bIsOperable = false;
                return false;

            case 28:
                this.FM.turret[3].bIsOperable = false;
                return false;

            case 29:
                this.FM.turret[4].bIsOperable = false;
                return false;

            case 30:
                this.FM.turret[5].bIsOperable = false;
                return false;

            case 19:
                this.killPilot(this, 5);
                this.killPilot(this, 6);
                this.killPilot(this, 7);
                this.killPilot(this, 8);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, 85F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -85F * f, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 0, 1);
            if (this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 2, 1);
            if (this.FM.AS.astateEngineStates[3] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 3, 1);
            if (this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            if (this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            if (this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            if (this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
            if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) for (int i = 0; i < this.FM.EI.getNum(); i++)
                if (this.FM.AS.astateEngineStates[i] > 3 && World.Rnd().nextFloat() < 0.2F) this.FM.EI.engines[i].setExtinguisherFire();
        }
        for (int j = 1; j < 10; j++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + j + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + j + "_D0", this.hierMesh().isChunkVisible("Pilot" + j + "_D0"));

    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2:
                this.FM.turret[0].setHealth(f);
                break;

            case 3:
                this.FM.turret[1].setHealth(f);
                this.FM.turret[2].setHealth(f);
                break;

            case 4:
                this.FM.turret[3].setHealth(f);
                break;

            case 5:
                this.FM.turret[4].setHealth(f);
                break;

            case 6:
                this.FM.turret[6].setHealth(f);
                break;

            case 7:
                this.FM.turret[5].setHealth(f);
                break;

            case 8:
                this.FM.turret[7].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        this.hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
        this.hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
    }

    private static final float anglesL2[] = { 0.0F, 0.0F, 0.2833333F, -19.025F, 0.6666666F, -55.467F, 0.8166666F, -71.339F, 1.0F, -79.5F };
    private static final float anglesL3[] = { 0.0F, 0.0F, 0.3333333F, 2.333F, 0.6666666F, -36.833F, 0.8166666F, -70.944F, 1.0F, -110F };
    private static final float anglesL4[] = { 0.0F, 0.0F, 0.1666667F, 8.5F, 0.5F, 27.5F, 0.8333333F, 49.5F, 1.0F, 61F };

    static {
        Class class1 = B_17.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
