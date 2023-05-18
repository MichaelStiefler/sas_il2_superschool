/*Modified Aircraft class for the SAS Engine Mod*/
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import java.util.*;

public abstract class AircraftLH extends Aircraft
{
    //TODO: Default Parameters
    //----------------------------------------------------------
    private static Map infoMap = new HashMap();
    public static int hudLogCompassId = HUD.makeIdLog();
    public static boolean hasFuzeModeSelector = false;
    private static final float armingTravelToAltScale[] = {
        0.0F, 8F, 13F, 17F, 29F, 43F, 55F, 66F, 78F, 92F, 
        116F, 152F, 186F, 223F, 260F, 300F
    };
    public boolean bWantBeaconKeys;
    private float headPos[];
    private float headOr[];
    private static Orient tmpOrLH = new Orient();
    private float headYp;
    private float headTp;
    private float headYm;
    private float headTm;
    //----------------------------------------------------------

    //TODO: New Parameters
    //----------------------------------------------------------
    private static float fShakeThreshold = 0.2F;    // Shake Threshold, apply no shake if shake level would be lower than this value
    private static float fMaxShake = 0.4F;          // Maximum Shake Level
    private static float fStartupShakeLevel = 0.5F; // Max. Startup Shake Level in range 0.0F - 1.0F
    private float[] fEngineShakeLevel = null;       // Array of current shake levels per engine
    public static boolean printCompassHeading = false;
    private boolean initialFuzeSynced;
    //----------------------------------------------------------

    public AircraftLH()
    {
        initialFuzeSynced = false;
        bWantBeaconKeys = false;
        headPos = new float[3];
        headOr = new float[3];
    }

    public void destroy()
    {
        if(this == World.getPlayerAircraft())
        {
            clearInfo();
            Torpedo.clearInfo();
        }
        super.destroy();
    }

    public static void clearInfo()
    {
        infoMap.clear();
    }

    public static Map getInfoList()
    {
        return infoMap;
    }

    public static void setInfo(Fuze fuze, String s, String s1, float f)
    {
        ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/gui", RTSConf.cur.locale, LDRres.loader());
        String s2 = s;
        String as[] = new String[5];
        try
        {
            s2 = resourcebundle.getString(s);
        }
        catch(MissingResourceException missingresourceexception)
        {
            System.out.println(missingresourceexception);
        }
        as[0] = resourcebundle.getString("Bomb") + " " + s2;
        String s6 = null;
        if(fuze instanceof Fuze_EL_AZ)
        {
            try
            {
                String s4 = resourcebundle.getString(s1);
                int i = 1;
                int j = 0;
                for(int i1 = 0; i1 < s4.length(); i1++)
                    if(s4.charAt(i1) == '\n')
                    {
                        as[i] = "  " + s4.substring(j, i1);
                        j = i1 + 1;
                        i++;
                    }

            }
            catch(MissingResourceException missingresourceexception1)
            {
                System.out.println(missingresourceexception1);
            }
        } else
        {
            float f1 = Property.floatValue(fuze.getClass(), "airTravelToArm", -1F);
            String s5;
            if(f1 == -1F)
            {
                int k = Property.intValue(fuze.getClass(), "armingTime", 2000);
                String s9 = "" + (float)k / 1000F;
                s9 = s9.substring(0, s9.indexOf(".") + 2);
                s5 = "  " + resourcebundle.getString("Arming") + " " + s9 + " " + resourcebundle.getString("ArmingTimeSeconds");
            } else
            {
                int l = Math.round(floatindex(cvt(f1, 0.0F, 750F, 0.0F, 15F), armingTravelToAltScale));
                s5 = "  " + resourcebundle.getString("Arming") + " ~" + l + " " + resourcebundle.getString("DropAltM");
                s6 = "  " + resourcebundle.getString("Arming") + " ~" + (int)((float)l * 3.28084F) + " " + resourcebundle.getString("DropAltFt");
            }
            String s8 = "  " + resourcebundle.getString("Delay") + " " + f + " " + resourcebundle.getString("ArmingTimeSeconds");
            as[1] = s5;
            as[2] = s8;
            as[4] = s6;
        }
        if(!infoMap.containsKey(s))
            infoMap.put(s, as);
    }

    protected static float floatindex(float f, float af[])
    {
        int i = (int)f;
        if(i >= af.length - 1)
            return af[af.length - 1];
        if(i < 0)
            return af[0];
        if(i == 0)
        {
            if(f > 0.0F)
                return af[0] + f * (af[1] - af[0]);
            else
                return af[0];
        } else
        {
            return af[i] + (f % (float)i) * (af[i + 1] - af[i]);
        }
    }

    public void update(float f)
    {
        super.update(f);
        if(World.getPlayerAircraft() == this && !NetMissionTrack.isPlaying() && !World.isPlayerGunner() && FM.AS.astatePilotStates[0] <= 95)
        {
            HookPilot hookpilot = HookPilot.current;
            if(hookpilot != null)
                setHeadAngles(-hookpilot.getAzimut(), hookpilot.getTangage());
        }
        movePilotsHead(viewAzimut, viewTangage);//TODO: Plane shake code
        //--------------------------------------------------------------------
        if(!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel)) return; // don't shake for non-player aircraft.
        if (this.fEngineShakeLevel == null) this.fEngineShakeLevel = new float[this.FM.EI.getNum()]; // initialize damaged engines array if necessary.
        Arrays.fill(this.fEngineShakeLevel, 0.0F); // initialize Engine Shake Level Array
        for (int i=0; i<this.FM.EI.getNum(); i++) { // check each engine's stage
            if (this.FM.EI.engines[i].getType() > Motor._E_TYPE_RADIAL) continue; // shake applies to piston engines only
            if (this.FM.EI.engines[i].getStage() < Motor._E_STAGE_CATCH_UP) continue; // engine is not running, no shake
            if (this.FM.EI.engines[i].getStage() > Motor._E_STAGE_NOMINAL) {
                if (this.FM.EI.engines[i].getRPM() == 0.0F)    continue; // engine is not running, no shake
            }
            if (this.FM.EI.engines[i].getStage() < Motor._E_STAGE_CATCH_FIRE)
                this.fEngineShakeLevel[i] = fStartupShakeLevel * (float)(this.FM.EI.engines[i].getStage()) / 5.0F; // engine is starting, set specified startup shake level
            else {
                this.fEngineShakeLevel[i] = 1.0F - this.FM.EI.engines[i].getReadyness(); // engine is running, set shake level according to damage
                if (this.fEngineShakeLevel[i] < fShakeThreshold) this.fEngineShakeLevel[i] = 0.0F; // only take shake levels into account which exceed the threshold shake setting
            }
            // Shake whole aircraft, not just inside the cockpit.
            if (this.fEngineShakeLevel[i] == 0.0F) continue; // don't apply aircraft shake force vector if there's no shake to apply
            Point3f theEnginePos = this.FM.EI.engines[i].getEnginePos(); // get engine position
            Vector3f theEngineShake = new Vector3f(World.Rnd().nextFloat(-1.0F, 1.0F), World.Rnd().nextFloat(-1.0F, 1.0F), World.Rnd().nextFloat(-1.0F, 1.0F)); // generate random shake force vector
            float fShakeFactor = (float)Math.pow(this.FM.M.massEmpty, 0.3F) / (float)this.FM.EI.getNum() * 10000F * fMaxShake; // calculate shake force, must fit for aircraft weight etc.
            theEngineShake.scale(this.fEngineShakeLevel[i] * fShakeFactor); // scale random shake vector accordingly
            Vector3f theEngineMomentum = new Vector3f(); // instantiate the damage shake moment
            theEngineMomentum.cross(theEnginePos, theEngineShake); // damage shake momentum is the vector's cartesian product between engine pos and random shake force
            this.FM.producedAM.x += theEngineMomentum.x; // apply x-axis turn momentum only
        }

        float fTotalShake = 0.0F; // aggregated shake level of all engines
        int iShakeWeightFactor = 1 << (this.FM.EI.getNum() - 1); // weighted shake, engine with highest shake level counts most
        if (this.FM.EI.getNum() == 1) { // single engine aircraft, the engine's shake level equals total shake level
            fTotalShake = this.fEngineShakeLevel[0];
        } else { // multi engine aircraft, take all shake level into account (weighted)
            Arrays.sort(fEngineShakeLevel); // sort shake level array (sorts in ascending order)
            for (int i=this.FM.EI.getNum()-1; i>=0; i--) { // go through the list of engine shake levels in descending order
                if (this.fEngineShakeLevel[i] == 0.0F) break; // no more engine shake? exit loop.
                fTotalShake += this.fEngineShakeLevel[i] * (float)iShakeWeightFactor; // add weighted total shake
                iShakeWeightFactor >>= 1; // reduce weight factor by 2
            }
            fTotalShake /= (float)((1 << this.FM.EI.getNum()) - 1); // adjust total shake to 0.0F through 1.0F again.
        }
        if (((RealFlightModel)this.FM).producedShakeLevel < fTotalShake * fMaxShake) // if in-cockpit shake is stronger already, do nothing.
            ((RealFlightModel)this.FM).producedShakeLevel = fTotalShake * fMaxShake; // apply shake level according to max shake level setting.
        //--------------------------------------------------------------------
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        // TODO: Fixed by SAS~Storebror: Private Method from "Aircraft" Class shifted here...
        //if(Mission.isCoop() && !initialFuzeSynced && (this == World.getPlayerAircraft() || Aircraft.isPlayersWing(this)))
        if(Mission.isCoop() && !initialFuzeSynced && (this == World.getPlayerAircraft() || isPlayersWing(this)))
        {
            UserCfg usercfg = World.cur().userCfg;
            int i = usercfg.fuzeType;
            float f1 = usercfg.bombDelay;
            this.FM.AS.replicateFuzeStatesToNet(i, 1, f1);
            initialFuzeSynced = true;
        }
        //...
        if(this == World.getPlayerAircraft())
        {
            if(!World.cur().diffCur.No_Outside_Views && World.cur().diffCur.NoOwnPlayerViews && Main3D.cur3D().isViewOutside() && Main3D.cur3D().viewActor() == World.getPlayerAircraft() && !Aircraft.isPlayerTaxing())
                HotKeyCmd.exec("aircraftView", "CockpitView");
            if((double)FM.CT.getGear() > 0.01D && (FM.AS.gearStates[0] != 0.0F || FM.AS.gearStates[1] != 0.0F || FM.AS.gearStates[2] != 0.0F))
            {
                if(FM.getOverload() > World.Rnd().nextFloat(3F, 6F))
                {
                    if(FM.AS.gearStates[0] < 0.0F && FM.AS.gearDamRecoveryStates[0] < 2)
                        FM.AS.fixGear(this, 0);
                    if(FM.AS.gearStates[1] < 0.0F && FM.AS.gearDamRecoveryStates[1] < 2)
                        FM.AS.fixGear(this, 1);
                    if(FM.AS.gearStates[2] < 0.0F && FM.AS.gearDamRecoveryStates[2] < 2)
                        FM.AS.fixGear(this, 2);
                }
                if((double)FM.CT.getGear() > 0.10000000000000001D && (double)FM.CT.getGear() < 0.20000000000000001D && FM.CT.GearControl == 0.0F)
                {
                    if(FM.AS.gearStates[0] < 0.0F && FM.AS.gearDamRecoveryStates[0] == 0)
                        FM.AS.fixGear(this, 0);
                    if(FM.AS.gearStates[1] < 0.0F && FM.AS.gearDamRecoveryStates[1] == 0)
                        FM.AS.fixGear(this, 1);
                    if(FM.AS.gearStates[2] < 0.0F && FM.AS.gearDamRecoveryStates[2] == 0)
                        FM.AS.fixGear(this, 2);
                }
            }
        }
    }

    public void beaconPlus()
    {
        if(!bWantBeaconKeys || Main.cur().mission.getBeacons(getArmy()) != null && Main.cur().mission.getBeacons(getArmy()).size() == 0)
        {
            return;
        } else
        {
            FM.AS.beaconPlus();
            return;
        }
    }

    public void beaconMinus()
    {
        if(!bWantBeaconKeys || Main.cur().mission.getBeacons(getArmy()) != null && Main.cur().mission.getBeacons(getArmy()).size() == 0)
        {
            return;
        } else
        {
            FM.AS.beaconMinus();
            return;
        }
    }

    public void beaconSet(int i)
    {
        if(!bWantBeaconKeys || Main.cur().mission.getBeacons(getArmy()) != null && Main.cur().mission.getBeacons(getArmy()).size() == 0)
        {
            return;
        } else
        {
            FM.AS.setBeacon(i);
            return;
        }
    }

    public void auxPlus(int i)
    {
        switch(i)
        {
        case 1: // '\001'
        headingBug = headingBug + 1.0F;
        if(headingBug >= 360F)
            headingBug = 0.0F;
        if(Main3D.cur3D().cockpitCur.printCompassHeading && World.cur().diffCur.RealisticNavigationInstruments && bWantBeaconKeys)
            HUD.log(hudLogCompassId, "CompassHeading", new Object[] {
                    "" + (int)headingBug
            });
        break;
        }
    }

    public void auxMinus(int i)
    {
        switch(i)
        {
        case 1: // '\001'
            headingBug = headingBug - 1.0F;
            if(headingBug < 0.0F)
                headingBug = 359F;
            if(Main3D.cur3D().cockpitCur.printCompassHeading && World.cur().diffCur.RealisticNavigationInstruments && bWantBeaconKeys)
                HUD.log(hudLogCompassId, "CompassHeading", new Object[] {
                        "" + (int)headingBug
                });
            break;
        }
    }

    public void auxPressed(int i)
    {
        if(i == 1)
            FM.CT.dropExternalStores(true);
        if(i == 2 && hasFuzeModeSelector && World.cur().diffCur.BombFuzes)
        {
            Fuze_EL_AZ.toggleELAZFuzeMode();
            UserCfg usercfg = World.cur().userCfg;
            int j = usercfg.fuzeType;
            float f = usercfg.bombDelay;
            FM.AS.replicateFuzeStatesToNet(j, Fuze_EL_AZ.getFuzeMode(), f);
        }
        if(i == 3)
        {
            Cockpit cockpit = Main3D.cur3D().cockpitCur;
            if(cockpit instanceof CockpitPilot)
                ((CockpitPilot)cockpit).toggleReticleBrightness();
        }
        if(i == 4)
            if(!Aircraft.showTaxingWay)
            {
                if(isAircraftTaxing())
                    Aircraft.showTaxingWay = !Aircraft.showTaxingWay;
            } else
            {
                Aircraft.showTaxingWay = !Aircraft.showTaxingWay;
            }
        if(i == 5 && FM.CT.bHasBayDoorControl && ((Aircraft)FM.actor).canOpenBombBay())
            if(FM.CT.BayDoorControl > 0.5F && FM.CT.getBayDoor() > 0.99F)
            {
                FM.CT.BayDoorControl = 0.0F;
                HUD.log("BombBayClosed");
            } else
                if(FM.CT.BayDoorControl < 0.5F && FM.CT.getBayDoor() < 0.01F)
                {
                    FM.CT.BayDoorControl = 1.0F;
                    HUD.log("BombBayOpen");
                }
        if (i == 40 && FM.CT.bHasFormationLights)
            if (FM.CT.bFormationLights) {
                FM.CT.bFormationLights = false;
                HUD.log("FormationLightsOFF");
            } else {
                FM.CT.bFormationLights = true;
                HUD.log("FormationLightsON");
            }
        if (i == 41 && FM.CT.bHasAntiColLights)
            if (FM.CT.bAntiColLights) {
                FM.CT.bAntiColLights = false;
                HUD.log("AntiColLightsOFF");
            } else {
                FM.CT.bAntiColLights = true;
                HUD.log("AntiColLightsON");
            }
    }

    protected void hitFlesh(int i, Shot shot, int j)
    {
        int k = 0;
        int l = (int)(shot.power * 0.0035F * World.Rnd().nextFloat(0.5F, 1.5F));
        switch(j)
        {
        default:
            break;

        case 0: // '\0'
        if(World.Rnd().nextFloat() < 0.05F)
            return;
        if(shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
            HUD.logCenter("H E A D S H O T");
        l *= 30;
        break;

        case 1: // '\001'
            if(World.Rnd().nextFloat() < 0.08F)
            {
                l *= 2;
                k = World.Rnd().nextInt(1, 15) * 8000;
                break;
            }
            boolean flag1 = World.Rnd().nextInt(0, 100 - l) <= 20;
            if(flag1)
                k = l / World.Rnd().nextInt(1, 10);
            break;

        case 2: // '\002'
            if(World.Rnd().nextFloat() < 0.015F)
            {
                k = World.Rnd().nextInt(1, 15) * 1000;
            } else
            {
                boolean flag3 = World.Rnd().nextInt(0, 100 - l) <= 10;
                if(flag3)
                    k = l / World.Rnd().nextInt(1, 15);
            }
            l = (int)((float)l / 1.5F);
            break;
        }
        debuggunnery("*** Pilot " + i + " hit for " + l + "% (" + (int)shot.power + " J)");
        FM.AS.hitPilot(shot.initiator, i, l);
        if(World.cur().diffCur.RealisticPilotVulnerability)
        {
            if(k > 0)
                FM.AS.setBleedingPilot(shot.initiator, i, k);
            if(i == 0 && j > 0)
                FM.AS.woundedPilot(shot.initiator, j, l);
        }
        if(FM.AS.astatePilotStates[i] > 95 && j == 0)
            debuggunnery("*** Headshot!.");
    }

    public void movePilotsHead(float f, float f1)
    {
        if(Config.isUSE_RENDER() && (headTp < f1 || headTm > f1 || headYp < f || headYm > f))
        {
            headTp = f1 + 0.0005F;
            headTm = f1 - 0.0005F;
            headYp = f + 0.0005F;
            headYm = f - 0.0005F;
            f *= 0.7F;
            f1 *= 0.7F;
            tmpOrLH.setYPR(0.0F, 0.0F, 0.0F);
            tmpOrLH.increment(0.0F, f, 0.0F);
            tmpOrLH.increment(f1, 0.0F, 0.0F);
            tmpOrLH.increment(0.0F, 0.0F, -0.2F * f1 + 0.05F * f);
            headOr[0] = tmpOrLH.getYaw();
            headOr[1] = tmpOrLH.getPitch();
            headOr[2] = tmpOrLH.getRoll();
            headPos[0] = 0.0005F * Math.abs(f);
            headPos[1] = -0.0001F * Math.abs(f);
            headPos[2] = 0.0F;
            hierMesh().chunkSetLocate("Head1_D0", headPos, headOr);
        }
    }

    public void doWreck(String s)
    {
        if(hierMesh().chunkFindCheck(s) != -1)
        {
            hierMesh().hideSubTrees(s);
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind(s));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }
    
    // TODO: Fixed by SAS~Storebror: Private Method from "Aircraft" Class shifted here...
    public static boolean isPlayersWing(Aircraft aircraft)
    {
        try
        {
            Wing wing = aircraft.getWing();
            if (wing == null)
            {
              return false;
            }
            for (int i = 0; i < wing.airc.length; i++)
            {
                Aircraft aircraft2 = wing.airc[i];
                if (aircraft2 == World.getPlayerAircraft())
                {
                    return true;
                }
                if ((aircraft2.isNetPlayer()) || (aircraft2.isNetMaster()))
                {
                    return false;
                }
            }
            return false;
        }
        catch (Exception e) {}
        return false;
    }
    //...

}
