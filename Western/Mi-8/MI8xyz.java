
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import java.util.ArrayList;


public class MI8xyz extends Mi8_24HeliFamily
    implements TypeScout, TypeTransport, TypeStormovik, TypeGuidedMissileCarrier, TypeCountermeasure, TypeRadarWarningReceiver
{

    public MI8xyz()
    {
        guidedMissileUtils = new GuidedMissileUtils(this);
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        backfire = false;

        if(Config.cur.ini.get("Mods", "RWRTextStop", 0) > 0) bRWR_Show_Text_Warning = false;
        rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_RHMIS, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, RWR_SECOTOR_NUM, bRWR_Show_Text_Warning);
    }

    public RadarWarningReceiverUtils getRadarWarningReceiverUtils()
    {
        return rwrUtils;
    }

    public void myRadarSearchYou(Actor actor, String soundpreset)
    {
        rwrUtils.recordRadarSearched(actor, soundpreset);
    }

    public void myRadarLockYou(Actor actor, String soundpreset)
    {
        rwrUtils.recordRadarLocked(actor, soundpreset);
    }

    public void startCockpitSounds()
    {
        if(rwrUtils != null)
            rwrUtils.setSoundEnable(true);
    }

    public void stopCockpitSounds()
    {
        if(rwrUtils != null)
            rwrUtils.stopAllRWRSounds();
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

    public void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        {
            if(Time.current() > lastFlareDeployed + 700L)
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
            if(Time.current() > lastChaffDeployed + 1000L)
            {
                ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
                hasChaff = true;
                lastChaffDeployed = Time.current();
                if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                    counterChaffList.remove(0);
            }
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    private void checkAmmo()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_GUV8700_gn16)
                        ((Pylon_GUV8700_gn16) FM.CT.Weapons[i][j]).matSilver();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_GUV8700AGS_gn16)
                        ((Pylon_GUV8700AGS_gn16) FM.CT.Weapons[i][j]).matSilver();
                    else if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                            counterFlareList.add(FM.CT.Weapons[i][j]);
                    }
                }
            }

    }

    protected void moveElevator(float f)
    {
    }

    public void moveSteering(float f)
    {
    }

    public void moveWheelSink()
    {
        hierMesh().chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 1.0F, 0.0F, 40F), 0.0F);
        hierMesh().chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, 40F), 0.0F);
        hierMesh().chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 1.0F, 0.0F, 30F), 0.0F);
        hierMesh().chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, 30F), 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, 1.2F);
        hierMesh().chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void onAircraftLoaded()
    {
        checkAmmo();
        super.onAircraftLoaded();
        FM.CT.bHasAntiColLights = true;
        guidedMissileUtils.onAircraftLoaded();

        rwrUtils.onAircraftLoaded();
        rwrUtils.setLockTone("aircraft.usRWRScan", "aircraft.Sirena2", "aircraft.Sirena2", "aircraft.usRWRThreatNew");
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(!FM.isPlayers())
            FM.CT.bAntiColLights = FM.AS.bNavLightsOn;
        anticollights();
    }

    private void anticollights()
    {
        if(FM.CT.bAntiColLights && isGeneratorAlive && chunkDamageVisible("Tail1") < 3)
        {
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            super.pos.getAbs(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(this, findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
            lightpointactor.light.setEmit(1.0F, 3F);
            ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
        }
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        rwrUtils.update();
        backfire = rwrUtils.getBackfire();
        bRadarWarning = rwrUtils.getRadarLockedWarning();
        bMissileWarning = rwrUtils.getMissileWarning();
        if(backfire)
            backFire();
        super.update(f);
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

    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;

    //By western0221, Radar Warning Receiver
    private RadarWarningReceiverUtils rwrUtils;
    public boolean bRadarWarning;
    public boolean bMissileWarning;
    public boolean backfire;

    private static final int RWR_GENERATION = 1;
    private static final int RWR_MAX_DETECT = 16;
    private static final int RWR_KEEP_SECONDS = 6;
    private static final double RWR_RECEIVE_ELEVATION = 45.0D;
    private static final boolean RWR_DETECT_RHMIS = false;
    private static final boolean RWR_DETECT_IRMIS = true;
    private static final boolean RWR_DETECT_ELEVATION = false;
    private static final int RWR_SECOTOR_NUM = 8;
    private boolean bRWR_Show_Text_Warning = true;
}