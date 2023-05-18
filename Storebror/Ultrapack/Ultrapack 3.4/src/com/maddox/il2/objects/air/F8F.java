package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public abstract class F8F extends Scheme1 implements TypeFighter, TypeTNBFighter {

    public F8F() {
        this.arrestor2 = 0.0F;
        this.degUpper = 46.39D;
        this.degLower = 49.61D;
        this.initCosUpper = Math.cos(this.degUpper);
        this.initCosLower = Math.cos(this.degLower);
        this.armLengthRatio = 5.2631578947368425D;
    }

    public void onAircraftLoaded() {
        BulletEmitter be[] = new BulletEmitter[21];
        super.onAircraftLoaded();
        be[0] = this.getBulletEmitterByHookName("_CANNON01");
        be[1] = this.getBulletEmitterByHookName("_CANNON02");
        be[2] = this.getBulletEmitterByHookName("_CANNON03");
        be[3] = this.getBulletEmitterByHookName("_CANNON04");
        be[4] = this.getBulletEmitterByHookName("_ExternalDev01");
        be[5] = this.getBulletEmitterByHookName("_ExternalDev02");
        be[6] = this.getBulletEmitterByHookName("_ExternalBomb01");
        be[7] = this.getBulletEmitterByHookName("_ExternalBomb02");
        be[8] = this.getBulletEmitterByHookName("_ExternalBomb03");
        be[9] = this.getBulletEmitterByHookName("_ExternalDev03");
        be[10] = this.getBulletEmitterByHookName("_ExternalDev04");
        be[11] = this.getBulletEmitterByHookName("_ExternalDev05");
        be[12] = this.getBulletEmitterByHookName("_ExternalDev06");
        be[13] = this.getBulletEmitterByHookName("_ExternalRock01");
        be[14] = this.getBulletEmitterByHookName("_ExternalRock02");
        be[15] = this.getBulletEmitterByHookName("_ExternalRock03");
        be[16] = this.getBulletEmitterByHookName("_ExternalRock04");
        be[17] = this.getBulletEmitterByHookName("_ExternalDev01");
        be[18] = this.getBulletEmitterByHookName("_ExternalDev02");
        be[19] = this.getBulletEmitterByHookName("_ExternalBomb01");
        be[20] = this.getBulletEmitterByHookName("_ExternalBomb02");
        boolean gunFlg = false;
        for (int i = 0; i < 8; i++) {
            if (be[i] == GunEmpty.get()) continue;
            gunFlg = true;
            break;
        }

        this.hierMesh().chunkVisible("20mm1_D0", gunFlg);
        this.hierMesh().chunkVisible("20mm2_D0", gunFlg);
        this.hierMesh().chunkVisible("20mm3_D0", gunFlg);
        this.hierMesh().chunkVisible("20mm4_D0", gunFlg);
        boolean pylonFlg = false;
        for (int i = 8; i < be.length; i++) {
            if (be[i] == GunEmpty.get()) continue;
            pylonFlg = true;
            break;
        }

        this.hierMesh().chunkVisible("PylonsR_D0", pylonFlg);
        this.hierMesh().chunkVisible("PylonsL_D0", pylonFlg);
    }

    protected void moveAirBrake(float f) {
        this.FM.setGCenter(0.0F - 0.2F * f);
        this.hierMesh().chunkSetAngles("DiveFlapL_D0", 0.0F, -40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("DiveFlapR_D0", 0.0F, -40F * f, 0.0F);
    }

    protected void nextDMGLevel(String string, int i, Actor actor) {
        super.nextDMGLevel(string, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String string, int i, Actor actor) {
        super.nextCUTLevel(string, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void moveCockpitDoor(float f) {
        try {
            this.resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.5F);
            this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
            if (Config.isUSE_RENDER()) {
                if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
                this.setDoorSnd(f);
            }
        } catch (Exception exception) {}
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        try {
            if (f < 0.01D) f = 0.0F;
            float f1 = f >= 0.3F ? 1.0F : f / 0.3F;
            Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
            hiermesh.chunkSetAngles("GearCL1_D0", -110F * f1, 0.0F, 0.0F);
            Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
            hiermesh.chunkSetAngles("GearCR1_D0", 110F * f1, 0.0F, 0.0F);
            float f2 = f <= 0.3F ? 0.0F : (f - 0.3F) / 0.7F;
            Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
            hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 40F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
            Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
            hiermesh.chunkVisible("GearL7_D0", f2 > 0.1D);
            hiermesh.chunkSetAngles("GearL7_D0", 143.5F - 143.5F * f2, 0.0F, -45F + 45F * f2);
            Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
            hiermesh.chunkSetAngles("GearL2_D0", -68F * f2, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearL3_D0", 155F * f2, 0.0F, 0.0F);
            Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
            hiermesh.chunkVisible("GearR7_D0", f2 > 0.1D);
            hiermesh.chunkSetAngles("GearR7_D0", -143.5F + 143.5F * f2, 0.0F, -45F + 45F * f2);
            Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
            hiermesh.chunkSetAngles("GearR2_D0", 68F * f2, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", -155F * f2, 0.0F, 0.0F);
        } catch (Exception exception) {}
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    protected void moveFlap(float f) {
        float f_0_ = -50F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f_0_, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f_0_, 0.0F);
    }

    public void moveWheelSink() {
        try {
            this.resetYPRmodifier();
            double sinkAngleUpper[] = new double[2];
            double sinkAngleLower[] = new double[2];
            sinkAngleUpper[0] = this.degUpper - Math.toDegrees(this.initCosUpper + Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.3575F, 0.0F, (float) this.armLengthRatio * 0.3575F));
            sinkAngleLower[0] = this.degLower - Math.toDegrees(this.initCosLower + Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.3575F, 0.0F, (float) this.armLengthRatio * 0.3575F));
            sinkAngleUpper[1] = this.degUpper - Math.toDegrees(this.initCosUpper + Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.3575F, 0.0F, (float) this.armLengthRatio * 0.3575F));
            sinkAngleLower[1] = this.degLower - Math.toDegrees(this.initCosLower + Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.3575F, 0.0F, (float) this.armLengthRatio * 0.3575F));
            float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.3575F, 0.0F, 0.3575F);
            Aircraft.xyz[1] = f;
            this.hierMesh().chunkSetLocate("GearL6_D0", Aircraft.xyz, Aircraft.ypr);
            f = (float) sinkAngleUpper[0];
            this.hierMesh().chunkSetAngles("TArmL1_D0", 0.0F, 0.0F, 90F - f);
            f = (float) sinkAngleLower[0];
            this.hierMesh().chunkSetAngles("TArmL2_D0", 0.0F, 0.0F, f);
            f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.3575F, 0.0F, 0.3575F);
            Aircraft.xyz[1] = f;
            this.hierMesh().chunkSetLocate("GearR6_D0", Aircraft.xyz, Aircraft.ypr);
            f = (float) sinkAngleUpper[1];
            this.hierMesh().chunkSetAngles("TArmR1_D0", 0.0F, 0.0F, 90F - f);
            f = (float) sinkAngleLower[1];
            this.hierMesh().chunkSetAngles("TArmR2_D0", 0.0F, 0.0F, f);
        } catch (Exception exception) {}
    }

    public void moveSteering(float f) {
        try {
            this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, f, 0.0F);
        } catch (Exception exception) {}
    }

    public void moveArrestorHook(float f) {
        try {
            this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, 10.5F * f, 0.0F);
        } catch (Exception exception) {}
    }

    public void update(float f) {
        try {
            super.update(f);
            if (this.FM.CT.getArrestor() > 0.9F) if (this.FM.Gears.arrestorVAngle != 0.0F) {
                this.arrestor2 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -65F, 3F, 45F, -23F);
                this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, this.arrestor2, 0.0F);
                this.FM.Gears.getClass();
            } else {
                float f_0_ = -41F * this.FM.Gears.arrestorVSink;
                if (f_0_ < 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                if (f_0_ > 0.0F && this.FM.CT.getArrestor() < 0.9F) f_0_ = 0.0F;
                if (f_0_ > 6.2F) f_0_ = 6.2F;
                this.arrestor2 += f_0_;
                if (this.arrestor2 < -23F) this.arrestor2 = -23F;
                else if (this.arrestor2 > 45F) this.arrestor2 = 45F;
                this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, this.arrestor2, 0.0F);
            }
        } catch (Exception exception) {}
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;
        }
    }

    protected void hitBone(String string, Shot shot, Point3d point3d) {
        if (string.startsWith("xx")) {
            if (string.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (string.endsWith("f1")) this.getEnergyPastArmor(World.Rnd().nextFloat(8F, 12F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else if (string.endsWith("p1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(16F, 36F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) this.doRicochetBack(shot);
                } else if (string.endsWith("p2")) this.getEnergyPastArmor(11D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else if (string.endsWith("p3")) this.getEnergyPastArmor(11.5D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
            } else {
                if (string.startsWith("xxcmglammo") && World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                    int i = 0 + 2 * World.Rnd().nextInt(0, 2);
                    this.FM.AS.setJamBullets(0, i);
                }
                if (string.startsWith("xxcmgrammo") && World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                    int i = 1 + 2 * World.Rnd().nextInt(0, 2);
                    this.FM.AS.setJamBullets(0, i);
                }
                if (string.startsWith("xxcontrols")) {
                    this.debuggunnery("Controls: Hit..");
                    int i = string.charAt(10) - 48;
                    switch (i) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                                this.debuggunnery("Controls: Ailerones Controls: Disabled..");
                                this.FM.AS.setControlsDamage(shot.initiator, 0);
                            }
                            break;

                        case 7:
                        case 8:
                            if (World.Rnd().nextFloat() < 0.08F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                                this.debuggunnery("Controls: Elevator Controls: Disabled..");
                                this.FM.AS.setControlsDamage(shot.initiator, 1);
                            }
                            break;

                        case 9:
                            if (World.Rnd().nextFloat() < 0.95F && this.getEnergyPastArmor(1.27F, shot) > 0.0F) {
                                this.debuggunnery("Controls: Rudder Controls: Disabled..");
                                this.FM.AS.setControlsDamage(shot.initiator, 2);
                            }
                            break;
                    }
                } else if (string.startsWith("xxeng1")) {
                    this.debuggunnery("Engine Module: Hit..");
                    if (string.endsWith("case")) {
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < shot.power / 280000F) {
                                this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                                this.FM.AS.setEngineStuck(shot.initiator, 0);
                            }
                            if (World.Rnd().nextFloat() < shot.power / 100000F) {
                                this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                                this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            }
                        }
                        this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                    }
                    if (string.endsWith("cyls")) {
                        if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.66F) {
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                            this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                            if (World.Rnd().nextFloat() < shot.power / 1000000F) {
                                this.FM.AS.hitEngine(shot.initiator, 0, 2);
                                this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                            }
                        }
                        this.getEnergyPastArmor(25F, shot);
                    }
                    if (string.endsWith("eqpt")) {
                        if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                            if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                            if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        }
                        this.getEnergyPastArmor(2.0F, shot);
                    }
                    if (string.endsWith("gear")) {
                        if (this.getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.debuggunnery("Engine Module: Bullet Jams Reductor Gear..");
                            this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                        }
                        this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 12.44565F), shot);
                    }
                    if (string.startsWith("xxeng1mag")) {
                        int i = string.charAt(9) - 49;
                        this.debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                        this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                    }
                    if (string.endsWith("oil1")) {
                        if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
                        this.FM.AS.hitOil(shot.initiator, 0);
                    }
                    if (string.endsWith("prop") && this.getEnergyPastArmor(0.42F, shot) > 0.0F) this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                    if (string.startsWith("xxeng1typ") && this.getEnergyPastArmor(0.42F, shot) > 0.0F) this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
                } else if (string.startsWith("xxhyd")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F) {
                        this.debuggunnery("Hydro System: Disabled..");
                        this.FM.AS.setInternalDamage(shot.initiator, 0);
                    }
                } else if (string.startsWith("xxlock")) {
                    this.debuggunnery("Lock Construction: Hit..");
                    if (string.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                        this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                    }
                    if (string.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                        this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                    }
                    if (string.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                        this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                    }
                    if (string.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                        this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                    }
                    if (string.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                        this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                    }
                } else if (string.startsWith("xxmgun0")) {
                    int i = string.charAt(7) - 49;
                    if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                        this.debuggunnery("Armament: Machine Gun (" + i + ") Disabled..");
                        this.FM.AS.setJamBullets(0, i);
                        this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                    }
                } else if (string.startsWith("xxoil")) {
                    if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                        this.FM.AS.hitOil(shot.initiator, 0);
                        this.getEnergyPastArmor(0.22F, shot);
                        this.debuggunnery("Engine Module: Oil Tank Pierced..");
                    }
                } else if (string.startsWith("xxspar")) {
                    Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                    if (string.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 1 && this.getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                    }
                    if (string.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 1 && this.getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                    }
                    if (string.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 1 && this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                    }
                    if (string.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 1 && this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                    }
                    if (string.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.8F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                    }
                    if (string.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.8F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                    if (string.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 1 && this.getEnergyPastArmor(14.8F * World.Rnd().nextFloat(0.99F, 1.8F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** Tail1 Spars Damaged..");
                        this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                    }
                } else if (string.startsWith("xxsupc")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F) {
                        this.debuggunnery("Engine Module: Turbine Disabled..");
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                    }
                } else if (string.startsWith("xxtank")) {
                    int i = string.charAt(6) - 49;
                    if (this.getEnergyPastArmor(1.0F, shot) > 0.0F) {
                        if (this.FM.AS.astateTankStates[i] == 0) {
                            this.debuggunnery("Fuel Tank (" + i + "): Pierced..");
                            this.FM.AS.hitTank(shot.initiator, i, 1);
                            this.FM.AS.doSetTankState(shot.initiator, i, 1);
                        }
                        if (World.Rnd().nextFloat() < 0.07F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F) {
                            this.FM.AS.hitTank(shot.initiator, i, 2);
                            this.debuggunnery("Fuel Tank (" + i + "): Hit..");
                        }
                    }
                }
            }
        } else if (string.startsWith("xcf") || string.startsWith("xblister")) {
            if (this.chunkDamageVisible("CF") < 1) this.hitChunk("CF", shot);
            if ((string.startsWith("xcf2") || string.startsWith("xblister")) && World.Rnd().nextFloat() < 0.25F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            if (string.startsWith("xcf3") && point3d.x > -0.645D && point3d.x < 0.406D && point3d.z > 0.3D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
        } else if (string.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 1) this.hitChunk("Engine1", shot);
        } else if (string.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 1) this.hitChunk("Tail1", shot);
        } else if (string.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 1) this.hitChunk("Keel1", shot);
        } else if (string.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
        } else if (string.startsWith("xstab")) {
            if (string.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 1) this.hitChunk("StabL", shot);
            if (string.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 1) this.hitChunk("StabR", shot);
        } else if (string.startsWith("xvator")) {
            if (string.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
            if (string.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
        } else if (string.startsWith("xwing")) {
            if (string.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 1) this.hitChunk("WingLIn", shot);
            if (string.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 1) this.hitChunk("WingRIn", shot);
            if (string.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 1) this.hitChunk("WingLMid", shot);
            if (string.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 1) this.hitChunk("WingRMid", shot);
            if (string.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 1) this.hitChunk("WingLOut", shot);
            if (string.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 1) this.hitChunk("WingROut", shot);
        } else if (string.startsWith("xarone")) {
            if (string.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 1) this.hitChunk("AroneL", shot);
            if (string.startsWith("xaroner") && this.chunkDamageVisible("AroneR") < 1) this.hitChunk("AroneR", shot);
        } else if (string.startsWith("xgear")) {
            if (string.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
        } else if (string.startsWith("xpilot") || string.startsWith("xhead")) {
            int i = 0;
            int i_1_;
            if (string.endsWith("a")) {
                i = 1;
                i_1_ = string.charAt(6) - 49;
            } else if (string.endsWith("b")) {
                i = 2;
                i_1_ = string.charAt(6) - 49;
            } else i_1_ = string.charAt(5) - 49;
            this.hitFlesh(i_1_, shot, i);
        }
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLMid_D0", 100F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", -100F * f, 0.0F, 0.0F);
    }

    public void moveWingFold(float f) {
        this.moveWingFold(this.hierMesh(), f);
    }

    public void rareAction(float f, boolean bool) {
        super.rareAction(f, bool);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    public static boolean bChangedPit = false;
    private float         arrestor2;
    double                degUpper;
    double                degLower;
    double                initCosUpper;
    double                initCosLower;
    double                armLengthRatio;

    static {
        Class var_class = F8F.class;
        Property.set(var_class, "originCountry", PaintScheme.countryUSA);
    }
}
