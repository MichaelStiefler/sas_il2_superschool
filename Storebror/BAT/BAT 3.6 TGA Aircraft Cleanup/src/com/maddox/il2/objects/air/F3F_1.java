package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class F3F_1 extends Scheme1 implements TypeFighter {

    public F3F_1() {
        this.arrestor = 0.0F;
        this.bChangedExts = false;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        this.bChangedExts = true;
        if (this.FM.isPlayers()) {
            F3F_1.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        this.bChangedExts = true;
        if (this.FM.isPlayers()) {
            F3F_1.bChangedPit = true;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 50F * f);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -130F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 130F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 135F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -135F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -87F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 87F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, 90F * f, 0.0F);
    }

    protected void moveGear(float f) {
        F3F_1.moveGear(this.hierMesh(), f);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -25F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 25F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.63F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Pilot1_D0", false);
                    this.hierMesh().chunkVisible("Head1_D0", false);
                    this.hierMesh().chunkVisible("HMask1_D0", false);
                    this.hierMesh().chunkVisible("Pilot1_D1", true);
                }
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor") && s.endsWith("p1")) {
                this.getEnergyPastArmor(15F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
                if (s.startsWith("xxeng1")) {
                    if (s.endsWith("case") && (this.getEnergyPastArmor(0.7F, shot) > 0.0F)) {
                        if (World.Rnd().nextFloat(20000F, 200000F) < shot.power) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat(10000F, 50000F) < shot.power) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                        if (World.Rnd().nextFloat(8000F, 28000F) < shot.power) {
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    if (s.endsWith("cyls") && (this.getEnergyPastArmor(0.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.75F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (this.FM.AS.astateEngineStates[0] < 1) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 1);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                        this.getEnergyPastArmor(25F, shot);
                    }
                    if (s.endsWith("supc") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
                        this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                    }
                }
                if (s.endsWith("oil1")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
            }
            if (s.startsWith("xxtank")) {
                int i = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitTank(shot.initiator, i, 1);
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.11F)) {
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                    }
                }
            }
            return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
            if (World.Rnd().nextFloat() < 0.21F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            }
            if (World.Rnd().nextFloat() < 0.21F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            }
            if (World.Rnd().nextFloat() < 0.21F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
            }
            if (World.Rnd().nextFloat() < 0.21F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
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
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
                this.hitChunk("AroneR", shot);
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

    public void moveArrestorHook(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = -0.97F * f;
        Aircraft.ypr[1] = -this.arrestor;
        this.hierMesh().chunkSetLocate("Hook1_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.CT.getArrestor();
        float f2 = 81F * f1 * f1 * f1 * f1 * f1 * f1 * f1;
        if (f1 > 0.01F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                this.arrestor = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -f2, f2, -f2, f2);
                this.moveArrestorHook(f1);
                if (this.FM.Gears.arrestorVAngle < -81F) {

                }
            } else {
                float f3 = 58F * this.FM.Gears.arrestorVSink;
                if ((f3 > 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                this.arrestor += f3;
                if (this.arrestor > f2) {
                    this.arrestor = f2;
                }
                if (this.arrestor < -f2) {
                    this.arrestor = -f2;
                }
                this.moveArrestorHook(f1);
            }
        }
    }

    private float         arrestor;
    public boolean        bChangedExts;
    public static boolean bChangedPit = false;

    static {
        Class class1 = F3F_1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F3F-1");
        Property.set(class1, "meshName", "3DO/Plane/F3F-1(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/F3F1.fmd:F3F1_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF3F1.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
    }
}
