package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public class MH_6 extends Scheme1 implements TypeScout, TypeTransport {

    public MH_6() {
        this.suka = new Loc();
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.rotorrpm = 0;
    }

    protected void moveElevator(float f) {
    }

    protected void moveAileron(float f) {
    }

    protected void moveFlap(float f) {
    }

    protected void moveRudder(float f) {
    }

    public void moveSteering(float f) {
    }

    protected void moveFan(float f) {
        this.rotorrpm = Math.abs((int) ((this.FM.EI.engines[0].getw() * 0.025F) + (this.FM.Vwld.length() / 30D)));
        if (this.rotorrpm >= 1) {
            this.rotorrpm = 1;
        }
        if (this.FM.EI.engines[0].getw() > 100F) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", true);
        }
        if (this.FM.EI.engines[0].getw() < 100F) {
            this.hierMesh().chunkVisible("Prop1_D0", true);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Prop1_D1")) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if (this.FM.EI.engines[0].getw() > 100F) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", true);
        }
        if (this.FM.EI.engines[0].getw() < 100F) {
            this.hierMesh().chunkVisible("Prop2_D0", true);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Prop2_D1")) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Tail1_CAP")) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
            this.hierMesh().chunkVisible("Prop2_D1", false);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 100F) % 360F : (float) (this.dynamoOrient - (this.rotorrpm * 25D)) % 360F;
        this.hierMesh().chunkSetAngles("Prop1_D0", -this.dynamoOrient, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, this.dynamoOrient * -10F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -10F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, 10F), 0.0F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.42F, 0.0F, 0.3F);
        this.hierMesh().chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(15F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(4F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
                } else if (s.endsWith("p3")) {
                    this.getEnergyPastArmor(2.0F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
                }
            }
            if (s.startsWith("xxcontrols")) {
                if (s.endsWith("1")) {
                    if (World.Rnd().nextFloat() < 0.3F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.3F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                } else if (s.endsWith("2")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.3F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.3F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        Aircraft.debugprintln(this, "*** Engine Controls Out..");
                    }
                } else if (s.endsWith("3")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                    }
                } else if (s.startsWith("xxeng1")) {
                    if (s.endsWith("prp") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                        this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                    }
                    if (s.endsWith("cas") && (this.getEnergyPastArmor(0.7F, shot) > 0.0F)) {
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
                    if (s.endsWith("cyl") && (this.getEnergyPastArmor(0.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.75F))) {
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
                    if (s.endsWith("sup") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
                        this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                    }
                    if (s.endsWith("sup")) {
                        if ((World.Rnd().nextFloat() < 0.3F) && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
                            this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
                        }
                        if ((World.Rnd().nextFloat() < 0.3F) && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
                            this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                        }
                        if ((World.Rnd().nextFloat() < 0.3F) && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
                            this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                        }
                        if ((World.Rnd().nextFloat() < 0.3F) && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
                            this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                        }
                        if ((World.Rnd().nextFloat() < 0.3F) && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
                            this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                    }
                    if (s.endsWith("oil")) {
                        this.FM.AS.hitOil(shot.initiator, 0);
                    }
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
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            this.hitChunk("CF", shot);
        }
        if (s.startsWith("xcockpit")) {
            if (point3d.z > 0.75D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            }
            if ((point3d.x > -1.1D) && (World.Rnd().nextFloat() < 0.1F)) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
            if (World.Rnd().nextFloat() < 0.25F) {
                if (point3d.y > 0.0D) {
                    if (point3d.x > -1.1D) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                    } else {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                    }
                } else if (point3d.x > -1.1D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                } else {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                }
            }
        } else if (s.startsWith("xeng")) {
            this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xtail")) {
            this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xrudder")) {
            this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglout")) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout")) {
                this.hitChunk("WingROut", shot);
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

    public void update(float f) {
        this.tiltRotor(f);
        this.computeVerticalThrust();
        this.computeOrizzontalThrust();
        this.computeOrizzontalThrust2();
        this.computeHovering();
        this.computeHovering2();
        this.computeEngine();
        this.computeMass();
        super.update(f);
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            if ((this.FM.EI.engines[0].getPowerOutput() >= 0.0F) && (this.FM.EI.engines[0].getStage() == 6)) {
                if (this.FM.EI.engines[0].getPowerOutput() > 0.5F) {
                    this.FM.AS.setSootState(this, 0, 2);
                } else {
                    this.FM.AS.setSootState(this, 0, 4);
                }
            } else {
                this.FM.AS.setSootState(this, 0, 0);
            }
        }
    }

    private void tiltRotor(float f) {
    }

    public void computeVerticalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 200F, 1.0F, 0.1F);
        float f3 = Aircraft.cvt(this.FM.getAltitude(), 0.0F, 4000F, 1.0F, 0.5F);
        float f4 = 0.0F;
        if (((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 < 12F) && (this.FM.getSpeedKMH() > 1.0F) && this.thisWeaponsName.startsWith("def")) || this.thisWeaponsName.startsWith("non")) {
            f4 = 2.0F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 < 12F) && (this.FM.getSpeedKMH() > 1.0F) && this.thisWeaponsName.startsWith("M134")) {
            f4 = 2.2F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 < 18F) && (this.FM.getSpeedKMH() > 1.0F) && this.thisWeaponsName.startsWith("Delta")) {
            f4 = 2.7F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 < 18F) && (this.FM.getSpeedKMH() > 1.0F) && this.thisWeaponsName.startsWith("LAU")) {
            f4 = 2.45F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 < 18F) && (this.FM.getSpeedKMH() > 1.0F) && this.thisWeaponsName.startsWith("2xMiniGun")) {
            f4 = 2.8F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 < 18F) && (this.FM.getSpeedKMH() > 1.0F) && this.thisWeaponsName.startsWith("2xMiniguns+2xLAU")) {
            f4 = 3.1F * f;
        } else if (((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 > 12F) && this.thisWeaponsName.startsWith("def")) || this.thisWeaponsName.startsWith("non") || this.thisWeaponsName.startsWith("M134")) {
            f4 = 1.0F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 > 18F) && this.thisWeaponsName.startsWith("LAU")) {
            f4 = 1.2F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 > 18F) && this.thisWeaponsName.startsWith("2xMiniGun")) {
            f4 = 1.7F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 > 18F) && this.thisWeaponsName.startsWith("2xMiniguns+2xLAU")) {
            f4 = 2.05F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 > 18F) && this.thisWeaponsName.startsWith("Delta")) {
            f4 = 1.65F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && !flag) {
            f4 = 1.45F * f;
        }
        this.FM.producedAF.z += f4 * ((10F * this.FM.M.referenceWeight) + (10F * this.FM.M.fuel)) * (1.0F * f2) * (1.0F * f3);
    }

    public void computeOrizzontalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 80F, 1.0F, 0.0F);
        float f2 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && !flag) {
            f2 = 0.1F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.getSpeedKMH() > 3F) && flag) {
            f2 = 0.1F * f;
        }
        this.FM.producedAF.x -= f2 * (10F * this.FM.M.mass) * (1.0F * f1);
    }

    public void computeOrizzontalThrust2() {
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.0F) && (this.FM.getSpeedKMH() < 0.0F)) {
            f1 = 0.2F * f;
        }
        this.FM.producedAF.x += f1 * (10F * this.FM.M.mass);
    }

    public void computeHovering() {
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (f1 < 50F) && (this.FM.getSpeedKMH() > 16F) && this.FM.CT.StabilizerControl) {
            f2 = 0.58F * f;
        }
        this.FM.producedAF.x -= f2 * (10F * this.FM.M.mass);
    }

    public void computeHovering2() {
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (f1 < 18F) && (this.FM.getSpeedKMH() > 2.0F) && this.FM.CT.StabilizerControl) {
            f2 = 0.08F * f;
        }
        this.FM.producedAF.y -= f2 * (10F * this.FM.M.mass);
    }

    public void computeEngine() {
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if (this.FM.EI.engines[0].getStage() > 5) {
            if (this.FM.EI.getPowerOutput() <= 0.0F) {

            }
        }
        if (f > 6D) {
            f1 = 5F;
        } else {
            float f2 = f * f;
            float f3 = f2 * f;
            f1 = ((0.0493827F * f3) - (0.266667F * f2)) + (1.08889F * f);
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public void computeMass() {
        float f = Aircraft.cvt(this.FM.getSpeedKMH(), 70F, 160F, 722F, 3000F);
        if (this.FM.getSpeedKMH() > 100F) {
            this.FM.M.massEmpty = 1.0F * f;
        }
    }

    public static boolean bChangedPit = false;
    public Loc            suka;
    private float         dynamoOrient;
    private boolean       bDynamoRotary;
    private int           rotorrpm;
    static {
        Class class1 = MH_6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Loach ");
        Property.set(class1, "meshName", "3DO/Plane/MH-6/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1966F);
        Property.set(class1, "yearExpired", 2010F);
        Property.set(class1, "FlightModel", "FlightModels/MH-6.fmd:LoachFM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHuey1B.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0, 9, 1, 1, 2, 2, 2, 2, 9, 9, 1, 1, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev11", "_MGUN07", "_MGUN08", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalDev12", "_ExternalDev13", "_MGUN09", "_MGUN10", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalDev19" });
    }
}
