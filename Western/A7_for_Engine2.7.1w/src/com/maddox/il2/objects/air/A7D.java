
package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F_86F, Aircraft, TypeTankerBoom, TypeDockable, 
//            TypeTankerDrogue, PaintSchemeFMPar06, TypeX4Carrier, TypeGuidedMissileCarrier, 
//            TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeFastJet, 
//            Cockpit, NetAircraft

public class A7D extends F_86F
    implements TypeDockable, TypeX4Carrier, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeFastJet
{

    public A7D()
    {
        guidedMissileUtils = new GuidedMissileUtils(this);
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        arrestor = 0.0F;
        bToFire = false;
        kangle = 0.0F;
        tX4Prev = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 150F;
        fSightCurReadyness = 0.0F;
        APmode1 = false;
        APmode2 = false;
        APmode3 = false;
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
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
        if(fSightCurForwardAngle < 0.0F)
            fSightCurForwardAngle = 0.0F;
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
        if(fSightCurAltitude > 6000F)
            fSightCurAltitude = 6000F;
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
        fSightCurSpeed = 250F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 10F;
        if(fSightCurSpeed > 900F)
            fSightCurSpeed = 900F;
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
            if((double)fSightCurDistance < (double)(fSightCurSpeed / 3.6F) * Math.sqrt(fSightCurAltitude * 0.2038736F))
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
        netmsgguaranted.writeFloat(fSightCurSpeed);
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
        fSightCurSpeed = netmsginput.readFloat();
        fSightCurReadyness = (float)netmsginput.readUnsignedByte() / 200F;
    }

    public void setCommonThreatActive()
    {
        long l = Time.current();
        if(l - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = l;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long l = Time.current();
        if(l - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = l;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long l = Time.current();
        if(l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = l;
            doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat()
    {
    }

    private void doDealRadarLockThreat()
    {
    }

    private void doDealMissileLaunchThreat()
    {
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(1.5F, 1.5F, 1.0F, 2.0F, 2.0F, 2.0F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 0.1F;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.1F;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.1F;
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.1F;
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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.CT.bHasBombSelect = true;
    }

    public void update(float f)
    {
        computeCy();
        computeEngine();
        setSubsonicLimiter();
        if(bNeedSetup)
            checkAsDrone();
        guidedMissileUtils.update();
        int i = aircIndex();
        if(super.FM instanceof Maneuver)
            receivingRefuel(f);
        if(((FlightModelMain) (super.FM)).CT.getArrestor() > 0.2F)
            calculateArrestor();
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
        super.missionStarting();
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
        if(Actor.isValid(target_) && (target_ instanceof TypeTankerBoom))
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
        if(FM.AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerBoom)
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
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
        FM.EI.setEngineRunning();
        FM.CT.setGearAirborne();
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
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC02_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR03_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL03_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC01_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearL02_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearL01_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearR02_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearR01_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 80F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        float f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[2], 0.19075F, 0.0F, 0.0F, 1.0F);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        hierMesh().chunkSetLocate("GearC03_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 85F), 0.0F);
    }

    public void moveWingFold(float f)
    {
        if(f < 0.001F)
        {
            setGunPodsOn(true);
            hideWingWeapons(false);
        } else
        {
            setGunPodsOn(false);
            ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
            hideWingWeapons(false);
        }
        moveWingFold(hierMesh(), f);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 40F * f, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 30F * f, 0.0F);
        hierMesh().chunkSetAngles("GearC03_D0", 0.0F, 30F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 45F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 45F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = 50F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -f1, 0.0F);
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, 0.9F);
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 45F * f, 0.0F);
        float f1 = (float)Math.sin(Aircraft.cvt(f, 0.4F, 0.99F, 0.0F, 3.141593F));
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -30F * f, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[2] = 0.1385F * f;
        arrestor = f;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            ((FlightModelMain) (super.FM)).CT.bHasArrestorControl = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void checkHydraulicStatus()
    {
        if(((FlightModelMain) (super.FM)).EI.engines[0].getStage() < 6 && ((FlightModelMain) (super.FM)).Gears.nOfGearsOnGr > 0)
        {
            super.hasHydraulicPressure = false;
            ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
            ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
            ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
            ((FlightModelMain) (super.FM)).CT.bHasArrestorControl = false;
        } else
        if(!super.hasHydraulicPressure)
        {
            super.hasHydraulicPressure = true;
            ((FlightModelMain) (super.FM)).CT.bHasAileronControl = true;
            ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = true;
            ((FlightModelMain) (super.FM)).CT.bHasAirBrakeControl = true;
            ((FlightModelMain) (super.FM)).CT.bHasArrestorControl = true;
        }
    }

    public void computeCy()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float f = calculateMach();
        if(calculateMach() < 0.0F);
        float f1 = 0.0F;
        if((double)f > 1.0D)
        {
            f1 = 0.6F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            f1 = ((9500F * f3 - 21285F * f2) + 8833F * f + 4752F) / 3600F;
        }
        polares.CyCritH_0 = f1;
    }

    public void computeEngine()
    {
        if(super.FM.getAltitude() >= 0.0F && (double)calculateMach() <= 0.34999999999999998D && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 0.8F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 6000D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() == 6)
            if((double)f > 13.5D)
            {
                f1 = 11F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = ((374F * f3 - 8417F * f2) + 54708F * f) / 11340F;
            }
        FM.producedAF.x -= f1 * 1000F;
    }

    private void setSubsonicLimiter()
    {
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.96999999999999997D && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.003F;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude ON");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude OFF");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction ON");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(true);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction OFF");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(false);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = true;
            }
        if(i == 22)
            if(!APmode3)
            {
                APmode3 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route ON");
                ((FlightModelMain) (super.FM)).AP.setWayPoint(true);
            } else
            if(APmode3)
            {
                APmode3 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route OFF");
                ((FlightModelMain) (super.FM)).AP.setWayPoint(false);
                ((FlightModelMain) (super.FM)).CT.AileronControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.ElevatorControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.RudderControl = 0.0F;
            }
        if(i == 23)
        {
            ((FlightModelMain) (super.FM)).CT.AileronControl = 0.0F;
            ((FlightModelMain) (super.FM)).CT.ElevatorControl = 0.0F;
            ((FlightModelMain) (super.FM)).CT.RudderControl = 0.0F;
            ((FlightModelMain) (super.FM)).AP.setWayPoint(false);
            ((FlightModelMain) (super.FM)).AP.setStabDirection(false);
            ((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
            APmode1 = false;
            APmode2 = false;
            APmode3 = false;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: All Off");
        }
    }

    private void receivingRefuel(float f)
    {
        int i = aircIndex();

        if(typeDockableIsDocked())
        {
            if(false)   // FM.CT.getRefuel() < 0.9F
            {
                typeDockableAttemptDetach();
                return;
            } else
            {
                if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
                {
                    ((Maneuver)super.FM).unblock();
                    ((Maneuver)super.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((Maneuver)super.FM).push(48);

                    if(FM.AP.way.curr().Action != 3)
                        ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                    ((Pilot)FM).setDumbTime(3000L);
                }
                FuelTank fuelTanks[];
                fuelTanks = FM.CT.getFuelTanks();
                if(FM.M.fuel < FM.M.maxFuel - 31F)
                {
                    float getFuel = ((TypeTankerBoom) (queen_)).requestRefuel((Aircraft) this, 30.093F, f);
                    FM.M.fuel += getFuel;
                }
                else if(fuelTanks.length > 0 && fuelTanks[0] != null && !FM.M.bFuelTanksDropped)
                {
                    float freeTankSum = 0F;
                    for(int num = 0; num < fuelTanks.length; num++)
                        freeTankSum += fuelTanks[num].checkFreeTankSpace();
                    if(freeTankSum < 31F)
                    {
                        typeDockableAttemptDetach();
                        return;
                    }
                    float getFuel = ((TypeTankerBoom) (queen_)).requestRefuel((Aircraft) this, 30.093F, f);
                    for(int num = 0; num < fuelTanks.length; num++)
                        fuelTanks[num].doRefuel(getFuel * (fuelTanks[num].checkFreeTankSpace() / freeTankSum));
                }
                else
                {
                    typeDockableAttemptDetach();
                    return;
                }
            }
        } else
        if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
        {
            if(FM.CT.GearControl == 0.0F && FM.EI.engines[0].getStage() == 0)
                FM.EI.setEngineRunning();
            if(dtime > 0L && ((Maneuver)super.FM).Group != null)
            {
                ((Maneuver)super.FM).Group.leaderGroup = null;
                ((Maneuver)super.FM).set_maneuver(22);
                ((Pilot)FM).setDumbTime(3000L);
                if(Time.current() > dtime + 3000L)
                {
                    dtime = -1L;
                    ((Maneuver)super.FM).clear_stack();
                    ((Maneuver)super.FM).set_maneuver(0);
                    ((Pilot)FM).setDumbTime(0L);
                }
            } else
            if(FM.AP.way.curr().Action == 0)
            {
                Maneuver maneuver = (Maneuver)super.FM;
                if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                    maneuver.Group.setGroupTask(2);
            }
        }
    }

    private void calculateArrestor()
    {
        if(FM.Gears.arrestorVAngle != 0.0F)
        {
            float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            arrestor = 0.8F * arrestor + 0.2F * f1;
            moveArrestorHook(arrestor);
        } else
        {
            float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
            if(f2 < 0.0F && FM.getSpeedKMH() > 60F)
                Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if(f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
                f2 = 0.0F;
            if(f2 > 0.2F)
                f2 = 0.2F;
            if(f2 > 0.0F)
                arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
            else
                arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
            if(arrestor < 0.0F)
                arrestor = 0.0F;
            else
            if(arrestor > 1.0F)
                arrestor = 1.0F;
            moveArrestorHook(arrestor);
        }
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

    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    public boolean bToFire;
    private float arrestor;
    private long tX4Prev;
    private float kangle;
    private float deltaAzimuth;
    private float deltaTangage;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public static boolean bChangedPit = false;
    public boolean APmode1;
    public boolean APmode2;
    public boolean APmode3;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.A7D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-7D");
        Property.set(class1, "meshName", "3DO/Plane/A7D/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1966.9F);
        Property.set(class1, "yearExpired", 1994.3F);
        Property.set(class1, "FlightModel", "FlightModels/A7D_E.fmd:A7_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitA7A.class, com.maddox.il2.objects.air.CockpitA7D_Bombardier.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 2, 2, 2, 2, 9, 9, 
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 9, 9, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 
            9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev03", "_ExternalDev04", 
            "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", 
            "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", 
            "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev19", "_ExternalDev20", "_ExternalBomb01", "_ExternalBomb02", 
            "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", 
            "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", 
            "_ExternalBomb23", "_ExternalBomb24", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", 
            "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", 
            "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalDev21", "_ExternalDev22", "_ExternalPylon1", "_ExternalPylon2", 
            "_Pylon3", "_Pylon4", "_ExternalPylon5", "_ExternalPylon6", "_Pylon7", "_Pylon8", "_ExternalmK1", "_ExternalmK2", "_mK3", "_mK4", 
            "_ExternalmK5", "_ExternalmK6", "_mK7", "_mK8", "_ExternalmK9", "_ExternalmK10", "_mK11", "_mK12", "External_mK82_13", "External_mK82_14", 
            "_mK82_15", "_mK82_16", "External_mK82_17", "_ExternalmK18", "_mK19", "_mK20", "_ExternalmK21", "_ExternalmK22", "_mK23", "_mK24"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 120;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAGM12C";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunAGM12", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM12", 1);
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM12C+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM12C+4xMk82+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM12C+16xMk82";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM12C+4xM117";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM12C+4xBLU2";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xBLU2";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xBLU2+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18xCBU24";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xCBU24+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk81";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk81+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk81+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18xMk82";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18xMk82+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[114] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[119] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xMk82";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[114] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[119] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "30xMk82";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[114] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[119] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xM117";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xM117+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM117+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk84+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xM117";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk83";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk84";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82+12xMk81";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[114] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[119] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82+18xMk81";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[114] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[119] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82+6xMk81+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[114] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[119] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18xMk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82SnakeEye+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82SnakeEye+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xAGM65B";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAGM65B+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "CAS_CBU24+AGM65B+Zuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "CAS_Mk82SnakeEye+AGM65B+Zuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "CAS_Mk82+AGM65B+Zuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "CAS_M117+AGM65B+Zuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "CAS_2xAGM12C+2xM117+Zuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "CAS_2xAGM12C+4xMk82+Zuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "CannonRocketSimpleZuni", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_Mk82+Fac";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunWPFAC", 7);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunWPFAC", 7);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FO_Mk82+Fo";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunWPFO", 7);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunWPFO", 7);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xNuke+dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61", 1040);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankAD4", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk12", 1);
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int i = 0; i < byte0; i++)
                a_lweaponslot[i] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}