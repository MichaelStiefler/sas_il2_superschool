package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;

public abstract class GO_229 extends Scheme2 {

    public GO_229() {
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f, 0.0F, 0.078F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f, 0.0F, 0.078F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, cvt(f, 0.0F, 0.078F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, cvt(f, 0.0F, 0.078F, 0.0F, 90F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        if (this.FM.CT.getGear() < 0.8F) return;
        else {
            this.resetYPRmodifier();
            xyz[1] = cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.425F, 0.0F, 0.425F);
            this.hierMesh().chunkSetLocate("GearC6_D0", xyz, ypr);
            this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.425F, 100F, 83.5F), 0.0F);
            return;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && World.Rnd().nextFloat() < 0.2F) {
            if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.12F) {
                this.FM.AS.explodeEngine(this, 0);
                this.msgCollision(this, "WingLIn_D0", "WingLIn_D0");
                if (World.Rnd().nextBoolean()) this.FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
                else this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
            }
            if (this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.12F) {
                this.FM.AS.explodeEngine(this, 1);
                this.msgCollision(this, "WingRIn_D0", "WingRIn_D0");
                if (World.Rnd().nextBoolean()) this.FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
                else this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
            }
        }
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
    }

    protected void moveRudder(float f) {
        if (this.FM.CT.getGear() > 0.99F) this.hierMesh().chunkSetAngles("GearC7_D0", 0.0F, 20F * f, 0.0F);
        this.resetYPRmodifier();
        xyz[2] = cvt(f, -1F, 0.0F, 0.16F, 0.0F);
        this.hierMesh().chunkSetLocate("Rudder1_D0", xyz, ypr);
        this.resetYPRmodifier();
        xyz[1] = cvt(f, -1F, 0.0F, 0.0975F, 0.0F);
        this.hierMesh().chunkSetLocate("Rudder2_D0", xyz, ypr);
        this.resetYPRmodifier();
        xyz[2] = cvt(f, 0.0F, 1.0F, 0.0F, 0.16F);
        this.hierMesh().chunkSetLocate("Rudder3_D0", xyz, ypr);
        this.resetYPRmodifier();
        xyz[1] = cvt(f, 0.0F, 1.0F, 0.0F, 0.0975F);
        this.hierMesh().chunkSetLocate("Rudder4_D0", xyz, ypr);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -15F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -15F * f, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 0:
                this.FM.cut(0, j, actor);
                this.FM.cut(31, j, actor);
                break;

            case 1:
                this.FM.cut(1, j, actor);
                this.FM.cut(32, j, actor);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveAirBrake(float f) {
        this.resetYPRmodifier();
        xyz[1] = 0.1137F * f;
        this.FM.setGCenter(0.05F + 0.12F * f);
        this.hierMesh().chunkSetLocate("Brake01_D0", xyz, ypr);
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (!Actor.isValid(aircraft)) return;
                else {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                    return;
                }
            }

        };
        this.hierMesh().chunkVisible("Seat_D0", false);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) this.getEnergyPastArmor(7.72D / v1.x * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                else if (s.endsWith("g1")) this.getEnergyPastArmor(9.81D / v1.x * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                if (this.getEnergyPastArmor(1.0F, shot) > 0.0F) {
                    if (World.Rnd().nextFloat() < 0.12F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        this.debuggunnery("E/A Controls Out..");
                        return;
                    }
                    if (World.Rnd().nextFloat() < 0.12F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        this.debuggunnery("Rudder Controls Out..");
                        return;
                    }
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int i = s.charAt(5) - 49;
                if (this.getEnergyPastArmor(3.4F, shot) > 0.0F) this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6));
                if (World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass) this.FM.AS.hitEngine(shot.initiator, i, 5);
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) if (shot.power < 14100F) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if (this.FM.AS.astateTankStates[j] < 4 && World.Rnd().nextFloat() < 0.03F) this.FM.AS.hitTank(shot.initiator, j, 1);
                    if (shot.powerType == 3 && this.FM.AS.astateTankStates[j] > 2 && World.Rnd().nextFloat() < 0.015F) this.FM.AS.hitTank(shot.initiator, j, 10);
                } else this.FM.AS.hitTank(shot.initiator, j, World.Rnd().nextInt(0, (int) (shot.power / 56000F)));
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
        } else if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
        } else if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
        else if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
        else if (!s.startsWith("xengine1") && !s.startsWith("xengine2") && (s.startsWith("xpilot") || s.startsWith("xhead"))) {
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

    public void update(float f) {
        if (this.FM.AS.isMaster()) {
            for (int i = 0; i < 2; i++) {
                if (this.curctl[i] == -1F) {
                    this.curctl[i] = this.oldctl[i] = this.FM.EI.engines[i].getControlThrottle();
                    continue;
                }
                this.curctl[i] = this.FM.EI.engines[i].getControlThrottle();
                if ((this.curctl[i] - this.oldctl[i]) / f > 3F && this.FM.EI.engines[i].getRPM() < 2400F && this.FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.25F) this.FM.AS.hitEngine(this, i, 100);
                if ((this.curctl[i] - this.oldctl[i]) / f < -3F && this.FM.EI.engines[i].getRPM() < 2400F && this.FM.EI.engines[i].getStage() == 6) {
                    if (World.Rnd().nextFloat() < 0.25F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) this.FM.EI.engines[i].setEngineStops(this);
                    if (World.Rnd().nextFloat() < 0.75F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) this.FM.EI.engines[i].setKillCompressor(this);
                }
                this.oldctl[i] = this.curctl[i];
            }

            if (Config.isUSE_RENDER()) {
                if (this.FM.EI.engines[0].getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6) {
                    if (this.FM.EI.engines[0].getPowerOutput() > 0.95F) this.FM.AS.setSootState(this, 0, 3);
                    else this.FM.AS.setSootState(this, 0, 2);
                } else this.FM.AS.setSootState(this, 0, 0);
                if (this.FM.EI.engines[1].getPowerOutput() > 0.8F && this.FM.EI.engines[1].getStage() == 6) {
                    if (this.FM.EI.engines[1].getPowerOutput() > 0.95F) this.FM.AS.setSootState(this, 1, 3);
                    else this.FM.AS.setSootState(this, 1, 2);
                } else this.FM.AS.setSootState(this, 1, 0);
            }
        }
        super.update(f);
    }

    private float oldctl[] = { -1F, -1F };
    private float curctl[] = { -1F, -1F };

    static {
        Class class1 = GO_229.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
