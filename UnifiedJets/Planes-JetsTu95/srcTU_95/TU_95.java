// Source File Name:   TU_95.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            TU_95X, Aircraft, TypeTankerDrogue, TypeDockable, 
//            PaintSchemeBMPar05, PaintSchemeFMPar06, TypeBomber, TypeGuidedMissileCarrier, 
//            TypeX4Carrier, TypeFastJet, TypeSupersonic, NetAircraft

public class TU_95 extends TU_95X
    implements TypeBomber, TypeGuidedMissileCarrier, TypeX4Carrier, TypeFastJet, TypeSupersonic, TypeDockable
{

    public TU_95()
    {
        SonicBoom = 0.0F;
        guidedMissileUtils = new GuidedMissileUtils(this);
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
    }

    public float getAirPressure(float f)
    {
        float f1 = 1.0F - (0.0065F * f) / 288.15F;
        float f2 = 5.255781F;
        return 101325F * (float)Math.pow(f1, f2);
    }

    public float getAirPressureFactor(float f)
    {
        return getAirPressure(f) / 101325F;
    }

    public float getAirDensity(float f)
    {
        return (getAirPressure(f) * 0.0289644F) / (8.31447F * (288.15F - 0.0065F * f));
    }

    public float getAirDensityFactor(float f)
    {
        return getAirDensity(f) / 1.225F;
    }

    public float getMachForAlt(float f)
    {
        f /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length; i++)
            if(TypeSupersonic.fMachAltX[i] > f)
                break;

        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + f2 * f5;
        }
    }

    public float getMpsFromKmh(float f)
    {
        return f / 3.6F;
    }

    public float calculateMach()
    {
        return super.FM.getSpeedKMH() / getMachForAlt(super.FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(super.FM.getAltitude()) - super.FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f1 = super.FM.getSpeedKMH() - getMachForAlt(super.FM.getAltitude());
        if(f1 < 0.5F)
            f1 = 0.5F;
        if((double)calculateMach() <= 1.0D)
        {
            super.FM.VmaxAllowed = super.FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if((double)calculateMach() >= 1.0D)
        {
            super.FM.VmaxAllowed = super.FM.getSpeedKMH() + f1;
            isSonic = true;
        }
        if(((FlightModelMain) (super.FM)).VmaxAllowed > 1500F)
            super.FM.VmaxAllowed = 1500F;
        if(isSonic && SonicBoom < 1.0F)
        {
            super.playSound("aircraft.SonicBoom", true);
            super.playSound("aircraft.SonicBoomInternal", true);
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log("Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(super.FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if((double)calculateMach() > 1.01D || (double)calculateMach() < 1.0D)
            Eff3DActor.finish(shockwave);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !flag || !(super.FM instanceof Pilot))
            return;
        if(flag && ((FlightModelMain) (super.FM)).AP.way.curr().Action == 3 && typeDockableIsDocked() && Math.abs(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).Or.getKren()) < 3F)
            if(super.FM.isPlayers())
            {
                if((super.FM instanceof RealFlightModel) && !((RealFlightModel)super.FM).isRealMode())
                {
                    typeDockableAttemptDetach();
                    ((Maneuver)super.FM).set_maneuver(22);
                    ((Maneuver)super.FM).setCheckStrike(false);
                    ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
                    dtime = Time.current();
                }
            } else
            {
                typeDockableAttemptDetach();
                ((Maneuver)super.FM).set_maneuver(22);
                ((Maneuver)super.FM).setCheckStrike(false);
                ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
                dtime = Time.current();
            }
        for(int i = 1; i < 7; i++)
            if(super.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(super.FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(super.FM.isPlayers())
            bChangedPit = true;
    }

    private String getPropMeshName(int i, int j)
    {
        String s = "Prop";
        if(j == 1)
            s = s + "Rot";
        s = s + (i + 1) + "_D";
        if(j == 2)
            s = s + "1";
        else
            s = s + "0";
        return s;
    }

    protected void moveFan(float f)
    {
        if(!Config.isUSE_RENDER())
            return;
        int i = FM.EI.getNum();
        if(oldProp.length < i * 2)
        {
            oldProp = new int[i * 2];
            propPos = new float[i * 2];
            for(int j = 0; j < i * 2; j++)
            {
                oldProp[j] = 0;
                propPos[j] = World.Rnd().nextFloat(-180F, 180F);
            }

        }
        for(int k = 0; k < FM.EI.getNum(); k++)
        {
            int l = FM.EI.engines[k].getStage();
            if(l > 0 && l < 6)
                f = 0.005F * (float)l;
            for(int i1 = 0; i1 < 2; i1++)
            {
                hierMesh().chunkFind(getPropMeshName(i1, k));
                int j1 = k * 2 + i1;
                int k1 = oldProp[j1];
                if(k1 < 2)
                {
                    k1 = Math.abs((int)(FM.EI.engines[k].getPropw() * 0.06F));
                    if(k1 >= 1)
                        k1 = 1;
                    if(k1 != oldProp[j1] && hierMesh().isChunkVisible(getPropMeshName(j1, oldProp[j1])))
                    {
                        hierMesh().chunkVisible(getPropMeshName(j1, oldProp[j1]), false);
                        hierMesh().chunkVisible(getPropMeshName(j1, k1), true);
                        oldProp[j1] = k1;
                    }
                }
                if(k1 == 0)
                {
                    propPos[j1] = (propPos[j1] + 57.3F * FM.EI.engines[k].getPropw() * f) % 360F;
                } else
                {
                    float f1 = 57.3F * FM.EI.engines[k].getPropw();
                    f1 %= 2880F;
                    f1 /= 2880F;
                    if(f1 <= 0.5F)
                        f1 *= 2.0F;
                    else
                        f1 = f1 * 2.0F - 2.0F;
                    f1 *= 1200F;
                    propPos[j1] = (propPos[j1] + f1 * f) % 360F;
                }
                if(i1 == 0)
                    hierMesh().chunkSetAngles(getPropMeshName(j1, k1), 0.0F, propPos[j1], 0.0F);
                else
                    hierMesh().chunkSetAngles(getPropMeshName(j1, k1), 0.0F, -propPos[j1], 0.0F);
            }

        }

    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 2: // '\003'
            super.FM.turret[0].bIsOperable = false;
            break;

        case 3: // '\004'
            super.FM.turret[1].bIsOperable = false;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
        hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33: // '!'
            hitProp(1, j, actor);
            ((FlightModelMain) (super.FM)).EI.engines[1].setEngineStuck(actor);
            ((FlightModelMain) (super.FM)).AS.hitTank(actor, 1, World.Rnd().nextInt(0, 9));
            // fall through

        case 34: // '"'
            hitProp(0, j, actor);
            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineStuck(actor);
            ((FlightModelMain) (super.FM)).AS.hitTank(actor, 0, World.Rnd().nextInt(2, 8));
            ((FlightModelMain) (super.FM)).AS.hitTank(actor, 1, World.Rnd().nextInt(0, 5));
            // fall through

        case 35: // '#'
            ((FlightModelMain) (super.FM)).AS.hitTank(actor, 0, World.Rnd().nextInt(0, 4));
            break;

        case 36: // '$'
            hitProp(2, j, actor);
            ((FlightModelMain) (super.FM)).EI.engines[2].setEngineStuck(actor);
            ((FlightModelMain) (super.FM)).AS.hitTank(actor, 2, World.Rnd().nextInt(0, 9));
            // fall through

        case 37: // '%'
            hitProp(3, j, actor);
            ((FlightModelMain) (super.FM)).EI.engines[3].setEngineStuck(actor);
            ((FlightModelMain) (super.FM)).AS.hitTank(actor, 2, World.Rnd().nextInt(0, 5));
            ((FlightModelMain) (super.FM)).AS.hitTank(actor, 3, World.Rnd().nextInt(2, 8));
            // fall through

        case 38: // '&'
            ((FlightModelMain) (super.FM)).AS.hitTank(actor, 3, World.Rnd().nextInt(0, 4));
            break;

        case 25: // '\031'
            super.FM.turret[0].bIsOperable = false;
            return false;

        case 26: // '\032'
            super.FM.turret[1].bIsOperable = false;
            return false;

        case 19: // '\023'
            killPilot(this, 5);
            killPilot(this, 6);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean typeBomberToggleAutomation()
    {
        bSightAutomation = !bSightAutomation;
        bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (bSightAutomation ? "ON" : "OFF"));
        return bSightAutomation;
    }

    public void typeBomberAdjDistanceReset()
    {
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus()
    {
        fSightCurForwardAngle++;
        if(fSightCurForwardAngle > 85F)
            fSightCurForwardAngle = 85F;
        fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)fSightCurForwardAngle)
        });
        if(bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle--;
        if(fSightCurForwardAngle < -15F)
            fSightCurForwardAngle = -15F;
        fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)fSightCurForwardAngle)
        });
        if(bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        fSightCurSideslip += 0.1F;
        if(fSightCurSideslip > 3F)
            fSightCurSideslip = 3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 10F))
        });
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip -= 0.1F;
        if(fSightCurSideslip < -3F)
            fSightCurSideslip = -3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 10F))
        });
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 850F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 10F;
        if(fSightCurAltitude > 10000F)
            fSightCurAltitude = 10000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
        fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 10F;
        if(fSightCurAltitude < 850F)
            fSightCurAltitude = 850F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
        fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 150F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 10F;
        if(fSightCurSpeed > 800F)
            fSightCurSpeed = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 10F;
        if(fSightCurSpeed < 150F)
            fSightCurSpeed = 150F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberUpdate(float f)
    {
        if((double)Math.abs(((FlightModelMain) (super.FM)).Or.getKren()) > 4.5D)
        {
            fSightCurReadyness -= 0.0666666F * f;
            if(fSightCurReadyness < 0.0F)
                fSightCurReadyness = 0.0F;
        }
        if(fSightCurReadyness < 1.0F)
            fSightCurReadyness += 0.0333333F * f;
        else
        if(bSightAutomation)
        {
            fSightCurDistance -= (fSightCurSpeed / 3.6F) * f;
            if(fSightCurDistance < 0.0F)
            {
                fSightCurDistance = 0.0F;
                typeBomberToggleAutomation();
            }
            fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(fSightCurDistance / fSightCurAltitude));
            if((double)fSightCurDistance < (double)(fSightCurSpeed / 3.6F) * Math.sqrt(fSightCurAltitude * 0.203874F))
                bSightBombDump = true;
            if(bSightBombDump)
                if(super.FM.isTick(3, 0))
                {
                    if(((FlightModelMain) (super.FM)).CT.Weapons[3] != null && ((FlightModelMain) (super.FM)).CT.Weapons[3][((FlightModelMain) (super.FM)).CT.Weapons[3].length - 1] != null && ((FlightModelMain) (super.FM)).CT.Weapons[3][((FlightModelMain) (super.FM)).CT.Weapons[3].length - 1].haveBullets())
                    {
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else
                {
                    ((FlightModelMain) (super.FM)).CT.WeaponControl[3] = false;
                }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(fSightCurDistance);
        netmsgguaranted.writeByte((int)fSightCurForwardAngle);
        netmsgguaranted.writeByte((int)((fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeByte((int)(fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int)(fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        int i = netmsginput.readUnsignedByte();
        bSightAutomation = (i & 1) != 0;
        bSightBombDump = (i & 2) != 0;
        fSightCurDistance = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readUnsignedByte();
        fSightCurSideslip = -3F + (float)netmsginput.readUnsignedByte() / 33.33333F;
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = (float)netmsginput.readUnsignedByte() * 2.5F;
        fSightCurReadyness = (float)netmsginput.readUnsignedByte() / 200F;
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f)
    {
        if(bNeedSetup)
            checkAsDrone();
        int i = aircIndex();
        if(super.FM instanceof Maneuver)
            if(typeDockableIsDocked())
            {
                if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
                {
                    ((Maneuver)super.FM).unblock();
                    ((Maneuver)super.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((Maneuver)super.FM).push(48);

                    if(((FlightModelMain) (super.FM)).AP.way.curr().Action != 3)
                        ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                    ((Pilot)super.FM).setDumbTime(3000L);
                }
                if(((FlightModelMain) (super.FM)).M.fuel < ((FlightModelMain) (super.FM)).M.maxFuel)
                    ((FlightModelMain) (super.FM)).M.fuel += 20F * f;
            } else
            if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
            {
                if(((FlightModelMain) (super.FM)).CT.GearControl == 0.0F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 0)
                    ((FlightModelMain) (super.FM)).EI.setEngineRunning();
                if(dtime > 0L && ((Maneuver)super.FM).Group != null)
                {
                    ((Maneuver)super.FM).Group.leaderGroup = null;
                    ((Maneuver)super.FM).set_maneuver(22);
                    ((Pilot)super.FM).setDumbTime(3000L);
                    if(Time.current() > dtime + 3000L)
                    {
                        dtime = -1L;
                        ((Maneuver)super.FM).clear_stack();
                        ((Maneuver)super.FM).set_maneuver(0);
                        ((Pilot)super.FM).setDumbTime(0L);
                    }
                } else
                if(((FlightModelMain) (super.FM)).AP.way.curr().Action == 0)
                {
                    Maneuver maneuver = (Maneuver)super.FM;
                    if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                        maneuver.Group.setGroupTask(2);
                }
            }
        guidedMissileUtils.update();
        super.update(f);
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        super.msgCollisionRequest(actor, aflag);
        if(queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
            aflag[0] = false;
        else
            aflag[0] = true;
    }

    public void missionStarting()
    {
        checkAsDrone();
    }

    private void checkAsDrone()
    {
        if(target_ == null)
        {
            if(((FlightModelMain) (super.FM)).AP.way.curr().getTarget() == null)
                ((FlightModelMain) (super.FM)).AP.way.next();
            target_ = ((FlightModelMain) (super.FM)).AP.way.curr().getTarget();
            if(Actor.isValid(target_) && (target_ instanceof Wing))
            {
                Wing wing = (Wing)target_;
                int i = aircIndex();
                if(Actor.isValid(wing.airc[i / 2]))
                    target_ = wing.airc[i / 2];
                else
                    target_ = null;
            }
        }
        if(Actor.isValid(target_) && (target_ instanceof TypeTankerDrogue))
        {
            queen_last = target_;
            queen_time = Time.current();
            if(isNetMaster())
                ((TypeDockable)target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
        }
        bNeedSetup = false;
        target_ = null;
    }

    public int typeDockableGetDockport()
    {
        if(typeDockableIsDocked())
            return dockport_;
        else
            return -1;
    }

    public Actor typeDockableGetQueen()
    {
        return queen_;
    }

    public boolean typeDockableIsDocked()
    {
        return Actor.isValid(queen_);
    }

    public void typeDockableAttemptAttach()
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
            ((TypeDockable)queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(Actor actor)
    {
    }

    public void typeDockableRequestDetach(Actor actor)
    {
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i)
    {
        queen_ = actor;
        dockport_ = i;
        queen_last = queen_;
        queen_time = 0L;
        ((FlightModelMain) (super.FM)).EI.setEngineRunning();
        ((FlightModelMain) (super.FM)).CT.setGearAirborne();
        moveGear(0.0F);
        FlightModel flightmodel = ((SndAircraft) ((Aircraft)queen_)).FM;
        if(aircIndex() == 0 && (super.FM instanceof Maneuver) && (flightmodel instanceof Maneuver))
        {
            Maneuver maneuver = (Maneuver)flightmodel;
            Maneuver maneuver1 = (Maneuver)super.FM;
            if(maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1)
            {
                AirGroup airgroup = new AirGroup(maneuver1.Group);
                maneuver1.Group.delAircraft(this);
                airgroup.addAircraft(this);
                airgroup.attachGroup(maneuver.Group);
                airgroup.rejoinGroup = null;
                airgroup.leaderGroup = null;
                airgroup.clientGroup = maneuver.Group;
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i)
    {
        if(dockport_ == i)
        {
            queen_last = queen_;
            queen_time = Time.current();
            queen_ = null;
            dockport_ = 0;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        if(typeDockableIsDocked())
        {
            netmsgguaranted.writeByte(1);
            ActorNet actornet = null;
            if(Actor.isValid(queen_))
            {
                actornet = queen_.net;
                if(actornet.countNoMirrors() > 0)
                    actornet = null;
            }
            netmsgguaranted.writeByte(dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else
        {
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        if(netmsginput.readByte() == 1)
        {
            dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if(netobj != null)
            {
                Actor actor = (Actor)netobj.superObj();
                ((TypeDockable)actor).typeDockableDoAttachToDrone(this, dockport_);
            }
        }
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 1.0F;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -1F;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 1.0F;
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -1F;
    }

    public void typeX4CResetControls()
    {
        deltaAzimuth = deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth()
    {
        return deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage()
    {
        return deltaTangage;
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunVYakNS23", 315);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(10, "MGunVYakNS23", 315);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(11, "MGunVYakNS23", 315);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(11, "MGunVYakNS23", 315);
        }
        catch(Exception exception) { }
        return a_lweaponslot;
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    public static boolean bChangedPit = false;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    private GuidedMissileUtils guidedMissileUtils;
    private float deltaAzimuth;
    private float deltaTangage;
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.TU_95.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "TU-95");
        Property.set(class1, "meshName", "3DO/Plane/Tu-95/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/TU_95FM.fmd:TU95_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitTU95.class, com.maddox.il2.objects.air.CockpitTU95_Bombardier.class, com.maddox.il2.objects.air.CockpitTU95_RGunner.class, com.maddox.il2.objects.air.CockpitTU95_AGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 10, 11, 11,  3,  3,  2,  2,  2,  2,
             9,  9,  2,  2,  2,  2,  2,  2,  2,  2,
             9,  9,  2,  2,  2,  2,  2,  2,  2,  2,
             2,  2,  2,  2,  2,  2,  2,  2,  2,  2,
             2,  2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN09",         "_MGUN10",         "_MGUN11",         "_MGUN12",         "_BombSpawn01",    "_BombSpawn02",    "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", 
            "_ExternalDev01",  "_ExternalDev02",  "_ExternalRock03", "_ExternalRock03", "_ExternalRock12", "_ExternalRock12", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07",
            "_ExternalDev06",  "_ExternalDev07",  "_ExternalRock08", "_ExternalRock08", "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", 
            "_ExternalDev08",  "_ExternalDev09",  "_ExternalRock13", "_ExternalRock13", "_ExternalRock14", "_ExternalRock14", "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05",
            "_InternalRock04", "_InternalRock04"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 42;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "120xFAB-100";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 60);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 60);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "48xFAB-250";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250int", 24);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250int", 24);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "48xFAB-250M-46";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 24);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 24);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xFAB-500";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB500int", 12);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB500int", 12);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xFAB-1000";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB1000int", 12);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB-2000";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB2000", 6);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-5000";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB5000", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xRDS-4T";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRDS4T", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRDS-4T";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRDS4T", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xKh-20";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunKh20", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xKh-22";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunKh22", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunKh22", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonTu95Kh22", 0);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonTu95Kh22", 0);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunKh22", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xKh-55";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 6);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16xKh-55";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55In", 0);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55In", 0);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55Out", 0);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55Out", 0);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh55", 6);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xKh-15";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 6);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16xKh-15";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55In", 0);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55In", 0);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55Out", 0);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55Out", 0);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh15", 6);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xKh-15S";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 6);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16xKh-15S";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55In", 0);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55In", 0);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55Out", 0);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonTu95Kh55Out", 0);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh15S", 6);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}