// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MIG_21PF.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;
import java.util.Map;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            MIG_21, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, 
//            PaintSchemeFMParMiG21, Aircraft, Cockpit, Chute, 
//            NetAircraft

public class MIG_21PF extends MIG_21
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public MIG_21PF()
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
        long curTime = Time.current();
        if(curTime - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = curTime;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = curTime;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = curTime;
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
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        guidedMissileUtils.onAircraftLoaded();
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(super.k14Mode == 0)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
            hunted = War.GetNearestEnemyAircraft(((Interpolate) (super.FM)).actor, 2000F, 9);
        if(hunted != null)
        {
            super.k14Distance = (float)((Interpolate) (super.FM)).actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(super.k14Distance > 1700F)
                super.k14Distance = 1700F;
            else
            if(super.k14Distance < 200F)
                super.k14Distance = 200F;
        }
    }

    protected void moveFlap(float f)
    {
        super.moveFlap(f);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.3F);
        hierMesh().chunkSetLocate("Flap01a_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap02a_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        super.k14Mode++;
        if(super.k14Mode > 1)
            super.k14Mode = 0;
        if(super.k14Mode == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "PKI Sight: On");
        } else
        if(super.k14Mode == 1 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "PKI Sight: Off");
        return true;
    }

    public void update(float f)
    {
        super.update(f);
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
        setAfterburner();
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteMiG21/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(1.0F);
            ((Actor) (chute)).pos.setRel(new Point3d(-5D, 0.0D, -0.59999999999999998D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() > 600F || ((FlightModelMain) (super.FM)).CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 0.40000000000000002D), new Orient(0.0F, 60F, 0.0F));
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
        typeFighterAceMakerRangeFinder();
        guidedMissileUtils.update();
    }

    public void rareAction(float f, boolean flag)
    {
        if(counter++ % 5 == 0)
            RP21();
        super.rareAction(f, flag);
    }

    private boolean RP21()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
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
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if((actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
            {
                super.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                new String();
                new String();
                double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                Engine.land();
                String s = "level with us";
                if(d2 - d6 - 300D >= 0.0D)
                    s = "below us";
                if((d2 - d6) + 300D <= 0.0D)
                    s = "above us";
                if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                    s = "slightly below";
                if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                    s = "slightly above";
                new String();
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                int j1 = (int)(Math.floor((int)f1) - 90D);
                if(j1 < 0)
                    j1 += 360;
                int k1 = j1 - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f2 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int l1 = random.nextInt(6) - 3;
                float f3 = 10000F;
                float f4 = f3;
                if(d3 < 1200D)
                    f4 = (float)(d3 * 0.80000001192092896D * 3D);
                int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
                if((float)i2 > f3)
                    i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                float f5 = 57.32484F * (float)Math.atan2(i2, d7);
                int j2 = (int)(Math.floor((int)f5) - 90D);
                int k2 = (j2 - (90 - j)) + l1;
                int l2 = (int)f3;
                if((float)i2 < f3)
                    if(i2 > 1150)
                        l2 = (int)(Math.ceil((double)i2 / 900D) * 900D);
                    else
                        l2 = (int)(Math.ceil((double)i2 / 500D) * 500D);
                int i3 = k1 + l1;
                int j3 = i3;
                if(j3 < 0)
                    j3 += 360;
                float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * ((double)f4 * 0.25D));
                int k3 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                String s1 = "  ";
                if(j3 < 5)
                    s1 = "Dead ahead, ";
                if(j3 >= 5 && (double)j3 <= 7.5D)
                    s1 = "Right 5, ";
                if((double)j3 > 7.5D && (double)j3 <= 12.5D)
                    s1 = "Right 10, ";
                if((double)j3 > 12.5D && (double)j3 <= 17.5D)
                    s1 = "Right 15, ";
                if((double)j3 > 17.5D && j3 <= 25)
                    s1 = "Right 20, ";
                if(j3 > 25 && j3 <= 35)
                    s1 = "Right 30, ";
                if(j3 > 35 && j3 <= 45)
                    s1 = "Right 40, ";
                if(j3 > 45 && j3 <= 60)
                    s1 = "Turn right, ";
                if(j3 > 355)
                    s1 = "Dead ahead, ";
                if(j3 <= 355 && (double)j3 >= 352.5D)
                    s1 = "Left 5, ";
                if((double)j3 < 352.5D && (double)j3 >= 347.5D)
                    s1 = "Left 10, ";
                if((double)j3 < 347.5D && (double)j3 >= 342.5D)
                    s1 = "Left 15, ";
                if((double)j3 < 342.5D && j3 >= 335)
                    s1 = "Left 20, ";
                if(j3 < 335 && j3 >= 325)
                    s1 = "Left 30, ";
                if(j3 < 325 && j3 >= 315)
                    s1 = "Left 40, ";
                if(j3 < 345 && j3 >= 300)
                    s1 = "Turn left, ";
                String s2 = "  ";
                if(k2 < -10)
                    s2 = "nose down";
                if(k2 >= -10 && k2 <= -5)
                    s2 = "down a bit";
                if(k2 > -5 && k2 < 5)
                    s2 = "level";
                if(k2 <= 10 && k2 >= 5)
                    s2 = "up a bit";
                if(k2 > 10)
                    s2 = "pull up";
                String s3 = "  ";
                if(j3 < 5)
                    s3 = "dead ahead, ";
                if(j3 >= 5 && (double)j3 <= 7.5D)
                    s3 = "right by 5\260, ";
                if((double)j3 > 7.5D && (double)j3 <= 12.5D)
                    s3 = "right by 10\260, ";
                if((double)j3 > 12.5D && (double)j3 <= 17.5D)
                    s3 = "right by 15\260, ";
                if((double)j3 > 17.5D && j3 <= 25)
                    s3 = "right by 20\260, ";
                if(j3 > 25 && j3 <= 35)
                    s3 = "right by 30\260, ";
                if(j3 > 35 && j3 <= 45)
                    s3 = "right by 40\260, ";
                if(j3 > 45 && j3 <= 60)
                    s3 = "off our right, ";
                if(j3 > 355)
                    s3 = "dead ahead, ";
                if(j3 <= 355 && (double)j3 >= 352.5D)
                    s3 = "left by 5\260, ";
                if((double)j3 < 352.5D && (double)j3 >= 347.5D)
                    s3 = "left by 10\260, ";
                if((double)j3 < 347.5D && (double)j3 >= 342.5D)
                    s3 = "left by 15\260, ";
                if((double)j3 < 342.5D && j3 >= 335)
                    s3 = "left by 20\260, ";
                if(j3 < 335 && j3 >= 325)
                    s3 = "left by 30\260, ";
                if(j3 < 325 && j3 >= 315)
                    s3 = "left by 40\260, ";
                if(j3 < 345 && j3 >= 300)
                    s3 = "off our left, ";
                if((double)i2 <= (double)k3 && (double)i2 > 7000D && k2 >= -20 && k2 <= 20 && Math.sqrt(i3 * i3) <= 60D)
                {
                    HUD.logCenter("                                          RP-21: Contact " + s3 + s + ", " + l2 + "m");
                    freq = 7;
                } else
                if((double)i2 <= (double)k3 && (double)i2 <= 7000D && (double)i2 >= 700D && k2 >= -20 && k2 <= 20 && Math.sqrt(i3 * i3) <= 40D)
                {
                    HUD.logCenter("                                          RP-21: Target Locked!: " + s1 + s2 + ", " + l2 + "m");
                    freq = 3;
                } else
                {
                    freq = 7;
                }
                setTimer(freq);
            }
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

    private void setAfterburner()
    {
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 18500D;
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 1.03D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.96999999999999997D && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 6000D;
        if(super.FM.getAltitude() > 1000F && (double)calculateMach() >= 1.03D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 4000D;
        if(super.FM.getAltitude() > 1500F && (double)calculateMach() >= 1.03D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 2000D;
        if(super.FM.getAltitude() > 2000F && (double)calculateMach() >= 1.03D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 2000D;
        if(super.FM.getAltitude() > 10500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 2500D;
        if(super.FM.getAltitude() > 11000F && (double)calculateMach() >= 2.0099999999999998D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
        if(super.FM.getAltitude() > 12000F && (double)calculateMach() >= 0.94999999999999996D && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 6000D;
        if(super.FM.getAltitude() > 12700F && (double)calculateMach() >= 1.99D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
        if(super.FM.getAltitude() > 13000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
        if(super.FM.getAltitude() > 14000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
        if(super.FM.getAltitude() > 14500F && (double)calculateMach() >= 1.9299999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
        if(super.FM.getAltitude() > 14500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
        if(super.FM.getAltitude() > 15000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
        if(super.FM.getAltitude() > 15500F && (double)calculateMach() >= 1.8200000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
        if(super.FM.getAltitude() > 16000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
        if(super.FM.getAltitude() > 16500F && (double)calculateMach() >= 1.75D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
        if(super.FM.getAltitude() > 17000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
        if(super.FM.getAltitude() > 17000F && (double)calculateMach() >= 1.7D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
        if(super.FM.getAltitude() > 17500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2500D;
        if(super.FM.getAltitude() > 17500F && (double)calculateMach() >= 1.6499999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
        if(super.FM.getAltitude() > 18000F && (double)calculateMach() >= 1.5900000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
        if(super.FM.getAltitude() > 18500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 5000D;
        if(super.FM.getAltitude() > 20000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.01F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 12000D;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, -50F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

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
    private static Actor hunted = null;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private int counter;
    private int freq;
    public float Timer1;
    public float Timer2;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.MIG_21PF.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG21");
        Property.set(class1, "meshName", "3DO/Plane/MiG-21PF/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-21PF.fmd:MIG21");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMIG_21PF.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 9, 2, 2, 2, 
            2, 2, 2, 2, 2, 9, 9, 3, 3, 9, 
            9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", 
            "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev06", "_ExternalDev07", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev08", 
            "_ExternalDev09", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", 
            "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", 
            "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", 
            "_ExternalRock34", "_ExternalRock35", "_ExternalRock36"
        });
        Aircraft.weaponsRegister(class1, "default", new String[53]);
        String as[] = new String[53];
        as[0] = "MGunNull 1";
        as[5] = "MiG21WingPylon 1";
        as[6] = "MiG21WingPylon 1";
        as[7] = "RocketGunK13A 1";
        as[8] = "RocketGunNull 1";
        as[9] = "RocketGunK13A 1";
        as[10] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketK13", as);
        String as1[] = new String[53];
        as1[0] = "MGunNull 1";
        as1[5] = "MiG21WingPylon 1";
        as1[6] = "MiG21WingPylon 1";
        as1[11] = "RocketGunK5M 1";
        as1[12] = "RocketGunNull 1";
        as1[13] = "RocketGunK5M 1";
        as1[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketK5M", as1);
        String as2[] = new String[53];
        as2[0] = "MGunNull 1";
        as2[5] = "MiG21WingPylon 1";
        as2[6] = "MiG21WingPylon 1";
        as2[11] = "RocketGunK5Mf 1";
        as2[12] = "RocketGunNull 1";
        as2[13] = "RocketGunK5Mf 1";
        as2[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketK5Mf", as2);
        String as3[] = new String[53];
        as3[0] = "MGunNull 1";
        as3[5] = "MiG21WingPylon 1";
        as3[6] = "MiG21WingPylon 1";
        as3[11] = "RocketGunR55 1";
        as3[12] = "RocketGunNull 1";
        as3[13] = "RocketGunR55 1";
        as3[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketR55", as3);
        String as4[] = new String[53];
        as4[0] = "MGunNull 1";
        as4[5] = "MiG21WingPylon 1";
        as4[6] = "MiG21WingPylon 1";
        as4[11] = "RocketGunR55f 1";
        as4[12] = "RocketGunNull 1";
        as4[13] = "RocketGunR55f 1";
        as4[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketR55f", as4);
        String as5[] = new String[52];
        as5[0] = "MGunNull 1";
        as5[5] = "MiG21WingPylon 1";
        as5[6] = "MiG21WingPylon 1";
        as5[16] = "BombGunFAB100 1";
        as5[17] = "BombGunFAB100 1";
        Aircraft.weaponsRegister(class1, "2xFAB100", as5);
        String as6[] = new String[52];
        as6[0] = "MGunNull 1";
        as6[5] = "MiG21WingPylon 1";
        as6[6] = "MiG21WingPylon 1";
        as6[16] = "BombGunFAB250m46 1";
        as6[17] = "BombGunFAB250m46 1";
        Aircraft.weaponsRegister(class1, "2xFAB250", as6);
        String as7[] = new String[52];
        as7[0] = "MGunNull 1";
        as7[5] = "MiG21WingPylon 1";
        as7[6] = "MiG21WingPylon 1";
        as7[16] = "BombGunZB360 1";
        as7[17] = "BombGunZB360 1";
        Aircraft.weaponsRegister(class1, "2xZB360", as7);
        Aircraft.weaponsRegister(class1, "2xUBI16", new String[] {
            "MGunNull 1", 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, "PylonUB16 1", "PylonUB16 1", 
            "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
            "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
            "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
            "RocketGunS5 1", "RocketGunS5 1"
        });
        String as8[] = new String[53];
        as8[0] = "MGunNull 1";
        as8[5] = "MiG21WingPylon 1";
        as8[6] = "MiG21WingPylon 1";
        as8[11] = "RocketGunS24 1";
        as8[12] = "RocketGunNull 1";
        as8[13] = "RocketGunS24 1";
        as8[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketS24", as8);
        String as9[] = new String[53];
        as9[3] = "MiG21Pylon 1";
        as9[4] = "FuelTankGun_PT490 1";
        Aircraft.weaponsRegister(class1, "droptank", as9);
        String as10[] = new String[53];
        as10[0] = "MGunNull 1";
        as10[3] = "MiG21Pylon 1";
        as10[4] = "FuelTankGun_PT490 1";
        as10[5] = "MiG21WingPylon 1";
        as10[6] = "MiG21WingPylon 1";
        as10[7] = "RocketGunK13A 1";
        as10[8] = "RocketGunNull 1";
        as10[9] = "RocketGunK13A 1";
        as10[10] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketK13", as10);
        String as11[] = new String[53];
        as11[0] = "MGunNull 1";
        as11[3] = "MiG21Pylon 1";
        as11[4] = "FuelTankGun_PT490 1";
        as11[5] = "MiG21WingPylon 1";
        as11[6] = "MiG21WingPylon 1";
        as11[11] = "RocketGunK5M 1";
        as11[12] = "RocketGunNull 1";
        as11[13] = "RocketGunK5M 1";
        as11[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketK5M", as11);
        String as12[] = new String[53];
        as12[0] = "MGunNull 1";
        as12[3] = "MiG21Pylon 1";
        as12[4] = "FuelTankGun_PT490 1";
        as12[5] = "MiG21WingPylon 1";
        as12[6] = "MiG21WingPylon 1";
        as12[11] = "RocketGunK5Mf 1";
        as12[12] = "RocketGunNull 1";
        as12[13] = "RocketGunK5Mf 1";
        as12[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketK5Mf", as12);
        String as13[] = new String[53];
        as13[0] = "MGunNull 1";
        as13[3] = "MiG21Pylon 1";
        as13[4] = "FuelTankGun_PT490 1";
        as13[5] = "MiG21WingPylon 1";
        as13[6] = "MiG21WingPylon 1";
        as13[11] = "RocketGunR55 1";
        as13[12] = "RocketGunNull 1";
        as13[13] = "RocketGunR55 1";
        as13[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketR55", as13);
        String as14[] = new String[53];
        as14[0] = "MGunNull 1";
        as14[3] = "MiG21Pylon 1";
        as14[4] = "FuelTankGun_PT490 1";
        as14[5] = "MiG21WingPylon 1";
        as14[6] = "MiG21WingPylon 1";
        as14[11] = "RocketGunR55f 1";
        as14[12] = "RocketGunNull 1";
        as14[13] = "RocketGunR55f 1";
        as14[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketR55f", as14);
        String as15[] = new String[53];
        as15[0] = "MGunNull 1";
        as15[3] = "MiG21Pylon 1";
        as15[4] = "FuelTankGun_PT490 1";
        as15[5] = "MiG21WingPylon 1";
        as15[6] = "MiG21WingPylon 1";
        as15[17] = "BombGunFAB100 1";
        as15[18] = "BombGunFAB100 1";
        Aircraft.weaponsRegister(class1, "dt_2xFAB100", as15);
        String as16[] = new String[53];
        as16[0] = "MGunNull 1";
        as16[3] = "MiG21Pylon 1";
        as16[4] = "FuelTankGun_PT490 1";
        as16[5] = "MiG21WingPylon 1";
        as16[6] = "MiG21WingPylon 1";
        as16[17] = "BombGunFAB250m46 1";
        as16[18] = "BombGunFAB250m46 1";
        Aircraft.weaponsRegister(class1, "dt_2xFAB250", as16);
        String as17[] = new String[53];
        as17[0] = "MGunNull 1";
        as17[3] = "MiG21Pylon 1";
        as17[4] = "FuelTankGun_PT490 1";
        as17[5] = "MiG21WingPylon 1";
        as17[6] = "MiG21WingPylon 1";
        as17[17] = "BombGunZB360 1";
        as17[18] = "BombGunZB360 1";
        Aircraft.weaponsRegister(class1, "dt_2xZB360", as17);
        Aircraft.weaponsRegister(class1, "dt_2xUBI16", new String[] {
            "MGunNull 1", 0, 0, "MiG21Pylon 1", "FuelTankGun_PT490 1", "MiG21WingPylon 1", "MiG21WingPylon 1", 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, "PylonUB16 1", 
            "PylonUB16 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
            "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
            "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1", 
            "RocketGunS5 1", "RocketGunS5 1", "RocketGunS5 1"
        });
        String as18[] = new String[53];
        as18[0] = "MGunNull 1";
        as18[3] = "MiG21Pylon 1";
        as18[4] = "FuelTankGun_PT490 1";
        as18[5] = "MiG21WingPylon 1";
        as18[6] = "MiG21WingPylon 1";
        as18[11] = "RocketGunS24 1";
        as18[12] = "RocketGunNull 1";
        as18[13] = "RocketGunS24 1";
        as18[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketS24", as18);
        String as19[] = new String[54];
        as19[3] = "MiG21Pylon 1";
        as19[4] = "FuelTankGun_PT490 1";
        as19[5] = "MiG21WingPylon 1";
        as19[6] = "MiG21WingPylon 1";
        as19[15] = "FuelTankGun_PT490 1";
        as19[16] = "FuelTankGun_PT490 1";
        Aircraft.weaponsRegister(class1, "3xdroptank", as19);
        Aircraft.weaponsRegister(class1, "none", new String[53]);
    }
}
