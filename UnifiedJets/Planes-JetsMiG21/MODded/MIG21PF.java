////////////////////////////////////////////////////////////////////////////////////////
// By PAL, from MiG-21 Source - *1st Generation*
// Removed MoveSteering(float f) method to SuperClass, for proper Passive Steering (DiffBrakes=3)
// Fail Safe version of SirenaWarning(), now is Range dependant SirenaWarning(float range)
// Method playSirenaWarning was not doing anything. Corrected.   
////////////////////////////////////////////////////////////////////////////////////////

// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/10/2015 02:13:25 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MIG21PF.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            MIG_21, Chute, PaintSchemeFMParMiG21, TypeGuidedMissileCarrier, 
//            TypeCountermeasure, TypeThreatDetector, Aircraft, Cockpit, 
//            NetAircraft

public class MIG21PF extends MIG_21
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public MIG21PF()
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
        computeR11F2300_AB();
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

    public void computeR11F2300_AB()
    {
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 21000D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            if((double)f > 17D)
            {
                f1 = 20F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = (0.0385625F * f3 - 0.758644F * f2) + 2.92887F * f;
            }
        FM.producedAF.x -= f1 * 1000F;
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
    private int freq;
    public float Timer1;
    public float Timer2;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.MIG21PF.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG21");
        Property.set(class1, "meshName", "3DO/Plane/MiG-21PF/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-21PF.fmd:MIG21");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMIG21PF.class
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
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 53;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 100);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x R-3A";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RS-2-US";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RS-2-US (flare)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-100";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x ZB-360";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x UBI-16-7U Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x S-24 Rockets";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x PTB-490 Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x R-3A + 1x PTB-490";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RS-2-US + 1x PTB-490";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RS-2-US (flare) + 1x PTB-490";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-100 + 1x PTB-490";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-250 + 1x PTB-490";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x ZB-360 + 1x PTB-490";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x UBI-16-7U + 1x PTB-490";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x S-24 Rockets + 1x PTB-490";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x PTB-490 Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
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
