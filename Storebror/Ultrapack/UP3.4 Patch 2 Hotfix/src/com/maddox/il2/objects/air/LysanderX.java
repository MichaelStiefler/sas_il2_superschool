package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class LysanderX extends Scheme1 {

    public LysanderX() {
        this.hasSkis = false;
        this.setSuspension(0.0F);
        this.skiAngleL = 0.0F;
        this.skiAngleR = 0.0F;
        this.spring = 0.0F;
        this.arrestor = 0.0F;
    }

    protected void moveAileron(float f) {
        if (this.FM.CT.getWing() < 0.01F) {
            this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
        }
    }

    protected void moveFlap1(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -60F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -60F * f, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -29F * f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                break;
        }
    }

    public void doKillPilot(int i) {
        if (i == 1) this.FM.turret[0].bIsOperable = false;
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                if (!this.FM.AS.bIsAboutToBailout) this.hierMesh().chunkVisible("Gore1_D0", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                if (!this.FM.AS.bIsAboutToBailout) this.hierMesh().chunkVisible("Gore2_D0", true);
                break;
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 1.0F, 0.0F, -0.56F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        // Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.98F);
        // Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F);
        // Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.15F);
        // hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.32F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.78F);
        this.hierMesh().chunkSetLocate("BlisterL_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.14F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.39F);
        this.hierMesh().chunkSetLocate("BlisterL2_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.06F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.23F);
        this.hierMesh().chunkSetLocate("BlisterL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.28F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.78F);
        this.hierMesh().chunkSetLocate("BlisterR_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.14F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.39F);
        this.hierMesh().chunkSetLocate("BlisterR2_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.06F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.23F);
        this.hierMesh().chunkSetLocate("BlisterR3_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxcontrols")) {
                if (s.endsWith("s1") && this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
                    debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                }
                if (s.endsWith("s2") && this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                }
                if (s.endsWith("s3") && this.getEnergyPastArmor(6.8F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                    debugprintln(this, "*** Elevator Crank: Hit, Controls Destroyed..");
                }
                if ((s.endsWith("s4") || s.endsWith("s5")) && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                    debugprintln(this, "*** Evelator Controls Out..");
                }
                if ((s.endsWith("s6") || s.endsWith("s7") || s.endsWith("s10") || s.endsWith("s11")) && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                    debugprintln(this, "*** Aileron Controls Out..");
                }
                if ((s.endsWith("s8") || s.endsWith("s9")) && this.getEnergyPastArmor(6.75F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                    debugprintln(this, "*** Aileron Cranks Destroyed..");
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 140000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 85000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.01F) this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    else {
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.002F);
                        debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.75F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 48000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.startsWith("xxeng1mag")) {
                    int i = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                }
                if (s.endsWith("oil1")) {
                    if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if ((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.5F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F) {
                    debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if ((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if ((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if ((s.endsWith("lo1") || s.endsWith("lo2")) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if ((s.endsWith("ro1") || s.endsWith("ro2")) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitTank(shot.initiator, j, 1);
                    if (World.Rnd().nextFloat() < 0.05F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F) this.FM.AS.hitTank(shot.initiator, j, 2);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxmgun01")) {
                if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                    this.FM.AS.setJamBullets(0, 0);
                    this.getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if (s.startsWith("xxmgun02")) {
                if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                    this.FM.AS.setJamBullets(0, 1);
                    this.getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if (s.startsWith("xxoil")) return;
            else return;
        }
        if (s.startsWith("xcf")) {
            this.hitChunk("CF", shot);
            if (point3d.x > 0.305D && point3d.x < 1.597D) {
                if (point3d.x > 1.202D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                if (v1.x < -0.8D && World.Rnd().nextFloat() < 0.2F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                if (point3d.z > 0.577D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                else if (point3d.y > 0.0D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
            }
        } else if (!s.startsWith("xblister")) if (s.startsWith("xeng")) this.hitChunk("Engine1", shot);
        else if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) this.hitChunk("Keel1", shot);
        else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
            if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
            if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 2) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneR") < 2) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xflap")) {
            if (s.startsWith("xflapl") && this.chunkDamageVisible("Flap01_D0") < 2) this.hitChunk("Flap01_D0", shot);
            if (s.startsWith("xflapr") && this.chunkDamageVisible("Flap02_D0") < 2) this.hitChunk("Flap02_D0", shot);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k;
            if (s.endsWith("a")) {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else k = s.charAt(5) - 49;
            this.hitFlesh(k, shot, byte0);
        }

    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 34:
                return super.cutFM(35, j, actor);
            case 37:
                return super.cutFM(38, j, actor);
            case 19:
                this.FM.CT.bHasArrestorControl = false;
                // fall through
            case 11:
                this.hierMesh().chunkVisible("Wire_D0", false);
                // fall through
            default:
                return super.cutFM(i, j, actor);
        }
    }

    public void moveSteering(float f) {
        if (f > 66.5F) {
            f = 66.5F;
            this.FM.Gears.steerAngle = f;
        }
        if (f < -66.5F) {
            f = -66.5F;
            this.FM.Gears.steerAngle = f;
        }
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 60F * f, 0.0F);
        this.arrestor = f;
    }

    public void update(float f) {
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatLInner_D0", 0.0F, 0.0F, cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
            this.hierMesh().chunkSetAngles("SlatLOuter_D0", 0.0F, 0.0F, cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
            this.hierMesh().chunkSetAngles("SlatRInner_D0", 0.0F, 0.0F, cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
            this.hierMesh().chunkSetAngles("SlatROuter_D0", 0.0F, 0.0F, cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F));
        }
        super.update(f);
        if (this.FM.AS.isMaster() && (!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && this.FM.Gears.onGround()) {
            if (this.FM.getSpeedKMH() < 20.0F) {
              this.FM.CT.cockpitDoorControl = 1.0F;
            } else {
              this.FM.CT.cockpitDoorControl = 0.0F;
            }
        }
        if (this.FM.Gears.arrestorVAngle != 0.0F) {
            float f1 = cvt(this.FM.Gears.arrestorVAngle, -44F, 9F, 1.0F, 0.0F);
            this.arrestor = 0.8F * this.arrestor + 0.2F * f1;
        } else {
            float f2 = -28F * this.FM.Gears.arrestorVSink / 53F;
            if (f2 < 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if (f2 > 0.15F) f2 = 0.15F;
            this.arrestor = 0.3F * this.arrestor + 0.7F * (this.arrestor + f2);
            if (this.arrestor < 0.0F) this.arrestor = 0.0F;
        }
        if (this.arrestor > this.FM.CT.getArrestor()) this.arrestor = this.FM.CT.getArrestor();
        this.moveArrestorHook(this.arrestor);
        float f3 = this.FM.EI.engines[0].getControlRadiator();
        float flapps = 0;
        if (Math.abs(flapps - f3) > 0.02F) {
            flapps = f3;
            for (int i = 1; i < 11; i++)
                this.hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, 22F * f3, 0.0F);

        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER() && World.cur().camouflage == 1) {
            this.hierMesh().chunkVisible("GearL1_D0", false);
            this.hierMesh().chunkVisible("GearL11_D0", true);
            this.hierMesh().chunkVisible("GearR1_D0", false);
            this.hierMesh().chunkVisible("GearR11_D0", true);
            this.hierMesh().chunkVisible("GearC1_D0", false);
            this.hierMesh().chunkVisible("GearC11_D0", true);
            this.FM.CT.bHasBrakeControl = false;
        }
        if (World.cur().camouflage == 2 || World.cur().camouflage == 6) this.hierMesh().chunkVisible("SandFilter_D0", true);
    }

//	protected void moveFan(float f) {
//		if (Config.isUSE_RENDER()) {
//			super.moveFan(f);
//		}
//	}

    public void moveWheelSink() {
        if (World.cur().camouflage == 1) {
            float f1 = Aircraft.cvt(this.FM.getSpeed(), 30F, 75F, 1.0F, 0.0F);
            float f2 = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 30F, 0.0F, 0.5F);
            if (this.FM.Gears.gWheelSinking[0] > 0.0F) {
                this.skiAngleL = 0.5F * this.skiAngleL + 0.5F * this.FM.Or.getTangage();
                if (this.skiAngleL > 20F) this.skiAngleL = this.skiAngleL - this.spring;
                this.hierMesh().chunkSetAngles("GearL11_D0", World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) + this.skiAngleL, World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(f2, f2));
                if (this.FM.Gears.gWheelSinking[1] == 0.0F && this.FM.Or.getRoll() < 365F && this.FM.Or.getRoll() > 355F) {
                    this.skiAngleR = this.skiAngleL;
                    this.hierMesh().chunkSetAngles("GearR11_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleR);
                }
            } else {
                if (this.skiAngleL > f1 * -10F + 0.01D) this.skiAngleL = this.skiAngleL - this.spring;
                else if (this.skiAngleL < f1 * -10F - 0.01D) this.skiAngleL = this.skiAngleL + this.spring;
                this.hierMesh().chunkSetAngles("GearL11_D0", this.skiAngleL, 0.0F, 0.0F);
            }
            if (this.FM.Gears.gWheelSinking[1] > 0.0F) {
                this.skiAngleR = 0.5F * this.skiAngleR + 0.5F * this.FM.Or.getTangage();
                if (this.skiAngleR > 20F) this.skiAngleR = this.skiAngleR - this.spring;
                this.hierMesh().chunkSetAngles("GearR11_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleR);
            } else {
                if (this.skiAngleR > f1 * -10F + 0.01D) this.skiAngleR = this.skiAngleR - this.spring;
                else if (this.skiAngleR < f1 * -10F - 0.01D) this.skiAngleR = this.skiAngleR + this.spring;
                this.hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, -this.skiAngleR);
            }
            this.hierMesh().chunkSetAngles("GearC11_D0", (this.skiAngleL + this.skiAngleR) / 2.0F, 0.0F, 0.0F);
        }
    }

    public void sfxWheels() {
        if (!this.hasSkis) super.sfxWheels();
    }

    public float getSuspension() {
        return this.suspension;
    }

    public void setSuspension(float suspension) {
        this.suspension = suspension;
    }

    protected void moveFlap(float f) {
        float f1 = -60F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);

        this.FM.AS.hitTank(shot.initiator, 1, (int) (1.0F + shot.mass * 18.95F * 2.0F));
        if (shot.chunkName.startsWith("Engine")) {
            if (World.Rnd().nextFloat(0.0F, 1.0F) < shot.mass) this.FM.AS.hitEngine(shot.initiator, 0, 1);
            if (Aircraft.v1.z > 0.0D && World.Rnd().nextFloat() < 0.12F) {
                this.FM.AS.setEngineDies(shot.initiator, 0);
                if (shot.mass > 0.1F) this.FM.AS.hitEngine(shot.initiator, 0, 5);
            }
            if (Aircraft.v1.x < 0.1D && World.Rnd().nextFloat() < 0.57F) this.FM.AS.hitOil(shot.initiator, 0);
        }
        if (shot.chunkName.startsWith("Pilot1")) {
            this.killPilot(shot.initiator, 0);
            this.FM.setCapableOfBMP(false, shot.initiator);
            if (Aircraft.Pd.z > 0.5D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
        } else if (shot.chunkName.startsWith("Pilot2")) {
            this.killPilot(shot.initiator, 1);
            if (Aircraft.Pd.z > 0.5D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
        } else {
            if (shot.chunkName.startsWith("Turret")) this.FM.turret[0].bIsOperable = false;
            if (this.FM.AS.astateEngineStates[0] == 4 && World.Rnd().nextInt(0, 99) < 33) this.FM.setCapableOfBMP(false, shot.initiator);
            super.msgShot(shot);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        if (f < -45F) {
            f = -45F;
            flag = false;
        }
        if (f > 45F) {
            f = 45F;
            flag = false;
        }
        if (f1 < -45F) {
            f1 = -45F;
            flag = false;
        }
        if (f1 > 20F) {
            f1 = 20F;
            flag = false;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberReplicateFromNet(NetMsgInput arg0) throws IOException {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted arg0) throws IOException {
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberUpdate(float arg0) {
    }

    public float getArrestor() {
        return this.arrestor;
    }

    public void setArrestor(float arrestor) {
        this.arrestor = arrestor;
    }

    public boolean hasSkis;
    private float  suspension;
    private float  skiAngleL;
    private float  skiAngleR;
    private float  spring;
    float          arrestor;

    static {
        Class class1 = LysanderX.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
