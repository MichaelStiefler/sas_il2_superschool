package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;

public class AH64 extends Scheme1 implements TypeScout, TypeTransport, TypeStormovik {

    public AH64() {
        this.suka = new Loc();
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.rotorrpm = 0;
        this.pictAileron = 0.0F;
        this.pictVator = 0.0F;
        this.pictRudder = 0.0F;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.AS.bNavLightsOn) {
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            this.pos.getAbs(point3d, orient);
            AH64.l.set(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(this, this.findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
            lightpointactor.light.setEmit(1.0F, 3F);
            eff3dactor.draw.lightMap().put("light", lightpointactor);
        }
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

    private void stability() {
        double d = 0.0D;
        Vector3d vector3d = new Vector3d();
        this.getSpeed(vector3d);
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        float f = (float) (this.FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
        if ((f < 10F) && (this.FM.getSpeedKMH() < 60F) && (vector3d.z < -1D)) {
            vector3d.z *= 0.9D;
            this.setSpeed(vector3d);
        }
        if (this.FM.Gears.nOfGearsOnGr > 2) {
            vector3d.x *= 0.99D;
            vector3d.y *= 0.99D;
            this.setSpeed(vector3d);
        }
        if (this.FM.getSpeedKMH() > 180F) {
            d = (this.FM.getSpeedKMH() - this.FM.VmaxFLAPS) / 10F;
            if (d < 0.0D) {
                d = 0.0D;
            }
        }
        Point3d point3d1 = new Point3d(0.0D, 0.0D, 0.0D);
        point3d1.x = 0.0D - ((this.FM.Or.getTangage() / 10F) - (this.FM.CT.getElevator() * 2.5D));
        point3d1.y = 0.0D - ((this.FM.Or.getKren() / 10F) - (this.FM.CT.getAileron() * 2.5D)) - d;
        point3d1.z = 2D;
        this.FM.EI.engines[0].setPropPos(point3d1);
        this.FM.producedAF.x += 7000D * (-this.FM.CT.getElevator() * this.FM.EI.engines[0].getPowerOutput());
        this.FM.producedAF.y += 6000D * (-this.FM.CT.getAileron() * this.FM.EI.engines[0].getPowerOutput());
    }

    public void update(float f) {
        this.tiltRotor(f);
        this.stability();
        super.update(f);
        Pilot pilot = (Pilot) this.FM;
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
        if (pilot != null) {
            Actor actor = War.GetNearestEnemy(this, 1, 2000F);
            if ((pilot != null) && Actor.isAlive(actor) && !(actor instanceof BridgeSegment)) {
                Point3d point3d = new Point3d();
                actor.pos.getAbs(point3d);
                if (this.pos.getAbsPoint().distance(point3d) < 1000D) {
                    point3d.sub(this.FM.Loc);
                    this.FM.Or.transformInv(point3d);
                    if (point3d.y < 0.0D) {
                        this.FM.turret[0].target = actor;
                        this.FM.turret[0].tMode = 2;
                    }
                }
            } else if (actor != null) {
                for (int i = 0; i < this.FM.turret.length; i++) {
                    if ((this.FM.turret[i].target != null) && !(this.FM.turret[i].target instanceof Aircraft) && !Actor.isAlive(this.FM.turret[i].target)) {
                        this.FM.turret[i].target = null;
                    }
                }

            }
        }
        float f1 = this.FM.CT.getAirBrake();
        f1 = this.FM.CT.getAileron();
        if (Math.abs(this.pictAileron - f1) > 0.01F) {
            this.pictAileron = f1;
        }
        f1 = this.FM.CT.getRudder();
        if (Math.abs(this.pictRudder - f1) > 0.01F) {
            this.pictRudder = f1;
        }
        f1 = this.FM.CT.getElevator();
        if (Math.abs(this.pictVator - f1) > 0.01F) {
            this.pictVator = f1;
        }
        float f2 = this.FM.EI.getPowerOutput() * Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 600F, 2.0F, 0.0F);
        if (this.FM.CT.getAirBrake() > 0.5F) {
            if (this.FM.Or.getTangage() > 5F) {
                this.FM.getW().scale(Aircraft.cvt(this.FM.Or.getTangage(), 45F, 90F, 1.0F, 0.1F));
                float f3 = this.FM.Or.getTangage();
                if (Math.abs(this.FM.Or.getKren()) > 90F) {
                    f3 = 90F + (90F - f3);
                }
                float f4 = f3 - 90F;
                this.FM.CT.trimElevator = Aircraft.cvt(f4, -20F, 20F, 0.5F, -0.5F);
                f4 = this.FM.Or.getKren();
                if (Math.abs(f4) > 90F) {
                    if (f4 > 0.0F) {
                        f4 = 180F - f4;
                    } else {
                        f4 = -180F - f4;
                    }
                }
                this.FM.CT.trimAileron = Aircraft.cvt(f4, -20F, 20F, 0.5F, -0.5F);
                this.FM.CT.trimRudder = Aircraft.cvt(f4, -15F, 15F, 0.04F, -0.04F);
            }
        } else {
            this.FM.CT.trimAileron = 0.0F;
            this.FM.CT.trimElevator = 0.0F;
            this.FM.CT.trimRudder = 0.0F;
        }
        this.FM.Or.increment(f2 * (this.FM.CT.getRudder() + this.FM.CT.getTrimRudderControl()), f2 * (this.FM.CT.getElevator() + this.FM.CT.getTrimElevatorControl()), f2 * (this.FM.CT.getAileron() + this.FM.CT.getTrimAileronControl()));
        if ((this.FM.getAltitude() > 0.0F) && (this.FM.getSpeedKMH() >= 190D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= this.producedAfValues[0];
        }
        if ((this.FM.getAltitude() > 0.0F) && (this.FM.getSpeedKMH() >= 210D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= this.producedAfValues[1];
        }
        if ((this.FM.getAltitude() > 0.0F) && (this.FM.getSpeedKMH() >= 225D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= this.producedAfValues[2];
        }
        if ((this.FM.getAltitude() > 3700F) && (this.FM.EI.engines[0].getThrustOutput() > 1.01F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= this.producedAfValues[3];
        }
        if ((this.FM.getAltitude() > 4500F) && (this.FM.EI.engines[0].getThrustOutput() > 1.01F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= this.producedAfValues[4];
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -80F) {
            af[0] = -80F;
            flag = false;
        } else if (af[0] > 80F) {
            af[0] = 80F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if (af[1] < -85F) {
            af[1] = -85F;
            flag = false;
        }
        if (af[1] > 1.0F) {
            af[1] = 1.0F;
            flag = false;
        }
        if (!flag) {
            return false;
        }
        float f1 = af[1];
        if ((f < 1.2F) && (f1 < 13.3F)) {
            return false;
        } else {
            return (f1 >= -3.1F) || (f1 <= -4.6F);
        }
    }

    private void tiltRotor(float f) {
    }

    public static boolean bChangedPit      = false;
    private static Loc    l                = new Loc();
    public Loc            suka;
    private float         dynamoOrient;
    private boolean       bDynamoRotary;
    private int           rotorrpm;
    private float         pictAileron;
    private float         pictVator;
    private float         pictRudder;
    double[]              producedAfValues = { 1200D, 1200D, 1200D, 1000D, 1000D };

    static {
        Class class1 = AH64.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AH64");
        Property.set(class1, "meshName", "3DO/Plane/AH64/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1986.5F);
        Property.set(class1, "FlightModel", "FlightModels/AH1.fmd:AH1FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAH1.class, CockpitAH1_TGunner.class });
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 1, 1, 3, 3, 3, 3, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 9, 9, 1, 1, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_MGUN03", "_MGUN04", "_ExternalDev07", "_ExternalDev08", "_MGUN05", "_MGUN06", "_Pylon01", "_Pylon02" });
    }
}
