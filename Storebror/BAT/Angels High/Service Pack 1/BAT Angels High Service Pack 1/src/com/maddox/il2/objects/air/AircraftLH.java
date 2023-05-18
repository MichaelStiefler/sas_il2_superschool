package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.Fuze;
import com.maddox.il2.objects.weapons.Fuze_Barometric;
import com.maddox.il2.objects.weapons.Fuze_EL_AZ;
import com.maddox.il2.objects.weapons.Fuze_Proximity;
import com.maddox.il2.objects.weapons.Fuze_SABBarometric;
import com.maddox.il2.objects.weapons.Torpedo;
import com.maddox.rts.Finger;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.LDRres;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.util.HashMapInt;
import com.maddox.util.NumberTokenizer;

public abstract class AircraftLH extends Aircraft
{

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
//        String s3 = "";
        String s6 = null;
//        String s7 = "";
        if(fuze instanceof Fuze_EL_AZ)
            try
            {
                String s4 = resourcebundle.getString(s1);
                int i = 1;
                int k = 0;
                for(int j1 = 0; j1 < s4.length(); j1++)
                    if(s4.charAt(j1) == '\n')
                    {
                        as[i] = "  " + s4.substring(k, j1);
                        k = j1 + 1;
                        i++;
                    }

            }
            catch(MissingResourceException missingresourceexception1)
            {
                System.out.println(missingresourceexception1);
            }
        else
        if((fuze instanceof Fuze_SABBarometric) || (fuze instanceof Fuze_Barometric) || (fuze instanceof Fuze_Proximity))
        {
            int j = (int)f;
            as[1] = "  " + resourcebundle.getString("neta.BombFuzeAlt") + " " + j + " " + resourcebundle.getString("neta.m.");
            as[2] = "";
            as[4] = "  " + resourcebundle.getString("neta.BombFuzeAlt") + " " + (int)((float)j * 3.28084F) + " " + resourcebundle.getString("neta.ft.");
        } else
        {
            float f1 = Property.floatValue(fuze.getClass(), "airTravelToArm", -1F);
            String s5;
            if(f1 == -1F)
            {
                int l = Property.intValue(fuze.getClass(), "armingTime", 2000);
                String s9 = "" + (float)l / 1000F;
                s9 = s9.substring(0, s9.indexOf(".") + 2);
                s5 = "  " + resourcebundle.getString("Arming") + " " + s9 + " " + resourcebundle.getString("ArmingTimeSeconds");
            } else
            {
                int i1 = Math.round(floatindex(Aircraft.cvt(f1, 0.0F, 750F, 0.0F, 15F), armingTravelToAltScale));
                s5 = "  " + resourcebundle.getString("Arming") + " ~" + i1 + " " + resourcebundle.getString("DropAltM");
                s6 = "  " + resourcebundle.getString("Arming") + " ~" + (int)((float)i1 * 3.28084F) + " " + resourcebundle.getString("DropAltFt");
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
        if(World.getPlayerAircraft() == this && !NetMissionTrack.isPlaying() && !World.isPlayerGunner() && this.FM.AS.astatePilotStates[0] <= 95)
        {
            HookPilot hookpilot = HookPilot.current;
            if(hookpilot != null)
                setHeadAngles(-hookpilot.getAzimut(), hookpilot.getTangage());
        }
        movePilotsHead(this.viewAzimut, this.viewTangage);
    }

    protected static void XweaponsRegister(Class class1, String s, String as[])
    {
        if(as.length != Aircraft.getWeaponHooksRegistered(class1).length)
            throw new RuntimeException("Sizeof 'weaponSlots' != sizeof 'weaponHooks'");
        int ai[] = Aircraft.getWeaponTriggersRegistered(class1);
        ArrayList arraylist = XweaponsListProperty(class1);
        HashMapInt hashmapint = XweaponsMapProperty(class1);
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[as.length];
        try
        {
            for(int i = 0; i < as.length; i++)
                if(as[i] != null)
                {
                    NumberTokenizer numbertokenizer = new NumberTokenizer(as[i]);
                    a_lweaponslot[i] = new Aircraft._WeaponSlot(ai[i], numbertokenizer.next(null), numbertokenizer.next(-12345));
                }

        }
        catch(Exception exception)
        {
            throw new RuntimeException(exception.toString());
        }
        int j = arraylist.indexOf(s);
        if(j >= 0)
            arraylist.remove(j);
        arraylist.add(s);
        hashmapint.put(Finger.Int(s), a_lweaponslot);
    }

    private static ArrayList XweaponsListProperty(Class class1)
    {
        Object obj = Property.value(class1, "weaponsList", null);
        if(obj != null)
        {
            return (ArrayList)obj;
        } else
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            return arraylist;
        }
    }

    private static HashMapInt XweaponsMapProperty(Class class1)
    {
        Object obj = Property.value(class1, "weaponsMap", null);
        if(obj != null)
        {
            return (HashMapInt)obj;
        } else
        {
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            return hashmapint;
        }
    }
    
    // TODO: Fixed by SAS~Storebror: Private Method from "Aircraft" Class shifted here...
    public static boolean isPlayersWing(Aircraft aircraft)
    {
      try
      {
        Wing wing = aircraft.getWing();
        if (wing == null) {
          return false;
        }
        for (int i = 0; i < wing.airc.length; i++)
        {
          Aircraft aircraft2 = wing.airc[i];
          if (aircraft2 == World.getPlayerAircraft()) {
            return true;
          }
          if ((aircraft2.isNetPlayer()) || (aircraft2.isNetMaster())) {
            return false;
          }
        }
        return false;
      }
      catch (Exception e) {}
      return false;
    }
    //...

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        // TODO: Fixed by SAS~Storebror: Private Method from "Aircraft" Class shifted here...
        //if(Mission.isCoop() && !initialFuzeSynced && (this == World.getPlayerAircraft() || Aircraft.isPlayersWing(this)))
        if(Mission.isCoop() && !initialFuzeSynced && (this == World.getPlayerAircraft() || isPlayersWing(this)))
        //...
        {
            UserCfg usercfg = World.cur().userCfg;
            int i = usercfg.fuzeType;
            float f1 = usercfg.bombDelay;
            this.FM.AS.replicateFuzeStatesToNet(i, 1, f1);
            initialFuzeSynced = true;
        }
        if(this == World.getPlayerAircraft())
        {
            if(!World.cur().diffCur.No_Outside_Views && World.cur().diffCur.NoOwnPlayerViews && Main3D.cur3D().isViewOutside() && Main3D.cur3D().viewActor() == World.getPlayerAircraft() && !Aircraft.isPlayerTaxing())
                HotKeyCmd.exec("aircraftView", "CockpitView");
            if((double)this.FM.CT.getGear() > 0.01D && (this.FM.AS.gearStates[0] != 0.0F || this.FM.AS.gearStates[1] != 0.0F || this.FM.AS.gearStates[2] != 0.0F))
            {
                if(this.FM.getOverload() > World.Rnd().nextFloat(3F, 6F))
                {
                    if(this.FM.AS.gearStates[0] < 0.0F && this.FM.AS.gearDamRecoveryStates[0] < 2)
                        this.FM.AS.fixGear(this, 0);
                    if(this.FM.AS.gearStates[1] < 0.0F && this.FM.AS.gearDamRecoveryStates[1] < 2)
                        this.FM.AS.fixGear(this, 1);
                    if(this.FM.AS.gearStates[2] < 0.0F && this.FM.AS.gearDamRecoveryStates[2] < 2)
                        this.FM.AS.fixGear(this, 2);
                }
                if((double)this.FM.CT.getGear() > 0.1D && (double)this.FM.CT.getGear() < 0.2D && this.FM.CT.GearControl == 0.0F)
                {
                    if(this.FM.AS.gearStates[0] < 0.0F && this.FM.AS.gearDamRecoveryStates[0] == 0)
                        this.FM.AS.fixGear(this, 0);
                    if(this.FM.AS.gearStates[1] < 0.0F && this.FM.AS.gearDamRecoveryStates[1] == 0)
                        this.FM.AS.fixGear(this, 1);
                    if(this.FM.AS.gearStates[2] < 0.0F && this.FM.AS.gearDamRecoveryStates[2] == 0)
                        this.FM.AS.fixGear(this, 2);
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
            this.FM.AS.beaconPlus();
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
            this.FM.AS.beaconMinus();
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
            this.FM.AS.setBeacon(i);
            return;
        }
    }

    public void auxPlus(int i)
    {
        switch(i)
        {
        case 1:
            this.headingBug++;
            if(this.headingBug >= 360F)
                this.headingBug = 0.0F;
            if(Main3D.cur3D().cockpitCur.printCompassHeading && World.cur().diffCur.RealisticNavigationInstruments && bWantBeaconKeys)
                HUD.log(hudLogCompassId, "CompassHeading", new Object[] {
                    "" + (int)this.headingBug
                });
            break;
        }
    }

    public void auxMinus(int i)
    {
        switch(i)
        {
        case 1:
            this.headingBug--;
            if(this.headingBug < 0.0F)
                this.headingBug = 359F;
            if(Main3D.cur3D().cockpitCur.printCompassHeading && World.cur().diffCur.RealisticNavigationInstruments && bWantBeaconKeys)
                HUD.log(hudLogCompassId, "CompassHeading", new Object[] {
                        "" + (int)this.headingBug
                });
            break;
        }
    }

    public void auxPressed(int i)
    {
        if(i == 1)
            this.FM.CT.dropExternalStores(true);
        if(i == 2 && hasFuzeModeSelector && World.cur().diffCur.BombFuzes)
        {
            Fuze_EL_AZ.toggleELAZFuzeMode();
            UserCfg usercfg = World.cur().userCfg;
            int j = usercfg.fuzeType;
            float f = usercfg.bombDelay;
            this.FM.AS.replicateFuzeStatesToNet(j, Fuze_EL_AZ.getFuzeMode(), f);
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
        if(i == 5 && this.FM.CT.bHasBayDoorControl && ((Aircraft)this.FM.actor).canOpenBombBay())
            if(this.FM.CT.BayDoorControl > 0.5F && this.FM.CT.getBayDoor() > 0.99F)
            {
                this.FM.CT.BayDoorControl = 0.0F;
                HUD.log("BombBayClosed");
            } else
            if(this.FM.CT.BayDoorControl < 0.5F && this.FM.CT.getBayDoor() < 0.01F)
            {
                this.FM.CT.BayDoorControl = 1.0F;
                HUD.log("BombBayOpen");
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

        case 0:
            if(World.Rnd().nextFloat() < 0.05F)
                return;
            if(shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
                HUD.logCenter("H E A D S H O T");
            l *= 30;
            break;

        case 1:
            if(World.Rnd().nextFloat() < 0.08F)
            {
                l *= 2;
//                boolean flag = true;
                k = World.Rnd().nextInt(1, 15) * 8000;
                break;
            }
            boolean flag1 = World.Rnd().nextInt(0, 100 - l) <= 20;
            if(flag1)
                k = l / World.Rnd().nextInt(1, 10);
            break;

        case 2:
            if(World.Rnd().nextFloat() < 0.015F)
            {
//                boolean flag2 = true;
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
        this.FM.AS.hitPilot(shot.initiator, i, l);
        if(World.cur().diffCur.RealisticPilotVulnerability)
        {
            if(k > 0)
                this.FM.AS.setBleedingPilot(shot.initiator, i, k);
            if(i == 0 && j > 0)
                this.FM.AS.woundedPilot(shot.initiator, j, l);
        }
        if(this.FM.AS.astatePilotStates[i] > 95 && j == 0)
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
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public boolean gearEmergencyUp()
    {
        return true;
    }

    public boolean gearEmergencyDown()
    {
        return true;
    }

    private static Map infoMap = new HashMap();
    public static int hudLogCompassId = HUD.makeIdLog();
    public static boolean hasFuzeModeSelector = false;
    private boolean initialFuzeSynced;
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

    // TODO: Added by SAS~Storebror for Plane Cockpit backward compatibility with 4.10 based mods!
    public static boolean printCompassHeading = false;
    // ---
}
