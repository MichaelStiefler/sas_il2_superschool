
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.FuelTankGun_TankF5A150galH2_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankF5E150galH2_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankF5E275gal_gn16;
import com.maddox.il2.objects.weapons.Pylon_USMERmd_gn16;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import java.io.IOException;
import java.io.PrintStream;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme2, TypeSupersonic, TypeFighter, TypeBNZFighter, 
//            TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeRadar, 
//            TypeX4Carrier, Aircraft, Cockpit, PaintScheme, 
//            EjectionSeat

public class F_5 extends Scheme2
    implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeRadar, TypeX4Carrier
{

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

    public F_5()
    {
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 150F;
        fSightCurReadyness = 0.0F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        lLightHook = new Hook[4];
        SonicBoom = 0.0F;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        AirBrakeControl = 0.0F;
        DragChuteControl = 0.0F;
        overrideBailout = false;
        ejectComplete = false;
        lightTime = 0.0F;
        ft = 0.0F;
        mn = 0.0F;
        ts = false;
        ictl = false;
        engineSurgeDamage = 0.0F;
        gearTargetAngle = -1F;
        gearCurrentAngle = -1F;
        hasHydraulicPressure = true;
        bCanopyInitState = false;
        radarmode = 0;
        targetnum = 0;
        lockrange = 0.018F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        APmode1 = false;
        APmode2 = false;
        pylonOccupied = false;
        pylonOccupied_DT = false;
        pylonOccupied_Py = false;
        Flaps = false;
        bSlatsOff = false;
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(2.5F, 2.5F, 2.0F, 5F, 3F, 3F);
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
            if(!FM.AS.bIsAboutToBailout)
            {
                if(hierMesh().isChunkVisible("Blister1_D0"))
                    hierMesh().chunkVisible("Gore1_D0", true);
                hierMesh().chunkVisible("Gore2_D0", true);
            }
            break;
        }
    }

    public void onAircraftLoaded()
    {
        FM.CT.bHasBombSelect = true;
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).AS.wantBeaconsNet(true);
        actl = ((FlightModelMain) (super.FM)).SensRoll;
        ectl = ((FlightModelMain) (super.FM)).SensPitch;
        rctl = ((FlightModelMain) (super.FM)).SensYaw;
        ((FlightModelMain) (super.FM)).CT.DiffBrakesType = 3;
        System.out.println("*** Diff Brakes Set to Type: " + ((FlightModelMain) (super.FM)).CT.DiffBrakesType);
        ((FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = true;
        ((FlightModelMain) (super.FM)).CT.dvCockpitDoor = 1.0F;
        if(thisWeaponsName.startsWith("Def"))
        {
            hierMesh().chunkVisible("PylonL1", false);
            hierMesh().chunkVisible("PylonL2", false);
            hierMesh().chunkVisible("PylonCenter", false);
            hierMesh().chunkVisible("PylonR1", false);
            hierMesh().chunkVisible("PylonR2", false);
        }
        if(thisWeaponsName.startsWith("Fighter:"))
        {
            hierMesh().chunkVisible("PylonL1", false);
            hierMesh().chunkVisible("PylonL2", false);
            hierMesh().chunkVisible("PylonCenter", false);
            hierMesh().chunkVisible("PylonR1", false);
            hierMesh().chunkVisible("PylonR2", false);
        }
        if(thisWeaponsName.startsWith("Fighter01:"))
        {
            hierMesh().chunkVisible("PylonL1", false);
            hierMesh().chunkVisible("PylonL2", false);
            hierMesh().chunkVisible("PylonR1", false);
            hierMesh().chunkVisible("PylonR2", false);
            Polares polares = (Polares)Reflection.getValue(FM, "Wing");
            polares.CxMin_0 = 0.0326F;
        }
        if(thisWeaponsName.startsWith("Fighter02:"))
        {
            hierMesh().chunkVisible("PylonL1", false);
            hierMesh().chunkVisible("PylonR1", false);
            hierMesh().chunkVisible("PylonCenter", false);
            Polares polares1 = (Polares)Reflection.getValue(FM, "Wing");
            polares1.CxMin_0 = 0.0327F;
        }
        if(thisWeaponsName.startsWith("Fighter03:"))
        {
            hierMesh().chunkVisible("PylonL1", false);
            hierMesh().chunkVisible("PylonR1", false);
            Polares polares2 = (Polares)Reflection.getValue(FM, "Wing");
            polares2.CxMin_0 = 0.0329F;
        }
        if(thisWeaponsName.startsWith("Fighter04:"))
        {
            hierMesh().chunkVisible("PylonL2", false);
            hierMesh().chunkVisible("PylonR2", false);
            hierMesh().chunkVisible("PylonCenter", false);
            Polares polares3 = (Polares)Reflection.getValue(FM, "Wing");
            polares3.CxMin_0 = 0.0328F;
        }
        if(thisWeaponsName.startsWith("Fighter05:"))
        {
            hierMesh().chunkVisible("PylonL2", false);
            hierMesh().chunkVisible("PylonR2", false);
            Polares polares4 = (Polares)Reflection.getValue(FM, "Wing");
            polares4.CxMin_0 = 0.0328F;
        }
        if(thisWeaponsName.startsWith("Fighter06:"))
        {
            hierMesh().chunkVisible("PylonCenter", false);
            Polares polares5 = (Polares)Reflection.getValue(FM, "Wing");
            polares5.CxMin_0 = 0.0327F;
        }
        if(thisWeaponsName.startsWith("Fighter07:"))
        {
            Polares polares6 = (Polares)Reflection.getValue(FM, "Wing");
            polares6.CxMin_0 = 0.0328F;
        }
        if(thisWeaponsName.startsWith("Ferry:"))
        {
            hierMesh().chunkVisible("PylonL1", false);
            hierMesh().chunkVisible("PylonL2", false);
            hierMesh().chunkVisible("PylonCenter", false);
            hierMesh().chunkVisible("PylonR1", false);
            hierMesh().chunkVisible("PylonR2", false);
            hierMesh().chunkVisible("TankL", true);
            hierMesh().chunkVisible("TankR", true);
            float f = 300F;
            float f5 = ((FlightModelMain) (FM)).M.fuel / ((FlightModelMain) (FM)).M.maxFuel;
            ((FlightModelMain) (FM)).M.maxFuel += f;
            ((FlightModelMain) (FM)).M.fuel += f * f5;
            ((FlightModelMain) (FM)).M.referenceWeight += f * f5;
            Polares polares9 = (Polares)Reflection.getValue(FM, "Wing");
            polares9.CxMin_0 = 0.0327F;
        }
        if(thisWeaponsName.startsWith("Ferry01:"))
        {
            hierMesh().chunkVisible("PylonL1", false);
            hierMesh().chunkVisible("PylonL2", false);
            hierMesh().chunkVisible("PylonR1", false);
            hierMesh().chunkVisible("PylonR2", false);
            hierMesh().chunkVisible("TankL", true);
            hierMesh().chunkVisible("TankR", true);
            float f1 = 300F;
            float f6 = ((FlightModelMain) (FM)).M.fuel / ((FlightModelMain) (FM)).M.maxFuel;
            ((FlightModelMain) (FM)).M.maxFuel += f1;
            ((FlightModelMain) (FM)).M.fuel += f1 * f6;
            ((FlightModelMain) (FM)).M.referenceWeight += f1 * f6;
            Polares polares10 = (Polares)Reflection.getValue(FM, "Wing");
            polares10.CxMin_0 = 0.0328F;
        }
        if(thisWeaponsName.startsWith("Ferry02:"))
        {
            hierMesh().chunkVisible("PylonL1", false);
            hierMesh().chunkVisible("PylonR1", false);
            hierMesh().chunkVisible("PylonCenter", false);
            hierMesh().chunkVisible("TankL", true);
            hierMesh().chunkVisible("TankR", true);
            float f2 = 300F;
            float f7 = ((FlightModelMain) (FM)).M.fuel / ((FlightModelMain) (FM)).M.maxFuel;
            ((FlightModelMain) (FM)).M.maxFuel += f2;
            ((FlightModelMain) (FM)).M.fuel += f2 * f7;
            ((FlightModelMain) (FM)).M.referenceWeight += f2 * f7;
            Polares polares11 = (Polares)Reflection.getValue(FM, "Wing");
            polares11.CxMin_0 = 0.0329F;
        }
        if(thisWeaponsName.startsWith("Ferry03:"))
        {
            hierMesh().chunkVisible("PylonL1", false);
            hierMesh().chunkVisible("PylonR1", false);
            hierMesh().chunkVisible("TankL", true);
            hierMesh().chunkVisible("TankR", true);
            float f3 = 300F;
            float f8 = ((FlightModelMain) (FM)).M.fuel / ((FlightModelMain) (FM)).M.maxFuel;
            ((FlightModelMain) (FM)).M.maxFuel += f3;
            ((FlightModelMain) (FM)).M.fuel += f3 * f8;
            ((FlightModelMain) (FM)).M.referenceWeight += f3 * f8;
            Polares polares12 = (Polares)Reflection.getValue(FM, "Wing");
            polares12.CxMin_0 = 0.0331F;
        }
        if(thisWeaponsName.endsWith("xMk81(MER)"))
        {
            hierMesh().chunkVisible("PylonL1", false);
            hierMesh().chunkVisible("PylonR1", false);
            Polares polares7 = (Polares)Reflection.getValue(FM, "Wing");
            polares7.CxMin_0 = 0.0329F;
        }
        if(thisWeaponsName.startsWith("GAttack: "))
        {
            Polares polares8 = (Polares)Reflection.getValue(FM, "Wing");
            polares8.CxMin_0 = 0.033F;
        }
        if(thisWeaponsName.startsWith("GAttack&Wingtiptanks: "))
        {
            hierMesh().chunkVisible("TankL", true);
            hierMesh().chunkVisible("TankR", true);
            float f4 = 300F;
            float f9 = ((FlightModelMain) (FM)).M.fuel / ((FlightModelMain) (FM)).M.maxFuel;
            ((FlightModelMain) (FM)).M.maxFuel += f4;
            ((FlightModelMain) (FM)).M.fuel += f4 * f9;
            ((FlightModelMain) (FM)).M.referenceWeight += f4 * f9;
            Polares polares13 = (Polares)Reflection.getValue(FM, "Wing");
            polares13.CxMin_0 = 0.0332F;
        }
        if((getBulletEmitterByHookName("_ExternalTank01") instanceof FuelTankGun_TankF5A150galH2_gn16)
        || (getBulletEmitterByHookName("_ExternalTank01") instanceof FuelTankGun_TankF5E150galH2_gn16)
        || (getBulletEmitterByHookName("_ExternalTank01") instanceof FuelTankGun_TankF5E275gal_gn16))
        {
            pylonOccupied_DT = true;
            ((FlightModelMain) (super.FM)).Sq.dragAirbrakeCx = ((FlightModelMain) (super.FM)).Sq.dragAirbrakeCx / 2.0F;
        }
        if(getBulletEmitterByHookName("_ExternalDev05") instanceof Pylon_USMERmd_gn16)
        {
            pylonOccupied_Py = true;
            ((FlightModelMain) (super.FM)).Sq.dragAirbrakeCx = ((FlightModelMain) (super.FM)).Sq.dragAirbrakeCx / 2.0F;
        }
        if(pylonOccupied_DT || pylonOccupied_Py)
            pylonOccupied = true;
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
        super.pos.getRender(Actor._tmpLoc);
        if(lLight == null)
        {
            if(Actor._tmpLoc.getX() >= 1.0D)
            {
                lLight = new LightPointWorld[4];
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
                if(((FlightModelMain) (super.FM)).AS.astateLandingLightEffects[j] != null)
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
        if(super.FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(super.FM.isPlayers())
            bChangedPit = true;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag && World.Rnd().nextFloat() < 0.2F)
        {
            if(FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.12F)
            {
                FM.AS.explodeEngine(this, 0);
                msgCollision(this, "WingLIn_D0", "WingLIn_D0");
                if(World.Rnd().nextBoolean())
                    FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
                else
                    FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
            }
            if(FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.12F)
            {
                FM.AS.explodeEngine(this, 1);
                msgCollision(this, "WingRIn_D0", "WingRIn_D0");
                if(World.Rnd().nextBoolean())
                    FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
                else
                    FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
            }
        }
        if(FM.getAltitude() < 200F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
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
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Caged");
        } else
        if(k14Mode == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Uncaged");
        } else
        if(k14Mode == 2 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
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
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        if(k14WingspanType == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
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

    public void doEjectCatapult()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 30D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
        ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -117F);
        float f2 = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F);
        float f3 = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, -90F, -90F) : Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F);
        float f4 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -87F);
        float f5 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -100F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f5, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f5, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f3, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        float f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 35F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    public void moveSteering(float f)
    {
        if(((FlightModelMain) (super.FM)).CT.DiffBrakesType > 0)
        {
            if(((FlightModelMain) (super.FM)).CT.getGear() < 0.75F)
                return;
            hierMesh().chunkSetAngles("GearC21_D0", 0.0F, -30F * f, 0.0F);
        }
    }

    protected void moveElevator(float f)
    {
        if(super.FM.getSpeedKMH() > 570F)
        {
            float f1 = 1.7F * f;
            hierMesh().chunkSetAngles("VatorL_D0", 12F * f, -20F * f1, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", -12F * f, -20F * f1, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 12F * f, -20F * f, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", -12F * f, -20F * f, 0.0F);
        }
    }

    protected void moveAileron(float f)
    {
        if(super.FM.getSpeedKMH() > 570F)
        {
            float f1 = 1.5F * f;
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 20F * f1);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -20F * f1);
        } else
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 20F * f);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -20F * f);
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = -44.5F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveFan(float f)
    {
    }

    protected void moveSlats(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 0.04F);
        Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.04F);
        Aircraft.xyz[2] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Slat01_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        hierMesh().chunkSetLocate("Slat01_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.04F);
        Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.04F);
        hierMesh().chunkSetAngles("Slat02_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        hierMesh().chunkSetLocate("Slat02_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void slatsOff()
    {
        if(!bSlatsOff)
        {
            resetYPRmodifier();
            Aircraft.xyz[0] = 0.04F;
            Aircraft.xyz[1] = -0.04F;
            Aircraft.xyz[2] = 0.0F;
            hierMesh().chunkSetAngles("Slat01_D0", 0.0F, 8.5F, 0.0F);
            hierMesh().chunkSetLocate("Slat01_D0", Aircraft.xyz, Aircraft.ypr);
            Aircraft.xyz[0] = -0.04F;
            Aircraft.xyz[1] = -0.04F;
            hierMesh().chunkSetAngles("Slat02_D0", 0.0F, 8.5F, 0.0F);
            hierMesh().chunkSetLocate("Slat02_D0", Aircraft.xyz, Aircraft.ypr);
            bSlatsOff = true;
        }
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 70F * f, 0.0F, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(point3d.x > 1.7D)
            {
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setJamBullets(0, 0);
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setJamBullets(0, 1);
                if(World.Rnd().nextFloat() < 0.12F)
                    FM.AS.setJamBullets(1, 0);
                if(World.Rnd().nextFloat() < 0.12F)
                    FM.AS.setJamBullets(1, 1);
            }
            if(point3d.x > -0.999D && point3d.x < 0.53500000000000003D && point3d.z > -0.224D)
            {
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
            }
            if(point3d.x > 0.80000000000000004D && point3d.x < 1.5800000000000001D && World.Rnd().nextFloat() < 0.25F && (shot.powerType == 3 && getEnergyPastArmor(0.4F, shot) > 0.0F || shot.powerType == 0))
                FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, (int)(shot.power / 4000F)));
            if(point3d.x > -2.4849999999999999D && point3d.x < -1.6000000000000001D && World.Rnd().nextFloat() < 0.25F && (shot.powerType == 3 && getEnergyPastArmor(0.4F, shot) > 0.0F || shot.powerType == 0))
                FM.AS.hitTank(shot.initiator, 1, World.Rnd().nextInt(1, (int)(shot.power / 4000F)));
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else
        if(s.startsWith("xstabl"))
            hitChunk("StabL", shot);
        else
        if(s.startsWith("xstabr"))
            hitChunk("StabR", shot);
        else
        if(s.startsWith("xwing"))
        {
            if(s.endsWith("lin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.endsWith("rin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.endsWith("lmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.endsWith("rmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.endsWith("lout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.endsWith("rout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xengine"))
        {
            int i = s.charAt(7) - 49;
            if(point3d.x > 0.0D && point3d.x < 0.69699999999999995D)
                FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6));
            if(World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass)
                FM.AS.hitEngine(shot.initiator, i, 5);
        } else
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
            hitFlesh(j, shot, byte0);
        }
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 2)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 200F;
            hunted = War.GetNearestEnemyAircraft(((Interpolate) (super.FM)).actor, 2700F, 9);
        }
        if(hunted != null)
        {
            k14Distance = (float)((Interpolate) (super.FM)).actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
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
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(super.FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if((double)calculateMach() > 1.01D || (double)calculateMach() < 1.0D)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster())
        {
            for(int i = 0; i < 2; i++)
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
                    if(curthrl < 1.05F)
                    {
                        if((curthrl - oldthrl) / f > 20F && ((FlightModelMain) (super.FM)).EI.engines[i].getRPM() < 3200F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.01D * (double)(((FlightModelMain) (super.FM)).EI.engines[i].getRPM() / 1000F);
                            ((FlightModelMain) (super.FM)).EI.engines[i].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                ((FlightModelMain) (super.FM)).AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                ((FlightModelMain) (super.FM)).EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && ((FlightModelMain) (super.FM)).EI.engines[i].getRPM() < 3200F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.001D * (double)(((FlightModelMain) (super.FM)).EI.engines[i].getRPM() / 1000F);
                            ((FlightModelMain) (super.FM)).EI.engines[i].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.4F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            {
                                if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                ((FlightModelMain) (super.FM)).EI.engines[i].setEngineStops(this);
                            } else
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        }
                    }
                    oldthrl = curthrl;
                }

        }
    }

    private float flapsMovement(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = (float)Math.exp(-f / f3);
        float f6 = f1 + (f2 - f1) * f5;
        if(f6 < f1)
        {
            f6 += f4 * f;
            if(f6 > f1)
                f6 = f1;
        } else
        if(f6 > f1)
        {
            f6 -= f4 * f;
            if(f6 < f1)
                f6 = f1;
        }
        return f6;
    }

    public void update(float f)
    {
        if((((FlightModelMain) (super.FM)).AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && super.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = false;
            bailout();
        }
        if(((FlightModelMain) (super.FM)).CT.getFlap() < ((FlightModelMain) (super.FM)).CT.FlapsControl)
            ((FlightModelMain) (super.FM)).CT.forceFlaps(flapsMovement(f, ((FlightModelMain) (super.FM)).CT.FlapsControl, ((FlightModelMain) (super.FM)).CT.getFlap(), 999F, Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.08F)));
        else
            ((FlightModelMain) (super.FM)).CT.forceFlaps(flapsMovement(f, ((FlightModelMain) (super.FM)).CT.FlapsControl, ((FlightModelMain) (super.FM)).CT.getFlap(), 999F, Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.7F)));
        float f1 = super.FM.getSpeedKMH() - 1000F;
        if(f1 < 0.0F)
            f1 = 0.0F;
        ((FlightModelMain) (super.FM)).CT.dvGear = 0.2F - f1 / 1000F;
        if(((FlightModelMain) (super.FM)).CT.dvGear < 0.0F)
            ((FlightModelMain) (super.FM)).CT.dvGear = 0.0F;
        if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
        {
            if(((FlightModelMain) (super.FM)).AP.way.isLanding() && ((FlightModelMain) (super.FM)).Gears.onGround() && super.FM.getSpeed() > 40F)
            {
                ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
                if(((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
                    ((FlightModelMain) (super.FM)).CT.DragChuteControl = 1.0F;
            }
            if(((FlightModelMain) (super.FM)).AP.way.isLanding() && ((FlightModelMain) (super.FM)).Gears.onGround() && super.FM.getSpeed() < 40F)
            {
                ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
                if(super.FM.getSpeed() < 20F)
                    ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
            }
        }
        if(((FlightModelMain) (super.FM)).AS.isMaster() && Config.isUSE_RENDER())
        {
            for(int i = 0; i < 2; i++)
            {
                if(((FlightModelMain) (super.FM)).EI.engines[i].getPowerOutput() > 0.25F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6)
                {
                    if(((FlightModelMain) (super.FM)).EI.engines[i].getPowerOutput() > 1.001F)
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, i, 5);
                    else
                    if(((FlightModelMain) (super.FM)).EI.engines[i].getPowerOutput() > 0.65F && ((FlightModelMain) (super.FM)).EI.engines[i].getPowerOutput() < 1.001F)
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, i, 3);
                    else
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, i, 2);
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, i, 0);
                }
                setExhaustFlame(Math.round(Aircraft.cvt(((FlightModelMain) (super.FM)).EI.engines[i].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
            }

            if(super.FM instanceof RealFlightModel)
                umn();
        }
        if(pylonOccupied && (double)airBrake_State < 0.0015D && pylonOccupied_DT && !getBulletEmitterByHookName("_ExternalTank01").haveBullets())
        {
            pylonOccupied = false;
            ((FlightModelMain) (super.FM)).Sq.dragAirbrakeCx = ((FlightModelMain) (super.FM)).Sq.dragAirbrakeCx * 2.0F;
        }
        if(super.FM.getSpeed() > 5F)
        {
            moveSlats(f);
            bSlatsOff = false;
        } else
        {
            slatsOff();
        }
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        checkHydraulicStatus();
        moveHydraulics(f);
        soundbarier();
        Flaps();
        computeLift();
        computeEnergy();
        computeEngine();
        super.update(f);
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k]);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1: // '\001'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 2: // '\002'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5: // '\005'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 4: // '\004'
            ((FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
        }
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F, 50F * f);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F, 50F * f);
        if(!pylonOccupied)
        {
            hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F, 30F * f);
            hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F, 30F * f);
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
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 0 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep < 2)
            {
                if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl > 0.5F && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() > 0.5F)
                {
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
                    doRemoveBlisters();
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 2;
                }
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 2 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 3)
            {
                switch(((FlightModelMain) (super.FM)).AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl < 0.5F)
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    doRemoveBlisters();
                    break;
                }
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate = ((FlightModelMain) (super.FM)).AS;
                aircraftstate.astateBailoutStep = (byte)(aircraftstate.astateBailoutStep + 1);
                if(((FlightModelMain) (super.FM)).AS.astateBailoutStep == 4)
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 11 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 19)
            {
                byte byte0 = ((FlightModelMain) (super.FM)).AS.astateBailoutStep;
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate1 = ((FlightModelMain) (super.FM)).AS;
                aircraftstate1.astateBailoutStep = (byte)(aircraftstate1.astateBailoutStep + 1);
                if(byte0 == 11)
                {
                    super.FM.setTakenMortalDamage(true, null);
                    if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(((FlightModelMain) (super.FM)).AS.actor != World.getPlayerAircraft())
                            ((Maneuver)super.FM).set_maneuver(44);
                    }
                }
                if(((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11] < 99)
                {
                    doRemoveBodyFromPlane(byte0 - 10);
                    if(byte0 == 11)
                    {
                        doEjectCatapult();
                        super.FM.setTakenMortalDamage(true, null);
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(((FlightModelMain) (super.FM)).Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(((FlightModelMain) (super.FM)).Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    private final void umn()
    {
        Vector3d vector3d = super.FM.getVflow();
        mn = (float)vector3d.lengthSquared();
        mn = (float)Math.sqrt(mn);
        F_5 f_5 = this;
        float f = mn;
        World.cur().getClass();
        f_5.mn = f / Atmosphere.sonicSpeed((float)((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z);
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

    public void typeRadarGainMinus()
    {
        if(radarmode == 0)
        {
            lockrange -= 0.002F;
            if(lockrange < -0.01F)
                lockrange = -0.01F;
        } else
        {
            targetnum--;
            if(targetnum < 0)
                targetnum = 0;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Target number  " + targetnum);
        }
    }

    public void typeRadarGainPlus()
    {
        if(radarmode == 0)
        {
            lockrange += 0.002F;
            if(lockrange > 0.018F)
                lockrange = 0.018F;
        } else
        {
            targetnum++;
            if(targetnum > 10)
                targetnum = 0;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Target number  " + targetnum);
        }
    }

    public void typeRadarRangeMinus()
    {
    }

    public void typeRadarRangePlus()
    {
    }

    public void typeRadarReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public void typeRadarReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public boolean typeRadarToggleMode()
    {
        long l = Time.current();
        if(radarmode == 1)
            radarmode++;
        if(radarmode == 0 && l > twait + 5000L)
        {
            radarmode++;
            twait = l;
        }
        if(radarmode > 1)
            radarmode = 0;
        return false;
    }

    public void computeSubsonicLimiter()
    {
        float f = calculateMach();
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() == 6 && (double)calculateMach() >= 0.90000000000000002D)
            if((double)f > 0.96999999999999997D)
            {
                f1 = 0.001F;
            } else
            {
                float f2 = f * f;
                f1 = 0.0128571F * f - 0.0114714F;
            }
        ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += f1;
    }

    public void computeLift()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float f = calculateMach();
        if(calculateMach() < 0.0F);
        float f1 = 0.0F;
        if((double)f > 2.0999999046325684D)
        {
            f1 = 0.014F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            float f5 = f4 * f;
            float f6 = f5 * f;
            float f7 = f6 * f;
            float f8 = f7 * f;
            f1 = (((((0.156695F * f7 - 1.10767F * f6) + 2.8585F * f5) - 3.03812F * f4) + 0.677614F * f3 + 0.819203F * f2) - 0.420869F * f) + 0.13F;
        }
        polares.lineCyCoeff = f1;
    }

    public void computeEnergy()
    {
        float f = FM.getOverload();
        if(FM.getOverload() < 4.5F);
        float f1 = 0.0F;
        if((double)f >= 10D)
        {
            f1 = 0.085F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            float f5 = f4 * f;
            float f6 = f5 * f;
            f1 = ((8.42E-007F * f5 + 9.877E-007F * f4) - 9.47291E-006F * f3) + 2.22222E-006F * f2 + 1.70512E-005F * f;
        }
        ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += f1;
    }

    public void computeEngine()
    {
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5 && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            if(calculateMach() <= 0.0F);
        if((double)f > 13.5D)
        {
            f1 = 1.5F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            f1 = 0.0130719F * f2 - 0.0653595F * f;
        }
        FM.producedAF.x -= f1 * 1000F;
    }

    private boolean Flaps()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && (double)calculateMach() < 0.34999999999999998D && !Flaps && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && (double)calculateMach() < 0.34999999999999998D && !Flaps)
        {
            polares.Cy0_1 += 0.34999999999999998D;
            Flaps = true;
        }
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && (double)calculateMach() >= 0.34999999999999998D && Flaps && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() < 1.001F && (double)calculateMach() >= 0.34999999999999998D && Flaps)
        {
            polares.Cy0_1 -= 0.34999999999999998D;
            Flaps = false;
        }
        return Flaps;
    }

    public boolean typeDiveBomberToggleAutomation()
    {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset()
    {
    }

    public void typeDiveBomberAdjAltitudePlus()
    {
    }

    public void typeDiveBomberAdjAltitudeMinus()
    {
    }

    public void typeDiveBomberAdjVelocityReset()
    {
    }

    public void typeDiveBomberAdjVelocityPlus()
    {
    }

    public void typeDiveBomberAdjVelocityMinus()
    {
    }

    public void typeDiveBomberAdjDiveAngleReset()
    {
    }

    public void typeDiveBomberAdjDiveAnglePlus()
    {
    }

    public void typeDiveBomberAdjDiveAngleMinus()
    {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
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
        if(deltaAzimuth == 0.0F)
            return;
        fSightCurSideslip += 0.05F;
        if(fSightCurSideslip > 3F)
            fSightCurSideslip = 3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Float(fSightCurSideslip * 10F)
        });
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(deltaAzimuth == 0.0F)
            return;
        fSightCurSideslip -= 0.05F;
        if(fSightCurSideslip < -3F)
            fSightCurSideslip = -3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Float(fSightCurSideslip * 10F)
        });
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 850F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(deltaTangage == 0.0F)
            return;
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
        if(deltaTangage == 0.0F)
            return;
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
        if((double)Math.abs(FM.Or.getKren()) > 4.5D)
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
                if(FM.isTick(3, 0))
                {
                    if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else
                {
                    FM.CT.WeaponControl[3] = false;
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

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(true);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(false);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = true;
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

    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    private float deltaAzimuth;
    private float deltaTangage;
    private long twait;
    private float oldctl;
    private float curctl;
    private float oldthrl;
    private float curthrl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    public float AirBrakeControl;
    public float DragChuteControl;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private float lightTime;
    private float ft;
    private LightPointWorld lLight[];
    private Hook lLightHook[];
    private static Loc lLightLoc1 = new Loc();
    private static Point3d lLightP1 = new Point3d();
    private static Point3d lLightP2 = new Point3d();
    private static Point3d lLightPL = new Point3d();
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
    public static boolean bChangedPit = false;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    public static int LockState = 0;
    static Actor hunted = null;
    private float engineSurgeDamage;
    private float gearTargetAngle;
    private float gearCurrentAngle;
    public boolean hasHydraulicPressure;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    private boolean bCanopyInitState;
    public int radarmode;
    public int targetnum;
    public float lockrange;
    public boolean APmode1;
    public boolean APmode2;
    private boolean pylonOccupied_DT;
    private boolean pylonOccupied_Py;
    private boolean pylonOccupied;
    private float airBrake_State;
    private boolean Flaps;
    protected boolean bSlatsOff;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.F_5.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}