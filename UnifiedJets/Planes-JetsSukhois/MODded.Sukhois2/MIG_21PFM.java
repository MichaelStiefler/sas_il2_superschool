// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MIG_21PFM.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.util.HashMapExt;
import java.util.Map;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            MIG_21, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, 
//            PaintSchemeFMParMiG21, Aircraft, Cockpit, Chute, 
//            NetAircraft, EjectionSeat

public class MIG_21PFM extends MIG_21
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public MIG_21PFM()
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
        super.bHasSK1Seat = false;
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        guidedMissileUtils.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).CT.bHasRefuelControl = true;
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
            ((Actor) (chute)).pos.setRel(new Point3d(-5D, 0.0D, 0.59999999999999998D), new Orient(0.0F, 90F, 0.0F));
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

    protected void moveVarWing(float f)
    {
        hierMesh().chunkSetAngles("AirbrakeL", -45F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("AirbrakeR", 45F * f, 0.0F, 0.0F);
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

            public void doAction(Object paramObject)
            {
                Aircraft localAircraft = (Aircraft)paramObject;
                if(Actor.isValid(localAircraft))
                {
                    Loc localLoc1 = new Loc();
                    Loc localLoc2 = new Loc();
                    Vector3d localVector3d = new Vector3d(0.0D, 0.0D, 40D);
                    HookNamed localHookNamed = new HookNamed(localAircraft, "_ExternalSeat01");
                    ((Actor) (localAircraft)).pos.getAbs(localLoc2);
                    localHookNamed.computePos(localAircraft, localLoc2, localLoc1);
                    localLoc1.transform(localVector3d);
                    localVector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).x;
                    localVector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).y;
                    localVector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).z;
                    new EjectionSeat(10, localLoc1, localVector3d, localAircraft);
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
    private int freq;
    public float Timer1;
    public float Timer2;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.MIG_21PFM.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG21");
        Property.set(class1, "meshName", "3DO/Plane/MiG-21PFM/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-21PFM.fmd:MIG21");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMIG_21PFM.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 9, 2, 2, 2, 
            2, 2, 2, 2, 2, 9, 9, 3, 3, 9, 
            9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 4
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", 
            "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev06", "_ExternalDev07", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev08", 
            "_ExternalDev09", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", 
            "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", 
            "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", 
            "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37"
        });
        Aircraft.weaponsRegister(class1, "default", new String[54]);
        String as[] = new String[54];
        as[0] = "MGunNull 1";
        as[5] = "MiG21WingPylon 1";
        as[6] = "MiG21WingPylon 1";
        as[7] = "RocketGunK13A 1";
        as[8] = "RocketGunNull 1";
        as[9] = "RocketGunK13A 1";
        as[10] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketK13", as);
        String as1[] = new String[54];
        as1[0] = "MGunNull 1";
        as1[5] = "MiG21WingPylon 1";
        as1[6] = "MiG21WingPylon 1";
        as1[11] = "RocketGunK5M 1";
        as1[12] = "RocketGunNull 1";
        as1[13] = "RocketGunK5M 1";
        as1[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketK5M", as1);
        String as2[] = new String[54];
        as2[0] = "MGunNull 1";
        as2[5] = "MiG21WingPylon 1";
        as2[6] = "MiG21WingPylon 1";
        as2[11] = "RocketGunK5Mf 1";
        as2[12] = "RocketGunNull 1";
        as2[13] = "RocketGunK5Mf 1";
        as2[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketK5Mf", as2);
        String as3[] = new String[54];
        as3[0] = "MGunNull 1";
        as3[5] = "MiG21WingPylon 1";
        as3[6] = "MiG21WingPylon 1";
        as3[11] = "RocketGunR55 1";
        as3[12] = "RocketGunNull 1";
        as3[13] = "RocketGunR55 1";
        as3[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketR55", as3);
        String as4[] = new String[54];
        as4[0] = "MGunNull 1";
        as4[5] = "MiG21WingPylon 1";
        as4[6] = "MiG21WingPylon 1";
        as4[11] = "RocketGunR55f 1";
        as4[12] = "RocketGunNull 1";
        as4[13] = "RocketGunR55f 1";
        as4[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketR55f", as4);
        Aircraft.weaponsRegister(class1, "rocketKh66", new String[] {
            "MGunNull 1", 0, 0, 0, "MiG21Pylon 1", 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, "RocketGunKh66 1"
        });
        Aircraft.weaponsRegister(class1, "rocketKh66+K13", new String[] {
            "MGunNull 1", 0, 0, 0, "MiG21Pylon 1", "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
            "RocketGunNull 1", 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, "RocketGunKh66 1"
        });
        String as5[] = new String[53];
        as5[0] = "MGunNull 1";
        as5[5] = "MiG21WingPylon 1";
        as5[6] = "MiG21WingPylon 1";
        as5[16] = "BombGunFAB100 1";
        as5[17] = "BombGunFAB100 1";
        Aircraft.weaponsRegister(class1, "2xFAB100", as5);
        String as6[] = new String[53];
        as6[0] = "MGunNull 1";
        as6[5] = "MiG21WingPylon 1";
        as6[6] = "MiG21WingPylon 1";
        as6[16] = "BombGunFAB250m46 1";
        as6[17] = "BombGunFAB250m46 1";
        Aircraft.weaponsRegister(class1, "2xFAB250", as6);
        String as7[] = new String[53];
        as7[0] = "MGunNull 1";
        as7[5] = "MiG21WingPylon 1";
        as7[6] = "MiG21WingPylon 1";
        as7[16] = "BombGunZB360 1";
        as7[17] = "BombGunZB360 1";
        Aircraft.weaponsRegister(class1, "2xZB360", as7);
        String as8[] = new String[53];
        as8[0] = "MGunNull 1";
        as8[18] = "PylonUB16 1";
        as8[19] = "PylonUB16 1";
        as8[20] = "RocketGunS5 1";
        as8[21] = "RocketGunS5 1";
        as8[22] = "RocketGunS5 1";
        as8[23] = "RocketGunS5 1";
        as8[24] = "RocketGunS5 1";
        as8[25] = "RocketGunS5 1";
        as8[26] = "RocketGunS5 1";
        as8[27] = "RocketGunS5 1";
        as8[28] = "RocketGunS5 1";
        as8[29] = "RocketGunS5 1";
        as8[30] = "RocketGunS5 1";
        as8[31] = "RocketGunS5 1";
        as8[32] = "RocketGunS5 1";
        as8[33] = "RocketGunS5 1";
        as8[34] = "RocketGunS5 1";
        as8[35] = "RocketGunS5 1";
        as8[36] = "RocketGunS5 1";
        as8[37] = "RocketGunS5 1";
        as8[38] = "RocketGunS5 1";
        as8[39] = "RocketGunS5 1";
        as8[40] = "RocketGunS5 1";
        as8[41] = "RocketGunS5 1";
        as8[42] = "RocketGunS5 1";
        as8[43] = "RocketGunS5 1";
        as8[44] = "RocketGunS5 1";
        as8[45] = "RocketGunS5 1";
        as8[46] = "RocketGunS5 1";
        as8[47] = "RocketGunS5 1";
        as8[48] = "RocketGunS5 1";
        as8[49] = "RocketGunS5 1";
        as8[50] = "RocketGunS5 1";
        as8[51] = "RocketGunS5 1";
        Aircraft.weaponsRegister(class1, "2xUBI16", as8);
        String as9[] = new String[54];
        as9[0] = "MGunNull 1";
        as9[5] = "MiG21WingPylon 1";
        as9[6] = "MiG21WingPylon 1";
        as9[11] = "RocketGunS24 1";
        as9[12] = "RocketGunNull 1";
        as9[13] = "RocketGunS24 1";
        as9[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketS24", as9);
        String as10[] = new String[54];
        as10[3] = "MiG21Pylon 1";
        as10[4] = "FuelTankGun_PT490 1";
        Aircraft.weaponsRegister(class1, "droptank", as10);
        String as11[] = new String[54];
        as11[0] = "MGunNull 1";
        as11[3] = "MiG21Pylon 1";
        as11[4] = "FuelTankGun_PT490 1";
        as11[5] = "MiG21WingPylon 1";
        as11[6] = "MiG21WingPylon 1";
        as11[7] = "RocketGunK13A 1";
        as11[8] = "RocketGunNull 1";
        as11[9] = "RocketGunK13A 1";
        as11[10] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketK13", as11);
        String as12[] = new String[54];
        as12[0] = "MGunNull 1";
        as12[3] = "MiG21Pylon 1";
        as12[4] = "FuelTankGun_PT490 1";
        as12[5] = "MiG21WingPylon 1";
        as12[6] = "MiG21WingPylon 1";
        as12[11] = "RocketGunK5M 1";
        as12[12] = "RocketGunNull 1";
        as12[13] = "RocketGunK5M 1";
        as12[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketK5M", as12);
        String as13[] = new String[54];
        as13[0] = "MGunNull 1";
        as13[3] = "MiG21Pylon 1";
        as13[4] = "FuelTankGun_PT490 1";
        as13[5] = "MiG21WingPylon 1";
        as13[6] = "MiG21WingPylon 1";
        as13[11] = "RocketGunK5Mf 1";
        as13[12] = "RocketGunNull 1";
        as13[13] = "RocketGunK5Mf 1";
        as13[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketK5Mf", as13);
        String as14[] = new String[54];
        as14[0] = "MGunNull 1";
        as14[3] = "MiG21Pylon 1";
        as14[4] = "FuelTankGun_PT490 1";
        as14[5] = "MiG21WingPylon 1";
        as14[6] = "MiG21WingPylon 1";
        as14[11] = "RocketGunR55 1";
        as14[12] = "RocketGunNull 1";
        as14[13] = "RocketGunR55 1";
        as14[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketR55", as14);
        String as15[] = new String[54];
        as15[0] = "MGunNull 1";
        as15[3] = "MiG21Pylon 1";
        as15[4] = "FuelTankGun_PT490 1";
        as15[5] = "MiG21WingPylon 1";
        as15[6] = "MiG21WingPylon 1";
        as15[11] = "RocketGunR55f 1";
        as15[12] = "RocketGunNull 1";
        as15[13] = "RocketGunR55f 1";
        as15[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketR55f", as15);
        String as16[] = new String[54];
        as16[0] = "MGunNull 1";
        as16[3] = "MiG21Pylon 1";
        as16[4] = "FuelTankGun_PT490 1";
        as16[5] = "MiG21WingPylon 1";
        as16[6] = "MiG21WingPylon 1";
        as16[17] = "BombGunFAB100 1";
        as16[18] = "BombGunFAB100 1";
        Aircraft.weaponsRegister(class1, "dt_2xFAB100", as16);
        String as17[] = new String[54];
        as17[0] = "MGunNull 1";
        as17[3] = "MiG21Pylon 1";
        as17[4] = "FuelTankGun_PT490 1";
        as17[5] = "MiG21WingPylon 1";
        as17[6] = "MiG21WingPylon 1";
        as17[17] = "BombGunFAB250m46 1";
        as17[18] = "BombGunFAB250m46 1";
        Aircraft.weaponsRegister(class1, "dt_2xFAB250", as17);
        String as18[] = new String[54];
        as18[0] = "MGunNull 1";
        as18[3] = "MiG21Pylon 1";
        as18[4] = "FuelTankGun_PT490 1";
        as18[5] = "MiG21WingPylon 1";
        as18[6] = "MiG21WingPylon 1";
        as18[17] = "BombGunZB360 1";
        as18[18] = "BombGunZB360 1";
        Aircraft.weaponsRegister(class1, "dt_2xZB360", as18);
        String as19[] = new String[54];
        as19[0] = "MGunNull 1";
        as19[3] = "MiG21Pylon 1";
        as19[4] = "FuelTankGun_PT490 1";
        as19[5] = "MiG21WingPylon 1";
        as19[6] = "MiG21WingPylon 1";
        as19[19] = "PylonUB16 1";
        as19[20] = "PylonUB16 1";
        as19[21] = "RocketGunS5 1";
        as19[22] = "RocketGunS5 1";
        as19[23] = "RocketGunS5 1";
        as19[24] = "RocketGunS5 1";
        as19[25] = "RocketGunS5 1";
        as19[26] = "RocketGunS5 1";
        as19[27] = "RocketGunS5 1";
        as19[28] = "RocketGunS5 1";
        as19[29] = "RocketGunS5 1";
        as19[30] = "RocketGunS5 1";
        as19[31] = "RocketGunS5 1";
        as19[32] = "RocketGunS5 1";
        as19[33] = "RocketGunS5 1";
        as19[34] = "RocketGunS5 1";
        as19[35] = "RocketGunS5 1";
        as19[36] = "RocketGunS5 1";
        as19[37] = "RocketGunS5 1";
        as19[38] = "RocketGunS5 1";
        as19[39] = "RocketGunS5 1";
        as19[40] = "RocketGunS5 1";
        as19[41] = "RocketGunS5 1";
        as19[42] = "RocketGunS5 1";
        as19[43] = "RocketGunS5 1";
        as19[44] = "RocketGunS5 1";
        as19[45] = "RocketGunS5 1";
        as19[46] = "RocketGunS5 1";
        as19[47] = "RocketGunS5 1";
        as19[48] = "RocketGunS5 1";
        as19[49] = "RocketGunS5 1";
        as19[50] = "RocketGunS5 1";
        as19[51] = "RocketGunS5 1";
        as19[52] = "RocketGunS5 1";
        Aircraft.weaponsRegister(class1, "dt_2xUBI16", as19);
        String as20[] = new String[54];
        as20[0] = "MGunNull 1";
        as20[3] = "MiG21Pylon 1";
        as20[4] = "FuelTankGun_PT490 1";
        as20[5] = "MiG21WingPylon 1";
        as20[6] = "MiG21WingPylon 1";
        as20[11] = "RocketGunS24 1";
        as20[12] = "RocketGunNull 1";
        as20[13] = "RocketGunS24 1";
        as20[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketS24", as20);
        String as21[] = new String[54];
        as21[0] = "MGunGSh23Ls 100";
        as21[1] = "MGunGSh23Ls 100";
        as21[2] = "PylonGP9 1";
        Aircraft.weaponsRegister(class1, "gunpod", as21);
        String as22[] = new String[54];
        as22[0] = "MGunGSh23Ls 100";
        as22[1] = "MGunGSh23Ls 100";
        as22[2] = "PylonGP9 1";
        as22[5] = "MiG21WingPylon 1";
        as22[6] = "MiG21WingPylon 1";
        as22[7] = "RocketGunK13A 1";
        as22[8] = "RocketGunNull 1";
        as22[9] = "RocketGunK13A 1";
        as22[10] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketK13", as22);
        String as23[] = new String[54];
        as23[0] = "MGunGSh23Ls 100";
        as23[1] = "MGunGSh23Ls 100";
        as23[2] = "PylonGP9 1";
        as23[5] = "MiG21WingPylon 1";
        as23[6] = "MiG21WingPylon 1";
        as23[11] = "RocketGunK5M 1";
        as23[12] = "RocketGunNull 1";
        as23[13] = "RocketGunK5M 1";
        as23[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketK5M", as23);
        String as24[] = new String[54];
        as24[0] = "MGunGSh23Ls 100";
        as24[1] = "MGunGSh23Ls 100";
        as24[2] = "PylonGP9 1";
        as24[5] = "MiG21WingPylon 1";
        as24[6] = "MiG21WingPylon 1";
        as24[11] = "RocketGunK5Mf 1";
        as24[12] = "RocketGunNull 1";
        as24[13] = "RocketGunK5Mf 1";
        as24[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketK5Mf", as24);
        String as25[] = new String[54];
        as25[0] = "MGunGSh23Ls 100";
        as25[1] = "MGunGSh23Ls 100";
        as25[2] = "PylonGP9 1";
        as25[5] = "MiG21WingPylon 1";
        as25[6] = "MiG21WingPylon 1";
        as25[11] = "RocketGunR55 1";
        as25[12] = "RocketGunNull 1";
        as25[13] = "RocketGunR55 1";
        as25[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketR55", as25);
        String as26[] = new String[54];
        as26[0] = "MGunGSh23Ls 100";
        as26[1] = "MGunGSh23Ls 100";
        as26[2] = "PylonGP9 1";
        as26[5] = "MiG21WingPylon 1";
        as26[6] = "MiG21WingPylon 1";
        as26[11] = "RocketGunR55f 1";
        as26[12] = "RocketGunNull 1";
        as26[13] = "RocketGunR55f 1";
        as26[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketR55f", as26);
        String as27[] = new String[54];
        as27[0] = "MGunGSh23Ls 100";
        as27[1] = "MGunGSh23Ls 100";
        as27[2] = "PylonGP9 1";
        as27[5] = "MiG21WingPylon 1";
        as27[6] = "MiG21WingPylon 1";
        as27[17] = "BombGunFAB100 1";
        as27[18] = "BombGunFAB100 1";
        Aircraft.weaponsRegister(class1, "gp_2xFAB100", as27);
        String as28[] = new String[54];
        as28[0] = "MGunGSh23Ls 100";
        as28[1] = "MGunGSh23Ls 100";
        as28[2] = "PylonGP9 1";
        as28[5] = "MiG21WingPylon 1";
        as28[6] = "MiG21WingPylon 1";
        as28[17] = "BombGunFAB250m46 1";
        as28[18] = "BombGunFAB250m46 1";
        Aircraft.weaponsRegister(class1, "gp_2xFAB250", as28);
        String as29[] = new String[54];
        as29[0] = "MGunGSh23Ls 100";
        as29[1] = "MGunGSh23Ls 100";
        as29[2] = "PylonGP9 1";
        as29[5] = "MiG21WingPylon 1";
        as29[6] = "MiG21WingPylon 1";
        as29[17] = "BombGunZB360 1";
        as29[18] = "BombGunZB360 1";
        Aircraft.weaponsRegister(class1, "gp_2xZB360", as29);
        String as30[] = new String[54];
        as30[0] = "MGunGSh23Ls 100";
        as30[1] = "MGunGSh23Ls 100";
        as30[2] = "PylonGP9 1";
        as30[5] = "MiG21WingPylon 1";
        as30[6] = "MiG21WingPylon 1";
        as30[15] = "FuelTankGun_PT490 1";
        as30[16] = "FuelTankGun_PT490 1";
        Aircraft.weaponsRegister(class1, "gp_2xdroptank", as30);
        String as31[] = new String[54];
        as31[0] = "MGunNull 1";
        as31[3] = "MiG21Pylon 1";
        as31[4] = "FuelTankGun_PT490 1";
        as31[5] = "MiG21WingPylon 1";
        as31[6] = "MiG21WingPylon 1";
        as31[19] = "PylonUB16 1";
        as31[20] = "PylonUB16 1";
        as31[21] = "RocketGunS5 1";
        as31[22] = "RocketGunS5 1";
        as31[23] = "RocketGunS5 1";
        as31[24] = "RocketGunS5 1";
        as31[25] = "RocketGunS5 1";
        as31[26] = "RocketGunS5 1";
        as31[27] = "RocketGunS5 1";
        as31[28] = "RocketGunS5 1";
        as31[29] = "RocketGunS5 1";
        as31[30] = "RocketGunS5 1";
        as31[31] = "RocketGunS5 1";
        as31[32] = "RocketGunS5 1";
        as31[33] = "RocketGunS5 1";
        as31[34] = "RocketGunS5 1";
        as31[35] = "RocketGunS5 1";
        as31[36] = "RocketGunS5 1";
        as31[37] = "RocketGunS5 1";
        as31[38] = "RocketGunS5 1";
        as31[39] = "RocketGunS5 1";
        as31[40] = "RocketGunS5 1";
        as31[41] = "RocketGunS5 1";
        as31[42] = "RocketGunS5 1";
        as31[43] = "RocketGunS5 1";
        as31[44] = "RocketGunS5 1";
        as31[45] = "RocketGunS5 1";
        as31[46] = "RocketGunS5 1";
        as31[47] = "RocketGunS5 1";
        as31[48] = "RocketGunS5 1";
        as31[49] = "RocketGunS5 1";
        as31[50] = "RocketGunS5 1";
        as31[51] = "RocketGunS5 1";
        as31[52] = "RocketGunS5 1";
        Aircraft.weaponsRegister(class1, "gp_2xUBI16", as31);
        String as32[] = new String[54];
        as32[0] = "MGunGSh23Ls 100";
        as32[1] = "MGunGSh23Ls 100";
        as32[2] = "PylonGP9 1";
        as32[5] = "MiG21WingPylon 1";
        as32[6] = "MiG21WingPylon 1";
        as32[11] = "RocketGunS24 1";
        as32[12] = "RocketGunNull 1";
        as32[13] = "RocketGunS24 1";
        as32[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketS24", as32);
        String as33[] = new String[54];
        as33[3] = "MiG21Pylon 1";
        as33[4] = "FuelTankGun_PT490 1";
        as33[5] = "MiG21WingPylon 1";
        as33[6] = "MiG21WingPylon 1";
        as33[15] = "FuelTankGun_PT490 1";
        as33[16] = "FuelTankGun_PT490 1";
        Aircraft.weaponsRegister(class1, "3xdroptank", as33);
        Aircraft.weaponsRegister(class1, "default_RATO", new String[54]);
        String as34[] = new String[54];
        as34[0] = "MGunNull 1";
        as34[5] = "MiG21WingPylon 1";
        as34[6] = "MiG21WingPylon 1";
        as34[7] = "RocketGunK13A 1";
        as34[8] = "RocketGunNull 1";
        as34[9] = "RocketGunK13A 1";
        as34[10] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketK13_RATO", as34);
        String as35[] = new String[54];
        as35[0] = "MGunNull 1";
        as35[5] = "MiG21WingPylon 1";
        as35[6] = "MiG21WingPylon 1";
        as35[11] = "RocketGunK5M 1";
        as35[12] = "RocketGunNull 1";
        as35[13] = "RocketGunK5M 1";
        as35[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketK5M_RATO", as35);
        String as36[] = new String[54];
        as36[0] = "MGunNull 1";
        as36[5] = "MiG21WingPylon 1";
        as36[6] = "MiG21WingPylon 1";
        as36[11] = "RocketGunK5Mf 1";
        as36[12] = "RocketGunNull 1";
        as36[13] = "RocketGunK5Mf 1";
        as36[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketK5Mf_RATO", as36);
        String as37[] = new String[54];
        as37[0] = "MGunNull 1";
        as37[5] = "MiG21WingPylon 1";
        as37[6] = "MiG21WingPylon 1";
        as37[11] = "RocketGunR55 1";
        as37[12] = "RocketGunNull 1";
        as37[13] = "RocketGunR55 1";
        as37[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketR55_RATO", as37);
        String as38[] = new String[54];
        as38[0] = "MGunNull 1";
        as38[5] = "MiG21WingPylon 1";
        as38[6] = "MiG21WingPylon 1";
        as38[11] = "RocketGunR55f 1";
        as38[12] = "RocketGunNull 1";
        as38[13] = "RocketGunR55f 1";
        as38[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketR55f_RATO", as38);
        Aircraft.weaponsRegister(class1, "rocketKh66_RATO", new String[] {
            "MGunNull 1", 0, 0, 0, "MiG21Pylon 1", 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, "RocketGunKh66 1"
        });
        Aircraft.weaponsRegister(class1, "rocketKh66+K13_RATO", new String[] {
            "MGunNull 1", 0, 0, 0, "MiG21Pylon 1", "MiG21WingPylon 1", "MiG21WingPylon 1", "RocketGunK13A 1", "RocketGunNull 1", "RocketGunK13A 1", 
            "RocketGunNull 1", 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, "RocketGunKh66 1"
        });
        String as39[] = new String[53];
        as39[0] = "MGunNull 1";
        as39[5] = "MiG21WingPylon 1";
        as39[6] = "MiG21WingPylon 1";
        as39[16] = "BombGunFAB100 1";
        as39[17] = "BombGunFAB100 1";
        Aircraft.weaponsRegister(class1, "2xFAB100_RATO", as39);
        String as40[] = new String[53];
        as40[0] = "MGunNull 1";
        as40[5] = "MiG21WingPylon 1";
        as40[6] = "MiG21WingPylon 1";
        as40[16] = "BombGunFAB250m46 1";
        as40[17] = "BombGunFAB250m46 1";
        Aircraft.weaponsRegister(class1, "2xFAB250_RATO", as40);
        String as41[] = new String[53];
        as41[0] = "MGunNull 1";
        as41[5] = "MiG21WingPylon 1";
        as41[6] = "MiG21WingPylon 1";
        as41[16] = "BombGunZB360 1";
        as41[17] = "BombGunZB360 1";
        Aircraft.weaponsRegister(class1, "2xZB360_RATO", as41);
        String as42[] = new String[53];
        as42[0] = "MGunNull 1";
        as42[18] = "PylonUB16 1";
        as42[19] = "PylonUB16 1";
        as42[20] = "RocketGunS5 1";
        as42[21] = "RocketGunS5 1";
        as42[22] = "RocketGunS5 1";
        as42[23] = "RocketGunS5 1";
        as42[24] = "RocketGunS5 1";
        as42[25] = "RocketGunS5 1";
        as42[26] = "RocketGunS5 1";
        as42[27] = "RocketGunS5 1";
        as42[28] = "RocketGunS5 1";
        as42[29] = "RocketGunS5 1";
        as42[30] = "RocketGunS5 1";
        as42[31] = "RocketGunS5 1";
        as42[32] = "RocketGunS5 1";
        as42[33] = "RocketGunS5 1";
        as42[34] = "RocketGunS5 1";
        as42[35] = "RocketGunS5 1";
        as42[36] = "RocketGunS5 1";
        as42[37] = "RocketGunS5 1";
        as42[38] = "RocketGunS5 1";
        as42[39] = "RocketGunS5 1";
        as42[40] = "RocketGunS5 1";
        as42[41] = "RocketGunS5 1";
        as42[42] = "RocketGunS5 1";
        as42[43] = "RocketGunS5 1";
        as42[44] = "RocketGunS5 1";
        as42[45] = "RocketGunS5 1";
        as42[46] = "RocketGunS5 1";
        as42[47] = "RocketGunS5 1";
        as42[48] = "RocketGunS5 1";
        as42[49] = "RocketGunS5 1";
        as42[50] = "RocketGunS5 1";
        as42[51] = "RocketGunS5 1";
        Aircraft.weaponsRegister(class1, "2xUBI16_RATO", as42);
        String as43[] = new String[54];
        as43[0] = "MGunNull 1";
        as43[5] = "MiG21WingPylon 1";
        as43[6] = "MiG21WingPylon 1";
        as43[11] = "RocketGunS24 1";
        as43[12] = "RocketGunNull 1";
        as43[13] = "RocketGunS24 1";
        as43[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "rocketS24_RATO", as43);
        String as44[] = new String[54];
        as44[3] = "MiG21Pylon 1";
        as44[4] = "FuelTankGun_PT490 1";
        Aircraft.weaponsRegister(class1, "droptank_RATO", as44);
        String as45[] = new String[54];
        as45[0] = "MGunNull 1";
        as45[3] = "MiG21Pylon 1";
        as45[4] = "FuelTankGun_PT490 1";
        as45[5] = "MiG21WingPylon 1";
        as45[6] = "MiG21WingPylon 1";
        as45[7] = "RocketGunK13A 1";
        as45[8] = "RocketGunNull 1";
        as45[9] = "RocketGunK13A 1";
        as45[10] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketK13_RATO", as45);
        String as46[] = new String[54];
        as46[0] = "MGunNull 1";
        as46[3] = "MiG21Pylon 1";
        as46[4] = "FuelTankGun_PT490 1";
        as46[5] = "MiG21WingPylon 1";
        as46[6] = "MiG21WingPylon 1";
        as46[11] = "RocketGunK5M 1";
        as46[12] = "RocketGunNull 1";
        as46[13] = "RocketGunK5M 1";
        as46[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketK5M_RATO", as46);
        String as47[] = new String[54];
        as47[0] = "MGunNull 1";
        as47[3] = "MiG21Pylon 1";
        as47[4] = "FuelTankGun_PT490 1";
        as47[5] = "MiG21WingPylon 1";
        as47[6] = "MiG21WingPylon 1";
        as47[11] = "RocketGunK5Mf 1";
        as47[12] = "RocketGunNull 1";
        as47[13] = "RocketGunK5Mf 1";
        as47[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketK5Mf_RATO", as47);
        String as48[] = new String[54];
        as48[0] = "MGunNull 1";
        as48[3] = "MiG21Pylon 1";
        as48[4] = "FuelTankGun_PT490 1";
        as48[5] = "MiG21WingPylon 1";
        as48[6] = "MiG21WingPylon 1";
        as48[11] = "RocketGunR55 1";
        as48[12] = "RocketGunNull 1";
        as48[13] = "RocketGunR55 1";
        as48[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketR55_RATO", as48);
        String as49[] = new String[54];
        as49[0] = "MGunNull 1";
        as49[3] = "MiG21Pylon 1";
        as49[4] = "FuelTankGun_PT490 1";
        as49[5] = "MiG21WingPylon 1";
        as49[6] = "MiG21WingPylon 1";
        as49[11] = "RocketGunR55f 1";
        as49[12] = "RocketGunNull 1";
        as49[13] = "RocketGunR55f 1";
        as49[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketR55f_RATO", as49);
        String as50[] = new String[54];
        as50[0] = "MGunNull 1";
        as50[3] = "MiG21Pylon 1";
        as50[4] = "FuelTankGun_PT490 1";
        as50[5] = "MiG21WingPylon 1";
        as50[6] = "MiG21WingPylon 1";
        as50[17] = "BombGunFAB100 1";
        as50[18] = "BombGunFAB100 1";
        Aircraft.weaponsRegister(class1, "dt_2xFAB100_RATO", as50);
        String as51[] = new String[54];
        as51[0] = "MGunNull 1";
        as51[3] = "MiG21Pylon 1";
        as51[4] = "FuelTankGun_PT490 1";
        as51[5] = "MiG21WingPylon 1";
        as51[6] = "MiG21WingPylon 1";
        as51[17] = "BombGunFAB250m46 1";
        as51[18] = "BombGunFAB250m46 1";
        Aircraft.weaponsRegister(class1, "dt_2xFAB250_RATO", as51);
        String as52[] = new String[54];
        as52[0] = "MGunNull 1";
        as52[3] = "MiG21Pylon 1";
        as52[4] = "FuelTankGun_PT490 1";
        as52[5] = "MiG21WingPylon 1";
        as52[6] = "MiG21WingPylon 1";
        as52[17] = "BombGunZB360 1";
        as52[18] = "BombGunZB360 1";
        Aircraft.weaponsRegister(class1, "dt_2xZB360_RATO", as52);
        String as53[] = new String[54];
        as53[0] = "MGunNull 1";
        as53[3] = "MiG21Pylon 1";
        as53[4] = "FuelTankGun_PT490 1";
        as53[5] = "MiG21WingPylon 1";
        as53[6] = "MiG21WingPylon 1";
        as53[19] = "PylonUB16 1";
        as53[20] = "PylonUB16 1";
        as53[21] = "RocketGunS5 1";
        as53[22] = "RocketGunS5 1";
        as53[23] = "RocketGunS5 1";
        as53[24] = "RocketGunS5 1";
        as53[25] = "RocketGunS5 1";
        as53[26] = "RocketGunS5 1";
        as53[27] = "RocketGunS5 1";
        as53[28] = "RocketGunS5 1";
        as53[29] = "RocketGunS5 1";
        as53[30] = "RocketGunS5 1";
        as53[31] = "RocketGunS5 1";
        as53[32] = "RocketGunS5 1";
        as53[33] = "RocketGunS5 1";
        as53[34] = "RocketGunS5 1";
        as53[35] = "RocketGunS5 1";
        as53[36] = "RocketGunS5 1";
        as53[37] = "RocketGunS5 1";
        as53[38] = "RocketGunS5 1";
        as53[39] = "RocketGunS5 1";
        as53[40] = "RocketGunS5 1";
        as53[41] = "RocketGunS5 1";
        as53[42] = "RocketGunS5 1";
        as53[43] = "RocketGunS5 1";
        as53[44] = "RocketGunS5 1";
        as53[45] = "RocketGunS5 1";
        as53[46] = "RocketGunS5 1";
        as53[47] = "RocketGunS5 1";
        as53[48] = "RocketGunS5 1";
        as53[49] = "RocketGunS5 1";
        as53[50] = "RocketGunS5 1";
        as53[51] = "RocketGunS5 1";
        as53[52] = "RocketGunS5 1";
        Aircraft.weaponsRegister(class1, "dt_2xUBI16_RATO", as53);
        String as54[] = new String[54];
        as54[0] = "MGunNull 1";
        as54[3] = "MiG21Pylon 1";
        as54[4] = "FuelTankGun_PT490 1";
        as54[5] = "MiG21WingPylon 1";
        as54[6] = "MiG21WingPylon 1";
        as54[11] = "RocketGunS24 1";
        as54[12] = "RocketGunNull 1";
        as54[13] = "RocketGunS24 1";
        as54[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "dt_rocketS24_RATO", as54);
        String as55[] = new String[54];
        as55[0] = "MGunGSh23Ls 100";
        as55[1] = "MGunGSh23Ls 100";
        as55[2] = "PylonGP9 1";
        Aircraft.weaponsRegister(class1, "gunpod_RATO", as55);
        String as56[] = new String[54];
        as56[0] = "MGunGSh23Ls 100";
        as56[1] = "MGunGSh23Ls 100";
        as56[2] = "PylonGP9 1";
        as56[5] = "MiG21WingPylon 1";
        as56[6] = "MiG21WingPylon 1";
        as56[7] = "RocketGunK13A 1";
        as56[8] = "RocketGunNull 1";
        as56[9] = "RocketGunK13A 1";
        as56[10] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketK13_RATO", as56);
        String as57[] = new String[54];
        as57[0] = "MGunGSh23Ls 100";
        as57[1] = "MGunGSh23Ls 100";
        as57[2] = "PylonGP9 1";
        as57[5] = "MiG21WingPylon 1";
        as57[6] = "MiG21WingPylon 1";
        as57[11] = "RocketGunK5M 1";
        as57[12] = "RocketGunNull 1";
        as57[13] = "RocketGunK5M 1";
        as57[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketK5M_RATO", as57);
        String as58[] = new String[54];
        as58[0] = "MGunGSh23Ls 100";
        as58[1] = "MGunGSh23Ls 100";
        as58[2] = "PylonGP9 1";
        as58[5] = "MiG21WingPylon 1";
        as58[6] = "MiG21WingPylon 1";
        as58[11] = "RocketGunK5Mf 1";
        as58[12] = "RocketGunNull 1";
        as58[13] = "RocketGunK5Mf 1";
        as58[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketK5Mf_RATO", as58);
        String as59[] = new String[54];
        as59[0] = "MGunGSh23Ls 100";
        as59[1] = "MGunGSh23Ls 100";
        as59[2] = "PylonGP9 1";
        as59[5] = "MiG21WingPylon 1";
        as59[6] = "MiG21WingPylon 1";
        as59[11] = "RocketGunR55 1";
        as59[12] = "RocketGunNull 1";
        as59[13] = "RocketGunR55 1";
        as59[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketR55_RATO", as59);
        String as60[] = new String[54];
        as60[0] = "MGunGSh23Ls 100";
        as60[1] = "MGunGSh23Ls 100";
        as60[2] = "PylonGP9 1";
        as60[5] = "MiG21WingPylon 1";
        as60[6] = "MiG21WingPylon 1";
        as60[11] = "RocketGunR55f 1";
        as60[12] = "RocketGunNull 1";
        as60[13] = "RocketGunR55f 1";
        as60[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketR55f_RATO", as60);
        String as61[] = new String[54];
        as61[0] = "MGunGSh23Ls 100";
        as61[1] = "MGunGSh23Ls 100";
        as61[2] = "PylonGP9 1";
        as61[5] = "MiG21WingPylon 1";
        as61[6] = "MiG21WingPylon 1";
        as61[17] = "BombGunFAB100 1";
        as61[18] = "BombGunFAB100 1";
        Aircraft.weaponsRegister(class1, "gp_2xFAB100_RATO", as61);
        String as62[] = new String[54];
        as62[0] = "MGunGSh23Ls 100";
        as62[1] = "MGunGSh23Ls 100";
        as62[2] = "PylonGP9 1";
        as62[5] = "MiG21WingPylon 1";
        as62[6] = "MiG21WingPylon 1";
        as62[17] = "BombGunFAB250m46 1";
        as62[18] = "BombGunFAB250m46 1";
        Aircraft.weaponsRegister(class1, "gp_2xFAB250_RATO", as62);
        String as63[] = new String[54];
        as63[0] = "MGunGSh23Ls 100";
        as63[1] = "MGunGSh23Ls 100";
        as63[2] = "PylonGP9 1";
        as63[5] = "MiG21WingPylon 1";
        as63[6] = "MiG21WingPylon 1";
        as63[17] = "BombGunZB360 1";
        as63[18] = "BombGunZB360 1";
        Aircraft.weaponsRegister(class1, "gp_2xZB360_RATO", as63);
        String as64[] = new String[54];
        as64[0] = "MGunGSh23Ls 100";
        as64[1] = "MGunGSh23Ls 100";
        as64[2] = "PylonGP9 1";
        as64[5] = "MiG21WingPylon 1";
        as64[6] = "MiG21WingPylon 1";
        as64[15] = "FuelTankGun_PT490 1";
        as64[16] = "FuelTankGun_PT490 1";
        Aircraft.weaponsRegister(class1, "gp_2xdroptank_RATO", as64);
        String as65[] = new String[54];
        as65[0] = "MGunNull 1";
        as65[3] = "MiG21Pylon 1";
        as65[4] = "FuelTankGun_PT490 1";
        as65[5] = "MiG21WingPylon 1";
        as65[6] = "MiG21WingPylon 1";
        as65[19] = "PylonUB16 1";
        as65[20] = "PylonUB16 1";
        as65[21] = "RocketGunS5 1";
        as65[22] = "RocketGunS5 1";
        as65[23] = "RocketGunS5 1";
        as65[24] = "RocketGunS5 1";
        as65[25] = "RocketGunS5 1";
        as65[26] = "RocketGunS5 1";
        as65[27] = "RocketGunS5 1";
        as65[28] = "RocketGunS5 1";
        as65[29] = "RocketGunS5 1";
        as65[30] = "RocketGunS5 1";
        as65[31] = "RocketGunS5 1";
        as65[32] = "RocketGunS5 1";
        as65[33] = "RocketGunS5 1";
        as65[34] = "RocketGunS5 1";
        as65[35] = "RocketGunS5 1";
        as65[36] = "RocketGunS5 1";
        as65[37] = "RocketGunS5 1";
        as65[38] = "RocketGunS5 1";
        as65[39] = "RocketGunS5 1";
        as65[40] = "RocketGunS5 1";
        as65[41] = "RocketGunS5 1";
        as65[42] = "RocketGunS5 1";
        as65[43] = "RocketGunS5 1";
        as65[44] = "RocketGunS5 1";
        as65[45] = "RocketGunS5 1";
        as65[46] = "RocketGunS5 1";
        as65[47] = "RocketGunS5 1";
        as65[48] = "RocketGunS5 1";
        as65[49] = "RocketGunS5 1";
        as65[50] = "RocketGunS5 1";
        as65[51] = "RocketGunS5 1";
        as65[52] = "RocketGunS5 1";
        Aircraft.weaponsRegister(class1, "dt_2xUBI16_RATO", as65);
        String as66[] = new String[54];
        as66[0] = "MGunGSh23Ls 100";
        as66[1] = "MGunGSh23Ls 100";
        as66[2] = "PylonGP9 1";
        as66[5] = "MiG21WingPylon 1";
        as66[6] = "MiG21WingPylon 1";
        as66[11] = "RocketGunS24 1";
        as66[12] = "RocketGunNull 1";
        as66[13] = "RocketGunS24 1";
        as66[14] = "RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "gp_rocketS24_RATO", as66);
        String as67[] = new String[54];
        as67[3] = "MiG21Pylon 1";
        as67[4] = "FuelTankGun_PT490 1";
        as67[5] = "MiG21WingPylon 1";
        as67[6] = "MiG21WingPylon 1";
        as67[15] = "FuelTankGun_PT490 1";
        as67[16] = "FuelTankGun_PT490 1";
        Aircraft.weaponsRegister(class1, "3xdroptank_RATO", as67);
        Aircraft.weaponsRegister(class1, "none", new String[54]);
    }
}
