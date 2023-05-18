package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class Letov extends Scheme1
    implements TypeScout, TypeBomber, TypeTransport, TypeStormovik
{

    public Letov()
    {
        lookoutTimeLeft = 2.0F;
        lookoutAz = 0.0F;
        lookoutEl = 0.0F;
        lookoutPos = new float[3][128];
        turretPos = new float[3];
        turretAng = new float[3];
        tu = new float[3];
        afBk = new float[3];
        turretPos[0] = 0.0F;
        turretPos[1] = 0.0F;
        turretPos[2] = 0.0F;
        turretAng[0] = 0.0F;
        turretAng[1] = 0.0F;
        turretAng[2] = 0.0F;
        turretLRposW = 0.0F;
        btme = -1L;
        fGunPos = 0.0F;
        turretState = 6;
        tu[0] = 0.0F;
        tu[1] = 0.0F;
        tu[2] = 0.0F;
        afBk[0] = 0.0F;
        afBk[1] = 0.0F;
        afBk[2] = 0.0F;
        finishedTurn = false;
        lookout = 0.0F;
        bGunnerKilled = false;
        gunOutOverride = 0;
        for(int i = 0; i < 128; i++)
        {
            lookoutPos[0][i] = World.Rnd().nextFloat() * 180F - 90F;
            lookoutPos[1][i] = World.Rnd().nextFloat() * 100F - 50F;
            if(lookoutPos[1][i] > 0.0F)
                lookoutPos[2][i] = World.Rnd().nextFloat() * 2.0F;
            else
                lookoutPos[2][i] = (World.Rnd().nextFloat() + World.Rnd().nextFloat() + World.Rnd().nextFloat() + World.Rnd().nextFloat()) * 3F;
        }

    }

    public boolean cut(String s)
    {
        boolean flag = super.cut(s);
        if(s.equalsIgnoreCase("WingLIn"))
            hierMesh().chunkVisible("WingLMid_CAP", true);
        else
        if(s.equalsIgnoreCase("WingRIn"))
            hierMesh().chunkVisible("WingRMid_CAP", true);
        return flag;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        default:
            break;

        case 19:
            FM.Gears.hitCentreGear();
            break;

        case 3:
            if(World.Rnd().nextInt(0, 99) < 1)
            {
                FM.AS.hitEngine(this, 0, 4);
                hitProp(0, j, actor);
                FM.EI.engines[0].setEngineStuck(actor);
                return cut("engine1");
            } else
            {
                FM.AS.setEngineDies(this, 0);
                return false;
            }
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
    }

    protected void moveFlap(float f)
    {
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -29F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
        float f1 = FM.CT.getAileron();
        hierMesh().chunkSetAngles("pilot1_arm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - cvt(f, -1F, 1.0F, -37F, 35F));
        hierMesh().chunkSetAngles("pilot1_arm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f, -1F, 0.0F, -61F, 0.0F) + cvt(f, 0.0F, 1.0F, 0.0F, 43F));
        if(f < 0.0F)
            f /= 2.0F;
        hierMesh().chunkSetAngles("Stick_D0", 0.0F, 15F * f1, cvt(f, -1F, 1.0F, -16F, 20F));
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneL3_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR3_D0", 0.0F, -35F * f, 0.0F);
        float f1 = FM.CT.getElevator();
        hierMesh().chunkSetAngles("pilot1_arm2_d0", cvt(f, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f, -1F, 1.0F, 6F, -8F) - cvt(f1, -1F, 1.0F, -37F, 35F));
        hierMesh().chunkSetAngles("pilot1_arm1_d0", 0.0F, 0.0F, cvt(f, -1F, 1.0F, -16F, 14F) + cvt(f1, -1F, 0.0F, -61F, 0.0F) + cvt(f1, 0.0F, 1.0F, 0.0F, 43F));
        if(f1 < 0.0F)
            f1 /= 2.0F;
        hierMesh().chunkSetAngles("Stick_D0", 0.0F, 15F * f, cvt(f1, -1F, 1.0F, -16F, 20F));
    }

    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        hierMesh().chunkVisible("pilot1_arm2_d0", false);
        hierMesh().chunkVisible("pilot1_arm1_d0", false);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("pilot1_arm2_d0", false);
            hierMesh().chunkVisible("pilot1_arm1_d0", false);
            break;

        case 1:
            bGunnerKilled = true;
            FM.turret[0].bIsOperable = false;
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;
        }
    }

    private void moveAndTurnTurret(float af[])
    {
        float f = Math.abs(af[0]);
        if(!bGunnerKilled)
        {
            turretAng[1] = 0.0F;
            turretAng[2] = 0.0F;
            turretPos[0] = 0.0F;
            turretPos[2] = 0.0F;
            if(turretFront > 0.5F)
            {
                if(af[0] < 0.0F)
                    turretAng[0] = (720F - af[0]) / 5F + turretLRpos * 25F;
                else
                    turretAng[0] = (-720F - af[0]) / 5F + turretLRpos * 25F;
                turretPos[1] = -0.6F + 0.2F * (float)Math.sin(Math.toRadians(f));
                turretAng[2] = 20F;
            } else
            {
                turretAng[0] = -af[0] / 5F - turretLRpos * 25F;
                turretPos[1] = -0.4F * (float)Math.sin(Math.toRadians(f));
            }
            hierMesh().chunkSetLocate("Pilot2_D0", turretPos, turretAng);
        }
        turretAng[0] = 0.0F;
        turretAng[1] = 0.0F;
        turretAng[2] = 0.0F;
        turretPos[2] = 0.0F;
        turretPos[1] = -0.6F * turretFront;
        turretPos[0] = 0.0F;
        hierMesh().chunkSetLocate("tyc", turretPos, turretAng);
        turretPos[1] = 0.0F;
        turretPos[0] = 0.35F * turretLRpos;
        hierMesh().chunkSetLocate("buben", turretPos, turretAng);
        hierMesh().chunkSetAngles("Turret1A_D0X", -af[0], 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Turret1B_D0X", 0.0F, af[1], 0.0F);
    }

    private boolean turretMoves(float af[])
    {
        boolean flag = true;
        float f = Math.abs(af[0]);
        if(af[0] == 0.0F && af[1] == 0.0F)
        {
            turretLRposW = 0.0F;
        } else
        {
            turretLRposW = (float)Math.cos(Math.toRadians(f));
            turretLRposW *= turretLRposW;
            turretLRposW *= turretLRposW;
            turretLRposW *= turretLRposW;
            if(af[0] < 0.0F)
                turretLRposW = turretLRposW - 1.0F;
            else
                turretLRposW = 1.0F - turretLRposW;
            if(turretState < 5 && (af[1] > -25F && f < 20F || af[1] > -5F))
                if(af[0] < 0.0F)
                    turretLRposW = -1F;
                else
                    turretLRposW = 1.0F;
        }
        if(turretLRposW < -1F)
            turretLRposW = -1F;
        if(turretLRposW > 1.0F)
            turretLRposW = 1.0F;
        turretLRpos = 0.95F * turretLRpos + 0.05F * turretLRposW;
        float f1 = 0.0F;
        if(f > 100F - 20F * turretFront)
            f1 = 1.0F;
        if(turretFront < 0.0F)
            turretFront = 0.0F;
        else
        if(turretFront > 1.0F)
            turretFront = 1.0F;
        if(f1 - turretFront > 0.01D || f1 - turretFront < -0.01D)
        {
            if(f1 > turretFront)
                turretFront += 0.15F * (0.3F - (0.5F - turretFront) * (0.5F - turretFront));
            else
                turretFront -= 0.15F * (0.3F - (0.5F - turretFront) * (0.5F - turretFront));
            if(f1 - turretFront > 0.1D || f1 - turretFront < -0.1D)
                flag = false;
        }
        return flag;
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        for(; af[0] > 180F; af[0] -= 360F);
        for(; af[0] < -180F; af[0] += 360F);
        float f = Math.abs(af[0]);
        if(af[1] < -50F)
        {
            af[1] = -50F;
            flag = false;
        }
        float f1;
        if(f < 7F)
        {
            if(f < 5F)
                f1 = -2F;
            else
                f1 = f * 7.5F - 39.5F;
        } else
        if(f < 55F)
            f1 = f + 6F;
        else
        if(f < 95F)
            f1 = 50F + f / 5F;
        else
        if(f < 155F)
            f1 = 206.7F - 1.46F * f;
        else
            f1 = -20F;
        if(af[1] > f1)
        {
            af[1] = f1;
            flag = false;
        }
        if(f < 34F && af[1] < 3F && af[1] > -3F)
            flag = false;
        if(f > 100F)
        {
            if(af[1] > -20F)
                flag = false;
        } else
        if(f > 90F && af[1] > 12F)
            flag = false;
        afBk[0] = af[0];
        afBk[1] = af[1];
        if(turretState != 4)
        {
            return false;
        } else
        {
            flag &= turretMoves(af);
            moveAndTurnTurret(af);
            return flag;
        }
    }

    private boolean anglesMoveTo(float af[], float f)
    {
        float f2 = f * MAX_DANGLE;
        finishedTurn = true;
        float f1;
        for(f1 = af[0] - tu[0]; f1 < -180F; f1 += 360F);
        for(; f1 > 180F; f1 -= 360F);
        if(f1 > f2)
        {
            f1 = f2;
            finishedTurn = false;
        }
        if(f1 < -f2)
        {
            f1 = -f2;
            finishedTurn = false;
        }
        tu[0] += f1;
        for(f1 = af[1] - tu[1]; f1 < -180F; f1 += 360F);
        for(; f1 > 180F; f1 -= 360F);
        if(f1 > f2)
        {
            f1 = f2;
            finishedTurn = false;
        }
        if(f1 < -f2)
        {
            f1 = -f2;
            finishedTurn = false;
        }
        tu[1] += f1;
        return finishedTurn;
    }

    private void gunStoreMoveAnim()
    {
        float f = fGunPos * fGunPos * (3F - 2.0F * fGunPos);
        hierMesh().chunkSetAngles("buben", 0.0F, 0.0F, 70F * (1.0F - f));
        if(fGunPos > 0.8F)
        {
            hierMesh().chunkSetAngles("Turret1B_D0X", 0.0F, 0.0F, 0.0F);
        } else
        {
            float f1 = 1.0F - fGunPos * 1.25F;
            hierMesh().chunkSetAngles("Turret1B_D0X", 0.0F, 70F * f1 * f1 * (2.0F * f1 - 3F), 0.0F);
        }
    }

    private void setGunnerSitting(boolean flag)
    {
        if(flag)
        {
            turretAng[0] = 180F;
            turretAng[1] = 0.0F;
            turretAng[2] = 15F;
            turretPos[0] = 0.0F;
            turretPos[1] = -0.3F;
            turretPos[2] = -0.15F;
            hierMesh().chunkSetLocate("Pilot2_D0", turretPos, turretAng);
            lookoutAz = 0.0F;
            lookoutEl = 0.0F;
            turnHead(lookoutAz, lookoutEl);
            lookoutAnim = -1F;
            lookoutTimeLeft = -1F;
        }
    }

    private boolean gunStoreTurnAnim(float af[], float f)
    {
        boolean flag;
        if(anglesMoveTo(af, f))
            flag = true;
        else
            flag = false;
        turretMoves(tu);
        moveAndTurnTurret(tu);
        return flag;
    }

    private void turnHead(float f, float f1)
    {
        turretAng[0] = 0.0F;
        turretAng[1] = f;
        turretAng[2] = 0.0F;
        turretPos[0] = 0.3F * (1.0F - (float)Math.cos(Math.toRadians(f)));
        turretPos[1] = 0.0F;
        turretPos[2] = -0.28F * (float)Math.sin(Math.toRadians(f));
        hierMesh().chunkSetLocate("Head2_D0", turretPos, turretAng);
    }

    void gunnerLookout(float f)
    {
        lookoutTimeLeft -= f;
        if(lookoutTimeLeft > 0.0F)
            return;
        if(lookoutAnim < 0.0F)
        {
            lookoutIndex++;
            if(lookoutIndex > 127)
                lookoutIndex = 0;
            lookoutTimeLeft = lookoutPos[2][lookoutIndex];
            float f1 = lookoutPos[0][lookoutIndex] - lookoutAz;
            float f3 = lookoutPos[1][lookoutIndex] - lookoutEl;
            lookoutAnim = 0.001F;
            lookout = 0.0F;
            if(f1 * f1 > f3 * f3)
                lookoutMax = Math.abs(f1);
            else
                lookoutMax = Math.abs(f3);
            if(lookoutMax == 0.0F)
                lookoutMax = 1E-005F;
            lookoutAzSpd = f1 / lookoutMax;
            lookoutElSpd = f3 / lookoutMax;
            return;
        }
        if(2.0F * lookout > lookoutMax)
            lookoutAnim -= f;
        else
            lookoutAnim += f;
        float f2 = 1.0F - 5F / (lookoutAnim + 5F);
        f2 = f * f2 * 800F;
        lookoutAz += f2 * lookoutAzSpd;
        lookoutEl += f2 * lookoutElSpd;
        lookout += f2;
        turnHead(lookoutAz, lookoutEl);
    }

    private void gunnerTurnInit(boolean flag)
    {
        if(flag)
        {
            turnPos = 1.0F;
        } else
        {
            lookoutAz = 0.0F;
            lookoutEl = 0.0F;
            turnPos = 0.0F;
        }
    }

    private void gunnerTurn(float f)
    {
        float f1 = f * f * (3F - 2.0F * f);
        turnHead(f1 * lookoutAz, f1 * lookoutEl);
        if(lookoutAz > 0.0F)
            turretAng[0] = f1 * 180F;
        else
            turretAng[0] = -f1 * 180F;
        turretAng[1] = 0.0F;
        turretAng[2] = f1 * 15F;
        turretPos[0] = 0.0F;
        turretPos[1] = -f1 * 0.3F;
        turretPos[2] = -f1 * 0.15F;
        hierMesh().chunkSetLocate("Pilot2_D0", turretPos, turretAng);
    }

    public void update(float f)
    {
        kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
        if(kangle > 1.0F)
            kangle = 1.0F;
        hierMesh().chunkSetAngles("radiator1_D0", 0.0F, -55F * kangle, 0.0F);
        hierMesh().chunkSetAngles("radiator2_D0", 0.0F, -40F * kangle, 0.0F);
        if(!bGunnerKilled && !FM.AS.isPilotParatrooper(FM.AS.astatePilotStates[1]))
        {
            if(FM.AS.isMaster() || NetMissionTrack.isPlaying())
            {
                if(turretState == 0 && (gunOutOverride == 1 || FM.turret[0].target != null))
                {
                    turretState = 1;
                    setGunnerSitting(false);
                    gunnerTurnInit(true);
                }
                if(Time.current() > btme)
                {
                    btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                    if(FM.turret[0].target == null && turretState == 4 && gunOutOverride == 0)
                    {
                        turretState = 5;
                        tu[0] = afBk[0];
                        tu[1] = afBk[1];
                        afBk[0] = 0.0F;
                        afBk[1] = 0.0F;
                    }
                }
            }
            switch(turretState)
            {
            case 4:
            default:
                break;

            case 0:
                gunnerLookout(f);
                break;

            case 1:
                turnPos -= f;
                if(turnPos < 0.0F)
                {
                    turretState = 2;
                    turnPos = 0.0F;
                }
                gunnerTurn(turnPos);
                break;

            case 2:
                fGunPos += 1.0F * f;
                gunStoreMoveAnim();
                if(fGunPos > 0.999D)
                    turretState = 3;
                break;

            case 3:
                if(gunStoreTurnAnim(afBk, f))
                    turretState = 4;
                break;

            case 5:
                if(gunStoreTurnAnim(afBk, f))
                    turretState = 6;
                break;

            case 6:
                if(turretLRpos < 0.1F && turretLRpos > -0.1F)
                {
                    fGunPos -= 1.0F * f;
                    gunStoreMoveAnim();
                } else
                {
                    gunStoreTurnAnim(afBk, f);
                }
                if(fGunPos < 0.001D)
                {
                    turretState = 7;
                    setGunnerSitting(true);
                    gunnerTurnInit(false);
                }
                break;

            case 7:
                turnPos += f;
                if(turnPos > 1.0F)
                {
                    turretState = 0;
                    turnPos = 1.0F;
                }
                gunnerTurn(turnPos);
                break;
            }
        }
        super.update(f);
    }

    public void missionStarting()
    {
        super.missionStarting();
        hierMesh().chunkVisible("pilot1_arm2_d0", true);
        hierMesh().chunkVisible("pilot1_arm1_d0", true);
    }

    public void prepareCamouflage()
    {
        super.prepareCamouflage();
        hierMesh().chunkVisible("pilot1_arm2_d0", true);
        hierMesh().chunkVisible("pilot1_arm1_d0", true);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1"))
                    getEnergyPastArmor(9.96F / (1E-005F + (float)Math.abs(Aircraft.v1.x)), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(s.endsWith("7"))
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#7)");
                    }
                } else
                if(s.endsWith("8"))
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#8)");
                    }
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Arone Controls Out.. (#8)");
                    }
                } else
                if(s.endsWith("5"))
                {
                    if(World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#5)");
                    }
                } else
                if(s.endsWith("6"))
                {
                    if(World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#6)");
                    }
                } else
                if((s.endsWith("2") || s.endsWith("4")) && World.Rnd().nextFloat() < 0.5F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Arone Controls Out.. (#2/#4)");
                }
                return;
            }
            if(s.startsWith("xxeng") || s.startsWith("xxEng"))
            {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if(s.endsWith("prop"))
                    Aircraft.debugprintln(this, "*** Prop hit");
                else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(2.1F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                        }
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12.7F, shot);
                } else
                if(s.startsWith("xxeng1cyls"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.12F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if(World.Rnd().nextFloat() < 0.005F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        getEnergyPastArmor(22.5F, shot);
                    }
                } else
                if(s.endsWith("Oil1"))
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
                return;
            }
            if(s.startsWith("xxoil"))
            {
                FM.AS.hitOil(shot.initiator, 0);
                getEnergyPastArmor(0.22F, shot);
                Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
            } else
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.4F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F)
                {
                    if(FM.AS.astateTankStates[i] == 0)
                    {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        FM.AS.hitTank(shot.initiator, i, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.hitTank(shot.initiator, i, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
            } else
            if(s.startsWith("xxlock"))
            {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                } else
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                } else
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
            } else
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("01"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Gun #1: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Gun #2: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            } else
            if(s.startsWith("xxspar"))
            {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(9.5F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                } else
                if(s.startsWith("xxsparli") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D" + chunkDamageVisible("WingLIn"), shot.initiator);
                } else
                if(s.startsWith("xxspar2i") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D" + chunkDamageVisible("WingRIn"), shot.initiator);
                } else
                if(s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                } else
                if(s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                } else
                if(s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                } else
                if(s.startsWith("xxsparro") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                }
            }
            return;
        }
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int j;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else
            {
                j = s.charAt(5) - 49;
            }
            Aircraft.debugprintln(this, "*** hitFlesh..");
            hitFlesh(j, shot, byte0);
        } else
        if(s.startsWith("xcf") || s.startsWith("xcockpit"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xturret1b"))
        {
            Aircraft.debugprintln(this, "*** Turret Gun: Disabled.. (xturret1b)");
            FM.AS.setJamBullets(10, 0);
            getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
        } else
        if(s.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xWing"))
        {
            Aircraft.debugprintln(this, "*** xWing: " + s);
            if(s.startsWith("xWingLIn") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xWingRIn") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xWingLmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xWingRmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
        } else
        if(s.startsWith("xwing"))
        {
            Aircraft.debugprintln(this, "*** xwing: " + s);
            if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
            {
                hitChunk("AroneL1", shot);
                hitChunk("AroneL2", shot);
            }
            if(s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
            {
                hitChunk("AroneR1", shot);
                hitChunk("AroneR2", shot);
            }
        }
    }

    float lookoutTimeLeft;
    float lookoutAz;
    float lookoutEl;
    float lookoutAnim;
    float lookoutMax;
    float lookoutAzSpd;
    float lookoutElSpd;
    int lookoutIndex;
    float lookoutPos[][];
    private float turnPos;
    private float lookout;
    float turretLRpos;
    float turretFront;
    float turretLRposW;
    float turretPos[];
    float turretAng[];
    float tu[];
    float afBk[];
    protected float kangle;
    private int turretState;
    private static final int SLEEPING = 0;
    private static final int GOING_OUT_M = 1;
    private static final int GOING_OUT_PULL = 2;
    private static final int GOING_OUT_T = 3;
    private static final int ACTIVE = 4;
    private static final int GOING_IN_T = 5;
    private static final int GOING_IN_PULL = 6;
    private static final int GOING_IN_M = 7;
    private float fGunPos;
    private boolean finishedTurn;
    private long btme;
    private static float MAX_DANGLE = 100F;
    protected int gunOutOverride;
    boolean bGunnerKilled;

    static 
    {
        Class class1 = Letov.class;
        Property.set(class1, "originCountry", PaintScheme.countrySlovakia);
    }
}
