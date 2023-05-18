// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:18:40
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileGun.java

package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.DifficultySettings;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeGuidedMissileCarrier;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun, Missile, GuidedMissileUtils

public class MissileGun extends RocketGun
{

    public MissileGun()
    {
        engineWarmupRunning = false;
        engineWarmupTime = 0L;
        shotFrequency = 500L;
        shotsAfterEngineWarmup = 0;
        theMissileName = null;
    }

    public float bulletMassa()
    {
        return bulletMassa / 10F;
    }

    public void checkPendingWeaponRelease()
    {
        if(engineWarmupRunning)
        {
            Missile missile = (Missile)rocket;
            if(missile == null)
            {
                engineWarmupRunning = false;
                return;
            }
            if(Time.current() > missile.getStartTime() + engineWarmupTime)
                shots(shotsAfterEngineWarmup);
            else
                missile.runupEngine();
        }
    }

    public Missile getMissile()
    {
        return (Missile)rocket;
    }

    public void shots(int i)
    {
        Missile missile;
        if(i == 0)
            return;
        if(hudMissileGunId == 0)
            hudMissileGunId = HUD.makeIdLog();
        missile = (Missile)rocket;
        if(theMissileName == null && missile != null)
        {
            theMissileName = Property.stringValue(missile.getClass(), "friendlyName", "Missile");
            shotFrequency = (long)(1000F * Property.floatValue(missile.getClass(), "shotFreq", 0.5F));
        }
        try
        {
            if(Actor.isValid(actor) && (actor instanceof Aircraft) && (actor instanceof TypeGuidedMissileCarrier) && (Aircraft)actor == World.getPlayerAircraft() && ((RealFlightModel)((SndAircraft) ((Aircraft)actor)).FM).isRealMode() && ((TypeGuidedMissileCarrier)actor).getGuidedMissileUtils().hasMissiles() && ((TypeGuidedMissileCarrier)actor).getGuidedMissileUtils().getMissileLockState() == 0)
            {
                //GuidedMissileUtils.LocalLog(actor, hudMissileGunId, theMissileName + " launch cancelled (disengaged)");
                HUD.log(hudLogMissileId, "LCD", new Object[] {
                		theMissileName
                    });
                return;
            }
        }
        catch(Exception exception)
        {
            //GuidedMissileUtils.LocalLog(actor, hudMissileGunId, theMissileName + " launch cancelled (system error)");
            HUD.log(hudLogMissileId, "LCS", new Object[] {
            		theMissileName
                });
            return;
        }
        if(Actor.isValid(actor) && (actor instanceof Aircraft) && (actor instanceof TypeGuidedMissileCarrier) && (Aircraft)actor == World.getPlayerAircraft() && ((RealFlightModel)((SndAircraft) ((Aircraft)actor)).FM).isRealMode() && ((TypeGuidedMissileCarrier)actor).getGuidedMissileUtils().hasMissiles() && Time.current() < ((TypeGuidedMissileCarrier)actor).getGuidedMissileUtils().getStartLastMissile() + shotFrequency)
        {
            //GuidedMissileUtils.LocalLog(actor, hudMissileGunId, theMissileName + " launch cancelled (missile not ready yet)");
            HUD.log(hudLogMissileId, "LCR", new Object[] {
            		theMissileName
                });
            return;
        }
        if(missile == null)
        {
            engineWarmupRunning = false;
            return;
        }
        if(engineWarmupRunning && Time.current() < missile.getStartTime() + engineWarmupTime)
        {
            //GuidedMissileUtils.LocalLog(actor, hudMissileGunId, theMissileName + " launch cancelled (engine warmup running)");
            HUD.log(hudLogMissileId, "LCE", new Object[] {
            		theMissileName
                });
            return;
        }
        engineWarmupTime = Property.longValue(missile.getClass(), "engineDelayTime", 0L) * -1L;
        if(engineWarmupTime > 0L && !engineWarmupRunning)
        {
            missile.startEngine();
            missile.setStartTime();
            if(engineWarmupTime > 1000L)
                //GuidedMissileUtils.LocalLog(actor, hudMissileGunId, theMissileName + " engine starting");
	            HUD.log(hudLogMissileId, "EnStart", new Object[] {
	            		theMissileName
	                });
            
            engineWarmupRunning = true;
            shotsAfterEngineWarmup = i;
            return;
        }
        engineWarmupRunning = false;
        bExecuted = false;
        super.shots(i);
        if(Actor.isValid(super.actor) && (actor instanceof TypeGuidedMissileCarrier))
            ((TypeGuidedMissileCarrier)actor).getGuidedMissileUtils().setStartLastMissile(Time.current());
        if(engineWarmupTime > 1000L)
            //GuidedMissileUtils.LocalLog(super.actor, hudMissileGunId, theMissileName + " released");
	        HUD.log(hudLogMissileId, "Released", new Object[] {
	        		theMissileName
	            });
        if(!NetMissionTrack.isPlaying() && !actor.isNetMirror() && i > 0 && Actor.isValid(super.actor) && (actor instanceof TypeGuidedMissileCarrier) && (World.cur().diffCur.Limited_Ammo || actor != World.getPlayerAircraft()))
            ((TypeGuidedMissileCarrier)actor).getGuidedMissileUtils().shotMissile();
        return;
    }

    public static int hudMissileGunId = 0;
    private boolean engineWarmupRunning;
    private long engineWarmupTime;
    private long shotFrequency;
    private int shotsAfterEngineWarmup;
    private String theMissileName;
    public static int hudLogMissileId = HUD.makeIdLog();

}