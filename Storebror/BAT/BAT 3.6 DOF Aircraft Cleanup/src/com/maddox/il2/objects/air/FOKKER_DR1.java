package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class FOKKER_DR1 extends Scheme1 implements TypeScout, TypeFighter, TypeTNBFighter {

    public FOKKER_DR1() {
        this.oldProp = new int[6];
        this.tmpPoint = new Point3d();
        this.tmpVector = new Vector3d();
        this.clearedTakeoff = false;
        this.timerTakeoff = -1L;
        this.bChangedPit = true;
        this.syncMechDmg = false;
    }

    protected void moveFlap(float f1) {
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 20F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("GearC2_D0", 20F * f, 0.0F, 0.0F);
    }

    public void moveSteering(float f1) {
    }

    public void moveWheelSink() {
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 19:
                this.FM.Gears.hitCentreGear();
                break;

            case 9:
                if (this.hierMesh().chunkFindCheck("GearL2_D0") != -1) {
                    this.hierMesh().hideSubTrees("GearL2_D0");
                    Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("GearL2_D0"));
                    wreckage.collide(true);
                    this.FM.Gears.hitLeftGear();
                }
                break;

            case 10:
                if (this.hierMesh().chunkFindCheck("GearR2_D0") != -1) {
                    this.hierMesh().hideSubTrees("GearR2_D0");
                    Wreckage wreckage1 = new Wreckage(this, this.hierMesh().chunkFind("GearR2_D0"));
                    wreckage1.collide(true);
                    this.FM.Gears.hitRightGear();
                }
                break;

            case 3:
                if (World.Rnd().nextInt(0, 99) < 1) {
                    this.FM.AS.hitEngine(this, 0, 4);
                    this.hitProp(0, j, actor);
                    this.FM.EI.engines[0].setEngineStuck(actor);
                    return this.cut("engine1");
                } else {
                    this.FM.AS.setEngineDies(this, 0);
                    return false;
                }
        }
        return super.cutFM(i, j, actor);
    }

    public boolean cut(String s) {
        boolean flag = super.cut(s);
        return flag;
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && (this.FM.AS.astateEngineStates[0] > 3) && (World.Rnd().nextFloat() < 0.4F)) {
            this.FM.EI.engines[0].setExtinguisherFire();
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D1", true);
                break;
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxcontrols")) {
                if (s.endsWith("8")) {
                    if (World.Rnd().nextFloat() < 0.2F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#8)");
                    }
                } else if (s.endsWith("9")) {
                    if (World.Rnd().nextFloat() < 0.2F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#9)");
                    }
                    if (World.Rnd().nextFloat() < 0.2F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Arone Controls Out.. (#9)");
                    }
                } else if ((s.endsWith("2") || s.endsWith("4")) && (World.Rnd().nextFloat() < 0.5F)) {
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Arone Controls Out.. (#2/#4)");
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("prop")) {
                    Aircraft.debugprintln(this, "*** Prop hit");
                } else if (s.endsWith("case")) {
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
                } else if (s.endsWith("cyl1") || s.endsWith("cyl2")) {
                    if (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.12F)) {
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
                    }
                } else if (s.endsWith("sync")) {
                    Aircraft.debugprintln(this, "*** Engine Module: Gun Synchronized Hit, Nose Guns Lose Authority..");
                    this.syncMechDmg = true;
                } else if (s.endsWith("oil1")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
            } else if (s.endsWith("gear")) {
                Aircraft.debugprintln(this, "*** Engine Module: Gear Hit..");
                if ((this.getEnergyPastArmor(2.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.05F)) {
                    this.FM.AS.setEngineStuck(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: gear hit, engine stuck..");
                }
            } else if (s.startsWith("xxtank")) {
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.99F)) {
                    if (this.FM.AS.astateTankStates[0] == 0) {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 0, 2);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 1.25F)) {
                        this.FM.AS.hitTank(shot.initiator, 0, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
            } else if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                } else if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                } else if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
            } else if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    Aircraft.debugprintln(this, "*** Fixed Gun #1: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    Aircraft.debugprintln(this, "*** Fixed Gun #2: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                if (s.endsWith("03")) {
                    Aircraft.debugprintln(this, "*** Fixed Gun #3: Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.endsWith("04")) {
                    Aircraft.debugprintln(this, "*** Fixed Gun #4: Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            } else if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2)) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                } else if (s.startsWith("xxsparli") && (World.Rnd().nextFloat() < 0.25F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                } else if (s.startsWith("xxsparri") && (World.Rnd().nextFloat() < 0.25F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                } else if (s.startsWith("xxsparlm") && (World.Rnd().nextFloat() < 0.25F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                } else if (s.startsWith("xxsparrm") && (World.Rnd().nextFloat() < 0.25F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                }
            }
            return;
        }
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i;
            if (s.endsWith("a")) {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else {
                i = s.charAt(5) - 49;
            }
            Aircraft.debugprintln(this, "*** hitFlesh..");
            this.hitFlesh(i, shot, byte0);
        } else if (s.startsWith("xcockpit")) {
            if (World.Rnd().nextFloat() < 0.2F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            } else if (World.Rnd().nextFloat() < 0.4F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            } else if (World.Rnd().nextFloat() < 0.6F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            } else if (World.Rnd().nextFloat() < 0.8F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            } else {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
        } else if (s.startsWith("xcf")) {
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
            if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 2)) {
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
            Aircraft.debugprintln(this, "*** xWing: " + s);
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
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
                this.hitChunk("AroneR", shot);
            }
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if ((!super.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver) && (this.FM.Skill == 3)) {
            this.hierMesh().chunkVisible("OverlayVoss", true);
        } else if (this.FM.isPlayers()) {
            this.FM.Skill = 3;
            this.hierMesh().chunkVisible("OverlayVoss", true);
        }
    }

    protected void moveFan(float f) {
        int i = 0;
        int j = 0;
        int k = 1;
        if (this.oldProp[j] < 2) {
            i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
            if (i >= 1) {
                i = 1;
            }
            if ((i != this.oldProp[j]) && this.hierMesh().isChunkVisible(FOKKER_DR1.Props[j][this.oldProp[j]])) {
                this.hierMesh().chunkVisible(FOKKER_DR1.Props[0][this.oldProp[j]], false);
                this.oldProp[j] = i;
                this.hierMesh().chunkVisible(FOKKER_DR1.Props[j][i], true);
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
        this.hierMesh().chunkSetAngles(FOKKER_DR1.Props[j][0], 0.0F, 0.0F, -this.propPos[j]);
        if (this.oldProp[k] < 2) {
            i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
            if (i >= 1) {
                i = 1;
            }
            if ((i != this.oldProp[k]) && this.hierMesh().isChunkVisible(FOKKER_DR1.Props[k][this.oldProp[k]])) {
                this.hierMesh().chunkVisible(FOKKER_DR1.Props[k][this.oldProp[k]], false);
                this.oldProp[k] = i;
                this.hierMesh().chunkVisible(FOKKER_DR1.Props[k][i], true);
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
        this.hierMesh().chunkSetAngles(FOKKER_DR1.Props[k][0], 0.0F, 0.0F, -this.propPos[k]);
    }

    public void doSetSootState(int i, int j) {
        for (int k = 0; k < 2; k++) {
            if (this.FM.AS.astateSootEffects[i][k] != null) {
                Eff3DActor.finish(this.FM.AS.astateSootEffects[i][k]);
            }
            this.FM.AS.astateSootEffects[i][k] = null;
        }

        switch (j) {
            case 1:
            case 2:
            case 3:
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("HolyGrail01"), null, 1.0F, "3DO/Effects/Aircraft/EngineBlackSmall.eff", -1F);
                break;

            case 4:
            case 5:
            default:
                return;
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.Gears.onGround()) {
            this.FM.AS.bIsEnableToBailout = true;
        } else {
            this.FM.AS.bIsEnableToBailout = false;
        }
        float f1 = Atmosphere.temperature((float) this.FM.Loc.z) - 273.15F;
        float f2 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeedKMH());
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        float f3 = (((this.FM.EI.engines[0].getControlRadiator() * f * f2) / (f2 + 50F)) * (this.FM.EI.engines[0].tWaterOut - f1)) / 256F;
        this.FM.EI.engines[0].tWaterOut -= f3;
        if ((this.FM.EI.engines[0].getStage() == 6) && (this.FM.EI.engines[0].getControlThrottle() > 0.2F)) {
            this.FM.AS.setSootState(this, 0, 3);
        } else {
            this.FM.AS.setSootState(this, 0, 0);
        }
        if ((this.FM.EI.engines[0].getStage() == 6) && ((this.FM.CT.getMagnetoControl() == 2.0F) || (this.FM.CT.getMagnetoControl() == 1.0F))) {
            this.FM.EI.engines[0].setControlThrottle(0.33F);
        } else if ((this.FM.EI.engines[0].getStage() == 6) && (this.FM.CT.getMagnetoControl() == 3F)) {
            this.FM.EI.engines[0].setControlThrottle(1.0F);
        }
        this.gunSync();
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
            if (!this.clearedTakeoff && this.FM.Gears.onGround() && (this.FM.getSpeedKMH() < 5F)) {
                this.FM.brakeShoe = true;
            } else if (this.clearedTakeoff || !this.FM.Gears.onGround()) {
                this.FM.brakeShoe = false;
            }
            if ((this.FM.EI.engines[0].getStage() == 6) && (this.FM.CT.getMagnetoControl() == 3F)) {
                if (!this.FM.AP.way.isLanding()) {
                    this.FM.CT.PowerControl = 1.0F;
                } else {
                    this.FM.CT.setMixControl(0.2F);
                }
                if (!this.clearedTakeoff && (this.timerTakeoff == -1L)) {
                    this.timerTakeoff = Time.current() + 60000L;
                }
                this.tmpPoint.set(this.pos.getAbsPoint());
                this.tmpVector.set(60D, 0.0D, 0.0D);
                this.pos.getAbsOrient().transform(this.tmpVector);
                this.tmpPoint.add(this.tmpVector);
                Aircraft.Pd.set(this.tmpPoint);
                if ((Time.current() > this.timerTakeoff) && (War.getNearestFriendAtPoint(Aircraft.Pd, this, 50F) == null)) {
                    this.clearedTakeoff = true;
                }
            }
        }
    }

    private void gunSync() {
        if (this.syncMechDmg && this.FM.CT.WeaponControl[0] && (this.FM.EI.engines[0].getStage() == 6) && (this.FM.CT.Weapons[0][0].countBullets() != 0) && (World.Rnd().nextFloat() < 0.005F)) {
            Aircraft.debugprintln(this, "*** Prop hit");
            this.hierMesh().chunkVisible("PropRot1_D0", false);
            this.hierMesh().chunkVisible("Prop1_D1", true);
            this.FM.EI.engines[0].setEngineDies(this);
            this.FM.AS.setJamBullets(0, 0);
            this.FM.AS.setJamBullets(0, 1);
            this.FM.AS.setJamBullets(0, 2);
        }
    }

    protected float               kangle;
    public boolean                syncMechDmg;
    public boolean                bChangedPit;
    protected int                 oldProp[];
    protected static final String Props[][] = { { "Prop1_D0", "PropRot1_D0", "Prop1_D1" }, { "Prop2_D0", "PropRot2_D0", "Prop2_D1" }, { "Prop3_D0", "PropRot3_D0", "Prop3_D1" }, { "Prop4_D0", "PropRot4_D0", "Prop4_D1" }, { "Prop5_D0", "PropRot5_D0", "Prop5_D1" }, { "Prop6_D0", "PropRot6_D0", "Prop6_D1" } };
    private boolean               clearedTakeoff;
    private long                  timerTakeoff;
    private Point3d               tmpPoint;
    private Vector3d              tmpVector;

    static {
        Class class1 = FOKKER_DR1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Fokker");
        Property.set(class1, "meshName", "3DO/Plane/FokkerDr1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParWW1());
        Property.set(class1, "yearService", 1916F);
        Property.set(class1, "yearExpired", 1918F);
        Property.set(class1, "FlightModel", "FlightModels/FokkerDr1.fmd:FOKKER");
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "LOSElevation", 0.66F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitFOKKERDR1.class });
        Aircraft.weaponTriggersRegister(class1, new int[2]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
