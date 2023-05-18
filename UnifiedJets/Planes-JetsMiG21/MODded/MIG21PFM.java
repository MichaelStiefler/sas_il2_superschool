////////////////////////////////////////////////////////////////////////////////////////
// By PAL, from MiG-21 Source - *1st Generation*
// Removed MoveSteering(float f) method to SuperClass, for proper Passive Steering (DiffBrakes=3)
// Fail Safe version of SirenaWarning(), now is Range dependant SirenaWarning(float range)
// Method playSirenaWarning was not doing anything. Corrected.   
////////////////////////////////////////////////////////////////////////////////////////

// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/10/2015 02:13:06 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MIG21PFM.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            MIG_21, Chute, PaintSchemeFMParMiG21, TypeGuidedMissileCarrier, 
//            TypeCountermeasure, TypeThreatDetector, Cockpit, NetAircraft, 
//            Aircraft, EjectionSeat

public class MIG21PFM extends MIG_21
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public MIG21PFM()
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
        computeR11F2S300_AB();
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

    public void computeR11F2S300_AB()
    {
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 22800D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            if(f > 17F)
            {
                f1 = 20F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = (0.0329824F * f3 - 0.647042F * f2) + 2.64429F * f;
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
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private int freq;
    public float Timer1;
    public float Timer2;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.MIG21PFM.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG21");
        Property.set(class1, "meshName", "3DO/Plane/MiG-21PFM/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-21PFM.fmd:MIG21");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMIG21PFM.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 9, 2, 2, 2, 
            2, 2, 2, 2, 2, 9, 9, 3, 3, 9, 
            9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", 
            "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev06", "_ExternalDev07", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev08", 
            "_ExternalDev09", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", 
            "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", 
            "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", 
            "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 57;
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
            s = "2x Kh-66";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
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
            s = "2x Kh-66 + 1x PTB-490";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
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
            s = "1xGP-1 Gunpod";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "R-3A + 1xGP-1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RS-2-US + 1xGP-1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RS-2-US (flare) + 1xGP-1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Kh-66 + 1xGP-1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-100 + 1xGP-1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-250 + 1xGP-1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x ZB-360 + 1xGP-1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x GP-1 + 2x PTB-490";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x UBI-16-7U + 1xGP-1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
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
            s = "2x S-24 Rockets + 1xGP-1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
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
            s = "Default + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 100);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x R-3A + RATO";
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
            s = "2x RS-2-US + RATO";
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
            s = "2x RS-2-US (flare) + RATO";
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
            s = "2x Kh-66 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-100 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-250 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x ZB-360 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x UBI-16-7U + RATO";
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
            s = "2x S-24 Rockets + RATO";
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
            s = "1x PTB-490 Droptank + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x R-3A + 1x PTB-490 + RATO";
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
            s = "2x RS-2-US + 1x PTB-490 + RATO";
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
            s = "2x RS-2-US (flare) + 1x PTB-490 + RATO";
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
            s = "2x Kh-66 + 1x PTB-490 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-100 + 1x PTB-490 + RATO";
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
            s = "2x FAB-250 + 1x PTB-490 + RATO";
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
            s = "2x ZB-360 + 1x PTB-490 + RATO";
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
            s = "2x UBI-16-7U + 1x PTB-490 + RATO";
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
            s = "2x S-24 Rockets + 1x PTB-490 + RATO";
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
            s = "1xGP-1 Gunpod + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "R-3A + 1xGP-1 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RS-2-US + 1xGP-1 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RS-2-US (flare) + 1xGP-1 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Kh-66 + 1xGP-1 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-100 + 1xGP-1 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB100", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-250 + 1xGP-1 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x ZB-360 + 1xGP-1 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x GP-1 + 2x PTB-490 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x UBI-16-7U + 1xGP-1 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
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
            s = "2x S-24 Rockets + 1xGP-1 + RATO";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x PTB-490 Droptank + RATO";
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
