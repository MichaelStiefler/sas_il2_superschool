
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;
import java.util.*;
import com.maddox.sas1946.il2.util.Reflection;

// Referenced classes of package com.maddox.il2.objects.air:
//            F_4, Aircraft, TypeFighterAceMaker, TypeRadarGunsight,
//            PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure,
//            TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump,
//            TypeFastJet, NetAircraft

public class F_14A extends F_14
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public F_14A()
    {
        guidedMissileUtils = null;
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
        guidedMissileUtils = new GuidedMissileUtils(this);
        engineSFX = null;
        engineSTimer = 0x98967f;
        outCommand = new NetMsgFiltered();
        tX4Prev = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        counter = 0;
        freq = 800;
        Timer1 = Timer2 = freq;
        error = 0;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
    }

    private void checkAmmo()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                            counterFlareList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                            counterChaffList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16)
                            super.bHasPaveway = true;
                    }
            }
    }

    public void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        {
            if(Time.current() > lastFlareDeployed + 1200L)
            {
                ((RocketGunFlare_gn16)counterFlareList.get(0)).shots(1);
                hasFlare = true;
                lastFlareDeployed = Time.current();
                if(!((RocketGunFlare_gn16)counterFlareList.get(0)).haveBullets())
                    counterFlareList.remove(0);
            }
        }
        if(counterChaffList.isEmpty())
            hasChaff = false;
        else
        {
            if(Time.current() > lastChaffDeployed + 1500L)
            {
                ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
                hasChaff = true;
                lastChaffDeployed = Time.current();
                if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                    counterChaffList.remove(0);
            }
        }
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


    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        checkAmmo();
        guidedMissileUtils.onAircraftLoaded();
        if(thisWeaponsName.endsWith("IRIAF"))
        {
            hierMesh().chunkVisible("ANAWG", false);
            hierMesh().chunkVisible("ANAWG-IRIAF", true);
        }
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        this.computeTF30_AB();
        super.update(f);
        if(super.backfire)
            backFire();
    }

    public void rareAction(float f, boolean flag)
    {
        if(raretimer != Time.current() && this == World.getPlayerAircraft())
        {
            counter++;
            if(counter % 6 == 0)
                AWG9();
            if(counter % 12 == 3)
                IRST();
            if(counter % 12 == 9)
                InertialNavigation();
        }
        super.rareAction(f, flag);
        raretimer = Time.current();
    }

    private boolean AWG9()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag = false;
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        double d3 = d2 - (double)Landscape.Hmin((float)((Actor) (aircraft)).pos.getAbsPoint().x, (float)((Actor) (aircraft)).pos.getAbsPoint().y);
        if(aircraft instanceof F_14A)
            flag1 = true;
        if(d3 < 0.0D)
            d3 = 0.0D;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(flag1 && (actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
            {
                super.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                String s = "level with us";
                if(d2 - d6 - 300D >= 0.0D)
                    s = "below us";
                if((d2 - d6) + 300D <= 0.0D)
                    s = "above us";
                if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                    s = "slightly below";
                if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                    s = "slightly above";
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = 57.32484F * (float)Math.atan2(d9, -d8);
                int k = (int)(Math.floor((int)f) - 90D);
                if(k < 0)
                    k += 360;
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f1 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int i1 = random.nextInt(6) - 3;
                float f2 = 200000F;
                float f3 = f2;
                if(d3 < 15D)
                    f3 = (float)(d3 * 0.80000001192092896D * 3D);
                int j1 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f1) / 10D) * 10D);
                if((float)j1 > f2)
                    j1 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                float f4 = 57.32484F * (float)Math.atan2(j1, d7);
                int k1 = (int)(Math.floor((int)f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int i2 = (int)f2;
                if((float)j1 < f2)
                    if(j1 > 1150)
                        i2 = (int)(Math.ceil((double)j1 / 900D) * 900D);
                    else
                        i2 = (int)(Math.ceil((double)j1 / 500D) * 500D);
                int j2 = l + i1;
                int k2 = j2;
                if(k2 < 0)
                    k2 += 360;
                float f5 = (float)((double)f3 + Math.sin(Math.toRadians(Math.sqrt(l * l) * 3D)) * ((double)f3 * 0.25D));
                int l2 = (int)((double)f5 * Math.cos(Math.toRadians(l1)));
                String s1 = "  ";
                if(k2 < 5)
                    s1 = "Dead ahead, ";
                if(k2 >= 5 && (double)k2 <= 7.5D)
                    s1 = "Right 5, ";
                if((double)k2 > 7.5D && (double)k2 <= 12.5D)
                    s1 = "Right 10, ";
                if((double)k2 > 12.5D && (double)k2 <= 17.5D)
                    s1 = "Right 15, ";
                if((double)k2 > 17.5D && k2 <= 25)
                    s1 = "Right 20, ";
                if(k2 > 25 && k2 <= 35)
                    s1 = "Right 30, ";
                if(k2 > 35 && k2 <= 45)
                    s1 = "Right 40, ";
                if(k2 > 45 && k2 <= 60)
                    s1 = "Turn right, ";
                if(k2 > 355)
                    s1 = "Dead ahead, ";
                if(k2 <= 355 && (double)k2 >= 352.5D)
                    s1 = "Left 5, ";
                if((double)k2 < 352.5D && (double)k2 >= 347.5D)
                    s1 = "Left 10, ";
                if((double)k2 < 347.5D && (double)k2 >= 342.5D)
                    s1 = "Left 15, ";
                if((double)k2 < 342.5D && k2 >= 335)
                    s1 = "Left 20, ";
                if(k2 < 335 && k2 >= 325)
                    s1 = "Left 30, ";
                if(k2 < 325 && k2 >= 315)
                    s1 = "Left 40, ";
                if(k2 < 345 && k2 >= 300)
                    s1 = "Turn left, ";
                String s2 = "  ";
                if(l1 < -10)
                    s2 = "nose down";
                if(l1 >= -10 && l1 <= -5)
                    s2 = "down a bit";
                if(l1 > -5 && l1 < 5)
                    s2 = "level";
                if(l1 <= 10 && l1 >= 5)
                    s2 = "up a bit";
                if(l1 > 10)
                    s2 = "pull up";
                String s3 = "  ";
                if(k2 < 5)
                    s3 = "dead ahead, ";
                if(k2 >= 5 && (double)k2 <= 7.5D)
                    s3 = "right by 5\260, ";
                if((double)k2 > 7.5D && (double)k2 <= 12.5D)
                    s3 = "right by 10\260, ";
                if((double)k2 > 12.5D && (double)k2 <= 17.5D)
                    s3 = "right by 15\260, ";
                if((double)k2 > 17.5D && k2 <= 25)
                    s3 = "right by 20\260, ";
                if(k2 > 25 && k2 <= 35)
                    s3 = "right by 30\260, ";
                if(k2 > 35 && k2 <= 45)
                    s3 = "right by 40\260, ";
                if(k2 > 45 && k2 <= 60)
                    s3 = "off our right, ";
                if(k2 > 355)
                    s3 = "dead ahead, ";
                if(k2 <= 355 && (double)k2 >= 352.5D)
                    s3 = "left by 5\260, ";
                if((double)k2 < 352.5D && (double)k2 >= 347.5D)
                    s3 = "left by 10\260, ";
                if((double)k2 < 347.5D && (double)k2 >= 342.5D)
                    s3 = "left by 15\260, ";
                if((double)k2 < 342.5D && k2 >= 335)
                    s3 = "left by 20\260, ";
                if(k2 < 335 && k2 >= 325)
                    s3 = "left by 30\260, ";
                if(k2 < 325 && k2 >= 315)
                    s3 = "left by 40\260, ";
                if(k2 < 345 && k2 >= 300)
                    s3 = "off our left, ";
                if((double)j1 <= (double)l2 && (double)j1 > 100D && l1 >= -20 && l1 <= 20 && Math.sqrt(j2 * j2) <= 60D)
                {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "WSO: Contact " + s3 + s + ", " + i2 + "m");
                    freq = 1;
                } else
                {
                    freq = 1;
                }
                setTimer(freq);
            }
        }

        return true;
    }


    private boolean IRST()
    {
        if(thisWeaponsName.endsWith("IRIAF"))
            return false;

        // F-14A has only IR sensor, no function to determine aircraft class or distance.
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag = false;
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        double d3 = d2 - (double)Landscape.Hmin((float)((Actor) (aircraft)).pos.getAbsPoint().x, (float)((Actor) (aircraft)).pos.getAbsPoint().y);
        if(aircraft instanceof F_14A)
            flag1 = true;
        if(d3 < 0.0D)
            d3 = 0.0D;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(flag1 && (actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D
               && !(Main.cur().clouds != null && Main.cur().clouds.getVisibility(actor.pos.getAbsPoint(), aircraft.pos.getAbsPoint()) < 1.0F))
            {
                super.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                String s = "level with us";
                if(d2 - d6 - 300D >= 0.0D)
                    s = "below us";
                if((d2 - d6) + 300D <= 0.0D)
                    s = "above us";
                if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                    s = "slightly below";
                if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                    s = "slightly above";
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = 57.32484F * (float)Math.atan2(d9, -d8);
                int k = (int)(Math.floor((int)f) - 90D);
                if(k < 0)
                    k += 360;
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f1 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int i1 = random.nextInt(6) - 3;
                float f2 = 100000F;
                float f3 = f2;
                if(d3 < 15D)
                    f3 = (float)(d3 * 0.80000001192092896D * 3D);
                int j1 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f1) / 10D) * 10D);
                if((float)j1 > f2)
                    j1 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                float f4 = 57.32484F * (float)Math.atan2(j1, d7);
                int k1 = (int)(Math.floor((int)f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int i2 = (int)f2;
                if((float)j1 < f2)
                    if(j1 > 1150)
                        i2 = (int)(Math.ceil((double)j1 / 900D) * 900D);
                    else
                        i2 = (int)(Math.ceil((double)j1 / 500D) * 500D);
                int j2 = l + i1;
                int k2 = j2;
                if(k2 < 0)
                    k2 += 360;
                float f5 = (float)((double)f3 + Math.sin(Math.toRadians(Math.sqrt(l * l) * 3D)) * ((double)f3 * 0.25D));
                int l2 = (int)((double)f5 * Math.cos(Math.toRadians(l1)));
                String s1 = "  ";
                if(k2 < 5)
                    s1 = "Dead ahead, ";
                if(k2 >= 5 && (double)k2 <= 7.5D)
                    s1 = "Right 5, ";
                if((double)k2 > 7.5D && (double)k2 <= 12.5D)
                    s1 = "Right 10, ";
                if((double)k2 > 12.5D && (double)k2 <= 17.5D)
                    s1 = "Right 15, ";
                if((double)k2 > 17.5D && k2 <= 25)
                    s1 = "Right 20, ";
                if(k2 > 25 && k2 <= 35)
                    s1 = "Right 30, ";
                if(k2 > 35 && k2 <= 45)
                    s1 = "Right 40, ";
                if(k2 > 45 && k2 <= 60)
                    s1 = "Turn right, ";
                if(k2 > 355)
                    s1 = "Dead ahead, ";
                if(k2 <= 355 && (double)k2 >= 352.5D)
                    s1 = "Left 5, ";
                if((double)k2 < 352.5D && (double)k2 >= 347.5D)
                    s1 = "Left 10, ";
                if((double)k2 < 347.5D && (double)k2 >= 342.5D)
                    s1 = "Left 15, ";
                if((double)k2 < 342.5D && k2 >= 335)
                    s1 = "Left 20, ";
                if(k2 < 335 && k2 >= 325)
                    s1 = "Left 30, ";
                if(k2 < 325 && k2 >= 315)
                    s1 = "Left 40, ";
                if(k2 < 345 && k2 >= 300)
                    s1 = "Turn left, ";
                String s2 = "  ";
                if(l1 < -10)
                    s2 = "nose down";
                if(l1 >= -10 && l1 <= -5)
                    s2 = "down a bit";
                if(l1 > -5 && l1 < 5)
                    s2 = "level";
                if(l1 <= 10 && l1 >= 5)
                    s2 = "up a bit";
                if(l1 > 10)
                    s2 = "pull up";
                String s3 = "  ";
                if(k2 < 5)
                    s3 = "dead ahead, ";
                if(k2 >= 5 && (double)k2 <= 7.5D)
                    s3 = "right by 5\260, ";
                if((double)k2 > 7.5D && (double)k2 <= 12.5D)
                    s3 = "right by 10\260, ";
                if((double)k2 > 12.5D && (double)k2 <= 17.5D)
                    s3 = "right by 15\260, ";
                if((double)k2 > 17.5D && k2 <= 25)
                    s3 = "right by 20\260, ";
                if(k2 > 25 && k2 <= 35)
                    s3 = "right by 30\260, ";
                if(k2 > 35 && k2 <= 45)
                    s3 = "right by 40\260, ";
                if(k2 > 45 && k2 <= 60)
                    s3 = "off our right, ";
                if(k2 > 355)
                    s3 = "dead ahead, ";
                if(k2 <= 355 && (double)k2 >= 352.5D)
                    s3 = "left by 5\260, ";
                if((double)k2 < 352.5D && (double)k2 >= 347.5D)
                    s3 = "left by 10\260, ";
                if((double)k2 < 347.5D && (double)k2 >= 342.5D)
                    s3 = "left by 15\260, ";
                if((double)k2 < 342.5D && k2 >= 335)
                    s3 = "left by 20\260, ";
                if(k2 < 335 && k2 >= 325)
                    s3 = "left by 30\260, ";
                if(k2 < 325 && k2 >= 315)
                    s3 = "left by 40\260, ";
                if(k2 < 345 && k2 >= 300)
                    s3 = "off our left, ";
                double IRstrength = 0D;
                EnginesInterface ei = ((Aircraft)actor).FM.EI;
                for(int en = 0; en < ei.engines.length; en++)
                {
                    float add = 0;
                    if(ei.engines[en].getType() == 2 || ei.engines[en].getType() == 3)
                    {   // get IR strength from Jet exhaust from thrust and throttle values
                        add = ei.engines[en].thrustMax;
                        if(ei.engines[en].getPowerOutput() > 1.0F)
                            add *= (ei.engines[en].getPowerOutput() * 1.22F);
                        else
                            add *= (ei.engines[en].getPowerOutput() * 0.8F + 0.2F);
                    }
                    else
                        add = Reflection.getFloat(ei.engines[en], "horsePowers") * (ei.engines[en].getPowerOutput() * 0.9F + 0.1F) * 0.36F;

                    IRstrength += (double)add;
                }
                if(ei.engines.length > 4)
                    IRstrength *= (2.0D / (double)ei.engines.length);
                else
                    IRstrength /= Math.sqrt((double)ei.engines.length);
                IRstrength *= 1.5D;   // approximation distance in meters

                if((double)j1 <= (double)l2 && l1 >= -20 && l1 <= 20 && Math.sqrt(j2 * j2) <= 60D
                   && (double)j1 < IRstrength)
                {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "IR Tracking! " + s3 + s);
                    freq = 1;
                    setTimer(freq);
                    return true;
                }
            }
        }
        return true;
    }

    private boolean InertialNavigation()
    {
            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            if(aircraft.getSpeed(vector3d) > 20D && ((Actor) (aircraft)).pos.getAbsPoint().z >= 150D && (aircraft instanceof F_14A))
            {
                pos.getAbs(point3d);
                if(Mission.cur() != null)
                {
                    error++;
                    if(error > 99)
                        error = 1;
                }
                int i = error;
                int j = i;
                Random random = new Random();
                int k = random.nextInt(100);
                if(k > 50)
                    i -= i * 2;
                k = random.nextInt(100);
                if(k > 50)
                    j -= j * 2;
                double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d1 = ((Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft)).pos.getAbsPoint().x) / 1000D) / 10D;
                double d2 = ((Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().y) / 1000D) / 10D;
                char c = (char)(int)(65D + Math.floor((d1 / 676D - Math.floor(d1 / 676D)) * 26D));
                char c1 = (char)(int)(65D + Math.floor((d1 / 26D - Math.floor(d1 / 26D)) * 26D));
                String s = "";
                if(d > 260D)
                    s = "" + c + c1;
                else
                    s = "" + c1;
                int l = (int)Math.ceil(d2);
                HUD.log(AircraftHotKeys.hudLogWeaponId, "INS: " + s + "-" + l);
            }
        return true;
    }

    public void setTimer(int i)
    {
        Random random = new Random();
        Timer1 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
        Timer2 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
    }

    public void resetTimer(float f)
    {
        Timer1 = f;
        Timer2 = f;
    }

    public void computeTF30_AB()
    {
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5)
            FM.producedAF.x += 33200D;
        if(FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() > 5)
            FM.producedAF.x += 33200D;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5 && calculateMach() > 1.3F)
            FM.producedAF.x -= 20000D;
        if(FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() > 5 && calculateMach() > 1.3F)
            FM.producedAF.x -= 20000D;
        float x = FM.getAltitude() / 1000F;
        float thrustDegradation = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6
          && FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() == 6)
            if (x > 19F)
            {
                thrustDegradation = 20.0F;
            }
            else
            {
                float x2 = x * x;
                float x3 = x2 * x;
                float x4 = x3 * x;
                float x5 = x4 * x;
                float x6 = x5 * x;
                float x7 = x6 * x;
                thrustDegradation = -0.000281394F*x6 + 0.0135257F*x5 - 0.227697F*x4 + 1.62839F*x3 - 5.10746F*x2 + 6.31553F*x;
        //{{0,0},{2,2},{5,3},{7,0},{10,-25},{15,-20},{18.5,37}}
        //thrustDegradation = (3401F*x4/720720F) - (44399F*x3/360360F) + (633091F*x2/720720F) - (829459F*x/360360F);
        //{{0,0},{2,-2},{5,-2},{7,-4},{18,20}}
            }
        FM.producedAF.x -= thrustDegradation * 1000F;
    }


    private GuidedMissileUtils guidedMissileUtils;
    private boolean bRadarWarning;
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
    public boolean bToFire;
    protected SoundFX engineSFX;
    protected int engineSTimer;
    private NetMsgFiltered outCommand;
    private long tX4Prev;
    private float deltaAzimuth;
    private float deltaTangage;
    public float Timer1;
    public float Timer2;
    private int freq;
    private int counter;
    private int error;
    private long raretimer;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;

    static
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-14A");
        Property.set(class1, "meshName", "3DO/Plane/F-14/F14A.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/F-14A.fmd:F14_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF_14.class, com.maddox.il2.objects.air.CockpitF_14FLIR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 9, 9, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 3, 3, 3, 3, 9, 9, 7, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
           "_CANNON01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_Rock05", "_Rock06", "_Rock07", "_Rock08","_ExternalRock09",
           "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev01", "_ExternalDev02","_ExternalRock17",
           "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27",
           "_ExternalRock28", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev03", "_ExternalDev04", "_ExternalRock29", "_ExternalRock30"

        });
        String s = "";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            char c = '\50';
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[c];
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM9D";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7E";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7E+4xAIM9D";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M+4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM9D+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7E+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7E+4xAIM9D+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M+4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+2xAIM7M+2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+2xAIM7M+2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM9L_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M+4xAIM9L_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM9L+2xDt_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M+2xDt_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M+4xAIM9L+2xDt_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+4xAIM9L_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+2xAIM7M+2xAIM9L_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+4xAIM9L+2xDt_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+2xAIM7M+2xAIM9L+2xDt_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+4xAIM9L_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+2xAIM7M+2xAIM9L_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+4xAIM9L+2xDt_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+2xAIM7M+2xAIM9L+2xDt_IRIAF";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_6xAIM54A";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_6xAIM54A+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54C";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54C+4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54C+2xAIM7M+2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54C+4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54C+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54C";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54C+4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54C+2xAIM7M+2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54C+4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54C+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_6xAIM54C";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_6xAIM54C+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2xMk83+3xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2xMk83Ballute+3xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2xMk84+3xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2xMk84Ballute+3xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2xCBU100+3xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4xMk83+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4xMk83Ballute+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4xMk84+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4xMk84Ballute+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4xCBU100+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU12+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU16+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU10+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU12+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU16+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU10+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU12+1xAIM54C+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU16+1xAIM54C+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU10+1xAIM54C+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_2xAIM7M+4xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_2xAIM54A+4xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_2xAIM54A+2xAIM7M+2xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_2xAIM54C+4xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_2xAIM54C+2xAIM7M+2xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
         catch(Exception exception) {
            System.out.println("Weapon register error - F_14A : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}