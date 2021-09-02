package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;

public class MeteorXX extends Scheme2 implements TypeFighter, TypeBNZFighter {

    public void moveCockpitDoor(float f) {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.48F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.05F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                hierMesh().chunkVisible("Pilot1_D0", false);
                hierMesh().chunkVisible("Head1_D0", false);
                hierMesh().chunkVisible("HMask1_D0", false);
                hierMesh().chunkVisible("Pilot1_D1", true);
                if (!FM.AS.bIsAboutToBailout && hierMesh().isChunkVisible("Blister1_D0"))
                    hierMesh().chunkVisible("Gore1_D0", true);
                break;
        }
    }

    protected void moveGear(float f) {
        moveGear(hierMesh(), f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 110F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        float f1 = Math.max(-f * 1000F, -90F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
    }

    protected void moveRudder(float f) {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
        if (FM.CT.getGear() > 0.75F)
            hierMesh().chunkSetAngles("GearC22_D0", 0.0F, -40F * f, 0.0F);
    }

    protected void moveAirBrake(float f) {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 90F * f, 0.0F);
        hierMesh().chunkSetAngles("Brake03_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Brake04_D0", 0.0F, 90F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xcf")) {
            if (chunkDamageVisible("CF") < 2)
                hitChunk("CF", shot);
        } else if (s.startsWith("xxtank1")) {
            int i = s.charAt(6) - 49;
            if (getEnergyPastArmor(0.4F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F) {
                if (FM.AS.astateTankStates[i] == 0) {
                    Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                    FM.AS.hitTank(shot.initiator, i, 2);
                }
                if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F) {
                    FM.AS.hitTank(shot.initiator, i, 2);
                    Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                }
            }
        } else if (s.startsWith("xxmgun")) {
            if (s.endsWith("01")) {
                Aircraft.debugprintln(this, "*** Cannon #1: Disabled..");
                FM.AS.setJamBullets(0, 0);
            }
            if (s.endsWith("02")) {
                Aircraft.debugprintln(this, "*** Cannon #2: Disabled..");
                FM.AS.setJamBullets(0, 1);
            }
            if (s.endsWith("03")) {
                Aircraft.debugprintln(this, "*** Cannon #3: Disabled..");
                FM.AS.setJamBullets(1, 0);
            }
            if (s.endsWith("04")) {
                Aircraft.debugprintln(this, "*** Cannon #4: Disabled..");
                FM.AS.setJamBullets(1, 1);
            }
            getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
        } else if (s.startsWith("xxcontrols")) {
            int j = s.charAt(10) - 48;
            switch (j) {
                case 1:
                    if (getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.3F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < 0.25F) {
                            FM.AS.setControlsDamage(shot.initiator, 2);
                            Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                        }
                        if (World.Rnd().nextFloat() < 0.25F) {
                            FM.AS.setControlsDamage(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Elevator Controls: Disabled..");
                        }
                    }

                case 2:
                case 3:
                    if (getEnergyPastArmor(1.5F, shot) > 0.0F) {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Aileron Controls: Disabled..");
                    }
                    break;
            }
        } else if (s.startsWith("xtail")) {
            if (chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else if (s.startsWith("xstabl"))
            hitChunk("StabL", shot);
        else if (s.startsWith("xstabr"))
            hitChunk("StabR", shot);
        else if (s.startsWith("xwing")) {
            if (s.endsWith("lin") && chunkDamageVisible("WingLIn") < 2)
                hitChunk("WingLIn", shot);
            if (s.endsWith("rin") && chunkDamageVisible("WingRIn") < 2)
                hitChunk("WingRIn", shot);
            if (s.endsWith("lmid") && chunkDamageVisible("WingLMid") < 2)
                hitChunk("WingLMid", shot);
            if (s.endsWith("rmid") && chunkDamageVisible("WingRMid") < 2)
                hitChunk("WingRMid", shot);
            if (s.endsWith("lout") && chunkDamageVisible("WingLOut") < 2)
                hitChunk("WingLOut", shot);
            if (s.endsWith("rout") && chunkDamageVisible("WingROut") < 2)
                hitChunk("WingROut", shot);
        } else if (s.startsWith("xengine")) {
            int k = s.charAt(7) - 49;
            if (((Tuple3d) (point3d)).x > 0.0D && ((Tuple3d) (point3d)).x < 0.69699999999999995D)
                FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6));
            if (World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass)
                FM.AS.hitEngine(shot.initiator, k, 5);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l;
            if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else {
                l = s.charAt(5) - 49;
            }
            if (l == 2)
                l = 1;
            Aircraft.debugprintln(this, "*** hitFlesh..");
            hitFlesh(l, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);

            case 11:
                cutFM(17, j, actor);
                FM.cut(17, j, actor);
                cutFM(18, j, actor);
                FM.cut(18, j, actor);
                return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        if (FM.AS.isMaster()) {
            for (int i = 0; i < 2; i++)
                if (curctl[i] == -1F) {
                    curctl[i] = oldctl[i] = FM.EI.engines[i].getControlThrottle();
                } else {
                    curctl[i] = FM.EI.engines[i].getControlThrottle();
                    if ((curctl[i] - oldctl[i]) / f > 3F && FM.EI.engines[i].getRPM() < 2400F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.25F)
                        FM.AS.hitEngine(this, i, 100);
                    if ((curctl[i] - oldctl[i]) / f < -3F && FM.EI.engines[i].getRPM() < 2400F && FM.EI.engines[i].getStage() == 6) {
                        if (World.Rnd().nextFloat() < 0.25F && (FM instanceof RealFlightModel) && ((RealFlightModel) FM).isRealMode())
                            FM.EI.engines[i].setEngineStops(this);
                        if (World.Rnd().nextFloat() < 0.75F && (FM instanceof RealFlightModel) && ((RealFlightModel) FM).isRealMode())
                            FM.EI.engines[i].setKillCompressor(this);
                    }
                    oldctl[i] = curctl[i];
                }

            if (Config.isUSE_RENDER()) {
                if (FM.EI.engines[0].getPowerOutput() > 0.8F && FM.EI.engines[0].getStage() == 6) {
                    if (FM.EI.engines[0].getPowerOutput() > 0.95F)
                        FM.AS.setSootState(this, 0, 3);
                    else
                        FM.AS.setSootState(this, 0, 2);
                } else {
                    FM.AS.setSootState(this, 0, 0);
                }
                if (FM.EI.engines[1].getPowerOutput() > 0.8F && FM.EI.engines[1].getStage() == 6) {
                    if (FM.EI.engines[1].getPowerOutput() > 0.95F)
                        FM.AS.setSootState(this, 1, 3);
                    else
                        FM.AS.setSootState(this, 1, 2);
                } else {
                    FM.AS.setSootState(this, 1, 0);
                }
            }
        }
        super.update(f);
    }

    private float oldctl[] = { -1F, -1F };
    private float curctl[] = { -1F, -1F };
}
