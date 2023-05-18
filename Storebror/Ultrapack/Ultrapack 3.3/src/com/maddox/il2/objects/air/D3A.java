package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class D3A extends Scheme1 implements TypeStormovik {

    public D3A() {
        this.arrestor = 0.0F;
        this.flapps = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
    }

    protected void moveAileron(float f) {
        if (this.FM.CT.getWing() < 0.01F) {
            this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
        }
        this.hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -38F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 90F * f, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
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
                if ((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if ((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
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
            if (s.startsWith("xWingLIn") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xWingRIn") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xWingLMid")) {
                if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
                if (World.Rnd().nextFloat() < shot.mass + 0.02F) this.FM.AS.hitOil(shot.initiator, 0);
            }
            if (s.startsWith("xWingRMid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xWingLOut") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xWingROut") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
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

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, 0.0F, 110F * f);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, -110F * f, 0.0F);
    }

    public void moveWingFold(float f) {
        this.moveWingFold(this.hierMesh(), f);
        this.FM.doRequestFMSFX(1, (int) cvt(f, 0.1F, 1.0F, 0.0F, 40F));
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
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
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 60F * f, 0.0F);
        this.arrestor = f;
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.CT.getArrestor() > 0.2F) if (this.FM.Gears.arrestorVAngle != 0.0F) {
            float f1 = cvt(this.FM.Gears.arrestorVAngle, -52F, 8F, 1.0F, 0.0F);
            this.arrestor = 0.8F * this.arrestor + 0.2F * f1;
            this.moveArrestorHook(this.arrestor);
        } else {
            float f2 = -76F * this.FM.Gears.arrestorVSink / 60F;
            if (f2 < 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if (f2 > 0.0F && this.FM.CT.getArrestor() < 0.95F) f2 = 0.0F;
            if (f2 > 0.2F) f2 = 0.2F;
            if (f2 > 0.0F) this.arrestor = 0.7F * this.arrestor + 0.3F * (this.arrestor + f2);
            else this.arrestor = 0.3F * this.arrestor + 0.7F * (this.arrestor + f2);
            if (this.arrestor < 0.0F) this.arrestor = 0.0F;
            else if (this.arrestor > 1.0F) this.arrestor = 1.0F;
            this.moveArrestorHook(this.arrestor);
        }
        float f3 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f3) > 0.01F) {
            this.flapps = f3;
            for (int i = 1; i < 9; i++)
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, 22F * f3, 0.0F);

        }
    }

    private float arrestor;
    private float flapps;

    static {
        Class class1 = D3A.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
