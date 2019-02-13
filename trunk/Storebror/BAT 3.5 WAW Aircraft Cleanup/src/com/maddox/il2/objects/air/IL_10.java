package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class IL_10 extends Scheme1 implements TypeStormovikArmored {

    public IL_10() {
        this.kangle = 0.0F;
        this.flapps = 0.0F;
    }

    public float getEyeLevelCorrection() {
        return -0.05F;
    }

    public void update(float f) {
        super.update(f);
        World.cur();
        if ((this == World.getPlayerAircraft()) && (super.FM.turret.length > 0) && (((FlightModelMain) (super.FM)).AS.astatePilotStates[1] < 90) && super.FM.turret[0].bIsAIControlled && ((super.FM.getOverload() > 7F) || (super.FM.getOverload() < -0.7F))) {
            Voice.speakRearGunShake();
        }
        this.kangle = (0.95F * this.kangle) + (0.05F * ((FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator());
        if (this.kangle > 1.0F) {
            this.kangle = 1.0F;
        }
        if (Math.abs(this.flapps - this.kangle) > 0.01F) {
            this.flapps = this.kangle;
            this.hierMesh().chunkSetAngles("radiator1_D0", 0.0F, -23F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("radiator2_D0", 0.0F, -70F * this.kangle, 0.0F);
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1A_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 60F), 0.0F);
        this.hierMesh().chunkSetAngles("Blister1B_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -170F), 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).AS.wantBeaconsNet(true);
        ((FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = true;
        ((FlightModelMain) (super.FM)).CT.dvCockpitDoor = 0.65F;
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.47F, 0.0F, -45F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.6F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.6F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.6F, 0.0F, -69F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.44F, 0.99F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(f1, 0.44F, 0.99F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f1, 0.44F, 0.99F, 0.0F, -69F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.44F, 0.54F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f1, 0.44F, 0.54F, 0.0F, -70F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        moveGear(this.hierMesh(), f, f1, f2);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink() {
        if (((FlightModelMain) (super.FM)).CT.getGear() < 0.99F) {
            return;
        } else {
            this.resetYPRmodifier();
            Aircraft.ypr[1] = -90F;
            Aircraft.xyz[1] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.228F, 0.0F, -0.228F);
            this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
            Aircraft.xyz[1] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.228F, 0.0F, 0.228F);
            this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
            return;
        }
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -90F * f, 0.0F);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -43F) {
            af[0] = -43F;
            flag = false;
        } else if (af[0] > 43F) {
            af[0] = 43F;
            flag = false;
        }
        if (af[1] < -2F) {
            af[1] = -2F;
            flag = false;
        }
        if (af[1] > 56F) {
            af[1] = 56F;
            flag = false;
        }
        return flag;
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1: // '\001'
                if (super.FM.turret.length == 0) {
                    return;
                }
                super.FM.turret[0].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0: // '\0'
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1: // '\001'
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        int i = 0;
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.startsWith("xxarmorp")) {
                    i = s.charAt(8) - 48;
                    switch (i) {
                        default:
                            break;

                        case 1: // '\001'
                            this.getEnergyPastArmor(12.880000114440918D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                            if ((shot.power <= 0.0F) && (World.Rnd().nextFloat() < 0.23F)) {
                                this.doRicochet(shot);
                            }
                            break;

                        case 2: // '\002'
                        case 5: // '\005'
                        case 7: // '\007'
                        case 8: // '\b'
                            this.getEnergyPastArmor(8D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            if ((shot.power <= 0.0F) && (Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)) {
                                this.doRicochet(shot);
                            }
                            break;

                        case 3: // '\003'
                            this.getEnergyPastArmor(16D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                            break;

                        case 4: // '\004'
                            this.getEnergyPastArmor(20.200000762939453D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                            if (shot.power <= 0.0F) {
                                this.doRicochetBack(shot);
                            }
                            break;

                        case 6: // '\006'
                            this.getEnergyPastArmor(8D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            if ((shot.power <= 0.0F) && (Math.abs(((Tuple3d) (Aircraft.v1)).z) < 0.43999999761581421D)) {
                                this.doRicochet(shot);
                            }
                            break;
                    }
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                i = s.charAt(10) - 48;
                if (s.endsWith("10")) {
                    i = 10;
                }
                switch (i) {
                    default:
                        break;

                    case 5: // '\005'
                        if (this.getEnergyPastArmor(0.25F / ((float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z)) + 0.0001F), shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.05F) {
                                this.debuggunnery("Controls: Elevator Wiring Hit, Elevator Controls Disabled..");
                                ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                            }
                            if (World.Rnd().nextFloat() < 0.75F) {
                                this.debuggunnery("Controls: Rudder Wiring Hit, Rudder Controls Disabled..");
                                ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                            }
                        }
                        break;

                    case 3: // '\003'
                    case 4: // '\004'
                        if ((this.getEnergyPastArmor(4D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                            this.debuggunnery("Controls: Aileron Wiring Hit, Aileron Controls Disabled..");
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;
                }
            }
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (World.Rnd().nextFloat() > (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.86F / (float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            if (s.startsWith("xxeng")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("prop")) {
                    if ((this.getEnergyPastArmor(3.6F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.8F)) {
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.debuggunnery("Engine Module: Prop Governor Hit, Disabled..");
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        } else {
                            this.debuggunnery("Engine Module: Prop Governor Hit, Oil Pipes Damaged..");
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                    }
                } else if (s.endsWith("gear")) {
                    if ((this.getEnergyPastArmor(4.6F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                        this.debuggunnery("Engine Module: Reductor Hit, Bullet Jams Reductor Gear..");
                        ((FlightModelMain) (super.FM)).EI.engines[0].setEngineStuck(shot.initiator);
                    }
                } else if (s.endsWith("feed")) {
                    if ((this.getEnergyPastArmor(3.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if (World.Rnd().nextFloat() < 0.05F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            this.debuggunnery("Engine Module: Crank Case Hit, Bullet Jams Ball Bearings..");
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() + "..");
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    ((FlightModelMain) (super.FM)).EI.engines[0].setReadyness(shot.initiator, ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    this.debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() + "..");
                    this.getEnergyPastArmor(22.5F, shot);
                } else if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(1.3F, shot) > 0.0F) && (World.Rnd().nextFloat() < (((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 1.75F))) {
                        ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        this.debuggunnery("Engine Module: Cylinders Assembly Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Operating..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.debuggunnery("Engine Module: Cylinders Assembly Hit, Engine Fires..");
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                        }
                        if (World.Rnd().nextFloat() < 0.01F) {
                            this.debuggunnery("Engine Module: Cylinders Assembly Hit, Bullet Jams Piston Head..");
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                    if ((Math.abs(((Tuple3d) (point3d)).y) < 0.1379999965429306D) && (this.getEnergyPastArmor(3.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if (World.Rnd().nextFloat() < 0.05F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else if (s.startsWith("xxeng1oil") && (this.getEnergyPastArmor(0.5F, shot) > 0.0F)) {
                    this.debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, 0);
                }
            }
            if (s.startsWith("xxOil") && (this.getEnergyPastArmor(3.5F, shot) > 0.0F)) {
                this.debuggunnery("Engine Module: Oil Tank Hit..");
                ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, 0);
            }
            if (s.startsWith("xxtank") && (this.getEnergyPastArmor(0.12F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                if (((FlightModelMain) (super.FM)).AS.astateTankStates[0] == 0) {
                    this.debuggunnery("Fuel System: Fuel Tank Pierced..");
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 1);
                    ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, 0, 1);
                } else if (((FlightModelMain) (super.FM)).AS.astateTankStates[i] == 1) {
                    this.debuggunnery("Fuel System: Fuel Tank Pierced..");
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 1);
                    ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, 0, 2);
                }
                if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) {
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 2);
                    this.debuggunnery("Fuel System: Fuel Tank Pierced, State Shifted..");
                }
            }
            if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    this.debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    this.debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
            }
            if (s.startsWith("xxcannon")) {
                if (s.endsWith("01") && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.debuggunnery("Armament System: Left Cannon: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 0);
                }
                if (s.endsWith("02") && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.debuggunnery("Armament System: Right Cannon: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if (s.startsWith("xxbomb")) {
                if ((World.Rnd().nextFloat() < 0.00345F) && (((FlightModelMain) (super.FM)).CT.Weapons[3] != null) && ((FlightModelMain) (super.FM)).CT.Weapons[3][0].haveBullets()) {
                    this.debuggunnery("Armament System: Bomb Payload Detonated..");
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 10);
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 1, 10);
                    this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcockpit")) {
            if (((Tuple3d) (point3d)).z > 0.77500000000000002D) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
                if ((((Tuple3d) (Aircraft.v1)).x < -0.90000000000000002D) && (this.getEnergyPastArmor(12D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
                }
            } else if (((Tuple3d) (point3d)).y > 0.0D) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 4);
            } else {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x10);
            }
            if (World.Rnd().nextFloat() < 0.067F) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 8);
            }
            if (World.Rnd().nextFloat() < 0.067F) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x20);
            }
            if (World.Rnd().nextFloat() < 0.067F) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x40);
            }
        }
        if (s.startsWith("xcf")) {
            if (((Tuple3d) (point3d)).z < 0.67200000000000004D) {
                this.getEnergyPastArmor(6D / (Math.abs(Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z))) + 9.9999997473787516E-005D), shot);
                if ((shot.power <= 0.0F) && (Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)) {
                    this.doRicochet(shot);
                }
            }
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xeng")) {
            if (((Tuple3d) (point3d)).z > 0.54900000000000004D) {
                this.getEnergyPastArmor(2D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
            } else if (((Tuple3d) (point3d)).x > 2.819D) {
                this.getEnergyPastArmor(6D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
            } else {
                this.getEnergyPastArmor(4D / (Math.abs(Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z))) + 9.9999997473787516E-005D), shot);
            }
            if ((Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D) && ((shot.power <= 0.0F) || (World.Rnd().nextFloat() < 0.1F))) {
                this.doRicochet(shot);
            }
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
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
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl")) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr")) {
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
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05F)) {
                this.debuggunnery("Hydro System: Disabled..");
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
            }
            if ((s.endsWith("2a") || s.endsWith("2b")) && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
                this.debuggunnery("Undercarriage: Stuck..");
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xturret")) {
            if (this.getEnergyPastArmor(0.25F, shot) > 0.0F) {
                this.debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                ((FlightModelMain) (super.FM)).AS.setJamBullets(10, 0);
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else if (s.startsWith("xhelm")) {
            this.getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 3.56F), shot);
            if (shot.power <= 0.0F) {
                this.doRicochetBack(shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
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
            this.hitFlesh(j, shot, byte0);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++) {
            if (super.FM.getAltitude() < 3000F) {
                if (this.hierMesh().chunkFindCheck("HMask" + i + "_D0") != -1) {
                    this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
                }
            } else if (this.hierMesh().chunkFindCheck("HMask" + i + "_D0") != -1) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    private float kangle;
    private float flapps;

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Il-10");
        Property.set(class1, "meshName", "3DO/Plane/Il-10(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "meshName_ru", "3DO/Plane/Il-10(ru)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_pl", "3DO/Plane/Il-10(ru)/hier.him");
        Property.set(class1, "PaintScheme_pl", new PaintSchemeBMPar05());
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Il-10.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitIL_10.class, CockpitIL_10_TGunner.class });
        Property.set(class1, "LOSElevation", 0.93155F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 10, 2, 2, 2, 2, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_MGUN03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02", "_ExternalDev01", "_ExternalDev02" });
    }
}
