package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

/*
 * Sunderland base class by Barnesy/CWatson/Freemodding
 * Latest edit: 2016-03-11 by SAS~Storebror
 */
public abstract class Sunderlandxyz extends Scheme4 implements TypeSailPlane, TypeSeaPlane, TypeTransport, TypeBomber {

    public Sunderlandxyz() {
        this.cfDamageVisible = 0;
        this.tail1DamageVisible = 0;
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 2; j++)
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }

    }

    protected void moveFlap(float f) {
        float f1 = -45.5F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxammo")) {
                int i = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.15F) this.FM.AS.setJamBullets(10 + i, 0);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int j = s.charAt(10) - 48;
                switch (j) {
                    default:
                        break;

                    case 1:
                    case 2:
                    case 3:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                        }
                        break;

                    case 6:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) this.FM.AS.setControlsDamage(shot.initiator, 1);
                        break;

                    case 4:
                    case 5:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) this.FM.AS.setControlsDamage(shot.initiator, 2);
                        break;
                }
                return;
            }
            if (s.startsWith("xxengine")) {
                int k = s.charAt(8) - 49;
                if (s.endsWith("base")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 140000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, k);
                            Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 85000F) {
                            this.FM.AS.hitEngine(shot.initiator, k, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.005F) this.FM.EI.engines[k].setCyliderKnockOut(shot.initiator, 1);
                    else {
                        this.FM.EI.engines[k].setReadyness(shot.initiator, this.FM.EI.engines[k].getReadyness() - 0.00082F);
                        Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[k].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyl")) {
                    if (this.getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[k].getCylindersRatio() * 0.75F) {
                        this.FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine (" + k + ") Cylinders Hit, " + this.FM.EI.engines[k].getCylindersOperable() + "/" + this.FM.EI.engines[k].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 18000F) {
                            this.FM.AS.hitEngine(shot.initiator, k, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + k + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("sup") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.EI.engines[k].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Compressor Stops..");
                    this.getEnergyPastArmor(2.6F, shot);
                }
                return;
            }
            if (s.startsWith("xxgun")) {
                int l = s.charAt(5) - 49;
                if (this.getEnergyPastArmor(5.7F, shot) > 0.0F && World.Rnd().nextFloat() < 0.96F) {
                    this.FM.AS.setJamBullets(10 + l, 0);
                    this.getEnergyPastArmor(29.95F, shot);
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
            if (s.startsWith("xxoil")) {
                int i1 = s.charAt(5) - 49;
                if (this.getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2435F) this.FM.AS.hitOil(shot.initiator, i1);
                Aircraft.debugprintln(this, "*** Engine (" + i1 + ") Module: Oil Tank Pierced..");
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspark") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F) {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
                }
                if (s.startsWith("xxsparsl") && this.chunkDamageVisible("StabL") > 1 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F) {
                    Aircraft.debugprintln(this, "*** StabL: Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D" + this.chunkDamageVisible("StabL"), shot.initiator);
                }
                if (s.startsWith("xxsparsr") && this.chunkDamageVisible("StabR") > 1 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F) {
                    Aircraft.debugprintln(this, "*** StabR: Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D" + this.chunkDamageVisible("StabR"), shot.initiator);
                }
                if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F) {
                    Aircraft.debugprintln(this, "*** Tail1: Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Tail1_D" + this.chunkDamageVisible("Tail1"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j1 = s.charAt(6) - 48;
                if (s.length() == 8) j1 = 10;
                switch (j1) {
                    case 1:
                    case 2:
                    case 3:
                        this.doHitMeATank(shot, 2);
                        break;

                    case 4:
                    case 5:
                    case 6:
                        this.doHitMeATank(shot, 3);
                        break;

                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        this.doHitMeATank(shot, World.Rnd().nextInt(0, 1));
                        break;
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
            if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
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
            if (this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
            return;
        }
        if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
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
            if (this.chunkDamageVisible("AroneL") < 1) this.hitChunk("AroneL", shot);
            return;
        }
        if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 1) this.hitChunk("AroneR", shot);
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
        if (s.startsWith("xnose")) {
            if (this.chunkDamageVisible("Nose") < 2) this.hitChunk("Nose", shot);
            return;
        }
        if (s.startsWith("xgearr")) {
            this.hitChunk("GearR2", shot);
            return;
        }
        if (s.startsWith("xgearl")) {
            this.hitChunk("GearL2", shot);
            return;
        }
        if (s.startsWith("xturret")) return;
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k1;
            if (s.endsWith("a")) {
                byte0 = 1;
                k1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k1 = s.charAt(6) - 49;
            } else k1 = s.charAt(5) - 49;
            this.hitFlesh(k1, shot, byte0);
            return;
        } else return;
    }

    private final void doHitMeATank(Shot shot, int i) {
        if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) if (shot.power < 14100F) {
            if (shot.powerType == 3 && this.FM.AS.astateTankStates[i] > 0 && World.Rnd().nextFloat() < 0.25F) this.FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(1, 2));
            else if (this.FM.AS.astateTankStates[i] == 0) {
                this.FM.AS.hitTank(shot.initiator, i, 1);
                this.FM.AS.doSetTankState(shot.initiator, i, 1);
            }
        } else this.FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int) (shot.power / 56000F)));
    }

    protected boolean cutFM(int i, int j, Actor actor) {
//        System.out.println("### Sunderland MkII cutFM(" + i + ", " + j + ", " + (actor==null?"null":actor.getClass().getName()) + ")");
        switch (i) {
            case Aircraft._TAIL_1:
                this.killPilot(this, 5);
                break;

            case Aircraft._NOSE:
                this.killPilot(this, 2);
                break;

            case Aircraft._WING_ROOT_L:
                this.hitProp(1, j, actor);
                // fall through, cutting wing root cuts wing mid and end section as well

            case Aircraft._WING_MIDDLE_L:
                this.hitProp(0, j, actor);
                // fall through, cutting wing mid cuts end section as well

            case Aircraft._WING_END_L:
                this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(0, 5));
                break;

            case Aircraft._WING_ROOT_R:
                this.hitProp(2, j, actor);
                // fall through, cutting wing root cuts wing mid section as well

            case Aircraft._WING_MIDDLE_R:
                this.hitProp(3, j, actor);
                // fall through, cutting wing mid cuts end section as well

            case Aircraft._WING_END_R:
                this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(0, 5));
                break;

            case Aircraft._TURRET_1:
                this.FM.turret[0].bIsOperable = false;
                break;

            case Aircraft._TURRET_2:
                this.FM.turret[1].bIsOperable = false;
                break;
            case Aircraft._TURRET_3:
                this.FM.turret[2].bIsOperable = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public abstract void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException;

    public abstract void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException;

    public abstract void typeBomberUpdate(float f);

    public abstract void typeBomberAdjSpeedMinus();

    public abstract void typeBomberAdjSpeedPlus();

    public abstract void typeBomberAdjSpeedReset();

    public abstract void typeBomberAdjAltitudeMinus();

    public abstract void typeBomberAdjAltitudePlus();

    public abstract void typeBomberAdjAltitudeReset();

    public abstract void typeBomberAdjSideslipMinus();

    public abstract void typeBomberAdjSideslipPlus();

    public abstract void typeBomberAdjSideslipReset();

    public abstract void typeBomberAdjDistanceMinus();

    public abstract void typeBomberAdjDistancePlus();

    public abstract void typeBomberAdjDistanceReset();

    public abstract boolean typeBomberToggleAutomation();

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (s.startsWith("CF") || s.startsWith("Tail1")) if (this == World.getPlayerAircraft()) this.updateDamageState(); // Keep track of fuselage and tail damage states, this is required to hide and show them properly player enters/leaves turrets
    }

    protected void updateDamageState() {
        // Keep track of fuselage and tail damage states, this is required to hide and show them properly player enters/leaves turrets
        this.cfDamageVisible = this.chunkDamageVisible("CF");
        this.tail1DamageVisible = this.chunkDamageVisible("Tail1");
    }

    // Encapsulate fuselage and tail damage states
    private int cfDamageVisible    = 0;
    private int tail1DamageVisible = 0;

    // +++ Getters and Setters for fuselage and tail damage state
    public int getTail1DamageVisible() {
        return this.tail1DamageVisible;
    }

    public void setTail1DamageVisible(int tail1DamageVisible) {
        this.tail1DamageVisible = tail1DamageVisible;
    }

    public int getCfDamageVisible() {
        return this.cfDamageVisible;
    }

    public void setCfDamageVisible(int cfDamageVisible) {
        this.cfDamageVisible = cfDamageVisible;
    }
    // ---

    private static Point3d tmpp = new Point3d();
    static {
        Class class1 = Sunderlandxyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
