// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 14/10/2015 10:00:11 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   F84F1.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F84, Aircraft, PaintSchemeFMPar06, TypeGuidedMissileCarrier, 
//            TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeFastJet, 
//            Cockpit, NetAircraft

public class F84F1 extends F84
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeFastJet
{

    public F84F1()
    {
        guidedMissileUtils = null;
        trgtPk = 0.0F;
        trgtAI = null;
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
        rocketsList = new ArrayList();
        bToFire = false;
        tX4Prev = 0L;
        guidedMissileUtils = new GuidedMissileUtils(this);
        APmode1 = false;
        APmode2 = false;
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

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -70F), 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.7F, 0.0F, -100F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.1F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.1F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, 81F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.7F, 0.0F, -81F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, 88F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.7F, 0.0F, -88F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.025F, 0.0F, -110F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.025F, 0.0F, 110F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public void moveWheelSink()
    {
        float f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F, 40F * f);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 30F * f, 0.0F);
        if(FM.CT.GearControl > 0.5F)
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -60F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = 55F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, f1);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, f1);
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

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(1.5F, 1.5F, 1.0F, 2.0F, 2.0F, 2.0F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public Point3f getMissileTargetOffset()
    {
        return guidedMissileUtils.getSelectedActorOffset();
    }

    public boolean hasMissiles()
    {
        return !rocketsList.isEmpty();
    }

    public void shotMissile()
    {
        if(hasMissiles())
            rocketsList.remove(0);
    }

    public int getMissileLockState()
    {
        return guidedMissileUtils.getMissileLockState();
    }

    private float getMissilePk()
    {
        float f = 0.0F;
        guidedMissileUtils.setMissileTarget(guidedMissileUtils.lookForGuidedMissileTarget(((Interpolate) (super.FM)).actor, guidedMissileUtils.getMaxPOVfrom(), guidedMissileUtils.getMaxPOVto(), guidedMissileUtils.getPkMaxDist()));
        if(Actor.isValid(guidedMissileUtils.getMissileTarget()))
            f = guidedMissileUtils.Pk(((Interpolate) (super.FM)).actor, guidedMissileUtils.getMissileTarget());
        return f;
    }

    private void checkAIlaunchMissile()
    {
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
            return;
        if(rocketsList.isEmpty())
            return;
        Pilot pilot = (Pilot)super.FM;
        if((pilot.get_maneuver() == 27 || pilot.get_maneuver() == 62 || pilot.get_maneuver() == 63) && ((Maneuver) (pilot)).target != null)
        {
            trgtAI = ((Interpolate) (((Maneuver) (pilot)).target)).actor;
            if(!Actor.isValid(trgtAI) || !(trgtAI instanceof Aircraft))
                return;
            bToFire = false;
            if(trgtPk > 25F && Actor.isValid(guidedMissileUtils.getMissileTarget()) && (guidedMissileUtils.getMissileTarget() instanceof Aircraft) && guidedMissileUtils.getMissileTarget().getArmy() != ((Interpolate) (super.FM)).actor.getArmy() && Time.current() > tX4Prev + 10000L)
            {
                bToFire = true;
                tX4Prev = Time.current();
                shootRocket();
                bToFire = false;
            }
        }
    }

    public void shootRocket()
    {
        if(rocketsList.isEmpty())
        {
            return;
        } else
        {
            ((RocketGun)rocketsList.get(0)).shots(1);
            return;
        }
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        rocketsList.clear();
        guidedMissileUtils.createMissileList(rocketsList);
        if(Config.isUSE_RENDER())
        {
            turbo = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            turbosmoke = Eff3DActor.New(this, findHook("_Engine1ES_01"), null, 1.0F, "3DO/Effects/Aircraft/GraySmallTSPD.eff", -1F);
            afterburner = Eff3DActor.New(this, findHook("_Engine1EF_02"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurner.eff", -1F);
            Eff3DActor.setIntesity(turbo, 0.0F);
            Eff3DActor.setIntesity(turbosmoke, 0.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
        }
        guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        setSubsonicLimiter();
        super.update(f);
        trgtPk = getMissilePk();
        guidedMissileUtils.checkLockStatus();
        checkAIlaunchMissile();
        if(((FlightModelMain) (super.FM)).AS.isMaster() && Config.isUSE_RENDER())
        {
            if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.45F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 6)
            {
                if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.65F)
                {
                    if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 1.001F)
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 3);
                    else
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 2);
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 1);
                }
            } else
            if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() <= 0.45F || ((FlightModelMain) (super.FM)).EI.engines[0].getStage() < 6)
                ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 0);
            setExhaustFlame(Math.round(Aircraft.cvt(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
        }
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.91000000000000003D && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 0.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 15000D;
    }

    public void doSetSootState(int i, int j)
    {
        switch(j)
        {
        case 0: // '\0'
            Eff3DActor.setIntesity(turbo, 0.0F);
            Eff3DActor.setIntesity(turbosmoke, 0.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
            break;

        case 1: // '\001'
            Eff3DActor.setIntesity(turbo, 0.0F);
            Eff3DActor.setIntesity(turbosmoke, 1.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
            break;

        case 2: // '\002'
            Eff3DActor.setIntesity(turbo, 1.0F);
            Eff3DActor.setIntesity(turbosmoke, 1.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
            break;

        case 3: // '\003'
            Eff3DActor.setIntesity(turbo, 1.0F);
            Eff3DActor.setIntesity(turbosmoke, 1.0F);
            Eff3DActor.setIntesity(afterburner, 1.0F);
            break;
        }
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 0.9F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    private void setSubsonicLimiter()
    {
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.78000000000000003D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx -= 0.003F;
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.91000000000000003D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).Sq.dragParasiteCx -= 0.002F;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(true);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(false);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = true;
            }
    }

//    static Class _mthclass$(String s)
//    {
//        try
//        {
//            return Class.forName(s);
//        }
//        catch(ClassNotFoundException classnotfoundexception)
//        {
//            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
//        }
//    }
//
//    static Class _mthclass$(String s)
//    {
//        try
//        {
//            return Class.forName(s);
//        }
//        catch(ClassNotFoundException classnotfoundexception)
//        {
//            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
//        }
//    }

    private GuidedMissileUtils guidedMissileUtils;
    private float trgtPk;
    private Actor trgtAI;
    private Eff3DActor turbo;
    private Eff3DActor turbosmoke;
    private Eff3DActor afterburner;
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
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    private ArrayList rocketsList;
    public boolean bToFire;
    private long tX4Prev;
    public boolean APmode1;
    public boolean APmode2;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.F84F1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F84F");
        Property.set(class1, "meshName", "3DO/Plane/F84F/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "meshName_us", "3DO/Plane/F84F(US)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/F84F1.fmd:F84_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF84F1.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 
            9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 9, 9, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", 
            "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalBomb05", "_ExternalBomb03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", 
            "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", 
            "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalDev15", "_ExternalDev16", "_ExternalDev13", "_ExternalDev14"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 48;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x500lbs";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x500lbs+02x250lbs";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x1000lbs";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x1000lbs+02x250lbs";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x500lbs+08xHVAR";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x1000lbs+08xHVAR";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x75gal_napalm+08xHVAR";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x75gal_napalm+02x250lbs";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "20xHVAR";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02xTinyTim";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunTinyTim", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunTinyTim", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02xTinyTim+08xHVAR";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunTinyTim", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunTinyTim", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x230gal_tank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "04x230gal_tank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x450gal_tank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank450gal", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank450gal", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x450gal_tank+02x230gal_tank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank450gal", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank450gal", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int i = 0; i < byte0; i++)
                a_lweaponslot[i] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
