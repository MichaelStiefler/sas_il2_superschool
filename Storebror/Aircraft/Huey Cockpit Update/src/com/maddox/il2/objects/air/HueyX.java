package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
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
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.Time;

public abstract class HueyX extends Scheme1 implements TypeScout, TypeTransport {

    public HueyX() {
        this.suka = new Loc();
        this.dynamoOrient = 0.0F;
        this.rotorrpm = 0;
        this.hoverThrustFactor1 = 1.4F;
        this.hoverThrustFactor2 = 1.1F;
        this.numTurrets = 2;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (super.FM.AS.bNavLightsOn) {
            final Point3d point3d = new Point3d();
            final Orient orient = new Orient();
            this.pos.getAbs(point3d, orient);
            this.l.set(point3d, orient);
            final Eff3DActor eff3dactor = Eff3DActor.New(this, this.findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            final LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
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

    protected void moveFan(float f) {
        double newRotorRpm = Math.abs((this.FM.EI.engines[0].getw() * 0.025D) - ((this.FM.Vwld.z < 0D) || (this.FM.EI.engines[0].getw() == 0D) ? this.FM.Vwld.z / 10D : 0D));
        if ((this.FM.EI.engines[0].getw() == 0D) && this.FM.Gears.onGround()) {
            newRotorRpm = 0D;
        }
        this.rotorrpm = (0.95D * this.rotorrpm) + (0.05D * newRotorRpm);
        this.hierMesh().chunkVisible("Prop1_D0", this.rotorrpm <= 0.8F);
        this.hierMesh().chunkVisible("PropRot1_D0", this.rotorrpm > 0.8F);
        this.hierMesh().chunkVisible("Prop2_D0", this.rotorrpm <= 0.8F);
        this.hierMesh().chunkVisible("PropRot2_D0", this.rotorrpm > 0.8F);
        if (this.hierMesh().isChunkVisible("Prop1_D1")) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
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
        this.dynamoOrient = (float) (this.dynamoOrient - (Math.min(this.rotorrpm, 1D) * 25D)) % 360F;
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
                    this.getEnergyPastArmor(15F / (0.00001F + (float) Math.abs(Aircraft.v1.x)), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(4F / (0.00001F + (float) Math.abs(Aircraft.v1.x)), shot);
                } else if (s.endsWith("p3")) {
                    this.getEnergyPastArmor(2.0F / (0.00001F + (float) Math.abs(Aircraft.v1.x)), shot);
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
                final int i = s.charAt(6) - 49;
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
        if (this.FM instanceof RealFlightModel) {
            this.computeVerticalThrust();
            this.computeOrizzontalThrust();
            this.computeOrizzontalThrust2();
            this.computeHovering();
            this.computeEngine();
            this.computeMass();
        }
        super.update(f);
        final Pilot pilot = (Pilot) this.FM;
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
            final Actor actor = War.GetNearestEnemy(this, 1, 1000F);
            if ((pilot != null) && Actor.isAlive(actor) && !(actor instanceof BridgeSegment)) {
                final Point3d point3d = new Point3d();
                actor.pos.getAbs(point3d);
                if (this.pos.getAbsPoint().distance(point3d) < 350D) {
                    point3d.sub(this.FM.Loc);
                    this.FM.Or.transformInv(point3d);
                    if (point3d.y < 0.0D) {
                        for (int i = 0; i < this.numTurrets; i++) {
                            this.FM.turret[i].target = actor;
                            this.FM.turret[i].tMode = 2;
                        }
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
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -70F) {
                    f = -70F;
                    flag = false;
                }
                if (f > 15F) {
                    f = 15F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -15F) {
                    f = -15F;
                    flag = false;
                }
                if (f > 65F) {
                    f = 65F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
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

    void tiltRotor(float f) {
    }

    public void computeVerticalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        final float f = this.FM.EI.engines[0].getThrustOutput();
        final float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        final float f2 = Aircraft.cvt(f1, 0.0F, 210F, 1.0F, 0.2F);
        final float f3 = Aircraft.cvt(this.FM.getAltitude(), 0.0F, 6000F, 1.0F, 0.5F);
        float f4 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 < 10F)) {
            f4 = this.hoverThrustFactor1 * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 > 10F)) {
            f4 = this.hoverThrustFactor2 * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && !flag) {
            f4 = 1.25F * f;
        }
        this.FM.producedAF.z += f4 * (10F * this.FM.M.mass) * (1.0F * f2) * (1.0F * f3);
    }

    public void computeOrizzontalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        final float f = this.FM.EI.engines[0].getThrustOutput();
        final float f1 = Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 200F, 1.0F, 0.0F);
        float f2 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && !flag) {
            f2 = 0.18F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.getSpeedKMH() > 5F) && flag) {
            f2 = 0.08F * f;
        }
        this.FM.producedAF.x -= f2 * (10F * this.FM.M.mass) * (1.0F * f1);
    }

    public void computeOrizzontalThrust2() {
        final float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.0F) && (this.FM.getSpeedKMH() < 0.0F)) {
            f1 = 0.35F * f;
        }
        this.FM.producedAF.x += f1 * (10F * this.FM.M.mass);
    }

    public void computeHovering() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        final float f = this.FM.EI.engines[0].getThrustOutput();
        final float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        final float f2 = Aircraft.cvt(f1, 0.0F, 100F, 1.0F, 0.0F);
        float f3 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && !flag && (f1 < 35F) && (this.FM.getSpeedKMH() > 10F) && this.FM.CT.StabilizerControl) {
            f3 = 0.13F * f;
        }
        this.FM.producedAF.x -= f3 * (10F * this.FM.M.mass) * (1.0F * f2);
    }

    public void computeEngine() {
        final float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if (f > 6D) {
            f1 = 5F;
        } else {
            final float f2 = f * f;
            final float f3 = f2 * f;
            f1 = ((0.03125F * f3) - (0.2125F * f2)) + (0.9F * f);
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public void computeMass() {
        this.FM.M.massEmpty = Aircraft.cvt(this.FM.getSpeedKMH(), 100F, 180F, 2034F, 3000F);
    }

    Loc                   l           = new Loc();
    public static boolean bChangedPit = false;
    public Loc            suka;
    float                 dynamoOrient;
    double                rotorrpm;
    float                 hoverThrustFactor1;
    float                 hoverThrustFactor2;
    int                   numTurrets;
}
