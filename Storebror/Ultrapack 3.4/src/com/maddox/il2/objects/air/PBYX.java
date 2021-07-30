package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class PBYX extends Scheme2 implements TypeBomber, TypeScout, TypeSailPlane, TypeTransport {

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 2; j++)
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }

    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -floatindex(10F * f, gear3), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, floatindex(10F * f, gear4), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, floatindex(10F * f, gear5), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -floatindex(10F * f, gear3), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, floatindex(10F * f, gear4), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -floatindex(10F * f, gear5), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    protected void moveFlap(float f) {
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -90F) {
                    f = -90F;
                    flag = false;
                }
                if (f > 90F) {
                    f = 90F;
                    flag = false;
                }
                if (f1 < -10F) {
                    f1 = -10F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -90F) {
                    f = -90F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 90F) {
                    f = 90F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) this.hitChunk("Rudder1", shot);
        else if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
        else if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
        else if (s.startsWith("xvatorl")) this.hitChunk("VatorL", shot);
        else if (s.startsWith("xvatorr")) this.hitChunk("VatorR", shot);
        else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F) this.FM.AS.hitTank(shot.initiator, 0, 1);
            if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F) this.FM.AS.hitTank(shot.initiator, 1, 1);
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F) this.FM.AS.hitTank(shot.initiator, 2, 1);
            if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F) this.FM.AS.hitTank(shot.initiator, 3, 1);
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
        else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
            if (this.getEnergyPastArmor(1.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.5F) {
                this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                if (this.FM.AS.astateEngineStates[0] < 1) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                }
                if (World.Rnd().nextFloat() < shot.power / 960000F) this.FM.AS.hitEngine(shot.initiator, 0, 3);
                this.getEnergyPastArmor(25F, shot);
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
            if (this.getEnergyPastArmor(1.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[1].getCylindersRatio() * 0.5F) {
                this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                if (this.FM.AS.astateEngineStates[1] < 1) {
                    this.FM.AS.hitEngine(shot.initiator, 1, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 1, 1);
                }
                if (World.Rnd().nextFloat() < shot.power / 960000F) this.FM.AS.hitEngine(shot.initiator, 1, 3);
                this.getEnergyPastArmor(25F, shot);
            }
        } else if (s.startsWith("xgearl")) this.hitChunk("GearL2", shot);
        else if (s.startsWith("xgearr")) this.hitChunk("GearR2", shot);
        else if (s.startsWith("xturret")) {
            if (s.startsWith("xturret1")) this.FM.AS.setJamBullets(10, 0);
            if (s.startsWith("xturret2")) this.FM.AS.setJamBullets(11, 0);
            if (s.startsWith("xturret3")) this.FM.AS.setJamBullets(12, 0);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i;
            if (s.endsWith("a")) {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else i = s.charAt(5) - 49;
            this.hitFlesh(i, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.killPilot(this, 5);
                this.killPilot(this, 6);
                break;

            case 33:
                this.hierMesh().chunkVisible("Wire01_D0", false);
                this.hitProp(0, j, actor);
                this.hitProp(1, j, actor);
                super.cutFM(36, j, actor);
                // fall through

            case 34:
            case 35:
                this.hierMesh().chunkVisible("Wire02_D0", false);
                break;

            case 36:
                this.hierMesh().chunkVisible("Wire02_D0", false);
                this.hitProp(0, j, actor);
                this.hitProp(1, j, actor);
                super.cutFM(33, j, actor);
                // fall through

            case 37:
            case 38:
                this.hierMesh().chunkVisible("Wire01_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 7; i++) {
            if (i == 5) continue;
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
        }

    }

    private static final float gear3[] = { 0.0F, 0.75F, 1.5F, 2.25F, 3.49F, 5F, 7.75F, 12F, 17.75F, 25.01F, 45F };
    private static final float gear4[] = { 0.0F, 7.25F, 14.25F, 21.5F, 28.75F, 36.25F, 44F, 54.6F, 64F, 72.75F, 85F };
    private static final float gear5[] = { 0.0F, 3.5F, 7F, 10.6F, 13.9F, 17F, 19.25F, 20.75F, 24.25F, 30.95F, 50F };
    private static Point3d     tmpp    = new Point3d();

    static {
        Class class1 = PBYX.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
