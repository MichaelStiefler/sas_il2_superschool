// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 28.04.2015 14:09:28
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MIG_21bis.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            MIG_21, Aircraft, TypeFighterAceMaker, TypeSupersonic, 
//            TypeFastJet, Chute, PaintSchemeFMParMiG21, TypeGuidedMissileCarrier, 
//            TypeCountermeasure, TypeThreatDetector, Cockpit, NetAircraft, 
//            EjectionSeat

public class MIG_21bis extends MIG_21
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public MIG_21bis()
    {
        counter = 0;
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
        fxSPO10 = newSound("aircraft.Sirena2", false);
        smplSPO10 = new Sample("sample.Sirena2.wav", 256, 65535);
        SPO10SoundPlaying = false;
        smplSPO10.setInfinite(true);
        counter = 0;
        freq = 800;
        Timer1 = Timer2 = freq;
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        super.bHasSK1Seat = false;
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        guidedMissileUtils.onAircraftLoaded();
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        super.k14Mode++;
        if(super.k14Mode > 1)
            super.k14Mode = 0;
        if(super.k14Mode == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed");
        } else
        if(super.k14Mode == 1)
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + Gyro");
            else
            if(super.k14Mode == 2 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Gyro");
        return true;
    }

    public void rareAction(float f, boolean flag)
    {
        if(counter++ % 5 == 0)
            sirenaWarning();
        super.rareAction(f, flag);
    }

    private boolean sirenaWarning()
    {
        boolean flag = false;
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        double d3 = d2 - (double)Landscape.Hmin((float)((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x, (float)((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y);
        if(d3 < 0.0D)
            d3 = 0.0D;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        Aircraft aircraft1 = War.getNearestEnemy(aircraft, 6000F);
        if((aircraft1 instanceof Aircraft) && aircraft.getArmy() != World.getPlayerArmy() && (aircraft instanceof TypeFighterAceMaker) && ((aircraft instanceof TypeSupersonic) || (aircraft instanceof TypeFastJet)) && aircraft1 == World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
        {
            super.pos.getAbs(point3d);
            double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
            double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
            double d8 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
            new String();
            new String();
            int k = (int)(Math.floor(((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z * 0.10000000000000001D) * 10D);
            int l = (int)(Math.floor((aircraft1.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
            double d10 = (int)(Math.ceil((d2 - d8) / 10D) * 10D);
            boolean flag1 = false;
            Engine.land();
            int j1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x), Engine.land().WORLD2PIXY(((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y));
            float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
            if(j1 >= 28 && j1 < 32 && f < 7.5F)
                flag1 = true;
            new String();
            double d14 = d4 - d;
            double d16 = d6 - d1;
            float f1 = 57.32484F * (float)Math.atan2(d16, -d14);
            int k1 = (int)(Math.floor((int)f1) - 90D);
            if(k1 < 0)
                k1 += 360;
            int l1 = k1 - i;
            double d19 = d - d4;
            double d20 = d1 - d6;
            Random random = new Random();
            float f3 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
            int l2 = random.nextInt(6) - 3;
            float f4 = 19000F;
            float f5 = f4;
            if(d3 < 1200D)
                f5 = (float)(d3 * 0.80000001192092896D * 3D);
            int i3 = (int)(Math.ceil(Math.sqrt((d20 * d20 + d19 * d19) * (double)f3) / 10D) * 10D);
            if((float)i3 > f4)
                i3 = (int)(Math.ceil(Math.sqrt(d20 * d20 + d19 * d19) / 10D) * 10D);
            float f6 = 57.32484F * (float)Math.atan2(i3, d10);
            int j3 = (int)(Math.floor((int)f6) - 90D);
            int k3 = (j3 - (90 - j)) + l2;
            int l3 = (int)f4;
            if((float)i3 < f4)
                if(i3 > 1150)
                    l3 = (int)(Math.ceil((double)i3 / 900D) * 900D);
                else
                    l3 = (int)(Math.ceil((double)i3 / 500D) * 500D);
            int i4 = l1 + l2;
            int j4 = i4;
            if(j4 < 0)
                j4 += 360;
            float f7 = (float)((double)f5 + Math.sin(Math.toRadians(Math.sqrt(l1 * l1) * 3D)) * ((double)f5 * 0.25D));
            int k4 = (int)((double)f7 * Math.cos(Math.toRadians(k3)));
            if((double)i3 <= (double)k4 && (double)i3 <= 14000D && (double)i3 >= 200D && k3 >= -30 && k3 <= 30 && Math.sqrt(i4 * i4) <= 60D)
                flag = true;
            else
                flag = false;
        }
        super.pos.getAbs(point3d);
        Aircraft aircraft2 = World.getPlayerAircraft();
        double d5 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
        double d7 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
        double d9 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
        int i1 = (int)(-((double)((Actor) (aircraft2)).pos.getAbsOrient().getYaw() - 90D));
        if(i1 < 0)
            i1 += 360;
        if(flag && aircraft1 == World.getPlayerAircraft())
        {
            super.pos.getAbs(point3d);
            double d11 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
            double d12 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
            double d13 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
            new String();
            new String();
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
            int i2 = (int)(Math.floor((int)f2) - 90D);
            if(i2 < 0)
                i2 += 360;
            int j2 = i2 - i1;
            if(j2 < 0)
                j2 += 360;
            int k2 = (int)(Math.ceil((double)(j2 + 15) / 30D) - 1.0D);
            if(k2 < 1)
                k2 = 12;
            double d21 = d5 - d11;
            double d22 = d7 - d12;
            double d23 = Math.ceil(Math.sqrt(d22 * d22 + d21 * d21) / 10D) * 10D;
            bRadarWarning = d23 <= 8000D && d23 >= 500D && Math.sqrt(d15 * d15) <= 6000D;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-10: Spike at " + k2 + " o'clock" + s + "!");
            playSirenaWarning(bRadarWarning);
        } else
        {
            bRadarWarning = false;
            playSirenaWarning(bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean flag)
    {
        if(!flag || sirenaSoundPlaying)
            if(flag);
    }

    public void update(float f)
    {
        super.update(f);
        computeR25_300_AB();
        if(Config.isUSE_RENDER())
            if(!((FlightModelMain) (super.FM)).AS.bIsAboutToBailout)
            {
                if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null && Main3D.cur3D().cockpits[0].cockpitDimControl)
                {
                    hierMesh().chunkVisible("Head1_D0", false);
                    hierMesh().chunkVisible("Glass_Head1_D0", true);
                } else
                {
                    hierMesh().chunkVisible("Head1_D0", true);
                    hierMesh().chunkVisible("Glass_Head1_D0", false);
                }
            } else
            {
                hierMesh().chunkVisible("Glass_Head1_D0", false);
            }
        setSubsonicLimiter();
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteMiG21bis/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-7D, 0.0D, 0.59999999999999998D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() > 600F || ((FlightModelMain) (super.FM)).CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !((FlightModelMain) (super.FM)).CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
        guidedMissileUtils.update();
    }

    private void setSubsonicLimiter()
    {
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.96999999999999997D && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx += 0.003F;
    }

    public void computeR25_300_AB()
    {
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 30000D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            if((double)f > 17.5D)
            {
                f1 = 28F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = ((86F * f3 - 2035F * f2) + 16331F * f) / 4410F;
            }
        FM.producedAF.x -= f1 * 1000F;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 1500F, -140F);
        float f2 = Math.max(-f * 1500F, -100F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -125F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL44_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR44_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, -f1);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, f1);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, -f2);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, f2);
        hiermesh.chunkSetAngles("GearTelescopeL", 0.0F, 115F * f, 0.0F);
        hiermesh.chunkSetAngles("GearTelescopeR", 0.0F, 115F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.32F);
        hierMesh().chunkSetLocate("GearL33_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.32F);
        hierMesh().chunkSetLocate("GearR33_D0", Aircraft.xyz, Aircraft.ypr);
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        float f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[2], 0.0F, 0.4F, 0.0F, 0.3F);
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, 50F * f, 0.0F);
        resetYPRmodifier();
        f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 1.0F, 0.0F, -0.5F);
        Aircraft.xyz[0] = f;
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, -0.5F);
        Aircraft.xyz[0] = f;
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveSteering(float f)
    {
        if((double)((FlightModelMain) (super.FM)).CT.getGear() < 0.98999999999999999D)
            hierMesh().chunkSetAngles("GearC_D0", 0.0F * f, 0.0F, 0.0F);
        else
            hierMesh().chunkSetAngles("GearC_D0", 30F * f, 0.0F, 0.0F);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
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
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
        ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
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

    private SoundFX fxSirenaLaunch;
    private Sample smplSirenaLaunch;
    private boolean sirenaLaunchSoundPlaying;
    private boolean bRadarWarningLaunch;
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    public float Timer1;
    public float Timer2;
    private int freq;
    private GuidedMissileUtils guidedMissileUtils;
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
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private int counter;
    protected boolean bHasBoosters;
    protected long boosterFireOutTime;
    private SoundFX fxSPO10;
    private Sample smplSPO10;
    private boolean SPO10SoundPlaying;
    private boolean bRadarWarning;
    protected boolean bHasSK1Seat;
    private static Point3d p = new Point3d();

    static 
    {
        Class class1 = com.maddox.il2.objects.air.MIG_21bis.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG21");
        Property.set(class1, "meshName", "3DO/Plane/MiG-21bis/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-21bis.fmd:MIG21BIS");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMIG_21MF.class, com.maddox.il2.objects.air.CockpitRP_22.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 9, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 
            2, 2, 9, 9, 2, 2, 3, 3, 3, 3, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 9, 9, 3, 3, 3, 3, 
            3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 
            2, 2, 7, 7, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_Flare01", "_Flare02", "_ExternalDev02", "_ExternalDev01", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_Rock01", "_Rock02", 
            "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_Rock07", "_Rock08", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", 
            "_ExternalRock09", "_ExternalRock10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock11", "_ExternalRock22", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalDev13", "_ExternalDev14", "_Rock23", "_Rock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_Rock29", "_Rock30", 
            "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", 
            "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_Rock35", "_Rock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", 
            "_Rock41", "_Rock42", "_CANNON01", "_CANNON02", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 68;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-60M+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-13M+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-13M+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xR-3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xR-3R+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-13M+2xR-3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-13M+2xR-3R+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M(APU-60-II)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-3S+1x800L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunR13M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunR13M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-13M+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunR13M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunR13M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-13M+1x800L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunR13M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunR13M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-60M+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-60M+1x800L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-3R+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-60M+2xR-3R+1x800L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRS-2US";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRS-2US+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRS-2USf";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRS-2US+1x490Lf";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xRS-2US";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xRS-2US+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-55";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-55+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xR-55";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xR-55+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+3x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-13M+3x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-60M+3x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xR-13M+1x800L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+4xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+4xR-60M+1x800L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xR-60M+1x800L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS-24";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS-24+R-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS-24+R-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS-24+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS-24+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS-24+R-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS-24+R-13M+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS-24+R-60M+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+2xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+2x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-100";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-100";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250m46";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-250m46";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xZB-360";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xZB-360";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xFAB-100";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB-100+2xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB-100+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB-100+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB-100+4xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB-100+4xR-60M+PTB-800L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "APU60L", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "APU60R", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
