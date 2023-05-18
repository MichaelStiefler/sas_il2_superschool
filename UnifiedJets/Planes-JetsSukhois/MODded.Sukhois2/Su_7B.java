////////////////////////////////////////////////////////////////////////////////////////
// By PAL, from Vega Sukhoi Pack v2
// Corrected MoveSteering to match with normal settings on Gear.class
// Pend: Update Sirena Warning
////////////////////////////////////////////////////////////////////////////////////////

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Su_7B.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.FuelTankGun_Tank19;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Sukhoi, Aircraft, TypeFighterAceMaker, TypeRadarGunsight, 
//            Chute, PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, 
//            TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, 
//            NetAircraft

public class Su_7B extends Sukhoi
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump
{

    public Su_7B()
    {
        fxSirena = newSound("aircraft.Sirena2", false);
        smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        guidedMissileUtils = null;
        sirenaSoundPlaying = false;
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
        removeChuteTimer = -1L;
        smplSirena.setInfinite(true);
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
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

//By PAL, user SuperClass method:
//    private boolean sirenaWarning()
//    {
//        Point3d point3d = new Point3d();
//        super.pos.getAbs(point3d);
//        Vector3d vector3d = new Vector3d();
//        Aircraft aircraft = World.getPlayerAircraft();
//        if(World.getPlayerAircraft() == null)
//            return false;
//        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
//        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
//        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
//        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
//        if(i < 0)
//            i += 360;
//        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
//        if(j < 0)
//            j += 360;
//        Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
//        if((aircraft1 instanceof Aircraft) && aircraft1.getArmy() != World.getPlayerArmy() && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && aircraft1 != World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
//        {
//            super.pos.getAbs(point3d);
//            double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
//            double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
//            double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
//            new String();
//            new String();
//            double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
//            new String();
//            double d7 = d3 - d;
//            double d8 = d4 - d1;
//            float f = 57.32484F * (float)Math.atan2(d8, -d7);
//            int k = (int)(Math.floor((int)f) - 90D);
//            if(k < 0)
//                k += 360;
//            int l = k - i;
//            double d9 = d - d3;
//            double d10 = d1 - d4;
//            double d11 = Math.sqrt(d6 * d6);
//            int i1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
//            float f1 = 57.32484F * (float)Math.atan2(i1, d11);
//            int j1 = (int)(Math.floor((int)f1) - 90D);
//            if(j1 < 0)
//                j1 += 360;
//            int k1 = j1 - j;
//            int l1 = (int)(Math.ceil(((double)i1 * 3.2808399000000001D) / 100D) * 100D);
//            if(l1 >= 5280)
//                l1 = (int)Math.floor(l1 / 5280);
//            bRadarWarning = (double)i1 <= 3000D && (double)i1 >= 50D && k1 >= 195 && k1 <= 345 && Math.sqrt(l * l) >= 120D;
//            playSirenaWarning(bRadarWarning);
//        } else
//        {
//            bRadarWarning = false;
//            playSirenaWarning(bRadarWarning);
//        }
//        return true;
//    }

//By PAL, user SuperClass method:
//    public void playSirenaWarning(boolean flag)
//    {
//        if(flag && !sirenaSoundPlaying)
//        {
//            fxSirena.play(smplSirena);
//            sirenaSoundPlaying = true;
//            HUD.log(AircraftHotKeys.hudLogWeaponId, "AN/APR-36: Enemy at Six!");
//        } else
//        if(!flag && sirenaSoundPlaying)
//        {
//            fxSirena.cancel();
//            sirenaSoundPlaying = false;
//        }
//    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(1.5F, 1.5F, 1.5F, 2.2F, 2.0F, 1.5F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        droptank();
        guidedMissileUtils.onAircraftLoaded();
        super.FM.Skill = 3;
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        if(super.thisWeaponsName.endsWith("P1"))
        {
            hierMesh().chunkVisible("PylonTL", true);
            hierMesh().chunkVisible("PylonTR", true);
        }
        if(super.thisWeaponsName.endsWith("P2"))
        {
            hierMesh().chunkVisible("PylonTL", true);
            hierMesh().chunkVisible("PylonTR", true);
            hierMesh().chunkVisible("PylonML", true);
            hierMesh().chunkVisible("PylonMR", true);
        }
        if(super.thisWeaponsName.endsWith("P3"))
        {
            hierMesh().chunkVisible("PylonML", true);
            hierMesh().chunkVisible("PylonMR", true);
        }
    }

    private final void doRemovedroptankL()
    {
        if(hierMesh().chunkFindCheck("DroptankL") != -1)
        {
            hierMesh().hideSubTrees("DroptankL");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("DroptankL"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemovedroptankR()
    {
        if(hierMesh().chunkFindCheck("DroptankR") != -1)
        {
            hierMesh().hideSubTrees("DroptankR");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("DroptankR"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }

    private void droptank()
    {
        for(int i = 0; i < ((FlightModelMain) (super.FM)).CT.Weapons.length; i++)
            if(((FlightModelMain) (super.FM)).CT.Weapons[i] != null)
            {
                for(int j = 0; j < ((FlightModelMain) (super.FM)).CT.Weapons[i].length; j++)
                    if(((FlightModelMain) (super.FM)).CT.Weapons[i][j].haveBullets() && (((FlightModelMain) (super.FM)).CT.Weapons[i][j] instanceof FuelTankGun_Tank19))
                    {
                        havedroptank = true;
                        super.hierMesh().chunkVisible("DroptankL", true);
                        super.hierMesh().chunkVisible("DroptankR", true);
                    }

            }

    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        //By PAL, played in SuperClass, redundant here
        //sirenaWarning(4000F);
        computeAL7F1_AB();
        super.update(f);
        if(havedroptank && !((FlightModelMain) (super.FM)).CT.Weapons[9][1].haveBullets())
        {
            havedroptank = false;
            doRemovedroptankL();
            doRemovedroptankR();
        }
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF86/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-5D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
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

//    public void moveSteering(float f)
//    {
//        if((double)((FlightModelMain) (super.FM)).CT.getGear() < 0.98999999999999999D)
//            hierMesh().chunkSetAngles("GearC_D0", 0.0F * f, 0.0F, 0.0F);
//        else
//            hierMesh().chunkSetAngles("GearC_D0", 30F * f, 0.0F, 0.0F);
//    }
    
    public void moveSteering(float f)
    {
        if(((FlightModelMain) (super.FM)).CT.GearControl > 0.9F)
        {
			//By PAL, limited to twist 30 degrees only.
			if(f < -30F)
	    		hierMesh().chunkSetAngles("GearC_D0", -30F, 0.0F, 0.0F);
	    	else
			if(f > 30F)
	    		hierMesh().chunkSetAngles("GearC_D0", 30F, 0.0F, 0.0F);
	    	else
	    		hierMesh().chunkSetAngles("GearC_D0", f, 0.0F, 0.0F);         		        	
        }
        else
        	//By PAL, to align wheel on position (theorically not required because it is implemented in MoveGear)
            hierMesh().chunkSetAngles("GearC_D0", 0.0F, 0.0F, 0.0F);
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

    public void computeAL7F1_AB()
    {
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 24000D;
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

    private GuidedMissileUtils guidedMissileUtils;
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
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
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    public boolean bToFire;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private boolean havedroptank;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.Su_7B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-7");
        Property.set(class1, "meshName", "3DO/Plane/Su-7B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1961F);
        Property.set(class1, "yearExpired", 1986F);
        Property.set(class1, "FlightModel", "FlightModels/Su-7B.fmd:Sukhoi_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSu_7.class, com.maddox.il2.objects.air.CockpitSu_7Bombardier.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 3, 3, 9, 9, 
            2, 2, 9, 9, 9, 9, 9, 9, 3, 3, 
            9, 9, 9, 9, 2, 2, 9, 9, 9, 9, 
            9, 9, 9, 9, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_Gun01", "_Gun02", "_ExternalDev01", "_ExternalDev02", "_ExternalTank01", "_ExternalTank02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev03", "_ExternalDev04", 
            "_Rocket01", "_Rocket02", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalTank03", "_ExternalTank04", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_Rocket03", "_Rocket04", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", 
            "_ExternalDev17", "_ExternalDev18", "_ExternalDev19", "_ExternalDev20", "_Rocket05", "_Rocket06"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 36;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Droptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-250m46";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RBK-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x ZB-360";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x UB-16 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x UB-32 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x Droptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 2x FAB250m46";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 2x FAB250m46 + 2x UB-16 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 2x RBK-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 2x RBK-250 + 2x UB-16 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 2x ZB-360";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 2x ZB-360 + 2x ORO57 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 2x FAB-500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 2x FAB-500 + 2x ORO57 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x FAB250m46";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x FAB250m46 + 2x UB-16 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x RBK-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x RBK-250 + 2x UB-16 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x ZB-360";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "BombGunZB360", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "BombGunZB360", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x FAB-500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "BombGunFAB500", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "BombGunFAB500", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x ZB-360 + 2x ORO-57 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "BombGunZB360", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "BombGunZB360", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x FAB-500 + 2x ORO-57 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "BombGunFAB500", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "BombGunFAB500", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x DT+ 2x UB-16 Rocket Pods ";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x DT+ 2x UB-32 Rocket Pods ";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x DT+ 2x ORO57 Rocket Pods ";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x DT+ 4x ORO57 Rocket Pods ";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 2x UB-16 + 2x ORO57 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 4x UB-16 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x Dt + 2x UB-32 + 2x UB-16 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x Dt + 2x UB-16 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-250m46 + 2x UB-16 +2x ORO-57 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-250m46 + 2x UB-32 +2x UB-16 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RBK-250 + 2x UB-16 +2x ORO-57 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x RBK-250 + 2x UB-32 +2x UB-16 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x ZB-360 + 2x UB-16 +2x ORO-57 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x ZB-360 + 2x UB-32 +2x UB-16 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-500 + 2x UB-16 +2x ORO-57 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x FAB-500 + 2x UB-32 +2x UB-16 Rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB500", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x UB-16 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x UB-16 + 2x ORO57 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x UB-32 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x UB-16 + 2x ORO57 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x UB-32 + 2x UB-16 + 2x ORO57 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 8);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x UB-16 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x UB-32 + 4x UB-16 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x UB-32 + 2x UB-16 Rocket Pods";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNR30ki2", 80);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunNR30ki3", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
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
