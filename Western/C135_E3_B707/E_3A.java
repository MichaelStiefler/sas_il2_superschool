package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.MissileInterceptable;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import com.maddox.sas1946.il2.util.TrueRandom;
import java.io.IOException;
import java.util.*;


public class E_3A extends C_135
    implements TypeBomber, TypeRadar
{

    public class EnemyRadarData implements Comparable
    {
        Actor actor;
        double distance;
        double speed;
        double dirdiff;

        public EnemyRadarData()
        {
            actor = null;
            distance = -1.0D;
            speed = -1.0D;
            dirdiff = 0.0D;
        }

        public int compareTo(Object other)
        {
            EnemyRadarData otherEnemy = (EnemyRadarData)other;
            int r = 0;
            if(this.distance - otherEnemy.distance > 0.0D) r = 1;
            else if(this.distance - otherEnemy.distance < 0.0D) r = -1;
            return r;
        }
    }

    public E_3A()
    {
        rotoMode = 0;   // 0=Stop, 1=Slow Rotate / No Radar working, 2=Fast Rotate / Radar working
        rotoOrient = 0.0F;
        rotodegpm = 0;
        rareTimer = -1L;
        awacsTimer = -1L;
        awacsCounter = 0;
        enemyPlaneList = new ArrayList();
        enemyCruiseMissileList = new ArrayList();
        friendlyPlaneList = new ArrayList();
        friendlyAwacsList = new ArrayList();
        radarDisplayRange = 0;
        radarDisplayVrt = 0;
        radarDisplayHol = 0;
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "E-3A_";
    }

    private static double distanceBetween(Actor actorFrom, Actor actorTo)
    {
        double distanceRetVal = 999999.999D;
        if(!(Actor.isValid(actorFrom)) || !(Actor.isValid(actorTo))) return distanceRetVal;
        Loc distanceActorLoc = new Loc();
        Point3d distanceActorPos = new Point3d();
        Point3d distanceTargetPos = new Point3d();
        actorFrom.pos.getAbs(distanceActorLoc);
        distanceActorLoc.get(distanceActorPos);
        actorTo.pos.getAbs(distanceTargetPos);
        distanceRetVal = distanceActorPos.distance(distanceTargetPos);
        return distanceRetVal;
    }

    private static float angleBetween(Actor actorFrom, Actor actorTo)
    {
        float angleRetVal = 180.1F;
        if(!(Actor.isValid(actorFrom)) || !(Actor.isValid(actorTo))) return angleRetVal;
        double angleDoubleTemp = 0.0D;
        Loc angleActorLoc = new Loc();
        Point3d angleActorPos = new Point3d();
        Point3d angleTargetPos = new Point3d();
        Vector3d angleTargRayDir = new Vector3d();
        Vector3d angleNoseDir = new Vector3d();
        actorFrom.pos.getAbs(angleActorLoc);
        angleActorLoc.get(angleActorPos);
        actorTo.pos.getAbs(angleTargetPos);
        angleTargRayDir.sub(angleTargetPos, angleActorPos);
        angleDoubleTemp = angleTargRayDir.length();
        angleTargRayDir.scale(1.0D / angleDoubleTemp);
        angleNoseDir.set(1.0D, 0.0D, 0.0D);
        angleActorLoc.transform(angleNoseDir);
        angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
        angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
        return angleRetVal;
    }

    private static float angle360Between(Actor actorFrom, Actor actorTo)
    {
        float angleRetVal = 360.1F;
        if(!(Actor.isValid(actorFrom)) || !(Actor.isValid(actorTo))) return angleRetVal;
        double angleDoubleTemp = 0.0D;

        double yaw = -actorFrom.pos.getAbsOrient().getYaw() + 90D;
        if(yaw < 0D) yaw += 360D;
        double x = actorTo.pos.getAbsPoint().x - actorFrom.pos.getAbsPoint().x;
        double y = actorTo.pos.getAbsPoint().y - actorFrom.pos.getAbsPoint().y;

        double deg = Math.toDegrees(Math.atan2(y, -x)) - 90D;
        if(deg < 0D) deg += 360D;
        double degNew = deg - yaw;
        if(degNew < 0D) degNew += 360D;

        angleRetVal = (float)degNew;
        return angleRetVal;
    }

    public void update(float f)
    {
        super.update(f);
        moveRotodome(f);
        if(!(FM.AS.isMaster() && Config.isUSE_RENDER()))
        {
            return;
        }
        for(int engineIndex = 0; engineIndex < FM.EI.engines.length; engineIndex++)
        {
            if((FM.EI.engines[engineIndex].getPowerOutput() > 0.8F) && (FM.EI.engines[engineIndex].getStage() == 6))
            {
                if(FM.EI.engines[engineIndex].getPowerOutput() > 0.95F)
                {
                    FM.AS.setSootState(this, engineIndex, 3);
                }
                else
                {
                    FM.AS.setSootState(this, engineIndex, 2);
                }
            }
            else
            {
                FM.AS.setSootState(this, engineIndex, 0);
            }
        }
    }

    protected void moveRotodome(float f)
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.EI.engines[1].getStage() < 6 && FM.EI.engines[2].getStage() < 6 && FM.EI.engines[3].getStage() < 6)
        {
            rotoMode = 0;  // Stop
            rotodegpm = 0;
            radarDisplayRange = 0;
            radarDisplayVrt = 0;
            radarDisplayHol = 0;
            return;
        }
        else if(FM.getAltitude() < 950F)
        {
            rotoMode = 1;  // Slow rotate / Not Radar working
            rotodegpm = 90;  // 1/4 rpm
        }
        else if(FM.getAltitude() > 1000F)
        {
            rotoMode = 2;  // Fast rotate / Radar working
            rotodegpm = 2160;  // 6 rpm
        }
        rotoOrient -= f * (float)rotodegpm / 60F;
        if(rotoOrient < -360F) rotoOrient += 360F;
        hierMesh().chunkSetAngles("RADAR_D0", rotoOrient, 0.0F, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        if(rareTimer != Time.current())
        {
            if(rotoMode == 2 && Time.current() > awacsTimer + 10000L)
            {
                AWACS();
                awacsTimer = Time.current();
                awacsCounter++;
            }
        }
        super.rareAction(f, flag);
        rareTimer = Time.current();
    }

    private void AWACS()
    {
        enemyPlaneList.clear();
        enemyCruiseMissileList.clear();
        friendlyPlaneList.clear();
        friendlyAwacsList.clear();

        List list = Engine.targets();
        Actor actor = null;
        double altitudeAbs = 0D;
        double altitudeTrue = 0D;
        boolean playerInAwacsSight = false;
        Point3d point3dtemp = new Point3d();

        for(int i = 0; i < list.size(); i++)
        {
            actor = (Actor)list.get(i);

            if(actor == this)
                continue;
            if(!Actor.isValid(actor))
                continue;
            if(!(actor instanceof Aircraft) && !(actor instanceof MissileInterceptable))
                continue;
            if(distanceBetween(this, actor) > maxRange)
                continue;

            altitudeAbs = actor.pos.getAbsPoint().z - Engine.land().HQ_Air(actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y);
            altitudeTrue = actor.pos.getAbsPoint().z;

            if(altitudeAbs < minAltitude)
                continue;
            if(altitudeAbs < thresholdLA && distanceBetween(this, actor) > maxRangeLA)
                continue;
            if(altitudeAbs < thresholdAbsLA && altitudeTrue > 120D && distanceBetween(this, actor) > maxRangeAbsLA)
                continue;  // Low altitude on ground is harder to search than on sea.
            if(Landscape.rayHitHQ(this.pos.getAbsPoint(), actor.pos.getAbsPoint(), point3dtemp))
                continue;  // "actor" is behind of terrain and cannot detect from AWACS.
            if(Mission.curCloudsType() > 4 && (Mission.curCloudsHeight() - 20D > this.pos.getAbsPoint().z || Mission.curCloudsHeight() - 30D > actor.pos.getAbsPoint().z) && Main.cur().clouds != null)
                if(distanceBetween(this, actor) > maxRangeLA * Main.cur().clouds.getVisibility(this.pos.getAbsPoint(), actor.pos.getAbsPoint()) * TrueRandom.nextFloat(0.7F, 1.2F) * (float)(7 - Mission.curCloudsType()))
                    continue;   // Thunderclouds disturb Radar detecting. (No Rain clouds don't do.)

            if(actor.getArmy() != this.getArmy())
            {
                if(actor instanceof Aircraft)
                {
                    enemyPlaneList.add(actor);
                    if(actor instanceof TypeRadarWarningReceiver)
                        ((TypeRadarWarningReceiver)actor).myRadarSearchYou(this, sr_soundpreset);
                }
                else if(actor instanceof MissileInterceptable && ((MissileInterceptable)actor).isReleased())
                    enemyCruiseMissileList.add(actor);
            }
            else
            {
                if(actor instanceof Aircraft)
                {
                    if(actor instanceof E_3A)
                        friendlyAwacsList.add(actor);
                    else
                    {
                        friendlyPlaneList.add(actor);
                        if(actor == World.getPlayerAircraft())
                            playerInAwacsSight = true;
                    }
                }
            }
        }

        if(this == World.getPlayerAircraft() && awacsCounter % 5 == 0)
            AWACSmessageOwn();
        else if(playerInAwacsSight && enemyPlaneList.size() > 0 && awacsCounter % 5 == 0)
            AWACSmessageSend();
        return;
    }

    private void AWACSmessageOwn()
    {
        if(enemyPlaneList.size() + enemyCruiseMissileList.size() + friendlyPlaneList.size() + friendlyAwacsList.size() == 0)
            HUD.logCenter("                                          No Radar detect");
        else
            HUD.logCenter("                     " + enemyPlaneList.size() + " enemy planes detected, " + enemyCruiseMissileList.size() + " enemy missiles detected / " + friendlyPlaneList.size() + " friendly aircrafts");
    }

    private void AWACSmessageSend()
    {
        ArrayList enemies = new ArrayList();
        enemies.clear();
        EnemyRadarData tempERD = null;
        Vector3d vtemp = new Vector3d();
        int drawspeed = Main3D.cur3D().hud.drawSpeed();

        for(int i = 0; i < enemyPlaneList.size(); i++)
        {
            tempERD = new EnemyRadarData();
            tempERD.actor = (Actor)enemyPlaneList.get(i);
            tempERD.distance = distanceBetween(tempERD.actor, World.getPlayerAircraft());
            tempERD.actor.pos.speed(vtemp);
            tempERD.speed = vtemp.length();
            tempERD.dirdiff = angleBetween(tempERD.actor, World.getPlayerAircraft());
            enemies.add(tempERD);
        }
        Collections.sort(enemies);

        int iBear = (int)((angle360Between(World.getPlayerAircraft(), ((EnemyRadarData)(enemies.get(0))).actor) + 5.5F) * 0.1F) * 10;
        int iHead = (int)(-((double)(((EnemyRadarData)(enemies.get(0))).actor).pos.getAbsOrient().getYaw() - 90D - 5.5D) * 0.1F) * 10;
        if(iHead < 0) iHead += 360;
        switch(drawspeed)
        {
        default:
        case 1:
        case 4:
            HUD.logCenter("                     Target bearing " + iBear + "\260" + ", range " + (int)(((EnemyRadarData)(enemies.get(0))).distance * 0.001D) + "km, height " + (int)(((EnemyRadarData)(enemies.get(0))).actor.pos.getAbsPoint().z * 0.1D) * 10 + "m, heading " + iHead + "\260" + ", Spd " + (int)(((EnemyRadarData)(enemies.get(0))).speed * 0.36D + 0.5D) * 10 + "km/h");
            break;
        case 2:
        case 5:
            HUD.logCenter("                     Target bearing " + iBear + "\260" + ", range " + (int)(((EnemyRadarData)(enemies.get(0))).distance * 0.0005399568D) + "nm, height " + (int)(((EnemyRadarData)(enemies.get(0))).actor.pos.getAbsPoint().z * 0.32808D) * 10 + "ft, heading " + iHead + "\260" + ", Spd " + (int)(((EnemyRadarData)(enemies.get(0))).speed * 0.1943845D + 0.5D) * 10 + "kt");
            break;
        case 3:
        case 6:
            HUD.logCenter("                     Target bearing " + iBear + "\260" + ", range " + (int)(((EnemyRadarData)(enemies.get(0))).distance * 0.000621371D) + "mile, height " + (int)(((EnemyRadarData)(enemies.get(0))).actor.pos.getAbsPoint().z * 0.32808D) * 10 + "ft, heading " + iHead + "\260" + ", Spd " + (int)(((EnemyRadarData)(enemies.get(0))).speed * 0.2236936D + 0.5D) * 10 + "mph");
            break;
        }

/*            if(flag)
            {
                if(i2 > 4)
                    HUD.logCenter("                                          Target bearing " + k1 + "\260" + ", range " + i2 + "km, height " + l + "m, heading " + l1 + "\260");
                else
                    HUD.logCenter(" ");
            } else
            {
                HUD.logCenter("                                                                             Target at " + s + "-" + j1 + ", height " + l + "m, heading " + l1 + "\260");
            }
        } */
    }

    public void typeRadarGainMinus()
    {
    }

    public void typeRadarGainPlus()
    {
    }

    public void typeRadarRangeMinus()
    {
        if(radarDisplayRange > 1)
            return;

        radarDisplayRange++;

        if(radarDisplayRange == 1)
        {
            radarDisplayVrt = 0;
            radarDisplayHol = 0;
        }
        else if(radarDisplayRange == 2)
        {
            if(radarDisplayVrt == 0)
                radarDisplayVrt = 1;
            else if(radarDisplayVrt == 1)
                radarDisplayVrt = 2;
            if(radarDisplayHol == 0)
                radarDisplayHol = 1;
            else if(radarDisplayHol == 1)
                radarDisplayHol = 2;
        }

        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar dislay range " + radarDisplayRange);
    }

    public void typeRadarRangePlus()
    {
        if(radarDisplayRange < 1)
            return;

        radarDisplayRange--;

        if(radarDisplayRange == 0)
        {
            radarDisplayVrt = 0;
            radarDisplayHol = 0;
        }
        else if(radarDisplayRange == 1)
        {
            if(radarDisplayVrt < 2)
                radarDisplayVrt = 0;
            else
                radarDisplayVrt = 1;
            if(radarDisplayHol < 2)
                radarDisplayHol = 0;
            else
                radarDisplayHol = 1;
        }

        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar dislay range " + radarDisplayRange);
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
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
        if(radarDisplayRange == 0) return;

        radarDisplayVrt--;
        if(radarDisplayVrt < 0)
            radarDisplayVrt = 0;
    }

    public void typeBomberAdjDistanceMinus()
    {
        if(radarDisplayRange == 0) return;

        int maxnum = 0;
        if(radarDisplayRange == 1) maxnum = 1;
        else if(radarDisplayRange == 2) maxnum = 2;

        radarDisplayVrt++;
        if(radarDisplayVrt > maxnum)
            radarDisplayVrt = maxnum;
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(radarDisplayRange == 0) return;

        int maxnum = 0;
        if(radarDisplayRange == 1) maxnum = 1;
        else if(radarDisplayRange == 2) maxnum = 2;

        radarDisplayHol++;
        if(radarDisplayHol > maxnum)
            radarDisplayHol = maxnum;
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(radarDisplayRange == 0) return;

        radarDisplayHol--;
        if(radarDisplayHol < 0)
            radarDisplayHol = 0;
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public boolean typeBomberToggleAutomation()
    {
        return true;
    }

    // Radar ArrayList "getter" interfaces For Radar display cockpit class
    public ArrayList getEnemyPlaneList()
    {
        return enemyPlaneList;
    }

    public ArrayList getEnemyCruiseMissileList()
    {
        return enemyCruiseMissileList;
    }

    public ArrayList getFriendlyPlaneList()
    {
        return friendlyPlaneList;
    }

    public ArrayList getFriendlyAwacsList()
    {
        return friendlyAwacsList;
    }

//    public static boolean   bChangedPit = false;
    private long rareTimer;
    public int rotoMode;
    private int rotodegpm;
    private float rotoOrient;
    public long awacsTimer;
    private int awacsCounter;

    private ArrayList enemyPlaneList;
    private ArrayList enemyCruiseMissileList;
    private ArrayList friendlyPlaneList;
    private ArrayList friendlyAwacsList;

    public int radarDisplayRange = 0;  // 0:Full display 550x550km, 1:2/3 display 400x400km, 2:1/3 display 200x200km
    public int radarDisplayVrt = 0;    // When radarDisplayRange == 1 or 2, move display area Vertical and Horizontal
    public int radarDisplayHol = 0;

    private static double maxRange = 550000D;   // Radar search range 550km
    private static double maxRangeLA = 320000D; // == 320km Radar search range for low altitude
    private static double thresholdLA = 1200D;  // under 1200m is treated as low altitude
    private static double maxRangeAbsLA = 180000D; // == 180km Radar search range for lower absolute altitude
    private static double thresholdAbsLA = 100D;  // Absolute Altitude under 100m on ground makes difficult to searched
    private static double minAltitude = 30D;  // cannot detect planes flying lower than 30m from terrain
    private static int maxDetectNum = 640;
    private static int maxTrackNum = 240;
    private static String sr_soundpreset = "aircraft.APR25AAA";   // Sound Preset string for enemy Gen-0 RWR sound play

    static {
        final Class class1 = E_3A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Sentry");
        Property.set(class1, "meshName", "3DO/Plane/707/hierE-3.him");
        Property.set(class1, "yearService", 1977F);
        Property.set(class1, "yearExpired", 2050F);
        Property.set(class1, "FlightModel", "FlightModels/E-3A.fmd:C135FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitC_135.class , CockpitE_3RADAR.class
          });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] {});
        Aircraft.weaponHooksRegister(class1, new String[] {});
        try {
            final ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            final HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            final byte byte0 = 0;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "None";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int i = 0; i < byte0; i++) {
                a_lweaponslot[i] = null;
            }

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (final Exception exception) {
        }
    }
}
