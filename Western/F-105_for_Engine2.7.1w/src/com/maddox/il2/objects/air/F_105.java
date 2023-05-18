
package com.maddox.il2.objects.air;

import java.io.IOException;
import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.air.F_105D;
import com.maddox.il2.objects.air.F_105F;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;
import java.util.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, TypeSupersonic, TypeFighter, TypeBNZFighter,
//            TypeFighterAceMaker, TypeStormovik, TypeGSuit, TypeLaserSpotter,
//            Aircraft, Cockpit, PaintScheme, EjectionSeat

public class F_105 extends Scheme1
  implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeStormovik, TypeBomber, TypeGSuit, TypeLaserSpotter, TypeDockable, TypeX4Carrier, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeFastJet
{
    private class _cls0 {

        public void rs(int i)
        {
            if (i == 0 || i == 1)
                F_105.this.actl *= 0.68D;
            if (i == 31 || i == 32)
                F_105.this.ectl *= 0.68D;
            if (i == 15 || i == 16)
                F_105.this.rctl *= 0.68D;
        }



        private float lal;
        private float tal;
        private float bef;
        private float tef;
        private float bhef;
        private float thef;
        private float phef;
        private float mef;
        private float wef;
        private float lef;
        private float ftl;

        private _cls0(float f, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10)
        {
            lal = f;
            tal = f1;
            bef = f2;
            tef = f3;
            bhef = f4;
            thef = f5;
            phef = f6;
            mef = f7;
            wef = f8;
            lef = f9;
            ftl = f10;
        }
    }

    public float getDragForce(float f, float f1, float f2, float f3)
    {
        throw new UnsupportedOperationException("getDragForce not supported anymore.");
    }

    public float getDragInGravity(float f, float f1, float f2, float f3, float f4, float f5)
    {
        throw new UnsupportedOperationException("getDragInGravity supported anymore.");
    }

    public float getForceInGravity(float f, float f1, float f2)
    {
        throw new UnsupportedOperationException("getForceInGravity supported anymore.");
    }

    public float getDegPerSec(float f, float f1)
    {
        throw new UnsupportedOperationException("getDegPerSec supported anymore.");
    }

    public float getGForce(float f, float f1)
    {
        throw new UnsupportedOperationException("getGForce supported anymore.");
    }

    public F_105()
    {
        lLightHook = new Hook[4];
        SonicBoom = 0.0F;
        bSlatsOff = false;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        AirBrakeControl = 0.0F;
        overrideBailout = false;
        ejectComplete = false;
        lightTime = 0.0F;
        ft = 0.0F;
        mn = 0.0F;
        ts = false;
        ictl = false;
        engineSurgeDamage = 0.0F;
        hasHydraulicPressure = true;
        APmode1 = false;
        APmode2 = false;
        APmode3 = false;
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
        Flaps = false;
        Flaps2 = false;
        Flaps3 = false;
        DragChuteControl = 0.0F;
        obsLookTime = 0;
        obsLookAzimuth = 0.0F;
        obsLookElevation = 0.0F;
        obsAzimuth = 0.0F;
        obsElevation = 0.0F;
        obsAzimuthOld = 0.0F;
        obsElevationOld = 0.0F;
        obsMove = 0.0F;
        obsMoveTot = 0.0F;
        bObserverKilled = false;
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        FM.CT.bHasBombSelect = true;
        guidedMissileUtils.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        actl = FM.SensRoll;
        ectl = FM.SensPitch;
        rctl = FM.SensYaw;
        if(!(this instanceof com.maddox.il2.objects.air.F_105D))
            bTwoSeat = true;
        fAirbrakeCx = FM.Sq.dragAirbrakeCx;
        }

    public void checkHydraulicStatus()
    {
        if (FM.EI.engines[0].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
            hasHydraulicPressure = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.bHasRudderControl = false;
            FM.CT.AirBrakeControl = 0.14F;
        }
        else if (!hasHydraulicPressure)
        {
            hasHydraulicPressure = true;
            FM.CT.bHasAileronControl = true;
            FM.CT.bHasElevatorControl = true;
            FM.CT.bHasRudderControl = true;
            FM.CT.bHasAirBrakeControl = true;
            if(FM.CT.AirBrakeControl == 0.14F)  FM.CT.AirBrakeControl = 0.0F;
            lTimeLastHydraulicPressureRecover = Time.current();
        }
    }

    public void updateLLights()
    {
        pos.getRender(Actor._tmpLoc);
        if (lLight == null)
        {
            if (Actor._tmpLoc.getX() >= 1.0D)
            {
                lLight = new LightPointWorld[4];
                for (int i = 0; i < 4; i++)
                {
                    lLight[i] = new LightPointWorld();
                    lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    lLight[i].setEmit(0.0F, 0.0F);
                    try {
                        lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    } catch (Exception exception) {}
                }

            }
        }
        else
        {
            for (int j = 0; j < 4; j++)
                if (FM.AS.astateLandingLightEffects[j] != null)
                {
                    lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP1);
                    lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP2);
                    Engine.land();
                    if (Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL))
                    {
                        lLightPL.z++;
                        lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
                        lLight[j].setPos(lLightP2);
                        float f = (float) lLightP1.distance(lLightPL);
                        float f1 = f * 0.5F + 60F;
                        float f2 = 0.7F - (0.8F * f * lightTime) / 2000F;
                        lLight[j].setEmit(f2, f1);
                    }
                    else
                    {
                        lLight[j].setEmit(0.0F, 0.0F);
                    }
                }
                else if (lLight[j].getR() != 0.0F)
                    lLight[j].setEmit(0.0F, 0.0F);

        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if (FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if (FM.isPlayers())
            bChangedPit = true;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if (FM.Gears.onGround() && FM.CT.getCockpitDoor() == 1.0F)
        {
            hierMesh().chunkVisible("HMask1_D0", false);
            if(bTwoSeat)
                hierMesh().chunkVisible("HMask2_D0", false);
        }
        else
        {
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
            if(bTwoSeat)
                hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
        }
        if (FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = FM.getVflow();
            mn = (float) vector3d.lengthSquared();
            mn = (float) Math.sqrt(mn);
            F_105 f_105 = this;
            float f1 = mn;
            World.cur().getClass();
            f_105.mn = f1 / Atmosphere.sonicSpeed((float) FM.Loc.z);
            if (mn >= 0.9F && mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
        }
        ft = World.getTimeofDay() % 0.01F;
        if (ft == 0.0F)
            UpdateLightIntensity();
        if(bTwoSeat && !bObserverKilled)
            observerLookAroundRA();
        if ((FM instanceof RealFlightModel) && ((RealFlightModel) super.FM).isRealMode() || !flag || !(super.FM instanceof Pilot))
            return;
        if (flag && FM.AP.way.curr().Action == 3 && typeDockableIsDocked() && Math.abs(((FlightModelMain) (((SndAircraft) ((Aircraft) queen_)).FM)).Or.getKren()) < 3F)
            if (super.FM.isPlayers())
            {
                if ((super.FM instanceof RealFlightModel) && !((RealFlightModel) super.FM).isRealMode())
                {
                    typeDockableAttemptDetach();
                    ((Maneuver) super.FM).set_maneuver(22);
                    ((Maneuver) super.FM).setCheckStrike(false);
                    FM.Vwld.z -= 5D;
                    dtime = Time.current();
                }
            }
            else
            {
                typeDockableAttemptDetach();
                ((Maneuver) super.FM).set_maneuver(22);
                ((Maneuver) super.FM).setCheckStrike(false);
                FM.Vwld.z -= 5D;
                dtime = Time.current();
            }
        if ((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
            aiAirbrakeRareAction();
    }

    private final void UpdateLightIntensity()
    {
        if (World.getTimeofDay() >= 6F && World.getTimeofDay() < 7F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 6F, 7F, 1.0F, 0.1F);
        else if (World.getTimeofDay() >= 18F && World.getTimeofDay() < 19F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 18F, 19F, 0.1F, 1.0F);
        else if (World.getTimeofDay() >= 7F && World.getTimeofDay() < 18F)
            lightTime = 0.1F;
        else
            lightTime = 1.0F;
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if (k14Mode > 2)
            k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if (k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if (k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if (k14WingspanType < 0)
            k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "SperryWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if (k14WingspanType > 9)
            k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "SperryWing" + k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    public void doMurderPilot(int i)
    {
        switch (i)
        {
            case 0: // '\0'
                hierMesh().chunkVisible("Pilot1_D0", false);
                hierMesh().chunkVisible("Head1_D0", false);
                hierMesh().chunkVisible("HMask1_D0", false);
                hierMesh().chunkVisible("Pilot1_D1", true);
                break;
            case 1: // '\1'
            if(bTwoSeat)
            {
                hierMesh().chunkVisible("Pilot2_D0", false);
                hierMesh().chunkVisible("Head2_D0", false);
                hierMesh().chunkVisible("HMask2_D0", false);
                hierMesh().chunkVisible("Pilot2_D1", true);
                bObserverKilled = true;
                break;
            }
        }
    }


    public void moveCockpitDoor(float f)
    {
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, (bTwoSeat ? 70F : 45F) * f, 0.0F);
        if(bTwoSeat)
            hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 70F * f, 0.0F);
        if (Config.isUSE_RENDER())
        {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }


    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        // Fully open Main Gear Fuselage Covers and keep them open,
        // "over-extend" opening angle by 5 degrees to "lean" covers against fuselage
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.15F, 0.26F, 0.0F, -89F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.09F, 0.22F, 0.0F, -89F), 0.0F);

        if (f2 < 0.27F)
        { // Open Front Gear Doors fully while Gear Strut passes through
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.0F, 0.11F, 0.0F, -90F), 0.0F);
            hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f2, 0.0F, 0.11F, 0.0F, -90F), 0.0F);
        }
        else
        { // Slightly close Front Gear Doors again when gear is fully extracted
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.89F, 0.99F, -90F, -55F), 0.0F);
            hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f2, 0.89F, 0.99F, -90F, -55F), 0.0F);
        }

        // Helper values for chunkSetLocate
        float[] xyz = { 0.0F, 0.0F, 0.0F };
        float[] ypr = { 0.0F, 0.0F, 0.0F };

        // Left Gear Fuselage Cover avoidance
        if (f < 0.33F) // When near Fuselage cover, retract strut slightly to avoid cover
            xyz[0] = Aircraft.cvt(f, 0.23F, 0.33F, 0.0F, 0.2F);
        else // after passing cover, extract strut again
            xyz[0] = Aircraft.cvt(f, 0.34F, 0.65F, 0.2F, 0.0F);
        ypr[0] = Aircraft.cvt(f, 0.23F, 0.65F, 0.0F, 43F); // rotate left main gear strut to align with x-axis properly
        ypr[1] = Aircraft.cvt(f, 0.23F, 0.65F, 0.0F, -90F); // extract/retract left main gear
        hiermesh.chunkSetLocate("GearL2_D0", xyz, ypr); // Left Main Gear moves here!
        // Left Main Gear outer Wing cover, move and rotate to align with x-axis properly
        hiermesh.chunkSetAngles("GearL4_D0", Aircraft.cvt(f, 0.23F, 0.65F, 0.0F, -43F), Aircraft.cvt(f, 0.23F, 0.65F, 0.0F, -90F), 0.0F);

        // Right Gear Fuselage Cover avoidance
        if (f1 < 0.38F) // When near Fuselage cover, retract strut slightly to avoid cover
            xyz[0] = Aircraft.cvt(f1, 0.28F, 0.38F, 0.0F, 0.2F);
        else // after passing cover, extract strut again
            xyz[0] = Aircraft.cvt(f1, 0.39F, 0.70F, 0.2F, 0.0F);
        ypr[0] = Aircraft.cvt(f1, 0.28F, 0.7F, 0.0F, -43F); // rotate right main gear strut to align with x-axis properly
        ypr[1] = Aircraft.cvt(f1, 0.28F, 0.7F, 0.0F, -90F); // extract/retract right main gear
        hiermesh.chunkSetLocate("GearR2_D0", xyz, ypr); // Right Main Gear moves here!
        // Right Main Gear outer Wing cover, move and rotate to align with x-axis properly
        hiermesh.chunkSetAngles("GearR4_D0", Aircraft.cvt(f1, 0.28F, 0.7F, 0.0F, 43F), Aircraft.cvt(f1, 0.28F, 0.7F, 0.0F, -90F), 0.0F);

        // Remaining Gear Parts movement
          //  hiermesh.chunkSetAngles("GearC10_D0", 0.0F, Aircraft.cvt(f2, 0.69F, 0.74F, 0.0F, -90F), 0.0F);   // missing in F-105
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.63F, 0.99F, 0.0F, -105F), 0.0F);
          //   hiermesh.chunkSetAngles("Gear5e_D0", 0.0F, Aircraft.cvt(f2, 0.63F, 0.99F, 0.0F, -90F), 0.0F);  // missing in F-105
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    // ************************************************************************************************
    // Gear code for backward compatibility, older base game versions don'f1 indepently move their gears
    public static void moveGear(HierMesh hiermesh, float f)
    {
        moveGear(hiermesh, f, f, f); // re-route old style function calls to new code
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    // ************************************************************************************************


    public void moveWheelSink()
    {
        float f = Aircraft.cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.32F, 0.0F, 1.0F);
        resetYPRmodifier();
        Aircraft.xyz[1] = 0.10F - 0.18F * f;
        hierMesh().chunkSetLocate("GearC7d_D0", Aircraft.xyz, Aircraft.ypr);

        resetYPRmodifier();
        f = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.11F, 0.0F, 1.0F);
        Aircraft.xyz[0] = - 0.08F + 0.07F * f;
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        f = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.11F, 0.0F, 1.0F);
        Aircraft.xyz[0] = - 0.08F + 0.07F * f;
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 30F * f, 0.0F);
    }

    public void moveSteering(float f)
    {
        if(FM.CT.GearControl > 0.5F && FM.Gears.onGround())
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, 1.0F * f, 0.0F);
        if(FM.CT.GearControl < 0.5F)
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = -45F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        f1 = Aircraft.cvt(f, 0.22F, 0.29F, 0.0F, 20F);
        hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 0.0F, -f1);
        hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 0.0F, -f1);
    }

    protected void moveFan(float f)
    {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        int i = part(s);
        if (s.startsWith("xx"))
        {
            if (s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if (s.endsWith("p1"))
                {
                    getEnergyPastArmor(13.350000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    if (shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
                else if (s.endsWith("p2"))
                    getEnergyPastArmor(8.770001F, shot);
                else if (s.endsWith("g1"))
                {
                    getEnergyPastArmor(World.Rnd().nextFloat(40F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            }
            else if (s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                switch (j)
                {
                    case 1: // '\001'
                    case 2: // '\002'
                        if (World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, shot) > 0.0F)
                        {
                            debuggunnery("Controls: Ailerones Controls: Out..");
                            FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3: // '\003'
                    case 4: // '\004'
                        if (getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                        {
                            debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                            FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        if (getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                        {
                            debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;
                }
            }
            else if (s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if (s.endsWith("bloc"))
                    getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if (s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if (s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    FM.AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                s.endsWith("exht");
            }
            else if (s.startsWith("xxmgun0"))
            {
                int k = s.charAt(7) - 49;
                if (getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: mnine Gun (" + k + ") Disabled..");
                    FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            }
            else if (s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if (getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if (FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        FM.AS.hitTank(shot.initiator, l, 2);
                        debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
            }
            else if (s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            }
            else if (s.startsWith("xxhyd"))
                FM.AS.setInternalDamage(shot.initiator, 3);
            else if (s.startsWith("xxpnm"))
                FM.AS.setInternalDamage(shot.initiator, 1);
        }
        else
        {
            if (s.startsWith("xcockpit"))
            {
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if (s.startsWith("xcf"))
                hitChunk("CF", shot);
            else if (s.startsWith("xnose"))
                hitChunk("Nose", shot);
            else if (s.startsWith("xtail"))
            {
                if (chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            }
            else if (s.startsWith("xkeel"))
            {
                if (chunkDamageVisible("Keel1") < 2)
                    hitChunk("Keel1", shot);
            }
            else if (s.startsWith("xrudder"))
                hitChunk("Rudder1", shot);
            else if (s.startsWith("xstab"))
            {
                if (s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", shot);
                if (s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                    hitChunk("StabR", shot);
            }
            else if (s.startsWith("xvator"))
            {
                if (s.startsWith("xvatorl"))
                    hitChunk("VatorL", shot);
                if (s.startsWith("xvatorr"))
                    hitChunk("VatorR", shot);
            }
            else if (s.startsWith("xwing"))
            {
                if (s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                    hitChunk("WingLIn", shot);
                if (s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                    hitChunk("WingRIn", shot);
                if (s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
                if (s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                    hitChunk("WingRMid", shot);
                if (s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                    hitChunk("WingLOut", shot);
                if (s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                    hitChunk("WingROut", shot);
            }
            else if (s.startsWith("xarone"))
            {
                if (s.startsWith("xaronel"))
                    hitChunk("AroneL", shot);
                if (s.startsWith("xaroner"))
                    hitChunk("AroneR", shot);
            }
            else if (s.startsWith("xgear"))
            {
                if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if (s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    FM.AS.setInternalDamage(shot.initiator, 3);
                }
            }
            else if (s.startsWith("xpilot") || s.startsWith("xhead"))
            {
                byte byte0 = 0;
                int i1;
                if (s.endsWith("a"))
                {
                    byte0 = 1;
                    i1 = s.charAt(6) - 49;
                }
                else if (s.endsWith("b"))
                {
                    byte0 = 2;
                    i1 = s.charAt(6) - 49;
                }
                else
                {
                    i1 = s.charAt(5) - 49;
                }
                hitFlesh(i1, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch (i)
        {
            case 13: // '\r'
                FM.Gears.cgear = false;
                float f = World.Rnd().nextFloat(0.0F, 1.0F);
                if (f < 0.1F)
                {
                    FM.AS.hitEngine(this, 0, 100);
                    if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.48999999999999999D)
                        FM.EI.engines[0].setEngineDies(actor);
                }
                else if (f > 0.55000000000000004D)
                    FM.EI.engines[0].setEngineDies(actor);
                return super.cutFM(i, j, actor);

            case 19: // '\023'
                FM.EI.engines[0].setEngineDies(actor);
                FM.CT.bHasArrestorControl = false;
                return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public float getAirPressure(float f)
    {
        float f1 = 1.0F - (0.0065F * f) / 288.15F;
        float f2 = 5.255781F;
        return 101325F * (float) Math.pow(f1, f2);
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
        for (i = 0; i < TypeSupersonic.fMachAltX.length - 1; i++)
            if (TypeSupersonic.fMachAltX[i] > f)
                break;

        if (i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        }
        else
        {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + f2 * f5;
        }
    }

    public float calculateMach()
    {
        return FM.getSpeedKMH() / getMachForAlt(FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(FM.getAltitude()) - FM.getSpeedKMH();
        if (f < 0.5F)
            f = 0.5F;
        float f1 = FM.getSpeedKMH() - getMachForAlt(FM.getAltitude());
        if (f1 < 0.5F)
            f1 = 0.5F;
        if (calculateMach() <= 1.0D)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if (calculateMach() >= 1.0D)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f1;
            isSonic = true;
        }
        if (FM.VmaxAllowed > 1500F)
            FM.VmaxAllowed = 1500F;
        if (isSonic && SonicBoom < 1.0F)
        {
            super.playSound("aircraft.SonicBoom", true);
            super.playSound("aircraft.SonicBoomInternal", true);
            if (FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            if (Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if (calculateMach() > 1.01D || calculateMach() < 1.0D)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if (FM.AS.isMaster())
            if (curthrl == -1F)
            {
                curthrl = oldthrl = FM.EI.engines[0].getControlThrottle();
            }
            else
            {
                curthrl = FM.EI.engines[0].getControlThrottle();
                if (curthrl < 1.05F)
                {
                    if ((curthrl - oldthrl) / f > 20F && FM.EI.engines[0].getRPM() < 3200F && FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                    {
                        if (FM.actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.01D * (FM.EI.engines[0].getRPM() / 1000F);
                        FM.EI.engines[0].doSetReadyness(FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if (World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel) FM).isRealMode())
                            FM.AS.hitEngine(this, 0, 100);
                        if (World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel) FM).isRealMode())
                            FM.EI.engines[0].setEngineDies(this);
                    }
                    if ((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && FM.EI.engines[0].getRPM() < 3200F && FM.EI.engines[0].getStage() == 6)
                    {
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.001D * (FM.EI.engines[0].getRPM() / 1000F);
                        FM.EI.engines[0].doSetReadyness(FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if (World.Rnd().nextFloat() < 0.4F && (FM instanceof RealFlightModel) && ((RealFlightModel) FM).isRealMode())
                        {
                            if (FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                            FM.EI.engines[0].setEngineStops(this);
                        }
                        else if (FM.actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                    }
                }
                oldthrl = curthrl;
            }
    }

    public void update(float f)
    {
        if (bNeedSetup)
            checkAsDrone();
        if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete)
        {
            overrideBailout = true;
            FM.AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
        }
        if (FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            if (FM.EI.engines[0].getPowerOutput() > 0.45F && FM.EI.engines[0].getStage() == 6)
            {
                if (FM.EI.engines[0].getPowerOutput() > 0.65F)
                    FM.AS.setSootState(this, 0, 3);
                else
                    FM.AS.setSootState(this, 0, 2);
            }
            else
            {
                FM.AS.setSootState(this, 0, 0);
            }
            setExhaustFlame((int) Aircraft.cvt(FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F), 0);
            if (FM instanceof RealFlightModel)
            {
                umn();
            }
        }
        if(FM.getSpeedKMH() > 600F && FM.CT.bHasFlapsControl)
        {
            FM.CT.FlapsControl = 0.0F;
            FM.CT.bHasFlapsControl = false;
        }
        else
        {
            FM.CT.bHasFlapsControl = true;
        }
        engineSurge(f);
        checkHydraulicStatus();
        soundbarier();
        Flaps();
        Flaps2();
        Flaps3();
        computeLift();
        computeEngine();
        computeEngineAB();
        computeSupersonicLimiter();
        setSubsonicLimiter();
        guidedMissileUtils.update();
        if (FM instanceof Maneuver)
            receivingRefuel(f);
        if (FM.CT.getArrestor() > 0.2F)
            calculateArrestor();

        super.update(f);
        if(bTwoSeat && obsMove < obsMoveTot && !bObserverKilled && !FM.AS.isPilotParatrooper(1))
            observerLookAroundUpdate(f);
        computeDragChute();
        moveAirBrake(FM.CT.getAirBrake());
      }

    public void doSetSootState(int i, int j)
    {
        for (int k = 0; k < 2; k++)
        {
            if (FM.AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(FM.AS.astateSootEffects[i][k]);
            FM.AS.astateSootEffects[i][k] = null;
        }

        switch (j)
        {
            case 1: // '\001'
                FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
                FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
                break;

            case 3: // '\003'
                FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
                // fall through

            case 2: // '\002'
                FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 0.75F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
                break;

            case 5: // '\005'
                FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.5F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
                // fall through

            case 4: // '\004'
                FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
                break;
        }
    }

    protected void moveAirBrake(float f)
    {
        if(FM.CT.getGear() > 0.1F)
            FM.Sq.dragAirbrakeCx = fAirbrakeCx / 2F;
        else
            FM.Sq.dragAirbrakeCx = fAirbrakeCx;

        boolean bNozzleOpen = false;
        float f1 = f;
        float fNozzleOpenBase = Aircraft.cvt(FM.EI.engines[0].getControlThrottle(), 0.90F, 1.10F, 0.0F, 0.115F);
        if(FM.EI.engines[0].getControlThrottle() > 0.90F)
        {
            bNozzleOpen = true;
            f1 = Math.max(f, fNozzleOpenBase);
        }

        if(hasHydraulicPressure && (Time.current() > lTimeLastHydraulicPressureRecover + 500L) && FM.CT.getGear() > 0.1F)
        {
            hierMesh().chunkSetAngles("Brake01_D0", 0.0F, (bNozzleOpen ? 75F : 0F) * fNozzleOpenBase, 0.0F);      // under
            hierMesh().chunkSetAngles("Brake02_D0", 0.0F, (bNozzleOpen ? 75F : 0F) * fNozzleOpenBase, 0.0F);      // up
            hierMesh().chunkSetAngles("Brake03_D0",  -75F * f1, 0.0F, 0.0F);   // side
            hierMesh().chunkSetAngles("Brake04_D0",  75F * f1, 0.0F, 0.0F);    // side
        }
        else
        {
            hierMesh().chunkSetAngles("Brake01_D0", 0.0F, (hasHydraulicPressure ? 75F : 460F) * f1, 0.0F);     // under
            hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 75F * f1, 0.0F);     // up
            hierMesh().chunkSetAngles("Brake03_D0",  -75F * f1, 0.0F, 0.0F);   // side
            hierMesh().chunkSetAngles("Brake04_D0",  75F * f1, 0.0F, 0.0F);    // side
        }
 }

    public void moveRefuel(float f)
    {
        float f0 = Aircraft.cvt(f, 0.27F, 0.73F, 0.0F, 1.0F);
        hierMesh().chunkSetAngles("FuelProbe", 29F * f0, 0.0F, 29F * f0);
        float f1 = 0.0F;
        if(f < 0.5F)  f1 = Aircraft.cvt(f, 0.0F, 0.22F, 0.0F, 1.0F);
        else          f1 = Aircraft.cvt(f, 0.78F, 1.0F, 1.0F, 0.0F);
        hierMesh().chunkSetAngles("FuelProbe2", 0.0F, 115.0F * f1, 15.0F * f1);
    }

    public void setExhaustFlame(int i, int j)
    {
        if (j == 0)
            switch (i)
            {
                case 0: // '\0'
                    hierMesh().chunkVisible("Exhaust1", false);
                    hierMesh().chunkVisible("Exhaust2", false);
                    hierMesh().chunkVisible("Exhaust3", false);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 1: // '\001'
                    hierMesh().chunkVisible("Exhaust1", true);
                    hierMesh().chunkVisible("Exhaust2", false);
                    hierMesh().chunkVisible("Exhaust3", false);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 2: // '\002'
                    hierMesh().chunkVisible("Exhaust1", false);
                    hierMesh().chunkVisible("Exhaust2", true);
                    hierMesh().chunkVisible("Exhaust3", false);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 3: // '\003'
                    hierMesh().chunkVisible("Exhaust1", true);
                    hierMesh().chunkVisible("Exhaust2", true);
                    hierMesh().chunkVisible("Exhaust3", false);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", false);
                    // fall through

                case 4: // '\004'
                    hierMesh().chunkVisible("Exhaust1", false);
                    hierMesh().chunkVisible("Exhaust2", false);
                    hierMesh().chunkVisible("Exhaust3", true);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 5: // '\005'
                    hierMesh().chunkVisible("Exhaust1", true);
                    hierMesh().chunkVisible("Exhaust2", false);
                    hierMesh().chunkVisible("Exhaust3", true);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 6: // '\006'
                    hierMesh().chunkVisible("Exhaust1", false);
                    hierMesh().chunkVisible("Exhaust2", true);
                    hierMesh().chunkVisible("Exhaust3", true);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 7: // '\007'
                    hierMesh().chunkVisible("Exhaust1", true);
                    hierMesh().chunkVisible("Exhaust2", false);
                    hierMesh().chunkVisible("Exhaust3", false);
                    hierMesh().chunkVisible("Exhaust4", true);
                    hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 8: // '\b'
                    hierMesh().chunkVisible("Exhaust1", false);
                    hierMesh().chunkVisible("Exhaust2", true);
                    hierMesh().chunkVisible("Exhaust3", false);
                    hierMesh().chunkVisible("Exhaust4", true);
                    hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 9: // '\t'
                    hierMesh().chunkVisible("Exhaust1", false);
                    hierMesh().chunkVisible("Exhaust2", false);
                    hierMesh().chunkVisible("Exhaust3", true);
                    hierMesh().chunkVisible("Exhaust4", true);
                    hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 10: // '\n'
                    hierMesh().chunkVisible("Exhaust1", true);
                    hierMesh().chunkVisible("Exhaust2", false);
                    hierMesh().chunkVisible("Exhaust3", false);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", true);
                    break;

                case 11: // '\013'
                    hierMesh().chunkVisible("Exhaust1", false);
                    hierMesh().chunkVisible("Exhaust2", true);
                    hierMesh().chunkVisible("Exhaust3", false);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", true);
                    break;

                case 12: // '\f'
                    hierMesh().chunkVisible("Exhaust1", false);
                    hierMesh().chunkVisible("Exhaust2", false);
                    hierMesh().chunkVisible("Exhaust3", true);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", true);
                    break;

                default:
                    hierMesh().chunkVisible("Exhaust1", false);
                    hierMesh().chunkVisible("Exhaust2", false);
                    hierMesh().chunkVisible("Exhaust3", false);
                    hierMesh().chunkVisible("Exhaust4", false);
                    hierMesh().chunkVisible("Exhaust5", false);
                    break;
            }
    }

    private void bailout()
    {
        if(overrideBailout)
            if(FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2)
            {
                if(FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.5F && FM.getSpeedKMH() < 15F)
                {
                    FM.AS.astateBailoutStep = 11;
                }
                else
                {
                    FM.AS.astateBailoutStep = 2;
                }
            }
            else if(FM.AS.astateBailoutStep >= 2 && FM.AS.astateBailoutStep <= 3)
            {
                switch(FM.AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(FM.CT.cockpitDoorControl < 0.5F || FM.CT.getCockpitDoor() < 0.5F || FM.getSpeedKMH() > 15F)
                        doRemoveBlisters();
                    break;

                case 3: // '\003'
                    lTimeNextEject = Time.current() + 1000L;
                    break;
                }
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                FM.AS.astateBailoutStep = (byte)(FM.AS.astateBailoutStep + 1);
                if(FM.AS.astateBailoutStep == 4)
                    FM.AS.astateBailoutStep = 11;
            }
            else if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
            {
                byte byte0 = FM.AS.astateBailoutStep;
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                FM.AS.astateBailoutStep = (byte)(FM.AS.astateBailoutStep + 1);
                if((FM instanceof Maneuver) && ((Maneuver)FM).get_maneuver() != 44)
                {
                    World.cur();
                    if(FM.AS.actor != World.getPlayerAircraft())
                        ((Maneuver)FM).set_maneuver(44);
                }
                doRemoveBodyFromPlane(byte0 - 10);
                if(FM.getSpeedKMH() > 15F)
                {
                    if(byte0 == 11 || (byte0 == 12 && bTwoSeat))
                        doEjectCatapult(byte0 - 10);
                    if(byte0 == 11)
                        lTimeNextEject = Time.current() + 1000L;
                    if (!bTwoSeat || byte0 > 11)
                    {
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    EventLog.onBailedOut(this, byte0 - 11);
                    FM.AS.astatePilotStates[byte0 - 11] = 99;
                    return;
                }
                else
                {
                    if(FM.AS.astatePilotStates[byte0 - 11] < 99)
                    {
                        FM.AS.astatePilotStates[byte0 - 11] = 100;
                        if(FM.AS.isMaster())
                        {
                            try
                            {
                              Hook localHook = findHook("_ExternalBail0" + (byte0 - 10));

                              if (localHook != null)
                              {
                                  Loc localLoc = new Loc(0.0D, 0.0D, 0.0D, World.Rnd().nextFloat(-45.0F, 45.0F), 0.0F, 0.0F);

                                  localHook.computePos(this, pos.getAbs(), localLoc);

                                  new Paratrooper(this, getArmy(), byte0 - 11, localLoc, FM.Vwld);

                                  if ((byte0 > 10) && (byte0 <= 19))
                                  {
                                      EventLog.onBailedOut(this, byte0 - 11);
                                  }
                              }
                            }
                            catch (Exception localException)
                            {
                            }
                            finally
                            {
                            }
                            if ((FM.AS.astatePilotStates[byte0 - 11] == 19) && (this == World.getPlayerAircraft()) && (!World.isPlayerGunner()) && (FM.brakeShoe))
                            {
                                  MsgDestroy.Post(Time.current() + 1000L, this);
                            }
                        }
                        FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.CT.bHasAileronControl = false;
                        FM.CT.bHasRudderControl = false;
                        FM.CT.bHasElevatorControl = false;
                        if(!bTwoSeat || byte0 > 11)
                        {
                            FM.AS.astateBailoutStep = -1;
                            overrideBailout = false;
                            FM.AS.bIsAboutToBailout = true;
                            ejectComplete = true;
                            if ((this == World.getPlayerAircraft()) && (!World.isPlayerGunner()) && (FM.brakeShoe))
                            {
                                  MsgDestroy.Post(Time.current() + 1000L, this);
                            }
                        }
                        return;
                    }
                }
                EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + FM.AS.astatePilotStates[byte0 - 11]);
            }
    }

    private final void doRemoveBlister1()
    {
    }

    private final void doRemoveBlisters()
    {
        for(int i = 1; i < 5; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind( bTwoSeat ? ("Blister" + i + "a_D0") : ("Blister" + i + "_D0") ));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(FM.Vwld);
                wreckage.setSpeed(vector3d);
            }
    }

    public void doEjectCatapult(final int i)
    {
        new MsgAction(false, this)
        {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D - (double) i * 7D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat0" + i);
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(6, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat" + i + "_D0", false);
        FM.setTakenMortalDamage(true, null);
        FM.CT.WeaponControl[0] = false;
        FM.CT.WeaponControl[1] = false;
        FM.CT.bHasAileronControl = false;
        FM.CT.bHasRudderControl = false;
        FM.CT.bHasElevatorControl = false;
    }

    private final void umn()
    {
        Vector3d vector3d = FM.getVflow();
        mn = (float) vector3d.lengthSquared();
        mn = (float) Math.sqrt(mn);
        F_105 f_105 = this;
        float f = mn;
        World.cur().getClass();
        f_105.mn = f / Atmosphere.sonicSpeed((float) FM.Loc.z);
        if (mn >= lteb)
            ts = true;
        else
            ts = false;
    }

    public boolean ist()
    {
        return ts;
    }

    public float gmnr()
    {
        return mn;
    }

    public boolean inr()
    {
        return ictl;
    }


    public void computeLift()
       {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        if(calculateMach() < 0.38F)
        polares.lineCyCoeff= 0.08F;
        float x = calculateMach();
        if(calculateMach() >= 0.38F);
        float Lift = 0.08F;
        if((double)x > 2.1F)
        {
            Lift = 0.12F;
        }
        else
        {
            float x2 = x * x;
            float x3 = x2 * x;
            float x4 = x3 * x;
            float x5 = x4 * x;
            float x6 = x5 * x;
            float x7 = x6 * x;
            Lift= 0.126188F*x6 - 1.00592F*x5 + 3.04283F*x4 - 4.28911F*x3 + 2.73472F*x2 - 0.644456F*x + 0.109496F;
           // {{0.38, 0.08}, {0.55, 0.1},{0.97, 0.078}, {1.3, 0.035},{1.68, 0.022}, {2.0, 0.0185}, {2.1, 0.012}}
            }
        polares.lineCyCoeff= Lift;
    }


    public void computeEngine()
    {
        float x = FM.getAltitude() / 1000F;
        float ThrustDegradation = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() > 5)
            if(calculateMach() < 0.0F);
        if((double)x > 14.0D)
        {
            ThrustDegradation = 6F;
        }
        else
        {
            float x2 = x * x;
            float x3 = x2 * x;
            float x4 = x3 * x;
            ThrustDegradation = 0.0030506F*x3 - 0.0446429F*x2 + 0.741369F*x;
           // {{0,0},{5,1.5},{12,2.5},{14,3.5}}
        }
        FM.producedAF.x -= ThrustDegradation * 1000F;
    }


    public void computeEngineAB()
    {
        if(FM.EI.engines[0].getThrustOutput() > 0.99F && FM.EI.engines[0].getStage() > 5 && calculateMach() <= 0.3 )
           FM.producedAF.x += 12000D;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5)
           FM.producedAF.x += 38110D;
        float x = FM.getAltitude() / 1000F;
        float thrustDegradation  = 0.0F;
        if (FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            if (x > 13.8D)
            {
                thrustDegradation = 23F;
            }
            else
            {
                float x2 = x * x;
                float x3 = x2 * x;
                float x4 = x3 * x;
                float x5 = x4 * x;
                float x6 = x5 * x;
                float x7 = x6 * x;
                float x8 = x7 * x;
                float x9 = x8 * x;
                thrustDegradation = 0.0000151443F*x8 - 0.000849596F*x7 +0.0187838F*x6 - 0.207318F*x5 + 1.20049F*x4 - 3.57196F*x3 + 5.16067F*x2 - 2.59983F*x;
                //{{0,0},{1,1},{4,6},{10.5,-5},{12.5,4},{15.5,20}}
            }
        FM.producedAF.x -= thrustDegradation  * 1000F;
    }

    private void setSubsonicLimiter()
    {
        if (FM.getAltitude() > 0.0F && calculateMach() >= 0.96999999999999997D && FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() > 5)
            FM.Sq.dragParasiteCx += 0.003F;
    }

    public void computeSupersonicLimiter()
    {
        float x = FM.getAltitude() / 1000F;
        float Drag = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6 && (double)calculateMach() >= 1.092)
            if (x > 2)
            {
                Drag = 0.0F;
            }
            else
            {
                float x2 = x * x;
                Drag = 0.00032F - 0.00016F*x;
                //{{0,0.00032},{2, 0.0}}
            }
         FM.Sq.dragParasiteCx += Drag;
    }

    private boolean Flaps ()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && (double)calculateMach() >= 0.13D && (double)calculateMach() < 0.18D && !Flaps)
        {
            polares.Cy0_1 += 2.0F;
            Flaps = true;
        }
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && (double)calculateMach() < 0.13D && (double)calculateMach() > 0.18D && Flaps)
        {
            polares.Cy0_1 -=2.0F;
            Flaps = false;
        }
        return Flaps;
    }

    private boolean Flaps2()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && (double)calculateMach() >= 0.05D && (double)calculateMach() < 0.21D && !Flaps2)
        {
            polares.CyCritH_1 += 1.3F;
            Flaps2 = true;
        }
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && (double)calculateMach() < 0.05D && (double)calculateMach() > 0.21D && Flaps2)
        {
            polares.CyCritH_1 -=1.3F;
            Flaps2 = false;
        }
        return Flaps2;
    }

    private boolean Flaps3()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && (double)calculateMach() < 0.28D && !Flaps3)
        {
            polares.CxMin_1 -= 0.04;
            Flaps3 = true;
        }
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && (double)calculateMach() > 0.28D && Flaps3)
        {
            polares.CxMin_1 +=0.04;
            Flaps3 = false;
        }
        return Flaps3;
    }


    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if (i == 20)
            if (!APmode1)
            {
                APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude ON");
                FM.AP.setStabAltitude(1000F);
            }
            else if (APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude OFF");
                FM.AP.setStabAltitude(false);
            }
        if (i == 21)
            if (!APmode2)
            {
                APmode2 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction ON");
                FM.AP.setStabDirection(true);
                FM.CT.bHasRudderControl = false;
            }
            else if (APmode2)
            {
                APmode2 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction OFF");
                FM.AP.setStabDirection(false);
                FM.CT.bHasRudderControl = true;
            }
        if (i == 22)
            if (!APmode3)
            {
                APmode3 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route ON");
                FM.AP.setWayPoint(true);
            }
            else if (APmode3)
            {
                APmode3 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route OFF");
                FM.AP.setWayPoint(false);
                FM.CT.AileronControl = 0.0F;
                FM.CT.ElevatorControl = 0.0F;
                FM.CT.RudderControl = 0.0F;
            }
        if (i == 23)
        {
            FM.CT.AileronControl = 0.0F;
            FM.CT.ElevatorControl = 0.0F;
            FM.CT.RudderControl = 0.0F;
            FM.AP.setWayPoint(false);
            FM.AP.setStabDirection(false);
            FM.AP.setStabAltitude(false);
            APmode1 = false;
            APmode2 = false;
            APmode3 = false;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: All Off");
        }
    }

    protected void checkAsDrone()
    {
        if (target_ == null)
        {
            if (FM.AP.way.curr().getTarget() == null)
                FM.AP.way.next();
            target_ = FM.AP.way.curr().getTarget();
            if (Actor.isValid(target_) && (target_ instanceof Wing))
            {
                Wing wing = (Wing) target_;
                int i = aircIndex();
                if (Actor.isValid(wing.airc[i / 2]))
                    target_ = wing.airc[i / 2];
                else
                    target_ = null;
            }
        }
        if (Actor.isValid(target_) && (target_ instanceof TypeTankerDrogue))
        {
            queen_last = target_;
            queen_time = Time.current();
            if (isNetMaster())
                ((TypeDockable) target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
        }
        bNeedSetup = false;
        target_ = null;
    }

    public int typeDockableGetDockport()
    {
        if (typeDockableIsDocked())
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
        if (FM.AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if (aircraft instanceof TypeTankerDrogue && FM.CT.getRefuel() > 0.95F)
                ((TypeDockable) aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach()
    {
        if (FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
            ((TypeDockable) queen_).typeDockableRequestDetach(this);
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
        FlightModel flightmodel = ((SndAircraft) ((Aircraft) queen_)).FM;
        if (aircIndex() == 0 && (super.FM instanceof Maneuver) && (flightmodel instanceof Maneuver))
        {
            Maneuver maneuver = (Maneuver) flightmodel;
            Maneuver maneuver1 = (Maneuver) super.FM;
            if (maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1)
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
        if (dockport_ == i)
        {
            queen_last = queen_;
            queen_time = Time.current();
            queen_ = null;
            dockport_ = 0;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException
    {
        if (typeDockableIsDocked())
        {
            netmsgguaranted.writeByte(1);
            ActorNet actornet = null;
            if (Actor.isValid(queen_))
            {
                actornet = queen_.net;
                if (actornet.countNoMirrors() > 0)
                    actornet = null;
            }
            netmsgguaranted.writeByte(dockport_);
            netmsgguaranted.writeNetObj(actornet);
        }
        else
        {
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException
    {
        if (netmsginput.readByte() == 1)
        {
            dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null)
            {
                Actor actor = (Actor) netobj.superObj();
                ((TypeDockable) actor).typeDockableDoAttachToDrone(this, dockport_);
            }
        }
    }

    public long getChaffDeployed()
    {
        if (hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if (hasFlare)
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
        if (fSightCurForwardAngle > 85F)
            fSightCurForwardAngle = 85F;
        fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
        if (bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle--;
        if (fSightCurForwardAngle < 0.0F)
            fSightCurForwardAngle = 0.0F;
        fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
        if (bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        fSightCurSideslip += 0.1F;
        if (fSightCurSideslip > 3F)
            fSightCurSideslip = 3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 10F)) });
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip -= 0.1F;
        if (fSightCurSideslip < -3F)
            fSightCurSideslip = -3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 10F)) });
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 850F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 10F;
        if (fSightCurAltitude > 6000F)
            fSightCurAltitude = 6000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
        fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 10F;
        if (fSightCurAltitude < 850F)
            fSightCurAltitude = 850F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
        fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 250F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 10F;
        if (fSightCurSpeed > 900F)
            fSightCurSpeed = 900F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 10F;
        if (fSightCurSpeed < 150F)
            fSightCurSpeed = 150F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f)
    {
        if (Math.abs(FM.Or.getKren()) > 4.5D)
        {
            fSightCurReadyness -= 0.0666666F * f;
            if (fSightCurReadyness < 0.0F)
                fSightCurReadyness = 0.0F;
        }
        if (fSightCurReadyness < 1.0F)
            fSightCurReadyness += 0.0333333F * f;
        else if (bSightAutomation)
        {
            fSightCurDistance -= (fSightCurSpeed / 3.6F) * f;
            if (fSightCurDistance < 0.0F)
            {
                fSightCurDistance = 0.0F;
                typeBomberToggleAutomation();
            }
            fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(fSightCurDistance / fSightCurAltitude));
            if (fSightCurDistance < fSightCurSpeed / 3.6F * Math.sqrt(fSightCurAltitude * 0.2038736F))
                bSightBombDump = true;
            if (bSightBombDump)
                if (super.FM.isTick(3, 0))
                {
                    if (FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                }
                else
                {
                    FM.CT.WeaponControl[3] = false;
                }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException
    {
        netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(fSightCurDistance);
        netmsgguaranted.writeByte((int) fSightCurForwardAngle);
        netmsgguaranted.writeByte((int) ((fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeFloat(fSightCurSpeed);
        netmsgguaranted.writeByte((int) (fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException
    {
        int i = netmsginput.readUnsignedByte();
        bSightAutomation = (i & 1) != 0;
        bSightBombDump = (i & 2) != 0;
        fSightCurDistance = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readUnsignedByte();
        fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = netmsginput.readFloat();
        fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    public void setCommonThreatActive()
    {
        long l = Time.current();
        if (l - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = l;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long l = Time.current();
        if (l - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = l;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long l = Time.current();
        if (l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
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

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        super.msgCollisionRequest(actor, aflag);
        if (queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
            aflag[0] = false;
        else
            aflag[0] = true;
    }

    public void missionStarting()
    {
		super.missionStarting();
        checkAsDrone();
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 45F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 45F * f, 0.0F);
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook_D0", 0.0F, 0.0F, 65F * f);
        resetYPRmodifier();
        Aircraft.xyz[2] = 0.1385F * f;
        arrestor = f;
    }

    private void receivingRefuel(float f)
    {
        int i = aircIndex();
        if(typeDockableIsDocked())
        {
            if(FM.CT.getRefuel() < 0.9F)
            {
                typeDockableAttemptDetach();
                return;
            }
            else
            {
                if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
                {
                    ((Maneuver)super.FM).unblock();
                    ((Maneuver)super.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((Maneuver)super.FM).push(48);

                    if(FM.AP.way.curr().Action != 3)
                        ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                    ((Pilot)super.FM).setDumbTime(3000L);
                }
                FuelTank fuelTanks[];
                fuelTanks = FM.CT.getFuelTanks();
                if(FM.M.fuel < FM.M.maxFuel - 11F)
                {
                    float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 10.093F, f);
                    FM.M.fuel += getFuel;
                }
                else if(fuelTanks.length > 0 && fuelTanks[0] != null && !FM.M.bFuelTanksDropped)
                {
                    float freeTankSum = 0F;
                    for(int num = 0; num < fuelTanks.length; num++)
                        freeTankSum += fuelTanks[num].checkFreeTankSpace();
                    if(freeTankSum < 11F)
                    {
                        typeDockableAttemptDetach();
                        return;
                    }
                    float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 10.093F, f);
                    for(int num = 0; num < fuelTanks.length; num++)
                        fuelTanks[num].doRefuel(getFuel * (fuelTanks[num].checkFreeTankSpace() / freeTankSum));
                }
                else
                {
                    typeDockableAttemptDetach();
                    return;
                }
            }
        }
        else if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
        {
            if(FM.CT.GearControl == 0.0F && FM.EI.engines[0].getStage() == 0)
                FM.EI.setEngineRunning();
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
            }
            else if(FM.AP.way.curr().Action == 0)
            {
                Maneuver maneuver = (Maneuver)super.FM;
                if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                    maneuver.Group.setGroupTask(2);
            }
        }
    }

    private void aiAirbrakeRareAction()
    {
        if (FM.AP.way.isLanding() && FM.getSpeed() > FM.VmaxFLAPS && FM.getSpeed() > FM.AP.way.curr().getV() * 1.4F)
        {
            if (FM.CT.AirBrakeControl != 1.0F)
                FM.CT.AirBrakeControl = 1.0F;
        }
        else if (((Maneuver) FM).get_maneuver() == 25 && FM.AP.way.isLanding() && FM.getSpeed() < FM.VmaxFLAPS * 1.16F)
        {
            if (FM.getSpeed() > FM.VminFLAPS * 0.5F && FM.Gears.onGround())
            {
                if (FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
                if (openChuteTimer == 0L)
                    openChuteTimer = Time.current() + 800L;
                if (openChuteTimer > 0L && openChuteTimer < Time.current() && !bHasDeployedDragChute && FM.CT.DragChuteControl == 0.0F)
                    FM.CT.DragChuteControl = 1.0F;
            }
            else if (FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        }
        else if (((Maneuver) FM).get_maneuver() == 66)
        {
            if (FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        }
        else if (((Maneuver) FM).get_maneuver() == 7)
        {
            if (FM.CT.AirBrakeControl != 1.0F)
                FM.CT.AirBrakeControl = 1.0F;
        }
        else if (hasHydraulicPressure && FM.CT.AirBrakeControl != 0.0F)
            FM.CT.AirBrakeControl = 0.0F;
    }

    private void computeDragChute()
    {
        if(FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF-105/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.8F);
            ((Actor) (chute)).pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        }
        else if(bHasDeployedDragChute && FM.CT.bHasDragChuteControl)
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() > 600F || FM.CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            }
            else if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !FM.CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
    }

    private void observerLookAroundRA()
    {
        if(obsLookTime == 0)
        {
            obsLookTime = 2 + World.Rnd().nextInt(1, 3);
            obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
            obsMove = 0.0F;
            obsAzimuthOld = obsAzimuth;
            obsElevationOld = obsElevation;
            if((double)World.Rnd().nextFloat() > 0.80000000000000004D)
            {
                obsAzimuth = 0.0F;
                obsElevation = 0.0F;
            }
            else
            {
                obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
                obsElevation = World.Rnd().nextFloat() * 50F - 20F;
            }
        }
        else
        {
            obsLookTime--;
        }
    }

    private void calculateArrestor()
    {
        if (FM.Gears.arrestorVAngle != 0.0F)
        {
            float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            arrestor = 0.8F * arrestor + 0.2F * f1;
            moveArrestorHook(arrestor);
        }
        else
        {
            float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
            if (f2 < 0.0F && super.FM.getSpeedKMH() > 60F)
                Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if (f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
                f2 = 0.0F;
            if (f2 > 0.2F)
                f2 = 0.2F;
            if (f2 > 0.0F)
                arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
            else
                arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
            if (arrestor < 0.0F)
                arrestor = 0.0F;
            else if (arrestor > 1.0F)
                arrestor = 1.0F;
            moveArrestorHook(arrestor);
        }
    }

    private void observerLookAroundUpdate(float f)
    {
        if(obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
            obsMove += 0.29999999999999999D * (double)f;
        else
        if(obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
            obsMove += 0.15F;
        else
            obsMove += 1.2D * (double)f;
        obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
        obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
        hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
    }

    protected boolean            bSlatsOff;
    private final _cls0          sbcls1                 = new _cls0(0.0F, 13000F, 0.65F, 1.0F, 0.01F, 1.0F, 0.2F, 1.0F, 0.5F, 0.6F, 0.0F);
    private float                oldthrl;
    private float                curthrl;
    public int                   k14Mode;
    public int                   k14WingspanType;
    public float                 k14Distance;
    public float                 AirBrakeControl;
    private boolean              overrideBailout;
    private boolean              ejectComplete;
    private float                lightTime;
    private float                ft;
    private LightPointWorld      lLight[];
    private Hook                 lLightHook[];
    private Loc           lLightLoc1             = new Loc();
    private Point3d       lLightP1               = new Point3d();
    private Point3d       lLightP2               = new Point3d();
    private Point3d       lLightPL               = new Point3d();
    private boolean              ictl;
    private static float         mteb                   = 1.0F;
    private float                mn;
    private static float         uteb                   = 1.25F;
    private static float         lteb                   = 0.92F;
    private float                actl;
    private float                rctl;
    private float                ectl;
    private boolean              ts;
    public boolean               bChangedPit            = false;
    private float                SonicBoom;
    private Eff3DActor           shockwave;
    private boolean              isSonic;
    public int                   LockState              = 0;
    Actor                        hunted                 = null;
    private float                engineSurgeDamage;
    public boolean               hasHydraulicPressure;
    private static final float   NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float   NEG_G_TIME_FACTOR      = 1.5F;
    private static final float   NEG_G_RECOVERY_FACTOR  = 1F;
    private static final float   POS_G_TOLERANCE_FACTOR = 2F;
    private static final float   POS_G_TIME_FACTOR      = 2F;
    private static final float   POS_G_RECOVERY_FACTOR  = 2F;
    public boolean               APmode1;
    public boolean               APmode2;
    public boolean               APmode3;
    private Actor                target_;
    protected Actor              queen_;
    private int                  dockport_;
    protected Actor              queen_last;
    protected long               queen_time;
    protected boolean            bNeedSetup;
    private long                 dtime;
    protected GuidedMissileUtils guidedMissileUtils;
    private boolean              hasChaff;
    private boolean              hasFlare;
    private long                 lastChaffDeployed;
    private long                 lastFlareDeployed;
    private long                 lastCommonThreatActive;
    private long                 intervalCommonThreat;
    private long                 lastRadarLockThreatActive;
    private long                 intervalRadarLockThreat;
    private long                 lastMissileLaunchThreatActive;
    private long                 intervalMissileLaunchThreat;
    public boolean               bToFire;
    protected float              arrestor;
    private float                deltaAzimuth;
    private float                deltaTangage;
    private boolean              bSightAutomation;
    private boolean              bSightBombDump;
    private float                fSightCurDistance;
    public float                 fSightCurForwardAngle;
    public float                 fSightCurSideslip;
    public float                 fSightCurAltitude;
    public float                 fSightCurSpeed;
    public float                 fSightCurReadyness;
    private boolean Flaps;
    private boolean Flaps2;
    private boolean Flaps3;
    private long lTimeNextEject;
    public float DragChuteControl;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long openChuteTimer = 0L;
    private long removeChuteTimer;
//    private Orient o = new Orient();
    private boolean bTwoSeat;
    private float fAirbrakeCx;
    private long lTimeLastHydraulicPressureRecover;
    private int obsLookTime;
    private float obsLookAzimuth;
    private float obsLookElevation;
    private float obsAzimuth;
    private float obsElevation;
    private float obsAzimuthOld;
    private float obsElevationOld;
    private float obsMove;
    private float obsMoveTot;
    boolean bObserverKilled;

    static {
        Class class1 = com.maddox.il2.objects.air.F_105.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }

}
