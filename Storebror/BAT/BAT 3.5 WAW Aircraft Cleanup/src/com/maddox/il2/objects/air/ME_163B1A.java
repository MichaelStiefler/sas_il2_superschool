package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.MGunMG15120MGk;
import com.maddox.rts.Property;

public class ME_163B1A extends Scheme1 implements TypeFighter, TypeBNZFighter {

    public ME_163B1A() {
        this.bCockpitNVentilated = false;
        this.bCartAttached = true;
        this.flame = null;
        this.dust = null;
        this.trail = null;
        this.sprite = null;
        this.turboexhaust = null;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.pk = 0;
    }

    public void destroy() {
        if (Actor.isValid(this.flame)) {
            this.flame.destroy();
        }
        if (Actor.isValid(this.dust)) {
            this.dust.destroy();
        }
        if (Actor.isValid(this.trail)) {
            this.trail.destroy();
        }
        if (Actor.isValid(this.sprite)) {
            this.sprite.destroy();
        }
        if (Actor.isValid(this.turboexhaust)) {
            this.turboexhaust.destroy();
        }
        super.destroy();
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER()) {
            this.flame = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109F.eff", -1F);
            this.dust = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109D.eff", -1F);
            this.trail = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", -1F);
            this.sprite = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", -1F);
            this.turboexhaust = Eff3DActor.New(this, this.findHook("_Engine1ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallGND.eff", -1F);
            Eff3DActor.setIntesity(this.flame, 0.0F);
            Eff3DActor.setIntesity(this.dust, 0.0F);
            Eff3DActor.setIntesity(this.trail, 0.0F);
            Eff3DActor.setIntesity(this.sprite, 0.0F);
            Eff3DActor.setIntesity(this.turboexhaust, 1.0F);
        }
        if (this.getGunByHookName("_CANNON03") instanceof MGunMG15120MGk) {
            this.hierMesh().chunkVisible("20mmL_D0", true);
        } else {
            this.hierMesh().chunkVisible("20mmL_D0", false);
        }
        if (this.getGunByHookName("_CANNON04") instanceof MGunMG15120MGk) {
            this.hierMesh().chunkVisible("20mmR_D0", true);
        } else {
            this.hierMesh().chunkVisible("20mmR_D0", false);
        }
    }

    protected void hitBone(String var1, Shot var2, Point3d var3) {
        if (var1.startsWith("xx")) {
            if (var1.startsWith("xxarmor")) {
                if (var1.endsWith("1")) {
                    this.FM.AS.setCockpitState(var2.initiator, this.FM.AS.astateCockpitState | 2);
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(30F, 90F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), var2) < 0.0F) {
                        this.doRicochet(var2);
                    }
                } else if (var1.endsWith("2")) {
                    this.getEnergyPastArmor(13.130000114440918D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), var2);
                }
            } else if (var1.startsWith("xxcontrols")) {
                int var6 = var1.charAt(10) - 48;
                switch (var6) {
                    case 1:
                        if ((this.getEnergyPastArmor(2.2F / (float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z)), var2) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.FM.AS.setControlsDamage(var2.initiator, 2);
                        }
                        break;

                    case 2:
                        if (this.getEnergyPastArmor(2.2F / (float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z)), var2) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setControlsDamage(var2.initiator, 2);
                            }
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setControlsDamage(var2.initiator, 1);
                                this.FM.AS.setControlsDamage(var2.initiator, 0);
                            }
                        }
                        break;

                    case 3:
                    case 4:
                        if ((this.getEnergyPastArmor(2.2F, var2) > 0.0F) && (World.Rnd().nextFloat() < 0.1F)) {
                            this.FM.AS.setControlsDamage(var2.initiator, 1);
                            this.FM.AS.setControlsDamage(var2.initiator, 0);
                        }
                        break;
                }
            } else if (var1.startsWith("xxspar")) {
                if (var1.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (World.Rnd().nextFloat() < (1.0D - (0.92000001668930054D * Math.abs(((Tuple3d) (Aircraft.v1)).x)))) && (this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(((Tuple3d) (Aircraft.v1)).y)), var2) > 0.0F)) {
                    this.debuggunnery("*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", var2.initiator);
                }
                if (var1.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (World.Rnd().nextFloat() < (1.0D - (0.92000001668930054D * Math.abs(((Tuple3d) (Aircraft.v1)).x)))) && (this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(((Tuple3d) (Aircraft.v1)).y)), var2) > 0.0F)) {
                    this.debuggunnery("*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", var2.initiator);
                }
                if (var1.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (World.Rnd().nextFloat() < (1.0D - (0.92000001668930054D * Math.abs(((Tuple3d) (Aircraft.v1)).x)))) && (this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(((Tuple3d) (Aircraft.v1)).y)), var2) > 0.0F)) {
                    this.debuggunnery("*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", var2.initiator);
                }
                if (var1.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (World.Rnd().nextFloat() < (1.0D - (0.92000001668930054D * Math.abs(((Tuple3d) (Aircraft.v1)).x)))) && (this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(((Tuple3d) (Aircraft.v1)).y)), var2) > 0.0F)) {
                    this.debuggunnery("*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", var2.initiator);
                }
                if (var1.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (World.Rnd().nextFloat() < (1.0D - (0.92000001668930054D * Math.abs(((Tuple3d) (Aircraft.v1)).x)))) && (this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(((Tuple3d) (Aircraft.v1)).y)), var2) > 0.0F)) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", var2.initiator);
                }
                if (var1.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (World.Rnd().nextFloat() < (1.0D - (0.92000001668930054D * Math.abs(((Tuple3d) (Aircraft.v1)).x)))) && (this.getEnergyPastArmor(17.799999237060547D / (1.0001000165939331D - Math.abs(((Tuple3d) (Aircraft.v1)).y)), var2) > 0.0F)) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", var2.initiator);
                }
                if (var1.startsWith("xxspark") && (this.chunkDamageVisible("Keel1") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), var2) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D3", var2.initiator);
                }
            } else if (var1.startsWith("xxlock")) {
                if (var1.startsWith("xxlockal") && (this.getEnergyPastArmor(4.35F, var2) > 0.0F)) {
                    this.debuggunnery("*** AroneL Lock Damaged..");
                    this.nextDMGLevels(1, 2, "AroneL_D0", var2.initiator);
                }
                if (var1.startsWith("xxlockar") && (this.getEnergyPastArmor(4.35F, var2) > 0.0F)) {
                    this.debuggunnery("*** AroneR Lock Damaged..");
                    this.nextDMGLevels(1, 2, "AroneR_D0", var2.initiator);
                }
                if (var1.startsWith("xxlockfl") && (this.getEnergyPastArmor(4.35F, var2) > 0.0F)) {
                    this.debuggunnery("*** VatorL Lock Damaged..");
                    this.nextDMGLevels(1, 2, "VatorL_D0", var2.initiator);
                }
                if (var1.startsWith("xxlockfr") && (this.getEnergyPastArmor(4.35F, var2) > 0.0F)) {
                    this.debuggunnery("*** VatorR Lock Damaged..");
                    this.nextDMGLevels(1, 2, "VatorR_D0", var2.initiator);
                }
                if (var1.startsWith("xxlockr") && (this.getEnergyPastArmor(4.32F, var2) > 0.0F)) {
                    this.debuggunnery("*** Rudder1 Lock Damaged..");
                    this.nextDMGLevels(1, 2, "Rudder1_D0", var2.initiator);
                }
            } else if (var1.startsWith("xxeng")) {
                int var6 = var1.charAt(8) - 48;
                switch (var6) {
                    case 1:
                        if (World.Rnd().nextFloat() < 0.01F) {
                            this.FM.AS.hitEngine(var2.initiator, 0, 100);
                        }
                        if (((Tuple3d) (Aircraft.Pd)).x < -2.7000000476837158D) {
                            this.FM.EI.engines[0].setReadyness(var2.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.01F, 0.35F));
                        }
                        break;

                    case 2:
                        if ((this.getEnergyPastArmor(4.96F, var2) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.FM.AS.hitEngine(var2.initiator, 0, 100);
                        }
                        break;

                    case 3:
                        this.getEnergyPastArmor(5.808F, var2);
                        break;
                }
            } else if (var1.startsWith("xxtank")) {
                int var6 = var1.charAt(6) - 48;
                switch (var6) {
                    case 1:
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 7.9F), var2) > 0.0F) {
                            this.FM.AS.hitTank(var2.initiator, 3, 1);
                        }
                        break;

                    case 2:
                    case 3:
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 7.9F), var2) > 0.0F) {
                            this.FM.AS.hitTank(var2.initiator, 2, 1);
                            this.bCockpitNVentilated = true;
                        }
                        // fall through

                    case 4:
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 7.9F), var2) > 0.0F) {
                            this.FM.AS.hitTank(var2.initiator, 0, World.Rnd().nextInt(1, 4));
                        }
                        break;

                    case 5:
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 7.9F), var2) > 0.0F) {
                            this.FM.AS.hitTank(var2.initiator, 1, World.Rnd().nextInt(1, 4));
                        }
                        break;
                }
            } else if (var1.startsWith("xxammo")) {
                int var6 = var1.charAt(6) - 48;
                if (World.Rnd().nextFloat() < 0.1F) {
                    if (var6 == 0) {
                        this.FM.AS.setJamBullets(1, 1);
                    } else {
                        this.FM.AS.setJamBullets(1, 0);
                    }
                }
            } else {
                if (var1.startsWith("xxgunl") && (this.getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 35.6F), var2) > 0.0F)) {
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (var1.startsWith("xxgunr") && (this.getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 35.6F), var2) > 0.0F)) {
                    this.FM.AS.setJamBullets(1, 1);
                }
                if (!var1.startsWith("xxeqpt")) {

                }
            }
        } else if (var1.startsWith("xcf")) {
            if ((((Tuple3d) (Aircraft.Pd)).x > 2.0099999999999998D) && (this.getEnergyPastArmor(11.11F / ((float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z)) + 0.0001F), var2) <= 0.0F)) {
                this.doRicochet(var2);
                return;
            }
            if ((((Tuple3d) (Aircraft.Pd)).x > 0.80000000000000004D) && (((Tuple3d) (Aircraft.Pd)).x < 2D)) {
                if (((Tuple3d) (Aircraft.Pd)).z > 0.42499999999999999D) {
                    if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.setCockpitState(var2.initiator, this.FM.AS.astateCockpitState | 1);
                    }
                } else if (((Tuple3d) (Aircraft.Pd)).y > 0.0D) {
                    if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.setCockpitState(var2.initiator, this.FM.AS.astateCockpitState | 4);
                    } else {
                        this.FM.AS.setCockpitState(var2.initiator, this.FM.AS.astateCockpitState | 8);
                    }
                } else if (World.Rnd().nextFloat() < 0.5F) {
                    this.FM.AS.setCockpitState(var2.initiator, this.FM.AS.astateCockpitState | 0x10);
                } else {
                    this.FM.AS.setCockpitState(var2.initiator, this.FM.AS.astateCockpitState | 0x20);
                }
            }
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", var2);
            }
        } else if (var1.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", var2);
            }
        } else if (var1.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", var2);
            }
        } else if (var1.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", var2);
            }
        } else if (var1.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) {
                this.hitChunk("WingLIn", var2);
            }
        } else if (var1.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) {
                this.hitChunk("WingRIn", var2);
            }
        } else if (var1.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) {
                this.hitChunk("WingLMid", var2);
            }
        } else if (var1.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) {
                this.hitChunk("WingRMid", var2);
            }
        } else if (var1.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) {
                this.hitChunk("WingLOut", var2);
            }
        } else if (var1.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) {
                this.hitChunk("WingROut", var2);
            }
        } else if (var1.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 2) {
                this.hitChunk("AroneL", var2);
            }
        } else if (var1.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 2) {
                this.hitChunk("AroneR", var2);
            }
        } else if (var1.startsWith("xflapl")) {
            if (this.chunkDamageVisible("VatorL") < 1) {
                this.hitChunk("VatorL", var2);
            }
        } else if (var1.startsWith("xflapr")) {
            if (this.chunkDamageVisible("VatorR") < 1) {
                this.hitChunk("VatorR", var2);
            }
        } else if (var1.startsWith("xpilot") || var1.startsWith("xhead")) {
            byte var4 = 0;
            int var5;
            if (!var1.endsWith("a") && !var1.endsWith("a2")) {
                if (!var1.endsWith("b") && !var1.endsWith("b2")) {
                    var5 = var1.charAt(5) - 49;
                } else {
                    var4 = 2;
                    var5 = var1.charAt(6) - 49;
                }
            } else {
                var4 = 1;
                var5 = var1.charAt(6) - 49;
            }
            this.hitFlesh(var5, var2, var4);
        }
    }

    public void rareAction(float var1, boolean var2) {
        super.rareAction(var1, var2);
        if (var2 && this.bCockpitNVentilated) {
            this.FM.AS.hitPilot(this, 0, 1);
        }
        if (Config.isUSE_RENDER()) {
            if ((this.oldVwld < 20F) && (this.FM.getSpeed() > 20F)) {
                Eff3DActor.finish(this.turboexhaust);
                this.turboexhaust = Eff3DActor.New(this, this.findHook("_Engine1ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallTSPD.eff", -1F);
            }
            if ((this.oldVwld > 20F) && (this.FM.getSpeed() < 20F)) {
                Eff3DActor.finish(this.turboexhaust);
                this.turboexhaust = Eff3DActor.New(this, this.findHook("_Engine1ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallGND.eff", -1F);
            }
            this.oldVwld = this.FM.getSpeed();
        }
    }

    public void doMurderPilot(int var1) {
        if (var1 == 0) {
            this.hierMesh().chunkVisible("Pilot1_D0", false);
            this.hierMesh().chunkVisible("Head1_D0", false);
            this.hierMesh().chunkVisible("Pilot1_D1", true);
            this.hierMesh().chunkVisible("HMask1_D0", false);
        }
    }

    public static void moveGear(HierMesh var0, float var1) {
        var0.chunkSetAngles("GearL2_D0", 0.0F, -45F * var1, 0.0F);
        var0.chunkSetAngles("GearL3_D0", 0.0F, -45F * var1, 0.0F);
        var0.chunkSetAngles("GearL4_D0", 0.0F, -45F * var1, 0.0F);
        var0.chunkSetAngles("GearL5_D0", 0.0F, -45F * var1, 0.0F);
        var0.chunkSetAngles("GearL6_D0", 0.0F, -45F * var1, 0.0F);
        var0.chunkSetAngles("GearC2_D0", 0.0F, -15F * var1, 0.0F);
        var0.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
    }

    protected void moveGear(float var1) {
        HierMesh var2 = this.hierMesh();
        if (this.bCartAttached) {
            if (var1 < 1.0F) {
                this.hierMesh().chunkVisible("GearL1_D0", false);
                this.hierMesh().chunkVisible("GearR1_D0", false);
                if (this.hierMesh().isChunkVisible("Cart_D0")) {
                    this.hierMesh().chunkVisible("CartDrop_D0", true);
                    this.cut("CartDrop");
                }
                this.hierMesh().chunkVisible("Cart_D0", false);
                this.bCartAttached = false;
                this.FM.setCapableOfTaxiing(false);
                this.FM.CT.bHasBrakeControl = false;
            }
        } else {
            this.resetYPRmodifier();
            Aircraft.xyz[1] = -0.3F + (0.1125F * var1);
            Aircraft.ypr[1] = 88F;
            var2.chunkSetLocate("Cart_D0", Aircraft.xyz, Aircraft.ypr);
        }
        var2.chunkSetAngles("GearL2_D0", 0.0F, -45F * var1, 0.0F);
        var2.chunkSetAngles("GearL3_D0", 0.0F, -45F * var1, 0.0F);
        var2.chunkSetAngles("GearL4_D0", 0.0F, -45F * var1, 0.0F);
        var2.chunkSetAngles("GearL5_D0", 0.0F, -45F * var1, 0.0F);
        var2.chunkSetAngles("GearL6_D0", 0.0F, -45F * var1, 0.0F);
        var2.chunkSetAngles("GearC2_D0", 0.0F, -15F * var1, 0.0F);
        var2.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
    }

    public void moveWheelSink() {
        if (!this.bCartAttached && (this.FM.CT.getGear() > 0.99F)) {
            float var1 = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.066F, -45F, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, var1, 0.0F);
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, var1, 0.0F);
            this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, var1, 0.0F);
            this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, var1, 0.0F);
            this.hierMesh().chunkSetAngles("GearL6_D0", 0.0F, var1, 0.0F);
        }
    }

    public void moveSteering(float var1) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -var1, 0.0F);
    }

    protected void moveElevator(float var1) {
        this.reflectControls();
    }

    protected void moveAileron(float var1) {
        this.reflectControls();
    }

    private void reflectControls() {
        HierMesh var1 = this.hierMesh();
        float var2 = -20F * this.FM.CT.getAileron();
        float var3 = 20F * this.FM.CT.getElevator();
        var1.chunkSetAngles("AroneL_D0", 0.0F, var2 + var3, 0.0F);
        var1.chunkSetAngles("AroneR_D0", 0.0F, var2 - var3, 0.0F);
        var1.chunkSetAngles("VatorL_D0", 0.0F, 0.5F * var3, 0.0F);
        var1.chunkSetAngles("VatorR_D0", 0.0F, 0.5F * var3, 0.0F);
    }

    protected void moveFlap(float var1) {
        float var2 = -50F * var1;
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, var2, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, var2, 0.0F);
    }

    protected void moveFan(float var1) {
        this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
        if (this.pk >= 1) {
            this.pk = 1;
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("Prop1_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("PropRot1_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        this.hierMesh().chunkSetAngles("Prop1_D0", 0.0F, this.dynamoOrient, 0.0F);
    }

    public void update(float var1) {
        super.update(var1);
        if (this.bCartAttached) {
            this.moveGear(this.FM.CT.getGear());
            if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                this.FM.CT.bHasBrakeControl = false;
            } else {
                this.FM.CT.bHasBrakeControl = true;
            }
        }
        if (this.FM.AS.isMaster()) {
            if (Config.isUSE_RENDER()) {
                if ((this.FM.EI.engines[0].getw() > 0.0F) && (this.FM.EI.engines[0].getStage() == 6)) {
                    this.FM.AS.setSootState(this, 0, 1);
                } else {
                    this.FM.AS.setSootState(this, 0, 0);
                }
            }
            if (this.oldThtl < 0.35F) {
                this.FM.EI.setThrottle(0.0F);
            } else if (this.oldThtl < 0.65F) {
                this.FM.EI.setThrottle(0.35F);
            } else if (this.oldThtl < 1.0F) {
                this.FM.EI.setThrottle(0.65F);
            } else {
                this.FM.EI.setThrottle(1.0F);
            }
            if (this.oldThtl != this.FM.CT.PowerControl) {
                this.oldThtl = this.FM.CT.PowerControl;
                if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                    HUD.log(AircraftHotKeys.hudLogPowerId, "Power", new Object[] { new Integer(Math.round(this.oldThtl * 100F)) });
                }
            }
            if (this.oldThtl == 0.0F) {
                if (!this.FM.Gears.onGround()) {
                    if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() && (this.FM.EI.engines[0].getStage() == 6)) {
                        HUD.log("EngineI0");
                    }
                    this.FM.EI.engines[0].setEngineStops(this);
                }
            } else {
                if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() && (this.FM.EI.engines[0].getStage() == 0) && (this.FM.M.fuel > 0.0F)) {
                    HUD.log("EngineI1");
                }
                this.FM.EI.engines[0].setStage(this, 6);
            }
        }
    }

    public void doSetSootState(int var1, int var2) {
        switch (var2) {
            case 0:
                Eff3DActor.setIntesity(this.flame, 0.0F);
                Eff3DActor.setIntesity(this.dust, 0.0F);
                Eff3DActor.setIntesity(this.trail, 0.0F);
                Eff3DActor.setIntesity(this.sprite, 0.0F);
                break;

            case 1:
                Eff3DActor.setIntesity(this.flame, 1.0F);
                Eff3DActor.setIntesity(this.dust, 1.0F);
                Eff3DActor.setIntesity(this.trail, 1.0F);
                Eff3DActor.setIntesity(this.sprite, 1.0F);
                break;
        }
    }

    private boolean    bCockpitNVentilated;
    public boolean     bCartAttached;
    private Eff3DActor flame;
    private Eff3DActor dust;
    private Eff3DActor trail;
    private Eff3DActor sprite;
    private Eff3DActor turboexhaust;
    private float      oldThtl;
    private float      oldVwld;
    private float      dynamoOrient;
    private boolean    bDynamoRotary;
    private int        pk;

    static {
        Class var0 = ME_163B1A.class;
        new NetAircraft.SPAWN(var0);
        Property.set(var0, "iconFar_shortClassName", "Me-163");
        Property.set(var0, "meshName", "3DO/Plane/Me-163B-1a/hier.him");
        Property.set(var0, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(var0, "originCountry", PaintScheme.countryGermany);
        Property.set(var0, "yearService", 1944F);
        Property.set(var0, "yearExpired", 1946F);
        Property.set(var0, "FlightModel", "FlightModels/Me-163B-1a.fmd");
        Property.set(var0, "cockpitClass", new Class[] { CockpitME_163.class });
        Property.set(var0, "LOSElevation", 0.87325F);
        Aircraft.weaponTriggersRegister(var0, new int[] { 1, 1, 1, 1 });
        Aircraft.weaponHooksRegister(var0, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04" });
    }
}
