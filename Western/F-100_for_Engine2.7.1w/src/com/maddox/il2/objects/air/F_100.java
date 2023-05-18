
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.F_100D;
import com.maddox.il2.objects.air.F_100F;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import java.io.IOException;


public class F_100 extends Scheme1
    implements TypeSupersonic, TypeFastJet, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeRadarGunsight, TypeStormovik, TypeGSuit, TypeZBReceiver, TypeFuelDump, TypeDockable
{
    private class _cls0
    {

        public void rs(int i)
        {
            if(i == 0 || i == 1)
                actl *= 0.68000000000000005D;
            if(i == 31 || i == 32)
                ectl *= 0.68000000000000005D;
            if(i == 15 || i == 16)
                rctl *= 0.68000000000000005D;
        }

        private void sbcls1()
        {
            if(ts)
            {
                float f = Aircraft.cvt(FM.getAltitude(), lal, tal, bef, tef);
                float f1 = Aircraft.cvt(mn, mn < F_100.mteb ? F_100.lteb : F_100.uteb, mn < F_100.mteb ? F_100.uteb : F_100.lteb, mn < F_100.mteb ? bhef : thef, mn < F_100.mteb ? thef : phef);
                float f2 = Aircraft.cvt(mn, mn < F_100.mteb ? F_100.lteb : F_100.uteb, mn < F_100.mteb ? F_100.uteb : F_100.lteb, mn < F_100.mteb ? mef : wef / f, mn < F_100.mteb ? wef / f : lef / f);
                ((RealFlightModel)FM).producedShakeLevel += 0.1125F * f1;
                FM.SensPitch = ectl * f2 * f2;
                FM.SensRoll = actl * f2;
                FM.SensYaw = rctl * f2;
                if(f1 > 0.6F)
                    ictl = true;
                else
                    ictl = false;
                if(ftl > 0.0F)
                {
                    if(World.Rnd().nextFloat() > 0.6F)
                        if(FM.CT.RudderControl > 0.0F)
                            FM.CT.RudderControl -= ftl * f1;
                        else
                        if(FM.CT.RudderControl < 0.0F)
                        {
                            FM.CT.RudderControl += ftl * f1;
                        } else
                        {
                            Controls controls = FM.CT;
                            controls.RudderControl = controls.RudderControl + (World.Rnd().nextFloat() > 0.5F ? ftl * f1 : -ftl * f1);
                        }
                    if(FM.CT.RudderControl > 1.0F)
                        FM.CT.RudderControl = 1.0F;
                    if(FM.CT.RudderControl < -1F)
                        FM.CT.RudderControl = -1F;
                }
            } else
            {
                FM.SensPitch = ectl;
                FM.SensRoll = actl;
                FM.SensYaw = rctl;
            }
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


        private _cls0(float f, float f1, float f2, float f3, float f4, float f5, 
                float f6, float f7, float f8, float f9, float f10)
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


    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
    }

    public F_100()
    {
        SonicBoom = 0.0F;
        bSlatsOff = false;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        AirBrakeControl = 0.0F;
        lightTime = 0.0F;
        ft = 0.0F;
        mn = 0.0F;
        ts = false;
        ictl = false;
        engineSurgeDamage = 0.0F;
        gearTargetAngle = -1F;
        gearCurrentAngle = -1F;
        hasHydraulicPressure = true;
        APmode1 = false;
        APmode2 = false;
        Flaps = false;
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
        gfactors.setGFactors(1.5F, 1.5F, 1.0F, 2.0F, 2.0F, 2.0F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        FM.AS.wantBeaconsNet(true);
        FM.CT.bHasBombSelect = true;
        actl = FM.SensRoll;
        ectl = FM.SensPitch;
        rctl = FM.SensYaw;
        if(this instanceof com.maddox.il2.objects.air.F_100F)
            bTwoSeat = true;
    }

    public void checkHydraulicStatus()
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
            gearTargetAngle = 90F;
            hasHydraulicPressure = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
        } else
        if(!hasHydraulicPressure)
        {
            gearTargetAngle = 0.0F;
            hasHydraulicPressure = true;
            FM.CT.bHasAileronControl = true;
            FM.CT.bHasElevatorControl = true;
            FM.CT.bHasAirBrakeControl = true;
        }
    }

    public void moveHydraulics(float f)
    {
        if(gearTargetAngle >= 0.0F)
            if(gearCurrentAngle < gearTargetAngle)
            {
                gearCurrentAngle += 90F * f * 0.8F;
                if(gearCurrentAngle >= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            } else
            {
                gearCurrentAngle -= 90F * f * 0.8F;
                if(gearCurrentAngle <= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            }
    }

    public void updateLLights()
    {
        pos.getRender(Actor._tmpLoc);
        if(lLight == null)
        {
            if(Actor._tmpLoc.getX() >= 1.0D)
            {
                lLight = (new LightPointWorld[] {
                    null, null, null, null
                });
                for(int i = 0; i < 4; i++)
                {
                    lLight[i] = new LightPointWorld();
                    lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    lLight[i].setEmit(0.0F, 0.0F);
                    try
                    {
                        lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    }
                    catch(Exception exception) { }
                }

            }
        } else
        {
            for(int j = 0; j < 4; j++)
                if(FM.AS.astateLandingLightEffects[j] != null)
                {
                    lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP1);
                    lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP2);
                    Engine.land();
                    if(Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL))
                    {
                        lLightPL.z++;
                        lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
                        lLight[j].setPos(lLightP2);
                        float f = (float)lLightP1.distance(lLightPL);
                        float f1 = f * 0.5F + 60F;
                        float f2 = 0.7F - (0.8F * f * lightTime) / 2000F;
                        lLight[j].setEmit(f2, f1);
                    } else
                    {
                        lLight[j].setEmit(0.0F, 0.0F);
                    }
                } else
                if(lLight[j].getR() != 0.0F)
                    lLight[j].setEmit(0.0F, 0.0F);

        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
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
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = FM.getVflow();
            mn = (float)vector3d.lengthSquared();
            mn = (float)Math.sqrt(mn);
            F_100 f_100 = this;
            float f1 = mn;
            if(World.cur().Atm != null);
            f_100.mn = f1 / Atmosphere.sonicSpeed((float)FM.Loc.z);
            if(mn >= 0.9F && (double)mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
        }
        if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver))
            aiAirbrakeRareAction();
        ft = World.getTimeofDay() % 0.01F;
        if(ft == 0.0F)
            UpdateLightIntensity();
        if(bTwoSeat && !bObserverKilled)
            observerLookAroundRA();
    }

    private final void UpdateLightIntensity()
    {
        if(World.getTimeofDay() >= 6F && World.getTimeofDay() < 7F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 6F, 7F, 1.0F, 0.1F);
        else
        if(World.getTimeofDay() >= 18F && World.getTimeofDay() < 19F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 18F, 19F, 0.1F, 1.0F);
        else
        if(World.getTimeofDay() >= 7F && World.getTimeofDay() < 18F)
            lightTime = 0.1F;
        else
            lightTime = 1.0F;
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Caged");
        } else
        if(k14Mode == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Uncaged");
        } else
        if(k14Mode == 2 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Off");
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        if(k14WingspanType == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        if(k14WingspanType == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    public void doMurderPilot(int i)
    {
        switch(i)
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

    protected void moveSlats(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 0.125F);
        Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.125F);
        Aircraft.xyz[2] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.125F);
        Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.125F);
        hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void slatsOff()
    {
        if(!bSlatsOff)
        {
            resetYPRmodifier();
            Aircraft.xyz[0] = 0.125F;
            Aircraft.xyz[1] = -0.125F;
            Aircraft.xyz[2] = 0.0F;
            hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 8.5F, 0.0F);
            hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
            Aircraft.xyz[0] = -0.125F;
            Aircraft.xyz[1] = -0.125F;
            hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 8.5F, 0.0F);
            hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
            bSlatsOff = true;
        }
    }

    public void moveCockpitDoor(float f)
    {
        float f1 = (bTwoSeat ? 30F : 40F) * f;
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 0.0F, f1);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveAirBrake(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -40F * f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        // By western, once deleting GearFX function to avoid "static" conflicting.
        // recovering it ... is a future plan.
        if(Math.abs(f) < 0.27F)
        {
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.15F, 0.26F, 0.0F, -90F), 0.0F);
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.09F, 0.22F, 0.0F, -90F), 0.0F);
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.11F, 0.0F, -90F), 0.0F);
        } else
        {
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.65F, 0.74F, -90F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.67F, 0.78F, -90F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.89F, 0.99F, -90F, 0.0F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.23F, 0.65F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.23F, 0.65F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.28F, 0.7F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.28F, 0.7F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.63F, 0.99F, 0.0F, -105F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.63F, 0.99F, 0.0F, -95F), 0.0F);

        // By western, original GearFX codes are here.
/*        if(Math.abs(f) < 0.27F)
        {
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f * kl, 0.15F, 0.26F, 0.0F, -90F), 0.0F);
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f * kr, 0.09F, 0.22F, 0.0F, -90F), 0.0F);
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f * kc, 0.0F, 0.11F, 0.0F, -90F), 0.0F);
        } else
        {
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f * kl, 0.65F, 0.74F, -90F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f * kl, 0.67F, 0.78F, -90F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f * kl, 0.89F, 0.99F, -90F, 0.0F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f * kl, 0.23F, 0.65F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f * kl, 0.23F, 0.65F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f * kr, 0.28F, 0.7F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f * kr, 0.28F, 0.7F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f * kc, 0.63F, 0.99F, 0.0F, -105F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f * kc, 0.63F, 0.99F, 0.0F, -95F), 0.0F);
*/
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
        // By western, if possible, want to move gear animation codes into here. But today it doesn't work as planned.
/*        if(Math.abs(f) < 0.27F)
        {
            hierMesh().chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f * kl, 0.15F, 0.26F, 0.0F, -90F), 0.0F);
            hierMesh().chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f * kr, 0.09F, 0.22F, 0.0F, -90F), 0.0F);
            hierMesh().chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f * kc, 0.0F, 0.11F, 0.0F, -90F), 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f * kl, 0.65F, 0.74F, -90F, 0.0F), 0.0F);
            hierMesh().chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f * kl, 0.67F, 0.78F, -90F, 0.0F), 0.0F);
            hierMesh().chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f * kl, 0.89F, 0.99F, -90F, 0.0F), 0.0F);
        }
        hierMesh().chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f * kl, 0.23F, 0.65F, 0.0F, -85F), 0.0F);
        hierMesh().chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f * kl, 0.23F, 0.65F, 0.0F, -85F), 0.0F);
        hierMesh().chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f * kr, 0.28F, 0.7F, 0.0F, -85F), 0.0F);
        hierMesh().chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f * kr, 0.28F, 0.7F, 0.0F, -85F), 0.0F);
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f * kc, 0.63F, 0.99F, 0.0F, -105F), 0.0F);
        hierMesh().chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f * kc, 0.63F, 0.99F, 0.0F, -95F), 0.0F);
*/
        if(FM.CT.getGear() >= 0.9985F)
        {
            kl = 1.0F;
            kr = 1.0F;
            kc = 1.0F;
        }
    }

    private void gearDamageFX(String s)
    {
        if(s.startsWith("xgearl") || s.startsWith("GearL"))
        {
            if(super.FM.isPlayers())
                HUD.log("Left Gear:  Hydraulic system Failed");
            kl = World.Rnd().nextFloat();
            kr = World.Rnd().nextFloat() * kl;
            kc = 0.1F;
            cutGearCovers("L");
        } else
        if(s.startsWith("xgearr") || s.startsWith("GearR"))
        {
            if(super.FM.isPlayers())
                HUD.log("Right Gear:  Hydraulic system Failed");
            kr = World.Rnd().nextFloat();
            kl = World.Rnd().nextFloat() * kr;
            kc = 0.1F;
            cutGearCovers("R");
        } else
        {
            if(super.FM.isPlayers())
                HUD.log("Center Gear:  Hydraulic system Failed");
            kc = World.Rnd().nextFloat();
            kl = World.Rnd().nextFloat() * kc;
            kr = World.Rnd().nextFloat() * kc;
            cutGearCovers("C");
        }
        FM.CT.GearControl = 1.0F;
        FM.Gears.setHydroOperable(false);
    }

    private void cutGearCovers(String s)
    {
        Vector3d vector3d = new Vector3d();
        if(World.Rnd().nextFloat() < 0.3F)
        {
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Gear" + s + 5 + "_D0"));
            wreckage.collide(true);
            vector3d.set(FM.Vwld);
            wreckage.setSpeed(vector3d);
            hierMesh().chunkVisible("Gear" + s + 5 + "_D0", false);
            Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("Gear" + s + 6 + "_D0"));
            wreckage1.collide(true);
            vector3d.set(FM.Vwld);
            wreckage1.setSpeed(vector3d);
            hierMesh().chunkVisible("Gear" + s + 6 + "_D0", false);
        } else
        if(World.Rnd().nextFloat() < 0.3F)
        {
            int i = World.Rnd().nextInt(2) + 5;
            Wreckage wreckage2 = new Wreckage(this, hierMesh().chunkFind("Gear" + s + i + "_D0"));
            wreckage2.collide(true);
            vector3d.set(FM.Vwld);
            wreckage2.setSpeed(vector3d);
            hierMesh().chunkVisible("Gear" + s + i + "_D0", false);
        }
    }

    public void moveWheelSink()
    {
        if(curctl == -1F)
        {
            curctl = oldctl = FM.CT.getBrake();
            H1 = 0.17F;
            FM.Gears.tailStiffness = 0.4F;
        } else
        {
            curctl = FM.CT.getBrake();
        }
        if(!FM.brakeShoe && FM.Gears.cgear)
        {
            if(curctl - oldctl < -0.02F)
                curctl = oldctl - 0.02F;
            if(curctl < 0.0F)
                curctl = 0.0F;
            float f = 0.25F * curctl * Math.max(Aircraft.cvt(FM.EI.engines[0].getThrustOutput(), 0.5F, 0.8F, 0.0F, 1.0F), Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 80F, 0.0F, 1.0F));
            FM.setGC_Gear_Shift(H1 - f);
            resetYPRmodifier();
            Aircraft.xyz[0] = -0.4F * f;
            hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
        }
        oldctl = curctl;
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        if(FM.CT.GearControl > 0.5F)
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -50F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -12F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -12F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = -45F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveFan(float f)
    {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        int i = part(s);
        sbcls1.rs(i);
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(13.350000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(8.770001F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            } else
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                switch(j)
                {
                case 1: // '\001'
                case 2: // '\002'
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            } else
            if(s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    FM.AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                if(!s.endsWith("exht"));
            } else
            if(s.startsWith("xxmgun0"))
            {
                int k = s.charAt(7) - 49;
                if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: mnine Gun (" + k + ") Disabled..");
                    FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        FM.AS.hitTank(shot.initiator, l, 2);
                        debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
            } else
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            } else
            if(s.startsWith("xxhyd"))
                FM.AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                FM.AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xcf"))
                hitChunk("CF", shot);
            else
            if(s.startsWith("xnose"))
                hitChunk("Nose", shot);
            else
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
                hitChunk("Rudder1", shot);
            else
            if(s.startsWith("xstab"))
            {
                if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", shot);
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                    hitChunk("StabR", shot);
            } else
            if(s.startsWith("xvator"))
            {
                if(s.startsWith("xvatorl"))
                    hitChunk("VatorL", shot);
                if(s.startsWith("xvatorr"))
                    hitChunk("VatorR", shot);
            } else
            if(s.startsWith("xwing"))
            {
                if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                    hitChunk("WingLIn", shot);
                if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                    hitChunk("WingRIn", shot);
                if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
                if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                    hitChunk("WingRMid", shot);
                if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                    hitChunk("WingLOut", shot);
                if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                    hitChunk("WingROut", shot);
            } else
            if(s.startsWith("xarone"))
            {
                if(s.startsWith("xaronel"))
                    hitChunk("AroneL", shot);
                if(s.startsWith("xaroner"))
                    hitChunk("AroneR", shot);
            } else
            if(s.startsWith("xgear"))
            {
                if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 0);
                    hasHydraulicPressure = false;
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    FM.AS.setInternalDamage(shot.initiator, 3);
                    FM.Gears.setHydroOperable(false);
                    gearDamageFX(s);
                }
            } else
            if(s.startsWith("xpilot") || s.startsWith("xhead"))
            {
                byte byte0 = 0;
                int i1;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    i1 = s.charAt(6) - 49;
                } else
                if(s.endsWith("b"))
                {
                    byte0 = 2;
                    i1 = s.charAt(6) - 49;
                } else
                {
                    i1 = s.charAt(5) - 49;
                }
                hitFlesh(i1, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 13: // '\r'
            FM.Gears.cgear = false;
            float f = World.Rnd().nextFloat(0.0F, 1.0F);
            if(f < 0.1F)
            {
                FM.AS.hitEngine(this, 0, 100);
                if((double)World.Rnd().nextFloat(0.0F, 1.0F) < 0.48999999999999999D)
                    FM.EI.engines[0].setEngineDies(actor);
            } else
            if((double)f > 0.55000000000000004D)
                FM.EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);

        case 19: // '\023'
            FM.EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 2)
            return;
        if(!Config.isUSE_RENDER())
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 200F;
            hunted = War.GetNearestEnemyAircraft(FM.actor, 2700F, 9);
        }
        if(hunted != null)
        {
            k14Distance = (float)FM.actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 800F)
                k14Distance = 800F;
            else
            if(k14Distance < 200F)
                k14Distance = 200F;
        }
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
        for(i = 0; i < TypeSupersonic.fMachAltX.length - 1; i++)
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

    public float calculateMach()
    {
        return FM.getSpeedKMH() / getMachForAlt(FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(FM.getAltitude()) - FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f1 = FM.getSpeedKMH() - getMachForAlt(FM.getAltitude());
        if(f1 < 0.5F)
            f1 = 0.5F;
        if((double)calculateMach() <= 1.0D)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if((double)calculateMach() >= 1.0D)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f1;
            isSonic = true;
        }
        if(FM.VmaxAllowed > 1500F)
            FM.VmaxAllowed = 1500F;
        if(isSonic && SonicBoom < 1.0F)
        {
            super.playSound("aircraft.SonicBoom", true);
            super.playSound("aircraft.SonicBoomInternal", true);
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if((double)calculateMach() > 1.01D || (double)calculateMach() < 1.0D)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(FM.AS.isMaster())
            if(curthrl == -1F)
            {
                curthrl = oldthrl = FM.EI.engines[0].getControlThrottle();
            } else
            {
                curthrl = FM.EI.engines[0].getControlThrottle();
                if(curthrl < 1.05F)
                {
                    if((curthrl - oldthrl) / f > 20F && FM.EI.engines[0].getRPM() < 3200F && FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                    {
                        if(FM.actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.01D * (double)(FM.EI.engines[0].getRPM() / 1000F);
                        FM.EI.engines[0].doSetReadyness(FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                            FM.AS.hitEngine(this, 0, 100);
                        if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                            FM.EI.engines[0].setEngineDies(this);
                    }
                    if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && FM.EI.engines[0].getRPM() < 3200F && FM.EI.engines[0].getStage() == 6)
                    {
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.001D * (double)(FM.EI.engines[0].getRPM() / 1000F);
                        FM.EI.engines[0].doSetReadyness(FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.4F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                        {
                            if(FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                            FM.EI.engines[0].setEngineStops(this);
                        } else
                        if(FM.actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                    }
                }
                oldthrl = curthrl;
            }
    }

    public void update(float f)
    {
        if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete)
        {
            overrideBailout = true;
            FM.AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
        }
        if(bNeedSetup)
            checkAsDrone();
        computeLimiters();
        computeCy();
        Flaps();
        computeDragChute();
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        checkHydraulicStatus();
        moveHydraulics(f);
        soundbarier();
        if(super.FM instanceof Maneuver)
            receivingRefuel(f);
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
            if(FM.EI.engines[0].getThrustOutput() > 0.45F && FM.EI.engines[0].getStage() == 6)
            {
                if(FM.EI.engines[0].getThrustOutput() > 1.001F)
                    FM.AS.setSootState(this, 0, 5);
                else
                if(FM.EI.engines[0].getThrustOutput() > 0.65F && FM.EI.engines[0].getThrustOutput() <= 1.001F)
                    FM.AS.setSootState(this, 0, 3);
                else
                    FM.AS.setSootState(this, 0, 2);
            } else
            {
                FM.AS.setSootState(this, 0, 0);
            }
        if(super.FM.getSpeed() > 5F)
        {
            moveSlats(f);
            bSlatsOff = false;
        } else
        {
            slatsOff();
        }
        setExhaustFlame((int)Aircraft.cvt(FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F), 0);
        if(FM instanceof RealFlightModel)
        {
            umn();
            sbcls1.sbcls1();
        }
        computeThrust();
        super.update(f);
        if(bTwoSeat && obsMove < obsMoveTot && !bObserverKilled && !FM.AS.isPilotParatrooper(1))
            observerLookAroundUpdate(f);
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
            if(FM.AP.way.curr().getTarget() == null)
                FM.AP.way.next();
            target_ = FM.AP.way.curr().getTarget();
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
        if(FM.AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
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

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(FM.AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(FM.AS.astateSootEffects[i][k]);
            FM.AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1: // '\001'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            // fall through

        case 2: // '\002'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
            // fall through

        case 4: // '\004'
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
        }
    }

    public void setExhaustFlame(int i, int j)
    {
        if(j == 0)
            switch(i)
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
                } else
                {
                    FM.AS.astateBailoutStep = 2;
                }
            } else
            if(FM.AS.astateBailoutStep >= 2 && FM.AS.astateBailoutStep <= 3)
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
            } else
            if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
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
                    if (!bTwoSeat || byte0 > 11) {
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
                            } catch (Exception localException) {
                            } finally {
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

    public void doEjectCatapult(final int i)
    {
        new MsgAction(false, this) {

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

    private final void doRemoveBlister1()
    {
    }

    private final void doRemoveBlisters()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && FM.AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void umn()
    {
        Vector3d vector3d = FM.getVflow();
        mn = (float)vector3d.lengthSquared();
        mn = (float)Math.sqrt(mn);
        F_100 f_100 = this;
        float f = mn;
        if(World.cur().Atm != null);
        f_100.mn = f / Atmosphere.sonicSpeed((float)FM.Loc.z);
        if(mn >= lteb)
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

    public void computeLimiters()
    {
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.96999999999999997D && FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() > 5)
            FM.Sq.dragParasiteCx += 0.003F;
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 1.3100000000000001D && FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5)
            FM.Sq.dragParasiteCx += 0.003F;
    }

    public void computeCy()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float f = calculateMach();
        if(calculateMach() < 0.0F);
        float f1 = 0.0F;
        if((double)f > 1.25D)
        {
            f1 = 0.5F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            f1 = -(((1050F * f3 - 2395F * f2) + 2359F * f) - 2535F) / 1950F;
        }
        polares.CyCritH_0 = f1;
    }

    private boolean Flaps()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && !Flaps)
        {
            polares.Cy0_1 += 0.45000000000000001D;
            Flaps = true;
        }
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && Flaps)
        {
            polares.Cy0_1 -= 0.45000000000000001D;
            Flaps = false;
        }
        return Flaps;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                FM.AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                FM.AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                FM.AP.setStabDirection(true);
                FM.CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                FM.AP.setStabDirection(false);
                FM.CT.bHasRudderControl = true;
            }
    }

    private void computeThrust()
    {
        if(FM.EI.engines[0].getStage() < 6)
            return;
        double d = FM.EI.engines[0].getThrustOutput();
        if(d >= 1.0010000467300415D)
            FM.producedAF.x += d * 24000D;
        if(d < 1.0010000467300415D && FM.CT.GearControl < 0.1F)
            FM.producedAF.x += d * 5000D;
        if(FM.getAltitude() > 9000F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 1250D;
        if(FM.getAltitude() > 10000F && d < 1.0010000467300415D)
            FM.producedAF.x -= d * 600D;
        if(FM.getAltitude() > 10000F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 3000D;
        if(FM.getAltitude() > 10000F && (double)calculateMach() >= 1.3500000000000001D)
            FM.producedAF.x -= d * 7000D;
        if(FM.getAltitude() > 11000F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 2300D;
        if(FM.getAltitude() > 11000F && (double)calculateMach() >= 1.3D)
            FM.producedAF.x -= d * 7000D;
        if(FM.getAltitude() > 11500F && (double)calculateMach() >= 1.22D)
            FM.producedAF.x -= d * 7000D;
        if(FM.getAltitude() > 12000F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 1600D;
        if(FM.getAltitude() > 12000F && (double)calculateMach() >= 1.1499999999999999D)
            FM.producedAF.x -= d * 7000D;
        if(FM.getAltitude() > 12500F && (double)calculateMach() >= 1.1000000000000001D)
            FM.producedAF.x -= d * 7000D;
        if(FM.getAltitude() > 13000F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 1500D;
        if(FM.getAltitude() > 13500F && (double)calculateMach() >= 1.05D)
            FM.producedAF.x -= d * 7000D;
        if(FM.getAltitude() > 14000F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 1200D;
        if(FM.getAltitude() > 15000F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 1300D;
        if(FM.getAltitude() > 15500F && (double)calculateMach() >= 0.96999999999999997D)
            FM.producedAF.x -= d * 7000D;
        if(FM.getAltitude() > 16000F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 1350D;
        if(FM.getAltitude() > 17000F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 7500D;
        if(FM.getAltitude() > 18200F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 8300D;
        if(FM.getAltitude() > 20000F && d > 1.0010000467300415D)
            FM.producedAF.x -= d * 11000D;
    }

    private void receivingRefuel(float f)
    {
        int i = aircIndex();
        if(typeDockableIsDocked())
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
            if(FM.M.fuel < FM.M.maxFuel - 10F)
            {
                float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 9.893F, f);
                FM.M.fuel += getFuel;
            }
            else if(fuelTanks.length > 0 && fuelTanks[0] != null && !FM.M.bFuelTanksDropped)
            {
                float freeTankSum = 0F;
                for(int num = 0; num < fuelTanks.length; num++)
                    freeTankSum += fuelTanks[num].checkFreeTankSpace();
                if(freeTankSum < 10F)
                {
                    typeDockableAttemptDetach();
                    return;
                }
                float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 9.893F, f);
                for(int num = 0; num < fuelTanks.length; num++)
                    fuelTanks[num].doRefuel(getFuel * (fuelTanks[num].checkFreeTankSpace() / freeTankSum));
            }
            else
            {
                typeDockableAttemptDetach();
                return;
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
                ((Pilot)super.FM).setDumbTime(3000L);
                if(Time.current() > dtime + 3000L)
                {
                    dtime = -1L;
                    ((Maneuver)super.FM).clear_stack();
                    ((Maneuver)super.FM).set_maneuver(0);
                    ((Pilot)super.FM).setDumbTime(0L);
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

    private void computeDragChute()
    {
        if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
        {
            if(FM.AP.way.isLanding() && FM.Gears.onGround() && super.FM.getSpeed() > 40F)
            {
                FM.CT.AirBrakeControl = 1.0F;
                if(FM.CT.bHasDragChuteControl)
                    FM.CT.DragChuteControl = 1.0F;
            }
            if(FM.AP.way.isLanding() && FM.Gears.onGround() && super.FM.getSpeed() < 40F)
            {
                FM.CT.AirBrakeControl = 0.0F;
                if(super.FM.getSpeed() < 20F)
                    FM.CT.DragChuteControl = 0.0F;
            }
        }
        if(FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF100/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(1.0F);
            chute.pos.setRel(new Point3d(-5D, 0.0D, 0.59999999999999998D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && FM.CT.bHasDragChuteControl)
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() > 600F || FM.CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    chute.pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                chute.pos.setRel(new Orient(0.0F, 100F, 0.0F));
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !FM.CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
    }

    private void aiAirbrakeRareAction()
    {
        if(FM.AP.way.isLanding() && FM.getSpeed() > FM.VmaxFLAPS && FM.getSpeed() > FM.AP.way.curr().getV() * 1.4F)
        {
            if(FM.CT.AirBrakeControl != 1.0F)
                FM.CT.AirBrakeControl = 1.0F;
        } else
        if(((Maneuver)FM).get_maneuver() == 25 && FM.AP.way.isLanding() && FM.getSpeed() < FM.VmaxFLAPS * 1.16F)
        {
            if(FM.getSpeed() > FM.VminFLAPS * 0.5F && (FM.Gears.nearGround() || FM.Gears.onGround()))
            {
                if(FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            } else
            if(FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        } else
        if(((Maneuver)FM).get_maneuver() == 66)
        {
            if(FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        } else
        if(((Maneuver)FM).get_maneuver() == 7)
        {
            if(FM.CT.AirBrakeControl != 1.0F)
                FM.CT.AirBrakeControl = 1.0F;
        } else
        if(hasHydraulicPressure && FM.CT.AirBrakeControl != 0.0F)
            FM.CT.AirBrakeControl = 0.0F;
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
            } else
            {
                obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
                obsElevation = World.Rnd().nextFloat() * 50F - 20F;
            }
        } else
        {
            obsLookTime--;
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

    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    public boolean bToFire;
    protected boolean bSlatsOff;
    private final _cls0 sbcls1 = new _cls0(0.0F, 13000F, 0.65F, 1.0F, 0.01F, 1.0F, 0.2F, 1.0F, 0.5F, 0.6F, 0.0F);
    private float oldctl;
    private float curctl;
    private float oldthrl;
    private float curthrl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    public float AirBrakeControl;
    private float lightTime;
    private float ft;
    private LightPointWorld lLight[];
    private Hook lLightHook[] = {
        null, null, null, null
    };
    private Loc lLightLoc1 = new Loc();
    private Point3d lLightP1 = new Point3d();
    private Point3d lLightP2 = new Point3d();
    private Point3d lLightPL = new Point3d();
    private boolean ictl;
    private static float mteb = 1.0F;
    private float mn;
    private static float uteb = 1.25F;
    private static float lteb = 0.92F;
    private float actl;
    private float rctl;
    private float ectl;
    private boolean ts;
    private float H1;
    public boolean bChangedPit = false;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    public int LockState = 0;
    Actor hunted = null;
    private float engineSurgeDamage;
    protected float gearTargetAngle;
    private float gearCurrentAngle;
    public boolean hasHydraulicPressure;
    private float kl = 1.0F;
    private float kr = 1.0F;
    private float kc = 1.0F;
    public static float FlowRate = 8.5F;
    public static float FuelReserve = 1000F;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    public boolean APmode1;
    public boolean APmode2;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    private boolean Flaps;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private long lTimeNextEject;
    private boolean bTwoSeat;
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

    static 
    {
        Class class1 = com.maddox.il2.objects.air.F_100.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }

}
