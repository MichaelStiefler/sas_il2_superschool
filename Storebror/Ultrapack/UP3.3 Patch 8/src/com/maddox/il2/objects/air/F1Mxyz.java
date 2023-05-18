
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.Property;

public class F1Mxyz extends Scheme1 implements TypeSailPlane, TypeScout, TypeStormovik, TypeFighter {

    public F1Mxyz() {
        this.flapps = 0.0F;
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 25F * f, 0.0F, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, 25F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, 25F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, -20F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, 20F * f);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap1_D0", 0.0F, 0.0F, -45F * f);
        this.hierMesh().chunkSetAngles("Flap2_D0", 0.0F, 0.0F, -45F * f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (super.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    F1Mxyz.tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    F1Mxyz.tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(F1Mxyz.tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }
            }

        }

    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0: // '\0'
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1: // '\001'
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0: // '\0'
                if (f < -28F) {
                    f = -28F;
                    flag = false;
                }
                if (f > 28F) {
                    f = 28F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                super.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxcontrols")) {
                if (s.endsWith("s1") && (this.getEnergyPastArmor(3.2F, shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                    ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                    ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                    ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                }
                if (s.endsWith("s2") && (this.getEnergyPastArmor(0.2F, shot) > 0.0F)) {
                    ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                }
                if (s.endsWith("s3") && (this.getEnergyPastArmor(6.8F, shot) > 0.0F)) {
                    ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Elevator Crank: Hit, Controls Destroyed..");
                }
                if ((s.endsWith("s4") || s.endsWith("s5")) && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                    ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                }
                if ((s.endsWith("s6") || s.endsWith("s7") || s.endsWith("s10") || s.endsWith("s11")) && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.35F, shot) > 0.0F)) {
                    ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                }
                if ((s.endsWith("s8") || s.endsWith("s9")) && (this.getEnergyPastArmor(6.75F, shot) > 0.0F)) {
                    ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Aileron Cranks Destroyed..");
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() + "..");
                        }
                        ((FlightModelMain) (super.FM)).EI.engines[0].setReadyness(shot.initiator, ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12.7F, shot);
                } else if (s.startsWith("xxeng1cyls")) {
                    if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F) && (World.Rnd().nextFloat() < (((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 1.12F))) {
                        ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.005F) {
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                } else if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) {
                        this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("mag1")) {
                    if ((this.getEnergyPastArmor(0.2721F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            ((FlightModelMain) (super.FM)).EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Magneto 0 Destroyed..");
                        }
                    }
                } else if (s.endsWith("mag2")) {
                    if ((this.getEnergyPastArmor(0.2721F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            ((FlightModelMain) (super.FM)).EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine Module: Magneto 1 Destroyed..");
                        }
                    }
                } else if (s.endsWith("oil1")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                        this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    }
                    ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(5.5F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if (s.startsWith("xxsparli") && (World.Rnd().nextFloat() > 2) && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                }
                if (s.startsWith("xxsparri") && (World.Rnd().nextFloat() > 2) && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (World.Rnd().nextFloat() > 2) && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                }
                if (s.startsWith("xxsparro") && (World.Rnd().nextFloat() > 2) && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                }
                if (s.startsWith("xxsparsl") && (this.getEnergyPastArmor(3.5F, shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D" + this.chunkDamageVisible("StabL"), shot.initiator);
                }
                if (s.startsWith("xxsparsr") && (this.getEnergyPastArmor(3.5F, shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D" + this.chunkDamageVisible("StabR"), shot.initiator);
                }
                if (s.startsWith("xxspark") && (this.getEnergyPastArmor(3.5F, shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank1")) {
                int i = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.4F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.99F)) {
                    if (((FlightModelMain) (super.FM)).AS.astateTankStates[i] == 0) {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, 2);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.25F)) {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
            } else if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                } else if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                } else if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                } else if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                } else if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            return;
        }
        if (s.startsWith("xxmgun01")) {
            if ((this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.setJamBullets(0, 0);
                this.getEnergyPastArmor(11.98F, shot);
            }
            return;
        }
        if (s.startsWith("xxmgun02")) {
            if ((this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.setJamBullets(0, 1);
                this.getEnergyPastArmor(11.98F, shot);
            }
            return;
        }
        if (s.startsWith("xxmgun03")) {
            if ((this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.setJamBullets(10, World.Rnd().nextInt(0, 1));
                this.getEnergyPastArmor(11.98F, shot);
            }
            return;
        }
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
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
            if (j == 2) {
                j = 1;
            }
            Aircraft.debugprintln(this, "*** hitFlesh..");
            this.hitFlesh(j, shot, byte0);
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
        } else if (s.startsWith("xWing")) {
            Aircraft.debugprintln(this, "*** xWing: " + s);
            if (s.startsWith("xWingLIn") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xWingRIn") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
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
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
                this.hitChunk("AroneR", shot);
            }
        }
    }

    protected float        flapps;
    private static Point3d tmpp = new Point3d();

    static {
        Property.set(F1Mxyz.class, "originCountry", PaintScheme.countryJapan);
    }
}
