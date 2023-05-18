// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 18/06/2015 18:40:43
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   F_16.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.F_16A_B10;
import com.maddox.il2.objects.air.F_16B_B05;
import com.maddox.il2.objects.air.F_16I;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, Aircraft, TypeFighterAceMaker, TypeSupersonic,
//            TypeFastJet, F_18, TypeLaserSpotter, TypeFighter,
//            TypeGSuit, TypeRadar, TypeBomber, TypeX4Carrier,
//            TypeStormovikArmored, TypeSemiRadar, TypeGroundRadar, Cockpit,
//            PaintScheme, EjectionSeat

public class F_16 extends Scheme1
    implements TypeSupersonic, TypeFighter, TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeRadar, TypeBomber, TypeX4Carrier, TypeLaserSpotter, TypeStormovikArmored, TypeSemiRadar, TypeGroundRadar
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

    public F_16()
    {
        elevatorsField = null;
        tflap = 0L;
        bForceFlapmodeAuto = false;
        lLightHook = new Hook[4];
        SonicBoom = 0.0F;
        bSlatsOff = false;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 2;
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
        gearCurrentSteer = 0F;
        hasHydraulicPressure = true;
        radarmode = 0;
        targetnum = 0;
        lockrange = 0.04F;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        clipBoardPage_ = 1;
        showClipBoard_ = false;
        missilesList = new ArrayList();
        lockmode = 0;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        radargunsight = 0;
        leftscreen = 0;
        Bingofuel = 1000;
        radarrange = 1;
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        hold = false;
        t1 = 0L;
        tangate = 0.0F;
        azimult = 0.0F;
        tf = 0L;
        APmode1 = false;
        radartogle = false;
        v = 0.0F;
        h = 0.0F;
        lockmode = 0;
        radargunsight = 0;
        leftscreen = 2;
        Bingofuel = 1000;
        radarrange = 1;
        Nvision = false;
        fxRWR = newSound("aircraft.RWR2", false);
        fxMissileWarning = newSound("aircraft.MissileMissile", false);
        misslebrg = 0.0F;
        aircraftbrg = 0.0F;
        FL = false;
        thrustMaxField = new Field[2];
        lTimeNextEject = 0L;
    }

    public float checkfuel(int i)
    {
        FuelTank afueltank[] = FM.CT.getFuelTanks();
        if(afueltank.length == 0)
            return 0.0F;
        for(i = 0; i < afueltank.length; i++)
            Fuelamount = afueltank[i].Fuel;

        return Fuelamount;
    }

    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!radartogle)
            {
                radartogle = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar ON");
                radarmode = 0;
            } else
            {
                radartogle = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar OFF");
            }
        if(i == 21)
            if(!Nvision)
            {
                Nvision = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Nvision ON");
            } else
            {
                Nvision = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Nvision OFF");
            }
        if(i == 22)
        {
            lockmode++;
            if(lockmode > 1)
                lockmode = 0;
        }
        if(i == 23)
        {
            radargunsight++;
            if(radargunsight > 3)
                radargunsight = 0;
            if(radargunsight == 0)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: funnel");
            if(radargunsight == 1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Radar ranging");
            if(radargunsight == 2)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Unguided Rocket");
            if(radargunsight == 3)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Ground");
        }
        if(i == 24)
        {
            leftscreen++;
            if(leftscreen > 2)
                leftscreen = 0;
            if(leftscreen == 0)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Fuel");
            else
            if(leftscreen == 1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: FPAS");
            else
            if(leftscreen == 2)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Engine");
        }
        if(i == 25)
        {
            Bingofuel += 500;
            if(Bingofuel > 6000)
                Bingofuel = 1000;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Bingofuel  " + Bingofuel);
        }
        if(i == 26)
        {
            if(hold && t1 + 200L < Time.current() && FLIR)
            {
                hold = false;
                HUD.log("Lazer Unlock");
                t1 = Time.current();
            }
            if(!hold && t1 + 200L < Time.current() && FLIR)
            {
                hold = true;
                HUD.log("Lazer Lock");
                t1 = Time.current();
            }
        }
        if(i == 27)
            if(!ILS)
            {
                ILS = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS ON");
            } else
            {
                ILS = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS OFF");
            }
        if(i == 28)
            if(!FL)
            {
                FL = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "FL ON");
            } else
            {
                FL = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "FL OFF");
            }
    }

    private boolean RWRWarning()
    {
        boolean flag = false;
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        if(aircraft != null)
        {
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i += 360;
            int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j += 360;
            Aircraft aircraft1 = War.getNearestEnemy(aircraft, 6000F);
            boolean flag1;
            if((aircraft1 instanceof Aircraft) && aircraft.getArmy() != World.getPlayerArmy() && (aircraft instanceof TypeFighterAceMaker) && ((aircraft instanceof TypeSupersonic) || (aircraft instanceof TypeFastJet)) && aircraft1 == World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
            {
                super.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft1)).pos.getAbsPoint().x;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft1)).pos.getAbsPoint().y;
                double d8 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft1)).pos.getAbsPoint().z;
                new String();
                new String();
                int k = (int)(Math.floor(((Actor) (aircraft1)).pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
                int i1 = (int)(Math.floor((aircraft1.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                double d10 = (int)(Math.ceil((d2 - d8) / 10D) * 10D);
                boolean flag2 = false;
                Engine.land();
                int k1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(((Actor) (aircraft1)).pos.getAbsPoint().x), Engine.land().WORLD2PIXY(((Actor) (aircraft1)).pos.getAbsPoint().y));
                float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                if(k1 >= 28 && k1 < 32 && f < 7.5F)
                    flag2 = true;
                new String();
                double d14 = d4 - d;
                double d16 = d6 - d1;
                float f1 = 57.32484F * (float)Math.atan2(d16, -d14);
                int l1 = (int)(Math.floor((int)f1) - 90D);
                if(l1 < 0)
                    l1 += 360;
                int i2 = l1 - i;
                double d19 = d - d4;
                double d20 = d1 - d6;
                Random random = new Random();
                float f3 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int i3 = random.nextInt(6) - 3;
                float f4 = 19000F;
                float f5 = f4;
                if(d3 < 1200D)
                    f5 = (float)(d3 * 0.80000001192092896D * 3D);
                int j3 = (int)(Math.ceil(Math.sqrt((d20 * d20 + d19 * d19) * (double)f3) / 10D) * 10D);
                if((float)j3 > f4)
                    j3 = (int)(Math.ceil(Math.sqrt(d20 * d20 + d19 * d19) / 10D) * 10D);
                float f6 = 57.32484F * (float)Math.atan2(j3, d10);
                int k3 = (int)(Math.floor((int)f6) - 90D);
                int l3 = (k3 - (90 - j)) + i3;
                int i4 = (int)f4;
                if((float)j3 < f4)
                    if(j3 > 1150)
                        i4 = (int)(Math.ceil((double)j3 / 900D) * 900D);
                    else
                        i4 = (int)(Math.ceil((double)j3 / 500D) * 500D);
                int j4 = i2 + i3;
                int k4 = j4;
                if(k4 < 0)
                    k4 += 360;
                float f7 = (float)((double)f5 + Math.sin(Math.toRadians(Math.sqrt(i2 * i2) * 3D)) * ((double)f5 * 0.25D));
                int l4 = (int)((double)f7 * Math.cos(Math.toRadians(l3)));
                if((double)j3 <= (double)l4 && (double)j3 <= 14000D && (double)j3 >= 200D && l3 >= -30 && l3 <= 30 && Math.sqrt(j4 * j4) <= 60D)
                    flag1 = true;
                else
                    flag1 = false;
            } else
            {
                flag1 = false;
            }
            Aircraft aircraft2 = World.getPlayerAircraft();
            double d5 = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft1)).pos.getAbsPoint().x;
            double d7 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft1)).pos.getAbsPoint().y;
            double d9 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft1)).pos.getAbsPoint().z;
            int j1 = (int)(-((double)((Actor) (aircraft2)).pos.getAbsOrient().getYaw() - 90D));
            if(j1 < 0)
                j1 += 360;
            if(flag1 && aircraft1 == World.getPlayerAircraft() && (aircraft1 instanceof F_18))
            {
                super.pos.getAbs(point3d);
                double d11 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                double d12 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                double d13 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                double d15 = (int)(Math.ceil((d9 - d13) / 10D) * 10D);
                String s = "";
                if(d9 - d13 - 500D >= 0.0D)
                    s = " low";
                if((d9 - d13) + 500D < 0.0D)
                    s = " high";
                new String();
                double d17 = d11 - d5;
                double d18 = d12 - d7;
                float f2 = 57.32484F * (float)Math.atan2(d18, -d17);
                int j2 = (int)(Math.floor((int)f2) - 90D);
                if(j2 < 0)
                    j2 += 360;
                int k2 = j2 - j1;
                if(k2 < 0)
                    k2 += 360;
                int l2 = (int)(Math.ceil((double)(k2 + 15) / 30D) - 1.0D);
                if(l2 < 1)
                    l2 = 12;
                double d21 = d5 - d11;
                double d22 = d7 - d12;
                double d23 = Math.ceil(Math.sqrt(d22 * d22 + d21 * d21) / 10D) * 10D;
                if(bMissileWarning)
                {
                    bRadarWarning = false;
                    playRWRWarning();
                } else
                {
                    bRadarWarning = d23 <= 8000D && d23 >= 500D && Math.sqrt(d15 * d15) <= 6000D;
                    aircraftbrg = Aircraft.cvt(l2, 0.0F, 12F, 0.0F, 360F);
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Enemy at " + l2 + " o'clock" + s + "!");
                }
                playRWRWarning();
            } else
            {
                bRadarWarning = false;
                playRWRWarning();
                aircraftbrg = 0.0F;
            }
        } else
        {
            bRadarWarning = false;
            playRWRWarning();
            aircraftbrg = 0.0F;
        }
        return true;
    }

    private boolean RWRLaunchWarning()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Object obj;
        if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
            obj = World.getPlayerAircraft();
        else
            obj = this;
        super.pos.getAbs(point3d);
        Object obj1;
        if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
            obj1 = World.getPlayerAircraft();
        else
            obj1 = this;
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (obj)).pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (obj)).pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (obj)).pos.getAbsPoint().z;
        int i = (int)((double)(-((Aircraft) (obj1)).pos.getAbsOrient().getYaw()) - 90D);
        if(i < 0)
            i += 360;
        List list = Engine.missiles();
        int j = list.size();
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)list.get(k);
            if(((actor instanceof Missile) || (actor instanceof MissileSAM)) && actor.getSpeed(vector3d) > 20D)
            {
                super.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                String s = "";
                if(d2 - d5 - 500D >= 0.0D)
                    s = " LOW";
                if((d2 - d5) + 500D < 0.0D)
                    s = " HIGH";
                new String();
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float)Math.atan2(d8, -d7);
                int i1 = (int)(Math.floor((int)f) - 90D);
                if(i1 < 0)
                    i1 += 360;
                int j1 = i1 - i;
                if(j1 < 0)
                    j1 += 360;
                int k1 = (int)(Math.ceil((double)(j1 + 15) / 30D) - 1.0D);
                if(k1 < 1)
                    k1 = 12;
                double d9 = d - d3;
                double d10 = d1 - d4;
                double d11 = Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D;
                bMissileWarning = true;
                if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
                {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "MISSILE AT " + k1 + " O'CLOCK" + s + "!!!" + misslebrg);
                    playRWRWarning();
                    misslebrg = Aircraft.cvt(k1, 0.0F, 12F, 0.0F, 360F);
                }
                if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver))
                    backfire = true;
            } else
            {
                bMissileWarning = false;
                playRWRWarning();
                backfire = false;
                misslebrg = 0.0F;
            }
        }

        return true;
    }

    public void playRWRWarning()
    {
        if(bRadarWarning && !fxRWR.isPlaying())
            fxRWR.start();
        else
        if(!bRadarWarning && fxRWR.isPlaying())
            fxRWR.stop();
        if(bMissileWarning && !fxMissileWarning.isPlaying())
        {
            fxMissileWarning.start();
            fxRWR.stop();
        } else
        if(!bMissileWarning && fxMissileWarning.isPlaying())
            fxMissileWarning.stop();
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
        if(FLIR)
        {
            azimult++;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            h += 0.0035F;
    }

    public void typeBomberAdjDistanceMinus()
    {
        if(FLIR)
        {
            azimult--;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            h -= 0.0035F;
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(FLIR)
        {
            tangate++;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            v += 0.0035F;
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(FLIR)
        {
            tangate--;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            v -= 0.0035F;
    }

    public void updatecontrollaser()
    {
        if(tf + 5L <= Time.current())
        {
            tangate = 0.0F;
            azimult = 0.0F;
        }
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(FLIR)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Engaged");
                FM.AP.setStabAltitude(2000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Released");
                FM.AP.setStabAltitude(false);
            }
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
        radarrange++;
        if(radarrange > 5)
            radarrange = 5;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeBomberAdjSpeedMinus()
    {
        radarrange--;
        if(radarrange < 1)
            radarrange = 1;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
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
            fSightCurDistance -= toMetersPerSecond(fSightCurSpeed) * f;
            if(fSightCurDistance < 0.0F)
            {
                fSightCurDistance = 0.0F;
                typeBomberToggleAutomation();
            }
            fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(fSightCurDistance / toMeters(fSightCurAltitude)));
            if((double)fSightCurDistance < (double)toMetersPerSecond(fSightCurSpeed) * Math.sqrt(toMeters(fSightCurAltitude) * 0.2038736F))
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

    private void laser(Point3d point3d)
    {
        point3d.z = World.land().HQ(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y);
        Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, ((Tuple3d) (point3d)).z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        eff3dactor.postDestroy(Time.current() + 1500L);
    }

    private void FLIR()
    {
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(((actor instanceof Aircraft) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric) || (actor instanceof TankGeneric)) && !(actor instanceof StationaryGeneric) && !(actor instanceof TypeLaserSpotter) && actor.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 20000D)
            {
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                actor.pos.getAbs(point3d, orient);
                l.set(point3d, orient);
                Eff3DActor eff3dactor = Eff3DActor.New(actor, null, new Loc(), 1.0F, "effects/Explodes/Air/Zenitka/Germ_88mm/Glow.eff", 1.0F);
                eff3dactor.postDestroy(Time.current() + 1500L);
                LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
                if(actor instanceof Aircraft)
                    lightpointactor.light.setEmit(8F, 50F);
                else
                if(!(actor instanceof ArtilleryGeneric))
                    lightpointactor.light.setEmit(5F, 30F);
                else
                    lightpointactor.light.setEmit(3F, 10F);
                ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
            }
        }

    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(2.5F, 2.5F, 2.0F, 9.5F, 3F, 3F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        t1 = Time.current();
        FM.CT.toggleRocketHook();
        if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
        {
            FM.Sq.squareElevators += 2.0F;
            FM.Sq.liftStab += 2.0F;
        }
        if(this instanceof com.maddox.il2.objects.air.F_16B_B05 || this instanceof com.maddox.il2.objects.air.F_16I)
            bTwoSeat = true;
    }

    public void checkHydraulicStatus()
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
         //   gearTargetAngle = 90F;   // why this line is needed?
            hasHydraulicPressure = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.bHasRudderControl = false;
            FM.CT.bHasFlapsControl = false;
            FM.CT.bHasAirBrakeControl = false;
            FM.CT.AirBrakeControl = 0.0F;
        } else
        if(!hasHydraulicPressure)
        {
            gearTargetAngle = 0.0F;
            hasHydraulicPressure = true;
            FM.CT.bHasAileronControl = true;
            FM.CT.bHasElevatorControl = true;
            FM.CT.bHasRudderControl = true;
            FM.CT.bHasFlapsControl = true;
            FM.CT.bHasAirBrakeControl = true;
            FM.CT.AirBrakeControl = 0.0F;
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
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = FM.getVflow();
            mn = (float)vector3d.lengthSquared();
            mn = (float)Math.sqrt(mn);
            F_16 f_16 = this;
            float f1 = mn;
            World.cur().getClass();
            f_16.mn = f1 / Atmosphere.sonicSpeed((float)((Tuple3d) (FM.Loc)).z);
            if(mn >= 0.9F && (double)mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if (this.FM.Gears.onGround() && this.FM.CT.getCockpitDoor() == 1.0F)
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
        if(FLIR)
            FLIR();
        if(!FLIR)
            FM.AP.setStabAltitude(false);
        if(!FM.isPlayers())
            if(((Maneuver)FM).get_maneuver() == 25 && FM.AP.way.isLanding())
                FM.CT.FlapsControlSwitch = 2;
            else
            if(((Maneuver)FM).get_maneuver() == 26)
                FM.CT.FlapsControlSwitch = 1;
            else
                FM.CT.FlapsControlSwitch = 0;
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
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
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
            }
            break;
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
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 75D - (double) i * 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat0" + i);
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
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

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F, 40F * f);
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 30F * f);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 116F * f);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC55_D0", 0.0F, Aircraft.cvt(f, 0.15F, 0.3F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 85F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 45F * f, -90F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -45F * f, -90F * f);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
        resetYPRmodifier();
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = FM.Gears.gWheelSinking[2];
        Aircraft.xyz[1] = 0.1F - 0.8F * f;
        Aircraft.ypr[1] = gearCurrentSteer;
        hierMesh().chunkSetLocate("GearC7_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        f = FM.Gears.gWheelSinking[0] + FM.Gears.gWheelSinking[1];
        Aircraft.xyz[1] = -f / 2.0F;
     //   hierMesh().chunkSetLocate("GearB21_D0", Aircraft.xyz, Aircraft.ypr);
     //   hierMesh().chunkSetAngles("GearB3_D0", 0.0F, 20F * f, 0.0F);
    }

    public void moveCockpitDoor(float f)
    {
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 0.0F, 45F * f);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    public void moveSteering(float f)
    {
        if(FM.CT.GearControl > 0.5F && FM.Gears.onGround())
            gearCurrentSteer = f;
        else
            gearCurrentSteer = 0F;
        moveWheelSink();
    }

    protected void moveElevator(float f)
    {
        updateControlsVisuals();
    }

    protected void moveAileron(float f)
    {
        updateControlsVisuals();
        if(FM.getSpeedKMH() > 570F)
        {
            float f1 = 2.0F * f;
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f1);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f1);
        } else
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f);
        }
    }

    private final void updateControlsVisuals()
    {
        if(FM.getSpeedKMH() > 590F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -30F * FM.CT.getElevator() + 17F * FM.CT.getAileron());
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -30F * FM.CT.getElevator() - 17F * FM.CT.getAileron());
        } else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -17F * FM.CT.getElevator() + 10F * FM.CT.getAileron());
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -17F * FM.CT.getElevator() - 10F * FM.CT.getAileron());
        }
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, 30F * f);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, 30F * f);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        int i = part(s);
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(13.350000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    if(shot.power <= -7F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(8.770001F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(shot.power <= -7F)
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
                    if(World.Rnd().nextFloat() < -10.5F && getEnergyPastArmor(1.1F, shot) > 200F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200F && World.Rnd().nextFloat() < -10.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200F && World.Rnd().nextFloat() < -10.25F)
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
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 5800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 44000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 44000F)
                {
                    FM.AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                if(s.endsWith("exht"))
                {
                }
            } else
            if(s.startsWith("xxengine1"))
            {
                if(s.endsWith("exht"))
                {
                }
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
                int i1 = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[i1] == 0)
                    {
                        debuggunnery("Fuel Tank (" + i1 + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, i1, 1);
                        FM.AS.doSetTankState(shot.initiator, i1, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        FM.AS.hitTank(shot.initiator, i1, 2);
                        debuggunnery("Fuel Tank (" + i1 + "): Hit..");
                    }
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
            else
            if(s.startsWith("xcf"))
                hitChunk("CF", shot);
            else
            if(s.startsWith("xnose"))
            {
                if(chunkDamageVisible("Nose") < 2)
                    hitChunk("Nose", shot);
                if(chunkDamageVisible("Tail2") < 2)
                    hitChunk("Tail2", shot);
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
                hitChunk("Rudder1", shot);
            else
            if(s.startsWith("xvator"))
            {
                if(s.startsWith("xvatorl"))
                    hitChunk("VatorL", shot);
                if(s.startsWith("xvatorr"))
                    hitChunk("VatorR", shot);
            } else
            if(s.startsWith("xwing"))
            {
                if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 2)
                    hitChunk("WingLIn", shot);
                if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 2)
                    hitChunk("WingRIn", shot);
                if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                {
                    hitChunk("WingLMid", shot);
                    hitChunk("Flap1", shot);
                    hitChunk("Flap12", shot);
                }
                if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                {
                    hitChunk("WingRMid", shot);
                    hitChunk("Flap2", shot);
                    hitChunk("Flap22", shot);
                }
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
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    FM.AS.setInternalDamage(shot.initiator, 3);
                }
            } else
            if(s.startsWith("xpilot") || s.startsWith("xhead"))
            {
                byte byte0 = 0;
                int j1;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    j1 = s.charAt(6) - 49;
                } else
                if(s.endsWith("b"))
                {
                    byte0 = 2;
                    j1 = s.charAt(6) - 49;
                } else
                {
                    j1 = s.charAt(5) - 49;
                }
                hitFlesh(j1, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
label0:
        switch(i)
        {
        default:
            break;

        case 13: // '\r'
            FM.Gears.cgear = false;
            float f = World.Rnd().nextFloat();
            for(int k = 0; k < 1; k++)
            {
                if(f < 0.1F)
                {
                    FM.AS.hitEngine(this, k, 100);
                    if(World.Rnd().nextFloat() < 0.49F)
                        FM.EI.engines[k].setEngineDies(actor);
                    break label0;
                }
                if(f > 0.55F)
                    FM.EI.engines[k].setEngineDies(actor);
            }

            break;

        case 34: // '"'
            FM.Gears.lgear = false;
            break;

        case 37: // '%'
            FM.Gears.rgear = false;
            break;

        case 19: // '\023'
            for(int i1 = 0; i1 < 1; i1++)
            {
                FM.CT.bHasAirBrakeControl = false;
                FM.EI.engines[i1].setEngineDies(actor);
            }

            break;

        case 11: // '\013'
            FM.CT.bHasElevatorControl = false;
            FM.CT.bHasRudderControl = false;
            FM.CT.bHasRudderTrim = false;
            FM.CT.bHasElevatorTrim = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode != 1)
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
        if(calculateMach() <= 1.0F)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if(calculateMach() >= 1.0F)
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
        if(calculateMach() > 1.01F || calculateMach() < 1.0F)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(FM.AS.isMaster())
        {
            for(int i = 0; i < 1; i++)
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = FM.EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl = FM.EI.engines[i].getControlThrottle();
                    if(curthrl < 1.05F)
                    {
                        if((curthrl - oldthrl) / f > 20F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.01D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                                FM.AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                                FM.EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.001D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.4F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                            {
                                if(FM.actor == World.getPlayerAircraft())
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                FM.EI.engines[i].setEngineStops(this);
                            } else
                            if(FM.actor == World.getPlayerAircraft())
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
        if(FM.CT.getFlap() < FM.CT.FlapsControl)
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.08F)));
        else
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.7F)));
        if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            FM.AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
        }
        float f1 = FM.getSpeedKMH() - 1000F;
        if(f1 < 0.0F)
            f1 = 0.0F;
        FM.CT.dvGear = 0.2F - f1 / 1000F;
        if(FM.CT.dvGear < 0.0F)
            FM.CT.dvGear = 0.0F;
        if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver))
        {
            if(FM.AP.way.isLanding() && FM.Gears.onGround() && FM.getSpeed() > 40F)
            {
                FM.CT.AirBrakeControl = 1.0F;
                if(FM.CT.bHasDragChuteControl)
                    FM.CT.DragChuteControl = 1.0F;
            }
            if(FM.AP.way.isLanding() && FM.Gears.onGround() && FM.getSpeed() < 40F)
            {
                FM.CT.AirBrakeControl = 0.0F;
                if(FM.getSpeed() < 20F)
                    FM.CT.DragChuteControl = 0.0F;
            }
        }
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            if(FM.AS.isMaster() && Config.isUSE_RENDER())
            {
                if(FM.EI.engines[0].getPowerOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
                {
                    if(World.getTimeofDay() >= 18F || World.getTimeofDay() <= 6F)
                        FM.AS.setSootState(this, 0, 5);
                    else
                        FM.AS.setSootState(this, 0, 3);
                } else
                {
                    FM.AS.setSootState(this, 0, 0);
                }
                setExhaustFlame(Math.round(Aircraft.cvt(FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
            }
            if(FM instanceof RealFlightModel)
                umn();
        }
        if(FLIR)
            laser(TypeLaserSpotter.spot);
        updatecontrollaser();
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        checkHydraulicStatus();
        moveHydraulics(f);
        soundbarier();
        groundcrew();
        super.update(f);
        this.computeLift();
        this.computeEnergy();
        computeflightmodel();
        moveSlat();
        restoreElevatorControl();
        if(FM.getSpeedKMH() > 300F)
            FM.CT.cockpitDoorControl = 0.0F;
        for(int i = 1; i < 33; i++)
        {
            fNozzleOpenL = FM.EI.engines[0].getPowerOutput() <= 0.92F ? Aircraft.cvt(FM.EI.engines[0].getPowerOutput(), 0.0F, 0.92F, -9F, 0.0F) : Aircraft.cvt(FM.EI.engines[0].getPowerOutput(), 0.92F, 1.1F, 0.0F, -9F);
            hierMesh().chunkSetAngles("Eflap" + i, fNozzleOpenL, 0.0F, 0.0F);
        }

        float f2 = Aircraft.cvt(FM.getSpeedKMH(), 500F, 2000F, 0.999F, 0.301F);
        if(FM.getSpeed() > 7F && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
        {
            if(FM.getOverload() > 5.7F)
            {
                pull01 = Eff3DActor.New(this, findHook("_Pull01"), null, f2, "3DO/Effects/Aircraft/PullingvaporF16.eff", -1F);
                pull02 = Eff3DActor.New(this, findHook("_Pull02"), null, f2, "3DO/Effects/Aircraft/PullingvaporF16.eff", -1F);
            }
            if(FM.getOverload() <= 5.7F)
            {
                Eff3DActor.finish(pull01);
                Eff3DActor.finish(pull02);
            }
        }
    }

    private void groundcrew()
    {
        if(FM.Vwld.length() < 0.05D && FM.Gears.onGround() && FM.CT.cockpitDoorControl > 0.5F
           && Time.current() > timeLastCrewErase + 2000L && (FM.isPlayers() || ((Maneuver)FM).get_maneuver() != 102)
           && !FM.AS.bIsAboutToBailout && !overrideBailout)
        {
            hierMesh().chunkVisible("crew", true);
            bCrewErased = false;
        }
        else
        {
            hierMesh().chunkVisible("crew", false);
            if(!bCrewErased)
            {
                timeLastCrewErase = Time.current();
                bCrewErased = true;
            }
        }
    }


    public void computeLift()
       {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float x = this.calculateMach();
        if(this.calculateMach() >= 0.0F);
        float Lift = 0.0F;
        if((double)x > 2.25F)
        {
            Lift = 0.12F;
        } else
        {
            float x2 = x * x;
            float x3 = x2 * x;
            float x4 = x3 * x;
            float x5 = x4 * x;
            float x6 = x5 * x;
            float x7 = x6 * x;
            float x8 = x7 * x;
            float x9 = x8 * x;
            Lift= 0.00152131F*x8 + 0.0351945F*x7 - 0.403687F*x6 + 1.58931F*x5 - 3.09189F*x4 + 3.21415F*x3 - 1.73844F*x2 + 0.364213F*x + 0.078F;
           // {{0.0,0.078},{0.25, 0.1}, {0.6, 0.05},{0.97, 0.04}, {1.3, 0.03},{1.68, 0.013}, {2.0, 0.012}, {2.2, 0.011}, {2.3, 0.010}}
            }
        polares.lineCyCoeff= Lift;
    }
    
    public void computeEnergy()
       {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float x = this.calculateMach();
        if(this.calculateMach() >= 0.0F);
        float Energy = 0.0F;
        if((double)x >=1F)
        {
            Energy = 8.5F;
        } else
        {
            float x2 = x * x;
            float x3 = x2 * x;
            float x4 = x3 * x;
            float x5 = x4 * x;
            float x6 = x5 * x;
            Energy= -1672.62F*x5 +  3775.6F*x4 - 2934.82F*x3 + 933.101F*x2 - 99.256F*x + 6.5F;  
            //{{0,6.5}, {0.2,6},{0.5,7},{0.7,13},{0.9,23},{1,8.5}}                                                  
            }
        polares.parabCxCoeff_0 = Energy*0.0001F;
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

    private void computeflightmodel()
    {
        float f = (float)((double)(Aircraft.cvt(FM.getAOA(), 0.0F, 5F, 0.0F, 1.0F) * FM.getAOA()) * 0.025000000000000001D * (double)Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 500F, 0.0F, 1.0F));
        if(FM.getSpeedKMH() > 465F || FM.CT.FlapsControlSwitch == 0)
        {
            if(FM.CT.FlapsControlSwitch > 0)
                bForceFlapmodeAuto = true;
            else
                bForceFlapmodeAuto = false;
            if(Time.current() > tflap + 3000L && Time.current() < tflap + 4000L)
            {
                FM.CT.setTrimElevatorControl(0.0F);
            } else
            if(Time.current() > tflap + 4000L)
                FM.CT.FlapsControl = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 0.2F);
        } else
        {
            bForceFlapmodeAuto = false;
            float f1 = Aircraft.cvt(FM.getSpeedKMH(), 330F, 465F, 1.0F, 0.0F);
            if(FM.CT.FlapsControlSwitch == 1 && f1 > FM.CT.FlapStage[0])
                f1 = FM.CT.FlapStage[0];
            FM.CT.FlapsControl = f1;
            tflap = Time.current();
            if((double)FM.CT.FlapsControl > 0.59999999999999998D && FM.Gears.onGround())
                FM.CT.setTrimElevatorControl(0.7F);
        }
        if(FM.getAOA() > 28F || FM.getSpeedKMH() < 469F && FM.CT.FlapsControl > 0.16F && FM.CT.getGear() < 0.8F || FM.getOverload() >= 6F)
            FM.CT.AirBrakeControl = 0.0F;
    }




    private float clamp11(float f)
    {
        if(f < -1F)
            f = -1F;
        else
        if(f > 1.0F)
            f = 1.0F;
        return f;
    }

    public void restoreElevatorControl()
    {
        if(hasHydraulicPressure)
            FM.CT.bHasElevatorControl = true;
    }

    public void netUpdateWayPoint()
    {
        super.netUpdateWayPoint();
        if(!(FM instanceof RealFlightModel))
        {
            return;
        } else
        {
            computeElevators();
            return;
        }
    }

    public void computeElevators()
    {
        if(FM.CT.StabilizerControl)
            return;
        if(FM.Gears.onGround() && FM.CT.FlapsControl > 0.68F)
            return;
        if(FM.getSpeedKMH() < 30F)
            return;
        float elevatorControl = FM.CT.getElevator();
        float targetGForce = 0.0F;
        if(FM.CT.ElevatorControl > 0.0F)
            targetGForce = (FM.getLimitLoad() * 0.9F - 1.0F) * FM.CT.ElevatorControl + 1.0F;
        else
            targetGForce = 1.0F - (FM.Negative_G_Limit * 0.9F - 1.0F) * FM.CT.ElevatorControl;
        float gForceDiff  = FM.getOverload() - targetGForce;
        elevatorControl -= gForceDiff  / Math.max(FM.getSpeedKMH() / 1.6F, 200F);
        elevatorControl = clamp11(elevatorControl);
        FM.CT.bHasElevatorControl = false;
        if(elevatorsField == null)
        {
            elevatorsField = Reflection.getField(FM.CT, "Elevators");
            elevatorsField.setAccessible(true);
        }
        try
        {
            elevatorsField.setFloat(FM.CT, elevatorControl);
        }
        catch(IllegalAccessException illegalaccessexception) { }
    }

    private void moveSlat()
    {
        if(FM.Gears.onGround())
        {
            hierMesh().chunkSetAngles("Slat01_D0", 0.0F, 0.0F, -14.5F);
            hierMesh().chunkSetAngles("Slat02_D0", 0.0F, 0.0F, -14.5F);
        } else
        if(FM.getSpeed() > 5F && !FM.Gears.onGround())
        {
            hierMesh().chunkSetAngles("Slat01_D0", 0.0F, 0.0F, Aircraft.cvt(FM.getAOA(), 6.8F, 15F, 0.0F, -14.5F));
            hierMesh().chunkSetAngles("Slat02_D0", 0.0F, 0.0F, Aircraft.cvt(FM.getAOA(), 6.8F, 15F, 0.0F, -14.5F));
        }
        if(FM.getSpeed() > 10F && !FM.Gears.onGround())
        {
            hierMesh().chunkSetAngles("Slat01_D0", 0.0F, 0.0F, Aircraft.cvt(FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
            hierMesh().chunkSetAngles("Slat02_D0", 0.0F, 0.0F, Aircraft.cvt(FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
        }
        if(FM.getSpeed() > 20F && !FM.Gears.onGround())
        {
            hierMesh().chunkSetAngles("Slat01_D0", 0.0F, 0.0F, Aircraft.cvt(FM.getAOA(), 6.8F, 15F, 0.0F, -30.5F));
            hierMesh().chunkSetAngles("Slat02_D0", 0.0F, 0.0F, Aircraft.cvt(FM.getAOA(), 6.8F, 15F, 0.0F, -30.5F));
        }
    }

    public void moveRefuel(float f)
    {
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
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.5F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 4F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            break;

        case 2: // '\002'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.8F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF18.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF18A.eff", -1F);
            break;

        case 4: // '\004'
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
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
                    if(FM.CT.cockpitDoorControl < 0.5F || FM.getSpeedKMH() > 15F)
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    lTimeNextEject = Time.current() + 1000L;
                    break;
                }
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate = FM.AS;
                aircraftstate.astateBailoutStep = (byte)(aircraftstate.astateBailoutStep + 1);
                if(FM.AS.astateBailoutStep == 4)
                    FM.AS.astateBailoutStep = 11;
            } else
            if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
            {
                byte byte0 = FM.AS.astateBailoutStep;
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate1 = FM.AS;
                aircraftstate1.astateBailoutStep = (byte)(aircraftstate1.astateBailoutStep + 1);
                if(byte0 == 11)
                {
                    FM.setTakenMortalDamage(true, null);
                    if((FM instanceof Maneuver) && ((Maneuver)FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver)FM).set_maneuver(44);
                    }
                }
                if(FM.AS.astatePilotStates[byte0 - 11] < 99)
                {
                    doRemoveBodyFromPlane(byte0 - 10);
                    if(byte0 == 11)
                    {
                        doEjectCatapult(byte0 - 10);
                        FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        lTimeNextEject = Time.current() + 1000L;
                        if(!bTwoSeat)
                        {
                            FM.AS.astateBailoutStep = -1;
                            overrideBailout = false;
                            FM.AS.bIsAboutToBailout = true;
                            ejectComplete = true;
                            if(byte0 > 10 && byte0 <= 19)
                                EventLog.onBailedOut(this, byte0 - 11);
                        }
                    }
                    else if (bTwoSeat && byte0 == 12) {
                        doEjectCatapult(byte0 - 10);
                        FM.AS.astateBailoutStep = 51;
                        super.FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    FM.AS.astatePilotStates[byte0 - 11] = 99;
                } else {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + FM.AS.astatePilotStates[byte0 - 11]);
                }
            }
    }

    private final void doRemoveBlister1()
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
        F_16 f_16 = this;
        float f = mn;
        World.cur().getClass();
        f_16.mn = f / Atmosphere.sonicSpeed((float)FM.Loc.z);
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
    radarrange++;
    if(radarrange > 4)
         radarrange = 4;
    HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarGainPlus()
    {
    radarrange--;
    if(radarrange < 1)
          radarrange = 1;
     HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarRangeMinus()
    {
        radarrange++;
        if(radarrange > 4)
            radarrange = 4;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarRangePlus()
    {
        radarrange--;
        if(radarrange < 1)
            radarrange = 1;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
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
        radarmode++;
        if((this instanceof com.maddox.il2.objects.air.F_16B_B05 || this instanceof com.maddox.il2.objects.air.F_16A_B10)
           && radarmode > 1)
            radarmode = 0;
        if(radarmode > 2)
            radarmode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar mode" + radarmode);
        return false;
    }

    public boolean typeBomberToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Bomb");
        } else
        if(k14Mode == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Gunnery");
        } else
        if(k14Mode == 2 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Navigation");
        return true;
    }

    public boolean typeGuidedBombCisMasterAlive()
    {
        return isMasterAlive;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag)
    {
        isMasterAlive = flag;
    }

    public boolean typeGuidedBombCgetIsGuiding()
    {
        return isGuidingBomb;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag)
    {
        isGuidingBomb = flag;
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 0.002F;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.002F;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.002F;
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

    public float Fuelamount;
    public int nozzlemode;
    public long tvect;
    private boolean bDynamoOperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;
    public int radarrange;
    private long twait;
    protected boolean bSlatsOff;
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
    private float gearCurrentSteer;
    public boolean hasHydraulicPressure;
    private static final float NEG_G_TOLERANCE_FACTOR = 2.5F;
    private static final float NEG_G_TIME_FACTOR = 2.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 2F;
    private static final float POS_G_TOLERANCE_FACTOR = 9.5F;
    private static final float POS_G_TIME_FACTOR = 3F;
    private static final float POS_G_RECOVERY_FACTOR = 3F;
    private Eff3DActor pull1;
    private Eff3DActor pull2;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public boolean FLIR;
    private static Loc l = new Loc();
    public int clipBoardPage_;
    public boolean showClipBoard_;
    private ArrayList missilesList;
    private float deltaAzimuth;
    private float deltaTangage;
    private boolean isGuidingBomb;
    private boolean isMasterAlive;
    private Eff3DActor pull01;
    private Eff3DActor pull02;
    public int lockmode;
    private boolean APmode1;
    public boolean ILS;
    public boolean FL;
    public float azimult;
    public float tangate;
    public long tf;
    public float v;
    public float h;
    public boolean hold;
    private long t1;
    public boolean radartogle;
    public int radarmode;
    public int targetnum;
    public float lockrange;
    public int radargunsight;
    public int leftscreen;
    public int Bingofuel;
    public boolean Nvision;
    public float misslebrg;
    public float aircraftbrg;
    public boolean backfire;
    public boolean bRadarWarning;
    public boolean bMissileWarning;
    private SoundFX fxRWR;
    private SoundFX fxMissileWarning;
    private Field elevatorsField;
    private long tflap;
    public boolean bForceFlapmodeAuto;
    public float fNozzleOpenL;
    private Field thrustMaxField[];
    private long timeLastCrewErase;
    private boolean bCrewErased;
    private boolean bTwoSeat;
    private long lTimeNextEject;

  static
    {
        Class class1 = com.maddox.il2.objects.air.F_16.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
